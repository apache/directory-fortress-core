/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.cfg;

import com.jts.fortress.GlobalIds;
import com.jts.fortress.rbac.ClassUtil;
import com.jts.fortress.SecurityException;
import com.jts.fortress.rest.ConfigMgrRestImpl;

/**
 * Creates an instance of the ConfigMgr object.
 * <p/>
 * The default implementation class is specified as {@link ConfigMgrImpl} but can be overridden by
 * adding the {@link com.jts.fortress.GlobalIds#CONFIG_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 * @created February 5, 2011
 */
public class ConfigMgrFactory
{
    private static String configClassName = Config.getProperty(GlobalIds.CONFIG_IMPLEMENTATION);
    private final static String ENABLE_REST = "enable.mgr.impl.rest";
    private static final boolean IS_REST = ((Config.getProperty(ENABLE_REST) != null) && (Config.getProperty(ENABLE_REST).equalsIgnoreCase("true")));

    /**
     * Create and return a reference to {@link ConfigMgr} object.
     *
     * @return instance of {@link ConfigMgr}.
     * @throws com.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static ConfigMgr createInstance()
        throws SecurityException
    {
        // TODO: Don't reuse {@link VUtil#isNotNullOrEmpty} here until it is determined why it forces different execution path through GlobalIds.OPENLDAP_IS_PW_POLICY_ENABLED:
        //if (!VUtil.isNotNullOrEmpty(configClassName))
        if (configClassName == null || configClassName.compareTo("") == 0)
        {
            if(IS_REST)
            {
                configClassName = ConfigMgrRestImpl.class.getName();
            }
            else
            {
                configClassName = ConfigMgrImpl.class.getName();
            }
        }

        ConfigMgr configMgr = (ConfigMgr) ClassUtil.createInstance(configClassName);
        return configMgr;
    }
}