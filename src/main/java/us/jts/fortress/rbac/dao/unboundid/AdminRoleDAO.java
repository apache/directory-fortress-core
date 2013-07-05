/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac.dao.unboundid;


import java.util.ArrayList;
import java.util.List;

import us.jts.fortress.CreateException;
import us.jts.fortress.FinderException;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.GlobalIds;
import us.jts.fortress.ObjectFactory;
import us.jts.fortress.RemoveException;
import us.jts.fortress.UpdateException;
import us.jts.fortress.ldap.DataProvider;
import us.jts.fortress.rbac.AdminRole;
import us.jts.fortress.rbac.AdminRoleUtil;
import us.jts.fortress.rbac.Graphable;
import us.jts.fortress.rbac.Role;
import us.jts.fortress.rbac.process.AdminRoleP;
import us.jts.fortress.util.attr.VUtil;
import us.jts.fortress.util.time.CUtil;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttribute;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModification;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchResults;


/**
 * The AdminRoleDAO is called by {@link AdminRoleP} and processes data via its entity {@link AdminRole}.
 * <p/>
 * The Fortress AdminRoleDAO uses the following other Fortress structural and aux object classes:
 * <h4>1. ftRls Structural objectclass is used to store the AdminRole information like name, and temporal constraints</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass	( 1.3.6.1.4.1.38088.2.1</code>
 * <li> <code>NAME 'ftRls'</code>
 * <li> <code>DESC 'Fortress Role Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftRoleName )</code>
 * <li> <code>MAY ( description $ ftCstr ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>2. ftProperties AUXILIARY Object Class is used to store client specific name/value pairs on target entity</h4>
 * <code># This aux object class can be used to store custom attributes.</code><br />
 * <code># The properties collections consist of name/value pairs and are not constrainted by Fortress.</code><br />
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
 * <p/>
 * This class is thread safe.
 *
 * @author Shawn McKinney
 */
public final class AdminRoleDAO extends DataProvider
{
    private static final String CLS_NM = AdminRoleDAO.class.getName();
    private static final String ROLE_OCCUPANT = "roleOccupant";
    private static final String ROLE_OSP = "ftOSP";
    private static final String ROLE_OSU = "ftOSU";
    private static final String ROLE_RANGE = "ftRange";
    private static final String POOLS_AUX_OBJECT_CLASS_NAME = "ftPools";
    private static final String ADMIN_ROLE_OBJ_CLASS[] =
        {
            GlobalIds.TOP,
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
            GlobalIds.DESC,
            GlobalIds.CONSTRAINT,
            ROLE_OCCUPANT,
            ROLE_OSP,
            ROLE_OSU,
            ROLE_RANGE,
            GlobalIds.PARENT_NODES
    };


    public AdminRoleDAO()
    {
    }


