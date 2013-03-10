/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import us.jts.fortress.util.time.Constraint;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.*;


/**
 * All entities ({@link User}, {@link us.jts.fortress.rbac.Role}, {@link us.jts.fortress.rbac.Permission},
 * {@link PwPolicy} {@link us.jts.fortress.rbac.SDSet} etc...) are used to carry data between three Fortress
 * layers.starting with the (1) Manager layer down thru middle (2) Process layer and it's processing rules into
 * (3) DAO layer where persistence with the OpenLDAP server occurs.
 * <p/>
 * <h4>Fortress Processing Layers</h4>
 * <ol>
 * <li>Manager layer:  {@link us.jts.fortress.rbac.AdminMgrImpl}, {@link us.jts.fortress.rbac.AccessMgrImpl}, {@link us.jts.fortress.rbac.ReviewMgrImpl},...</li>
 * <li>Process layer:  {@link UserP}, {@link us.jts.fortress.rbac.RoleP}, {@link us.jts.fortress.rbac.PermP},...</li>
 * <li>DAO layer: {@link UserDAO}, {@link us.jts.fortress.rbac.RoleDAO}, {@link us.jts.fortress.rbac.PermDAO},...</li>
 * </ol>
 * Fortress clients must first instantiate the data entity before invoking one of the Manager APIs.  The caller must first
 * provide enough information to uniquely identity target record for the particular ldap operation performed.<br />
 * For example the User entity requires the {@link User#setUserId} attribute to be set before calling a Manager API.
 * The unique key to locate a User entity in the Fortress DIT is simply the userId field.<br />
 * Other ldap operations on User may require additional attributes to be set.
 * <p/>
 * <h4>User entity attribute usages include</h4>
 * <ul>
 * <li>{@link #setPassword(char[])} must be set before calling {@link us.jts.fortress.rbac.AccessMgrImpl#authenticate} and {@link us.jts.fortress.rbac.AccessMgrImpl#createSession(User, boolean)} (unless trusted).
 * <li>{@link #setOu} is required before calling {@link us.jts.fortress.rbac.AdminMgrImpl#addUser(User)} to add a new user to ldap.
 * <li>{@link #setRoles} will be set for {@link us.jts.fortress.rbac.AccessMgrImpl#createSession(User, boolean)} when selective RBAC Role activation is required.
 * <li>{@link #setAdminRoles} will be set for {@link us.jts.fortress.rbac.AccessMgrImpl#createSession(User, boolean)} when selective Administrative Role activation is required.
 * <li>{@link #setPwPolicy} may be set for {@link us.jts.fortress.rbac.AdminMgrImpl#updateUser(User)} to assign User to a policy {@link PwPolicy}.
 * <li>{@link #password} is the only case sensitive attribute on this entity.
 * </ul>
 * <p/>
 * Example to create new Fortress User:
 * <pre>
 * try
 * {
 *  // Instantiate the AdminMgr first
 *  AdminMgr adminMgr = AdminMgrFactory.createInstance();
 *
 *  User myUser = new User("myUserId", "myPassword".toCharArray(), myRoleName", "myOU");
 *  adminMgr.addUser(myUser);
 * }
 * catch (SecurityException ex)
 * {
 *  // log or throw
 * }</pre>
 * The above code will persist to LDAP a User object that has a userId of "myUserId", a password of "myPassword", a role assignment to "myRoleName", and assigned to organzational unit named "myOU".
 * This User can be used as a target for subsequent User-Role assignments, User-Permission grants, authentication, authorization and more.
 *
 * This entity aggregates one standard LDAP structural object class, {@code inetOrgPerson} see <a href="http://www.ietf.org/rfc/rfc2798.txt">RFC 2798</a>,
 * along with three auxiliary object extensions supplied by Fortress:  {@code ftUserAttrs}, {@code ftProperties}, {@code ftMods}.
 * The combination of the standard and custom object classes form a single entry within the directory and is represented in this entity class.
 *
 * <h4>Fortress User Schema</h4>
 *
 * 1. InetOrgPerson Structural Object Class. <br />
 * <code># The inetOrgPerson represents people who are associated with an</code><br />
 * <code># organization in some way.  It is a structural class and is derived</code><br />
 * <code># from the organizationalPerson which is defined in X.521 [X521].</code><br />
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.16.840.1.113730.3.2.2</code>
 * <li> <code>NAME 'inetOrgPerson'</code>
 * <li> <code>DESC 'RFC2798: Internet Organizational Person'</code>
 * <li> <code>SUP organizationalPerson</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MAY ( audio $ businessCategory $ carLicense $ departmentNumber $</code>
 * <li> <code>displayName $ employeeNumber $ employeeType $ givenName $</code>
 * <li> <code>homePhone $ homePostalAddress $ initials $ jpegPhoto $</code>
 * <li> <code>labeledURI $ mail $ manager $ mobile $ o $ pager $ photo $</code>
 * <li> <code>roomNumber $ secretary $ uid $ userCertificate $</code>
 * <li> <code>x500uniqueIdentifier $ preferredLanguage $</code>
 * <li> <code>userSMIMECertificate $ userPKCS12 ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 *
 * 2. organizationalPerson Structural Object Class.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.5.6.7</code>
 * <li> <code>NAME 'organizationalPerson'</code>
 * <li> <code>DESC 'RFC2256: an organizational person'</code>
 * <li> <code>SUP person</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MAY ( title $ x121Address $ registeredAddress $ destinationIndicator $</code>
 * <li> <code>preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $</code>
 * <li> <code>telephoneNumber $ internationaliSDNNumber $</code>
 * <li> <code>facsimileTelephoneNumber $ street $ postOfficeBox $ postalCode $</code>
 * <li> <code>postalAddress $ physicalDeliveryOfficeName $ ou $ st $ l ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 *
 * 3. ftProperties AUXILIARY Object Class is used to store client specific name/value pairs on target entity.<br />
 * <code># This aux object class can be used to store custom attributes.</code><br />
 * <code># The properties collections consist of name/value pairs and are not constrainted by Fortress.</code><br />
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.2</code>
 * <li> <code>NAME 'ftProperties'</code>
 * <li> <code>DESC 'Fortress Properties AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY ( ftProps ) ) </code>
 * <li>  ------------------------------------------
 * </ul>
 *
 * 4. ftUserAttrs is used to store user RBAC and Admin role assignment and other security attributes on User entity.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.1</code>
 * <li> <code>NAME 'ftUserAttrs'</code>
 * <li> <code>DESC 'Fortress User Attribute AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MUST ( ftId )</code>
 * <li> <code>MAY ( ftRC $ ftRA $ ftARC $ ftARA $ ftCstr</code>
 * <li>  ------------------------------------------
 * </ul>
 *
 * 5. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.4</code>
 * <li> <code>NAME 'ftMods'</code>
 * <li> <code>DESC 'Fortress Modifiers AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY (</code>
 * <li> <code>ftModifier $</code>
 * <li> <code>ftModCode $</code>
 * <li> <code>ftModId ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 *
 * @author Shawn McKinney
 */

