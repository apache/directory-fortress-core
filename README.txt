___________________________________________________________________________________
###################################################################################
README for Fortress Identity and Access Management SDK
Version 1.0.0.RC24
last updated: April 10, 2013

This document contains instructions to download, compile, test and use the
Fortress Identity and Access Management system.
Fortress is released under BSD open source license as specified within this package.
___________________________________________________________________________________
###################################################################################
# SECTION 0:  Prerequisites
###################################################################################
1. Internet access to retrieve source code from OpenLDAP GIT and binary dependencies from online Maven repo.

NOTE: The Fortress build.xml may run without connection to Internet iff:
- The Fortress source modules have been downloaded
- The binary dependencies are already present in FORTRESS_HOME/lib folder
- Local mode has been enabled on target machine.  Local mode can be enabled by adding this property to build.properties:
local.mode=true

2. Java SDK Version 7 or beyond installed to target environment
3. Apache Ant 1.8 or beyond installed to target environment
4. OpenLDAP installed to target system.  (options follow in section 1)
5. GIT installed to target environment. (Fortress developers only)

___________________________________________________________________________________
###################################################################################
# SECTION 1:  Options for installing OpenLDAP to target server environment
###################################################################################

This document includes three options for installing OpenLDAP server:

-------------------------------------------------------------------------------
- INSTALL OPTION 1 - JOSHUATREE SOFTWARE Fortress Quickstart installation packages for OpenLDAP server
-------------------------------------------------------------------------------
- Required Sections to follow:
    2, 3, 4

-------------------------------------------------------------------------------
- INSTALL OPTION 2 - TARGET system package management system for OpenLDAP server
-------------------------------------------------------------------------------
- Required Sections to follow:
    2, 3, 5, 6

-------------------------------------------------------------------------------
- INSTALL OPTION 3 - SYMAS Gold and Silver installation packages for OpenLDAP server
-------------------------------------------------------------------------------
- Required Sections to follow:
    2, 3, 5, 7

___________________________________________________________________________________
###################################################################################
# SECTION 2. Instructions to pull Fortress source code from OpenLDAP GIT
###################################################################################

# If Fortress User

RELEASES from Maven website:
http://search.maven.org/#browse%7C-1179527181

SNAPSHOTs from OpenLDAP's GIT Software Repo:
http://www.openldap.org/devel/gitweb.cgi?p=openldap-fortress-core.git;a=summary

read-only:
>git clone git://git.openldap.org/openldap-fortress-core.git

# If Fortress Developer and have access to GIT repo:

committers: Open a terminal session within preferred folder name/location and enter the following command:
>git clone ssh://git-master.openldap.org/~git/git/openldap-fortress-core.git

This will pull down source code from GIT and load into
the directory from which it ran, hereafter called 'FORTRESS_HOME'.
___________________________________________________________________________________
###################################################################################
# SECTION 3. Instructions to build openldap-fortress-core software distribution packages
###################################################################################

NOTE: The Fortress build.xml may run without connection to Internet iff:
- The binary dependencies are already present in $FORTRESS_HOME/openldap-fortress-core/lib folder
- Local mode has been enabled on target machine.  Local mode can be enabled by adding this property to build.properties:
local.mode=true

a. from the FORTRESS_HOME root folder, enter the following:

>$ANT_HOME/bin/ant dist

- During the above step, Apache Ivy jar will download automatically to the configured $ANT_HOME/lib folder.

- During the above step, fortress dependencies will be downloaded from maven global
  Internet repository using Apache Ivy into $FORTRESS_HOME/openldap-fortress-core/lib.

- Fortress source modules will be compiled along with production of java archive (jar)
  files, javadoc and sample distributions.

- All project artifacts are loaded into $FORTRESS_HOME/openldap-fortress-core/dist location.
___________________________________________________________________________________
###################################################################################
# SECTION 4. Instructions for JOSHUATREE BUILDER installation of OpenLDAP
###################################################################################

a. Go to https://joshuatreesoftware.us/jtspages/download.php

b. Register, pull down the Fortress Builder package to match your target platform.

c. Follow the README-QUICKSTART.txt or more involved README-INSTALL-FORTRESS.txt contained within the Builder package.

