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

import org.apache.directory.fortress.core.DelAdminMgr;
import org.apache.directory.fortress.core.DelAdminMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.impl.TestUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CreateUserOrgHierarchySample JUnit Test. This test program will show how to build a simple User OrgUnit hierarchy which are
 * used to enable administrators to group Users by organizational structure.  This system supports multiple
 * inheritance between OrgUnits and there are no limits on how deep a hierarchy can be.  The OrgUnits require name and type.  Optionally can
 * include a description.  The User OrgUnit must be associated with Users and are used to provide Administratrive RBAC control
 * over who may perform User Role assigns and deassigns in directory.
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CreateUserOrgHierarchySample extends TestCase
{
    private static final String CLS_NM = CreateUserOrgHierarchySample.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    // This constant will be added to index for creation of multiple nodes in directory.
    public static final String TEST_HIER_USERORG_PREFIX = "sampleHierUserOrg";
    public static final String TEST_HIER_BASE_USERORG = "sampleHierUserOrg1";
    public static final int TEST_NUMBER = 6;

    public static final String TEST_HIER_DESC_USERORG_PREFIX = "sampleHierUserOrgD";
    public static final String TEST_HIER_ASC_USERORG_PREFIX = "sampleHierUserOrgA";


    /**
     * Simple constructor kicks off JUnit test suite.
     * @param name
     */
    public CreateUserOrgHierarchySample(String name)
    {
        super(name);
    }

    /**
     * Run the User OrgUnit test cases.
     *
     * @return Test
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();

        if(!AllSamplesJUnitTest.isFirstRun())
        {
            suite.addTest(new CreateUserOrgHierarchySample("testDeleteHierUserOrgs"));
            suite.addTest(new CreateUserOrgHierarchySample("testDeleteDescendantUserOrgs"));
            suite.addTest(new CreateUserOrgHierarchySample("testDeleteAscendantUserOrgs"));
        }

        suite.addTest(new CreateUserOrgHierarchySample("testCreateHierUserOrgs"));
        suite.addTest(new CreateUserOrgHierarchySample("testCreateDescendantUserOrgs"));
        suite.addTest(new CreateUserOrgHierarchySample("testCreateAscendantUserOrgs"));

            /*
        suite.addTest(new CreateUserOrgHierarchySample("testDeleteHierUserOrgs"));
        suite.addTest(new CreateUserOrgHierarchySample("testCreateHierUserOrgs"));

        suite.addTest(new CreateUserOrgHierarchySample("testDeleteDescendantUserOrgs"));
        suite.addTest(new CreateUserOrgHierarchySample("testCreateDescendantUserOrgs"));

        suite.addTest(new CreateUserOrgHierarchySample("testDeleteAscendantUserOrgs"));
        suite.addTest(new CreateUserOrgHierarchySample("testCreateAscendantUserOrgs"));
        */

        return suite;
    }

    /**
     * Remove the simple hierarchical OrgUnits from the directory.  Before removal call the API to move the relationship
     * between the parent and child OrgUnits.  Once the relationship is removed the parent OrgUnit can be removed.
     * User OrgUnit removal is not allowed (SecurityException will be thrown) if ou is assigned to Users in ldap.
     * <p>
     * <img src="./doc-files/HierUserOrgSimple.png" alt="">
     */
    public static void testDeleteHierUserOrgs()
    {
        String szLocation = ".testDeleteHierUserOrgs";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the DelAdminMgr implementation which is used to provision ARBAC policies.
            DelAdminMgr delAdminMgr = DelAdminMgrFactory.createInstance(TestUtils.getContext());
            for (int i = 1; i < TEST_NUMBER; i++)
            {
                // The key that must be set to locate any OrgUnit is simply the name and type.
                OrgUnit parentOrgUnit = new OrgUnit(TEST_HIER_USERORG_PREFIX + i, OrgUnit.Type.USER);
                OrgUnit childOrgUnit = new OrgUnit(TEST_HIER_USERORG_PREFIX + (i + 1), OrgUnit.Type.USER);

                // Remove the relationship from the parent and child OrgUnit:
                delAdminMgr.deleteInheritance(parentOrgUnit, childOrgUnit);

                // Remove the parent OrgUnit from directory:
                delAdminMgr.delete(parentOrgUnit);
            }
            // Remove the child OrgUnit from directory:
            delAdminMgr.delete(new OrgUnit(TEST_HIER_USERORG_PREFIX + TEST_NUMBER, OrgUnit.Type.USER));
            LOG.info(szLocation + " success");
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Add a simple OrgUnit hierarchy to ldap.  The OrgUnits will named to include a name,'sampleHierUserOrg', appended with the
     * sequence of 1 - 6.  'sampleHierUserOrg1' is the root or highest level OrgUnit in the structure while sampleHierUserOrg6 is the lowest
     * most child.  Fortress OrgUnits may have multiple parents which is demonstrated in testCreateAscendantUserOrgs sample.
     * <p>
     * <img src="./doc-files/HierUserOrgSimple.png" alt="">
     */
    public static void testCreateHierUserOrgs()
    {
        String szLocation = ".testCreateHierUserOrgs";
        try
        {
            // Instantiate the DelAdminMgr implementation which is used to provision ARBAC policies.
            DelAdminMgr delAdminMgr = DelAdminMgrFactory.createInstance(TestUtils.getContext());

            // Instantiate the root OrgUnit entity.  OrgUnit requires name and type before addition.
            OrgUnit baseOrgUnit = new OrgUnit(TEST_HIER_BASE_USERORG, OrgUnit.Type.USER);

            // Add the root OrgUnit entity to the directory.
            delAdminMgr.add(baseOrgUnit);

            // Create User OrgUnits, 'sampleHierUserOrg2' - 'sampleHierUserOrg6'.
            for (int i = 2; i < TEST_NUMBER + 1; i++)
            {
                // Instantiate the OrgUnit entity.
                OrgUnit childOrgUnit = new OrgUnit(TEST_HIER_USERORG_PREFIX + i, OrgUnit.Type.USER);

                // Add the OrgUnit entity to the directory.
                delAdminMgr.add(childOrgUnit);

                // Instantiate the parent OrgUnit.  The key is the name and type.
                OrgUnit parentOrgUnit = new OrgUnit(TEST_HIER_USERORG_PREFIX + (i - 1), OrgUnit.Type.USER);

                // Add a relationship between the parent and child OrgUnits:
                delAdminMgr.addInheritance(parentOrgUnit, childOrgUnit);
            }
            LOG.info(szLocation + " success");
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     * Demonstrate teardown of a parent to child relationship of one-to-many.  Each child must first remove the inheritance
     * relationship with parent before being removed from ldap.  The parent OrgUnit will be removed from ldap last.
     * User OrgUnit removal is not allowed (SecurityException will be thrown) if ou is assigned to Users in ldap.
     * <p>
     * <img src="./doc-files/HierUserOrgDescendants.png" alt="">
     */
    public static void testDeleteDescendantUserOrgs()
    {
        String szLocation = ".testDeleteDescendantUserOrgs";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the DelAdminMgr implementation which is used to provision ARBAC policies.
            DelAdminMgr delAdminMgr = DelAdminMgrFactory.createInstance(TestUtils.getContext());

            // This parent has many children.  They must be deleted before parent itself can.
            OrgUnit parentOrgUnit = new OrgUnit(TEST_HIER_DESC_USERORG_PREFIX + 1, OrgUnit.Type.USER);

            // There are N User OrgUnits to process:
            for (int i = 2; i < TEST_NUMBER + 1; i++)
            {
                // Instantiate the child OrgUnit entity.  The key is the name and type.
                OrgUnit childOrgUnit = new OrgUnit(TEST_HIER_DESC_USERORG_PREFIX + i, OrgUnit.Type.USER);

                // Remove the relationship from the parent and child OrgUnit:
                delAdminMgr.deleteInheritance(parentOrgUnit, childOrgUnit);

                // Remove the child OrgUnit from directory:
                delAdminMgr.delete(childOrgUnit);
            }
            // Remove the parent OrgUnit from directory:
            delAdminMgr.delete(parentOrgUnit);
            LOG.info(szLocation + " success");
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Demonstrate a parent to child OrgUnit structure of one-to-many.  The parent OrgUnit must be created before
     * the call to addDescendant which will Add a new OrgUnit node and set a OrgUnit relationship with parent node.
     * <p>
     * <img src="./doc-files/HierUserOrgDescendants.png" alt="">
     */
    public static void testCreateDescendantUserOrgs()
    {
        String szLocation = ".testCreateDescendantUserOrgs";
        try
        {
            // Instantiate the DelAdminMgr implementation which is used to provision ARBAC policies.
            DelAdminMgr delAdminMgr = DelAdminMgrFactory.createInstance(TestUtils.getContext());

            // Instantiate the parent User OrgUnit entity.  This needs a name and type before it can be added to ldap.
            OrgUnit parentOrgUnit = new OrgUnit(TEST_HIER_DESC_USERORG_PREFIX + 1, OrgUnit.Type.USER);

            // This parent will have many children:
            delAdminMgr.add(parentOrgUnit);

            // Create User OrgUnits, 'sampleHierUserOrgD2' - 'sampleHierUserOrgD6'.
            for (int i = 1; i < TEST_NUMBER; i++)
            {
                // Now add relationship to the directory between parent and child User OrgUnits.
                OrgUnit childOrgUnit = new OrgUnit(TEST_HIER_DESC_USERORG_PREFIX + (i + 1), OrgUnit.Type.USER);

                // Now add child OrgUnit entity to directory and add relationship with existing parent OrgUnit.
                delAdminMgr.addDescendant(parentOrgUnit, childOrgUnit);
            }
            LOG.info(szLocation + " success");
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
     * User OrgUnit removal is not allowed (SecurityException will be thrown) if ou is assigned to Users in ldap.
     * <p>
     * <img src="./doc-files/HierUserOrgAscendants.png" alt="">
     */
    public static void testDeleteAscendantUserOrgs()
    {
        String szLocation = ".testDeleteAscendantUserOrgs";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the DelAdminMgr implementation which is used to provision ARBAC policies.
            DelAdminMgr delAdminMgr = DelAdminMgrFactory.createInstance(TestUtils.getContext());

            // This child OrgUnit has many parents:
            OrgUnit childOrgUnit = new OrgUnit(TEST_HIER_ASC_USERORG_PREFIX + 1, OrgUnit.Type.USER);

            for (int i = 2; i < TEST_NUMBER + 1; i++)
            {
                // Instantiate the parent.  This needs a name and type before it can be used in operation.
                OrgUnit parentOrgUnit = new OrgUnit(TEST_HIER_ASC_USERORG_PREFIX + i, OrgUnit.Type.USER);

                // Remove the relationship between parent and child OrgUnits:
                delAdminMgr.deleteInheritance(parentOrgUnit, childOrgUnit);

                // Remove the parent OrgUnit from directory:
                delAdminMgr.delete(parentOrgUnit);
            }
            // Remove the child OrgUnit from directory:
            delAdminMgr.delete(childOrgUnit);
            LOG.info(szLocation + " success");
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Demonstrate a child to parent OrgUnit structure of one-to-many.  To use this API, the child OrgUnit must be created before
     * the call to addAscendant which will Add a new OrgUnit node and set a OrgUnit relationship with child node.
     * <p>
     * <img src="./doc-files/HierUserOrgAscendants.png" alt="">
     */
    public static void testCreateAscendantUserOrgs()
    {
        String szLocation = ".testCreateAscendantUserOrgs";
        try
        {
            // Instantiate the DelAdminMgr implementation which is used to provision ARBAC policies.
            DelAdminMgr delAdminMgr = DelAdminMgrFactory.createInstance(TestUtils.getContext());

            // Instantiate the child OrgUnit.  This needs a name and type.
            OrgUnit childOrgUnit = new OrgUnit(TEST_HIER_ASC_USERORG_PREFIX + 1, OrgUnit.Type.USER);

            // This child will have many parents:
            delAdminMgr.add(childOrgUnit);

            // Create OrgUnits, 'sampleHierUserOrgA2' - 'sampleHierUserOrgA6'.
            for (int i = 1; i < TEST_NUMBER; i++)
            {
                // Instantiate the parent OrgUnit.  This needs a name and type before it can be added to ldap.
                OrgUnit parentOrgUnit = new OrgUnit(TEST_HIER_ASC_USERORG_PREFIX + (i + 1), OrgUnit.Type.USER);

                // Now add parent OrgUnit entity to directory and add relationship with existing child OrgUnit.
                delAdminMgr.addAscendant(childOrgUnit, parentOrgUnit);
            }
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
}
