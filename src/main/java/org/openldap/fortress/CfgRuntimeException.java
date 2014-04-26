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
 * This exception extends {@link BaseRuntimeException} and is thrown when Fortress config startup failed.
 * This is critical runtime exception and means system is inoperable due to a cfg error.
 * See the {@link GlobalErrIds} javadoc for list of error ids.
 *
 * @author Shawn McKinney
 */
public class CfgRuntimeException extends BaseRuntimeException
{
    private int subsystem;
    private Exception exception;
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
        this.exception = newException;
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
