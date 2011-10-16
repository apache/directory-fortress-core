/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;


import com.jts.fortress.rbac.User;
import com.jts.fortress.rbac.UserRole;

import java.util.StringTokenizer;

/**
 * Entity is used by custom Apache Ant task for special handling of collections.  This is necessary because the
 * Ant parser cannot deal with complex data attribute types.  The class extends a base entity.
 *
 * @author smckinn
 * @created November 20, 2010
 */
public class UserAnt extends User
{
    private String antRoles;

    /**
     * Return a comma delimited string that contains the User's roles.
     *
     * @return variable contains a comma delimited collection of role names.
     */
    public String getAntRoles()
    {
        return antRoles;
    }

    /**
     * Accept a comma delimited String containing a list of Roles to be granted to a user.  This function
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
                UserRole ur = new UserRole(rTkn);
                setRole(ur);
            }
        }
    }
}