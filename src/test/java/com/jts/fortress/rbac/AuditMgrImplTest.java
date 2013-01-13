/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.AuditMgr;
import com.jts.fortress.AuditMgrFactory;
import com.jts.fortress.SecurityException;
import com.jts.fortress.util.LogUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AccessMgrImpl Tester.
 *
 * @author Shawn McKinney
 */
public class AuditMgrImplTest extends TestCase
{
    private static final String CLS_NM = AuditMgrImplTest.class.getName();
    private static Session adminSess = null;
    final protected static Logger log = Logger.getLogger(CLS_NM);

    public AuditMgrImplTest(String name)
    {
        super(name);
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
        //suite.addTest(new AuditMgrImplTest("testSearchBinds"));
        //suite.addTest(new AuditMgrImplTest("testSearchAuthNInvalid"));
        //suite.addTest(new AuditMgrImplTest("testGetAuthZs"));
        suite.addTest(new AuditMgrImplTest("testSearchAuthZs"));
        //suite.addTest(new AuditMgrImplTest("testSearchMods"));
        //suite.addTest(new AuditMgrImplTest("testSearchAdminMods"));
        return suite;
    }


    /**
     *
     */
    public void testSearchAdminMods()
    {
        searchAdminMods("SESS-USRS RBAC TU0", UserTestData.USERS_TU0, PermTestData.ADMINMGR_OBJ, PermTestData.ADMINMGR_OPS);
        searchAdminMods("SESS-USRS ARBAC TU0", UserTestData.USERS_TU0, PermTestData.DELEGATEDMGR_OBJ, PermTestData.DELEGATEDMGR_OPS);
        searchAdminMods("SESS-USRS PWPOLICY TU0", UserTestData.USERS_TU0, PermTestData.PSWDMGR_OBJ, PermTestData.PSWDMGR_OPS);
    }


    private static Map disabled = loadAuditMap();
    private static Map loadAuditMap()
    {
        disabled = new HashMap();
        //disabled.put("AdminMgrImpl.changePassword", null);
        //disabled.put("AdminMgrRestImpl.changePassword", null);
        log.info(CLS_NM + ".loadAuditMap isFirstRun [" + FortressJUnitTest.isFirstRun() + "]");
        if(FortressJUnitTest.isFirstRun())
        {
            disabled.put("AdminMgrImpl.deleteRole", null);
            disabled.put("AdminMgrImpl.deleteUser", null);
            disabled.put("AdminMgrImpl.deassignUser", null);
            disabled.put("AdminMgrImpl.deletePermission", null);
            disabled.put("AdminMgrImpl.deletePermObj", null);
            disabled.put("AdminMgrImpl.revokePermission", null);
            disabled.put("AdminMgrImpl.revokePermissionUser", null);
            disabled.put("AdminMgrImpl.deleteInheritance", null);
            disabled.put("AdminMgrImpl.deleteSsdRoleMember", null);
            disabled.put("AdminMgrImpl.deleteSsdSet", null);
            disabled.put("AdminMgrImpl.deleteDsdRoleMember", null);
            disabled.put("AdminMgrImpl.deleteDsdSet", null);
            disabled.put("AdminMgrImpl.disableUser", null);

            disabled.put("DelAdminMgrImpl.deleteRole", null);
            disabled.put("DelAdminMgrImpl.deassignUser", null);
            disabled.put("DelAdminMgrImpl.deleteOU", null);
            disabled.put("DelAdminMgrImpl.deleteInheritanceOU", null);
            disabled.put("DelAdminMgrImpl.deleteInheritanceRole", null);

            disabled.put("PwPolicyMgrImpl.deletePasswordPolicy", null);
            disabled.put("PwPolicyMgrImpl.delete", null);
        }
        return disabled;
    }

