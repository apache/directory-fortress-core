/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress;

import us.jts.fortress.rbac.OrgUnit;
import us.jts.fortress.rbac.TestUtils;
import us.jts.fortress.rbac.UserAdminRole;
import us.jts.fortress.rbac.Address;
import us.jts.fortress.rbac.Permission;
import us.jts.fortress.rbac.Role;
import us.jts.fortress.rbac.User;
import us.jts.fortress.rbac.UserRole;
import us.jts.fortress.util.attr.VUtil;
import us.jts.fortress.util.time.Constraint;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Enumeration;

/**
 * Test class for driving Fortress RBAC review API within a console.
 *
 * @author Shawn McKinney
 */
class ReviewMgrConsole
{
    private ReviewMgr rm;
    private static final String CLS_NM = ReviewMgrConsole.class.getName();
    final private static Logger log = Logger.getLogger(CLS_NM);

    /**
     * Constructor for the ReviewMgrConsole object
     */
    ReviewMgrConsole()
    {
        try
        {
            rm = ReviewMgrFactory.createInstance(TestUtils.getContext());
        }
        catch (SecurityException e)
        {
            log.error(CLS_NM + " constructor caught SecurityException  rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
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
            System.out.println("Enter Role name to read:");
            Role role = new Role();
            role.setName(ReaderUtil.readLn());
            Role re = rm.readRole(role);
            if (re != null)
            {
                System.out.println("ROLE OBJECT:");
                System.out.println("    name      [" + re.getName() + "]");
                System.out.println("    internalId  [" + re.getId() + "]");
                System.out.println("    description [" + re.getDescription() + "]");
                System.out.println("    parents [" + re.getParents() + "]");
                printTemporal(re, "RBACROLE");
            }
            else
            {
                System.out.println("	name [" + role.getName() + "] was not found");
            }
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            log.error(CLS_NM + ".readRole caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    void findRoles()
    {
        ReaderUtil.clearScreen();
        try
        {
            System.out.println("");
            System.out.println("Enter value to search Roles with:");
            String val = ReaderUtil.readLn();
            List<Role> list = rm.findRoles(val);
            if (list != null && list.size() > 0)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    Role re = list.get(i);
                    System.out.println("ROLE OBJECT [" + i + "]:");
                    System.out.println("    name      [" + re.getName() + "]");
                    System.out.println("    internalId  [" + re.getId() + "]");
                    System.out.println("    description [" + re.getDescription() + "]");
                    System.out.println("    parents [" + re.getParents() + "]");
                    printTemporal(re, "RBACROLE");
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
            log.error(CLS_NM + ".findRoles caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     * Description of the Method
     */
    protected void displaySessions()
    {
    }


    /**
     * Description of the Method
     */
    protected void displayUsers()
    {
        try
        {
            User ue;
            ArrayList list = (ArrayList) rm.findUsers(new User());
            int size = list.size();

            for (int i = 0; i < size; i++)
            {
                ue = (User) list.get(i);
                System.out.println("USER[" + i + "]");
                System.out.println("    userId      [" + ue.getUserId() + "]");
                System.out.println("    internalId  [" + ue.getInternalId() + "]");
                System.out.println("    description [" + ue.getDescription() + "]");
                System.out.println("    common name [" + ue.getCn() + "]");
                System.out.println("    surname     [" + ue.getSn() + "]");
                System.out.println("    orgUnitId   [" + ue.getOu() + "]");
                System.out.println("    pwpolicy    [" + ue.getPwPolicy() + "]");
                printTemporal(ue, "USER");
                printAddress(ue.getAddress(), "ADDRESS");
                printPhone(ue.getPhones(), "PHONES");
                printPhone(ue.getMobiles(), "MOBILES");
                if (ue.getRoles() != null)
                {
                    for (UserRole ur : ue.getRoles())
                    {
                        printTemporal(ur, "RBACROLE");
                    }
                }
                if (ue.getAdminRoles() != null)
                {
                    for (UserAdminRole ur : ue.getAdminRoles())
                    {
                        printAdminRole(ur);
                        printTemporal(ur, "ADMINROLE");
                    }
                }
                System.out.println();
            }
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            log.error(CLS_NM + ".displayUsers caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     * Description of the Method
     */
    void findUsers()
    {
        String userVal;

        ReaderUtil.clearScreen();
        try
        {
            System.out.println("Enter User Search Value");
            userVal = ReaderUtil.readLn();
            User ue = new User();
            ue.setUserId(userVal);
            ArrayList list = (ArrayList) rm.findUsers(ue);
            int size = list.size();

            for (int i = 0; i < size; i++)
            {
                ue = (User) list.get(i);
                System.out.println("USER[" + i + "]");
                System.out.println("    userId      [" + ue.getUserId() + "]");
                System.out.println("    internalId  [" + ue.getInternalId() + "]");
                System.out.println("    description [" + ue.getDescription() + "]");
                System.out.println("    common name [" + ue.getCn() + "]");
                System.out.println("    surname     [" + ue.getSn() + "]");
                System.out.println("    orgUnitId   [" + ue.getOu() + "]");
                System.out.println("    pwpolicy    [" + ue.getPwPolicy() + "]");
                System.out.println("    seqId       [" + ue.getSequenceId() + "]");
                printTemporal(ue, "USER");
                printAddress(ue.getAddress(), "ADDRESS");
                printPhone(ue.getPhones(), "PHONES");
                printPhone(ue.getMobiles(), "MOBILES");
                if (ue.getRoles() != null)
                {
                    for (UserRole ur : ue.getRoles())
                    {
                        printTemporal(ur, "RBACROLE");
                    }
                }
                if (ue.getAdminRoles() != null)
                {
                    for (UserAdminRole ur : ue.getAdminRoles())
                    {
                        printAdminRole(ur);
                        printTemporal(ur, "ADMINROLE");
                    }
                }
                if (ue.getProperties() != null && ue.getProperties().size() > 0)
                {
                    int ctr = 0;
                    for (Enumeration e = ue.getProperties().propertyNames(); e.hasMoreElements();)
                    {
                        String key = (String) e.nextElement();
                        String val = ue.getProperty(key);
                        System.out.println("prop key[" + ctr + "]=" + key);
                        System.out.println("prop value[" + ctr++ + "]=" + val);
                    }
                }

                System.out.println();
            }
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            log.error(CLS_NM + ".findUsers caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    void getUser()
    {
        String userVal;
        ReaderUtil.clearScreen();
        try
        {
            System.out.println("Enter Internal id for user:");
            userVal = ReaderUtil.readLn();
            User ue = new User();
            ue.setInternalId(userVal);
            ArrayList list = (ArrayList) rm.findUsers(ue);
            int size = list.size();
            for (int i = 0; i < size; i++)
            {
                ue = (User) list.get(i);
                System.out.println("USER[" + i + "]");
                System.out.println("    userId      [" + ue.getUserId() + "]");
                System.out.println("    internalId  [" + ue.getInternalId() + "]");
                System.out.println("    description [" + ue.getDescription() + "]");
                System.out.println("    common name [" + ue.getCn() + "]");
                System.out.println("    surname     [" + ue.getSn() + "]");
                System.out.println("    orgUnitId   [" + ue.getOu() + "]");
                System.out.println("    pwpolicy    [" + ue.getPwPolicy() + "]");
                printTemporal(ue, "USER");
                printAddress(ue.getAddress(), "ADDRESS");
                printPhone(ue.getPhones(), "PHONES");
                printPhone(ue.getMobiles(), "MOBILES");
                if (ue.getRoles() != null)
                {
                    for (UserRole ur : ue.getRoles())
                    {
                        printTemporal(ur, "RBACROLE");
                    }
                }
                if (ue.getAdminRoles() != null)
                {
                    for (UserAdminRole ur : ue.getAdminRoles())
                    {
                        printAdminRole(ur);
                        printTemporal(ur, "ADMINROLE");
                    }
                }
                if (ue.getProperties() != null && ue.getProperties().size() > 0)
                {
                    int ctr = 0;
                    for (Enumeration e = ue.getProperties().propertyNames(); e.hasMoreElements();)
                    {
                        String key = (String) e.nextElement();
                        String val = ue.getProperty(key);
                        System.out.println("prop key[" + ctr + "]=" + key);
                        System.out.println("prop value[" + ctr++ + "]=" + val);
                    }
                }

                System.out.println();
            }
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            log.error(CLS_NM + ".getUser caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    void findUsersByOrg()
    {
        String szOu;

        ReaderUtil.clearScreen();
        try
        {
            System.out.println("Enter OrgUnit name");
            szOu = ReaderUtil.readLn();
            OrgUnit ou = new OrgUnit(szOu);
            ou.setType(OrgUnit.Type.USER);
            List<User> list = rm.findUsers(ou);
            int ctr = 0;
            for (User ue : list)
            {
                System.out.println("USER[" + ++ctr + "]");
                System.out.println("    userId      [" + ue.getUserId() + "]");
                System.out.println("    internalId  [" + ue.getInternalId() + "]");
                System.out.println("    description [" + ue.getDescription() + "]");
                System.out.println("    common name [" + ue.getCn() + "]");
                System.out.println("    surname     [" + ue.getSn() + "]");
                System.out.println("    orgUnitId   [" + ue.getOu() + "]");
                System.out.println("    pwpolicy    [" + ue.getPwPolicy() + "]");
                printTemporal(ue, "USER");
                printAddress(ue.getAddress(), "ADDRESS");
                printPhone(ue.getPhones(), "PHONES");
                printPhone(ue.getMobiles(), "MOBILES");
                if (ue.getRoles() != null)
                {
                    for (UserRole ur : ue.getRoles())
                    {
                        printTemporal(ur, "RBACROLE");
                    }
                }
                if (ue.getAdminRoles() != null)
                {
                    for (UserAdminRole ur : ue.getAdminRoles())
                    {
                        printAdminRole(ur);
                        printTemporal(ur, "ADMINROLE");
                    }
                }
                if (ue.getProperties() != null && ue.getProperties().size() > 0)
                {
                    int pctr = 0;
                    for (Enumeration e = ue.getProperties().propertyNames(); e.hasMoreElements();)
                    {
                        String key = (String) e.nextElement();
                        String val = ue.getProperty(key);
                        System.out.println("prop key[" + pctr + "]=" + key);
                        System.out.println("prop value[" + pctr++ + "]=" + val);
                    }
                }

                System.out.println();
            }
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            log.error(CLS_NM + ".findUsersByOrg caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
      *
      */
     void assignedRoles()
     {
         ReaderUtil.clearScreen();
         try
         {
             System.out.println("Enter UserId:");
             User user = new User();
             user.setUserId(ReaderUtil.readLn());
             List<UserRole> userRoles = rm.assignedRoles(user);
             if (userRoles != null)
             {
                 for(UserRole userRole : userRoles)
                 {
                     System.out.println("ROLE OBJECT:");
                     System.out.println("    name      [" + userRole.getName() + "]");
                     printTemporal(userRole, "RBACROLE");
                 }

             }
             else
             {
                 System.out.println("	userId [" + user.getUserId() + "] has no roles");
             }
             System.out.println("ENTER to continue");
         }
         catch (SecurityException e)
         {
             log.error(CLS_NM + ".assignedRoles caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
         }
         ReaderUtil.readChar();
     }


    static void printTemporal(Constraint constraint, String label)
    {
        if (constraint != null)
        {
            System.out.println("    " + label + "[" + constraint.getName() + "]:");
            System.out.println("        begin time [" + constraint.getBeginTime() + "]");
            System.out.println("        end time [" + constraint.getEndTime() + "]");
            System.out.println("        begin date [" + constraint.getBeginDate() + "]");
            System.out.println("        end date [" + constraint.getEndDate() + "]");
            System.out.println("        begin lock [" + constraint.getBeginLockDate() + "]");
            System.out.println("        end lock [" + constraint.getEndLockDate() + "]");
            System.out.println("        day mask [" + constraint.getDayMask() + "]");
            System.out.println("        time out [" + constraint.getTimeout() + "]");
        }
    }

    private static void printAddress(Address address, String label)
    {
        if (address != null)
        {
            System.out.println(label);
            if(VUtil.isNotNullOrEmpty(address.getAddresses()))
            {
                for (String addr : address.getAddresses())
                {
                    System.out.println("    line        [" + addr + "]");
                }
            }
            System.out.println("    city        [" + address.getCity() + "]");
            System.out.println("    state       [" + address.getState() + "]");
            System.out.println("    zip         [" + address.getPostalCode() + "]");
        }
    }

    private static void printPhone(List<String> phones, String label)
    {
        if (phones != null)
        {
            System.out.println(label);
            for(String phone : phones)
            {
                System.out.println("    number      [" + phone + "]");
            }
        }
    }

    private static void printAdminRole(UserAdminRole ur)
    {
        if (ur != null)
        {
            System.out.println("    ADMINATTR UID[" + ur.getUserId() + "]:");
            System.out.println("        begin range [" + ur.getBeginRange() + "]");
            System.out.println("        end range [" + ur.getEndRange() + "]");
            System.out.println("        OsP [" + ur.getOsP() + "]");
            System.out.println("        OsU [" + ur.getOsU() + "]");
        }
    }

    /**
     * Description of the Method
     */
    void readUser()
    {
        String userId;

        ReaderUtil.clearScreen();
        try
        {
            System.out.println("Enter UserId to read:");
            userId = ReaderUtil.readLn();
            User user = new User();
            user.setUserId(userId);
            User ue = rm.readUser(user);
            if (ue != null)
            {
                System.out.println("USER OBJECT:");
                System.out.println("    userId      [" + ue.getUserId() + "]");
                System.out.println("    internalId  [" + ue.getInternalId() + "]");
                System.out.println("    description [" + ue.getDescription() + "]");
                System.out.println("    common name [" + ue.getCn() + "]");
                System.out.println("    surname     [" + ue.getSn() + "]");
                System.out.println("    orgUnitId   [" + ue.getOu() + "]");
                System.out.println("    pwpolicy    [" + ue.getPwPolicy() + "]");
                printTemporal(ue, "USER");
                if (ue.getRoles() != null)
                {
                    for (UserRole ur : ue.getRoles())
                    {
                        printTemporal(ur, "RBACROLE");
                    }
                }
                if (ue.getAdminRoles() != null)
                {
                    for (UserAdminRole ur : ue.getAdminRoles())
                    {
                        printAdminRole(ur);
                        printTemporal(ur, "ADMINROLE");
                    }
                }
                if (ue.getProperties() != null && ue.getProperties().size() > 0)
                {
                    int ctr = 0;
                    for (Enumeration e = ue.getProperties().propertyNames(); e.hasMoreElements();)
                    {
                        String key = (String) e.nextElement();
                        String val = ue.getProperty(key);
                        System.out.println("prop key[" + ctr + "]=" + key);
                        System.out.println("prop value[" + ctr++ + "]=" + val);
                    }
                }
            }
            else
            {
                System.out.println("	user [" + userId + "] was not found");
            }
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            log.error(CLS_NM + ".readUser caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     * Description of the Method
     */
    void searchPermissions()
    {
        Permission pe;
        /*
           *  This is the Fortress Logical Data Model for Perm:
           *  public class Perm
           *  implements java.io.Serializable
           *  {
           *  public void setInternalId()
           *  public void setInternalId(String internalId)
           *  public String getInternalId()
           *  public void setName(String name)
           *  public String getName()
           *  public void setDescription(String description)
           *  public String getDescription()
           *  public void addRole(String name)
           *  public List getRoles()
           *  public void addRoles(ArrayList roles)
           *  public void addUser(String user)
           *  public List getUsers()
           *  public void addUsers(ArrayList users)
           *  public void addGroup(String group)
           *  public List getGroups()
           *  public void addGroups(ArrayList groups)
           *  public void addProperty(String key, String value)
           *  public void addProperties(Properties props)
           *  public Properties getProperties()
           *  public String getProperty(String key)
           *  public void setOu(String ou)
           *  public String getOu()
           *  public void setType(String type)
           *  public String getType()
           *  public void setDn(String dn)
           *  public String getDn()
           *  public void addFinePerm(FinePerm fPerm)
           *  public HashMap getFinePerms()
           *  public void addFinePerms(HashMap fPerms)
           *  }
           *  public class FinePerm
           *  implements java.io.Serializable
           *  {
           *  public String getObjectName()
           *  public void setObjectName(String objectName)
           *  public void addRole(String name)
           *  public List getRoles()
           *  public void addUser(String user)
           *  public List getUsers()
           *  public void addGroup(String group)
           *  public List getGroups()
           *  }
           */
        try
        {
            //ReaderUtil.clearScreen();
            //System.out.println("Enter operationid");

            System.out.println("Enter object name:");
            String name = ReaderUtil.readLn();
            System.out.println("Enter op name:");
            String opname = ReaderUtil.readLn();
            pe = new Permission();
            pe.setObjectName(name);
            pe.setOpName(opname);
            List list = rm.findPermissions(pe);
            if (list != null)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    pe = (Permission) list.get(i);
                    System.out.println("**perm:" + (i + 1) + "***");
                    //System.out.println("perm operation [" + pe.operation + "]");
                    System.out.println("object name [" + pe.getObjectName() + "]");
                    System.out.println("object id [" + pe.getObjectId() + "]");
                    System.out.println("operation name [" + pe.getOpName() + "]");
                    System.out.println("abstract perm name [" + pe.getAbstractName() + "]");
                    System.out.println("internalId [" + pe.getInternalId() + "]");
                    if (pe.getUsers() != null && pe.getUsers().size() > 0)
                    {
                        int ctr = 0;
                        for (String user : pe.getUsers())
                        {
                            System.out.println("user[" + ctr++ + "]=" + user);
                        }
                    }
                    if (pe.getRoles() != null && pe.getRoles().size() > 0)
                    {
                        int ctr = 0;
                        for (String role : pe.getRoles())
                        {
                            System.out.println("name[" + ctr++ + "]=" + role);
                        }
                    }
                    if (pe.getProperties() != null && pe.getProperties().size() > 0)
                    {
                        int ctr = 0;
                        for (Enumeration e = pe.getProperties().propertyNames(); e.hasMoreElements();)
                        {
                            String key = (String) e.nextElement();
                            String val = pe.getProperty(key);
                            System.out.println("prop key[" + ctr + "]=" + key);
                            System.out.println("prop value[" + ctr++ + "]=" + val);
                        }
                    }

                    //prettyPrintFinePermissions(pe.getFinePerms());
                    System.out.println("**");
                }
                System.out.println("search complete");
                System.out.println("ENTER to continue");
            }
        }
        catch (SecurityException e)
        {
            log.error(CLS_NM + ".searchPermissions caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     *  Description of the Method
     *
     *@param  pmap  Description of the Parameter
     */
/*	private final void prettyPrintFinePermissions(Map pmap)
	{
		if (pmap != null)
		{
			Set pset = pmap.keySet();
			Iterator piter = pset.iterator();
			while (piter.hasNext())
			{
				FinePerm perm = (FinePerm) pmap.get(piter.next());
				System.out.println("Fine Perm objectname:" + perm.getObjectName());
				if (perm.getRoles() != null && perm.getRoles().size() > 0)
				{
					List roles = perm.getRoles();
					for (int i = 0; i < roles.size(); i++)
					{
						String name = (String) roles.get(i);
						System.out.println("Fine Perm name: " + name);
					}
				}
				if (perm.getUsers() != null && perm.getUsers().size() > 0)
				{
					List users = perm.getUsers();
					for (int i = 0; i < users.size(); i++)
					{
						String user = (String) users.get(i);
						System.out.println("Fine Perm user: " + user);
					}
				}
				if (perm.getGroups() != null && perm.getGroups().size() > 0)
				{
					List groups = perm.getGroups();
					for (int i = 0; i < groups.size(); i++)
					{
						String group = (String) groups.get(i);
						System.out.println("Fine Perm group: " + group);
					}
				}
			}
		}
	}
	*/


    /**
     * Description of the Method
     */
    void readPermission()
    {
        Permission pe;
        try
        {
            Permission permission = new Permission();
            ReaderUtil.clearScreen();

            System.out.println("Enter perm object name:");
            String name = ReaderUtil.readLn();
            permission.setObjectName(name);
            System.out.println("Enter perm object id or null for none:");
            String oid = ReaderUtil.readLn();
            permission.setObjectId(oid);
            System.out.println("Enter perm operation name:");
            String op = ReaderUtil.readLn();
            permission.setOpName(op);
            pe = rm.readPermission(permission);
            if (pe != null)
            {
                //System.out.println("perm operation [" + pe.operation + "]");
                System.out.println("object name [" + pe.getObjectName() + "]");
                System.out.println("object id [" + pe.getObjectId() + "]");
                System.out.println("operation name [" + pe.getOpName() + "]");
                System.out.println("abstract perm name [" + pe.getAbstractName() + "]");
                System.out.println("internalId [" + pe.getInternalId() + "]");
                if (pe.getUsers() != null && pe.getUsers().size() > 0)
                {
                    int ctr = 0;
                    for (String user : pe.getUsers())
                    {
                        System.out.println("user[" + ctr++ + "]=" + user);
                    }
                }
                if (pe.getRoles() != null && pe.getRoles().size() > 0)
                {
                    int ctr = 0;
                    for (String role : pe.getRoles())
                    {
                        System.out.println("name[" + ctr++ + "]=" + role);
                    }
                }
                if (pe.getProperties() != null && pe.getProperties().size() > 0)
                {
                    int ctr = 0;
                    for (Enumeration e = pe.getProperties().propertyNames(); e.hasMoreElements();)
                    {
                        String key = (String) e.nextElement();
                        String val = pe.getProperty(key);
                        System.out.println("prop key[" + ctr + "]=" + key);
                        System.out.println("prop value[" + ctr++ + "]=" + val);
                    }
                }
                System.out.println("**");
                System.out.println("read operation complete");
                System.out.println("ENTER to continue");
            }
        }
        catch (SecurityException e)
        {
            log.error(CLS_NM + ".readPermission caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     * Description of the Method
     */
    void permissionRoles()
    {
        Permission pe;
        try
        {
            Permission permission = new Permission();
            ReaderUtil.clearScreen();

            System.out.println("Enter perm object name:");
            String name = ReaderUtil.readLn();
            permission.setObjectName(name);
            System.out.println("Enter perm object id or null for none:");
            String oid = ReaderUtil.readLn();
            permission.setObjectId(oid);
            System.out.println("Enter perm operation name:");
            String op = ReaderUtil.readLn();
            permission.setOpName(op);
            pe = rm.readPermission(permission);
            if (pe != null)
            {
                //System.out.println("perm operation [" + pe.operation + "]");
                System.out.println("object name [" + pe.getObjectName() + "]");
                System.out.println("object id [" + pe.getObjectId() + "]");
                System.out.println("operation name [" + pe.getOpName() + "]");
                System.out.println("abstract perm name [" + pe.getAbstractName() + "]");
                System.out.println("internalId [" + pe.getInternalId() + "]");
                if (pe.getRoles() != null && pe.getRoles().size() > 0)
                {
                    int ctr = 0;
                    for (String role : pe.getRoles())
                    {
                        System.out.println("name[" + ctr++ + "]=" + role);
                    }
                }
                System.out.println("**");
                System.out.println("read operation complete");
                System.out.println("ENTER to continue");
            }
        }
        catch (SecurityException e)
        {
            log.error(CLS_NM + ".permissionRoles caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    void permissionUsers()
    {
        Permission pe;
        try
        {
            Permission permission = new Permission();
            ReaderUtil.clearScreen();

            System.out.println("Enter perm object name:");
            String name = ReaderUtil.readLn();
            permission.setObjectName(name);
            System.out.println("Enter perm object id or null for none:");
            String oid = ReaderUtil.readLn();
            permission.setObjectId(oid);
            System.out.println("Enter perm operation name:");
            String op = ReaderUtil.readLn();
            permission.setOpName(op);
            pe = rm.readPermission(permission);
            if (pe != null)
            {
                //System.out.println("perm operation [" + pe.operation + "]");
                System.out.println("object name [" + pe.getObjectName() + "]");
                System.out.println("object id [" + pe.getObjectId() + "]");
                System.out.println("operation name [" + pe.getOpName() + "]");
                System.out.println("abstract perm name [" + pe.getAbstractName() + "]");
                System.out.println("internalId [" + pe.getInternalId() + "]");
                if (pe.getUsers() != null && pe.getUsers().size() > 0)
                {
                    int ctr = 0;
                    for (String user : pe.getUsers())
                    {
                        System.out.println("user[" + ctr++ + "]=" + user);
                    }
                }
                System.out.println("**");
                System.out.println("read operation complete");
                System.out.println("ENTER to continue");
            }
        }
        catch (SecurityException e)
        {
            log.error(CLS_NM + ".permissionUsers caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    void authorizedUsers()
    {
        try
        {
            System.out.println("Enter role name:");
            String name = ReaderUtil.readLn();
            User ue;
            ArrayList list = (ArrayList) rm.authorizedUsers(new Role(name));
            int size = list.size();

            for (int i = 0; i < size; i++)
            {
                ue = (User) list.get(i);
                System.out.println("USER[" + i + "]");
                System.out.println("    userId      [" + ue.getUserId() + "]");
                System.out.println("    internalId  [" + ue.getInternalId() + "]");
                System.out.println("    description [" + ue.getDescription() + "]");
                System.out.println("    common name [" + ue.getCn() + "]");
                System.out.println("    surname     [" + ue.getSn() + "]");
                System.out.println("    orgUnitId   [" + ue.getOu() + "]");
                printTemporal(ue, "USER");
                System.out.println();
            }
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            log.error(CLS_NM + ".authorizedUsers caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    void rolePermissions()
    {
        try
        {
            System.out.println("Enter role name:");
            String name = ReaderUtil.readLn();
            List<Permission> list = rm.rolePermissions(new Role(name));
            if (list != null)
            {
                int ctr = 0;
                for (Permission pe : list)
                {
                    System.out.println("**perm:" + (++ctr) + "***");
                    //System.out.println("perm operation [" + pe.operation + "]");
                    System.out.println("object name [" + pe.getObjectName() + "]");
                    System.out.println("object id [" + pe.getObjectId() + "]");
                    System.out.println("operation name [" + pe.getOpName() + "]");
                    System.out.println("abstract perm name [" + pe.getAbstractName() + "]");
                    System.out.println("internalId [" + pe.getInternalId() + "]");
                    if (pe.getUsers() != null && pe.getUsers().size() > 0)
                    {
                        int ctr2 = 0;
                        for (String user : pe.getUsers())
                        {
                            System.out.println("user[" + ctr2++ + "]=" + user);
                        }
                    }
                    if (pe.getRoles() != null && pe.getRoles().size() > 0)
                    {
                        int ctr2 = 0;
                        for (String role : pe.getRoles())
                        {
                            System.out.println("name[" + ctr2++ + "]=" + role);
                        }
                    }
                    if (pe.getProperties() != null && pe.getProperties().size() > 0)
                    {
                        int pctr = 0;
                        for (Enumeration e = pe.getProperties().propertyNames(); e.hasMoreElements();)
                        {
                            String key = (String) e.nextElement();
                            String val = pe.getProperty(key);
                            System.out.println("prop key[" + pctr + "]=" + key);
                            System.out.println("prop value[" + pctr++ + "]=" + val);
                        }
                    }

                    //prettyPrintFinePermissions(pe.getFinePerms());
                    System.out.println("**");
                }
                System.out.println("search complete");
                System.out.println("ENTER to continue");
            }
        }
        catch (SecurityException e)
        {
            log.error(CLS_NM + ".rolePermissions caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

}