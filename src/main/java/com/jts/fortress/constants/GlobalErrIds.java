/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.constants;

/**
 * This module contains error identifiers that are used when exception need be thrown.
 * The Fortress Manager APIs declare {@code SecurityException} as thrown even though the child exception may vary according to type:
 * <ul>
 * <li>{@link com.jts.fortress.ConfigurationException} in the event the configuration of runtime fails.
 * <li>{@link com.jts.fortress.CreateException} in the event DAO cannot create entity.
 * <li>{@link com.jts.fortress.FinderException} in the event DAO cannot find the entity.
 * <li>{@link com.jts.fortress.PasswordException} in the event user fails password checks.
 * <li>{@link com.jts.fortress.RemoveException} in the event DAO cannot remove entity.
 * <li>{@link com.jts.fortress.SecurityException} in the event security check fails.
 * <li>{@link com.jts.fortress.UpdateException} in the event DAO cannot update entity.
 * <li>{@link com.jts.fortress.ValidationException} in the event entity validation fails.
 * </ul>
 * <p/>
 * All Fortress public Manager Impl APIs will throw exception derived from {@link com.jts.fortress.SecurityException} and containing
 * an id that maps to one of these error codes.
 *
 * @author smckinn
 * @created August 24, 2009
 */
public class GlobalErrIds
{
    /**
     * Group 1 - Configuration Errors:
     */

    /**
     * The Manager impl could not be instantiated because the supplied class name was not found.
     *
     */
    public final static int FT_MGR_CLASS_NOT_FOUND = 103;

    /**
     * The reflection API could not create the Manager instance due to instantiation exception.
     */
    public final static int FT_MGR_INST_EXCEPTION = 104;

    /**
     * The reflection API could not create the Manager instance due to illegal access exception.
     */
    public final static int FT_MGR_ILLEGAL_ACCESS = 105;

    /**
     * The Manager impl class name was not found in the configuration.
     */
    public final static int FT_MGR_CLASS_NAME_NULL = 106;

    /**
     * The remote configuration instance could not be found on the ldap server.
     */
    public final static int FT_CONFIG_NOT_FOUND = 107;

    /**
     * The configuration node name was not found in configuration file.
     */
    public final static int FT_CONFIG_NAME_NULL = 108;

    /**
     * The name specified for config instance is too long.  Check to ensure length is less than {@link GlobalIds#OU_LEN}.
     */
    public final static int FT_CONFIG_NAME_INVLD = 109;

    /**
     * The config API was called with an empty properties list.
     */
    public final static int FT_CONFIG_PROPS_NULL = 110;

    /**
     * The config node could not be created on ldap server.
     */
    public final static int FT_CONFIG_CREATE_FAILED = 120;

    /**
     * The config node could not be updated on ldap server.
     */
    public final static int FT_CONFIG_UPDATE_FAILED = 121;

    /**
     * The config node could not be removed from ldap.
     */
    public final static int FT_CONFIG_DELETE_FAILED = 122;

    /**
     * The config parameters could not be removed from ldap.
     */
    public final static int FT_CONFIG_DELETE_PROPS_FAILED = 123;

    /**
     * The config node could not be read from ldap.
     */
    public final static int FT_CONFIG_READ_FAILED = 124;

    /**
     * The config node could not be created because it already exists.
     */
    public final static int FT_CONFIG_ALREADY_EXISTS = 125;

    /**
     * The config node could not be read from ldap.
     */
    public final static int FT_CONFIG_BOOTSTRAP_FAILED = 126;

    /**
     * The config node could not be read from ldap.
     */
    public final static int FT_CONFIG_INITIALIZE_FAILED = 127;

    /**
     * 1000's - User Entity Rule and LDAP Errors
     */

    /**
     * The User node could not be searched in ldap.
     */
    public final static int USER_SEARCH_FAILED = 1000;

    /**
     * The User node could not be read from ldap.
     */
    public final static int USER_READ_FAILED = 1001;

    /**
     * The User node could not be added to ldap.
     */
    public final static int USER_ADD_FAILED = 1002;

    /**
     * The User node could not be updated in ldap.
     */
    public final static int USER_UPDATE_FAILED = 1003;

    /**
     * The User node could not be deleted from ldap.
     */
    public final static int USER_DELETE_FAILED = 1004;

    /**
     * The User node was not found in ldap.
     */
    public final static int USER_NOT_FOUND = 1005;

    /**
     * The supplied userId was null and is required for this operation.
     */
    public final static int USER_ID_NULL = 1006;

    /**
     * The User could not be added because it already exists in ldap.
     */
    public final static int USER_ID_DUPLICATE = 1007;

