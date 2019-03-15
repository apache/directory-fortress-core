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
package org.apache.directory.fortress.core.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.directory.fortress.core.AuditMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.Manageable;
import org.apache.directory.fortress.core.model.AuthZ;
import org.apache.directory.fortress.core.model.Bind;
import org.apache.directory.fortress.core.model.FortRequest;
import org.apache.directory.fortress.core.model.FortResponse;
import org.apache.directory.fortress.core.model.Mod;
import org.apache.directory.fortress.core.model.UserAudit;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * This class performs searches across <a href="http://www.openldap.org/">OpenLDAP</a>'s slapd access log using HTTP access 
 * to Fortress Rest server. The access log events are
 * persisted in <a href="http://www.oracle.com/technetwork/database/berkeleydb/overview/index.html">BDB</a>.
 * Audit entries stored on behalf of Fortress operations correspond to runtime authentication 
 * {@link org.apache.directory.fortress.core.model.Bind}, authorization 
 * {@link org.apache.directory.fortress.core.model.AuthZ} and modification {@link Mod}
 * events as they occur automatically on the server when audit is enabled.
 * <h3></h3>
 * <h4>Audit Interrogator</h4>
 * Provides an OpenLDAP access log retrieval mechanism that enables security event monitoring.
 * <ol>
 *   <li>Authentication events:</li>
 *   <li>Session enablement events</li>
 *   <li>Authorization events</li>
 *   <li>Entity mods and deletes</li>
 * </li>
 * </ol>
 * <img src="../doc-files/Audit.png" alt="">
 * <p>
 * All events include Fortress context, see {@link org.apache.directory.fortress.core.model.FortEntity}.
 * <p>
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
 *     User Authentications:     <code>List&lt;{@link org.apache.directory.fortress.core.model.Bind}@gt;  
 *     {@link org.apache.directory.fortress.core.AuditMgr#searchBinds(org.apache.directory.fortress.core.model.UserAudit)}
 *     </code>
 *   </li>
 *   <li>
 *     Invalid Users AuthN:      <code>List&lt;{@link org.apache.directory.fortress.core.model.Bind}@gt;  
 *     {@link org.apache.directory.fortress.core.AuditMgr#searchInvalidUsers(
 *     org.apache.directory.fortress.core.model.UserAudit)} </code>
 *   </li>
 *   <li>
 *     User Authorizations 1:    <code>List&lt;{@link org.apache.directory.fortress.core.model.AuthZ}@gt; 
 *     {@link org.apache.directory.fortress.core.AuditMgr#getUserAuthZs(org.apache.directory.fortress.core.model.UserAudit)} 
 *     </code>
 *   </li>
 *   <li>
 *     User Authorizations 2:    <code>List&lt;{@link org.apache.directory.fortress.core.model.AuthZ}@gt; 
 *     {@link org.apache.directory.fortress.core.AuditMgr#searchAuthZs(org.apache.directory.fortress.core.model.UserAudit)} 
 *     </code>
 *   </li>
 *   <li>
 *     User Session Activations: <code>List&lt;{@link Mod}@gt;   
 *     {@link org.apache.directory.fortress.core.AuditMgr#searchUserSessions(
 *     org.apache.directory.fortress.core.model.UserAudit)} </code>
 *   </li>
 *   <li>
 *     Entity Modifications:     <code>List&lt;{@link Mod}@gt;   
 *     {@link org.apache.directory.fortress.core.AuditMgr#searchAdminMods(
 *     org.apache.directory.fortress.core.model.UserAudit)} </code>
 *   </li>
 * </ul>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AuditMgrRestImpl extends Manageable implements AuditMgr
{
    private static final String CLS_NM = AuditMgrRestImpl.class.getName();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AuthZ> getUserAuthZs(UserAudit uAudit)
        throws SecurityException
    {
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, CLS_NM + ".getUserAuthZs");
        List<AuthZ> outRecords;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(uAudit);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.AUDIT_UAUTHZS);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            outRecords = response.getEntities();
            // do not return a null list to the caller:
            if (outRecords == null)
            {
                outRecords = new ArrayList<>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return outRecords;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<AuthZ> searchAuthZs(UserAudit uAudit)
        throws SecurityException
    {
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, CLS_NM + ".searchAuthZs");
        List<AuthZ> outRecords;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(uAudit);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.AUDIT_AUTHZS);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            outRecords = response.getEntities();
            // do not return a null list to the caller:
            if (outRecords == null)
            {
                outRecords = new ArrayList<>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return outRecords;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Bind> searchBinds(UserAudit uAudit)
        throws SecurityException
    {
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, CLS_NM + ".searchBinds");
        List<Bind> outRecords;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(uAudit);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.AUDIT_BINDS);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            outRecords = response.getEntities();
            // do not return a null list to the caller:
            if (outRecords == null)
            {
                outRecords = new ArrayList<>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return outRecords;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mod> searchUserSessions(UserAudit uAudit)
        throws SecurityException
    {
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, CLS_NM + ".searchUserSessions");
        List<Mod> outRecords;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(uAudit);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.AUDIT_SESSIONS);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            outRecords = response.getEntities();
            // do not return a null list to the caller:
            if (outRecords == null)
            {
                outRecords = new ArrayList<>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return outRecords;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mod> searchAdminMods(UserAudit uAudit)
        throws SecurityException
    {
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, CLS_NM + ".searchAdminMods");
        List<Mod> outRecords;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(uAudit);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.AUDIT_MODS);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            outRecords = response.getEntities();
            // do not return a null list to the caller:
            if (outRecords == null)
            {
                outRecords = new ArrayList<>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return outRecords;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<AuthZ> searchInvalidUsers(UserAudit uAudit)
        throws SecurityException
    {
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, CLS_NM + ".searchInvalidUsers");
        List<AuthZ> outRecords;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(uAudit);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.AUDIT_INVLD);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            outRecords = response.getEntities();
            // do not return a null list to the caller:
            if (outRecords == null)
            {
                outRecords = new ArrayList<>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return outRecords;
    }
}
