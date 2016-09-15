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
 * This entity is used by {@link org.apache.directory.fortress.core.ant.FortressAntTask} to add {@link Permission} grants to
 * RBAC {@link Role}, or ARBAC {@link AdminRole}.
 * Can also be used to grant Permissions directly to {@link User}s.
 * This entity is used for Ant and Fortress Rest processing only.

 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "fortGrant")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "permGrant", propOrder = {
    "objName",
    "opName",
    "objId",
    "userId",
    "roleNm",
    "admin"
})
public class PermGrant extends FortEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** The permission object name */
    private String objName;
    
    /** The permission operation name */
    private String opName;
    
    /** The permission object ID */
    private String objId;
    
    /** The userId attribute from this entity */
    private String userId;
    
    /** The role name associated from this entity */
    private String roleNm;
    
    /** Tells if the entity is stored with administrative permissions */
    private boolean admin;

    /**
     * Return the permission object name.
     * @return maps to 'ftObjNm' attribute on 'ftOperation' object class.
     */
    public String getObjName()
    {
        return objName;
    }
    

    /**
     * Set the permission object name.
     * @param objName maps to 'ftObjNm' attribute on 'ftOperation' object class.
     */
    public void setObjName(String objName)
    {
        this.objName = objName;
    }
    

    /**
     * Return the permission object id.
     * @return maps to 'ftObjId' attribute on 'ftOperation' object class.
     */
    public String getObjId()
    {
        return objId;
    }
    

    /**
     * Set the permission object id.
     * @param objId maps to 'ftObjId' attribute on 'ftOperation' object class.
     */
    public void setObjId(String objId)
    {
        this.objId = objId;
    }
    

    /**
     * Return the permission operation name.
     * @return maps to 'ftOpNm' attribute on 'ftOperation' object class.
     */
    public String getOpName()
    {
        return opName;
    }
    

    /**
     * Set the permission operation name.
     * @param opName maps to 'ftOpNm' attribute on 'ftOperation' object class.
     */
    public void setOpName(String opName)
    {
        this.opName = opName;
    }
    

    /**
     * Get the userId attribute from this entity.
     *
     * @return maps to 'ftUsers' attribute on 'ftOperation' object class.
     */
    public String getUserId()
    {
        return userId;
    }

    
    /**
     * Set the userId attribute on this entity.
     *
     * @param userId maps to 'ftUsers' attribute on 'ftOperation' object class.
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    

    /**
     * Get the role name associated from this entity.
     *
     * @return maps to 'ftRoles' attribute on 'ftOperation' object class.
     */
    public String getRoleNm()
    {
        return roleNm;
    }
    

    /**
     * Set the role name associated with this entity.
     *
     * @param roleNm maps to 'ftRoles' attribute on 'ftOperation' object class.
     */
    public void setRoleNm(String roleNm)
    {
        this.roleNm = roleNm;
    }


    /**
     * If set to true entity will be stored in ldap subdirectory associated with administrative permissions {@link org.apache.directory.fortress.core.GlobalIds#ADMIN_PERM_ROOT}.
     * otherwise will be RBAC permissions {@link org.apache.directory.fortress.core.GlobalIds#PERM_ROOT}
     * @return boolean if administrative entity.
     */
    public boolean isAdmin()
    {
        return admin;
    }
    

    /**
     * Return boolean value that will be set to true if this entity will be stored in Administrative Permissions.
     * @param admin will be true if administrative entity.
     */
    public void setAdmin(boolean admin)
    {
        this.admin = admin;
    }


    /**
     * @see Object#toString()
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append( "PermGrant object: \n" );

        sb.append( "    roleNm :" ).append( roleNm ).append( '\n' );
        sb.append( "    objName :" ).append( objName ).append( '\n' );
        sb.append( "    objId :" ).append( objId ).append( '\n' );
        sb.append( "    userId :" ).append( userId ).append( '\n' );
        sb.append( "    opName :" ).append( opName ).append( '\n' );
        sb.append( "    isAdmin :" ).append( admin ).append( '\n' );

        return sb.toString();
    }
}