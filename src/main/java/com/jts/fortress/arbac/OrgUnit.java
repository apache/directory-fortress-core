/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */
package com.jts.fortress.arbac;

import com.jts.fortress.FortEntity;

import javax.xml.bind.annotation.*;
import java.util.Set;
import java.util.UUID;

/**
 * All entities ({@link com.jts.fortress.arbac.AdminRole}, {@link OrgUnit},
 * {@link com.jts.fortress.rbac.SDSet} etc...) are used to carry data between three Fortress
 * layers.starting with the (1) Manager layer down thru middle (2) Process layer and it's processing rules into
 * (3) DAO layer where persistence with the OpenLDAP server occurs.
 * <h4>Fortress Processing Layers</h4>
 * <ol>
 * <li>Manager layer:  {@link com.jts.fortress.arbac.DelegatedAdminMgrImpl}, {@link com.jts.fortress.arbac.DelegatedAccessMgrImpl}, {@link com.jts.fortress.arbac.DelegatedReviewMgrImpl},...</li>
 * <li>Process layer:  {@link com.jts.fortress.arbac.AdminRoleP}, {@link OrgUnitP},...</li>
 * <li>DAO layer: {@link com.jts.fortress.arbac.AdminRoleDAO}, {@link OrgUnitDAO},...</li>
 * </ol>
 * Fortress clients first instantiate and populate a data entity before invoking any of the Manager APIs.  The caller must
 * provide enough information to uniquely identity the entity target within ldap.<br />
 * For example, this entity requires {@link #name} and {@link #type} set before passing into {@link com.jts.fortress.arbac.DelegatedAdminMgrImpl} or  {@link com.jts.fortress.arbac.DelegatedReviewMgrImpl} APIs.
 * Create methods usually require more attributes (than Read) due to constraints enforced between entities.
 * <p/>
 * This entity implements both User and Permission OU pool functionality that defines org membership of entities for ARBAC02 style admin checks..
 * <br />The unique key to locate an OrgUnit entity (which is subsequently assigned both to Users and Permissions) is 'OrgUnit.name' and 'OrgUnit.Type'.<br />
 * <p/>
 * An OrgUnit name may contain alphanumeric and simple symbols that are safe text (.,:;-_).  Any non-safe text will be
 * encoded before persistence.  Valid names include:
 * <ol>
 * <li>123</li>
 * <li>OneTwoThree</li>
 * <li>One-Two-Three</li>
 * <li>One_Two_Three</li>
 * <li>One:2:3</li>
 * <li>1:2:3</li>
 * <li>1.2.3</li>
 * <li>1,2,3</li>
 * <li>1_2_3</li>
 * <li>etc...</li>
 * </ol>
 * <p/>
 * There is a Many-to-One relationship between a User and OrgUnit.
 * <h3>{@link com.jts.fortress.rbac.User}*<->1 {@link OrgUnit}</h3>
 * <p/>
 * There is a Many-to-One relationship between a {@link com.jts.fortress.rbac.PermObj} object and {@link OrgUnit}.
 * <h3>{@link com.jts.fortress.rbac.PermObj}*<->1 {@link OrgUnit}</h3>
 * <p/>
 * Example to create new ARBAC User OrgUnit:
 * <p/>
 * <code>OrgUnit myUserOU = new OrgUnit("MyUserOrgName", OrgUnit.Type.USER);</code><br />
 * <code>myUserOU.setDescription("This is a test User OrgUnit");</code><br />
 * <code>DelegatedAdminMgr delAdminMgr = DelegatedAdminMgrFactory.createInstance();</code><br />
 * <code>delAdminMgr.add(myUserOU);</code><br />
 * <p/>
 * This will create a User OrgUnit that can be used as a target for User OU and AdminRole OS-U assignments.
 * <p/>
 * Example to create new ARBAC Perm OrgUnit:
 * <p/>
 * <code>OrgUnit myPermOU = new OrgUnit("MyPermOrgName", OrgUnit.Type.PERM);</code><br />
 * <code>myPermOU.setDescription("This is a test Perm OrgUnit");</code><br />
 * <code>DelegatedAdminMgr delAdminMgr = DelegatedAdminMgrFactory.createInstance();</code><br />
 * <code>delAdminMgr.add(myPermOU);</code><br />
 * <p/>
 * This will create a Perm OrgUnit that can be used as a target for Perm OU and AdminRole OS-P assignments.
 * <p/>
 * <h4>OrgUnit Schema</h4>
 * The Fortress OrgUnit entity is a composite of the following other Fortress structural and aux object classes:
 * <p/>
 * 1. organizationalUnit Structural Object Class is used to store basic attributes like ou and description.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.5.6.5 NAME 'organizationalUnit'</code>
 * <li> <code>DESC 'RFC2256: an organizational unit'</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST ou</code>
 * <li> <code>MAY ( userPassword $ searchGuide $ seeAlso $ businessCategory $</code>
 * <li> <code>x121Address $ registeredAddress $ destinationIndicator $</code>
 * <li> <code>preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $</code>
 * <li> <code>telephoneNumber $ internationaliSDNNumber $</code>
 * <li> <code>facsimileTelephoneNumber $ street $ postOfficeBox $ postalCode $</code>
 * <li> <code>postalAddress $ physicalDeliveryOfficeName $ st $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * <p/>
 * 2. ftOrgUnit Structural objectclass is used to store the OrgUnit internal id.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code> objectclass	( 1.3.6.1.4.1.38088.2.6</code>
 * <li> <code>NAME 'ftOrgUnit'</code>
 * <li> <code>DESC 'Fortress OrgUnit Class'</code>
 * <li> <code>SUP organizationalunit</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * 3. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.
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
 * @author smckinn
 * @created September 18, 2010
 */
