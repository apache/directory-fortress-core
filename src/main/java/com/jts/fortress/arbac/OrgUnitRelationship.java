/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */
package com.jts.fortress.arbac;

import com.jts.fortress.FortEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This entity is used by en masse to communicate parent and child {@link com.jts.fortress.arbac.OrgUnit} information to the server.
 * <p/>
 * @author Shawn McKinney
 * @created January 21, 2012
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

