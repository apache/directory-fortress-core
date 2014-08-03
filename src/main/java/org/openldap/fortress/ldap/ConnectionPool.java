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
 *
 * The contents of this file are subject to the Netscape Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/NPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is mozilla.org code.
 *
 * The Initial Developer of the Original Code is Netscape
 * Communications Corporation.  Portions created by Netscape are
 * Copyright (C) 1999 Netscape Communications Corporation. All
 * Rights Reserved.
 *
 * Contributor(s):
 *
 *        Tony Dahbura (tony@dahbura.com)
 *            Fixed pool close to reset auth to original if
 *                changed during use.
 *            Setup LDAPConnection argument to immediately clone
 *                the passed connection so if the caller did
 *                something to it after pool creation then it
 *                would not affect pool operations.
 *
 */

package org.openldap.fortress.ldap;


import java.security.GeneralSecurityException;
import java.util.Date;

import com.unboundid.ldap.sdk.migrate.ldapjdk.JavaToLDAPSocketFactory;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustStoreTrustManager;
import org.openldap.fortress.cfg.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;

import javax.net.ssl.SSLSocketFactory;


/**
 * This connection pool class is used by Fortress {@link PoolMgr}.
 * PoolMgr operations utilize multiple instances of this class to connections for different purposes.
 * For example the 'admin' pool contains connections that have privileges to make modifications to the directory data during administrative operations {@link org.openldap.fortress.AdminMgr}.
 * The 'user' pool contain unprivileged connections used for authentication processing only, {@link org.openldap.fortress.AccessMgr}.
 * A 3rd pool, may be used to interrogate data stored by OpenLDAP's slapo access log info, This is used interrogating the fortress audit log events, {@link org.openldap.fortress.AuditMgr}.
 * The contents of this file have been derived from the original, Mozilla Java LDAP SDK, and are subject to the Netscape Public License Version 1.1 (the "License")
 * as described at the top of this file;
 * The code mods include additional functionality to enable SSL connections in pool.  There have been other updates to the original functions to integrate with UnboundID's Java LDAP SDK.
 * </p>
 * Original Mozilla javadoc:
 * Class to maintain a pool of individual connections to the
 * same server. Specify the initial size and the max size
 * when constructing a pool. Call getConnection() to obtain
 * a connection from the pool and close() to return it. If
 * the pool is fully extended and there are no free connections,
 * getConnection() blocks until a connection has been returned
 * to the pool.<BR>
 * Call destroy() to release all connections.
 * <BR><BR>Example:<BR>
 * <PRE>
 * ConnectionPool pool = null;
 * try {
 * pool = new ConnectionPool( 10, 30,
 * "foo.acme.com",389,
 * "uid=me, o=acme.com",
 * "password" );
 * } catch ( LDAPException e ) {
 * System.err.println( "Unable to create connection pool" );
 * System.exit( 1 );
 * }
 * while ( clientsKnocking ) {
 * String filter = getSearchFilter();
 * LDAPConnection ld = pool.getConnection();
 * try {
 * LDAPSearchResults res = ld.search( BASE, ld.SCOPE_SUB,
 * filter, attrs,
 * false );
 * pool.close( ld );
 * while( res.hasMoreElements() ) {
 * ...
 * </PRE>
 */
class ConnectionPool
{
    // Logging
    private static final String CLS_NM = ConnectionPool.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    /**
     * Create a new instance of connection pool with specified parameters.  These connections will be used by the Fortress DAO
     * methods for processing ldap server operations.
     *
     * @param min    initial number of connections
     * @param max    maximum number of connections
     * @param host   hostname of LDAP server
     * @param port   port number of LDAP server
     * @param authdn DN to authenticate as
     * @param authpw password for authentication
     * @throws LDAPException on failure to create connections
     */
    ConnectionPool( int min, int max,
        String host, int port,
        String authdn, String authpw )
        throws LDAPException
    {
        this( min, max, host, port, authdn, authpw, null );
    }


    /*
     * Constructor for using an existing connection to clone
     * from
     * 
     * @param min initial number of connections
     * @param max maximum number of connections
     * @param host hostname of LDAP server
     * @param port port number of LDAP server
     * @param authdn DN to authenticate as
     * @param authpw password for authentication
     * @param ldc connection to clone 
     * @exception LDAPException on failure to create connections 
     */
    private ConnectionPool( int min, int max,
        String host, int port,
        String authdn, String authpw,
        LDAPConnection ldc )
        throws LDAPException
    {
        this.poolSize = min;
        this.poolMax = max;
        this.host = host;
        this.port = port;
        this.authdn = authdn;
        this.authpw = authpw;
        this.ldc = ldc;
        this.debugMode = false;
        createPool();
    }


