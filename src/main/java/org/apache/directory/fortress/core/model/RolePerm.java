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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This entity is used by Fortress Rest to communicate {@link org.apache.directory.fortress.core.model.Role}, {@link Permission} and {@link org.apache.directory.fortress.core.model.Session} information to the server for access control decisions.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "fortRolePerm")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rolePerm", propOrder = {
    "role",
    "perm"
})
public class RolePerm extends FortEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private Role role;
    private Permission perm;

    public Role getRole()
    {
        return role;
    }

    
    public void setRole(Role role)
    {
        this.role = role;
    }
    

    public Permission getPerm()
    {
        return perm;
    }

    
    public void setPerm(Permission perm)
    {
        this.perm = perm;
    }


    /**
     * @see Object#toString()
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append( "RolePerm object: \n" );

        sb.append( "    role :" ).append( role ).append( '\n' );
        sb.append( "    perm :" ).append( perm ).append( '\n' );

        return sb.toString();
    }
}