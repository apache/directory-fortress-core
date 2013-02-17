/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress;

import us.jts.fortress.cfg.Config;
import us.jts.fortress.rbac.AccessMgrImpl;
import us.jts.fortress.rbac.ClassUtil;
import us.jts.fortress.rbac.Session;
import us.jts.fortress.rest.AccessMgrRestImpl;
import us.jts.fortress.util.attr.VUtil;

/**
 * Creates an instance of the AccessMgr object.
 * <p/>
 * The default implementation class is specified as {@link AccessMgrRestImpl} but can be overridden by
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
     * Create and return a reference to {@link us.jts.fortress.AccessMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link us.jts.fortress.AccessMgr}.
     * @throws us.jts.fortress.SecurityException in the event of failure during instantiation.
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