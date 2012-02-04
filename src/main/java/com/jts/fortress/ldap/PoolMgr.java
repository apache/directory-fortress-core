/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ldap;

import com.jts.fortress.configuration.Config;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.crypto.EncryptUtil;
import org.apache.log4j.Logger;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPControl;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConstraints;

/**
 * This class uses {@link ConnectionPool} to manage pools of {@link com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection}
 * to supply resource connections to Fortress DAO utilities.  The methods in the class are used by internal Fortress functions
 * and are not intended for used by external clients.  This class maintains 3 pools of connections.
 * <ol>
 * <li>Connections of type, {@link PoolMgr.ConnType#USER}, use {@link #connPoolUser} for user authentication and password change operations.</li>
 * <li>Connections of type, {@link PoolMgr.ConnType#ADMIN}, use {@link #connPoolAdmin} and are used for maintenance and interrogation of ldap server objects.</li>
 * <li>Connections of type, {@link PoolMgr.ConnType#LOG}, use {@link #connPoolLog} and used for pulling slapd log data from the server,  </li>
 * </ol>
 * <p/>
 * This class uses <a href="http://www.unboundid.com/products/ldap-sdk/">UnboundID LDAP SDK for Java</a> as client to
 * process LDAP operations.  The UnboundID SDK is distributed under 3 open source licenses and is free to use and distribute in
 * other open source or proprietary software packages.  For more info see, <a href="http://www.unboundid.com/products/ldap-sdk/docs/">LDAP SDK for Java</a>
 * <p/>
 * The {@link ConnectionPool} class derives source code from the Mozilla Java LDAP SDK.  For more
 * info on the license this derived code adheres, see: <a href="http://www.mozilla.org/MPL/MPL-1.1.html/">Mozilla Public License Version 1.1</a>
 * <p/>
 * This class is thread safe.
 * <p/>
 *
 * @author smckinn
 * @created August 30, 2009
 */
public class PoolMgr
{
    // Property names for ldap connection pools:
    private final static String LDAP_ADMIN_POOL_UID = "admin.user";
    private final static String LDAP_ADMIN_POOL_PW = "admin.pw";
    private final static String LDAP_LOG_POOL_UID = "log.admin.user";
    private final static String LDAP_LOG_POOL_PW = "log.admin.pw";
    private final static String LDAP_ADMIN_POOL_MIN = "min.admin.conn";
    private final static String LDAP_ADMIN_POOL_MAX = "max.admin.conn";
    private final static String LDAP_USER_POOL_MIN = "min.user.conn";
    private final static String LDAP_USER_POOL_MAX = "max.user.conn";
    private final static String LDAP_LOG_POOL_MIN = "min.log.conn";
    private final static String LDAP_LOG_POOL_MAX = "max.log.conn";
    private final static String LDAP_VERSION = "ldapVersion";
    private final static String LDAP_CONNECTION_TIMEOUT = "connTimeout";
    private final static String LDAP_DEBUG_FLAG = "debug.ldap.pool";
    private final static String LDAP_HOST = "host";
    private final static String LDAP_PORT = "port";

    // 3 types of connection pools are managed by ths class:
    public enum ConnType
    {
        /**
         * Admin connections used for most of the Fortress internal operations.  Internal bind on connection
         * will be performed using config param found {@link #LDAP_ADMIN_POOL_UID}
         */
        ADMIN,

        /**
         * User connections for non-admin binds and password mods.  Connections will not be bound
         * to user prior to returning to caller.
         */
        USER,

        /**
         * All slapd log operations use this connection pool.   Internal bind on connection
         * will be performed using config param found {@link #LDAP_LOG_POOL_UID}
         */
        LOG
    }

    // Used to synch the getConnection method:
    private static final Object adminSynchLock = new Object();
    private static final Object userSynchLock = new Object();
    private static final Object logSynchLock = new Object();

    // Canaries in the coal mine:
    private static LDAPConnection testAdminConn;
    private static LDAPConnection testUConn;
    private static LDAPConnection testLConn;

    // Logging
    private static final String OCLS_NM = PoolMgr.class.getName();
    private static final Logger log = Logger.getLogger(OCLS_NM);

