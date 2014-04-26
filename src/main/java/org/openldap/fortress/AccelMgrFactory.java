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
import org.openldap.fortress.rbac.AccelMgrImpl;
import org.openldap.fortress.rbac.ClassUtil;
import org.openldap.fortress.util.attr.VUtil;

/**
 * Creates an instance of the AccelMgr object.
 * <p/>
 * The default implementation class is specified as {@link AccelMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#ACCEL_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 */
public class AccelMgrFactory
{
    private static String accelClassName = Config.getProperty(GlobalIds.ACCEL_IMPLEMENTATION);
    private static final String CLS_NM = AccelMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link org.openldap.fortress.AccelMgr} object using HOME context.
     *
     * @return instance of {@link org.openldap.fortress.AccelMgr}.
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
     */
    public static AccelMgr createInstance()
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link org.openldap.fortress.AccelMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link org.openldap.fortress.AccelMgr}.
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
     */
    public static AccelMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        if (!VUtil.isNotNullOrEmpty(accelClassName))
        {
                accelClassName = AccelMgrImpl.class.getName();
        }

        AccelMgr accelMgr = (AccelMgr) ClassUtil.createInstance(accelClassName);
        accelMgr.setContextId(contextId);
        return accelMgr;
    }
}