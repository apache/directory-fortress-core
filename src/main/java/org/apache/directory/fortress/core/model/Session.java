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
package org.apache.directory.fortress.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This contains attributes related to a user's RBAC session.
 * The following example shows the mapping to Session attributes on this entity:
 * <p>
 * <ul> 
 *   <li><code>Session</code></li>
 *   <li><code>session.getUserId() =&gt; demoUser4</code></li>
 *   <li><code>session.getInternalUserId() =&gt; be2dd2e:12a82ba707e:-7fee</code></li>
 *   <li><code>session.getMessage() =&gt; Fortress checkPwPolicies userId &lt;demouser4&gt; VALIDATION GOOD</code></li>
 *   <li><code>session.getErrorId() =&gt; 0</code></li>
 *   <li><code>session.getWarningId() =&gt; 11</code></li>
 *   <li><code>session.getExpirationSeconds() =&gt; 469831</code></li>
 *   <li><code>session.getGraceLogins() =&gt; 0</code></li>
 *   <li><code>session.getIsAuthenticated() =&gt; true</code></li>
 *   <li><code>session.getLastAccess() =&gt; 1283623680440</code></li>
 *   <li><code>session.getSessionId() =&gt; -7410986f:12addeea576:-7fff</code></li>
 *   <li>------------------------------------------
 *   <li><code>User user = session.getUser();</code>
 *   <li>
 *     <ul>
 *       <li><code>user.getUserId() =&gt; demoUser4</code></li>
 *       <li><code>user.getInternalId() =&gt; be2dd2e:12a82ba707e:-7fee</code></li>
 *       <li><code>user.getCn() =&gt; JoeUser4</code></li>
 *       <li><code>user.getDescription() =&gt; Demo Test User 4</code></li>
 *       <li><code>user.getOu() =&gt; test</code></li>
 *       <li><code>user.getSn() =&gt; User4</code></li>
 *       <li><code>user.getBeginDate() =&gt; 20090101</code></li>
 *       <li><code>user.getEndDate() =&gt; none</code></li>
 *       <li><code>user.getBeginLockDate() =&gt; none</code></li>
 *       <li><code>user.getEndLockDate() =&gt; none</code></li>
 *       <li><code>user.getDayMask() =&gt; 1234567</code></li>
 *       <li><code>user.getTimeout() =&gt; 60</code></li>
 *       <li><code>List&lt;UserRole&gt; roles = session.getRoles();</code></li>
 *     </ul>
 *   </li>
 *   <li>
 *     <ul> 
 *       <li><code>UserRole userRole = roles.get(i);</code></li>
 *       <li><code>userRole.getName() =&gt; role1</code></li>
 *       <li><code>userRole.getBeginTime() =&gt; 0000</code></li>
 *       <li><code>userRole.getEndTime() =&gt; 0000</code></li>
 *       <li><code>userRole.getBeginDate() =&gt; none</code></li>
 *       <li><code>userRole.getEndDate() =&gt; none</code></li>
 *       <li><code>userRole.getBeginLockDate() =&gt; null</code></li>
 *       <li><code>userRole.getEndLockDate() =&gt; null</code></li>
 *       <li><code>userRole.getDayMask() =&gt; null</code></li>
 *       <li><code>userRole.getTimeout() =&gt; 0</code></li>
 *       <li><code>List&lt;UserAdminRole&gt; adminRoles = session.getAdminRoles();</code></li>
 *     </ul>
 *   </li>
 *   <li>
 *     <ul>
 *       <li><code>UserAdminRole userAdminRole = adminRoles.get(i);</code></li>
 *       <li><code>userAdminRole.getName() =&gt; DemoAdminUsers</code></li>
 *       <li><code>userAdminRole.getBeginTime() =&gt; 0000</code></li>
 *       <li><code>userAdminRole.getEndTime() =&gt; 0000</code></li>
 *       <li><code>userAdminRole.getBeginDate() =&gt; none</code></li>
 *       <li><code>userAdminRole.getEndDate() =&gt; none</code></li>
 *       <li><code>userAdminRole.getBeginLockDate() =&gt; null</code></li>
 *       <li><code>userAdminRole.getEndLockDate() =&gt; null</code></li>
 *       <li><code>userAdminRole.getDayMask() =&gt; null</code></li>
 *       <li><code>userAdminRole.getTimeout() =&gt; 0</code></li>
 *       <li><code>userAdminRole.getOsPs() =&gt; [ftT3POrg10, ftT4POrg10]</code></li>
 *       <li><code>userAdminRole.getOsUs() =&gt; [ftT1UOrg10, ftT2UOrg10]</code></li>
 *       <li><code>userAdminRole.getBeginRange() =&gt; ftT14Role1</code></li>
 *       <li><code>userAdminRole.getEndRange() =&gt; ftT14Role10</code></li>
 *       <li><code>userAdminRole.getBeginInclusive() =&gt; true</code></li>
 *       <li><code>userAdminRole.getEndInclusive() =&gt; false</code></li>
 *     </ul>
 *   </li>
 * </ul>
 * <p>
 * 
 * Sample Data data contained within this Entity.
 * <p>
 * Ses UID      [demoUser4]:<br>
 * Ses IID      [ccbb2929-bf01-413d-b768-529de4d428e5]<br>
 * Ses ERR      [0]<br>
 * Ses WARN     [10]<br>
 * Ses MSG      [checkPwPolicies for userId &lt;demouser4&gt; PASSWORD CHECK SUCCESS]<br>
 * Ses EXP      [0]<br>
 * Ses GRAC     [0]<br>
 * Ses AUTH     [true]<br>
 * Ses LAST     [1297408501356]<br>
 * Ses SID      [fc228713-1242-4061-9d8a-d4860bf8d3d8]<br>
 * ------------------------------------------<br>
 * Usr UID      [demoUser4]<br>
 * Usr IID      [ccbb2929-bf01-413d-b768-529de4d428e5]<br>
 * Usr CN       [JoeUser4]<br>
 * Usr DESC     [Demo Test User 4]<br>
 * Usr OU       [demousrs1]<br>
 * Usr SN       [User4]<br>
 * Usr BDTE     [20090101]<br>
 * Usr EDTE     [20990101]<br>
 * Usr BLDT     [none]<br>
 * Usr ELDT     [none]<br>
 * Usr DMSK     [1234567]<br>
 * Usr TO       [60]<br>
 * Usr REST     [false]<br>
 * Usr PROP1    [customerNumber, 3213432]<br>
 * <p>
 * USER RBAC ROLE[0]:<br>
 * Rle  role name       [role1]<br>
 * Rle  begin time      [0000]<br>
 * Rle  end time        [0000]<br>
 * Rle  begin date      [20110101]<br>
 * Rle  end date        [none]<br>
 * Rle  begin lock      [none]<br>
 * Rle  end lock        [none]<br>
 * Rle  day mask        [all]<br>
 * Rle  time out        [60]<br>
 * <p>
 * USER ADMIN ROLE[0]:<br>
 * Adm  admin role name [DemoAdminUsers]<br>
 * Adm  OsU             [Dev1]<br>
 * Adm  OsP             [App1]<br>
 * Adm  begin range     [role1]<br>
 * Adm  end range       [role3]<br>
 * Adm  begin time      [0000]<br>
 * Adm  end time        [0000]<br>
 * Adm  begin date      [20110101]<br>
 * Adm  end date        [none]<br>
 * Adm  begin lock      [none]<br>
 * Adm  end lock        [none]<br>
 * Adm  day mask        [23456]<br>
 * Adm  time out        [30]<br>
 * <p>
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "fortSession")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "session", propOrder = {
    "user",
    "group",
    "isAuthenticated",
    "isGroupSession",
    "sessionId",
    "lastAccess",
    "timeout",
    "errorId",
    "expirationSeconds",
    "graceLogins",
    "message",
    "warnings"
})
public class Session  extends FortEntity implements PwMessage, Serializable
{
    private static final long serialVersionUID = 1L;
    private User user;
    private Group group;
    private String sessionId;
    private long lastAccess;
    private int timeout;
    private int errorId;
    private int graceLogins;
    private int expirationSeconds;
    private boolean isAuthenticated;
    private boolean isGroupSession;
    private String message;
    private List<Warning> warnings;

