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

import java.io.Serializable;
import java.util.Properties;

import org.apache.directory.fortress.core.ConfigMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.Configuration;
import org.apache.directory.fortress.core.util.VUtil;


/**
 * This Manager impl supplies CRUD methods used to manage properties stored within the ldap directory.
 * The Fortress config nodes are used to remotely share Fortress client specific properties between processes.
 * Fortress places no limits on the number of unique configurations that can be present at one time in the directory.
 * The Fortress client will specify the preferred cfg node by name via a property named, 
 * {@link org.apache.directory.fortress.core.GlobalIds#CONFIG_REALM}.
 * Each process using Fortress client is free to share an existing node with other processes or create its own unique config
 * instance using the methods within this class.<BR>
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class ConfigMgrImpl  extends Manageable implements ConfigMgr, Serializable
{
    private ConfigP cfgP;
    private static final String CLS_NM = ConfigMgrImpl.class.getName();

    public ConfigMgrImpl() 
    {
        cfgP = new ConfigP();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration add(Configuration cfg) throws SecurityException
    {
        String methodName = "add";
        setEntitySession( CLS_NM, methodName, cfg );
        assertContext( CLS_NM, methodName, cfg, GlobalErrIds.FT_CONFIG_NULL );
        VUtil.assertNotNull( cfg, GlobalErrIds.FT_CONFIG_NULL, CLS_NM + methodName );
        VUtil.assertNotNull( cfg.getName(), GlobalErrIds.FT_CONFIG_NAME_NULL, CLS_NM + methodName );
        return cfgP.add(cfg);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration update(Configuration cfg) throws SecurityException
    {
        String methodName = "update";
        setEntitySession( CLS_NM, methodName, cfg );
        assertContext( CLS_NM, methodName, cfg, GlobalErrIds.FT_CONFIG_NULL );
        VUtil.assertNotNull( cfg, GlobalErrIds.FT_CONFIG_NULL, CLS_NM + methodName );
        VUtil.assertNotNull( cfg.getName(), GlobalErrIds.FT_CONFIG_NAME_NULL, CLS_NM + methodName );
        return cfgP.update(cfg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateProperty(String name, String key, String value, String newValue) throws SecurityException
    {
        cfgP.updateProperty(name, key, value, newValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String name) throws SecurityException
    {
        cfgP.delete(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String name, Properties inProps) throws SecurityException
    {
        cfgP.delete(name, inProps);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration read(String name) throws SecurityException
    {
        return cfgP.read(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getIds(String name) throws SecurityException
    {
        return cfgP.readPosixIds(name);
    }
}
