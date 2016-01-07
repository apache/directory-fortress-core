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
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.TestUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CreatePermOrgHierarchySample JUnit Test. This test program will show how to build a simple Permission OrgUnit hierarchy which are
 * used to enable administrators to group Permissions by organizational structure i.e. by application groups.  This system supports multiple
 * inheritance between OrgUnits and there are no limits on how deep a hierarchy can be.  The OrgUnits require name and type.  Optionally can
 * include a description.  The Permission OrgUnit must be associated with PermObj and are used to provide Administratrive RBAC control
 * over who may perform Permission grants and revocations in directory.
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CreatePermOrgHierarchySample extends TestCase
{
    private static final String CLS_NM = CreatePermOrgHierarchySample.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    // This constant will be added to index for creation of multiple nodes in directory.
    private static final String TEST_HIER_PERMORG_PREFIX = "sampleHierPermOrg";
    private static final String TEST_HIER_BASE_PERMORG = "sampleHierPermOrg1";
    private static int TEST_NUMBER = 6;

    private static final String TEST_HIER_DESC_PERMORG_PREFIX = "sampleHierPermOrgD";
    private static final String TEST_HIER_ASC_PERMORG_PREFIX = "sampleHierPermOrgA";


    /**
     * Simple constructor kicks off JUnit test suite.
     * @param name
     */
    public CreatePermOrgHierarchySample(String name)
    {
        super(name);
    }

    /**
     * Run the Perm OrgUnit test cases.
     *
     * @return Test
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();

        if(!AllSamplesJUnitTest.isFirstRun())
        {
            suite.addTest(new CreatePermOrgHierarchySample("testDeleteHierPermOrgs"));
            suite.addTest(new CreatePermOrgHierarchySample("testDeleteDescendantPermOrgs"));
            suite.addTest(new CreatePermOrgHierarchySample("testDeleteAscendantPermOrgs"));
        }

        suite.addTest(new CreatePermOrgHierarchySample("testCreateHierPermOrgs"));
        suite.addTest(new CreatePermOrgHierarchySample("testCreateDescendantPermOrgs"));
        suite.addTest(new CreatePermOrgHierarchySample("testCreateAscendantPermOrgs"));

            /*
        suite.addTest(new CreatePermOrgHierarchySample("testDeleteHierPermOrgs"));
        suite.addTest(new CreatePermOrgHierarchySample("testCreateHierPermOrgs"));

        suite.addTest(new CreatePermOrgHierarchySample("testDeleteDescendantPermOrgs"));
        suite.addTest(new CreatePermOrgHierarchySample("testCreateDescendantPermOrgs"));

        suite.addTest(new CreatePermOrgHierarchySample("testDeleteAscendantPermOrgs"));
        suite.addTest(new CreatePermOrgHierarchySample("testCreateAscendantPermOrgs"));
              */
        return suite;
    }

    /**
     * Remove the simple hierarchical OrgUnits from the directory.  Before removal call the API to move the relationship
     * between the parent and child OrgUnits.  Once the relationship is removed the parent OrgUnit can be removed.
     * Perm OrgUnit removal is not allowed (SecurityException will be thrown) if ou is assigned to PermObjs in ldap.
     * <p>
     * <img src="./doc-files/HierPermOrgSimple.png" alt="">
     */
    public static void testDeleteHierPermOrgs()
    {
        String szLocation = ".testDeleteHierPermOrgs";

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
                OrgUnit parentOrgUnit = new OrgUnit(TEST_HIER_PERMORG_PREFIX + i, OrgUnit.Type.PERM);
                OrgUnit childOrgUnit = new OrgUnit(TEST_HIER_PERMORG_PREFIX + (i + 1), OrgUnit.Type.PERM);

                // Remove the relationship from the parent and child OrgUnit:
                delAdminMgr.deleteInheritance(parentOrgUnit, childOrgUnit);

                // Remove the parent OrgUnit from directory:
                delAdminMgr.delete(parentOrgUnit);
            }
            // Remove the child OrgUnit from directory:
            delAdminMgr.delete(new OrgUnit(TEST_HIER_PERMORG_PREFIX + TEST_NUMBER, OrgUnit.Type.PERM));
            LOG.info(szLocation + " success");
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Add a simple OrgUnit hierarchy to ldap.  The OrgUnits will named to include a name,'sampleHierPermOrg', appended with the
     * sequence of 1 - 6.  'sampleHierPermOrg1' is the root or highest level OrgUnit in the structure while sampleHierPermOrg6 is the lowest
     * most child.  Fortress OrgUnits may have multiple parents which is demonstrated in testCreateAscendantPermOrgs sample.
     * <p>
     * <img src="./doc-files/HierPermOrgSimple.png" alt="">
     */
    public static void testCreateHierPermOrgs()
    {
        String szLocation = ".testCreateHierPermOrgs";
        try
        {
            // Instantiate the DelAdminMgr implementation which is used to provision ARBAC policies.
            DelAdminMgr delAdminMgr = DelAdminMgrFactory.createInstance(TestUtils.getContext());

            // Instantiate the root OrgUnit entity.  OrgUnit requires name and type before addition.
            OrgUnit baseOrgUnit = new OrgUnit(TEST_HIER_BASE_PERMORG, OrgUnit.Type.PERM);

            // Add the root OrgUnit entity to the directory.
            delAdminMgr.add(baseOrgUnit);

            // Create Perm OrgUnits, 'sampleHierPermOrg2' - 'sampleHierPermOrg6'.
            for (int i = 2; i < TEST_NUMBER + 1; i++)
            {
                // Instantiate the OrgUnit entity.
                OrgUnit childOrgUnit = new OrgUnit(TEST_HIER_PERMORG_PREFIX + i, OrgUnit.Type.PERM);

                // Add the OrgUnit entity to the directory.
                delAdminMgr.add(childOrgUnit);

                // Instantiate the parent OrgUnit.  The key is the name and type.
                OrgUnit parentOrgUnit = new OrgUnit(TEST_HIER_PERMORG_PREFIX + (i - 1), OrgUnit.Type.PERM);

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
     * Perm OrgUnit removal is not allowed (SecurityException will be thrown) if ou is assigned to PermObjs in ldap.
     * <p>
     * <img src="./doc-files/HierPermOrgDescendants.png" alt="">
     */
    public static void testDeleteDescendantPermOrgs()
    {
        String szLocation = ".testDeleteDescendantPermOrgs";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the DelAdminMgr implementation which is used to provision ARBAC policies.
            DelAdminMgr delAdminMgr = DelAdminMgrFactory.createInstance(TestUtils.getContext());

            // This parent has many children.  They must be deleted before parent itself can.
            OrgUnit parentOrgUnit = new OrgUnit(TEST_HIER_DESC_PERMORG_PREFIX + 1, OrgUnit.Type.PERM);

            // There are N Perm OrgUnits to process:
            for (int i = 2; i < TEST_NUMBER + 1; i++)
            {
                // Instantiate the child OrgUnit entity.  The key is the name and type.
                OrgUnit childOrgUnit = new OrgUnit(TEST_HIER_DESC_PERMORG_PREFIX + i, OrgUnit.Type.PERM);

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
     * <img src="./doc-files/HierPermOrgDescendants.png" alt="">
     */
    public static void testCreateDescendantPermOrgs()
    {
        String szLocation = ".testCreateDescendantPermOrgs";
        try
        {
            // Instantiate the DelAdminMgr implementation which is used to provision ARBAC policies.
            DelAdminMgr delAdminMgr = DelAdminMgrFactory.createInstance(TestUtils.getContext());

            // Instantiate the parent Perm OrgUnit entity.  This needs a name and type before it can be added to ldap.
            OrgUnit parentOrgUnit = new OrgUnit(TEST_HIER_DESC_PERMORG_PREFIX + 1, OrgUnit.Type.PERM);

            // This parent will have many children:
            delAdminMgr.add(parentOrgUnit);

            // Create Perm OrgUnits, 'sampleHierPermD2' - 'sampleHierPermOrgD6'.
            for (int i = 1; i < TEST_NUMBER; i++)
            {
                // Now add relationship to the directory between parent and child Perm OrgUnits.
                OrgUnit childOrgUnit = new OrgUnit(TEST_HIER_DESC_PERMORG_PREFIX + (i + 1), OrgUnit.Type.PERM);

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
     * Perm OrgUnit removal is not allowed (SecurityException will be thrown) if ou is assigned to PermObjs in ldap.
     * <p>
     * <img src="./doc-files/HierPermOrgAscendants.png" alt="">
     */
    public static void testDeleteAscendantPermOrgs()
    {
        String szLocation = ".testDeleteAscendantPermOrgs";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the DelAdminMgr implementation which is used to provision ARBAC policies.
            DelAdminMgr delAdminMgr = DelAdminMgrFactory.createInstance(TestUtils.getContext());

            // This child OrgUnit has many parents:
            OrgUnit childOrgUnit = new OrgUnit(TEST_HIER_ASC_PERMORG_PREFIX + 1, OrgUnit.Type.PERM);

            for (int i = 2; i < TEST_NUMBER + 1; i++)
            {
                // Instantiate the parent.  This needs a name and type before it can be used in operation.
                OrgUnit parentOrgUnit = new OrgUnit(TEST_HIER_ASC_PERMORG_PREFIX + i, OrgUnit.Type.PERM);

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
     * <img src="./doc-files/HierPermOrgAscendants.png" alt="">
     */
    public static void testCreateAscendantPermOrgs()
    {
        String szLocation = ".testCreateAscendantPermOrgs";
        try
        {
            // Instantiate the DelAdminMgr implementation which is used to provision ARBAC policies.
            DelAdminMgr delAdminMgr = DelAdminMgrFactory.createInstance(TestUtils.getContext());

            // Instantiate the child OrgUnit.  This needs a name and type.
            OrgUnit childOrgUnit = new OrgUnit(TEST_HIER_ASC_PERMORG_PREFIX + 1, OrgUnit.Type.PERM);

            // This child will have many parents:
            delAdminMgr.add(childOrgUnit);

            // Create OrgUnits, 'sampleHierPermOrgA2' - 'sampleHierPermOrgA6'.
            for (int i = 1; i < TEST_NUMBER; i++)
            {
                // Instantiate the parent OrgUnit.  This needs a name and type before it can be added to ldap.
                OrgUnit parentOrgUnit = new OrgUnit(TEST_HIER_ASC_PERMORG_PREFIX + (i + 1), OrgUnit.Type.PERM);

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
