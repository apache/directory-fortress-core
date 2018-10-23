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
package org.apache.directory.fortress.core;


import java.util.List;
import java.util.Set;

import org.apache.directory.fortress.core.model.*;


/**
 * This object performs runtime access control operations on objects that are provisioned RBAC entities
 * that reside in LDAP directory.  These APIs map directly to similar named APIs specified by ANSI and NIST
 * RBAC system functions.
 * Many of the java doc function descriptions found below were taken directly from ANSI INCITS 359-2004.
 * The RBAC Functional specification describes administrative operations for the creation
 * and maintenance of RBAC element sets and relations; administrative review functions for
 * performing administrative queries; and system functions for creating and managing
 * RBAC attributes on user sessions and making access control decisions.
 * <hr>
 * <h3></h3>
 * <h4>RBAC0 - Core</h4>
 * Many-to-many relationship between Users, Roles and Permissions. Selective role activation into sessions.  API to add, 
 * update, delete identity data and perform identity and access control decisions during runtime operations.
 * <p>
 * <img src="./doc-files/RbacCore.png" alt="Rbac Core">
 * <hr>
 * <h4>RBAC1 - General Hierarchical Roles</h4>
 * Simplifies role engineering tasks using inheritance of one or more parent roles.
 * <p>
 * <img src="./doc-files/RbacHier.png" alt="Rbac Hierarchy">
 * <hr>
 * <h4>RBAC2 - Static Separation of Duty (SSD) Relations</h4>
 * Enforce mutual membership exclusions across role assignments.  Facilitate dual control policies by restricting which 
 * roles may be assigned to users in combination.  SSD provide added granularity for authorization limits which help 
 * enterprises meet strict compliance regulations.
 * <p>
 * <img src="./doc-files/RbacSSD.png" alt="Rbac SSDe">
 * <hr>
 * <h4>RBAC3 - Dynamic Separation of Duty (DSD) Relations</h4>
 * Control allowed role combinations to be activated within an RBAC session.  DSD policies fine tune role policies that 
 * facilitate authorization dual control and two man policy restrictions during runtime security checks.
 * <p>
 * <img src="./doc-files/RbacDSD.png" alt="Rbac DSD">
 * <hr>
 * <p>
 * This interface's implementer will NOT be thread safe if parent instance variables ({@link Manageable#setContextId(String)} 
 * or {@link Manageable#setAdmin(org.apache.directory.fortress.core.model.Session)}) are set.
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface AccessMgr extends Manageable
{

    /**
     * Perform user authentication only.  It does not activate RBAC roles in session but will evaluate password policies.
     *
     * @param userId   Contains the userid of the user signing on.
     * @param password Contains the user's password.
     * @return Session object will be returned if authentication successful.  This will not contain user's roles.
     * @throws SecurityException
     *          in the event of data validation failure, security policy violation or DAO error.
     */
    Session authenticate( String userId, String password )
        throws SecurityException;


    /**
     * Perform user authentication {@link User#password} and role activations.<br>
     * This method must be called once per user prior to calling other methods within this class.
     * The successful result is {@link org.apache.directory.fortress.core.model.Session} that contains target user's RBAC 
     * {@link User#roles} and Admin role {@link User#adminRoles}.<br>
     * In addition to checking user password validity it will apply configured password policy checks 
     * {@link org.apache.directory.fortress.core.model.User#pwPolicy}.<br>
     * Method may also store parms passed in for audit trail {@link org.apache.directory.fortress.core.model.FortEntity}.
     * <h3></h3>
     * <h4> This API will...</h4>
     * <ul>
     *   <li>authenticate user password if trusted == false.</li>
     *   <li>
     *     perform <a href="http://tools.ietf.org/html/draft-behera-ldap-password-policy-10">password policy evaluation</a>.
     *   </li>
     *   <li>
     *     fail for any user who is locked by pw policies
     *     {@link org.apache.directory.fortress.core.model.User#isLocked()}, regardless of trusted flag being set as parm 
     *     on API.
     *   </li>
     *   <li>
     *     evaluate temporal {@link org.apache.directory.fortress.core.model.Constraint}(s) on {@link User}, {@link UserRole} and {@link org.apache.directory.fortress.core.model.UserAdminRole} entities.
     *   <li>process selective role activations into User RBAC Session {@link User#roles}.</li>
     *   <li>
     *     check Dynamic Separation of Duties {@link org.apache.directory.fortress.core.impl.DSDChecker#validate(
     *          org.apache.directory.fortress.core.model.Session, 
     *          org.apache.directory.fortress.core.model.Constraint, 
     *          org.apache.directory.fortress.core.util.time.Time,
     *          org.apache.directory.fortress.core.util.VUtil.ConstraintType)} on 
     *          {@link org.apache.directory.fortress.core.model.User#roles}.
     *   </li>
     *   <li>process selective administrative role activations {@link User#adminRoles}.</li>
     *   <li>
     *     return a {@link org.apache.directory.fortress.core.model.Session} containing 
     *     {@link org.apache.directory.fortress.core.model.Session#getUser()}, 
     *     {@link org.apache.directory.fortress.core.model.Session#getRoles()} and (if admin user) 
     *     {@link org.apache.directory.fortress.core.model.Session#getAdminRoles()} if everything checks out good.
     *   </li>
     *   <li>throw a checked exception that will be {@link SecurityException} or its derivation.</li>
     *   <li>throw a {@link SecurityException} for system failures.</li>
     *   <li>throw a {@link PasswordException} for authentication and password policy violations.</li>
     *   <li>throw a {@link ValidationException} for data validation errors.</li>
     *   <li>throw a {@link FinderException} if User id not found.</li>
     * </ul>
     * <h4>
     * The function is valid if and only if:
     * </h4>
     * <ul>
     *   <li> the user is a member of the USERS data set</li>
     *   <li> the password is supplied (unless trusted).</li>
     *   <li> the (optional) active role set is a subset of the roles authorized for that user.</li>
     * </ul>
     * <h4>
     * The following attributes may be set when calling this method
     * </h4>
     * <ul>
     *   <li>{@link User#userId} - required</li>
     *   <li>{@link org.apache.directory.fortress.core.model.User#password}
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.User#roles} contains a list of RBAC role names authorized for user 
     *     and targeted for activation within this session.  Default is all authorized RBAC roles will be activated into this 
     *     Session.
     *   </li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.User#adminRoles} contains a list of Admin role names authorized for 
     *     user and targeted for activation.  Default is all authorized ARBAC roles will be activated into this Session.
     *   </li>
     *   <li>
     *     {@link User#props} collection of name value pairs collected on behalf of User during signon.  For example 
     *     hostname:myservername or ip:192.168.1.99
     *   </li>
     * </ul>
     * <h4>
     * Notes:
     * </h4>
     * <ul>
     * <li> roles that violate Dynamic Separation of Duty Relationships will not be activated into session.
     * <li> role activations will proceed in same order as supplied to User entity setter, see {@link User#setRoleName(String)}.
     * </ul>
     *
     * @param user      Contains {@link User#userId}, {@link org.apache.directory.fortress.core.model.User#password} 
     * (optional if {@code isTrusted} is 'true'), optional {@link User#roles}, optional 
     * {@link org.apache.directory.fortress.core.model.User#adminRoles}
     * @param isTrusted if true password is not required.
     * @return Session object will contain authentication result code 
     * {@link org.apache.directory.fortress.core.model.Session#errorId}, 
     * RBAC role activations {@link org.apache.directory.fortress.core.model.Session#getRoles()}, 
     * Admin Role activations {@link org.apache.directory.fortress.core.model.Session#getAdminRoles()},
     * Password policy codes {@link org.apache.directory.fortress.core.model.Session#warnings},
     * {@link org.apache.directory.fortress.core.model.Session#expirationSeconds}, 
     * {@link org.apache.directory.fortress.core.model.Session#graceLogins} and more.
     * @throws SecurityException
     *          in the event of data validation failure, security policy violation or DAO error.
     */
    Session createSession( User user, boolean isTrusted )
        throws SecurityException;

    /**
     * Same as {@link AccessMgr#createSession( User user, boolean isTrusted )}
     * Plus constraint which places attribute key:value, e.g. location=north, into the runtime context, for evaluation during role activation.
     *
     * @param user      Contains {@link User#userId}, {@link org.apache.directory.fortress.core.model.User#password}
     * (optional if {@code isTrusted} is 'true'), optional {@link User#roles}, optional
     * {@link org.apache.directory.fortress.core.model.User#adminRoles}
     * @param constraints List of case-sensitive {@link RoleConstraint#key}, {@link RoleConstraint#value}, bound for role activation checks.
     * @param isTrusted if true password is not required.
     * @return Session object will contain authentication result code
     * {@link org.apache.directory.fortress.core.model.Session#errorId},
     * RBAC role activations {@link org.apache.directory.fortress.core.model.Session#getRoles()},
     * Admin Role activations {@link org.apache.directory.fortress.core.model.Session#getAdminRoles()},
     * Password policy codes {@link org.apache.directory.fortress.core.model.Session#warnings},
     * {@link org.apache.directory.fortress.core.model.Session#expirationSeconds},
     * {@link org.apache.directory.fortress.core.model.Session#graceLogins} and more.
     * @throws SecurityException
     *          in the event of data validation failure, security policy violation or DAO error.
     */
    Session createSession( User user, List<RoleConstraint> constraints, boolean isTrusted )
        throws SecurityException;

    /**
     * Perform group {@link Group} role activations {@link Group#members}.<br>
     * Group sessions are always trusted. <br>
     * This method must be called once per group prior to calling other methods within this class.
     * The successful result is {@link org.apache.directory.fortress.core.model.Session} that contains target group's RBAC
     * {@link Group#members}
     * <h4> This API will...</h4>
     * <ul>
     *   <li>
     *     fail for any non-existing group
     *   </li>
     *   <li>
     *     evaluate temporal {@link org.apache.directory.fortress.core.model.Constraint}(s) on member {@link UserRole} entities.
     *   <li>process selective role activations into Group RBAC Session {@link Group#roles}.</li>
     *   <li>
     *     check Dynamic Separation of Duties {@link org.apache.directory.fortress.core.impl.DSDChecker#validate(
     *          org.apache.directory.fortress.core.model.Session,
     *          org.apache.directory.fortress.core.model.Constraint,
     *          org.apache.directory.fortress.core.util.time.Time,
     *          org.apache.directory.fortress.core.util.VUtil.ConstraintType)} on
     *          {@link org.apache.directory.fortress.core.model.User#roles}.
     *   </li>
     *   <li>
     *     return a {@link org.apache.directory.fortress.core.model.Session} containing
     *     {@link org.apache.directory.fortress.core.model.Session#getGroup()},
     *     {@link org.apache.directory.fortress.core.model.Session#getRoles()}
     *   </li>
     *   <li>throw a checked exception that will be {@link SecurityException} or its derivation.</li>
     *   <li>throw a {@link SecurityException} for system failures.</li>
     *   <li>throw a {@link ValidationException} for data validation errors.</li>
     *   <li>throw a {@link FinderException} if Group name not found.</li>
     * </ul>
     * <h4>
     * The function is valid if and only if:
     * </h4>
     * <ul>
     *   <li> the group is a member of the GROUPS data set</li>
     *   <li> the (optional) active role set is a subset of the roles authorized for that group.</li>
     * </ul>
     * <h4>
     * The following attributes may be set when calling this method
     * </h4>
     * <ul>
     *   <li>{@link Group#name} - required</li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.Group#members} contains a list of RBAC role names authorized for group
     *     and targeted for activation within this session.  Default is all authorized RBAC roles will be activated into this
     *     Session.
     *   </li>
     * </ul>
     * <h4>
     * Notes:
     * </h4>
     * <ul>
     * <li> roles that violate Dynamic Separation of Duty Relationships will not be activated into session.
     * </ul>
     *
     * @param group Contains {@link Group#name}, {@link org.apache.directory.fortress.core.model.Group#members}
     * (optional), optional {@link Group#type}, optional
     * @return Session object will contain authentication result code
     * {@link org.apache.directory.fortress.core.model.Session#errorId},
     * RBAC role activations {@link org.apache.directory.fortress.core.model.Session#getRoles()},
     * Password policy codes {@link org.apache.directory.fortress.core.model.Session#warnings},
     * {@link org.apache.directory.fortress.core.model.Session#expirationSeconds},
     * {@link org.apache.directory.fortress.core.model.Session#graceLogins} and more.
     * @throws SecurityException
     *          in the event of data validation failure, security policy violation or DAO error.
     */
    Session createSession(Group group)
            throws SecurityException;


    /**
     * Perform user RBAC authorization.  This function returns a Boolean value meaning whether the subject of a given 
     * session is allowed or not to perform a given operation on a given object. The function is valid if and
     * only if the session is a valid Fortress session, the object is a member of the OBJS data set,
     * and the operation is a member of the OPS data set. The session's subject has the permission
     * to perform the operation on that object if and only if that permission is assigned to (at least)
     * one of the session's active roles. This implementation will verify the roles or userId correspond
     * to the subject's active roles are registered in the object's access control list.
     *
     * @param perm    must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName}, of 
     * permission User is trying to access.
     * @param session This object must be instantiated by calling {@link AccessMgr#createSession} method before passing 
     * into the method.  No variables need to be set by client after returned from createSession.
     * @return True if user has access, false otherwise.
     * @throws SecurityException
     *          in the event of data validation failure, security policy violation or DAO error.
     */
    boolean checkAccess( Session session, Permission perm )
        throws SecurityException;

    /**
     * Combine createSession and checkAccess into a single method.
     * This function returns a Boolean value meaning whether the User is allowed or not to perform a given operation on a given object.
     * The function is valid if and only if the user is a valid Fortress user, the object is a member of the OBJS data set,
     * and the operation is a member of the OPS data set. The user has the permission
     * to perform the operation on that object if and only if that permission is assigned to (at least)
     * one of the session's active roles. This implementation will verify the roles or userId correspond
     * to the user's active roles are registered in the object's access control list.
     *
     * @param user      Contains {@link User#userId}, {@link org.apache.directory.fortress.core.model.User#password}
     * (optional if {@code isTrusted} is 'true'), optional {@link User#roles}}
     * @param perm    must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName}, of
     * permission User is trying to access.
     * @return True if user has access, false otherwise.
     * @throws SecurityException
     *          in the event of data validation failure, security policy violation or DAO error.
     */
    public boolean checkAccess( User user, Permission perm, boolean isTrusted )
        throws SecurityException;

    /**
     * Combine createSession and a role check into a single method.
     * This function returns a Boolean value meaning whether the User has a particular role.
     * The function is valid if and only if the user is a valid Fortress user and the role is a member of the ROLES data set.
     *
     * @param user      Contains {@link User#userId}, {@link org.apache.directory.fortress.core.model.User#password}
     * (optional if {@code isTrusted} is 'true'), optional {@link User#roles}}
     * @param role    object contains the role name, {@link Role#name}, to be checked.
     * @return True if user has role, false otherwise.
     * @throws SecurityException
     *          in the event of data validation failure, security policy violation or DAO error.
     */
    public boolean isUserInRole( User user, Role role, boolean isTrusted )
        throws SecurityException;

    /**
     * This function returns the permissions of the session, i.e., the permissions assigned
     * to its authorized roles. The function is valid if and only if the session is a valid Fortress session.
     *
     * @param session This object must be instantiated by calling {@link AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @return List&lt;Permission&gt; containing permissions (op, obj) active for user's session.
     * @throws SecurityException is thrown if runtime error occurs with system.
     */
    List<Permission> sessionPermissions( Session session )
        throws SecurityException;


    /**
     * This function returns the active roles associated with a session. The function is valid if
     * and only if the session is a valid Fortress session.
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @return List&lt;UserRole&gt; containing all roles active in user's session.  This will NOT contain inherited roles.
     * @throws SecurityException is thrown if session invalid or system. error.
     */
    List<UserRole> sessionRoles( Session session )
        throws SecurityException;


    /**
     * This function returns the authorized roles associated with a session based on hierarchical relationships. The 
     * function is valid if and only if the session is a valid Fortress session.
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @return Set&lt;String&gt; containing all roles active in user's session.  This will contain inherited roles.
     * @throws SecurityException is thrown if session invalid or system. error.
     */
    Set<String> authorizedRoles( Session session )
        throws SecurityException;


    /**
     * This function adds a role as an active role of a session whose owner is a given user.
     * <p>
     * The function is valid if and only if:
     * <ul>
     *   <li> the user is a member of the USERS data set</li>
     *   <li> the role is a member of the ROLES data set</li>
     *   <li> the role inclusion does not violate Dynamic Separation of Duty Relationships</li>
     *   <li> the session is a valid Fortress session</li>
     *   <li> the user is authorized to that role</li>
     *   <li> the session is owned by that user.</li>
     * </ul>
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @param role    object contains the role name, {@link UserRole#name}, to be activated into session.
     * @throws SecurityException is thrown if user is not allowed to activate or runtime error occurs with system.
     */
    void addActiveRole( Session session, UserRole role )
        throws SecurityException;


    /**
     * This function deletes a role from the active role set of a session owned by a given user.
     * The function is valid if and only if the user is a member of the USERS data set, the
     * session object contains a valid Fortress session, the session is owned by the user,
     * and the role is an active role of that session.
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @param role    object contains the role name, {@link org.apache.directory.fortress.core.model.UserRole#name}, to be 
     * deactivated.
     * @throws SecurityException is thrown if user is not allowed to deactivate or runtime error occurs with system.
     */
    void dropActiveRole( Session session, UserRole role )
        throws SecurityException;


    /**
     * This function returns the userId value that is contained within the session object.
     * The function is valid if and only if the session object contains a valid Fortress session.
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @return The userId value
     * @throws SecurityException is thrown if user session not active or runtime error occurs with system.
     */
    String getUserId( Session session )
        throws SecurityException;


    /**
     * This function returns the user object that is contained within the session object.
     * The function is valid if and only if the session object contains a valid Fortress session.
     *
     * @param session object contains the user's returned RBAC session from the createSession method.
     * @return The user value
     *         Sample User data contained in Session object:<br>
     *         <tt>Session</tt>
     *         <ul> 
     *           <li><tt>session.getUserId() =&gt; demoUser4</tt></li>
     *           <li><tt>session.getInternalUserId() =&gt; be2dd2e:12a82ba707e:-7fee</tt></li>
     *           <li><tt>session.getMessage() =&gt; Fortress checkPwPolicies userId &lt;demouser4&gt; VALIDATION GOOD</tt></li>
     *           <li><tt>session.getErrorId() =&gt; 0</tt></li>
     *           <li><tt>session.getWarningId() =&gt; 11</tt></li>
     *           <li><tt>session.getExpirationSeconds() =&gt; 469831</tt></li>
     *           <li><tt>session.getGraceLogins() =&gt; 0</tt></li>
     *           <li><tt>session.getIsAuthenticated() =&gt; true</tt></li>
     *           <li><tt>session.getLastAccess() =&gt; 1283623680440</tt></li>
     *           <li><tt>session.getSessionId() =&gt; -7410986f:12addeea576:-7fff</tt></li>
     *           <li>------------------------------------------</li>
     *           <li><tt>User user = session.getUser();</tt></li>
     *           <li>
     *             <ul> 
     *               <li><tt>user.getUserId() =&gt; demoUser4</tt></li>
     *               <li><tt>user.getInternalId() =&gt; be2dd2e:12a82ba707e:-7fee</tt></li>
     *               <li><tt>user.getCn() =&gt; JoeUser4</tt></li>
     *               <li><tt>user.getDescription() =&gt; Demo Test User 4</tt></li>
     *               <li><tt>user.getOu() =&gt; test</tt></li>
     *               <li><tt>user.getSn() =&gt; User4</tt></li>
     *               <li><tt>user.getBeginDate() =&gt; 20090101</tt></li>
     *               <li><tt>user.getEndDate() =&gt; none</tt></li>
     *               <li><tt>user.getBeginLockDate() =&gt; none</tt></li>
     *               <li><tt>user.getEndLockDate() =&gt; none</tt></li>
     *               <li><tt>user.getDayMask() =&gt; 1234567</tt></li>
     *               <li><tt>user.getTimeout() =&gt; 60</tt></li>
     *               <li><tt>List&lt;UserRole&gt; roles = session.getRoles();</tt></li>
     *               <li>
     *                 <ul> 
     *                   <li><tt>UserRole userRole = roles.get(i);</tt></li>
     *                   <li><tt>userRole.getName() =&gt; role1</tt></li>
     *                   <li><tt>userRole.getBeginTime() =&gt; 0000</tt></li>
     *                   <li><tt>userRole.getEndTime() =&gt; 0000</tt></li>
     *                   <li><tt>userRole.getBeginDate() =&gt; none</tt></li>
     *                   <li><tt>userRole.getEndDate() =&gt; none</tt></li>
     *                   <li><tt>userRole.getBeginLockDate() =&gt; null</tt></li>
     *                   <li><tt>userRole.getEndLockDate() =&gt; null</tt></li>
     *                   <li><tt>userRole.getDayMask() =&gt; null</tt></li>
     *                   <li><tt>userRole.getTimeout() =&gt; 0</tt></li>
     *                 </ul>
     *               </li>
     *             </ul>
     *           </li>
     *         </ul>
     * @throws SecurityException is thrown if user session not active or runtime error occurs with system.
     */
    User getUser( Session session )
        throws SecurityException;
}
