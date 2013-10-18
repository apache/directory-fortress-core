/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import java.util.List;

import org.apache.tools.ant.Task;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.jts.fortress.AccessMgr;
import us.jts.fortress.AccessMgrFactory;
import us.jts.fortress.ReviewMgr;
import us.jts.fortress.SecurityException;
import us.jts.fortress.ant.AddpermGrant;
import us.jts.fortress.ant.AddpermObj;
import us.jts.fortress.ant.AddpermOp;
import us.jts.fortress.ant.Addrole;
import us.jts.fortress.ant.Adduser;
import us.jts.fortress.ant.Adduserrole;
import us.jts.fortress.ant.FortressAntTask;
import us.jts.fortress.ant.PermAnt;
import us.jts.fortress.ant.UserAnt;
import us.jts.fortress.util.LogUtil;
import us.jts.fortress.util.Testable;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


/**
 * The ReviewMgrAnt Tester component is used to verify results against XML load file.  It is called by {@link FortressAntTask} after it completes
 * its data load.
 *
 * @author Shawn McKinney
 * @version 1.0
 */
public class FortressAntLoadTest implements Testable
{
    private static final String CLS_NM = FortressAntLoadTest.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    // This static variable stores reference for input data.  It must be static to make available for junit test methods.
    private static FortressAntTask fortressAntTask;

    /**
     * This method is called by {@link FortressAntTask} via reflexion and invokes its JUnit tests to verify loaded data into LDAP against input data.
     */
    @Override
    public synchronized void execute( Task task )
    {
        fortressAntTask = ( FortressAntTask ) task;
        LOG.info( "execute FortressAntLoadTest JUnit tests..." );
        Result result = JUnitCore.runClasses( FortressAntLoadTest.class );
        for ( Failure failure : result.getFailures() )
        {
            System.out.println( failure.toString() );
        }
        System.out.println( result.wasSuccessful() );
    }


    @Test
    public void testAuthorizations()
    {
        // gather permission input data:
        List<AddpermOp> addpermOps = fortressAntTask.getAddpermOps();
        List<PermAnt> permissions = addpermOps.get( 0 ).getPermOps();
        // gather user input data:
        List<Adduser> addusers = fortressAntTask.getAddusers();
        List<UserAnt> users = addusers.get( 0 ).getUsers();
        checkPermissions( "CHECK-PERMS", users, permissions );
    }


