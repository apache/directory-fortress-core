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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * This a grouping of {@link org.apache.directory.fortress.core.model.PermissionAttribute}.
 * A {@link org.apache.directory.fortress.core.model.Permission} can link to 0 to many Permission
 * Attribute Sets.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
/**
 * All entities (User, Role, Permission, Policy, SDSet, etc...) are used to carry data between Fortress's
 * layers starting with the (1) Manager layer down thru middle (2) Process layer and it's processing rules into
 * (3) DAO layer where persistence with the LDAP server occurs.  The clients must instantiate an Fortress entity before use
 * and must provide enough information to uniquely identity target record for reads.
 * <p>
 * The PermissionAttributeSet entity is a grouping of {@link org.apache.directory.fortress.core.model.PermissionAttribute}.
 * A {@link org.apache.directory.fortress.core.model.Permission} can link to 0 to many Permission
 * Attribute Sets.
 * <p>
 * <h4>PermissionAttribute Schema</h4>
 * <p>
 * The PermissionAttributeSet entity is a composite of 2 different LDAP Schema object classes:
 * <p>
 * 1. organizationalUnit Structural Object Class is used to store basic attributes like ou and description.
 * <pre>
 * ------------------------------------------
 * objectclass ( 2.5.6.5 NAME 'organizationalUnit'
 *  DESC 'RFC2256: an organizational unit'
 *  SUP top STRUCTURAL
 *  MUST ou
 *  MAY (
 *      userPassword $ searchGuide $ seeAlso $ businessCategory $
 *      x121Address $ registeredAddress $ destinationIndicator $
 *      preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $
 *      telephoneNumber $ internationaliSDNNumber $
 *      facsimileTelephoneNumber $ street $ postOfficeBox $ postalCode $
 *      postalAddress $ physicalDeliveryOfficeName $ st $ l $ description
 *  )
 * )
 * ------------------------------------------
 * </pre>
 * <p>
 * 2. The ftAttributeSet STRUCTURAL Object Class
 * <pre>
 * ------------------------------------------
 * objectclass ( ftObId:9
 * NAME 'ftAttributeSet'
 * DESC 'Fortress Permission Attribute Set Structural Object Class'
 * SUP organizationalunit STRUCTURAL
 * MUST (
 *      ftId $ ftPASet $ cn
 *  )
 * MAY (
 *      ftPA $ ftPASetType $ description
 *  )
 * )
 * ------------------------------------------
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement( name = "fortPermissionAttributeSet" )
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "permissionAttributeSet", propOrder = {
    "name",
    "attributes",
    "internalId",
    "description",
    "type"
} )

public class PermissionAttributeSet extends FortEntity implements Serializable
{

    /**
     * Default serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private Set<PermissionAttribute> attributes;
    private String internalId;
    private String description;
    private String type;
    @XmlTransient
    private String dn;


    public PermissionAttributeSet(String name)
    {
        this.name = name;
    }

    public PermissionAttributeSet()
    {

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Return the collection of optional Attributes that have been loaded into this entity.  This is stored as a
     * multi-occurring
     * attribute of ftPA entries on the 'ftAttributeSet' object class.
     *
     * @return Set containing the roles which maps to 'ftRoles' attribute in 'ftOperation' object class.
     */
    public Set<PermissionAttribute> getAttributes()
    {
        if ( this.attributes == null )
        {
            attributes = new HashSet<PermissionAttribute>();
        }

        return this.attributes;
    }


    /**
     * Set the collection of optional Attributes that have been loaded into this entity.  This is stored as a
     * multi-occurring
     * attribute of ftPAs on the 'ftOperation' object class.
     *
     * @param attributes maps to 'ftPA' attribute in 'ftOperation' object class.
     */
    public void setAttributes(Set<PermissionAttribute> attributes)
    {
        this.attributes = attributes;
    }

    public void setInternalId(String internalId)
    {
        this.internalId = internalId;
    }

    public String getInternalId()
    {
        return internalId;
    }

    public void setInternalId()
    {
        UUID uuid = UUID.randomUUID();
        this.internalId = uuid.toString();
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getDn()
    {
        return dn;
    }

    public void setDn(String dn)
    {
        this.dn = dn;
    }

}
