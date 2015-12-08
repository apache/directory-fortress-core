#
#   Licensed to the Apache Software Foundation (ASF) under one
#   or more contributor license agreements.  See the NOTICE file
#   distributed with this work for additional information
#   regarding copyright ownership.  The ASF licenses this file
#   to you under the Apache License, Version 2.0 (the
#   "License"); you may not use this file except in compliance
#   with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing,
#   software distributed under the License is distributed on an
#   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#   KIND, either express or implied.  See the License for the
#   specific language governing permissions and limitations
#   under the License.
#
___________________________________________________________________________________
###################################################################################
# README-CONFIG.txt for Fortress Core Configuration Usage
# Version 1.0-RC41
# This file contains an overview of the fortress configuration system.
___________________________________________________________________________________
###################################################################################
# SECTION 1.  Fortress Configuration Subsystem Overview
###################################################################################

Fortress obtains all of its configuration settings from the following two locations:

___________________________________________________________________________________
A. Ant Property files - Seeds downstream fortress.properties, refreshLDAP.xml and slapd.conf files.
___________________________________________________________________________________

The ant property subsystem is fed using three files:
i.   user.properties  - optional, when found, located in user's home directory.  Properties found here take precedence over those following.
ii.  slapd.properties - optional, when found, located in root folder of the package.  These props override those found in the build.properties file.
iii. build.properties - this file is required and must be located in the root folder of the package.

The ant configuration task uses the values found above to seed the fortress.properties and the base ldap load script (refreshLDAPData.xml).
It can also be used to populate the slapd.conf (if needing to configure the openldap server to fortress specs).

___________________________________________________________________________________
B. Java System Properties - Optional - Used to override fortress properties at runtime.
___________________________________________________________________________________
These fortress properties may be overridden at runtime by setting as Java System Properties:
i.     fortress.host
ii.    fortress.port
iii.   fortress.admin.user
iv.    fortress.admin.pw
v.     fortress.min.admin.conn
vi.    fortress.max.admin.conn
vii.   fortress.enable.ldap.ssl
viii.  fortress.enable.ldap.ssl.debug
ix.    fortress.trust.store
x.     fortress.trust.store.password
xi.    fortress.trust.store.set.prop
xii.   fortress.config.realm
xiii.  fortress.ldap.server.type

___________________________________________________________________________________
###################################################################################
# SECTION 2.  Fortress Configuration Subsystem Targets
###################################################################################

The ant build property files used by the fortress build.xml ant script, pushes attribute values into:
A. fortress.properties - primary target.  Contains connection coordinates to the remote config node in ldap.
B. refreshLDAPData.xml - secondary target.  Used to populate an actual ldap config node.
C. slapd.conf - this is an optional target - only needed when configuring a new openldap server from scratch.

___________________________________________________________________________________
###################################################################################
# SECTION 3.  Precedence of Artifacts found in the Fortress Configuration Subsystem
###################################################################################

Fortress uses apache commons configuration system to manage its properties inside its Java runtime environment.
This subsystem has been hard wired to pick up properties in the following order:
A. fortress.properties file - found on the classpath of that name.
B. LDAP configuration node - found by config coordinates set in the fortress.properties file itself.
C. Java system properties - to override any of the 13 properties listed above.

These properties are mutable inside the fortress config subsystem which allows C's values to override B's to override A's.

___________________________________________________________________________________
###################################################################################
# SECTION 4.  More on Fortress properties
###################################################################################

The general idea is the fortress.properties are just enough to find the coordinates to locate an ldap entry on a remote server somewhere.
The fortress.properties file is picked off the runtime classpath during startup.  It then will read whatever is stored on the
remote server node in that config node for overrides, followed by those set as java system properties.

The remote server node's dn is constructed using fortress.property values:
config.realm=DEFAULT
config.root=ou=Config,@SUFFIX@

The above would be combined to create the dn:
ou=Config,dc=[whatever the suffix is]

Anytime you need to reinitialize the properties, the ldap config node and the DIT itself, re-run this command:
# mvn install

followed by:
# mvn install -Dload.file=./ldap/setup/refreshLDAPData.xml

If you are needing to make changes to an ldap system already in use, simply modify the values inside fortress.properties directly.  If the config
node must be changed, any general purpose ldapbrowser can be used to to mod its values.  Fortress also has apis to do this, ConfigMgr, for custom
program utilities.

More notes:
- Use caution when running the -Dload.file target with 'refreshLDAPData.xml' as that script also deletes all nodes the suffix before it reloads anew.
- Don't run refreshLDAPData.xml in production.
- There is nothing preventing you from putting all of the fortress properties inside the fortress.properties file and skipping the remote server step.
- We want to minimize the number of locations where the same data must be stored.  Imagine a network with hundreds, even thousands of fortress agents running.
- We don't need to replicate the same data everywhere which is where remote config nodes are used.
- Tailor these procedures to match your requirements.
- For more info on which parameters are used where, look at the init-fortress-config target located inside the build.xml file.