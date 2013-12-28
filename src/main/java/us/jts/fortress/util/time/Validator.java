/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.util.time;

import us.jts.fortress.rbac.Session;

/**
 * Interface used by Fortress to provide pluggable validation routines for constraints.
 *
 * <h4> Constraint Targets</h4>
 * <ol>
 * <li>{@link us.jts.fortress.rbac.User}</li>
 * <li>{@link us.jts.fortress.rbac.UserRole}</li>
 * <li>{@link us.jts.fortress.rbac.Role}</li>
 * <li>{@link us.jts.fortress.rbac.AdminRole}</li>
 * <li>{@link us.jts.fortress.rbac.UserAdminRole}</li>
 * </ol>
 * </p>
 * <h4> Constraint Processors </h4>
 * <ol>
 * <li>Time of day:  {@link us.jts.fortress.util.time.ClockTime}</li>
 * <li>Date:         {@link us.jts.fortress.util.time.Date}</li>
 * <li>Days of week: {@link us.jts.fortress.util.time.Day}</li>
 * <li>Timeout:      {@link us.jts.fortress.util.time.Timeout}</li>
 * <li>Lock dates:   {@link us.jts.fortress.util.time.LockDate}</li>
 * <li>DSDs:         {@link us.jts.fortress.rbac.DSDChecker}</li>
 * </ol>
 * </p>
 * <h4> Constraint Error Codes </h4>
 * <ol>
 * <li>{@link us.jts.fortress.GlobalErrIds#ACTV_FAILED_DAY}</li>
 * <li>{@link us.jts.fortress.GlobalErrIds#ACTV_FAILED_DATE}</li>
 * <li>{@link us.jts.fortress.GlobalErrIds#ACTV_FAILED_TIME}</li>
 * <li>{@link us.jts.fortress.GlobalErrIds#ACTV_FAILED_TIMEOUT}</li>
 * <li>{@link us.jts.fortress.GlobalErrIds#ACTV_FAILED_LOCK}</li>
 * <li>{@link us.jts.fortress.GlobalErrIds#ACTV_FAILED_DSD}</li>
 * </ol>
 *
 * @author Shawn McKinney
 */
public interface Validator
{
    /**
     * This method is called during activation of {@link us.jts.fortress.rbac.UserRole} and {@link us.jts.fortress.rbac.UserAdminRole}
     * </p>
     * The following error codes can be returned for validations:
     * <ol>
     * <li>{@link us.jts.fortress.GlobalErrIds#ACTV_FAILED_DAY}</li>
     * <li>{@link us.jts.fortress.GlobalErrIds#ACTV_FAILED_DATE}</li>
     * <li>{@link us.jts.fortress.GlobalErrIds#ACTV_FAILED_TIME}</li>
     * <li>{@link us.jts.fortress.GlobalErrIds#ACTV_FAILED_TIMEOUT}</li>
     * <li>{@link us.jts.fortress.GlobalErrIds#ACTV_FAILED_LOCK}</li>
     * <li>{@link us.jts.fortress.GlobalErrIds#ACTV_FAILED_DSD}</li>
     * </ol>
     *
     * @param session contains the reference to Fortress entities that are targets for constraints.
     * @param constraint contains the temporal attributes.
     * @param time current time of day.
     * @return activation failure code.
     * @throws us.jts.fortress.SecurityException in the event of validation fails or system exception.
     */
    public int validate(Session session, Constraint constraint, Time time) throws us.jts.fortress.SecurityException;
}

