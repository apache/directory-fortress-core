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


import org.openldap.fortress.rbac.AdminRole;

import java.util.StringTokenizer;

/**
 * Entity is used by custom Apache Ant task for special handling of collections.  This is necessary because the
 * Ant parser cannot deal with complex data attribute types.
 *
 * @author Shawn McKinney
 */
public class AdminRoleAnt extends AdminRole
{
    private String osPs;
    private String osUs;

    /**
     * Default constructor required for all Ant entities.
     *
     * @return String containing OSPs.
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
     * @return String containing OSUs.
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

