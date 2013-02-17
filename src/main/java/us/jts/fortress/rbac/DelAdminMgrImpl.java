/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import us.jts.fortress.AdminMgr;
import us.jts.fortress.AdminMgrFactory;
import us.jts.fortress.DelAdminMgr;
import us.jts.fortress.SecurityException;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.util.time.CUtil;
import us.jts.fortress.util.attr.AttrHelper;
import us.jts.fortress.util.attr.VUtil;

import java.util.List;

/**
 * This class implements the ARBAC02 DelAdminMgr interface for performing policy administration of Fortress ARBAC entities
 * that reside in LDAP directory.
 * These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification describes delegated administrative
 * operations for the creation and maintenance of ARBAC element sets and relations.  Delegated administrative review functions for performing administrative queries
 * and system functions for creating and managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 * This class is NOT thread safe.
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="../doc-files/ARbac.png">
 * <p/>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without sacrificing regulations for accountability or traceability.
 * <p/>
 * This class is NOT thread safe if parent instance variables ({@link #contextId} or {@link #adminSess}) are set.
 *
 * @author Shawn McKinney
 */
public final class DelAdminMgrImpl extends Manageable implements DelAdminMgr
{
    private static final String CLS_NM = DelAdminMgrImpl.class.getName();
    private static final OrgUnitP ouP = new OrgUnitP();
    private static final AdminRoleP admRP = new AdminRoleP();
    private static final PermP permP = new PermP();
    private static final UserP userP = new UserP();

    // package private constructor ensures outside classes cannot use:
    DelAdminMgrImpl()
    {}