    private static boolean isAudit(String objName, String opName)
    {
        return !disabled.containsKey(objName + "." + opName);
    }
    /**
     *
     * @param msg
     * @param uArray
     */
    public static void searchAdminMods(String msg, String[][] uArray, String[][] oArray, String[][] opArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AuditMgr auditMgr = getManagedAuditMgr();
            for (String[] usr : uArray)
            {
                User user = UserTestData.getUser(usr);
                // now search for successful session creation events:
                UserAudit uAudit = new UserAudit();
                uAudit.setUserId(user.getUserId());
                for (String[] obj : oArray)
                {
                    String objectName = AdminUtil.getObjName(PermTestData.getName(obj));
                    uAudit.setObjName(objectName);
                    for (String[] op : opArray)
                    {
                        uAudit.setOpName(PermTestData.getName(op));
                        List<Mod> mods = auditMgr.searchAdminMods(uAudit);
                        assertNotNull(mods);

                        assertTrue(CLS_NM + "searchAdminMods failed search for successful authentication user [" + user.getUserId() + "] object [" + objectName + "] operation [" + PermTestData.getName(op) + "]", mods.size() > 0 || !isAudit(objectName, PermTestData.getName(op)));
                        boolean result = mods.size() > 0 || !isAudit(objectName, PermTestData.getName(op));
                        //System.out.println(CLS_NM + "searchAdminMods search user [" + user.getUserId() + "] object [" + objectName + "] operation [" + PermTestData.getName(op) + "] result: " + result);
                    }
                }
            }
            log.debug(CLS_NM + ".searchAdminMods successful");
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".searchAdminMods: failed with SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     *
     */
    public void testSearchMods()
    {
        searchMods("SESS-USRS TU1_UPD", UserTestData.USERS_TU1_UPD);
        searchMods("SESS-USRS TU3", UserTestData.USERS_TU3);
        searchMods("SESS-USRS TU4", UserTestData.USERS_TU4);
    }


    /**
     *
     * @param msg
     * @param uArray
     */
    public static void searchMods(String msg, String[][] uArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AuditMgr auditMgr = getManagedAuditMgr();
            for (String[] usr : uArray)
            {
                User user = UserTestData.getUser(usr);
                // now search for successful session creation events:
                UserAudit uAudit = new UserAudit();
                uAudit.setUserId(user.getUserId());
                uAudit.setFailedOnly(false);
                List<Mod> mods = auditMgr.searchUserSessions(uAudit);
                assertNotNull(mods);
                assertTrue(CLS_NM + "searchUserSessions failed search for successful authentication user [" + user.getUserId() + "]", mods.size() > 0);
            }
            log.debug(CLS_NM + ".searchUserSessions successful");
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".searchUserSessions: failed with SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testSearchAuthZs()
    {
        searchAuthZs("GET-AUTHZ TU1_UPD", UserTestData.USERS_TU1_UPD, PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, false);
        searchAuthZs("GET-AUTHZ TU3", UserTestData.USERS_TU3, PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, false);
        searchAuthZs("GET-AUTHZ TU4", UserTestData.USERS_TU4, PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, false);

        // search for failed only:
        searchAuthZs("GET-AUTHZ TU1_UPD", UserTestData.USERS_TU1_UPD, PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true);
        searchAuthZs("GET-AUTHZ TU3", UserTestData.USERS_TU3, PermTestData.OBJS_TOB2, PermTestData.OPS_TOP1, true);
        searchAuthZs("GET-AUTHZ TU4", UserTestData.USERS_TU4, PermTestData.OBJS_TOB2, PermTestData.OPS_TOP1, true);
    }


    /**
     *
     * @param msg
     * @param uArray
     */
    public static void searchAuthZs(String msg, String[][] uArray, String[][] oArray, String[][] opArray, boolean failedOnly)
    {
        LogUtil.logIt(msg);
        try
        {
            AuditMgr auditMgr = getManagedAuditMgr();
            for (String[] usr : uArray)
            {
                User user = UserTestData.getUser(usr);
                // now search for successful authentications:
                UserAudit uAudit = new UserAudit();
                uAudit.setUserId(user.getUserId());
                uAudit.setFailedOnly(false);
                for (String[] obj : oArray)
                {
                    uAudit.setObjName(PermTestData.getName(obj));
                    for (String[] op : opArray)
                    {
                        uAudit.setOpName(PermTestData.getName(op));
                        uAudit.setObjId(PermTestData.getObjectId(op));
                        uAudit.setFailedOnly(failedOnly);
                        List<AuthZ> authZs = auditMgr.searchAuthZs(uAudit);
                        assertNotNull(authZs);
                        assertTrue(CLS_NM + "searchAuthZs failed search for successful authorization user [" + user.getUserId() + "]", authZs.size() > 0);
                    }
                }
            }
            log.debug(CLS_NM + ".searchAuthZs successful");
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".searchAuthZs: failed with SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     *
     */
    public void testGetAuthZs()
    {
        getAuthZs("GET-AUTHZ TU1_UPD", UserTestData.USERS_TU1_UPD);
        getAuthZs("GET-AUTHZ TU3", UserTestData.USERS_TU3);
        getAuthZs("GET-AUTHZ TU4", UserTestData.USERS_TU4);
    }


    /**
     *
     * @param msg
     * @param uArray
     */
    public static void getAuthZs(String msg, String[][] uArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AuditMgr auditMgr = getManagedAuditMgr();
            for (String[] usr : uArray)
            {
                User user = UserTestData.getUser(usr);
                // now search for successful authentications:
                UserAudit uAudit = new UserAudit();
                uAudit.setUserId(user.getUserId());
                uAudit.setFailedOnly(false);
                List<AuthZ> authZs = auditMgr.getUserAuthZs(uAudit);
                assertNotNull(authZs);
                assertTrue(CLS_NM + "getUserAuthZs failed search for successful authorization user [" + user.getUserId() + "]", authZs.size() > 0);

                // now search for failed authentications:
                uAudit.setFailedOnly(true);
                authZs = auditMgr.getUserAuthZs(uAudit);
                assertNotNull(authZs);
                assertTrue(CLS_NM + "getUserAuthZs failed search for failed authorization user [" + user.getUserId() + "]", authZs.size() > 0);
            }
            log.debug(CLS_NM + ".getUserAuthZs successful");
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".getUserAuthZs: failed with SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     *
     */
    public void testSearchAuthNInvalid()
    {
        searchAuthNInvalid("INV-AUTHN");
    }


    /**
     *
     * @param msg
     */
    public static void searchAuthNInvalid(String msg)
    {
        LogUtil.logIt(msg);
        try
        {
            AuditMgr auditMgr = getManagedAuditMgr();
            UserAudit uAudit = new UserAudit();
            List<AuthZ> resultSet = auditMgr.searchInvalidUsers(uAudit);
            assertNotNull(resultSet);
            assertTrue(CLS_NM + "searchInvalidUsers failed search for invalid authentications", resultSet.size() > 0);
            log.debug(CLS_NM + ".searchInvalidUsers successful");
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".searchInvalidUsers: failed with SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     *
     */
    public void testSearchBinds()
    {
        searchBinds("BIND-USRS TU1_UPD", UserTestData.USERS_TU1_UPD);
        searchBinds("BIND-USRS TU2_CHG", UserTestData.USERS_TU2_CHG);
        searchBinds("BIND-USRS TU3", UserTestData.USERS_TU3);
        searchBinds("BIND-USRS TU4", UserTestData.USERS_TU4);
    }

    /**
     *
     * @param msg
     * @param uArray
     */
    public static void searchBinds(String msg, String[][] uArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AuditMgr auditMgr = getManagedAuditMgr();
            for (String[] usr : uArray)
            {
                User user = UserTestData.getUser(usr);
                // now search for successful authentications:
                UserAudit uAudit = new UserAudit();
                uAudit.setUserId(user.getUserId());
                uAudit.setFailedOnly(false);
                List<Bind> binds = auditMgr.searchBinds(uAudit);
                assertNotNull(binds);
                assertTrue(CLS_NM + "searchBinds failed search for successful authentication user [" + user.getUserId() + "]", binds.size() > 0);

                // now search for failed authentications:
                uAudit.setFailedOnly(true);
                binds = auditMgr.searchBinds(uAudit);
                assertNotNull(binds);
                assertTrue(CLS_NM + "searchBinds failed search for failed authentication user [" + user.getUserId() + "]", binds.size() > 0);
            }
            log.debug(CLS_NM + ".searchBinds successful");
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".searchBinds: failed with SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     *
     * @return
     * @throws com.jts.fortress.SecurityException
     */
    public static AuditMgr getManagedAuditMgr() throws SecurityException
    {
        if(FortressJUnitTest.isAdminEnabled() && adminSess == null)
        {
            adminSess = DelegatedMgrImplTest.createAdminSession();
        }
        AuditMgr auditMgr = AuditMgrFactory.createInstance(TestUtils.getContext(), adminSess);
        return auditMgr;
    }
}
