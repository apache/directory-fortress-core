/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;


/**
 * This entity contains the list of name/value pairs targeted for updating on server.
 *
 * @author smckinn
 * @created February 1, 2011
 */
public class ConfigAnt
{
    private String props;

    /**
     * Default constructor required for all Ant entities.
     *
     * @return
     */
    public ConfigAnt()
    {
    }


    /**
     * Get the properties as a String.
     *
     * @return
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

