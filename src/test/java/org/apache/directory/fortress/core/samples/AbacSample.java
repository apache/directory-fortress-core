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


import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.directory.fortress.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.AccessMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.TestUtils;


/**
 * AccessMgrSample JUnit Test. The APIs exercised within this class are used to perform
 * dynamic constraints.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AbacSample extends TestCase
{
    private static final String CLS_NM = AbacSample.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    public AbacSample( String name )
    {
        super( name );
    }

    /**
     * Run the Fortress Abac samples.
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new AbacSample( "testCurlyEast" ) );
        suite.addTest( new AbacSample( "testCurlyNorth" ) );
        suite.addTest( new AbacSample( "testMoeNorth" ) );
        suite.addTest( new AbacSample( "testMoeSouth" ) );
        return suite;
    }

    public static void testMoeNorth()
    {
        String szLocation = ".testMoeNorth";
        LOG.info( szLocation );
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            List<RoleConstraint> constraints = new ArrayList();
            RoleConstraint constraint = new RoleConstraint();
            constraint.setKey( "locale" );
            constraint.setValue( "north" );
            constraints.add( constraint );
            Session session = accessMgr.createSession( new User("moe", "password"), constraints, false );
            assertNotNull( session );
            displayUserRoles( accessMgr, session );
            displaySessionPerms( accessMgr, session );
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }

    public static void testMoeSouth()
    {
        String szLocation = ".testMoeSouth";
        LOG.info( szLocation );
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            List<RoleConstraint> constraints = new ArrayList();
            RoleConstraint constraint = new RoleConstraint();
            constraint.setKey( "locale" );
            constraint.setValue( "south" );
            constraints.add( constraint );
            Session session = accessMgr.createSession( new User("moe", "password"), constraints, false );
            assertNotNull( session );
            displayUserRoles( accessMgr, session );
            displaySessionPerms( accessMgr, session );
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }

    public static void testCurlyEast()
    {
        String szLocation = ".testCurlyEast";
        LOG.info( szLocation );
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            List<RoleConstraint> constraints = new ArrayList();
            RoleConstraint constraint = new RoleConstraint();
            constraint.setKey( "locale" );
            constraint.setValue( "east" );
            constraints.add( constraint );
            Session session = accessMgr.createSession( new User("curly", "password"), constraints, false );
            assertNotNull( session );
            displayUserRoles( accessMgr, session );
            displaySessionPerms( accessMgr, session );
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }

    public static void testCurlyNorth()
    {
        String szLocation = ".testCurlyNorth";
        LOG.info( szLocation );
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
            List<RoleConstraint> constraints = new ArrayList();
            RoleConstraint constraint = new RoleConstraint();
            constraint.setKey( "locale" );
            constraint.setValue( "north" );
            constraints.add( constraint );
            Session session = accessMgr.createSession( new User("curly", "password"), constraints, false );
            assertNotNull( session );
            displayUserRoles( accessMgr, session );
            displaySessionPerms( accessMgr, session );
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }

    public static void displaySessionPerms( AccessMgr accessMgr, Session session )
    {
        String szLocation = ".displaySessionPerms";
        try
        {
            LOG.info( szLocation );
            LOG.info( "S   UID  [" + session.getUserId() + "]:" );
            List<Permission> perms = accessMgr.sessionPermissions(session);
            assertNotNull( perms );
            if ( perms != null )
            {
                for ( int i = 0; i < perms.size(); i++ )
                {
                    Permission perm = perms.get( i );
                    LOG.info( "    PERM[" + i + "]:" + perm.getObjName() + "." + perm.getOpName() );
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }

    public static void displayUserRoles( AccessMgr accessMgr, Session session )
    {
        String szLocation = ".displayUserRoles";
        try
        {
            User user = accessMgr.getUser( session );
            assertNotNull( user );
            LOG.info( szLocation );
            LOG.info( "S   UID  [" + session.getUserId() + "]:" );
            List<UserRole> roles = session.getRoles();
            if ( roles != null )
            {
                for ( int i = 0; i < roles.size(); i++ )
                {
                    UserRole ur = roles.get( i );
                    LOG.info( "    USER ROLE[" + i + "]:" );
                    LOG.info( "        role name [" + ur.getName() + "]" );
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }
}