/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rest;

import com.jts.fortress.AuditMgr;
import com.jts.fortress.SecurityException;
import com.jts.fortress.audit.AuthZ;
import com.jts.fortress.audit.Bind;
import com.jts.fortress.audit.Mod;
import com.jts.fortress.audit.UserAudit;
import com.jts.fortress.rbac.Session;
import com.jts.fortress.util.attr.VUtil;
import com.jts.fortress.constants.GlobalErrIds;

import java.util.ArrayList;
import java.util.List;

/**
 * This object performs searches across <a href="http://www.openldap.org/">OpenLDAP</a>'s slapd access log using HTTP access to En Masse REST server.
 * The access log events are
 * persisted in <a href="http://www.oracle.com/technetwork/database/berkeleydb/overview/index.html">BDB</a>.
 * Audit entries stored on behalf of Fortress operations correspond to runtime authentication {@link com.jts.fortress.audit.Bind}, authorization {@link com.jts.fortress.audit.AuthZ} and modification {@link Mod}
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
 * <img src="../../../../images/Audit.png">
 * <p/>
 * All events include Fortress context, see {@link com.jts.fortress.FortEntity}.
 * <p/>
 * <h4>
 * The following APIs generate events subsequently stored in this access log:
 * </h4>
 * <ul>
 * <li> {@link com.jts.fortress.AccessMgr}
 * <li> {@link com.jts.fortress.AdminMgr}
 * <li> {@link com.jts.fortress.AdminMgr}
 * <li> {@link com.jts.fortress.DelegatedAdminMgr}
 * <li> {@link com.jts.fortress.configuration.ConfigMgr}
 * <li> {@link com.jts.fortress.PswdPolicyMgr}
 * </ul>
 * <h4>
 * The following reports are supported using search input: {@link com.jts.fortress.audit.UserAudit}
 * </h4>
 * <ul>
 * <li>User Authentications:     <code>List<{@link com.jts.fortress.audit.Bind}>  {@link com.jts.fortress.AuditMgr#searchBinds(com.jts.fortress.audit.UserAudit)}</code>
 * <li>Invalid Users AuthN:      <code>List<{@link com.jts.fortress.audit.Bind}>  {@link com.jts.fortress.AuditMgr#searchInvalidUsers(com.jts.fortress.audit.UserAudit)} </code>
 * <li>User Authorizations 1:    <code>List<{@link com.jts.fortress.audit.AuthZ}> {@link com.jts.fortress.AuditMgr#getUserAuthZs(com.jts.fortress.audit.UserAudit)} </code>
 * <li>User Authorizations 2:    <code>List<{@link com.jts.fortress.audit.AuthZ}> {@link com.jts.fortress.AuditMgr#searchAuthZs(com.jts.fortress.audit.UserAudit)} </code>
 * <li>User Session Activations: <code>List<{@link Mod}>   {@link com.jts.fortress.AuditMgr#searchUserSessions(com.jts.fortress.audit.UserAudit)} </code>
 * <li>Entity Modifications:     <code>List<{@link Mod}>   {@link com.jts.fortress.AuditMgr#searchAdminMods(com.jts.fortress.audit.UserAudit)} </code>
 * </ul>
 * <p/>
 *
 * @author Shawn McKinney
 * @created February 13, 2012
 */
public class AuditMgrRestImpl implements AuditMgr
{
    private static final String CLS_NM = AuditMgrRestImpl.class.getName();
    // thread unsafe variable:
    private Session adminSess;


