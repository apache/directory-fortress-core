/*
cs *   Licensed to the Apache Software Foundation (ASF) under one
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
package org.apache.directory.fortress.core.util;


import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.CfgException;
import org.apache.directory.fortress.core.CfgRuntimeException;
import org.apache.directory.fortress.core.ConfigMgr;
import org.apache.directory.fortress.core.ConfigMgrFactory;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class wraps <a href="http://commons.apache.org/cfg/">Apache Commons Config</a> utility and is used by internal components to retrieve name-value
 * pair properties from its cfg context.  The class will combine properties that it finds in its local property
 * file along with data that is retrieved by name from the ldap server if name is specified within
 * the {@link org.apache.directory.fortress.core.GlobalIds#CONFIG_REALM} switch.
 * <p>
 * The class will bootstrap itself during startup and must initialize correctly for the Fortress APIs to work correctly.
 * <p>
 * This object is thread safe but stores a static reference to Apache Commons Configuration {@link #config} object.
 * <p>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class Config
{
    private static PropertiesConfiguration config = new PropertiesConfiguration();
    private static final String CLS_NM = Config.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final String PROP_FILE = "fortress.properties";
    private static final String USER_PROP_FILE = "fortress.user.properties";
    private static final String EXT_LDAP_HOST = "fortress.host";
    private static final String EXT_LDAP_PORT = "fortress.port";
    private static final String EXT_LDAP_ADMIN_POOL_UID = "fortress.admin.user";
    private static final String EXT_LDAP_ADMIN_POOL_PW = "fortress.admin.pw";
    private static final String EXT_LDAP_ADMIN_POOL_MIN = "fortress.min.admin.conn";
    private static final String EXT_LDAP_ADMIN_POOL_MAX = "fortress.max.admin.conn";
    private static final String EXT_LDAP_LOG_POOL_UID = "fortress.log.user";
    private static final String EXT_LDAP_LOG_POOL_PW = "fortress.log.pw";
    private static final String EXT_LDAP_LOG_POOL_MIN = "fortress.min.log.conn";
    private static final String EXT_LDAP_LOG_POOL_MAX = "fortress.max.log.conn";
    private static final String EXT_ENABLE_LDAP_SSL = "fortress.enable.ldap.ssl";
    private static final String EXT_ENABLE_LDAP_STARTTLS = "fortress.enable.ldap.starttls";
    private static final String EXT_ENABLE_LDAP_SSL_DEBUG = "fortress.enable.ldap.ssl.debug";
    private static final String EXT_TRUST_STORE = "fortress.trust.store";
    private static final String EXT_TRUST_STORE_PW = "fortress.trust.store.password";
    private static final String EXT_TRUST_STORE_ONCLASSPATH = "fortress.trust.store.onclasspath";
    private static final String EXT_CONFIG_REALM = "fortress.config.realm";
    private static final String EXT_CONFIG_ROOT_DN = "fortress.config.root";
    private static final String EXT_CONFIG_SUFFIX = "fortress.suffix";
    private static final String EXT_SERVER_TYPE = "fortress.ldap.server.type";
    private static final String EXT_IS_ARBAC02 = "fortress.is.arbac02";

    // static reference contains this.
    private static volatile Config sINSTANCE = null;

    // used internally to determine if the remote config has been loaded.
    private boolean remoteConfigLoaded = false;

    /**
     * Return a static reference to this instance.  If the instance has not been instantiated, call the boostrap:
     * <ul>
     *     <li>Load from fortress.properties</li>
     *     <li>Load some overrides from System properties</li>
     *     <li>Load from ou=Config ldap node</li>
     * </ul>
     *
     * @return this
     */
    public static Config getInstance()
    {
        if(sINSTANCE == null)
        {
            synchronized (Config.class)
            {
                if(sINSTANCE == null)
                {
                    sINSTANCE = new Config();
                    if( !sINSTANCE.isRemoteConfigLoaded() )
                    {
                        sINSTANCE.loadRemoteConfig();
                    }
                }
            }
        }
        return sINSTANCE;
    }

    /**
     * Private constructor called during bootstrap sequence of this class.
     *
     */
    private Config()
    {
        // Load from properties file:
        loadLocalConfig();
        // load the system property overrides:
        getExternalConfig();
    }

    /**
     * Gets the prop attribute as String value from the apache commons cfg component.
     *
     * @param name contains the name of the property.
     * @return contains the value associated with the property or null if not not found.
     */
    public String getProperty( String name )
    {
        return getProperty( name, false );
    }

    /**
     * Gets the prop attribute as String value from the apache commons cfg component.
     *
     * @param name contains the name of the property.
     * @param nologvalue if true will not output this prop's value to the debug logger.
     * @return contains the value associated with the property or null if not not found.
     */
    public String getProperty( String name, boolean nologvalue )
    {
        String value = null;
        if ( config != null )
        {
            value = ( String ) config.getProperty( name );
            LOG.debug( "getProperty name [{}] value [{}]", name, nologvalue ? "****" : value );
        }
        else
        {
            LOG.warn( "getProperty invalid config, can't read prop [{}]", name );
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
    public String getProperty( String name, String defaultValue )
    {
        String value = null;
        if ( config != null )
        {
            value = ( String ) config.getProperty( name );
            LOG.debug( "getProperty name [{}] value [{}] default value [{}]", name, value, defaultValue );
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
    public char getChar( String name )
    {
        char value = 0;
        if ( config != null )
        {
            value = ( char ) config.getProperty( name );
            LOG.debug( "getChar name [{}] value [{}]", name, value );
        }
        else
        {
            LOG.warn( "getChar invalid config, can't read prop [{}]", name );
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
    public char getChar( String name, char defaultValue )
    {
        char value = 0;
        if ( config != null )
        {
            value = ( char ) config.getProperty( name );
            LOG.debug( "getChar name [{}] value [{}]", name, value );
        }
        else
        {
            LOG.warn( "getChar invalid config, can't read prop [{}], using default [{}]", name, defaultValue );
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
    public int getInt( String key )
    {
        int value = 0;
        try
        {
            if ( config != null )
            {
                value = config.getInt( key );
                LOG.debug( "getInt name [{}] value [{}]", key, value );
            }
            else
            {
                LOG.warn( "getInt invalid config, can't read prop [{}]", key );
            }
        }
        catch (org.apache.commons.configuration.ConversionException e)
        {
            LOG.debug( "getInt can't read prop [{}], exception [{}]", key, e );
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
    public int getInt( String key, int defaultValue )
    {
        int value = defaultValue;
        try
        {
            if ( config != null )
            {
                value = config.getInt( key, defaultValue );
                LOG.debug( "getInt name [{}] value [{}]", key, value );
            }
            else
            {
                LOG.warn( "getInt invalid config, can't read prop [{}], using default [{}]", key, defaultValue );
            }
        }
        catch (org.apache.commons.configuration.ConversionException e)
        {
            LOG.debug( "getInt name [{}], conversion exception using default  [{}]", key, defaultValue );
        }
        return value;
    }

    /**
     * Gets the boolean attribute associated with the name or false if not found.
     *
     * @param key name of the property name.
     * @return The boolean value or false if not found.
     */
    public boolean getBoolean( String key )
    {
        boolean value = false;
        try
        {
            if (config != null)
            {
                value = config.getBoolean(key);
                LOG.debug( "getBoolean name [{}] value [{}]", key, value );
            }
            else
            {
                LOG.warn( "getBoolean invalid config, can't read prop [{}], using default [{}]", key, false );
            }
        }
        catch (java.util.NoSuchElementException e )
        {
            LOG.debug( "getBoolean - no such element [{}], using default [{}]", key, false );
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
    public boolean getBoolean( String key, boolean defaultValue )
    {
        boolean value = defaultValue;
        try
        {
            if ( config != null )
            {
                try
                {
                    value = config.getBoolean( key, defaultValue );
                    LOG.debug( "getBoolean name [{}] value [{}]", key, value );
                }
                catch (org.apache.commons.configuration.ConversionException e )
                {
                    LOG.debug( "getBoolean name [{}], conversion exception using default  [{}]", key, defaultValue );
                }
            }
            else
            {
                LOG.warn( "getBoolean - invalid config, can't read prop [{}], using default [{}]", key, defaultValue );
            }
        }
        catch (java.util.NoSuchElementException nse )
        {
            LOG.debug( "getBoolean - no such element [{}], using default [{}]", key, defaultValue );
        }
        return value;
    }

    /**
     * Set the property String value to the apache commons config.
     *
     * @param name         contains the name of the property.
     * @param value        contains the String value of the property.
     */
    public void setProperty( String name, String value )
    {
        if ( config != null )
        {
            config.setProperty( name, value );
        }
        else
        {
            LOG.warn( "setProperty invalid config, can't set prop name [{}], value [{}]" + name, value);
        }
    }

    /**
     * Clear the property from apache commons config.
     *
     * @param name  contains the name of the property.
     */
    public void clearProperty( String name )
    {
        if ( config != null )
        {
            config.clearProperty( name );
        }
        else
        {
            LOG.warn( "clearProperty invalid config, prop name [{}]", name );
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
    private Properties getRemoteConfig( String realmName ) throws SecurityException
    {
        Properties props = null;
        try
        {
            String configClassName = this.getProperty( GlobalIds.CONFIG_IMPLEMENTATION );
            ConfigMgr cfgMgr = ConfigMgrFactory.createInstance(configClassName, false);
            Configuration configuration = cfgMgr.read( realmName );
            props = configuration.getProperties();
        }
        catch ( CfgException ce )
        {
            if ( ce.getErrorId() == GlobalErrIds.FT_CONFIG_NOT_FOUND )
            {
                LOG.warn( "getRemoteConfig could not find cfg entry [{}]", realmName );
            }
            else
            {
                throw ce;
            }
        }
        return props;
    }

    public boolean isRestEnabled()
    {
        return ( ( getProperty( GlobalIds.ENABLE_REST ) != null ) && ( getProperty( GlobalIds.ENABLE_REST ).equalsIgnoreCase( "true" ) ) );
    }
    /**
     * Fortress stores complex attribute types within a single attribute in ldap.  Usually a delimiter of '$' is used for string tokenization.
     * format: {@code part1$part2$part3....}  Stored in fortress.properties as 'attr.delimiter=$'
     */
    public String getDelimiter()
    {
        return getProperty( "attr.delimiter", "$" );
    }
    public boolean isAuditDisabled()
    {
        return ( ( getProperty( GlobalIds.DISABLE_AUDIT ) != null ) && ( getProperty( GlobalIds.DISABLE_AUDIT ).equalsIgnoreCase( "true" ) ) );
    }
    public boolean isOpenldap()
    {
        return ( ( getProperty( GlobalIds.SERVER_TYPE ) != null ) && ( getProperty( GlobalIds.SERVER_TYPE ).equalsIgnoreCase( "openldap" ) ) );
    }
    public boolean isApacheds()
    {
        return ( ( getProperty( GlobalIds.SERVER_TYPE ) != null ) && ( getProperty( GlobalIds.SERVER_TYPE ).equalsIgnoreCase( "apacheds" ) ) );
    }
    public boolean isRealm()
    {
        return GlobalIds.REALM_TYPE.equalsIgnoreCase( getProperty( GlobalIds.AUTHENTICATION_TYPE ) );
    }
    public boolean isRoleOccupant()
    {
        // misc LDAP:
        String ROLE_OCCUPANTS = "role.occupants";

        // default is true:
        boolean result = true;
        if(( ( getProperty( ROLE_OCCUPANTS ) != null ) && ( getProperty( ROLE_OCCUPANTS ).equalsIgnoreCase( "false" ) ) ))
            result = false;
        return result;
    }

    private boolean isRemoteConfigLoaded()
    {
        return remoteConfigLoaded;
    }

    private char[] loadLdapEscapeChars()
    {
        char[] ldapMetaChars = new char[LdapUtil.getInstance().getLdapFilterSize()];

        for ( int i = 1;; i++ )
        {
            String prop = GlobalIds.LDAP_FILTER + i;
            String value = getProperty( prop );

            if ( value == null )
            {
                break;
            }

            ldapMetaChars[i - 1] = value.charAt( 0 );
        }

        return ldapMetaChars;
    }

    private String[] loadValidLdapVals()
    {
        String[] ldapReplacements = new String[LdapUtil.getInstance().getLdapFilterSize()];

        for ( int i = 1;; i++ )
        {
            String prop = GlobalIds.LDAP_SUB + i;
            String value = getProperty( prop );

            if ( value == null )
            {
                break;
            }

            ldapReplacements[i - 1] = value;
        }

        return ldapReplacements;
    }

    /**
     * Load the config parameters from fortress.properties file.
     */
    private void loadLocalConfig()
    {
        try
        {
            // Load the system config file.
            URL fUrl = Config.class.getClassLoader().getResource( PROP_FILE );
            config.setDelimiterParsingDisabled( true );
            if ( fUrl == null )
            {
                String error = "static init: Error, null cfg file: " + PROP_FILE;
                LOG.warn( error );
            }
            else
            {
                LOG.info( "static init: found from: {} path: {}", PROP_FILE, fUrl.getPath() );
                config.load( fUrl );
                LOG.info( "static init: loading from: {}", PROP_FILE );
            }

            URL fUserUrl = Config.class.getClassLoader().getResource( USER_PROP_FILE );
            if ( fUserUrl != null )
            {
                LOG.info( "static init: found user properties from: {} path: {}", USER_PROP_FILE, fUserUrl.getPath() );
                config.load( fUserUrl );
            }
        }
        catch ( org.apache.commons.configuration.ConfigurationException ex )
        {
            String error = "static init: Error loading from cfg file: [" + PROP_FILE
                + "] ConfigurationException=" + ex;
            LOG.error( error );
            throw new CfgRuntimeException( GlobalErrIds.FT_CONFIG_BOOTSTRAP_FAILED, error, ex );
        }
    }

    /**
     * Load these config parameters from the system.properties.
     */
    private void getExternalConfig()
    {
        String PREFIX = "getExternalConfig override name [{}] value [{}]";
        // Check to see if the ldap host has been overridden by a system property:
        String szValue = System.getProperty( EXT_LDAP_HOST );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_HOST, szValue );
            LOG.info( PREFIX, GlobalIds.LDAP_HOST, szValue );
        }
        // Check to see if the ldap port has been overridden by a system property:
        szValue = System.getProperty( EXT_LDAP_PORT );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_PORT, szValue );
            LOG.info( PREFIX, GlobalIds.LDAP_PORT, szValue );
        }

        // Check to see if the admin pool uid has been overridden by a system property:
        szValue = System.getProperty( EXT_LDAP_ADMIN_POOL_UID );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_ADMIN_POOL_UID, szValue );
            // never display ldap admin userid name to log:
            LOG.info( "getExternalConfig override name [{}]", GlobalIds.LDAP_ADMIN_POOL_UID );
        }

        // Check to see if the admin pool pw has been overridden by a system property:
        szValue = System.getProperty( EXT_LDAP_ADMIN_POOL_PW );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_ADMIN_POOL_PW, szValue );
            // never display password of any type to log:
            LOG.info( "getExternalConfig override name [{}]", GlobalIds.LDAP_ADMIN_POOL_PW );
        }

        // Check to see if the admin pool min connections has been overridden by a system property:
        szValue = System.getProperty( EXT_LDAP_ADMIN_POOL_MIN );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_ADMIN_POOL_MIN, szValue );
            LOG.info( PREFIX, GlobalIds.LDAP_ADMIN_POOL_MIN, szValue );
        }

        // Check to see if the admin pool max connections has been overridden by a system property:
        szValue = System.getProperty( EXT_LDAP_ADMIN_POOL_MAX );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_ADMIN_POOL_MAX, szValue );
            LOG.info( PREFIX, GlobalIds.LDAP_ADMIN_POOL_MAX, szValue );
        }

        // Check to see if the log pool uid has been overridden by a system property:
        szValue = System.getProperty( EXT_LDAP_LOG_POOL_UID );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_LOG_POOL_UID, szValue );
            // never display ldap admin userid name to log:
            LOG.info( "getExternalConfig override name [{}]", GlobalIds.LDAP_LOG_POOL_UID );
        }

        // Check to see if the log pool pw has been overridden by a system property:
        szValue = System.getProperty( EXT_LDAP_LOG_POOL_PW );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_LOG_POOL_PW, szValue );
            // never display password of any type to log:
            LOG.info( "getExternalConfig override name [{}]", GlobalIds.LDAP_LOG_POOL_PW );
        }

        // Check to see if the log pool min connections has been overridden by a system property:
        szValue = System.getProperty( EXT_LDAP_LOG_POOL_MIN );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_LOG_POOL_MIN, szValue );
            LOG.info( PREFIX, GlobalIds.LDAP_LOG_POOL_MIN, szValue );
        }

        // Check to see if the log pool max connections has been overridden by a system property:
        szValue = System.getProperty( EXT_LDAP_LOG_POOL_MAX );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.LDAP_LOG_POOL_MAX, szValue );
            LOG.info( PREFIX, GlobalIds.LDAP_LOG_POOL_MAX, szValue );
        }

        // Check to see if ssl enabled parameter has been overridden by a system property:
        szValue = System.getProperty( EXT_ENABLE_LDAP_SSL );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.ENABLE_LDAP_SSL, szValue );
            LOG.info( PREFIX, GlobalIds.ENABLE_LDAP_SSL, szValue );
        }
        
        // Check to see if start tls enabled parameter has been overridden by a system property:
        szValue = System.getProperty( EXT_ENABLE_LDAP_STARTTLS );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.ENABLE_LDAP_STARTTLS, szValue );
            LOG.info( PREFIX, GlobalIds.ENABLE_LDAP_STARTTLS, szValue );
        }

        // Check to see if the ssl debug enabled parameter has been overridden by a system property:
        szValue = System.getProperty( EXT_ENABLE_LDAP_SSL_DEBUG );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.ENABLE_LDAP_SSL_DEBUG, szValue );
            LOG.info( PREFIX, GlobalIds.ENABLE_LDAP_SSL_DEBUG, szValue );
        }

        // Check to see if the trust store location has been overridden by a system property:
        szValue = System.getProperty( EXT_TRUST_STORE );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.TRUST_STORE, szValue );
            LOG.info( PREFIX, GlobalIds.TRUST_STORE, szValue );
        }

        // Check to see if the trust store password has been overridden by a system property:
        szValue = System.getProperty( EXT_TRUST_STORE_PW );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.TRUST_STORE_PW, szValue );
            // never display password value to log:
            LOG.info( "getExternalConfig override name [{}]", GlobalIds.TRUST_STORE_PW );
        }

        // Check to see if the trust store onclasspath parameter has been overridden by a system property:
        szValue = System.getProperty( EXT_TRUST_STORE_ONCLASSPATH );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.TRUST_STORE_ON_CLASSPATH, szValue );
            LOG.info( PREFIX, GlobalIds.TRUST_STORE_ON_CLASSPATH, szValue );
        }

        // Check to see if the suffix has been overridden by a system property:
        szValue = System.getProperty( EXT_CONFIG_SUFFIX );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.SUFFIX, szValue );
            LOG.info( PREFIX, GlobalIds.SUFFIX, szValue );

        }

        // Check to see if the config realm name has been overridden by a system property:
        szValue = System.getProperty( EXT_CONFIG_REALM );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.CONFIG_REALM, szValue );
            LOG.info( PREFIX, GlobalIds.CONFIG_REALM, szValue );
        }

        // Check to see if the config node dn has been overridden by a system property:
        szValue = System.getProperty( EXT_CONFIG_ROOT_DN );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.CONFIG_ROOT_PARAM, szValue );
            LOG.info( PREFIX, GlobalIds.CONFIG_ROOT_PARAM, szValue );
        }

        // Check to see if the ldap server type has been overridden by a system property:
        szValue = System.getProperty( EXT_SERVER_TYPE  );
        if( StringUtils.isNotEmpty( szValue ))
        {
            config.setProperty( GlobalIds.SERVER_TYPE, szValue );
            LOG.info( PREFIX, GlobalIds.SERVER_TYPE, szValue );
        }

        // Check to see if ARBAC02 checking enforced in service layer:
        szValue = System.getProperty( EXT_IS_ARBAC02 );
        if( StringUtils.isNotEmpty( szValue ))
        {
            Boolean isArbac02 = Boolean. valueOf( szValue );
            config.setProperty( GlobalIds.IS_ARBAC02, isArbac02.booleanValue() );
            LOG.info( PREFIX, GlobalIds.IS_ARBAC02, isArbac02.booleanValue() );
        }
    }

    /**
     * Load the properties contained within ou=Config node in LDAP.
     */
    private void loadRemoteConfig()
    {
        try
        {
            // Retrieve parameters from the config node stored in target LDAP DIT:
            String realmName = config.getString( GlobalIds.CONFIG_REALM, "DEFAULT" );
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
                        config.setProperty( key, val );
                    }
                }

                //init ldap util vals since config is stored on server
                boolean ldapfilterSizeFound = ( getProperty( GlobalIds.LDAP_FILTER_SIZE_PROP ) != null );
                LdapUtil.getInstance().setLdapfilterSizeFound(ldapfilterSizeFound);
                LdapUtil.getInstance().setLdapMetaChars( loadLdapEscapeChars() );
                LdapUtil.getInstance().setLdapReplVals( loadValidLdapVals() );
                try
                {
                    String lenProp = getProperty( GlobalIds.LDAP_FILTER_SIZE_PROP );
                    if ( ldapfilterSizeFound )
                    {
                        LdapUtil.getInstance().setLdapFilterSize(Integer.valueOf( lenProp ));
                    }
                }
                catch ( java.lang.NumberFormatException nfe )
                {
                    String error = "loadRemoteConfig caught NumberFormatException=" + nfe;
                    LOG.warn( error );
                }
                remoteConfigLoaded = true;
            }
            else
            {
                LOG.info( "static init: config realm not setup" );
            }
        }
        catch ( SecurityException se )
        {
            String error = "static init: Error loading from remote config: SecurityException="
                + se;
            LOG.error( error );
            throw new CfgRuntimeException( GlobalErrIds.FT_CONFIG_INITIALIZE_FAILED, error, se );
        }
    }

    /**
     * Constructs a key used to store dynamic role constraints inside the properties, as name:value.
     * The key format is: RC$contextId$role
     * @param role contains the name of the role being constrained.
     * @param contextId contains the tenant name.
     * @return String containing the key name used to lookup the value of the role constraint.
     */
    public String getConstraintKey( String role, String contextId )
    {
        return GlobalIds.CONSTRAINT_KEY_PREFIX +
                getDelimiter() +
                contextId +
                getDelimiter()
                + role.toLowerCase();
    }

    /**
     * Performs auto-increment on a list of key names that map to integer values stored on the current config node of the runtime.
     * Unfortunately, it's synchronized to prevent a race condition of multiple threads trying to update the same id.
     * Worse, it doesn't lock meaning not synched across processes and so a temporary workaround until the pending Apache LDAP API/Directory support for RFC 4525 (Modify Increment attribute).
     *
     * @param props list of attribute names to update on config node.
     * @param propUpdater reference to object that updates to new value.
     * @return Configuration entity containing the old values.
     */
    public synchronized Configuration getIncrementReplacePosixIds(List<String> props, PropUpdater propUpdater ) throws CfgException
    {
        String cfgName = Config.getInstance().getProperty( GlobalIds.CONFIG_REALM, "DEFAULT" );
        org.apache.directory.fortress.core.model.Configuration inConfig;
        try
        {
            ConfigMgr cfgMgr = ConfigMgrFactory.createInstance();
            inConfig = cfgMgr.getIds( cfgName );
            org.apache.directory.fortress.core.model.Configuration outConfig = new Configuration();
            outConfig.setName( cfgName );
            for( String name : props )
            {
                if( name.equals( GlobalIds.UID_NUMBER ) )
                {
                    outConfig.setUidNumber( propUpdater.newValue( inConfig.getUidNumber() ) );
                }
                if( name.equals( GlobalIds.GID_NUMBER ) )
                {
                    outConfig.setGidNumber( propUpdater.newValue( inConfig.getGidNumber() ) );
                }
            }
            cfgMgr.update( outConfig );
        }
        catch ( SecurityException se )
        {
            String error = "replaceProperty failed, exception=" + se.getMessage();
            throw new CfgRuntimeException( GlobalErrIds.FT_CONFIG_UPDATE_FAILED, error, se );
        }
        return inConfig;
    }
}