    /**
     * The operation failed because the supplied User entity is null and is required.
     */
    public final static int USER_NULL = 1008;

    /**
     * The operation failed because the supplied User's password is required but was found to be null.
     */
    public final static int USER_PW_NULL = 1009;

    /**
     * The operation failed because the supplied User password is too long.  Ensure the length does not exceed {@link GlobalIds#PASSWORD_LEN}.
     */
    public final static int USER_PW_INVLD_LEN = 1010;

    /**
     * The operation failed because of password policy violation.
     */
    public final static int USER_PW_PLCY_VIOLATION = 1011;

    /**
     * The PW Policy node could not be removed from ldap.
     */
    public final static int USER_PW_PLCY_DEL_FAILED = 1012;

    /**
     * The supplied User password was invalid.
     */
    public final static int USER_PW_INVLD = 1013;

    /**
     * The User password check failed in ldap.
     */
    public final static int USER_PW_CHK_FAILED = 1014;

    /**
     * The User's password is in reset state which requires change.
     */
    public final static int USER_PW_RESET = 1015;

    /**
     * Authentication failed because User's password has been locked on the server.
     */
    public final static int USER_PW_LOCKED = 1016;

    /**
     * Authentication failed because User's password is expired on the server.
     */
    public final static int USER_PW_EXPIRED = 1017;

    /**
     * The password change failed because User is not allowed to change password.
     */
    public final static int USER_PW_MOD_NOT_ALLOWED = 1018;

    /**
     * The password change failed because User did not supply old password.
     */
    public final static int USER_PW_MUST_SUPPLY_OLD = 1019;

    /**
     * The password change failed because supplied password is not of sufficient quality.
     */
    public final static int USER_PW_NSF_QUALITY = 1020;

    /**
     * The password change failed because supplied User password was too short.
     */
    public final static int USER_PW_TOO_SHORT = 1021;

    /**
     * The password change failed because the supplied User password is too young.
     */
    public final static int USER_PW_TOO_YOUNG = 1022;

    /**
     * The password change failed because supplied User password was found in history.
     */
    public final static int USER_PW_IN_HISTORY = 1023;

    /**
     * The password unlock operation failed on the server.
     */
    public final static int USER_PW_UNLOCK_FAILED = 1024;

    /**
     * The password lock operation failed on the server.
     */
    public final static int USER_PW_LOCK_FAILED = 1025;

    /**
     * The password change failed on the server.
     */
    public final static int USER_PW_CHANGE_FAILED = 1026;

    /**
     * The reset password operation failed on the server.
     */
    public final static int USER_PW_RESET_FAILED = 1027;

    /**
     * The User has been prevented to logon due to Fortress Constraints.
     */
    public final static int USER_LOCKED_BY_CONST = 1028;

    /**
     * The User session could not be created.
     */
    public final static int USER_SESS_CREATE_FAILED = 1029;

    /**
     * The required User Session was not supplied and is required.
     */
    public final static int USER_SESS_NULL = 1030;

    /**
     * The logged on administrator is not authorized to perform the function.
     */
    public final static int USER_ADMIN_NOT_AUTHORIZED = 1031;

    /**
     * The common name was not supplied but is required.
     */
    public final static int USER_CN_NULL = 1032;

    /**
     * The surname was not supplied but is required.
     */
    public final static int USER_SN_NULL = 1033;

    /**
     * The policy name supplied for User was not found on server.
     */
    public final static int USER_PW_PLCY_INVALID = 1034;

    /**
     * The User ou name supplied for User was not found on server.
     */
    public final static int USER_OU_INVALID = 1035;

    /**
     * The required User Session was not supplied and is required.
     */
    public final static int SESS_CTXT_NULL = 1036;

    /**
     * 2000's User-Role assignments
     */

    /**
     * The UserRole entity was not supplied but is required.
     */
    public final static int URLE_NULL = 2003;

    /**
     * The User RBAC Role assignment failed.
     */
    public final static int URLE_ASSIGN_FAILED = 2004;

    /**
     * The User RBAC Role deassignment failed.
     */
    public final static int URLE_DEASSIGN_FAILED = 2005;

    /**
     * The supplied RBAC Role could not be activated in the User's Session.
     */
    public final static int URLE_ACTIVATE_FAILED = 2006;

    /**
     * The supplied RBAC Role could not be deactivated from the User's session.
     */
    public final static int URLE_DEACTIVE_FAILED = 2007;

    /**
     * The RBAC Role could not be assigned to User already has it assigned.
     */
    public final static int URLE_ASSIGN_EXIST = 2008;

    /**
     * The RBAC Role could not be deassigned from User because it wasn't assigned in the first place.
     */
    public final static int URLE_ASSIGN_NOT_EXIST = 2009;

