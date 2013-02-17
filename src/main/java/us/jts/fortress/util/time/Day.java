/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.util.time;

import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.GlobalIds;
import us.jts.fortress.rbac.Session;
import org.apache.log4j.Logger;

/**
 * This class performs lock day of week validation for {@link us.jts.fortress.util.time.Constraint}.  This validator will ensure the current day is allowed for {@link us.jts.fortress.util.time.Constraint#getDayMask()}.
 * The data format requires 1234567 for Sun, Mon, Tue, Wed, Thur, Fri, Sat, Sun respectively.  i.e. 23456 will allow entity to activated Monday - Friday.  The constant {@link us.jts.fortress.GlobalIds#ALL} may be used to disable checks for a particular entity.
 * <h4> Constraint Targets include</h4>
 * <ol>
 * <li>{@link us.jts.fortress.rbac.User} maps to 'ftCstr' attribute on 'ftUserAttrs' object class</li>
 * <li>{@link us.jts.fortress.rbac.UserRole} maps to 'ftRC' attribute on 'ftUserAttrs' object class</li>
 * <li>{@link us.jts.fortress.rbac.Role}  maps to 'ftCstr' attribute on 'ftRls' object class</li>
 * <li>{@link us.jts.fortress.rbac.AdminRole}  maps to 'ftCstr' attribute on 'ftRls' object class</li>
 * <li>{@link us.jts.fortress.rbac.UserAdminRole}  maps to 'ftARC' attribute on 'ftRls' object class</li>
 * </ol>
 * </p>
 *
 * @author Shawn McKinney
 */
public class Day
    implements Validator
{
    private static Logger log = Logger.getLogger(Day.class.getName());

    /**
     * This method is called during entity activation, {@link us.jts.fortress.util.time.CUtil#validateConstraints} and ensures the current day falls
     * within {@link us.jts.fortress.util.time.Constraint#getDayMask()} range.
     *
     * @param session    required for {@link Validator} interface but not used here.
     * @param constraint contains the days of week entity may be activated.  Data mappings listed above.
     * @param time       contains the current time stamp.
     * @return '0' if validation succeeds else {@link us.jts.fortress.GlobalErrIds#ACTV_FAILED_DAY} if failed.
     */
    @Override
    public int validate(Session session, Constraint constraint, Time time)
    {
        int rc = GlobalErrIds.ACTV_FAILED_DAY;
        if (constraint.getDayMask() == null || constraint.getDayMask().compareToIgnoreCase(GlobalIds.ALL) == 0)
        {
            rc = 0;
        }
        else
        {
            if (constraint.getDayMask().contains(time.day))
            {
                rc = 0;
            }
        }
        return rc;
    }
}

