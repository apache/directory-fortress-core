--------------------------------------------------------------
JoshuaTree Fortress README
created: November 5, 2011
--------------------------------------------------------------
This document contains instructions to download, compile, test and run Fortress in your development environment.  It is intended for developers but also contains instructions (hints)
to obtain and install OpenLDAP but is not document's main intent.
--------------------------------------------------------------

*** Note this is a work in progress. ***

###################################################################################
# O. Software Usage Prerequisites
###################################################################################
1. Internet access to fetch Fortress source code from OpenLDAP GIT and binary dependencies from Maven.
2. GIT installed to target environment.
2. Java SDK Version 6 or beyond installed to target environment
3. Apache Ant 1.8 or beyond installed to target environment
4. OpenLDAP server installed configured for Fortress on your network (more in step 4)

In addition to the above, software bootstrapping requires the use of Apache Ivy but that component will be setup on step #2 automatically with the getIvy Ant script.

###################################################################################
# 1. Instructions to create a local GIT clone of openldap-fortress-core repo
###################################################################################

a. Open a terminal session inside an empty folder on your target system and enter the following command:

git clone ssh://git-master.openldap.org/~git/git/openldap-fortress-core.git

Assuming you have network access after the above command has run all of the fortress source code will be downloaded from GIT and installed under the selected folder.

###################################################################################
# 2. Instructions to download IVY jar for dependency management
###################################################################################

Ivy is used to pull down the 10 or so other jar files that are fortress is dependent on.

a. Go to shell prompt in the root of the openldap-fortress-core local repository and enter the following commands:

export JAVA_HOME=/path to the root folder of your java SDK
export ANT_HOME=/path to the root folder of your Apache Ant installation
$ANT_HOME/bin/ant -buildfile getIvy.xml

After the above command is run, a single Apache Ivy jar file will be downloaded into the specified ANT_HOME/lib folder.  Ivy is used the first time you compile fortress
(step 3 below) to automatically fetch all of fortress' binary dependencies from maven repo website on Internet.

###################################################################################
# 3. Instructions to compile and build the Fortress binaries and javadoc
###################################################################################

a. from the same shell prompt as 2a enter the following:

$ANT_HOME/bin/ant dist

After the above step runs, all of the fortress source modules are compiled and the classes are loaded into jars.  Java documentation will also be generated.
These artifacts will be created under openldap-fortress-core/dist folder.

###################################################################################
# 4. Instructions to install OpenLDAP
###################################################################################

You have 3 options:

a. Go to https://joshuatreesoftware.us/jtspages/download.php, register and pull down the Fortress Builder package.
Follow these instructions:
https://joshuatreesoftware.us/jtspages/install.html

b. Go to http://www.symas.com/index.php/downloads/, register, pull down and install Silver or Gold Symas OpenLDAP releases of binaries for your target platform.

c. Get, install and OpenLDAP per your favored means.

###################################################################################
# 5. Instructions to configure Fortress and OpenLDAP on target system
###################################################################################

Note: the 'init-config' ant target on this project will substitute parameters found in 'build.properties' into their proper location.

a. Edit the FORTRESS_BUILDER_HOME build.properties file located in $FORTRESS_BUILDER_HOME root folder.

b. Set the LDAP Host and port properties.  Either a valid host name or IP address can be used.
host=myhostname (host or ip)
port=389

c. Set the suffix name and domain component.  For example suffix.name=jts + suffix.dc=com will = 'dc=jts,dc=com'.
suffix.name=jts
suffix.dc=com

d. Set the administrative LDAP connection pool parameters:

# Set the encryption key value used for encryption/decryption of admin passwords stored in Fortress property files used during client access LDAP server.
crypto.prop=abcd12345

OR leave the value blank if passwords are entered in the clear into property files:
crypto.prop=

Note: This value must be the same as used when password was encrypted (See Section VII)

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

j. (optional) if using sudo, set the sudo.pw that has read/write access to the file system where directory will be installed.
Edit the FORTRESS_BUILDER_HOME platform.properties file located in $FORTRESS_BUILDER_HOME root folder.
Change the sudo password to that of your target system:
sudo.pw=mysudopw

note: If not using sudo it is important to leave the value empty.  Otherwise the installation will fail.
sudo.pw=

###################################################################################
# 6. Instructions to Install OpenLDAP, configure and load with seed data
###################################################################################
note: if you downloaded and successfully ran the Fortress Builder installation wizard (step 4a above) you can skip this step.

a. Now install Symas OpenLDAP

Warning: This target is destructive as it reinstalls and reloads the OpenLDAP server program, configuration and data.
Do not point this at OpenLDAP server that is in use.  It will disrupt user access and clear out all old entries including Users, passwords, policies, etc.

From openldap-fortress-core root folder, enter the following command from a system prompt:

if sudo:
sudo $ANT_HOME/bin/ant init-slapd

if not sudo you must run as user that has priv to modify folders in /var and /opt folders:
>su
>$ANT_HOME/bin/ant init-slapd


###################################################################################
# 7. Instructions to run the Regression tests
###################################################################################

a. from the same shell prompt as 2a enter the following:

(if first time regression tests run:)

$ANT_HOME/bin/ant test-full-init

b. Or for subsequent runs:

$ANT_HOME/bin/ant test-full


###################################################################################
# 8. Instructions to run the Sample applications
###################################################################################

a. from the same shell prompt as 2a enter the following:

(if first time sample tests run)

$ANT_HOME/bin/ant test-samples-init

b. Or if subsequent runs:

$ANT_HOME/bin/ant test-samples

###################################################################################
# 9. Instructions to run the Console
###################################################################################

a. from the same shell prompt as 2a enter the following:

$ANT_HOME/bin/ant console