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
package org.apache.directory.fortress.core.cfg;


import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.directory.fortress.core.util.attr.VUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.CfgException;
import org.apache.directory.fortress.core.CfgRuntimeException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;

/**
 * This class wraps <a href="http://commons.apache.org/cfg/">Apache Commons Config</a> utility and is used by internal components to retrieve name-value
 * pair properties from its cfg context.  The class will combine properties that it finds in its local property
 * file along with data that is retrieved by name from the ldap server if name is specified within
 * the {@link org.apache.directory.fortress.core.GlobalIds#CONFIG_REALM} switch.
 * <p>
 * The class will bootstrap itself during startup and must initialize correctly for the Fortress APIs to work correctly.
 * <p>
 * This object is thread safe but stores a static reference to Apache Commons Configuration {@link #config} object.
 * </p>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Config
{
    private static final String propFile = "fortress.properties";
    private static final String userPropFile = "fortress.user.properties";
    private static final String EXT_LDAP_HOST = "fortress.host";
    private static final String EXT_LDAP_PORT = "fortress.port";
    private static final String EXT_LDAP_ADMIN_POOL_UID = "fortress.admin.user";
    private static final String EXT_LDAP_ADMIN_POOL_PW = "fortress.admin.pw";
    private static final String EXT_LDAP_ADMIN_POOL_MIN = "fortress.min.admin.conn";
    private static final String EXT_LDAP_ADMIN_POOL_MAX = "fortress.max.admin.conn";
    private static final String EXT_ENABLE_LDAP_SSL = "fortress.enable.ldap.ssl";
    private static final String EXT_ENABLE_LDAP_SSL_DEBUG = "fortress.enable.ldap.ssl.debug";
    private static final String EXT_TRUST_STORE = "fortress.trust.store";
    private static final String EXT_TRUST_STORE_PW = "fortress.trust.store.password";
    private static final String EXT_SET_TRUST_STORE_PROP = "fortress.trust.store.set.prop";
    private static final String EXT_CONFIG_REALM = "fortress.config.realm";
    private static final String EXT_SERVER_TYPE = "fortress.ldap.server.type";
    private static final PropertiesConfiguration config;
    private static final String CLS_NM = Config.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    static
    {
        try
        {
            // Load the system config file.
            URL fUrl = Config.class.getClassLoader().getResource( propFile );

            if ( fUrl == null )
            {
                String error = "static init: Error, null cfg file: " + propFile;
                LOG.error( error );
                throw new java.lang.RuntimeException( error );
            }

            LOG.info( "static init: found from: {} path: {}", propFile, fUrl.getPath() );

            config = new PropertiesConfiguration();
            config.setDelimiterParsingDisabled( true );
            config.load( fUrl );
            LOG.info( "static init: loading from: {}", propFile );

            URL fUserUrl = Config.class.getClassLoader().getResource( userPropFile );
            if ( fUserUrl != null )
            {
                LOG.info( "static init: found user properties from: {} path: {}",
                    userPropFile, fUserUrl.getPath() );
                config.load( fUserUrl );
            }

            // Check to see if any of the ldap connection parameters have been overridden:
            getExternalConfig();

            // Retrieve parameters from the config node stored in target LDAP DIT:
            String realmName = config.getString( GlobalIds.CONFIG_REALM );
            if ( realmName != null && realmName.length() > 0 )
            {
                LOG.info( "static init: load config realm [{}]", realmName );
                Properties props = getRemoteConfig( realmName );
                if ( props != null )
                {
                    for ( Enumeration<?> e = props.propertyNames(); e.hasMoreElements(); )
                    {
                        String key = ( String ) e.nextElement();
                        String val = props.getProperty( key );
                        config.addProperty( key, val );
                    }
                }
            }
            else
            {
                LOG.info( "static init: config realm not setup" );
            }
        }
        catch ( org.apache.commons.configuration.ConfigurationException ex )
        {
            String error = "static init: Error loading from cfg file: [" + propFile
                + "] ConfigurationException=" + ex;
            LOG.error( error );
            throw new CfgRuntimeException( GlobalErrIds.FT_CONFIG_BOOTSTRAP_FAILED, error, ex );
        }
        catch ( SecurityException se )
        {
            String error = "static init: Error loading from cfg file: [" + propFile + "] SecurityException="
                + se;
            LOG.error( error );
            throw new CfgRuntimeException( GlobalErrIds.FT_CONFIG_INITIALIZE_FAILED, error, se );
        }
    }

    /**
     * Private constructor
     *
     */
    private Config()
    {
    }

    /**
     * Gets the prop attribute as String value from the apache commons cfg component.
     *
     * @param name contains the name of the property.
     * @return contains the value associated with the property or null if not not found.
     */
    public static String getProperty( String name )
    {
        String value = null;
        if ( config != null )
        {
            value = ( String ) config.getProperty( name );
            LOG.debug( "getProperty name [{}] value [{}]", name, value );
        }
        else
        {
            LOG.error( "getProperty invalid config, can't read prop [{}]", name );
        }
        return value;
    }


    /**
     * Get the property value from the apache commons config but specify a default value if not found.
     *
     * @param name         contains the name of the property.
     * @param defaultValue specified by client will be returned if property value is not found.
     * @return contains the value for the property as a String.
     */
    public static String getProperty( String name, String defaultValue )
    {
        String value = null;
        if ( config != null )
        {
            value = ( String ) config.getProperty( name );
        }
        else
        {
            String warn = "getProperty invalid config, can't read prop [" + name + "]";
            LOG.warn( warn );
        }
        if ( value == null || value.length() == 0 )
        {
            value = defaultValue;
        }
        return value;
    }


    /**
     * Gets the prop attribute as char value from the apache commons cfg component.
     *
     * @param name contains the name of the property.
     * @return contains the value associated with the property or 0 if not not found.
     */
    public static char getChar( String name )
    {
        char value = 0;
        if ( config != null )
        {
            value = ( char ) config.getProperty( name );
            LOG.debug( "getChar name [{}] value [{}]", name, value );
        }
        else
        {
            LOG.error( "getChar invalid config, can't read prop [{}]", name );
        }
        return value;
    }


    /**
     * Get the property value from the apache commons config but specify a default value if not found.
     *
     * @param name         contains the name of the property.
     * @param defaultValue specified by client will be returned if property value is not found.
     * @return contains the value for the property as a char.
     */
    public static char getChar( String name, char defaultValue )
    {
        char value = 0;
        if ( config != null )
        {
            value = ( char ) config.getProperty( name );
        }
        else
        {
            String warn = "getChar invalid config, can't read prop [" + name + "]";
            LOG.warn( warn );
        }
        if ( value == 0 )
        {
            value = defaultValue;
        }
        return value;
    }


    /**
     * Gets the int attribute of the Config class, or 0 if not found.
     *
     * @param key name of the property name.
     * @return The int value or 0 if not found.
     */
    public static int getInt( String key )
    {
        int value = 0;
        if ( config != null )
        {
            value = config.getInt( key );
        }
        else
        {
            String warn = "getInt invalid config, can't read prop [" + key + "]";
            LOG.warn( warn );
        }
        return value;
    }


    /**
     * Gets the int attribute of the Config class or default value if not found.
     *
     * @param key          name of the property name.
     * @param defaultValue to use if property not found.
     * @return The int value or default value if not found.
     */
    public static int getInt( String key, int defaultValue )
    {
        int value = 0;
        if ( config != null )
        {
            value = config.getInt( key, defaultValue );
        }
        else
        {
            String warn = "getInt invalid config, can't read prop [" + key + "]";
            LOG.warn( warn );
        }
        return value;
    }


    /**
     * Gets the boolean attribute associated with the name or false if not found.
     *
     * @param key name of the property name.
     * @return The boolean value or false if not found.
     */
    public static boolean getBoolean( String key )
    {
        boolean value = false;
        if ( config != null )
        {
            value = config.getBoolean( key );
        }
        else
        {
            String warn = "getBoolean - invalid config, can't read prop [" + key + "]";
            LOG.warn( warn );
        }
        return value;
    }


    /**
     * Gets the boolean attribute associated with the name or false if not found.
     *
     * @param key          name of the property name.
     * @param defaultValue specified by client will be returned if property value is not found.
     * @return The boolean value or false if not found.
     */
    public static boolean getBoolean( String key, boolean defaultValue )
    {
        boolean value = defaultValue;
        if ( config != null )
        {
            value = config.getBoolean( key, defaultValue );
        }
        else
        {
            String warn = "getBoolean - invalid config, can't read prop [" + key + "]";
            LOG.warn( warn );
        }
        return value;
    }


    /**
     * Set the property String value to the apache commons config.
     *
     * @param name         contains the name of the property.
     * @param value        contains the String value of the property.
     */
    public static void setProperty( String name, String value )
    {
        if ( config != null )
        {
            config.setProperty( name, value );
        }
        else
        {
            String warn = "setProperty invalid config, can't set prop name [" + name + "], value [" + value + "]";
            LOG.warn( warn );
        }
    }


    /**
     * Fetch the remote cfg params from ldap with given name.
     *
     * @param realmName required attribute contains the name of config node name on ldap.
     * @return {@link Properties} containing collection of name/value pairs found in directory.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of system or validation error.
     */
    private static Properties getRemoteConfig( String realmName ) throws SecurityException
    {
        Properties props = null;
        try
        {
            ConfigMgr cfgMgr = ConfigMgrFactory.createInstance();
            props = cfgMgr.read( realmName );
        }
        catch ( CfgException ce )
        {
            if ( ce.getErrorId() == GlobalErrIds.FT_CONFIG_NOT_FOUND )
            {
                String warning = "getRemoteConfig could not find cfg entry";
                LOG.warn( warning );
            }
            else
            {
                throw ce;
            }
        }
        return props;
    }


    /**
     * This method is called during configuration initialization.  It determines if
     * the ldap connection coordinates have been overridden as system properties.
     */
    private static void getExternalConfig()
    {
        String PREFIX = "getExternalConfig override name [{}] value [{}]";
        // Check to see if the ldap host has been overriden by a system property:
        String szValue = System.getProperty( EXT_LDAP_HOST );
        if( VUtil.isNotNullOrEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_HOST, szValue );
            LOG.info( PREFIX, GlobalIds.LDAP_HOST, szValue );
        }
        // Check to see if the ldap port has been overriden by a system property:
        szValue = System.getProperty( EXT_LDAP_PORT );
        if( VUtil.isNotNullOrEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_PORT, szValue );
            LOG.info( PREFIX, GlobalIds.LDAP_PORT, szValue );
        }

        // Check to see if the admin pool uid has been overriden by a system property:
        szValue = System.getProperty( EXT_LDAP_ADMIN_POOL_UID );
        if( VUtil.isNotNullOrEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_ADMIN_POOL_UID, szValue );
            // never display ldap admin userid name to log:
            LOG.info( "getExternalConfig override name [{}]", GlobalIds.LDAP_ADMIN_POOL_UID );
        }

        // Check to see if the admin pool pw has been overriden by a system property:
        szValue = System.getProperty( EXT_LDAP_ADMIN_POOL_PW );
        if( VUtil.isNotNullOrEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_ADMIN_POOL_PW, szValue );
            // never display password of any type to log:
            LOG.info( "getExternalConfig override name [{}]", GlobalIds.LDAP_ADMIN_POOL_PW );
        }

        // Check to see if the admin pool min connections has been overriden by a system property:
        szValue = System.getProperty( EXT_LDAP_ADMIN_POOL_MIN );
        if( VUtil.isNotNullOrEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_ADMIN_POOL_MIN, szValue );
            LOG.info( PREFIX, GlobalIds.LDAP_ADMIN_POOL_MIN, szValue );
        }

        // Check to see if the admin pool max connections has been overriden by a system property:
        szValue = System.getProperty( EXT_LDAP_ADMIN_POOL_MAX );
        if( VUtil.isNotNullOrEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_ADMIN_POOL_MAX, Integer.valueOf( szValue ) );
            LOG.info( PREFIX, GlobalIds.LDAP_ADMIN_POOL_MAX, szValue );
        }

        // Check to see if ssl enabled parameter has been overriden by a system property:
        szValue = System.getProperty( EXT_ENABLE_LDAP_SSL );
        if( VUtil.isNotNullOrEmpty( szValue ))
        {
            config.setProperty( GlobalIds.ENABLE_LDAP_SSL, szValue );
            LOG.info( PREFIX, GlobalIds.ENABLE_LDAP_SSL, szValue );
        }

        // Check to see if the ssl debug enabled parameter has been overriden by a system property:
        szValue = System.getProperty( EXT_ENABLE_LDAP_SSL_DEBUG );
        if( VUtil.isNotNullOrEmpty( szValue ))
        {
            config.setProperty( GlobalIds.ENABLE_LDAP_SSL_DEBUG, szValue );
            LOG.info( PREFIX, GlobalIds.ENABLE_LDAP_SSL_DEBUG, szValue );
        }

        // Check to see if the trust store location has been overriden by a system property:
        szValue = System.getProperty( EXT_TRUST_STORE );
        if( VUtil.isNotNullOrEmpty( szValue ))
        {
            config.setProperty( GlobalIds.TRUST_STORE, szValue );
            LOG.info( PREFIX, GlobalIds.TRUST_STORE, szValue );
        }

        // Check to see if the trust store password has been overriden by a system property:
        szValue = System.getProperty( EXT_TRUST_STORE_PW );
        if( VUtil.isNotNullOrEmpty( szValue ))
        {
            config.setProperty( GlobalIds.TRUST_STORE_PW, szValue );
            // never display password value to log:
            LOG.info( "getExternalConfig override name [{}]", GlobalIds.TRUST_STORE_PW );
        }

        // Check to see if the trust store set parameter has been overridden by a system property:
        szValue = System.getProperty( EXT_SET_TRUST_STORE_PROP );
        if( VUtil.isNotNullOrEmpty( szValue ))
        {
            config.setProperty( GlobalIds.SET_TRUST_STORE_PROP, szValue );
            LOG.info( PREFIX, GlobalIds.SET_TRUST_STORE_PROP, szValue );
        }

        // Check to see if the config realm name has been overriden by a system property:
        szValue = System.getProperty( EXT_CONFIG_REALM );
        if( VUtil.isNotNullOrEmpty( szValue ))
        {
            config.setProperty( GlobalIds.CONFIG_REALM, szValue );
            LOG.info( PREFIX, GlobalIds.CONFIG_REALM, szValue );
        }

        // Check to see if the ldap server type has been overriden by a system property:
        szValue = System.getProperty( EXT_SERVER_TYPE  );
        if( VUtil.isNotNullOrEmpty( szValue ))
        {
            config.setProperty( GlobalIds.SERVER_TYPE, szValue );
            LOG.info( PREFIX, GlobalIds.SERVER_TYPE, szValue );
        }
    }
}