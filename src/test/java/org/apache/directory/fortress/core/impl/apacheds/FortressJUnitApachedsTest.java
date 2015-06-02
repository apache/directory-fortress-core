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

import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.AccessMgrImplTest;
import org.apache.directory.fortress.core.impl.AdminMgrImplTest;
import org.apache.directory.fortress.core.impl.AdminRoleTestData;
import org.apache.directory.fortress.core.impl.DelegatedMgrImplTest;
import org.apache.directory.fortress.core.impl.DelegatedMgrImplTest.ASSIGN_OP;
import org.apache.directory.fortress.core.impl.DelegatedMgrImplTest.GRANT_OP;
import org.apache.directory.fortress.core.impl.FortressJUnitTest;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.impl.OrgUnitTestData;
import org.apache.directory.fortress.core.impl.PRATestData;
import org.apache.directory.fortress.core.impl.PermTestData;
import org.apache.directory.fortress.core.impl.ReviewMgrImplTest;
import org.apache.directory.fortress.core.impl.RoleTestData;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.impl.URATestData;
import org.apache.directory.fortress.core.impl.UserTestData;
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
public class FortressJUnitApachedsTest extends AbstractLdapTestUnit
{
    private static final String CLS_NM = DelegatedMgrImplTest.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static Session adminSess = null;


    @Before
    public void init()
    {
        CacheMgr.getInstance().clearAll();
    }


    @After
    public void displayCounters()
    {
        // TODO: FIX THIS: (removed with unboundid)
        //LdapCounters counters = UnboundIdDataProvider.getLdapCounters();
        //System.out.println( "NUMBER OF READS: " + counters.getRead() );
        //System.out.println( "NUMBER OF SEARCHES: " + counters.getSearch() );
        //System.out.println( "NUMBER OF COMPARES: " + counters.getCompare() );
        //System.out.println( "NUMBER OF BINDS: " + counters.getBind() );
        //System.out.println( "NUMBER OF ADDS: " + counters.getAdd() );
        //System.out.println( "NUMBER OF MODS: " + counters.getMod() );
        //System.out.println( "NUMBER OF DELETES: " + counters.getDelete() );
    }


    /***********************************************************/
    /* 2. Build Up                                             */
    /***********************************************************/
    // DelegatedAdminMgrImplTest ARBAC Buildup APIs:
    @Test
    public void testAddAdminUser()
    {
        DelegatedMgrImplTest.addOrgUnit( "ADD ORG_PRM_APP0", OrgUnitTestData.ORGS_PRM_APP0[0] );
        DelegatedMgrImplTest.addOrgUnit( "ADD ORG_USR_DEV0", OrgUnitTestData.ORGS_USR_DEV0[0] );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ARLS SUPER", AdminRoleTestData.AROLES_SUPER, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS PSWDMGR_OBJ", PermTestData.PSWDMGR_OBJ, false, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS ADMINMGR_OBJ", PermTestData.ADMINMGR_OBJ, false, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS DELEGATEDMGR_OBJ", PermTestData.DELEGATEDMGR_OBJ, false, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS DELEGATEDREVIEWMGR_OBJ", PermTestData.DELEGATEDREVIEWMGR_OBJ, false,
            false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS REVIEWMGR_OBJ", PermTestData.REVIEWMGR_OBJ, false, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS PSWDMGR_OBJ PSWDMGR_OPS", PermTestData.PSWDMGR_OBJ,
            PermTestData.PSWDMGR_OPS, false, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS ADMINMGR_OBJ ADMINMGR_OPS", PermTestData.ADMINMGR_OBJ,
            PermTestData.ADMINMGR_OPS, false, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS DELEGATEDMGR_OBJ DELEGATEDMGR_OPS", PermTestData.DELEGATEDMGR_OBJ,
            PermTestData.DELEGATEDMGR_OPS, false, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS DELEGATEDREVIEWMGR_OBJ DELEGATEDREVIEWMGR_OPS",
            PermTestData.DELEGATEDREVIEWMGR_OBJ, PermTestData.DELEGATEDREVIEWMGR_OPS, false, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS REVIEWMGR_OBJ REVIEWMGR_OPS", PermTestData.REVIEWMGR_OBJ,
            PermTestData.REVIEWMGR_OPS, false, false );
        AdminMgrImplTest.addRoleGrants( "GRNT-APRMS SUPER PSWDMGR_OBJ PSWDMGR_OPS", AdminRoleTestData.AROLES_SUPER,
            PermTestData.PSWDMGR_OBJ, PermTestData.PSWDMGR_OPS, false, false );
        AdminMgrImplTest.addRoleGrants( "GRNT-APRMS SUPER ADMINMGR_OBJ ADMINMGR_OPS", AdminRoleTestData.AROLES_SUPER,
            PermTestData.ADMINMGR_OBJ, PermTestData.ADMINMGR_OPS, false, false );
        AdminMgrImplTest.addRoleGrants( "GRNT-APRMS SUPER DELEGATEDMGR_OBJ DELEGATEDMGR_OPS",
            AdminRoleTestData.AROLES_SUPER, PermTestData.DELEGATEDMGR_OBJ, PermTestData.DELEGATEDMGR_OPS, false, false );
        AdminMgrImplTest.addRoleGrants( "GRNT-APRMS SUPER DELEGATEDREVIEWMGR_OBJ DELEGATEDREVIEWMGR_OPS",
            AdminRoleTestData.AROLES_SUPER, PermTestData.DELEGATEDREVIEWMGR_OBJ, PermTestData.DELEGATEDREVIEWMGR_OPS,
            false, false );
        AdminMgrImplTest.addRoleGrants( "GRNT-APRMS SUPER REVIEWMGR_OBJ REVIEWMGR_OPS", AdminRoleTestData.AROLES_SUPER,
            PermTestData.REVIEWMGR_OBJ, PermTestData.REVIEWMGR_OPS, false, false );
        AdminMgrImplTest.addUsers( "ADD-USRS TU0", UserTestData.USERS_TU0, false );
        DelegatedMgrImplTest.assignAdminUsers( "ASGN-USRS TU0 SUPER", UserTestData.USERS_TU0,
            AdminRoleTestData.AROLES_SUPER, false );

