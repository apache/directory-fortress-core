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
# README for Fortress Core Identity and Access Management SDK
# Version 1.0-RC40
# last updated: February 5, 2015
###################################################################################
# Table of Contents
###################################################################################
# Document Overview
# Tips for users of Fortress Core
# SECTION 0.  Prerequisites for Fortress Core SDK installation and usage
# SECTION 1.  Options for installing OpenLDAP to target server environment
# SECTION 2.  Instructions to pull Fortress Core source code from Apache GIT
# SECTION 3.  Instructions to build software distribution packages using maven 'install'.
# SECTION 4.  Instructions to build software distribution packages using ant 'dist' target.
# SECTION 5.  Instructions to configure SDK for target system using build.properties file.
# SECTION 6.  Instructions for pre-existing or native OpenLDAP installation using 'load-slapd' target.
# SECTION 7.  Instructions for Symas installation of OpenLDAP - using 'init-slapd' target
# SECTION 8.  Instructions to integration test using 'FortressJUnitTest' maven target
# SECTION 9.  Instructions to run the command line interpreter (CLI) utility using 'cli' target
# SECTION 10. Instructions to load policy data using maven fortress-load.
# SECTION 11. Instructions to build and test the fortress samples with 'test-samples' target
# SECTION 12. Instructions to run the command console using 'console' target
# SECTION 13. Instructions to performance test fortress core using maven loadtest profile and jmeter.
# SECTION 14. Instructions to encrypt LDAP passwords used in config files.

___________________________________________________________________________________
###################################################################################
# Document Overview
###################################################################################
This document contains instructions to download, compile, load and test Fortress SDK with LDAP system.
If you don't already have an LDAP server installed, options follow in subsequent sections.
___________________________________________________________________________________
###################################################################################
#  Tips for users of Fortress Core
###################################################################################

 - Definitions: When you read:
   + FORTRESS_HOME, refer to the package root of the openldap-fortress-core project download.
   + OPENLDAP_HOME, refer to the root of OpenLDAP binary installation folder, e.g. /opt/etc/openldap
   + ANT_HOME, refer to the package root of the target machine's ant distribution package.
   + M2_HOME, refer to the package root of the target machine's maven distribution package.

 - New users check out the 10 minute guide, instructions inside README-TEN-MINUTE-GUIDE.txt

 - Fortress Core package operations originally were designed to work using Ant.  Today, these same operations are being moved into
   the maven pom.xml.  Both still work, but ant is being phased out.

 - This software packages preferred means to build and install uses Apache Maven (script FORTRESS_HOME/pom.xml) - Sections 3,8,13

 - This software package still uses Apache Ant (script FORTRESS_HOME/build.xml) for many utility functions listed in this README - Sections 6,7,9-12.

 - The targets may be used to the fortress security policy data contained within an existing LDAP server.

 - This document describes the most important targets to start using fortress.  For a complete list, enter (from FORTRESS_HOME):
   $ANT_HOME/bin/ant -p

 - Or view the maven script (FORTRESS_HOME/pom.xml) and ant script (FORTRESS_HOME/build.xml).

 - Questions pertaining to usage of this software may be submitted to:
    http://mail-archives.apache.org/mod_mbox/directory-fortress/
___________________________________________________________________________________
###################################################################################
# SECTION 0.  Prerequisites for Fortress Core SDK installation and usage
###################################################################################
1. Internet access to retrieve source code from Apache Fortress Core GIT and binary dependencies from online Maven repo.
Fortress installation procedures use Maven and Apache Ant.
The ant targets need external access to the Internet to pull down dependencies but may run without IFF:
a. The necessary binary jars are already present and loaded into FORTRESS_HOME/lib folder.  For list of dependency jars check out the ivy.xml file.
b. Local mode has been enabled in target runtime env.  This can be done by adding the following to build.properties file:

local.mode=true

More prereqs:

2. Java SDK Version 7 or beyond installed to target environment

3. Apache Maven 3 installed to target environment

4. LDAP server installed.  (options follow in section 1).

Prereq notes:

 - Fortress is LDAPv3 compliant and works with any directory server.
 - Tested with ApacheDS: FORTRESS_HOME/deprecate/README-QUICKSTART-APACHEDS.html.
 - Tested with OpenLDAP: FORTRESS_HOME/deprecate/README-QUICKSTART.html.
