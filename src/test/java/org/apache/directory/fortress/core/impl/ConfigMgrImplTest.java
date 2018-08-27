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

import java.util.Properties;

import org.apache.directory.fortress.core.ConfigMgr;
import org.apache.directory.fortress.core.ConfigMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

public class ConfigMgrImplTest extends TestCase
{
    private static final String CLS_NM = ConfigMgrImplTest.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    public ConfigMgrImplTest( String name )
    {
        super( name );
    }

    public void testCrudProperties()
    {
    }

    public void testAddAbacConfig()
    {
        addConfig( "ADD ABAC RBAC CFG", ConfigTestData.ABAC_SAMPLE1 );
    }

    private void addConfig(String msg, String[][] cfgArrays )
    {
        LogUtil.logIt( msg );
        Properties props = new Properties();
        for(String[] cArray : cfgArrays )
        {
            props.setProperty( cArray[ConfigTestData.NAME_COL], cArray[ConfigTestData.VALUE_COL] );
        }
        try
        {
            ConfigMgr cMgr = getConfigMgr();
            cMgr.update( cfgArrays[0][ConfigTestData.REALM_COL], props);
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "addConfig caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }

    public void testDelAbacConfig()
    {
        delConfig( "DEL ABAC RBAC CFG", ConfigTestData.ABAC_SAMPLE1 );
    }

    private void delConfig(String msg, String[][] cfgArrays )
    {
        LogUtil.logIt( msg );
        Properties props = new Properties();
        for(String[] cArray : cfgArrays )
        {
            props.setProperty( cArray[ConfigTestData.NAME_COL], cArray[ConfigTestData.VALUE_COL] );
        }
        try
        {
            ConfigMgr cMgr = getConfigMgr();
            cMgr.delete( cfgArrays[0][ConfigTestData.REALM_COL], props );
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "delConfig caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }

    public static ConfigMgr getConfigMgr() throws SecurityException
    {
        return ConfigMgrFactory.createInstance( );
    }
}