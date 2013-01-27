/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress;

import us.jts.fortress.cfg.Config;
import us.jts.fortress.rbac.AdminMgrImpl;
import us.jts.fortress.rbac.ClassUtil;
import us.jts.fortress.rbac.Session;
import us.jts.fortress.rest.AdminMgrRestImpl;
import us.jts.fortress.util.attr.VUtil;

/**
 * Creates an instance of the AdminMgr object.
 * The factory allows deployments of Fortress override the default AdminMgrImpl component with another.
 * <p/>
 * The default class is specified as {@link AdminMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#ADMIN_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 */
public class AdminMgrFactory
{
    private static String adminClassName = Config.getProperty(GlobalIds.ADMIN_IMPLEMENTATION);
    private static final String CLS_NM = AdminMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link us.jts.fortress.AdminMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link us.jts.fortress.AdminMgr}.
     * @throws us.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static AdminMgr createInstance(String contextId)
        throws us.jts.fortress.SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        if (!VUtil.isNotNullOrEmpty(adminClassName))
        {
            if(GlobalIds.IS_REST)
            {
                adminClassName = AdminMgrRestImpl.class.getName();
            }
            else
            {
                adminClassName = AdminMgrImpl.class.getName();
            }
        }

        AdminMgr adminMgr = (AdminMgr) ClassUtil.createInstance(adminClassName);
        adminMgr.setContextId(contextId);
        return adminMgr;
    }

    /**
     * Create and return a reference to {@link us.jts.fortress.AdminMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link us.jts.fortress.AdminMgr}.
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