    /**
     * This command creates a new admin role. The command is valid if and only if the new admin role is not
     * already a member of the ADMIN ROLES data set. The ADMIN ROLES data set is updated.
     * Initially, no user or permission is assigned to the new role.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link AdminRole#name} - contains the name of the new AdminRole being targeted for addition to LDAP</li>
     * </ul>
     * <p/>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link AdminRole#description} - contains any safe text</li>
     * <li>{@link AdminRole#osPs} * - multi-occurring attribute used to set associations to existing PERMS OrgUnits</li>
     * <li>{@link AdminRole#osUs} * - multi-occurring attribute used to set associations to existing USERS OrgUnits</li>
     * <li>{@link AdminRole#beginRange} - contains the name of an existing RBAC Role that represents the lowest role in hierarchy that administrator (whoever has this AdminRole activated) controls</li>
     * <li>{@link AdminRole#endRange} - contains the name of an existing RBAC Role that represents that highest role in hierarchy that administrator may control</li>
     * <li>{@link AdminRole#beginInclusive} - if 'true' the RBAC Role specified in beginRange is also controlled by the posessor of this AdminRole</li>
     * <li>{@link AdminRole#endInclusive} - if 'true' the RBAC Role specified in endRange is also controlled by the administratrator</li>
     * <li>{@link AdminRole#beginTime} - HHMM - determines begin hour adminRole may be activated into user's ARBAC session</li>
     * <li>{@link AdminRole#endTime} - HHMM - determines end hour adminRole may be activated into user's ARBAC session.</li>
     * <li>{@link AdminRole#beginDate} - YYYYMMDD - determines date when adminRole may be activated into user's ARBAC session</li>
     * <li>{@link AdminRole#endDate} - YYYYMMDD - indicates latest date adminRole may be activated into user's ARBAC session</li>
     * <li>{@link AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     * <li>{@link AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     * <li>{@link AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's ARBAC session</li>
     * </ul>
     *
     * @param role Contains role name and description.
     * @return AdminRole contains reference to entity operated on.
     * @throws us.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    @Override
    public AdminRole addRole(AdminRole role)
        throws SecurityException
    {
        String methodName = "addRole";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ARLE_NULL);
        setEntitySession(CLS_NM, methodName, role);
        return admRP.add(role);
    }

    /**
     * This command deletes an existing admin role from the ARBAC database. The command is valid
     * if and only if the role to be deleted is a member of the ADMIN ROLES data set.  This command will
     * also deassign role from all users.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link AdminRole#name} - contains the name of the new AdminRole being targeted for removal</li>
     * </ul>
     * <p/>
     *
     * @param role Contains role name.
     * @throws us.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    @Override
    public void deleteRole(AdminRole role)
        throws SecurityException
    {
        String methodName = "deleteRole";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ARLE_NULL);
        setEntitySession(CLS_NM, methodName, role);
        int numChildren = AdminRoleUtil.numChildren(role.getName(), role.getContextId());
        if (numChildren > 0)
        {
            String error = CLS_NM + "." + methodName + " role [" + role.getName() + "] must remove [" + numChildren + "] descendants before deletion";
            throw new SecurityException(GlobalErrIds.HIER_DEL_FAILED_HAS_CHILD, error, null);
        }
        // search for all users assigned this role and deassign:
        List<User> users = userP.getAssignedUsers(role);
        if (users != null)
        {
            for (User ue : users)
            {
                User user = new User(ue.getUserId());
                UserAdminRole uAdminRole = new UserAdminRole(ue.getUserId(), role.getName());
                uAdminRole.setContextId(contextId);
                setAdminData(CLS_NM, methodName, user);
                deassignUser(uAdminRole);
            }
        }
        permP.remove(role);
        admRP.delete(role);
    }

    /**
     * Method will update an AdminRole entity in the directory.  The role must exist in directory prior to this call.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link AdminRole#name} - contains the name of the new AdminRole being targeted for updating</li>
     * </ul>
     * <p/>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link AdminRole#description} - contains any safe text</li>
     * <li>{@link AdminRole#osPs} * - multi-occurring attribute used to set associations to existing PERMS OrgUnits</li>
     * <li>{@link AdminRole#osUs} * - multi-occurring attribute used to set associations to existing USERS OrgUnits</li>
     * <li>{@link AdminRole#beginRange} - contains the name of an existing RBAC Role that represents the lowest role in hierarchy that administrator (whoever has this AdminRole activated) controls</li>
     * <li>{@link AdminRole#endRange} - contains the name of an existing RBAC Role that represents that highest role in hierarchy that administrator may control</li>
     * <li>{@link AdminRole#beginInclusive} - if 'true' the RBAC Role specified in beginRange is also controlled by the posessor of this AdminRole</li>
     * <li>{@link AdminRole#endInclusive} - if 'true' the RBAC Role specified in endRange is also controlled by the administratrator</li>
     * <li>{@link AdminRole#beginTime} - HHMM - determines begin hour adminRole may be activated into user's ARBAC session</li>
     * <li>{@link AdminRole#endTime} - HHMM - determines end hour adminRole may be activated into user's ARBAC session.</li>
     * <li>{@link AdminRole#beginDate} - YYYYMMDD - determines date when adminRole may be activated into user's ARBAC session</li>
     * <li>{@link AdminRole#endDate} - YYYYMMDD - indicates latest date adminRole may be activated into user's ARBAC session</li>
     * <li>{@link AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     * <li>{@link AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     * <li>{@link AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's ARBAC session</li>
     * </ul>
     *
     * @param role Contains role name and new description.
     * @return AdminRole contains reference to entity operated on.
     * @throws us.jts.fortress.SecurityException
     *          Description of the Exception
     */
    @Override
    public AdminRole updateRole(AdminRole role)
        throws SecurityException
    {
        String methodName = "updateRole";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ARLE_NULL);
        setEntitySession(CLS_NM, methodName, role);
        AdminRole re = admRP.update(role);
        // search for all users assigned this role and update:
        List<User> users = userP.getAssignedUsers(role);
        if(VUtil.isNotNullOrEmpty(users))
        {
            final AdminMgr aMgr = AdminMgrFactory.createInstance(this.contextId);
            for (User ue : users)
            {
                User upUe = new User(ue.getUserId());
                setAdminData(CLS_NM, methodName, upUe);
                List<UserAdminRole> uaRoles = ue.getAdminRoles();
                UserAdminRole chgRole = new UserAdminRole();
                chgRole.setName(role.getName());
                chgRole.setUserId(ue.getUserId());
                chgRole.setOsP(role.getOsP());
                chgRole.setOsU(role.getOsU());
                uaRoles.remove(chgRole);
                CUtil.copy(re, chgRole);
                uaRoles.add(chgRole);
                upUe.setUserId(ue.getUserId());
                upUe.setAdminRole(chgRole);
                aMgr.updateUser(upUe);
            }
        }
        return re;
    }


    /**
     * This command assigns a user to an admin role.
     * Successful completion of this op, the following occurs:
     * </p>
     * <ul>
     * <li> User entity (resides in people container) has role assignment added to aux object class attached to actual user record.
     * <li> AdminRole entity (resides in admin role container) has userId added as role occupant.
     * <li> (optional) Temporal constraints may be associated with <code>ftUserAttrs</code> aux object class based on:
     * <ul>
     * <li> timeout - number in seconds of session inactivity time allowed.
     * <li> beginDate - YYYYMMDD - determines date when role may be activated.
     * <li> endDate - YYMMDD - indicates latest date role may be activated.
     * <li> beginLockDate - YYYYMMDD - determines beginning of enforced inactive status
     * <li> endLockDate - YYMMDD - determines end of enforced inactive status.
     * <li> beginTime - HHMM - determines begin hour role may be activated in user's session.
     * <li> endTime - HHMM - determines end hour role may be activated in user's session.*
     * <li> dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day of week role may be activated.
     * </ul>
     * </ul>
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#name} - contains the name for already existing AdminRole to be assigned</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#userId} - contains the userId for existing User</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#beginTime} - HHMM - determines begin hour AdminRole may be activated into user's RBAC session</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#endTime} - HHMM - determines end hour AdminRole may be activated into user's RBAC session.</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#beginDate} - YYYYMMDD - determines date when AdminRole may be activated into user's RBAC session</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#endDate} - YYYYMMDD - indicates latest date AdminRole may be activated into user's RBAC session</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's ARBAC session</li>
     * </ul>
     *
     * @param uAdminRole entity contains {@link us.jts.fortress.rbac.User#userId} and {@link us.jts.fortress.rbac.AdminRole#name} and optional {@code us.jts.fortress.rbac.Constraints}.
     * @throws SecurityException in the event data error in user or role objects or system error.
     */
    @Override
    public void assignUser(UserAdminRole uAdminRole)
        throws SecurityException
    {
        String methodName = "assignUser";
        assertContext(CLS_NM, methodName, uAdminRole, GlobalErrIds.ARLE_NULL);
        setEntitySession(CLS_NM, methodName, uAdminRole);

        AdminRole adminRole = new AdminRole(uAdminRole.getName());
        adminRole.setContextId(uAdminRole.getContextId());
        // retrieve the admin role info:
        AdminRole validRole = admRP.read(adminRole);

        // if the UserAdminRole entity doesn't have temporal constraints set already, copy from the AdminRole declaration:
        // if the input role entity attribute doesn't have temporal constraints set, copy from the role declaration:
        CUtil.validateOrCopy(validRole, uAdminRole);

        // copy the ARBAC AdminRole attributes to UserAdminRole:
        AttrHelper.copyAdminAttrs(validRole, uAdminRole);
        String dn = userP.assign(uAdminRole);
        // copy the admin session info to AdminRole:
        setAdminData(CLS_NM, methodName, validRole);
        // Assign user dn attribute to the adminRole, this will add a single, standard attribute value, called "roleOccupant", directly onto the adminRole node:
        admRP.assign(validRole, dn);
    }


    /**
     * This method removes assigned admin role from user entity.  Both user and admin role entities must exist and have role relationship
     * before calling this method.
     * Successful completion:
     * del Role to User assignment in User data set
     * AND
     * User to Role assignment in Admin Role data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#name} - contains the name for already existing AdminRole to be deassigned</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#userId} - contains the userId for existing User</li>
     * </ul>
     *
     * @param uAdminRole entity contains {@link us.jts.fortress.rbac.User#userId} and {@link us.jts.fortress.rbac.AdminRole#name}.
     * @throws SecurityException - in the event data error in user or role objects or system error.
     */
    @Override
    public void deassignUser(UserAdminRole uAdminRole)
        throws SecurityException
    {
        String methodName = "deassignUser";
        assertContext(CLS_NM, methodName, uAdminRole, GlobalErrIds.ARLE_NULL);
        setEntitySession(CLS_NM, methodName, uAdminRole);
        String dn = userP.deassign(uAdminRole);
        AdminRole adminRole = new AdminRole(uAdminRole.getName());
        // copy the ARBAC attributes to AdminRole:
        setAdminData(CLS_NM, methodName, adminRole);
        // Deassign user dn attribute to the adminRole, this will remove a single, standard attribute value, called "roleOccupant", directly onto the adminRole node:
        admRP.deassign(adminRole, dn);
    }


    /**
     * Commands adds a new OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit to be added</li>
     * <li>{@link us.jts.fortress.rbac.OrgUnit#type} - contains the type of OU:  {@link us.jts.fortress.rbac.OrgUnit.Type#USER} or {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.OrgUnit#description} - contains any safe text</li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return OrgUnit contains reference to entity added.
     * @throws us.jts.fortress.SecurityException
     *          in the event of data validation or system error.
     */
    @Override
    public OrgUnit add(OrgUnit entity)
        throws SecurityException
    {
        String methodName = "addOU";
        assertContext(CLS_NM, methodName, entity, GlobalErrIds.ORG_NULL);
        setEntitySession(CLS_NM, methodName, entity);
        VUtil.assertNotNull(entity.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        return ouP.add(entity);
    }

    /**
     * Commands updates existing OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit to be updated</li>
     * <li>{@link us.jts.fortress.rbac.OrgUnit#type} - contains the type of OU:  {@link us.jts.fortress.rbac.OrgUnit.Type#USER} or {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.OrgUnit#description} - contains any safe text</li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return OrgUnit contains reference to entity operated on.
     * @throws us.jts.fortress.SecurityException
     *          in the event of data validation or system error.
     */
    @Override
    public OrgUnit update(OrgUnit entity)
        throws SecurityException
    {
        String methodName = "updateOU";
        assertContext(CLS_NM, methodName, entity, GlobalErrIds.ORG_NULL);
        setEntitySession(CLS_NM, methodName, entity);
        VUtil.assertNotNull(entity.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        return ouP.update(entity);
    }

    /**
     * Commands deletes existing OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit to be removed</li>
     * <li>{@link us.jts.fortress.rbac.OrgUnit#type} - contains the type of OU:  {@link us.jts.fortress.rbac.OrgUnit.Type#USER} or {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return OrgUnit contains reference to entity operated on.
     * @throws SecurityException in the event of data validation or system error.
     */
    @Override
    public OrgUnit delete(OrgUnit entity)
        throws SecurityException
    {
        String methodName = "deleteOU";
        assertContext(CLS_NM, methodName, entity, GlobalErrIds.ORG_NULL);
        setEntitySession(CLS_NM, methodName, entity);
        VUtil.assertNotNull(entity.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        int numChildren;
        if (entity.getType() == OrgUnit.Type.USER)
        {
            numChildren = UsoUtil.numChildren(entity.getName(), entity.getContextId());
        }
        else
        {
            numChildren = PsoUtil.numChildren(entity.getName(), entity.getContextId());
        }
        if (numChildren > 0)
        {
            String error = CLS_NM + "." + methodName + " orgunit [" + entity.getName() + "] must remove [" + numChildren + "] descendants before deletion";
            throw new SecurityException(GlobalErrIds.HIER_DEL_FAILED_HAS_CHILD, error, null);
        }
        if (entity.getType() == OrgUnit.Type.USER)
        {
            // Ensure the org unit is not assigned to any users, but set the sizeLimit to "true" to limit result set size.
            List<User> assignedUsers = userP.search(entity, true);
            if (VUtil.isNotNullOrEmpty(assignedUsers))
            {
                String error = CLS_NM + "." + methodName + " orgunit [" + entity.getName() + "] must unassign [" + assignedUsers.size() + "] users before deletion";
                throw new SecurityException(GlobalErrIds.ORG_DEL_FAILED_USER, error, null);
            }
        }
        else
        {
            // Ensure the org unit is not assigned to any permission objects but set the sizeLimit to "true" to limit result set size..
            // pass a "false" which places no restrictions on how many records server returns.
            List<PermObj> assignedPerms = permP.search(entity, false);
            if (VUtil.isNotNullOrEmpty(assignedPerms))
            {
                String error = CLS_NM + "." + methodName + " orgunit [" + entity.getName() + "] must unassign [" + assignedPerms.size() + "] perm objs before deletion";
                throw new SecurityException(GlobalErrIds.ORG_DEL_FAILED_PERM, error, null);
            }
        }
        // everything checked out good - remove the org unit from the OrgUnit data set:
        return ouP.delete(entity);
    }


    /**
     * This command creates a new orgunit child, and inserts it in the orgunit hierarchy as an immediate descendant of
     * the existing orgunit parent.
     * <p>
     * The command is valid if and only if:
     * <ul>
     * <li> The child orgunit is not a member of the ORGUNITS data set.
     * <li> The parent orgunit is a member of the ORGUNITS data set.
     * </ul>
     * </p>
     * <p> This method:
     * <ul>
     * <li> Adds new orgunit.
     * <li> Assigns orgunit relationship between new child and pre-existing parent.
     * </ul>
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to be parent</li>
     * <li>parentRole - {@link us.jts.fortress.rbac.OrgUnit#type} - contains the type of OrgUnit targeted: {@link us.jts.fortress.rbac.OrgUnit.Type#USER} or {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * <li>childRole - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of new OrgUnit to be child</li>
     * </ul>
     * <h4>optional parameters child</h4>
     * <ul>
     * <li>childRole - {@link us.jts.fortress.rbac.OrgUnit#description} - maps to description attribute on organizationalUnit object class for new child</li>
     * </ul>
     *
     * @param parent This entity must be present in ORGUNIT data set.  Success will add rel with child.
     * @param child  This entity must not be present in ORGUNIT data set.  Success will add the new entity to ORGUNIT data set.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void addDescendant(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "addDescendantOU";
        assertContext(CLS_NM, methodName, parent, GlobalErrIds.ORG_PARENT_NULL);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        assertContext(CLS_NM, methodName, child, GlobalErrIds.ORG_CHILD_NULL);
        setEntitySession(CLS_NM, methodName, child);

        // ensure the parent OrgUnit exists:
        ouP.read(parent);
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.validateRelationship(child, parent, false);
        }
        else
        {
            PsoUtil.validateRelationship(child, parent, false);
        }
        child.setParent(parent.getName());
        ouP.add(child);
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.ADD);
        }
        else
        {
            PsoUtil.updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.ADD);
        }
    }

    /**
     * This command creates a new orgunit parent, and inserts it in the orgunit hierarchy as an immediate ascendant of
     * the existing child orgunit.
     * <p>
     * The command is valid if and only if:
     * <ul>
     * <li> The parent is not a member of the ORGUNITS data set.
     * <li> The child is a member of the ORGUNITS data set.
     * </ul>
     * </p>
     * <p> This method:
     * <ul>
     * <li> Adds new orgunit.
     * <li> Assigns orgunit relationship between new parent and pre-existing child.
     * </ul>
     * <h4>required parameters</h4>
     * <ul>
     * <li>parent - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to be parent</li>
     * <li>parent - {@link us.jts.fortress.rbac.OrgUnit#type} - contains the type of OrgUnit targeted: {@link us.jts.fortress.rbac.OrgUnit.Type#USER} or {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * <li>child - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of new OrgUnit to be child</li>
     * </ul>
     * <h4>optional parameters child</h4>
     * <ul>
     * <li>child - {@link us.jts.fortress.rbac.OrgUnit#description} - maps to description attribute on organizationalUnit object class for new child</li>
     * </ul>
     *
     * @param parent completion of op assigns new child relationship with child orgunit.
     * @param child  completion of op assigns new parent relationship with parent orgunit.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void addAscendant(OrgUnit child, OrgUnit parent)
        throws SecurityException
    {
        String methodName = "addAscendantOU";
        assertContext(CLS_NM, methodName, parent, GlobalErrIds.ORG_PARENT_NULL);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        setEntitySession(CLS_NM, methodName, parent);
        assertContext(CLS_NM, methodName, child, GlobalErrIds.ORG_CHILD_NULL);

        // ensure the child OrgUnit exists:
        OrgUnit newChild = ouP.read(child);
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.validateRelationship(child, parent, false);
        }
        else
        {
            PsoUtil.validateRelationship(child, parent, false);
        }
        ouP.add(parent);
        newChild.setParent(parent.getName());
        newChild.setContextId(this.contextId);
        ouP.update(newChild);
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.ADD);
        }
        else
        {
            PsoUtil.updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.ADD);
        }
    }

    /**
     * This command establishes a new immediate inheritance relationship with parent orgunit <<-- child orgunit
     * <p>
     * The command is valid if and only if:
     * <ul>
     * <li> The parent and child are members of the ORGUNITS data set.
     * <li> The parent is not an immediate ascendant of child.
     * <li> The child does not properly inherit parent (in order to avoid cycle creation).
     * </ul>
     * </p>
     * <h4>required parameters</h4>
     * <ul>
     * <li>parent - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to be parent</li>
     * <li>parent - {@link us.jts.fortress.rbac.OrgUnit#type} - contains the type of OrgUnit targeted: {@link us.jts.fortress.rbac.OrgUnit.Type#USER} or {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * <li>child - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to be child</li>
     * </ul>
     *
     * @param parent completion of op deassigns child relationship with child orgunit.
     * @param child  completion of op deassigns parent relationship with parent orgunit.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void addInheritance(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "addInheritanceOU";
        assertContext(CLS_NM, methodName, parent, GlobalErrIds.ORG_PARENT_NULL);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        assertContext(CLS_NM, methodName, child, GlobalErrIds.ORG_CHILD_NULL);
        setEntitySession(CLS_NM, methodName, parent);
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.validateRelationship(child, parent, false);
        }
        else
        {
            PsoUtil.validateRelationship(child, parent, false);
        }
        // validate that both orgs are present:
        ouP.read(parent);
        OrgUnit cOrg = ouP.read(child);
        cOrg.setParent(parent.getName());
        cOrg.setContextId(this.contextId);
        setAdminData(CLS_NM, methodName, cOrg);
        ouP.update(cOrg);

        // we're still good, now set the hierarchical relationship:
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.ADD);
        }
        else
        {
            PsoUtil.updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.ADD);
        }
    }

    /**
     * This command deletes an existing immediate inheritance relationship parent <<-- child.
     * <p/>
     * The command is valid if and only if:
     * <ul>
     * <li> The orgunits parent and child are members of the ORGUNITS data set.
     * <li> The parent is an immediate ascendant of child.
     * <li> The new inheritance relation is computed as the reflexive-transitive closure of the immediate inheritance
     * relation resulted after deleting the relationship parent <<-- child.
     * </ul>
     * <h4>required parameters</h4>
     * <ul>
     * <li>parent - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to remove as parent</li>
     * <li>parent - {@link us.jts.fortress.rbac.OrgUnit#type} - contains the type of OrgUnit targeted: {@link us.jts.fortress.rbac.OrgUnit.Type#USER} or {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * <li>child - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to remove as child</li>
     * </ul>
     *
     * @param parent completion of op removes child relationship with childRole.
     * @param child  completion of op removes parent relationship with parentRole.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void deleteInheritance(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "deleteInheritanceOU";
        assertContext(CLS_NM, methodName, parent, GlobalErrIds.ORG_PARENT_NULL);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        assertContext(CLS_NM, methodName, child, GlobalErrIds.ORG_CHILD_NULL);
        setEntitySession(CLS_NM, methodName, parent);
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.validateRelationship(child, parent, true);
        }
        else
        {
            PsoUtil.validateRelationship(child, parent, true);
        }
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.REM);
        }
        else
        {
            PsoUtil.updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.REM);
        }
        OrgUnit cOrg = ouP.read(child);
        cOrg.setContextId(this.contextId);
        cOrg.delParent(parent.getName());
        setAdminData(CLS_NM, methodName, cOrg);
        ouP.update(cOrg);
    }


    /**
     * This command creates a new role childRole, and inserts it in the role hierarchy as an immediate descendant of
     * the existing role parentRole. The command is valid if and only if childRole is not a member of the ADMINROLES data set,
     * and parentRole is a member of the ADMINROLES data set.
     * <p/>
     * This method:
     * 1 - Adds new role.
     * 2 - Assigns role relationship between new childRole and pre-existing parentRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link AdminRole#name} - contains the name of existing Role to be parent</li>
     * <li>childRole - {@link AdminRole#name} - contains the name of new Role to be child</li>
     * </ul>
     * <h4>optional parameters childRole</h4>
     * <ul>
     * <li>childRole - {@link AdminRole#description} - maps to description attribute on organizationalRole object class for new child</li>
     * <li>childRole - {@link AdminRole#beginTime} - HHMM - determines begin hour role may be activated into user's session for new child</li>
     * <li>childRole - {@link AdminRole#endTime} - HHMM - determines end hour role may be activated into user's session for new child</li>
     * <li>childRole - {@link AdminRole#beginDate} - YYYYMMDD - determines date when role may be activated into user's session for new child</li>
     * <li>childRole - {@link AdminRole#endDate} - YYYYMMDD - indicates latest date role may be activated into user's session for new child</li>
     * <li>childRole - {@link AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status for new child</li>
     * <li>childRole - {@link AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status for new child</li>
     * <li>childRole - {@link AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's session for new child</li>
     * </ul>
     *
     * @param parentRole This entity must be present in ADMINROLES data set.  Success will add role rel with childRole.
     * @param childRole  This entity must not be present in ADMINROLES data set.  Success will add the new role entity to ADMINROLES data set.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void addDescendant(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "addDescendantRole";
        assertContext(CLS_NM, methodName, parentRole, GlobalErrIds.ARLE_PARENT_NULL);
        assertContext(CLS_NM, methodName, childRole, GlobalErrIds.ARLE_CHILD_NULL);
        setEntitySession(CLS_NM, methodName, childRole);
        // ensure the parent AdminRole exists:
        admRP.read(parentRole);
        AdminRoleUtil.validateRelationship(childRole, parentRole, false);
        childRole.setParent(parentRole.getName());
        admRP.add(childRole);
        AdminRoleUtil.updateHier(this.contextId, new Relationship(childRole.getName().toUpperCase(), parentRole.getName().toUpperCase()), Hier.Op.ADD);
    }


    /**
     * This command creates a new role parentRole, and inserts it in the role hierarchy as an immediate ascendant of
     * the existing role childRole. The command is valid if and only if parentRole is not a member of the ROLES data set,
     * and childRole is a member of the ROLES data set.
     * This method:
     * 1 - Adds new role.
     * 2 - Assigns role relationship between new parentRole and pre-existing childRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>childRole - {@link AdminRole#name} - contains the name of existing Role to be child</li>
     * <li>parentRole - {@link AdminRole#name} - contains the name of new Role to be added as parent</li>
     * </ul>
     * <h4>optional parameters parentRole</h4>
     * <ul>
     * <li>parentRole - {@link AdminRole#description} - maps to description attribute on organizationalRole object class for new parent</li>
     * <li>parentRole - {@link AdminRole#beginTime} - HHMM - determines begin hour role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link AdminRole#endTime} - HHMM - determines end hour role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link AdminRole#beginDate} - YYYYMMDD - determines date when role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link AdminRole#endDate} - YYYYMMDD - indicates latest date role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status for new parent</li>
     * <li>parentRole - {@link AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status for new parent</li>
     * <li>parentRole - {@link AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's session for new parent</li>
     * </ul>
     *
     * @param parentRole completion of op assigns new child relationship with childRole.
     * @param childRole  completion of op assigns new parent relationship with parentRole.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void addAscendant(AdminRole childRole, AdminRole parentRole)
        throws SecurityException
    {
        String methodName = "addAscendantRole";
        assertContext(CLS_NM, methodName, parentRole, GlobalErrIds.ARLE_PARENT_NULL);
        setEntitySession(CLS_NM, methodName, parentRole);
        assertContext(CLS_NM, methodName, childRole, GlobalErrIds.ARLE_CHILD_NULL);
        // ensure the child AdminRole exists:
        AdminRole newChild = admRP.read(childRole);
        AdminRoleUtil.validateRelationship(childRole, parentRole, false);
        admRP.add(parentRole);
        newChild.setParent(parentRole.getName());
        newChild.setContextId(this.contextId);
        admRP.update(newChild);
        AdminRoleUtil.updateHier(this.contextId, new Relationship(childRole.getName().toUpperCase(), parentRole.getName().toUpperCase()), Hier.Op.ADD);
    }


    /**
     * This command establishes a new immediate inheritance relationship parentRole <<-- childRole between existing
     * roles parentRole, childRole. The command is valid if and only if parentRole and childRole are members of the ROLES data
     * set, parentRole is not an immediate ascendant of childRole, and childRole does not properly inherit parentRole (in order to
     * avoid cycle creation).
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link AdminRole#name} - contains the name of existing AdminRole to be parent</li>
     * <li>childRole - {@link AdminRole#name} - contains the name of existing AdminRole to be child</li>
     * </ul>
     *
     * @param parentRole completion of op deassigns child relationship with childRole.
     * @param childRole  completion of op deassigns parent relationship with parentRole.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void addInheritance(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "addInheritanceRole";
        assertContext(CLS_NM, methodName, parentRole, GlobalErrIds.ARLE_PARENT_NULL);
        assertContext(CLS_NM, methodName, childRole, GlobalErrIds.ARLE_CHILD_NULL);
        setEntitySession(CLS_NM, methodName, parentRole);
        // make sure the parent role is already there:
        AdminRole pRole = admRP.read(parentRole);
        AdminRoleUtil.validateRelationship(childRole, parentRole, false);
        // make sure the child role is already there:
        AdminRole cRole = new AdminRole(childRole.getName());
        cRole.setContextId(this.contextId);
        cRole = admRP.read(cRole);
        AdminRoleUtil.updateHier(this.contextId, new Relationship(childRole.getName().toUpperCase(), parentRole.getName().toUpperCase()), Hier.Op.ADD);
        cRole.setParent(parentRole.getName());
        cRole.setContextId(this.contextId);
        setAdminData(CLS_NM, methodName, cRole);
        admRP.update(cRole);
    }


    /**
     * This command deletes an existing immediate inheritance relationship parentRole <<-- childRole. The command is
     * valid if and only if the roles parentRole and childRole are members of the ROLES data set, and parentRole is an
     * immediate ascendant of childRole. The new inheritance relation is computed as the reflexive-transitive
     * closure of the immediate inheritance relation resulted after deleting the relationship parentRole <<-- childRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link AdminRole#name} - contains the name of existing AdminRole to remove as parent</li>
     * <li>childRole - {@link AdminRole#name} - contains the name of existing AdminRole to remove as child</li>
     * </ul>
     *
     * @param parentRole completion of op removes child relationship with childRole.
     * @param childRole  completion of op removes parent relationship with parentRole.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void deleteInheritance(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "deleteInheritanceRole";
        assertContext(CLS_NM, methodName, parentRole, GlobalErrIds.ARLE_PARENT_NULL);
        assertContext(CLS_NM, methodName, childRole, GlobalErrIds.ARLE_CHILD_NULL);
        setEntitySession(CLS_NM, methodName, parentRole);
        AdminRoleUtil.validateRelationship(childRole, parentRole, true);
        AdminRoleUtil.updateHier(this.contextId, new Relationship(childRole.getName().toUpperCase(), parentRole.getName().toUpperCase()), Hier.Op.REM);
        // need to remove the parent from the child role:
        AdminRole cRole = new AdminRole(childRole.getName());
        cRole.setContextId(this.contextId);
        cRole = admRP.read(cRole);
        cRole.setContextId(this.contextId);
        cRole.delParent(parentRole.getName());
        setAdminData(CLS_NM, methodName, cRole);
        admRP.update(cRole);
    }

    /**
     * This method will add an administrative permission operation to an existing permission object which resides under {@code ou=AdminPerms,ou=ARBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may have {@link AdminRole} or {@link us.jts.fortress.rbac.User} associations.  The target {@link Permission} must not exist prior to calling.
     * A Fortress Permission instance exists in a hierarchical, one-many relationship between its parent and itself as stored in ldap tree: ({@link PermObj}*->{@link Permission}).
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the name of existing object being targeted for the permission add</li>
     * <li>{@link Permission#opName} - contains the name of new permission operation being added</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link Permission#roles} * - multi occurring attribute contains RBAC Roles that permission operation is being granted to</li>
     * <li>{@link Permission#users} * - multi occurring attribute contains Users that permission operation is being granted to</li>
     * <li>{@link Permission#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * <li>{@link Permission#type} - any safe text</li>
     * </ul>
     *
     * @param perm must contain the object, {@link us.jts.fortress.rbac.Permission#objectName}, and operation, {@link Permission#opName}, that identifies target along with optional other attributes..
     * @return copy of Permission entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    @Override
    public Permission addPermission(Permission perm)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        perm.setAdmin(true);
        return adminMgr.addPermission(perm);
    }

    /**
     * This method will update administrative permission operation pre-existing in target directory under {@code ou=AdminPerms,ou=ARBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may also contain {@link AdminRole} or {@link us.jts.fortress.rbac.User} associations to add or remove using this function.
     * The perm operation must exist before making this call.  Only non-null attributes will be updated.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the name of existing object being targeted for the permission update</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation being updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link Permission#roles} * - multi occurring attribute contains RBAC Roles that permission operation is being granted to</li>
     * <li>{@link Permission#users} * - multi occurring attribute contains Users that permission operation is being granted to</li>
     * <li>{@link Permission#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * <li>{@link Permission#type} - any safe text</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target and any optional data to update.  Null or empty attributes will be ignored.
     * @return copy of Permission entity.
     * @throws us.jts.fortress.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    @Override
    public Permission updatePermission(Permission perm)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        perm.setAdmin(true);
        return adminMgr.updatePermission(perm);
    }

    /**
     * This method will remove administrative permission operation entity from permission object. A Fortress permission is (object->operation).
     * The perm operation must exist before making this call.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the name of existing object being targeted for the permission delete</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation being removed</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @throws us.jts.fortress.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    @Override
    public void deletePermission(Permission perm)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        perm.setAdmin(true);
        adminMgr.deletePermission(perm);
    }

    /**
     * This method will add administrative permission object to admin perms container in directory. The perm object must not exist before making this call.
     * A {@link PermObj} instance exists in a hierarchical, one-many relationship between itself and children as stored in ldap tree: ({@link PermObj}*->{@link Permission}).
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link PermObj#objectName} - contains the name of new object being added</li>
     * <li>{@link PermObj#ou} - contains the name of an existing PERMS OrgUnit this object is associated with</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link PermObj#description} - any safe text</li>
     * <li>{@link PermObj#type} - contains any safe text</li>
     * <li>{@link PermObj#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * </ul>
     *
     * @param pObj must contain the {@link PermObj#objectName} and {@link PermObj#ou}.  The other attributes are optional.
     * @return copy of PermObj entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    @Override
    public PermObj addPermObj(PermObj pObj)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        pObj.setAdmin(true);
        return adminMgr.addPermObj(pObj);
    }

    /**
     * This method will update administrative permission object in perms container in directory.  The perm object must exist before making this call.
     * A {@link PermObj} instance exists in a hierarchical, one-many relationship between itself and children as stored in ldap tree: ({@link PermObj}*->{@link Permission}).
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link PermObj#objectName} - contains the name of existing object being updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link PermObj#ou} - contains the name of an existing PERMS OrgUnit this object is associated with</li>
     * <li>{@link PermObj#description} - any safe text</li>
     * <li>{@link PermObj#type} - contains any safe text</li>
     * <li>{@link PermObj#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * </ul>
     *
     * @param pObj must contain the {@link PermObj#objectName}. Only non-null attributes will be updated.
     * @return copy of newly updated PermObj entity.
     * @throws us.jts.fortress.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    @Override
    public PermObj updatePermObj(PermObj pObj)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        pObj.setAdmin(true);
        return adminMgr.updatePermObj(pObj);
    }

    /**
     * This method will remove administrative permission object from perms container in directory.  This method will also remove
     * in associated permission objects that are attached to this object.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link PermObj#objectName} - contains the name of existing object targeted for removal</li>
     * </ul>
     *
     * @param pObj must contain the {@link PermObj#objectName} of object targeted for removal.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    @Override
    public void deletePermObj(PermObj pObj)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        pObj.setAdmin(true);
        adminMgr.deletePermObj(pObj);
    }

    /**
     * This command grants an AdminRole the administrative permission to perform an operation on an object to a role.
     * The command is implemented by granting administrative permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * and the adminRole is a member of the ADMIN_ROLES data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the object name</li>
     * <li>{@link Permission#opName} - contains the operation name</li>
     * <li>{@link AdminRole#name} - contains the adminRole name</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param role must contains {@link AdminRole#name}.
     * @throws us.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    @Override
    public void grantPermission(Permission perm, AdminRole role)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        perm.setAdmin(true);
        adminMgr.grantPermission(perm, role);
    }

    /**
     * This command revokes the administrative permission to perform an operation on an object from the set
     * of permissions assigned to an AdminRole. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * the role is a member of the ADMIN_ROLES data set, and the permission is assigned to that AdminRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the object name</li>
     * <li>{@link Permission#opName} - contains the operation name</li>
     * <li>{@link AdminRole#name} - contains the adminRole name</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param role must contains {@link AdminRole#name}.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    @Override
    public void revokePermission(Permission perm, AdminRole role)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        perm.setAdmin(true);
        adminMgr.revokePermission(perm, role);
    }

    /**
     * This command grants a user the administrative permission to perform an operation on an object to a user.
     * The command is implemented by granting administrative permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents an administrative permission,
     * and the user is a member of the USERS data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the object name</li>
     * <li>{@link Permission#opName} - contains the operation name</li>
     * <li>{@link User#userId} - contains the userId</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param user must contain {@link User#userId} of target User entity.
     * @throws us.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    @Override
    public void grantPermission(Permission perm, User user)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        perm.setAdmin(true);
        adminMgr.grantPermission(perm, user);
    }

    /**
     * This command revokes the administrative permission to perform an operation on an object from the set
     * of permissions assigned to a user. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents an administrative permission,
     * the user is a member of the USERS data set, and the permission is assigned to that user.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the object name</li>
     * <li>{@link Permission#opName} - contains the operation name</li>
     * <li>{@link User#userId} - contains the userId</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param user must contain {@link User#userId} of target User entity.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    @Override
    public void revokePermission(Permission perm, User user)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        perm.setAdmin(true);
        adminMgr.revokePermission(perm, user);
    }
}

