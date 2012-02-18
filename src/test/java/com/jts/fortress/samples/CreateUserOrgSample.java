/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.samples;

import com.jts.fortress.*;
import com.jts.fortress.SecurityException;
import com.jts.fortress.arbac.OrgUnit;
import com.jts.fortress.constants.GlobalErrIds;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;


/**
 * CreateUserOrgSample JUnit Test.  Fortress User entities must be associated with an OrgUnit.
 * The User OrgUnit is associated with corresponding organizational groups that the User has been assigned by their
 * enterprise resource department.  There is no limit to the number of User Organizations that are created but at least
 * one must be created before any User can be added to the directory.  Each OrgUnit may be related to one or more parent
 * OrgUnits although that functionality is not demonstrated here.
 *
 * @author smckinn
 * @created March 1, 2011
 */
public class CreateUserOrgSample extends TestCase
{
    private static final String OCLS_NM = CreateUserOrgSample.class.getName();
    private static final Logger log = Logger.getLogger(OCLS_NM);
    public static final String TEST_USER_OU_NM = "sampleUsers-OU.1";

    public CreateUserOrgSample(String name)
    {
        super(name);
    }

    /**
     * Run the User OrgUnit test suite.
     * @return
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
        String szLocation = OCLS_NM + ".testCreateUserOrg";
        try
        {
            DelegatedReviewMgr dRevAdminMgr = DelegatedReviewMgrFactory.createInstance();

            // The OrgUnit requires name and type to be set before use.
            OrgUnit inOU = new OrgUnit(TEST_USER_OU_NM, OrgUnit.Type.USER);

            try
            {
                OrgUnit outOu = dRevAdminMgr.read(inOU);
                // if org is found, return.
                return;
            }
            catch(FinderException fe)
            {
                assertTrue(szLocation + " excep id check", fe.getErrorId() == GlobalErrIds.ORG_NOT_FOUND_USER);
                // pass
            }

            // Instantiate the Delegated AdminMgr implementation object which provisions OrgUnits and AdminRoles to the system.
            DelegatedAdminMgr dAdminMgr = DelegatedAdminMgrFactory.createInstance();

            // Add the OrgUnit to the directory.
            dAdminMgr.add(inOU);

            // Instantiate the Delegated RevewMgr implementation which interrogates the OrgUnit and AdminRole data.
            DelegatedReviewMgr dReviewMgr = DelegatedReviewMgrFactory.createInstance();

            // Now read the OrgUnit back to make sure it got added OK.
            OrgUnit outOU = dReviewMgr.read(inOU);
            assertTrue(szLocation + " failed read", inOU.equals(outOU));
            log.info(szLocation + " [" + outOU.getName() + "] success");
        }
        catch (SecurityException ex)
        {
            log.error(szLocation + " caught SecurityException errCode=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

}