    /**
     * A 'true' value here indicates user successfully authenticated with Fortress.
     *
     * @return boolean indicating successful authentication.
     */
    public boolean isAuthenticated()
    {
        return isAuthenticated;
    }

    /**
     * A 'true' value here indicates this Session was created for Group entity
     *
     * @return boolean indicating if this Session is created for Group
     */
    public boolean isGroupSession()
    {
        return isGroupSession;
    }
    

    private void init()
    {
        // generate a unique id that will be used as the id for this session:
        UUID uuid = UUID.randomUUID();
        this.sessionId = uuid.toString();
    }
    

    /**
     * Copy values from incoming Session object.
     *
     * @param inSession contains Session values.
     */
    public void copy( Session inSession )
    {
        this.user = inSession.getUser();
        this.group = inSession.getGroup();
        // don't copy session id:
        //this.sessionId = inSession.getSessionId();
        this.lastAccess = inSession.getLastAccess();
        this.timeout = inSession.getTimeout();
        this.errorId = inSession.getErrorId();
        this.graceLogins = inSession.getGraceLogins();
        this.expirationSeconds = inSession.expirationSeconds;
        this.isAuthenticated = inSession.isAuthenticated();
        this.isGroupSession = inSession.isGroupSession();
        this.message = inSession.getMsg();
        this.warnings = inSession.getWarnings();
    }
    

