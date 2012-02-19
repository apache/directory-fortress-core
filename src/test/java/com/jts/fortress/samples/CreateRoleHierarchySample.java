/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.samples;

import com.jts.fortress.*;
import com.jts.fortress.SecurityException;
import com.jts.fortress.rbac.Role;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

/**
 * CreateRoleHierarchySample JUnit Test. This test program will show how to build a simple Role hierarchy which are
 * used to enable administrators to subclass common Roles that can be inherited.  This system supports the
 * RBAC General Hierarchical Role specs.  The Roles are simple containing a name only.  The Roles may also be created containing Date/Time constraints.
 * Roles constrained by Date and Time can be overridden when the Role is assigned to a User, see {@link CreateUserRoleSample}.
 * Temporal constraints checks will be performed when the Role is activated, see {@link CreateSessionSample}.
 *
 * @author smckinn
 * @created March 18, 2011
 */
public class CreateRoleHierarchySample extends TestCase
{
    private static final String CLS_NM = CreateRoleHierarchySample.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);

    // This constant will be added to index for creation of multiple nodes in directory.
    public static final String TEST_HIER_ROLE_PREFIX = "sampleHierRole";
    public static final String TEST_HIER_BASE_ROLE = "sampleHierRole1";
    public static int TEST_NUMBER = 6;

    public static final String TEST_HIER_DESC_ROLE_PREFIX = "sampleHierRoleD";
    public static final String TEST_HIER_ASC_ROLE_PREFIX = "sampleHierRoleA";


    public CreateRoleHierarchySample(String name)
    {
        super(name);
    }

    /**
     * Run the Role test cases.
     *
     * @return
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();

        if(!AllSamplesJUnitTest.isFirstRun())
        {
            suite.addTest(new CreateRoleHierarchySample("testDeleteHierRoles"));
            suite.addTest(new CreateRoleHierarchySample("testDeleteDescendantRoles"));
            suite.addTest(new CreateRoleHierarchySample("testDeleteAscendantRoles"));
        }

        suite.addTest(new CreateRoleHierarchySample("testCreateHierRoles"));
        suite.addTest(new CreateRoleHierarchySample("testCreateDescendantRoles"));
        suite.addTest(new CreateRoleHierarchySample("testCreateAscendantRoles"));

        /*
        suite.addTest(new CreateRoleHierarchySample("testCreateHierRoles"));

        suite.addTest(new CreateRoleHierarchySample("testDeleteDescendantRoles"));
        suite.addTest(new CreateRoleHierarchySample("testCreateDescendantRoles"));

        suite.addTest(new CreateRoleHierarchySample("testDeleteAscendantRoles"));
        suite.addTest(new CreateRoleHierarchySample("testCreateAscendantRoles"));
        */
        return suite;
    }

    /**
     * Remove the simple hierarchical Roles from the directory.  Before removal call the API to move the relationship
     * between the parent and child Roles.  Once the relationship is removed the parent Role can be removed.
     * Role removal will trigger automatic deassignment from all Users or revocation of Permission as well.
     * <p>
     * <img src="../../../../images/HierRoleSimple.png">
     */
    public static void testDeleteHierRoles()
    {
        String szLocation = CLS_NM + ".testDeleteHierRoles";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance();
            for (int i = 1; i < TEST_NUMBER; i++)
            {
                // The key that must be set to locate any Role is simply the name.
                Role parentRole = new Role(TEST_HIER_ROLE_PREFIX + i);
                Role childRole = new Role(TEST_HIER_ROLE_PREFIX + (i + 1));

                adminMgr.deleteInheritance(parentRole, childRole);

                // Remove the Role from directory along with associated assignments:
                adminMgr.deleteRole(parentRole);
                log.info(szLocation + " role [" + parentRole.getName() + "] success");
            }
            // Remove the Role from directory along with associated assignments:
            adminMgr.deleteRole(new Role(TEST_HIER_ROLE_PREFIX + TEST_NUMBER));
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Add a simple Role hierarchy to ldap.  The Roles will named to include a name,'sampleHierRole', appended with the
     * sequence of 1 - 6.  sampleHierRole1 is the root or highest level Role in the structure while sampleHierRole6 is the lowest
     * most child.  Fortress Roles may have multiple parents which is demonstrated in testCreateAscendantRoles sample.
     * <p>
     * <img src="../../../../images/HierRoleSimple.png">
     */
    public static void testCreateHierRoles()
    {
        String szLocation = CLS_NM + ".testCreateHierRoles";
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance();

            // Instantiate the Role entity.
            Role baseRole = new Role(TEST_HIER_BASE_ROLE);

            // Add the Role entity to the directory.
            adminMgr.addRole(baseRole);

            // Create roles, sampleHierRole2 - sampleHierRole10
            for (int i = 2; i < TEST_NUMBER + 1; i++)
            {
                // Instantiate the Role entity.
                Role childRole = new Role(TEST_HIER_ROLE_PREFIX + i);

                // Add the Role entity to the directory.
                adminMgr.addRole(childRole);

                // Now add Role relationship to the directory between parent and child Roles.
                Role parentRole = new Role(TEST_HIER_ROLE_PREFIX + (i - 1));
                adminMgr.addInheritance(parentRole, childRole);
            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     * Demonstrate teardown of a parent to child relationship of one-to-many.  Each child must first remove the inheritance
     * relationship with parent before being removed from ldap.  The parent Role will be removed from ldap last.
     * Role removal will trigger automatic deassignment from all Users or revocation of Permission as well.
     * <p>
     * <img src="../../../../images/HierRoleDescendants.png">
     */
    public static void testDeleteDescendantRoles()
    {
        String szLocation = CLS_NM + ".testDeleteDescendantRoles";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance();

            // This parent has many children.  They must be deleted before parent itself can.
            Role parentRole = new Role(TEST_HIER_DESC_ROLE_PREFIX + 1);

            // There are 10 Roles to process:
            for (int i = 2; i < TEST_NUMBER + 1; i++)
            {
                Role childRole = new Role(TEST_HIER_DESC_ROLE_PREFIX + i);
                adminMgr.deleteInheritance(parentRole, childRole);

                // Remove the child Role from directory along with associated assignments:
                adminMgr.deleteRole(childRole);
                log.info(szLocation + " role [" + childRole.getName() + "] success");
            }
            // Remove the parent Role from directory along with associated assignments:
            adminMgr.deleteRole(parentRole);
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Demonstrate a parent to child Role structure of one-to-many.  The parent Role must be created before
     * the call to addDescendant which will Add a new Role node and set a Role relationship with parent node.
     * <p>
     * <img src="../../../../images/HierRoleDescendants.png">
     */
    public static void testCreateDescendantRoles()
    {
        String szLocation = CLS_NM + ".testCreateDescendantRoles";
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance();

            // Instantiate the Role entity.
            Role parentRole = new Role(TEST_HIER_DESC_ROLE_PREFIX + 1);

            // This parent will have many children:
            adminMgr.addRole(parentRole);

            // Create roles, sampleHierRoleD2 - sampleHierRoleD10
            for (int i = 1; i < TEST_NUMBER; i++)
            {
                // Now add Role relationship to the directory between parent and child Roles.
                Role childRole = new Role(TEST_HIER_DESC_ROLE_PREFIX + (i + 1));
                adminMgr.addDescendant(parentRole, childRole);
            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * This example demonstrates tear down of a child to parent represented as one-to-many.  The parents must all
     * be removed from the child before the child can be removed.
     * Role removal will trigger automatic deassignment from all Users or revocation of Permission as well.
     * <p>
     * <img src="../../../../images/HierRoleAscendants.png">
     */
    public static void testDeleteAscendantRoles()
    {
        String szLocation = CLS_NM + ".testDeleteAscendantRoles";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance();

            // This child has many parents:
            Role childRole = new Role(TEST_HIER_ASC_ROLE_PREFIX + 1);

            for (int i = 2; i < TEST_NUMBER + 1; i++)
            {
                Role parentRole = new Role(TEST_HIER_ASC_ROLE_PREFIX + i);
                adminMgr.deleteInheritance(parentRole, childRole);

                // Remove the Role from directory along with associated assignments:
                adminMgr.deleteRole(parentRole);
                log.info(szLocation + " role [" + childRole.getName() + "] success");
            }
            // Remove the Role from directory along with associated assignments:
            adminMgr.deleteRole(childRole);
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Demonstrate a child to parent Role structure of one-to-many.  To use this API, the child Role must be created before
     * the call to addAscendant which will Add a new Role node and set a Role relationship with child node.
     * <p>
     * <img src="../../../../images/HierRoleAscendants.png">
     */
    public static void testCreateAscendantRoles()
    {
        String szLocation = CLS_NM + ".testCreateAscendantRoles";
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance();

            // Instantiate the Role entity.
            Role childRole = new Role(TEST_HIER_ASC_ROLE_PREFIX + 1);

            // This child will have many parents:
            adminMgr.addRole(childRole);

            // Create roles, sampleHierRoleA2 - sampleHierRoleA10
            for (int i = 1; i < TEST_NUMBER; i++)
            {
                // Now add Role relationship to the directory between parent and child Roles.
                Role parentRole = new Role(TEST_HIER_ASC_ROLE_PREFIX + (i + 1));
                adminMgr.addAscendant(childRole, parentRole);
            }
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
}