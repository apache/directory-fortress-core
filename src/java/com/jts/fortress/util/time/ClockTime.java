/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.util.time;

import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.rbac.Session;

/**
 * This class performs time validation for {@link com.jts.fortress.util.time.Constraint}.  This validator will ensure the current time falls between {@link com.jts.fortress.util.time.Constraint#getBeginTime()} and {@link com.jts.fortress.util.time.Constraint#getEndTime()}
 * The format requires military time, i.e. 0800 for 8:00 am, 1700 for 5:00 pm.  The constant {@link com.jts.fortress.constants.GlobalIds#NONE} may be used to disable checks for a particular entity.
 * for {@link com.jts.fortress.util.time.Constraint} validations that occur in
 * <h4> Constraint Targets include</h4>
 * <ol>
 * <li>{@link com.jts.fortress.rbac.User} maps to 'ftCstr' attribute on 'ftUserAttrs' object class</li>
 * <li>{@link com.jts.fortress.rbac.UserRole} maps to 'ftRC' attribute on 'ftUserAttrs' object class</li>
 * <li>{@link com.jts.fortress.rbac.Role}  maps to 'ftCstr' attribute on 'ftRls' object class</li>
 * <li>{@link com.jts.fortress.arbac.AdminRole}  maps to 'ftCstr' attribute on 'ftRls' object class</li>
 * <li>{@link com.jts.fortress.arbac.UserAdminRole}  maps to 'ftARC' attribute on 'ftRls' object class</li>
 * </ol>
 * </p>
 *
 * @author smckinn
 * @created February 13, 2010
 */
public class ClockTime
    implements Validator
{
    /**
     * This method is called during entity activation, {@link com.jts.fortress.util.time.CUtil#validateConstraints} and ensures the current time is
     * between {@link com.jts.fortress.util.time.Constraint#getBeginTime()} and {@link com.jts.fortress.util.time.Constraint#getBeginTime()}.
     *
     * @param session    required for {@link Validator} interface but not used here.
     * @param constraint contains the begin and end times.  Maps listed above.
     * @param time       contains the current time.
     * @return '0' if validation succeeds else {@link com.jts.fortress.constants.GlobalErrIds#ACTV_FAILED_TIME} if failed.
     */
    public int validate(Session session, Constraint constraint, Time time)
    {
        int rc = com.jts.fortress.constants.GlobalErrIds.ACTV_FAILED_TIME;
        if (constraint.getBeginTime() == null || constraint.getBeginTime().compareToIgnoreCase(GlobalIds.NONE) == 0)
        {
            rc = 0;
        }
        else
        {
            Integer beginTime = new Integer(constraint.getBeginTime());
            Integer endTime = new Integer(constraint.getEndTime());
            if (beginTime == 0 && endTime == 0)
            {
                rc = 0;
            }
            else
            {
                if (beginTime.compareTo(time.currentTime) <= 0
                    && endTime.compareTo(time.currentTime) >= 0)
                {
                    rc = 0;
                }
            }
        }
        return rc;
    }
}