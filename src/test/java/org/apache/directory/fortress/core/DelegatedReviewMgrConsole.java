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


import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.model.UserAdminRole;
import org.apache.directory.fortress.core.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Description of the Class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
class DelegatedReviewMgrConsole
{
    private DelReviewMgr rm;
    private static final String CLS_NM = DelegatedReviewMgrConsole.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    /**
     * Constructor for the ReviewMgrConsole object
     */
    DelegatedReviewMgrConsole()
    {
        try
        {
            rm = DelReviewMgrFactory.createInstance( TestUtils.getContext() );
        }
        catch ( org.apache.directory.fortress.core.SecurityException e )
        {
            LOG.error( " constructor caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
    }


    /**
     *
     */
    void readRole()
    {
        ReaderUtil.clearScreen();
        try
        {
            System.out.println( "Enter AdminRole name to read:" );
            AdminRole role = new AdminRole();
            role.setName( ReaderUtil.readLn() );
            AdminRole re = rm.readRole( role );
            if ( re != null )
            {
                System.out.println( "ADMIN ROLE OBJECT:" );
                System.out.println( "    name      [" + re.getName() + "]" );
                System.out.println( "    internalId  [" + re.getId() + "]" );
                System.out.println( "    description [" + re.getDescription() + "]" );
                System.out.println( "    osU [" + re.getOsUSet() + "]" );
                System.out.println( "    osP [" + re.getOsPSet() + "]" );
                ReviewMgrConsole.printTemporal( re, "ADMINROLE" );
            }
            else
            {
                System.out.println( "    name [" + role.getName() + "] was not found" );
            }
            System.out.println( "ENTER to continue" );
        }
        catch ( SecurityException e )
        {
            LOG.error( "readRole caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
        ReaderUtil.readChar();
    }


    void findRoles()
    {
        ReaderUtil.clearScreen();
        try
        {
            System.out.println( "Enter value to search AdminRoles with:" );
            String val = ReaderUtil.readLn();
            List<AdminRole> list = rm.findRoles( val );
            if ( list != null && list.size() > 0 )
            {
                for ( int i = 0; i < list.size(); i++ )
                {
                    AdminRole re = list.get( i );
                    System.out.println( "ADMIN ROLE OBJECT [" + i + "]:" );
                    System.out.println( "    name      [" + re.getName() + "]" );
                    System.out.println( "    internalId  [" + re.getId() + "]" );
                    System.out.println( "    description [" + re.getDescription() + "]" );
                    System.out.println( "    osU [" + re.getOsUSet() + "]" );
                    System.out.println( "    osP [" + re.getOsPSet() + "]" );
                    ReviewMgrConsole.printTemporal( re, "ADMINROLE" );
                }
            }
            else
            {
                System.out.println( "name [" + val + "] was not found" );
            }
            System.out.println( "ENTER to continue" );
        }
        catch ( SecurityException e )
        {
            LOG.error( "findRoles caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    void assignedUsers()
    {
        try
        {
            System.out.println( "Enter admin role name:" );
            String name = ReaderUtil.readLn();
            List<User> list = rm.assignedUsers( new AdminRole( name ) );
            int i = 0;
            for ( User ue : list )
            {
                System.out.println( "USER[" + ++i + "]" );
                System.out.println( "    userId      [" + ue.getUserId() + "]" );
                System.out.println( "    internalId  [" + ue.getInternalId() + "]" );
                System.out.println( "    description [" + ue.getDescription() + "]" );
                System.out.println( "    common name [" + ue.getCn() + "]" );
                System.out.println( "    surname     [" + ue.getSn() + "]" );
                System.out.println( "    orgUnitId   [" + ue.getOu() + "]" );
                ReviewMgrConsole.printTemporal( ue, "USER" );
                System.out.println();
            }
            System.out.println( "ENTER to continue" );
        }
        catch ( SecurityException e )
        {
            LOG.error( "assignedUsers caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    void assignedRoles()
    {
        String userId;

        ReaderUtil.clearScreen();
        try
        {
            System.out.println( "Enter UserId to read admin roles:" );
            userId = ReaderUtil.readLn();
            User user = new User();
            user.setUserId( userId );
            List<UserAdminRole> roles = rm.assignedRoles( user );
            for ( UserAdminRole re : roles )
            {
                System.out.println( "USER OBJECT:" );
                System.out.println( "    admin role  [" + re.getName() + "]" );
                System.out.println( "    OsU         [" + re.getOsUSet() + "]" );
                System.out.println( "    OsP         [" + re.getOsPSet() + "]" );
                ReviewMgrConsole.printTemporal( re, "ADMINROLE" );
            }
            System.out.println( "ENTER to continue" );
        }
        catch ( SecurityException e )
        {
            LOG.error( "assignedRoles caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
        ReaderUtil.readChar();
    }
}
