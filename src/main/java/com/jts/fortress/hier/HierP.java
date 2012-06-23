/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.hier;

import com.jts.fortress.FinderException;
import com.jts.fortress.SecurityException;
import com.jts.fortress.ValidationException;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.util.attr.VUtil;

import org.apache.log4j.Logger;


/**
 * Process module for Fortress hierarchical processing. The data sets affected include RBAC {@link com.jts.fortress.rbac.Role} and ARBAC {@link com.jts.fortress.arbac.AdminRole},{@link com.jts.fortress.arbac.OrgUnit}.
 * Each entry in the Hier data set contains a parent-child pairing that is read into graph using {@link HierUtil}.
 * The type of Hier entity is set via {@link com.jts.fortress.hier.Hier.Type} which must be equal to one of the following: {@link com.jts.fortress.hier.Hier.Type#ROLE},{@link com.jts.fortress.hier.Hier.Type#AROLE}, {@link com.jts.fortress.hier.Hier.Type#USER}, {@link com.jts.fortress.hier.Hier.Type#PERM}.
 * This class performs data validations and error mapping in addition to calling DAO methods.  It is called by internal Fortress Hierarchical utility classes,
 * {@link HierUtil},{@link RoleUtil},{@link AdminRoleUtil},{@link com.jts.fortress.arbac.UsoUtil},{@link PsoUtil}
 * <p>
 * This class will accept Fortress entity, {@link com.jts.fortress.hier.Hier}, validate its contents and forward to DAO class {@link HierDAO}.
 * <p>
 * Class will throw {@link SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link com.jts.fortress.FinderException},
 * {@link com.jts.fortress.CreateException},{@link com.jts.fortress.UpdateException},{@link com.jts.fortress.RemoveException}),
 *  or {@link com.jts.fortress.ValidationException} as {@link com.jts.fortress.SecurityException}s with appropriate
 * error id from {@link com.jts.fortress.constants.GlobalErrIds}.
 * <p>
 * This class is thread safe but should not be called by external programs.
 * <p/>

 *
 * @author Shawn McKinney
 * @created June 13, 2010
 */
public class HierP
{
    /**
     * Description of the Field
     */
    private static final String CLS_NM = HierP.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);
    private static final HierDAO hDao = new HierDAO();

    /**
     * Package private constructor
     */
    HierP()
    {

    }

    /**
     * Return a fully populated Hier entity for a given type.  If matching record not found a SecurityException will be thrown.
     *
     * @param type contains {@link com.jts.fortress.hier.Hier.Type#ROLE}, {@link com.jts.fortress.hier.Hier.Type#AROLE}, {@link com.jts.fortress.hier.Hier.Type#USER}, {@link com.jts.fortress.hier.Hier.Type#PERM} which points to the target Hier data set.
     * @return Hier entity containing all attributes associated with Hier data set in directory.
     * @throws FinderException in the event DAO search error.
     * @throws com.jts.fortress.ValidationException in the event input data fails validate checks.
     */
    final Hier read(Hier.Type type)
        throws FinderException, ValidationException
    {
        VUtil.assertNotNull(type, GlobalErrIds.HIER_TYPE_NULL, CLS_NM + ".read type=" + type);
        return hDao.getHier(type);
    }

    /**
     * Adds a new Hierarchy node to directory. The Hier type enum will determine which type of data set insertion will
     * occur - {@link com.jts.fortress.hier.Hier.Type#ROLE}, {@link com.jts.fortress.hier.Hier.Type#AROLE}, {@link com.jts.fortress.hier.Hier.Type#USER}, {@link com.jts.fortress.hier.Hier.Type#PERM}.  The Hier entity input will be validated to ensure that:
     * type is present and type is specified.
     *
     * @param entity Hier contains data targeted for insertion.
     * @return Hier entity copy of input + additional attributes (internalId) that were added by op.
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    final Hier add(Hier entity)
        throws SecurityException
    {
        validate("add", entity);
        return hDao.create(entity);
    }


    /**
     * Updates existing Hier data set node in directory. The Type enum will determine which data set insertion will
     * occur {@link com.jts.fortress.hier.Hier.Type#ROLE}, {@link com.jts.fortress.hier.Hier.Type#AROLE}, {@link com.jts.fortress.hier.Hier.Type#USER}, {@link com.jts.fortress.hier.Hier.Type#PERM}.
     * The Hier entity input will be validated to ensure that type is present.  Other non-required attributes may be null or empty and will be ignored by DAO.
     *
     * @param entity Hier contains data targeted for updating.  Null attributes ignored.
     * @param op     specified what type of Hier update - {@link com.jts.fortress.hier.Hier.Op#ADD},{@link com.jts.fortress.hier.Hier.Op#MOD},{@link com.jts.fortress.hier.Hier.Op#REM}
     * @return Hier entity copy of input + additional attributes (internalId) that were updated by op.
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    final Hier update(Hier entity, Hier.Op op)
        throws SecurityException
    {
        validate("update", entity);
        return hDao.update(entity, op);
    }


    /**
     * This method performs a "hard" delete.  It completely the Hier node from the ldap directory.
     * The SDSet type enum will determine where deletion will occur - {@link com.jts.fortress.hier.Hier.Type#ROLE}, {@link com.jts.fortress.hier.Hier.Type#AROLE}, {@link com.jts.fortress.hier.Hier.Type#USER}, {@link com.jts.fortress.hier.Hier.Type#PERM} data sets.
     * Hier entity must exist in directory prior to making this call else exception will be thrown.
     *
     * @param hier Contains the type name of the SDSet node targeted for deletion - {@link com.jts.fortress.hier.Hier.Type#ROLE}, {@link com.jts.fortress.hier.Hier.Type#AROLE}, {@link com.jts.fortress.hier.Hier.Type#USER}, {@link com.jts.fortress.hier.Hier.Type#PERM}.
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    final void delete(Hier hier)
        throws SecurityException
    {
        VUtil.assertNotNull(hier.getType(), GlobalErrIds.HIER_TYPE_NULL, CLS_NM + ".delete type");
        hDao.remove(hier);
    }


    /**
     * Method will perform simple validations to ensure the integrity of the Hier entity targeted for insertion
     * or updating in directory.  This method will ensure the type enum are specified.
     *
     * @param function specifies method name - either "add" or "update".
     * @param entity   contains the enum type to validate
     * @throws com.jts.fortress.SecurityException thrown in the event the attribute is null.
     */
    private void validate(String function, Hier entity)
        throws SecurityException
    {
        VUtil.assertNotNull(entity, GlobalErrIds.HIER_NULL, CLS_NM + ".validate function: " + function);
        VUtil.assertNotNull(entity.getType(), GlobalErrIds.HIER_TYPE_NULL, CLS_NM + ".validate type function: " + function);
    }
}

