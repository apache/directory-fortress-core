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
 *  Base runtime exception class for Fortress runtime exceptions.
 * See the {@link GlobalErrIds} javadoc for list of error ids.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public abstract class BaseRuntimeException extends RuntimeException
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;

    private final int errorId;
    private final String[] msgParams;


    /**
     *
     * @param errorId int contains the error id which is defined here {@link GlobalErrIds}.
     * @param msgParam contains message pertaining to exception.
     * @param previousException contains reference to related exception which usually is system related, i.e. ldap.
     */
    protected BaseRuntimeException( int errorId, String msgParam, Throwable previousException )
    {
        super( msgParam + ", errCode=" + errorId, previousException );
        this.errorId = errorId;
        this.msgParams = new String[1];
        this.msgParams[0] = msgParam;
    }


    /**
     *
     * @param errorId int contains the error id which is defined here {@link GlobalErrIds}.
     * @param msgParam contains message pertaining to exception.
     */
    protected BaseRuntimeException( int errorId, String msgParam )
    {
        super( msgParam + ", errCode=" + errorId );
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

        if ( this.msgParams != null )
        {
            msg = this.msgParams[0];
        }

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
