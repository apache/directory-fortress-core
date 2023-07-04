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
public class CheckAccess extends UserBase
{
    /**
     * This performs createSession and checkAccess API
     *
     * @param samplerContext Description of the Parameter
     * @return Description of the Return Value
     */
    public SampleResult runTest( JavaSamplerContext samplerContext )
    {
        String userId  = hostname + '-' + qualifier + '-' + getKey( Op.CHECK );
        SampleResult sampleResult = new SampleResult();
        try
        {
            sampleResult.sampleStart();
            assertNotNull( accessMgr );
            User user = new User();
            user.setUserId( userId );
            user.setPassword( "secret" );
            LOG.debug( "threadid: {}, userId: {}", getThreadId(), userId );
            Permission p = getPermission();
            assertNotNull("perm operand not setup", p);
            // This does both createSession and checkAccess:
            assertTrue( accessMgr.checkAccess( user, p, false ) );
            if( sleep > 0 )
            {
                try
                {
                    Thread.sleep( sleep );
                }
                catch (InterruptedException ie)
                {
                    Thread.currentThread().interrupt();
                }
            }
            sampleResult.setSampleCount( 1 );
            sampleResult.sampleEnd();
            sampleResult.setResponseMessage("test completed TID: " + getThreadId() + " UID: " + userId);
            sampleResult.setSuccessful(true);
        }
        catch ( org.apache.directory.fortress.core.SecurityException se )
        {
            warn( "ThreadId: " + getThreadId() + ", error running test: " + se );
            se.printStackTrace();
            sampleResult.setSuccessful( false );
        }

        return sampleResult;
    }

    private Permission getPermission()
    {
        Permission p = null;
        // The perm property format is: object.operation
        int indx = perm.indexOf('.');
        if (indx != -1)
        {
            p = new Permission( );
            p.setObjName( perm.substring(0, indx) );
            p.setOpName( perm.substring( indx + 1 ) + getRandomNumber( TOTAL_NUMBER_OF_PERMISSIONS ) );
        }
        return p;
    }
}
