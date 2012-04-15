/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.samples;

import com.jts.fortress.AdminMgr;
import com.jts.fortress.AdminMgrFactory;
import com.jts.fortress.ReviewMgr;
import com.jts.fortress.ReviewMgrFactory;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.rbac.PermObj;
import com.jts.fortress.rbac.Permission;
import com.jts.fortress.rbac.Role;
import com.jts.fortress.SecurityException;
import com.jts.fortress.rbac.User;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * CreatePermSample JUnit Test demonstrates creation of Fortress permissions.  It also shows how
 * permissions may be granted to Roles and Users.
 *
 * @author Shawn McKinney
 * @created March 6, 2011
 */
public class CreatePermSample extends TestCase
{
    private static final String CLS_NM = CreatePermSample.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);
    public static final String TEST_PERM_OBJECT = "sampleObject1";
    public static final String TEST_PERM_OPERATION_PREFIX = "OPER";

    /**
     * @param name
     */
    public CreatePermSample(String name)
    {
        super(name);
    }

    /**
     * Run the permission test cases.
     *
     * @return
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();

        if(!AllSamplesJUnitTest.isFirstRun())
        {
            suite.addTest(new CreatePermSample("testRevokePermissionRole"));
            suite.addTest(new CreatePermSample("testDelPermObjects"));
        }

        suite.addTest(new CreatePermSample("testAddPermObjects"));
        suite.addTest(new CreatePermSample("testAddPermOperations"));
        suite.addTest(new CreatePermSample("testGrantPermissionRole"));
        suite.addTest(new CreatePermSample("testGrantPermissionUser"));
        suite.addTest(new CreatePermSample("testRevokePermissionUser"));
        return suite;
    }

    /**
     * This test will remove the RBAC Role name associated with a particular Permission Operation node in ldap.
     */
    public static void testRevokePermissionRole()
    {
        String szLocation = CLS_NM + ".testRevokePermissionRole";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance();

            // Instantiate the ReviewMgr implementation which is used to interrogate policy information.
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();

            // Iterate over roles...
            for (int i = 1; i < 11; i++)
            {
                Role inRole = new Role(CreateRoleSample.TEST_ROLE_PREFIX + i);
                List<Permission> perms = reviewMgr.rolePermissions(inRole);
                for(Permission perm : perms)
                {
                    // This API removes the 'oamRoles' attribute associated with Role from the 'oamOperation' ldap object class:
                    adminMgr.revokePermission(perm, inRole);
                }
            }

            // Iterate to ensure all Operation entities no longer contain Role assignments (for test purposes only):
            for (int j = 1; j < 6; j++)
            {
                // Permissions contain Object to Operation mapping and once created can then be targeted for assignment to Role entities in ldap:
                Permission inPerm = new Permission(TEST_PERM_OBJECT, TEST_PERM_OPERATION_PREFIX + j);

                // now retrieve the list of Roles that are still assigned to perm.  This should be a null list because of revocation performed above:
                List<String> assignedRoles = reviewMgr.permissionRoles(inPerm);
                assertNull(assignedRoles);
                log.info(szLocation + " permission roles revocation check for object [" + inPerm.getObjectName() + "] operation name [" + inPerm.getOpName() + "] revocation success");
            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Removal of Permission Object node from ldap will also remove any child Operation nodes that are located
     * directly below.
     */
    public static void testDelPermObjects()
    {
        String szLocation = CLS_NM + ".testDelPermObjects";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance();
            // this will remove the object along with any operations associated with it:
            adminMgr.deletePermObj(new PermObj(TEST_PERM_OBJECT, CreatePermOrgSample.TEST_PERM_OU_NM));

            // Instantiate the ReviewMgr implementation which is used to interrogate policy information.
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();
            try
            {
                // this should fail:
                PermObj outObj = reviewMgr.readPermObj(new PermObj(TEST_PERM_OBJECT));
                fail(szLocation + " permission object delete failed");
            }
            catch (SecurityException se)
            {
                assertTrue(szLocation + " excep id check", se.getErrorId() == GlobalErrIds.PERM_OBJ_NOT_FOUND);
                // pass
            }
            log.info(szLocation + " permission object [" + TEST_PERM_OBJECT + "] success");

        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    public static void testDeleteShoppingCartObjects()
    {
        String szLocation = CLS_NM + ".testDeleteShoppingCartObjects";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the AdminMgr first
            AdminMgr adminMgr = AdminMgrFactory.createInstance();

            // Now Instantiate the Object
            PermObj shoppingCart = new PermObj("ShoppingCart", "KillerBikes.com");

            // Add it to the directory
            adminMgr.deletePermObj(shoppingCart);
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * The Fortress object entity must be created before operations may be granted.  There is a one-to-many
     * relationship between objects and operations.
     */
    public static void testAddPermObjects()
    {
        String szLocation = CLS_NM + ".testAddPermObjects";
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance();

            // Add the PermObj entity to ldap.  The PermObj entity must have a name and an OrgUnit affiliation.
            adminMgr.addPermObj(new PermObj(TEST_PERM_OBJECT, CreatePermOrgSample.TEST_PERM_OU_NM));

            // Instantiate the ReviewMgr implementation which is used to interrogate policy information.
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();
            // now read the newly created Object entity back:
            PermObj outObj = reviewMgr.readPermObj(new PermObj(TEST_PERM_OBJECT));

            // Do some validations.
            assertNotNull(outObj);
            assertTrue(szLocation + " failed obj name check", TEST_PERM_OBJECT.equals(outObj.getObjectName()));
            assertTrue(szLocation + " failed obj ou check", CreatePermOrgSample.TEST_PERM_OU_NM.equals(outObj.getOu()));
            log.info(szLocation + " permission object [" + outObj.getObjectName() + "] success");
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     *
     */
    public static void testAddShoppingCartObjects()
    {
        String szLocation = CLS_NM + ".testAddShoppingCartObjects";
        try
        {
            // Instantiate the AdminMgr first
            AdminMgr adminMgr = AdminMgrFactory.createInstance();

            // Now Instantiate the Object
            PermObj shoppingCart = new PermObj("ShoppingCart", "KillerBikes.com");

            // Add it to the directory
            adminMgr.addPermObj(shoppingCart);

            // Now create the permission operations and grant...
            Permission create = new Permission(shoppingCart.getObjectName(), "create");
            adminMgr.addPermission(create);
            adminMgr.grantPermission(create, new Role("Customer"));

            Permission read = new Permission(shoppingCart.getObjectName(), "read");
            adminMgr.addPermission(read);
            adminMgr.grantPermission(read, new Role("Customer"));

            Permission update = new Permission(shoppingCart.getObjectName(), "update");
            adminMgr.addPermission(update);
            adminMgr.grantPermission(update, new Role("Admin"));

            Permission delete = new Permission(shoppingCart.getObjectName(), "delete");
            adminMgr.addPermission(delete);
            adminMgr.grantPermission(delete, new Role("Manager"));

            Permission checkout = new Permission(shoppingCart.getObjectName(), "checkout");
            adminMgr.addPermission(checkout);
            adminMgr.grantPermission(delete, new Role("Customer"));
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * The Permission entity contains operation name along with any assigned Role and User entities.  The Permission
     * ldap node is located as child node of Permission Object node.
     */
    public static void testAddPermOperations()
    {
        String szLocation = CLS_NM + ".testAddPermOperations";
        try
        {
            AdminMgr adminMgr = AdminMgrFactory.createInstance();
            for (int i = 1; i < 6; i++)
            {
                // The Permission entity is associated with PermObj (name) entity and is uniquely identified by Operation name:
                Permission inPerm = new Permission(TEST_PERM_OBJECT, TEST_PERM_OPERATION_PREFIX + i);

                // The Permission entity will be a child node of specified PermObject entity.
                adminMgr.addPermission(inPerm);

                // Instantiate the ReviewMgr implementation which is used to interrogate policy information.
                ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();

                // now read the newly created Permission entity back.
                Permission outPerm = reviewMgr.readPermission(inPerm);

                // Do some validations.
                assertNotNull(outPerm);
                assertTrue(szLocation + " failed permission check", outPerm.equals(inPerm));
                log.info(szLocation + " permission object [" + outPerm.getObjectName() + "] operation name [" + outPerm.getOpName() + "] success");

            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Permissions contain a multi-occurring String attribute that contains the Role name(s) for which it is granted to.
     * The checkAccess method will determine if User has been assigned to a Role that Permission has been granted to.
     */
    public static void testGrantPermissionRole()
    {
        String szLocation = CLS_NM + ".testGrantPermissionRole";
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance();

            // Iterate over roles...
            for (int i = 1; i < 11; i++)
            {
                Role inRole = new Role(CreateRoleSample.TEST_ROLE_PREFIX + i);
                for (int j = 1; j < 6; j++)
                {
                    // Permissions contain Object to Operation mapping and once created can then be targeted for assignment to Role entities in ldap:
                    Permission inPerm = new Permission(TEST_PERM_OBJECT, TEST_PERM_OPERATION_PREFIX + j);

                    // This API add a 'oamRoles' attribute associated with Role to the 'oamOperation' ldap object class:
                    adminMgr.grantPermission(inPerm, inRole);
                    log.info(szLocation + " permission role [" + inRole.getName() + "] object [" + inPerm.getObjectName() + "] operation name [" + inPerm.getOpName() + "] success");
                }
            }

            // Instantiate the ReviewMgr implementation which is used to interrogate policy information.
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();

            // Iterate test to ensure that all Roles contain the associated Operation assignments:
            for (int i = 1; i < 11; i++)
            {
                // Create this Role to interrogate the system to return all assigned Operation entities:
                Role inRole = new Role(CreateRoleSample.TEST_ROLE_PREFIX + i);

                // Read the list of permissions that have been granted to test Role:
                List<Permission> assignedPerms = reviewMgr.rolePermissions(inRole);
                assertTrue(szLocation + " list check, expected: 5, actual:" + assignedPerms.size(), assignedPerms.size() == 5);
            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     * Fortress allows Permissions to be granted directly to User entities.  Note this is not an RBAC specified
     * capability but can otherwise be useful for certain circumstances.
     */
    public static void testGrantPermissionUser()
    {
        String szLocation = CLS_NM + ".testGrantPermissionUser";
        User inUser = new User(CreateUserSample.TEST_USERID);
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance();
            // Iterate over perms...
            for (int i = 1; i < 6; i++)
            {
                // Permissions contain Object to Operation mapping and once created can then be targeted for assignment to User entities in ldap:
                Permission inPerm = new Permission(TEST_PERM_OBJECT, TEST_PERM_OPERATION_PREFIX + i);

                // This API add a 'oamUsers' attribute associated with User to the 'oamOperation' ldap object class:
                adminMgr.grantPermission(inPerm, inUser);
                log.info(szLocation + " permission user [" + inUser.getUserId() + "] object [" + inPerm.getObjectName() + "] operation name [" + inPerm.getOpName() + "] success");
            }
            // Instantiate the ReviewMgr implementation which is used to interrogate policy information.
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();

            // Iterate over roles...
            for (int i = 1; i < 6; i++)
            {
                // now read the list of Permissions that have been granted to the test User:
                List<Permission> assignedUserPerms = reviewMgr.userPermissions(inUser);
                assertTrue(szLocation + " list check, expected: 5, actual:" + assignedUserPerms.size(), assignedUserPerms.size() == 5);
            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Test will remove the associated User attribute from Permission Operation nodes in LDAP.
     */
    public static void testRevokePermissionUser()
    {
        String szLocation = CLS_NM + ".testRevokePermissionUser";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        User inUser = new User(CreateUserSample.TEST_USERID);
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance();
            // Iterate over perms...
            for (int i = 1; i < 6; i++)
            {
                // Permissions contain Object to Operation mapping and once created can then be targeted for assignment of User entities in ldap:
                Permission inPerm = new Permission(TEST_PERM_OBJECT, TEST_PERM_OPERATION_PREFIX + i);

                // This API will remove the 'oamUsers' attribute associated with User in 'oamOperation' ldap object class:
                adminMgr.revokePermission(inPerm, inUser);

                // Instantiate the ReviewMgr implementation which is used to interrogate policy information.
                ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();
                // now read the list of Users that are still granted.  This should be a null list because of revocation performed above:
                List<String> assignedUsers = reviewMgr.permissionUsers(inPerm);
                assertNull(assignedUsers);
                log.info(szLocation + " permission user [" + inUser.getUserId() + "] object [" + inPerm.getObjectName() + "] operation name [" + inPerm.getOpName() + "] success");
            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
}
