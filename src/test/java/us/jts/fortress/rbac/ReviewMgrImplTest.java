/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.SecurityException;
import us.jts.fortress.ReviewMgr;
import us.jts.fortress.ReviewMgrFactory;

import us.jts.fortress.util.LogUtil;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ReviewMgrImpl Tester.
 *
 * @author Shawn McKinney
 * @version 1.0
 */
public class ReviewMgrImplTest extends TestCase
{
    private static final String CLS_NM = ReviewMgrImplTest.class.getName();
    final protected static Logger log = Logger.getLogger(CLS_NM);
    static Session adminSess = null;

    public ReviewMgrImplTest(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();

        suite.addTest(new ReviewMgrImplTest("testReadPermissionOp"));
        suite.addTest(new ReviewMgrImplTest("testFindPermissionOps"));
        suite.addTest(new ReviewMgrImplTest("testReadPermissionObj"));
        suite.addTest(new ReviewMgrImplTest("testFindPermissionObjs"));
        suite.addTest(new ReviewMgrImplTest("testReadRole"));
        suite.addTest(new ReviewMgrImplTest("testFindRoles"));
        suite.addTest(new ReviewMgrImplTest("testReadUser"));
        suite.addTest(new ReviewMgrImplTest("testFindUsers"));
        suite.addTest(new ReviewMgrImplTest("testAssignedRoles"));
        suite.addTest(new ReviewMgrImplTest("testAuthorizedUsers"));
        suite.addTest(new ReviewMgrImplTest("testAuthorizedRoles"));
        suite.addTest(new ReviewMgrImplTest("testRolePermissions"));
        suite.addTest(new ReviewMgrImplTest("testUserPermissions"));
        suite.addTest(new ReviewMgrImplTest("testPermissionRoles"));
        suite.addTest(new ReviewMgrImplTest("testAuthorizedPermissionRoles"));
        suite.addTest(new ReviewMgrImplTest("testPermissionUsers"));
        suite.addTest(new ReviewMgrImplTest("testAuthorizedPermissionUsers"));
        return suite;
    }


    public void setUp() throws Exception
    {
        super.setUp();
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    public static Test suitex()
    {
        return new TestSuite(ReviewMgrImplTest.class);
    }

    public void testReadPermissionOp()
    {
        //    public Permission readPermission(Permission permOp)
        readPermissionOps("RD-PRM-OPS TOB1 OPS_TOP1_UPD", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1_UPD);
        readPermissionOps("RD-PRM-OPS TOB1 TOP2", PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2);
        readPermissionOps("RD-PRM-OPS TOB1 TOP3", PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3);
    }

    /**
     * @param msg
     * @param pObjArray
     * @param pOpArray
     */
    public void readPermissionOps(String msg, String[][] pObjArray, String[][] pOpArray)
    {
        Permission pOp = new Permission();
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] objs : pObjArray)
            {
                for (String[] ops : pOpArray)
                {
                    pOp = new Permission();
                    pOp.setObjectName(PermTestData.getName(objs));
                    pOp.setOpName(PermTestData.getName(ops));
                    pOp.setObjectId(PermTestData.getObjectId(ops));
                    Permission entity = reviewMgr.readPermission(pOp);
                    assertNotNull(entity);
                    PermTestData.assertEquals(PermTestData.getName(objs), entity, ops);
                    log.debug(CLS_NM + ".readPermissionOps object name [" + pOp.getObjectName() + "] operation name [" + pOp.getOpName() + "] objectId [" + pOp.getObjectId() + "] successful");
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".readPermissionOps object name [" + pOp.getObjectName() + "] operation name [" + pOp.getOpName() + "] objectId [" + pOp.getObjectId() + "] caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage() + ex);
            fail(ex.getMessage());
        }
    }

    public void testFindPermissionOps()
    {
        // public List<Permission> findPermissions(Permission permOp)
        searchPermissionOps("FND-PRM-OPS TOB1 OPS_TOP1_UPD", TestUtils.getSrchValue(PermTestData.getName(PermTestData.OPS_TOP1_UPD[0])), PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1_UPD);
        searchPermissionOps("FND-PRM-OPS TOB2 TOP2", TestUtils.getSrchValue(PermTestData.getName(PermTestData.OPS_TOP2[0])), PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2);
        searchPermissionOps("FND-PRM-OPS TOB3 TOP3", TestUtils.getSrchValue(PermTestData.getName(PermTestData.OPS_TOP3[0])), PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3);
    }

