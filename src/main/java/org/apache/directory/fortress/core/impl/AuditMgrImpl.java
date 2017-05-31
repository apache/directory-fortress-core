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

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.annotation.AdminPermissionOperation;
import org.apache.directory.fortress.core.AuditMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.ReviewMgr;
import org.apache.directory.fortress.core.ReviewMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.AuthZ;
import org.apache.directory.fortress.core.model.Bind;
import org.apache.directory.fortress.core.model.Mod;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserAudit;

/**
 * This object performs searches across <a href="http://www.openldap.org/">OpenLDAP</a>'s slapd access log.  The access log 
 * events are persisted in <a href="http://www.oracle.com/technetwork/database/berkeleydb/overview/index.html">BDB</a> and 
 * available for inquiry via common LDAP protocols.
 * Audit entries stored on behalf of Fortress operations correspond to runtime authentication 
 * {@link org.apache.directory.fortress.core.model.Bind}, authorization 
 * {@link org.apache.directory.fortress.core.model.AuthZ} and modification 
 * {@link org.apache.directory.fortress.core.model.Mod} events as they occur automatically on the server when audit is 
 * enabled.
 * <h3></h3>
 * <h4>Audit Interrogator</h4>
 * Provides an OpenLDAP access log retrieval mechanism that enables security event monitoring.
 * <ol>
 *   <li>Authentication events:</li>
 *   <li>Session enablement events</li>
 *   <li>Authorization events</li>
 *   <li>Entity mods and deletes</li>
 * </ol>
 * <img src="../doc-files/Audit.png" alt="">
 * <p>
 * All events include Fortress context, see {@link org.apache.directory.fortress.core.model.FortEntity}.
 * <h4>
 * The following APIs generate events subsequently stored in this access log:
 * </h4>
 * <ul>
 *   <li>{@link org.apache.directory.fortress.core.AccessMgr}</li>
 *   <li>{@link org.apache.directory.fortress.core.AdminMgr}</li>
 *   <li>{@link org.apache.directory.fortress.core.AdminMgr}</li>
 *   <li>{@link org.apache.directory.fortress.core.DelAdminMgr}</li>
 *   <li>{@link org.apache.directory.fortress.core.ConfigMgr}</li>
 *   <li>{@link org.apache.directory.fortress.core.PwPolicyMgr}</li>
 * </ul>
 * <h4>
 * The following reports are supported using search input: {@link org.apache.directory.fortress.core.model.UserAudit}
 * </h4>
 * <ul>
 *   <li>
 *     User Authentications: <code>List&lt;{@link org.apache.directory.fortress.core.model.Bind}&gt;  
 *     {@link org.apache.directory.fortress.core.AuditMgr#searchBinds(org.apache.directory.fortress.core.model.UserAudit)}
 *     </code>
 *   </li>
 *   <li>
 *     Invalid Users AuthN: <code>List&lt;{@link org.apache.directory.fortress.core.model.Bind}&gt;  
 *     {@link org.apache.directory.fortress.core.AuditMgr#searchInvalidUsers(org.apache.directory.fortress.core.model.UserAudit)} 
 *     </code>
 *   </li>
 *   <li>
 *     User Authorizations 1: <code>List&lt;{@link org.apache.directory.fortress.core.model.AuthZ}&gt; 
 *     {@link org.apache.directory.fortress.core.AuditMgr#getUserAuthZs(org.apache.directory.fortress.core.model.UserAudit)} 
 *     </code>
 *   </li>
 *   <li>
 *     User Authorizations 2: <code>List&lt;{@link org.apache.directory.fortress.core.model.AuthZ}&gt;
 *     {@link org.apache.directory.fortress.core.AuditMgr#searchAuthZs(org.apache.directory.fortress.core.model.UserAudit)} 
 *     </code>
 *   </li>
 *   <li>
 *     User Session Activations: <code>List&lt;{@link org.apache.directory.fortress.core.model.Mod}&gt;  
 *     {@link org.apache.directory.fortress.core.AuditMgr#searchUserSessions(org.apache.directory.fortress.core.model.UserAudit)} 
 *     </code>
 *   </li>
 *   <li>
 *     Entity Modifications: <code>List&lt;{@link org.apache.directory.fortress.core.model.Mod}&gt;   
 *     {@link org.apache.directory.fortress.core.AuditMgr#searchAdminMods(org.apache.directory.fortress.core.model.UserAudit)}
 *     </code>
 *   </li>
 * </ul>
 * <p>
 * This class is NOT thread safe if parent instance variables ({@link #contextId} or {@link #adminSess}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AuditMgrImpl extends Manageable implements AuditMgr, Serializable
{
    private static final String CLS_NM = AuditMgrImpl.class.getName();
    private static final AuditP auditP = new AuditP();

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<AuthZ> getUserAuthZs(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "getUserAuthZs";
        assertContext(CLS_NM, methodName, uAudit, GlobalErrIds.AUDT_INPUT_NULL);
        checkAccess(CLS_NM, methodName);
        return auditP.getAuthZs(uAudit);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<AuthZ> searchAuthZs(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchAuthZs";
        assertContext(CLS_NM, methodName, uAudit, GlobalErrIds.AUDT_INPUT_NULL);
        checkAccess(CLS_NM, methodName);
        return auditP.searchAuthZs(uAudit);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<Bind> searchBinds(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchBinds";
        assertContext(CLS_NM, methodName, uAudit, GlobalErrIds.AUDT_INPUT_NULL);
        checkAccess(CLS_NM, methodName);
        return auditP.searchBinds(uAudit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<Mod> searchUserSessions(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchUserSessions";
        assertContext(CLS_NM, methodName, uAudit, GlobalErrIds.AUDT_INPUT_NULL);
        checkAccess(CLS_NM, methodName);
        return auditP.searchUserMods(uAudit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<Mod> searchAdminMods(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchAdminMods";
        assertContext(CLS_NM, methodName, uAudit, GlobalErrIds.AUDT_INPUT_NULL);
        checkAccess(CLS_NM, methodName);
        if ( StringUtils.isNotEmpty( uAudit.getUserId() ))
        {
            ReviewMgr rMgr = ReviewMgrFactory.createInstance(this.contextId);
            User user = rMgr.readUser(new User(uAudit.getUserId()));
            uAudit.setInternalUserId(user.getInternalId());
        }
        return auditP.searchAdminMods(uAudit);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<AuthZ> searchInvalidUsers(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchInvalidUsers";
        assertContext(CLS_NM, methodName, uAudit, GlobalErrIds.AUDT_INPUT_NULL);
        checkAccess(CLS_NM, methodName);
        return auditP.searchInvalidAuthNs(uAudit);
    }
}