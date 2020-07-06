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


import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.fortress.annotation.AdminPermissionOperation;
import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.*;
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
public class AccessMgrImpl extends Manageable implements AccessMgr, Serializable
{
    private static final String CLS_NM = AccessMgrImpl.class.getName();
    private static final UserP userP = new UserP();
    private static final GroupP groupP = new GroupP();
    private static final PermP permP = new PermP();

    /**
     * {@inheritDoc}
     */
    @Override
    public Session authenticate( String userId, String password )
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
    public Session createSession( User user, List<RoleConstraint> constraints, boolean isTrusted )
        throws SecurityException
    {
        String methodName = "createSession";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        VUtil.assertNotNull( constraints, GlobalErrIds.ROLE_CONSTRAINT_NULL, methodName );
        return userP.createSession( user, constraints, isTrusted );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session createSession( Group group )
            throws SecurityException
    {
        String methodName = "createSession";
        assertContext( CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL );

        return groupP.createSession( group );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
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
        VUtil.getInstance().validateConstraints( session, VUtil.ConstraintType.USER, false );
        VUtil.getInstance().validateConstraints( session, VUtil.ConstraintType.ROLE, false );
        setEntitySession(CLS_NM, methodName, session);
        return permP.checkPermission( session, perm );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public boolean checkAccess( User user, Permission perm, boolean isTrusted )
        throws SecurityException
    {
        Session session = createSession( user, isTrusted );
        return checkAccess( session, perm );
    }


    /**
     * {@inheritDoc}
     */
    public boolean isUserInRole( User user, Role role, boolean isTrusted )
        throws SecurityException
    {
        String methodName = "isUserInRole";
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        VUtil.assertNotNullOrEmpty( role.getName(), GlobalErrIds.ROLE_NM_NULL,
            getFullMethodName( CLS_NM, methodName ) );
        boolean result = false;
        Session session = createSession( user, isTrusted );
        List<UserRole> sRoles = session.getRoles();
        UserRole uRole = new UserRole( user.getUserId(), role.getName() );
        // If session has role activated:
        if ( sRoles != null && sRoles.contains( uRole ) )
        {
            result = true;
        }
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<Permission> sessionPermissions( Session session )
        throws SecurityException
    {
        String methodName = "sessionPermissions";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        VUtil.getInstance().validateConstraints( session, VUtil.ConstraintType.USER, false );
        VUtil.getInstance().validateConstraints( session, VUtil.ConstraintType.ROLE, false );
        setEntitySession(CLS_NM, methodName, session);
        return permP.search( session );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<UserRole> sessionRoles( Session session )
        throws SecurityException
    {
        String methodName = "sessionRoles";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        VUtil.getInstance().validateConstraints( session, VUtil.ConstraintType.USER, false );
        VUtil.getInstance().validateConstraints( session, VUtil.ConstraintType.ROLE, false );
        setEntitySession(CLS_NM, methodName, session);
        return session.getRoles();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Set<String> authorizedRoles( Session session )
        throws SecurityException
    {
        String methodName = "authorizedRoles";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        if (session.isGroupSession())
        {
            VUtil.assertNotNull(session.getGroup(), GlobalErrIds.GROUP_NULL, CLS_NM + ".authorizedRoles");
        }
        else
        {
            VUtil.assertNotNull(session.getUser(), GlobalErrIds.USER_NULL, CLS_NM + ".authorizedRoles");
        }
        VUtil.getInstance().validateConstraints( session, VUtil.ConstraintType.USER, false );
        VUtil.getInstance().validateConstraints( session, VUtil.ConstraintType.ROLE, false );
        setEntitySession(CLS_NM, methodName, session);
        return RoleUtil.getInstance().getInheritedRoles( session.getRoles(), this.contextId );
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

        String entityId;
        if (session.isGroupSession())
        {
            entityId = session.getGroupName();
        }
        else
        {
            entityId = session.getUserId();
        }
        role.setUserId(entityId);
        List<UserRole> assignedRoles;
        List<UserRole> sRoles = session.getRoles();
        // If session already has same role activated:
        if ( sRoles != null && sRoles.contains( role ) )
        {
            String info = getFullMethodName(CLS_NM, methodName) + " Entity [" + entityId + "] Role ["
                        + role.getName() + "] role already activated.";
            throw new SecurityException( GlobalErrIds.URLE_ALREADY_ACTIVE, info );
        }

        if (session.isGroupSession())
        {
            Group inGroup = new Group(session.getGroupName());
            inGroup.setContextId(this.contextId);
            Group ge = groupP.read(inGroup);
            assignedRoles = ge.getRoles();
        }
        else
        {
            User inUser = new User(session.getUserId());
            inUser.setContextId(this.contextId);
            User ue = userP.read(inUser, true);
            assignedRoles = ue.getRoles();
        }
        int indx;
        // Is the user has not been assigned the role:
        if ( CollectionUtils.isEmpty( assignedRoles ) || ( ( indx = assignedRoles.indexOf( role ) ) == -1 ) )
        {
            String info = getFullMethodName(CLS_NM, methodName) + " Role [" + role.getName() + "] Entity ["
                        + entityId + "] role not authorized for entity.";
            throw new SecurityException( GlobalErrIds.URLE_ACTIVATE_FAILED, info );
        }

        // validate Dynamic Separation of Duty Relations:
        SDUtil.getInstance().validateDSD( session, role );

        // set the role to the session:
        session.setRole( assignedRoles.get( indx ) );

        // Check role temporal constraints, not DSD, performed earlier:
        VUtil.getInstance().validateConstraints( session, VUtil.ConstraintType.ROLE, false );
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

        String entityId;
        if (session.isGroupSession())
        {
            entityId = session.getGroupName();
        }
        else
        {
            entityId = session.getUserId();
        }
        role.setUserId(entityId);
        List<UserRole> activatedRoles = session.getRoles();
        VUtil.assertNotNull( activatedRoles, GlobalErrIds.URLE_DEACTIVE_FAILED, CLS_NM + getFullMethodName( CLS_NM, methodName ) );
        int indx = activatedRoles.indexOf( role );
        if ( indx != -1 )
        {
            activatedRoles.remove( role );
        }
        else
        {
            String info = getFullMethodName( CLS_NM, methodName ) + " Role [" + role.getName() + "] Entity ["
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