/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.GlobalErrIds;
import com.jts.fortress.GlobalIds;
import com.jts.fortress.SecurityException;

import java.util.List;
import java.util.Set;

import com.jts.fortress.util.attr.VUtil;
import com.jts.fortress.util.cache.CacheMgr;
import com.jts.fortress.util.cache.Cache;
import org.apache.log4j.Logger;

/**
 * Process module for the OrgUnit entity. The Fortress OrgUnit data set can be associated with two entities:
 * {@link com.jts.fortress.rbac.User} class or {@link com.jts.fortress.rbac.PermObj} class.  The OrgUnit entity itself is stored in two separate locations in the ldap tree one
 * for each entity listed above.  The type of OU entity is set via the enum attribute {@link OrgUnit.Type} which is equal to 'PERM' or 'USER'.
 * This class performs data validations.  The methods of this class are called by internal Fortress manager impl classes
 * {@link DelAdminMgrImpl} and {@link DelReviewMgrImpl} but is also called by {@link com.jts.fortress.rbac.PermP#validate} method and {@link UserP#validate} functions
 * which ensure the entities are related to valid OU entries. This class is not intended to be called external
 * to Fortress Core itself.  This class will accept Fortress entity {@link OrgUnit}, validate its contents and forward on to it's
 * corresponding DAO class {@link OrgUnitDAO} for data access.
 * <p>
 * Class will throw {@link SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link com.jts.fortress.FinderException},
 * {@link com.jts.fortress.CreateException},{@link com.jts.fortress.UpdateException},{@link com.jts.fortress.RemoveException}),
 *  or {@link com.jts.fortress.ValidationException} as {@link com.jts.fortress.SecurityException}s with appropriate
 * error id from {@link GlobalErrIds}.
 * <p>
 * This object uses synchronized data sets ({@link #ouCache} but is thread safe.
 * <p/>

 * @author Shawn McKinney
 * @created September 18, 2010
 */
