/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.cfg.Config;
import com.jts.fortress.rbac.ClassUtil;
import com.jts.fortress.rbac.Session;
import com.jts.fortress.util.attr.VUtil;

/**
 * Creates an instance of the DelReviewMgr object.
 * <p/>
 * The default implementation class is specified as {@link GlobalIds#DELEGATED_REVIEW_DEFAULT_CLASS} but can be overridden by
 * adding the {@link GlobalIds#DELEGATED_REVIEW_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 * @created September 18, 2010
 */
public class DelReviewMgrFactory
{
    private static String dReviewClassName = Config.getProperty(GlobalIds.DELEGATED_REVIEW_IMPLEMENTATION);
    private static final String CLS_NM = DelReviewMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link DelReviewMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link DelReviewMgr}.
     * @throws com.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static DelReviewMgr createInstance(String contextId)
        throws com.jts.fortress.SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        DelReviewMgr delReviewMgr;
        if (dReviewClassName == null || dReviewClassName.compareTo("") == 0)
        {
            dReviewClassName = GlobalIds.DELEGATED_REVIEW_DEFAULT_CLASS;
        }
        delReviewMgr = (DelReviewMgr) ClassUtil.createInstance(dReviewClassName);
        delReviewMgr.setContextId(contextId);
        return delReviewMgr;
    }

    /**
     * Create and return a reference to {@link com.jts.fortress.DelReviewMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link com.jts.fortress.DelReviewMgr}.
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