___________________________________________________________________________________
###################################################################################
# SECTION 1.  Options for installing OpenLDAP to target server environment
###################################################################################

This document includes three options for use of Fortress and LDAP server:

-------------------------------------------------------------------------------
- INSTALL OPTION 1 - Fortress 10 Minute Guide - recommended for first-time users
-------------------------------------------------------------------------------
- Covered by README-TEN-MINUTE-GUIDE.txt

-------------------------------------------------------------------------------
- INSTALL OPTION 2 - TARGET operating system's OpenLDAP server
-------------------------------------------------------------------------------
- Required Sections to follow:
    2, 4, 5, 6, 8

-------------------------------------------------------------------------------
- INSTALL OPTION 3 - SYMAS Gold and Silver installation packages for OpenLDAP server
-------------------------------------------------------------------------------
- Required Sections to follow:
    2, 4, 5, 7, 8
___________________________________________________________________________________
###################################################################################
# SECTION 2. Instructions to pull Fortress source code from Apache GIT
###################################################################################

SNAPSHOTs from OpenLDAP's GIT Software Repo:
https://git-wip-us.apache.org/repos/asf?p=directory-fortress-core.git

Clone the Apache Fortress Core Git Repo::
# git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-core.git

___________________________________________________________________________________
###################################################################################
# SECTION 3. Instructions to build software distribution packages using maven 'install'.
###################################################################################

NOTE: The Fortress pom.xml may run without connection to Internet iff:
- The binary dependencies are already present in local maven repo.

a. set JAVA_HOME per target machine

for example:
# export JAVA_HOME=/opt/jdk1.7.0_10

b. set M2_HOME per target machine:

for example:
# export M2_HOME=/usr/share/maven

c. from the FORTRESS_HOME root folder, enter the following command:

# $M2_HOME/bin/mvn install -DskipTests

d. from the FORTRESS_HOME root folder, enter the following command:

# $M2_HOME/bin/mvn javadoc:javadoc

install notes:

- Fortress source modules will be compiled along with production of java archive (jar)
  files, javadoc and test distributions.

- Project artifacts are loaded into $FORTRESS_HOME/target location.
___________________________________________________________________________________
###################################################################################
# SECTION 4. Instructions to build software distribution packages using ant 'dist' target.
###################################################################################

NOTE: The ant targets still work but are being phased out.  They are still needed for running
many of the utilities.

NOTE: The Fortress build.xml may run without connection to Internet iff:
- The binary dependencies are already present in $FORTRESS_HOME/lib folder
- Local mode has been enabled on target machine.  Local mode can be enabled by adding this property to build.properties:
local.mode=true

a. set JAVA_HOME per target machine

for example:
# export JAVA_HOME=/opt/jdk1.7.0_10

b. set ANT_HOME per target machine:

for example:
# export ANT_HOME=/home/user/apache-ant-1.8.2

c. from the FORTRESS_HOME root folder, enter the following command:

# $ANT_HOME/bin/ant dist

dist notes:

- Apache Ivy jar will download automatically to the configured $ANT_HOME/lib folder.

- Dependencies will be downloaded from maven global Internet repository using Apache Ivy into $FORTRESS_HOME/lib.

- Xml file (FORTRESS_HOME/ivy.xml) contains the list of dependencies.

- Fortress source modules will be compiled along with production of java archive (jar)
  files, javadoc and sample distributions.

- Project artifacts are loaded into $FORTRESS_HOME/dist location.
___________________________________________________________________________________
###################################################################################
# SECTION 5. Instructions to configure SDK for target system using build.properties file.
###################################################################################

- This must be done when OpenLDAP is not installed with the Fortress QUICKSTART package.

- The 'init-config' ant target on this project will substitute parameters found in 'build.properties' into their proper location.

- For newcomers just trying to learn the ropes the defaults usually work.

- unless you know what you are doing, never change ant substitution parameters within the properties.  These are are anything inside and including '${}'.  i.e. ${param1}.

a. Edit the $FORTRESS_HOME/build.properties file.

b. Set the LDAP Host and port properties.  Either a valid host name or IP address can be used.  If you are running the build.xml script from same platform as your
are running OpenLDAP, localhost will do:
host=localhost
port=389

c. Set the suffix name and domain component.  For example suffix.name=example + suffix.dc=com will = 'dc=example,dc=com'.
suffix.name=example
suffix.dc=com

