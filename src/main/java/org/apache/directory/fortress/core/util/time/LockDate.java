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
 * This class performs lock date validation for {@link org.apache.directory.fortress.core.model.Constraint}.  This validator will ensure the current date falls outside {@link org.apache.directory.fortress.core.model.Constraint#getBeginLockDate()} and {@link Constraint#getEndLockDate()} range.
 * The idea is an entity can be barred from activation for a particular blackout period, i.e. vacation, leave of absence, etc.
 * The data format requires YYYYMMDD, i.e. 20110101 for January 1, 2011.  The constant {@link org.apache.directory.fortress.core.GlobalIds#NONE} may be used to disable checks for a particular entity.
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
public class LockDate
    implements Validator
{
    /**
     * This method is called during entity activation, {@link org.apache.directory.fortress.core.util.VUtil#validateConstraints} and ensures the current date falls
     * outside the {@link org.apache.directory.fortress.core.model.Constraint#getBeginLockDate()} and {@link org.apache.directory.fortress.core.model.Constraint#getEndLockDate()} range.
     *
     * This validation routine will automatically pass if either beginLockDate or endLockDate equals null or "none".
     * If both beginLockDate and endLockDate are set the validator will ensure current date does not fall between the date range.
     * The format expected if date is set is YYYYMMDD.  For example, '20110101' equals Jan 1, 2011.
     *
     * @param session    required for {@link Validator} interface but not used here.
     * @param constraint contains the begin and end lock dates.  Maps listed above.
     * @param time       contains the current time stamp.
     * @return '0' if validation succeeds else {@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_LOCK} if failed.
     */
    @Override
    public int validate(Session session, Constraint constraint, Time time, VUtil.ConstraintType type )
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
            // Does current date fall inside lock date?
            if (!(constraint.getBeginLockDate().compareTo(time.date) <= 0
                && constraint.getEndLockDate().compareTo(time.date) >= 0))
            {
                rc = 0;
            }
        }
        return rc;
    }
}

