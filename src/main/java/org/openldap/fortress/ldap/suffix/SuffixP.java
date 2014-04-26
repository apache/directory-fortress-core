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

package org.openldap.fortress.ldap.suffix;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.SecurityException;
import org.openldap.fortress.util.attr.VUtil;


/**
 * Process module for the suffix or root node of Fortress directory structure. The suffix represents the topmost node in a directory information
 * tree.  For example dc=companyName,dc=com.  The suffix data is passed using {@link Suffix} class.  This class does perform simple data validations.
 * The {@link org.openldap.fortress.ant.FortressAntTask#addSuffixes()} method calls the {@link #add} from this class during initial base loads.
 * Removal {@link org.openldap.fortress.ant.FortressAntTask#deleteSuffixes()} is performed during regression tests and should never
 * be executed targeting production directory systems.<BR>
 * This class will accept {@link Suffix}, and forward on to it's corresponding DAO class {@link SuffixDAO} for add/delete of suffix.
 * <p>
 * Class will throw {@link org.openldap.fortress.SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link org.openldap.fortress.FinderException},
 * {@link org.openldap.fortress.CreateException},{@link org.openldap.fortress.UpdateException},{@link org.openldap.fortress.RemoveException}),
 *  or {@link org.openldap.fortress.ValidationException} as {@link org.openldap.fortress.SecurityException}s with appropriate
 * error id from {@link GlobalErrIds}.
 * <p>
 * <font size="3" color="red">
 * The {@link #delete} method in this class is destructive as it will remove all nodes below the suffix using recursive delete function.<BR>
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
public class SuffixP
{
    private static final String CLS_NM = SuffixP.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    /**
     * Add a new suffix to the Directory Information Tree (DIT).  After added the
     * node will be listed in domain component format, i.e. dc=companyName, dc=com, or dc=orgName, dc=org.
     *
     * @param suffix contains the dc name and top level dc for target node.
     * @throws org.openldap.fortress.SecurityException in event of validation or system error.
     */
    public final void add( Suffix suffix )
        throws org.openldap.fortress.SecurityException
    {
        validate( suffix );
        SuffixDAO sDao = new SuffixDAO();
        sDao.create( suffix );
    }


    /**
     * Remove the suffix along with descendant nodes.  This is a destructive method which will remove all DIT nodes under
     * the specified.
     * <p/>
     * <font size="2" color="red">
     * This method is destructive and will remove all nodes below.<BR>
     * Extreme care should be taken during execution to ensure target dn is correct and permanent removal of data is intended.  There is no
     * 'undo' for this operation.
     * </font>
     * <p/>
     *
     * @param suffix contains the dc name and top level dc for target node.
     * @throws SecurityException in event of validation or system error.
     */
    public final void delete( Suffix suffix )
        throws org.openldap.fortress.SecurityException
    {
        validate( suffix );
        SuffixDAO sDao = new SuffixDAO();
        sDao.remove( suffix );
    }


    /**
     * Method will perform simple validations to ensure the integrity of the {@link Suffix} entity targeted for insertion
     * or deletion in directory.
     *
     * @param entity contains the enum type to validate
     * @throws org.openldap.fortress.SecurityException thrown in the event the attribute is null.
     */
    private void validate( Suffix entity )
        throws SecurityException
    {
        if ( entity.getName().length() > GlobalIds.OU_LEN )
        {
            String name = entity.getName();
            String error = "validate name [" + name + "] invalid length [" + entity.getName().length() + "]";
            LOG.warn( error );
            throw new org.openldap.fortress.ValidationException( GlobalErrIds.SUFX_NAME_INVLD, error );
        }
        if ( !VUtil.isNotNullOrEmpty( entity.getName() ) )
        {
            String error = "validate name validation failed, null or empty value";
            LOG.warn( error );
            throw new org.openldap.fortress.ValidationException( GlobalErrIds.SUFX_NAME_NULL, error );
        }
        if ( entity.getDc().length() > GlobalIds.OU_LEN )
        {
            String name = entity.getName();
            String error = "validate dc [" + name + "] invalid length [" + entity.getName().length() + "]";
            LOG.warn( error );
            throw new org.openldap.fortress.ValidationException( GlobalErrIds.SUFX_DCTOP_INVLD, error );
        }
        if ( !VUtil.isNotNullOrEmpty( entity.getDc() ) )
        {
            String error = "validate dc validation failed, null or empty value";
            LOG.warn( error );
            throw new org.openldap.fortress.ValidationException( GlobalErrIds.SUFX_DCTOP_NULL, error );
        }
        VUtil.safeText( entity.getDescription(), GlobalIds.DESC_LEN );
        if ( VUtil.isNotNullOrEmpty( entity.getDescription() ) )
        {
            VUtil.description( entity.getDescription() );
        }
    }
}
