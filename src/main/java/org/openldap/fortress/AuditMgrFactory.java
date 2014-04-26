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
import org.openldap.fortress.rbac.AuditMgrImpl;
import org.openldap.fortress.rbac.ClassUtil;
import org.openldap.fortress.rbac.Session;
import org.openldap.fortress.rest.AuditMgrRestImpl;
import org.openldap.fortress.util.attr.VUtil;

/**
 * Creates an instance of the AuditMgr object.
 * <p/>
 * The default implementation class is specified as {@link AuditMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#AUDIT_IMPLEMENTATION} config property.
 * <p/>
 *
 * @author Shawn McKinney
 */
public class AuditMgrFactory
{
    private static String auditClassName = Config.getProperty(GlobalIds.AUDIT_IMPLEMENTATION);
    private static final String CLS_NM = AuditMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link org.openldap.fortress.AuditMgr} object using HOME context.
     *
     * @return instance of {@link org.openldap.fortress.AuditMgr}.
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
     */
    public static AuditMgr createInstance()
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link org.openldap.fortress.AuditMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link org.openldap.fortress.AuditMgr}.
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
     */
    public static AuditMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        if (!VUtil.isNotNullOrEmpty(auditClassName))
        {
            if(GlobalIds.IS_REST)
            {
                auditClassName = AuditMgrRestImpl.class.getName();
            }
            else
            {
                auditClassName = AuditMgrImpl.class.getName();
            }
        }

        AuditMgr auditMgr = (AuditMgr) ClassUtil.createInstance(auditClassName);
        auditMgr.setContextId(contextId);
        return auditMgr;
    }

    /**
     * Create and return a reference to {@link org.openldap.fortress.AuditMgr} object using HOME context.
     *
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link org.openldap.fortress.AuditMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static AuditMgr createInstance(Session adminSess)
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME, adminSess );
    }

    /**
     * Create and return a reference to {@link org.openldap.fortress.AuditMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link org.openldap.fortress.AuditMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static AuditMgr createInstance(String contextId, Session adminSess)
        throws SecurityException
    {
        AuditMgr auditMgr = createInstance(contextId);
        auditMgr.setAdmin(adminSess);
        return auditMgr;
    }
}