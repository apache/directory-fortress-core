/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */

package org.openldap.fortress.cfg;

import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.rbac.ClassUtil;
import org.openldap.fortress.SecurityException;
import org.openldap.fortress.rest.ConfigMgrRestImpl;

/**
 * Creates an instance of the ConfigMgr object.
 * <p/>
 * The default implementation class is specified as {@link ConfigMgrImpl} but can be overridden by
 * adding the {@link org.openldap.fortress.GlobalIds#CONFIG_IMPLEMENTATION} config property.
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
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
     */
    public static ConfigMgr createInstance()
        throws SecurityException
    {
        // TODO: Don't reuse {@link VUtil#isNotNullOrEmpty} here until it is determined why it forces different execution path through GlobalIds.IS_OPENLDAP:
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