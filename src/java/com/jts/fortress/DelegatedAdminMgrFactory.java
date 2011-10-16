/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.configuration.Config;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.ClassUtil;

/**
 * Creates an instance of the DelegatedAdminMgr object.
 * <p/>
 * The default implementation class is specified as {@link com.jts.fortress.constants.GlobalIds#DELEGATED_ADMIN_DEFAULT_CLASS} but can be overridden by
 * adding the {@link com.jts.fortress.constants.GlobalIds#DELEGATED_ADMIN_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author smckinn
 * @created September 18, 2010
 */
public class DelegatedAdminMgrFactory
{
    private static String dAdminClassName = Config.getProperty(GlobalIds.DELEGATED_ADMIN_IMPLEMENTATION);

    /**
     * Create and return a reference to {@link com.jts.fortress.DelegatedAdminMgr} object.
     *
     * @return instance of {@link com.jts.fortress.DelegatedAdminMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static com.jts.fortress.DelegatedAdminMgr createInstance()
        throws SecurityException
    {
        com.jts.fortress.DelegatedAdminMgr dAdminMgr;
        if (dAdminClassName == null || dAdminClassName.compareTo("") == 0)
        {
            dAdminClassName = GlobalIds.DELEGATED_ADMIN_DEFAULT_CLASS;
        }
        dAdminMgr = (com.jts.fortress.DelegatedAdminMgr) ClassUtil.createInstance(dAdminClassName);
        return dAdminMgr;
    }
}

