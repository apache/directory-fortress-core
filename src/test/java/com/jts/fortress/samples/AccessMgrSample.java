/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package com.jts.fortress.samples;

import com.jts.fortress.AccessMgr;
import com.jts.fortress.AccessMgrFactory;
import com.jts.fortress.rbac.TestUtils;
import com.jts.fortress.rbac.UserAdminRole;
import com.jts.fortress.rbac.Permission;
import com.jts.fortress.rbac.Session;
import com.jts.fortress.rbac.User;
import com.jts.fortress.SecurityException;
import com.jts.fortress.rbac.UserRole;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

import java.util.Enumeration;
import java.util.List;


/**
 * AccessMgrSample JUnit Test. The APIs exercised within this class are used to perform
 * runtime security policy enforcement for data center applications.  These APIs are thread safe and can be
 * executed within high-volume Java transactional environments like Java EE Web,EJB and Service tier applications,
 * Because these APIs require direct access to the LDAP server, it is not recommended that they be called from
 * thick client applications (i.e. Applets) unless the thick client is running on a machine that is "behind the firewall" (or on VPN) that
 * are common to enterprise data center runtime environments.
 *
 * @author Shawn McKinney
 */
public class AccessMgrSample extends TestCase
{
    private static final String CLS_NM = AccessMgrSample.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);

    public AccessMgrSample(String name)
    {
        super(name);
    }

    /**
     * Run the Fortress AccessMgr samples.
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest(new AccessMgrSample("testCheckAccess"));
        suite.addTest(new AccessMgrSample("testSessionPermissions"));
        suite.addTest(new AccessMgrSample("testSessionRoles"));
        suite.addTest(new AccessMgrSample("testAddActiveRoles"));
        suite.addTest(new AccessMgrSample("testDropActiveRoles"));
        suite.addTest(new AccessMgrSample("testDisplayUserSession"));

        return suite;
    }

    /**
     * The checkAccess API is used to perform authorization on User.  It will return a 'true' if User is authorized to
     * perform operation or a 'false' if User is not.  This API is useful for performing method or service level authorization
     * within Server side programs.  It is expected that this API will be wrapped by other application Security frameworks
     * i.e. Spring or Java EE to provide fine-grained permission check authorization capabilities to business applications
     * running in the datacenter.
     */
    public static void testCheckAccess()
    {
        String szLocation = CLS_NM + ".testCheckAccess";
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance(TestUtils.getContext());

            // utility function will create an Fortress Session.  The Session contains the user's activated
            // roles along with other related attributes and status information (i.e. password status)
            Session session = createSession(CreateUserSample.TEST_USERID, CreateUserSample.TEST_PASSWORD.toCharArray(), accessMgr);
            assertNotNull(session);
            for (int i = 1; i < 6; i++)
            {
                // Fortress Permissions have an Object name and Operation name.  There is a one to many relationship between
                // objects and operations.  An example is object name "MyDataBaseTable" operations "READ", "WRITE", "DELETE". or object "MyFile" operations "R", "W", "C" or "MyClassName" "methodA", "methodB", "methodC", or "MyPageName.ControlName" "checkOut", "applyDiscount".
                Permission inPerm = new Permission(CreatePermSample.TEST_PERM_OBJECT, CreatePermSample.TEST_PERM_OPERATION_PREFIX + i);
                // method will return a 'true' if authorized or 'false' if not.
                boolean result = accessMgr.checkAccess(session, inPerm);
                assertTrue(szLocation, result);
                log.info(szLocation + " user [" + session.getUserId() + "] permission object [" + inPerm.getObjectName() + "] operation name [" + inPerm.getOpName() + "] success");
            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * The sessionPermissions API is useful for GUI programs that need to cache all of the User's Permissions in the
     * HTTP Session or application cache.  This is useful when providing access control lists for menu items and other
     * controls that sometimes need to check authorizations on.  This API will return all permissions that are granted
     * to User's activated Roles along with Permissions that have been granted directly to the User entity itself.
     */
    public static void testSessionPermissions()
    {
        String szLocation = CLS_NM + ".testSessionPermissions";
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance(TestUtils.getContext());

            // utility function will create an Fortress Session.  The Session contains the user's activated
            // roles along with other related attributes and status information (i.e. password status)
            Session session = createSession(CreateUserSample.TEST_USERID, CreateUserSample.TEST_PASSWORD.toCharArray(), accessMgr);
            assertNotNull(session);
            List<Permission> perms = accessMgr.sessionPermissions(session);
            assertNotNull(perms);
            assertTrue(szLocation + " list check, expected: 5, actual:" + perms.size(), perms.size() == 5);

            // iterate over expected permissions to make sure they are returned from sessionPermissions API.
            for (int i = 1; i < 6; i++)
            {
                // A Permission consists of an object name and operation name.
                Permission checkPerm = new Permission(CreatePermSample.TEST_PERM_OBJECT, CreatePermSample.TEST_PERM_OPERATION_PREFIX + i);
                boolean result = accessMgr.checkAccess(session, checkPerm);
                assertTrue(szLocation, result);
                log.info(szLocation + " user [" + session.getUserId() + "] permission object [" + checkPerm.getObjectName() + "] operation name [" + checkPerm.getOpName() + "] success");
            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * The RBAC Session can be interrogated to return the list of all activated Roles within a User's Session.  The API
     * will cache these Roles in the User's Session object.  The Roles will also include temporal data that is used to
     * enforce the day, date and time for which a given Role may be placed in the User's Session.
     */
    public static void testSessionRoles()
    {
        String szLocation = CLS_NM + ".testSessionRoles";
        User inUser = new User(CreateUserSample.TEST_USERID);
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance(TestUtils.getContext());
            // utility function will create an Fortress Session.  The Session contains the user's activated
            // roles along with other related attributes and status information (i.e. password status)
            Session session = createSession(CreateUserSample.TEST_USERID, CreateUserSample.TEST_PASSWORD.toCharArray(), accessMgr);
            // A null Session would be a bug and should never happen.  Fortress will throw a SecurityException if it cannot create.
            assertNotNull(session);
            // Get the activated Roles from the Session.
            List<UserRole> uRoles = accessMgr.sessionRoles(session);
            // The list of Roles could be null if User has not been assigned any or if all assigned failed activation checks.
            assertNotNull(uRoles);
            // Test to see that the list size is same as expected.
            assertTrue(szLocation + " list check, expected: 10, actual:" + uRoles.size(), uRoles.size() == 10);

            // Test to ensure that all of the roles activated are returned in the uRoles list.  In a real
            // program this would not be necessary.
            for (int i = 1; i < 11; i++)
            {
                UserRole inUserRole = new UserRole(inUser.getUserId(), CreateRoleSample.TEST_ROLE_PREFIX + i);
                assertTrue(szLocation + " contains check userId [" + inUserRole.getUserId() + "] role [" + inUserRole.getName() + "]", uRoles.contains(inUserRole));
                log.info(szLocation + " userId [" + inUserRole.getUserId() + "] activated role [" + inUserRole.getName() + "] found in session");
            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * The addActivateRole API allows only Roles that have been assigned to a given User to be activated in their
     * RBAC Session.  The API will also ensure that a given Role has passed its constraint tests which include
     * Static Separation of Duty (SSD) and RBAC Role temporal constraint validations.
     */
    public static void testAddActiveRoles()
    {
        String szLocation = CLS_NM + ".testAddActiveRoles";
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance(TestUtils.getContext());
            // authenticate will check the password but will not activated any roles into Session.
            Session session = authenticate(CreateUserSample.TEST_USERID, CreateUserSample.TEST_PASSWORD.toCharArray(), accessMgr);
            assertNotNull(session);
            // now, activate roles into User's Session one at a time:
            for (int i = 1; i < 11; i++)
            {
                UserRole addUserRole = new UserRole(CreateUserSample.TEST_USERID, CreateRoleSample.TEST_ROLE_PREFIX + i);
                accessMgr.addActiveRole(session, addUserRole);
                log.info(szLocation + " userId [" + addUserRole.getUserId() + "] activated role [" + addUserRole.getName() + "] added to session");
            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * RBAC compliant systems allow User Roles to be activated and deactivated from their Session.  This facilitates
     * the principle of least privilege which prescribes only giving User's as much capability as they need to complete
     * their job duties.  This means not all Roles that a User may be authorized to activated will necessarily be active
     * at any one point in time.  This allows for separation of duty restrictions to be enforced.
     */
    public static void testDropActiveRoles()
    {
        String szLocation = CLS_NM + ".testDropActiveRoles";
        User inUser = new User(CreateUserSample.TEST_USERID);
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance(TestUtils.getContext());
            // Calling createSession and not setting any roles on User beforehand will attempt to activate all assigned Roles:
            Session session = createSession(CreateUserSample.TEST_USERID, CreateUserSample.TEST_PASSWORD.toCharArray(), accessMgr);
            assertNotNull(session);
            // now, drop roles from User's Session one at a time:
            for (int i = 1; i < 11; i++)
            {
                UserRole dropUserRole = new UserRole(inUser.getUserId(), CreateRoleSample.TEST_ROLE_PREFIX + i);
                accessMgr.dropActiveRole(session, dropUserRole);
                log.info(szLocation + " userId [" + dropUserRole.getUserId() + "] deactivated role [" + dropUserRole.getName() + "] removed from session");
            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * This test will display all of the User Session attributes to the System out of test machine.  It is intended
     * to demonstrate what data is carried within a User's Fortress Session object.
     */
    public static void testDisplayUserSession()
    {
        String szLocation = CLS_NM + ".testDisplayUserSession";
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance(TestUtils.getContext());
            // utility function will create an Fortress Session.  The Session contains the user's activated
            // roles along with other related attributes and status information (i.e. password status)
            Session session = createSession(CreateUserSample.TEST_USERID, CreateUserSample.TEST_PASSWORD.toCharArray(), accessMgr);
            assertNotNull(session);
            User user = accessMgr.getUser(session);
            assertNotNull(user);
            log.info(szLocation);
            log.info("S   UID  [" + session.getUserId() + "]:");
            log.info("S   IID  [" + session.getInternalUserId() + "]");
            log.info("S   ERR  [" + session.getErrorId() + "]");
            log.info("S   WARN [" + session.getWarningId() + "]");
            log.info("S   MSG  [" + session.getMsg() + "]");
            log.info("S   EXP  [" + session.getExpirationSeconds() + "]");
            log.info("S   GRAC [" + session.getGraceLogins() + "]");
            log.info("S   AUTH [" + session.isAuthenticated() + "]");
            log.info("S   LAST [" + session.getLastAccess() + "]");
            log.info("S   SID  [" + session.getSessionId() + "]");
            log.info("------------------------------------------");
            log.info("U   UID  [" + user.getUserId() + "]");
            log.info("U   IID  [" + user.getInternalId() + "]");
            log.info("U   CN   [" + user.getCn() + "]");
            log.info("U   DESC [" + user.getDescription() + "]");
            log.info("U   OU   [" + user.getOu() + "]");
            log.info("U   SN   [" + user.getSn() + "]");
            log.info("U   BDTE [" + user.getBeginDate() + "]");
            log.info("U   EDTE [" + user.getEndDate() + "]");
            log.info("U   BLDT [" + user.getBeginLockDate() + "]");
            log.info("U   ELDT [" + user.getEndLockDate() + "]");
            log.info("U   DMSK [" + user.getDayMask() + "]");
            log.info("U   TO   [" + user.getTimeout() + "]");
            log.info("U   REST [" + user.isReset() + "]");
            if (user.getProperties() != null && user.getProperties().size() > 0)
            {
                int ctr = 0;
                for (Enumeration e = user.getProperties().propertyNames(); e.hasMoreElements();)
                {
                    String key = (String) e.nextElement();
                    String val = user.getProperty(key);
                    log.info("U   PROP[" + ctr++ + "]=" + key + " VAL=" + val);
                }
            }

            List<UserRole> roles = session.getRoles();
            if (roles != null)
            {
                for (int i = 0; i < roles.size(); i++)
                {
                    UserRole ur = roles.get(i);
                    log.info("    USER ROLE[" + i + "]:");
                    log.info("        role name [" + ur.getName() + "]");
                    log.info("        begin time [" + ur.getBeginTime() + "]");
                    log.info("        end time [" + ur.getEndTime() + "]");
                    log.info("        begin date [" + ur.getBeginDate() + "]");
                    log.info("        end date [" + ur.getEndDate() + "]");
                    log.info("        begin lock [" + ur.getBeginLockDate() + "]");
                    log.info("        end lock [" + ur.getEndLockDate() + "]");
                    log.info("        day mask [" + ur.getDayMask() + "]");
                    log.info("        time out [" + ur.getTimeout() + "]");
                }
            }

            List<UserAdminRole> aRoles = session.getAdminRoles();
            if (aRoles != null)
            {
                for (int i = 0; i < aRoles.size(); i++)
                {
                    UserAdminRole ur = aRoles.get(i);
                    log.info("    USER ADMIN ROLE[" + i + "]:");
                    log.info("        admin role name [" + ur.getName() + "]");
                    log.info("        OsU [" + ur.getOsU() + "]");
                    log.info("        OsP [" + ur.getOsP() + "]");
                    log.info("        begin range [" + ur.getBeginRange() + "]");
                    log.info("        end range [" + ur.getEndRange() + "]");
                    log.info("        begin time [" + ur.getBeginTime() + "]");
                    log.info("        end time [" + ur.getEndTime() + "]");
                    log.info("        begin date [" + ur.getBeginDate() + "]");
                    log.info("        end date [" + ur.getEndDate() + "]");
                    log.info("        begin lock [" + ur.getBeginLockDate() + "]");
                    log.info("        end lock [" + ur.getEndLockDate() + "]");
                    log.info("        day mask [" + ur.getDayMask() + "]");
                    log.info("        time out [" + ur.getTimeout() + "]");
                }
            }

            java.util.Properties jProps = System.getProperties();
            if (jProps != null && jProps.size() > 0)
            {
                int ctr = 0;
                for (Enumeration e = jProps.propertyNames(); e.hasMoreElements();)
                {
                    String key = (String) e.nextElement();
                    String val = jProps.getProperty(key);
                    log.info("J   PROP[" + ctr++ + "]=" + key + " VAL=" + val);
                }
            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * @param userId   String contains case insensitive userId field.
     * @param password String contains case sensitive, clear text password field.
     * @return User RBAC Session that is used for subsequent AccessMgr API calls.
     */
    private static Session createSession(String userId, char[] password, AccessMgr accessMgr)
    {
        String szLocation = CLS_NM + ".createSession";
        Session session = null;
        try
        {
            User user = new User(userId, password);
            // These properties will be persisted within User's audit trail in OpenLDAP:
            user.addProperty("system.user.name", System.getProperty("user.name"));
            //user.addProperty("system.timezone VAL", System.getProperty("user.timezone VAL"));
            user.addProperty("system.country", System.getProperty("user.country"));

            // utility function will create an Fortress Session.  The Session contains the user's activated
            // roles along with other related attributes and status information (i.e. password status)
            session = accessMgr.createSession(user, false);
            log.info(szLocation + " successful");
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
        return session;
    }

    /**
     * Test Utility wraps OpenAcccessManager createSession API.
     *
     * @param userId          String contains case insensitive userId field.
     * @param password        String contains case sensitive, clear text password field.
     * @param activationRoles array of Role names targeted for activation into User's RBAC Session.
     * @return User RBAC Session that is used for subsequent AccessMgr API calls.
     */
    private static Session createSession(String userId, char[] password, String[] activationRoles, AccessMgr accessMgr)
    {
        String szLocation = CLS_NM + ".createSession";
        Session session = null;
        try
        {
            User user = new User(userId, password, activationRoles);
            user.addProperty("system.user.name", System.getProperty("user.name"));
            //user.addProperty("system.timezone VAL", System.getProperty("user.timezone VAL"));
            user.addProperty("system.country", System.getProperty("user.country"));

            // Create an Fortress Session.  The Session contains the user's activated
            // roles along with other related attributes and status information (i.e. password status)
            session = accessMgr.createSession(user, false);
            log.info(szLocation + " with roles successful");
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " with roles caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
        return session;
    }


    /**
     * The authenticate API is used for use cases where RBAC authorization is not required.  This API will authenticate
     * the User's password and will check password policies but will not activate User's Roles into the return Session.
     *
     * @param userId   String contains case insensitive userId field.
     * @param password String contains case sensitive, clear text password field.
     * @return User Session that has no Roles activated thus will fail checkAccess and sessionPermission calls.
     */
    private static Session authenticate(String userId, char[] password, AccessMgr accessMgr)
    {
        String szLocation = CLS_NM + ".authenticate";
        Session session = null;
        try
        {
            // authenticate will check the password but will not activated any roles into Session.
            session = accessMgr.authenticate(userId, password);
            log.info(szLocation + " successful");
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
        return session;
    }
}
