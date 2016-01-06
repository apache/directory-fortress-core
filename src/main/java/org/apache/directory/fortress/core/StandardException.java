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
 *  Interface that is implemented by exception base class {@link StandardException} used to associate a Fortress error code to 
 * the exception instance.
 * See the {@link GlobalErrIds} javadoc for list of error ids used by Fortress.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface StandardException
{
    /**
     * Return the Fortress error code that is optional to exceptions thrown within this security system.  See 
     * {@link GlobalErrIds} for list of all error codes.
     *
     * @return integer containing the source error code.  Valid values for Fortress error codes fall between 0 &amp; 100_000.
     */
    int getErrorId();
}
