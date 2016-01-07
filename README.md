
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
# README for Apache Fortress Core
 * Version 1.0-RC41
 * last updated: January 6, 2016

-------------------------------------------------------------------------------
# Table of Contents
 * Document Overview
 * Tips for first-time users of Apache Fortress
 * SECTION 0.  Prerequisites
 * SECTION 1.  Options for using Apache Fortress and LDAP server together.
 * SECTION 2.  Instructions for using Apache Fortress and ApacheDS LDAP server.
 * SECTION 3.  Instructions for using Apache Fortress and Symas OpenLDAP server.
 * SECTION 4.  Instructions to pull Apache Fortress Core source code from Apache GIT
 * SECTION 5.  Instructions to build software package.
 * SECTION 6.  Instructions for installing OpenLDAP.
 * SECTION 7.  Instructions for using Apache Fortress with OpenLDAP.
 * SECTION 8.  Instructions to integration test.
 * SECTION 9.  Instructions to load policy data using maven fortress-load.
 * SECTION 10. Instructions to run the command line interpreter (CLI) utility.
 * SECTION 11. Instructions to run the command console.
 * SECTION 12. Instructions to build and test the Apache Fortress samples.
 * SECTION 13. Instructions to performance test.
 * SECTION 14. Instructions to encrypt LDAP passwords used in config files.

___________________________________________________________________________________
# Document Overview
This document contains instructions to download, compile, load and test Fortress with an LDAP server.
If you don't already have an LDAP server installed, goto **SECTION 1** for options.

___________________________________________________________________________________
#  Tips for first-time users
 * Apache Fortress uses an LDAP server to store its policy data.  For options on how to set one up, see **SECTION 1**
 * If you see **FORTRESS_HOME**, refer to the package [root folder](.).
 * If you see **OPENLDAP_HOME**, refer to the root of OpenLDAP binary installation folder, e.g. /opt/etc/openldap
 * This package's Apache Maven [pom.xml](./pom.xml) and Apache Ant [build.xml](./build.xml) files are also found in root folder.
 * Apache Ant usage is deprecated, but is still needed to seed initial config data onto target ldap server.
  * The configuration subsystem [README-CONFIG.md](./README-CONFIG.md) has more details.
  * Ant does not need to be installed to your target machine.
 * To understand API usage, check out the [samples](./src/test/java/org/apache/directory/fortress/core/samples).
 * Questions about this software package should be directed to its mailing list:
   * http://mail-archives.apache.org/mod_mbox/directory-fortress/

-------------------------------------------------------------------------------
# SECTION 0. Prerequisites

Minimum hardware requirements to test on machine with an LDAP server:
 * 2 Cores
 * 4GB RAM

Minimum software requirements:
 * Java SDK 7++
 * Apache Maven3++
 * git

Notes:
 * Apache Fortress is LDAPv3 compliant.
 * ApacheDS & OpenLDAP are supported options.
 * Tested on Debian, Centos and Windows machines.

___________________________________________________________________________________
# SECTION 1.  Options for using Apache Fortress and LDAP server together.

This document contains three options for installing Apache Fortress and configuration with an LDAP server:

1. Apache Fortress and ApacheDS LDAP server (recommended for first-time users).
 * Do **SECTION 2**  Instructions for using Apache Fortress and ApacheDS LDAP server.
2. Apache Fortress and SYMAS OpenLDAP server
 * Do **SECTION 3** Apache Fortress and Symas OpenLDAP server.
3. Apache Fortress and pre-existing OpenLDAP server
 * Do SECTIONS 4 - 8

___________________________________________________________________________________
# SECTION 2. Instructions for using Apache Fortress and ApacheDS LDAP server.

 * Follow these instructions: [README-TEN-MINUTE-GUIDE.md](./README-TEN-MINUTE-GUIDE.md)

___________________________________________________________________________________
# SECTION 3. Instructions for using Apache Fortress and Symas OpenLDAP server.

 * Follow these instructions: [README-QUICKSTART-SLAPD.md](./README-QUICKSTART-SLAPD.md)

___________________________________________________________________________________
# SECTION 4. Instructions to pull Apache Fortress source code from Apache GIT repo

