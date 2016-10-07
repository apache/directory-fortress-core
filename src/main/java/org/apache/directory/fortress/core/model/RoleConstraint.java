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

import org.apache.directory.fortress.core.util.Config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The role constraint object holds non date time constraints on user to role relationships.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement( name = "fortRoleConstraint" )
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "roleConstraint", propOrder = {
    "paSetName",
    "value",
    "type"
} )

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
        OTHER
    }

    private RCType type;
    private String value;
    private String paSetName;

    public RoleConstraint()
    {

    }

    public RoleConstraint(String value, RCType type, String paSetName)
    {
        this.type = type;
        this.value = value;
        this.paSetName = paSetName;
    }

    public RCType getType()
    {
        return type;
    }

    public void setType(RCType type)
    {
        this.type = type;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getPaSetName()
    {
        return paSetName;
    }

    public void setPaSetName(String paSetName)
    {
        this.paSetName = paSetName;
    }

    public String getRawData(UserRole uRole)
    {
        StringBuilder sb = new StringBuilder();
        String delimeter = Config.getInstance().getDelimiter();

        sb.append( uRole.getName() );
        sb.append( delimeter );
        sb.append( RC_TYPE_NAME );
        sb.append( delimeter );
        sb.append( type );
        sb.append( delimeter );
        sb.append( paSetName );
        sb.append( delimeter );
        sb.append( value );

        return sb.toString();
    }

}