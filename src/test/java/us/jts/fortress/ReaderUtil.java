/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress;

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

