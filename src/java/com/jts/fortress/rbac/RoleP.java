/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.FinderException;
import com.jts.fortress.SecurityException;
import com.jts.fortress.ValidationException;
import com.jts.fortress.constants.GlobalErrIds;

import java.util.List;

import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.attr.VUtil;

/**
 * Process module for the Role entity.  This class performs data validations and error mapping.  It is typically called
 * by internal Fortress manager classes ({@link com.jts.fortress.rbac.AdminMgrImpl}, {@link com.jts.fortress.rbac.AccessMgrImpl},
 * {@link com.jts.fortress.rbac.ReviewMgrImpl}, ...) and not intended for external non-Fortress clients.  This class will accept,
 * {@link Role}, validate its contents and forward on to it's corresponding DAO class {@link RoleDAO}.
 * <p>
 * Class will throw {@link com.jts.fortress.SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link com.jts.fortress.FinderException},
 * {@link com.jts.fortress.CreateException},{@link com.jts.fortress.UpdateException},{@link com.jts.fortress.RemoveException}),
 *  or {@link com.jts.fortress.ValidationException} as {@link com.jts.fortress.SecurityException}s with appropriate
 * error id from {@link com.jts.fortress.constants.GlobalErrIds}.
 * <p>
 * This object is thread safe.
 * </p>

 *
 * @author kpmckinn
 * @created October 29, 2009
 */
public final class RoleP
{
    private static final String OCLS_NM = RoleP.class.getName();
    private static final RoleDAO rDao = new RoleDAO();

    /**
     * Return a fully populated Role entity for a given RBAC role name.  If matching record not found a
     * SecurityException will be thrown.
     *
     * @param name contains full role name for RBAC role in directory.
     * @return Role entity containing all attributes associated with Role in directory.
     * @throws SecurityException in the event Role not found or DAO search error.
     */
    public Role read(String name)
        throws SecurityException
    {
        return rDao.getRole(name);
    }

    /**
     * Takes a search string that contains full or partial RBAC Role name in directory.
     *
     * @param searchVal contains full or partial RBAC role name.
     * @return List of type Role containing fully populated matching RBAC Role entities.  If no records found this will be empty.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final List<Role> search(String searchVal)
        throws SecurityException
    {
        return rDao.findRoles(searchVal);
    }


    /**
     * Takes a search string that contains full or partial RBAC Role name in directory.
     * This search is used by RealmMgr for Websphere.
     *
     * @param searchVal contains full or partial RBAC role name.
     * @param limit     specify the max number of records to return in result set.
     * @return List of type String containing RBAC Role name of all matching User entities.  If no records found this will be empty.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final List<String> search(String searchVal, int limit)
        throws SecurityException
    {
        return rDao.findRoles(searchVal, limit);
    }


    /**
     * Adds a new Role entity to directory.  The Role entity input object will be validated to ensure that:
     * role name is present, and reasonability checks on all of the other populated values.
     *
     * @param entity Role entity contains data targeted for insertion.
     * @return Role entity copy of input + additional attributes (internalId) that were added by op.
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    public Role add(Role entity)
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
    public Role update(Role entity)
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
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    public Role assign(Role entity, String userDn)
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
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    public Role deassign(Role entity, String userDn)
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
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final void addOccupant(List<UserRole> uRoles, String userDn)
        throws SecurityException
    {
        if (VUtil.isNotNullOrEmpty(uRoles))
        {
            for (UserRole uRole : uRoles)
            {
                assign(new Role(uRole.getName()), userDn);
            }
        }
    }


    /**
     * Remove the User dn occupant attribute from the OrganizationalRole entity in ldap.  This method is called by AdminMgrImpl
     * when the User is being deleted.
     *
     * @param userDn contains the userId targeted for attribute removal.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final void removeOccupant(String userDn)
        throws SecurityException
    {
        List<String> list;
        try
        {
            list = rDao.findAssignedRoles(userDn);
            for (String roleNm : list)
            {
                deassign(new Role(roleNm), userDn);
            }
        }
        catch (FinderException fe)
        {
            String error = OCLS_NM + ".removeOccupant userDn <" + userDn + "> caught FinderException=" + fe;
            throw new SecurityException(GlobalErrIds.ROLE_REMOVE_OCCUPANT_FAILED, error, fe);
        }
    }


    /**
     * This method performs a "hard" delete.  It completely the RBAC Role node from the ldap directory.
     * RBAC Role entity must exist in directory prior to making this call else exception will be thrown.
     *
     * @param entity Contains the name of the RBAC Role targeted for deletion.
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    public void delete(Role entity)
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
     * @throws com.jts.fortress.ValidationException in the event of data validation error or Org validation.
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