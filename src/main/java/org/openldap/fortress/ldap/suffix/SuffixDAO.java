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

import org.openldap.fortress.CreateException;
import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.RemoveException;
import org.openldap.fortress.ldap.UnboundIdDataProvider;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import org.openldap.fortress.util.attr.VUtil;


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
 * This class is thread safe.
 *
 * @author Shawn McKinney
 */
final class SuffixDAO extends UnboundIdDataProvider
{
    private static final String CLS_NM = SuffixDAO.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final String DC = "dc";
    private static final String O = "o";
    private static final String[] SUFFIX_OBJ_CLASS =
        {
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
     * @throws org.openldap.fortress.CreateException
     */
    final void create( Suffix se )
        throws org.openldap.fortress.CreateException
    {
        LDAPConnection ld = null;
        String nodeDn = getDn( se );
        try
        {
            LOG.info( "create suffix dn [" + nodeDn + "]" );
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add( createAttributes( GlobalIds.OBJECT_CLASS, SUFFIX_OBJ_CLASS ) );
            attrs.add( createAttribute( DC, se.getName() ) );
            attrs.add( createAttribute( O, se.getDescription() ) );
            LDAPEntry myEntry = new LDAPEntry( nodeDn, attrs );
            ld = getAdminConnection();
            add( ld, myEntry );
        }
        catch ( LDAPException e )
        {
            String error = "create container node dn [" + nodeDn + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new CreateException( GlobalErrIds.SUFX_CREATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
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
     * @throws org.openldap.fortress.RemoveException
     */
    final void remove( Suffix se )
        throws org.openldap.fortress.RemoveException
    {
        LDAPConnection ld = null;
        String nodeDn = getDn( se );
        LOG.info( "remove suffix dn [" + nodeDn + "]" );
        try
        {
            ld = getAdminConnection();
            deleteRecursive( ld, nodeDn );
        }
        catch ( LDAPException e )
        {
            String error = "remove suffix node dn [" + nodeDn + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new RemoveException( GlobalErrIds.SUFX_DELETE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     *
     * @param se
     * @return
     */
    private String getDn( Suffix se )
    {
        String dn = DC + "=" + se.getName() + "," + DC + "=" + se.getDc();
        // only use this domain component variable if it has been set in the build.properties file:
        if( VUtil.isNotNullOrEmpty( se.getDc2() ) && !se.getDc2().equals( "${suffix.dc2}" ))
        {
            dn += "," + DC + "=" + se.getDc2();
        }
        LOG.debug( "suffix=" + dn );
        return dn;
    }
}
