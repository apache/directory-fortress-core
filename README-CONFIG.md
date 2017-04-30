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
# README for Apache Fortress Configuration Subsystem
-------------------------------------------------------------------------------

## Table of Contents

 * SECTION 1. Fortress Configuration Subsystem Overview.
 * SECTION 2. Fortress Ant Property Files.
 * SECTION 3. Fortress Configuration Artifacts.
 * SECTION 4. Fortress Configuration Overrides.
 * SECTION 5. Order Artifacts found in the Fortress Configuration Subsystem.
 * SECTION 6. Configuration Subsystem Usage.

-------------------------------------------------------------------------------
## SECTION 1.  Fortress Configuration Subsystem Overview

Fortress uses the following source property files to bootstrap configuration:

1. Apache Ant Property Files:
 * user.properties
 * [slapd.properties](./slapd.properties.example)
 * [build.properties](./build.properties.example)
2. Apache Ant script:
 * [build-config.xml](./build-config.xml)

Which are used to produce new artifacts used by Fortress runtime:

1. [fortress.properties](./config/fortress.properties.src)
2. [refreshLDAPData.xml](./ldap/setup/refreshLDAPData-src.xml)
3. [slapd.conf](./ldap/slapd.conf.src)  (if using openldap)

Notice the substitution parameters, **@name@**, contained within the above config artifacts.  These are used by an Ant config task
  to replace values into the actual target artifact using values found inside the source property files.  Thus Fortress uses simple Apache Ant
  substitution to seed implementation specific variables, e.g. host names, ports, pw's, into its config artifacts used at runtime.

-------------------------------------------------------------------------------
## SECTION 2.  Fortress Ant Property Files

Fortress configuration artifacts are seeded using Ant property files.
 * user.properties  - optional, when found, located in user's home directory.  Properties found here take precedence over those following.
 * slapd.properties - optional, when found, located in root folder of the package.  These props override those found in the build.properties file.
 * build.properties - this file is required and must be located in the root folder of the package.

-------------------------------------------------------------------------------
## SECTION 3.  Fortress Configuration Artifacts

The ant config task, **init-fortress-config**, uses the values found within the Ant property files to seed into the following targets:.
 * fortress.properties - contains name/value pairs inside of a property file.
 * refreshLDAPData.xml - load script to shape DIT and add fortress properties to a remote ldap entry.
 * slapd.conf files - (optional) used to bind fortress to openldap.

-------------------------------------------------------------------------------
## SECTION 4.  Fortress Configuration Overrides

### Optional - Used to override fortress properties at runtime.

 These fortress properties may be overridden at runtime by setting as Java System Properties:
 * fortress.host
 * fortress.port
 * fortress.admin.user
 * fortress.admin.pw
 * fortress.min.admin.conn
 * fortress.max.admin.conn
 * fortress.enable.ldap.ssl
 * fortress.enable.ldap.ssl.debug
 * fortress.trust.store
 * fortress.trust.store.password
 * fortress.trust.store.onclasspath
 * fortress.config.realm
 * fortress.config.root
 * fortress.ldap.server.type

 The minimum system.properties to enable fortress apis to work:
  * fortress.admin.user
  * fortress.admin.pw=secret
  * fortress.config.root=ou=Config,dc=example,dc=com

 If the ldap host and port are not localhost:389 set these two:
 * fortress.host
 * fortress.port

___________________________________________________________________________________
## SECTION 5.  Order Artifacts found in the Fortress Configuration Subsystem

This subsystem has been hard wired to the following order:
 1. fortress.properties file - found on the classpath of that name.
 2. Java system properties - to override any of the 14 properties listed above.
 3. LDAP configuration node - found by config coordinates set in the fortress.properties file itself.

Properties found in LDAP config node will override Java system properties which will override fortress.properties.
__________________________________________________________________________________
## SECTION 6.  Configuration Subsystem Usage

The general flow is the fortress.properties provide the coordinates to locate an ldap entry on a remote server.
That file is found on the runtime classpath during startup.  Some of its props may be overridden as Java system properties.
The combination of Fortress and Java system properties are used to connect to remote ldap server and read its configuration entry where the remainder of Fortress' properties are stored.

The remote server node's dn is constructed from fortress.property values:
```
config.realm=DEFAULT
config.root=ou=Config,@SUFFIX@
```

The above would be combined to create the dn: cn=Default, ou=Config, [whatever the @SUFFIX@ is]

When reinitialization of properties is needed, to the ldap config node or the DIT itself, re-run this command:
```
# mvn install
```

Followed by A:
 ```
 mvn install -Dload.file=./ldap/setup/refreshLDAPData.xml
 ```

Or B:
 ```
 mvn install -Dload.file=./ldap/setup/ConfigNodeUpdate.xml
 ```

A refreshes the entire LDAP server DIT, deletes of all entries under the suffix, recreating the DIT node structure, and re-adding of the config node.
B just updates the config node with the new values, preserving the other data.

### More notes:
 * Use caution when running the **refreshLDAPData.xml** script.  It deletes all nodes below the suffix before readding.
 * To change values in a config node, use a general purpose ldapbrowser.  Fortress has config apis for this (ConfigMgr) to perform CRUD on config data.
 * Another option is to use a script like **ConfigNodeUpdate.xml** to perform the CRUD ops.
 * You can also *simply* place the properties inside the fortress.properties file (only).  The idea is to minimize the number of locations
 where the same data must be stored.  Imagine a network with hundreds, even thousands of fortress agents running.  We don't need to replicate the same data everywhere which is where remote config nodes help.
 * For more info on which parameters are used where, look at the **init-fortress-config** target located inside the [build-config.xml](build-config.xml) file.

 ___________________________________________________________________________________
  #### END OF README-CONFIG