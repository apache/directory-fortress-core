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


import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.BlockingCache;
import net.sf.ehcache.constructs.blocking.LockTimeoutException;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;

import org.apache.directory.fortress.core.CfgRuntimeException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class provides cache functionality from <a href="http://ehcache.org//">Ehcache</a> provider.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class EhCacheImpl implements Cache
{
    private static final String CLS_NM = EhCacheImpl.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    private BlockingCache cache;
    private final String name;


    /**
     * Create an instance of a wrapped, singleton cache instance using Ehcache.
     *
     * @param name name for the cache instance.
     * @param blockingCache that is being wrapped.
     */
    EhCacheImpl( String name, BlockingCache blockingCache )
    {
        this.name = name;
        if ( blockingCache == null )
        {
            String error = " constructor cache: " + name + " is null";
            throw new CfgRuntimeException( GlobalErrIds.FT_CACHE_NOT_CONFIGURED, error );
        }
        this.cache = blockingCache;
    }


    /**
     * Given a key name, return the corresponding value.
     *
     * @param key is the name used to store the entry.
     * @return entry stored in the cache.
     * @throws CacheException in the event ehcache throws an exception it will be wrapped.
     */
    @Override
    public Object get( Object key ) throws CacheException
    {
        if ( cache == null )
        {
            String error = "get detected null cache name [" + name + "]";
            throw new CacheException( GlobalErrIds.FT_NULL_CACHE, error );
        }
        try
        {
            Element element = cache.get( key );
            if ( element != null )
            {
                return element.getObjectValue();
            }
            else
            {
                return null;
            }
        }
        catch (LockTimeoutException lte){
            LOG.warn("Lock timout exception for key {}", key);
            return null;
        }
        catch ( net.sf.ehcache.CacheException ce )
        {
            String error = "get cache name [" + name + "] key [" + key + "] caught CacheException="
                + ce.getMessage();
            throw new CacheException( GlobalErrIds.FT_CACHE_GET_ERR, error, ce );
        }
    }


    /**
     * Add a new entry to the cache.
     *
     * @param key name to be used for the entry.
     * @param value object that is stored.
     * @throws CacheException in the event ehcache throws an exception it will be wrapped.
     */
    @Override
    public void put( Object key, Object value ) throws CacheException
    {
        if ( cache == null )
        {
            String error = "put detected null cache name [" + name + "]";
            throw new CacheException( GlobalErrIds.FT_NULL_CACHE, error );
        }
        try
        {
            cache.put( new Element( key, value ) );
        }
        catch ( net.sf.ehcache.CacheException ce )
        {
            String error = "put cache name [" + name + "] key [" + key + "] caught CacheException="
                + ce.getMessage();
            throw new CacheException( GlobalErrIds.FT_CACHE_PUT_ERR, error, ce );
        }
    }


    /**
     * Clear a cache entry for a given name.
     *
     * @param key name that entry is stored as.
     * @return boolean value will be false if entry not found and true if entry was found and removed.
     * @throws CacheException in the event ehcache throws an exception it will be wrapped.
     */
    @Override
    public boolean clear( Object key ) throws CacheException
    {
        boolean result;
        if ( cache == null )
        {
            String error = "clear detected null cache name [" + name + "]";
            throw new CacheException( GlobalErrIds.FT_NULL_CACHE, error );
        }
        try
        {
            result = cache.remove( key );
        }
        catch ( net.sf.ehcache.CacheException ce )
        {
            String error = "clear cache name [" + name + "] key [" + key + "] caught CacheException="
                + ce.getMessage();
            throw new CacheException( GlobalErrIds.FT_CACHE_CLEAR_ERR, error, ce );
        }
        return result;
    }


    /**
     * Remove all entries from this cache.
     *
     * @throws CacheException in the event ehcache throws an exception it will be wrapped.
     */
    @Override
    public void flush() throws CacheException
    {
        if ( cache == null )
        {
            String error = "flush detected null cache name [" + name + "]";
            throw new CacheException( GlobalErrIds.FT_NULL_CACHE, error );
        }
        try
        {
            cache.removeAll();
        }
        catch ( net.sf.ehcache.CacheException ce )
        {
            String error = "flush cache name [" + name + "] caught CacheException=" + ce.getMessage();
            throw new CacheException( GlobalErrIds.FT_CACHE_FLUSH_ERR, error, ce );

        }
    }


    /**
     * Retrieve the Cache attribute
     *
     * @param attributeName the name of search attribute
     * @param <T> the type of search attribute
     * @return the search attribute
     * @throws CacheException in the event ehcache throws an exception it will be wrapped.
     */
    @Override
    public <T> Attribute<T> getSearchAttribute( String attributeName ) throws CacheException
    {
        if ( cache == null )
        {
            String error = "getSearchAttribute detected null cache name [" + name + "]";
            throw new CacheException( GlobalErrIds.FT_NULL_CACHE, error );
        }
        return this.cache.getSearchAttribute( attributeName );
    }


    /**
     * Create a search query builder for the cache.
     *
     * @return a new Query builder
     */
    @Override
    public Query createQuery()
    {
        if ( cache == null )
        {
            String error = "createQuery detected null cache name [" + name + "]";
            throw new CacheException( GlobalErrIds.FT_NULL_CACHE, error );
        }
        return this.cache.createQuery();
    }


    public void clear()
    {
        cache.flush();
    }
}
