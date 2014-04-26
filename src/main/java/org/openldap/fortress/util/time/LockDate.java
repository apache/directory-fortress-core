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
 * This class performs lock date validation for {@link org.openldap.fortress.util.time.Constraint}.  This validator will ensure the current date falls outside {@link org.openldap.fortress.util.time.Constraint#getBeginLockDate()} and {@link org.openldap.fortress.util.time.Constraint#getEndLockDate()} range.
 * The idea is an entity can be barred from activation for a particular blackout period, i.e. vacation, leave of absence, etc.
 * The data format requires YYYYMMDD, i.e. 20110101 for January 1, 2011.  The constant {@link org.openldap.fortress.GlobalIds#NONE} may be used to disable checks for a particular entity.
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
public class LockDate
    implements Validator
{
    /**
     * This method is called during entity activation, {@link org.openldap.fortress.util.time.CUtil#validateConstraints} and ensures the current date falls
     * outside the {@link org.openldap.fortress.util.time.Constraint#getBeginLockDate()} and {@link org.openldap.fortress.util.time.Constraint#getEndLockDate()} range.
     *
     * This validation routine will automatically pass if either beginLockDate or endLockDate equals null or "none".
     * If both beginLockDate and endLockDate are set the validator will ensure current date does not fall between the date range.
     * The format expected if date is set is YYYYMMDD.  For example, '20110101' equals Jan 1, 2011.
     *
     * @param session    required for {@link Validator} interface but not used here.
     * @param constraint contains the begin and end lock dates.  Maps listed above.
     * @param time       contains the current time stamp.
     * @return '0' if validation succeeds else {@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_LOCK} if failed.
     */
    @Override
    public int validate(Session session, Constraint constraint, Time time)
    {
        int rc = GlobalErrIds.ACTV_FAILED_LOCK;

        // if either beginLockDate or endLockDate equal to null or 'none', validation will automatically pass.
        if ( constraint.getBeginLockDate() == null || constraint.getBeginLockDate().compareToIgnoreCase(GlobalIds.NONE) == 0
          || constraint.getEndLockDate() == null || constraint.getEndLockDate().compareToIgnoreCase(GlobalIds.NONE) == 0)
        {
            rc = 0;
        }
        else
        {
            if (!(constraint.getBeginLockDate().compareTo(time.date) <= 0
                && constraint.getEndLockDate().compareTo(time.date) >= 0))

                //if (!(constraint.getBeginLockDate().compareTo(time.date) <= 0
                //    && constraint.getEndLockDate().compareTo(time.date) >= 0))
                //{
                //    rc = 0;
                //}

            {
                rc = 0;
            }
        }
        return rc;
    }
}

