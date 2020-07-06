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
import org.apache.directory.fortress.core.AdminMgr;
import org.apache.directory.fortress.core.AdminMgrFactory;
import org.apache.directory.fortress.core.DelAdminMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.ConstraintUtil;
import org.apache.directory.fortress.core.model.Hier;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Relationship;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserAdminRole;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * This class implements the ARBAC02 DelAdminMgr interface for performing policy administration of Fortress ARBAC entities
 * that reside in LDAP directory.
 * These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification 
 * describes delegated administrative operations for the creation and maintenance of ARBAC element sets and relations.  
 * Delegated administrative review functions for performing administrative queries and system functions for creating and 
 * managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 * This class is NOT thread safe.
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
public final class DelAdminMgrImpl extends Manageable implements DelAdminMgr, Serializable
{
    private static final String CLS_NM = DelAdminMgrImpl.class.getName();
    private OrgUnitP ouP;
    private AdminRoleP admRP;
    private PermP permP;
    private UserP userP;

    public DelAdminMgrImpl() {
        ouP = new OrgUnitP();
        admRP = new AdminRoleP();
        permP = new PermP();
        userP = new UserP();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public AdminRole addRole(AdminRole role)
        throws SecurityException
    {
        String methodName = "addRole";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ARLE_NULL);
        setEntitySession(CLS_NM, methodName, role);
        return admRP.add(role);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void deleteRole(AdminRole role)
        throws SecurityException
    {
        String methodName = "deleteRole";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ARLE_NULL);
        setEntitySession(CLS_NM, methodName, role);
        int numChildren = AdminRoleUtil.numChildren( role.getName(), role.getContextId() );
        if (numChildren > 0)
        {
            String error =  methodName + " role [" + role.getName() + "] must remove [" + numChildren + "] descendants before deletion";
            throw new SecurityException(GlobalErrIds.HIER_DEL_FAILED_HAS_CHILD, error, null);
        }
        // search for all users assigned this role and deassign:
        List<User> users = userP.getAssignedUsers(role);
        if (users != null)
        {
            for (User ue : users)
            {
                User user = new User(ue.getUserId());
                UserAdminRole uAdminRole = new UserAdminRole(ue.getUserId(), role.getName());
                uAdminRole.setContextId(contextId);
                setAdminData(CLS_NM, methodName, user);
                deassignUser(uAdminRole);
            }
        }
        permP.remove(role);
        // remove all parent relationships from the role graph:
        Set<String> parents = AdminRoleUtil.getParents(role.getName(), this.contextId);
        if(parents != null)
        {
            for(String parent : parents)
            {
                AdminRoleUtil.updateHier(this.contextId, new Relationship(role.getName().toUpperCase(), parent.toUpperCase()), Hier.Op.REM);
            }
        }
        admRP.delete(role);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public AdminRole updateRole(AdminRole role)
        throws SecurityException
    {
        String methodName = "updateRole";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ARLE_NULL);
        setEntitySession(CLS_NM, methodName, role);
        AdminRole re = admRP.update(role);
        // search for all users assigned this role and update:
        List<User> users = userP.getAssignedUsers(role);
        if(CollectionUtils.isNotEmpty( users ))
        {
            final AdminMgr aMgr = AdminMgrFactory.createInstance(this.contextId);
            for (User ue : users)
            {
                User upUe = new User(ue.getUserId());
                setAdminData(CLS_NM, methodName, upUe);
                List<UserAdminRole> uaRoles = ue.getAdminRoles();
                UserAdminRole chgRole = new UserAdminRole();
                chgRole.setName(role.getName());
                chgRole.setUserId(ue.getUserId());
                chgRole.setOsPSet( role.getOsPSet() );
                chgRole.setOsUSet( role.getOsUSet() );
                uaRoles.remove(chgRole);
                ConstraintUtil.copy( re, chgRole );
                uaRoles.add(chgRole);
                upUe.setUserId(ue.getUserId());
                upUe.setAdminRole(chgRole);
                aMgr.updateUser(upUe);
            }
        }
        return re;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void assignUser(UserAdminRole uAdminRole)
        throws SecurityException
    {
        String methodName = "assignUser";
        assertContext(CLS_NM, methodName, uAdminRole, GlobalErrIds.ARLE_NULL);

        setEntitySession(CLS_NM, methodName, uAdminRole);

        AdminRole adminRole = new AdminRole(uAdminRole.getName());
        adminRole.setContextId(uAdminRole.getContextId());
        // retrieve the admin role info:
        AdminRole validRole = admRP.read(adminRole);

        // if the UserAdminRole entity doesn't have temporal constraints set already, copy from the AdminRole declaration:
        // if the input role entity attribute doesn't have temporal constraints set, copy from the role declaration:
        ConstraintUtil.validateOrCopy( validRole, uAdminRole );

        // copy the ARBAC AdminRole attributes to UserAdminRole:
        userP.copyAdminAttrs( validRole, uAdminRole );
        String dn = userP.assign(uAdminRole);
        // copy the admin session info to AdminRole:
        setAdminData(CLS_NM, methodName, validRole);
        // Assign user dn attribute to the adminRole, this will add a single, standard attribute value, called "roleOccupant", directly onto the adminRole node:
        admRP.assign(validRole, dn);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void deassignUser(UserAdminRole uAdminRole)
        throws SecurityException
    {
        String methodName = "deassignUser";
        assertContext(CLS_NM, methodName, uAdminRole, GlobalErrIds.ARLE_NULL);
        setEntitySession(CLS_NM, methodName, uAdminRole);
        String dn = userP.deassign(uAdminRole);
        AdminRole adminRole = new AdminRole(uAdminRole.getName());
        // copy the ARBAC attributes to AdminRole:
        setAdminData(CLS_NM, methodName, adminRole);
        // Deassign user dn attribute to the adminRole, this will remove a single, standard attribute value, called "roleOccupant", directly onto the adminRole node:
        admRP.deassign(adminRole, dn);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public OrgUnit add(OrgUnit entity) throws SecurityException
    {
        String methodName = "addOU";
        assertContext(CLS_NM, methodName, entity, GlobalErrIds.ORG_NULL);
        setEntitySession(CLS_NM, methodName, entity);
        VUtil.assertNotNull(entity.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        return ouP.add(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public OrgUnit update(OrgUnit entity)
        throws SecurityException
    {
        String methodName = "updateOU";
        assertContext(CLS_NM, methodName, entity, GlobalErrIds.ORG_NULL);
        setEntitySession(CLS_NM, methodName, entity);
        VUtil.assertNotNull(entity.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        return ouP.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public OrgUnit delete(OrgUnit entity)
        throws SecurityException
    {
        String methodName = "deleteOU";
        assertContext(CLS_NM, methodName, entity, GlobalErrIds.ORG_NULL);
        setEntitySession(CLS_NM, methodName, entity);
        VUtil.assertNotNull(entity.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        int numChildren;
        if (entity.getType() == OrgUnit.Type.USER)
        {
            numChildren = UsoUtil.getInstance().numChildren( entity.getName(), entity.getContextId() );
        }
        else
        {
            numChildren = PsoUtil.getInstance().numChildren( entity.getName(), entity.getContextId() );
        }
        if (numChildren > 0)
        {
            String error =  methodName + " orgunit [" + entity.getName() + "] must remove [" + numChildren + "] descendants before deletion";
            throw new SecurityException(GlobalErrIds.HIER_DEL_FAILED_HAS_CHILD, error, null);
        }
        if (entity.getType() == OrgUnit.Type.USER)
        {
            // Ensure the org unit is not assigned to any users, but set the sizeLimit to "true" to limit result set size.
            List<User> assignedUsers = userP.search(entity, true);
            if (CollectionUtils.isNotEmpty( assignedUsers ))
            {
                String error =  methodName + " orgunit [" + entity.getName() + "] must unassign [" + assignedUsers.size() + "] users before deletion";
                throw new SecurityException(GlobalErrIds.ORG_DEL_FAILED_USER, error, null);
            }
        }
        else
        {
            // Ensure the org unit is not assigned to any permission objects but set the sizeLimit to "true" to limit result set size..
            // pass a "false" which places no restrictions on how many records server returns.
            List<PermObj> assignedPerms = permP.search(entity, false);
            if (CollectionUtils.isNotEmpty( assignedPerms ))
            {
                String error =  methodName + " orgunit [" + entity.getName() + "] must unassign [" + assignedPerms.size() + "] perm objs before deletion";
                throw new SecurityException(GlobalErrIds.ORG_DEL_FAILED_PERM, error, null);
            }
        }
        // remove all parent relationships from this org graph:
        Set<String> parents;
        if (entity.getType() == OrgUnit.Type.USER)
        {
            parents = UsoUtil.getInstance().getParents(entity.getName(), this.contextId);
        }
        else
        {
            parents = PsoUtil.getInstance().getParents(entity.getName(), this.contextId);
        }
        if(parents != null)
        {
            for(String parent : parents)
            {
                if (entity.getType() == OrgUnit.Type.USER)
                {
                    UsoUtil.getInstance().updateHier(this.contextId, new Relationship(entity.getName().toUpperCase(), parent.toUpperCase()), Hier.Op.REM);
                }
                else
                {
                    PsoUtil.getInstance().updateHier(this.contextId, new Relationship(entity.getName().toUpperCase(), parent.toUpperCase()), Hier.Op.REM);
                }
            }
        }
        // everything checked out good - remove the org unit from the OrgUnit data set:
        return ouP.delete(entity);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void addDescendant(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "addDescendantOU";
        assertContext(CLS_NM, methodName, parent, GlobalErrIds.ORG_PARENT_NULL);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        assertContext(CLS_NM, methodName, child, GlobalErrIds.ORG_CHILD_NULL);
        setEntitySession(CLS_NM, methodName, child);

        // ensure the parent OrgUnit exists:
        ouP.read(parent);
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.getInstance().validateRelationship(child, parent, false);
        }
        else
        {
            PsoUtil.getInstance().validateRelationship(child, parent, false);
        }
        child.setParent(parent.getName());
        ouP.add(child);
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.getInstance().updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.ADD);
        }
        else
        {
            PsoUtil.getInstance().updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.ADD);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation(operationName="addAscendantOU")
    public void addAscendant(OrgUnit child, OrgUnit parent)
        throws SecurityException
    {
        String methodName = "addAscendantOU";
        assertContext(CLS_NM, methodName, parent, GlobalErrIds.ORG_PARENT_NULL);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        setEntitySession(CLS_NM, methodName, parent);
        assertContext(CLS_NM, methodName, child, GlobalErrIds.ORG_CHILD_NULL);

        // ensure the child OrgUnit exists:
        OrgUnit newChild = ouP.read(child);
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.getInstance().validateRelationship(child, parent, false);
        }
        else
        {
            PsoUtil.getInstance().validateRelationship(child, parent, false);
        }
        ouP.add(parent);
        newChild.setParent(parent.getName());
        newChild.setContextId(this.contextId);
        ouP.update(newChild);
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.getInstance().updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.ADD);
        }
        else
        {
            PsoUtil.getInstance().updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.ADD);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation(operationName="addInheritanceOU")
    public void addInheritance(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "addInheritanceOU";
        assertContext(CLS_NM, methodName, parent, GlobalErrIds.ORG_PARENT_NULL);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        assertContext(CLS_NM, methodName, child, GlobalErrIds.ORG_CHILD_NULL);
        setEntitySession(CLS_NM, methodName, parent);
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.getInstance().validateRelationship(child, parent, false);
        }
        else
        {
            PsoUtil.getInstance().validateRelationship(child, parent, false);
        }
        // validate that both orgs are present:
        ouP.read(parent);
        OrgUnit cOrg = ouP.read(child);
        cOrg.setParent(parent.getName());
        cOrg.setContextId(this.contextId);
        setAdminData(CLS_NM, methodName, cOrg);
        ouP.update(cOrg);

        // we're still good, now set the hierarchical relationship:
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.getInstance().updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.ADD);
        }
        else
        {
            PsoUtil.getInstance().updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.ADD);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation(operationName="deleteInheritanceOU")
    public void deleteInheritance(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "deleteInheritanceOU";
        assertContext(CLS_NM, methodName, parent, GlobalErrIds.ORG_PARENT_NULL);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        assertContext(CLS_NM, methodName, child, GlobalErrIds.ORG_CHILD_NULL);
        setEntitySession(CLS_NM, methodName, parent);
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.getInstance().validateRelationship(child, parent, true);
        }
        else
        {
            PsoUtil.getInstance().validateRelationship(child, parent, true);
        }
        if (parent.getType() == OrgUnit.Type.USER)
        {
            UsoUtil.getInstance().updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.REM);
        }
        else
        {
            PsoUtil.getInstance().updateHier(this.contextId, new Relationship(child.getName().toUpperCase(), parent.getName().toUpperCase()), Hier.Op.REM);
        }
        OrgUnit cOrg = ouP.read(child);
        cOrg.setContextId(this.contextId);
        cOrg.delParent(parent.getName());
        setAdminData(CLS_NM, methodName, cOrg);
        // are there any parents left?
        if(!CollectionUtils.isNotEmpty( cOrg.getParents() ))
        {
            // The updates only update non-empty multivalued attributes
            // so if last parent assigned, so must remove the attribute completely:
            ouP.deleteParent(cOrg);
        }
        else
        {
            ouP.update(cOrg);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void addDescendant(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "addDescendantRole";
        assertContext(CLS_NM, methodName, parentRole, GlobalErrIds.ARLE_PARENT_NULL);
        assertContext(CLS_NM, methodName, childRole, GlobalErrIds.ARLE_CHILD_NULL);
        setEntitySession(CLS_NM, methodName, childRole);
        // ensure the parent AdminRole exists:
        admRP.read(parentRole);
        AdminRoleUtil.validateRelationship(childRole, parentRole, false);
        childRole.setParent(parentRole.getName());
        admRP.add(childRole);
        AdminRoleUtil.updateHier(this.contextId, new Relationship(childRole.getName().toUpperCase(), parentRole.getName().toUpperCase()), Hier.Op.ADD);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void addAscendant(AdminRole childRole, AdminRole parentRole)
        throws SecurityException
    {
        String methodName = "addAscendantRole";
        assertContext(CLS_NM, methodName, parentRole, GlobalErrIds.ARLE_PARENT_NULL);
        setEntitySession(CLS_NM, methodName, parentRole);
        assertContext(CLS_NM, methodName, childRole, GlobalErrIds.ARLE_CHILD_NULL);
        // ensure the child AdminRole exists:
        AdminRole newChild = admRP.read(childRole);
        AdminRoleUtil.validateRelationship(childRole, parentRole, false);
        admRP.add(parentRole);

        // Use cRole2 to update ONLY the parents attribute on the child role and nothing else:
        AdminRole cRole2 = new AdminRole(childRole.getName());
        cRole2.setParents(newChild.getParents());
        cRole2.setParent(parentRole.getName());
        cRole2.setContextId(this.contextId);
        setAdminData(CLS_NM, methodName, cRole2);
        admRP.update(cRole2);
        AdminRoleUtil.updateHier(this.contextId, new Relationship(childRole.getName().toUpperCase(), parentRole.getName().toUpperCase()), Hier.Op.ADD);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void addInheritance(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "addInheritanceRole";
        assertContext(CLS_NM, methodName, parentRole, GlobalErrIds.ARLE_PARENT_NULL);
        assertContext(CLS_NM, methodName, childRole, GlobalErrIds.ARLE_CHILD_NULL);
        setEntitySession(CLS_NM, methodName, parentRole);
        // make sure the parent role is already there:
        admRP.read(parentRole);
        AdminRoleUtil.validateRelationship(childRole, parentRole, false);
        // make sure the child role is already there:
        AdminRole cRole = new AdminRole(childRole.getName());
        cRole.setContextId(this.contextId);
        cRole = admRP.read(cRole);
        // Use cRole2 to update ONLY the parents attribute on the child role and nothing else:
        AdminRole cRole2 = new AdminRole(childRole.getName());
        cRole2.setParents(cRole.getParents());
        cRole2.setParent(parentRole.getName());
        cRole2.setContextId(this.contextId);
        setAdminData(CLS_NM, methodName, cRole2);
        AdminRoleUtil.updateHier(this.contextId, new Relationship(childRole.getName().toUpperCase(), parentRole.getName().toUpperCase()), Hier.Op.ADD);
        admRP.update(cRole2);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void deleteInheritance(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "deleteInheritanceRole";
        assertContext(CLS_NM, methodName, parentRole, GlobalErrIds.ARLE_PARENT_NULL);
        assertContext(CLS_NM, methodName, childRole, GlobalErrIds.ARLE_CHILD_NULL);
        setEntitySession(CLS_NM, methodName, parentRole);
        AdminRoleUtil.validateRelationship(childRole, parentRole, true);
        AdminRoleUtil.updateHier(this.contextId, new Relationship(childRole.getName().toUpperCase(), parentRole.getName().toUpperCase()), Hier.Op.REM);
        // need to remove the parent from the child role:
        AdminRole cRole = new AdminRole(childRole.getName());
        cRole.setContextId(this.contextId);
        cRole = admRP.read(cRole);
        // Use cRole2 to update ONLY the parents attribute on the child role and nothing else:
        AdminRole cRole2 = new AdminRole(childRole.getName());
        cRole2.setParents(cRole.getParents());
        cRole2.delParent(parentRole.getName());
        cRole2.setContextId(this.contextId);
        setAdminData(CLS_NM, methodName, cRole2);
        // are there any parents left?
        if(!CollectionUtils.isNotEmpty( cRole2.getParents() ))
        {
            // The updates only update non-empty multivalued attributes
            // so if last parent assigned, so must remove the attribute completely:
            admRP.deleteParent(cRole2);
        }
        else
        {
            admRP.update(cRole2);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Permission addPermission(Permission perm)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        perm.setAdmin(true);
        return adminMgr.addPermission(perm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Permission updatePermission(Permission perm)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        perm.setAdmin(true);
        return adminMgr.updatePermission(perm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePermission(Permission perm)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        perm.setAdmin(true);
        adminMgr.deletePermission(perm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PermObj addPermObj(PermObj pObj)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        pObj.setAdmin(true);
        return adminMgr.addPermObj(pObj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PermObj updatePermObj(PermObj pObj)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        pObj.setAdmin(true);
        return adminMgr.updatePermObj(pObj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePermObj(PermObj pObj)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        pObj.setAdmin(true);
        adminMgr.deletePermObj(pObj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void grantPermission(Permission perm, AdminRole role)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        perm.setAdmin(true);
        adminMgr.grantPermission(perm, role);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void revokePermission(Permission perm, AdminRole role)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        perm.setAdmin(true);
        adminMgr.revokePermission(perm, role);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void grantPermission(Permission perm, User user)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        perm.setAdmin(true);
        adminMgr.grantPermission(perm, user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void revokePermission(Permission perm, User user)
        throws SecurityException
    {
        final AdminMgr adminMgr = AdminMgrFactory.createInstance(this.contextId);
        perm.setAdmin(true);
        adminMgr.revokePermission(perm, user);
    }
}

