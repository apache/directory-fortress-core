/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * This entity is used by Fortress Rest to communicate parent and child {@link AdminRole} information to the server.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "fortAdminRoleRelationship")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adminrelationship", propOrder = {
    "child",
    "parent"
})
public class AdminRoleRelationship extends FortEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** The parent AdminRole */
    private AdminRole parent;
    
    /** The child AdminRole */
    private AdminRole child;

    public AdminRole getParent()
    {
        return parent;
    }

    public void setParent(AdminRole parent)
    {
        this.parent = parent;
    }

    public AdminRole getChild()
    {
        return child;
    }

    public void setChild(AdminRole child)
    {
        this.child = child;
    }


    /**
     * @see Object#toString()
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append( "AdminRoleRelationship object: \n" );

        sb.append( "    parent :" ).append( parent ).append( '\n' );
        sb.append( "    child :" ).append( child ).append( '\n' );

        return sb.toString();
    }
}
