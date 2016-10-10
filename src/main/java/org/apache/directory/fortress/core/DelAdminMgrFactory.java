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

import org.apache.directory.api.util.Strings;
import org.apache.directory.fortress.core.impl.DelAdminMgrImpl;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.rest.DelAdminMgrRestImpl;
import org.apache.directory.fortress.core.util.ClassUtil;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * Creates an instance of the DelAdminMgr object.
 * <p>
 * The default implementation class is specified as {@link DelAdminMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#DELEGATED_ADMIN_IMPLEMENTATION} config property.
 * <p>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class DelAdminMgrFactory
{
    private static final String CLS_NM = DelAdminMgrFactory.class.getName();
    private static final String CREATE_INSTANCE_METHOD = CLS_NM + ".createInstance";

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
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return instance of {@link DelAdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelAdminMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull( contextId, GlobalErrIds.CONTEXT_NULL, CREATE_INSTANCE_METHOD );
        
        String dAdminClassName = Config.getInstance().getProperty(GlobalIds.DELEGATED_ADMIN_IMPLEMENTATION);
        
        DelAdminMgr delAdminMgr;

        if ( Strings.isEmpty( dAdminClassName ) )
        {
            if ( Config.getInstance().isRestEnabled() )
            {
                delAdminMgr = new DelAdminMgrRestImpl();
            }
            else
            {
                delAdminMgr = new DelAdminMgrImpl();
            }
        }
        else
        {
            delAdminMgr = (DelAdminMgr) ClassUtil.createInstance( dAdminClassName );
        }

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
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
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

