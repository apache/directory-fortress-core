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


import org.openldap.fortress.ldap.group.Group;
import org.openldap.fortress.rbac.SDSet;
import org.openldap.fortress.util.attr.AttrHelper;

import java.util.StringTokenizer;

/**
 * Entity is used by custom Apache Ant task for special handling of collections.  This is necessary because the
 * Ant parser cannot deal with complex data attribute types.  The class extends a base entity.
 *
 * @author Shawn McKinney
 */
public class GroupAnt extends Group
{
    private String members;
    private String groupProps;

    public String getUserProps()
    {
        return groupProps;
    }

    public void setGroupProps( String groupProps )
    {
        this.groupProps = groupProps;
        addProperties( AttrHelper.getProperties( groupProps, '=', "," ));
    }

    /**
     * Return the members as a String.
     *
     * @return String contain comma delimited list of members.
     */
    public String getGroupMembers()
    {
        return members;
    }

    /**
     * Accept a comma delimited list of members, iterate of each and call the setter on the parent class.
     *
     * @param members contains comma delimited set of members.
     */
    public void setGroupMembers(String members)
    {
        this.members = members;
        if (members != null)
        {
            StringTokenizer tkn = new StringTokenizer(members, ",");
            if (tkn.countTokens() > 0)
            {
                while (tkn.hasMoreTokens())
                {
                    String member = tkn.nextToken();

                    setMember( member );
                }
            }
        }
    }
}