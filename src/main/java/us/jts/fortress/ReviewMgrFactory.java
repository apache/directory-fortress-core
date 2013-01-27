/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress;

import us.jts.fortress.cfg.Config;
import us.jts.fortress.rbac.ClassUtil;
import us.jts.fortress.rbac.ReviewMgrImpl;
import us.jts.fortress.rbac.Session;
import us.jts.fortress.rest.ReviewMgrRestImpl;
import us.jts.fortress.util.attr.VUtil;

/**
 * Creates an instance of the ReviewMgr object.
 * <p/>
 * The default implementation class is specified as {@link ReviewMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#REVIEW_IMPLEMENTATION} config property.
 * <p/>
 *
 * @author Shawn McKinney
 */
public class ReviewMgrFactory
{
    private static String reviewClassName = Config.getProperty(GlobalIds.REVIEW_IMPLEMENTATION);
    private static final String CLS_NM = ReviewMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link us.jts.fortress.ReviewMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link us.jts.fortress.ReviewMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static us.jts.fortress.ReviewMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        if (!VUtil.isNotNullOrEmpty(reviewClassName))
        {
            if(GlobalIds.IS_REST)
            {
                reviewClassName = ReviewMgrRestImpl.class.getName();
            }
            else
            {
                reviewClassName = ReviewMgrImpl.class.getName();
            }
        }

        ReviewMgr reviewMgr = (us.jts.fortress.ReviewMgr) ClassUtil.createInstance(reviewClassName);
        reviewMgr.setContextId(contextId);
        return reviewMgr;
    }

    /**
     * Create and return a reference to {@link us.jts.fortress.ReviewMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link us.jts.fortress.ReviewMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static ReviewMgr createInstance(String contextId, Session adminSess)
        throws SecurityException
    {
        ReviewMgr reviewMgr = createInstance(contextId);
        reviewMgr.setAdmin(adminSess);
        return reviewMgr;
    }
}

