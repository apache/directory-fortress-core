/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.arbac;

import com.jts.fortress.FinderException;
import com.jts.fortress.RemoveException;
import com.jts.fortress.SecurityException;
import com.jts.fortress.constants.GlobalErrIds;

import java.util.List;

import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.rbac.RoleUtil;
import com.jts.fortress.util.attr.VUtil;
import org.apache.log4j.Logger;

/**
 * Process module for the AdminRole entity.  This class performs data validations and error mapping.  It is typically called
 * by internal Fortress delegated manager classes ({@link com.jts.fortress.arbac.DelegatedAdminMgrImpl}, {@link com.jts.fortress.arbac.DelegatedAccessMgrImpl},
 * {@link com.jts.fortress.arbac.DelegatedReviewMgrImpl}, ...) and not intended for external non-Fortress clients.  This class will accept,
 * {@link com.jts.fortress.arbac.AdminRole}, validate its contents and forward on to it's corresponding DAO class {@link com.jts.fortress.arbac.AdminRoleDAO}.
 * <p>
 * Class will throw {@link com.jts.fortress.SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link com.jts.fortress.FinderException},
 * {@link com.jts.fortress.CreateException},{@link com.jts.fortress.UpdateException},{@link com.jts.fortress.RemoveException}),
 *  or {@link com.jts.fortress.ValidationException} as {@link com.jts.fortress.SecurityException}s with appropriate
 * error id from {@link GlobalErrIds}.
 * <p>
 * This object is thread safe.
 * <p/>

 *
 * @author smckinn
 * @created November 13, 2009
 */
public class AdminRoleP
{
    private static final String OCLS_NM = AdminRoleP.class.getName();
    private static final Logger log = Logger.getLogger(OCLS_NM);
    private static final AdminRoleDAO rDao = new AdminRoleDAO();
    private static final OrgUnitP op = new OrgUnitP();

    /**
     * Return a fully populated Admin Role entity for a given Admin Role name.  If matching record not found a
     * SecurityException will be thrown.
     *
     * @param name contains full Admin Role name.
     * @return AdminRole entity containing all attributes associated with Administrative Role in directory.
     * @throws com.jts.fortress.SecurityException in the event AdminRole not found or DAO search error.
     */
    public AdminRole read(String name)
        throws SecurityException
    {
        return rDao.getRole(name);
    }

    /**
     * Takes a search string that contains full or partial Admin Role name in directory.
     *
     * @param searchVal contains full or partial Admin role name.
     * @return List of type Role containing fully populated matching Admin Role entities.  If no records found this will be empty.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final List<AdminRole> search(String searchVal)
        throws SecurityException
    {
        return rDao.findRoles(searchVal);
    }


    /**
     * Takes a search string that contains full or partial Admin Role name in directory.
     *
     * @param searchVal contains full or partial Admin role name.
     * @param limit     specify the max number of records to return in result set.
     * @return List of type String containing Admin Role name of all matching User entities.  If no records found this will be empty.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final List<String> search(String searchVal, int limit)
        throws SecurityException
    {
        return rDao.findRoles(searchVal, limit);
    }


    /**
     * Adds a new Admin Role entity to directory.  The Role entity input object will be validated to ensure that:
     * role name is present, and reasonability checks on all of the other populated values.
     *
     * @param entity Admin Role entity contains data targeted for insertion.
     * @return AdminRole entity copy of input + additional attributes (internalId) that were added by op.
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    public AdminRole add(AdminRole entity)
        throws SecurityException
    {
        validate(entity);
        return rDao.create(entity);
    }


    /**
     * Updates existing AdminRole entity in directory.  For example the AdminRole description and temporal constraints
     * updated.
     *
     * @param entity Admin Role entity contains data targeted for updating.
     * @return AdminRole entity contains fully populated updated entity.
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    public AdminRole update(AdminRole entity)
        throws SecurityException
    {
        validate(entity);
        entity = rDao.update(entity);
        return read(entity.getName());
    }


    /**
     * This command assigns a user to an admin role.
     * Successful completion of this op, the following occurs:
     * </p>
     * <ul>
     * <li> User entity (resides in people container) has role assignment added to aux object class attached to actual user record.
     * <li> AdminRole entity (resides in admin role container) has userId added as role occupant.
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
     *
     * @param entity contains userId and admin role name and (optional) role temporal constraints.
     * @return AdminRole contains copy of input entity and additional data processed by request.
     * @throws com.jts.fortress.SecurityException in the event data error in user or role objects or system error.
     */
    public AdminRole assign(AdminRole entity, String userDn)
        throws SecurityException
    {
        return rDao.assign(entity, userDn);
    }


