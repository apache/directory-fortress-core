   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE file
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this file
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this file except in compliance
   with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.

-------------------------------------------------------------------------------
# README for Apache Fortress Properties
-------------------------------------------------------------------------------

## Table of Contents

 * SECTION 1. Fortress Properties Overview.
 * SECTION 2. Fortress Properties.

-------------------------------------------------------------------------------
## SECTION 1.  Fortress Configuration Subsystem Overview

This document lists the properties used within the fortress core.  These props can be stored in various locations.  For example as system.properties, in a file on within an LDAP node.  See the README-CONFIG for description of how the config system works.

-------------------------------------------------------------------------------
## SECTION 16. Fortress Core properties

This section describes the properties needed to control fortress core.

1. LDAP Hostname coordinates.  The host name can be specified as a fully qualified domain name or IP address.

 ```
 # Host name and port of LDAP DIT:
 host=localhost
 port=10389
 ```

2. LDAP Server type.  Each LDAP server impl has different behavior on operations like password policies and audit.  If using a 3rd type of server that isn't formally supported, leave blank or type is other.

 ```
 # If ApacheDS server:
 ldap.server.type=apacheds
 ```

 ```
 # Else if OpenLDAP server:
 ldap.server.type=openldap
 ```

 ```
 # Else leave blank:
 #ldap.server.type=other
 ```

3.  Set the credentials of service account.  Must have read/write privileges over the Fortress LDAP DIT:

 ```
 # If ApacheDS it will look something like this:
 admin.user=uid=admin,ou=system
 admin.pw=secret
 ```

 ```
 # Else If OpenLDAP it will look something like this:
 admin.user=cn=Manager,dc=example,dc=com
 ```

4. Define the number of LDAP connections to use in the pool  This setting will be proportional to the number of concurrent users but won't be one-to-one.  The number of required ldap connections will be much lower than concurrent users:

 ```
 # This is min/max settings for LDAP connections.  For testing and low-volume instances this will work:
 min.admin.conn=1
 max.admin.conn=10
 ```

5. Give coordinates to the Config node that contains all of the other Fortress properties.  This will match your LDAP's server's config node per Fortress Core setup.

 ```
 # This node contains fortress properties stored on behalf of connecting LDAP clients:
 config.realm=DEFAULT
 config.root=ou=Config,dc=example,dc=com
 ```

6. If using LDAPS.

 ```
 # Used for SSL Connection to LDAP Server:
 enable.ldap.ssl=true
 enable.ldap.ssl.debug=true
 trust.store.password=changeit
 ```

 a. Trust store can found on the classpath

 ```
 trust.store.onclasspath=true
 trust.store=mytruststorename
 ```

 b. Trust store can found as fully qualified filename:

 ```
 trust.store.onclasspath=false
 trust.store=/fully/qualified/path/and/file/name/to/java/mytruststorename
 ```

 * Question: Should I access my truststore from classpath or as fully qualified?
 * Answer: If using for REST/HTTPS or JDBC/SSL - yes, otherwise your option (classpath=true only works with LDAPS).

7. To use REST instead of LDAP.  Points to fortress-rest instance.

 ```
 # This will override default LDAP manager implementations for the RESTful ones:
 enable.mgr.impl.rest=true
 ```

8. If using REST, provide the credentials of user that has access to fortress-rest.

 ```
 # Optional parameters needed when Fortress client is connecting with the En Masse (rather than LDAP) server:
 http.user=demouser4
 http.pw=gX9JbCTxJW5RiH+otQEX0Ja0RIAoPBQf
 http.host=localhost
 http.port=8080
 ```

9. If using ApacheDS and setting password policies, point to the correction location.

 ```
 # ApacheDS stores its password policies objects here by default:
 apacheds.pwpolicy.root=ou=passwordPolicies,ads-interceptorId=authenticationInterceptor,ou=interceptors,ads-directoryServiceId=default,ou=config
 ```

10. LDAP Directory Information Tree (DIT) Samples

 ```
 # Define the high-level structure of LDAP DIT
 # For a two-part domain context, e.g. dc=example,dc=com:
 suffix.name=example
 suffix.dc=com
 suffix=dc=example,dc=com
 suffix=dc=${suffix.name},dc=${suffix.dc}
 # Else, for a three-part domain context, e.g. dc=foo, dc=example,dc=com:
 suffix.name=foo
 suffix.dc=example
 suffix.dc2=com
 suffix=dc=${suffix.name},dc=${suffix.dc},dc=${suffix.dc2}
 # The Config container must be specified in the properties file.
 config.root=ou=Config,dc=example,dc=com
 # The other fortress containers may either be in the build.properties file, or loaded into the config node.
 user.root=ou=People,dc=example,dc=com
 pwpolicy.root=ou=Policies,dc=example,dc=com
 role.root=ou=Roles,ou=RBAC,dc=example,dc=com
 perm.root=ou=Permissions,ou=RBAC,dc=example,dc=com
 sdconstraint.root=ou=Constraints,ou=RBAC,dc=example,dc=com
 userou.root=ou=OS-U,ou=ARBAC,dc=example,dc=com
 permou.root=ou=OS-P,ou=ARBAC,dc=example,dc=com
 adminrole.root=ou=AdminRoles,ou=ARBAC,dc=example,dc=com
 adminperm.root=ou=AdminPerms,ou=ARBAC,dc=example,dc=com
 group.root=ou=Groups,dc=example,dc=com
 ```

 Note: See the [README-CONFIG](./README-CONFIG.md)) guide for more info how fortress finds its properties.

