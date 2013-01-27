/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ant;


import us.jts.fortress.rbac.SDSet;

import java.util.StringTokenizer;

/**
 * Entity is used by custom Apache Ant task for special handling of collections.  This is necessary because the
 * Ant parser cannot deal with complex data attribute types.  The class extends a base entity.
 *
 * @author Shawn McKinney
 */
public class SDSetAnt extends SDSet
{
    private String type;
    private String members;

    /**
     * Return the members as a String.
     *
     * @return String contain comma delimited list of members.
     */
    public String getSetMembers()
    {
        return members;
    }

    /**
     * Accept a comma delimited list of members, iterate of each and call the setter on the parent class.
     *
     * @param members contains comma delimited set of members.
     */
    public void setSetMembers(String members)
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
                    addMember(member);
                }
            }
        }
    }


    /**
     * Return the type of SD set in string format.
     *
     * @return String that represents static or dynamic relations.
     */
    public String getSetType()
    {
        return type;
    }

    /**
     * Method accepts a String variable that maps to its parent's set type.
     *
     * @param type String value represents static or dynamic set relations.
     */
    public void setSetType(String type)
    {
        this.type = type;
        if (type != null && type.equals("DYNAMIC"))
        {
            setType(SDSet.SDType.DYNAMIC);
        }
        else
        {
            setType(SDSet.SDType.STATIC);
        }
    }
}

