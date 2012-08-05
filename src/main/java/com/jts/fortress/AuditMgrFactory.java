/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.cfg.Config;
import com.jts.fortress.rbac.ClassUtil;
import com.jts.fortress.util.attr.VUtil;

/**
 * Creates an instance of the AuditMgr object.
 * <p/>
 * The default implementation class is specified as {@link GlobalIds#AUDIT_DEFAULT_CLASS} but can be overridden by
 * adding the {@link GlobalIds#AUDIT_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 * @created April 2, 2010
 */
public class AuditMgrFactory
{
    private static String auditClassName = Config.getProperty(GlobalIds.AUDIT_IMPLEMENTATION);
    private static final String CLS_NM = AuditMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link com.jts.fortress.AuditMgr} object.
     *
     * @return instance of {@link com.jts.fortress.AuditMgr}.
     * @throws com.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static com.jts.fortress.AuditMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        com.jts.fortress.AuditMgr auditMgr;
        if (auditClassName == null || auditClassName.compareTo("") == 0)
        {
            auditClassName = GlobalIds.AUDIT_DEFAULT_CLASS;
        }
        auditMgr = (com.jts.fortress.AuditMgr) ClassUtil.createInstance(auditClassName);
        auditMgr.setContextId(contextId);
        return auditMgr;
    }
}