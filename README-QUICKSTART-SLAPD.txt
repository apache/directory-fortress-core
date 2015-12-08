#
#   Licensed to the Apache Software Foundation (ASF) under one
#   or more contributor license agreements.  See the NOTICE file
#   distributed with this work for additional information
#   regarding copyright ownership.  The ASF licenses this file
#   to you under the Apache License, Version 2.0 (the
#   "License"); you may not use this file except in compliance
#   with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing,
#   software distributed under the License is distributed on an
#   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#   KIND, either express or implied.  See the License for the
#   specific language governing permissions and limitations
#   under the License.
#
___________________________________________________________________________________
###################################################################################
# README for Apache Fortress and OpenLDAP QUICKSTART
# Version 1.0-RC41
###################################################################################

Covers these sections

SECTION I   - Machine Prereqs
SECTION II  - Apache Fortress Core and OpenLDAP Quickstart
SECTION III - Apache Fortress Realm Setup
SECTION IV  - Apache Tomcat Setup Fortress Usage
SECTION V   - Apache Fortress Rest Setup
SECTION VI  - Apache Fortress Web Setup

Sections III and beyond are not required for OpenLDAP Accelerator usage

___________________________________________________________________________________
###################################################################################
SECTION I - Machine Prereqs
###################################################################################

- 2 Cores, 4GB RAM
- Java 7++
- git, ant, maven3++
- Symas OpenLDAP Silver v.2.4.43++
  + slapo rbac, access log , pwpolicy overlays
- slapd.properties file tailored for slapd slapo-rbac overlay enablement. This file will be used during section 3 to seed openldap with the correct configuration.
- Apache Fortress v1.0-RC41++
  + Core, Realm*, Rest* and Web*
- Tomcat* v7++

*optional

___________________________________________________________________________________
###################################################################################
SECTION II - Apache Fortress Core and OpenLDAP Quickstart
###################################################################################

During this section, you will be asked to install the prerequisite infrastructure to run OpenLDAP accelerator.  Included is the ldap server used by the apache fortress system.

1. download and prepare the package:
git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-core.git
cd directory-fortress-core
cp build.properties.example build.properties
cp slapd.properties.example slapd.properties

2. edit the install script:
vi build.properties

3. add sudo priv to script:
sudo.pw=mysudopassword

4. Install openldap or specify Symas OpenLDAP Silver installation package file name and location.

For Debian installs:
slapd.install=dpkg -i symas-openldap-silver.version.platform.ext

For Redhat:
slapd.install=rpm -i symas-openldap-silver.x86_64-2.4.43-20151205.rpm

5. save and exit

6. run the maven install:
mvn clean install

7. install, configure and load openldap with DIT and seed data:
sudo ./b.sh init-slapd

8. run the fortress core regression tests:
mvn test -Dtest=FortressJUnitTest

9. run the openldap accelerator regression tests:
mvn test -Dtest=AccelMgrImplTest

10. all tests should run without errors.

11. run the console:
mvn install -Pconsole

___________________________________________________________________________________
###################################################################################
SECTION III - Apache Fortress Realm Setup
###################################################################################

During this section, you will be asked to setup Apache Fortress Realm.

1. download and prepare:
git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-realm.git
cd directory-fortress-realm

2. build:
mvn clean install

___________________________________________________________________________________
###################################################################################
SECTION IV - Apache Tomcat Setup Fortress Usage
###################################################################################

During this section, you will be asked to setup Apache Tomcat 8 and prepare for usage with Apache Fortress

1. download and prepare the package:
wget http://apache.mirrorcatalogs.com/tomcat/tomcat-8/v8.0.29/bin/apache-tomcat-8.0.29.tar.gz
tar -xvf apache-tomcat-8.0.29.tar.gz
sudo mv apache-tomcat-8.0.29 /usr/local/tomcat8

2. copy the fortress realm proxy jar into tomcat/lib folder:
sudo cp /home/student/fortress/directory-fortress-realm/proxy/target/fortress-realm-proxy-1.0-RC41-SNAPSHOT.jar /usr/local/tomcat8/lib

3. prepare tomcat fortress usage:
sudo vi /usr/local/tomcat8/conf/tomcat-users.xml

4. add tomcat user to deploy fortress:
<role rolename="manager-script"/>
<role rolename="manager-gui"/>
<user username="tcmanager" password="m@nager123" roles="manager-script"/>
<user username="tcmanagergui" password="m@nager123" roles="manager-gui"/>

5. save and exit tomcat-users.xml file

6. start tomcat server:
sudo /usr/local/tomcat8/bin/startup.sh

7.  Verify clean logs after startup:
tail -f -n10000 /usr/local/tomcat8/logs/catalina.out

8.  Verify setup by signing onto the Tomcat Manager app with credentials userId: tcmanagergui, password: m@nager123
http://hostname:8080/manager

___________________________________________________________________________________
###################################################################################
SECTION V - Apache Fortress Rest Setup
###################################################################################

During this section, you will be asked to setup Apache Fortress Rest Application

1. download and prepare the package:
git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-enmasse.git
cp /home/student/fortress/fortressBuilder-Debian-Silver-x86-64-1.0-RC41/config/fortress.properties directory-fortress-enmasse/src/main/resources
cd directory-fortress-enmasse

2. build:
mvn clean install

3. perform fortress rest test policy load:
mvn install -Dload.file=./src/main/resources/FortressRestServerPolicy.xml

4. deploy:
mvn tomcat:deploy

5. redeploy (if need be):
mvn tomcat:redeploy

6. smoke test:
mvn test -Dtest=EmTest

___________________________________________________________________________________
###################################################################################
SECTION VI - Apache Fortress Web Setup
###################################################################################

During this section, you will be asked to setup Apache Fortress Web Application

1. download and prepare the package:
git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-commander.git
cp /home/student/fortress/fortressBuilder-Debian-Silver-x86-64-1.0-RC41/config/fortress.properties directory-fortress-commander/src/main/resources
cd directory-fortress-commander

2. build:
mvn clean install

3. perform fortress web test policy load:
mvn install -Dload.file=./src/main/resources/FortressWebDemoUsers.xml

4. deploy:
mvn tomcat:deploy

5. redeploy (if need be):
mvn tomcat:redeploy

6. open browser and test (creds: test/password):
http://hostname:8080/fortress-web

7. Click on the links, to pull up various views on the data stored in the directory.

**** End of README *****