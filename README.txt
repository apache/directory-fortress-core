--------------------------------------------------------------
JoshuaTree Fortress README
created: November 5, 2011
--------------------------------------------------------------

Note this document is a work in progress.

###################################################################################
# O. Prerequisites
###################################################################################
1. Access to Internet to pull down source from openldap's GIT repo and dependencies from Maven.
2. GIT installed
2. Java SDK Version 6 or beyond
3. Apache Ant 1.8 or beyond

###################################################################################
# 1. Instructions to create a local GIT clone of openldap-fortress-core repo
###################################################################################

a. Open a shell prompt inside a new folder on your harddrive and enter the following command:

git clone ssh://git-master.openldap.org/~git/git/openldap-fortress-core.git

###################################################################################
# 2. Instructions to download IVY jar for dependency management
###################################################################################

Ivy is used to pull down the 10 or so other jar files that are fortress is dependent on.

a. Go to shell prompt in the root of the openldap-fortress-core local repository and enter the following commands:

export JAVA_HOME=/path to the root folder of your java SDK
export ANT_HOME=/path to the root folder of your Apache Ant installation
$ANT_HOME/bin/ant -buildfile getIvy.xml

###################################################################################
# 3. Instructions to compile and build the Fortress binaries and javadoc
###################################################################################

a. from the same shell prompt as 2a enter the following:

$ANT_HOME/bin/ant dist

###################################################################################
# 4. Instructions to install and setup OpenLDAP
###################################################################################

Download and run the Fortress Builder package for your target server env:

https://joshuatreesoftware.us/jtspages/download.php

###################################################################################
# 5. Instructions to run Fortress regression tests
###################################################################################

a. from the same shell prompt as 2a enter the following:

if first time regression tests run:

$ANT_HOME/bin/ant test-full-init

or if subsequent runs:

$ANT_HOME/bin/ant test-full


###################################################################################
# 6. Instructions to run Fortress samples
###################################################################################

a. from the same shell prompt as 2a enter the following:

if first time sample tests run:

$ANT_HOME/bin/ant test-samples-init

or if subsequent runs:

$ANT_HOME/bin/ant test-samples


###################################################################################
# 7. Instructions to run Fortress Console
###################################################################################

a. from the same shell prompt as 2a enter the following:

$ANT_HOME/bin/ant console

