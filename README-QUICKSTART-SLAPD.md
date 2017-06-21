
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

# OpenLDAP & Fortress QUICKSTART

 Apache Fortress 2.0.0 and OpenLDAP Quickstart System Architecture
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

 * This document contains instructions to install Apache Fortress 2.0.0 Core, Web, Rest and OpenLDAP.

-------------------------------------------------------------------------------
## SECTION 1. Prerequisites

Minimum hardware requirements:
 * 2 Cores
 * 4GB RAM

Minimum software requirements:
 * Centos or Debian Machine
 * Java SDK 8
 * Apache Maven3++

 *Everything else covered in steps that follow.*
___________________________________________________________________________________
## SECTION 2. Apache Fortress Core and OpenLDAP Setup

1. Download the package:

 a. from git:
 ```
 git clone --branch 2.0.0 https://git-wip-us.apache.org/repos/asf/directory-fortress-core.git
 cd directory-fortress-core
 ```

 b. or download package:
 ```
 wget http://www.apache.org/dist/directory/fortress/dist/2.0.0/fortress-core-2.0.0-source-release.zip
 unzip fortress-core-2.0.0-source-release.zip
 cd fortress-core-2.0.0
 ```

2. Prepare the package:

 ```
 cp build.properties.example build.properties
 cp slapd.properties.example slapd.properties
 ```

 *[slapd.properties.example](slapd.properties.example) contains the slapd default config. Learn more about how the config works: [README-CONFIG](README-CONFIG.md)*

