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
import org.openldap.fortress.rbac.AccessMgrImpl;
import org.openldap.fortress.rbac.ClassUtil;
import org.openldap.fortress.rest.AccessMgrRestImpl;
import org.openldap.fortress.util.attr.VUtil;

/**
 * Creates an instance of the AccessMgr object.
 * <p/>
 * The default implementation class is specified as {@link AccessMgrImpl} but can be overridden by
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
     * Create and return a reference to {@link org.openldap.fortress.AccessMgr} object using HOME context.
     *
     * @return instance of {@link org.openldap.fortress.AccessMgr}.
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
     */
    public static AccessMgr createInstance()
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link org.openldap.fortress.AccessMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link org.openldap.fortress.AccessMgr}.
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
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