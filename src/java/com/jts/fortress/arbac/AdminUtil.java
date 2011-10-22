/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.arbac;

import com.jts.fortress.FortEntity;
import com.jts.fortress.SecurityException;
import com.jts.fortress.DelegatedAccessMgr;
import com.jts.fortress.DelegatedAccessMgrFactory;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.rbac.Permission;
import com.jts.fortress.rbac.Role;
import com.jts.fortress.rbac.Session;
import com.jts.fortress.rbac.User;

/**
 * This class supplies static wrapper utilities to provide ARBAC functionality to Fortress internal Manager APIs.
 * The utilities within this class are all static and should not be called by code outside of Fortress.
 * </p>
 * This object is thread safe.
 *
 * @author smckinn
 * @created January 9, 2011
 */
public class AdminUtil
{
    private static final String OCLS_NM = AdminUtil.class.getName();

    /**
     * Wrapper function to call {@link DelegatedAccessMgrImpl#canAssign(com.jts.fortress.rbac.Session, com.jts.fortress.rbac.User, com.jts.fortress.rbac.Role)}.
     * This will determine if the user contains an AdminRole that is authorized assignment control over User-Role Assignment (URA).  This adheres to the ARBAC02 functional specification for can-assign URA.
     *
     * @param session This object must be instantiated by calling {@link com.jts.fortress.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param user    Instantiated User entity requires only valid userId attribute set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws com.jts.fortress.SecurityException In the event of data validation error (i.e. invalid userId or role name) or system error.
     */
    public static void canAssign(Session session, User user, Role role) throws SecurityException
    {
        if (session != null)
        {
            DelegatedAccessMgr dAccessMgr = DelegatedAccessMgrFactory.createInstance();
            boolean result = dAccessMgr.canAssign(session, user, role);
            if (!result)
            {
                String warning = OCLS_NM + ".canAssign Role <" + role.getName() + "> User <" + user.getUserId() + "> Admin <" + session.getUserId() + "> failed check.";
                throw new SecurityException(GlobalErrIds.URLE_ADMIN_CANNOT_ASSIGN, warning);

            }
        }
    }

    /**
     * Wrapper function to call {@link DelegatedAccessMgrImpl#canDeassign(com.jts.fortress.rbac.Session, com.jts.fortress.rbac.User, com.jts.fortress.rbac.Role)}.
     * This function will determine if the user contains an AdminRole that is authorized revoke control over User-Role Assignment (URA).  This adheres to the ARBAC02 functional specification for can-revoke URA.
     *
     * @param session This object must be instantiated by calling {@link com.jts.fortress.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.     * @param user    Instantiated User entity requires only valid userId attribute set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws com.jts.fortress.SecurityException In the event of data validation error (i.e. invalid userId or role name) or system error.
     */
    public static void canDeassign(Session session, User user, Role role) throws SecurityException
    {
        if (session != null)
        {
            DelegatedAccessMgr dAccessMgr = DelegatedAccessMgrFactory.createInstance();
            boolean result = dAccessMgr.canDeassign(session, user, role);
            if (!result)
            {
                String warning = OCLS_NM + ".canDeassign Role <" + role.getName() + "> User <" + user.getUserId() + "> Admin <" + session.getUserId() + "> failed check.";
                throw new com.jts.fortress.SecurityException(GlobalErrIds.URLE_ADMIN_CANNOT_DEASSIGN, warning);

            }
        }
    }

    /**
     * Wrapper function to call {@link DelegatedAccessMgrImpl#canGrant(com.jts.fortress.rbac.Session, com.jts.fortress.rbac.Role, com.jts.fortress.rbac.Permission)}.
     * This function will determine if the user contains an AdminRole that is authorized assignment control over
     * Permission-Role Assignment (PRA).  This adheres to the ARBAC02 functional specification for can-assign-p PRA.
     *
     * @param session This object must be instantiated by calling {@link com.jts.fortress.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.     * @param perm    Instantiated Permission entity requires valid object name and operation name attributes set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws SecurityException In the event of data validation error (i.e. invalid perm or role name) or system error.
     */
    public static void canGrant(Session session, Role role, Permission perm) throws com.jts.fortress.SecurityException
    {
        if (session != null)
        {
            DelegatedAccessMgr dAccessMgr = DelegatedAccessMgrFactory.createInstance();
            boolean result = dAccessMgr.canGrant(session, role, perm);
            if (!result)
            {
                String warning = OCLS_NM + ".canGrant Role <" + role.getName() + "> Perm object <" + perm.getObjectName() + "> Perm Operation <" + perm.getOpName() + "> Admin <" + session.getUserId() + "> failed check.";
                throw new com.jts.fortress.SecurityException(GlobalErrIds.URLE_ADMIN_CANNOT_GRANT, warning);
            }
        }
    }

