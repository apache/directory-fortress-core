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
# README for Fortress Ten Minute Guide
# Version 1.0-RC40
# last updated: February 15, 2015
###################################################################################
___________________________________________________________________________________
###################################################################################
# SECTION 0.  Prerequisites for Fortress Ten Minute Guide
###################################################################################
1. Linux machine (tested on Ubuntu 12.04, 14.04 & Centos 6.3, 7)

2. Internet access to retrieve source code from Apache Fortress GIT repos and binary dependencies from online Maven repo.

3. Git installed to target machine.

4. Java SDK Version 7 (or beyond) sdk installed to target machine.

5. Apache Maven 3 installed to target machine.

All other packages will be covered inside the tutorial.

#############################################################################
# Instructions for downloading app and generating the install doc:
#############################################################################

1. Clone the directory-fortress repos from apache git:
# git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-core.git
# git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-realm.git
# git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-commander.git
# git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-enmasse.git

2. Change directory to fortress core package home:
# cd directory-fortress-core/

3. Set java and maven home env variables.

4. Build the Fortress Core source:
# mvn clean install -DskipTests

5. Create the Fortress Core javadoc:
# mvn javadoc:javadoc

6. Point your web browser to the following location:
   file:///[directory-fortress-core]/target/site/apidocs/org/apache/directory/fortress/core/doc-files/ten-minute-guide.html

   (where [directory-fortress-core] is location of current source package)

7. Follow the steps under 'Navigation Links':
  a. Setup Apache Directory Server
  b. Setup Apache Directory Studio
  c. Build Apache Fortress Core
  d. Build Apache Fortress Realm
  e. Setup Apache Tomcat Web Server
  f. Build Apache Fortress Web
  g. Build Apache Fortress Rest
