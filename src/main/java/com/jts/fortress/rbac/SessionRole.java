package com.jts.fortress.rbac;

import com.jts.fortress.FortEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This entity is used by en masse to communicate {@link com.jts.fortress.rbac.Role} and {@link com.jts.fortress.rbac.Session} information to the server for access control decisions.
 * <p/>
 * @author smckinn
 * @created January 29, 2012
 */
@XmlRootElement(name = "fortSessionRole")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sessionRole", propOrder = {
    "role",
    "session"
})
public class SessionRole extends FortEntity
    implements java.io.Serializable
{
    private Session session;
    private Role role;

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
}