    /**
     * Default constructor for Fortress Session.
     */
    public Session()
    {
        init();
        // this class will not check for null on user object.
        user = new User();
        // by default, the Session is created for user
        isGroupSession = false;
    }
    

    /**
     * Construct a new Session instance with given User entity.
     *
     * @param user contains the User attributes that are associated with the Session.
     */
    public Session( User user )
    {
        init();
        this.user = user;
        isGroupSession = false;
    }

    /**
     * Construct a new Session instance with given Group entity.
     *
     * @param group contains the Group attributes that are associated with the Session.
     */
    public Session( Group group )
    {
        init();
        this.group = group;
        isGroupSession = true;
    }
    

    /**
     * Construct a new Session instance with given User entity.
     *
     * @param user contains the User attributes that are associated with the Session.
     */
    public Session (User user, String sessionId )
    {
        this.sessionId = sessionId;
        this.user = user;
        isGroupSession = false;
    }

    /**
     * Construct a new Session instance with given Group entity.
     *
     * @param group contains the Group attributes that are associated with the Session.
     */
    public Session( Group group, String sessionId )
    {
        this.sessionId = sessionId;
        this.group = group;
        isGroupSession = true;
    }


    /**
     * Return the unique id that is associated with User.  This attribute is generated automatically
     * by Fortress when new Session is created and is not known or changeable by external client.
     *
     * @return attribute maps to unique sessionId associated with user's session.
     */
    public String getSessionId()
    {
        return sessionId;
    }


    /**
     * Return the User entity that is associated with this entity.
     *
     * Sample User data contained in Session object:
     * <p>
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
     * role name &lt;role1&gt;<br>
     * begin time &lt;0000&gt;<br>
     * end time &lt;0000&gt;<br>
     * begin date &lt;none&gt;<br>
     * end date &lt;none&gt;<br>
     * begin lock &lt;none&gt;<br>
     * end lock &lt;none&gt;<br>
     * day mask &lt;all&gt;<br>
     * time out &lt;0&gt;<br>
     * <p>
     * USER ADMIN ROLE[0]:<br>
     * admin role name &lt;DemoAdminUsers&gt;<br>
     * OsU &lt;null&gt;<br>
     * OsP &lt;null&gt;<br>
     * begin range &lt;null&gt;<br>
     * end range &lt;null&gt;<br>
     * begin time &lt;0000&gt;<br>
     * end time &lt;0000&gt;<br>
     * begin date &lt;none&gt;<br>
     * end date &lt;none&gt;<br>
     * begin lock &lt;none&gt;<br>
     * end lock &lt;none&gt;<br>
     * day mask &lt;all&gt;<br>
     * time out &lt;0&gt;<br>
     * <p>
     * @return User entity that contains userid, roles and other attributes valid for Session.
     */
    public User getUser()
    {
        return this.user;
    }

    /**
     * Return the Group entity that is associated with this entity.
     */
    public Group getGroup()    { return this.group; }


    /**
     * Return the userId that is associated with this Session object.
     *
     * @return userId maps to the 'uid' attribute on the 'inetOrgPerson' object class.
     */
    public String getUserId()
    {
        return this.user.getUserId();
    }

    /**
     * Return the group name that is associated with this Session object.
     *
     * @return group name maps to the 'name' attribute on the 'ftGroup' object class.
     */
    public String getGroupName()
    {
        return this.group.getName();
    }
    

    /**
     * Return the internal userId that is associated with User.  This attribute is generated automatically
     * by Fortress when new User is added to directory and is not known or changeable by external client.
     *
     * @return attribute maps to 'ftId' in 'ftUserAttrs' object class.
     */
    public String getInternalUserId()
    {
        return this.user.getInternalId();
    }


