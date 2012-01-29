package com.jts.fortress.rbac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This entity is used by en masse to communicate {@link com.jts.fortress.rbac.Permission} and {@link com.jts.fortress.rbac.Session} information to the server for access control decisions.
 * <p/>
 * @author smckinn
 * @created January 29, 2012
 */
@XmlRootElement(name = "fortSessionPerm")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sessionPerm", propOrder = {
    "permission",
    "session"
})
public class SessionPerm
    implements java.io.Serializable
{
    private Session session;
    private Permission permission;

    public Session getSession()
    {
        return session;
    }

    public void setSession(Session session)
    {
        this.session = session;
    }

    public Permission getPermission()
    {
        return permission;
    }

    public void setPermission(Permission permission)
    {
        this.permission = permission;
    }
}

