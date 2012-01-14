/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.SecurityException;
import com.jts.fortress.ReviewMgr;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.util.time.Constraint;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * This utilty provides functionality to process SSD requests.
 *

 * @author smckinn
 * @created September 3, 2010
 */
public class SDUtil
{
    /**
     * Description of the Field
     */
    private static final String OCLS_NM = SDUtil.class.getName();
    private static ReviewMgr rMgr = new ReviewMgrImpl();

    /**
     * This method is called by AdminMgr.assignUser and is used to validate Static Separation of Duty
     * constraints when assigning a role to user.
     *
     * @param uRole
     * @throws com.jts.fortress.SecurityException
     *
     */
    public static void validateSSD(UserRole uRole)
        throws SecurityException
    {
        validateSSD(new User(uRole.getUserId()), new Role(uRole.getName()));
    }

    /**
     * This method is called by AdminMgr.assignUser and is used to validate Static Separation of Duty
     * constraints when assigning a role to user.
     *
     * @param user
     * @param role
     * @throws com.jts.fortress.SecurityException
     *
     */
    public static void validateSSD(User user, Role role)
        throws SecurityException
    {
        int matchCount;
        // get all authorized roles for user
        Set<String> rls = rMgr.authorizedRoles(user);
        if (rls == null || rls.size() == 0)
        {
            return;
        }

        // get all SSD sets that contain the new role
        List<SDSet> ssdSets = rMgr.ssdRoleSets(role);
        for (SDSet ssd : ssdSets)
        {
            matchCount = 0;
            Map<String, String> map = ssd.getMembers();
            // iterate over every authorized role for user:
            for (String authRole : rls)
            {
                // is there a match found between authorized role and SSD set's members?
                if (map.containsKey(authRole))
                {
                    matchCount++;
                    // does the match count exceed the cardinality allowed for this particular SSD set?
                    if (matchCount >= ssd.getCardinality() - 1)
                    {
                        String error = OCLS_NM + ".validateSSD new role [" + role.getName() + "] validates SSD Set Name:" + ssd.getName() + " Cardinality:" + ssd.getCardinality() + ", Count:" + matchCount;
                        throw new SecurityException(GlobalErrIds.SSD_VALIDATION_FAILED, error);
                    }
                }
            }
        }
    }


    /**
     * This method is called by AccessMgr.addActiveRole and is used to validate Dynamic Separation of Duty
     * constraints when activating a role one at a time.  For activation of multiple roles simultaneously use
     * the DSD.validate API which is used during createSession sequence.
     *
     * @param session
     * @param role
     * @throws com.jts.fortress.SecurityException
     *
     */
    public static void validateDSD(Session session, Constraint role)
        throws SecurityException
    {
        //int matchCount;
        // get all activated roles from user's session:
        List<UserRole> rls = session.getRoles();
        if (rls == null || rls.size() == 0)
        {
            // An empty list of roles was passed in the session variable.
            // No need to continue.
            return;
        }
        // get all DSD sets that contain the target role
        List<SDSet> dsdSets = rMgr.dsdRoleSets(new Role(role.getName()));
        for (SDSet dsd : dsdSets)
        {
            // Keeps the number of matched roles to a particular DSD set.
            int matchCount = 0;

            // Contains the list of roles assigned to a particular DSD set.
            Map<String, String> map = dsd.getMembers();

            // iterate over every role active in session for match wth DSD members:
            for (UserRole actRole : rls)
            {
                // is there a match found between active role in session and DSD set members?
                if (map.containsKey(actRole.getName()))
                {
                    // Yes, we found a match, increment the count.
                    matchCount++;

                    // Does the match count exceed the cardinality allowed for this particular DSD set?
                    if (matchCount >= dsd.getCardinality() - 1)
                    {
                        // Yes, the target role violoates DSD cardinality rule.
                        String error = OCLS_NM + ".validateDSD failed for role [" + role.getName() + "] DSD Set Name:" + dsd.getName() + " Cardinality:" + dsd.getCardinality() + ", Count:" + matchCount;
                        throw new SecurityException(GlobalErrIds.DSD_VALIDATION_FAILED, error);
                    }
                }
                else // Check the parents of activated role for DSD match:
                {
                    // Now pull the activated role's list of parents.
                    Set<String> parentSet = RoleUtil.getAscendants(actRole.getName());

                    // Iterate over the list of parent roles:
                    for (String parentRole : parentSet)
                    {
                        if (map.containsKey(parentRole)) // is there match between parent and DSD member?
                        {
                            matchCount++;
                            if (matchCount >= dsd.getCardinality() - 1) // Does the counter exceed max per cardinality on this DSD set?
                            {
                                String error = OCLS_NM + ".validateDSD failed for role [" + role.getName() + "] parent role [" + parentRole + "] DSD Set Name:" + dsd.getName() + " Cardinality:" + dsd.getCardinality() + ", Count:" + matchCount;
                                throw new SecurityException(GlobalErrIds.DSD_VALIDATION_FAILED, error);
                            }
                            // Breaking out of the loop here means the DSD algorithm will only match one
                            // role per parent of active role candidate.
                            break;
                        }
                    }
                }
            }
        }
    }
}