@XmlRootElement(name = "fortUser")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "user", propOrder = {
    "userId",
    "description",
    "name",
    "internalId",
    "ou",
    "pwPolicy",
    "sn",
    "cn",
    "dn",
    "employeeType",
    "title",
    "address",
    "phones",
    "mobiles",
    "emails",
    "props",
    "locked",
    "reset",
    "system",
    "beginTime",
    "endTime",
    "beginDate",
    "endDate",
    "beginLockDate",
    "endLockDate",
    "dayMask",
    "timeout",
    "roles",
    "adminRoles",
    "password",
    "newPassword"
})
public class User extends FortEntity implements Constraint, Serializable
{
    private String userId;
    @XmlElement(nillable = true)
    private char[] password;
    @XmlElement(nillable = true)
    private char[] newPassword;
    private String internalId;
    @XmlElement(nillable = true)
    private List<UserRole> roles;
    @XmlElement(nillable = true)
    private List<UserAdminRole> adminRoles;
    private String pwPolicy;
    private String cn;
    private String sn;
    private String dn;
    private String ou;
    private String description;
    private String beginTime;
    private String endTime;
    private String beginDate;
    private String endDate;
    private String beginLockDate;
    private String endLockDate;
    private String dayMask;
    private String name;
    private String employeeType;
    private String title;
    private int timeout;
    private boolean reset;
    private boolean locked;
    private Boolean system;
    @XmlElement(nillable = true)
    private Props props = new Props();
    @XmlElement(nillable = true)
    private Address address;
    @XmlElement(nillable = true)
    private List<String> phones;
    @XmlElement(nillable = true)
    private List<String> mobiles;
    @XmlElement(nillable = true)
    private List<String> emails;

