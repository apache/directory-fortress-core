/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;


import java.util.List;
import java.util.Set;

import us.jts.fortress.AccessMgr;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.SecurityException;
import us.jts.fortress.util.attr.VUtil;
import us.jts.fortress.util.time.CUtil;


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
public class AccessMgrImpl extends Manageable implements AccessMgr
{
    private static final String CLS_NM = AccessMgrImpl.class.getName();
    private static final UserP userP = new UserP();
    private static final PermP permP = new PermP();


    // package private constructor ensures outside classes cannot use:
    public AccessMgrImpl()
    {
    }


    /**
     * Perform user authentication only.  It does not activate RBAC roles in session but will evaluate
     * password policies.
     *
     * @param userId   Contains the userid of the user signing on.
     * @param password Contains the user's password.
     * @return Session object will be returned if authentication successful.  This will not contain user's roles.
     * @throws SecurityException in the event of data validation failure, security policy violation or DAO error.
     */
    @Override
    public Session authenticate( String userId, char[] password )
        throws SecurityException
    {
        String methodName = "authenticate";
        VUtil.assertNotNullOrEmpty( userId, GlobalErrIds.USER_ID_NULL, getFullMethodName( CLS_NM, methodName ) );
        VUtil.assertNotNullOrEmpty( password, GlobalErrIds.USER_PW_NULL, getFullMethodName( CLS_NM, methodName ) );
        User inUser = new User( userId );
        inUser.setContextId( contextId );
        // false tells the User Read not to fetch roles.
        User user = userP.read( inUser, false );
        user.setPassword( password );
        user.setContextId( contextId );
        Session ftSess = userP.authenticate( user );
        ftSess.setUser( user );

        return ftSess;
    }


    /**
     * Perform user authentication {@link User#password} and role activations.<br />
     * This method must be called once per user prior to calling other methods within this class.
     * The successful result is {@link Session} that contains target user's RBAC {@link User#roles} and Admin role {@link User#adminRoles}.<br />
     * In addition to checking user password validity it will apply configured password policy checks {@link User#pwPolicy}..<br />
     * Method may also store parms passed in for audit trail {@link FortEntity}.
     * <h4> This API will...</h4>
     * <ul>
     * <li> authenticate user password if trusted == false.
     * <li> perform <a href="http://www.openldap.org/">OpenLDAP</a> <a href="http://tools.ietf.org/html/draft-behera-ldap-password-policy-10">password policy evaluation</a>, see {@link us.jts.fortress.ldap.openldap.OLPWControlImpl}.
     * <li> fail for any user who is locked by OpenLDAP's policies {@link User#isLocked()}, regardless of trusted flag being set as parm on API.
     * <li> evaluate temporal {@link us.jts.fortress.util.time.Constraint}(s) on {@link User}, {@link UserRole} and {@link UserAdminRole} entities.
     * <li> process selective role activations into User RBAC Session {@link User#roles}.
     * <li> check Dynamic Separation of Duties {@link DSDChecker#validate(Session, us.jts.fortress.util.time.Constraint, us.jts.fortress.util.time.Time)} on {@link User#roles}.
     * <li> process selective administrative role activations {@link User#adminRoles}.
     * <li> return a {@link Session} containing {@link Session#getUser()}, {@link Session#getRoles()} and (if admin user) {@link Session#getAdminRoles()} if everything checks out good.
     * <li> throw a checked exception that will be {@link us.jts.fortress.SecurityException} or its derivation.
     * <li> throw a {@link SecurityException} for system failures.
     * <li> throw a {@link us.jts.fortress.PasswordException} for authentication and password policy violations.
     * <li> throw a {@link us.jts.fortress.ValidationException} for data validation errors.
     * <li> throw a {@link us.jts.fortress.FinderException} if User id not found.
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
     * <li> {@link User#userId} - required
     * <li> {@link User#password}
     * <li> {@link User#roles} contains a list of RBAC role names authorized for user and targeted for activation within this session.  Default is all authorized RBAC roles will be activated into this Session.
     * <li> {@link User#adminRoles} contains a list of Admin role names authorized for user and targeted for activation.  Default is all authorized ARBAC roles will be activated into this Session.
     * <li> {@link User#props} collection of name value pairs collected on behalf of User during signon.  For example hostname:myservername or ip:192.168.1.99
     * </ul>
     * <h4>
     * Notes:
     * </h4>
     * <ul>
     * <li> roles that violate Dynamic Separation of Duty Relationships will not be activated into session.
     * <li> role activations will proceed in same order as supplied to User entity setter, see {@link User#setRole(String)}.
     * </ul>
     * </p>
     *
     * @param user Contains {@link User#userId}, {@link User#password} (optional if {@code isTrusted} is 'true'), optional {@link User#roles}, optional {@link User#adminRoles}
     * @param isTrusted if true password is not required.
     * @return Session object will contain authentication result code {@link Session#errorId}, RBAC role activations {@link Session#getRoles()}, Admin Role activations {@link Session#getAdminRoles()},OpenLDAP pw policy codes {@link Session#warningId}, {@link Session#expirationSeconds}, {@link Session#graceLogins} and more.
     * @throws SecurityException in the event of data validation failure, security policy violation or DAO error.
     */
    @Override
    public Session createSession( User user, boolean isTrusted )
        throws SecurityException
    {
        String methodName = "createSession";
        assertContext( CLS_NM, methodName, user, GlobalErrIds.USER_NULL );

        return userP.createSession( user, isTrusted );
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
     * @param perm  must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, of permission User is trying to access.
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
        VUtil.assertNotNullOrEmpty( perm.getOpName(), GlobalErrIds.PERM_OPERATION_NULL,
            getFullMethodName( CLS_NM, methodName ) );
        VUtil.assertNotNullOrEmpty( perm.getObjectName(), GlobalErrIds.PERM_OBJECT_NULL,
            getFullMethodName( CLS_NM, methodName ) );
        CUtil.validateConstraints( session, CUtil.ConstraintType.USER, false );
        CUtil.validateConstraints( session, CUtil.ConstraintType.ROLE, false );
        return permP.checkPermission( session, perm );
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
        String methodName = "sessionPermissions";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        CUtil.validateConstraints( session, CUtil.ConstraintType.USER, false );
        CUtil.validateConstraints( session, CUtil.ConstraintType.ROLE, false );
        return permP.search( session );
    }