final class OrgUnitP
{
    // init the logger:
    private static final String CLS_NM = OrgUnitP.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);

    // these fields are used to synchronize access to the above static pools:
    private static final Object userPoolSynchLock = new Object();
    private static final Object permPoolSynchLock = new Object();
    private static Cache ouCache;

    // DAO class for OU data sets must be initializer before the other statics:
    private static final OrgUnitDAO oDao = new OrgUnitDAO();
    private static final String USER_OUS = "user.ous";
    private static final String PERM_OUS = "perm.ous";
    private static final String FORTRESS_OUS = "fortress.ous";

    static
    {
        CacheMgr cacheMgr = CacheMgr.getInstance();
        OrgUnitP.ouCache = cacheMgr.getCache(FORTRESS_OUS);
    }

    /**
     * Package private constructor.
     */
    OrgUnitP()
    {}

    /**
     * This function uses a case insensitive search.
     * @param entity
     * @return
     */
    final boolean isValid(OrgUnit entity)
    {
        boolean result = false;
        if(entity.type == OrgUnit.Type.USER)
        {
            Set<String> userPool = getOrgSet(entity);
            if (userPool != null && entity.getType() == OrgUnit.Type.USER)
            {
                result = userPool.contains(entity.getName());
            }
        }
        else
        {
            Set<String> permPool = getOrgSet(entity);
            if (permPool != null)
            {
                result = permPool.contains(entity.getName());
            }
        }
        return result;
    }

    /**
     * @param orgUnit
     * @return
     */
    private static final Set<String> loadOrgSet(OrgUnit orgUnit)
    {
        Set<String> ouSet = null;
        try
        {
            ouSet = oDao.getOrgs(orgUnit);
        }
        catch (SecurityException se)
        {
            String warning = CLS_NM + ".loadOrgSet static initializer caught SecurityException=" + se;
            log.info(warning, se);
        }
        if(orgUnit.getType() == OrgUnit.Type.USER)
        {
            // TODO:  add context id to this cache
            ouCache.put(getKey(USER_OUS, orgUnit.getContextId()), ouSet);
        }
        else
        {
            ouCache.put(getKey(PERM_OUS, orgUnit.getContextId()), ouSet);
        }

        return ouSet;
    }

    /**
     *
     * @param orgUnit will either be a User or Perm OU.
     * @return Set containing the OU mapping to a particular type and tenant.
     */
    private static final Set<String> getOrgSet(OrgUnit orgUnit)
    {
        Set<String> orgSet;
        if(orgUnit.getType() == OrgUnit.Type.USER)
        {
            orgSet = (Set<String>)ouCache.get(getKey(USER_OUS, orgUnit.getContextId()));
        }
        else
        {
            orgSet = (Set<String>)ouCache.get(getKey(PERM_OUS, orgUnit.getContextId()));
        }

        if (orgSet == null)
        {
            orgSet = loadOrgSet(orgUnit);
        }
        return orgSet;
    }


    /**
     * Return a fully populated OrgUnit entity for a given Perm or User orgUnitId.  If matching record not found a
     * SecurityException will be thrown.
     *
     * @param entity contains full orgUnit name used for User or Perm OU data sets in directory.
     * @return OrgUnit entity containing all attributes associated with ou in directory.
     * @throws com.jts.fortress.SecurityException in the event OrgUnit not found or DAO search error.
     */
    final OrgUnit read(OrgUnit entity)
        throws SecurityException
    {
        validate(entity, false);
        return oDao.findByKey(entity);
    }

    /**
     * Will search either User or Perm OrgUnit data sets depending on which type is passed.
     * The search string that contains full or partial OrgUnit name associated with OU node in directory.
     *
     * @param orgUnit contains full or partial OU name.
     * @return List of type OrgUnit containing fully populated matching OU entities.  If no records found this will be empty.
     * @throws SecurityException in the event of DAO search error.
     */
    final List<OrgUnit> search(OrgUnit orgUnit)
        throws SecurityException
    {
        // Call the finder.
        return oDao.findOrgs(orgUnit);
    }


    /**
     * Adds a new OrgUnit to directory. The OrgUnit type enum will determine which data set insertion will
     * occur - User or Perm.  The OrgUnit entity input will be validated to ensure that:
     * orgUnit name is present and type is specified, and reasonability checks on all of the other populated values.
     *
     * @param entity OrgUnit contains data targeted for insertion.
     * @return OrgUnit entity copy of input + additional attributes (internalId) that were added by op.
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    final OrgUnit add(OrgUnit entity)
        throws SecurityException
    {
        validate(entity, false);
        OrgUnit oe = oDao.create(entity);
        if (entity.getType() == OrgUnit.Type.USER)
        {
            Set<String> userPool = getOrgSet(entity);
            synchronized (userPoolSynchLock)
            {
                if(userPool != null)
                    userPool.add(entity.getName());
            }
        }
        else
        {
            Set<String> permPool = getOrgSet(entity);
            synchronized (permPoolSynchLock)
            {
                if(permPool != null)
                    permPool.add(entity.getName());
            }
        }
        return oe;
    }


    /**
     * Updates existing OrgUnit in directory. The OrgUnit type enum will determine which data set insertion will
     * occur - User or Perm.  The OrgUnit entity input will be validated to ensure that:
     * orgUnit name is present, and reasonability checks on all of the other populated values.
     * Null or empty attributes are ignored.
     *
     * @param entity OrgUnit contains data targeted for updating.  Null attributes ignored.
     * @return OrgUnit entity copy of input + additional attributes (internalId) that were updated by op.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    final OrgUnit update(OrgUnit entity)
        throws SecurityException
    {
        validate(entity, false);
        return oDao.update(entity);
    }


    /**
     * This method performs a "hard" delete.  It completely the OrgUnit node from the ldap directory.
     * The OrgUnit type enum will determine where deletion will occur - User or Perm OU data sets.
     * OrgUnit entity must exist in directory prior to making this call else exception will be thrown.
     *
     * @param entity Contains the name of the OrgUnit node targeted for deletion.
     * @return OrgUnit entity copy of input.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    final OrgUnit delete(OrgUnit entity)
        throws SecurityException
    {
        oDao.remove(entity);
        if (entity.getType() == OrgUnit.Type.USER)
        {
            Set<String> userPool = getOrgSet(entity);
            synchronized (userPoolSynchLock)
            {
                if(userPool != null)
                    userPool.remove(entity.getName());
            }
        }
        else
        {
            Set<String> permPool = getOrgSet(entity);
            synchronized (permPoolSynchLock)
            {
                if(permPool != null)
                    permPool.remove(entity.getName());
            }
        }
        return entity;
    }


    /**
     * Return all OrgUnits that have a parent assignment.  This used for hierarchical processing.
     *
      * @param orgUnit will either be a User or Perm OU.
     * @return List of type OrgUnit containing {@link OrgUnit#name} and {@link OrgUnit#parents} populated.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    final List<Graphable> getAllDescendants(OrgUnit orgUnit)
        throws SecurityException
    {
        return oDao.getAllDescendants(orgUnit);
    }


    /**
     * Method will perform simple validations to ensure the integrity of the OrgUnit entity targeted for insertion
     * or updating in directory.  This method will ensure the name and type enum are specified.  It will also perform
     * reasonability check on description if set.
     *
     * @param entity   contains the enum type to validate
     * @param isUpdate not used at this time.
     * @throws SecurityException thrown in the event the attribute is null.
     */
    private void validate(OrgUnit entity, boolean isUpdate)
        throws SecurityException
    {
        VUtil.safeText(entity.getName(), GlobalIds.OU_LEN);
        if (VUtil.isNotNullOrEmpty(entity.getDescription()))
        {
            VUtil.description(entity.getDescription());
        }
        if (entity.getType() == null)
        {
            String error = CLS_NM + ".validate null or empty org unit type";
            int errCode;
            if(entity.getType() == OrgUnit.Type.PERM)
            {
                errCode = GlobalErrIds.ORG_TYPE_NULL_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_TYPE_NULL_USER;
            }
            throw new SecurityException(errCode, error);
        }
    }


    /**
     * Build a key that is composed of the OU type ({@link #USER_OUS} or {@link #PERM_OUS}) and the contextId which is the id of tenant.
     *
     * @param type either {@link #USER_OUS} or {@link #PERM_OUS}.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return
     */
    private static String getKey(String type, String contextId)
    {
        String key = type;
        if(VUtil.isNotNullOrEmpty(contextId) && !contextId.equalsIgnoreCase(GlobalIds.NULL))
        {
            key += ":" + contextId;

        }
        return key;
    }
}