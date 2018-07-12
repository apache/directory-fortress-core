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
package org.apache.directory.fortress.core.impl;


import org.apache.commons.lang.StringUtils;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.fortress.core.CreateException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.RemoveException;
import org.apache.directory.fortress.core.ldap.LdapDataProvider;
import org.apache.directory.fortress.core.model.Suffix;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class contains the Suffix node for OpenLDAP Directory Information Tree.
 * <br>The domain component object class is 'dcObject' <br>
 * <p>
 * dcObject Auxiliary Object Class is used to store basic attributes like domain component names and description.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code># RFC 2247</code>
 * <li> <code>objectclass ( 1.3.6.1.4.1.1466.344 NAME 'dcObject'</code>
 * <li> <code>SUP top AUXILIARY MUST dc )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p>
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
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class SuffixDAO extends LdapDataProvider
{
    private static final String CLS_NM = SuffixDAO.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final String[] SUFFIX_OBJ_CLASS =
        {
            SchemaConstants.DC_OBJECT_OC, SchemaConstants.ORGANIZATION_OC
    };

    /**
     * @param se
     * @throws org.apache.directory.fortress.core.CreateException
     */
    void create( Suffix se )
        throws CreateException
    {
        LdapConnection ld = null;
        String nodeDn = getDn( se );
        try
        {
            LOG.info( "create suffix dn [{}]", nodeDn );
            Entry myEntry = new DefaultEntry( nodeDn );
            myEntry.add( SchemaConstants.OBJECT_CLASS_AT, SUFFIX_OBJ_CLASS );
            myEntry.add( SchemaConstants.DC_AT, se.getName() );
            myEntry.add( SchemaConstants.O_AT, se.getDescription() );

            ld = getAdminConnection();
            add( ld, myEntry );
        }
        catch ( LdapException e )
        {
            String error = "create container node dn [" + nodeDn + "] caught LDAPException="
                + e;
            throw new CreateException( GlobalErrIds.SUFX_CREATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * <p>
     * <font size="4" color="red">
     * This method is destructive as it will remove all nodes below the suffix using recursive delete function.<BR>
     * Extreme care should be taken during execution to ensure target directory is correct and permanent removal of data is intended.  There is no
     * 'undo' for this operation.
     * </font>
     *
     * @param se
     * @throws org.apache.directory.fortress.core.RemoveException
     */
    void remove( Suffix se )
        throws RemoveException
    {
        LdapConnection ld = null;
        String nodeDn = getDn( se );
        LOG.info( "remove suffix dn [{}]", nodeDn );
        try
        {
            ld = getAdminConnection();
            deleteRecursive( ld, nodeDn );
        }
        catch ( CursorException e )
        {
            String error = "remove suffix node dn [" + nodeDn + "] caught CursorException="
                + e.getMessage();
            throw new RemoveException( GlobalErrIds.SUFX_DELETE_FAILED, error, e );
        }
        catch ( LdapException e )
        {
            String error = "remove suffix node dn [" + nodeDn + "] caught LDAPException="
                + e;
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
        String dn = SchemaConstants.DC_AT + "=" + se.getName() + "," + SchemaConstants.DC_AT + "=" + se.getDc();

        // only use this domain component variable if it has been set in the build.properties file:
        if ( StringUtils.isNotEmpty( se.getDc2() ) && !se.getDc2().equals( "${suffix.dc2}" ) )
        {
            dn += "," + SchemaConstants.DC_AT + "=" + se.getDc2();
        }

        LOG.debug( "suffix={}", dn );

        return dn;
    }
}