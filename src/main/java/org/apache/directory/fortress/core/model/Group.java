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


import org.apache.directory.fortress.core.util.PropUtil;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;


/**
 * All entities (User, Role, Permission, Policy, SDSet, etc...) are used to carry data between Fortress's
 * layers starting with the (1) Manager layer down thru middle (2) Process layer and it's processing rules into
 * (3) DAO layer where persistence with the LDAP server occurs.  The clients must instantiate an Fortress entity before use
 * and must provide enough information to uniquely identity target record for reads.
 * <p>
 * <h4>Group Schema</h4>
 * <p>
 * The Fortress Group entity is a composite of 2 different LDAP Schema object classes:
 * <p>
 * 1. groupOfNames Structural Object Class is used to manage groups within LDAP.
 * <pre>
 * ------------------------------------------
 * objectClasses: ( 2.5.6.9 NAME 'groupOfNames'
 * DESC 'RFC2256: a group of names (DNs)' SUP top STRUCTURAL
 * MUST (
 * member $ cn )
 * MAY
 * ( businessCategory $ seeAlso $ owner $ ou $ o $ description
 * )
 * )
 * ------------------------------------------
 * </pre>
 * <p>
 * 2. configGroup STRUCTURAL Object Class is used to store groups and their relationships to users or roles.
 * <pre>
 * ------------------------------------------
 * LDAP Configuration Group Structural Object Class
 * objectClass ( ftObId:8
 * NAME 'configGroup'
 * DESC 'LDAP Configuration Group'
 * S
 * SUP groupOfNames
 * MUST (
 * configProtocol $
 * ftType
 * )
 * MAY configParameter
 * )
 * ------------------------------------------
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "fortGroup")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "group", propOrder =
    {
        "name",
        "description",
        "protocol",
        "members",
        "props",
        "type",
        "roles"
})
public class Group extends FortEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private String protocol;
    private List<String> members;
    private Props props = new Props();
    @XmlTransient
    private boolean memberDn;
    private Type type;

    /**
     *  Auxiliary field used to store roles in UserRole format.
     */
    @XmlElement( nillable = true )
    private List<UserRole> roles = new ArrayList<>();

    /**
     * enum for User or Role data sets.  Both nodes may be stored in the same LDAP container.
     */
    @XmlType(name = "groupType")
    @XmlEnum
    public enum Type
    {
        /**
         * Entry contains a set of Users.
         */
        USER,

        /**
         * Entry contains a set of Roles.
         */
        ROLE
    }

    /**
     * Get the required type of Group - 'USER' Or 'ROLE'.
     *
     * @return type that determines what node maps to.
     */
    public Type getType()
    {
        return type;
    }

    /**
     * Set the required type of Group - 'USER' Or 'ROLE'.
     *
     * @param type determines what set the node contains.
     */
    public void setType( Type type )
    {
        this.type = type;
    }


    /**
     * Default constructor used by {@link org.apache.directory.fortress.core.ant.FortressAntTask} defaults to type USER.
     */
    public Group()
    {
        type = Type.USER;
    }

    /**
     * Constructor for base type.
     */
    public Group( Type type )
    {
        this.type = type;
    }

    /**
     * Generate instance of group to be loaded as ldap object.
     *
     * @param name        maps to 'cn' attribute in group object class.
     */
    public Group( String name )
    {
        this.name = name;
        type = Type.USER;
    }

    /**
     * Generate instance of group to be loaded as ldap object with node type.
     *
     * @param name        maps to 'cn' attribute in group object class.
     */
    public Group( String name, Type type )
    {
        this.name = name;
        this.type = type;
    }

    /**
     * Generate instance of group to be loaded as ldap object.
     *
     * @param name        maps to 'cn' attribute in group object class.
     * @param description maps to 'description' attribute in group object class.
     */
    public Group( String name, String description )
    {
        this.name = name;
        this.description = description;
        type = Type.USER;
    }


    /**
     * Generate instance of group to be loaded as ldap object with node type.
     *
     * @param name        maps to 'cn' attribute in group object class.
     * @param description maps to 'description' attribute in group object class.
     */
    public Group( String name, String description, Type type )
    {
        this.name = name;
        this.description = description;
        this.type = type;
    }


    /**
     * Get the second level qualifier on the domain component.  This attribute is required.
     *
     * @return name maps to 'dcObject' object class.
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set the second level qualifier on the domain component.  This attribute is required.
     *
     * @param name maps to 'dcObject' object class.
     */
    public void setName( String name )
    {
        this.name = name;
    }


    /**
     * Get the description for the domain component.  This value is not required or constrained
     * but is validated on reasonability.
     *
     * @return field maps to 'o' attribute on 'dcObject'.
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set the description for the domain component.  This value is not required or constrained
     * but is validated on reasonability.
     *
     * @param description maps to 'o' attribute on 'dcObject'.
     */
    public void setDescription( String description )
    {
        this.description = description;
    }


    /**
     * Get protocol qualifier for this entity.
     *
     * @return protocol.
     */
    public String getProtocol()
    {
        return protocol;
    }


    /**
     * Set the protocol qualifier for this entity.
     *
     * @param protocol contains protocol qualifier for this entity.
     */
    public void setProtocol( String protocol )
    {
        this.protocol = protocol;
    }


    /**
     * Add a single userId as member of this entity.
     *
     * @param userId
     */
    public void setMember( String userId )
    {
        if ( members == null )
        {
            members = new ArrayList<>();
        }
        members.add( userId );
    }


    /**
     * Return the members
     *
     * @return List of type String containing userIds.
     */
    public List<String> getMembers()
    {
        return members;
    }


    /**
     * Set a member on this entity using a comma delimited String.
     *
     * @param members String contains one or more userids in comma delimited format.
     */
    public void setMembersWithCsv( String members )
    {
        if ( members != null )
        {
            StringTokenizer tkn = new StringTokenizer( members, "," );
            if ( tkn.countTokens() > 0 )
            {
                while ( tkn.hasMoreTokens() )
                {
                    String member = tkn.nextToken();
                    setMember( member );
                }
            }
        }
    }


    /**
     * Set members onto this entity using a List of userIds.
     *
     * @param members List of type String contains userIds to be associated as members of this group.
     */
    public void setMembers( List<String> members )
    {
        this.members = members;
    }


    /**
     * Add name/value pair to list of properties associated with PermObj.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @param key   contains property name and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     * @param value
     */
    public void addProperty( String key, String value )
    {
        Props.Entry entry = new Props.Entry();
        entry.setKey( key );
        entry.setValue( value );
        this.props.getEntry().add( entry );
    }


    /**
     * Get a name/value pair attribute from list of properties associated with PermObj.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @param key contains property name and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     * @return value containing name/value pair that maps to 'ftProps' attribute in 'ftProperties' aux object class.
     */
    public String getProperty( String key )
    {
        List<Props.Entry> props = this.props.getEntry();
        Props.Entry keyObj = new Props.Entry();
        keyObj.setKey( key );

        String value = null;
        int indx = props.indexOf( keyObj );
        if ( indx != -1 )
        {
            Props.Entry entry = props.get( props.indexOf( keyObj ) );
            value = entry.getValue();
        }

        return value;
    }


    /**
     * Replace teh collection of name/value pairs to attributes associated with Group entity.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @param properties contains collection of name/value pairs and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     */
    public void setProperties( Properties properties )
    {
        if ( properties != null )
        {
            // reset the existing properties stored in this entity.
            props = new Props();

            for ( Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); )
            {
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                String key = ( String ) e.nextElement();
                String val = properties.getProperty( key );
                addProperty( key, val );
            }
        }
    }


    /**
     * Add new collection of name=value pairs to attributes associated with Group.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @param properties contains name=value pairs that are comma delmited.
     */
    public void setPropertiesWithCsv( String properties )
    {
        setProperties( PropUtil.getProperties( properties, '=', "," ) );
    }


    /**
     * Return the collection of name/value pairs to attributes associated with PermObj.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @return Properties contains collection of name/value pairs and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     */
    public Properties getProperties()
    {
        Properties properties = null;
        List<Props.Entry> props = this.props.getEntry();
        if ( props.size() > 0 )
        {
            properties = new Properties();
            for ( Props.Entry entry : props )
            {
                String key = entry.getKey();
                String val = entry.getValue();
                properties.setProperty( key, val );
            }
        }
        return properties;
    }


    public List<String> getPropList()
    {
        List<Props.Entry> props = this.props.getEntry();
        List<String> propList = null;
        if ( props.size() > 0 )
        {
            propList = new ArrayList<>();
            for ( Props.Entry entry : props )
            {
                String key = entry.getKey();
                String val = entry.getValue();
                String prop = key + "=" + val;
                propList.add( prop );
            }
        }
        return propList;
    }


    /**
     * Gets the value of the Props property.  This method is used by Fortress Core and Rest and should not be called by external programs.
     *
     * @return {@link Props }
     *
     */
    public Props getProps()
    {
        return props;
    }


    /**
     * Sets the value of the Props property.  This method is used by Fortress Core and Rest and should not be called by external programs.
     *
     * @param props
     *     allowed object is
     *     {@link Props }
     *
     */
    public void setProps( Props props )
    {
        this.props = props;
    }


    /**
     * Set if userDn's are loaded in dn format.
     *
     * @return true indicates members are in dn format.
     */
    public boolean isMemberDn()
    {
        return memberDn;
    }


    /**
     * Set to 'true' if members are in dn format.
     *
     * @param memberDn boolean value, set to 'true' if distinguished name (dn) format, 'false' if relative distinguished name (rdn) format.
     */
    public void setMemberDn( boolean memberDn )
    {
        this.memberDn = memberDn;
    }

    /**
     * List of roles for given groups if they were populated. Empty list otherwise.
     * @return
     */
    public List<UserRole> getRoles() {
        return roles;
    }

    /**
     * Setter for auxiliary 'roles' field.
     * @param roles list of roles to be set
     */
    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }


    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        Group group = ( Group ) o;

        if ( name == null )
        {
            return false;
        }

        return name.equals( group.name );
    }


    @Override
    public int hashCode()
    {
        int result = name.hashCode();
        result = 31 * result + ( description != null ? description.hashCode() : 0 );
        result = 31 * result + ( protocol != null ? protocol.hashCode() : 0 );
        result = 31 * result + ( members != null ? members.hashCode() : 0 );
        result = 31 * result + ( props != null ? props.hashCode() : 0 );
        return result;
    }


    @Override
    public String toString()
    {
        return "Group{" +
            "name='" + name + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}