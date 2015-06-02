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

import org.apache.directory.fortress.core.DelReviewMgr;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.DelAdminMgr;
import org.apache.directory.fortress.core.DelAdminMgrFactory;
import org.apache.directory.fortress.core.DelReviewMgrFactory;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.impl.TestUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * CreatePermOrgSample JUnit Test. Fortress Permission Object entities must be associated with an OrgUnit.
 * The Perm OrgUnit is associated with the corresponding Application Group that is responsible for the maintenance of
 * the Permission Objects.  There is no limit to the amount of Permission Organizations that are created but at least
 * one must be created before any Permissions can be added to the directory.  Each OrgUnit may be related to one or more parent
 * OrgUnits although that functionality is not demonstrated here.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CreatePermOrgSample extends TestCase
{
    private static final String CLS_NM = CreatePermOrgSample.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    public static final String TEST_PERM_OU_NM = "samplePerms-OU.1";
    private static final String TEST_PERM_OU_NM2 = "KillerBikes.com";

    public CreatePermOrgSample(String name)
    {
        super(name);
    }

    /**
     * Run the Permission OrgUnit test cases.
     * @return Test
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest(new CreatePermOrgSample("testCreatePermOrg"));
        return suite;
    }

    /**
     * Create a new Permission OrgUnit entity in LDAP.  The Permission OrgUnit entity must have the
     * OrgUnit name and the OrgUnit type set before being added.
     *
     */
    public static void testCreatePermOrg()
    {
        String szLocation = ".testCreatePermOrg";
        try
        {
            DelReviewMgr dRevAdminMgr = DelReviewMgrFactory.createInstance(TestUtils.getContext());

            // The OrgUnit requires name and type to be set before use.
            OrgUnit inOU = new OrgUnit(TEST_PERM_OU_NM, OrgUnit.Type.PERM);

            try
            {
                dRevAdminMgr.read(inOU);
                // if org is found, return.
                return;
            }
            catch(FinderException fe)
            {
                assertTrue(szLocation + " excep id check", fe.getErrorId() == GlobalErrIds.ORG_NOT_FOUND_PERM);
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


    /**
     *
     */
    public static void testCreatePermOrg2()
    {
        String szLocation = ".testCreatePermOrg2";
        try
        {
            DelReviewMgr dRevAdminMgr = DelReviewMgrFactory.createInstance(TestUtils.getContext());

            // The OrgUnit requires name and type to be set before use.
            OrgUnit inOU = new OrgUnit(TEST_PERM_OU_NM2, OrgUnit.Type.PERM);

            try
            {
                dRevAdminMgr.read(inOU);
                // if org is found, return.
                return;
            }
            catch(FinderException fe)
            {
                assertTrue(szLocation + " excep id check", fe.getErrorId() == GlobalErrIds.ORG_NOT_FOUND_PERM);
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