    /**
     * The RBAC Role search could failed on server.
     */
    public final static int URLE_SEARCH_FAILED = 2010;

    /**
     * The RBAC Role is already activated in User's Session.
     */
    public final static int URLE_ALREADY_ACTIVE = 2011;

    /**
     * The supplied RBAC Role is not activated in User's Session.
     */
    public final static int URLE_NOT_ACTIVE = 2022;

    /**
     * The logged on administrator cannot assign RBAC Role to User because unauthorized.
     */
    public final static int URLE_ADMIN_CANNOT_ASSIGN = 2023;

    /**
     * The logged on administrator cannot deassign RBAC Role to User because unauthorized.
     */
    public final static int URLE_ADMIN_CANNOT_DEASSIGN = 2024;

    /**
     * The logged on administrator cannot grant RBAC Role to Permission because unauthorized.
     */
    public final static int URLE_ADMIN_CANNOT_GRANT = 2025;

    /**
     * The logged on administrator cannot revoke RBAC Role from Permission because unauthorized.
     */
    public final static int URLE_ADMIN_CANNOT_REVOKE = 2026;

    /**
     * Temporal Constraint Activation Violations
     */

    /**
     * Entity activation failed due to day validation failure.
     */
    public final static int ACTV_FAILED_DAY = 2050;

    /**
     * Entity activation failed due to date validation failure.
     */
    public final static int ACTV_FAILED_DATE = 2051;

    /**
     * Entity activation failed due to time validation failure.
     */
    public final static int ACTV_FAILED_TIME = 2052;

    /**
     * Entity activation failed due to Session timeout validation failure.
     */
    public final static int ACTV_FAILED_TIMEOUT = 2053;

    /**
     * Entity activation failed due to lockout date.
     */
    public final static int ACTV_FAILED_LOCK = 2054;

    /**
     * Entity activation failed due to Dynamic Separation of Duty restriction on Role.
     */
    public final static int ACTV_FAILED_DSD = 2055;

    /**
     * 3000's - Permission Entity
     */

    /**
     * The supplied Permission could not be searched due to server failure.
     */
    public final static int PERM_SEARCH_FAILED = 3000;

    /**
     * The supplied Permission operation could not be read due to server failure.
     */
    public final static int PERM_READ_OP_FAILED = 3001;

    /**
     * The supplied Permission object could not be read due to server failure.
     */
    public final static int PERM_READ_OBJ_FAILED = 3002;

    /**
     * The supplied Permission could not be added to ldap server.
     */
    public final static int PERM_ADD_FAILED = 3003;

    /**
     * The supplied Permission could not be updated on ldap server.
     */
    public final static int PERM_UPDATE_FAILED = 3004;

    /**
     * The supplied Permission could not be removed from ldap server.
     */
    public final static int PERM_DELETE_FAILED = 3005;

    /**
     * The supplied Permission operation could not be found on ldap server.
     */
    public final static int PERM_OP_NOT_FOUND = 3006;

    /**
     * The supplied Permission object could not be found on ldap server.
     */
    public final static int PERM_OBJ_NOT_FOUND = 3007;

    /**
     * The supplied Permission entity is required but was passed as null.
     */
    public final static int PERM_NULL = 3008;

    /**
     * The supplied Permission operation is required but was passed as null.
     */
    public final static int PERM_OPERATION_NULL = 3009;

    /**
     * The supplied Permission object is required but was passed as null.
     */
    public final static int PERM_OBJECT_NULL = 3010;

    /**
     * The Permission could not be added because it already exists on ldap server.
     */
    public final static int PERM_DUPLICATE = 3011;

    /**
     * The Permission could not be granted to Role.
     */
    public final static int PERM_GRANT_FAILED = 3012;

    /**
     * The Permission could not be granted to User.
     */
    public final static int PERM_GRANT_USER_FAILED = 3013;

    /**
     * The Permission could not be revoked from Role.
     */
    public final static int PERM_REVOKE_FAILED = 3024;

    /**
     * The Permission has already been granted.
     */
    public final static int PERM_ROLE_EXIST = 3015;

    /**
     * The Permission could not be revoked because it does not exist on server.
     */
    public final static int PERM_ROLE_NOT_EXIST = 3016;

    /**
     * The Permission could not be granted to User because it already exists on server.
     */
    public final static int PERM_USER_EXIST = 3017;

    /**
     * The Permission could not be revoked from User because it does not exist on server.
     */
    public final static int PERM_USER_NOT_EXIST = 3018;

    /**
     * The Role-Permission search failed on server.
     */
    public final static int PERM_ROLE_SEARCH_FAILED = 3019;

