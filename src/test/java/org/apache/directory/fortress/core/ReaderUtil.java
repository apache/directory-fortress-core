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
package org.apache.directory.fortress.core;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Description of the Class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
class ReaderUtil
{
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( ReaderUtil.class.getName() );


    /**
     * Description of the Method
     */
    public static void clearScreen()
    {
        for ( int lineCounter = 0; lineCounter < 60; lineCounter++ )
        {
            System.out.println();
        }
    }


    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public static String readLn()
    {
        BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
        String input = null;

        try
        {
            input = br.readLine();
        }
        catch ( Exception e )
        {
            LOG.error( "Exception caught in readLn = " + e );
        }
        return input;
    }


    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public static char readChar()
    {
        BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
        char input = 0;

        try
        {
            input = ( char ) br.read();
        }
        catch ( Exception e )
        {
            LOG.error( "Exception caught in readLn = " + e );
        }
        return input;
    }


    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public static int readInt()
    {
        BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
        String input;
        int retVal = 0;

        try
        {
            input = br.readLine();
            retVal = new Integer( input );
        }
        catch ( Exception e )
        {
            LOG.error( "Exception caught in readLn = " + e );
        }
        return retVal;
    }
}

