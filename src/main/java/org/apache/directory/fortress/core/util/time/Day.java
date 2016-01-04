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
package org.apache.directory.fortress.core.util.time;

import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.model.Constraint;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * This class performs lock day of week validation for {@link Constraint}.  This validator will ensure the current day is allowed for {@link Constraint#getDayMask()}.
 * The data format requires 1234567 for Sun, Mon, Tue, Wed, Thur, Fri, Sat, Sun respectively.  i.e. 23456 will allow entity to activated Monday - Friday.  The constant {@link org.apache.directory.fortress.core.GlobalIds#ALL} may be used to disable checks for a particular entity.
 * <h4> Constraint Targets include</h4>
 * <ol>
 * <li>{@link org.apache.directory.fortress.core.model.User} maps to 'ftCstr' attribute on 'ftUserAttrs' object class</li>
 * <li>{@link org.apache.directory.fortress.core.model.UserRole} maps to 'ftRC' attribute on 'ftUserAttrs' object class</li>
 * <li>{@link org.apache.directory.fortress.core.model.Role}  maps to 'ftCstr' attribute on 'ftRls' object class</li>
 * <li>{@link org.apache.directory.fortress.core.model.AdminRole}  maps to 'ftCstr' attribute on 'ftRls' object class</li>
 * <li>{@link org.apache.directory.fortress.core.model.UserAdminRole}  maps to 'ftARC' attribute on 'ftRls' object class</li>
 * </ol>
 * <p>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Day
    implements Validator
{
    /**
     * This method is called during entity activation, {@link org.apache.directory.fortress.core.util.VUtil#validateConstraints} and ensures the current day falls
     * within {@link org.apache.directory.fortress.core.model.Constraint#getDayMask()} range.
     *
     * @param session    required for {@link Validator} interface but not used here.
     * @param constraint contains the days of week entity may be activated.  Data mappings listed above.
     * @param time       contains the current time stamp.
     * @param type       required by interface, not used here.
     * @return '0' if validation succeeds else {@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_DAY} if failed.
     */
    @Override
    public int validate(Session session, Constraint constraint, Time time, VUtil.ConstraintType type )
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

