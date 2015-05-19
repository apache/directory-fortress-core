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


import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;


/**
 * This Junit test class calls all of the Samples.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
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


    public AllSamplesJUnitTest( String name )
    {
        super( name );
    }


    /**
     * @return Test
     */
    public static Test suite() throws Exception
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new TestSuite( CreateUserOrgSample.class ) );
        suite.addTest( new TestSuite( CreatePermOrgSample.class ) );
        suite.addTest( new TestSuite( CreateRoleSample.class ) );
        suite.addTest( new TestSuite( CreateRoleHierarchySample.class ) );
        suite.addTest( new TestSuite( CreateUserOrgHierarchySample.class ) );
        suite.addTest( new TestSuite( CreatePermOrgHierarchySample.class ) );
        suite.addTest( new TestSuite( CreateUserSample.class ) );
        suite.addTest( new TestSuite( CreateUserRoleSample.class ) );
        suite.addTest( new TestSuite( CreatePermSample.class ) );
        suite.addTest( new TestSuite( CreateSessionSample.class ) );
        suite.addTest( new TestSuite( AccessMgrSample.class ) );
        suite.addTest( new TestSuite( AccessMgrSample.class ) );
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
