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
package org.apache.directory.fortress.core.rest;

import java.util.Properties;

import org.apache.directory.fortress.core.ConfigMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.Configuration;
import org.apache.directory.fortress.core.model.FortRequest;
import org.apache.directory.fortress.core.model.FortResponse;
import org.apache.directory.fortress.core.model.Props;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * This Manager impl supplies CRUD methods used to manage properties stored within the ldap directory using HTTP access to Fortress Rest server.
 * The Fortress config nodes are used to remotely share Fortress client specific properties between processes.
 * Fortress places no limits on the number of unique configurations that can be present at one time in the directory.
 * The Fortress client will specify the preferred cfg node by name via a property named, {@link org.apache.directory.fortress.core.GlobalIds#CONFIG_REALM}.
 * Each process using Fortress client is free to share an existing node with other processes or create its own unique config
 * instance using the methods within this class.<BR>
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class ConfigMgrRestImpl implements ConfigMgr
{
    private static final String CLS_NM = ConfigMgrRestImpl.class.getName();


    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration add(Configuration cfg) throws SecurityException
    {
        VUtil.assertNotNull( cfg, GlobalErrIds.FT_CONFIG_NULL, CLS_NM + ".add" );
        Configuration retCfg;
        FortRequest request = RestUtils.getRequest( GlobalIds.HOME );
        request.setEntity( cfg );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.CFG_ADD );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retCfg = ( Configuration ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retCfg;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration update(Configuration cfg) throws SecurityException
    {
        VUtil.assertNotNull( cfg, GlobalErrIds.FT_CONFIG_NULL, CLS_NM + ".update" );
        Configuration retCfg;
        FortRequest request = RestUtils.getRequest( GlobalIds.HOME );
        request.setEntity( cfg );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.CFG_UPDATE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retCfg = ( Configuration ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retCfg;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateProperty(String name, String key, String value, String newValue) throws SecurityException
    {
        throw new java.lang.UnsupportedOperationException();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String name) throws SecurityException
    {
        VUtil.assertNotNull(name, GlobalErrIds.FT_CONFIG_NAME_NULL, CLS_NM + ".deleteProp");
        FortRequest request = new FortRequest();
        request.setValue(name);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.CFG_DELETE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String name, Properties inProperties) throws SecurityException
    {
        VUtil.assertNotNull(name, GlobalErrIds.FT_CONFIG_NAME_NULL, CLS_NM + ".delete");
        VUtil.assertNotNull(inProperties, GlobalErrIds.FT_CONFIG_PROPS_NULL, CLS_NM + ".delete");
        FortRequest request = new FortRequest();
        Props inProps = RestUtils.getProps(inProperties);
        request.setEntity(inProps);
        request.setValue(name);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.CFG_DELETE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration read(String name) throws SecurityException
    {
        VUtil.assertNotNull(name, GlobalErrIds.FT_CONFIG_NAME_NULL, CLS_NM + ".readRole");
        Configuration retCfg;
        FortRequest request = new FortRequest();
        request.setValue(name);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.CFG_READ);
        FortResponse response = RestUtils.unmarshall(szResponse);
        Props props;
        if (response.getErrorCode() == 0)
        {
            retCfg = (Configuration) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retCfg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getIds(String name) throws SecurityException
    {
        throw new UnsupportedOperationException( "not implemented" );
    }
}