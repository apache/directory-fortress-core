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
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.fortress.annotation.AdminPermissionOperation;
import org.apache.directory.fortress.core.AdminMgr;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;

import org.apache.directory.fortress.core.model.*;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.VUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class performs administrative functions to provision Fortress RBAC entities into the LDAP directory.  These APIs
 * map directly to similar named APIs specified by ANSI and NIST RBAC models.
 * Many of the java doc function descriptions found below were taken directly from ANSI INCITS 359-2004.
 * The RBAC Functional specification describes administrative operations for the creation
 * and maintenance of RBAC element sets and relations; administrative review functions for
 * performing administrative queries; and system functions for creating and managing
 * RBAC attributes on user sessions and making access control decisions.
 * <hr>
 * <h3></h3>
 * <h4>RBAC0 - Core</h4>
 * Many-to-many relationship between Users, Roles and Permissions. Selective role activation into sessions.  API to
 * add, update, delete identity data and perform identity and access control decisions during runtime operations.
 * <p>
 * <img src="../doc-files/RbacCore.png" alt="">
 * <hr>
 * <h4>RBAC1 - General Hierarchical Roles</h4>
 * Simplifies role engineering tasks using inheritance of one or more parent roles.
 * <p>
 * <img src="../doc-files/RbacHier.png" alt="">
 * <hr>
 * <h4>RBAC2 - Static Separation of Duty (SSD) Relations</h4>
 * Enforce mutual membership exclusions across role assignments.  Facilitate dual control policies by restricting
 * which roles may be assigned to users in combination.  SSD provide added granularity for authorization limits which
 * help enterprises meet strict compliance regulations.
 * <p>
 * <img src="../doc-files/RbacSSD.png" alt="">
 * <hr>
 * <h4>RBAC3 - Dynamic Separation of Duty (DSD) Relations</h4>
 * Control allowed role combinations to be activated within an RBAC session.  DSD policies fine tune role policies
 * that facilitate authorization dual control and two man policy restrictions during runtime security checks.
 * <p>
 * <img src="../doc-files/RbacDSD.png" alt="">
 * <hr>
 * <p>
 * This class is NOT thread safe if parent instance variables ({@link #contextId} or {@link #adminSess}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class AdminMgrImpl extends Manageable implements AdminMgr, Serializable
{
    private static final String CLS_NM = AdminMgrImpl.class.getName();
    private static final AdminRoleP adminP = new AdminRoleP();
    private static final PermP permP = new PermP();
    private static final RoleP roleP = new RoleP();
    private static final SdP sdP = new SdP();
    private static final UserP userP = new UserP();
    private static final GroupP groupP = new GroupP();
    private static final ConfigP configP = new ConfigP();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public User addUser( User user ) throws SecurityException
    {
        String methodName = "addUser";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        // Perform delegated admin check:
        AdminUtil.canDo( adminSess, user, contextId, true );
        //AdminUtil.checkUser(user.getAdminSession(), user);
        // Add the User record to ldap.
        return userP.add( user );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void disableUser( User user ) throws SecurityException
    {
        String methodName = "disableUser";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        // Perform delegated admin check:
        AdminUtil.canDo( adminSess, user, contextId, false );
        // set the user's status to "deleted"
        String userDn = userP.softDelete( user );
        // lock the user out of ldap.
        userP.lock( user );
        // remove the userId attribute from any granted permission operations (if applicable).
        permP.remove( user );
        // remove the user dn occupant attribute from assigned ldap role entities.
        roleP.removeOccupant( userDn, this.contextId );
        // remove the user dn occupant attribute from assigned ldap adminRole entities.
        adminP.removeOccupant( userDn, this.contextId );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void deleteUser( User user ) throws SecurityException
    {
        String methodName = "deleteUser";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        // Perform delegated admin check:
        AdminUtil.canDo( adminSess, user, contextId, false );
        // remove the userId attribute from any granted permission operations (if applicable).
        permP.remove( user );
        // remove the user inetOrgPerson object from ldap.
        String userDn = userP.delete( user );
        // remove the user dn occupant attribute from assigned ldap role entities.
        roleP.removeOccupant( userDn, this.contextId );
        // remove the user dn occupant attribute from assigned ldap adminRole entities.
        adminP.removeOccupant( userDn, this.contextId );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public User updateUser( User user ) throws SecurityException
    {
        String methodName = "updateUser";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        // Perform delegated admin check:
        AdminUtil.canDo( adminSess, user, contextId, false );
        return userP.update( user );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void changePassword( User user, String newPassword ) throws SecurityException
    {
        String methodName = "changePassword";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        // Perform delegated admin check:
        AdminUtil.canDo( adminSess, user, contextId, false );
        VUtil.assertNotNullOrEmpty( newPassword, GlobalErrIds.USER_PW_NULL, CLS_NM + methodName );
        userP.changePassword( user, newPassword );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void lockUserAccount( User user ) throws SecurityException
    {
        String methodName = "lockUserAccount";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        // Perform delegated admin check:
        AdminUtil.canDo( adminSess, user, contextId, false );
        userP.lock( user );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void unlockUserAccount( User user ) throws SecurityException
    {
        String methodName = "unlockUserAccount";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        userP.unlock( user );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void resetPassword( User user, String newPassword ) throws SecurityException
    {
        String methodName = "resetPassword";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        VUtil.assertNotNullOrEmpty( newPassword, GlobalErrIds.USER_PW_NULL, CLS_NM + "." + methodName );
        setEntitySession( CLS_NM, methodName, user );
        // Perform delegated admin check:
        AdminUtil.canDo( adminSess, user, contextId, false );
        user.setPassword( newPassword );
        userP.resetPassword( user );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void deletePasswordPolicy( User user ) throws SecurityException
    {
        String methodName = "deletePasswordPolicy";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        // Perform delegated admin check:
        AdminUtil.canDo( adminSess, user, contextId, false );
        userP.deletePwPolicy( user );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Role addRole( Role role ) throws SecurityException
    {
        String methodName = "addRole";
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        setEntitySession( CLS_NM, methodName, role );
        return roleP.add( role );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void deleteRole( Role role ) throws SecurityException
    {
        String methodName = "deleteRole";
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        setEntitySession( CLS_NM, methodName, role );
        int numChildren = RoleUtil.getInstance().numChildren( role.getName(), role.getContextId() );
        if ( numChildren > 0 )
        {
            String error =  methodName + " role [" + role.getName() + "] must remove [" + numChildren +
                "] descendants before deletion";
            LOG.error( error );
            throw new SecurityException( GlobalErrIds.HIER_DEL_FAILED_HAS_CHILD, error, null );
        }
        // Read the Role from LDAP:
        Role outRole = roleP.read( role );
        outRole.setContextId( role.getContextId() );
        // deassign all groups assigned to this role first (because of schema's configGroup class constraints)
        List<Group> groups = groupP.roleGroups( outRole );
        for ( Group group : groups )
        {
            group.setContextId( this.contextId );
            groupP.deassign( group, outRole.getDn() );
        }

        // If user membership associated with role, remove the role object:
        if( Config.getInstance().isRoleOccupant() )
        {
            // this reads the role object itself:
            List<User> users = userP.getAssignedUsers( role );
            if ( users != null )
            {
                for ( User ue : users )
                {
                    UserRole uRole = new UserRole( ue.getUserId(), role.getName() );
                    setAdminData( CLS_NM, methodName, uRole );
                    deassignUser( uRole );
                }
            }
        }
        else
        {
            // search for all users assigned this role and deassign:
            List<String> userIds = userP.getAssignedUserIds( role );
            for ( String userId : userIds )
            {
                UserRole uRole = new UserRole( userId, role.getName() );
                setAdminData( CLS_NM, methodName, uRole );
                deassignUser( uRole );
            }
        }

        // Now remove the role association from all permissions:
        permP.remove( role );
        // remove all parent relationships from the role graph:
        Set<String> parents = RoleUtil.getInstance().getParents( role.getName(), this.contextId );
        if ( parents != null )
        {
            for ( String parent : parents )
            {
                RoleUtil.getInstance().updateHier( this.contextId, new Relationship( role.getName().toUpperCase(),
                    parent.toUpperCase() ), Hier.Op.REM );
            }
        }

        // Finally, delete the role object:
        roleP.delete( role );
   }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Role updateRole( Role role ) throws SecurityException
    {
        String methodName = "updateRole";
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        setEntitySession( CLS_NM, methodName, role );
        return roleP.update( role );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void assignUser( UserRole uRole ) throws SecurityException
    {
        String methodName = "assignUser";
        assertContext( CLS_NM, methodName, uRole, GlobalErrIds.URLE_NULL );
        VUtil.assertNotNull( uRole.getName(), GlobalErrIds.ROLE_NM_NULL, CLS_NM + methodName );
        Role role = new Role( uRole.getName() );
        role.setContextId( contextId );
        User user = new User( uRole.getUserId() );
        user.setContextId( contextId );
        setEntitySession( CLS_NM, methodName, uRole );
        AdminUtil.canAssign( uRole.getAdminSession(), user, role, contextId );
        SDUtil.getInstance().validateSSD( user, role );

        // Get the default constraints from role:
        role.setContextId( this.contextId );
        Role validRole = roleP.read( role );
        // if the input role entity attribute doesn't have temporal constraints set, copy from the role declaration:
        ConstraintUtil.validateOrCopy( validRole, uRole );
        // Assign the Role data to User:
        String dn = userP.assign( uRole );

        // If user membership associated with role, set it here:
        if( Config.getInstance().isRoleOccupant() )
        {
            setAdminData( CLS_NM, methodName, role );
            // Assign user dn attribute to the role, this will add a single, standard attribute value,
            // called "roleOccupant", directly onto the role node:
            roleP.assign( role, dn );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void enableRoleConstraint( Role role, RoleConstraint roleConstraint )
        throws SecurityException
    {        
        String methodName = "enableRoleConstraint";
        VUtil.assertNotNull( role, GlobalErrIds.ROLE_NULL, CLS_NM + methodName );
        VUtil.assertNotNull( role.getName(), GlobalErrIds.ROLE_NM_NULL, CLS_NM + methodName );
        VUtil.assertNotNull( roleConstraint, GlobalErrIds.ROLE_CONSTRAINT_NULL, CLS_NM + methodName );
        setEntitySession( CLS_NM, methodName, role );
        // This constraint type requires a global config parameter keyed by RC$tenant$role:constraint:
        String propKey = Config.getInstance().getConstraintKey( role.getName(), contextId );
        String propValue = roleConstraint.getKey();
        VUtil.assertNotNull( propValue, GlobalErrIds.ROLE_CONSTRAINT_KEY_NULL, CLS_NM + methodName );
        // Verify the role exists:
        roleP.read( role );
        Properties props = new Properties();
        props.setProperty( propKey, propValue );
        // Retrieve parameters from the config node stored in target LDAP DIT:
        String realmName = Config.getInstance().getProperty( GlobalIds.CONFIG_REALM, "DEFAULT" );
        configP.update( new Configuration(realmName, props) );
        // update in-memory:
        Config.getInstance().setProperty( propKey, propValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void disableRoleConstraint( Role role, RoleConstraint roleConstraint )
    throws SecurityException
    {
        String methodName = "disableRoleConstraint";
        setEntitySession( CLS_NM, methodName, role );
        VUtil.assertNotNull( role, GlobalErrIds.ROLE_NULL, CLS_NM + methodName );
        VUtil.assertNotNull( role.getName(), GlobalErrIds.ROLE_NM_NULL, CLS_NM + methodName );
        VUtil.assertNotNull( roleConstraint, GlobalErrIds.ROLE_CONSTRAINT_NULL, CLS_NM + methodName );
        // This constraint type requires a global config parameter keyed by RC$tenant$role:constraint:
        String propKey = Config.getInstance().getConstraintKey( role.getName(), contextId );
        String propValue = roleConstraint.getKey();
        VUtil.assertNotNull( propValue, GlobalErrIds.ROLE_CONSTRAINT_KEY_NULL, CLS_NM + methodName );
        Properties props = new Properties();
        props.setProperty( propKey, propValue );
        // Retrieve parameters from the config node stored in target LDAP DIT:
        String realmName = Config.getInstance().getProperty( GlobalIds.CONFIG_REALM, "DEFAULT" );
        configP.delete( realmName, props );
        // update in-memory:
        Config.getInstance().clearProperty( propKey );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public RoleConstraint addRoleConstraint( UserRole uRole, RoleConstraint roleConstraint )
        throws SecurityException
    {
        String methodName = "addRoleConstraint";
        assertContext( CLS_NM, methodName, uRole, GlobalErrIds.URLE_NULL );
        VUtil.assertNotNull( uRole.getName(), GlobalErrIds.ROLE_NM_NULL, CLS_NM + methodName );
        VUtil.assertNotNull( roleConstraint, GlobalErrIds.ROLE_CONSTRAINT_NULL, CLS_NM + methodName );
        setEntitySession( CLS_NM, methodName, uRole );

        if ( roleConstraint.getType() == RoleConstraint.RCType.USER )
        {
            // This constraint type requires a global config parameter keyed by RC$tenant$role:constraint:
            String propKey = Config.getInstance().getConstraintKey( uRole.getName(), contextId );
            String propValue = Config.getInstance().getProperty( propKey );
            VUtil.assertNotNull( propValue, GlobalErrIds.ROLE_CONSTRAINT_NOT_ENABLED, CLS_NM + methodName );
        }

        // Validate the user-role assignment exists:
        User user = new User( uRole.getUserId());
        user.setContextId( contextId );
        List<String> assignedRoles = userP.getAssignedRoles( user );
        assertContext( CLS_NM, methodName, uRole, GlobalErrIds.URLE_NULL );
        // Do not assign role constraint to user if not assigned the role itself.
        if ( CollectionUtils.isEmpty( assignedRoles ) || !assignedRoles.stream().anyMatch( uRole.getName()::equalsIgnoreCase) )
        {
            String error =  methodName + " user [" + uRole.getUserId() + "] not assigned role [" + uRole.getName() + "]";
            LOG.error( error );
            throw new SecurityException( GlobalErrIds.URLE_ASSIGN_NOT_EXIST, error, null );
        }
        AdminUtil.canAssign( uRole.getAdminSession(), new User( uRole.getUserId() ), new Role( uRole.getName() ),
            contextId );
        userP.assign( uRole, roleConstraint );
        return roleConstraint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void removeRoleConstraint( UserRole uRole, RoleConstraint roleConstraint )
        throws SecurityException
    {        
        String methodName = "removeRoleConstraint";
        assertContext( CLS_NM, methodName, uRole, GlobalErrIds.URLE_NULL );
        VUtil.assertNotNull( roleConstraint, GlobalErrIds.ROLE_CONSTRAINT_NULL, CLS_NM + methodName );
        VUtil.assertNotNull( uRole.getName(), GlobalErrIds.ROLE_NM_NULL, CLS_NM + methodName );
        setEntitySession( CLS_NM, methodName, uRole );
        userP.deassign( uRole, roleConstraint );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void removeRoleConstraint( UserRole uRole, String roleConstraintId )
            throws SecurityException
    {        
        String methodName = "removeRoleConstraint";
        assertContext( CLS_NM, methodName, uRole, GlobalErrIds.URLE_NULL );
        VUtil.assertNotNull( uRole.getName(), GlobalErrIds.ROLE_NM_NULL, CLS_NM + methodName );
        VUtil.assertNotNull( roleConstraintId, GlobalErrIds.ROLE_CONSTRAINT_NULL, CLS_NM + methodName );
        VUtil.assertNotNull( uRole.getName(), GlobalErrIds.ROLE_NM_NULL, CLS_NM + methodName );
        setEntitySession( CLS_NM, methodName, uRole );
        AdminUtil.canDeassign( uRole.getAdminSession(), new User( uRole.getUserId() ), new Role( uRole.getName() ), contextId );
        
        //find role constraint that needs removed
        boolean found = false;
        User user = new User(uRole.getUserId());
        user.setContextId( contextId );
        List<UserRole> userRoles = userP.read( user, true ).getRoles();
        for( UserRole ur : userRoles ){
            // find matching name
            if( ur.getName().equals( uRole.getName() ) ){
                //find matching constraint
                List<RoleConstraint> rcs = ur.getRoleConstraints();
                for( RoleConstraint rc : rcs ){
                    if( rc.getId().equals( roleConstraintId )){
                        userP.deassign( uRole, rc );
                        found = true;
                        break;
                    }
                }
            }
        }

        if( !found ){
            throw new FinderException( GlobalErrIds.RCON_NOT_FOUND, "Role constraint with id " + roleConstraintId + " not found" );
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void deassignUser( UserRole uRole ) throws SecurityException
    {
        String methodName = "deassignUser";
        assertContext( CLS_NM, methodName, uRole, GlobalErrIds.URLE_NULL );
        Role role = new Role( uRole.getName() );
        role.setContextId( contextId );
        User user = new User( uRole.getUserId() );
        setEntitySession( CLS_NM, methodName, uRole );
        AdminUtil.canDeassign( user.getAdminSession(), user, role, contextId );
        String dn = userP.deassign( uRole );
        // If user membership is assocated with role, remove role occupants:
        if( Config.getInstance().isRoleOccupant() )
        {
            setAdminData( CLS_NM, methodName, role );
            // Now "deassign" user dn attribute, this will remove a single, standard attribute value,
            // called "roleOccupant", from the node:
            roleP.deassign( role, dn );
        }
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Permission addPermission( Permission perm ) throws SecurityException
    {
        String methodName = "addPermission";
        assertContext( CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL );
        setEntitySession( CLS_NM, methodName, perm );
        return permP.add( perm );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public PermissionAttributeSet addPermissionAttributeSet( PermissionAttributeSet permAttributeSet ) throws SecurityException
    {
        String methodName = "addPermissionAttributeSet";         
        assertContext( CLS_NM, methodName, permAttributeSet, GlobalErrIds.PERM_ATTRIBUTE_SET_NULL );
        setEntitySession( CLS_NM, methodName, permAttributeSet );    
        return permP.add( permAttributeSet );
    }          
    
    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void deletePermissionAttributeSet( PermissionAttributeSet permAttributeSet ) throws SecurityException
    {
        String methodName = "deletePermissionAttributeSet";         
        assertContext( CLS_NM, methodName, permAttributeSet, GlobalErrIds.PERM_ATTRIBUTE_SET_NULL );
        setEntitySession( CLS_NM, methodName, permAttributeSet );   
        permP.delete( permAttributeSet );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public PermissionAttribute addPermissionAttributeToSet( PermissionAttribute permAttribute, String attributeSetName )
        throws SecurityException
    {
        String methodName = "addPermissionAttributeToSet";         
        assertContext( CLS_NM, methodName, permAttribute, GlobalErrIds.PERM_ATTRIBUTE_NULL );
        setEntitySession( CLS_NM, methodName, permAttribute );
        return permP.add( permAttribute, attributeSetName );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void removePermissionAttributeFromSet( PermissionAttribute permAttribute, String attributeSetName )
        throws SecurityException
    {
        String methodName = "removePermissionAttributeFromSet";
        assertContext( CLS_NM, methodName, permAttribute, GlobalErrIds.PERM_ATTRIBUTE_NULL );
        setEntitySession( CLS_NM, methodName, permAttribute );
        permP.delete( permAttribute, attributeSetName );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override    
    @AdminPermissionOperation
    public void updatePermissionAttributeInSet(PermissionAttribute permAttribute, String attributeSetName, boolean replaceValidValues) 
        throws SecurityException 
    {
        String methodName = "updatePermissionAttributeInSet"; 
        assertContext( CLS_NM, methodName, permAttribute, GlobalErrIds.PERM_ATTRIBUTE_NULL );
        setEntitySession( CLS_NM, methodName, permAttribute );
        permP.update( permAttribute, attributeSetName, replaceValidValues );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Permission updatePermission( Permission perm ) throws SecurityException
    {
        String methodName = "updatePermission";
        assertContext( CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL );
        setEntitySession( CLS_NM, methodName, perm );
        return permP.update( perm );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void deletePermission( Permission perm ) throws SecurityException
    {
        String methodName = "deletePermission";
        assertContext( CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL );
        setEntitySession( CLS_NM, methodName, perm );
        permP.delete( perm );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public PermObj addPermObj( PermObj pObj ) throws SecurityException
    {
        String methodName = "addPermObj";
        assertContext( CLS_NM, methodName, pObj, GlobalErrIds.PERM_OBJECT_NULL );
        setEntitySession( CLS_NM, methodName, pObj );
        return permP.add( pObj );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public PermObj updatePermObj( PermObj pObj ) throws SecurityException
    {
        String methodName = "updatePermObj";
        assertContext( CLS_NM, methodName, pObj, GlobalErrIds.PERM_OBJECT_NULL );
        setEntitySession( CLS_NM, methodName, pObj );
        return permP.update( pObj );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void deletePermObj( PermObj pObj ) throws SecurityException
    {
        String methodName = "deletePermObj";
        assertContext( CLS_NM, methodName, pObj, GlobalErrIds.PERM_OBJECT_NULL );
        setEntitySession( CLS_NM, methodName, pObj );
        permP.delete( pObj );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void grantPermission( Permission perm, Role role ) throws SecurityException
    {
        String methodName = "grantPermission";
        assertContext( CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL );
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        setEntitySession( CLS_NM, methodName, perm );

        // validate role
        if ( perm.isAdmin() )
        {
            AdminRole adminRole = new AdminRole( role.getName() );
            adminRole.setContextId( this.contextId );
            adminP.read( adminRole );
        }
        else
        {
            AdminUtil.canGrant( perm.getAdminSession(), role, perm, contextId );
            roleP.read( role );
        }
        permP.grant( perm, role );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void revokePermission( Permission perm, Role role ) throws SecurityException
    {
        String methodName = "revokePermission";
        assertContext( CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL );
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        setEntitySession( CLS_NM, methodName, perm );
        if ( !perm.isAdmin() )
        {
            AdminUtil.canRevoke( perm.getAdminSession(), role, perm, contextId );
        }
        permP.revoke( perm, role );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void grantPermission( Permission perm, User user ) throws SecurityException
    {
        String methodName = "grantPermissionUser";
        assertContext( CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL );
        setEntitySession( CLS_NM, methodName, perm );
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        // Ensure the user entity exists:
        userP.read( user, false );
        permP.grant( perm, user );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void revokePermission( Permission perm, User user ) throws SecurityException
    {
        String methodName = "revokePermissionUser";
        assertContext( CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL );
        setEntitySession( CLS_NM, methodName, perm );
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        permP.revoke( perm, user );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void addDescendant( Role parentRole, Role childRole ) throws SecurityException
    {
        String methodName = "addDescendant";
        assertContext( CLS_NM, methodName, parentRole, GlobalErrIds.PARENT_ROLE_NULL );
        assertContext( CLS_NM, methodName, childRole, GlobalErrIds.CHILD_ROLE_NULL );
        setEntitySession( CLS_NM, methodName, childRole );
        // make sure the parent role is already there:
        Role role = new Role( parentRole.getName() );
        role.setContextId( this.contextId );
        roleP.read( role );
        RoleUtil.getInstance().validateRelationship( childRole, parentRole, false );
        childRole.setParent( parentRole.getName() );
        roleP.add( childRole );
        RoleUtil.getInstance().updateHier( this.contextId, new Relationship( childRole.getName().toUpperCase(),
            parentRole.getName().toUpperCase() ), Hier.Op.ADD );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void addAscendant( Role childRole, Role parentRole ) throws SecurityException
    {
        String methodName = "addAscendant";
        assertContext( CLS_NM, methodName, parentRole, GlobalErrIds.PARENT_ROLE_NULL );
        setEntitySession( CLS_NM, methodName, parentRole );
        assertContext( CLS_NM, methodName, childRole, GlobalErrIds.CHILD_ROLE_NULL );
        // make sure the child role is already there:
        Role role = new Role( childRole.getName() );
        role.setContextId( this.contextId );
        role = roleP.read( role );
        role.setContextId( this.contextId );
        RoleUtil.getInstance().validateRelationship( childRole, parentRole, false );
        roleP.add( parentRole );
        // Use cRole2 to update ONLY the parents attribute on the child role and nothing else:
        Role cRole2 = new Role( childRole.getName() );
        cRole2.setParents( role.getParents() );
        cRole2.setParent( parentRole.getName() );
        cRole2.setContextId( this.contextId );
        setAdminData( CLS_NM, methodName, cRole2 );
        roleP.update( cRole2 );
        RoleUtil.getInstance().updateHier( this.contextId, new Relationship( childRole.getName().toUpperCase(),
            parentRole.getName().toUpperCase() ), Hier.Op.ADD );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void addInheritance( Role parentRole, Role childRole ) throws SecurityException
    {
        String methodName = "addInheritance";
        assertContext( CLS_NM, methodName, parentRole, GlobalErrIds.PARENT_ROLE_NULL );
        assertContext( CLS_NM, methodName, childRole, GlobalErrIds.CHILD_ROLE_NULL );
        setEntitySession( CLS_NM, methodName, parentRole );
        // make sure the parent role is already there:
        Role pRole = new Role( parentRole.getName() );
        pRole.setContextId( this.contextId );
        roleP.read( pRole );
        // make sure the child role is already there:
        Role cRole = new Role( childRole.getName() );
        cRole.setContextId( this.contextId );
        cRole = roleP.read( cRole );
        RoleUtil.getInstance().validateRelationship( childRole, parentRole, false );
        RoleUtil.getInstance().updateHier( this.contextId, new Relationship( childRole.getName().toUpperCase(),
            parentRole.getName().toUpperCase() ), Hier.Op.ADD );
        // Use cRole2 to update ONLY the parents attribute on the child role and nothing else:
        Role cRole2 = new Role( childRole.getName() );
        cRole2.setParents( cRole.getParents() );
        cRole2.setParent( parentRole.getName() );
        cRole2.setContextId( this.contextId );
        setAdminData( CLS_NM, methodName, cRole2 );
        roleP.update( cRole2 );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void deleteInheritance( Role parentRole, Role childRole ) throws SecurityException
    {
        String methodName = "deleteInheritance";
        assertContext( CLS_NM, methodName, parentRole, GlobalErrIds.PARENT_ROLE_NULL );
        setEntitySession( CLS_NM, methodName, parentRole );
        assertContext( CLS_NM, methodName, childRole, GlobalErrIds.CHILD_ROLE_NULL );
        RoleUtil.getInstance().validateRelationship( childRole, parentRole, true );
        RoleUtil.getInstance().updateHier( this.contextId, new Relationship( childRole.getName().toUpperCase(),
            parentRole.getName().toUpperCase() ), Hier.Op.REM );
        // need to remove the parent from the child role:
        Role cRole = new Role( childRole.getName() );
        cRole.setContextId( this.contextId );
        cRole = roleP.read( cRole );
        // Use cRole2 to update ONLY the parents attribute on the child role and nothing else:
        Role cRole2 = new Role( childRole.getName() );
        cRole2.setParents( cRole.getParents() );
        cRole2.delParent( parentRole.getName() );
        cRole2.setContextId( this.contextId );
        setAdminData( CLS_NM, methodName, cRole2 );
        // are there any parents left?
        if ( !CollectionUtils.isNotEmpty( cRole2.getParents() ) )
        {
            // The updates only update non-empty multivalued attributes
            // so if last parent assigned, so must remove the attribute completely:
            roleP.deleteParent( cRole2 );
        }
        else
        {
            roleP.update( cRole2 );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public SDSet createSsdSet( SDSet ssdSet ) throws SecurityException
    {
        String methodName = "createSsdSet";
        assertContext( CLS_NM, methodName, ssdSet, GlobalErrIds.SSD_NULL );
        setEntitySession( CLS_NM, methodName, ssdSet );
        ssdSet.setType( SDSet.SDType.STATIC );
        if ( ssdSet.getCardinality() == null )
        {
            // default cardinality == 2
            ssdSet.setCardinality( 2 );
        }
        clearSSDCache( ssdSet );
        return sdP.add( ssdSet );
    }


    /**
     * {@inheritDoc}
     */
    @AdminPermissionOperation
    public SDSet updateSsdSet( SDSet ssdSet ) throws SecurityException
    {
        String methodName = "updateSsdSet";
        assertContext( CLS_NM, methodName, ssdSet, GlobalErrIds.SSD_NULL );
        setEntitySession( CLS_NM, methodName, ssdSet );
        ssdSet.setType( SDSet.SDType.STATIC );
        return sdP.update( ssdSet );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public SDSet addSsdRoleMember( SDSet ssdSet, Role role ) throws SecurityException
    {
        String methodName = "addSsdRoleMember";
        assertContext( CLS_NM, methodName, ssdSet, GlobalErrIds.SSD_NULL );
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        setEntitySession( CLS_NM, methodName, ssdSet );
        SDSet entity = sdP.read( ssdSet );
        entity.setContextId( this.contextId );
        entity.addMember( role.getName() );
        setAdminData( CLS_NM, methodName, entity );
        SDSet ssdOut = sdP.update( entity );
        // remove any references to the old SSD from cache:
        clearSSDCache( role );
        return ssdOut;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public SDSet deleteSsdRoleMember( SDSet ssdSet, Role role ) throws SecurityException
    {
        String methodName = "deleteSsdRoleMember";
        assertContext( CLS_NM, methodName, ssdSet, GlobalErrIds.SSD_NULL );
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        setEntitySession( CLS_NM, methodName, ssdSet );
        SDSet entity = sdP.read( ssdSet );
        entity.setContextId( this.contextId );
        entity.delMember( role.getName() );

        // when removing last role member a placeholder must be left in data set:
        if ( entity.getMembers().isEmpty() )
        {
            entity.addMember( GlobalIds.NONE );
        }
        setAdminData( CLS_NM, methodName, entity );
        SDSet ssdOut = sdP.update( entity );
        // remove any references to the old SSD from cache:
        clearSSDCache( role );
        return ssdOut;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public SDSet deleteSsdSet( SDSet ssdSet ) throws SecurityException
    {
        String methodName = "deleteSsdSet";
        assertContext( CLS_NM, methodName, ssdSet, GlobalErrIds.SSD_NULL );
        setEntitySession( CLS_NM, methodName, ssdSet );
        ssdSet.setType( SDSet.SDType.STATIC );
        // remove any references to the old SSD from cache:
        clearSSDCache( ssdSet );
        return sdP.delete( ssdSet );
    }


    /**
     * Clear the SSD cache entries that correspond to this SSD
     *
     * @param ssdSet
     * @throws SecurityException
     */
    private void clearSSDCache( SDSet ssdSet )
    {
        if ( ssdSet.getMembers() != null )
        {
            for ( String roleName : ssdSet.getMembers() )
            {
                SDUtil.getInstance().clearSsdCacheEntry( roleName, contextId );
            }
        }
    }


    /**
     * Clear the SSD cache entries that correspond to this Role.
     *
     * @param role
     * @throws SecurityException
     */
    private void clearSSDCache( Role role )
    {
        SDUtil.getInstance().clearSsdCacheEntry( role.getName(), contextId );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public SDSet setSsdSetCardinality( SDSet ssdSet, int cardinality ) throws SecurityException
    {
        String methodName = "setSsdSetCardinality";
        assertContext( CLS_NM, methodName, ssdSet, GlobalErrIds.SSD_NULL );
        setEntitySession( CLS_NM, methodName, ssdSet );
        ssdSet.setType( SDSet.SDType.STATIC );
        ssdSet.setCardinality( cardinality );
        // remove any references to the old SSD from cache:
        clearSSDCache( ssdSet );
        return sdP.update( ssdSet );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public SDSet createDsdSet( SDSet dsdSet ) throws SecurityException
    {
        String methodName = "createDsdSet";
        assertContext( CLS_NM, methodName, dsdSet, GlobalErrIds.DSD_NULL );
        setEntitySession( CLS_NM, methodName, dsdSet );
        dsdSet.setType( SDSet.SDType.DYNAMIC );
        if ( dsdSet.getCardinality() == null )
        {
            // default cardinality == 2
            dsdSet.setCardinality( 2 );
        }
        return sdP.add( dsdSet );
    }


    /**
     * {@inheritDoc}
     */
    @AdminPermissionOperation
    public SDSet updateDsdSet( SDSet dsdSet ) throws SecurityException
    {
        String methodName = "updateDsdSet";
        assertContext( CLS_NM, methodName, dsdSet, GlobalErrIds.DSD_NULL );
        setEntitySession( CLS_NM, methodName, dsdSet );
        dsdSet.setType( SDSet.SDType.DYNAMIC );
        return sdP.update( dsdSet );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public SDSet addDsdRoleMember( SDSet dsdSet, Role role ) throws SecurityException
    {
        String methodName = "addDsdRoleMember";
        assertContext( CLS_NM, methodName, dsdSet, GlobalErrIds.DSD_NULL );
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        setEntitySession( CLS_NM, methodName, dsdSet );
        SDSet entity = sdP.read( dsdSet );
        entity.setContextId( this.contextId );
        entity.addMember( role.getName() );
        setAdminData( CLS_NM, methodName, entity );
        SDSet dsdOut = sdP.update( entity );
        // remove any references to the old DSD from cache:
        clearDSDCache( dsdSet );
        return dsdOut;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public SDSet deleteDsdRoleMember( SDSet dsdSet, Role role ) throws SecurityException
    {
        String methodName = "deleteDsdRoleMember";
        assertContext( CLS_NM, methodName, dsdSet, GlobalErrIds.DSD_NULL );
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        setEntitySession( CLS_NM, methodName, dsdSet );
        SDSet entity = sdP.read( dsdSet );
        entity.setContextId( this.contextId );
        entity.delMember( role.getName() );

        // when removing last role member a placeholder must be left in data set:
        if ( entity.getMembers().isEmpty() )
        {
            entity.addMember( GlobalIds.NONE );
        }
        setAdminData( CLS_NM, methodName, entity );
        SDSet dsdOut = sdP.update( entity );
        // remove any references to the old DSD from cache:
        clearDSDCache( dsdSet );
        return dsdOut;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public SDSet deleteDsdSet( SDSet dsdSet ) throws SecurityException
    {
        String methodName = "deleteDsdSet";
        assertContext( CLS_NM, methodName, dsdSet, GlobalErrIds.DSD_NULL );
        setEntitySession( CLS_NM, methodName, dsdSet );
        dsdSet.setType( SDSet.SDType.DYNAMIC );
        // remove any references to the old DSD from cache:
        clearDSDCache( dsdSet );
        return sdP.delete( dsdSet );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public SDSet setDsdSetCardinality( SDSet dsdSet, int cardinality ) throws SecurityException
    {
        String methodName = "setDsdSetCardinality";
        assertContext( CLS_NM, methodName, dsdSet, GlobalErrIds.DSD_NULL );
        setEntitySession( CLS_NM, methodName, dsdSet );
        dsdSet.setType( SDSet.SDType.DYNAMIC );
        dsdSet.setCardinality( cardinality );
        // remove any references to the old DSD from cache:
        clearDSDCache( dsdSet );
        return sdP.update( dsdSet );
    }


    /**
     * Clear the DSD cache entries that correspond to this DSD
     *
     * @param dsdSet
     * @throws SecurityException
     */
    private void clearDSDCache( SDSet dsdSet )
    {
        SDUtil.getInstance().clearDsdCacheEntry( dsdSet.getName(), contextId );
    }
}
