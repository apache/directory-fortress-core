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


/**
 * Entity is used by custom Apache Ant task for special handling of collections.  This is necessary because the
 * Ant parser cannot deal with complex data attribute types.  The class extends a base entity.
 *
 * @author Shawn McKinney
 */
public class OrgUnitAnt extends OrgUnit
    implements java.io.Serializable
{
    private String typeName;
    /**
     * Return the type of OU in string format.
     *
     * @return String that represents static or dynamic relations.
     */
    public String getTypeName()
    {
        return typeName;
    }

    /**
     * Method accepts a String variable that maps to its parent's set type.
     *
     * @param typeName String value represents perm or user ou data sets.
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
        if (typeName != null && typeName.equalsIgnoreCase("PERM"))
        {
            setType(OrgUnit.Type.PERM);
        }
        else
        {
            setType(OrgUnit.Type.USER);
        }
    }
}