d. Set the administrative LDAP connection pool parameters:

# Set the encryption key value used as key for encryption/decryption commands for fortress-core ldap service account passwords.
crypto.prop=abcd12345

OR leave the value blank if passwords are entered in the clear into property files:
crypto.prop=

Note: This value must be the same as used when password was encrypted (See Section 12)

# This value contains dn of user that has read/write access to LDAP DIT:
root.dn=cn=Manager,${suffix}

# This password is for above admin dn, will be stored in OpenLDAP 'slapd.conf'.  It may be hashed using OpenLDAP 'slappasswd' command before placing here:
root.pw={SSHA}pSOV2TpCxj2NMACijkcMko4fGrFopctU

# This is password is for same user but will be stored as property in fortress.properties file.  It may be encrypted using Fortress' 'encrypt' ant target (see section 12):
cfg.root.pw=W7T0G9hylKZQ4K+DF8gfgA==

# These properties specify the min/max settings for connection pool containing read/write connections to LDAP DIT:
admin.min.conn=1

# You may need to experiment to determine optimal setting for max.  It should be much less than concurrent number of user's.
admin.max.conn=10

e. Set the audit connection pool parameters:

# This value contains dn of user that has read/write access to OpenLDAP slapd access log entries:
log.root.dn=cn=Manager,${log.suffix}

# This password is for above log user dn, will be stored in OpenLDAP 'slapd.conf'.  It may be hashed using OpenLDAP 'slappasswd' command before placing here:
log.root.pw={SSHA}pSOV2TpCxj2NMACijkcMko4fGrFopctU

# This password is for same log user but will be stored as property in fortress.properties file.  It may be encrypted using Fortress' 'encrypt' ant target (see section 12):
cfg.log.root.pw=W7T0G9hylKZQ4K+DF8gfgA==

log.min.conn=1

# You may need to experiment to determine optimal setting for max.  It should be much less than concurrent number of user's.
log.max.conn=3

f. Set more audit logger parameters:
log.suffix=cn=log

# To enable slapd persistence on the following OpenLDAP operations:
log.ops=logops search bind writes

# Or, to disable Fortress audit altogether, use this:
#log.ops=###AuditDisabled

g. Set user authentication connection pool parameters:
user.min.conn=1

# You may need to experiment to determine optimal setting for max.  It should be much less than concurrent number of user's.
user.max.conn=10
___________________________________________________________________________________
###################################################################################
# SECTION 6. Instructions for pre-existing or native OpenLDAP installation using 'load-slapd' target.
###################################################################################

a. Install OpenLDAP using preferred method.

    For example (existing package management system):

        + On Debian systems: http://wiki.debian.org/LDAP/OpenLDAPSetup

        + Ubuntu: https://help.ubuntu.com/community/OpenLDAPServer

        + etc.

b. Copy fortress schema to openldap schema folder:

# cp FORTRESS_HOME/ldap/schema/fortress.schema OPENLDAP_HOME/etc/openldap/schema


c. Enable Fortress schema in slapd.conf:

include		OPENLDAP_HOME/etc/openldap/schema/fortress.schema

note: for steps b above substitute FORTRESS_HOME for root of your Fortress installation.
note: for steps b & c above substitute OPENLDAP_HOME for root of your OPENLDAP installation.


d. For password policy support, enable pwpolicy overlay in slapd.conf:

moduleload	ppolicy.la


e. For Fortress audit support, enable slapoaccesslog in slapd.conf:

moduleload  accesslog.la


f. Add Fortress audit log settings to slapd.conf:

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


g. Add Fortress default DB settings to slapd.conf:

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


h. More Fortress audit log settings in slapd.conf:

# Audit Log Settings (optional, use only if fortress audit is needed)
# note: the following settings may be tailored to your requirements:
logops bind writes compare
logoldattr ftModifier ftModCode ftModId ftRC ftRA ftARC ftARA ftCstr ftId ftPermName ftObjNm ftOpNm ftObjId ftGroups ftRoles ftUsers ftType
logpurge 5+00:00 1+00:00

# Instructions to configure Fortress to work with your customized OpenLDAP instance


i. Gather the following information about your OpenLDAP instance:

i. suffix
ii. host
iii. port
iv. ldap user account that has read/write priv for default DIT (root works)
v. pw for above
vi. ldap user account that has read/write priv for access log DIT (log root works)
vii. pw for above


