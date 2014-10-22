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

import org.apache.directory.fortress.core.cfg.Config;
import org.apache.directory.fortress.core.rbac.ClassUtil;
import org.apache.directory.fortress.core.rbac.DelAdminMgrImpl;
import org.apache.directory.fortress.core.rbac.Session;
import org.apache.directory.fortress.core.rest.DelAdminMgrRestImpl;
import org.apache.directory.fortress.core.util.attr.VUtil;

/**
 * Creates an instance of the DelAdminMgr object.
 * <p/>
 * The default implementation class is specified as {@link DelAdminMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#DELEGATED_ADMIN_IMPLEMENTATION} config property.
 * <p/>
 *
 * @author Shawn McKinney
 */
public class DelAdminMgrFactory
{
    private static String dAdminClassName = Config.getProperty(GlobalIds.DELEGATED_ADMIN_IMPLEMENTATION);
    private static final String CLS_NM = DelAdminMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link DelAdminMgr} object using HOME context.
     *
     * @return instance of {@link DelAdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelAdminMgr createInstance()
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link DelAdminMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link DelAdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelAdminMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        if (!VUtil.isNotNullOrEmpty(dAdminClassName))
        {
            if(GlobalIds.IS_REST)
            {
                dAdminClassName = DelAdminMgrRestImpl.class.getName();
            }
            else
            {
                dAdminClassName = DelAdminMgrImpl.class.getName();
            }
        }

        DelAdminMgr delAdminMgr = (DelAdminMgr) ClassUtil.createInstance(dAdminClassName);
        delAdminMgr.setContextId(contextId);
        return delAdminMgr;
    }

    /**
     * Create and return a reference to {@link DelAdminMgr} object using HOME context.
     *
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link DelAdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelAdminMgr createInstance(Session adminSess)
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME, adminSess );
    }

    /**
     * Create and return a reference to {@link DelAdminMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link DelAdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelAdminMgr createInstance(String contextId, Session adminSess)
        throws SecurityException
    {
        DelAdminMgr delAdminMgr = createInstance(contextId);
        delAdminMgr.setAdmin(adminSess);
        return delAdminMgr;
    }
}

