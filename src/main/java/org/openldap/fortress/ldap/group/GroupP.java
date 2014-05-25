/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */

package org.openldap.fortress.ldap.group;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.SecurityException;
import org.openldap.fortress.util.attr.VUtil;

import java.util.List;
import java.util.Properties;


/**
 * Process module for the group node of Fortress directory structure.
 * This class is thread safe.
 *
 * @author Shawn McKinney
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
     * @throws org.openldap.fortress.SecurityException
     *          in event of validation or system error.
     */
    final Group add( Group group ) throws org.openldap.fortress.SecurityException
    {
        validate( group );
        return gDao.create( group );
    }

    /**
     * Modify a group node within the Directory Information Tree (DIT).
     *
     * @param group contains the group entity for target node.
     * @throws org.openldap.fortress.SecurityException
     *          in event of validation or system error.
     */
    final Group update( Group group ) throws org.openldap.fortress.SecurityException
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
    final Group delete( Group group ) throws org.openldap.fortress.SecurityException
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
     * @throws org.openldap.fortress.SecurityException
     *
     */
    final Group add( Group group, String key, String value ) throws org.openldap.fortress.SecurityException
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
     * @throws org.openldap.fortress.SecurityException
     *
     */
    final Group delete( Group group, String key, String value ) throws org.openldap.fortress.SecurityException
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
    final Group assign( Group entity, String userDn ) throws SecurityException
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
    final Group deassign( Group entity, String userDn ) throws SecurityException
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
    final Group read( Group group ) throws SecurityException
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
    final List<Group> search( Group group ) throws SecurityException
    {
        return gDao.find( group );
    }

    /**
     * Method will perform simple validations to ensure the integrity of the {@link Group} entity targeted for insertion
     * or deletion in directory.
     *
     * @param entity contains the enum type to validate
     * @throws org.openldap.fortress.SecurityException
     *          thrown in the event the attribute is null.
     */
    private void validate( Group entity ) throws SecurityException
    {
        if ( !VUtil.isNotNullOrEmpty( entity.getName() ) )
        {
            String error = "validate name validation failed, null or empty value";
            LOG.warn( error );
            throw new org.openldap.fortress.ValidationException( GlobalErrIds.GROUP_NAME_NULL, error );
        }
        if ( entity.getName().length() > GlobalIds.OU_LEN )
        {
            String name = entity.getName();
            String error = "validate name [" + name + "] invalid length [" + entity.getName().length() + "]";
            LOG.warn( error );
            throw new org.openldap.fortress.ValidationException( GlobalErrIds.GROUP_NAME_INVLD, error );
        }
        if ( entity.getProtocol().length() > GlobalIds.OU_LEN )
        {
            String error = "validate protocol [" + entity.getProtocol() + "] invalid length [" + entity.getProtocol()
                .length() + "]";
            LOG.warn( error );
            throw new org.openldap.fortress.ValidationException( GlobalErrIds.GROUP_PROTOCOL_INVLD, error );
        }
        if ( VUtil.isNotNullOrEmpty( entity.getDescription() ) )
        {
            VUtil.description( entity.getDescription() );
        }
    }
}