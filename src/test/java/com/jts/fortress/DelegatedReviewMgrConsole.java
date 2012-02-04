/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.arbac.AdminRole;
import com.jts.fortress.arbac.UserAdminRole;
import com.jts.fortress.rbac.User;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Description of the Class
 *
 * @author smckinn
 * @created November 14, 2010
 */
public class DelegatedReviewMgrConsole
{
    private DelegatedReviewMgr rm;
    private static final String OCLS_NM = DelegatedReviewMgrConsole.class.getName();
    private static final Logger log = Logger.getLogger(OCLS_NM);

    /**
     * Constructor for the ReviewMgrConsole object
     */
    DelegatedReviewMgrConsole()
    {
        try
        {
            rm = DelegatedReviewMgrFactory.createInstance();
        }
        catch (SecurityException e)
        {
            log.error(OCLS_NM + " constructor caught SecurityException errCode=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
    }

    /**
     *
     */
    protected void readRole()
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
            log.error(OCLS_NM + ".readRole caught SecurityException errCode=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    protected void findRoles()
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
            log.error(OCLS_NM + ".findRoles caught SecurityException errCode=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    protected void assignedUsers()
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
            log.error(OCLS_NM + ".assignedUsers caught SecurityException errCode=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    /**
     *
     */
    protected void assignedRoles()
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
            log.error(OCLS_NM + ".assignedRoles caught SecurityException errCode=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }
}
