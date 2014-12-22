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
import org.apache.directory.fortress.core.rbac.DelReviewMgrImpl;
import org.apache.directory.fortress.core.rbac.Session;
import org.apache.directory.fortress.core.rest.DelReviewMgrRestImpl;
import org.apache.directory.fortress.core.util.attr.VUtil;

/**
 * Creates an instance of the DelReviewMgr object.
 * <p/>
 * The default implementation class is specified as {@link DelReviewMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#DELEGATED_REVIEW_IMPLEMENTATION} config property.
 * <p/>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DelReviewMgrFactory
{
    private static String dReviewClassName = Config.getProperty(GlobalIds.DELEGATED_REVIEW_IMPLEMENTATION);
    private static final String CLS_NM = DelReviewMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link DelReviewMgr} object using HOME context.
     *
     * @return instance of {@link DelReviewMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelReviewMgr createInstance()
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link DelReviewMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link DelReviewMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelReviewMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        DelReviewMgr delReviewMgr;

        if (!VUtil.isNotNullOrEmpty(dReviewClassName))
        {
            if(GlobalIds.IS_REST)
            {
                delReviewMgr = new DelReviewMgrRestImpl();
            }
            else
            {
                delReviewMgr = new DelReviewMgrImpl();
            }
        }
        else
        {
            delReviewMgr = (DelReviewMgr) ClassUtil.createInstance(dReviewClassName);
        }

        delReviewMgr.setContextId(contextId);
        return delReviewMgr;
    }

    /**
     * Create and return a reference to {@link DelReviewMgr} object using HOME context.
     *
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link DelReviewMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelReviewMgr createInstance(Session adminSess)
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME, adminSess );
    }

    /**
     * Create and return a reference to {@link DelReviewMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link DelReviewMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelReviewMgr createInstance(String contextId, Session adminSess)
        throws SecurityException
    {
        DelReviewMgr delReviewMgr = createInstance(contextId);
        delReviewMgr.setAdmin(adminSess);
        return delReviewMgr;
    }
}

