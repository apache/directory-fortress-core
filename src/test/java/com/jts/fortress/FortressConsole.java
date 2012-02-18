/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

/*
 *  This class is used for testing purposes.
 */

package com.jts.fortress;

/**
 *  Description of the Class
 *
 * @author     smckinn
 * @created    August 24, 2009
 */
public class FortressConsole
{

	/**
	 * put your documentation comment here
	 *
	 * @param  args
	 */
	public static void main(String[] args)
	{
		ProcessMenuCommand rc = new ProcessMenuCommand();

		rc.processRbacControl();
	}
}

