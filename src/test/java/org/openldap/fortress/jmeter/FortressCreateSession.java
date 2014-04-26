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

package org.openldap.fortress.jmeter;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.LoggerFactory;
import org.openldap.fortress.AccessMgr;
import org.openldap.fortress.AccessMgrFactory;
import org.openldap.fortress.rbac.Session;
import org.openldap.fortress.rbac.TestUtils;
import org.openldap.fortress.rbac.User;

import static org.junit.Assert.*;

/**
 * Description of the Class
 *
 * @author Shawn McKinney
 */
public class FortressCreateSession extends AbstractJavaSamplerClient
{
    private AccessMgr accessMgr;
    private boolean echoRequest = false;
    private boolean returnResult = false;
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( FortressCreateSession.class );
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
            String message = "FT CreateSession TID: " + getThreadId() + " #:" + ctr++;
            LOG.info( message );
            System.out.println( message );
            assertNotNull( accessMgr );
            Session session;
            User user = new User();
            // positive test case:
            user.setUserId( "rbacuser1" );
            user.setPassword( "secret".toCharArray() );
            session = accessMgr.createSession( user, false );
            assertNotNull( session );
            assertTrue( session.isAuthenticated() );
            sampleResult.sampleEnd();
            sampleResult.setBytes(1);
            sampleResult.setResponseMessage("test completed");
            sampleResult.setSuccessful(true);
        }
        catch ( org.openldap.fortress.SecurityException se )
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
        String message = "FT SETUP CreateSession TID: " + getThreadId();
        LOG.info( message );
        System.out.println( message );
        try
        {
            accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
        }
        catch ( org.openldap.fortress.SecurityException se )
        {
            se.printStackTrace();
            System.out.println( "ThreadId:" + getThreadId() + "FT SETUP Error: " + se );
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
        String message = "FT SETUP CreateSession TID: " + getThreadId();
        LOG.info( message );
        System.out.println( message );
    }
}