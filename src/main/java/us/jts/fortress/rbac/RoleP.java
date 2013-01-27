/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import us.jts.fortress.FinderException;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.GlobalIds;
import us.jts.fortress.SecurityException;
import us.jts.fortress.ValidationException;

import java.util.List;

import us.jts.fortress.util.attr.VUtil;

/**
 * Process module for the Role entity.  This class performs data validations and error mapping.  It is typically called
 * by internal Fortress manager classes ({@link us.jts.fortress.rbac.AdminMgrImpl}, {@link us.jts.fortress.rbac.AccessMgrImpl},
 * {@link us.jts.fortress.rbac.ReviewMgrImpl}, ...) and not intended for external non-Fortress clients.  This class will accept,
 * {@link Role}, validate its contents and forward on to it's corresponding DAO class {@link RoleDAO}.
 * <p>
 * Class will throw {@link us.jts.fortress.SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link us.jts.fortress.FinderException},
 * {@link us.jts.fortress.CreateException},{@link us.jts.fortress.UpdateException},{@link us.jts.fortress.RemoveException}),
 *  or {@link us.jts.fortress.ValidationException} as {@link us.jts.fortress.SecurityException}s with appropriate
 * error id from {@link us.jts.fortress.GlobalErrIds}.
 * <p>
 * This class is thread safe.
 * </p>

 *
 * @author Kevin McKinney
 */
final class RoleP
{
    private static final String CLS_NM = RoleP.class.getName();
    private static final RoleDAO rDao = new RoleDAO();

    /**
     * Package private
     */
    RoleP()
    {}

    /**
     * Return a fully populated Role entity for a given RBAC role name.  If matching record not found a
     * SecurityException will be thrown.
     *
     * @param role contains full role name for RBAC role in directory.
     * @return Role entity containing all attributes associated with Role in directory.
     * @throws SecurityException in the event Role not found or DAO search error.
     */
    final Role read(Role role)
        throws SecurityException
    {
        return rDao.getRole(role);
    }

    /**
     * Takes a search string that contains full or partial RBAC Role name in directory.
     *
     * @param role contains full or partial RBAC role name.
     * @return List of type Role containing fully populated matching RBAC Role entities.  If no records found this will be empty.
     * @throws us.jts.fortress.SecurityException in the event of DAO search error.
     */
    final List<Role> search(Role role)
        throws SecurityException
    {
        return rDao.findRoles(role);
    }


    /**
     * Takes a search string that contains full or partial RBAC Role name in directory.
     * This search is used by RealmMgr for Websphere.
     *
     * @param role contains full or partial RBAC role name.
     * @param limit     specify the max number of records to return in result set.
     * @return List of type String containing RBAC Role name of all matching User entities.  If no records found this will be empty.
     * @throws us.jts.fortress.SecurityException in the event of DAO search error.
     */
    final List<String> search(Role role, int limit)
        throws SecurityException
    {
        return rDao.findRoles(role, limit);
    }


    /**
     * Return all Roles that have a parent assignment.  This used for hierarchical processing.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return List of type Role containing {@link Role#name} and {@link Role#parents} populated.
     * @throws us.jts.fortress.SecurityException in the event of DAO search error.
     */
    final List<Graphable> getAllDescendants(String contextId)
        throws SecurityException
    {
        return rDao.getAllDescendants(contextId);
    }


