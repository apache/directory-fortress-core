/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.audit;

import com.jts.fortress.AuditMgr;
import com.jts.fortress.ReviewMgrFactory;
import com.jts.fortress.SecurityException;
import com.jts.fortress.ReviewMgr;
import com.jts.fortress.arbac.AdminUtil;
import com.jts.fortress.rbac.Session;
import com.jts.fortress.rbac.Permission;
import com.jts.fortress.rbac.User;
import com.jts.fortress.util.attr.VUtil;
import com.jts.fortress.constants.GlobalErrIds;

import java.util.List;

/**
 * This object performs searches across <a href="http://www.openldap.org/">OpenLDAP</a>'s slapd access log.  The access log events are
 * persisted in <a href="http://www.oracle.com/technetwork/database/berkeleydb/overview/index.html">BDB</a> and available for inquiry via common LDAP protocols.
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

 * @author smckinn
 * @created April 2, 2010
 */
public class AuditMgrImpl implements AuditMgr
{
    private static final String OCLS_NM = AuditMgrImpl.class.getName();
    private static final AuditP auditP = new AuditP();
    // thread unsafe variable:
    private Session adminSess;


    /**
     * This method returns a list of authorization events for a particular user {@link com.jts.fortress.audit.UserAudit#userId}
     * and given timestamp field {@link com.jts.fortress.audit.UserAudit#beginDate}.<BR>
     * Method also can discriminate between all events or failed only by setting {@link com.jts.fortress.audit.UserAudit#failedOnly}.
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws com.jts.fortress.SecurityException if a runtime system error occurs.
     */
    public List<AuthZ> getUserAuthZs(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "getUserAuthZs";
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, OCLS_NM + "." + methodName);
        checkAccess(methodName);
        return auditP.getAuthZs(uAudit);
    }


    /**
     * This method returns a list of authorization events for a particular user {@link com.jts.fortress.audit.UserAudit#userId},
     * object {@link com.jts.fortress.audit.UserAudit#objName}, and given timestamp field {@link com.jts.fortress.audit.UserAudit#beginDate}.<BR>
     * Method also can discriminate between all events or failed only by setting flag {@link com.jts.fortress.audit.UserAudit#failedOnly}..
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws SecurityException if a runtime system error occurs.
     */
    public List<AuthZ> searchAuthZs(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchAuthZs";
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, OCLS_NM + "." + methodName);
        checkAccess(methodName);
        return auditP.searchAuthZs(uAudit);
    }


    /**
     * This method returns a list of authentication audit events for a particular user {@link com.jts.fortress.audit.UserAudit#userId},
     * and given timestamp field {@link com.jts.fortress.audit.UserAudit#beginDate}.<BR>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Bind.  Each Bind object contains one bind event.
     * @throws com.jts.fortress.SecurityException if a runtime system error occurs.
     */
    public List<Bind> searchBinds(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchBinds";
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, OCLS_NM + "." + methodName);
        checkAccess(methodName);
        return auditP.searchBinds(uAudit);
    }

    /**
     * This method returns a list of sessions created for a given user {@link com.jts.fortress.audit.UserAudit#userId},
     * and timestamp {@link com.jts.fortress.audit.UserAudit#beginDate}.<BR>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Mod.  Each Mod object in list corresponds to one update or delete event on directory.
     * @throws com.jts.fortress.SecurityException if a runtime system error occurs.
     */
    public List<Mod> searchUserSessions(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchUserSessions";
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, OCLS_NM + "." + methodName);
        checkAccess(methodName);
        return auditP.searchUserMods(uAudit);
    }

    /**
     * This method returns a list of admin operations events for a particular entity {@link com.jts.fortress.audit.UserAudit#dn},
     * object {@link com.jts.fortress.audit.UserAudit#objName} and timestamp {@link com.jts.fortress.audit.UserAudit#beginDate}.  If the internal
     * userId {@link com.jts.fortress.audit.UserAudit#internalUserId} is set it will limit search by that field.
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Mod.  Each Mod object in list corresponds to one update or delete event on directory.
     * @throws com.jts.fortress.SecurityException if a runtime system error occurs.
     */
    public List<Mod> searchAdminMods(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchAdminMods";
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, OCLS_NM + "." + methodName);
        checkAccess(methodName);
        if(VUtil.isNotNullOrEmpty(uAudit.getUserId()))
        {
            ReviewMgr rMgr = ReviewMgrFactory.createInstance();
            User user = rMgr.readUser(new User(uAudit.getUserId()));
            uAudit.setInternalUserId(user.getInternalId());
        }
        return auditP.searchAdminMods(uAudit);
    }


    /**
     * This method returns a list of failed authentication events for a particular invalid user {@link com.jts.fortress.audit.UserAudit#userId},
     * and given timestamp {@link com.jts.fortress.audit.UserAudit#beginDate}.  If the {@link com.jts.fortress.audit.UserAudit#failedOnly} is true it will
     * return only authentication attempts made with invalid userId.
     * </p>
     * This is possible because Fortress performs read on user before the bind.
     * </p>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one failed authentication event.
     * @throws com.jts.fortress.SecurityException if a runtime system error occurs.
     */
    public List<AuthZ> searchInvalidUsers(UserAudit uAudit)
        throws SecurityException
    {
        String methodName = "searchInvalidUsers";
        VUtil.assertNotNull(uAudit, GlobalErrIds.AUDT_INPUT_NULL, OCLS_NM + "." + methodName);
        checkAccess(methodName);
        return auditP.searchInvalidAuthNs(uAudit);
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


    /**
     * perform authorization on behalf of the caller if the {@link AuditMgrImpl#adminSess} is set.
     * @param opName contains operation name.
     * @throws com.jts.fortress.SecurityException in the event of data validation or system error.
     */
    private void checkAccess(String opName) throws SecurityException
    {
        if (this.adminSess != null)
        {
            AdminUtil.checkAccess(adminSess, new Permission(OCLS_NM, opName));
        }
    }
}