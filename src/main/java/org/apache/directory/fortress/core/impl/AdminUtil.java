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
package org.apache.directory.fortress.core.impl;

import org.apache.directory.fortress.core.DelAccessMgr;
import org.apache.directory.fortress.core.AuthorizationException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.DelAccessMgrFactory;
import org.apache.directory.fortress.core.model.*;

/**
 * This class supplies static wrapper utilities to provide ARBAC functionality to Fortress internal Manager APIs.
 * The utilities within this class are all static and can not be called by code outside of Fortress.
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class AdminUtil
{
    /**
     * Private constructor
     *
     */
    private AdminUtil()
    {
    }

    /**
     * Wrapper function to call {@link DelAccessMgrImpl#canAssign(org.apache.directory.fortress.core.model.Session, org.apache.directory.fortress.core.model.User, org.apache.directory.fortress.core.model.Role)}.
     * This will determine if the user contains an AdminRole that is authorized assignment control over User-Role Assignment (URA).  This adheres to the ARBAC02 functional specification for can-assign URA.
     *
     * @param session This object must be instantiated by calling {@link org.apache.directory.fortress.core.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param user    Instantiated User entity requires only valid userId attribute set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @throws org.apache.directory.fortress.core.SecurityException In the event of data validation error (i.e. invalid userId or role name) or system error.
     */
    static void canAssign(Session session, User user, Role role, String contextId) throws SecurityException
    {
        if (session != null)
        {
            DelAccessMgr dAccessMgr = DelAccessMgrFactory.createInstance(contextId);
            boolean result = dAccessMgr.canAssign(session, user, role);
            if (!result)
            {
                String warning = "canAssign Role [" + role.getName() + "] User [" + user.getUserId() + "] Admin [" + session.getUserId() + "] failed check.";
                throw new SecurityException(GlobalErrIds.URLE_ADMIN_CANNOT_ASSIGN, warning);
            }
        }
    }

    /**
     * Wrapper function to call {@link DelAccessMgrImpl#canDeassign(org.apache.directory.fortress.core.model.Session, org.apache.directory.fortress.core.model.User, org.apache.directory.fortress.core.model.Role)}.
     *
     * This function will determine if the user contains an AdminRole that is authorized revoke control over User-Role Assignment (URA).  This adheres to the ARBAC02 functional specification for can-revoke URA.
     *
     * @param session This object must be instantiated by calling {@link org.apache.directory.fortress.core.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.     * @param user    Instantiated User entity requires only valid userId attribute set.
     * @param user    Instantiated User entity requires userId attribute set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @throws org.apache.directory.fortress.core.SecurityException In the event of data validation error (i.e. invalid userId or role name) or system error.
     */
    static void canDeassign(Session session, User user, Role role, String contextId) throws SecurityException
    {
        if (session != null)
        {
            DelAccessMgr dAccessMgr = DelAccessMgrFactory.createInstance(contextId);
            boolean result = dAccessMgr.canDeassign(session, user, role);
            if (!result)
            {
                String warning = "canDeassign Role [" + role.getName() + "] User [" + user.getUserId() + "] Admin [" + session.getUserId() + "] failed check.";
                throw new SecurityException(GlobalErrIds.URLE_ADMIN_CANNOT_DEASSIGN, warning);

            }
        }
    }

    /**
     * Wrapper function to call {@link DelAccessMgrImpl#canGrant(org.apache.directory.fortress.core.model.Session, org.apache.directory.fortress.core.model.Role, Permission)}.
     * This function will determine if the user contains an AdminRole that is authorized assignment control over
     * Permission-Role Assignment (PRA).  This adheres to the ARBAC02 functional specification for can-assign-p PRA.
     *
     * @param session This object must be instantiated by calling {@link org.apache.directory.fortress.core.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.     * @param perm    Instantiated Permission entity requires valid object name and operation name attributes set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @param perm    Instantiated Permission entity requires {@link Permission#objName} and {@link Permission#opName}.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return boolean value true indicates access allowed.
     * @throws SecurityException In the event of data validation error (i.e. invalid perm or role name) or system error.
     */
    static void canGrant(Session session, Role role, Permission perm, String contextId) throws SecurityException
    {
        if (session != null)
        {
            DelAccessMgr dAccessMgr = DelAccessMgrFactory.createInstance(contextId);
            boolean result = dAccessMgr.canGrant(session, role, perm);
            if (!result)
            {
                String warning = "canGrant Role [" + role.getName() + "] Perm object [" + perm.getObjName() + "] Perm Operation [" + perm.getOpName() + "] Admin [" + session.getUserId() + "] failed check.";
                throw new SecurityException(GlobalErrIds.URLE_ADMIN_CANNOT_GRANT, warning);
            }
        }
    }

    /**
     * Wrapper function to call {@link DelAccessMgrImpl#canRevoke(org.apache.directory.fortress.core.model.Session, org.apache.directory.fortress.core.model.Role, Permission)}.
     *
     * This function will determine if the user contains an AdminRole that is authorized revoke control over
     * Permission-Role Assignment (PRA).  This adheres to the ARBAC02 functional specification for can-revoke-p PRA.
     *
     * @param session This object must be instantiated by calling {@link org.apache.directory.fortress.core.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.     * @param perm    Instantiated Permission entity requires valid object name and operation name attributes set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @param perm    Instantiated Permission entity requires {@link Permission#objName} and {@link Permission#opName}.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @throws org.apache.directory.fortress.core.SecurityException In the event of data validation error (i.e. invalid perm or role name) or system error.
     */
    static void canRevoke(Session session, Role role, Permission perm, String contextId) throws SecurityException
    {
        if (session != null)
        {
            DelAccessMgr dAccessMgr = DelAccessMgrFactory.createInstance(contextId);
            boolean result = dAccessMgr.canRevoke(session, role, perm);
            if (!result)
            {
                String warning = "canRevoke Role [" + role.getName() + "] Perm object [" + perm.getObjName() + "] Perm Operation [" + perm.getOpName() + "] Admin [" + session.getUserId() + "] failed check.";
                throw new SecurityException(GlobalErrIds.URLE_ADMIN_CANNOT_REVOKE, warning);
            }
        }
    }

    /**
     * Wrapper function to call {@link DelAccessMgrImpl#canAssign(org.apache.directory.fortress.core.model.Session, org.apache.directory.fortress.core.model.User, org.apache.directory.fortress.core.model.Role)}.
     * This will determine if the user contains an AdminRole that is authorized assignment control over User.
     *
     * @param session This object must be instantiated by calling {@link org.apache.directory.fortress.core.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param user    Instantiated User entity requires only valid userId attribute set.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @throws org.apache.directory.fortress.core.SecurityException In the event of data validation error (i.e. invalid userId or role name) or system error.
     */
    static void canDo(Session session, User user, String contextId, boolean isAdd) throws SecurityException
    {
        if (session != null)
        {
            boolean result;
            DelAccessMgr dAccessMgr = DelAccessMgrFactory.createInstance(contextId);
            if(isAdd)
            {
                result = dAccessMgr.canAdd(session, user);
                if (!result)
                {
                    String warning = "canDo User [" + user.getUserId() + "] Admin [" + session.getUserId() + "] failed check.";
                    throw new SecurityException(GlobalErrIds.USER_ADMIN_CANNOT_ADD, warning);
                }
            }
            else
            {
                result = dAccessMgr.canEdit(session, user);
                if (!result)
                {
                    String warning = "canDo User [" + user.getUserId() + "] Admin [" + session.getUserId() + "] failed check.";
                    throw new SecurityException(GlobalErrIds.USER_ADMIN_CANNOT_CHANGE, warning);
                }
            }
        }
    }

    /**
     * Method is called by Manager APIs to load contextual information on {@link org.apache.directory.fortress.core.model.FortEntity} and perform checkAccess on Administrative permission.
     * <p>
     * The information is used to
     * <ol>
     * <li>Load the administrative User's {@link Session} object into entity.  This is used for checking to ensure administrator has privilege to perform administrative operation.</li>
     * <li>Load the target operation's permission into the audit context.  This is used for Fortress audit log stored in OpenLDAP</li>
     * </ol>
     *
     * @param session object contains the {@link org.apache.directory.fortress.core.model.User}'s RBAC, {@link org.apache.directory.fortress.core.model.UserRole}, and Administrative Roles {@link org.apache.directory.fortress.core.model.UserAdminRole}.
     * @param perm    contains the permission object name, {@link Permission#objName}, and operation name, {@link Permission#opName}
     * @param entity  used to pass contextual information through Fortress layers for administrative security checks and audit.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @throws org.apache.directory.fortress.core.SecurityException in the event of system error.
     */
    static void setEntitySession(Session session, Permission perm, FortEntity entity, String contextId) throws SecurityException
    {
        if (session != null)
        {
            entity.setAdminSession(session);
            entity.setModCode(getObjName(perm.getObjName()) + "." + perm.getOpName());
            checkAccess(session, perm, contextId);
        }
    }

    /**
     * Wrapper function to call {@link DelAccessMgrImpl#checkAccess(org.apache.directory.fortress.core.model.Session, Permission)}.
     * Perform user arbac authorization.  This function returns a Boolean value meaning whether the subject of a given session is
     * allowed or not to perform a given operation on a given object. The function is valid if and
     * only if the session is a valid Fortress session, the object is a member of the OBJS data set,
     * and the operation is a member of the OPS data set. The session's subject has the permission
     * to perform the operation on that object if and only if that permission is assigned to (at least)
     * one of the session's active roles. This implementation will verify the roles or userId correspond
     * to the subject's active roles are registered in the object's access control list.
     *
     * @param session This object must be instantiated by calling {@link org.apache.directory.fortress.core.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param perm    object contains obj attribute which is a String and contains the name of the object user is trying to access;
     *                perm object contains operation attribute which is also a String and contains the operation name for the object.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @throws SecurityException in the event of data validation failure, security policy violation or DAO error.
     */
    static void checkAccess(Session session, Permission perm, String contextId) throws SecurityException
    {
        if (session != null)
        {
            DelAccessMgr dAccessMgr = DelAccessMgrFactory.createInstance(contextId);
            boolean result = dAccessMgr.checkAccess(session, perm);
            if (!result)
            {
                String info = "checkAccess failed for user [" + session.getUserId() + "] object [" + perm.getObjName() + "] operation [" + perm.getOpName() + "]";
                throw new AuthorizationException(GlobalErrIds.USER_ADMIN_NOT_AUTHORIZED, info);
            }
        }
    }

    /**
     * Utility will parse a String containing objName.operationName and return the objName only.
     *
     * @param szObj contains raw data format.
     * @return String containing objName.
     */
    static String getObjName(String szObj)
    {
        return szObj.substring(szObj.lastIndexOf('.') + 1);
    }
}
