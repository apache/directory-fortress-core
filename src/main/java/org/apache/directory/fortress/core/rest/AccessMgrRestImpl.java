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

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.Manageable;
import org.apache.directory.fortress.core.model.*;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * Implementation class that performs runtime access control operations on data objects of type Fortress entities
 * This object performs runtime access control operations on objects that are provisioned RBAC entities
 * using HTTP access to Fortress Rest server.  These APIs map directly to similar named APIs specified by ANSI and NIST
 * RBAC system functions.
 * Many of the java doc function descriptions found below were taken directly from ANSI INCITS 359-2004.
 * The RBAC Functional specification describes administrative operations for the creation
 * and maintenance of RBAC element sets and relations; administrative review functions for
 * performing administrative queries; and system functions for creating and managing
 * RBAC attributes on user sessions and making access control decisions.
 * <p>
 * <hr>
 * <h3></h3>
 * <h4>RBAC0 - Core</h4>
 * Many-to-many relationship between Users, Roles and Permissions. Selective role activation into sessions.  API to add, update, delete identity data and perform identity and access control decisions during runtime operations.
 * <p>
 * <img src="../doc-files/RbacCore.png" alt="">
 * <hr>
 * <h4>RBAC1 - General Hierarchical Roles</h4>
 * Simplifies role engineering tasks using inheritance of one or more parent roles.
 * <p>
 * <img src="../doc-files/RbacHier.png"alt="">
 * <hr>
 * <h4>RBAC2 - Static Separation of Duty (SSD) Relations</h4>
 * Enforce mutual membership exclusions across role assignments.  Facilitate dual control policies by restricting which roles may be assigned to users in combination.  SSD provide added granularity for authorization limits which help enterprises meet strict compliance regulations.
 * <p>
 * <img src="../doc-files/RbacSSD.png"alt="">
 * <hr>
 * <h4>RBAC3 - Dynamic Separation of Duty (DSD) Relations</h4>
 * Control allowed role combinations to be activated within an RBAC session.  DSD policies fine tune role policies that facilitate authorization dual control and two man policy restrictions during runtime security checks.
 * <p>
 * <img src="../doc-files/RbacDSD.png"alt="">
 * <hr>
 * <p>
 * This class is thread safe and may be used in servlet or other multi-threaded environment.
 * <p>
 *
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AccessMgrRestImpl extends Manageable implements AccessMgr
{
    private static final String CLS_NM = AccessMgrRestImpl.class.getName();

    /**
     * {@inheritDoc}
     */
    @Override
    public Session authenticate(String userId, String password)
        throws SecurityException
    {
        VUtil.assertNotNullOrEmpty(userId, GlobalErrIds.USER_ID_NULL, CLS_NM + ".authenticate");
        VUtil.assertNotNullOrEmpty(password, GlobalErrIds.USER_PW_NULL, ".authenticate");
        Session retSession;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( new User( userId, password ) );
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.RBAC_AUTHN);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSession = response.getSession();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSession;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session createSession(User user, boolean isTrusted)
        throws SecurityException
    {
        VUtil.assertNotNull( user, GlobalErrIds.USER_NULL, CLS_NM + ".createSession" );
        Session retSession;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity(user);
        String szRequest = RestUtils.marshal(request);
        String szResponse;
        if(isTrusted)
        {
            szResponse = RestUtils.getInstance().post(szRequest, HttpIds.RBAC_CREATE_TRUSTED);
        }
        else
        {
            szResponse = RestUtils.getInstance().post(szRequest, HttpIds.RBAC_CREATE);
        }
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retSession = response.getSession();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retSession;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session createSession(User user, List<RoleConstraint> constraints, boolean isTrusted)
        throws SecurityException
    {
        VUtil.assertNotNull( constraints, GlobalErrIds.ROLE_CONSTRAINT_NULL, CLS_NM + ".createSession" );
        for( RoleConstraint constraint : constraints )
        {
            user.addProperty( constraint.getKey(), constraint.getValue() );
        }
        return createSession( user, isTrusted );
    }

    @Override
    public Session createSession( Group group ) throws SecurityException
    {
        VUtil.assertNotNull( group, GlobalErrIds.GROUP_NULL, CLS_NM + ".createSession" );
        Session retSession;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( group );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.RBAC_CREATE_GROUP_SESSION );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if (response.getErrorCode() == 0)
        {
            retSession = response.getSession();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retSession;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkAccess(Session session, Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_NULL, CLS_NM + ".checkAccess");
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, CLS_NM + ".checkAccess");
        boolean result;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setSession(session);
        request.setEntity(perm);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.RBAC_AUTHZ);
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
    public boolean checkAccess(User user, Permission perm, boolean isTrusted)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_NULL, CLS_NM + ".checkAccess");
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".checkAccess");
        boolean result;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity2(user);
        request.setEntity( perm );
        request.setIsFlag( isTrusted );
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.RBAC_CHECK);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            result = response.getAuthorized();
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
    public boolean isUserInRole( User user, Role role, boolean isTrusted )
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".isUserInRole");
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".isUserInRole");
        boolean result;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity2(user);
        request.setEntity(role);
        request.setIsFlag( isTrusted );
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.RBAC_CHECK_ROLE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            result = response.getAuthorized();
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
    public List<Permission> sessionPermissions(Session session)
        throws SecurityException
    {
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, CLS_NM + ".sessionPermissions");
        List<Permission> retPerms;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setSession(session);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.RBAC_PERMS);
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
    public List<UserRole> sessionRoles(Session session)
        throws SecurityException
    {
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, CLS_NM + ".sessionRoles");
        List<UserRole> retRoles;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setSession(session);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.RBAC_ROLES);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retRoles = response.getEntities();
            Session outSession = response.getSession();
            session.copy(outSession);
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
    public Set<String> authorizedRoles(Session session)
        throws SecurityException
    {
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, CLS_NM + ".sessionRoles");
        Set<String> retRoleNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setSession(session);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.RBAC_AUTHZ_ROLES);
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
    public void addActiveRole(Session session, UserRole role)
        throws SecurityException
    {
        String fullMethodName = CLS_NM + ".addActiveRole";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, fullMethodName);
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, fullMethodName);
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setSession(session);
        request.setEntity(role);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.RBAC_ADD);
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
    public void dropActiveRole(Session session, UserRole role)
        throws SecurityException
    {
        String fullMethodName = ".dropActiveRole";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, CLS_NM + fullMethodName);
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + fullMethodName);
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setSession(session);
        request.setEntity(role);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.RBAC_DROP);
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
    public String getUserId(Session session)
        throws SecurityException
    {
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, CLS_NM + ".getUserId");
        String userId;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setSession(session);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.RBAC_USERID);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            User outUser = (User) response.getEntity();
            userId = outUser.getUserId();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(Session session)
        throws SecurityException
    {
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, CLS_NM + ".getUser");
        User retUser;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setSession(session);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.RBAC_USER);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retUser = (User) response.getEntity();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retUser;
    }
}
