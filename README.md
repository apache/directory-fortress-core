
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
 * last updated: January 5, 2016

-------------------------------------------------------------------------------
# Table of Contents
 * Document Overview
 * Tips for new users of Apache Fortress
 * SECTION 0.  Prerequisites for Apache Fortress Core SDK installation and usage
 * SECTION 1.  Options for installing OpenLDAP to target server environment
 * SECTION 2.  Instructions to pull Apache Fortress Core source code from Apache GIT
 * SECTION 3.  Instructions to build software distribution packages using maven 'install'.
 * SECTION 4.  Instructions to configure SDK for target system using build.properties file.
 * SECTION 5.  Instructions for using Apache Fortress and ApacheDS LDAP server.
 * SECTION 6.  Instructions for using Apache Fortress and pre-existing or native OpenLDAP installation.
 * SECTION 7.  Instructions for using Apache Fortress and Symas OpenLDAP server.
 * SECTION 8.  Instructions to integration test using 'FortressJUnitTest' maven target
 * SECTION 9.  Instructions to run the command line interpreter (CLI) utility using 'cli' target
 * SECTION 10. Instructions to load policy data using maven fortress-load.
 * SECTION 11. Instructions to build and test the Apache Fortress samples with 'test-samples' target
 * SECTION 12. Instructions to run the command console using 'console' target
 * SECTION 13. Instructions to performance test fortress core using maven loadtest profile and jmeter.
 * SECTION 14. Instructions to encrypt LDAP passwords used in config files.

___________________________________________________________________________________
# Document Overview
This document contains instructions to download, compile, load and test Fortress with an server.
If you don't already have an LDAP server installed, goto **SECTION 1**.

___________________________________________________________________________________
#  Tips for new users
 * When you read:
   * FORTRESS_HOME, refer to the package root of the openldap-fortress-core project download.
   * OPENLDAP_HOME, refer to the root of OpenLDAP binary installation folder, e.g. /opt/etc/openldap
   * ANT_HOME, refer to the package root of the target machine's ant distribution package.
   * M2_HOME, refer to the package root of the target machine's maven distribution package.
 * This package uses Apache Maven [pom.xml](./pom.xml).
 * Also still uses Apache Ant [build.xml](./build.xml) for seeding configuration data onto the target ldap server.  Checkout the configuration subsystem [README-CONFIG.md](./README-CONFIG.md) for info on how it works.
 * Questions pertaining to usage of this software package should be directed to its mailing list:
    http://mail-archives.apache.org/mod_mbox/directory-fortress/

-------------------------------------------------------------------------------
# SECTION 0 - Prerequisites

Minimum hardware requirements:
 * 2 Cores
 * 4GB RAM

Minimum software requirements:
 * Java SDK 7++
 * git
 * Apache Ant 1.7++
 * Apache Maven3++

Notes:
 * Tested on Debian, Centos and Windows machines.
 * Apache Fortress is LDAPv3 compliant and works with any directory server.
    * ApacheDS & OpenLDAP are supported.

___________________________________________________________________________________
# SECTION 1.  Options for using Apache Fortress and LDAP server.

This document contains three options for installing Apache Fortress and configuration with an LDAP server:

1. Apache Fortress and ApacheDS LdAP server (recommended for first-time users).
 * Do **SECTION 5**  Instructions for using Apache Fortress and ApacheDS LDAP server.
2. Apache Fortress and SYMAS OpenLDAP server
 * Do **SECTION 6** Instructions for using Apache Fortress and Symas OpenLDAP server.
3. Apache Fortress and existing OpenLDAP server
 * Follow setups under SECTIONS 2, 3, 4, 7, 8

___________________________________________________________________________________
# SECTION 2. Instructions to pull Apache Fortress source code from Apache GIT

SNAPSHOTs from OpenLDAP's GIT Software Repo:
https://git-wip-us.apache.org/repos/asf?p=directory-fortress-core.git

Clone the Apache Fortress Core Git Repo::
 ```
 git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-core.git
 ```
___________________________________________________________________________________
# SECTION 3. Instructions to build software distribution packages using maven 'install'.

NOTE: The Fortress pom.xml may run without connection to Internet iff:
 * The binary dependencies are already present in local maven repo.

1. Set Java and Maven home on machines.
2. From the FORTRESS_HOME root folder, enter the following command:

 ```
 mvn clean install
 ```
 Install notes:
 * Fortress source modules will be compiled along with production of java archive (jar)
  files, javadoc and test distributions.
 * Project artifacts are loaded into $FORTRESS_HOME/target location.

3. From the FORTRESS_HOME root folder, enter the following command:

 ```
 mvn javadoc:javadoc
 ```

 Javadoc note: if using java 8, add this param to the pom.xml:
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
___________________________________________________________________________________
# SECTION 4. Instructions to configure SDK for target system using build.properties file.

 * More information about the Fortress Configuration subsystem: [README-CONFIG.md](./README-CONFIG.md)
 * For newcomers just trying to learn the ropes the defaults usually work.
 * Unless you know what you are doing, never change ant substitution parameters within the properties.  These are are anything inside and including '${}'.  i.e. ${param1}.

