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

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.blocking.BlockingCache;

import org.apache.directory.fortress.core.CfgException;
import org.apache.directory.fortress.core.CfgRuntimeException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.util.ClassUtil;
import org.apache.directory.fortress.core.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is a facade and shields internal Fortress objects from specifics of the actual
 * cache implementation that is in use.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class CacheMgr
{
    private static final Logger LOG = LoggerFactory.getLogger( CacheMgr.class.getName() );
    private static final String EHCACHE_CONFIG_FILE = "ehcache.config.file";
    private CacheManager mEhCacheImpl;
    
    private static volatile CacheMgr sINSTANCE = null;
    
    /**
     * Create or return the fortress cache manager reference.
     * @return handle to the cache manager in effect for process.
     */
    public static CacheMgr getInstance()
    {
        try
        {
            if( sINSTANCE == null)
            {
                synchronized (CacheMgr.class)
                {
                    if( sINSTANCE == null){
                        sINSTANCE = new CacheMgr();
                    }
                }
            }
        }
        catch (Exception e)
        {
            String error = "CacheMgr.getInstance caught Exception=" + e.getMessage();
            LOG.error( error );
            throw new CfgRuntimeException( GlobalErrIds.FT_CACHE_NOT_CONFIGURED, error, e );
        }
        return sINSTANCE;
    }
    
    private void init()
    {
        // Use default name of 'ehcache.xml':
        String cacheConfig = Config.getInstance().getProperty( EHCACHE_CONFIG_FILE, "ehcache.xml" );
        try
        {
            // 1. Construct an instance of Ehcache's CacheManager object.
            // 2. Requires location of ehcache's config file as parameter.
            // 3. The CacheManager reference then gets stored as member variable of this class instance.
            mEhCacheImpl = new CacheManager( ClassUtil.resourceAsStream( cacheConfig ) );
        }
        catch(CfgException ce)
        {
            // The ehcache file cannot be located on this program's classpath.  Ehcache is required, throw runtime exception.
            LOG.error( "CfgException caught  initializing cacher=" + ce.getMessage());
            throw new CfgRuntimeException( GlobalErrIds.FT_CACHE_NOT_CONFIGURED, cacheConfig, ce );
        }
        catch(Exception e)
        {
            String error = "CacheMgr.init caught Exception=" + e.getMessage();
            LOG.error( error );
            throw new CfgRuntimeException( GlobalErrIds.FT_CACHE_NOT_CONFIGURED, error, e );
        }
    }

    /**
     * Private constructor.
     *
     */
    private CacheMgr()
    {
        init();
    }

    /**
     * Create a new reference to the ehcache cache implementation.
     *
     * @param cacheName contains the name of the cache to retrieve
     * @return reference to cache for specified object.
     */
    public Cache getCache( String cacheName )
    {
        Ehcache cache = mEhCacheImpl.getEhcache( cacheName );
        if(cache != null)
        {
            return new EhCacheImpl( cacheName, new BlockingCache(cache) );
        }
        else
        {
            return CacheFactory.createInstance( cacheName, mEhCacheImpl );
        }
    }

    /**
     * Used to clear all elements from all cache objects.
     *
     */
    public void clearAll()
    {
        mEhCacheImpl.clearAll();
    }
}
