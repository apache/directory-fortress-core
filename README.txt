--------------------------------------------------------------
README to install, configure and use openldap-fortress-core applications
created: November 5, 2011
last updated: November 5, 2011
--------------------------------------------------------------
This document contains instructions to download, compile, test and use Fortress Core SDK in your target development environment.
These instructions are intended for developers who opt to use lear, use or change the APIs but also contains instructions (hints) to obtain and install OpenLDAP but is not document's main intent.
--------------------------------------------------------------

*** Note this is a work in progress. ***

###############################################################################
# SECTION 0:  Prerequisites
###############################################################################
1. Internet access to retrieve Fortress source code from OpenLDAP GIT and binary dependencies from Maven.
2. GIT installed to target environment.
2. Java SDK Version 6 or beyond installed to target environment
3. Apache Ant 1.8 or beyond installed to target environment
4. OpenLDAP server installed configured for Fortress on your network (more on this in step 4)

In addition to the above, software bootstrapping requires the use of Apache Ivy but that component will be setup on step #2 automatically with the getIvy Ant script.

###############################################################################
# SECTION 1:  Installation Options
###############################################################################

There are install options for the openldap-fortress-core application:

-------------------------------------------------------------------------------
Install Option 1:  Utilize pre-existing OpenLDAP installation
-------------------------------------------------------------------------------
Get, install and OpenLDAP via source from openldap.org or using Linux package management.

Required Sections:
2, 3, 4, 5, 7

Optional Sections:
8. 9, 10

-------------------------------------------------------------------------------
Install Option 2:  Use Fortress Builder
-------------------------------------------------------------------------------
Go to https://joshuatreesoftware.us/jtspages/download.php, register and pull down one of the Fortress Builder packages that match your target platform.
Follow these instructions: https://joshuatreesoftware.us/jtspages/install.html

Required Sections:
2, 3, 4

Optional Sections:
8. 9, 10

-------------------------------------------------------------------------------
Install Option 3:  Use Symas OpenLDAP binaries (Gold or Silver)
-------------------------------------------------------------------------------
Go to http://www.symas.com/index.php/downloads/, register, pull down and install Silver or Gold Symas OpenLDAP releases of binaries for your target platform.

Required Sections:
2, 3, 4, 5, 6

Optional Sections:
8. 9, 10

###################################################################################
# SECTION 2. Instructions to check out source by creating a local GIT clone of the openldap-fortress-core repository.
###################################################################################

a. Open a terminal session inside an empty folder on your target system and enter the following command:

git clone ssh://git-master.openldap.org/~git/git/openldap-fortress-core.git

Assuming you have network access after the above command has run all of the fortress source code will be downloaded from GIT
and installed under the selected folder, heretofore called FORTRESS_HOME;

###################################################################################
# SECTION 3. Instructions to download IVY jar for dependency management
###################################################################################

Ivy is used to pull down the 10 or so libraries that the fortress-core project dependent on.

a. Go to shell prompt in the root of your project and enter the following commands:

export JAVA_HOME=/path to the root folder of your java SDK
export ANT_HOME=/path to the root folder of your Apache Ant installation
$ANT_HOME/bin/ant -buildfile getIvy.xml

After the above command is run, the Apache Ivy jar file will be downloaded into the specified ANT_HOME/lib folder.  Ivy is used the first time you compile fortress
(step 3 below) to automatically fetch all of fortress' binary dependencies from maven repo website on Internet.

###################################################################################
# SECTION 4. Instructions to compile and build the Fortress binaries and javadoc
###################################################################################

a. from the same shell prompt as 2a enter the following:

$ANT_HOME/bin/ant dist

After the above step runs, all of the fortress source modules are compiled and the classes are loaded into jars.  Java documentation will also be generated.
These artifacts will be created under FORTRESS_HOME/openldap-fortress-core/dist folder.


###################################################################################
# SECTION 5. Instructions to configure Fortress and OpenLDAP applications on target system
###################################################################################

Notes:

- The 'init-config' ant target on this project will substitute parameters found in 'build.properties' into their proper location.

- For newcomers just trying to learn the ropes the defaults usually work, especially if you are using the fortress-builder package to install OpenLDAP.

- unless you know what you are doing, never change ant substitution parameters within the properties.  These are are anything inside and including '${}'.  i.e. ${param1}.

a. Edit the FORTRESS_HOME/openldap-fortress-core/build.properties file.

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

Note: This value must be the same as used when password was encrypted (See Section 10)

# This value contains dn of user that has read/write access to LDAP DIT:
root.dn=cn=Manager,${suffix}

