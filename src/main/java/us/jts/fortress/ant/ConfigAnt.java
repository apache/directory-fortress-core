/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ant;


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

