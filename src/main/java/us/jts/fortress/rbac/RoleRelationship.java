/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This entity is used by en masse to communicate parent and child {@link us.jts.fortress.rbac.Role} information to the server.
 * <p/>
 * @author Shawn McKinney
 */
@XmlRootElement(name = "fortRoleRelationship")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "roleRelationship", propOrder = {
    "child",
    "parent"
})
public class RoleRelationship extends FortEntity
    implements java.io.Serializable
{
    private Role parent;
    private Role child;

    public Role getParent()
    {
        return parent;
    }

    public void setParent(Role parent)
    {
        this.parent = parent;
    }

    public Role getChild()
    {
        return child;
    }

    public void setChild(Role child)
    {
        this.child = child;
    }
}
