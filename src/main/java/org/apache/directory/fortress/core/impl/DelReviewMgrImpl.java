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

import org.apache.directory.fortress.annotation.AdminPermissionOperation;
import org.apache.directory.fortress.core.DelReviewMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserAdminRole;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * This class implements the ARBAC02 DelReviewMgr interface for performing policy interrogation of provisioned Fortress 
 * ARBAC entities that reside in LDAP directory.
 * These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification 
 * describes delegated administrative
 * operations for the creation and maintenance of ARBAC element sets and relations.  Delegated administrative review 
 * functions for performing administrative queries and system functions for creating and managing ARBAC attributes on user
 * sessions and making delegated administrative access control decisions.
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="../doc-files/ARbac.png" alt="">
 * <p>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises 
 * the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without 
 * sacrificing regulations for accountability or traceability.
 * <p>
 * This class is NOT thread safe if parent instance variables ({@link #contextId} or {@link #adminSess}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DelReviewMgrImpl extends Manageable implements DelReviewMgr, Serializable
{
    private static final String CLS_NM = DelReviewMgrImpl.class.getName();
    private UserP userP;
    private OrgUnitP ouP;
    private AdminRoleP admRP;
    private PermP permP;

    public DelReviewMgrImpl() {
        userP = new UserP();
        ouP = new OrgUnitP();
        admRP = new AdminRoleP();
        permP = new PermP();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public AdminRole readRole(AdminRole role)
        throws SecurityException
    {
        String methodName = "readRole";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ARLE_NULL);
        checkAccess(CLS_NM, methodName);
        return admRP.read(role);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<AdminRole> findRoles(String searchVal)
        throws SecurityException
    {
        String methodName = "findRoles";
        VUtil.assertNotNull(searchVal, GlobalErrIds.ARLE_NM_NULL, CLS_NM + "." + methodName);
        checkAccess(CLS_NM, methodName);
        AdminRole adminRole = new AdminRole(searchVal);
        adminRole.setContextId(this.contextId);
        return admRP.search(adminRole);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<UserAdminRole> assignedRoles(User user)
        throws SecurityException
    {
        String methodName = "assignedRoles";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        checkAccess(CLS_NM, methodName);
        User ue = userP.read(user, true);
        return ue.getAdminRoles();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<User> assignedUsers(AdminRole role)
        throws SecurityException
    {
        String methodName = "assignedUsers";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ARLE_NULL);
        checkAccess(CLS_NM, methodName);
        return userP.getAssignedUsers(role);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation(operationName="readOU")
    public OrgUnit read(OrgUnit entity)
        throws SecurityException
    {
        String methodName = "readOU";
        assertContext(CLS_NM, methodName, entity, GlobalErrIds.ORG_NULL);
        checkAccess(CLS_NM, methodName);
        return ouP.read(entity);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation(operationName="searchOU")
    public List<OrgUnit> search(OrgUnit.Type type, String searchVal)
        throws SecurityException
    {
        String methodName = "searchOU";
        //VUtil.assertNotNullOrEmpty(searchVal, GlobalErrIds.ORG_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(type, GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        checkAccess(CLS_NM, methodName);
        OrgUnit orgUnit = new OrgUnit(searchVal);
        orgUnit.setType(type);
        orgUnit.setContextId(this.contextId);
        return ouP.search(orgUnit);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Permission> rolePermissions(AdminRole role)
        throws SecurityException 
    {
        return rolePermissions( role, false );    
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<Permission> rolePermissions(AdminRole role,
        boolean noInheritance) throws SecurityException 
    {
        String methodName = "rolePermissions";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess(CLS_NM, methodName);
        return permP.search( role, noInheritance );
    }
}

