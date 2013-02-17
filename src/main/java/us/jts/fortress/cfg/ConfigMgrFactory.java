/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.cfg;

import us.jts.fortress.GlobalIds;
import us.jts.fortress.rbac.ClassUtil;
import us.jts.fortress.SecurityException;
import us.jts.fortress.rest.ConfigMgrRestImpl;

/**
 * Creates an instance of the ConfigMgr object.
 * <p/>
 * The default implementation class is specified as {@link ConfigMgrImpl} but can be overridden by
 * adding the {@link us.jts.fortress.GlobalIds#CONFIG_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
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
     * @throws us.jts.fortress.SecurityException in the event of failure during instantiation.
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

        return (ConfigMgr) ClassUtil.createInstance(configClassName);
    }
}