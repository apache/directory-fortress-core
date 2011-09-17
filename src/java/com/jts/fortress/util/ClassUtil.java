/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.util;

import com.jts.fortress.ConfigurationException;
import com.jts.fortress.constants.GlobalErrIds;


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
 * @author smckinn
 * @created February 13, 2010
 */
public class ClassUtil
{
    private static final String OCLS_NM = ClassUtil.class.getName();

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
                String error = OCLS_NM + ".createInstance() null or empty classname";
                throw new com.jts.fortress.ConfigurationException(GlobalErrIds.FT_MGR_CLASS_NAME_NULL, error);
            }
            target = Class.forName(className).newInstance();
        }
        catch (java.lang.ClassNotFoundException e)
        {
            String error = OCLS_NM + ".createInstance() className <" + className + "> caught java.lang.ClassNotFoundException=" + e;
            throw new com.jts.fortress.ConfigurationException(GlobalErrIds.FT_MGR_CLASS_NOT_FOUND, error, e);
        }
        catch (java.lang.InstantiationException e)
        {
            String error = OCLS_NM + ".createInstance()  <" + className + "> caught java.lang.InstantiationException=" + e;
            throw new ConfigurationException(GlobalErrIds.FT_MGR_INST_EXCEPTION, error, e);
        }
        catch (java.lang.IllegalAccessException e)
        {
            String error = OCLS_NM + ".createInstance()  <" + className + "> caught java.lang.IllegalAccessException=" + e;
            throw new com.jts.fortress.ConfigurationException(com.jts.fortress.constants.GlobalErrIds.FT_MGR_ILLEGAL_ACCESS, error, e);
        }
        return target;
	}
}
