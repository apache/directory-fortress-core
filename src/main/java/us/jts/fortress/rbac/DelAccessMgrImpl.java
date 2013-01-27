/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import us.jts.fortress.DelAccessMgr;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.cfg.Config;
import us.jts.fortress.util.attr.VUtil;
import us.jts.fortress.SecurityException;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class implements the ARBAC02 DelAccessMgr interface for performing runtime delegated access control operations on objects that are provisioned Fortress ARBAC entities
 * that reside in LDAP directory.  These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification describes delegated administrative
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
 * This class is NOT thread safe if parent instance variables ({@link #contextId} or {@link #adminSess}) are set.
 * <p/>
 *
 * @author Shawn McKinney
 */
public class DelAccessMgrImpl extends AccessMgrImpl implements DelAccessMgr
{
    private static final String CLS_NM = DelAccessMgrImpl.class.getName();
    private static final UserP userP = new UserP();
    private static final PermP permP = new PermP();
    private static final String SUPER_ADMIN = Config.getProperty("superadmin.role", "ftSuperAdmin");

    // package private constructor ensures outside classes cannot use:
    DelAccessMgrImpl()
    {}

    /**
     * This function will determine if the user contains an AdminRole that is authorized assignment control over
     * User-Role Assignment (URA).  This adheres to the ARBAC02 functional specification for can-assign URA.
     *
     * @param session This object must be instantiated by calling {@link us.jts.fortress.AccessMgr#createSession(us.jts.fortress.rbac.User, boolean)} before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param user    Instantiated User entity requires only valid userId attribute set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws us.jts.fortress.SecurityException In the event of data validation error (i.e. invalid userId or role name) or system error.
     */
    @Override
    public boolean canAssign(Session session, User user, Role role)
        throws SecurityException
    {
        String methodName = "canAssign";
        assertContext(CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL);
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        return checkUserRole(session, user, role);
    }

    /**
     * This function will determine if the user contains an AdminRole that is authorized revoke control over
     * User-Role Assignment (URA).  This adheres to the ARBAC02 functional specification for can-revoke URA.
     *
     * @param session This object must be instantiated by calling {@link us.jts.fortress.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param user    Instantiated User entity requires only valid userId attribute set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws SecurityException In the event of data validation error (i.e. invalid userId or role name) or system error.
     */
    @Override
    public boolean canDeassign(Session session, User user, Role role)
        throws SecurityException
    {
        String methodName = "canDeassign";
        assertContext(CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL);
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        return checkUserRole(session, user, role);
    }

    /**
     * This function will determine if the user contains an AdminRole that is authorized assignment control over
     * Permission-Role Assignment (PRA).  This adheres to the ARBAC02 functional specification for can-assign-p PRA.
     *
     * @param session This object must be instantiated by calling {@link us.jts.fortress.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param perm    Instantiated Permission entity requires valid object name and operation name attributes set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws us.jts.fortress.SecurityException In the event of data validation error (i.e. invalid perm or role name) or system error.
     */
    @Override
    public boolean canGrant(Session session, Role role, Permission perm)
        throws SecurityException
    {
        String methodName = "canGrant";
        assertContext(CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL);
        assertContext(CLS_NM, methodName, perm, GlobalErrIds.PERM_OBJECT_NULL);
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        return checkRolePermission(session, role, perm);
    }

    /**
     * This function will determine if the user contains an AdminRole that is authorized revoke control over
     * Permission-Role Assignment (PRA).  This adheres to the ARBAC02 functional specification for can-revoke-p PRA.
     *
     * @param session This object must be instantiated by calling {@link us.jts.fortress.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param perm    Instantiated Permission entity requires valid object name and operation name attributes set.
     * @param role    Instantiated Role entity requires only valid role name attribute set.
     * @return boolean value true indicates access allowed.
     * @throws SecurityException In the event of data validation error (i.e. invalid perm or role name) or system error.
     */
    @Override
    public boolean canRevoke(Session session, Role role, Permission perm)
        throws SecurityException
    {
        String methodName = "canRevoke";
        assertContext(CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL);
        assertContext(CLS_NM, methodName, perm, GlobalErrIds.PERM_OBJECT_NULL);
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        return checkRolePermission(session, role, perm);
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
     * @param session This object must be instantiated by calling {@link us.jts.fortress.AccessMgr#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @return True of user has access, false otherwise.
     * @throws SecurityException In the event of data validation error (i.e. invalid perm name) or system error.
     */
    @Override
    public boolean checkAccess(Session session, Permission perm)
        throws SecurityException
    {
        String methodName =  "checkAccess";
        assertContext(CLS_NM, methodName, perm, GlobalErrIds.PERM_NULL);
        VUtil.assertNotNullOrEmpty(perm.getOpName(), GlobalErrIds.PERM_OPERATION_NULL, methodName);
        VUtil.assertNotNullOrEmpty(perm.getObjectName(), GlobalErrIds.PERM_OBJECT_NULL, methodName);
        assertContext(CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL);
        // This flag set will check administrative permission data set.
        perm.setAdmin(true);
        return super.checkAccess(session, perm);
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
     * @throws us.jts.fortress.SecurityException is thrown if user is not allowed to activate or runtime error occurs with system.
     */
    @Override
    public void addActiveRole(Session session, UserAdminRole role)
        throws SecurityException
    {
        String methodName = "addActiveRole";
        assertContext(CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL);
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ARLE_NULL);
        role.setUserId(session.getUserId());
        List<UserAdminRole> sRoles = session.getAdminRoles();
        // If session already has admin role activated log an error and throw an exception:
        if (sRoles != null && sRoles.contains(role))
        {
            String info = getFullMethodName(CLS_NM, methodName) + " User [" + session.getUserId() + "] Role [" + role.getName() + "] role already activated.";
            throw new SecurityException(GlobalErrIds.ARLE_ALREADY_ACTIVE, info);
        }

        User ue = userP.read(session.getUser(), true);
        List<UserAdminRole> uRoles = ue.getAdminRoles();
        int indx;
        // Is the admin role activation target valid for this user?
        if (!VUtil.isNotNullOrEmpty(uRoles) || ((indx = uRoles.indexOf(role)) == -1))
        {
            String info = getFullMethodName(CLS_NM, methodName) + " Admin Role [" + role.getName() + "] User [" + session.getUserId() + "] adminRole not authorized for user.";
            throw new SecurityException(GlobalErrIds.ARLE_ACTIVATE_FAILED, info);
        }
        SDUtil.validateDSD(session, role);

        // now activate the role to the session:
        session.setRole(uRoles.get(indx));
    }

    /**
     * This function deactivates adminRole from the active adminRole set of a session owned by a given user.
     * The function is valid if and only if the user is a member of the USERS data set, the
     * session object contains a valid Fortress session, the session is owned by the user,
     * and the adminRole is an active adminRole of that session.
     *
     * @param session object contains the user's returned RBAC and ARBAC sessions from the createSession method.
     * @param role    object contains the adminRole name to be deactivated.
     * @throws us.jts.fortress.SecurityException is thrown if user is not allowed to deactivate or runtime error occurs with system.
     */
    @Override
    public void dropActiveRole(Session session, UserAdminRole role)
        throws SecurityException
    {
        String methodName = "dropActiveRole";
        assertContext(CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL);
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ARLE_NULL);
        role.setUserId(session.getUserId());
        List<UserAdminRole> roles = session.getAdminRoles();
        VUtil.assertNotNull(roles, GlobalErrIds.ARLE_DEACTIVE_FAILED, methodName);
        int indx = roles.indexOf(role);
        if (indx != -1)
        {
            roles.remove(role);
        }
        else
        {
            String info = methodName + " Admin Role [" + role.getName() + "] User [" + session.getUserId() + "], not previously activated";
            throw new SecurityException(GlobalErrIds.ARLE_NOT_ACTIVE, info);
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
        return session.getAdminRoles();
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
        String methodName = "authorizedAdminRoles";
        assertContext(CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL);
        assertContext(CLS_NM, methodName, session.getUser(), GlobalErrIds.USER_NULL);
        return AdminRoleUtil.getInheritedRoles(session.getAdminRoles(), this.contextId);
    }

    /**
     * This helper function processes ARBAC URA "can assign".
     * @param session
     * @param user
     * @param role
     * @return boolean
     * @throws SecurityException
     */
    private boolean checkUserRole(Session session, User user, Role role)
        throws SecurityException
    {
        boolean result = false;
        List<UserAdminRole> uaRoles = session.getAdminRoles();
        if(VUtil.isNotNullOrEmpty(uaRoles))
        {
            // validate user and retrieve user' ou:
            User ue = userP.read(user, false);
            for(UserAdminRole uaRole : uaRoles)
            {
                if(uaRole.getName().equalsIgnoreCase(SUPER_ADMIN))
                {
                    result = true;
                    break;
                }
                Set<String> osUs = uaRole.getOsU();
                if(VUtil.isNotNullOrEmpty(osUs))
                {
                    // create Set with case insensitive comparator:
                    Set<String> osUsFinal = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
                    for(String osU : osUs)
                    {
                        // Add osU children to the set:
                        osUsFinal.add(osU);
                        Set<String> children = UsoUtil.getDescendants(osU, this.contextId);
                        osUsFinal.addAll(children);
                    }
                    // does the admin role have authority over the user object?
                    if(osUsFinal.contains(ue.getOu()))
                    {
                        // Get the Role range for admin role:
                        Set<String> range;
                        if(!uaRole.getBeginRange().equalsIgnoreCase(uaRole.getEndRange()))
                        {
                            range = RoleUtil.getAscendants(uaRole.getBeginRange(), uaRole.getEndRange(), uaRole.isEndInclusive(), this.contextId);
                            if(uaRole.isBeginInclusive())
                            {
                                range.add(uaRole.getBeginRange());
                            }
                            if(VUtil.isNotNullOrEmpty(range))
                            {
                                // Does admin role have authority over a role contained with the allowable role range?
                                if(range.contains(role.getName()))
                                {
                                    result = true;
                                    break;
                                }
                            }
                        }
                        // Does admin role have authority over the role?
                        else if(uaRole.getBeginRange().equalsIgnoreCase(role.getName()))
                        {
                            result = true;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * This helper function processes ARBAC PRA "can assign".
     * @param session
     * @param role
     * @param perm
     * @return boolean
     * @throws SecurityException
     */
    private boolean checkRolePermission(Session session, Role role, Permission perm)
        throws SecurityException
    {
        boolean result = false;
        List<UserAdminRole> uaRoles = session.getAdminRoles();
        if(VUtil.isNotNullOrEmpty(uaRoles))
        {
            // validate perm and retrieve perm's ou:
            PermObj inObj = new PermObj(perm.getObjectName());
            inObj.setContextId(contextId);
            PermObj pObj = permP.read(inObj);
            for(UserAdminRole uaRole : uaRoles)
            {
                if(uaRole.getName().equalsIgnoreCase(SUPER_ADMIN))
                {
                    result = true;
                    break;
                }
                Set<String> osPs = uaRole.getOsP();
                if(VUtil.isNotNullOrEmpty(osPs))
                {
                    // create Set with case insensitive comparator:
                    Set<String> osPsFinal = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
                    for(String osP : osPs)
                    {
                        // Add osU children to the set:
                        osPsFinal.add(osP);
                        Set<String> children = PsoUtil.getDescendants(osP, this.contextId);
                        osPsFinal.addAll(children);
                    }
                    // does the admin role have authority over the perm object?
                    if(osPsFinal.contains(pObj.getOu()))
                    {
                        // Get the Role range for admin role:
                        Set<String> range;
                        if(!uaRole.getBeginRange().equalsIgnoreCase(uaRole.getEndRange()))
                        {
                            range = RoleUtil.getAscendants(uaRole.getBeginRange(), uaRole.getEndRange(), uaRole.isEndInclusive(), this.contextId);
                            if(uaRole.isBeginInclusive())
                            {
                                range.add(uaRole.getBeginRange());
                            }
                            if(VUtil.isNotNullOrEmpty(range))
                            {
                                // Does admin role have authority over a role contained with the allowable role range?
                                if(range.contains(role.getName()))
                                {
                                    result = true;
                                    break;
                                }
                            }
                        }
                        // Does admin role have authority over the role?
                        else if(uaRole.getBeginRange().equalsIgnoreCase(role.getName()))
                        {
                            result = true;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }
}