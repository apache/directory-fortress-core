/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */
package us.jts.fortress.rbac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This entity is used by en masse to communicate parent and child {@link OrgUnit} information to the server.
 * <p/>
 * @author Shawn McKinney
 */
@XmlRootElement(name = "fortOrgUnitRelationship")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "orgrelationship", propOrder = {
    "child",
    "parent"
})
public class OrgUnitRelationship extends FortEntity
    implements java.io.Serializable
{
    private OrgUnit parent;
    private OrgUnit child;

    public OrgUnit getParent()
    {
        return parent;
    }

    public void setParent(OrgUnit parent)
    {
        this.parent = parent;
    }

    public OrgUnit getChild()
    {
        return child;
    }

    public void setChild(OrgUnit child)
    {
        this.child = child;
    }
}

