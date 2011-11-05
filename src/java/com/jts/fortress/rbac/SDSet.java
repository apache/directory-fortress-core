/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.FortEntity;
import com.jts.fortress.util.AlphabeticalOrder;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * <h4>Static Separation of Duties Schema</h4>
 * The Fortress SDSet entity is a composite of the following other Fortress structural and aux object classes:
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
 * 2. The RBAC Separation of Duties includes:
* <p/> Static Separation of Duties
 * <img src="../../../../images/RbacSSD.png">
 * <ul>
 * <li>  ---Static Separation of Duties Set-------
 * <li> <code>objectclass	( 1.3.6.1.4.1.38088.2.4</code>
 * <li> <code>NAME 'ftSSDSet'</code>
 * <li> <code>DESC 'Fortress Role Static Separation of Duty Set Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftSetName $ ftSetCardinality )</code>
 * <li> <code>MAY ( ftRoles $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * OR
 * <p/> Dynamic Separation of Duties
 * <img src="../../../../images/RbacDSD.png">
 * <ul>
 * <li>  ---Dynamic Separation of Duties Set-----
 * <li> <code>objectclass	( 1.3.6.1.4.1.38088.2.5</code>
 * <li> <code>NAME 'ftDSDSet'</code>
 * <li> <code>DESC 'Fortress Role Dynamic Separation of Duty Set Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftSetName $ ftSetCardinality )</code>
 * <li> <code>MAY ( ftRoles $ description ) )</code>
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
 * @created September 11, 2010
 */
public class SDSet extends FortEntity
    implements java.io.Serializable
{
    private String id;
    private String name;
    private String description;
    private Integer cardinality;
    private Map<String, String> members;
    private SDType type;


    /**
     * enum for SSD or DSD data sets.  Both nodes will be stored in the same LDAP container but use different
     * object classes.
     * SDType determines if 'ftSSDSet' or 'ftDSDSet' object class is used.
     */
    public enum SDType
    {
        /**
         * Static Separation of Duty data set.
         */
        STATIC,

        /**
         * Dynamic Separation of Duty data set.
         */
        DYNAMIC
    }

    /**
     * Get the required type of SD Set - 'STATIC' Or 'DYNAMIC'.
     *
     * @return type that maps to either 'ftSSDSet' or 'ftDSDSet' object class is used.
     */
    public SDType getType()
    {
        return type;
    }

    /**
     * Set the required type of SD Set - 'STATIC' Or 'DYNAMIC'.
     *
     * @param type maps to either 'ftSSDSet' or 'ftDSDSet' object class is used.
     */
    public void setType(SDType type)
    {
        this.type = type;
    }

    /**
     * Create a new, empty map that is used to load Role members.  This method is called by any class
     * that needs to create an SDSet map.
     *
     * @return Map that sorts members by alphabetical order.
     */
    public static Map<String, String> createMembers()
    {
        return new TreeMap<String, String>(new AlphabeticalOrder());
    }


    /**
     * Return the name of SDSet entity.  This field is required.
     *
     * @return attribute maps to 'cn' attribute on the 'organizationalRole' object class.
     */
    public String getName()
    {
        return this.name;
    }


    /**
     * Set the name of SDSet entity.  This field is required.
     *
     * @param name maps to 'cn' attribute on the 'organizationalRole' object class.
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Returns optional description that is associated with SDSet.  This attribute is validated but not constrained by Fortress.
     *
     * @return value that is mapped to 'description' in 'organizationalrole' object class.
     */
    public String getDescription()
    {
        return this.description;
    }


    /**
     * Sets the optional description that is associated with SDSet.  This attribute is validated but not constrained by Fortress.
     *
     * @param description that is mapped to same name in 'organizationalrole' object class.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the internal id that is associated with SDSet.  This attribute is generated automatically
     * by Fortress when new SDSet is added to directory and is not known or changeable by external client.
     *
     * @return attribute maps to 'ftId' in either 'ftSSDSet' or 'ftDSDSet' object class.
     */
    public String getId()
    {
        return id;
    }


    /**
     * Generate an internal Id that is associated with SDSet.  This method is used by DAO class and
     * is not available to outside classes.   The generated attribute maps to 'ftId' in either 'ftSSDSet' or 'ftDSDSet' object class.
     */
    public void setId()
    {
        // generate a unique id that will be used as the rDn for this entry:
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();

        //UID iid = new UID();
        // assign the unique id to the internal id of the entity:
        //this.id = iid.toString();
    }


    /**
     * Set the internal Id that is associated with Role.  This method is used by DAO class and
     * is generated automatically by Fortress.  Attribute stored in LDAP cannot be changed by external caller.
     * This method can be used by client for search purposes only.
     *
     * @param id maps to 'ftId' in either 'ftSSDSet' or 'ftDSDSet' object class.
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the numeric value that reflects the membership cardinality for SDSet.  A value of '2' indicates
     * the Role membership is mutually exclusive amongst members.  A value of '3' indicates no more than two Roles
     * in set can be assigned to a single User (SSD) or activated within a single Session (DSD).  A value of '4' indicates
     * no more than three Roles may be used at a time, etc...
     *
     * @return attribute maps to 'ftSetCardinality' attribute in either 'ftSSDSet' or 'ftDSDSet' object class.
     */
    public Integer getCardinality()
    {
        return cardinality;
    }

    /**
     * Set the numeric value that reflects the membership cardinality for SDSet.  A value of '2' indicates
     * the Role membership is mutually exclusive amongst members.  A value of '3' indicates no more than two Roles
     * in set can be assigned to a single User (SSD) or activated within a single Session (DSD).  A value of '4' indicates
     * no more than three Roles may be used at a time, etc...
     *
     * @return attribute maps to 'ftSetCardinality' attribute in either 'ftSSDSet' or 'ftDSDSet' object class.
     */
    public void setCardinality(Integer cardinality)
    {
        this.cardinality = cardinality;
    }

    /**
     * Return the alphabetically sorted Map containing Role membership to SDSet.
     *
     * @return attribute maps to 'ftRoles' attribute in either 'ftSSDSet' or 'ftDSDSet' object class.
     */
    public Map<String, String> getMembers()
    {
        return members;
    }

    /**
     * Set an alphabetically sorted Map containing Role membership to SDSet.
     *
     * @param members attribute maps to 'ftRoles' attribute in either 'ftSSDSet' or 'ftDSDSet' object class.
     */
    public void setMembers(Map<String, String> members)
    {
        this.members = members;
    }

    /**
     * Add a member to an alphabetically sorted Map containing Role membership to SDSet.
     *
     * @param role attribute maps to 'ftRoles' attribute in either 'ftSSDSet' or 'ftDSDSet' object class.
     */
    public void addMember(String role)
    {
        if (this.members == null)
        {
            this.members = createMembers();
        }
        this.members.put(role, null);
    }

    /**
     * Remove a member from the alphabetically sorted Map containing Role membership to SDSet.
     *
     * @param role attribute maps to 'ftRoles' attribute in either 'ftSSDSet' or 'ftDSDSet' object class.
     */
    public void delMember(String role)
    {
        if (this.members == null)
        {
            return;
        }
        this.members.remove(role);
    }

    /**
     * Matches the name from two SDSet entities.
     *
     * @param thatObj contains an SDSet entity.
     * @return boolean indicating both objects contain matching SDSet names.
     */
    public boolean equals(Object thatObj)
    {
        if (this == thatObj)
        {
            return true;
        }
        if (this.getName() == null)
        {
            return false;
        }
        if (!(thatObj instanceof Role))
        {
            return false;
        }
        SDSet thatSet = (SDSet) thatObj;
        if (thatSet.getName() == null)
        {
            return false;
        }
        return thatSet.getName().equalsIgnoreCase(this.getName());
    }
}
