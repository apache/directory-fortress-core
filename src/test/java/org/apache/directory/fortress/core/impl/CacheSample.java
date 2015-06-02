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
package org.apache.directory.fortress.core.impl;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

import org.apache.directory.fortress.core.model.SDSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.util.cache.Cache;
import org.apache.directory.fortress.core.util.cache.CacheMgr;


/**
 * Created by IntelliJ IDEA.
 * User: smckinn
 * Date: 6/2/12
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class CacheSample
{
    private static final String CLS_NM = CacheSample.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final SdP sdP = new SdP();
    private Cache cache;


    private void initializeCache()
    {
        CacheMgr cacheManager = CacheMgr.getInstance();
        cache = cacheManager.getCache( "fortress.dsd" );
    }


    private void loadCache()
    {
        try
        {
            SDSet sdSet = new SDSet();
            sdSet.setType( SDSet.SDType.DYNAMIC );
            sdSet.setName( "" );
            List<SDSet> dsds = sdP.search( sdSet );
            if ( dsds != null )
            {
                for ( SDSet dsd : dsds )
                {
                    Set<String> members = dsd.getMembers();
                    if ( members != null )
                    {
                        for ( String member : members )
                        {
                            DsdCacheEntry entry = new DsdCacheEntry( member, dsd );
                            String key = dsd.getName() + ":" + member;
                            cache.put( key, entry );
                            LOG.info( "Add DSD key: " + key + " members: " + dsd.getMembers() + " to the cache" );
                        }
                    }
                }
            }
            else
            {
                LOG.info( "No records found for loading into test cache" );
            }
        }
        catch ( org.apache.directory.fortress.core.SecurityException se )
        {
            String error = " static initializer caught SecurityException=" + se.getMessage() + " rc="
                + se.getErrorId();
            LOG.error( error );
        }
    }


    void runTests()
    {
        loadCache();
        Attribute<String> member = cache.getSearchAttribute( "member" );
        Query query = cache.createQuery();
        query.includeKeys();
        query.includeValues();
        Set<String> roles = new HashSet<>();
        roles.add( "oamt17dsd1" );
        roles.add( "oamt17dsd4" );
        roles.add( "oamT13DSD6" );
        roles.add( "oamT16SDR7" );
        query.addCriteria( member.in( roles ) );
        Results results = query.execute();
        System.out.println( " Size: " + results.size() );
        System.out.println( "----Results-----\n" );
        Set<SDSet> resultSet = new HashSet<>();

        for ( Result result : results.all() )
        {
            DsdCacheEntry entry = ( DsdCacheEntry ) result.getValue();
            resultSet.add( entry.getSdSet() );
        }

        for ( SDSet sdSet : resultSet )
        {
            LOG.info( "Found SDSet: " + sdSet.getName() );
        }
    }


    /**
     * put your documentation comment here
     *
     * @param args
     */
    public static void main( String[] args )
    {
        LOG.info( "main Test #args=" + args.length );
        CacheSample sample = new CacheSample();
        sample.initializeCache();
        sample.runTests();
    }

    public class DsdCacheEntry
    {
        private String member;
        private SDSet sdSet;


        public DsdCacheEntry( String member, SDSet sdSet )
        {
            this.sdSet = sdSet;
            this.member = member;
        }


        public String getMember()
        {
            return member;
        }


        public void setMember( String member )
        {
            this.member = member;
        }


        public SDSet getSdSet()
        {
            return sdSet;
        }


        public void setSdSet( SDSet sdSet )
        {
            this.sdSet = sdSet;
        }
    }
}