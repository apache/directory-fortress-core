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
package org.apache.directory.fortress.core.ldap;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.directory.api.ldap.codec.api.LdapApiService;
import org.apache.directory.api.ldap.codec.api.LdapApiServiceFactory;
import org.apache.directory.api.ldap.codec.standalone.StandaloneLdapApiService;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.fortress.core.CfgRuntimeException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.EncryptUtil;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapConnectionPool;
import org.apache.directory.ldap.client.api.ValidatingPoolableLdapConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This utility manages the LDAP connection pools and provides methods for adding / removing connections from the three pools.
 * <ul>
 *   <li>Admin Connections - bound with ldap service account creds</li>
 *   <li>User Connections - unbound used for authentication</li>
 *   <li>Audit Log Connections - bound with slapo access log service account creds (OpenLDAP only)</li>
 * </ul>
 *
 * Each connection pool is initialized on first invocation of getInstance() which stores a reference to self used by subsequent callers.
 * <p>
 * This class is not thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class LdapConnectionProvider
{

    private static final String CLS_NM = LdapConnectionProvider.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final String ENABLE_LDAP_STARTTLS = "enable.ldap.starttls";
    private boolean IS_SSL;

    /**
     * The Admin connection pool
     */
    private static LdapConnectionPool adminPool;

    /**
     * The Log connection pool
     */
    private static LdapConnectionPool logPool;

    /**
     * The User connection pool
     */
    private static LdapConnectionPool userPool;

    private static volatile LdapConnectionProvider sINSTANCE = null;

    /**
     * Synchronized getter guards access to reference to self which is a singleton and only be created the first time invoked.
     *
     * @return reference to self.
     */
    public static LdapConnectionProvider getInstance()
    {
        if ( sINSTANCE == null )
        {
            synchronized ( LdapConnectionProvider.class )
            {
                if ( sINSTANCE == null )
                {
                    sINSTANCE = new LdapConnectionProvider();
                }
            }
        }
        return sINSTANCE;
    }

    /**
     * Private constructor calls the init method which initializes the connection pools.
     *
     */
    private LdapConnectionProvider()
    {
        init();
    }

    /**
     * Initialize the three connection pools using settings and coordinates contained in the config.
     */
    private void init()
    {
        IS_SSL = ( Config.getInstance().getProperty( GlobalIds.ENABLE_LDAP_SSL ) != null &&
            Config.getInstance().getProperty( GlobalIds.ENABLE_LDAP_SSL ).equalsIgnoreCase( "true" ) &&
            Config.getInstance().getProperty( GlobalIds.TRUST_STORE ) != null &&
            Config.getInstance().getProperty( GlobalIds.TRUST_STORE_PW, true ) != null );

        String host = Config.getInstance().getProperty( GlobalIds.LDAP_HOST, "localhost" );
        int port = Config.getInstance().getInt( GlobalIds.LDAP_PORT, 389 );
        int min = Config.getInstance().getInt( GlobalIds.LDAP_ADMIN_POOL_MIN, 1 );
        int max = Config.getInstance().getInt( GlobalIds.LDAP_ADMIN_POOL_MAX, 10 );
        int logmin = Config.getInstance().getInt( GlobalIds.LDAP_LOG_POOL_MIN, 1 );
        int logmax = Config.getInstance().getInt( GlobalIds.LDAP_LOG_POOL_MAX, 10 );
        boolean validate = Config.getInstance().getBoolean( GlobalIds.LDAP_VALIDATE_CONN, false );
        boolean testWhileIdle = Config.getInstance().getBoolean( GlobalIds.LDAP_ADMIN_POOL_TEST_IDLE, true );
        boolean logTestWhileIdle = Config.getInstance().getBoolean( GlobalIds.LDAP_LOG_POOL_TEST_IDLE, true );

        int timeBetweenEvictionRunMillis = Config.getInstance().getInt( GlobalIds.LDAP_ADMIN_POOL_EVICT_RUN_MILLIS, 1000 * 60 * 30 );
        int logTimeBetweenEvictionRunMillis = Config.getInstance().getInt( GlobalIds.LDAP_LOG_POOL_EVICT_RUN_MILLIS, 1000 * 60 * 30 );

        LOG.info( "LDAP POOL:  host=[{}], port=[{}], min=[{}], max=[{}]", host, port, min, max );

        LdapConnectionConfig config = new LdapConnectionConfig();
        config.setLdapHost( host );
        config.setLdapPort( port );
        config.setName( Config.getInstance().getProperty( GlobalIds.LDAP_ADMIN_POOL_UID, "" ) );

        config.setUseSsl( IS_SSL );
        //config.setTrustManagers( new NoVerificationTrustManager() );

        if ( Config.getInstance().getBoolean( ENABLE_LDAP_STARTTLS, false ) )
        {
            config.setUseTls( true );
        }

        if ( IS_SSL && StringUtils.isNotEmpty( Config.getInstance().getProperty( GlobalIds.TRUST_STORE ) ) &&
            StringUtils.isNotEmpty( Config.getInstance().getProperty( GlobalIds.TRUST_STORE_PW, true ) ) )
        {
            // validate certificates but allow self-signed certs if within this truststore:
            config.setTrustManagers( new LdapClientTrustStoreManager( Config.getInstance().getProperty( GlobalIds
                .TRUST_STORE ), Config.getInstance().getProperty( GlobalIds.TRUST_STORE_PW, true ).toCharArray(), null,
                true ) );
        }

        String adminPw;
        if ( EncryptUtil.isEnabled() )
        {
            adminPw = EncryptUtil.getInstance().decrypt( Config.getInstance().getProperty( GlobalIds
                .LDAP_ADMIN_POOL_PW, true ) );
        }
        else
        {
            adminPw = Config.getInstance().getProperty( GlobalIds.LDAP_ADMIN_POOL_PW, true );
        }

        config.setCredentials( adminPw );
        try
        {
            List<String> listExOps = new ArrayList<>();
            listExOps.add( "org.openldap.accelerator.impl.createSession.RbacCreateSessionFactory" );
            listExOps.add( "org.openldap.accelerator.impl.checkAccess.RbacCheckAccessFactory" );
            listExOps.add( "org.openldap.accelerator.impl.addRole.RbacAddRoleFactory" );
            listExOps.add( "org.openldap.accelerator.impl.dropRole.RbacDropRoleFactory" );
            listExOps.add( "org.openldap.accelerator.impl.deleteSession.RbacDeleteSessionFactory" );
            listExOps.add( "org.openldap.accelerator.impl.sessionRoles.RbacSessionRolesFactory" );
            LdapApiService ldapApiService = new StandaloneLdapApiService( new ArrayList<String>(), listExOps );

            if ( !LdapApiServiceFactory.isInitialized() )
            {
                LdapApiServiceFactory.initialize( ldapApiService );
            }
            config.setLdapApiService( ldapApiService );
        }
        catch ( Exception ex )
        {
            String error = "Exception caught initializing Admin Pool: " + ex;
            throw new CfgRuntimeException( GlobalErrIds.FT_APACHE_LDAP_POOL_INIT_FAILED, error, ex );
        }

        PoolableObjectFactory<LdapConnection> poolFactory = new ValidatingPoolableLdapConnectionFactory( config );

        // Create the Admin pool
        adminPool = new LdapConnectionPool( poolFactory );
        adminPool.setTestOnBorrow( validate );
        adminPool.setWhenExhaustedAction( GenericObjectPool.WHEN_EXHAUSTED_GROW );
        adminPool.setMaxActive( max );
        adminPool.setMinIdle( min );
        adminPool.setMaxIdle( -1 );
        adminPool.setTestWhileIdle( testWhileIdle );
        adminPool.setTimeBetweenEvictionRunsMillis( timeBetweenEvictionRunMillis );
        //adminPool.setMaxWait( 0 );

        // Create the User pool
        userPool = new LdapConnectionPool( poolFactory );
        userPool.setTestOnBorrow( validate );
        userPool.setWhenExhaustedAction( GenericObjectPool.WHEN_EXHAUSTED_GROW );
        userPool.setMaxActive( max );
        userPool.setMinIdle( min );
        userPool.setMaxIdle( -1 );
        userPool.setTestWhileIdle( testWhileIdle );
        userPool.setTimeBetweenEvictionRunsMillis( timeBetweenEvictionRunMillis );

        // This pool of access log connections is used by {@link org.apache.directory.fortress.AuditMgr}.
        // To enable, set {@code log.admin.user} && {@code log.admin.pw} inside fortress.properties file:
        if ( StringUtils.isNotEmpty( GlobalIds.LDAP_LOG_POOL_UID ) && StringUtils.isNotEmpty( GlobalIds.LDAP_LOG_POOL_PW ) )
        {
            // Initializing the log pool in static block requires static props set within fortress.properties.
            // To make this dynamic requires moving this code outside of static block AND storing the connection
            // metadata inside fortress config node (in ldap).
            LdapConnectionConfig logConfig = new LdapConnectionConfig();
            logConfig.setLdapHost( host );
            logConfig.setLdapPort( port );
            logConfig.setName( Config.getInstance().getProperty( GlobalIds.LDAP_ADMIN_POOL_UID, "" ) );

            logConfig.setUseSsl( IS_SSL );

            if ( IS_SSL && StringUtils.isNotEmpty( Config.getInstance().getProperty( GlobalIds.TRUST_STORE ) ) &&
                StringUtils.isNotEmpty( Config.getInstance().getProperty( GlobalIds.TRUST_STORE_PW, true ) ) )
            {
                // validate certificates but allow self-signed certs if within this truststore:
                logConfig.setTrustManagers( new LdapClientTrustStoreManager( Config.getInstance().getProperty(
                    GlobalIds.TRUST_STORE ), Config.getInstance().getProperty( GlobalIds.TRUST_STORE_PW, true ).toCharArray
                    (), null, true ) );
            }

            logConfig.setName( Config.getInstance().getProperty( GlobalIds.LDAP_LOG_POOL_UID, "" ) );
            String logPw;
            if ( EncryptUtil.isEnabled() )
            {
                logPw = EncryptUtil.getInstance().decrypt( Config.getInstance().getProperty( GlobalIds.LDAP_LOG_POOL_PW, true ) );
            }
            else
            {
                logPw = Config.getInstance().getProperty( GlobalIds.LDAP_LOG_POOL_PW, true );
            }
            logConfig.setCredentials( logPw );
            poolFactory = new ValidatingPoolableLdapConnectionFactory( logConfig );
            logPool = new LdapConnectionPool( poolFactory );
            logPool.setTestOnBorrow( validate );
            logPool.setWhenExhaustedAction( GenericObjectPool.WHEN_EXHAUSTED_GROW );
            logPool.setMaxActive( logmax );
            logPool.setMinIdle( logmin );
            logPool.setTestWhileIdle( logTestWhileIdle );
            logPool.setTimeBetweenEvictionRunsMillis( logTimeBetweenEvictionRunMillis );
        }
    }


    /**
     * Calls the PoolMgr to close the Admin LDAP connection.
     *
     * @param connection handle to ldap connection object.
     */
    public void closeAdminConnection(LdapConnection connection)
    {
        try
        {
            adminPool.releaseConnection( connection );
        }
        catch ( Exception e )
        {
            LOG.warn( "Error closing admin connection: " + e );
            //throw new RuntimeException( e );
        }
    }


    /**
     * Calls the PoolMgr to close the Log LDAP connection.
     *
     * @param connection handle to ldap connection object.
     */
    public void closeLogConnection(LdapConnection connection)
    {
        try
        {
            logPool.releaseConnection( connection );
        }
        catch ( Exception e )
        {
            LOG.warn( "Error closing log connection: " + e );
            //throw new RuntimeException( e );
        }
    }


    /**
     * Calls the PoolMgr to close the User LDAP connection.
     *
     * @param connection handle to ldap connection object.
     */
    public void closeUserConnection(LdapConnection connection)
    {
        try
        {
            userPool.releaseConnection( connection );
        }
        catch ( Exception e )
        {
            LOG.warn( "Error closing user connection: " + e );
            //throw new RuntimeException( e );
        }
    }


    /**
     * Calls the PoolMgr to get an Admin connection to the LDAP server.
     *
     * @return ldap connection.
     * @throws LdapException If we had an issue getting an LDAP connection
     */
    public LdapConnection getAdminConnection() throws LdapException
    {
        try
        {
            return adminPool.getConnection();
        }
        catch ( Exception e )
        {
            throw new LdapException( e );
        }
    }


    /**
     * Calls the PoolMgr to get an Log connection to the LDAP server.
     *
     * @return ldap connection.
     * @throws LdapException If we had an issue getting an LDAP connection
     */
    public LdapConnection getLogConnection() throws LdapException
    {
        try
        {
            return logPool.getConnection();
        }
        catch ( Exception e )
        {
            throw new LdapException( e );
        }
    }


    /**
     * Calls the PoolMgr to get an User connection to the LDAP server.
     *
     * @return ldap connection.
     * @throws LdapException If we had an issue getting an LDAP connection
     */
    public LdapConnection getUserConnection() throws LdapException
    {
        try
        {
            return userPool.getConnection();
        }
        catch ( Exception e )
        {
            throw new LdapException( e );
        }
    }

    /**
     * Closes all the ldap connection pools.
     */
    public static void closeAllConnectionPools()
    {
        try
        {
            LOG.info( "Closing admin pool" );
            adminPool.close();
        }
        catch ( Exception e )
        {
            LOG.warn( "Error closing admin pool: " + e );
        }

        try
        {
            LOG.info( "Closing user pool" );
            userPool.close();
        }
        catch ( Exception e )
        {
            LOG.warn( "Error closing user pool: " + e );
        }

        try
        {
            LOG.info( "Closing log pool" );
            logPool.close();
        }
        catch ( Exception e )
        {
            LOG.warn( "Error closing log pool: " + e );
        }
    }
}