    /**
     * Default constructor not intended for external use and is typically used by internal Fortress classes.
     * User entity constructed in this manner cannot be used by other until additional attributes (i.e. userId) are set.
     */
    public User()
    {
    }

    /**
     * Construct User given userId.   Once loaded this entity can be passed to AccessMgr.createSession iff trusted == 'true'..
     *
     * @param userId String validated using simple length test and optional regular expression, i.e. safe text.
     */
    public User(String userId)
    {
        this.userId = userId;
    }

    /**
     * Construct User given userId and password.  Once loaded this entity can be passed to AccessMgr.createSession.
     *
     * @param userId   String validated using simple length test and optional regular expression, i.e. safe text.
     * @param password validated using simple length test and OpenLDAP password policies.
     */
    public User(String userId, char[] password)
    {
        this.userId = userId;
        this.password = password;
    }

    /**
     * Construct User given userId and password.  Once loaded this entity can be passed to AccessMgr.createSession.
     *
     * @param userId   String validated using simple length test and optional regular expression, i.e. safe text.
     * @param password validated using simple length test and OpenLDAP password policies.
     * @param roleName contains role that caller is requesting activation.
     */
    public User(String userId, char[] password, String roleName)
    {
        this.userId = userId;
        this.password = password;
        setRole(new UserRole(roleName));
    }

    /**
     * Construct User given userId and password.  Once loaded this entity can be passed to AccessMgr.createSession.
     *
     * @param userId   String validated using simple length test and optional regular expression, i.e. safe text.
     * @param password validated using simple length test and OpenLDAP password policies.
     * @param roleNames contains array of roleNames that caller is requesting activation.
     */
    public User(String userId, char[] password, String[] roleNames)
    {
        this.userId = userId;
        this.password = password;
        if (roleNames != null)
        {
            for (String name : roleNames)
            {
                setRole(new UserRole(name));
            }
        }
    }

    /**
     * Construct User given userId and password.  Once loaded this entity can be passed to AccessMgr.createSession.
     *
     * @param userId   String validated using simple length test and optional regular expression, i.e. safe text.
     * @param password validated using simple length test and OpenLDAP password policies.
     * @param roleName contains role that caller is requesting activation (see {@link us.jts.fortress.AccessMgr#createSession(User, boolean)}) or assignment (see {@link us.jts.fortress.AdminMgr#addUser(User)}).
     * @param ou org unit name that caller is requesting assigned to newly created User (see {@link us.jts.fortress.AdminMgr#addUser(User)}).
     */
    public User(String userId, char[] password, String roleName, String ou)
    {
        this.userId = userId;
        this.password = password;
        setRole(new UserRole(roleName));
        this.ou = ou;
    }

    /**
     * Used to retrieve User's valid userId attribute.  The Fortress userId maps to 'uid' for InetOrgPerson object class.
     *
     * @return String containing the userId.
     */
    public String toString()
    {
        return userId;
    }

    /**
     * Required by Constraint Interface but not needed for user entity. Not intended for external use.
     *
     * @return String containing constraint data ready for ldap.
     * @throws UnsupportedOperationException
     */
    public String getRawData()
    {
        throw new UnsupportedOperationException("not allowed for user");
    }

    /**
     * This is used internally by Fortress for Constraint operations.
     *
     * @return String contains name attribute used internally for constraint checking.
     */
    public String getName()
    {
        return name;
    }

    /**
     * This is used internally by Fortress for Constraint operations.  Values set here by external caller will be ignored.
     *
     * @param name contains attribute used internally for constraint checking.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Used to identify the employer to employee relationship.  Typical values used will be "Contractor", "Employee", "Intern", "Temp",
     * "External", and "Unknown" but any value may be used.
     *
     * @return  attribute maps to 'employeeType' attribute in 'inetOrgPerson' object class.
     */
    public String getEmployeeType()
    {
        return employeeType;
    }

    /**
     * Used to identify the employer to employee relationship.  Typical values used will be "Contractor", "Employee", "Intern", "Temp",
     * "External", and "Unknown" but any value may be used.
     *
     * @param employeeType maps to 'employeeType' attribute in 'inetOrgPerson' object class.
     */
    public void setEmployeeType(String employeeType)
    {
        this.employeeType = employeeType;
    }

