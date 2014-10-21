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
package org.openldap.fortress;

import org.openldap.fortress.rbac.Permission;
import org.openldap.fortress.rbac.Role;
import org.openldap.fortress.rbac.TestUtils;
import org.openldap.fortress.rbac.User;
import org.openldap.fortress.rbac.Session;
import org.openldap.fortress.util.attr.VUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *  This class is used to test drive the Fortress Delegated Access Control APIs.
 *
 *@author     Shawn McKinney
 */
class DelegatedAccessMgrConsole
{
	private DelAccessMgr dAmgr = null;
    private Session session = null;
    private static final String CLS_NM = DelegatedAccessMgrConsole.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

	/**
	 * put your documentation comment here
	 */
	public DelegatedAccessMgrConsole()
	{
		try
		{
			dAmgr = DelAccessMgrFactory.createInstance(TestUtils.getContext());
		}
		catch (SecurityException e)
		{
            LOG.error(" constructor caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
		}
	}

    /**
     *
     */
    void canAssign()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter userId:");
            User user = new User(ReaderUtil.readLn());
            System.out.println("Enter role name:");
            Role role = new Role(ReaderUtil.readLn());
            boolean result = dAmgr.canAssign(session, user, role);
            System.out.println("Can Assign User [" + user.getUserId() + "] Role [" + role.getName() + "] return [" + result + "]");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("canAssign caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    void canDeassign()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter userId:");
            User user = new User(ReaderUtil.readLn());
            System.out.println("Enter role name:");
            Role role = new Role(ReaderUtil.readLn());
            boolean result = dAmgr.canDeassign(session, user, role);
            System.out.println("Can Deassign User [" + user.getUserId() + "] Role [" + role.getName() + "] return [" + result + "]");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("canDeassign caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    void canGrant()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter role name:");
            Role role = new Role(ReaderUtil.readLn());
            System.out.println("Enter perm object name:");
            String objName = ReaderUtil.readLn();
            boolean result = dAmgr.canGrant(session, role, new Permission(objName));
            System.out.println("Can Assign Role [" + role.getName() + "] Object name [" + objName + "] return [" + result + "]");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("canGrant caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    void canRevoke()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter role name:");
            Role role = new Role(ReaderUtil.readLn());
            System.out.println("Enter perm object name:");
            String objName = ReaderUtil.readLn();
            boolean result = dAmgr.canRevoke(session, role, new Permission(objName));
            System.out.println("Can Revoke Role [" + role.getName() + "] Object name [" + objName + "] return [" + result + "]");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("canRevoke caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    void createSession()
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
            System.out.println("Session created successfully for userId [" + userId + "]");
            System.out.println("session [" + session + "]");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("createSession caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    void createSessionTrusted()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter userId:");
            String userId = ReaderUtil.readLn();
            session = ((AccessMgr)dAmgr).createSession(new User(userId), true);
            System.out.println("Trusted Session created successfully for userId [" + userId + "]");
            System.out.println("session [" + session + "]");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("createSessionTrusted caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }



    void checkAccess()
    {
        try
        {
            VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, ".checkAccess");
            ReaderUtil.clearScreen();
            System.out.println("Enter object name:");
            String objName = ReaderUtil.readLn();
            System.out.println("Enter operation name:");
            String opName = ReaderUtil.readLn();
            boolean result = dAmgr.checkAccess(session, new Permission(objName, opName));
            System.out.println("CheckAccess return [" + result + "] for user [" + session.getUserId() + "] objName [" + objName + "] operationName [" + opName + "]");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("checkAccess caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

}