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


import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.openldap.fortress.ldap.ApacheDsDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.util.attr.VUtil;


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
 * This class is thread safe.
 *
 * @author Shawn McKinney
 */
final class OrganizationalUnitDAO extends ApacheDsDataProvider
{
    private static final String CLS_NM = OrganizationalUnitDAO.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final String ORGUNIT_CLASS = "organizationalunit";
    private static final String[] ORGUNIT_OBJ_CLASS =
        {
            ORGUNIT_CLASS
    };


    /**
     * Package private default constructor.
     */
    OrganizationalUnitDAO()
    {
    }


    private String getSdRoot( String contextId )
    {
        return getRootDn( contextId, GlobalIds.SUFFIX );
    }


    /**
     * @param oe
     * @throws org.openldap.fortress.CreateException
     */
    final void create( OrganizationalUnit oe )
        throws org.openldap.fortress.CreateException
    {
        LdapConnection ld = null;
        String nodeDn = GlobalIds.OU + "=" + oe.getName() + ",";
        if ( VUtil.isNotNullOrEmpty( oe.getParent() ) )
            nodeDn += GlobalIds.OU + "=" + oe.getParent() + ",";
        nodeDn += getRootDn( oe.getContextId() );
        try
        {
            LOG.info( "create container dn [" + nodeDn + "]" );
            Entry myEntry = new DefaultEntry( nodeDn );
            myEntry.add( GlobalIds.OBJECT_CLASS, ORGUNIT_OBJ_CLASS );
            myEntry.add( GlobalIds.OU, oe.getName() );
            myEntry.add( GlobalIds.DESC, oe.getDescription() );
            ld = getAdminConnection();
            add( ld, myEntry );
        }
        catch ( LdapException e )
        {
            String error = "create container node dn [" + nodeDn + "] caught LDAPException="
                + e.getMessage();
            throw new org.openldap.fortress.CreateException( GlobalErrIds.CNTR_CREATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param oe
     * @throws org.openldap.fortress.RemoveException
     */
    final void remove( OrganizationalUnit oe )
        throws org.openldap.fortress.RemoveException
    {
        LdapConnection ld = null;
        String nodeDn = GlobalIds.OU + "=" + oe.getName() + ",";
        if ( VUtil.isNotNullOrEmpty( oe.getParent() ) )
            nodeDn += GlobalIds.OU + "=" + oe.getParent() + ",";
        nodeDn += getRootDn( oe.getContextId(), GlobalIds.SUFFIX );

        LOG.info( "remove container dn [" + nodeDn + "]" );
        try
        {
            ld = getAdminConnection();
            deleteRecursive( ld, nodeDn );
        }
        catch ( CursorException e )
        {
            String error = "remove container node dn [" + nodeDn + "] caught CursorException="
                + e.getMessage();
            throw new org.openldap.fortress.RemoveException( GlobalErrIds.CNTR_DELETE_FAILED, error, e );
        }
        catch ( LdapException e )
        {
            String error = "remove container node dn [" + nodeDn + "] caught LDAPException="
                + e.getMessage();
            throw new org.openldap.fortress.RemoveException( GlobalErrIds.CNTR_DELETE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }
}