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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.fortress.annotation.AdminPermissionOperation;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.ReviewMgr;
import org.apache.directory.fortress.core.SecurityException;
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
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * This class performs administrative review functions on already provisioned Fortress RBAC entities
 * that reside in LDAP directory.  These APIs map directly to similar named APIs specified by ANSI and NIST RBAC models.
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
 * <hr>`
 * <p>
 * This class is NOT thread safe if parent instance variables ({@link #contextId} or {@link #adminSess}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class ReviewMgrImpl extends Manageable implements ReviewMgr, Serializable
{
    private static final String CLS_NM = ReviewMgrImpl.class.getName();
    private UserP userP = new UserP();
    private RoleP roleP = new RoleP();
    private PermP permP = new PermP();
    private SdP ssdP = new SdP();

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Permission readPermission(Permission permission)
        throws SecurityException
    {
        String methodName = "readPermission";
        assertContext(CLS_NM, methodName, permission, GlobalErrIds.PERM_OPERATION_NULL);
        VUtil.assertNotNullOrEmpty(permission.getObjName(), GlobalErrIds.PERM_OBJECT_NM_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNullOrEmpty(permission.getOpName(), GlobalErrIds.PERM_OPERATION_NM_NULL, CLS_NM + "." + methodName);
        checkAccess(CLS_NM, methodName);
        return permP.read(permission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public PermObj readPermObj(PermObj permObj)
        throws SecurityException
    {
        String methodName = "readPermObj";
        assertContext( CLS_NM, methodName, permObj, GlobalErrIds.PERM_OBJECT_NULL );
        VUtil.assertNotNull( permObj.getObjName(), GlobalErrIds.PERM_OBJECT_NM_NULL, CLS_NM + "." + methodName );
        checkAccess(CLS_NM, methodName);
        return permP.read(permObj);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public PermissionAttributeSet readPermAttributeSet( PermissionAttributeSet permAttributeSet )
            throws SecurityException
    {
        String methodName = "readPermAttributeSet";
        assertContext( CLS_NM, methodName, permAttributeSet, GlobalErrIds.PERM_ATTRIBUTE_SET_NULL );
        VUtil.assertNotNull( permAttributeSet.getName(), GlobalErrIds.PERM_ATTRIBUTE_SET_NM_NULL, CLS_NM + "." + methodName );
        checkAccess(CLS_NM, methodName);
        
        return permP.read(permAttributeSet);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<Permission> findPermissions(Permission permission)
        throws SecurityException
    {
        String methodName = "findPermissions";
        assertContext( CLS_NM, methodName, permission, GlobalErrIds.PERM_OPERATION_NULL );
        checkAccess(CLS_NM, methodName);
        return permP.search( permission );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<Permission> findPermsByObj(PermObj permObj)
        throws SecurityException 
    {
        String methodName = "findPermsByObj";
        assertContext( CLS_NM, methodName, permObj, GlobalErrIds.PERM_OBJECT_NULL );
        VUtil.assertNotNullOrEmpty(permObj.getObjName(), GlobalErrIds.PERM_OBJECT_NM_NULL, CLS_NM + "." + methodName);
        checkAccess(CLS_NM, methodName);
        return permP.searchOperations( permObj );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<Permission> findAnyPermissions(Permission permission)
        throws SecurityException
    {
        String methodName = "findAnyPermissions";
        assertContext( CLS_NM, methodName, permission, GlobalErrIds.PERM_OPERATION_NULL );
        checkAccess(CLS_NM, methodName);
        return permP.searchAny( permission );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<PermObj> findPermObjs(PermObj permObj)
        throws SecurityException
    {
        String methodName = "findPermObjs";
        assertContext(CLS_NM, methodName, permObj, GlobalErrIds.PERM_OBJECT_NULL);
        checkAccess(CLS_NM, methodName);
        return permP.search(permObj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<PermObj> findPermObjs(OrgUnit ou)
        throws SecurityException
    {
        String methodName = "findPermObjs";
        assertContext(CLS_NM, methodName, ou, GlobalErrIds.ORG_NULL_PERM);
        checkAccess(CLS_NM, methodName);
        // pass a "false" which places no restrictions on how many records server returns.
        return permP.search(ou, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Role readRole(Role role)
        throws SecurityException
    {
        String methodName = "readRole";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        VUtil.assertNotNullOrEmpty( role.getName(), GlobalErrIds.ROLE_NM_NULL, CLS_NM + "." + methodName );
        checkAccess(CLS_NM, methodName);
        return roleP.read( role );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<Role> findRoles(String searchVal)
        throws SecurityException
    {
        String methodName = "findRoles";
        VUtil.assertNotNull( searchVal, GlobalErrIds.ROLE_NM_NULL, CLS_NM + "." + methodName );
        checkAccess( CLS_NM, methodName );
        Role role = new Role(searchVal);
        role.setContextId( this.contextId );
        return roleP.search( role );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<String> findRoles(String searchVal, int limit)
        throws SecurityException
    {
        String methodName = "findRoles";
        VUtil.assertNotNull(searchVal, GlobalErrIds.ROLE_NM_NULL, CLS_NM + "." + methodName);
        checkAccess(CLS_NM, methodName);
        Role role = new Role(searchVal);
        role.setContextId(this.contextId);
        return roleP.search(role, limit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public final User readUser(User user)
        throws SecurityException
    {
        String methodName = "readUser";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        VUtil.assertNotNullOrEmpty( user.getUserId(), GlobalErrIds.USER_ID_NULL, CLS_NM + "." + methodName );
        checkAccess( CLS_NM, methodName );
        return userP.read( user, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public final List<User> findUsers(User user)
        throws SecurityException
    {
        String methodName = "findUsers";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        checkAccess(CLS_NM, methodName);
        return userP.search( user );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<User> findUsers(OrgUnit ou)
        throws SecurityException
    {
        String methodName = "findUsers";
        assertContext(CLS_NM, methodName, ou, GlobalErrIds.ORG_NULL_USER);
        checkAccess(CLS_NM, methodName);
        // pass a "false" which places no restrictions on how many records server returns.
        return userP.search(ou, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public final List<String> findUsers(User user, int limit)
        throws SecurityException
    {
        String methodName = "findUsers";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        checkAccess(CLS_NM, methodName);
        return userP.search(user, limit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<String> assignedUsers(Role role, int limit)
        throws SecurityException
    {
        String methodName = "assignedUsers";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess(CLS_NM, methodName);
        List<String> users = null;

        // If role occupant is set on role, get it from the role object itself:
        if( Config.getInstance().isRoleOccupant() )
        {
            Role entity = roleP.read( role );
            // this one retrieves from the role itself.
            users = entity.getOccupants();
            if ( users != null && users.size() > limit )
            {
                users = users.subList( 0, limit );
            }
        }
        // otherwise, search across the people tree for all users assigned to this role:
        else
        {
            users = userP.getAssignedUserIds( role );
        }

        // No users found for this role.
        // return empty list to caller:
        if (users == null)
        {
            users = new ArrayList<>();
        }

        return users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<User> assignedUsers(Role role)
        throws SecurityException
    {
        String methodName = "assignedUsers";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess(CLS_NM, methodName);
        return userP.getAssignedUsers(role);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<User> assignedUsers(Role role, RoleConstraint roleConstraint)
        throws SecurityException
    {
        String methodName = "assignedUsers";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess(CLS_NM, methodName);
        return userP.getAssignedUsers(role, roleConstraint);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<UserRole> assignedUsers(Role role, RCType rcType, String keyName)
        throws SecurityException
    {
        String methodName = "assignedUsers";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess(CLS_NM, methodName);
        return userP.getAssignedUsers(role, rcType, keyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<UserRole> assignedRoles(User user)
        throws SecurityException
    {
        String methodName = "assignedRoles";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        checkAccess(CLS_NM, methodName);
        User ue = userP.read(user, true);
        return ue.getRoles();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<String> assignedRoles(String userId)
        throws SecurityException
    {
        String methodName = "assignedRoles";
        VUtil.assertNotNullOrEmpty( userId, GlobalErrIds.USER_NULL, CLS_NM + "." + methodName );
        checkAccess(CLS_NM, methodName);
        User user = new User(userId);
        user.setContextId(this.contextId);
        return userP.getAssignedRoles( user );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<User> authorizedUsers(Role role)
        throws SecurityException
    {
        String methodName = "authorizedUsers";
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        checkAccess( CLS_NM, methodName );
        return userP.getAuthorizedUsers( role );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Set<String> authorizedRoles(User user)
        throws SecurityException
    {
        String methodName = "authorizedRoles";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        checkAccess(CLS_NM, methodName);
        User ue = userP.read(user, true);
        List<UserRole> roles = ue.getRoles();
        Set<String> iRoles = null;
        if (CollectionUtils.isNotEmpty( roles ))
        {
            iRoles = RoleUtil.getInstance().getInheritedRoles( roles, this.contextId );
        }
        return iRoles;
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
    @AdminPermissionOperation
    public List<Permission> rolePermissions(Role role, boolean noInheritance )
        throws SecurityException
    {
        String methodName = "rolePermissions";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess(CLS_NM, methodName);
        return permP.search( role, noInheritance );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<PermissionAttributeSet> rolePermissionAttributeSets( Role role, boolean noInhertiance )    
        throws SecurityException
    {
        Map<String, PermissionAttributeSet> permAttributeSets = new HashMap<String, PermissionAttributeSet>();
        //look through all permissions in the role
        List<Permission> permissions = this.rolePermissions( role, noInhertiance );
        for(Permission perm : permissions)
        {
            if( CollectionUtils.isNotEmpty(perm.getPaSets() ))
            {
                for(String paSetName : perm.getPaSets())
                {
                    if(!permAttributeSets.containsKey( paSetName ))
                    {
                        PermissionAttributeSet paSet = new PermissionAttributeSet( paSetName );
                        paSet.setContextId( this.contextId );
                        PermissionAttributeSet permAttributeSet = permP.read( paSet );
                        permAttributeSets.put( paSetName, permAttributeSet );
                    }
                }
            }
        }
        return new ArrayList<>(permAttributeSets.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<Permission> userPermissions(User user)
        throws SecurityException
    {
        String methodName = "userPermissions";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        checkAccess(CLS_NM, methodName);
        user = readUser(user);
        user.setContextId(this.contextId);
        return permP.search(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<String> permissionRoles(Permission perm)
        throws SecurityException
    {
        String methodName = "permissionRoles";
        assertContext(CLS_NM, methodName, perm, GlobalErrIds.PERM_OBJECT_NULL);
        checkAccess(CLS_NM, methodName);
        Permission pe = permP.read(perm);
        List<String> retVals;
        if(pe != null && CollectionUtils.isNotEmpty( pe.getRoles() ))
        {
            retVals = new ArrayList<>(pe.getRoles());
        }
        else
        {
            retVals =  new ArrayList<>();
        }
        return retVals;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Set<String> authorizedPermissionRoles(Permission perm)
        throws SecurityException
    {
        Set<String> authorizedRoles;
        String methodName = "authorizedPermissionRoles";
        assertContext(CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL);
        checkAccess(CLS_NM, methodName);
        // Pull the permission from ldap:
        Permission pe = permP.read(perm);

        // Get all roles that this permission is authorized for:
        authorizedRoles = authorizeRoles(pe.getRoles());
        return authorizedRoles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<String> permissionUsers(Permission perm)
        throws SecurityException
    {
        String methodName = "permissionUsers";
        assertContext(CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL);
        checkAccess(CLS_NM, methodName);
        Permission pe = permP.read(perm);
        List<String> retVals;
        if(pe != null && CollectionUtils.isNotEmpty( pe.getUsers() ))
        {
            retVals = new ArrayList<>(pe.getUsers());
        }
        else
        {
            retVals =  new ArrayList<>();
        }
        return retVals;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Set<String> authorizedPermissionUsers(Permission perm)
        throws SecurityException
    {
        Set<String> authorizedUsers = null;
        String methodName = "authorizedPermissionUsers";
        assertContext(CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL);
        checkAccess(CLS_NM, methodName);
        // Pull the permission from ldap:
        Permission pe = permP.read(perm);

        // Get all roles that this permission is authorized for:
        Set<String> authorizedRoles = authorizeRoles(pe.getRoles());
        if (authorizedRoles != null)
        {
            // Pull the set of users assigned to descendant or assigned roles from ldap:
            authorizedUsers = userP.getAssignedUsers(authorizedRoles, this.contextId);
        }
        // Now add any users who have been directly assigned to this permission entity:
        Set<String> assignedUsers = pe.getUsers();
        if (assignedUsers != null)
        {
            // It is possible this dataset has not yet been instantiated (if perm has no assigned roles):
            if (authorizedUsers == null)
            {
                authorizedUsers = new HashSet<>();
            }
            authorizedUsers.addAll(assignedUsers);
        }
        // The returned list includes all assigned users plus any users assigned via authorized roles.
        return authorizedUsers;
    }

    /**
     * {@inheritDoc}
     */
    private Set<String> authorizeRoles(Set<String> assignedRoles)
    {
        Set<String> authorizedRoles = null;
        if (assignedRoles != null)
        {
            // Get the descendant roles of all assigned roles from jgrapht hierarchical roles data set:
            authorizedRoles = RoleUtil.getInstance().getDescendantRoles(assignedRoles, this.contextId);
        }
        return authorizedRoles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<SDSet> ssdRoleSets(Role role)
        throws SecurityException
    {
        String methodName = "ssdRoleSets";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess(CLS_NM, methodName);
        return ssdP.search(role, SDSet.SDType.STATIC);
    }

    /**
     * {@inheritDoc}
     */
    @AdminPermissionOperation
    public List<SDSet> ssdSets(SDSet ssd)
        throws SecurityException
    {
        String methodName = "ssdSets";
        ssd.setType(SDSet.SDType.STATIC);
        assertContext(CLS_NM, methodName, ssd, GlobalErrIds.SSD_NULL);
        checkAccess(CLS_NM, methodName);
        return ssdP.search(ssd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public SDSet ssdRoleSet(SDSet set)
        throws SecurityException
    {
        String methodName = "ssdRoleSet";
        assertContext(CLS_NM, methodName, set, GlobalErrIds.SSD_NULL);
        checkAccess(CLS_NM, methodName);
        set.setType(SDSet.SDType.STATIC);
        return ssdP.read(set);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Set<String> ssdRoleSetRoles(SDSet ssd)
        throws SecurityException
    {
        String methodName = "ssdRoleSetRoles";
        assertContext(CLS_NM, methodName, ssd, GlobalErrIds.SSD_NULL);
        checkAccess(CLS_NM, methodName);
        ssd.setType(SDSet.SDType.STATIC);
        SDSet se = ssdP.read(ssd);
        return se.getMembers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public int ssdRoleSetCardinality(SDSet ssd)
        throws SecurityException
    {
        String methodName = "ssdRoleSetCardinality";
        assertContext(CLS_NM, methodName, ssd, GlobalErrIds.SSD_NULL);
        checkAccess(CLS_NM, methodName);
        SDSet se = ssdP.read(ssd);
        return se.getCardinality();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<SDSet> dsdRoleSets(Role role)
        throws SecurityException
    {
        String methodName = "dsdRoleSets";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess(CLS_NM, methodName);
        return ssdP.search(role, SDSet.SDType.DYNAMIC);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public SDSet dsdRoleSet(SDSet set)
        throws SecurityException
    {
        String methodName = "dsdRoleSet";
        assertContext(CLS_NM, methodName, set, GlobalErrIds.DSD_NULL);
        checkAccess(CLS_NM, methodName);
        set.setType(SDSet.SDType.DYNAMIC);
        return ssdP.read(set);
    }

    /**
     * {@inheritDoc}
     */
    @AdminPermissionOperation
    public List<SDSet> dsdSets(SDSet ssd)
        throws SecurityException
    {
        String methodName = "dsdSets";
        ssd.setType(SDSet.SDType.DYNAMIC);
        assertContext(CLS_NM, methodName, ssd, GlobalErrIds.DSD_NULL);
        checkAccess(CLS_NM, methodName);
        return ssdP.search(ssd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Set<String> dsdRoleSetRoles(SDSet dsd)
        throws SecurityException
    {
        String methodName = "dsdRoleSetRoles";
        assertContext(CLS_NM, methodName, dsd, GlobalErrIds.DSD_NULL);
        checkAccess(CLS_NM, methodName);
        dsd.setType(SDSet.SDType.DYNAMIC);
        SDSet se = ssdP.read(dsd);
        return se.getMembers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public int dsdRoleSetCardinality(SDSet dsd)
        throws SecurityException
    {
        String methodName = "dsdRoleSetCardinality";
        assertContext(CLS_NM, methodName, dsd, GlobalErrIds.DSD_NULL);
        checkAccess(CLS_NM, methodName);
        SDSet se = ssdP.read(dsd);
        return se.getCardinality();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<RoleConstraint> findRoleConstraints(User user, Permission permission, RoleConstraint.RCType rcType) throws SecurityException
    {
        String methodName = "findRoleConstraints";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        assertContext(CLS_NM, methodName, permission, GlobalErrIds.PERM_NULL);
        checkAccess(CLS_NM, methodName);        
        //find roles this permission is authorized for
        Permission pe = permP.read(permission);
        Set<String> authorizedRoles = authorizeRoles(pe.getRoles());

        //find role constraints for the user and the permission's pa set
        return userP.findRoleConstraints(authorizedRoles, user, rcType, pe.getPaSets());                
    }

}