/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.SearchCursor;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.DefaultModification;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.exception.LdapNoSuchObjectException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.util.Strings;
import org.apache.directory.fortress.core.CreateException;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.RemoveException;
import org.apache.directory.fortress.core.UpdateException;
import org.apache.directory.fortress.core.ldap.LdapDataProvider;
import org.apache.directory.fortress.core.model.Graphable;
import org.apache.directory.fortress.core.model.ObjectFactory;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class provides dataaccess to the OrgUnit datasets in LDAP.
 * <p>
 * The OrgUnitDAO maintains the following structural and aux object classes:
 * <h4>1. organizationalUnit Structural Object Class is used to store basic attributes like ou and description</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.5.6.5 NAME 'organizationalUnit'</code>
 * <li> <code>DESC 'RFC2256: an organizational unit'</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST ou</code>
 * <li> <code>MAY ( userPassword $ searchGuide $ seeAlso $ businessCategory $</code>
 * <li> <code>x121Address $ registeredAddress $ destinationIndicator $</code>
 * <li> <code>preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $</code>
 * <li> <code>telephoneNumber $ internationaliSDNNumber $</code>
 * <li> <code>facsimileTelephoneNumber $ street $ postOfficeBox $ postalCode $</code>
 * <li> <code>postalAddress $ physicalDeliveryOfficeName $ st $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>2. ftOrgUnit Structural objectclass is used to store the OrgUnit internal id</h4>
 * <ul>                                                              org.apache.directory.fortress.arbac.
 * <li>  ------------------------------------------
 * <li> <code> objectclass    ( 1.3.6.1.4.1.38088.2.6</code>
 * <li> <code>NAME 'ftOrgUnit'</code>
 * <li> <code>DESC 'Fortress OrgUnit Class'</code>
 * <li> <code>SUP organizationalunit</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>3. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity</h4>
 * <ul>
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.4</code>
 * <li> <code>NAME 'ftMods'</code>
 * <li> <code>DESC 'Fortress Modifiers AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY (</code>
 * <li> <code>ftModifier $</code>
 * <li> <code>ftModCode $</code>
 * <li> <code>ftModId ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @created September 18, 2010
 */
final class OrgUnitDAO extends LdapDataProvider
{
    private static final String CLS_NM = OrgUnitDAO.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final String ORGUNIT_OBJECT_CLASS_NM = "ftOrgUnit";

    private static final String ORGUNIT_OBJ_CLASS[] =
        {
            SchemaConstants.TOP_OC, ORGUNIT_OBJECT_CLASS_NM, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };
    private static final String[] ORGUNIT_ATRS =
        {
            GlobalIds.FT_IID, SchemaConstants.OU_AT, SchemaConstants.DESCRIPTION_AT, GlobalIds.PARENT_NODES
    };

    private static final String[] ORGUNIT_ATR =
        {
            SchemaConstants.OU_AT
    };

    /**
     * @param entity
     * @return
     * @throws org.apache.directory.fortress.core.CreateException
     *
     */
    OrgUnit create( OrgUnit entity ) throws CreateException
    {
        LdapConnection ld = null;
        Dn dn = getDn( entity );

        try
        {
            Entry entry = new DefaultEntry( dn );
            entry.add( SchemaConstants.OBJECT_CLASS_AT, ORGUNIT_OBJ_CLASS );
            entity.setId();
            entry.add( GlobalIds.FT_IID, entity.getId() );

            String description = entity.getDescription();

            if ( !Strings.isEmpty( description ) )
            {
                entry.add( SchemaConstants.DESCRIPTION_AT, description );
            }

            // organizational name requires OU attribute:
            entry.add( SchemaConstants.OU_AT, entity.getName() );

            // These multi-valued attributes are optional.  The utility function will return quietly if no items are loaded into collection:
            loadAttrs( entity.getParents(), entry, GlobalIds.PARENT_NODES );

            ld = getAdminConnection();
            add( ld, entry, entity );
        }
        catch ( LdapException e )
        {
            String error = "create orgUnit name [" + entity.getName() + "] type [" + entity.getType()
                + "] root [" + dn + "] caught LdapException=" + e;
            int errCode;

            if ( entity.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_ADD_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_ADD_FAILED_USER;

            }

            throw new CreateException( errCode, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     * @param entity
     * @return
     * @throws org.apache.directory.fortress.core.UpdateException
     *
     */
    OrgUnit update( OrgUnit entity ) throws UpdateException
    {
        LdapConnection ld = null;
        Dn dn = getDn( entity );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();

            if ( entity.getDescription() != null && entity.getDescription().length() > 0 )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE, SchemaConstants.DESCRIPTION_AT, entity.getDescription() ) );
            }

            loadAttrs( entity.getParents(), mods, GlobalIds.PARENT_NODES );

            if ( mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, dn, mods, entity );
            }
        }
        catch ( LdapException e )
        {
            String error = "update orgUnit name [" + entity.getName() + "] type [" + entity.getType()
                + "] root [" + dn + "] caught LdapException=" + e;
            int errCode;

            if ( entity.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_UPDATE_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_UPDATE_FAILED_USER;
            }

            throw new UpdateException( errCode, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     * @param entity
     * @throws org.apache.directory.fortress.core.UpdateException
     *
     */
    void deleteParent( OrgUnit entity ) throws UpdateException
    {
        LdapConnection ld = null;
        Dn dn = getDn( entity );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();
            mods.add( new DefaultModification( ModificationOperation.REMOVE_ATTRIBUTE, GlobalIds.PARENT_NODES ) );
            ld = getAdminConnection();
            modify( ld, dn, mods, entity );
        }
        catch ( LdapException e )
        {
            String error = "deleteParent orgUnit name [" + entity.getName() + "] type [" + entity.getType()
                + "] root [" + dn + "] caught LdapException=" + e;
            int errCode;

            if ( entity.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_REMOVE_PARENT_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_REMOVE_PARENT_FAILED_USER;
            }

            throw new UpdateException( errCode, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param entity
     * @return
     * @throws org.apache.directory.fortress.core.RemoveException
     *
     */
    OrgUnit remove( OrgUnit entity ) throws RemoveException
    {
        LdapConnection ld = null;
        Dn dn = getDn( entity );

        try
        {
            ld = getAdminConnection();
            delete( ld, dn, entity );
        }
        catch ( LdapException e )
        {
            String error = "remove orgUnit name [" + entity.getName() + "] type [" + entity.getType()
                + "] root [" + dn + "] caught LdapException=" + e;
            int errCode;

            if ( entity.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_DELETE_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_DELETE_FAILED_USER;
            }

            throw new RemoveException( errCode, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     * @param entity
     * @return
     * @throws FinderException
     *
     */
    OrgUnit findByKey( OrgUnit entity ) throws FinderException
    {
        OrgUnit oe = null;
        LdapConnection ld = null;
        Dn dn = getDn( entity );

        try
        {
            ld = getAdminConnection();
            Entry findEntry = read( ld, dn, ORGUNIT_ATRS );

            if ( findEntry == null )
            {
                String warning = "findByKey orgUnit name [" + entity.getName() + "] type ["
                    + entity.getType() + "] COULD NOT FIND ENTRY for dn [" + dn + "]";
                int errCode;

                if ( entity.getType() == OrgUnit.Type.PERM )
                {
                    errCode = GlobalErrIds.ORG_NOT_FOUND_PERM;
                }
                else
                {
                    errCode = GlobalErrIds.ORG_NOT_FOUND_USER;
                }

                throw new FinderException( errCode, warning );
            }

            oe = getEntityFromLdapEntry( findEntry, 0, entity.getContextId() );
        }
        catch ( LdapNoSuchObjectException e )
        {
            String warning = "findByKey orgUnit name [" + entity.getName() + "] type ["
                + entity.getType() + "] COULD NOT FIND ENTRY for dn [" + dn + "]";
            int errCode;

            if ( entity.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_NOT_FOUND_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_NOT_FOUND_USER;
            }
            throw new FinderException( errCode, warning );
        }
        catch ( LdapException e )
        {
            String error = "findByKey orgUnitName [" + entity.getName() + "] type [" + entity.getType()
                + "] dn [" + dn + "] caught LdapException=" + e;
            int errCode;

            if ( entity.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_READ_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_READ_FAILED_USER;
            }

            throw new FinderException( errCode, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return oe;
    }


    /**
     * @param orgUnit
     * @return
     * @throws org.apache.directory.fortress.core.FinderException
     *
     */
    List<OrgUnit> findOrgs( OrgUnit orgUnit ) throws FinderException
    {
        List<OrgUnit> orgUnitList = new ArrayList<>();
        LdapConnection ld = null;
        String orgUnitRoot = getOrgRoot( orgUnit );

        try
        {
            String searchVal = encodeSafeText( orgUnit.getName(), GlobalIds.ROLE_LEN );
            String filter = GlobalIds.FILTER_PREFIX + ORGUNIT_OBJECT_CLASS_NM + ")("
                + SchemaConstants.OU_AT + "=" + searchVal + "*))";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, orgUnitRoot,
                SearchScope.ONELEVEL, filter, ORGUNIT_ATRS, false, Config.getInstance().getInt(GlobalIds.CONFIG_LDAP_MAX_BATCH_SIZE, GlobalIds.BATCH_SIZE ) );
            long sequence = 0;

            while ( searchResults.next() )
            {
                orgUnitList
                    .add( getEntityFromLdapEntry( searchResults.getEntry(), sequence++, orgUnit.getContextId() ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "findOrgs search val [" + orgUnit.getName() + "] type [" + orgUnit.getType()
                + "] root [" + orgUnitRoot + "] caught LdapException=" + e;
            int errCode;

            if ( orgUnit.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_SEARCH_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_SEARCH_FAILED_USER;
            }

            throw new FinderException( errCode, error, e );
        }
        catch ( CursorException e )
        {
            String error = "findOrgs search val [" + orgUnit.getName() + "] type [" + orgUnit.getType()
                + "] root [" + orgUnitRoot + "] caught CursorException=" + e;
            int errCode;

            if ( orgUnit.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_SEARCH_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_SEARCH_FAILED_USER;
            }

            throw new FinderException( errCode, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return orgUnitList;
    }


    /**
     *
     * @param orgUnit
     * @return
     * @throws FinderException
     */
    Set<String> getOrgs( OrgUnit orgUnit ) throws FinderException
    {
        Set<String> ouSet = new TreeSet<String>( String.CASE_INSENSITIVE_ORDER );
        LdapConnection ld = null;
        String orgUnitRoot = getOrgRoot( orgUnit );

        try
        {
            String filter = "(objectclass=" + ORGUNIT_OBJECT_CLASS_NM + ")";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, orgUnitRoot,
                SearchScope.ONELEVEL, filter, ORGUNIT_ATR, false, Config.getInstance().getInt(GlobalIds.CONFIG_LDAP_MAX_BATCH_SIZE, GlobalIds.BATCH_SIZE ) );

            while ( searchResults.next() )
            {
                ouSet.add( getAttribute( searchResults.getEntry(), SchemaConstants.OU_AT ) );
            }

            searchResults.close();
        }
        catch ( LdapException e )
        {
            String error = "getOrgs type [" + orgUnit.getType() + "] root [" + orgUnitRoot
                + "] caught LdapException=" + e;
            int errCode;

            if ( orgUnit.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_GET_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_GET_FAILED_USER;
            }

            throw new FinderException( errCode, error, e );
        }
        catch ( CursorException e )
        {
            String error = "getOrgs type [" + orgUnit.getType() + "] root [" + orgUnitRoot
                + "] caught CursorException=" + e;
            int errCode;

            if ( orgUnit.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_GET_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_GET_FAILED_USER;
            }

            throw new FinderException( errCode, error, e );
        }
        catch ( IOException e )
        {
            String error = "getOrgs type [" + orgUnit.getType() + "] root [" + orgUnitRoot
                + "] caught IOException=" + e;
            int errCode;

            if ( orgUnit.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_GET_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_GET_FAILED_USER;
            }

            throw new FinderException( errCode, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return ouSet;
    }


    /**
      *
      * @param orgUnit
      * @return
      * @throws FinderException
      */
    List<Graphable> getAllDescendants( OrgUnit orgUnit ) throws FinderException
    {
        String orgUnitRoot = getOrgRoot( orgUnit );
        String[] DESC_ATRS =
            { SchemaConstants.OU_AT, GlobalIds.PARENT_NODES };
        List<Graphable> descendants = new ArrayList<>();
        LdapConnection ld = null;
        String filter = null;

        try
        {
            filter = GlobalIds.FILTER_PREFIX + ORGUNIT_OBJECT_CLASS_NM + ")("
                + GlobalIds.PARENT_NODES + "=*))";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, orgUnitRoot,
                SearchScope.ONELEVEL, filter, DESC_ATRS, false, Config.getInstance().getInt(GlobalIds.CONFIG_LDAP_MAX_BATCH_SIZE, GlobalIds.BATCH_SIZE ) );
            long sequence = 0;

            while ( searchResults.next() )
            {
                descendants.add( unloadDescendants( searchResults.getEntry(), sequence++, orgUnit.getContextId() ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "getAllDescendants filter [" + filter + "] caught LdapException="
                + e;
            throw new FinderException( GlobalErrIds.ARLE_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "getAllDescendants filter [" + filter + "] caught CursorException="
                + e.getMessage();
            throw new FinderException( GlobalErrIds.ARLE_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return descendants;
    }


    /**
     * Creates a new Dn for the given orgUnit
     *  
     * @param orgUnit The orgUnit
     * @return A Dn
     * @throws LdapInvalidDnException If the DN is invalid 
     */
    private Dn getDn( OrgUnit orgUnit )
    {
        Dn dn = null;

        try
        {
            switch ( orgUnit.type )
            {
                case USER:
                    dn = new Dn( SchemaConstants.OU_AT + "=" + orgUnit.getName(), getRootDn( orgUnit.getContextId(),
                        GlobalIds.OSU_ROOT ) );
                    break;

                case PERM:
                    dn = new Dn( SchemaConstants.OU_AT + "=" + orgUnit.getName(), getRootDn( orgUnit.getContextId(),
                        GlobalIds.PSU_ROOT ) );
                    break;

                default:
                    String warning = "getDn invalid type";
                    LOG.warn( warning );
                    break;
            }

            return dn;
        }
        catch ( LdapInvalidDnException lide )
        {
            LOG.error( lide.getMessage() );
            throw new RuntimeException( lide.getMessage() );
        }
    }


    /**
     *
     * @param orgUnit
     * @return
     */
    private String getOrgRoot( OrgUnit orgUnit )
    {
        String dn = null;

        switch ( orgUnit.type )
        {
            case USER:
                dn = getRootDn( orgUnit.getContextId(), GlobalIds.OSU_ROOT );
                break;

            case PERM:
                dn = getRootDn( orgUnit.getContextId(), GlobalIds.PSU_ROOT );
                break;

            default:
                String warning = "getOrgRootDn invalid type";
                LOG.warn( warning );
                break;
        }

        return dn;
    }


    /**
    *
    * @param le
    * @param sequence
    * @param contextId
    * @return
     * @throws LdapInvalidAttributeValueException 
    * @throws LdapException
    */
    private Graphable unloadDescendants( Entry le, long sequence, String contextId )
        throws LdapInvalidAttributeValueException
    {
        OrgUnit entity = new ObjectFactory().createOrgUnit();
        entity.setSequenceId( sequence );
        entity.setName( getAttribute( le, SchemaConstants.OU_AT ) );
        entity.setParents( getAttributeSet( le, GlobalIds.PARENT_NODES ) );

        return entity;
    }


    /**
     *
     * @param le
     * @param sequence
     * @param contextId
     * @return
     * @throws LdapInvalidAttributeValueException 
     * @throws LdapException
     */
    private OrgUnit getEntityFromLdapEntry( Entry le, long sequence, String contextId )
        throws LdapInvalidAttributeValueException
    {
        OrgUnit entity = new ObjectFactory().createOrgUnit();
        entity.setSequenceId( sequence );
        entity.setId( getAttribute( le, GlobalIds.FT_IID ) );
        entity.setName( getAttribute( le, SchemaConstants.OU_AT ) );
        entity.setDescription( getAttribute( le, SchemaConstants.DESCRIPTION_AT ) );
        String dn = le.getDn().getName();

        if ( dn.toLowerCase().contains( getRootDn( contextId, GlobalIds.PSU_ROOT ).toLowerCase() ) )
        {
            entity.setType( OrgUnit.Type.PERM );
            entity.setChildren( PsoUtil.getInstance().getChildren( entity.getName().toUpperCase(), contextId ) );
        }
        else if ( dn.toLowerCase().contains( getRootDn( contextId, GlobalIds.OSU_ROOT ).toLowerCase() ) )
        {
            entity.setType( OrgUnit.Type.USER );
            entity.setChildren( UsoUtil.getInstance().getChildren( entity.getName().toUpperCase(), contextId ) );
        }
        entity.setParents( getAttributeSet( le, GlobalIds.PARENT_NODES ) );
        return entity;
    }
}