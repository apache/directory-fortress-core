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

import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.ReviewMgr;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.SDSet;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.model.VUtil;
import org.apache.directory.fortress.core.util.ObjUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class performs administrative review functions on already provisioned Fortress RBAC entities
 * that reside in LDAP directory.  These APIs map directly to similar named APIs specified by ANSI and NIST RBAC models.
 * Many of the java doc function descriptions found below were taken directly from ANSI INCITS 359-2004.
 * The RBAC Functional specification describes administrative operations for the creation
 * and maintenance of RBAC element sets and relations; administrative review functions for
 * performing administrative queries; and system functions for creating and managing
 * RBAC attributes on user sessions and making access control decisions.
 * <p/>
 * <hr>
 * <h4>RBAC0 - Core</h4>
 * Many-to-many relationship between Users, Roles and Permissions. Selective role activation into sessions.  API to add, update, delete identity data and perform identity and access control decisions during runtime operations.
 * <p/>
 * <img src="../doc-files/RbacCore.png">
 * <hr>
 * <h4>RBAC1 - General Hierarchical Roles</h4>
 * Simplifies role engineering tasks using inheritance of one or more parent roles.
 * <p/>
 * <img src="../doc-files/RbacHier.png">
 * <hr>
 * <h4>RBAC2 - Static Separation of Duty (SSD) Relations</h4>
 * Enforce mutual membership exclusions across role assignments.  Facilitate dual control policies by restricting which roles may be assigned to users in combination.  SSD provide added granularity for authorization limits which help enterprises meet strict compliance regulations.
 * <p/>
 * <img src="../doc-files/RbacSSD.png">
 * <hr>
 * <h4>RBAC3 - Dynamic Separation of Duty (DSD) Relations</h4>
 * Control allowed role combinations to be activated within an RBAC session.  DSD policies fine tune role policies that facilitate authorization dual control and two man policy restrictions during runtime security checks.
 * <p/>
 * <img src="../doc-files/RbacDSD.png">
 * <hr>
 * <p/>
 * This class is NOT thread safe if parent instance variables ({@link #contextId} or {@link #adminSess}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class ReviewMgrImpl extends Manageable implements ReviewMgr
{
    private static final String CLS_NM = ReviewMgrImpl.class.getName();
    private static final UserP userP = new UserP();
    private static final RoleP roleP = new RoleP();
    private static final PermP permP = new PermP();
    private static final SdP ssdP = new SdP();

    /**
     * This method returns a matching permission entity to caller.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.model.Permission#objName} - contains the name of existing object being targeted</li>
     * <li>{@link org.apache.directory.fortress.core.model.Permission#opName} - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param permission must contain the object, {@link org.apache.directory.fortress.core.model.Permission#objName}, and operation, {@link org.apache.directory.fortress.core.model.Permission#opName}, and optionally object id of targeted permission entity.
     * @return Permission entity that is loaded with data.
     * @throws SecurityException if permission not found or system error occurs.
     */
    @Override
    public Permission readPermission(Permission permission)
        throws SecurityException
    {
        String methodName = "readPermission";
        assertContext(CLS_NM, methodName, permission, GlobalErrIds.PERM_OPERATION_NULL);
        VUtil.assertNotNullOrEmpty(permission.getObjName(), GlobalErrIds.PERM_OBJECT_NM_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNullOrEmpty(permission.getOpName(), GlobalErrIds.PERM_OPERATION_NM_NULL, CLS_NM + "." + methodName);
        checkAccess(CLS_NM, methodName);
        return permP.read(permission);
    }

    /**
     * Method reads permission object from perm container in directory.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.model.PermObj#objName} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param permObj entity contains the {@link org.apache.directory.fortress.core.model.PermObj#objName} of target record.
     * @return PermObj loaded with perm object data.
     * @throws SecurityException is thrown if object not found or system error.
     */
    @Override
    public PermObj readPermObj(PermObj permObj)
        throws SecurityException
    {
        String methodName = "readPermObj";
        assertContext(CLS_NM, methodName, permObj, GlobalErrIds.PERM_OBJECT_NULL);
        VUtil.assertNotNull(permObj.getObjName(), GlobalErrIds.PERM_OBJECT_NM_NULL, CLS_NM + "." + methodName);
        checkAccess(CLS_NM, methodName);
        return permP.read(permObj);
    }

    /**
     * Method returns a list of type Permission that match the perm object search string.
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link Permission#objName} - contains one or more characters of existing object being targeted</li>
     * <li>{@link Permission#opName} - contains one or more characters of existing permission operation</li>
     * </ul>
     *
     * @param permission contains object and operation name search strings.  Each contains 1 or more leading chars that correspond to object or op name.
     * @return List of type Permission.  Fortress permissions are object->operation mappings.  The permissions may contain
     *         assigned user, role or group entities as well.
     * @throws SecurityException thrown in the event of system error.
     */
    @Override
    public List<Permission> findPermissions(Permission permission)
        throws SecurityException
    {
        String methodName = "findPermissions";
        assertContext(CLS_NM, methodName, permission, GlobalErrIds.PERM_OPERATION_NULL);
        checkAccess(CLS_NM, methodName);
        return permP.search(permission);
    }

    /**
     * Method returns a list of type PermObj that match the perm object search string.
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link PermObj#objName} - contains one or more characters of existing object being targeted</li>
     * </ul>
     *
     * @param permObj contains object name search string.  The search val contains 1 or more leading chars that correspond to object name.
     * @return List of type PermObj.  Fortress permissions are object->operation mappings.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          thrown in the event of system error.
     */
    @Override
    public List<PermObj> findPermObjs(PermObj permObj)
        throws SecurityException
    {
        String methodName = "findPermObjs";
        assertContext(CLS_NM, methodName, permObj, GlobalErrIds.PERM_OBJECT_NULL);
        checkAccess(CLS_NM, methodName);
        return permP.search(permObj);
    }

    /**
     * Method returns a list of type Permission that match the perm object search string.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.model.OrgUnit#name} - contains one or more characters of org unit associated with existing object being targeted</li>
     * </ul>
     *
     * @param ou contains org unit name {@link org.apache.directory.fortress.core.model.OrgUnit#name}.  The search val contains the full name of matching ou in OS-P data set.
     * @return List of type PermObj.  Fortress permissions are object->operation mappings.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          thrown in the event of system error.
     */
    @Override
    public List<PermObj> findPermObjs(OrgUnit ou)
        throws SecurityException
    {
        String methodName = "findPermObjs";
        assertContext(CLS_NM, methodName, ou, GlobalErrIds.ORG_NULL_PERM);
        checkAccess(CLS_NM, methodName);
        // pass a "false" which places no restrictions on how many records server returns.
        return permP.search(ou, false);
    }

    /**
     * Method reads Role entity from the role container in directory.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.model.Role#name} - contains the name to use for the Role to read.</li>
     * </ul>
     *
     * @param role contains role name, {@link org.apache.directory.fortress.core.model.Role#name}, to be read.
     * @return Role entity that corresponds with role name.
     * @throws SecurityException will be thrown if role not found or system error occurs.
     */
    @Override
    public Role readRole(Role role)
        throws SecurityException
    {
        String methodName = "readRole";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        VUtil.assertNotNullOrEmpty(role.getName(), GlobalErrIds.ROLE_NM_NULL, CLS_NM + "." + methodName);
        checkAccess(CLS_NM, methodName);
        return roleP.read(role);
    }

    /**
     * Method will return a list of type Role matching all or part of Role name, {@link Role#name}.
     *
     * @param searchVal contains all or some of the chars corresponding to role entities stored in directory.
     * @return List of type Role containing role entities that match the search criteria.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of system error.
     */
    @Override
    public List<Role> findRoles(String searchVal)
        throws SecurityException
    {
        String methodName = "findRoles";
        VUtil.assertNotNull(searchVal, GlobalErrIds.ROLE_NM_NULL, CLS_NM + "." + methodName);
        checkAccess(CLS_NM, methodName);
        Role role = new Role(searchVal);
        role.setContextId(this.contextId);
        return roleP.search(role);
    }

    /**
     * Method returns a list of roles of type String.  This method can be limited by integer value that indicates max
     * number of records that may be contained in the result set.  This number can further limit global default but can
     * not increase the max.  This method is called by the Websphere Realm impl.
     *
     * @param searchVal contains all or some leading chars that correspond to roles stored in the role container in the directory.
     * @param limit     integer value specifies the max records that may be returned in the result set.
     * @return List of type String containing names of the role entities that match the inbound search criteria.
     * @throws SecurityException in the event of system error.
     */
    @Override
    public List<String> findRoles(String searchVal, int limit)
        throws SecurityException
    {
        String methodName = "findRoles";
        VUtil.assertNotNull(searchVal, GlobalErrIds.ROLE_NM_NULL, CLS_NM + "." + methodName);
        checkAccess(CLS_NM, methodName);
        Role role = new Role(searchVal);
        role.setContextId(this.contextId);
        return roleP.search(role, limit);
    }

    /**
     * Method returns matching User entity that is contained within the people container in the directory.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.model.User#userId} - contains the userId associated with the User object targeted for read.</li>
     * </ul>
     *
     * @param user entity contains a value {@link org.apache.directory.fortress.core.model.User#userId} that matches record in the directory.  userId is globally unique in
     *             people container.
     * @return entity containing matching user data.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          if record not found or system error occurs.
     */
    @Override
    public final User readUser(User user)
        throws SecurityException
    {
        String methodName = "readUser";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        VUtil.assertNotNullOrEmpty(user.getUserId(), GlobalErrIds.USER_ID_NULL, CLS_NM + "." + methodName);
        checkAccess(CLS_NM, methodName);
        return userP.read(user, true);
    }

    /**
     * Return a list of type User of all users in the people container that match all or part of the {@link User#userId} field passed in User entity.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.model.User#userId} - contains all or some leading chars that match userId(s) stored in the directory.</li>
     * </ul>
     *
     * @param user contains all or some leading chars that match userIds stored in the directory.
     * @return List of type User.
     * @throws SecurityException In the event of system error.
     */
    @Override
    public final List<User> findUsers(User user)
        throws SecurityException
    {
        String methodName = "findUsers";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        checkAccess(CLS_NM, methodName);
        return userP.search(user);
    }

    /**
     * Return a list of type User of all users in the people container that match the name field passed in OrgUnit entity.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link OrgUnit#name} - contains one or more characters of org unit associated with existing object(s) being targeted</li>
     * </ul>
     *
     * @param ou contains name of User OU, {@link OrgUnit#name} that match ou attribute associated with User entity in the directory.
     * @return List of type User.
     * @throws SecurityException In the event of system error.
     */
    @Override
    public List<User> findUsers(OrgUnit ou)
        throws SecurityException
    {
        String methodName = "findUsers";
        assertContext(CLS_NM, methodName, ou, GlobalErrIds.ORG_NULL_USER);
        checkAccess(CLS_NM, methodName);
        // pass a "false" which places no restrictions on how many records server returns.
        return userP.search(ou, false);
    }

    /**
     * Return a list of type String of all users in the people container that match the userId field passed in User entity.
     * This method is used by the Websphere realm component.  The max number of returned users may be set by the integer limit arg.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - contains the userId associated with the User object targeted for read.</li>
     * <li>limit - max number of objects to return.</li>
     * </ul>
     *
     * @param user  contains all or some leading chars that correspond to users stored in the directory.
     * @param limit integer value sets the max returned records.
     * @return List of type String containing matching userIds.
     * @throws SecurityException in the event of system error.
     */
    @Override
    public final List<String> findUsers(User user, int limit)
        throws SecurityException
    {
        String methodName = "findUsers";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        checkAccess(CLS_NM, methodName);
        return userP.search(user, limit);
    }

    /**
     * This function returns the set of users assigned to a given role. The function is valid if and
     * only if the role is a member of the ROLES data set.
     * The max number of users returned is constrained by limit argument.
     * This method is used by the Websphere realm component.  This method does NOT use hierarchical impl.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role targeted for search.</li>
     * <li>limit - max number of objects to return.</li>
     * </ul>
     *
     * @param role  Contains {@link Role#name} of Role entity assigned to user.
     * @param limit integer value sets the max returned records.
     * @return List of type String containing userIds assigned to a particular role.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of data validation or system error.
     */
    @Override
    public List<String> assignedUsers(Role role, int limit)
        throws SecurityException
    {
        String methodName = "assignedUsers";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess(CLS_NM, methodName);
        Role entity = roleP.read(role);
        // this one retrieves from the role itself.
        List<String> users = entity.getOccupants();
        if (users != null && users.size() > limit)
        {
            users = users.subList(0, limit);
        }
        // No users found for this role.
        // return empty list to caller:
        else if (users == null)
        {
            users = new ArrayList<>();
        }
        return users;
        // this one does a search across all users:
        //return userP.getAuthorizedUsers(role, limit);
    }

    /**
     * This method returns the data set of all users who are assigned the given role.  This searches the User data set for
     * Role relationship.  This method does NOT search for hierarchical RBAC Roles relationships.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role contains the role name, {@link Role#name} used to search the User data set.
     * @return List of type User containing the users assigned data.
     * @throws SecurityException If system error occurs.
     */
    @Override
    public List<User> assignedUsers(Role role)
        throws SecurityException
    {
        String methodName = "assignedUsers";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess(CLS_NM, methodName);
        return userP.getAssignedUsers(role);
    }

    /**
     * This function returns the set of roles assigned to a given user. The function is valid if and
     * only if the user is a member of the USERS data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - contains the userId associated with the User object targeted for search.</li>
     * </ul>
     *
     * @param user contains {@link User#userId} matching User entity targeted in the directory.
     * @return List of type UserRole containing the Roles assigned to User.
     * @throws SecurityException If user not found or system error occurs.
     */
    @Override
    public List<UserRole> assignedRoles(User user)
        throws SecurityException
    {
        String methodName = "assignedRoles";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        checkAccess(CLS_NM, methodName);
        User ue = userP.read(user, true);
        return ue.getRoles();
    }

    /**
     * This function returns the set of roles assigned to a given user. The function is valid if and
     * only if the user is a member of the USERS data set.
     *
     * @param userId matches userId stored in the directory.
     * @return List of type String containing the role names of all roles assigned to user.
     * @throws SecurityException If user not found or system error occurs.
     */
    @Override
    public List<String> assignedRoles(String userId)
        throws SecurityException
    {
        String methodName = "assignedRoles";
        VUtil.assertNotNullOrEmpty(userId, GlobalErrIds.USER_NULL, CLS_NM + "." + methodName);
        checkAccess(CLS_NM, methodName);
        User user = new User(userId);
        user.setContextId(this.contextId);
        return userP.getAssignedRoles(user);
    }

    /**
     * This function returns the set of users authorized to a given role, i.e., the users that are assigned to a role that
     * inherits the given role. The function is valid if and only if the given role is a member of the ROLES data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role Contains role name, {@link Role#name} of Role entity assigned to User.
     * @return List of type User containing all user's that having matching role assignment.
     * @throws SecurityException In the event the role is not present in directory or system error occurs.
     */
    @Override
    public List<User> authorizedUsers(Role role)
        throws SecurityException
    {
        String methodName = "authorizedUsers";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess(CLS_NM, methodName);
        return userP.getAuthorizedUsers(role);
    }

    /**
     * This function returns the set of roles authorized for a given user. The function is valid if
     * and only if the user is a member of the USERS data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - contains the userId associated with the User object targeted for search.</li>
     * </ul>
     *
     * @param user contains the {@link User#userId} matching User entity stored in the directory.
     * @return Set of type String containing the roles assigned and roles inherited.
     * @throws SecurityException If user not found or system error occurs.
     */
    @Override
    public Set<String> authorizedRoles(User user)
        throws SecurityException
    {
        String methodName = "authorizedRoles";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        checkAccess(CLS_NM, methodName);
        User ue = userP.read(user, true);
        List<UserRole> roles = ue.getRoles();
        Set<String> iRoles = null;
        if (ObjUtil.isNotNullOrEmpty(roles))
        {
            iRoles = RoleUtil.getInheritedRoles( roles, this.contextId );
        }
        return iRoles;
    }

    /**
     * This function returns the set of all permissions (op, obj), granted to or inherited by a
     * given role. The function is valid if and only if the role is a member of the ROLES data
     * set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role contains role name, {@link Role#name} of Role entity Permission is granted to.
     * @return List of type Permission that contains all perms granted to a role.
     * @throws SecurityException In the event system error occurs.
     */
    @Override
    public List<Permission> rolePermissions(Role role)
        throws SecurityException
    {
        String methodName = "rolePermissions";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess(CLS_NM, methodName);
        return permP.search(role);
    }

    /**
     * This function returns the set of permissions a given user gets through his/her authorized
     * roles. The function is valid if and only if the user is a member of the USERS data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - contains the userId associated with the User object targeted for search.</li>
     * </ul>
     *
     * @param user contains the {@link User#userId} of User targeted for search.
     * @return List of type Permission containing matching permission entities.
     * @throws org.apache.directory.fortress.core.SecurityException
     *
     */
    @Override
    public List<Permission> userPermissions(User user)
        throws SecurityException
    {
        String methodName = "userPermissions";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        checkAccess(CLS_NM, methodName);
        user = readUser(user);
        user.setContextId(this.contextId);
        return permP.search(user);
    }

    /**
     * Return a list of type String of all roles that have granted a particular permission.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objName} - contains the name of existing object being targeted</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param perm must contain the object, {@link org.apache.directory.fortress.core.model.Permission#objName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return List of type string containing the role names that have the matching perm granted.
     * @throws SecurityException in the event permission not found or system error occurs.
     */
    @Override
    public List<String> permissionRoles(Permission perm)
        throws SecurityException
    {
        String methodName = "permissionRoles";
        assertContext(CLS_NM, methodName, perm, GlobalErrIds.PERM_OBJECT_NULL);
        checkAccess(CLS_NM, methodName);
        Permission pe = permP.read(perm);
        List<String> retVals;
        if(pe != null && ObjUtil.isNotNullOrEmpty(pe.getRoles()))
        {
            retVals = new ArrayList<>(pe.getRoles());
        }
        else
        {
            retVals =  new ArrayList<>();
        }
        return retVals;
    }

    /**
     * Return all role names that have been authorized for a given permission.  This will process role hierarchies to determine set of all Roles who have access to a given permission.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.model.Permission#objName} - contains the name of existing object being targeted</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return Set of type String containing all roles names that have been granted a particular permission.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of validation or system error.
     */
    @Override
    public Set<String> authorizedPermissionRoles(Permission perm)
        throws SecurityException
    {
        Set<String> authorizedRoles;
        String methodName = "authorizedPermissionRoles";
        assertContext(CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL);
        checkAccess(CLS_NM, methodName);
        // Pull the permission from ldap:
        Permission pe = permP.read(perm);

        // Get all roles that this permission is authorized for:
        authorizedRoles = authorizeRoles(pe.getRoles());
        return authorizedRoles;
    }

    /**
     * Return all userIds that have been granted (directly) a particular permission.  This will not consider assigned or authorized Roles.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objName} - contains the name of existing object being targeted</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return List of type String containing all userIds that have been granted a particular permission.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of validation or system error.
     */
    @Override
    public List<String> permissionUsers(Permission perm)
        throws SecurityException
    {
        String methodName = "permissionUsers";
        assertContext(CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL);
        checkAccess(CLS_NM, methodName);
        Permission pe = permP.read(perm);
        List<String> retVals;
        if(pe != null && ObjUtil.isNotNullOrEmpty( pe.getUsers() ))
        {
            retVals = new ArrayList<>(pe.getUsers());
        }
        else
        {
            retVals =  new ArrayList<>();
        }
        return retVals;
    }

    /**
     * Return all userIds that have been authorized for a given permission.  This will process role hierarchies to determine set of all Users who have access to a given permission.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objName} - contains the name of existing object being targeted</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return Set of type String containing all userIds that have been granted a particular permission.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of validation or system error.
     */
    @Override
    public Set<String> authorizedPermissionUsers(Permission perm)
        throws SecurityException
    {
        Set<String> authorizedUsers = null;
        String methodName = "authorizedPermissionUsers";
        assertContext(CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL);
        checkAccess(CLS_NM, methodName);
        // Pull the permission from ldap:
        Permission pe = permP.read(perm);

        // Get all roles that this permission is authorized for:
        Set<String> authorizedRoles = authorizeRoles(pe.getRoles());
        if (authorizedRoles != null)
        {
            // Pull the set of users assigned to descendant or assigned roles from ldap:
            authorizedUsers = userP.getAssignedUsers(authorizedRoles, this.contextId);
        }
        // Now add any users who have been directly assigned to this permission entity:
        Set<String> assignedUsers = pe.getUsers();
        if (assignedUsers != null)
        {
            // It is possible this dataset has not yet been instantiated (if perm has no assigned roles):
            if (authorizedUsers == null)
            {
                authorizedUsers = new HashSet<>();
            }
            authorizedUsers.addAll(assignedUsers);
        }
        // The returned list includes all assigned users plus any users assigned via authorized roles.
        return authorizedUsers;
    }

    /**
     * @param assignedRoles
     * @return Set contains both assigned and descendant role names
     * @throws SecurityException
     */
    private Set<String> authorizeRoles(Set<String> assignedRoles)
    {
        Set<String> authorizedRoles = null;
        if (assignedRoles != null)
        {
            // Get the descendant roles of all assigned roles from jgrapht hierarchical roles data set:
            authorizedRoles = RoleUtil.getDescendantRoles(assignedRoles, this.contextId);
        }
        return authorizedRoles;
    }

    /**
     * This function returns the list of all SSD role sets that have a particular Role as member or Role's
     * parent as a member.  If the Role parameter is left blank, function will return all SSD role sets.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role Will contain the role name, {@link Role#name}, for targeted SSD set or null to return all
     * @return List containing all matching SSD's.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of data or system error.
     */
    @Override
    public List<SDSet> ssdRoleSets(Role role)
        throws SecurityException
    {
        String methodName = "ssdRoleSets";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess(CLS_NM, methodName);
        return ssdP.search(role, SDSet.SDType.STATIC);
    }

    /**
     * This function returns the list of SSDs that match a given ssd name value.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param ssd contains the name for the SSD set targeted, {@link SDSet#name}.
     * @return List containing all SSDSets that match a given SSDSet name.
     * @throws SecurityException in the event of data or system error.
     */
    public List<SDSet> ssdSets(SDSet ssd)
        throws SecurityException
    {
        String methodName = "ssdSets";
        ssd.setType(SDSet.SDType.STATIC);
        assertContext(CLS_NM, methodName, ssd, GlobalErrIds.SSD_NULL);
        checkAccess(CLS_NM, methodName);
        return ssdP.search(ssd);
    }

    /**
     * This function returns the SSD data set that matches a particular set name.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param set Will contain the name for existing SSD data set
     * @return SDSet containing all attributes from matching SSD name.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of data or system error.
     */
    @Override
    public SDSet ssdRoleSet(SDSet set)
        throws SecurityException
    {
        String methodName = "ssdRoleSet";
        assertContext(CLS_NM, methodName, set, GlobalErrIds.SSD_NULL);
        checkAccess(CLS_NM, methodName);
        set.setType(SDSet.SDType.STATIC);
        return ssdP.read(set);
    }

    /**
     * This function returns the set of roles of a SSD role set. The function is valid if and only if the
     * role set exists.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param ssd contains the name for the SSD set targeted.
     * @return Map containing all Roles that are members of SSD data set.
     * @throws SecurityException in the event of data or system error.
     */
    @Override
    public Set<String> ssdRoleSetRoles(SDSet ssd)
        throws SecurityException
    {
        String methodName = "ssdRoleSetRoles";
        assertContext(CLS_NM, methodName, ssd, GlobalErrIds.SSD_NULL);
        checkAccess(CLS_NM, methodName);
        ssd.setType(SDSet.SDType.STATIC);
        SDSet se = ssdP.read(ssd);
        return se.getMembers();
    }

    /**
     * This function returns the cardinality associated with a SSD role set. The function is valid if and only if the
     * role set exists.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param ssd contains the name of the SSD set targeted, {@link SDSet#name}.
     * @return int value containing cardinality of SSD set.
     * @throws SecurityException in the event of data or system error.
     */
    @Override
    public int ssdRoleSetCardinality(SDSet ssd)
        throws SecurityException
    {
        String methodName = "ssdRoleSetCardinality";
        assertContext(CLS_NM, methodName, ssd, GlobalErrIds.SSD_NULL);
        checkAccess(CLS_NM, methodName);
        SDSet se = ssdP.read(ssd);
        return se.getCardinality();
    }

    /**
     * This function returns the list of all dSD role sets that have a particular Role as member or Role's
     * parent as a member.  If the Role parameter is left blank, function will return all dSD role sets.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role Will contain the role name, {@link Role#name}, for targeted dSD set or null to return all
     * @return List containing all matching dSD's.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of data or system error.
     */
    @Override
    public List<SDSet> dsdRoleSets(Role role)
        throws SecurityException
    {
        String methodName = "dsdRoleSets";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess(CLS_NM, methodName);
        return ssdP.search(role, SDSet.SDType.DYNAMIC);
    }

    /**
     * This function returns the DSD data set that matches a particular set name.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param set Will contain the name for existing DSD data set, {@link SDSet#name}.
     * @return SDSet containing all attributes from matching DSD name.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of data or system error.
     */
    @Override
    public SDSet dsdRoleSet(SDSet set)
        throws SecurityException
    {
        String methodName = "dsdRoleSet";
        assertContext(CLS_NM, methodName, set, GlobalErrIds.DSD_NULL);
        checkAccess(CLS_NM, methodName);
        set.setType(SDSet.SDType.DYNAMIC);
        return ssdP.read(set);
    }

    /**
     * This function returns the list of DSDs that match a given dsd name value.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param ssd contains the name for the SSD set targeted, {@link SDSet#name}.
     * @return List containing all DSDSets that match a given DSDSet name.
     * @throws SecurityException in the event of data or system error.
     */
    public List<SDSet> dsdSets(SDSet ssd)
        throws SecurityException
    {
        String methodName = "dsdSets";
        ssd.setType(SDSet.SDType.DYNAMIC);
        assertContext(CLS_NM, methodName, ssd, GlobalErrIds.DSD_NULL);
        checkAccess(CLS_NM, methodName);
        return ssdP.search(ssd);
    }

    /**
     * This function returns the set of roles of a DSD role set. The function is valid if and only if the
     * role set exists.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param dsd contains the name for the DSD set targeted, {@link SDSet#name}.
     * @return List containing all Roles that are members of DSD data set.
     * @throws SecurityException in the event of data or system error.
     */
    @Override
    public Set<String> dsdRoleSetRoles(SDSet dsd)
        throws SecurityException
    {
        String methodName = "dsdRoleSetRoles";
        assertContext(CLS_NM, methodName, dsd, GlobalErrIds.DSD_NULL);
        checkAccess(CLS_NM, methodName);
        dsd.setType(SDSet.SDType.DYNAMIC);
        SDSet se = ssdP.read(dsd);
        return se.getMembers();
    }

    /**
     * This function returns the cardinality associated with a DSD role set. The function is valid if and only if the
     * role set exists.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param dsd contains the name of the DSD set targeted, {@link SDSet#name}.
     * @return int value containing cardinality of DSD set.
     * @throws SecurityException in the event of data or system error.
     */
    @Override
    public int dsdRoleSetCardinality(SDSet dsd)
        throws SecurityException
    {
        String methodName = "dsdRoleSetCardinality";
        assertContext(CLS_NM, methodName, dsd, GlobalErrIds.DSD_NULL);
        checkAccess(CLS_NM, methodName);
        SDSet se = ssdP.read(dsd);
        return se.getCardinality();
    }
}