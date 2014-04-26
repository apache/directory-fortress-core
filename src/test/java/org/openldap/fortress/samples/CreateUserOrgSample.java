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

import org.openldap.fortress.DelAdminMgr;
import org.openldap.fortress.DelAdminMgrFactory;
import org.openldap.fortress.DelReviewMgr;
import org.openldap.fortress.DelReviewMgrFactory;
import org.openldap.fortress.FinderException;
import org.openldap.fortress.SecurityException;
import org.openldap.fortress.rbac.OrgUnit;
import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.rbac.TestUtils;
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
 * @author Shawn McKinney
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
