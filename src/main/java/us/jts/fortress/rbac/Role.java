/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import us.jts.fortress.util.time.CUtil;
import us.jts.fortress.util.time.Constraint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * All entities ({@link User}, {@link Role}, {@link us.jts.fortress.rbac.Permission},
 * {@link PwPolicy} {@link SDSet} etc...) are used to carry data between three Fortress
 * layers.starting with the (1) Manager layer down thru middle (2) Process layer and it's processing rules into
 * (3) DAO layer where persistence with the OpenLDAP server occurs.
 * <h4>Fortress Processing Layers</h4>
 * <ol>
 * <li>Manager layer:  {@link us.jts.fortress.rbac.AdminMgrImpl}, {@link us.jts.fortress.rbac.AccessMgrImpl}, {@link us.jts.fortress.rbac.ReviewMgrImpl},...</li>
 * <li>Process layer:  {@link UserP}, {@link RoleP}, {@link us.jts.fortress.rbac.PermP},...</li>
 * <li>DAO layer: {@link UserDAO}, {@link RoleDAO}, {@link us.jts.fortress.rbac.PermDAO},...</li>
 * </ol>
 * Fortress clients first instantiate and populate a data entity before invoking any of the Manager APIs.  The caller must
 * provide enough information to uniquely identity the entity target within ldap.<br />
 * For example, this entity requires {@link #setName} attribute set before passing into {@link us.jts.fortress.rbac.AdminMgrImpl} APIs.
 * Create methods sometimes require more attributes (than Read) due to constraints enforced between entities although only {@link Role#setName} is required for {@link Role}.
 * <p/>
 * <h4>Role entity attribute usages include</h4>
 * <ul>
 * <li>{@link #setName} attribute must be set before calling {@link us.jts.fortress.rbac.AdminMgrImpl#addRole(Role)}, {@link us.jts.fortress.rbac.AdminMgrImpl#updateRole(Role)} or  {@link us.jts.fortress.rbac.AdminMgrImpl#deleteRole(Role)}
 * <li>{@link us.jts.fortress.util.time.Constraint} may be set <b>before</b> calling method {@link us.jts.fortress.rbac.AdminMgrImpl#addRole(Role)}.
 * <li>{@link us.jts.fortress.util.time.Constraint} will be <b>returned</b> to caller on methods like {@link us.jts.fortress.rbac.ReviewMgrImpl#readRole(Role)} or {@link us.jts.fortress.rbac.ReviewMgrImpl#findRoles(String)} iff persisted to entity prior to call.
 * </ul>
 * <p/>
 * This entity is used to store the RBAC Role assignments that comprise the many-to-many relationships between {@link User}s and {@link us.jts.fortress.rbac.Permission}s.
 * <br />The unique key to locate a Role entity (which is subsequently assigned both to Users and Permissions) is 'Role.name'.<br />
 * <p/>
 * There is a many-to-many relationship between User's, RBAC Roles and Permissions.
 * <h3>{@link User}*<->*{@link Role}*<->*{@link us.jts.fortress.rbac.Permission}</h3>
 * <p/>
 * <img src="../doc-files/RbacCore.png">
 * <p/>
 * Example to create new RBAC Role:
 * <pre>
 * try
 * {
 *  // Instantiate the AdminMgr first
 *  AdminMgr adminMgr = AdminMgrFactory.createInstance();
 *
 *  Role myRole = new Role("MyRoleName");
 *  myRole.setDescription("This is a test role");
 *  adminMgr.addRole(myRole);
 * }
 * catch (SecurityException ex)
 * {
 *  // log or throw
 * }</pre>
 * The above code will persist to LDAP a Role object that can be used as a target for User-Role assignments and Role-Permission grants.
 * <p/>
 * <h4>Role Schema</h4>
 * The Fortress Role entity is a composite of the following other Fortress structural and aux object classes:
 * <p/>
 * 1. organizationalRole Structural Object Class is used to store basic attributes like cn and description.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.5.6.8 NAME 'organizationalRole'</code>
 * <li> <code>DESC 'RFC2256: an organizational role'</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST cn</code>
 * <li> <code>MAY ( x121Address $ registeredAddress $ destinationIndicator $</code>
 * <li> <code>preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $</code>
 * <li> <code>telephoneNumber $ internationaliSDNNumber $ facsimileTelephoneNumber $</code>
 * <li> <code>seeAlso $ roleOccupant $ preferredDeliveryMethod $ street $</code>
 * <li> <code>postOfficeBox $ postalCode $ postalAddress $</code>
 * <li> <code>physicalDeliveryOfficeName $ ou $ st $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * 2. ftRls Structural objectclass is used to store the Role information like name and temporal constraint attributes.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass	( 1.3.6.1.4.1.38088.2.1</code>
 * <li> <code>NAME 'ftRls'</code>
 * <li> <code>DESC 'Fortress Role Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftRoleName )</code>
 * <li> <code>MAY ( description $ ftCstr ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
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
 * <p/>
 * 4. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.
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
 * <p/>
 *
 * @author Shawn McKinney
 */
@XmlRootElement(name = "fortRole")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "role", propOrder = {
    "name",
    "id",
    "description",
    "parents",
    "children",
    "beginDate",
    "beginLockDate",
    "beginTime",
    "dayMask",
    "endDate",
    "endLockDate",
    "endTime",
    "timeout",
    "rawData"
})
@XmlSeeAlso({
    AdminRole.class
})
public class Role extends FortEntity
    implements Constraint, Graphable, java.io.Serializable
{
    private String id;          // this maps to ftId
    private String name;          // this is ftRoleName
    private String description; // this is description
    @XmlTransient
    private String dn;          // this attribute is automatically saved to each ldap record.
    @XmlTransient
    private List<String> occupants;
    private Set<String> parents;
    private Set<String> children;
    private String beginTime;     // this attribute is ftCstr
    private String endTime;        // this attribute is ftCstr
    private String beginDate;    // this attribute is ftCstr
    private String endDate;        // this attribute is ftCstr
    private String beginLockDate;// this attribute is ftCstr
    private String endLockDate;    // this attribute is ftCstr
    private String dayMask;        // this attribute is ftCstr
    private int timeout;        // this attribute is ftCstr

    /**
     * Default constructor is used by internal Fortress classes.
     */
    public Role()
    {
    }

    /**
     * Construct a Role entity with a given name.
     *
     * @param name maps to 'cn' attribute on 'organizationalrole' object class.
     */
    public Role(String name)
    {
        this.name = name;
    }

    /**
     * Construct an RBAC Role with a given temporal constraint.
     *
     * @param con maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    public Role(us.jts.fortress.util.time.Constraint con)
    {
        CUtil.copy(con, this);
    }

    /**
     * Required on DAO classes convert Temporal attributes stored on entity to raw data object format needed for ldap.  For internal use only.
     *
     * @return String that maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    public String getRawData()
    {
        return rawData;
    }

    /**
     * Required on DAO classes convert Temporal from raw ldap data to entity attributes.  For internal use only.
     *
     * @param rawData maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    public void setRawData(String rawData)
    {
        this.rawData = rawData;
    }

    private String rawData;

    /**
     * Gets the name required attribute of the Role object
     *
     * @return attribute maps to 'cn' attribute on 'organizationalrole' object class.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the required name attribute on the Role object
     *
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Set the occupant attribute with the contents of the User dn.
     * @param occupant maps to 'roleOccupant' attribute on 'organizationalrole' object class.
     */
    public void setOccupant(String occupant)
    {
        if (this.occupants == null)
        {
            this.occupants = new ArrayList<>();
        }
        this.occupants.add(occupant);
    }

    /**
     * Return list of occupants for a particular Role entity.
     * @return List of type String containing User dn that maps to 'roleOccupant' attribute on 'organizationalrole' object class.
     */
    public List<String> getOccupants()
    {
        return occupants;
    }

    /**
     * Set a list of occupants for a particular Role entity.
     * @param occupants contains a List of type String which maps to 'roleOccupant' attribute on 'organizationalrole' object class.
     */
    public void setOccupants(List<String> occupants)
    {
        this.occupants = occupants;
    }

    /**
     * Returns optional description that is associated with Role.  This attribute is validated but not constrained by Fortress.
     *
     * @return value that is mapped to 'description' in 'organizationalrole' object class.
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Sets the optional description that is associated with Role.  This attribute is validated but not constrained by Fortress.
     *
     * @param description that is mapped to same name in 'organizationalrole' object class.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Return the internal id that is associated with Role.  This attribute is generated automatically
     * by Fortress when new Role is added to directory and is not known or changeable by external client.
     *
     * @return attribute maps to 'ftId' in 'ftRls' object class.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Generate an internal Id that is associated with Role.  This method is used by DAO class and
     * is not available to outside classes.   The generated attribute maps to 'ftId' in 'ftRls' object class.
     */
    public void setId()
    {
        // generate a unique id that will be used as the rDn for this entry:
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
    }

    /**
     * Set the internal Id that is associated with Role.  This method is used by DAO class and
     * is generated automatically by Fortress.  Attribute stored in LDAP cannot be changed by external caller.
     * This method can be used by client for search purposes only.
     *
     * @param id maps to 'ftId' in 'ftRls' object class.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * temporal boolean flag is used by internal Fortress components.
     *
     * @return boolean indicating if temporal constraints are placed on Role.
     */
    @Override
    public boolean isTemporalSet()
    {
        return (beginTime != null && endTime != null && beginDate != null && endDate != null && beginLockDate != null && endLockDate != null && dayMask != null);
    }


    /**
     * Contains the begin time of day Role is allowed to be activated in session.  The format is military time - HHMM, i.e. 0800 (8:00 am) or 1700 (5:00 p.m.).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public String getBeginTime()
    {
        return this.beginTime;
    }

    /**
     * Set the begin time of day Role is allowed to be activated in session.  The format is military time - HHMM, i.e. 0800 (8:00 am) or 1700 (5:00 p.m.).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param beginTime maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public void setBeginTime(String beginTime)
    {
        this.beginTime = beginTime;
    }

    /**
     * Contains the end time of day Role is allowed to be activated in session.  The format is military time - HHMM, i.e. 0000 (12:00 am) or 2359 (11:59 p.m.).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public String getEndTime()
    {
        return this.endTime;
    }

    /**
     * Set the end time of day Role is allowed to be activated in session.  The format is military time - HHMM, i.e. 0000 (12:00 am) or 2359 (11:59 p.m.).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param endTime maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    /**
     * Contains the begin date when Role is allowed to be activated in session.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public String getBeginDate()
    {
        return this.beginDate;
    }

    /**
     * Set the beginDate when Role is allowed to be activated in session.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param beginDate maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public void setBeginDate(String beginDate)
    {
        this.beginDate = beginDate;
    }

    /**
     * Contains the end date when Role is allowed to be activated in session.  The format is - YYYYMMDD, i.e. 20101231 (December 31, 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public String getEndDate()
    {
        return this.endDate;
    }

    /**
     * Set the end date when Role is not allowed to be activated in session.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param endDate maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    /**
     * Contains the begin lock date when Role is temporarily not allowed to be activated in session.  The format is - YYMMDD, i.e. 20100101 (January 1. 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public String getBeginLockDate()
    {
        return this.beginLockDate;
    }

    /**
     * Set the begin lock date when Role is temporarily not allowed to be activated in session.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param beginLockDate maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public void setBeginLockDate(String beginLockDate)
    {
        this.beginLockDate = beginLockDate;
    }

    /**
     * Contains the end lock date when Role is allowed to be activated in session once again.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public String getEndLockDate()
    {
        return this.endLockDate;
    }

    /**
     * Set the end lock date when Role is allowed to be activated in session once again.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param endLockDate maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public void setEndLockDate(String endLockDate)
    {
        this.endLockDate = endLockDate;
    }

    /**
     * Get the daymask that indicates what days of week Role is allowed to be activated in session.  The format is 1234567, i.e. 23456 (Monday, Tuesday, Wednesday, Thursday, Friday).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return attribute maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public String getDayMask()
    {
        return this.dayMask;
    }

    /**
     * Set the daymask that specifies what days of week Role is allowed to be activated in session.  The format is 1234567, i.e. 23456 (Monday, Tuesday, Wednesday, Thursday, Friday).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param dayMask maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public void setDayMask(String dayMask)
    {
        this.dayMask = dayMask;
    }

    /**
     * Return the integer timeout that contains total time (in seconds) that Role may remain inactive in User's session before it is deactivated.
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return int maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public Integer getTimeout()
    {
        return this.timeout;
    }

    /**
     * Set the integer timeout that contains max time (in seconds) that Role may remain inactive in User's session before it is deactivated.
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param timeout maps to 'ftCstr' attribute in 'ftRls' object class.
     */
    @Override
    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }

    /**
     * Get the names of roles that are parents (direct ascendants) of this role.
     * @return Set of parent role names assigned to this role.
     */
    @Override
    public Set<String> getParents()
    {
        return parents;
    }

    /**
     * Set the names of roles names that are parents (direct ascendants) of this role.
     * @param parents contains the Set of parent role names assigned to this role.
     */
    @Override
    public void setParents(Set<String> parents)
    {
        this.parents = parents;
    }

    /**
     * Set the occupant attribute with the contents of the User dn.
     * @param parent maps to 'ftParents' attribute on 'ftRls' object class.
     */
    @Override
    public void setParent(String parent)
    {
        if (this.parents == null)
        {
            this.parents = new HashSet<>();
        }
        this.parents.add(parent);
    }

    /**
     * Set the occupant attribute with the contents of the User dn.
     * @param parent maps to 'ftParents' attribute on 'ftRls' object class.
     */
    @Override
    public void delParent(String parent)
    {
        if (this.parents != null)
        {
            this.parents.remove(parent);
        }
    }

    /**
     * Return the Set of child role names (direct descendants) of this role.
     * @return Set of child role names assigned to this role.
     */
    public Set<String> getChildren()
    {
        return children;
    }

    /**
     * Set the Set of child role names (direct descendants) of this role
     * @param children contains the Set of child role names assigned to this role.
     */
    public void setChildren(Set<String> children)
    {
        this.children = children;
    }

    /**
     * Matches the name from two Role entities.
     *
     * @param thatObj contains a Role entity.
     * @return boolean indicating both objects contain matching Role names.
     */
    public boolean equals(Object thatObj)
    {
        if (this == thatObj) return true;
        if (this.getName() == null) return false;
        if (!(thatObj instanceof Role)) return false;
        Role thatRole = (Role) thatObj;
        if (thatRole.getName() == null) return false;
        return thatRole.getName().equalsIgnoreCase(this.getName());
    }
}

