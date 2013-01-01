/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package com.jts.fortress.cfg;

import com.jts.fortress.SecurityException;

import java.util.Properties;

/**
 * This interface prescribes CRUD methods used to manage properties stored within the ldap directory.
 * The Fortress config nodes are used to remotely share Fortress client specific properties between processes.
 * Fortress places no limits on the number of unique configurations that can be present at one time in the directory.
 * The Fortress client will specify the preferred cfg node by name via a property named, {@link com.jts.fortress.GlobalIds#CONFIG_REALM}.
 * Each process using Fortress client is free to share an existing node with other processes or create its own unique config
 * instance using the methods within this class.<BR>
 * <p/>
 * This class is thread safe.
 * <p/>

 *
 * @author Shawn McKinney
 */
public interface ConfigMgr
{
    /**
     * Create a new cfg node with given name and properties.  The name is required.  If node already exists,
     * a {@link com.jts.fortress.SecurityException} with error {@link com.jts.fortress.GlobalErrIds#FT_CONFIG_ALREADY_EXISTS} will be thrown.
     *
     * @param name    attribute is required and maps to 'cn' attribute in 'device' object class.
     * @param inProps contains {@link Properties} with list of name/value pairs to add to existing config node.
     * @return {@link Properties} containing the collection of name/value pairs just added.
     * @throws com.jts.fortress.SecurityException in the event entry already present or other system error.
     */
    public Properties add(String name, Properties inProps) throws com.jts.fortress.SecurityException;

    /**
     * Update existing cfg node with additional properties, or, replace existing properties.  The name is required.  If node does not exist,
     * a {@link com.jts.fortress.SecurityException} with error {@link com.jts.fortress.GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     *
     * @param name    attribute is required and maps to 'cn' attribute in 'device' object class.
     * @param inProps contains {@link Properties} with list of name/value pairs to add or udpate from existing config node.
     * @return {@link Properties} containing the collection of name/value pairs to be added to existing node.
     * @throws com.jts.fortress.SecurityException in the event entry not present or other system error.
     */
    public Properties update(String name, Properties inProps) throws SecurityException;

    /**
     * Completely removes named cfg node from the directory.
     * <p/>
     * <font size="3" color="red">This method is destructive and will remove the cfg node completely from directory.<BR>
     * Care should be taken during execution to ensure target name is correct and permanent removal of all parameters located
     * there is intended.  There is no 'undo' for this operation.
     * </font>
     *
     * @param name is required and maps to 'cn' attribute on 'device' object class of node targeted for operation.
     * @throws com.jts.fortress.SecurityException in the event of system error.
     */
    public void delete(String name) throws com.jts.fortress.SecurityException;

    /**
     * Delete properties from existing cfg node.  The name is required.  If node does not exist,
     * a {@link com.jts.fortress.SecurityException} with error {@link com.jts.fortress.GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     *
     * @param name attribute is required and maps to 'cn' attribute in 'device' object class.
     * @throws com.jts.fortress.SecurityException in the event entry not present or other system error.
     */
    public void delete(String name, Properties inProps) throws com.jts.fortress.SecurityException;

    /**
     * Read an existing cfg node with given name and return to caller.  The name is required.  If node doesn't exist,
     * a {@link com.jts.fortress.SecurityException} with error {@link com.jts.fortress.GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     *
     * @param name attribute is required and maps to 'cn' attribute in 'device' object class.
     * @return {@link Properties} containing the collection of name/value pairs just added. Maps to 'ftProps' attribute in 'ftProperties' object class.
     * @throws com.jts.fortress.SecurityException in the event entry doesn't exist or other system error.
     */
    public Properties read(String name) throws SecurityException;
}

