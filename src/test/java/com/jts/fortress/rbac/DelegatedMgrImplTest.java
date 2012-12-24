/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.AccessMgr;
import com.jts.fortress.AccessMgrFactory;
import com.jts.fortress.DelAccessMgr;
import com.jts.fortress.DelAccessMgrFactory;
import com.jts.fortress.DelAdminMgr;
import com.jts.fortress.DelAdminMgrFactory;
import com.jts.fortress.DelReviewMgr;
import com.jts.fortress.DelReviewMgrFactory;
import com.jts.fortress.ReviewMgr;
import com.jts.fortress.ReviewMgrFactory;
import com.jts.fortress.SecurityException;
import com.jts.fortress.util.LogUtil;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DelegatedMgrImpl Tester.
 *
 * @author Shawn McKinney
 * @created September 18, 2010
  */
public class DelegatedMgrImplTest extends TestCase
{
    private static final String CLS_NM = DelegatedMgrImplTest.class.getName();
    private static final String contextId = FortressJUnitTest.getContext();
    final protected static Logger log = Logger.getLogger(CLS_NM);

    public DelegatedMgrImplTest(String name)
    {
        super(name);
    }

    static Session adminSess = null;

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
        suite.addTest(new DelegatedMgrImplTest("testRevokePermissionRole"));
        suite.addTest(new DelegatedMgrImplTest("testDeassignAdminUser"));
        suite.addTest(new DelegatedMgrImplTest("testDeleteUser"));
        suite.addTest(new DelegatedMgrImplTest("testDeletePermission"));
        suite.addTest(new DelegatedMgrImplTest("testDeleteRole"));
        suite.addTest(new DelegatedMgrImplTest("testDeleteOrgInheritance"));
        suite.addTest(new DelegatedMgrImplTest("testDeleteOrgUnit"));
        suite.addTest(new DelegatedMgrImplTest("testDeleteAdminUser"));
        suite.addTest(new DelegatedMgrImplTest("testAddAdminUser"));
        suite.addTest(new DelegatedMgrImplTest("testAddOrgUnit"));
        suite.addTest(new DelegatedMgrImplTest("testUpdateOrgUnit"));
        suite.addTest(new DelegatedMgrImplTest("testAddOrgInheritance"));        
        suite.addTest(new DelegatedMgrImplTest("testReadOrgUnit"));
        suite.addTest(new DelegatedMgrImplTest("testSearchOrgUnits"));
        suite.addTest(new DelegatedMgrImplTest("testAddRole"));
        suite.addTest(new DelegatedMgrImplTest("testAddUser"));
        suite.addTest(new DelegatedMgrImplTest("testAddPermission"));
        suite.addTest(new DelegatedMgrImplTest("testAssignAdminUser"));
        suite.addTest(new DelegatedMgrImplTest("testGrantPermissionRole"));
        suite.addTest(new DelegatedMgrImplTest("testCheckAccess"));
        suite.addTest(new DelegatedMgrImplTest("testCanAssignUser"));
        suite.addTest(new DelegatedMgrImplTest("testCanDeassignUser"));
        suite.addTest(new DelegatedMgrImplTest("testCanGrantPerm"));
        suite.addTest(new DelegatedMgrImplTest("testCanRevokePerm"));

