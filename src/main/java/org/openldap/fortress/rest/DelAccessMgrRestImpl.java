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

import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.rbac.RolePerm;
import org.openldap.fortress.rbac.UserAdminRole;
import org.openldap.fortress.DelAccessMgr;
import org.openldap.fortress.rbac.Permission;
import org.openldap.fortress.rbac.Session;
import org.openldap.fortress.rbac.User;
import org.openldap.fortress.rbac.UserRole;
import org.openldap.fortress.rbac.Role;
import org.openldap.fortress.util.attr.VUtil;
import org.openldap.fortress.SecurityException;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class implements the ARBAC02 DelAccessMgr interface for performing runtime delegated access control operations on objects that are provisioned Fortress ARBAC entities
 * using HTTP access to En Masse REST server.  These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification describes delegated administrative
 * operations for the creation and maintenance of ARBAC element sets and relations.  Delegated administrative review functions for performing administrative queries
 * and system functions for creating and managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 *
 * This class also extends the RBAC AccessMgrImpl object which is used for performing runtime session creation and
 * access control decisions based on behalf of administrative user who is logged onto the system.  (See the AccessMgr javadoc for more info of how RBAC works).
 *
 * This class provides both sets of functionality as is necessary to fulfill runtime delegated administrative access control functionality
 * within RBAC provisioning systems.
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="../doc-files/ARbac.png">
 * <p/>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without sacrificing regulations for accountability or traceability.
 * <p/>
 * This class is thread safe.
 * <p/>
 * @author Shawn McKinney
 */
public class DelAccessMgrRestImpl extends AccessMgrRestImpl implements DelAccessMgr
{
    private static final String CLS_NM = DelAccessMgrRestImpl.class.getName();

