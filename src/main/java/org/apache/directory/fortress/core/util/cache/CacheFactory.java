/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core.util.cache;

import net.sf.ehcache.constructs.blocking.BlockingCache;

import org.apache.directory.fortress.core.CfgRuntimeException;
import org.apache.directory.fortress.core.GlobalErrIds;

/**
 * Creates an instance of the {@link EhCacheImpl} object with a {@link Cache} facade.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
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
        blockingCache.setTimeoutMillis(60000);
        
        cacheManager.replaceCacheWithDecoratedCache(cache, blockingCache);
        return new EhCacheImpl(name, blockingCache);
    }
}
