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


import org.apache.directory.fortress.core.model.Group;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;

import java.util.List;


/**
 * This interface prescribes CRUD methods used to manage groups stored within the ldap directory.
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface GroupMgr extends Manageable
{
    /**
     * Create a new group node.,
     *
     * @param group contains {@link org.apache.directory.fortress.core.model.Group}.
     * @return {@link org.apache.directory.fortress.core.model.Group} containing entity just added.
     * @throws org.apache.directory.fortress.core.SecurityException in the event system error.
     */
    Group add( Group group ) throws org.apache.directory.fortress.core.SecurityException;


    /**
     * Modify existing group node.  The name is required.  Does not update members or properties.
     * Use {@link GroupMgr#add( Group group, String key, String value )}, {@link GroupMgr#delete( Group group, String key, 
     * String value )}, {@link GroupMgr#assign( Group group, String member) }, or 
     * {@link GroupMgr#deassign( Group group, String member) } for multivalued attributes.
     *
     * @param group contains {@link Group}.
     * @return {@link Group} containing entity just modified.
     * @throws org.apache.directory.fortress.core.SecurityException in the event system error.
     */
    Group update( Group group ) throws SecurityException;


    /**
     * Delete existing group node.  The name is required.
     *
     * @param group contains {@link Group}.
     * @return {@link Group} containing entity just removed.
     * @throws org.apache.directory.fortress.core.SecurityException in the event system error.
     */
    Group delete( Group group ) throws SecurityException;


    /**
     * Add a property to an existing group node. Must have a name and at least one member.
     *
     * @param group contains {@link Group}.
     * @param key contains the property key.
     * @param value contains contains the property value.
     * @return {@link Group} containing entity just modified.
     * @throws org.apache.directory.fortress.core.SecurityException in the event system error.
     */
    Group add( Group group, String key, String value ) throws SecurityException;


    /**
     * Delete existing group node.  The name is required.
     *
     * @param group contains {@link Group}.
     * @param key contains the property key.
     * @param value contains contains the property value.
     * @return {@link Group} containing entity just modified.
     * @throws org.apache.directory.fortress.core.SecurityException in the event system error.
     */
    Group delete( Group group, String key, String value ) throws SecurityException;


    /**
     * Read an existing group node.  The name is required.
     *
     * @param group contains {@link Group} with name field set with an existing group name.
     * @return {@link Group} containing entity found.
     * @throws org.apache.directory.fortress.core.SecurityException in the event system error.
     */
    Group read( Group group ) throws SecurityException;


    /**
     * Search using a full or partial group node.  The name is required.
     *
     * @param group contains {@link Group}.
     * @return List of type {@link Group} containing entities found.
     * @throws org.apache.directory.fortress.core.SecurityException in the event system error.
     */
    List<Group> find( Group group ) throws SecurityException;


    /**
     * Search for groups by userId.  Member (maps to userId) and is required.
     *
     * @param user contains userId that maps to Group member attribute.
     * @return {@link Group} containing entity just added.
     * @throws org.apache.directory.fortress.core.SecurityException in the event system error.
     */
    List<Group> find( User user ) throws SecurityException;

    /**
     * Search for groups by role name.  Member (maps to role name) is required.
     *
     * @param role contains userId that maps to Group member attribute.
     * @return {@link Group} containing entity just added.
     * @throws org.apache.directory.fortress.core.SecurityException in the event system error.
     */
    List<Group> roleGroups( Role role ) throws SecurityException;

    /**
     * Read an existing group node's roles.  The name is required.
     *
     * @param group contains {@link Group} with name field set with an existing group name.
     * @return list of {@link UserRole} for given group. Will return empty list if Group has no roles assigned.
     * @throws org.apache.directory.fortress.core.SecurityException in the event system error.
     */
    List<UserRole> groupRoles( Group group ) throws SecurityException;


    /**
     * Assign a user to an existing group node.  The group name and member are required.
     *
     * @param group contains {@link Group}.
     * @param member is the relative distinguished name (rdn) of an existing user in ldap.
     * @return {@link Group} containing entity to assign.
     * @throws org.apache.directory.fortress.core.SecurityException in the event entry already present or other system error.
     */
    Group assign( Group group, String member ) throws SecurityException;


    /**
     * Deassign a member from an existing group node. The group name and member are required.
     *
     * @param group contains {@link Group}.
     * @param member is the relative distinguished name (rdn) of an existing user in ldap.
     * @return {@link Group} containing entity to deassign
     * @throws org.apache.directory.fortress.core.SecurityException in the event entry already present or other system error.
     */
    Group deassign( Group group, String member ) throws SecurityException;
}