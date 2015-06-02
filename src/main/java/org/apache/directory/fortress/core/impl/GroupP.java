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


import org.apache.directory.api.util.Strings;
import org.apache.directory.fortress.core.ValidationException;
import org.apache.directory.fortress.core.model.Group;
import org.apache.directory.fortress.core.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.util.VUtil;

import java.util.List;


/**
 * Process module for the group node of Fortress directory structure.
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class GroupP
{
    private static final String CLS_NM = GroupP.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static GroupDAO gDao = new GroupDAO();


    /**
     * Add a group node to the Directory Information Tree (DIT).
     *
     * @param group contains the group entity for target node.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in event of validation or system error.
     */
    Group add( Group group ) throws SecurityException
    {
        validate( group );

        return gDao.create( group );
    }


    /**
     * Modify a group node within the Directory Information Tree (DIT).
     *
     * @param group contains the group entity for target node.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in event of validation or system error.
     */
    Group update( Group group ) throws SecurityException
    {
        validate( group );

        return gDao.update( group );
    }


    /**
     * Remove the group node.
     *
     * @param group contains the group entity for target node.
     * @throws SecurityException in event of validation or system error.
     */
    Group delete( Group group ) throws SecurityException
    {
        return gDao.remove( group );
    }


    /**
     * Add a new property to an existing Group
     *
     * @param group
     * @param key
     * @param value
     * @return
     * @throws org.apache.directory.fortress.core.SecurityException
     *
     */
    Group add( Group group, String key, String value ) throws SecurityException
    {
        return gDao.add( group, key, value );
    }


    /**
     * Remove an existing property value from an existing Group
     *
     * @param group
     * @param key
     * @param value
     * @return
     * @throws org.apache.directory.fortress.core.SecurityException
     *
     */
    Group delete( Group group, String key, String value ) throws SecurityException
    {
        return gDao.delete( group, key, value );
    }


    /**
     * Method will add the "member" attribute on LDAP entry which represents a Group assignment.
     *
     * @param entity contains the group name targeted.
     * @param userDn String contains the dn for the user entry that is being assigned the RBAC Role.
     * @return Group containing copy of input data.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    Group assign( Group entity, String userDn ) throws SecurityException
    {
        return gDao.assign( entity, userDn );
    }


    /**
     * Method will remove the "member" attribute on LDAP entry which represents a Group assignment.
     *
     * @param entity contains the role name targeted.
     * @param userDn String contains the dn for the user entry that is being assigned the RBAC Role.
     * @return Role containing copy of input data.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    Group deassign( Group entity, String userDn ) throws SecurityException
    {
        return gDao.deassign( entity, userDn );
    }


    /**
     * Return a fully populated Group entity for a given name.  If matching record not found a
     * SecurityException will be thrown.
     *
     * @param group contains full group name for entry in directory.
     * @return Group entity containing all attributes associated.
     * @throws SecurityException in the event not found or DAO search error.
     */
    Group read( Group group ) throws SecurityException
    {
        return gDao.get( group );
    }


    /**
     * Takes a search string that contains full or partial Group name in directory.
     *
     * @param group contains full or partial name.
     * @return List of type Group containing fully populated matching entities.  If no records found this will be empty.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Group> search( Group group ) throws SecurityException
    {
        return gDao.find( group );
    }


    /**
     * Takes a search string that contains full or partial Group name in directory.
     *
     * @param user contains full dn for existing user.
     * @return List of type Group containing fully populated matching entities.  If no records found this will be empty.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Group> search( User user ) throws SecurityException
    {
        return gDao.find( user );
    }


    /**
     * Method will perform simple validations to ensure the integrity of the {@link Group} entity targeted for insertion
     * or deletion in directory.
     *
     * @param entity contains the enum type to validate
     * @throws org.apache.directory.fortress.core.SecurityException
     *          thrown in the event the attribute is null.
     */
    private void validate( Group entity ) throws SecurityException
    {
        if ( Strings.isEmpty( entity.getName() ) )
        {
            String error = "validate name validation failed, null or empty value";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.GROUP_NAME_NULL, error );
        }

        if ( entity.getName().length() > GlobalIds.OU_LEN )
        {
            String name = entity.getName();
            String error = "validate name [" + name + "] invalid length [" + entity.getName().length() + "]";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.GROUP_NAME_INVLD, error );
        }

        if ( entity.getProtocol().length() > GlobalIds.OU_LEN )
        {
            String error = "validate protocol [" + entity.getProtocol() + "] invalid length [" + entity.getProtocol()
                .length() + "]";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.GROUP_PROTOCOL_INVLD, error );
        }

        if ( !Strings.isEmpty( entity.getDescription() ) )
        {
            VUtil.description( entity.getDescription() );
        }
    }
}