        // do these last - may have already been added via demoUsers:
        AdminMgrImplTest.addPermObjs( "ADD-OBS AUDITMGR_OBJ", PermTestData.AUDITMGR_OBJ, false, true );
        AdminMgrImplTest.addPermOps( "ADD-OPS AUDITMGR_OBJ AUDITMGR_OPS", PermTestData.AUDITMGR_OBJ,
            PermTestData.AUDITMGR_OPS, false, true );
        AdminMgrImplTest.addRoleGrants( "GRNT-APRMS SUPER AUDITMGR_OBJ AUDITMGR_OPS", AdminRoleTestData.AROLES_SUPER,
            PermTestData.AUDITMGR_OBJ, PermTestData.AUDITMGR_OPS, false, true );
    }


    @Test
    public void testAddOrgUnit()
    {
        //addOrgUnits("ADD ORGS_USR_DEV0", OrgUnitTestData.ORGS_USR_DEV0);
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_TO1", OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO2", OrgUnitTestData.ORGS_USR_TO2 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO4", OrgUnitTestData.ORGS_PRM_TO4 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );

        // The DEV1 OU is not removed during cleanup phase because the test system users belong to it:
        if ( FortressJUnitTest.isFirstRun() )
        {
            DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        }

        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );
    }


    @Test
    public void testUpdateOrgUnit()
    {
        // Inject the needed data for the test
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_TO1", OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3 );

        // The test itself
        DelegatedMgrImplTest.updateOrgUnits( "UPD ORGS_TO1", OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.updateOrgUnits( "UPD ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3 );
    }


    @Test
    public void testAddOrgInheritance()
    {
        // Inject the needed data for the test
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO2", OrgUnitTestData.ORGS_USR_TO2 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO4", OrgUnitTestData.ORGS_PRM_TO4 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );

        // The test itself
        DelegatedMgrImplTest.addInheritedOrgUnits( "ADD-INHERIT ORGS_USR_TO2", OrgUnitTestData.ORGS_USR_TO2 );
        DelegatedMgrImplTest.addInheritedOrgUnits( "ADD-INHERIT ORGS_PRM_TO4", OrgUnitTestData.ORGS_PRM_TO4 );
        DelegatedMgrImplTest.addInheritedOrgUnits( "ADD-INHERIT ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );
        DelegatedMgrImplTest.addInheritedOrgUnits( "ADD-INHERIT ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );
    }


    @Test
    public void testAddOrgUnitDescendant()
    {
        DelegatedMgrImplTest.addOrgUnitDescendant( "ADD ORGS-USR-TO6-DESC", OrgUnitTestData.ORGS_USR_TO6_DSC,
            OrgUnit.Type.USER );
        DelegatedMgrImplTest.addOrgUnitDescendant( "ADD ORGS-PRM-TO6-DESC", OrgUnitTestData.ORGS_PRM_TO6_DSC,
            OrgUnit.Type.PERM );
    }


    @Test
    public void testAddOrgUnitAscendants()
    {
        DelegatedMgrImplTest.addOrgUnitAscendant( "ADD-ORGS-USR-TR7-ASC", OrgUnitTestData.ORGS_USR_TO7_ASC,
            OrgUnit.Type.USER );
        DelegatedMgrImplTest.addOrgUnitAscendant( "ADD-ORGS-PRM-TR7-ASC", OrgUnitTestData.ORGS_PRM_TO7_ASC,
            OrgUnit.Type.PERM );
    }


    @Test
    public void testAddRole()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO2", OrgUnitTestData.ORGS_USR_TO2 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_TO1", OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO4", OrgUnitTestData.ORGS_PRM_TO4 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );

        // The test itself
        //     public Role setRole(Role role)
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );

        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR1", AdminRoleTestData.AROLES_TR1, true );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR2", AdminRoleTestData.AROLES_TR2, true );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR3", AdminRoleTestData.AROLES_TR3, true );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR6", AdminRoleTestData.AROLES_TR6_HIER, true );
    }


    @Test
    public void testUpdateAdminRole()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR3", AdminRoleTestData.AROLES_TR3, true );

        // The test itself
        //     public Role updateRole(Role role)
        DelegatedMgrImplTest.updateAdminRoles( "UPD-ADMRLS TR3_UPD", AdminRoleTestData.AROLES_TR3_UPD, true );
    }


    @Test
    public void testAddAdminRoleDescendant()
    {
        DelegatedMgrImplTest.addAdminRoleDescendant( "ADD-ARLS-TR5-DESC", AdminRoleTestData.AROLES_TR5_DSC );
    }


    @Test
    public void testAddAdminRoleAscendants()
    {
        DelegatedMgrImplTest.addAdminRoleAscendant( "ADD-ARLS-TR4-ASC", AdminRoleTestData.AROLES_TR4_ASC );
    }


    @Test
    public void testAddAdminRoleInheritance()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR6", AdminRoleTestData.AROLES_TR6_HIER, true );

        // The test itself
        DelegatedMgrImplTest.addInheritedAdminRoles( "ADD-ARLS-TR6-HIER", AdminRoleTestData.AROLES_TR6_HIER );
    }


    @Test
    public void testAddUser()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_TO1", OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );

        // The test itself
        //     public User addUser(User user)
        // the admin user must be added before the "addUsers" can be called:
        //AdminMgrImplTest.addAdminUser("ADD-USRS TU0", UserTestData.USERS_TU0[0]);
        AdminMgrImplTest.addUsers( "ADD-USRS TU16_ARBAC", UserTestData.USERS_TU16_ARBAC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU16B_ARBAC", UserTestData.USERS_TU16B_ARBAC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU16A_ARBAC", UserTestData.USERS_TU17A_ARBAC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU16U_ARBAC", UserTestData.USERS_TU17U_ARBAC, true );
    }


    @Test
    public void testAddPermission()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // The test itself
        //     public Permission addPermObj(Permission pOp)
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB5", PermTestData.OBJS_TOB5, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS ARBAC1", PermTestData.ARBAC_OBJS_1, true, false );
        AdminMgrImplTest
            .addPermOps( "ADD-OPS ARBAC1", PermTestData.ARBAC_OBJS_1, PermTestData.ARBAC_OPS_1, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS ARBAC2", PermTestData.ARBAC_OBJ2, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS ARBAC2", PermTestData.ARBAC_OBJ2, PermTestData.ARBAC_OPS_2, true, false );
    }


    @Test
    public void testAssignAdminUser()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO2", OrgUnitTestData.ORGS_USR_TO2 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_TO1", OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO4", OrgUnitTestData.ORGS_PRM_TO4 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR1", AdminRoleTestData.AROLES_TR1, true );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR2", AdminRoleTestData.AROLES_TR2, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU16_ARBAC", UserTestData.USERS_TU16_ARBAC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU16A_ARBAC", UserTestData.USERS_TU17A_ARBAC, true );

        // The test itself
        //     public void assignUser(User user, Role role)
        DelegatedMgrImplTest.assignAdminUsers( "ASGN-USRS TU16 TR1", UserTestData.USERS_TU16_ARBAC,
            AdminRoleTestData.AROLES_TR1, true );
        DelegatedMgrImplTest.assignAdminUserRole( "ASGN-USR TU17A TR2", UserTestData.USERS_TU17A_ARBAC,
            AdminRoleTestData.AROLES_TR2, true );
    }


    @Test
    public void testGrantPermissionRole()
    {
        // Add the needed data for this test 
        // From testAddUser
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_TO1", OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );
        AdminMgrImplTest.addUsers( "ADD-USRS TU16_ARBAC", UserTestData.USERS_TU16_ARBAC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU16B_ARBAC", UserTestData.USERS_TU16B_ARBAC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU16A_ARBAC", UserTestData.USERS_TU17A_ARBAC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU16U_ARBAC", UserTestData.USERS_TU17U_ARBAC, true );

        // From testAddPermission
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        //     public Permission addPermObj(Permission pOp)
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB5", PermTestData.OBJS_TOB5, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS ARBAC1", PermTestData.ARBAC_OBJS_1, true, false );
        AdminMgrImplTest
            .addPermOps( "ADD-OPS ARBAC1", PermTestData.ARBAC_OBJS_1, PermTestData.ARBAC_OPS_1, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS ARBAC2", PermTestData.ARBAC_OBJ2, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS ARBAC2", PermTestData.ARBAC_OBJ2, PermTestData.ARBAC_OPS_2, true, false );

        // From testAssignAdminUser
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO2", OrgUnitTestData.ORGS_USR_TO2 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO4", OrgUnitTestData.ORGS_PRM_TO4 );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR1", AdminRoleTestData.AROLES_TR1, true );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR2", AdminRoleTestData.AROLES_TR2, true );

        // The test itself
        //     public void grantPermission(Permission pOp, Role role)
        AdminMgrImplTest.addRoleGrants( "GRNT-APRMS ARTR2 AROBJ1 AROPS1", AdminRoleTestData.AROLES_TR2,
            PermTestData.ARBAC_OBJS_1, PermTestData.ARBAC_OPS_1, true, false );
    }


    // AdminMgr RBAC APIs:
    @Test
    public void testAdminMgrAddRole()
    {
        //     public Role addRole(Role role)
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR4", RoleTestData.ROLES_TR4 );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR8_SSD", RoleTestData.ROLES_TR8_SSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR9_SSD", RoleTestData.ROLES_TR9_SSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR10_SSD", RoleTestData.ROLES_TR10_SSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR8_DSD", RoleTestData.ROLES_TR8_DSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR9_DSD", RoleTestData.ROLES_TR9_DSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR10_DSD", RoleTestData.ROLES_TR10_DSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR16_SD", RoleTestData.ROLES_TR16_SD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR17_DSD_BRUNO", RoleTestData.ROLES_TR17_DSD_BRUNO );
    }


    @Test
    public void testAdminMgrAddRoleInheritance()
    {
        // Add the needed data for this test 
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR4", RoleTestData.ROLES_TR4 );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B );

        // The test itself
        //TODO: Test goes here...
        //     public void addInheritance(Role parentRole, Role childRole)
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B );
    }


    @Test
    public void testAdminMgrddRoleDescendant()
    {
        //TODO: Test goes here...
        //     public void addDescendant(Role parentRole, Role childRole)
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR6-DESC", RoleTestData.ROLES_TR6_DESC );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR11-DESC-SSD", RoleTestData.ROLES_TR11_DESC_SSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR12-DESC-SSD", RoleTestData.ROLES_TR12_DESC_SSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR13-DESC-SSD", RoleTestData.ROLES_TR13_DESC_SSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR11-DESC-DSD", RoleTestData.ROLES_TR11_DESC_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR12-DESC-DSD", RoleTestData.ROLES_TR12_DESC_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR13-DESC-DSD", RoleTestData.ROLES_TR13_DESC_DSD );
    }


    @Test
    public void testAdminMgrAddRoleAscendants()
    {
        //TODO: Test goes here...
        //     public void addDescendant(Role parentRole, Role childRole)
        AdminMgrImplTest.addRoleAscendant( "ADD-RLS-TR7-ASC", RoleTestData.ROLES_TR7_ASC );
    }


    @Test
    public void testAdminMgrCreateSsdSet()
    {
        // Add the needed data for this test 
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR8_SSD", RoleTestData.ROLES_TR8_SSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR11-DESC-SSD", RoleTestData.ROLES_TR11_DESC_SSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR12-DESC-SSD", RoleTestData.ROLES_TR12_DESC_SSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR13-DESC-SSD", RoleTestData.ROLES_TR13_DESC_SSD );

        // The test itself
        //     public SDSet createSsdSet(SDSet ssdSet)
        AdminMgrImplTest.createSsdSet( "ADD-SSD T1", RoleTestData.SSD_T1 );
        AdminMgrImplTest.createSsdSet( "ADD-SSD T4", RoleTestData.SSD_T4 );
        AdminMgrImplTest.createSsdSet( "ADD-SSD T5", RoleTestData.SSD_T5 );
        AdminMgrImplTest.createSsdSet( "ADD-SSD T6", RoleTestData.SSD_T6 );
    }


    @Test
    public void testAdminMgrCreateDsdSet()
    {
        // Add the needed data for this test 
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR8_DSD", RoleTestData.ROLES_TR8_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR11-DESC-DSD", RoleTestData.ROLES_TR11_DESC_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR12-DESC-DSD", RoleTestData.ROLES_TR12_DESC_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR13-DESC-DSD", RoleTestData.ROLES_TR13_DESC_DSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR17_DSD_BRUNO", RoleTestData.ROLES_TR17_DSD_BRUNO );

        // The test itself
        //     public SDSet createDsdSet(SDSet dsdSet)
        AdminMgrImplTest.createDsdSet( "ADD-DSD T1", RoleTestData.DSD_T1 );
        AdminMgrImplTest.createDsdSet( "ADD-DSD T4", RoleTestData.DSD_T4 );
        AdminMgrImplTest.createDsdSet( "ADD-DSD T5", RoleTestData.DSD_T5 );
        AdminMgrImplTest.createDsdSet( "ADD-DSD T6", RoleTestData.DSD_T6 );
        AdminMgrImplTest.createDsdSet( "ADD-DSD T8 BRUNO", RoleTestData.DSD_T8_BRUNO );
    }


    @Test
    public void testAdminMgrAddSsdRoleMember()
    {
        // Add the needed data for this test 
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR9_SSD", RoleTestData.ROLES_TR9_SSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR10_SSD", RoleTestData.ROLES_TR10_SSD );

        // The test itself
        //     public SDSet addSsdRoleMember(SDSet ssdSet, Role role)
        AdminMgrImplTest.addSsdRoleMember( "ADD-MEM-SSD T2 TR9", RoleTestData.SSD_T2, RoleTestData.ROLES_TR9_SSD );
        AdminMgrImplTest.addSsdRoleMember( "ADD-MEM-SSD T3 TR10", RoleTestData.SSD_T3, RoleTestData.ROLES_TR10_SSD );
    }


    @Test
    public void testAdminMgrAddDsdRoleMember()
    {
        // Add the needed data for this test 
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR9_DSD", RoleTestData.ROLES_TR9_DSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR10_DSD", RoleTestData.ROLES_TR10_DSD );

        // The test itself
        //     public SDSet addDsdRoleMember(SDSet dsdSet, Role role)
        AdminMgrImplTest.addDsdRoleMember( "ADD-MEM-DSD T2 TR9", RoleTestData.DSD_T2, RoleTestData.ROLES_TR9_DSD );
        AdminMgrImplTest.addDsdRoleMember( "ADD-MEM-DSD T3 TR10", RoleTestData.DSD_T3, RoleTestData.ROLES_TR10_DSD );

    }


    @Test
    public void testAdminMgrSsdCardinality()
    {
        // Add the needed data for this test 
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR16_SD", RoleTestData.ROLES_TR16_SD );

        // The test itself
        AdminMgrImplTest.setSsdCardinality( "CARD-SSD T7 TR16", RoleTestData.SSD_T7, RoleTestData.ROLES_TR16_SD );
    }


    @Test
    public void testAdminMgrDsdCardinality()
    {
        // Add the needed data for this test 
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR16_SD", RoleTestData.ROLES_TR16_SD );

        // The test itself
        AdminMgrImplTest.setDsdCardinality( "CARD-DSD T7 TR16", RoleTestData.DSD_T7, RoleTestData.ROLES_TR16_SD );
    }


    @Test
    public void testAdminMgrUpdateRole()
    {
        // Add the needed data for this test 
        AdminMgrImplTest.addRoles( "ADD-RLS TR4", RoleTestData.ROLES_TR4 );

        // The test itself
        //     public Role updateRole(Role role)
        AdminMgrImplTest.updateRoles( "UPD-RLS TR4_UPD", RoleTestData.ROLES_TR4_UPD );
    }


    @Test
    public void testAdminMgrAddUser()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR6-DESC", RoleTestData.ROLES_TR6_DESC );
        AdminMgrImplTest.addRoleAscendant( "ADD-RLS-TR7-ASC", RoleTestData.ROLES_TR7_ASC );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR17_DSD_BRUNO", RoleTestData.ROLES_TR17_DSD_BRUNO );

        // The test itself
        //     public User addUser(User user)
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU2", UserTestData.USERS_TU2, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU5", UserTestData.USERS_TU5, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU6", UserTestData.USERS_TU6, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU7_HIER", UserTestData.USERS_TU7_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU8_SSD", UserTestData.USERS_TU8_SSD, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU12_DSD", UserTestData.USERS_TU12_DSD, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU13_DSD_HIER", UserTestData.USERS_TU13_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU14_DSD_HIER", UserTestData.USERS_TU14_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU15_DSD_HIER", UserTestData.USERS_TU15_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU18 TR6_DESC", UserTestData.USERS_TU18U_TR6_DESC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU19 TR7_ASC", UserTestData.USERS_TU19U_TR7_ASC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU20 TR5_HIER", UserTestData.USERS_TU20U_TR5B, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU21 DSD_BRUNO", UserTestData.USERS_TU21_DSD_BRUNO, true );
    }


    @Test
    public void testAdminMgrUpdateUser()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );

        // The test itself
        //     public User updateUser(User user)
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );
    }


    @Test
    public void testAdminMgrAssignUser()
    {
        // Add the needed data for this test
        // From testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_TO1", OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO2", OrgUnitTestData.ORGS_USR_TO2 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO4", OrgUnitTestData.ORGS_PRM_TO4 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // From testAddRole Delegated
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR1", AdminRoleTestData.AROLES_TR1, true );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR2", AdminRoleTestData.AROLES_TR2, true );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR3", AdminRoleTestData.AROLES_TR3, true );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR6", AdminRoleTestData.AROLES_TR6_HIER, true );

        // From testAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU16_ARBAC", UserTestData.USERS_TU16_ARBAC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU16B_ARBAC", UserTestData.USERS_TU16B_ARBAC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU16A_ARBAC", UserTestData.USERS_TU17A_ARBAC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU16U_ARBAC", UserTestData.USERS_TU17U_ARBAC, true );

        // From testAddRole Admin
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR4", RoleTestData.ROLES_TR4 );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR8_SSD", RoleTestData.ROLES_TR8_SSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR9_SSD", RoleTestData.ROLES_TR9_SSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR10_SSD", RoleTestData.ROLES_TR10_SSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR8_DSD", RoleTestData.ROLES_TR8_DSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR9_DSD", RoleTestData.ROLES_TR9_DSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR10_DSD", RoleTestData.ROLES_TR10_DSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR16_SD", RoleTestData.ROLES_TR16_SD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR17_DSD_BRUNO", RoleTestData.ROLES_TR17_DSD_BRUNO );

        // From testAddRoleDescendant
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR6-DESC", RoleTestData.ROLES_TR6_DESC );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR11-DESC-SSD", RoleTestData.ROLES_TR11_DESC_SSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR12-DESC-SSD", RoleTestData.ROLES_TR12_DESC_SSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR13-DESC-SSD", RoleTestData.ROLES_TR13_DESC_SSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR11-DESC-DSD", RoleTestData.ROLES_TR11_DESC_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR12-DESC-DSD", RoleTestData.ROLES_TR12_DESC_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR13-DESC-DSD", RoleTestData.ROLES_TR13_DESC_DSD );

        // From testCreateSsdSet
        AdminMgrImplTest.createSsdSet( "ADD-SSD T1", RoleTestData.SSD_T1 );
        AdminMgrImplTest.createSsdSet( "ADD-SSD T4", RoleTestData.SSD_T4 );
        AdminMgrImplTest.createSsdSet( "ADD-SSD T5", RoleTestData.SSD_T5 );
        AdminMgrImplTest.createSsdSet( "ADD-SSD T6", RoleTestData.SSD_T6 );

        // From testAddUser (Admin)
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU2", UserTestData.USERS_TU2, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );

        AdminMgrImplTest.addUsers( "ADD-USRS TU7_HIER", UserTestData.USERS_TU7_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU8_SSD", UserTestData.USERS_TU8_SSD, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU12_DSD", UserTestData.USERS_TU12_DSD, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU13_DSD_HIER", UserTestData.USERS_TU13_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU14_DSD_HIER", UserTestData.USERS_TU14_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU15_DSD_HIER", UserTestData.USERS_TU15_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU20 TR5_HIER", UserTestData.USERS_TU20U_TR5B, true );

        // The test itself
        //     public void assignUser(User user, Role role)
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );
        AdminMgrImplTest.assignUsersH( "ASGN-USRS_H TU7 HIER TR5 HIER", UserTestData.USERS_TU7_HIER,
            RoleTestData.ROLES_TR5_HIER, true );
        AdminMgrImplTest.assignUsersH( "ASGN-USRS_H TU20 TR5B HIER", UserTestData.USERS_TU20U_TR5B,
            RoleTestData.ROLES_TR5B, true );
        AdminMgrImplTest.assignUsersSSD( "ASGN-USRS_SSDT1 TU8 SSD_T1", UserTestData.USERS_TU8_SSD, RoleTestData.SSD_T1 );
        AdminMgrImplTest.assignUsersSSD( "ASGN-USRS_SSDT4B TU9 SSD_T4_B", UserTestData.USERS_TU9_SSD_HIER,
            RoleTestData.SSD_T4_B );
        AdminMgrImplTest.assignUsersSSD( "ASGN-USRS_SSDT5B TU10 SSD_T5_B", UserTestData.USERS_TU10_SSD_HIER,
            RoleTestData.SSD_T5_B );
        AdminMgrImplTest.assignUsersSSD( "ASGN-USRS_SSDT6B TU11 SSD_T6_B", UserTestData.USERS_TU11_SSD_HIER,
            RoleTestData.SSD_T6_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT1 TU8 DSD_T1", UserTestData.USERS_TU8_SSD, RoleTestData.DSD_T1 );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT4B TU9 DSD_T4_B", UserTestData.USERS_TU9_SSD_HIER,
            RoleTestData.DSD_T4_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT5B TU10 DSD_T5_B", UserTestData.USERS_TU10_SSD_HIER,
            RoleTestData.DSD_T5_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT6B TU11 DSD_T6_B", UserTestData.USERS_TU11_SSD_HIER,
            RoleTestData.DSD_T6_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT1 TU12 DSD_T1", UserTestData.USERS_TU12_DSD,
            RoleTestData.DSD_T1 );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT4B TU13 DSD_T4_B", UserTestData.USERS_TU13_DSD_HIER,
            RoleTestData.DSD_T4_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT5B TU14 DSD_T5_B", UserTestData.USERS_TU14_DSD_HIER,
            RoleTestData.DSD_T5_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT6B TU15 DSD_T6_B", UserTestData.USERS_TU15_DSD_HIER,
            RoleTestData.DSD_T6_B );
    }


    @Test
    public void testAdminMgrAddPermissionObj() throws SecurityException
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // The test itself
        //     public Permission addPermObj(Permission pOp)
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB2", PermTestData.OBJS_TOB2, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB3", PermTestData.OBJS_TOB3, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB4", PermTestData.OBJS_TOB4, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB6", PermTestData.OBJS_TOB6, true, false );
    }


    @Test
    public void testAdminMgrUpdatePermissionObj() throws SecurityException
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB4", PermTestData.OBJS_TOB4, true, false );

        // The test itself
        AdminMgrImplTest.updatePermObjs( "UPD-OBS TOB4_UPD", PermTestData.OBJS_TOB4_UPD, true );
    }


    @Test
    public void testAdminMgrAddPermissionOp()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB2", PermTestData.OBJS_TOB2, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB3", PermTestData.OBJS_TOB3, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB4", PermTestData.OBJS_TOB4, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB6", PermTestData.OBJS_TOB6, true, false );

        // The test itself
        //     public PermObj addPermObj(PermObj pObj)
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB2 TOP2", PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB3 TOP3", PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB4 TOP4", PermTestData.OBJS_TOB4, PermTestData.OPS_TOP4, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB6 TOP5", PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5, true, false );
    }


    @Test
    public void testAdminMgrUpdatePermissionOp()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );

        // The test itself
        AdminMgrImplTest.updatePermOps( "UPD-OPS TOB1 TOP1_UPD", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1_UPD,
            true );
    }


    /**
     * AMT24
     *
     * @throws SecurityException
     */
    @Test
    public void testAdminMgrGrantPermissionRole()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB2", PermTestData.OBJS_TOB2, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB3", PermTestData.OBJS_TOB3, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB4", PermTestData.OBJS_TOB4, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB6", PermTestData.OBJS_TOB6, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB2 TOP2", PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB3 TOP3", PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB4 TOP4", PermTestData.OBJS_TOB4, PermTestData.OPS_TOP4, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB6 TOP5", PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5, true, false );
        AdminMgrImplTest.updatePermOps( "UPD-OPS TOB1 TOP1_UPD", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1_UPD,
            true );
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B );
        AdminMgrImplTest.addUsers( "ADD-USRS TU20 TR5_HIER", UserTestData.USERS_TU20U_TR5B, true );

        // The test itself
        //     public void grantPermission(Permission pOp, Role role)
        AdminMgrImplTest.addRoleGrants( "GRNT-PRMS TR1 TOB1 TOP1", RoleTestData.ROLES_TR1, PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1, true, false );
        AdminMgrImplTest.addRoleGrants( "GRNT-PRMS TR2 TOB2 TOP2", RoleTestData.ROLES_TR2, PermTestData.OBJS_TOB2,
            PermTestData.OPS_TOP2, true, false );
        AdminMgrImplTest.addRoleGrants( "GRNT-PRMS TR3 TOB3 TOP3", RoleTestData.ROLES_TR3, PermTestData.OBJS_TOB3,
            PermTestData.OPS_TOP3, true, false );
        AdminMgrImplTest.addRoleGrantsH( "GRNT-PRMS_H ROLES_TR5_HIER TOB4 TOP4", RoleTestData.ROLES_TR5_HIER,
            PermTestData.OBJS_TOB4,
            PermTestData.OPS_TOP4 );
        AdminMgrImplTest.addRoleGrantsHB( "GRNT-PRMS_HB USERS TU20 ROLES_TR5B TOB6 TOP5",
            UserTestData.USERS_TU20U_TR5B,
            RoleTestData.ROLES_TR5B, PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5 );
    }


    @Test
    public void testAdminMgrGrantPermissionUser()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );

        // The test itself
        //     public void grantPermission(Permission pOp, User user)
        AdminMgrImplTest.addUserGrants( "GRNT-PRMS TU1 TOB1 TOP1", UserTestData.USERS_TU1, PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1 );
    }


    /***********************************************************/
    /* 3. Interrogation                                        */
    /***********************************************************/
    // DelReviewMgr ARBAC:
    @Test
    public void testReadOrgUnit()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_TO1", OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3 );

        // The test itself
        // public Role readRole(Role role)
        DelegatedMgrImplTest.readOrgUnits( "RD ORGS_TO1", OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.readOrgUnits( "RD ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3 );
    }


    @Test
    public void testSearchOrgUnits()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_TO1", OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3 );

        // The test itself
        DelegatedMgrImplTest.searchOrgUnits( "SRCH ORGS_TO1",
            TestUtils.getSrchValue( OrgUnitTestData.getName( OrgUnitTestData.ORGS_TO1[0] ) ), OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.searchOrgUnits( "SRCH ORGS_PRM_TO3",
            TestUtils.getSrchValue( OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO3[0] ) ),
            OrgUnitTestData.ORGS_PRM_TO3 );
    }


    @Test
    public void testReadAdminRole()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO2", OrgUnitTestData.ORGS_USR_TO2 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_TO1", OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO4", OrgUnitTestData.ORGS_PRM_TO4 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );

        //     public Role setRole(Role role)
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );

        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR1", AdminRoleTestData.AROLES_TR1, true );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR2", AdminRoleTestData.AROLES_TR2, true );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR3", AdminRoleTestData.AROLES_TR3, true );
        DelegatedMgrImplTest.updateAdminRoles( "UPD-ADMRLS TR3_UPD", AdminRoleTestData.AROLES_TR3_UPD, true );

        // The test itself
        // public Role readRole(Role role)
        DelegatedMgrImplTest.readAdminRoles( "RD-ADMRLS TR1", AdminRoleTestData.AROLES_TR1 );
        DelegatedMgrImplTest.readAdminRoles( "RD-ADMRLS TR2", AdminRoleTestData.AROLES_TR2 );
        DelegatedMgrImplTest.readAdminRoles( "RD-ADMRLS TR3_UPD", AdminRoleTestData.AROLES_TR3_UPD );
    }


    @Test
    public void testSearchAdminRole()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO2", OrgUnitTestData.ORGS_USR_TO2 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_TO1", OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO4", OrgUnitTestData.ORGS_PRM_TO4 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );

        //     public Role setRole(Role role)
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );

        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR1", AdminRoleTestData.AROLES_TR1, true );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR2", AdminRoleTestData.AROLES_TR2, true );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR3", AdminRoleTestData.AROLES_TR3, true );
        DelegatedMgrImplTest.updateAdminRoles( "UPD-ADMRLS TR3_UPD", AdminRoleTestData.AROLES_TR3_UPD, true );

        // The test itself
        DelegatedMgrImplTest.searchAdminRoles( "SRCH-ADMRLS TR1",
            TestUtils.getSrchValue( RoleTestData.getName( AdminRoleTestData.AROLES_TR1[0] ) ),
            AdminRoleTestData.AROLES_TR1 );
        DelegatedMgrImplTest.searchAdminRoles( "SRCH-ADMRLS TR2",
            TestUtils.getSrchValue( RoleTestData.getName( AdminRoleTestData.AROLES_TR2[0] ) ),
            AdminRoleTestData.AROLES_TR2 );
        DelegatedMgrImplTest.searchAdminRoles( "SRCH-ADMRLS TR3",
            TestUtils.getSrchValue( RoleTestData.getName( AdminRoleTestData.AROLES_TR3_UPD[0] ) ),
            AdminRoleTestData.AROLES_TR3_UPD );
    }


    // ReviewMgr RBAC:
    @Test
    public void testReadRole()
    {
        // Add the needed data for this test 
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR4", RoleTestData.ROLES_TR4 );
        AdminMgrImplTest.updateRoles( "UPD-RLS TR4_UPD", RoleTestData.ROLES_TR4_UPD );

        // The test itself
        // public Role readRole(Role role)
        ReviewMgrImplTest.readRoles( "RD-RLS TR1", RoleTestData.ROLES_TR1 );
        ReviewMgrImplTest.readRoles( "RD-RLS TR2", RoleTestData.ROLES_TR2 );
        ReviewMgrImplTest.readRoles( "RD-RLS TR3", RoleTestData.ROLES_TR3 );
        ReviewMgrImplTest.readRoles( "RD-RLS TR4_UPD", RoleTestData.ROLES_TR4_UPD );
    }


    /**
     * RMT06
     *
     * @throws org.apache.directory.fortress.core.SecurityException
     */
    @Test
    public void testFindRoles()
    {
        // Add the needed data for this test 
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR4", RoleTestData.ROLES_TR4 );
        AdminMgrImplTest.updateRoles( "UPD-RLS TR4_UPD", RoleTestData.ROLES_TR4_UPD );

        // The test itself
        // public List<Role> findRoles(String searchVal)
        ReviewMgrImplTest.searchRoles( "SRCH-RLS TR1",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.ROLES_TR1[0] ) ),
            RoleTestData.ROLES_TR1 );
        ReviewMgrImplTest.searchRoles( "SRCH-RLS TR2",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.ROLES_TR2[0] ) ),
            RoleTestData.ROLES_TR2 );
        ReviewMgrImplTest.searchRoles( "SRCH-RLS TR3",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.ROLES_TR3[0] ) ),
            RoleTestData.ROLES_TR3 );
        ReviewMgrImplTest.searchRoles( "SRCH-RLS TR4_UPD",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.ROLES_TR4[0] ) ),
            RoleTestData.ROLES_TR4_UPD );
    }


    @Test
    public void testFindRoleNms()
    {
        // Add the needed data for this test 
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR4", RoleTestData.ROLES_TR4 );
        AdminMgrImplTest.updateRoles( "UPD-RLS TR4_UPD", RoleTestData.ROLES_TR4_UPD );

        // The test itself
        // public List<String> findRoles(String searchVal, int limit)
        ReviewMgrImplTest.searchRolesNms( "SRCH-RLNMS TR1",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.ROLES_TR1[0] ) ),
            RoleTestData.ROLES_TR1 );
        ReviewMgrImplTest.searchRolesNms( "SRCH-RLNMS TR2",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.ROLES_TR2[0] ) ),
            RoleTestData.ROLES_TR2 );
        ReviewMgrImplTest.searchRolesNms( "SRCH-RLNMS TR3",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.ROLES_TR3[0] ) ),
            RoleTestData.ROLES_TR3 );
        ReviewMgrImplTest.searchRolesNms( "SRCH-RLNMS TR4_UPD",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.ROLES_TR4[0] ) ), RoleTestData.ROLES_TR4_UPD );
    }


    @Test
    public void testReadUser()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );
        AdminMgrImplTest.addUsers( "ADD-USRS TU2", UserTestData.USERS_TU2, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU5", UserTestData.USERS_TU5, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU8_SSD", UserTestData.USERS_TU8_SSD, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU12_DSD", UserTestData.USERS_TU12_DSD, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU13_DSD_HIER", UserTestData.USERS_TU13_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU14_DSD_HIER", UserTestData.USERS_TU14_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU15_DSD_HIER", UserTestData.USERS_TU15_DSD_HIER, true );

        // The test itself
        // public User readUser(User user)
        ReviewMgrImplTest.readUsers( "READ-USRS TU1_UPD", UserTestData.USERS_TU1_UPD );
        ReviewMgrImplTest.readUsers( "READ-USRS TU3", UserTestData.USERS_TU3 );
        ReviewMgrImplTest.readUsers( "READ-USRS TU4", UserTestData.USERS_TU4 );
        ReviewMgrImplTest.readUsers( "READ-USRS TU5", UserTestData.USERS_TU5 );
        ReviewMgrImplTest.readUsers( "READ-USRS TU8", UserTestData.USERS_TU8_SSD );
        ReviewMgrImplTest.readUsers( "READ-USRS TU9", UserTestData.USERS_TU9_SSD_HIER );
        ReviewMgrImplTest.readUsers( "READ-USRS TU10", UserTestData.USERS_TU10_SSD_HIER );
        ReviewMgrImplTest.readUsers( "READ-USRS TU11", UserTestData.USERS_TU11_SSD_HIER );
        ReviewMgrImplTest.readUsers( "READ-USRS TU12", UserTestData.USERS_TU12_DSD );
        ReviewMgrImplTest.readUsers( "READ-USRS TU13", UserTestData.USERS_TU13_DSD_HIER );
        ReviewMgrImplTest.readUsers( "READ-USRS TU14", UserTestData.USERS_TU14_DSD_HIER );
        ReviewMgrImplTest.readUsers( "READ-USRS TU15", UserTestData.USERS_TU15_DSD_HIER );
    }


    @Test
    public void testFindUsers()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU5", UserTestData.USERS_TU5, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU8_SSD", UserTestData.USERS_TU8_SSD, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU12_DSD", UserTestData.USERS_TU12_DSD, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU13_DSD_HIER", UserTestData.USERS_TU13_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU14_DSD_HIER", UserTestData.USERS_TU14_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU15_DSD_HIER", UserTestData.USERS_TU15_DSD_HIER, true );

        // The test itself
        // public List<User> findUsers(User user)
        ReviewMgrImplTest.searchUsers( "SRCH-USRS TU1_UPD",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU1[0] ) ), UserTestData.USERS_TU1_UPD );
        ReviewMgrImplTest.searchUsers( "SRCH-USRS TU3",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU3[0] ) ),
            UserTestData.USERS_TU3 );
        ReviewMgrImplTest.searchUsers( "SRCH-USRS TU4",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU4[0] ) ),
            UserTestData.USERS_TU4 );
        ReviewMgrImplTest.searchUsers( "SRCH-USRS TU5",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU5[0] ) ),
            UserTestData.USERS_TU5 );
        ReviewMgrImplTest.searchUsers( "SRCH-USRS TU8",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU8_SSD[0] ) ),
            UserTestData.USERS_TU8_SSD );
        ReviewMgrImplTest.searchUsers( "SRCH-USRS TU9",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU9_SSD_HIER[0] ) ),
            UserTestData.USERS_TU9_SSD_HIER );
        ReviewMgrImplTest.searchUsers( "SRCH-USRS TU10",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU10_SSD_HIER[0] ) ),
            UserTestData.USERS_TU10_SSD_HIER );
        ReviewMgrImplTest.searchUsers( "SRCH-USRS TU11",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU11_SSD_HIER[0] ) ),
            UserTestData.USERS_TU11_SSD_HIER );
        ReviewMgrImplTest.searchUsers( "SRCH-USRS TU12",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU12_DSD[0] ) ),
            UserTestData.USERS_TU12_DSD );
        ReviewMgrImplTest.searchUsers( "SRCH-USRS TU13",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU13_DSD_HIER[0] ) ),
            UserTestData.USERS_TU13_DSD_HIER );
        ReviewMgrImplTest.searchUsers( "SRCH-USRS TU14",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU14_DSD_HIER[0] ) ),
            UserTestData.USERS_TU14_DSD_HIER );
        ReviewMgrImplTest.searchUsers( "SRCH-USRS TU15",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU15_DSD_HIER[0] ) ),
            UserTestData.USERS_TU15_DSD_HIER );
    }


    @Test
    public void testFindUserIds()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );

        // The test itself
        // public List<String> findUsers(User user, int limit)
        ReviewMgrImplTest.searchUserIds( "SRCH-USRIDS TU1_UPD",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU1[0] ) ), UserTestData.USERS_TU1_UPD );
        ReviewMgrImplTest.searchUserIds( "SRCH-USRIDS TU3",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU3[0] ) ), UserTestData.USERS_TU3 );
        ReviewMgrImplTest.searchUserIds( "SRCH-USRIDS TU4",
            TestUtils.getSrchValue( UserTestData.getUserId( UserTestData.USERS_TU4[0] ) ), UserTestData.USERS_TU4 );
    }


    @Test
    public void testAssignedRoles()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );

        // from testAssignUser
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );

        // from testAddPermissionObj
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );

        // from testAddPermissionOp
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );

        // from testGrantPermissionUser
        AdminMgrImplTest.addUserGrants( "GRNT-PRMS TU1 TOB1 TOP1", UserTestData.USERS_TU1, PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1 );

        // The test itself
        // public List<UserRole> assignedRoles(User userId)
        ReviewMgrImplTest.assignedRoles( "ASGN-RLS TU1_UPD TR1", UserTestData.USERS_TU1_UPD, RoleTestData.ROLES_TR1 );
        //assignedRoles("ASGN-RLS TU3 TR2", UserTestData.USERS_TU3, RoleTestData.ROLES_TR2);
        ReviewMgrImplTest.assignedRoles( "ASGN-RLS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2 );
        ReviewMgrImplTest.assignedRoles( "ASGN-RLS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3 );
    }


    @Test
    public void testAssignedRoleNms()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );

        // from testAssignUser
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );

        // The test itself
        //  public List<String> assignedRoles(String userId)
        ReviewMgrImplTest.assignedRoleNms( "ASGN-RLS TU1_UPD TR1", UserTestData.USERS_TU1_UPD, RoleTestData.ROLES_TR1 );
        //assignedRoles("ASGN-RLS TU3 TR2", UserTestData.USERS_TU3, RoleTestData.ROLES_TR2);
        ReviewMgrImplTest.assignedRoleNms( "ASGN-RLS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2 );
        ReviewMgrImplTest.assignedRoleNms( "ASGN-RLS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3 );
    }


    @Test
    public void testAuthorizedRoles()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR6-DESC", RoleTestData.ROLES_TR6_DESC );
        AdminMgrImplTest.addRoleAscendant( "ADD-RLS-TR7-ASC", RoleTestData.ROLES_TR7_ASC );
        AdminMgrImplTest.addUsers( "ADD-USRS TU18 TR6_DESC", UserTestData.USERS_TU18U_TR6_DESC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU19 TR7_ASC", UserTestData.USERS_TU19U_TR7_ASC, true );

        // The test itself
        // public Set<String> authorizedRoles(User user)
        ReviewMgrImplTest.authorizedRoles( "AUTHZ-RLS TU18 TR6 DESC", UserTestData.USERS_TU18U_TR6_DESC );
        ReviewMgrImplTest.authorizedRoles( "AUTHZ-RLS TU19 TR7 ASC", UserTestData.USERS_TU19U_TR7_ASC );
    }


    @Test
    public void testAuthorizedUsers()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );

        // from testAdminMgrUpdateUser
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );

        // from testAssignUser
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );

        // The test itself
        // public List<User> authorizedUsers(Role role)
        ReviewMgrImplTest.authorizedUsers( "ATHZ-USRS TR1 TU1_UPD", RoleTestData.ROLES_TR1, UserTestData.USERS_TU1_UPD );
        ReviewMgrImplTest.authorizedUsers( "ATHZ-USRS TR2 TU4", RoleTestData.ROLES_TR2, UserTestData.USERS_TU4 );
        ReviewMgrImplTest.authorizedUsers( "ATHZ-USRS TR3 TU3", RoleTestData.ROLES_TR3, UserTestData.USERS_TU3 );
    }


    @Test
    public void testAuthorizedUserIds()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );

        // from testAdminMgrUpdateUser
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );

        // from testAssignUser
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );

        // The test itself
        //  public List<String> authorizedUsers(Role role, int limit)
        ReviewMgrImplTest.assignedUserIds( "ATHZ-USRS TR1 TU1_UPD", RoleTestData.ROLES_TR1, UserTestData.USERS_TU1_UPD );
        ReviewMgrImplTest.assignedUserIds( "ATHZ-USRS TR2 TU4", RoleTestData.ROLES_TR2, UserTestData.USERS_TU4 );
        ReviewMgrImplTest.assignedUserIds( "ATHZ-USRS TR3 TU3", RoleTestData.ROLES_TR3, UserTestData.USERS_TU3 );
    }


    @Test
    public void testAuthorizedUsersHier()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );

        // from testAdminMgrAddRoleDescendant
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR6-DESC", RoleTestData.ROLES_TR6_DESC );

        // from testAdminMgrAddRoleAscendants
        AdminMgrImplTest.addRoleAscendant( "ADD-RLS-TR7-ASC", RoleTestData.ROLES_TR7_ASC );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU18 TR6_DESC", UserTestData.USERS_TU18U_TR6_DESC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU19 TR7_ASC", UserTestData.USERS_TU19U_TR7_ASC, true );

        // The test itself
        // public List<User> authorizedUsers(Role role)
        ReviewMgrImplTest.authorizedUsersHier( "ATHZ-USRS-HIER TR6 TU18", RoleTestData.TR6_AUTHORIZED_USERS );
        ReviewMgrImplTest.authorizedUsersHier( "ATHZ-USRS-HIER TR7 TU19", RoleTestData.TR7_AUTHORIZED_USERS );
    }


    @Test
    public void testReadPermissionObj()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // The test itself
        //     public Permission addPermObj(Permission pOp)
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB2", PermTestData.OBJS_TOB2, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB3", PermTestData.OBJS_TOB3, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB4", PermTestData.OBJS_TOB4, true, false );
        AdminMgrImplTest.updatePermObjs( "UPD-OBS TOB4_UPD", PermTestData.OBJS_TOB4_UPD, true );

        // The test itself
        //    public Permission readPermission(Permission permOp)
        ReviewMgrImplTest.readPermissionObjs( "RD-PRM-OBJS TOB1", PermTestData.OBJS_TOB1 );
        ReviewMgrImplTest.readPermissionObjs( "RD-PRM-OBJS TOB2", PermTestData.OBJS_TOB2 );
        ReviewMgrImplTest.readPermissionObjs( "RD-PRM-OBJS TOB3", PermTestData.OBJS_TOB3 );
        ReviewMgrImplTest.readPermissionObjs( "RD-PRM-OBJS TOB4_UPD", PermTestData.OBJS_TOB4_UPD );
    }


    @Test
    public void testFindPermissionObjs()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // The test itself
        //     public Permission addPermObj(Permission pOp)
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB2", PermTestData.OBJS_TOB2, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB3", PermTestData.OBJS_TOB3, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB4", PermTestData.OBJS_TOB4, true, false );
        AdminMgrImplTest.updatePermObjs( "UPD-OBS TOB4_UPD", PermTestData.OBJS_TOB4_UPD, true );

        // The test itself
        // public List<Permission> findPermissions(Permission permOp)
        ReviewMgrImplTest.searchPermissionObjs( "FND-PRM-OBJS TOB1",
            TestUtils.getSrchValue( PermTestData.getName( PermTestData.OBJS_TOB1[0] ) ), PermTestData.OBJS_TOB1 );
        ReviewMgrImplTest.searchPermissionObjs( "FND-PRM-OBJS TOB2",
            TestUtils.getSrchValue( PermTestData.getName( PermTestData.OBJS_TOB2[0] ) ), PermTestData.OBJS_TOB2 );
        ReviewMgrImplTest.searchPermissionObjs( "FND-PRM-OBJS TOB3",
            TestUtils.getSrchValue( PermTestData.getName( PermTestData.OBJS_TOB3[0] ) ), PermTestData.OBJS_TOB3 );
        ReviewMgrImplTest
            .searchPermissionObjs( "FND-PRM-OBJS TOB4_UPD",
                TestUtils.getSrchValue( PermTestData.getName( PermTestData.OBJS_TOB4_UPD[0] ) ),
                PermTestData.OBJS_TOB4_UPD );
    }


    @Test
    public void testReadPermissionOp()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddPermissionObj
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB2", PermTestData.OBJS_TOB2, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB3", PermTestData.OBJS_TOB3, true, false );

        // from testAddPermissionOp
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB2 TOP2", PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB3 TOP3", PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true, false );

        // from testUpdatePermissionOp
        AdminMgrImplTest.updatePermOps( "UPD-OPS TOB1 TOP1_UPD", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1_UPD,
            true );

        // The test itself
        //    public Permission readPermission(Permission permOp)
        ReviewMgrImplTest.readPermissionOps( "RD-PRM-OPS TOB1 OPS_TOP1_UPD", PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1_UPD );
        ReviewMgrImplTest.readPermissionOps( "RD-PRM-OPS TOB1 TOP2", PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2 );
        ReviewMgrImplTest.readPermissionOps( "RD-PRM-OPS TOB1 TOP3", PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3 );
    }


    @Test
    public void testFindPermissionOps()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddPermissionObj
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB2", PermTestData.OBJS_TOB2, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB3", PermTestData.OBJS_TOB3, true, false );

        // from testAddPermissionOp
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB2 TOP2", PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB3 TOP3", PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true, false );

        // from testUpdatePermissionOp
        AdminMgrImplTest.updatePermOps( "UPD-OPS TOB1 TOP1_UPD", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1_UPD,
            true );

        // The test itself
        // public List<Permission> findPermissions(Permission permOp)
        ReviewMgrImplTest.searchPermissionOps( "FND-PRM-OPS TOB1 OPS_TOP1_UPD",
            TestUtils.getSrchValue( PermTestData.getName( PermTestData.OPS_TOP1_UPD[0] ) ), PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1_UPD );
        ReviewMgrImplTest.searchPermissionOps( "FND-PRM-OPS TOB2 TOP2",
            TestUtils.getSrchValue( PermTestData.getName( PermTestData.OPS_TOP2[0] ) ), PermTestData.OBJS_TOB2,
            PermTestData.OPS_TOP2 );
        ReviewMgrImplTest.searchPermissionOps( "FND-PRM-OPS TOB3 TOP3",
            TestUtils.getSrchValue( PermTestData.getName( PermTestData.OPS_TOP3[0] ) ), PermTestData.OBJS_TOB3,
            PermTestData.OPS_TOP3 );
    }


    @Test
    public void testRolePermissions()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );

        // from testAddPermissionObj
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );

        // from testAddPermissionOp
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );

        // from testUpdatePermissionOp
        AdminMgrImplTest.updatePermOps( "UPD-OPS TOB1 TOP1_UPD", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1_UPD,
            true );

        // from testGrantPermissionRole
        AdminMgrImplTest.addRoleGrants( "GRNT-PRMS TR1 TOB1 TOP1", RoleTestData.ROLES_TR1, PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1, true, false );

        // The test itself
        // public List<Permission> rolePermissions(Role role)
        ReviewMgrImplTest.rolePermissions( "ATHRZ-RLE-PRMS TR1 TOB1 TOP1_UPD", RoleTestData.ROLES_TR1,
            PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1_UPD );
    }


    @Test
    public void testPermissionRoles()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );

        // from testAddPermissionObj
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );

        // from testAddPermissionOp
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );

        // from testGrantPermissionRole
        AdminMgrImplTest.addRoleGrants( "GRNT-PRMS TR1 TOB1 TOP1", RoleTestData.ROLES_TR1, PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1, true, false );

        // The test itself
        // public List<Role> permissionRoles(Permission perm)
        ReviewMgrImplTest.permissionRoles( "PRM-RLS TOB1 TOP1 TR1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1,
            RoleTestData.ROLES_TR1 );
    }


    @Test
    public void testAuthorizedPermissionRoles()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B );

        // from testAdminMgrAddRoleInheritance
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU20 TR5_HIER", UserTestData.USERS_TU20U_TR5B, true );

        // from testAddPermissionObj
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB6", PermTestData.OBJS_TOB6, true, false );

        // from testAddPermissionOp
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB6 TOP5", PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5, true, false );

        // from testGrantPermissionRole
        AdminMgrImplTest.addRoleGrantsHB( "GRNT-PRMS_HB USERS TU20 ROLES_TR5B TOB6 TOP5",
            UserTestData.USERS_TU20U_TR5B,
            RoleTestData.ROLES_TR5B, PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5 );

        // The test itself
        // public Set<String> authorizedPermissionRoles(Permission perm)
        ReviewMgrImplTest.authorizedPermissionRoles( "AUTHZ PRM-RLES TOB6 TOP5 TR5B", PermTestData.OBJS_TOB6,
            PermTestData.OPS_TOP5,
            RoleTestData.ROLES_TR5B );
    }


    @Test
    public void testPermissionUsers()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );

        // from testAdminMgrUpdateUser
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );

        // from testAssignUser
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );

        // from testAddPermissionObj
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );

        // from testAddPermissionOp
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );
        //
        // from testGrantPermissionUser
        AdminMgrImplTest.addUserGrants( "GRNT-PRMS TU1 TOB1 TOP1", UserTestData.USERS_TU1, PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1 );

        // The test itself
        //  public List<User> permissionUsers(Permission perm)
        ReviewMgrImplTest.permissionUsers( "PRM-USRS TOB1 TOP1 TU1_UPD", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1,
            UserTestData.USERS_TU1_UPD );
    }


    @Test
    public void testAuthorizedPermissionUsers()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B );

        // from testAdminMgrAddRoleInheritance
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU20 TR5_HIER", UserTestData.USERS_TU20U_TR5B, true );

        // from testAssignUser
        AdminMgrImplTest.assignUsersH( "ASGN-USRS_H TU20 TR5B HIER", UserTestData.USERS_TU20U_TR5B,
            RoleTestData.ROLES_TR5B, true );

        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB6", PermTestData.OBJS_TOB6, true, false );

        // from testAddPermissionOp
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB6 TOP5", PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5, true, false );

        // from testGrantPermissionRole
        AdminMgrImplTest.addRoleGrantsHB( "GRNT-PRMS_HB USERS TU20 ROLES_TR5B TOB6 TOP5",
            UserTestData.USERS_TU20U_TR5B,
            RoleTestData.ROLES_TR5B, PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5 );

        // The test itself
        // public Set<String> authorizedPermissionUsers(Permission perm)
        ReviewMgrImplTest.authorizedPermissionUsers( "AUTHZ PRM-USRS TOB6 TOP5 TU20", PermTestData.OBJS_TOB6,
            PermTestData.OPS_TOP5,
            UserTestData.USERS_TU20U_TR5B );
    }


    @Test
    public void testUserPermissions()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B );

        // from testAdminMgrAddRoleInheritance
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU2", UserTestData.USERS_TU2, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );

        // from testAssignUser
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );

        // from testAddPermissionObj
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB2", PermTestData.OBJS_TOB2, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB3", PermTestData.OBJS_TOB3, true, false );

        // from testAddPermissionOp
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB2 TOP2", PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB3 TOP3", PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true, false );

        // from testUpdatePermissionOp
        AdminMgrImplTest.updatePermOps( "UPD-OPS TOB1 TOP1_UPD", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1_UPD,
            true );

        // from testGrantPermissionRole
        AdminMgrImplTest.addRoleGrants( "GRNT-PRMS TR1 TOB1 TOP1", RoleTestData.ROLES_TR1, PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1, true, false );
        AdminMgrImplTest.addRoleGrants( "GRNT-PRMS TR2 TOB2 TOP2", RoleTestData.ROLES_TR2, PermTestData.OBJS_TOB2,
            PermTestData.OPS_TOP2, true, false );
        AdminMgrImplTest.addRoleGrants( "GRNT-PRMS TR3 TOB3 TOP3", RoleTestData.ROLES_TR3, PermTestData.OBJS_TOB3,
            PermTestData.OPS_TOP3, true, false );

        // from testGrantPermissionUser
        AdminMgrImplTest.addUserGrants( "GRNT-PRMS TU1 TOB1 TOP1", UserTestData.USERS_TU1, PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1 );

        // The test itself
        // public List <Permission> userPermissions(User user)
        ReviewMgrImplTest.userPermissions( "USR-PRMS TU1_UPD TOB1 TOP1_UPD", UserTestData.USERS_TU1_UPD,
            PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1_UPD );
        ReviewMgrImplTest.userPermissions( "USR-PRMS TU3 TOB3 TOP3", UserTestData.USERS_TU3, PermTestData.OBJS_TOB3,
            PermTestData.OPS_TOP3 );
        ReviewMgrImplTest.userPermissions( "USR-PRMS TU4 TOB2 TOP2", UserTestData.USERS_TU4, PermTestData.OBJS_TOB2,
            PermTestData.OPS_TOP2 );
    }


    /***********************************************************/
    /* 4. Security Checks                                      */
    /***********************************************************/
    // DelAccessMgr ARABC:
    @Test
    public void testCheckAccess()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR2", AdminRoleTestData.AROLES_TR2, true );

        // from testAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU16A_ARBAC", UserTestData.USERS_TU17A_ARBAC, true );

        // from testAddPermission
        AdminMgrImplTest.addPermObjs( "ADD-OBS ARBAC1", PermTestData.ARBAC_OBJS_1, true, false );
        AdminMgrImplTest
            .addPermOps( "ADD-OPS ARBAC1", PermTestData.ARBAC_OBJS_1, PermTestData.ARBAC_OPS_1, true, false );

        // from testAssignAdminUser
        DelegatedMgrImplTest.assignAdminUserRole( "ASGN-USR TU17A TR2", UserTestData.USERS_TU17A_ARBAC,
            AdminRoleTestData.AROLES_TR2, true );

        // from testGrantPermissionRole
        AdminMgrImplTest.addRoleGrants( "GRNT-APRMS ARTR2 AROBJ1 AROPS1", AdminRoleTestData.AROLES_TR2,
            PermTestData.ARBAC_OBJS_1, PermTestData.ARBAC_OPS_1, true, false );

        // from testAdminMgrAddRoleAscendants
        AdminMgrImplTest.addRoleAscendant( "ADD-RLS-TR7-ASC", RoleTestData.ROLES_TR7_ASC );

        // The test itself
        // public boolean checkAccess(String object, String operation, Session session)
        DelegatedMgrImplTest.checkAccess( "CHCK-ACS TU1_UPD TO1 TOP1 ", UserTestData.USERS_TU17A_ARBAC,
            PermTestData.ARBAC_OBJS_1,
            PermTestData.ARBAC_OPS_1, PermTestData.ARBAC_OBJ2, PermTestData.ARBAC_OPS_2 );
    }


    @Test
    public void testCanAssignUser()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        //        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddOrgInheritance
        DelegatedMgrImplTest.addInheritedOrgUnits( "ADD-INHERIT ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR2", AdminRoleTestData.AROLES_TR2, true );

        // from testAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU16A_ARBAC", UserTestData.USERS_TU17A_ARBAC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU16U_ARBAC", UserTestData.USERS_TU17U_ARBAC, true );

        // from testAssignAdminUser
        DelegatedMgrImplTest.assignAdminUserRole( "ASGN-USR TU17A TR2", UserTestData.USERS_TU17A_ARBAC,
            AdminRoleTestData.AROLES_TR2, true );

        // The test itself
        //canAssignUsers("CAN-ASGN-USRS TU1 TR1", UserTestData.USERS_TU16_ARBAC, UserTestData.USERS_TU16B_ARBAC, RoleTestData.ROLES_TR14_ARBAC);
        DelegatedMgrImplTest.canAssignUsers( "CAN-ASGN-USRS URA_T1 TU17A TU17U TR15", ASSIGN_OP.ASSIGN,
            URATestData.URA_T1,
            UserTestData.USERS_TU17A_ARBAC, UserTestData.USERS_TU17U_ARBAC, RoleTestData.ROLES_TR15_ARBAC );
    }


    @Test
    public void testCanDeassignUser()
    {
        // Add the needed data for this test
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        //        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddOrgInheritance
        DelegatedMgrImplTest.addInheritedOrgUnits( "ADD-INHERIT ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR2", AdminRoleTestData.AROLES_TR2, true );

        // from testAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU16A_ARBAC", UserTestData.USERS_TU17A_ARBAC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU16U_ARBAC", UserTestData.USERS_TU17U_ARBAC, true );

        // from testAssignAdminUser
        DelegatedMgrImplTest.assignAdminUserRole( "ASGN-USR TU17A TR2", UserTestData.USERS_TU17A_ARBAC,
            AdminRoleTestData.AROLES_TR2, true );

        // The test itself
        DelegatedMgrImplTest.canAssignUsers( "CAN-DEASGN-USRS URA_T1 TU17A TU17U TR15", ASSIGN_OP.DEASSIGN,
            URATestData.URA_T1,
            UserTestData.USERS_TU17A_ARBAC, UserTestData.USERS_TU17U_ARBAC, RoleTestData.ROLES_TR15_ARBAC );
    }


    @Test
    public void testCanGrantPerm()
    {
        // Add the needed data for this test
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );

        // from testAddOrgInheritance
        DelegatedMgrImplTest.addInheritedOrgUnits( "ADD-INHERIT ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR2", AdminRoleTestData.AROLES_TR2, true );

        // from testAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU16A_ARBAC", UserTestData.USERS_TU17A_ARBAC, true );

        // from testAddPermission
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB5", PermTestData.OBJS_TOB5, true, false );

        // from testAssignAdminUser
        DelegatedMgrImplTest.assignAdminUserRole( "ASGN-USR TU17A TR2", UserTestData.USERS_TU17A_ARBAC,
            AdminRoleTestData.AROLES_TR2, true );

        // The test itself
        DelegatedMgrImplTest.canGrantPerms( "CAN-GRNT-PRMS PRA_T1 TU17A TOB5 TR15", GRANT_OP.GRANT, PRATestData.PRA_T1,
            UserTestData.USERS_TU17A_ARBAC, PermTestData.OBJS_TOB5, RoleTestData.ROLES_TR15_ARBAC );
    }


    @Test
    public void testCanRevokePerm()
    {
        // Add the needed data for this test
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );

        // from testAddOrgInheritance
        DelegatedMgrImplTest.addInheritedOrgUnits( "ADD-INHERIT ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC );
        DelegatedMgrImplTest.addAdminRoles( "ADD-ADMRLS TR2", AdminRoleTestData.AROLES_TR2, true );

        // from testAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU16A_ARBAC", UserTestData.USERS_TU17A_ARBAC, true );

        // from testAddPermission
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB5", PermTestData.OBJS_TOB5, true, false );

        // from testAssignAdminUser
        DelegatedMgrImplTest.assignAdminUserRole( "ASGN-USR TU17A TR2", UserTestData.USERS_TU17A_ARBAC,
            AdminRoleTestData.AROLES_TR2, true );

        // The test itself
        DelegatedMgrImplTest.canGrantPerms( "CAN-RVKE-PRMS PRA_T1 TU17A TOB5 TR15", GRANT_OP.REVOKE,
            PRATestData.PRA_T1,
            UserTestData.USERS_TU17A_ARBAC, PermTestData.OBJS_TOB5, RoleTestData.ROLES_TR15_ARBAC );
    }


    // AccessMgr RBAC:
    @Test
    public void testGetUserId()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );

        // The test itself
        // public String getUserId(Session, session)
        AccessMgrImplTest.getUsers( "GET-USRIDS TU1_UPD", UserTestData.USERS_TU1_UPD );
        AccessMgrImplTest.getUsers( "GET-USRIDS TU3", UserTestData.USERS_TU3 );
        AccessMgrImplTest.getUsers( "GET-USRIDS TU4", UserTestData.USERS_TU4 );
    }


    @Test
    public void testGetUser()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );

        // The test itself
        // public User getUser(Session, session)
        AccessMgrImplTest.getUsers( "GET-USRS TU1_UPD", UserTestData.USERS_TU1_UPD );
        AccessMgrImplTest.getUsers( "GET-USRS TU3", UserTestData.USERS_TU3 );
        AccessMgrImplTest.getUsers( "GET-USRS TU4", UserTestData.USERS_TU4 );
    }


    /**
    *
    */
    @Test
    public void testCreateSession()
    {
        // Add the needed data for this test 
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );

        // The test itself
        // public Session createSession(User user, boolean isTrusted)
        AccessMgrImplTest
            .createSessions( "CREATE-SESS TU1_UPD TR1", UserTestData.USERS_TU1_UPD, RoleTestData.ROLES_TR1 );
        AccessMgrImplTest.createSessions( "CREATE-SESS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3 );
        AccessMgrImplTest.createSessions( "CREATE-SESS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2 );
    }


    /**
     *
     */
    @Test
    public void testCreateSessionTrusted()
    {
        // Add the needed data for this test 
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );

        // The test itself
        // public Session createSession(User user, boolean isTrusted)
        AccessMgrImplTest.createSessionsTrusted( "CR-SESS-TRST TU1_UPD TR1", UserTestData.USERS_TU1_UPD,
            RoleTestData.ROLES_TR1 );
        AccessMgrImplTest
            .createSessionsTrusted( "CR-SESS-TRST TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3 );
        AccessMgrImplTest
            .createSessionsTrusted( "CR-SESS-TRST TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2 );
    }


    /**
     *
     */
    @Test
    public void testCreateSessionHier()
    {
        // Add the needed data for this test 
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR6-DESC", RoleTestData.ROLES_TR6_DESC );
        AdminMgrImplTest.addRoleAscendant( "ADD-RLS-TR7-ASC", RoleTestData.ROLES_TR7_ASC );

        AdminMgrImplTest.addUsers( "ADD-USRS TU18 TR6_DESC", UserTestData.USERS_TU18U_TR6_DESC, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU19 TR7_ASC", UserTestData.USERS_TU19U_TR7_ASC, true );

        // The test itself
        // public Session createSession(User user, boolean isTrusted)
        AccessMgrImplTest.createSessionsHier( "CREATE-SESS-HIER TU18 TR6 DESC", UserTestData.USERS_TU18U_TR6_DESC );
        AccessMgrImplTest.createSessionsHier( "CREATE-SESS-HIER TU19U TR7 ASC", UserTestData.USERS_TU19U_TR7_ASC );
    }


    @Test
    public void testCreateSessionsDSD()
    {
        // from testAddOrgUnit
        //        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_TO1", OrgUnitTestData.ORGS_TO1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR8_DSD", RoleTestData.ROLES_TR8_DSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR9_DSD", RoleTestData.ROLES_TR9_DSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR17_DSD_BRUNO", RoleTestData.ROLES_TR17_DSD_BRUNO );

        // from testAdminMgrAddRoleDescendant
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR11-DESC-DSD", RoleTestData.ROLES_TR11_DESC_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR12-DESC-DSD", RoleTestData.ROLES_TR12_DESC_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR13-DESC-DSD", RoleTestData.ROLES_TR13_DESC_DSD );

        // from testAdminMgrCreateDsdSet
        AdminMgrImplTest.createDsdSet( "ADD-DSD T1", RoleTestData.DSD_T1 );
        AdminMgrImplTest.createDsdSet( "ADD-DSD T4", RoleTestData.DSD_T4 );
        AdminMgrImplTest.createDsdSet( "ADD-DSD T5", RoleTestData.DSD_T5 );
        AdminMgrImplTest.createDsdSet( "ADD-DSD T6", RoleTestData.DSD_T6 );
        AdminMgrImplTest.createDsdSet( "ADD-DSD T8 BRUNO", RoleTestData.DSD_T8_BRUNO );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU12_DSD", UserTestData.USERS_TU12_DSD, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU13_DSD_HIER", UserTestData.USERS_TU13_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU14_DSD_HIER", UserTestData.USERS_TU14_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU15_DSD_HIER", UserTestData.USERS_TU15_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU21 DSD_BRUNO", UserTestData.USERS_TU21_DSD_BRUNO, true );

        // from testAssignUser
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT1 TU12 DSD_T1", UserTestData.USERS_TU12_DSD,
            RoleTestData.DSD_T1 );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT4B TU13 DSD_T4_B", UserTestData.USERS_TU13_DSD_HIER,
            RoleTestData.DSD_T4_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT5B TU14 DSD_T5_B", UserTestData.USERS_TU14_DSD_HIER,
            RoleTestData.DSD_T5_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT6B TU15 DSD_T6_B", UserTestData.USERS_TU15_DSD_HIER,
            RoleTestData.DSD_T6_B );

        // The test itself
        // public Session createSession(User user, boolean isTrusted)
        AccessMgrImplTest.createSessionsDSD( "CR-SESS-DSD TU12 DSD_T1", UserTestData.USERS_TU12_DSD,
            RoleTestData.DSD_T1 );
        AccessMgrImplTest.createSessionsDSD( "CR-SESS-DSD TU13 DSD_T4_B", UserTestData.USERS_TU13_DSD_HIER,
            RoleTestData.DSD_T4_B );
        AccessMgrImplTest.createSessionsDSD( "CR-SESS-DSD TU14 DSD_T5_B", UserTestData.USERS_TU14_DSD_HIER,
            RoleTestData.DSD_T5_B );
        AccessMgrImplTest.createSessionsDSD( "CR-SESS-DSD TU15 DSD_T6_C", UserTestData.USERS_TU15_DSD_HIER,
            RoleTestData.DSD_T6_C );
        AccessMgrImplTest.createSessionsDSD( "CR-SESS-DSD TU21 DSD_T8_BRUNO", UserTestData.USERS_TU21_DSD_BRUNO,
            RoleTestData.DSD_T8_BRUNO );

        // Running it again to make sure the caching is working good:
        AccessMgrImplTest.createSessionsDSD( "CR-SESS-DSD TU12 DSD_T1", UserTestData.USERS_TU12_DSD,
            RoleTestData.DSD_T1 );
        AccessMgrImplTest.createSessionsDSD( "CR-SESS-DSD TU13 DSD_T4_B", UserTestData.USERS_TU13_DSD_HIER,
            RoleTestData.DSD_T4_B );
        AccessMgrImplTest.createSessionsDSD( "CR-SESS-DSD TU14 DSD_T5_B", UserTestData.USERS_TU14_DSD_HIER,
            RoleTestData.DSD_T5_B );
        AccessMgrImplTest.createSessionsDSD( "CR-SESS-DSD TU15 DSD_T6_C", UserTestData.USERS_TU15_DSD_HIER,
            RoleTestData.DSD_T6_C );
        AccessMgrImplTest.createSessionsDSD( "CR-SESS-DSD TU21 DSD_T8_BRUNO", UserTestData.USERS_TU21_DSD_BRUNO,
            RoleTestData.DSD_T8_BRUNO );
    }


    /**
    *
    */
    @Test
    public void testSessionRole()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );

        // from testAdminMgrUpdateUser
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );

        // from testAssignUser
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );

        // The test itself
        // public List<UserRole> sessionRoles(Session session)
        AccessMgrImplTest.sessionRoles( "SESS-RLS TU1_UPD TR1", UserTestData.USERS_TU1_UPD, RoleTestData.ROLES_TR1 );
        AccessMgrImplTest.sessionRoles( "SESS-RLS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3 );
        AccessMgrImplTest.sessionRoles( "SESS-RLS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2 );
    }


    /**
    *
    */
    @Test
    public void testAccessMgrCheckAccess()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );

        // from testAdminMgrUpdateUser
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );

        // from testAssignUser
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );

        // from testAddPermissionObj
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB2", PermTestData.OBJS_TOB2, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB3", PermTestData.OBJS_TOB3, true, false );

        // from testAddPermissionOp
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB2 TOP2", PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB3 TOP3", PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true, false );

        // from testGrantPermissionRole
        AdminMgrImplTest.addRoleGrants( "GRNT-PRMS TR1 TOB1 TOP1", RoleTestData.ROLES_TR1, PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1, true, false );
        AdminMgrImplTest.addRoleGrants( "GRNT-PRMS TR2 TOB2 TOP2", RoleTestData.ROLES_TR2, PermTestData.OBJS_TOB2,
            PermTestData.OPS_TOP2, true, false );
        AdminMgrImplTest.addRoleGrants( "GRNT-PRMS TR3 TOB3 TOP3", RoleTestData.ROLES_TR3, PermTestData.OBJS_TOB3,
            PermTestData.OPS_TOP3, true, false );

        // The test itself
        // public boolean checkAccess(String object, String operation, Session session)
        AccessMgrImplTest.checkAccess( "CHCK-ACS TU1_UPD TO1 TOP1 ", UserTestData.USERS_TU1_UPD,
            PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1, PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3 );
        AccessMgrImplTest.checkAccess( "CHCK-ACS TU3 TO3 TOP1 ", UserTestData.USERS_TU3, PermTestData.OBJS_TOB3,
            PermTestData.OPS_TOP3,
            PermTestData.OBJS_TOB2, PermTestData.OPS_TOP1 );
        AccessMgrImplTest.checkAccess( "CHCK-ACS TU4 TO4 TOP1 ", UserTestData.USERS_TU4, PermTestData.OBJS_TOB2,
            PermTestData.OPS_TOP2,
            PermTestData.OBJS_TOB2, PermTestData.OPS_TOP1 );
    }


    @Test
    public void testAddActiveRole()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR8_DSD", RoleTestData.ROLES_TR8_DSD );

        // from testAdminMgrAddRoleDescendant
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR11-DESC-DSD", RoleTestData.ROLES_TR11_DESC_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR12-DESC-DSD", RoleTestData.ROLES_TR12_DESC_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR13-DESC-DSD", RoleTestData.ROLES_TR13_DESC_DSD );

        // from testAdminMgrCreateDsdSet
        AdminMgrImplTest.createDsdSet( "ADD-DSD T1", RoleTestData.DSD_T1 );
        AdminMgrImplTest.createDsdSet( "ADD-DSD T4", RoleTestData.DSD_T4 );
        AdminMgrImplTest.createDsdSet( "ADD-DSD T5", RoleTestData.DSD_T5 );
        AdminMgrImplTest.createDsdSet( "ADD-DSD T6", RoleTestData.DSD_T6 );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU8_SSD", UserTestData.USERS_TU8_SSD, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, true );

        // from testAdminMgrUpdateUser
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );

        // from testAssignUser
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT1 TU8 DSD_T1", UserTestData.USERS_TU8_SSD, RoleTestData.DSD_T1 );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT4B TU9 DSD_T4_B", UserTestData.USERS_TU9_SSD_HIER,
            RoleTestData.DSD_T4_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT5B TU10 DSD_T5_B", UserTestData.USERS_TU10_SSD_HIER,
            RoleTestData.DSD_T5_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT6B TU11 DSD_T6_B", UserTestData.USERS_TU11_SSD_HIER,
            RoleTestData.DSD_T6_B );

        // The test itself
        // public void addActiveRole(Session session, String role)
        AccessMgrImplTest.addActiveRoles( "ADD-ACT-RLS TU1_UPD TR1 bad:TR2", UserTestData.USERS_TU1_UPD,
            RoleTestData.ROLES_TR1,
            RoleTestData.ROLES_TR2 );
        AccessMgrImplTest.addActiveRoles( "ADD-ACT-RLS TU3 TR3 bad:TR1:", UserTestData.USERS_TU3,
            RoleTestData.ROLES_TR3,
            RoleTestData.ROLES_TR1 );
        AccessMgrImplTest.addActiveRoles( "ADD-ACT-RLS TU4 TR2 bad:TR1", UserTestData.USERS_TU4,
            RoleTestData.ROLES_TR2,
            RoleTestData.ROLES_TR1 );
        AccessMgrImplTest.addActiveRolesDSD( "ADD-ACT-RLS-USRS_DSDT1 TU8 DSD_T1", UserTestData.USERS_TU8_SSD,
            RoleTestData.DSD_T1 );
        AccessMgrImplTest.addActiveRolesDSD( "ADD-ACT-RLS-USRS_DSDT4B TU9 DSD_T4_B", UserTestData.USERS_TU9_SSD_HIER,
            RoleTestData.DSD_T4_B );
        AccessMgrImplTest.addActiveRolesDSD( "ADD-ACT-RLS-USRS_DSDT5B TU10 DSD_T5_B", UserTestData.USERS_TU10_SSD_HIER,
            RoleTestData.DSD_T5_B );
        AccessMgrImplTest.addActiveRolesDSD( "ADD-ACT-RLS-USRS_DSDT6B TU11 DSD_T6_B", UserTestData.USERS_TU11_SSD_HIER,
            RoleTestData.DSD_T6_B );
    }


    @Test
    public void testDropActiveRole()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR8_DSD", RoleTestData.ROLES_TR8_DSD );

        // from testAdminMgrAddRoleDescendant
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR11-DESC-DSD", RoleTestData.ROLES_TR11_DESC_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR12-DESC-DSD", RoleTestData.ROLES_TR12_DESC_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR13-DESC-DSD", RoleTestData.ROLES_TR13_DESC_DSD );

        // from testAdminMgrCreateDsdSet
        AdminMgrImplTest.createDsdSet( "ADD-DSD T1", RoleTestData.DSD_T1 );
        AdminMgrImplTest.createDsdSet( "ADD-DSD T4", RoleTestData.DSD_T4 );
        AdminMgrImplTest.createDsdSet( "ADD-DSD T5", RoleTestData.DSD_T5 );
        AdminMgrImplTest.createDsdSet( "ADD-DSD T6", RoleTestData.DSD_T6 );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU8_SSD", UserTestData.USERS_TU8_SSD, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, true );

        // from testAdminMgrUpdateUser
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );

        // from testAssignUser
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT1 TU8 DSD_T1", UserTestData.USERS_TU8_SSD, RoleTestData.DSD_T1 );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT4B TU9 DSD_T4_B", UserTestData.USERS_TU9_SSD_HIER,
            RoleTestData.DSD_T4_B );

        // The test itself
        // public void dropActiveRole(Session session, String role)
        AccessMgrImplTest.dropActiveRoles( "DRP-ACT-RLS TU1_UPD TR1 bad:TR2", UserTestData.USERS_TU1_UPD,
            RoleTestData.ROLES_TR1 );
        AccessMgrImplTest.dropActiveRoles( "DRP-ACT-RLS TU3 TR3 bad:TR1", UserTestData.USERS_TU3,
            RoleTestData.ROLES_TR3 );
        AccessMgrImplTest.dropActiveRoles( "DRP-ACT-RLS TU4 TR2 bad:TR1", UserTestData.USERS_TU4,
            RoleTestData.ROLES_TR2 );
    }


    @Test
    public void testSessionPermission()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );

        // from testAdminMgrAddRoleInheritance
        AdminMgrImplTest.addInheritedRoles( "ADD-INHERIT-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU7_HIER", UserTestData.USERS_TU7_HIER, true );

        // from testAdminMgrUpdateUser
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );

        // from testAssignUser
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsersH( "ASGN-USRS_H TU7 HIER TR5 HIER", UserTestData.USERS_TU7_HIER,
            RoleTestData.ROLES_TR5_HIER, true );

        // from testAddPermissionObj
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false );
        AdminMgrImplTest.addPermObjs( "ADD-OBS TOB4", PermTestData.OBJS_TOB4, true, false );

        // from testAddPermissionOp
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false );
        AdminMgrImplTest.addPermOps( "ADD-OPS TOB4 TOP4", PermTestData.OBJS_TOB4, PermTestData.OPS_TOP4, true, false );

        // from testGrantPermissionRole
        AdminMgrImplTest.addRoleGrants( "GRNT-PRMS TR1 TOB1 TOP1", RoleTestData.ROLES_TR1, PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1, true, false );
        AdminMgrImplTest.addRoleGrantsH( "GRNT-PRMS_H ROLES_TR5_HIER TOB4 TOP4", RoleTestData.ROLES_TR5_HIER,
            PermTestData.OBJS_TOB4,
            PermTestData.OPS_TOP4 );

        // The test itself
        // public List<Permission> sessionPermissions(Session session)
        // public static void sessionPermissions(String msg, String[][] uArray, String[][] oArray, String[][] opArray)
        AccessMgrImplTest.sessionPermissions( "SESS-PRMS TU1_UPD TO1 TOP1 ", UserTestData.USERS_TU1_UPD,
            PermTestData.OBJS_TOB1,
            PermTestData.OPS_TOP1 );
        AccessMgrImplTest.sessionPermissionsH( "SESS-PRMS_H USERS_TU7_HIER OBJS_TOB4 OPS_TOP4 ",
            UserTestData.USERS_TU7_HIER,
            PermTestData.OBJS_TOB4, PermTestData.OPS_TOP4 );
    }


    @Test
    public void testCreateSessionWithRole()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );

        // from testAdminMgrUpdateUser
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );

        // from testAssignUser
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );

        // The test itself
        // public Session createSession(User user, boolean isTrusted)
        AccessMgrImplTest.createSessionsWithRoles( "CR-SESS-WRLS TU1_UPD TR1", UserTestData.USERS_TU1_UPD,
            RoleTestData.ROLES_TR1 );
        AccessMgrImplTest.createSessionsWithRoles( "CR-SESS-WRLS TU3 TR3", UserTestData.USERS_TU3,
            RoleTestData.ROLES_TR3 );
        AccessMgrImplTest.createSessionsWithRoles( "CR-SESS-WRLS TU4 TR2", UserTestData.USERS_TU4,
            RoleTestData.ROLES_TR2 );
    }


    @Test
    public void testCreateSessionWithRolesTrusted()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS TR1", RoleTestData.ROLES_TR1 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR2", RoleTestData.ROLES_TR2 );
        AdminMgrImplTest.addRoles( "ADD-RLS TR3", RoleTestData.ROLES_TR3 );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU1", UserTestData.USERS_TU1, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU3", UserTestData.USERS_TU3, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU4", UserTestData.USERS_TU4, true );

        // from testAdminMgrUpdateUser
        AdminMgrImplTest.updateUsers( "UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD );

        // from testAssignUser
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true );
        AdminMgrImplTest.assignUsers( "ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true );

        // The test itself
        // public Session createSession(User user, boolean isTrusted)
        AccessMgrImplTest.createSessionsWithRolesTrusted( "CR-SESS-WRLS-TRST TU1_UPD TR1", UserTestData.USERS_TU1_UPD,
            RoleTestData.ROLES_TR1 );
        AccessMgrImplTest.createSessionsWithRolesTrusted( "CR-SESS-WRLS-TRST TU3 TR3", UserTestData.USERS_TU3,
            RoleTestData.ROLES_TR3 );
        AccessMgrImplTest.createSessionsWithRolesTrusted( "CR-SESS-WRLS-TRST TU4 TR2", UserTestData.USERS_TU4,
            RoleTestData.ROLES_TR2 );
    }


    @Test
    public void testFindSsdSets()
    {
        // Add the needed data for this test 
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR8_SSD", RoleTestData.ROLES_TR8_SSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR9_SSD", RoleTestData.ROLES_TR9_SSD );
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR10_SSD", RoleTestData.ROLES_TR10_SSD );

        // from testAdminMgrAddRoleDescendant
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR11-DESC-SSD", RoleTestData.ROLES_TR11_DESC_SSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR12-DESC-SSD", RoleTestData.ROLES_TR12_DESC_SSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR13-DESC-SSD", RoleTestData.ROLES_TR13_DESC_SSD );

        // from testAdminMgrCreateSsdSet
        AdminMgrImplTest.createSsdSet( "ADD-SSD T4", RoleTestData.SSD_T4 );
        AdminMgrImplTest.createSsdSet( "ADD-SSD T5", RoleTestData.SSD_T5 );
        AdminMgrImplTest.createSsdSet( "ADD-SSD T6", RoleTestData.SSD_T6 );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, true );

        // from testAssignUser
        AdminMgrImplTest.assignUsersSSD( "ASGN-USRS_SSDT4B TU9 SSD_T4_B", UserTestData.USERS_TU9_SSD_HIER,
            RoleTestData.SSD_T4_B );
        AdminMgrImplTest.assignUsersSSD( "ASGN-USRS_SSDT5B TU10 SSD_T5_B", UserTestData.USERS_TU10_SSD_HIER,
            RoleTestData.SSD_T5_B );
        AdminMgrImplTest.assignUsersSSD( "ASGN-USRS_SSDT6B TU11 SSD_T6_B", UserTestData.USERS_TU11_SSD_HIER,
            RoleTestData.SSD_T6_B );

        // The test itself
        ReviewMgrImplTest.searchSsdSets(
            "SRCH-SSD T1",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.SSD_T1[0] ),
                RoleTestData.getName( RoleTestData.SSD_T1[0] ).length() - 1 ), RoleTestData.SSD_T1 );
        ReviewMgrImplTest.searchSsdSets(
            "SRCH-SSD T4",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.SSD_T4[0] ),
                RoleTestData.getName( RoleTestData.SSD_T4[0] ).length() - 1 ), RoleTestData.SSD_T4 );
        ReviewMgrImplTest.searchSsdSets(
            "SRCH-SSD T5",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.SSD_T5[0] ),
                RoleTestData.getName( RoleTestData.SSD_T5[0] ).length() - 1 ), RoleTestData.SSD_T5 );
        ReviewMgrImplTest.searchSsdSets(
            "SRCH-SSD T6",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.SSD_T6[0] ),
                RoleTestData.getName( RoleTestData.SSD_T6[0] ).length() - 1 ), RoleTestData.SSD_T6 );
    }


    @Test
    public void testFindDsdSets()
    {
        // from testAddOrgUnit
        DelegatedMgrImplTest.addOrgUnits( "ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1 );

        // from testAddRole
        AdminMgrImplTest.addRoles( "ADD-RLS ROLES_TR8_DSD", RoleTestData.ROLES_TR8_DSD );

        // from testAdminMgrAddRoleDescendant
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR11-DESC-DSD", RoleTestData.ROLES_TR11_DESC_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR12-DESC-DSD", RoleTestData.ROLES_TR12_DESC_DSD );
        AdminMgrImplTest.addRoleDescendant( "ADD-RLS-TR13-DESC-DSD", RoleTestData.ROLES_TR13_DESC_DSD );

        // from testAdminMgrAddUser
        AdminMgrImplTest.addUsers( "ADD-USRS TU8_SSD", UserTestData.USERS_TU8_SSD, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU12_DSD", UserTestData.USERS_TU12_DSD, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU13_DSD_HIER", UserTestData.USERS_TU13_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU14_DSD_HIER", UserTestData.USERS_TU14_DSD_HIER, true );
        AdminMgrImplTest.addUsers( "ADD-USRS TU15_DSD_HIER", UserTestData.USERS_TU15_DSD_HIER, true );

        // from testAssignUser
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT1 TU8 DSD_T1", UserTestData.USERS_TU8_SSD, RoleTestData.DSD_T1 );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT4B TU9 DSD_T4_B", UserTestData.USERS_TU9_SSD_HIER,
            RoleTestData.DSD_T4_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT5B TU10 DSD_T5_B", UserTestData.USERS_TU10_SSD_HIER,
            RoleTestData.DSD_T5_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT6B TU11 DSD_T6_B", UserTestData.USERS_TU11_SSD_HIER,
            RoleTestData.DSD_T6_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT1 TU12 DSD_T1", UserTestData.USERS_TU12_DSD,
            RoleTestData.DSD_T1 );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT4B TU13 DSD_T4_B", UserTestData.USERS_TU13_DSD_HIER,
            RoleTestData.DSD_T4_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT5B TU14 DSD_T5_B", UserTestData.USERS_TU14_DSD_HIER,
            RoleTestData.DSD_T5_B );
        AdminMgrImplTest.assignUsersDSD( "ASGN-USRS_DSDT6B TU15 DSD_T6_B", UserTestData.USERS_TU15_DSD_HIER,
            RoleTestData.DSD_T6_B );

        // The test itself
        ReviewMgrImplTest.searchDsdSets(
            "SRCH-DSD T1",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.DSD_T1[0] ),
                RoleTestData.getName( RoleTestData.DSD_T1[0] ).length() - 1 ), RoleTestData.DSD_T1 );
        ReviewMgrImplTest.searchDsdSets(
            "SRCH-DSD T4",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.DSD_T4[0] ),
                RoleTestData.getName( RoleTestData.DSD_T4[0] ).length() - 1 ), RoleTestData.DSD_T4 );
        ReviewMgrImplTest.searchDsdSets(
            "SRCH-DSD T5",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.DSD_T5[0] ),
                RoleTestData.getName( RoleTestData.DSD_T5[0] ).length() - 1 ), RoleTestData.DSD_T5 );
        ReviewMgrImplTest.searchDsdSets(
            "SRCH-DSD T6",
            TestUtils.getSrchValue( RoleTestData.getName( RoleTestData.DSD_T6[0] ),
                RoleTestData.getName( RoleTestData.DSD_T6[0] ).length() - 1 ), RoleTestData.DSD_T6 );
    }
}
