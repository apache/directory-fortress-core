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


import org.apache.directory.fortress.core.model.UserAdminRole;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.User;

import java.util.List;
import java.util.Set;


/**
 * This interface prescribes the API for performing runtime delegated access control operations on objects that are 
 * provisioned Fortress ARBAC entities that reside in LDAP directory.
 * These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification 
 * describes delegated administrative operations for the creation and maintenance of ARBAC element sets and relations.  
 * Delegated administrative review functions for performing administrative queries and system functions for creating and 
 * managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="./doc-files/ARbac.png" alt="">
 * <p>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises 
 * the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without 
 * sacrificing regulations for accountability or traceability.
 * <p>
 * This interface's implementer will NOT be thread safe if parent instance variables ({@link Manageable#setContextId(String)} 
 * or {@link Manageable#setAdmin(org.apache.directory.fortress.core.model.Session)}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface DelAccessMgr extends Manageable
{
    /**
     * This function will determine if the user contains an AdminRole that is authorized assignment control over
     * User-Role Assignment (URA).  This adheres to the ARBAC02 functional specification for can-assign URA.
     *
     * @param session This object must be instantiated by calling 
     * {@link AccessMgr#createSession(org.apache.directory.fortress.core.model.User, boolean)} before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param user    Instantiated User entity requires only valid userId attribute set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws SecurityException
     *          In the event of data validation error (i.e. invalid userId or role name) or system error.
     */
    boolean canAssign( Session session, User user, Role role )
        throws SecurityException;


    /**
     * This function will determine if the user contains an AdminRole that is authorized revoke control over
     * User-Role Assignment (URA).  This adheres to the ARBAC02 functional specification for can-revoke URA.
     *
     * @param session This object must be instantiated by calling {@link AccessMgr#createSession} method before passing into 
     * the method.  No variables need to be set by client after returned from createSession.     
     * @param user    Instantiated User entity requires only valid userId attribute set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws SecurityException
     *          In the event of data validation error (i.e. invalid userId or role name) or system error.
     */
    boolean canDeassign( Session session, User user, Role role )
        throws SecurityException;


    /**
     * This function will determine if the user contains an AdminRole that is authorized assignment control over
     * Permission-Role Assignment (PRA).  This adheres to the ARBAC02 functional specification for can-assign-p PRA.
     *
     * @param session This object must be instantiated by calling {@link AccessMgr#createSession} method before passing 
     * into the method.  No variables need to be set by client after returned from createSession.     
     * @param perm    Instantiated Permission entity requires valid object name and operation name attributes set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws SecurityException
     *          In the event of data validation error (i.e. invalid perm or role name) or system error.
     */
    boolean canGrant( Session session, Role role, Permission perm )
        throws SecurityException;


    /**
     * This function will determine if the user contains an AdminRole that is authorized revoke control over
     * Permission-Role Assignment (PRA).  This adheres to the ARBAC02 functional specification for can-revoke-p PRA.
     *
     * @param session This object must be instantiated by calling {@link AccessMgr#createSession} method before passing 
     * into the method.  No variables need to be set by client after returned from createSession.     
     * @param perm    Instantiated Permission entity requires valid object name and operation name attributes set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws SecurityException In the event of data validation error (i.e. invalid perm or role name) or system error.
     */
    boolean canRevoke( Session session, Role role, Permission perm )
        throws SecurityException;


    /**
     * This function will determine if the user contains an AdminRole that is authorized to add a new User.
     *
     * @param session This object must be instantiated by calling
     * {@link AccessMgr#createSession(org.apache.directory.fortress.core.model.User, boolean)} before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param user    Instantiated User entity requires only valid userId attribute set.
     * @return boolean value true indicates access allowed.
     * @throws SecurityException
     *          In the event of data validation error (i.e. invalid userId or role name) or system error.
     */
    boolean canAdd( Session session, User user )
            throws SecurityException;


    /**
     * This function will determine if the user contains an AdminRole that is authorized update/delete control over
     * User.
     *
     * @param session This object must be instantiated by calling
     * {@link AccessMgr#createSession(org.apache.directory.fortress.core.model.User, boolean)} before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param user    Instantiated User entity requires only valid userId attribute set.
     * @return boolean value true indicates access allowed.
     * @throws SecurityException
     *          In the event of data validation error (i.e. invalid userId or role name) or system error.
     */
    boolean canEdit( Session session, User user )
            throws SecurityException;


    /**
     * This function returns a Boolean value meaning whether the subject of a given session is
     * allowed or not to perform a given operation on a given object. The function is valid if and
     * only if the session is a valid Fortress session, the object is a member of the OBJS data set,
     * and the operation is a member of the OPS data set. The session's subject has the permission
     * to perform the operation on that object if and only if that permission is assigned to (at least)
     * one of the session's active roles. This implementation will verify the roles or userId correspond
     * to the subject's active roles are registered in the object's access control list.
     *
     * @param perm    object contains obj attribute which is a String and contains the name of the object user is trying to 
     * access; perm object contains operation attribute which is also a String and contains the operation name for the object.
     * @param session This object must be instantiated by calling {@link AccessMgr#createSession} method before passing into 
     * the method.  No variables need to be set by client after returned from createSession.
     * @return True of user has access, false otherwise.
     * @throws SecurityException
     *          is thrown if runtime error occurs with system.
     */
    boolean checkAccess( Session session, Permission perm )
        throws SecurityException;


    /**
     * This function adds an adminRole as an active role of a session whose owner is a given user.
     * <p>
     * The function is valid if and only if:
     * <ul>
     *   <li>the user is a member of the USERS data set</li>
     *   <li>the role is a member of the ADMIN ROLES data set</li>
     *   <li>the session is a valid Fortress session</li>
     *   <li>the user is authorized to that admin role</li>
     *   <li>the session is owned by that user.</li>
     * </ul>
     * <p>
     *
     * @param session object contains the user's returned RBAC and ARBAC sessions from the createSession method.
     * @param role    object contains the adminRole name to be activated into session.
     * @throws SecurityException
     *          is thrown if user is not allowed to activate or runtime error occurs with system.
     */
    void addActiveRole( Session session, UserAdminRole role )
        throws SecurityException;


    /**
     * This function deactivates adminRole from the active adminRole set of a session owned by a given user.
     * The function is valid if and only if the user is a member of the USERS data set, the
     * session object contains a valid Fortress session, the session is owned by the user,
     * and the adminRole is an active adminRole of that session.
     *
     * @param session object contains the user's returned RBAC and ARBAC sessions from the createSession method.
     * @param role    object contains the adminRole name to be deactivated.
     * @throws SecurityException is thrown if user is not allowed to deactivate or runtime error occurs with system.
     */
    void dropActiveRole( Session session, UserAdminRole role )
        throws SecurityException;


    /**
     * This function returns the active admin roles associated with a session. The function is valid if
     * and only if the session is a valid Fortress session.
     *
     * @param session object contains the user's returned ARBAC session from the createSession method.
     * @return List&lt;UserAdminRole&gt; containing all adminRoles active in user's session.  This will NOT contain inherited roles.
     * @throws SecurityException
     *          is thrown if session invalid or system. error.
     */
    List<UserAdminRole> sessionAdminRoles( Session session )
        throws SecurityException;


    /**
     * This function returns the authorized admin roles associated with a session based on hierarchical relationships. 
     * The function is valid if and only if the session is a valid Fortress session.
     *
     * @param session object contains the user's returned ARBAC session from the createSession method.
     * @return Set&lt;String&gt; containing all adminRoles authorized in user's session.  This will contain inherited roles.
     * @throws SecurityException is thrown if session invalid or system. error.
     */
    Set<String> authorizedAdminRoles( Session session )
        throws SecurityException;


    /**
     * This function returns the ARBAC (administrative) permissions of the session, i.e., the permissions assigned
     * to its authorized admin roles. The function is valid if and only if the session is a valid Fortress session.
     *
     * @param session object contains the user's returned ARBAC session from the createSession method.
     * @return List&lt;Permission&gt; containing admin permissions (op, obj) active for user's session.
     * @throws SecurityException in the event runtime error occurs with system.
     */
    List<Permission> sessionPermissions( Session session )
        throws SecurityException;
}