    /**
     * The User-Permission search failed on ldap server.
     */
    public final static int PERM_USER_SEARCH_FAILED = 3020;

    /**
     * The search for authorized Permission failed on server.
     */
    public final static int PERM_SESS_SEARCH_FAILED = 3021;

    /**
     * The operation to revoke all Permissions from User failed on server.
     */
    public final static int PERM_BULK_USER_REVOKE_FAILED = 3022;

    /**
     * The operation to revoke all Permissions from RBAC Role failed on server.
     */
    public final static int PERM_BULK_ROLE_REVOKE_FAILED = 3023;

    /**
     * The operation to revoke all administrative Permissions from AdminRole failed on server.
     */
    public final static int PERM_BULK_ADMINROLE_REVOKE_FAILED = 3024;

    /**
     * The supplied Perm OU was not found on ldap server.
     */
    public final static int PERM_OU_INVALID = 3025;

    /**
     * The supplied Permission operation name is required but was passed as null.
     */
    public final static int PERM_OPERATION_NM_NULL = 3026;

    /**
     * The supplied Permission object name is required but was passed as null.
     */
    public final static int PERM_OBJECT_NM_NULL = 3027;
    /**
     * 4000's - Password Policy Entity
     */

    /**
     * The Password policy could not be read from ldap server.
     */
    public final static int PSWD_READ_FAILED = 4000;

    /**
     * The supplied Password policy could not be added to ldap server.
     */
    public final static int PSWD_CREATE_FAILED = 4001;

    /**
     * The supplied Password policy could not be updated on ldap server.
     */
    public final static int PSWD_UPDATE_FAILED = 4002;

    /**
     * The supplied Password policy could not be removed from ldap server.
     */
    public final static int PSWD_DELETE_FAILED = 4003;

    /**
     * The supplied Password policy could not be searched on ldap server.
     */
    public final static int PSWD_SEARCH_FAILED = 4004;

    /**
     * The supplied Password policy was not found on ldap server.
     */
    public final static int PSWD_NOT_FOUND = 4005;

    /**
     * The supplied Password policy name failed length check.  Ensure that does not exceed {@link GlobalIds#PWPOLICY_NAME_LEN}.
     */
    public final static int PSWD_NAME_INVLD_LEN = 4006;

    /**
     * The supplied Password quality value failed length check.
     *
     */
    public final static int PSWD_QLTY_INVLD_LEN = 4007;

    /**
     * The supplied Password quality value invalid.
     */
    public final static int PSWD_QLTY_INVLD = 4008;

    /**
     * The supplied Password max age value is invalid.
     */
    public final static int PSWD_MAXAGE_INVLD = 4009;

    /**
     * The supplied Password min age value is invalid.
     */
    public final static int PSWD_MINAGE_INVLD = 4010;

    /**
     * The supplied Password minLength is invalid.
     */
    public final static int PSWD_MINLEN_INVLD = 4011;

    /**
     * The supplied Password interval value invalid.
     */
    public final static int PSWD_INTERVAL_INVLD = 4012;

    /**
     * The Password max failure count invalid.
     */
    public final static int PSWD_MAXFAIL_INVLD = 4013;

    /**
     * The Password must change value invalid.
     */
    public final static int PSWD_MUSTCHG_INVLD = 4014;

    /**
     * The supplied Password safe change attribute invalid.
     */
    public final static int PSWD_SAFECHG_INVLD = 4015;

    /**
     * The supplied Password allow change attribute invalid.
     */
    public final static int PSWD_ALLOWCHG_INVLD = 4016;

    /**
     * The supplied Password history value invalid.
     */
    public final static int PSWD_HISTORY_INVLD = 4017;

    /**
     * The supplied Password grace value invalid.
     */
    public final static int PSWD_GRACE_INVLD = 4018;

    /**
     * The supplied lockout duration was invalid.
     */
    public final static int PSWD_LOCKOUTDUR_INVLD = 4019;

    /**
     * The supplied password expiration value was invalid.
     */
    public final static int PSWD_EXPWARN_INVLD = 4020;

    /**
     * The supplied password lockout attribute is invalid.
     */
    public final static int PSWD_LOCKOUT_INVLD = 4021;

    /**
     * The supplied Password policy name is required and cannot be null.
     */
    public final static int PSWD_NAME_NULL = 4022;

    /**
     * The supplied Password entity is required and cannot be null.
     */
    public final static int PSWD_PLCY_NULL = 4023;

    /**
     * Server returned password policy constraint violation.
     */
    public final static int PSWD_CONST_VIOLATION = 4024;


    /**
     * 5000's - RBAC
     */

    /**
     * Role Rule and System errors
     */

