/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.cfg.Config;
import com.jts.fortress.rbac.ClassUtil;
import com.jts.fortress.util.attr.VUtil;
import org.apache.log4j.Logger;

/**
 * Creates an instance of the ReviewMgr object.
 * <p/>
 * The default implementation class is specified as {@link GlobalIds#REVIEW_DEFAULT_CLASS} but can be overridden by
 * adding the {@link GlobalIds#REVIEW_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 * @created August 30, 2009
 */
public class ReviewMgrFactory
{
    private static Logger log = Logger.getLogger(ReviewMgrFactory.class.getName());
    private static String reviewClassName = Config.getProperty(GlobalIds.REVIEW_IMPLEMENTATION);
    private static final String CLS_NM = ReviewMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link com.jts.fortress.ReviewMgr} object.
     *
     * @return instance of {@link com.jts.fortress.ReviewMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static com.jts.fortress.ReviewMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        com.jts.fortress.ReviewMgr reviewMgr;
        if (reviewClassName == null || reviewClassName.compareTo("") == 0)
        {
            reviewClassName = GlobalIds.REVIEW_DEFAULT_CLASS;
        }
        reviewMgr = (com.jts.fortress.ReviewMgr) ClassUtil.createInstance(reviewClassName);
        reviewMgr.setContextId(contextId);
        return reviewMgr;
    }
}

