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

package org.openldap.fortress;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Description of the Class
 *
 * @author Shawn McKinney
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