    /**
     * @param msg
     * @param srchValue
     * @param pObjArray
     */
    public void searchPermissionOps(String msg, String srchValue, String[][] pObjArray, String[][] pOpArray)
    {
        LogUtil.logIt(msg);
        Permission pOp;
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] obj : pObjArray)
            {
                for (String[] op : pOpArray)
                {
                    pOp = new Permission();
                    pOp.setObjectName(PermTestData.getName(obj));
                    pOp.setOpName(srchValue);
                    List<Permission> ops = reviewMgr.findPermissions(pOp);
                    assertNotNull(ops);
                    assertTrue(CLS_NM + "searchPermissionOps srchValue [" + srchValue + "] list size check", pOpArray.length == ops.size());

                    int indx = ops.indexOf(new Permission(PermTestData.getName(obj), PermTestData.getName(op)));
                    if (indx != -1)
                    {
                        Permission entity = ops.get(indx);
                        assertNotNull(entity);
                        PermTestData.assertEquals(PermTestData.getName(obj), entity, op);
                        log.debug(CLS_NM + ".searchPermissionOps objectName [" + entity.getObjectName() + "] operation name [" + entity.getOpName() + "] successful");
                    }
                    else
                    {
                        msg = CLS_NM + ".searchPermissionOps srchValue [" + srchValue + "] failed list search";
                        LogUtil.logIt(msg);
                        fail(msg);
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".searchPermissionOps srchValue [" + srchValue + "] caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testReadPermissionObj()
    {
        //    public Permission readPermission(Permission permOp)
        readPermissionObjs("RD-PRM-OBJS TOB1", PermTestData.OBJS_TOB1);
        readPermissionObjs("RD-PRM-OBJS TOB2", PermTestData.OBJS_TOB2);
        readPermissionObjs("RD-PRM-OBJS TOB3", PermTestData.OBJS_TOB3);
        readPermissionObjs("RD-PRM-OBJS TOB4_UPD", PermTestData.OBJS_TOB4_UPD);
    }

    /**
     * @param msg
     * @param pArray
     */
    public void readPermissionObjs(String msg, String[][] pArray)
    {
        PermObj pObj = new PermObj();
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] objs : pArray)
            {
                pObj = new PermObj();
                pObj.setObjectName(PermTestData.getName(objs));
                PermObj entity = reviewMgr.readPermObj(pObj);
                assertNotNull(entity);
                PermTestData.assertEquals(entity, objs);
                log.debug(CLS_NM + ".readPermissionObjs object name [" + pObj.getObjectName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".readPermissionOps object name [" + pObj.getObjectName() + "] caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage() + ex);
            fail(ex.getMessage());
        }
    }

    public void testFindPermissionObjs()
    {
        // public List<Permission> findPermissions(Permission permOp)
        searchPermissionObjs("FND-PRM-OBJS TOB1", TestUtils.getSrchValue(PermTestData.getName(PermTestData.OBJS_TOB1[0])), PermTestData.OBJS_TOB1);
        searchPermissionObjs("FND-PRM-OBJS TOB2", TestUtils.getSrchValue(PermTestData.getName(PermTestData.OBJS_TOB2[0])), PermTestData.OBJS_TOB2);
        searchPermissionObjs("FND-PRM-OBJS TOB3", TestUtils.getSrchValue(PermTestData.getName(PermTestData.OBJS_TOB3[0])), PermTestData.OBJS_TOB3);
        searchPermissionObjs("FND-PRM-OBJS TOB4_UPD", TestUtils.getSrchValue(PermTestData.getName(PermTestData.OBJS_TOB4_UPD[0])), PermTestData.OBJS_TOB4_UPD);
    }

    /**
     * @param msg
     * @param srchValue
     * @param pArray
     */
    public void searchPermissionObjs(String msg, String srchValue, String[][] pArray)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            List<PermObj> objs = reviewMgr.findPermObjs(new PermObj(srchValue));
            assertNotNull(objs);
            assertTrue(CLS_NM + "searchPermissionObjs srchValue [" + srchValue + "] list size check", pArray.length == objs.size());
            for (String[] obj : pArray)
            {
                int indx = objs.indexOf(new PermObj(PermTestData.getName(obj)));
                if (indx != -1)
                {
                    PermObj entity = objs.get(indx);
                    assertNotNull(entity);
                    PermTestData.assertEquals(entity, obj);
                    log.debug(CLS_NM + ".searchPermissionObjs [" + entity.getObjectName() + "] successful");
                }
                else
                {
                    msg = CLS_NM + ".searchPermissionObjs srchValue [" + srchValue + "] failed list search";
                    LogUtil.logIt(msg);
                    fail(msg);
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".searchPermissionObjs srchValue [" + srchValue + "] caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testReadRole()
    {
        // public Role readRole(Role role)
        readRoles("RD-RLS TR1", RoleTestData.ROLES_TR1);
        readRoles("RD-RLS TR2", RoleTestData.ROLES_TR2);
        readRoles("RD-RLS TR3", RoleTestData.ROLES_TR3);
        readRoles("RD-RLS TR4_UPD", RoleTestData.ROLES_TR4_UPD);
    }

    /**
     * Determine if old fortress regression test cases are loaded in this directory and must be torn down.
     *
     * @param msg
     * @param rArray
     */
    public static boolean teardownRequired(String msg, String[][] rArray)
    {
        // default return is 'true':
        boolean tearDown = true;
        String methodName = CLS_NM + ".teardownRequired";
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] rle : rArray)
            {
                Role entity = reviewMgr.readRole(new Role(RoleTestData.getName(rle)));
                RoleTestData.assertEquals(entity, rle);
            }
            // if we get to here it means that old test data must be removed from directory.
        }
        catch (SecurityException ex)
        {
            // This is the expected when teardown is not required:
            if(ex.getErrorId() == GlobalErrIds.ROLE_NOT_FOUND)
            {
                // did not find old test data no need to teardown
                tearDown = false;
            }
            else
            {
                // Something unexpected occurred here, Report as warning to the logger:
                String warning = methodName + " caught SecurityException=" + ex.getMessage();
                log.warn(warning);
                // TODO: Determine if it would be better to throw a SecurityException here.
            }
        }
        log.info(methodName + ":" + tearDown);
        return tearDown;
    }

    /**
     *
     * @param msg
     * @param rArray
     */
    public void readRoles(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] rle : rArray)
            {
                Role entity = reviewMgr.readRole(new Role(RoleTestData.getName(rle)));
                RoleTestData.assertEquals(entity, rle);
                log.debug(CLS_NM + ".readRoles [" + entity.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".readRoles caught SecurityException=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * RMT06
     *
     * @throws us.jts.fortress.SecurityException
     */
    public void testFindRoles()
    {
        // public List<Role> findRoles(String searchVal)
        searchRoles("SRCH-RLS TR1", TestUtils.getSrchValue(RoleTestData.getName(RoleTestData.ROLES_TR1[0])), RoleTestData.ROLES_TR1);
        searchRoles("SRCH-RLS TR2", TestUtils.getSrchValue(RoleTestData.getName(RoleTestData.ROLES_TR2[0])), RoleTestData.ROLES_TR2);
        searchRoles("SRCH-RLS TR3", TestUtils.getSrchValue(RoleTestData.getName(RoleTestData.ROLES_TR3[0])), RoleTestData.ROLES_TR3);
        searchRoles("SRCH-RLS TR4_UPD", TestUtils.getSrchValue(RoleTestData.getName(RoleTestData.ROLES_TR4[0])), RoleTestData.ROLES_TR4_UPD);
    }

    /**
     *
     * @param msg
     * @param srchValue
     * @param rArray
     */
    public void searchRoles(String msg, String srchValue, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            List<Role> roles = reviewMgr.findRoles(srchValue);
            assertNotNull(roles);
            assertTrue(CLS_NM + "searchRoles list size check", rArray.length == roles.size());
            for (String[] rle : rArray)
            {
                int indx = roles.indexOf(new Role(RoleTestData.getName(rle)));
                if (indx != -1)
                {
                    Role entity = roles.get(indx);
                    assertNotNull(entity);
                    RoleTestData.assertEquals(entity, rle);
                    log.debug(CLS_NM + ".searchRoles [" + entity.getName() + "] successful");
                }
                else
                {
                    msg = CLS_NM + ".searchRoles srchValue [" + srchValue + "] failed list search";
                    LogUtil.logIt(msg);
                    fail(msg);
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".searchRoles srchValue [" + srchValue + "] caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testReadUser()
    {
        // public User readUser(User user)
        readUsers("READ-USRS TU1_UPD", UserTestData.USERS_TU1_UPD);
        readUsers("READ-USRS TU3", UserTestData.USERS_TU3);
        readUsers("READ-USRS TU4", UserTestData.USERS_TU4);
        readUsers("READ-USRS TU5", UserTestData.USERS_TU5);
        readUsers("READ-USRS TU8", UserTestData.USERS_TU8_SSD);
        readUsers("READ-USRS TU9", UserTestData.USERS_TU9_SSD_HIER);
        readUsers("READ-USRS TU10", UserTestData.USERS_TU10_SSD_HIER);
        readUsers("READ-USRS TU11", UserTestData.USERS_TU11_SSD_HIER);
        readUsers("READ-USRS TU12", UserTestData.USERS_TU12_DSD);
        readUsers("READ-USRS TU13", UserTestData.USERS_TU13_DSD_HIER);
        readUsers("READ-USRS TU14", UserTestData.USERS_TU14_DSD_HIER);
        readUsers("READ-USRS TU15", UserTestData.USERS_TU15_DSD_HIER);
    }

    /**
     *
     * @param msg
     * @param uArray
     */
    public void readUsers(String msg, String[][] uArray)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] usr : uArray)
            {
                User entity = reviewMgr.readUser(new User(UserTestData.getUserId(usr)));
                assertNotNull(entity);
                UserTestData.assertEquals(entity, usr);
                log.debug(CLS_NM + ".readUsers userId [" + entity.getUserId() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".readUsers caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testFindUsers()
    {
        // public List<User> findUsers(User user)
        searchUsers("SRCH-USRS TU1_UPD", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU1[0])), UserTestData.USERS_TU1_UPD);
        searchUsers("SRCH-USRS TU3", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU3[0])), UserTestData.USERS_TU3);
        searchUsers("SRCH-USRS TU4", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU4[0])), UserTestData.USERS_TU4);
        searchUsers("SRCH-USRS TU5", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU5[0])), UserTestData.USERS_TU5);
        searchUsers("SRCH-USRS TU8", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU8_SSD[0])), UserTestData.USERS_TU8_SSD);
        searchUsers("SRCH-USRS TU9", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU9_SSD_HIER[0])), UserTestData.USERS_TU9_SSD_HIER);
        searchUsers("SRCH-USRS TU10", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU10_SSD_HIER[0])), UserTestData.USERS_TU10_SSD_HIER);
        searchUsers("SRCH-USRS TU11", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU11_SSD_HIER[0])), UserTestData.USERS_TU11_SSD_HIER);        
        searchUsers("SRCH-USRS TU12", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU12_DSD[0])), UserTestData.USERS_TU12_DSD);
        searchUsers("SRCH-USRS TU13", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU13_DSD_HIER[0])), UserTestData.USERS_TU13_DSD_HIER);
        searchUsers("SRCH-USRS TU14", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU14_DSD_HIER[0])), UserTestData.USERS_TU14_DSD_HIER);
        searchUsers("SRCH-USRS TU15", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU15_DSD_HIER[0])), UserTestData.USERS_TU15_DSD_HIER);        
    }

    /**
     *
     * @param msg
     * @param srchValue
     * @param uArray
     */
    public void searchUsers(String msg, String srchValue, String[][] uArray)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            List<User> users = reviewMgr.findUsers(new User(srchValue));
            assertNotNull(users);
            assertTrue(CLS_NM + "searchUsers list size check", uArray.length == users.size());
            for (String[] usr : uArray)
            {
                int indx = users.indexOf(new User(UserTestData.getUserId(usr)));
                if (indx != -1)
                {
                    User entity = users.get(indx);
                    assertNotNull(entity);
                    UserTestData.assertEquals(entity, usr);
                    log.debug(CLS_NM + ".searchUsers userId [" + entity.getUserId() + "] successful");
                }
                else
                {
                    msg = CLS_NM + ".searchUsers srchValue [" + srchValue + "] failed list search";
                    LogUtil.logIt(msg);
                    fail(msg);
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".searchUsers srchValue [" + srchValue + "] caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testAssignedRoles()
    {
        // public List<UserRole> assignedRoles(User userId)
        assignedRoles("ASGN-RLS TU1_UPD TR1", UserTestData.USERS_TU1_UPD, RoleTestData.ROLES_TR1);
        //assignedRoles("ASGN-RLS TU3 TR2", UserTestData.USERS_TU3, RoleTestData.ROLES_TR2);
        assignedRoles("ASGN-RLS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2);
        assignedRoles("ASGN-RLS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3);
    }


    /**
     * @param msg
     * @param uArray
     * @param rArray
     */
    public void assignedRoles(String msg, String[][] uArray, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] usr : uArray)
            {
                User user = reviewMgr.readUser(new User(UserTestData.getUserId(usr)));
                assertNotNull(user);
                List<UserRole> uRoles = reviewMgr.assignedRoles(user);
                assertTrue(CLS_NM + "assignedRoles list size check", rArray.length == uRoles.size());
                for (String[] url : rArray)
                {
                    int indx = uRoles.indexOf(RoleTestData.getUserRole(UserTestData.getUserId(usr), url));
                    if (indx != -1)
                    {
                        UserRole uRole = uRoles.get(indx);
                        assertNotNull(uRole);
                        RoleTestData.assertEquals(UserTestData.getUserId(usr), uRole, url);
                        log.debug(CLS_NM + ".assignedRoles userId [" + uRole.getUserId() + "] role [" + uRole.getName() + "] successful");
                    }
                    else
                    {
                        msg = CLS_NM + ".assignedRoles userId [" + user.getUserId() + "] role [" + RoleTestData.getName(url) + "] failed list search";
                        LogUtil.logIt(msg);
                        fail(msg);
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".assignedRoles caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     *
     */
    public void testAuthorizedRoles()
    {
        // public Set<String> authorizedRoles(User user)
        authorizedRoles("AUTHZ-RLS TU18 TR6 DESC", UserTestData.USERS_TU18U_TR6_DESC);
        authorizedRoles("AUTHZ-RLS TU19 TR7 ASC", UserTestData.USERS_TU19U_TR7_ASC);
    }


    /**
     *
     * @param msg
     * @param uArray
     */
    public void authorizedRoles(String msg, String[][] uArray)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] usr : uArray)
            {
                User user = reviewMgr.readUser(new User(UserTestData.getUserId(usr)));
                assertNotNull(user);

                // Get the authorized roles for this user:
                Set<String> authZRoles = UserTestData.getAuthorizedRoles(usr);

                // If there are any assigned roles, add them to list of authorized.
                Set<String> asgnRoles = UserTestData.getAssignedRoles(usr);
                assertNotNull(asgnRoles);
                assertTrue(asgnRoles.size() > 0);
                for (String asgnRole : asgnRoles)
                {
                    authZRoles.add(asgnRole);
                }

                // Retrieve actual roles authorized to User according to LDAP:
                Set<String> actualRoles = reviewMgr.authorizedRoles(user);
                assertNotNull(actualRoles);
                assertTrue(actualRoles.size() > 0);

                // The two list sizes better match or fail the test case.
                assertTrue(CLS_NM + "authorizedRoles list size test case", authZRoles.size() == actualRoles.size());

                // For each authorized role found in User test data, check to see if it was found in LDAP for User.  If not fail the test case.
                for (String roleName : authZRoles)
                {
                    assertTrue(CLS_NM + ".authorizedRoles userId [" + user.getUserId() + "] role [" + roleName + "] not found", actualRoles.contains(roleName));
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".assignedRoles caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testAuthorizedUsers()
    {
        // public List<User> authorizedUsers(Role role)
        authorizedUsers("ATHZ-USRS TR1 TU1_UPD", RoleTestData.ROLES_TR1, UserTestData.USERS_TU1_UPD);
        authorizedUsers("ATHZ-USRS TR2 TU4", RoleTestData.ROLES_TR2, UserTestData.USERS_TU4);
        authorizedUsers("ATHZ-USRS TR3 TU3", RoleTestData.ROLES_TR3, UserTestData.USERS_TU3);
    }

    /**
     * @param msg
     * @param rArray
     * @param uArray
     */
    public void authorizedUsers(String msg, String[][] rArray, String[][] uArray)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] rle : rArray)
            {
                List<User> users = reviewMgr.authorizedUsers(new Role(RoleTestData.getName(rle)));
                assertNotNull(users);
                //log.debug(CLS_NM + "authorizedUsers list source size=" + uArray.length + " ldap size="  + users.size());
                assertTrue(CLS_NM + "authorizedUsers list size check", uArray.length == users.size());
                for (String[] usr : uArray)
                {
                    int indx = users.indexOf(UserTestData.getUser(usr));
                    if (indx != -1)
                    {
                        User user = users.get(indx);
                        assertNotNull(user);
                        UserTestData.assertEquals(user, usr);
                        log.debug(CLS_NM + ".authorizedUsers role name [" + RoleTestData.getName(rle) + "] userId [" + user.getUserId() + "] successful");
                    }
                    else
                    {
                        msg = CLS_NM + ".authorizedUsers role [" + RoleTestData.getName(rle) + "] user [" + UserTestData.getUserId(usr) + "] failed list search";
                        LogUtil.logIt(msg);
                        fail(msg);
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".authorizedUsers caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testAuthorizedUsersHier()
    {
        // public List<User> authorizedUsers(Role role)
        authorizedUsersHier("ATHZ-USRS-HIER TR6 TU18", RoleTestData.TR6_AUTHORIZED_USERS);
        authorizedUsersHier("ATHZ-USRS-HIER TR7 TU19", RoleTestData.TR7_AUTHORIZED_USERS);
    }

    /**
     *
     * @param msg
     * @param roleMap
     */
    public void authorizedUsersHier(String msg, Map roleMap)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();

            // create iterator based on rolemap:
            Iterator itr = roleMap.entrySet().iterator();

            // iterate over every role entry found in map:
            while(itr.hasNext())
            {
                Map.Entry pairs = (Map.Entry)itr.next();
                String roleName = (String)pairs.getKey();
                String szValidUsers = (String)pairs.getValue();
                Set<String> userSet = TestUtils.getSets(szValidUsers);
                assertNotNull(userSet);
                assertTrue(userSet.size() > 0);
                List<User> actualUsers = reviewMgr.authorizedUsers(new Role(roleName));
                assertNotNull(actualUsers);
                assertTrue(actualUsers.size() > 0);

                // Ensure the two list sizes match or fail the test case.
                assertTrue(CLS_NM + "authorizedUsersHier failed list size test case", userSet.size() == actualUsers.size());

                // for each valid user expected, ensure it actually pulled from API:
                for (String userId : userSet)
                {
                    User validUser = new User(userId);
                    assertTrue(CLS_NM + ".authorizedUsersHier failed authorizedUsers test, role [" + roleName + "] does not have user [" + validUser.getUserId() + "] as authorized", actualUsers.contains(validUser));
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".authorizedUsersHier caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testRolePermissions()
    {
        // public List<Permission> rolePermissions(Role role)
        rolePermissions("ATHRZ-RLE-PRMS TR1 TOB1 TOP1_UPD", RoleTestData.ROLES_TR1, PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1_UPD);
    }

    /**
     * @param msg
     * @param rArray
     * @param pObjArray
     * @param pOpArray
     */
    public void rolePermissions(String msg, String[][] rArray, String[][] pObjArray, String[][] pOpArray)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] rle : rArray)
            {
                Role role = RoleTestData.getRole(rle);
                List<Permission> perms = reviewMgr.rolePermissions(role);
                assertNotNull(perms);
                assertTrue(CLS_NM + "rolePermissions list size check", pOpArray.length * pObjArray.length == perms.size());
                for (String[] obj : pObjArray)
                {
                    for (String[] op : pOpArray)
                    {
                        int indx = perms.indexOf(new Permission(PermTestData.getName(obj), PermTestData.getName(op)));
                        if (indx != -1)
                        {
                            Permission pOp = perms.get(indx);
                            assertNotNull(pOp);
                            PermTestData.assertEquals(PermTestData.getName(obj), pOp, op);
                            log.debug(CLS_NM + ".rolePermissions role name [" + role.getName() + "] perm objectName [" + PermTestData.getName(obj) + "] operationName [" + PermTestData.getName(op) + "] successful");
                        }
                        else
                        {
                            msg = CLS_NM + ".rolePermissions role name [" + role.getName() + "] perm objectName [" + PermTestData.getName(obj) + "] operationName [" + PermTestData.getName(op) + "] failed list search";
                            LogUtil.logIt(msg);
                            fail(msg);
                        }
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".rolePermissions caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testPermissionRoles()
    {
        // public List<Role> permissionRoles(Permission perm)
        permissionRoles("PRM-RLS TOB1 TOP1 TR1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, RoleTestData.ROLES_TR1);
    }

    /**
     * @param msg
     * @param pObjArray
     * @param pOpArray
     * @param rArray
     */
    public void permissionRoles(String msg, String[][] pObjArray, String[][] pOpArray, String[][] rArray)
    {
        LogUtil.logIt(msg);
        Permission pOp;
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] obj : pObjArray)
            {
                for (String[] op : pOpArray)
                {
                    pOp = new Permission();
                    pOp.setObjectName(PermTestData.getName(obj));
                    pOp.setOpName(PermTestData.getName(op));
                    pOp.setObjectId(PermTestData.getObjectId(op));
                    List<String> roles = reviewMgr.permissionRoles(pOp);
                    assertNotNull(roles);
                    assertTrue(CLS_NM + "permissionRoles permission object [" + pOp.getObjectName() + "] operationName [" + pOp.getOpName() + "] objectId [" + pOp.getObjectId() + "]", rArray.length == roles.size());
                    for (String[] rle : rArray)
                    {
                        int indx = roles.indexOf(RoleTestData.getName(rle));
                        if (indx != -1)
                        {
                            String roleNm = roles.get(indx);
                            assertEquals(CLS_NM + ".permissionRoles failed compare role name", RoleTestData.getName(rle), roleNm);
                            log.debug(CLS_NM + ".permissionRoles permission objectName [" + pOp.getObjectName() + "] operationName [" + pOp.getOpName() + "] objectId [" + pOp.getObjectId() + "] roleNm [" + roleNm + "] successful");
                        }
                        else
                        {
                            msg = CLS_NM + ".permissionRoles permission objectName [" + pOp.getObjectName() + "] operationName [" + pOp.getOpName() + "]  objectId [" + pOp.getObjectId() + "] role [" + RoleTestData.getName(rle) + "] failed list search";
                            LogUtil.logIt(msg);
                            fail(msg);
                        }
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".permissionRoles caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     *
     */
    public void testAuthorizedPermissionRoles()
    {
        // public Set<String> authorizedPermissionRoles(Permission perm)
        authorizedPermissionRoles("AUTHZ PRM-RLES TOB6 TOP5 TR5B", PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5, RoleTestData.ROLES_TR5B);
    }

    /**
     *
     * @param msg
     * @param pObjArray
     * @param pOpArray
     * @param rArray
     */
    public void authorizedPermissionRoles(String msg, String[][] pObjArray, String[][] pOpArray, String[][] rArray)
    {
        LogUtil.logIt(msg);
        Permission pOp;
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] obj : pObjArray)
            {
                int i = 0;
                for (String[] op : pOpArray)
                {
                    pOp = new Permission();
                    pOp.setObjectName(PermTestData.getName(obj));
                    pOp.setOpName(PermTestData.getName(op));
                    pOp.setObjectId(PermTestData.getObjectId(op));
                    Set<String> roles = reviewMgr.authorizedPermissionRoles(pOp);
                    assertNotNull(roles);
                    int expectedAuthZedRoles = i + 1;
                    assertTrue(CLS_NM + "authorizedPermissionRoles permission object [" + pOp.getObjectName() + "] operationName [" + pOp.getOpName() + "] objectId [" + pOp.getObjectId() + "]", expectedAuthZedRoles == roles.size());
                    int j = 1;
                    for (String[] rle : rArray)
                    {
                        String roleName = RoleTestData.getName(rle);
                        if(j++ <= expectedAuthZedRoles)
                        {
                            assertTrue(CLS_NM + "authorizedPermissionRoles roleName [" + roleName + "] should be authorized for operationName [" + pOp.getOpName() + "] objectId [" + pOp.getObjectId() + "]", roles.contains(roleName));
                        }
                        else
                        {
                            assertTrue(CLS_NM + "authorizedPermissionRoles roleName [" + roleName + "] should not be authorized for operationName [" + pOp.getOpName() + "] objectId [" + pOp.getObjectId() + "]", !roles.contains(roleName));
                        }
                    }
                    i++;
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".authorizedPermissionRoles caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testPermissionUsers()
    {
        //  public List<User> permissionUsers(Permission perm)
        permissionUsers("PRM-USRS TOB1 TOP1 TU1_UPD", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, UserTestData.USERS_TU1_UPD);
    }

    /**
     * @param msg
     * @param pObjArray
     * @param pOpArray
     * @param uArray
     */
    public void permissionUsers(String msg, String[][] pObjArray, String[][] pOpArray, String[][] uArray)
    {
        LogUtil.logIt(msg);
        Permission pOp;
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] obj : pObjArray)
            {
                for (String[] op : pOpArray)
                {
                    pOp = new Permission();
                    pOp.setObjectName(PermTestData.getName(obj));
                    pOp.setOpName(PermTestData.getName(op));
                    pOp.setObjectId(PermTestData.getObjectId(op));
                    List<String> users = reviewMgr.permissionUsers(pOp);
                    assertNotNull(users);
                    assertTrue(CLS_NM + "permissionUsers permission object [" + pOp.getObjectName() + "] operationName [" + pOp.getOpName() + "] objectId [" + pOp.getObjectId() + "]", uArray.length == users.size());
                    for (String[] usr : uArray)
                    {
                        int indx = users.indexOf(RoleTestData.getName(usr));
                        if (indx != -1)
                        {
                            String userId = users.get(indx);
                            assertEquals(CLS_NM + ".permissionUsers failed compare userId", UserTestData.getUserId(usr), userId);
                            log.debug(CLS_NM + ".permissionUsers permission objectName [" + pOp.getObjectName() + "] operationName [" + pOp.getOpName() + "] objectId [" + pOp.getObjectId() + "] userId [" + userId + "] successful");
                        }
                        else
                        {
                            msg = CLS_NM + ".permissionUsers permission objectName [" + pOp.getObjectName() + "] operationName [" + pOp.getOpName() + "]  objectId [" + pOp.getObjectId() + "] userId [" + UserTestData.getUserId(usr) + "] failed list search";
                            LogUtil.logIt(msg);
                            fail(msg);
                        }
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".permissionUsers caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     *
     */
    public void testAuthorizedPermissionUsers()
    {
        // public Set<String> authorizedPermissionUsers(Permission perm)
        authorizedPermissionUsers("AUTHZ PRM-USRS TOB6 TOP5 TU20", PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5, UserTestData.USERS_TU20U_TR5B);
    }

    /**
     *
     * @param msg
     * @param pObjArray
     * @param pOpArray
     * @param uArray
     */
    public void authorizedPermissionUsers(String msg, String[][] pObjArray, String[][] pOpArray, String[][] uArray)
    {
        LogUtil.logIt(msg);
        Permission pOp;
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] obj : pObjArray)
            {
                int i = 0;
                for (String[] op : pOpArray)
                {
                    pOp = new Permission();
                    pOp.setObjectName(PermTestData.getName(obj));
                    pOp.setOpName(PermTestData.getName(op));
                    pOp.setObjectId(PermTestData.getObjectId(op));
                    Set<String> users = reviewMgr.authorizedPermissionUsers(pOp);
                    assertNotNull(users);
                    int expectedAuthZedUsers = i + 1;
                    assertTrue(CLS_NM + "authorizedPermissionUsers permission object [" + pOp.getObjectName() + "] operationName [" + pOp.getOpName() + "] objectId [" + pOp.getObjectId() + "]", expectedAuthZedUsers == users.size());
                    int j = 1;
                    for (String[] usr : uArray)
                    {
                        String userId = UserTestData.getUserId(usr);
                        if(j++ <= expectedAuthZedUsers)
                        {
                            assertTrue(CLS_NM + "authorizedPermissionUsers userId [" + userId + "] should be authorized for operationName [" + pOp.getOpName() + "] objectId [" + pOp.getObjectId() + "]", users.contains(userId));
                        }
                        else
                        {
                            assertTrue(CLS_NM + "authorizedPermissionUsers userId [" + userId + "] should not be authorized for operationName [" + pOp.getOpName() + "] objectId [" + pOp.getObjectId() + "]", !users.contains(userId));
                        }
                    }
                    i++;
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".authorizedPermissionUsers caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testUserPermissions()
    {
        // public List <Permission> userPermissions(User user)
        userPermissions("USR-PRMS TU1_UPD TOB1 TOP1_UPD", UserTestData.USERS_TU1_UPD, PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1_UPD);
        userPermissions("USR-PRMS TU3 TOB3 TOP3", UserTestData.USERS_TU3, PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3);
        userPermissions("USR-PRMS TU4 TOB2 TOP2", UserTestData.USERS_TU4, PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2);
    }

    /**
     * @param msg
     * @param uArray
     * @param pObjArray
     * @param pOpArray
     */
    public void userPermissions(String msg, String[][] uArray, String[][] pObjArray, String[][] pOpArray)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] usr : uArray)
            {
                User user = UserTestData.getUser(usr);
                List<Permission> perms = reviewMgr.userPermissions(user);
                assertNotNull(perms);
                assertTrue(CLS_NM + "userPermissions list size check", pOpArray.length * pObjArray.length == perms.size());
                for (String[] obj : pObjArray)
                {
                    for (String[] op : pOpArray)
                    {
                        int indx = perms.indexOf(new Permission(PermTestData.getName(obj), PermTestData.getName(op)));
                        if (indx != -1)
                        {
                            Permission pOp = perms.get(indx);
                            assertNotNull(pOp);
                            PermTestData.assertEquals(PermTestData.getName(obj), pOp, op);
                            log.debug(CLS_NM + ".userPermissions userId [" + user.getUserId() + "] perm objectName [" + PermTestData.getName(obj) + "] operationName [" + PermTestData.getName(op) + "] successful");
                        }
                        else
                        {
                            msg = CLS_NM + ".userPermissions userId [" + user.getUserId() + "] perm objectName [" + PermTestData.getName(obj) + "] operationName [" + PermTestData.getName(op) + "] failed list search";
                            LogUtil.logIt(msg);
                            fail(msg);
                        }
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".userPermissions caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testFindRoleNms()
    {
        // public List<String> findRoles(String searchVal, int limit)
        searchRolesNms("SRCH-RLNMS TR1", TestUtils.getSrchValue(RoleTestData.getName(RoleTestData.ROLES_TR1[0])), RoleTestData.ROLES_TR1);
        searchRolesNms("SRCH-RLNMS TR2", TestUtils.getSrchValue(RoleTestData.getName(RoleTestData.ROLES_TR2[0])), RoleTestData.ROLES_TR2);
        searchRolesNms("SRCH-RLNMS TR3", TestUtils.getSrchValue(RoleTestData.getName(RoleTestData.ROLES_TR3[0])), RoleTestData.ROLES_TR3);
        searchRolesNms("SRCH-RLNMS TR4_UPD", TestUtils.getSrchValue(RoleTestData.getName(RoleTestData.ROLES_TR4[0])), RoleTestData.ROLES_TR4_UPD);
    }

    /**
     *
     * @param msg
     * @param srchValue
     * @param rArray
     */
    public void searchRolesNms(String msg, String srchValue, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            List<String> roles = reviewMgr.findRoles(srchValue, rArray.length);
            assertNotNull(roles);
            assertTrue(CLS_NM + "searchRolesNms list size check", rArray.length == roles.size());
            for (String[] rle : rArray)
            {
                int indx = roles.indexOf(RoleTestData.getName(rle));
                if (indx != -1)
                {
                    String roleNm = roles.get(indx);
                    assertNotNull(roleNm);
                    assertEquals(CLS_NM + ".searchRolesNms failed compare role name", RoleTestData.getName(rle), roleNm);
                }
                else
                {
                    msg = CLS_NM + ".searchRolesNms srchValue [" + srchValue + "] failed list search";
                    LogUtil.logIt(msg);
                    fail(msg);
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".searchRolesNms srchValue [" + srchValue + "] caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testFindUserIds()
    {
        // public List<String> findUsers(User user, int limit)
        searchUserIds("SRCH-USRIDS TU1_UPD", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU1[0])), UserTestData.USERS_TU1_UPD);
        searchUserIds("SRCH-USRIDS TU3", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU3[0])), UserTestData.USERS_TU3);
        searchUserIds("SRCH-USRIDS TU4", TestUtils.getSrchValue(UserTestData.getUserId(UserTestData.USERS_TU4[0])), UserTestData.USERS_TU4);
    }

    /**
     *
     * @param msg
     * @param srchValue
     * @param uArray
     */
    public void searchUserIds(String msg, String srchValue, String[][] uArray)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            List<String> users = reviewMgr.findUsers(new User(srchValue), uArray.length);
            assertNotNull(users);
            assertTrue(CLS_NM + "searchUserIds list size check", uArray.length == users.size());
            for (String[] usr : uArray)
            {
                int indx = users.indexOf(UserTestData.getUserId(usr));
                if (indx != -1)
                {
                    String userId = users.get(indx);
                    assertNotNull(userId);
                    assertEquals(CLS_NM + ".searchUserIds failed compare user userId", UserTestData.getUserId(usr).toUpperCase(), userId.toUpperCase());
                }
                else
                {
                    msg = CLS_NM + ".searchUserIds srchValue [" + srchValue + "] failed list search";
                    LogUtil.logIt(msg);
                    fail(msg);
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".searchUserIds srchValue [" + srchValue + "] caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testAuthorizedUserIds()
    {
        //  public List<String> authorizedUsers(Role role, int limit)
        assignedUserIds("ATHZ-USRS TR1 TU1_UPD", RoleTestData.ROLES_TR1, UserTestData.USERS_TU1_UPD);
        assignedUserIds("ATHZ-USRS TR2 TU4", RoleTestData.ROLES_TR2, UserTestData.USERS_TU4);
        assignedUserIds("ATHZ-USRS TR3 TU3", RoleTestData.ROLES_TR3, UserTestData.USERS_TU3);
    }

    /**
     * @param msg
     * @param rArray
     * @param uArray
     */
    public void assignedUserIds(String msg, String[][] rArray, String[][] uArray)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] rle : rArray)
            {
                List<String> users = reviewMgr.assignedUsers(new Role(RoleTestData.getName(rle)), uArray.length);
                assertNotNull(users);
                assertTrue(CLS_NM + ".assignedUserIds list size check", uArray.length == users.size());
                for (String[] usr : uArray)
                {
                    int indx = users.indexOf(UserTestData.getUserId(usr));
                    // todo - figure out how to compare userid dns with userids:
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".assignedUserIds caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testAssignedRoleNms()
    {
        //  public List<String> assignedRoles(String userId)
        assignedRoleNms("ASGN-RLS TU1_UPD TR1", UserTestData.USERS_TU1_UPD, RoleTestData.ROLES_TR1);
        //assignedRoles("ASGN-RLS TU3 TR2", UserTestData.USERS_TU3, RoleTestData.ROLES_TR2);
        assignedRoleNms("ASGN-RLS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2);
        assignedRoleNms("ASGN-RLS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3);
    }


    /**
     * @param msg
     * @param uArray
     * @param rArray
     */
    public void assignedRoleNms(String msg, String[][] uArray, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            ReviewMgr reviewMgr = getManagedReviewMgr();
            for (String[] usr : uArray)
            {
                List<String> uRoles = reviewMgr.assignedRoles(UserTestData.getUserId(usr));
                assertNotNull(uRoles);
                assertTrue(CLS_NM + "assignedRoleNms list size check", rArray.length == uRoles.size());
                for (String[] url : rArray)
                {
                    int indx = uRoles.indexOf(RoleTestData.getName(url));
                    if (indx != -1)
                    {
                        String uRole = uRoles.get(indx);
                        assertNotNull(uRole);
                        assertEquals(CLS_NM + ".assignedRoleNms failed compare role name", RoleTestData.getName(url), uRole);
                    }
                    else
                    {
                        msg = CLS_NM + ".assignedRoleNms userId [" + UserTestData.getUserId(usr) + "] role [" + RoleTestData.getName(url) + "] failed list search";
                        LogUtil.logIt(msg);
                        fail(msg);
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".assignedRoleNms caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     *
     * @return
     * @throws us.jts.fortress.SecurityException
     */
    public static ReviewMgr getManagedReviewMgr() throws SecurityException
    {
        if(FortressJUnitTest.isAdminEnabled() && adminSess == null)
        {
            adminSess = DelegatedMgrImplTest.createAdminSession();
        }
        ReviewMgr reviewMgr = ReviewMgrFactory.createInstance(TestUtils.getContext(), adminSess);
        return reviewMgr;
    }
}