    // Declare the index for connection pool array:
    private static final int ADMIN = 0;
    private static final int USER = 1;
    private static final int LOG = 2;

    // Contains the adminUserId LDAP connections:
    private static ConnectionPool connPoolAdmin = null;
    private static ConnectionPool connPoolUser = null;
    private static ConnectionPool connPoolLog = null;
    private static ConnectionPool[] connPool = {connPoolAdmin, connPoolUser, connPoolLog};

    // this modules uses openldap pw policies
    private static LDAPControl pwPolicyControl = new LDAPControl(GlobalIds.OPENLDAP_PW_RESPONSE_CONTROL, false, null);
    private static String adminPw;
    private static String adminUserId;
    private static boolean isDebugEnabled = Config.getBoolean(LDAP_DEBUG_FLAG, false);
    private static int connectionTimeout;
    private static String hostName;
    private static int portId;
    private static int ldapRevision;

    // Load all of the static member variables of this class & initialize the admin connection pools:
    static
    {
        try
        {
            adminUserId = Config.getProperty(LDAP_ADMIN_POOL_UID);
            if(EncryptUtil.isEnabled())
            {
                adminPw = EncryptUtil.decrypt(Config.getProperty(LDAP_ADMIN_POOL_PW));
            }
            else
            {
                adminPw = Config.getProperty(LDAP_ADMIN_POOL_PW);
            }

            // Default ldap version to v3:
            ldapRevision = Config.getInt(LDAP_VERSION, 3);
            // Default 10 seconds for client wait on new connection requests from pool:
            connectionTimeout = Config.getInt(LDAP_CONNECTION_TIMEOUT, 10000);
            createAdminPool();
        }

        // If we can't initialize the connection pools we're dead in the water.
        catch (com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException le)
        {
            String error = OCLS_NM + " Static Initializer Block caught com.unboundid.ldap.sdk.migrate.ldapjdk.LdapException=" + le;
            log.fatal(error);
            //throw new java.lang.RuntimeException(error, le);
        }
        catch (Exception e)
        {
            String error = OCLS_NM + " Static Initializer Block caught java.lang.Exception=" + e;
            log.fatal(error);
            e.printStackTrace();
            //throw new java.lang.RuntimeException(error, e);
        }
    }


    /**
     * Method performs an LDAP bind for a user/password combination.  This function is valid
     * if and only if the user entity is a member of the USERS data set.  The LDAP directory
     * will return the OpenLDAP PW Policy control.
     *
     * @param ld       connection to ldap server.
     * @param userId   contains the LDAP dn to the user entry.
     * @param password contains the password in clear text.
     * @return boolean value - true if bind successful, false otherwise.
     * @throws LDAPException in the event of LDAP error.
     */
    public static boolean bind(LDAPConnection ld, String userId, char[] password)
        throws LDAPException
    {
        return bindUser(userId, password, ld);
    }

    /**
     * Close the LDAP connection.
     *
     * @param ld   handle to ldap connection object.
     * @param type specifies the type of connection - ADMIN, USER Or LOG.
     */
    public static void closeConnection(LDAPConnection ld, ConnType type)
    {
        switch (type)
        {
            case ADMIN:
                if (ld != null)
                {
                    connPool[ADMIN].close(ld);
                }
                break;

            case USER:
                if (ld != null)
                {
                    connPool[USER].close(ld);
                }
                break;

            case LOG:
                if (ld != null)
                {
                    connPool[LOG].close(ld);
                }
                break;
        }
    }

    @Deprecated
    private static void closeConnectionx(LDAPConnection ld, ConnType type)
    {
        switch (type)
        {
            case ADMIN:
                if (ld != null && ld.isConnected())
                {
                    connPool[ADMIN].close(ld);
                }
                break;

            case USER:
                if (ld != null && ld.isConnected())
                {
                    connPool[USER].close(ld);
                }
                break;

            case LOG:
                if (ld != null && ld.isConnected())
                {
                    connPool[LOG].close(ld);
                }
                break;
        }
    }

