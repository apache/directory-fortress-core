/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.cfg.Config;
import com.jts.fortress.rbac.ClassUtil;
import com.jts.fortress.rbac.Session;
import com.jts.fortress.util.attr.VUtil;

/**
 * Creates an instance of the AdminMgr object.
 * The factory allows deployments of Fortress override the default AdminMgrImpl component with another.
 * <p/>
 * The default class is specified as {@link GlobalIds#ADMIN_DEFAULT_CLASS} but can be overridden by
 * adding the {@link GlobalIds#ADMIN_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 * @created August 30, 2009
 */
public class AdminMgrFactory
{
    private static String adminClassName = Config.getProperty(GlobalIds.ADMIN_IMPLEMENTATION);
    private static final String CLS_NM = AdminMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link com.jts.fortress.AdminMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link com.jts.fortress.AdminMgr}.
     * @throws com.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static AdminMgr createInstance(String contextId)
        throws com.jts.fortress.SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        if (adminClassName == null || adminClassName.compareTo("") == 0)
        {
            adminClassName = GlobalIds.ADMIN_DEFAULT_CLASS;
        }
        AdminMgr adminMgr = (AdminMgr) ClassUtil.createInstance(adminClassName);
        adminMgr.setContextId(contextId);
        return adminMgr;
    }

    /**
     * Create and return a reference to {@link com.jts.fortress.AdminMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link com.jts.fortress.AdminMgr}.
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

