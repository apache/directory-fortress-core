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
package org.openldap.fortress.ldap.group;

import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.cfg.Config;
import org.openldap.fortress.rbac.ClassUtil;
import org.openldap.fortress.SecurityException;
import org.openldap.fortress.rbac.Session;
import org.openldap.fortress.rest.ConfigMgrRestImpl;
import org.openldap.fortress.util.attr.VUtil;

/**
 * Creates an instance of the ConfigMgr object.
 * <p/>
 * The default implementation class is specified as {@link GroupMgrImpl} but can be overridden by
 * adding the {@link org.openldap.fortress.GlobalIds#GROUP_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 */
public class GroupMgrFactory
{
    private static String groupClassName = Config.getProperty( GlobalIds.GROUP_IMPLEMENTATION );
    private static final String CLS_NM = GroupMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link GroupMgr} object using HOME context.
     *
     * @return instance of {@link org.openldap.fortress.AdminMgr}.
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
     */
    public static GroupMgr createInstance()
        throws org.openldap.fortress.SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link GroupMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link GroupMgr}.
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
     */
    public static GroupMgr createInstance(String contextId)
        throws org.openldap.fortress.SecurityException
    {
        VUtil.assertNotNull( contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance" );
        if (!VUtil.isNotNullOrEmpty(groupClassName))
        {
            groupClassName = GroupMgrImpl.class.getName();
        }

        GroupMgr groupMgr = (GroupMgr) ClassUtil.createInstance(groupClassName);
        groupMgr.setContextId(contextId);
        return groupMgr;
    }


    /**
     * Create and return a reference to {@link GroupMgr} object using HOME context.
     *
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link org.openldap.fortress.AdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static GroupMgr createInstance(Session adminSess)
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME, adminSess );
    }

    /**
     * Create and return a reference to {@link GroupMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link org.openldap.fortress.AdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static GroupMgr createInstance(String contextId, Session adminSess)
        throws SecurityException
    {
        GroupMgr groupMgr = createInstance(contextId);
        groupMgr.setAdmin(adminSess);
        return groupMgr;
    }
}
