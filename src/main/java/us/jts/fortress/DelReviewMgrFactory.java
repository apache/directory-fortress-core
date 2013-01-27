/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress;

import us.jts.fortress.cfg.Config;
import us.jts.fortress.rbac.ClassUtil;
import us.jts.fortress.rbac.DelReviewMgrImpl;
import us.jts.fortress.rbac.Session;
import us.jts.fortress.rest.DelReviewMgrRestImpl;
import us.jts.fortress.util.attr.VUtil;

/**
 * Creates an instance of the DelReviewMgr object.
 * <p/>
 * The default implementation class is specified as {@link DelReviewMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#DELEGATED_REVIEW_IMPLEMENTATION} config property.
 * <p/>
 *
 * @author Shawn McKinney
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
     * @throws us.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static DelReviewMgr createInstance(String contextId)
        throws us.jts.fortress.SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        if (!VUtil.isNotNullOrEmpty(dReviewClassName))
        {
            if(GlobalIds.IS_REST)
            {
                dReviewClassName = DelReviewMgrRestImpl.class.getName();
            }
            else
            {
                dReviewClassName = DelReviewMgrImpl.class.getName();
            }
        }

        DelReviewMgr delReviewMgr = (DelReviewMgr) ClassUtil.createInstance(dReviewClassName);
        delReviewMgr.setContextId(contextId);
        return delReviewMgr;
    }

    /**
     * Create and return a reference to {@link us.jts.fortress.DelReviewMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link us.jts.fortress.DelReviewMgr}.
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

