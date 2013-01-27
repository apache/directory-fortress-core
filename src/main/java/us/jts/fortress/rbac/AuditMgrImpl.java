/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import us.jts.fortress.AuditMgr;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.ReviewMgrFactory;
import us.jts.fortress.SecurityException;
import us.jts.fortress.ReviewMgr;
import us.jts.fortress.util.attr.VUtil;

import java.util.List;

/**
 * This object performs searches across <a href="http://www.openldap.org/">OpenLDAP</a>'s slapd access log.  The access log events are
 * persisted in <a href="http://www.oracle.com/technetwork/database/berkeleydb/overview/index.html">BDB</a> and available for inquiry via common LDAP protocols.
 * Audit entries stored on behalf of Fortress operations correspond to runtime authentication {@link Bind}, authorization {@link AuthZ} and modification {@link Mod}
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
 * All events include Fortress context, see {@link us.jts.fortress.rbac.FortEntity}.
 * <p/>
 * <h4>
 * The following APIs generate events subsequently stored in this access log:
 * </h4>
 * <ul>
 * <li> {@link us.jts.fortress.AccessMgr}
 * <li> {@link us.jts.fortress.AdminMgr}
 * <li> {@link us.jts.fortress.AdminMgr}
 * <li> {@link us.jts.fortress.DelAdminMgr}
 * <li> {@link us.jts.fortress.cfg.ConfigMgr}
 * <li> {@link us.jts.fortress.PwPolicyMgr}
 * </ul>
 * <h4>
 * The following reports are supported using search input: {@link UserAudit}
 * </h4>
 * <ul>
 * <li>User Authentications:     <code>List<{@link Bind}>  {@link us.jts.fortress.AuditMgr#searchBinds(UserAudit)}</code>
 * <li>Invalid Users AuthN:      <code>List<{@link Bind}>  {@link us.jts.fortress.AuditMgr#searchInvalidUsers(UserAudit)} </code>
 * <li>User Authorizations 1:    <code>List<{@link AuthZ}> {@link us.jts.fortress.AuditMgr#getUserAuthZs(UserAudit)} </code>
 * <li>User Authorizations 2:    <code>List<{@link AuthZ}> {@link us.jts.fortress.AuditMgr#searchAuthZs(UserAudit)} </code>
 * <li>User Session Activations: <code>List<{@link Mod}>   {@link us.jts.fortress.AuditMgr#searchUserSessions(UserAudit)} </code>
 * <li>Entity Modifications:     <code>List<{@link Mod}>   {@link us.jts.fortress.AuditMgr#searchAdminMods(UserAudit)} </code>
 * </ul>
 * <p/>
 * This class is NOT thread safe if parent instance variables ({@link #contextId} or {@link #adminSess}) are set.
 *
 * @author Shawn McKinney
 */
public class AuditMgrImpl extends Manageable implements AuditMgr
{
    private static final String CLS_NM = AuditMgrImpl.class.getName();
    private static final AuditP auditP = new AuditP();

    // package private constructor ensures outside classes cannot use:
    AuditMgrImpl()
    {}

    /**
     * This method returns a list of authorization events for a particular user {@link UserAudit#userId}
     * and given timestamp field {@link UserAudit#beginDate}.<BR>
     * Method also can discriminate between all events or failed only by setting {@link UserAudit#failedOnly}.
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserAudit#userId} - contains the target userId</li>
     * <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * <li>{@link UserAudit#failedOnly} - if set to 'true', return only failed authorization events</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws us.jts.fortress.SecurityException
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
     * @throws us.jts.fortress.SecurityException
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
     * @throws us.jts.fortress.SecurityException
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
     * <li>{@link UserAudit#internalUserId} - maps to the internalUserId of user who changed the record in LDAP.  This maps to {@link us.jts.fortress.rbac.User#internalId}.</li>
     * <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * <li>{@link UserAudit#endDate} - contains the date in which to end search</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Mod.  Each Mod object in list corresponds to one update or delete event on directory.
     * @throws us.jts.fortress.SecurityException
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
     * @throws us.jts.fortress.SecurityException
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