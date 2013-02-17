/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */
package us.jts.fortress.util.cache;

import us.jts.fortress.BaseRuntimeException;


/**
 * This exception extends {@code BaseRuntimeException} and is thrown when Fortress caching operation failed.
 * This is critical runtime exception and means system is inoperable due to a caching error.
 * See the {@link us.jts.fortress.GlobalErrIds} javadoc for list of error ids.
 *
 * @author Shawn McKinney
 */
class CacheException extends BaseRuntimeException
{
    private int subsystem;
    private Exception exception;
    private Object moreInfo;

    /**
     * Create exception with error id, message and related exception.
     * @param errorId contains error code that is contained within {@link us.jts.fortress.GlobalErrIds}
     * @param newMsgText contains text related to the exception.
     * @param newException contains related exception.
     */
    public CacheException(int errorId, String newMsgText, Exception newException)
    {
        super(errorId, newMsgText, newException);
        this.exception = newException;
    }

    /**
     * Create exception with error id and message.
     * @param errorId contains error code that is contained within {@link us.jts.fortress.GlobalErrIds}
     * @param newMsgText contains text related to the exception.
     */
    public CacheException(int errorId, String newMsgText)
    {
        super(errorId, newMsgText);
    }

    /**
     * Get the exception object.
     *
     * @return reference to Exception.
     */
    public Exception getException()
    {
        return exception;
    }
}

