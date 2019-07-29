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
package org.apache.directory.fortress.core;

import org.apache.directory.fortress.core.model.Configuration;
import org.apache.directory.fortress.core.model.Session;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.directory.fortress.core.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * * Test class for driving Fortress RBAC runtime policy APIs within a console.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
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
        catch ( org.apache.directory.fortress.core.SecurityException e)
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
            cm.add(new Configuration(realm, props));
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
            cm.update(new Configuration(realm, props));
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
            Configuration configuration = cm.read(realm);
            Properties props = configuration.getProperties();
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


    void getProperty()
    {
        ReaderUtil.clearScreen();
        System.out.println("Enter config param key:");
        String key = ReaderUtil.readLn();
        String value = Config.getInstance().getProperty(key);
        System.out.println("PROP=" + key + " VALUE=" + value);
        System.out.println("ENTER to continue");
        ReaderUtil.readChar();
    }


    void getInt()
    {
        ReaderUtil.clearScreen();
        System.out.println("Enter config param key:");
        String key = ReaderUtil.readLn();
        int value = Config.getInstance().getInt(key);
        System.out.println("PROP=" + key + " VALUE=" + value);
        System.out.println("ENTER to continue");
        ReaderUtil.readChar();
    }

}