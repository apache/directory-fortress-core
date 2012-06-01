/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.SecurityException;
import com.jts.fortress.ReviewMgr;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.util.cache.Cache;
import com.jts.fortress.util.cache.CacheMgr;
import com.jts.fortress.util.time.Constraint;

import java.util.List;
import java.util.Set;


/**
 * This utilty provides functionality to process SSD requests.
 *
 * @author Shawn McKinney
 * @created September 3, 2010
 */
public class SDUtil
{
    private static final String CLS_NM = SDUtil.class.getName();
    private static ReviewMgr rMgr = new ReviewMgrImpl();
    private static Cache m_dsdCache;
    private static final String FORTRESS_DSDS = "fortress.dsd";
    private static Cache m_ssdCache;
    private static final String FORTRESS_SSDS = "fortress.ssd";

    static
    {
        CacheMgr cacheMgr = CacheMgr.getInstance();
        SDUtil.m_dsdCache = cacheMgr.getCache(FORTRESS_DSDS);
        SDUtil.m_ssdCache = cacheMgr.getCache(FORTRESS_SSDS);
    }

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
        List<SDSet> ssdSets = getSsdCache(role.getName());
        for (SDSet ssd : ssdSets)
        {
            matchCount = 0;
            Set<String> map = ssd.getMembers();
            // iterate over every authorized role for user:
            for (String authRole : rls)
            {
                // is there a match found between authorized role and SSD set's members?
                if (map.contains(authRole))
                {
                    matchCount++;
                    // does the match count exceed the cardinality allowed for this particular SSD set?
                    if (matchCount >= ssd.getCardinality() - 1)
                    {
                        String error = CLS_NM + ".validateSSD new role [" + role.getName() + "] validates SSD Set Name:" + ssd.getName() + " Cardinality:" + ssd.getCardinality() + ", Count:" + matchCount;
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
        List<SDSet> dsdSets = getDsdCache(role.getName());
        for (SDSet dsd : dsdSets)
        {
            // Keeps the number of matched roles to a particular DSD set.
            int matchCount = 0;

            // Contains the list of roles assigned to a particular DSD set.
            Set<String> map = dsd.getMembers();

            // iterate over every role active in session for match wth DSD members:
            for (UserRole actRole : rls)
            {
                // is there a match found between active role in session and DSD set members?
                if (map.contains(actRole.getName()))
                {
                    // Yes, we found a match, increment the count.
                    matchCount++;

                    // Does the match count exceed the cardinality allowed for this particular DSD set?
                    if (matchCount >= dsd.getCardinality() - 1)
                    {
                        // Yes, the target role violates DSD cardinality rule.
                        String error = CLS_NM + ".validateDSD failed for role [" + role.getName() + "] DSD Set Name:" + dsd.getName() + " Cardinality:" + dsd.getCardinality() + ", Count:" + matchCount;
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
                        if (map.contains(parentRole)) // is there match between parent and DSD member?
                        {
                            matchCount++;
                            if (matchCount >= dsd.getCardinality() - 1) // Does the counter exceed max per cardinality on this DSD set?
                            {
                                String error = CLS_NM + ".validateDSD failed for role [" + role.getName() + "] parent role [" + parentRole + "] DSD Set Name:" + dsd.getName() + " Cardinality:" + dsd.getCardinality() + ", Count:" + matchCount;
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

    /**
     *
     * @param name
     * @throws SecurityException
     */
    static void clearDsdCacheEntry(String name)
        throws SecurityException
    {
        m_dsdCache.clear(name);
    }

    /**
     *
     * @param name
     * @return
     * @throws SecurityException
     */
    private static List<SDSet> putDsdCache(String name)
        throws SecurityException
    {
        List<SDSet> dsdSets = rMgr.dsdRoleSets(new Role(name));
        m_dsdCache.put(name, dsdSets);
        return dsdSets;
    }

    /**
     *
     * @param name
     * @return
     * @throws SecurityException
     */
    private static List<SDSet> getDsdCache(String name)
        throws SecurityException
    {
        List<SDSet> dsdSets = (List<SDSet>) m_dsdCache.get(name);
        if (dsdSets == null)
        {
            dsdSets = putDsdCache(name);
        }
        return dsdSets;
    }

    /**
     *
     * @param name
     * @throws SecurityException
     */
    static void clearSsdCacheEntry(String name)
        throws SecurityException
    {
        m_ssdCache.clear(name);
    }

    /**
     *
     * @param name
     * @return
     * @throws SecurityException
     */
    private static List<SDSet> putSsdCache(String name)
        throws SecurityException
    {
        List<SDSet> ssdSets = rMgr.ssdRoleSets(new Role(name));
        m_ssdCache.put(name, ssdSets);
        return ssdSets;
    }

    /**
     *
     * @param name
     * @return
     * @throws SecurityException
     */
    private static List<SDSet> getSsdCache(String name)
        throws SecurityException
    {
        List<SDSet> ssdSets = (List<SDSet>) m_ssdCache.get(name);
        if (ssdSets == null)
        {
            ssdSets = putSsdCache(name);
        }
        return ssdSets;
    }
}
