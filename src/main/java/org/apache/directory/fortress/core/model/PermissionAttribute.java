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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * All entities (User, Role, Permission, Policy, SDSet, etc...) are used to carry data between Fortress's
 * layers starting with the (1) Manager layer down thru middle (2) Process layer and it's processing rules into
 * (3) DAO layer where persistence with the LDAP server occurs.  The clients must instantiate an Fortress entity before use
 * and must provide enough information to uniquely identity target record for reads.
 * <p>
 * A Permission Attribute is used for attribute type permission filtering. Fortress merely stores this data, does
 * not enforce the attribute filtering. It is up to the client to do the attribute checks.
 * <p>
 * <h4>PermissionAttribute Schema</h4>
 * <p>
 * The PermissionAttribute entity is a composite of 2 different LDAP Schema object classes:
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
 * 2. The ftAttribute STRUCTURAL Object Class
 * <pre>
 * ------------------------------------------
 * Fortress Permission Attribute Structural Object Class
 * objectclass ( ftObId:10
 * NAME 'ftAttribute'
 * DESC 'Fortress Attribute Structural Object Class'
 * SUP organizationalrole STRUCTURAL
 * MUST (
 *      ftId $ ftPASet $ ftPA $ cn
 *  )
 *  MAY (
 *      ftPADataType $ ftPADefaultValue $ ftPADefaultStrategy $ ftPADefaultOperator $ ftPAValidVals $ description
 *  )
 * )
 * ------------------------------------------
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement( name = "fortPermissionAttribute" )
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "permissionAttribute", propOrder = {
    "attributeName",
    "defaultOperator",
    "dataType",
    "defaultValue",
    "defaultStrategy",
    "validValues",
    "internalId",
    "description"
} )

public class PermissionAttribute extends FortEntity implements Serializable
{

    /**
     * Default serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private String attributeName;
    private String defaultOperator;
    private String dataType;
    private String defaultValue;
    private String defaultStrategy;
    private List<String> validValues;
    @XmlTransient
    private String dn;
    private String internalId;
    private String description;

    public PermissionAttribute()
    {

    }

    public PermissionAttribute(String attributeName)
    {
        this.attributeName = attributeName;
    }

    /**
     * Checks that attribute names are equal
     */
    @Override
    public boolean equals(Object thatObj)
    {
        if ( this == thatObj )
        {
            return true;
        }

        if ( this.getAttributeName() == null )
        {
            return false;
        }

        if ( !( thatObj instanceof PermissionAttribute ) )
        {
            return false;
        }

        PermissionAttribute thatPermObj = ( PermissionAttribute ) thatObj;

        if ( thatPermObj.getAttributeName() == null )
        {
            return false;
        }

        return thatPermObj.getAttributeName().equalsIgnoreCase( this.getAttributeName() );
    }

    @Override
    public int hashCode()
    {
        int result = 31 * ( attributeName != null ? attributeName.hashCode() : 0 );
        return result;
    }

    public String getDefaultOperator()
    {
        return defaultOperator;
    }

    public void setDefaultOperator(String defaultOperator)
    {
        this.defaultOperator = defaultOperator;
    }

    public String getDataType()
    {
        return dataType;
    }

    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public String getDefaultStrategy()
    {
        return defaultStrategy;
    }

    public void setDefaultStrategy(String defaultStrategy)
    {
        this.defaultStrategy = defaultStrategy;
    }

    public List<String> getValidValues()
    {
        if ( validValues == null )
        {
            validValues = new ArrayList<String>();
        }
        return validValues;
    }

    public void setValidValues(List<String> validValues)
    {
        this.validValues = validValues;
    }

    public String getAttributeName()
    {
        return attributeName;
    }

    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }

    public String getDn()
    {
        return dn;
    }

    public void setDn(String dn)
    {
        this.dn = dn;
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

    public void setInternalId(String internalId)
    {
        this.internalId = internalId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

}