    /**
     * Adds a new Role entity to directory.  The Role entity input object will be validated to ensure that:
     * role name is present, and reasonability checks on all of the other populated values.
     *
     * @param entity Role entity contains data targeted for insertion.
     * @return Role entity copy of input + additional attributes (internalId) that were added by op.
     * @throws us.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    final Role add(Role entity)
        throws SecurityException
    {
        validate(entity);
        return rDao.create(entity);
    }


    /**
     * Updates existing Role entity in directory.  For example the Role description and temporal constraints
     * updated.
     *
     * @param entity Role entity contains data targeted for updating.
     * @return Role entity contains fully populated updated entity.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    final Role update(Role entity)
        throws SecurityException
    {
        validate(entity);
        return rDao.update(entity);
    }


    /**
     * Method will add the "roleOccupant" attribute on OpenLDAP entry which represents an RBAC Role assignment in Fortress.
     *
     * @param entity contains the role name targeted.
     * @param userDn String contains the dn for the user entry that is being assigned the RBAC Role.
     * @return Role containing copy of input data.
     * @throws us.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    final Role assign(Role entity, String userDn)
        throws SecurityException
    {
        return rDao.assign(entity, userDn);
    }


    /**
     * Method will remove the "roleOccupant" attribute on OpenLDAP entry which represents an RBAC Role assignment in Fortress.
     *
     * @param entity contains the role name targeted.
     * @param userDn String contains the dn for the user entry that is being assigned the RBAC Role.
     * @return Role containing copy of input data.
     * @throws us.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    final Role deassign(Role entity, String userDn)
        throws SecurityException
    {
        return entity = rDao.deassign(entity, userDn);
    }


    /**
     * Add the User dn occupant attribute to the OrganizationalRole entity in ldap.  This method is called by AdminMgrImpl
     * when the User is being added.
     *
     * @param uRoles contains a collection of UserRole being targeted for assignment.
     * @param userDn contains the userId targeted for addition.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @throws us.jts.fortress.SecurityException in the event of DAO search error.
     */
    final void addOccupant(List<UserRole> uRoles, String userDn, String contextId)
        throws SecurityException
    {
        if (VUtil.isNotNullOrEmpty(uRoles))
        {
            for (UserRole uRole : uRoles)
            {
                Role role = new Role(uRole.getName());
                role.setContextId(contextId);
                assign(role, userDn);
            }
        }
    }


    /**
     * Remove the User dn occupant attribute from the OrganizationalRole entity in ldap.  This method is called by AdminMgrImpl
     * when the User is being deleted.
     *
     * @param userDn contains the userId targeted for attribute removal.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @throws us.jts.fortress.SecurityException in the event of DAO search error.
     */
    final void removeOccupant(String userDn, String contextId)
        throws SecurityException
    {
        List<String> list;
        try
        {
            list = rDao.findAssignedRoles(userDn, contextId);
            for (String roleNm : list)
            {
                Role role = new Role(roleNm);
                role.setContextId(contextId);
                deassign(role, userDn);
            }
        }
        catch (FinderException fe)
        {
            String error = CLS_NM + ".removeOccupant userDn [" + userDn + "] caught FinderException=" + fe;
            throw new SecurityException(GlobalErrIds.ROLE_REMOVE_OCCUPANT_FAILED, error, fe);
        }
    }


    /**
     * This method performs a "hard" delete.  It completely the RBAC Role node from the ldap directory.
     * RBAC Role entity must exist in directory prior to making this call else exception will be thrown.
     *
     * @param entity Contains the name of the RBAC Role targeted for deletion.
     * @throws us.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    void delete(Role entity)
        throws SecurityException
    {
        rDao.remove(entity);
    }


    /**
     * Method will perform simple validations to ensure the integrity of the RBAC Role entity targeted for insertion
     * or updating in directory.  For example the Role temporal constraints will be validated.  Data reasonability
     * checks will be performed on all non-null attributes.
     *
     * @param entity contains data targeted for insertion or update.
     * @throws us.jts.fortress.ValidationException in the event of data validation error or Org validation.
     */
    private void validate(Role entity)
        throws ValidationException
    {
        VUtil.safeText(entity.getName(), GlobalIds.ROLE_LEN);
        if (VUtil.isNotNullOrEmpty(entity.getDescription()))
        {
            VUtil.description(entity.getDescription());
        }
        if (VUtil.isNotNullOrEmpty(entity.getTimeout()))
        {
            VUtil.timeout(entity.getTimeout());
        }
        if (VUtil.isNotNullOrEmpty(entity.getBeginTime()))
        {
            VUtil.beginTime(entity.getBeginTime());
        }
        if (VUtil.isNotNullOrEmpty(entity.getEndTime()))
        {
            VUtil.endTime(entity.getEndTime());
        }
        if (VUtil.isNotNullOrEmpty(entity.getBeginDate()))
        {
            VUtil.beginDate(entity.getBeginDate());
        }
        if (VUtil.isNotNullOrEmpty(entity.getEndDate()))
        {
            VUtil.endDate(entity.getEndDate());
        }
        if (VUtil.isNotNullOrEmpty(entity.getDayMask()))
        {
            VUtil.dayMask(entity.getDayMask());
        }
        if (VUtil.isNotNullOrEmpty(entity.getBeginLockDate()))
        {
            VUtil.beginDate(entity.getBeginDate());
        }
        if (VUtil.isNotNullOrEmpty(entity.getEndLockDate()))
        {
            VUtil.endDate(entity.getEndLockDate());
        }
    }
}