/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */
package us.jts.fortress.util.cache;

import us.jts.fortress.CfgRuntimeException;
import us.jts.fortress.GlobalErrIds;
import net.sf.ehcache.constructs.blocking.BlockingCache;

/**
 * Creates an instance of the {@link EhCacheImpl} object with a {@link Cache} facade.
 *
 * @author Shawn McKinney
 */
class CacheFactory
{
    private static final String CLS_NM = CacheFactory.class.getName();

    /**
     * Create and return a reference to {@link Cache} object.
     *
     * @return instance of {@link Cache}.
     */
    public static Cache createInstance(String name, net.sf.ehcache.CacheManager cacheManager)
    {
        net.sf.ehcache.Ehcache cache = cacheManager.getEhcache(name);
        if(cache == null)
        {
            String error = "createInstance cache: " + name + " is null";
            throw new CfgRuntimeException(GlobalErrIds.FT_CACHE_NOT_CONFIGURED, error);
        }
        BlockingCache blockingCache = new BlockingCache(cache);
        cacheManager.replaceCacheWithDecoratedCache(cache, blockingCache);
        return new EhCacheImpl(name, blockingCache);
    }
}
