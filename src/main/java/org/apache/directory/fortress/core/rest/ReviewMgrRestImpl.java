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
package org.apache.directory.fortress.core.rest;

import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.ReviewMgr;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.FortRequest;
import org.apache.directory.fortress.core.model.FortResponse;
import org.apache.directory.fortress.core.impl.Manageable;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.SDSet;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.util.VUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class performs administrative review functions on already provisioned Fortress RBAC entities using HTTP access to En Masse REST server.
 * These APIs map directly to similar named APIs specified by ANSI and NIST RBAC models.
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
 * This class is thread safe.
 * <p/>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class ReviewMgrRestImpl extends Manageable implements ReviewMgr
{
    private static final String CLS_NM = ReviewMgrRestImpl.class.getName();

    /**
     * This method returns a matching permission entity to caller.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objName} - contains the name of existing object being targeted</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param permission must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return Permission entity that is loaded with data.
     * @throws SecurityException if permission not found or system error occurs.
     */
    @Override
    public Permission readPermission(Permission permission)
        throws SecurityException
    {
        VUtil.assertNotNull(permission, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".readPermission");
        Permission retPerm;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(permission);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.PERM_READ);
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
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link PermObj#objName} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param permObj entity contains the {@link PermObj#objName} of target record.
     * @return PermObj loaded with perm object data.
     * @throws SecurityException is thrown if object not found or system error.
     */
    @Override
    public PermObj readPermObj(PermObj permObj)
        throws SecurityException
    {
        VUtil.assertNotNull(permObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".readPermObj");
        PermObj retObj;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(permObj);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.OBJ_READ);
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
        VUtil.assertNotNull(permission, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".findPermissions");
        List<Permission> retPerms;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(permission);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.PERM_SEARCH);
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
     * Method returns a list of type Permission that match any part of either {@link Permission#objName} or {@link Permission#opName} search strings.
     * This method differs from findPermissions in that any permission that matches any part of the perm obj or any part of the perm op will be returned in result set (uses substring string matching).
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
    public List<Permission> findAnyPermissions(Permission permission)
        throws SecurityException
    {
        VUtil.assertNotNull(permission, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".findAnyPermissions");
        List<Permission> retPerms;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(permission);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.PERM_SEARCH_ANY);
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
        VUtil.assertNotNull(permObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".findPermObjs");
        List<PermObj> retObjs;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(permObj);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.OBJ_SEARCH);
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
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link OrgUnit#name} - contains one or more characters of org unit associated with existing object being targeted</li>
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
        VUtil.assertNotNull(ou, GlobalErrIds.ORG_NULL_PERM, CLS_NM + ".findPermObjs");
        List<PermObj> retObjs;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        PermObj inObj = new PermObj();
        inObj.setOu(ou.getName());
        request.setEntity(inObj);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.OBJ_SEARCH);
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
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role to read.</li>
     * </ul>
     *
     * @param role contains role name, {@link Role#name}, to be read.
     * @return Role entity that corresponds with role name.
     * @throws SecurityException will be thrown if role not found or system error occurs.
     */
    @Override
    public Role readRole(Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".readRole");
        Role retRole;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ROLE_READ);
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
     * @param searchVal contains all or some of the chars corresponding to role entities stored in directory.
     * @return List of type Role containing role entities that match the search criteria.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of system error.
     */
    @Override
    public List<Role> findRoles(String searchVal)
        throws SecurityException
    {
        VUtil.assertNotNull(searchVal, GlobalErrIds.ROLE_NM_NULL, CLS_NM + ".findRoles");
        List<Role> retRoles;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setValue(searchVal);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ROLE_SEARCH);
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
     * @return List of type Role containing role entities that match the search criteria.
     * @throws SecurityException in the event of system error.
     */
    @Override
    public List<String> findRoles(String searchVal, int limit)
        throws SecurityException
    {
        VUtil.assertNotNull(searchVal, GlobalErrIds.ROLE_NM_NULL, CLS_NM + ".findRoles");
        List<String> retRoles;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setValue(searchVal);
        request.setLimit(limit);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ROLE_SEARCH);
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
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - contains the userId associated with the User object targeted for read.</li>
     * </ul>
     *
     * @param user entity contains a value {@link User#userId} that matches record in the directory.  userId is globally unique in
     *             people container.
     * @return entity containing matching user data.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          if record not found or system error occurs.
     */
    @Override
    public final User readUser(User user)
        throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".readUser");
        User retUser;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.USER_READ);
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
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - contains all or some leading chars that match userId(s) stored in the directory.</li>
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
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".findUsers");
        List<User> retUsers;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.USER_SEARCH);
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
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link OrgUnit#name} - contains one or more characters of org unit associated with existing object(s) being targeted</li>
     * </ul>
     *
     * @param ou contains name of User OU, {@link org.apache.directory.fortress.core.model.OrgUnit#name} that match ou attribute associated with User entity in the directory.
     * @return List of type User.
     * @throws SecurityException In the event of system error.
     */
    @Override
    public List<User> findUsers(OrgUnit ou)
        throws SecurityException
    {
        VUtil.assertNotNull(ou, GlobalErrIds.ORG_NULL_USER, CLS_NM + ".findUsers");
        List<User> retUsers;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        User inUser = new User();
        inUser.setOu( ou.getName() );
        request.setEntity(inUser);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.USER_SEARCH);
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
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".findUsers");
        List<String> retUsers;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setLimit( limit );
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.USER_SEARCH);
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
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".assignedUsers");
        List<String> retUsers;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setLimit(limit);
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.USER_ASGNED);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retUsers = response.getValues();
            // do not return a null list to the caller:
            if (retUsers == null)
            {
                retUsers = new ArrayList<>();
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
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".assignedUsers");
        List<User> retUsers;
        FortRequest request = new FortRequest();
        request.setContextId( this.contextId );
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.USER_ASGNED);
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
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".assignedRoles");
        List<UserRole> retUserRoles;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ROLE_ASGNED);
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
    @Override
    public List<String> assignedRoles(String userId)
        throws SecurityException
    {
        VUtil.assertNotNullOrEmpty( userId, GlobalErrIds.USER_NULL, CLS_NM + ".assignedRoles" );
        List<String> retUserRoles;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setValue( userId );
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ROLE_ASGNED);
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
        VUtil.assertNotNull( role, GlobalErrIds.ROLE_NULL, CLS_NM + ".authorizedUsers" );
        List<User> retUsers;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity( role );
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ROLE_AUTHZED);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retUsers = response.getEntities();
            if (retUsers == null)
            {
                retUsers = new ArrayList<>();
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
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".authorizedRoles");
        Set<String> retRoleNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.USER_AUTHZED);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Set<String> tempNames = response.getValueSet();
            // This is done to use a case insensitive TreeSet for returned names.
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
        return rolePermissions( role, false );
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
     * @param noInheritance if true will NOT include inherited roles in the search.
     * @return List of type Permission that contains all perms granted to a role.
     * @throws SecurityException In the event system error occurs.
     */
    @Override
    public List<Permission> rolePermissions( Role role, boolean noInheritance )
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".rolePermissions");
        List<Permission> retPerms;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(role);
        request.setValue( String.valueOf( noInheritance ) );
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ROLE_PERMS);
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
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".userPermissions");
        List<Permission> retPerms;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(user);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.USER_PERMS);
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
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objName} - contains the name of existing object being targeted</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName}, and optionally object id of targeted permission entity.
     * @return List of type string containing the role names that have the matching perm granted.
     * @throws SecurityException in the event permission not found or system error occurs.
     */
    @Override
    public List<String> permissionRoles(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".permissionRoles");
        List<String> retRoleNames;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(perm);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.PERM_ROLES);
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
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objName} - contains the name of existing object being targeted</li>
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
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".authorizedPermissionRoles");
        Set<String> retRoleNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(perm);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.PERM_ROLES_AUTHZED);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Set<String> tempNames = response.getValueSet();
            // This is done to use a case insensitive TreeSet for returned names.
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
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".permissionUsers");
        List<String> retUsers;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(perm);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.PERM_USERS);
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
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".authorizedPermissionUsers");
        Set<String> retUserIds = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(perm);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.PERM_USERS_AUTHZED);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Set<String> tempNames = response.getValueSet();
            // This is done to use a case insensitive TreeSet for returned names.
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
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".ssdRoleSets");
        List<SDSet> retSsdRoleSets;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.SSD_ROLE_SETS);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSsdRoleSets = response.getEntities();
            if (retSsdRoleSets == null)
            {
                retSsdRoleSets = new ArrayList<>();
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
        VUtil.assertNotNull(set, GlobalErrIds.SSD_NULL, CLS_NM + ".ssdRoleSet");
        SDSet retSet;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(set);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.SSD_READ);
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
        VUtil.assertNotNull(ssd, GlobalErrIds.ROLE_NULL, CLS_NM + ".ssdSets");
         List<SDSet> retSsdSets;
         FortRequest request = new FortRequest();
         request.setContextId(this.contextId);
         request.setEntity(ssd);
         if (this.adminSess != null)
         {
             request.setSession(adminSess);
         }
         String szRequest = RestUtils.marshal(request);
         String szResponse = RestUtils.post(szRequest, HttpIds.SSD_SETS);
         FortResponse response = RestUtils.unmarshall(szResponse);
         if (response.getErrorCode() == 0)
         {
             retSsdSets = response.getEntities();
             if (retSsdSets == null)
             {
                 retSsdSets = new ArrayList<>();
             }
         }
         else
         {
             throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
         }
         return retSsdSets;
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
        VUtil.assertNotNull(ssd, GlobalErrIds.SSD_NULL, CLS_NM + ".ssdRoleSetRoles");
        Set<String> retRoleNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(ssd);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.SSD_ROLES);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Set<String> tempNames = response.getValueSet();
            // This is done to use a case insensitive TreeSet for returned names.
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
        VUtil.assertNotNull(ssd, GlobalErrIds.SSD_NULL, CLS_NM + ".ssdRoleSetCardinality");
        SDSet retSet;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(ssd);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.SSD_CARD);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSet = (SDSet) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSet.getCardinality();
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
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".dsdRoleSets");
        List<SDSet> retDsdRoleSets;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.DSD_ROLE_SETS);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retDsdRoleSets = response.getEntities();
            if (retDsdRoleSets == null)
            {
                retDsdRoleSets = new ArrayList<>();
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
        VUtil.assertNotNull(set, GlobalErrIds.DSD_NULL, CLS_NM + ".dsdRoleSet");
        SDSet retSet;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(set);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.DSD_READ);
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
     * This function returns the list of DSDs that match a given dsd name value.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param dsd contains the name for the DSD set targeted, {@link SDSet#name}.
     * @return List containing all DSDSets that match a given DSDSet name.
     * @throws SecurityException in the event of data or system error.
     */
    public List<SDSet> dsdSets(SDSet dsd)
        throws SecurityException
    {
        VUtil.assertNotNull(dsd, GlobalErrIds.ROLE_NULL, CLS_NM + ".dsdSets");
         List<SDSet> retDsdSets;
         FortRequest request = new FortRequest();
         request.setContextId(this.contextId);
         request.setEntity(dsd);
         if (this.adminSess != null)
         {
             request.setSession(adminSess);
         }
         String szRequest = RestUtils.marshal(request);
         String szResponse = RestUtils.post(szRequest, HttpIds.DSD_SETS);
         FortResponse response = RestUtils.unmarshall(szResponse);
         if (response.getErrorCode() == 0)
         {
             retDsdSets = response.getEntities();
             if (retDsdSets == null)
             {
                 retDsdSets = new ArrayList<>();
             }
         }
         else
         {
             throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
         }
         return retDsdSets;
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
        VUtil.assertNotNull(dsd, GlobalErrIds.SSD_NULL, CLS_NM + ".dsdRoleSetRoles");
        Set<String> retRoleNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(dsd);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.DSD_ROLES);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Set<String> tempNames = response.getValueSet();
            // This is done to use a case insensitive TreeSet for returned names.
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
        VUtil.assertNotNull(dsd, GlobalErrIds.DSD_NULL, CLS_NM + ".dsdRoleSetCardinality");
        SDSet retSet;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(dsd);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.DSD_CARD);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSet = (SDSet) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSet.getCardinality();
    }
}