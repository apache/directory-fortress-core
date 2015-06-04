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
package org.apache.directory.fortress.core.util.time;

/**
 * Class contains a custom timestamp that is processed by {@link Validator} to check {@link org.apache.directory.fortress.core.model.Constraint}.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Time
{
    /**
     * Stored as {@code System.out.getCurrentMillis()} format.
     */
    public Integer currentTime;

    /**
     * Stored in '1234567' format for Sun, Mon, Tue, Wed, Thur, Fri, Sat respectively.  i.e. '23456' is Mon-Friday.
     */
    public String day;

    /**
     * Stored in 'YYYYMMDD' format.  i.e. '20110101' is January 1, 2011.
     */
    public String date;
}

