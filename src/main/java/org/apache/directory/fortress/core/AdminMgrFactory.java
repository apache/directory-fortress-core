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
import org.apache.directory.fortress.core.impl.AdminMgrImpl;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.rest.AdminMgrRestImpl;
import org.apache.directory.fortress.core.util.ClassUtil;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * Creates an instance of the AdminMgr object.
 * The factory allows deployments of Fortress override the default AdminMgrImpl component with another.
 * <p>
 * The default class is specified as {@link AdminMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#ADMIN_IMPLEMENTATION} config property.
 * <p>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class AdminMgrFactory
{
    private static final String CLS_NM = AdminMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link AdminMgr} object using HOME context.
     *
     * @return instance of {@link AdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static AdminMgr createInstance()
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link AdminMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return instance of {@link AdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static AdminMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        
        String adminClassName = Config.getInstance().getProperty(GlobalIds.ADMIN_IMPLEMENTATION);
        
        AdminMgr adminMgr;

        if ( StringUtils.isEmpty( adminClassName ) )
        {
            if(Config.getInstance().isRestEnabled())
            {
                adminMgr = new AdminMgrRestImpl();
            }
            else
            {
                adminMgr = new AdminMgrImpl();
            }
        }
        else
        {
            adminMgr = (AdminMgr) ClassUtil.createInstance(adminClassName);
        }

        adminMgr.setContextId(contextId);
        return adminMgr;
    }

    /**
     * Create and return a reference to {@link AdminMgr} object using HOME context.
     *
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link AdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static AdminMgr createInstance(Session adminSess)
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME, adminSess );
    }

    /**
     * Create and return a reference to {@link AdminMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link AdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static AdminMgr createInstance(String contextId, Session adminSess)
        throws SecurityException
    {
        AdminMgr adminMgr = createInstance(contextId);
        adminMgr.setAdmin(adminSess);
        return adminMgr;
    }
}

