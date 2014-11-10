/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core.ldap.container;


import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.fortress.core.CreateException;
import org.apache.directory.fortress.core.RemoveException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.fortress.core.ldap.ApacheDsDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.util.attr.VUtil;


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
     * @throws org.apache.directory.fortress.core.CreateException
     */
    final void create( OrganizationalUnit oe )
        throws CreateException
    {
        LdapConnection ld = null;
        String nodeDn = GlobalIds.OU + "=" + oe.getName() + ",";
        
        if ( VUtil.isNotNullOrEmpty( oe.getParent() ) )
        {
            nodeDn += GlobalIds.OU + "=" + oe.getParent() + ",";
        }
        
        nodeDn += getRootDn( oe.getContextId() );
        
        try
        {
            LOG.info( "create container dn [{}]",  nodeDn );

            Entry myEntry = new DefaultEntry( nodeDn,
                GlobalIds.OBJECT_CLASS, ORGUNIT_CLASS,
                GlobalIds.OU, oe.getName(),
                GlobalIds.DESC, oe.getDescription() );

            ld = getAdminConnection();
            add( ld, myEntry );
        }
        catch ( LdapException e )
        {
            String error = "create container node dn [" + nodeDn + "] caught LDAPException="
                + e.getMessage();
            throw new CreateException( GlobalErrIds.CNTR_CREATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param oe
     * @throws org.apache.directory.fortress.core.RemoveException
     */
    final void remove( OrganizationalUnit oe )
        throws RemoveException
    {
        LdapConnection ld = null;
        String nodeDn = GlobalIds.OU + "=" + oe.getName() + ",";
        
        if ( VUtil.isNotNullOrEmpty( oe.getParent() ) )
        {
            nodeDn += GlobalIds.OU + "=" + oe.getParent() + ",";
        }
        
        nodeDn += getRootDn( oe.getContextId(), GlobalIds.SUFFIX );

        LOG.info( "remove container dn [{}]", nodeDn );
        
        try
        {
            ld = getAdminConnection();
            deleteRecursive( ld, nodeDn );
        }
        catch ( CursorException e )
        {
            String error = "remove container node dn [" + nodeDn + "] caught CursorException="
                + e.getMessage();
            throw new RemoveException( GlobalErrIds.CNTR_DELETE_FAILED, error, e );
        }
        catch ( LdapException e )
        {
            String error = "remove container node dn [" + nodeDn + "] caught LDAPException="
                + e.getMessage();
            throw new RemoveException( GlobalErrIds.CNTR_DELETE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }
}