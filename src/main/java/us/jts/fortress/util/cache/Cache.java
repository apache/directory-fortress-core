/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */
package us.jts.fortress.util.cache;

import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;

/**
 * This Interface is implemented by cacheable Fortress objects and is used to wrap the caching implementation to provide isolation.
 *
 * @author Shawn McKinney
 */
public interface Cache
{
    /**
     * Given a key name, return the corresponding value.
     *
     * @param key is the name used to store the entry.
     * @return entry stored in the cache.
     * @throws CacheException will wraps the implementation's exception.
     */
    public Object get(Object key) throws CacheException;

    /**
     * Add a new entry to the cache.
     *
     * @param key name to be used for the entry.
     * @param value object that is stored.
     * @throws CacheException will wraps the implementation's exception.
     */
    public void put(Object key, Object value) throws CacheException;

    /**
     * Clear a cache entry for a given name.
     *
     * @param key name that entry is stored as.
     * @return boolean value will be false if entry not found and true if entry was found and removed.
     * @throws CacheException will wraps the implementation's exception.
     */
    public boolean clear(Object key) throws CacheException;

    /**
     * Remove all entries from the cache.
     *
     * @throws CacheException will wraps the implementation's exception.
     */
    public void flush() throws CacheException;

    /**
     * Retrieve the Cache attribute
     *
     * @param attributeName the name of search attribute
     * @param <T> the type of search attribute
     * @return the search attribute
     * @throws CacheException will wraps the implementation's exception.
     */
    public <T> Attribute<T> getSearchAttribute(String attributeName) throws CacheException;

    /**
     * Create a search query for the cache.
     *
     * @return a new Query builder
     */
    public Query createQuery();
}