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
package org.apache.directory.fortress.core;


import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.util.VUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Console;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;


/**
 *  This class is used to test drive the Fortress Delegated Access Control APIs.
 *
 *@author     Shawn McKinney
 */
class DelegatedAccessMgrConsole
{
    private DelAccessMgr dAmgr = null;
    private Session session = null;
    private static final String CLS_NM = DelegatedAccessMgrConsole.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    /**
     * put your documentation comment here
     */
    public DelegatedAccessMgrConsole()
    {
        try
        {
            dAmgr = DelAccessMgrFactory.createInstance( TestUtils.getContext() );
        }
        catch ( org.apache.directory.fortress.core.SecurityException e )
        {
            LOG.error( " constructor caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
    }


    /**
     *
     */
    void canAssign()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println( "Enter userId:" );
            User user = new User( ReaderUtil.readLn() );
            System.out.println( "Enter role name:" );
            Role role = new Role( ReaderUtil.readLn() );
            boolean result = dAmgr.canAssign( session, user, role );
            System.out.println( "Can Assign User [" + user.getUserId() + "] Role [" + role.getName() + "] return ["
                + result + "]" );
            System.out.println( "ENTER to continue" );
        }
        catch ( SecurityException e )
        {
            LOG.error( "canAssign caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
        ReaderUtil.readChar();
    }


    void canDeassign()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println( "Enter userId:" );
            User user = new User( ReaderUtil.readLn() );
            System.out.println( "Enter role name:" );
            Role role = new Role( ReaderUtil.readLn() );
            boolean result = dAmgr.canDeassign( session, user, role );
            System.out.println( "Can Deassign User [" + user.getUserId() + "] Role [" + role.getName() + "] return ["
                + result + "]" );
            System.out.println( "ENTER to continue" );
        }
        catch ( SecurityException e )
        {
            LOG.error( "canDeassign caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
        ReaderUtil.readChar();
    }


    void canGrant()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println( "Enter role name:" );
            Role role = new Role( ReaderUtil.readLn() );
            System.out.println( "Enter perm object name:" );
            String objName = ReaderUtil.readLn();
            boolean result = dAmgr.canGrant( session, role, new Permission( objName ) );
            System.out.println( "Can Assign Role [" + role.getName() + "] Object name [" + objName + "] return ["
                + result + "]" );
            System.out.println( "ENTER to continue" );
        }
        catch ( SecurityException e )
        {
            LOG.error( "canGrant caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
        ReaderUtil.readChar();
    }


    void canRevoke()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println( "Enter role name:" );
            Role role = new Role( ReaderUtil.readLn() );
            System.out.println( "Enter perm object name:" );
            String objName = ReaderUtil.readLn();
            boolean result = dAmgr.canRevoke( session, role, new Permission( objName ) );
            System.out.println( "Can Revoke Role [" + role.getName() + "] Object name [" + objName + "] return ["
                + result + "]" );
            System.out.println( "ENTER to continue" );
        }
        catch ( SecurityException e )
        {
            LOG.error( "canRevoke caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    void createSession()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println( "Enter userId:" );
            String userId = ReaderUtil.readLn();
            System.out.println("Enter password:");
            Console console = System.console();
            String password = new String ( console.readPassword() );
            session = new Session();
            //((AccessMgr)dAmgr).createSession(session, userId, password);
            ( ( AccessMgr ) dAmgr ).createSession( new User( userId, password ), false );
            System.out.println( "Session created successfully for userId [" + userId + "]" );
            System.out.println( "session [" + session + "]" );
            System.out.println( "ENTER to continue" );
        }
        catch ( SecurityException e )
        {
            LOG.error( "createSession caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
        ReaderUtil.readChar();
    }


    void createSessionTrusted()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println( "Enter userId:" );
            String userId = ReaderUtil.readLn();
            session = ( ( AccessMgr ) dAmgr ).createSession( new User( userId ), true );
            System.out.println( "Trusted Session created successfully for userId [" + userId + "]" );
            System.out.println( "session [" + session + "]" );
            System.out.println( "ENTER to continue" );
        }
        catch ( SecurityException e )
        {
            LOG.error(
                "createSessionTrusted caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
        ReaderUtil.readChar();
    }


    void checkAccess()
    {
        try
        {
            VUtil.assertNotNull( session, GlobalErrIds.USER_SESS_NULL, ".checkAccess" );
            ReaderUtil.clearScreen();
            System.out.println( "Enter object name:" );
            String objName = ReaderUtil.readLn();
            System.out.println( "Enter operation name:" );
            String opName = ReaderUtil.readLn();
            boolean result = dAmgr.checkAccess( session, new Permission( objName, opName ) );
            System.out.println( "CheckAccess return [" + result + "] for user [" + session.getUserId() + "] objName ["
                + objName + "] operationName [" + opName + "]" );
            System.out.println( "ENTER to continue" );
        }
        catch ( SecurityException e )
        {
            LOG.error( "checkAccess caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
        ReaderUtil.readChar();
    }


    void sessionPermissions()
    {
        try
        {
            VUtil.assertNotNull( session, GlobalErrIds.USER_SESS_NULL, "DelegatedAccessMgrConsole.sessionPermissions" );
            ReaderUtil.clearScreen();
            List<Permission> list = dAmgr.sessionPermissions( session );
            if ( list != null )
            {
                Collections.sort( list, new Comparator<Permission>()
                {
                    @Override
                    public int compare( Permission p1, Permission p2 )
                    {
                        return p1.getAbstractName().compareTo( p2.getAbstractName() );
                    }
                } );
                int i = 0;
                for ( Permission pe : list )
                {
                    //pe = (Permission) list.get(i);
                    System.out.println( "**perm:" + ( i++ ) + "***" );
                    System.out.println( "object name [" + pe.getObjName() + "]" );
                    System.out.println( "object id [" + pe.getObjId() + "]" );
                    System.out.println( "operation name [" + pe.getOpName() + "]" );
                    System.out.println( "abstract perm name [" + pe.getAbstractName() + "]" );
                    System.out.println( "internalId [" + pe.getInternalId() + "]" );
                    if ( pe.getUsers() != null && pe.getUsers().size() > 0 )
                    {
                        int ctr = 0;
                        for ( String user : pe.getUsers() )
                        {
                            System.out.println( "user[" + ctr++ + "]=" + user );
                        }
                    }
                    if ( pe.getRoles() != null && pe.getRoles().size() > 0 )
                    {
                        int ctr = 0;
                        for ( String role : pe.getRoles() )
                        {
                            System.out.println( "name[" + ctr++ + "]=" + role );
                        }
                    }
                    if ( pe.getProperties() != null && pe.getProperties().size() > 0 )
                    {
                        int ctr = 0;
                        for ( Enumeration e = pe.getProperties().propertyNames(); e.hasMoreElements(); )
                        {
                            String key = ( String ) e.nextElement();
                            String val = pe.getProperty( key );
                            System.out.println( "prop key[" + ctr + "]=" + key );
                            System.out.println( "prop value[" + ctr++ + "]=" + val );
                        }
                    }
                    System.out.println( "**" );
                }
            }
            System.out.println( "ENTER to continue" );
        }
        catch ( SecurityException e )
        {
            LOG.error( "sessionPermissions caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(),
                e );
        }
        ReaderUtil.readChar();
    }
}
