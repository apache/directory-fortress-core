/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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

import org.apache.commons.lang3.StringUtils;
import org.apache.directory.fortress.core.impl.DelAccessMgrImpl;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.rest.DelAccessMgrRestImpl;
import org.apache.directory.fortress.core.util.ClassUtil;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * Creates an instance of the DelAccessMgr object.
 * <p>
 * The default implementation class is specified as {@link DelAccessMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#DELEGATED_ACCESS_IMPLEMENTATION} config property.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class DelAccessMgrFactory
{    
    private static final String CLS_NM = DelAccessMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link DelAccessMgr} object using HOME context.
     *
     * @return instance of {@link DelAccessMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelAccessMgr createInstance()
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }


    /**
     * Create and return a reference to {@link DelAccessMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return instance of {@link DelAccessMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelAccessMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        
        String accessClassName = Config.getInstance().getProperty(GlobalIds.DELEGATED_ACCESS_IMPLEMENTATION);
        
        DelAccessMgr accessMgr;

        if ( StringUtils.isEmpty( accessClassName ) )
        {
            if(Config.getInstance().isRestEnabled())
            {
                accessMgr = new DelAccessMgrRestImpl();
            }
            else
            {
                accessMgr = new DelAccessMgrImpl();
            }
        }
        else
        {
            accessMgr = (DelAccessMgr) ClassUtil.createInstance(accessClassName);
        }

        accessMgr.setContextId(contextId);
        return accessMgr;
    }


    /**
     * Create and return a reference to {@link DelAccessMgr} object using HOME context.
     *
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link DelAccessMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelAccessMgr createInstance(Session adminSess)
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME, adminSess );
    }


    /**
     * Create and return a reference to {@link DelAccessMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link DelAccessMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelAccessMgr createInstance(String contextId, Session adminSess)
        throws SecurityException
    {
        DelAccessMgr accessMgr = createInstance(contextId);
        accessMgr.setAdmin(adminSess);
        return accessMgr;
    }
}
