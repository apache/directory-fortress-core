/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.cfg;

import us.jts.fortress.CfgException;
import us.jts.fortress.CfgRuntimeException;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.GlobalIds;
import us.jts.fortress.SecurityException;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/**
 * This class wraps <a href="http://commons.apache.org/cfg/">Apache Commons Config</a> utility and is used by internal components to retrieve name-value
 * pair properties from its cfg context.  The class will combine properties that it finds in its local property
 * file along with data that is retrieved by name from the ldap server if name is specified within
 * the {@link us.jts.fortress.GlobalIds#CONFIG_REALM} switch.
 * <p>
 * The class will bootstrap itself during startup and must initialize correctly for the Fortress APIs to work correctly.
 * <p>
 * This object is thread safe but stores a static reference to Apache Commons Configuration {@link #config} object.
 * </p>
 *
 * @author Shawn McKinney
 */
public class Config
{
    final private static String propFile = "fortress.properties";
    final private static String userPropFile = "fortress.user.properties";
    private static PropertiesConfiguration config;
    private static final String CLS_NM = Config.class.getName();
    final protected static Logger log = Logger.getLogger(CLS_NM);

    static
    {
        try
        {
            // Load the system config bootstrap xml file.
            URL fUrl = Config.class.getClassLoader().getResource(propFile);
            if (fUrl == null)
            {
                String error = CLS_NM + " static init: Error, null cfg file: " + propFile;
                log.fatal(error);
                throw new java.lang.RuntimeException(error);
            }
            log.info(CLS_NM + " static init: found from: " + propFile + " path:" + fUrl.getPath());
            config = new PropertiesConfiguration();
            config.setDelimiterParsingDisabled(true);
            config.load(fUrl);
            log.info(CLS_NM + " static init: loading from: " + propFile);

            URL fUserUrl = Config.class.getClassLoader().getResource(userPropFile);
            if (fUserUrl != null)
            {
                log.info(CLS_NM + " static init: found user properties from: " + userPropFile + " path:" + fUserUrl.getPath());
                config.load(fUserUrl);
            }

            String realmName = config.getString(GlobalIds.CONFIG_REALM);
            if (realmName != null && realmName.length() > 0)
            {
                log.info(CLS_NM + " static init: load config realm [" + realmName + "]");
                Properties props = getRemoteConfig(realmName);
                if (props != null)
                {
                    for (Enumeration e = props.propertyNames(); e.hasMoreElements(); )
                    {
                        String key = (String) e.nextElement();
                        String val = props.getProperty(key);
                        config.addProperty(key, val);
                    }
                }
            }
            else
            {
                log.info(CLS_NM + " static init: config realm not setup");
            }
        }
        catch (org.apache.commons.configuration.ConfigurationException ex)
        {
            String error = CLS_NM + " static init: Error loading from cfg file: [" + propFile + "] ConfigurationException=" + ex;
            log.fatal(error);
            throw new CfgRuntimeException(GlobalErrIds.FT_CONFIG_BOOTSTRAP_FAILED, error, ex);
        }
        catch (SecurityException se)
        {
            String error = CLS_NM + " static init: Error loading from cfg file: [" + propFile + "] SecurityException=" + se;
            log.fatal(error);
            throw new CfgRuntimeException(GlobalErrIds.FT_CONFIG_INITIALIZE_FAILED, error, se);
        }
    }

    /**
     * Fetch the remote cfg params from ldap with given name.
     *
     * @param realmName required attribute contains the name of config node name on ldap.
     * @return {@link Properties} containing collection of name/value pairs found in directory.
     * @throws us.jts.fortress.SecurityException
     *          in the event of system or validation error.
     */
    private static Properties getRemoteConfig(String realmName) throws SecurityException
    {
        Properties props = null;
        try
        {
            ConfigMgr cfgMgr = ConfigMgrFactory.createInstance();
            props = cfgMgr.read(realmName);
        }
        catch (CfgException ce)
        {
            if (ce.getErrorId() == GlobalErrIds.FT_CONFIG_NOT_FOUND)
            {
                String warning = CLS_NM + ".getRemoteConfig could not find cfg entry";
                log.warn(warning);
            }
            else
            {
                throw ce;
            }
        }
        return props;
    }

    /**
     * Gets the prop attribute as String value from the apache commons cfg component.
     *
     * @param name contains the name of the property.
     * @return contains the value associated with the property or null if not not found.
     */
    public static String getProperty(String name)
    {
        String value = null;
        if (config != null)
        {
            value = (String) config.getProperty(name);
            if (log.isDebugEnabled())
                log.debug(CLS_NM + ".getProperty name [" + name + "] value [" + value + "]");
        }
        else
        {
            String error = CLS_NM + ".getProperty invalid config, can't read prop [" + name + "]";
            log.fatal(error);
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
    public static String getProperty(String name, String defaultValue)
    {
        String value = null;
        if (config != null)
        {
            value = (String) config.getProperty(name);
        }
        else
        {
            String warn = CLS_NM + ".getProperty invalid config, can't read prop [" + name + "]";
            log.warn(warn);
        }
        if (value == null || value.length() == 0)
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
    public static int getInt(String key)
    {
        int value = 0;
        if (config != null)
        {
            value = config.getInt(key);
        }
        else
        {
            String warn = CLS_NM + ".getInt invalid config, can't read prop [" + key + "]";
            log.warn(warn);
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
    public static int getInt(String key, int defaultValue)
    {
        int value = 0;
        if (config != null)
        {
            value = config.getInt(key, defaultValue);
        }
        else
        {
            String warn = CLS_NM + ".getInt invalid config, can't read prop [" + key + "]";
            log.warn(warn);
        }
        return value;
    }

    /**
     * Gets the boolean attribute associated with the name or false if not found.
     *
     * @param key name of the property name.
     * @return The boolean value or false if not found.
     */
    public static boolean getBoolean(String key)
    {
        boolean value = false;
        if (config != null)
        {
            value = config.getBoolean(key);
        }
        else
        {
            String warn = CLS_NM + ".getBoolean - invalid config, can't read prop [" + key + "]";
            log.warn(warn);
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
    public static boolean getBoolean(String key, boolean defaultValue)
    {
        boolean value = defaultValue;
        if (config != null)
        {
            value = config.getBoolean(key, defaultValue);
        }
        else
        {
            String warn = CLS_NM + ".getBoolean - invalid config, can't read prop [" + key + "]";
            log.warn(warn);
        }
        return value;
    }
}