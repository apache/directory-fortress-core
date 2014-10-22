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
package org.apache.directory.fortress.core.rbac.dao.unboundid;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.directory.fortress.core.CreateException;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.ObjectFactory;
import org.apache.directory.fortress.core.RemoveException;
import org.apache.directory.fortress.core.UpdateException;
import org.apache.directory.fortress.core.ldap.UnboundIdDataProvider;
import org.apache.directory.fortress.core.rbac.Role;
import org.apache.directory.fortress.core.rbac.RoleUtil;
import org.apache.directory.fortress.core.rbac.SDSet;
import org.apache.directory.fortress.core.util.attr.VUtil;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttribute;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModification;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchResults;


/**
 * This class performs persistence on the RBAC Static Separation of Duties and Dynamic Separation of Duties data sets.
 * <p/>
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
 * <li> <code>objectclass	( 1.3.6.1.4.1.38088.2.4</code>
 * <li> <code>NAME 'ftSSDSet'</code>
 * <li> <code>DESC 'Fortress Role Static Separation of Duty Set Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftSetName $ ftSetCardinality )</code>
 * <li> <code>MAY ( ftRoles $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * OR
 * <h4>Dynamic Separation of Duties Set</h4>
 * <ul>
 * <li>
 * <li> <code>objectclass	( 1.3.6.1.4.1.38088.2.5</code>
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
 * <p/>
 * This class is thread safe.
 * <p/>
 *
 * @author Shawn McKinney
 */
public final class SdDAO extends UnboundIdDataProvider implements org.apache.directory.fortress.core.rbac.dao.SdDAO

{
    private static final String SD_SET_NM = "ftSetName";
    private static final String ROLES = "ftRoles";
    private static final String SD_SET_CARDINALITY = "ftSetCardinality";

    private static final String SSD_OBJECT_CLASS_NM = "ftSSDSet";
    private static final String SSD_OBJ_CLASS[] =
        {
            GlobalIds.TOP, SSD_OBJECT_CLASS_NM, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };

    private static final String DSD_OBJECT_CLASS_NM = "ftDSDSet";
    private static final String DSD_OBJ_CLASS[] =
        {
            GlobalIds.TOP, DSD_OBJECT_CLASS_NM, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };

    private static final String[] SD_SET_ATRS =
        {
            GlobalIds.FT_IID, SD_SET_NM, GlobalIds.DESC, ROLES, SD_SET_CARDINALITY
    };


