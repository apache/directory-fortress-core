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
package org.apache.directory.fortress.core.impl;


import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.model.Constraint;
import org.apache.directory.fortress.core.model.ObjectFactory;
import org.apache.directory.fortress.core.model.SDSet;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.model.Warning;
import org.apache.directory.fortress.core.util.VUtil;
import org.apache.directory.fortress.core.util.time.Time;
import org.apache.directory.fortress.core.util.time.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class performs Dynamic Separation of Duty checking on a collection of roles targeted for activation within a 
 * particular user's session.  This method is called from 
 * {@link org.apache.directory.fortress.core.util.VUtil#validateConstraints} during createSession sequence for users.  If 
 * DSD constraint violation is detected for a particular role method will remove the role from collection of activation 
 * candidates and log a warning.  This proc will also consider hierarchical relations between roles (RBAC spec calls these 
 * authorized roles).
 * This validator will ensure the role being targeted for activation does not violate RBAC dynamic separation of duty 
 * constraints.
 * <h3></h3>
 * <h4> Constraint Targets include</h4>
 * <ol>
 *   <li>{@link org.apache.directory.fortress.core.model.User} maps to 'ftCstr' attribute on 'ftUserAttrs' object class</li>
 *   <li>{@link org.apache.directory.fortress.core.model.UserRole} maps to 'ftRC' attribute on 'ftUserAttrs' object class</li>
 *   <li>{@link org.apache.directory.fortress.core.model.Role}  maps to 'ftCstr' attribute on 'ftRls' object class</li>
 *   <li>{@link org.apache.directory.fortress.core.model.AdminRole}  maps to 'ftCstr' attribute on 'ftRls' object class</li>
 *   <li>{@link org.apache.directory.fortress.core.model.UserAdminRole}  maps to 'ftARC' attribute on 'ftRls' object class</li>
 * </ol>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DSDChecker
    implements Validator
{
    private static final String CLS_NM = DSDChecker.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    /**
     * This method is called during entity activation, {@link org.apache.directory.fortress.core.util.VUtil#validateConstraints} and ensures the role does not violate dynamic separation of duty constraints.
     *
     * @param session    contains list of RBAC roles {@link org.apache.directory.fortress.core.model.UserRole} targeted for activation.
     * @param constraint required for Validator interface, not used here..
     * @param time       required for Validator interface, not used here.
     * @param type       required by interface, not used here.
     * @return '0' if validation succeeds else {@link org.apache.directory.fortress.core.GlobalErrIds#ACTV_FAILED_DSD} if failed.
     */
    @Override
    public int validate( Session session, Constraint constraint, Time time, VUtil.ConstraintType type )
        throws org.apache.directory.fortress.core.SecurityException
    {
        int rc = 0;
        int matchCount;

        // get all candidate activated roles user:
        List<UserRole> activeRoleList = session.getRoles();
        if ( activeRoleList == null || activeRoleList.size() == 0 )
        {
            return rc;
        }

        // Depending on if session is group or user session, fill objects
        String contextId = session.isGroupSession()
                ? session.getGroup().getContextId()
                : session.getUser().getContextId();
        String entityId = session.isGroupSession() ? session.getGroupName() : session.getUserId();
        String entityType = session.isGroupSession() ? "groupName" : "userId";

        // get the list of authorized roles for this user/group:
        Set<String> authorizedRoleSet = RoleUtil.getInstance().getInheritedRoles( activeRoleList, contextId);
        // only need to check DSD constraints if more than one role is being activated:
        if ( authorizedRoleSet != null && authorizedRoleSet.size() > 1 )
        {
            // get all DSD sets that contain the candidate activated and authorized roles,
            //If DSD cache is disabled, this will search the directory using authorizedRoleSet
            Set<SDSet> dsdSets = SDUtil.getInstance().getDsdCache( authorizedRoleSet, contextId);
            if ( dsdSets != null && dsdSets.size() > 0 )
            {
                for ( SDSet dsd : dsdSets )
                {
                    Iterator<UserRole> activatedRoles = activeRoleList.iterator();
                    matchCount = 0;
                    Set<String> map = dsd.getMembers();

                    // now check the DSD on every role activation candidate contained within session object:
                    while ( activatedRoles.hasNext() )
                    {
                        UserRole activatedRole = activatedRoles.next();

                        if ( map.contains( activatedRole.getName() ) )
                        {
                            matchCount++;
                            if ( matchCount >= dsd.getCardinality() )
                            {
                                activatedRoles.remove();
                                String warning = "validate " + entityType + " [" + entityId
                                    + "] failed activation of assignedRole [" + activatedRole.getName()
                                    + "] validates DSD Set Name:" + dsd.getName() + " Cardinality:"
                                    + dsd.getCardinality();
                                LOG.warn( warning );
                                rc = GlobalErrIds.ACTV_FAILED_DSD;
                                session.setWarning( new ObjectFactory().createWarning( rc, warning,
                                    Warning.Type.ROLE, activatedRole.getName() ) );
                            }
                        }
                        else
                        {
                            Set<String> parentSet = RoleUtil.getInstance().getAscendants( activatedRole.getName(), contextId);
                            // now check for every role inherited from this activated role:
                            for ( String parentRole : parentSet )
                            {
                                if ( map.contains( parentRole ) )
                                {
                                    matchCount++;
                                    if ( matchCount >= dsd.getCardinality() )
                                    {
                                        String warning = "validate " + entityType + " [" + entityId
                                            + "] assignedRole [" + activatedRole.getName() + "] parentRole ["
                                            + parentRole + "] validates DSD Set Name:" + dsd.getName()
                                            + " Cardinality:" + dsd.getCardinality();
                                        rc = GlobalErrIds.ACTV_FAILED_DSD;

                                        // remove the assigned role from session (not the authorized role):
                                        activatedRoles.remove();

                                        session.setWarning( new ObjectFactory().createWarning( rc, warning,
                                            Warning.Type.ROLE, activatedRole.getName() ) );
                                        LOG.warn( warning );
                                        // Breaking out of the loop because assigned role has been removed from session.
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return rc;
    }
}