    /**
     * Return the list of User's RBAC Roles that have been activated into User's or Group's session.  This list will not include
     * ascendant RBAC roles which may be retrieved using {@link org.apache.directory.fortress.core.impl.AccessMgrImpl#authorizedRoles(Session)}.
     *
     * @return List containing User's RBAC roles.  This list may be empty if User not assigned RBAC.
     */
    public List<UserRole> getRoles()
    {
        List<UserRole> roles = null;

        if ( isGroupSession && group != null )
        {
            roles = group.getRoles();
        }
        if ( !isGroupSession && user != null )
        {
            roles = user.getRoles();
        }

        return roles;
    }
    

    /**
     * Return a list of User's Admin Roles  that have been activated into User's session.  This list will not include
     * ascendant ARBAC roles which may be retrieved using {@link org.apache.directory.fortress.core.DelAccessMgr#authorizedAdminRoles(Session)}.
     *
     * @return List containing User's Admin roles.  This list may be empty if User not assigned Administrative role.
     */
    public List<UserAdminRole> getAdminRoles()
    {
        List<UserAdminRole> roles = null;

//        TODO: Do we need admin roles for Group?
//        if ( isGroupSession && group != null )
//        {
//            roles = group.getAdminRoles();
//        }
        if ( !isGroupSession && user != null )
        {
            roles = user.getAdminRoles();
        }

        return roles;
    }

    /**
     * Returns the last access time in milliseconds. Note that while the unit of time of the return value is a millisecond,
     * the granularity of the value depends on the underlying operating system and may be larger. For example, many
     * operating systems measure time in units of tens of milliseconds.
     *
     * @return the difference, measured in milliseconds, between the last access time and midnight, January 1, 1970 UTC.
     */
    public long getLastAccess()
    {
        return lastAccess;
    }
    

    /**
     * Gets the message that is associated with the user's last authentication attempt.
     *
     * @return String contains text explaining result of user's last authentication.
     */
    public String getMsg()
    {
        return message;
    }
    

    /**
     * Gets the attribute that specifies the number of times an expired password can
     * be used to authenticate before failure.
     *
     * @return The number of logins the user has left before password fails.
     */
    public int getGraceLogins()
    {
        return graceLogins;
    }
    

    /**
     * This attribute specifies the maximum number of seconds before a
     * password is due to expire that expiration warning messages will be
     * returned to an authenticating user.
     * <p>
     * If this attribute is not present, or if the value is 0 no warnings
     * will be returned.  If not 0, the value must be smaller than the value
     * of the pwdMaxAge attribute.
     *
     * @return attribute is computed based on last time user has changed their password.
     */
    public int getExpirationSeconds()
    {
        return expirationSeconds;
    }
    

    /**
     * Get the integer timeout that contains max time ((in minutes)) that User's session may remain inactive.
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return int maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    private int getTimeout()
    {
        return timeout;
    }
    

    /**
     * Get the value that will be set to 'true' if user has successfully authenticated with Fortress for this Session.  This value is set by
     * the Fortress DAO object.
     *
     * @return value indicates result of authentication.
     */
    public boolean setAuthenticated()
    {
        return isAuthenticated;
    }
    

