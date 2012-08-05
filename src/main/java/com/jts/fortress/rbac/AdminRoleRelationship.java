/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */
package com.jts.fortress.rbac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This entity is used by en masse to communicate parent and child {@link AdminRole} information to the server.
 * <p/>
 * @author Shawn McKinney
 * @created January 28, 2012
 */
@XmlRootElement(name = "fortAdminRoleRelationship")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adminrelationship", propOrder = {
    "child",
    "parent"
})
public class AdminRoleRelationship extends FortEntity
    implements java.io.Serializable
{
    private AdminRole parent;
    private AdminRole child;

    public AdminRole getParent()
    {
        return parent;
    }

    public void setParent(AdminRole parent)
    {
        this.parent = parent;
    }

    public AdminRole getChild()
    {
        return child;
    }

    public void setChild(AdminRole child)
    {
        this.child = child;
    }
}