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


import java.util.List;
import java.util.Set;

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


/**
 * This interface prescribes the administrative review functions on already provisioned Fortress RBAC entities
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
 * <img src="./doc-files/RbacCore.png" alt="">
 * <hr>
 * <h4>RBAC1 - General Hierarchical Roles</h4>
 * Simplifies role engineering tasks using inheritance of one or more parent roles.
 * <p>
 * <img src="./doc-files/RbacHier.png" alt="">
 * <hr>
 * <h4>RBAC2 - Static Separation of Duty (SSD) Relations</h4>
 * Enforce mutual membership exclusions across role assignments.  Facilitate dual control policies by restricting which roles 
 * may be assigned to users in combination.  SSD provide added granularity for authorization limits which help enterprises 
 * meet strict compliance regulations.
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
 * This interface's implementer will NOT be thread safe if parent instance variables ({@link Manageable#setContextId(String)
 * or {@link Manageable#setAdmin(org.apache.directory.fortress.core.model.Session)}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface ReviewMgr extends Manageable
{

    /**
     * This method returns a matching permission entity to caller.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Permission#objName - contains the name of existing object being targeted</li>
     *   <li>Permission#opName - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param permission must contain the object, Permission#objName}, and operation, Permission#opName}, and
     * optionally object id of targeted permission entity.
     * @return Permission entity that is loaded with data.
     * @throws SecurityException
     *          if permission not found or system error occurs.
     */
    Permission readPermission( Permission permission )
        throws SecurityException;


    /**
     * Method reads permission object from perm container in directory.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>PermObj#objName - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param permObj entity contains the PermObj#objName of target record.
     * @return PermObj loaded with perm object data.
     * @throws SecurityException is thrown if object not found or system error.
     */
    PermObj readPermObj( PermObj permObj )
        throws SecurityException;

    /**
     * Method read permission attribute set in directory
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>PermissionAttributeSet#name - contains the name of existing object being targeted</li>
     * </ul>
     * 
     * @param permAttributeSet  entity contains the PermissionAttributeSet#name of target record.
     * @return PermissionAttributeSet loaded with perm attribute set data.
     * @throws SecurityException is thrown if object not found or system error.
     */
    PermissionAttributeSet readPermAttributeSet( PermissionAttributeSet permAttributeSet )
            throws SecurityException;
        
    
    /**
     * Method returns a list of type Permission that match the perm object search string.
     * <h3></h3>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>Permission#objName - contains one or more characters of existing object being targeted</li>
     *   <li>Permission#opName - contains one or more characters of existing permission operation</li>
     * </ul>
     *
     * @param permission contains object and operation name search strings.  Each contains 1 or more leading chars that 
     * correspond to object or op name.
     * @return List of type Permission.  Fortress permissions are object-&gt;operation mappings.  The permissions may contain
     *         assigned user, role or group entities as well.
     * @throws SecurityException thrown in the event of system error.
     */
    List<Permission> findPermissions( Permission permission )
        throws SecurityException;

    /**
     * Method returns Permission operations for the provided permission object
     * 
     * @param permObj entity contains the PermObj#objName of target record.
     * @return List of type Permission for provided permission object
     * @throws SecurityException
     *          thrown in the event of system error.
     */
    List<Permission> findPermsByObj( PermObj permObj )
        throws SecurityException;
    
    /**
     * Method returns a list of Permissions that match any part of the permission object or operation.
     * 
     * @param permission contains object and operation name search strings.
     * @return List of type Permission.  Fortress permissions are object-&gt;operation mappings.  The permissions may contain
     *         assigned user, role or group entities as well.
     * @throws SecurityException thrown in the event of system error.
     */
    List<Permission> findAnyPermissions( Permission permission )
            throws SecurityException;
    
    /**
     * Method returns a list of type PermObj that match the perm object search string.
     * <h3></h3>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>PermObj#objName - contains one or more characters of existing object being targeted</li>
     * </ul>
     *
     * @param permObj contains object name search string.  The search val contains 1 or more leading chars that correspond 
     * to object name.
     * @return List of type PermObj.  Fortress permissions are object-&gt;operation mappings.
     * @throws SecurityException thrown in the event of system error.
     */
    List<PermObj> findPermObjs( PermObj permObj )
        throws SecurityException;


    /**
     * Method returns a list of type Permission that match the perm object search string.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     OrgUnit#name - contains one or more characters of org unit associated with existing object being targeted
     *   </li>
     * </ul>
     *
     * @param ou contains org unit name org.apache.directory.fortress.core.model.OrgUnit#name}.  The search val
     * contains the full name of matching ou in OS-P data set.
     * @return List of type PermObj.  Fortress permissions are object-&gt;operation mappings.
     * @throws SecurityException thrown in the event of system error.
     */
    List<PermObj> findPermObjs( OrgUnit ou )
        throws SecurityException;


    /**
     * Method reads Role entity from the role container in directory.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name to use for the Role to read.</li>
     * </ul>
     *
     * @param role contains role name, Role#name}, to be read.
     * @return Role entity that corresponds with role name.
     * @throws SecurityException will be thrown if role not found or system error occurs.
     */
    Role readRole( Role role )
        throws SecurityException;


    /**
     * Method will return a list of type Role matching all or part of Role name, Role#name}.
     *
     * @param searchVal contains all or some of the chars corresponding to role entities stored in directory.
     * @return List of type Role containing role entities that match the search criteria.
     * @throws SecurityException in the event of system error.
     */
    List<Role> findRoles( String searchVal )
        throws SecurityException;


    /**
     * Method returns a list of roles of type String.  This method can be limited by integer value that indicates max
     * number of records that may be contained in the result set.  This number can further limit global default but can
     * not increase the max.  This method is called by the Websphere Realm impl.
     *
     * @param searchVal contains all or some leading chars that correspond to roles stored in the role container in the
     *  directory.
     * @param limit     integer value specifies the max records that may be returned in the result set.
     * @return List of type String containing matching Role names.
     * @throws SecurityException in the event of system error.
     */
    List<String> findRoles( String searchVal, int limit )
        throws SecurityException;


    /**
     * Method returns matching User entity that is contained within the people container in the directory.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - contains the userId associated with the User object targeted for read.</li>
     * </ul>
     *
     * @param user entity contains a value User#userId that matches record in the directory.  userId is globally
     * unique in people container.
     * @return entity containing matching user data.
     * @throws SecurityException if record not found or system error occurs.
     */
    User readUser( User user )
        throws SecurityException;


    /**
     * Return a list of type User of all users in the people container that match all or part of the User#userId
     * field passed in User entity.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - contains all or some leading chars that match userId(s) stored in the directory.</li>
     * </ul>
     *
     * @param user contains all or some leading chars that match userIds stored in the directory.
     * @return List of type User.
     * @throws SecurityException In the event of system error.
     */
    List<User> findUsers( User user )
        throws SecurityException;


    /**
     * Return a list of type User of all users in the people container that match the name field passed in OrgUnit entity.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     OrgUnit#name - contains one or more characters of org unit associated with existing object(s) being
     *     targeted
     *   </li>
     * </ul>
     *
     * @param ou contains name of User OU, OrgUnit#name that match ou attribute associated with User entity in the
     * directory.
     * @return List of type User.
     * @throws SecurityException In the event of system error.
     */
    List<User> findUsers( OrgUnit ou )
        throws SecurityException;


    /**
     * Return a list of type String of all users in the people container that match the userId field passed in User entity.
     * This method is used by the Websphere realm component.  The max number of returned users may be set by the integer 
     * limit arg.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - contains the userId associated with the User object targeted for read.</li>
     *   <li>limit - max number of objects to return.</li>
     * </ul>
     *
     * @param user  contains all or some leading chars that correspond to users stored in the directory.
     * @param limit integer value sets the max returned records.
     * @return List of type String containing matching userIds.
     * @throws SecurityException in the event of system error.
     */
    List<String> findUsers( User user, int limit )
        throws SecurityException;


    /**
     * This function returns the set of users assigned to a given role. The function is valid if and
     * only if the role is a member of the ROLES data set.
     * The max number of users returned is constrained by limit argument.
     * This method is used by the Websphere realm component.  This method does NOT use hierarchical impl.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name to use for the Role targeted for search.</li>
     *   <li>limit - max number of objects to return.</li>
     * </ul>
     *
     * @param role  Contains Role#name of Role entity assigned to user.
     * @param limit integer value sets the max returned records.
     * @return List of type String containing userIds assigned to a particular role.
     * @throws SecurityException in the event of data validation or system error.
     */
    List<String> assignedUsers( Role role, int limit )
        throws SecurityException;


    /**
     * This function returns the set of roles assigned to a given user. The function is valid if and
     * only if the user is a member of the USERS data set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - contains the userId associated with the User object targeted for search.</li>
     * </ul>
     *
     * @param user contains User#userId matching User entity targeted in the directory.
     * @return List of type UserRole containing the Roles assigned to User.
     * @throws SecurityException If user not found or system error occurs.
     */
    List<UserRole> assignedRoles( User user )
        throws SecurityException;


    /**
     * This method returns the data set of all users who are assigned the given role.  This searches the User data set for
     * Role relationship.  This method does NOT search for hierarchical RBAC Roles relationships.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role contains the role name, Role#name used to search the User data set.
     * @return List of type User containing the users assigned data.
     * @throws SecurityException If system error occurs.
     */
    List<User> assignedUsers( Role role )
        throws SecurityException;

    /**
     * This method returns the data set of all users who are assigned the given role.  This searches the User data set for
     * Role relationship.  This method does NOT search for hierarchical RBAC Roles relationships.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role contains the role name, Role#name used to search the User data set.
     * @param roleConstraint constraint to filter the roles return
     * @return List of type User containing the users assigned data.
     * @throws SecurityException If system error occurs.
     */
    List<User> assignedUsers( Role role, RoleConstraint roleConstraint ) throws SecurityException;
    
    /**
     * This method returns the user roles for all users who have the given role, with a specified constraint type
     * and permission attribute set name.
     *
     * @param role
     * @param rcType
     * @param paSetName
     * @return
     * @throws SecurityException
     */
    List<UserRole> assignedUsers( Role role, RCType rcType, String paSetName ) throws SecurityException;
    
    /**
     * This function returns the set of roles assigned to a given user. The function is valid if and
     * only if the user is a member of the USERS data set.
     *
     * @param userId matches userId stored in the directory.
     * @return List of type String containing the role names of all roles assigned to user.
     * @throws SecurityException If user not found or system error occurs.
     */
    List<String> assignedRoles( String userId )
        throws SecurityException;


    /**
     * This function returns the set of users authorized to a given role, i.e., the users that are assigned to a role that
     * inherits the given role. The function is valid if and only if the given role is a member of the ROLES data set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role Contains role name, Role#name of Role entity assigned to User.
     * @return List of type User containing all user's that having matching role assignment.
     * @throws SecurityException In the event the role is not present in directory or system error occurs.
     */
    List<User> authorizedUsers( Role role )
        throws SecurityException;


    /**
     * This function returns the set of roles authorized for a given user. The function is valid if
     * and only if the user is a member of the USERS data set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - contains the userId associated with the User object targeted for search.</li>
     * </ul>
     *
     * @param user contains the User#userId matching User entity stored in the directory.
     * @return Set of type String containing the roles assigned and roles inherited.
     * @throws SecurityException If user not found or system error occurs.
     */
    Set<String> authorizedRoles( User user )
        throws SecurityException;


    /**
     * This function returns the set of all permissions (op, obj), granted to or inherited by a
     * given role. The function is valid if and only if the role is a member of the ROLES data
     * set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role contains role name, Role#name of Role entity Permission is granted to.
     * @return List of type Permission that contains all perms granted to a role.
     * @throws SecurityException In the event system error occurs.
     */
    List<Permission> rolePermissions( Role role )
        throws SecurityException;


    /**
     * This function returns the set of all permissions (op, obj), granted to or inherited by a
     * given role. The function is valid if and only if the role is a member of the ROLES data
     * set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role contains role name, Role#name of Role entity Permission is granted to.
     * @param noInheritance if true will NOT include inherited roles in the search.
     * @return List of type Permission that contains all perms granted to a role.
     * @throws SecurityException In the event system error occurs.
     */
    List<Permission> rolePermissions( Role role, boolean noInheritance )
        throws SecurityException;

    /**
     * This function returns all the permission attribute set (which contain 0 to many permission attributes)
     * for a given role. The function is valid if and only if the role is a member of the ROLES data
     * set.
     *      * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name to use for the Role targeted for search.</li>
     * </ul>
     * 
     * @param role contains role name, Role#name of Role entity Permission is granted to.
     * @param noInheritance if true will NOT include inherited roles in the search.
     * @return List of type PermissionAttributeSet that contains all Permission Attribute valid for the role.
     * @throws SecurityException In the event system error occurs.
     */
    List<PermissionAttributeSet> rolePermissionAttributeSets( Role role, boolean noInheritance )
        throws SecurityException;
    
    /**
     * This function returns the set of permissions a given user gets through his/her authorized
     * roles. The function is valid if and only if the user is a member of the USERS data set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - contains the userId associated with the User object targeted for search.</li>
     * </ul>
     *
     * @param user contains the User#userId of User targeted for search.
     * @return List of type Permission containing matching permission entities.
     * @throws SecurityException in the event of validation or system error.
     */
    List<Permission> userPermissions( User user )
        throws SecurityException;


    /**
     * Return a list of type String of all roles that have granted a particular permission.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Permission#objName - contains the name of existing object being targeted</li>
     *   <li>Permission#opName - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param perm must contain the object, Permission#objName}, and operation, Permission#opName}, and optionally object id of targeted permission entity.
     * @return List of type string containing the Role names that have the matching perm granted.
     * @throws SecurityException in the event permission not found or system error occurs.
     */
    List<String> permissionRoles( Permission perm )
        throws SecurityException;


    /**
     * Return all role names that have been authorized for a given permission.  This will process role hierarchies to determine set of all Roles who have access to a given permission.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Permission#objName - contains the name of existing object being targeted</li>
     *   <li>Permission#opName - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param perm must contain the object, Permission#objName}, and operation, Permission#opName}, and optionally object id of targeted permission entity.
     * @return Set of type String containing all roles names that have been granted a particular permission.
     * @throws SecurityException in the event of validation or system error.
     */
    Set<String> authorizedPermissionRoles( Permission perm )
        throws SecurityException;


    /**
     * Return all userIds that have been granted (directly) a particular permission.  This will not consider assigned or authorized Roles.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Permission#objName - contains the name of existing object being targeted</li>
     *   <li>Permission#opName - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param perm must contain the object, Permission#objName}, and operation, Permission#opName}, and optionally object id of targeted permission entity.
     * @return List of type String containing all userIds that have been granted a particular permission.
     * @throws SecurityException in the event of validation or system error.
     */
    List<String> permissionUsers( Permission perm )
        throws SecurityException;


    /**
     * Return all userIds that have been authorized for a given permission.  This will process role hierarchies to determine set of all Users who have access to a given permission.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Permission#objName - contains the name of existing object being targeted</li>
     *   <li>Permission#opName - contains the name of existing permission operation</li>
     * </ul>
     *
     * @param perm must contain the object, Permission#objName}, and operation, Permission#opName}, and optionally object id of targeted permission entity.
     * @return Set of type String containing all userIds that have been granted a particular permission.
     * @throws SecurityException in the event of validation or system error.
     */
    Set<String> authorizedPermissionUsers( Permission perm )
        throws SecurityException;


    /**
     * This function returns the list of all SSD role sets that have a particular Role as member or Role's
     * parent as a member.  If the Role parameter is left blank, function will return all SSD role sets.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role Will contain the role name, Role#name}, for targeted SSD set or null to return all
     * @return List containing all matching SSD's.
     * @throws SecurityException in the event of data or system error.
     */
    List<SDSet> ssdRoleSets( Role role )
        throws SecurityException;


    /**
     * This function returns the SSD data set that matches a particular set name.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param set Will contain the name for existing SSD data set, SDSet#name}.
     * @return SDSet containing all attributes from matching SSD name.
     * @throws SecurityException in the event of data or system error.
     */
    SDSet ssdRoleSet( SDSet set )
        throws SecurityException;


    /**
     * This function returns the list of SSDs that match a given ssd name value.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param ssd contains the name for the SSD set targeted, SDSet#name}.
     * @return List containing all SSDSets that match a given SSDSet name.
     * @throws SecurityException in the event of data or system error.
     */
    List<SDSet> ssdSets( SDSet ssd )
        throws SecurityException;


    /**
     * This function returns the set of roles of a SSD role set. The function is valid if and only if the
     * role set exists.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param ssd contains the name for the SSD set targeted, SDSet#name}.
     * @return Set containing all Roles that are members of SSD data set.
     * @throws SecurityException in the event of data or system error.
     */
    Set<String> ssdRoleSetRoles( SDSet ssd )
        throws SecurityException;


    /**
     * This function returns the cardinality associated with a SSD role set. The function is valid if and only if the
     * role set exists.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param ssd contains the name of the SSD set targeted, SDSet#name}.
     * @return int value containing cardinality of SSD set.
     * @throws SecurityException in the event of data or system error.
     */
    int ssdRoleSetCardinality( SDSet ssd )
        throws SecurityException;


    /**
     * This function returns the list of all dSD role sets that have a particular Role as member or Role's
     * parent as a member.  If the Role parameter is left blank, function will return all dSD role sets.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>Role#name - contains the name to use for the Role targeted for search.</li>
     * </ul>
     *
     * @param role Will contain the role name, Role#name}, for targeted dSD set or null to return all
     * @return List containing all matching dSD's.
     * @throws SecurityException in the event of data or system error.
     */
    List<SDSet> dsdRoleSets( Role role )
        throws SecurityException;


    /**
     * This function returns the DSD data set that matches a particular set name.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param set Will contain the name for existing DSD data set, SDSet#name}.
     * @return SDSet containing all attributes from matching DSD name.
     * @throws SecurityException in the event of data or system error.
     */
    SDSet dsdRoleSet( SDSet set )
        throws SecurityException;


    /**
     * This function returns the list of DSDs that match a given dsd name value.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param dsd contains the name for the DSD set targeted, SDSet#name}.
     * @return List containing all DSDSets that match a given DSDSet name.
     * @throws SecurityException in the event of data or system error.
     */
    List<SDSet> dsdSets( SDSet dsd )
        throws SecurityException;


    /**
     * This function returns the set of roles of a DSD role set. The function is valid if and only if the
     * role set exists.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param dsd contains the name for the DSD set targeted, SDSet#name}.
     * @return Set containing all Roles that are members of DSD data set.
     * @throws SecurityException in the event of data or system error.
     */
    Set<String> dsdRoleSetRoles( SDSet dsd )
        throws SecurityException;


    /**
     * This function returns the cardinality associated with a DSD role set. The function is valid if and only if the
     * role set exists.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>SDSet#name - contains the name of existing object being targeted</li>
     * </ul>
     *
     * @param dsd contains the name of the DSD set targeted, SDSet#name}.
     * @return int value containing cardinality of DSD set.
     * @throws SecurityException in the event of data or system error.
     */
    int dsdRoleSetCardinality( SDSet dsd )
        throws SecurityException;

    /**
     * Find all of the role constraints for the given user and permission attribute set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>User#userId - contains the name of existing user being targeted</li>
     *   <li>PermissionAttributeSet#name - contains the name of permission attribute set</li>
     * </ul>
     * 
     * @param user The user to filter role constraints
     * @param permission Contains the permission attribute set to filter role constraints
     * @return List of the Role Constraints for the given user and pa set.   
     * @throws SecurityException in the event of data or system error.
     */
    List<RoleConstraint>  findRoleConstraints(User user, Permission permission, RoleConstraint.RCType rcType)
        throws SecurityException;
    
}