    /**
     * The honorific prefix(es) of the User, or "Title" in most Western languages (e.g.  Ms. given the full name Ms.
     * Barbara Jane Jensen, III.).
     *
     * @return maps to 'title' attribute in 'inetOrgPerson' objectclass.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * The honorific prefix(es) of the User, or "Title" in most Western languages (e.g.  Ms. given the full name Ms.
     * Barbara Jane Jensen, III.).
     *
     * @param title maps to 'title' attribute in 'inetOrgPerson' objectclass.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Return the name of the OpenLDAP password policy that is set for this user.  This attribute may be null.
     * The attribute maps to 'pwdPolicySubentry' attribute from pwpolicy ldap object class.
     *
     * @return name maps to name of OpenLDAP policy in effect for User.
     */
    public String getPwPolicy()
    {
        return pwPolicy;
    }

    /**
     * Sets the OpenLDAP password policy name to enable for User.  This attribute is optional but if set, will be validated to ensure
     * contains actual OpenLDAP password policy name.
     *
     * @param pwPolicy parameter must contain valid OpenLDAP policy name.
     */
    public void setPwPolicy(String pwPolicy)
    {
        this.pwPolicy = pwPolicy;
    }

    /**
     * Return a list of User's RBAC Roles.
     *
     * @return List containing User's RBAC roles.  This list may be empty if User not assigned RBAC.
     */
    public List<UserRole> getRoles()
    {
        // do not return a null List to caller:
        if (roles == null)
        {
            roles = new ArrayList<>();
        }
        return roles;
    }

    /**
     * Add a list of RBAC Roles to this entity be considered for later processing:
     * AccessMgr (user-role activation) or AdminMgr (user-role assignment).
     *
     * @param roles List of type UserRole that contains at minimum UserId and Role name.
     */
    public void setRoles(List<UserRole> roles)
    {
        this.roles = roles;
    }

