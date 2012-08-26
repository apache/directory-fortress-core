--------------------------------------------------------------
JoshuaTree Fortress Builder Quickstart
created: November 12, 2011
last updated: August 25, 2012
--------------------------------------------------------------

These instructions are intended for new users who want to get the Fortress/OpenLDAP system up and running as fast as possible.  

For more detailed instructions on how to tailor this for User preferences, view the README.txt in the same package or
check out the documentation on joshuatreesoftware.us and iamfortress.org websites.
___________________________________________________________________________________
###################################################################################
# Prerequisites
###################################################################################
1. Internet access to retrieve source code from OpenLDAP GIT and
binary dependencies from online Maven repo.

NOTE: The Fortress build.xml may run without connection to Internet iff:
- The Fortress source modules have been downloaded
- The binary dependencies are already present in FORTRESS_HOME/lib folder
- Local mode has been enabled on target machine.  Local mode can be
enabled by adding this property to build.properties:
local.mode=true

2. Java SDK Version 6 or beyond installed to target environment
___________________________________________________________________________________
###################################################################################
# Guidelines & Tips for first-time users
###################################################################################

- In the document that follows, when you read  "[version]" or "[platform]" with correct Fortress edition.
  For example - if version is 1.0.0 and platform is 'Windows Silver i686',
  change fortressBuilder-[platform]-[version].jar to
  fortressBuilder-Debian-Silver-i386-1.0.0.zip
___________________________________________________________________________________
###################################################################################
# I. Instructions to extract and configure Fortress Builder Package to Target System
###################################################################################

a. Copy fortressBuilder-[platform]-[version].jar to hard drive on target server env.  

b. Extract the zip.  The location for archive can vary according to requirements.  The location
for package will be referred to as "FORTRESS_HOME" later in these instructions.

c. Enable permission for the binaries to execute.  From FORTRESS_HOME root folder, enter the following
command from a system prompt:
>chmod a+x -Rf *

d. if debian platform and using sudo, edit file named 'build.properties' and insert sudo password here:

sudo.pw=yoursudopwhere

note: If not using sudo it is important to leave the value empty.  Otherwise the installation will fail.
sudo.pw=

___________________________________________________________________________________
###################################################################################
# II. Instructions to run the Builder to Install OpenLDAP, configure and load with seed data
###################################################################################

Warning: This target is destructive as it reinstalls and reloads the OpenLDAP server program, configuration and data.  
Do not point this at OpenLDAP server that is in use.  It will disrupt user access and clear out all old entries including Users, passwords, policies, etc.

a. From FORTRESS_HOME root folder, edit the b.sh file and edit the following to point to your java and apache ant home:
export JAVA_HOME=/opt/jdk1.6.0_27
export ANT_HOME=/home/smckinn/JavaTools/apache-ant-1.8.2

b. From FORTRESS_HOME root folder, enter the following command from a system prompt:

if sudo:
sudo ./b.sh init-slapd

if not sudo you must run as user that has priv to modify folders in /var and /opt folders:
>su
>./b.sh init-slapd

If the above step completed successfully you now have OpenLDAP installed on your target server and configured to perform Fortress IAM functionality.  The following optional 
steps describe how you can test and use the admin console.

___________________________________________________________________________________
###################################################################################
# III. Instructions to test Fortress and Symas OpenLDAP (optional)
###################################################################################

a. From FORTRESS_HOME root folder, enter the following command from a system prompt:

>./b.sh test-full-init

 /home/smckinn/GIT/fortressDev/openldap-fortress-core/dist/fortressBuilder-Debian-Silver-i386-1.0.0-rc12.zip

b. verfify the tests ran with no failures.

c. If no errors you have successfully installed Symas OpenLDAP and enabled for use with Fortress.


note: if step II b was run as sudo user, you will need to run above as sudo as well, or, chown FORTRESS_HOME back to normal user.  Otherwise you will receive this error:

BUILD FAILED
/home/smckinn/tmp/fortress/50/fortressBuilder-Debian-Silver-i386-1.0.0-rc12/build.xml:264: Directory /home/smckinn/tmp/fortress/50/fortressBuilder-Debian-Silver-i386-1.0.0-rc12/build/test-classes creation was not successful for an unknown reason

___________________________________________________________________________________
###################################################################################
# IV. Instructions to run the Fortress Command Line
Interpreter (CLI) utility (optional)
###################################################################################

a. from the same shell prompt as 2a enter the following:

>./b.sh cli
b. follow instructions in the command line interpreter reference
manual in the javadoc located here:

FORTRESS_HOME/dist/docs/api/com/jts/fortress/cli/package-summary.html

note: if javadocs are not found, go to Section VI
___________________________________________________________________________________
###################################################################################
# V. Instructions to run Fortress Console (optional)
###################################################################################
For tasks like one-time setup of new users, password resets, searches
the Fortress Console application can be used.

a. From FORTRESS_HOME root folder, enter the following command
from a system prompt:

> ./b.sh console
b. Follow the screen prompts to Add, Update, Delete and Search
Fortress entities via a command line tool.
___________________________________________________________________________________
###################################################################################
# VI. Instructions to view Javadoc  (optional)
###################################################################################

a. run the javadoc target (this does not need to be run if you successfully ran init-slapd target above):

> ./b.sh javadoc

b. Open the following document using your preferred HTML Browser:

FORTRESS_HOME/dist/docs/api/com/jts/fortress//dist/docs/api/index.html