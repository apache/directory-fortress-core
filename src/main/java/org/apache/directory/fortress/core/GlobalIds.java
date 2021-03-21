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


import org.apache.directory.api.ldap.model.constants.SchemaConstants;


/**
 * This class contains constants that must be defined globally but are not to be used by external programs.
 * The constants are used internally by Fortress when looking up cfg values, performing maintenance on
 * standard and custom ldap objects and attributes, instantiating manager instances, validating objects and attributes, and more.
 * Some of the values for public constants defined here must be known to end users of system to declare system cfg parameters, see {@link org.apache.directory.fortress.core.util.Config}.
 * For example the {@link #SUFFIX} constant uses key name {@code suffix} which must have a corresponding value, i.e. {@code dc=example,dc=com},
 * which tells location of Directory Information Tree to the Fortress runtime processor.
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class GlobalIds
{
    public static final String CONFIG_ROOT_PARAM = "config.root";    

    public static final String HOME = "HOME";
    public static final String TENANT = "tenant";
    public static final String DISABLE_AUDIT = "disable.audit";
    public static final String ENABLE_REST = "enable.mgr.impl.rest";
    public static final String CONSTRAINT_KEY_PREFIX = "RC";

    /**
     * The following constants are used within the factory classes:
     */

    /**
     * When this optional tag, {@code accessmgr.implementation}, is placed in Fortress properties, its class name will be the default {@link AccessMgr} instance used.
     */
    public static final String ACCESS_IMPLEMENTATION = "accessmgr.implementation";

    /**
     * When this optional tag, {@code adminImplementation}, is placed in Fortress properties, its class name will be the default {@link AdminMgr} instance used.
     */
    public static final String ADMIN_IMPLEMENTATION = "adminmgr.implementation";

    /**
     * When this optional tag, {@code daoConnector}, is placed in Fortress properties,
     */
    public static final String DAO_CONNECTOR = "dao.connector";

    /**
     * When this optional tag, {@code reviewImplementation}, is placed in Fortress properties, its class name will be the default {@link ReviewMgr} instance used.
     */
    public static final String REVIEW_IMPLEMENTATION = "reviewmgr.implementation";

    /**
     * When this optional tag, {@code policyImplementation}, is placed in Fortress properties, its class name will be the default {@link PwPolicyMgr} instance used.
     */
    public static final String PSWD_POLICY_IMPLEMENTATION = "policymgr.implementation";

    /**
     * When this optional tag, {@code auditmgr.implementation}, is placed in Fortress properties, its class name will be the default {@link AuditMgr} instance used.
     */
    public static final String AUDIT_IMPLEMENTATION = "auditmgr.implementation";

    /**
     * When this optional tag, {@code delegatedAdminImplementation}, is placed in Fortress properties, its class name will be the default {@link DelAdminMgr} instance used.
     */
    public static final String DELEGATED_ADMIN_IMPLEMENTATION = "delegated.adminmgr.implementation";

    /**
     * When this optional tag, {@code delegatedReviewImplementation}, is placed in Fortress properties, its class name will be the default {@link DelReviewMgr} instance used.
     */
    public static final String DELEGATED_REVIEW_IMPLEMENTATION = "delegated.reviewmgr.implementation";

    /**
     * When this optional tag, {@code delegatedAccessImplementation}, is placed in Fortress properties, its class name will be the default {@link DelAccessMgr} instance used.
     */
    public static final String DELEGATED_ACCESS_IMPLEMENTATION = "delegated.accessmgr.implementation";

    /**
     * When this optional tag, {@code configImplementation}, is placed in Fortress properties, its class name will be the default {link ConfigMgr} instance used.
     */
    public static final String CONFIG_IMPLEMENTATION = "configmgr.implementation";

    /**
     * When this optional tag, {@code accelsmgr.implementation}, is placed in Fortress properties, its class name will be the default {@link AccelMgr} instance used.
     */
    public static final String ACCEL_IMPLEMENTATION = "accelmgr.implementation";

    /**
     * When this optional tag, {@code groupImplementation}, is placed in Fortress properties, its class name will be the default {link GroupMgr} instance used.
     */
    public static final String GROUP_IMPLEMENTATION = "groupmgr.implementation";

    /**
     * When this optional tag, {@code propertyImplementation}, is placed in Fortress properties, its class name will be the default {@link PropertyMgr} instance used.
     */
    public static final String PROPERTY_IMPLEMENTATION = "propertymgr.implementation";
    
    // AUTHENTICATION_TYPE
    /**
     * This property is used to specify if authentication is being performed within a security realm.
     */
    public static final String AUTHENTICATION_TYPE = "authn.type";

    /**
     * Specifies realm authentication mode.
     */
    public static final String REALM_TYPE = "REALM";

    /**
     * Used to declare validation modules that are used to process constraint checks during session activation.
     */
    public static final String VALIDATOR_PROPS = "temporal.validator.";

    /**
     * The DSD validator performs Dynamic Separation of Duty checks during role activation.
     */
    public static final String DSD_VALIDATOR_PROP = "temporal.validator.dsd";

    /**
     * Parameter specifies the distinguished name (dn) of the LDAP suffix.  The is the root or top-most node for a Directory Information Tree (DIT).  The typical
     * Fortress suffix format is {@code dc=example,dc=com}.
     */
    public static final String SUFFIX = "suffix";

    /**
     * Specifies the dn of the container where the Fortress User data set is located within DIT.  This is typically in the format of
     * {@code ou=People, dc=example, dc=com}
     */
    public static final String USER_ROOT = "user.root";

    /**
     * Specifies the dn of the container where the Fortress Permissions are located.  This is typically in the format of
     * {@code ou=Permissions,ou=RBAC,dc=example,dc=com}
     */
    public static final String PERM_ROOT = "perm.root";

    /**
     * Specifies the dn of the container where the Fortress RBAC Roles are located.  This is typically in the format of
     * {@code ou=Roles,ou=RBAC,dc=example,dc=com}
     */
    public static final String ROLE_ROOT = "role.root";

    /**
     * Specifies the dn of the container where the Fortress Password Polices are located.  This is typically in the format of
     * {@code ou=Policies,dc=example,dc=com}
     */
    public static final String PPOLICY_ROOT = "pwpolicy.root";

    /**
     * Specifies the dn of the container where the Fortress SSD and DSD constraints are located.  This is typically in the format of
     * {@code ou=Constraints,ou=RBAC,dc=example,dc=com}
     */
    public static final String SD_ROOT = "sdconstraint.root";

    /**
     * Specifies the dn of the container where the Fortress User OU pools are located.  This is typically in the format of
     * {@code ou=OS-U,ou=ARBAC,dc=example,dc=com}
     */
    public static final String OSU_ROOT = "userou.root";

    /**
     * Specifies the dn of the container where the Fortress Permission OU pools are located.  This is typically in the format of
     * {@code ou=OS-P,ou=ARBAC,dc=example,dc=com}
     */
    public static final String PSU_ROOT = "permou.root";

    /**
     * Specifies the dn of the container where the Fortress Administrative Roles are located.  This is typically in the format of
     * {@code ou=AdminRoles,ou=ARBAC,dc=example,dc=com}
     */
    public static final String ADMIN_ROLE_ROOT = "adminrole.root";

    /**
     * Specifies the dn of the container where the Fortress Administrative Permissions are located.  This is typically in the format of
     * {@code ou=AdminPerms,ou=ARBAC,dc=example,dc=com}
     */
    public static final String ADMIN_PERM_ROOT = "adminperm.root";

    /**
     * Specifies the dn of the container where the Groups are located.  This is typically in the format of
     * {@code ou=Groups,dc=example,dc=com}
     */
    public static final String GROUP_ROOT = "group.root";

    /**
     * Specifies the dn of the container where password policies are stored in ApachDS.  This is typically here:
     * {@code ou=passwordPolicies,ads-interceptorId=authenticationInterceptor,ou=interceptors,ads-directoryServiceId=default,ou=config}
     */
    public static final String ADS_PPOLICY_ROOT = "apacheds.pwpolicy.root";

    /*
    *  *************************************************************************
    *  **  LDAP ATTRIBUTE NAMES AND CONSTANT VALUES
    *  ************************************************************************
    */
    public static final String SERVER_TYPE = "ldap.server.type";

    /*
      *  *************************************************************************
      *  **  AUDIT
      *  ************************************************************************
      */

    public static final int AUTHZ_COMPARE_FAILURE_FLAG = 5;
    /**
     * This string will be appended to the operation name to force failure on compare.
     */
    public static final String FAILED_AUTHZ_INDICATOR = "%failed%";
    /**
     * This aux object class contains Fortress audit contextual information.
     */
    public static final String FT_MODIFIER_AUX_OBJECT_CLASS_NAME = "ftMods";
    /**
     * This aux object class stores uidNumber and gidNumber sequence numbers.
     */
    public static final String FT_CONFIG_AUX_OBJECT_CLASS_NAME = "ftConfig";

    /**
     * The ftModifier contains the internalUserId of administrator who performed action.
     */
    public static final String FT_MODIFIER = "ftModifier";

    /**
     * The {@code ftModCode} attribute contains the permission object name and operation of admin function performed.
     */
    public static final String FT_MODIFIER_CODE = "ftModCode";

    /**
     * The {@code ftModId} contains a globally unique id that is bound to the audit event entity.
     */
    public static final String FT_MODIFIER_ID = "ftModId";

    /**
     * The {@code ftId} contains a globally unique id that is bound to the application entity.
     */
    public static final String FT_IID = "ftId";

    /**
     * This string literal contains a common start for most ldap search filters that fortress uses.
     */
    public static final String FILTER_PREFIX = "(&(" + SchemaConstants.OBJECT_CLASS_AT + "=";

    /*
      *  *************************************************************************
      *  **  Fortress PROPERTIES are used by USER, PERM, CONFIG DAO'S.
      *  ************************************************************************
      */
    /**
     * The {@code ftProperties} object class contains name-value pairs that are neither validated nor constrained.
     * Properties are application defined parameters and clients may store any reasonable values.
     */
    public static final String PROPS_AUX_OBJECT_CLASS_NAME = "ftProperties";

    /**
     * The {@code ftProps} attribute contains a single name-value pairs that is {@code :} separated.
     */
    public static final String PROPS = "ftProps";

    /*
      *  *************************************************************************
      *  **  OpenAccessMgr ROLE STATICS are used by RBAC and ARBAC DAO
      *  ************************************************************************
      */

    /**
     * The object class is used to store Fortress Role entity data.
     */
    public static final String ROLE_OBJECT_CLASS_NM = "ftRls";

    /*
      *  *************************************************************************
      *  **  OpenAccessMgr CONSTRAINTS are used by USER, ROLE, ADMINROLE DAO'S.
      *  ************************************************************************
      */

    /**
     * Constraint AUX Object Class Schema definitions:
     */

    /**
     * This single occurring attribute is used to store constraint policies on Fortress User objects.
     */
    public static final String CONSTRAINT = "ftCstr";

    // USER Role Definitions:

    /**
     * multivalued attribute contains RBAC Role assignments for Users.
     */
    //public static final String USER_ROLE_ASSIGN = "ftRA";

    /**
     * multivalued attribute contains constraint policies for RBAC Role assignments for Users.
     */
    public static final String USER_ROLE_DATA = "ftRC";

    /**
     * multivalued attribute contains Administrative Role assignments for Users.
     */
    public static final String USER_ADMINROLE_ASSIGN = "ftARA";

    /**
     * multivalued attribute contains constraint policies for Administrative Role assignments for Users.
     */
    public static final String USER_ADMINROLE_DATA = "ftARC";

    /**
     * Attribute name for storing Fortress permission object names.
     */
    public static final String POBJ_NAME = "ftObjNm";    
    
    /**
     * Attribute name for storing Fortress permission object id.
     */
    public static final String POBJ_ID = "ftObjId";

    /**
     * Attribute name for storing parent node names for hierarchical processing.
     */
    public static final String PARENT_NODES = "ftParents";

    /**
     * Attribute name for storing type on either permission or groups.
     */
    public static final String TYPE = "ftType";


    /**
     * RF2307bis uses these on users and roles:
     */
    public static final String RFC2307_PROP = "rfc2307";
    public static final String GID_NUMBER = "gidNumber";
    public static final String UID_NUMBER = "uidNumber";

    /*
    *  *************************************************************************
    *  **  RBAC Entity maximum length constants
    *  ************************************************************************
    */

    /**
     * Fortress userId cannot exceed length of 40.
     */
    public static final int USERID_LEN = 40;

    /**
     * Fortress role names cannot exceed length of 40.
     */
    public static final int ROLE_LEN = 40;

    /**
     * Fortress description text cannot exceed length of 80.
     */
    public static final int DESC_LEN = 180;

    /**
     * Fortress permission names cannot exceed length of 100.
     */
    public static final int PERM_LEN = 100;

    /**
     * Fortress User passwords must have length of 50 or less..
     */
    public static final int PASSWORD_LEN = 50;

    /**
     * Fortress password policy names cannot exceed length of 40.
     */
    public static final int PWPOLICY_NAME_LEN = 40;

    /**
     * Fortress ou's cannot exceed length of 40.
     */
    public static final int OU_LEN = 40;

    /**
     * Fortress User surname cannot exceed length of 80.
     */
    public static final int SN_LEN = 80;

    /**
     * Fortress common name attributes cannot exceed length of 80.
     */
    public static final int CN_LEN = 80;

    /**
     * Fortress properties cannot exceed length of 100.
     */
    public static final int PROP_LEN = 100;

    // Regular Expression Patterns stored in Fortress config file:
    public static final String REG_EX_SAFE_TEXT = "regXSafetext";

    /*
      *  *************************************************************************
      *  **  LDAP FILTER CONSTANTS
      *  ************************************************************************
      */
    /**
     * Used to define characters that must be encoded before being processed by ldap operations.
     */
    public static final String LDAP_FILTER = "ldap.filter.";

    /**
     * Used to define encoded replacements for characters to be filtered.
     */
    public static final String LDAP_SUB = "ldap.sub.";

    /**
     * Defines how many entries are to be stored in the encoding set.
     */
    public static final String LDAP_FILTER_SIZE_PROP = "ldap.filter.size";

    public static final String APACHE_LDAP_API = "apache";
    public static final String AUTH_Z_FAILED = "authzfailed";
    public static final String POP_NAME = "ftOpNm";
    public static final String AUTH_Z_FAILED_VALUE = POP_NAME + "=" + AUTH_Z_FAILED;

    /**
     * Used for ldap connection pool of admin users.
     */
    public static final String LDAP_ADMIN_POOL_MIN = "min.admin.conn";
    public static final String LDAP_ADMIN_POOL_MAX = "max.admin.conn";
    public static final String LDAP_ADMIN_POOL_UID = "admin.user";
    public static final String LDAP_ADMIN_POOL_PW = "admin.pw";
    public static final String LDAP_ADMIN_POOL_TEST_IDLE = "admin.conn.test.idle";
    public static final String LDAP_ADMIN_POOL_EVICT_RUN_MILLIS = "admin.conn.evict.run.millis";

    /**
     * Used for ldap connection pool of log users.
     */
    public static final String LDAP_LOG_POOL_UID = "log.admin.user";
    public static final String LDAP_LOG_POOL_PW = "log.admin.pw";
    public static final String LDAP_LOG_POOL_MIN = "min.log.conn";
    public static final String LDAP_LOG_POOL_MAX = "max.log.conn";
    public static final String LDAP_LOG_POOL_TEST_IDLE = "log.conn.test.idle";
    public static final String LDAP_LOG_POOL_EVICT_RUN_MILLIS = "log.conn.evict.run.millis";

    /**
     * Applies to all pools, connection validated on retrieval with dummy ldapsearch.
     */
    public static final String LDAP_VALIDATE_CONN = "validate.conn";

    // Used for TLS/SSL client-side configs:
    public static final String ENABLE_LDAP_SSL = "enable.ldap.ssl";
    public static final String ENABLE_LDAP_STARTTLS = "enable.ldap.starttls";
    public static final String ENABLE_LDAP_SSL_DEBUG = "enable.ldap.ssl.debug";
    public static final String TRUST_STORE = "trust.store";
    public static final String TRUST_STORE_PW = "trust.store.password";
    public static final String TRUST_STORE_ON_CLASSPATH = "trust.store.onclasspath";

    // coordinates to the LDAP server:
    public static final String LDAP_HOST = "host";
    public static final String LDAP_PORT = "port";

    /**
     * maximum number of entries allowed for ldap filter replacements.
     */
    private static int ldapFilterSize = 25;

    /**
     * Maximum number of entries allowed for ldap filter replacements.
     */
    public static final int LDAP_FILTER_SIZE = ldapFilterSize;

    /**
     * This property contains the location for the remote Fortress properties stored in ldap.  This is typically in the format of
     * {@code cn=DEFAULT,ou=Config,dc=example,dc=com}
     */
    public static final String CONFIG_REALM = "config.realm";

    /**
     * This config is used to retrieve uidNumber from property list during file load.
     */
    public static final String CONFIG_UID_NUMBER = "config.uid.number";

    /**
     * This config is used to retrieve uidNumber from property list during file load.
     */
    public static final String CONFIG_GID_NUMBER = "config.gid.number";

    /**
     * Fortress stores name-value pairs within multivalued attributes in ldap.  Usually a separator of ':' is used
     * format: {@code name:value},
     */
    public static final char PROP_SEP = ':';

    /**
     * Maximum number of records for ldap client to wait on while processing results sets from ldap server.
     */
    public static final int BATCH_SIZE = 1000;

    /**
     * This is the config property key used to store override of max LDAP batch size:
     */
    public static final String CONFIG_LDAP_MAX_BATCH_SIZE = "ldap.search.max.batch.size";

    /**
     * Attribute is used in Fortress time/date constraints as default which will always pass.  i.e. values stored as beginDate=none or beginTime=none will turn the date and time constraints off
     * for a particular entity..
     */
    public static final String NONE = "none";

    /**
     * Attribute is used in Fortress day mask constraints as default which will always pass.  i.e. values stored as dayMask=all will always pass the day of week constraint.
     */
    public static final String ALL = "all";
    public static final String NULL = "null";
    
    /**
     * The ftPA field contains attributes and associated metadata for permissions.
     */
    public static final String FT_PERMISSION_ATTRIBUTE = "ftPA";
    
    /**
     * The ftPASet field contains the name of the ftPermissionAttributeSet for a permission
     */
    public static final String FT_PERMISSION_ATTRIBUTE_SET = "ftPASet";
    
    /**
     * Attribute name for storing Fortress permission attribute set object names.
     */
    public static final String PERMISSION_ATTRIBUTE_SET_NAME = "ftAttributeSet";

    /**
     * When this property is set to true in fortress.properties, the userPassword field on newly created users will not be created.
     */
    public static final String USER_CREATION_PASSWORD_FIELD = "user.creation.field.password.disable";
    
    /**
     * The ftPASetType field contains the user defined type of a pa set
     */
    public static final String FT_PERMISSION_ATTRIBUTE_SET_TYPE = "ftPASetType";
    
    /**
     * Attribute name for storing Fortress permission attribute set object names.
     */
    public static final String PERMISSION_ATTRIBUTE_NAME = "ftAttribute";
    
    /**
     * Attribute name for storing Fortress permission attribute data type name.
     */
    public static final String FT_PERMISSION_ATTRIBUTE_DATA_TYPE = "ftPADataType";
    
    /**
     * Attribute name for storing Fortress permission attribute default value name.
     */
    public static final String FT_PERMISSION_ATTRIBUTE_DEFAULT_VALUE = "ftPADefaultValue";
    
    /**
     * Attribute name for storing Fortress permission attribute default strategy name.
     */
    public static final String FT_PERMISSION_ATTRIBUTE_DEFAULT_STRATEGY = "ftPADefaultStrategy";
    
    /**
     * Attribute name for storing Fortress permission attribute set default operator name.
     */
    public static final String FT_PERMISSION_ATTRIBUTE_DEFAULT_OPERATOR = "ftPADefaultOperator";
    
    /**
     * Attribute name for storing Fortress permission attribute valid vals name.
     */
    public static final String FT_PERMISSION_ATTRIBUTE_VALID_VALUES = "ftPAValidVals";

    /**
     * Attribute name for property containing HTTP service account userId.
     */
    public static final String HTTP_UID_PROP = "http.user";

    /**
     * Attribute name for property containing HTTP service account password.
     */
    public static final String HTTP_PW_PROP = "http.pw";

    /**
     * Attribute name for property ARBAC02 enforcement boolean.
     */
    public static final String IS_ARBAC02 = "is.arbac02";
}
