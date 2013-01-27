/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.cfg;

import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.GlobalIds;
import us.jts.fortress.SecurityException;
import us.jts.fortress.ValidationException;
import us.jts.fortress.util.attr.VUtil;
import org.apache.log4j.Logger;

import java.util.Properties;


/**
 * Process module for the configurations node used for remotely storing Fortress specific properties.
 * Fortress places no limits on the number of unique configurations that can be present.  The Fortress client will specify
 * the preferred cfg node by name via a property named, {@link us.jts.fortress.GlobalIds#CONFIG_REALM}.  Each
 * process using Fortress client is free to share existing node with other processes or create its own unique config
 * instance using the methods within this class.<BR>
 * This class does perform simple data validations to ensure data reasonability and the required fields are present.<BR>
 * The {@link us.jts.fortress.ant.FortressAntTask#addConfig()} method calls the {@link #add} from this class during initial base loads.
 * Removal {@link us.jts.fortress.ant.FortressAntTask#deleteConfig()} is performed when removal of properties is the aim.<BR>
 * This class will accept {@link Properties}, and forward on to it's corresponding DAO class {@link ConfigDAO} for updating properties stored on behalf of Fortress.
 * <p>
 * Class will throw {@link SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link us.jts.fortress.FinderException},
 * {@link us.jts.fortress.CreateException},{@link us.jts.fortress.UpdateException},{@link us.jts.fortress.RemoveException}),
 *  or {@link us.jts.fortress.ValidationException} as {@link us.jts.fortress.SecurityException}s with appropriate
 *  error id from {@link us.jts.fortress.GlobalErrIds}.
 * <p>
 * This class performs simple data validation on properties to ensure length does not exceed 100 and contents are safe text.
 * <p/>

 *
 * @author Shawn McKinney
 */
final class ConfigP
{
    private static final String CLS_NM = ConfigP.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);

    /**
     * Package private constructor
     */
    ConfigP()
    {
    }

    /**
     * Create a new cfg node with given name and properties.  The name is required.  If node already exists,
     * a {@link us.jts.fortress.SecurityException} with error {@link GlobalErrIds#FT_CONFIG_ALREADY_EXISTS} will be thrown.
     *
     * @param name    attribute is required and maps to 'cn' attribute in 'device' object class.
     * @param inProps contains {@link Properties} with list of name/value pairs to remove from existing config node.
     * @return {@link Properties} containing the collection of name/value pairs just added.
     * @throws SecurityException in the event entry already present or other system error.
     */
    final Properties add(String name, Properties inProps)
        throws SecurityException
    {
        validate(name, inProps);
        ConfigDAO cfgDao = new ConfigDAO();
        return cfgDao.create(name, inProps);
    }


    /**
     * Update existing cfg node with additional properties, or, replace existing properties.  The name is required.  If node does not exist,
     * a {@link SecurityException} with error {@link us.jts.fortress.GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     *
     * @param name    attribute is required and maps to 'cn' attribute in 'device' object class.
     * @param inProps contains {@link Properties} with list of name/value pairs to add or udpate from existing config node.
     * @return {@link Properties} containing the collection of name/value pairs to be added to existing node.
     * @throws us.jts.fortress.SecurityException in the event entry not present or other system error.
     */
    final Properties update(String name, Properties inProps)
        throws us.jts.fortress.SecurityException
    {
        validate(name, inProps);
        ConfigDAO cfgDao = new ConfigDAO();
        return cfgDao.update(name, inProps);
    }


    /**
     * Delete existing cfg node which will remove all properties associated with that node.
     * The name is required.  If node does not exist, a {@link SecurityException} with error
     * {@link GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     * <p/>
     * <font size="2" color="red">
     * This method is destructive and will remove the cfg node completely from directory.<BR>
     * Care should be taken during execution to ensure target name is correct and permanent removal of all parameters located
     * there is intended.  There is no 'undo' for this operation.
     * </font>
     * <p/>
     * @param name attribute is required and maps to 'cn' attribute in 'device' object class.
     * @return {@link Properties} containing the collection of name/value pairs to be added to existing node.
     * @throws SecurityException in the event entry not present or other system error.
     */
    final void delete(String name)
        throws us.jts.fortress.SecurityException
    {
        if (!VUtil.isNotNullOrEmpty(name))
        {
            String error = CLS_NM + ".delete detected null config realm name";
            log.warn(error);
            throw new ValidationException(GlobalErrIds.FT_CONFIG_NAME_NULL, error);
        }
        ConfigDAO cfgDao = new ConfigDAO();
        cfgDao.remove(name);
    }

    /**
     * Delete existing cfg node with additional properties, or, replace existing properties.  The name is required.  If node does not exist,
     * a {@link SecurityException} with error {@link us.jts.fortress.GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     *
     * @param name attribute is required and maps to 'cn' attribute in 'device' object class.
     * @throws us.jts.fortress.SecurityException in the event entry not present or other system error.
     */
    final void delete(String name, Properties inProps)
        throws SecurityException
    {
        validate(name, inProps);
        ConfigDAO cfgDao = new ConfigDAO();
        cfgDao.remove(name, inProps);
    }

    /**
     * Read an existing cfg node with given name and return to caller.  The name is required.  If node doesn't exist,
     * a {@link SecurityException} with error {@link us.jts.fortress.GlobalErrIds#FT_CONFIG_NOT_FOUND} will be thrown.
     *
     * @param name attribute is required and maps to 'cn' attribute in 'device' object class.
     * @return {@link Properties} containing the collection of name/value pairs just added. Maps to 'ftProps' attribute in 'ftProperties' object class.
     * @throws us.jts.fortress.SecurityException in the event entry doesn't exist or other system error.
     */
    final Properties read(String name)
        throws SecurityException
    {
        Properties outProps;
        ConfigDAO cfgDao = new ConfigDAO();
        outProps = cfgDao.getConfig(name);
        return outProps;
    }


    /**
     * Method will perform simple validations to ensure the integrity of the {@link Properties} entity targeted for insertion
     * or deletion in directory.
     *
     * @param name contains the name of the cfg node.
     * @param entity contains the name/value properties targeted for operation.
     * @throws us.jts.fortress.ValidationException thrown in the event the validations fail.
     */
    private void validate(String name, Properties entity)
        throws us.jts.fortress.ValidationException
    {
        if(!VUtil.isNotNullOrEmpty(name))
        {
            String error = CLS_NM + ".validate detected null config realm name";
            log.warn(error);
            throw new ValidationException(GlobalErrIds.FT_CONFIG_NAME_NULL, error);
        }
        if (name.length() > GlobalIds.OU_LEN)
        {
            String error = CLS_NM + ".validate name [" + name + "] invalid length [" + name.length() + "]";
            log.warn(error);
            throw new ValidationException(GlobalErrIds.FT_CONFIG_NAME_INVLD, error);
        }
        if (entity == null || entity.size() == 0)
        {
            String error = CLS_NM + ".validate name [" + name + "] config props null";
            log.warn(error);
            throw new ValidationException(GlobalErrIds.FT_CONFIG_PROPS_NULL, error);
        }
        VUtil.properties(entity);
    }
}

