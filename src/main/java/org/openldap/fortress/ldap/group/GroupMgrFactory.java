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