    /**
     * The RBAC Role search failed on ldap server.
     */
    public final static int ROLE_SEARCH_FAILED = 5000;

    /**
     * The RBAC Role read failed on ldap server.
     */
    public final static int ROLE_READ_FAILED = 5001;

    /**
     * The supplied RBAC Role could not be added to ldap server.
     */
    public final static int ROLE_ADD_FAILED = 5002;

    /**
     * The supplied RBAC Role could not be updated on ldap server.
     */
    public final static int ROLE_UPDATE_FAILED = 5003;

    /**
     * The supplied RBAC Role could not be removed from ldap server.
     */
    public final static int ROLE_DELETE_FAILED = 5004;

    /**
     * The RBAC Role name is required and cannot be null.
     */
    public final static int ROLE_NM_NULL = 5005;

    /**
     * The RBAC Role was not found on ldap server.
     */
    public final static int ROLE_NOT_FOUND = 5006;

    /**
     * The RBAC Role entity is required and cannot be null
     */
    public final static int ROLE_NULL = 5007;

    /**
     * The RBAC Role assignment failed on the ldap server.
     */
    public final static int ROLE_USER_ASSIGN_FAILED = 5008;

    /**
     * The RBAC Role deassignment operation failed on the ldap server.
     */
    public final static int ROLE_USER_DEASSIGN_FAILED = 5009;

    /**
     * The RBAC Role list is required and cannot be null.
     */
    public final static int ROLE_LST_NULL = 5010;

    /**
     * Role occupant search failed.
     */
    public final static int ROLE_OCCUPANT_SEARCH_FAILED = 5011;

    /**
     * The operation to remove User as occupant to Roles failed..
     */
    public final static int ROLE_REMOVE_OCCUPANT_FAILED = 5012;

    /**
     * The RBAC Parent Role entity is required for this operation and cannot be null
     */
    public final static int PARENT_ROLE_NULL = 5013;

    /**
     * The RBAC Child Role entity is required for this operation and cannot be null
     */
    public final static int CHILD_ROLE_NULL = 5014;

    /**
     * Hierarchical Constraints
     */

    /**
     * The Hierarchical node could not be read from the ldap server.
     */
    public final static int HIER_READ_FAILED = 5051;

    /**
     * The supplied Hierarchical data could not be added to the ldap server.
     */
    public final static int HIER_ADD_FAILED = 5052;

    /**
     * The supplied Hierarchical data could not be updated on the ldap server.
     */
    public final static int HIER_UPDATE_FAILED = 5053;

    /**
     * The supplied Hierarchical data could not be removed from the ldap server.
     */
    public final static int HIER_DELETE_FAILED = 5054;

    /**
     * The requested Hierarchical data could not be found on the ldap server.
     */
    public final static int HIER_NOT_FOUND = 5056;

    /**
     * The specified relationship is invalid.
     */
    public final static int HIER_REL_INVLD = 5057;

    /**
     * The parent cannot be removed from ldap server because it has child.  Must remove child first.
     */
    public final static int HIER_DEL_FAILED_HAS_CHILD = 5058;

    /**
     * The specified parent-child relationship already exists on the server.
     */
    public final static int HIER_REL_EXIST = 5059;

    /**
     * The specified parent-child relationship does not exist on the server.
     */
    public final static int HIER_REL_NOT_EXIST = 5060;

    /**
     * The supplied Hierarchical entity is required and cannot be null.
     */
    public final static int HIER_NULL = 5061;

    /**
     * The supplied Hierarchical type is required and cannot be null.
     */
    public final static int HIER_TYPE_NULL = 5062;


    /**
     * Separation of Duty Relations
     */

    /**
     * The Static Separation of Duty constraint search failed on the ldap server.
     */
    public final static int SSD_SEARCH_FAILED = 5080;

    /**
     * The Static Separation of Duty constraint read failed on the ldap server.
     */
    public final static int SSD_READ_FAILED = 5081;

    /**
     * The Static Separation of Duty constraint cannot be added to the ldap server.
     */
    public final static int SSD_ADD_FAILED = 5082;

    /**
     * The Static Separation of Duty constraint cannot be updated on the ldap server.
     */
    public final static int SSD_UPDATE_FAILED = 5083;

    /**
     * The Static Separation of Duty constraint cannot be removed from the ldap server.
     */
    public final static int SSD_DELETE_FAILED = 5084;

    /**
     * The Static Separation of Duty name is required and cannot be null.
      */
    public final static int SSD_NM_NULL = 5085;

    /**
     * The Static Separation of Duty constraint could not be found on the ldap server.
     */
    public final static int SSD_NOT_FOUND = 5086;

    /**
     * The Static Separation of Duty Constraint entity is required and cannot be null.
     */
    public final static int SSD_NULL = 5087;