    /**
     * This function will determine if the user contains an AdminRole that is authorized assignment control over
     * User-Role Assignment (URA).  This adheres to the ARBAC02 functional specification for can-assign URA.
     *
     * @param session This object must be instantiated by calling {@link org.openldap.fortress.AccessMgr#createSession(org.openldap.fortress.rbac.User, boolean)} before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param user    Instantiated User entity requires only valid userId attribute set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws org.openldap.fortress.SecurityException In the event of data validation error (i.e. invalid userId or role name) or system error.
     */
    @Override
    public boolean canAssign(Session session, User user, Role role)
        throws SecurityException
    {
        String methodName = CLS_NM + ".canAssign";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, methodName);
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, methodName);
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, methodName);
        boolean result;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        UserRole uRole = new UserRole(user.getUserId(), role.getName());
        request.setSession(session);
        request.setEntity(uRole);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ADMIN_ASSIGN);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            result = response.getAuthorized();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return result;
    }

    /**
     * This function will determine if the user contains an AdminRole that is authorized revoke control over
     * User-Role Assignment (URA).  This adheres to the ARBAC02 functional specification for can-revoke URA.
     *
     * @param session This object must be instantiated by calling {@link org.openldap.fortress.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param user    Instantiated User entity requires only valid userId attribute set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws SecurityException In the event of data validation error (i.e. invalid userId or role name) or system error.
     */
    @Override
    public boolean canDeassign(Session session, User user, Role role)
        throws SecurityException
    {
        String methodName = CLS_NM + ".canDeassign";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, methodName);
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, methodName);
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, methodName);
        boolean result;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        UserRole uRole = new UserRole(user.getUserId(), role.getName());
        request.setSession(session);
        request.setEntity(uRole);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ADMIN_DEASSIGN);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            result = response.getAuthorized();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return result;
    }

    /**
     * This function will determine if the user contains an AdminRole that is authorized assignment control over
     * Permission-Role Assignment (PRA).  This adheres to the ARBAC02 functional specification for can-assign-p PRA.
     *
     * @param session This object must be instantiated by calling {@link org.openldap.fortress.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param perm    Instantiated Permission entity requires valid object name and operation name attributes set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws org.openldap.fortress.SecurityException In the event of data validation error (i.e. invalid perm or role name) or system error.
     */
    @Override
    public boolean canGrant(Session session, Role role, Permission perm)
        throws SecurityException
    {
        String methodName = CLS_NM + "canGrant";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, methodName);
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OBJECT_NULL, methodName);
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, methodName);
        boolean result;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        RolePerm context = new RolePerm();
        context.setPerm(perm);
        context.setRole(role);
        request.setSession(session);
        request.setEntity(context);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ADMIN_GRANT);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            result = response.getAuthorized();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return result;
    }

    /**
     * This function will determine if the user contains an AdminRole that is authorized revoke control over
     * Permission-Role Assignment (PRA).  This adheres to the ARBAC02 functional specification for can-revoke-p PRA.
     *
     * @param session This object must be instantiated by calling {@link org.openldap.fortress.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param perm    Instantiated Permission entity requires valid object name and operation name attributes set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws SecurityException In the event of data validation error (i.e. invalid perm or role name) or system error.
     */
    @Override
    public boolean canRevoke(Session session, Role role, Permission perm)
        throws SecurityException
    {
        String methodName = CLS_NM + "canRevoke";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, methodName);
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OBJECT_NULL, methodName);
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, methodName);
        boolean result;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        RolePerm context = new RolePerm();
        context.setPerm(perm);
        context.setRole(role);
        request.setSession(session);
        request.setEntity(context);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ADMIN_REVOKE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            result = response.getAuthorized();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return result;
    }

    /**
     * This function overrides same in RBAC's AccessMgrImpl, but instead processes permissions contained within AdminPerm dataset.
     * Function returns a Boolean value containing result of a given administrator's access to perform a given operation on a given object.
     * The function is valid if and only if the session is a valid Fortress session, the object is a member of the AdminPerm OBJS data set,
     * and the operation is a member of the AdminPerms OPS data set. The session's subject has the permission
     * to perform the operation on that object if and only if that permission is assigned to (at least)
     * one of the session's active roles. This implementation will verify the roles or userId correspond
     * to the subject's active roles are registered in the object's access control list.
     *
     * @param perm object contains obj attribute which is a String and contains the name of the object user is trying to access;
     * perm object contains operation attribute which is also a String and contains the operation name for the object.
     * @param session This object must be instantiated by calling {@link org.openldap.fortress.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @return True of user has access, false otherwise.
     * @throws SecurityException In the event of data validation error (i.e. invalid perm name) or system error.
     */
    @Override
    public boolean checkAccess(Session session, Permission perm)
        throws SecurityException
    {
        String methodName = CLS_NM + ".checkAccess";
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_NULL, methodName);
        VUtil.assertNotNullOrEmpty(perm.getOpName(), GlobalErrIds.PERM_OPERATION_NULL, methodName);
        VUtil.assertNotNullOrEmpty(perm.getObjName(), GlobalErrIds.PERM_OBJECT_NULL, methodName);
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, methodName);
        boolean result;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setSession(session);
        request.setEntity(perm);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ADMIN_AUTHZ);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            result = response.getAuthorized();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return result;
    }


    /**
     * This function adds an adminRole as an active role of a session whose owner is a given user.
     * <p>
     * The function is valid if and only if:
     * <ul>
     *  <li> the user is a member of the USERS data set
     *  <li> the role is a member of the ADMIN ROLES data set
     *  <li> the session is a valid Fortress session
     *  <li> the user is authorized to that admin role
     *  <li> the session is owned by that user.
     * </ul>
     * </p>
     * @param session object contains the user's returned RBAC and ARBAC sessions from the createSession method.
     * @param role    object contains the adminRole name to be activated into session.
     * @throws org.openldap.fortress.SecurityException is thrown if user is not allowed to activate or runtime error occurs with system.
     */
    @Override
    public void addActiveRole(Session session, UserAdminRole role)
        throws SecurityException
    {
        String methodName = CLS_NM + ".addActiveRole";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, methodName);
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setSession(session);
        request.setEntity(role);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ADMIN_ADD);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This function deactivates adminRole from the active adminRole set of a session owned by a given user.
     * The function is valid if and only if the user is a member of the USERS data set, the
     * session object contains a valid Fortress session, the session is owned by the user,
     * and the adminRole is an active adminRole of that session.
     *
     * @param session object contains the user's returned RBAC and ARBAC sessions from the createSession method.
     * @param role    object contains the adminRole name to be deactivated.
     * @throws org.openldap.fortress.SecurityException is thrown if user is not allowed to deactivate or runtime error occurs with system.
     */
    @Override
    public void dropActiveRole(Session session, UserAdminRole role)
        throws SecurityException
    {
        String methodName = CLS_NM + ".dropActiveRole";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, methodName);
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setSession(session);
        request.setEntity(role);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ADMIN_DROP);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This function returns the active admin roles associated with a session. The function is valid if
     * and only if the session is a valid Fortress session.
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @return List<UserAdminRole> containing all adminRoles active in user's session.  This will NOT contain inherited roles.
     * @throws SecurityException is thrown if session invalid or system. error.
     */
    @Override
    public List<UserAdminRole> sessionAdminRoles(Session session)
        throws SecurityException
    {
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, CLS_NM + ".sessionAdminRoles");
        List<UserAdminRole> roles;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setSession(session);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ADMIN_ROLES);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            roles = response.getEntities();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return roles;
    }

    /**
     * This function returns the authorized admin roles associated with a session based on hierarchical relationships. The function is valid if
     * and only if the session is a valid Fortress session.
     *
     * @param session object contains the user's returned ARBAC session from the createSession method.
     * @return Set<String> containing all adminRoles authorized in user's session.  This will contain inherited roles.
     * @throws SecurityException is thrown if session invalid or system. error.
     */
    @Override
    public Set<String> authorizedAdminRoles(Session session)
        throws SecurityException
    {
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, CLS_NM + ".authorizedAdminRoles");
        Set<String> retRoleNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setSession(session);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ADMIN_AUTHZ_ROLES);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            Set<String> tempNames = response.getValueSet();
            // This is done to use a case insensitive TreeSet for returned names.
            retRoleNames.addAll(tempNames);
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retRoleNames;
        //throw new java.lang.UnsupportedOperationException();
    }

    /**
     * This function returns the ARBAC (administrative) permissions of the session, i.e., the permissions assigned
     * to its authorized admin roles. The function is valid if and only if the session is a valid Fortress session.
     *
     * @param session object contains the user's returned ARBAC session from the createSession method.
     * @return List<Permission> containing admin permissions (op, obj) active for user's session.
     * @throws SecurityException in the event runtime error occurs with system.
     */
    @Override
    public List<Permission> sessionPermissions(Session session)
        throws SecurityException
    {
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, CLS_NM + ".sessionPermissions");
        List<Permission> retPerms;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setSession(session);
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ADMIN_PERMS);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retPerms = response.getEntities();
            Session outSession = response.getSession();
            session.copy(outSession);
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retPerms;
        //throw new java.lang.UnsupportedOperationException();
    }
}