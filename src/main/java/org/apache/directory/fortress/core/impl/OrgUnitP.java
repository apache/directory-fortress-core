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
import org.apache.directory.fortress.core.model.Graphable;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.util.VUtil;
import org.apache.directory.fortress.core.util.cache.Cache;
import org.apache.directory.fortress.core.util.cache.CacheMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Process module for the OrgUnit entity. The Fortress OrgUnit data set can be associated with two entities:
 * {@link org.apache.directory.fortress.core.model.User} class or {@link org.apache.directory.fortress.core.model.PermObj} class.  The OrgUnit entity itself is stored in two separate locations in the ldap tree one
 * for each entity listed above.  The type of OU entity is set via the enum attribute {@link org.apache.directory.fortress.core.model.OrgUnit.Type} which is equal to 'PERM' or 'USER'.
 * This class performs data validations.  The methods of this class are called by internal Fortress manager impl classes
 * {@link DelAdminMgrImpl} and {@link DelReviewMgrImpl} but is also called by {@link org.apache.directory.fortress.core.impl.PermP#validate} method and {@link org.apache.directory.fortress.core.impl.UserP#validate} functions
 * which ensure the entities are related to valid OU entries. This class is not intended to be called external
 * to Fortress Core itself.  This class will accept Fortress entity {@link org.apache.directory.fortress.core.model.OrgUnit}, validate its contents and forward on to it's
 * corresponding DAO class {@link OrgUnitDAO} for data access.
 * <p>
 * Class will throw {@link SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link org.apache.directory.fortress.core.FinderException},
 * {@link org.apache.directory.fortress.core.CreateException},{@link org.apache.directory.fortress.core.UpdateException},{@link org.apache.directory.fortress.core.RemoveException}),
 *  or {@link org.apache.directory.fortress.core.ValidationException} as {@link SecurityException}s with appropriate
 * error id from {@link GlobalErrIds}.
 * <p>
 * This class uses synchronized data sets ({@link #ouCache} but is thread safe.

 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class OrgUnitP
{
    // init the logger:
    private static final String CLS_NM = OrgUnitP.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    // these fields are used to synchronize access to the above static pools:
    private static final ReadWriteLock userPoolLock = new ReentrantReadWriteLock();
    private static final ReadWriteLock permPoolLock = new ReentrantReadWriteLock();
    private static Cache ouCache;

    // DAO class for OU data sets must be initializer before the other statics:
    private OrgUnitDAO oDao = new OrgUnitDAO();
    private static final String USER_OUS = "user.ous";
    private static final String PERM_OUS = "perm.ous";
    private static final String FORTRESS_OUS = "fortress.ous";

    private void init()
    {
        CacheMgr cacheMgr = CacheMgr.getInstance();
        OrgUnitP.ouCache = cacheMgr.getCache( FORTRESS_OUS );
    }


    /**
     * Package private constructor.
     */
    OrgUnitP()
    {
        init();
    }


    /**
     * This function uses a case insensitive search.
     * @param entity
     * @return true if valid, false otherwise.
     */
    /* No Qualifier */boolean isValid( OrgUnit entity )
    {
        boolean result = false;

        if ( entity.type == OrgUnit.Type.USER )
        {
            try
            {
                userPoolLock.readLock().lock();
                Set<String> userPool = getUserSet( entity );

                if ( userPool != null )
                {
                    result = userPool.contains( entity.getName() );
                }
            }
            finally
            {
                userPoolLock.readLock().unlock();
            }
        }
        else
        {
            try
            {
                permPoolLock.readLock().lock();
                Set<String> permPool = getPermSet( entity );

                if ( permPool != null )
                {
                    result = permPool.contains( entity.getName() );
                }
            }
            finally
            {
                permPoolLock.readLock().unlock();
            }
        }

        return result;
    }


    /**
     * Loads the User set for an orgUnit
     * @param orgUnit The orgUnit
     * @return The set of associated User
     */
    private Set<String> loadUserSet( OrgUnit orgUnit )
    {
        Set<String> ouUserSet = null;

        try
        {
            ouUserSet = oDao.getOrgs( orgUnit );
        }
        catch ( SecurityException se )
        {
            String warning = "loadOrgSet static initializer caught SecurityException=" + se;
            LOG.info( warning, se );
        }

        ouCache.put( getKey( USER_OUS, orgUnit.getContextId() ), ouUserSet );

        return ouUserSet;
    }


    /**
     * Loads the Perm set for an orgUnit
     * @param orgUnit The orgUnit
     * @return The set of associated Perms
     */
    private Set<String> loadPermSet( OrgUnit orgUnit )
    {
        Set<String> ouPermSet = null;

        try
        {
            ouPermSet = oDao.getOrgs( orgUnit );
        }
        catch ( SecurityException se )
        {
            String warning = "loadOrgSet static initializer caught SecurityException=" + se;
            LOG.info( warning, se );
        }

        ouCache.put( getKey( PERM_OUS, orgUnit.getContextId() ), ouPermSet );

        return ouPermSet;
    }


    /**
     *
     * @param orgUnit will be a Perm OU.
     * @return Set containing the OU mapping to a Perm type and tenant.
     */
    private Set<String> getPermSet( OrgUnit orgUnit )
    {
        @SuppressWarnings("unchecked")
        Set<String> permSet = ( Set<String> ) ouCache.get( getKey( PERM_OUS, orgUnit.getContextId() ) );

        if ( permSet == null )
        {
            permSet = loadPermSet( orgUnit );
        }

        return permSet;
    }


    /**
     *
     * @param orgUnit will be a User OU
     * @return Set containing the OU mapping to the user type and tenant.
     */
    private Set<String> getUserSet( OrgUnit orgUnit )
    {
        @SuppressWarnings("unchecked")
        Set<String> userSet = ( Set<String> ) ouCache.get( getKey( USER_OUS, orgUnit.getContextId() ) );

        if ( userSet == null )
        {
            userSet = loadUserSet( orgUnit );
        }

        return userSet;
    }


    /**
     * Return a fully populated OrgUnit entity for a given Perm or User orgUnitId.  If matching record not found a
     * SecurityException will be thrown.
     *
     * @param entity contains full orgUnit name used for User or Perm OU data sets in directory.
     * @return OrgUnit entity containing all attributes associated with ou in directory.
     * @throws SecurityException in the event OrgUnit not found or DAO search error.
     */
    OrgUnit read( OrgUnit entity ) throws SecurityException
    {
        validate( entity, false );

        return oDao.findByKey( entity );
    }


    /**
     * Will search either User or Perm OrgUnit data sets depending on which type is passed.
     * The search string that contains full or partial OrgUnit name associated with OU node in directory.
     *
     * @param orgUnit contains full or partial OU name.
     * @return List of type OrgUnit containing fully populated matching OU entities.  If no records found this will be empty.
     * @throws SecurityException in the event of DAO search error.
     */
    List<OrgUnit> search( OrgUnit orgUnit ) throws SecurityException
    {
        // Call the finder.
        return oDao.findOrgs( orgUnit );
    }


    /**
     * Adds a new OrgUnit to directory. The OrgUnit type enum will determine which data set insertion will
     * occur - User or Perm.  The OrgUnit entity input will be validated to ensure that:
     * orgUnit name is present and type is specified, and reasonability checks on all of the other populated values.
     *
     * @param entity OrgUnit contains data targeted for insertion.
     * @return OrgUnit entity copy of input + additional attributes (internalId) that were added by op.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    OrgUnit add( OrgUnit entity ) throws SecurityException
    {
        validate( entity, false );
        OrgUnit oe = oDao.create( entity );

        if ( entity.getType() == OrgUnit.Type.USER )
        {
            try
            {
                userPoolLock.writeLock().lock();

                Set<String> userPool = getUserSet( entity );

                if ( userPool != null )
                {
                    userPool.add( entity.getName() );
                }
            }
            finally
            {
                userPoolLock.writeLock().unlock();
            }
        }
        else
        {
            try
            {
                permPoolLock.writeLock().lock();

                Set<String> permPool = getPermSet( entity );

                if ( permPool != null )
                {
                    permPool.add( entity.getName() );
                }
            }
            finally
            {
                permPoolLock.writeLock().unlock();
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
    OrgUnit update( OrgUnit entity ) throws SecurityException
    {
        validate( entity, false );

        return oDao.update( entity );
    }


    /**
     * Remove the parent attribute from the OrgUnit entry in directory. The OrgUnit type enum will determine which data set insertion will
     * occur - User or Perm.  The OrgUnit entity input will be validated to ensure that:
     * orgUnit name is present.
     *
     * @param entity OrgUnit contains data targeted for updating.  Null attributes ignored.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    void deleteParent( OrgUnit entity ) throws SecurityException
    {
        validate( entity, false );
        oDao.deleteParent( entity );
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
    OrgUnit delete( OrgUnit entity ) throws SecurityException
    {
        oDao.remove( entity );

        if ( entity.getType() == OrgUnit.Type.USER )
        {
            try
            {
                userPoolLock.writeLock().lock();
                Set<String> userPool = getUserSet( entity );

                if ( userPool != null )
                {
                    userPool.remove( entity.getName() );
                }
            }
            finally
            {
                userPoolLock.writeLock().unlock();
            }
        }
        else
        {
            try
            {
                permPoolLock.writeLock().lock();
                Set<String> permPool = getPermSet( entity );

                if ( permPool != null )
                {
                    permPool.remove( entity.getName() );
                }
            }
            finally
            {
                permPoolLock.writeLock().unlock();
            }
        }

        return entity;
    }


    /**
     * Return all OrgUnits that have a parent assignment.  This used for hierarchical processing.
     *
     * @param orgUnit will either be a User or Perm OU.
     * @return List of type OrgUnit containing {@link OrgUnit#name} and {@link OrgUnit#parents} populated.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Graphable> getAllDescendants( OrgUnit orgUnit ) throws SecurityException
    {
        return oDao.getAllDescendants( orgUnit );
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
    private void validate( OrgUnit entity, boolean isUpdate )
        throws SecurityException
    {
        VUtil.safeText( entity.getName(), GlobalIds.OU_LEN );

        if ( StringUtils.isNotEmpty( entity.getDescription() ) )
        {
            VUtil.description( entity.getDescription() );
        }

        if ( entity.getType() == null )
        {
            String error = "validate null or empty org unit type";
            throw new SecurityException( GlobalErrIds.ORG_TYPE_NULL, error );
        }
    }


    /**
     * Build a key that is composed of the OU type ({@link #USER_OUS} or {@link #PERM_OUS}) and the contextId which is the id of tenant.
     *
     * @param type either {@link #USER_OUS} or {@link #PERM_OUS}.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return key mapping to this tenant's cache entry.
     */
    private static String getKey( String type, String contextId )
    {
        String key = type;

        if ( StringUtils.isNotEmpty( contextId ) && !contextId.equalsIgnoreCase( GlobalIds.NULL ) )
        {
            key += ":" + contextId;
        }

        return key;
    }
}