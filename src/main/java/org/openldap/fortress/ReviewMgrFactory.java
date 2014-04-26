/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */

package org.openldap.fortress;

import org.openldap.fortress.cfg.Config;
import org.openldap.fortress.rbac.ClassUtil;
import org.openldap.fortress.rbac.ReviewMgrImpl;
import org.openldap.fortress.rbac.Session;
import org.openldap.fortress.rest.ReviewMgrRestImpl;
import org.openldap.fortress.util.attr.VUtil;

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
     * Create and return a reference to {@link org.openldap.fortress.ReviewMgr} object using HOME context.
     *
     * @return instance of {@link org.openldap.fortress.ReviewMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static org.openldap.fortress.ReviewMgr createInstance()
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link org.openldap.fortress.ReviewMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link org.openldap.fortress.ReviewMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static org.openldap.fortress.ReviewMgr createInstance(String contextId)
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

        ReviewMgr reviewMgr = ( org.openldap.fortress.ReviewMgr) ClassUtil.createInstance(reviewClassName);
        reviewMgr.setContextId(contextId);
        return reviewMgr;
    }

    /**
     * Create and return a reference to {@link org.openldap.fortress.ReviewMgr} object using HOME context.
     *
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link org.openldap.fortress.ReviewMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static ReviewMgr createInstance(Session adminSess)
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME, adminSess );
    }

    /**
     * Create and return a reference to {@link org.openldap.fortress.ReviewMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link org.openldap.fortress.ReviewMgr}.
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

