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

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.SDSet;
import org.apache.directory.fortress.core.util.VUtil;


/**
 * Process module for Separation of Duty Relation data sets. The Fortress SD data set can be of two types:
 * <ol>
 *   <li>Static Separation of Duties (SSD)</li>
 *   <li>Dynamic Separation of Duties (DSD)</li>
 * </ol>
 * The SDSet entity itself distinguishes which is being targeted by {@link org.apache.directory.fortress.core.model.SDSet.SDType} which is equal to {@link org.apache.directory.fortress.core.model.SDSet.SDType#STATIC} or {@link org.apache.directory.fortress.core.model.SDSet.SDType#DYNAMIC}.
 * This class performs data validations and error mapping in addition to calling DAO methods.  It is typically called
 * by internal Fortress Manager classes ({@link org.apache.directory.fortress.core.AdminMgr}, {@link org.apache.directory.fortress.core.ReviewMgr}) and also by internal SD utils.
 * This class is not intended to be called externally or outside of Fortress Core itself.  This class will accept {@link org.apache.directory.fortress.core.model.SDSet},
 * validate its contents and forward on to it's corresponding DAO {@link org.apache.directory.fortress.core.impl.SdDAO}.
 * <p>
 * Class will throw {@link SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link org.apache.directory.fortress.core.FinderException},
 * {@link org.apache.directory.fortress.core.CreateException},{@link org.apache.directory.fortress.core.UpdateException},{@link org.apache.directory.fortress.core.RemoveException}),
 * or {@link org.apache.directory.fortress.core.ValidationException} as {@link SecurityException}s with appropriate
 * error id from {@link org.apache.directory.fortress.core.GlobalErrIds}.
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class SdP
{
    /**
     * Get the DAO created:
     */
    private SdDAO sdDao = new SdDAO();


    /**
     * Adds a new SDSet to directory. The OrgUnit SDType enum will determine which data set insertion will
     * occur - STATIC or DYNAMIC.  The SDSet entity input will be validated to ensure that:
     * name is present, and reasonability checks on all of the other populated values.
     *
     * @param entity SDSet contains data targeted for insertion.
     * @return SDSet entity copy of input + additional attributes (internalId) that were added by op.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    SDSet add( SDSet entity ) throws SecurityException
    {
        validate( entity );
        return sdDao.create( entity );
    }


    /**
     * Updates existing SDSet in directory. The SDSet type enum will determine which data set insertion will
     * occur - STATIC or DYNAMIC.  The SDSet entity input will be validated to ensure that:
     * name is present, and reasonability checks on all of the other populated values.
     * Null or empty attributes are ignored.
     *
     * @param entity SDSet contains data targeted for updating.  Null attributes ignored.
     * @return SDSet entity copy of input + additional attributes (internalId) that were updated by op.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    SDSet update( SDSet entity ) throws SecurityException
    {
        validate( entity );
        return sdDao.update( entity );
    }


    /**
     * This method performs a "hard" delete.  It completely the SDSet node from the ldap directory.
     * The SDSet type enum will determine where deletion will occur - STATIC or DYNAMIC data sets.
     * SDSet entity must exist in directory prior to making this call else exception will be thrown.
     *
     * @param entity Contains the name of the SDSet node targeted for deletion.
     * @return SDSet is a copy of entity.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    SDSet delete( SDSet entity ) throws SecurityException
    {
        return sdDao.remove( entity );
    }


    /**
     * Return a fully populated SDSet entity for a given STATIC or DYNAMIC SDSet name.  If matching record not found a
     * SecurityException will be thrown.
     *
     * @param entity contains full SDSet name used for STATIC or DYNAMIC data sets in directory.
     * @return SDSet entity containing all attributes associated with ou in directory.
     * @throws SecurityException in the event SDSet not found or DAO search error.
     */
    SDSet read( SDSet entity ) throws SecurityException
    {
        SDSet sde;
        // The assumption is this method is called from ReviewMgr.ssdRoleSetRoles or ReviewMgr.dsdRoleSetRoles.
        // If called from ReviewMgr, the object class type will be passed in:
        SDSet.SDType type = entity.getType();
        sde = sdDao.getSD( entity );
        // Load the previously saved type onto the return entity:
        sde.setType( type );
        return sde;
    }


    /**
     * Will search using a single RBAC Role name either STATIC or DYNAMIC SDSet depending on which type is passed.
     * The role entity contains full RBAC Role name associated with SDSet node in directory.
     *
     * @param sdSet contains sdset name or partial name along with sdset type of STATIC or DYNAMIC.
     * @return List of SDSet entities found.
     * @throws SecurityException in the event of DAO search error.
     */
    List<SDSet> search( SDSet sdSet ) throws SecurityException
    {
        return sdDao.search( sdSet );
    }


    /**
     * Will search using a single RBAC Role name either STATIC or DYNAMIC SDSet depending on which type is passed.
     * The role entity contains full RBAC Role name associated with SDSet node in directory.
     *
     * @param role contains full role name associated with SDSet.
     * @param type either STATIC or DYNAMIC depending on target search data set.
     * @return List of SDSet entities found.
     * @throws SecurityException in the event of DAO search error.
     */
    List<SDSet> search( Role role, SDSet.SDType type ) throws SecurityException
    {
        return sdDao.search( role, type );
    }


    /**
     * Will search using list of input RBAC role names either STATIC or DYNAMIC SDSet depending on which type is passed.
     * The role entity contains full RBAC Role name associated with SDSet node in directory.
     *
     * @param rls  contains set of type String containing full role names associated with SDSet.
     * @param sdSet contains type either STATIC or DYNAMIC depending on target search data set.
     * @return List of SDSet entities found.
     * @throws SecurityException in the event of DAO search error.
     */
    Set<SDSet> search( Set<String> rls, SDSet sdSet ) throws SecurityException
    {
        return sdDao.search( rls, sdSet );
    }


    /**
     * Method will perform simple validations to ensure the integrity of the SDSet entity targeted for insertion
     * or updating in directory.  This method will ensure the name and type enum are specified.  Method will
     * also ensure every Role name set is valid RBAC role entity in directory.  It will also perform
     * reasonability check on description if set.
     *
     * @param entity contains the enum type to validate
     * @throws SecurityException thrown in the event the attribute is null.
     */
    private void validate( SDSet entity )
        throws SecurityException
    {
        // TODO: Add more validations here:
        VUtil.safeText( entity.getName(), GlobalIds.OU_LEN );
        if ( StringUtils.isNotEmpty( entity.getDescription() ) )
        {
            VUtil.description( entity.getDescription() );
        }
        Set<String> roles = entity.getMembers();
        if ( roles != null )
        {
            RoleP rp = new RoleP();
            for ( String key : roles )
            {
                // when removing last role member a placeholder must be left in data set:
                if ( !key.equalsIgnoreCase( GlobalIds.NONE ) )
                {
                    // Ensure the name exists:
                    Role role = new Role( key );
                    role.setContextId( entity.getContextId() );
                    rp.read( role );
                }
            }
        }
    }
}
