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

import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.rbac.ClassUtil;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.rest.ConfigMgrRestImpl;

/**
 * Creates an instance of the ConfigMgr object.
 * <p/>
 * The default implementation class is specified as {@link ConfigMgrImpl} but can be overridden by
 * adding the {@link org.apache.directory.fortress.core.GlobalIds#CONFIG_IMPLEMENTATION} config property.
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
     * @throws org.apache.directory.fortress.core.SecurityException in the event of failure during instantiation.
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