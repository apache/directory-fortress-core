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

import org.apache.directory.fortress.core.AccelMgr;
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
public class AccelMgrImpl extends Manageable implements AccelMgr, Serializable
{
    private static final String CLS_NM = AccessMgrImpl.class.getName();
    private AcceleratorDAO aDao;


    /**
     * package private constructor ensures outside classes must use factory: {@link org.apache.directory.fortress.core.AccelMgrFactory}
     */
    public AccelMgrImpl()
    {
        aDao = new org.apache.directory.fortress.core.impl.AcceleratorDAO();
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
        return aDao.createSession( user );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteSession( Session session )
        throws SecurityException
    {
        String methodName = "deleteSession";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        aDao.deleteSession( session );
    }


    /**
     * {@inheritDoc}
     */
    public List<UserRole> sessionRoles(Session session)
        throws SecurityException
    {
        String methodName = "sessionRoles";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        return aDao.sessionRoles( session );
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
        VUtil.assertNotNullOrEmpty( perm.getOpName(), GlobalErrIds.PERM_OPERATION_NULL, getFullMethodName( CLS_NM,
            methodName ) );
        VUtil.assertNotNullOrEmpty( perm.getObjName(), GlobalErrIds.PERM_OBJECT_NULL, getFullMethodName( CLS_NM,
            methodName ) );
        return aDao.checkAccess( session, perm );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Permission> sessionPermissions( Session session )
        throws SecurityException
    {
        throw new java.lang.UnsupportedOperationException();
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
        VUtil.assertNotNullOrEmpty( role.getUserId(), GlobalErrIds.USER_ID_NULL,
            getFullMethodName( CLS_NM, methodName ) );
        VUtil.assertNotNullOrEmpty( role.getName(), GlobalErrIds.ROLE_NM_NULL, getFullMethodName( CLS_NM,
            methodName ) );
        aDao.addActiveRole( session, role );
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
        VUtil.assertNotNullOrEmpty( role.getUserId(), GlobalErrIds.USER_ID_NULL,
            getFullMethodName( CLS_NM, methodName ) );
        VUtil.assertNotNullOrEmpty( role.getName(), GlobalErrIds.ROLE_NM_NULL, getFullMethodName( CLS_NM,
            methodName ) );
        aDao.dropActiveRole( session, role );
    }
}