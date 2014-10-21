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

import org.openldap.fortress.rbac.Session;

/**
 * Interface used by Fortress to provide pluggable validation routines for constraints.
 *
 * <h4> Constraint Targets</h4>
 * <ol>
 * <li>{@link org.openldap.fortress.rbac.User}</li>
 * <li>{@link org.openldap.fortress.rbac.UserRole}</li>
 * <li>{@link org.openldap.fortress.rbac.Role}</li>
 * <li>{@link org.openldap.fortress.rbac.AdminRole}</li>
 * <li>{@link org.openldap.fortress.rbac.UserAdminRole}</li>
 * </ol>
 * </p>
 * <h4> Constraint Processors </h4>
 * <ol>
 * <li>Time of day:  {@link org.openldap.fortress.util.time.ClockTime}</li>
 * <li>Date:         {@link org.openldap.fortress.util.time.Date}</li>
 * <li>Days of week: {@link org.openldap.fortress.util.time.Day}</li>
 * <li>Timeout:      {@link org.openldap.fortress.util.time.Timeout}</li>
 * <li>Lock dates:   {@link org.openldap.fortress.util.time.LockDate}</li>
 * <li>DSDs:         {@link org.openldap.fortress.rbac.DSDChecker}</li>
 * </ol>
 * </p>
 * <h4> Constraint Error Codes </h4>
 * <ol>
 * <li>{@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_DAY}</li>
 * <li>{@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_DATE}</li>
 * <li>{@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_TIME}</li>
 * <li>{@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_TIMEOUT}</li>
 * <li>{@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_LOCK}</li>
 * <li>{@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_DSD}</li>
 * </ol>
 *
 * @author Shawn McKinney
 */
public interface Validator
{
    /**
     * This method is called during activation of {@link org.openldap.fortress.rbac.UserRole} and {@link org.openldap.fortress.rbac.UserAdminRole}
     * </p>
     * The following error codes can be returned for validations:
     * <ol>
     * <li>{@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_DAY}</li>
     * <li>{@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_DATE}</li>
     * <li>{@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_TIME}</li>
     * <li>{@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_TIMEOUT}</li>
     * <li>{@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_LOCK}</li>
     * <li>{@link org.openldap.fortress.GlobalErrIds#ACTV_FAILED_DSD}</li>
     * </ol>
     *
     * @param session contains the reference to Fortress entities that are targets for constraints.
     * @param constraint contains the temporal attributes.
     * @param time current time of day.
     * @return activation failure code.
     * @throws org.openldap.fortress.SecurityException in the event of validation fails or system exception.
     */
    public int validate(Session session, Constraint constraint, Time time) throws org.openldap.fortress.SecurityException;
}

