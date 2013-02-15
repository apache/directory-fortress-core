/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import us.jts.fortress.*;
import us.jts.fortress.ldap.DataProvider;
import us.jts.fortress.ldap.PoolMgr;
import us.jts.fortress.util.time.CUtil;
import us.jts.fortress.util.attr.VUtil;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.GlobalIds;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttribute;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
//import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPCompareAttrNames;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModification;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchResults;

import java.util.ArrayList;
import java.util.List;

/**
 * This class perform data access for Fortress Role entity.
 * <p/>
 * The Fortress Role entity is a composite of the following other Fortress structural and aux object classes:
 * <h4>1. ftRls Structural objectclass is used to store the Role information like name and temporal constraint attributes</h4>
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
 *
 * @author Kevin McKinney
 */
final class RoleDAO extends DataProvider

{
    /*
      *  *************************************************************************
      *  **  OpenAccessMgr ROLE STATICS
      *  ************************************************************************
      */
    private static final String CLS_NM = RoleDAO.class.getName();
    private static final String ROLE_OCCUPANT = "roleOccupant";
    private static final String ROLE_NM = "ftRoleName";

    private static final String[] ROLE_NM_ATR = {
        ROLE_NM
    };
    private static final String[] ROLE_ATRS = {
        GlobalIds.FT_IID, ROLE_NM, GlobalIds.DESC, GlobalIds.CONSTRAINT, ROLE_OCCUPANT, GlobalIds.PARENT_NODES
    };


    /**
     * Don't let any classes outside of this package construct instance of this class.
     */
    RoleDAO()
    {}

