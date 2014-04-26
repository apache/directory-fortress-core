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
import org.openldap.fortress.rbac.DelReviewMgrImpl;
import org.openldap.fortress.rbac.Session;
import org.openldap.fortress.rest.DelReviewMgrRestImpl;
import org.openldap.fortress.util.attr.VUtil;

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
     * Create and return a reference to {@link DelReviewMgr} object using HOME context.
     *
     * @return instance of {@link DelReviewMgr}.
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
     */
    public static DelReviewMgr createInstance()
        throws org.openldap.fortress.SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link DelReviewMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link DelReviewMgr}.
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
     */
    public static DelReviewMgr createInstance(String contextId)
        throws org.openldap.fortress.SecurityException
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
     * Create and return a reference to {@link org.openldap.fortress.DelReviewMgr} object using HOME context.
     *
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link org.openldap.fortress.DelReviewMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelReviewMgr createInstance(Session adminSess)
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME, adminSess );
    }

    /**
     * Create and return a reference to {@link org.openldap.fortress.DelReviewMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link org.openldap.fortress.DelReviewMgr}.
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

