/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.arbac.AdminRoleUtil;
import com.jts.fortress.arbac.UserAdminRole;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * This contains attributes related to a user's RBAC session.
 * The following example shows the mapping to Session attributes on this entity:
 * <p/>
 * <ul> <li><code>Session</code>
 * <li> <code>session.getUserId() => demoUser4</code>
 * <li> <code>session.getInternalUserId() => be2dd2e:12a82ba707e:-7fee</code>
 * <li> <code>session.getMessage() => Fortress checkPwPolicies userId <demouser4> VALIDATION GOOD</code>
 * <li> <code>session.getErrorId() => 0</code>
 * <li> <code>session.getWarningId() => 11</code>
 * <li> <code>session.getExpirationSeconds() => 469831</code>
 * <li> <code>session.getGraceLogins() => 0</code>
 * <li> <code>session.getIsAuthenticated() => true</code>
 * <li> <code>session.getLastAccess() => 1283623680440</code>
 * <li> <code>session.getSessionId() => -7410986f:12addeea576:-7fff</code>
 * <li>  ------------------------------------------
 * <li> <code>User user = session.getUser();</code>
 * <ul> <li> <code>user.getUserId() => demoUser4</code>
 * <li> <code>user.getInternalId() => be2dd2e:12a82ba707e:-7fee</code>
 * <li> <code>user.getCn() => JoeUser4</code>
 * <li> <code>user.getDescription() => Demo Test User 4</code>
 * <li> <code>user.getOu() => test</code>
 * <li> <code>user.getSn() => User4</code>
 * <li> <code>user.getBeginDate() => 20090101</code>
 * <li> <code>user.getEndDate() => none</code>
 * <li> <code>user.getBeginLockDate() => none</code>
 * <li> <code>user.getEndLockDate() => none</code>
 * <li> <code>user.getDayMask() => 1234567</code>
 * <li> <code>user.getTimeout() => 60</code>
 * <li> <code>List<UserRole> roles = session.getRoles();</code>
 * <ul> <li><code>UserRole userRole = roles.get(i);</code>
 * <li> <code>userRole.getName() => role1</code>
 * <li> <code>userRole.getBeginTime() => 0000</code>
 * <li> <code>userRole.getEndTime() => 0000</code>
 * <li> <code>userRole.getBeginDate() => none</code>
 * <li> <code>userRole.getEndDate() => none</code>
 * <li> <code>userRole.getBeginLockDate() => null</code>
 * <li> <code>userRole.getEndLockDate() => null</code>
 * <li> <code>userRole.getDayMask() => null</code>
 * <li> <code>userRole.getTimeout() => 0</code>
 * <li> <code>List<UserAdminRole> adminRoles = session.getAdminRoles();</code>
 * </ul>
 * <ul> <li><code>UserAdminRole userAdminRole = adminRoles.get(i);</code>
 * <li> <code>userAdminRole.getName() => DemoAdminUsers</code>
 * <li> <code>userAdminRole.getBeginTime() => 0000</code>
 * <li> <code>userAdminRole.getEndTime() => 0000</code>
 * <li> <code>userAdminRole.getBeginDate() => none</code>
 * <li> <code>userAdminRole.getEndDate() => none</code>
 * <li> <code>userAdminRole.getBeginLockDate() => null</code>
 * <li> <code>userAdminRole.getEndLockDate() => null</code>
 * <li> <code>userAdminRole.getDayMask() => null</code>
 * <li> <code>userAdminRole.getTimeout() => 0</code>
 * <li> <code>userAdminRole.getOsPs() => [ftT3POrg10, ftT4POrg10]</code>
 * <li> <code>userAdminRole.getOsUs() => [ftT1UOrg10, ftT2UOrg10]</code>
 * <li> <code>userAdminRole.getBeginRange() => ftT14Role1</code>
 * <li> <code>userAdminRole.getEndRange() => ftT14Role10</code>
 * <li> <code>userAdminRole.getBeginInclusive() => true</code>
 * <li> <code>userAdminRole.getEndInclusive() => false</code>
 * </ul>
 * </ul>
 * <p/>
 * Sample Data data contained within this Entity.
 * <p/>
 * Ses UID      [demoUser4]:<br />
 * Ses IID      [ccbb2929-bf01-413d-b768-529de4d428e5]<br />
 * Ses ERR      [0]<br />
 * Ses WARN     [10]<br />
 * Ses MSG      [checkPwPolicies for userId <demouser4> PASSWORD CHECK SUCCESS]<br />
 * Ses EXP      [0]<br />
 * Ses GRAC     [0]<br />
 * Ses AUTH     [true]<br />
 * Ses LAST     [1297408501356]<br />
 * Ses SID      [fc228713-1242-4061-9d8a-d4860bf8d3d8]<br />
 * ------------------------------------------<br />
 * Usr UID      [demoUser4]<br />
 * Usr IID      [ccbb2929-bf01-413d-b768-529de4d428e5]<br />
 * Usr CN       [JoeUser4]<br />
 * Usr DESC     [Demo Test User 4]<br />
 * Usr OU       [demousrs1]<br />
 * Usr SN       [User4]<br />
 * Usr BDTE     [20090101]<br />
 * Usr EDTE     [20990101]<br />
 * Usr BLDT     [none]<br />
 * Usr ELDT     [none]<br />
 * Usr DMSK     [1234567]<br />
 * Usr TO       [60]<br />
 * Usr REST     [false]<br />
 * Usr PROP1    [customerNumber, 3213432]<br />
 * <p/>
 * USER RBAC ROLE[0]:<br />
 * Rle  role name       [role1]<br />
 * Rle  begin time      [0000]<br />
 * Rle  end time        [0000]<br />
 * Rle  begin date      [20110101]<br />
 * Rle  end date        [none]<br />
 * Rle  begin lock      [none]<br />
 * Rle  end lock        [none]<br />
 * Rle  day mask        [all]<br />
 * Rle  time out        [60]<br />
 * <p/>
 * USER ADMIN ROLE[0]:<br />
 * Adm  admin role name [DemoAdminUsers]<br />
 * Adm  OsU             [Dev1]<br />
 * Adm  OsP             [App1]<br />
 * Adm  begin range     [role1]<br />
 * Adm  end range       [role3]<br />
 * Adm  begin time      [0000]<br />
 * Adm  end time        [0000]<br />
 * Adm  begin date      [20110101]<br />
 * Adm  end date        [none]<br />
 * Adm  begin lock      [none]<br />
 * Adm  end lock        [none]<br />
 * Adm  day mask        [23456]<br />
 * Adm  time out        [30]<br />
 * <p/>

 * @author smckinn
 * @created October 13, 2009
 */
