/*
 * Copyright © McKinney Identity Management Solutions, LLC, 2009-2011 All Rights Reserved. No part of this program may be reproduced without written consent from MIMS.
 */

package com.jts.fortress.samples;

import com.jts.fortress.arbac.DelegatedMgrImplTest;
import com.jts.fortress.pwpolicy.PswdPolicyMgrImplTest;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

/**
 * This Junit test class calls all of the Samples.
 *
 * @author smckinn
 * @created April 3, 2011
 */
public class AllSamplesJUnitTest extends TestCase
{
    public AllSamplesJUnitTest(String name)
    {
        super(name);
    }

    /**
     * @return
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

