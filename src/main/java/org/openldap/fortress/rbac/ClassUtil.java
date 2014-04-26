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

package org.openldap.fortress.rbac;

import org.openldap.fortress.CfgException;
import org.openldap.fortress.GlobalErrIds;

import java.io.InputStream;


/**
 * General purpose factory uses java reflection to instantiate new Manager object.
 * </p>
 * This class is called by the Manager factories:
 * <ol>
 * <li>{@link org.openldap.fortress.AccessMgrFactory}</li>
 * <li>{@link org.openldap.fortress.AdminMgrFactory}</li>
 * <li>{@link org.openldap.fortress.AuditMgrFactory}</li>
 * <li>{@link org.openldap.fortress.DelAccessMgrFactory}</li>
 * <li>{@link org.openldap.fortress.DelAdminMgrFactory}</li>
 * <li>{@link org.openldap.fortress.DelReviewMgrFactory}</li>
 * <li>{@link org.openldap.fortress.PwPolicyMgrFactory}</li>
 * <li>{@link org.openldap.fortress.ReviewMgrFactory}</li>
 * <li>{@link org.openldap.fortress.cfg.ConfigMgrFactory}</li>
 * </ol>
 *
 * @author Shawn McKinney
 */
public class ClassUtil
{
    /**
     * Given a valid class name call the default constructor through reflexion and return the reference to the caller.
     * @param className contains fully qualified java class name to be instantiated.  Must have a public default constructor to be successful.
     * @return reference to instantiated ManagerImpl object.
     * @throws org.openldap.fortress.CfgException in the event of failure to instantiate.
     *
     */
    public static Object createInstance(String className)
        throws CfgException
    {
        Object target;
        try
        {
            if (className == null || className.length() == 0)
            {
                String error = "createInstance() null or empty classname";
                throw new CfgException(GlobalErrIds.FT_MGR_CLASS_NAME_NULL, error);
            }
            target = Class.forName(className).newInstance();
        }
        catch (java.lang.ClassNotFoundException e)
        {
            String error = "createInstance() className [" + className + "] caught java.lang.ClassNotFoundException=" + e;
            throw new CfgException(GlobalErrIds.FT_MGR_CLASS_NOT_FOUND, error, e);
        }
        catch (java.lang.InstantiationException e)
        {
            String error = "createInstance()  [" + className + "] caught java.lang.InstantiationException=" + e;
            throw new CfgException(GlobalErrIds.FT_MGR_INST_EXCEPTION, error, e);
        }
        catch (java.lang.IllegalAccessException e)
        {
            String error = "createInstance()  [" + className + "] caught java.lang.IllegalAccessException=" + e;
            throw new CfgException(GlobalErrIds.FT_MGR_ILLEGAL_ACCESS, error, e);
        }
        return target;
	}


    /**
     * Find a file on the classloader and return as InputStream.
     * @param name contains the name of the file resource.
     * @return handle to the InputStream
     * @throws org.openldap.fortress.CfgException in the event resource is not found on classloader.
     */
    public static InputStream resourceAsStream(String name) throws CfgException
    {
        InputStream is;
        is = ClassUtil.class.getClassLoader().getResourceAsStream(name);
        if (is == null)
        {
            throw new CfgException(GlobalErrIds.FT_RESOURCE_NOT_FOUND, name);
        }
        return is;
    }
}
