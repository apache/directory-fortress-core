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
# README-CONFIG.txt for Fortress Core Identity and Access Management SDK
# Version 1.0-RC41
# This file contains an overview of the fortress configuration system.

___________________________________________________________________________________
###################################################################################
# SECTION 1.  Overview
###################################################################################

Fortress obtains its configuration information from the following three sources:
1. fortress.properties file.
2. Java system properties
3. LDAP entry

Of the three listed, 1 is mandatory, 2 & 3 are optional.  This means you may copy everything into the fortress.properties
file and forgo the usage of the other two.  The precedence is same order as listed.

A. fortress.property file:
###################################################################################
The general idea is the property file contains the coordinates to locate the config entry stored in LDAP.

B. Java system properties:
###################################################################################
The system properties are only there to allow external subsystems the ability to poke properties in at runtime.  A use case for #2 is a deployed war file, i.e. fortress-web,
that is downloaded from the Internet and needs to have its ldap configuration parameters overridden to point to proper location.

You can only change these values using the system properties, which are LDAP connection coordinates to config LDAP entry:
  fortress.host
  fortress.port
  fortress.admin.user
  fortress.admin.pw
  fortress.min.admin.conn
  fortress.max.admin.conn
  fortress.enable.ldap.ssl
  fortress.enable.ldap.ssl.debug
  fortress.trust.store
  fortress.trust.store.password
  fortress.trust.store.set.prop
  fortress.config.realm
  fortress.ldap.server.type

C. LDAP entry:
###################################################################################
The idea is to store most of the parameters inside the LDAP config node to cut back on the number of places things must change.

The fortress configuration subsystem determines the location of the configuration node using the following properties located
inside either fortress.properties, or overriden by system properties:

# This node contains fortress properties stored on behalf of connecting LDAP clients:
config.realm=DEFAULT
config.root=ou=Config,dc=example,dc=com

The properties are stored inside the configuration node in name:value format.  You can view the config node of a
working fortress DIT to see what's there.

___________________________________________________________________________________
###################################################################################
# SECTION 2.  How It Works
###################################################################################

The build.properties file is used by the fortress core ant script, build.xml, to push attribute values into the following locations:
1. fortress.properties
2. refreshLDAPData.xml - this is the base load script that sets up the DIT structure and populates the config node in LDAP

The fortress.properties are then loaded onto the classpath where it will be found by fortress.  The refreshLDAPData.xml is
the base load script that can be loaded using this command:a
# mvn install -Dload.file=./ldap/setup/refreshLDAPData.xml

Anytime you need to refresh the values contained inside the other files, run this command:
# mvn install

This will then use the maven-antrun-plugin to kick off the following ant target within the build.xml file:

<ant antfile="${basedir}/build.xml" target="init-fortress-config" />

Which then copies values from here:
fortress.properties.src
refreshLDAPData-src.xml

Replaces values inside the src file tokens @NAME@ with the corresponding value found inside the build.properties.

For more info on which parameters are used where, look at the init-fortress-config target located inside the build.xml file.