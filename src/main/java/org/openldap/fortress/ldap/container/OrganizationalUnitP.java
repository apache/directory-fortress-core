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

package org.openldap.fortress.ldap.container;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.SecurityException;
import org.openldap.fortress.ValidationException;
import org.openldap.fortress.util.attr.VUtil;


/**
 * Process module for the container node used for grouping  related nodes within Fortress directory structure. The organizational unit represents
 * the middle nodes that act as containers for other nodes, i.e. ou=People container which groups Users.
 * The organizational unit data is passed using {@link OrganizationalUnit} class.  This class does perform simple data validations.
 * The {@link org.openldap.fortress.ant.FortressAntTask#addContainers()} method calls the {@link #add} from this class during initial base loads.
 * Removal {@link org.openldap.fortress.ant.FortressAntTask#deleteContainers()} is performed during regression tests and should never
 * be executed targeting enabled production directory system datasets.<BR>
 * This class will accept {@link OrganizationalUnit}, and forward on to it's corresponding DAO class {@link OrganizationalUnitDAO} for add/delete of container.
 * <p>
 * Class will throw {@link SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions (
 * {@link org.openldap.fortress.CreateException},,{@link org.openldap.fortress.RemoveException}),
 *  or {@link org.openldap.fortress.ValidationException} as {@link org.openldap.fortress.SecurityException}s with appropriate
 *  error id from {@link org.openldap.fortress.GlobalErrIds}.
 * <p>
 * <font size="3" color="red">
 * The {@link #delete} method in this class is destructive as it will remove all nodes below the container using recursive delete function.<BR>
 * Extreme care should be taken during execution to ensure target dn is correct and permanent removal of data is intended.  There is no
 * 'undo' for this operation.
 * </font>
 * <p/>
 * Simple error mapping is performed in {@link #validate} class.
 * <p/>
 * This class is thread safe.
 *
 * @author Shawn McKinney
 */
public class OrganizationalUnitP
{
    private static final String CLS_NM = OrganizationalUnitP.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    /**
     * Add a new container to the Directory Information Tree (DIT).  After added the
     * node will be inserted after suffix, i.e. ou=NewContainerName, dc=companyName, dc=com.
     *
     * @param orgUnit contains the ou name and description for target node.
     * @throws org.openldap.fortress.SecurityException in the event node already present, {@link GlobalErrIds#CNTR_CREATE_FAILED}, validation, {@link GlobalErrIds#CNTR_NAME_NULL}, {@link org.openldap.fortress.GlobalErrIds#CNTR_NAME_INVLD} or system error.
     */
    public final void add( OrganizationalUnit orgUnit )
        throws SecurityException
    {
        OrganizationalUnitDAO oDao = new OrganizationalUnitDAO();
        oDao.create( orgUnit );
    }


    /**
     * Remove a container from the Directory Information Tree (DIT).  After this operation the
     * node will be removed after suffix.
     *
     * <p>
     * <font size="4" color="red">
     * The {@link #delete} method in this class is destructive as it will remove all nodes below the container using recursive delete function.<BR>
     * Extreme care should be taken during execution to ensure target dn is correct and permanent removal of data is intended.  There is no
     * 'undo' for this operation.
     * </font>
     * <p/>
     *
     * @param orgUnit contains the ou name of container targeted for removal.
     * @throws org.openldap.fortress.SecurityException in the event node not present, {@link org.openldap.fortress.GlobalErrIds#CNTR_DELETE_FAILED}, validation, {@link org.openldap.fortress.GlobalErrIds#CNTR_NAME_NULL}, {@link org.openldap.fortress.GlobalErrIds#CNTR_NAME_INVLD} or system error.
     */
    public final void delete( OrganizationalUnit orgUnit )
        throws SecurityException
    {
        OrganizationalUnitDAO oDao = new OrganizationalUnitDAO();
        oDao.remove( orgUnit );
    }


    /**
     * Method will perform simple validations to ensure the integrity of the {@link OrganizationalUnit} entity targeted for insertion
     * or deletion in directory.
     *
     * @param entity contains the enum type to validate
     * @throws SecurityException thrown in the event the attribute is null.
     */
    private void validate( OrganizationalUnit entity )
        throws SecurityException
    {
        if ( entity.getName().length() > GlobalIds.OU_LEN )
        {
            String name = entity.getName();
            String error = "validate name [" + name + "] invalid length [" + entity.getName().length() + "]";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.CNTR_NAME_INVLD, error );
        }
        if ( !VUtil.isNotNullOrEmpty( entity.getName() ) )
        {
            String error = "validate name validation failed, null or empty value";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.CNTR_NAME_NULL, error );
        }
        if ( entity.getParent().length() > GlobalIds.OU_LEN )
        {
            String name = entity.getName();
            String error = "validate parent [" + name + "] invalid length [" + entity.getName().length()
                + "]";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.CNTR_PARENT_INVLD, error );
        }
        if ( !VUtil.isNotNullOrEmpty( entity.getParent() ) )
        {
            String error = "validate parent validation failed, null or empty value";
            LOG.warn( error );
            throw new org.openldap.fortress.ValidationException( GlobalErrIds.CNTR_PARENT_NULL, error );
        }
        VUtil.safeText( entity.getDescription(), GlobalIds.DESC_LEN );
        if ( VUtil.isNotNullOrEmpty( entity.getDescription() ) )
        {
            VUtil.description( entity.getDescription() );
        }
    }
}
