/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */

package org.openldap.fortress.samples;

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

