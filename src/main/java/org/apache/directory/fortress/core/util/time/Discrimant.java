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

/**
 * This class performs dynamic constraint validation on role per FC-235
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Discrimant
    implements Validator
{
    /**
     * This method is called during entity activation, {@link org.apache.directory.fortress.core.util.VUtil#validateConstraints} and ensures role has a
     * matching constraint value.
     *
     * @param session    required for {@link Validator} interface but not used here.
     * @param constraint only the name is used on this validator.
     * @param time       contains the current time stamp which is not needed here.
     * @param type       here it is used as this validator should not be applied to a user.
     * @return '0' if validation succeeds else {@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_DYNAMIC_RUNTIME} if failed.
     */
    @Override
    public int validate(Session session, Constraint constraint, Time time, VUtil.ConstraintType type )
    {
        int rc = 0;

        // Doesn't make sense to apply this constraint on a user:
        if ( type != VUtil.ConstraintType.USER )
        {
            String constraintType = Config.getInstance().getProperty( constraint.getName() );
            // Is there a runtime constraint placed on this role activation?
            if ( StringUtils.isNotEmpty( constraintType ))
            {
                // Get the value for the constraint set by the caller:
                String userProp = session.getUser().getProperty( constraint.getName() );

                // Does the user object have a property that is associated with the runtime constraint set by the caller on this particular role?
                if ( StringUtils.isNotEmpty( userProp ) )
                {
                    // The constraint is passed by caller in a property with keyName located in the constraintType for the role.
                    //String constraintValue = user.getProperty( constraintType );
                    String constraintValue = session.getUser().getProperty( constraintType );

                    // Is the activated role's constraint valid per the constraint value passed in by the caller?  (Is the role allowed here)
                    if ( !userProp.equalsIgnoreCase( constraintValue ) )
                    {
                        rc = GlobalErrIds.ACTV_DYNAMIC_RUNTIME;
                    }
                }
                else
                {
                    // No, there is no constraint value on a role that requires one.
                    rc = GlobalErrIds.ACTV_DYNAMIC_RUNTIME;
                }
            }
        }
        return rc;
    }
}

