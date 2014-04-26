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

package org.openldap.fortress;

import org.openldap.fortress.rbac.AdminRole;
import org.openldap.fortress.rbac.TestUtils;
import org.openldap.fortress.rbac.UserAdminRole;
import org.openldap.fortress.rbac.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Description of the Class
 *
 * @author Shawn McKinney
 */
class DelegatedReviewMgrConsole
{
    private DelReviewMgr rm;
    private static final String CLS_NM = DelegatedReviewMgrConsole.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    /**
     * Constructor for the ReviewMgrConsole object
     */
    DelegatedReviewMgrConsole()
    {
        try
        {
            rm = DelReviewMgrFactory.createInstance(TestUtils.getContext());
        }
        catch (SecurityException e)
        {
            LOG.error(" constructor caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
    }

    /**
     *
     */
    void readRole()
    {
        ReaderUtil.clearScreen();
        try
        {
            System.out.println("Enter AdminRole name to read:");
            AdminRole role = new AdminRole();
            role.setName(ReaderUtil.readLn());
            AdminRole re = rm.readRole(role);
            if (re != null)
            {
                System.out.println("ADMIN ROLE OBJECT:");
                System.out.println("    name      [" + re.getName() + "]");
                System.out.println("    internalId  [" + re.getId() + "]");
                System.out.println("    description [" + re.getDescription() + "]");
                System.out.println("    osU [" + re.getOsU() + "]");
                System.out.println("    osP [" + re.getOsP() + "]");
                ReviewMgrConsole.printTemporal(re, "ADMINROLE");
            }
            else
            {
                System.out.println("	name [" + role.getName() + "] was not found");
            }
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("readRole caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    void findRoles()
    {
        ReaderUtil.clearScreen();
        try
        {
            System.out.println("Enter value to search AdminRoles with:");
            String val = ReaderUtil.readLn();
            List<AdminRole> list = rm.findRoles(val);
            if (list != null && list.size() > 0)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    AdminRole re = list.get(i);
                    System.out.println("ADMIN ROLE OBJECT [" + i + "]:");
                    System.out.println("    name      [" + re.getName() + "]");
                    System.out.println("    internalId  [" + re.getId() + "]");
                    System.out.println("    description [" + re.getDescription() + "]");
                    System.out.println("    osU [" + re.getOsU() + "]");
                    System.out.println("    osP [" + re.getOsP() + "]");
                    ReviewMgrConsole.printTemporal(re, "ADMINROLE");
                }
            }
            else
            {
                System.out.println("name [" + val + "] was not found");
            }
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("findRoles caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    void assignedUsers()
    {
        try
        {
            System.out.println("Enter admin role name:");
            String name = ReaderUtil.readLn();
            List<User> list = rm.assignedUsers(new AdminRole(name));
            int i = 0;
            for (User ue : list)
            {
                System.out.println("USER[" + ++i + "]");
                System.out.println("    userId      [" + ue.getUserId() + "]");
                System.out.println("    internalId  [" + ue.getInternalId() + "]");
                System.out.println("    description [" + ue.getDescription() + "]");
                System.out.println("    common name [" + ue.getCn() + "]");
                System.out.println("    surname     [" + ue.getSn() + "]");
                System.out.println("    orgUnitId   [" + ue.getOu() + "]");
                ReviewMgrConsole.printTemporal(ue, "USER");
                System.out.println();
            }
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("assignedUsers caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    /**
     *
     */
    void assignedRoles()
    {
        String userId;

        ReaderUtil.clearScreen();
        try
        {
            System.out.println("Enter UserId to read admin roles:");
            userId = ReaderUtil.readLn();
            User user = new User();
            user.setUserId(userId);
            List<UserAdminRole> roles = rm.assignedRoles(user);
            for(UserAdminRole re : roles)
            {
                System.out.println("USER OBJECT:");
                System.out.println("    admin role  [" + re.getName() + "]");
                System.out.println("    OsU         [" + re.getOsU() + "]");
                System.out.println("    OsP         [" + re.getOsP() + "]");
                ReviewMgrConsole.printTemporal(re, "ADMINROLE");
            }
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("assignedRoles caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }
}
