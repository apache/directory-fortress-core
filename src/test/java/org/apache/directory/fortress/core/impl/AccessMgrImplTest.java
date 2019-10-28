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


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.SDSet;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.model.RoleConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.AccessMgrFactory;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.PwPolicyMgr;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.util.LogUtil;


/**
 * AccessMgrImpl Tester.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AccessMgrImplTest extends TestCase
{
    private static final String CLS_NM = AccessMgrImplTest.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        //suite.addTest(new AccessMgrImplTest("testDropActiveRole"));
/*
        suite.addTest( new AdminMgrImplTest( "testResetPassword" ) );
        suite.addTest( new AccessMgrImplTest( "testAuthenticateReset" ) );
        suite.addTest( new AdminMgrImplTest( "testChangePassword" ) );
        suite.addTest( new AccessMgrImplTest( "testAuthenticate" ) );
        suite.addTest( new AdminMgrImplTest( "testLockUserAccount" ) );
        suite.addTest( new AccessMgrImplTest( "testAuthenticateLocked" ) );
        suite.addTest( new AdminMgrImplTest( "testUnlockUserAccount" ) );
        suite.addTest( new AccessMgrImplTest( "testCheckAccess" ) );
*/
        suite.addTest( new AccessMgrImplTest( "testAbacConstraintsRole" ) );

        return suite;
    }


    public AccessMgrImplTest( String name )
    {
        super( name );
    }


    public void setUp() throws Exception
    {
        super.setUp();
    }


    public void tearDown() throws Exception
    {
        super.tearDown();
    }


    public void testGetSession() throws Exception
    {
        //TODO: Test goes here...
    }


    public void testGetToken() throws Exception
    {
        //TODO: Test goes here...
    }


    public void testGetUserId()
    {
        // public String getUserId(Session, session)
        getUsers( "GET-USRIDS TU1_UPD", UserTestData.USERS_TU1_UPD );
        getUsers( "GET-USRIDS TU3", UserTestData.USERS_TU3 );
        getUsers( "GET-USRIDS TU4", UserTestData.USERS_TU4 );
    }


    /**
     * @param msg
     * @param uArray
     */
    public static void getUserIds( String msg, String[][] uArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                Session session = accessMgr.authenticate( user.getUserId(), user.getPassword() );
                assertNotNull( session );
                String userId = accessMgr.getUserId( session );
                assertTrue( "getUserIds failed compare found userId [" + userId + "] valid userId ["
                    + UserTestData.getUserId( usr ) + "]", userId.equalsIgnoreCase( UserTestData.getUserId( usr ) ) );
            }
            LOG.debug( "getUserIds successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "getUserIds: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testGetUser()
    {
        // public User getUser(Session, session)
        getUsers( "GET-USRS TU1_UPD", UserTestData.USERS_TU1_UPD );
        getUsers( "GET-USRS TU3", UserTestData.USERS_TU3 );
        getUsers( "GET-USRS TU4", UserTestData.USERS_TU4 );
    }


    /**
     * @param msg
     * @param uArray
     */
    public static void getUsers( String msg, String[][] uArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                Session session = accessMgr.createSession( user, false );
                assertNotNull( session );
                user = accessMgr.getUser( session );
                UserTestData.assertEquals( user, usr );
            }
            LOG.debug( "getUsers successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "getUsers: failed with SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    public void testAuthenticate()
    {
        // public Session authenticate(String userId, String password)
        authenticateUsers( "AUTH-USRS TU1_UPD", UserTestData.USERS_TU1_UPD, 1 );
        authenticateUsers( "AUTH-USRS TU2_CHG", UserTestData.USERS_TU2_CHG, 1 );
        authenticateUsers( "AUTH-USRS TU3", UserTestData.USERS_TU3, 1 );
        authenticateUsers( "AUTH-USRS TU4", UserTestData.USERS_TU4, 1 );
    }


    /**
     * @param msg
     * @param uArray
     * @param multiplier
     */
    private static void authenticateUsers( String msg, String[][] uArray, int multiplier )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                Session session = accessMgr.authenticate( user.getUserId(), user.getPassword() );
                assertNotNull( session );
                // todo: need to test to ensure roles are not added to session.
                // now try negative test case:
                try
                {
                    accessMgr.authenticate( user.getUserId(), "wrongpw" );
                    fail( "authenticateUsers failed negative test" );
                }
                catch ( SecurityException se )
                {
                    assertTrue( "authenticateUsers reset excep id check",
                        se.getErrorId() == GlobalErrIds.USER_PW_INVLD );
                    // pass
                }
            }
            LOG.debug( "authenticateUsers successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "authenticateUsers: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testAuthenticateLocked()
    {
        // public Session authenticate(String userId, String password)
        authenticateLockedUsers( "AUTH-L-USRS TU1_UPD", UserTestData.USERS_TU1_UPD );
        authenticateLockedUsers( "AUTH-L-USRS TU3", UserTestData.USERS_TU3 );
        authenticateLockedUsers( "AUTH-L-USRS TU4", UserTestData.USERS_TU4 );
    }


    /**
     * @param msg
     * @param uArray
     */
    private static void authenticateLockedUsers( String msg, String[][] uArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                // now try negative test case:
                try
                {
                    accessMgr.authenticate( user.getUserId(), user.getPassword() );
                    fail( CLS_NM + ".authenticateLockedUsers failed test" );
                }
                catch ( SecurityException se )
                {
                    assertTrue( CLS_NM + "authenticateLockedUsers reset excep id check",
                        se.getErrorId() == GlobalErrIds.USER_PW_LOCKED );
                    // pass
                    //LOG.error("locked=" + se.getMsgid() + " msg=" + se.getMessage());
                }
            }
            LOG.debug( "authenticateLockedUsers successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "authenticateLockedUsers: failed with SecurityException rc=" + ex.getErrorId()
                + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testAuthenticateReset()
    {
        // public Session authenticate(String userId, String password)
        authenticateResetUsers( "AUTH-R-USRS TU2_RST", UserTestData.USERS_TU2_RST, PolicyTestData.POLICIES_TP2[0] );
    }


    /**
     * @param msg
     * @param uArray
     */
    private static void authenticateResetUsers( String msg, String[][] uArray, String[] plcy )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            PwPolicyMgr policyMgr = PswdPolicyMgrImplTest.getManagedPswdMgr();
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                // update this user with pw policy that requires change after reset:
                policyMgr.updateUserPolicy( user.getUserId(), PolicyTestData.getName( plcy ) );
                // now try negative test case:
                try
                {
                    accessMgr.authenticate( user.getUserId(), user.getPassword() );
                    //accessMgr.authenticate( user.getUserId(), user.getPassword() );
                    fail( CLS_NM + ".authenticateResetUsers failed test" );
                }
                catch ( SecurityException se )
                {
                    assertTrue( CLS_NM + "authenticateResetUsers reset excep id check",
                        se.getErrorId() == GlobalErrIds.USER_PW_RESET );
                    // pass
                }
            }
            LOG.debug( "authenticateResetUsers successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "authenticateResetUsers: failed with SecurityException rc=" + ex.getErrorId()
                + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testCreateSession()
    {
        // public Session createSession(User user, boolean isTrusted)
        createSessions( "CREATE-SESS TU1_UPD TR1", UserTestData.USERS_TU1_UPD, RoleTestData.ROLES_TR1 );
        createSessions( "CREATE-SESS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3 );
        createSessions( "CREATE-SESS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2 );
    }


    /**
     * @param msg
     * @param uArray
     * @param rArray
     */
    public static void createSessions( String msg, String[][] uArray, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                Session session = accessMgr.createSession( user, false );
                assertNotNull( session );
                String userId = accessMgr.getUserId( session );
                assertTrue( CLS_NM + ".createSessions failed compare found userId [" + userId + "] valid userId ["
                    + UserTestData.getUserId( usr ) + "]", userId.equalsIgnoreCase( UserTestData.getUserId( usr ) ) );
                UserTestData.assertEquals( user, usr );
                List<UserRole> uRoles = session.getRoles();
                assertNotNull( uRoles );
                assertEquals( CLS_NM + ".createSessions user role check failed list size user [" + user.getUserId()
                    + "]", rArray.length, uRoles.size() );
                for ( String[] rle : rArray )
                {
                    assertTrue( CLS_NM + ".createSessions failed role search USER [" + user.getUserId() + "] ROLE1 ["
                        + RoleTestData.getName( rle ) + "] should be present",
                        uRoles.contains( RoleTestData.getUserRole( UserTestData.getUserId( usr ), rle ) ) );
                }

                // now try negative test case:
                try
                {
                    User userBad = new User( user.getUserId(), "badpw" );
                    accessMgr.createSession( userBad, false );
                    fail( CLS_NM + ".createSessions failed negative test" );
                }
                catch ( SecurityException se )
                {
                    assertTrue( CLS_NM + "createSessions excep id check", se.getErrorId() == GlobalErrIds.USER_PW_INVLD );
                    // pass
                }
            }
            LOG.debug( "createSessions successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "createSessions: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testCreateSessionWithRole()
    {
        // public Session createSession(User user, boolean isTrusted)
        createSessionsWithRoles( "CR-SESS-WRLS TU1_UPD TR1", UserTestData.USERS_TU1_UPD, RoleTestData.ROLES_TR1 );
        createSessionsWithRoles( "CR-SESS-WRLS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3 );
        createSessionsWithRoles( "CR-SESS-WRLS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2 );
    }


    /**
     * @param msg
     * @param uArray
     * @param rArray
     */
    public static void createSessionsWithRoles( String msg, String[][] uArray, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                List<UserRole> rlsRequested = new ArrayList<>();
                int cnt = 0;
                for ( String[] rle : rArray )
                {
                    rlsRequested.add( RoleTestData.getUserRole( user.getUserId(), rle ) );
                    user.setRoles( rlsRequested );
                    Session session = accessMgr.createSession( user, false );
                    assertTrue( CLS_NM + ".createSessionsWithRoles failed role search USER [" + user.getUserId()
                        + "] CNT [" + ++cnt + "] size [" + session.getRoles().size() + "]", cnt == session.getRoles()
                        .size() );
                    String userId = accessMgr.getUserId( session );
                    assertTrue( CLS_NM + ".createSessionsWithRoles failed compare found userId [" + userId
                        + "] valid userId [" + UserTestData.getUserId( usr ) + "]",
                        userId.equalsIgnoreCase( UserTestData.getUserId( usr ) ) );
                    UserTestData.assertEquals( user, usr );
                }
            }
            LOG.debug( "createSessionsWithRoles successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "createSessionsWithRoles: failed with SecurityException rc=" + ex.getErrorId()
                + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testCreateSessionWithRolesTrusted()
    {
        // public Session createSession(User user, boolean isTrusted)
        createSessionsWithRolesTrusted( "CR-SESS-WRLS-TRST TU1_UPD TR1", UserTestData.USERS_TU1_UPD,
            RoleTestData.ROLES_TR1 );
        createSessionsWithRolesTrusted( "CR-SESS-WRLS-TRST TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3 );
        createSessionsWithRolesTrusted( "CR-SESS-WRLS-TRST TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2 );
    }


    /**
     * @param msg
     * @param uArray
     * @param rArray
     */
    public static void createSessionsWithRolesTrusted( String msg, String[][] uArray, String[][] rArray )
    {
        LogUtil.logIt( msg );
        
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                List<UserRole> rlsRequested = new ArrayList<>();
                int cnt = 0;
                for ( String[] rle : rArray )
                {
                    rlsRequested.add( RoleTestData.getUserRole( user.getUserId(), rle ) );
                    user.setRoles( rlsRequested );
                    Session session = accessMgr.createSession( user, true );
                    assertTrue( CLS_NM + ".createSessionsWithRolesTrusted failed role search USER [" + user.getUserId()
                        + "] CNT [" + ++cnt + "] size [" + session.getRoles().size() + "]", cnt == session.getRoles()
                        .size() );
                    String userId = accessMgr.getUserId( session );
                    assertTrue( CLS_NM + ".createSessionsWithRolesTrusted failed compare found userId [" + userId
                        + "] valid userId [" + UserTestData.getUserId( usr ) + "]",
                        userId.equalsIgnoreCase( UserTestData.getUserId( usr ) ) );
                    UserTestData.assertEquals( user, usr );
                }
            }
            LOG.debug( "createSessionsWithRolesTrusted successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "createSessionsWithRolesTrusted: failed with SecurityException rc=" + ex.getErrorId()
                + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testCreateSessionTrusted()
    {
        // public Session createSession(User user, boolean isTrusted)
        createSessionsTrusted( "CR-SESS-TRST TU1_UPD TR1", UserTestData.USERS_TU1_UPD, RoleTestData.ROLES_TR1 );
        createSessionsTrusted( "CR-SESS-TRST TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3 );
        createSessionsTrusted( "CR-SESS-TRST TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2 );
    }


    /**
     * @param msg
     * @param uArray
     * @param rArray
     */
    public static void createSessionsTrusted( String msg, String[][] uArray, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                Session session = accessMgr.createSession( user, true );
                assertNotNull( session );
                String userId = accessMgr.getUserId( session );
                assertTrue( CLS_NM + ".createSessionsTrusted failed compare found userId [" + userId
                    + "] valid userId [" + UserTestData.getUserId( usr ) + "]",
                    userId.equalsIgnoreCase( UserTestData.getUserId( usr ) ) );
                UserTestData.assertEquals( user, usr );
                List<UserRole> uRoles = session.getRoles();
                assertNotNull( uRoles );
                assertEquals(
                    CLS_NM + ".createSessionsTrusted user role check failed list size user [" + user.getUserId() + "]",
                    rArray.length, uRoles.size() );
                for ( String[] rle : rArray )
                {
                    assertTrue( CLS_NM + ".createSessionsTrusted failed role search USER [" + user.getUserId()
                        + "] ROLE1 [" + RoleTestData.getName( rle ) + "] should be present",
                        uRoles.contains( RoleTestData.getUserRole( UserTestData.getUserId( usr ), rle ) ) );
                }

                // now try negative test case:
                try
                {
                    User badUser = new User( user.getUserId() + "wrong" );
                    accessMgr.createSession( badUser, true );
                    fail( CLS_NM + ".createSessionsTrusted failed negative test" );
                }
                catch ( SecurityException se )
                {
                    assertTrue( CLS_NM + "createSessionsTrusted excep id check",
                        se.getErrorId() == GlobalErrIds.USER_NOT_FOUND );
                    // pass
                }
            }
            LOG.debug( "createSessionsTrusted successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "createSessionsTrusted: failed with SecurityException rc=" + ex.getErrorId()
                + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void createSessionsDSD()
    {
        // public Session createSession(User user, boolean isTrusted)
        createSessionsDSD( "CR-SESS-DSD TU12 DSD_T1", UserTestData.USERS_TU12_DSD, RoleTestData.DSD_T1 );
        createSessionsDSD( "CR-SESS-DSD TU13 DSD_T4_B", UserTestData.USERS_TU13_DSD_HIER, RoleTestData.DSD_T4_B );
        createSessionsDSD( "CR-SESS-DSD TU14 DSD_T5_B", UserTestData.USERS_TU14_DSD_HIER, RoleTestData.DSD_T5_B );
        createSessionsDSD( "CR-SESS-DSD TU15 DSD_T6_C", UserTestData.USERS_TU15_DSD_HIER, RoleTestData.DSD_T6_C );
        createSessionsDSD( "CR-SESS-DSD TU21 DSD_T8_BRUNO", UserTestData.USERS_TU21_DSD_BRUNO,
            RoleTestData.DSD_T8_BRUNO );
    }


    /**
     *
     * @param msg
     * @param uArray
     * @param dsdArray
     */
    public static void createSessionsDSD( String msg, String[][] uArray, String[][] dsdArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            int i = 0;
            for ( String[] usr : uArray )
            {
                SDSet dsd = RoleTestData.getSDSet( dsdArray[i++] );
                User user = UserTestData.getUser( usr );
                Session session = accessMgr.createSession( user, false );
                assertNotNull( session );
                String userId = accessMgr.getUserId( session );
                assertTrue( CLS_NM + ".createSessionsDSD failed compare found userId [" + userId + "] valid userId ["
                    + UserTestData.getUserId( usr ) + "]", userId.equalsIgnoreCase( UserTestData.getUserId( usr ) ) );
                UserTestData.assertEquals( user, usr );
                List<UserRole> uRoles = session.getRoles();
                assertNotNull( uRoles );
                // was the number of members in test DSD greater than the cardinality?
                if ( dsd.getMembers().size() < dsd.getCardinality() )
                {
                    assertEquals(
                        CLS_NM + ".createSessionsDSD role list size check failed user-role user [" + user.getUserId()
                            + "]", dsd.getMembers().size(), uRoles.size() );
                }
                else
                {
                    assertEquals(
                        CLS_NM + ".createSessionsDSD role cardinality check failed user-role list size user ["
                            + user.getUserId() + "] dsd set [" + dsd.getName() + "] card [" + dsd.getCardinality()
                            + "] listsize [" + uRoles.size() + "]", dsd.getCardinality() - 1, uRoles.size() );
                }
            }
            LOG.debug( "createSessionsDSD successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "createSessionsDSD: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testCreateSessionHier()
    {
        // public Session createSession(User user, boolean isTrusted)
        createSessionsHier( "CREATE-SESS-HIER TU18 TR6 DESC", UserTestData.USERS_TU18U_TR6_DESC );
        createSessionsHier( "CREATE-SESS-HIER TU19U TR7 ASC", UserTestData.USERS_TU19U_TR7_ASC );
    }


    /**
     *
     * @param msg
     * @param uArray
     */
    public static void createSessionsHier( String msg, String[][] uArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                Session session = accessMgr.createSession( user, false );
                assertNotNull( session );
                String userId = accessMgr.getUserId( session );
                assertTrue( CLS_NM + ".createSessionsHier failed compare found userId [" + userId + "] valid userId ["
                    + UserTestData.getUserId( usr ) + "]", userId.equalsIgnoreCase( UserTestData.getUserId( usr ) ) );
                UserTestData.assertEquals( user, usr );

                // Get the authorized roles for this user:
                Collection<String> authZRoles = UserTestData.getAuthorizedRoles( usr );

                // If there are any assigned roles, add them to list of authorized.
                Set<String> asgnRoles = UserTestData.getAssignedRoles( usr );
                assertNotNull( asgnRoles );
                assertTrue( asgnRoles.size() > 0 );

                for ( String asgnRole : asgnRoles )
                {
                    authZRoles.add( asgnRole );
                }

                Set<String> actualRoles = accessMgr.authorizedRoles( session );
                assertNotNull( actualRoles );
                assertEquals(
                    CLS_NM + ".createSessionsHier authorized roles list size test case failed for [" + user.getUserId()
                        + "]", authZRoles.size(), actualRoles.size() );
                for ( String name : authZRoles )
                {
                    assertTrue( CLS_NM + ".createSessionsHier authorized roles compare test case failed for USER ["
                        + user.getUserId() + "] expect role [" + name + "] nout found", actualRoles.contains( name ) );
                }
            }
            LOG.debug( "createSessionsHier successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "createSessionsHier: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testCheckAccess()
    {
        // public boolean checkAccess(String object, String operation, Session session)
        checkAccess( "CHCK-ACS TU1_UPD TO1 TOP1 ", UserTestData.USERS_TU1_UPD, PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1, PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3 );
        checkAccess( "CHCK-ACS TU3 TO3 TOP1 ", UserTestData.USERS_TU3, PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3,
            PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2 );
        checkAccess( "CHCK-ACS TU4 TO4 TOP1 ", UserTestData.USERS_TU4, PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2,
            PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3 );
    }


    public static void checkAccess( String msg, String[][] uArray, String[][] oArray, String[][] opArray,
        String[][] oArrayBad, String[][] opArrayBad )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                Session session = accessMgr.createSession( user, false );
                assertNotNull( session );
                int i = 0;
                for ( String[] obj : oArray )
                {
                    int j = 0;
                    for ( String[] op : opArray )
                    {
                        Permission goodPerm;
                        if( StringUtils.isNotEmpty( PermTestData.getObjId( opArray[j] ) ) )
                        {
                            // with an objectId:
                            goodPerm = new Permission(
                                PermTestData.getName( obj ),
                                PermTestData.getName( op ),
                                PermTestData.getObjId( opArray[j] ) );
                        }
                        else
                        {
                            // without an objectId:
                            goodPerm = new Permission(
                                PermTestData.getName( obj ),
                                PermTestData.getName( op ) );
                        }

                        // Positive test case, call checkAccess method, should return 'true':
                        assertTrue( CLS_NM + ".checkAccess failed userId [" + user.getUserId() + "] Perm objName [" +
                                PermTestData.getName( obj ) + "] operationName [" + PermTestData.getName( op ) + "]",
                            accessMgr.checkAccess( session, goodPerm ) );
                        Permission badPerm;
                        if( StringUtils.isNotEmpty( PermTestData.getObjId( opArrayBad[j] ) ) )
                        {
                            // with an objectId:
                            badPerm = new Permission(
                                PermTestData.getName( oArrayBad[i] ),
                                PermTestData.getName( opArrayBad[j] ),
                                PermTestData.getObjId( opArrayBad[j] ) );
                        }
                        else
                        {
                            // without an objectId:
                            badPerm = new Permission(
                                PermTestData.getName( oArrayBad[i] ),
                                PermTestData.getName( opArrayBad[j] ) );
                        }
                        //LOG.warn("Assert False userId [" + user.getUserId() + "], perm: " + badPerm);
                        // Negative test case, call checkAccess method again, should return 'false':
                        assertFalse( CLS_NM + ".checkAccess failed userId [" + user.getUserId() + "] Perm objName [" +
                            PermTestData.getName( oArrayBad[i] ) + "] operationName [" + PermTestData.getName(
                            opArrayBad[j] ) + "]", accessMgr.checkAccess( session, badPerm ) );
                        j++;
                    }
                    i++;
                }
            }
            LOG.debug( "checkAccess successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "checkAccess: failed with SecurityException rc=" + ex.getErrorId() + ", " +
                "msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testSessionPermission()
    {
        // public List<Permission> sessionPermissions(Session session)
        // public static void sessionPermissions(String msg, String[][] uArray, String[][] oArray, String[][] opArray)
        sessionPermissions( "SESS-PRMS TU1_UPD TO1 TOP1 ", UserTestData.USERS_TU1_UPD, PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1 );
        sessionPermissionsH( "SESS-PRMS_H USERS_TU7_HIER OBJS_TOB4 OPS_TOP4 ", UserTestData.USERS_TU7_HIER,
            PermTestData.OBJS_TOB4, PermTestData.OPS_TOP4 );
    }


    /**
     * @param msg
     * @param uArray
     * @param oArray
     * @param opArray
     */
    public static void sessionPermissions( String msg, String[][] uArray, String[][] oArray, String[][] opArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                Session session = accessMgr.createSession( user, false );
                assertNotNull( session );
                List<Permission> pOps = accessMgr.sessionPermissions( session );
                assertNotNull( pOps );
                // There should be objs * ops number of perms in the list returned from sessionPermissions method:
                assertEquals( CLS_NM +
                    ".sessionPermissions failed list size user[" + user.getUserId() + "]",
                    oArray.length * opArray.length, pOps.size() );

                // Iterate over objs x ops, see if every expected valid permission is contained within the returned list:
                for ( String[] obj : oArray )
                {
                    for ( String[] op : opArray )
                    {
                        Permission validPOp = PermTestData.getOp( PermTestData.getName( obj ), op );
                        assertTrue( CLS_NM +
                            ".sessionPermissions failed perm list compare USER [" + user.getUserId() +
                            "] PERM Obj [" + PermTestData.getName( obj ) + "] " +
                            "OPER [" + PermTestData.getName( op ) + "]",
                            pOps.contains( validPOp ) );
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "sessionPermissions: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     * @param msg
     * @param uArray
     * @param oArray
     * @param opArray
     */
    public static void sessionPermissionsH( String msg, String[][] uArray, String[][] oArray, String[][] opArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            int i = 0;
            for ( String[] usr : uArray )
            {
                i++;
                User user = UserTestData.getUser( usr );
                Session session = accessMgr.createSession( user, false );
                assertNotNull( session );
                List<Permission> pOps = accessMgr.sessionPermissions( session );
                assertNotNull( pOps );
                //LOG.warn("sessionPermissionsH list size user [" + user.getUserId() + "] expected len=" +
                //    (11 - i) * opArray.length + " actual len=" + pOps.size());
                assertEquals( CLS_NM +
                    ".sessionPermissionsH failed list size user[" + user.getUserId() + "]",
                    ( 11 - i ) * opArray.length, pOps.size() );

                // Iterate over objs x ops, see if every expected valid permission is contained within the returned list:
                int j = 0;
                for ( String[] obj : oArray )
                {
                    j++;
                    // role1 inherits 2 - 10, role 2 inherits 3 - 10, role 3 inherits 4 - 10, etc.
                    // obj1.op1 - 10 are granted to role1, obj2.op1 - 10 are granted to role2, etc...
                    // positive tests:
                    if ( i == j || i < j )
                    {
                        int k = 0;
                        for ( String[] op : opArray )
                        {
                            k++;
                            Permission validPOp = PermTestData.getOp( PermTestData.getName( obj ), op );
                            assertTrue( CLS_NM +
                                ".sessionPermissionsH failed perm list compare USER [" + user.getUserId() +
                                "] PERM Obj [" + PermTestData.getName( obj ) + "] " +
                                "OPER [" + PermTestData.getName( op ) + "]",
                                pOps.contains( validPOp ) );

                            boolean result = accessMgr.checkAccess( session, new Permission(
                                PermTestData.getName( obj ), PermTestData.getName( op ) ) );
                            assertTrue( CLS_NM +
                                ".sessionPermissionsH failed checkAccess USER [" + user.getUserId() +
                                "] PERM Obj [" + PermTestData.getName( obj ) + "] " +
                                "OPER [" + PermTestData.getName( op ) + "]",
                                result );
                        }
                    }
                    // negative tests:
                    else
                    {
                        int k = 0;
                        for ( String[] op : opArray )
                        {
                            k++;
                            Permission validPOp = PermTestData.getOp( PermTestData.getName( obj ), op );
                            assertTrue( CLS_NM +
                                ".sessionPermissionsH failed negative perm list compare USER [" + user.getUserId() +
                                "] PERM Obj [" + PermTestData.getName( obj ) + "] " +
                                "OPER [" + PermTestData.getName( op ) + "]",
                                !pOps.contains( validPOp ) );

                            boolean result = accessMgr.checkAccess( session, new Permission(
                                PermTestData.getName( obj ), PermTestData.getName( op ) ) );
                            assertTrue( CLS_NM +
                                ".sessionPermissionsH failed negative checkAccess USER [" + user.getUserId() +
                                "] PERM Obj [" + PermTestData.getName( obj ) + "] " +
                                "OPER [" + PermTestData.getName( op ) + "]",
                                !result );
                        }
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "sessionPermissionsH: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testSessionRole()
    {
        // public List<UserRole> sessionRoles(Session session)
        sessionRoles( "SESS-RLS TU1_UPD TR1", UserTestData.USERS_TU1_UPD, RoleTestData.ROLES_TR1 );
        sessionRoles( "SESS-RLS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3 );
        sessionRoles( "SESS-RLS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2 );
    }


    /**
     * @param msg
     * @param uArray
     * @param rArray
     */
    public static void sessionRoles( String msg, String[][] uArray, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                Session session = accessMgr.createSession( user, false );
                assertNotNull( session );
                String userId = accessMgr.getUserId( session );
                assertTrue( CLS_NM + ".sessionRoles failed compare found userId [" + userId + "] valid userId ["
                    + UserTestData.getUserId( usr ) + "]", userId.equalsIgnoreCase( UserTestData.getUserId( usr ) ) );
                UserTestData.assertEquals( user, usr );
                List<UserRole> uRoles = accessMgr.sessionRoles( session );
                assertNotNull( uRoles );
                assertEquals(
                    CLS_NM + ".sessionRoles user role check failed list size user [" + user.getUserId() + "]",
                    rArray.length, uRoles.size() );
                for ( String[] rle : rArray )
                {
                    assertTrue( CLS_NM + ".sessionRoles failed role search USER [" + user.getUserId() + "] ROLE1 ["
                        + RoleTestData.getName( rle ) + "] should be present",
                        uRoles.contains( RoleTestData.getUserRole( UserTestData.getUserId( usr ), rle ) ) );
                }
            }
            LOG.debug( "sessionRoles successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "sessionRoles: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testAddActiveRole()
    {
        // public void addActiveRole(Session session, String role)
        addActiveRoles( "ADD-ACT-RLS TU1_UPD TR1 bad:TR2", UserTestData.USERS_TU1_UPD, RoleTestData.ROLES_TR1,
            RoleTestData.ROLES_TR2 );
        addActiveRoles( "ADD-ACT-RLS TU3 TR3 bad:TR1:", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3,
            RoleTestData.ROLES_TR1 );
        addActiveRoles( "ADD-ACT-RLS TU4 TR2 bad:TR1", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2,
            RoleTestData.ROLES_TR1 );
        addActiveRolesDSD( "ADD-ACT-RLS-USRS_DSDT1 TU8 DSD_T1", UserTestData.USERS_TU8_SSD, RoleTestData.DSD_T1 );
        addActiveRolesDSD( "ADD-ACT-RLS-USRS_DSDT4B TU9 DSD_T4_B", UserTestData.USERS_TU9_SSD_HIER,
            RoleTestData.DSD_T4_B );
        addActiveRolesDSD( "ADD-ACT-RLS-USRS_DSDT5B TU10 DSD_T5_B", UserTestData.USERS_TU10_SSD_HIER,
            RoleTestData.DSD_T5_B );
        addActiveRolesDSD( "ADD-ACT-RLS-USRS_DSDT6B TU11 DSD_T6_B", UserTestData.USERS_TU11_SSD_HIER,
            RoleTestData.DSD_T6_D );
    }


    /**
     * @param msg
     * @param uArray
     * @param rPosArray
     * @param rNegArray
     */
    public static void addActiveRoles( String msg, String[][] uArray, String[][] rPosArray, String[][] rNegArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                Session session = accessMgr.createSession( user, false );
                assertNotNull( session );
                List<UserRole> uRoles = session.getRoles();
                assertNotNull( uRoles );
                assertEquals( CLS_NM + ".addActiveRoles failed list size user[" + user.getUserId() + "]",
                    rPosArray.length, uRoles.size() );
                for ( String[] rle : rPosArray )
                {
                    assertTrue( CLS_NM + ".addActiveRoles failed role search USER [" + user.getUserId() + "] ROLE ["
                        + RoleTestData.getName( rle ) + "] should be present",
                        uRoles.contains( RoleTestData.getUserRole( UserTestData.getUserId( usr ), rle ) ) );
                }
                // Attempt to activate roles that aren't assigned to user:
                for ( String[] badRle : rNegArray )
                {
                    try
                    {
                        // Add Role (this better fail):
                        accessMgr.addActiveRole( session, new UserRole( RoleTestData.getName( badRle ) ) );
                        String error = "addActiveRoles failed negative test 1 User [" + user.getUserId()
                            + "] Role [" + RoleTestData.getName( badRle ) + "]";
                        LOG.info( error );
                        fail( error );
                    }
                    catch ( SecurityException se )
                    {
                        assertTrue( CLS_NM + "addActiveRoles excep id check",
                            se.getErrorId() == GlobalErrIds.URLE_ACTIVATE_FAILED );
                        // pass
                    }
                }
                // remove all roles from the user's session:
                int ctr = rPosArray.length;
                for ( String[] rle : rPosArray )
                {
                    // Drop Role:
                    accessMgr.dropActiveRole( session, new UserRole( RoleTestData.getName( rle ) ) );
                    assertEquals( CLS_NM + ".addActiveRoles failed list size user[" + user.getUserId() + "]",
                        ( --ctr ), session.getRoles().size() );
                    assertTrue( CLS_NM + ".addActiveRoles failed role search USER [" + user.getUserId() + "] ROLE ["
                        + RoleTestData.getName( rle ) + "] should not contain role",
                        !session.getRoles().contains( RoleTestData.getUserRole( UserTestData.getUserId( usr ), rle ) ) );
                    // Drop Role again: (This better fail because role  has already been deactivated from user's session)
                    try
                    {
                        // Drop Role3 (this better fail):
                        accessMgr.dropActiveRole( session, new UserRole( RoleTestData.getName( rle ) ) );
                        String error = "addActiveRoles failed negative test 2 User [" + user.getUserId()
                            + "] Role [" + RoleTestData.getName( rle ) + "]";
                        LOG.info( error );
                        fail( error );
                    }
                    catch ( SecurityException se )
                    {
                        assertTrue( CLS_NM + "addActiveRoles excep id check",
                            se.getErrorId() == GlobalErrIds.URLE_NOT_ACTIVE );
                    }
                }
                // Now activate the list of assigned roles:
                ctr = 0;
                for ( String[] rle : rPosArray )
                {
                    // Activate Role(s):
                    accessMgr.addActiveRole( session, new UserRole( RoleTestData.getName( rle ) ) );
                    uRoles = session.getRoles();
                    assertEquals( CLS_NM + ".addActiveRoles failed list size user [" + user.getUserId() + "]", ++ctr,
                        uRoles.size() );
                    assertTrue( CLS_NM + ".addActiveRoles failed role search USER [" + user.getUserId() + "] ROLE ["
                        + RoleTestData.getName( rle ) + "] should contain role",
                        uRoles.contains( RoleTestData.getUserRole( UserTestData.getUserId( usr ), rle ) ) );
                    try
                    {
                        // Activate Role again (this should throw SecurityException):
                        accessMgr.addActiveRole( session, new UserRole( RoleTestData.getName( rle ) ) );
                        String error = "addActiveRoles failed test 3 User [" + user.getUserId() + "] Role ["
                            + RoleTestData.getName( rle ) + "]";
                        LOG.info( error );
                        fail( error );
                    }
                    catch ( SecurityException se )
                    {
                        assertTrue( CLS_NM + "addActiveRoles excep id check",
                            se.getErrorId() == GlobalErrIds.URLE_ALREADY_ACTIVE );
                        // this is good
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "addActiveRoles: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testDropActiveRole()
    {
        // public void dropActiveRole(Session session, String role)
        dropActiveRoles( "DRP-ACT-RLS TU1_UPD TR1 bad:TR2", UserTestData.USERS_TU1_UPD, RoleTestData.ROLES_TR1 );
        dropActiveRoles( "DRP-ACT-RLS TU3 TR3 bad:TR1", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3 );
        dropActiveRoles( "DRP-ACT-RLS TU4 TR2 bad:TR1", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2 );
    }


    /**
     * @param msg
     * @param uArray
     * @param rArray
     */
    public static void dropActiveRoles( String msg, String[][] uArray, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                Session session = accessMgr.createSession( user, false );
                assertNotNull( session );
                List<UserRole> uRoles = session.getRoles();
                assertNotNull( uRoles );
                assertEquals( CLS_NM + ".dropActiveRoles failed list size user[" + user.getUserId() + "]",
                    rArray.length, uRoles.size() );
                for ( String[] rle : rArray )
                {
                    assertTrue( CLS_NM + ".dropActiveRoles failed role search USER [" + user.getUserId() + "] ROLE ["
                        + RoleTestData.getName( rle ) + "] should be present",
                        uRoles.contains( RoleTestData.getUserRole( UserTestData.getUserId( usr ), rle ) ) );
                }
                // remove all roles from the user's session:
                int ctr = rArray.length;
                for ( String[] rle : rArray )
                {
                    // Drop Role:
                    accessMgr.dropActiveRole( session, new UserRole( RoleTestData.getName( rle ) ) );
                    assertEquals( CLS_NM + ".dropActiveRoles failed list size user[" + user.getUserId() + "]",
                        ( --ctr ), session.getRoles().size() );
                    assertTrue( CLS_NM + ".dropActiveRoles failed role search USER [" + user.getUserId() + "] ROLE ["
                        + RoleTestData.getName( rle ) + "] should not contain role",
                        !session.getRoles().contains( RoleTestData.getUserRole( UserTestData.getUserId( usr ), rle ) ) );
                    // Drop Role again: (This better fail because role  has already been deactivated from user's session)
                    try
                    {
                        // Drop Role3 (this better fail):
                        accessMgr.dropActiveRole( session, new UserRole( RoleTestData.getName( rle ) ) );
                        String error = "dropActiveRoles failed negative test 2 User [" + user.getUserId()
                            + "] Role [" + RoleTestData.getName( rle ) + "]";
                        LOG.info( error );
                        fail( error );
                    }
                    catch ( SecurityException se )
                    {
                        assertTrue( "dropActiveRoles excep id check",
                            se.getErrorId() == GlobalErrIds.URLE_NOT_ACTIVE );
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "dropActiveRoles: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public static void addActiveRolesDSD( String msg, String[][] uArray, String[][] sArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            int i = 0;
            for ( String[] usr : uArray )
            {
                SDSet dsd = RoleTestData.getSDSet( sArray[i++] );
                //Set<String> roles = dsd.getMembers().keySet();
                Set<String> roles = dsd.getMembers();
                User user = UserTestData.getUser( usr );
                Session session = accessMgr.authenticate( user.getUserId(), user.getPassword() );
                int j = 0;
                for ( String role : roles )
                {
                    j++;
                    try
                    {
                        assertNotNull( session );

                        // Activate Role(s):
                        accessMgr.addActiveRole( session, new UserRole( role ) );
                        if ( j >= dsd.getCardinality() )
                        {
                            fail( CLS_NM + ".addActiveRolesDSD user [" + user.getUserId() + "] role [" + role
                                + "] ssd [" + dsd.getName() + "] cardinality [" + dsd.getCardinality() + "] count ["
                                + j + "] failed" );
                        }
                    }
                    catch ( SecurityException ex )
                    {
                        assertTrue( CLS_NM + ".addActiveRolesDSD cardinality test failed user [" + user.getUserId()
                            + "] role [" + role + "] ssd [" + dsd.getName() + "] cardinality [" + dsd.getCardinality()
                            + "] count [" + j + "]", j >= ( dsd.getCardinality() ) );
                        assertTrue(
                            CLS_NM + ".addActiveRolesDSD cardinality test failed [" + UserTestData.getUserId( usr )
                                + "]", ex.getErrorId() == GlobalErrIds.DSD_VALIDATION_FAILED );
                        // still good, break from loop, we're done here
                        break;
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "addActiveRolesDSD caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     * Test the Rbac-Abac curly,moe,larry use cases.
     *
     */
    public void testAbacConstraintsRole()
    {

        createSessionsConstraints( "ABAC RBAC", RoleTestData.ROLE_CONSTRAINTS_TR18_ABAC );
    }

    public static void createSessionsConstraints( String msg, String[][] cArray  )
    {
        LogUtil.logIt( msg );
        String TELLERS = "tellers"; // DSD excluded from washer
        String WASHERS = "washers"; // DSD excluded from teller
        String NORTH = "north";
        String SOUTH = "south";
        String EAST = "east";
        String CURLY = "curly"; // Head Teller of the East, Coin Washer in North and South
        String MOE = "moe"; // Head Teller of the North, Coin Washer in East and South
        String LARRY = "larry"; // Head Teller of the South, Coin Washer in North and East

        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            for( String[] cons : cArray )
            {
                UserRole uRole = RoleTestData.getUserRoleConstraintAbac( cons );
                User user = new User( uRole.getUserId() );
                RoleConstraint constraint = uRole.getConstraints().get( 0 );
                List<RoleConstraint> constraints = new ArrayList();
                constraints.add( constraint );
                Session session = accessMgr.createSession( user, constraints, true );
                assertNotNull( session );
                if( uRole.getName().equalsIgnoreCase( TELLERS ))
                {
                    List<UserRole> actRoles = session.getRoles();
                    for( UserRole actRole : actRoles )
                    {
                        // Someone with Teller role cannot activate Washer.
                        if( actRole.getName().equalsIgnoreCase( WASHERS ))
                        {
                            fail( CLS_NM + ".createSessionsConstraints " + TELLERS + " cannot contain " + WASHERS +", user: " + uRole.getUserId() + ", constraint value: " + constraint.getValue() );
                        }
                    }
                    if( constraint.getValue().equalsIgnoreCase( EAST ))
                    {
                        // Only Curly is an East Teller
                        if( !uRole.getUserId().equalsIgnoreCase( CURLY ))
                        {
                            fail( CLS_NM + ".createSessionsConstraints " + TELLERS + "," + EAST +" invalid relationship, user: " + uRole.getUserId() + ", role name: " + uRole.getName() + ", constraint value: " + constraint.getValue() );
                        }
                    }
                    else if( constraint.getValue().equalsIgnoreCase( NORTH ))
                    {
                        // Only Moe is North Teller
                        if( !uRole.getUserId().equalsIgnoreCase( MOE ))
                        {
                            fail( CLS_NM + ".createSessionsConstraints " + TELLERS + "," + NORTH + " invalid relationship, user: " + uRole.getUserId() + ", role name: " + uRole.getName() + ", constraint value: " + constraint.getValue() );
                        }
                    }
                    else if( constraint.getValue().equalsIgnoreCase( SOUTH ))
                    {
                        // Only Larry is South Teller
                        if( !uRole.getUserId().equalsIgnoreCase( LARRY ))
                        {
                            fail( CLS_NM + ".createSessionsConstraints " + TELLERS + "," + SOUTH + " invalid relationship, user: " + uRole.getUserId() + ", role name: " + uRole.getName() + ", constraint value: " + constraint.getValue() );
                        }
                    }
                    // Invalid test case
                    else
                    {
                        fail( CLS_NM + ".createSessionsConstraints " + TELLERS + " invalid test role: " + uRole.getName() + ", constraint value: " + constraint.getValue() );
                    }

                    // Verify that the Teller has access to the account operations:
                    for( String[] op : PermTestData.ABAC_ACCOUNT_OPS )
                    {
                        Permission validPOp = PermTestData.getOp( PermTestData.getName( PermTestData.ABAC_ACCOUNT_OBJS[0] ), op );
                        boolean result = accessMgr.checkAccess( session, validPOp );
                        assertTrue( CLS_NM +
                                ".createSessionsConstraints failed checkAccess USER [" + user.getUserId() +
                                "] PERM Obj [" + validPOp.getObjName() + "] " +
                                "OPER [" + validPOp.getOpName() + "]",
                            result );
                    }
                }
                // Verify the Washer has access to currency operations:
                else if( uRole.getName().equalsIgnoreCase( WASHERS ))
                {
                    List<UserRole> actRoles = session.getRoles();
                    for( UserRole actRole : actRoles )
                    {
                        // Someone with Washer role cannot activate Teller.
                        if( actRole.getName().equalsIgnoreCase( TELLERS ))
                        {
                            fail( CLS_NM + ".createSessionsConstraints " + WASHERS + " cannot contain " + TELLERS +", user: " + uRole.getUserId() + ", constraint value: " + constraint.getValue() );
                        }
                    }
                    if( constraint.getValue().equalsIgnoreCase( EAST ))
                    {
                        // Curly cannot be an East Washer
                        if( uRole.getUserId().equalsIgnoreCase( CURLY ))
                        {
                            fail( CLS_NM + ".createSessionsConstraints " + WASHERS + "," + EAST + "  invalid relationship, user: " + uRole.getUserId() + ", role name: " + uRole.getName() + ", constraint value: " + constraint.getValue() );
                        }
                    }
                    else if( constraint.getValue().equalsIgnoreCase( NORTH ))
                    {
                        // Moe cannot be a North Washer
                        if( uRole.getUserId().equalsIgnoreCase( MOE ))
                        {
                            fail( CLS_NM + ".createSessionsConstraints " + WASHERS + ","+ NORTH + " invalid relationship, user: " + uRole.getUserId() + ", role name: " + uRole.getName() + ", constraint value: " + constraint.getValue() );
                        }
                    }
                    else if( constraint.getValue().equalsIgnoreCase( SOUTH ))
                    {
                        // Larry cannot be a South Washer
                        if( uRole.getUserId().equalsIgnoreCase( LARRY ))
                        {
                            fail( CLS_NM + ".createSessionsConstraints " + WASHERS + "," + SOUTH + " invalid relationship, user: " + uRole.getUserId() + ", role name: " + uRole.getName() + ", constraint value: " + constraint.getValue() );
                        }
                    }
                    // Invalid test case
                    else
                    {
                        fail( CLS_NM + ".createSessionsConstraints " + WASHERS + " invalid test role: " + uRole.getName() + ", constraint value: " + constraint.getValue() );
                    }
                    // Verify that the Washer has access to the currency operations:
                    for( String[] op : PermTestData.ABAC_CURRENCY_OPS )
                    {
                        Permission validPOp = PermTestData.getOp( PermTestData.getName( PermTestData.ABAC_CURRENCY_OBJS[0] ), op );
                        boolean result = accessMgr.checkAccess( session, validPOp );
                        assertTrue( CLS_NM +
                                ".createSessionsConstraints failed checkAccess USER [" + user.getUserId() +
                                "] PERM Obj [" + validPOp.getObjName() + "] " +
                                "OPER [" + validPOp.getOpName() + "]",
                            result );
                    }
                }
                // Invalid test case
                else
                {
                    fail( CLS_NM + ".createSessionsConstraints invalid test role: " + uRole.getName() );
                }

            }
            LOG.debug( "createSessionsConstraints successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "createSessionsConstraints: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }
}