    /**
     * Get a connection to the LDAP server.
     *
     * @param type type specifies the type of connection - ADMIN, USER Or LOG.
     * @return ldap connection.
     * @throws LDAPException
     */
    public static LDAPConnection getConnection(ConnType type)
        throws LDAPException
    {
        LDAPConnection ld = null;
        ConnectionPool cp = null;
        Object lockObj = null;
        String szType = null;
        switch (type)
        {
            case ADMIN:
                cp = connPool[ADMIN];
                lockObj = adminSynchLock;
                szType = "ADMIN";
                break;

            case USER:
                cp = connPool[USER];
                lockObj = userSynchLock;
                szType = "USER";
                break;

            case LOG:
                cp = connPool[LOG];
                lockObj = logSynchLock;
                szType = "LOG";
                break;
        }
        try
        {
            synchronized (lockObj)
            {
                // check the connection pool reference
                if (cp == null)
                {
                    String info = OCLS_NM + ".getConnection " + szType + " initializing pool";
                    log.info(info);
                    cp = recoverPool(type);
                }
                if (connectionTimeout > 0)
                {
                    ld = cp.getConnection(connectionTimeout);
                }
                else
                {
                    ld = cp.getConnection();
                }
                // Did the pool object return a null value?
                if (ld == null)
                {
                    String MSG_HDR = OCLS_NM + ".getConnection " + szType;
                    String warning = MSG_HDR + " detected null connection";
                    log.warn(warning);
                    // Is the canary is still alive?
                    // todo: recheck this sequence, make sure still good.
                    if (!checkConnection(type))
                    {
                        warning += szType + " attempt to recover pool";
                        log.warn(warning);
                        cp = recoverPool(type);
                        ld = cp.getConnection();
                        if (ld == null || !ld.isConnected())
                        {
                            // Give up:
                            String error = MSG_HDR + " could not recover";
                            log.fatal(error);
                            throw new LDAPException(error, LDAPException.LDAP_TIMEOUT);
                        }
                    }
                    // todo: think about this scenario some more.  should it attempt recovery of pool here?
                    else
                    {
                        // Cannot establish a good connection, give up:
                        String error = MSG_HDR + " could not retrieve connection";
                        log.fatal(error);
                        throw new LDAPException(error, LDAPException.CONNECT_ERROR);
                    }
                }
                // Did the pool object return a bad connection?
                else if (!ld.isConnected())
                {
                    String MSG_HDR = OCLS_NM + ".getConnection " + szType;
                    String warning = MSG_HDR + " detected bad connection, retry";
                    log.warn(warning);
                    // attempt to reconnect:
                    ld.connect(hostName, portId);
                    // if admin connection type must bind here using stored creds:
                    if(type.equals(ConnType.ADMIN))
                    {
                        ld.bind(ldapRevision, adminUserId, adminPw);
                    }
                    // Did the reconnect succeed?
                    if (!ld.isConnected())
                    {
                        warning += szType + " cannot reconnect, attempt pool recovery";
                        log.warn(warning);
                        // Try one last ditch effort to recover entire pool.
                        cp = recoverPool(type);
                        ld = cp.getConnection();
                        // Still bad?
                        if (ld == null || !ld.isConnected())
                        {
                            // Give up:
                            String error = MSG_HDR + " recovery failed";
                            log.fatal(error);
                            throw new LDAPException(error, LDAPException.SERVER_DOWN);
                        }
                    }
                }
            }
        }
        catch (LDAPException e)
        {
            String MSG_HDR = OCLS_NM + ".getConnection " + szType;
            String warning = MSG_HDR + " detected bad connection, retry caught LDAPException=" + e;
            log.warn(warning);
            // Todo: Test these scenarios:
            // Did the pool object return a null value or bad conn?
            if (ld != null && !ld.isConnected()
                // Make sure this ldap exception wasn't thrown directly above:
                && e.getLDAPResultCode() != LDAPException.SERVER_DOWN
                && e.getLDAPResultCode() != LDAPException.CONNECT_ERROR
                && e.getLDAPResultCode() != LDAPException.LDAP_TIMEOUT)
            {
                warning += " attempt to reconnect";
                log.warn(warning);
                // attempt reconnect:
                ld.connect(hostName, portId);
                // if admin connection type must bind here using stored creds:
                if(type.equals(ConnType.ADMIN))
                {
                    ld.bind(ldapRevision, adminUserId, adminPw);
                }
                // Did it work?
                if (!ld.isConnected())
                {
                    // Give up:
                    warning = MSG_HDR + " failed to reconnect";
                    log.fatal(warning);
                    throw e;
                }
            }
            else
            {
                // Give up
                warning = MSG_HDR + " failed";
                log.fatal(warning);
                throw e;
            }
        }
        return ld;
    }


