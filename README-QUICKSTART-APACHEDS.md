
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

# Apache Fortress and ApacheDS QUICKSTART

-------------------------------------------------------------------------------
## Table of Contents

 * SECTION 1. Prerequisites
 * SECTION 2. ApacheDS Setup
 * SECTION 3. Apache Fortress Core Setup
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
## SECTION 2. ApacheDS Setup

1. Download and install Apache Directory Server

 a. For 32-bit (as sudo or root):

 ```
 wget http://www.trieuvan.com/apache//directory/apacheds/dist/2.0.0-M21/apacheds-2.0.0-M21-32bit.bin
 chmod a+x *.bin
 ./apacheds-2.0.0-M21-32bit.bin
 ```

 b. For 64-bit (as sudo or root):

 ```
 wget http://www.trieuvan.com/apache//directory/apacheds/dist/2.0.0-M21/apacheds-2.0.0-M21-64bit.bin
 chmod a+x *.bin
 ./apacheds-2.0.0-M21-64bit.bin
 ```

 *Accept the defaults.*

2. If you have trouble pulling you can get binaries here: [Apache Directory Downloads Page](http://directory.staging.apache.org/apacheds/downloads.html).

3. Edit ApacheDS conf file

 ```
 sudo vi /opt/apacheds-2.0.0-M21/conf/wrapper.conf
 ```

4. Add location of java:

 ```
 wrapper.java.command=/path/java/bin/java
 ```

 *Where 'path' equals fully qualified location of target machine's java.*

5. Save and exit.

6. After installation start the directory server process. From system command prompt (as sudo or root):

 ```
 /etc/init.d/apacheds-2.0.0-M21-default start
 ```
7. Check the status of server process. From system command prompt (as sudo or root):

 ```
 /etc/init.d/apacheds-2.0.0-M21-default status
 ApacheDS - default is running (70041).
 ```

___________________________________________________________________________________
## SECTION 3. Apache Fortress Core Setup

1. Download and prepare the package:

 ```
 wget http://www.apache.org/dist/directory/fortress/dist/1.0-RC42/fortress-core-1.0-RC42-source-release.zip
 unzip fortress-core-1.0-RC42-source-release.zip
 cd fortress-core-1.0-RC42
 cp build.properties.example build.properties
 ```

 *[build.properties.example](build.properties.example) is where the ApacheDS server defaults reside. This file, after being renamed to slapd.properties, will override values found in build.properties. Learn more about the configuration subsystem: [README-CONFIG](README-CONFIG.md)*


3. Prepare your terminal for execution of maven commands.

 ```
 export JAVA_HOME=[my-jdk]
 export M2_HOME=[my-mvn]
 ```

3. Build fortress core. This step will generate config artifacts using settings from build.properties.

 ```
 mvn install
 ```

4. Load the Apache Fortress schema into ApacheDS server.

 a. Load the [fortress schema](ldap/schema/apacheds-fortress.ldif) to ApacheDS instance using ldapmodify command.
 ```
 ldapmodify -h localhost -p 10389 -D uid=admin,ou=system -w secret -a -f FORTRESS_HOME/ldap/schema/apacheds-fortress.ldif
 ```

 b. Load the fortress schema contained in ldap/schema/apacheds-fortress.ldif to ApacheDS instance using Apache Directory Studio.
 *more instructions in Apache Fortress 10 Minute Guide under Apache Directory Studio and Core Setup*

5. More steps to follow in the [README](README.md) file:

 * SECTION 9.  Instructions to integration test.
 * SECTION 11. Instructions to run the Apache Fortress Command Line Interpreter (CLI).
 * SECTION 12. Instructions to run the Apache Fortress Command Console.
 * SECTION 13. Instructions to build and test the Apache Fortress samples.
 * SECTION 14. Instructions to performance test.

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

2. Download the fortress realm proxy jar into tomcat/lib folder:

  ```
  sudo wget http://repo.maven.apache.org/maven2/org/apache/directory/fortress/fortress-realm-proxy/1.0-RC42/fortress-realm-proxy-1.0-RC42.jar -P /usr/local/tomcat8/lib
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

1. Download and prepare the package:

 ```
 wget http://www.apache.org/dist/directory/fortress/dist/1.0-RC42/fortress-rest-1.0-RC42-source-release.zip
 unzip fortress-rest-1.0-RC42-source-release.zip
 cp ./fortress-core-1.0-RC42/config/fortress.properties ./fortress-rest-1.0-RC42/src/main/resources
 cd fortress-rest-1.0-RC42
 ```

2. Build, perform fortress rest test policy load and deploy to Tomcat:

 ```
 mvn clean install -Dload.file=./src/main/resources/FortressRestServerPolicy.xml tomcat:deploy
 ```

3. Redeploy (if need be):

 ```
 mvn tomcat:redeploy
 ```

4. Smoke test:

 ```
 mvn test -Dtest=EmTest
 ```

___________________________________________________________________________________
## SECTION 6. Apache Fortress Web Setup

During this section, you will be asked to setup Apache Fortress Web Application

1. Download and prepare the package:

 ```
 wget http://www.apache.org/dist/directory/fortress/dist/1.0-RC42/fortress-web-1.0-RC42-source-release.zip
 unzip fortress-web-1.0-RC42-source-release.zip
 cp ./fortress-core-1.0-RC42/config/fortress.properties ./fortress-web-1.0-RC42/src/main/resources
 cd fortress-web-1.0-RC42
 ```

2. Build, perform fortress web test policy load and deploy to Tomcat:

 ```
 mvn clean install -Dload.file=./src/main/resources/FortressWebDemoUsers.xml tomcat:deploy
 ```

3. Redeploy (if need be):

 ```
 mvn tomcat:redeploy
 ```

4. Open browser and test (creds: test/password):

 ```
 http://hostname:8080/fortress-web
 ```

5. Click on the links, to pull up various views on the data stored in the directory.

6. Run the selenium automated test:

 ```
 mvn test -Dtest=FortressWebSeleniumITCase
 ```

 *Requires Firefox on target machine.*

 ___________________________________________________________________________________
  #### END OF README-QUICKSTART-APACHEDS