j. Example OpenLDAP instance:

i. dc=example, dc=com
ii. myhostname
iii. 389
iv. "cn=Manager,dc=example,dc=com"
v. secret
vi. "cn=Manager,cn=log"
vii. secret

h. Modify the build.properties file with settings

k.
suffix.name=example
suffix.dc=com

ii. ldap.host=myhostname

iii. ldap.port=389

iv. root.dn=cn=Manager,${suffix}

v. root.pw=secret
note: the above may be hased using slappasswd

vi. log.root.dn=cn=Manager,${log.suffix}

vii. secret

l. Create the Fortress DIT:

from the FORTRESS_HOME root folder, enter the following:

# $ANT_HOME/bin/ant load-slapd

m. Skip to SECTION 8 to regression test Fortress and OpenLDAP

load-slapd notes:

  - Uses 'admin' target (described in SECTION 13) to seed the configured default database with data, i.e. db.root in build.properties file, using the following files:
    1 - refreshLDAPData.xml - DIT organizationalUnit structure and client config data - required for Fortress Java SDK to work.
    2 - DelegatedAdminManagerLoad.xml - Delegated administration policy - required for EnMasse and Commander web application demonstrations.
    3 - FortressDemoUsers.xml - demo/sample data - not required.
  - for production usage 2 & 3 above may be cleared out using any ldap client tool.

___________________________________________________________________________________
###################################################################################
# SECTION 7. Instructions for Symas installation of OpenLDAP - using 'init-slapd' target
###################################################################################

a. Go to Symas.com downloads section.

b. Register, pull down Silver or Gold packages to match target platform.

c. copy installation binaries to FORTRESS_HOME/ldap/setup folder.

d. enable the correct installation particulars into FORTRESS_HOME/build.properties.

- If using sudo you are required to enter your sudo pw:

- If not using sudo it is important to leave the value empty.  Otherwise the installation will fail.
sudo.pw=

- Make sure you use the correct package name that matches the download from Symas.

# Option 1 - Debian i386 Silver:
platform=Debian-Silver-i386
slapd.install=dpkg -i symas-openldap-silver.32_2.4.26-1_i386.deb
slapd.uninstall=dpkg -r symas-openldap-silver.32

# Option 2 - Debian i386 Gold:
platform=Debian-Gold-i386
slapd.install=dpkg -i symas-openldap-gold.32_2.4.25-110507_i386.deb
slapd.uninstall=dpkg -r symas-openldap-gold.32

# Option 3 - Redhat i386 Silver:
platform=Redhat-Silver-i386
slapd.install=rpm -Uvv symas-openldap-silver.i386-2.4.26.1.rpm
slapd.uninstall=rpm -e symas-openldap-silver

# Option 4 - Redhat i386 Gold:
platform=Redhat-Gold-i386
slapd.install=rpm -Uvv symas-openldap-gold.i386-2.4.25.110424.rpm
slapd.uninstall=rpm -e symas-openldap-gold

e. Run the install target:

From $FORTRESS_HOME root folder, enter the following command from a system prompt:

if Debian sudo:
# sudo $ANT_HOME/bin/ant init-slapd

if not sudo you must run as user that has priv to modify folders in /var and /opt folders:
# su
# $ANT_HOME/bin/ant init-slapd

init-slapd notes:

  - Refreshes database contents by moving default and history database folders to location ${db.root}/backup.
    - per your db.root setting in build.properties file.
  - Seeds LDAP data by calling 'load-slapd' target as described in section above.
_______________________________________________________________________________
###############################################################################
# SECTION 8. Instructions to integration test using 'FortressJUnitTest' maven target
########################################s#######################################

a. from FORTRESS_HOME enter the following command:

# $M2_HOME/bin/mvn -Dtest=FortressJUnitTest test

FortressJUnitTest Notes:

  - If tests complete without Junit ERRORS, current Fortress is certified to run on target ldap server.

  - Tests load thousands of records into target ldap server.

  - The 2nd and subsequent time tests runs, teardown of data from prior run occurs.

  - Should be run at least twice to verify Fortress Core teardown API success.

  - After this target runs, the organizationalUnit structure must remain in target LDAP DIT.
  - The test data may be cleared.
    - One way to clear out the the test data is to run the 'init-slapd' target (described in previous section).
        - If you followed steps from SECTION 6 (existing OpenLDAP server), do NOT run the 'init-slapd' target.

  - WARNING log messages are good as these are negative tests in action:
