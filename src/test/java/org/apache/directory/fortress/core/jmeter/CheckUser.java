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

import org.apache.commons.lang3.StringUtils;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Session;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.directory.fortress.core.model.User;

import static org.junit.Assert.*;

/**
 * Authentication and authorization tests.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CheckUser extends LoadBase
{
    /**
     * This test can perform authentication and multiple authorization checks.
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
            // This method performs both an ldap bind and a search:
            Session session = accessMgr.createSession( user, false );
            assertNotNull( "failed createSession", session );
            if(StringUtils.isNotEmpty( perm ) )
            {
                for( int i = 1; i <= TOTAL_NUMBER_OF_PERMISSIONS; i++ )
                {
                    Permission p = getPermission( i );
                    assertTrue(
                            concat( "failed test uid: ", userId, ", perm obj: ", p.getObjName(), ", op: ", p.getOpName() ),
                            accessMgr.checkAccess( session, p ) );
                }
            }
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
