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
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.ReviewMgr;
import org.apache.directory.fortress.core.ReviewMgrFactory;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.TestUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CreateRoleSample JUnit Test. This test program will demonstrate creation of Fortress Roles.
 * The Roles may be simple containing a name only.  The Roles may also be created containing Date/Time constraints.
 * Roles constrained by Date and Time can be overridden when the Role is assigned to a User, see {@code CreateUserRoleSample}.
 * Temporal constraints checks will be peformed when the Role is activated, see {@code CreateSessionSample}.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CreateRoleSample extends TestCase
{
    private static final String CLS_NM = CreateRoleSample.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final String TEST_SIMPLE_ROLE = "simpleRole";
    private static final String TEST_SIMPLE_ROLE2[] = {"Customer", "Admin", "Supervisor"};

    // This constant will be added to index for creation of multiple nodes in directory.
    public static final String TEST_ROLE_PREFIX = "sampleRole";

    public CreateRoleSample(String name)
    {
        super(name);
    }

    /**
     * Run the Role test cases.
     * @return Test
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();

        if(!AllSamplesJUnitTest.isFirstRun())
        {
            suite.addTest(new CreateRoleSample("testDeleteRoles"));
            suite.addTest(new CreateRoleSample("testDeleteSimpleRole2"));
        }

        suite.addTest(new CreateRoleSample("testCreateSimpleRole"));
        suite.addTest(new CreateRoleSample("testCreateComplexRole"));
        return suite;
    }

    public static void testDeleteSimpleRole()
    {
        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        String szLocation = ".testDeleteSimpleRole";
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());

            // At its simplest a Role contains only a name.
            Role inRole = new Role(TEST_SIMPLE_ROLE);

            // Call the API to remove the Role from ldap.
            adminMgr.deleteRole(inRole);
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     *
     */
    public static void testDeleteSimpleRole2()
    {
        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        String szLocation = ".testDeleteSimpleRole2";
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());

            for(String roleName : TEST_SIMPLE_ROLE2)
            {
                // At its simplest a Role contains only a name.
                Role inRole = new Role(roleName);

                // Call the API to remove the Role from ldap.
                adminMgr.deleteRole(inRole);
            }
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            //fail(ex.getMessage());
        }
    }


    /**
     * Remove the Role from the directory.  Role removal will trigger automatic deassignment from all Users or revocation of Permission as well.
     *
     */
    public static void testDeleteRoles()
    {
        String szLocation = ".testDeleteRoles";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            for(int i = 1; i < 11; i++)
            {
                // The key that must be set to locate any Role is simply the name.
                Role inRole = new Role(TEST_ROLE_PREFIX + i);

                // Remove the Role from directory along with associated assignments:
                adminMgr.deleteRole(inRole);

                // Instantiate the ReviewMgr implementation which is used to interrogate RBAC policy information.
                ReviewMgr reviewMgr = ReviewMgrFactory.createInstance(TestUtils.getContext());
                try
                {
                    // this should fail because the Role was deleted above:
                    reviewMgr.readRole(inRole);
                    fail(szLocation + " role [" + inRole.getName() + "] delete failed");
                }
                catch (FinderException se)
                {
                    assertTrue(szLocation + " excep id check", se.getErrorId() == GlobalErrIds.ROLE_NOT_FOUND);
                    // pass
                }
                LOG.info(szLocation + " role [" + inRole.getName() + "] success");
            }
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Demonstrate simple Role creation.  Roles may be assigned to Users or may be targets for Permission grants.
     *
     */
    public static void testCreateSimpleRole()
    {
        String szLocation = ".testCreateSimpleRole";
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());

            // At its simplest a Role contains only a name.
            Role inRole = new Role(TEST_SIMPLE_ROLE);

            // Call the API to actually add the Role to ldap.
            adminMgr.addRole(inRole);

            // Instantiate the ReviewMgr implementation which is used to interrogate RBAC policy information.
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance(TestUtils.getContext());

            // now read the newly created Role entity back:
            Role outRole = reviewMgr.readRole(inRole);
            assertTrue(szLocation + " failed read", inRole.equals(outRole));
            LOG.info(szLocation + " [" + outRole.getName() + "] success");
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    public static void testCreateSimpleRole2()
    {
        String szLocation = ".testCreateSimpleRole2";
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());

            for(String roleName : TEST_SIMPLE_ROLE2)
            {
                // At its simplest a Role contains only a name.
                Role inRole = new Role(roleName);

                // Call the API to actually add the Role to ldap.
                adminMgr.addRole(inRole);
                // Instantiate the ReviewMgr implementation which is used to interrogate RBAC policy information.
                ReviewMgr reviewMgr = ReviewMgrFactory.createInstance(TestUtils.getContext());

                // now read the newly created Role entity back:
                Role outRole = reviewMgr.readRole(inRole);
                assertTrue(szLocation + " failed read", inRole.equals(outRole));
                LOG.info(szLocation + " [" + outRole.getName() + "] success");
            }
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }


    /**
     * Demonstrate the creation of Roles that contains temporal constraints.  These constraints are used to control
     * the day, date, and time of Role activation.  They also can enforce mandatory blackout periods for Role activation.
     *
     */
    public static void testCreateComplexRole()
    {
        String szLocation = ".testCreateComplexRole";
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());

            // Create roles, sampleRole2 - sampleRole10
            for(int i = 1; i < 11; i++)
            {
                // Instantiate the Role entity.
                Role inRole = new Role(TEST_ROLE_PREFIX + i);
                // Set the Role start date to Jan 1, 2011:
                inRole.setBeginDate("20110101");
                // Set the Role end date to never:
                inRole.setEndDate("none");
                // Set the role begin time to 1 am:
                inRole.setBeginTime("0100");
                // Set the role end time to midnight.  This role cannot be activated between hours of midnight and 1 am.
                inRole.setEndTime("0000");
                // set the day mask to Mon, Tue, Wed, Thur, Fri, Sat.  Role can't be activated on Sunday.
                inRole.setDayMask("234567");
                // set the begin lock date to Jan 15, 2011
                inRole.setBeginLockDate("20110115");
                // set the end lock date to Feb 15, 2011 - of course this lockout occurred in the past.
                inRole.setEndLockDate("20110215");

                // Add the Role entity to the directory.
                adminMgr.addRole(inRole);

                // Instantiate the ReviewMgr implementation which is used to interrogate policy information.
                ReviewMgr reviewMgr = ReviewMgrFactory.createInstance(TestUtils.getContext());

                // now read the newly created Role entity back:
                Role outRole = reviewMgr.readRole(inRole);
                assertTrue(szLocation + " failed read", inRole.equals(outRole));
                LOG.info(szLocation + " role [" + outRole.getName() + "] success");
            }

        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
}
