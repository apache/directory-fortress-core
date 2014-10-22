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


import java.util.List;

import org.apache.directory.fortress.core.AccelMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.rbac.dao.AcceleratorDAO;
import org.apache.directory.fortress.core.util.attr.VUtil;


/**
 * Implementation class that performs runtime access control operations on data objects of type Fortress entities
 * This class performs runtime access control operations on objects that are provisioned RBAC entities
 * that reside in LDAP directory.  These APIs map directly to similar named APIs specified by ANSI and NIST
 * RBAC system functions.
 * Many of the java doc function descriptions found below were taken directly from ANSI INCITS 359-2004.
 * The RBAC Functional specification describes administrative operations for the creation
 * and maintenance of RBAC element sets and relations; administrative review functions for
 * performing administrative queries; and system functions for creating and managing
 * RBAC attributes on user sessions and making access control decisions.
 * <p/>
 * <hr>
 * <h4>RBAC0 - Core</h4>
 * Many-to-many relationship between Users, Roles and Permissions. Selective role activation into sessions.  API to add, update, delete identity data and perform identity and access control decisions during runtime operations.
 * <p/>
 * <img src="../doc-files/RbacCore.png">
 * <hr>
 * <h4>RBAC1 - General Hierarchical Roles</h4>
 * Simplifies role engineering tasks using inheritance of one or more parent roles.
 * <p/>
 * <img src="../doc-files/RbacHier.png">
 * <hr>
 * <h4>RBAC2 - Static Separation of Duty (SSD) Relations</h4>
 * Enforce mutual membership exclusions across role assignments.  Facilitate dual control policies by restricting which roles may be assigned to users in combination.  SSD provide added granularity for authorization limits which help enterprises meet strict compliance regulations.
 * <p/>
 * <img src="../doc-files/RbacSSD.png">
 * <hr>
 * <h4>RBAC3 - Dynamic Separation of Duty (DSD) Relations</h4>
 * Control allowed role combinations to be activated within an RBAC session.  DSD policies fine tune role policies that facilitate authorization dual control and two man policy restrictions during runtime security checks.
 * <p/>
 * <img src="../doc-files/RbacDSD.png">
 * <hr>
 * <p/>
 * This class is NOT thread safe if parent instance variables ({@link #contextId} or {@link #adminSess}) are set.
 * <p/>
 *
 * @author Shawn McKinney
 */
public class AccelMgrImpl extends Manageable implements AccelMgr
{
    private static final String CLS_NM = AccessMgrImpl.class.getName();
    private static final AcceleratorDAO aDao = new org.apache.directory.fortress.core.rbac.dao.apache.AcceleratorDAO();


    /**
     * package private constructor ensures outside classes must use factory: {@link org.apache.directory.fortress.core.AccelMgrFactory}
     */
    public AccelMgrImpl()
    {
    }


