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
package org.apache.directory.fortress.core.impl.accelerator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.directory.fortress.core.AccelMgr;
import org.apache.directory.fortress.core.AccelMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAccelerator
{
    private static final Logger LOG = LoggerFactory.getLogger( TestAccelerator.class );

    @BeforeClass
    public static void testSetup()
    {
    }

    @AfterClass
    public static void testCleanup()
    {
        // Teardown for data used by the unit tests
    }

    @Test
    public void testSessionRoles()
    {
        LOG.info( "testSessionRoles..." );
        User user = new User();
        user.setUserId( "rbacuser1" );
        user.setPassword( "secret" );
        try
        {
            AccelMgr accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
            Session session;
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
            accelMgr.sessionRoles( session );
        }
        catch( org.apache.directory.fortress.core.SecurityException se)
        {
            se.printStackTrace();
        }
    }

    @Test
    public void testCreateSession()
    {
        LOG.info( "testCreateSession..." );
        try
        {
            AccelMgr accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
            Session session;
            User user = new User();
            // positive test case:
            user.setUserId( "rbacuser1" );
            user.setPassword( "secret" );
            user.setRoleName( "rbacrole1" );
            user.setRoleName( "rbacrole2" );
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
            assertTrue( session.isAuthenticated() );
            try
            {
                // negative test case:
                user.setUserId( "rbacuser1" );
                user.setPassword( "secretx" );
                session = null;
                session = accelMgr.createSession( user, false );
                fail("failed negative createSession for rbacuser1");
            }
            catch( SecurityException se)
            {
                // sucess
            }

            // negative case should leave the session null.
            assertNull( session );
        }
        catch( SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCheckAccess()
    {
        AccelMgr accelMgr = null;
        LOG.info( "testCheckAccess..." );
        User user = new User();
        user.setUserId( "rbacuser1" );
        user.setPassword( "secret" );
        //user.setRole( "rbacrole1" );
        //user.setRole( "rbacrole2" );
        Session session = null;
        try
        {
            accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
        }
        catch( SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
        try
        {
            // positive test case:
            Permission perm = new Permission();
            perm.setObjName( "/impl/cal2.jsp" );
            //perm.setObjId( "123456" );
            perm.setOpName( "8am" );
            boolean result = accelMgr.checkAccess( session, perm );
            assertTrue( result );
            // negative test case:
            perm.setOpName( "9am" );
            result = accelMgr.checkAccess( session, perm );
            assertTrue( !result );
        }
        catch( SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeleteSession()
    {
        LOG.info( "testDeleteSession..." );
        User user = new User();
        user.setUserId( "rbacuser1" );
        user.setPassword( "secret" );
        try
        {
            AccelMgr accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
            Session session;
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
            accelMgr.deleteSession( session );
        }
        catch( SecurityException se)
        {
            se.printStackTrace();
        }
    }

    //@Test
    public void testAddActiveRole()
    {
        LOG.info( "testAddActiveRole..." );
        try
        {
            AccelMgr accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
            Session session;
            User user = new User();
            // positive test case:
            user.setUserId( "rbacuser1" );
            user.setPassword( "secret" );
            user.setRoleName( "rbacrole1" );
            //user.setRole( "rbacrole2" );
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
            assertTrue( session.isAuthenticated() );
            UserRole userRole = new UserRole( user.getUserId(), "rbacrole2" );
            accelMgr.addActiveRole( session, userRole );
        }
        catch( SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDropActiveRole()
    {
        LOG.info( "testDropActiveRole..." );
        //AcceleratorDAO aDao = new org.apache.directory.fortress.core.impl.AcceleratorDAO();
        try
        {
            AccelMgr accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
            Session session;
            User user = new User();
            // positive test case:
            user.setUserId( "rbacuser1" );
            user.setPassword( "secret" );
            //user.setRole( "rbacrole1" );
            //user.setRole( "rbacrole2" );
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
            assertTrue( session.isAuthenticated() );
            UserRole userRole = new UserRole( user.getUserId(), "rbacrole2" );
            accelMgr.dropActiveRole( session, userRole );
        }
        catch( SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
    }


    @Test
    public void testCombinedCalls()
    {
        LOG.info( "testCombinedCalls..." );
        try
        {
            AccelMgr accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
            Session session;
            User user = new User();
            // positive test case:
            user.setUserId( "rbacuser1" );
            user.setPassword( "secret" );
            session = accelMgr.createSession( user, false );
            // positive test case:
            Permission perm = new Permission();
            perm.setObjName( "/impl/cal2.jsp" );
            perm.setOpName( "8am" );
            boolean result = accelMgr.checkAccess( session, perm );
            assertTrue( result );

            // drop role1:
            UserRole userRole = new UserRole( user.getUserId(), "rbacrole1" );
            accelMgr.dropActiveRole( session, userRole );

            // this should return false:
            result = accelMgr.checkAccess( session, perm );
            assertTrue( !result );

            // now add role1 back again:
            userRole = new UserRole( user.getUserId(), "rbacrole1" );
            accelMgr.addActiveRole( session, userRole );

            // this should return true:
            result = accelMgr.checkAccess( session, perm );
            assertTrue( result );
        }
        catch( SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
    }
}