SNAPSHOTs from Apache GIT Software Repo:
https://git-wip-us.apache.org/repos/asf?p=directory-fortress-core.git

Clone the Apache Fortress Core Git Repo::
 ```
 git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-core.git
 ```
___________________________________________________________________________________
# SECTION 5. Instructions to build software package.

1. Set Java and Maven home on machines.
2. From the FORTRESS_HOME root folder, enter the following command:

 ```
 mvn clean install
 ```

3. From the FORTRESS_HOME root folder, enter the following command:

 ```
 mvn javadoc:javadoc
 ```

 If using java 8, add this param to the pom.xml:
 ```
<plugin>
    ...
    <artifactId>maven-javadoc-plugin</artifactId>
    <configuration>
        <additionalparam>-Xdoclint:none</additionalparam>
        ...
    </configuration>
</plugin>
 ```

4. View the generated document here: [overview-summary.html](./target/site/apidocs/overview-summary.html).

Build Notes:
 * The Apache Fortress [pom.xml](./pom.xml) may run without connection to Internet iff its dependencies are already present in local or intermediate maven repo.
 * Running ```mvn install``` calls out to maven-ant **init-fortress-config** task in [build.xml](./build.xml) to seed properties (more info here: [README-CONFIG](./README-CONFIG.md)).

___________________________________________________________________________________
# SECTION 6. Instructions for installing OpenLDAP.

1. Install OpenLDAP using preferred method.
 * For example (existing package management system):
   * On Debian systems: http://wiki.debian.org/LDAP/OpenLDAPSetup
   * Ubuntu: https://help.ubuntu.com/community/OpenLDAPServer
   * etc.

2. Copy fortress schema to openldap schema folder:

 ```
 cp FORTRESS_HOME/ldap/schema/fortress.schema OPENLDAP_HOME/etc/openldap/schema
 ```

3. Enable Fortress schema in slapd.conf:

 ```
include		OPENLDAP_HOME/etc/openldap/schema/fortress.schema
 ```

4. For password policy support, enable pwpolicy overlay in slapd.conf:

 ```
 moduleload	 ppolicy.la
 ```

5. For Fortress audit support, enable slapoaccesslog in slapd.conf:

 ```
 moduleload  accesslog.la
 ```

6. Add Fortress audit log settings to slapd.conf:

 ```
 # History DB Settings  (optional, use only if fortress audit is needed)
 # note: the following settings may be tailored to your requirements:
 database	 mdb
 maxreaders 64
 maxsize 1000000000
 suffix		"cn=log"
 rootdn      "cn=Manager,cn=log"
 rootpw      "{SSHA}pSOV2TpCxj2NMACijkcMko4fGrFopctU"
 index objectClass,reqDN,reqAuthzID,reqStart,reqAttr eq
 directory	"/var/openldap/hist"
 access to *
    by dn.base="cn=Manager,cn=log" write
 dbnosync
 checkpoint   64 5
 ```

7. Add Fortress default DB settings to slapd.conf:

 ```
 # Default DB Settings
 # note: the following settings may be tailored to your requirements:
 database	mdb
 maxreaders 64
 maxsize 1000000000
 suffix		"dc=example,dc=com"
 rootdn      "cn=Manager,dc=example,dc=com"
 rootpw      "{SSHA}pSOV2TpCxj2NMACijkcMko4fGrFopctU"

 index uidNumber,gidNumber,objectclass eq
 index cn,sn,ftObjNm,ftOpNm,ftRoleName,uid,ou eq,sub
 index ftId,ftPermName,ftRoles,ftUsers,ftRA,ftARA eq

 directory	"/var/openldap/dflt"
 overlay accesslog
 logdb   "cn=log"
 dbnosync
 checkpoint	64 5
 ```

8. More Fortress audit log settings in slapd.conf:

 ```
 # Audit Log Settings (optional, use only if fortress audit is needed)
 # note: the following settings may be tailored to your requirements:
 logops bind writes compare
 logoldattr ftModifier ftModCode ftModId ftRC ftRA ftARC ftARA ftCstr ftId ftPermName ftObjNm ftOpNm ftObjId ftGroups ftRoles ftUsers ftType
 logpurge 5+00:00 1+00:00
 ```

 * More information about the Fortress Configuration subsystem: [README-CONFIG.md](./README-CONFIG.md)
 * For newcomers just trying to learn the ropes the defaults usually work.
 * Unless you know what you are doing, never change ant substitution parameters within the properties.  These are are anything inside and including '${}'.  i.e. ${param1}.