public class Session implements com.jts.fortress.pwpolicy.PwMessage, java.io.Serializable
{
    //private ArrayList roles;
    private User user;
    private String sessionId;
    private long lastAccess;
    private int timeout;
    private int warningId;
    private int errorId;
    private int graceLogins;
    private int expirationSeconds;
    private boolean isAuthenticated;
    private String message;

    /**
     * A 'true' value here indicates user successfully authenticated with Fortress.
     *
     * @return boolean indicating successful authentication.
     */
    public boolean isAuthenticated()
    {
        return isAuthenticated;
    }

    private void init()
    {
        // generate a unique id that will be used as the id for this session:
        UUID uuid = UUID.randomUUID();
        this.sessionId = uuid.toString();
    }

    /**
     * Default constructor for Fortress Session.
     */
    public Session()
    {
        init();
        // this class will not check for null on user object.
        this.user = new User();
    }

    /**
     * Construct a new Session instance with given User entity.
     *
     * @param user contains the User attributes that are associated with the Session.
     */
    public Session(User user)
    {
        init();
        this.user = user;
    }

    /**
     * Return the unique id that is associated with User.  This attribute is generated automatically
     * by Fortress when new Session is created and is not known or changeable by external client.
     *
     * @return attribute maps to unique sessionId associated with user's session.
     */
    public String getSessionId()
    {
        return this.sessionId;
    }


    /**
     * Return the User entity that is associated with this entity.
     *
     * Sample User data contained in Session object:
     * <p/>
     * ------------------------------------------<br />
     * U   UID  [demoUser4]<br />
     * U   IID  [ccbb2929-bf01-413d-b768-529de4d428e5]<br />
     * U   CN   [JoeUser4]<br />
     * U   DESC [Demo Test User 4]<br />
     * U   OU   [demousrs1]<br />
     * U   SN   [User4]<br />
     * U   BDTE [20090101]<br />
     * U   EDTE [20990101]<br />
     * U   BLDT [none]<br />
     * U   ELDT [none]<br />
     * U   DMSK [1234567]<br />
     * U   TO   [60]<br />
     * U   REST [false]<br />
     * U   PROP[0]=customerNumber VAL=3213432<br />
     * <p/>
     * USER ROLE[0]:<br />
     * role name <role1><br />
     * begin time <0000><br />
     * end time <0000><br />
     * begin date <none><br />
     * end date <none><br />
     * begin lock <none><br />
     * end lock <none><br />
     * day mask <all><br />
     * time out <0><br />
     * <p/>
     * USER ADMIN ROLE[0]:<br />
     * admin role name <DemoAdminUsers><br />
     * OsU <null><br />
     * OsP <null><br />
     * begin range <null><br />
     * end range <null><br />
     * begin time <0000><br />
     * end time <0000><br />
     * begin date <none><br />
     * end date <none><br />
     * begin lock <none><br />
     * end lock <none><br />
     * day mask <all><br />
     * time out <0><br />
     * <p/>
     * @return User entity that contains userid, roles and other attributes valid for Session.
     */
    public User getUser()
    {
        return this.user;
    }

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
     * Return the Set of User's RBAC Role names that are authorized from User's session based on hierarchical relationships.
     *
     * @return Set containing User's authorized RBAC role names.  This Set may be empty if User not assigned RBAC roles.
     */
    public Set<String> getAuthorizedRoles()
    {
        Set<String> authorizedSet = null;
        if (user != null)
        {
            authorizedSet = RoleUtil.getInheritedRoles(getRoles());
        }
        return authorizedSet;
    }

