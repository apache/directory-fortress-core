/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.configuration;

import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.ClassUtil;
import com.jts.fortress.SecurityException;

/**
 * Creates an instance of the ConfigMgr object.
 * <p/>
 * The default implementation class is specified as {@link com.jts.fortress.constants.GlobalIds#CONFIG_DEFAULT_CLASS} but can be overridden by
 * adding the {@link com.jts.fortress.constants.GlobalIds#CONFIG_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author smckinn
 * @created February 5, 2011
 */
public class ConfigMgrFactory
{
    private static String configClassName = Config.getProperty(GlobalIds.CONFIG_IMPLEMENTATION);

    /**
     * Create and return a reference to {@link ConfigMgr} object.
     *
     * @return instance of {@link ConfigMgr}.
     * @throws com.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static ConfigMgr createInstance()
        throws SecurityException
    {
        if (configClassName == null || configClassName.compareTo("") == 0)
        {
            configClassName = GlobalIds.CONFIG_DEFAULT_CLASS;
        }
        return (ConfigMgr) ClassUtil.createInstance(GlobalIds.CONFIG_DEFAULT_CLASS);
    }
}