    /**
     * Add a single user-role object to the list of UserRoles for User.
     *
     * @param role UserRole contains {@link UserRole#name} to target for activation into {@link us.jts.fortress.rbac.Session}.
     */
    public void setRole(UserRole role)
    {
        if (roles == null)
        {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }

    /**
     * Add a single user-role object to the list of UserRoles for User.
     *
     * @param roleName contains role name to target for activation into {@link us.jts.fortress.rbac.Session}.
     */
    public void setRole(String roleName)
    {
        if (roles == null)
        {
            roles = new ArrayList<>();
        }
        roles.add(new UserRole(roleName));
    }

    /**
     * Removes a user-role object from the list of UserRoles.
     *
     * @param role UserRole must contain userId and role name.
     */
    public void delRole(UserRole role)
    {
        if (roles != null)
        {
            roles.remove(role);
        }
    }

    /**
     * Return a list of User's Admin Roles.
     *
     * @return List containing User's Admin roles.  This list may be empty if User not assigned Administrative role.
     */
    public List<UserAdminRole> getAdminRoles()
    {
        // do not return a null List to caller:
        if (adminRoles == null)
        {
            adminRoles = new ArrayList<>();
        }
        return adminRoles;
    }

    /**
     * Add a single user-adminRole object to the list of UserAdminRoles for User.
     *
     * @param roles UserAdminRole contains at least userId and admin role name (activation) and additional constraints (assignment)
     */
    public void setAdminRoles(List<UserAdminRole> roles)
    {
        this.adminRoles = roles;
    }

    /**
     * Add a single user-adminRole object to the list of UserAdminRoles for User.
     *
     * @param role UserAdminRole contains at least userId and adminRole name (activation) and additional constraints (assignment)
     */
    public void setAdminRole(UserAdminRole role)
    {
        if (adminRoles == null)
        {
            adminRoles = new ArrayList<>();
        }
        adminRoles.add(role);
    }

    /**
     * Add a single user-adminRole object to the list of UserAdminRoles for User.
     *
     * @param roleName contrains adminRole name.
     */
    public void setAdminRole(String roleName)
    {
        if (adminRoles == null)
        {
            adminRoles = new ArrayList<>();
        }
        adminRoles.add(new UserAdminRole(this.userId, roleName));
    }

    /**
     * Return the userId that is associated with User.  UserId is required attribute and must be set on add, update, delete, createSession, authenticate, etc..
     *
     * @return attribute maps to 'uid' in 'inetOrgPerson' object class.
     */
    public String getUserId()
    {
        return this.userId;
    }

    /**
     * Set the userId that is associated with User.  UserId is required attribute and must be set on add, update, delete, createSession, authenticate, etc..
     *
     * @param userId maps to 'uid' attribute in 'inNetOrgPerson' object class.
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }


    /**
     * Return the internal userId that is associated with User.  This attribute is generated automatically
     * by Fortress when new User is added to directory and is not known or changeable by external client.
     *
     * @return attribute maps to 'ftId' in 'ftUserAttrs' object class.
     */
    public String getInternalId()
    {
        return this.internalId;
    }

    /**
     * Set the internal userId that is associated with User.  This method is used by DAO class and
     * is generated automatically by Fortress.  Attribute stored in LDAP cannot be changed by external caller.
     * This method can be used by client for search purposes only.
     *
     * @param internalId maps to 'ftId' in 'ftUserAttrs' object class.
     */
    public void setInternalId(String internalId)
    {
        this.internalId = internalId;
    }

    /**
     * Generate an internal userId that is associated with User.  This method is used by DAO class and
     * is not available to outside classes.   The generated attribute maps to 'ftId' in 'ftUserAttrs' object class.
     */
    void setInternalId()
    {
        UUID uuid = UUID.randomUUID();
        this.internalId = uuid.toString();
    }

    /**
     * Returns optional description that is associated with User.  This attribute is validated but not constrained by Fortress.
     *
     * @return value that is mapped to 'description' in 'inetOrgPerson' object class.
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Sets the optional description that is associated with User.  This attribute is validated but not constrained by Fortress.
     *
     * @param description that is mapped to same name in 'inetOrgPerson' object class.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Return the optional password attribute for User.  Note this will only return values that were set by client
     * as the Fortress User DAO class does not return the value of stored password to caller.
     *
     * @return attribute containing User password.
     */
    public char[] getPassword()
    {
        return this.password;
    }

    /**
     * Set the optional password attribute associated for a User.  Note, this value is required before User will pass Fortress
     * authentication in {@link AccessMgrImpl#createSession(User, boolean)}.
     * Even though password is char[] format here it will be stored on the ldap server (using server-side controls) in configurable and standard hashed formats.
     *
     * @param password maps to 'userPassword' attribute in 'inetOrgPerson' object class.
     */
    public void setPassword(char[] password)
    {
        this.password = password;
    }

    public char[] getNewPassword()
    {
        return newPassword;
    }

    public void setNewPassword(char[] newPassword)
    {
        this.newPassword = newPassword;
    }

    /**
     * Returns common name associated with User.  This attribute is validated but not constrained by Fortress.
     * cn is not required but if not supplied by caller on create, will default to same value as {@link #userId} attribute.
     *
     * @return value that is mapped to 'cn' in 'inetOrgPerson' object class.
     */
    public String getCn()
    {
        return this.cn;
    }

    /**
     * Set the common name associated with User.  This attribute is validated but not constrained by Fortress.
     * cn is not required but if not supplied by caller on create, will default to same value as {@link #userId} attribute.
     *
     * @param cn mapped to same name in 'inetOrgPerson' object class.
     */
    public void setCn(String cn)
    {
        this.cn = cn;
    }

    /**
     * Returns surname associated with User.  This attribute is validated but not constrained by Fortress.
     * sn is not required but if not supplied by caller on create, will default to same value as {@link #userId} attribute.
     *
     * @return value that is mapped to 'sn' in 'inetOrgPerson' object class.
     */
    public String getSn()
    {
        return this.sn;
    }

    /**
     * Set the surname associated with User.  This attribute is validated but not constrained by Fortress.
     * sn is not required but if not supplied by caller on create, will default to same value as {@link #userId} attribute.
     *
     * @param sn mapped to same name in 'inetOrgPerson' object class.
     */
    public void setSn(String sn)
    {
        this.sn = sn;
    }

    /**
     * Returns distinguished name associated with User.  This attribute is generated by DAO and is not allowed for outside classes to modify.
     * This attribute is for internal user only and need not be processed by external clients.
     *
     * @return value that is mapped to 'dn' in 'inetOrgPerson' object class.
     */
    public String getDn()
    {
        return this.dn;
    }

    /**
     * Set distinguished name associated with User.  This attribute is used by DAO and is not allowed for outside classes.
     * This attribute cannot be set by external callers.
     *
     * @param dn that is mapped to same name in 'inetOrgPerson' object class.
     */
    void setDn(String dn)
    {
        this.dn = dn;
    }

    /**
     * Returns orgUnit name for User.  This attribute is validated and constrained by Fortress and must contain name of existing User OU.
     * This attribute is required on {@link AdminMgrImpl#addUser(User)} but not on {@link ReviewMgrImpl#readUser(User)}.
     *
     * @return value that is mapped to 'ou' in 'inetOrgPerson' object class.
     */
    public String getOu()
    {
        return this.ou;
    }

    /**
     * Set the orgUnit name associated with User.  This attribute is validated and constrained by Fortress and must contain name of existing User OU.
     * This attribute is required on {@link AdminMgrImpl#addUser(User)} but not on {@link ReviewMgrImpl#readUser(User)}.
     *
     * @param ou mapped to same name in 'inetOrgPerson' object class.
     */
    public void setOu(String ou)
    {
        this.ou = ou;
    }

    /**
     * temporal boolean flag is used by internal Fortress components.
     *
     * @return boolean indicating if temporal constraints are placed on user.
     */
    @Override
    public boolean isTemporalSet()
    {
        return (beginTime != null && endTime != null && beginDate != null && endDate != null && beginLockDate != null && endLockDate != null && dayMask != null);
    }

    /**
     * Contains the begin time of day user is allowed to signon to system.  The format is military time - HHMM, i.e. 0800 (8:00 am) or 1700 (5:00 p.m.).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public String getBeginTime()
    {
        return this.beginTime;
    }

    /**
     * Set the begin time of day user is allowed to signon to system.  The format is military time - HHMM, i.e. 0800 (8:00 am) or 1700 (5:00 p.m.).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param beginTime maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public void setBeginTime(String beginTime)
    {
        this.beginTime = beginTime;
    }

    /**
     * Contains the end time of day user is allowed to occupy system.  The format is military time - HHMM, i.e. 0000 (12:00 am) or 2359 (11:59 p.m.).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public String getEndTime()
    {
        return this.endTime;
    }

    /**
     * Set the end time of day user is allowed to signon to system.  The format is military time - HHMM, i.e. 0000 (12:00 am) or 2359 (11:59 p.m.).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param endTime maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    /**
     * Contains the begin date when user is allowed to signon to system.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public String getBeginDate()
    {
        return this.beginDate;
    }

    /**
     * Set the beginDate when user is allowed to signon to system.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param beginDate maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public void setBeginDate(String beginDate)
    {
        this.beginDate = beginDate;
    }

    /**
     * Contains the end date when user is allowed to signon to system.  The format is - YYYYMMDD, i.e. 20101231 (December 31, 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public String getEndDate()
    {
        return this.endDate;
    }

    /**
     * Set the end date when user is not allowed to signon to system.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param endDate maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    /**
     * Contains the begin lock date when user is temporarily not allowed to signon to system.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public String getBeginLockDate()
    {
        return this.beginLockDate;
    }

    /**
     * Set the begin lock date when user is temporarily not allowed to signon to system.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param beginLockDate maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public void setBeginLockDate(String beginLockDate)
    {
        this.beginLockDate = beginLockDate;
    }

    /**
     * Contains the end lock date when user is allowed to signon to system once again.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public String getEndLockDate()
    {
        return this.endLockDate;
    }

    /**
     * Set the end lock date when user is allowed to signon to system once again.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param endLockDate maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public void setEndLockDate(String endLockDate)
    {
        this.endLockDate = endLockDate;
    }

    /**
     * Get the daymask that indicates what days of week user is allowed to signon to system.  The format is 1234567, i.e. 23456 (Monday, Tuesday, Wednesday, Thursday, Friday).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public String getDayMask()
    {
        return this.dayMask;
    }

    /**
     * Set the daymask that specifies what days of week user is allowed to signon to system.  The format is 1234567, i.e. 23456 (Monday, Tuesday, Wednesday, Thursday, Friday).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param dayMask maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public void setDayMask(String dayMask)
    {
        this.dayMask = dayMask;
    }

    /**
     * Return the integer timeout that contains total time (in seconds) that User's session may remain inactive.
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public Integer getTimeout()
    {
        return this.timeout;
    }

    /**
     * Set the integer timeout that contains max time (in seconds) that User's session may remain inactive.
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param timeout maps to 'ftCstr' attribute in 'ftUserAttrs' object class.
     */
    @Override
    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }

