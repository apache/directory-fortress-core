
   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE file
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this file
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this file except in compliance
   with the License.  You may obtain a copy of the License at

     https://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.

# OpenLDAP & Fortress QUICKSTART

 Apache Fortress 3.0.0 and OpenLDAP Quickstart System Architecture
 ![OpenLDAP & Fortress System Architecture](images/fortress-openldap-accel-system-arch.png "OpenLDAP & Fortress System Architecture")

-------------------------------------------------------------------------------
## Table of Contents

 * Document Overview
 * SECTION 1. Prerequisites
 * SECTION 2. Apache Fortress Core and OpenLDAP Setup
 * SECTION 3. Apache Fortress Core Integration Test
 * SECTION 4. Apache Tomcat Setup
 * SECTION 5. Apache Fortress Rest Setup
 * SECTION 6. Apache Fortress Web Setup

___________________________________________________________________________________
## Document Overview

 * This document contains instructions to install Apache Fortress 3.0.0 Core and related components.

-------------------------------------------------------------------------------
## SECTION 1. Prerequisites

Minimum software requirements:
 * RHEL or Debian Machine
 * Java SDK >= 11
 * Apache Maven >= 3
___________________________________________________________________________________
## SECTION 2. Apache Fortress Core and OpenLDAP Setup

