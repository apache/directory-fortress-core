/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rest;

import com.jts.fortress.ReviewMgr;
import com.jts.fortress.SecurityException;
import com.jts.fortress.arbac.OrgUnit;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.rbac.PermObj;
import com.jts.fortress.rbac.Permission;
import com.jts.fortress.rbac.Role;
import com.jts.fortress.rbac.SDSet;
import com.jts.fortress.rbac.Session;
import com.jts.fortress.rbac.User;
import com.jts.fortress.rbac.UserRole;
import com.jts.fortress.util.AlphabeticalOrder;
import com.jts.fortress.util.attr.VUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This object performs administrative review functions on already provisioned Fortress RBAC entities
 * that reside in LDAP directory.  These APIs map directly to similar named APIs specified by ANSI and NIST RBAC models.
 * Many of the java doc function descriptions found below were taken directly from ANSI INCITS 359-2004.
 * The RBAC Functional specification describes administrative operations for the creation
 * and maintenance of RBAC element sets and relations; administrative review functions for
 * performing administrative queries; and system functions for creating and managing
 * RBAC attributes on user sessions and making access control decisions.
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
 * This object is thread safe.
 * <p/>
 *
 * @author smckinn
 * @created February 5, 2012
 */
public class ReviewMgrRestImpl
    implements ReviewMgr
{
    private static final String OCLS_NM = ReviewMgrRestImpl.class.getName();
    // thread unsafe variable:
    private Session adminSess;
    private static final String USERID = "demouser4";
    private static final String PW = "password";


    /**
     * Setting Session into this object will enforce ARBAC controls and render this class
     * thread unsafe..
     *
     * @param session
     */
    public void setAdmin(Session session)
    {
        this.adminSess = session;
    }

    /**
     * This method returns a matching permission entity to caller.
     *
     * @param permission must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return Permission entity that is loaded with data.
     * @throws SecurityException if permission not found or system error occurs.
     */
    public Permission readPermission(Permission permission)
        throws SecurityException
    {
        VUtil.assertNotNull(permission, GlobalErrIds.PERM_OPERATION_NULL, OCLS_NM + ".readPermission");
        Permission retPerm;
        FortRequest request = new FortRequest();
        request.setEntity(permission);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.permRead.toString());
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
     * Method reads permission object from perm container in directory.
     *
     * @param permObj entity contains the {@link PermObj#objectName} of target record.
     * @return PermObj loaded with perm object data.
     * @throws SecurityException is thrown if object not found or system error.
     */
    public PermObj readPermObj(PermObj permObj)
        throws SecurityException
    {
        VUtil.assertNotNull(permObj, GlobalErrIds.PERM_OBJECT_NULL, OCLS_NM + ".readPermObj");
        PermObj retObj;
        FortRequest request = new FortRequest();
        request.setEntity(permObj);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.objRead.toString());
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
     * Method returns a list of type Permission that match the perm object search string.
     *
     * @param permission contains object and operation name search strings.  Each contains 1 or more leading chars that correspond to object or op name.
     * @return List of type Permission.  Fortress permissions are object->operation mappings.  The permissions may contain
     *         assigned user, role or group entities as well.
     * @throws SecurityException thrown in the event of system error.
     */
    public List<Permission> findPermissions(Permission permission)
        throws SecurityException
    {
        VUtil.assertNotNull(permission, GlobalErrIds.PERM_OPERATION_NULL, OCLS_NM + ".findPermissions");
        List<Permission> retPerms;
        FortRequest request = new FortRequest();
        request.setEntity(permission);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.permSearch.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retPerms = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retPerms;
    }

    /**
     * Method returns a list of type Permission that match the perm object search string.
     *
     * @param permObj contains object name search string.  The search val contains 1 or more leading chars that correspond to object name.
     * @return List of type PermObj.  Fortress permissions are object->operation mappings.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of system error.
     */
    public List<PermObj> findPermObjs(PermObj permObj)
        throws SecurityException
    {
        VUtil.assertNotNull(permObj, GlobalErrIds.PERM_OBJECT_NULL, OCLS_NM + ".findPermObjs");
        List<PermObj> retObjs;
        FortRequest request = new FortRequest();
        request.setEntity(permObj);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.objSearch.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retObjs = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retObjs;
    }

    /**
     * Method returns a list of type Permission that match the perm object search string.
     *
     * @param ou contains org unit name {@link com.jts.fortress.arbac.OrgUnit#name}.  The search val contains the full name of matching ou in OS-P data set.
     * @return List of type PermObj.  Fortress permissions are object->operation mappings.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of system error.
     */
    public List<PermObj> findPermObjs(OrgUnit ou)
        throws SecurityException
    {
        VUtil.assertNotNull(ou, GlobalErrIds.ORG_NULL_PERM, OCLS_NM + ".findPermObjs");
        List<PermObj> retObjs;
        FortRequest request = new FortRequest();
        PermObj inObj = new PermObj();
        inObj.setOu(ou.getName());
        request.setEntity(inObj);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.objSearch.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retObjs = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retObjs;
    }

    /**
     * Method reads Role entity from the role container in directory.
     *
     * @param role contains role name, {@link Role#name}, to be read.
     * @return Role entity that corresponds with role name.
     * @throws SecurityException will be thrown if role not found or system error occurs.
     */
    public Role readRole(Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".readRole");
        Role retRole;
        FortRequest request = new FortRequest();
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.roleRead.toString());
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
     * Method will return a list of type Role matching all or part of Role name, {@link Role#name}.
     *
     * @param searchVal contains the all or some of the chars corresponding to role entities stored in directory.
     * @return List of type Role containing role entities that match the search criteria.
     * @throws com.jts.fortress.SecurityException
     *          in the event of system error.
     */
    public List<Role> findRoles(String searchVal)
        throws SecurityException
    {
        VUtil.assertNotNull(searchVal, GlobalErrIds.ROLE_NM_NULL, OCLS_NM + ".findRoles");
        List<Role> retRoles;
        FortRequest request = new FortRequest();
        request.setValue(searchVal);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.roleSearch.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retRoles = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retRoles;
    }

    /**
     * Method returns a list of roles of type String.  This method can be limited by integer value that indicates max
     * number of records that may be contained in the result set.  This number can further limit global default but can
     * not increase the max.  This method is called by the Websphere Realm impl.
     *
     * @param searchVal contains all or some leading chars that correspond to roles stored in the role container in the directory.
     * @param limit     integer value specifies the max records that may be returned in the result set.
     * @return
     * @throws SecurityException in the event of system error.
     */
    public List<String> findRoles(String searchVal, int limit)
        throws SecurityException
    {
        VUtil.assertNotNull(searchVal, GlobalErrIds.ROLE_NM_NULL, OCLS_NM + ".findRoles");
        List<String> retRoles;
        FortRequest request = new FortRequest();
        request.setValue(searchVal);
        request.setLimit(limit);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.roleSearch.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retRoles = response.getValues();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retRoles;
    }

    /**
     * Method returns matching User entity that is contained within the people container in the directory.
     *
     * @param user entity contains a value {@link User#userId} that matches record in the directory.  userId is globally unique in
     *             people container.
     * @return entity containing matching user data.
     * @throws com.jts.fortress.SecurityException
     *          if record not found or system error occurs.
     */
    public final User readUser(User user)
        throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".readUser");
        User retUser = null;
        FortRequest request = new FortRequest();
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.userRead.toString());
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
     * Return a list of type User of all users in the people container that match all or part of the {@link User#userId} field passed in User entity.
     *
     * @param user contains all or some leading chars that match userIds stored in the directory.
     * @return List of type User.
     * @throws SecurityException In the event of system error.
     */
    public final List<User> findUsers(User user)
        throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".findUsers");
        List<User> retUsers;
        FortRequest request = new FortRequest();
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.userSearch.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retUsers = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retUsers;
    }

    /**
     * Return a list of type User of all users in the people container that match the name field passed in OrgUnit entity.
     *
     * @param ou contains name of User OU, {@link com.jts.fortress.arbac.OrgUnit#name} that match ou attribute associated with User entity in the directory.
     * @return List of type User.
     * @throws SecurityException In the event of system error.
     */
    public List<User> findUsers(OrgUnit ou)
        throws SecurityException
    {
        VUtil.assertNotNull(ou, GlobalErrIds.ORG_NULL_USER, OCLS_NM + ".findUsers");
        List<User> retUsers;
        FortRequest request = new FortRequest();
        User inUser = new User();
        inUser.setOu(ou.getName());
        request.setEntity(inUser);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.userSearch.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retUsers = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retUsers;
    }

    /**
     * Return a list of type String of all users in the people container that match the userId field passed in User entity.
     * This method is used by the Websphere realm component.  The max number of returned users may be set by the integer limit arg.
     *
     * @param user  contains all or some leading chars that correspond to users stored in the directory.
     * @param limit integer value sets the max returned records.
     * @return List of type String containing matching userIds.
     * @throws SecurityException in the event of system error.
     */
    public final List<String> findUsers(User user, int limit)
        throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".findUsers");
        List<String> retUsers;
        FortRequest request = new FortRequest();
        request.setLimit(limit);
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.userSearch.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retUsers = response.getValues();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retUsers;
    }

    /**
     * This function returns the set of users assigned to a given role. The function is valid if and
     * only if the role is a member of the ROLES data set.
     * The max number of users returned is constrained by limit argument.
     * This method is used by the Websphere realm component.  This method does NOT use hierarchical rbac.
     *
     * @param role  Contains {@link Role#name} of Role entity assigned to user.
     * @param limit integer value sets the max returned records.
     * @return List of type String containing userIds assigned to a particular role.
     * @throws com.jts.fortress.SecurityException
     *          in the event of data validation or system error.
     */
    public List<String> assignedUsers(Role role, int limit)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".assignedUsers");
        List<String> retUsers;
        FortRequest request = new FortRequest();
        request.setLimit(limit);
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.userAsigned.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retUsers = response.getValues();
            // do not return a null list to the caller:
            if(retUsers == null)
            {
                retUsers = new ArrayList<String>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retUsers;
    }

    /**
     * This method returns the data set of all users who are assigned the given role.  This searches the User data set for
     * Role relationship.  This method does NOT search for hierarchical RBAC Roles relationships.
     *
     * @param role contains the role name, {@link Role#name} used to search the User data set.
     * @return List of type User containing the users assigned data.
     * @throws SecurityException If system error occurs.
     */
    public List<User> assignedUsers(Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".assignedUsers");
        List<User> retUsers;
        FortRequest request = new FortRequest();
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.userAsigned.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retUsers = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retUsers;
    }

    /**
     * This function returns the set of roles assigned to a given user. The function is valid if and
     * only if the user is a member of the USERS data set.
     *
     * @param user contains {@link User#userId} matching User entity targeted in the directory.
     * @return List of type UserRole containing the Roles assigned to User.
     * @throws SecurityException If user not found or system error occurs.
     */
    public List<UserRole> assignedRoles(User user)
        throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".assignedRoles");
        List<UserRole> retUserRoles = null;
        FortRequest request = new FortRequest();
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.roleAsigned.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retUserRoles = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retUserRoles;
    }

    /**
     * This function returns the set of roles assigned to a given user. The function is valid if and
     * only if the user is a member of the USERS data set.
     *
     * @param userId matches userId stored in the directory.
     * @return List of type String containing the role names of all roles assigned to user.
     * @throws SecurityException If user not found or system error occurs.
     */
    public List<String> assignedRoles(String userId)
        throws SecurityException
    {
        VUtil.assertNotNullOrEmpty(userId, GlobalErrIds.USER_NULL, OCLS_NM + ".assignedRoles");
        List<String> retUserRoles;
        FortRequest request = new FortRequest();
        request.setValue(userId);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.roleAsigned.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retUserRoles = response.getValues();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retUserRoles;
    }

    /**
     * This function returns the set of users authorized to a given role, i.e., the users that are assigned to a role that
     * inherits the given role. The function is valid if and only if the given role is a member of the ROLES data set.
     *
     * @param role Contains role name, {@link Role#name} of Role entity assigned to User.
     * @return List of type User containing all user's that having matching role assignment.
     * @throws SecurityException In the event the role is not present in directory or system error occurs.
     */
    public List<User> authorizedUsers(Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".authorizedUsers");
        List<User> retUsers;
        FortRequest request = new FortRequest();
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.roleAuthzed.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retUsers = response.getEntities();
            if(retUsers == null)
            {
                retUsers = new ArrayList<User>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retUsers;
    }

    /**
     * This function returns the set of roles authorized for a given user. The function is valid if
     * and only if the user is a member of the USERS data set.
     *
     * @param user contains the {@link User#userId} matching User entity stored in the directory.
     * @return Set of type String containing the roles assigned and roles inherited.
     * @throws SecurityException If user not found or system error occurs.
     */
    public Set<String> authorizedRoles(User user)
        throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".authorizedRoles");
        Set<String> retRoleNames = new TreeSet<String>(new AlphabeticalOrder());
        FortRequest request = new FortRequest();
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.userAuthzed.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Set<String> tempNames = response.getValueSet();
            // TODO: This is done to use a case insensitive TreeSet for returned names.  Find a better way to do this:
            retRoleNames.addAll(tempNames);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retRoleNames;
    }

    /**
     * This function returns the set of all permissions (op, obj), granted to or inherited by a
     * given role. The function is valid if and only if the role is a member of the ROLES data
     * set.
     *
     * @param role contains role name, {@link Role#name} of Role entity Permission is granted to.
     * @return List of type Permission that contains all perms granted to a role.
     * @throws SecurityException In the event system error occurs.
     */
    public List<Permission> rolePermissions(Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".rolePermissions");
        List<Permission> retPerms;
        FortRequest request = new FortRequest();
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.rolePerms.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retPerms = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retPerms;
    }

    /**
     * This function returns the set of permissions a given user gets through his/her authorized
     * roles. The function is valid if and only if the user is a member of the USERS data set.
     *
     * @param user contains the {@link User#userId} of User targeted for search.
     * @return List of type Permission containing matching permission entities.
     * @throws com.jts.fortress.SecurityException
     *
     */
    public List<Permission> userPermissions(User user)
        throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, OCLS_NM + ".userPermissions");
        List<Permission> retPerms;
        FortRequest request = new FortRequest();
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.userPerms.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retPerms = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retPerms;
    }

    /**
     * Return a list of type String of all roles that have granted a particular permission.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return List of type string containing the role names that have the matching perm granted.
     * @throws SecurityException in the event permission not found or system error occurs.
     */
    public List<String> permissionRoles(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OBJECT_NULL, OCLS_NM + ".permissionRoles");
        List<String> retRoleNames;
        FortRequest request = new FortRequest();
        request.setEntity(perm);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.permRoles.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retRoleNames = response.getValues();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retRoleNames;
    }

    /**
     * Return all role names that have been authorized for a given permission.  This will process role hierarchies to determine set of all Roles who have access to a given permission.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return Set of type String containing all roles names that have been granted a particular permission.
     * @throws com.jts.fortress.SecurityException
     *          in the event of validation or system error.
     */
    public Set<String> authorizedPermissionRoles(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, OCLS_NM + ".authorizedPermissionRoles");
        Set<String> retRoleNames = new TreeSet<String>(new AlphabeticalOrder());
        FortRequest request = new FortRequest();
        request.setEntity(perm);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.permRolesAuthzed.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Set<String> tempNames = response.getValueSet();
            // TODO: This is done to use a case insensitive TreeSet for returned names.  Find a better way to do this:
            retRoleNames.addAll(tempNames);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retRoleNames;
    }

    /**
     * Return all userIds that have been granted (directly) a particular permission.  This will not consider assigned or authorized Roles.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return List of type String containing all userIds that have been granted a particular permission.
     * @throws com.jts.fortress.SecurityException
     *          in the event of validation or system error.
     */
    public List<String> permissionUsers(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, OCLS_NM + ".permissionUsers");
        List<String> retUsers;
        FortRequest request = new FortRequest();
        request.setEntity(perm);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.permUsers.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retUsers = response.getValues();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retUsers;
    }

    /**
     * Return all userIds that have been authorized for a given permission.  This will process role hierarchies to determine set of all Users who have access to a given permission.
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return Set of type String containing all userIds that have been granted a particular permission.
     * @throws com.jts.fortress.SecurityException
     *          in the event of validation or system error.
     */
    public Set<String> authorizedPermissionUsers(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, OCLS_NM + ".authorizedPermissionUsers");
        Set<String> retUserIds = new TreeSet<String>(new AlphabeticalOrder());
        FortRequest request = new FortRequest();
        request.setEntity(perm);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.permUsersAuthzed.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Set<String> tempNames = response.getValueSet();
            // TODO: This is done to use a case insensitive TreeSet for returned names.  Find a better way to do this:
            retUserIds.addAll(tempNames);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retUserIds;
    }

    /**
     * This function returns the list of all SSD role sets that have a particular Role as member or Role's
     * parent as a member.  If the Role parameter is left blank, function will return all SSD role sets.
     *
     * @param role Will contain the role name, {@link Role#name}, for targeted SSD set or null to return all
     * @return List containing all matching SSD's.
     * @throws com.jts.fortress.SecurityException
     *          in the event of data or system error.
     */
    public List<SDSet> ssdRoleSets(Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".ssdRoleSets");
        List<SDSet> retSsdRoleSets;
        FortRequest request = new FortRequest();
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.ssdSets.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSsdRoleSets = response.getEntities();
            if(retSsdRoleSets == null)
            {
                retSsdRoleSets = new ArrayList<SDSet>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSsdRoleSets;
    }

    /**
     * This function returns the SSD data set that matches a particular set name.
     *
     * @param set Will contain the name for existing SSD data set
     * @return SDSet containing all attributes from matching SSD name.
     * @throws com.jts.fortress.SecurityException
     *          in the event of data or system error.
     */
    public SDSet ssdRoleSet(SDSet set)
        throws SecurityException
    {
        VUtil.assertNotNull(set, GlobalErrIds.SSD_NULL, OCLS_NM + ".ssdRoleSet");
        SDSet retSet;
        FortRequest request = new FortRequest();
        request.setEntity(set);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.ssdRead.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSet = (SDSet)response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSet;
    }

    /**
     * This function returns the set of roles of a SSD role set. The function is valid if and only if the
     * role set exists.
     *
     * @param ssd contains the name for the SSD set targeted.
     * @return Map containing all Roles that are members of SSD data set.
     * @throws SecurityException in the event of data or system error.
     */
    public Set<String> ssdRoleSetRoles(SDSet ssd)
        throws SecurityException
    {
        VUtil.assertNotNull(ssd, GlobalErrIds.SSD_NULL, OCLS_NM + ".ssdRoleSetRoles");
        Set<String> retRoleNames = new TreeSet<String>(new AlphabeticalOrder());
        FortRequest request = new FortRequest();
        request.setEntity(ssd);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.ssdRoles.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Set<String> tempNames = response.getValueSet();
            // TODO: This is done to use a case insensitive TreeSet for returned names.  Find a better way to do this:
            retRoleNames.addAll(tempNames);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retRoleNames;
    }

    /**
     * This function returns the cardinality associated with a SSD role set. The function is valid if and only if the
     * role set exists.
     *
     * @param ssd contains the name of the SSD set targeted, {@link SDSet#name}.
     * @return int value containing cardinality of SSD set.
     * @throws SecurityException in the event of data or system error.
     */
    public int ssdRoleSetCardinality(SDSet ssd)
        throws SecurityException
    {
        VUtil.assertNotNull(ssd, GlobalErrIds.SSD_NULL, OCLS_NM + ".ssdRoleSetCardinality");
        String szResponse = RestUtils.get(USERID, PW, ssd.getName(), null, null, Ids.Services.ssdCard.toString());
        int cardinality;
        if(VUtil.isNotNullOrEmpty(szResponse))
        {
            Integer tmpInt = new Integer(szResponse);
            cardinality = tmpInt.intValue();
        }
        else
        {
            throw new SecurityException(GlobalErrIds.REST_GET_FAILED, OCLS_NM + ".ssdRoleSetCardinality failed HTTP Get");
        }
        return cardinality;
    }

    /**
     * This function returns the list of all dSD role sets that have a particular Role as member or Role's
     * parent as a member.  If the Role parameter is left blank, function will return all dSD role sets.
     *
     * @param role Will contain the role name, {@link Role#name}, for targeted dSD set or null to return all
     * @return List containing all matching dSD's.
     * @throws com.jts.fortress.SecurityException
     *          in the event of data or system error.
     */
    public List<SDSet> dsdRoleSets(Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, OCLS_NM + ".dsdRoleSets");
        List<SDSet> retDsdRoleSets;
        FortRequest request = new FortRequest();
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.dsdSets.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retDsdRoleSets = response.getEntities();
            if(retDsdRoleSets == null)
            {
                retDsdRoleSets = new ArrayList<SDSet>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retDsdRoleSets;
    }

    /**
     * This function returns the DSD data set that matches a particular set name.
     *
     * @param set Will contain the name for existing DSD data set, {@link SDSet#name}.
     * @return SDSet containing all attributes from matching DSD name.
     * @throws com.jts.fortress.SecurityException
     *          in the event of data or system error.
     */
    public SDSet dsdRoleSet(SDSet set)
        throws SecurityException
    {
        VUtil.assertNotNull(set, GlobalErrIds.DSD_NULL, OCLS_NM + ".dsdRoleSet");
        SDSet retSet;
        FortRequest request = new FortRequest();
        request.setEntity(set);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.dsdRead.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSet = (SDSet)response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSet;
    }

    /**
     * This function returns the set of roles of a DSD role set. The function is valid if and only if the
     * role set exists.
     *
     * @param dsd contains the name for the DSD set targeted, {@link SDSet#name}.
     * @return List containing all Roles that are members of DSD data set.
     * @throws SecurityException in the event of data or system error.
     */
    public Set<String> dsdRoleSetRoles(SDSet dsd)
        throws SecurityException
    {
        VUtil.assertNotNull(dsd, GlobalErrIds.SSD_NULL, OCLS_NM + ".dsdRoleSetRoles");
        Set<String> retRoleNames = new TreeSet<String>(new AlphabeticalOrder());
        FortRequest request = new FortRequest();
        request.setEntity(dsd);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(USERID, PW, szRequest, Ids.Services.dsdRoles.toString());
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Set<String> tempNames = response.getValueSet();
            // TODO: This is done to use a case insensitive TreeSet for returned names.  Find a better way to do this:
            retRoleNames.addAll(tempNames);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retRoleNames;
    }

    /**
     * This function returns the cardinality associated with a DSD role set. The function is valid if and only if the
     * role set exists.
     *
     * @param dsd contains the name of the DSD set targeted, {@link SDSet#name}.
     * @return int value containing cardinality of DSD set.
     * @throws SecurityException in the event of data or system error.
     */
    public int dsdRoleSetCardinality(SDSet dsd)
        throws SecurityException
    {
        VUtil.assertNotNull(dsd, GlobalErrIds.DSD_NULL, OCLS_NM + ".dsdRoleSetCardinality");
        String szResponse = RestUtils.get(USERID, PW, dsd.getName(), null, null, Ids.Services.dsdCard.toString());
        int cardinality;
        if(VUtil.isNotNullOrEmpty(szResponse))
        {
            Integer tmpInt = new Integer(szResponse);
            cardinality = tmpInt.intValue();
        }
        else
        {
            throw new SecurityException(GlobalErrIds.REST_GET_FAILED, OCLS_NM + ".dsdRoleSetCardinality failed HTTP Get");
        }
        return cardinality;
    }
}