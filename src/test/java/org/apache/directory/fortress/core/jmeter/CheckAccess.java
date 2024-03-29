/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core.jmeter;

import org.apache.directory.fortress.core.model.Permission;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.directory.fortress.core.model.User;

import static org.junit.Assert.*;

/**
 * Authentication and authorization tests.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CheckAccess extends LoadBase
{
    /**
     * This performs createSession and checkAccess in one API call
     *
     * @param samplerContext Description of the Parameter
     * @return Description of the Return Value
     */
    public SampleResult runTest( JavaSamplerContext samplerContext )
    {
        boolean result = false;
        String userId = getUserId ( Op.CHECK );
        SampleResult sampleResult = new SampleResult();
        try
        {
            sampleResult.sampleStart();
            assertNotNull( accessMgr );
            User user = new User();
            user.setUserId( userId );
            user.setPassword( password );
            LOG.debug( "threadid: {}, userId: {}", getThreadId(), userId );
            Permission p = getPermission();
            assertTrue( concat("failed test uid: ", userId, ", perm obj: ", p.getObjName(), ", op: ", p.getOpName()),
                    accessMgr.checkAccess( user, p, false ) );
            result = true;
            sleep();
        }
        catch ( org.apache.directory.fortress.core.SecurityException se )
        {
            warn( se.getMessage() );
        }
        wrapup( sampleResult, userId, result );
        return sampleResult;
    }
}
