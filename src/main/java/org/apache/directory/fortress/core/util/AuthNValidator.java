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
package org.apache.directory.fortress.core.util;


import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.model.Constraint;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.util.time.Time;
import org.apache.directory.fortress.core.util.time.Validator;

/**
 * This class performs authentication validation.  This validator will ensure the current user has been authenticated
 * before activating the role into the session.
 * <p>
 * <ol>
 * <li> Create a new class, that extends this one.
 * <pre>
 * package org.mycompany.myutil;
 * import org.apache.directory.fortress.core.model.Constraint;
 * import org.apache.directory.fortress.core.model.Session;
 * import org.apache.directory.fortress.core.util.time.Time;
 *
 *  public class AuthNUserValidator extends AuthNValidator
 * </pre>
 * <li> Implement the abstract method validate.  Must call the super.validate.
 *  <pre>
 *
 * public int validate(Session session, Constraint constraint, Time time, VUtil.ConstraintType type )
 * {
 *   super.setRoleName( "AuthNUsers" );
 *   super.setAuthenticated( true );
 *   return super.validate( session, constraint, time, type );
 * }
 *  </pre>
 * <li> Place the name of the role to verify.
 * <pre>
 * // This can be any valid role name here:
 * super.setRoleName( "AuthNUsers" );
 * </pre>
 * <li> Set the preferred authentication status.
 * <pre>
 * // This can be true or false:
 * super.setAuthenticated( true );
 * </pre>
 * <li> Add a new property to allowed fortress validators.  Where N is an integer and in order of the other enabled validators:
 * <pre>
 * temporal.validator.0:org.apache.directory.fortress.core.util.time.Date
 * temporal.validator.1:org.apache.directory.fortress.core.util.time.LockDate
 * temporal.validator.2:org.apache.directory.fortress.core.util.time.Timeout
 * temporal.validator.3:org.apache.directory.fortress.core.util.time.ClockTime
 * temporal.validator.4:org.apache.directory.fortress.core.util.time.Day
 * temporal.validator.5:org.mycompany.myutil.AuthNUserValidator
 * </pre>
 * <li> Add the compiled Class to runtime classpath.
 * <li> Add the new role.
 * <li> Assign to users as needed.
 * </ol>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public abstract class AuthNValidator implements Validator
{
    private String roleName;
    private boolean isAuthenticated;

    /**
     * This method is called during user-role activation, {@link org.apache.directory.fortress.core.util
     * .VUtil#validateConstraints} and ensures the current user has been
     * proper authentication status before activating the specified role into the session.
     * <p>
     * This validation routine will automatically pass if session.isAuthenticated matches the isAuthenticated member
     * variable AND the role matches the subclasses name AND type == ROLE.
     *
     * @param session    contains the isAuthenticated flag.  If 'true', user must be authenticated before role may be
     *                   authenticated.  If 'false' user must not be authenticated for role to activate.
     * @param constraint contains the role name.
     * @param time       contains the current time stamp.
     * @param type       only constraints of type role use this.
     * @return '0' if validation succeeds else {@link org.apache.directory.fortress.core
     * .GlobalErrIds#ACTV_FAILED_AUTHN} if failed.
     */

    public int validate(Session session, Constraint constraint, Time time, VUtil.ConstraintType type)
    {
        int rc = 0;
        if ( type == VUtil.ConstraintType.ROLE && constraint.getName().equalsIgnoreCase( roleName ) && session
            .isAuthenticated() == isAuthenticated )
        {
            rc = GlobalErrIds.ACTV_FAILED_AUTHN;
        }
        return rc;
    }

    protected String getRoleName()
    {
        return roleName;
    }

    protected void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    protected boolean isAuthenticated()
    {
        return isAuthenticated;
    }

    protected void setAuthenticated(boolean isAuthenticated)
    {
        this.isAuthenticated = isAuthenticated;
    }
}