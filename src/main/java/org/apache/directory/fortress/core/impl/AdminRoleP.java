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
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.RemoveException;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.util.ConstraintValidator;
import org.apache.directory.fortress.core.model.Graphable;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.model.UserAdminRole;
import org.apache.directory.fortress.core.util.VUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Process module for the AdminRole entity.  This class performs data validations and error mapping.  It is typically called
 * by internal Fortress delegated manager classes ({@link DelAdminMgrImpl}, {@link DelAccessMgrImpl},
 * {@link DelReviewMgrImpl}, ...) and not intended for external non-Fortress clients.  This class will accept,
 * {@link org.apache.directory.fortress.core.model.AdminRole}, validate its contents and forward on to it's corresponding DAO class {@link AdminRoleDAO}.
 * <p>
 * Class will throw {@link SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link org.apache.directory.fortress.core.FinderException},
 * {@link org.apache.directory.fortress.core.CreateException},{@link org.apache.directory.fortress.core.UpdateException},{@link org.apache.directory.fortress.core.RemoveException}),
 *  or {@link org.apache.directory.fortress.core.ValidationException} as {@link SecurityException}s with appropriate
 * error id from {@link GlobalErrIds}.
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class AdminRoleP
{
    private static final String CLS_NM = AdminRoleP.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private AdminRoleDAO rDao = new AdminRoleDAO();
    private OrgUnitP op = new OrgUnitP();
    private static final ConstraintValidator constraintValidator = VUtil.getConstraintValidator();

    /**
     * Return a fully populated Admin Role entity for a given Admin Role name.  If matching record not found a
     * SecurityException will be thrown.
     *
     * @param adminRole contains full Admin Role name.
     * @return AdminRole entity containing all attributes associated with Administrative Role in directory.
     * @throws SecurityException in the event AdminRole not found or DAO search error.
     */
    AdminRole read( AdminRole adminRole ) throws SecurityException
    {
        return rDao.getRole( adminRole );
    }


    /**
     * Takes a search string that contains full or partial Admin Role name in directory.
     *
     * @param adminRole contains full or partial Admin role name.
     * @return List of type Role containing fully populated matching Admin Role entities.  If no records found this will be empty.
     * @throws SecurityException in the event of DAO search error.
     */
    List<AdminRole> search( AdminRole adminRole )
        throws SecurityException
    {
        return rDao.findRoles( adminRole );
    }


    /**
     * Takes a search string that contains full or partial Admin Role name in directory.
     *
     * @param adminRole contains full or partial Admin role name.
     * @param limit     specify the max number of records to return in result set.
     * @return List of type String containing Admin Role name of all matching User entities.  If no records found this will be empty.
     * @throws SecurityException in the event of DAO search error.
     */
    List<String> search( AdminRole adminRole, int limit )
        throws SecurityException
    {
        return rDao.findRoles( adminRole, limit );
    }


    /**
     * Return all AdminRoles that have a parent assignment.  This used for hierarchical processing.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return List of type AdminRole containing {@link AdminRole#name} and {@link AdminRole#parents} populated.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Graphable> getAllDescendants( String contextId )
        throws SecurityException
    {
        return rDao.getAllDescendants( contextId );
    }


    /**
     * Adds a new Admin Role entity to directory.  The Role entity input object will be validated to ensure that:
     * role name is present, and reasonability checks on all of the other populated values.
     *
     * @param entity Admin Role entity contains data targeted for insertion.
     * @return AdminRole entity copy of input + additional attributes (internalId) that were added by op.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    AdminRole add( AdminRole entity )
        throws SecurityException
    {
        validate( entity );
        return rDao.create( entity );
    }


    /**
     * Updates existing AdminRole entity in directory.   e.g., the AdminRole description and temporal constraints
     * updated.
     *
     * @param entity Admin Role entity contains data targeted for updating.
     * @return AdminRole entity contains fully populated updated entity.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    AdminRole update( AdminRole entity ) throws SecurityException
    {
        validate( entity );
        AdminRole updateEntity = rDao.update( entity );
        return read( updateEntity );
    }


    /**
     * Removes parent role assignments from Role entity in directory.
     * updated.
     *
     * @param entity Admin Role entity contains data targeted for updating.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    void deleteParent( AdminRole entity ) throws SecurityException
    {
        validate( entity );
        rDao.deleteParent( entity );
    }


    /**
     * This command assigns a user to an admin role.
     * Successful completion of this op, the following occurs:
     * <p>
     * <ul>
     * <li> User entity (resides in people container) has role assignment added to aux object class attached to actual user record.
     * <li> AdminRole entity (resides in admin role container) has userId added as role occupant.
     * <li> (optional) Temporal constraints may be associated with <code>ftUserAttrs</code> aux object class based on:
     * <ul>
     * <li> timeout - number (in minutes) of session inactivity time allowed.
     * <li> beginDate - YYYYMMDD - determines date when role may be activated.
     * <li> endDate - YYMMDD - indicates latest date role may be activated.
     * <li> beginLockDate - YYYYMMDD - determines beginning of enforced inactive status
     * <li> endLockDate - YYMMDD - determines end of enforced inactive status.
     * <li> beginTime - HHMM - determines begin hour role may be activated in user's session.
     * <li> endTime - HHMM - determines end hour role may be activated in user's session.*
     * <li> dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day of week role may be activated.
     * </ul>
     * </ul>
     *
     * @param entity contains userId and admin role name and (optional) role temporal constraints.
     * @param userDn contains the DN of user being assigned.
     * @return AdminRole contains copy of input entity and additional data processed by request.
     * @throws SecurityException in the event data error in user or role objects or system error.
     */
    AdminRole assign( AdminRole entity, String userDn ) throws SecurityException
    {
        return rDao.assign( entity, userDn );
    }


    /**
     * Add the User dn occupant attribute to the OrganizationalRole entity in ldap.  This method is called by AdminMgrImpl
     * when the User is being added.
     *
     * @param uRoles contains a collection of UserAdminRole being targeted for assignment.
     * @param userDn contains the userId targeted for attribute addition.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @throws SecurityException in the event of DAO search error.
     */
    void addOccupant( List<UserAdminRole> uRoles, String userDn, String contextId )
        throws SecurityException
    {
        if ( CollectionUtils.isNotEmpty( uRoles ) )
        {
            for ( UserAdminRole uRole : uRoles )
            {
                AdminRole role = new AdminRole( uRole.getName() );
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
    void removeOccupant( String userDn, String contextId )
        throws SecurityException
    {
        List<String> list;
        try
        {
            list = rDao.findAssignedRoles( userDn, contextId );
            for ( String roleNm : list )
            {
                AdminRole role = new AdminRole( roleNm );
                role.setContextId( contextId );
                deassign( role, userDn );
            }
        }
        catch ( FinderException fe )
        {
            String error = "removeOccupant userDn [" + userDn + "] caught FinderException=" + fe;
            throw new SecurityException( GlobalErrIds.ARLE_REMOVE_OCCUPANT_FAILED, error, fe );
        }
    }


    /**
     * This method removes assigned admin role from user entity.  Both user and admin role entities must exist and have role relationship
     * before calling this method.
     * Successful completion:
     * del Role to User assignment in User data set
     * AND
     * User to Role assignment in Admin Role data set.
     *
     * @param entity contains userId and admin role name targeted for removal.
     * @param userDn contains the userId targeted for attribute removal.
     * @return AdminRole contains copy of input entity and additional data processed by request.
     * @throws SecurityException - in the event data error in user or role objects or system error.
     */
    AdminRole deassign( AdminRole entity, String userDn ) throws SecurityException
    {
        return rDao.deassign( entity, userDn );
    }


    /**
     * This method performs a "hard" delete.  It completely the Admin Role node from the ldap directory.
     * Admin Role entity must exist in directory prior to making this call else exception will be thrown.
     *
     * @param entity Contains the name of the Admin Role targeted for deletion.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    void delete( AdminRole entity ) throws SecurityException
    {
        try
        {
            rDao.remove( entity );
        }
        catch ( RemoveException re )
        {
            String error = "delete name [" + entity.getName() + "] caught RemoveException=" + re;
            LOG.error( error );
            throw new SecurityException( GlobalErrIds.ARLE_DELETE_FAILED, error, re );
        }
    }
    
    /**
     * Method will perform simple validations to ensure the integrity of the Admin Role entity targeted for insertion
     * or updating in directory.  For example the Admin Role temporal constraints will be validated.  Data reasonability
     * checks will be performed on all non-null attributes.  Validations will be performed on ARBAC constraints as well.
     *
     * @param entity contains data targeted for insertion or update.
     * @throws SecurityException in the event of data validation error or DAO error on Org validation.
     */
    private void validate( AdminRole entity )
        throws SecurityException
    {
        VUtil.safeText( entity.getName(), GlobalIds.ROLE_LEN );

        if ( StringUtils.isNotEmpty( entity.getBeginRange() ) && StringUtils.isNotEmpty( entity.getEndRange() ) )
        {
            VUtil.safeText( entity.getBeginRange(), GlobalIds.ROLE_LEN );
            VUtil.safeText( entity.getEndRange(), GlobalIds.ROLE_LEN );

            if ( entity.getBeginRange().equalsIgnoreCase( entity.getEndRange() )
                && ( !entity.isBeginInclusive() || !entity.isEndInclusive() ) )
            {
                String error = "validate invalid range detected for role name [" + entity.getName()
                    + "] non inclusive endpoint for identical range [" + entity.getBeginRange() + "] begin inclusive ["
                    + entity.isBeginInclusive() + "] end inclusive [" + entity.isEndInclusive() + "]";
                LOG.warn( error );
                throw new SecurityException( GlobalErrIds.ARLE_INVLD_RANGE_INCLUSIVE, error );
            }
            else if ( !RoleUtil.getInstance().isParent( entity.getBeginRange(), entity.getEndRange(), entity.getContextId() )
                && !entity.getBeginRange().equalsIgnoreCase( entity.getEndRange() ) )
            {
                String error = "validate invalid range detected for role name [" + entity.getName()
                    + "] begin range [" + entity.getBeginRange() + "] end range [" + entity.getEndRange() + "]";
                LOG.warn( error );
                throw new SecurityException( GlobalErrIds.ARLE_INVLD_RANGE, error );
            }
        }
        else if ( StringUtils.isEmpty( entity.getBeginRange() ) && StringUtils.isNotEmpty( entity.getEndRange() ) )
        {
            String error = "validate role name [" + entity.getName() + "] begin range value null or empty.";
            LOG.warn( error );
            throw new SecurityException( GlobalErrIds.ARLE_BEGIN_RANGE_NULL, error );
        }
        else if ( StringUtils.isNotEmpty( entity.getBeginRange() ) && StringUtils.isEmpty( entity.getEndRange() ) )
        {
            String error = "validate role name [" + entity.getName() + "] end range value null or empty.";
            LOG.warn( error );
            throw new SecurityException( GlobalErrIds.ARLE_END_RANGE_NULL, error );
        }

        if ( StringUtils.isNotEmpty( entity.getDescription() ) )
        {
            VUtil.description( entity.getDescription() );
        }

        if ( entity.getTimeout() >= 0 )
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

        if ( CollectionUtils.isNotEmpty( entity.getOsUSet() ) )
        {
            validateOrgs( entity.getOsUSet(), OrgUnit.Type.USER, entity.getContextId() );
        }

        if ( CollectionUtils.isNotEmpty( entity.getOsPSet() ) )
        {
            validateOrgs( entity.getOsPSet(), OrgUnit.Type.PERM, entity.getContextId() );
        }
    }

    /**
     * Called by validate()
     *
     * @param orgs
     * @param contextId
     * @throws SecurityException
     */
    private void validateOrgs( Set<String> orgs, OrgUnit.Type type, String contextId ) throws SecurityException
    {
        for ( String ou : orgs )
        {
            OrgUnit inOe = new OrgUnit( ou );
            inOe.setType( type );
            inOe.setContextId( contextId );
            op.read( inOe );
        }
    }
}