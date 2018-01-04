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
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;


/**
 * <h4>Static Separation of Duties Schema</h4>
 * The Fortress SDSet entity is a composite of the following other Fortress structural and aux object classes:
 * <p>
 * 1. organizationalRole Structural Object Class is used to store basic attributes like cn and description.
 * <pre>
 * ------------------------------------------
 * objectclass ( 2.5.6.8 NAME 'organizationalRole'
 *  DESC 'RFC2256: an organizational role'
 *  SUP top STRUCTURAL
 *  MUST cn
 *  MAY (
 *      x121Address $ registeredAddress $ destinationIndicator $
 *      preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $
 *      telephoneNumber $ internationaliSDNNumber $ facsimileTelephoneNumber $
 *      seeAlso $ roleOccupant $ preferredDeliveryMethod $ street $
 *      postOfficeBox $ postalCode $ postalAddress $
 *      physicalDeliveryOfficeName $ ou $ st $ l $ description
 *  )
 * )
 * ------------------------------------------
 * </pre>
 * <p>
 * 2. The RBAC Separation of14:14 Duties includes:
 * <p> 
 * Static Separation of Duties
 * <img src="../doc-files/RbacSSD.png" alt="">
 * <pre>
 * ------------------------------------------
 * Fortress Dynamic Separation of Duties Structural Object Class
 *  objectclass    ( 1.3.6.1.4.1.38088.2.5
 *  NAME 'ftDSDSet'
 *  DESC 'Fortress Role Dynamic Separation of Duty Set Structural Object Class'
 *  SUP organizationalrole
 *  STRUCTURAL
 *  MUST (
 *      ftId $
 *      ftSetName $
 *      ftSetCardinality
 *  )
 *  MAY (
 *      ftRoles $
 *      description
 *  )
 * )
 * ------------------------------------------
 * </pre>
 * <p>
 * OR
 * <p> 
 * Dynamic Separation of Duties
 * <img src="../doc-files/RbacDSD.png" alt="">
 * <pre>
 * ------------------------------------------
 * Fortress Static Separation of Duties Structural Object Class
 *  objectclass    ( 1.3.6.1.4.1.38088.2.4
 *  NAME 'ftSSDSet'
 *  DESC 'Fortress Role Static Separation of Duty Set Structural Object Class'
 *  SUP organizationalrole
 *  STRUCTURAL
 *  MUST (
 *      ftId $
 *      ftSetName $
 *      ftSetCardinality
 *  )
 *  MAY (
 *      ftRoles $
 *      description
 *  )
 *)
 * ------------------------------------------
 * </pre>
 * <p>
 * 3. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.
 * <pre>
 * ------------------------------------------
 * Fortress Audit Modification Auxiliary Object Class
 * objectclass ( 1.3.6.1.4.1.38088.3.4
 *  NAME 'ftMods'
 *  DESC 'Fortress Modifiers AUX Object Class'
 *  AUXILIARY
 *  MAY (
 *      ftModifier $
 *      ftModCode $
 *      ftModId
 *  )
 * )
 * ------------------------------------------
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "fortSet")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sdset", propOrder =
    {
        "name",
        "id",
        "description",
        "cardinality",
        "members",
        "type"
})
public class SDSet extends FortEntity implements Serializable, Comparable<SDSet>
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String description;
    private Integer cardinality;
    private Set<String> members;
    private SDType type;

    /**
     * enum for SSD or DSD data sets.  Both nodes will be stored in the same LDAP container but use different
     * object classes.
     * SDType determines if 'ftSSDSet' or 'ftDSDSet' object class is used.
     */
    @XmlType(name = "sdtype")
    @XmlEnum
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
    public void setType( SDType type )
    {
        this.type = type;
    }


    /**
     * Create a new, empty map that is used to load Role members.  This method is called by any class
     * that needs to create an SDSet set.
     *
     * @return Set that sorts members by alphabetical order.
     */
    private static Set<String> createMembers()
    {
        return new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
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
    public void setName( String name )
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
    public void setDescription( String description )
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
        // generate a unique id:
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
    }


    /**
     * Set the internal Id that is associated with Role.  This method is used by DAO class and
     * is generated automatically by Fortress.  Attribute stored in LDAP cannot be changed by external caller.
     * This method can be used by client for search purposes only.
     *
     * @param id maps to 'ftId' in either 'ftSSDSet' or 'ftDSDSet' object class.
     */
    public void setId( String id )
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
     * @param cardinality The membership cardinality for SDSet
     *
     */
    public void setCardinality( Integer cardinality )
    {
        this.cardinality = cardinality;
    }


    /**
     * Return the alphabetically sorted Set containing Role membership to SDSet.
     *
     * @return attribute maps to 'ftRoles' attribute in either 'ftSSDSet' or 'ftDSDSet' object class.
     */
    //@XmlJavaTypeAdapter(SetAdapter.class)
    public Set<String> getMembers()
    {
        return members;
    }


    /**
     * Set an alphabetically sorted Set containing Role membership to SDSet.
     *
     * @param members attribute maps to 'ftRoles' attribute in either 'ftSSDSet' or 'ftDSDSet' object class.
     */
    public void setMembers( Set<String> members )
    {
        this.members = members;
    }


    /**
     * Add a member to the set.
     *
     * @param member role name.
     */
    public void setMember( String member )
    {
        if ( this.members == null )
        {
            this.members = new HashSet<>();
        }
        this.members.add( member );
    }


    /**
     * Add a member to an alphabetically sorted Set containing Role membership to SDSet.
     *
     * @param role attribute maps to 'ftRoles' attribute in either 'ftSSDSet' or 'ftDSDSet' object class.
     */
    public void addMember( String role )
    {
        if ( this.members == null )
        {
            this.members = createMembers();
        }
        this.members.add( role );
    }


    /**
     * Remove a member from the alphabetically sorted Set containing Role membership to SDSet.
     *
     * @param role attribute maps to 'ftRoles' attribute in either 'ftSSDSet' or 'ftDSDSet' object class.
     */
    public void delMember( String role )
    {
        if ( this.members == null )
        {
            return;
        }
        this.members.remove( role );
    }


    public int compareTo( SDSet that )
    {
        return name.compareToIgnoreCase( that.getName() );
    }


    /**
     * Matches the name from two SDSet entities.
     *
     * @param thatObj contains an SDSet entity.
     * @return boolean indicating both objects contain matching SDSet names.
     */
    public boolean equals( Object thatObj )
    {
        if ( this == thatObj )
        {
            return true;
        }
        if ( this.getName() == null )
        {
            return false;
        }
        if ( ( thatObj instanceof Role ) )
        {
            return false;
        }
        SDSet thatSet = ( SDSet ) thatObj;
        if ( thatSet.getName() == null )
        {
            return false;
        }
        return thatSet.getName().equalsIgnoreCase( this.getName() );
    }


    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + ( name != null ? name.hashCode() : 0 );
        result = 31 * result + ( description != null ? description.hashCode() : 0 );
        result = 31 * result + ( cardinality != null ? cardinality.hashCode() : 0 );
        result = 31 * result + ( members != null ? members.hashCode() : 0 );
        result = 31 * result + ( type != null ? type.hashCode() : 0 );
        return result;
    }


    @Override
    public String toString()
    {
        return "SDSet{" +
            "name='" + name + '\'' +
            '}';
    }
}
