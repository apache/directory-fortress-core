/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.configuration.Config;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.ClassUtil;

/**
 * Creates an instance of the PswdPolicyMgr object.
 * <p/>
 * The default implementation class is specified as {@link com.jts.fortress.constants.GlobalIds#PSWD_POLICY_DEFAULT_CLASS} but can be overridden by
 * adding the {@link com.jts.fortress.constants.GlobalIds#PSWD_POLICY_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author smckinn
 * @created October 17, 2009
 */
public class PswdPolicyMgrFactory
{
    private static String policyClassName = Config.getProperty(GlobalIds.PSWD_POLICY_IMPLEMENTATION);

    /**
     * Create and return a reference to {@link com.jts.fortress.PswdPolicyMgr} object.
     *
     * @return instance of {@link com.jts.fortress.PswdPolicyMgr}.
     * @throws com.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static com.jts.fortress.PswdPolicyMgr createInstance()
        throws SecurityException
    {
        com.jts.fortress.PswdPolicyMgr policyMgr;
        if (policyClassName == null || policyClassName.compareTo("") == 0)
        {
            policyClassName = GlobalIds.PSWD_POLICY_DEFAULT_CLASS;
        }

        policyMgr = (com.jts.fortress.PswdPolicyMgr) ClassUtil.createInstance(policyClassName);
        return policyMgr;
    }
}

