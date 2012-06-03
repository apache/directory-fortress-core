/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */
package com.jts.fortress.util.cache;

import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;

/**
 * This Interface is implemented by cacheable Fortress objects.
 *
 * @author Shawn McKinney
 * @created March 9, 2012
 */
public interface Cache
{
    /**
     * Retrieve an object from the cache.
     *
     * @param key
     * @return
     * @throws CacheException
     */
    public Object get(Object key) throws CacheException;

    /**
     * Place an object in the cache.
     *
     * @param key
     * @param value
     * @throws CacheException
     */
    public void put(Object key, Object value) throws CacheException;

    /**
     * Clear an object from the cache.
     *
     * @param key
     * @return
     * @throws CacheException
     */
    public boolean clear(Object key) throws CacheException;

    /**
     * Remove all entries from the cache.
     *
     * @throws CacheException
     */
    public void flush() throws CacheException;


    public <T> Attribute<T> getSearchAttribute(String attributeName) throws CacheException;

    public Query createQuery();
}