        return suite;
    }

    public void testAddAdminUser()
    {
        addOrgUnit("ADD ORG_PRM_APP0", OrgUnitTestData.ORGS_PRM_APP0[0]);
        addOrgUnit("ADD ORG_USR_DEV0", OrgUnitTestData.ORGS_USR_DEV0[0]);
        addAdminRoles("ADD-ARLS SUPER", AdminRoleTestData.AROLES_SUPER, false);
        AdminMgrImplTest.addPermObjs("ADD-OBS PSWDMGR_OBJ", PermTestData.PSWDMGR_OBJ, false, false);
        AdminMgrImplTest.addPermObjs("ADD-OBS ADMINMGR_OBJ", PermTestData.ADMINMGR_OBJ, false, false);
        AdminMgrImplTest.addPermObjs("ADD-OBS DELEGATEDMGR_OBJ", PermTestData.DELEGATEDMGR_OBJ, false, false);
        AdminMgrImplTest.addPermObjs("ADD-OBS DELEGATEDREVIEWMGR_OBJ", PermTestData.DELEGATEDREVIEWMGR_OBJ, false, false);
        AdminMgrImplTest.addPermObjs("ADD-OBS REVIEWMGR_OBJ", PermTestData.REVIEWMGR_OBJ, false, false);
        AdminMgrImplTest.addPermOps("ADD-OPS PSWDMGR_OBJ PSWDMGR_OPS", PermTestData.PSWDMGR_OBJ, PermTestData.PSWDMGR_OPS, false, false);
        AdminMgrImplTest.addPermOps("ADD-OPS ADMINMGR_OBJ ADMINMGR_OPS", PermTestData.ADMINMGR_OBJ, PermTestData.ADMINMGR_OPS, false, false);
        AdminMgrImplTest.addPermOps("ADD-OPS DELEGATEDMGR_OBJ DELEGATEDMGR_OPS", PermTestData.DELEGATEDMGR_OBJ, PermTestData.DELEGATEDMGR_OPS, false, false);
        AdminMgrImplTest.addPermOps("ADD-OPS DELEGATEDREVIEWMGR_OBJ DELEGATEDREVIEWMGR_OPS", PermTestData.DELEGATEDREVIEWMGR_OBJ, PermTestData.DELEGATEDREVIEWMGR_OPS, false, false);
        AdminMgrImplTest.addPermOps("ADD-OPS REVIEWMGR_OBJ REVIEWMGR_OPS", PermTestData.REVIEWMGR_OBJ, PermTestData.REVIEWMGR_OPS, false, false);
        AdminMgrImplTest.addRoleGrants("GRNT-APRMS SUPER PSWDMGR_OBJ PSWDMGR_OPS", AdminRoleTestData.AROLES_SUPER, PermTestData.PSWDMGR_OBJ, PermTestData.PSWDMGR_OPS, false, false);
        AdminMgrImplTest.addRoleGrants("GRNT-APRMS SUPER ADMINMGR_OBJ ADMINMGR_OPS", AdminRoleTestData.AROLES_SUPER, PermTestData.ADMINMGR_OBJ, PermTestData.ADMINMGR_OPS, false, false);
        AdminMgrImplTest.addRoleGrants("GRNT-APRMS SUPER DELEGATEDMGR_OBJ DELEGATEDMGR_OPS", AdminRoleTestData.AROLES_SUPER, PermTestData.DELEGATEDMGR_OBJ, PermTestData.DELEGATEDMGR_OPS, false, false);
        AdminMgrImplTest.addRoleGrants("GRNT-APRMS SUPER DELEGATEDREVIEWMGR_OBJ DELEGATEDREVIEWMGR_OPS", AdminRoleTestData.AROLES_SUPER, PermTestData.DELEGATEDREVIEWMGR_OBJ, PermTestData.DELEGATEDREVIEWMGR_OPS, false, false);
        AdminMgrImplTest.addRoleGrants("GRNT-APRMS SUPER REVIEWMGR_OBJ REVIEWMGR_OPS", AdminRoleTestData.AROLES_SUPER, PermTestData.REVIEWMGR_OBJ, PermTestData.REVIEWMGR_OPS, false, false);
        AdminMgrImplTest.addUsers("ADD-USRS TU0", UserTestData.USERS_TU0, false);
        assignAdminUsers("ASGN-USRS TU0 SUPER", UserTestData.USERS_TU0, AdminRoleTestData.AROLES_SUPER, false);

        // do these last - may have already been added via demoUsers:
        AdminMgrImplTest.addPermObjs("ADD-OBS AUDITMGR_OBJ", PermTestData.AUDITMGR_OBJ, false, true);
        AdminMgrImplTest.addPermOps("ADD-OPS AUDITMGR_OBJ AUDITMGR_OPS", PermTestData.AUDITMGR_OBJ, PermTestData.AUDITMGR_OPS, false, true);
        AdminMgrImplTest.addRoleGrants("GRNT-APRMS SUPER AUDITMGR_OBJ AUDITMGR_OPS", AdminRoleTestData.AROLES_SUPER, PermTestData.AUDITMGR_OBJ, PermTestData.AUDITMGR_OPS, false, true);
    }

    public void testDeleteAdminUser()
    {
        deassignAdminUsers("DEASGN-USRS TU0 SUPER", UserTestData.USERS_TU0, AdminRoleTestData.AROLES_SUPER, false);
        AdminMgrImplTest.deleteUsers("DEL-USRS TU0", UserTestData.USERS_TU0, true, false);
        AdminMgrImplTest.delRoleGrants("REVK-APRMS SUPER AUDITMGR_OBJ AUDITMGR_OPS", AdminRoleTestData.AROLES_SUPER, PermTestData.AUDITMGR_OBJ, PermTestData.AUDITMGR_OPS, false);
        AdminMgrImplTest.delRoleGrants("REVK-APRMS SUPER REVIEWMGR_OBJ REVIEWMGR_OPS", AdminRoleTestData.AROLES_SUPER, PermTestData.REVIEWMGR_OBJ, PermTestData.REVIEWMGR_OPS, false);
        AdminMgrImplTest.delRoleGrants("REVK-APRMS SUPER PSWDMGR_OBJ PSWDMGR_OPS", AdminRoleTestData.AROLES_SUPER, PermTestData.PSWDMGR_OBJ, PermTestData.PSWDMGR_OPS, false);
        AdminMgrImplTest.delRoleGrants("REVK-APRMS SUPER ADMINMGR_OBJ ADMINMGR_OPS", AdminRoleTestData.AROLES_SUPER, PermTestData.ADMINMGR_OBJ, PermTestData.ADMINMGR_OPS, false);
        AdminMgrImplTest.delRoleGrants("REVK-APRMS SUPER DELEGATEDREVIEWMGR_OBJ DELEGATEDREVIEWMGR_OPS", AdminRoleTestData.AROLES_SUPER, PermTestData.DELEGATEDREVIEWMGR_OBJ, PermTestData.DELEGATEDREVIEWMGR_OPS, false);
        AdminMgrImplTest.delRoleGrants("REVK-APRMS SUPER DELEGATEDMGR_OBJ DELEGATEDMGR_OPS", AdminRoleTestData.AROLES_SUPER, PermTestData.DELEGATEDMGR_OBJ, PermTestData.DELEGATEDMGR_OPS, false);
        AdminMgrImplTest.delPermOps("DEL-OPS AUDITMGR_OBJ AUDITMGR_OPS ", PermTestData.AUDITMGR_OBJ, PermTestData.AUDITMGR_OPS, false, false);
        AdminMgrImplTest.delPermOps("DEL-OPS REVIEWMGR_OBJ REVIEWMGR_OPS ", PermTestData.REVIEWMGR_OBJ, PermTestData.REVIEWMGR_OPS, false, false);
        AdminMgrImplTest.delPermOps("DEL-OPS PSWDMGR_OBJ PSWDMGR_OPS", PermTestData.PSWDMGR_OBJ, PermTestData.PSWDMGR_OPS, false, false);
        AdminMgrImplTest.delPermOps("DEL-OPS ADMINMGR_OBJ ADMINMGR_OPS ", PermTestData.ADMINMGR_OBJ, PermTestData.ADMINMGR_OPS, false, false);
        AdminMgrImplTest.delPermOps("DEL-OPS DELEGATEDREVIEWMGR_OBJ DELEGATEDREVIEWMGR_OPS ", PermTestData.DELEGATEDREVIEWMGR_OBJ, PermTestData.DELEGATEDREVIEWMGR_OPS, false, false);
        AdminMgrImplTest.delPermOps("DEL-OPS DELEGATEDMGR_OBJ DELEGATEDMGR_OPS ", PermTestData.DELEGATEDMGR_OBJ, PermTestData.DELEGATEDMGR_OPS, false, false);
        AdminMgrImplTest.delPermObjs("DEL-OBJS AUDITMGR_OBJ", PermTestData.AUDITMGR_OBJ, false);
        AdminMgrImplTest.delPermObjs("DEL-OBJS REVIEWMGR_OBJ", PermTestData.REVIEWMGR_OBJ, false);
        AdminMgrImplTest.delPermObjs("DEL-OBJS PSWDMGR_OBJ", PermTestData.PSWDMGR_OBJ, false);
        AdminMgrImplTest.delPermObjs("DEL-OBJS ADMINMGR_OBJ", PermTestData.ADMINMGR_OBJ, false);
        AdminMgrImplTest.delPermObjs("DEL-OBJS DELEGATEDREVIEWMGR_OBJ", PermTestData.DELEGATEDREVIEWMGR_OBJ, false);
        AdminMgrImplTest.delPermObjs("DEL-OBJS DELEGATEDMGR_OBJ", PermTestData.DELEGATEDMGR_OBJ, false);
        deleteAdminRoles("DEL-ARLS SUPER", AdminRoleTestData.AROLES_SUPER, false);
        deleteOrgUnit("DEL ORG_USR_DEV0", OrgUnitTestData.ORGS_USR_DEV0[0]);
        deleteOrgUnit("DEL ORG_PRM_APP0", OrgUnitTestData.ORGS_PRM_APP0[0]);
    }

    public void testAssignAdminUser()
    {
        //     public void assignUser(User user, Role role)
        assignAdminUsers("ASGN-USRS TU16 TR1", UserTestData.USERS_TU16_ARBAC, AdminRoleTestData.AROLES_TR1, true);
        assignAdminUserRole("ASGN-USR TU17A TR2", UserTestData.USERS_TU17A_ARBAC, AdminRoleTestData.AROLES_TR2, true);
    }


    /**
     * 
     * @param msg
     * @param uArray
     * @param rArray
     */
    public void assignAdminUsers(String msg, String[][] uArray, String[][] rArray, boolean isAdmin)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr;
            DelReviewMgr dReviewMgr;
            if(isAdmin)
            {
                dAdminMgr = getManagedDelegatedMgr();
                dReviewMgr = getManagedDelegatedReviewMgr();
            }
            else
            {
                dAdminMgr = DelAdminMgrFactory.createInstance(contextId);
                dReviewMgr = DelReviewMgrFactory.createInstance(contextId);
            }

            int i = 0;
            for (String[] usr : uArray)
            {
                i++;
                for (String[] rle : rArray)
                {
                    UserAdminRole uAdminRole = new UserAdminRole(UserTestData.getUserId(usr), AdminRoleTestData.getRole(rle).getName());
                    dAdminMgr.assignUser(uAdminRole);
                    log.debug(CLS_NM + ".assignAdminUsers user [" + uAdminRole.getUserId() + "] role [" + uAdminRole.getName() + "] successful");
                    // Let's double check the number of users not associated with role:
                    // This one retrieves the collection of all "roleOccupant" attributes associated with the role node:
                    List<User> users = dReviewMgr.assignedUsers(AdminRoleTestData.getRole(rle));
                    assertNotNull(users);
                    assertTrue(CLS_NM + ".assignAdminUsers list size check", i == users.size());
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".assignAdminUsers caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDeassignAdminUser()
    {
        //     public void deassignUser(User user, Role role)
        deassignAdminUsers("DEASGN-USRS TU16 TR1", UserTestData.USERS_TU16_ARBAC, AdminRoleTestData.AROLES_TR1, true);
        deassignAdminUserRole("DEASGN-USR TU17A TR2", UserTestData.USERS_TU17A_ARBAC, AdminRoleTestData.AROLES_TR2, true);
    }


    /**
     * @param msg
     * @param uArray
     * @param rArray
     */
    public void deassignAdminUsers(String msg, String[][] uArray, String[][] rArray, boolean isAdmin)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr;
            DelReviewMgr dReviewMgr;
            if(isAdmin)
            {
                dAdminMgr = getManagedDelegatedMgr();
                dReviewMgr = getManagedDelegatedReviewMgr();
            }
            else
            {
                dAdminMgr = DelAdminMgrFactory.createInstance(contextId);
                dReviewMgr = DelReviewMgrFactory.createInstance(contextId);
            }
            int i = 0;
            for (String[] usr : uArray)
            {
                i++;
                for (String[] rle : rArray)
                {
                    UserAdminRole uAdminRole = new UserAdminRole(UserTestData.getUserId(usr), AdminRoleTestData.getRole(rle).getName());
                    AdminRole role = AdminRoleTestData.getRole(rle);
                    dAdminMgr.deassignUser(uAdminRole);
                    log.debug(CLS_NM + ".deassignAdminUsers user [" + uAdminRole.getUserId() + "] role [" + uAdminRole.getName() + "] successful");
                    // Let's double check the number of users associated with role:
                    List<User> users = dReviewMgr.assignedUsers(AdminRoleTestData.getRole(rle));
                    assertNotNull(users);

                    // If this is the last user deassigned from role, both lists will be returned empty:
                    if (i == uArray.length)
                    {
                        assertTrue(users.size() == 0);
                    }
                    else
                    {
                        assertTrue(CLS_NM + "deassignAdminUsers list size check", (rArray.length - i) == users.size());
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deassignAdminUsers caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void assignAdminUserRole(String msg, String[][] uArray, String[][] rArray, boolean isAdmin)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr;
            if(isAdmin)
            {
                dAdminMgr = getManagedDelegatedMgr();
            }
            else
            {
                dAdminMgr = DelAdminMgrFactory.createInstance(contextId);
            }
            int i = 0;
            for (String[] usr : uArray)
            {
                UserAdminRole uAdminRole = new UserAdminRole(UserTestData.getUserId(usr), AdminRoleTestData.getRole(rArray[i]).getName());
                dAdminMgr.assignUser(uAdminRole);
                i++;
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".assignAdminUsers caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void deassignAdminUserRole(String msg, String[][] uArray, String[][] rArray, boolean isAdmin)
    {
        LogUtil.logIt(msg);
        AdminRole aRole = null;
        try
        {
            DelAdminMgr dAdminMgr;
            if(isAdmin)
            {
                dAdminMgr = getManagedDelegatedMgr();
            }
            else
            {
                dAdminMgr = DelAdminMgrFactory.createInstance(contextId);
            }
            int i = 0;
            for (String[] usr : uArray)
            {
                UserAdminRole uAdminRole = new UserAdminRole(UserTestData.getUserId(usr), AdminRoleTestData.getRole(rArray[i]).getName());
                dAdminMgr.deassignUser(uAdminRole);
                i++;
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deassignAdminUserRole role [" + aRole.getName() + "] caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testAddUser()
    {
        //     public User addUser(User user)
        // the admin user must be added before the "addUsers" can be called:
        //AdminMgrImplTest.addAdminUser("ADD-USRS TU0", UserTestData.USERS_TU0[0]);
        AdminMgrImplTest.addUsers("ADD-USRS TU16_ARBAC", UserTestData.USERS_TU16_ARBAC, true);
        AdminMgrImplTest.addUsers("ADD-USRS TU16B_ARBAC", UserTestData.USERS_TU16B_ARBAC, true);
        AdminMgrImplTest.addUsers("ADD-USRS TU16A_ARBAC", UserTestData.USERS_TU17A_ARBAC, true);
        AdminMgrImplTest.addUsers("ADD-USRS TU16U_ARBAC", UserTestData.USERS_TU17U_ARBAC, true);

    }


    public void testDeleteUser()
    {
        //     public void disableUser(User user)
        AdminMgrImplTest.deleteUsers("DEL-USRS TU16B_ARBAC", UserTestData.USERS_TU16B_ARBAC, true, true);
        AdminMgrImplTest.deleteUsers("DEL-USRS TU16_ARBAC", UserTestData.USERS_TU16_ARBAC, true, true);
        AdminMgrImplTest.deleteUsers("DEL-USRS TU16A_ARBAC", UserTestData.USERS_TU17A_ARBAC, true, true);
        AdminMgrImplTest.deleteUsers("DEL-USRS TU16U_ARBAC", UserTestData.USERS_TU17U_ARBAC, true, true);
    }

    public void testAddPermission()
    {
        //     public Permission addPermObj(Permission pOp)
        AdminMgrImplTest.addPermObjs("ADD-OBS TOB5", PermTestData.OBJS_TOB5, true, false);
        AdminMgrImplTest.addPermObjs("ADD-OBS ARBAC1", PermTestData.ARBAC_OBJS_1, true, false);
        AdminMgrImplTest.addPermOps("ADD-OPS ARBAC1", PermTestData.ARBAC_OBJS_1, PermTestData.ARBAC_OPS_1, true, false);
        AdminMgrImplTest.addPermObjs("ADD-OBS ARBAC2", PermTestData.ARBAC_OBJ2, true, false);
        AdminMgrImplTest.addPermOps("ADD-OPS ARBAC2", PermTestData.ARBAC_OBJ2, PermTestData.ARBAC_OPS_2, true, false);
    }

    public void testDeletePermission()
    {
        //     public void deletePermission(Permission pOp)
        AdminMgrImplTest.delPermObjs("DEL-OBJS TOB5", PermTestData.OBJS_TOB5, true);
        AdminMgrImplTest.delPermOps("DEL-OPS ARBAC1", PermTestData.ARBAC_OBJS_1, PermTestData.ARBAC_OPS_1, true, false);
        AdminMgrImplTest.delPermObjs("DEL-OBJS ARBAC1", PermTestData.ARBAC_OBJS_1, true);
        AdminMgrImplTest.delPermOps("DEL-OPS ARBAC2", PermTestData.ARBAC_OBJ2, PermTestData.ARBAC_OPS_2, true, false);
        AdminMgrImplTest.delPermObjs("DEL-OBS ARBAC2", PermTestData.ARBAC_OBJ2, true);
    }


    public void testGrantPermissionRole()
    {
        //     public void grantPermission(Permission pOp, Role role)
        AdminMgrImplTest.addRoleGrants("GRNT-APRMS ARTR2 AROBJ1 AROPS1", AdminRoleTestData.AROLES_TR2, PermTestData.ARBAC_OBJS_1, PermTestData.ARBAC_OPS_1, true, false);
    }

    public void testRevokePermissionRole()
    {
        //     public void revokePermission(Permission pOp, Role role)
        AdminMgrImplTest.delRoleGrants("REVK-APRMS ARTR2 AROBJ1 AROPS1", AdminRoleTestData.AROLES_TR2, PermTestData.ARBAC_OBJS_1, PermTestData.ARBAC_OPS_1, true);
    }

    public void testCheckAccess()
    {
        // public boolean checkAccess(String object, String operation, Session session)
        checkAccess("CHCK-ACS TU1_UPD TO1 TOP1 ", UserTestData.USERS_TU17A_ARBAC, PermTestData.ARBAC_OBJS_1, PermTestData.ARBAC_OPS_1, PermTestData.ARBAC_OBJ2, PermTestData.ARBAC_OPS_2);
    }

    public static void checkAccess(String msg, String[][] uArray, String[][] oArray, String[][] opArray, String[][] oArrayBad, String[][] opArrayBad)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAccessMgr dAccessMgr = DelAccessMgrFactory.createInstance(contextId);
            AccessMgr accessMgr = (AccessMgr)dAccessMgr;
            for (String[] usr : uArray)
            {
                User user = UserTestData.getUser(usr);
                Session session = accessMgr.createSession(user, false);

                assertNotNull(session);
                int i = 0;
                for (String[] obj : oArray)
                {
                    int j = 0;
                    for (String[] op : opArray)
                    {
                        // Call checkAccess method
                        assertTrue(CLS_NM + ".checkAccess failed userId [" + user.getUserId() + "] Perm objectName [" + PermTestData.getName(obj) + "] operationName [" + PermTestData.getName(op) + "]",
                            dAccessMgr.checkAccess(session, new Permission(PermTestData.getName(obj), PermTestData.getName(op))));
                        j++;
                    }
                    i++;
                }
                i = 0;
                for (String[] obj : oArrayBad)
                {
                    int j = 0;
                    for (String[] op : opArrayBad)
                    {
                        // Call checkAccess method (this should fail):
                        assertTrue(CLS_NM + ".checkAccess failed userId [" + user.getUserId() + "] Perm objectName [" + PermTestData.getName(oArrayBad[i]) + "] operationName [" + PermTestData.getName(opArrayBad[j]) + "]",
                            !dAccessMgr.checkAccess(session, new Permission(PermTestData.getName(oArrayBad[i]),PermTestData.getName(opArrayBad[j]))));

                        j++;
                    }
                    i++;
                }
            }
            log.debug(CLS_NM + ".checkAccess successful");
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".checkAccess: caught SecurityException errCode=" + ex.getErrorId() + ", msg: " + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testAddRole()
    {
        //     public Role setRole(Role role)
        AdminMgrImplTest.addRoles("ADD-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC);
        AdminMgrImplTest.addInheritedRoles("ADD-INHERIT-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC);
        AdminMgrImplTest.addRoles("ADD-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC);
        AdminMgrImplTest.addInheritedRoles("ADD-INHERIT-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC);
        addAdminRoles("ADD-ADMRLS TR1", AdminRoleTestData.AROLES_TR1, true);
        addAdminRoles("ADD-ADMRLS TR2", AdminRoleTestData.AROLES_TR2, true);
        addAdminRoles("ADD-ADMRLS TR3", AdminRoleTestData.AROLES_TR3, true);
        addAdminRoles("ADD-ADMRLS TR6", AdminRoleTestData.AROLES_TR6_HIER, true);
    }


    /**
     * @param rArray
     */
    private void addAdminRoles(String msg, String[][] rArray, boolean isAdmin)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr;
            if(isAdmin)
            {
                dAdminMgr = getManagedDelegatedMgr();
            }
            else
            {
                dAdminMgr = DelAdminMgrFactory.createInstance(contextId);
            }
            for (String[] rle : rArray)
            {
                AdminRole role = AdminRoleTestData.getRole(rle);
                AdminRole entity = dAdminMgr.addRole(role);
                log.debug(CLS_NM + ".addAdminRoles role [" + entity.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addAdminRoles caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDeleteRole()
    {
        //     public void deleteRole(Role role)
        deleteAdminRoles("DEL-ADMRLS TR1", AdminRoleTestData.AROLES_TR1, true);
        deleteAdminRoles("DEL-ADMRLS TR2", AdminRoleTestData.AROLES_TR2, true);
        deleteAdminRoles("DEL-ADMRLS TR3", AdminRoleTestData.AROLES_TR3, true);
        deleteAdminRoles("DEL-ADMRLS TR6", AdminRoleTestData.AROLES_TR6_HIER, true);
        AdminMgrImplTest.deleteInheritedRoles("DEL-INHERIT-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC);
        AdminMgrImplTest.deleteRoles("DEL-RLS ROLES_TR14_ARBAC", RoleTestData.ROLES_TR14_ARBAC);
        AdminMgrImplTest.deleteInheritedRoles("DEL-INHERIT-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC);
        AdminMgrImplTest.deleteRoles("DEL-RLS ROLES_TR15_ARBAC", RoleTestData.ROLES_TR15_ARBAC);
    }

    /**
     * @param rArray
     */
    private void deleteAdminRoles(String msg, String[][] rArray, boolean isAdmin)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr;
            if(isAdmin)
            {
                dAdminMgr = getManagedDelegatedMgr();
            }
            else
            {
                dAdminMgr = DelAdminMgrFactory.createInstance(contextId);
            }
            for (String[] rle : rArray)
            {
                AdminRole role = new AdminRole();
                role.setName(RoleTestData.getName(rle));
                dAdminMgr.deleteRole(role);
                log.debug(CLS_NM + ".deleteAdminRoles role [" + role.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deleteAdminRoles caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testUpdateAdminRole()
    {
        //     public Role updateRole(Role role)
        updateAdminRoles("UPD-ADMRLS TR3_UPD", AdminRoleTestData.AROLES_TR3_UPD, true);
    }


    /**
     * @param msg
     * @param rArray
     */
    private void updateAdminRoles(String msg, String[][] rArray, boolean isAdmin)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr;
            if(isAdmin)
            {
                dAdminMgr = getManagedDelegatedMgr();
            }
            else
            {
                dAdminMgr = DelAdminMgrFactory.createInstance(contextId);
            }
            for (String[] rle : rArray)
            {
                AdminRole role = AdminRoleTestData.getRole(rle);
                AdminRole entity = dAdminMgr.updateRole(role);
                log.debug(CLS_NM + ".updateAdminRoles role [" + entity.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".updateAdminRoles caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    private enum ASSIGN_OP{
        ASSIGN, DEASSIGN
    }

    public void testCanAssignUser()
    {
        //canAssignUsers("CAN-ASGN-USRS TU1 TR1", UserTestData.USERS_TU16_ARBAC, UserTestData.USERS_TU16B_ARBAC, RoleTestData.ROLES_TR14_ARBAC);
        canAssignUsers("CAN-ASGN-USRS URA_T1 TU17A TU17U TR15", ASSIGN_OP.ASSIGN, URATestData.URA_T1, UserTestData.USERS_TU17A_ARBAC, UserTestData.USERS_TU17U_ARBAC, RoleTestData.ROLES_TR15_ARBAC);
    }

    public void testCanDeassignUser()
    {
        canAssignUsers("CAN-DEASGN-USRS URA_T1 TU17A TU17U TR15", ASSIGN_OP.DEASSIGN, URATestData.URA_T1, UserTestData.USERS_TU17A_ARBAC, UserTestData.USERS_TU17U_ARBAC, RoleTestData.ROLES_TR15_ARBAC);
    }

    /**
     *
     * @param msg
     * @param op
     * @param uraArray
     * @param uaArray
     * @param uArray
     * @param rArray
     */
    public void canAssignUsers(String msg, ASSIGN_OP op, String[][] uraArray, String[][] uaArray, String[][] uArray, String[][] rArray)
    {
        LogUtil.logIt(msg);
        Role role;
        Map<URA,URA> uraTestResults = URATestData.getURAs(uraArray);

        try
        {
            DelAccessMgr delAccessMgr = DelAccessMgrFactory.createInstance(contextId);
            AccessMgr accessMgr = (AccessMgr)delAccessMgr;
            int i = 0;
            for (String[] aUsr : uaArray)
            {
                User aUser = UserTestData.getUser(aUsr);
                Session session = accessMgr.createSession(aUser, false);
                assertNotNull(session);
                for (String[] usr : uArray)
                {
                    User user = UserTestData.getUser(usr);
                    i++;
                    for (String[] rle : rArray)
                    {
                        role = RoleTestData.getRole(rle);
                        String methodName;
                        boolean result;
                        if(op == ASSIGN_OP.ASSIGN)
                        {
                            result = delAccessMgr.canAssign(session, user, role);
                            methodName = ".canAssignUsers";
                        }
                        else
                        {
                            result = delAccessMgr.canDeassign(session, user, role);
                            methodName = ".canDeassignUsers";
                        }

                        List<UserAdminRole> aRoles = session.getAdminRoles();
                        assertNotNull(aRoles);
                        assertTrue(CLS_NM + methodName + " Admin User invalid number of roles", aRoles.size() == 1);
                        // since this user should only have one admin role, get the first one from list:
                        UserAdminRole aRole = aRoles.get(0);
                        URA sourceUra = new URA(aRole.getName(), user.getOu(), role.getName(), result);
                        URA targetUra = uraTestResults.get(sourceUra);
                        assertTrue(CLS_NM + methodName + " cannot find target URA admin role [" + sourceUra.getArole() + " uou [" + sourceUra.getUou() + "] role [" + sourceUra.getUrole() + "] Result [" + sourceUra.isCanAssign() + "] actual result [" + result + "]", targetUra != null);
                        log.debug(CLS_NM + methodName + " User [" + user.getUserId() + "] success URA using admin role [" + targetUra.getArole() + " uou [" + targetUra.getUou() + "] role [" + targetUra.getUrole() + "] target result [" + targetUra.isCanAssign() + "] actual result [" + result + "]");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".canAssignUsers op [" + op + "] caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testCanGrantPerm()
    {
        canGrantPerms("CAN-GRNT-PRMS PRA_T1 TU17A TOB5 TR15", GRANT_OP.GRANT, PRATestData.PRA_T1, UserTestData.USERS_TU17A_ARBAC, PermTestData.OBJS_TOB5, RoleTestData.ROLES_TR15_ARBAC);
    }

    public void testCanRevokePerm()
    {
        canGrantPerms("CAN-RVKE-PRMS PRA_T1 TU17A TOB5 TR15", GRANT_OP.REVOKE, PRATestData.PRA_T1, UserTestData.USERS_TU17A_ARBAC, PermTestData.OBJS_TOB5, RoleTestData.ROLES_TR15_ARBAC);
    }

    private enum GRANT_OP{
        GRANT, REVOKE
    }

    /**
     *
     * @param msg
     * @param op
     * @param uraArray
     * @param uaArray
     * @param pArray
     * @param rArray
     */
    public void canGrantPerms(String msg, GRANT_OP op, String[][] uraArray, String[][] uaArray, String[][] pArray, String[][] rArray)
    {
        LogUtil.logIt(msg);
        Role role;
        Map<PRA,PRA> praTestResults = PRATestData.getPRAs(uraArray);

        try
        {
            int ctr;
            DelAccessMgr delAccessMgr = DelAccessMgrFactory.createInstance(contextId);
            AccessMgr accessMgr = (AccessMgr)delAccessMgr;
            int i = 0;
            for (String[] aUsr : uaArray)
            {
                User aUser = UserTestData.getUser(aUsr);
                Session session = accessMgr.createSession(aUser, false);
                assertNotNull(session);
                for (String[] prm : pArray)
                {
                    PermObj pObj = PermTestData.getObj(prm);
                    i++;
                    for (String[] rle : rArray)
                    {
                        role = RoleTestData.getRole(rle);
                        String methodName;
                        boolean result;
                        if(op == GRANT_OP.GRANT)
                        {
                            result = delAccessMgr.canGrant(session, role, new Permission(pObj.getObjectName(), ""));
                            methodName = ".canGrantPerms";
                        }
                        else
                        {
                            result = delAccessMgr.canRevoke(session, role, new Permission(pObj.getObjectName(), ""));
                            methodName = ".canRevokePerms";
                        }

                        List<UserAdminRole> aRoles = session.getAdminRoles();
                        assertNotNull(aRoles);
                        assertTrue(CLS_NM + methodName + " Admin User invalid number of roles", aRoles.size() == 1);
                        UserAdminRole aRole = aRoles.get(0);
                        PRA sourceUra = new PRA(aRole.getName(), pObj.getOu(), role.getName(), result);
                        PRA targetUra = praTestResults.get(sourceUra);
                        assertTrue(CLS_NM + methodName + " cannot find target PRA admin role [" + sourceUra.getArole() + " pou [" + sourceUra.getPou() + "] role [" + sourceUra.getUrole() + "] Result [" + sourceUra.isCanAssign() + "] actual result [" + result + "]", targetUra != null);
                        log.debug(CLS_NM + methodName + " failed target PRA admin role [" + targetUra.getArole() + " pou [" + targetUra.getPou() + "] role [" + targetUra.getUrole() + "] target result [" + targetUra.isCanAssign() + "] actual result [" + result + "]");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".canGrantPerms op [" + op + "] caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testAddOrgUnit()
    {
        //addOrgUnits("ADD ORGS_USR_DEV0", OrgUnitTestData.ORGS_USR_DEV0);
        addOrgUnits("ADD ORGS_TO1", OrgUnitTestData.ORGS_TO1);
        addOrgUnits("ADD ORGS_USR_TO2", OrgUnitTestData.ORGS_USR_TO2);
        addOrgUnits("ADD ORGS_ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3);
        addOrgUnits("ADD ORGS_PRM_TO4", OrgUnitTestData.ORGS_PRM_TO4);
        addOrgUnits("ADD ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5);
        addOrgUnits("ADD ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5);
        // The DEV1 OU is not removed during cleanup phase because the test system users belong to it:
        if(FortressJUnitTest.isFirstRun())
        {
            addOrgUnits("ADD ORGS_DEV1", OrgUnitTestData.ORGS_DEV1);
        }
        addOrgUnits("ADD ORGS_APP1", OrgUnitTestData.ORGS_APP1);
    }


    private void addOrgUnit(String msg, String[] org)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = DelAdminMgrFactory.createInstance(contextId);
            OrgUnit ou = OrgUnitTestData.getOrgUnit(org);
            OrgUnit entity = dAdminMgr.add(ou);
            log.debug(CLS_NM + ".addOrgUnit ou [" + entity.getName() + "] successful");
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addOrgUnit caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     *
     * @param msg
     * @param oArray
     */
    private void addOrgUnits(String msg, String[][] oArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            for (String[] ole : oArray)
            {
                OrgUnit ou = OrgUnitTestData.getOrgUnit(ole);
                OrgUnit entity = dAdminMgr.add(ou);
                log.debug(CLS_NM + ".addOrgUnits ou [" + entity.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addOrgUnits caught SecurityException=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testDeleteOrgUnit()
    {
        //deleteOrgUnits("DEL ORGS_USR_DEV0", OrgUnitTestData.ORGS_USR_DEV0);
        deleteOrgUnits("DEL ORGS_TO1", OrgUnitTestData.ORGS_TO1);
        deleteOrgUnits("DEL ORGS_USR_TO2", OrgUnitTestData.ORGS_USR_TO2);
        deleteOrgUnits("DEL ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3);
        deleteOrgUnits("DEL ORGS_PRM_TO4", OrgUnitTestData.ORGS_PRM_TO4);
        deleteOrgUnits("DEL ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5);
        deleteOrgUnits("DEL ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5);
        // Don't delete because the test system users will belong to DEV1 OU and cannot be removed by normal API call:
        // deleteOrgUnits("DEL ORGS_DEV1", OrgUnitTestData.ORGS_DEV1);
        deleteOrgUnits("DEL ORGS_APP1", OrgUnitTestData.ORGS_APP1);
    }

    /**
     *
     * @param msg
     * @param org
     */
    private void deleteOrgUnit(String msg, String[] org)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            OrgUnit ou = OrgUnitTestData.getOrgUnit(org);
            OrgUnit entity = dAdminMgr.delete(ou);
            log.debug(CLS_NM + ".deleteOrgUnit ou [" + ou.getName() + "] successful");
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deleteOrgUnit caught SecurityException=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     *
     * @param msg
     * @param oArray
     */
    private void deleteOrgUnits(String msg, String[][] oArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            for (String[] ole : oArray)
            {
                OrgUnit ou = OrgUnitTestData.getOrgUnit(ole);
                OrgUnit entity = dAdminMgr.delete(ou);
                log.debug(CLS_NM + ".deleteOrgUnits ou [" + ou.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deleteOrgUnits caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testUpdateOrgUnit()
    {
        updateOrgUnits("UPD ORGS_TO1", OrgUnitTestData.ORGS_TO1);
        updateOrgUnits("UPD ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3);

    }

    /**
     *
     * @param msg
     * @param oArray
     */
    private void updateOrgUnits(String msg, String[][] oArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            for (String[] ole : oArray)
            {
                OrgUnit ou = OrgUnitTestData.getOrgUnit(ole);
                OrgUnit entity = dAdminMgr.update(ou);
                log.debug(CLS_NM + ".updateOrgUnits ou [" + ou.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".updateOrgUnits caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testReadOrgUnit()
    {
        // public Role readRole(Role role)
        readOrgUnits("RD ORGS_TO1", OrgUnitTestData.ORGS_TO1);
        readOrgUnits("RD ORGS_PRM_TO3", OrgUnitTestData.ORGS_PRM_TO3);
    }

    /**
     *
     * @param msg
     * @param oArray
     */
    public void readOrgUnits(String msg, String[][] oArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelReviewMgr dReviewMgr = getManagedDelegatedReviewMgr();
            for (String[] ole : oArray)
            {
                OrgUnit ou = OrgUnitTestData.getOrgUnit(ole);
                OrgUnit entity = dReviewMgr.read(ou);
                OrgUnitTestData.assertEquals(entity, ole);
                log.debug(CLS_NM + ".readOrgUnits [" + entity.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".readOrgUnits caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testSearchOrgUnits()
    {
        searchOrgUnits("SRCH ORGS_TO1", TestUtils.getSrchValue(OrgUnitTestData.getName(OrgUnitTestData.ORGS_TO1[0])), OrgUnitTestData.ORGS_TO1);
        searchOrgUnits("SRCH ORGS_PRM_TO3", TestUtils.getSrchValue(OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO3[0])), OrgUnitTestData.ORGS_PRM_TO3);
    }

    /**
     *
     * @param msg
     * @param srchValue
     * @param oArray
     */
    public void searchOrgUnits(String msg, String srchValue, String[][] oArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelReviewMgr dReviewMgr = getManagedDelegatedReviewMgr();
            // grab the type from the first entry in the test org list:
            OrgUnit.Type type = OrgUnitTestData.getType(oArray[0]);
            List<OrgUnit> ous = dReviewMgr.search(type, srchValue);
            assertNotNull(ous);
            assertTrue(CLS_NM + "searchOrgUnits list size check", oArray.length == ous.size());
            for (String[] ole : oArray)
            {
                int indx = ous.indexOf(new OrgUnit(OrgUnitTestData.getName(ole)));
                if (indx != -1)
                {
                    OrgUnit entity = ous.get(indx);
                    assertNotNull(entity);
                    OrgUnitTestData.assertEquals(entity, ole);
                    log.debug(CLS_NM + ".searchOrgUnits [" + entity.getName() + "] successful");
                }
                else
                {
                    msg = CLS_NM + ".searchOrgUnits srchValue [" + srchValue + "] failed list search";
                    LogUtil.logIt(msg);
                    fail(msg);
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".searchOrgUnits srchValue [" + srchValue + "] caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testAddOrgInheritance()
    {
        addInheritedOrgUnits("ADD-INHERIT ORGS_USR_TO2", OrgUnitTestData.ORGS_USR_TO2);
        addInheritedOrgUnits("ADD-INHERIT ORGS_PRM_TO4", OrgUnitTestData.ORGS_PRM_TO4);
        addInheritedOrgUnits("ADD-INHERIT ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5);
        addInheritedOrgUnits("ADD-INHERIT ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5);
    }


    /**
     *
     * @param msg
     * @param oArray
     */
    private void addInheritedOrgUnits(String msg, String[][] oArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            for (String[] ole : oArray)
            {
                OrgUnit child = OrgUnitTestData.getOrgUnit(ole);
                Set<String> parents = OrgUnitTestData.getRelationships(ole);
                if (parents != null)
                {
                    for (String pOrg : parents)
                    {
                        OrgUnit parent = new OrgUnit(pOrg);
                        parent.setType(child.getType());
                        dAdminMgr.addInheritance(parent, child);
                        log.debug(CLS_NM + ".addInheritedOrgUnits child org [" + child.getName() + "] parent org [" + pOrg + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addInheritedOrgUnits caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDeleteOrgInheritance()
    {
        deleteInheritedOrgUnits("DEL-INHERIT ORGS_USR_TO2", OrgUnitTestData.ORGS_USR_TO2);
        deleteInheritedOrgUnits("DEL-INHERIT ORGS_PRM_TO4", OrgUnitTestData.ORGS_PRM_TO4);
        deleteInheritedOrgUnits("DEL-INHERIT ORGS_USR_TO5", OrgUnitTestData.ORGS_USR_TO5);
        deleteInheritedOrgUnits("DEL-INHERIT ORGS_PRM_TO5", OrgUnitTestData.ORGS_PRM_TO5);
    }


    /**
     * 
     * @param msg
     * @param oArray
     */
    private void deleteInheritedOrgUnits(String msg, String[][] oArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            for (String[] ole : oArray)
            {
                OrgUnit child = OrgUnitTestData.getOrgUnit(ole);
                Set<String> parents = OrgUnitTestData.getRelationships(ole);
                if (parents != null)
                {
                    for (String pOrg : parents)
                    {
                        OrgUnit parent = new OrgUnit(pOrg);
                        parent.setType(child.getType());
                        //public void deleteInheritance(OrgUnit parent, OrgUnit child)
                        dAdminMgr.deleteInheritance(parent, child);
                        log.debug(CLS_NM + ".deleteInheritedOrgUnits child org [" + child.getName() + "] parent org [" + pOrg + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deleteInheritedOrgUnits caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testReadAdminRole()
    {
        // public Role readRole(Role role)
        readAdminRoles("RD-ADMRLS TR1", AdminRoleTestData.AROLES_TR1);
        readAdminRoles("RD-ADMRLS TR2", AdminRoleTestData.AROLES_TR2);
        readAdminRoles("RD-ADMRLS TR3_UPD", AdminRoleTestData.AROLES_TR3_UPD);
    }

    /**
     *
     * @param msg
     * @param rArray
     */
    public void readAdminRoles(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelReviewMgr dReviewMgr = getManagedDelegatedReviewMgr();
            for (String[] rle : rArray)
            {
                AdminRole entity = dReviewMgr.readRole(AdminRoleTestData.getRole(rle));
                AdminRoleTestData.assertEquals(entity, rle);
                log.debug(CLS_NM + ".readAdminRoles [" + entity.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".readAdminRoles caught SecurityException=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testSearchAdminRole()
    {
        searchAdminRoles("SRCH-ADMRLS TR1", TestUtils.getSrchValue(RoleTestData.getName(AdminRoleTestData.AROLES_TR1[0])), AdminRoleTestData.AROLES_TR1);
        searchAdminRoles("SRCH-ADMRLS TR2", TestUtils.getSrchValue(RoleTestData.getName(AdminRoleTestData.AROLES_TR2[0])), AdminRoleTestData.AROLES_TR2);
        searchAdminRoles("SRCH-ADMRLS TR3", TestUtils.getSrchValue(RoleTestData.getName(AdminRoleTestData.AROLES_TR3_UPD[0])), AdminRoleTestData.AROLES_TR3_UPD);
    }

    /**
     *
     * @param msg
     * @param srchValue
     * @param rArray
     */
    public void searchAdminRoles(String msg, String srchValue, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelReviewMgr dReviewMgr = getManagedDelegatedReviewMgr();
            List<AdminRole> roles = dReviewMgr.findRoles(srchValue);
            assertNotNull(roles);
            assertTrue(CLS_NM + "searchAdminRoles list size check", rArray.length == roles.size());
            for (String[] rle : rArray)
            {
                int indx = roles.indexOf(AdminRoleTestData.getRole(rle));
                if (indx != -1)
                {
                    AdminRole entity = roles.get(indx);
                    assertNotNull(entity);
                    AdminRoleTestData.assertEquals(entity, rle);
                    log.debug(CLS_NM + ".searchAdminRoles [" + entity.getName() + "] successful");
                }
                else
                {
                    msg = CLS_NM + ".searchAdminRoles srchValue [" + srchValue + "] failed list search";
                    LogUtil.logIt(msg);
                    fail(msg);
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".searchAdminRoles srchValue [" + srchValue + "] caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testAddOrgUnitDescendant()
    {
        addOrgUnitDescendant("ADD ORGS-USR-TO6-DESC", OrgUnitTestData.ORGS_USR_TO6_DSC, OrgUnit.Type.USER);
        addOrgUnitDescendant("ADD ORGS-PRM-TO6-DESC", OrgUnitTestData.ORGS_PRM_TO6_DSC, OrgUnit.Type.PERM);
    }


    /**
     *
     * @param msg
     * @param oArray
     */
    private void addOrgUnitDescendant(String msg, String[][] oArray, OrgUnit.Type type)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            int ctr = 0;
            for (String[] ole : oArray)
            {
                OrgUnit orgUnit = OrgUnitTestData.getOrgUnit(ole);
                if(ctr++ == 0 || OrgUnitTestData.isTree(ole))
                {
                    dAdminMgr.add(orgUnit);
                    log.debug(CLS_NM + ".addOrgUnitDescendant add orgUnit [" + orgUnit.getName() + "] successful");
                }

                // use list because order is important for test structure:
                List<String> descs = OrgUnitTestData.getRelationshipList(ole);
                if (descs != null)
                {
                    if (OrgUnitTestData.isTree(ole))
                    {
                        OrgUnit parent = orgUnit;
                        for (String desc : descs)
                        {
                            OrgUnit child = new OrgUnit(desc, type);
                            dAdminMgr.addDescendant(parent, new OrgUnit(desc, type));
                            log.debug(CLS_NM + ".addOrgUnitDescendant asc orgUnit [" + orgUnit.getName() + "] desc orgUnit [" + desc + "] successful");
                            parent = child;
                        }
                    }
                    else
                    {
                        for (String desc : descs)
                        {
                            dAdminMgr.addDescendant(orgUnit, new OrgUnit(desc, type));
                            log.debug(CLS_NM + ".addOrgUnitDescendant asc orgUnit [" + orgUnit.getName() + "] desc orgUnit [" + desc + "] successful");
                        }
                    }
                }

                Set<String> inheritances = OrgUnitTestData.getInheritances(ole);
                if (inheritances != null)
                {
                    for (String desc : inheritances)
                    {
                        dAdminMgr.addInheritance(orgUnit, new OrgUnit(desc, type));
                        log.debug(CLS_NM + ".addOrgUnitDescendant asc orgUnit [" + orgUnit.getName() + "] desc orgUnit [" + desc + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addOrgUnitDescendant caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDelOrgUnitDescendant() throws SecurityException
    {
        delOrgUnitDescendant("DEL-ORGS-USR-TO6-DESC", OrgUnitTestData.ORGS_USR_TO6_DSC, OrgUnit.Type.USER);
        delOrgUnitDescendant("DEL-ORGS-PRM-TO6-DESC", OrgUnitTestData.ORGS_PRM_TO6_DSC, OrgUnit.Type.PERM);
    }


    /**
     *
     * @param msg
     */
    private void delOrgUnitDescendant(String msg, String[][] oArray, OrgUnit.Type type)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            for (String[] ole : oArray)
            {
                OrgUnit orgUnit = OrgUnitTestData.getOrgUnit(ole);
                // use list because order is important for test structure:
                List<String> descs = OrgUnitTestData.getRelationshipList(ole);
                if (descs != null)
                {
                    if (OrgUnitTestData.isTree(ole))
                    {
                        OrgUnit parent = orgUnit;
                        for (String desc : descs)
                        {
                            OrgUnit child = new OrgUnit(desc);
                            dAdminMgr.deleteInheritance(parent, new OrgUnit(desc, type));
                            log.debug(CLS_NM + ".delOrgUnitDescendant asc orgUnit [" + orgUnit.getName() + "] desc orgUnit [" + desc + "] successful");
                            parent = child;
                        }
                    }
                    else
                    {
                        for (String desc : descs)
                        {
                            dAdminMgr.deleteInheritance(orgUnit, new OrgUnit(desc, type));
                            log.debug(CLS_NM + ".delOrgUnitDescendant asc orgUnit [" + orgUnit.getName() + "] desc orgUnit [" + desc + "] successful");
                        }
                    }
                }

                Set<String> inheritances = OrgUnitTestData.getInheritances(ole);
                if (inheritances != null)
                {
                    for (String desc : inheritances)
                    {
                        dAdminMgr.deleteInheritance(orgUnit, new OrgUnit(desc, type));
                        log.debug(CLS_NM + ".delOrgUnitDescendant asc orgUnit [" + orgUnit.getName() + "] desc orgUnit [" + desc + "] successful");
                    }
                }
                dAdminMgr.delete(orgUnit);
                log.debug(CLS_NM + ".delOrgUnitDescendant remove asc orgUnit [" + orgUnit.getName() + "] successful");
            }

            // cleanup the last row of descendants from orgUnit data set.
            DelReviewMgr dReviewMgr = getManagedDelegatedReviewMgr();
            String orgUnitSrchVal = TestUtils.getSrchValue(OrgUnitTestData.getName(oArray[0]));
            List<OrgUnit> cleanup = dReviewMgr.search(type, orgUnitSrchVal);
            for(OrgUnit oe : cleanup)
            {
                dAdminMgr.delete(oe);
                log.debug(CLS_NM + ".delOrgUnitDescendant cleanup orgUnit [" + oe.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".delOrgUnitDescendant caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }



    public void testAddOrgUnitAscendants()
    {
        addOrgUnitAscendant("ADD-ORGS-USR-TR7-ASC", OrgUnitTestData.ORGS_USR_TO7_ASC, OrgUnit.Type.USER);
        addOrgUnitAscendant("ADD-ORGS-PRM-TR7-ASC", OrgUnitTestData.ORGS_PRM_TO7_ASC, OrgUnit.Type.PERM);


    }

    /**
     *
     * @param msg
     * @param oArray
     * @param type
     */
    private void addOrgUnitAscendant(String msg, String[][] oArray, OrgUnit.Type type)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            int ctr = 0;
            for (String[] ole : oArray)
            {
                OrgUnit orgUnit = OrgUnitTestData.getOrgUnit(ole);
                if(OrgUnitTestData.isCreate(ole))
                {
                    dAdminMgr.add(orgUnit);
                    log.debug(CLS_NM + ".addOrgUnitAscendant add orgUnit [" + orgUnit.getName() + "] successful");
                }

                Set<String> ascs = OrgUnitTestData.getRelationships(ole);
                if (ascs != null)
                {
                    for (String asc : ascs)
                    {
                        dAdminMgr.addAscendant(orgUnit, new OrgUnit(asc, type));
                        log.debug(CLS_NM + ".addOrgUnitAscendant desc role [" + orgUnit.getName() + "] asc role [" + asc + "] successful");
                    }
                }

                Set<String> inheritances = OrgUnitTestData.getInheritances(ole);
                if (inheritances != null)
                {
                    for (String asc : inheritances)
                    {
                        dAdminMgr.addInheritance(new OrgUnit(asc, type), orgUnit);
                        log.debug(CLS_NM + ".addOrgUnitAscendant desc role [" + orgUnit.getName() + "] asc role [" + asc + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addOrgUnitAscendant caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDelOrgUnitAscendant()
    {
        delOrgUnitAscendant("DEL-ORGS-USR-TR7-ASC", OrgUnitTestData.ORGS_USR_TO7_ASC, OrgUnit.Type.USER);
        delOrgUnitAscendant("DEL-ORGS-PRM-TR7-ASC", OrgUnitTestData.ORGS_PRM_TO7_ASC, OrgUnit.Type.PERM);
    }


    /**
     *
     * @param msg
     * @param oArray
     */
    private void delOrgUnitAscendant(String msg, String[][] oArray, OrgUnit.Type type)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            for (String[] ole : oArray)
            {
                OrgUnit orgUnit = OrgUnitTestData.getOrgUnit(ole);
                Set<String> ascs = OrgUnitTestData.getRelationships(ole);
                if (ascs != null)
                {
                    for (String asc : ascs)
                    {
                        dAdminMgr.deleteInheritance(new OrgUnit(asc, type), orgUnit);
                        log.debug(CLS_NM + ".delOrgUnitAscendant desc orgUnit [" + orgUnit.getName() + "] asc orgUnit [" + asc + "] successful");
                    }
                }

                Set<String> inheritances = OrgUnitTestData.getInheritances(ole);
                if (inheritances != null)
                {
                    for (String asc : inheritances)
                    {
                        dAdminMgr.deleteInheritance(new OrgUnit(asc, type), orgUnit);
                        log.debug(CLS_NM + ".delOrgUnitAscendant desc orgUnit [" + orgUnit.getName() + "] asc orgUnit [" + asc + "] successful");
                    }
                }
                dAdminMgr.delete(orgUnit);
                log.debug(CLS_NM + ".delOrgUnitAscendant remove desc orgUnit [" + orgUnit.getName() + "] successful");
            }

            // cleanup the top ascendant from orgUnit data set.
            DelReviewMgr dReviewMgr = getManagedDelegatedReviewMgr();
            String orgUnitSrchVal = OrgUnitTestData.getName(oArray[0]);
            // stip off prefix and search:
            orgUnitSrchVal = orgUnitSrchVal.substring(0,3);
            List<OrgUnit> cleanup = dReviewMgr.search(type, orgUnitSrchVal);
            for(OrgUnit oe : cleanup)
            {
                dAdminMgr.delete(oe);
                log.debug(CLS_NM + ".delOrgUnitAscendant cleanup orgUnit [" + oe.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".delOrgUnitAscendant caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testAddAdminRoleDescendant()
    {
        addAdminRoleDescendant("ADD-ARLS-TR5-DESC", AdminRoleTestData.AROLES_TR5_DSC);
    }


    /**
     *
     * @param msg
     * @param rArray
     */
    private void addAdminRoleDescendant(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            int ctr = 0;
            for (String[] rle : rArray)
            {
                AdminRole adminRole = AdminRoleTestData.getRole(rle);
                if(ctr++ == 0 || RoleTestData.isTree(rle))
                {
                    dAdminMgr.addRole(adminRole);
                    log.debug(CLS_NM + ".addAdminRoleDescendant add role [" + adminRole.getName() + "] successful");
                }

                // use list because order is important for test structure:
                List<String> descs = RoleTestData.getRelationshipList(rle);
                if (descs != null)
                {
                    if (RoleTestData.isTree(rle))
                    {
                        AdminRole parent = adminRole;
                        for (String desc : descs)
                        {
                            AdminRole child = new AdminRole(desc);
                            dAdminMgr.addDescendant(parent, new AdminRole(desc));
                            log.debug(CLS_NM + ".addAdminRoleDescendant asc role [" + adminRole.getName() + "] desc role [" + desc + "] successful");
                            parent = child;
                        }
                    }
                    else
                    {
                        for (String desc : descs)
                        {
                            dAdminMgr.addDescendant(adminRole, new AdminRole(desc));
                            log.debug(CLS_NM + ".addAdminRoleDescendant asc role [" + adminRole.getName() + "] desc role [" + desc + "] successful");
                        }
                    }
                }

                Set<String> inheritances = RoleTestData.getInheritances(rle);
                if (inheritances != null)
                {
                    for (String desc : inheritances)
                    {
                        dAdminMgr.addInheritance(adminRole, new AdminRole(desc));
                        log.debug(CLS_NM + ".addAdminRoleDescendant asc role [" + adminRole.getName() + "] desc role [" + desc + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addAdminRoleDescendant caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDelAdminRoleDescendant() throws SecurityException
    {
        delAdminRoleDescendant("DEL-ARLS-TR5-DESC", AdminRoleTestData.AROLES_TR5_DSC);
    }


    private void delAdminRoleDescendant(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            for (String[] rle : rArray)
            {
                AdminRole adminRole = AdminRoleTestData.getRole(rle);
                // use list because order is important for test structure:
                List<String> descs = RoleTestData.getRelationshipList(rle);
                if (descs != null)
                {
                    if (RoleTestData.isTree(rle))
                    {
                        AdminRole parent = adminRole;
                        for (String desc : descs)
                        {
                            AdminRole child = new AdminRole(desc);
                            dAdminMgr.deleteInheritance(parent, new AdminRole(desc));
                            log.debug(CLS_NM + ".delAdminRoleDescendant asc adminRole [" + adminRole.getName() + "] desc adminRole [" + desc + "] successful");
                            parent = child;
                        }
                    }
                    else
                    {
                        for (String desc : descs)
                        {
                            dAdminMgr.deleteInheritance(adminRole, new AdminRole(desc));
                            log.debug(CLS_NM + ".delAdminRoleDescendant asc adminRole [" + adminRole.getName() + "] desc adminRole [" + desc + "] successful");
                        }
                    }
                }

                Set<String> inheritances = RoleTestData.getInheritances(rle);
                if (inheritances != null)
                {
                    for (String desc : inheritances)
                    {
                        dAdminMgr.deleteInheritance(adminRole, new AdminRole(desc));
                        log.debug(CLS_NM + ".delAdminRoleDescendant asc adminRole [" + adminRole.getName() + "] desc adminRole [" + desc + "] successful");
                    }
                }
                dAdminMgr.deleteRole(adminRole);
                log.debug(CLS_NM + ".delAdminRoleDescendant remove asc adminRole [" + adminRole.getName() + "] successful");
            }

            // cleanup the last row of descendants from orgUnit data set.
            DelReviewMgr dReviewMgr = getManagedDelegatedReviewMgr();
            String srchVal = TestUtils.getSrchValue(RoleTestData.getName(rArray[0]));
            List<AdminRole> cleanup = dReviewMgr.findRoles(srchVal);
            for(AdminRole re : cleanup)
            {
                dAdminMgr.deleteRole(re);
                log.debug(CLS_NM + ".delAdminRoleDescendant cleanup adminRole [" + re.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".delOrgUnitDescendant caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }



    public void testAddAdminRoleAscendants()
    {
        addAdminRoleAscendant("ADD-ARLS-TR4-ASC", AdminRoleTestData.AROLES_TR4_ASC);
    }

    /**
     *
     * @param msg
     * @param rArray
     */
    private void addAdminRoleAscendant(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            for (String[] rle : rArray)
            {
                AdminRole adminRole = AdminRoleTestData.getRole(rle);
                if(RoleTestData.isCreate(rle))
                {
                    dAdminMgr.addRole(adminRole);
                    log.debug(CLS_NM + ".addAdminRoleAscendant add adminRole [" + adminRole.getName() + "] successful");
                }
                Set<String> ascs = RoleTestData.getRelationships(rle);
                if (ascs != null)
                {
                    for (String asc : ascs)
                    {
                        dAdminMgr.addAscendant(adminRole, new AdminRole(asc));
                        log.debug(CLS_NM + ".addAdminRoleAscendant desc role [" + adminRole.getName() + "] asc role [" + asc + "] successful");
                    }
                }
                Set<String> inheritances = RoleTestData.getInheritances(rle);
                if (inheritances != null)
                {
                    for (String asc : inheritances)
                    {
                        dAdminMgr.addInheritance(new AdminRole(asc), adminRole);
                        log.debug(CLS_NM + ".addAdminRoleAscendant desc role [" + adminRole.getName() + "] asc role [" + asc + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addAdminRoleAscendant caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDelAdminRoleAscendant()
    {
        delAdminRoleAscendant("DEL-ARLS-TR4-ASC", AdminRoleTestData.AROLES_TR4_ASC);
    }


    /**
     *
     * @param msg
     * @param rArray
     */
    private void delAdminRoleAscendant(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            for (String[] rle : rArray)
            {
                AdminRole adminRole = AdminRoleTestData.getRole(rle);
                Set<String> ascs = RoleTestData.getRelationships(rle);
                if (ascs != null)
                {
                    for (String asc : ascs)
                    {
                        dAdminMgr.deleteInheritance(new AdminRole(asc), adminRole);
                        log.debug(CLS_NM + ".delAdminRoleAscendant desc adminRole [" + adminRole.getName() + "] asc adminRole [" + asc + "] successful");
                    }
                }

                Set<String> inheritances = RoleTestData.getInheritances(rle);
                if (inheritances != null)
                {
                    for (String asc : inheritances)
                    {
                        dAdminMgr.deleteInheritance(new AdminRole(asc), adminRole);
                        log.debug(CLS_NM + ".delAdminRoleAscendant desc adminRole [" + adminRole.getName() + "] asc orgUnit [" + asc + "] successful");
                    }
                }
                dAdminMgr.deleteRole(adminRole);
                log.debug(CLS_NM + ".delAdminRoleAscendant remove desc adminRole [" + adminRole.getName() + "] successful");
            }

            // cleanup the top ascendant from orgUnit data set.
            DelReviewMgr dReviewMgr = getManagedDelegatedReviewMgr();
            String adminRoleSrchVal = RoleTestData.getName(rArray[0]);
            // stip off prefix and search:
            adminRoleSrchVal = adminRoleSrchVal.substring(0,3);
            List<AdminRole> cleanup = dReviewMgr.findRoles(adminRoleSrchVal);
            for(AdminRole re : cleanup)
            {
                dAdminMgr.deleteRole(re);
                log.debug(CLS_NM + ".delAdminRoleAscendant cleanup adminRole [" + re.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".delAdminRoleAscendant caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testAddAdminRoleInheritance()
    {
        addInheritedAdminRoles("ADD-ARLS-TR6-HIER", AdminRoleTestData.AROLES_TR6_HIER);
    }


    /**
     *
     * @param msg
     * @param rArray
     */
    public static void addInheritedAdminRoles(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            for (String[] rle : rArray)
            {
                AdminRole role = AdminRoleTestData.getRole(rle);
                Set<String> parents = RoleTestData.getRelationships(rle);
                if (parents != null)
                {
                    for (String pRole : parents)
                    {
                        dAdminMgr.addInheritance(new AdminRole(pRole), role);
                        log.debug(CLS_NM + ".addInheritedAdminRoles child role [" + role.getName() + "] parent role [" + pRole + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addInheritedAdminRoles caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDeleteAdminRoleInheritance()
    {
        deleteInheritedAdminRoles("DEL-ARLS-TR6-HIER", AdminRoleTestData.AROLES_TR6_HIER);
    }


    /**
     *
     * @param msg
     * @param rArray
     */
    public static void deleteInheritedAdminRoles(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            DelAdminMgr dAdminMgr = getManagedDelegatedMgr();
            for (String[] rle : rArray)
            {
                AdminRole role = AdminRoleTestData.getRole(rle);
                Set<String> parents = RoleTestData.getRelationships(rle);
                if (parents != null)
                {
                    for (String pRole : parents)
                    {
                        dAdminMgr.deleteInheritance(new AdminRole(pRole), role);
                        log.debug(CLS_NM + ".deleteInheritedAdminRoles child role [" + role.getName() + "] parent role [" + pRole + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deleteInheritedAdminRoles caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     *
     * @return
     * @throws SecurityException
     */
    public static DelReviewMgr getManagedDelegatedReviewMgr() throws SecurityException
    {
        DelReviewMgr dReviewMgr = DelReviewMgrFactory.createInstance(contextId);
        if(FortressJUnitTest.isAdminEnabled() && adminSess == null)
        {
            adminSess = DelegatedMgrImplTest.createAdminSession();
        }
        dReviewMgr.setAdmin(adminSess);
        return dReviewMgr;
    }

    /**
     *
     * @return
     * @throws SecurityException
     */
    public static DelAdminMgr getManagedDelegatedMgr() throws SecurityException
    {
        if(FortressJUnitTest.isAdminEnabled() && adminSess == null)
        {
            adminSess = DelegatedMgrImplTest.createAdminSession();
        }
        DelAdminMgr dAdminMgr = DelAdminMgrFactory.createInstance(contextId, adminSess);
        return dAdminMgr;
    }

    /**
     *
     * @return
     */
    public static boolean loadAdminPolicy()
    {
        boolean result = true;
        try
        {
            ReviewMgr rMgr = ReviewMgrFactory.createInstance(contextId);
            User admin = UserTestData.getUser(UserTestData.USERS_TU0[0]);
            rMgr.readUser(admin);
            result = false;
        }
        catch(SecurityException se)
        {
            String info = CLS_NM + " loadAdminPolicy detected admin policy not loaded";
            log.debug(info);
        }
        return result;
    }
    /**
     *
     */
    public static Session createAdminSession()
    {
        Session adminSess = null;
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance(contextId);
            User admin = UserTestData.getUser(UserTestData.USERS_TU0[0]);
            adminSess = accessMgr.createSession(admin, false);
        }
        catch(SecurityException ex)
        {
            String error = CLS_NM + " static initializer caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage();
            log.error(error);
        }
        return adminSess;
    }
}
