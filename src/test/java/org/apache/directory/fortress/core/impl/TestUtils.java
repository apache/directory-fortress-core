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
package org.apache.directory.fortress.core.impl;


import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.model.Constraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.util.LogUtil;


/**
 * Description of the Class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class TestUtils extends TestCase
{
    private static final String CLS_NM = TestUtils.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    /**
     * Fortress stores complex attribute types within a single attribute in ldap.  Usually a delimiter of ',' is used for string tokenization.
     * format: {@code name:value}
     */
    public static final String DELIMITER_TEST_DATA = ",";

    private static String contextId = GlobalIds.HOME;


    public static String getContext()
    {
        // This property can be overriden with system property:
        String tenant = System.getProperty( GlobalIds.TENANT );
        
        if ( StringUtils.isNotEmpty( tenant ) && !tenant.equals( "${tenant}" ) )
        {
            contextId = tenant;
        }
        
        return contextId;
    }


    public static byte[] readJpegFile( String fileName )
    {
        URL fUrl = TestUtils.class.getClassLoader().getResource( fileName );
        byte[] image = null;
        try
        {
            if ( fUrl != null )
            {
                image = FileUtils.readFileToByteArray( new File( fUrl.toURI() ) );
            }
        }
        catch ( URISyntaxException se )
        {
            String error = "readJpegFile caught URISyntaxException=" + se;
            LOG.error( error );
        }
        catch ( IOException ioe )
        {
            String error = "readJpegFile caught IOException=" + ioe;
            LOG.error( error );
        }
        return image;
    }


    /**
     * 
     * @param len
     */
    public static void sleep( String len )
    {
        try
        {
            Integer iSleep = ( Integer.parseInt( len ) * 1000 );
            //LOG.info(TestUtils.class.getName() + ".sleep for len=" + iSleep);
            LogUtil.logIt( TestUtils.class.getName() + ".sleep for len=" + iSleep );
            Thread.currentThread().sleep( iSleep );
        }
        catch ( InterruptedException ie )
        {
            LOG.warn( TestUtils.class.getName() + ".sleep caught InterruptedException=" + ie.getMessage(), ie );
        }
    }


    /**
     *
     * @param len
     */
    public static void sleep( int len )
    {
        try
        {
            int iSleep = len * 1000;
            LogUtil.logIt( TestUtils.class.getName() + ".sleep for len=" + iSleep );
            Thread.currentThread().sleep( iSleep );
        }
        catch ( InterruptedException ie )
        {
            LOG.warn( TestUtils.class.getName() + ".sleep caught InterruptedException=" + ie.getMessage(), ie );
        }
    }


    /**
     *
     * @param len
     */
    public static void sleep( long len )
    {
        try
        {
            long iSleep = len * 1000;
            LogUtil.logIt( TestUtils.class.getName() + ".sleep for len=" + iSleep );
            Thread.currentThread().sleep( iSleep );
        }
        catch ( InterruptedException ie )
        {
            LOG.warn( TestUtils.class.getName() + ".sleep caught InterruptedException=" + ie.getMessage(), ie );
        }
    }


    /**
     *
     * @param inClass
     * @param fieldLabel
     * @return
     */
    public static String getDataLabel( Class inClass, String fieldLabel )
    {
        String labelValue = null;
        try
        {
            Field field = inClass.getField( fieldLabel );
            Annotation annotation = field.getAnnotation( MyAnnotation.class );
            if ( annotation != null )
            {
                MyAnnotation myAnnotation = ( MyAnnotation ) annotation;
                labelValue = myAnnotation.value();
            }
        }
        catch ( NoSuchFieldException e )
        {
            System.out.println( "annotation excep=" + e );
        }

        return labelValue;
    }


    /**
     *
     * @param inClass
     * @param fieldLabel
     * @return
     */
    public static String getTestDataLabels( Class inClass, String fieldLabel )
    {
        String fieldName = null;
        try
        {
            //Field field = inClass.getField(fieldLabel);
            Field field = inClass.getField( "POLICIES_TP1" );

            Annotation annotation = field.getAnnotation( MyAnnotation.class );
            //Annotation[] annotations = field.getDeclaredAnnotations();
            if ( annotation != null )
            {
                MyAnnotation myAnnotation = ( MyAnnotation ) annotation;

                //System.out.println("name: " + "dd");
                System.out.println( "*************** name: " + myAnnotation.name() );
                System.out.println( "*************** value: " + myAnnotation.value() );
                fieldName = myAnnotation.name();
            }
        }
        catch ( NoSuchFieldException e )
        {
            System.out.println( "annotation excep=" + e );
        }

        return fieldName;
    }


    /**
     * 
     * @param srchVal
     * @return
     */
    public static String getSrchValue( String srchVal )
    {
        srchVal = srchVal.substring( 0, srchVal.length() - 2 );
        return srchVal;
    }


    public static String getSrchValue( String srchVal, int length )
    {
        srchVal = srchVal.substring( 0, length );
        return srchVal;
    }


    /**
     * @param msg
     * @param c1
     * @param c2
     */
    public static void assertTemporal( String msg, Constraint c1,
        Constraint c2 )
    {
        if(c1.getBeginDate() != null || c2.getBeginDate() != null)
            assertEquals( msg, c1.getBeginDate(), c2.getBeginDate() );
        if(c1.getEndDate() != null || c2.getEndDate() != null)
            assertEquals( msg, c1.getEndDate(), c2.getEndDate() );
        if(c1.getBeginLockDate() != null || c2.getBeginLockDate() != null)
            assertEquals( msg, c1.getBeginLockDate(), c2.getBeginLockDate() );
        if(c1.getEndLockDate() != null || c2.getEndLockDate() != null)
            assertEquals( msg, c1.getEndLockDate(), c2.getEndLockDate() );
        assertEquals( msg, c1.getBeginTime(), c2.getBeginTime() );
        assertEquals( msg, c1.getEndTime(), c2.getEndTime() );
        assertEquals( msg, c1.getDayMask(), c2.getDayMask() );
        assertEquals( msg, c1.getTimeout(), c2.getTimeout() );
    }


    /**
     *
     * @param szInput
     * @return
     */
    public static Set<String> getSets( String szInput )
    {
        Set<String> vSets = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        try
        {
            if ( StringUtils.isNotEmpty( szInput ) )
            {
                StringTokenizer charSetTkn = new StringTokenizer( szInput, TestUtils.DELIMITER_TEST_DATA );
                if ( charSetTkn.countTokens() > 0 )
                {
                    while ( charSetTkn.hasMoreTokens() )
                    {
                        String value = charSetTkn.nextToken();
                        vSets.add( value );
                    }
                }
            }
        }
        catch ( java.lang.ArrayIndexOutOfBoundsException ae )
        {
            // ignore
        }
        return vSets;
    }
}