/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

/*
 *  This class is used for testing purposes.
 */
package us.jts.fortress;

import us.jts.fortress.cfg.ConfigMgr;
import us.jts.fortress.cfg.ConfigMgrFactory;
import us.jts.fortress.rbac.Session;

import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * * Test class for driving Fortress RBAC runtime policy APIs within a console.
 *
 * @author Shawn McKinney
 */
class ConfigMgrConsole
{
    private ConfigMgr cm = null;
    private Session session = null;
    private static final String CLS_NM = ConfigMgrConsole.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    /**
     * put your documentation comment here
     */
    public ConfigMgrConsole()
    {
        try
        {
            cm = ConfigMgrFactory.createInstance();
        }
        catch (SecurityException e)
        {
            LOG.error(" constructor caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
    }


    /**
     * Description of the Method
     */
    void addProp()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter config realm name:");
            String realm = ReaderUtil.readLn();
            System.out.println("Enter property name:");
            String key = ReaderUtil.readLn();
            System.out.println("Enter property value:");
            String value = ReaderUtil.readLn();
            Properties props = new Properties();
            props.setProperty(key, value);
            cm.add(realm, props);
            System.out.println("Property successfully added");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("addProp caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    void updateProp()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter config realm name:");
            String realm = ReaderUtil.readLn();
            System.out.println("Enter property name:");
            String key = ReaderUtil.readLn();
            System.out.println("Enter property value:");
            String value = ReaderUtil.readLn();
            Properties props = new Properties();
            props.setProperty(key, value);
            cm.add(realm, props);
            System.out.println("Property successfully updated");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("updateProp caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    void readProps()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter config realm name:");
            String realm = ReaderUtil.readLn();
            Properties props = cm.read(realm);
            int ctr = 0;
            for (Enumeration e = props.propertyNames(); e.hasMoreElements();)
            {
                String key = (String) e.nextElement();
                String val = props.getProperty(key);
                System.out.println("PROP[" + ++ctr + "]= key [" + key + "] value [" + val + "]");
            }

            System.out.println("Properties successfully read");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("readProps caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    void deleteProps()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter config realm name:");
            String realm = ReaderUtil.readLn();
            cm.delete(realm);
            System.out.println("Properties successfully deleted");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("deleteProps caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }
}