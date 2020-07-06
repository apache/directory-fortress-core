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
package org.apache.directory.fortress.core;


import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.PermissionAttribute;
import org.apache.directory.fortress.core.model.PermissionAttributeSet;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.RoleConstraint;
import org.apache.directory.fortress.core.model.SDSet;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;


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
 * Many-to-many relationship between Users, Roles and Permissions. Selective role activation into sessions.  API to add, 
 * update, delete identity data and perform identity and access control decisions during runtime operations.
 * <p>
 * <img src="./doc-files/RbacCore.png" alt="">
 * <hr>
 * <h4>RBAC1 - General Hierarchical Roles</h4>
 * Simplifies role engineering tasks using inheritance of one or more parent roles.
 * <p>
 * <img src="./doc-files/RbacHier.png" alt="">
 * <hr>
 * <h4>RBAC2 - Static Separation of Duty (SSD) Relations</h4>
 * Enforce mutual membership exclusions across role assignments.  Facilitate dual control policies by restricting which 
 * roles may be assigned to users in combination.  SSD provide added granularity for authorization limits which help 
 * enterprises meet strict compliance regulations.
 * <p>
 * <img src="./doc-files/RbacSSD.png" alt="">
 * <hr>
 * <h4>RBAC3 - Dynamic Separation of Duty (DSD) Relations</h4>
 * Control allowed role combinations to be activated within an RBAC session.  DSD policies fine tune role policies that 
 * facilitate authorization dual control and two man policy restrictions during runtime security checks.
 * <p>
 * <img src="./doc-files/RbacDSD.png" alt="">
 * <hr>
 * <p>
 * This interface's implementer will NOT be thread safe if parent instance variables 
 * ({@link Manageable#setContextId(String)} or 
 * {@link Manageable#setAdmin(org.apache.directory.fortress.core.model.Session)}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface AdminMgr extends Manageable
{
    /**
     * This command creates a new RBAC user. The command is valid only if the new user is
     * not already a member of the USERS data set. The USER data set is updated. The new user
     * does not own any session at the time of its creation.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - maps to INetOrgPerson uid</li>
     *   <li>User#password - used to authenticate the User</li>
     *   <li>User#ou - contains the name of an already existing User OU node</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>User#pwPolicy - contains the name of an already existing OpenLDAP password policy node</li>
     *   <li>User#cn - maps to INetOrgPerson common name attribute</li>
     *   <li>User#sn - maps to INetOrgPerson surname attribute</li>
     *   <li>User#description - maps to INetOrgPerson description attribute</li>
     *   <li>User#title - maps to INetOrgPerson title attribute</li>
     *   <li>User#employeeType - maps to INetOrgPerson employeeType attribute</li>
     *   <li>User#phones * - multivalued attribute maps to organizationalPerson telephoneNumber  attribute</li>
     *   <li>User#mobiles * - multivalued attribute maps to INetOrgPerson mobile attribute</li>
     *   <li>User#emails * - multivalued attribute maps to INetOrgPerson mail attribute</li>
     *   <li>
     *     User#address * - multivalued attribute maps to organizationalPerson postalAddress, st,
     *     l, postalCode, postOfficeBox attributes
     *   </li>
     *   <li>User#beginTime - HHMM - determines begin hour user may activate session</li>
     *   <li>User#endTime - HHMM - determines end hour user may activate session.</li>
     *   <li>User#beginDate - YYYYMMDD - determines date when user may sign on</li>
     *   <li>User#endDate - YYYYMMDD - indicates latest date user may sign on</li>
     *   <li>User#beginLockDate - YYYYMMDD - determines beginning of enforced inactive status</li>
     *   <li>User#endLockDate - YYYYMMDD - determines end of enforced inactive status</li>
     *   <li>User#dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day of user may sign on</li>
     *   <li>User#timeout - number (in minutes) of session inactivity time allowed</li>
     *   <li>
     *     User#props * - multivalued attribute contains property key and values are separated with a ':'.
     *     e.g. mykey1:myvalue1
     *   </li>
     *   <li>
     *     User#roles * - multivalued attribute contains the name of already existing role to assign to user
     *   </li>
     *   <li>
     *     User#adminRoles * - multivalued attribute contains the name of already existing adminRole to assign
     *     to user
     *   </li>
     * </ul>
     *
     * @param user User entity must contain User#userId and User#ou (required) and optional
     * User#description,User#roles and many others.
     * @return Returns entity containing user data that was added.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    User addUser( User user )
        throws SecurityException;


    /**
     * This command deletes an existing user from the RBAC database. The command is valid
     * if and only if the user to be deleted is a member of the USERS data set. The USERS and
     * UA data sets and the assigned_users function are updated.
     * Method performs a "soft" delete.  It performs the following:
     * <ul>
     *   <li>sets the user status to "deleted"</li>
     *   <li>deassigns all roles from the user</li>
     *   <li>locks the user's password in LDAP</li>
     *   <li>revokes all perms that have been granted to user entity.</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - maps to INetOrgPerson uid</li>
     * </ul>
     *
     * @param user Contains the User#userId of the User targeted for deletion.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    void disableUser( User user )
        throws SecurityException;


    /**
     * This command deletes an existing user from the RBAC database. The command is valid
     * if and only if the user to be deleted is a member of the USERS data set. The USERS and
     * UA data sets and the assigned_users function are updated.
     * This method performs a "hard" delete.  It completely removes all data associated with this user from the directory.
     * User entity must exist in directory prior to making this call else exception will be thrown.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - maps to INetOrgPerson uid</li>
     * </ul>
     *
     * @param user Contains the User#userId of the User targeted for deletion.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    void deleteUser( User user )
        throws SecurityException;


    /**
     * This method performs an update on User entity in directory.  Prior to making this call the entity must exist in
     * directory.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - maps to INetOrgPerson uid</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>User#password - used to authenticate the User</li>
     *   <li>User#ou - contains the name of an already existing User OU node</li>
     *   <li>User#pwPolicy - contains the name of an already existing OpenLDAP password policy node</li>
     *   <li>User#cn - maps to INetOrgPerson common name attribute</li>
     *   <li>User#sn - maps to INetOrgPerson surname attribute</li>
     *   <li>User#description - maps to INetOrgPerson description attribute</li>
     *   <li>User#title - maps to INetOrgPerson title attribute</li>
     *   <li>User#employeeType - maps to INetOrgPerson employeeType attribute</li>
     *   <li>User#phones * - multivalued attribute maps to organizationalPerson telephoneNumber  attribute</li>
     *   <li>User#mobiles * - multivalued attribute maps to INetOrgPerson mobile attribute</li>
     *   <li>User#emails * - multivalued attribute maps to INetOrgPerson mail attribute</li>
     *   <li>
     *     User#address * - multivalued attribute maps to organizationalPerson postalAddress, st, l,
     *     postalCode, postOfficeBox attributes
     *   </li>
     *   <li>User#beginTime - HHMM - determines begin hour user may activate session</li>
     *   <li>User#endTime - HHMM - determines end hour user may activate session.</li>
     *   <li>User#beginDate - YYYYMMDD - determines date when user may sign on</li>
     *   <li>User#endDate - YYYYMMDD - indicates latest date user may sign on</li>
     *   <li>User#beginLockDate - YYYYMMDD - determines beginning of enforced inactive status</li>
     *   <li>User#endLockDate - YYYYMMDD - determines end of enforced inactive status</li>
     *   <li>User#dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day of user may sign on</li>
     *   <li>User#timeout - number (in minutes) of session inactivity time allowed</li>
     *   <li>
     *     User#props * - multivalued attribute contains property key and values are separated with a ':'.
     *     e.g. mykey1:myvalue1
     *   </li>
     *   <li>
     *     User#roles * - multivalued attribute contains the name of already existing role to assign to user
     *   </li>
     *   <li>
     *     User#adminRoles * - multivalued attribute contains the name of already existing adminRole to assign
     *     to user
     *   </li>
     * </ul>
     *
     * @param user must contain User#userId and optional entity data to update i.e. desc, ou, properties, all
     * attributes that are not set will be ignored.
     * @return Updated user entity data.
     * @throws SecurityException thrown in the event of validation or system error.
     */
    User updateUser( User user )
        throws SecurityException;


    /**
     * Method will change user's password.  This method will evaluate user's password policies.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - maps to INetOrgPerson uid</li>
     *   <li>User#password - contains the User's old password</li>
     *   <li>newPassword - contains the User's new password</li>
     * </ul>
     *
     * @param user        contains User#userId and old user password User#password.
     * @param newPassword contains new user password.
     * @throws SecurityException will be thrown in the event of password policy violation or system error.
     */
    void changePassword( User user, String newPassword )
        throws SecurityException;


    /**
     * Method will lock user's password which will prevent the user from authenticating with directory.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - maps to INetOrgPerson uid</li>
     * </ul>
     *
     * @param user entity contains User#userId of User to be locked.
     * @throws SecurityException will be thrown in the event of pw policy violation or system error.
     */
    void lockUserAccount( User user )
        throws SecurityException;


    /**
     * Method will unlock user's password which will enable user to authenticate with directory.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - maps to INetOrgPerson uid</li>
     * </ul>
     *
     * @param user entity contains User#userId of User to be unlocked.
     * @throws SecurityException will be thrown in the event of pw policy violation or system error.
     */
    void unlockUserAccount( User user )
        throws SecurityException;


    /**
     * Method will reset user's password which will require user to change password before successful authentication with 
     * directory.
     * This method will not evaluate password policies on the new user password as it must be changed before use.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - maps to INetOrgPerson uid</li>
     *   <li>newPassword - contains the User's new password</li>
     * </ul>
     *
     * @param user entity contains User#userId of User to be reset.
     * @param newPassword The new password to set
     * @throws SecurityException will be thrown in the event of pw policy violation or system error.
     */
    void resetPassword( User user, String newPassword )
        throws SecurityException;


    /**
     * Method will delete user's password policy designation.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - maps to INetOrgPerson uid</li>
     *   <li>newPassword - contains the User's new password</li>
     * </ul>
     *
     * @param user  contains User#userId.
     * @throws SecurityException will be thrown in the event of password policy violation or system error.
     */
    void deletePasswordPolicy( User user )
        throws SecurityException;


    /**
     * This command creates a new role. The command is valid if and only if the new role is not
     * already a member of the ROLES data set. The ROLES data set is updated.
     * Initially, no user or permission is assigned to the new role.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name to use for the Role to be created.</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>Role#description - maps to description attribute on organizationalRole object class</li>
     *   <li>Role#beginTime - HHMM - determines begin hour role may be activated into user's RBAC session</li>
     *   <li>Role#endTime - HHMM - determines end hour role may be activated into user's RBAC session.</li>
     *   <li>Role#beginDate - YYYYMMDD - determines date when role may be activated into user's RBAC session</li>
     *   <li>Role#endDate - YYYYMMDD - indicates latest date role may be activated into user's RBAC session</li>
     *   <li>Role#beginLockDate - YYYYMMDD - determines beginning of enforced inactive status</li>
     *   <li>Role#endLockDate - YYYYMMDD - determines end of enforced inactive status</li>
     *   <li>
     *     Role#dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into
     *     user's RBAC session
     *   </li>
     * </ul>
     *
     * @param role must contains Role#name (required) and optional Role#description.
     * @return copy of the added Role 
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    Role addRole( Role role )
        throws SecurityException;


    /**
     * This command deletes an existing role from the RBAC database. The command is valid
     * if and only if the role to be deleted is a member of the ROLES data set.  This command will
     * also deassign role from all users.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name to use for the Role to be deleted.</li>
     * </ul>
     * @param role Must contain Role#name for Role to delete.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    void deleteRole( Role role )
        throws SecurityException;


    /**
     * Method will update a Role entity in the directory.  The role must exist in role container prior to this call.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name to use for the Role to be updated.</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>Role#description - maps to description attribute on organizationalRole object class</li>
     *   <li>Role#beginTime - HHMM - determines begin hour role may be activated into user's RBAC session</li>
     *   <li>Role#endTime - HHMM - determines end hour role may be activated into user's RBAC session.</li>
     *   <li>Role#beginDate - YYYYMMDD - determines date when role may be activated into user's RBAC session</li>
     *   <li>Role#endDate - YYYYMMDD - indicates latest date role may be activated into user's RBAC session</li>
     *   <li>Role#beginLockDate - YYYYMMDD - determines beginning of enforced inactive status</li>
     *   <li>Role#endLockDate - YYYYMMDD - determines end of enforced inactive status</li>
     *   <li>
     *     {Role#dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into
     *     user's RBAC session
     *   </li>
     * </ul>
     * @param role Must contains Role#name and may contain new description or
     *  {@link org.apache.directory.fortress.core.model.Constraint}
     * @return Role contains reference to entity operated on.
     * @throws SecurityException in the event of validation or system error.
     */
    Role updateRole( Role role )
        throws SecurityException;


    /**
     * This command assigns a user to a role.
     * <ul>
     *   <li> The command is valid if and only if:</li>
     *   <li>
     *     <ul>
     *       <li> The user is a member of the USERS data set</li>
     *       <li> The role is a member of the ROLES data set</li>
     *       <li> The user is not already assigned to the role</li>
     *       <li> The SSD constraints are satisfied after assignment.</li>
     *     </ul>
     *   </li>
     * </ul>
     * <p>
     * Successful completion of this op, the following occurs:
     * <ul>
     *   <li> 
     *     User entity (resides in people container) has role assignment added to aux object class attached to actual 
     *     user record.
     *   </li>
     *   <li> Role entity (resides in role container) has userId added as role occupant.</li>
     *   <li> (optional) Temporal constraints may be associated with <code>ftUserAttrs</code> aux object class based on:</li>
     *   <li>
     *     <ul>
     *       <li> timeout - number (in minutes) of session inactivity time allowed.</li>
     *       <li> beginDate - YYYYMMDD - determines date when role may be activated.</li>
     *       <li> endDate - YYMMDD - indicates latest date role may be activated.</li>
     *       <li> beginLockDate - YYYYMMDD - determines beginning of enforced inactive status</li>
     *       <li> endLockDate - YYMMDD - determines end of enforced inactive status.</li>
     *       <li> beginTime - HHMM - determines begin hour role may be activated in user's session.</li>
     *       <li> endTime - HHMM - determines end hour role may be activated in user's session.*</li>
     *       <li> dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day of week role may be activated.</li>
     *     </ul>
     *   </li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>UserRole#name - contains the name for already existing Role to be assigned</li>
     *   <li>UserRole#userId - contains the userId for existing User</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>UserRole#beginTime - HHMM - determines begin hour role may be activated into user's RBAC session</li>
     *   <li>UserRole#endTime - HHMM - determines end hour role may be activated into user's RBAC session.</li>
     *   <li>UserRole#beginDate - YYYYMMDD - determines date when role may be activated into user's RBAC session</li>
     *   <li>UserRole#endDate - YYYYMMDD - indicates latest date role may be activated into user's RBAC session</li>
     *   <li>UserRole#beginLockDate - YYYYMMDD - determines beginning of enforced inactive status</li>
     *   <li>UserRole#endLockDate - YYYYMMDD - determines end of enforced inactive status</li>
     *   <li>
     *     UserRole#dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into
     *     user's RBAC session
     *   </li>
     * </ul>
     *
     * @param uRole must contain UserRole#userId and UserRole#name and optional {@code Constraints}.
     * @throws SecurityException in the event of validation or system error.
     */
    void assignUser( UserRole uRole )
        throws SecurityException;

    /**
     * This method enables a role to be constrainted by attributes.
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name for already existing Role to be constrained</li>
     *   <li>RoleConstraint#key - contains the name of the constraint, e.g. locale, organization, or accountnumber</li>
     * </ul>
     *
     * @param role must contain Role#name
     * @param roleConstraint must contain RoleConstraint#key
     * @throws SecurityException in the event of validation or system error.
     */
    public void enableRoleConstraint( Role role, RoleConstraint roleConstraint )
        throws SecurityException;

    /**
     * This method disables a role to be constrainted by attributes.
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name for already existing Role to be unconstrained</li>
     *   <li>RoleConstraint#key - contains the name of the constraint, e.g. locale, organization, or accountnumber</li>
     * </ul>
     *
     * @param role must contain Role#name
     * @param roleConstraint must contain RoleConstraint#key
     * @throws SecurityException in the event of validation or system error.
     */
    public void disableRoleConstraint( Role role, RoleConstraint roleConstraint )
        throws SecurityException;

    /**
     * This method adds a roleConstraint (ftRC) to the user ldap entry. (ftRC=ROLE_NAME$type$CONSTRAINT_TYPE$CONSTRAINT_PASETNAME$CONSTRAINT_VALUE)
     * <h4>required parameters</h4>
     * <ul>
     *   <li>UserRole#name - contains the name for already existing Role to be assigned</li>
     *   <li>UserRole#userId - contains the userId for existing User</li>
     *   <li>RoleConstraint#type - contains the type of role constraint (filter, other)</li>
     *   <li>RoleConstraint#value - contains the value of the role constraint which is currently not validated in any way</li>
     *   <li>RoleConstraint#key - contains the name of the permission attribute set this constraint is applicable for</li>
     * </ul>
     * 
     * @param uRole must contain UserRole#userId and UserRole#name
     * @param roleConstraint must contain RoleConstraint#type, RoleConstraint#value and RoleConstraint#paSetName
     * @return RoleConstraint that was added to user role assignment
     * @throws SecurityException in the event of validation or system error.
     */
    RoleConstraint addRoleConstraint( UserRole uRole, RoleConstraint roleConstraint )
        throws SecurityException;
    
    /**
     * Thie method removes a roleConstraint (ftRC) from the user ldap entry.
     * <h4>required parameters</h4>
     * <ul>
     *   <li>UserRole#name - contains the name for already existing Role to be assigned</li>
     *   <li>UserRole#userId - contains the userId for existing User</li>
     *   <li>RoleConstraint#type - contains the type of role constraint (filter, other)</li>
     *   <li>RoleConstraint#value - contains the value of the role constraint which is currently not validated in any way</li>
     *   <li>RoleConstraint#paSetName - contains the name of the permission attribute set this constraint is applicable for</li>
     * </ul>
     * 
     * @param uRole must contain UserRole#userId and UserRole#name
     * @param roleConstraint must contain RoleConstraint#type, RoleConstraint#value and RoleConstraint#paSetName
     * @throws SecurityException in the event of validation or system error.
     */
    void removeRoleConstraint( UserRole uRole, RoleConstraint roleConstraint )
        throws SecurityException;
    
    /**
     * Thie method removes a roleConstraint (ftRC) from the user ldap entry.
     * <h4>required parameters</h4>
     * <ul>
     *   <li>UserRole#name - contains the name for already existing Role to be assigned</li>
     *   <li>UserRole#userId - contains the userId for existing User</li>
     *   <li>RoleConstraint#type - contains the type of role constraint (filter, other)</li>
     *   <li>RoleConstraint#value - contains the value of the role constraint which is currently not validated in any way</li>
     *   <li>RoleConstraint#paSetName - contains the userId for existing User, contains the name of the permission attribute set this constraint is applicable for</li>
     * </ul>
     * 
     * @param uRole must contain UserRole#userId} and UserRole#name}
     * @param roleConstraintId id of the role constraint to remove
     * @throws SecurityException in the event of validation or system error.
     */
    void removeRoleConstraint( UserRole uRole, String roleConstraintId ) 
        throws SecurityException;
    
    /**
     * This command deletes the assignment of the User from the Role entities. The command is
     * valid if and only if the user is a member of the USERS data set, the role is a member of
     * the ROLES data set, and the user is assigned to the role.
     * Any sessions that currently have this role activated will not be effected.
     * Successful completion includes:
     * <ul>
     *   <li>User entity in USER data set has role assignment removed.</li>
     *   <li>Role entity in ROLE data set has userId removed as role occupant.</li>
     *   <li>(optional) Temporal constraints will be removed from user aux object if set prior to call.</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>UserRole#name - contains the name for already existing Role to be deassigned</li>
     *   <li>UserRole#userId - contains the userId for existing User</li>
     * </ul>
     * @param uRole must contain org.apache.directory.fortress.core.model.UserRole#userId} and UserRole#name}.
     * @throws SecurityException - in the event data error in user or role objects or system error.
     */
    void deassignUser( UserRole uRole )
        throws SecurityException;


    /**
     * This method will add permission operation to an existing permission object which resides under 
     * {@code ou=Permissions,ou=RBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may have {@link org.apache.directory.fortress.core.model.Role} or 
     * {@link org.apache.directory.fortress.core.model.User} associations.  The target Permission} must not exist
     * prior to calling.
     * A Fortress Permission instance exists in a hierarchical, one-many relationship between its parent and itself as 
     * stored in ldap tree: ({@link PermObj}*-&gt;{@link Permission}).
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Permission#objName - contains the name of existing object being targeted for the permission add</li>
     *   <li>Permission#opName - contains the name of new permission operation being added</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>
     *     Permission#roles} * - multi occurring attribute contains RBAC Roles that permission operation is being
     *     granted to
     *   </li>
     *   <li>
     *     Permission#users} * - multi occurring attribute contains Users that permission operation is being granted
     *     to
     *   </li>
     *   <li>
     *     Permission#props} * - multi-occurring property key and values are separated with a ':'.  e.g.
     *     mykey1:myvalue1
     *   </li>
     *   <li>Permission#type - any safe text</li>
     * </ul>
     *
     * @param perm must contain the object, org.apache.directory.fortress.core.model.Permission#objName}, and operation, Permission#opName}, that identifies target along with optional other attributes..
     * @return copy of Permission entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    Permission addPermission( Permission perm )
        throws SecurityException;

    
    /**
     * This method will create a new permission attribute set object with resides under the 
     * {@code ou=Constraints,ou=RBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The attribute set may contain 0 to many {@link org.apache.directory.fortress.core.model.PermissionAttribute}
     * <h4>required parameters</h4>
     * <ul>
     *   <li>PermissionAttributeSet#name - contains the name of the permission attribute set</li>
     * </ul>
     * 
     * @param permAttributeSet must contain PermissionAttributeSet#name}
     * @return PermissionAttributeSet that was created
     * @throws SecurityException - thrown in the event of perm attribute set data or system error.
     */
    PermissionAttributeSet addPermissionAttributeSet( PermissionAttributeSet permAttributeSet )
        throws SecurityException;
    
    /**
     * This method will delete a permission attribute set object.
     * <h4>required parameters</h4>
     * <ul>
     *   <li>PermissionAttributeSet#name - contains the name of the permission attribute set</li>
     * </ul>
     * 
     * @param permAttributeSet must contain PermissionAttributeSet#name}
     * @throws SecurityException - thrown in the event of perm attribute set data or system error.
     */
    void deletePermissionAttributeSet( PermissionAttributeSet permAttributeSet )
        throws SecurityException;
    
    /**
     * This method adds a permission attribute (ftPA) to a permission attribute set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>PermissionAttribute#attributeName - contains the name of existing object being targeted for the permission update</li>
     *   <li>PermissionAttribute#dataType - contains the data type of the permission attribute values (string,int,long,float)</li>
     *   <li>attributeSetName - contains the name of existing permission attribute set being modified</li>
     * </ul>  
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>PermissionAttribute#required - Flag to specify this attribute is required, defaults to false.</li>
     *   <li>PermissionAttribute#operator - Can specify an operator this attribute must use.</li>
     *   <li>PermissionAttribute#validValues - CSV of valid values. Currently up to interpreting application to understand these.</li>
     *   <li>PermissionAttribute#defaultValue - A default value for the attribute value if none is specified.</li>
     * </ul>
     *   
     * @param permAttribute must contain PermissionAttribute#attributeName} and PermissionAttribute#dataType}
     * @param attributeSetName The name of the permission attribute set this ftPA should be added.
     * @return PermissionAttribute entity created
     * @throws SecurityException - thrown in the event of data or system error
     */
    PermissionAttribute addPermissionAttributeToSet( PermissionAttribute permAttribute, String attributeSetName )
        throws SecurityException;
    
    /**
     * This method removed a permission attribute (ftPA) from an existing permission attribute set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>PermissionAttribute#attributeName - contains the name of existing object being targeted for the permission update</li>
     *   <li>attributeSetName - contains the name of existing permission attribute set being modified</li>
     * </ul>     
     *
     * @param permAttribute must contain PermissionAttribute#attributeName}
     * @param attributeSetName The name of the permission attribute set this pa should be removed from
     * @throws SecurityException - thrown in the event of data or system error
     */
    void removePermissionAttributeFromSet( PermissionAttribute permAttribute, String attributeSetName )
        throws SecurityException;

    /**
     * This method updates a permission attribute (ftPA) on a permission attribute set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>PermissionAttribute#attributeName - contains the name of existing object being targeted for the permission update</li>
     *   <li>PermissionAttribute#dataType - contains the data type of the permission attribute values (string,int,long,float)</li>
     *   <li>attributeSetName - contains the name of existing permission attribute set being modified</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>PermissionAttribute#required - Flag to specify this attribute is required, defaults to false.</li>
     *   <li>PermissionAttribute#operator - Can specify an operator this attribute must use.</li>
     *   <li>PermissionAttribute#validValues - CSV of valid values. Currently up to interpreting application to understand these.</li>
     *   <li>PermissionAttribute#defaultValue - A default value for the attribute value if none is specified.</li>
     * </ul>
     *
     * @param permAttribute must contain PermissionAttribute#attributeName} and PermissionAttribute#dataType}
     * @param attributeSetName The name of the permission attribute set this ftPA should be updated.
     * @throws SecurityException - thrown in the event of data or system error
     */
    void updatePermissionAttributeInSet( PermissionAttribute permAttribute, String attributeSetName, boolean replaceValidValues )
        throws SecurityException;        
    
    /**
     * This method will update permission operation pre-existing in target directory under 
     * {@code ou=Permissions,ou=RBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may also contain {@link org.apache.directory.fortress.core.model.Role} or 
     * {@link org.apache.directory.fortress.core.model.User} associations to add or remove using this function.
     * The perm operation must exist before making this call.  Only non-null attributes will be updated.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Permission#objName - contains the name of existing object being targeted for the permission update</li>
     *   <li>Permission#opName - contains the name of existing permission operation being updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>
     *     Permission#roles} * - multi occurring attribute contains RBAC Roles that permission operation is being
     *     granted to
     *   </li>
     *   <li>
     *     Permission#users} * - multi occurring attribute contains Users that permission operation is being granted
     *     to
     *   </li>
     *   <li>
     *     Permission#props} * - multi-occurring property key and values are separated with a ':'.  e.g.
     *     mykey1:myvalue1
     *   </li>
     *   <li>Permission#type - any safe text</li>
     * </ul>
     *
     * @param perm must contain the object, Permission#objName}, and operation, Permission#opName}, that
     * identifies target and any optional data to update.  Null or empty attributes will be ignored.
     * @return copy of Permission entity.
     * @throws SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    Permission updatePermission( Permission perm )
        throws SecurityException;


    /**
     * This method will remove permission operation entity from permission object. A Fortress permission is 
     * (object-&gt;operation).
     * The perm operation must exist before making this call.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Permission#objName - contains the name of existing object being targeted for the permission delete</li>
     *   <li>Permission#opName - contains the name of existing permission operation being removed</li>
     * </ul>
     *
     * @param perm must contain the object, Permission#objName}, and operation, Permission#opName}, that
     * identifies target.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    void deletePermission( Permission perm )
        throws SecurityException;


    /**
     * This method will add permission object to perms container in directory. The perm object must not exist before making 
     * this call.
     * A {@link PermObj} instance exists in a hierarchical, one-many relationship between itself and children as stored in 
     * ldap tree: ({@link PermObj}*-&gt;Permission}).
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>PermObj#objName - contains the name of new object being added</li>
     *   <li>PermObj#ou - contains the name of an existing PERMS OrgUnit this object is associated with</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>PermObj#description - any safe text</li>
     *   <li>PermObj#type - contains any safe text</li>
     *   <li>
     *     PermObj#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1
     *   </li>
     * </ul>
     *
     * @param pObj must contain the PermObj#objName} and PermObj#ou}.  The other attributes are optional.
     * @return copy of PermObj entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    PermObj addPermObj( PermObj pObj )
        throws SecurityException;


    /**
     * This method will update permission object in perms container in directory.  The perm object must exist before making 
     * this call.
     * A {@link PermObj} instance exists in a hierarchical, one-many relationship between itself and children as stored in 
     * ldap tree: ({@link PermObj}*-&gt;{@link Permission}).
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>PermObj#objName - contains the name of existing object being updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>PermObj#ou - contains the name of an existing PERMS OrgUnit this object is associated with</li>
     *   <li>PermObj#description - any safe text</li>
     *   <li>PermObj#type - contains any safe text</li>
     *   <li>
     *     PermObj#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1
     *   </li>
     * </ul>
     *
     * @param pObj must contain the PermObj#objName}. Only non-null attributes will be updated.
     * @return copy of newly updated PermObj entity.
     * @throws SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    PermObj updatePermObj( PermObj pObj )
        throws SecurityException;


    /**
     * This method will remove permission object to perms container in directory.  This method will also remove
     * in associated permission objects that are attached to this object.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>PermObj#objName - contains the name of existing object targeted for removal</li>
     * </ul>
     *
     * @param pObj must contain the PermObj#objName} of object targeted for removal.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    void deletePermObj( PermObj pObj )
        throws SecurityException;


    /**
     * This command grants a role the permission to perform an operation on an object to a role.
     * The command is implemented by granting permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * and the role is a member of the ROLES data set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Permission#objName - contains the object name</li>
     *   <li>Permission#opName - contains the operation name</li>
     *   <li>Role#name - contains the role name</li>
     * </ul>
     *
     * @param perm must contain the object, Permission#objName}, and operation, Permission#opName}, that
     * identifies target.
     * @param role must contains Role#name}.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    void grantPermission( Permission perm, Role role )
        throws SecurityException;


    /**
     * This command revokes the permission to perform an operation on an object from the set
     * of permissions assigned to a role. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * the role is a member of the ROLES data set, and the permission is assigned to that role.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Permission#objName - contains the object name</li>
     *   <li>Permission#opName - contains the operation name</li>
     *   <li>Role#name - contains the role name</li>
     * </ul>
     *
     * @param perm must contain the object, Permission#objName}, and operation, Permission#opName}, that
     * identifies target.
     * @param role must contains Role#name}.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    void revokePermission( Permission perm, Role role )
        throws SecurityException;


    /**
     * This command grants a user the permission to perform an operation on an object to a role.
     * The command is implemented by granting permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * and the user is a member of the USERS data set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Permission#objName - contains the object name</li>
     *   <li>Permission#opName - contains the operation name</li>
     *   <li>User#userId - contains the userId</li>
     * </ul>
     *
     * @param perm must contain the object, Permission#objName}, and operation, Permission#opName},
     * that identifies target.
     * @param user must contain User#userId} of target User entity.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    void grantPermission( Permission perm, User user )
        throws SecurityException;


    /**
     * This command revokes the permission to perform an operation on an object from the set
     * of permissions assigned to a user. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * the user is a member of the USERS data set, and the permission is assigned to that user.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Permission#objName - contains the object name</li>
     *   <li>Permission#opName - contains the operation name</li>
     *   <li>User#userId - contains the userId</li>
     * </ul>
     *
     * @param perm must contain the object, Permission#objName}, and operation, Permission#opName}, that
     * identifies target.
     * @param user must contain User#userId} of target User entity.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    void revokePermission( Permission perm, User user )
        throws SecurityException;


    /**
     * This command creates a new role childRole, and inserts it in the role hierarchy as an immediate descendant of
     * the existing role parentRole.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The childRole is not a member of the ROLES data set.</li>
     *   <li>The parentRole is a member of the ROLES data set.</li>
     * </ul>
     * <p>
     * This method:
     * <ul>
     *   <li>Adds new role.</li>
     *   <li>Assigns role relationship between new childRole and pre-existing parentRole.</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>parentRole - Role#name - contains the name of existing Role to be parent</li>
     *   <li>childRole - Role#name - contains the name of new Role to be child</li>
     * </ul>
     * <h4>optional parameters childRole</h4>
     * <ul>
     *   <li>
     *     childRole - Role#description - maps to description attribute on organizationalRole object class for new
     *     child
     *   </li>
     *   <li>
     *     childRole - Role#beginTime - HHMM - determines begin hour role may be activated into user's RBAC session
     *     for new child
     *   </li>
     *   <li>
     *     childRole - Role#endTime - HHMM - determines end hour role may be activated into user's RBAC session for
     *     new child
     *   </li>
     *   <li>
     *     childRole - Role#beginDate - YYYYMMDD - determines date when role may be activated into user's RBAC
     *     session for new child
     *   </li>
     *   <li>
     *     childRole - Role#endDate - YYYYMMDD - indicates latest date role may be activated into user's RBAC
     *     session for new child
     *   </li>
     *   <li>
     *     childRole - Role#beginLockDate - YYYYMMDD - determines beginning of enforced inactive status for new
     *     child
     *   </li>
     *   <li>
     *     childRole - Role#endLockDate - YYYYMMDD - determines end of enforced inactive status for new child
     *   </li>
     *   <li>
     *     childRole - Role#dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be
     *     activated into user's RBAC session for new child
     *   </li>
     * </ul>
     *
     * @param parentRole This entity must be present in ROLE data set.  Success will add role rel with childRole.
     * @param childRole  This entity must not be present in ROLE data set.  Success will add the new role entity to ROLE
     * data set.
     * @throws SecurityException
     *          thrown in the event of data validation or system error.
     */
    void addDescendant( Role parentRole, Role childRole )
        throws SecurityException;


    /**
     * This command creates a new role parentRole, and inserts it in the role hierarchy as an immediate ascendant of
     * the existing role childRole.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The parentRole is not a member of the ROLES data set.</li>
     *   <li>The childRole is a member of the ROLES data set.</li>
     * </ul>
     * <p>
     * This method:
     * <ul>
     *   <li>Adds new role.</li>
     *   <li>Assigns role relationship between new parentRole and pre-existing childRole.</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>childRole - Role#name - contains the name of existing child Role</li>
     *   <li>parentRole - Role#name - contains the name of new Role to be parent</li>
     * </ul>
     * <h4>optional parameters parentRole</h4>
     * <ul>
     *   <li>
     *     parentRole - Role#description - maps to description attribute on organizationalRole object class for
     *     new parent
     *   </li>
     *   <li>
     *     parentRole - Role#beginTime - HHMM - determines begin hour role may be activated into user's RBAC
     *     session for new parent
     *   </li>
     *   <li>
     *     parentRole - Role#endTime - HHMM - determines end hour role may be activated into user's RBAC session
     *     for new parent
     *   </li>
     *   <li>
     *     parentRole - Role#beginDate - YYYYMMDD - determines date when role may be activated into user's RBAC
     *     session for new parent
     *   </li>
     *   <li>
     *     parentRole - Role#endDate - YYYYMMDD - indicates latest date role may be activated into user's RBAC
     *     session for new parent
     *   </li>
     *   <li>
     *     parentRole - Role#beginLockDate - YYYYMMDD - determines beginning of enforced inactive status for new
     *     parent
     *   </li>
     *   <li>
     *     parentRole - Role#endLockDate - YYYYMMDD - determines end of enforced inactive status for new parent
     *   </li>
     *   <li>
     *     parentRole - Role#dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be
     *     activated into user's RBAC session for new parent
     *   </li>
     * </ul>
     *
     * @param parentRole completion of op assigns new child relationship with childRole.
     * @param childRole  completion of op assigns new parent relationship with parentRole.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    void addAscendant( Role childRole, Role parentRole )
        throws SecurityException;


    /**
     * This command establishes a new immediate inheritance relationship parentRole &lt;&lt;-- childRole between existing
     * roles parentRole, childRole.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The parentRole and childRole are members of the ROLES data set.</li>
     *   <li>The parentRole is not an immediate ascendant of childRole.</li>
     *   <li>The childRole does not properly inherit parentRole (in order to avoid cycle creation).</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>parentRole - Role#name - contains the name of existing Role to be parent</li>
     *   <li>childRole - Role#name - contains the name of existing Role to be child</li>
     * </ul>
     *
     * @param parentRole completion of op deassigns child relationship with childRole.
     * @param childRole  completion of op deassigns parent relationship with parentRole.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    void addInheritance( Role parentRole, Role childRole )
        throws SecurityException;


    /**
     * This command deletes an existing immediate inheritance relationship parentRole &lt;&lt;-- childRole.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The roles parentRole and childRole are members of the ROLES data set.</li>
     *   <li>The parentRole is an immediate ascendant of childRole.</li>
     *   <li>
     *     The new inheritance relation is computed as the reflexive-transitive closure of the immediate inheritance
     *     relation resulted after deleting the relationship parentRole &lt;&lt;-- childRole.
     *   </li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>parentRole - Role#name - contains the name of existing Role to remove parent relationship</li>
     *   <li>childRole - Role#name - contains the name of existing Role to remove child relationship</li>
     * </ul>
     *
     * @param parentRole completion of op removes child relationship with childRole.
     * @param childRole  completion of op removes parent relationship with parentRole.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    void deleteInheritance( Role parentRole, Role childRole )
        throws SecurityException;


    /**
     * This command creates a named SSD set of roles and sets the cardinality n of its subsets
     * that cannot have common users.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The name of the SSD set is not already in use.</li>
     *   <li>All the roles in the SSD set are members of the ROLES data set.</li>
     *   <li>
     *     n is a natural number greater than or equal to 2 and less than or equal to the cardinality of the SSD role set.
     *   </li>
     *   <li>The SSD constraint for the new role set is satisfied.</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of new SSD role set to be added</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>SDSet#members} * - multivalued attribute contains the RBAC Role names to be added to this set</li>
     *   <li>
     *     SDSet#cardinality - default is 2 which is one more than maximum number of Roles that may be assigned to
     *      User from a particular set
     *    </li>
     *   <li>SDSet#description - contains any safe text</li>
     * </ul>
     *
     * @param ssdSet contains an instantiated reference to new SSD set containing, name, members, and cardinality (default 2)
     * @return reference to newly created SSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    SDSet createSsdSet( SDSet ssdSet )
        throws SecurityException;


    /**
     * This command updates existing SSD set of roles and sets the cardinality n of its subsets
     * that cannot have common users.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The name of the SSD set already exists.</li>
     *   <li>All the roles in the SSD set are members of the ROLES data set.</li>
     *   <li>
     *     n is a natural number greater than or equal to 2 and less than or equal to the cardinality of the SSD role set.
     *   </li>
     *   <li>The SSD constraint for the new role set is satisfied.</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of existing SSD role set to be updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>SDSet#members} * - multivalued attribute contains the RBAC Role names to be added to this set</li>
     *   <li>
     *     SDSet#cardinality - default is 2 which is one more than maximum number of Roles that may be assigned to
     *     User from a particular set
     *   </li>
     *   <li>SDSet#description - contains any safe text</li>
     * </ul>
     *
     * @param ssdSet contains an instantiated reference to existing SSD set containing, name, members, and cardinality 
     * (default 2)
     * @return reference to SSDSet object targeted for update.
     * @throws SecurityException in the event of data validation or system error.
     */
    SDSet updateSsdSet( SDSet ssdSet )
        throws SecurityException;


    /**
     * This command adds a role to a named SSD set of roles. The cardinality associated with the role set remains unchanged.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The SSD role set exists.</li>
     *   <li>The role to be added is a member of the ROLES data set but not of a member of the SSD role set.</li>
     *   <li>The SSD constraint is satisfied after the addition of the role to the SSD role set.</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of SSD role set to be modified</li>
     *   <li>Role#name - contains the name of new SDSet#members} to be added</li>
     * </ul>
     *
     * @param ssdSet contains an instantiated reference to new SSD set containing, name
     * @param role   contains instantiated Role object with role name field set.
     * @return reference to updated SSDSet object.
     * @throws SecurityException
     *          in the event of data validation or system error.
     */
    SDSet addSsdRoleMember( SDSet ssdSet, Role role )
        throws SecurityException;


    /**
     * This command removes a role from a named SSD set of roles. The cardinality associated with the role set remains 
     * unchanged.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The SSD role set exists.</li>
     *   <li>The role to be removed is a member of the SSD role set.</li>
     *   <li>The cardinality associated with the SSD role set is less than the number of elements of the SSD role set.</li>
     * </ul>
     * Note that the SSD constraint should be satisfied after the removal of the role from the SSD role set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of SSD role set to be modified</li>
     *   <li>Role#name - contains the name of existing SDSet#members} to be removed</li>
     * </ul>
     *
     * @param ssdSet contains an instantiated reference to new SSD set containing name.
     * @param role   contains instantiated Role object with role name field set.
     * @return reference to updated SSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    SDSet deleteSsdRoleMember( SDSet ssdSet, Role role )
        throws SecurityException;


    /**
     * This command deletes a SSD role set completely. The command is valid if and only if the SSD role set exists.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of SSD role set to be removed</li>
     * </ul>
     *
     * @param ssdSet contains an instantiated reference to SSD set targeted for removal.
     * @return reference to deleted SSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    SDSet deleteSsdSet( SDSet ssdSet )
        throws SecurityException;


    /**
     * This command sets the cardinality associated with a given SSD role set.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The SSD role set exists.</li>
     *   <li>
     *     The new cardinality is a natural number greater than or equal to 2 and less than or equal to the number of 
     *     elements of the SSD role set.
     *   </li>
     *   <li>The SSD constraint is satisfied after setting the new cardinality.
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of SSD role set to be modified</li>
     *   <li>cardinality - contains new cardinality setting for SSD</li>
     * </ul>
     *
     * @param ssdSet      contains an instantiated reference to new SSD set containing, name
     * @param cardinality integer value contains new cardinality value for data set.
     * @return reference to updated SSDSet object.
     * @throws SecurityException
     *          in the event of data validation or system error.
     */
    SDSet setSsdSetCardinality( SDSet ssdSet, int cardinality )
        throws SecurityException;


    /**
     * This command creates a named DSD set of roles and sets an associated cardinality n.
     * The DSD constraint stipulates that the DSD role set cannot contain n or more roles
     * simultaneously active in the same session.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The name of the DSD set is not already in use.</li>
     *   <li>All the roles in the DSD set are members of the ROLES data set.</li>
     *   <li>
     *     n is a natural number greater than or equal to 2 and less than or equal to the cardinality of the DSD role set.
     *   </li>
     *   <li> The DSD constraint for the new role set is satisfied.</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of new DSD role set to be added</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>SDSet#members} * - multivalued attribute contains the RBAC Role names to be added to this set</li>
     *   <li>
     *     SDSet#cardinality - default is 2 which is one more than maximum number of Roles that may be assigned to
     *     User from a particular set
     *   </li>
     *   <li>SDSet#description - contains any safe text</li>
     * </ul>
     *
     * @param dsdSet contains an instantiated reference to new DSD set containing, name, members, and cardinality 
     * (default 2)
     * @return reference to newly created SSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    SDSet createDsdSet( SDSet dsdSet )
        throws SecurityException;


    /**
     * This command updates existing DSD set of roles and sets the cardinality n of its subsets
     * that cannot have common users.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The name of the DSD set already exists.</li>
     *   <li>All the roles in the DSD set are members of the ROLES data set.</li>
     *   <li>
     *     n is a natural number greater than or equal to 2 and less than or equal to the cardinality of the DSD role set.
     *   </li>
     *   <li>The DSD constraint for the new role set is satisfied.</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of existing DSD role set to be updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>SDSet#members} * - multivalued attribute contains the RBAC Role names to be added to this set</li>
     *   <li>
     *     SDSet#cardinality - default is 2 which is one more than maximum number of Roles that may be assigned
     *     to User from a particular set
     *   </li>
     *   <li>SDSet#description - contains any safe text</li>
     * </ul>
     *
     * @param dsdSet contains an instantiated reference to existing DSD set containing, name, members, and cardinality 
     * (default 2)
     * @return reference to DSDSet object targeted for update.
     * @throws SecurityException in the event of data validation or system error.
     */
    SDSet updateDsdSet( SDSet dsdSet )
        throws SecurityException;


    /**
     * This command adds a role to a named DSD set of roles. The cardinality associated with
     * the role set remains unchanged.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The DSD role set exists.</li>
     *   <li>The role to be added is a member of the ROLES data set but not of a member of the DSD role set.</li>
     *   <li>The DSD constraint is satisfied after the addition of the role to the SSD role set.</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of DSD role set to be modified</li>
     *   <li>Role#name - contains the name of new SDSet#members} to be added</li>
     * </ul>
     * @param dsdSet contains an instantiated reference to new DSD set containing, name
     * @param role   contains instantiated Role object with role name field set.
     * @return reference to updated DSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    SDSet addDsdRoleMember( SDSet dsdSet, Role role )
        throws SecurityException;


    /**
     * This command removes a role from a named DSD set of roles. The cardinality associated
     * with the role set remains unchanged.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The DSD role set exists</li>
     *   <li>The role to be removed is a member of the DSD role set.</li>
     *   <li>The cardinality associated with the DSD role set is less than the number of elements of the DSD role set.</li>
     * </ul>
     * Note that the DSD constraint should be satisfied after the removal of the role from the DSD role set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of DSD role set to be modified</li>
     *   <li>Role#name - contains the name of existing SDSet#members} to be removed</li>
     * </ul>
     *
     * @param dsdSet contains an instantiated reference to new DSD set containing name.
     * @param role   contains instantiated Role object with role name field set.
     * @return reference to updated DSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    SDSet deleteDsdRoleMember( SDSet dsdSet, Role role )
        throws SecurityException;


    /**
     * This command deletes a DSD role set completely. The command is valid if and only if the DSD role set exists.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of DSD role set to be removed</li>
     * </ul>
     *
     * @param dsdSet contains an instantiated reference to DSD set targeted for removal.
     * @return reference to deleted DSDSet object.
     * @throws SecurityException in the event of data validation or system error.
     */
    SDSet deleteDsdSet( SDSet dsdSet )
        throws SecurityException;


    /**
     * This command sets the cardinality associated with a given DSD role set.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The SSD role set exists.</li>
     *   <li>
     *     The new cardinality is a natural number greater than or equal to 2 and less than or equal to the number of 
     *     elements of the SSD role set.
     *   </li>
     *   <li>The SSD constraint is satisfied after setting the new cardinality.</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of DSD role set to be modified</li>
     *   <li>cardinality - contains new cardinality setting for SSD</li>
     * </ul>
     *
     * @param dsdSet      contains an instantiated reference to new DSD set containing, name
     * @param cardinality integer value contains new cardinality value for data set.
     * @return reference to updated DSDSet object.
     * @throws SecurityException
     *          in the event of data validation or system error.
     */
    SDSet setDsdSetCardinality( SDSet dsdSet, int cardinality )
        throws SecurityException;
    
}