    /**
     * Return the error id that is associated with the password policy checks.  a '0' indicates no errors.
     * <ul>
     * <li> <code>INVALID_PASSWORD_MESSAGE = -10;</code>
     * <li> <code>GOOD = 0;</code>
     * <li> <code>PASSWORD_HAS_EXPIRED = 100;</code>
     * <li> <code>ACCOUNT_LOCKED = 101;</code>
     * <li> <code>CHANGE_AFTER_RESET = 102;</code>
     * <li> <code>NO_MODIFICATIONS = 103;</code>
     * <li> <code>MUST_SUPPLY_OLD = 104;</code>
     * <li> <code>INSUFFICIENT_QUALITY = 105;</code>
     * <li> <code>PASSWORD_TOO_SHORT = 106;</code>
     * <li> <code>PASSWORD_TOO_YOUNG = 107;</code>
     * <li> <code>HISTORY_VIOLATION = 108;</code>
     * <li> <code>ACCOUNT_LOCKED_CONSTRAINTS = 109;</code>
     * </ul>
     * <p>
     *
     * @return int contains the error id that was generated on the user's last authentication.
     */
    public int getErrorId()
    {
        return errorId;
    }

    
    /**
     * Set a User entity into the Session.
     * Sample User data contained in Session object:
     * <p>
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
     * role name &lt;role1&gt;<br>
     * begin time &lt;0000&gt;<br>
     * end time &lt;0000&gt;<br>
     * begin date &lt;none&gt;<br>
     * end date &lt;none&gt;<br>
     * begin lock &lt;none&gt;<br>
     * end lock &lt;none&gt;<br>
     * day mask &lt;all&gt;<br>
     * time out &lt;0&gt;<br>
     * <p>
     * USER ADMIN ROLE[0]:<br>
     * admin role name &lt;DemoAdminUsers&gt;<br>
     * OsU &lt;null&gt;<br>
     * OsP &lt;null&gt;<br>
     * begin range &lt;null&gt;<br>
     * end range &lt;null&gt;<br>
     * begin time &lt;0000&gt;<br>
     * end time &lt;0000&gt;<br>
     * begin date &lt;none&gt;<br>
     * end date &lt;none&gt;<br>
     * begin lock &lt;none&gt;<br>
     * end lock &lt;none&gt;<br>
     * day mask &lt;all&gt;<br>
     * time out &lt;0&gt;<br>
     * <p>
     * @param user Contains userId, roles and other security attributes used for access control.
     */
    public void setUser( User user )
    {
        this.user = user;
    }

    /**
     * Set a Group entity into the Session.
     * @param group Contains group name, roles members and other security attributes used for access control.
     */
    public void setGroup( Group group )
    {
        this.group = group;
    }
    

    /**
     * Set the internal userId that is associated with User.  This method is used by DAO class and
     * is generated automatically by Fortress.  Attribute stored in LDAP cannot be changed by external caller.
     * This method can be used by client for search purposes only.
     *
     * @param internalUserId maps to 'ftId' in 'ftUserAttrs' object class.
     */
    public void setInternalUserId( String internalUserId )
    {
        this.user.setInternalId( internalUserId );
    }
    

    /**
     * Set the value to 'true' indicating that user has successfully authenticated with Fortress.  This value is set by
     * the Fortress DAO object.
     *
     * @param authenticated indicates result of authentication.
     */
    public void setAuthenticated( boolean authenticated )
    {
        isAuthenticated = authenticated;
    }


    /**
     * Set the value to 'true' indicating that Session is created for Group entity
     * @param isGroupSession indicates if Session is for Group
     */
    public void setGroupSession(boolean isGroupSession) { this.isGroupSession = isGroupSession; }

    /**
     * Set the userId that is associated with User.  UserId is required attribute and must be set on add, update, delete, createSession, authenticate, etc..
     *
     * @param userId maps to 'uid' attribute in 'inNetOrgPerson' object class.
     */
    public void setUserId( String userId )
    {
        user.setUserId( userId );
    }

    /**
     * Set the groupName that is associated with Group.  GroupName is required attribute and must be set on add, update, delete, createSession, authenticate, etc..
     *
     * @param groupName maps to 'name' attribute in 'ftGroup' object class.
     */
    public void setGroupName ( String groupName )
    {
        group.setName( groupName );
    }


    /**
     * Add a list of RBAC Roles to this entity that have been activated into Session or are under consideration for activation.
     *
     * @param roles List of type UserRole that contains at minimum UserId or GroupName and Role name.
     */
    public void setRoles( List<UserRole> roles )
    {
        if ( isGroupSession )
        {
            group.setRoles( roles );
        }
        else
        {
            user.setRoles( roles );
        }
    }
    

    /**
     * Add a single user-role object to the list of UserRoles for User.
     *
     * @param role UserRole contains at least userId and role name (activation) and additional constraints (assignment)
     */
    public void setRole( UserRole role )
    {
        if ( isGroupSession )
        {
            group.getRoles().add( role );
        }
        else
        {
            user.setRole( role );
        }
    }
    

    /**
     * Set the integer timeout that contains max time ((in minutes)) that User's session may remain inactive.
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param timeout maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    private void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }
    

    /**
     * Set the last access time in milliseconds. Note that while the unit of time of the return value is a millisecond,
     * the granularity of the value depends on the underlying operating system and may be larger. For example, many
     * operating systems measure time in units of tens of milliseconds.
     */
    public void setLastAccess()
    {
        lastAccess = System.currentTimeMillis();
    }
    

    /**
     * Set the message that is associated with the user's last authentication attempt.
     *
     * @param message Contains text explaining result of user's last authentication.
     */
    public void setMsg( String message )
    {
        this.message = message;
    }
    

