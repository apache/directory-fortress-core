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


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
import org.apache.directory.api.ldap.model.exception.LdapNoSuchObjectException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.fortress.core.CreateException;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.RemoveException;
import org.apache.directory.fortress.core.UpdateException;
import org.apache.directory.fortress.core.ldap.LdapDataProvider;
import org.apache.directory.fortress.core.model.ObjectFactory;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.SDSet;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.ldap.client.api.LdapConnection;


/**
 * This class performs persistence on the RBAC Static Separation of Duties and Dynamic Separation of Duties data sets.
 * <p>
 * The Fortress SDSet entity is a composite of the following other Fortress structural and aux object classes:
 * <h4>1. organizationalRole Structural Object Class is used to store basic attributes like cn and description</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.5.6.8 NAME 'organizationalRole'</code>
 * <li> <code>DESC 'RFC2256: an organizational role'</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST cn</code>
 * <li> <code>MAY ( x121Address $ registeredAddress $ destinationIndicator $</code>
 * <li> <code>preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $</code>
 * <li> <code>telephoneNumber $ internationaliSDNNumber $ facsimileTelephoneNumber $</code>
 * <li> <code>seeAlso $ roleOccupant $ preferredDeliveryMethod $ street $</code>
 * <li> <code>postOfficeBox $ postalCode $ postalAddress $</code>
 * <li> <code>physicalDeliveryOfficeName $ ou $ st $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>2. The RBAC Separation of Duties</h4>
 * <ul>
 * <li>  ---Static Separation of Duties Set-------
 * <li> <code>objectclass    ( 1.3.6.1.4.1.38088.2.4</code>
 * <li> <code>NAME 'ftSSDSet'</code>
 * <li> <code>DESC 'Fortress Role Static Separation of Duty Set Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftSetName $ ftSetCardinality )</code>
 * <li> <code>MAY ( ftRoles $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p>
 * OR
 * <h4>Dynamic Separation of Duties Set</h4>
 * <ul>
 * <li>
 * <li> <code>objectclass    ( 1.3.6.1.4.1.38088.2.5</code>
 * <li> <code>NAME 'ftDSDSet'</code>
 * <li> <code>DESC 'Fortress Role Dynamic Separation of Duty Set Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftSetName $ ftSetCardinality )</code>
 * <li> <code>MAY ( ftRoles $ description ) )</code>
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
 */
final class SdDAO extends LdapDataProvider
{
    private static final String SD_SET_NM = "ftSetName";
    private static final String ROLES = "ftRoles";
    private static final String SD_SET_CARDINALITY = "ftSetCardinality";

    private static final String SSD_OBJECT_CLASS_NM = "ftSSDSet";
    private static final String SSD_OBJ_CLASS[] =
        {
            SchemaConstants.TOP_OC, SSD_OBJECT_CLASS_NM, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };

    private static final String DSD_OBJECT_CLASS_NM = "ftDSDSet";
    private static final String DSD_OBJ_CLASS[] =
        {
            SchemaConstants.TOP_OC, DSD_OBJECT_CLASS_NM, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };

    private static final String[] SD_SET_ATRS =
        {
            GlobalIds.FT_IID, SD_SET_NM, SchemaConstants.DESCRIPTION_AT, ROLES, SD_SET_CARDINALITY
    };

