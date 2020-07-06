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

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.fortress.annotation.AdminPermissionOperation;
import org.apache.directory.fortress.core.DelAccessMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserAdminRole;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * This class implements the ARBAC02 DelAccessMgr interface for performing runtime delegated access control operations on 
 * objects that are provisioned Fortress ARBAC entities that reside in LDAP directory.  These APIs map directly to similar 
 * named APIs specified by ARBAC02 functions.  The ARBAC Functional specification describes delegated administrative 
 * operations for the creation and maintenance of ARBAC element sets and relations.  Delegated administrative review 
 * functions for performing administrative queries and system functions for creating and managing ARBAC attributes on user 
 * sessions and making delegated administrative access control decisions.
 *
 * This class also extends the RBAC AccessMgrImpl object which is used for performing runtime session creation and
 * access control decisions based on behalf of administrative user who is logged onto the system.  
 * (See the AccessMgr javadoc for more info of how RBAC works).
 *
 * This class provides both sets of functionality as is necessary to fulfill runtime delegated administrative access control 
 * functionality within RBAC provisioning systems.
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="../doc-files/ARbac.png" alt="">
 * <p>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises 
 * the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without 
 * sacrificing regulations for accountability or traceability.
 * <p>
 * This class is NOT thread safe if parent instance variables ({@link #contextId} or {@link #adminSess}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DelAccessMgrImpl extends AccessMgrImpl implements DelAccessMgr, Serializable
{
    private  final String CLS_NM = DelAccessMgrImpl.class.getName();
    private UserP userP;
    private PermP permP;
    private String SUPER_ADMIN;
    private String REST_ADMIN;

    public DelAccessMgrImpl()
    {
        userP = new UserP();
        permP = new PermP();
        SUPER_ADMIN = Config.getInstance().getProperty("superadmin.role", "fortress-core-super-admin");
        REST_ADMIN = Config.getInstance().getProperty("serviceadmin.role", "fortress-rest-admin");
    }
    
    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public boolean canAdd(Session session, User user)
            throws SecurityException
    {
        String methodName = "canAssign";
        assertContext(CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL);
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        VUtil.assertNotNullOrEmpty(user.getOu(), GlobalErrIds.USER_OU_NULL, methodName);
        return checkUser(session, user, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canEdit(Session session, User user)
            throws SecurityException
    {
        String methodName = "canAssign";
        assertContext(CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL);
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        return checkUser(session, user, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkAccess(Session session, Permission perm)
        throws SecurityException
    {
        String methodName =  "checkAccess";
        assertContext(CLS_NM, methodName, perm, GlobalErrIds.PERM_NULL);
        VUtil.assertNotNullOrEmpty(perm.getOpName(), GlobalErrIds.PERM_OPERATION_NULL, methodName);
        VUtil.assertNotNullOrEmpty(perm.getObjName(), GlobalErrIds.PERM_OBJECT_NULL, methodName);
        assertContext(CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL);
        // This flag set will check administrative permission data set.
        perm.setAdmin(true);
        return super.checkAccess(session, perm);
    }


    /**
     * {@inheritDoc}
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
        if (!CollectionUtils.isNotEmpty( uRoles ) || ((indx = uRoles.indexOf(role)) == -1))
        {
            String info = getFullMethodName(CLS_NM, methodName) + " Admin Role [" + role.getName() + "] User [" + session.getUserId() + "] adminRole not authorized for user.";
            throw new SecurityException(GlobalErrIds.ARLE_ACTIVATE_FAILED, info);
        }
        SDUtil.getInstance().validateDSD( session, role );

        // now activate the role to the session:
        session.setRole(uRoles.get(indx));
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<UserAdminRole> sessionAdminRoles(Session session)
        throws SecurityException
    {
        String methodName = "sessionAdminRoles";
        VUtil.assertNotNull(session, GlobalErrIds.USER_SESS_NULL, CLS_NM + ".sessionAdminRoles");
        setEntitySession(CLS_NM, methodName, session);
        return session.getAdminRoles();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Set<String> authorizedAdminRoles(Session session)
        throws SecurityException
    {
        String methodName = "authorizedAdminRoles";
        assertContext(CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL);
        assertContext( CLS_NM, methodName, session.getUser(), GlobalErrIds.USER_NULL );
        setEntitySession(CLS_NM, methodName, session);
        return AdminRoleUtil.getInheritedRoles( session.getAdminRoles(), this.contextId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<Permission> sessionPermissions(Session session)
        throws SecurityException
    {
        String methodName = "sessionPermissions";
        assertContext(CLS_NM, methodName, session, GlobalErrIds.USER_SESS_NULL);
        VUtil.getInstance().validateConstraints( session, VUtil.ConstraintType.USER, false );
        VUtil.getInstance().validateConstraints( session, VUtil.ConstraintType.ROLE, false );
        setEntitySession(CLS_NM, methodName, session);
        return permP.search( session, true );
    }

    /**
     * This helper function processes "can do".
     * @param session
     * @param user
     * @return boolean
     * @throws SecurityException
     */
    private boolean checkUser(Session session, User user, boolean isAdd)
        throws SecurityException
    {
        boolean result = false;
        List<UserAdminRole> uaRoles = session.getAdminRoles();
        if(CollectionUtils.isNotEmpty( uaRoles ))
        {
            // validate user and retrieve user' ou:
            User ue;
            if(!isAdd)
            {
                ue = userP.read(user, false);
            }
            else
            {
                ue = user;
            }

            for(UserAdminRole uaRole : uaRoles)
            {
                if(uaRole.getName().equalsIgnoreCase(SUPER_ADMIN))
                {
                    result = true;
                    break;
                }
                Set<String> osUs = uaRole.getOsUSet();
                if(CollectionUtils.isNotEmpty( osUs ))
                {
                    // create Set with case insensitive comparator:
                    Set<String> osUsFinal = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
                    for(String osU : osUs)
                    {
                        // Add osU children to the set:
                        osUsFinal.add(osU);
                        Set<String> children = UsoUtil.getInstance().getDescendants( osU, this.contextId );
                        osUsFinal.addAll(children);
                    }
                    // does the admin role have authority over the user object?
                    if(osUsFinal.contains(ue.getOu()))
                    {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
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
        if(CollectionUtils.isNotEmpty( uaRoles ))
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
                Set<String> osUs = uaRole.getOsUSet();
                if(CollectionUtils.isNotEmpty( osUs ))
                {
                    // create Set with case insensitive comparator:
                    Set<String> osUsFinal = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
                    for(String osU : osUs)
                    {
                        // Add osU children to the set:
                        osUsFinal.add(osU);
                        Set<String> children = UsoUtil.getInstance().getDescendants( osU, this.contextId );
                        osUsFinal.addAll(children);
                    }
                    // does the admin role have authority over the user object?
                    if(osUsFinal.contains(ue.getOu()))
                    {
                        // Get the Role range for admin role:
                        Set<String> range;
                        if(uaRole.getName().equalsIgnoreCase(REST_ADMIN))
                        {
                            result = true;
                            break;
                        }
                        else if(uaRole.getBeginRange() != null && uaRole.getEndRange() != null && !uaRole.getBeginRange().equalsIgnoreCase(uaRole.getEndRange()))
                        {
                            range = RoleUtil.getInstance().getAscendants( uaRole.getBeginRange(), uaRole.getEndRange(),
                                uaRole.isEndInclusive(), this.contextId );
                            if(uaRole.isBeginInclusive())
                            {
                                range.add(uaRole.getBeginRange());
                            }
                            if(CollectionUtils.isNotEmpty( range ))
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
                        else if(uaRole.getBeginRange() != null && uaRole.getBeginRange().equalsIgnoreCase(role.getName()))
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
        if(CollectionUtils.isNotEmpty( uaRoles ))
        {
            // validate perm and retrieve perm's ou:
            PermObj inObj = new PermObj(perm.getObjName());
            inObj.setContextId(contextId);
            PermObj pObj = permP.read(inObj);
            for(UserAdminRole uaRole : uaRoles)
            {
                if(uaRole.getName().equalsIgnoreCase(SUPER_ADMIN))
                {
                    result = true;
                    break;
                }
                Set<String> osPs = uaRole.getOsPSet();
                if(CollectionUtils.isNotEmpty( osPs ))
                {
                    // create Set with case insensitive comparator:
                    Set<String> osPsFinal = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
                    for(String osP : osPs)
                    {
                        // Add osU children to the set:
                        osPsFinal.add(osP);
                        Set<String> children = PsoUtil.getInstance().getDescendants( osP, this.contextId );
                        osPsFinal.addAll(children);
                    }
                    // does the admin role have authority over the perm object?
                    if(osPsFinal.contains(pObj.getOu()))
                    {
                        // Get the Role range for admin role:
                        Set<String> range;
                        if(uaRole.getName().equalsIgnoreCase(REST_ADMIN))
                        {
                            result = true;
                            break;
                        }
                        else if(uaRole.getBeginRange() != null && uaRole.getEndRange() != null && !uaRole.getBeginRange().equalsIgnoreCase(uaRole.getEndRange()))
                        {
                            range = RoleUtil.getInstance().getAscendants(uaRole.getBeginRange(), uaRole.getEndRange(), uaRole.isEndInclusive(), this.contextId);
                            if(uaRole.isBeginInclusive())
                            {
                                range.add(uaRole.getBeginRange());
                            }
                            if( CollectionUtils.isNotEmpty( range ))
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
                        else if(uaRole.getBeginRange() != null && uaRole.getBeginRange().equalsIgnoreCase(role.getName()))
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