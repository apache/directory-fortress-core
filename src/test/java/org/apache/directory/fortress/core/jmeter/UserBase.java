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
import org.apache.directory.fortress.core.model.User;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.directory.fortress.core.AdminMgr;
import org.apache.directory.fortress.core.impl.TestUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertNotNull;

/**
 * Description of the Class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public abstract class UserBase extends AbstractJavaSamplerClient
{
    protected AdminMgr adminMgr;
    protected ReviewMgr reviewMgr;
    protected static final Logger LOG = LoggerFactory.getLogger( UserBase.class );
    private static int count = 0;
    protected String hostname;
    protected String qualifier;
    private String filename;
    protected boolean verify;
    private PrintWriter printWriter;

    protected enum Op
    {
        ADD,
        DEL
    }

    protected boolean verify( String userId, Op op )
    {
        boolean found = false;
        try
        {
            assertNotNull( adminMgr );
            User user = new User( userId );
            User outUser = reviewMgr.readUser( user );
            if( op == Op.DEL )
            {
                warn( "Failed del check, threadId: " + getThreadId() + ", user: " + userId );
            }
            assertNotNull( outUser );
            found = true;
        }
        catch ( org.apache.directory.fortress.core.SecurityException se )
        {
            if( op == Op.ADD )
            {
                warn( "Failed add check, threadId: " + getThreadId() + ", error reading user: " + se );
                se.printStackTrace();
            }
        }
        return found;
    }

    private void init()
    {
        hostname = System.getProperty( "hostname" );
        System.setProperty( "fortress.host", hostname );
        qualifier = System.getProperty( "qualifier" );
        filename = "operations" + '-' + "thread" + getThreadId() + '-' + hostname + '-' + qualifier + ".txt";
        String szVerify = System.getProperty( "verify" );
        verify = szVerify.equalsIgnoreCase( "true" );
        open();
    }

    /**
     * Description of the Method
     *
     * @param samplerContext Description of the Parameter
     */
    public void setupTest( JavaSamplerContext samplerContext )
    {
        String message = "FT SETUP User TID: " + getThreadId() + ", hostname: " + hostname + ", qualifier: " + qualifier + ", verify:" + verify;
        log( message );
        System.out.println( message );
        init();
        try
        {
            adminMgr = AdminMgrFactory.createInstance( TestUtils.getContext() );
            reviewMgr = ReviewMgrFactory.createInstance( TestUtils.getContext() );
        }
        catch ( SecurityException se )
        {
            warn( "ThreadId: " + getThreadId() + ", error setting up test: " + se );
            se.printStackTrace();
        }
        open();
    }

    protected void log( String message )
    {
        LOG.info( message );
        System.out.println( message );
    }

    protected void warn( String message )
    {
        LOG.warn( message );
        System.out.println( message );
    }

    /**
     *
     * @return
     */
    synchronized int getKey( )
    {
        return ++count;
    }

    String getThreadId()
    {
        return "" + Thread.currentThread().getId();
    }

    /**
     * Description of the Method
     *
     * @param samplerContext Description of the Parameter
     */
    public void teardownTest( JavaSamplerContext samplerContext )
    {
        String message = "FT SETUP User TID: " + getThreadId();
        log( message );
        close();
    }

    private void open()
    {
        try
        {
            FileWriter fileWriter = new FileWriter(filename);
            printWriter = new PrintWriter(fileWriter);
        }
        catch ( IOException ie )
        {
            warn( ie.getMessage() );
        }
    }

    private void close()
    {
        printWriter.close();
    }

    protected void write( String message )
    {
        printWriter.printf("%s\n", message);
        printWriter.flush();
    }
}
