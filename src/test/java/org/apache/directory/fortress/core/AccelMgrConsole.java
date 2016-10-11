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
package org.apache.directory.fortress.core;

import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;

import java.util.List;

import org.apache.directory.fortress.core.util.VUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * * Test class for driving Fortress RBAC runtime policy APIs within a console.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
class AccelMgrConsole
{
    private AccelMgr am = null;
    private Session session = null;
    private static final String CLS_NM = AccelMgrConsole.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    /**
     * put your documentation comment here
     */
    public AccelMgrConsole()
    {
        try
        {
            am = AccelMgrFactory.createInstance( TestUtils.getContext() );
        }
        catch ( org.apache.directory.fortress.core.SecurityException e)
        {
            LOG.error("constructor caught SecurityException  rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
    }


    void createSession()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter userId:");
            String userId = ReaderUtil.readLn();
            System.out.println("Enter password:");
            String password = ReaderUtil.readLn();
            session = am.createSession(new User(userId, password), false);
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

    /**
     *
     */
    void checkAccess()
    {
        //Session session = null;
        try
        {
            VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, "AccelMgrConsole.checkAccess");
            ReaderUtil.clearScreen();
            Permission perm = new Permission();
            System.out.println("Enter object name:");
            perm.setObjName( ReaderUtil.readLn() );
            System.out.println("Enter operation name:");
            perm.setOpName( ReaderUtil.readLn() );
            System.out.println("Enter object id (or NULL to skip):");
            String val = ReaderUtil.readLn();
            if ( val != null && val.length() > 0 )
            {
                perm.setObjId( val );
            }

            boolean result = am.checkAccess( session, perm );
            System.out.println("CheckAccess return [" + result + "] for user [" + session.getUserId() + "], objName [" + perm.getObjName() + "], operationName [" + perm.getOpName() + "]" +
                ", objId [" + perm.getObjId() + "]");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("checkAccess caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    void sessionRoles()
    {
        try
        {
            VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, "AccelMgrConsole.sessionRoles");
            ReaderUtil.clearScreen();
            List<UserRole> roles = am.sessionRoles( session );
            //System.out.println("    USER [" + session.getUserId() + "]:");
            if (roles != null)
            {
                for (int i = 0; i < roles.size(); i++)
                {
                    UserRole ur = roles.get(i);
                    System.out.println("    USER ROLE[" + i + "]:");
                    System.out.println("        role name [" + ur.getName() + "]");
                    System.out.println("        begin time [" + ur.getBeginTime() + "]");
                    System.out.println("        end time [" + ur.getEndTime() + "]");
                    System.out.println("        begin date [" + ur.getBeginDate() + "]");
                    System.out.println("        end date [" + ur.getEndDate() + "]");
                    System.out.println("        begin lock [" + ur.getBeginLockDate() + "]");
                    System.out.println("        end lock [" + ur.getEndLockDate() + "]");
                    System.out.println("        day mask [" + ur.getDayMask() + "]");
                    System.out.println("        time out [" + ur.getTimeout() + "]");
                }
            }

            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("sessionRoles caught SecurityException=" + e, e);
        }
        ReaderUtil.readChar();
    }


    void addActiveRole()
    {
        try
        {
            VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, "AccelMgrConsole.addActiveRole");
            ReaderUtil.clearScreen();
            System.out.println("Enter userId");
            String userId = ReaderUtil.readLn();
            ReaderUtil.clearScreen();
            System.out.println("Enter role name");
            String role = ReaderUtil.readLn();
            am.addActiveRole(session, new UserRole(userId, role));
            System.out.println("addActiveRole successful");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("addActiveRole caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    void dropActiveRole()
    {
        try
        {
            VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, "AccelMgrConsole.dropActiveRole");
            ReaderUtil.clearScreen();
            System.out.println("Enter userId");
            String userId = ReaderUtil.readLn();
            ReaderUtil.clearScreen();
            System.out.println("Enter role name");
            String role = ReaderUtil.readLn();
            am.dropActiveRole( session, new UserRole( userId, role ) );
            System.out.println("dropActiveRole successful");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("dropActiveRole caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    void deleteSession()
    {
        try
        {
            VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, "AccelMgrConsole.deleteSession");
            am.deleteSession( session );
            System.out.println("deleteSession successful");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("deleteSession caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

}