    /**
     * Return the Set of User's ARBAC Role names that are authorized from User's session based on hierarchical relationships.
     *
     * @return Set containing User's authorized ARBAC role names.  This Set may be empty if User not assigned ARBAC roles.
     */
    public Set<String> getAuthorizedAdminRoles()
    {
        Set<String> authorizedSet = null;
        if (user != null)
        {
            authorizedSet = AdminRoleUtil.getInheritedRoles(getAdminRoles());
        }
        return authorizedSet;
    }

    /**
     * Return the list of User's RBAC Roles that have been activated into User's session.  This list will not include
     * ascendant RBAC roles which may be retrieved using {@link #getAuthorizedRoles()}.
     *
     * @return List containing User's RBAC roles.  This list may be empty if User not assigned RBAC.
     */
    public List<UserRole> getRoles()
    {
        List<UserRole> roles = null;

        if (user != null)
            roles = user.getRoles();

        return roles;
    }

    /**
     * Return a list of User's Admin Roles  that have been activated into User's session.  This list will not include
     * ascendant ARBAC roles which may be retrieved using {@link #getAuthorizedAdminRoles()}.
     *
     * @return List containing User's Admin roles.  This list may be empty if User not assigned Administrative role.
     */
    public List<UserAdminRole> getAdminRoles()
    {
        List<UserAdminRole> roles = null;

        if (user != null)
            roles = user.getAdminRoles();

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
        return this.lastAccess;
    }

    /**
     * Gets the message that is associated with the user's last authentication attempt.
     *
     * @return String contains text explaining result of user's last authentication.
     */
    public String getMsg()
    {
        return this.message;
    }

    /**
     * Gets the warningId attribute that was returned from password policy checks.
     * <p/>
     * <ul>
     * <li> <code>NOWARNING = 0;</code>
     * <li> <code>NO_CONTROLS_FOUND = 10;</code>
     * <li> <code>PASSWORD_EXPIRATION_WARNING = 11;</code>
     * <li> <code>PASSWORD_GRACE_WARNING = 12;</code>
     * </ul>
     * <p/>
     *
     * @return int contains warning id.
     */
    public int getWarningId()
    {
        return this.warningId;
    }

    /**
     * Gets the attribute that specifies the number of times an expired password can
     * be used to authenticate before failure.
     *
     * @return The number of logins the user has left before password fails.
     */
    public int getGraceLogins()
    {
        return this.graceLogins;
    }