11. Define the delegated administration super admin role.  Any user who is assigned this role will bypass all ARBAC02 security checks, when they are enabled.

 ```
 superadmin.role=fortress-core-super-admin
 ```

12. Enable validation for temporal constraints.

 ```
 # these properties will enable temporal constraint checks on role activations:
 temporal.validator.0=org.apache.directory.fortress.core.util.time.Date
 temporal.validator.1=org.apache.directory.fortress.core.util.time.LockDate
 temporal.validator.2=org.apache.directory.fortress.core.util.time.Timeout
 temporal.validator.3=org.apache.directory.fortress.core.util.time.ClockTime
 temporal.validator.4=org.apache.directory.fortress.core.util.time.Day
 ```

13. Enable validation for dynamic static separation of duty constraints.

 ```
 # enabling this property will enable Dynamic Separation of Duty constraint checks on role activations:
 temporal.validator.dsd=org.apache.directory.fortress.core.impl.DSDChecker
 ```

14. Define system user accounts that may not be deleted with fortress APIs.

 ```
 # Users in the following list cannot be deleted using OAM admin functions (AdminMgr.deleteUser, AdminMgr.forceDeleteUser)
 sys.user.1=oamTU6User1
 sys.user.2=oamTU6User2
 sys.user.3=oamTU6User3
 sys.user.4=oamTU6User4
 sys.user.5=oamTU6User5
 ```

15. Manager implementation class overrides.

 ```
 # Fortress Class Definitions:  NOT NEEDED UNLESS OVERIDING DEFAULT IMPLEMENTATIONS
 accessmgr.implementation=org.apache.directory.fortress.core.impl.AccessMgrImpl
 auditmgr.implementation=org.apache.directory.fortress.core.impl.AuditMgrImpl
 ```

16. EHcache config file containing defaults for caches.

 ```
 ehcache.config.file=ehcache.xml
 ```

17. Max field length for data validations.

 ```
 # Fortress Data Validation settings
 field.length=130
 ```

18. LDAP encoding defaults.  Used to perform secure encoding on data input to prevent injection attacks.

 ```
 #  This section is for filtering out LDAP meta characters from search field input:
 #  Ensure the chars are placed in ASCII value ascending order.
 # This must match the total number of items that need to be filtered in our list:
 ldap.filter.size=15

 #! 33 0041 0x21
 ldap.filter.1=!
 ldap.sub.1=21
 #% 37 0045 0x25
 ldap.filter.2=%
 ldap.sub.2=25
 #& 38 0046 0x26
 ldap.filter.3=&
 ldap.sub.3=26
 #( 40 0050 0x28
 ldap.filter.4=(
 ldap.sub.4=28
 #) 41 0051 0x29
 ldap.filter.5=)
 ldap.sub.5=29
 #* 42 0052 0x2a
 ldap.filter.6=*
 ldap.sub.6=2a
 #+ 43 0053 0x2b
 ldap.filter.7=+
 ldap.sub.7=2b
 #- 45 0055 0x2d
 ldap.filter.8=-
 ldap.sub.8=2d
 #/ 47 0057 0x2f
 ldap.filter.9=/
 ldap.sub.9=2f
 #< 60 0074 0x3c
 ldap.filter.10=<
 ldap.sub.10=3c
 #= 61 0075 0x3d
 ldap.filter.11==
 ldap.sub.11=3d
 #> 62 0076 0x3e
 ldap.filter.12=>
 ldap.sub.12=3e
 #\ 92 0134 0x5c
 ldap.filter.13=\\
 ldap.sub.13=5c
 #| 124 0174 0x7c
 ldap.filter.14=|
 ldap.sub.14=7c
 #~ 126 0176 0x7e
 ldap.filter.15=~
 ldap.sub.15=7e
 ```

19. Default LDAP data object classes

 ```
 user.objectclass=inetOrgPerson
 group.objectclass=configGroup
 group.protocol=configProtocol
 group.properties=ftProps
 ```

20. Enable RFC2307bis support for Users and Roles (groups)
 Setting this prop to true requires the RFC2307bis schema to be present in ldap server. This defines the posixAccount and posixGroup object classes as auxiliary not structural.
 This will add uidNumber, gidNumber, uidNumber and homeDirectory to Users and gidNumber to Roles. Those attributes are required and will be automatically generated if not otherwise passed in.

 ```
 # Boolean value. If true, requires rfc2307bis schema because posixUser and posixGroup must be auxiliary object classes to work with ftRls which is structural..
 rfc2307=true
 ```

21. Disable OpenLDAP audit operations.  If server type is OpenLDAP *ldap.server.type-slapd*, setting this value to true will disable all auditing operations.  Default is false.

 ```
 disable.audit=true
 ```

22. Define delimiter to use for storage of fortress temporal constraints in LDAP.  It is used to delimit fields that are combined into a single attribute, i.e. ftConstraint.

  ```
  # Use '$' as delimiter
  attr.delimiter=$
  ```

23. These properties still have some wiring inside fortress but aren't typically used or needed.

 ```
 dao.connector=apache
 #keep alphanumerics and dashes
 regXSafetext=^A-Za-z0-9- .
 crypto.prop=${crypto.prop}
 clientside.sorting=true
 user.session.props.enabled
 ```

 ___________________________________________________________________________________
 #### END OF README