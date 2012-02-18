/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */
package com.jts.fortress.rest;

import com.jts.fortress.*;
import com.jts.fortress.SecurityException;
import com.jts.fortress.arbac.AdminUtil;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.rbac.PermGrant;
import com.jts.fortress.rbac.PermObj;
import com.jts.fortress.rbac.Permission;
import com.jts.fortress.rbac.Role;
import com.jts.fortress.rbac.RoleRelationship;
import com.jts.fortress.rbac.SDSet;
import com.jts.fortress.rbac.Session;
import com.jts.fortress.rbac.User;
import com.jts.fortress.rbac.UserRole;
import com.jts.fortress.util.attr.VUtil;



/**
 * This object performs administrative functions to provision Fortress RBAC entities using HTTP access to En Masse REST server.  These APIs
 * map directly to similar named APIs specified by ANSI and NIST RBAC models.
 * Many of the java doc function descriptions found below were taken directly from ANSI INCITS 359-2004.
 * The RBAC Functional specification describes administrative operations for the creation
 * and maintenance of RBAC element sets and relations; administrative review functions for
 * performing administrative queries; and system functions for creating and managing
 * RBAC attributes on user sessions and making access control decisions.
 * <p/>
 * <h4>RBAC0 - Core</h4>
 * Many-to-many relationship between Users, Roles and Permissions. Selective role activation into sessions.  API to add, update, delete identity data and perform identity and access control decisions during runtime operations.
 * <p/>
 * <img src="../../../../images/RbacCore.png">
 * <h4>RBAC1 - General Hierarchical Roles</h4>
 * Simplifies role engineering tasks using inheritance of one or more parent roles.
 * <p/>
 * <img src="../../../../images/RbacHier.png">
 * <h4>RBAC2 - Static Separation of Duty (SSD) Relations</h4>
 * Enforce mutual membership exclusions across role assignments.  Facilitate dual control policies by restricting which roles may be assigned to users in combination.  SSD provide added granularity for authorization limits which help enterprises meet strict compliance regulations.
 * <p/>
 * <img src="../../../../images/RbacSSD.png">
 * <h4>RBAC3 - Dynamic Separation of Duty (DSD) Relations</h4>
 * Control allowed role combinations to be activated within an RBAC session.  DSD policies fine tune role policies that facilitate authorization dual control and two man policy restrictions during runtime security checks.
 * <p/>
 * <img src="../../../../images/RbacDSD.png">
 * <p/>
 * This object is NOT thread safe as it contains instance variables.
 * <p/>

 *
 * @author smckinn
 * @created February 12, 2012
 */
public final class AdminMgrRestImpl implements AdminMgr
{
    private static final String OCLS_NM = AdminMgrRestImpl.class.getName();

    // thread unsafe variable:
    private Session adminSess;

    /**
     * Setting Session into this object will enforce ARBAC controls and render this class
     * thread unsafe..
     *
     * @param session contains Session to add as instance variable.
     */
    public void setAdmin(Session session)
    {
        this.adminSess = session;
    }

