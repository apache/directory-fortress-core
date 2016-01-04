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
 * This class performs time validation for {@link org.apache.directory.fortress.core.model.Constraint}.  This validator will ensure the current time falls between {@link org.apache.directory.fortress.core.model.Constraint#getBeginTime()} and {@link Constraint#getEndTime()}
 * The format requires military time, i.e. 0800 for 8:00 am, 1700 for 5:00 pm.  The constant {@link org.apache.directory.fortress.core.GlobalIds#NONE} may be used to disable checks for a particular entity.
 * for {@link org.apache.directory.fortress.core.model.Constraint} validations that occur in
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
public class ClockTime
    implements Validator
{
    /**
     * This method is called during entity activation, {@link org.apache.directory.fortress.core.util.VUtil#validateConstraints} and ensures the current time is
     * between {@link Constraint#getBeginTime()} and {@link org.apache.directory.fortress.core.model.Constraint#getBeginTime()}.
     *
     * @param session    required for {@link Validator} interface but not used here.
     * @param constraint contains the begin and end times.  Maps listed above.
     * @param time       contains the current time.
     * @param type       required by interface, not used here.
     * @return '0' if validation succeeds else {@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_TIME} if failed.
     */
    @Override
    public int validate( Session session, Constraint constraint, Time time, VUtil.ConstraintType type )
    {
        int rc = GlobalErrIds.ACTV_FAILED_TIME;

        if ( ( constraint.getBeginTime() == null )
            || ( constraint.getBeginTime().compareToIgnoreCase( GlobalIds.NONE ) == 0 ) )
        {
            rc = 0;
        }
        else
        {
            Integer beginTime = Integer.valueOf( constraint.getBeginTime() );
            Integer endTime = Integer.valueOf( constraint.getEndTime() );

            if ( ( beginTime == 0 ) && ( endTime == 0 ) )
            {
                rc = 0;
            }
            else
            {
                if ( beginTime.compareTo( time.currentTime ) <= 0
                    && endTime.compareTo( time.currentTime ) >= 0 )
                {
                    rc = 0;
                }
            }
        }

        return rc;
    }
}
