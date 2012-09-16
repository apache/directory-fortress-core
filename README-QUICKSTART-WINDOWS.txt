--------------------------------------------------------------
JoshuaTree Fortress Builder Quickstart README for Windows
created: August 25, 2012
--------------------------------------------------------------

These instructions are intended for new users who want to quickly learn how to use Fortress and OpenLDAP IAM software.
Follow these steps carefully, and OpenLDAP will be installed, configured, loaded, and ready to use by Section IV.

For more detailed instructions on how to tailor this for User
preferences, view the README.txt in the same package or
check out the documentation on joshuatreesoftware.us and
iamfortress.org websites.

___________________________________________________________________________________
###################################################################################
# Prerequisites
###################################################################################
1. Internet access to retrieve binary dependencies from online Maven repo.

NOTE: The Fortress build.xml may run without connection to Internet iff:
- The binary dependencies are already present in FORTRESS_HOME/lib folder
- Local mode has been enabled on target machine.  Local mode can be
enabled by adding this property to build.properties:
local.mode=true

2. Java SDK Version 6 or beyond installed to target environment
___________________________________________________________________________________
###################################################################################
# Guidelines & Tips for first-time users
###################################################################################
- In the document that follows, when you read  "[version]" or "[platform]" substitute with current package info.
  For example - if version is 1.0.0 and platform is 'Windows Silver i686',
  change fortressBuilder-[platform]-[version].jar to
  fortressBuilder-Windows-Silver-i686-1.0.0.zip

- Does your machine OS end with an 'X'?  Go to README-QUICKSTART.txt

- The source code for this project is located in FORTRESS_HOME/src folder.
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
# II. Instructions to run the Fortress Ant Build
###################################################################################

The following command will pull down binary dependencies from Maven, compile the source, create the target specific configuration and load scripts and build the javadoc.

a. Edit FORTRESS_HOME\build.properties file

b. Set locations as below, save and exit:

slapd.exe.drive=C         <-- this is drive that you installed Symas OpenLDAP to (i.e. 'C')
slapd.exe.dir=Fortress    <-- this is folder that you installed Symas OpenLDAP to (i.e. 'Fortress')
fortress.home.drive=D     <-- this is the drive that you extracted FORTRESS_HOME to (i.e. 'D')

c. From FORTRESS_HOME root folder, edit the b.bat script to point to java home directory:

set JAVA_HOME=\Progra~1\Java\jdk1.7.0

Note: The b.bat batch file referred to here uses Ant package that is local to Fortress quickstart package.

d. Run the distribution target.

> b dist

e. Verify it ran correctly according to Ant.

> BUILD SUCCESSFUL

You may now view the project binaries and documentation located under FORTRESS_HOME/dist.

___________________________________________________________________________________
###################################################################################
# III. Instructions to install OpenLDAP
###################################################################################

a. Navigate to FORTRESS_HOME\openldap

b. Start the install by double-clicking on name from windows explorer, etc...

c. Accept the Symas agreement

d. Set the Destination Folder: i.e. C:\Fortress

Note: if you change the destination folder to other than what is listed above
let fortress know by setting in build.properties file located in FORTRESS_HOME:
slapd.exe.drive=C
slapd.exe.dir=Fortress

e. Click Finish

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
# IV. Instructions to run the Builder to configure OpenLDAP and load
with seed data
###################################################################################

a. From FORTRESS_HOME folder, enter the following command
from a system prompt:

> b init-slapd

b. Verify it ran correctly according to Ant.

> BUILD SUCCESSFUL


Note 1: After this step completes, OpenLDAP will be installed, configured and loaded with fortress bootstrap config.  This
step also runs provisioning scripts which may be tailored according to requirements.  Check out the xml load scripts
in FORTRESS_HOME/ldap/setup folder.

Note 2: Point your LDAP browser to the installed directory.
The config switches you'll need to browse can be found in the 'slapd.conf' file this step generates:

    To view data stored in default database:
        suffix		"dc=jts,dc=com"
        rootdn      "cn=Manager,dc=jts,dc=com"
        rootpw      "secret"    <--- This clear text value will be encrypted before stored in slapd.conf

    To view data stored in audit log database:
        suffix		"cn=log"
        rootdn      "cn=Manager,cn=log"
        rootpw      "secret"    <--- This clear text value will be encrypted before stored in slapd.conf

