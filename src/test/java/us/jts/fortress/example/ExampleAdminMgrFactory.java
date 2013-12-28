/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.example;

import us.jts.fortress.SecurityException;
import us.jts.fortress.rbac.ClassUtil;

/**
 * Factory class used to instantiate the ExampleAdminMgrImpl.
 *
 * @author Shawn McKinney
 * @created December 26, 2010
 */
public class ExampleAdminMgrFactory
{
    private static String exampleAdminClassName = us.jts.fortress.cfg.Config.getProperty(EIds.EXAMPLE_ADMIN_IMPLEMENTATION);


    public static ExampleAdminMgr createInstance()
        throws SecurityException
    {
        ExampleAdminMgr adminMgr;
        if (exampleAdminClassName == null || exampleAdminClassName.compareTo("") == 0)
        {
            exampleAdminClassName = EIds.EXAMPLE_ADMIN_DEFAULT_CLASS;
        }
        adminMgr = (ExampleAdminMgr) ClassUtil.createInstance(exampleAdminClassName);
        return adminMgr;
    }
}

