/*
 * Copyright © Joshua Tree Software, LLC, 2009-2012 All Rights Reserved.
 */

package com.jts.fortress;


/**
 * This exception extends {@link SecurityException} and is thrown when administrative permission check fails.
 * See the {@link com.jts.fortress.constants.GlobalErrIds} javadoc for list of error ids.
 *
 * @author smckinn
 * @created April 23, 2011
 */
public class AuthorizationException extends SecurityException
{
    /**
     * Create an exception with an error code that maps to {@link com.jts.fortress.constants.GlobalErrIds} and message text.
     *
     * @param  errorId see {@link com.jts.fortress.constants.GlobalErrIds} for list of valid error codes that can be set.  Valid values between 0 & 100_000.
     * @param msg contains textual information including method of origin and description of the root cause.
     */
    public AuthorizationException(int errorId, String msg)
    {
        super(errorId, msg);
    }
}