1. Copy $FORTRESS_HOME/build.properties.example to build.properties

2. Edit the $FORTRESS_HOME/build.properties file.

3. Set the LDAP Host and port properties.  Either a valid host name or IP address can be used.  If you are running the build.xml script from same platform as your
are running OpenLDAP, localhost will do:
host=localhost
port=389

4. Set the suffix name and domain component.  For example suffix.name=example + suffix.dc=com will = 'dc=example,dc=com'.
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

7. Audit settings (openldap only):

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
___________________________________________________________________________________
# SECTION 5. Instructions for using Apache Fortress and ApacheDS LDAP server.

 * Follow these instructions: [README-TEN-MINUTE-GUIDE.md](./README-TEN-MINUTE-GUIDE.md)

___________________________________________________________________________________
# SECTION 6. Instructions for using Apache Fortress and Symas OpenLDAP server.

 * Follow these instructions: [README-QUICKSTART-SLAPD.md](./README-QUICKSTART-SLAPD.md)

___________________________________________________________________________________
# SECTION 7. Instructions for using Apache Fortress and pre-existing or native OpenLDAP installation.

1. Install OpenLDAP using preferred method.

    For example (existing package management system):

        + On Debian systems: http://wiki.debian.org/LDAP/OpenLDAPSetup

        + Ubuntu: https://help.ubuntu.com/community/OpenLDAPServer

        + etc.

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
___________________________________________________________________________________
# SECTION 8. Instructions to integration test using 'FortressJUnitTest' maven target.

From FORTRESS_HOME enter the following command:

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
# SECTION 9. Instructions to run the command line interpreter (CLI) utility using 'cli' target

1. from FORTRESS_HOME enter the following command:

 ```
 mvn -Pcli test
  ```

2. follow instructions in the command line interpreter reference manual contained within the javadoc:

   file:///[directory-fortress-core]/target/site/apidocs/org/apache/directory/fortress/core/cli/package-summary.html

   (where [directory-fortress-core] is location of current source package)

___________________________________________________________________________________
# SECTION 10. Instructions to load policy data using maven fortress-load.

Loads policy data into ldap.

1. Create a load file using examples from FORTRESS_HOME/ldap/setup folder.

e.g. myLoadFile.xml

2. From FORTRESS_HOME folder, enter the following command:

 ```
 mvn install -Dload.file=./ldap/setup/myLoadFile.xml
 ```

Policy Load Notes:
 * This maven target executes FortressAntTask class (as described in FORTRESS_HOME/dist/docs/api/org/openldap/fortress/ant/FortressAntTask.html).
 * Drives Fortress policy apis using a simple xml format.
 * Use to automate user and rbac policy data loads.

___________________________________________________________________________________
# SECTION 11. Instructions to build and test the Apache Fortress samples with 'test-samples' target

1. From FORTRESS_HOME enter the following command:

 ```
 mvn -Dtest=AllSamplesJUnitTest test
 ```

2. View and change the samples here:

 ```
 $FORTRESS_HOME/src/test/com/jts/fortress/samples
 ```

3. Compile and re-run samples to test your changes using:

 ```
 mvn -Dtest=AllSamplesJUnitTest test
 ```

4. View the samples java doc here:

 ```
 $FORTRESS_HOME/dist/docs/samples/index.html
 ```

5. View the fortress-core SDK java doc here:

 ```
 $FORTRESS_HOME/dist/docs/api/index.html
 ```

Testing Sample Notes:
 * Test cases are simple and useful for learning how to code using Fortress A/P/R/BAC APIs.
 * Tests should complete without Junit or ant ERRORS.
 * These tests will load some records into the target ldap server.
 * The target may be run as many times as necessary and should be run at least twice to test the teardown A/P/R/BAC APIs.
 * The 2nd and subsequent times runs, it will tear down the data loaded during the prior run.

___________________________________________________________________________________
# SECTION 12. Instructions to run the command console using 'console' target

1. From FORTRESS_HOME enter the following command:

 ```
 mvn -Pconsole test
 ```

___________________________________________________________________________________
# SECTION 13. Instructions to performance test fortress core using maven loadtest profile and jmeter.

To load test fortress createSession or checkAccess performance using jmeter:

1. Update .jmx located under FORTRESS_HOME/src/test/jmeter folder.

e.g. ftCheckAccess.jmx

2. Load the security policy and users required by the jmeter test routines:

 ```
 mvn install -Dload.file=./ldap/setup/LoadTestUsers.xml
 ```

3. From FORTRESS_HOME folder, enter the following command from a system prompt:

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

1. From FORTRESS_HOME root folder, enter the following command from a system prompt:

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
 # END OF README