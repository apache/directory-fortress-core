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
import org.apache.directory.fortress.core.rbac.AccessMgrImpl;
import org.apache.directory.fortress.core.rbac.ClassUtil;
import org.apache.directory.fortress.core.rest.AccessMgrRestImpl;
import org.apache.directory.fortress.core.util.attr.VUtil;

/**
 * Creates an instance of the AccessMgr object.
 * <p/>
 * The default implementation class is specified as {@link AccessMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#ACCESS_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 */
public class AccessMgrFactory
{
    private static String accessClassName = Config.getProperty(GlobalIds.ACCESS_IMPLEMENTATION);
    private static final String CLS_NM = AccessMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link AccessMgr} object using HOME context.
     *
     * @return instance of {@link AccessMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static AccessMgr createInstance()
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link AccessMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link AccessMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static AccessMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        if (!VUtil.isNotNullOrEmpty(accessClassName))
        {
            if(GlobalIds.IS_REST)
            {
                accessClassName = AccessMgrRestImpl.class.getName();
            }
            else
            {
                accessClassName = AccessMgrImpl.class.getName();
            }
        }

        AccessMgr accessMgr = (AccessMgr) ClassUtil.createInstance(accessClassName);
        accessMgr.setContextId(contextId);
        return accessMgr;
    }
}