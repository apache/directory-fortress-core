--------------------------------------------------------------
JoshuaTree Fortress Builder Quickstart for Windows
created: August 25, 2012
--------------------------------------------------------------

These instructions are intended for new users who want to get the
Fortress/OpenLDAP system up and running on Windows machine.

For more detailed instructions on how to tailor this for User
preferences, view the README.txt in the same package or
check out the documentation on joshuatreesoftware.us and
iamfortress.org websites.
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
  fortressBuilder-Windows-Silver-i686-1.0.0.zip
___________________________________________________________________________________
###################################################################################
# I. Instructions to extract and configure Fortress Builder Package to
Target System
###################################################################################

a. Copy fortressBuilder-[platform]-[version].zip to hard drive on
target server env.

b. Extract the zip.  The location you copy this archive can vary according to
your requirements.  The location
for package will be referred to as "FORTRESS_HOME" later in
these instructions.

___________________________________________________________________________________
###################################################################################
# II. Instructions to install OpenLDAP
###################################################################################

a. navigate to FORTRESS_BUILDER_HOME\openldap

b. start the install by double-clicking on name from windows explorer, etc...

c. accept the Symas agreement

d. Set the Destination Folder: i.e. C:\Fortress

Note: if you change the destination folder to other than what is listed above
let fortress know by setting in build.properties file located in FORTRESS_HOME:
slapd.exe.drive=C
slapd.exe.dir=Fortress

e. click Finish

f. Verify installation completed successfully.

Note 1: depending on security settings of your windows machine, you may
receive a pop-up with 'Windows Firewall' stating
that access has been blocked.  This is normal as you now have OpenLDAP
process running and it wants to access ports
over the network.

Note 2: In order to run the tests you must allow access on private networks.

Note 3: You only need to allow access over a public network if
applications outside your firewall need access to the ldap server.

___________________________________________________________________________________
###################################################################################
# III. Instructions to run the Builder to configure OpenLDAP and load
with seed data
###################################################################################

a. Edit FORTRESS_HOME\build.properties file

b. Set locations as below, save and exit:

slapd.exe.drive=C         <-- this is drive that you installed Symas OpenLDAP to (i.e. 'C')
slapd.exe.dir=Fortress    <-- this is folder that you installed Symas OpenLDAP to (i.e. 'Fortress')
fortress.home.drive=C     <-- this is the drive that you extracted this package (FORTRESS BUILDER) to (i.e. 'C')

c. From FORTRESS_HOME folder, enter the following command
from a system prompt:

> b init-slapd

If the above step completed successfully you now have OpenLDAP installed on your target server and configured to perform Fortress IAM
functionality.  The following optional steps describe how you can run tests and perform admin tasks using fortress utilities.

___________________________________________________________________________________
###################################################################################
# IV. Instructions to test Fortress and Symas OpenLDAP (optional)
###################################################################################

a. From FORTRESS_HOME root folder, enter the following command
from a system prompt:

> ./b.bat test-full-init
b. verfify the tests ran with no failures.

c. If no errors you have successfully installed Symas OpenLDAP and
enabled for use with Fortress.

___________________________________________________________________________________
###################################################################################
# V. Instructions to run the Fortress Command Line
Interpreter (CLI) utility (optional)
###################################################################################

a. from the same shell prompt as 2a enter the following:

> ./b.bat cli
b. follow instructions in the command line interpreter reference
manual in the javadoc located here:

FORTRESS_HOME/dist/docs/api/com/jts/fortress/cli/package-summary.html

___________________________________________________________________________________
###################################################################################
# VI. Instructions to run Fortress Console (optional)
###################################################################################
For tasks like one-time setup of new users, password resets, searches
the Fortress Console application can be used.

a. From FORTRESS_HOME root folder, enter the following command
from a system prompt:

> ./b.bat console
b. Follow the screen prompts to Add, Update, Delete and Search
Fortress entities via a command line tool.

___________________________________________________________________________________
###################################################################################
# VII. Instructions to view Javadoc  (optional)
###################################################################################

a. run the javadoc target (this does not need to be run if you successfully ran init-slapd target above):

> ./b.bat javadoc

b. Open the following document using your preferred HTML Browser:

FORTRESS_HOME/dist/docs/api/com/jts/fortress//dist/docs/api/index.html
___________________________________________________________________________________
###################################################################################
# VIII. Instructions to Uninstall OpenLDAP from target machine (optional)
###################################################################################

a. navigate to location you installed to, i.e. step II. d

\Fortress\uninstall-symas-openldap-silver-win.exe

b. click 'yes' to uninstall

c. check both server & client boxes, and click 'Next'

d. click on 'Uninstall'

e. close the dialog

f. you may now start over with OpenLDAP install, Section II.