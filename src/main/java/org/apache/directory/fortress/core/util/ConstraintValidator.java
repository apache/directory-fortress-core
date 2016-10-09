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
package org.apache.directory.fortress.core.util;

import org.apache.directory.fortress.core.ValidationException;

/**
 * This interface provides constraint entity validation.
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface ConstraintValidator
{
    /**
     * Perform simple reasonability check on contraint timeout value.
     *
     * @param timeout must be greater than 0 and less than max value for {@link Integer#MAX_VALUE}
     * @throws org.apache.directory.fortress.core.ValidationException
     *          in the event value falls out of range.
     */
    void timeout( Integer timeout ) throws ValidationException;

    /**
     * Perform simple reasonability check on contraint beginTime value.
     *
     * @param beginTime if set, must be between '0000' and '2400'.
     * @throws org.apache.directory.fortress.core.ValidationException
     *          in the event value falls out of range.
     */
    void beginTime( String beginTime ) throws ValidationException;

    /**
     * Perform simple reasonability check on contraint endTime value.
     * @param endTime if set, must be between '0000' and '2400'.
     * @throws ValidationException in the event value falls out of range.
     */
    void endTime( String endTime ) throws ValidationException;

    /**
     * Perform simple reasonability check on contraint beginDate value.
     * @param beginDate if set, must be format 'YYYYMMDD'.
     * @throws ValidationException in the event value falls out of range.
     */
    void beginDate( String beginDate ) throws ValidationException;

    /**
     * Perform simple reasonability check on contraint endDate value.
     * @param endDate if set, must be format 'YYYYMMDD'.
     * @throws ValidationException in the event value falls out of range.
     */
    void endDate( String endDate ) throws ValidationException;

    /**
     * Perform simple reasonability check on contraint dayMask value.
     * @param dayMask format is '1234567', 1 = Sunday, 2 = Monday, etc.  Any or all of the 'bits' may be left blank.
     * @throws ValidationException in the event value falls out of range.
     */
    void dayMask( String dayMask ) throws ValidationException;
}
