/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.example;

import com.jts.fortress.SecurityException;

/**
 * Factory class used to instantiate the ExampleAdminMgrImpl.
 *
 * @author smckinn
 * @created December 26, 2010
 */
public class ExampleAdminMgrFactory
{
    private static String exampleAdminClassName = com.jts.fortress.configuration.Config.getProperty(EIds.EXAMPLE_ADMIN_IMPLEMENTATION);


    public static ExampleAdminMgr createInstance()
        throws SecurityException
    {
        ExampleAdminMgr adminMgr;
        if (exampleAdminClassName == null || exampleAdminClassName.compareTo("") == 0)
        {
            exampleAdminClassName = EIds.EXAMPLE_ADMIN_DEFAULT_CLASS;
        }
        adminMgr = (ExampleAdminMgr) com.jts.fortress.util.ClassUtil.createInstance(exampleAdminClassName);
        return adminMgr;
    }
}