d. Execute the 'init-slapd' and 'init-config' targets in Fortress Builder.

e. Add a property to build.properties in this package that points back to where Fortress Builder package resides.

Edit $FORTRESS_HOME/openldap-fortress-core/build.properties and add the following directive:

builder.home=/path to $FORTRESS_BUILDER_HOME/fortressBuilder-[platform]-[version]

e.g.:

builder.home=/home/user/dev/fortressBuilder-Debian-Silver-i386-1.0.0
___________________________________________________________________________________
###################################################################################
# SECTION 5. Instructions to configure openldap-fortress-core SDK for target system
###################################################################################

- This must be done when OpenLDAP is not installed with Fortress Builder.

- The 'init-config' ant target on this project will substitute parameters found in 'build.properties' into their proper location.

- For newcomers just trying to learn the ropes the defaults usually work, especially if you are using the fortress-builder package to install OpenLDAP.

- unless you know what you are doing, never change ant substitution parameters within the properties.  These are are anything inside and including '${}'.  i.e. ${param1}.

a. Edit the $FORTRESS_HOME/openldap-fortress-core/build.properties file.

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

h. (optional) if uninstalling old Symas OpenLDAP, set the slapd.uninstall correct Symas OpenLDAP package name.
for example, if Redhat i386:
slapd.uninstall=rpm -e symas-openldap-gold

i. (option if using Symas OpenLDAP binaries) Point slapdInstall.sh to use correct Symas OpenLDAP installation binaries.
for example for Redhat i386:
slapd.install=rpm -Uvv symas-openldap-gold.i386-2.4.25.110424.rpm

___________________________________________________________________________________
###################################################################################
# SECTION 6. Instructions for using pre-existing or native OpenLDAP installation.
###################################################################################

a. Install OpenLDAP using your existing package management system.

    For example:

        + On Debian systems: http://wiki.debian.org/LDAP/OpenLDAPSetup

        + Ubuntu: https://help.ubuntu.com/community/OpenLDAPServer

        + etc.

- No need to configure or load the OpenLDAP server.  That is handled on step c below.


b. enable the correct installation particulars into FORTRESS_HOME/openldap-fortress-core/build.properties.

    These parameters will need to vary according to how your OpenLDAP system was installed. For Debian OpenLDAP builds,
    use the following:

## If using Debian/Ubuntu OpenLDAP, uncomment this section:
db.dir=/var/lib/ldap
db.hist.dir=${db.dir}/hist
db.bak.dir=/var/lib/ldap-backup/db
db.bak.hist.dir=/var/lib/ldap-backup/hist
slapd.dir=/etc/ldap
pid.dir=/var/run/slapd
slapd.module.dir=/usr/lib/ldap
slapd.start=slapd -f /etc/ldap/slapd.conf
 unless you know what you're doing, take the default:
log.dbnosynch=dbnosync
dflt.dbnosynch=dbnosync
log.checkpoint=checkpoint	4056 60
dflt.checkpoint=checkpoint	1024 60

c. Run the install target:

if Debian sudo:
>sudo $ANT_HOME/bin/ant init-slapd

if not sudo you must run as user that has priv to modify folders in /var and /opt folders:
>su
>$ANT_HOME/bin/ant init-slapd

___________________________________________________________________________________
###################################################################################
# SECTION 7. Instructions for Symas installation of OpenLDAP
###################################################################################

a. Go to http://www.symas.com/index.php/downloads/

b. Register, pull down Silver or Gold packages for target server.

c. copy installation binaries to FORTRESS_HOME/openldap-fortress-core/ldap/setup folder.

d. enable the correct installation particulars into FORTRESS_HOME/openldap-fortress-core/build.properties.

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

From $FORTRESS_HOME/openldap-fortress-core folder, enter the following command from a system prompt:

if Debian sudo:
>sudo $ANT_HOME/bin/ant init-slapd

if not sudo you must run as user that has priv to modify folders in /var and /opt folders:
>su
>$ANT_HOME/bin/ant init-slapd

_______________________________________________________________________________
###############################################################################
# SECTION 8. Instructions to test openldap-fortress-core using regression tests
###############################################################################

