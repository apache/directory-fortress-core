/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * This entity is used by en masse to communicate parent and child {@link com.jts.fortress.rbac.Role} information to the server.
 * <p/>
 * @author smckinn
 * @created January 21, 2012
 */
@XmlRootElement(name = "fortRoleRelationship")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "roleRelationship", propOrder = {
    "child",
    "parent"
})
public class RoleRelationship
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
