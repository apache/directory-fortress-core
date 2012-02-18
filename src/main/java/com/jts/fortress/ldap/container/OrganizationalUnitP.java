/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ldap.container;

import com.jts.fortress.SecurityException;
import com.jts.fortress.ValidationException;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.attr.VUtil;
import org.apache.log4j.Logger;


/**
 * Process module for the container node used for grouping  related nodes within Fortress directory structure. The organizational unit represents
 * the middle nodes that act as containers for other nodes, i.e. ou=People container which groups Users.
 * The organizational unit data is passed using {@link OrganizationalUnit} class.  This class does perform simple data validations.
 * The {@link com.jts.fortress.ant.FortressAntTask#addContainers()} method calls the {@link #add} from this class during initial base loads.
 * Removal {@link com.jts.fortress.ant.FortressAntTask#deleteContainers()} is performed during regression tests and should never
 * be executed targeting enabled production directory system datasets.<BR>
 * This class will accept {@link OrganizationalUnit}, and forward on to it's corresponding DAO class {@link OrganizationalUnitDAO} for add/delete of container.
 * <p>
 * Class will throw {@link SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions (
 * {@link com.jts.fortress.CreateException},,{@link com.jts.fortress.RemoveException}),
 *  or {@link com.jts.fortress.ValidationException} as {@link com.jts.fortress.SecurityException}s with appropriate
 *  error id from {@link com.jts.fortress.constants.GlobalErrIds}.
 * <p>
 * <font size="3" color="red">
 * The {@link #delete} method in this class is destructive as it will remove all nodes below the container using recursive delete function.<BR>
 * Extreme care should be taken during execution to ensure target dn is correct and permanent removal of data is intended.  There is no
 * 'undo' for this operation.
 * </font>
 * <p/>
 * Simple error mapping is performed in {@link #validate} class.
 * <BR><BR>
 * <p/>

 *
 * @author smckinn
 * @created January 21, 2010
 */
public class OrganizationalUnitP
{
    private static final String OCLS_NM = OrganizationalUnitP.class.getName();
    private static final Logger log = Logger.getLogger(OCLS_NM);

    /**
     * Add a new container to the Directory Information Tree (DIT).  After added the
     * node will be inserted after suffix, i.e. ou=NewContainerName, dc=companyName, dc=com.
     *
     * @param orgUnit contains the ou name and description for target node.
     * @throws com.jts.fortress.SecurityException in the event node already present, {@link GlobalErrIds#CNTR_CREATE_FAILED}, validation, {@link GlobalErrIds#CNTR_NAME_NULL}, {@link com.jts.fortress.constants.GlobalErrIds#CNTR_NAME_INVLD} or system error.
     */
    public final void add(OrganizationalUnit orgUnit)
        throws SecurityException
    {
        OrganizationalUnitDAO oDao = new OrganizationalUnitDAO();
        oDao.create(orgUnit);
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
     * @throws com.jts.fortress.SecurityException in the event node not present, {@link com.jts.fortress.constants.GlobalErrIds#CNTR_DELETE_FAILED}, validation, {@link com.jts.fortress.constants.GlobalErrIds#CNTR_NAME_NULL}, {@link com.jts.fortress.constants.GlobalErrIds#CNTR_NAME_INVLD} or system error.
     */
    public final void delete(OrganizationalUnit orgUnit)
        throws SecurityException
    {
        OrganizationalUnitDAO oDao = new OrganizationalUnitDAO();
        oDao.remove(orgUnit);
    }

    /**
     * Method will perform simple validations to ensure the integrity of the {@link OrganizationalUnit} entity targeted for insertion
     * or deletion in directory.
     *
     * @param entity contains the enum type to validate
     * @throws SecurityException thrown in the event the attribute is null.
     */
    private void validate(OrganizationalUnit entity)
        throws SecurityException
    {
        if (entity.getName().length() > GlobalIds.OU_LEN)
        {
            String name = entity.getName();
            String error = OCLS_NM + ".validate name [" + name + "] invalid length [" + entity.getName().length() + "]";
            log.warn(error);
            throw new ValidationException(GlobalErrIds.CNTR_NAME_INVLD, error);
        }
        if (!VUtil.isNotNullOrEmpty(entity.getName()))
        {
            String error = OCLS_NM + ".validate name validation failed, null or empty value";
            log.warn(error);
            throw new ValidationException(GlobalErrIds.CNTR_NAME_NULL, error);
        }
        if (entity.getParent().length() > GlobalIds.OU_LEN)
        {
            String name = entity.getName();
            String error = OCLS_NM + ".validate parent [" + name + "] invalid length [" + entity.getName().length() + "]";
            log.warn(error);
            throw new ValidationException(GlobalErrIds.CNTR_PARENT_INVLD, error);
        }
        if (!VUtil.isNotNullOrEmpty(entity.getParent()))
        {
            String error = OCLS_NM + ".validate parent validation failed, null or empty value";
            log.warn(error);
            throw new com.jts.fortress.ValidationException(GlobalErrIds.CNTR_PARENT_NULL, error);
        }
        VUtil.safeText(entity.getDescription(), GlobalIds.DESC_LEN);
        if (VUtil.isNotNullOrEmpty(entity.getDescription()))
        {
            VUtil.description(entity.getDescription());
        }
    }
}

