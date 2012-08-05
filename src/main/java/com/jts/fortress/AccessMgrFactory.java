/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.cfg.Config;
import com.jts.fortress.rbac.ClassUtil;
import com.jts.fortress.rbac.Session;
import com.jts.fortress.util.attr.VUtil;

/**
 * Creates an instance of the AccessMgr object.
 * <p/>
 * The default implementation class is specified as {@link GlobalIds#ACCESS_DEFAULT_CLASS} but can be overridden by
 * adding the {@link GlobalIds#ACCESS_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 * @created October 13, 2009
 */
public class AccessMgrFactory
{
    private static String accessClassName = Config.getProperty(GlobalIds.ACCESS_IMPLEMENTATION);
    private static final String CLS_NM = AccessMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link com.jts.fortress.AccessMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link com.jts.fortress.AccessMgr}.
     * @throws com.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static AccessMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        com.jts.fortress.AccessMgr accessMgr;
        if (accessClassName == null || accessClassName.compareTo("") == 0)
        {
            accessClassName = GlobalIds.ACCESS_DEFAULT_CLASS;
        }
        accessMgr = (AccessMgr) ClassUtil.createInstance(accessClassName);
        accessMgr.setContextId(contextId);
        return accessMgr;
    }


    /**
     * Create and return a reference to {@link com.jts.fortress.AccessMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link com.jts.fortress.AccessMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static AccessMgr createInstance(String contextId, Session adminSess)
        throws SecurityException
    {
        AccessMgr accessMgr = createInstance(contextId);
        accessMgr.setAdmin(adminSess);
        return accessMgr;
    }
}