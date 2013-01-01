/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.SecurityException;

import java.util.List;


/**
 * This class is process layer for Fortress audit data.  It performs data validation
 * and data mapping functions.
 * Process module for the for Fortress audit data.  It performs data validation and data mapping functions.
 * The audit data is passed using {@link AuthZ} class.  This class does perform simple data validations to ensure data reasonability and
 * the required fields are present..<BR>
 * The methods in this class are called by {@link AuditMgrImpl} methods during audit log interrogations.
 * <p/>
 * Class will throw {@link com.jts.fortress.SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exception {@link com.jts.fortress.FinderException},
 * or {@link com.jts.fortress.ValidationException} as {@link com.jts.fortress.SecurityException}s with appropriate
 * error id from {@link com.jts.fortress.GlobalErrIds}.
 * <p/>
 * This class performs simple data validations.
 * <p/>
 * This class is thread safe.
 *
 * @author Shawn McKinney
 */
final class AuditP
{
    private static final AuditDAO aDao = new AuditDAO();

    /**
     * Package private constructor
     */
    AuditP()
    {}

    /**
     * This method returns a list of authorization events for a particular user {@link UserAudit#userId}
     * and given timestamp field {@link UserAudit#beginDate}.<BR>
     * Method also can discriminate between all events or failed only by setting {@link UserAudit#failedOnly}.
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws com.jts.fortress.SecurityException if a runtime system error occurs.
     */
    final List<AuthZ> getAuthZs(UserAudit uAudit)
        throws SecurityException
    {
        return aDao.getAllAuthZs(uAudit);
    }


    /**
     * This method returns a list of authorization events for a particular user {@link UserAudit#userId},
     * object {@link UserAudit#objName}, and given timestamp field {@link UserAudit#beginDate}.<BR>
     * Method also can discriminate between all events or failed only by setting flag {@link UserAudit#failedOnly}..
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws SecurityException if a runtime system error occurs.
     */
    final List<AuthZ> searchAuthZs(UserAudit uAudit)
        throws SecurityException
    {
        return aDao.searchAuthZs(uAudit);
    }


    /**
     * This method returns a list of authentication audit events for a particular user {@link UserAudit#userId},
     * and given timestamp field {@link UserAudit#beginDate}.<BR>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Bind.  Each Bind object contains one bind event.
     * @throws com.jts.fortress.SecurityException if a runtime system error occurs.
     */
    final List<Bind> searchBinds(UserAudit uAudit)
        throws SecurityException
    {
        return aDao.searchBinds(uAudit);
    }


    /**
     * This method returns a list of sessions created for a given user {@link UserAudit#userId},
     * and timestamp {@link UserAudit#beginDate}.<BR>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws com.jts.fortress.SecurityException if a runtime system error occurs.
     */
    final List<Mod> searchUserMods(UserAudit uAudit)
        throws SecurityException
    {
        return aDao.searchUserMods(uAudit);
    }


    /**
     * This method returns a list of admin operations events for a particular entity {@link UserAudit#dn},
     * object {@link UserAudit#objName} and timestamp {@link UserAudit#beginDate}.  If the internal
     * userId {@link UserAudit#internalUserId} is set it will limit search by that field.
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws com.jts.fortress.SecurityException if a runtime system error occurs.
     */
    final List<Mod> searchAdminMods(UserAudit uAudit)
        throws SecurityException
    {
        return aDao.searchAdminMods(uAudit);
    }


    /**
     * This method returns a list of failed authentication events for a particular invalid user {@link UserAudit#userId},
     * and given timestamp {@link UserAudit#beginDate}.  If the {@link UserAudit#failedOnly} is true it will
     * return only authentication attempts made with invalid userId.
     * </p>
     * This is possible because Fortress performs read on user before the bind.
     * </p>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one failed authentication event.
     * @throws com.jts.fortress.SecurityException if a runtime system error occurs.
     */
    final List<AuthZ> searchInvalidAuthNs(UserAudit uAudit)
        throws SecurityException
    {
        return aDao.searchInvalidAuthNs(uAudit);
    }
}

