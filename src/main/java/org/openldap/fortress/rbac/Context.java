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
 * This class contains the Context id which is used as container for segregating data by customer within the LDAP Directory Information Tree.
 * <p/>
 *
 * @author Shawn McKinney
 */
public class Context
{
    private String name;
    private String description;


    /**
     * Generate instance of context.
     *
     * @param name        contains the id to use for sub-directory within the DIT.
     * @param description maps to 'description' attribute in 'organizationalUnit' object class.
     */
    public Context(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    /**
     * Default constructor used by {@link org.openldap.fortress.ant.FortressAntTask}
     */
    public Context()
    {
    }

    /**
     * Get the id to use for sub-directory within the DIT.  This attribute is required.
     *
     * @return name maps to 'dcObject' object class.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the id to use for sub-directory within the DIT.  This attribute is required.
     *
     * @param name maps to 'dcObject' object class.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the description for the context.  This value is not required or constrained
     * but is validated on reasonability.
     *
     * @return field maps to 'description' attribute on 'organizationalUnit'.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set the description for the context.  This value is not required or constrained
     * but is validated on reasonability.
     *
     * @param description maps to to 'description' attribute on 'organizationalUnit'.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
}
