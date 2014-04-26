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

package org.openldap.fortress.ldap.suffix;


public class Suffix
{
    private String dc;
    private String dc2;
    private String name;
    private String description;


    /**
     * Generate instance of suffix to be loaded as domain component ldap object.
     *
     * @param dc          top level domain component maps to 'dc' (i.e. 'com') attribute in 'dcObject' object class.
     * @param name        second level domain component name maps to attribute in 'dcObject' object class.
     * @param description maps to 'o' attribute in 'dcObject' object class.
     */
    public Suffix(String dc, String name, String description)
    {
        this.dc = dc;
        this.name = name;
        this.description = description;
    }

    /**
     * Default constructor used by {@link org.openldap.fortress.ant.FortressAntTask}
     */
    public Suffix()
    {
    }

    /**
     * Get the second level qualifier on the domain component.  This attribute is required.
     *
     * @return name maps to 'dcObject' object class.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the second level qualifier on the domain component.  This attribute is required.
     *
     * @param name maps to 'dcObject' object class.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the description for the domain component.  This value is not required or constrained
     * but is validated on reasonability.
     *
     * @return field maps to 'o' attribute on 'dcObject'.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set the description for the domain component.  This value is not required or constrained
     * but is validated on reasonability.
     *
     * @param description maps to 'o' attribute on 'dcObject'.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Get top level domain component specifier, i.e. dc=com.  This attribute is required.
     *
     * @return dc maps to 'dc' in 'dcObject' object class.
     */
    public String getDc()
    {
        return dc;
    }

    /**
     * Set top level domain component specifier, i.e. dc=com.  This attribute is required.
     *
     * @param dc maps to 'dc' in 'dcObject' object class.
     */
    public void setDc(String dc)
    {
        this.dc = dc;
    }

    /**
     * Get top level domain component specifier, i.e. dc=com for a three part dc structure.  This attribute is optional.
     *
     * @return dc maps to 'dc' in 'dcObject' object class.
     */
    public String getDc2()
    {
        return dc2;
    }

    /**
     * Get top level domain component specifier, i.e. dc=com for three part dc structure.  This attribute is optional.
     *
     * @return dc maps to 'dc' in 'dcObject' object class.
     */
    public void setDc2( String dc2 )
    {
        this.dc2 = dc2;
    }
}

