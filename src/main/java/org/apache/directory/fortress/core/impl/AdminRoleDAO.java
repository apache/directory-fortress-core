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
import java.util.List;

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
import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.ConstraintUtil;
import org.apache.directory.fortress.core.model.FortEntity;
import org.apache.directory.fortress.core.model.Graphable;
import org.apache.directory.fortress.core.model.ObjectFactory;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.ldap.client.api.LdapConnection;


/**
 * The AdminRoleDAO is called by {@link AdminRoleP} and processes data via its entity {@link org.apache.directory.fortress.core.model.AdminRole}.
 * <p>
 * The Fortress AdminRoleDAO uses the following other Fortress structural and aux object classes:
 * <h4>1. ftRls Structural objectclass is used to store the AdminRole information like name, and temporal constraints</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass    ( 1.3.6.1.4.1.38088.2.1</code>
 * <li> <code>NAME 'ftRls'</code>
 * <li> <code>DESC 'Fortress Role Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftRoleName )</code>
 * <li> <code>MAY ( description $ ftCstr ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>2. ftProperties AUXILIARY Object Class is used to store client specific name/value pairs on target entity</h4>
 * <code># This aux object class can be used to store custom attributes.</code><br>
 * <code># The properties collections consist of name/value pairs and are not constrainted by Fortress.</code><br>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.2</code>
 * <li> <code>NAME 'ftProperties'</code>
 * <li> <code>DESC 'Fortress Properties AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY ( ftProps ) ) </code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>3. ftPools Auxiliary object class store the ARBAC Perm and User OU assignments on AdminRole entity</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.3</code>
 * <li> <code>NAME 'ftPools'</code>
 * <li> <code>DESC 'Fortress Pools AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY ( ftOSU $ ftOSP ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>4. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity</h4>
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
final class AdminRoleDAO extends LdapDataProvider implements PropertyProvider<AdminRole>
{
    private static final String ROLE_OCCUPANT = "roleOccupant";
    private static final String ROLE_OSP = "ftOSP";
    private static final String ROLE_OSU = "ftOSU";
    private static final String ROLE_RANGE = "ftRange";
    private static final String POOLS_AUX_OBJECT_CLASS_NAME = "ftPools";
    private static final String ADMIN_ROLE_OBJ_CLASS[] =
        {
            SchemaConstants.TOP_OC,
            GlobalIds.ROLE_OBJECT_CLASS_NM,
            GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME,
            POOLS_AUX_OBJECT_CLASS_NAME,
            GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
        };
    private static final String ROLE_NM = "ftRoleName";
    private static final String[] ROLE_NM_ATR =
        {
            ROLE_NM
    };

    private static final String[] ROLE_ATRS =
        {
            GlobalIds.FT_IID,
            ROLE_NM,
            SchemaConstants.DESCRIPTION_AT,
            GlobalIds.CONSTRAINT,
            ROLE_OCCUPANT,
            ROLE_OSP,
            ROLE_OSU,
            ROLE_RANGE,
            GlobalIds.PARENT_NODES
    };


    /**
     * Create a new AdminRole entity using supplied data.  Required attribute is {@link org.apache.directory.fortress.core.model.AdminRole#name}.
     * This data will be stored in the {@link GlobalIds#ADMIN_ROLE_ROOT} container.
     *
     * @param entity record contains AdminRole data.  Null attributes will be ignored.
     * @return input record back to client.
     * @throws org.apache.directory.fortress.core.CreateException in the event LDAP errors occur.
     */
    AdminRole create( AdminRole entity ) throws CreateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity );

        try
        {
            Entry entry = new DefaultEntry( dn );

            entry.add( SchemaConstants.OBJECT_CLASS_AT, ADMIN_ROLE_OBJ_CLASS );
            entity.setId();
            entry.add( GlobalIds.FT_IID, entity.getId() );
            entry.add( ROLE_NM, entity.getName() );

            // description field is optional on this object class:
            if ( StringUtils.isNotEmpty( entity.getDescription() ) )
            {
                entry.add( SchemaConstants.DESCRIPTION_AT, entity.getDescription() );
            }

            // CN attribute is required for this object class:
            entry.add( SchemaConstants.CN_AT, entity.getName() );
            entry.add( GlobalIds.CONSTRAINT, ConstraintUtil.setConstraint( entity ) );
            loadAttrs( entity.getOsPSet(), entry, ROLE_OSP );
            loadAttrs( entity.getOsUSet(), entry, ROLE_OSU );
            String szRaw = entity.getRoleRangeRaw();

            if ( StringUtils.isNotEmpty( szRaw ) )
            {
                entry.add( ROLE_RANGE, szRaw );
            }

            // These multi-valued attributes are optional.  The utility function will return quietly if no items are loaded into collection:
            loadAttrs( entity.getParents(), entry, GlobalIds.PARENT_NODES );

            ld = getAdminConnection();
            add( ld, entry, entity );
        }
        catch ( LdapException e )
        {
            String error = "create role [" + entity.getName() + "] caught LdapException=" + e;
            throw new CreateException( GlobalErrIds.ARLE_ADD_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     * Update existing AdminRole entity using supplied data.  Required attribute is {@link AdminRole#name}.
     * This data will be stored in the {@link GlobalIds#ADMIN_ROLE_ROOT} container.
     *
     * @param entity record contains AdminRole data.  Null attributes will be ignored.
     * @return input record back to client.
     * @throws UpdateException in the event LDAP errors occur.
     */
    AdminRole update( AdminRole entity ) throws UpdateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();

            if ( StringUtils.isNotEmpty( entity.getDescription() ) )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    SchemaConstants.DESCRIPTION_AT, entity.getDescription() ) );
            }

            if ( CollectionUtils.isNotEmpty( entity.getOccupants() ) )
            {
                for ( String name : entity.getOccupants() )
                {
                    mods.add( new DefaultModification(
                        ModificationOperation.REPLACE_ATTRIBUTE, ROLE_OCCUPANT, name ) );
                }
            }

            if ( entity.isTemporalSet() )
            {
                String szRawData = ConstraintUtil.setConstraint( entity );

                if ( StringUtils.isNotEmpty( szRawData ) )
                {
                    mods.add( new DefaultModification(
                        ModificationOperation.REPLACE_ATTRIBUTE, GlobalIds.CONSTRAINT, szRawData ) );
                }
            }

            loadAttrs( entity.getOsUSet(), mods, ROLE_OSU );
            loadAttrs( entity.getOsPSet(), mods, ROLE_OSP );
            String szRaw = entity.getRoleRangeRaw();

            if ( StringUtils.isNotEmpty( szRaw ) )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE, ROLE_RANGE, szRaw ) );
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
            String error = "update name [" + entity.getName() + "] caught LdapException=" + e;
            throw new UpdateException( GlobalErrIds.ARLE_UPDATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     *
     * @param entity
     * @throws UpdateException
     */
    void deleteParent( AdminRole entity ) throws UpdateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();
            mods.add( new DefaultModification( ModificationOperation.REMOVE_ATTRIBUTE, GlobalIds.PARENT_NODES ) );
            ld = getAdminConnection();
            modify( ld, dn, mods, entity );
        }
        catch ( LdapException e )
        {
            String error = "deleteParent name [" + entity.getName() + "] caught LdapException=" + e;
            throw new UpdateException( GlobalErrIds.ARLE_REMOVE_PARENT_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * This method will add the supplied DN as a role occupant to the target record.
     * This data will be stored in the {@link GlobalIds#ADMIN_ROLE_ROOT} container.
     *
     * @param entity record contains {@link AdminRole#name}.  Null attributes will be ignored.
     * @param userDn contains the DN for userId who is being assigned.
     * @return input record back to client.
     * @throws UpdateException in the event LDAP errors occur.
     */
    AdminRole assign( AdminRole entity, String userDn ) throws UpdateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();
            mods.add( new DefaultModification( ModificationOperation.ADD_ATTRIBUTE, ROLE_OCCUPANT, userDn ) );
            ld = getAdminConnection();
            modify( ld, dn, mods, entity );
        }
        catch ( LdapException e )
        {
            String error = "assign role name [" + entity.getName() + "] user dn [" + userDn + "] caught LdapException="
                + e;
            throw new UpdateException( GlobalErrIds.ARLE_USER_ASSIGN_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     * This method will remove the supplied DN as a role occupant to the target record.
     * This data will be stored in the {@link GlobalIds#ADMIN_ROLE_ROOT} container.
     *
     * @param entity record contains {@link AdminRole#name}.  Null attributes will be ignored.
     * @param userDn contains the DN for userId who is being deassigned.
     * @return input record back to client.
     * @throws UpdateException in the event LDAP errors occur.
     */
    AdminRole deassign( AdminRole entity, String userDn ) throws UpdateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();
            mods.add( new DefaultModification(
                ModificationOperation.REMOVE_ATTRIBUTE, ROLE_OCCUPANT, userDn ) );
            ld = getAdminConnection();
            modify( ld, dn, mods, entity );
        }
        catch ( LdapException e )
        {
            String error = "deassign role name [" + entity.getName() + "] user dn [" + userDn
                + "] caught LdapException=" + e;
            throw new UpdateException( GlobalErrIds.ARLE_USER_DEASSIGN_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return entity;
    }


    /**
     * This method will completely remove the AdminRole from the directory.  It will use {@link AdminRole#name} as key.
     * This operation is performed on the {@link GlobalIds#ADMIN_ROLE_ROOT} container.
     *
     * @param role record contains {@link AdminRole#name}.
     * @throws RemoveException in the event LDAP errors occur.
     */
    void remove( AdminRole role ) throws RemoveException
    {
        LdapConnection ld = null;
        String dn = getDn( role );

        try
        {
            ld = getAdminConnection();
            delete( ld, dn, role );
        }
        catch ( LdapException e )
        {
            String error = "remove role name=" + role.getName() + " LdapException=" + e;
            throw new RemoveException( GlobalErrIds.ARLE_DELETE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * This method will retrieve the AdminRole from {@link GlobalIds#ADMIN_ROLE_ROOT} container by name.
     *
     * @param adminRole maps to {@link AdminRole#name}.
     * @return AdminRole back to client.
     * @throws FinderException in the event LDAP errors occur.
     */
    AdminRole getRole( AdminRole adminRole ) throws FinderException
    {
        AdminRole entity = null;
        LdapConnection ld = null;
        String dn = getDn( adminRole );

        try
        {
            ld = getAdminConnection();
            Entry findEntry = read( ld, dn, ROLE_ATRS );
            if ( findEntry != null )
            {
                entity = unloadLdapEntry( findEntry, 0, adminRole.getContextId() );
            }
            if ( entity == null )
            {
                String warning = "getRole name [" + adminRole.getName() + "] no entry found dn [" + dn + "]";
                throw new FinderException( GlobalErrIds.ARLE_NOT_FOUND, warning );
            }
        }
        catch ( LdapNoSuchObjectException e )
        {
            String warning = "getRole name [" + adminRole.getName() + "] Obj COULD NOT FIND ENTRY for dn [" + dn
                + "]";
            throw new FinderException( GlobalErrIds.ARLE_NOT_FOUND, warning, e );
        }
        catch ( LdapException e )
        {
            String error = "getRole dn [" + dn + "] LEXCD=" + e;
            throw new FinderException( GlobalErrIds.ARLE_READ_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     * @param adminRole
     * @return
     * @throws FinderException
     *
     */
    List<AdminRole> findRoles( AdminRole adminRole ) throws FinderException
    {
        List<AdminRole> roleList = new ArrayList<AdminRole>();
        LdapConnection ld = null;
        String roleRoot = getRootDn( adminRole.getContextId(), GlobalIds.ADMIN_ROLE_ROOT );
        String filter;

        try
        {
            String searchVal = encodeSafeText( adminRole.getName(), GlobalIds.ROLE_LEN );
            filter = GlobalIds.FILTER_PREFIX + GlobalIds.ROLE_OBJECT_CLASS_NM + ")("
                + ROLE_NM + "=" + searchVal + "*))";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, roleRoot,
                SearchScope.ONELEVEL, filter, ROLE_ATRS, false, Config.getInstance().getInt(GlobalIds.CONFIG_LDAP_MAX_BATCH_SIZE, GlobalIds.BATCH_SIZE ) );
            long sequence = 0;

            while ( searchResults.next() )
            {
                roleList.add( unloadLdapEntry( searchResults.getEntry(), sequence++, adminRole.getContextId() ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "findRoles name [" + adminRole.getName() + "] caught LdapException=" + e;
            throw new FinderException( GlobalErrIds.ARLE_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "findRoles name [" + adminRole.getName() + "] caught CursorException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.ARLE_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return roleList;
    }


    /**
     * @param adminRole
     * @param limit
     * @return
     * @throws FinderException
     *
     */
    List<String> findRoles( AdminRole adminRole, int limit ) throws FinderException
    {
        List<String> roleList = new ArrayList<String>();
        LdapConnection ld = null;
        String roleRoot = getRootDn( adminRole.getContextId(), GlobalIds.ADMIN_ROLE_ROOT );
        String filter;
        String searchVal = null;

        try
        {
            searchVal = encodeSafeText( adminRole.getName(), GlobalIds.ROLE_LEN );
            filter = GlobalIds.FILTER_PREFIX + GlobalIds.ROLE_OBJECT_CLASS_NM + ")("
                + ROLE_NM + "=" + searchVal + "*))";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, roleRoot,
                SearchScope.ONELEVEL, filter, ROLE_NM_ATR, false, limit );

            while ( searchResults.next() )
            {
                Entry entry = searchResults.getEntry();
                roleList.add( getAttribute( entry, ROLE_NM ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "findRoles name [" + searchVal + "] caught LdapException=" + e;
            throw new FinderException( GlobalErrIds.ARLE_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "findRoles name [" + searchVal + "] caught CursorException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.ARLE_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return roleList;
    }


    /**
     * @param userDn
     * @return
     * @throws FinderException
     */
    List<String> findAssignedRoles( String userDn, String contextId ) throws FinderException
    {
        List<String> roleNameList = new ArrayList<>();
        LdapConnection ld = null;
        String roleRoot = getRootDn( contextId, GlobalIds.ADMIN_ROLE_ROOT );

        try
        {
            String filter = GlobalIds.FILTER_PREFIX + GlobalIds.ROLE_OBJECT_CLASS_NM + ")";
            filter += "(" + ROLE_OCCUPANT + "=" + userDn + "))";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, roleRoot,
                SearchScope.ONELEVEL, filter, ROLE_NM_ATR, false, Config.getInstance().getInt(GlobalIds.CONFIG_LDAP_MAX_BATCH_SIZE, GlobalIds.BATCH_SIZE ) );

            while ( searchResults.next() )
            {
                roleNameList.add( getAttribute( searchResults.getEntry(), ROLE_NM ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "findAssignedRoles userDn [" + userDn + "] caught LdapException=" + e;
            throw new FinderException( GlobalErrIds.ARLE_OCCUPANT_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "findAssignedRoles userDn [" + userDn + "] caught CursorException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.ARLE_OCCUPANT_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return roleNameList;
    }


    /**
      *
      * @param contextId
      * @return
      * @throws FinderException
      */
    List<Graphable> getAllDescendants( String contextId )
        throws FinderException
    {
        String[] DESC_ATRS =
            { ROLE_NM, GlobalIds.PARENT_NODES };
        List<Graphable> descendants = new ArrayList<>();
        LdapConnection ld = null;
        String roleRoot = getRootDn( contextId, GlobalIds.ADMIN_ROLE_ROOT );
        String filter = null;

        try
        {
            filter = GlobalIds.FILTER_PREFIX + GlobalIds.ROLE_OBJECT_CLASS_NM + ")("
                + GlobalIds.PARENT_NODES + "=*))";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, roleRoot,
                SearchScope.ONELEVEL, filter, DESC_ATRS, false, Config.getInstance().getInt(GlobalIds.CONFIG_LDAP_MAX_BATCH_SIZE, GlobalIds.BATCH_SIZE ) );
            long sequence = 0;

            while ( searchResults.next() )
            {
                descendants.add( unloadDescendants( searchResults.getEntry(), sequence++ ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "getAllDescendants filter [" + filter + "] caught LdapException=" + e;
            throw new FinderException( GlobalErrIds.ARLE_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "getAllDescendants filter [" + filter + "] caught CursorException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.ARLE_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return descendants;
    }


    /**
    *
    * @param le
    * @param sequence
    * @return
     * @throws LdapInvalidAttributeValueException 
    * @throws LdapException
    */
    private Graphable unloadDescendants( Entry le, long sequence )
        throws LdapInvalidAttributeValueException
    {
        Role entity = new ObjectFactory().createRole();
        entity.setSequenceId( sequence );
        entity.setName( getAttribute( le, ROLE_NM ) );
        entity.setParents( getAttributeSet( le, GlobalIds.PARENT_NODES ) );
        return entity;
    }


    /**
     * @param le
     * @return
     * @throws LdapInvalidAttributeValueException 
     * @throws LdapException
     */
    private AdminRole unloadLdapEntry( Entry le, long sequence, String contextId )
        throws LdapInvalidAttributeValueException
    {
        AdminRole entity = new ObjectFactory().createAdminRole();
        entity.setSequenceId( sequence );
        entity.setId( getAttribute( le, GlobalIds.FT_IID ) );
        entity.setDescription( getAttribute( le, SchemaConstants.DESCRIPTION_AT ) );
        entity.setOccupants( getAttributes( le, ROLE_OCCUPANT ) );
        entity.setOsPSet( getAttributeSet( le, ROLE_OSP ) );
        entity.setOsUSet( getAttributeSet( le, ROLE_OSU ) );
        entity.setName( getAttribute( le, SchemaConstants.CN_AT ) );
        unloadTemporal( le, entity );
        entity.setRoleRangeRaw( getAttribute( le, ROLE_RANGE ) );
        entity.setParents( getAttributeSet( le, GlobalIds.PARENT_NODES ) );
        entity.setChildren( AdminRoleUtil.getChildren( entity.getName().toUpperCase(), contextId ) );
        return entity;
    }

    
    @Override
    public String getDn( AdminRole adminRole )
    {
        return SchemaConstants.CN_AT + "=" + adminRole.getName() + ","
            + getRootDn( adminRole.getContextId(), GlobalIds.ADMIN_ROLE_ROOT );
    }


    @Override
    public FortEntity getEntity( AdminRole entity ) throws FinderException
    {
        return this.getRole( entity );
    }
}