    /**
     * Wrapper function to call {@link DelegatedAccessMgrImpl#canRevoke(com.jts.fortress.rbac.Session, com.jts.fortress.rbac.Role, com.jts.fortress.rbac.Permission)}.
     * This function will determine if the user contains an AdminRole that is authorized revoke control over
     * Permission-Role Assignment (PRA).  This adheres to the ARBAC02 functional specification for can-revoke-p PRA.
     *
     * @param session This object must be instantiated by calling {@link com.jts.fortress.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.     * @param perm    Instantiated Permission entity requires valid object name and operation name attributes set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws com.jts.fortress.SecurityException In the event of data validation error (i.e. invalid perm or role name) or system error.
     */
    public static void canRevoke(Session session, Role role, Permission perm) throws com.jts.fortress.SecurityException
    {
        if (session != null)
        {
            DelegatedAccessMgr dAccessMgr = DelegatedAccessMgrFactory.createInstance();
            boolean result = dAccessMgr.canRevoke(session, role, perm);
            if (!result)
            {
                String warning = OCLS_NM + ".canRevoke Role <" + role.getName() + "> Perm object <" + perm.getObjectName() + "> Perm Operation <" + perm.getOpName() + "> Admin <" + session.getUserId() + "> failed check.";
                throw new SecurityException(GlobalErrIds.URLE_ADMIN_CANNOT_REVOKE, warning);
            }
        }
    }

    /**
     * Method is called by Manager APIs to load contextual information on {@link com.jts.fortress.FortEntity}.
     * </p>
     * The information is used to
     * <ol>
     * <li>Load the administrative User's {@link Session} object into entity.  This is used for checking to ensure administrator has privilege to perform administrative operation.</li>
     * <li>Load the target operation's permission into the audit context.  This is used for Fortress audit log stored in OpenLDAP</li>
     * </ol>
     *
     * @param session object contains the {@link com.jts.fortress.rbac.User}'s RBAC, {@link com.jts.fortress.rbac.UserRole}, and Administrative Roles {@link UserAdminRole}.
     * @param perm    contains the permission object name, {@link com.jts.fortress.rbac.Permission#objectName}, and operation name, {@link com.jts.fortress.rbac.Permission#opName}
     * @param entity  used to pass contextual information through Fortress layers for administrative security checks and audit.
     */
    public static void setAdminData(Session session, Permission perm, FortEntity entity) throws com.jts.fortress.SecurityException
    {
        if (session != null)
        {
            entity.setAdminSession(session);
            entity.setModCode(getObjName(perm.getObjectName()) + "." + perm.getOpName());
        }
    }

    /**
     * Method is called by Manager APIs to load contextual information on {@link com.jts.fortress.FortEntity} and perform checkAccess on Administrative permission.
     * </p>
     * The information is used to
     * <ol>
     * <li>Load the administrative User's {@link Session} object into entity.  This is used for checking to ensure administrator has privilege to perform administrative operation.</li>
     * <li>Load the target operation's permission into the audit context.  This is used for Fortress audit log stored in OpenLDAP</li>
     * </ol>
     *
     * @param session object contains the {@link com.jts.fortress.rbac.User}'s RBAC, {@link com.jts.fortress.rbac.UserRole}, and Administrative Roles {@link UserAdminRole}.
     * @param perm    contains the permission object name, {@link com.jts.fortress.rbac.Permission#objectName}, and operation name, {@link com.jts.fortress.rbac.Permission#opName}
     * @param entity  used to pass contextual information through Fortress layers for administrative security checks and audit.
     * @throws com.jts.fortress.SecurityException in the event of system error.
     */
    public static void setEntitySession(Session session, Permission perm, FortEntity entity) throws com.jts.fortress.SecurityException
    {
        if (session != null)
        {
            entity.setAdminSession(session);
            entity.setModCode(getObjName(perm.getObjectName()) + "." + perm.getOpName());
            checkAccess(session, perm);
        }
    }

    /**
     * Wrapper function to call {@link com.jts.fortress.rbac.AccessMgrImpl#checkAccess(com.jts.fortress.rbac.Session, com.jts.fortress.rbac.Permission)}.
     * Perform user arbac authorization.  This function returns a Boolean value meaning whether the subject of a given session is
     * allowed or not to perform a given operation on a given object. The function is valid if and
     * only if the session is a valid Fortress session, the object is a member of the OBJS data set,
     * and the operation is a member of the OPS data set. The session's subject has the permission
     * to perform the operation on that object if and only if that permission is assigned to (at least)
     * one of the session's active roles. This implementation will verify the roles or userId correspond
     * to the subject's active roles are registered in the object's access control list.
     *
     * @param session This object must be instantiated by calling {@link com.jts.fortress.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param perm    object contains obj attribute which is a String and contains the name of the object user is trying to access;
     *                perm object contains operation attribute which is also a String and contains the operation name for the object.
     * @return True if user has access, false otherwise.
     * @throws SecurityException in the event of data validation failure, security policy violation or DAO error.
     */
    public static void checkAccess(Session session, Permission perm) throws com.jts.fortress.SecurityException
    {
        if (session != null)
        {
            DelegatedAccessMgr dAccessMgr = com.jts.fortress.DelegatedAccessMgrFactory.createInstance();
            boolean result = dAccessMgr.checkAccess(session, perm);
            if (!result)
            {
                String info = OCLS_NM + ".checkAccess failed for user <" + session.getUserId() + "> object <" + perm.getObjectName() + "> operation <" + perm.getOpName() + ">";
                throw new com.jts.fortress.AuthorizationException(GlobalErrIds.USER_ADMIN_NOT_AUTHORIZED, info);
            }
        }
    }

    /**
     * Utility will parse a String containing objectName.operationName and return the objectName only.
     *
     * @param szObj contains raw data format.
     * @return String containing objectName.
     */
    public static String getObjName(String szObj)
    {
        return szObj.substring(szObj.lastIndexOf('.') + 1);
    }
}