    /**
     * The validation for Static Separation of Duty constraint data failed.
     */
    public final static int SSD_VALIDATION_FAILED = 5088;

    /**
     * The Dynamic Separation of Duty constraint search failed on the ldap server.
     */
    public final static int DSD_SEARCH_FAILED = 5089;

    /**
     * The Dynamic Separation of Duty constraint read failed on the ldap server.
     */
    public final static int DSD_READ_FAILED = 5090;

    /**
     * The Dynamic Separation of Duty constraint cannot be added to the ldap server.
     */
    public final static int DSD_ADD_FAILED = 5091;

    /**
     * The Dynamic Separation of Duty constraint cannot be updated on the ldap server.
     */
    public final static int DSD_UPDATE_FAILED = 5092;

    /**
     * The Dynamic Separation of Duty constraint cannot be removed from the ldap server.
     */
    public final static int DSD_DELETE_FAILED = 5093;

    /**
     * The Dynamic Separation of Duty name is required and cannot be null.
      */
    public final static int DSD_NM_NULL = 5094;

    /**
     * The Dynamic Separation of Duty constraint could not be found on the ldap server.
     */
    public final static int DSD_NOT_FOUND = 5095;

    /**
     * The Dynamic Separation of Duty Constraint entity is required and cannot be null.
     */
    public final static int DSD_NULL = 5096;

    /**
     * The validation for Dynamic Separation of Duty constraint data failed.
     */
    public final static int DSD_VALIDATION_FAILED = 5097;


    /**
     * 6000's - LDAP Suffix and Container Entities
     */

    /**
     * The supplied ldap subdirectory node could not be created on ldap server.
     */
    public final static int CNTR_CREATE_FAILED = 6001;

    /**
     * The supplied ldap subdirectory node could not be removed from the ldap server.
     */
    public final static int CNTR_DELETE_FAILED = 6002;

    /**
     * The subdirectory node name is required and cannot be null.
     */
    public final static int CNTR_NAME_NULL = 6003;

    /**
     * The subdirectory node name failed length check.
     */
    public final static int CNTR_NAME_INVLD = 6004;

    /**
     * The supplied parent node name is required and cannot be null.
     */
    public final static int CNTR_PARENT_NULL = 6005;

    /**
     * The supplied parent node name is invalid.
     */
    public final static int CNTR_PARENT_INVLD = 6006;

    /**
     * The ldap suffix could not be created on the ldap server.
     */
    public final static int SUFX_CREATE_FAILED = 6010;

    /**
     * The ldap suffix node could not be removed from the ldap server.
     */
    public final static int SUFX_DELETE_FAILED = 6011;

    /**
     * The suffix name is required and cannot be null.
     */
    public final static int SUFX_NAME_NULL = 6012;

    /**
     * The supplied suffix name failed length check.
     */
    public final static int SUFX_NAME_INVLD = 6013;

    /**
     * The suffix domain component top level qualifier is required and cannot be null.
     */
    public final static int SUFX_DCTOP_NULL = 6014;

    /**
     * The Suffix domain component top level qualifier name failed the length check.
     */
    public final static int SUFX_DCTOP_INVLD = 6015;


    /**
     * 7000's - Audit Activities
     */

    /**
     * The audit bind search failed on the ldap server.
     */
    public final static int AUDT_BIND_SEARCH_FAILED = 7000;

    /**
     * The Audit input entity is required on this method and cannot be supplied as null
     */
    public final static int AUDT_INPUT_NULL = 7001;

    /**
     * The Audit search for Fortress authorization records failed on the ldap server.
     */
    public final static int AUDT_AUTHZ_SEARCH_FAILED = 7002;

    /**
     * The Audit search for modifications failed on the ldap server.
     */
    public final static int AUDT_MOD_SEARCH_FAILED = 7003;

    /**
     * The Audit mod search by administrator internal id failed on ldap server.
     */
    public final static int AUDT_MOD_ADMIN_SEARCH_FAILED = 7004;

    /**
     * The Audit search for authentication events failed on the ldap server.
     */
    public final static int AUDT_AUTHN_INVALID_FAILED = 7005;


    /**
     * 8000's Organizational Unit Rule and System errors
     */

    /**
     * The supplied organization unity entity is required and cannot be null.
     */
    public final static int ORG_NULL = 8001;

    /**
     * The supplied {@link com.jts.fortress.arbac.OrgUnit#type} is required and cannot be null.
     */
    public final static int ORG_TYPE_NULL = 8002;

    /**
     * The supplied User OU entity could not be read due to ldap error.
     */
    public final static int ORG_READ_FAILED_USER = 8011;

