
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

# Apache Fortress and OpenLDAP QUICKSTART

 Apache Fortress Version 1.0-RC42 System Architecture Diagram
 ![OpenLDAP Accelerator System Architecture](images/fortress-openldap-accel-system-arch.png "OpenLDAP & Apache Fortress System Architecture")

-------------------------------------------------------------------------------
## Table of Contents

 * SECTION 1. Prerequisites
 * SECTION 2. Apache Fortress Core and OpenLDAP Setup
 * SECTION 3. Apache Fortress Realm Setup
 * SECTION 4. Apache Tomcat Setup
 * SECTION 5. Apache Fortress Rest Setup
 * SECTION 6. Apache Fortress Web Setup

-------------------------------------------------------------------------------
## SECTION 1. Prerequisites

Minimum hardware requirements:
 * 2 Cores
 * 4GB RAM

Minimum software requirements:
 * Java SDK 7++
 * git
 * Apache Ant 1.7++
 * Apache Maven3++

Everything else covered in steps that follow.  Tested on Debian & Centos systems.
___________________________________________________________________________________
## SECTION 2. Apache Fortress Core and OpenLDAP Setup

1. Download and prepare the package:

 ```
 git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-core.git
 cd directory-fortress-core
 cp build.properties.example build.properties
 cp slapd.properties.example slapd.properties
 ```

 *[slapd.properties.example](slapd.properties.example) is where the OpenLDAP server defaults reside. This file, after being renamed to slapd.properties, will override values found in build.properties. Learn more about the configuration subsystem: [README-CONFIG](README-CONFIG.md)*

2. Download Symas OpenLDAP Silver Binaries:
 https://symas.com/downloads/

3. Place it under this folder: [directory-fortress-core/ldap](./ldap)

4. Edit the slapd install properties file:

 ```
 vi slapd.properties
 ```

5. Add sudo pw to run install script (if running slapd with sudo per step 10):

 ```
 sudo.pw=mysudopassword
 ```

6. Update with slapd install for package downladed.

 a. For Debian installs:

  ```
  slapd.install=dpkg -i symas-openldap-silver.version.platform.deb
  ```

 b. For Redhat:

  ```
  slapd.install=rpm -i symas-openldap-silver.version.platform.rpm
  ```

7. Specify whether you want to enable slapo-rbac overlay:

 a. Yes, I want to enable slapo-rbac:

  ```
  rbac.accelerator=true
  ```

 *To use this option, symas-openldap version 2.4.43++ is required.*

 b. No, I don't want to enable slapo-rbac:

  ```
  rbac.accelerator=false
  ```

8. Save and exit

9. Prepare your terminal for execution of maven and ant commands.

 ```
 export JAVA_HOME=[my-jdk]
 export ANT_HOME=[my-ant]
 export M2_HOME=[my-mvn]
 export PATH=$PATH:$ANT_HOME/bin:$M2_HOME/bin
 ```

10. Run the maven install:

 ```
 mvn clean install
 ```

 *Use maven to build the software package*

11. Install, configure and load openldap with DIT and seed data:

 a. If sudo:

  ```
  sudo ant init-slapd
  ```

 b. No sudo, running as priv user:

  ```
  ant init-slapd
  ```

 *Use ant to install Symas OpenLDAP to target machine.*

12. More steps to follow in the [README](README.md) file:

 * SECTION 9.  Instructions to integration test.
 * SECTION 11. Instructions to run the Apache Fortress Command Line Interpreter (CLI).
 * SECTION 12. Instructions to run the Apache Fortress Command Console.
 * SECTION 13. Instructions to build and test the Apache Fortress samples.
 * SECTION 14. Instructions to performance test.

___________________________________________________________________________________
## SECTION 3. Apache Fortress Realm Setup

During this section, you will be asked to setup Apache Fortress Realm.

1. Download and prepare:

 ```
 git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-realm.git
 cd directory-fortress-realm
 ```

2. Build:

 ```
 mvn clean install
 ```

___________________________________________________________________________________
## SECTION 4. Apache Tomcat Setup

During this section, you will be asked to setup Apache Tomcat 8 and prepare for usage with Apache Fortress

1. Download and prepare the package:

 ```
 wget http://www.eu.apache.org/dist/tomcat/tomcat-8/v8.0.30/bin/apache-tomcat-8.0.30.tar.gz
 tar -xvf apache-tomcat-8.0.30.tar.gz
 sudo mv apache-tomcat-8.0.30 /usr/local/tomcat8
 ```
 *Change the tomcat version as neeeded - v7 and beyond are ok.*

2. Copy the fortress realm proxy jar into tomcat/lib folder:

 ```
 sudo cp ./directory-fortress-realm/proxy/target/fortress-realm-proxy-1.0-RC41-SNAPSHOT.jar /usr/local/tomcat8/lib
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

6. Start tomcat server:

 ```
 sudo /usr/local/tomcat8/bin/startup.sh
 ```

7.  Verify clean logs after startup:

 ```
 tail -f -n10000 /usr/local/tomcat8/logs/catalina.out
 ```

8.  Verify setup by signing onto the Tomcat Manager app with credentials userId: tcmanagergui, password: m@nager123

 ```
 http://hostname:8080/manager
 ```

___________________________________________________________________________________
## SECTION 5. Apache Fortress Rest Setup

During this section, you will be asked to setup Apache Fortress Rest Application

1. Download and prepare the package:

 ```
 git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-enmasse.git
 cp ./directory-fortress-core/config/fortress.properties ./directory-fortress-enmasse/src/main/resources
 cd directory-fortress-enmasse
 ```

2. Build:

 ```
 mvn clean install
 ```

3. Perform fortress rest test policy load:

 ```
 mvn install -Dload.file=./src/main/resources/FortressRestServerPolicy.xml
 ```

4. Deploy:

 ```
 mvn tomcat:deploy
 ```

5. Redeploy (if need be):

 ```
 mvn tomcat:redeploy
 ```

6. Smoke test:

 ```
 mvn test -Dtest=EmTest
 ```

___________________________________________________________________________________
## SECTION 6. Apache Fortress Web Setup

During this section, you will be asked to setup Apache Fortress Web Application

1. Download and prepare the package:

 ```
 git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-commander.git
 cp ./directory-fortress-core/config/fortress.properties ./directory-fortress-commander/src/main/resources
 cd directory-fortress-commander
 ```

2. Build:

 ```
 mvn clean install
 ```

3. Perform fortress web test policy load:

 ```
 mvn install -Dload.file=./src/main/resources/FortressWebDemoUsers.xml
 ```

4. Deploy:

 ```
 mvn tomcat:deploy
 ```

5. Redeploy (if need be):

 ```
 mvn tomcat:redeploy
 ```

6. Open browser and test (creds: test/password):

 ```
 http://hostname:8080/fortress-web
 ```

7. Click on the links, to pull up various views on the data stored in the directory.

8. Run the selenium automated test:

 ```
 mvn test -Dtest=FortressWebSeleniumITCase
 ```

 *Requires Firefox on target machine.*

 ___________________________________________________________________________________
  #### END OF README-QUICKSTART-SLAPD