/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.arbac;

import com.jts.fortress.SecurityException;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.rbac.UserP;

import java.util.List;
import java.util.Set;

import com.jts.fortress.util.attr.VUtil;
import org.apache.log4j.Logger;

/**
 * Process module for the OrgUnit entity. The Fortress OrgUnit data set can be associated with two entities:
 * {@link com.jts.fortress.rbac.User} class or {@link com.jts.fortress.rbac.PermObj} class.  The OrgUnit entity itself is stored in two separate locations in the ldap tree one
 * for each entity listed above.  The type of OU entity is set via the enum attribute {@link com.jts.fortress.arbac.OrgUnit.Type} which is equal to 'PERM' or 'USER'.
 * This class performs data validations.  The methods of this class are called by internal Fortress manager impl classes
 * {@link com.jts.fortress.arbac.DelegatedAdminMgrImpl} and {@link DelegatedReviewMgrImpl} but is also called by {@link com.jts.fortress.rbac.PermP#validate} method and {@link UserP#validate} functions
 * which ensure the entities are related to valid OU entries. This class is not intended to be called external
 * to Fortress Core itself.  This class will accept Fortress entity {@link com.jts.fortress.arbac.OrgUnit}, validate its contents and forward on to it's
 * corresponding DAO class {@link com.jts.fortress.arbac.OrgUnitDAO} for data access.
 * <p>
 * Class will throw {@link SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link com.jts.fortress.FinderException},
 * {@link com.jts.fortress.CreateException},{@link com.jts.fortress.UpdateException},{@link com.jts.fortress.RemoveException}),
 *  or {@link com.jts.fortress.ValidationException} as {@link com.jts.fortress.SecurityException}s with appropriate
 * error id from {@link GlobalErrIds}.
 * <p>
 * This object uses 2 synchronized data sets ({@link #userPool}, {@link #permPool}) but is thread safe.
 * <p/>

 * @author smckinn
 * @created September 18, 2010
 */
public final class OrgUnitP
{
    // init the logger:
    private static final String OCLS_NM = OrgUnitP.class.getName();
    private static final Logger log = Logger.getLogger(OCLS_NM);

    // these fields are used to synchronize access to the above static pools:
    private static final Object userPoolSynchLock = new Object();
    private static final Object permPoolSynchLock = new Object();

    // DAO class for OU data sets must be initializer before the other statics:
    private static final OrgUnitDAO oDao = new OrgUnitDAO();
    // these static data sets are use to store the list of valid user ou's and perm ou's:
    private static Set<String> userPool = loadOrgSet(OrgUnit.Type.USER);
    private static Set<String> permPool = loadOrgSet(OrgUnit.Type.PERM);

    /**
     * This function uses a case insensitive search.
     * @param entity
     * @return
     */
    public boolean isValid(OrgUnit entity)
    {
        boolean result = false;
        if (userPool != null && entity.getType() == OrgUnit.Type.USER)
        {
            result = userPool.contains(entity.getName());
        }
        else if (permPool != null)
        {
            result = permPool.contains(entity.getName());
        }
        return result;
    }

    /**
     * @param type
     * @return
     */
    private static Set<String> loadOrgSet(OrgUnit.Type type)
    {
        Set<String> ouSet = null;
        try
        {
            ouSet = oDao.getOrgs(type);
        }
        catch (SecurityException se)
        {
            String warning = OCLS_NM + ".loadOrgSet static initializer caught SecurityException=" + se;
            log.info(warning, se);
        }
        return ouSet;
    }


    /**
     * Return a fully populated OrgUnit entity for a given Perm or User orgUnitId.  If matching record not found a
     * SecurityException will be thrown.
     *
     * @param entity contains full orgUnit name used for User or Perm OU data sets in directory.
     * @return OrgUnit entity containing all attributes associated with ou in directory.
     * @throws com.jts.fortress.SecurityException in the event OrgUnit not found or DAO search error.
     */
    public OrgUnit read(OrgUnit entity)
        throws SecurityException
    {
        validate(entity, false);
        return oDao.findByKey(entity);
    }

    /**
     * Will search either User or Perm OrgUnit data sets depending on which type is passed.
     * The search string that contains full or partial OrgUnit name associated with OU node in directory.
     *
     * @param searchVal contains full or partial OU name.
     * @return List of type OrgUnit containing fully populated matching OU entities.  If no records found this will be empty.
     * @throws SecurityException in the event of DAO search error.
     */
    public final List<OrgUnit> search(OrgUnit.Type type, String searchVal)
        throws SecurityException
    {
        // Call the finder.
        return oDao.findOrgs(type, searchVal);
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
    public OrgUnit add(OrgUnit entity)
        throws SecurityException
    {
        validate(entity, false);
        OrgUnit oe = oDao.create(entity);
        if (entity.getType() == OrgUnit.Type.USER)
        {
            synchronized (userPoolSynchLock)
            {
                if(userPool != null)
                    userPool.add(entity.getName());
            }
        }
        else
        {
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
    public OrgUnit update(OrgUnit entity)
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
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    public OrgUnit delete(OrgUnit entity)
        throws SecurityException
    {
        oDao.remove(entity);
        if (entity.getType() == OrgUnit.Type.USER)
        {
            synchronized (userPoolSynchLock)
            {
                if(userPool != null)
                    userPool.remove(entity.getName());
            }
        }
        else
        {
            synchronized (permPoolSynchLock)
            {
                if(permPool != null)
                    permPool.remove(entity.getName());
            }
        }
        return entity;
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
    void validate(OrgUnit entity, boolean isUpdate)
        throws SecurityException
    {
        VUtil.safeText(entity.getName(), GlobalIds.OU_LEN);
        if (VUtil.isNotNullOrEmpty(entity.getDescription()))
        {
            VUtil.description(entity.getDescription());
        }
        if (entity.getType() == null)
        {
            String error = OCLS_NM + ".validate null or empty org unit type";
            //log.warn(error);
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
}