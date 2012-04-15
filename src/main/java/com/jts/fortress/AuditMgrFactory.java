/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.configuration.Config;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.ClassUtil;

/**
 * Creates an instance of the AuditMgr object.
 * <p/>
 * The default implementation class is specified as {@link com.jts.fortress.constants.GlobalIds#AUDIT_DEFAULT_CLASS} but can be overridden by
 * adding the {@link com.jts.fortress.constants.GlobalIds#AUDIT_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 * @created April 2, 2010
 */
public class AuditMgrFactory
{
    private static String auditClassName = Config.getProperty(GlobalIds.AUDIT_IMPLEMENTATION);

    /**
     * Create and return a reference to {@link com.jts.fortress.AuditMgr} object.
     *
     * @return instance of {@link com.jts.fortress.AuditMgr}.
     * @throws com.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static com.jts.fortress.AuditMgr createInstance()
        throws SecurityException
    {
        com.jts.fortress.AuditMgr auditMgr;
        if (auditClassName == null || auditClassName.compareTo("") == 0)
        {
            auditClassName = GlobalIds.AUDIT_DEFAULT_CLASS;
        }
        auditMgr = (com.jts.fortress.AuditMgr) ClassUtil.createInstance(auditClassName);
        return auditMgr;
    }
}