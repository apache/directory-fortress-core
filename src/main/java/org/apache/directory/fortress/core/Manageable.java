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


import org.apache.directory.fortress.core.model.Session;


/**
 * Interface allows outside clients to manage security and multi-tenant concerns within the Fortress runtime.
 * The {@link #setAdmin(org.apache.directory.fortress.core.model.Session)} method allows A/RBAC sessions to be loaded and 
 * allows authorization to be performed on behalf of the user who is contained within the Session object itself.
 * The ARBAC permissions will be checked each time outside client makes calls into Fortress API.
 * This interface also allows Fortress clients to operate in a multi-tenant fashion using {@link #setContextId(String)}.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface Manageable
{
    /**
     * Use this method to load an administrative user's ARBAC Session object into Manager object which will enable 
     * authorization to be performed on behalf of admin user.
     * Setting Session into this object will enforce ARBAC controls and render this class' implementer thread unsafe.
     *
     * @param session contains a valid Fortress ARBAC Session object.
     */
    void setAdmin( Session session );


    /**
     * Use this method to set the tenant id onto function call into Fortress which allows segregation of data by customer.
     * The contextId is used for multi-tenancy to isolate data sets within a particular sub-tree within DIT.
     * Setting contextId into this object will render this class' implementer thread unsafe.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     */
    void setContextId( String contextId );
}