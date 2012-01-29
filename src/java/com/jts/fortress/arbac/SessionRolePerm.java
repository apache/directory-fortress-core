package com.jts.fortress.arbac;

import com.jts.fortress.rbac.Permission;
import com.jts.fortress.rbac.Role;
import com.jts.fortress.rbac.Session;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This entity is used by en masse to communicate {@link com.jts.fortress.rbac.Role}, {@link com.jts.fortress.rbac.Permission} and {@link com.jts.fortress.rbac.Session} information to the server for access control decisions.
 * <p/>
 * @author smckinn
 * @created January 29, 2012
 */
@XmlRootElement(name = "fortSessionRolePerm")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sessionRolePerm", propOrder = {
    "role",
    "perm",
    "session"
})
public class SessionRolePerm
    implements java.io.Serializable
{
    private Session session;
    private Role role;
    private Permission perm;

    public Session getSession()
    {
        return session;
    }

    public void setSession(Session session)
    {
        this.session = session;
    }

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