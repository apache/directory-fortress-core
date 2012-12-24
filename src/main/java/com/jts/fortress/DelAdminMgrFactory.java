/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.cfg.Config;
import com.jts.fortress.rbac.ClassUtil;
import com.jts.fortress.rbac.DelAdminMgrImpl;
import com.jts.fortress.rbac.Session;
import com.jts.fortress.rest.DelAdminMgrRestImpl;
import com.jts.fortress.util.attr.VUtil;

/**
 * Creates an instance of the DelAdminMgr object.
 * <p/>
 * The default implementation class is specified as {@link DelAdminMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#DELEGATED_ADMIN_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 * @created September 18, 2010
 */
public class DelAdminMgrFactory
{
    private static String dAdminClassName = Config.getProperty(GlobalIds.DELEGATED_ADMIN_IMPLEMENTATION);
    private static final String CLS_NM = DelAdminMgrFactory.class.getName();

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
     * Create and return a reference to {@link com.jts.fortress.DelAdminMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link com.jts.fortress.DelAdminMgr}.
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

