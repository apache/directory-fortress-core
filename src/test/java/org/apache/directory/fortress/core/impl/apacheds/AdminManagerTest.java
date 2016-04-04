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
package org.apache.directory.fortress.core.impl.apacheds;


import static org.junit.Assert.fail;

import java.util.Set;

import org.apache.directory.server.annotations.CreateLdapServer;
import org.apache.directory.server.annotations.CreateTransport;
import org.apache.directory.server.core.annotations.ApplyLdifFiles;
import org.apache.directory.server.core.annotations.CreateDS;
import org.apache.directory.server.core.annotations.CreatePartition;
import org.apache.directory.server.core.integ.AbstractLdapTestUnit;
import org.apache.directory.server.core.integ.FrameworkRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.AdminMgr;
import org.apache.directory.fortress.core.AdminMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.ldap.LdapDataProvider;
import org.apache.directory.fortress.core.ldap.LdapCounters;
import org.apache.directory.fortress.core.impl.DelegatedMgrImplTest;
import org.apache.directory.fortress.core.impl.FortressJUnitTest;
import org.apache.directory.fortress.core.impl.OrgUnitTestData;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.impl.UserTestData;
import org.apache.directory.fortress.core.util.LogUtil;
import org.apache.directory.fortress.core.util.cache.CacheMgr;


@RunWith(FrameworkRunner.class)
@CreateDS(name = "classDS", partitions =
    { @CreatePartition(name = "example", suffix = "dc=example,dc=com") })
@CreateLdapServer(
    transports =
        {
            @CreateTransport(protocol = "LDAP", port = 10389)
    })
@ApplyLdifFiles(
    { "fortress-schema.ldif", "init-ldap.ldif"/*, "test-data.ldif"*/})
public class AdminManagerTest extends AbstractLdapTestUnit
{
    private static final Logger LOG = LoggerFactory.getLogger( AdminManagerTest.class.getName() );
    private static Session adminSess = null;


    @Before
    public void init()
    {
        CacheMgr.getInstance().clearAll();
    }


    @After
    public void displayCounters()
    {
        LdapCounters counters = LdapDataProvider.getLdapCounters();
        System.out.println( "NUMBER OF READS: " + counters.getRead() );
        System.out.println( "NUMBER OF SEARCHES: " + counters.getSearch() );
        System.out.println( "NUMBER OF COMPARES: " + counters.getCompare() );
        System.out.println( "NUMBER OF BINDS: " + counters.getBind() );
        System.out.println( "NUMBER OF ADDS: " + counters.getAdd() );
        System.out.println( "NUMBER OF MODS: " + counters.getMod() );
        System.out.println( "NUMBER OF DELETES: " + counters.getDelete() );
    }


    /**
     *
     * @return
     * @throws org.apache.directory.fortress.core.SecurityException
     */
    private AdminMgr getManagedAdminMgr() throws SecurityException
    {
        if ( FortressJUnitTest.isAdminEnabled() && adminSess == null )
        {
            adminSess = DelegatedMgrImplTest.createAdminSession();
        }

        return AdminMgrFactory.createInstance( TestUtils.getContext(), adminSess );
    }


    /**
     * @param uArray
     */
    private void addUsers( String msg, String[][] uArray, boolean isAdmin )
    {
        LogUtil.logIt( msg );
        
        try
        {
            AdminMgr adminMgr;
            
            if ( isAdmin )
            {
                adminMgr = getManagedAdminMgr();
            }
            else
            {
                adminMgr = AdminMgrFactory.createInstance( TestUtils.getContext() );
            }
            
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                adminMgr.addUser( user );
                LOG.debug( "addUsers user [" + user.getUserId() + "] successful" );
                // Does User have Role assignments?
                Set<String> asgnRoles = UserTestData.getAssignedRoles( usr );
                if ( asgnRoles != null )
                {
                    for ( String name : asgnRoles )
                    {
                        adminMgr.assignUser( new UserRole( user.getUserId(), name ) );
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            ex.printStackTrace();
            LOG.error(
                "addUsers: caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    @Test
    public void testAddUser()
    {
        // Add the mandatory elements
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );

        //     public User addUser(User user)
        addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        addUsers( "ADD-USRS TU2", UserTestData.USERS_TU2, true );
        addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );
        addUsers( "ADD-USRS TU5", UserTestData.USERS_TU5, true );
        addUsers( "ADD-USRS TU6", UserTestData.USERS_TU6, true );
        addUsers( "ADD-USRS TU7_HIER", UserTestData.USERS_TU7_HIER, true );
        addUsers( "ADD-USRS TU8_SSD", UserTestData.USERS_TU8_SSD, true );
        addUsers( "ADD-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, true );
        addUsers( "ADD-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, true );
        addUsers( "ADD-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, true );
        addUsers( "ADD-USRS TU12_DSD", UserTestData.USERS_TU12_DSD, true );
        addUsers( "ADD-USRS TU13_DSD_HIER", UserTestData.USERS_TU13_DSD_HIER, true );
        addUsers( "ADD-USRS TU14_DSD_HIER", UserTestData.USERS_TU14_DSD_HIER, true );
        addUsers( "ADD-USRS TU15_DSD_HIER", UserTestData.USERS_TU15_DSD_HIER, true );
    }
}
