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

import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserAdminRole;


/**
 * This class prescribes the ARBAC02 DelReviewMgr interface for performing policy interrogation of provisioned Fortress 
 * ARBAC entities that reside in LDAP directory.
 * These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification 
 * describes delegated administrative operations for the creation and maintenance of ARBAC element sets and relations.  
 * Delegated administrative review functions for performing administrative queries and system functions for creating and 
 * managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="./doc-files/ARbac.png" alt="">
 * <p>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises 
 * the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without 
 * sacrificing regulations for accountability or traceability.
 * <p>
 * This interface's implementer will NOT be thread safe if parent instance variables ({@link Manageable#setContextId(String)} 
 * or {@link Manageable#setAdmin(org.apache.directory.fortress.core.model.Session)}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface DelReviewMgr extends Manageable
{
    /**
     * Method reads Admin Role entity from the admin role container in directory.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link AdminRole#name} - contains the name of the AdminRole being targeted for read</li>
     * </ul>
     *
     * @param role contains role name to be read.
     * @return AdminRole entity that corresponds with role name.
     * @throws SecurityException will be thrown if role not found or system error occurs.
     */
    AdminRole readRole( AdminRole role )
        throws SecurityException;


    /**
     * Method will return a list of type Admin Role.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link AdminRole#name} - contains all or some chars in the name of AdminRole(s) targeted for search</li>
     * </ul>
     *
     * @param searchVal contains the all or some of the chars corresponding to admin role entities stored in directory.
     * @return List of type AdminRole containing role entities that match the search criteria.
     * @throws SecurityException in the event of system error.
     */
    List<AdminRole> findRoles( String searchVal )
        throws SecurityException;


    /**
     * This function returns the set of admin roles assigned to a given user. The function is valid if and
     * only if the user is a member of the USERS data set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link User#userId} - contains the userId associated with the User object targeted for search.</li>
     * </ul>
     *
     * @param user contains userId matching user entity stored in the directory.
     * @return List of type UserAdminRole containing the user admin role data.
     * @throws SecurityException If user not found or system error occurs.
     */
    List<UserAdminRole> assignedRoles( User user )
        throws SecurityException;


    /**
     * This method returns the data set of all users who are assigned the given admin role.  This searches the User data set 
     * for AdminRole relationship.  This method does NOT search for hierarchical Admin Roles relationships.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link AdminRole#name} - contains the name of AdminRole targeted for search</li>
     * </ul>
     *
     * @param role contains the role name used to search the User data set.
     * @return List of type User containing the users assigned data.
     * @throws SecurityException If system error occurs.
     */
    List<User> assignedUsers( AdminRole role )
        throws SecurityException;


    /**
     * Commands reads existing OrgUnit entity from OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.OrgUnit#name} - contains the name associated with the OrgUnit 
     *     object targeted for search.
     *   </li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.OrgUnit#type} - contains the type of OU:  
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#USER} or 
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#PERM}
     *   </li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return OrgUnit entity that corresponds with ou name and type.
     * @throws SecurityException in the event of data validation or system error.
     */
    OrgUnit read( OrgUnit entity )
        throws SecurityException;


    /**
     * Commands searches existing OrgUnit entities from OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type parameter on API.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.OrgUnit#type} - contains the type of OU:  
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#USER} or 
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#PERM}
     *   </li>
     *   <li>searchVal - contains some or all of the chars associated with the OrgUnit objects targeted for search.</li>
     * </ul>
     *
     * @param type      either PERM or USER
     * @param searchVal contains the leading chars that map to {@link OrgUnit#name} on existing OrgUnit(s) targeted for search.
     * @return List of type OrgUnit containing the OrgUnit data.
     * @throws SecurityException in the event of data validation or system error.
     */
    List<OrgUnit> search( OrgUnit.Type type, String searchVal )
        throws SecurityException;
    
    /**
     * This function returns the set of all ARBAC permissions (op, obj), granted to or inherited by a
     * given ARBAC role. The function is valid if and only if the role is a member of the ROLES data
     * set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link AdminRole#name} - contains the name to use for the AdminRole targeted for search.</li>
     * </ul>
     *
     * @param role contains role name, {@link AdminRole#name} of AdminRole entity Permission is granted to.
     * @return List of type Permission that contains all perms granted to a role.
     * @throws SecurityException In the event system error occurs.
     */
    List<Permission> rolePermissions( AdminRole role )
        throws SecurityException;


    /**
     * This function returns the set of all ARBAC permissions (op, obj), granted to or inherited by a
     * given ARBAC role. The function is valid if and only if the role is a member of the ROLES data
     * set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link AdminRole#name} - contains the name to use for the AdminRole targeted for search.</li>
     * </ul>
     *
     * @param role contains role name, {@link AdminRole#name} of AdminRole entity Permission is granted to.
     * @param noInheritance if true will NOT include inherited roles in the search.
     * @return List of type Permission that contains all perms granted to a role.
     * @throws SecurityException In the event system error occurs.
     */
    List<Permission> rolePermissions( AdminRole role, boolean noInheritance )
        throws SecurityException;
}