    /**
     * @param entity
     * @return
     * @throws org.apache.directory.fortress.core.CreateException
     */
    public final SDSet create( SDSet entity )
        throws CreateException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity.getName(), entity.getContextId() );
        String[] objectClass = SSD_OBJ_CLASS;
        if ( entity.getType() == SDSet.SDType.DYNAMIC )
        {
            objectClass = DSD_OBJ_CLASS;
        }
        try
        {
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add( createAttributes( GlobalIds.OBJECT_CLASS, objectClass ) );
            entity.setId();
            attrs.add( createAttribute( GlobalIds.FT_IID, entity.getId() ) );
            attrs.add( createAttribute( SD_SET_NM, entity.getName() ) );
            // description field is optional on this object class:
            if ( VUtil.isNotNullOrEmpty( entity.getDescription() ) )
            {
                attrs.add( createAttribute( GlobalIds.DESC, entity.getDescription() ) );
            }
            // CN attribute is required for this object class:
            attrs.add( createAttribute( GlobalIds.CN, entity.getName() ) );
            loadAttrs( entity.getMembers(), attrs, ROLES );
            attrs.add( createAttribute( SD_SET_CARDINALITY, "" + entity.getCardinality() ) );
            LDAPEntry myEntry = new LDAPEntry( dn, attrs );
            ld = getAdminConnection();
            add( ld, myEntry, entity );
        }
        catch ( LDAPException e )
        {
            String error = "create SD set name [" + entity.getName() + "] type [" + entity.getType()
                + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
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
    public final SDSet update( SDSet entity )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity.getName(), entity.getContextId() );
        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();
            if ( VUtil.isNotNullOrEmpty( entity.getDescription() ) )
            {
                LDAPAttribute desc = new LDAPAttribute( GlobalIds.DESC, entity.getDescription() );
                mods.add( LDAPModification.REPLACE, desc );
            }
            if ( entity.getCardinality() != null )
            {
                LDAPAttribute cardinality = new LDAPAttribute( SD_SET_CARDINALITY, "" + entity.getCardinality() );
                mods.add( LDAPModification.REPLACE, cardinality );
            }
            loadAttrs( entity.getMembers(), mods, ROLES );
            if ( mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, dn, mods, entity );
            }
        }
        catch ( LDAPException e )
        {
            String error = "update name [" + entity.getName() + "] type [" + entity.getType()
                + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
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
    public final SDSet remove( SDSet entity )
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity.getName(), entity.getContextId() );
        try
        {
            ld = getAdminConnection();
            delete( ld, dn, entity );
        }
        catch ( LDAPException e )
        {
            String error = "remove SD name=" + entity.getName() + " type [" + entity.getType() + "] LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
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
    public final SDSet getSD( SDSet sdSet )
        throws FinderException
    {
        SDSet entity = null;
        LDAPConnection ld = null;
        String dn = getDn( sdSet.getName(), sdSet.getContextId() );
        try
        {
            ld = getAdminConnection();
            LDAPEntry findEntry = read( ld, dn, SD_SET_ATRS );
            entity = unloadLdapEntry( findEntry, 0 );
            if ( entity == null )
            {
                String warning = "getSD no entry found dn [" + dn + "]";
                throw new FinderException( GlobalErrIds.SSD_NOT_FOUND, warning );
            }
        }
        catch ( LDAPException e )
        {
            if ( e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT )
            {
                String warning = "getSD Obj COULD NOT FIND ENTRY for dn [" + dn + "]";
                throw new FinderException( GlobalErrIds.SSD_NOT_FOUND, warning );
            }
            String error = "getSSD dn [" + dn + "] LEXCD=" + e.getLDAPResultCode() + " LEXMSG=" + e;
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
    public final List<SDSet> search( SDSet sdset )
        throws FinderException
    {
        List<SDSet> sdList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
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
            searchResults = search( ld, ssdRoot,
                LDAPConnection.SCOPE_SUB, filter, SD_SET_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;
            while ( searchResults.hasMoreElements() )
            {
                sdList.add( unloadLdapEntry( searchResults.next(), sequence++ ) );
            }
        }
        catch ( LDAPException e )
        {
            String error = "search sdset name [" + sdset.getName() + "] type [" + sdset.getType()
                + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
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
    public final List<SDSet> search( Role role, SDSet.SDType type )
        throws FinderException
    {
        List<SDSet> sdList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String ssdRoot = getSdRoot( role.getContextId() );
        String objectClass = SSD_OBJECT_CLASS_NM;
        if ( type == SDSet.SDType.DYNAMIC )
        {
            objectClass = DSD_OBJECT_CLASS_NM;
        }

        try
        {
            String roleVal = encodeSafeText( role.getName(), GlobalIds.ROLE_LEN );
            //String filter = GlobalIds.FILTER_PREFIX + SSD_OBJECT_CLASS_NM + ")(" + ROLES + "=" + roleVal + "))";
            String filter = GlobalIds.FILTER_PREFIX + objectClass + ")(";
            // Include any parents target role may have:
            Set<String> roles = RoleUtil.getAscendants( role.getName(), role.getContextId() );
            if ( VUtil.isNotNullOrEmpty( roles ) )
            {
                filter += "|(" + ROLES + "=" + roleVal + ")";
                for ( String uRole : roles )
                {
                    filter += "(" + ROLES + "=" + uRole + ")";
                }
                filter += ")";
            }
            else
            {
                filter += ROLES + "=" + roleVal + ")";
            }
            filter += ")";
            ld = getAdminConnection();
            searchResults = search( ld, ssdRoot,
                LDAPConnection.SCOPE_SUB, filter, SD_SET_ATRS, false, GlobalIds.BATCH_SIZE );

            long sequence = 0;
            while ( searchResults.hasMoreElements() )
            {
                sdList.add( unloadLdapEntry( searchResults.next(), sequence++ ) );
            }
        }
        catch ( LDAPException e )
        {
            String error = "search role [" + role.getName() + "] type [" + type + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
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
    public final Set<SDSet> search( Set<String> roles, SDSet sdSet )
        throws FinderException
    {
        Set<SDSet> sdList = new HashSet<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String ssdRoot = getSdRoot( sdSet.getContextId() );
        String objectClass = SSD_OBJECT_CLASS_NM;
        if ( sdSet.getType() == SDSet.SDType.DYNAMIC )
        {
            objectClass = DSD_OBJECT_CLASS_NM;
        }
        try
        {
            if ( VUtil.isNotNullOrEmpty( roles ) )
            {
                String filter = GlobalIds.FILTER_PREFIX + objectClass + ")(|";
                for ( String rle : roles )
                {
                    filter += "(" + ROLES + "=" + rle + ")";
                }
                filter += "))";
                ld = getAdminConnection();
                searchResults = search( ld, ssdRoot,
                    LDAPConnection.SCOPE_SUB, filter, SD_SET_ATRS, false, GlobalIds.BATCH_SIZE );
                long sequence = 0;
                while ( searchResults.hasMoreElements() )
                {
                    sdList.add( unloadLdapEntry( searchResults.next(), sequence++ ) );
                }
            }
        }
        catch ( LDAPException e )
        {
            String error = "search type [" + sdSet.getType() + "] caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
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
     * @throws LDAPException
     */
    private SDSet unloadLdapEntry( LDAPEntry le, long sequence )
    {
        SDSet entity = new ObjectFactory().createSDset();
        entity.setSequenceId( sequence );
        entity.setId( getAttribute( le, GlobalIds.FT_IID ) );
        entity.setName( getAttribute( le, SD_SET_NM ) );
        entity.setDescription( getAttribute( le, GlobalIds.DESC ) );
        entity.setMembers( getAttributeSet( le, ROLES ) );
        String szCard = getAttribute( le, SD_SET_CARDINALITY );
        entity.setCardinality( new Integer( szCard ) );
        return entity;
    }


    private String getDn( String name, String contextId )
    {
        return GlobalIds.CN + "=" + name + "," + getSdRoot( contextId );
    }


    private String getSdRoot( String contextId )
    {
        return getRootDn( contextId, GlobalIds.SD_ROOT );
    }
}