3. Download the latest OpenLDAP binaries for your platform:
 [Symas OpenLDAP Silver Edition](https://downloads.symas.com/products/symas-openldap-directory-silver-edition/)

4. Place either a centos or debian package under the folder named *ldap* : [fortress-core-[VERSION]/ldap](./ldap)

5. Edit the *slapd.properties* file:

 ```
 vi slapd.properties
 ```

6. Update the *slapd.properties* file *slapd.install* statement with a reference to the openldap file install downloaded earlier.

 a. For Debian installs:
  ```
  slapd.install=dpkg -i symas-openldap-silver.version.platform.deb
  ```

 b. For Centos:
  ```
  slapd.install=rpm -i symas-openldap-silver.version.platform.rpm
  ```

7. Specify whether you want to enable the slapo-rbac overlay:

 a. Yes, I want to enable slapo-rbac:
  ```
  rbac.accelerator=true
  ```

 *To use this option, symas-openldap version 2.4.43++ is required.*

 b. No, I don't want to enable slapo-rbac:
  ```
  rbac.accelerator=false
  ```

8. (optional) Specify whether you want to communicate over SSL using LDAPS:

 a. Place .pem files for ca-certificate, server certificate and private key in folder named *certs* : [fortress-core-[VERSION]/src/test/resources/certs](./src/test/resources/certs)

  These will get copied to openldap ssl folder during init-slapd target.
  For example:
  - ca-cert.pem is the ca certificate file
  - server-cert.pem is the server certificate
  - server-key.pem is the server private key

 b. add or replace the following slapd.properties:

  ```
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

  more notes
  - whatever used for LDAP host name must match the common name element of the server's certificate
  - the truststore may be found on the classpath or as a fully qualified file name determined by trust.store.onclasspath.
  - The LDAP URIs are used by the server listener during startup.

9. Save and exit

10. Prepare your terminal for execution of maven commands.

 ```
 #!/bin/sh
 export M2_HOME=...
 export JAVA_HOME=...
 export PATH=$PATH:$M2_HOME/bin
 ```

11. Run the maven install:

 ```
 mvn clean install
 ```

12. Install, configure and load the slapd server:

  ```
  mvn test -Pinit-slapd
  ```

13. To start the slapd process:

  ```
  mvn test -Pstart-slapd
  ```

14. To stop the slapd process:

  ```
  mvn test -Pstop-slapd
  ```

 * must be run with either sudo or root privs

___________________________________________________________________________________
## SECTION 3. Apache Fortress Core Integration Test

1. From fortress core base folder, enter the following commands:

 ```
 mvn install -Dload.file=./ldap/setup/refreshLDAPData.xml
 mvn install -Dload.file=./ldap/setup/DelegatedAdminManagerLoad.xml
 ```

 *These will build the Directory Information Tree (DIT), create the config and data policies needed for the integration test to follow.*

2. Next, enter the following command:

 ```
 mvn -Dtest=FortressJUnitTest test
 ```

 *Tests the APIs against your LDAP server.*

3. Verify the tests worked:

 ```
 Tests run: 126, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 196 sec - in org.apache.directory.fortress.core.impl.FortressJUnitTest

 Results :

 Tests run: 126, Failures: 0, Errors: 0, Skipped: 0

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

 ```
 mvn -Dtest=FortressJUnitTest test
 ```

5. Verify that worked also:

 ```
 Tests run: 158, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 207.081 sec - in org.apache.directory.fortress.core.impl.FortressJUnitTest

 Results :

 Tests run: 158, Failures: 0, Errors: 0, Skipped: 0

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
 Notice 141 tests ran this time vs 113 the first time.

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

During this section, you will be asked to setup Apache Tomcat 8 and prepare for usage with Apache Fortress

1. Download and prepare the package:

 ```
 wget http://archive.apache.org/dist/tomcat/tomcat-8/v8.0.30/bin/apache-tomcat-8.0.30.tar.gz
 tar -xvf apache-tomcat-8.0.30.tar.gz
 sudo mv apache-tomcat-8.0.30 /usr/local/tomcat8
 ```
 *Change the tomcat version as neeeded - v7 and beyond are ok.*

2. Download the fortress realm proxy jar into tomcat/lib folder:

  ```
  sudo wget http://repo.maven.apache.org/maven2/org/apache/directory/fortress/fortress-realm-proxy/2.0.0/fortress-realm-proxy-2.0.0.jar -P /usr/local/tomcat8/lib
  ```

3. Prepare tomcat fortress usage:

 ```
 sudo vi /usr/local/tomcat8/conf/tomcat-users.xml
 ```

4. Add tomcat user to deploy fortress:

 ```
 <role rolename="manager-script"/>
 <role rolename="manager-gui"/>
 <user username="tcmanager" password="m@nager123" roles="manager-script"/>
 <user username="tcmanagergui" password="m@nager123" roles="manager-gui"/>
 ```

5. Save and exit tomcat-users.xml file

6. Configure Tomcat as a service (optional)

 a. Edit the config file:

 ```
 vi /etc/init.d/tomcat
 ```

 b. Add the following:

 ```
 #!/bin/bash
 # description: Tomcat Start Stop Restart
 # processname: tomcat
 # chkconfig: 234 20 80
 CATALINA_HOME=/usr/local/tomcat8
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

 ```
 cd /etc/init.d
 chmod 755 tomcat
 chkconfig --add tomcat
 chkconfig --level 234 tomcat on
 ```

7. Start tomcat server:

 a. If running Tomcat as a service:

 ```
 service tomcat start
 ```

 b. Else

 ```
 sudo /usr/local/tomcat8/bin/startup.sh
 ```

8.  Verify clean logs after startup:

 ```
 tail -f -n10000 /usr/local/tomcat8/logs/catalina.out
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
 ```
 git clone --branch 2.0.0 https://git-wip-us.apache.org/repos/asf/directory-fortress-enmasse.git
 cd directory-fortress-enmasse
 ```

 b. or download package:
 ```
 wget http://www.apache.org/dist/directory/fortress/dist/2.0.0/fortress-rest-2.0.0-source-release.zip
 unzip fortress-rest-2.0.0-source-release.zip
 cd fortress-rest-2.0.0
 ```

2. Prepare:

 ```
 cp ../[FORTRESS-CORE-HOME]/config/fortress.properties src/main/resources
 ```

 *where FORTRESS-CORE-HOME is package location on your machine*

3. Build, perform fortress rest test policy load and deploy to Tomcat:

 ```
 mvn clean install -Dload.file=./src/main/resources/FortressRestServerPolicy.xml tomcat:deploy
 ```

4. Redeploy (if need be):

 ```
 mvn tomcat:redeploy
 ```

5. Smoke test:

 ```
 mvn test -Dtest=EmTest
 ```

___________________________________________________________________________________
## SECTION 6. Apache Fortress Web Setup

During this section, you will be asked to setup Apache Fortress Web Application

1. Download the package:

 a. from git:
 ```
 git clone --branch 2.0.0 https://git-wip-us.apache.org/repos/asf/directory-fortress-commander.git
 cd directory-fortress-commander
 ```

 b. or download package:
 ```
 wget http://www.apache.org/dist/directory/fortress/dist/2.0.0/fortress-web-2.0.0-source-release.zip
 unzip fortress-web-2.0.0-source-release.zip
 cd fortress-web-2.0.0
 ```

2. Prepare:

 ```
 cp ../[FORTRESS-CORE-HOME]/config/fortress.properties src/main/resources
 ```

 *where FORTRESS-CORE-HOME is package location on your machine*

3. Build, perform fortress web test policy load and deploy to Tomcat:

 ```
 mvn clean install -Dload.file=./src/main/resources/FortressWebDemoUsers.xml tomcat:deploy
 ```

4. Redeploy (if need be):

 ```
 mvn tomcat:redeploy
 ```

5. Open browser and test (creds: test/password):

 ```
 http://hostname:8080/fortress-web
 ```

6. Click on the links, to pull up various views on the data stored in the directory.

7. Run the Selenium Web driver integration tests with Firefox (default):

 ```
 mvn test -Dtest=FortressWebSeleniumITCase
 ```

8. Run the tests using Chrome:

 ```
 mvn test -Dtest=FortressWebSeleniumITCase -Dweb.driver=chrome
 ```

 Note: The Selenium tests require that:
 * Either Firefox or Chrome installed to target machine.
 * **FORTRESS_CORE_HOME**/*FortressJUnitTest* successfully run.  This will load some test data to grind on.
 * [FortressWebDemoUsers](./src/main/resources/FortressWebDemoUsers.xml) policy loaded into target LDAP server.

 ___________________________________________________________________________________
  #### END OF README-QUICKSTART-SLAPD