    /**
     * @param msg
     * @param permissions
     */
    private static void checkPermissions( String msg, List<UserAnt> users, List<PermAnt> permissions )
    {
        AccessMgr accessMgr = null;
        LogUtil.logIt( msg );
        try
        {
            accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "checkPermissions caught SecurityException creating AccessMgr rc=" + ex.getErrorId() + ", " +
                "msg=" + ex.getMessage() + ex );
            // Can't continue without AccessMgr
            fail( ex.getMessage() );
        }
        for ( UserAnt user : users )
        {
            try
            {
                Session session = accessMgr.createSession( user, false );
                assertNotNull( session );
                for ( PermAnt permAnt : permissions )
                {
                    Boolean result = accessMgr.checkAccess( session, permAnt );
                    // TODO: send this message as CSV output file:
                    LOG.info( "User: " + user.getUserId() + " Perm Obj: " + permAnt.getObjectName() + " Perm " +
                        "Operation: " + permAnt.getOpName() + " RESULT: " + result );
                }
            }
            catch ( SecurityException ex )
            {
                // Log but don't fail test so entire permission matrix can be evaluated.
                LOG.error( "checkPermissions caught SecurityException rc=" + ex.getErrorId() + ", " +
                    "msg=" + ex.getMessage() + ex );
            }
        }
    }


    @Test
    public void testPermissionRoles()
    {
        // gather permission to role grant input data:
        List<AddpermGrant> addpermGrants = fortressAntTask.getAddpermGrants();
        for ( AddpermGrant addpermGrant : addpermGrants )
        {
            List<PermGrant> permGrants = addpermGrant.getPermGrants();
            permissionRoles( "PRM-RLS", permGrants );
        }
    }


    private static void permissionRoles( String msg, List<PermGrant> permGrants )
    {
        LogUtil.logIt( msg );
        Permission pOp;
        try
        {
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for ( PermGrant permGrant : permGrants )
            {
                pOp = new Permission();
                pOp.setObjectName( permGrant.getObjName() );
                pOp.setOpName( permGrant.getOpName() );
                pOp.setObjectId( permGrant.getObjId() );
                List<String> roles = reviewMgr.permissionRoles( pOp );
                assertNotNull( roles );
                int indx = roles.indexOf( permGrant.getRoleNm() );
                assertTrue( "Failed to find roleNm: " + permGrant.getRoleNm(), indx != -1 );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "permissionRoles caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    @Test
    public void testReadUser()
    {
        // gather user input data:
        List<Adduser> addusers = fortressAntTask.getAddusers();
        for ( Adduser adduser : addusers )
        {
            List<UserAnt> users = adduser.getUsers();
            readUsers( "READ-USRS", users );
        }
    }


    private static void readUsers( String msg, List<UserAnt> users )
    {
        LogUtil.logIt( msg );
        try
        {
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for ( User user : users )
            {
                User entity = reviewMgr.readUser( user );
                assertNotNull( entity );
                UserTestData.assertEquals( entity, user );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "readUsers caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    @Test
    public void testReadRole()
    {
        // gather role input data:
        List<Addrole> addroles = fortressAntTask.getAddroles();
        for ( Addrole addrole : addroles )
        {
            List<Role> roles = addrole.getRoles();
            readRoles( "RD-RLS", roles );
        }
    }


    private static void readRoles( String msg, List<Role> roles )
    {
        LogUtil.logIt( msg );
        try
        {
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for ( Role role : roles )
            {
                Role entity = reviewMgr.readRole( role );
                assertNotNull( entity );
                assertTrue( "Failed role name", entity.getName().equals( role.getName() ) );
                TestUtils.assertTemporal( CLS_NM + ".assertEquals", role, entity );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "readRoles caught SecurityException=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    @Test
    public void testAssignedRoles()
    {
        // gather assigned user to role input data:
        List<Adduserrole> adduserroles = fortressAntTask.getAdduserroles();
        for ( Adduserrole adduserrole : adduserroles )
        {
            List<UserRole> userroles = adduserrole.getUserRoles();
            assignedRoles( "ASGN-RLS", userroles );
        }
    }


    private static void assignedRoles( String msg, List<UserRole> userroles )
    {
        LogUtil.logIt( msg );
        try
        {
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for ( UserRole userrole : userroles )
            {
                List<UserRole> assignedRoles = reviewMgr.assignedRoles( new User( userrole.getUserId() ) );
                assertNotNull( assignedRoles );
                int indx = assignedRoles.indexOf( userrole );
                assertTrue( "Failed userrole name", indx != -1 );
                UserRole assignedRole = assignedRoles.get( indx );
                TestUtils.assertTemporal( CLS_NM + ".assertEquals", userrole, assignedRole );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "assignedRoles caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    @Test
    public void testReadPermissionOp()
    {
        // gather permission operation input data:
        List<AddpermOp> addpermOps = fortressAntTask.getAddpermOps();
        for ( AddpermOp addpermOp : addpermOps )
        {
            List<PermAnt> permissions = addpermOp.getPermOps();
            readPermissionOps( "RD-PRM-OPS", permissions );
        }
    }


    /**
     * @param msg
     * @param permissions
     */
    private static void readPermissionOps( String msg, List<PermAnt> permissions )
    {
        LogUtil.logIt( msg );
        try
        {
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for ( PermAnt permAnt : permissions )
            {
                Permission entity = reviewMgr.readPermission( permAnt );
                assertNotNull( entity );
                assertTrue( "Failed objectName value compare", entity.getObjectName().equals( permAnt.getObjectName()
                ) );
                assertTrue( "Failed opName value compare", entity.getOpName().equals( permAnt.getOpName() ) );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "readPermissionOps caught SecurityException rc=" + ex.getErrorId() + ", " +
                "msg=" + ex.getMessage() + ex );
            fail( ex.getMessage() );
        }
    }


    @Test
    public void testReadPermissionObj()
    {
        // gather permission object input data:
        List<AddpermObj> addpermObjs = fortressAntTask.getAddpermObjs();
        for ( AddpermObj addpermObj : addpermObjs )
        {
            List<PermObj> permObjs = addpermObj.getPermObjs();
            readPermissionObjs( "RD-PRM-OBJS", permObjs );
        }
    }


    private static void readPermissionObjs( String msg, List<PermObj> permObjs )
    {
        LogUtil.logIt( msg );
        try
        {
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for ( PermObj permObj : permObjs )
            {
                PermObj entity = reviewMgr.readPermObj( permObj );
                assertNotNull( entity );
                assertTrue( "Failed objectName value compare", entity.getObjectName().equals( permObj.getObjectName()
                ) );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "readPermissionOps caught SecurityException rc=" + ex.getErrorId() + ", " +
                "msg=" + ex.getMessage() + ex );
            fail( ex.getMessage() );
        }
    }
}