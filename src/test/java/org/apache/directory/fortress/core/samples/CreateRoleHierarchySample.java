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
package org.apache.directory.fortress.core.samples;

import org.apache.directory.fortress.core.AdminMgr;
import org.apache.directory.fortress.core.AdminMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.impl.TestUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CreateRoleHierarchySample JUnit Test. This test program will show how to build a simple Role hierarchy which are
 * used to enable administrators to subclass common Roles that can be inherited.  This system supports the
 * RBAC General Hierarchical Role specs.  The Roles are simple containing a name only.  The Roles may also be created containing Date/Time constraints.
 * Roles constrained by Date and Time can be overridden when the Role is assigned to a User, see {@link CreateUserRoleSample}.
 * Temporal constraints checks will be performed when the Role is activated, see {@link CreateSessionSample}.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CreateRoleHierarchySample extends TestCase
{
    private static final String CLS_NM = CreateRoleHierarchySample.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    // This constant will be added to index for creation of multiple nodes in directory.
    private static final String TEST_HIER_ROLE_PREFIX = "sampleHierRole";
    private static final String TEST_HIER_BASE_ROLE = "sampleHierRole1";
    private static final int TEST_NUMBER = 6;

    private static final String TEST_HIER_DESC_ROLE_PREFIX = "sampleHierRoleD";
    private static final String TEST_HIER_ASC_ROLE_PREFIX = "sampleHierRoleA";


    public CreateRoleHierarchySample(String name)
    {
        super(name);
    }

    /**
     * Run the Role test cases.
     *
     * @return Test
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
        return suite;
    }

    /**
     * Remove the simple hierarchical Roles from the directory.  Before removal call the API to move the relationship
     * between the parent and child Roles.  Once the relationship is removed the parent Role can be removed.
     * Role removal will trigger automatic deassignment from all Users or revocation of Permission as well.
     * <p>
     * <img src="./doc-files/HierRoleSimple.png" alt="">
     */
    public static void testDeleteHierRoles()
    {
        String szLocation = ".testDeleteHierRoles";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            for (int i = 1; i < TEST_NUMBER; i++)
            {
                // The key that must be set to locate any Role is simply the name.
                Role parentRole = new Role(TEST_HIER_ROLE_PREFIX + i);
                Role childRole = new Role(TEST_HIER_ROLE_PREFIX + (i + 1));

                adminMgr.deleteInheritance(parentRole, childRole);

                // Remove the Role from directory along with associated assignments:
                adminMgr.deleteRole(parentRole);
                LOG.info(szLocation + " role [" + parentRole.getName() + "] success");
            }
            // Remove the Role from directory along with associated assignments:
            adminMgr.deleteRole(new Role(TEST_HIER_ROLE_PREFIX + TEST_NUMBER));
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Add a simple Role hierarchy to ldap.  The Roles will named to include a name,'sampleHierRole', appended with the
     * sequence of 1 - 6.  sampleHierRole1 is the root or highest level Role in the structure while sampleHierRole6 is the lowest
     * most child.  Fortress Roles may have multiple parents which is demonstrated in testCreateAscendantRoles sample.
     * <p>
     * <img src="./doc-files/HierRoleSimple.png" alt="">
     */
    public static void testCreateHierRoles()
    {
        String szLocation = ".testCreateHierRoles";
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());

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
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     * Demonstrate teardown of a parent to child relationship of one-to-many.  Each child must first remove the inheritance
     * relationship with parent before being removed from ldap.  The parent Role will be removed from ldap last.
     * Role removal will trigger automatic deassignment from all Users or revocation of Permission as well.
     * <p>
     * <img src="./doc-files/HierRoleDescendants.png" alt="">
     */
    public static void testDeleteDescendantRoles()
    {
        String szLocation = ".testDeleteDescendantRoles";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());

            // This parent has many children.  They must be deleted before parent itself can.
            Role parentRole = new Role(TEST_HIER_DESC_ROLE_PREFIX + 1);

            // There are 10 Roles to process:
            for (int i = 2; i < TEST_NUMBER + 1; i++)
            {
                Role childRole = new Role(TEST_HIER_DESC_ROLE_PREFIX + i);
                adminMgr.deleteInheritance(parentRole, childRole);

                // Remove the child Role from directory along with associated assignments:
                adminMgr.deleteRole(childRole);
                LOG.info(szLocation + " role [" + childRole.getName() + "] success");
            }
            // Remove the parent Role from directory along with associated assignments:
            adminMgr.deleteRole(parentRole);
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Demonstrate a parent to child Role structure of one-to-many.  The parent Role must be created before
     * the call to addDescendant which will Add a new Role node and set a Role relationship with parent node.
     * <p>
     * <img src="./doc-files/HierRoleDescendants.png" alt="">
     */
    public static void testCreateDescendantRoles()
    {
        String szLocation = ".testCreateDescendantRoles";
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());

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
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * This example demonstrates tear down of a child to parent represented as one-to-many.  The parents must all
     * be removed from the child before the child can be removed.
     * Role removal will trigger automatic deassignment from all Users or revocation of Permission as well.
     * <p>
     * <img src="./doc-files/HierRoleAscendants.png" alt="">
     */
    public static void testDeleteAscendantRoles()
    {
        String szLocation = ".testDeleteAscendantRoles";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());

            // This child has many parents:
            Role childRole = new Role(TEST_HIER_ASC_ROLE_PREFIX + 1);

            for (int i = 2; i < TEST_NUMBER + 1; i++)
            {
                Role parentRole = new Role(TEST_HIER_ASC_ROLE_PREFIX + i);
                adminMgr.deleteInheritance(parentRole, childRole);

                // Remove the Role from directory along with associated assignments:
                adminMgr.deleteRole(parentRole);
                LOG.info(szLocation + " role [" + childRole.getName() + "] success");
            }
            // Remove the Role from directory along with associated assignments:
            adminMgr.deleteRole(childRole);
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Demonstrate a child to parent Role structure of one-to-many.  To use this API, the child Role must be created before
     * the call to addAscendant which will Add a new Role node and set a Role relationship with child node.
     * <p>
     * <img src="./doc-files/HierRoleAscendants.png" alt="">
     */
    public static void testCreateAscendantRoles()
    {
        String szLocation = ".testCreateAscendantRoles";
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());

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
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
}