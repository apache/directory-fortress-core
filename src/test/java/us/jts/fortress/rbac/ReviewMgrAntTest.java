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

import us.jts.fortress.ReviewMgr;
import us.jts.fortress.SecurityException;

import us.jts.fortress.ant.AddpermObj;
import us.jts.fortress.ant.AddpermOp;
import us.jts.fortress.ant.Addrole;
import us.jts.fortress.ant.Adduserrole;
import us.jts.fortress.ant.FortressAntTask;
import us.jts.fortress.ant.PermAnt;
import us.jts.fortress.util.LogUtil;
import us.jts.fortress.util.Testable;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


/**
 * ReviewMgrAnt Tester.
 *
 * @author Shawn McKinney
 * @version 1.0
 */
public class ReviewMgrAntTest implements Testable
{
    private static final String CLS_NM = ReviewMgrAntTest.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static FortressAntTask fortressAntTask;

    public synchronized void execute( Task task )
    {
        fortressAntTask = ( FortressAntTask ) task;
        LOG.info( "execute ReviewMgrAntTest JUnit tests..." );
        Result result = JUnitCore.runClasses( ReviewMgrAntTest.class );
        for ( Failure failure : result.getFailures() )
        {
            System.out.println( failure.toString() );
        }
        System.out.println( result.wasSuccessful() );
    }

    @Test
    public void testReadRole()
    {
        List<Addrole> addroles = this.fortressAntTask.getAddroles();
        for ( Addrole addrole : addroles )
        {
            List<Role> roles = addrole.getRoles();
            readRoles( "RD-RLS", roles );
        }
    }


    public static void readRoles( String msg, List<Role> roles )
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
        List<Adduserrole> adduserroles = this.fortressAntTask.getAdduserroles();
        for ( Adduserrole adduserrole : adduserroles )
        {
            List<UserRole> userroles = adduserrole.getUserRoles();
            assignedRoles( "ASGN-RLS", userroles );
        }
    }


    public static void assignedRoles( String msg, List<UserRole> userroles )
    {
        LogUtil.logIt( msg );
        try
        {
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for ( UserRole userrole : userroles )
            {
                List<UserRole> assignedRoles = reviewMgr.assignedRoles( new User( userrole.getUserId() ) );
                assertNotNull( assignedRoles );
                assertTrue( "Failed userrole name", assignedRoles.contains( userrole ) );
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
        //    public Permission readPermission(Permission permOp)
        List<AddpermOp> addpermOps = this.fortressAntTask.getAddpermOps();
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
    public static void readPermissionOps( String msg, List<PermAnt> permissions )
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


    public void testFindPermissionOps()
    {
        searchPermissionOps( "FND-PRM-OPS TOB1 OPS_TOP1_UPD", TestUtils.getSrchValue( PermTestData.getName(
            PermTestData.OPS_TOP1_UPD[0] ) ), PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1_UPD );
    }


    /**
     * @param msg
     * @param srchValue
     * @param pObjArray
     */
    public static void searchPermissionOps( String msg, String srchValue, String[][] pObjArray, String[][] pOpArray )
    {
        LogUtil.logIt( msg );
        Permission pOp;
        try
        {
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for ( String[] obj : pObjArray )
            {
                for ( String[] op : pOpArray )
                {
                    pOp = new Permission();
                    pOp.setObjectName( PermTestData.getName( obj ) );
                    pOp.setOpName( srchValue );
                    List<Permission> ops = reviewMgr.findPermissions( pOp );
                    assertNotNull( ops );
                    assertTrue( CLS_NM + "searchPermissionOps srchValue [" + srchValue + "] list size check",
                        pOpArray.length == ops.size() );

                    int indx = ops.indexOf( new Permission( PermTestData.getName( obj ), PermTestData.getName( op ) ) );
                    if ( indx != -1 )
                    {
                        Permission entity = ops.get( indx );
                        assertNotNull( entity );
                        PermTestData.assertEquals( PermTestData.getName( obj ), entity, op );
                        LOG.debug( "searchPermissionOps objectName [" + entity.getObjectName() + "] operation name ["
                            + entity.getOpName() + "] successful" );
                    }
                    else
                    {
                        msg = "searchPermissionOps srchValue [" + srchValue + "] failed list search";
                        LogUtil.logIt( msg );
                        fail( msg );
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "searchPermissionOps srchValue [" + srchValue + "] caught SecurityException rc=" + ex
                .getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    @Test
    public void testReadPermissionObj()
    {
        //    public Permission readPermission(Permission permOp)
        List<AddpermObj> addpermObjs = this.fortressAntTask.getAddpermObjs();
        for ( AddpermObj addpermObj : addpermObjs )
        {
            List<PermObj> permObjs = addpermObj.getPermObjs();
            readPermissionObjs( "RD-PRM-OBJS", permObjs );
        }
    }

    public static void readPermissionObjs( String msg, List<PermObj> permObjs )
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


    public void testFindPermissionObjs()
    {
        // public List<Permission> findPermissions(Permission permOp)
        searchPermissionObjs( "FND-PRM-OBJS TOB1", TestUtils.getSrchValue( PermTestData.getName( PermTestData
            .OBJS_TOB1[0] ) ), PermTestData.OBJS_TOB1 );
    }


    /**
     * @param msg
     * @param srchValue
     * @param pArray
     */
    public static void searchPermissionObjs( String msg, String srchValue, String[][] pArray )
    {
        LogUtil.logIt( msg );
        try
        {
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            List<PermObj> objs = reviewMgr.findPermObjs( new PermObj( srchValue ) );
            assertNotNull( objs );
            assertTrue( CLS_NM + "searchPermissionObjs srchValue [" + srchValue + "] list size check",
                pArray.length == objs.size() );
            for ( String[] obj : pArray )
            {
                int indx = objs.indexOf( new PermObj( PermTestData.getName( obj ) ) );
                if ( indx != -1 )
                {
                    PermObj entity = objs.get( indx );
                    assertNotNull( entity );
                    PermTestData.assertEquals( entity, obj );
                    LOG.debug( "searchPermissionObjs [" + entity.getObjectName() + "] successful" );
                }
                else
                {
                    msg = "searchPermissionObjs srchValue [" + srchValue + "] failed list search";
                    LogUtil.logIt( msg );
                    fail( msg );
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "searchPermissionObjs srchValue [" + srchValue + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }
}
