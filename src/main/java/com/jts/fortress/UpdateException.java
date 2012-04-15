/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

/**
 * This exception extends {@link SecurityException} and is thrown when DAO cannot update entity.
 * See the {@link com.jts.fortress.constants.GlobalErrIds} javadoc for list of error ids.
 *
 * @author Shawn McKinney
 * @created August 30, 2009
 */
public class UpdateException extends SecurityException
{
    /**
     * Create an exception with an error code that maps to {@link com.jts.fortress.constants.GlobalErrIds} and message text.
     *
     * @param  errorId see {@link com.jts.fortress.constants.GlobalErrIds} for list of valid error codes that can be set.  Valid values between 0 & 100_000.
     * @param msg contains textual information including method of origin and description of the root cause.
     */
    public UpdateException(int errorId, String msg)
    {
        super(errorId, msg);
    }

    /**
     * Create exception with error id, message and related exception.
     * @param  errorId see {@link com.jts.fortress.constants.GlobalErrIds} for list of valid error codes that can be set.  Valid values between 0 & 100_000.
     * @param msg contains textual information including method of origin and description of the root cause.
     * @param previousException contains reference to related exception which usually is system related, i.e. ldap.
     */
    public UpdateException(int errorId, String msg, Exception previousException)
    {
        super(errorId, msg, previousException);
    }
}

