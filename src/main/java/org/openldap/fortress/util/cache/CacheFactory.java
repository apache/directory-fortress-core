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
package org.openldap.fortress.util.cache;

import org.openldap.fortress.CfgRuntimeException;
import org.openldap.fortress.GlobalErrIds;
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