    /**
     * @param entity
     * @return
     * @throws org.apache.directory.fortress.core.CreateException
     */
    SDSet create( SDSet entity ) throws CreateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity.getName(), entity.getContextId() );
        String[] objectClass = SSD_OBJ_CLASS;

        if ( entity.getType() == SDSet.SDType.DYNAMIC )
        {
            objectClass = DSD_OBJ_CLASS;
        }

        try
        {
            Entry entry = new DefaultEntry( dn );
            entry.add( createAttributes( SchemaConstants.OBJECT_CLASS_AT, objectClass ) );
            entity.setId();
            entry.add( GlobalIds.FT_IID, entity.getId() );
            entry.add( SD_SET_NM, entity.getName() );

            // description field is optional on this object class:
            if ( StringUtils.isNotEmpty( entity.getDescription() ) )
            {
                entry.add( SchemaConstants.DESCRIPTION_AT, entity.getDescription() );
            }

            // CN attribute is required for this object class:
            entry.add( SchemaConstants.CN_AT, entity.getName() );
            loadAttrs( entity.getMembers(), entry, ROLES );
            entry.add( SD_SET_CARDINALITY, "" + entity.getCardinality() );

            ld = getAdminConnection();
            add( ld, entry, entity );
        }
        catch ( LdapException e )
        {
            String error = "create SD set name [" + entity.getName() + "] type [" + entity.getType()
                + "] caught LdapException=" + e;
            int errCode;
            if ( entity.getType() == SDSet.SDType.DYNAMIC )
            {
                errCode = GlobalErrIds.DSD_ADD_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_ADD_FAILED;
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
     */
    SDSet update( SDSet entity ) throws UpdateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity.getName(), entity.getContextId() );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();

            if ( StringUtils.isNotEmpty( entity.getDescription() ) )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE, SchemaConstants.DESCRIPTION_AT, entity.getDescription() ) );
            }

            if ( entity.getCardinality() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE, SD_SET_CARDINALITY, entity.getCardinality().toString() ) );
            }

            loadAttrs( entity.getMembers(), mods, ROLES );

            if ( mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, dn, mods, entity );
            }
        }
        catch ( LdapException e )
        {
            String error = "update name [" + entity.getName() + "] type [" + entity.getType()
                + "] caught LdapException=" + e;
            int errCode;
            if ( entity.getType() == SDSet.SDType.DYNAMIC )
            {
                errCode = GlobalErrIds.DSD_UPDATE_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_UPDATE_FAILED;
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
     * @throws org.apache.directory.fortress.core.RemoveException
     */
    SDSet remove( SDSet entity ) throws RemoveException
    {
        LdapConnection ld = null;
        String dn = getDn( entity.getName(), entity.getContextId() );

        try
        {
            ld = getAdminConnection();
            delete( ld, dn, entity );
        }
        catch ( LdapException e )
        {
            String error = "remove SD name=" + entity.getName() + " type [" + entity.getType() + "] LdapException="
                + e;
            int errCode;
            if ( entity.getType() == SDSet.SDType.DYNAMIC )
            {
                errCode = GlobalErrIds.DSD_DELETE_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_DELETE_FAILED;
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
     * @param sdSet
     * @return
     * @throws FinderException
     */
    SDSet getSD( SDSet sdSet ) throws FinderException
    {
        SDSet entity = null;
        LdapConnection ld = null;
        String dn = getDn( sdSet.getName(), sdSet.getContextId() );

        try
        {
            ld = getAdminConnection();
            Entry findEntry = read( ld, dn, SD_SET_ATRS );
            if ( findEntry == null )
            {
                String warning = "getSD no entry found dn [" + dn + "]";
                throw new FinderException( GlobalErrIds.SSD_NOT_FOUND, warning );
            }
            entity = unloadLdapEntry( findEntry, 0 );
        }
        catch ( LdapNoSuchObjectException e )
        {
            String warning = "getSD Obj COULD NOT FIND ENTRY for dn [" + dn + "]";
            throw new FinderException( GlobalErrIds.SSD_NOT_FOUND, warning );
        }
        catch ( LdapException e )
        {
            String error = "getSSD dn [" + dn + "] LEXCD=" + e;
            int errCode;

            if ( sdSet.getType() == SDSet.SDType.DYNAMIC )
            {
                errCode = GlobalErrIds.DSD_READ_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_READ_FAILED;
            }

            throw new FinderException( errCode, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     * Given an SSD name and type, find matching object in the directory.
     * @param sdset requires name and type.
     * @return List of matching SDSets.
     * @throws org.apache.directory.fortress.core.FinderException
     */
    List<SDSet> search( SDSet sdset ) throws FinderException
    {
        List<SDSet> sdList = new ArrayList<>();
        LdapConnection ld = null;
        String ssdRoot = getSdRoot( sdset.getContextId() );
        String objectClass = SSD_OBJECT_CLASS_NM;

        if ( sdset.getType() == SDSet.SDType.DYNAMIC )
        {
            objectClass = DSD_OBJECT_CLASS_NM;
        }

        try
        {
            String searchVal = encodeSafeText( sdset.getName(), GlobalIds.ROLE_LEN );
            String filter = GlobalIds.FILTER_PREFIX + objectClass + ")(" + SD_SET_NM + "=" + searchVal + "*))";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, ssdRoot,
                SearchScope.SUBTREE, filter, SD_SET_ATRS, false, Config.getInstance().getInt(GlobalIds.CONFIG_LDAP_MAX_BATCH_SIZE, GlobalIds.BATCH_SIZE ) );
            long sequence = 0;

            while ( searchResults.next() )
            {
                sdList.add( unloadLdapEntry( searchResults.getEntry(), sequence++ ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "search sdset name [" + sdset.getName() + "] type [" + sdset.getType()
                + "] caught LdapException=" + e;
            int errCode;

            if ( sdset.getType() == SDSet.SDType.DYNAMIC )
            {
                errCode = GlobalErrIds.DSD_SEARCH_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_SEARCH_FAILED;
            }

            throw new FinderException( errCode, error, e );
        }
        catch ( CursorException e )
        {
            String error = "search sdset name [" + sdset.getName() + "] type [" + sdset.getType()
                + "] caught CursorException=" + e.getMessage();
            int errCode;

            if ( sdset.getType() == SDSet.SDType.DYNAMIC )
            {
                errCode = GlobalErrIds.DSD_SEARCH_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_SEARCH_FAILED;
            }

            throw new FinderException( errCode, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return sdList;
    }


    /**
     * @param role
     * @return
     * @throws org.apache.directory.fortress.core.FinderException
     */
    List<SDSet> search( Role role, SDSet.SDType type ) throws FinderException
    {
        List<SDSet> sdList = new ArrayList<>();
        LdapConnection ld = null;
        String ssdRoot = getSdRoot( role.getContextId() );
        String objectClass = SSD_OBJECT_CLASS_NM;
        if ( type == SDSet.SDType.DYNAMIC )
        {
            objectClass = DSD_OBJECT_CLASS_NM;
        }
        try
        {
            String roleVal = encodeSafeText( role.getName(), GlobalIds.ROLE_LEN );
            StringBuilder filterbuf = new StringBuilder();
            filterbuf.append( GlobalIds.FILTER_PREFIX );
            filterbuf.append( objectClass );
            filterbuf.append( ")(" );

            // Include any parents target role may have:
            Set<String> roles = RoleUtil.getInstance().getAscendants( role.getName(), role.getContextId() );

            if ( CollectionUtils.isNotEmpty( roles ) )
            {
                filterbuf.append( "|(" );
                filterbuf.append( ROLES );
                filterbuf.append( "=" );
                filterbuf.append( roleVal );
                filterbuf.append( ")" );

                for ( String uRole : roles )
                {
                    filterbuf.append( "(" );
                    filterbuf.append( ROLES );
                    filterbuf.append( "=" );
                    filterbuf.append( uRole );
                    filterbuf.append( ")" );
                }
                filterbuf.append( ")" );

            }
            else
            {
                filterbuf.append( ROLES );
                filterbuf.append( "=" );
                filterbuf.append( roleVal );
                filterbuf.append( ")" );

            }

            filterbuf.append( ")" );
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, ssdRoot,
                SearchScope.SUBTREE, filterbuf.toString(), SD_SET_ATRS, false, Config.getInstance().getInt(GlobalIds.CONFIG_LDAP_MAX_BATCH_SIZE, GlobalIds.BATCH_SIZE ) );

            long sequence = 0;
            while ( searchResults.next() )
            {
                sdList.add( unloadLdapEntry( searchResults.getEntry(), sequence++ ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "search role [" + role.getName() + "] type [" + type + "] caught LdapException="
                + e;
            int errCode;

            if ( type == SDSet.SDType.DYNAMIC )
            {
                errCode = GlobalErrIds.DSD_SEARCH_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_SEARCH_FAILED;
            }

            throw new FinderException( errCode, error, e );
        }
        catch ( CursorException e )
        {
            String error = "search role [" + role.getName() + "] type [" + type + "] caught CursorException="
                + e.getMessage();
            int errCode;

            if ( type == SDSet.SDType.DYNAMIC )
            {
                errCode = GlobalErrIds.DSD_SEARCH_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_SEARCH_FAILED;
            }

            throw new FinderException( errCode, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return sdList;
    }


    /**
     * @param roles
     * @param sdSet
     * @return
     * @throws org.apache.directory.fortress.core.FinderException
     */
    Set<SDSet> search( Set<String> roles, SDSet sdSet ) throws FinderException
    {
        Set<SDSet> sdList = new HashSet<>();
        LdapConnection ld = null;
        String ssdRoot = getSdRoot( sdSet.getContextId() );
        String objectClass = SSD_OBJECT_CLASS_NM;

        if ( sdSet.getType() == SDSet.SDType.DYNAMIC )
        {
            objectClass = DSD_OBJECT_CLASS_NM;
        }

        try
        {
            if ( CollectionUtils.isNotEmpty( roles ) )
            {
                StringBuilder filterbuf = new StringBuilder();
                filterbuf.append( GlobalIds.FILTER_PREFIX );
                filterbuf.append( objectClass );
                filterbuf.append( ")(|" );

                for ( String rle : roles )
                {
                    filterbuf.append( "(" );
                    filterbuf.append( ROLES );
                    filterbuf.append( "=" );
                    filterbuf.append( rle );
                    filterbuf.append( ")" );
                }
                filterbuf.append( "))" );
                ld = getAdminConnection();
                SearchCursor searchResults = search( ld, ssdRoot,
                    SearchScope.SUBTREE, filterbuf.toString(), SD_SET_ATRS, false, Config.getInstance().getInt(GlobalIds.CONFIG_LDAP_MAX_BATCH_SIZE, GlobalIds.BATCH_SIZE ) );
                long sequence = 0;

                while ( searchResults.next() )
                {
                    sdList.add( unloadLdapEntry( searchResults.getEntry(), sequence++ ) );
                }
            }
        }
        catch ( LdapException e )
        {
            String error = "search type [" + sdSet.getType() + "] caught LdapException=" + e;
            int errCode;

            if ( sdSet.getType() == SDSet.SDType.DYNAMIC )
            {
                errCode = GlobalErrIds.DSD_SEARCH_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_SEARCH_FAILED;
            }
            throw new FinderException( errCode, error, e );
        }
        catch ( CursorException e )
        {
            String error = "search type [" + sdSet.getType() + "] caught CursorException=" + e.getMessage();
            int errCode;

            if ( sdSet.getType() == SDSet.SDType.DYNAMIC )
            {
                errCode = GlobalErrIds.DSD_SEARCH_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_SEARCH_FAILED;
            }
            throw new FinderException( errCode, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return sdList;
    }


    /**
     * @param le
     * @return
     * @throws LdapInvalidAttributeValueException 
     * @throws LdapException
     */
    private SDSet unloadLdapEntry( Entry le, long sequence ) throws LdapInvalidAttributeValueException
    {
        SDSet entity = new ObjectFactory().createSDset();
        entity.setSequenceId( sequence );
        entity.setId( getAttribute( le, GlobalIds.FT_IID ) );
        entity.setName( getAttribute( le, SD_SET_NM ) );
        entity.setDescription( getAttribute( le, SchemaConstants.DESCRIPTION_AT ) );
        entity.setMembers( getAttributeSet( le, ROLES ) );
        String szCard = getAttribute( le, SD_SET_CARDINALITY );
        entity.setCardinality( Integer.valueOf( szCard ) );

        return entity;
    }


    private String getDn( String name, String contextId )
    {
        return SchemaConstants.CN_AT + "=" + name + "," + getSdRoot( contextId );
    }


    private String getSdRoot( String contextId )
    {
        return getRootDn( contextId, GlobalIds.SD_ROOT );
    }
}
