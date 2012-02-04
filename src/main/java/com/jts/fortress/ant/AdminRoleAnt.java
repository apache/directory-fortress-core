/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;


import com.jts.fortress.arbac.AdminRole;

import java.util.StringTokenizer;

/**
 * Entity is used by custom Apache Ant task for special handling of collections.  This is necessary because the
 * Ant parser cannot deal with complex data attribute types.
 *
 * @author smckinn
 * @created December 19, 2010
 */
public class AdminRoleAnt extends AdminRole
{
    private String osPs;
    private String osUs;

    /**
     * Default constructor required for all Ant entities.
     *
     * @return
     */
    public String getOSPs()
    {
        return osPs;
    }

    /**
     * Set the list of Perm OUs as a comma delimited string.  This method will convert from that format to
     * the AdminRole native format which is collection of Strings.
     *
     * @param osPs
     */
    public void setOSPs(String osPs)
    {
        this.osPs = osPs;
        if (osPs != null)
        {
            // allow the setter to process comma delimited strings:
            StringTokenizer tkn = new StringTokenizer(osPs, ",");
            if (tkn.countTokens() > 0)
            {
                while (tkn.hasMoreTokens())
                {
                    String osP = tkn.nextToken();
                    setOsP(osP);                                                            /**
                 * Set the list of Perm OUs as a comma delimited string.  This method will convert from that format to
                 * the AdminRole native format which is collection of Strings.
                 * @param osPs
                 */

                }
            }
        }
    }

    /**
     * Return the comma delimited OU string.
     *
     * @return
     */
    public String getOSUs()
    {
        return osUs;
    }

    /**
     * Load an OU into the collection of OUs stored by collection.
     *
     * @param osUs contains OU name.
     */
    public void setOSUs(String osUs)
    {
        this.osUs = osUs;
        if (osUs != null)
        {
            // allow the setter to process comma delimited strings:
            StringTokenizer tkn = new StringTokenizer(osUs, ",");
            if (tkn.countTokens() > 0)
            {
                while (tkn.hasMoreTokens())
                {
                    String osU = tkn.nextToken();
                    setOsU(osU);
                }
            }
        }
    }
}

