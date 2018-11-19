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
 * This exception extends {@link SecurityException} and is thrown when password check fails.
 * See the {@link GlobalErrIds} javadoc for list of error ids.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class PasswordException extends SecurityException
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;


    /**
     * Create an exception with an error code that maps to {@link GlobalErrIds} and message text.
     *
     * @param  errorId see {@link GlobalErrIds} for list of valid error codes that can be set.  Valid values between 
     * 0 &amp; 100_000.
     * @param msg contains textual information including method of origin and description of the root cause.
     */
    public PasswordException( int errorId, String msg )
    {
        super( errorId, msg );
        setHttpStatus(400);
    }
}