    /**
     * The supplied User OU entity could not be added due to ldap error.
     */
    public final static int ORG_ADD_FAILED_USER = 8012;

    /**
     * The supplied User OU entity could not be updated due to ldap error.
     */
    public final static int ORG_UPDATE_FAILED_USER = 8013;

    /**
     * The supplied User OU entity could not be removed due to ldap error.
     */
    public final static int ORG_DELETE_FAILED_USER = 8014;

    /**
     * The OU User search failed due to ldap error.
     */
    public final static int ORG_SEARCH_FAILED_USER = 8015;

    /**
     * The search for User OU's failed on ldap server.
     */
    public final static int ORG_GET_FAILED_USER = 8016;

    /**
     * The supplied User OU not found on ldap server.
     */
    public final static int ORG_NOT_FOUND_USER = 8017;

    /**
     * The supplied User OU is required and cannot be null.
     */
    public final static int ORG_NULL_USER = 8018;

    /**
     * The supplied User OU type is required and cannot be null.
     */
    public final static int ORG_TYPE_NULL_USER = 8019;

    /**
     * The supplied User OU entry could not be removed from the server.
     */
    public final static int ORG_DEL_FAILED_USER = 8020;

    /**
     * The supplied Perm OU entity could not be read due to ldap error.
     */
    public final static int ORG_READ_FAILED_PERM = 8061;

    /**
     * The supplied Perm OU entity could not be added due to ldap error.
     */
    public final static int ORG_ADD_FAILED_PERM = 8062;

    /**
     * The supplied Perm OU entity could not be updated due to ldap error.
     */
    public final static int ORG_UPDATE_FAILED_PERM = 8063;

    /**
     * The supplied Perm OU entity could not be removed due to ldap error.
     */
    public final static int ORG_DELETE_FAILED_PERM = 8064;

    /**
     * The OU Perm search failed due to ldap error.
     */
    public final static int ORG_SEARCH_FAILED_PERM = 8065;

    /**
     * The search for Perm OU's failed on ldap server.
     */
    public final static int ORG_GET_FAILED_PERM = 8066;

    /**
     * The supplied Perm OU not found on ldap server.
     */
    public final static int ORG_NOT_FOUND_PERM = 8067;

    /**
     * The supplied Perm OU is required and cannot be null.
     */
    public final static int ORG_NULL_PERM = 8068;

    /**
     * The supplied Perm OU type is required and cannot be null.
     */
    public final static int ORG_TYPE_NULL_PERM = 8069;

    /**
     * The supplied Perm OU entry could not be removed from the server.
     */
    public final static int ORG_DEL_FAILED_PERM = 8070;

    /**
     * The supplied OU name exceed maximum allowed {@link GlobalIds#OU_LEN}.
     */
    public final static int ORG_LEN_INVLD = 8071;

    /**
     * The supplied Parent OU is required for this operation and cannot be null.
     */
    public final static int ORG_PARENT_NULL = 8072;

    /**
     * The supplied Parent OU is required for this operation and cannot be null.
     */
    public final static int ORG_CHILD_NULL = 8073;

    /**
     * 9000's Administrative RBAC
     */
    /**
     * The Administrative Role search failed on ldap server.
     */
    public final static int ARLE_SEARCH_FAILED = 9000;

    /**
     * The Administrative Role read failed on ldap server.
     */
    public final static int ARLE_READ_FAILED = 9001;

    /**
     * The supplied Administrative Role could not be added to ldap server.
     */
    public final static int ARLE_ADD_FAILED = 9002;

    /**
     * The supplied Administrative Role could not be updated on ldap server.
     */
    public final static int ARLE_UPDATE_FAILED = 9003;

    /**
     * The supplied Administrative Role could not be removed from ldap server.
     */
    public final static int ARLE_DELETE_FAILED = 9004;

    /**
     * The Administrative Role name is required and cannot be null.
     */
    public final static int ARLE_NM_NULL = 9005;

    /**
     * The Administrative Role was not found on ldap server.
     */
    public final static int ARLE_NOT_FOUND = 9006;

    /**
     * The Administrative Role entity is required and cannot be null
     */
    public final static int ARLE_NULL = 9007;

    /**
     * The User Administrative Role assignment failed.
     */
    public final static int ARLE_USER_ASSIGN_FAILED = 9008;

    /**
     * The User Administrative Role deassignment failed.
     */
    public final static int ARLE_USER_DEASSIGN_FAILED = 9009;

    /**
     * Method requires list of Administrative Roles and cannot be null.
     *
     */
    public final static int ARLE_LST_NULL = 9010;

    /**
     * The supplied begin range for Administrative Role is required and cannot be null.
     *
     */
    public final static int ARLE_BEGIN_RANGE_NULL = 9011;

