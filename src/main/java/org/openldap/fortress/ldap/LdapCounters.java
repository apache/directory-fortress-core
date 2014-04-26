/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.openldap.fortress.ldap;


import java.util.concurrent.atomic.AtomicInteger;


/**
 *
 */
public class LdapCounters
{
    AtomicInteger readCtr = new AtomicInteger( 0 );
    AtomicInteger searchCtr = new AtomicInteger( 0 );
    AtomicInteger compareCtr = new AtomicInteger( 0 );
    AtomicInteger addCtr = new AtomicInteger( 0 );
    AtomicInteger modCtr = new AtomicInteger( 0 );
    AtomicInteger deleteCtr = new AtomicInteger( 0 );
    AtomicInteger bindCtr = new AtomicInteger( 0 );


    public void incrementSearch()
    {
        searchCtr.incrementAndGet();
    }


    public void incrementRead()
    {
        readCtr.incrementAndGet();
    }


    public void incrementCompare()
    {
        compareCtr.incrementAndGet();
    }


    public void incrementAdd()
    {
        addCtr.incrementAndGet();
    }


    public void incrementMod()
    {
        modCtr.incrementAndGet();
    }


    public void incrementDelete()
    {
        deleteCtr.incrementAndGet();
    }


    public void incrementBind()
    {
        bindCtr.incrementAndGet();
    }


    public long getSearch()
    {
        return searchCtr.intValue();
    }


    public long getRead()
    {
        return readCtr.intValue();
    }


    public long getCompare()
    {
        return compareCtr.intValue();
    }


    public long getAdd()
    {
        return addCtr.intValue();
    }


    public long getMod()
    {
        return modCtr.intValue();
    }


    public long getDelete()
    {
        return deleteCtr.intValue();
    }


    public long getBind()
    {
        return bindCtr.intValue();
    }
}