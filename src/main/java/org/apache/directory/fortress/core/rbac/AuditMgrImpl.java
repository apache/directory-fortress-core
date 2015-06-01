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
package org.apache.directory.fortress.core.rbac;

import org.apache.directory.fortress.core.AuditMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.ReviewMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.ReviewMgr;
import org.apache.directory.fortress.core.model.AuthZ;
import org.apache.directory.fortress.core.model.Bind;
import org.apache.directory.fortress.core.model.Mod;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserAudit;
import org.apache.directory.fortress.core.util.attr.VUtil;

import java.util.List;

/**
 * This object performs searches across <a href="http://www.openldap.org/">OpenLDAP</a>'s slapd access log.  The access log events are
 * persisted in <a href="http://www.oracle.com/technetwork/database/berkeleydb/overview/index.html">BDB</a> and available for inquiry via common LDAP protocols.
 * Audit entries stored on behalf of Fortress operations correspond to runtime authentication {@link org.apache.directory.fortress.core.model.Bind}, authorization {@link org.apache.directory.fortress.core.model.AuthZ} and modification {@link org.apache.directory.fortress.core.model.Mod}
 * events as they occur automatically on the server when audit is enabled.
 * <h4>Audit Interrogator</h4>
 * Provides an OpenLDAP access log retrieval mechanism that enables security event monitoring.
 * <ol>
 * <li>Authentication events:
 * <li>Session enablement events
 * <li>Authorization events
 * <li>Entity mods and deletes
 * </li>
 * </ol>
 * <img src="../doc-files/Audit.png">
 * <p/>
 * All events include Fortress context, see {@link org.apache.directory.fortress.core.model.FortEntity}.
 * <p/>
 * <h4>
 * The following APIs generate events subsequently stored in this access log:
 * </h4>
 * <ul>
 * <li> {@link org.apache.directory.fortress.core.AccessMgr}
 * <li> {@link org.apache.directory.fortress.core.AdminMgr}
 * <li> {@link org.apache.directory.fortress.core.AdminMgr}
 * <li> {@link org.apache.directory.fortress.core.DelAdminMgr}
 * <li> {@link org.apache.directory.fortress.core.cfg.ConfigMgr}
 * <li> {@link org.apache.directory.fortress.core.PwPolicyMgr}
 * </ul>
 * <h4>
 * The following reports are supported using search input: {@link org.apache.directory.fortress.core.model.UserAudit}
 * </h4>
 * <ul>
 * <li>User Authentications:     <code>List<{@link org.apache.directory.fortress.core.model.Bind}>  {@link org.apache.directory.fortress.core.AuditMgr#searchBinds(org.apache.directory.fortress.core.model.UserAudit)}</code>
 * <li>Invalid Users AuthN:      <code>List<{@link org.apache.directory.fortress.core.model.Bind}>  {@link org.apache.directory.fortress.core.AuditMgr#searchInvalidUsers(org.apache.directory.fortress.core.model.UserAudit)} </code>
 * <li>User Authorizations 1:    <code>List<{@link org.apache.directory.fortress.core.model.AuthZ}> {@link org.apache.directory.fortress.core.AuditMgr#getUserAuthZs(org.apache.directory.fortress.core.model.UserAudit)} </code>
 * <li>User Authorizations 2:    <code>List<{@link org.apache.directory.fortress.core.model.AuthZ}> {@link org.apache.directory.fortress.core.AuditMgr#searchAuthZs(org.apache.directory.fortress.core.model.UserAudit)} </code>
 * <li>User Session Activations: <code>List<{@link org.apache.directory.fortress.core.model.Mod}>   {@link org.apache.directory.fortress.core.AuditMgr#searchUserSessions(org.apache.directory.fortress.core.model.UserAudit)} </code>
 * <li>Entity Modifications:     <code>List<{@link org.apache.directory.fortress.core.model.Mod}>   {@link org.apache.directory.fortress.core.AuditMgr#searchAdminMods(org.apache.directory.fortress.core.model.UserAudit)} </code>
 *
 * </ul>
 * <p/>
 * This class is NOT thread safe if parent instance variables ({@link #contextId} or {@link #adminSess}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AuditMgrImpl extends Manageable implements AuditMgr
{
    private static final String CLS_NM = AuditMgrImpl.class.getName();
    private static final AuditP auditP = new AuditP();

    /**
     * This method returns a list of authorization events for a particular user {@link org.apache.directory.fortress.core.model.UserAudit#userId}
     * and given timestamp field {@link org.apache.directory.fortress.core.model.UserAudit#beginDate}.<BR>
     * Method also can discriminate between all events or failed only by setting {@link org.apache.directory.fortress.core.model.UserAudit#failedOnly}.
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.model.UserAudit#userId} - contains the target userId</li>
     * <li>{@link org.apache.directory.fortress.core.model.UserAudit#beginDate} - contains the date in which to begin search</li>
     * <li>{@link org.apache.directory.fortress.core.model.UserAudit#failedOnly} - if set to 'true', return only failed authorization events</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          if a runtime system error occurs.
     */
    @Override
    public List<AuthZ> getUserAuthZs(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "getUserAuthZs";
        assertContext(CLS_NM, methodName, uAudit, GlobalErrIds.AUDT_INPUT_NULL);
        checkAccess(CLS_NM, methodName);
        return auditP.getAuthZs(uAudit);
    }


    /**
     * This method returns a list of authorization events for a particular user {@link UserAudit#userId},
     * object {@link UserAudit#objName}, and given timestamp field {@link UserAudit#beginDate}.<BR>
     * Method also can discriminate between all events or failed only by setting flag {@link UserAudit#failedOnly}..
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link UserAudit#userId} - contains the target userId<</li>
     * <li>{@link UserAudit#objName} - contains the object (authorization resource) name</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * <li>{@link UserAudit#failedOnly} - if set to 'true', return only failed authorization events</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws SecurityException if a runtime system error occurs.
     */
    @Override
    public List<AuthZ> searchAuthZs(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchAuthZs";
        assertContext(CLS_NM, methodName, uAudit, GlobalErrIds.AUDT_INPUT_NULL);
        checkAccess(CLS_NM, methodName);
        return auditP.searchAuthZs(uAudit);
    }


    /**
     * This method returns a list of authentication audit events for a particular user {@link UserAudit#userId},
     * and given timestamp field {@link UserAudit#beginDate}.<BR>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserAudit#userId} - contains the target userId<</li>
     * <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * <li>{@link UserAudit#failedOnly} - if set to 'true', return only failed authorization events</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Bind.  Each Bind object contains one bind event.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          if a runtime system error occurs.
     */
    @Override
    public List<Bind> searchBinds(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchBinds";
        assertContext(CLS_NM, methodName, uAudit, GlobalErrIds.AUDT_INPUT_NULL);
        checkAccess(CLS_NM, methodName);
        return auditP.searchBinds(uAudit);
    }

    /**
     * This method returns a list of sessions created for a given user {@link UserAudit#userId},
     * and timestamp {@link UserAudit#beginDate}.<BR>
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link UserAudit#userId} - contains the target userId<</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Mod.  Each Mod object in list corresponds to one update or delete event on directory.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          if a runtime system error occurs.
     */
    @Override
    public List<Mod> searchUserSessions(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchUserSessions";
        assertContext(CLS_NM, methodName, uAudit, GlobalErrIds.AUDT_INPUT_NULL);
        checkAccess(CLS_NM, methodName);
        return auditP.searchUserMods(uAudit);
    }

    /**
     * This method returns a list of admin operations events for a particular entity {@link UserAudit#dn},
     * object {@link UserAudit#objName} and timestamp {@link UserAudit#beginDate}.  If the internal
     * userId {@link UserAudit#internalUserId} is set it will limit search by that field.
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserAudit#dn} - contains the LDAP distinguished name for the updated object.  For example if caller
     * wants to find out what changes were made to John Doe's user object this would be 'uid=jdoe,ou=People,dc=example,dc=com'</li>
     * <li>{@link UserAudit#objName} - contains the object (authorization resource) name corresponding to the event.  For example if caller
     * wants to return events where User object was modified, this would be 'updateUser'</li>
     * <li>{@link UserAudit#internalUserId} - maps to the internalUserId of user who changed the record in LDAP.  This maps to {@link org.apache.directory.fortress.core.model.User#internalId}.</li>
     * <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * <li>{@link UserAudit#endDate} - contains the date in which to end search</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Mod.  Each Mod object in list corresponds to one update or delete event on directory.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          if a runtime system error occurs.
     */
    @Override
    public List<Mod> searchAdminMods(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchAdminMods";
        assertContext(CLS_NM, methodName, uAudit, GlobalErrIds.AUDT_INPUT_NULL);
        checkAccess(CLS_NM, methodName);
        if (VUtil.isNotNullOrEmpty(uAudit.getUserId()))
        {
            ReviewMgr rMgr = ReviewMgrFactory.createInstance(this.contextId);
            User user = rMgr.readUser(new User(uAudit.getUserId()));
            uAudit.setInternalUserId(user.getInternalId());
        }
        return auditP.searchAdminMods(uAudit);
    }


    /**
     * This method returns a list of failed authentication events for a particular invalid user {@link UserAudit#userId},
     * and given timestamp {@link UserAudit#beginDate}.  If the {@link UserAudit#failedOnly} is true it will
     * return only authentication attempts made with invalid userId.
     * </p>
     * This is possible because Fortress performs read on user before the bind.
     * </p>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserAudit#userId} - contains the target userId</li>
     * <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * <li>{@link UserAudit#failedOnly} - if set to 'true', return only failed authorization events</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one failed authentication event.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          if a runtime system error occurs.
     */
    @Override
    public List<AuthZ> searchInvalidUsers(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchInvalidUsers";
        assertContext(CLS_NM, methodName, uAudit, GlobalErrIds.AUDT_INPUT_NULL);
        checkAccess(CLS_NM, methodName);
        return auditP.searchInvalidAuthNs(uAudit);
    }
}