    /**
     * Add the User dn occupant attribute to the OrganizationalRole entity in ldap.  This method is called by AdminMgrImpl
     * when the User is being added.
     *
     * @param uRoles contains a collection of UserAdminRole being targeted for assignment.
     * @param userDn contains the userId targeted for attribute addition.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final void addOccupant(List<UserAdminRole> uRoles, String userDn)
        throws SecurityException
    {
        if (VUtil.isNotNullOrEmpty(uRoles))
        {
            for (UserAdminRole uRole : uRoles)
            {
                assign(new AdminRole(uRole.getName()), userDn);
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
                deassign(new AdminRole(roleNm), userDn);
            }
        }
        catch (FinderException fe)
        {
            String error = OCLS_NM + ".removeOccupant userDn [" + userDn + "] caught FinderException=" + fe;
            throw new SecurityException(GlobalErrIds.ARLE_REMOVE_OCCUPANT_FAILED, error, fe);
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
     * @return AdminRole contains copy of input entity and additional data processed by request.
     * @throws com.jts.fortress.SecurityException - in the event data error in user or role objects or system error.
     */
    public AdminRole deassign(AdminRole entity, String userDn)
        throws SecurityException
    {
        return rDao.deassign(entity, userDn);
    }


    /**
     * This method performs a "hard" delete.  It completely the Admin Role node from the ldap directory.
     * Admin Role entity must exist in directory prior to making this call else exception will be thrown.
     *
     * @param entity Contains the name of the Admin Role targeted for deletion.
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    public void delete(AdminRole entity)
        throws SecurityException
    {
        try
        {
            rDao.remove(entity);
        }
        catch (RemoveException re)
        {
            String error = OCLS_NM + ".delete name [" + entity.getName() + "] caught RemoveException=" + re;
            log.error(error);
            throw new SecurityException(GlobalErrIds.ARLE_DELETE_FAILED, error, re);
        }
    }


    /**
     * Method will perform simple validations to ensure the integrity of the Admin Role entity targeted for insertion
     * or updating in directory.  For example the Admin Role temporal constraints will be validated.  Data reasonability
     * checks will be performed on all non-null attributes.  Validations will be performed on ARBAC constraints as well.
     *
     * @param entity contains data targeted for insertion or update.
     * @throws com.jts.fortress.SecurityException in the event of data validation error or DAO error on Org validation.
     */
    private void validate(AdminRole entity)
        throws SecurityException
    {
        VUtil.safeText(entity.getName(), GlobalIds.ROLE_LEN);
        if (VUtil.isNotNullOrEmpty(entity.getBeginRange()) && VUtil.isNotNullOrEmpty(entity.getEndRange()))
        {
            VUtil.safeText(entity.getBeginRange(), GlobalIds.ROLE_LEN);
            VUtil.safeText(entity.getEndRange(), GlobalIds.ROLE_LEN);
            if (entity.getBeginRange().equalsIgnoreCase(entity.getEndRange()) && (!entity.isBeginInclusive() || !entity.isEndInclusive()))
            {
                String error = OCLS_NM + ".validate invalid range detected for role name [" + entity.getName() + "] non inclusive endpoint for identical range [" + entity.getBeginRange() + "] begin inclusive [" + entity.isBeginInclusive() + "] end inclusive [" + entity.isEndInclusive() + "]";
                log.warn(error);
                throw new SecurityException(GlobalErrIds.ARLE_INVLD_RANGE_INCLUSIVE, error);
            }
            else if (!RoleUtil.isParent(entity.getBeginRange(), entity.getEndRange()) && !entity.getBeginRange().equalsIgnoreCase(entity.getEndRange()))
            //public static boolean isParent(String child, String parent)
            {
                String error = OCLS_NM + ".validate invalid range detected for role name [" + entity.getName() + "] begin range [" + entity.getBeginRange() + "] end range [" + entity.getEndRange() + "]";
                log.warn(error);
                throw new SecurityException(GlobalErrIds.ARLE_INVLD_RANGE, error);
            }
        }
        else if (!VUtil.isNotNullOrEmpty(entity.getBeginRange()) && VUtil.isNotNullOrEmpty(entity.getEndRange()))
        {
            String error = OCLS_NM + ".validate role name [" + entity.getName() + "] begin range value null or empty.";
            log.warn(error);
            throw new SecurityException(GlobalErrIds.ARLE_BEGIN_RANGE_NULL, error);
        }
        else if (VUtil.isNotNullOrEmpty(entity.getBeginRange()) && !VUtil.isNotNullOrEmpty(entity.getEndRange()))
        {
            String error = OCLS_NM + ".validate role name [" + entity.getName() + "] end range value null or empty.";
            log.warn(error);
            throw new SecurityException(GlobalErrIds.ARLE_END_RANGE_NULL, error);
        }
        if (VUtil.isNotNullOrEmpty(entity.getDescription()))
        {
            VUtil.description(entity.getDescription());
        }
        if (entity.getTimeout() >= 0)
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
        if (VUtil.isNotNullOrEmpty(entity.getOsU()))
        {
            for (String ou : entity.getOsU())
            {
                OrgUnit inOe = new OrgUnit(ou);
                inOe.setType(OrgUnit.Type.USER);
                op.read(inOe);
            }
        }
        if (VUtil.isNotNullOrEmpty(entity.getOsP()))
        {
            for (String ou : entity.getOsP())
            {
                OrgUnit inOe = new OrgUnit(ou);
                inOe.setType(OrgUnit.Type.PERM);
                op.read(inOe);
            }
        }
    }
}