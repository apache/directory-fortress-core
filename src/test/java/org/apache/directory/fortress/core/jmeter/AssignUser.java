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

import org.apache.directory.fortress.core.model.UserRole;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import static org.junit.Assert.*;

/**
 * Assign user entry tests.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AssignUser extends LoadBase
{
    /**
     * This test case performs assign user entry.
     *
     * @param samplerContext Description of the Parameter
     * @return Description of the Return Value
     */
    public SampleResult runTest( JavaSamplerContext samplerContext )
    {
        boolean result = false;
        String userId = getUserId ( Op.ADD );
        String roleNm = userId;
        SampleResult sampleResult = new SampleResult();
        try
        {
            sampleResult.sampleStart();
            assertNotNull( adminMgr );
            UserRole userRole = new UserRole(userId, roleNm);
            LOG.debug( "threadid: {}, userId: {}, roleNm: {}", getThreadId(), userId, roleNm );
            adminMgr.assignUser( userRole );
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
