/*
 * Copyright © 2009-2013, JoshuaTree.  All Rights Reserved.
 */

package us.jts.fortress;


/**
 * This exception extends {@link SecurityException} and is thrown when administrative permission check fails.
 * See the {@link GlobalErrIds} javadoc for list of error ids.
 *
 * @author Shawn McKinney
 */
public class AuthorizationException extends SecurityException
{
    /**
     * Create an exception with an error code that maps to {@link GlobalErrIds} and message text.
     *
     * @param  errorId see {@link GlobalErrIds} for list of valid error codes that can be set.  Valid values between 0 & 100_000.
     * @param msg contains textual information including method of origin and description of the root cause.
     */
    public AuthorizationException(int errorId, String msg)
    {
        super(errorId, msg);
    }
}

