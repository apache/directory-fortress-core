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