    /**
     * This method returns a list of authorization events for a particular user {@link com.jts.fortress.audit.UserAudit#userId}
     * and given timestamp field {@link com.jts.fortress.audit.UserAudit#beginDate}.<BR>
     * Method also can discriminate between all events or failed only by setting {@link com.jts.fortress.audit.UserAudit#failedOnly}.
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserAudit#userId} - contains the target userId</li>
     * <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * <li>{@link UserAudit#failedOnly} - if set to 'true', return only failed authorization events</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws com.jts.fortress.SecurityException
     *          if a runtime system error occurs.
     */
    public List<AuthZ> getUserAuthZs(UserAudit uAudit)
        throws SecurityException
    {
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, CLS_NM + ".getUserAuthZs");
        List<AuthZ> outRecords;
        FortRequest request = new FortRequest();
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
                outRecords = new ArrayList<AuthZ>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return outRecords;
    }


    /**
     * This method returns a list of authorization events for a particular user {@link com.jts.fortress.audit.UserAudit#userId},
     * object {@link com.jts.fortress.audit.UserAudit#objName}, and given timestamp field {@link com.jts.fortress.audit.UserAudit#beginDate}.<BR>
     * Method also can discriminate between all events or failed only by setting flag {@link com.jts.fortress.audit.UserAudit#failedOnly}..
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
    public List<AuthZ> searchAuthZs(UserAudit uAudit)
        throws SecurityException
    {
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, CLS_NM + ".searchAuthZs");
        List<AuthZ> outRecords;
        FortRequest request = new FortRequest();
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
                outRecords = new ArrayList<AuthZ>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return outRecords;
    }


    /**
     * This method returns a list of authentication audit events for a particular user {@link com.jts.fortress.audit.UserAudit#userId},
     * and given timestamp field {@link com.jts.fortress.audit.UserAudit#beginDate}.<BR>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserAudit#userId} - contains the target userId<</li>
     * <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * <li>{@link UserAudit#failedOnly} - if set to 'true', return only failed authorization events</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Bind.  Each Bind object contains one bind event.
     * @throws com.jts.fortress.SecurityException
     *          if a runtime system error occurs.
     */
    public List<Bind> searchBinds(UserAudit uAudit)
        throws SecurityException
    {
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, CLS_NM + ".searchBinds");
        List<Bind> outRecords;
        FortRequest request = new FortRequest();
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
                outRecords = new ArrayList<Bind>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return outRecords;
    }

    /**
     * This method returns a list of sessions created for a given user {@link com.jts.fortress.audit.UserAudit#userId},
     * and timestamp {@link com.jts.fortress.audit.UserAudit#beginDate}.<BR>
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
     * @throws com.jts.fortress.SecurityException
     *          if a runtime system error occurs.
     */
    public List<Mod> searchUserSessions(UserAudit uAudit)
        throws SecurityException
    {
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, CLS_NM + ".searchUserSessions");
        List<Mod> outRecords;
        FortRequest request = new FortRequest();
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
                outRecords = new ArrayList<Mod>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return outRecords;
    }

    /**
     * This method returns a list of admin operations events for a particular entity {@link com.jts.fortress.audit.UserAudit#dn},
     * object {@link com.jts.fortress.audit.UserAudit#objName} and timestamp {@link com.jts.fortress.audit.UserAudit#beginDate}.  If the internal
     * userId {@link com.jts.fortress.audit.UserAudit#internalUserId} is set it will limit search by that field.
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserAudit#dn} - contains the LDAP distinguished name for the updated object.  For example if caller
     * wants to find out what changes were made to John Doe's user object this would be 'uid=jdoe,ou=People,dc=example,dc=com'</li>
     * <li>{@link UserAudit#objName} - contains the object (authorization resource) name corresponding to the event.  For example if caller
     * wants to return events where User object was modified, this would be 'updateUser'</li>
     * <li>{@link UserAudit#internalUserId} - maps to the internalUserId of user who changed the record in LDAP.  This maps to {@link com.jts.fortress.rbac.User#internalId}.</li>
     * <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * <li>{@link UserAudit#endDate} - contains the date in which to end search</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Mod.  Each Mod object in list corresponds to one update or delete event on directory.
     * @throws com.jts.fortress.SecurityException
     *          if a runtime system error occurs.
     */
    public List<Mod> searchAdminMods(UserAudit uAudit)
        throws SecurityException
    {
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, CLS_NM + ".searchAdminMods");
        List<Mod> outRecords;
        FortRequest request = new FortRequest();
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
                outRecords = new ArrayList<Mod>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return outRecords;
    }


    /**
     * This method returns a list of failed authentication events for a particular invalid user {@link com.jts.fortress.audit.UserAudit#userId},
     * and given timestamp {@link com.jts.fortress.audit.UserAudit#beginDate}.  If the {@link com.jts.fortress.audit.UserAudit#failedOnly} is true it will
     * return only authentication attempts made with invalid userId.
     * </p>
     * This is possible because Fortress performs read on user before the bind.
     * </p>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserAudit#userId} - contains the target userId<</li>
     * <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * <li>{@link UserAudit#failedOnly} - if set to 'true', return only failed authorization events</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one failed authentication event.
     * @throws com.jts.fortress.SecurityException
     *          if a runtime system error occurs.
     */
    public List<AuthZ> searchInvalidUsers(UserAudit uAudit)
        throws SecurityException
    {
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, CLS_NM + ".searchInvalidUsers");
        List<AuthZ> outRecords;
        FortRequest request = new FortRequest();
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
                outRecords = new ArrayList<AuthZ>();
            }
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return outRecords;
    }


    /**
     * Setting Session into this object will enforce ARBAC controls and render this class
     * thread unsafe..
     *
     * @param session
     */
    public void setAdmin(Session session)
    {
        this.adminSess = session;
    }
}
