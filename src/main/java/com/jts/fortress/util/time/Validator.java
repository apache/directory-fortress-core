/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.util.time;

import com.jts.fortress.rbac.Session;

/**
 * Interface used by Fortress to provide pluggable validation routines for constraints.
 *
 * <h4> Constraint Targets</h4>
 * <ol>
 * <li>{@link com.jts.fortress.rbac.User}</li>
 * <li>{@link com.jts.fortress.rbac.UserRole}</li>
 * <li>{@link com.jts.fortress.rbac.Role}</li>
 * <li>{@link com.jts.fortress.rbac.AdminRole}</li>
 * <li>{@link com.jts.fortress.rbac.UserAdminRole}</li>
 * </ol>
 * </p>
 * <h4> Constraint Processors </h4>
 * <ol>
 * <li>Time of day:  {@link com.jts.fortress.util.time.ClockTime}</li>
 * <li>Date:         {@link com.jts.fortress.util.time.Date}</li>
 * <li>Days of week: {@link com.jts.fortress.util.time.Day}</li>
 * <li>Timeout:      {@link com.jts.fortress.util.time.Timeout}</li>
 * <li>Lock dates:   {@link com.jts.fortress.util.time.LockDate}</li>
 * <li>DSDs:         {@link com.jts.fortress.rbac.DSD}</li>
 * </ol>
 * </p>
 * <h4> Constraint Error Codes </h4>
 * <ol>
 * <li>{@link com.jts.fortress.GlobalErrIds#ACTV_FAILED_DAY}</li>
 * <li>{@link com.jts.fortress.GlobalErrIds#ACTV_FAILED_DATE}</li>
 * <li>{@link com.jts.fortress.GlobalErrIds#ACTV_FAILED_TIME}</li>
 * <li>{@link com.jts.fortress.GlobalErrIds#ACTV_FAILED_TIMEOUT}</li>
 * <li>{@link com.jts.fortress.GlobalErrIds#ACTV_FAILED_LOCK}</li>
 * <li>{@link com.jts.fortress.GlobalErrIds#ACTV_FAILED_DSD}</li>
 * </ol>
 *

 * @author Shawn McKinney
 * @created February 13, 2010
 */
public interface Validator
{
    /**
     * This method is called during activation of {@link com.jts.fortress.rbac.UserRole} and {@link com.jts.fortress.rbac.UserAdminRole}
     * </p>
     * The following error codes can be returned for validations:
     * <ol>
     * <li>{@link com.jts.fortress.GlobalErrIds#ACTV_FAILED_DAY}</li>
     * <li>{@link com.jts.fortress.GlobalErrIds#ACTV_FAILED_DATE}</li>
     * <li>{@link com.jts.fortress.GlobalErrIds#ACTV_FAILED_TIME}</li>
     * <li>{@link com.jts.fortress.GlobalErrIds#ACTV_FAILED_TIMEOUT}</li>
     * <li>{@link com.jts.fortress.GlobalErrIds#ACTV_FAILED_LOCK}</li>
     * <li>{@link com.jts.fortress.GlobalErrIds#ACTV_FAILED_DSD}</li>
     * </ol>
     *
     * @param session contains the reference to Fortress entities that are targets for constraints.
     * @param constraint contains the temporal attributes.
     * @param time current time of day.
     * @return activation failure code.
     * @throws com.jts.fortress.SecurityException in the event of validation fails or system exception.
     */
    public int validate(Session session, Constraint constraint, Time time) throws com.jts.fortress.SecurityException;
}

