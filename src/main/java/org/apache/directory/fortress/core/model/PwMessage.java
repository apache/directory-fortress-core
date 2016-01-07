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
package org.apache.directory.fortress.core.model;


import java.util.List;


/**
 * Interface is implemented by {@link org.apache.directory.fortress.core.model.Session} and prescribes methods used to return Fortress
 * password messages to the caller.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface PwMessage
{
    /**
     * Return the {@link org.apache.directory.fortress.core.model.User#userId} from entity.
     *
     * @param userId maps to {@code uid} attribute on inetOrgPerson object class.
     */
    void setUserId( String userId );


    /**
     * Set the {@link org.apache.directory.fortress.core.model.User#userId} in entity.
     *
     * @return userId maps to {@code uid} attribute on inetOrgPerson object class.
     */
    String getUserId();


    /**
     * Contains the message that corresponds to password.  These messages map to {@link org.apache.directory.fortress.core.impl.GlobalPwMsgIds#pwMsgs}
     *
     * @param message
     */
    void setMsg( String message );


    /**
     * Return the message that corresponds to last password check.
     *
     * @return message maps to {@link org.apache.directory.fortress.core.impl.GlobalPwMsgIds#pwMsgs}
     */
    String getMsg();


    /**
     * If set to true the user's password check out good.
     *
     * @param isAuthenticated
     */
    void setAuthenticated( boolean isAuthenticated );


    /**
     * If set to true the user's password check out good.
     *
     * @return param isAuthenticated
     */
    boolean isAuthenticated();


    /**
     * Return the warning id that pertain to User's password. This attribute maps to values between 0 and 100 contained within here {@link org.apache.directory.fortress.core.impl.GlobalPwMsgIds}
     *
     * @param warning contains warning id.
     */
    void setWarning( Warning warning );


    void setWarnings( List<Warning> warnings );


    //void setWarningId(int warning);

    /**
     * Set the warning id that pertain to User's password. This attribute maps to values between 0 and 100 contained within here {@link org.apache.directory.fortress.core.impl.GlobalPwMsgIds}
     *
     * @return warning contains warning id.
     */
    List<Warning> getWarnings();


    //int getWarningId();

    /**
     * Set the error id that pertain to User's password. This attribute maps to values greater than or equal to 100 contained within here {@link org.apache.directory.fortress.core.impl.GlobalPwMsgIds}
     *
     * @param error contains error id that maps to {@link org.apache.directory.fortress.core.impl.GlobalPwMsgIds#pwIds}
     */
    void setErrorId( int error );


    /**
     * Return the error id that pertain to User's password. This attribute maps to values greater than or equal to 100 contained within here {@link org.apache.directory.fortress.core.impl.GlobalPwMsgIds}
     *
     * @return error contains error id that maps to {@link org.apache.directory.fortress.core.impl.GlobalPwMsgIds#pwIds}
     */
    int getErrorId();


    /**
     * Grace count indicates how many binds User can perform before password slips into expired state.
     *
     * @param grace integer containing number of binds allowed for user.
     */
    void setGraceLogins( int grace );


    /**
     * Get the grace count which indicates how many binds User can perform before password slips into expired state.
     *
     * @return grace integer containing number of binds allowed for user.
     */
    int getGraceLogins();


    /**
     * The number of seconds until the User's password expires.
     *
     * @param expire value is computed by ldap server and contains the number of seconds until password will expire.
     */
    void setExpirationSeconds( int expire );


    /**
     * Get the number of seconds until the User's password expires.
     *
     * @return expire value is computed by ldap server and contains the number of seconds until password will expire.
     */
    int getExpirationSeconds();
}
