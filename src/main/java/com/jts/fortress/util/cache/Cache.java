/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */
package com.jts.fortress.util.cache;

/**
 * This Interface is implemented by Fortress objects that can be cached.
 *
 * @author smckinn
 * @created March 9, 2012
 */
public interface Cache
{
    /**
     *
     * @param key
     * @return
     * @throws CacheException
     */
    public Object get(Object key) throws CacheException;

    /**
     *
     * @param key
     * @param value
     * @throws CacheException
     */
    public void put(Object key, Object value) throws CacheException;

    /**
     *
     * @param key
     * @return
     * @throws CacheException
     */
    public boolean clear(Object key) throws CacheException;

    /**
     *
     * @throws CacheException
     */
    public void flush() throws CacheException;
}