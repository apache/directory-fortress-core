package com.jts.fortress.rest;

/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

import com.jts.fortress.SecurityException;
import com.jts.fortress.configuration.ConfigMgr;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.rbac.Props;
import com.jts.fortress.util.attr.VUtil;

import java.util.Properties;

/**
 * This Manager impl supplies CRUD methods used to manage properties stored within the ldap directory using HTTP access to En Masse REST server.
 * The Fortress config nodes are used to remotely share Fortress client specific properties between processes.
 * Fortress places no limits on the number of unique configurations that can be present at one time in the directory.
 * The Fortress client will specify the preferred configuration node by name via a property named, {@link com.jts.fortress.constants.GlobalIds#CONFIG_REALM}.
 * Each process using Fortress client is free to share an existing node with other processes or create its own unique config
 * instance using the methods within this class.<BR>
 * <p/>
 * This object is thread safe.
 * <p/>
 *
 * @author Shawn McKinney
 * @created March 4, 2012
 */
public class ConfigMgrRestImpl implements ConfigMgr
{
    private static final String CLS_NM = ConfigMgrRestImpl.class.getName();

    /**
     * Create a new configuration node with given name and properties.  The name is required.  If node already exists,
     * a {@link com.jts.fortress.SecurityException} with error {@link com.jts.fortress.constants.GlobalErrIds#FT_CONFIG_ALREADY_EXISTS} will be thrown.
     *
     * @param name    attribute is required and maps to 'cn' attribute in 'device' object class.
     * @param inProperties contains {@link Properties} with list of name/value pairs to add to existing config node.
     * @return {@link java.util.Properties} containing the collection of name/value pairs just added.
     * @throws com.jts.fortress.SecurityException
     *          in the event entry already present or other system error.
     */
    public Properties add(String name, Properties inProperties) throws com.jts.fortress.SecurityException
    {
        VUtil.assertNotNull(name, GlobalErrIds.FT_CONFIG_NAME_NULL, CLS_NM + ".add");
        VUtil.assertNotNull(inProperties, GlobalErrIds.FT_CONFIG_PROPS_NULL, CLS_NM + ".add");
        Properties retProperties;
        FortRequest request = new FortRequest();
        Props inProps = RestUtils.getProps(inProperties);
        request.setEntity(inProps);
        request.setValue(name);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.CFG_ADD);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Props outProps = (Props) response.getEntity();
            retProperties = RestUtils.getProperties(outProps);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retProperties;
    }

    /**
     * Update existing configuration node with additional properties, or, replace existing properties.  The name is required.  If node does not exist,
     * a {@link com.jts.fortress.SecurityException} with error {@link com.jts.fortress.constants.GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     *
     * @param name    attribute is required and maps to 'cn' attribute in 'device' object class.
     * @param inProperties contains {@link Properties} with list of name/value pairs to add or udpate from existing config node.
     * @return {@link Properties} containing the collection of name/value pairs to be added to existing node.
     * @throws com.jts.fortress.SecurityException
     *          in the event entry not present or other system error.
     */
    public Properties update(String name, Properties inProperties) throws com.jts.fortress.SecurityException
    {
        VUtil.assertNotNull(name, GlobalErrIds.FT_CONFIG_NAME_NULL, CLS_NM + ".update");
        VUtil.assertNotNull(inProperties, GlobalErrIds.FT_CONFIG_PROPS_NULL, CLS_NM + ".update");
        Properties retProperties;
        FortRequest request = new FortRequest();
        Props inProps = RestUtils.getProps(inProperties);
        request.setEntity(inProps);
        request.setValue(name);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.CFG_UPDATE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Props outProps = (Props) response.getEntity();
            retProperties = RestUtils.getProperties(outProps);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retProperties;
    }

    /**
      * Completely removes named configuration node from the directory.
     * <p/>
     * <font size="3" color="red">This method is destructive and will remove the configuration node completely from directory.<BR>
     * Care should be taken during execution to ensure target name is correct and permanent removal of all parameters located
     * there is intended.  There is no 'undo' for this operation.
     * </font>
     *
     * @param name is required and maps to 'cn' attribute on 'device' object class of node targeted for operation.
     * @throws com.jts.fortress.SecurityException
     *          in the event of system error.
     */
    public void delete(String name) throws com.jts.fortress.SecurityException
    {
        VUtil.assertNotNull(name, GlobalErrIds.FT_CONFIG_NAME_NULL, CLS_NM + ".deleteProp");
        Properties retProperties;
        FortRequest request = new FortRequest();
        request.setValue(name);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.CFG_DELETE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Delete properties from existing configuration node.  The name is required.  If node does not exist,
     * a {@link com.jts.fortress.SecurityException} with error {@link com.jts.fortress.constants.GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     *
     * @param name attribute is required and maps to 'cn' attribute in 'device' object class.
     * @return {@link Properties} containing the collection of name/value pairs to remove from existing node.
     * @throws com.jts.fortress.SecurityException in the event entry not present or other system error.
     */
    public void delete(String name, Properties inProperties) throws SecurityException
    {
        VUtil.assertNotNull(name, GlobalErrIds.FT_CONFIG_NAME_NULL, CLS_NM + ".delete");
        VUtil.assertNotNull(inProperties, GlobalErrIds.FT_CONFIG_PROPS_NULL, CLS_NM + ".delete");
        Properties retProperties;
        FortRequest request = new FortRequest();
        Props inProps = RestUtils.getProps(inProperties);
        request.setEntity(inProps);
        request.setValue(name);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.CFG_DELETE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Read an existing configuration node with given name and return to caller.  The name is required.  If node doesn't exist,
     * a {@link com.jts.fortress.SecurityException} with error {@link com.jts.fortress.constants.GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     *
     * @param name attribute is required and maps to 'cn' attribute in 'device' object class.
     * @return {@link Properties} containing the collection of name/value pairs just added. Maps to 'ftProps' attribute in 'ftProperties' object class.
     * @throws SecurityException in the event entry doesn't exist or other system error.
     */
    public Properties read(String name) throws com.jts.fortress.SecurityException
    {
        VUtil.assertNotNull(name, GlobalErrIds.FT_CONFIG_NAME_NULL, CLS_NM + ".readRole");
        Properties retProps;
        FortRequest request = new FortRequest();
        request.setValue(name);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.CFG_READ);
        FortResponse response = RestUtils.unmarshall(szResponse);
        Props props;
        if (response.getErrorCode() == 0)
        {
            props = (Props) response.getEntity();
            retProps = RestUtils.getProperties(props);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retProps;
    }
}