    /**
     * Internal function is used to create a new pool of admin connections to ldap server.
     *
     * @throws LDAPException
     */
    private static void createAdminPool()
        throws LDAPException
    {
        String adminUserId = Config.getProperty(LDAP_ADMIN_POOL_UID);
        String adminPw;
        if(EncryptUtil.isEnabled())
        {
            adminPw = EncryptUtil.decrypt(Config.getProperty(LDAP_ADMIN_POOL_PW));
        }
        else
        {
            adminPw = Config.getProperty(LDAP_ADMIN_POOL_PW);
        }

        String host = Config.getProperty(LDAP_HOST, "localhost");
        int port = Config.getInt(LDAP_PORT, 389);
        int min = Config.getInt(LDAP_ADMIN_POOL_MIN, 1);
        int max = Config.getInt(LDAP_ADMIN_POOL_MAX, 10);
        log.info(OCLS_NM + ".createAdminPool min [" + min + "] max [" + max + "] host [" + host + "] port [" + port + "]");
        testAdminConn = new LDAPConnection();
        connPool[ADMIN] = new ConnectionPool(min, max, host, port, adminUserId, adminPw);
        if (isDebugEnabled)
        {
            connPool[ADMIN].setDebug(true);
        }
    }


    /**
     * Internal function is used to create a new pool of user connections to ldap server.
     *
     * @throws LDAPException
     */
    private static void createUserPool()
        throws LDAPException
    {
        String host = Config.getProperty(LDAP_HOST, "localhost");
        int port = Config.getInt(LDAP_PORT, 389);
        int min = Config.getInt(LDAP_USER_POOL_MIN, 1);
        int max = Config.getInt(LDAP_USER_POOL_MAX, 5);
        String adminUserId = Config.getProperty(LDAP_ADMIN_POOL_UID);
        String adminPw;
        if(EncryptUtil.isEnabled())
        {
            adminPw = EncryptUtil.decrypt(Config.getProperty(LDAP_ADMIN_POOL_PW));
        }
        else
        {
            adminPw = Config.getProperty(LDAP_ADMIN_POOL_PW);
        }

        log.info(OCLS_NM + ".createUserPool min [" + min + "] max [" + max + "] host [" + host + "] port [" + port + "]");
        connPool[USER] = new ConnectionPool(min, max, host, port, adminUserId, adminPw);
        if (isDebugEnabled)
        {
            connPool[USER].setDebug(true);
        }
    }

    /**
     * Internal function is used to create a new pool of slapd log connections to ldap server.
     *
     * @throws LDAPException
     */
    private static void createLogPool()
        throws LDAPException
    {
        String logUserId = Config.getProperty(LDAP_LOG_POOL_UID);
        String logUserPw;
        if(EncryptUtil.isEnabled())
        {
            logUserPw = EncryptUtil.decrypt(Config.getProperty(LDAP_LOG_POOL_PW));
        }
        else
        {
            logUserPw = Config.getProperty(LDAP_LOG_POOL_PW);
        }

        String host = Config.getProperty(LDAP_HOST, "localhost");
        int port = Config.getInt(LDAP_PORT, 389);
        int min = Config.getInt(LDAP_LOG_POOL_MIN, 1);
        int max = Config.getInt(LDAP_LOG_POOL_MAX, 5);
        log.info(OCLS_NM + ".createLogPool min [" + min + "] max [" + max + "] host [" + host + "] port [" + port + "]");
        connPool[LOG] = new ConnectionPool(min, max, host, port, logUserId, logUserPw);
        if (isDebugEnabled)
        {
            connPool[LOG].setDebug(true);
        }
    }

