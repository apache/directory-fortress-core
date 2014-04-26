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

package org.openldap.fortress.example;

import org.openldap.fortress.SecurityException;
import org.openldap.fortress.rbac.ClassUtil;

/**
 * Factory class used to instantiate the ExampleAdminMgrImpl.
 *
 * @author Shawn McKinney
 * @created December 26, 2010
 */
public class ExampleAdminMgrFactory
{
    private static String exampleAdminClassName = org.openldap.fortress.cfg.Config.getProperty(EIds.EXAMPLE_ADMIN_IMPLEMENTATION);


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

