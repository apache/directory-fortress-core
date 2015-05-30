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
package org.apache.directory.fortress.core.ldap;


import java.util.concurrent.atomic.AtomicInteger;


/**
 * This class handles simple counters that correspond to ldap operations.
 *
 */
public class LdapCounters
{
    private AtomicInteger readCtr = new AtomicInteger( 0 );
    private AtomicInteger searchCtr = new AtomicInteger( 0 );
    private AtomicInteger compareCtr = new AtomicInteger( 0 );
    private AtomicInteger addCtr = new AtomicInteger( 0 );
    private AtomicInteger modCtr = new AtomicInteger( 0 );
    private AtomicInteger deleteCtr = new AtomicInteger( 0 );
    private AtomicInteger bindCtr = new AtomicInteger( 0 );


    /**
     * Increment the search counter.
     */
    public void incrementSearch()
    {
        searchCtr.incrementAndGet();
    }


    /**
     * Increment the read counter.
     */
    public void incrementRead()
    {
        readCtr.incrementAndGet();
    }


    /**
     * Increment the compare counter.
     */
    public void incrementCompare()
    {
        compareCtr.incrementAndGet();
    }


    /**
     *  Increment the add counter.
     */
    public void incrementAdd()
    {
        addCtr.incrementAndGet();
    }


    /**
     *  Increment the mod counter.
     */
    public void incrementMod()
    {
        modCtr.incrementAndGet();
    }


    /**
     *  Increment the delete counter.
     */
    public void incrementDelete()
    {
        deleteCtr.incrementAndGet();
    }

    /**
     *  Increment the bind counter.
     */

    public void incrementBind()
    {
        bindCtr.incrementAndGet();
    }


    /**
     * Return the search counter.
     * @return long containing search.
     */
    public long getSearch()
    {
        return searchCtr.intValue();
    }


    /**
     * Return the read counter.
     * @return long containing read.
     */
    public long getRead()
    {
        return readCtr.intValue();
    }


    /**
     * Return the compare counter.
     * @return long containing compare.
     */
    public long getCompare()
    {
        return compareCtr.intValue();
    }


    /**
     * Return the add counter.
     * @return long containing add.
     */
    public long getAdd()
    {
        return addCtr.intValue();
    }


    /**
     * Return the blank counter.
     * @return long containing blank.
     */
    public long getMod()
    {
        return modCtr.intValue();
    }


    /**
     * Return the delete counter.
     * @return long containing delete.
     */
    public long getDelete()
    {
        return deleteCtr.intValue();
    }


    /**
     * Return the bind counter.
     * @return long containing bind.
     */
    public long getBind()
    {
        return bindCtr.intValue();
    }
}