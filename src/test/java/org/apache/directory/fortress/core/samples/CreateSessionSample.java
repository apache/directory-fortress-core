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

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.AccessMgrFactory;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.PasswordException;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CreateSessionSample JUnit Test. Fortress Sessions are modeled on the RBAC specification.  Each Session
 * may contain User attributes plus all of their activated Roles.  The Session will also contain pw policy
 * and error status flags that were set during User's previous CreateSession invocation.
 *
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CreateSessionSample extends TestCase
{
    private static final String CLS_NM = CreateSessionSample.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    public CreateSessionSample(String name)
    {
        super(name);
    }

    /**
     * Run several Use cases that demonstrate RBAC Session creation attempts using common scenarios.
     * @return Test
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest(new CreateSessionSample("testCreateSession"));
        suite.addTest(new CreateSessionSample("testCreateSessionWithRole"));
        suite.addTest(new CreateSessionSample("testCreateSessionTrusted"));
        suite.addTest(new CreateSessionSample("testCreateSessionWithRolesTrusted"));
        return suite;
    }

    /**
     * This method is simple wrapper.
     *
     */
    public void testCreateSession()
    {
        //createSession("oamuser1", "passw0rd1", 10);
        createSession(CreateUserSample.TEST_USERID, CreateUserSample.TEST_PASSWORD, 10);
    }

    /**
     * Another wrapper for create session with roles.
     *
     */
    public void testCreateSessionWithRole()
    {
        //createSessionsWithRole(CreateUserSample.TEST_USERID, CreateUserSample.TEST_PASSWORD, CreateRoleSample.TEST_SIMPLE_ROLE);
        createSessionsWithRole(CreateUserSample.TEST_USERID, CreateUserSample.TEST_PASSWORD, CreateRoleSample.TEST_ROLE_PREFIX + "1");
    }

    /**
     * This wrapper will create a session passing roles without a password.
     *
     */
    public void testCreateSessionWithRolesTrusted()
    {
        createSessionsWithRolesTrusted(CreateUserSample.TEST_USERID, new String[]{CreateRoleSample.TEST_ROLE_PREFIX + "1", CreateRoleSample.TEST_ROLE_PREFIX + "3", CreateRoleSample.TEST_ROLE_PREFIX + "4"}, 3);
    }

    /**
     * This wrapper will create a session without a password.
     *
     */
    public void testCreateSessionTrusted()
    {
        createSessionTrusted(CreateUserSample.TEST_USERID);
    }

    /**
     * Calls AccessMgr createSession API.  Will check to ensure the RBAC Session contains the expected number of Roles
     * activated.
     *
     * @param userId  Case insensitive userId.
     * @param password Password is case sensitive, clear text but is stored in directory as hashed value.
     * @param expectedRoles integer contains the expected number of Roles in the Session.
     */
    public static void createSession(String userId, String password, int expectedRoles)
    {
        String szLocation = ".createSession";
        try
        {
            // Instantiate the AccessMgr implementation which perform runtime RBAC operations.
            AccessMgr accessMgr = AccessMgrFactory.createInstance(TestUtils.getContext());

            // The User entity is used to pass data into the createSession API.
            User user = new User(userId, password);

            // This API will return a Session object that contains the User's activated Roles and other info.
            Session session = accessMgr.createSession(user, false);

            // createSession will throw SecurityException if fails thus the Session should never be null.
            assertNotNull(session);

            // Pull the userId from the Session.
            String sessUserId = accessMgr.getUserId(session);
            assertTrue(szLocation + " failed compare found userId in session [" + sessUserId + "] valid userId [" + userId + "]", userId.equalsIgnoreCase(sessUserId));

            // Get the User's activated Roles.
            List<UserRole> uRoles = session.getRoles();

            // do some validations
            assertNotNull(uRoles);
            assertEquals(szLocation + " user role check failed list size user [" + user.getUserId() + "]", expectedRoles, uRoles.size());
            // now try negative test case:
            try
            {
                // this better fail
                User userBad = new User(user.getUserId(), "badpw");

                // The API will authenticate the User password, evaluate password policies and perform Role activations.
                accessMgr.createSession(userBad, false);
                fail(szLocation + " userId [" + userId + "]  failed negative test");
            }
            catch (PasswordException pe)
            {
                assertTrue(szLocation + " userId [" + userId + "]  excep id check", pe.getErrorId() == GlobalErrIds.USER_PW_INVLD);
                // pass
            }
            catch (SecurityException se)
            {
                fail(szLocation + " userId [" + userId + "]  failed with unexpected errorId" + se.getErrorId() + " msg=" + se.getMessage());
                // pass
            }
            LOG.info(szLocation + " userId [" + userId + "] successful");
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " userId [" + userId + "]  caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Call the AccessMgr createSession API passing a single Role for activation.  Successful RBAC Session should
     * contains same Role activated.
     *
     * @param userId  Case insensitive userId.
     * @param password Password is case sensitive, clear text but is stored in directory as hashed value.
     * @param role contains role name of Role targeted for Activation.
     */
    public static void createSessionsWithRole(String userId, String password, String role)
    {
        String szLocation = ".createSessionsWithRole";
        try
        {
            // Instantiate the AccessMgr implementation which perform runtime RBAC operations.
            AccessMgr accessMgr = AccessMgrFactory.createInstance(TestUtils.getContext());

            // The User entity is used to pass data into the createSession API.
            User user = new User(userId, password, role);

            // The API will authenticate the User password, evaluate password policies and perform Role activations.
            Session session = accessMgr.createSession(user, false);

            // createSession will throw SecurityException if fails thus the Session should never be null.
            assertNotNull(session);

            // do some validations
            // Get the User's activated Roles.
            List<UserRole> sessRoles = session.getRoles();
            assertTrue(szLocation + " userId [" + userId + "]  with roles failed role check", sessRoles.contains(new UserRole(role)));
            LOG.info(szLocation + "  userId [" + userId + "] successful");
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " userId [" + userId + "]  caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Create RBAC Session and activated supplied Roles.  This scenario perform authentication in trusted manner
     * which does not require User password.
     *
     * @param userId  Case insensitive userId.
     * @param roles array of Role names to activate into RBAC Session.
     * @param expectedRoles integer contains the expected number of Roles in the Session.
     */
    public static void createSessionsWithRolesTrusted(String userId, String[] roles, int expectedRoles)
    {
        String szLocation = ".createSessionsWithRolesTrusted";
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance(TestUtils.getContext());

            // The User entity is used to pass data into the createSession API.
            User user = new User(userId);

            // iterate over array of input Role names.
            for (String roleName : roles)
            {
                // Add the Role name to list of Roles to be activated on Session.
                user.setRoleName(roleName);
            }

            // The API will verify User is good and perform Role activations.  Request will fail if User is locked out of ldap for any reason.
            Session session = accessMgr.createSession(user, true);

            // createSession will throw SecurityException if fails thus the Session should never be null.
            assertNotNull(session);

            // Get the User's activated Roles.
            List<UserRole> sessRoles = session.getRoles();

            // do some validations
            assertEquals(szLocation + " user role check failed list size user [" + user.getUserId() + "]", expectedRoles, sessRoles.size());
            for (String roleName : roles)
            {
                assertTrue(szLocation + " userId [" + userId + "]  with roles trusted failed role check", sessRoles.contains(new UserRole(roleName)));
            }

            LOG.info(szLocation + "  userId [" + userId + "] successful");
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught userId [" + userId + "]  SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Create trusted RBAC Session.  This API will attempt to activate all of the User's assigned Roles.
     *
     * @param userId  Case insensitive userId.
     */
    public static void createSessionTrusted(String userId)
    {
        String szLocation = ".createSessionTrusted";
        try
        {
            // Instantiate the AccessMgr implementation which perform runtime RBAC operations.
            AccessMgr accessMgr = AccessMgrFactory.createInstance(TestUtils.getContext());

            // The User entity is used to pass data into the createSession API.
            User user = new User(userId);

            // The API will verify User is good and perform Role activations.  Request will fail if User is locked out of ldap for any reason.
            Session session = accessMgr.createSession(user, true);

            // createSession will throw SecurityException if fails thus the Session should never be null.
            assertNotNull(session);
            LOG.info(szLocation + "  userId [" + userId + "] successful");
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " userId [" + userId + "] caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
}
