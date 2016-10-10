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


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.api.util.Strings;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.ValidationException;
import org.apache.directory.fortress.core.model.*;
import org.apache.directory.fortress.core.model.ConstraintUtil;
import org.apache.directory.fortress.core.util.VUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Process module for the group node of Fortress directory structure.
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class GroupP
{
    private static final String CLS_NM = GroupP.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private GroupDAO gDao = new GroupDAO();

    /**
     * Add a group node to the Directory Information Tree (DIT).
     *
     * @param group contains the group entity for target node.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in event of validation or system error.
     */
    Group add( Group group ) throws SecurityException
    {
        validate( group );

        Group outGroup = gDao.create(group);
        fillRoles(outGroup);
        return outGroup;
    }


    /**
     * Modify a group node within the Directory Information Tree (DIT).
     *
     * @param group contains the group entity for target node.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in event of validation or system error.
     */
    Group update( Group group ) throws SecurityException
    {
        validate( group );

        return gDao.update( group );
    }


    /**
     * Remove the group node.
     *
     * @param group contains the group entity for target node.
     * @throws SecurityException in event of validation or system error.
     */
    Group delete( Group group ) throws SecurityException
    {
        return gDao.remove( group );
    }


    /**
     * Add a new property to an existing Group
     *
     * @param group
     * @param key
     * @param value
     * @return
     * @throws org.apache.directory.fortress.core.SecurityException
     *
     */
    Group add( Group group, String key, String value ) throws SecurityException
    {
        return gDao.add( group, key, value );
    }


    /**
     * Remove an existing property value from an existing Group
     *
     * @param group
     * @param key
     * @param value
     * @return
     * @throws org.apache.directory.fortress.core.SecurityException
     *
     */
    Group delete( Group group, String key, String value ) throws SecurityException
    {
        return gDao.delete( group, key, value );
    }


    /**
     * Method will add the "member" attribute on LDAP entry which represents a Group assignment.
     *
     * @param entity contains the group name targeted.
     * @param userDn String contains the dn for the user entry that is being assigned the RBAC Role.
     * @return Group containing copy of input data.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    Group assign( Group entity, String userDn ) throws SecurityException
    {
        Group group = read( entity );
        group.setContextId( entity.getContextId() );

        return gDao.assign( group, userDn );
    }


    /**
     * Method will remove the "member" attribute on LDAP entry which represents a Group assignment.
     *
     * @param entity contains the role name targeted.
     * @param userDn String contains the dn for the user entry that is being assigned the RBAC Role.
     * @return Role containing copy of input data.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    Group deassign( Group entity, String userDn ) throws SecurityException
    {
        Group group = read( entity );
        group.setContextId( entity.getContextId() );

        return gDao.deassign( group, userDn );
    }


    /**
     * Return a fully populated Group entity for a given name.  If matching record not found a
     * SecurityException will be thrown.
     *
     * @param group contains full group name for entry in directory.
     * @return Group entity containing all attributes associated.
     * @throws SecurityException in the event not found or DAO search error.
     */
    Group read( Group group ) throws SecurityException
    {
        Group outGroup = gDao.get(group);
        outGroup.setContextId( group.getContextId() );
        fillRoles(outGroup);
        return outGroup;
    }


    /**
     * Takes a search string that contains full or partial Group name in directory.
     *
     * @param group contains full or partial name.
     * @return List of type Group containing fully populated matching entities.  If no records found this will be empty.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Group> search( Group group ) throws SecurityException
    {
        return gDao.find( group );
    }


    /**
     * Takes a search string that contains full or partial Group name in directory.
     *
     * @param user contains full dn for existing user.
     * @return List of type Group containing fully populated matching entities.  If no records found this will be empty.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Group> search( User user ) throws SecurityException
    {
        return gDao.find( user );
    }

    /**
     * Takes a search string that contains full or partial Role name in directory.
     *
     * @param role contains full dn name of role
     * @return List of type Group containing fully populated matching entities.  If no records found this will be empty.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Group> roleGroups( Role role ) throws SecurityException
    {
        return gDao.roleGroups( role );
    }

    /**
     * Return a list of group Roles for a given Group name.  If matching record not found a
     * SecurityException will be thrown.
     *
     * @param group contains full group name for entry in directory.
     * @return Group entity containing all attributes associated.
     * @throws SecurityException in the event not found or DAO search error.
     */
    List<UserRole> groupRoles( Group group ) throws SecurityException
    {
        Group outGroup = read(group);
        fillRoles( outGroup );
        return outGroup.getRoles();
    }


    /**
     * Creates a Session using given Group and its members, if Group type is ROLE
     * @param group a group to create Session for
     * @return Session object
     * @throws SecurityException
     */
    Session createSession( Group group ) throws SecurityException
    {
        // Create the impl session without authentication of password.
        Session session = createSessionTrusted( group );

        // Did the caller pass in a set of roles for selective activation?
        if ( CollectionUtils.isNotEmpty( group.getMembers() ) )
        {
            // Process selective activation of user's RBAC roles into session:
            List<String> availableRoles = session.getGroup().getMembers();
            availableRoles.retainAll( group.getMembers() );
        }
        // Fill aux field 'roles' with Role entities
        fillRoles( session.getGroup() );

        // Check role temporal constraints + activate roles:
        VUtil.getInstance().validateConstraints( session, VUtil.ConstraintType.ROLE, true );
        return session;
    }


    private Session createSessionTrusted( Group inGroup) throws SecurityException
    {
        Group group = read( inGroup );
        group.setContextId( inGroup.getContextId() );

        if ( group.getType() != Group.Type.ROLE )
        {
            String info = "createSession failed for Group ["
                    + group.getName() + "], group must be of type ROLE.";

            throw new ValidationException( GlobalErrIds.GROUP_TYPE_INVLD, info );
        }

        Session session = new Session(group);
        // Set this flag to false because group was not authenticated.
        session.setAuthenticated( false );
        return session;
    }


    /**
     * Populates the auxiliary field 'roles' in given group object with
     * {@link UserRole} data
     * @param group a group object to populate
     * @throws SecurityException thrown in the event the attribute is null.
     */
    private void fillRoles( Group group ) throws SecurityException {
        if ( Group.Type.ROLE.equals( group.getType() ) )
        {
            RoleP rp = new RoleP();
            List<UserRole> uRoles = new ArrayList<>();
            List<Role> roles = rp.search( group );
            for ( Role inRole : roles )
            {
                UserRole ure = new UserRole( group.getName(), inRole.getName(), true );
                ConstraintUtil.validateOrCopy( inRole, ure );
                uRoles.add( ure );
            }
            group.setRoles( uRoles );
        }
    }


/*
    private void fillRoles( Group group ) throws SecurityException {
        if ( Group.Type.ROLE.equals( group.getType() ) )
        {
            RoleP rp = new RoleP();
            List<UserRole> roles = new ArrayList<>();
            List<String> members = group.getMembers();
            for ( String roleDn : members )
            {
                String roleRdn = roleDn;
                if ( group.isMemberDn() )
                {
                    String[] parts = roleDn.split( "," );
                    if (parts.length > 0)
                    {
                        roleRdn = parts[ 0 ];
                    }
                    roleRdn = roleRdn.replaceFirst( "cn=", "" ); // remove 'cn='
                }
                Role inRole = new Role( roleRdn );
                inRole.setContextId( group.getContextId() );
                Role role = rp.read( inRole );

                UserRole ure = new UserRole( group.getName(), roleRdn, true );
                ConstraintUtil.validateOrCopy( role, ure );
                roles.add( ure );
            }
            group.setRoles( roles );
        }
    }


*/
    /**
     * Method will perform simple validations to ensure the integrity of the {@link Group} entity targeted for insertion
     * or deletion in directory.
     *
     * @param entity contains the enum type to validate
     * @throws org.apache.directory.fortress.core.SecurityException
     *          thrown in the event the attribute is null.
     */
    private void validate( Group entity ) throws SecurityException
    {
        if ( Strings.isEmpty( entity.getName() ) )
        {
            String error = "validate name validation failed, null or empty value";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.GROUP_NAME_NULL, error );
        }

        if ( entity.getName().length() > GlobalIds.OU_LEN )
        {
            String name = entity.getName();
            String error = "validate name [" + name + "] invalid length [" + entity.getName().length() + "]";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.GROUP_NAME_INVLD, error );
        }

        if ( entity.getProtocol().length() > GlobalIds.OU_LEN )
        {
            String error = "validate protocol [" + entity.getProtocol() + "] invalid length [" + entity.getProtocol()
                .length() + "]";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.GROUP_PROTOCOL_INVLD, error );
        }

        if ( !Strings.isEmpty( entity.getDescription() ) )
        {
            VUtil.description( entity.getDescription() );
        }
    }
}