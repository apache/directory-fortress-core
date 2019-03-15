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

import org.apache.directory.fortress.core.DelAdminMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.Manageable;
import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.AdminRoleRelationship;
import org.apache.directory.fortress.core.model.FortRequest;
import org.apache.directory.fortress.core.model.FortResponse;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.model.OrgUnitRelationship;
import org.apache.directory.fortress.core.model.PermGrant;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserAdminRole;
import org.apache.directory.fortress.core.util.VUtil;


/**
 * This class implements the ARBAC02 DelAdminMgr interface for performing policy administration of Fortress ARBAC entities
 * using HTTP access to Fortress Rest server.
 * These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification describes delegated administrative
 * operations for the creation and maintenance of ARBAC element sets and relations.  Delegated administrative review functions for performing administrative queries
 * and system functions for creating and managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 * This class is NOT thread safe.
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="../doc-files/ARbac.png" alt="">
 * <p>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without sacrificing regulations for accountability or traceability.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class DelAdminMgrRestImpl extends Manageable implements DelAdminMgr
{
    private static final String CLS_NM = DelAdminMgrRestImpl.class.getName();


    /**
     * {@inheritDoc}
     */
    @Override
    public AdminRole addRole(AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, CLS_NM + ".addRole");
        AdminRole retRole;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(role);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ARLE_ADD);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retRole = (AdminRole) response.getEntity();
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
    public void deleteRole(AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, CLS_NM + ".deleteRole");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(role);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ARLE_DELETE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AdminRole updateRole(AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, CLS_NM + ".updateRole");
        AdminRole retRole;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(role);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ARLE_UPDATE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retRole = (AdminRole) response.getEntity();
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
    public void assignUser(UserAdminRole uAdminRole)
        throws SecurityException
    {
        VUtil.assertNotNull(uAdminRole, GlobalErrIds.ARLE_NULL, CLS_NM + ".assignUser");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(uAdminRole);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ARLE_ASGN);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deassignUser(UserAdminRole uAdminRole)
        throws SecurityException
    {
        VUtil.assertNotNull(uAdminRole, GlobalErrIds.ARLE_NULL, CLS_NM + ".deassignUser");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(uAdminRole);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ARLE_DEASGN);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public OrgUnit add(OrgUnit entity)
        throws SecurityException
    {
        VUtil.assertNotNull(entity, GlobalErrIds.ORG_NULL, CLS_NM + ".addOU");
        OrgUnit retOrg;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(entity);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ORG_ADD);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retOrg = (OrgUnit) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retOrg;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public OrgUnit update(OrgUnit entity)
        throws SecurityException
    {
        VUtil.assertNotNull(entity, GlobalErrIds.ORG_NULL, CLS_NM + ".updateOU");
        OrgUnit retOrg;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(entity);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ORG_UPDATE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retOrg = (OrgUnit) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retOrg;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public OrgUnit delete(OrgUnit entity)
        throws SecurityException
    {
        VUtil.assertNotNull(entity, GlobalErrIds.ORG_NULL, CLS_NM + ".deleteOU");
        OrgUnit retOrg;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(entity);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ORG_DELETE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retOrg = (OrgUnit) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retOrg;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addDescendant(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "addDescendantOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(child, GlobalErrIds.ORG_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        OrgUnitRelationship relationship = new OrgUnitRelationship();
        relationship.setParent(parent);
        relationship.setChild(child);
        request.setEntity(relationship);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ORG_DESC);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addAscendant(OrgUnit child, OrgUnit parent)
        throws SecurityException
    {
        String methodName = "addAscendantOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        OrgUnitRelationship relationship = new OrgUnitRelationship();
        relationship.setParent(parent);
        relationship.setChild(child);
        request.setEntity(relationship);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ORG_ASC);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addInheritance(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "addInheritanceOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(child, GlobalErrIds.ORG_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        OrgUnitRelationship relationship = new OrgUnitRelationship();
        relationship.setParent(parent);
        relationship.setChild(child);
        request.setEntity(relationship);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ORG_ADDINHERIT);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteInheritance(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "deleteInheritanceOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(child, GlobalErrIds.ORG_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        OrgUnitRelationship relationship = new OrgUnitRelationship();
        relationship.setParent(parent);
        relationship.setChild(child);
        request.setEntity(relationship);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ORG_DELINHERIT);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addDescendant(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "addDescendantRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        AdminRoleRelationship relationship = new AdminRoleRelationship();
        relationship.setParent(parentRole);
        relationship.setChild(childRole);
        request.setEntity(relationship);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ARLE_DESC);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addAscendant(AdminRole childRole, AdminRole parentRole)
        throws SecurityException
    {
        String methodName = "addAscendantRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        AdminRoleRelationship relationship = new AdminRoleRelationship();
        relationship.setParent(parentRole);
        relationship.setChild(childRole);
        request.setEntity(relationship);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ARLE_ASC);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addInheritance(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "addInheritanceRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        AdminRoleRelationship relationship = new AdminRoleRelationship();
        relationship.setParent(parentRole);
        relationship.setChild(childRole);
        request.setEntity(relationship);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ARLE_ADDINHERIT);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteInheritance(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "deleteInheritanceRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        AdminRoleRelationship relationship = new AdminRoleRelationship();
        relationship.setParent(parentRole);
        relationship.setChild(childRole);
        request.setEntity(relationship);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ARLE_DELINHERIT);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Permission addPermission(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".addPermission");
        Permission retPerm;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        perm.setAdmin(true);
        request.setEntity(perm);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.PERM_ADD);
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
    public Permission updatePermission(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".updatePermission");
        Permission retPerm;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        perm.setAdmin(true);
        request.setEntity(perm);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.PERM_UPDATE);
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
    public void deletePermission(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".deletePermission");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        perm.setAdmin(true);
        request.setEntity(perm);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.PERM_DELETE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PermObj addPermObj(PermObj pObj)
        throws SecurityException
    {
        VUtil.assertNotNull(pObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".addPermObj");
        PermObj retObj;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        pObj.setAdmin(true);
        request.setEntity(pObj);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.OBJ_ADD);
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
    public PermObj updatePermObj(PermObj pObj)
        throws SecurityException
    {
        VUtil.assertNotNull(pObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".updatePermObj");
        PermObj retObj;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        pObj.setAdmin(true);
        request.setEntity(pObj);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.OBJ_UPDATE);
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
    public void deletePermObj(PermObj pObj)
        throws SecurityException
    {
        VUtil.assertNotNull(pObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".deletePermObj");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        pObj.setAdmin(true);
        request.setEntity(pObj);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.OBJ_DELETE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void grantPermission(Permission perm, AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".grantPermission");
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".grantPermission");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin(true);
        permGrant.setObjName(perm.getObjName());
        permGrant.setObjId(perm.getObjId());
        permGrant.setOpName(perm.getOpName());
        permGrant.setRoleNm(role.getName());
        request.setEntity(permGrant);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ROLE_GRANT);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void revokePermission(Permission perm, AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".revokePermission");
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".revokePermission");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin(true);
        permGrant.setObjName(perm.getObjName());
        permGrant.setObjId(perm.getObjId());
        permGrant.setOpName(perm.getOpName());
        permGrant.setRoleNm(role.getName());
        request.setEntity(permGrant);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.ROLE_REVOKE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void grantPermission(Permission perm, User user)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".grantPermissionUser");
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".grantPermissionUser");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin(true);
        permGrant.setObjName(perm.getObjName());
        permGrant.setObjId(perm.getObjId());
        permGrant.setOpName(perm.getOpName());
        permGrant.setUserId(user.getUserId());
        request.setEntity(permGrant);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.USER_GRANT);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void revokePermission(Permission perm, User user)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".revokePermission");
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".revokePermission");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin(true);
        permGrant.setObjName(perm.getObjName());
        permGrant.setObjId(perm.getObjId());
        permGrant.setOpName(perm.getOpName());
        permGrant.setUserId(user.getUserId());
        request.setEntity(permGrant);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.USER_REVOKE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }
}
