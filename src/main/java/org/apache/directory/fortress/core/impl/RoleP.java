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


import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.ValidationException;
import org.apache.directory.fortress.core.util.ConstraintValidator;
import org.apache.directory.fortress.core.model.Graphable;
import org.apache.directory.fortress.core.model.Group;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.util.VUtil;


/**
 * Process module for the Role entity.  This class performs data validations and error mapping.  It is typically called
 * by internal Fortress manager classes ({@link AdminMgrImpl}, {@link AccessMgrImpl},
 * {@link ReviewMgrImpl}, ...) and not intended for external non-Fortress clients.  This class will accept,
 * {@link org.apache.directory.fortress.core.model.Role}, validate its contents and forward on to it's corresponding DAO class {@link RoleDAO}.
 * <p>
 * Class will throw {@link SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link org.apache.directory.fortress.core.FinderException},
 * {@link org.apache.directory.fortress.core.CreateException},{@link org.apache.directory.fortress.core.UpdateException},{@link org.apache.directory.fortress.core.RemoveException}),
 *  or {@link org.apache.directory.fortress.core.ValidationException} as {@link SecurityException}s with appropriate
 * error id from {@link org.apache.directory.fortress.core.GlobalErrIds}.
 * <p>
 * This class is thread safe.
 * <p>

 *
 * @author Kevin McKinney
 */
final class RoleP
{
    private RoleDAO rDao = new RoleDAO();
    private static final ConstraintValidator constraintValidator = VUtil.getConstraintValidator();


    /**
     * Return a fully populated Role entity for a given RBAC role name.  If matching record not found a
     * SecurityException will be thrown.
     *
     * @param role contains full role name for RBAC role in directory.
     * @return Role entity containing all attributes associated with Role in directory.
     * @throws SecurityException in the event Role not found or DAO search error.
     */
    Role read( Role role ) throws SecurityException
    {
        return rDao.getRole( role );
    }


    /**
     * Takes a search string that contains full or partial RBAC Role name in directory.
     *
     * @param role contains full or partial RBAC role name.
     * @return List of type Role containing fully populated matching RBAC Role entities.  If no records found this will be empty.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Role> search( Role role ) throws SecurityException
    {
        return rDao.findRoles( role );
    }


    /**
     * Takes a search string that contains full or partial RBAC Role name in directory.
     * This search is used by RealmMgr for Websphere.
     *
     * @param role contains full or partial RBAC role name.
     * @param limit     specify the max number of records to return in result set.
     * @return List of type String containing RBAC Role name of all matching User entities.  If no records found this will be empty.
     * @throws SecurityException in the event of DAO search error.
     */
    List<String> search( Role role, int limit ) throws SecurityException
    {
        return rDao.findRoles( role, limit );
    }


    /**
     * Given a particular group, containing a set of members, return all Roles with a matching member.
     *
     * @param group contains a list of member names pertaining to roles.
     * @return List of type Role containing fully populated matching RBAC Role entities.  If no records found this will be empty.
     * @throws SecurityException in the event not the right type of Group or DAO search error.
     */
    List<Role> search( Group group ) throws SecurityException
    {
        if ( group.getType() != Group.Type.ROLE )
        {
            String info = "search failed for Group ["
                + group.getName() + "], group must be of type ROLE.";

            throw new ValidationException( GlobalErrIds.GROUP_TYPE_INVLD, info );
        }
        return rDao.groupRoles( group );
    }


