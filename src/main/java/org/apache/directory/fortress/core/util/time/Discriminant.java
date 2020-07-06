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

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.model.Constraint;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.VUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Do not use, is deprecated for {@link org.apache.directory.fortress.core.util.time.UserRoleConstraint}.
 * This class performs dynamic constraint validation on role per FC-235.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@Deprecated
public class Discriminant
    implements Validator
{
    /**
     * This method is called during entity activation, {@link org.apache.directory.fortress.core.util.VUtil#validateConstraints} and ensures role has a
     * matching constraint value.
     *
     * @param session Contains the name and value of discriminator, passed by the caller, along with its corresponding values, stored on the user's properties.
     * @param role only the name is used on this argument.
     * @param time contains the current time stamp, and required by the interface, but not needed here.
     * @param type used on this validator to prevent it from ever being applied to a user's constraint.
     * @return '0' if validation succeeds else {@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_DISCRIMINANT} if failed.
     */
    @Override
    public int validate(Session session, Constraint role, Time time, VUtil.ConstraintType type )
    {
        int rc = 0;

        // Doesn't make sense to apply this constraint on a user:
        if ( type != VUtil.ConstraintType.USER )
        {
            // This constraint type requires a global config parameter keyed by the role name:
            String constraintType = Config.getInstance().getProperty( role.getName() );
            // Is there a runtime constraint placed on this role activation?
            if ( StringUtils.isNotEmpty( constraintType ))
            {
                // Get the constraint value for this user set as property on the user entity keyed with the role's name:
                String userProp = session.getUser().getProperty( role.getName() );
                // Does the user have one set?
                if ( StringUtils.isNotEmpty( userProp ) )
                {
                    Set<String> values = getValues( userProp );
                    // This value must be placed here by the caller:
                    String constraintValue = session.getUser().getProperty( constraintType );
                    if( StringUtils.isEmpty( constraintValue ) || !values.contains( constraintValue ) )
                    {
                        rc = GlobalErrIds.ACTV_FAILED_DISCRIMINANT;
                    }
                }
                else
                {
                    // This user does not have a corresponding property applied to a role that has a runtime constraint set.
                    rc = GlobalErrIds.ACTV_FAILED_DISCRIMINANT;
                }
            }
        }
        return rc;
    }

    public Set<String> getValues( String members )
    {
        Set<String> values = new HashSet<>(  );
        if ( members != null )
        {
            StringTokenizer tkn = new StringTokenizer( members, "," );
            if ( tkn.countTokens() > 0 )
            {
                while ( tkn.hasMoreTokens() )
                {
                    values.add( tkn.nextToken() );
                }
            }
        }
        return values;
    }

}