/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.configuration.Config;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.ClassUtil;

/**
 * Creates an instance of the AccessMgr object.
 * <p/>
 * The default implementation class is specified as {@link com.jts.fortress.constants.GlobalIds#ACCESS_DEFAULT_CLASS} but can be overridden by
 * adding the {@link com.jts.fortress.constants.GlobalIds#ACCESS_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author smckinn
 * @created October 13, 2009
 */
public class AccessMgrFactory
{
    private static String accessClassName = Config.getProperty(GlobalIds.ACCESS_IMPLEMENTATION);


    /**
     * Create and return a reference to {@link com.jts.fortress.AccessMgr} object.
     *
     * @return instance of {@link com.jts.fortress.AccessMgr}.
     * @throws com.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static com.jts.fortress.AccessMgr createInstance()
        throws SecurityException
    {
        com.jts.fortress.AccessMgr accessMgr;
        if (accessClassName == null || accessClassName.compareTo("") == 0)
        {
            accessClassName = GlobalIds.ACCESS_DEFAULT_CLASS;
        }
        accessMgr = (com.jts.fortress.AccessMgr) ClassUtil.createInstance(accessClassName);
        return accessMgr;
    }
}