# This password is for above admin dn, will be stored in OpenLDAP 'slapd.conf'.  It may be hashed using OpenLDAP 'slappasswd' command before placing here:
root.pw={SSHA}pSOV2TpCxj2NMACijkcMko4fGrFopctU

# This is password is for same user but will be stored as property in fortress.properties file.  It may be encrypted using Fortress' 'encrypt' ant target (see section VII):
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

# This password is for same log user but will be stored as property in fortress.properties file.  It may be encrypted using Fortress' 'encrypt' ant target (see section VII):
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

###################################################################################
# SECTION 6. Instructions for using Symas binaries to create new OpenLDAP server.
###################################################################################

a. download Symas OpenLDAP installation binaries from Symas.

b. copy to the FORTRESS_HOME/openldap-fortress-core/ldap/setup

c. enable the correct installation particulars into FORTRESS_HOME/openldap-fortress-core/build.properties.


If using sudo you are required to enter your sudo pw:

note: If not using sudo it is important to leave the value empty.  Otherwise the installation will fail.
sudo.pw=

Make sure you use the correct package name that matches the download from Symas.

# Option 1 - Debian i386 Silver:
#platform=Debian-Silver-i386
#slapd.install=dpkg -i symas-openldap-silver.32_2.4.26-1_i386.deb
#slapd.uninstall=dpkg -r symas-openldap-silver.32

# Option 2 - Debian i386 Gold:
#platform=Debian-Gold-i386
#slapd.install=dpkg -i symas-openldap-gold.32_2.4.25-110507_i386.deb
#slapd.uninstall=dpkg -r symas-openldap-gold.32

# Option 3 - Redhat i386 Silver:
#platform=Redhat-Silver-i386
#slapd.install=rpm -Uvv symas-openldap-silver.i386-2.4.26.1.rpm
#slapd.uninstall=rpm -e symas-openldap-silver

#sudo.pw=# Option 4 - Redhat i386 Gold:
#platform=Redhat-Gold-i386
#slapd.install=rpm -Uvv symas-openldap-gold.i386-2.4.25.110424.rpm
#slapd.uninstall=rpm -e symas-openldap-gold

d. Run the install target:

From FORTRESS_HOME/openldap-fortress-core folder, enter the following command from a system prompt:

if Debian sudo:
>sudo $ANT_HOME/bin/ant init-slapd

if not sudo you must run as user that has priv to modify folders in /var and /opt folders:
>su
>$ANT_HOME/bin/ant init-slapd


###################################################################################
# SECTION 7. Instructions for using OpenLDAP server that was already installed.
###################################################################################

a. install OpenLDAP using your systems package management system.

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


###############################################################################
# SECTION 8. Instructions to run the Regression tests
###############################################################################

a. from the same shell prompt as 2a enter the following:

(if first time regression tests run:)

$ANT_HOME/bin/ant test-full-init

b. Or for subsequent runs:

$ANT_HOME/bin/ant test-full


###############################################################################
# SECTION 9. Learn how to use Fortress APIs using the Sample applications
###############################################################################

a. from the same shell prompt as 2a enter the following:

(if first time sample tests run)

$ANT_HOME/bin/ant test-samples-init

b. Or if subsequent runs:

$ANT_HOME/bin/ant test-samples

c. view and change the samples here:

[FORTRESS_HOME]/openldap-fortress-core/src/test/com/jts/fortress/samples

d. compile and re-run samples to test your changes using:

 $ANT_HOME/bin/ant test-samples

e. view the samples java doc here:

[FORTRESS_HOME]/openldap-fortress-core/dist/docs/samples/index.html

f. view the fortress-core SDK java doc here:

[FORTRESS_HOME]/openldap-fortress-core/dist/docs/api/index.html


###################################################################################
# SECTION 10. Instructions to run the simple command console
###################################################################################

a. from the same shell prompt as 2a enter the following:

$ANT_HOME/bin/ant console


###################################################################################
# SECTION 11. Instructions to encrypt LDAP passwords used in Fortress configuation files.
###################################################################################

If you need the passwords for LDAP service accounts to be encrypted before loading into Fortress properties files you can
use the 'encrypt' ant target.

a. From FORTRESS_BUILDER_HOME root folder, enter the following command from a system prompt:

>./build.sh encrypt -Dparam1=secret
encrypt:
     [echo] Encrypt a value
     [java] Encrypted value=wApnJUnuYZRBTF1zQNxX/Q==
BUILD SUCCESSFUL
Total time: 1 second

b. Copy the Encrypted value and paste it into the corresponding build.properties setting, e.g.:

# This OpenLDAP admin root pass is bound for fortress.properties and was encrypted using 'encrypt' target in build.xml:
cfg.root.pw=wApnJUnuYZRBTF1zQNxX/Q==
