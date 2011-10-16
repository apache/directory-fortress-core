/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.constants.GlobalErrIds;

/**
 * This exception extends {@code BaseRuntimeException} and is thrown when Fortress config startup failed.
 * This is critical runtime exception and means system is inoperable due to a configuration error.
 * See the {@link com.jts.fortress.constants.GlobalErrIds} javadoc for list of error ids.
 *
 * @author smckinn
 * @created February 20, 2011
 */
public class ConfigurationRuntimeException extends BaseRuntimeException
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
    public ConfigurationRuntimeException(int errorId, String newMsgText, Exception newException)
    {
        super(errorId, newMsgText, newException);
        this.m_Exception = newException;
    }
}
