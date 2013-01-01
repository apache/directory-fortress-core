/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package com.jts.fortress.rbac;


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