1. Setup your Debian or Rehat Symas OpenLDAP 2.5 package repo: [Symas OpenLDAP 2.5](https://repo.symas.com/soldap2.5/)
   - Select your distro
   - Debian systems must install the gpg key
   - Follow steps 1 and 2 (only) to update your repo
   - Everything else (install, configure) is covered in the steps that follow
   
2. Get the Apache Fortress Core source package:

```bash
git clone https://gitbox.apache.org/repos/asf/directory-fortress-core.git
cd directory-fortress-core
```

3. Prepare the Apache Fortress package:

```bash
cp build.properties.example build.properties
cp slapd.properties.example slapd.properties
```

 *[slapd.properties.example](slapd.properties.example) contains the slapd default config.
 * Learn more about how the config works: [README-CONFIG](README-CONFIG.md).
 * Learn more about what properties there are: [README-PROPERTIES](README-PROPERTIES.md).

4. Edit the *slapd.properties* file:

```bash
vi slapd.properties
```

5. Choose which package to install Debian or Redhat:

```properties
#Debian:
slapd.install=apt install symas-openldap-clients symas-openldap-server -y
slapd.uninstall=apt remove symas-openldap-clients symas-openldap-server -y

# Or:

# Redhat:
slapd.install=yum install symas-openldap-servers symas-openldap-clients -y
slapd.uninstall=yum remove symas-openldap-servers symas-openldap-clients -y
```

6. (optional) Specify whether you want to communicate over SSL using LDAPS:

 a. Place .pem files for ca-certificate, server certificate and private key in folder named *certs* : [fortress-core-[VERSION]/src/test/resources/certs](./src/test/resources/certs)

  These will get copied to openldap ssl folder during init-slapd target.
  For example:
  - ca-cert.pem is the ca certificate file
  - server-cert.pem is the server certificate
  - server-key.pem is the server private key

 b. add or replace the following slapd.properties:

```properties
# These are needed for client SSL connections with LDAP Server:
enable.ldap.ssl=true
# The LDAP hostname must match the common name in the server certificate:
ldap.host=fortressdemo2.com
# 636 is default LDAPS on OpenLDAP:
ldap.port=636
enable.ldap.ssl.debug=true
# The trust store is found either on the application's classpath or filepath as specified by trust.store.onclasspath:
trust.store=mytruststore
trust.store.password=changeit
# Will pick up the truststore from the classpath if set to true  which is the default.  Otherwise, file must be specified a fully qualified filename:
trust.store.onclasspath=true

# These are needed for slapd startup SSL configuration:
ldap.uris=ldap://${ldap.host}:389 ldaps://${ldap.host}:${ldap.port}

# These are the 3 crypto artifacts copied earlier:
tls.ca.cert.file=ca-cert.pem
tls.cert.file=server-cert.pem
tls.key.file=server-key.pem
```

  more ldaps notes
  - whatever used for LDAP host name must match the common name element of the server's certificate
  - the truststore may be found on the classpath or as a fully qualified file name determined by trust.store.onclasspath.
  - The LDAP URIs are used by the server listener during startup.

7. Save and exit

8. Prepare your terminal for execution of maven commands.

```bash
#!/bin/sh
export M2_HOME=...
export JAVA_HOME=...
export PATH=$PATH:$M2_HOME/bin
```

9. Run the maven install:

```bash
mvn clean install
```

10. Install, configure and load the slapd server:

```bash
mvn test -Pinit-slapd
```

11. To start the slapd process:

```bash
mvn test -Pstart-slapd
```

12. To stop the slapd process:

```bash
mvn test -Pstop-slapd
```

 * must be run with either sudo or root privs

___________________________________________________________________________________
## SECTION 3. Apache Fortress Core Integration Test

1. From fortress core base folder, enter the following commands:

```bash
mvn install -Dload.file=./ldap/setup/refreshLDAPData.xml
```

 *These will build the Directory Information Tree (DIT), create the config and data policies needed for the integration test to follow.*

2. Next, enter the following command:

```bash
mvn -Dtest=FortressJUnitTest test
```

 *Tests the APIs against your LDAP server.*

3. Verify the tests worked:

```bash
Tests run: Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 196 sec - in org.apache.directory.fortress.core.impl.FortressJUnitTest
Results :
Tests run: Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] --- maven-antrun-plugin:1.8:run (default) @ fortress-core ---
[INFO] Executing tasks
fortress-load:
[INFO] Executed tasks
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 03:19 min
[INFO] Finished at: 2016-01-07T09:28:18-06:00
[INFO] Final Memory: 27M/532M
[INFO] ------------------------------------------------------------------------
```

4. Rerun the tests to verify teardown APIs work:

```bash
mvn -Dtest=FortressJUnitTest test
```

5. Verify that worked also:

```bash
Tests run: Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 207.081 sec - in org.apache.directory.fortress.core.impl.FortressJUnitTest
Results :
Tests run: Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] --- maven-antrun-plugin:1.8:run (default) @ fortress-core ---
[INFO] Executing tasks
fortress-load:
[INFO] Executed tasks
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 03:30 min
[INFO] Finished at: 2016-01-07T09:33:11-06:00
[INFO] Final Memory: 27M/531M
[INFO] ------------------------------------------------------------------------
```
 Notice more tests ran this time vs the first time, due to teardown.

 Test Notes:
  * If tests complete without errors Apache Fortress works with your OpenLDAP server.
  * These tests load thousands of objects into the target ldap server.
  * Warning messages are negative tests in action.

6. Optional sections in the [README](README.md) file:

 * SECTION 11. Instructions to run the Apache Fortress Command Line Interpreter (CLI).
 * SECTION 12. Instructions to run the Apache Fortress Command Console.
 * SECTION 13. Instructions to build and test the Apache Fortress samples.
 * SECTION 14. Instructions to performance test.

___________________________________________________________________________________
## SECTION 4. Apache Tomcat Setup

During this section, you will be asked to setup Apache Tomcat 10 and prepare for usage with Apache Fortress

1. Download and prepare the package:

```bash
# Requires >= Apache Tomcat 10
wget https://archive.apache.org/dist/tomcat/tomcat-10/v[version]/bin/apache-tomcat-[version].tar.gz
tar -xvf apache-tomcat-[version].tar.gz
mv apache-tomcat-[version] /usr/local/tomcat10
```
 *Change the tomcat version as neeeded.*
 *For BSD variants (i.e. Mac) append /* to the folder name above on mv command.*

2. Download the fortress realm proxy jar into tomcat/lib folder:

```bash
wget https://repo.maven.apache.org/maven2/org/apache/directory/fortress/fortress-realm-proxy/[version]/fortress-realm-proxy-[version].jar -P /usr/local/tomcat10/lib
```

3. Prepare tomcat fortress deployments (optional):

```bash
vi /usr/local/tomcat10/conf/tomcat-users.xml
```

4. Add tomcat user to deploy fortress (optional):

```xml
<role rolename="manager-script"/>
<role rolename="manager-gui"/>
<user username="tcmanager" password="m@nager123" roles="manager-script"/>
<user username="tcmanagergui" password="m@nager123" roles="manager-gui"/>
```

5. Save and exit tomcat-users.xml file

6. Configure Tomcat as a service (optional)

 a. Edit the config file:

```bash
vi /etc/init.d/tomcat
```

 b. Add the following:

```
#!/bin/bash
# description: Tomcat Start Stop Restart
# processname: tomcat
# chkconfig: 234 20 80
CATALINA_HOME=/usr/local/tomcat10
case $1 in
start)
sh $CATALINA_HOME/bin/startup.sh
;;
stop)
sh $CATALINA_HOME/bin/shutdown.sh
;;
restart)
sh $CATALINA_HOME/bin/shutdown.sh
sh $CATALINA_HOME/bin/startup.sh
;;
esac
exit 0
```

 c. Add the init script to startup for run level 2, 3 and 4:

```bash
cd /etc/init.d
chmod 755 tomcat
chkconfig --add tomcat
chkconfig --level 234 tomcat on
```

7. Start tomcat server:

 a. If running Tomcat as a service:

```bash
service tomcat start
```

 b. Else

```bash
/usr/local/tomcat10/bin/startup.sh
```

8.  Verify clean logs after startup:

```bash
tail -f -n10000 /usr/local/tomcat10/logs/catalina.out
```

9.  Verify setup by signing onto the Tomcat Manager app with credentials userId: tcmanagergui, password: m@nager123

```
http://hostname:8080/manager
```

___________________________________________________________________________________
## SECTION 5. Apache Fortress Rest Setup

During this section, you will be asked to setup Apache Fortress Rest Application

1. Download the package:

 a. from git:
```bash
git clone --branch [version] https://gitbox.apache.org/repos/asf/directory-fortress-enmasse.git
cd directory-fortress-enmasse
```

 b. or download package:
```bash
wget https://www.apache.org/dist/directory/fortress/dist/[version]/fortress-rest-[version]-source-release.zip
unzip fortress-rest-[version]-source-release.zip
cd fortress-rest-[version]
```

2. Prepare:

```bash
cp ../[FORTRESS-CORE-HOME]/config/fortress.properties src/main/resources
```

 *where FORTRESS-CORE-HOME is package location on your machine*

3. Build, perform fortress rest test policy load and deploy to Tomcat:

```bash
mvn clean install -Dload.file=./src/main/resources/FortressRestServerPolicy.xml tomcat:deploy
```

4. Redeploy (if need be):

```bash
mvn tomcat:redeploy
```

5. Smoke test:

```bash
mvn test -Dtest=EmTest
```

___________________________________________________________________________________
## SECTION 6. Apache Fortress Web Setup

During this section, you will be asked to setup Apache Fortress Web Application

1. Download the package:

 a. from git:
```bash
git clone --branch [version] https://gitbox.apache.org/repos/asf/directory-fortress-commander.git
cd directory-fortress-commander
 ```

 b. or download package:
```bash
wget https://www.apache.org/dist/directory/fortress/dist/[version]/fortress-web-[version]-source-release.zip
unzip fortress-web-[version]-source-release.zip
cd fortress-web-[version]
```

2. Prepare:

```bash
cp ../[FORTRESS-CORE-HOME]/config/fortress.properties src/main/resources
```

 *where FORTRESS-CORE-HOME is package location on your machine*

3. Build, perform fortress web test policy load and deploy to Tomcat:

```bash
mvn clean install -Dload.file=./src/main/resources/FortressWebDemoUsers.xml tomcat:deploy
```

4. Redeploy (if need be):

```bash
mvn tomcat:redeploy
```

5. Open browser and test (creds: test/password):

```
http://hostname:8080/fortress-web
```

6. Click on the links, to pull up various views on the data stored in the directory.

7. Run the Selenium Web driver integration tests with Firefox (default):

```bash
mvn test -Dtest=FortressWebSeleniumITCase
```

8. Run the tests using Chrome:

```bash
mvn test -Dtest=FortressWebSeleniumITCase -Dweb.driver=chrome
```

 Note: The Selenium tests require that:
 * Either Firefox or Chrome installed to target machine.
 * **FORTRESS_CORE_HOME**/*FortressJUnitTest* successfully run.  This will load some test data to grind on.
 * [FortressWebDemoUsers](./src/main/resources/FortressWebDemoUsers.xml) policy loaded into target LDAP server.

___________________________________________________________________________________
#### END OF README-QUICKSTART-SLAPD
