/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.configuration.Config;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.ClassUtil;

/**
 * Creates an instance of the DelegatedReviewMgr object.
 * <p/>
 * The default implementation class is specified as {@link com.jts.fortress.constants.GlobalIds#DELEGATED_REVIEW_DEFAULT_CLASS} but can be overridden by
 * adding the {@link com.jts.fortress.constants.GlobalIds#DELEGATED_REVIEW_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author smckinn
 * @created September 18, 2010
 */
public class DelegatedReviewMgrFactory
{
    private static String dReviewClassName = Config.getProperty(GlobalIds.DELEGATED_REVIEW_IMPLEMENTATION);

    /**
     * Create and return a reference to {@link com.jts.fortress.DelegatedReviewMgr} object.
     *
     * @return instance of {@link com.jts.fortress.DelegatedReviewMgr}.
     * @throws com.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static com.jts.fortress.DelegatedReviewMgr createInstance()
        throws com.jts.fortress.SecurityException
    {
        com.jts.fortress.DelegatedReviewMgr dReviewMgr;
        if (dReviewClassName == null || dReviewClassName.compareTo("") == 0)
        {
            dReviewClassName = GlobalIds.DELEGATED_REVIEW_DEFAULT_CLASS;
        }
        dReviewMgr = (com.jts.fortress.DelegatedReviewMgr) ClassUtil.createInstance(dReviewClassName);
        return dReviewMgr;
    }
}