    /**
     * This function returns the active roles associated with a session. The function is valid if
     * and only if the session is a valid Fortress session.
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @return List<UserRole> containing all roles active in user's session.  This will NOT contain inherited roles.
     * @throws SecurityException
     *          is thrown if session invalid or system. error.
     */
    @Override
    public List<UserRole> sessionRoles( Session session )
        throws SecurityException
    {
        String methodName = "sessionRoles";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        CUtil.validateConstraints( session, CUtil.ConstraintType.USER, false );
        CUtil.validateConstraints( session, CUtil.ConstraintType.ROLE, false );
        return session.getRoles();
    }


    /**
     * This function returns the authorized roles associated with a session based on hierarchical relationships. The function is valid if
     * and only if the session is a valid Fortress session.
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @return Set<String> containing all roles active in user's session.  This will contain inherited roles.
     * @throws SecurityException is thrown if session invalid or system. error.
     */
    @Override
    public Set<String> authorizedRoles( Session session )
        throws SecurityException
    {
        String methodName = "authorizedRoles";
        assertContext( CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL );
        VUtil.assertNotNull( session.getUser(), GlobalErrIds.USER_NULL, CLS_NM + ".authorizedRoles" );
        CUtil.validateConstraints( session, CUtil.ConstraintType.USER, false );
        CUtil.validateConstraints( session, CUtil.ConstraintType.ROLE, false );
        return RoleUtil.getInheritedRoles( session.getRoles(), this.contextId );
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
        role.setUserId( session.getUserId() );
        List<UserRole> uRoles;
        List<UserRole> sRoles = session.getRoles();
        // If session already has role activated log an error and throw an exception:
        if ( sRoles != null && sRoles.contains( role ) )
        {
            String info = getFullMethodName( CLS_NM, methodName ) + " User [" + session.getUserId() + "] Role ["
                + role.getName() + "] role already activated.";
            throw new SecurityException( GlobalErrIds.URLE_ALREADY_ACTIVE, info );
        }

        User inUser = new User( session.getUserId() );
        inUser.setContextId( this.contextId );
        User ue = userP.read( inUser, true );
        uRoles = ue.getRoles();
        int indx;
        // Is the role activation target valid for this user?
        if ( !VUtil.isNotNullOrEmpty( uRoles ) || ( ( indx = uRoles.indexOf( role ) ) == -1 ) )
        {
            String info = getFullMethodName( CLS_NM, methodName ) + " Role [" + role.getName() + "] User ["
                + session.getUserId() + "] role not authorized for user.";
            throw new SecurityException( GlobalErrIds.URLE_ACTIVATE_FAILED, info );
        }
        // validate Dynamic Separation of Duty Relations:
        SDUtil.validateDSD( session, role );
        // now activate the role to the session:
        session.setRole( uRoles.get( indx ) );
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
        role.setUserId( session.getUserId() );
        List<UserRole> roles = session.getRoles();
        VUtil
            .assertNotNull( roles, GlobalErrIds.URLE_DEACTIVE_FAILED, CLS_NM + getFullMethodName( CLS_NM, methodName ) );
        int indx = roles.indexOf( role );
        if ( indx != -1 )
        {
            roles.remove( role );
        }
        else
        {
            String info = getFullMethodName( CLS_NM, methodName ) + " Role [" + role.getName() + "] User ["
                + session.getUserId() + "], not previously activated";
            throw new SecurityException( GlobalErrIds.URLE_NOT_ACTIVE, info );
        }
    }


