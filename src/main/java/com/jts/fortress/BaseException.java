/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

/**
 * Base exception class for checked exceptions thrown.  This class wraps {@code java.lang.Exception}.  The BaseException
 * class adds int attribute which stores the necessary error code as required by all checked exceptions in this system.
 * The BaseException class has been extended by {@link SecurityException} which is then declared thrown on most Fortress public APIs.
 * See the {@link com.jts.fortress.constants.GlobalErrIds} javadoc for list of error ids that will be set.
 *
 * @author     smckinn
 * @created    August 23, 2009
 */
public abstract class BaseException extends Exception implements StandardException
{
	private int errorId;

    /**
     * Create exception containing error code and message.
     *
     * @param  errorId see {@link com.jts.fortress.constants.GlobalErrIds} for list of valid error codes that can be set.  Valid values between 0 & 100_000.
     * @param msg contains textual information including method of origin and description of the root cause.
     */
	public BaseException(int errorId, String msg)
	{
		super(msg);
		this.errorId = errorId;
	}

    /**
     * Create exception containing error code, message and previous exception.
     *
     * @param  errorId see {@link com.jts.fortress.constants.GlobalErrIds} for list of valid error codes that can be set.  Valid values between 0 & 100_000.
     * @param msg contains textual information including method of origin and description of the root cause.
     * @param previousException contains reference to related exception which usually is system related, i.e. ldap.
     */
	public BaseException(int errorId, String msg, Throwable previousException)
	{
		super(msg, previousException);
		this.errorId = errorId;
	}

    /**
     * Return the error id that is defined by this class {@link com.jts.fortress.constants.GlobalErrIds}.
     *
     * @return error id which is defined here {@link com.jts.fortress.constants.GlobalErrIds}.  Valid values for Fortress error codes fall between 0 and 100_000.
     */
	public int getErrorId()
	{
		return errorId;
	}
}