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
import org.apache.directory.fortress.core.model.RoleConstraint;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.VUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class performs dynamic constraint validation on role activation, per FC-235
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class UserRoleConstraint
    implements Validator
{
    private static final String CLS_NM = UserRoleConstraint.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    /**
     * This method is called during entity activation, {@link org.apache.directory.fortress.core.util.VUtil#validateConstraints} and ensures role has a
     * matching constraint value.
     *
     * @param session Contains the name and value of discriminator, passed by the caller.  e.g. locale=north
     * @param role contains the attribute constraint allowed for a given user's role.  e.g. role=tellers, locale=north.
     * @param time contains the current time stamp, required by the interface, not used here.
     * @param type used on this validator to prevent it from ever being applied to a user's constraint.
     * @return '0' if validation succeeds else {@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_ABAC} if failed.
     */
    @Override
    public int validate(Session session, Constraint role, Time time, VUtil.ConstraintType type )
    {
        int rc = 0;
        // Doesn't make sense to apply this constraint on a user:
        if ( type != VUtil.ConstraintType.USER )
        {
            // This constraint type requires a global config parameter keyed by RC$tenant$role:constraint:
            String constraintKey = Config.getInstance().getConstraintKey( role.getName(), session.getContextId() );
            String constraintType = Config.getInstance().getProperty( constraintKey );

            // Is there a runtime constraint placed on this role activation?
            if ( StringUtils.isNotEmpty( constraintType ))
            {
                String constraintValue = session.getUser().getProperty( constraintType );
                if( StringUtils.isEmpty( constraintValue ) || role.getConstraints().isEmpty() )
                {
                    // This user does not have a corresponding property applied to a role that has a runtime constraint set -OR- Have no applicable role constraint.
                    rc = GlobalErrIds.ACTV_FAILED_ABAC_NO_KEY_FOUND;
                    LOG.warn( "User: {}, property: {} not found matching role: {}, constraint: ", session.getUserId(), constraintType, role.getName() );
                }
                else
                {
                    boolean found = false;
                    for ( RoleConstraint constraint : role.getConstraints() )
                    {
                        if( constraint.getType() == RoleConstraint.RCType.USER && constraint.getKey().equalsIgnoreCase( constraintType ) && constraint.getValue().equalsIgnoreCase( constraintValue ) )
                        {
                            found = true;
                            break;
                        }
                    }
                    if( !found)
                    {
                        rc = GlobalErrIds.ACTV_FAILED_ABAC;
                    }
                }
            }
        }
        return rc;
    }
}