    /**
     * @param entity
     * @return
     * @throws CreateException
     */
    final Role create(Role entity)
        throws CreateException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity.getName(), entity.getContextId());
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add(createAttributes(GlobalIds.OBJECT_CLASS, GlobalIds.ROLE_OBJ_CLASS));
            entity.setId();
            attrs.add(createAttribute(GlobalIds.FT_IID, entity.getId()));
            attrs.add(createAttribute(ROLE_NM, entity.getName()));
            // description field is optional on this object class:
            if (VUtil.isNotNullOrEmpty(entity.getDescription()))
            {
                attrs.add(createAttribute(GlobalIds.DESC, entity.getDescription()));
            }
            // CN attribute is required for this object class:
            attrs.add(createAttribute(GlobalIds.CN, entity.getName()));
            attrs.add(createAttribute(GlobalIds.CONSTRAINT, CUtil.setConstraint(entity)));

            // These multi-valued attributes are optional.  The utility function will return quietly if no items are loaded into collection:
            loadAttrs(entity.getParents(), attrs, GlobalIds.PARENT_NODES);

            LDAPEntry myEntry = new LDAPEntry(dn, attrs);
            add(ld, myEntry, entity);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".create role [" + entity.getName() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new CreateException(GlobalErrIds.ROLE_ADD_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.UpdateException
     *
     */
    final Role update(Role entity)
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity.getName(), entity.getContextId());
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            if (VUtil.isNotNullOrEmpty(entity.getDescription()))
            {
                LDAPAttribute desc = new LDAPAttribute(GlobalIds.DESC, entity.getDescription());
                mods.add(LDAPModification.REPLACE, desc);
            }
            if (VUtil.isNotNullOrEmpty(entity.getOccupants()))
            {
                for (String name : entity.getOccupants())
                {
                    LDAPAttribute occupant = new LDAPAttribute(ROLE_OCCUPANT, name);
                    mods.add(LDAPModification.ADD, occupant);
                }
            }
            if (entity.isTemporalSet())
            {
                String szRawData = CUtil.setConstraint(entity);
                if (VUtil.isNotNullOrEmpty(szRawData))
                {
                    LDAPAttribute constraint = new LDAPAttribute(GlobalIds.CONSTRAINT, szRawData);
                    mods.add(LDAPModification.REPLACE, constraint);
                }
            }
            loadAttrs(entity.getParents(), mods, GlobalIds.PARENT_NODES);
            if (mods.size() > 0)
            {
                modify(ld, dn, mods, entity);
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".update name [" + entity.getName() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException(GlobalErrIds.ROLE_UPDATE_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param entity
     * @param userDn
     * @return
     * @throws us.jts.fortress.UpdateException
     *
     */
    final Role assign(Role entity, String userDn)
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity.getName(), entity.getContextId());
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute occupant = new LDAPAttribute(ROLE_OCCUPANT, userDn);
            mods.add(LDAPModification.ADD, occupant);
            modify(ld, dn, mods, entity);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".assign role name [" + entity.getName() + "] user dn [" + userDn + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException(GlobalErrIds.ROLE_USER_ASSIGN_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param entity
     * @param userDn
     * @return
     * @throws us.jts.fortress.UpdateException
     *
     */
    final Role deassign(Role entity, String userDn)
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity.getName(), entity.getContextId());
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute occupant = new LDAPAttribute(ROLE_OCCUPANT, userDn);
            mods.add(LDAPModification.DELETE, occupant);
            modify(ld, dn, mods, entity);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".deassign role name [" + entity.getName() + "] user dn [" + userDn + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException(GlobalErrIds.ROLE_USER_DEASSIGN_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param role
     * @throws RemoveException
     */
    final void remove(Role role)
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = getDn(role.getName(), role.getContextId());
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            delete(ld, dn, role);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".remove role name=" + role.getName() + " LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new RemoveException(GlobalErrIds.ROLE_DELETE_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
    }


    /**
     * @param role
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    final Role getRole(Role role)
        throws FinderException
    {
        Role entity = null;
        LDAPConnection ld = null;
        String dn = getDn(role.getName(), role.getContextId());
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPEntry findEntry = read(ld, dn, ROLE_ATRS);
            entity = unloadLdapEntry(findEntry, 0, role.getContextId());
            if (entity == null)
            {
                String warning = CLS_NM + ".getRole no entry found dn [" + dn + "]";
                throw new FinderException(GlobalErrIds.ROLE_NOT_FOUND, warning);
            }
        }
        catch (LDAPException e)
        {
            if (e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT)
            {
                String warning = CLS_NM + ".getRole Obj COULD NOT FIND ENTRY for dn [" + dn + "]";
                throw new FinderException(GlobalErrIds.ROLE_NOT_FOUND, warning);
            }
            String error = CLS_NM + ".getRole dn [" + dn + "] LEXCD=" + e.getLDAPResultCode() + " LEXMSG=" + e;
            throw new FinderException(GlobalErrIds.ROLE_READ_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param role
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    final List<Role> findRoles(Role role)
        throws FinderException
    {
        List<Role> roleList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String roleRoot = getRootDn(role.getContextId(), GlobalIds.ROLE_ROOT);
        String filter = null;
        try
        {
            String searchVal = encodeSafeText(role.getName(), GlobalIds.ROLE_LEN);
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            filter = GlobalIds.FILTER_PREFIX + GlobalIds.ROLE_OBJECT_CLASS_NM + ")("
                + ROLE_NM + "=" + searchVal + "*))";
            searchResults = search(ld, roleRoot,
                LDAPConnection.SCOPE_ONE, filter, ROLE_ATRS, false, GlobalIds.BATCH_SIZE);
            long sequence = 0;
            while (searchResults.hasMoreElements())
            {
                roleList.add(unloadLdapEntry(searchResults.next(), sequence++, role.getContextId()));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".findRoles filter [" + filter + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException(GlobalErrIds.ROLE_SEARCH_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return roleList;
    }


    /**
     * @param role
     * @param limit
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    final List<String> findRoles(Role role, int limit)
        throws FinderException
    {
        List<String> roleList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String roleRoot = getRootDn(role.getContextId(), GlobalIds.ROLE_ROOT);
        String filter = null;
        try
        {
            String searchVal = encodeSafeText(role.getName(), GlobalIds.ROLE_LEN);
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            filter = GlobalIds.FILTER_PREFIX + GlobalIds.ROLE_OBJECT_CLASS_NM + ")("
                + ROLE_NM + "=" + searchVal + "*))";
            searchResults = search(ld, roleRoot,
                LDAPConnection.SCOPE_ONE, filter, ROLE_NM_ATR, false, GlobalIds.BATCH_SIZE, limit);
            while (searchResults.hasMoreElements())
            {
                LDAPEntry entry = searchResults.next();
                roleList.add(getAttribute(entry, ROLE_NM));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".findRoles filter [" + filter + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException(GlobalErrIds.ROLE_SEARCH_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return roleList;
    }


    /**
     *
     * @param userDn
     * @param contextId
     * @return
     * @throws FinderException
     */
    final List<String> findAssignedRoles(String userDn, String contextId)
        throws FinderException
    {
        List<String> roleNameList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String roleRoot = getRootDn(contextId, GlobalIds.ROLE_ROOT);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            String filter = GlobalIds.FILTER_PREFIX + GlobalIds.ROLE_OBJECT_CLASS_NM + ")";
            filter += "(" + ROLE_OCCUPANT + "=" + userDn + "))";
            searchResults = search(ld, roleRoot,
                LDAPConnection.SCOPE_ONE, filter, ROLE_NM_ATR, false, GlobalIds.BATCH_SIZE);
            while (searchResults.hasMoreElements())
            {
                roleNameList.add(getAttribute(searchResults.next(), ROLE_NM));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".findAssignedRoles userDn [" + userDn + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException(GlobalErrIds.ROLE_OCCUPANT_SEARCH_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return roleNameList;
    }


    /**
     *
     * @param contextId
     * @return
     * @throws FinderException
     */
    final List<Graphable> getAllDescendants(String contextId)
        throws FinderException
    {
        String[] DESC_ATRS = { ROLE_NM, GlobalIds.PARENT_NODES };
        List<Graphable> descendants = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String roleRoot = getRootDn(contextId, GlobalIds.ROLE_ROOT);
        String filter = null;
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            filter = GlobalIds.FILTER_PREFIX + GlobalIds.ROLE_OBJECT_CLASS_NM + ")("
                + GlobalIds.PARENT_NODES + "=*))";
            searchResults = search(ld, roleRoot,
                LDAPConnection.SCOPE_ONE, filter, DESC_ATRS, false, GlobalIds.BATCH_SIZE);
            long sequence = 0;
            while (searchResults.hasMoreElements())
            {
                descendants.add(unloadDescendants(searchResults.next(), sequence++, contextId));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".getAllDescendants filter [" + filter + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException(GlobalErrIds.ROLE_SEARCH_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
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
    private Graphable unloadDescendants(LDAPEntry le, long sequence, String contextId)
        throws LDAPException
    {
        Role entity = new ObjectFactory().createRole();
        entity.setSequenceId(sequence);
        entity.setName(getAttribute(le, ROLE_NM));
        entity.setParents(getAttributeSet(le, GlobalIds.PARENT_NODES));
        return entity;
    }

    /**
     *
     * @param le
     * @param sequence
     * @param contextId
     * @return
     * @throws LDAPException
     */
    private Role unloadLdapEntry(LDAPEntry le, long sequence, String contextId)
        throws LDAPException
    {
        Role entity = new ObjectFactory().createRole();
        entity.setSequenceId(sequence);
        entity.setId(getAttribute(le, GlobalIds.FT_IID));
        entity.setName(getAttribute(le, ROLE_NM));
        entity.setDescription(getAttribute(le, GlobalIds.DESC));
        entity.setOccupants(getAttributes(le, ROLE_OCCUPANT));
        //entity.setParents(RoleUtil.getParents(entity.getName().toUpperCase(), contextId));
        entity.setChildren(RoleUtil.getChildren(entity.getName().toUpperCase(), contextId));
        entity.setParents(getAttributeSet(le, GlobalIds.PARENT_NODES));
        unloadTemporal(le, entity);
        return entity;
    }

    private String getDn(String name, String contextId)
    {
        return GlobalIds.CN + "=" + name + "," + getRootDn(contextId, GlobalIds.ROLE_ROOT);
    }
}