___________________________________________________________________________________
###################################################################################
# V. Instructions to regression test Fortress and Symas OpenLDAP on target machine
###################################################################################

a. From FORTRESS_HOME root folder, enter the following command
from a system prompt:

> b test-full-init

b. Verify the tests ran with no failures.

> BUILD SUCCESSFUL

c. To re-run these tests:
> b test-full

Note 1: WARNING messages are good as these are negative tests in action

Note 2: If you made it to this point with no ERRORS this means Fortress and OpenLDAP are good to go on your machine.

Note 3: This section prescribes that you run the Fortress regression test targets.  They load tens of thousands of test records into your directory.
The 'init-slapd' target may be re-run after the 'test-full-init' and 'test-full' targets have completed.  This will delete the test data from the directory
and restart directory with clean base load.
___________________________________________________________________________________
###################################################################################
# VI. Instructions to run the Fortress Command Line
Interpreter (CLI) utility (optional)
###################################################################################

This command line tool provides an interactive session with the user based on a simple command line syntax.

a. To start the CLI, enter:

> b cli

Which will bring up the command interpreter:

[java] 2012-08-26 19:05:43,904 (INFO ) CLI function groups include admin, review, system, dadmin
     [java] 2012-08-26 19:05:43,904 (INFO ) Enter one from above or 'q' to quit

b. enter the command.  This example will return all users with userId that begins with 'demo':

review fuser -u demo

c. will return a listing:

     [java] 2012-08-26 19:07:13,706 (INFO ) arg:review
     [java] 2012-08-26 19:07:13,706 (INFO ) arg:fuser
     [java] 2012-08-26 19:07:13,707 (INFO ) arg:-u
     [java] 2012-08-26 19:07:13,707 (INFO ) arg:d
     [java] 2012-08-26 19:07:13,717 (INFO ) fuser
     [java] ConnectionPool (Sun Aug 26 19:07:13 CDT 2012) : adding a connection to pool...
     [java] 2012-08-26 19:07:13,757 (INFO ) U   CTR  [0]
     [java] 2012-08-26 19:07:13,757 (INFO ) U   UID  [demoUser1]
     [java] 2012-08-26 19:07:13,757 (INFO ) U   IID  [d3564f03-19eb-4832-bd6e-01654455a61c]
     [java] 2012-08-26 19:07:13,757 (INFO ) U   CN   [JoeUser1]
     [java] 2012-08-26 19:07:13,757 (INFO ) U   DESC [Demo Test User 1]
     [java] 2012-08-26 19:07:13,758 (INFO ) U   OU   [demousrs1]
     [java] 2012-08-26 19:07:13,758 (INFO ) U   SN   [User1]
     ...

d. To learn more about the CLI, follow instructions in the command line interpreter reference
manual in the javadoc located here:

- FORTRESS_HOME/dist/docs/api/com/jts/fortress/cli/package-summary.html

Note: if javadocs are not found, go to Section VIII
___________________________________________________________________________________
###################################################################################
# VII. Instructions to run Fortress Console (optional)
###################################################################################
For tasks like one-time setup of new users, password resets, searches
the Fortress Console application can be used.

a. From FORTRESS_HOME root folder, enter the following command
from a system prompt:

> b console

b. Follow the screen prompts to perform regular RBAC, administrative ARBAC, password Policies and audit searches
     [java] CHOOSE FUNCTION
     [java] 1. ADMIN MANAGER FUNCTIONS
     [java] 2. REVIEW MANAGER FUNCTIONS
     [java] 3. ACCESS MANAGER FUNCTIONS
     [java] 4. DELEGATED ADMIN MANAGER FUNCTIONS
     [java] 5. DELEGATED REVIEW MANAGER FUNCTIONS
     [java] 6. DELEGATED ACCESS MANAGER FUNCTIONS
     [java] 7. PASSWORD POLICY MANAGER FUNCTIONS
     [java] 8. AUDIT MANAGER FUNCTIONS
     [java] 9. CONFIG MANAGER FUNCTIONS
     [java] A. ENCRYPTION MANAGER FUNCTIONS
