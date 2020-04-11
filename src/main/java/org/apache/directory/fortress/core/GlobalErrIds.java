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

/**
 * This module contains error identifiers that are used when exception need be thrown.
 * The Fortress Manager APIs declare {@code SecurityException} as thrown even though the child exception may vary according 
 * to type:
 * <ul>
 *   <li>{@link CfgException} in the event the cfg of runtime fails.</li>
 *   <li>{@link CreateException} in the event DAO cannot create entity.</li>
 *   <li>{@link FinderException} in the event DAO cannot find the entity.</li>
 *   <li>{@link PasswordException} in the event user fails password checks.</li>
 *   <li>{@link RemoveException} in the event DAO cannot remove entity.</li>
 *   <li>{@link SecurityException} in the event security check fails.</li>
 *   <li>{@link UpdateException} in the event DAO cannot update entity.</li>
 *   <li>{@link ValidationException} in the event entity validation fails.</li>
 * </ul>
 * <p>
 * All Fortress public Manager Impl APIs will throw exception derived from {@link SecurityException} and containing
 * an id that maps to one of these error codes.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class GlobalErrIds
{
    /**
     * Private constructor
     *
     */
    private GlobalErrIds()
    {
    }

    /** Default value when we have no error */
    public static final int NO_ERROR = 0;
    
    /**
     * Group 1 - Configuration Errors:
     */

    /**
     * The supplied context is required but was null or empty.
     */
    public static final int CONTEXT_NULL = 101;

    /**
     * The Manager impl could not be instantiated because the supplied class name was not found.
     */
    public static final int FT_MGR_CLASS_NOT_FOUND = 103;

    /**
     * The reflection API could not create the Manager instance due to instantiation exception.
     */
    public static final int FT_MGR_INST_EXCEPTION = 104;

    /**
     * The reflection API could not create the Manager instance due to illegal access exception.
     */
    public static final int FT_MGR_ILLEGAL_ACCESS = 105;

    /**
     * The Manager impl class name was not found in the cfg.
     */
    public static final int FT_MGR_CLASS_NAME_NULL = 106;

    /**
     * The remote cfg instance could not be found on the ldap server.
     */
    public static final int FT_CONFIG_NOT_FOUND = 107;

    /**
     * The cfg node name was not found in cfg file.
     */
    public static final int FT_CONFIG_NAME_NULL = 108;

    /**
     * The name specified for config instance is too long.  Check to ensure length is less than {@link GlobalIds#OU_LEN}.
     */
    public static final int FT_CONFIG_NAME_INVLD = 109;

    /**
     * The config API was called with an empty properties list.
     */
    public static final int FT_CONFIG_PROPS_NULL = 110;

    /**
     * The cfg object is required but was passed in null.
     */
    public static final int FT_CONFIG_NULL = 111;

    /**
     * The config node could not be created on ldap server.
     */
    public static final int FT_CONFIG_CREATE_FAILED = 120;

    /**
     * The config node could not be updated on ldap server.
     */
    public static final int FT_CONFIG_UPDATE_FAILED = 121;

    /**
     * The config node could not be removed from ldap.
     */
    public static final int FT_CONFIG_DELETE_FAILED = 122;

    /**
     * The config parameters could not be removed from ldap.
     */
    public static final int FT_CONFIG_DELETE_PROPS_FAILED = 123;

    /**
     * The config node could not be read from ldap.
     */
    public static final int FT_CONFIG_READ_FAILED = 124;

    /**
     * The config node could not be created because it already exists.
     */
    public static final int FT_CONFIG_ALREADY_EXISTS = 125;

    /**
     * The config node could not be read from ldap.
     */
    public static final int FT_CONFIG_BOOTSTRAP_FAILED = 126;

    /**
     * The config node could not be read from ldap.
     */
    public static final int FT_CONFIG_INITIALIZE_FAILED = 127;

    /**
     * The resource could not be located on the runtime classloader path.
     */
    public static final int FT_RESOURCE_NOT_FOUND = 128;

    /**
     * The Fortress cache is not configured.
     */
    public static final int FT_CACHE_NOT_CONFIGURED = 129;

    /**
     * The Fortress cache get operation failed.
     */
    public static final int FT_CACHE_GET_ERR = 130;

    /**
     * The Fortress cache put operation failed.
     */
    public static final int FT_CACHE_PUT_ERR = 131;

    /**
     * The Fortress cache clear operation failed.
     */
    public static final int FT_CACHE_CLEAR_ERR = 132;

    /**
     * The Fortress cache flush operation failed.
     */
    public static final int FT_CACHE_FLUSH_ERR = 133;

    /**
     * The Fortress cache is null.
     */
    public static final int FT_NULL_CACHE = 134;

    /**
     * Could not initialize Apache LDAP Connection Pool.
     */
    public static final int FT_APACHE_LDAP_POOL_INIT_FAILED = 135;

    /**
     * Cannot load JSSE TrustStore because the full-qualified input file name is null.
     */
    public static final int FT_CONFIG_JSSE_TRUSTSTORE_NULL = 136;

    /**
     * 1000's - User Entity Rule and LDAP Errors
     */

    /**
     * The User node could not be searched in ldap.
     */
    public static final int USER_SEARCH_FAILED = 1000;

    /**
     * The User node could not be read from ldap.
     */
    public static final int USER_READ_FAILED = 1001;

    /**
     * The User node could not be added to ldap.
     */
    public static final int USER_ADD_FAILED = 1002;

    /**
     * The User node could not be updated in ldap.
     */
    public static final int USER_UPDATE_FAILED = 1003;

    /**
     * The User node could not be deleted from ldap.
     */
    public static final int USER_DELETE_FAILED = 1004;

    /**
     * The User node was not found in ldap.
     */
    public static final int USER_NOT_FOUND = 1005;

    /**
     * The supplied userId was null and is required for this operation.
     */
    public static final int USER_ID_NULL = 1006;

    /**
     * The User could not be added because it already exists in ldap.
     */
    public static final int USER_ID_DUPLICATE = 1007;

    /**
     * The operation failed because the supplied User entity is null and is required.
     */
    public static final int USER_NULL = 1008;

    /**
     * The operation failed because the supplied User's password is required but was found to be null.
     */
    public static final int USER_PW_NULL = 1009;

    /**
     * The operation failed because the supplied User password is too long.  Ensure the length does not exceed 
     * {@link GlobalIds#PASSWORD_LEN}.
     */
    public static final int USER_PW_INVLD_LEN = 1010;

    /**
     * The operation failed because of policy violation due to being designated a 'system' user..
     */
    public static final int USER_PLCY_VIOLATION = 1011;

    /**
     * The PW Policy node could not be removed from ldap.
     */
    public static final int USER_PW_PLCY_DEL_FAILED = 1012;

    /**
     * The supplied User password was invalid.
     */
    public static final int USER_PW_INVLD = 1013;

    /**
     * The User password check failed in ldap.
     */
    public static final int USER_PW_CHK_FAILED = 1014;

    /**
     * The User's password is in reset state which requires change.
     */
    public static final int USER_PW_RESET = 1015;

    /**
     * Authentication failed because User's password has been locked on the server.
     */
    public static final int USER_PW_LOCKED = 1016;

    /**
     * Authentication failed because User's password is expired on the server.
     */
    public static final int USER_PW_EXPIRED = 1017;

    /**
     * The password change failed because User is not allowed to change password.
     */
    public static final int USER_PW_MOD_NOT_ALLOWED = 1018;

    /**
     * The password change failed because User did not supply old password.
     */
    public static final int USER_PW_MUST_SUPPLY_OLD = 1019;

    /**
     * The password change failed because supplied password is not of sufficient quality.
     */
    public static final int USER_PW_NSF_QUALITY = 1020;

    /**
     * The password change failed because supplied User password was too short.
     */
    public static final int USER_PW_TOO_SHORT = 1021;

    /**
     * The password change failed because the supplied User password is too young.
     */
    public static final int USER_PW_TOO_YOUNG = 1022;

    /**
     * The password change failed because supplied User password was found in history.
     */
    public static final int USER_PW_IN_HISTORY = 1023;

    /**
     * The password unlock operation failed on the server.
     */
    public static final int USER_PW_UNLOCK_FAILED = 1024;

    /**
     * The password lock operation failed on the server.
     */
    public static final int USER_PW_LOCK_FAILED = 1025;

    /**
     * The password change failed on the server.
     */
    public static final int USER_PW_CHANGE_FAILED = 1026;

    /**
     * The reset password operation failed on the server.
     */
    public static final int USER_PW_RESET_FAILED = 1027;

    /**
     * The User has been prevented to logon due to Fortress Constraints.
     */
    public static final int USER_LOCKED_BY_CONST = 1028;

    /**
     * The User session could not be created.
     */
    public static final int USER_SESS_CREATE_FAILED = 1029;

    /**
     * The required User Session was not supplied and is required.
     */
    public static final int USER_SESS_NULL = 1030;

    /**
     * The logged on administrator is not authorized to perform the function.
     */
    public static final int USER_ADMIN_NOT_AUTHORIZED = 1031;

    /**
     * The common name was not supplied but is required.
     */
    public static final int USER_CN_NULL = 1032;

    /**
     * The surname was not supplied but is required.
     */
    public static final int USER_SN_NULL = 1033;

    /**
     * The policy name supplied for User was not found on server.
     */
    public static final int USER_PW_PLCY_INVALID = 1034;

    /**
     * The User ou name supplied for User was not found on server.
     */
    public static final int USER_OU_INVALID = 1035;

    /**
     * The required User Session was not supplied and is required.  Used by fortress realm.
     */
    public static final int SESS_CTXT_NULL = 1036;

    /**
     * The User bind operation failed on server.
     */
    public static final int USER_BIND_FAILED = 1037;

    /**
     * The User node could not be added to ldap.
     */
    public static final int USER_ADD_FAILED_ALREADY_EXISTS = 1038;

    /**
     * The Admin is not authorized to add the User.
     */
    public static final int USER_ADMIN_CANNOT_ADD = 1039;

    /**
     * The Admin is not authorized to edit the User.
     */
    public static final int USER_ADMIN_CANNOT_CHANGE = 1040;
    /**
     * The User ou name supplied for User was not found on server.
     */
    public static final int USER_OU_NULL = 1041;

    /**
     * 2000's User-Role assignments
     */

    /**
     * The UserRole entity was not supplied but is required.
     */
    public static final int URLE_NULL = 2003;

    /**
     * The User RBAC Role assignment failed.
     */
    public static final int URLE_ASSIGN_FAILED = 2004;

    /**
     * The User RBAC Role deassignment failed.
     */
    public static final int URLE_DEASSIGN_FAILED = 2005;

    /**
     * The supplied RBAC Role could not be activated in the User's Session.
     */
    public static final int URLE_ACTIVATE_FAILED = 2006;

    /**
     * The supplied RBAC Role could not be deactivated from the User's session.
     */
    public static final int URLE_DEACTIVE_FAILED = 2007;

    /**
     * The RBAC Role could not be assigned to User already has it assigned.
     */
    public static final int URLE_ASSIGN_EXIST = 2008;

    /**
     * The RBAC Role could not be deassigned from User because it wasn't assigned in the first place.
     */
    public static final int URLE_ASSIGN_NOT_EXIST = 2009;

    /**
     * The RBAC Role search could failed on server.
     */
    public static final int URLE_SEARCH_FAILED = 2010;

    /**
     * The RBAC Role is already activated in User's Session.
     */
    public static final int URLE_ALREADY_ACTIVE = 2011;

    /**
     * The supplied RBAC Role is not activated in User's Session.
     */
    public static final int URLE_NOT_ACTIVE = 2022;

    /**
     * The logged on administrator cannot assign RBAC Role to User because unauthorized.
     */
    public static final int URLE_ADMIN_CANNOT_ASSIGN = 2023;

    /**
     * The logged on administrator cannot deassign RBAC Role to User because unauthorized.
     */
    public static final int URLE_ADMIN_CANNOT_DEASSIGN = 2024;

    /**
     * The logged on administrator cannot grant RBAC Role to Permission because unauthorized.
     */
    public static final int URLE_ADMIN_CANNOT_GRANT = 2025;

    /**
     * The logged on administrator cannot revoke RBAC Role from Permission because unauthorized.
     */
    public static final int URLE_ADMIN_CANNOT_REVOKE = 2026;

    /**
     * Temporal Constraint Activation Violations
     */

    /**
     * Entity activation failed due to day validation failure.
     */
    public static final int ACTV_FAILED_DAY = 2050;

    /**
     * Entity activation failed due to date validation failure.
     */
    public static final int ACTV_FAILED_DATE = 2051;

    /**
     * Entity activation failed due to time validation failure.
     */
    public static final int ACTV_FAILED_TIME = 2052;

    /**
     * Entity activation failed due to Session timeout validation failure.
     */
    public static final int ACTV_FAILED_TIMEOUT = 2053;

    /**
     * Entity activation failed due to lockout date.
     */
    public static final int ACTV_FAILED_LOCK = 2054;

    /**
     * Entity activation failed due to Dynamic Separation of Duty restriction on Role.
     */
    public static final int ACTV_FAILED_DSD = 2055;

    /**
     * Entity activation failed due to authentication status violation.
     */
    public static final int ACTV_FAILED_AUTHN = 2056;

    /**
     * Entity activation failed during dynamic discriminator test on Role.
     */
    public static final int ACTV_FAILED_DISCRIMINANT = 2057;

    /**
     * Entity activation failed during dynamic discriminator test on Role.
     */
    public static final int ACTV_FAILED_ABAC = 2058;
    /**
     * Entity activation failed, the ABAC constraint key was not found in user context.
     */
    public static final int ACTV_FAILED_ABAC_NO_KEY_FOUND = 2059;
    /**
     * 3000's - Permission Entity
     */

    /**
     * The supplied Permission could not be searched due to server failure.
     */
    public static final int PERM_SEARCH_FAILED = 3000;

    /**
     * The supplied Permission operation could not be read due to server failure.
     */
    public static final int PERM_READ_OP_FAILED = 3001;

    /**
     * The supplied Permission object could not be read due to server failure.
     */
    public static final int PERM_READ_OBJ_FAILED = 3002;

    /**
     * The supplied Permission could not be added to ldap server.
     */
    public static final int PERM_ADD_FAILED = 3003;

    /**
     * The supplied Permission could not be updated on ldap server.
     */
    public static final int PERM_UPDATE_FAILED = 3004;

    /**
     * The supplied Permission could not be removed from ldap server.
     */
    public static final int PERM_DELETE_FAILED = 3005;

    /**
     * The supplied Permission operation could not be found on ldap server.
     */
    public static final int PERM_OP_NOT_FOUND = 3006;

    /**
     * The supplied Permission object could not be found on ldap server.
     */
    public static final int PERM_OBJ_NOT_FOUND = 3007;

    /**
     * The supplied Permission entity is required but was passed as null.
     */
    public static final int PERM_NULL = 3008;

    /**
     * The supplied Permission operation is required but was passed as null.
     */
    public static final int PERM_OPERATION_NULL = 3009;

    /**
     * The supplied Permission object is required but was passed as null.
     */
    public static final int PERM_OBJECT_NULL = 3010;

    /**
     * The Permission could not be added because it already exists on ldap server.
     */
    public static final int PERM_DUPLICATE = 3011;

    /**
     * The Permission could not be granted to Role.
     */
    public static final int PERM_GRANT_FAILED = 3012;

    /**
     * The Permission could not be granted to User.
     */
    public static final int PERM_GRANT_USER_FAILED = 3013;

    /**
     * The Permission could not be revoked from Role.
     */
    public static final int PERM_REVOKE_FAILED = 3024;

    /**
     * The Permission has already been granted.
     */
    public static final int PERM_ROLE_EXIST = 3015;

    /**
     * The Permission could not be revoked because it does not exist on server.
     */
    public static final int PERM_ROLE_NOT_EXIST = 3016;

    /**
     * The Permission could not be granted to User because it already exists on server.
     */
    public static final int PERM_USER_EXIST = 3017;

    /**
     * The Permission could not be revoked from User because it does not exist on server.
     */
    public static final int PERM_USER_NOT_EXIST = 3018;

    /**
     * The Role-Permission search failed on server.
     */
    public static final int PERM_ROLE_SEARCH_FAILED = 3019;

    /**
     * The User-Permission search failed on ldap server.
     */
    public static final int PERM_USER_SEARCH_FAILED = 3020;

    /**
     * The search for authorized Permission failed on server.
     */
    public static final int PERM_SESS_SEARCH_FAILED = 3021;

    /**
     * The operation to revoke all Permissions from User failed on server.
     */
    public static final int PERM_BULK_USER_REVOKE_FAILED = 3022;

    /**
     * The operation to revoke all Permissions from RBAC Role failed on server.
     */
    public static final int PERM_BULK_ROLE_REVOKE_FAILED = 3023;

    /**
     * The operation to revoke all administrative Permissions from AdminRole failed on server.
     */
    public static final int PERM_BULK_ADMINROLE_REVOKE_FAILED = 3024;

    /**
     * The supplied Perm OU was not found on ldap server.
     */
    public static final int PERM_OU_INVALID = 3025;

    /**
     * The supplied Permission operation name is required but was passed as null.
     */
    public static final int PERM_OPERATION_NM_NULL = 3026;

    /**
     * The supplied Permission object name is required but was passed as null.
     */
    public static final int PERM_OBJECT_NM_NULL = 3027;
    /**
     * The supplied Permission operation could not be read due to server failure.
     */
    public static final int PERM_COMPARE_OP_FAILED = 3028;
    /**
     * The supplied Permission does not exist in LDAP DIT.
     */
    public static final int PERM_NOT_EXIST = 3029;
    /**
     * The supplied Permission Attribute Set is required but was passed as null.
     */
    public static final int PERM_ATTRIBUTE_SET_NULL = 3030;
    /**
     * The supplied Permission Attribute Set could not be removed from ldap server.
     */
    public static final int PERM_ATTRIBUTE_SET_DELETE_FAILED = 3031;
    /**
     * The supplied Permission Attribute is required but was passed as null.
     */
    public static final int PERM_ATTRIBUTE_NULL = 3032;
    /**
     * The supplied Permission Attribute could not be removed from ldap server.
     */
    public static final int PERM_ATTRIBUTE_DELETE_FAILED = 3033;
    /**
     * The supplied Permission Attribute could not be added to ldap server.
     */
    public static final int PERM_ATTR_ADD_FAILED = 3034;
    /**
     * The supplied Permission Attribute Set could not be found on ldap server.
     */
    public static final int PERM_ATTRIBUTE_SET_NOT_FOUND = 3035;
    /**
     * The supplied Permission Attribute Set name is required but was passed as null.
     */
    public static final int PERM_ATTRIBUTE_SET_NM_NULL = 3036;
    /**
     * The supplied Permission Attribute was not found
     */
    public static final int PERM_ATTRIBUTE_NOT_FOUND = 3037;
    /**
     * The supplied Permission Attribute update failed
     */
    public static final int PERM_ATTRIBUTE_UPDATE_FAILED = 3038;
    
    /**
     * 4000's - Password Policy Entity
     */

    /**
     * The Password policy could not be read from ldap server.
     */
    public static final int PSWD_READ_FAILED = 4000;

    /**
     * The supplied Password policy could not be added to ldap server.
     */
    public static final int PSWD_CREATE_FAILED = 4001;

    /**
     * The supplied Password policy could not be updated on ldap server.
     */
    public static final int PSWD_UPDATE_FAILED = 4002;

    /**
     * The supplied Password policy could not be removed from ldap server.
     */
    public static final int PSWD_DELETE_FAILED = 4003;

    /**
     * The supplied Password policy could not be searched on ldap server.
     */
    public static final int PSWD_SEARCH_FAILED = 4004;

    /**
     * The supplied Password policy was not found on ldap server.
     */
    public static final int PSWD_NOT_FOUND = 4005;

    /**
     * The supplied Password policy name failed length check.  Ensure that does not exceed 
     * {@link GlobalIds#PWPOLICY_NAME_LEN}.
     */
    public static final int PSWD_NAME_INVLD_LEN = 4006;

    /**
     * The supplied Password quality value failed length check.
     */
    public static final int PSWD_QLTY_INVLD_LEN = 4007;

    /**
     * The supplied Password quality value invalid.
     */
    public static final int PSWD_QLTY_INVLD = 4008;

    /**
     * The supplied Password max age value is invalid.
     */
    public static final int PSWD_MAXAGE_INVLD = 4009;

    /**
     * The supplied Password min age value is invalid.
     */
    public static final int PSWD_MINAGE_INVLD = 4010;

    /**
     * The supplied Password minLength is invalid.
     */
    public static final int PSWD_MINLEN_INVLD = 4011;

    /**
     * The supplied Password interval value invalid.
     */
    public static final int PSWD_INTERVAL_INVLD = 4012;

    /**
     * The Password max failure count invalid.
     */
    public static final int PSWD_MAXFAIL_INVLD = 4013;

    /**
     * The Password must change value invalid.
     */
    public static final int PSWD_MUSTCHG_INVLD = 4014;

    /**
     * The supplied Password safe change attribute invalid.
     */
    public static final int PSWD_SAFECHG_INVLD = 4015;

    /**
     * The supplied Password allow change attribute invalid.
     */
    public static final int PSWD_ALLOWCHG_INVLD = 4016;

    /**
     * The supplied Password history value invalid.
     */
    public static final int PSWD_HISTORY_INVLD = 4017;

    /**
     * The supplied Password grace value invalid.
     */
    public static final int PSWD_GRACE_INVLD = 4018;

    /**
     * The supplied lockout duration was invalid.
     */
    public static final int PSWD_LOCKOUTDUR_INVLD = 4019;

    /**
     * The supplied password expiration value was invalid.
     */
    public static final int PSWD_EXPWARN_INVLD = 4020;

    /**
     * The supplied password lockout attribute is invalid.
     */
    public static final int PSWD_LOCKOUT_INVLD = 4021;

    /**
     * The supplied Password policy name is required and cannot be null.
     */
    public static final int PSWD_NAME_NULL = 4022;

    /**
     * The supplied Password entity is required and cannot be null.
     */
    public static final int PSWD_PLCY_NULL = 4023;

    /**
     * Server returned password policy constraint violation.
     */
    public static final int PSWD_CONST_VIOLATION = 4024;


    /**
     * 5000's - RBAC
     */

    /**
     * Role Rule and System errors
     */

    /**
     * The RBAC Role search failed on ldap server.
     */
    public static final int ROLE_SEARCH_FAILED = 5000;

    /**
     * The RBAC Role read failed on ldap server.
     */
    public static final int ROLE_READ_FAILED = 5001;

    /**
     * The supplied RBAC Role could not be added to ldap server.
     */
    public static final int ROLE_ADD_FAILED = 5002;

    /**
     * The supplied RBAC Role could not be updated on ldap server.
     */
    public static final int ROLE_UPDATE_FAILED = 5003;

    /**
     * The supplied RBAC Role could not be removed from ldap server.
     */
    public static final int ROLE_DELETE_FAILED = 5004;

    /**
     * The RBAC Role name is required and cannot be null.
     */
    public static final int ROLE_NM_NULL = 5005;

    /**
     * The RBAC Role was not found on ldap server.
     */
    public static final int ROLE_NOT_FOUND = 5006;

    /**
     * The RBAC Role entity is required and cannot be null
     */
    public static final int ROLE_NULL = 5007;

    /**
     * The RBAC Role assignment failed on the ldap server.
     */
    public static final int ROLE_USER_ASSIGN_FAILED = 5008;

    /**
     * The RBAC Role deassignment operation failed on the ldap server.
     */
    public static final int ROLE_USER_DEASSIGN_FAILED = 5009;

    /**
     * The RBAC Role list is required and cannot be null.
     */
    public static final int ROLE_LST_NULL = 5010;

    /**
     * Role occupant search failed.
     */
    public static final int ROLE_OCCUPANT_SEARCH_FAILED = 5011;

    /**
     * The operation to remove User as occupant to Roles failed..
     */
    public static final int ROLE_REMOVE_OCCUPANT_FAILED = 5012;

    /**
     * The RBAC Parent Role entity is required for this operation and cannot be null
     */
    public static final int PARENT_ROLE_NULL = 5013;

    /**
     * The RBAC Child Role entity is required for this operation and cannot be null
     */
    public static final int CHILD_ROLE_NULL = 5014;

    /**
     * The operation to remove parent attribute to Role failed..
     */
    public static final int ROLE_REMOVE_PARENT_FAILED = 5015;
    /**
     * Hierarchical Constraints
     */

    /**
     * The Hierarchical node could not be read from the ldap server.
     */
    public static final int HIER_READ_FAILED = 5051;

    /**
     * The supplied Hierarchical data could not be added to the ldap server.
     */
    public static final int HIER_ADD_FAILED = 5052;

    /**
     * The supplied Hierarchical data could not be updated on the ldap server.
     */
    public static final int HIER_UPDATE_FAILED = 5053;

    /**
     * The supplied Hierarchical data could not be removed from the ldap server.
     */
    public static final int HIER_DELETE_FAILED = 5054;

    /**
     * The requested Hierarchical data could not be found on the ldap server.
     */
    public static final int HIER_NOT_FOUND = 5056;

    /**
     * The specified relationship is invalid.
     */
    public static final int HIER_REL_INVLD = 5057;

    /**
     * The parent cannot be removed from ldap server because it has child.  Must remove child first.
     */
    public static final int HIER_DEL_FAILED_HAS_CHILD = 5058;

    /**
     * The specified parent-child relationship already exists on the server.
     */
    public static final int HIER_REL_EXIST = 5059;

    /**
     * The specified parent-child relationship does not exist on the server.
     */
    public static final int HIER_REL_NOT_EXIST = 5060;

    /**
     * The supplied Hierarchical entity is required and cannot be null.
     */
    public static final int HIER_NULL = 5061;

    /**
     * The supplied Hierarchical type is required and cannot be null.
     */
    public static final int HIER_TYPE_NULL = 5062;

    /**
     * The supplied Hierarchical type is required and cannot be null.
     */
    public static final int HIER_CANNOT_PERFORM = 5063;

    /**
     * The specified relationship would cause a cyclic dependency in graph.
     */
    public static final int HIER_REL_CYCLIC = 5064;


    /**
     * Separation of Duty Relations
     */

    /**
     * The Static Separation of Duty constraint search failed on the ldap server.
     */
    public static final int SSD_SEARCH_FAILED = 5080;

    /**
     * The Static Separation of Duty constraint read failed on the ldap server.
     */
    public static final int SSD_READ_FAILED = 5081;

    /**
     * The Static Separation of Duty constraint cannot be added to the ldap server.
     */
    public static final int SSD_ADD_FAILED = 5082;

    /**
     * The Static Separation of Duty constraint cannot be updated on the ldap server.
     */
    public static final int SSD_UPDATE_FAILED = 5083;

    /**
     * The Static Separation of Duty constraint cannot be removed from the ldap server.
     */
    public static final int SSD_DELETE_FAILED = 5084;

    /**
     * The Static Separation of Duty name is required and cannot be null.
     */
    public static final int SSD_NM_NULL = 5085;

    /**
     * The Static Separation of Duty constraint could not be found on the ldap server.
     */
    public static final int SSD_NOT_FOUND = 5086;

    /**
     * The Static Separation of Duty Constraint entity is required and cannot be null.
     */
    public static final int SSD_NULL = 5087;

    /**
     * The validation for Static Separation of Duty constraint data failed.
     */
    public static final int SSD_VALIDATION_FAILED = 5088;

    /**
     * The Dynamic Separation of Duty constraint search failed on the ldap server.
     */
    public static final int DSD_SEARCH_FAILED = 5089;

    /**
     * The Dynamic Separation of Duty constraint read failed on the ldap server.
     */
    public static final int DSD_READ_FAILED = 5090;

    /**
     * The Dynamic Separation of Duty constraint cannot be added to the ldap server.
     */
    public static final int DSD_ADD_FAILED = 5091;

    /**
     * The Dynamic Separation of Duty constraint cannot be updated on the ldap server.
     */
    public static final int DSD_UPDATE_FAILED = 5092;

    /**
     * The Dynamic Separation of Duty constraint cannot be removed from the ldap server.
     */
    public static final int DSD_DELETE_FAILED = 5093;

    /**
     * The Dynamic Separation of Duty name is required and cannot be null.
     */
    public static final int DSD_NM_NULL = 5094;

    /**
     * The Dynamic Separation of Duty constraint could not be found on the ldap server.
     */
    public static final int DSD_NOT_FOUND = 5095;

    /**
     * The Dynamic Separation of Duty Constraint entity is required and cannot be null.
     */
    public static final int DSD_NULL = 5096;

    /**
     * The validation for Dynamic Separation of Duty constraint data failed.
     */
    public static final int DSD_VALIDATION_FAILED = 5097;

    /**
     * 5100's - Role Constraint Errors
     */
    
    /**
     * The validation for Role Constraint type failed.
     */
    public static final int ROLE_CONSTRAINT_TYPE_NULL = 5100;
    /**
     * The validation for Role Constraint value failed.
     */
    public static final int ROLE_CONSTRAINT_VALUE_NULL = 5101;
    /**
     * The validation for Role Constraint entity reference not set.
     */
    public static final int ROLE_CONSTRAINT_NULL = 5102;
    /**
     * The validation for Role Constraint key is required.
     */
    public static final int ROLE_CONSTRAINT_KEY_NULL = 5103;
    /**
     * An attempt to add a user-role constraint when the role constraint has not been enabled (added).
     */
    public static final int ROLE_CONSTRAINT_NOT_ENABLED = 5104;

    
    /**
     * 6000's - LDAP Suffix and Container Entities
     */

    /**
     * The supplied ldap subdirectory node could not be created on ldap server.
     */
    public static final int CNTR_CREATE_FAILED = 6001;

    /**
     * The supplied ldap subdirectory node could not be removed from the ldap server.
     */
    public static final int CNTR_DELETE_FAILED = 6002;

    /**
     * The subdirectory node name is required and cannot be null.
     */
    public static final int CNTR_NAME_NULL = 6003;

    /**
     * The subdirectory node name failed length check.
     */
    public static final int CNTR_NAME_INVLD = 6004;

    /**
     * The supplied parent node name is required and cannot be null.
     */
    public static final int CNTR_PARENT_NULL = 6005;

    /**
     * The supplied parent node name is invalid.
     */
    public static final int CNTR_PARENT_INVLD = 6006;

    /**
     * The ldap suffix could not be created on the ldap server.
     */
    public static final int SUFX_CREATE_FAILED = 6010;

    /**
     * The ldap suffix node could not be removed from the ldap server.
     */
    public static final int SUFX_DELETE_FAILED = 6011;

    /**
     * The suffix name is required and cannot be null.
     */
    public static final int SUFX_NAME_NULL = 6012;

    /**
     * The supplied suffix name failed length check.
     */
    public static final int SUFX_NAME_INVLD = 6013;

    /**
     * The suffix domain component top level qualifier is required and cannot be null.
     */
    public static final int SUFX_DCTOP_NULL = 6014;

    /**
     * The Suffix domain component top level qualifier name failed the length check.
     */
    public static final int SUFX_DCTOP_INVLD = 6015;


    /**
     * 7000's - Audit Activities
     */

    /**
     * The audit bind search failed on the ldap server.
     */
    public static final int AUDT_BIND_SEARCH_FAILED = 7000;

    /**
     * The Audit input entity is required on this method and cannot be supplied as null
     */
    public static final int AUDT_INPUT_NULL = 7001;

    /**
     * The Audit search for Fortress authorization records failed on the ldap server.
     */
    public static final int AUDT_AUTHZ_SEARCH_FAILED = 7002;

    /**
     * The Audit search for modifications failed on the ldap server.
     */
    public static final int AUDT_MOD_SEARCH_FAILED = 7003;

    /**
     * The Audit mod search by administrator internal id failed on ldap server.
     */
    public static final int AUDT_MOD_ADMIN_SEARCH_FAILED = 7004;

    /**
     * The Audit search for authentication events failed on the ldap server.
     */
    public static final int AUDT_AUTHN_INVALID_FAILED = 7005;


    /**
     * 8000's Organizational Unit Rule and System errors
     */

    /**
     * The supplied organization unity entity is required and cannot be null.
     */
    public static final int ORG_NULL = 8001;

    /**
     * The supplied {@link org.apache.directory.fortress.core.model.OrgUnit#type} is required and cannot be null.
     */
    public static final int ORG_TYPE_NULL = 8002;

    /**
     * The supplied User OU entity could not be read due to ldap error.
     */
    public static final int ORG_READ_FAILED_USER = 8011;

    /**
     * The supplied User OU entity could not be added due to ldap error.
     */
    public static final int ORG_ADD_FAILED_USER = 8012;

    /**
     * The supplied User OU entity could not be updated due to ldap error.
     */
    public static final int ORG_UPDATE_FAILED_USER = 8013;

    /**
     * The supplied User OU entity could not be removed due to ldap error.
     */
    public static final int ORG_DELETE_FAILED_USER = 8014;

    /**
     * The OU User search failed due to ldap error.
     */
    public static final int ORG_SEARCH_FAILED_USER = 8015;

    /**
     * The search for User OU's failed on ldap server.
     */
    public static final int ORG_GET_FAILED_USER = 8016;

    /**
     * The supplied User OU not found on ldap server.
     */
    public static final int ORG_NOT_FOUND_USER = 8017;

    /**
     * The supplied User OU is required and cannot be null.
     */
    public static final int ORG_NULL_USER = 8018;

    /**
     * The supplied User OU type is required and cannot be null.
     */
    public static final int ORG_TYPE_NULL_USER = 8019;

    /**
     * The supplied User OU entry could not be removed from the server.
     */
    public static final int ORG_DEL_FAILED_USER = 8020;

    /**
     * The supplied Perm OU entity parent attribute could not be updated due to ldap error.
     */
    public static final int ORG_REMOVE_PARENT_FAILED_USER = 8021;

    /**
     * The supplied Perm OU entity could not be read due to ldap error.
     */
    public static final int ORG_READ_FAILED_PERM = 8061;

    /**
     * The supplied Perm OU entity could not be added due to ldap error.
     */
    public static final int ORG_ADD_FAILED_PERM = 8062;

    /**
     * The supplied Perm OU entity could not be updated due to ldap error.
     */
    public static final int ORG_UPDATE_FAILED_PERM = 8063;

    /**
     * The supplied Perm OU entity could not be removed due to ldap error.
     */
    public static final int ORG_DELETE_FAILED_PERM = 8064;

    /**
     * The OU Perm search failed due to ldap error.
     */
    public static final int ORG_SEARCH_FAILED_PERM = 8065;

    /**
     * The search for Perm OU's failed on ldap server.
     */
    public static final int ORG_GET_FAILED_PERM = 8066;

    /**
     * The supplied Perm OU not found on ldap server.
     */
    public static final int ORG_NOT_FOUND_PERM = 8067;

    /**
     * The supplied Perm OU is required and cannot be null.
     */
    public static final int ORG_NULL_PERM = 8068;

    /**
     * The supplied Perm OU type is required and cannot be null.
     */
    public static final int ORG_TYPE_NULL_PERM = 8069;

    /**
     * The supplied Perm OU entry could not be removed from the server.
     */
    public static final int ORG_DEL_FAILED_PERM = 8070;

    /**
     * The supplied OU name exceed maximum allowed {@link GlobalIds#OU_LEN}.
     */
    public static final int ORG_LEN_INVLD = 8071;

    /**
     * The supplied Parent OU is required for this operation and cannot be null.
     */
    public static final int ORG_PARENT_NULL = 8072;

    /**
     * The supplied Parent OU is required for this operation and cannot be null.
     */
    public static final int ORG_CHILD_NULL = 8073;

    /**
     * The supplied Perm OU parent attribute could not be removed due to ldap error.
     */
    public static final int ORG_REMOVE_PARENT_FAILED_PERM = 8074;

    /**
     * 9000's Administrative RBAC
     */

    /**
     * The Administrative Role search failed on ldap server.
     */
    public static final int ARLE_SEARCH_FAILED = 9000;

    /**
     * The Administrative Role read failed on ldap server.
     */
    public static final int ARLE_READ_FAILED = 9001;

    /**
     * The supplied Administrative Role could not be added to ldap server.
     */
    public static final int ARLE_ADD_FAILED = 9002;

    /**
     * The supplied Administrative Role could not be updated on ldap server.
     */
    public static final int ARLE_UPDATE_FAILED = 9003;

    /**
     * The supplied Administrative Role could not be removed from ldap server.
     */
    public static final int ARLE_DELETE_FAILED = 9004;

    /**
     * The Administrative Role name is required and cannot be null.
     */
    public static final int ARLE_NM_NULL = 9005;

    /**
     * The Administrative Role was not found on ldap server.
     */
    public static final int ARLE_NOT_FOUND = 9006;

    /**
     * The Administrative Role entity is required and cannot be null
     */
    public static final int ARLE_NULL = 9007;

    /**
     * The User Administrative Role assignment failed.
     */
    public static final int ARLE_USER_ASSIGN_FAILED = 9008;

    /**
     * The User Administrative Role deassignment failed.
     */
    public static final int ARLE_USER_DEASSIGN_FAILED = 9009;

    /**
     * Method requires list of Administrative Roles and cannot be null.
     */
    public static final int ARLE_LST_NULL = 9010;

    /**
     * The supplied begin range for Administrative Role is required and cannot be null.
     */
    public static final int ARLE_BEGIN_RANGE_NULL = 9011;

    /**
     * The supplied end range for Administrative Role is required and cannot be null.
     */
    public static final int ARLE_END_RANGE_NULL = 9011;

    /**
     * The supplied range for Administrative Role is invalid.
     */
    public static final int ARLE_INVLD_RANGE = 9012;

    /**
     * The supplied range for Administrative Role inclusion is invalid.
     */
    public static final int ARLE_INVLD_RANGE_INCLUSIVE = 9013;

    /**
     * The supplied Administrative Role could not be activated in the User's Session.
     */
    public static final int ARLE_ACTIVATE_FAILED = 9014;

    /**
     * The supplied Administrative Role could not be deactivated from the User's session.
     */
    public static final int ARLE_DEACTIVE_FAILED = 9015;

    /**
     * The Administrative Role is already activated in User's Session.
     */
    public static final int ARLE_ALREADY_ACTIVE = 9016;

    /**
     * The supplied Administrative Role is not activated in User's Session.
     */
    public static final int ARLE_NOT_ACTIVE = 9017;

    /**
     * The Administrative Role search could failed on server.
     */
    public static final int ARLE_USER_SEARCH_FAILED = 9018;

    /**
     * The Parent Administrative Role entity is required and cannot be null
     */
    public static final int ARLE_PARENT_NULL = 9019;

    /**
     * The Child Administrative Role entity is required and cannot be null
     */
    public static final int ARLE_CHILD_NULL = 9020;

    /**
     * The Admin Role could not be assigned to User already has it assigned.
     */
    public static final int ARLE_ASSIGN_EXIST = 9021;

    /**
     * The User Admin Role could not be deassigned from User because it wasn't assigned in the first place.
     */
    public static final int ARLE_ASSIGN_NOT_EXIST = 9022;

    /**
     * The User Admin Role could not be deassigned from User because it wasn't assigned in the first place.
     */
    public static final int ARLE_DEASSIGN_NOT_EXIST = 9023;

    /**
     * The User Admin Role assignment failed.
     */
    public static final int ARLE_ASSIGN_FAILED = 9024;

    /**
     * The User Admin Role deassignment failed.
     */
    public static final int ARLE_DEASSIGN_FAILED = 9025;

    /**
     * AdminRole occupant search failed.
     */
    public static final int ARLE_OCCUPANT_SEARCH_FAILED = 9026;

    /**
     * The operation to remove User as occupant to AdminRoles failed..
     */
    public static final int ARLE_REMOVE_OCCUPANT_FAILED = 9027;

    /**
     * The supplied Administrative Role parent attribute could not be removed on ldap server.
     */
    public static final int ARLE_REMOVE_PARENT_FAILED = 9028;

    /**
     * 10000's - Temporal Constraint Validation Error Ids
     */

    /**
     * The constraint contains invalid text.
     */
    public static final int CONST_INVLD_TEXT = 10001;

    /**
     * The Constraint value failed length check.
     */
    public static final int CONST_INVLD_FIELD_LEN = 10002;

    /**
     * The Constraint contains an invalid timeout value.
     */
    public static final int CONST_TIMEOUT_INVLD = 10003;

    /**
     * The Constraint contains an invalid beginTime value.
     */
    public static final int CONST_BEGINTIME_INVLD = 10004;

    /**
     * The Constraint contains an invalid beginTime length.
     */
    public static final int CONST_BEGINTIME_LEN_ERR = 10005;

    /**
     * The Constraint contains an invalid endTime value.
     */
    public static final int CONST_ENDTIME_INVLD = 10006;

    /**
     * The Constraint contains an invalid endTime length.
     */
    public static final int CONST_ENDTIME_LEN_ERR = 10007;

    /**
     * The Constraint contains an invalid beginDate value.
     */
    public static final int CONST_BEGINDATE_INVLD = 10008;

    /**
     * The Constraint contains an invalid beginDate length.
     */
    public static final int CONST_BEGINDATE_NULL = 10009;

    /**
     * The Constraint contains an invalid endDate value.
     */
    public static final int CONST_ENDDATE_INVLD = 10010;

    /**
     * The Constraint contains an invalid endDate length.
     */
    public static final int CONST_ENDDATE_NULL = 10011;

    /**
     * The Constraint contains an invalid dayMask value.
     */
    public static final int CONST_DAYMASK_INVLD = 10012;

    /**
     * The Constraint contains a null dayMask value.
     */
    public static final int CONST_DAYMASK_NULL = 10013;

    /**
     * The Constraint description is optional but cannot exceed length of {@link GlobalIds#DESC_LEN} if supplied.
     */
    public static final int CONST_DESC_LEN_INVLD = 10014;

    /**
     * The Constraint contains a null value.
     */
    public static final int CONST_NULL_TEXT = 10015;

    /**
     * 10100's - REST Error Ids
     */

    /**
     * The REST function failed with HTTP error.
     */
    public static final int REST_WEB_ERR = 10101;

    /**
     * The REST function failed with an IO error.
     */
    public static final int REST_IO_ERR = 10102;

    /**
     * The REST function failed during XML marshaling.
     */
    public static final int REST_MARSHALL_ERR = 10103;

    /**
     * The REST function failed during XML unmarshal
     */
    public static final int REST_UNMARSHALL_ERR = 10104;

    /**
     * The REST fucntion failed with HTTP Get.
     */
    public static final int REST_GET_FAILED = 10105;

    /**
     * The REST endpoint was not found.
     */
    public static final int REST_NOT_FOUND_ERR = 10106;

    /**
     *  The REST function failed with an unknown error.
     */
    public static final int REST_UNKNOWN_ERR = 10107;

    /**
     * The REST function failed with HTTP forbidden error.
     */
    public static final int REST_FORBIDDEN_ERR = 10108;

    /**
     * The REST function failed with an HTTP unauthorized error.
     */
    public static final int REST_UNAUTHORIZED_ERR = 10109;

    /**
     * The REST function could not get handle to HTTP Request.
     */
    public static final int REST_NULL_HTTP_REQ_ERR = 10110;

    /**
     * The REST function failed with an HTTP 500 Internal error.
     */
    public static final int REST_INTERNAL_ERR = 10111;

    /**
     * The REST function failed with an HTTP 400 Validation Exception.
     */
    public static final int REST_VALIDATION_ERR = 10112;


    /**
     * 10200's - RBAC Accelerator Error Ids
     */

    /**
     * The RBAC Accelerator function failed because CreateSession LDAP extended operation error.
     */
    public static final int ACEL_CREATE_SESSION_ERR = 10201;

    /**
     * The RBAC Accelerator function failed because DeleteSession LDAP extended operation error.
     */
    public static final int ACEL_DELETE_SESSION_ERR = 10202;

    /**
     * The RBAC Accelerator function failed because CheckAccess LDAP extended operation error.
     */
    public static final int ACEL_CHECK_ACCESS_ERR = 10203;

    /**
     * The RBAC Accelerator function failed because AddRole LDAP extended operation error.
     */
    public static final int ACEL_ADD_ROLE_ERR = 10204;

    /**
     * The RBAC Accelerator function failed because DropRole LDAP extended operation error.
     */
    public static final int ACEL_DROP_ROLE_ERR = 10205;

    /**
     * The RBAC Accelerator function failed because SessionRoles LDAP extended operation error.
     */
    public static final int ACEL_SESSION_ROLES_ERR = 10206;
    /**
     * 10300's - Group Error Ids
     */
    /**
     * The Group search failed on ldap server.
     */
    public static final int GROUP_SEARCH_FAILED = 10300;

    /**
     * The Group read failed on ldap server.
     */
    public static final int GROUP_READ_FAILED = 10301;

    /**
     * The supplied Group could not be added to ldap server.
     */
    public static final int GROUP_ADD_FAILED = 10302;

    /**
     * The supplied Group could not be updated on ldap server.
     */
    public static final int GROUP_UPDATE_FAILED = 10303;

    /**
     * The supplied Group could not be removed from ldap server.
     */
    public static final int GROUP_DELETE_FAILED = 10304;

    /**
     * The supplied Group could not be updated on ldap server.
     */
    public static final int GROUP_ADD_PROPERTY_FAILED = 10305;

    /**
     * The supplied Group could not be removed from ldap server.
     */
    public static final int GROUP_DELETE_PROPERTY_FAILED = 10306;

    /**
     * The Group was not found on ldap server.
     */
    public static final int GROUP_NOT_FOUND = 10307;

    /**
     * The Group entity is required and cannot be null
     */
    public static final int GROUP_NULL = 10308;

    /**
     * The Group assignment failed on the ldap server.
     */
    public static final int GROUP_USER_ASSIGN_FAILED = 10309;

    /**
     * The Group deassignment operation failed on the ldap server.
     */
    public static final int GROUP_USER_DEASSIGN_FAILED = 10310;

    /**
     * The group name is required and cannot be null.
     */
    public static final int GROUP_NAME_NULL = 10311;

    /**
     * The supplied group name failed length check.
     */
    public static final int GROUP_NAME_INVLD = 10312;

    /**
     * The supplied group protocol name failed length check.
     */
    public static final int GROUP_PROTOCOL_INVLD = 10313;

    /**
     * The supplied group type is invalid for operation
     */
    public static final int GROUP_TYPE_INVLD = 10314;

    /**
     * The Group member is required and cannot be null
     */
    public static final int GROUP_MEMBER_NULL = 10315;

    /**
     * 10400's - ROLE CONSTRAINT Error Ids
     */

    /**
     * The RoleConstraint entity was not supplied but is required.
     */
    public static final int RCON_NULL = 10401;
    
    /**
     * The RoleConstraint entity was not found
     */
    public static final int RCON_NOT_FOUND = 10402;

    /**
     * 10500's - Property Management Error Ids
     */

    /**
     * The entity does not support properties
     */
    public static final int ENTITY_PROP_NOT_SUPPORTED = 10501;
    
    /**
     * The entity was not found
     */
    public static final int ENTITY_PROPS_NOT_FOUND = 10502;

    /**
     * The entity was not found
     */
    public static final int ENTITY_PROPS_LOAD_FAILED = 10503;
}
