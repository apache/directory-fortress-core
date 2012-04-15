/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.configuration.Config;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.ClassUtil;

/**
 * Creates an instance of the AdminMgr object.
 * The factory allows deployments of Fortress override the default AdminMgrImpl component with another.
 * <p/>
 * The default class is specified as {@link com.jts.fortress.constants.GlobalIds#ADMIN_DEFAULT_CLASS} but can be overridden by
 * adding the {@link com.jts.fortress.constants.GlobalIds#ADMIN_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 * @created August 30, 2009
 */
public class AdminMgrFactory
{
    private static String adminClassName = Config.getProperty(GlobalIds.ADMIN_IMPLEMENTATION);

    /**
     * Create and return a reference to {@link com.jts.fortress.AdminMgr} object.
     *
     * @return instance of {@link com.jts.fortress.AdminMgr}.
     * @throws com.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static com.jts.fortress.AdminMgr createInstance()
        throws com.jts.fortress.SecurityException
    {
        com.jts.fortress.AdminMgr adminMgrs;
        if (adminClassName == null || adminClassName.compareTo("") == 0)
        {
            adminClassName = GlobalIds.ADMIN_DEFAULT_CLASS;
        }
        AdminMgr adminMgr = (com.jts.fortress.AdminMgr) ClassUtil.createInstance(adminClassName);
        return adminMgr;
    }
}

