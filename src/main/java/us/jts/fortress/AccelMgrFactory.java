/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress;

import us.jts.fortress.cfg.Config;
import us.jts.fortress.rbac.AccelMgrImpl;
import us.jts.fortress.rbac.ClassUtil;
import us.jts.fortress.util.attr.VUtil;

/**
 * Creates an instance of the AccelMgr object.
 * <p/>
 * The default implementation class is specified as {@link AccelMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#ACCEL_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author Shawn McKinney
 */
public class AccelMgrFactory
{
    private static String accelClassName = Config.getProperty(GlobalIds.ACCEL_IMPLEMENTATION);
    private static final String CLS_NM = AccelMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link us.jts.fortress.AccelMgr} object using HOME context.
     *
     * @return instance of {@link us.jts.fortress.AccelMgr}.
     * @throws us.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static AccelMgr createInstance()
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link us.jts.fortress.AccelMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link us.jts.fortress.AccelMgr}.
     * @throws us.jts.fortress.SecurityException in the event of failure during instantiation.
     */
    public static AccelMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        if (!VUtil.isNotNullOrEmpty(accelClassName))
        {
                accelClassName = AccelMgrImpl.class.getName();
        }

        AccelMgr accelMgr = (AccelMgr) ClassUtil.createInstance(accelClassName);
        accelMgr.setContextId(contextId);
        return accelMgr;
    }
}