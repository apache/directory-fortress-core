/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.arbac;

import com.jts.fortress.*;
import com.jts.fortress.SecurityException;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.hier.Hier;
import com.jts.fortress.rbac.PermP;
import com.jts.fortress.rbac.Permission;
import com.jts.fortress.rbac.PermObj;
import com.jts.fortress.rbac.User;
import com.jts.fortress.rbac.UserP;
import com.jts.fortress.rbac.Session;
import com.jts.fortress.util.time.CUtil;
import com.jts.fortress.util.attr.AttrHelper;
import com.jts.fortress.util.attr.VUtil;

import java.util.List;

/**
 * This object implements the ARBAC02 DelegatedAdminMgr interface for performing policy administration of Fortress ARBAC entities
 * that reside in LDAP directory.
 * These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification describes delegated administrative
 * operations for the creation and maintenance of ARBAC element sets and relations.  Delegated administrative review functions for performing administrative queries
 * and system functions for creating and managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 * This object is NOT thread safe.
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="../../../../images/ARbac.png">
 * <p/>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without sacrificing regulations for accountability or traceability.
 * <p/>

 * @author smckinn
 * @created September 18, 2010
 */
public final class DelegatedAdminMgrImpl
    implements DelegatedAdminMgr
{
    private static final String OCLS_NM = DelegatedAdminMgrImpl.class.getName();
    final private static OrgUnitP ouP = new OrgUnitP();
    final private static AdminRoleP admRP = new AdminRoleP();
    final private static UserP userP = new UserP();
    final private static PermP permP = new PermP();

    // thread unsafe variable:
    private Session adminSess;

    /**
     * Setting Session into this object will enforce Administrative controls.
     * @param session
    */
    public void setAdmin(Session session)
    {
        this.adminSess = session;
    }

    private void setEntitySession(String opName, FortEntity entity) throws SecurityException
    {
        if (this.adminSess != null)
        {
            AdminUtil.setEntitySession(adminSess, new Permission(OCLS_NM, opName), entity);
        }
    }

    /**
     * This command creates a new admin role. The command is valid if and only if the new admin role is not
     * already a member of the ADMIN ROLES data set. The ADMIN ROLES data set is updated.
     * Initially, no user or permission is assigned to the new role.
     *
     * @param role Contains role name and description.
     * @throws com.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    public AdminRole addRole(AdminRole role)
        throws SecurityException
    {
        String methodName = "addRole";
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, OCLS_NM + "." + methodName);
        setEntitySession(methodName, role);
        AdminRole newRole = admRP.add(role);
        return newRole;
    }

    /**
     * This command deletes an existing admin role from the ARBAC database. The command is valid
     * if and only if the admin role to be deleted is a member of the ADMIN ROLES data set and has been
     * deassigned from all users.
     *
     * @param role Contains role name.
     * @throws com.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    public void deleteRole(AdminRole role)
        throws SecurityException
    {
        String methodName = "deleteRole";
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, OCLS_NM + "." + methodName);
        setEntitySession(methodName, role);
        int numChildren = AdminRoleUtil.numChildren(role.getName());
        if (numChildren > 0)
        {
            String error = OCLS_NM + "." + methodName + " role [" + role.getName() + "] must remove [" + numChildren + "] descendants before deletion";
            throw new SecurityException(GlobalErrIds.HIER_DEL_FAILED_HAS_CHILD, error, null);
        }
        // search for all users assigned this role and deassign:
        // todo: change the max value to param set in config:
        List<User> users = userP.getAssignedUsers(role);
        if (users != null)
        {
            for(User ue : users)
            {
                User user = new User(ue.getUserId());
                UserAdminRole uAdminRole = new UserAdminRole(ue.getUserId(), role.getName());
                AdminUtil.setAdminData(role.getAdminSession(), new Permission(OCLS_NM, methodName), user);
                deassignUser(uAdminRole);
            }
        }
        permP.remove(role);
        admRP.delete(role);
    }

    /**

     * Method will update a admin Role entity in the directory.  The role must exist in admin role container prior to this call.
     *
     * @param role Contains role name and new description.
     * @throws com.jts.fortress.SecurityException
     *          Description of the Exception
     */
    public AdminRole updateRole(AdminRole role)
        throws SecurityException
    {
        String methodName = "updateRole";
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, OCLS_NM + "." + methodName);
        setEntitySession(methodName, role);
        AdminRole re = admRP.update(role);
        List<User> users = userP.getAssignedUsers(re);
        for(User ue : users)
        {
            User upUe = new User(ue.getUserId());
            AdminUtil.setAdminData(role.getAdminSession(), new Permission(OCLS_NM, methodName), upUe);
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
            userP.update(upUe);
        }
        return re;
    }


    /**
     *
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
     * @param uAdminRole entity contains {@link User#userId} and {@link AdminRole#name} and optional {@code Constraints}.
     * @return AdminRole contains copy of input entity and additional data processed by request.
     * @throws SecurityException in the event data error in user or role objects or system error.
     */
    public void assignUser(UserAdminRole uAdminRole)
        throws SecurityException
    {
        String methodName = "assignUser";
        VUtil.assertNotNull(uAdminRole, GlobalErrIds.ARLE_NULL, OCLS_NM + "." + methodName);
        setEntitySession(methodName, uAdminRole);

        // retrieve the admin role info:
        AdminRole validRole = admRP.read(uAdminRole.getName());
        // todo:  use: CUtil.validateOrCopy(validRole, uRole);
        // if the UserAdminRole entity doesn't have temporal constraints set already, copy from the AdminRole declaration:
        // if the input role entity attribute doesn't have temporal constraints set, copy from the role declaration:
        CUtil.validateOrCopy(validRole, uAdminRole);

        // copy the ARBAC AdminRole attributes to UserAdminRole:
        AttrHelper.copyAdminAttrs(validRole, uAdminRole);
        String dn = userP.assign(uAdminRole);
        // copy the admin session info to AdminRole:
        AdminUtil.setAdminData(uAdminRole.getAdminSession(), new Permission(OCLS_NM, methodName), validRole);
        // Assign user dn attribute to the adminRole, this will add a single, standard attribute value, called "roleOccupant", directly onto the adminRole node:
        admRP.assign(validRole, dn);
    }


    /**
     * This method removes assigned admin role from user entity.  Both user and admin role entities must exist and have role relationship
     * before calling this method.
     * Successful completion:
     *      del Role to User assignment in User data set
     * AND
     *      User to Role assignment in Admin Role data set.
     *
     * @param uAdminRole entity contains {@link User#userId} and {@link AdminRole#name}.
     * @return AdminRole contains copy of input entity and additional data processed by request.
     * @throws SecurityException - in the event data error in user or role objects or system error.
     */
    public void deassignUser(UserAdminRole uAdminRole)
        throws SecurityException
    {
        String methodName = "deassignUser";
        VUtil.assertNotNull(uAdminRole, GlobalErrIds.ARLE_NULL, OCLS_NM + "." + methodName);
        setEntitySession(methodName, uAdminRole);
        String dn = userP.deassign(uAdminRole);
        AdminRole adminRole = new AdminRole(uAdminRole.getName());
        // copy the ARBAC attributes to AdminRole:
        AdminUtil.setAdminData(uAdminRole.getAdminSession(), new Permission(OCLS_NM, methodName), adminRole);
        // Deassign user dn attribute to the adminRole, this will remove a single, standard attribute value, called "roleOccupant", directly onto the adminRole node:
        admRP.deassign(adminRole, dn);

    }


    /**
     * Commands adds a new OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * @param entity contains OrgUnit name and type.
     * @return
     * @throws com.jts.fortress.SecurityException in the event of data validation or system error.
     */
    public OrgUnit add(OrgUnit entity)
        throws SecurityException
    {
        String methodName = "addOU";
        VUtil.assertNotNull(entity, GlobalErrIds.ORG_NULL, OCLS_NM + "." + methodName);
        setEntitySession(methodName, entity);
        VUtil.assertNotNull(entity.getType(), GlobalErrIds.ORG_TYPE_NULL, OCLS_NM + "." + methodName);
        return ouP.add(entity);

    }

    /**
     * Commands updates existing OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * @param entity contains OrgUnit name and type.
     * @return
     * @throws com.jts.fortress.SecurityException in the event of data validation or system error.
     */
    public OrgUnit update(OrgUnit entity)
        throws SecurityException
    {
        String methodName = "updateOU";
        VUtil.assertNotNull(entity, GlobalErrIds.ORG_NULL, OCLS_NM + "." + methodName);
        setEntitySession(methodName, entity);
        VUtil.assertNotNull(entity.getType(), GlobalErrIds.ORG_TYPE_NULL, OCLS_NM + "." + methodName);
        return ouP.update(entity);

    }

    /**
     * Commands deletes existing OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * @param entity contains OrgUnit name and type.
     * @return
     * @throws SecurityException in the event of data validation or system error.
     */
    public OrgUnit delete(OrgUnit entity)
        throws SecurityException
    {
        String methodName = "deleteOU";
        VUtil.assertNotNull(entity, GlobalErrIds.ORG_NULL, OCLS_NM + "." + methodName);
        setEntitySession(methodName, entity);
        VUtil.assertNotNull(entity.getType(), GlobalErrIds.ORG_TYPE_NULL, OCLS_NM + "." + methodName);
        int numChildren;
        if(entity.getType() == OrgUnit.Type.USER)
        {
            numChildren = UsoUtil.numChildren(entity.getName());
        }
        else
        {
            numChildren = PsoUtil.numChildren(entity.getName());
        }
        if (numChildren > 0)
        {
            String error = OCLS_NM + "." + methodName + " orgunit [" + entity.getName() + "] must remove [" + numChildren + "] descendants before deletion";
            throw new SecurityException(GlobalErrIds.HIER_DEL_FAILED_HAS_CHILD, error, null);
        }
        if(entity.getType() == OrgUnit.Type.USER)
        {
            // Ensure the org unit is not assigned to any users, but set the sizeLimit to "true" to limit result set size.
            List<User> assignedUsers = userP.search(entity, true);
            if(VUtil.isNotNullOrEmpty(assignedUsers))
            {
                String error = OCLS_NM + "." + methodName + " orgunit [" + entity.getName() + "] must unassign [" + assignedUsers.size() + "] users before deletion";
                throw new SecurityException(GlobalErrIds.ORG_DEL_FAILED_USER, error, null);
            }
        }
        else
        {
            // Ensure the org unit is not assigned to any permission objects but set the sizeLimit to "true" to limit result set size..
            List<PermObj> assignedPerms = permP.search(entity, true);
            if(VUtil.isNotNullOrEmpty(assignedPerms))
            {
                String error = OCLS_NM + "." + methodName + " orgunit [" + entity.getName() + "] must unassign [" + assignedPerms.size() + "] perm objs before deletion";
                throw new SecurityException(GlobalErrIds.ORG_DEL_FAILED_PERM, error, null);
            }
        }
        // everything checked out good - remove the org unit from the OrgUnit data set:
        return ouP.delete(entity);
    }


    /**
     * This commands creates a new orgunit child, and inserts it in the orgunit hierarchy as an immediate descendant of
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
     * </p>
     * @param parent This entity must be present in ORGUNIT data set.  Success will add rel with child.
     * @param child  This entity must not be present in ORGUNIT data set.  Success will add the new entity to ORGUNIT data set.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addDescendant(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "addDescendantOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, OCLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, OCLS_NM + "." + methodName);
        VUtil.assertNotNull(child, GlobalErrIds.ORG_CHILD_NULL, OCLS_NM + "." + methodName);
        setEntitySession(methodName, child);
        // ensure the parent OrgUnit exists:
        ouP.read(parent);
        if(parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.validateRelationship(child, parent, false);
        }
        else
        {
            PsoUtil.validateRelationship(child, parent, false);
        }
        ouP.add(child);
        Hier hier = new Hier(child.getName(), parent.getName());
        AdminUtil.setAdminData(child.getAdminSession(), new Permission(OCLS_NM, methodName), hier);
        if(parent.getType() == OrgUnit.Type.USER)
        {
            hier.setType(Hier.Type.USER);
            UsoUtil.updateHier(hier, Hier.Op.ADD);
        }
        else
        {
            hier.setType(Hier.Type.PERM);
            PsoUtil.updateHier(hier, Hier.Op.ADD);
        }
    }


    /**
     * This commands creates a new orgunit parent, and inserts it in the orgunit hierarchy as an immediate ascendant of
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
     * </p>
     * @param parent completion of op assigns new child relationship with child orgunit.
     * @param child  completion of op assigns new parent relationship with parent orgunit.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addAscendant(OrgUnit child, OrgUnit parent)
        throws SecurityException
    {
        String methodName = "addAscendantOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, OCLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, OCLS_NM + "." + methodName);
        setEntitySession(methodName, parent);
        VUtil.assertNotNull(child, GlobalErrIds.ORG_CHILD_NULL, OCLS_NM + "." + methodName);
        // ensure the child OrgUnit exists:
        ouP.read(child);
        if(parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.validateRelationship(child, parent, false);
        }
        else
        {
            PsoUtil.validateRelationship(child, parent, false);
        }
        ouP.add(parent);
        Hier hier = new Hier(child.getName(), parent.getName());
        AdminUtil.setAdminData(parent.getAdminSession(), new Permission(OCLS_NM, methodName), hier);
        if(parent.getType() == OrgUnit.Type.USER)
        {
            hier.setType(Hier.Type.USER);
            UsoUtil.updateHier(hier, Hier.Op.ADD);
        }
        else
        {
            hier.setType(Hier.Type.PERM);
            PsoUtil.updateHier(hier, Hier.Op.ADD);
        }
    }

    /**
     * This commands establishes a new immediate inheritance relationship with parent orgunit <<-- child orgunit
     * <p>
     * The command is valid if and only if:
     * <ul>
     * <li> The parent and child are members of the ORGUNITS data set.
     * <li> The parent is not an immediate ascendant of child.
     * <li> The child does not properly inherit parent (in order to avoid cycle creation).
     * </ul>
     * </p>
     * @param parent completion of op deassigns child relationship with child orgunit.
     * @param child  completion of op deassigns parent relationship with parent orgunit.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addInheritance(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "addInheritanceOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, OCLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, OCLS_NM + "." + methodName);
        VUtil.assertNotNull(child, GlobalErrIds.ORG_CHILD_NULL, OCLS_NM + "." + methodName);
        setEntitySession(methodName, parent);
        if(parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.validateRelationship(child, parent, false);
        }
        else
        {
            PsoUtil.validateRelationship(child, parent, false);
        }
        // validate that both orgs are present:
        ouP.read(child);
        ouP.read(parent);

        // we're still good, now set the hierarchical relationship:
        Hier hier = new Hier(child.getName(), parent.getName());
        AdminUtil.setAdminData(parent.getAdminSession(), new Permission(OCLS_NM, methodName), hier);
        if(parent.getType() == OrgUnit.Type.USER)
        {
            hier.setType(Hier.Type.USER);
            UsoUtil.updateHier(hier, Hier.Op.ADD);
        }
        else
        {
            hier.setType(Hier.Type.PERM);
            PsoUtil.updateHier(hier, Hier.Op.ADD);
        }
    }

    /**
     * This command deletes an existing immediate inheritance relationship parent <<-- child.
     * <p>
     * The command is valid if and only if:
     * <ul>
     * <li> The orgunits parent and child are members of the ORGUNITS data set.
     * <li> The parent is an immediate ascendant of child.
     * <li> The new inheritance relation is computed as the reflexive-transitive closure of the immediate inheritance
     * relation resulted after deleting the relationship parent <<-- child.
     * </ul>
     * </p>
     *
     * @param parent completion of op removes child relationship with childRole.
     * @param child  completion of op removes parent relationship with parentRole.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void deleteInheritance(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "deleteInheritanceOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, OCLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, OCLS_NM + "." + methodName);
        VUtil.assertNotNull(child, GlobalErrIds.ORG_CHILD_NULL, OCLS_NM + "." + methodName);
        setEntitySession(methodName, parent);
        if(parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.validateRelationship(child, parent, true);
        }
        else
        {
            PsoUtil.validateRelationship(child, parent, true);
        }
        Hier hier = new Hier(child.getName(), parent.getName());
        AdminUtil.setAdminData(parent.getAdminSession(), new Permission(OCLS_NM, methodName), hier);
        if(parent.getType() == OrgUnit.Type.USER)
        {
            hier.setType(Hier.Type.USER);
            UsoUtil.updateHier(hier, Hier.Op.REM);
        }
        else
        {
            hier.setType(Hier.Type.PERM);
            PsoUtil.updateHier(hier, Hier.Op.REM);
        }
    }


    /**
     * This commands creates a new role childRole, and inserts it in the role hierarchy as an immediate descendant of
     * the existing role parentRole. The command is valid if and only if childRole is not a member of the ROLES data set,
     * and parentRole is a member of the ROLES data set.
     * @param parentRole This entity must be present in ROLE data set.  Success will add role rel with childRole.
     * @param childRole  This entity must not be present in ROLE data set.  Success will add the new role entity to ROLE data set.
     * This method:
     * 1 - Adds new role.
     * 2 - Assigns role relationship between new childRole and pre-existing parentRole.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addDescendant(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "addDescendantRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, OCLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, OCLS_NM + "." + methodName);
        setEntitySession(methodName, childRole);
        // ensure the parent AdminRole exists:
        admRP.read(parentRole.getName());
        AdminRoleUtil.validateRelationship(childRole, parentRole, false);
        admRP.add(childRole);
        Hier hier = new Hier(Hier.Type.AROLE, childRole.getName(), parentRole.getName());
        AdminUtil.setAdminData(childRole.getAdminSession(), new Permission(OCLS_NM, methodName), hier);
        AdminRoleUtil.updateHier(hier, Hier.Op.ADD);
    }

    /**
     * This commands creates a new role parentRole, and inserts it in the role hierarchy as an immediate ascendant of
     * the existing role childRole. The command is valid if and only if parentRole is not a member of the ROLES data set,
     * and childRole is a member of the ROLES data set.
     * This method:
     * 1 - Adds new role.
     * 2 - Assigns role relationship between new parentRole and pre-existing childRole.
     * @param parentRole completion of op assigns new child relationship with childRole.
     * @param childRole  completion of op assigns new parent relationship with parentRole.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addAscendant(AdminRole childRole, AdminRole parentRole)
        throws SecurityException
    {
        String methodName = "addAscendantRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, OCLS_NM + "." + methodName);
        setEntitySession(methodName, parentRole);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, OCLS_NM + "." + methodName);
        // ensure the child AdminRole exists:
        admRP.read(childRole.getName());
        AdminRoleUtil.validateRelationship(childRole, parentRole, false);
        admRP.add(parentRole);
        Hier hier = new Hier(Hier.Type.AROLE, childRole.getName(), parentRole.getName());
        AdminUtil.setAdminData(parentRole.getAdminSession(), new Permission(OCLS_NM, methodName), hier);
        AdminRoleUtil.updateHier(hier, Hier.Op.ADD);
    }

    /**
     * This commands establishes a new immediate inheritance relationship parentRole <<-- childRole between existing
     * roles parentRole, childRole. The command is valid if and only if parentRole and childRole are members of the ROLES data
     * set, parentRole is not an immediate ascendant of childRole, and childRole does not properly inherit parentRole (in order to
     * avoid cycle creation).
     * @param parentRole completion of op deassigns child relationship with childRole.
     * @param childRole  completion of op deassigns parent relationship with parentRole.
     * @throws com.jts.fortress.SecurityException thrown in the event of data validation or system error.
     */
    public void addInheritance(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "addInheritanceRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, OCLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, OCLS_NM + "." + methodName);
        // todo: set the hier entity not the parentRole:
        setEntitySession(methodName, parentRole);
        AdminRoleUtil.validateRelationship(childRole, parentRole, false);
        Hier hier = new Hier(Hier.Type.AROLE, childRole.getName(), parentRole.getName());
        AdminUtil.setAdminData(parentRole.getAdminSession(), new Permission(OCLS_NM, methodName), hier);
        AdminRoleUtil.updateHier(hier, Hier.Op.ADD);
    }

    /**
     * This command deletes an existing immediate inheritance relationship parentRole <<-- childRole. The command is
     * valid if and only if the roles parentRole and childRole are members of the ROLES data set, and parentRole is an
     * immediate ascendant of childRole. The new inheritance relation is computed as the reflexive-transitive
     * closure of the immediate inheritance relation resulted after deleting the relationship parentRole <<-- childRole.
     * @param parentRole completion of op removes child relationship with childRole.
     * @param childRole  completion of op removes parent relationship with parentRole.
     * @throws com.jts.fortress.SecurityException thrown in the event of data validation or system error.
     */
    public void deleteInheritance(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "deleteInheritanceRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, OCLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, OCLS_NM + "." + methodName);
        // todo: set the hier entity not the parentRole:
        setEntitySession(methodName, parentRole);
        AdminRoleUtil.validateRelationship(childRole, parentRole, true);
        Hier hier = new Hier(Hier.Type.AROLE, childRole.getName(), parentRole.getName());
        AdminUtil.setAdminData(parentRole.getAdminSession(), new Permission(OCLS_NM, methodName), hier);
        AdminRoleUtil.updateHier(hier, Hier.Op.REM);
    }

    /**
     * This method will add an administrative permission operation to an existing permission object which resides under {@code ou=AdminPerms,ou=ARBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may have {@link com.jts.fortress.arbac.AdminRole} or {@link com.jts.fortress.rbac.User} associations.  The target {@link Permission} must not exist prior to calling.
     * A Fortress Permission instance exists in a hierarchical, one-many relationship between its parent and itself as stored in ldap tree: ({@link PermObj}*->{@link Permission}).
     *
     * @param perm must contain the object, {@link com.jts.fortress.rbac.Permission#objectName}, and operation, {@link Permission#opName}, that identifies target along with optional other attributes..
     * @return copy of Permission entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    public Permission addPermission(Permission perm)
        throws SecurityException
    {
        AdminMgr adminMgr = AdminMgrFactory.createInstance();
        perm.setAdmin(true);
        return adminMgr.addPermission(perm);
    }

    /**
     * This method will update administrative permission operation pre-existing in target directory under {@code ou=AdminPerms,ou=ARBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may also contain {@link com.jts.fortress.arbac.AdminRole} or {@link com.jts.fortress.rbac.User} associations to add or remove using this function.
     * The perm operation must exist before making this call.  Only non-null attributes will be updated.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target and any optional data to update.  Null or empty attributes will be ignored.
     * @return copy of permOp entity.
     * @throws com.jts.fortress.SecurityException - thrown in the event of perm object data or system error.
     */
    public Permission updatePermission(Permission perm)
        throws SecurityException
    {
        AdminMgr adminMgr = AdminMgrFactory.createInstance();
        perm.setAdmin(true);
        return adminMgr.updatePermission(perm);
    }

    /**
     * This method will remove administrative permission operation entity from permission object. A Fortress permission is (object->operation).
     * The perm operation must exist before making this call.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @throws com.jts.fortress.SecurityException - thrown in the event of perm object data or system error.
     */
    public void deletePermission(Permission perm)
        throws SecurityException
    {
        AdminMgr adminMgr = AdminMgrFactory.createInstance();
        perm.setAdmin(true);
        adminMgr.deletePermission(perm);
    }

    /**
     * This method will add administrative permission object to admin perms container in directory. The perm object must not exist before making this call.
     * A {@link PermObj} instance exists in a hierarchical, one-many relationship between itself and children as stored in ldap tree: ({@link PermObj}*->{@link Permission}).
     *
     * @param pObj must contain the {@link PermObj#objectName} and {@link PermObj#ou}.  The other attributes are optional.
     * @return copy of permObj entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    public PermObj addPermObj(PermObj pObj)
        throws SecurityException
    {
        AdminMgr adminMgr = AdminMgrFactory.createInstance();
        pObj.setAdmin(true);
        return adminMgr.addPermObj(pObj);
    }

    /**
     * This method will update administrative permission object in perms container in directory.  The perm object must exist before making this call.
     * A {@link PermObj} instance exists in a hierarchical, one-many relationship between itself and children as stored in ldap tree: ({@link PermObj}*->{@link Permission}).
     *
     * @param pObj must contain the {@link PermObj#objectName}. Only non-null attributes will be updated.
     * @return copy of newly updated permObj entity.
     * @throws com.jts.fortress.SecurityException - thrown in the event of perm object data or system error.
     */
    public PermObj updatePermObj(PermObj pObj)
        throws SecurityException
    {
        AdminMgr adminMgr = AdminMgrFactory.createInstance();
        pObj.setAdmin(true);
        return adminMgr.updatePermObj(pObj);
    }

    /**
     * This method will remove administrative permission object from perms container in directory.  This method will also remove
     * in associated permission objects that are attached to this object.
     *
     * @param pObj must contain the {@link PermObj#objectName} of object targeted for removal.
     * @return copy of permObj entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    public void deletePermObj(PermObj pObj)
        throws SecurityException
    {
        AdminMgr adminMgr = AdminMgrFactory.createInstance();
        pObj.setAdmin(true);
        adminMgr.deletePermObj(pObj);
    }

    /**
     * This command grants an AdminRole the administrative permission to perform an operation on an object to a role.
     * The command is implemented by granting administrative permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * and the adminRole is a member of the ADMIN_ROLES data set.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param role must contains {@link AdminRole#name}.
     * @throws com.jts.fortress.SecurityException Thrown in the event of data validation or system error.
     */
    public void grantPermission(Permission perm, AdminRole role)
        throws SecurityException
    {
        AdminMgr adminMgr = AdminMgrFactory.createInstance();
        perm.setAdmin(true);
        adminMgr.grantPermission(perm, role);
    }

    /**
     * This command revokes the administrative permission to perform an operation on an object from the set
     * of permissions assigned to an AdminRole. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * the role is a member of the ADMIN_ROLES data set, and the permission is assigned to that AdminRole.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param role must contains {@link AdminRole#name}.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    public void revokePermission(Permission perm, AdminRole role)
        throws SecurityException
    {
        AdminMgr adminMgr = AdminMgrFactory.createInstance();
        perm.setAdmin(true);
        adminMgr.revokePermission(perm, role);
    }

    /**
     * Method grants an administrative permission directly to a User entity.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param user must contain {@link User#userId} of target User entity.
     * @throws com.jts.fortress.SecurityException Thrown in the event of data validation or system error.
     */
    public void grantPermission(Permission perm, User user)
        throws SecurityException
    {
        AdminMgr adminMgr = AdminMgrFactory.createInstance();
        perm.setAdmin(true);
        adminMgr.grantPermission(perm, user);
    }

    /**
     * Method revokes an administrative permission directly from a User entity.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param user must contain {@link User#userId} of target User entity.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    public void revokePermission(Permission perm, User user)
        throws SecurityException
    {
        AdminMgr adminMgr = AdminMgrFactory.createInstance();
        perm.setAdmin(true);
        adminMgr.revokePermission(perm, user);
    }

}

