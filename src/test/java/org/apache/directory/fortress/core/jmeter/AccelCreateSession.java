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
package org.apache.directory.fortress.core.jmeter;

import org.apache.directory.fortress.core.*;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.LoggerFactory;
import org.apache.directory.fortress.core.AccelMgr;
import org.apache.directory.fortress.core.rbac.Session;
import org.apache.directory.fortress.core.rbac.TestUtils;
import org.apache.directory.fortress.core.rbac.User;

import static org.junit.Assert.*;

/**
 * Description of the Class
 *
 * @author Shawn McKinney
 */
public class AccelCreateSession extends AbstractJavaSamplerClient
{

    private boolean echoRequest = false;
    private boolean returnResult = false;
    private AccelMgr accelMgr;
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( AccelCreateSession.class );
    private static int count = 0;
    private int ctr = 0;

    /**
     * Description of the Method
     *
     * @param samplerContext Description of the Parameter
     * @return Description of the Return Value
     */
    public SampleResult runTest( JavaSamplerContext samplerContext )
    {
        SampleResult sampleResult = new SampleResult();
        try
        {
            sampleResult.sampleStart();
            String fiKey = getKey( Thread.currentThread().getId() );
            assertNotNull( accelMgr );
            String message = "AC CreateSession TID: " + getThreadId() + " #:" + ctr++;
            LOG.info( message );
            System.out.println( message );
            Session session;
            User user = new User();
            // positive test case:
            user.setUserId( "rbacuser1" );
            user.setPassword( "secret".toCharArray() );
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
            assertTrue( session.isAuthenticated() );
            sampleResult.sampleEnd();
            sampleResult.setBytes(1);
            sampleResult.setResponseMessage("test completed");
            sampleResult.setSuccessful(true);
            //accelMgr.deleteSession( session );
        }
        catch ( org.apache.directory.fortress.core.SecurityException se )
        {
            se.printStackTrace();
            System.out.println( "ThreadId:" + getThreadId() + "Error running test: " + se );
            se.printStackTrace();
            sampleResult.setSuccessful( false );
        }

        return sampleResult;
    }

    /**
     * @param threadId
     * @return
     */
    synchronized private String getKey( long threadId )
    {
        return threadId + "-" + count++;
    }


    private String getThreadId()
    {
        return "" + Thread.currentThread().getId();
    }

    /**
     * Description of the Method
     *
     * @param samplerContext Description of the Parameter
     */
    public void setupTest( JavaSamplerContext samplerContext )
    {
        getKey( Thread.currentThread().getId() );
        String message = "AC SETUP CreateSession TID: " + getThreadId();
        LOG.info( message );
        System.out.println( message );
        try
        {
            accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
        }
        catch ( SecurityException se )
        {
            se.printStackTrace();
            System.out.println( "AC SETUP ThreadId:" + getThreadId() + "Error setup test: " + se );
            se.printStackTrace();
        }
    }

    /**
     * Description of the Method
     *
     * @param samplerContext Description of the Parameter
     */
    public void teardownTest( JavaSamplerContext samplerContext )
    {
        String message = "AC TEARDOWN CreateSession TID: " + getThreadId();
        LOG.info( message );
        System.out.println( message );
    }
}

