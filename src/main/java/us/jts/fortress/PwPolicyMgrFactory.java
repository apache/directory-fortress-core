/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress;

import us.jts.fortress.cfg.Config;
import us.jts.fortress.rbac.ClassUtil;
import us.jts.fortress.rbac.PwPolicyMgrImpl;
import us.jts.fortress.rbac.Session;
import us.jts.fortress.rest.PwPolicyMgrRestImpl;
import us.jts.fortress.util.attr.VUtil;

/**
 * Creates an instance of the PwPolicyMgr object.
 * <p/>
 * The default implementation class is specified as {@link PwPolicyMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#PSWD_POLICY_IMPLEMENTATION} config property.
 * <p/>
 *
 * @author Shawn McKinney
 */
public class PwPolicyMgrFactory
{
    private static String policyClassName = Config.getProperty(GlobalIds.PSWD_POLICY_IMPLEMENTATION);
    private static final String CLS_NM = PwPolicyMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link PwPolicyMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link PwPolicyMgr}.
     * @throws us.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static PwPolicyMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        if (!VUtil.isNotNullOrEmpty(policyClassName))
        {
            if(GlobalIds.IS_REST)
            {
                policyClassName = PwPolicyMgrRestImpl.class.getName();
            }
            else
            {
                policyClassName = PwPolicyMgrImpl.class.getName();
            }
        }

        PwPolicyMgr policyMgr = (PwPolicyMgr) ClassUtil.createInstance(policyClassName);
        policyMgr.setContextId(contextId);
        return policyMgr;
    }

    /**
     * Create and return a reference to {@link us.jts.fortress.PwPolicyMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link us.jts.fortress.PwPolicyMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static PwPolicyMgr createInstance(String contextId, Session adminSess)
        throws SecurityException
    {
        PwPolicyMgr policyMgr = createInstance(contextId);
        policyMgr.setAdmin(adminSess);
        return policyMgr;
    }
}

