/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;


import com.jts.fortress.rbac.Permission;

import java.util.StringTokenizer;

/**
 * Entity is used by custom Apache Ant task for special handling of collections.  This is necessary because the
 * Ant parser cannot deal with complex data attribute types.  The class extends a base entity.
 *
 * @author smckinn
 * @created February 1, 2011
 */
public class PermAnt extends Permission
{
    private String antRoles;

    /**
     * Return the roles as a String.
     *
     * @return
     */
    public String getAntRoles()
    {
        return antRoles;
    }

    /**
     * Accept a comma delimited String containing a list of Roles to be granted to a permission.  This function
     * will parse the String and call the setter on its parent.
     *
     * @param antRoles contains a comma delimited set of role names.
     */
    public void setAntRoles(String antRoles)
    {
        this.antRoles = antRoles;
        // allow the setter to process comma delimited strings:
        StringTokenizer tkn = new StringTokenizer(antRoles, ",");
        if (tkn.countTokens() > 0)
        {
            while (tkn.hasMoreTokens())
            {
                String rTkn = tkn.nextToken();
                setRole(rTkn);
            }
        }
    }
}