    /**
     * Set the error id that is associated with the password policy checks.  a '0' indicates no errors.
     * <ul>
     * <li> <code>INVALID_PASSWORD_MESSAGE = -10;</code>
     * <li> <code>GOOD = 0;</code>
     * <li> <code>PASSWORD_HAS_EXPIRED = 100;</code>
     * <li> <code>ACCOUNT_LOCKED = 101;</code>
     * <li> <code>CHANGE_AFTER_RESET = 102;</code>
     * <li> <code>NO_MODIFICATIONS = 103;</code>
     * <li> <code>MUST_SUPPLY_OLD = 104;</code>
     * <li> <code>INSUFFICIENT_QUALITY = 105;</code>
     * <li> <code>PASSWORD_TOO_SHORT = 106;</code>
     * <li> <code>PASSWORD_TOO_YOUNG = 107;</code>
     * <li> <code>HISTORY_VIOLATION = 108;</code>
     * <li> <code>ACCOUNT_LOCKED_CONSTRAINTS = 109;</code>
     * </ul>
     * <p>
     *
     * @param error contains the error id that was generated on the user's last authentication.
     */
    public void setErrorId( int error )
    {
        this.errorId = error;
    }
    

    /**
     * This attribute specifies the number of times an expired password can
     * be used to authenticate.
     *
     * @param grace The number of logins the user has left before password fails.
     */
    public void setGraceLogins( int grace )
    {
        this.graceLogins = grace;
    }
    

    /**
     * This attribute specifies the maximum number of seconds before a
     * password is due to expire that expiration warning messages will be
     * returned to an authenticating user.
     * <p>
     * If this attribute is not present, or if the value is 0 no warnings
     * will be returned.  If not 0, the value must be smaller than the value
     * of the pwdMaxAge attribute.
     *
     * @param expire attribute is computed based on last time user has changed their password.
     */
    public void setExpirationSeconds( int expire )
    {
        this.expirationSeconds = expire;
    }
    

    /**
     * Get the warnings attached to this Session.  Used for processing password policy scenarios, e.g.. password expiring message.
     *
     * @return null value, zero or more objects of type {@link Warning} will be returned.  Note: the caller of this method must ensure a not null condition before use.
     */
    public List<Warning> getWarnings()
    {
        return warnings;
    }
    

    /**
     * Set the warnings on this Session.  Used for processing password policy scenarios, e.g.. password expiring message.
     * Not intended for use outside of Fortress packages.
     *
     * @param warnings zero or more objects of type warning may be set on a Fortress session.
     */
    public void setWarnings( List<Warning> warnings )
    {
        this.warnings = warnings;
    }
    

    /**
     * Add a warning to the collection into Fortress Session object.  Used for processing password policy scenarios, e.g.. password expiring message.
     * Not intended for use outside of Fortress packages.
     *
     * @param warning one object of type warning will be added to Fortress session.
     */
    public void setWarning( Warning warning )
    {
        if ( warnings == null )
        {
            warnings = new ArrayList<Warning>();
        }
        
        this.warnings.add( warning );
    }


    /**
     * @see Object#toString()
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append( "Session object: \n" );

        sb.append( "    sessionId :" ).append( sessionId ).append( '\n' );
        if ( isGroupSession )
        {
            sb.append( "    group :" ).append( group ).append( '\n' );
        }
        else
        {
            sb.append( "    user :" ).append( user ).append( '\n' );
        }
        sb.append( "    isAuthenticated :" ).append( isAuthenticated ).append( '\n' );
        sb.append( "    lastAccess :" ).append( lastAccess ).append( '\n' );
        sb.append( "    timeout :" ).append( timeout ).append( '\n' );
        sb.append( "    graceLogins :" ).append( graceLogins ).append( '\n' );
        sb.append( "    expirationSeconds :" ).append( expirationSeconds ).append( '\n' );
        sb.append( "    errorId :" ).append( errorId ).append( '\n' );
        sb.append( "    message :" ).append( message ).append( '\n' );

        if ( warnings != null )
        {
            sb.append( "    warnings : " );

            boolean isFirst = true;

            for ( Warning warning : warnings )
            {
                if ( isFirst )
                {
                    isFirst = false;
                }
                else
                {
                    sb.append( ", " );
                }

                sb.append( warning );
            }

            sb.append( '\n' );
        }

        return sb.toString();
    }
}