    /**
     * Destroy the whole pool - called during a shutdown
     */
    void destroy()
    {
        for ( int i = 0; i < pool.size(); i++ )
        {
            disconnect( ( LDAPConnectionObject ) pool.elementAt( i ) );
        }
        pool.removeAllElements();
    }


    /**
     * Gets a connection from the pool
     * <p/>
     * If no connections are available, the pool will be
     * extended if the number of connections is less than
     * the maximum; if the pool cannot be extended, the method
     * blocks until a free connection becomes available.
     *
     * @return an active connection.
     */
    LDAPConnection getConnection()
    {
        LDAPConnection con;

        while ( ( con = getConnFromPool() ) == null )
        {
            synchronized ( pool )
            {
                try
                {
                    pool.wait();
                }
                catch ( InterruptedException e )
                {
                    LOG.warn( "getConnection caught InterruptedException" );
                }
            }
        }
        return con;
    }


    /**
     * Gets a connection from the pool within a time limit.
     * <p/>
     * If no connections are available, the pool will be
     * extended if the number of connections is less than
     * the maximum; if the pool cannot be extended, the method
     * blocks until a free connection becomes available or the
     * time limit is exceeded.
     *
     * @param timeout timeout in milliseconds
     * @return an active connection or <CODE>null</CODE> if timed out.
     */
    LDAPConnection getConnection( int timeout )
    {
        LDAPConnection con;

        while ( ( con = getConnFromPool() ) == null )
        {
            long t1, t0 = System.currentTimeMillis();

            if ( timeout <= 0 )
            {
                return con;
            }

            synchronized ( pool )
            {
                try
                {
                    pool.wait( timeout );
                }
                catch ( InterruptedException e )
                {
                    LOG.warn( "getConnection caught InterruptedException for timeout: " + timeout );
                    return null;
                }
            }

            t1 = System.currentTimeMillis();
            timeout -= ( t1 - t0 );
        }
        return con;
    }


    /**
     * Gets a connection from the pool
     * <p/>
     * If no connections are available, the pool will be
     * extended if the number of connections is less than
     * the maximum; if the pool cannot be extended, the method
     * returns null.
     *
     * @return an active connection or null.
     */
    synchronized LDAPConnection getConnFromPool()
    {
        LDAPConnection con = null;
        LDAPConnectionObject ldapconnobj = null;

        int pSize = pool.size();

        // Get an available connection
        for ( int i = 0; i < pSize; i++ )
        {

            // Get the ConnectionObject from the pool
            LDAPConnectionObject co =
                ( LDAPConnectionObject ) pool.elementAt( i );

            if ( co.isAvailable() )
            { // Conn available?
                ldapconnobj = co;
                break;
            }
        }

        if ( ldapconnobj == null )
        {
            // If there there were no conns in pool, can we grow
            // the pool?
            if ( ( poolMax < 0 ) ||
                ( ( poolMax > 0 ) &&
                ( pSize < poolMax ) ) )
            {

                // Yes we can grow it
                int i = addConnection();

                // If a new connection was created, use it
                if ( i >= 0 )
                {
                    ldapconnobj =
                        ( LDAPConnectionObject ) pool.elementAt( i );
                }
            }
            else
            {
                debug( "All pool connections in use" );
            }
        }

        if ( ldapconnobj != null )
        {
            ldapconnobj.setInUse( true ); // Mark as in use
            con = ldapconnobj.getLDAPConn();
        }
        return con;
    }


    /**
     * This is our soft close - all we do is mark
     * the connection as available for others to use.
     * We also reset the auth credentials in case
     * they were changed by the caller.
     *
     * @param ld a connection to return to the pool
     */
    synchronized void close( LDAPConnection ld )
    {

        int index = find( ld );
        if ( index != -1 )
        {
            LDAPConnectionObject co =
                ( LDAPConnectionObject ) pool.elementAt( index );
            // Reset the auth if necessary
            if ( ldc == null )
            {
                boolean reauth = false;
                //if user bound anon then getAuthenticationDN is null
                if ( ld.getAuthenticationDN() == null )
                {
                    reauth = ( authdn != null );
                }
                else if ( !ld.getAuthenticationDN().equalsIgnoreCase( authdn ) )
                {
                    reauth = true;
                }
            }
            co.setInUse( false ); // Mark as available
            synchronized ( pool )
            {
                pool.notifyAll();
            }
        }
    }


