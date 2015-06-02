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

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.directory.fortress.core.model.PermGrant;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.model.Warning;
import org.apache.tools.ant.Task;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.AccessMgrFactory;
import org.apache.directory.fortress.core.ReviewMgr;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.ant.AddpermGrant;
import org.apache.directory.fortress.core.ant.AddpermObj;
import org.apache.directory.fortress.core.ant.AddpermOp;
import org.apache.directory.fortress.core.ant.Addrole;
import org.apache.directory.fortress.core.ant.Adduser;
import org.apache.directory.fortress.core.ant.Adduserrole;
import org.apache.directory.fortress.core.ant.FortressAntTask;
import org.apache.directory.fortress.core.ant.PermAnt;
import org.apache.directory.fortress.core.ant.UserAnt;
import org.apache.directory.fortress.core.util.LogUtil;
import org.apache.directory.fortress.core.util.Testable;

import static org.junit.Assert.*;


/**
 * The ReviewMgrAnt Tester component is used to verify results against XML load file.  It is called by {@link
 * FortressAntTask} after it completes
 * its data load.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version 1.0
 */
public class FortressAntLoadTest implements Testable
{
    private static final String CLS_NM = FortressAntLoadTest.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    // This static variable stores reference for input data.  It must be static to make available for junit test
    // methods.
    private static FortressAntTask fortressAntTask;
    private static String fileName;

    /**
     * This method is called by {@link FortressAntTask} via reflexion and invokes its JUnit tests to verify loaded
     * data into LDAP against input data.
     */
    @Override
    public synchronized void execute( Task task )
    {
        fortressAntTask = ( FortressAntTask ) task;
        fileName = task.getProject().getName();
        LOG.info( "execute FortressAntLoadTest JUnit tests on file name: " + fileName );
        Result result = JUnitCore.runClasses( FortressAntLoadTest.class );
        for ( Failure failure : result.getFailures() )
        {
            LOG.info( failure.toString() );
        }
        LOG.info( "TEST SUCCESS: " + result.wasSuccessful() );
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
    private void checkPermissions( String msg, List<UserAnt> users, List<PermAnt> permissions )
    {
        String DATE_FORMAT = "E yyyy.MM.dd 'at' hh:mm:ss a zzz";
        SimpleDateFormat format = new SimpleDateFormat( DATE_FORMAT );
        Date now = new Date();
        String szTimestamp = format.format( now );
        AccessMgr accessMgr = null;
        CSVWriter writer = null;
        LogUtil.logIt( msg );
        try
        {
            accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            writer = new CSVWriter( new FileWriter( fileName + ".csv" ), '\t' );
            String[] entries = "user#resource#operation#result#assigned roles#activated roles#timestamp#warnings"
                .split( "#" );
            writer.writeNext( entries );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "checkPermissions caught SecurityException creating AccessMgr rc=" + ex.getErrorId() + ", " +
                "msg=" + ex.getMessage() + ex );
            // Can't continue without AccessMgr
            fail( ex.getMessage() );
        }
        catch ( IOException ioe )
        {
            String error = "File IO Exception=" + ioe;
            LOG.warn( error );
            // Can't continue without output file to write the results in
            fail( ioe.getMessage() );
        }
        for ( UserAnt user : users )
        {
            try
            {
                List<String> warnings = null;
                Session session = accessMgr.createSession( user, false );
                assertNotNull( session );
                if ( session.getWarnings() != null )
                {
                    warnings = new ArrayList();
                    for ( Warning warning : session.getWarnings() )
                    {
                        warnings.add( warning.getMsg() );
                    }
                }

                ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
                List<UserRole> assignedRoles = reviewMgr.assignedRoles( user );

                for ( PermAnt permAnt : permissions )
                {
                    Boolean result = accessMgr.checkAccess( session, permAnt );
                    // TODO: send this message as CSV output file:
                    LOG.info( "User: " + user.getUserId() + " Perm Obj: " + permAnt.getObjName() + " Perm " +
                        "Operation: " + permAnt.getOpName() + " RESULT: " + result );
                    String[] entries = ( user.getUserId() + "#" + permAnt.getObjName() + "#" + permAnt.getOpName()
                        + "#" + result + "#" + assignedRoles + "#" + session.getUser().getRoles() + "#" + szTimestamp
                        + "#" + warnings ).split( "#" );
                    writer.writeNext( entries );
                }
            }
            catch ( SecurityException ex )
            {
                // Log but don't fail test so entire permission matrix can be evaluated.
                LOG.error( "checkPermissions caught SecurityException rc=" + ex.getErrorId() + ", " +
                    "msg=" + ex.getMessage() + ex );
            }
        }
        try
        {
            writer.close();
        }
        catch ( IOException ioe )
        {
            // ignore
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
                pOp.setObjName( permGrant.getObjName() );
                pOp.setOpName( permGrant.getOpName() );
                pOp.setObjId( permGrant.getObjId() );
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
                assertTrue( "Failed objName value compare", entity.getObjName().equals( permAnt.getObjName()
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
                assertTrue( "Failed objName value compare", entity.getObjName().equals( permObj.getObjName()
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