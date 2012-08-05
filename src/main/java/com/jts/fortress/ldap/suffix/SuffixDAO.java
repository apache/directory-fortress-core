/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ldap.suffix;

import com.jts.fortress.GlobalErrIds;
import com.jts.fortress.GlobalIds;
import com.jts.fortress.RemoveException;
import com.jts.fortress.ldap.DaoUtil;
import com.jts.fortress.ldap.PoolMgr;
import org.apache.log4j.Logger;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;

/**
 * This class contains the Suffix node for OpenLDAP Directory Information Tree.
 * <br />The domain component object class is 'dcObject' <br />
 * <p/>
 * dcObject Auxiliary Object Class is used to store basic attributes like domain component names and description.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code># RFC 2247</code>
 * <li> <code>objectclass ( 1.3.6.1.4.1.1466.344 NAME 'dcObject'</code>
 * <li> <code>SUP top AUXILIARY MUST dc )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * Following wikipedia excerpt describes usage of this object <a href="http://http://en.wikipedia.org/wiki/LDAP/">Wikipedia LDAP</a>
 * <font size="2" color="blue">
 * <blockquote>
 * <h3>
 * Naming structure
 * </h3>
 * Since an LDAP server can return referrals to other servers for requests the server itself will not/can not serve, a naming structure for LDAP entries is needed so one can find a server holding a given DN. Since such a structure already exists in the Domain name system (DNS), servers' top level names often mimic DNS names, as they do in X.500.
 * If an organization has domain name example.org, its top level LDAP entry will typically have the DN dc=example,dc=org (where dc means domain component). If the LDAP server is also named ldap.example.org, the organization's top level LDAP URL becomes ldap://ldap.example.org/dc=example,dc=org.
 * Below the top level, the entry names will typically reflect the organization's internal structure or needs rather than DNS names.
 * </blockquote>
 * </font>
 * <p/>

 *
 * @author Shawn McKinney
 * @created January 21, 2010
 */
final class SuffixDAO
{
    private static final String CLS_NM = SuffixDAO.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);
    private static final String DC = "dc";
    private static final String O = "o";
    private static final String[] SUFFIX_OBJ_CLASS = {
        GlobalIds.SUFFIX_CLASS, GlobalIds.ORGANIZATION_CLASS
    };

    /**
     * Package private default constructor.
     */
    SuffixDAO()
    {
    }

    /**
     * @param se
     * @throws com.jts.fortress.CreateException
     */
    final void create(Suffix se)
        throws com.jts.fortress.CreateException
    {
        LDAPConnection ld = null;
        String nodeDn = getDn(se);
        try
        {
            log.info(CLS_NM + ".create suffix dn [" + nodeDn + "]");
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add(DaoUtil.createAttributes(GlobalIds.OBJECT_CLASS, SUFFIX_OBJ_CLASS));
            attrs.add(DaoUtil.createAttribute(DC, se.getName()));
            attrs.add(DaoUtil.createAttribute(O, se.getDescription()));
            LDAPEntry myEntry = new LDAPEntry(nodeDn, attrs);
            DaoUtil.add(ld, myEntry);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".create container node dn [" + nodeDn + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new com.jts.fortress.CreateException(GlobalErrIds.SUFX_CREATE_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
    }


    /**
     * <p/>
     * <font size="4" color="red">
     * This method is destructive as it will remove all nodes below the suffix using recursive delete function.<BR>
     * Extreme care should be taken during execution to ensure target directory is correct and permanent removal of data is intended.  There is no
     * 'undo' for this operation.
     * </font>
     * <p/>
     *
     * @param se
     * @throws com.jts.fortress.RemoveException
     */
    final void remove(Suffix se)
        throws com.jts.fortress.RemoveException
    {
        LDAPConnection ld = null;
        String nodeDn = getDn(se);
        log.info(CLS_NM + ".remove suffix dn [" + nodeDn + "]");
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            DaoUtil.deleteRecursive(ld, nodeDn);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".remove suffix node dn [" + nodeDn + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new RemoveException(GlobalErrIds.SUFX_DELETE_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
    }

    /**
     *
     * @param se
     * @return
     */
    private String getDn(Suffix se)
    {
        return DC + "=" + se.getName() + "," + DC + "=" + se.getDc();
    }
}

