/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import us.jts.fortress.AdminMgr;
import us.jts.fortress.AdminMgrFactory;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.ReviewMgr;
import us.jts.fortress.SecurityException;
import us.jts.fortress.util.time.CUtil;
import us.jts.fortress.util.LogUtil;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * AdminMgrImpl Tester.
 *
 * @author Shawn McKinney
 */
public class AdminMgrImplTest extends TestCase
{
    private static final String CLS_NM = AdminMgrImplTest.class.getName();
    final private static Logger log = Logger.getLogger(CLS_NM);
    private static Session adminSess = null;

    public AdminMgrImplTest(String name)
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
        /*
        suite.addTest(new AdminMgrImplTest("testDelRoleDescendant"));
        suite.addTest(new AdminMgrImplTest("testAddRoleDescendant"));
        suite.addTest(new AdminMgrImplTest("testDelRoleAscendant"));
        suite.addTest(new AdminMgrImplTest("testAddRoleAscendants"));
        */

        suite.addTest(new AdminMgrImplTest("testDeleteSsdRoleMember"));
        suite.addTest(new AdminMgrImplTest("testDeleteSsdSet"));
        suite.addTest(new AdminMgrImplTest("testCreateSsdSet"));
        suite.addTest(new AdminMgrImplTest("testAddSsdRoleMember"));
        
