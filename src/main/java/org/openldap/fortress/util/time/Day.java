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
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.rbac.Session;

/**
 * This class performs lock day of week validation for {@link org.openldap.fortress.util.time.Constraint}.  This validator will ensure the current day is allowed for {@link org.openldap.fortress.util.time.Constraint#getDayMask()}.
 * The data format requires 1234567 for Sun, Mon, Tue, Wed, Thur, Fri, Sat, Sun respectively.  i.e. 23456 will allow entity to activated Monday - Friday.  The constant {@link org.openldap.fortress.GlobalIds#ALL} may be used to disable checks for a particular entity.
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
public class Day
    implements Validator
{
    /**
     * This method is called during entity activation, {@link org.openldap.fortress.util.time.CUtil#validateConstraints} and ensures the current day falls
     * within {@link org.openldap.fortress.util.time.Constraint#getDayMask()} range.
     *
     * @param session    required for {@link Validator} interface but not used here.
     * @param constraint contains the days of week entity may be activated.  Data mappings listed above.
     * @param time       contains the current time stamp.
     * @return '0' if validation succeeds else {@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_DAY} if failed.
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

