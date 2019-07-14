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

import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.AdminMgr;
import org.apache.directory.fortress.core.AdminMgrFactory;
import org.apache.directory.fortress.core.ReviewMgr;
import org.apache.directory.fortress.core.ReviewMgrFactory;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.model.User;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.directory.fortress.core.model.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LoadTestUserSample JUnit Test.  These samples create many users and assign them to roles.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class LoadTestUserSample extends TestCase
{
    private static final String CLS_NM = CreateUserSample.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    public static int NUMBER_TEST_USERS = 1000000;
    public static int NUMBER_TEST_ROLES = 10;
    public static final String TEST_ROLE = "loadtestrole";
    public static final String TEST_USERID = "loadtestuser";
    public static final String TEST_PASSWORD = "secret";


    /**
     * @param name
     */
    public LoadTestUserSample(String name)
    {
        super(name);
    }

    /**
     * Run the Create User samples.
     *
     * @return Test
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();

/*
        if(teardownRequired())
        {
            suite.addTest(new LoadTestUserSample("testDeleteUser"));
        }
*/
        suite.addTest(new LoadTestUserSample("testCreateUser"));
        suite.addTest(new LoadTestUserSample("testAssignUser"));
        return suite;
    }

    /**
     * Determines if teardown needs to occur on sample data.
     *
     * @return true if teardown is required
     */
    static boolean teardownRequired()
    {
        // The default for this check is 'true'
        boolean tearDown = true;
        String methodName = ".teardownRequired";
        try
        {
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance(TestUtils.getContext());
            User inUser = new User(TEST_USERID + 1, TEST_PASSWORD);
            reviewMgr.readUser(inUser);
            // If we get here, the sample data needs to be removed:
        }
        catch ( SecurityException ex)
        {
            if(ex.getErrorId() == GlobalErrIds.USER_NOT_FOUND)
            {
                // If we get here the sample data does not need to be removed:
                tearDown = false;
            }
            else
            {
                String warning = methodName + " caught SecurityException=" + ex.getMessage();
                LOG.warn(warning);
            }
        }
        LOG.info(methodName + ":" + tearDown);
        return tearDown;
    }

    /**
     * The deleteUser will completely remove the User data from the LDAP directory.  There is also a 'softDelete' that
     * can be used to disable the User if hard delete is not the aim.
     *
     */
    public static void testDeleteUser()
    {
        String szLocation = ".testDeleteUser";

        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            LOG.info(szLocation + "deleting [" + NUMBER_TEST_USERS + "] users... (every '-' is 1000 users)");
            for( int i = 1; i <= NUMBER_TEST_USERS; i++)
            {
                User inUser = new User( TEST_USERID + i );
                adminMgr.deleteUser( inUser );
                if( i % 1000 == 0)
                {
                    System.out.print( "-" );
                }
            }
            System.out.println("");
            LOG.info(szLocation + " users delete success");
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Demonstrate how to create a simple user and assign to a single RBAC Role in one API call.  The example will
     * also read the User back from LDAP after creation but this is not required for real world examples.
     */
    public static void testCreateUser()
    {
        String szLocation = ".testCreateUser";
        try
        {
            // Instantiate the AdminMgr implementation.  All AdminMgr APIs can throw a SecurityException in the event
            // of rule violation or system error.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            // You do not have to assign a Role to User when calling 'addUser'.  Role assignment may be done using the 'assignUser' API.
            /**
             * Create new User entity:
             *   {@link org.apache.directory.fortress.core.model.User#userId}="sampleUser1"
             *   {@link User#password}="password1"
             *   {@link User#setRole(String)}="sampleRole1"
             *   {@link User#ou}="sampleUserOU1"
             */
            LOG.info(szLocation + "CREATING [" + NUMBER_TEST_USERS + "] users... (every '+' is 1000 users)");
            for( int i = 1; i <= NUMBER_TEST_USERS; i++)
            {
                User inUser = new User(TEST_USERID + i, TEST_PASSWORD);
                inUser.setOu("DEV0");

                try
                {
                    // Now call the add API.  The API will return User entity with associated LDAP dn if creation was successful.
                    User outUser = adminMgr.addUser(inUser);
                    assertNotNull(outUser);
                    if( i % 1000 == 0)
                    {
                        System.out.print( "+" );
                    }
                }
                catch (SecurityException ex)
                {
                    if(ex.getErrorId() == GlobalErrIds.USER_ADD_FAILED_ALREADY_EXISTS)
                    {
                        // ignore
                    }
                    else
                    {
                        LOG.error(szLocation + "testAddUsers caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
                        fail(ex.getMessage());
                    }
                    if( i % 1000 == 0)
                    {
                        System.out.print( "=" );
                    }
                }
            }
            System.out.println("");
            LOG.info( szLocation + " users create success" );
        }
        catch (SecurityException ex)
        {
                LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
                fail(ex.getMessage());
        }
    }

    /**
     * Demonstrate how to create a simple user and assign to a single RBAC Role in one API call.  The example will
     * also read the User back from LDAP after creation but this is not required for real world examples.
     */
    public static void testAssignUser()
    {
        String szLocation = ".testCreateUser";
        try
        {
            // Instantiate the AdminMgr implementation.  All AdminMgr APIs can throw a SecurityException in the event
            // of rule violation or system error.
            AdminMgr adminMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
            // You do not have to assign a Role to User when calling 'addUser'.  Role assignment may be done using the 'assignUser' API.
            /**
             * Create new User entity:
             *   {@link org.apache.directory.fortress.core.model.User#userId}="sampleUser1"
             *   {@link User#password}="password1"
             *   {@link User#setRole(String)}="sampleRole1"
             *   {@link User#ou}="sampleUserOU1"
             */
            //User inUser = new User(TEST_USERID, TEST_PASSWORD, CreateRoleSample.TEST_SIMPLE_ROLE, CreateUserOrgSample.TEST_USER_OU_NM);
            //User inUser = new User(TEST_USERID, TEST_PASSWORD, CreateRoleSample.TEST_SIMPLE_ROLE, CreateUserOrgSample.TEST_USER_OU_NM);
            LOG.info(szLocation + "ASSIGNING [" + NUMBER_TEST_USERS + "] users to [" + NUMBER_TEST_ROLES + "] roles (every '@' is 1000 users)");
            for( int i = 1; i <= NUMBER_TEST_USERS; i++)
            {
                for( int j = 1; j <= NUMBER_TEST_ROLES; j++ )
                {
                    try
                    {
                        UserRole inUserRole = new UserRole(TEST_USERID + i, TEST_ROLE + j);
                        // Now call the assignUser API.  The API will assign user to specified role.
                        adminMgr.assignUser( inUserRole );
                    }
                    catch (SecurityException ex)
                    {
                        LOG.error(szLocation + "testAssignUsers caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
                        //fail(ex.getMessage());
                    }
                    if( i % 1000 == 0)
                    {
                        System.out.print( "@" );
                    }
                }
            }
            System.out.println("");
            LOG.info(szLocation + " users assignment success");
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
}