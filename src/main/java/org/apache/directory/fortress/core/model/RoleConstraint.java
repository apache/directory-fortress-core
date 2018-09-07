/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.directory.fortress.core.model;

import java.io.Serializable;
import java.util.UUID;

import org.apache.directory.fortress.core.util.Config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * The role constraint object holds non date time constraints on user to role relationships.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement( name = "fortRoleConstraint" )
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "roleConstraint", propOrder =
{
    "id",
    "key",
    "value",
    "type",
} )

/**
 * This value object stores attributes associated with a User-Role assignment that are used to augment of constraint the usage of that particular Role.  It maps to the 'ftRC' attribute on ftUserAttrs aux object class, stored on the User.
 * Constraints can be used for various purposes.  For example, it can map to permission attribute sets, or, can constraint the activation of the Role, see {@link org.apache.directory.fortress.core.util.time.UserRoleConstraint}.
 */
public class RoleConstraint extends FortEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final String RC_TYPE_NAME = "type";

    /**
     * The type of role constraint.
     *
     * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
     */
    @XmlType( name = "rctype" )
    @XmlEnum
    public enum RCType
    {
        FILTER,
        USER,
        OTHER
    }

    private String id;
    private RCType type;
    private String value;
    private String key;
    @XmlTransient
    private String typeName;

    public RoleConstraint()
    {
    }

    public RoleConstraint(String id, String value, RCType type, String key)
    {
        this.id = id;
        this.type = type;
        this.value = value;
        this.key = key;
    }

    /**
     * Return the object id.
     *
     * @return
     */
    public String getId()
    {
        return id;
    }

    /**
     * Set the id.
     *
     * @param id
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Generate a unique, random id, set as the value of id.
     *
     */
    public void genId()
    {
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
    }

    /**
     * Return the type of the constraint.
     *
     * @return
     */
    public RCType getType()
    {
        return type;
    }

    /**
     * Set the constraint type.
     *
     * @param type
     */
    public void setType(RCType type)
    {
        this.type = type;
    }

    /**
     * Get the value of a particular constraint. For example, locale=North, has a value of 'North'.
     *
     * @return
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Set the objects value.
     *
     * @param value
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * The key associated with a particular constraint.  For example, locale=North, key is 'locale'.
     * @return
     */
    public String getKey()
    {
        return key;
    }

    /**
     * Set the key on a constraint.
     *
     * @param key
     */
    public void setKey(String key)
    {
        this.key = key;
    }

    /**
     * This is the actual value of data stored in the LDAP attribute ftRC.  It concatenates the object's values, separated by a delimiter.
     *
     * @param uRole
     * @return
     */
    public String getRawData(UserRole uRole)
    {
        StringBuilder sb = new StringBuilder();
        String delimeter = Config.getInstance().getDelimiter();

        sb.append( uRole.getName().toLowerCase() );
        sb.append( delimeter );
        sb.append( RC_TYPE_NAME );
        sb.append( delimeter );
        sb.append( type );
        sb.append( delimeter );
        sb.append( key );
        sb.append( delimeter );
        sb.append( value );
        sb.append( delimeter );
        // ID's not req'd for RoleConstraints of type 'USER':
        if( getType() != RCType.USER)
        {
            sb.append( id );
        }
        return sb.toString();
    }

    /**
     * Return the type of OU in string format.

     *
     * @return String that represents static or dynamic relations.
     */
    public String getTypeName()
    {
        return typeName;
    }

    /**
     * Method accepts a String variable that maps to its parent's set type.
     *
     * @param typeName String value represents perm or user ou data sets.
     */
    public void setTypeName( String typeName )
    {
        this.typeName = typeName;
        if ( typeName != null && typeName.equalsIgnoreCase( "FILTER" ) )
        {
            setType( RCType.FILTER );
        }
        else if ( typeName != null && typeName.equalsIgnoreCase( "USER" ) )
        {
            setType( RCType.USER );
        }
        else
        {
            setType( RCType.OTHER );
        }
    }

    @Override
    public String toString()
    {
        return "RoleConstraint{" +
            "type=" + type +
            ", key='" + key + '\'' +
            ", value='" + value + '\'' +
            ", id='" + id + '\'' +
            '}';
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + type.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + key.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        RoleConstraint that = ( RoleConstraint ) o;

        if ( id != null ? !id.equals( that.id ) : that.id != null )
        {
            return false;
        }
        if ( !key.equals( that.key ) )
        {
            return false;
        }
        if ( type != that.type )
        {
            return false;
        }
        if ( !value.equals( that.value ) )
        {
            return false;
        }

        return true;
    }
}