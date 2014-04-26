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
import org.openldap.fortress.rbac.DelAdminMgrImpl;
import org.openldap.fortress.rbac.Session;
import org.openldap.fortress.rest.DelAdminMgrRestImpl;
import org.openldap.fortress.util.attr.VUtil;

/**
 * Creates an instance of the DelAdminMgr object.
 * <p/>
 * The default implementation class is specified as {@link DelAdminMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#DELEGATED_ADMIN_IMPLEMENTATION} config property.
 * <p/>
 *
 * @author Shawn McKinney
 */
public class DelAdminMgrFactory
{
    private static String dAdminClassName = Config.getProperty(GlobalIds.DELEGATED_ADMIN_IMPLEMENTATION);
    private static final String CLS_NM = DelAdminMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link DelAdminMgr} object using HOME context.
     *
     * @return instance of {@link DelAdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelAdminMgr createInstance()
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

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
     * Create and return a reference to {@link org.openldap.fortress.DelAdminMgr} object using HOME context.
     *
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link org.openldap.fortress.DelAdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static DelAdminMgr createInstance(Session adminSess)
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME, adminSess );
    }

    /**
     * Create and return a reference to {@link org.openldap.fortress.DelAdminMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link org.openldap.fortress.DelAdminMgr}.
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