        return suite;
    }

    public void testAddUser()
    {

        //     public User addUser(User user)
        addUsers("ADD-USRS TU1", UserTestData.USERS_TU1, true);
        addUsers("ADD-USRS TU2", UserTestData.USERS_TU2, true);
        addUsers("ADD-USRS TU3", UserTestData.USERS_TU3, true);
        addUsers("ADD-USRS TU4", UserTestData.USERS_TU4, true);
        addUsers("ADD-USRS TU5", UserTestData.USERS_TU5, true);
        if(FortressJUnitTest.isFirstRun())
        {
            addUsers("ADD-USRS TU6", UserTestData.USERS_TU6, true);
        }
        addUsers("ADD-USRS TU7_HIER", UserTestData.USERS_TU7_HIER, true);
        addUsers("ADD-USRS TU8_SSD", UserTestData.USERS_TU8_SSD, true);
        addUsers("ADD-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, true);
        addUsers("ADD-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, true);
        addUsers("ADD-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, true);
        addUsers("ADD-USRS TU12_DSD", UserTestData.USERS_TU12_DSD, true);
        addUsers("ADD-USRS TU13_DSD_HIER", UserTestData.USERS_TU13_DSD_HIER, true);
        addUsers("ADD-USRS TU14_DSD_HIER", UserTestData.USERS_TU14_DSD_HIER, true);
        addUsers("ADD-USRS TU15_DSD_HIER", UserTestData.USERS_TU15_DSD_HIER, true);
        addUsers("ADD-USRS TU18 TR6_DESC", UserTestData.USERS_TU18U_TR6_DESC, true);
        addUsers("ADD-USRS TU19 TR7_ASC", UserTestData.USERS_TU19U_TR7_ASC, true);
        addUsers("ADD-USRS TU20 TR5_HIER", UserTestData.USERS_TU20U_TR5B, true);
        addUsers("ADD-USRS TU21 DSD_BRUNO", UserTestData.USERS_TU21_DSD_BRUNO, true);
    }

    /**
     * @param uArray
     */
    public static void addUsers(String msg, String[][] uArray, boolean isAdmin)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr;
            if(isAdmin)
            {
                adminMgr = getManagedAdminMgr();
            }
            else
            {
                adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            }
            for (String[] usr : uArray)
            {
                User user = UserTestData.getUser(usr);
                adminMgr.addUser(user);
                log.debug(CLS_NM + ".addUsers user [" + user.getUserId() + "] successful");
                // Does User have Role assignments?
                Set<String> asgnRoles = UserTestData.getAssignedRoles(usr);
                if(asgnRoles != null)
                {
                    for(String name : asgnRoles)
                    {
                        adminMgr.assignUser(new UserRole(user.getUserId(), name));
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addUsers: caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testDeleteUser()
    {
        //     public void disableUser(User user)
        deleteUsers("DEL-USRS TU1", UserTestData.USERS_TU1, false, true);
        deleteUsers("DEL-USRS TU2", UserTestData.USERS_TU2, false, true);
        deleteUsers("DEL-USRS TU3", UserTestData.USERS_TU3, false, true);
        deleteUsers("DEL-USRS TU4", UserTestData.USERS_TU4, false, true);
        deleteUsers("DEL-USRS TU5", UserTestData.USERS_TU5, false, true);
        deleteUsers("DEL-USRS TU6", UserTestData.USERS_TU6, false, true);
        deleteUsers("DEL-USRS TU7_HIER", UserTestData.USERS_TU7_HIER, false, true);
        deleteUsers("DEL-USRS TU8_SSD", UserTestData.USERS_TU8_SSD, false, true);
        deleteUsers("DEL-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, false, true);
        deleteUsers("DEL-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, false, true);
        deleteUsers("DEL-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, false, true);
        deleteUsers("DEL-USRS TU12_DSD", UserTestData.USERS_TU12_DSD, false, true);
        deleteUsers("DEL-USRS TU13_DSD_HIER", UserTestData.USERS_TU13_DSD_HIER, false, true);
        deleteUsers("DEL-USRS TU14_DSD_HIER", UserTestData.USERS_TU14_DSD_HIER, false, true);
        deleteUsers("DEL-USRS TU15_DSD_HIER", UserTestData.USERS_TU15_DSD_HIER, false, true);
    }


    /**
     * @param uArray
     */
    public static void deleteUsers(String msg, String[][] uArray, boolean force, boolean isAdmin)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr;
            if(isAdmin)
            {
                adminMgr = getManagedAdminMgr();
            }
            else
            {
                adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            }
            for (String[] usr : uArray)
            {
                User user = new User();
                user.setUserId(UserTestData.getUserId(usr));
                try
                {
                    if (force)
                    {
                        adminMgr.deleteUser(user);
                        log.debug(CLS_NM + ".deleteUsers force user [" + user.getUserId() + "] successful");
                    }
                    else
                    {
                        adminMgr.disableUser(user);
                        log.debug(CLS_NM + ".deleteUsers user [" + user.getUserId() + "] successful");
                    }
                }
                catch (SecurityException ex)
                {
                    // don't fail test if user was a system user:
                    if (ex.getErrorId() != GlobalErrIds.USER_PLCY_VIOLATION)
                    {
                        log.error(CLS_NM + ".deleteUsers caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
                        fail(ex.getMessage());
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deleteUsers caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testForceDeleteUser()
    {
        //TODO: Test goes here...
        //     public boolean deleteUser(User user)
        //     public void disableUser(User user)
        deleteUsers("FDEL-USRS TU1", UserTestData.USERS_TU1, true, true);
        deleteUsers("FDEL-USRS TU2", UserTestData.USERS_TU2, true, true);
        deleteUsers("FDEL-USRS TU3", UserTestData.USERS_TU3, true, true);
        deleteUsers("FDEL-USRS TU4", UserTestData.USERS_TU4, true, true);
        deleteUsers("FDEL-USRS TU5", UserTestData.USERS_TU5, true, true);
        deleteUsers("FDEL-USRS TU6", UserTestData.USERS_TU6, true, true);
        deleteUsers("FDEL-USRS TU7_HIER", UserTestData.USERS_TU7_HIER, true, true);
        deleteUsers("FDEL-USRS TU8_SSD", UserTestData.USERS_TU8_SSD, true, true);
        deleteUsers("FDEL-USRS TU9_SSD_HIER", UserTestData.USERS_TU9_SSD_HIER, true, true);
        deleteUsers("FDEL-USRS TU10_SSD_HIER", UserTestData.USERS_TU10_SSD_HIER, true, true);
        deleteUsers("FDEL-USRS TU11_SSD_HIER", UserTestData.USERS_TU11_SSD_HIER, true, true);
        deleteUsers("FDEL-USRS TU12_DSD", UserTestData.USERS_TU12_DSD, true, true);
        deleteUsers("FDEL-USRS TU13_DSD_HIER", UserTestData.USERS_TU13_DSD_HIER, true, true);
        deleteUsers("FDEL-USRS TU14_DSD_HIER", UserTestData.USERS_TU14_DSD_HIER, true, true);
        deleteUsers("FDEL-USRS TU15_DSD_HIER", UserTestData.USERS_TU15_DSD_HIER, true, true);
        deleteUsers("FDEL-USRS TU18_TR6_DESC", UserTestData.USERS_TU18U_TR6_DESC, true, true);
        deleteUsers("FDEL-USRS TU19_TR7_ASC", UserTestData.USERS_TU19U_TR7_ASC, true, true);
        deleteUsers("FDEL-USRS TU20_TR5_HIER", UserTestData.USERS_TU20U_TR5B, true, true);
        deleteUsers("FDEL-USRS TU21 DSD_BRUNO", UserTestData.USERS_TU21_DSD_BRUNO, true, true);
    }


    public void testUpdateUser()
    {
        //     public User updateUser(User user)
        updateUsers("UPD USERS TU1_UPD", UserTestData.USERS_TU1_UPD);


    }


    /**
     * @param uArray
     */
    void updateUsers(String msg, String[][] uArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] usr : uArray)
            {
                User user = UserTestData.getUser(usr);
                adminMgr.updateUser(user);
                log.debug(CLS_NM + ".updateUsers user [" + user.getUserId() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".updateUsers: caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testChangePassword()
    {
        //     public void changePassword(User user, String newPassword)
        changePasswords("RESET PW TU2_RST TU2_CHG", UserTestData.USERS_TU2_RST, UserTestData.USERS_TU2_CHG);
    }


    /**
     * @param msg
     * @param uOldArray
     * @param uNewArray
     */
    void changePasswords(String msg, String[][] uOldArray, String[][] uNewArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int i = 0;
            for (String[] usr : uOldArray)
            {
                User user = UserTestData.getUser(usr);
                adminMgr.changePassword(user, UserTestData.getPassword(uNewArray[i++]));
                log.debug(CLS_NM + ".changePasswords user [" + user.getUserId() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".changePasswords: caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testLockUserAccount()
    {
        //     public void lockUserAccount(User user)
        lockUsers("LCK-PWS TU1_UPD", UserTestData.USERS_TU1_UPD);
        lockUsers("LCK-PWS TU3", UserTestData.USERS_TU3);
        lockUsers("LCK-PWS TU4", UserTestData.USERS_TU4);
    }


    /**
     * @param msg
     * @param uArray
     */
    void lockUsers(String msg, String[][] uArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] usr : uArray)
            {
                User user = UserTestData.getUser(usr);
                adminMgr.lockUserAccount(user);
                log.debug(CLS_NM + ".lockUsers user [" + user.getUserId() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".lockUsers: caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testUnlockUserAccount()
    {
        //     public void unlockUserAccount(User user)
        unlockUsers("UNLCK-PWS TU1_UPD", UserTestData.USERS_TU1_UPD);
        unlockUsers("UNLCK-PWS TU3", UserTestData.USERS_TU3);
        unlockUsers("UNLCK-PWS TU4", UserTestData.USERS_TU4);
    }


    /**
     * @param msg
     * @param uArray
     */
    void unlockUsers(String msg, String[][] uArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] usr : uArray)
            {
                User user = UserTestData.getUser(usr);
                adminMgr.unlockUserAccount(user);
                log.debug(CLS_NM + ".unlockUsers user [" + user.getUserId() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".unlockUsers: caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testResetPassword()
    {
        //     public void resetPassword(User user, String newPassword)
        resetPasswords("RST-PW TU2_RST", UserTestData.USERS_TU2_RST);
    }

    /**
     * @param msg
     * @param uArray
     */
    void resetPasswords(String msg, String[][] uArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] usr : uArray)
            {
                User user = UserTestData.getUser(usr);
                adminMgr.resetPassword(user, UserTestData.getPassword(usr));
                log.debug(CLS_NM + ".resetPasswords user [" + user.getUserId() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".resetPasswords: caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testAddRole()
    {
        //     public Role addRole(Role role)
        addRoles("ADD-RLS TR1", RoleTestData.ROLES_TR1);
        addRoles("ADD-RLS TR2", RoleTestData.ROLES_TR2);
        addRoles("ADD-RLS TR3", RoleTestData.ROLES_TR3);
        addRoles("ADD-RLS TR4", RoleTestData.ROLES_TR4);
        addRoles("ADD-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER);
        addRoles("ADD-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B);
        addRoles("ADD-RLS ROLES_TR8_SSD", RoleTestData.ROLES_TR8_SSD);
        addRoles("ADD-RLS ROLES_TR9_SSD", RoleTestData.ROLES_TR9_SSD);
        addRoles("ADD-RLS ROLES_TR10_SSD", RoleTestData.ROLES_TR10_SSD);
        addRoles("ADD-RLS ROLES_TR8_DSD", RoleTestData.ROLES_TR8_DSD);
        addRoles("ADD-RLS ROLES_TR9_DSD", RoleTestData.ROLES_TR9_DSD);
        addRoles("ADD-RLS ROLES_TR10_DSD", RoleTestData.ROLES_TR10_DSD);
        addRoles("ADD-RLS ROLES_TR16_SD", RoleTestData.ROLES_TR16_SD);
        addRoles("ADD-RLS ROLES_TR17_DSD_BRUNO", RoleTestData.ROLES_TR17_DSD_BRUNO);

    }


    /**
     * @param rArray
     */
    public static void addRoles(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] rle : rArray)
            {
                Role role = RoleTestData.getRole(rle);
                Role entity = adminMgr.addRole(role);
                log.debug(CLS_NM + ".addRoles role [" + entity.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addRoles caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDeleteRole()
    {
        //     public void deleteRole(Role role)
        deleteRoles("DEL-RLS TR1", RoleTestData.ROLES_TR1);
        deleteRoles("DEL-RLS TR2", RoleTestData.ROLES_TR2);
        deleteRoles("DEL-RLS TR3", RoleTestData.ROLES_TR3);
        deleteRoles("DEL-RLS TR4", RoleTestData.ROLES_TR4);
        deleteRoles("DEL-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER);
        deleteRoles("DEL-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B);
        deleteRoles("DEL-RLS ROLES_TR8_SSD", RoleTestData.ROLES_TR8_SSD);
        deleteRoles("DEL-RLS ROLES_TR9_SSD", RoleTestData.ROLES_TR9_SSD);
        deleteRoles("DEL-RLS ROLES_TR10_SSD", RoleTestData.ROLES_TR10_SSD);        
        deleteRoles("DEL-RLS ROLES_TR8_DSD", RoleTestData.ROLES_TR8_DSD);
        deleteRoles("DEL-RLS ROLES_TR9_DSD", RoleTestData.ROLES_TR9_DSD);
        deleteRoles("DEL-RLS ROLES_TR10_DSD", RoleTestData.ROLES_TR10_DSD);
        deleteRoles("DEL-RLS ROLES_TR16_SD", RoleTestData.ROLES_TR16_SD);
        deleteRoles("DEL-RLS ROLES_TR17_DSD_BRUNO", RoleTestData.ROLES_TR17_DSD_BRUNO);
    }

    /**
     * @param rArray
     */
    public static void deleteRoles(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] rle : rArray)
            {
                Role role = new Role();
                role.setName(RoleTestData.getName(rle));
                adminMgr.deleteRole(role);
                log.debug(CLS_NM + ".deleteRoles role [" + role.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deleteRoles caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testUpdateRole()
    {
        //     public Role updateRole(Role role)
        updateRoles("UPD-RLS TR4_UPD", RoleTestData.ROLES_TR4_UPD);
    }


    /**
     * @param msg
     * @param rArray
     */
    private void updateRoles(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] rle : rArray)
            {
                Role role = RoleTestData.getRole(rle);
                Role entity = adminMgr.updateRole(role);
                log.debug(CLS_NM + ".updateRoles role [" + entity.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".updateRoles caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testAddRoleDescendant()
    {
        //TODO: Test goes here...
        //     public void addDescendant(Role parentRole, Role childRole)
        addRoleDescendant("ADD-RLS-TR6-DESC", RoleTestData.ROLES_TR6_DESC);
        addRoleDescendant("ADD-RLS-TR11-DESC-SSD", RoleTestData.ROLES_TR11_DESC_SSD);
        addRoleDescendant("ADD-RLS-TR12-DESC-SSD", RoleTestData.ROLES_TR12_DESC_SSD);
        addRoleDescendant("ADD-RLS-TR13-DESC-SSD", RoleTestData.ROLES_TR13_DESC_SSD);
        addRoleDescendant("ADD-RLS-TR11-DESC-DSD", RoleTestData.ROLES_TR11_DESC_DSD);
        addRoleDescendant("ADD-RLS-TR12-DESC-DSD", RoleTestData.ROLES_TR12_DESC_DSD);
        addRoleDescendant("ADD-RLS-TR13-DESC-DSD", RoleTestData.ROLES_TR13_DESC_DSD);
    }


    /**
     *
     * @param msg
     * @param rArray
     */
    private void addRoleDescendant(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int ctr = 0;
            for (String[] rle : rArray)
            {
                Role role = RoleTestData.getRole(rle);
                if(ctr++ == 0 || RoleTestData.isTree(rle))
                {
                    adminMgr.addRole(role);
                    log.debug(CLS_NM + ".addDescendant add role [" + role.getName() + "] successful");
                }

                // use list because order is important for test structure:
                List<String> descs = RoleTestData.getRelationshipList(rle);
                if (descs != null)
                {
                    if (RoleTestData.isTree(rle))
                    {
                        Role parent = role;
                        for (String desc : descs)
                        {
                            Role child = new Role(desc);
                            adminMgr.addDescendant(parent, new Role(desc));
                            log.debug(CLS_NM + ".addDescendant asc role [" + role.getName() + "] desc role [" + desc + "] successful");
                            parent = child;
                        }
                    }
                    else
                    {
                        for (String desc : descs)
                        {
                            adminMgr.addDescendant(role, new Role(desc));
                            log.debug(CLS_NM + ".addDescendant asc role [" + role.getName() + "] desc role [" + desc + "] successful");
                        }
                    }
                }

                Set<String> inheritances = RoleTestData.getInheritances(rle);
                if (inheritances != null)
                {
                    for (String desc : inheritances)
                    {
                        adminMgr.addInheritance(role, new Role(desc));
                        log.debug(CLS_NM + ".addDescendant asc role [" + role.getName() + "] desc role [" + desc + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addDescendant caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }




    public void testDelRoleDescendant() throws SecurityException
    {
        //TODO: Test goes here...
        //     public void addDescendant(Role parentRole, Role childRole)
        //delRoleDescendant("DEL-RLS-DESC");
        delRoleDescendant("DEL-RLS-TR6-DESC", RoleTestData.ROLES_TR6_DESC);
        delRoleDescendant("DEL-RLS-TR11-DESC-SSD", RoleTestData.ROLES_TR11_DESC_SSD);
        delRoleDescendant("DEL-RLS-TR12-DESC-SSD", RoleTestData.ROLES_TR12_DESC_SSD);
        delRoleDescendant("DEL-RLS-TR13-DESC-SSD", RoleTestData.ROLES_TR13_DESC_SSD);        
        delRoleDescendant("DEL-RLS-TR11-DESC-DSD", RoleTestData.ROLES_TR11_DESC_DSD);
        delRoleDescendant("DEL-RLS-TR12-DESC-DSD", RoleTestData.ROLES_TR12_DESC_DSD);
        delRoleDescendant("DEL-RLS-TR13-DESC-DSD", RoleTestData.ROLES_TR13_DESC_DSD);
    }


    /**
     *
     * @param msg
     */
    private void delRoleDescendant(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] rle : rArray)
            {
                Role role = RoleTestData.getRole(rle);
                // use list because order is important for test structure:
                List<String> descs = RoleTestData.getRelationshipList(rle);
                if (descs != null)
                {
                    if (RoleTestData.isTree(rle))
                    {
                        Role parent = role;
                        for (String desc : descs)
                        {
                            Role child = new Role(desc);
                            adminMgr.deleteInheritance(parent, new Role(desc));
                            log.debug(CLS_NM + ".delRoleDescendant asc role [" + role.getName() + "] desc role [" + desc + "] successful");
                            parent = child;
                        }
                    }
                    else
                    {
                        for (String desc : descs)
                        {
                            adminMgr.deleteInheritance(role, new Role(desc));                            
                            log.debug(CLS_NM + ".delRoleDescendant asc role [" + role.getName() + "] desc role [" + desc + "] successful");
                        }
                    }
                }

                Set<String> inheritances = RoleTestData.getInheritances(rle);
                if (inheritances != null)
                {
                    for (String desc : inheritances)
                    {
                        adminMgr.deleteInheritance(role, new Role(desc));
                        log.debug(CLS_NM + ".delRoleDescendant asc role [" + role.getName() + "] desc role [" + desc + "] successful");
                    }
                }
                adminMgr.deleteRole(role);
                log.debug(CLS_NM + ".delRoleDescendant remove asc role [" + role.getName() + "] successful");
            }

            // cleanup the last row of descendants from roles data set.
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            String roleSrchVal = TestUtils.getSrchValue(RoleTestData.getName(rArray[0]));
            List<Role> cleanup = reviewMgr.findRoles(roleSrchVal);
            for(Role re : cleanup)
            {
                adminMgr.deleteRole(re);
                log.debug(CLS_NM + ".delRoleDescendant cleanup role [" + re.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".delRoleDescendant caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testAddRoleAscendants()
    {
        //TODO: Test goes here...
        //     public void addDescendant(Role parentRole, Role childRole)
        addRoleAscendant("ADD-RLS-TR7-ASC", RoleTestData.ROLES_TR7_ASC);
    }
    /**
     * 
     * @param msg
     * @param rArray
     */
    private void addRoleAscendant(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] rle : rArray)
            {
                Role role = RoleTestData.getRole(rle);
                if(RoleTestData.isCreate(rle))
                {
                    adminMgr.addRole(role);
                    log.debug(CLS_NM + ".addAscendant add role [" + role.getName() + "] successful");
                }

                Set<String> ascs = RoleTestData.getRelationships(rle);
                if (ascs != null)
                {
                    for (String asc : ascs)
                    {
                        adminMgr.addAscendant(role, new Role(asc));
                        log.debug(CLS_NM + ".addAscendant desc role [" + role.getName() + "] asc role [" + asc + "] successful");
                    }
                }

                Set<String> inheritances = RoleTestData.getInheritances(rle);
                if (inheritances != null)
                {
                    for (String asc : inheritances)
                    {
                        adminMgr.addInheritance(new Role(asc), role);
                        log.debug(CLS_NM + ".addAscendant desc role [" + role.getName() + "] asc role [" + asc + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addDescendant caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDelRoleAscendant()
    {
        //TODO: Test goes here...
        //     public void addDescendant(Role parentRole, Role childRole)
        delRoleAscendant("DEL-RLS-TR6-ASC", RoleTestData.ROLES_TR7_ASC);
    }


    /**
     *
     * @param msg
     * @param rArray
     */
    private void delRoleAscendant(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] rle : rArray)
            {
                Role role = RoleTestData.getRole(rle);
                Set<String> ascs = RoleTestData.getRelationships(rle);
                if (ascs != null)
                {
                    for (String asc : ascs)
                    {
                        adminMgr.deleteInheritance(new Role(asc), role);
                        log.debug(CLS_NM + ".delRoleAscendant desc role [" + role.getName() + "] asc role [" + asc + "] successful");
                    }
                }

                Set<String> inheritances = RoleTestData.getInheritances(rle);
                if (inheritances != null)
                {
                    for (String asc : inheritances)
                    {
                        adminMgr.deleteInheritance(new Role(asc), role);
                        log.debug(CLS_NM + ".delRoleAscendant desc role [" + role.getName() + "] asc role [" + asc + "] successful");
                    }
                }
                adminMgr.deleteRole(role);
                log.debug(CLS_NM + ".delRoleAscendant remove desc role [" + role.getName() + "] successful");
            }

            // cleanup the top ascendant from roles data set.
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            String roleSrchVal = RoleTestData.getName(rArray[0]);
            roleSrchVal = roleSrchVal.substring(0,roleSrchVal.length()-8);
            List<Role> cleanup = reviewMgr.findRoles(roleSrchVal);
            for(Role re : cleanup)
            {
                adminMgr.deleteRole(re);
                log.debug(CLS_NM + ".delRoleAscendant cleanup role [" + re.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".delRoleAscendant caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testAddRoleInheritance()
    {
        //TODO: Test goes here...
        //     public void addInheritance(Role parentRole, Role childRole)
        addInheritedRoles("ADD-INHERIT-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER);
        addInheritedRoles("ADD-INHERIT-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B);
    }


    /**
     * @param rArray
     */
    public static void addInheritedRoles(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] rle : rArray)
            {
                Role role = RoleTestData.getRole(rle);
                Set<String> parents = RoleTestData.getRelationships(rle);
                if (parents != null)
                {
                    for (String pRole : parents)
                    {
                        adminMgr.addInheritance(new Role(pRole), role);
                        log.debug(CLS_NM + ".addInheritedRoles child role [" + role.getName() + "] parent role [" + pRole + "] successful");
                    }
                }

            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addInheritedRoles caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDeleteRoleInheritance()
    {
        //TODO: Test goes here...
        //     public void addInheritance(Role parentRole, Role childRole)
        deleteInheritedRoles("DEL-INHERIT-RLS ROLES_TR5_HIER", RoleTestData.ROLES_TR5_HIER);
        deleteInheritedRoles("DEL-INHERIT-RLS ROLES_TR5B", RoleTestData.ROLES_TR5B);
    }


    /**
     * @param rArray
     */
    public static void deleteInheritedRoles(String msg, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] rle : rArray)
            {
                Role role = RoleTestData.getRole(rle);
                Set<String> parents = RoleTestData.getRelationships(rle);
                if (parents != null)
                {
                    for (String pRole : parents)
                    {
                        adminMgr.deleteInheritance(new Role(pRole), role);
                        log.debug(CLS_NM + ".deleteInheritedRoles child role [" + role.getName() + "] parent role [" + pRole + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deleteInheritedRoles caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testCreateSsdSet()
    {
        //     public SDSet createSsdSet(SDSet ssdSet)
        createSsdSet("ADD-SSD T1", RoleTestData.SSD_T1);
        createSsdSet("ADD-SSD T4", RoleTestData.SSD_T4);
        createSsdSet("ADD-SSD T5", RoleTestData.SSD_T5);
        createSsdSet("ADD-SSD T6", RoleTestData.SSD_T6);
    }


    /**
     * @param sArray
     */
    private void createSsdSet(String msg, String[][] sArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] ssdle : sArray)
            {
                SDSet ssd = RoleTestData.getSDSet(ssdle);
                SDSet entity = adminMgr.createSsdSet(ssd);
                log.debug(CLS_NM + ".createSsdSet SSD [" + entity.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".createSsdSet caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testCreateDsdSet()
    {
        //     public SDSet createDsdSet(SDSet dsdSet)
        createDsdSet("ADD-DSD T1", RoleTestData.DSD_T1);
        createDsdSet("ADD-DSD T4", RoleTestData.DSD_T4);
        createDsdSet("ADD-DSD T5", RoleTestData.DSD_T5);
        createDsdSet("ADD-DSD T6", RoleTestData.DSD_T6);
        createDsdSet("ADD-DSD T8 BRUNO", RoleTestData.DSD_T8_BRUNO);
    }

    /**
     * @param sArray
     */
    private void createDsdSet(String msg, String[][] sArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] dsdle : sArray)
            {
                SDSet dsd = RoleTestData.getSDSet(dsdle);
                SDSet entity = adminMgr.createDsdSet(dsd);
                log.debug(CLS_NM + ".createDsdSet DSD [" + entity.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".createDsdSet caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     *
     */
    public void testDeleteSsdSet()
    {
        //     public SDSet deleteSsdSet(SDSet ssdSet)
        deleteSsdSet("DEL-SSD T1", RoleTestData.SSD_T1);
        deleteSsdSet("DEL-SSD T4", RoleTestData.SSD_T4);
        deleteSsdSet("DEL-SSD T5", RoleTestData.SSD_T5);
        deleteSsdSet("DEL-SSD T6", RoleTestData.SSD_T6);
        deleteSsdSet("DEL-SSD T7", RoleTestData.SSD_T7);
        deleteDsdSet("DEL-DSD T7 BRUNO", RoleTestData.DSD_T8_BRUNO);
    }

    /**
     * @param sArray
     */
    private void deleteSsdSet(String msg, String[][] sArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] ssdle : sArray)
            {
                SDSet ssd = RoleTestData.getSDSet(ssdle);
                adminMgr.deleteSsdSet(ssd);
                log.debug(CLS_NM + ".deleteSsdSet role [" + ssd.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deleteSsdSet caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDeleteDsdSet()
    {
        //     public SDSet deleteDsdSet(SDSet dsdSet)
        deleteDsdSet("DEL-DSD T1", RoleTestData.DSD_T1);
        deleteDsdSet("DEL-DSD T4", RoleTestData.DSD_T4);
        deleteDsdSet("DEL-DSD T5", RoleTestData.DSD_T5);
        deleteDsdSet("DEL-DSD T6", RoleTestData.DSD_T6);
        deleteDsdSet("DEL-DSD T8", RoleTestData.DSD_T7);
    }

    /**
     * @param sArray
     */
    private void deleteDsdSet(String msg, String[][] sArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] dsdle : sArray)
            {
                SDSet dsd = RoleTestData.getSDSet(dsdle);
                adminMgr.deleteDsdSet(dsd);
                log.debug(CLS_NM + ".deleteDsdSet role [" + dsd.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deleteDsdSet caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testAddSsdRoleMember()
    {
        //     public SDSet addSsdRoleMember(SDSet ssdSet, Role role)
        addSsdRoleMember("ADD-MEM-SSD T2 TR9", RoleTestData.SSD_T2, RoleTestData.ROLES_TR9_SSD);
        addSsdRoleMember("ADD-MEM-SSD T3 TR10", RoleTestData.SSD_T3, RoleTestData.ROLES_TR10_SSD);

    }


    /**
     * @param sArray
     */
    private void addSsdRoleMember(String msg, String[][] sArray, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for (String[] ssdle : sArray)
            {
                SDSet ssd = RoleTestData.getSDSet(ssdle);
                SDSet entity = adminMgr.createSsdSet(ssd);
                log.debug(CLS_NM + ".addSsdRoleMember SSD [" + entity.getName() + "] successful");
                for (String[] rle : rArray)
                {
                    Role role = RoleTestData.getRole(rle);
                    adminMgr.addSsdRoleMember(ssd, role);
                    List<SDSet> ssdSets = reviewMgr.ssdRoleSets(role);
                    assertNotNull(ssdSets);
                    assertTrue(CLS_NM + "addSsdRoleMember list size check", ssdSets.size() == 1);
                    SDSet ssd2 = ssdSets.get(0);
                    assertTrue(CLS_NM + "addSsdRoleMember SSD name check", ssd.getName().equals(ssd2.getName()));
                    assertTrue(CLS_NM + "addSsdRoleMember SSD role check", ssd2.getMembers().contains(role.getName()));
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addSsdRoleMember caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testAddDsdRoleMember()
    {
        //     public SDSet addDsdRoleMember(SDSet dsdSet, Role role)
        addDsdRoleMember("ADD-MEM-DSD T2 TR9", RoleTestData.DSD_T2, RoleTestData.ROLES_TR9_DSD);
        addDsdRoleMember("ADD-MEM-DSD T3 TR10", RoleTestData.DSD_T3, RoleTestData.ROLES_TR10_DSD);

    }


    /**
     * @param sArray
     */
    private void addDsdRoleMember(String msg, String[][] sArray, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for (String[] dsdle : sArray)
            {
                SDSet dsd = RoleTestData.getSDSet(dsdle);
                SDSet entity = adminMgr.createDsdSet(dsd);
                log.debug(CLS_NM + ".addDsdRoleMember DSD [" + entity.getName() + "] successful");
                for (String[] rle : rArray)
                {
                    Role role = RoleTestData.getRole(rle);
                    adminMgr.addDsdRoleMember(dsd, role);
                    List<SDSet> dsdSets = reviewMgr.dsdRoleSets(role);
                    assertNotNull(dsdSets);
                    assertTrue(CLS_NM + "addDsdRoleMember list size check", dsdSets.size() == 1);
                    SDSet dsd2 = dsdSets.get(0);
                    assertTrue(CLS_NM + "addDsdRoleMember DSD name check", dsd.getName().equals(dsd2.getName()));
                    assertTrue(CLS_NM + "addDsdRoleMember DSD role check", dsd2.getMembers().contains(role.getName()));
                    //assertTrue(CLS_NM + "addDsdRoleMember DSD role check", dsd2.getMembers().containsKey(role.getName()));
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addDsdRoleMember caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDeleteSsdRoleMember()
    {
        //public SSDSet deleteSsdRoleMember(SSDSet ssdSet, Role role)
        deleteSsdRoleMember("DEL-MEM-SSD T2 TR9", RoleTestData.SSD_T2, RoleTestData.ROLES_TR9_SSD);
        deleteSsdRoleMember("DEL-MEM-SSD T3 TR10", RoleTestData.SSD_T3, RoleTestData.ROLES_TR10_SSD);

    }


    /**
     * @param sArray
     */
    private void deleteSsdRoleMember(String msg, String[][] sArray, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for (String[] ssdle : sArray)
            {
                SDSet ssd = RoleTestData.getSDSet(ssdle);
                // first add all of the roles as members of SSD entity:
                for (String[] rle : rArray)
                {
                    Role role = RoleTestData.getRole(rle);
                    ssd.addMember(role.getName());
                }
                // now iterate over roles and remove from SSD set one at a time.
                for (String[] rle : rArray)
                {
                    Role role = RoleTestData.getRole(rle);
                    adminMgr.deleteSsdRoleMember(ssd, role);
                    List<SDSet> ssdSets = reviewMgr.ssdRoleSets(role);
                    assertTrue(CLS_NM + "deleteSsdRoleMember list size check ssdSets size [" + ssdSets.size() + "]", ssdSets.size() == 0);
                }
                adminMgr.deleteSsdSet(ssd);
                log.debug(CLS_NM + ".deleteSsdRoleMember SSD [" + ssd.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deleteSsdRoleMember caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDeleteDsdRoleMember()
    {
        //public SSDSet deleteDsdRoleMember(DSDSet dsdSet, Role role)
        deleteDsdRoleMember("DEL-MEM-DSD T2 TR9", RoleTestData.DSD_T2, RoleTestData.ROLES_TR9_DSD);
        deleteDsdRoleMember("DEL-MEM-DSD T3 TR10", RoleTestData.DSD_T3, RoleTestData.ROLES_TR10_DSD);

    }


    /**
     * @param sArray
     */
    private void deleteDsdRoleMember(String msg, String[][] sArray, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for (String[] dsdle : sArray)
            {
                SDSet dsd = RoleTestData.getSDSet(dsdle);
                // first add all of the roles as members of SSD entity:
                for (String[] rle : rArray)
                {
                    Role role = RoleTestData.getRole(rle);
                    dsd.addMember(role.getName());
                }
                // now iterate over roles and remove from DSD set one at a time.
                for (String[] rle : rArray)
                {
                    Role role = RoleTestData.getRole(rle);
                    adminMgr.deleteDsdRoleMember(dsd, role);
                    List<SDSet> dsdSets = reviewMgr.dsdRoleSets(role);
                    assertTrue(CLS_NM + "deleteDsdRoleMember list size check", dsdSets.size() == 0);
                }
                adminMgr.deleteDsdSet(dsd);
                log.debug(CLS_NM + ".deletedsdRoleMember DSD [" + dsd.getName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deleteDsdRoleMember caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testSsdCardinality()
    {
        setSsdCardinality("CARD-SSD T7 TR16", RoleTestData.SSD_T7, RoleTestData.ROLES_TR16_SD);
    }


    /**
     *
     * @param msg
     * @param sArray
     * @param rArray
     */
    private void setSsdCardinality(String msg, String[][] sArray, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for (String[] ssdle : sArray)
            {
                SDSet ssd = RoleTestData.getSDSet(ssdle);
                SDSet entity = adminMgr.createSsdSet(ssd);
                log.debug(CLS_NM + ".setSsdCardinality SSD [" + entity.getName() + "] successful");
                int cardinality = 1;
                for (String[] rle : rArray)
                {
                    Role role = RoleTestData.getRole(rle);
                    adminMgr.addSsdRoleMember(ssd, role);
                    adminMgr.setSsdSetCardinality(ssd, cardinality);
                    int currentCardinality = reviewMgr.ssdRoleSetCardinality(ssd);
                    assertTrue(CLS_NM + "setSsdCardinality cardinality check", currentCardinality == cardinality++);
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".setSsdCardinality caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDsdCardinality()
    {
        setDsdCardinality("CARD-DSD T7 TR16", RoleTestData.DSD_T7, RoleTestData.ROLES_TR16_SD);
    }


    /**
     *
     * @param msg
     * @param sArray
     * @param rArray
     */
    private void setDsdCardinality(String msg, String[][] sArray, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            for (String[] dsdle : sArray)
            {
                SDSet dsd = RoleTestData.getSDSet(dsdle);
                SDSet entity = adminMgr.createDsdSet(dsd);
                log.debug(CLS_NM + ".setDsdCardinality DSD [" + entity.getName() + "] successful");
                int cardinality = 1;
                for (String[] rle : rArray)
                {
                    Role role = RoleTestData.getRole(rle);
                    adminMgr.addDsdRoleMember(dsd, role);
                    adminMgr.setDsdSetCardinality(dsd, cardinality);
                    int currentCardinality = reviewMgr.dsdRoleSetCardinality(dsd);
                    assertTrue(CLS_NM + "setDsdCardinality cardinality check", currentCardinality == cardinality++);
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".setDsdCardinality caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testAssignUser()
    {
        //     public void assignUser(User user, Role role)
        assignUsers("ASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1, false);
        assignUsers("ASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2, true);
        assignUsers("ASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3, true);
        assignUsersH("ASGN-USRS_H TU7 HIER TR5 HIER", UserTestData.USERS_TU7_HIER, RoleTestData.ROLES_TR5_HIER, true);
        assignUsersH("ASGN-USRS_H TU20 TR5B HIER", UserTestData.USERS_TU20U_TR5B, RoleTestData.ROLES_TR5B, true);
        assignUsersSSD("ASGN-USRS_SSDT1 TU8 SSD_T1", UserTestData.USERS_TU8_SSD, RoleTestData.SSD_T1);
        assignUsersSSD("ASGN-USRS_SSDT4B TU9 SSD_T4_B", UserTestData.USERS_TU9_SSD_HIER, RoleTestData.SSD_T4_B);
        assignUsersSSD("ASGN-USRS_SSDT5B TU10 SSD_T5_B", UserTestData.USERS_TU10_SSD_HIER, RoleTestData.SSD_T5_B);
        assignUsersSSD("ASGN-USRS_SSDT6B TU11 SSD_T6_B", UserTestData.USERS_TU11_SSD_HIER, RoleTestData.SSD_T6_B);        
        assignUsersDSD("ASGN-USRS_DSDT1 TU8 DSD_T1", UserTestData.USERS_TU8_SSD, RoleTestData.DSD_T1);
        assignUsersDSD("ASGN-USRS_DSDT4B TU9 DSD_T4_B", UserTestData.USERS_TU9_SSD_HIER, RoleTestData.DSD_T4_B);
        assignUsersDSD("ASGN-USRS_DSDT5B TU10 DSD_T5_B", UserTestData.USERS_TU10_SSD_HIER, RoleTestData.DSD_T5_B);
        assignUsersDSD("ASGN-USRS_DSDT6B TU11 DSD_T6_B", UserTestData.USERS_TU11_SSD_HIER, RoleTestData.DSD_T6_B);
        assignUsersDSD("ASGN-USRS_DSDT1 TU12 DSD_T1", UserTestData.USERS_TU12_DSD, RoleTestData.DSD_T1);
        assignUsersDSD("ASGN-USRS_DSDT4B TU13 DSD_T4_B", UserTestData.USERS_TU13_DSD_HIER, RoleTestData.DSD_T4_B);
        assignUsersDSD("ASGN-USRS_DSDT5B TU14 DSD_T5_B", UserTestData.USERS_TU14_DSD_HIER, RoleTestData.DSD_T5_B);
        assignUsersDSD("ASGN-USRS_DSDT6B TU15 DSD_T6_B", UserTestData.USERS_TU15_DSD_HIER, RoleTestData.DSD_T6_B);
    }


    /**
     * @param msg
     * @param uArray
     * @param rArray
     * @param setTemporal
     */
    void assignUsers(String msg, String[][] uArray, String[][] rArray, boolean setTemporal)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            int i = 0;
            for (String[] usr : uArray)
            {
                i++;
                for (String[] rle : rArray)
                {
                    User user = UserTestData.getUser(usr);
                    Role role = RoleTestData.getRole(rle);
                    UserRole uRole = new UserRole();
                    CUtil.copy(role, uRole);
                    if (!setTemporal)
                    {
                        // test the default constraints for role
                        uRole = new UserRole(role.getName());
                    }
                    uRole.setUserId(user.getUserId());
                    adminMgr.assignUser(uRole);
                    log.debug(CLS_NM + ".assignUsers user [" + user.getUserId() + "] role [" + uRole.getName() + "] successful");
                    // Let's double check the number of users not associated with role:
                    // This one retrieves the collection of all "roleOccupant" attributes associated with the role node:
                    List<String> users = reviewMgr.assignedUsers(RoleTestData.getRole(rle), rArray.length);
                    // This one searches across all Users and pull back list of type "User":
                    List<User> users2 = reviewMgr.authorizedUsers(RoleTestData.getRole(rle));
                    assertNotNull(users);
                    assertNotNull(users2);
                    assertTrue(CLS_NM + ".assignUsers list size check", i == users.size());
                    assertTrue(CLS_NM + ".assignUsers list2 size check", i == users2.size());
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".assignUsers user caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     *
     * @param msg
     * @param uArray
     * @param rArray
     * @param setTemporal
     */
    void assignUsersH(String msg, String[][] uArray, String[][] rArray, boolean setTemporal)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int i = 0;
            for (String[] usr : uArray)
            {
                Role role = RoleTestData.getRole(rArray[i]);
                UserRole uRole = new UserRole();
                uRole.setUserId(UserTestData.getUserId(usr));
                CUtil.copy(role, uRole);
                adminMgr.assignUser(uRole);
                i++;
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".assignUsersH user caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     *
     * @param msg
     * @param uArray
     * @param sArray
     */
    public void assignUsersSSD(String msg, String[][] uArray, String[][] sArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int i = 0;
            for (String[] usr : uArray)
            {
                SDSet ssd = RoleTestData.getSDSet(sArray[i++]);
                //Set<String> roles = ssd.getMembers().keySet();
                Set<String> roles = ssd.getMembers();
                User user = UserTestData.getUser(usr);
                int j = 0;
                for(String role : roles)
                {
                    j++;
                    try
                    {
                        UserRole uRole = new UserRole(user.getUserId(), role);
                        adminMgr.assignUser(uRole);
                        if(j >= ssd.getCardinality())
                        {
                            fail(CLS_NM + ".assignUsersSSD user [" + user.getUserId() + "] role [" + role + "] ssd [" + ssd.getName() + "] cardinality [" + ssd.getCardinality() + "] count [" + j + "] failed");
                        }
                    }
                    catch (SecurityException ex)
                    {
                        assertTrue(CLS_NM + ".assignUsersSSD cardinality test failed user [" + user.getUserId() + "] role [" + role + "] ssd [" + ssd.getName() + "] cardinality [" + ssd.getCardinality() + "] count [" + j + "]", j >= (ssd.getCardinality()));
                        assertTrue(CLS_NM + ".assignUsersSSD cardinality test failed [" + UserTestData.getUserId(usr) + "]", ex.getErrorId() == GlobalErrIds.SSD_VALIDATION_FAILED);
                        // still good, break from loop, we're done here
                        break;
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".assignUsersSSD caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     *
     * @param msg
     * @param uArray
     * @param sArray
     */
    public void assignUsersDSD(String msg, String[][] uArray, String[][] sArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int i = 0;
            for (String[] usr : uArray)
            {
                SDSet dsd = RoleTestData.getSDSet(sArray[i++]);
                Set<String> roles = dsd.getMembers();
                User user = UserTestData.getUser(usr);
                int j = 0;
                for(String role : roles)
                {
                    j++;
                    UserRole uRole = new UserRole(user.getUserId(), role);
                    try
                    {
                        adminMgr.assignUser(uRole);
                    }
                    catch (SecurityException ex)
                    {
                        log.error(CLS_NM + ".assignUsersDSD caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
                        fail(CLS_NM + ".assignUsersDSD user [" + user.getUserId() + "] role [" + role + "] dsd [" + dsd.getName() + "] failed");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".assignUsersDSD caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testDeassignUser()
    {
        //     public void deassignUser(User user, Role role)
        deassignUsers("DEASGN-USRS TU1 TR1", UserTestData.USERS_TU1, RoleTestData.ROLES_TR1);
        deassignUsers("DEASGN-USRS TU4 TR2", UserTestData.USERS_TU4, RoleTestData.ROLES_TR2);
        deassignUsers("DEASGN-USRS TU3 TR3", UserTestData.USERS_TU3, RoleTestData.ROLES_TR3);
        deassignUsersH("DEASGN-USRS_H TU7 TR5 HIER", UserTestData.USERS_TU7_HIER, RoleTestData.ROLES_TR5_HIER);
        deassignUsersH("DEASGN-USRS_H TU20 TR5B HIER", UserTestData.USERS_TU20U_TR5B, RoleTestData.ROLES_TR5B);
    }


    /**
     * @param msg
     * @param uArray
     * @param rArray
     */
    void deassignUsers(String msg, String[][] uArray, String[][] rArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            ReviewMgr reviewMgr = ReviewMgrImplTest.getManagedReviewMgr();
            int i = 0;
            for (String[] usr : uArray)
            {
                i++;
                for (String[] rle : rArray)
                {
                    UserRole uRole = new UserRole(UserTestData.getUserId(usr), RoleTestData.getName(rle));
                    adminMgr.deassignUser(uRole);
                    log.debug(CLS_NM + ".deassignUsers user [" + uRole.getUserId() + "] role [" + uRole.getName() + "] successful");
                    // Double check the number of users associated with role:
                    // This one retrieves the collection of all "roleOccupant" attributes associated with the role node:                    
                    List<String> users = reviewMgr.assignedUsers(RoleTestData.getRole(rle), rArray.length);
                    // This one searches across all Users and pull back list of type "User":
                    List<User> users2 = reviewMgr.authorizedUsers(RoleTestData.getRole(rle));
                    assertNotNull(users);
                    assertNotNull(users2);

                    // If this is the last user deassigned from role, both lists will be returned empty:
                    if (i == uArray.length)
                    {
                        assertTrue(users.size() == 0);
                        assertTrue(users2.size() == 0);
                    }
                    else
                    {
                        assertTrue(CLS_NM + "deassignUsers list size check", (rArray.length - i) == users.size());
                        assertTrue(CLS_NM + "deassignUsers list2 size check", (rArray.length - i) == users2.size());
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deassignUsers caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * @param msg
     * @param uArray
     * @param rArray
     */
    void deassignUsersH(String msg, String[][] uArray, String[][] rArray)
    {
        LogUtil.logIt(msg);
        User user = null;
        Role role = null;
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int i = 0;
            for (String[] usr : uArray)
            {
                user = UserTestData.getUser(usr);
                role = RoleTestData.getRole(rArray[i++]);
                UserRole uRole = new UserRole(user.getUserId(), role.getName());
                adminMgr.deassignUser(uRole);
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".deassignUsersH user [" + (user != null ? user.getUserId() : null) + "] role [" + (role != null ? role.getName() : null) + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public void testAddPermissionOp()
    {
        //     public PermObj addPermObj(PermObj pObj)
        addPermOps("ADD-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false);
        addPermOps("ADD-OPS TOB2 TOP2", PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, true, false);
        addPermOps("ADD-OPS TOB3 TOP3", PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true, false);
        addPermOps("ADD-OPS TOB4 TOP4", PermTestData.OBJS_TOB4, PermTestData.OPS_TOP4, true, false);
        addPermOps("ADD-OPS TOB6 TOP5", PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5, true, false);
    }

    /**
     * @param objArray
     * @param opArray
     */
    public static void addPermOps(String msg, String[][] objArray, String[][] opArray, boolean isAdmin, boolean canFail)
    {
        LogUtil.logIt(msg);
        Permission pOp = new Permission();
        try
        {
            AdminMgr adminMgr;
            if(isAdmin)
            {
                adminMgr = getManagedAdminMgr();
            }
            else
            {
                adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            }
            for (String[] obj : objArray)
            {
                for (String[] op : opArray)
                {
                    pOp = PermTestData.getOp(PermTestData.getName(obj), op);
                    adminMgr.addPermission(pOp);
                    log.debug(CLS_NM + ".addPermOp objectName [" + pOp.getObjectName() + "] opName [" + pOp.getOpName() + "]  objectId [" + pOp.getObjectId() + "] successful");
                }
            }
        }
        catch (SecurityException ex)
        {
            if(!canFail)
            {
                log.error(CLS_NM + ".addPermOp objectName [" + pOp.getObjectName() + "] opName [" + pOp.getOpName() + "]  objectId [" + pOp.getObjectId() + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
                fail(ex.getMessage());
            }
        }
    }


    public void testAddPermissionObj() throws SecurityException
    {
        //     public Permission addPermObj(Permission pOp)
        addPermObjs("ADD-OBS TOB1", PermTestData.OBJS_TOB1, true, false);
        addPermObjs("ADD-OBS TOB2", PermTestData.OBJS_TOB2, true, false);
        addPermObjs("ADD-OBS TOB3", PermTestData.OBJS_TOB3, true, false);
        addPermObjs("ADD-OBS TOB4", PermTestData.OBJS_TOB4, true, false);
        addPermObjs("ADD-OBS TOB6", PermTestData.OBJS_TOB6, true, false);
    }

    /**
     * @param objArray
     */
    public static void addPermObjs(String msg, String[][] objArray, boolean isAdmin, boolean canFail)
    {
        LogUtil.logIt(msg);
        PermObj pObj = new PermObj();
        try
        {
            AdminMgr adminMgr;
            if(isAdmin)
            {
                adminMgr = getManagedAdminMgr();
            }
            else
            {
                adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            }
            for (String[] obj : objArray)
            {
                pObj = PermTestData.getObj(obj);
                // Todo - add props
                adminMgr.addPermObj(pObj);
                log.debug(CLS_NM + ".addPermObjs objectName [" + pObj.getObjectName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            if(!canFail)
            {
                log.error(CLS_NM + ".addPermObjs objectName [" + pObj.getObjectName() + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
                fail(ex.getMessage());
            }
        }
    }


    /**
     *
     * @throws SecurityException
     */
    public void testDeletePermissionOp() throws SecurityException
    {
        //     public void deletePermObj(PermObj pObj)
        delPermOps("DEL-OPS TOB1 TOP1", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false);
        delPermOps("DEL-OPS TOB2 TOP3", PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, true, false);
        delPermOps("DEL-OPS TOB3 TOP4", PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true, false);
        delPermOps("DEL-OPS TOB4 TOP4", PermTestData.OBJS_TOB4, PermTestData.OPS_TOP4, true, false);
        delPermOps("DEL-OPS TOB6 TOP5", PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5, true, false);
    }


    /**
     * @param objArray
     * @param opArray
     */
    public static void delPermOps(String msg, String[][] objArray, String[][] opArray, boolean isAdmin, boolean  canFail)
    {
        LogUtil.logIt(msg);
        Permission pOp = new Permission();
        try
        {
            AdminMgr adminMgr;
            if(isAdmin)
            {
                adminMgr = getManagedAdminMgr();
            }
            else
            {
                adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            }
            for (String[] obj : objArray)
            {
                for (String[] op : opArray)
                {
                    pOp = PermTestData.getOp(PermTestData.getName(obj), op);
                    adminMgr.deletePermission(pOp);
                    log.debug(CLS_NM + ".delPermOps objectName [" + pOp.getObjectName() + "] opName [" + pOp.getOpName() + "]  objectId [" + pOp.getObjectId() + "] successful");
                }
            }
        }
        catch (SecurityException ex)
        {
            if(!canFail)
            {
                log.error(CLS_NM + ".delPermOps objectName [" + pOp.getObjectName() + "] opName [" + pOp.getOpName() + "]  objectId [" + pOp.getObjectId() + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
                fail(ex.getMessage());
            }
        }
    }


    public void testDeletePermissionObj() throws SecurityException
    {
        //     public void deletePermObj(Permission pOp)
        delPermObjs("DEL-OBJS TOB1", PermTestData.OBJS_TOB1, true);
        delPermObjs("DEL-OBJS TOB2", PermTestData.OBJS_TOB2, true);
        delPermObjs("DEL-OBJS TOB3", PermTestData.OBJS_TOB3, true);
        delPermObjs("DEL-OBJS TOB4", PermTestData.OBJS_TOB4, true);
        delPermObjs("DEL-OBJS TOB6", PermTestData.OBJS_TOB6, true);
    }

    /**
     * @param objArray
     */
    public static void delPermObjs(String msg, String[][] objArray, boolean isAdmin)
    {
        LogUtil.logIt(msg);
        PermObj pObj = new PermObj();
        try
        {
            AdminMgr adminMgr;
            if(isAdmin)
            {
                adminMgr = getManagedAdminMgr();
            }
            else
            {
                adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            }
            for (String[] obj : objArray)
            {
                pObj = PermTestData.getObj(obj);
                adminMgr.deletePermObj(pObj);
                log.debug(CLS_NM + ".delPermObjs objectName [" + pObj.getObjectName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".delPermObjs objectName [" + pObj.getObjectName() + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testUpdatePermissionObj() throws SecurityException
    {
        updatePermObjs("UPD-OBS TOB4_UPD", PermTestData.OBJS_TOB4_UPD, true);
    }

    /**
     * @param objArray
     */
    private static void updatePermObjs(String msg, String[][] objArray, boolean isAdmin)
    {
        LogUtil.logIt(msg);
        PermObj pObj = new PermObj();
        try
        {
            AdminMgr adminMgr;
            if(isAdmin)
            {
                adminMgr = getManagedAdminMgr();
            }
            else
            {
                adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            }
            for (String[] obj : objArray)
            {
                pObj = PermTestData.getObj(obj);
                // Todo - add props
                adminMgr.updatePermObj(pObj);
                log.debug(CLS_NM + ".updatePermObjs objectName [" + pObj.getObjectName() + "] successful");
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".updatePermObjs objectName [" + pObj.getObjectName() + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testUpdatePermissionOp()
    {
        updatePermOps("UPD-OPS TOB1 TOP1_UPD", PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1_UPD, true);
    }

    /**
     * @param objArray
     * @param opArray
     */
    private static void updatePermOps(String msg, String[][] objArray, String[][] opArray, boolean isAdmin)
    {
        LogUtil.logIt(msg);
        Permission pOp = new Permission();
        try
        {
            AdminMgr adminMgr;
            if(isAdmin)
            {
                adminMgr = getManagedAdminMgr();
            }
            else
            {
                adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            }
            for (String[] obj : objArray)
            {
                for (String[] op : opArray)
                {
                    pOp = PermTestData.getOp(PermTestData.getName(obj), op);
                    adminMgr.updatePermission(pOp);
                    log.debug(CLS_NM + ".updatePermOps objectName [" + pOp.getObjectName() + "] opName [" + pOp.getOpName() + "]  objectId [" + pOp.getObjectId() + "] successful");
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".updatePermOps objectName [" + pOp.getObjectName() + "] opName [" + pOp.getOpName() + "]  objectId [" + pOp.getObjectId() + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
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
        addRoleGrants("GRNT-PRMS TR1 TOB1 TOP1", RoleTestData.ROLES_TR1, PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true, false);
        addRoleGrants("GRNT-PRMS TR2 TOB2 TOP2", RoleTestData.ROLES_TR2, PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, true, false);
        addRoleGrants("GRNT-PRMS TR3 TOB3 TOP3", RoleTestData.ROLES_TR3, PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true, false);
        addRoleGrantsH("GRNT-PRMS_H ROLES_TR5_HIER TOB4 TOP4", RoleTestData.ROLES_TR5_HIER, PermTestData.OBJS_TOB4, PermTestData.OPS_TOP4);
        addRoleGrantsHB("GRNT-PRMS_HB USERS TU20 ROLES_TR5B TOB6 TOP5", UserTestData.USERS_TU20U_TR5B, RoleTestData.ROLES_TR5B, PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5);
    }


    /**
     * @param rArray
     * @param objArray
     * @param opArray
     */
    public static void addRoleGrants(String msg, String[][] rArray, String[][] objArray, String[][] opArray, boolean isAdmin, boolean canFail)
    {
        LogUtil.logIt(msg);
        Permission pOp = new Permission();
        Role role = new Role();
        try
        {
            AdminMgr adminMgr;
            if(isAdmin)
            {
                adminMgr = getManagedAdminMgr();
            }
            else
            {
                adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            }
            for (String[] rle : rArray)
            {
                for (String[] obj : objArray)
                {
                    for (String[] op : opArray)
                    {
                        role = new Role(RoleTestData.getName(rle));
                        pOp = PermTestData.getOp(PermTestData.getName(obj), op);
                        adminMgr.grantPermission(pOp, role);
                        log.debug(CLS_NM + ".addRoleGrants role name [" + role.getName() + "] objectName [" + pOp.getObjectName() + "] objectId [" + pOp.getObjectId() + "] operation name [" + pOp.getOpName() + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            if(!canFail)
            {
                log.error(CLS_NM + ".addRoleGrants role name [" + role.getName() + "] objectName [" + pOp.getObjectName() + "] objectId [" + pOp.getObjectId() + "] operation name [" + pOp.getOpName() + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
                fail(ex.getMessage());
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
    private void addRoleGrantsH(String msg, String[][] rArray, String[][] objArray, String[][] opArray)
    {
        LogUtil.logIt(msg);
        Permission pOp = new Permission();
        Role role = new Role();
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int i = 0;
            for (String[] obj : objArray)
            {
                role = new Role(RoleTestData.getName(rArray[i++]));
                for (String[] op : opArray)
                {
                    pOp = PermTestData.getOp(PermTestData.getName(obj), op);
                    adminMgr.grantPermission(pOp, role);
                    log.debug(CLS_NM + ".addRoleGrantsH role name [" + role.getName() + "] objectName [" + pOp.getObjectName() + "] objectId [" + pOp.getObjectId() + "] operation name [" + pOp.getOpName() + "] successful");
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addRoleGrantsH role name [" + role.getName() + "] objectName [" + pOp.getObjectName() + "] objectId [" + pOp.getObjectId() + "] operation name [" + pOp.getOpName() + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     *
     * @param msg
     * @param rArray
     * @param objArray
     * @param opArray
     */
    private void addRoleGrantsHB(String msg, String[][] uArray, String[][] rArray, String[][] objArray, String[][] opArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] obj : objArray)
            {
                int i = 0;
                for (String[] rle : rArray)
                {
                    // Get Role[i] from test data:
                    Role role = RoleTestData.getRole(rle);
                    // Get Permission[i] from test data:
                    String[] op = opArray[i];
                    // Load the Permission entity with data:
                    Permission pOp = PermTestData.getOp(PermTestData.getName(obj), op);

                    // Grant Permission[i] to Role[i]:
                    adminMgr.grantPermission(pOp, role);

                    // Get User[i] from test data:
                    String[] usr = uArray[i];
                    // Load the User entity with data:
                    User user = UserTestData.getUser(usr);
                    // Grant Permission[i] to User[i]:
                    adminMgr.grantPermission(pOp, user);
                    i++;

                    log.debug(CLS_NM + ".addRoleGrantsHB userId [" + user.getUserId() + "] role name [" + role.getName() + "] objectName [" + pOp.getObjectName() + "] objectId [" + pOp.getObjectId() + "] operation name [" + pOp.getOpName() + "] successful");
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addRoleGrantsHB caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     * AMT25
     *
     * @throws us.jts.fortress.SecurityException
     */
    public void testRevokePermissionRole()
    {
        //     public void revokePermission(Permission pOp, Role role)
        delRoleGrants("REVK-PRMS TR1 TOB1 TOP1", RoleTestData.ROLES_TR1, PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, true);
        delRoleGrants("REVK-PRMS TR2 TOB2 TOP2", RoleTestData.ROLES_TR2, PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, true);
        delRoleGrants("REVK-PRMS TR3 TOB3 TOP3", RoleTestData.ROLES_TR3, PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true);
        delRoleGrantsH("REVK-PRMS_H ROLES_TR5_HIER TOB4 TOP4", RoleTestData.ROLES_TR5_HIER, PermTestData.OBJS_TOB4, PermTestData.OPS_TOP4);
        delRoleGrantsHB("REVK-PRMS_H USERS TU20 ROLES_TR5B TOB6 TOP5", UserTestData.USERS_TU20U_TR5B, RoleTestData.ROLES_TR5B, PermTestData.OBJS_TOB6, PermTestData.OPS_TOP5);
    }


    /**
     * @param rArray
     * @param objArray
     * @param opArray
     */
    public static void delRoleGrants(String msg, String[][] rArray, String[][] objArray, String[][] opArray, boolean isAdmin)
    {
        LogUtil.logIt(msg);
        Permission pOp = new Permission();
        Role role = new Role();
        try
        {
            AdminMgr adminMgr;
            if(isAdmin)
            {
                adminMgr = getManagedAdminMgr();
            }
            else
            {
                adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            }
            for (String[] rle : rArray)
            {
                for (String[] obj : objArray)
                {
                    for (String[] op : opArray)
                    {
                        role = new Role(RoleTestData.getName(rle));
                        pOp = PermTestData.getOp(PermTestData.getName(obj), op);
                        adminMgr.revokePermission(pOp, role);
                        log.debug(CLS_NM + ".delRoleGrants role name [" + role.getName() + "] objectName [" + pOp.getObjectName() + "] objectId [" + pOp.getObjectId() + "] operation name [" + pOp.getOpName() + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".delRoleGrants role name [" + role.getName() + "] objectName [" + pOp.getObjectName() + "] objectId [" + pOp.getObjectId() + "] operation name [" + pOp.getOpName() + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * 
     * @param msg
     * @param rArray
     * @param objArray
     * @param opArray
     */
    private void delRoleGrantsH(String msg, String[][] rArray, String[][] objArray, String[][] opArray)
    {
        LogUtil.logIt(msg);
        Permission pOp = new Permission();
        Role role = new Role();
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            int i = 0;
            for (String[] obj : objArray)
            {
                role = new Role(RoleTestData.getName(rArray[i++]));
                for (String[] op : opArray)
                {
                    pOp = PermTestData.getOp(PermTestData.getName(obj), op);
                    adminMgr.revokePermission(pOp, role);
                    log.debug(CLS_NM + ".delRoleGrantsH role name [" + role.getName() + "] objectName [" + pOp.getObjectName() + "] objectId [" + pOp.getObjectId() + "] operation name [" + pOp.getOpName() + "] successful");
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".delRoleGrantsH role name [" + role.getName() + "] objectName [" + pOp.getObjectName() + "] objectId [" + pOp.getObjectId() + "] operation name [" + pOp.getOpName() + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     *
     * @param msg
     * @param rArray
     * @param objArray
     * @param opArray
     */
    private void delRoleGrantsHB(String msg, String[][] uArray, String[][] rArray, String[][] objArray, String[][] opArray)
    {
        LogUtil.logIt(msg);
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] obj : objArray)
            {
                int i = 0;
                for (String[] rle : rArray)
                {
                    // Get Role[i] from test data:
                    Role role = RoleTestData.getRole(rle);
                    // Get Permission[i] from test data:
                    String[] op = opArray[i];
                    // Load the permission entity with data:
                    Permission pOp = PermTestData.getOp(PermTestData.getName(obj), op);
                    // Grant Permission[i] to Role[i]:
                    adminMgr.revokePermission(pOp, role);

                    // Get User[i] from test data:
                    String[] usr = uArray[i];
                    // Load the User entity with data:
                    User user = UserTestData.getUser(usr);
                    // Grant Permission[i] to User[i]:
                    adminMgr.revokePermission(pOp, user);
                    i++;

                    log.debug(CLS_NM + ".delRoleGrantsHB userId [" + user.getUserId() + "] role name [" + role.getName() + "] objectName [" + pOp.getObjectName() + "] objectId [" + pOp.getObjectId() + "] operation name [" + pOp.getOpName() + "] successful");
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".delRoleGrantsHB caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testGrantPermissionUser()
    {
        //     public void grantPermission(Permission pOp, User user)
        addUserGrants("GRNT-PRMS TU1 TOB1 TOP1", UserTestData.USERS_TU1, PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1);
    }


    /**
     * @param uArray
     * @param objArray
     * @param opArray
     */
    private void addUserGrants(String msg, String[][] uArray, String[][] objArray, String[][] opArray)
    {
        LogUtil.logIt(msg);
        Permission pOp = new Permission();
        User user = new User();
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] usr : uArray)
            {
                for (String[] obj : objArray)
                {
                    for (String[] op : opArray)
                    {
                        user = new User(UserTestData.getUserId(usr));
                        pOp = PermTestData.getOp(PermTestData.getName(obj), op);
                        adminMgr.grantPermission(pOp, user);
                        log.debug(CLS_NM + ".addUserGrants userId [" + user.getUserId() + "] objectName [" + pOp.getObjectName() + "] objectId [" + pOp.getObjectId() + "] operation name [" + pOp.getOpName() + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".addUserGrants userId [" + user.getUserId() + "] objectName [" + pOp.getObjectName() + "] objectId [" + pOp.getObjectId() + "] operation name [" + pOp.getOpName() + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public void testRevokePermissionUser()
    {
        //     public void revokePermission(Permission pOp, User user)
        delUserGrants("RVK-PRMS TU1 TOB1 TOP1", UserTestData.USERS_TU1, PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1);
    }


    /**
     * @param uArray
     * @param objArray
     * @param opArray
     */
    private void delUserGrants(String msg, String[][] uArray, String[][] objArray, String[][] opArray)
    {
        LogUtil.logIt(msg);
        Permission pOp = new Permission();
        User user = new User();
        try
        {
            AdminMgr adminMgr = getManagedAdminMgr();
            for (String[] usr : uArray)
            {
                for (String[] obj : objArray)
                {
                    for (String[] op : opArray)
                    {
                        user = new User(UserTestData.getUserId(usr));
                        pOp = PermTestData.getOp(PermTestData.getName(obj), op);
                        adminMgr.revokePermission(pOp, user);
                        log.debug(CLS_NM + ".delUserGrants userId [" + user.getUserId() + "] objectName [" + pOp.getObjectName() + "] objectId [" + pOp.getObjectId() + "] operation name [" + pOp.getOpName() + "] successful");
                    }
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(CLS_NM + ".delUserGrants userId [" + user.getUserId() + "] objectName [" + pOp.getObjectName() + "] objectId [" + pOp.getObjectId() + "] operation name [" + pOp.getOpName() + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     *
     * @return
     * @throws us.jts.fortress.SecurityException
     */
    public static AdminMgr getManagedAdminMgr() throws SecurityException
    {
        if(FortressJUnitTest.isAdminEnabled() && adminSess == null)
        {
            adminSess = DelegatedMgrImplTest.createAdminSession();
        }
        return AdminMgrFactory.createInstance(TestUtils.getContext(), adminSess);
    }
}