    /**
     * Return all Roles that have a parent assignment.  This used for hierarchical processing.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return List of type Role containing {@link Role#name} and {@link Role#parents} populated.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Graphable> getAllDescendants( String contextId ) throws SecurityException
    {
        return rDao.getAllDescendants( contextId );
    }


    /**
     * Adds a new Role entity to directory.  The Role entity input object will be validated to ensure that:
     * role name is present, and reasonability checks on all of the other populated values.
     *
     * @param entity Role entity contains data targeted for insertion.
     * @return Role entity copy of input + additional attributes (internalId) that were added by op.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    Role add( Role entity ) throws SecurityException
    {
        validate( entity );
        return rDao.create( entity );
    }


    /**
     * Updates existing Role entity in directory.  For example the Role description and temporal constraints
     * updated.
     *
     * @param entity Role entity contains data targeted for updating.
     * @return Role entity contains fully populated updated entity.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    Role update( Role entity ) throws SecurityException
    {
        validate( entity );
        return rDao.update( entity );
    }


    /**
     * Removes parent role assignments from Role entity in directory.
     * updated.
     *
     * @param entity Role entity contains data targeted for updating.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    void deleteParent( Role entity ) throws SecurityException
    {
        validate( entity );
        rDao.deleteParent( entity );
    }


    /**
     * Method will add the "roleOccupant" attribute on OpenLDAP entry which represents an RBAC Role assignment in Fortress.
     *
     * @param entity contains the role name targeted.
     * @param userDn String contains the dn for the user entry that is being assigned the RBAC Role.
     * @return Role containing copy of input data.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    Role assign( Role entity, String userDn ) throws SecurityException
    {
        return rDao.assign( entity, userDn );
    }


    /**
     * Method will remove the "roleOccupant" attribute on OpenLDAP entry which represents an RBAC Role assignment in Fortress.
     *
     * @param entity contains the role name targeted.
     * @param userDn String contains the dn for the user entry that is being assigned the RBAC Role.
     * @return Role containing copy of input data.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    Role deassign( Role entity, String userDn ) throws SecurityException
    {
        entity = rDao.deassign( entity, userDn );
        return entity;
    }


    /**
     * Add the User dn occupant attribute to the OrganizationalRole entity in ldap.  This method is called by AdminMgrImpl
     * when the User is being added.
     *
     * @param uRoles contains a collection of UserRole being targeted for assignment.
     * @param userDn contains the userId targeted for addition.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @throws SecurityException in the event of DAO search error.
     */
    void addOccupant( List<UserRole> uRoles, String userDn, String contextId ) throws SecurityException
    {
        if ( CollectionUtils.isNotEmpty( uRoles ) )
        {
            for ( UserRole uRole : uRoles )
            {
                Role role = new Role( uRole.getName() );
                role.setContextId( contextId );
                assign( role, userDn );
            }
        }
    }


    /**
     * Remove the User dn occupant attribute from the OrganizationalRole entity in ldap.  This method is called by AdminMgrImpl
     * when the User is being deleted.
     *
     * @param userDn contains the userId targeted for attribute removal.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @throws SecurityException in the event of DAO search error.
     */
    void removeOccupant( String userDn, String contextId ) throws SecurityException
    {
        List<String> list;
        try
        {
            list = rDao.findAssignedRoles( userDn, contextId );
            for ( String roleNm : list )
            {
                Role role = new Role( roleNm );
                role.setContextId( contextId );
                deassign( role, userDn );
            }
        }
        catch ( FinderException fe )
        {
            String error = "removeOccupant userDn [" + userDn + "] caught FinderException=" + fe;
            throw new SecurityException( GlobalErrIds.ROLE_REMOVE_OCCUPANT_FAILED, error, fe );
        }
    }


    /**
     * This method performs a "hard" delete.  It completely the RBAC Role node from the ldap directory.
     * RBAC Role entity must exist in directory prior to making this call else exception will be thrown.
     *
     * @param entity Contains the name of the RBAC Role targeted for deletion.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    void delete( Role entity ) throws SecurityException
    {
        rDao.remove( entity );
    }


    /**
     * Method will perform simple validations to ensure the integrity of the RBAC Role entity targeted for insertion
     * or updating in directory.  For example the Role temporal constraints will be validated.  Data reasonability
     * checks will be performed on all non-null attributes.
     *
     * @param entity contains data targeted for insertion or update.
     * @throws org.apache.directory.fortress.core.ValidationException in the event of data validation error or Org validation.
     */
    private void validate( Role entity )
        throws ValidationException
    {
        VUtil.safeText( entity.getName(), GlobalIds.ROLE_LEN );
        if ( StringUtils.isNotEmpty( entity.getDescription() ) )
        {
            VUtil.description( entity.getDescription() );
        }
        if ( entity.getTimeout() != null )
        {
            constraintValidator.timeout( entity.getTimeout() );
        }
        if ( StringUtils.isNotEmpty( entity.getBeginTime() ) )
        {
            constraintValidator.beginTime( entity.getBeginTime() );
        }
        if ( StringUtils.isNotEmpty( entity.getEndTime() ) )
        {
            constraintValidator.endTime( entity.getEndTime() );
        }
        if ( StringUtils.isNotEmpty( entity.getBeginDate() ) )
        {
            constraintValidator.beginDate( entity.getBeginDate() );
        }
        if ( StringUtils.isNotEmpty( entity.getEndDate() ) )
        {
            constraintValidator.endDate( entity.getEndDate() );
        }
        if ( StringUtils.isNotEmpty( entity.getDayMask() ) )
        {
            constraintValidator.dayMask( entity.getDayMask() );
        }
        if ( StringUtils.isNotEmpty( entity.getBeginLockDate() ) )
        {
            constraintValidator.beginDate( entity.getBeginDate() );
        }
        if ( StringUtils.isNotEmpty( entity.getEndLockDate() ) )
        {
            constraintValidator.endDate( entity.getEndLockDate() );
        }
    }
}