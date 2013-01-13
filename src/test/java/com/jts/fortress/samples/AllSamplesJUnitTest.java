/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package com.jts.fortress.samples;

import com.jts.fortress.*;
import com.jts.fortress.util.LogUtil;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

/**
 * This Junit test class calls all of the Samples.
 *
 * @author Shawn McKinney
 */
public class AllSamplesJUnitTest extends TestCase
{

/*
    private static boolean isFirstRun = false;

    public static boolean isFirstRun() {
        return isFirstRun;
    }
*/

    private static boolean isFirstRun = getFirstRun();
    private static boolean getFirstRun()
    {
        isFirstRun = !CreateUserSample.teardownRequired();
        return isFirstRun;
    }

    public static boolean isFirstRun()
    {
        return isFirstRun;
    }

    @Deprecated
    public static void setFirstRun(boolean firstRun) {
        isFirstRun = firstRun;
    }

    public AllSamplesJUnitTest(String name)
    {
        super(name);
    }

    /**
     * @return Test
     */
    public static Test suite() throws Exception
    {
        TestSuite suite = new TestSuite();

        //String szRunProp = "isFirstJUnitRun";
        //String szFirstRun = System.getProperty(szRunProp);
        //setFirstRun(szFirstRun != null && szFirstRun.equalsIgnoreCase("true"));

        suite.addTest(new TestSuite(CreateUserOrgSample.class));
        suite.addTest(new TestSuite(CreatePermOrgSample.class));
        suite.addTest(new TestSuite(CreateRoleSample.class));
        suite.addTest(new TestSuite(CreateRoleHierarchySample.class));
        suite.addTest(new TestSuite(CreateUserOrgHierarchySample.class));
        suite.addTest(new TestSuite(CreatePermOrgHierarchySample.class));
        suite.addTest(new TestSuite(CreateUserSample.class));
        suite.addTest(new TestSuite(CreateUserRoleSample.class));
        suite.addTest(new TestSuite(CreatePermSample.class));
        suite.addTest(new TestSuite(CreateSessionSample.class));
        suite.addTest(new TestSuite(AccessMgrSample.class));
        suite.addTest(new TestSuite(AccessMgrSample.class));
		return suite;
	}

    /**
     * The JUnit setup method
     *
     * @throws Exception Description of the Exception
     */
    public void setUp() throws Exception
    {
        super.setUp();
    }


    /**
     * The teardown method for JUnit
     *
     * @throws Exception Description of the Exception
     */
    public void tearDown() throws Exception
    {
        super.tearDown();
    }
}

