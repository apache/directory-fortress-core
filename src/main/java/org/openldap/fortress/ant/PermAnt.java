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

package org.openldap.fortress.ant;


import org.openldap.fortress.rbac.Permission;

import java.util.StringTokenizer;

/**
 * Entity is used by custom Apache Ant task for special handling of collections.  This is necessary because the
 * Ant parser cannot deal with complex data attribute types.  The class extends a base entity.
 *
 * @author Shawn McKinney
 */
public class PermAnt extends Permission
{
    private String antRoles;

    /**
     * Return the roles as a String.
     *
     * @return String contains a comma delimited set of role names assigned to permission.
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

