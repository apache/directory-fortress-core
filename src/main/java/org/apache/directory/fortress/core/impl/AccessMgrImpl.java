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


import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.util.VUtil;


/**
 * Implementation class that performs runtime access control operations on data objects of type Fortress entities
 * This class performs runtime access control operations on objects that are provisioned RBAC entities
 * that reside in LDAP directory.  These APIs map directly to similar named APIs specified by ANSI and NIST
 * RBAC system functions.
 * Many of the java doc function descriptions found below were taken directly from ANSI INCITS 359-2004.
 * The RBAC Functional specification describes administrative operations for the creation
 * and maintenance of RBAC element sets and relations; administrative review functions for
 * performing administrative queries; and system functions for creating and managing
 * RBAC attributes on user sessions and making access control decisions.
 * <hr>
 * <h3></h3>
 * <h4>RBAC0 - Core</h4>
 * Many-to-many relationship between Users, Roles and Permissions. Selective role activation into sessions.  API to add, 
 * update, delete identity data and perform identity and access control decisions during runtime operations.
 * <p>
 * <img src="../doc-files/RbacCore.png" alt="">
 * <hr>
 * <h4>RBAC1 - General Hierarchical Roles</h4>
 * Simplifies role engineering tasks using inheritance of one or more parent roles.
 * <p>
 * <img src="../doc-files/RbacHier.png" alt="">
 * <hr>
 * <h4>RBAC2 - Static Separation of Duty (SSD) Relations</h4>
 * Enforce mutual membership exclusions across role assignments.  Facilitate dual control policies by restricting which 
 * roles may be assigned to users in combination.  SSD provide added granularity for authorization limits which help 
 * enterprises meet strict compliance regulations.
 * <p>
 * <img src="../doc-files/RbacSSD.png" alt="">
 * <hr>
 * <h4>RBAC3 - Dynamic Separation of Duty (DSD) Relations</h4>
 * Control allowed role combinations to be activated within an RBAC session.  DSD policies fine tune role policies that 
 * facilitate authorization dual control and two man policy restrictions during runtime security checks.
 * <p>
 * <img src="../doc-files/RbacDSD.png" alt="">
 * <hr>
 * <p>
 * This class is NOT thread safe if parent instance variables ({@link #contextId} or {@link #adminSess}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AccessMgrImpl extends Manageable implements AccessMgr
{
    private static final String CLS_NM = AccessMgrImpl.class.getName();
    private static final UserP userP = new UserP();
    private static final PermP permP = new PermP();

    /**
     * {@inheritDoc}
     */
    @Override
    public Session authenticate( String userId, char[] password )
        throws SecurityException
    {
        String methodName = "authenticate";
        VUtil.assertNotNullOrEmpty( userId, GlobalErrIds.USER_ID_NULL, getFullMethodName( CLS_NM, methodName ) );
        VUtil.assertNotNullOrEmpty( password, GlobalErrIds.USER_PW_NULL, getFullMethodName( CLS_NM, methodName ) );
        User inUser = new User( userId );
        inUser.setContextId( contextId );

        // Determine if user valid.
        User user = userP.read( inUser, false );
        user.setPassword( password );
        user.setContextId( contextId );
        Session ftSess = userP.authenticate( user );
        ftSess.setUser( user );
        return ftSess;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Session createSession( User user, boolean isTrusted )
        throws SecurityException
    {
        String methodName = "createSession";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );

        return userP.createSession( user, isTrusted );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkAccess( Session session, Permission perm )
        throws SecurityException
    {
        String methodName = "checkAccess";
        assertContext( CLS_NM, methodName, perm, GlobalErrIds.PERM_NULL );
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        
        VUtil.assertNotNullOrEmpty( perm.getOpName(), GlobalErrIds.PERM_OPERATION_NULL,
            getFullMethodName( CLS_NM, methodName ) );
        VUtil.assertNotNullOrEmpty( perm.getObjName(), GlobalErrIds.PERM_OBJECT_NULL,
            getFullMethodName( CLS_NM, methodName ) );
        VUtil.validateConstraints( session, VUtil.ConstraintType.USER, false );
        VUtil.validateConstraints( session, VUtil.ConstraintType.ROLE, false );
        setEntitySession(CLS_NM, methodName, session);
        return permP.checkPermission( session, perm );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Permission> sessionPermissions( Session session )
        throws SecurityException
    {
        String methodName = "sessionPermissions";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        VUtil.validateConstraints( session, VUtil.ConstraintType.USER, false );
        VUtil.validateConstraints( session, VUtil.ConstraintType.ROLE, false );
        setEntitySession(CLS_NM, methodName, session);
        return permP.search( session );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserRole> sessionRoles( Session session )
        throws SecurityException
    {
        String methodName = "sessionRoles";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        VUtil.validateConstraints( session, VUtil.ConstraintType.USER, false );
        VUtil.validateConstraints( session, VUtil.ConstraintType.ROLE, false );
        setEntitySession(CLS_NM, methodName, session);
        return session.getRoles();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> authorizedRoles( Session session )
        throws SecurityException
    {
        String methodName = "authorizedRoles";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        VUtil.assertNotNull( session.getUser(), GlobalErrIds.USER_NULL, CLS_NM + ".authorizedRoles" );
        VUtil.validateConstraints( session, VUtil.ConstraintType.USER, false );
        VUtil.validateConstraints( session, VUtil.ConstraintType.ROLE, false );
        setEntitySession(CLS_NM, methodName, session);
        return RoleUtil.getInheritedRoles( session.getRoles(), this.contextId );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addActiveRole( Session session, UserRole role )
        throws SecurityException
    {
        String methodName = "addActiveRole";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        role.setUserId( session.getUserId() );
        List<UserRole> uRoles;
        List<UserRole> sRoles = session.getRoles();
        // If session already has same role activated:
        if ( sRoles != null && sRoles.contains( role ) )
        {
            String info = getFullMethodName( CLS_NM, methodName ) + " User [" + session.getUserId() + "] Role ["
                + role.getName() + "] role already activated.";
            throw new SecurityException( GlobalErrIds.URLE_ALREADY_ACTIVE, info );
        }

        User inUser = new User( session.getUserId() );
        inUser.setContextId( this.contextId );
        User ue = userP.read( inUser, true );
        uRoles = ue.getRoles();
        int indx;
        // Is the role activation target valid for this user?
        if ( !CollectionUtils.isNotEmpty( uRoles ) || ( ( indx = uRoles.indexOf( role ) ) == -1 ) )
        {
            String info = getFullMethodName( CLS_NM, methodName ) + " Role [" + role.getName() + "] User ["
                + session.getUserId() + "] role not authorized for user.";
            throw new SecurityException( GlobalErrIds.URLE_ACTIVATE_FAILED, info );
        }

        // validate Dynamic Separation of Duty Relations:
        SDUtil.validateDSD( session, role );

        // set the role to the session:
        session.setRole( uRoles.get( indx ) );

        // Check role temporal constraints & DSD:
        VUtil.validateConstraints( session, VUtil.ConstraintType.ROLE, false );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void dropActiveRole( Session session, UserRole role )
        throws SecurityException
    {
        String methodName = "dropActiveRole";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        role.setUserId( session.getUserId() );
        List<UserRole> roles = session.getRoles();
        VUtil
            .assertNotNull( roles, GlobalErrIds.URLE_DEACTIVE_FAILED, CLS_NM + getFullMethodName( CLS_NM, methodName ) );
        int indx = roles.indexOf( role );
        if ( indx != -1 )
        {
            roles.remove( role );
        }
        else
        {
            String info = getFullMethodName( CLS_NM, methodName ) + " Role [" + role.getName() + "] User ["
                + session.getUserId() + "], not previously activated";
            throw new SecurityException( GlobalErrIds.URLE_NOT_ACTIVE, info );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserId( Session session )
        throws SecurityException
    {
        assertContext( CLS_NM, "getUserId", session, GlobalErrIds.USER_SESS_NULL );
        return session.getUserId();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser( Session session )
        throws SecurityException
    {
        assertContext( CLS_NM, "getUser", session, GlobalErrIds.USER_SESS_NULL );

        return session.getUser();
    }
}