    /**
     * Debug method to print the contents of the pool
     */
    public void printPool()
    {
        System.out.println( "--ConnectionPool--" );
        for ( int i = 0; i < pool.size(); i++ )
        {
            LDAPConnectionObject co =
                ( LDAPConnectionObject ) pool.elementAt( i );
            String msg = "" + i + "=" + co;
            LOG.info( "printPool: " + msg );
        }
    }


    private void disconnect(
        LDAPConnectionObject ldapconnObject )
    {
        if ( ldapconnObject != null )
        {
            if ( ldapconnObject.isAvailable() )
            {
                LDAPConnection ld = ldapconnObject.getLDAPConn();
                if ( ( ld != null ) && ( ld.isConnected() ) )
                {
                    try
                    {
                        ld.disconnect();
                    }
                    catch ( LDAPException e )
                    {
                        debug( "disconnect: " + e.toString() );
                        LOG.warn( "disconnect caught LDAPException: " + e.getMessage() );
                    }
                }
                ldapconnObject.setLDAPConn( null ); // Clear conn
            }
        }
    }


    private void createPool() throws LDAPException
    {
        // Called by the constructors
        if ( poolSize <= 0 )
        {
            throw new LDAPException( "ConnectionPoolSize invalid" );
        }
        if ( poolMax < poolSize )
        {
            debug( "ConnectionPoolMax is invalid, set to " +
                poolSize );
            poolMax = poolSize;
        }

        debug( "****Initializing LDAP Pool****" );
        debug( "LDAP host = " + host + " on port " + port );
        debug( "Number of connections=" + poolSize );
        debug( "Maximum number of connections=" + poolMax );
        debug( "******" );

        pool = new java.util.Vector(); // Create pool vector
        setUpPool( poolSize ); // Initialize it
    }


    private int addConnection()
    {
        int index = -1;

        debug( "adding a connection to pool..." );
        try
        {
            int size = pool.size() + 1; // Add one connection
            setUpPool( size );

            if ( size == pool.size() )
            {
                // New size is size requested?
                index = size - 1;
            }
        }
        catch ( Exception ex )
        {
            debug( "Adding a connection: " + ex.toString() );
            LOG.warn( "addConnection caught Exception: " + ex.getMessage() );
        }
        return index;
    }


    /**
     * *** FORTRESS MOD ****
     *
     * Create pool of LDAP connections to server.  Add SSL capability using unboundId's compatibility utility.
     *
     * @param size number of connections to generate and store in pool
     * @throws LDAPException in the event of system error.
     */
    private synchronized void setUpPool( int size )
        throws LDAPException
    {
        // Loop on creating connections
        while ( pool.size() < size )
        {
            LDAPConnectionObject co =
                new LDAPConnectionObject();

            LDAPConnection newConn = createConnection( );
            newConn.connect( host, port, authdn, authpw );
            co.setLDAPConn( newConn );
            co.setInUse( false ); // Mark not in use
            pool.addElement( co );
        }
    }

    /**
     * *** FORTRESS MOD ****
     *
     * Used to manage trust store properties.  If enabled, create SSL connection.
     *
     */
    private static final String ENABLE_LDAP_SSL = "enable.ldap.ssl";
    private static final String ENABLE_LDAP_SSL_DEBUG = "enable.ldap.ssl.debug";
    private static final String TRUST_STORE = Config.getProperty( "trust.store" );
    private static final String TRUST_STORE_PW = Config.getProperty( "trust.store.password" );
    private static final boolean IS_SSL = (
        Config.getProperty( ENABLE_LDAP_SSL ) != null   &&
        Config.getProperty( ENABLE_LDAP_SSL ).equalsIgnoreCase( "true" ) &&
        TRUST_STORE      != null   &&
        TRUST_STORE_PW   != null );

    private static final String SET_TRUST_STORE_PROP = "trust.store.set.prop";
    private static final boolean IS_SET_TRUST_STORE_PROP = (
        IS_SSL &&
        Config.getProperty( SET_TRUST_STORE_PROP ) != null   &&
        Config.getProperty( SET_TRUST_STORE_PROP ).equalsIgnoreCase( "true" ));

    private static final boolean IS_SSL_DEBUG = ( ( Config.getProperty( ENABLE_LDAP_SSL_DEBUG ) != null ) && ( Config
        .getProperty( ENABLE_LDAP_SSL_DEBUG ).equalsIgnoreCase( "true" ) ) );

