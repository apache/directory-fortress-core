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

/*
 *  This class is used for testing purposes.
 */

package org.openldap.fortress;

/**
 *  Description of the Class
 *
 * @author     Shawn McKinney
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