    /**
     * This command creates a new RBAC user. The command is valid only if the new user is
     * not already a member of the USERS data set. The USER data set is updated. The new user
     * does not own any session at the time of its creation.
     *
     * @param user User entity must contain {@link com.jts.fortress.rbac.User#userId} and {@link com.jts.fortress.rbac.User#ou} (required) and optional {@link com.jts.fortress.rbac.User#description},{@link com.jts.fortress.rbac.User#roles} and many others.
     * @return Returns entity containing user data that was added.
     * @throws com.jts.fortress.SecurityException Thrown in the event of data validation or system error.
     */
    public User addUser(User user)
        throws com.jts.fortress.SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".addUser");
        User retUser;
        FortRequest request = new FortRequest();
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.userAdd.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retUser = (User) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retUser;
    }

    /**
     * This command disables an existing user in the RBAC database. The command is valid
     * if and only if the user to be disabled is a member of the USERS data set. The USERS and
     * UA data sets and the assigned_users function are updated.
     * Method performs a "soft" delete.  It performs the following:
     * - sets the user status to "deleted"
     * - deassigns all roles from the user
     * - locks the user's password in LDAP
     * - revokes all perms that have been granted to user entity.
     *
     * @param user Contains the {@link User#userId} of the User targeted for deletion.
     * @throws com.jts.fortress.SecurityException  Thrown in the event of data validation or system error.
     */
    public void disableUser(User user)
        throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".disableUser");
        FortRequest request = new FortRequest();
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.userDisable.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This command deletes an existing user from the RBAC database. The command is valid
     * if and only if the user to be deleted is a member of the USERS data set. The USERS and
     * UA data sets and the assigned_users function are updated.
     * This method performs a "hard" delete.  It completely removes all data associated with this user from the directory.
     * User entity must exist in directory prior to making this call else exception will be thrown.
     *
     * @param user Contains the {@link User#userId} of the User targeted for deletion.
     * @throws SecurityException  Thrown in the event of data validation or system error.
     */
    public void deleteUser(User user)
        throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".deleteUser");
        FortRequest request = new FortRequest();
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.userDelete.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This method performs an update on User entity in directory.  Prior to making this call the entity must exist in
     * directory.
     *
     * @param user must contain {@link User#userId} and optional entity data to update i.e. desc, ou, properties, all attributes that are not set will be ignored.
     * @return Updated user entity data.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    public User updateUser(User user)
        throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".updateUser");
        User retUser;
        FortRequest request = new FortRequest();
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.userUpdate.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retUser = (User) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retUser;
    }

    /**
     * Method will change user's password.  This method will evaluate user's password policies.
     *
     * @param user        contains {@link User#userId} and old user password {@link User#password}.
     * @param newPassword contains new user password.
     * @throws com.jts.fortress.SecurityException Will be thrown in the event of password policy violation or system error.
     */
    public void changePassword(User user, char[] newPassword)
        throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".changePassword");
        VUtil.assertNotNullOrEmpty(newPassword, GlobalErrIds.USER_PW_NULL, OCLS_NM + ".changePassword");
        FortRequest request = new FortRequest();
        user.setNewPassword(newPassword);
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.userChange.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Method will lock user's password which will prevent the user from authenticating with directory.
     *
     * @param user entity contains {@link User#userId} of User to be locked.
     * @throws SecurityException will be thrown in the event of pw policy violation or system error.
     */
    public void lockUserAccount(User user)
        throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".lockUserAccount");
        FortRequest request = new FortRequest();
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.userLock.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Method will unlock user's password which will enable user to authenticate with directory.
     *
     * @param user entity contains {@link @link User#userId} of User to be unlocked.
     * @throws SecurityException will be thrown in the event of pw policy violation or system error.
     */
    public void unlockUserAccount(User user)
        throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".unlockUserAccount");
        FortRequest request = new FortRequest();
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.userUnlock.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Method will reset user's password which will require user to change password before successful authentication with directory.
     * This method will not evaulate password policies on the new user password as it must be changed before use.
     *
     * @param user entity contains {@link User#userId} of User to be reset.
     * @throws SecurityException will be thrown in the event of pw policy violation or system error.
     */
    public void resetPassword(User user, char[] newPassword)
        throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".resetPassword");
        VUtil.assertNotNullOrEmpty(newPassword, GlobalErrIds.USER_PW_NULL, OCLS_NM + ".resetPassword");
        FortRequest request = new FortRequest();
        user.setNewPassword(newPassword);
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.userReset.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This command creates a new role. The command is valid if and only if the new role is not
     * already a member of the ROLES data set. The ROLES data set is updated.
     * Initially, no user or permission is assigned to the new role.
     *
     * @param role must contains {@link com.jts.fortress.rbac.Role#name} (required) and optional {@link com.jts.fortress.rbac.Role#description}.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    public Role addRole(Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".addRole");
        Role retRole;
        FortRequest request = new FortRequest();
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.roleAdd.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retRole = (Role) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retRole;
    }

    /**
     * This command deletes an existing role from the RBAC database. The command is valid
     * if and only if the role to be deleted is a member of the ROLES data set and has been
     * deassigned from all users.
     *
     * @param role Contains {@link Role#name} for Role to delete.
     * @throws com.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    public void deleteRole(Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".deleteRole");
        FortRequest request = new FortRequest();
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.roleDelete.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Method will update a Role entity in the directory.  The role must exist prior to this call.
     *
     * @param role must contains {@link Role#name} and may contain new description or {@link com.jts.fortress.util.time.Constraint}
     * @throws com.jts.fortress.SecurityException in the event of validation or system error.
     */
    public Role updateRole(Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".updateRole");
        Role retRole;
        FortRequest request = new FortRequest();
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.roleUpdate.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retRole = (Role) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retRole;
    }

    /**
     * This command assigns a user to a role.
     * <p>
     * <ul>
     * <li> The command is valid if and only if:
     * <li> The user is a member of the USERS data set
     * <li> The role is a member of the ROLES data set
     * <li> The user is not already assigned to the role
     * <li> The SSD constraints are satisfied after assignment.
     * </ul>
     * </p>
     * <p>
     * Successful completion of this op, the following occurs:
     * </p>
     * <ul>
     * <li> User entity (resides in people container) has role assignment added to aux object class attached to actual user record.
     * <li> Role entity (resides in role container) has userId added as role occupant.
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
     *
     * @param uRole must contain {@link UserRole#userId} and {@link UserRole#name} and optional {@code Constraints}.
     * @throws com.jts.fortress.SecurityException in the event of validation or system error.
     */
    public void assignUser(UserRole uRole)
        throws SecurityException
    {
        VUtil.assertNotNull(uRole, GlobalErrIds.URLE_NULL, OCLS_NM + ".assignUser");
        FortRequest request = new FortRequest();
        request.setEntity(uRole);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.roleAsgn.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This command deletes the assignment of the User from the Role entities. The command is
     * valid if and only if the user is a member of the USERS data set, the role is a member of
     * the ROLES data set, and the user is assigned to the role.
     * Any sessions that currently have this role activated will not be effected.
     * Successful completion includes:
     * User entity in USER data set has role assignment removed.
     * Role entity in ROLE data set has userId removed as role occupant.
     * (optional) Temporal constraints will be removed from user aux object if set prior to call.
     *
     * @param uRole must contain {@link UserRole#userId} and {@link UserRole#name}.
     * @throws SecurityException - in the event data error in user or role objects or system error.
     */
    public void deassignUser(UserRole uRole)
        throws SecurityException
    {
        VUtil.assertNotNull(uRole, GlobalErrIds.URLE_NULL, OCLS_NM + ".deassignUser");
        FortRequest request = new FortRequest();
        request.setEntity(uRole);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.roleDeasgn.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This method will add permission operation to an existing permission object which resides under {@code ou=Permissions,ou=RBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may have {@link com.jts.fortress.rbac.Role} or {@link com.jts.fortress.rbac.User} associations.  The target {@link Permission} must not exist prior to calling.
     * A Fortress Permission instance exists in a hierarchical, one-many relationship between its parent and itself as stored in ldap tree: ({@link com.jts.fortress.rbac.PermObj}*->{@link Permission}).
     *
     * @param perm must contain the object, {@link com.jts.fortress.rbac.Permission#objectName}, and operation, {@link Permission#opName}, that identifies target along with optional other attributes..
     * @return copy of Permission entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    public Permission addPermission(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, OCLS_NM + ".addPermission");
        Permission retPerm;
        FortRequest request = new FortRequest();
        request.setEntity(perm);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.permAdd.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retPerm = (Permission) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retPerm;
    }

    /**
     * This method will update permission operation pre-existing in target directory under {@code ou=Permissions,ou=RBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may also contain {@link com.jts.fortress.rbac.Role} or {@link com.jts.fortress.rbac.User} associations to add or remove using this function.
     * The perm operation must exist before making this call.  Only non-null attributes will be updated.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target and any optional data to update.  Null or empty attributes will be ignored.
     * @return copy of permOp entity.
     * @throws com.jts.fortress.SecurityException - thrown in the event of perm object data or system error.
     */
    public Permission updatePermission(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, OCLS_NM + ".updatePermission");
        Permission retPerm;
        FortRequest request = new FortRequest();
        request.setEntity(perm);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.permUpdate.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retPerm = (Permission) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retPerm;
    }

    /**
     * This method will remove permission operation entity from permission object. A Fortress permission is (object->operation).
     * The perm operation must exist before making this call.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @throws com.jts.fortress.SecurityException - thrown in the event of perm object data or system error.
     */
    public void deletePermission(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, OCLS_NM + ".deletePermission");
        FortRequest request = new FortRequest();
        request.setEntity(perm);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.permDelete.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This method will add permission object to perms container in directory. The perm object must not exist before making this call.
     * A {@link com.jts.fortress.rbac.PermObj} instance exists in a hierarchical, one-many relationship between itself and children as stored in ldap tree: ({@link com.jts.fortress.rbac.PermObj}*->{@link Permission}).
     *
     * @param pObj must contain the {@link com.jts.fortress.rbac.PermObj#objectName} and {@link com.jts.fortress.rbac.PermObj#ou}.  The other attributes are optional.
     * @return copy of permObj entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    public PermObj addPermObj(PermObj pObj)
        throws SecurityException
    {
        VUtil.assertNotNull(pObj, GlobalErrIds.PERM_OBJECT_NULL, OCLS_NM + ".addPermObj");
        PermObj retObj;
        FortRequest request = new FortRequest();
        request.setEntity(pObj);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.objAdd.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retObj = (PermObj) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retObj;
    }

    /**
     * This method will update permission object in perms container in directory.  The perm object must exist before making this call.
     * A {@link PermObj} instance exists in a hierarchical, one-many relationship between itself and children as stored in ldap tree: ({@link PermObj}*->{@link Permission}).
     *
     * @param pObj must contain the {@link PermObj#objectName}. Only non-null attributes will be updated.
     * @return copy of newly updated permObj entity.
     * @throws com.jts.fortress.SecurityException - thrown in the event of perm object data or system error.
     */
    public PermObj updatePermObj(PermObj pObj)
        throws SecurityException
    {
        VUtil.assertNotNull(pObj, GlobalErrIds.PERM_OBJECT_NULL, OCLS_NM + ".updatePermObj");
        PermObj retObj;
        FortRequest request = new FortRequest();
        request.setEntity(pObj);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.objUpdate.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retObj = (PermObj) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retObj;
    }

    /**
     * This method will remove permission object to perms container in directory.  This method will also remove
     * in associated permission objects that are attached to this object.
     *
     * @param pObj must contain the {@link PermObj#objectName} of object targeted for removal.
     * @return copy of permObj entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    public void deletePermObj(PermObj pObj)
        throws SecurityException
    {
        VUtil.assertNotNull(pObj, GlobalErrIds.PERM_OBJECT_NULL, OCLS_NM + ".deletePermObj");
        FortRequest request = new FortRequest();
        request.setEntity(pObj);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.objDelete.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This command grants a role the permission to perform an operation on an object to a role.
     * The command is implemented by granting permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * and the role is a member of the ROLES data set.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param role must contains {@link Role#name}.
     * @throws com.jts.fortress.SecurityException Thrown in the event of data validation or system error.
     */
    public void grantPermission(Permission perm, Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, OCLS_NM + ".grantPermission");
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".grantPermission");
        FortRequest request = new FortRequest();
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin(perm.isAdmin());
        permGrant.setObjName(perm.getObjectName());
        permGrant.setObjId(perm.getObjectId());
        permGrant.setOpName(perm.getOpName());
        permGrant.setRoleNm(role.getName());
        request.setEntity(permGrant);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.roleGrant.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This command revokes the permission to perform an operation on an object from the set
     * of permissions assigned to a role. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * the role is a member of the ROLES data set, and the permission is assigned to that role.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param role must contains {@link Role#name}.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    public void revokePermission(Permission perm, Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, OCLS_NM + ".revokePermission");
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".revokePermission");
        FortRequest request = new FortRequest();
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin(perm.isAdmin());
        permGrant.setObjName(perm.getObjectName());
        permGrant.setObjId(perm.getObjectId());
        permGrant.setOpName(perm.getOpName());
        permGrant.setRoleNm(role.getName());
        request.setEntity(permGrant);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.roleRevoke.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Method grants a permission directly to a User entity.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param user must contain {@link User#userId} of target User entity.
     * @throws com.jts.fortress.SecurityException Thrown in the event of data validation or system error.
     */
    public void grantPermission(Permission perm, User user)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, OCLS_NM + ".grantPermissionUser");
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".grantPermissionUser");
        FortRequest request = new FortRequest();
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin(perm.isAdmin());
        permGrant.setObjName(perm.getObjectName());
        permGrant.setObjId(perm.getObjectId());
        permGrant.setOpName(perm.getOpName());
        permGrant.setUserId(user.getUserId());
        request.setEntity(permGrant);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.userGrant.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Method revokes a permission directly from a User entity.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param user must contain {@link User#userId} of target User entity.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    public void revokePermission(Permission perm, User user)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, OCLS_NM + ".revokePermission");
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".revokePermission");
        FortRequest request = new FortRequest();
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin(perm.isAdmin());
        permGrant.setObjName(perm.getObjectName());
        permGrant.setObjId(perm.getObjectId());
        permGrant.setOpName(perm.getOpName());
        permGrant.setUserId(user.getUserId());
        request.setEntity(permGrant);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.userRevoke.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This commands creates a new role childRole, and inserts it in the role hierarchy as an immediate descendant of
     * the existing role parentRole. The command is valid if and only if childRole is not a member of the ROLES data set,
     * and parentRole is a member of the ROLES data set.
     *
     * @param parentRole This entity must be present in ROLE data set.  Success will add role rel with childRole.
     * @param childRole  This entity must not be present in ROLE data set.  Success will add the new role entity to ROLE data set.
     *                   This method:
     *                   1 - Adds new role.
     *                   2 - Assigns role relationship between new childRole and pre-existing parentRole.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    public void addDescendant(Role parentRole, Role childRole)
        throws SecurityException
    {
        VUtil.assertNotNull(parentRole, GlobalErrIds.PARENT_ROLE_NULL, OCLS_NM + ".addDescendant");
        VUtil.assertNotNull(childRole, GlobalErrIds.CHILD_ROLE_NULL, OCLS_NM + ".addDescendant");
        FortRequest request = new FortRequest();
        RoleRelationship relationship = new RoleRelationship();
        relationship.setParent(parentRole);
        relationship.setChild(childRole);
        request.setEntity(relationship);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.roleDescendant.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This commands creates a new role parentRole, and inserts it in the role hierarchy as an immediate ascendant of
     * the existing role childRole. The command is valid if and only if parentRole is not a member of the ROLES data set,
     * and childRole is a member of the ROLES data set.
     * This method:
     * 1 - Adds new role.
     * 2 - Assigns role relationship between new parentRole and pre-existing childRole.
     *
     * @param parentRole completion of op assigns new child relationship with childRole.
     * @param childRole  completion of op assigns new parent relationship with parentRole.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    public void addAscendant(Role childRole, Role parentRole)
        throws SecurityException
    {
        VUtil.assertNotNull(parentRole, GlobalErrIds.PARENT_ROLE_NULL, OCLS_NM + ".addAscendant");
        VUtil.assertNotNull(childRole, GlobalErrIds.CHILD_ROLE_NULL, OCLS_NM + ".addAscendant");
        FortRequest request = new FortRequest();
        RoleRelationship relationship = new RoleRelationship();
        relationship.setParent(parentRole);
        relationship.setChild(childRole);
        request.setEntity(relationship);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.roleAscendent.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This commands establishes a new immediate inheritance relationship parentRole <<-- childRole between existing
     * roles parentRole, childRole. The command is valid if and only if parentRole and childRole are members of the ROLES data
     * set, parentRole is not an immediate ascendant of childRole, and childRole does not properly inherit parentRole (in order to
     * avoid cycle creation).
     *
     * @param parentRole completion of op deassigns child relationship with childRole.
     * @param childRole  completion of op deassigns parent relationship with parentRole.
     * @throws com.jts.fortress.SecurityException thrown in the event of data validation or system error.
     */
    public void addInheritance(Role parentRole, Role childRole)
        throws SecurityException
    {
        VUtil.assertNotNull(parentRole, GlobalErrIds.PARENT_ROLE_NULL, OCLS_NM + ".addInheritance");
        VUtil.assertNotNull(childRole, GlobalErrIds.CHILD_ROLE_NULL, OCLS_NM + ".addInheritance");
        FortRequest request = new FortRequest();
        RoleRelationship relationship = new RoleRelationship();
        relationship.setParent(parentRole);
        relationship.setChild(childRole);
        request.setEntity(relationship);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.roleAddinherit.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This command deletes an existing immediate inheritance relationship parentRole <<-- childRole. The command is
     * valid if and only if the roles parentRole and childRole are members of the ROLES data set, and parentRole is an
     * immediate ascendant of childRole. The new inheritance relation is computed as the reflexive-transitive
     * closure of the immediate inheritance relation resulted after deleting the relationship parentRole <<-- childRole.
     *
     * @param parentRole completion of op removes child relationship with childRole.
     * @param childRole  completion of op removes parent relationship with parentRole.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    public void deleteInheritance(Role parentRole, Role childRole)
        throws SecurityException
    {
        VUtil.assertNotNull(parentRole, GlobalErrIds.PARENT_ROLE_NULL, OCLS_NM + ".deleteInheritance");
        VUtil.assertNotNull(childRole, GlobalErrIds.CHILD_ROLE_NULL, OCLS_NM + ".deleteInheritance");
        FortRequest request = new FortRequest();
        RoleRelationship relationship = new RoleRelationship();
        relationship.setParent(parentRole);
        relationship.setChild(childRole);
        request.setEntity(relationship);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.roleDelinherit.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This command creates a named SSD set of roles and sets the cardinality n of its subsets
     * that cannot have common users. The command is valid if and only if:
     * 1 - the name of the SSD set is not already in use
     * 2 - all the roles in the SSD set are members of the ROLES data set
     * 3 - n is a natural number greater than or equal to 2 and less than or equal to the cardinality of the SSD role set,
     * 4 - the SSD constraint for the new role set is satisfied.
     *
     * @param ssdSet contains an instantiated reference to new SSD set containing, name, members, and cardinality (default 2)
     * @return reference to newly created SSDSet object.
     * @throws com.jts.fortress.SecurityException in the event of data validation or system error.
     */
    public SDSet createSsdSet(SDSet ssdSet)
        throws SecurityException
    {
        VUtil.assertNotNull(ssdSet, GlobalErrIds.SSD_NULL, OCLS_NM + ".createSsdSet");
        SDSet retSet = null;
        FortRequest request = new FortRequest();
        request.setEntity(ssdSet);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.ssdAdd.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSet = (SDSet) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSet;
    }

    /**
     * This command adds a role to a named SSD set of roles. The cardinality associated with the role set remains unchanged.
     * The command is valid if and only if:
     * 1 - the SSD role set exists, and
     * 2 - the role to be added is a member of the ROLES data set but not of a member of the SSD role set, and
     * 3 - the SSD constraint is satisfied after the addition of the role to the SSD role set.
     *
     * @param ssdSet contains an instantiated reference to new SSD set containing, name
     * @param role   contains instantiated Role object with role name field set.
     * @return reference to updated SSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    public SDSet addSsdRoleMember(SDSet ssdSet, Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(ssdSet, GlobalErrIds.SSD_NULL, OCLS_NM + ".addSsdRoleMember");
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".addSsdRoleMember");
        SDSet retSet = null;
        FortRequest request = new FortRequest();
        request.setEntity(ssdSet);
        request.setValue(role.getName());
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.ssdAddMember.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSet = (SDSet) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSet;
    }

    /**
     * This command removes a role from a named SSD set of roles. The cardinality associated with the role set remains unchanged.
     * The command is valid if and only if:
     * 1 - the SSD role set exists, and
     * 2 - the role to be removed is a member of the SSD role set, and
     * 3 - the cardinality associated with the SSD role set is less than the number of elements of the SSD role set.
     * Note that the SSD constraint should be satisfied after the removal of the role from the SSD role set.
     *
     * @param ssdSet contains an instantiated reference to new SSD set containing, name
     * @param role   contains instantiated Role object with role name field set.
     * @return reference to updated SSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    public SDSet deleteSsdRoleMember(SDSet ssdSet, Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(ssdSet, GlobalErrIds.SSD_NULL, OCLS_NM + ".deleteSsdRoleMember");
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".deleteSsdRoleMember");
        SDSet retSet = null;
        FortRequest request = new FortRequest();
        request.setEntity(ssdSet);
        request.setValue(role.getName());
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.ssdDelMember.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSet = (SDSet) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSet;
    }

    /**
     * This command deletes a SSD role set completely. The command is valid if and only if the SSD role set exists.
     *
     * @param ssdSet contains an instantiated reference to new SSD set containing, name
     * @return reference to deleted SSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    public SDSet deleteSsdSet(SDSet ssdSet)
        throws SecurityException
    {
        VUtil.assertNotNull(ssdSet, GlobalErrIds.SSD_NULL, OCLS_NM + ".deleteSsdSet");
        SDSet retSet = null;
        FortRequest request = new FortRequest();
        request.setEntity(ssdSet);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.ssdDelete.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSet = (SDSet) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSet;
    }

    /**
     * This command sets the cardinality associated with a given SSD role set. The command is valid if and only if:
     * 1 - the SSD role set exists, and
     * 2 - the new cardinality is a natural number greater than or equal to 2 and less than or equal to the number of elements of the SSD role set, and
     * 3 - the SSD constraint is satisfied after setting the new cardinality.
     *
     * @param ssdSet      contains an instantiated reference to new SSD set containing, name
     * @param cardinality integer value contains new cardinality value for data set.
     * @return reference to updated SSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    public SDSet setSsdSetCardinality(SDSet ssdSet, int cardinality)
        throws SecurityException
    {
        VUtil.assertNotNull(ssdSet, GlobalErrIds.SSD_NULL, OCLS_NM + ".setSsdSetCardinality");
        SDSet retSet = null;
        FortRequest request = new FortRequest();
        ssdSet.setCardinality(cardinality);
        request.setEntity(ssdSet);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.ssdCardUpdate.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSet = (SDSet) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSet;
    }

    /**
     * This command creates a named DSD set of roles and sets an associated cardinality n.
     * The DSD constraint stipulates that the DSD role set cannot contain n or more roles
     * simultaneously active in the same session.  The command is valid if and only if:
     * 1 - the name of the DSD set is not already in use
     * 2 - all the roles in the DSD set are members of the ROLES data set
     * 3 - n is a natural number greater than or equal to 2 and less than or equal to the cardinality of the DSD role set,
     * 4 - the DSD constraint for the new role set is satisfied.
     *
     * @param dsdSet contains an instantiated reference to new DSD set containing, name, members, and cardinality (default 2)
     * @return reference to newly created SSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    public SDSet createDsdSet(SDSet dsdSet)
        throws SecurityException
    {
        VUtil.assertNotNull(dsdSet, GlobalErrIds.SSD_NULL, OCLS_NM + ".createDsdSet");
        SDSet retSet = null;
        FortRequest request = new FortRequest();
        request.setEntity(dsdSet);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.dsdAdd.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSet = (SDSet) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSet;
    }


    /**
     * This command adds a role to a named DSD set of roles. The cardinality associated with
     * the role set remains unchanged. The command is valid if and only if:
     * 1 - the DSD role set exists, and
     * 2 - the role to be added is a member of the ROLES data set but not of a member of the DSD role set, and
     * 3 - the DSD constraint is satisfied after the addition of the role to the SSD role set.
     *
     * @param dsdSet contains an instantiated reference to new DSD set containing, name
     * @param role   contains instantiated Role object with role name field set.
     * @return reference to updated DSDSet object.
     * @throws com.jts.fortress.SecurityException in the event of data validation or system error.
     */
    public SDSet addDsdRoleMember(SDSet dsdSet, Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(dsdSet, GlobalErrIds.SSD_NULL, OCLS_NM + ".addDsdRoleMember");
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".addDsdRoleMember");
        SDSet retSet = null;
        FortRequest request = new FortRequest();
        request.setEntity(dsdSet);
        request.setValue(role.getName());
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.dsdAddMember.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSet = (SDSet) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSet;
    }


    /**
     * This command removes a role from a named DSD set of roles. The cardinality associated
     * with the role set remains unchanged. The command is valid if and only if:
     * 1 - the DSD role set exists, and
     * 2 - the role to be removed is a member of the DSD role set, and
     * 3 - the cardinality associated with the DSD role set is less than the number of elements of the DSD role set.
     * Note that the DSD constraint should be satisfied after the removal of the role from the DSD role set.
     *
     * @param dsdSet contains an instantiated reference to new DSD set containing, name
     * @param role   contains instantiated Role object with role name field set.
     * @return reference to updated DSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    public SDSet deleteDsdRoleMember(SDSet dsdSet, Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(dsdSet, GlobalErrIds.SSD_NULL, OCLS_NM + ".deleteDsdRoleMember");
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".deleteSsdRoleMember");
        SDSet retSet = null;
        FortRequest request = new FortRequest();
        request.setEntity(dsdSet);
        request.setValue(role.getName());
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.dsdDelMember.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSet = (SDSet) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSet;
    }


    /**
     * This command deletes a DSD role set completely. The command is valid if and only if the DSD role set exists.
     *
     * @param dsdSet contains an instantiated reference to new DSD set containing, name
     * @return reference to deleted DSDSet object.
     * @throws com.jts.fortress.SecurityException in the event of data validation or system error.
     */
    public SDSet deleteDsdSet(SDSet dsdSet)
        throws SecurityException
    {
        VUtil.assertNotNull(dsdSet, GlobalErrIds.SSD_NULL, OCLS_NM + ".deleteDsdSet");
        SDSet retSet = null;
        FortRequest request = new FortRequest();
        request.setEntity(dsdSet);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.dsdDelete.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSet = (SDSet) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSet;
    }


    /**
     * This command sets the cardinality associated with a given DSD role set. The command is valid if and only if:
     * 1 - the SSD role set exists, and
     * 2 - the new cardinality is a natural number greater than or equal to 2 and less than or equal to the number of elements of the SSD role set, and
     * 3 - the SSD constraint is satisfied after setting the new cardinality.
     *
     * @param dsdSet      contains an instantiated reference to new DSD set containing, name
     * @param cardinality integer value contains new cardinality value for data set.
     * @return reference to updated DSDSet object.
     * @throws com.jts.fortress.SecurityException in the event of data validation or system error.
     */
    public SDSet setDsdSetCardinality(SDSet dsdSet, int cardinality)
        throws SecurityException
    {
        VUtil.assertNotNull(dsdSet, GlobalErrIds.SSD_NULL, OCLS_NM + ".setSsdSetCardinality");
        SDSet retSet = null;
        FortRequest request = new FortRequest();
        dsdSet.setCardinality(cardinality);
        request.setEntity(dsdSet);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.Services.dsdCardUpdate.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSet = (SDSet) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSet;
    }

    /**
     * @param opName
     * @param entity
     * @throws SecurityException
     */
    private void setEntitySession(String opName, FortEntity entity) throws SecurityException
    {
        if (this.adminSess != null)
        {
            AdminUtil.setEntitySession(adminSess, new Permission(OCLS_NM, opName), entity);
        }
    }
}