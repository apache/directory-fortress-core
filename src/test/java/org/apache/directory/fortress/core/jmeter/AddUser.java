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

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.directory.fortress.core.model.User;

import static org.junit.Assert.*;

/**
 * Add user entry tests.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AddUser extends UserBase
{
    /**
     * This test case performs add user entry. It optionally also can update, assign role and verify.
     *
     * @param samplerContext Description of the Parameter
     * @return Description of the Return Value
     */
    public SampleResult runTest( JavaSamplerContext samplerContext )
    {
        String userId = getUserId ( Op.ADD );
        SampleResult sampleResult = new SampleResult();
        try
        {
            sampleResult.sampleStart();
            assertNotNull( adminMgr );
            assertNotNull( "ou operand not setup", ou );
            User user = new User();
            user.setUserId( userId );
            user.setDescription( concat( "add one: ", user.getUserId() ) );
            user.setPassword( password );
            user.setOu( ou );
            LOG.debug( "threadid: {}, userId: {}", getThreadId(), userId );
            User outUser = adminMgr.addUser( user );
            assertNotNull( outUser );
            // This tests replication, ability to handle conflicts:
            if ( duplicate > 0 && count.get() > duplicate && ( count.get() % duplicate ) == 0 )
            {
                warn( concat( "DUPLICATE ADD: ", user.getUserId() ) );
                user.setDescription( concat( "add two: ", user.getUserId() ) );
                outUser = adminMgr.addUser( user );
            }
            if( update )
            {
                user.setDescription( "updated: " + user.getUserId() );
                outUser = adminMgr.updateUser( user );
            }
            if(StringUtils.isNotEmpty( role ) )
            {
                adminMgr.assignUser( new UserRole( user.getUserId(), role ));
            }
            assertNotNull( outUser );
            if ( verify )
            {
                assertTrue( concat( "failed test uid: ", userId ), verify( userId, Op.ADD ) );
            }
            sleep();
            wrapup( sampleResult, userId );
        }
        catch ( org.apache.directory.fortress.core.SecurityException se )
        {
            warn( se.getMessage() );
            sampleResult.setSuccessful( false );
        }

        return sampleResult;
    }
}
