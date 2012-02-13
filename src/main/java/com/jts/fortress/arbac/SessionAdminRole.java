package com.jts.fortress.arbac;

import com.jts.fortress.FortEntity;
import com.jts.fortress.rbac.Session;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This entity is used by en masse to communicate {@link com.jts.fortress.rbac.UserRole} and {@link com.jts.fortress.rbac.Session} information to the server for access control decisions.
 * <p/>
 * @author smckinn
 * @created January 29, 2012
 */
@XmlRootElement(name = "fortSessionAdminRole")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sessionAdminRole", propOrder = {
    "adminRole",
    "session"
})
public class SessionAdminRole extends FortEntity
    implements java.io.Serializable
{
    private Session session;
    private AdminRole adminRole;

    public Session getSession()
    {
        return session;
    }

    public void setSession(Session session)
    {
        this.session = session;
    }

    public AdminRole getAdminRole()
    {
        return adminRole;
    }

    public void setAdminRole(AdminRole adminRole)
    {
        this.adminRole = adminRole;
    }
}