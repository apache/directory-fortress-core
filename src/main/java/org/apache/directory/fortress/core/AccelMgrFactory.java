/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core;

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.impl.AccelMgrImpl;
import org.apache.directory.fortress.core.util.ClassUtil;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * Creates an instance of the AccelMgr object.
 * <p/>
 * The default implementation class is specified as {@link AccelMgrImpl} but can be overridden by
 * adding the {@link GlobalIds#ACCEL_IMPLEMENTATION} config property.
 * <p/>

 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class AccelMgrFactory
{
    private static String accelClassName = Config.getProperty(GlobalIds.ACCEL_IMPLEMENTATION);
    private static final String CLS_NM = AccelMgrFactory.class.getName();

    /**
     * Create and return a reference to {@link AccelMgr} object using HOME context.
     *
     * @return instance of {@link AccelMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static AccelMgr createInstance()
        throws SecurityException
    {
        return createInstance( GlobalIds.HOME );
    }

    /**
     * Create and return a reference to {@link AccelMgr} object.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return instance of {@link AccelMgr}.
     * @throws SecurityException in the event of failure during instantiation.
     */
    public static AccelMgr createInstance(String contextId)
        throws SecurityException
    {
        VUtil.assertNotNull(contextId, GlobalErrIds.CONTEXT_NULL, CLS_NM + ".createInstance");
        AccelMgr accelMgr;
        if (!StringUtils.isNotEmpty( accelClassName ))
        {
            accelMgr = new AccelMgrImpl();
        }
        else
        {
            accelMgr = (AccelMgr) ClassUtil.createInstance(accelClassName);
        }

        accelMgr.setContextId(contextId);
        return accelMgr;
    }
}