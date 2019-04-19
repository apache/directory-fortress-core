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

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.ValidationException;
import org.apache.directory.fortress.core.model.Configuration;
import org.apache.directory.fortress.core.util.VUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Process module for the configurations node used for remotely storing Fortress specific properties.
 * Fortress places no limits on the number of unique configurations that can be present.  The Fortress client will specify
 * the preferred cfg node by name via a property named, {@link org.apache.directory.fortress.core.GlobalIds#CONFIG_REALM}.  Each
 * process using Fortress client is free to share existing node with other processes or create its own unique config
 * instance using the methods within this class.<BR>
 * This class does perform simple data validations to ensure data reasonability and the required fields are present.<BR>
 * The {@link org.apache.directory.fortress.core.ant.FortressAntTask#addConfig()} method calls the {@link #add} from this class during initial base loads.
 * Removal {@link org.apache.directory.fortress.core.ant.FortressAntTask#deleteConfig()} is performed when removal of properties is the aim.<BR>
 * This class will accept {@link Properties}, and forward on to it's corresponding DAO class {@link ConfigDAO} for updating properties stored on behalf of Fortress.
 * <p>
 * Class will throw {@link SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link org.apache.directory.fortress.core.FinderException},
 * {@link org.apache.directory.fortress.core.CreateException},{@link org.apache.directory.fortress.core.UpdateException},{@link org.apache.directory.fortress.core.RemoveException}),
 *  or {@link org.apache.directory.fortress.core.ValidationException} as {@link org.apache.directory.fortress.core.SecurityException}s with appropriate
 *  error id from {@link org.apache.directory.fortress.core.GlobalErrIds}.
 * <p>
 * This class performs simple data validation on properties to ensure length does not exceed 100 and contents are safe text.
 * <p>

 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class ConfigP
{
    private static final String CLS_NM = ConfigP.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    /**
     * Create a new cfg node with given name and properties.  The name is required.  If node already exists,
     * a {@link org.apache.directory.fortress.core.SecurityException} with error {@link GlobalErrIds#FT_CONFIG_ALREADY_EXISTS} will be thrown.
     *
     * @param name    attribute is required and maps to 'cn' attribute in 'device' object class.
     * @param inProps contains {@link Properties} with list of name/value pairs to remove from existing config node.
     * @return {@link Properties} containing the collection of name/value pairs just added.
     * @throws SecurityException in the event entry already present or other system error.
     */
    Configuration add(Configuration cfg)
        throws SecurityException
    {
        validate( cfg.getName(), cfg.getProperties(), false );
        ConfigDAO cfgDao = new ConfigDAO();
        return cfgDao.create( cfg );
    }


    /**
     * Update existing cfg node with additional properties, or, replace existing properties.  The name is required.  If node does not exist,
     * a {@link SecurityException} with error {@link org.apache.directory.fortress.core.GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     *
     * @param name    attribute is required and maps to 'cn' attribute in 'device' object class.
     * @param inProps contains {@link Properties} with list of name/value pairs to add or udpate from existing config node.
     * @return {@link Properties} containing the collection of name/value pairs to be added to existing node.
     * @throws org.apache.directory.fortress.core.SecurityException in the event entry not present or other system error.
     */
    Configuration update(Configuration cfg)
        throws SecurityException
    {
        validate( cfg.getName(), cfg.getProperties(), true );
        ConfigDAO cfgDao = new ConfigDAO();
        return cfgDao.update( cfg );
    }


    /**
     * This method will update a single property with a new value..
     *
     * @param name of the config node, mostly likely 'DEFAULT'.
     * @param key used for the property.
     * @param value this is old value to be replaced with newValue.
     * @param newValue new value for the property
     * @throws org.apache.directory.fortress.core.SecurityException in the event entry not present or other system error.
     */
    void updateProperty( String name, String key, String value, String newValue )
        throws SecurityException
    {
        ConfigDAO cfgDao = new ConfigDAO();
        cfgDao.updateProperty( name, key, value, newValue );
    }


    /**
     * Delete existing cfg node which will remove all properties associated with that node.
     * The name is required.  If node does not exist, a {@link SecurityException} with error
     * {@link GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     * <p>
     * <font size="2" color="red">
     * This method is destructive and will remove the cfg node completely from directory.<BR>
     * Care should be taken during execution to ensure target name is correct and permanent removal of all parameters located
     * there is intended.  There is no 'undo' for this operation.
     * </font>
     * 
     * @param name attribute is required and maps to 'cn' attribute in 'device' object class.
     * @return {@link Properties} containing the collection of name/value pairs to be added to existing node.
     * @throws SecurityException in the event entry not present or other system error.
     */
    void delete( String name )
        throws SecurityException
    {
        if ( StringUtils.isEmpty( name ) )
        {
            String error = "delete detected null config realm name";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.FT_CONFIG_NAME_NULL, error );
        }
        ConfigDAO cfgDao = new ConfigDAO();
        cfgDao.remove( name );
    }


    /**
     * Delete existing cfg node with additional properties, or, replace existing properties.  The name is required.  If node does not exist,
     * a {@link SecurityException} with error {@link org.apache.directory.fortress.core.GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     *
     * @param name attribute is required and maps to 'cn' attribute in 'device' object class.
     * @throws org.apache.directory.fortress.core.SecurityException in the event entry not present or other system error.
     */
    void delete( String name, Properties inProps )
        throws SecurityException
    {
        validate( name, inProps, false );
        ConfigDAO cfgDao = new ConfigDAO();
        cfgDao.remove( name, inProps );
    }


    /**
     * Read an existing cfg node with given name and return to caller.  The name is required.  If node doesn't exist,
     * a {@link SecurityException} with error {@link org.apache.directory.fortress.core.GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     *
     * @param name attribute is required and maps to 'cn' attribute in 'device' object class.
     * @return {@link Properties} containing the collection of name/value pairs just added. Maps to 'ftProps' attribute in 'ftProperties' object class.
     * @throws org.apache.directory.fortress.core.SecurityException in the event entry doesn't exist or other system error.
     */
    Configuration read( String name )
        throws SecurityException
    {
        ConfigDAO cfgDao = new ConfigDAO();
        return cfgDao.getConfig( name );
    }


    /**
     * Read an existing cfg node with given name and return posixIds to caller.  The name is required.  If node doesn't exist,
     * a {@link SecurityException} with error {@link org.apache.directory.fortress.core.GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     *
     * @param name attribute is required and maps to 'cn' attribute in 'device' object class.
     * @return Configuration entity contains the getPosixIds on the specified node.
     * @throws org.apache.directory.fortress.core.SecurityException in the event entry doesn't exist or other system error.
     */
    Configuration readPosixIds( String name )
        throws SecurityException
    {
        ConfigDAO cfgDao = new ConfigDAO();
        return cfgDao.getPosixIds( name );
    }


    /**
     * Method will perform simple validations to ensure the integrity of the {@link Properties} entity targeted for insertion
     * or deletion in directory.
     *
     * @param name contains the name of the cfg node.
     * @param entity contains the name/value properties targeted for operation.
     * @throws org.apache.directory.fortress.core.ValidationException thrown in the event the validations fail.
     */
    private void validate( String name, Properties entity, boolean isUpdate )
        throws ValidationException
    {
        if ( StringUtils.isEmpty( name ) )
        {
            String error = "validate detected null config realm name";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.FT_CONFIG_NAME_NULL, error );
        }
        if ( name.length() > GlobalIds.OU_LEN )
        {
            String error = "validate name [" + name + "] invalid length [" + name.length() + "]";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.FT_CONFIG_NAME_INVLD, error );
        }
        if ( !isUpdate && ( entity == null || entity.size() == 0 ) )
        {
            String error = "validate name [" + name + "] config props null";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.FT_CONFIG_PROPS_NULL, error );
        }
        VUtil.properties( entity );
    }
}
