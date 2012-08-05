/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

/**
 *  Interface that is implemented by exception base class {@link StandardException} used to associate a Fortress error code to the exception instance.
 * See the {@link GlobalErrIds} javadoc for list of error ids used by Fortress.
 *
 * @author     Shawn McKinney
 * @created    August 23, 2009
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

