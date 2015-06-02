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
package org.apache.directory.fortress.core.impl;


import org.apache.directory.fortress.core.model.PwMessage;

/**
 * Interface is used to allow pluggable password policy interrogation.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface PwPolicyControl
{
    /**
     * Check the password policy controls returned from server and sets the PwMessage with what it finds.
     *
     * @param controls ldap controls object.
     * @param isAuthenticated set to 'true' if password checks pass.
     * @param pwMsg describes the outcome of the policy checks.
     */
    void checkPasswordPolicy( Object[] controls, boolean isAuthenticated, PwMessage pwMsg );
}
