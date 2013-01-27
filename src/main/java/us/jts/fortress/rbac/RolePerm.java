package us.jts.fortress.rbac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This entity is used by en masse to communicate {@link us.jts.fortress.rbac.Role}, {@link us.jts.fortress.rbac.Permission} and {@link us.jts.fortress.rbac.Session} information to the server for access control decisions.
 * <p/>
 * @author Shawn McKinney
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