___________________________________________________________________________________
# SECTION 7. Instructions for using Apache Fortress with OpenLDAP.

1. Copy FORTRESS_HOME/build.properties.example to build.properties
 ```
 cp build.properties.example build.properties
 cp slapd.properties.example slapd.properties
 ```

2. Edit the slapd install properties file:
 ```
 vi slapd.properties
 ```

3. Set the LDAP Host and port properties.  Either a valid host name or IP address can be used.  If you are running on the same platform as your LDAP server, localhost will do:

 ```
 host=localhost
 port=389
 ```

4. Set the suffix name and domain component.  These will be according to your requirements.  For example suffix.name=example + suffix.dc=com will = 'dc=example,dc=com'.
 ```
 suffix.name=example
 suffix.dc=com
 ```

5. Set the administrative LDAP connection pool parameters:
 ```
 # This value contains dn of user that has read/write access to LDAP DIT:
 root.dn=cn=Manager,${suffix}

 # This password is for above admin dn, will be stored in OpenLDAP *slapd.conf*.  It may be hashed using OpenLDAP *slappasswd* command before placing here:
 root.pw={SSHA}pSOV2TpCxj2NMACijkcMko4fGrFopctU

 # This is password is for same user but will be stored as property in *fortress.properties* file.
 cfg.root.pw=secret

 # These properties specify the min/max settings for connection pool containing read/write connections to LDAP DIT:
 admin.min.conn=1

 # You may need to experiment to determine optimal setting for max.  It should be much less than concurrent number of users.
 admin.max.conn=10
 ```

6. Set user authentication connection pool parameters:
 ```
 user.min.conn=1
 # You may need to experiment to determine optimal setting for max.  It should be much less than concurrent number of users.
 user.max.conn=10
 ```

7. Audit settings (OpenLDAP only):

 ```
 # If you don't have slapo-access log overlay enabled, then disable the Fortress audit:
 # Default is false.  This only works if ldap.server.type=openldap:
 disable.audit=true

 # Set the audit connection pool parameters:
 # This value contains dn of user that has read/write access to OpenLDAP slapd access log entries:
 log.root.dn=cn=Manager,${log.suffix}

 # This password is for above log user dn, will be stored in OpenLDAP *slapd.conf*.  It may be hashed using OpenLDAP *slappasswd* command before placing here:
 log.root.pw={SSHA}pSOV2TpCxj2NMACijkcMko4fGrFopctU

 # This password is for same log user but will be stored as property in *fortress.properties* file.:
 cfg.log.root.pw=secret

 # number of connections in pool (to query the slapo access log data):
 log.min.conn=1
 log.max.conn=3

 # Set more audit logger parameters (openldap only):
 log.suffix=cn=log

 # To enable slapd persistence on the following OpenLDAP operations:
 log.ops=logops search bind writes
 ```

8. Perform the base load:
 ```
 mvn install -Dload.file=./ldap/setup/refreshLDAPData.xml
 ```

Usage Notes:
 * Use caution when running the -Dload.file target with **refreshLDAPData.xml** as that script deletes all nodes beneath the suffix and readds.
 * Sets up the basic Directory Information Tree format and remote configuration nodes (more info here: [README-CONFIG](./README-CONFIG.md)).

___________________________________________________________________________________
# SECTION 8. Instructions to integration test.

From **FORTRESS_HOME** enter the following command:

 ```
 mvn -Dtest=FortressJUnitTest test
 ```

 Test Notes:
  * If tests complete without Junit ERRORS, current Fortress is certified to run on target ldap server.
  * Tests load thousands of records into target ldap server.
  * The 2nd and subsequent time tests runs, teardown of data from prior run occurs.
  * Should be run at least twice to verify Fortress Core teardown API success.
  * After this target runs, the organizationalUnit structure must remain in target LDAP DIT.
  * WARNING log messages are good as these are negative tests in action:

___________________________________________________________________________________
# SECTION 9. Instructions to load policy data using maven fortress-load.

Loads policy data into ldap.

1. Create a load file using examples from **FORTRESS_HOME**/ldap/setup folder.

 e.g. myLoadFile.xml

2. From **FORTRESS_HOME** folder, enter the following command:

 ```
 mvn install -Dload.file=./ldap/setup/myLoadFile.xml
 ```

Policy Load Notes:
 * This maven target executes **FortressAntTask** to automate data loads into the LDAP server using the Fortress APIs.
 * More info on ant load utility in the generated javadoc:
  * [./target/site/apidocs/org/apache/directory/fortress/core/ant/FortressAntTask.html](./target/site/apidocs/org/apache/directory/fortress/core/ant/FortressAntTask.html)

___________________________________________________________________________________
# SECTION 10. Instructions to run the command line interpreter (CLI) utility.

1. From **FORTRESS_HOME** enter the following command:

 ```
 mvn -Pcli test
  ```

2. follow instructions in the command line interpreter reference manual contained within the generated javadoc:
 * [./target/site/apidocs/org/apache/directory/fortress/core/cli/package-summary.html/package-summary.html](./target/site/apidocs/org/apache/directory/fortress/core/cli/package-summary.html)

___________________________________________________________________________________
# SECTION 11. Instructions to run the command console.

1. From **FORTRESS_HOME** enter the following command:

 ```
 mvn -Pconsole test
 ```
___________________________________________________________________________________
# SECTION 12. Instructions to build and test the Apache Fortress samples.

1. From **FORTRESS_HOME** enter the following command:

 ```
 mvn -Dtest=AllSamplesJUnitTest test
 ```

2. View and change the samples here:
 [./src/test/java/org/apache/directory/fortress/core/samples](./src/test/java/org/apache/directory/fortress/core/samples)

3. Compile and re-run samples to test your changes using:

 ```
 mvn -Dtest=AllSamplesJUnitTest test
 ```

Sample Notes:
 * Test cases are simple and useful for learning how to code using Apache Fortress APIs.
 * Tests should complete without ERRORS.
 * These tests will load some records into the target ldap server.
 * The target may be run as many times as necessary and should be run at least twice to test the teardown.
 * The 2nd and subsequent times runs, it will tear down the data loaded during the prior run.

___________________________________________________________________________________
# SECTION 13. Instructions to performance test.

To load test fortress createSession or checkAccess performance using jmeter:

1. Update .jmx located under **FORTRESS_HOME**/src/test/jmeter folder.

e.g. ftCheckAccess.jmx

2. Load the security policy and users required by the jmeter test routines:

 ```
 mvn install -Dload.file=./ldap/setup/LoadTestUsers.xml
 ```

3. From **FORTRESS_HOME** folder, enter the following command from a system prompt:

 ```
 mvn -Ploadtest-ftca jmeter:jmeter
 ```

Load Testing Notes:
 * The above maps to ftCheckAccess.jmx
 * jmx files with prefex 'ac' call fortress accelerator functions (which requires special setup NOT covered by this document)
 * jmx files with prefix 'ft' are for fortress functions (which are covered by this document)

___________________________________________________________________________________
# SECTION 14. Instructions to encrypt LDAP passwords used in config files.

If you need the passwords for LDAP service accounts to be encrypted before loading into Fortress properties files you can
use the 'encrypt' ant target.

1. From **FORTRESS_HOME** root folder, enter the following command from a system prompt:

 ```
 ant encrypt -Dparam1=secret
 encrypt:
      [echo] Encrypt a value
      [java] Encrypted value=wApnJUnuYZRBTF1zQNxX/Q==
 BUILD SUCCESSFUL
 Total time: 1 second
 ```

2. Copy the hashed value and paste it into the corresponding build.properties setting, e.g.:

 ```
 # This OpenLDAP admin root pass is bound for fortress.properties and was hashed using 'encrypt' target in build.xml:
 cfg.log.root.pw=wApnJUnuYZRBTF1zQNxX/Q==
 ```

 ___________________________________________________________________________________
 #### END OF README