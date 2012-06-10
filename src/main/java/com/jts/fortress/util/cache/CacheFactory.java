/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.util.cache;

import com.jts.fortress.ConfigurationRuntimeException;
import com.jts.fortress.constants.GlobalErrIds;

/**
 * Creates an instance of the {@link EhCacheImpl} object with a {@link Cache} facade.
 *
 * @author Shawn McKinney
 * @created March 9, 2012
 */
public class CacheFactory
{
    private static final String CLS_NM = CacheFactory.class.getName();

    /**
     * Create and return a reference to {@link Cache} object.
     *
     * @return instance of {@link Cache}.
     */
    public static Cache createInstance(String name, net.sf.ehcache.CacheManager cacheManager)
    {
        net.sf.ehcache.Cache cache = cacheManager.getCache(name);
        if(cache == null)
        {
            String error = CLS_NM + ".createInstance cache: " + name + " is null";
            throw new ConfigurationRuntimeException(GlobalErrIds.FT_CACHE_NOT_CONFIGURED, error);
        }
        return new EhCacheImpl(name, cache);
    }
}
