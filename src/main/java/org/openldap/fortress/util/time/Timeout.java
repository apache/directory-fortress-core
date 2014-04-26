/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */

package org.openldap.fortress.util.time;

import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.rbac.Session;

/**
 * This class performs timeout validation for {@link org.openldap.fortress.util.time.Constraint}.  This validator will ensure the elapsed time an entity is active is less than {@link org.openldap.fortress.util.time.Constraint#getTimeout()} and {@link org.openldap.fortress.util.time.Constraint#getEndTime()}
 * The timeout is in minutes and is stored as integer value.  i.e. 30 for 30 minutes.  A value of '0' specifies no timeout for a particular entity.
 * <h4> Constraint Targets include</h4>
 * <ol>
 * <li>{@link org.openldap.fortress.rbac.User} maps to 'ftCstr' attribute on 'ftUserAttrs' object class</li>
 * <li>{@link org.openldap.fortress.rbac.UserRole} maps to 'ftRC' attribute on 'ftUserAttrs' object class</li>
 * <li>{@link org.openldap.fortress.rbac.Role}  maps to 'ftCstr' attribute on 'ftRls' object class</li>
 * <li>{@link org.openldap.fortress.rbac.AdminRole}  maps to 'ftCstr' attribute on 'ftRls' object class</li>
 * <li>{@link org.openldap.fortress.rbac.UserAdminRole}  maps to 'ftARC' attribute on 'ftRls' object class</li>
 * </ol>
 * </p>
 *
 * @author Shawn McKinney
 */
public class Timeout
    implements Validator
{
    /**
     * This method is called during entity activation, {@link org.openldap.fortress.util.time.CUtil#validateConstraints} and ensures the elapsed time a particular entity has been activated does not exceed specified.
     * value {@link org.openldap.fortress.util.time.Constraint#getTimeout()}.
     *
     * @param session    required for {@link Validator} interface but not used here.
     * @param constraint contains the elapsed time entity may remain inactive in minutes.  Maps listed above.
     * @param time       contains the current timestamp.
     * @return '0' if validation succeeds else {@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_TIMEOUT} if failed.
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

