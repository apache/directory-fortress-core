/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */

package org.openldap.fortress.rest;

import org.openldap.fortress.AuditMgr;
import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.SecurityException;
import org.openldap.fortress.rbac.AuthZ;
import org.openldap.fortress.rbac.Bind;
import org.openldap.fortress.rbac.Manageable;
import org.openldap.fortress.rbac.Mod;
import org.openldap.fortress.rbac.UserAudit;
import org.openldap.fortress.util.attr.VUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class performs searches across <a href="http://www.openldap.org/">OpenLDAP</a>'s slapd access log using HTTP access to En Masse REST server.
 * The access log events are
 * persisted in <a href="http://www.oracle.com/technetwork/database/berkeleydb/overview/index.html">BDB</a>.
 * Audit entries stored on behalf of Fortress operations correspond to runtime authentication {@link org.openldap.fortress.rbac.Bind}, authorization {@link org.openldap.fortress.rbac.AuthZ} and modification {@link Mod}
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
 * All events include Fortress context, see {@link org.openldap.fortress.rbac.FortEntity}.
 * <p/>
 * <h4>
 * The following APIs generate events subsequently stored in this access log:
 * </h4>
 * <ul>
 * <li> {@link org.openldap.fortress.AccessMgr}
 * <li> {@link org.openldap.fortress.AdminMgr}
 * <li> {@link org.openldap.fortress.AdminMgr}
 * <li> {@link org.openldap.fortress.DelAdminMgr}
 * <li> {@link org.openldap.fortress.cfg.ConfigMgr}
 * <li> {@link org.openldap.fortress.PwPolicyMgr}
 * </ul>
 * <h4>
 * The following reports are supported using search input: {@link org.openldap.fortress.rbac.UserAudit}
 * </h4>
 * <ul>
 * <li>User Authentications:     <code>List<{@link org.openldap.fortress.rbac.Bind}>  {@link org.openldap.fortress.AuditMgr#searchBinds(org.openldap.fortress.rbac.UserAudit)}</code>
 * <li>Invalid Users AuthN:      <code>List<{@link org.openldap.fortress.rbac.Bind}>  {@link org.openldap.fortress.AuditMgr#searchInvalidUsers(org.openldap.fortress.rbac.UserAudit)} </code>
 * <li>User Authorizations 1:    <code>List<{@link org.openldap.fortress.rbac.AuthZ}> {@link org.openldap.fortress.AuditMgr#getUserAuthZs(org.openldap.fortress.rbac.UserAudit)} </code>
 * <li>User Authorizations 2:    <code>List<{@link org.openldap.fortress.rbac.AuthZ}> {@link org.openldap.fortress.AuditMgr#searchAuthZs(org.openldap.fortress.rbac.UserAudit)} </code>
 * <li>User Session Activations: <code>List<{@link Mod}>   {@link org.openldap.fortress.AuditMgr#searchUserSessions(org.openldap.fortress.rbac.UserAudit)} </code>
 * <li>Entity Modifications:     <code>List<{@link Mod}>   {@link org.openldap.fortress.AuditMgr#searchAdminMods(org.openldap.fortress.rbac.UserAudit)} </code>
 * </ul>
 * <p/>
 *
 * @author Shawn McKinney
 */
public class AuditMgrRestImpl extends Manageable implements AuditMgr
{
    private static final String CLS_NM = AuditMgrRestImpl.class.getName();

    /**
     * This method returns a list of authorization events for a particular user {@link org.openldap.fortress.rbac.UserAudit#userId}
     * and given timestamp field {@link org.openldap.fortress.rbac.UserAudit#beginDate}.<BR>
     * Method also can discriminate between all events or failed only by setting {@link org.openldap.fortress.rbac.UserAudit#failedOnly}.
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserAudit#userId} - contains the target userId</li>
     * <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * <li>{@link UserAudit#failedOnly} - if set to 'true', return only failed authorization events</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws org.openldap.fortress.SecurityException
     *          if a runtime system error occurs.
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
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.AUDIT_UAUTHZS);
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
     * This method returns a list of authorization events for a particular user {@link org.openldap.fortress.rbac.UserAudit#userId},
     * object {@link org.openldap.fortress.rbac.UserAudit#objName}, and given timestamp field {@link org.openldap.fortress.rbac.UserAudit#beginDate}.<BR>
     * Method also can discriminate between all events or failed only by setting flag {@link org.openldap.fortress.rbac.UserAudit#failedOnly}..
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
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, CLS_NM + ".searchAuthZs");
        List<AuthZ> outRecords;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(uAudit);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.AUDIT_AUTHZS);
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
     * This method returns a list of authentication audit events for a particular user {@link org.openldap.fortress.rbac.UserAudit#userId},
     * and given timestamp field {@link org.openldap.fortress.rbac.UserAudit#beginDate}.<BR>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserAudit#userId} - contains the target userId<</li>
     * <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * <li>{@link UserAudit#failedOnly} - if set to 'true', return only failed authorization events</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Bind.  Each Bind object contains one bind event.
     * @throws org.openldap.fortress.SecurityException
     *          if a runtime system error occurs.
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
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.AUDIT_BINDS);
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
     * This method returns a list of sessions created for a given user {@link org.openldap.fortress.rbac.UserAudit#userId},
     * and timestamp {@link org.openldap.fortress.rbac.UserAudit#beginDate}.<BR>
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
     * @throws org.openldap.fortress.SecurityException
     *          if a runtime system error occurs.
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
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.AUDIT_SESSIONS);
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
     * This method returns a list of admin operations events for a particular entity {@link org.openldap.fortress.rbac.UserAudit#dn},
     * object {@link org.openldap.fortress.rbac.UserAudit#objName} and timestamp {@link org.openldap.fortress.rbac.UserAudit#beginDate}.  If the internal
     * userId {@link org.openldap.fortress.rbac.UserAudit#internalUserId} is set it will limit search by that field.
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserAudit#dn} - contains the LDAP distinguished name for the updated object.  For example if caller
     * wants to find out what changes were made to John Doe's user object this would be 'uid=jdoe,ou=People,dc=example,dc=com'</li>
     * <li>{@link UserAudit#objName} - contains the object (authorization resource) name corresponding to the event.  For example if caller
     * wants to return events where User object was modified, this would be 'updateUser'</li>
     * <li>{@link UserAudit#internalUserId} - maps to the internalUserId of user who changed the record in LDAP.  This maps to {@link org.openldap.fortress.rbac.User#internalId}.</li>
     * <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * <li>{@link UserAudit#endDate} - contains the date in which to end search</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Mod.  Each Mod object in list corresponds to one update or delete event on directory.
     * @throws org.openldap.fortress.SecurityException
     *          if a runtime system error occurs.
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
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.AUDIT_MODS);
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
     * This method returns a list of failed authentication events for a particular invalid user {@link org.openldap.fortress.rbac.UserAudit#userId},
     * and given timestamp {@link org.openldap.fortress.rbac.UserAudit#beginDate}.  If the {@link org.openldap.fortress.rbac.UserAudit#failedOnly} is true it will
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
     * @throws org.openldap.fortress.SecurityException
     *          if a runtime system error occurs.
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
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.AUDIT_INVLD);
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
