/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ldap.suffix;

import com.jts.fortress.SecurityException;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.attr.VUtil;
import org.apache.log4j.Logger;


/**
 * Process module for the suffix or root node of Fortress directory structure. The suffix represents the topmost node in a directory information
 * tree.  For example dc=companyName,dc=com.  The suffix data is passed using {@link Suffix} class.  This class does perform simple data validations.
 * The {@link com.jts.fortress.ant.FortressAntTask#addSuffixes()} method calls the {@link #add} from this class during initial base loads.
 * Removal {@link com.jts.fortress.ant.FortressAntTask#deleteSuffixes()} is performed during regression tests and should never
 * be executed targeting production directory systems.<BR>
 * This class will accept {@link Suffix}, and forward on to it's corresponding DAO class {@link SuffixDAO} for add/delete of suffix.
 * <p>
 * Class will throw {@link com.jts.fortress.SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link com.jts.fortress.FinderException},
 * {@link com.jts.fortress.CreateException},{@link com.jts.fortress.UpdateException},{@link com.jts.fortress.RemoveException}),
 *  or {@link com.jts.fortress.ValidationException} as {@link com.jts.fortress.SecurityException}s with appropriate
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

 * <BR><BR>
 * <PRE>
 * </PRE>
 *
 * @author smckinn
 * @created January 21, 2010
 */
public class SuffixP
{
    private static final String OCLS_NM = SuffixP.class.getName();
    private static final Logger log = Logger.getLogger(OCLS_NM);

    /**
     * Add a new suffix to the Directory Information Tree (DIT).  After added the
     * node will be listed in domain component format, i.e. dc=companyName, dc=com, or dc=orgName, dc=org.
     *
     * @param suffix contains the dc name and top level dc for target node.
     * @throws com.jts.fortress.SecurityException in event of validation or system error.
     */
    public final void add(Suffix suffix)
        throws com.jts.fortress.SecurityException
    {
        validate(suffix);
        SuffixDAO sDao = new SuffixDAO();
        sDao.create(suffix);
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
    public final void delete(Suffix suffix)
        throws com.jts.fortress.SecurityException
    {
        validate(suffix);
        SuffixDAO sDao = new SuffixDAO();
        sDao.remove(suffix);
    }


    /**
     * Method will perform simple validations to ensure the integrity of the {@link Suffix} entity targeted for insertion
     * or deletion in directory.
     *
     * @param entity contains the enum type to validate
     * @throws com.jts.fortress.SecurityException thrown in the event the attribute is null.
     */
    private void validate(Suffix entity)
        throws SecurityException
    {
        if (entity.getName().length() > GlobalIds.OU_LEN)
        {
            String name = entity.getName();
            String error = OCLS_NM + ".validate name [" + name + "] invalid length [" + entity.getName().length() + "]";
            log.warn(error);
            throw new com.jts.fortress.ValidationException(GlobalErrIds.SUFX_NAME_INVLD, error);
        }
        if (!VUtil.isNotNullOrEmpty(entity.getName()))
        {
            String error = OCLS_NM + ".validate name validation failed, null or empty value";
            log.warn(error);
            throw new com.jts.fortress.ValidationException(GlobalErrIds.SUFX_NAME_NULL, error);
        }
        if (entity.getDc().length() > GlobalIds.OU_LEN)
        {
            String name = entity.getName();
            String error = OCLS_NM + ".validate dc [" + name + "] invalid length [" + entity.getName().length() + "]";
            log.warn(error);
            throw new com.jts.fortress.ValidationException(GlobalErrIds.SUFX_DCTOP_INVLD, error);
        }
        if (!VUtil.isNotNullOrEmpty(entity.getDc()))
        {
            String error = OCLS_NM + ".validate dc validation failed, null or empty value";
            log.warn(error);
            throw new com.jts.fortress.ValidationException(GlobalErrIds.SUFX_DCTOP_NULL, error);
        }
        VUtil.safeText(entity.getDescription(), GlobalIds.DESC_LEN);
        if (VUtil.isNotNullOrEmpty(entity.getDescription()))
        {
            VUtil.description(entity.getDescription());
        }
    }
}