@XmlRootElement(name = "fortOrgUnit")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "orgUnit", propOrder = {
    "children",
    "description",
    "id",
    "name",
    "parents",
    "type"
})
public class OrgUnit extends FortEntity
    implements java.io.Serializable
{
    /**
     * Maps to the location for a particular OrgUnit entity to either the User, {@code ou=OS-U}, or Permission, {@code ou=OS-P}, tree in ldap.
     *
     */
    public Type type;
    private String name;
    private String id;
    private String description;
    @XmlElement(nillable = true)
    private Set<String> parents;
    @XmlElement(nillable = true)
    private Set<String> children;

    /**
     * Default constructor is used by internal Fortress classes.
     */
    public OrgUnit()
    {
    }

    /**
     * Construct a OrgUnit entity with a given ou name.
     *
     * @param ou maps to same name on on 'organizationalUnit' object class.
     */
    public OrgUnit(String ou)
    {
        this.name = ou;
    }

    /**
     * Construct a OrgUnit entity with a given ou name and specified type - 'USER' or 'PERM'.
     *
     * @param ou   maps to same name on on 'organizationalUnit' object class.
     * @param type is used to determine which OrgUnit tree is being targeted - 'USER' or 'PERM'.
     */
    public OrgUnit(String ou, Type type)
    {
        this.name = ou;
        this.type = type;
    }

    /**
     * Get the name required attribute of the OrgUnit object
     *
     * @return attribute maps to 'ou' attribute on 'organizationalUnit' object class.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the required name attribute on the OrgUnit object
     *
     * @return name maps to 'ou' attribute on 'organizationalUnit' object class.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Return the internal id that is associated with OrgUnit.  This attribute is generated automatically
     * by Fortress when new OrgUnit is added to directory and is not known or changeable by external client.
     *
     * @return attribute maps to 'ftId' in 'ftOrgUnit' object class.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Set the internal Id that is associated with OrgUnit.  This method is used by DAO class and
     * is generated automatically by Fortress.  Attribute stored in LDAP cannot be changed by external caller.
     * This method can be used by client for search purposes only.
     *
     * @param id maps to 'ftId' in 'ftOrgUnit' object class.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Generate an internal Id that is associated with OrgUnit.  This method is used by DAO class and
     * is not available to outside classes.   The generated attribute maps to 'ftId' in 'ftOrgUnit' object class.
     */
    public void setId()
    {
        // generate a unique id that will be used as the rDn for this entry:
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
    }

    /**
     * The OrgUnit 'Type' attribute is required and used to specify which OrgUnit tree a particular entity is in reference to.
     */
    @XmlType(name = "type")
    @XmlEnum
    public enum Type
    {
        /**
         * Type {@link com.jts.fortress.rbac.User} nodes reside in User OU pool.
         */
        USER,
        /**
         * Type {@link com.jts.fortress.rbac.Permission} nodes reside in Perm OU pool.
         */
        PERM
    }

    /**
     * Return the type of OrgUnit for this entity.  This field is required for this entity.
     *
     * @return Type contains 'PERM' or 'USER'.
     */
    public Type getType()
    {
        return type;
    }

    /**
     * Get the type of OrgUnit for this entity.  This field is required for this entity.
     *
     * @param type contains 'PERM' or 'USER'.
     */
    public void setType(Type type)
    {
        this.type = type;
    }

    /**
     * Returns optional description that is associated with OrgUnit.  This attribute is validated but not constrained by Fortress.
     *
     * @return value that is mapped to 'description' in 'organizationalUnit' object class.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the optional description that is associated with OrgUnit.  This attribute is validated but not constrained by Fortress.
     *
     * @param description that is mapped to same name in 'organizationalUnit' object class.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Get the names of orgUnits that are parents (direct ascendants) of this orgUnit.
     * @return Set of parent orgUnit names assigned to this orgUnit.
     */
    public Set<String> getParents()
    {
        return parents;
    }

    /**
     * Set the names of orgUnit names that are parents (direct ascendants) of this orgUnit.
     * @param parents contains the Set of parent orgUnit names assigned to this orgUnit.
     */
    public void setParents(Set<String> parents)
    {
        this.parents = parents;
    }

    /**
     * Return the Set of child orgUnit names (direct descendants) of this orgUnit.
     * @return Set of child orgUnit names assigned to this orgUnit.
     */
    public Set<String> getChildren()
    {
        return children;
    }

    /**
     * Set the Set of child orgUnit names (direct descendants) of this orgUnit
     * @param children contains the Set of child orgUnit names assigned to this orgUnit.
     */
    public void setChildren(Set<String> children)
    {
        this.children = children;
    }

    /**
     * @param thatObj
     * @return
     */
    public boolean equals(Object thatObj)
    {
        if (this == thatObj) return true;
        if (this.getName() == null) return false;
        if (!(thatObj instanceof OrgUnit)) return false;
        OrgUnit thatOrg = (OrgUnit) thatObj;
        if (thatOrg.getName() == null) return false;
        return thatOrg.getName().equalsIgnoreCase(this.getName());
    }
}

