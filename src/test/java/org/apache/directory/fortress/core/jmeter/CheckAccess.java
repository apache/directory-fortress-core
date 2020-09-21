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

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.*;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.LoggerFactory;
import org.apache.directory.fortress.core.AccelMgr;
import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.model.User;

import static org.junit.Assert.*;

/**
 * Description of the Class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CheckAccess extends AbstractJavaSamplerClient
{
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( CheckAccess.class );
    private static int count = 0;
    private Session session;
    private AccelMgr accelMgr;
    private AccessMgr accessMgr;
/*
    private int key = getKey();
    private int ctr = 0;
    private String userId = "loadtestuser" + key;
*/
    private int key = 0;
    private int ctr = 0;
    private String userId = "";
    private boolean isFortress = false;

    /**224
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
            int opCount = ++ctr % 10;
            if( opCount == 0 )
                opCount = 10;

            int objCount = (ctr / 10) + 1;
            if(objCount > 10)
            {
                objCount = objCount % 10;
            }
            if(objCount == 0)
            {
                objCount = 1;
            }

            //int objCount = ((ctr / 10) + 1) % 10;
            String opName = "oper" + opCount;
            String objName = "loadtestobject" + objCount;
            sampleResult.sampleStart();
            String message;
            if(isFortress)
            {
                message = "FT ";
            }
            else
            {
                message = "AC ";
            }

            message += "CheckAccess isFortress: " + isFortress + ", userId: " + userId + ", objName:" + objName + ", opName: " + opName;
/*
            LOG.info( message );
            System.out.println( message );
*/
            assertNotNull( session );
            assertTrue( session.isAuthenticated() );
            Permission perm = new Permission();
            perm.setObjName( objName );
            perm.setOpName( opName );
            boolean result;
            if(isFortress)
            {
                assertNotNull( accessMgr );
                result = accessMgr.checkAccess( session, perm );
            }
            else
            {
                assertNotNull( accelMgr );
                result = accelMgr.checkAccess( session, perm );
            }

            // positive test case:
            assertTrue( message, result );
            sampleResult.sampleEnd();
            sampleResult.setBytes(1);
            sampleResult.setResponseMessage("test checkAccess completed");
            sampleResult.setSuccessful(true);
        }
        catch ( org.apache.directory.fortress.core.SecurityException se )
        {
            String error = "ThreadId:" + getThreadId() + "Error running test: " + se;
            LOG.error( error );
            System.out.println( error );
            se.printStackTrace();
            fail( error );
            sampleResult.setSuccessful( false );
        }

        return sampleResult;
    }

    /**
     *
     * @return
     */
    synchronized private int getKey( )
    {
        return ++count;
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
        ctr = 0;
        int numberOfUsers = 100;
        String szNumber = samplerContext.getParameter( "number" );
        if(StringUtils.isNotEmpty( szNumber ))
        {
            numberOfUsers = new Integer( szNumber );
        }

        if(StringUtils.isEmpty( userId ))
        {
            // Load userids are format:  loadtestuserN - where N is a number between 0 and 99.
            // i.e. loadtestuser0,  loadtestuser1,  ... loadtestuser99
            // N is threadid mod 100.
            key = getKey();
            userId = "loadtestuser" + key % numberOfUsers;
        }
        try
        {
            String val = samplerContext.getParameter( "type" );
            System.out.println("PARAMETER VALUE = " + val);
            if(session == null)
            {
                String message;
                User user = new User(userId);
                // positive test case:
                user.setPassword( "secret" );
                if( StringUtils.isNotEmpty( val ) && val.equals( "1" ))
                {
                    message = "AC SETUP CreateSession, User: " + user.getUserId() + ", key: " + key + ", TID: " + getThreadId();
                    isFortress = false;

                    LOG.info( message );
                    System.out.println( message );

                    message = "ThreadId:" + getThreadId() + ", createSession user: " + user.getUserId();
                    LOG.info( message );
                    System.out.println( message );

                    accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
                    session = accelMgr.createSession( user, false );
                }
                else
                {
                    message = "FT SETUP CreateSession, User: " + user.getUserId() + ", key: " + key + ", TID: " + getThreadId();
                    isFortress = true;

                    LOG.info( message );
                    System.out.println( message );
                    message = "ThreadId:" + getThreadId() + ", createSession user: " + user.getUserId();
                    LOG.info( message );
                    System.out.println( message );

                    accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
                    session = accessMgr.createSession( user, false );
                }
/*
                if( StringUtils.isNotEmpty( val ) && val.equals( "1" ))
                {
                    message = "FT SETUP CreateSession, User: " + user.getUserId() + ", key: " + key + ", TID: " + getThreadId();
                    isFortress = true;
                    accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
                    session = accessMgr.createSession( user, false );
                }
                else
                {
                    message = "AC SETUP CreateSession, User: " + user.getUserId() + ", key: " + key + ", TID: " + getThreadId();
                    isFortress = false;
                    accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
                    session = accelMgr.createSession( user, false );
                }
*/
/*
                LOG.info( message );
                System.out.println( message );
*/
            }

            assertNotNull( session );
            assertTrue( session.isAuthenticated() );
        }
        catch ( SecurityException se )
        {
            String error = "ThreadId:" + getThreadId() + " Error starting test: " + se;
            System.out.println( error );
            LOG.error( error );
            se.printStackTrace();
            fail(error);
        }
    }

    /**
     * Description of the Method
     *
     * @param samplerContext Description of the Parameter
     */
    public void teardownTest( JavaSamplerContext samplerContext )
    {
        try
        {
            String message = "TEARDOWN UserId:" + userId + ", key: " + key + ", TID: " + getThreadId();
            LOG.info( message );
            System.out.println( message );
            if(!isFortress)
            {
                assertNotNull( session );
                accelMgr.deleteSession( session );
                //Thread.sleep( 100 );
            }
            session = null;
        }
        catch ( SecurityException se )
        {
            String error = "teardownTest ThreadId:" + getThreadId() + " Error stopping test: " + se;
            LOG.error( error );
            se.printStackTrace();
        }
        System.exit(0);
    }
}