    /**
     * The supplied end range for Administrative Role is required and cannot be null.
     *
     */
    public final static int ARLE_END_RANGE_NULL = 9011;

    /**
     * The supplied range for Administrative Role is invalid.
     *
     */
    public final static int ARLE_INVLD_RANGE = 9012;

    /**
     * The supplied range for Administrative Role inclusion is invalid.
     *
     */
    public final static int ARLE_INVLD_RANGE_INCLUSIVE = 9013;

    /**
     * The supplied Administrative Role could not be activated in the User's Session.
     */
    public final static int ARLE_ACTIVATE_FAILED = 9014;

    /**
     * The supplied Administrative Role could not be deactivated from the User's session.
     */
    public final static int ARLE_DEACTIVE_FAILED = 9015;

    /**
     * The Administrative Role is already activated in User's Session.
     */
    public final static int ARLE_ALREADY_ACTIVE = 9016;

    /**
     * The supplied Administrative Role is not activated in User's Session.
     */
    public final static int ARLE_NOT_ACTIVE = 9017;

    /**
     * The Administrative Role search could failed on server.
     */
    public final static int ARLE_USER_SEARCH_FAILED = 9018;

    /**
     * The Parent Administrative Role entity is required and cannot be null
     */
    public final static int ARLE_PARENT_NULL = 9019;

    /**
     * The Child Administrative Role entity is required and cannot be null
     */
    public final static int ARLE_CHILD_NULL = 9020;

    /**
     * The Admin Role could not be assigned to User already has it assigned.
     */
    public final static int ARLE_ASSIGN_EXIST = 9021;

    /**
     * The User Admin Role could not be deassigned from User because it wasn't assigned in the first place.
     */
    public final static int ARLE_ASSIGN_NOT_EXIST = 9022;

    /**
     * The User Admin Role could not be deassigned from User because it wasn't assigned in the first place.
     */
    public final static int ARLE_DEASSIGN_NOT_EXIST = 9023;

    /**
     * The User Admin Role assignment failed.
     */
    public final static int ARLE_ASSIGN_FAILED = 9024;

    /**
     * The User Admin Role deassignment failed.
     */
    public final static int ARLE_DEASSIGN_FAILED = 9025;

    /**
     * AdminRole occupant search failed.
     */
    public final static int ARLE_OCCUPANT_SEARCH_FAILED = 9026;

    /**
     * The operation to remove User as occupant to AdminRoles failed..
     */
    public final static int ARLE_REMOVE_OCCUPANT_FAILED = 9027;

    /**
     * 10000's - Temporal Constraint Validation Error Ids
     */

    /**
     * The constraint contains invalid text.
     */
    public final static int CONST_INVLD_TEXT = 10001;

    /**
     * The Constraint value failed length check.
     */
    public final static int CONST_INVLD_FIELD_LEN = 10002;

    /**
     * The Constraint contains an invalid timeout value.
     */
    public final static int CONST_TIMEOUT_INVLD = 10003;

    /**
     * The Constraint contains an invalid beginTime value.
     */
    public final static int CONST_BEGINTIME_INVLD = 10004;

    /**
     * The Constraint contains an invalid beginTime length.
     */
    public final static int CONST_BEGINTIME_LEN_ERR = 10005;

    /**
     * The Constraint contains an invalid endTime value.
     */
    public final static int CONST_ENDTIME_INVLD = 10006;

    /**
     * The Constraint contains an invalid endTime length.
     */
    public final static int CONST_ENDTIME_LEN_ERR = 10007;

    /**
     * The Constraint contains an invalid beginDate value.
     */
    public final static int CONST_BEGINDATE_INVLD = 10008;

    /**
     * The Constraint contains an invalid beginDate length.
     */
    public final static int CONST_BEGINDATE_NULL = 10009;

    /**
     * The Constraint contains an invalid endDate value.
     */
    public final static int CONST_ENDDATE_INVLD = 10010;

    /**
     * The Constraint contains an invalid endDate length.
     */
    public final static int CONST_ENDDATE_NULL = 10011;

    /**
     * The Constraint contains an invalid dayMask value.
     */
    public final static int CONST_DAYMASK_INVLD = 10012;

    /**
     * The Constraint contains a null dayMask value.
     */
    public final static int CONST_DAYMASK_NULL = 10013;

    /**
     * The Constraint description is optional but cannot exceed length of {@link GlobalIds#DESC_LEN} if supplied.
     */
    public final static int CONST_DESC_LEN_INVLD = 10014;

    /**
     * The Constraint contains a null value.
     */
    public final static int CONST_NULL_TEXT = 10015;
}