    /**
     * Perform user authentication {@link org.apache.directory.fortress.core.rbac.User#password} and role activations.<br />
     * This method must be called once per user prior to calling other methods within this class.
     * The successful result is {@link org.apache.directory.fortress.core.rbac.Session} that contains target user's RBAC {@link org.apache.directory.fortress.core.rbac.User#roles} and Admin role {@link org.apache.directory.fortress.core.rbac.User#adminRoles}.<br />
     * In addition to checking user password validity it will apply configured password policy checks {@link org.apache.directory.fortress.core.rbac.User#pwPolicy}..<br />
     * Method may also store parms passed in for audit trail {@link org.apache.directory.fortress.core.rbac.FortEntity}.
     * <h4> This API will...</h4>
     * <ul>
     * <li> authenticate user password if trusted == false.
     * <li> perform <a href="http://www.openldap.org/">OpenLDAP</a> <a href="http://tools.ietf.org/html/draft-behera-ldap-password-policy-10">password policy evaluation</a>, see {@link org.apache.directory.fortress.core.ldap.openldap.OLPWControlImpl}.
     * <li> fail for any user who is locked by OpenLDAP's policies {@link org.apache.directory.fortress.core.rbac.User#isLocked()}, regardless of trusted flag being set as parm on API.
     * <li> evaluate temporal {@link org.apache.directory.fortress.core.util.time.Constraint}(s) on {@link org.apache.directory.fortress.core.rbac.User}, {@link org.apache.directory.fortress.core.rbac.UserRole} and {@link UserAdminRole} entities.
     * <li> process selective role activations into User RBAC Session {@link org.apache.directory.fortress.core.rbac.User#roles}.
     * <li> check Dynamic Separation of Duties {@link org.apache.directory.fortress.core.rbac.DSDChecker#validate(org.apache.directory.fortress.core.rbac.Session, org.apache.directory.fortress.core.util.time.Constraint, org.apache.directory.fortress.core.util.time.Time)} on {@link org.apache.directory.fortress.core.rbac.User#roles}.
     * <li> process selective administrative role activations {@link org.apache.directory.fortress.core.rbac.User#adminRoles}.
     * <li> return a {@link org.apache.directory.fortress.core.rbac.Session} that contains a reference to an object stored on the RBAC server..
     * <li> throw a checked exception that will be {@link org.apache.directory.fortress.core.SecurityException} or its derivation.
     * <li> throw a {@link SecurityException} for system failures.
     * <li> throw a {@link org.apache.directory.fortress.core.PasswordException} for authentication and password policy violations.
     * <li> throw a {@link org.apache.directory.fortress.core.ValidationException} for data validation errors.
     * <li> throw a {@link org.apache.directory.fortress.core.FinderException} if User id not found.
     * </ul>
     * <h4>
     * The function is valid if and only if:
     * </h4>
     * <ul>
     * <li> the user is a member of the USERS data set
     * <li> the password is supplied (unless trusted).
     * <li> the (optional) active role set is a subset of the roles authorized for that user.
     * </ul>
     * <h4>
     * The following attributes may be set when calling this method
     * </h4>
     * <ul>
     * <li> {@link org.apache.directory.fortress.core.rbac.User#userId} - required
     * <li> {@link org.apache.directory.fortress.core.rbac.User#password}
     * <li> {@link org.apache.directory.fortress.core.rbac.User#roles} contains a list of RBAC role names authorized for user and targeted for activation within this session.  Default is all authorized RBAC roles will be activated into this Session.
     * <li> {@link org.apache.directory.fortress.core.rbac.User#adminRoles} contains a list of Admin role names authorized for user and targeted for activation.  Default is all authorized ARBAC roles will be activated into this Session.
     * <li> {@link org.apache.directory.fortress.core.rbac.User#props} collection of name value pairs collected on behalf of User during signon.  For example hostname:myservername or ip:192.168.1.99
     * </ul>
     * <h4>
     * Notes:
     * </h4>
     * <ul>
     * <li> roles that violate Dynamic Separation of Duty Relationships will not be activated into session.
     * <li> role activations will proceed in same order as supplied to User entity setter, see {@link org.apache.directory.fortress.core.rbac.User#setRole(String)}.
     * </ul>
     * </p>
     *
     * @param user Contains {@link org.apache.directory.fortress.core.rbac.User#userId}, {@link org.apache.directory.fortress.core.rbac.User#password} (optional if {@code isTrusted} is 'true'), optional {@link org.apache.directory.fortress.core.rbac.User#roles}, optional {@link org.apache.directory.fortress.core.rbac.User#adminRoles}
     * @param isTrusted if true password is not required.
     * @return Session object will contain authentication result code {@link org.apache.directory.fortress.core.rbac.Session#errorId},
     * @throws SecurityException in the event of data validation failure, security policy violation or DAO error.
     */
    @Override
    public Session createSession( User user, boolean isTrusted )
        throws SecurityException
    {
        String methodName = "createSession";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );
        return aDao.createSession( user );
    }


    /**
     * This function requests the RBAC server to delete the session from cache.
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @throws SecurityException in the event runtime error occurs with system.
     */
    @Override
    public void deleteSession( Session session )
        throws SecurityException
    {
        String methodName = "deleteSession";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        aDao.deleteSession( session );
    }


    /**
     * This function returns the active roles associated with a session. The function is valid if
     * and only if the session is a valid Fortress session.
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @return List<UserRole> containing all roles active in user's session.  This will NOT contain inherited roles.
     * @throws SecurityException is thrown if session invalid or system. error.
     */
    public List<UserRole> sessionRoles(Session session)
        throws SecurityException
    {
        String methodName = "sessionRoles";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        return aDao.sessionRoles( session );
    }


    /**
     * Perform user rbac authorization.  This function returns a Boolean value meaning whether the subject of a given session is
     * allowed or not to perform a given operation on a given object. The function is valid if and
     * only if the session is a valid Fortress session, the object is a member of the OBJS data set,
     * and the operation is a member of the OPS data set. The session's subject has the permission
     * to perform the operation on that object if and only if that permission is assigned to (at least)
     * one of the session's active roles. This implementation will verify the roles or userId correspond
     * to the subject's active roles are registered in the object's access control list.
     *
     * @param perm  must contain the object, {@link org.apache.directory.fortress.core.rbac.Permission#objName}, and operation, {@link org.apache.directory.fortress.core.rbac.Permission#opName}, of permission User is trying to access.
     * @param session This object must be instantiated by calling {@link AccessMgrImpl#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @return True if user has access, false otherwise.
     * @throws SecurityException in the event of data validation failure, security policy violation or DAO error.
     */
    @Override
    public boolean checkAccess( Session session, Permission perm )
        throws SecurityException
    {
        String methodName = "checkAccess";
        assertContext( CLS_NM, methodName, perm, GlobalErrIds.PERM_NULL );
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        VUtil.assertNotNullOrEmpty( perm.getOpName(), GlobalErrIds.PERM_OPERATION_NULL, getFullMethodName( CLS_NM,
            methodName ) );
        VUtil.assertNotNullOrEmpty( perm.getObjName(), GlobalErrIds.PERM_OBJECT_NULL, getFullMethodName( CLS_NM,
            methodName ) );
        return aDao.checkAccess( session, perm );
    }


    /**
     * This function returns the permissions of the session, i.e., the permissions assigned
     * to its authorized roles. The function is valid if and only if the session is a valid Fortress session.
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @return List<Permission> containing permissions (op, obj) active for user's session.
     * @throws SecurityException in the event runtime error occurs with system.
     */
    @Override
    public List<Permission> sessionPermissions( Session session )
        throws SecurityException
    {
        throw new java.lang.UnsupportedOperationException();
    }


    /**
     * This function adds a role as an active role of a session whose owner is a given user.
     * <p>
     * The function is valid if and only if:
     * <ul>
     * <li> the user is a member of the USERS data set
     * <li> the role is a member of the ROLES data set
     * <li> the role inclusion does not violate Dynamic Separation of Duty Relationships
     * <li> the session is a valid Fortress session
     * <li> the user is authorized to that role
     * <li> the session is owned by that user.
     * </ul>
     * </p>
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @param role object contains the role name, {@link UserRole#name}, to be activated into session.
     * @throws SecurityException is thrown if user is not allowed to activate or runtime error occurs with system.
     */
    @Override
    public void addActiveRole( Session session, UserRole role )
        throws SecurityException
    {
        String methodName = "addActiveRole";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        VUtil.assertNotNullOrEmpty( role.getUserId(), GlobalErrIds.USER_ID_NULL,
            getFullMethodName( CLS_NM, methodName ) );
        VUtil.assertNotNullOrEmpty( role.getName(), GlobalErrIds.ROLE_NM_NULL, getFullMethodName( CLS_NM,
            methodName ) );
        aDao.addActiveRole( session, role );
    }


    /**
     * This function deletes a role from the active role set of a session owned by a given user.
     * The function is valid if and only if the user is a member of the USERS data set, the
     * session object contains a valid Fortress session, the session is owned by the user,
     * and the role is an active role of that session.
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @param role object contains the role name, {@link UserRole#name}, to be deactivated.
     * @throws SecurityException is thrown if user is not allowed to deactivate or runtime error occurs with system.
     */
    @Override
    public void dropActiveRole( Session session, UserRole role )
        throws SecurityException
    {
        String methodName = "dropActiveRole";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        assertContext( CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL );
        VUtil.assertNotNullOrEmpty( role.getUserId(), GlobalErrIds.USER_ID_NULL,
            getFullMethodName( CLS_NM, methodName ) );
        VUtil.assertNotNullOrEmpty( role.getName(), GlobalErrIds.ROLE_NM_NULL, getFullMethodName( CLS_NM,
            methodName ) );
        aDao.dropActiveRole( session, role );
    }
}