___________________________________________________________________________________
###################################################################################
# SECTION 9. Instructions to run the command line interpreter (CLI) utility using 'cli' target
###################################################################################

a. from FORTRESS_HOME enter the following command:

# $ANT_HOME/bin/ant cli

b. follow instructions in the command line interpreter reference manual contained within the javadoc:

$FORTRESS_HOME/dist/docs/api/com/jts/fortress/cli/package-summary.html
___________________________________________________________________________________
###################################################################################
# SECTION 10. Instructions to load policy data using maven fortress-load.
###################################################################################

Loads policy data into ldap.

a. Create a load file using examples from FORTRESS_HOME/ldap/setup folder.

e.g. myLoadFile.xml

b. From FORTRESS_HOME folder, enter the following command:

# $M2_HOME/bin/mvn install -Dload.file=./ldap/setup/myLoadFile.xml -DskipTests=true

Notes:
  - This maven target executes FortressAntTask class (as described in FORTRESS_HOME/dist/docs/api/org/openldap/fortress/ant/FortressAntTask.html).
  - Drives Fortress policy apis using a simple xml format.
  - Use to automate user and rbac policy data loads.
___________________________________________________________________________________
###################################################################################
# SECTION 11. Instructions to build and test the fortress samples with 'test-samples' target
###################################################################################

a. from FORTRESS_HOME enter the following command:

# $ANT_HOME/bin/ant test-samples

c. view and change the samples here:

$FORTRESS_HOME/src/test/com/jts/fortress/samples

d. compile and re-run samples to test your changes using:

# $ANT_HOME/bin/ant test-samples

e. view the samples java doc here:

$FORTRESS_HOME/dist/docs/samples/index.html

f. view the fortress-core SDK java doc here:

$FORTRESS_HOME/dist/docs/api/index.html

Testing Notes:

  - Test cases are simple and useful for learning how to code using Fortress A/P/R/BAC APIs.

  - Tests should complete without Junit or ant ERRORS.

  - These tests will load some records into the target ldap server.

  - The 'test-samples' target may be run as many times as necessary and should be run at least twice to test the teardown A/P/R/BAC APIs.

  - The 2nd and subsequent times 'test-samples' runs, it will tear down the data loaded during the prior run.
___________________________________________________________________________________
###################################################################################
# SECTION 12. Instructions to run the command console using 'console' target
###################################################################################

a. from FORTRESS_HOME enter the following command:

# $ANT_HOME/bin/ant console
___________________________________________________________________________________
###################################################################################
# SECTION 13. Instructions to performance test fortress core using maven loadtest profile and jmeter.
###################################################################################

To load test fortress createSession or checkAccess performance using jmeter:

a. Update .jmx located under FORTRESS_HOME/src/test/jmeter folder.

e.g. ftCheckAccess.jmx

b. Load the security policy and users required by the jmeter test routines:

# $M2_HOME/bin/mvn install -Dload.file=./ldap/setup/LoadTestUsers.xml -DskipTests=true

c. From FORTRESS_HOME folder, enter the following command from a system prompt:

# $M2_HOME/bin/mvn -Ploadtest-ftca jmeter:jmeter

Notes:
    - the above maps to ftCheckAccess.jmx
    - jmx files with prefex 'ac' call fortress accelerator functions (which requires special setup NOT covered by this document)
    - jmx files with prefix 'ft' are for fortress functions (which are covered by this document)

___________________________________________________________________________________
###################################################################################
# SECTION 14. Instructions to encrypt LDAP passwords used in config files.
###################################################################################

If you need the passwords for LDAP service accounts to be encrypted before loading into Fortress properties files you can
use the 'encrypt' ant target.

a. From FORTRESS_HOME root folder, enter the following command from a system prompt:

# $ANT_HOME/bin/ant encrypt -Dparam1=secret
encrypt:
     [echo] Encrypt a value
     [java] Encrypted value=wApnJUnuYZRBTF1zQNxX/Q==
BUILD SUCCESSFUL
Total time: 1 second

b. Copy the hashed value and paste it into the corresponding build.properties setting, e.g.:

# This OpenLDAP admin root pass is bound for fortress.properties and was hashed using 'encrypt' target in build.xml:
cfg.log.root.pw=wApnJUnuYZRBTF1zQNxX/Q==
