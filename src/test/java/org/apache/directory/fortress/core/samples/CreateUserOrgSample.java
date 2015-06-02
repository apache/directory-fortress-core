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
import org.apache.directory.fortress.core.DelReviewMgr;
import org.apache.directory.fortress.core.DelReviewMgrFactory;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.impl.TestUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * CreateUserOrgSample JUnit Test.  Fortress User entities must be associated with an OrgUnit.
 * The User OrgUnit is associated with corresponding organizational groups that the User has been assigned by their
 * enterprise resource department.  There is no limit to the number of User Organizations that are created but at least
 * one must be created before any User can be added to the directory.  Each OrgUnit may be related to one or more parent
 * OrgUnits although that functionality is not demonstrated here.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CreateUserOrgSample extends TestCase
{
    private static final String CLS_NM = CreateUserOrgSample.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    public static final String TEST_USER_OU_NM = "sampleUsers-OU.1";

    public CreateUserOrgSample(String name)
    {
        super(name);
    }

    /**
     * Run the User OrgUnit test suite.
     * @return Test
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest(new CreateUserOrgSample("testCreateUserOrg"));
        return suite;
    }

    /**
     * Before a User can be added to ldap directory an OrgUnit must be created.  The User OrgUnit entity
     * supports general hierarchies meaning an OrgUnit can have zero or more parents.  The User OrgUnit
     * organizational structure is represented logically as a simple directional graph though that
     * functionality is not demonstrated here.
     *
     */
    public static void testCreateUserOrg()
    {
        String szLocation = ".testCreateUserOrg";
        try
        {
            DelReviewMgr dRevAdminMgr = DelReviewMgrFactory.createInstance(TestUtils.getContext());

            // The OrgUnit requires name and type to be set before use.
            OrgUnit inOU = new OrgUnit(TEST_USER_OU_NM, OrgUnit.Type.USER);

            try
            {
                dRevAdminMgr.read(inOU);
                // if org is found, return.
                return;
            }
            catch(FinderException fe)
            {
                assertTrue(szLocation + " excep id check", fe.getErrorId() == GlobalErrIds.ORG_NOT_FOUND_USER);
                // pass
            }

            // Instantiate the Delegated AdminMgr implementation object which provisions OrgUnits and AdminRoles to the system.
            DelAdminMgr dAdminMgr = DelAdminMgrFactory.createInstance(TestUtils.getContext());

            // Add the OrgUnit to the directory.
            dAdminMgr.add(inOU);

            // Instantiate the Delegated RevewMgr implementation which interrogates the OrgUnit and AdminRole data.
            DelReviewMgr dReviewMgr = DelReviewMgrFactory.createInstance(TestUtils.getContext());

            // Now read the OrgUnit back to make sure it got added OK.
            OrgUnit outOU = dReviewMgr.read(inOU);
            assertTrue(szLocation + " failed read", inOU.equals(outOU));
            LOG.info(szLocation + " [" + outOU.getName() + "] success");
        }
        catch (SecurityException ex)
        {
            LOG.error(szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

}
