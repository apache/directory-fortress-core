/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.configuration.Config;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.ClassUtil;

/**
 * Creates an instance of the DelegatedAccessMgr object.
 * <p/>
 * The default implementation class is specified as {@link com.jts.fortress.constants.GlobalIds#DELEGATED_ACCESS_DEFAULT_CLASS} but can be overridden by
 * adding the {@link com.jts.fortress.constants.GlobalIds#DELEGATED_ACCESS_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author smckinn
 * @created December 3, 2010
 */
public class DelegatedAccessMgrFactory
{
    private static String accessClassName = Config.getProperty(GlobalIds.DELEGATED_ACCESS_IMPLEMENTATION);


    /**
     * Create and return a reference to {@link com.jts.fortress.DelegatedAccessMgr} object.
     *
     * @return instance of {@link com.jts.fortress.DelegatedAccessMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static com.jts.fortress.DelegatedAccessMgr createInstance()
        throws SecurityException
    {
        com.jts.fortress.DelegatedAccessMgr accessMgr;
        if (accessClassName == null || accessClassName.compareTo("") == 0)
        {
            accessClassName = GlobalIds.DELEGATED_ACCESS_DEFAULT_CLASS;
        }
        accessMgr = (com.jts.fortress.DelegatedAccessMgr) ClassUtil.createInstance(accessClassName);
        return accessMgr;
    }
}
