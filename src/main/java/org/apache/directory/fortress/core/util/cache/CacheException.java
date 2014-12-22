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
package org.apache.directory.fortress.core.util.cache;


import org.apache.directory.fortress.core.BaseRuntimeException;


/**
 * This exception extends {@code BaseRuntimeException} and is thrown when Fortress caching operation failed.
 * This is critical runtime exception and means system is inoperable due to a caching error.
 * See the {@link org.apache.directory.fortress.core.GlobalErrIds} javadoc for list of error ids.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
class CacheException extends BaseRuntimeException
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;
    private int subsystem;
    private Exception exception;
    private Object moreInfo;


    /**
     * Create exception with error id, message and related exception.
     * @param errorId contains error code that is contained within {@link org.apache.directory.fortress.core.GlobalErrIds}
     * @param newMsgText contains text related to the exception.
     * @param newException contains related exception.
     */
    public CacheException( int errorId, String newMsgText, Exception newException )
    {
        super( errorId, newMsgText, newException );
        this.exception = newException;
    }


    /**
     * Create exception with error id and message.
     * @param errorId contains error code that is contained within {@link org.apache.directory.fortress.core.GlobalErrIds}
     * @param newMsgText contains text related to the exception.
     */
    public CacheException( int errorId, String newMsgText )
    {
        super( errorId, newMsgText );
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