    static
    {
        if(IS_SET_TRUST_STORE_PROP)
        {
            LOG.info( "Set JSSE truststore properties:");
            LOG.info( "javax.net.ssl.trustStore: " + TRUST_STORE );
            LOG.info( "javax.net.debug: " + new Boolean( IS_SSL_DEBUG ).toString());
            System.setProperty( "javax.net.ssl.trustStore", TRUST_STORE );
            System.setProperty( "javax.net.ssl.trustStorePassword", TRUST_STORE_PW );
            System.setProperty( "javax.net.debug", new Boolean( IS_SSL_DEBUG ).toString() );
        }
    }

    /**
     * *** FORTRESS MOD ****
     *
     * If enabled, use Unbound compatibility lib to create SSL connection.
     *
     * @return handle to LDAPConnection
     * @throws LDAPException wrap GeneralSecurityException or throws ldapexcep.
     */
    private LDAPConnection createConnection() throws LDAPException
    {
        LDAPConnection newConn = null;
        if(IS_SSL)
        {
            // Generate SSL Connection using Unbound compatibility lib utils:
            // http://stackoverflow.com/questions/22672477/unboundid-ldap-jdk-migration
            SSLSocketFactory sslSocketFactory;
            //SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
            // These config values set in fortress.properties
            SSLUtil sslUtil = new SSLUtil(
                new TrustStoreTrustManager(
                    TRUST_STORE,
                    TRUST_STORE_PW.toCharArray() , null, true ) );
            try
            {
                sslSocketFactory = sslUtil.createSSLSocketFactory();
            }
            catch(GeneralSecurityException e)
            {
                String error = "GeneralSecurityException while creating SSL socket factory=" + e;
                throw new LDAPException( error, LDAPException.CONNECT_ERROR );
            }
            JavaToLDAPSocketFactory ldapSocketFactory =
                new JavaToLDAPSocketFactory(sslSocketFactory);
            newConn = new LDAPConnection(ldapSocketFactory);
        }
        else
        {
            // Make LDAP connection, using template if available
            newConn = new LDAPConnection();
        }
        return newConn;
    }


    private int find( LDAPConnection con )
    {
        // Find the matching Connection in the pool
        if ( con != null )
        {
            for ( int i = 0; i < pool.size(); i++ )
            {
                LDAPConnectionObject co =
                    ( LDAPConnectionObject ) pool.elementAt( i );
                if ( co.getLDAPConn() == con )
                {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * Sets the debug printout mode.
     *
     * @param mode debug mode to use
     */
    public synchronized void setDebug( boolean mode )
    {
        debugMode = mode;
    }


    /**
     * Reports the debug printout mode.
     *
     * @return debug mode in use.
     */
    public boolean getDebug()
    {
        return debugMode;
    }


    private void debug( String s )
    {
        if ( debugMode )
            System.out.println( "ConnectionPool (" +
                new Date() + ") : " + s );
    }


    private void debug( String s, boolean severe )
    {
        if ( debugMode || severe )
        {
            System.out.println( "ConnectionPool (" +
                new Date() + ") : " + s );
        }
    }

    /**
     * Wrapper for LDAPConnection object in pool
     */
    class LDAPConnectionObject
    {

        /**
         * Returns the associated LDAPConnection.
         *
         * @return the LDAPConnection.
         */
        LDAPConnection getLDAPConn()
        {
            return this.ld;
        }


        /**
         * Sets the associated LDAPConnection
         *
         * @param ld the LDAPConnection
         */
        void setLDAPConn( LDAPConnection ld )
        {
            this.ld = ld;
        }


        /**
         * Marks a connection in use or available
         *
         * @param inUse <code>true</code> to mark in use, <code>false</code> if available
         */
        void setInUse( boolean inUse )
        {
            this.inUse = inUse;
        }


        /**
         * Returns whether the connection is available
         * for use by another user.
         *
         * @return <code>true</code> if available.
         */
        boolean isAvailable()
        {
            return !inUse;
        }


        /**
         * Debug method
         *
         * @return s user-friendly rendering of the object.
         */
        public String toString()
        {
            return "LDAPConnection=" + ld + ",inUse=" + inUse;
        }

        private LDAPConnection ld; // LDAP Connection
        private boolean inUse; // In use? (true = yes)
    }

    private final int poolSize; // Min pool size
    private int poolMax; // Max pool size
    private final String host; // LDAP host
    private final int port; // Port to connect at
    private final String authdn; // Identity of connections
    private final String authpw; // Password for authdn
    private LDAPConnection ldc = null; // Connection to clone
    private java.util.Vector pool; // the actual pool
    private boolean debugMode;
}
