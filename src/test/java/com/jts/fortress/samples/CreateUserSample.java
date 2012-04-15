/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.samples;

import com.jts.fortress.SecurityException;
import com.jts.fortress.AdminMgr;
import com.jts.fortress.AdminMgrFactory;
import com.jts.fortress.ReviewMgr;
import com.jts.fortress.ReviewMgrFactory;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.rbac.User;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

/**
 * CreateUserSample JUnit Test.  These samples demonstrate how to create and delete User entries in Fortress.
 * The examples were kept simple to provide an gentle introduction into Fortress administrative functions.  There are also
 * extra steps demonstrated in this test class, i.e. reading User after adding, that are for instructional purposes and not required
 * for real world applications.
 *
 * @author Shawn McKinney
 * @created March 1, 2011
 */
public class CreateUserSample extends TestCase
{
    private static final String CLS_NM = CreateUserSample.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);
    public static final String TEST_USERID = "sampleUser1";
    public static final String TEST_PASSWORD = "password1";

    /**
     * @param name
     */
    public CreateUserSample(String name)
    {
        super(name);
    }

    /**
     * Run the Create User samples.
     *
     * @return
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();

        if(!AllSamplesJUnitTest.isFirstRun())
        {
            suite.addTest(new CreateUserSample("testDeleteUser"));
        }
        suite.addTest(new CreateUserSample("testCreateUser"));
        return suite;
    }

   /**
     * The deleteUser will completely remove the User data from the LDAP directory.  There is also a 'softDelete' that
     * can be used to disable the User if hard delete is not the aim.
     *
     */
    public static void testDeleteUser()
    {
        String szLocation = CLS_NM + ".testDeleteUser";

        if(AllSamplesJUnitTest.isFirstRun())
        {
            return;
        }

        try
        {
            // Instantiate the AdminMgr implementation which is used to provision RBAC policies.
            AdminMgr adminMgr = AdminMgrFactory.createInstance();
            User inUser = new User(TEST_USERID);
            adminMgr.deleteUser(inUser);

            // now read it back:
            // Instantiate the ReviewMgr implementation which is used to interrogate policy information.
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();
            try
            {
                // this should fail because User was deleted above:
                User outUser = reviewMgr.readUser(inUser);
                fail(szLocation + " user [" + inUser.getUserId() + "] delete failed");
            }
            catch (SecurityException se)
            {
                assertTrue(szLocation + " excep id check", se.getErrorId() == GlobalErrIds.USER_NOT_FOUND);
                // pass
            }
            log.info(szLocation + " user [" + inUser.getUserId() + "] success");
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Demonstrate how to create a simple user and assign to a single RBAC Role in one API call.  The example will
     * also read the User back from LDAP after creation but this is not required for real world examples.
     */
    public static void testCreateUser()
    {
        String szLocation = CLS_NM + ".testCreateUser";
        try
        {
            // Instantiate the AdminMgr implementation.  All AdminMgr APIs can throw a SecurityException in the event
            // of rule violation or system error.
            AdminMgr adminMgr = AdminMgrFactory.createInstance();
            // You do not have to assign a Role to User when calling 'addUser'.  Role assignment may be done using the 'assignUser' API.
            /**
             * Create new User entity:
             *   {@link com.jts.fortress.rbac.User#userId}="sampleUser1"
             *   {@link User#password}="password1"
             *   {@link User#setRole(String)}="sampleRole1"
             *   {@link User#ou}="sampleUserOU1"
             */
            //User inUser = new User(TEST_USERID, TEST_PASSWORD, CreateRoleSample.TEST_SIMPLE_ROLE, CreateUserOrgSample.TEST_USER_OU_NM);
            //User inUser = new User(TEST_USERID, TEST_PASSWORD, CreateRoleSample.TEST_SIMPLE_ROLE, CreateUserOrgSample.TEST_USER_OU_NM);
            User inUser = new User(TEST_USERID, TEST_PASSWORD.toCharArray());
            inUser.setOu(CreateUserOrgSample.TEST_USER_OU_NM);
            // Now call the add API.  The API will return User entity with associated LDAP dn if creation was successful.
            User outUser = adminMgr.addUser(inUser);
            assertNotNull(outUser);

            // Instantiate the ReviewMgr implementation which is used to interrogate policy information.
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();

            // now read the newly created User entity back:
            User outUser2 = reviewMgr.readUser(inUser);
            assertTrue(szLocation + " failed read", inUser.equals(outUser2));
            log.info(szLocation + " user [" + outUser2.getUserId() + "] success");
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
}