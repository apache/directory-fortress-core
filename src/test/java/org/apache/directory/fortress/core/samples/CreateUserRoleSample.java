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
import org.apache.directory.fortress.core.ReviewMgr;
import org.apache.directory.fortress.core.ReviewMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * CreateUserRoleSample JUnit Test. This test suite demonstrates RBAC Role assignment and deassignment to Users.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CreateUserRoleSample extends TestCase
{
    private static final String CLS_NM = CreateUserRoleSample.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    /**
     *
     * @param name
     */
    public CreateUserRoleSample(String name)
    {
        super(name);
    }

    /**
     * Run the RBAC role test cases.
     *
     * @return Test
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        if(!AllSamplesJUnitTest.isFirstRun())
        {
            suite.addTest(new CreateUserRoleSample("testDeassignRoles"));
        }
        suite.addTest(new CreateUserRoleSample("testAssignComplexRole"));
        return suite;
    }

    /**
     *
     */
    public static void testDeassignRoles()
    {
        String szLocation = ".testDeassignRoles";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        // The key for User entity is the userId attribute.
        User inUser = new User(CreateUserSample.TEST_USERID);
        try
        {
            // Instantiate the ReviewMgr implementation which is used to interrogate policy information.
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance(TestUtils.getContext());

            // This should return null because all Roles assigned to User were removed above:
            List<UserRole> assignedRoles = reviewMgr.assignedRoles(inUser);

            if(assignedRoles != null)
            {
                // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
                AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
                for(UserRole uRole : assignedRoles)
                {
                    // Call the API to deassign the Role from the User entity.  This will remove 'oamRA' and 'oamRC' attributes from the 'oamUserAttrs' object class.
                    adminMgr.deassignUser(uRole);
                }
            }

            // This should return null because all Roles assigned to User were removed above:
            assignedRoles = reviewMgr.assignedRoles(inUser);
            assertTrue(szLocation + " failed deassign test", assignedRoles.size() == 0);
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
    public static void testAssignComplexRole()
    {
        String szLocation = ".testAssignComplexRole";

        // The key for User entity is the userId attribute.
        User inUser = new User(CreateUserSample.TEST_USERID);
        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());

            // Create roles, sampleRole1 - sampleRole10
            for(int i = 1; i < 11; i++)
            {
                // OpenAccessManagers UserRole entity may override Role's temporal constraints.
                // The key for User-Role addition is userId and role name.
                UserRole inUserRole = new UserRole(inUser.getUserId(), CreateRoleSample.TEST_ROLE_PREFIX + i);
                // Set some random constraints, whatever doesn't get set here will be provided by Constraints in corresponding Role defined in {@code ou=Roles}.
                // Don't set Role start date (accept default):
                // Override default on Role end date:
                inUserRole.setEndDate("21410101");
                // Override Role beginTime:
                inUserRole.setBeginTime("0000");
                // Don't set the Role endTime.
                // Override Role dayMask to Mon, Tue, Wed, Thur, Fri, Sat & Sun.
                inUserRole.setDayMask("1234567");
                // Override the Role beginLockDate to Jan 15, 2112
                inUserRole.setBeginLockDate("21120115");
                // Override the Role endLockDate to Feb 15, 2112.
                inUserRole.setEndLockDate("21120215");

                // Call the API to assign the Role to the User entity.  This will add 'oamRA' and 'oamRC' attributes to the 'oamUserAttrs' object class.
                adminMgr.assignUser(inUserRole);
            }

            // Instantiate the ReviewMgr implementation which is used to interrogate policy information.
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance(TestUtils.getContext());

            // Return the list of Roles assigned to User.  The User - Role assignments are loaded into the UserRole entity:
            List<UserRole> assignedRoles = reviewMgr.assignedRoles(inUser);

            // Iterate over list of Roles assigned to User.
            for(UserRole userRole : assignedRoles)
            {
                LOG.info(szLocation + " userId [" + userRole.getUserId() + " roleNm [" + userRole.getName() + "]");
            }
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
}
