/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.openldap.fortress.util.time;

import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.rbac.Session;

/**
 * This class performs date validation for {@link org.openldap.fortress.util.time.Constraint}.  This validator will ensure the current date falls between {@link org.openldap.fortress.util.time.Constraint#getBeginDate()} and {@link org.openldap.fortress.util.time.Constraint#getEndDate()}
 * The format requires YYYYMMDD, i.e. 20110101 for January 1, 2011.  The constant {@link org.openldap.fortress.GlobalIds#NONE} may be used to disable checks for a particular entity.
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
public class Date
    implements Validator
{
    /**
     * This method is called during entity activation, {@link org.openldap.fortress.util.time.CUtil#validateConstraints} and ensures the current date is
     * between {@link org.openldap.fortress.util.time.Constraint#getBeginDate()} and {@link org.openldap.fortress.util.time.Constraint#getEndDate()}.
     *
     * This validation routine allows for either beginDate or endDate to be null or set to "none" which will disable the corresponding check.
     * For example if beginDate is null or equal to 'none', the validator will not skip the date eval for begin date.
     * If either begin or end dates are set the validator will compare to the current date to ensure within range.
     * If set, the expected date format is YYYYMMDD.  For example, '20110101' equals Jan 1, 2011.
     *
     * @param session    required for {@link Validator} interface but not used here.
     * @param constraint contains the begin and end dates.  Maps listed above.
     * @param time       contains the current time stamp.
     * @return '0' if validation succeeds else {@link GlobalErrIds#ACTV_FAILED_DATE} if failed.
     */
    @Override
    public int validate(Session session, Constraint constraint, Time time)
    {
        int rc = GlobalErrIds.ACTV_FAILED_DATE;
        boolean noBegin = false;
        boolean noEnd = false;
        if (constraint.getBeginDate() == null || constraint.getBeginDate().compareToIgnoreCase(GlobalIds.NONE) == 0)
        {
            noBegin = true;
        }
        if (constraint.getEndDate() == null || constraint.getEndDate().compareToIgnoreCase(GlobalIds.NONE) == 0)
        {
            noEnd = true;
        }
        if(noBegin && noEnd)
        {
            rc = 0;
        }
        else if(noBegin)
        {
            if (constraint.getEndDate().compareTo(time.date) >= 0)
            {
                rc = 0;
            }
        }
        else if(noEnd)
        {
            if (constraint.getBeginDate().compareTo(time.date) <= 0)
            {
                rc = 0;
            }
        }
        else if(!noEnd)
        {
            if (constraint.getBeginDate().compareTo(time.date) <= 0
                && constraint.getEndDate().compareTo(time.date) >= 0)
            {
                rc = 0;
            }
        }
        return rc;
    }
}

