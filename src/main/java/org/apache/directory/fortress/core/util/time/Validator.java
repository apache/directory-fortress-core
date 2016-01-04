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


import org.apache.directory.fortress.core.model.Constraint;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.util.VUtil;


/**
 * Interface used by Fortress to provide pluggable validation routines for constraints.
 *
 * <h4> Constraint Targets</h4>
 * <ol>
 * <li>{@link org.apache.directory.fortress.core.model.User}</li>
 * <li>{@link org.apache.directory.fortress.core.model.UserRole}</li>
 * <li>{@link org.apache.directory.fortress.core.model.Role}</li>
 * <li>{@link org.apache.directory.fortress.core.model.AdminRole}</li>
 * <li>{@link org.apache.directory.fortress.core.model.UserAdminRole}</li>
 * </ol>
 * <p>
 * <h4> Constraint Processors </h4>
 * <ol>
 * <li>Time of day:  {@link ClockTime}</li>
 * <li>Date:         {@link Date}</li>
 * <li>Days of week: {@link Day}</li>
 * <li>Timeout:      {@link Timeout}</li>
 * <li>Lock dates:   {@link LockDate}</li>
 * <li>DSDs:         {@link org.apache.directory.fortress.core.impl.DSDChecker}</li>
 * </ol>
 * <p>
 * <h4> Constraint Error Codes </h4>
 * <ol>
 * <li>{@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_DAY}</li>
 * <li>{@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_DATE}</li>
 * <li>{@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_TIME}</li>
 * <li>{@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_TIMEOUT}</li>
 * <li>{@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_LOCK}</li>
 * <li>{@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_DSD}</li>
 * </ol>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface Validator
{
    /**
     * This method is called during activation of {@link org.apache.directory.fortress.core.model.UserRole} and {@link org.apache.directory.fortress.core.model.UserAdminRole}
     * <p>
     * The following error codes can be returned for validations:
     * <ol>
     * <li>{@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_DAY}</li>
     * <li>{@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_DATE}</li>
     * <li>{@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_TIME}</li>
     * <li>{@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_TIMEOUT}</li>
     * <li>{@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_LOCK}</li>
     * <li>{@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_DSD}</li>
     * </ol>
     *
     * @param session contains the reference to Fortress entities that are targets for constraints.
     * @param constraint contains the temporal attributes.
     * @param time current time of day.
     * @param type contains type of either role or user constraint.
     * @return activation failure code.
     * @throws org.apache.directory.fortress.core.SecurityException in the event of validation fails or system exception.
     */
    int validate( Session session, Constraint constraint, Time time, VUtil.ConstraintType type )
        throws org.apache.directory.fortress.core.SecurityException;
}
