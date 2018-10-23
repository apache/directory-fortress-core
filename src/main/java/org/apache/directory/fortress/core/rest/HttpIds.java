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
package org.apache.directory.fortress.core.rest;

/**
 * Contains global HttpIds that map to the Fortress REST server.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class HttpIds
{
    public static final String RBAC_AUTHN = "rbacAuthN";
    public static final String RBAC_CREATE = "rbacCreate";
    public static final String RBAC_CHECK = "rbacCheck";
    public static final String RBAC_CHECK_ROLE = "rbacCheckRole";
    public static final String RBAC_CREATE_TRUSTED = "rbacCreateT";
    public static final String RBAC_AUTHZ = "rbacAuthZ";
    public static final String RBAC_PERMS = "rbacPerms";
    public static final String RBAC_ROLES = "rbacRoles";
    public static final String RBAC_AUTHZ_ROLES = "rbacAuthzRoles";
    public static final String RBAC_ADD = "rbacAdd";
    public static final String RBAC_DROP = "rbacDrop";
    public static final String RBAC_USERID = "rbacUserId";
    public static final String RBAC_USER = "rbacUser";
    public static final String USER_READ = "userRead";
    public static final String USER_UPDATE = "userUpdate";
    public static final String USER_CHGPW = "userChange";
    public static final String USER_LOCK = "userLock";
    public static final String USER_UNLOCK = "userUnlock";
    public static final String USER_RESET = "userReset";
    public static final String USER_ADD = "userAdd";
    public static final String USER_DELETE = "userDelete";
    public static final String USER_DISABLE = "userDisable";
    public static final String USER_SEARCH = "userSearch";
    public static final String USER_PERMS = "userPerms";
    public static final String USER_GRANT = "userGrant";
    public static final String USER_REVOKE = "userRevoke";
    public static final String USER_ASGNED = "userAsigned";
    public static final String USER_ASGNED_CONSTRAINTS = "userAsignedConstraints";
    public static final String USER_ASGNED_CONSTRAINTS_KEY = "userAsignedConstraintsKey";
    public static final String USER_AUTHZED = "userAuthzed";
    public static final String USER_ASGNED_ADMIN = "userAsignedAdmin";
    public static final String ROLE_READ = "roleRead";
    public static final String ROLE_UPDATE = "roleUpdate";
    public static final String ROLE_ADD = "roleAdd";
    public static final String ROLE_DELETE = "roleDelete";
    public static final String ROLE_SEARCH = "roleSearch";
    public static final String ROLE_ASGN = "roleAsgn";
    public static final String ROLE_DEASGN = "roleDeasgn";
    public static final String ROLE_GRANT = "roleGrant";
    public static final String ROLE_REVOKE = "roleRevoke";
    public static final String ROLE_PERMS = "rolePerms";
    public static final String ROLE_PERM_ATTR_SETS = "rolePermAttrSets";
    public static final String ROLE_DESC = "roleDescendant";
    public static final String ROLE_ASC = "roleAscendent";
    public static final String ROLE_ADDINHERIT = "roleAddinherit";
    public static final String ROLE_DELINHERIT = "roleDelinherit";
    public static final String ROLE_ASGNED = "roleAsigned";
    public static final String ROLE_AUTHZED = "roleAuthzed";
    public static final String ROLE_ADD_CONSTRAINT = "addRoleConstraint";
    public static final String ROLE_DELETE_CONSTRAINT = "removeRoleConstraint";
    public static final String ROLE_ENABLE_CONSTRAINT = "roleEnableConstraint";
    public static final String ROLE_DISABLE_CONSTRAINT = "roleDisableConstraint";
    public static final String ROLE_DELETE_CONSTRAINT_ID = "removeRoleConstraintId";
    public static final String ROLE_FIND_CONSTRAINTS = "findRoleConstraints";
    public static final String OBJ_READ = "objRead";
    public static final String OBJ_UPDATE = "objUpdate";
    public static final String OBJ_ADD = "objAdd";
    public static final String OBJ_DELETE = "objDelete";
    public static final String OBJ_SEARCH = "objSearch";
    public static final String PERM_READ = "permRead";
    public static final String PERM_UPDATE = "permUpdate";
    public static final String PERM_ADD = "permAdd";
    public static final String PERM_DELETE = "permDelete";
    public static final String PERM_SEARCH = "permSearch";
    public static final String PERM_OBJ_SEARCH = "permObjSearch";
    public static final String PERM_SEARCH_ANY = "permSearchAny";
    public static final String PERM_ROLES = "permRoles";
    public static final String PERM_USERS = "permUsers";
    public static final String PERM_ROLES_AUTHZED = "permRolesAuthzed";
    public static final String PERM_USERS_AUTHZED = "permUsersAuthzed";
    public static final String PERM_ADD_ATTRIBUTE_SET = "addPermissionAttributeSet";
    public static final String PERM_DELETE_ATTRIBUTE_SET = "deletePermissionAttributeSet";
    public static final String PERM_ADD_PERM_ATTRIBUTE_TO_SET = "addPermissionAttributeToSet";
    public static final String PERM_DELETE_PERM_ATTRIBUTE_TO_SET = "removePermissionAttributeFromSet";
    public static final String PERM_UPDATE_PERM_ATTRIBUTE_IN_SET = "updatePermissionAttributeInSet";
    public static final String PERM_READ_PERM_ATTRIBUTE_SET = "readPermAttributeSet";
    public static final String ORG_READ = "orgRead";
    public static final String ORG_UPDATE = "orgUpdate";
    public static final String ORG_ADD = "orgAdd";
    public static final String ORG_DELETE = "orgDelete";
    public static final String ORG_DESC = "orgDescendant";
    public static final String ORG_ASC = "orgAscendent";
    public static final String ORG_ADDINHERIT = "orgAddinherit";
    public static final String ORG_DELINHERIT = "orgDelinherit";
    public static final String ORG_SEARCH = "orgSearch";
    public static final String SSD_ROLE_SETS = "ssdRoleSets";
    public static final String SSD_SETS = "ssdSets";
    public static final String SSD_READ = "ssdRead";
    public static final String SSD_ROLES = "ssdRoles";
    public static final String SSD_CARD = "ssdCard";
    public static final String SSD_ADD = "ssdAdd";
    public static final String SSD_UPDATE = "ssdUpdate";
    public static final String SSD_DELETE = "ssdDelete";
    public static final String SSD_ADD_MEMBER = "ssdAddMember";
    public static final String SSD_DEL_MEMBER = "ssdDelMember";
    public static final String SSD_CARD_UPDATE = "ssdCardUpdate";
    public static final String DSD_ROLE_SETS = "dsdRoleSets";
    public static final String DSD_SETS = "dsdSets";
    public static final String DSD_READ = "dsdRead";
    public static final String DSD_ROLES = "dsdRoles";
    public static final String DSD_CARD = "dsdCard";
    public static final String DSD_ADD = "dsdAdd";
    public static final String DSD_UPDATE = "dsdUpdate";
    public static final String DSD_DELETE = "dsdDelete";
    public static final String DSD_ADD_MEMBER = "dsdAddMember";
    public static final String DSD_DEL_MEMBER = "dsdDelMember";
    public static final String DSD_CARD_UPDATE = "dsdCardUpdate";
    public static final String ADMIN_AUTHZ = "adminAuthZ";
    public static final String ADMIN_ROLES = "adminRoles";
    public static final String ADMIN_PERMS = "adminPerms";
    public static final String ADMIN_AUTHZ_ROLES = "adminAuthzRoles";
    public static final String ADMIN_ADD = "adminAdd";
    public static final String ADMIN_DROP = "adminDrop";
    public static final String ADMIN_ASSIGN = "adminAssign";
    public static final String ADMIN_DEASSIGN = "adminDeassign";
    public static final String ADMIN_GRANT = "adminGrant";
    public static final String ADMIN_REVOKE = "adminRevoke";
    public static final String ARLE_READ = "arleRead";
    public static final String ARLE_UPDATE = "arleUpdate";
    public static final String ARLE_ADD = "arleAdd";
    public static final String ARLE_DELETE = "arleDelete";
    public static final String ARLE_SEARCH = "arleSearch";
    public static final String ARLE_DESC = "arleDescendant";
    public static final String ARLE_ASC = "arleAscendent";
    public static final String ARLE_ADDINHERIT = "arleAddinherit";
    public static final String ARLE_DELINHERIT = "arleDelinherit";
    public static final String ARLE_ASGN = "arleAsgn";
    public static final String ARLE_DEASGN = "arleDeasgn";
    public static final String ARLE_ASGNED = "arleAsigned";
    public static final String PSWD_READ = "pswdRead";
    public static final String PSWD_UPDATE = "pswdUpdate";
    public static final String PSWD_ADD = "pswdAdd";
    public static final String PSWD_DELETE = "pswdDelete";
    public static final String PSWD_SEARCH = "pswdSearch";
    public static final String PSWD_USER_ADD = "pswdUserAdd";
    public static final String PSWD_USER_DELETE = "pswdUserDelete";
    public static final String AUDIT_BINDS = "auditBinds";
    public static final String AUDIT_AUTHZS = "auditAuthzs";
    public static final String AUDIT_UAUTHZS = "auditUserAuthzs";
    public static final String AUDIT_SESSIONS = "auditSessions";
    public static final String AUDIT_MODS = "auditMods";
    public static final String AUDIT_INVLD = "auditInvld";
    public static final String CFG_ADD = "cfgAdd";
    public static final String CFG_UPDATE = "cfgUpdate";
    public static final String CFG_DELETE = "cfgDelete";
    public static final String CFG_READ = "cfgRead";
    public static final String RBAC_CREATE_GROUP_SESSION = "rbacCreateGroup";
    public static final String GROUP_ADD = "groupAdd";
    public static final String GROUP_READ = "groupRead";
    public static final String GROUP_UPDATE = "groupUpdate";
    public static final String GROUP_DELETE = "groupDelete";
    public static final String GROUP_ROLE_ASGNED = "roleGroupAsigned";
    public static final String GROUP_ASGNED = "groupAsigned";
    public static final String GROUP_ASGN = "groupAsgn";
    public static final String GROUP_DEASGN = "groupDeasgn";
}