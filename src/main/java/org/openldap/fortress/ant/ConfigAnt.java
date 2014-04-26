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


/**
 * This entity contains the list of name/value pairs targeted for updating on server.
 *
 * @author Shawn McKinney
 */
public class ConfigAnt
{
    private String props;

    /**
     * Default constructor required for all Ant entities.
     *
     */
    public ConfigAnt()
    {
    }


    /**
     * Get the properties as a String.
     *
     * @return String containing property name/values.
     */
    public String getProps()
    {
        return props;
    }

    /**
     * Set the properties as a String.
     *
     * @param szProps
     */
    public void setProps(String szProps)
    {
        this.props = szProps;
    }
}

