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

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.rest.PropertyMgrRestImpl;
import org.apache.directory.fortress.core.util.ClassUtil;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.VUtil;

public class PropertyMgrFactory
{

    private static final String CLS_NM = PropertyMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link PropertyMgr} object using HOME context.
     *
     * @return instance of {@link PropertyMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static PropertyMgr createInstance()
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link PropertyMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return instance of {@link PropertyMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static PropertyMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        
        String propertyClassName = Config.getInstance().getProperty(GlobalIds.PROPERTY_IMPLEMENTATION);
        
        PropertyMgr propertyMgr;

        if ( StringUtils.isEmpty( propertyClassName ) )
        {
            if(Config.getInstance().isRestEnabled())
            {
                propertyMgr = new PropertyMgrRestImpl();
            }
            else
            {
                propertyMgr = new org.apache.directory.fortress.core.impl.PropertyMgrImpl();
            }
        }
        else
        {
            propertyMgr = ( PropertyMgr ) ClassUtil.createInstance(propertyClassName);
        }

        propertyMgr.setContextId(contextId);
        return propertyMgr;
    }

    /**
     * Create and return a reference to {@link PropertyMgr} object using HOME context.
     *
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link PropertyMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static PropertyMgr createInstance(Session adminSess)
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME, adminSess );
    }

    /**
     * Create and return a reference to {@link PropertyMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link PropertyMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static PropertyMgr createInstance(String contextId, Session adminSess)
        throws SecurityException
    {
        PropertyMgr propertyMgr = createInstance(contextId);
        propertyMgr.setAdmin(adminSess);
        return propertyMgr;
    }
    
}