    /**
     * This attribute specifies the maximum number of seconds before a
     * password is due to expire that expiration warning messages will be
     * returned to an authenticating user.
     * <p/>
     * If this attribute is not present, or if the value is 0 no warnings
     * will be returned.  If not 0, the value must be smaller than the value
     * of the pwdMaxAge attribute.
     *
     * @return attribute is computed based on last time user has changed their password.
     */
    public int getExpirationSeconds()
    {
        return this.expirationSeconds;
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
     * <p/>
     *
     * @return int contains the error id that was generated on the user's last authentication.
     */
    public int getErrorId()
    {
        return this.errorId;
    }

    /**
     * Set a User entity into the Session.
     * Sample User data contained in Session object:
     * <p/>
     * ------------------------------------------<br />
     * U   UID  [demoUser4]<br />
     * U   IID  [ccbb2929-bf01-413d-b768-529de4d428e5]<br />
     * U   CN   [JoeUser4]<br />
     * U   DESC [Demo Test User 4]<br />
     * U   OU   [demousrs1]<br />
     * U   SN   [User4]<br />
     * U   BDTE [20090101]<br />
     * U   EDTE [20990101]<br />
     * U   BLDT [none]<br />
     * U   ELDT [none]<br />
     * U   DMSK [1234567]<br />
     * U   TO   [60]<br />
     * U   REST [false]<br />
     * U   PROP[0]=customerNumber VAL=3213432<br />
     * <p/>
     * USER ROLE[0]:<br />
     * role name <role1><br />
     * begin time <0000><br />
     * end time <0000><br />
     * begin date <none><br />
     * end date <none><br />
     * begin lock <none><br />
     * end lock <none><br />
     * day mask <all><br />
     * time out <0><br />
     * <p/>
     * USER ADMIN ROLE[0]:<br />
     * admin role name <DemoAdminUsers><br />
     * OsU <null><br />
     * OsP <null><br />
     * begin range <null><br />
     * end range <null><br />
     * begin time <0000><br />
     * end time <0000><br />
     * begin date <none><br />
     * end date <none><br />
     * begin lock <none><br />
     * end lock <none><br />
     * day mask <all><br />
     * time out <0><br />
     * <p/>
     * @param user Contains userId, roles and other security attributes used for access control.
     */
    public void setUser(User user)
    {
        this.user = user;
    }

    /**
     * Set the internal userId that is associated with User.  This method is used by DAO class and
     * is generated automatically by Fortress.  Attribute stored in LDAP cannot be changed by external caller.
     * This method can be used by client for search purposes only.
     *
     * @param internalUserId maps to 'ftId' in 'ftUserAttrs' object class.
     */
    public void setInternalUserId(String internalUserId)
    {
        this.user.setInternalId(internalUserId);
    }

    /**
     * Set the value to 'true' indicating that user has successfully authenticated with Fortress.  This value is set by
     * the Fortress DAO object.
     *
     * @param authenticated indicates result of authentication.
     */
    public void setAuthenticated(boolean authenticated)
    {
        isAuthenticated = authenticated;
    }

    /**
     * Set the userId that is associated with User.  UserId is required attribute and must be set on add, update, delete, createSession, authenticate, etc..
     *
     * @param userId maps to 'uid' attribute in 'inNetOrgPerson' object class.
     */
    public void setUserId(String userId)
    {
        this.user.setUserId(userId);
    }


    /**
     * Add a list of RBAC Roles to this entity that have been activated into Session or are under consideration for activation.
     *
     * @param roles List of type UserRole that contains at minimum UserId and Role name.
     */
    public void setRoles(List<UserRole> roles)
    {
        this.user.setRoles(roles);
    }

    /**
     * Add a single user-role object to the list of UserRoles for User.
     *
     * @param role UserRole contains at least userId and role name (activation) and additional constraints (assignment)
     */
    public void setRole(UserRole role)
    {
        user.setRole(role);
    }

    /**
     * Set the integer timeout that contains max time (in seconds) that User's session may remain inactive.
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
        this.lastAccess = System.currentTimeMillis();
    }

    /**
     * Set the message that is associated with the user's last authentication attempt.
     *
     * @param message Contains text explaining result of user's last authentication.
     */
    public void setMsg(String message)
    {
        this.message = message;
    }

    /**
     * Sets the warningId attribute that was returned from password policy checks.
     * <p/>
     * <ul>
     * <li> <code>NOWARNING = 0;</code>
     * <li> <code>NO_CONTROLS_FOUND = 10;</code>
     * <li> <code>PASSWORD_EXPIRATION_WARNING = 11;</code>
     * <li> <code>PASSWORD_GRACE_WARNING = 12;</code>
     * </ul>
     * <p/>
     *
     * @param warning contains warning id.
     */
    public void setWarningId(int warning)
    {
        this.warningId = warning;
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
     * <p/>
     *
     * @param error contains the error id that was generated on the user's last authentication.
     */
    public void setErrorId(int error)
    {
        this.errorId = error;
    }

    /**
     * This attribute specifies the number of times an expired password can
     * be used to authenticate.
     *
     * @param grace The number of logins the user has left before password fails.
     */
    public void setGraceLogins(int grace)
    {
        this.graceLogins = grace;
    }

    /**
     * This attribute specifies the maximum number of seconds before a
     * password is due to expire that expiration warning messages will be
     * returned to an authenticating user.
     * <p/>
     * If this attribute is not present, or if the value is 0 no warnings
     * will be returned.  If not 0, the value must be smaller than the value
     * of the pwdMaxAge attribute.
     *
     * @param expire attribute is computed based on last time user has changed their password.
     */
    public void setExpirationSeconds(int expire)
    {
        this.expirationSeconds = expire;
    }
}
