/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

/*
 *  This class is used for testing purposes.
 */
package com.jts.fortress;

import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.rbac.Permission;
import com.jts.fortress.rbac.Role;
import com.jts.fortress.rbac.User;
import com.jts.fortress.rbac.Session;

import com.jts.fortress.util.attr.VUtil;
import org.apache.log4j.Logger;

/**
 *  This class is used to test drive the Fortress Delegated Access Control APIs.
 *
 *@author     smckinn
 *@created    December 4, 2010
 */
public class DelegatedAccessMgrConsole
{
	private DelegatedAccessMgr dAmgr = null;
    private Session session = null;
    private static final String OCLS_NM = DelegatedAccessMgrConsole.class.getName();
    private static final Logger log = Logger.getLogger(OCLS_NM);

	/**
	 * put your documentation comment here
	 */
	public DelegatedAccessMgrConsole()
	{
		try
		{
			dAmgr = DelegatedAccessMgrFactory.createInstance();
		}
		catch (SecurityException e)
		{
            log.error(OCLS_NM + " constructor caught SecurityException errCode=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
		}
	}

    /**
     *
     */
    protected void canAssign()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter userId:");
            User user = new User(ReaderUtil.readLn());
            System.out.println("Enter role name:");
            Role role = new Role(ReaderUtil.readLn());
            boolean result = dAmgr.canAssign(session, user, role);
            System.out.println("Can Assign User <" + user.getUserId() + "> Role <" + role.getName() + "> return <" + result + ">");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            log.error(OCLS_NM + ".canAssign caught SecurityException errCode=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    protected void canDeassign()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter userId:");
            User user = new User(ReaderUtil.readLn());
            System.out.println("Enter role name:");
            Role role = new Role(ReaderUtil.readLn());
            boolean result = dAmgr.canDeassign(session, user, role);
            System.out.println("Can Deassign User <" + user.getUserId() + "> Role <" + role.getName() + "> return <" + result + ">");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            log.error(OCLS_NM + ".canDeassign caught SecurityException errCode=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    protected void canGrant()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter role name:");
            Role role = new Role(ReaderUtil.readLn());
            System.out.println("Enter perm object name:");
            String objectName = ReaderUtil.readLn();
            boolean result = dAmgr.canGrant(session, role, new Permission(objectName));
            System.out.println("Can Assign Role <" + role.getName() + "> Object name <" + objectName + "> return <" + result + ">");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            log.error(OCLS_NM + ".canGrant caught SecurityException errCode=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    protected void canRevoke()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter role name:");
            Role role = new Role(ReaderUtil.readLn());
            System.out.println("Enter perm object name:");
            String objectName = ReaderUtil.readLn();
            boolean result = dAmgr.canRevoke(session, role, new Permission(objectName));
            System.out.println("Can Revoke Role <" + role.getName() + "> Object name <" + objectName + "> return <" + result + ">");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            log.error(OCLS_NM + ".canRevoke caught SecurityException errCode=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    protected void createSession()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter userId:");
            String userId = ReaderUtil.readLn();
            System.out.println("Enter password:");
            String password = ReaderUtil.readLn();
            session = new Session();
            //((AccessMgr)dAmgr).createSession(session, userId, password);
            ((AccessMgr)dAmgr).createSession(new User(userId, password.toCharArray()), false);
            System.out.println("Session created successfully for userId <" + userId + ">");
            System.out.println("session <" + session + ">");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            log.error(OCLS_NM + ".createSession caught SecurityException errCode=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    protected void createSessionTrusted()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter userId:");
            String userId = ReaderUtil.readLn();
            session = ((AccessMgr)dAmgr).createSession(new User(userId), true);
            System.out.println("Trusted Session created successfully for userId <" + userId + ">");
            System.out.println("session <" + session + ">");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            log.error(OCLS_NM + ".createSessionTrusted caught SecurityException errCode=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }



    protected void checkAccess()
    {
        try
        {
            VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, OCLS_NM + ".checkAccess");
            ReaderUtil.clearScreen();
            System.out.println("Enter object name:");
            String objName = ReaderUtil.readLn();
            System.out.println("Enter operation name:");
            String opName = ReaderUtil.readLn();
            boolean result = dAmgr.checkAccess(session, new Permission(objName, opName));
            System.out.println("CheckAccess return <" + result + "> for user <" + session.getUserId() + "> objectName <" + objName + "> operationName <" + opName + ">");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            log.error(OCLS_NM + ".checkAccess caught SecurityException errCode=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

}