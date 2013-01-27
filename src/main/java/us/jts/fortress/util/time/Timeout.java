/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.util.time;

import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.rbac.Session;

/**
 * This class performs timeout validation for {@link us.jts.fortress.util.time.Constraint}.  This validator will ensure the elapsed time an entity is active is less than {@link us.jts.fortress.util.time.Constraint#getTimeout()} and {@link us.jts.fortress.util.time.Constraint#getEndTime()}
 * The timeout is in minutes and is stored as integer value.  i.e. 30 for 30 minutes.  A value of '0' specifies no timeout for a particular entity.
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
public class Timeout
    implements Validator
{
    /**
     * This method is called during entity activation, {@link us.jts.fortress.util.time.CUtil#validateConstraints} and ensures the elapsed time a particular entity has been activated does not exceed specified.
     * value {@link us.jts.fortress.util.time.Constraint#getTimeout()}.
     *
     * @param session    required for {@link Validator} interface but not used here.
     * @param constraint contains the elapsed time entity may remain inactive in minutes.  Maps listed above.
     * @param time       contains the current timestamp.
     * @return '0' if validation succeeds else {@link us.jts.fortress.GlobalErrIds#ACTV_FAILED_TIMEOUT} if failed.
     */
    public int validate(Session session, Constraint constraint, Time time)
    {
        int rc = GlobalErrIds.ACTV_FAILED_TIMEOUT;
        long timeLimit;
        long lastTime = session.getLastAccess();
        if (lastTime == 0)
        {
            rc = 0;
        }
        else
        {
            long elapsedTime = System.currentTimeMillis() - lastTime;
            timeLimit = constraint.getTimeout() * 60000;
            if (elapsedTime < timeLimit || constraint.getTimeout() == 0)
            {
                rc = 0;
            }
        }
        return rc;
    }
}

