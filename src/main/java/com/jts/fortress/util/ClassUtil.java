/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.util;

import com.jts.fortress.ConfigurationException;
import com.jts.fortress.constants.GlobalErrIds;

import java.io.InputStream;


/**
 * General purpose factory uses java reflection to instantiate new Manager object.
 * </p>
 * This class is called by the Manager factories:
 * <ol>
 * <li>{@link com.jts.fortress.AccessMgrFactory}</li>
 * <li>{@link com.jts.fortress.AdminMgrFactory}</li>
 * <li>{@link com.jts.fortress.AuditMgrFactory}</li>
 * <li>{@link com.jts.fortress.DelegatedAccessMgrFactory}</li>
 * <li>{@link com.jts.fortress.DelegatedAdminMgrFactory}</li>
 * <li>{@link com.jts.fortress.DelegatedReviewMgrFactory}</li>
 * <li>{@link com.jts.fortress.PswdPolicyMgrFactory}</li>
 * <li>{@link com.jts.fortress.ReviewMgrFactory}</li>
 * <li>{@link com.jts.fortress.configuration.ConfigMgrFactory}</li>
 * </ol>
 *
 * @author Shawn McKinney
 * @created February 13, 2010
 */
public class ClassUtil
{
    private static final String CLS_NM = ClassUtil.class.getName();

    /**
     * Given a valid class name call the default constructor through reflexion and return the reference to the caller.
     * @param className contains fully qualified java class name to be instantiated.  Must have a public default constructor to be successful.
     * @return reference to instantiated ManagerImpl object.
     * @throws com.jts.fortress.ConfigurationException in the event of failure to instantiate.
     *
     */
    public static Object createInstance(String className)
        throws com.jts.fortress.ConfigurationException
    {
        Object target;
        try
        {
            if (className == null || className.length() == 0)
            {
                String error = CLS_NM + ".createInstance() null or empty classname";
                throw new com.jts.fortress.ConfigurationException(GlobalErrIds.FT_MGR_CLASS_NAME_NULL, error);
            }
            target = Class.forName(className).newInstance();
        }
        catch (java.lang.ClassNotFoundException e)
        {
            String error = CLS_NM + ".createInstance() className [" + className + "] caught java.lang.ClassNotFoundException=" + e;
            throw new com.jts.fortress.ConfigurationException(GlobalErrIds.FT_MGR_CLASS_NOT_FOUND, error, e);
        }
        catch (java.lang.InstantiationException e)
        {
            String error = CLS_NM + ".createInstance()  [" + className + "] caught java.lang.InstantiationException=" + e;
            throw new ConfigurationException(GlobalErrIds.FT_MGR_INST_EXCEPTION, error, e);
        }
        catch (java.lang.IllegalAccessException e)
        {
            String error = CLS_NM + ".createInstance()  [" + className + "] caught java.lang.IllegalAccessException=" + e;
            throw new com.jts.fortress.ConfigurationException(com.jts.fortress.constants.GlobalErrIds.FT_MGR_ILLEGAL_ACCESS, error, e);
        }
        return target;
	}


    /**
     * Find a file on the classloader and return as InputStream.
     * @param name contains the name of the file resource.
     * @return handle to the InputStream
     * @throws ConfigurationException in the event resource is not found on classloader.
     */
    static public InputStream resourceAsStream(String name) throws ConfigurationException
    {
        InputStream is = null;
        is = ClassUtil.class.getClassLoader().getResourceAsStream(name);
        if (is == null)
        {
            throw new ConfigurationException(GlobalErrIds.FT_RESOURCE_NOT_FOUND, name);
        }
        return is;
    }
}
