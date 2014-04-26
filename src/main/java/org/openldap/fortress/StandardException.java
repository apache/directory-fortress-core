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

/**
 *  Interface that is implemented by exception base class {@link StandardException} used to associate a Fortress error code to the exception instance.
 * See the {@link GlobalErrIds} javadoc for list of error ids used by Fortress.
 *
 * @author Shawn McKinney
 */
public interface StandardException
{
    /**
     * Return the Fortress error code that is optional to exceptions thrown within this security system.  See {@link GlobalErrIds} for list of all error codes.
     *
     * @return integer containing the source error code.  Valid values for Fortress error codes fall between 0 and 100_000.
     */
	public int getErrorId();
}

