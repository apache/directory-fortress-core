/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *  Description of the Class
 *
 * @author     smckinn
 * @created    August 24, 2009
 */
public class ReaderUtil
{
	final private static Logger log = Logger.getLogger(ReaderUtil.class.getName());


	/**
	 *  Description of the Method
	 */
	public static void clearScreen()
	{
		for (int lineCounter = 0; lineCounter < 60; lineCounter++)
		{
			System.out.println();
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public static String readLn()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = null;

		try
		{
			input = br.readLine();
		}
		catch (Exception e)
		{
			log.fatal("Exception caught in readLn = " + e);
		}
		return input;
	}


	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public static char readChar()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		char input = 0;

		try
		{
			input = (char) br.read();
		}
		catch (Exception e)
		{
			log.fatal("Exception caught in readLn = " + e);
		}
		return input;
	}


	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public static int readInt()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input;
		int retVal = 0;

		try
		{
			input = br.readLine();
			retVal = (new Integer(input)).intValue();
		}
		catch (Exception e)
		{
			log.fatal("Exception caught in readLn = " + e);
		}
		return retVal;
	}
}

