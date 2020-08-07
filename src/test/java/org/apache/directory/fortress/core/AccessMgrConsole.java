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

import java.io.Console;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.RoleConstraint;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserAdminRole;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.util.VUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * * Test class for driving Fortress RBAC runtime policy APIs within a console.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
class AccessMgrConsole
{
    private AccessMgr am = null;
    private Session session = null;
    private static final String CLS_NM = AccessMgrConsole.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    /**
     * put your documentation comment here
     */
    public AccessMgrConsole()
    {
        try
        {
            am = AccessMgrFactory.createInstance( TestUtils.getContext() );
        }
        catch ( org.apache.directory.fortress.core.SecurityException e)
        {
            LOG.error("constructor caught SecurityException  rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
    }


    void sessionPermissions()
    {
        try
        {
            VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, "AccessMgrConsole.sessionPermissions");
            ReaderUtil.clearScreen();
            //List<UserRole> roles = session.getRoles();
            List<Permission> list = am.sessionPermissions(session);

            //List list = rm.findPermissions(pe);
            if (list != null)
            {
                Collections.sort( list, new Comparator<Permission>()
                {
                    @Override
                    public int compare(Permission p1, Permission p2)
                    {
                        return p1.getAbstractName().compareTo( p2.getAbstractName() );
                    }
                } );
                int i = 0;
                for (Permission pe : list)
                {
                    //pe = (Permission) list.get(i);
                    System.out.println("**perm:" + (i++) + "***");
                    System.out.println("object name [" + pe.getObjName() + "]");
                    System.out.println("object id [" + pe.getObjId() + "]");
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
                }
            }
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("sessionPermissions caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     * Description of the Method
     */
    void authenticate()
    {
        Session session;

        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter userId:");
            String userId = ReaderUtil.readLn();
            Console console = System.console();
            System.out.println("Enter password:");
            String password = new String ( console.readPassword() );
            session = am.authenticate(userId, password);
            System.out.println("Authentication successful for userId [" + userId + "]");
            System.out.println("session [" + session + "]");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("authenticate caught SecurityException=" + e, e);
        }
        ReaderUtil.readChar();
    }


    void createSession()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter userId:");
            String userId = ReaderUtil.readLn();
            System.out.println("Enter password:");
            Console console = System.console();
            String password = new String ( console.readPassword() );

            User inUser = new User(userId, password);
            List<RoleConstraint> constraints = new ArrayList();
            RoleConstraint constraint = getRuntimeConstraint( inUser );
            while ( constraint != null )
            {
                constraints.add( constraint );
                constraint = getRuntimeConstraint( inUser );
            }
            session = am.createSession(inUser, constraints, false);
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
            User inUser = new User(userId);
            List<RoleConstraint> constraints = new ArrayList();
            RoleConstraint constraint = getRuntimeConstraint( inUser );
            while ( constraint != null )
            {
                constraints.add( constraint );
                constraint = getRuntimeConstraint( inUser );
            }
            session = am.createSession(inUser, constraints, true);
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


    private RoleConstraint getRuntimeConstraint(User user)
    {
        RoleConstraint constraint = null;
        System.out.println("Do you want to set a runtime constrait on user role activation? - Y or NULL to skip");
        String choice = ReaderUtil.readLn();
        if (choice != null && choice.equalsIgnoreCase("Y"))
        {
            constraint = new RoleConstraint();
            System.out.println("Enter constraint type):");
            constraint.setKey( ReaderUtil.readLn() );
            System.out.println( "Enter constraint value):" );
            constraint.setValue( ReaderUtil.readLn() );
        }
        return constraint;
    }

    /**
     *
     */
    void createSessionProps()
    {
        try
        {
            boolean isTrusted = false;
            User user = new User();
            ReaderUtil.clearScreen();
            System.out.println("Enter userId:");
            user.setUserId(ReaderUtil.readLn());
            System.out.println("Is trusted? Y or N");
            String val = ReaderUtil.readLn();
            if (val.equalsIgnoreCase("Y"))
            {
                isTrusted = true;
            }
            else
            {
                System.out.println("Enter password:");
                user.setPassword(ReaderUtil.readLn());
            }
            System.out.println("Enter prop key (or NULL to skip):");
            String key = ReaderUtil.readLn();
            for (int i = 0; key != null && key.length() > 0; i++)
            {
                System.out.println("Enter prop val:");
                val = ReaderUtil.readLn();
                user.addProperty(key, val);
                System.out.println("Enter next prop key (or NULL if done entering properties)");
                key = ReaderUtil.readLn();
            }

            session = am.createSession(user, isTrusted);
            System.out.println("Session created successfully for userId [" + user.getUserId() + "]");
            System.out.println("session id [" + session.getSessionId() + "]");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("createSessionProps caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    void createSessionRolesTrusted()
    {
        try
        {
            User user = new User();
            ReaderUtil.clearScreen();
            System.out.println( "Enter userId:" );
            user.setUserId( ReaderUtil.readLn() );
            System.out.println("Enter role (or NULL to skip):");
            String key = ReaderUtil.readLn();
            for (int i = 0; key != null && key.length() > 0; i++)
            {
                user.setRole( new UserRole( key ) );
                System.out.println("Enter next role (or NULL if done entering roles)");
                key = ReaderUtil.readLn();
            }

            session = am.createSession(user, true);
            System.out.println("Session created successfully for userId [" + user.getUserId() + "]");
            System.out.println("session id [" + session.getSessionId() + "]");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("createSessionProps caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
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
            VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, "AccessMgrConsole.checkAccess");
            ReaderUtil.clearScreen();
            Permission perm = new Permission();
            System.out.println( "Enter object name:" );
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

    void createSessionCheckAccess()
    {
        //Session session = null;
        try
        {
            Permission perm = new Permission();
            System.out.println( "Enter object name:" );
            perm.setObjName( ReaderUtil.readLn() );
            System.out.println("Enter operation name:");
            perm.setOpName( ReaderUtil.readLn() );
            System.out.println("Enter object id (or NULL to skip):");
            String val = ReaderUtil.readLn();
            if ( val != null && val.length() > 0 )
            {
                perm.setObjId( val );
            }

            System.out.println("Enter userId:");
            String userId = ReaderUtil.readLn();
            User inUser = new User(userId);
            inUser.addProperties( getRuntimeProps() );
            boolean result = am.checkAccess( inUser, perm, true );
            System.out.println("createSessionCheckAccess return [" + result + "] for user [" + userId + "], objName [" + perm.getObjName() + "], operationName [" + perm.getOpName() + "]" +
                ", objId [" + perm.getObjId() + "]");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("createSessionCheckAccess caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    void isUserInRole()
    {
        //Session session = null;
        try
        {
            Role role = new Role();
            System.out.println("Enter role name:");
            role.setName( ReaderUtil.readLn() );

            System.out.println("Enter userId:");
            String userId = ReaderUtil.readLn();
            User inUser = new User(userId);
            inUser.addProperties( getRuntimeProps() );
            boolean result = am.isUserInRole( inUser, role, true );
            System.out.println("isUserInRole return [" + result + "] for user [" + userId + "], role [" + role.getName() + "]");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("isUserInRole caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    private Properties getRuntimeProps()
    {
        Properties props = new Properties(  );
        System.out.println("Do you want to set a runtime constrait on user role activation? - Y or NULL to skip");
        String choice = ReaderUtil.readLn();
        if (choice != null && choice.equalsIgnoreCase("Y"))
        {
            System.out.println("Enter constraint type):");
            String key = ReaderUtil.readLn();
            System.out.println( "Enter constraint value):" );
            String value = ReaderUtil.readLn();
            props.setProperty( key, value );
        }
        return props;
    }

    void sessionRoles()
    {
        try
        {
            VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, "AccessMgrConsole.sessionRoles");
            ReaderUtil.clearScreen();
            List<UserRole> roles = session.getRoles();
            System.out.println("    USER [" + session.getUserId() + "]:");
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
            List<UserAdminRole> aRoles = session.getAdminRoles();
            if (aRoles != null)
            {
                for (int i = 0; i < aRoles.size(); i++)
                {
                    UserAdminRole ur = aRoles.get(i);
                    System.out.println("    USER ADMIN ROLE[" + i + "]:");
                    System.out.println("        admin role name [" + ur.getName() + "]");
                    System.out.println("        OsU [" + ur.getOsUSet() + "]");
                    System.out.println("        OsP [" + ur.getOsPSet() + "]");
                    System.out.println("        begin range [" + ur.getBeginRange() + "]");
                    System.out.println("        end range [" + ur.getEndRange() + "]");
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
            VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, "AccessMgrConsole.addActiveRole");
            ReaderUtil.clearScreen();
            System.out.println("Enter role name");
            String role = ReaderUtil.readLn();
            am.addActiveRole(session, new UserRole(role));
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
            VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, "AccessMgrConsole.dropActiveRole");
            ReaderUtil.clearScreen();
            System.out.println("Enter role name");
            String role = ReaderUtil.readLn();
            am.dropActiveRole(session, new UserRole(role));
            System.out.println("dropActiveRole successful");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("dropActiveRole caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    /**
     * Sample User data contained in Session object:
     * <p>
     * S   UID  [demoUser4]:<br>
     * S   IID  [ccbb2929-bf01-413d-b768-529de4d428e5]<br>
     * S   ERR  [0]<br>
     * S   WARN [10]<br>
     * S   MSG  [checkPwPolicies for userId <demouser4> PASSWORD CHECK SUCCESS]<br>
     * S   EXP  [0]<br>
     * S   GRAC [0]<br>
     * S   AUTH [true]<br>
     * S   LAST [1297408501356]<br>
     * S   SID  [fc228713-1242-4061-9d8a-d4860bf8d3d8]<br>
     * ------------------------------------------<br>
     * U   UID  [demoUser4]<br>
     * U   IID  [ccbb2929-bf01-413d-b768-529de4d428e5]<br>
     * U   CN   [JoeUser4]<br>
     * U   DESC [Demo Test User 4]<br>
     * U   OU   [demousrs1]<br>
     * U   SN   [User4]<br>
     * U   BDTE [20090101]<br>
     * U   EDTE [20990101]<br>
     * U   BLDT [none]<br>
     * U   ELDT [none]<br>
     * U   DMSK [1234567]<br>
     * U   TO   [60]<br>
     * U   REST [false]<br>
     * U   PROP[0]=customerNumber VAL=3213432<br>
     * <p>
     * USER ROLE[0]:<br>
     * role name <role1><br>
     * begin time <0000><br>
     * end time <0000><br>
     * begin date <none><br>
     * end date <none><br>
     * begin lock <none><br>
     * end lock <none><br>
     * day mask <all><br>
     * time out <0><br>
     * <p>
     * USER ADMIN ROLE[0]:<br>
     * admin role name <DemoAdminUsers><br>
     * OsU <null><br>
     * OsP <null><br>
     * begin range <null><br>
     * end range <null><br>
     * begin time <0000><br>
     * end time <0000><br>
     * begin date <none><br>
     * end date <none><br>
     * begin lock <none><br>
     * end lock <none><br>
     * day mask <all><br>
     * time out <0><br>
     */
    void getUser()
    {
        try
        {
            VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, "AccessMgrConsole.getUser");
            ReaderUtil.clearScreen();
            User user = am.getUser(session);
            System.out.println("S   UID  [" + session.getUserId() + "]:");
            System.out.println("S   IID  [" + session.getInternalUserId() + "]");
            System.out.println("S   ERR  [" + session.getErrorId() + "]");
            System.out.println("S   WARN [" + session.getWarnings() + "]");
            System.out.println("S   MSG  [" + session.getMsg() + "]");
            System.out.println("S   EXP  [" + session.getExpirationSeconds() + "]");
            System.out.println("S   GRAC [" + session.getGraceLogins() + "]");
            System.out.println("S   AUTH [" + session.isAuthenticated() + "]");
            System.out.println("S   LAST [" + session.getLastAccess() + "]");
            System.out.println("S   SID  [" + session.getSessionId() + "]");
            System.out.println("------------------------------------------");
            System.out.println("U   UID  [" + user.getUserId() + "]");
            System.out.println("U   IID  [" + user.getInternalId() + "]");
            System.out.println("U   CN   [" + user.getCn() + "]");
            System.out.println("U   DESC [" + user.getDescription() + "]");
            System.out.println("U   OU   [" + user.getOu() + "]");
            System.out.println("U   SN   [" + user.getSn() + "]");
            System.out.println("U   BDTE [" + user.getBeginDate() + "]");
            System.out.println("U   EDTE [" + user.getEndDate() + "]");
            System.out.println("U   BLDT [" + user.getBeginLockDate() + "]");
            System.out.println("U   ELDT [" + user.getEndLockDate() + "]");
            System.out.println("U   DMSK [" + user.getDayMask() + "]");
            System.out.println("U   TO   [" + user.getTimeout() + "]");
            System.out.println("U   REST [" + user.isReset() + "]");
            if (user.getProperties() != null && user.getProperties().size() > 0)
            {
                int ctr = 0;
                for (Enumeration e = user.getProperties().propertyNames(); e.hasMoreElements();)
                {
                    String key = (String) e.nextElement();
                    String val = user.getProperty(key);
                    System.out.println("U   PROP[" + ctr++ + "]=" + key + " VAL=" + val);
                }
            }

            List<UserRole> roles = session.getRoles();
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

            List<UserAdminRole> aRoles = session.getAdminRoles();
            if (aRoles != null)
            {
                for (int i = 0; i < aRoles.size(); i++)
                {
                    UserAdminRole ur = aRoles.get(i);
                    System.out.println("    USER ADMIN ROLE[" + i + "]:");
                    System.out.println("        admin role name [" + ur.getName() + "]");
                    System.out.println("        OsU [" + ur.getOsUSet() + "]");
                    System.out.println("        OsP [" + ur.getOsPSet() + "]");
                    System.out.println("        begin range [" + ur.getBeginRange() + "]");
                    System.out.println("        end range [" + ur.getEndRange() + "]");
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
            LOG.error("getUser caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    void getUserId()
    {
        try
        {
            VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, "AccessMgrConsole.getUserId");
            ReaderUtil.clearScreen();
            String userId = am.getUserId(session);
            System.out.println("S   UID  [" + userId + "]:");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("getUserId caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


}


