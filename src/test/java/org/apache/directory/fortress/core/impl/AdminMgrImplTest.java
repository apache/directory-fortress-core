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


import java.util.List;
import java.util.Set;

import org.apache.directory.fortress.core.AdminMgr;
import org.apache.directory.fortress.core.AdminMgrFactory;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.ReviewMgr;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.ConstraintUtil;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.PermissionAttribute;
import org.apache.directory.fortress.core.model.PermissionAttributeSet;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.RoleConstraint;
import org.apache.directory.fortress.core.model.SDSet;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * AdminMgrImpl Tester.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AdminMgrImplTest extends TestCase
{
    private static final String CLS_NM = AdminMgrImplTest.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static Session adminSess = null;


    public AdminMgrImplTest( String name )
    {
        super( name );
    }


    public void setUp() throws Exception
    {
        super.setUp();
    }


    public void tearDown() throws Exception
    {
        super.tearDown();
    }


    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        /*
        suite.addTest(new AdminMgrImplTest("testDelRoleDescendant"));
        suite.addTest(new AdminMgrImplTest("testAddRoleDescendant"));
        suite.addTest(new AdminMgrImplTest("testDelRoleAscendant"));
        suite.addTest(new AdminMgrImplTest("testAddRoleAscendants"));
        suite.addTest( new AdminMgrImplTest( "testDeleteSsdRoleMember" ) );
        suite.addTest( new AdminMgrImplTest( "testDeleteSsdSet" ) );
        suite.addTest( new AdminMgrImplTest( "testCreateSsdSet" ) );
        suite.addTest( new AdminMgrImplTest( "testAddSsdRoleMember" ) );
        */

        suite.addTest( new AdminMgrImplTest( "testAddUserRoleConstraint" ) );
        return suite;
    }


    public void testAddUser()
    {

        //     public User addUser(User user)
        addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        addUsers( "ADD-USRS TU2", UserTestData.USERS_TU2, true );
        addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );
        addUsers( "ADD-USRS TU5", UserTestData.USERS_TU5, true );
        if ( FortressJUnitTest.isFirstRun() )
        {
            addUsers( "ADD-USRS TU6", UserTestData.USERS_TU6, true );
        }
        addUsers( "ADD-USRS TU7_HIER", UserTestData.USERS_TU7_HIER, true );
        addUsers( "ADD-USRS TU8_SSD", UserTestData.USERS_TU8_SSD, true );
        addUsers( "ADD-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, true );
        addUsers( "ADD-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, true );
        addUsers( "ADD-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, true );
        addUsers( "ADD-USRS TU12_DSD", UserTestData.USERS_TU12_DSD, true );
        addUsers( "ADD-USRS TU13_DSD_HIER", UserTestData.USERS_TU13_DSD_HIER, true );
        addUsers( "ADD-USRS TU14_DSD_HIER", UserTestData.USERS_TU14_DSD_HIER, true );
        addUsers( "ADD-USRS TU15_DSD_HIER", UserTestData.USERS_TU15_DSD_HIER, true );
        addUsers( "ADD-USRS TU18 TR6_DESC", UserTestData.USERS_TU18U_TR6_DESC, true );
        addUsers( "ADD-USRS TU19 TR7_ASC", UserTestData.USERS_TU19U_TR7_ASC, true );
        addUsers( "ADD-USRS TU20 TR5_HIER", UserTestData.USERS_TU20U_TR5B, true );
        addUsers( "ADD-USRS TU21 DSD_BRUNO", UserTestData.USERS_TU21_DSD_BRUNO, true );
        addUsers( "ADD-USRS TU22 ABAC", UserTestData.USERS_TU22_ABAC, true );
    }


    /**
     * @param uArray
     */
    public static void addUsers( String msg, String[][] uArray, boolean isAdmin )
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


    public void testDeleteUser()
    {
        //     public void disableUser(User user)
        deleteUsers( "DEL-USRS TU1", UserTestData.USERS_TU1, false, true );
        deleteUsers( "DEL-USRS TU2", UserTestData.USERS_TU2, false, true );
        deleteUsers( "DEL-USRS TU3", UserTestData.USERS_TU3, false, true );
        deleteUsers( "DEL-USRS TU4", UserTestData.USERS_TU4, false, true );
        deleteUsers( "DEL-USRS TU5", UserTestData.USERS_TU5, false, true );
        deleteUsers( "DEL-USRS TU6", UserTestData.USERS_TU6, false, true );
        deleteUsers( "DEL-USRS TU7_HIER", UserTestData.USERS_TU7_HIER, false, true );
        deleteUsers( "DEL-USRS TU8_SSD", UserTestData.USERS_TU8_SSD, false, true );
        deleteUsers( "DEL-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, false, true );
        deleteUsers( "DEL-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, false, true );
        deleteUsers( "DEL-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, false, true );
        deleteUsers( "DEL-USRS TU12_DSD", UserTestData.USERS_TU12_DSD, false, true );
        deleteUsers( "DEL-USRS TU13_DSD_HIER", UserTestData.USERS_TU13_DSD_HIER, false, true );
        deleteUsers( "DEL-USRS TU14_DSD_HIER", UserTestData.USERS_TU14_DSD_HIER, false, true );
        deleteUsers( "DEL-USRS TU15_DSD_HIER", UserTestData.USERS_TU15_DSD_HIER, false, true );
    }


    /**
     * @param uArray
     */
    public static void deleteUsers( String msg, String[][] uArray, boolean force, boolean isAdmin )
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
                User user = new User();
                user.setUserId( UserTestData.getUserId( usr ) );
                try
                {
                    if ( force )
                    {
                        adminMgr.deleteUser( user );
                        LOG.debug( "deleteUsers force user [" + user.getUserId() + "] successful" );
                    }
                    else
                    {
                        adminMgr.disableUser( user );
                        LOG.debug( "deleteUsers user [" + user.getUserId() + "] successful" );
                    }
                }
                catch ( SecurityException ex )
                {
                    // don't fail test if user was a system user:
                    if ( ex.getErrorId() != GlobalErrIds.USER_PLCY_VIOLATION )
                    {
                        LOG.error( "deleteUsers caught SecurityException rc=" + ex.getErrorId() + ", msg="
                            + ex.getMessage(), ex );
                        fail( ex.getMessage() );
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "deleteUsers caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testForceDeleteUser()
    {
        //TODO: Test goes here...
        //     public boolean deleteUser(User user)
        //     public void disableUser(User user)
        deleteUsers( "FDEL-USRS TU1", UserTestData.USERS_TU1, true, true );
        deleteUsers( "FDEL-USRS TU2", UserTestData.USERS_TU2, true, true );
        deleteUsers( "FDEL-USRS TU3", UserTestData.USERS_TU3, true, true );
        deleteUsers( "FDEL-USRS TU4", UserTestData.USERS_TU4, true, true );
        deleteUsers( "FDEL-USRS TU5", UserTestData.USERS_TU5, true, true );
        deleteUsers( "FDEL-USRS TU6", UserTestData.USERS_TU6, true, true );
        deleteUsers( "FDEL-USRS TU7_HIER", UserTestData.USERS_TU7_HIER, true, true );
        deleteUsers( "FDEL-USRS TU8_SSD", UserTestData.USERS_TU8_SSD, true, true );
        deleteUsers( "FDEL-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, true, true );
        deleteUsers( "FDEL-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, true, true );
        deleteUsers( "FDEL-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, true, true );
        deleteUsers( "FDEL-USRS TU12_DSD", UserTestData.USERS_TU12_DSD, true, true );
        deleteUsers( "FDEL-USRS TU13_DSD_HIER", UserTestData.USERS_TU13_DSD_HIER, true, true );
        deleteUsers( "FDEL-USRS TU14_DSD_HIER", UserTestData.USERS_TU14_DSD_HIER, true, true );
        deleteUsers( "FDEL-USRS TU15_DSD_HIER", UserTestData.USERS_TU15_DSD_HIER, true, true );
        deleteUsers( "FDEL-USRS TU18_TR6_DESC", UserTestData.USERS_TU18U_TR6_DESC, true, true );
        deleteUsers( "FDEL-USRS TU19_TR7_ASC", UserTestData.USERS_TU19U_TR7_ASC, true, true );
        deleteUsers( "FDEL-USRS TU20_TR5_HIER", UserTestData.USERS_TU20U_TR5B, true, true );
        deleteUsers( "FDEL-USRS TU21 DSD_BRUNO", UserTestData.USERS_TU21_DSD_BRUNO, true, true );
        deleteUsers( "DEL-USRS TU22_ABAC", UserTestData.USERS_TU22_ABAC, true, true );
    }


    public void testUpdateUser()
    {
        //     public User updateUser(User user)
        updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );

    }


    /**
     * @param uArray
     */
    public static void updateUsers( String msg, String[][] uArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                adminMgr.updateUser( user );
                LOG.debug( "updateUsers user [" + user.getUserId() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "updateUsers: caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    public void testChangePassword()
    {
        //     public void changePassword(User user, String newPassword)
        changePasswords( "RESET PW TU2_RST TU2_CHG", UserTestData.USERS_TU2_RST, UserTestData.USERS_TU2_CHG );
    }


    /**
     * @param msg
     * @param uOldArray
     * @param uNewArray
     */
    void changePasswords( String msg, String[][] uOldArray, String[][] uNewArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int i = 0;
            for ( String[] usr : uOldArray )
            {
                User user = UserTestData.getUser( usr );
                adminMgr.changePassword( user, UserTestData.getPassword( uNewArray[i++] ) );
                LOG.debug( "changePasswords user [" + user.getUserId() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "changePasswords: caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testLockUserAccount()
    {
        //     public void lockUserAccount(User user)
        lockUsers( "LCK-PWS TU1_UPD", UserTestData.USERS_TU1_UPD );
        lockUsers( "LCK-PWS TU3", UserTestData.USERS_TU3 );
        lockUsers( "LCK-PWS TU4", UserTestData.USERS_TU4 );
    }


    /**
     * @param msg
     * @param uArray
     */
    void lockUsers( String msg, String[][] uArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                adminMgr.lockUserAccount( user );
                LOG.debug( "lockUsers user [" + user.getUserId() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "lockUsers: caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testUnlockUserAccount()
    {
        //     public void unlockUserAccount(User user)
        unlockUsers( "UNLCK-PWS TU1_UPD", UserTestData.USERS_TU1_UPD );
        unlockUsers( "UNLCK-PWS TU3", UserTestData.USERS_TU3 );
        unlockUsers( "UNLCK-PWS TU4", UserTestData.USERS_TU4 );
    }


    /**
     * @param msg
     * @param uArray
     */
    void unlockUsers( String msg, String[][] uArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                adminMgr.unlockUserAccount( user );
                LOG.debug( "unlockUsers user [" + user.getUserId() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "unlockUsers: caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    public void testResetPassword()
    {
        //     public void resetPassword(User user, String newPassword)
        resetPasswords( "RST-PW TU2_RST", UserTestData.USERS_TU2_RST );
    }


    /**
     * @param msg
     * @param uArray
     */
    void resetPasswords( String msg, String[][] uArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                adminMgr.resetPassword( user, UserTestData.getPassword( usr ) );
                LOG.debug( "resetPasswords user [" + user.getUserId() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "resetPasswords: caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    public void testAddRole()
    {
        //     public Role addRole(Role role)
        addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );
        addRoles( "ADD-RLS TR4", RoleTestData.ROLES_TR4 );
        addRoles( "ADD-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );
        addRoles( "ADD-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B );
        addRoles( "ADD-RLS ROLES_TR8_SSD", RoleTestData.ROLES_TR8_SSD );
        addRoles( "ADD-RLS ROLES_TR9_SSD", RoleTestData.ROLES_TR9_SSD );
        addRoles( "ADD-RLS ROLES_TR10_SSD", RoleTestData.ROLES_TR10_SSD );
        addRoles( "ADD-RLS ROLES_TR8_DSD", RoleTestData.ROLES_TR8_DSD );
        addRoles( "ADD-RLS ROLES_TR9_DSD", RoleTestData.ROLES_TR9_DSD );
        addRoles( "ADD-RLS ROLES_TR10_DSD", RoleTestData.ROLES_TR10_DSD );
        addRoles( "ADD-RLS ROLES_TR16_SD", RoleTestData.ROLES_TR16_SD );
        addRoles( "ADD-RLS ROLES_TR17_DSD_BRUNO", RoleTestData.ROLES_TR17_DSD_BRUNO );
        addRoles( "ADD-RLS ROLES_ABAC_WASHERS", RoleTestData.ROLES_ABAC_WASHERS );
        addRoles( "ADD-RLS ROLES_ABAC_TELLERS", RoleTestData.ROLES_ABAC_TELLERS );
        addRoles( "ADD-RLS ROLES_ABAC_USERS", RoleTestData.ROLES_ABAC_USERS );
    }


    /**
     * @param rArray
     */
    public static void addRoles( String msg, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] rle : rArray )
            {
                Role role = RoleTestData.getRole( rle );
                Role entity = adminMgr.addRole( role );
                LOG.debug( "addRoles role [" + entity.getName() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "addRoles caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testDeleteRole()
    {
        //     public void deleteRole(Role role)
        deleteRoles( "DEL-RLS TR1", RoleTestData.ROLES_TR1 );
        deleteRoles( "DEL-RLS TR2", RoleTestData.ROLES_TR2 );
        deleteRoles( "DEL-RLS TR3", RoleTestData.ROLES_TR3 );
        deleteRoles( "DEL-RLS TR4", RoleTestData.ROLES_TR4 );
        deleteRoles( "DEL-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );
        deleteRoles( "DEL-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B );
        deleteRoles( "DEL-RLS ROLES_TR8_SSD", RoleTestData.ROLES_TR8_SSD );
        deleteRoles( "DEL-RLS ROLES_TR9_SSD", RoleTestData.ROLES_TR9_SSD );
        deleteRoles( "DEL-RLS ROLES_TR10_SSD", RoleTestData.ROLES_TR10_SSD );
        deleteRoles( "DEL-RLS ROLES_TR8_DSD", RoleTestData.ROLES_TR8_DSD );
        deleteRoles( "DEL-RLS ROLES_TR9_DSD", RoleTestData.ROLES_TR9_DSD );
        deleteRoles( "DEL-RLS ROLES_TR10_DSD", RoleTestData.ROLES_TR10_DSD );
        deleteRoles( "DEL-RLS ROLES_TR16_SD", RoleTestData.ROLES_TR16_SD );
        deleteRoles( "DEL-RLS ROLES_TR17_DSD_BRUNO", RoleTestData.ROLES_TR17_DSD_BRUNO );
        deleteRoles( "DEL-RLS ROLES_ABAC_WASHERS", RoleTestData.ROLES_ABAC_WASHERS );
        deleteRoles( "DEL-RLS ROLES_ABAC_TELLERS", RoleTestData.ROLES_ABAC_TELLERS );
        deleteRoles( "DEL-RLS ROLES_ABAC_USERS", RoleTestData.ROLES_ABAC_USERS );
    }


    /**
     * @param rArray
     */
    public static void deleteRoles( String msg, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] rle : rArray )
            {
                Role role = new Role();
                role.setName( RoleTestData.getName( rle ) );
                adminMgr.deleteRole( role );
                LOG.debug( "deleteRoles role [" + role.getName() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "deleteRoles caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testUpdateRole()
    {
        //     public Role updateRole(Role role)
        updateRoles( "UPD-RLS TR4_UPD", RoleTestData.ROLES_TR4_UPD );
    }


    /**
     * @param msg
     * @param rArray
     */
    public static void updateRoles( String msg, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] rle : rArray )
            {
                Role role = RoleTestData.getRole( rle );
                Role entity = adminMgr.updateRole( role );
                LOG.debug( "updateRoles role [" + entity.getName() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "updateRoles caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testAddRoleDescendant()
    {
        //TODO: Test goes here...
        //     public void addDescendant(Role parentRole, Role childRole)
        addRoleDescendant( "ADD-RLS-TR6-DESC", RoleTestData.ROLES_TR6_DESC );
        addRoleDescendant( "ADD-RLS-TR11-DESC-SSD", RoleTestData.ROLES_TR11_DESC_SSD );
        addRoleDescendant( "ADD-RLS-TR12-DESC-SSD", RoleTestData.ROLES_TR12_DESC_SSD );
        addRoleDescendant( "ADD-RLS-TR13-DESC-SSD", RoleTestData.ROLES_TR13_DESC_SSD );
        addRoleDescendant( "ADD-RLS-TR11-DESC-DSD", RoleTestData.ROLES_TR11_DESC_DSD );
        addRoleDescendant( "ADD-RLS-TR12-DESC-DSD", RoleTestData.ROLES_TR12_DESC_DSD );
        addRoleDescendant( "ADD-RLS-TR13-DESC-DSD", RoleTestData.ROLES_TR13_DESC_DSD );
    }


    /**
     *
     * @param msg
     * @param rArray
     */
    public static void addRoleDescendant( String msg, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int ctr = 0;
            for ( String[] rle : rArray )
            {
                Role role = RoleTestData.getRole( rle );
                if ( ctr++ == 0 || RoleTestData.isTree( rle ) )
                {
                    adminMgr.addRole( role );
                    LOG.debug( "addDescendant add role [" + role.getName() + "] successful" );
                }

                // use list because order is important for test structure:
                List<String> descs = RoleTestData.getRelationshipList( rle );
                if ( descs != null )
                {
                    if ( RoleTestData.isTree( rle ) )
                    {
                        Role parent = role;
                        for ( String desc : descs )
                        {
                            Role child = new Role( desc );
                            adminMgr.addDescendant( parent, new Role( desc ) );
                            LOG.debug( "addDescendant asc role [" + role.getName() + "] desc role [" + desc
                                + "] successful" );
                            parent = child;
                        }
                    }
                    else
                    {
                        for ( String desc : descs )
                        {
                            adminMgr.addDescendant( role, new Role( desc ) );
                            LOG.debug( "addDescendant asc role [" + role.getName() + "] desc role [" + desc
                                + "] successful" );
                        }
                    }
                }

                Set<String> inheritances = RoleTestData.getInheritances( rle );
                if ( inheritances != null )
                {
                    for ( String desc : inheritances )
                    {
                        adminMgr.addInheritance( role, new Role( desc ) );
                        LOG.debug( "addDescendant asc role [" + role.getName() + "] desc role [" + desc
                            + "] successful" );
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "addDescendant caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    public void testDelRoleDescendant() throws SecurityException
    {
        //TODO: Test goes here...
        //     public void addDescendant(Role parentRole, Role childRole)
        //delRoleDescendant("DEL-RLS-DESC");
        delRoleDescendant( "DEL-RLS-TR6-DESC", RoleTestData.ROLES_TR6_DESC );
        delRoleDescendant( "DEL-RLS-TR11-DESC-SSD", RoleTestData.ROLES_TR11_DESC_SSD );
        delRoleDescendant( "DEL-RLS-TR12-DESC-SSD", RoleTestData.ROLES_TR12_DESC_SSD );
        delRoleDescendant( "DEL-RLS-TR13-DESC-SSD", RoleTestData.ROLES_TR13_DESC_SSD );
        delRoleDescendant( "DEL-RLS-TR11-DESC-DSD", RoleTestData.ROLES_TR11_DESC_DSD );
        delRoleDescendant( "DEL-RLS-TR12-DESC-DSD", RoleTestData.ROLES_TR12_DESC_DSD );
        delRoleDescendant( "DEL-RLS-TR13-DESC-DSD", RoleTestData.ROLES_TR13_DESC_DSD );
    }


    /**
     *
     * @param msg
     */
    private void delRoleDescendant( String msg, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] rle : rArray )
            {
                Role role = RoleTestData.getRole( rle );
                // use list because order is important for test structure:
                List<String> descs = RoleTestData.getRelationshipList( rle );
                if ( descs != null )
                {
                    if ( RoleTestData.isTree( rle ) )
                    {
                        Role parent = role;
                        for ( String desc : descs )
                        {
                            Role child = new Role( desc );
                            adminMgr.deleteInheritance( parent, new Role( desc ) );
                            LOG.debug( "delRoleDescendant asc role [" + role.getName() + "] desc role ["
                                + desc + "] successful" );
                            parent = child;
                        }
                    }
                    else
                    {
                        for ( String desc : descs )
                        {
                            adminMgr.deleteInheritance( role, new Role( desc ) );
                            LOG.debug( "delRoleDescendant asc role [" + role.getName() + "] desc role ["
                                + desc + "] successful" );
                        }
                    }
                }

                Set<String> inheritances = RoleTestData.getInheritances( rle );
                if ( inheritances != null )
                {
                    for ( String desc : inheritances )
                    {
                        adminMgr.deleteInheritance( role, new Role( desc ) );
                        LOG.debug( "delRoleDescendant asc role [" + role.getName() + "] desc role [" + desc
                            + "] successful" );
                    }
                }
                adminMgr.deleteRole( role );
                LOG.debug( "delRoleDescendant remove asc role [" + role.getName() + "] successful" );
            }

            // cleanup the last row of descendants from roles data set.
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            String roleSrchVal = TestUtils.getSrchValue( RoleTestData.getName( rArray[0] ) );
            List<Role> cleanup = reviewMgr.findRoles( roleSrchVal );
            for ( Role re : cleanup )
            {
                adminMgr.deleteRole( re );
                LOG.debug( "delRoleDescendant cleanup role [" + re.getName() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "delRoleDescendant caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testAddRoleAscendants()
    {
        //TODO: Test goes here...
        //     public void addDescendant(Role parentRole, Role childRole)
        addRoleAscendant( "ADD-RLS-TR7-ASC", RoleTestData.ROLES_TR7_ASC );
    }


    /**
     * 
     * @param msg
     * @param rArray
     */
    public static void addRoleAscendant( String msg, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] rle : rArray )
            {
                Role role = RoleTestData.getRole( rle );
                if ( RoleTestData.isCreate( rle ) )
                {
                    adminMgr.addRole( role );
                    LOG.debug( "addAscendant add role [" + role.getName() + "] successful" );
                }

                Set<String> ascs = RoleTestData.getRelationships( rle );
                if ( ascs != null )
                {
                    for ( String asc : ascs )
                    {
                        adminMgr.addAscendant( role, new Role( asc ) );
                        LOG.debug( "addAscendant desc role [" + role.getName() + "] asc role [" + asc
                            + "] successful" );
                    }
                }

                Set<String> inheritances = RoleTestData.getInheritances( rle );
                if ( inheritances != null )
                {
                    for ( String asc : inheritances )
                    {
                        adminMgr.addInheritance( new Role( asc ), role );
                        LOG.debug( "addAscendant desc role [" + role.getName() + "] asc role [" + asc
                            + "] successful" );
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "addDescendant caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    public void testDelRoleAscendant()
    {
        //TODO: Test goes here...
        //     public void addDescendant(Role parentRole, Role childRole)
        delRoleAscendant( "DEL-RLS-TR6-ASC", RoleTestData.ROLES_TR7_ASC );
    }


    /**
     *
     * @param msg
     * @param rArray
     */
    private void delRoleAscendant( String msg, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] rle : rArray )
            {
                Role role = RoleTestData.getRole( rle );
                Set<String> ascs = RoleTestData.getRelationships( rle );
                if ( ascs != null )
                {
                    for ( String asc : ascs )
                    {
                        adminMgr.deleteInheritance( new Role( asc ), role );
                        LOG.debug( "delRoleAscendant desc role [" + role.getName() + "] asc role [" + asc
                            + "] successful" );
                    }
                }

                Set<String> inheritances = RoleTestData.getInheritances( rle );
                if ( inheritances != null )
                {
                    for ( String asc : inheritances )
                    {
                        adminMgr.deleteInheritance( new Role( asc ), role );
                        LOG.debug( "delRoleAscendant desc role [" + role.getName() + "] asc role [" + asc
                            + "] successful" );
                    }
                }
                adminMgr.deleteRole( role );
                LOG.debug( "delRoleAscendant remove desc role [" + role.getName() + "] successful" );
            }

            // cleanup the top ascendant from roles data set.
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            String roleSrchVal = RoleTestData.getName( rArray[0] );
            roleSrchVal = roleSrchVal.substring( 0, roleSrchVal.length() - 8 );
            List<Role> cleanup = reviewMgr.findRoles( roleSrchVal );
            for ( Role re : cleanup )
            {
                adminMgr.deleteRole( re );
                LOG.debug( "delRoleAscendant cleanup role [" + re.getName() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "delRoleAscendant caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testAddRoleInheritance()
    {
        //TODO: Test goes here...
        //     public void addInheritance(Role parentRole, Role childRole)
        addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );
        addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B );
    }


    /**
     * @param rArray
     */
    public static void addInheritedRoles( String msg, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] rle : rArray )
            {
                Role role = RoleTestData.getRole( rle );
                Set<String> parents = RoleTestData.getRelationships( rle );
                if ( parents != null )
                {
                    for ( String pRole : parents )
                    {
                        adminMgr.addInheritance( new Role( pRole ), role );
                        LOG.debug( "addInheritedRoles child role [" + role.getName() + "] parent role ["
                            + pRole + "] successful" );
                    }
                }

            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "addInheritedRoles caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testDeleteRoleInheritance()
    {
        //TODO: Test goes here...
        //     public void addInheritance(Role parentRole, Role childRole)
        deleteInheritedRoles( "DEL-INHERIT-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );
        deleteInheritedRoles( "DEL-INHERIT-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B );
    }


    /**
     * @param rArray
     */
    public static void deleteInheritedRoles( String msg, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] rle : rArray )
            {
                Role role = RoleTestData.getRole( rle );
                Set<String> parents = RoleTestData.getRelationships( rle );
                if ( parents != null )
                {
                    for ( String pRole : parents )
                    {
                        adminMgr.deleteInheritance( new Role( pRole ), role );
                        LOG.debug( "deleteInheritedRoles child role [" + role.getName() + "] parent role ["
                            + pRole + "] successful" );
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "deleteInheritedRoles caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testCreateSsdSet()
    {
        //     public SDSet createSsdSet(SDSet ssdSet)
        createSsdSet( "ADD-SSD T1", RoleTestData.SSD_T1 );
        createSsdSet( "ADD-SSD T4", RoleTestData.SSD_T4 );
        createSsdSet( "ADD-SSD T5", RoleTestData.SSD_T5 );
        createSsdSet( "ADD-SSD T6", RoleTestData.SSD_T6 );
    }


    /**
     * @param sArray
     */
    public static void createSsdSet( String msg, String[][] sArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] ssdle : sArray )
            {
                SDSet ssd = RoleTestData.getSDSet( ssdle );
                SDSet entity = adminMgr.createSsdSet( ssd );
                LOG.debug( "createSsdSet SSD [" + entity.getName() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "createSsdSet caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    public void testCreateDsdSet()
    {
        //     public SDSet createDsdSet(SDSet dsdSet)
        createDsdSet( "ADD-DSD T1", RoleTestData.DSD_T1 );
        createDsdSet( "ADD-DSD T4", RoleTestData.DSD_T4 );
        createDsdSet( "ADD-DSD T5", RoleTestData.DSD_T5 );
        createDsdSet( "ADD-DSD T6", RoleTestData.DSD_T6 );
        createDsdSet( "ADD-DSD T8 BRUNO", RoleTestData.DSD_T8_BRUNO );
        createDsdSet( "ADD-DSD TR18 ABAC6", RoleTestData.DSD_TR18_ABAC );
    }


    /**
     * @param sArray
     */
    public static void createDsdSet( String msg, String[][] sArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] dsdle : sArray )
            {
                SDSet dsd = RoleTestData.getSDSet( dsdle );
                SDSet entity = adminMgr.createDsdSet( dsd );
                LOG.debug( "createDsdSet DSD [" + entity.getName() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "createDsdSet caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testDeleteSsdSet()
    {
        //     public SDSet deleteSsdSet(SDSet ssdSet)
        deleteSsdSet( "DEL-SSD T1", RoleTestData.SSD_T1 );
        deleteSsdSet( "DEL-SSD T4", RoleTestData.SSD_T4 );
        deleteSsdSet( "DEL-SSD T5", RoleTestData.SSD_T5 );
        deleteSsdSet( "DEL-SSD T6", RoleTestData.SSD_T6 );
        deleteSsdSet( "DEL-SSD T7", RoleTestData.SSD_T7 );
        deleteDsdSet( "DEL-DSD T7 BRUNO", RoleTestData.DSD_T8_BRUNO );
    }


    /**
     * @param sArray
     */
    private void deleteSsdSet( String msg, String[][] sArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] ssdle : sArray )
            {
                SDSet ssd = RoleTestData.getSDSet( ssdle );
                adminMgr.deleteSsdSet( ssd );
                LOG.debug( "deleteSsdSet role [" + ssd.getName() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "deleteSsdSet caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    public void testDeleteDsdSet()
    {
        //     public SDSet deleteDsdSet(SDSet dsdSet)
        deleteDsdSet( "DEL-DSD T1", RoleTestData.DSD_T1 );
        deleteDsdSet( "DEL-DSD T4", RoleTestData.DSD_T4 );
        deleteDsdSet( "DEL-DSD T5", RoleTestData.DSD_T5 );
        deleteDsdSet( "DEL-DSD T6", RoleTestData.DSD_T6 );
        deleteDsdSet( "DEL-DSD T8", RoleTestData.DSD_T7 );
        deleteDsdSet( "DEL-DSD TR18 ABAC6", RoleTestData.DSD_TR18_ABAC );
    }


    /**
     * @param sArray
     */
    private void deleteDsdSet( String msg, String[][] sArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] dsdle : sArray )
            {
                SDSet dsd = RoleTestData.getSDSet( dsdle );
                adminMgr.deleteDsdSet( dsd );
                LOG.debug( "deleteDsdSet role [" + dsd.getName() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "deleteDsdSet caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    public void testAddSsdRoleMember()
    {
        //     public SDSet addSsdRoleMember(SDSet ssdSet, Role role)
        addSsdRoleMember( "ADD-MEM-SSD T2 TR9", RoleTestData.SSD_T2, RoleTestData.ROLES_TR9_SSD );
        addSsdRoleMember( "ADD-MEM-SSD T3 TR10", RoleTestData.SSD_T3, RoleTestData.ROLES_TR10_SSD );

    }


    /**
     * @param sArray
     */
    public static void addSsdRoleMember( String msg, String[][] sArray, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for ( String[] ssdle : sArray )
            {
                SDSet ssd = RoleTestData.getSDSet( ssdle );
                SDSet entity = adminMgr.createSsdSet( ssd );
                LOG.debug( "addSsdRoleMember SSD [" + entity.getName() + "] successful" );
                for ( String[] rle : rArray )
                {
                    Role role = RoleTestData.getRole( rle );
                    adminMgr.addSsdRoleMember( ssd, role );
                    List<SDSet> ssdSets = reviewMgr.ssdRoleSets( role );
                    assertNotNull( ssdSets );
                    assertTrue( CLS_NM + "addSsdRoleMember list size check", ssdSets.size() == 1 );
                    SDSet ssd2 = ssdSets.get( 0 );
                    assertTrue( CLS_NM + "addSsdRoleMember SSD name check", ssd.getName().equals( ssd2.getName() ) );
                    assertTrue( CLS_NM + "addSsdRoleMember SSD role check", ssd2.getMembers().contains( role.getName() ) );
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "addSsdRoleMember caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testAddDsdRoleMember()
    {
        //     public SDSet addDsdRoleMember(SDSet dsdSet, Role role)
        addDsdRoleMember( "ADD-MEM-DSD T2 TR9", RoleTestData.DSD_T2, RoleTestData.ROLES_TR9_DSD );
        addDsdRoleMember( "ADD-MEM-DSD T3 TR10", RoleTestData.DSD_T3, RoleTestData.ROLES_TR10_DSD );

    }


    /**
     * @param sArray
     */
    public static void addDsdRoleMember( String msg, String[][] sArray, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for ( String[] dsdle : sArray )
            {
                SDSet dsd = RoleTestData.getSDSet( dsdle );
                SDSet entity = adminMgr.createDsdSet( dsd );
                LOG.debug( "addDsdRoleMember DSD [" + entity.getName() + "] successful" );
                for ( String[] rle : rArray )
                {
                    Role role = RoleTestData.getRole( rle );
                    adminMgr.addDsdRoleMember( dsd, role );
                    List<SDSet> dsdSets = reviewMgr.dsdRoleSets( role );
                    assertNotNull( dsdSets );
                    assertTrue( CLS_NM + "addDsdRoleMember list size check", dsdSets.size() == 1 );
                    SDSet dsd2 = dsdSets.get( 0 );
                    assertTrue( CLS_NM + "addDsdRoleMember DSD name check", dsd.getName().equals( dsd2.getName() ) );
                    assertTrue( CLS_NM + "addDsdRoleMember DSD role check", dsd2.getMembers().contains( role.getName() ) );
                    //assertTrue(CLS_NM + "addDsdRoleMember DSD role check", dsd2.getMembers().containsKey(role.getName()));
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "addDsdRoleMember caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testDeleteSsdRoleMember()
    {
        //public SSDSet deleteSsdRoleMember(SSDSet ssdSet, Role role)
        deleteSsdRoleMember( "DEL-MEM-SSD T2 TR9", RoleTestData.SSD_T2, RoleTestData.ROLES_TR9_SSD );
        deleteSsdRoleMember( "DEL-MEM-SSD T3 TR10", RoleTestData.SSD_T3, RoleTestData.ROLES_TR10_SSD );

    }


    /**
     * @param sArray
     */
    private void deleteSsdRoleMember( String msg, String[][] sArray, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for ( String[] ssdle : sArray )
            {
                SDSet ssd = RoleTestData.getSDSet( ssdle );
                // first add all of the roles as members of SSD entity:
                for ( String[] rle : rArray )
                {
                    Role role = RoleTestData.getRole( rle );
                    ssd.addMember( role.getName() );
                }
                // now iterate over roles and remove from SSD set one at a time.
                for ( String[] rle : rArray )
                {
                    Role role = RoleTestData.getRole( rle );
                    adminMgr.deleteSsdRoleMember( ssd, role );
                    List<SDSet> ssdSets = reviewMgr.ssdRoleSets( role );
                    assertTrue( CLS_NM + "deleteSsdRoleMember list size check ssdSets size [" + ssdSets.size() + "]",
                        ssdSets.size() == 0 );
                }
                adminMgr.deleteSsdSet( ssd );
                LOG.debug( "deleteSsdRoleMember SSD [" + ssd.getName() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "deleteSsdRoleMember caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testDeleteDsdRoleMember()
    {
        //public SSDSet deleteDsdRoleMember(DSDSet dsdSet, Role role)
        deleteDsdRoleMember( "DEL-MEM-DSD T2 TR9", RoleTestData.DSD_T2, RoleTestData.ROLES_TR9_DSD );
        deleteDsdRoleMember( "DEL-MEM-DSD T3 TR10", RoleTestData.DSD_T3, RoleTestData.ROLES_TR10_DSD );

    }


    /**
     * @param sArray
     */
    private void deleteDsdRoleMember( String msg, String[][] sArray, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for ( String[] dsdle : sArray )
            {
                SDSet dsd = RoleTestData.getSDSet( dsdle );
                // first add all of the roles as members of SSD entity:
                for ( String[] rle : rArray )
                {
                    Role role = RoleTestData.getRole( rle );
                    dsd.addMember( role.getName() );
                }
                // now iterate over roles and remove from DSD set one at a time.
                for ( String[] rle : rArray )
                {
                    Role role = RoleTestData.getRole( rle );
                    adminMgr.deleteDsdRoleMember( dsd, role );
                    List<SDSet> dsdSets = reviewMgr.dsdRoleSets( role );
                    assertTrue( CLS_NM + "deleteDsdRoleMember list size check", dsdSets.size() == 0 );
                }
                adminMgr.deleteDsdSet( dsd );
                LOG.debug( "deletedsdRoleMember DSD [" + dsd.getName() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "deleteDsdRoleMember caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testSsdCardinality()
    {
        setSsdCardinality( "CARD-SSD T7 TR16", RoleTestData.SSD_T7, RoleTestData.ROLES_TR16_SD );
    }


    /**
     *
     * @param msg
     * @param sArray
     * @param rArray
     */
    public static void setSsdCardinality( String msg, String[][] sArray, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for ( String[] ssdle : sArray )
            {
                SDSet ssd = RoleTestData.getSDSet( ssdle );
                SDSet entity = adminMgr.createSsdSet( ssd );
                LOG.debug( "setSsdCardinality SSD [" + entity.getName() + "] successful" );
                int cardinality = 1;
                for ( String[] rle : rArray )
                {
                    Role role = RoleTestData.getRole( rle );
                    adminMgr.addSsdRoleMember( ssd, role );
                    adminMgr.setSsdSetCardinality( ssd, cardinality );
                    int currentCardinality = reviewMgr.ssdRoleSetCardinality( ssd );
                    assertTrue( CLS_NM + "setSsdCardinality cardinality check", currentCardinality == cardinality++ );
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "setSsdCardinality caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testDsdCardinality()
    {
        setDsdCardinality( "CARD-DSD T7 TR16", RoleTestData.DSD_T7, RoleTestData.ROLES_TR16_SD );
    }


    /**
     *
     * @param msg
     * @param sArray
     * @param rArray
     */
    public static void setDsdCardinality( String msg, String[][] sArray, String[][] rArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for ( String[] dsdle : sArray )
            {
                SDSet dsd = RoleTestData.getSDSet( dsdle );
                SDSet entity = adminMgr.createDsdSet( dsd );
                LOG.debug( "setDsdCardinality DSD [" + entity.getName() + "] successful" );
                int cardinality = 1;
                for ( String[] rle : rArray )
                {
                    Role role = RoleTestData.getRole( rle );
                    adminMgr.addDsdRoleMember( dsd, role );
                    adminMgr.setDsdSetCardinality( dsd, cardinality );
                    int currentCardinality = reviewMgr.dsdRoleSetCardinality( dsd );
                    assertTrue( CLS_NM + "setDsdCardinality cardinality check", currentCardinality == cardinality++ );
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "setDsdCardinality caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testAssignUser()
    {
        //     public void assignUser(User user, Role role)
        assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );
        assignUsers( "ASGN-USERS TU22 ABAC WASHERS", UserTestData.USERS_TU22_ABAC, RoleTestData.ROLES_ABAC_WASHERS, true, false );
        assignUsers( "ASGN-USERS TU22 ABAC TELLERS", UserTestData.USERS_TU22_ABAC, RoleTestData.ROLES_ABAC_TELLERS, true, false );
        assignUsers( "ASGN-USERS TU22 ABAC USERS", UserTestData.USERS_TU22_ABAC, RoleTestData.ROLES_ABAC_USERS, true, false );
        assignUsersH( "ASGN-USRS_H TU7 HIER TR5 HIER", UserTestData.USERS_TU7_HIER, RoleTestData.ROLES_TR5_HIER, true );
        assignUsersH( "ASGN-USRS_H TU20 TR5B HIER", UserTestData.USERS_TU20U_TR5B, RoleTestData.ROLES_TR5B, true );
        assignUsersSSD( "ASGN-USRS_SSDT1 TU8 SSD_T1", UserTestData.USERS_TU8_SSD, RoleTestData.SSD_T1 );
        assignUsersSSD( "ASGN-USRS_SSDT4B TU9 SSD_T4_B", UserTestData.USERS_TU9_SSD_HIER, RoleTestData.SSD_T4_B );
        assignUsersSSD( "ASGN-USRS_SSDT5B TU10 SSD_T5_B", UserTestData.USERS_TU10_SSD_HIER, RoleTestData.SSD_T5_B );
        assignUsersSSD( "ASGN-USRS_SSDT6B TU11 SSD_T6_B", UserTestData.USERS_TU11_SSD_HIER, RoleTestData.SSD_T6_B );
        assignUsersDSD( "ASGN-USRS_DSDT1 TU8 DSD_T1", UserTestData.USERS_TU8_SSD, RoleTestData.DSD_T1 );
        assignUsersDSD( "ASGN-USRS_DSDT4B TU9 DSD_T4_B", UserTestData.USERS_TU9_SSD_HIER, RoleTestData.DSD_T4_B );
        assignUsersDSD( "ASGN-USRS_DSDT5B TU10 DSD_T5_B", UserTestData.USERS_TU10_SSD_HIER, RoleTestData.DSD_T5_B );
        assignUsersDSD( "ASGN-USRS_DSDT6B TU11 DSD_T6_B", UserTestData.USERS_TU11_SSD_HIER, RoleTestData.DSD_T6_B );
        assignUsersDSD( "ASGN-USRS_DSDT1 TU12 DSD_T1", UserTestData.USERS_TU12_DSD, RoleTestData.DSD_T1 );
        assignUsersDSD( "ASGN-USRS_DSDT4B TU13 DSD_T4_B", UserTestData.USERS_TU13_DSD_HIER, RoleTestData.DSD_T4_B );
        assignUsersDSD( "ASGN-USRS_DSDT5B TU14 DSD_T5_B", UserTestData.USERS_TU14_DSD_HIER, RoleTestData.DSD_T5_B );
        assignUsersDSD( "ASGN-USRS_DSDT6B TU15 DSD_T6_B", UserTestData.USERS_TU15_DSD_HIER, RoleTestData.DSD_T6_B );
    }


    /**
     * Just call the oher one, passing 'true' for performListChck.
     *
     * @param msg
     * @param uArray
     * @param rArray
     */

    /**
     * Just call the oher one, passing 'true' for performListChck.
     *
     * @param msg
     * @param uArray
     * @param rArray
     * @param setTemporal
     */
    public static void assignUsers( String msg, String[][] uArray, String[][] rArray, boolean setTemporal )
    {
        assignUsers( msg, uArray, rArray, setTemporal, true );
    }

    /**
     * Assign the list of users to the list of roles. Perform some rudimentary testing to verify the assignments worked.
     *
     * @param msg
     * @param uArray
     * @param rArray
     * @param setTemporal
     */
    private static void assignUsers( String msg, String[][] uArray, String[][] rArray, boolean setTemporal, boolean performListCheck )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            int i = 0;
            for ( String[] usr : uArray )
            {
                i++;
                for ( String[] rle : rArray )
                {
                    User user = UserTestData.getUser( usr );
                    Role role = RoleTestData.getRole( rle );
                    UserRole uRole = new UserRole();
                    ConstraintUtil.copy( role, uRole );
                    if ( !setTemporal )
                    {
                        // test the default constraints for role
                        uRole = new UserRole( role.getName() );
                    }
                    uRole.setUserId( user.getUserId() );
                    adminMgr.assignUser( uRole );
                    LOG.debug( "assignUsers user [" + user.getUserId() + "] role [" + uRole.getName()
                        + "] successful" );

                    if(performListCheck)
                    {
                        // Let's double check the number of users not associated with role:
                        // This one retrieves the collection of all "roleOccupant" attributes associated with the role node:
                        List<String> users = reviewMgr.assignedUsers( RoleTestData.getRole( rle ), rArray.length );
                        // This one searches across all Users and pull back list of type "User":
                        List<User> users2 = reviewMgr.authorizedUsers( RoleTestData.getRole( rle ) );
                        assertNotNull( users );
                        assertNotNull( users2 );
                        assertTrue( CLS_NM + ".assignUsers list size check", i == users.size() );
                        assertTrue( CLS_NM + ".assignUsers list2 size check", i == users2.size() );
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "assignUsers user caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     * @param msg
     * @param uArray
     * @param rArray
     * @param setTemporal
     */
    public static void assignUsersH( String msg, String[][] uArray, String[][] rArray, boolean setTemporal )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int i = 0;
            for ( String[] usr : uArray )
            {
                Role role = RoleTestData.getRole( rArray[i] );
                UserRole uRole = new UserRole();
                uRole.setUserId( UserTestData.getUserId( usr ) );
                ConstraintUtil.copy( role, uRole );
                adminMgr.assignUser( uRole );
                i++;
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "assignUsersH user caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     * @param msg
     * @param uArray
     * @param sArray
     */
    public static void assignUsersSSD( String msg, String[][] uArray, String[][] sArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int i = 0;
            for ( String[] usr : uArray )
            {
                SDSet ssd = RoleTestData.getSDSet( sArray[i++] );
                //Set<String> roles = ssd.getMembers().keySet();
                Set<String> roles = ssd.getMembers();
                User user = UserTestData.getUser( usr );
                int j = 0;
                for ( String role : roles )
                {
                    j++;
                    try
                    {
                        UserRole uRole = new UserRole( user.getUserId(), role );
                        adminMgr.assignUser( uRole );
                        if ( j >= ssd.getCardinality() )
                        {
                            fail( CLS_NM + ".assignUsersSSD user [" + user.getUserId() + "] role [" + role + "] ssd ["
                                + ssd.getName() + "] cardinality [" + ssd.getCardinality() + "] count [" + j
                                + "] failed" );
                        }
                    }
                    catch ( SecurityException ex )
                    {
                        assertTrue( CLS_NM + ".assignUsersSSD cardinality test failed user [" + user.getUserId()
                            + "] role [" + role + "] ssd [" + ssd.getName() + "] cardinality [" + ssd.getCardinality()
                            + "] count [" + j + "]", j >= ( ssd.getCardinality() ) );
                        assertTrue( CLS_NM + ".assignUsersSSD cardinality test failed [" + UserTestData.getUserId( usr )
                            + "]", ex.getErrorId() == GlobalErrIds.SSD_VALIDATION_FAILED );
                        // still good, break from loop, we're done here
                        break;
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "assignUsersSSD caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     * @param msg
     * @param uArray
     * @param sArray
     */
    public static void assignUsersDSD( String msg, String[][] uArray, String[][] sArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int i = 0;
            for ( String[] usr : uArray )
            {
                SDSet dsd = RoleTestData.getSDSet( sArray[i++] );
                Set<String> roles = dsd.getMembers();
                User user = UserTestData.getUser( usr );
                int j = 0;
                for ( String role : roles )
                {
                    j++;
                    UserRole uRole = new UserRole( user.getUserId(), role );
                    try
                    {
                        adminMgr.assignUser( uRole );
                    }
                    catch ( SecurityException ex )
                    {
                        LOG.error( "assignUsersDSD caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex
                            .getMessage(), ex );
                        fail( CLS_NM + "assignUsersDSD user [" + user.getUserId() + "] role [" + role + "] dsd [" +
                            dsd.getName() + "] failed" );
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "assignUsersDSD caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }

    public void testDeassignUser()
    {
        //     public void deassignUser(User user, Role role)
        //deassignUsers( "DEASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1 );
        deassignUsers( "DEASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2 );
        deassignUsers( "DEASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3 );
        deassignUsers( "DEASGN-USERS TU22 ABAC WASHERS", UserTestData.USERS_TU22_ABAC, RoleTestData
            .ROLES_ABAC_WASHERS, false );
        deassignUsers( "DEASGN-USERS TU22 ABAC TELLERS", UserTestData.USERS_TU22_ABAC, RoleTestData.ROLES_ABAC_TELLERS, false );
        deassignUsers( "DEASGN-USERS TU22 ABAC USERS", UserTestData.USERS_TU22_ABAC, RoleTestData.ROLES_ABAC_USERS, false );
        deassignUsersH( "DEASGN-USRS_H TU7 TR5 HIER", UserTestData.USERS_TU7_HIER, RoleTestData.ROLES_TR5_HIER );
        deassignUsersH( "DEASGN-USRS_H TU20 TR5B HIER", UserTestData.USERS_TU20U_TR5B, RoleTestData.ROLES_TR5B );
    }


    /**
     * Just call the oher one, passing 'true' for performListChck.
     *
     * @param msg
     * @param uArray
     * @param rArray
     */
    void deassignUsers( String msg, String[][] uArray, String[][] rArray )
    {
        deassignUsers( msg, uArray, rArray, true );
    }

    /**
     * If performListCheck deassign the list of users to a list of roles. If the boolean is 'true' skip the list size tests which assume no outside entities are assigned..
     *
     * @param msg
     * @param uArray
     * @param rArray
     */
    private void deassignUsers( String msg, String[][] uArray, String[][] rArray, boolean performListCheck )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            int i = 0;
            for ( String[] usr : uArray )
            {
                i++;
                for ( String[] rle : rArray )
                {
                    UserRole uRole = new UserRole( UserTestData.getUserId( usr ), RoleTestData.getName( rle ) );
                    adminMgr.deassignUser( uRole );
                    LOG.debug( "deassignUsers user [" + uRole.getUserId() + "] role [" + uRole.getName()
                        + "] successful" );

                    if(performListCheck)
                    {
                        // Double check the number of users associated with role:
                        // This one retrieves the collection of all "roleOccupant" attributes associated with the role node:
                        List<String> users = reviewMgr.assignedUsers( RoleTestData.getRole( rle ), rArray.length );
                        // This one searches across all Users and pull back list of type "User":
                        List<User> users2 = reviewMgr.authorizedUsers( RoleTestData.getRole( rle ) );
                        assertNotNull( users );
                        assertNotNull( users2 );

                        // If this is the last user deassigned from role, both lists will be returned empty:
                        if ( i == uArray.length )
                        {
                            assertTrue( users.size() == 0 );
                            assertTrue( users2.size() == 0 );
                        }
                        else
                        {
                            assertTrue( CLS_NM + "deassignUsers list size check", ( rArray.length - i ) == users.size() );
                            assertTrue( CLS_NM + "deassignUsers list2 size check", ( rArray.length - i ) == users2.size() );
                        }
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "deassignUsers caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    /**
     * @param msg
     * @param uArray
     * @param rArray
     */
    void deassignUsersH( String msg, String[][] uArray, String[][] rArray )
    {
        LogUtil.logIt( msg );
        User user = null;
        Role role = null;
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int i = 0;
            for ( String[] usr : uArray )
            {
                user = UserTestData.getUser( usr );
                role = RoleTestData.getRole( rArray[i++] );
                UserRole uRole = new UserRole( user.getUserId(), role.getName() );
                adminMgr.deassignUser( uRole );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "deassignUsersH user [" + ( user != null ? user.getUserId() : null ) + "] role ["
                + ( role != null ? role.getName() : null ) + "] caught SecurityException rc=" + ex.getErrorId()
                + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testAssignUserRoleConstraints()
    {
        assignUserRoleConstraints( "ASSGN-USER-ROLE-CONSTRAINTS TR18 ABAC", RoleTestData.ROLE_CONSTRAINTS_TR18_ABAC );
    }


    private void assignUserRoleConstraints( String msg, String[][] urArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] urConstraint : urArray )
            {
                UserRole uRole = RoleTestData.getUserRoleConstraintAbac( urConstraint );
                RoleConstraint rConstraint = uRole.getConstraints().get( 0 );
                RoleConstraint out = adminMgr.addRoleConstraint( uRole, rConstraint );
                assertNotNull( out );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "assignUserRoleConstraints caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testDeassignUserRoleConstraints()
    {
        deassignUserRoleConstraints( "DEASSGN-USER-ROLE-CONSTRAINTS TR18 ABAC", RoleTestData
            .ROLE_CONSTRAINTS_TR18_ABAC );
    }


    private void deassignUserRoleConstraints( String msg, String[][] urArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] urConstraint : urArray )
            {
                UserRole uRole = RoleTestData.getUserRoleConstraintAbac( urConstraint );
                RoleConstraint rConstraint = uRole.getConstraints().get( 0 );
                adminMgr.removeRoleConstraint( uRole, rConstraint );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "deassignUserRoleConstraints caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     * Test the Rbac-Abac curly,moe,larry use cases.
     *
     */
    public void testEnableRoleConstraint()
    {
        enableRoleConstraint( "ENABLE ABAC RBAC", RoleTestData.ROLE_CONSTRAINTS_TR18_ROLES );
    }

    private void enableRoleConstraint( String msg, String[][] urArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] urConstraint : urArray )
            {
                UserRole uRole = RoleTestData.getUserRoleConstraintAbac( urConstraint );
                RoleConstraint rConstraint = uRole.getConstraints().get( 0 );
                Role role = new Role( uRole.getName() );
                adminMgr.enableRoleConstraint( role, rConstraint );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "enableRoleConstraint caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     * Test the Rbac-Abac curly,moe,larry use cases.
     *
     */
    public void testDisableRoleConstraint()
    {

        disableRoleConstraint( "DIABLE ABAC RBAC", RoleTestData.ROLE_CONSTRAINTS_TR18_ROLES );
    }

    private void disableRoleConstraint( String msg, String[][] urArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] urConstraint : urArray )
            {
                UserRole uRole = RoleTestData.getUserRoleConstraintAbac( urConstraint );
                RoleConstraint rConstraint = uRole.getConstraints().get( 0 );
                Role role = new Role( uRole.getName() );
                adminMgr.disableRoleConstraint( role, rConstraint );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "disableRoleConstraint caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testAddPermissionOp()
    {
        //     public PermObj addPermObj(PermObj pObj)
        addPermOps( "ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );
        addPermOps( "ADD-OPS TOB2 TOP2", PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, true, false );
        addPermOps( "ADD-OPS TOB3 TOP3", PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true, false );
        addPermOps( "ADD-OPS TOB4 TOP4", PermTestData.OBJS_TOB4, PermTestData.OPS_TOP4, true, false );
        addPermOps( "ADD-OPS TOB6 TOP5", PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5, true, false );
        addPermOps( "ADD-OPS ABAC WASHER", PermTestData.ABAC_WASHER_OBJS, PermTestData.ABAC_WASHER_OPS, true, false );
        addPermOps( "ADD-OPS ABAC TELLER", PermTestData.ABAC_TELLER_OBJS, PermTestData.ABAC_TELLER_OPS, true, false );
        addPermOps( "ADD-OPS ABAC ACCOUNT", PermTestData.ABAC_ACCOUNT_OBJS, PermTestData.ABAC_ACCOUNT_OPS, true, false );
        addPermOps( "ADD-OPS ABAC CURRENCY", PermTestData.ABAC_CURRENCY_OBJS, PermTestData.ABAC_CURRENCY_OPS, true, false );
        addPermOps( "ADD-OPS ABAC BRANCH", PermTestData.ABAC_BRANCH_OBJS, PermTestData.ABAC_BRANCH_OPS, true, false );
    }


    /**
     * @param objArray
     * @param opArray
     */
    public static void addPermOps( String msg, String[][] objArray, String[][] opArray, boolean isAdmin, boolean canFail )
    {
        LogUtil.logIt( msg );
        Permission pOp = new Permission();
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
            for ( String[] obj : objArray )
            {
                for ( String[] op : opArray )
                {
                    pOp = PermTestData.getOp( PermTestData.getName( obj ), op );
                    adminMgr.addPermission( pOp );
                    LOG.debug( "addPermOp objName [" + pOp.getObjName() + "] opName ["
                        + pOp.getOpName() + "]  objectId [" + pOp.getObjId() + "] successful" );
                }
            }
        }
        catch ( SecurityException ex )
        {
            if ( !canFail )
            {
                LOG.error( "addPermOp objName [" + pOp.getObjName() + "] opName [" + pOp.getOpName()
                    + "]  objectId [" + pOp.getObjId() + "] caught SecurityException rc=" + ex.getErrorId()
                    + ", msg=" + ex.getMessage(), ex );
                fail( ex.getMessage() );
            }
        }
    }


    public void testAddPermissionObj() throws SecurityException
    {
        //     public Permission addPermObj(Permission pOp)
        addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );
        addPermObjs( "ADD-OBS TOB2", PermTestData.OBJS_TOB2, true, false );
        addPermObjs( "ADD-OBS TOB3", PermTestData.OBJS_TOB3, true, false );
        addPermObjs( "ADD-OBS TOB4", PermTestData.OBJS_TOB4, true, false );
        addPermObjs( "ADD-OBS TOB6", PermTestData.OBJS_TOB6, true, false );
        addPermObjs( "ADD-OBS ABAC WASHER", PermTestData.ABAC_WASHER_OBJS, true, false );
        addPermObjs( "ADD-OBS ABAC TELLER", PermTestData.ABAC_TELLER_OBJS, true, false );
        addPermObjs( "ADD-OBS ABAC ACCOUNT", PermTestData.ABAC_ACCOUNT_OBJS, true, false );
        addPermObjs( "ADD-OBS ABAC CURRENCY", PermTestData.ABAC_CURRENCY_OBJS, true, false );
        addPermObjs( "ADD-OBS ABAC BRANCH", PermTestData.ABAC_BRANCH_OBJS, true, false );
    }



    /**
     * @param objArray
     */
    public static void addPermObjs( String msg, String[][] objArray, boolean isAdmin, boolean canFail )
    {
        LogUtil.logIt( msg );
        PermObj pObj = new PermObj();
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
            for ( String[] obj : objArray )
            {
                pObj = PermTestData.getObj( obj );
                // Todo - add props
                adminMgr.addPermObj( pObj );
                LOG.debug( "addPermObjs objName [" + pObj.getObjName() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            if ( !canFail )
            {
                LOG.error( "addPermObjs objName [" + pObj.getObjName()
                    + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
                fail( ex.getMessage() );
            }
        }
    }

    public void testAddPermissionAttributeSet() throws SecurityException
    {                
        addPermissionAttributeSet( "ADD-PA-SET TPASET1", PermTestData.TPA_SET_1_NAME, PermTestData.loadPermissionAttributes(PermTestData.PA_TPSASET1));
        addPermissionAttributeSet( "ADD-PA-SET TPASET2", PermTestData.TPA_SET_2_NAME, PermTestData.loadPermissionAttributes(PermTestData.PA_TPSASET2));
    }
    
    public static void addPermissionAttributeSet( String msg, String name, Set<PermissionAttribute> permAttr )
    {
        LogUtil.logIt(msg);
        PermissionAttributeSet paSet = new PermissionAttributeSet(name);  
        paSet.setType(PermTestData.TPA_SET_TYPE);
        
        try
        {
            paSet.setAttributes(permAttr);
            //AdminMgr adminMgr = AdminMgrFactory.createInstance( TestUtils.getContext() );
            AdminMgr adminMgr = getManagedAdminMgr();
            adminMgr.addPermissionAttributeSet(paSet);
            LOG.debug( "addPermissionAttributeSet name [" + paSet.getName() + "] successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "addPermissionAttributeSet name [" + paSet.getName()
                    + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }
    
    public void testAddPermissionAttributeToSet() throws SecurityException
    {                
        Set<PermissionAttribute> pas = PermTestData.loadPermissionAttributes(PermTestData.PA_TPSASET2_ADDITIONAL);
        
        for(PermissionAttribute pa : pas){
            addPermissionAttributeToSet( "ADD-PA-TO-SET TPASET2", PermTestData.TPA_SET_2_NAME, pa);
        }
    }
    
    public static void addPermissionAttributeToSet( String msg, String name, PermissionAttribute permAttr )
    {
        LogUtil.logIt(msg);
        PermissionAttributeSet paSet = new PermissionAttributeSet(name);  
        try
        {
            //AdminMgr adminMgr = AdminMgrFactory.createInstance( TestUtils.getContext() );
            AdminMgr adminMgr = getManagedAdminMgr();
            adminMgr.addPermissionAttributeToSet(permAttr, name);
            LOG.debug( "addPermissionAttributeToSet name [" + paSet.getName() + "] successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "addPermissionAttributeToSet name [" + paSet.getName()
                    + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }
    
    public void testDeletePermissionAttributeSets() throws SecurityException
    {
        delPermAttrSet( "DEL-PA-SET TPASET1" , PermTestData.TPA_SET_1_NAME );
        delPermAttrSet( "DEL-PA-SET TPASET2" , PermTestData.TPA_SET_2_NAME );
    }
    
    public static void delPermAttrSet( String msg, String name )
    {
        LogUtil.logIt(msg);
        PermissionAttributeSet paSet = new PermissionAttributeSet(name);  
        try
        {
            //AdminMgr adminMgr = AdminMgrFactory.createInstance( TestUtils.getContext() );
            AdminMgr adminMgr = getManagedAdminMgr();
            adminMgr.deletePermissionAttributeSet(paSet);
            LOG.debug( "delPermAttrSet name [" + paSet.getName() + "] successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "delPermAttrSet name [" + paSet.getName()
                    + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }
    
    public void testAddPASetToPermission()
    {
        addValidPASetToPermission( "ADD-PASET-TO-POP-VALID TOB_1 TOP_1", PermTestData.TPA_SET_1_NAME, "TOB1_1", PermTestData.OPS_TOP1_UPD[0] );       
        addInvalidPASetToPermission( "ADD-PASET-TO-POP-INVALID TOB_1 TOP_1", PermTestData.TPA_SET_NOT_EXIST_NAME, "TOB1_1", PermTestData.OPS_TOP1_UPD[0] );       
    }

    public static void addInvalidPASetToPermission( String msg, String paSetName, String obj, String[] op )
    {
        try{
            addPASetToPermission(msg, paSetName, obj, op);
            
            String message = "addInvalidPASetToPermission name [" + paSetName + "] was successfull, when should ahve failed.";
            LOG.error( message );
            fail( message );
        }
        catch ( SecurityException ex )
        {
            LOG.info("Caught exception adding invalid tpa set name " + paSetName);
        }
    }
    
    public static void addValidPASetToPermission( String msg, String paSetName, String obj, String[] op )
    {
        try{
            addPASetToPermission(msg, paSetName, obj, op);
        }
        catch ( SecurityException ex )
        {
            LOG.error( "addValidPASetToPermission name [" + paSetName
                    + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }
    
    public static void addPASetToPermission( String msg, String paSetName, String obj, String[] op ) throws SecurityException
    {
        LogUtil.logIt(msg);

        //AdminMgr adminMgr = AdminMgrFactory.createInstance( TestUtils.getContext() );
        AdminMgr adminMgr = getManagedAdminMgr();
        Permission pop = PermTestData.getOp( obj, op );
        pop.setPaSetName(paSetName);

        adminMgr.updatePermission(pop);
        LOG.debug( "addPASetToPermission name [" + paSetName + "] successful" );        
    }
    
    public void testAddUserRoleConstraint() throws SecurityException
    {
        assignValidUserRoleConstraint( "ASGN-URC-VALID TU1 TR1", UserTestData.USERS_TU1[0], RoleTestData.ROLES_TR1[1], URATestData.getRC(URATestData.URC_T1) );
        
        assignInvalidUserRoleConstraint( "ASGN-URC-INVALID TU1 TR1", UserTestData.USERS_TU1[0], RoleTestData.ROLES_TR1[1], URATestData.getRC(URATestData.URC_T1_INVALID) );
    }
    
    public static void assignValidUserRoleConstraint( String msg, String[] usr, String[] rle, RoleConstraint rc )
    {
        try{
            assignUserRoleConstraint(msg, usr, rle, rc);
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "assignUserRoleConstraint caught SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    
    }
    
    public static void assignInvalidUserRoleConstraint( String msg, String[] usr, String[] rle, RoleConstraint rc )
    {
        try{
            assignUserRoleConstraint(msg, usr, rle, rc);
            
            String message = "assignInvalidUserRoleConstraint name [" + rc.getKey() + "] was successfull, when should ahve failed.";
            LOG.error( message );
            fail( message );
        }
        catch ( SecurityException ex )
        {
            LOG.info("Caught exception adding invalid tpa set name " + rc.getKey());
        }
    
    }
    
    public static RoleConstraint assignUserRoleConstraint( String msg, String[] usr, String[] rle, RoleConstraint rc ) throws SecurityException
    {
        LogUtil.logIt( msg );

        AdminMgr adminMgr = getManagedAdminMgr();
        ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();

        User user = UserTestData.getUser( usr );
        Role role = RoleTestData.getRole( rle );

        RoleConstraint createdRoleConstraint = adminMgr.addRoleConstraint(new UserRole(user.getUserId(), role.getName()), rc);
            
        LOG.debug("assignUserRoleConstraint user [" + user.getUserId() + "] role [" + role.getName() + "] " +
                " rcvalue [" + rc.getValue() + "]");

        // Use the returned 'createdRoleConstraint' from addRoleConstraint method call, not its argument -- 'rc'.
        // Otherwise this will fail over REST, because the 'rc' won't have its 'id' field set, which is needed for this search to yield correct results.
        // get user with consratint filter:
        List<User> usersWithRc = reviewMgr.assignedUsers( role, createdRoleConstraint );
        assertTrue( usersWithRc.size() == 1 );
        assertEquals( user.getUserId(), usersWithRc.get( 0 ).getUserId() );
        
        return createdRoleConstraint;
    }
    
    public void testRemoveUserRoleConstraint() throws SecurityException
    {
        this.assertRoleConstraintSize( UserTestData.USERS_TU1[0][0], RoleTestData.ROLES_TR1[1][0], 1 );
        
        RoleConstraint rc1 = assignUserRoleConstraint( "ASGN-URC-VALID TU1 TR1", UserTestData.USERS_TU1[0], RoleTestData.ROLES_TR1[1], URATestData.getRC(URATestData.URC_T2) );
        RoleConstraint rc2 = assignUserRoleConstraint( "ASGN-URC-VALID TU1 TR1", UserTestData.USERS_TU1[0], RoleTestData.ROLES_TR1[1], URATestData.getRC(URATestData.URC_T3) ); 
        
        this.assertRoleConstraintSize( UserTestData.USERS_TU1[0][0], RoleTestData.ROLES_TR1[1][0], 3 );
        
        AdminMgr adminMgr = getManagedAdminMgr();
        adminMgr.removeRoleConstraint( new UserRole( UserTestData.USERS_TU1[0][0], RoleTestData.ROLES_TR1[1][0] ), rc1 );
        this.assertRoleConstraintSize( UserTestData.USERS_TU1[0][0], RoleTestData.ROLES_TR1[1][0], 2 );
        
        adminMgr.removeRoleConstraint( new UserRole( UserTestData.USERS_TU1[0][0], RoleTestData.ROLES_TR1[1][0] ), rc2.getId() );
        this.assertRoleConstraintSize( UserTestData.USERS_TU1[0][0], RoleTestData.ROLES_TR1[1][0], 1 );
    }
    
    private void assertRoleConstraintSize(String userId, String roleName, int size) throws SecurityException{
        boolean roleFound = false;
        
        ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
        List<UserRole> userRoles = reviewMgr.readUser( new User( userId ) ).getRoles();
        for(UserRole ur : userRoles){
            if( ur.getName().equals( roleName )){
                assertEquals( size, ur.getRoleConstraints().size() );
                roleFound = true;
            }
        }
        if( !roleFound ){
            fail("Role with name " + roleName + " not found");
        }
    }
    
    /**
     *
     * @throws SecurityException
     */
    public void testDeletePermissionOp() throws SecurityException
    {
        //     public void deletePermObj(PermObj pObj)
        delPermOps( "DEL-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );
        delPermOps( "DEL-OPS TOB2 TOP3", PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, true, false );
        delPermOps( "DEL-OPS TOB3 TOP4", PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true, false );
        delPermOps( "DEL-OPS TOB4 TOP4", PermTestData.OBJS_TOB4, PermTestData.OPS_TOP4, true, false );
        delPermOps( "DEL-OPS TOB6 TOP5", PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5, true, false );
        delPermOps( "DEL-OPS ABAC WASHER", PermTestData.ABAC_WASHER_OBJS, PermTestData.ABAC_WASHER_OPS, true, false );
        delPermOps( "DEL-OPS ABAC TELLER", PermTestData.ABAC_TELLER_OBJS, PermTestData.ABAC_TELLER_OPS, true, false );
        delPermOps( "DEL-OPS ABAC ACCOUNT", PermTestData.ABAC_ACCOUNT_OBJS, PermTestData.ABAC_ACCOUNT_OPS, true, false );
        delPermOps( "DEL-OPS ABAC CURRENCY", PermTestData.ABAC_CURRENCY_OBJS, PermTestData.ABAC_CURRENCY_OPS, true,
            false );
        delPermOps( "DEL-OPS ABAC BRANCH", PermTestData.ABAC_BRANCH_OBJS, PermTestData.ABAC_BRANCH_OPS, true, false );
    }


    /**
     * @param objArray
     * @param opArray
     */
    public static void delPermOps( String msg, String[][] objArray, String[][] opArray, boolean isAdmin, boolean canFail )
    {
        LogUtil.logIt( msg );
        Permission pOp = new Permission();
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
            for ( String[] obj : objArray )
            {
                for ( String[] op : opArray )
                {
                    pOp = PermTestData.getOp( PermTestData.getName( obj ), op );
                    adminMgr.deletePermission( pOp );
                    LOG.debug( "delPermOps objName [" + pOp.getObjName() + "] opName ["
                        + pOp.getOpName() + "]  objectId [" + pOp.getObjId() + "] successful" );
                }
            }
        }
        catch ( SecurityException ex )
        {
            if ( !canFail )
            {
                LOG.error( "delPermOps objName [" + pOp.getObjName() + "] opName [" + pOp.getOpName()
                    + "]  objectId [" + pOp.getObjId() + "] caught SecurityException rc=" + ex.getErrorId()
                    + ", msg=" + ex.getMessage(), ex );
                fail( ex.getMessage() );
            }
        }
    }


    public void testDeletePermissionObj() throws SecurityException
    {
        //     public void deletePermObj(Permission pOp)
        delPermObjs( "DEL-OBJS TOB1", PermTestData.OBJS_TOB1, true );
        delPermObjs( "DEL-OBJS TOB2", PermTestData.OBJS_TOB2, true );
        delPermObjs( "DEL-OBJS TOB3", PermTestData.OBJS_TOB3, true );
        delPermObjs( "DEL-OBJS TOB4", PermTestData.OBJS_TOB4, true );
        delPermObjs( "DEL-OBJS TOB6", PermTestData.OBJS_TOB6, true );
        delPermObjs( "DEL-OBS ABAC WASHER", PermTestData.ABAC_WASHER_OBJS, true );
        delPermObjs( "DEL-OBS ABAC TELLER", PermTestData.ABAC_TELLER_OBJS, true );
        delPermObjs( "DEL-OBS ABAC ACCOUNT", PermTestData.ABAC_ACCOUNT_OBJS, true );
        delPermObjs( "DEL-OBS ABAC CURRENCY", PermTestData.ABAC_CURRENCY_OBJS, true );
        delPermObjs( "DEL-OBS ABAC BRANCH", PermTestData.ABAC_BRANCH_OBJS, true );
    }


    /**
     * @param objArray
     */
    public static void delPermObjs( String msg, String[][] objArray, boolean isAdmin )
    {
        LogUtil.logIt( msg );
        PermObj pObj = new PermObj();
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
            for ( String[] obj : objArray )
            {
                pObj = PermTestData.getObj( obj );
                adminMgr.deletePermObj( pObj );
                LOG.debug( "delPermObjs objName [" + pObj.getObjName() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "delPermObjs objName [" + pObj.getObjName() + "] caught SecurityException rc="
                + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testUpdatePermissionObj() throws SecurityException
    {
        updatePermObjs( "UPD-OBS TOB4_UPD", PermTestData.OBJS_TOB4_UPD, true );
    }


    /**
     * @param objArray
     */
    public static void updatePermObjs( String msg, String[][] objArray, boolean isAdmin )
    {
        LogUtil.logIt( msg );
        PermObj pObj = new PermObj();
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
            for ( String[] obj : objArray )
            {
                pObj = PermTestData.getObj( obj );
                // Todo - add props
                adminMgr.updatePermObj( pObj );
                LOG.debug( "updatePermObjs objName [" + pObj.getObjName() + "] successful" );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "updatePermObjs objName [" + pObj.getObjName()
                + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testUpdatePermissionOp()
    {
        updatePermOps( "UPD-OPS TOB1 TOP1_UPD", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1_UPD, true );
    }


    /**
     * @param objArray
     * @param opArray
     */
    public static void updatePermOps( String msg, String[][] objArray, String[][] opArray, boolean isAdmin )
    {
        LogUtil.logIt( msg );
        Permission pOp = new Permission();
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
            for ( String[] obj : objArray )
            {
                for ( String[] op : opArray )
                {
                    pOp = PermTestData.getOp( PermTestData.getName( obj ), op );
                    adminMgr.updatePermission( pOp );
                    LOG.debug( "updatePermOps objName [" + pOp.getObjName() + "] opName ["
                        + pOp.getOpName() + "]  objectId [" + pOp.getObjId() + "] successful" );
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "updatePermOps objName [" + pOp.getObjName() + "] opName [" + pOp.getOpName()
                + "]  objectId [" + pOp.getObjId() + "] caught SecurityException rc=" + ex.getErrorId() + ", msg="
                + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     * AMT24
     *
     * @throws SecurityException
     */
    public void testGrantPermissionRole()
    {
        //     public void grantPermission(Permission pOp, Role role)
        addRoleGrants( "GRNT-PRMS TR1 TOB1 TOP1", RoleTestData.ROLES_TR1, PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1, true, false );
        addRoleGrants( "GRNT-PRMS TR2 TOB2 TOP2", RoleTestData.ROLES_TR2, PermTestData.OBJS_TOB2,
            PermTestData.OPS_TOP2, true, false );
        addRoleGrants( "GRNT-PRMS TR3 TOB3 TOP3", RoleTestData.ROLES_TR3, PermTestData.OBJS_TOB3,
            PermTestData.OPS_TOP3, true, false );
        addRoleGrants( "GRNT-PRMS ABAC WASHER", RoleTestData.ROLES_ABAC_WASHERS, PermTestData.ABAC_WASHER_OBJS, PermTestData.ABAC_WASHER_OPS, true, false );
        addRoleGrants( "GRNT-PRMS ABAC WASHER CURRENCY", RoleTestData.ROLES_ABAC_WASHERS, PermTestData.ABAC_CURRENCY_OBJS, PermTestData.ABAC_CURRENCY_OPS, true, false );
        addRoleGrants( "GRNT-PRMS ABAC TELLER", RoleTestData.ROLES_ABAC_TELLERS, PermTestData.ABAC_TELLER_OBJS, PermTestData.ABAC_TELLER_OPS, true, false );
        addRoleGrants( "GRNT-PRMS ABAC TELLER ACCOUNT", RoleTestData.ROLES_ABAC_TELLERS, PermTestData.ABAC_ACCOUNT_OBJS, PermTestData.ABAC_ACCOUNT_OPS, true, false );
        addRoleGrants( "GRNT-PRMS ABAC BANK USERS", RoleTestData.ROLES_ABAC_USERS, PermTestData.ABAC_BRANCH_OBJS, PermTestData.ABAC_BRANCH_OPS, true, false );
        addRoleGrantsH( "GRNT-PRMS_H ROLES_TR5_HIER TOB4 TOP4", RoleTestData.ROLES_TR5_HIER, PermTestData.OBJS_TOB4,
            PermTestData.OPS_TOP4 );
        addRoleGrantsHB( "GRNT-PRMS_HB USERS TU20 ROLES_TR5B TOB6 TOP5", UserTestData.USERS_TU20U_TR5B,
            RoleTestData.ROLES_TR5B, PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5 );
    }


    /**
     * @param rArray
     * @param objArray
     * @param opArray
     */
    public static void addRoleGrants( String msg, String[][] rArray, String[][] objArray, String[][] opArray,
        boolean isAdmin, boolean canFail )
    {
        LogUtil.logIt( msg );
        Permission pOp = new Permission();
        Role role = new Role();
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
            for ( String[] rle : rArray )
            {
                for ( String[] obj : objArray )
                {
                    for ( String[] op : opArray )
                    {
                        role = new Role( RoleTestData.getName( rle ) );
                        pOp = PermTestData.getOp( PermTestData.getName( obj ), op );
                        adminMgr.grantPermission( pOp, role );
                        LOG.debug( "addRoleGrants role name [" + role.getName() + "] objName ["
                            + pOp.getObjName() + "] objectId [" + pOp.getObjId() + "] operation name ["
                            + pOp.getOpName() + "] successful" );
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            if ( !canFail )
            {
                LOG.error(
                    "addRoleGrants role name [" + role.getName() + "] objName [" + pOp.getObjName()
                        + "] objectId [" + pOp.getObjId() + "] operation name [" + pOp.getOpName()
                        + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
                fail( ex.getMessage() );
            }
        }
    }


    /**
     *
     * @param msg
     * @param rArray
     * @param objArray
     * @param opArray
     */
    public static void addRoleGrantsH( String msg, String[][] rArray, String[][] objArray, String[][] opArray )
    {
        LogUtil.logIt( msg );
        Permission pOp = new Permission();
        Role role = new Role();
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int i = 0;
            for ( String[] obj : objArray )
            {
                role = new Role( RoleTestData.getName( rArray[i++] ) );
                for ( String[] op : opArray )
                {
                    pOp = PermTestData.getOp( PermTestData.getName( obj ), op );
                    adminMgr.grantPermission( pOp, role );
                    LOG.debug( "addRoleGrantsH role name [" + role.getName() + "] objName ["
                        + pOp.getObjName() + "] objectId [" + pOp.getObjId() + "] operation name ["
                        + pOp.getOpName() + "] successful" );
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "addRoleGrantsH role name [" + role.getName() + "] objName [" + pOp.getObjName()
                + "] objectId [" + pOp.getObjId() + "] operation name [" + pOp.getOpName()
                + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     * @param msg
     * @param rArray
     * @param objArray
     * @param opArray
     */
    public static void addRoleGrantsHB( String msg, String[][] uArray, String[][] rArray, String[][] objArray,
        String[][] opArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] obj : objArray )
            {
                int i = 0;
                for ( String[] rle : rArray )
                {
                    // Get Role[i] from test data:
                    Role role = RoleTestData.getRole( rle );
                    // Get Permission[i] from test data:
                    String[] op = opArray[i];
                    // Load the Permission entity with data:
                    Permission pOp = PermTestData.getOp( PermTestData.getName( obj ), op );

                    // Grant Permission[i] to Role[i]:
                    adminMgr.grantPermission( pOp, role );

                    // Get User[i] from test data:
                    String[] usr = uArray[i];
                    // Load the User entity with data:
                    User user = UserTestData.getUser( usr );
                    // Grant Permission[i] to User[i]:
                    adminMgr.grantPermission( pOp, user );
                    i++;

                    LOG.debug( "addRoleGrantsHB userId [" + user.getUserId() + "] role name ["
                        + role.getName() + "] objName [" + pOp.getObjName() + "] objectId [" + pOp.getObjId()
                        + "] operation name [" + pOp.getOpName() + "] successful" );
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "addRoleGrantsHB caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    /**
     * AMT25
     *
     * @throws org.apache.directory.fortress.core.SecurityException
     */
    public void testRevokePermissionRole()
    {
        //     public void revokePermission(Permission pOp, Role role)
        delRoleGrants( "REVK-PRMS TR1 TOB1 TOP1", RoleTestData.ROLES_TR1, PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1, true );
        delRoleGrants( "REVK-PRMS TR2 TOB2 TOP2", RoleTestData.ROLES_TR2, PermTestData.OBJS_TOB2,
            PermTestData.OPS_TOP2, true );
        delRoleGrants( "REVK-PRMS TR3 TOB3 TOP3", RoleTestData.ROLES_TR3, PermTestData.OBJS_TOB3,
            PermTestData.OPS_TOP3, true );
        delRoleGrants( "REVK-PRMS ABAC WASHER", RoleTestData.ROLES_ABAC_WASHERS, PermTestData.ABAC_WASHER_OBJS, PermTestData.ABAC_WASHER_OPS, true );
        delRoleGrants( "REVK-PRMS ABAC WASHER CURRENCY", RoleTestData.ROLES_ABAC_WASHERS, PermTestData
            .ABAC_CURRENCY_OBJS, PermTestData.ABAC_CURRENCY_OPS, true );
        delRoleGrants( "REVK-PRMS ABAC TELLER", RoleTestData.ROLES_ABAC_TELLERS, PermTestData.ABAC_TELLER_OBJS,
            PermTestData.ABAC_TELLER_OPS, true );
        delRoleGrants( "REVK-PRMS ABAC TELLER ACCOUNT", RoleTestData.ROLES_ABAC_TELLERS, PermTestData
            .ABAC_ACCOUNT_OBJS, PermTestData.ABAC_ACCOUNT_OPS, true );
        delRoleGrants( "GRNT-PRMS ABAC BANK USERS", RoleTestData.ROLES_ABAC_USERS, PermTestData.ABAC_BRANCH_OBJS,
            PermTestData.ABAC_BRANCH_OPS, true );
        delRoleGrantsH( "REVK-PRMS_H ROLES_TR5_HIER TOB4 TOP4", RoleTestData.ROLES_TR5_HIER, PermTestData.OBJS_TOB4,
            PermTestData.OPS_TOP4 );
        delRoleGrantsHB( "REVK-PRMS_H USERS TU20 ROLES_TR5B TOB6 TOP5", UserTestData.USERS_TU20U_TR5B,
            RoleTestData.ROLES_TR5B, PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5 );
    }


    /**
     * @param rArray
     * @param objArray
     * @param opArray
     */
    public static void delRoleGrants( String msg, String[][] rArray, String[][] objArray, String[][] opArray,
        boolean isAdmin )
    {
        LogUtil.logIt( msg );
        Permission pOp = new Permission();
        Role role = new Role();
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
            for ( String[] rle : rArray )
            {
                for ( String[] obj : objArray )
                {
                    for ( String[] op : opArray )
                    {
                        role = new Role( RoleTestData.getName( rle ) );
                        pOp = PermTestData.getOp( PermTestData.getName( obj ), op );
                        adminMgr.revokePermission( pOp, role );
                        LOG.debug( "delRoleGrants role name [" + role.getName() + "] objName ["
                            + pOp.getObjName() + "] objectId [" + pOp.getObjId() + "] operation name ["
                            + pOp.getOpName() + "] successful" );
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "delRoleGrants role name [" + role.getName() + "] objName [" + pOp.getObjName()
                + "] objectId [" + pOp.getObjId() + "] operation name [" + pOp.getOpName()
                + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     * 
     * @param msg
     * @param rArray
     * @param objArray
     * @param opArray
     */
    private void delRoleGrantsH( String msg, String[][] rArray, String[][] objArray, String[][] opArray )
    {
        LogUtil.logIt( msg );
        Permission pOp = new Permission();
        Role role = new Role();
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int i = 0;
            for ( String[] obj : objArray )
            {
                role = new Role( RoleTestData.getName( rArray[i++] ) );
                for ( String[] op : opArray )
                {
                    pOp = PermTestData.getOp( PermTestData.getName( obj ), op );
                    adminMgr.revokePermission( pOp, role );
                    LOG.debug( "delRoleGrantsH role name [" + role.getName() + "] objName ["
                        + pOp.getObjName() + "] objectId [" + pOp.getObjId() + "] operation name ["
                        + pOp.getOpName() + "] successful" );
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "delRoleGrantsH role name [" + role.getName() + "] objName [" + pOp.getObjName()
                + "] objectId [" + pOp.getObjId() + "] operation name [" + pOp.getOpName()
                + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     * @param msg
     * @param rArray
     * @param objArray
     * @param opArray
     */
    private void delRoleGrantsHB( String msg, String[][] uArray, String[][] rArray, String[][] objArray,
        String[][] opArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] obj : objArray )
            {
                int i = 0;
                for ( String[] rle : rArray )
                {
                    // Get Role[i] from test data:
                    Role role = RoleTestData.getRole( rle );
                    // Get Permission[i] from test data:
                    String[] op = opArray[i];
                    // Load the permission entity with data:
                    Permission pOp = PermTestData.getOp( PermTestData.getName( obj ), op );
                    // Grant Permission[i] to Role[i]:
                    adminMgr.revokePermission( pOp, role );

                    // Get User[i] from test data:
                    String[] usr = uArray[i];
                    // Load the User entity with data:
                    User user = UserTestData.getUser( usr );
                    // Grant Permission[i] to User[i]:
                    adminMgr.revokePermission( pOp, user );
                    i++;

                    LOG.debug( "delRoleGrantsHB userId [" + user.getUserId() + "] role name ["
                        + role.getName() + "] objName [" + pOp.getObjName() + "] objectId [" + pOp.getObjId()
                        + "] operation name [" + pOp.getOpName() + "] successful" );
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "delRoleGrantsHB caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(),
                ex );
            fail( ex.getMessage() );
        }
    }


    public void testGrantPermissionUser()
    {
        //     public void grantPermission(Permission pOp, User user)
        addUserGrants( "GRNT-PRMS TU1 TOB1 TOP1", UserTestData.USERS_TU1, PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1 );
    }


    /**
     * @param uArray
     * @param objArray
     * @param opArray
     */
    public static void addUserGrants( String msg, String[][] uArray, String[][] objArray, String[][] opArray )
    {
        LogUtil.logIt( msg );
        Permission pOp = new Permission();
        User user = new User();
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] usr : uArray )
            {
                for ( String[] obj : objArray )
                {
                    for ( String[] op : opArray )
                    {
                        user = new User( UserTestData.getUserId( usr ) );
                        pOp = PermTestData.getOp( PermTestData.getName( obj ), op );
                        adminMgr.grantPermission( pOp, user );
                        LOG.debug( "addUserGrants userId [" + user.getUserId() + "] objName ["
                            + pOp.getObjName() + "] objectId [" + pOp.getObjId() + "] operation name ["
                            + pOp.getOpName() + "] successful" );
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "addUserGrants userId [" + user.getUserId() + "] objName [" + pOp.getObjName()
                + "] objectId [" + pOp.getObjId() + "] operation name [" + pOp.getOpName()
                + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testRevokePermissionUser()
    {
        //     public void revokePermission(Permission pOp, User user)
        delUserGrants( "RVK-PRMS TU1 TOB1 TOP1", UserTestData.USERS_TU1, PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1 );
    }


    /**
     * @param uArray
     * @param objArray
     * @param opArray
     */
    private void delUserGrants( String msg, String[][] uArray, String[][] objArray, String[][] opArray )
    {
        LogUtil.logIt( msg );
        Permission pOp = new Permission();
        User user = new User();
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for ( String[] usr : uArray )
            {
                for ( String[] obj : objArray )
                {
                    for ( String[] op : opArray )
                    {
                        user = new User( UserTestData.getUserId( usr ) );
                        pOp = PermTestData.getOp( PermTestData.getName( obj ), op );
                        adminMgr.revokePermission( pOp, user );
                        LOG.debug( "delUserGrants userId [" + user.getUserId() + "] objName ["
                            + pOp.getObjName() + "] objectId [" + pOp.getObjId() + "] operation name ["
                            + pOp.getOpName() + "] successful" );
                    }
                }
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error( "delUserGrants userId [" + user.getUserId() + "] objName [" + pOp.getObjName()
                + "] objectId [" + pOp.getObjId() + "] operation name [" + pOp.getOpName()
                + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     * @return
     * @throws org.apache.directory.fortress.core.SecurityException
     */
    public static AdminMgr getManagedAdminMgr() throws SecurityException
    {
        if ( FortressJUnitTest.isAdminEnabled() && adminSess == null )
        {
            adminSess = DelegatedMgrImplTest.createAdminSession();
        }
        return AdminMgrFactory.createInstance( TestUtils.getContext(), adminSess );
    }
}
