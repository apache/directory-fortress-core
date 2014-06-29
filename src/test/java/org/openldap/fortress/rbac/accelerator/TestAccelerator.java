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

package org.openldap.fortress.rbac.accelerator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openldap.fortress.AccelMgr;
import org.openldap.fortress.AccelMgrFactory;
import org.openldap.fortress.rbac.Permission;
import org.openldap.fortress.rbac.Session;
import org.openldap.fortress.rbac.TestUtils;
import org.openldap.fortress.rbac.User;
import org.openldap.fortress.rbac.UserRole;

import static org.junit.Assert.*;

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
        user.setPassword( "secret".toCharArray() );
        try
        {
            AccelMgr accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
            Session session;
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
            accelMgr.sessionRoles( session );
        }
        catch( org.openldap.fortress.SecurityException se)
        {
            se.printStackTrace();
        }
    }

    //@Test
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
            user.setPassword( "secret".toCharArray() );
            user.setRole( "rbacrole1" );
            user.setRole( "rbacrole2" );
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
            assertTrue( session.isAuthenticated() );
            try
            {
                // negative test case:
                user.setUserId( "rbacuser1" );
                user.setPassword( "secretx".toCharArray() );
                session = null;
                session = accelMgr.createSession( user, false );
                fail("failed negative createSession for rbacuser1");
            }
            catch( org.openldap.fortress.SecurityException se)
            {
                // sucess
            }

            // negative case should leave the session null.
            assertNull( session );
        }
        catch( org.openldap.fortress.SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
    }

    //@Test
    public void testCheckAccess()
    {
        AccelMgr accelMgr = null;
        LOG.info( "testCheckAccess..." );
        User user = new User();
        user.setUserId( "rbacuser1" );
        user.setPassword( "secret".toCharArray() );
        //user.setRole( "rbacrole1" );
        //user.setRole( "rbacrole2" );
        Session session = null;
        try
        {
            accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
        }
        catch( org.openldap.fortress.SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
        try
        {
            // positive test case:
            Permission perm = new Permission();
            perm.setObjName( "/rbac/cal2.jsp" );
            //perm.setObjId( "123456" );
            perm.setOpName( "8am" );
            boolean result = accelMgr.checkAccess( session, perm );
            assertTrue( result );
            // negative test case:
            perm.setOpName( "9am" );
            result = accelMgr.checkAccess( session, perm );
            assertTrue( !result );
        }
        catch( org.openldap.fortress.SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
    }

    //@Test
    public void testDeleteSession()
    {
        LOG.info( "testDeleteSession..." );
        User user = new User();
        user.setUserId( "rbacuser1" );
        user.setPassword( "secret".toCharArray() );
        try
        {
            AccelMgr accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
            Session session;
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
            accelMgr.deleteSession( session );
        }
        catch( org.openldap.fortress.SecurityException se)
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
            user.setPassword( "secret".toCharArray() );
            user.setRole( "rbacrole1" );
            //user.setRole( "rbacrole2" );
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
            assertTrue( session.isAuthenticated() );
            UserRole userRole = new UserRole( user.getUserId(), "rbacrole2" );
            accelMgr.addActiveRole( session, userRole );
        }
        catch( org.openldap.fortress.SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
    }

    //@Test
    public void testDropActiveRole()
    {
        LOG.info( "testDropActiveRole..." );
        //AcceleratorDAO aDao = new org.openldap.fortress.rbac.dao.apache.AcceleratorDAO();
        try
        {
            AccelMgr accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
            Session session;
            User user = new User();
            // positive test case:
            user.setUserId( "rbacuser1" );
            user.setPassword( "secret".toCharArray() );
            //user.setRole( "rbacrole1" );
            //user.setRole( "rbacrole2" );
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
            assertTrue( session.isAuthenticated() );
            UserRole userRole = new UserRole( user.getUserId(), "rbacrole2" );
            accelMgr.dropActiveRole( session, userRole );
        }
        catch( org.openldap.fortress.SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
    }


    //@Test
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
            user.setPassword( "secret".toCharArray() );
            session = accelMgr.createSession( user, false );
            // positive test case:
            Permission perm = new Permission();
            perm.setObjName( "/rbac/cal2.jsp" );
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
        catch( org.openldap.fortress.SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
    }
}