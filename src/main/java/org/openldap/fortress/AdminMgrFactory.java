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
import org.openldap.fortress.rbac.AdminMgrImpl;
import org.openldap.fortress.rbac.ClassUtil;
import org.openldap.fortress.rbac.Session;
import org.openldap.fortress.rest.AdminMgrRestImpl;
import org.openldap.fortress.util.attr.VUtil;

/**
 * Creates an instance of the AdminMgr object.
 * The factory allows deployments of Fortress override the default AdminMgrImpl component with another.
 * <p/>
 * The default class is specified as {@link AdminMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#ADMIN_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 */
public class AdminMgrFactory
{
    private static String adminClassName = Config.getProperty(GlobalIds.ADMIN_IMPLEMENTATION);
    private static final String CLS_NM = AdminMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link org.openldap.fortress.AdminMgr} object using HOME context.
     *
     * @return instance of {@link org.openldap.fortress.AdminMgr}.
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
     */
    public static AdminMgr createInstance()
        throws org.openldap.fortress.SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link org.openldap.fortress.AdminMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link org.openldap.fortress.AdminMgr}.
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
     */
    public static AdminMgr createInstance(String contextId)
        throws org.openldap.fortress.SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        if (!VUtil.isNotNullOrEmpty(adminClassName))
        {
            if(GlobalIds.IS_REST)
            {
                adminClassName = AdminMgrRestImpl.class.getName();
            }
            else
            {
                adminClassName = AdminMgrImpl.class.getName();
            }
        }

        AdminMgr adminMgr = (AdminMgr) ClassUtil.createInstance(adminClassName);
        adminMgr.setContextId(contextId);
        return adminMgr;
    }

    /**
     * Create and return a reference to {@link org.openldap.fortress.AdminMgr} object using HOME context.
     *
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link org.openldap.fortress.AdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static AdminMgr createInstance(Session adminSess)
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME, adminSess );
    }

    /**
     * Create and return a reference to {@link org.openldap.fortress.AdminMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link org.openldap.fortress.AdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static AdminMgr createInstance(String contextId, Session adminSess)
        throws SecurityException
    {
        AdminMgr adminMgr = createInstance(contextId);
        adminMgr.setAdmin(adminSess);
        return adminMgr;
    }
}

