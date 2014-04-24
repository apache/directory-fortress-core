package us.jts.fortress.rbac.accelerator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.jts.fortress.AccelMgr;
import us.jts.fortress.AccelMgrFactory;
import us.jts.fortress.rbac.Permission;
import us.jts.fortress.rbac.Session;
import us.jts.fortress.rbac.TestUtils;
import us.jts.fortress.rbac.User;
import us.jts.fortress.rbac.UserRole;

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
            // negative test case:
            user.setUserId( "rbacuser1" );
            user.setPassword( "secretx".toCharArray() );
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
            assertFalse( session.isAuthenticated() );
        }
        catch(us.jts.fortress.SecurityException se)
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
        catch(us.jts.fortress.SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
        try
        {
            // positive test case:
            Permission perm = new Permission();
            perm.setObjName( "/rbac/cal2.jsp" );
            perm.setOpName( "8am" );
            boolean result = accelMgr.checkAccess( session, perm );
            assertTrue( result );
            // negative test case:
            perm.setOpName( "9am" );
            result = accelMgr.checkAccess( session, perm );
            assertTrue( !result );
        }
        catch(us.jts.fortress.SecurityException se)
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
        catch(us.jts.fortress.SecurityException se)
        {
            se.printStackTrace();
        }
    }

    @Test
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
            accelMgr.addActiveRole( session, userRole );
        }
        catch(us.jts.fortress.SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
    }

    //@Test
    public void testDropActiveRole()
    {
        LOG.info( "testDropActiveRole..." );
        //AcceleratorDAO aDao = new us.jts.fortress.rbac.dao.apache.AcceleratorDAO();
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
        catch(us.jts.fortress.SecurityException se)
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
        catch(us.jts.fortress.SecurityException se)
        {
            se.printStackTrace();
            fail();
        }
    }
}