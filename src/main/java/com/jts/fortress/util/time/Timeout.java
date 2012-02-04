/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.util.time;

import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.rbac.Session;

/**
 * This class performs timeout validation for {@link com.jts.fortress.util.time.Constraint}.  This validator will ensure the elapsed time an entity is active is less than {@link com.jts.fortress.util.time.Constraint#getTimeout()} and {@link com.jts.fortress.util.time.Constraint#getEndTime()}
 * The timeout is in minutes and is stored as integer value.  i.e. 30 for 30 minutes.  A value of '0' specifies no timeout for a particular entity.
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
public class Timeout
    implements Validator
{
    /**
     * This method is called during entity activation, {@link com.jts.fortress.util.time.CUtil#validateConstraints} and ensures the elapsed time a particular entity has been activated does not exceed specified.
     * value {@link com.jts.fortress.util.time.Constraint#getTimeout()}.
     *
     * @param session    required for {@link Validator} interface but not used here.
     * @param constraint contains the elapsed time entity may remain inactive in minutes.  Maps listed above.
     * @param time       contains the current timestamp.
     * @return '0' if validation succeeds else {@link com.jts.fortress.constants.GlobalErrIds#ACTV_FAILED_TIMEOUT} if failed.
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