    /**
     * If set to true User's password has been reset by administrator.
     * This attribute will be ignored if set by external callers.
     *
     * @return boolean value maps to 'pwdResetTime' in OpenLDAP's pwpolicy object class.
     */
    public boolean isReset()
    {
        return reset;
    }

    /**
     * If set to true User's password has been reset by administrator.
     * This attribute will be ignored if set by external callers.
     *
     * @param reset contains boolean value which maps to 'pwdResetTime' in OpenLDAP's pwpolicy object class.
     */
    public void setReset(boolean reset)
    {
        this.reset = reset;
    }

    /**
     * If set to true User's password has been locked by administrator or directory itself due to password policy violations.
     * This attribute will be ignored if set by external callers.
     *
     * @return boolean value maps to 'pwdLockedTime' in OpenLDAP's pwpolicy object class.
     */
    public boolean isLocked()
    {
        return locked;
    }

    /**
     * If set to true User's password has been locked by administrator or directory itself due to password policy violations.
     * This attribute will be ignored if set by external callers.
     *
     * @param locked contains boolean value which maps to 'pwdResetTime' in OpenLDAP's pwpolicy object class.
     */
    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }

    /**
     * Gets the value of the Props property.  This method is used by Fortress and En Masse and should not be called by external programs.
     *
     * @return
     *     possible object is
     *     {@link Props }
     *
     */
    public Props getProps()
    {
        return props;
    }

    /**
     * Sets the value of the Props property.  This method is used by Fortress and En Masse and should not be called by external programs.
     *
     * @param value
     *     allowed object is
     *     {@link Props }
     *
     */
    public void setProps(Props value)
    {
        this.props = value;
    }

    /**
     * Add name/value pair to list of properties associated with User.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @param key   contains property name and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     * @param value
     */
    public void addProperty(String key, String value)
    {
        Props.Entry entry = new Props.Entry();
        entry.setKey(key);
        entry.setValue(value);
        this.props.getEntry().add(entry);
    }

    /**
     * Get a name/value pair attribute from list of properties associated with User.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @param key contains property name and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     * @return value containing name/value pair that maps to 'ftProps' attribute in 'ftProperties' aux object class.
     */
    public String getProperty(String key)
    {
        List<Props.Entry> props = this.props.getEntry();
        Props.Entry keyObj = new Props.Entry();
        keyObj.setKey(key);

        String value = null;
        int indx = props.indexOf(keyObj);
        if(indx != -1)
        {
            Props.Entry entry = props.get(props.indexOf(keyObj));
            value = entry.getValue();
        }

        return value;
    }

    /**
     * Add new collection of name/value pairs to attributes associated with User.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @param props contains collection of name/value pairs and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     */
    public void addProperties(Properties props)
    {
        if(props != null)
        {
            for (Enumeration e = props.propertyNames(); e.hasMoreElements(); )
            {
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                String key = (String) e.nextElement();
                String val = props.getProperty(key);
                addProperty(key, val);
            }
        }
    }

    /**
     * Return the collection of name/value pairs to attributes associated with User.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @return Properties contains collection of name/value pairs and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     */
    public Properties getProperties()
    {
        Properties properties = null;
        List<Props.Entry> props = this.props.getEntry();
        if (props.size() > 0)
        {
            properties = new Properties();
            //int size = props.size();
            for (Props.Entry entry : props)
            {
                String key = entry.getKey();
                String val = entry.getValue();
                properties.setProperty(key, val);
            }
        }
        return properties;
    }

    /**
     * Get address data from entity that was persisted in directory as attributes defined by RFC 2798's LDAP inetOrgPerson Object Class:
     *
     * <ul>
     * <li>  ------------------------------------------
     * <li> <code>postalAddress</code>
     * <li> <code>st</code>
     * <li> <code>postalCode</code>
     * <li> <code>postOfficeBox</code>
     * <li>  ------------------------------------------
     * </ul>
     *
     * @return {@link Address}
     */
    public Address getAddress()
    {
        if(address == null)
        {
            address = new Address();
        }
        return address;
    }

    /**
     * Set address data onto entity that stored in directory as attributes defined by RFC 2798's LDAP inetOrgPerson Object Class:
     *
     * <ul>
     * <li>  ------------------------------------------
     * <li> <code>postalAddress</code>
     * <li> <code>st</code>
     * <li> <code>postalCode</code>
     * <li> <code>postOfficeBox</code>
     * <li>  ------------------------------------------
     * </ul>
     *
     * @param address
     */
    public void setAddress(Address address)
    {
        this.address = address;
    }

    /**
     * Retrieve multi-occurring {@code telephoneNumber} associated with {@code organizationalPerson} object class.
     *
     * @return List of type String that contains zero or more phone numbers associated with the user.
     */
    public List<String> getPhones()
    {
        if (phones == null)
        {
            phones = new ArrayList<>();
        }
        return phones;
    }

    /**
     * Set multi-occurring {@code telephoneNumber} number to associated with {@code organizationalPerson} object class.
     *
     * @param phones contains an ArrayList of type String with zero or more phone numbers associated with the user.
     */
    public void setPhones(List<String> phones)
    {
        this.phones = phones;
    }

    /**
     * Set phone number to stored in rfc822Mailbox format and associated with {@code inetOrgPerson} object class.
     *
     * @param phone contains String bound to {@code telephoneNumber} attribute on {@code organizationalPerson} object class.
     */
    public void setPhone(String phone)
    {
        if (phones == null)
        {
            phones = new ArrayList<>();
        }
        this.phones.add(phone);
    }

    /**
     * Retrieve multi-occurring {@code mobile} associated with {@code inetOrgPerson} object class.
     *
     * @return List of type String that contains zero or more mobile phone numbers associated with the user.
     */
    public List<String> getMobiles()
    {
        if (mobiles == null)
        {
            mobiles = new ArrayList<>();
        }
        return mobiles;
    }

    /**
     * Set multi-occurring {@code mobile} associated with {@code inetOrgPerson} object class.
     *
     * @param mobiles contains an ArrayList of type String with zero or more mobile phone numbers associated with the user.
     */
    public void setMobiles(List<String> mobiles)
    {
        this.mobiles = mobiles;
    }

    /**
     * Set a single {@code mobile} associated with {@code inetOrgPerson} object class.
     *
     * @param mobile contains a String containing mobile phone numbers associated with the user.
     */
    public void setMobile(String mobile)
    {
        if (mobiles == null)
        {
            mobiles = new ArrayList<>();
        }
        this.mobiles.add(mobile);
    }

    /**
     * Retrieve multi-occurring email address stored in rfc822Mailbox format associated with {@code inetOrgPerson} object class.
     *
     * @return List of type String that contains zero or more email addresses associated with the user.
     */
    public List<String> getEmails()
    {
        if (emails == null)
        {
            emails = new ArrayList<>();
        }
        return emails;
    }

    /**
     * Set multi-occurring email address to stored in rfc822Mailbox format and associated with {@code inetOrgPerson} object class.
     *
     * @param emails contains an ArrayList of type String with zero or more email addresses associated with the user.
     */
    public void setEmails(List<String> emails)
    {
        this.emails = emails;
    }

    /**
     * Set a single email address in rfc822Mailbox format to be assoicated with {@code inetOrgPerson} object class.
     *
     * @param email contains a String to be stored as email address on user.
     */
    public void setEmail(String email)
    {
        if (emails == null)
        {
            emails = new ArrayList<>();
        }
        this.emails.add(email);
    }

    public Boolean isSystem()
    {
        return system;
    }

    public void setSystem(Boolean system)
    {
        this.system = system;
    }

    /**
     * Override hashcode so User compare operations work in case insensitive manner in collection classes.
     *
     * @return int
     */
    public int hashCode()
    {
        return this.getUserId().toUpperCase().hashCode();
    }

    /**
     * Matches the userId from two User entities.
     *
     * @param thatObj contains a User entity.
     * @return boolean indicating both objects contain matching userIds.
     */
    public boolean equals(Object thatObj)
    {
        if (this == thatObj) return true;
        if (this.getUserId() == null) return false;
        if (!(thatObj instanceof User)) return false;
        User thatUser = (User) thatObj;
        if (thatUser.getUserId() == null) return false;
        return thatUser.getUserId().equalsIgnoreCase(this.getUserId());
    }
}