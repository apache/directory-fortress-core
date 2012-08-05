package com.jts.fortress.rbac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This entity is used by en masse to communicate {@link com.jts.fortress.rbac.Role}, {@link com.jts.fortress.rbac.Permission} and {@link com.jts.fortress.rbac.Session} information to the server for access control decisions.
 * <p/>
 * @author Shawn McKinney
 * @created January 29, 2012
 */
@XmlRootElement(name = "fortRolePerm")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rolePerm", propOrder = {
    "role",
    "perm"
})
public class RolePerm extends FortEntity
    implements java.io.Serializable
{
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
}