/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ldap.container;

import com.jts.fortress.configuration.Config;
import com.jts.fortress.ldap.DaoUtil;
import com.jts.fortress.ldap.PoolMgr;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.attr.VUtil;
import org.apache.log4j.Logger;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;

/**
 * This class provides data access for the standard ldap object class Organizational Unit.  This
 * entity is used to provide containers in DIT for organization of related nodes..
 * A container node is used to group other related nodes, i.e. 'ou=People' or 'ou'Roles'.
 * <br />The organizational unit object class is 'organizationalUnit' <br />
 * <p/>
 * The OrganizationalUnitDAO maintains the following structural object class:
 * <p/>
 * organizationalUnit Structural Object Class is used to store basic attributes like ou and description.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.5.6.5 NAME 'organizationalUnit'</code>
 * <li> <code>DESC 'RFC2256: an organizational unit'</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST ou</code>
 * <li> <code>MAY ( userPassword $ searchGuide $ seeAlso $ businessCategory $</code>
 * <li> <code>x121Address $ registeredAddress $ destinationIndicator $</code>
 * <li> <code>preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $</code>
 * <li> <code>telephoneNumber $ internationaliSDNNumber $</code>
 * <li> <code>facsimileTelephoneNumber $ street $ postOfficeBox $ postalCode $</code>
 * <li> <code>postalAddress $ physicalDeliveryOfficeName $ st $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>

 *
 * @author Shawn McKinney
 * @created January 21, 2010
 */
public final class OrganizationalUnitDAO
{
    private static final String CLS_NM = OrganizationalUnitDAO.class.getName();
    final private static Logger log = Logger.getLogger(CLS_NM);
    private final static String[] ORG_UNIT_ATRS = {
        GlobalIds.OU, GlobalIds.DESC
    };
    private final static String ORGUNIT_CLASS = "organizationalunit";
    private final static String[] ORGUNIT_OBJ_CLASS = {
        ORGUNIT_CLASS
    };

    /**
     * Package private default constructor.
     */
    OrganizationalUnitDAO()
    {
    }


    /**
     * @param oe
     * @throws com.jts.fortress.CreateException
     */
    void create(OrganizationalUnit oe)
        throws com.jts.fortress.CreateException
    {
        LDAPConnection ld = null;
        //String nodeDn = "ou=" + oe.getName() + "," + Config.getProperty(GlobalIds.SUFFIX);
        String nodeDn = GlobalIds.OU + "=" + oe.getName() + ",";
        if (VUtil.isNotNullOrEmpty(oe.getParent()))
            nodeDn += GlobalIds.OU + "=" + oe.getParent() + ",";
        nodeDn += Config.getProperty(GlobalIds.SUFFIX);
        try
        {
            log.info(CLS_NM + ".create container dn [" + nodeDn + "]");
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add(DaoUtil.createAttributes(GlobalIds.OBJECT_CLASS,
                ORGUNIT_OBJ_CLASS));
            attrs.add(DaoUtil.createAttribute(GlobalIds.OU, oe.getName()));
            attrs.add(DaoUtil.createAttribute(GlobalIds.DESC, oe.getDescription()));
            LDAPEntry myEntry = new LDAPEntry(nodeDn, attrs);
            DaoUtil.add(ld, myEntry);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".create container node dn [" + nodeDn + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new com.jts.fortress.CreateException(GlobalErrIds.CNTR_CREATE_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
    }


    /**
     * @param oe
     * @throws com.jts.fortress.RemoveException
     */
    void remove(OrganizationalUnit oe)
        throws com.jts.fortress.RemoveException
    {
        LDAPConnection ld = null;
        String nodeDn = GlobalIds.OU + "=" + oe.getName() + ",";
        if (VUtil.isNotNullOrEmpty(oe.getParent()))
            nodeDn += GlobalIds.OU + "=" + oe.getParent() + ",";
        nodeDn += Config.getProperty(GlobalIds.SUFFIX);

        log.info(CLS_NM + ".remove container dn [" + nodeDn + "]");
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            DaoUtil.deleteRecursive(ld, nodeDn);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".remove container node dn [" + nodeDn + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new com.jts.fortress.RemoveException(GlobalErrIds.CNTR_DELETE_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
    }
}