___________________________________________________________________________________
###################################################################################
# VIII. Instructions to generate and view Javadoc  (optional)
###################################################################################

a. run the javadoc target (this does not need to be run if you successfully ran init-slapd target above):

> b javadoc

b. Open the documents using your preferred HTML Browser:

c. The javadoc provides coverage of the Fortress APIs and also provides explanations on how RBAC works.

d. Good places to start learning about Fortress:

l1 - FORTRESS_HOME/dist/docs/api/com/jts/fortress//dist/docs/api/com/jts/fortress/package-summary.html
l2 - FORTRESS_HOME/dist/docs/api/com/jts/fortress//dist/docs/api/com/jts/fortress/rbac/package-summary.html
l3 - FORTRESS_HOME/dist/docs/api/com/jts/fortress//dist/docs/api/com/jts/fortress/AdminMgr.html
l4 - FORTRESS_HOME/dist/docs/api/com/jts/fortress//dist/docs/api/com/jts/fortress/AuditMgr.html
l5 - FORTRESS_HOME/dist/docs/api/com/jts/fortress//dist/docs/api/com/jts/fortress/cli/package-summary.html
l6 - FORTRESS_HOME/dist/docs/api/com/jts/fortress//dist/docs/api/com/jts/fortress/ant/FortressAntTask.html
___________________________________________________________________________________
###################################################################################
# IX. Instructions to Uninstall OpenLDAP from target machine (optional)
###################################################################################

a. navigate to location you installed to, i.e. step II. d

\Fortress\uninstall-symas-openldap-silver-win.exe

b. click 'yes' to uninstall

c. check both server & client boxes, and click 'Next'

d. click on 'Uninstall'

e. close the dialog

f. you may now start over with OpenLDAP install, Section III.

___________________________________________________________________________________
###################################################################################
# X. More Utilities
###################################################################################

Other execution targets you may find useful:

a. 'test-samples' - contains easy to follow examples of how the Fortress API's work
check out the following javadoc for more info on the samples package:
- FORTRESS_HOME/dist/docs/api/com/jts/fortress//dist/docs/samples/index.html

b. 'admin' - provides an XML-centric way to provision RBAC data policies and user accounts.
check out the following javadoc for more info on this utility:
- FORTRESS_HOME/dist/docs/api/com/jts/fortress//dist/docs/api/com/jts/fortress/ant/FortressAntTask.html

c. 'encrypt' - interface to jacypt encryption utility

d. 'start-slapd' - Starts OpenLDAP on target machine.

e. 'stop-slapd' - Stops OpenLDAP on target machine.

Note: for slapd commands sudo or elevated privileges may be required.

f. many more:

 bdb-delete                   delete BDB DB
 builder                      Create binary distribution
 clean                        removes generated files
 clean-cache                  --> clean the ivy cache
 cli                          start Fortress Command Line Interpreter
 compile                      compiles source files
 console                      start Fortress Console app
 dist                         Create source and binary distribution
 encrypt                      Encrypts a text value
 init-all-config              This task maps environment specific params in build.properties to the target scripts and config files..
 init-fortress-config-local   This task maps environment specific params in build.properties to the target scripts and config files..
 init-fortress-config-remote  This task maps environment specific params in build.properties to the target scripts and config files..
 init-openldap-config         This task maps environment specific params in build.properties to the target scripts and config files..
 init-slapd                   This task is destructive and must be run as elevated priv's for teardown/creation of slapd files and folders.
 init-slapd-win-script        This task creates a startup file for slapd on windows
 javadoc                      generates javadocs
 javadoc-samples              generates samples javadocs
 load-slapd                   This task is destructive and must be run as elevated priv's for teardown/creation of slapd files and folders.
 resolve                      --> retreive dependencies with ivy
 test-full                    run (junit) full regression tests
 test-full-init               run (junit) regression tests without teardown
 test-samples                 runs (junit) sample unit tests
 test-samples-init            runs (junit) sample unit tests without teardown
 use-slapd                    This task is destructive and must be run as elevated priv's for teardown/creation of slapd files and folders.