    /**
     * Create a new AdminRole entity using supplied data.  Required attribute is {@link AdminRole#name}.
     * This data will be stored in the {@link GlobalIds#ADMIN_ROLE_ROOT} container.
     *
     * @param entity record contains AdminRole data.  Null attributes will be ignored.
     * @return input record back to client.
     * @throws us.jts.fortress.CreateException in the event LDAP errors occur.
     */
    public final AdminRole create( AdminRole entity )
        throws CreateException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity );
        try
        {
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add( createAttributes( GlobalIds.OBJECT_CLASS, ADMIN_ROLE_OBJ_CLASS ) );
            entity.setId();
            attrs.add( createAttribute( GlobalIds.FT_IID, entity.getId() ) );
            attrs.add( createAttribute( ROLE_NM, entity.getName() ) );
            // description field is optional on this object class:
            if ( VUtil.isNotNullOrEmpty( entity.getDescription() ) )
            {
                attrs.add( createAttribute( GlobalIds.DESC, entity.getDescription() ) );
            }
            // CN attribute is required for this object class:
            attrs.add( createAttribute( GlobalIds.CN, entity.getName() ) );
            attrs.add( createAttribute( GlobalIds.CONSTRAINT, CUtil.setConstraint( entity ) ) );
            loadAttrs( entity.getOsP(), attrs, ROLE_OSP );
            loadAttrs( entity.getOsU(), attrs, ROLE_OSU );
            String szRaw = entity.getRoleRangeRaw();
            if ( VUtil.isNotNullOrEmpty( szRaw ) )
            {
                attrs.add( createAttribute( ROLE_RANGE, szRaw ) );
            }
            // These multi-valued attributes are optional.  The utility function will return quietly if no items are loaded into collection:
            loadAttrs( entity.getParents(), attrs, GlobalIds.PARENT_NODES );

            LDAPEntry myEntry = new LDAPEntry( dn, attrs );
            ld = getAdminConnection();
            add( ld, myEntry, entity );
        }
        catch ( LDAPException e )
        {
            String error = "create role [" + entity.getName() + "] caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
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
    public final AdminRole update( AdminRole entity )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity );
        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();
            if ( VUtil.isNotNullOrEmpty( entity.getDescription() ) )
            {
                LDAPAttribute desc = new LDAPAttribute( GlobalIds.DESC, entity.getDescription() );
                mods.add( LDAPModification.REPLACE, desc );
            }
            if ( VUtil.isNotNullOrEmpty( entity.getOccupants() ) )
            {
                for ( String name : entity.getOccupants() )
                {
                    LDAPAttribute occupant = new LDAPAttribute( ROLE_OCCUPANT, name );
                    mods.add( LDAPModification.REPLACE, occupant );
                }
            }
            if ( entity.isTemporalSet() )
            {
                String szRawData = CUtil.setConstraint( entity );
                if ( VUtil.isNotNullOrEmpty( szRawData ) )
                {
                    LDAPAttribute constraint = new LDAPAttribute( GlobalIds.CONSTRAINT, szRawData );
                    mods.add( LDAPModification.REPLACE, constraint );
                }
            }
            loadAttrs( entity.getOsU(), mods, ROLE_OSU );
            loadAttrs( entity.getOsP(), mods, ROLE_OSP );
            String szRaw = entity.getRoleRangeRaw();
            if ( VUtil.isNotNullOrEmpty( szRaw ) )
            {
                LDAPAttribute raw = new LDAPAttribute( ROLE_RANGE, szRaw );
                mods.add( LDAPModification.REPLACE, raw );
            }
            loadAttrs( entity.getParents(), mods, GlobalIds.PARENT_NODES );
            if ( mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, dn, mods, entity );
            }
        }
        catch ( LDAPException e )
        {
            String error = "update name [" + entity.getName() + "] caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
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
    public final void deleteParent( AdminRole entity )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity );
        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute occupant = new LDAPAttribute( GlobalIds.PARENT_NODES );
            mods.add( LDAPModification.DELETE, occupant );
            ld = getAdminConnection();
            modify( ld, dn, mods, entity );
        }
        catch ( LDAPException e )
        {
            String error = "deleteParent name [" + entity.getName() + "] caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
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
    public final AdminRole assign( AdminRole entity, String userDn )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity );
        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute occupant = new LDAPAttribute( ROLE_OCCUPANT, userDn );
            mods.add( LDAPModification.ADD, occupant );
            ld = getAdminConnection();
            modify( ld, dn, mods, entity );
        }
        catch ( LDAPException e )
        {
            String error = "assign role name [" + entity.getName() + "] user dn [" + userDn + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
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
    public final AdminRole deassign( AdminRole entity, String userDn )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity );
        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute occupant = new LDAPAttribute( ROLE_OCCUPANT, userDn );
            mods.add( LDAPModification.DELETE, occupant );
            ld = getAdminConnection();
            modify( ld, dn, mods, entity );
        }
        catch ( LDAPException e )
        {
            String error = "deassign role name [" + entity.getName() + "] user dn [" + userDn
                + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
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
    public final void remove( AdminRole role )
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = getDn( role );
        try
        {
            ld = getAdminConnection();
            delete( ld, dn, role );
        }
        catch ( LDAPException e )
        {
            String error = "remove role name=" + role.getName() + " LDAPException=" + e.getLDAPResultCode() + " msg="
                + e.getMessage();
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
    public final AdminRole getRole( AdminRole adminRole )
        throws FinderException
    {
        AdminRole entity = null;
        LDAPConnection ld = null;
        String dn = getDn( adminRole );
        try
        {
            ld = getAdminConnection();
            LDAPEntry findEntry = read( ld, dn, ROLE_ATRS );
            entity = unloadLdapEntry( findEntry, 0, adminRole.getContextId() );
            if ( entity == null )
            {
                String warning = "getRole name [" + adminRole.getName() + "] no entry found dn [" + dn + "]";
                throw new FinderException( GlobalErrIds.ARLE_NOT_FOUND, warning );
            }
        }
        catch ( LDAPException e )
        {
            if ( e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT )
            {
                String warning = "getRole name [" + adminRole.getName() + "] Obj COULD NOT FIND ENTRY for dn [" + dn
                    + "]";
                throw new FinderException( GlobalErrIds.ARLE_NOT_FOUND, warning );
            }
            String error = "getRole dn [" + dn + "] LEXCD=" + e.getLDAPResultCode() + " LEXMSG=" + e;
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
    public final List<AdminRole> findRoles( AdminRole adminRole )
        throws FinderException
    {
        List<AdminRole> roleList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String roleRoot = getRootDn( adminRole.getContextId(), GlobalIds.ADMIN_ROLE_ROOT );
        String filter;
        try
        {
            String searchVal = encodeSafeText( adminRole.getName(), GlobalIds.ROLE_LEN );
            filter = GlobalIds.FILTER_PREFIX + GlobalIds.ROLE_OBJECT_CLASS_NM + ")("
                + ROLE_NM + "=" + searchVal + "*))";
            ld = getAdminConnection();
            searchResults = search( ld, roleRoot,
                LDAPConnection.SCOPE_ONE, filter, ROLE_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;
            while ( searchResults.hasMoreElements() )
            {
                roleList.add( unloadLdapEntry( searchResults.next(), sequence++, adminRole.getContextId() ) );
            }
        }
        catch ( LDAPException e )
        {
            String error = "findRoles name [" + adminRole.getName() + "] caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
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
    public final List<String> findRoles( AdminRole adminRole, int limit )
        throws FinderException
    {
        List<String> roleList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String roleRoot = getRootDn( adminRole.getContextId(), GlobalIds.ADMIN_ROLE_ROOT );
        String filter;
        String searchVal = null;
        try
        {
            searchVal = encodeSafeText( adminRole.getName(), GlobalIds.ROLE_LEN );
            filter = GlobalIds.FILTER_PREFIX + GlobalIds.ROLE_OBJECT_CLASS_NM + ")("
                + ROLE_NM + "=" + searchVal + "*))";
            ld = getAdminConnection();
            searchResults = search( ld, roleRoot,
                LDAPConnection.SCOPE_ONE, filter, ROLE_NM_ATR, false, GlobalIds.BATCH_SIZE, limit );
            while ( searchResults.hasMoreElements() )
            {
                LDAPEntry entry = searchResults.next();
                roleList.add( getAttribute( entry, ROLE_NM ) );
            }
        }
        catch ( LDAPException e )
        {
            String error = "findRoles name [" + searchVal + "] caught LDAPException=" + e.getLDAPResultCode() + " msg="
                + e.getMessage();
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
    public final List<String> findAssignedRoles( String userDn, String contextId )
        throws FinderException
    {
        List<String> roleNameList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String roleRoot = getRootDn( contextId, GlobalIds.ADMIN_ROLE_ROOT );
        try
        {
            String filter = GlobalIds.FILTER_PREFIX + GlobalIds.ROLE_OBJECT_CLASS_NM + ")";
            filter += "(" + ROLE_OCCUPANT + "=" + userDn + "))";
            ld = getAdminConnection();
            searchResults = search( ld, roleRoot,
                LDAPConnection.SCOPE_ONE, filter, ROLE_NM_ATR, false, GlobalIds.BATCH_SIZE );
            while ( searchResults.hasMoreElements() )
            {
                roleNameList.add( getAttribute( searchResults.next(), ROLE_NM ) );
            }
        }
        catch ( LDAPException e )
        {
            String error = "findAssignedRoles userDn [" + userDn + "] caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
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
    public final List<Graphable> getAllDescendants( String contextId )
        throws FinderException
    {
        String[] DESC_ATRS =
            { ROLE_NM, GlobalIds.PARENT_NODES };
        List<Graphable> descendants = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String roleRoot = getRootDn( contextId, GlobalIds.ADMIN_ROLE_ROOT );
        String filter = null;
        try
        {
            filter = GlobalIds.FILTER_PREFIX + GlobalIds.ROLE_OBJECT_CLASS_NM + ")("
                + GlobalIds.PARENT_NODES + "=*))";
            ld = getAdminConnection();
            searchResults = search( ld, roleRoot,
                LDAPConnection.SCOPE_ONE, filter, DESC_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;
            while ( searchResults.hasMoreElements() )
            {
                descendants.add( unloadDescendants( searchResults.next(), sequence++, contextId ) );
            }
        }
        catch ( LDAPException e )
        {
            String error = "getAllDescendants filter [" + filter + "] caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
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
    * @param contextId
    * @return
    * @throws LDAPException
    */
    private Graphable unloadDescendants( LDAPEntry le, long sequence, String contextId )
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
     * @throws LDAPException
     */
    private AdminRole unloadLdapEntry( LDAPEntry le, long sequence, String contextId )
    {
        AdminRole entity = new ObjectFactory().createAdminRole();
        entity.setSequenceId( sequence );
        entity.setId( getAttribute( le, GlobalIds.FT_IID ) );
        entity.setName( getAttribute( le, ROLE_NM ) );
        entity.setDescription( getAttribute( le, GlobalIds.DESC ) );
        entity.setOccupants( getAttributes( le, ROLE_OCCUPANT ) );
        entity.setOsP( getAttributeSet( le, ROLE_OSP ) );
        entity.setOsU( getAttributeSet( le, ROLE_OSU ) );
        unloadTemporal( le, entity );
        entity.setRoleRangeRaw( getAttribute( le, ROLE_RANGE ) );
        //entity.setParents(AdminRoleUtil.getParents(entity.getName().toUpperCase(), contextId));
        entity.setParents( getAttributeSet( le, GlobalIds.PARENT_NODES ) );
        entity.setChildren( AdminRoleUtil.getChildren( entity.getName().toUpperCase(), contextId ) );
        return entity;
    }


    private String getDn( AdminRole adminRole )
    {
        return GlobalIds.CN + "=" + adminRole.getName() + ","
            + getRootDn( adminRole.getContextId(), GlobalIds.ADMIN_ROLE_ROOT );
    }

}
