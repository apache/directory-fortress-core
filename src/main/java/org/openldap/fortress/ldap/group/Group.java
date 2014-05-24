/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */

package org.openldap.fortress.ldap.group;


import org.openldap.fortress.rbac.FortEntity;
import org.openldap.fortress.rbac.Props;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

@XmlRootElement(name = "fortGroup")
@XmlAccessorType( XmlAccessType.FIELD)
@XmlType(name = "group", propOrder =
    {
        "name",
        "description",
        "protocol",
        "members",
        "props",
    })
public class Group extends FortEntity implements Serializable
{
    private String name;
    private String description;
    private String protocol;
    private List<String> members;
    private Props props = new Props();

    /**
     * Default constructor used by {@link org.openldap.fortress.ant.FortressAntTask}
     */
    public Group()
    {
    }

    /**
     * Generate instance of group to be loaded as ldap object.
     *
     * @param name        maps to 'cn' attribute in group object class.
     */
    public Group( String name )
    {
        this.name = name;
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
    public void setName(String name)
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
    public void setDescription(String description)
    {
        this.description = description;
    }

    public Props getProps()
    {
        return props;
    }

    public void setProps( Props props )
    {
        this.props = props;
    }

    public void setMember( String dn )
    {
        if ( members == null )
        {
            members = new ArrayList<>();
        }
        members.add( dn );
    }

    public List<String> getMembers()
    {
        return members;
    }

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
     * Add new collection of name/value pairs to attributes associated with PermObj.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @param props contains collection of name/value pairs and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     */
    public void addProperties( Properties props )
    {
        if ( props != null )
        {
            for ( Enumeration e = props.propertyNames(); e.hasMoreElements(); )
            {
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                String key = ( String ) e.nextElement();
                String val = props.getProperty( key );
                addProperty( key, val );
            }
        }
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
            //int size = props.size();
            for ( Props.Entry entry : props )
            {
                String key = entry.getKey();
                String val = entry.getValue();
                properties.setProperty( key, val );
            }
        }
        return properties;
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
}

