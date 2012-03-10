/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.util.cache;

import com.jts.fortress.configuration.Config;
import com.jts.fortress.constants.GlobalIds;

/**
 * Creates an instance of the AdminMgr object.
 * The factory allows deployments of Fortress override the default AdminMgrImpl component with another.
 * <p/>
 * The default class is specified as {@link com.jts.fortress.constants.GlobalIds#ADMIN_DEFAULT_CLASS} but can be overridden by
 * adding the {@link com.jts.fortress.constants.GlobalIds#ADMIN_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author smckinn
 * @created Mar 9, 2012
 */
public class CacheFactory
{
    private static String cacheClassName = Config.getProperty(GlobalIds.CACHE_IMPLEMENTATION);

    /**
     * Create and return a reference to {@link com.jts.fortress.util.cache.CacheMgr} object.
     *
     * @return instance of {@link Cache}.
     * @throws com.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static Cache createInstance(String name, net.sf.ehcache.CacheManager cacheManager)
    //    throws com.jts.fortress.SecurityException
    {
        com.jts.fortress.AdminMgr adminMgrs;
        if (cacheClassName == null || cacheClassName.compareTo("") == 0)
        {
            cacheClassName = GlobalIds.ADMIN_DEFAULT_CLASS;
        }
        return new EhCacheImpl(name, cacheManager.getCache(name));
    }
}
