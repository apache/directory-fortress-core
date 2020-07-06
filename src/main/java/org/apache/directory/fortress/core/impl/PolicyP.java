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


import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.ValidationException;
import org.apache.directory.fortress.core.model.PwPolicy;
import org.apache.directory.fortress.core.util.cache.Cache;
import org.apache.directory.fortress.core.util.cache.CacheMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Process module for the OpenLDAP Password Policy entity.  This class performs data validations and error mapping.
 * It is typically called by internal Fortress manager class {@link PwPolicyMgrImpl} but also
 * needed by {@link org.apache.directory.fortress.core.impl.UserP#validate(org.apache.directory.fortress.core.model.User, boolean)}
 * This class is not intended to be used by external programs.  This class will accept Fortress entity, {@link org.apache.directory.fortress.core.model.PwPolicy}, on its
 * methods, validate contents and forward on to it's corresponding DAO class {@link PolicyDAO}.
 * <p>
 * Class will throw {@link SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link org.apache.directory.fortress.core.FinderException},
 *
 * {@link org.apache.directory.fortress.core.CreateException},{@link org.apache.directory.fortress.core.UpdateException},{@link org.apache.directory.fortress.core.RemoveException}),
 * or {@link org.apache.directory.fortress.core.ValidationException} as {@link SecurityException}s with appropriate
 * error id from {@link org.apache.directory.fortress.core.GlobalErrIds}.
 * <p>
 * This class uses one reference to synchronized data set {@link #policyCache} but is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class PolicyP
{
    private static final String CLS_NM = PolicyP.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    // This is 5 years duration in seconds:
    private static final int MAX_AGE = 157680000;

    // DAO class for ol pw policy data sets must be initialized before the other statics:
    private PolicyDAO olDao = new PolicyDAO();
    // this field is used to synchronize access to the above static data set:
    private static final ReadWriteLock policySetLock = new ReentrantReadWriteLock();
    // static field holds the list of names for all valid pw policies in effect:
    private static Cache policyCache;
    private static final int MIN_PW_LEN = 20;
    private static final int MAX_FAILURE = 100;
    private static final int MAX_GRACE_COUNT = 10;
    private static final int MAX_HISTORY = 100;
    private static final String POLICIES = "policies";
    private static final String FORTRESS_POLICIES = "fortress.policies";

    private void init()
    {
        CacheMgr cacheMgr = CacheMgr.getInstance();
        PolicyP.policyCache = cacheMgr.getCache( FORTRESS_POLICIES );
    }


    /**
     * Package private constructor.
     */
    PolicyP()
    {
        init();
    }


    /**
     * This function uses a case insensitive search.
     *
     * @param policy
     * @return true if valid, false otherwise.
     */
    boolean isValid( PwPolicy policy )
    {
        boolean result = false;

        try
        {
            policySetLock.readLock().lock();

            Set<String> policySet = getPolicySet( policy.getContextId() );

            if ( policySet != null )
            {
                result = policySet.contains( policy.getName() );
            }

            return result;
        }
        finally
        {
            policySetLock.readLock().unlock();
        }
    }


    /**
     * This method will return the password policy entity to the caller.  This command is valid
     * if and only if the policy entry is present in the POLICIES data set.
     *
     * @param policy contains the name of the policy entity.
     * @return PswdPolicy entity returns fully populated with attributes.
     * @throws SecurityException In the event policy entry not found, data validation or system error.
     */
    PwPolicy read( PwPolicy policy ) throws SecurityException
    {
        // Call the finder method for the primary key.
        return olDao.getPolicy( policy );
    }


    /**
     * This method will add a new policy entry to the POLICIES data set.  This command is valid
     * if and only if the policy entry is not already present in the POLICIES data set.
     *
     * @param policy Object contains the password policy attributes.
     * @throws SecurityException In the event of data validation or system error.
     */
    void add( PwPolicy policy ) throws SecurityException
    {
        validate( policy );
        olDao.create( policy );

        try
        {
            policySetLock.writeLock().lock();

            Set<String> policySet = getPolicySet( policy.getContextId() );

            if ( policySet != null )
            {
                policySet.add( policy.getName() );
            }
        }
        finally
        {
            policySetLock.writeLock().unlock();
        }
    }


    /**
     * This method will update an exiting policy entry to the POLICIES data set.  This command is valid
     * if and only if the policy entry is already present in the POLICIES data set.
     *
     * @param policy Object must contain the name of the policy entity.  All non-null attributes will
     *               be updated.  null attributes will be ignored.
     * @throws SecurityException In the event policy not found , data validation or system error.
     */
    void update( PwPolicy policy ) throws SecurityException
    {
        validate( policy );
        olDao.update( policy );
    }


    /**
     * This method will delete exiting policy entry from the POLICIES data set.  This command is valid
     * if and only if the policy entry is already present in the POLICIES data set.  Existing users that
     * are assigned this policy will be removed from association.
     *
     * @param policy Object must contain the name of the policy entity.
     * @throws SecurityException In the event policy entity not found or system error.
     */
    void delete( PwPolicy policy ) throws SecurityException
    {
        olDao.remove( policy );

        try
        {
            policySetLock.writeLock().lock();

            Set<String> policySet = getPolicySet( policy.getContextId() );

            if ( policySet != null )
            {
                policySet.remove( policy.getName() );
            }
        }
        finally
        {
            policySetLock.writeLock().unlock();
        }
    }


    /**
     * This method will return a list of all password policy entities that match a particular search string.
     * This command will return an empty list of no matching entries are found.
     *
     * @param policy contains the leading chars of a policy entity.  This search is not case sensitive.
     * @return List<PswdPolicy> contains all matching password policy entities. If no records found this will be empty.
     * @throws SecurityException In the event of data validation or system error.
     */
    List<PwPolicy> search( PwPolicy policy ) throws SecurityException
    {
        return olDao.findPolicy( policy );
    }


    /**
     * Method will perform simple validations to ensure the integrity of the OpenLDAP Password Policy entity targeted for insertion
     * or updating in directory.  Data reasonability checks will be performed on all non-null attributes.
     *
     * @param policy contains data targeted for insertion or update.
     * @throws ValidationException in the event of data validation error or DAO error on Org validation.
     */
    private void validate( PwPolicy policy ) throws ValidationException
    {
        int length = policy.getName().length();

        if ( length < 1 || length > GlobalIds.PWPOLICY_NAME_LEN )
        {
            String error = "validate policy name [" + policy.getName() + "] INVALID LENGTH [" + length + "]";
            LOG.error( error );
            throw new ValidationException( GlobalErrIds.PSWD_NAME_INVLD_LEN, error );
        }

        if ( policy.getCheckQuality() != null )
        {
            try
            {
                if ( policy.getCheckQuality() < 0 || policy.getCheckQuality() > 2 )
                {
                    String error = "validate policy name [" + policy.getName() + "] value checkQuality ["
                        + policy.getCheckQuality() + "] INVALID INT VALUE";
                    LOG.error( error );
                    throw new ValidationException( GlobalErrIds.PSWD_QLTY_INVLD, error );
                }
            }
            catch ( java.lang.NumberFormatException nfe )
            {
                String error = "validate policy name [" + policy.getName() + "] value checkQuality ["
                    + policy.getCheckQuality() + "] INVALID INT VALUE";
                LOG.error( error );
                throw new ValidationException( GlobalErrIds.PSWD_QLTY_INVLD, error );
            }
        }

        if ( policy.getMaxAge() != null )
        {
            if ( policy.getMaxAge() > MAX_AGE )
            {
                String error = "validate policy name [" + policy.getName() + "] value maxAge [" + policy.getMaxAge()
                    + "] INVALID INT VALUE";
                LOG.error( error );
                throw new ValidationException( GlobalErrIds.PSWD_MAXAGE_INVLD, error );
            }
        }

        if ( policy.getMinAge() != null )
        {
            // policy.minAge
            if ( policy.getMinAge() > MAX_AGE )
            {
                String error = "validate policy name [" + policy.getName() + "] value minAge [" + policy.getMinAge()
                    + "] INVALID INT VALUE";
                LOG.error( error );
                throw new ValidationException( GlobalErrIds.PSWD_MINAGE_INVLD, error );
            }
        }

        if ( policy.getMinLength() != null )
        {
            if ( policy.getMinLength() > MIN_PW_LEN )
            {
                String error = "validate policy name [" + policy.getName() + "] value minLength ["
                    + policy.getMinLength() + "] INVALID INT VALUE";
                LOG.error( error );
                throw new ValidationException( GlobalErrIds.PSWD_MINLEN_INVLD, error );
            }
        }

        if ( policy.getFailureCountInterval() != null )
        {
            if ( policy.getFailureCountInterval() > MAX_AGE )
            {
                String error = "validate policy name [" + policy.getName() + "] value failureCountInterval ["
                    + policy.getFailureCountInterval() + "] INVALID INT VALUE";
                LOG.error( error );
                throw new ValidationException( GlobalErrIds.PSWD_INTERVAL_INVLD, error );
            }
        }

        if ( policy.getMaxFailure() != null )
        {
            if ( policy.getMaxFailure() > MAX_FAILURE )
            {
                String error = "validate policy name [" + policy.getName() + "] value maxFailure ["
                    + policy.getMaxFailure() + "] INVALID INT VALUE";
                LOG.error( error );
                throw new ValidationException( GlobalErrIds.PSWD_MAXFAIL_INVLD, error );
            }
        }

        if ( policy.getInHistory() != null )
        {
            if ( policy.getInHistory() > MAX_HISTORY )
            {
                String error = "validate policy name [" + policy.getName() + "] value inHistory ["
                    + policy.getInHistory() + "] INVALID VALUE";
                LOG.error( error );
                throw new ValidationException( GlobalErrIds.PSWD_HISTORY_INVLD, error );
            }
        }

        if ( policy.getGraceLoginLimit() != null )
        {
            if ( policy.getGraceLoginLimit() > MAX_GRACE_COUNT )
            {
                String error = "validate policy name [" + policy.getName() + "] value graceLoginLimit ["
                    + policy.getGraceLoginLimit() + "] INVALID VALUE";
                LOG.error( error );
                throw new ValidationException( GlobalErrIds.PSWD_GRACE_INVLD, error );
            }
        }

        if ( policy.getLockoutDuration() != null )
        {
            if ( policy.getLockoutDuration() > MAX_AGE )
            {
                String error = "validate policy name [" + policy.getName() + "] value lockoutDuration ["
                    + policy.getLockoutDuration() + "] INVALID VALUE";
                LOG.error( error );
                throw new ValidationException( GlobalErrIds.PSWD_LOCKOUTDUR_INVLD, error );
            }
        }

        if ( policy.getExpireWarning() != null )
        {
            if ( policy.getExpireWarning() > MAX_AGE )
            {
                String error = "validate policy name [" + policy.getName() + "] value expireWarning ["
                    + policy.getExpireWarning() + "] INVALID VALUE";
                LOG.error( error );
                throw new ValidationException( GlobalErrIds.PSWD_EXPWARN_INVLD, error );
            }
        }
    }


    /**
     * Load the cache with read only list of valid openldap policy names.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of unique names.
     */
    private Set<String> loadPolicySet( String contextId )
    {
        Set<String> policySet = null;

        try
        {
            policySet = olDao.getPolicies( contextId );
        }
        catch ( SecurityException se )
        {
            String warning = "loadPolicySet static initializer caught SecurityException=" + se;
            LOG.info( warning );
        }

        policyCache.put( getKey( contextId ), policySet );

        return policySet;
    }


    /**
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return set containing list of policy names active.
     */
    private Set<String> getPolicySet( String contextId )
    {
        try
        {
            policySetLock.readLock().lock();

            Set<String> policySet = ( Set<String> ) policyCache.get( getKey( contextId ) );

            if ( policySet == null )
            {
                policySet = loadPolicySet( contextId );
            }

            return policySet;
        }
        finally
        {
            policySetLock.readLock().unlock();
        }
    }


    /**
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return key for tenant's cache entry.
     */
    private static String getKey( String contextId )
    {
        String key = POLICIES;

        if ( StringUtils.isNotEmpty( contextId ) && !contextId.equalsIgnoreCase( GlobalIds.NULL ) )
        {
            key += ":" + contextId;
        }

        return key;
    }
}