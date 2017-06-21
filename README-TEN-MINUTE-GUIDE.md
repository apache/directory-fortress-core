
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

-------------------------------------------------------------------------------
# README for Apache Fortress Ten Minute Guide

 * Version 2.0.0
 * This document has been deprecated in favor of:
    * Follow these instructions: [README-QUICKSTART-APACHEDS](./README-QUICKSTART-APACHEDS.md)
    * Follow these instructions: [README-QUICKSTART-SLAPD](./README-QUICKSTART-SLAPD.md)

-------------------------------------------------------------------------------
# Document Overview

The documents linked by this guide provide the instructions to download, build
and install Apache Fortress software from source.  It also shows how to install
ApacheDS LDAP server and get it working with Fortress.

-------------------------------------------------------------------------------
## Prerequisites

Minimum hardware requirements:
 * 2 Cores
 * 4GB RAM

Minimum software requirements:
 * Java SDK 8
 * Apache Maven3++
 * git

Everything else covered in steps that follow.  Tested on Debian & Centos systems.

-------------------------------------------------------------------------------
## Instructions for downloading app and generating the install doc:

1. Clone the directory-fortress repos from apache git by commands:

 ```
 git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-core.git
 git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-realm.git
 git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-commander.git
 git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-enmasse.git
 ```

2. Change directory to fortress core package home:
 ```
 cd directory-fortress-core/
 ```

3. Set Java and Maven home env variables.

4. Build the Fortress Core source and javadoc:
 ```
 mvn clean install
 mvn javadoc:javadoc
 ```

5. Point your web browser to the following location:
 * file:///[directory-fortress-core]/target/site/apidocs/org/apache/directory/fortress/core/doc-files/ten-minute-guide.html
 * (where [directory-fortress-core] is location of current source package)

6. Follow the steps under **Navigation Links**:
  * Setup Apache Directory Server
  * Setup Apache Directory Studio
  * Build Apache Fortress Core
  * Build Apache Fortress Realm
  * Setup Apache Tomcat Web Server
  * Build Apache Fortress Web
  * Build Apache Fortress Rest

___________________________________________________________________________________
#### END OF README-TEN-MINUTE-GUIDE