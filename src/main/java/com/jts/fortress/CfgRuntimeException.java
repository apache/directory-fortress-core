/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

/**
 * This exception extends {@code BaseRuntimeException} and is thrown when Fortress config startup failed.
 * This is critical runtime exception and means system is inoperable due to a cfg error.
 * See the {@link GlobalErrIds} javadoc for list of error ids.
 *
 * @author Shawn McKinney
 * @created February 20, 2011
 */
public class CfgRuntimeException extends BaseRuntimeException
{
    private int subsystem;
    private Exception m_Exception;
    private Object moreInfo;

    /**
     * Create exception with error id, message and related exception.
     * @param errorId contains error code that is contained within {@link GlobalErrIds}
     * @param newMsgText contains text related to the exception.
     * @param newException contains related exception.
     */
    public CfgRuntimeException(int errorId, String newMsgText, Exception newException)
    {
        super(errorId, newMsgText, newException);
        this.m_Exception = newException;
    }

    /**
     * Create exception with error id and message.
     * @param errorId contains error code that is contained within {@link GlobalErrIds}
     * @param newMsgText contains text related to the exception.
     */
    public CfgRuntimeException(int errorId, String newMsgText)
    {
        super(errorId, newMsgText);
    }
}
