/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */
package org.openldap.fortress.rbac;

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

