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
# last updated: February 5, 2015
###################################################################################
_____________________________________________________________________________
#############################################################################
# Prerequisites:
#############################################################################
1. Linux machine (tested on Ubuntu 12.04 & Centos 6.3)
2. Internet access to retrieve dependencies from online Maven repo.
3. Java SDK Version 7 (or greater), Apache Maven 3, and Git installed to target machine.
4. All other packages will be covered inside the tutorial.
#############################################################################
# Instructions for downloading app and generating the install doc:
#############################################################################
1. Clone the directory-fortress-core from apache git repo:
# git clone https://git-wip-us.apache.org/repos/asf/directory-fortress-core.git

2. Change directory to package home:
# cd directory-fortress-core/

3. Set JAVA_HOME
# export JAVA_HOME=...

4. Set Maven Home:
# export M2_HOME=...

5. Build the Fortress Core source:
# $M2_HOME/bin/mvn clean install -DskipTests

5. Create the Fortress Core javadoc:
# $M2_HOME/bin/mvn javadoc:javadoc

6. Point your web browser to the following location:s
   file:///[package home]/target/site/apidocs/org/apache/directory/fortress/core/doc-files/ten-minute-guide.html

   (where [package_home] is location of directory-fortress-core base package)

7. Follow the steps under 'Navigation Links'