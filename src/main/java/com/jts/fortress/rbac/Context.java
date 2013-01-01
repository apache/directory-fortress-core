/*
* Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
*/

package com.jts.fortress.rbac;


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
     * Default constructor used by {@link com.jts.fortress.ant.FortressAntTask}
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
