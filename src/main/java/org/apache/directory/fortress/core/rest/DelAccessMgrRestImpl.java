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

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.directory.fortress.core.DelAccessMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.FortRequest;
import org.apache.directory.fortress.core.model.FortResponse;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.RolePerm;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserAdminRole;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * This class implements the ARBAC02 DelAccessMgr interface for performing runtime delegated access control operations on objects that are provisioned Fortress ARBAC entities
 * using HTTP access to Fortress Rest server.  These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification describes delegated administrative
 * operations for the creation and maintenance of ARBAC element sets and relations.  Delegated administrative review functions for performing administrative queries
 * and system functions for creating and managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 *
 * This class also extends the RBAC AccessMgrImpl object which is used for performing runtime session creation and
 * access control decisions based on behalf of administrative user who is logged onto the system.  (See the AccessMgr javadoc for more info of how RBAC works).
 *
 * This class provides both sets of functionality as is necessary to fulfill runtime delegated administrative access control functionality
 * within RBAC provisioning systems.
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="../doc-files/ARbac.png" alt="">
 * <p>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without sacrificing regulations for accountability or traceability.
 * <p>
 * This class is thread safe.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DelAccessMgrRestImpl extends AccessMgrRestImpl implements DelAccessMgr
{
    private static final String CLS_NM = DelAccessMgrRestImpl.class.getName();


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canAssign(Session session, User user, Role role)
        throws SecurityException
    {
        String methodName = CLS_NM + ".canAssign";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, methodName);
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, methodName);
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, methodName);
        boolean result;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        UserRole uRole = new UserRole(user.getUserId(), role.getName());
        request.setSession(session);
        request.setEntity(uRole);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ADMIN_ASSIGN);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            result = response.getAuthorized();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canDeassign(Session session, User user, Role role)
        throws SecurityException
    {
        String methodName = CLS_NM + ".canDeassign";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, methodName);
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, methodName);
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, methodName);
        boolean result;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        UserRole uRole = new UserRole(user.getUserId(), role.getName());
        request.setSession(session);
        request.setEntity(uRole);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ADMIN_DEASSIGN);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            result = response.getAuthorized();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canGrant(Session session, Role role, Permission perm)
        throws SecurityException
    {
        String methodName = CLS_NM + "canGrant";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, methodName);
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OBJECT_NULL, methodName);
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, methodName);
        boolean result;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        RolePerm context = new RolePerm();
        context.setPerm(perm);
        context.setRole(role);
        request.setSession(session);
        request.setEntity(context);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ADMIN_GRANT);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            result = response.getAuthorized();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canRevoke(Session session, Role role, Permission perm)
        throws SecurityException
    {
        String methodName = CLS_NM + "canRevoke";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, methodName);
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OBJECT_NULL, methodName);
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, methodName);
        boolean result;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        RolePerm context = new RolePerm();
        context.setPerm(perm);
        context.setRole(role);
        request.setSession(session);
        request.setEntity(context);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ADMIN_REVOKE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            result = response.getAuthorized();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkAccess(Session session, Permission perm)
        throws SecurityException
    {
        String methodName = CLS_NM + ".checkAccess";
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_NULL, methodName);
        VUtil.assertNotNullOrEmpty(perm.getOpName(), GlobalErrIds.PERM_OPERATION_NULL, methodName);
        VUtil.assertNotNullOrEmpty(perm.getObjName(), GlobalErrIds.PERM_OBJECT_NULL, methodName);
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, methodName);
        boolean result;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setSession(session);
        request.setEntity(perm);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ADMIN_AUTHZ);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            result = response.getAuthorized();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addActiveRole(Session session, UserAdminRole role)
        throws SecurityException
    {
        String methodName = CLS_NM + ".addActiveRole";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, methodName);
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setSession(session);
        request.setEntity(role);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ADMIN_ADD);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void dropActiveRole(Session session, UserAdminRole role)
        throws SecurityException
    {
        String methodName = CLS_NM + ".dropActiveRole";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, methodName);
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setSession(session);
        request.setEntity(role);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ADMIN_DROP);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserAdminRole> sessionAdminRoles(Session session)
        throws SecurityException
    {
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, CLS_NM + ".sessionAdminRoles");
        List<UserAdminRole> roles;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setSession(session);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ADMIN_ROLES);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            roles = response.getEntities();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return roles;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> authorizedAdminRoles(Session session)
        throws SecurityException
    {
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, CLS_NM + ".authorizedAdminRoles");
        Set<String> retRoleNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setSession(session);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ADMIN_AUTHZ_ROLES);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Set<String> tempNames = response.getValueSet();
            // This is done to use a case insensitive TreeSet for returned names.
            retRoleNames.addAll(tempNames);
            Session outSession = response.getSession();
            session.copy(outSession);
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
    public List<Permission> sessionPermissions(Session session)
        throws SecurityException
    {
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, CLS_NM + ".sessionPermissions");
        List<Permission> retPerms;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setSession(session);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ADMIN_PERMS);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retPerms = response.getEntities();
            Session outSession = response.getSession();
            session.copy(outSession);
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
    public boolean canAdd(Session session, User user)
            throws SecurityException
    {
        throw new UnsupportedOperationException( "not implemented" );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canEdit(Session session, User user)
            throws SecurityException
    {
        throw new UnsupportedOperationException( "not implemented" );
    }
}