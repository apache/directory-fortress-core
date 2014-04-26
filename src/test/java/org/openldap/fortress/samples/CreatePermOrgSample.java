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

import org.openldap.fortress.DelReviewMgr;
import org.openldap.fortress.FinderException;
import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.SecurityException;
import org.openldap.fortress.DelAdminMgr;
import org.openldap.fortress.DelAdminMgrFactory;
import org.openldap.fortress.DelReviewMgrFactory;
import org.openldap.fortress.rbac.OrgUnit;
import org.openldap.fortress.rbac.TestUtils;
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
 * @author Shawn McKinney
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
