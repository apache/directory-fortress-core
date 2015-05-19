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


import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;


/**
 * This Interface is implemented by cacheable Fortress objects and is used to wrap the caching implementation to provide isolation.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
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
    Object get( Object key ) throws CacheException;


    /**
     * Add a new entry to the cache.
     *
     * @param key name to be used for the entry.
     * @param value object that is stored.
     * @throws CacheException will wraps the implementation's exception.
     */
    void put( Object key, Object value ) throws CacheException;


    /**
     * Clear a cache entry for a given name.
     *
     * @param key name that entry is stored as.
     * @return boolean value will be false if entry not found and true if entry was found and removed.
     * @throws CacheException will wraps the implementation's exception.
     */
    boolean clear( Object key ) throws CacheException;


    /**
     * Remove all entries from the cache.
     *
     * @throws CacheException will wraps the implementation's exception.
     */
    void flush() throws CacheException;


    /**
     * Retrieve the Cache attribute
     *
     * @param attributeName the name of search attribute
     * @param <T> the type of search attribute
     * @return the search attribute
     * @throws CacheException will wraps the implementation's exception.
     */
    <T> Attribute<T> getSearchAttribute( String attributeName ) throws CacheException;


    /**
     * Create a search query for the cache.
     *
     * @return a new Query builder
     */
    Query createQuery();
}