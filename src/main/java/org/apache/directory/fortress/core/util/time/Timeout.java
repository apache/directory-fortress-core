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
import org.apache.directory.fortress.core.model.Constraint;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.util.VUtil;


/**
 * This class performs timeout validation for {@link Constraint}.  This validator will ensure the elapsed time an entity is active is less than {@link org.apache.directory.fortress.core.model.Constraint#getTimeout()} and {@link Constraint#getEndTime()}
 * The timeout is in minutes and is stored as integer value.  i.e. 30 for 30 minutes.  A value of '0' specifies no timeout for a particular entity.
 * <h4> Constraint Targets include</h4>
 * <ol>
 * <li>{@link org.apache.directory.fortress.core.model.User} maps to 'ftCstr' attribute on 'ftUserAttrs' object class</li>
 * <li>{@link org.apache.directory.fortress.core.model.UserRole} maps to 'ftRC' attribute on 'ftUserAttrs' object class</li>
 * <li>{@link org.apache.directory.fortress.core.model.Role}  maps to 'ftCstr' attribute on 'ftRls' object class</li>
 * <li>{@link org.apache.directory.fortress.core.model.AdminRole}  maps to 'ftCstr' attribute on 'ftRls' object class</li>
 * <li>{@link org.apache.directory.fortress.core.model.UserAdminRole}  maps to 'ftARC' attribute on 'ftRls' object class</li>
 * </ol>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Timeout implements Validator
{
    /**
     * This method is called during entity activation, {@link org.apache.directory.fortress.core.util.VUtil#validateConstraints} and ensures the elapsed time a particular entity has been activated does not exceed specified.
     * value {@link Constraint#getTimeout()}.
     *
     * @param session    required for {@link Validator} interface but not used here.
     * @param constraint contains the elapsed time entity may remain inactive in minutes.  Maps listed above.
     * @param time       contains the current timestamp.
     * @param type       required by interface, not used here.
     * @return '0' if validation succeeds else {@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_TIMEOUT} if failed.
     */
    public int validate( Session session, Constraint constraint, Time time, VUtil.ConstraintType type )
    {
        int rc = GlobalErrIds.ACTV_FAILED_TIMEOUT;
        long timeLimit;
        long lastTime = session.getLastAccess();

        if ( lastTime == 0 )
        {
            rc = 0;
        }
        else
        {
            long elapsedTime = System.currentTimeMillis() - lastTime;
            timeLimit = constraint.getTimeout() * 60000L;

            if ( ( elapsedTime < timeLimit ) || ( constraint.getTimeout() == 0 ) )
            {
                rc = 0;
            }
        }

        return rc;
    }
}