a. from the same shell prompt as 2a enter the following:

(if first time regression tests run:)

>$ANT_HOME/bin/ant test-full-init

b. Or for subsequent runs:

>$ANT_HOME/bin/ant test-full

___________________________________________________________________________________
###################################################################################
# SECTION 9. Instructions to run the openldap-fortress-core command line interpreter (CLI) utility
###################################################################################

a. from the same shell prompt as 2a enter the following:

>$ANT_HOME/bin/ant cli

b. follow instructions in the command line interpreter reference manual contained within the javadoc:

$FORTRESS_HOME/openldap-fortress-core/dist/docs/api/com/jts/fortress/cli/package-summary.html

___________________________________________________________________________________
###################################################################################
# SECTION 10. Learn how to use openldap-fortress-core APIs with samples
###################################################################################

a. from the same shell prompt as 2a enter the following:

(if first time sample tests run)

>$ANT_HOME/bin/ant test-samples-init

b. Or if subsequent runs:

>$ANT_HOME/bin/ant test-samples

c. view and change the samples here:

$FORTRESS_HOME/openldap-fortress-core/src/test/com/jts/fortress/samples

d. compile and re-run samples to test your changes using:

>$ANT_HOME/bin/ant test-samples

e. view the samples java doc here:

$FORTRESS_HOME/openldap-fortress-core/dist/docs/samples/index.html

f. view the fortress-core SDK java doc here:

$FORTRESS_HOME/openldap-fortress-core/dist/docs/api/index.html

___________________________________________________________________________________
###################################################################################
# SECTION 11. Instructions to run the openldap-fortress-core command console
###################################################################################

a. from the same shell prompt as 2a enter the following:

>$ANT_HOME/bin/ant console

___________________________________________________________________________________
###################################################################################
# SECTION 12. Instructions to encrypt LDAP passwords used in openldap-fortress-core config files.
###################################################################################

If you need the passwords for LDAP service accounts to be encrypted before loading into Fortress properties files you can
use the 'encrypt' ant target.

a. From FORTRESS_BUILDER_HOME root folder, enter the following command from a system prompt:

>$ANT_HOME/bin/ant encrypt -Dparam1=secret
encrypt:
     [echo] Encrypt a value
     [java] Encrypted value=wApnJUnuYZRBTF1zQNxX/Q==
BUILD SUCCESSFUL
Total time: 1 second

b. Copy the Encrypted value and paste it into the corresponding build.properties setting, e.g.:

# This OpenLDAP admin root pass is bound for fortress.properties and was encrypted using 'encrypt' target in build.xml:
cfg.log.root.pw=wApnJUnuYZRBTF1zQNxX/Q==

___________________________________________________________________________________
###################################################################################
# SECTION 13. Troubleshooting
###################################################################################

a. Problem with javac under sudo

If you see this error:

BUILD FAILED
/home/user/tmp/fortress/13/openldap-fortress-core-302f201/build.xml:233: Unable to find a javac compiler;
com.sun.tools.javac.Main is not on the classpath.
Perhaps JAVA_HOME does not point to the JDK.
It is currently set to "/usr/lib/jvm/java-6-openjdk/jre"

If running sudo:

- Option 1:
sudo apt-get install openjdk-6-jdk

- Option 2:

add this to build.xml javac task:

  	     executable="/opt/jdk1.6.0_27/bin/javac"
         compiler="javac1.6"
         fork = "true"

___________________________________________________________________________________
###################################################################################
# SECTION 14. Instructions to enable Apache Ivy dependency management
###################################################################################

Note:  This is included for informational purposes in case it fails to automatically run during Section #3.

- Apache Ivy is used to retrieve the Java libraries that openldap-fortress-core depends on.

a. Open a shell prompt within the FORTRESS_HOME root folder and enter the following:

>export JAVA_HOME=/path to the root folder of your java SDK
>export ANT_HOME=/path to the root folder of your Apache Ant installation
>$ANT_HOME/bin/ant -buildfile getIvy.xml

- After the above commands are run (also assuming network is good), Apache Ivy library
 will downloaded into ANT_HOME/lib folder.  Ivy is needed to build Fortress.