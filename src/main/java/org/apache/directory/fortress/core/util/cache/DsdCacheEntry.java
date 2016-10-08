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

import org.apache.directory.fortress.core.model.SDSet;

/**
 * Value object wraps {@link SDSet} for caching purposes.  This class also provides attributes that are used for
 * searching the DSD cache.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DsdCacheEntry
{
    private String name;
    private String member;
    private SDSet sdSet;
    private boolean empty;
    private String contextId;

    /**
     * Non-default contructor takes a {@link DsdCacheEntry#member} that maps to  {@link org.apache.directory.fortress.core.model.Role#name}, along
     * with a reference to {@link org.apache.directory.fortress.core.model.SDSet} and a boolean value to indicate if DSD not found for member, which indicates empty.
     *
     * @param member maps to {@link org.apache.directory.fortress.core.model.Role#name}
     * @param sdSet contains DSD entry.
     * @param empty if true, the DSD entry was not found for a corresponding Role.
     */
    public DsdCacheEntry(String member, SDSet sdSet, boolean empty)
    {
        this.sdSet = sdSet;
        this.member = member;
        this.empty = empty;
        this.contextId = sdSet.getContextId();
    }

    /**
     * To prevent repeated reads of the DSD's in the directory, Entries are added to the cache and marked empty.
     *
     * @return boolean if cache entry is 'empty'.
     */
    public boolean isEmpty()
    {
        return empty;
    }

    /**
     * To prevent repeated reads of the DSD's in the directory, Entries are added to the cache and marked empty.
     *
     * @param empty if the cache entry is 'empty'.
     */
    public void setEmpty(boolean empty)
    {
        this.empty = empty;
    }

    /**
     * Return the Role name this cache entry corresponds with.
     *
     * @return Role name.
     */
    public String getMember()
    {
        return member;
    }

    /**
     * Set the Role name this cache entry corresponds with.
     *
     * @param member Role name
     */
    public void setMember(String member)
    {
        this.member = member;
    }

    /**
     * Get the DSD for this cache entry.
     *
     * @return SDSet of type {@link org.apache.directory.fortress.core.model.SDSet.SDType#DYNAMIC}.
     */
    public SDSet getSdSet()
    {
        return sdSet;
    }

    /**
     * Set the DSD for this cache entry.
     *
     * @param sdSet reference to non-null {@link SDSet} of type {@link org.apache.directory.fortress.core.model.SDSet.SDType#DYNAMIC}.
     */
    public void setSdSet(SDSet sdSet)
    {
        this.sdSet = sdSet;
    }

    /**
     * Return the name that is used as the cache name for this entry.
     *
     * @return name for the Cache entry.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name to be used for this cache entry.
     *
     * @param name of the cache entry.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Set the contextId for this record.  The contextId is used for multi-tenancy to isolate data sets within a particular sub-tree within DIT
     *
     * @return value maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     */
    public String getContextId()
    {
        return contextId;
    }

    /**
     * Return the contextId associated with this record.  The contextId is used for multi-tenancy to isolate data sets within a particular sub-tree within DIT
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     */
    void setContextId(String contextId)
    {
        this.contextId = contextId;
    }
}