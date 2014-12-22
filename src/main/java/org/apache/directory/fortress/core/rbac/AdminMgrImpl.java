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
package org.apache.directory.fortress.core.rbac;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.AdminMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.util.attr.VUtil;
import org.apache.directory.fortress.core.util.time.CUtil;


/**
 * This class performs administrative functions to provision Fortress RBAC entities into the LDAP directory.  These APIs
 * map directly to similar named APIs specified by ANSI and NIST RBAC models.
 * Many of the java doc function descriptions found below were taken directly from ANSI INCITS 359-2004.
 * The RBAC Functional specification describes administrative operations for the creation
 * and maintenance of RBAC element sets and relations; administrative review functions for
 * performing administrative queries; and system functions for creating and managing
 * RBAC attributes on user sessions and making access control decisions.
 * <p/>
 * <hr>
 * <h4>RBAC0 - Core</h4>
 * Many-to-many relationship between Users, Roles and Permissions. Selective role activation into sessions.  API to
 * add, update, delete identity data and perform identity and access control decisions during runtime operations.
 * <p/>
 * <img src="../doc-files/RbacCore.png">
 * <hr>
 * <h4>RBAC1 - General Hierarchical Roles</h4>
 * Simplifies role engineering tasks using inheritance of one or more parent roles.
 * <p/>
 * <img src="../doc-files/RbacHier.png">
 * <hr>
 * <h4>RBAC2 - Static Separation of Duty (SSD) Relations</h4>
 * Enforce mutual membership exclusions across role assignments.  Facilitate dual control policies by restricting
 * which roles may be assigned to users in combination.  SSD provide added granularity for authorization limits which
 * help enterprises meet strict compliance regulations.
 * <p/>
 * <img src="../doc-files/RbacSSD.png">
 * <hr>
 * <h4>RBAC3 - Dynamic Separation of Duty (DSD) Relations</h4>
 * Control allowed role combinations to be activated within an RBAC session.  DSD policies fine tune role policies
 * that facilitate authorization dual control and two man policy restrictions during runtime security checks.
 * <p/>
 * <img src="../doc-files/RbacDSD.png">
 * <hr>
 * <p/>
 * This class is NOT thread safe if parent instance variables ({@link #contextId} or {@link #adminSess}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class AdminMgrImpl extends Manageable implements AdminMgr
{
    private static final String CLS_NM = AdminMgrImpl.class.getName();
    private static final AdminRoleP adminP = new AdminRoleP();
    private static final PermP permP = new PermP();
    private static final RoleP roleP = new RoleP();
    private static final SdP sdP = new SdP();
    private static final UserP userP = new UserP();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    /**
     * This command creates a new RBAC user. The command is valid only if the new user is
     * not already a member of the USERS data set. The USER data set is updated. The new user
     * does not own any session at the time of its creation.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#userId} - maps to INetOrgPerson uid</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#password} - used to authenticate the User</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#ou} - contains the name of an already existing User OU node</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#pwPolicy} - contains the name of an already existing OpenLDAP password policy node</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#cn} - maps to INetOrgPerson common name attribute</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#sn} - maps to INetOrgPerson surname attribute</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#description} - maps to INetOrgPerson description attribute</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#title} - maps to INetOrgPerson title attribute</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#employeeType} - maps to INetOrgPerson employeeType attribute</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#phones} * - multi-occurring attribute maps to organizationalPerson telephoneNumber
     * attribute</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#mobiles} * - multi-occurring attribute maps to INetOrgPerson mobile attribute</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#emails} * - multi-occurring attribute maps to INetOrgPerson mail attribute</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#address} * - multi-occurring attribute maps to organizationalPerson postalAddress, st, l,
     * postalCode, postOfficeBox attributes</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#beginTime} - HHMM - determines begin hour user may activate session</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#endTime} - HHMM - determines end hour user may activate session.</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#beginDate} - YYYYMMDD - determines date when user may sign on</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#endDate} - YYYYMMDD - indicates latest date user may sign on</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day of user may sign on</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#timeout} - number in seconds of session inactivity time allowed</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.User#props} * - multi-occurring attribute contains property key and values are separated with a ':'
     * .  e.g. mykey1:myvalue1</li>
     * </ul>
     *
     * @param user User entity must contain {@link org.apache.directory.fortress.core.rbac.User#userId} and {@link org.apache.directory.fortress.core.rbac.User#ou} (required) and optional {@link
     * org.apache.directory.fortress.core.rbac.User#description},{@link org.apache.directory.fortress.core.rbac.User#roles} and many others.
     * @return Returns entity containing user data that was added.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    @Override
    public User addUser( User user ) throws SecurityException
    {
        String methodName = "addUser";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );

        // Add the User record to ldap.
        return userP.add( user );
    }


    /**
     * This command disables an existing user in the RBAC database. The command is valid
     * if and only if the user to be disabled is a member of the USERS data set. The USERS and
     * UA data sets and the assigned_users function are updated.
     * Method performs a "soft" delete.  It performs the following:
     * - sets the user status to "deleted"
     * - deassigns all roles from the user
     * - locks the user's password in LDAP
     * - revokes all perms that have been granted to user entity.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - maps to INetOrgPerson uid</li>
     * </ul>
     *
     * @param user Contains the {@link User#userId} of the User targeted for deletion.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    @Override
    public void disableUser( User user ) throws SecurityException
    {
        String methodName = "disableUser";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        // set the user's status to "deleted"
        String userDn = userP.softDelete( user );
        // lock the user out of ldap.
        userP.lock( user );
        // remove the userId attribute from any granted permission operations (if applicable).
        permP.remove( user );
        // remove the user dn occupant attribute from assigned ldap role entities.
        roleP.removeOccupant( userDn, this.contextId );
        // remove the user dn occupant attribute from assigned ldap adminRole entities.
        adminP.removeOccupant( userDn, user.getContextId() );
    }


    /**
     * This command deletes an existing user from the RBAC database. The command is valid
     * if and only if the user to be deleted is a member of the USERS data set. The USERS and
     * UA data sets and the assigned_users function are updated.
     * This method performs a "hard" delete.  It completely removes all data associated with this user from the
     * directory.
     * User entity must exist in directory prior to making this call else exception will be thrown.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - maps to INetOrgPerson uid</li>
     * </ul>
     *
     * @param user Contains the {@link User#userId} of the User targeted for deletion.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    @Override
    public void deleteUser( User user ) throws SecurityException
    {
        String methodName = "deleteUser";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        // remove the userId attribute from any granted permission operations (if applicable).
        permP.remove( user );
        // remove the user inetOrgPerson object from ldap.
        String userDn = userP.delete( user );
        // remove the user dn occupant attribute from assigned ldap role entities.
        roleP.removeOccupant( userDn, this.contextId );
        // remove the user dn occupant attribute from assigned ldap adminRole entities.
        adminP.removeOccupant( userDn, user.getContextId() );
    }


    /**
     * This method performs an update on User entity in directory.  Prior to making this call the entity must exist in
     * directory.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - maps to INetOrgPerson uid</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link User#password} - used to authenticate the User</li>
     * <li>{@link User#ou} - contains the name of an already existing User OU node</li>
     * <li>{@link User#pwPolicy} - contains the name of an already existing OpenLDAP password policy node</li>
     * <li>{@link User#cn} - maps to INetOrgPerson common name attribute</li>
     * <li>{@link User#sn} - maps to INetOrgPerson surname attribute</li>
     * <li>{@link User#description} - maps to INetOrgPerson description attribute</li>
     * <li>{@link User#phones} * - multi-occurring attribute maps to organizationalPerson telephoneNumber
     * attribute</li>
     * <li>{@link User#mobiles} * - multi-occurring attribute maps to INetOrgPerson mobile attribute</li>
     * <li>{@link User#emails} * - multi-occurring attribute maps to INetOrgPerson mail attribute</li>
     * <li>{@link User#address} * - multi-occurring attribute maps to organizationalPerson postalAddress, st, l,
     * postalCode, postOfficeBox attributes</li>
     * <li>{@link User#beginTime} - HHMM - determines begin hour user may activate session</li>
     * <li>{@link User#endTime} - HHMM - determines end hour user may activate session.</li>
     * <li>{@link User#beginDate} - YYYYMMDD - determines date when user may sign on</li>
     * <li>{@link User#endDate} - YYYYMMDD - indicates latest date user may sign on</li>
     * <li>{@link User#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     * <li>{@link User#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     * <li>{@link User#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day of user may sign on</li>
     * <li>{@link User#timeout} - number in seconds of session inactivity time allowed</li>
     * <li>{@link User#props} * - multi-occurring attribute contains property key and values are separated with a ':'
     * .  e.g. mykey1:myvalue1</li>
     * </ul>
     *
     * @param user must contain {@link User#userId} and optional entity data to update i.e. desc, ou, properties,
     *             all attributes that are not set will be ignored.
     * @return Updated user entity data.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    @Override
    public User updateUser( User user ) throws SecurityException
    {
        String methodName = "updateUser";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        return userP.update( user );
    }


    /**
     * Method will change user's password.  This method will evaluate user's password policies.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - maps to INetOrgPerson uid</li>
     * <li>{@link User#password} - contains the User's old password</li>
     * <li>newPassword - contains the User's new password</li>
     * </ul>
     *
     * @param user        contains {@link User#userId} and old user password {@link User#password}.
     * @param newPassword contains new user password.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          Will be thrown in the event of password policy violation or system error.
     */
    @Override
    public void changePassword( User user, char[] newPassword ) throws SecurityException
    {
        String methodName = "changePassword";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        VUtil.assertNotNullOrEmpty( newPassword, GlobalErrIds.USER_PW_NULL, CLS_NM + methodName );
        userP.changePassword( user, newPassword );
    }


    /**
     * Method will lock user's password which will prevent the user from authenticating with directory.
     * <p/>
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - maps to INetOrgPerson uid</li>
     * </ul>
     *
     * @param user entity contains {@link User#userId} of User to be locked.
     * @throws SecurityException will be thrown in the event of pw policy violation or system error.
     */
    @Override
    public void lockUserAccount( User user ) throws SecurityException
    {
        String methodName = "lockUserAccount";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        userP.lock( user );
    }


    /**
     * Method will unlock user's password which will enable user to authenticate with directory.
     * <p/>
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - maps to INetOrgPerson uid</li>
     * </ul>
     *
     * @param user entity contains {@link User#userId} of User to be unlocked.
     * @throws SecurityException will be thrown in the event of pw policy violation or system error.
     */
    @Override
    public void unlockUserAccount( User user ) throws SecurityException
    {
        String methodName = "unlockUserAccount";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        userP.unlock( user );
    }


    /**
     * Method will reset user's password which will require user to change password before successful authentication
     * with directory.
     * This method will not evaluate password policies on the new user password as it must be changed before use.
     * <p/>
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - maps to INetOrgPerson uid</li>
     * <li>newPassword - contains the User's new password</li>
     * </ul>
     *
     * @param user entity contains {@link User#userId} of User to be reset.
     * @throws SecurityException will be thrown in the event of pw policy violation or system error.
     */
    @Override
    public void resetPassword( User user, char[] newPassword ) throws SecurityException
    {
        String methodName = "resetPassword";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        VUtil.assertNotNullOrEmpty( newPassword, GlobalErrIds.USER_PW_NULL, CLS_NM + "." + methodName );
        setEntitySession( CLS_NM, methodName, user );
        user.setPassword( newPassword );
        userP.resetPassword( user );
    }


    /**
     * Method will delete user's password policy designation.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - maps to INetOrgPerson uid</li>
     * <li>newPassword - contains the User's new password</li>
     * </ul>
     *
     * @param user contains {@link User#userId}.
     * @throws SecurityException will be thrown in the event of password policy violation or system error.
     */
    @Override
    public void deletePasswordPolicy( User user ) throws SecurityException
    {
        String methodName = "deletePasswordPolicy";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        setEntitySession( CLS_NM, methodName, user );
        userP.deletePwPolicy( user );
    }


    /**
     * This command creates a new role. The command is valid if and only if the new role is not
     * already a member of the ROLES data set. The ROLES data set is updated.
     * Initially, no user or permission is assigned to the new role.
     * <p/>
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.rbac.Role#name} - contains the name to use for the Role to be created.</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.rbac.Role#description} - maps to description attribute on organizationalRole object class</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.Role#beginTime} - HHMM - determines begin hour role may be activated into user's RBAC session</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.Role#endTime} - HHMM - determines end hour role may be activated into user's RBAC session.</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.Role#beginDate} - YYYYMMDD - determines date when role may be activated into user's RBAC session</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.Role#endDate} - YYYYMMDD - indicates latest date role may be activated into user's RBAC session</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.Role#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.Role#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.Role#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated
     * into user's RBAC session</li>
     * </ul>
     *
     * @param role must contains {@link org.apache.directory.fortress.core.rbac.Role#name} (required) and optional {@link org.apache.directory.fortress.core.rbac.Role#description}.
     * @return Role contains reference to entity operated on.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    @Override
    public Role addRole( Role role ) throws SecurityException
    {
        String methodName = "addRole";
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        setEntitySession( CLS_NM, methodName, role );
        return roleP.add( role );
    }


    /**
     * This command deletes an existing role from the RBAC database. The command is valid
     * if and only if the role to be deleted is a member of the ROLES data set.  This command will
     * also deassign role from all users.
     * <p/>
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role to be deleted.</li>
     * </ul>
     *
     * @param role Contains {@link Role#name} for Role to delete.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    @Override
    public void deleteRole( Role role ) throws SecurityException
    {
        String methodName = "deleteRole";
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        setEntitySession( CLS_NM, methodName, role );
        int numChildren = RoleUtil.numChildren( role.getName(), role.getContextId() );
        if ( numChildren > 0 )
        {
            String error =  methodName + " role [" + role.getName() + "] must remove [" + numChildren +
                "] descendants before deletion";
            LOG.error( error );
            throw new SecurityException( GlobalErrIds.HIER_DEL_FAILED_HAS_CHILD, error, null );
        }
        // search for all users assigned this role and deassign:
        //role.setContextId(this.contextId);
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
        permP.remove( role );
        // remove all parent relationships from the role graph:
        Set<String> parents = RoleUtil.getParents( role.getName(), this.contextId );
        if ( parents != null )
        {
            for ( String parent : parents )
            {
                RoleUtil.updateHier( this.contextId, new Relationship( role.getName().toUpperCase(),
                    parent.toUpperCase() ), Hier.Op.REM );
            }
        }
        roleP.delete( role );
    }


    /**
     * Method will update a Role entity in the directory.  The role must exist prior to this call.
     * <p/>
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Role#name} - contains the name to use for the Role to be updated.</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link Role#description} - maps to description attribute on organizationalRole object class</li>
     * <li>{@link Role#beginTime} - HHMM - determines begin hour role may be activated into user's RBAC session</li>
     * <li>{@link Role#endTime} - HHMM - determines end hour role may be activated into user's RBAC session.</li>
     * <li>{@link Role#beginDate} - YYYYMMDD - determines date when role may be activated into user's RBAC session</li>
     * <li>{@link Role#endDate} - YYYYMMDD - indicates latest date role may be activated into user's RBAC session</li>
     * <li>{@link Role#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     * <li>{@link Role#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     * <li>{@link Role#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated
     * into user's RBAC session</li>
     * </ul>
     *
     * @param role must contains {@link Role#name} and may contain new description or {@link org.apache.directory.fortress.util
     * .time.Constraint}
     * @return Role contains reference to entity operated on.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of validation or system error.
     */
    @Override
    public Role updateRole( Role role ) throws SecurityException
    {
        String methodName = "updateRole";
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        setEntitySession( CLS_NM, methodName, role );
        return roleP.update( role );
    }


    /**
     * This command assigns a user to a role.
     * <p>
     * <ul>
     * <li> The command is valid if and only if:
     * <li> The user is a member of the USERS data set
     * <li> The role is a member of the ROLES data set
     * <li> The user is not already assigned to the role
     * <li> The SSD constraints are satisfied after assignment.
     * </ul>
     * </p>
     * <p>
     * Successful completion of this op, the following occurs:
     * </p>
     * <ul>
     * <li> User entity (resides in people container) has role assignment added to aux object class attached to
     * actual user record.
     * <li> Role entity (resides in role container) has userId added as role occupant.
     * <li> (optional) Temporal constraints may be associated with <code>ftUserAttrs</code> aux object class based on:
     * <ul>
     * <li> timeout - number in seconds of session inactivity time allowed.
     * <li> beginDate - YYYYMMDD - determines date when role may be activated.
     * <li> endDate - YYMMDD - indicates latest date role may be activated.
     * <li> beginLockDate - YYYYMMDD - determines beginning of enforced inactive status
     * <li> endLockDate - YYMMDD - determines end of enforced inactive status.
     * <li> beginTime - HHMM - determines begin hour role may be activated in user's session.
     * <li> endTime - HHMM - determines end hour role may be activated in user's session.*
     * <li> dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day of week role may be activated.
     * </ul>
     * </ul>
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link UserRole#name} - contains the name for already existing Role to be assigned</li>
     * <li>{@link UserRole#userId} - contains the userId for existing User</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserRole#beginTime} - HHMM - determines begin hour role may be activated into user's RBAC session</li>
     * <li>{@link UserRole#endTime} - HHMM - determines end hour role may be activated into user's RBAC session.</li>
     * <li>{@link UserRole#beginDate} - YYYYMMDD - determines date when role may be activated into user's RBAC
     * session</li>
     * <li>{@link UserRole#endDate} - YYYYMMDD - indicates latest date role may be activated into user's RBAC
     * session</li>
     * <li>{@link UserRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     * <li>{@link UserRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     * <li>{@link UserRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be
     * activated into user's RBAC session</li>
     * </ul>
     *
     * @param uRole must contain {@link UserRole#userId} and {@link UserRole#name} and optional {@code Constraints}.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of validation or system error.
     */
    @Override
    public void assignUser( UserRole uRole ) throws SecurityException
    {
        String methodName = "assignUser";
        assertContext( CLS_NM, methodName, uRole, GlobalErrIds.URLE_NULL );
        Role role = new Role( uRole.getName() );
        role.setContextId( contextId );
        User user = new User( uRole.getUserId() );
        user.setContextId( contextId );
        setEntitySession( CLS_NM, methodName, uRole );
        AdminUtil.canAssign( uRole.getAdminSession(), user, role, contextId );
        SDUtil.validateSSD( user, role );

        // Get the default constraints from role:
        role.setContextId( this.contextId );
        Role validRole = roleP.read( role );
        // if the input role entity attribute doesn't have temporal constraints set, copy from the role declaration:
        CUtil.validateOrCopy( validRole, uRole );

        // Assign the Role data to User:
        String dn = userP.assign( uRole );
        setAdminData( CLS_NM, methodName, role );
        // Assign user dn attribute to the role, this will add a single, standard attribute value,
        // called "roleOccupant", directly onto the role node:
        roleP.assign( role, dn );
    }


    /**
     * This command deletes the assignment of the User from the Role entities. The command is
     * valid if and only if the user is a member of the USERS data set, the role is a member of
     * the ROLES data set, and the user is assigned to the role.
     * Any sessions that currently have this role activated will not be effected.
     * Successful completion includes:
     * User entity in USER data set has role assignment removed.
     * Role entity in ROLE data set has userId removed as role occupant.
     * (optional) Temporal constraints will be removed from user aux object if set prior to call.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link UserRole#name} - contains the name for already existing Role to be deassigned</li>
     * <li>{@link UserRole#userId} - contains the userId for existing User</li>
     * </ul>
     *
     * @param uRole must contain {@link UserRole#userId} and {@link UserRole#name}.
     * @throws SecurityException - in the event data error in user or role objects or system error.
     */
    @Override
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
        setAdminData( CLS_NM, methodName, role );
        // Now "deassign" user dn attribute, this will remove a single, standard attribute value,
        // called "roleOccupant", from the node:
        roleP.deassign( role, dn );
    }


    /**
     * This method will add permission operation to an existing permission object which resides under {@code
     * ou=Permissions,ou=RBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may have {@link org.apache.directory.fortress.core.rbac.Role} or {@link org.apache.directory.fortress.core.rbac.User}
     * associations.  The target {@link org.apache.directory.fortress.core.rbac.Permission} must not exist prior to calling.
     * A Fortress Permission instance exists in a hierarchical, one-many relationship between its parent and itself
     * as stored in ldap tree: ({@link org.apache.directory.fortress.core.rbac.PermObj}*->{@link org.apache.directory.fortress.core.rbac.Permission}).
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.rbac.Permission#objName} - contains the name of existing object being targeted for the permission
     * add</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.Permission#opName} - contains the name of new permission operation being added</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.rbac.Permission#roles} * - multi occurring attribute contains RBAC Roles that permission operation is
     * being granted to</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.Permission#users} * - multi occurring attribute contains Users that permission operation is being
     * granted to</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.Permission#props} * - multi-occurring property key and values are separated with a ':'.  e.g.
     * mykey1:myvalue1</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.Permission#type} - any safe text</li>
     * </ul>
     *
     * @param perm must contain the object, {@link org.apache.directory.fortress.core.rbac.Permission#objName}, and operation,
     * {@link org.apache.directory.fortress.core.rbac.Permission#opName}, that identifies target along with optional other attributes..
     * @return copy of Permission entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    @Override
    public Permission addPermission( Permission perm ) throws SecurityException
    {
        String methodName = "addPermission";
        assertContext( CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL );
        setEntitySession( CLS_NM, methodName, perm );
        return permP.add( perm );
    }


    /**
     * This method will update permission operation pre-existing in target directory under {@code ou=Permissions,
     * ou=RBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may also contain {@link org.apache.directory.fortress.core.rbac.Role} or {@link org.apache.directory.fortress.core.rbac
     * .User} associations to add or remove using this function.
     * The perm operation must exist before making this call.  Only non-null attributes will be updated.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objName} - contains the name of existing object being targeted for the permission
     * update</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation being updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link Permission#roles} * - multi occurring attribute contains RBAC Roles that permission operation is
     * being granted to</li>
     * <li>{@link Permission#users} * - multi occurring attribute contains Users that permission operation is being
     * granted to</li>
     * <li>{@link Permission#props} * - multi-occurring property key and values are separated with a ':'.  e.g.
     * mykey1:myvalue1</li>
     * <li>{@link Permission#type} - any safe text</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName},
     *             that identifies target and any optional data to update.  Null or empty attributes will be ignored.
     * @return copy of Permission entity.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    @Override
    public Permission updatePermission( Permission perm ) throws SecurityException
    {
        String methodName = "updatePermission";
        assertContext( CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL );
        setEntitySession( CLS_NM, methodName, perm );
        return permP.update( perm );
    }


    /**
     * This method will remove permission operation entity from permission object. A Fortress permission is
     * (object->operation).
     * The perm operation must exist before making this call.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objName} - contains the name of existing object being targeted for the permission
     * delete</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation being removed</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName},
     *             that identifies target.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    @Override
    public void deletePermission( Permission perm ) throws SecurityException
    {
        String methodName = "deletePermission";
        assertContext( CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL );
        setEntitySession( CLS_NM, methodName, perm );
        permP.delete( perm );
    }


    /**
     * This method will add permission object to perms container in directory. The perm object must not exist before
     * making this call.
     * A {@link org.apache.directory.fortress.core.rbac.PermObj} instance exists in a hierarchical, one-many relationship between itself and children as
     * stored in ldap tree: ({@link org.apache.directory.fortress.core.rbac.PermObj}*->{@link Permission}).
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.rbac.PermObj#objName} - contains the name of new object being added</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.PermObj#ou} - contains the name of an existing PERMS OrgUnit this object is associated with</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.rbac.PermObj#description} - any safe text</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.PermObj#type} - contains any safe text</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.PermObj#props} * - multi-occurring property key and values are separated with a ':'.  e.g.
     * mykey1:myvalue1</li>
     * </ul>
     *
     * @param pObj must contain the {@link org.apache.directory.fortress.core.rbac.PermObj#objName} and {@link org.apache.directory.fortress.core.rbac.PermObj#ou}.  The other attributes are
     *             optional.
     * @return copy of PermObj entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    @Override
    public PermObj addPermObj( PermObj pObj ) throws SecurityException
    {
        String methodName = "addPermObj";
        assertContext( CLS_NM, methodName, pObj, GlobalErrIds.PERM_OBJECT_NULL );
        setEntitySession( CLS_NM, methodName, pObj );
        return permP.add( pObj );
    }


    /**
     * This method will update permission object in perms container in directory.  The perm object must exist before
     * making this call.
     * A {@link PermObj} instance exists in a hierarchical, one-many relationship between itself and children as
     * stored in ldap tree: ({@link PermObj}*->{@link Permission}).
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link PermObj#objName} - contains the name of existing object being updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link PermObj#ou} - contains the name of an existing PERMS OrgUnit this object is associated with</li>
     * <li>{@link PermObj#description} - any safe text</li>
     * <li>{@link PermObj#type} - contains any safe text</li>
     * <li>{@link PermObj#props} * - multi-occurring property key and values are separated with a ':'.  e.g.
     * mykey1:myvalue1</li>
     * </ul>
     *
     * @param pObj must contain the {@link PermObj#objName}. Only non-null attributes will be updated.
     * @return copy of newly updated PermObj entity.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    @Override
    public PermObj updatePermObj( PermObj pObj ) throws SecurityException
    {
        String methodName = "updatePermObj";
        assertContext( CLS_NM, methodName, pObj, GlobalErrIds.PERM_OBJECT_NULL );
        setEntitySession( CLS_NM, methodName, pObj );
        return permP.update( pObj );
    }


    /**
     * This method will remove permission object to perms container in directory.  This method will also remove
     * in associated permission objects that are attached to this object.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link PermObj#objName} - contains the name of existing object targeted for removal</li>
     * </ul>
     *
     * @param pObj must contain the {@link PermObj#objName} of object targeted for removal.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    @Override
    public void deletePermObj( PermObj pObj ) throws SecurityException
    {
        String methodName = "deletePermObj";
        assertContext( CLS_NM, methodName, pObj, GlobalErrIds.PERM_OBJECT_NULL );
        setEntitySession( CLS_NM, methodName, pObj );
        permP.delete( pObj );
    }


    /**
     * This command grants a role the permission to perform an operation on an object to a role.
     * The command is implemented by granting permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * and the role is a member of the ROLES data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objName} - contains the object name</li>
     * <li>{@link Permission#opName} - contains the operation name</li>
     * <li>{@link Role#name} - contains the role name</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName},
     *             that identifies target.
     * @param role must contains {@link Role#name}.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    @Override
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
     * This command revokes the permission to perform an operation on an object from the set
     * of permissions assigned to a role. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * the role is a member of the ROLES data set, and the permission is assigned to that role.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objName} - contains the object name</li>
     * <li>{@link Permission#opName} - contains the operation name</li>
     * <li>{@link Role#name} - contains the role name</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName},
     *             that identifies target.
     * @param role must contains {@link Role#name}.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    @Override
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
     * This command grants a user the permission to perform an operation on an object to a role.
     * The command is implemented by granting permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * and the user is a member of the USERS data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objName} - contains the object name</li>
     * <li>{@link Permission#opName} - contains the operation name</li>
     * <li>{@link User#userId} - contains the userId</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName},
     *             that identifies target.
     * @param user must contain {@link User#userId} of target User entity.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    @Override
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
     * This command revokes the permission to perform an operation on an object from the set
     * of permissions assigned to a user. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * the user is a member of the USERS data set, and the permission is assigned to that user.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objName} - contains the object name</li>
     * <li>{@link Permission#opName} - contains the operation name</li>
     * <li>{@link User#userId} - contains the userId</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName},
     *             that identifies target.
     * @param user must contain {@link User#userId} of target User entity.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    @Override
    public void revokePermission( Permission perm, User user ) throws SecurityException
    {
        String methodName = "revokePermissionUser";
        assertContext( CLS_NM, methodName, perm, GlobalErrIds.PERM_OPERATION_NULL );
        setEntitySession( CLS_NM, methodName, perm );
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        permP.revoke( perm, user );
    }


    /**
     * This command creates a new role childRole, and inserts it in the role hierarchy as an immediate descendant of
     * the existing role parentRole. The command is valid if and only if childRole is not a member of the ROLES data
     * set,
     * and parentRole is a member of the ROLES data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link Role#name} - contains the name of existing Role to be parent</li>
     * <li>childRole - {@link Role#name} - contains the name of new Role to be child</li>
     * </ul>
     * <h4>optional parameters childRole</h4>
     * <ul>
     * <li>childRole - {@link Role#description} - maps to description attribute on organizationalRole object class
     * for new child</li>
     * <li>childRole - {@link Role#beginTime} - HHMM - determines begin hour role may be activated into user's RBAC
     * session for new child</li>
     * <li>childRole - {@link Role#endTime} - HHMM - determines end hour role may be activated into user's RBAC
     * session for new child</li>
     * <li>childRole - {@link Role#beginDate} - YYYYMMDD - determines date when role may be activated into user's
     * RBAC session for new child</li>
     * <li>childRole - {@link Role#endDate} - YYYYMMDD - indicates latest date role may be activated into user's RBAC
     * session for new child</li>
     * <li>childRole - {@link Role#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status for
     * new child</li>
     * <li>childRole - {@link Role#endLockDate} - YYYYMMDD - determines end of enforced inactive status for new
     * child</li>
     * <li>childRole - {@link Role#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be
     * activated into user's RBAC session for new child</li>
     * </ul>
     *
     * @param parentRole This entity must be present in ROLE data set.  Success will add role rel with childRole.
     * @param childRole  This entity must not be present in ROLE data set.  Success will add the new role entity to
     *                   ROLE data set.
     *                   This method:
     *                   1 - Adds new role.
     *                   2 - Assigns role relationship between new childRole and pre-existing parentRole.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    @Override
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
        RoleUtil.validateRelationship( childRole, parentRole, false );
        childRole.setParent( parentRole.getName() );
        roleP.add( childRole );
        RoleUtil.updateHier( this.contextId, new Relationship( childRole.getName().toUpperCase(),
            parentRole.getName().toUpperCase() ), Hier.Op.ADD );
    }


    /**
     * This command creates a new role parentRole, and inserts it in the role hierarchy as an immediate ascendant of
     * the existing role childRole. The command is valid if and only if parentRole is not a member of the ROLES data
     * set,
     * and childRole is a member of the ROLES data set.
     * This method:
     * 1 - Adds new role.
     * 2 - Assigns role relationship between new parentRole and pre-existing childRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>childRole - {@link Role#name} - contains the name of existing child Role</li>
     * <li>parentRole - {@link Role#name} - contains the name of new Role to be parent</li>
     * </ul>
     * <h4>optional parameters parentRole</h4>
     * <ul>
     * <li>parentRole - {@link Role#description} - maps to description attribute on organizationalRole object class
     * for new parent</li>
     * <li>parentRole - {@link Role#beginTime} - HHMM - determines begin hour role may be activated into user's RBAC
     * session for new parent</li>
     * <li>parentRole - {@link Role#endTime} - HHMM - determines end hour role may be activated into user's RBAC
     * session for new parent</li>
     * <li>parentRole - {@link Role#beginDate} - YYYYMMDD - determines date when role may be activated into user's
     * RBAC session for new parent</li>
     * <li>parentRole - {@link Role#endDate} - YYYYMMDD - indicates latest date role may be activated into user's
     * RBAC session for new parent</li>
     * <li>parentRole - {@link Role#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status for
     * new parent</li>
     * <li>parentRole - {@link Role#endLockDate} - YYYYMMDD - determines end of enforced inactive status for new
     * parent</li>
     * <li>parentRole - {@link Role#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be
     * activated into user's RBAC session for new parent</li>
     * </ul>
     *
     * @param parentRole completion of op assigns new child relationship with childRole.
     * @param childRole  completion of op assigns new parent relationship with parentRole.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    @Override
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
        RoleUtil.validateRelationship( childRole, parentRole, false );
        roleP.add( parentRole );
        // Use cRole2 to update ONLY the parents attribute on the child role and nothing else:
        Role cRole2 = new Role( childRole.getName() );
        cRole2.setParents( role.getParents() );
        cRole2.setParent( parentRole.getName() );
        cRole2.setContextId( this.contextId );
        setAdminData( CLS_NM, methodName, cRole2 );
        roleP.update( cRole2 );
        RoleUtil.updateHier( this.contextId, new Relationship( childRole.getName().toUpperCase(),
            parentRole.getName().toUpperCase() ), Hier.Op.ADD );
    }


    /**
     * This command establishes a new immediate inheritance relationship parentRole <<-- childRole between existing
     * roles parentRole, childRole. The command is valid if and only if parentRole and childRole are members of the
     * ROLES data
     * set, parentRole is not an immediate ascendant of childRole, and childRole does not properly inherit parentRole
     * (in order to
     * avoid cycle creation).
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link Role#name} - contains the name of existing Role to be parent</li>
     * <li>childRole - {@link Role#name} - contains the name of existing Role to be child</li>
     * </ul>
     *
     * @param parentRole completion of op deassigns child relationship with childRole.
     * @param childRole  completion of op deassigns parent relationship with parentRole.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
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
        RoleUtil.validateRelationship( childRole, parentRole, false );
        RoleUtil.updateHier( this.contextId, new Relationship( childRole.getName().toUpperCase(),
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
     * This command deletes an existing immediate inheritance relationship parentRole <<-- childRole. The command is
     * valid if and only if the roles parentRole and childRole are members of the ROLES data set, and parentRole is an
     * immediate ascendant of childRole. The new inheritance relation is computed as the reflexive-transitive
     * closure of the immediate inheritance relation resulted after deleting the relationship parentRole <<-- childRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link Role#name} - contains the name of existing Role to remove parent relationship</li>
     * <li>childRole - {@link Role#name} - contains the name of existing Role to remove child relationship</li>
     * </ul>
     *
     * @param parentRole completion of op removes child relationship with childRole.
     * @param childRole  completion of op removes parent relationship with parentRole.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    @Override
    public void deleteInheritance( Role parentRole, Role childRole ) throws SecurityException
    {
        String methodName = "deleteInheritance";
        assertContext( CLS_NM, methodName, parentRole, GlobalErrIds.PARENT_ROLE_NULL );
        setEntitySession( CLS_NM, methodName, parentRole );
        assertContext( CLS_NM, methodName, childRole, GlobalErrIds.CHILD_ROLE_NULL );
        RoleUtil.validateRelationship( childRole, parentRole, true );
        RoleUtil.updateHier( this.contextId, new Relationship( childRole.getName().toUpperCase(),
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
        if ( !VUtil.isNotNullOrEmpty( cRole2.getParents() ) )
        {
            // The updates only update non-empty multi-occurring attributes
            // so if last parent assigned, so must remove the attribute completely:
            roleP.deleteParent( cRole2 );
        }
        else
        {
            roleP.update( cRole2 );
        }
    }


    /**
     * This command creates a named SSD set of roles and sets the cardinality n of its subsets
     * that cannot have common users. The command is valid if and only if:
     * 1 - the name of the SSD set is not already in use
     * 2 - all the roles in the SSD set are members of the ROLES data set
     * 3 - n is a natural number greater than or equal to 2 and less than or equal to the cardinality of the SSD role
     * set,
     * 4 - the SSD constraint for the new role set is satisfied.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.rbac.SDSet#name} - contains the name of new SSD role set to be added</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.rbac.SDSet#members} * - multi-occurring attribute contains the RBAC Role names to be added to this set</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.SDSet#cardinality} - default is 2 which is one more than maximum number of Roles that may be
     * assigned to User from a particular set</li>
     * <li>{@link org.apache.directory.fortress.core.rbac.SDSet#description} - contains any safe text</li>
     * </ul>
     *
     * @param ssdSet contains an instantiated reference to new SSD set containing, name, members,
     *               and cardinality (default 2)
     * @return reference to newly created SSDSet object.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of data validation or system error.
     */
    @Override
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
     * This command updates existing SSD set of roles and sets the cardinality n of its subsets
     * that cannot have common users.
     * <p/>
     * The command is valid if and only if:
     * <ul>
     * <li>The name of the SSD set already exists.
     * <li> All the roles in the SSD set are members of the ROLES data set.
     * <li> n is a natural number greater than or equal to 2 and less than or equal to the cardinality of the SSD
     * role set.
     * <li> The SSD constraint for the new role set is satisfied.
     * </ul>
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing SSD role set to be updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link SDSet#members} * - multi-occurring attribute contains the RBAC Role names to be added to this set</li>
     * <li>{@link SDSet#cardinality} - default is 2 which is one more than maximum number of Roles that may be
     * assigned to User from a particular set</li>
     * <li>{@link SDSet#description} - contains any safe text</li>
     * </ul>
     *
     * @param ssdSet contains an instantiated reference to existing SSD set containing, name, members,
     *               and cardinality (default 2)
     * @return reference to SSDSet object targeted for update.
     * @throws SecurityException in the event of data validation or system error.
     */
    public SDSet updateSsdSet( SDSet ssdSet ) throws SecurityException
    {
        String methodName = "updateSsdSet";
        assertContext( CLS_NM, methodName, ssdSet, GlobalErrIds.SSD_NULL );
        setEntitySession( CLS_NM, methodName, ssdSet );
        ssdSet.setType( SDSet.SDType.STATIC );
        return sdP.update( ssdSet );
    }


    /**
     * This command adds a role to a named SSD set of roles. The cardinality associated with the role set remains
     * unchanged.
     * The command is valid if and only if:
     * 1 - the SSD role set exists, and
     * 2 - the role to be added is a member of the ROLES data set but not of a member of the SSD role set, and
     * 3 - the SSD constraint is satisfied after the addition of the role to the SSD role set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of SSD role set to be modified</li>
     * <li>{@link Role#name} - contains the name of new {@link SDSet#members} to be added</li>
     * </ul>
     *
     * @param ssdSet contains an instantiated reference to new SSD set containing, name
     * @param role   contains instantiated Role object with role name field set.
     * @return reference to updated SSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    @Override
    public SDSet addSsdRoleMember( SDSet ssdSet, Role role ) throws SecurityException
    {
        String methodName = "addSsdRoleMember";
        assertContext( CLS_NM, methodName, ssdSet, GlobalErrIds.SSD_NULL );
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        setEntitySession( CLS_NM, methodName, ssdSet );
        SDSet entity = sdP.read( ssdSet );
        entity.setContextId( this.contextId );
        entity.setContextId( this.contextId );
        entity.addMember( role.getName() );
        setAdminData( CLS_NM, methodName, entity );
        SDSet ssdOut = sdP.update( entity );
        // remove any references to the old SSD from cache:
        clearSSDCache( role );
        return ssdOut;
    }


    /**
     * This command removes a role from a named SSD set of roles. The cardinality associated with the role set
     * remains unchanged.
     * The command is valid if and only if:
     * 1 - the SSD role set exists, and
     * 2 - the role to be removed is a member of the SSD role set, and
     * 3 - the cardinality associated with the SSD role set is less than the number of elements of the SSD role set.
     * Note that the SSD constraint should be satisfied after the removal of the role from the SSD role set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of SSD role set to be modified</li>
     * <li>{@link Role#name} - contains the name of existing {@link SDSet#members} to be removed</li>
     * </ul>
     *
     * @param ssdSet contains an instantiated reference to new SSD set containing name.
     * @param role   contains instantiated Role object with role name field set.
     * @return reference to updated SSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    @Override
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
     * This command deletes a SSD role set completely. The command is valid if and only if the SSD role set exists.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing SSD role set to be removed</li>
     * </ul>
     *
     * @param ssdSet contains an instantiated reference to SSD set targeted for removal.
     * @return reference to deleted SSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    @Override
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
                SDUtil.clearSsdCacheEntry( roleName, contextId );
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
        SDUtil.clearSsdCacheEntry( role.getName(), contextId );
    }


    /**
     * This command sets the cardinality associated with a given SSD role set. The command is valid if and only if:
     * 1 - the SSD role set exists, and
     * 2 - the new cardinality is a natural number greater than or equal to 2 and less than or equal to the number of
     * elements of the SSD role set, and
     * 3 - the SSD constraint is satisfied after setting the new cardinality.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of SSD role set to be modified</li>
     * <li>cardinality - contains new cardinality setting for SSD</li>
     * </ul>
     *
     * @param ssdSet      contains an instantiated reference to new SSD set containing, name
     * @param cardinality integer value contains new cardinality value for data set.
     * @return reference to updated SSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    @Override
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
     * This command creates a named DSD set of roles and sets an associated cardinality n.
     * The DSD constraint stipulates that the DSD role set cannot contain n or more roles
     * simultaneously active in the same session.  The command is valid if and only if:
     * 1 - the name of the DSD set is not already in use
     * 2 - all the roles in the DSD set are members of the ROLES data set
     * 3 - n is a natural number greater than or equal to 2 and less than or equal to the cardinality of the DSD role
     * set,
     * 4 - the DSD constraint for the new role set is satisfied.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of new DSD role set to be added</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link SDSet#members} * - multi-occurring attribute contains the RBAC Role names to be added to this set</li>
     * <li>{@link SDSet#cardinality} - default is 2 which is one more than maximum number of Roles that may be
     * assigned to User from a particular set</li>
     * <li>{@link SDSet#description} - contains any safe text</li>
     * </ul>
     *
     * @param dsdSet contains an instantiated reference to new DSD set containing, name, members,
     *               and cardinality (default 2)
     * @return reference to newly created SSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    @Override
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
     * This command updates existing DSD set of roles and sets the cardinality n of its subsets
     * that cannot have common users.
     * <p/>
     * The command is valid if and only if:
     * <ul>
     * <li>The name of the DSD set already exists.
     * <li> All the roles in the DSD set are members of the ROLES data set.
     * <li> n is a natural number greater than or equal to 2 and less than or equal to the cardinality of the DSD
     * role set.
     * <li> The DSD constraint for the new role set is satisfied.
     * </ul>
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of existing DSD role set to be updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link SDSet#members} * - multi-occurring attribute contains the RBAC Role names to be added to this set</li>
     * <li>{@link SDSet#cardinality} - default is 2 which is one more than maximum number of Roles that may be
     * assigned to User from a particular set</li>
     * <li>{@link SDSet#description} - contains any safe text</li>
     * </ul>
     *
     * @param dsdSet contains an instantiated reference to existing DSD set containing, name, members,
     *               and cardinality (default 2)
     * @return reference to DSDSet object targeted for update.
     * @throws SecurityException in the event of data validation or system error.
     */
    public SDSet updateDsdSet( SDSet dsdSet ) throws SecurityException
    {
        String methodName = "updateDsdSet";
        assertContext( CLS_NM, methodName, dsdSet, GlobalErrIds.DSD_NULL );
        setEntitySession( CLS_NM, methodName, dsdSet );
        dsdSet.setType( SDSet.SDType.DYNAMIC );
        return sdP.update( dsdSet );
    }


    /**
     * This command adds a role to a named DSD set of roles. The cardinality associated with
     * the role set remains unchanged. The command is valid if and only if:
     * 1 - the DSD role set exists, and
     * 2 - the role to be added is a member of the ROLES data set but not of a member of the DSD role set, and
     * 3 - the DSD constraint is satisfied after the addition of the role to the SSD role set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of DSD role set to be modified</li>
     * <li>{@link Role#name} - contains the name of new {@link SDSet#members} to be added</li>
     * </ul>
     *
     * @param dsdSet contains an instantiated reference to new DSD set containing, name
     * @param role   contains instantiated Role object with role name field set.
     * @return reference to updated DSDSet object.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of data validation or system error.
     */
    @Override
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
        entity.setContextId( contextId );
        SDSet dsdOut = sdP.update( entity );
        // remove any references to the old DSD from cache:
        clearDSDCache( dsdSet );
        return dsdOut;
    }


    /**
     * This command removes a role from a named DSD set of roles. The cardinality associated
     * with the role set remains unchanged. The command is valid if and only if:
     * 1 - the DSD role set exists, and
     * 2 - the role to be removed is a member of the DSD role set, and
     * 3 - the cardinality associated with the DSD role set is less than the number of elements of the DSD role set.
     * Note that the DSD constraint should be satisfied after the removal of the role from the DSD role set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of DSD role set to be modified</li>
     * <li>{@link Role#name} - contains the name of existing {@link SDSet#members} to be removed</li>
     * </ul>
     *
     * @param dsdSet contains an instantiated reference to new DSD set containing name.
     * @param role   contains instantiated Role object with role name field set.
     * @return reference to updated DSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    @Override
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
     * This command deletes a DSD role set completely. The command is valid if and only if the DSD role set exists.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of DSD role set to be removed</li>
     * </ul>
     *
     * @param dsdSet contains an instantiated reference to DSD set targeted for removal.
     * @return reference to deleted DSDSet object.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of data validation or system error.
     */
    @Override
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
     * h     * This command sets the cardinality associated with a given DSD role set. The command is valid if and
     * only if:
     * 1 - the SSD role set exists, and
     * 2 - the new cardinality is a natural number greater than or equal to 2 and less than or equal to the number of
     * elements of the SSD role set, and
     * 3 - the SSD constraint is satisfied after setting the new cardinality.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link SDSet#name} - contains the name of SSD role set to be modified</li>
     * <li>cardinality - contains new cardinality setting for DSD</li>
     * </ul>
     *
     * @param dsdSet      contains an instantiated reference to new DSD set containing, name
     * @param cardinality integer value contains new cardinality value for data set.
     * @return reference to updated DSDSet object.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of data validation or system error.
     */
    @Override
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
        SDUtil.clearDsdCacheEntry( dsdSet.getName(), contextId );
    }
}
