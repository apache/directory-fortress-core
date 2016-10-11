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
package org.apache.directory.fortress.core.samples;


import java.util.Enumeration;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.AccessMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserAdminRole;
import org.apache.directory.fortress.core.model.UserRole;


/**
 * AccessMgrSample JUnit Test. The APIs exercised within this class are used to perform
 * runtime security policy enforcement for data center applications.  These APIs are thread safe and can be
 * executed within high-volume Java transactional environments like Java EE Web,EJB and Service tier applications,
 * Because these APIs require direct access to the LDAP server, it is not recommended that they be called from
 * thick client applications (i.e. Applets) unless the thick client is running on a machine that is "behind the firewall" (or on VPN) that
 * are common to enterprise data center runtime environments.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AccessMgrSample extends TestCase
{
    private static final String CLS_NM = AccessMgrSample.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    public AccessMgrSample( String name )
    {
        super( name );
    }


    /**
     * Run the Fortress AccessMgr samples.
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new AccessMgrSample( "testCheckAccess" ) );
        suite.addTest( new AccessMgrSample( "testSessionPermissions" ) );
        suite.addTest( new AccessMgrSample( "testSessionRoles" ) );
        suite.addTest( new AccessMgrSample( "testAddActiveRoles" ) );
        suite.addTest( new AccessMgrSample( "testDropActiveRoles" ) );
        suite.addTest( new AccessMgrSample( "testDisplayUserSession" ) );

        return suite;
    }


    /**
     * The checkAccess API is used to perform authorization on User.  It will return a 'true' if User is authorized to
     * perform operation or a 'false' if User is not.  This API is useful for performing method or service level authorization
     * within Server side programs.  It is expected that this API will be wrapped by other application Security frameworks
     * i.e. Spring or Java EE to provide fine-grained permission check authorization capabilities to business applications
     * running in the datacenter.
     */
    public static void testCheckAccess()
    {
        String szLocation = ".testCheckAccess";
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );

            // utility function will create an Fortress Session.  The Session contains the user's activated
            // roles along with other related attributes and status information (i.e. password status)
            Session session = createSession( CreateUserSample.TEST_USERID,
                CreateUserSample.TEST_PASSWORD, accessMgr );
            assertNotNull( session );

            for ( int i = 1; i < 6; i++ )
            {
                // Fortress Permissions have an Object name and Operation name.  There is a one to many relationship between
                // objects and operations.  An example is object name "MyDataBaseTable" operations "READ", "WRITE", "DELETE". or object "MyFile" operations "R", "W", "C" or "MyClassName" "methodA", "methodB", "methodC", or "MyPageName.ControlName" "checkOut", "applyDiscount".
                Permission inPerm = new Permission( CreatePermSample.TEST_PERM_OBJECT,
                    CreatePermSample.TEST_PERM_OPERATION_PREFIX + i );
                // method will return a 'true' if authorized or 'false' if not.
                boolean result = accessMgr.checkAccess( session, inPerm );
                assertTrue( szLocation, result );
                LOG.info( szLocation + " user [" + session.getUserId() + "] permission object ["
                    + inPerm.getObjName() + "] operation name [" + inPerm.getOpName() + "] success" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     * The sessionPermissions API is useful for GUI programs that need to cache all of the User's Permissions in the
     * HTTP Session or application cache.  This is useful when providing access control lists for menu items and other
     * controls that sometimes need to check authorizations on.  This API will return all permissions that are granted
     * to User's activated Roles along with Permissions that have been granted directly to the User entity itself.
     */
    public static void testSessionPermissions()
    {
        String szLocation = ".testSessionPermissions";
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );

            // utility function will create an Fortress Session.  The Session contains the user's activated
            // roles along with other related attributes and status information (i.e. password status)
            Session session = createSession( CreateUserSample.TEST_USERID,
                CreateUserSample.TEST_PASSWORD, accessMgr );
            assertNotNull( session );
            List<Permission> perms = accessMgr.sessionPermissions( session );
            assertNotNull( perms );
            assertTrue( szLocation + " list check, expected: 5, actual:" + perms.size(), perms.size() == 5 );

            // iterate over expected permissions to make sure they are returned from sessionPermissions API.
            for ( int i = 1; i < 6; i++ )
            {
                // A Permission consists of an object name and operation name.
                Permission checkPerm = new Permission( CreatePermSample.TEST_PERM_OBJECT,
                    CreatePermSample.TEST_PERM_OPERATION_PREFIX + i );
                boolean result = accessMgr.checkAccess( session, checkPerm );
                assertTrue( szLocation, result );
                LOG.info( szLocation + " user [" + session.getUserId() + "] permission object ["
                    + checkPerm.getObjName() + "] operation name [" + checkPerm.getOpName() + "] success" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     * The RBAC Session can be interrogated to return the list of all activated Roles within a User's Session.  The API
     * will cache these Roles in the User's Session object.  The Roles will also include temporal data that is used to
     * enforce the day, date and time for which a given Role may be placed in the User's Session.
     */
    public static void testSessionRoles()
    {
        String szLocation = ".testSessionRoles";
        User inUser = new User( CreateUserSample.TEST_USERID );
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            // utility function will create an Fortress Session.  The Session contains the user's activated
            // roles along with other related attributes and status information (i.e. password status)
            Session session = createSession( CreateUserSample.TEST_USERID,
                CreateUserSample.TEST_PASSWORD, accessMgr );
            // A null Session would be a bug and should never happen.  Fortress will throw a SecurityException if it cannot create.
            assertNotNull( session );
            // Get the activated Roles from the Session.
            List<UserRole> uRoles = accessMgr.sessionRoles( session );
            // The list of Roles could be null if User has not been assigned any or if all assigned failed activation checks.
            assertNotNull( uRoles );
            // Test to see that the list size is same as expected.
            assertTrue( szLocation + " list check, expected: 10, actual:" + uRoles.size(), uRoles.size() == 10 );

            // Test to ensure that all of the roles activated are returned in the uRoles list.  In a real
            // program this would not be necessary.
            for ( int i = 1; i < 11; i++ )
            {
                UserRole inUserRole = new UserRole( inUser.getUserId(), CreateRoleSample.TEST_ROLE_PREFIX + i );
                assertTrue(
                    szLocation + " contains check userId [" + inUserRole.getUserId() + "] role ["
                        + inUserRole.getName() + "]", uRoles.contains( inUserRole ) );
                LOG.info( szLocation + " userId [" + inUserRole.getUserId() + "] activated role ["
                    + inUserRole.getName() + "] found in session" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     * The addActivateRole API allows only Roles that have been assigned to a given User to be activated in their
     * RBAC Session.  The API will also ensure that a given Role has passed its constraint tests which include
     * Static Separation of Duty (SSD) and RBAC Role temporal constraint validations.
     */
    public static void testAddActiveRoles()
    {
        String szLocation = ".testAddActiveRoles";
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            // authenticate will check the password but will not activated any roles into Session.
            Session session = authenticate( CreateUserSample.TEST_USERID, CreateUserSample.TEST_PASSWORD,
                accessMgr );
            assertNotNull( session );
            // now, activate roles into User's Session one at a time:
            for ( int i = 1; i < 11; i++ )
            {
                UserRole addUserRole = new UserRole( CreateUserSample.TEST_USERID, CreateRoleSample.TEST_ROLE_PREFIX
                    + i );
                accessMgr.addActiveRole( session, addUserRole );
                LOG.info( szLocation + " userId [" + addUserRole.getUserId() + "] activated role ["
                    + addUserRole.getName() + "] added to session" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     * RBAC compliant systems allow User Roles to be activated and deactivated from their Session.  This facilitates
     * the principle of least privilege which prescribes only giving User's as much capability as they need to complete
     * their job duties.  This means not all Roles that a User may be authorized to activated will necessarily be active
     * at any one point in time.  This allows for separation of duty restrictions to be enforced.
     */
    public static void testDropActiveRoles()
    {
        String szLocation = ".testDropActiveRoles";
        User inUser = new User( CreateUserSample.TEST_USERID );
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            // Calling createSession and not setting any roles on User beforehand will attempt to activate all assigned Roles:
            Session session = createSession( CreateUserSample.TEST_USERID,
                CreateUserSample.TEST_PASSWORD, accessMgr );
            assertNotNull( session );
            // now, drop roles from User's Session one at a time:
            for ( int i = 1; i < 11; i++ )
            {
                UserRole dropUserRole = new UserRole( inUser.getUserId(), CreateRoleSample.TEST_ROLE_PREFIX + i );
                accessMgr.dropActiveRole( session, dropUserRole );
                LOG.info( szLocation + " userId [" + dropUserRole.getUserId() + "] deactivated role ["
                    + dropUserRole.getName() + "] removed from session" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     * This test will display all of the User Session attributes to the System out of test machine.  It is intended
     * to demonstrate what data is carried within a User's Fortress Session object.
     */
    public static void testDisplayUserSession()
    {
        String szLocation = ".testDisplayUserSession";
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            // utility function will create an Fortress Session.  The Session contains the user's activated
            // roles along with other related attributes and status information (i.e. password status)
            Session session = createSession( CreateUserSample.TEST_USERID,
                CreateUserSample.TEST_PASSWORD, accessMgr );
            assertNotNull( session );
            User user = accessMgr.getUser( session );
            assertNotNull( user );
            LOG.info( szLocation );
            LOG.info( "S   UID  [" + session.getUserId() + "]:" );
            LOG.info( "S   IID  [" + session.getInternalUserId() + "]" );
            LOG.info( "S   ERR  [" + session.getErrorId() + "]" );
            LOG.info( "S   WARN [" + session.getWarnings() + "]" );
            LOG.info( "S   MSG  [" + session.getMsg() + "]" );
            LOG.info( "S   EXP  [" + session.getExpirationSeconds() + "]" );
            LOG.info( "S   GRAC [" + session.getGraceLogins() + "]" );
            LOG.info( "S   AUTH [" + session.isAuthenticated() + "]" );
            LOG.info( "S   LAST [" + session.getLastAccess() + "]" );
            LOG.info( "S   SID  [" + session.getSessionId() + "]" );
            LOG.info( "------------------------------------------" );
            LOG.info( "U   UID  [" + user.getUserId() + "]" );
            LOG.info( "U   IID  [" + user.getInternalId() + "]" );
            LOG.info( "U   CN   [" + user.getCn() + "]" );
            LOG.info( "U   DESC [" + user.getDescription() + "]" );
            LOG.info( "U   OU   [" + user.getOu() + "]" );
            LOG.info( "U   SN   [" + user.getSn() + "]" );
            LOG.info( "U   BDTE [" + user.getBeginDate() + "]" );
            LOG.info( "U   EDTE [" + user.getEndDate() + "]" );
            LOG.info( "U   BLDT [" + user.getBeginLockDate() + "]" );
            LOG.info( "U   ELDT [" + user.getEndLockDate() + "]" );
            LOG.info( "U   DMSK [" + user.getDayMask() + "]" );
            LOG.info( "U   TO   [" + user.getTimeout() + "]" );
            LOG.info( "U   REST [" + user.isReset() + "]" );
            if ( user.getProperties() != null && user.getProperties().size() > 0 )
            {
                int ctr = 0;
                for ( Enumeration e = user.getProperties().propertyNames(); e.hasMoreElements(); )
                {
                    String key = ( String ) e.nextElement();
                    String val = user.getProperty( key );
                    LOG.info( "U   PROP[" + ctr++ + "]=" + key + " VAL=" + val );
                }
            }

            List<UserRole> roles = session.getRoles();
            if ( roles != null )
            {
                for ( int i = 0; i < roles.size(); i++ )
                {
                    UserRole ur = roles.get( i );
                    LOG.info( "    USER ROLE[" + i + "]:" );
                    LOG.info( "        role name [" + ur.getName() + "]" );
                    LOG.info( "        begin time [" + ur.getBeginTime() + "]" );
                    LOG.info( "        end time [" + ur.getEndTime() + "]" );
                    LOG.info( "        begin date [" + ur.getBeginDate() + "]" );
                    LOG.info( "        end date [" + ur.getEndDate() + "]" );
                    LOG.info( "        begin lock [" + ur.getBeginLockDate() + "]" );
                    LOG.info( "        end lock [" + ur.getEndLockDate() + "]" );
                    LOG.info( "        day mask [" + ur.getDayMask() + "]" );
                    LOG.info( "        time out [" + ur.getTimeout() + "]" );
                }
            }

            List<UserAdminRole> aRoles = session.getAdminRoles();
            if ( aRoles != null )
            {
                for ( int i = 0; i < aRoles.size(); i++ )
                {
                    UserAdminRole ur = aRoles.get( i );
                    LOG.info( "    USER ADMIN ROLE[" + i + "]:" );
                    LOG.info( "        admin role name [" + ur.getName() + "]" );
                    LOG.info( "        OsU [" + ur.getOsUSet() + "]" );
                    LOG.info( "        OsP [" + ur.getOsPSet() + "]" );
                    LOG.info( "        begin range [" + ur.getBeginRange() + "]" );
                    LOG.info( "        end range [" + ur.getEndRange() + "]" );
                    LOG.info( "        begin time [" + ur.getBeginTime() + "]" );
                    LOG.info( "        end time [" + ur.getEndTime() + "]" );
                    LOG.info( "        begin date [" + ur.getBeginDate() + "]" );
                    LOG.info( "        end date [" + ur.getEndDate() + "]" );
                    LOG.info( "        begin lock [" + ur.getBeginLockDate() + "]" );
                    LOG.info( "        end lock [" + ur.getEndLockDate() + "]" );
                    LOG.info( "        day mask [" + ur.getDayMask() + "]" );
                    LOG.info( "        time out [" + ur.getTimeout() + "]" );
                }
            }

            java.util.Properties jProps = System.getProperties();
            if ( jProps != null && jProps.size() > 0 )
            {
                int ctr = 0;
                for ( Enumeration e = jProps.propertyNames(); e.hasMoreElements(); )
                {
                    String key = ( String ) e.nextElement();
                    String val = jProps.getProperty( key );
                    LOG.info( "J   PROP[" + ctr++ + "]=" + key + " VAL=" + val );
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     * @param userId   String contains case insensitive userId field.
     * @param password String contains case sensitive, clear text password field.
     * @return User RBAC Session that is used for subsequent AccessMgr API calls.
     */
    private static Session createSession( String userId, String password, AccessMgr accessMgr )
    {
        String szLocation = ".createSession";
        Session session = null;

        try
        {
            User user = new User( userId, password );
            // These properties will be persisted within User's audit trail in OpenLDAP:
            user.addProperty( "system.user.name", System.getProperty( "user.name" ) );
            //user.addProperty("system.timezone VAL", System.getProperty("user.timezone VAL"));
            user.addProperty( "system.country", System.getProperty( "user.country" ) );

            // utility function will create an Fortress Session.  The Session contains the user's activated
            // roles along with other related attributes and status information (i.e. password status)
            session = accessMgr.createSession( user, false );
            LOG.info( szLocation + " successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }

        return session;
    }


    /**
     * Test Utility wraps OpenAcccessManager createSession API.
     *
     * @param userId          String contains case insensitive userId field.
     * @param password        String contains case sensitive, clear text password field.
     * @param activationRoles array of Role names targeted for activation into User's RBAC Session.
     * @return User RBAC Session that is used for subsequent AccessMgr API calls.
     */
    private static Session createSession( String userId, String password, String[] activationRoles, AccessMgr accessMgr )
    {
        String szLocation = ".createSession";
        Session session = null;
        try
        {
            User user = new User( userId, password, activationRoles );
            user.addProperty( "system.user.name", System.getProperty( "user.name" ) );
            //user.addProperty("system.timezone VAL", System.getProperty("user.timezone VAL"));
            user.addProperty( "system.country", System.getProperty( "user.country" ) );

            // Create an Fortress Session.  The Session contains the user's activated
            // roles along with other related attributes and status information (i.e. password status)
            session = accessMgr.createSession( user, false );
            LOG.info( szLocation + " with roles successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                szLocation + " with roles caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
        return session;
    }


    /**
     * The authenticate API is used for use cases where RBAC authorization is not required.  This API will authenticate
     * the User's password and will check password policies but will not activate User's Roles into the return Session.
     *
     * @param userId   String contains case insensitive userId field.
     * @param password String contains case sensitive, clear text password field.
     * @return User Session that has no Roles activated thus will fail checkAccess and sessionPermission calls.
     */
    private static Session authenticate( String userId, String password, AccessMgr accessMgr )
    {
        String szLocation = ".authenticate";
        Session session = null;
        try
        {
            // authenticate will check the password but will not activated any roles into Session.
            session = accessMgr.authenticate( userId, password );
            LOG.info( szLocation + " successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
        return session;
    }
}
