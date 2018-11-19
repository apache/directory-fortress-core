/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core;


/**
 * Base exception class for checked exceptions thrown.  This class wraps {@code java.lang.Exception}.  The BaseException
 * class adds int attribute which stores the necessary error code as required by all checked exceptions in this system.
 * The BaseException class has been extended by {@link SecurityException} which is then declared thrown on most Fortress public APIs.
 * See the {@link GlobalErrIds} javadoc for list of error ids that will be set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public abstract class BaseException extends Exception implements StandardException
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;

    private final int errorId;

    private int httpStatus = 500; // default is mapped to server error

    /**
     * Create exception containing error code and message.
     *
     * @param  errorId see {@link GlobalErrIds} for list of valid error codes that can be set.  Valid values between 0 & 100_000.
     * @param msg contains textual information including method of origin and description of the root cause.
     */
    BaseException( int errorId, String msg )
    {
        super( msg );
        this.errorId = errorId;
    }


    /**
     * Create exception containing error code, message and previous exception.
     *
     * @param  errorId see {@link GlobalErrIds} for list of valid error codes that can be set.  Valid values between 0 & 100_000.
     * @param msg contains textual information including method of origin and description of the root cause.
     * @param previousException contains reference to related exception which usually is system related, i.e. ldap.
     */
    BaseException( int errorId, String msg, Throwable previousException )
    {
        super( msg, previousException );
        this.errorId = errorId;
    }


    /**
     * Return the error id that is defined by this class {@link GlobalErrIds}.
     *
     * @return error id which is defined here {@link GlobalErrIds}.  Valid values for Fortress error codes fall between 0 and 100_000.
     */
    public int getErrorId()
    {
        return errorId;
    }


    /**
     * Returns the HTTP Status code mapped to represent this error
     * @return HTTP status code assigned for this exception
     */
    public int getHttpStatus()
    {
        return httpStatus;
    }


    public void setHttpStatus(int httpStatus)
    {
        this.httpStatus = httpStatus;
    }
}