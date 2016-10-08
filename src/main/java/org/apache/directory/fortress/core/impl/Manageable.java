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

import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.ValidationException;
import org.apache.directory.fortress.core.model.FortEntity;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * Abstract class allows outside clients to manage security and multi-tenant concerns within the Fortress runtime.
 * The {@link #setAdmin(org.apache.directory.fortress.core.model.Session)} method allows A/RBAC sessions to be loaded and 
 * allows authorization
 * to be performed on behalf of the user who is contained within the Session object itself.
 * The ARBAC permissions will be checked each time outside client makes calls into Fortress API.
 * This allows Fortress clients to operate in a multi-tenant context: {@link #setContextId(String)}.
 * <p>
 * Implementers of this abstract class will NOT be thread safe iff the instance variables are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public abstract class Manageable implements org.apache.directory.fortress.core.Manageable
{
    // These instance variables are the reason why children of this abstract class will not be thread safe:
    protected Session adminSess;
    protected String contextId;

    /**
     * Use this method to load an administrative user's ARBAC Session object into Manager object will enable authorization to
     * be performed on behalf of admin user.  Setting Session into this object will enforce ARBAC controls and render this 
     * class's implementer thread unsafe.
     *
     * @param session contains a valid Fortress A/RBAC Session object.
     */
    public final void setAdmin(Session session)
    {
        this.adminSess = session;
    }

    /**
     * Use this method to set the tenant id onto function call into Fortress which allows segregation of data by customer.
     * The contextId is used for multi-tenancy to isolate data sets within a particular sub-tree within DIT.
     * Setting contextId into this object will render this class' implementer thread unsafe.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     */
    public final void setContextId(String contextId)
    {
        this.contextId = contextId;
    }

    /**
     * Set A/RBAC session on entity and perform authorization on behalf of the caller if the {@link #adminSess} is set.
     *
     * @param className contains the class name.
     * @param opName contains operation name.
     * @param entity contains {@link org.apache.directory.fortress.core.model.FortEntity} instance.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of data validation or system error.
     */
    protected final void setEntitySession(String className, String opName, FortEntity entity) throws SecurityException
    {
        entity.setContextId(this.contextId);
        if (this.adminSess != null)
        {
            Permission perm = new Permission(className, opName);
            perm.setContextId(this.contextId);
            AdminUtil.setEntitySession( this.adminSess, perm, entity, this.contextId );
        }
    }

    /**
     * Every Fortress Manager API (e.g. addUser, updateUser, addRole, ...) will perform authorization on behalf of the 
     * caller IFF the {@link AuditMgrImpl#adminSess} has been set before invocation.
     *
     * @param className contains the class name.
     * @param opName contains operation name.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          in the event of data validation or system error.
     */
    protected final void checkAccess(String className, String opName) throws SecurityException
    {
        if (this.adminSess != null)
        {
            Permission perm = new Permission(className, opName);
            perm.setContextId(this.contextId);
            AdminUtil.checkAccess(this.adminSess, perm, this.contextId);
        }
    }

    /**
     * Method is called by Manager APIs to load contextual information on {@link FortEntity}.
     * <p>
     * The information is used to
     * <ol>
     *   <li>
     *     Load the administrative User's {@link Session} object into entity.  This is used for checking to ensure 
     *     administrator has privilege to perform administrative operation.
     *   </li>
     *   <li>
     *     Load the target operation's permission into the audit context.  This is used for Fortress audit log stored in 
     *     OpenLDAP
     *   </li>
     * </ol>
     *
     * @param className contains the class name.
     * @param opName contains operation name.
     * @param entity  used to pass contextual information through Fortress layers for administrative security checks and 
     * audit.
     */
    protected final void setAdminData(String className, String opName, FortEntity entity)
    {
        if (this.adminSess != null)
        {
            Permission perm = new Permission(className, opName);
            entity.setAdminSession(this.adminSess);
            entity.setModCode(AdminUtil.getObjName(perm.getObjName()) + "." + perm.getOpName());
        }
        
        entity.setContextId(this.contextId);
    }

    /**
     * Method will throw exception if entity reference is null, otherwise will set the contextId of the tenant onto the 
     * supplied entity reference.
     * @param className contains the class name of caller.
     * @param opName contains operation name of caller.
     * @param entity  used here to pass the tenant id into the Fortress DAO layer..
     * @param errorCode contains the error id to use if null.
     * @throws ValidationException in the event object is null.
     */
    protected final void assertContext( String className, String opName, FortEntity entity, int errorCode ) throws ValidationException
    {
        VUtil.assertNotNull( entity, errorCode, getFullMethodName( className, opName ) );
        entity.setContextId( contextId );
    }

    /**
     * Method will throw exception if entity reference is null, otherwise will set the contextId of the tenant onto the 
     * supplied entity reference.
     * 
     * @param methodName contains the full method name of caller.
     * @param entity  used here to pass the tenant id into the Fortress DAO layer..
     * @param errorCode contains the error id to use if null.
     * @throws ValidationException in the event object is null.
     */
    protected final void assertContext( String methodName, FortEntity entity, int errorCode ) throws ValidationException
    {
        VUtil.assertNotNull( entity, errorCode, methodName );
        entity.setContextId( contextId );
    }

    /**
     * This method is used to generate log statements and returns the concatenation of class name to the operation name.
     * @param className of the caller
     * @param opName of the caller
     * @return className + '.' + opName
     */
    protected final String getFullMethodName(String className, String opName)
    {
        return className + "." + opName;
    }
}