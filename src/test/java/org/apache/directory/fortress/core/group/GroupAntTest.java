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
package org.apache.directory.fortress.core.group;

import java.util.List;

import org.apache.tools.ant.Task;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.apache.directory.fortress.core.ant.Addgroup;
import org.apache.directory.fortress.core.model.Group;
import org.apache.directory.fortress.core.GroupMgr;
import org.apache.directory.fortress.core.GroupMgrFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.ant.FortressAntTask;
import org.apache.directory.fortress.core.util.LogUtil;
import org.apache.directory.fortress.core.util.Testable;

import static org.junit.Assert.*;


/**
 * The GroupAntTest Tester component is used to verify results against XML load file.  It is called by {@link
 * FortressAntTask} after it completes
 * its data load.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version 1.0
 */
public class GroupAntTest implements Testable
{
    private static final String CLS_NM = GroupAntTest.class.getName();
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
        LOG.info( "execute GroupAntTest JUnit tests on file name: " + fileName );
        Result result = JUnitCore.runClasses( GroupAntTest.class );
        for ( Failure failure : result.getFailures() )
        {
            LOG.info( failure.toString() );
        }
        LOG.info( "TEST SUCCESS: " + result.wasSuccessful() );
    }

    @Test
    public void testReadGroups()
    {
        // gather role input data:
        List<Addgroup> addgroups = fortressAntTask.getAddgroups();
        for ( Addgroup addgroup : addgroups )
        {
            List<Group> groups = addgroup.getGroups();
            readGroups( "READ-GRPS", groups );
        }
    }

    private void readGroups( String msg, List<Group> groups )
    {
        LogUtil.logIt( msg );
        try
        {
            GroupMgr groupMgr = GroupMgrFactory.createInstance();
            for ( Group inGroup : groups )
            {
                Group outGroup = groupMgr.read( inGroup );
                assertNotNull( outGroup );
                assertEqual( inGroup, outGroup );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "readGroups caught SecurityException=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }

    @Test
    public void testFindGroups()
    {
        // gather role input data:
        List<Addgroup> addgroups = fortressAntTask.getAddgroups();
        for ( Addgroup addgroup : addgroups )
        {
            List<Group> groups = addgroup.getGroups();
            findGroups( "FIND-GRPS", groups );
        }
    }

    private void findGroups( String msg, List<Group> inGroups )
    {
        LogUtil.logIt( msg );
        try
        {
            GroupMgr groupMgr = GroupMgrFactory.createInstance();
            List<Group> outGroups = groupMgr.find( new Group( "t" ) );
            assertNotNull( outGroups );
            assertTrue( "findGroups result set wrong size", outGroups.size() == inGroups.size() );
            for ( Group inGroupAnt : inGroups )
            {
                Group inGroup = inGroupAnt;
                int index = outGroups.indexOf( inGroup );
                assertTrue( "findGroups input data error ", index != -1 );
                Group outGroup = outGroups.get( index );
                assertNotNull( outGroup );
                assertEqual( inGroup, outGroup );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "findGroups caught SecurityException=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    private void assertEqual( Group group1, Group group2 )
    {
        assertEquals( CLS_NM + ".assertEquals failed compare group name", group1.getName(), group2.getName() );
        assertEquals( CLS_NM + ".assertEquals failed compare group description", group1.getDescription(), group2.getDescription() );
        assertEquals( CLS_NM + ".assertEquals failed compare group protocol", group1.getProtocol(), group2.getProtocol() );
        assertEquals( CLS_NM + ".assertEquals failed compare group members", group1.getMembers(), group2.getMembers() );
        assertEquals( CLS_NM + ".assertEquals failed compare group properties", group1.getProperties(), group2.getProperties() );
    }

    /*
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
*/
}
