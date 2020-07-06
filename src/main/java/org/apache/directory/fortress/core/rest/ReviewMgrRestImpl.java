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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.ReviewMgr;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.Manageable;
import org.apache.directory.fortress.core.model.FortRequest;
import org.apache.directory.fortress.core.model.FortResponse;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.PermissionAttributeSet;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.RoleConstraint;
import org.apache.directory.fortress.core.model.RoleConstraint.RCType;
import org.apache.directory.fortress.core.model.SDSet;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * This class performs administrative review functions on already provisioned Fortress RBAC entities using HTTP access to Fortress Rest server.
 * These APIs map directly to similar named APIs specified by ANSI and NIST RBAC models.
 * Many of the java doc function descriptions found below were taken directly from ANSI INCITS 359-2004.
 * The RBAC Functional specification describes administrative operations for the creation
 * and maintenance of RBAC element sets and relations; administrative review functions for
 * performing administrative queries; and system functions for creating and managing
 * RBAC attributes on user sessions and making access control decisions.
 * <p>
 * <hr>
 * <h4>RBAC0 - Core</h4>
 * Many-to-many relationship between Users, Roles and Permissions. Selective role activation into sessions.  API to add, update, delete identity data and perform identity and access control decisions during runtime operations.
 * <p>
 * <img src="../doc-files/RbacCore.png" alt="">
 * <hr>
 * <h4>RBAC1 - General Hierarchical Roles</h4>
 * Simplifies role engineering tasks using inheritance of one or more parent roles.
 * <p>
 * <img src="../doc-files/RbacHier.png" alt="">
 * <hr>
 * <h4>RBAC2 - Static Separation of Duty (SSD) Relations</h4>
 * Enforce mutual membership exclusions across role assignments.  Facilitate dual control policies by restricting which roles may be assigned to users in combination.  SSD provide added granularity for authorization limits which help enterprises meet strict compliance regulations.
 * <p>
 * <img src="../doc-files/RbacSSD.png" alt="">
 * <hr>
 * <h4>RBAC3 - Dynamic Separation of Duty (DSD) Relations</h4>
 * Control allowed role combinations to be activated within an RBAC session.  DSD policies fine tune role policies that facilitate authorization dual control and two man policy restrictions during runtime security checks.
 * <p>
 * <img src="../doc-files/RbacDSD.png" alt="">
 * <hr>
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class ReviewMgrRestImpl extends Manageable implements ReviewMgr
{
    private static final String CLS_NM = ReviewMgrRestImpl.class.getName();


    /**
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.PERM_READ);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.OBJ_READ);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.PERM_SEARCH);
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
     * {@inheritDoc}
     */
    @Override
    public List<Permission> findPermsByObj(PermObj permObj)
        throws SecurityException
    {
        VUtil.assertNotNull(permObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".findPermsByObj");
        List<Permission> retPerms;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(permObj);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.PERM_OBJ_SEARCH);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.PERM_SEARCH_ANY);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.OBJ_SEARCH);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.OBJ_SEARCH);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ROLE_READ);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ROLE_SEARCH);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ROLE_SEARCH);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.USER_READ);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.USER_SEARCH);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.USER_SEARCH);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.USER_SEARCH);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.USER_ASGNED);
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
     * {@inheritDoc}
     */
    @Override
    public List<User> assignedUsers(Role role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".assignedUsers");
        List<User> retUsers;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity(role);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.USER_ASGNED);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ROLE_ASGNED);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ROLE_ASGNED);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.USER_AUTHZED);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ROLE_AUTHZED);
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
     * {@inheritDoc}
     */
    @Override
    public List<Permission> rolePermissions(Role role)
        throws SecurityException
    {
        return rolePermissions( role, false );
    }


    /**
     * {@inheritDoc}
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
        request.setIsFlag( noInheritance );
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ROLE_PERMS);
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
     * {@inheritDoc}
     */
    @Override
    public List<PermissionAttributeSet> rolePermissionAttributeSets(Role role, boolean noInhertiance)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".rolePermissionAttributeSets");
        List<PermissionAttributeSet> retAttrSets;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(role);
        request.setIsFlag( noInhertiance );
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ROLE_PERM_ATTR_SETS);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retAttrSets = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retAttrSets;
    }
    
    /**
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.USER_PERMS);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.PERM_ROLES);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.PERM_ROLES_AUTHZED);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.PERM_USERS);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.PERM_USERS_AUTHZED);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.SSD_ROLE_SETS);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.SSD_READ);
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
     * {@inheritDoc}
     */
    public List<SDSet> ssdSets(SDSet ssd)
        throws SecurityException
    {
        VUtil.assertNotNull(ssd, GlobalErrIds.ROLE_NULL, CLS_NM + ".ssdSets");
         List<SDSet> retSsdSets;
         FortRequest request = new FortRequest();
         request.setContextId(this.contextId);
         request.setEntity(ssd);
         String szRequest = RestUtils.marshal(request);
         String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.SSD_SETS);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.SSD_ROLES);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.SSD_CARD);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.DSD_ROLE_SETS);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.DSD_READ);
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
     * {@inheritDoc}
     */
    public List<SDSet> dsdSets(SDSet dsd)
        throws SecurityException
    {
        VUtil.assertNotNull(dsd, GlobalErrIds.ROLE_NULL, CLS_NM + ".dsdSets");
         List<SDSet> retDsdSets;
         FortRequest request = new FortRequest();
         request.setContextId(this.contextId);
         request.setEntity(dsd);
         String szRequest = RestUtils.marshal(request);
         String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.DSD_SETS);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.DSD_ROLES);
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
     * {@inheritDoc}
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
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.DSD_CARD);
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
     * {@inheritDoc}
     */
    @Override
    public PermissionAttributeSet readPermAttributeSet(    PermissionAttributeSet permAttributeSet )
        throws SecurityException
    {
        {
            VUtil.assertNotNull(permAttributeSet, GlobalErrIds.PERM_ATTRIBUTE_SET_NULL, CLS_NM + ".readPermAttributeSet");
            PermissionAttributeSet retPermSet;
            FortRequest request = new FortRequest();
            request.setContextId(this.contextId);
            request.setEntity(permAttributeSet);
            String szRequest = RestUtils.marshal(request);
            String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.PERM_READ_PERM_ATTRIBUTE_SET);
            FortResponse response = RestUtils.unmarshall(szResponse);
            if (response.getErrorCode() == 0)
            {
                retPermSet = (PermissionAttributeSet)response.getEntity();
            }
            else
            {
                throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
            }
            return retPermSet;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<RoleConstraint> findRoleConstraints( User user, Permission permission, RoleConstraint.RCType rcType )
            throws SecurityException
    {
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".findRoleConstraints");
        VUtil.assertNotNull(user, GlobalErrIds.PERM_NULL, CLS_NM + ".findRoleConstraints");

        List<RoleConstraint> retConstraints;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity( user );
        request.setEntity2( permission);
        request.setValue( rcType.toString() );
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ROLE_FIND_CONSTRAINTS);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retConstraints = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retConstraints;
    }


    @Override
    public List<User> assignedUsers( Role role, RoleConstraint roleConstraint ) throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".assignedUsers");
        VUtil.assertNotNull(roleConstraint, GlobalErrIds.ROLE_CONSTRAINT_NULL, CLS_NM + ".assignedUsers");
        List<User> users;
        FortRequest request = new FortRequest();
        request.setContextId( this.contextId );
        request.setEntity( role );
        request.setEntity2( roleConstraint );
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.USER_ASGNED_CONSTRAINTS);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            users = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return users;
    }


    @Override
    public List<UserRole> assignedUsers( Role role, RCType rcType, String key ) throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".assignedUsers");
        VUtil.assertNotNull(rcType, GlobalErrIds.ROLE_CONSTRAINT_TYPE_NULL, CLS_NM + ".assignedUsers");
        VUtil.assertNotNull(key, GlobalErrIds.ROLE_CONSTRAINT_KEY_NULL, CLS_NM + ".assignedUsers");
        List<UserRole> uRoles;
        FortRequest request = new FortRequest();
        request.setContextId( this.contextId );
        request.setEntity( role );
        RoleConstraint constraint = new RoleConstraint();
        constraint.setKey( key );
        constraint.setType( rcType );
        request.setEntity2( constraint );
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.USER_ASGNED_CONSTRAINTS_KEY);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            uRoles = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return uRoles;
    }
}