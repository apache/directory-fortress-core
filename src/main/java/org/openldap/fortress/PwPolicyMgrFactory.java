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
import org.openldap.fortress.rbac.ClassUtil;
import org.openldap.fortress.rbac.PwPolicyMgrImpl;
import org.openldap.fortress.rbac.Session;
import org.openldap.fortress.rest.PwPolicyMgrRestImpl;
import org.openldap.fortress.util.attr.VUtil;

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
     * Create and return a reference to {@link PwPolicyMgr} object using HOME context.
     *
     * @return instance of {@link PwPolicyMgr}.
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
     */
    public static PwPolicyMgr createInstance()
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link PwPolicyMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link PwPolicyMgr}.
     * @throws org.openldap.fortress.SecurityException in the event of failure during instantiation.
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
     * Create and return a reference to {@link org.openldap.fortress.PwPolicyMgr} object using HOME context.
     *
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link org.openldap.fortress.PwPolicyMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static PwPolicyMgr createInstance(Session adminSess)
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME, adminSess );
    }

    /**
     * Create and return a reference to {@link org.openldap.fortress.PwPolicyMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param adminSess contains a valid Fortress A/RBAC Session object.
     * @return instance of {@link org.openldap.fortress.PwPolicyMgr}.
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