    /**
     * Method is used to perform a bind operation on the given connection object.  Connection will contain the
     * password policy control.
     *
     * @param userId   contains the LDAP dn to the user entry.
     * @param password contains the password in clear text.
     * @param ld       contains a valid ldap connection.
     * @return boolean value - true if bind successful, false otherwise.
     * @throws LDAPException in the event of LDAP error.
     */
    private static boolean bindUser(String userId, char[] password, LDAPConnection ld)
        throws LDAPException
    {
        boolean result;
        if (ld == null)
        {
            String error = OCLS_NM + ".bindUser detected null ldap connection";
            log.fatal(error);
            throw new LDAPException(error, LDAPException.CONNECT_ERROR);
        }
        if (GlobalIds.OPENLDAP_IS_PW_POLICY_ENABLED)
        {
            LDAPConstraints lCon = new LDAPConstraints();
            lCon.setServerControls(pwPolicyControl);
            ld.authenticate(ldapRevision, userId, new String(password), lCon);
            result = true;
        }
        else
        {
            ld.authenticate(ldapRevision, userId, new String(password));
            result = true;
        }
        return result;
    }

    /**
     * This method will recover a connection pool in the event the connections become stale due to some network
     * or system issue.
     *
     * @throws LDAPException in the event of ldap system error or the routine fails to reestablish the pool successfully.
     */
    private static ConnectionPool recoverPool(ConnType type) throws LDAPException
    {
        ConnectionPool cp = null;
        switch (type)
        {
            case ADMIN:
                if (connPool[ADMIN] != null)
                {
                    connPool[ADMIN].destroy();
                }
                createAdminPool();
                if (connPool[ADMIN] == null)
                {
                    String error = OCLS_NM + ".recoverPool LDAP_ADMIN_POOL_UID failed";
                    log.fatal(error);
                    throw new LDAPException(error, LDAPException.CONNECT_ERROR);
                }
                cp = connPool[ADMIN];
                break;

            case USER:
                if (connPool[USER] != null)
                {
                    connPool[USER].destroy();
                }
                createUserPool();
                if (connPool[USER] == null)
                {
                    String error = OCLS_NM + ".recoverPool USER failed";
                    log.fatal(error);
                    throw new LDAPException(error, LDAPException.CONNECT_ERROR);
                }
                cp = connPool[USER];
                break;
            case LOG:
                if (connPool[LOG] != null)
                {
                    connPool[LOG].destroy();
                }
                createLogPool();
                if (connPool[LOG] == null)
                {
                    String error = OCLS_NM + ".recoverPool LOG failed";
                    log.fatal(error);
                    throw new LDAPException(error, LDAPException.CONNECT_ERROR);
                }
                cp = connPool[LOG];
                break;
        }
        return cp;
    }

    /**
     * System health method will determine the integrity of a given connection associated with a specified pool is good.
     *
     * @param type specifies the type of connection - ADMIN, USER Or LOG.
     * @return true if connection is good, false otherwise.
     * @throws LDAPException in the event of ldap error.
     */
    private static boolean checkConnection(ConnType type)
        throws LDAPException
    {
        boolean rc = false;
        LDAPConnection conn = null;
        String szType = null;
        switch (type)
        {
            case ADMIN:
                conn = testAdminConn;
                szType = "LDAP_ADMIN_POOL_UID";
                break;
            case USER:
                conn = testUConn;
                szType = "USER";
                break;
            case LOG:
                conn = testLConn;
                szType = "LOG";
                break;
        }
        String info = OCLS_NM + ".checkConnection is checking " + szType + " Connection";
        log.info(info);
        if (conn != null)
        {
            if (conn.isConnected())
            {
                info = OCLS_NM + ".checkConnection -  " + szType + " connection good";
                log.debug(info);
                rc = true;
            }
            else
            {
                info = OCLS_NM + ".checkConnection -  " + szType + " connection bad";
                log.info(info);
                conn.reconnect();
                if (conn.isConnected())
                {
                    info = OCLS_NM + ".checkConnection -  " + szType + " connection reestablished";
                    log.info(info);
                    rc = true;
                }
            }
        }
        info = OCLS_NM + ".checkConnetion status code=" + rc;
        log.info(info);
        return rc;
    }
}