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

import org.apache.directory.fortress.core.ldap.LdapCounters;
import org.apache.directory.fortress.core.ldap.LdapDataProvider;
import org.apache.directory.fortress.core.util.Config;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This JUnit test class drives all of the Fortress Administration APIs contained within {@link AdminMgrImplTest},
 * {@link DelegatedMgrImplTest}, {@link org.apache.directory.fortress.core.impl.PwPolicyMgrImpl} and {@link org.apache.directory.fortress.core.impl.AuditMgrImpl}.
 * There are more than 125 distinct test cases that kicked off from within this JUnit test wrapper.
 * Fortress JUnit test phases in this file include:
 * 1. Tear-down data (optional) - during this phase previously loaded test data is removed from directory.
 * 2. Buildup data - add it back.
 * 3. Interrogate data - entity and security policy review APIs.
 * 4. Check data - runtime security rule evaluations.
 * 4. Audit data - validate audit log coverage.
 * <p>
 * Note 1: Data is retained in LDAP directory after these tests run (assuming the client was able to connect
 * to the LDAP server).
 * Note 2: To delete old test data, run the "FortressJUnitDeleteTest" test cases after success on main (if repeatable
 * runs of this class are not desired).
 * Note 3: On test first run, assertions will fail on the teardown phase (#1) as (presumably) prior test data does
 * not exist.  The tests
 * are designed to recover on repeated runs if/when ldap data falls out of synch - BUT - if/when errors do occur
 * during repeat of buildup phase,
 * delete the old test data manually in directory before trying again.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class FortressJUnitTest extends TestCase
{
    private static boolean adminEnabled;

    public static boolean isAdminEnabled()
    {
        return adminEnabled;
    }

    private static void setAdminEnabled( boolean adminVal )
    {
        adminEnabled = adminVal;
    }

    private static boolean isFirstRun = getFirstRun();
    //private static boolean isFirstRun = true;

    private static boolean getFirstRun()
    {
        isFirstRun = !ReviewMgrImplTest.teardownRequired( "TD-RLS TR1", RoleTestData.ROLES_TR1 );
        return isFirstRun;
    }

    public static boolean isFirstRun()
    {
        return isFirstRun;
    }

    @Deprecated
    public static void setFirstRun( boolean firstRun )
    {
        isFirstRun = firstRun;
    }

    /**
     * @return
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        //setAdminEnabled(false);
        setAdminEnabled( true );
        /***********************************************************/
        /* 0. Load the base Admin Policy if need be:
        /***********************************************************/
        if ( DelegatedMgrImplTest.loadAdminPolicy() )
        {
            // ARBAC Buildup APIs:
            suite.addTest( new DelegatedMgrImplTest( "testAddAdminUser" ) );
        }

        /***********************************************************/
        /* 1. Tear Down Phase                                      */
        /***********************************************************/
        if ( !isFirstRun() )
        {
            // PwPolicyMgr PW Policy Teardown:
            if ( Config.getInstance().isOpenldap() || Config.getInstance().isApacheds() )
            {
                suite.addTest( new PswdPolicyMgrImplTest( "testDeletePasswordPolicy" ) );
            }

            suite.addTest( new AdminMgrImplTest( "testDisableRoleConstraint" ) );

            // GroupMgr
            suite.addTest( new GroupMgrImplTest( "testDeassignGroupRoleMember" ) );
            suite.addTest( new GroupMgrImplTest( "testDeassignGroupUserMember" ) );
            suite.addTest( new GroupMgrImplTest( "testDeleteGroup" ) );

            // AdminMgrImplTest RBAC Teardown APIs:
            suite.addTest( new AdminMgrImplTest( "testRevokePermissionUser" ) );
            suite.addTest( new AdminMgrImplTest( "testRevokePermissionRole" ) );
            suite.addTest( new AdminMgrImplTest( "testDeletePermissionOp" ) );
            suite.addTest( new AdminMgrImplTest( "testDeletePermissionObj" ) );
            suite.addTest( new AdminMgrImplTest( "testDeletePermissionAttributeSets" ) );
            suite.addTest( new AdminMgrImplTest( "testDeassignUserRoleConstraints" ) );
            suite.addTest( new AdminMgrImplTest( "testDeassignUser" ) );


            // Requires password policies:
            if ( Config.getInstance().isOpenldap() || Config.getInstance().isApacheds() )
            {
                suite.addTest( new AdminMgrImplTest( "testDeleteUser" ) );
            }

            suite.addTest( new AdminMgrImplTest( "testForceDeleteUser" ) );
            suite.addTest( new AdminMgrImplTest( "testDeleteSsdRoleMember" ) );
            suite.addTest( new AdminMgrImplTest( "testDeleteDsdRoleMember" ) );
            suite.addTest( new AdminMgrImplTest( "testDeleteSsdSet" ) );
            suite.addTest( new AdminMgrImplTest( "testDeleteDsdSet" ) );
            suite.addTest( new AdminMgrImplTest( "testDeleteRoleInheritance" ) );
            suite.addTest( new AdminMgrImplTest( "testDelRoleDescendant" ) );
            suite.addTest( new AdminMgrImplTest( "testDelRoleAscendant" ) );
            suite.addTest( new AdminMgrImplTest( "testDeleteRole" ) );
            if ( Config.getInstance().isOpenldap() || Config.getInstance().isApacheds() )
            {
                suite.addTest( new PswdPolicyMgrImplTest( "testDelete" ) );
            }

            // DelAdminMgr ARBAC Teardown APIs
            suite.addTest( new DelegatedMgrImplTest( "testRevokePermissionRole" ) );
            suite.addTest( new DelegatedMgrImplTest( "testDeassignAdminUser" ) );
            suite.addTest( new DelegatedMgrImplTest( "testDeleteUser" ) );
            suite.addTest( new DelegatedMgrImplTest( "testDeletePermission" ) );
            suite.addTest( new DelegatedMgrImplTest( "testDelAdminRoleDescendant" ) );
            suite.addTest( new DelegatedMgrImplTest( "testDelAdminRoleAscendant" ) );
            suite.addTest( new DelegatedMgrImplTest( "testDeleteAdminRoleInheritance" ) );
            suite.addTest( new DelegatedMgrImplTest( "testDeleteRole" ) );
            suite.addTest( new DelegatedMgrImplTest( "testDeleteOrgInheritance" ) );
            suite.addTest( new DelegatedMgrImplTest( "testDelOrgUnitDescendant" ) );
            suite.addTest( new DelegatedMgrImplTest( "testDelOrgUnitAscendant" ) );
            suite.addTest( new DelegatedMgrImplTest( "testDeleteOrgUnit" ) );
            //suite.addTest(new DelegatedMgrImplTest("testDeleteAdminUser"));
        }

        /***********************************************************/
        /* 2. Build Up                                             */
        /***********************************************************/
        // PW PolicyMgr APIs:
        if ( Config.getInstance().isOpenldap() || Config.getInstance().isApacheds() )
        {
            suite.addTest( new PswdPolicyMgrImplTest( "testAdd" ) );
            suite.addTest( new PswdPolicyMgrImplTest( "testUpdate" ) );
        }

        // DelegatedAdminMgrImplTest ARBAC Buildup APIs:
        suite.addTest( new DelegatedMgrImplTest( "testAddOrgUnit" ) );
        suite.addTest( new DelegatedMgrImplTest( "testUpdateOrgUnit" ) );
        suite.addTest( new DelegatedMgrImplTest( "testAddOrgInheritance" ) );
        suite.addTest( new DelegatedMgrImplTest( "testAddOrgUnitDescendant" ) );
        suite.addTest( new DelegatedMgrImplTest( "testAddOrgUnitAscendants" ) );
        suite.addTest( new DelegatedMgrImplTest( "testAddRole" ) );
        suite.addTest( new DelegatedMgrImplTest( "testUpdateAdminRole" ) );
        suite.addTest( new DelegatedMgrImplTest( "testAddAdminRoleDescendant" ) );
        suite.addTest( new DelegatedMgrImplTest( "testAddAdminRoleAscendants" ) );
        suite.addTest( new DelegatedMgrImplTest( "testAddAdminRoleInheritance" ) );
        suite.addTest( new DelegatedMgrImplTest( "testAddUser" ) );
        suite.addTest( new DelegatedMgrImplTest( "testAddPermission" ) );
        suite.addTest( new DelegatedMgrImplTest( "testAssignAdminUser" ) );
        suite.addTest( new DelegatedMgrImplTest( "testGrantPermissionRole" ) );

        // AdminMgr RBAC APIs:
        suite.addTest( new AdminMgrImplTest( "testAddRole" ) );
        suite.addTest( new AdminMgrImplTest( "testAddRoleInheritance" ) );
        suite.addTest( new AdminMgrImplTest( "testAddRoleDescendant" ) );
        suite.addTest( new AdminMgrImplTest( "testAddRoleAscendants" ) );
        suite.addTest( new AdminMgrImplTest( "testCreateSsdSet" ) );
        suite.addTest( new AdminMgrImplTest( "testCreateDsdSet" ) );
        suite.addTest( new AdminMgrImplTest( "testAddSsdRoleMember" ) );
        suite.addTest( new AdminMgrImplTest( "testAddDsdRoleMember" ) );
        suite.addTest( new AdminMgrImplTest( "testSsdCardinality" ) );
        suite.addTest( new AdminMgrImplTest( "testDsdCardinality" ) );
        suite.addTest( new AdminMgrImplTest( "testUpdateRole" ) );
        suite.addTest( new AdminMgrImplTest( "testAddUser" ) );
        suite.addTest( new AdminMgrImplTest( "testUpdateUser" ) );
        if ( Config.getInstance().isOpenldap() || Config.getInstance().isApacheds() )
        {
            suite.addTest( new PswdPolicyMgrImplTest( "testUpdatePasswordPolicy" ) );
        }
        suite.addTest( new AdminMgrImplTest( "testAssignUser" ) );
        suite.addTest( new AdminMgrImplTest( "testEnableRoleConstraint" ) );
        suite.addTest( new AdminMgrImplTest( "testAssignUserRoleConstraints" ) );
        suite.addTest( new AdminMgrImplTest( "testAddPermissionObj" ) );
        suite.addTest( new AdminMgrImplTest( "testUpdatePermissionObj" ) );
        suite.addTest( new AdminMgrImplTest( "testAddPermissionOp" ) );
        suite.addTest( new AdminMgrImplTest( "testUpdatePermissionOp" ) );
        suite.addTest( new AdminMgrImplTest( "testGrantPermissionRole" ) );
        suite.addTest( new AdminMgrImplTest( "testGrantPermissionUser" ) );
        suite.addTest( new AdminMgrImplTest( "testAddPermissionAttributeSet" ) );
        suite.addTest( new AdminMgrImplTest( "testAddPermissionAttributeToSet" ) );
        suite.addTest( new AdminMgrImplTest( "testAddPASetToPermission" ) );
        suite.addTest( new AdminMgrImplTest( "testAddUserRoleConstraint" ) );
        suite.addTest( new AdminMgrImplTest( "testRemoveUserRoleConstraint" ) );
        
        // GroupMgr APIs
        suite.addTest( new GroupMgrImplTest( "testAddGroup" ) );
        suite.addTest( new GroupMgrImplTest( "testAssignGroupUserMember" ) );
        suite.addTest( new GroupMgrImplTest( "testAssignGroupRoleMember" ) );
        suite.addTest( new GroupMgrImplTest( "testGroupRoles" ) );
        suite.addTest( new GroupMgrImplTest( "testRoleGroups" ) );

        // PropertyMgr APIs
        suite.addTest( new PropertyMgrImplTest( "testCrudProperties" ) );
        
        /***********************************************************/
        /* 3. Interrogation                                        */
        /***********************************************************/
        // DelReviewMgr ARBAC:
        suite.addTest( new DelegatedMgrImplTest( "testReadOrgUnit" ) );
        suite.addTest( new DelegatedMgrImplTest( "testSearchOrgUnits" ) );
        suite.addTest( new DelegatedMgrImplTest( "testReadAdminRole" ) );
        suite.addTest( new DelegatedMgrImplTest( "testSearchAdminRole" ) );

        // ReviewMgr RBAC:
        if ( Config.getInstance().isOpenldap() || Config.getInstance().isApacheds() )
        {
            suite.addTest( new PswdPolicyMgrImplTest( "testRead" ) );
            suite.addTest( new PswdPolicyMgrImplTest( "testSearch" ) );
        }
        suite.addTest( new ReviewMgrImplTest( "testReadRole" ) );
        suite.addTest( new ReviewMgrImplTest( "testFindRoles" ) );
        suite.addTest( new ReviewMgrImplTest( "testFindRoleNms" ) );
        suite.addTest( new ReviewMgrImplTest( "testReadUser" ) );
        suite.addTest( new ReviewMgrImplTest( "testFindUsers" ) );
        suite.addTest( new ReviewMgrImplTest( "testFindUserIds" ) );
        suite.addTest( new ReviewMgrImplTest( "testAssignedRoles" ) );
        suite.addTest( new ReviewMgrImplTest( "testAssignedRoleNms" ) );
        suite.addTest( new ReviewMgrImplTest( "testAuthorizedRoles" ) );
        suite.addTest( new ReviewMgrImplTest( "testAuthorizedUsers" ) );
        suite.addTest( new ReviewMgrImplTest( "testAuthorizedUserIds" ) );
        suite.addTest( new ReviewMgrImplTest( "testAuthorizedUsersHier" ) );
        suite.addTest( new ReviewMgrImplTest( "testReadPermissionObj" ) );
        suite.addTest( new ReviewMgrImplTest( "testFindPermissionObjs" ) );
        suite.addTest( new ReviewMgrImplTest( "testReadPermissionOp" ) );
        suite.addTest( new ReviewMgrImplTest( "testFindPermissionOps" ) );
        suite.addTest( new ReviewMgrImplTest( "testRolePermissions" ) );
        suite.addTest( new ReviewMgrImplTest( "testPermissionRoles" ) );
        suite.addTest( new ReviewMgrImplTest( "testAuthorizedPermissionRoles" ) );
        suite.addTest( new ReviewMgrImplTest( "testPermissionUsers" ) );
        suite.addTest( new ReviewMgrImplTest( "testAuthorizedPermissionUsers" ) );
        suite.addTest( new ReviewMgrImplTest( "testUserPermissions" ) );
        suite.addTest( new ReviewMgrImplTest( "testFindSsdSets" ) );
        suite.addTest( new ReviewMgrImplTest( "testFindDsdSets" ) );
        suite.addTest( new ReviewMgrImplTest( "testAssignedUserRoleConstraints" ) );
        suite.addTest( new ReviewMgrImplTest( "testReadPermissionAttributeSets" ) );
        suite.addTest( new ReviewMgrImplTest( "testReadPASetFromPermission" ) );
        suite.addTest( new ReviewMgrImplTest( "testReadUserRoleConstraint" ) );
        suite.addTest( new ReviewMgrImplTest( "testFindRoleConstraints" ) );
        suite.addTest( new ReviewMgrImplTest( "testDeassignRoleWithRoleConstraint" ) );
        
        /***********************************************************/
        /* 4. Security Checks                                      */
        /***********************************************************/
        // DelAccessMgr ARABC:
        suite.addTest( new DelegatedMgrImplTest( "testCheckAccess" ) );
        suite.addTest( new DelegatedMgrImplTest( "testCanAssignUser" ) );
        suite.addTest( new DelegatedMgrImplTest( "testCanDeassignUser" ) );
        suite.addTest( new DelegatedMgrImplTest( "testCanGrantPerm" ) );
        suite.addTest( new DelegatedMgrImplTest( "testCanRevokePerm" ) );

        // AccessMgr RBAC:
        suite.addTest( new AccessMgrImplTest( "testGetUserId" ) );
        suite.addTest( new AccessMgrImplTest( "testGetUser" ) );
        if ( Config.getInstance().isOpenldap() || Config.getInstance().isApacheds() )
        {
            // These tests are reliant on OpenLDAP's pwpolicy overlay:
            suite.addTest( new AdminMgrImplTest( "testResetPassword" ) );
            suite.addTest( new AccessMgrImplTest( "testAuthenticateReset" ) );
            suite.addTest( new AdminMgrImplTest( "testChangePassword" ) );
            suite.addTest( new AccessMgrImplTest( "testAuthenticate" ) );
            suite.addTest( new AdminMgrImplTest( "testLockUserAccount" ) );
            suite.addTest( new AccessMgrImplTest( "testAuthenticateLocked" ) );
            suite.addTest( new AdminMgrImplTest( "testUnlockUserAccount" ) );
        }
        suite.addTest( new AccessMgrImplTest( "testCreateSession" ) );
        suite.addTest( new AccessMgrImplTest( "testCreateSessionTrusted" ) );
        suite.addTest( new AccessMgrImplTest( "testCreateSessionHier" ) );
        suite.addTest( new AccessMgrImplTest( "createSessionsDSD" ) );
        // hit it again to make sure the caching is working good:
        suite.addTest( new AccessMgrImplTest( "createSessionsDSD" ) );
        suite.addTest( new AccessMgrImplTest( "testSessionRole" ) );
        suite.addTest( new AccessMgrImplTest( "testCheckAccess" ) );
        suite.addTest( new AccessMgrImplTest( "testAddActiveRole" ) );
        suite.addTest( new AccessMgrImplTest( "testDropActiveRole" ) );
        suite.addTest( new AccessMgrImplTest( "testSessionPermission" ) );
        suite.addTest( new AccessMgrImplTest( "testCreateSessionWithRole" ) );
        suite.addTest( new AccessMgrImplTest( "testCreateSessionWithRolesTrusted" ) );
        //suite.addTest( new ConfigMgrImplTest( "testAddAbacConfig" ) );
        suite.addTest( new AccessMgrImplTest( "testAbacConstraintsRole" ) );

        // PwPolicyMgr PW Policy checks:
        if ( Config.getInstance().isOpenldap() || Config.getInstance().isApacheds() )
        {
            // These tests are reliant on OpenLDAP's pwpolicy overlay:
            suite.addTest( new PswdPolicyMgrImplTest( "testMinAge" ) );
            suite.addTest( new PswdPolicyMgrImplTest( "testMaxAge" ) );
            suite.addTest( new PswdPolicyMgrImplTest( "testInHistory" ) );
            suite.addTest( new PswdPolicyMgrImplTest( "testMinLength" ) );
            suite.addTest( new PswdPolicyMgrImplTest( "testExpireWarning" ) );
            suite.addTest( new PswdPolicyMgrImplTest( "testGraceLoginLimit" ) );
            suite.addTest( new PswdPolicyMgrImplTest( "testMaxFailure" ) );
            suite.addTest( new PswdPolicyMgrImplTest( "testLockoutDuration" ) );
            suite.addTest( new PswdPolicyMgrImplTest( "testLockout" ) );
            suite.addTest( new PswdPolicyMgrImplTest( "testFailureCountInterval" ) );
            suite.addTest( new PswdPolicyMgrImplTest( "testMustChange" ) );
            suite.addTest( new PswdPolicyMgrImplTest( "testAllowUserChange" ) );
            suite.addTest( new PswdPolicyMgrImplTest( "testSafeModify" ) );
        }

        /***********************************************************/
        /* 5. Audit Checks                                         */
        /***********************************************************/
        //suite.addTest(new AuditMgrImplTest("testSearchAuthNInvalid"));
        if ( Config.getInstance().isOpenldap() )
        {
            // These tests reliant on OpenLDAP's slapo access log overlay:
            suite.addTest( new AuditMgrImplTest( "testSearchBinds" ) );
            suite.addTest( new AuditMgrImplTest( "testGetAuthZs" ) );
            suite.addTest( new AuditMgrImplTest( "testSearchAuthZs" ) );
            suite.addTest( new AuditMgrImplTest( "testSearchMods" ) );
            suite.addTest( new AuditMgrImplTest( "testSearchAdminMods" ) );
        }

        suite.addTest( new FortressJUnitTest( "testDisplayCounters" ) );
        return suite;
    }

    /**
     * Constructor for the FortressJUnitTest object
     *
     * @param name Description of the Parameter
     */
    public FortressJUnitTest( String name )
    {
        super( name );
    }

    public void testDisplayCounters()
    {
        LdapCounters counters = LdapDataProvider.getLdapCounters();
        System.out.println( "NUMBER OF ADDS: " + counters.getAdd() );
        System.out.println( "NUMBER OF BINDS: " + counters.getBind() );
        System.out.println( "NUMBER OF DELETES: " + counters.getDelete() );
        System.out.println( "NUMBER OF MODS: " + counters.getMod() );
        System.out.println( "NUMBER OF READS: " + counters.getRead() );
        System.out.println( "NUMBER OF SEARCHES: " + counters.getSearch() );
        //System.out.println( "NUMBER OF COMPARES: " + counters.getCompare() );
    }

    /**
     * The JUnit setup method
     *
     * @throws Exception Description of the Exception
     */
    public void setUp() throws Exception
    {
        super.setUp();
    }

    /**
     * The teardown method for JUnit
     *
     * @throws Exception Description of the Exception
     */
    public void tearDown() throws Exception
    {
        super.tearDown();
    }
}
