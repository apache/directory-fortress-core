/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress;

/**
 *  Base runtime exception class for Fortress runtime exceptions.
 * See the {@link GlobalErrIds} javadoc for list of error ids.
 *
 * @author Shawn McKinney
 */
public abstract class BaseRuntimeException extends RuntimeException
{

	private final int errorId;
	private final String[] msgParams;

    /**
     *
     * @param errorId int contains the error id which is defined here {@link GlobalErrIds}.
     * @param msgParam contains message pertaining to exception.
     * @param previousException contains reference to related exception which usually is system related, i.e. ldap.
     */
	protected BaseRuntimeException(int errorId, String msgParam, Throwable previousException)
	{
		super(msgParam + ", errCode=" + errorId, previousException);
		this.errorId = errorId;
		this.msgParams = new String[1];
		this.msgParams[0] = msgParam;
	}

    /**
     *
     * @param errorId int contains the error id which is defined here {@link GlobalErrIds}.
     * @param msgParam contains message pertaining to exception.
     */
	protected BaseRuntimeException(int errorId, String msgParam)
	{
		super(msgParam + ", errCode=" + errorId);
		this.errorId = errorId;
		this.msgParams = new String[1];
		this.msgParams[0] = msgParam;
	}

    /**
     * Return the message for current exception.
     *
     * @return string contains the error message.
     */
	public String getMsg()
	{
        String msg = null;
		if (this.msgParams != null)
            msg = this.msgParams[0];
		return msg;
	}

    /**
     * Return the error id that is defined by this class {@link GlobalErrIds}.
     *
     * @return error id which is defined here {@link GlobalErrIds}.
     */
	public int getErrorId()
	{
		return errorId;
	}
}