    /**
     * This function returns the userId value that is contained within the session object.
     * The function is valid if and only if the session object contains a valid Fortress session.
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @return The userId value
     * @throws SecurityException is thrown if user session not active or runtime error occurs with system.
     */
    @Override
    public String getUserId( Session session )
        throws SecurityException
    {
        assertContext( CLS_NM, "getUserId", session, GlobalErrIds.USER_SESS_NULL );
        return session.getUserId();
    }


    /**
     * This function returns the user object that is contained within the session object.
     * The function is valid if and only if the session object contains a valid Fortress session.
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @return The user value
     *         Sample User data contained in Session object:
     *         <ul> <code>Session</code>
     *         <li> <code>session.getUserId() => demoUser4</code>
     *         <li> <code>session.getInternalUserId() => be2dd2e:12a82ba707e:-7fee</code>
     *         <li> <code>session.getMessage() => Fortress checkPwPolicies userId <demouser4> VALIDATION GOOD</code>
     *         <li> <code>session.getErrorId() => 0</code>
     *         <li> <code>session.getWarningId() => 11</code>
     *         <li> <code>session.getExpirationSeconds() => 469831</code>
     *         <li> <code>session.getGraceLogins() => 0</code>
     *         <li> <code>session.getIsAuthenticated() => true</code>
     *         <li> <code>session.getLastAccess() => 1283623680440</code>
     *         <li> <code>session.getSessionId() => -7410986f:12addeea576:-7fff</code>
     *         <li>  ------------------------------------------
     *         <li> <code>User user = session.getUser();</code>
     *         <ul> <li> <code>user.getUserId() => demoUser4</code>
     *         <li> <code>user.getInternalId() => be2dd2e:12a82ba707e:-7fee</code>
     *         <li> <code>user.getCn() => JoeUser4</code>
     *         <li> <code>user.getDescription() => Demo Test User 4</code>
     *         <li> <code>user.getOu() => test</code>
     *         <li> <code>user.getSn() => User4</code>
     *         <li> <code>user.getBeginDate() => 20090101</code>
     *         <li> <code>user.getEndDate() => none</code>
     *         <li> <code>user.getBeginLockDate() => none</code>
     *         <li> <code>user.getEndLockDate() => none</code>
     *         <li> <code>user.getDayMask() => 1234567</code>
     *         <li> <code>user.getTimeout() => 60</code>
     *         <li> <code>List<UserRole> roles = session.getRoles();</code>
     *         <ul> <li><code>UserRole userRole = roles.get(i);</code>
     *         <li> <code>userRole.getName() => role1</code>
     *         <li> <code>userRole.getBeginTime() => 0000</code>
     *         <li> <code>userRole.getEndTime() => 0000</code>
     *         <li> <code>userRole.getBeginDate() => none</code>
     *         <li> <code>userRole.getEndDate() => none</code>
     *         <li> <code>userRole.getBeginLockDate() => null</code>
     *         <li> <code>userRole.getEndLockDate() => null</code>
     *         <li> <code>userRole.getDayMask() => null</code>
     *         <li> <code>userRole.getTimeout() => 0</code>
     *         </ul>
     *         </ul>
     *         </ul>
     * @throws SecurityException is thrown if user session not active or runtime error occurs with system.
     */
    @Override
    public User getUser( Session session )
        throws SecurityException
    {
        assertContext( CLS_NM, "getUser", session, GlobalErrIds.USER_SESS_NULL );

        return session.getUser();
    }
}