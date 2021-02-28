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
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.util.Config;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.directory.fortress.core.AdminMgr;
import org.apache.directory.fortress.core.impl.TestUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertNotNull;

/**
 * Description of the Class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public abstract class UserBase extends AbstractJavaSamplerClient
{
    protected AccessMgr accessMgr;
    protected AdminMgr adminMgr;
    protected ReviewMgr reviewMgr;
    protected static final Logger LOG = LoggerFactory.getLogger( UserBase.class );
    private static AtomicInteger count = new AtomicInteger(0);
    protected String hostname;
    protected String qualifier;
    private String filename;
    protected boolean verify = false;
    protected boolean output = false;
    protected boolean update = false;
    protected String role = null;
    protected String perm = null;
    protected String ou = null;
    protected int sleep = 0;
    protected int size = 0;
    private PrintWriter printWriter;

    protected enum Op
    {
        ADD,
        DEL,
        CHECK
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
            if( op == Op.ADD || op == Op.CHECK )
            {
                warn( "Failed add check, threadId: " + getThreadId() + ", error reading user: " + se );
                se.printStackTrace();
            }
        }
        return found;
    }

    /**
     * Description of the Method
     *
     * @param samplerContext Description of the Parameter
     */
    public void setupTest( JavaSamplerContext samplerContext )
    {
        init( samplerContext );
        String message = "FT SETUP User TID: " + getThreadId() + ", hostname: " + hostname + ", qualifier: " + qualifier + ", verify: " + verify + ", sleep: " + sleep;
        info( message );
        try
        {
            accessMgr = AccessMgrFactory.createInstance( TestUtils.getContext() );
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

    private void init( JavaSamplerContext samplerContext )
    {
        // Can override hostname via system property:
        hostname = System.getProperty( "hostname" );
        if (! StringUtils.isEmpty( hostname ))
        {
            System.setProperty( "fortress.host", hostname );
        }
        else
        {
            hostname = Config.getInstance().getProperty( GlobalIds.LDAP_HOST );
        }
        qualifier = System.getProperty( "qualifier" );
        if (StringUtils.isEmpty( qualifier ))
        {
            qualifier = samplerContext.getParameter( "qualifier" );
        }
        ou = System.getProperty( "ou" );
        if (StringUtils.isEmpty( ou ))
        {
            ou = samplerContext.getParameter( "ou" );
        }
        role = System.getProperty( "role" );
        if (StringUtils.isEmpty( role ))
        {
            role = samplerContext.getParameter( "role" );
        }
        perm = System.getProperty( "perm" );
        if (StringUtils.isEmpty( perm ))
        {
            perm = samplerContext.getParameter( "perm" );
        }
        String szVerify = System.getProperty( "verify" );
        if (StringUtils.isEmpty( szVerify ))
        {
            verify = samplerContext.getParameter( "verify" ).equalsIgnoreCase( "true" );
        }
        else
        {
            verify = szVerify.equalsIgnoreCase( "true" );
        }
        String szLog = System.getProperty( "log" );
        if (StringUtils.isEmpty( szLog ))
        {
            output = samplerContext.getParameter( "output" ).equalsIgnoreCase( "true" );
        }
        else
        {
            output = szLog.equalsIgnoreCase( "true" );
        }
        String szUpdate = System.getProperty( "update" );
        if (StringUtils.isEmpty( szUpdate ))
        {
            update = samplerContext.getParameter( "update" ).equalsIgnoreCase( "true" );
        }
        else
        {
            update = szVerify.equalsIgnoreCase( "true" );
        }
        String szSleep = System.getProperty( "sleep" );
        if (StringUtils.isEmpty( szSleep ))
        {
            szSleep = samplerContext.getParameter( "sleep" );
        }
        if (!StringUtils.isEmpty( szSleep ))
        {
            sleep = Integer.valueOf(szSleep);
        }
        String szSize = System.getProperty( "size" );
        if (StringUtils.isEmpty( szSize ))
        {
            szSize = samplerContext.getParameter( "size" );
        }
        if (!StringUtils.isEmpty( szSize ))
        {
            size = Integer.valueOf(szSize);
        }
        filename = "operations" + '-' + "thread" + getThreadId() + '-' + hostname + '-' + qualifier + ".txt";
        open();
    }

    protected void info(String message )
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
    protected int getKey( )
    {
        //return ++count;
        return count.incrementAndGet();
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
        String message = "FT TEARDOWN User TID: " + getThreadId();
        info( message );
        close();
        System.exit(0);
    }

    private void open()
    {
        if( output )
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
    }

    private void close()
    {
        if( output )
        {
            printWriter.close();
        }
    }

    protected void write( String message )
    {
        if( output )
        {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            printWriter.printf("%s : %s\n", now, message);
            printWriter.flush();
        }
    }

    protected int getRandomNumber()
    {
        int number = (int) ((Math.random() * (size - 1)) + 1);
        return number;
    }
}
