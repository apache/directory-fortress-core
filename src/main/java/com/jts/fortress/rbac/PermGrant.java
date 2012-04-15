/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.FortEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This entity is used by {@link com.jts.fortress.ant.FortressAntTask} to add {@link com.jts.fortress.rbac.Permission} grants to
 * RBAC {@link com.jts.fortress.rbac.Role}, or ARBAC {@link com.jts.fortress.arbac.AdminRole}.
 * Can also be used to grant Permissions directly to {@link com.jts.fortress.rbac.User}s.
 * This entity is used for Ant and En Masse processing only.
 * <p/>

 * @author Shawn McKinney
 * @created July 22, 2010
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
public class PermGrant extends FortEntity
    implements java.io.Serializable
{
    private String objName;
    private String opName;
    private String objId;
    private String userId;
    private String roleNm;
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
     * If set to true entity will be stored in ldap subdirectory associated with administrative permissions {@link com.jts.fortress.constants.GlobalIds#ADMIN_PERM_ROOT}.
     * otherwise will be RBAC permissions {@link com.jts.fortress.constants.GlobalIds#PERM_ROOT}
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
}