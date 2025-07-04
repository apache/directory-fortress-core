
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

# OpenLDAP & Apache Fortress QUICKSTART on DOCKER

-------------------------------------------------------------------------------
## Table of Contents

 * Document Overview
 * SECTION 1. Prerequisites
 * SECTION 2. OpenLDAP Docker Image
 * SECTION 3. Apache Fortress Core Setup
 * SECTION 4. Apache Fortress Core Integration Test
 * SECTION 5. Docker Command Reference
___________________________________________________________________________________
## Document Overview

 * This document contains instructions to install Apache Fortress Core using OpenLDAP Docker image.
 * It uses [openldap-for-apache-fortress-tests](src/docker/openldap-for-apache-fortress-tests/Dockerfile)

-------------------------------------------------------------------------------
## SECTION 1. Prerequisites

Minimum software requirements:
 * Linux Machine with docker-engine
 * Java SDK >= 17
 * Apache Maven >= 3.6.3

___________________________________________________________________________________
## SECTION 2. OpenLDAP Docker Image

1. Get the Apache Fortress Core package:

a. clone git latest: 

```bash
git clone https://gitbox.apache.org/repos/asf/directory-fortress-core.git
cd directory-fortress-core
```

b. or clone git release: 

```bash
git clone --branch [version]  https://gitbox.apache.org/repos/asf/directory-fortress-core.git
cd directory-fortress-core
```

c. or download source package from Apache:  

```bash
wget https://www.apache.org/dist/directory/fortress/dist/[version]/fortress-core-[version]-source-release.zip
unzip fortress-core-[version]-source-release.zip
cd fortress-core-[version]
```

2. Prepare the OpenLDAP docker image

a. build the OpenLDAP docker image (trailing dot matters):

```bash
docker build -t apachedirectory/openldap-for-apache-fortress-tests -f src/docker/openldap-for-apache-fortress-tests/Dockerfile .
```

b. or pull the prebuilt image:

```bash
docker pull apachedirectory/openldap-for-apache-fortress-tests
```

3. Run the OpenLDAP docker image:

```bash
docker run --name=openldap -d -p 389:389 -P apachedirectory/openldap-for-apache-fortress-tests
```

 * Here we're mapping the internal port for running image to what the host machine uses, w/ OpenLDAP's default of 389
 * Setting name=openldap for managing image.

4. Verify the image started successfully:

```bash
root@localhost:/opt/fortress/directory-fortress-core# docker ps
CONTAINER ID   IMAGE                                                COMMAND                  CREATED         STATUS         PORTS                                   NAMES
8ca5feb67906   apachedirectory/openldap-for-apache-fortress-tests   "/opt/symas/lib/slap…"   3 seconds ago   Up 2 seconds   0.0.0.0:389->389/tcp, :::389->389/tcp   openldap
```

5. Manage OpenLDAP Image:

a. start 

```bash
docker run --name=openldap -d -p 389:389 -P apachedirectory/openldap-for-apache-fortress-tests
```

b. stop 

```bash
docker stop openldap
```

c. remove 

```bash
docker rm openldap
```

d. view the logs 

```bash
docker logs openldap
```

e. inspect 

```bash
docker inspect openldap
```

f. connect via bash 

```bash
docker exec -it openldap bash
```

g. version of slapd running in container

```bash
root@c88f854e2e73:/# /opt/symas/lib/slapd -VV
```

## SECTION 3. Apache Fortress Core Setup

1. Prepare your terminal for execution of maven commands.

```bash
#!/bin/bash
export M2_HOME=...
export JAVA_HOME=...
export PATH=$PATH:$M2_HOME/bin
```

2. Prepare the package:

```bash
cp build.properties.example build.properties
cp slapd.properties.example slapd.properties
```

 * Seeds the fortress properties with defaults for OpenLDAP usage.
 * [slapd.properties.example](slapd.properties.example) contains the default config for openldap docker image.
 * Learn how the fortress config subsystem works: [README-CONFIG](README-CONFIG.md).

3. Run the maven install to build fortress and initialize config settings:

```bash
mvn clean install
```
___________________________________________________________________________________
## SECTION 4. Apache Fortress Core Integration Test

1. From fortress core base folder, enter the following commands:

```bash
mvn install -Dload.file=./ldap/setup/refreshLDAPData.xml
```

 *These will build the Directory Information Tree (DIT), create the config and data policies needed for the integration test to follow.*

2. Next, enter the following command:

```bash
mvn -Dtest=FortressJUnitTest test
```

 More about this step: 
  * Tests the APIs against your LDAP server.

3. Verify the tests worked:

```bash
NUMBER OF ADDS: 1146
NUMBER OF BINDS: 1131
NUMBER OF DELETES: 0
NUMBER OF MODS: 4247
NUMBER OF READS: 22885
NUMBER OF SEARCHES: 9684
Tests run: 133, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 323.006 sec - in org.apache.directory.fortress.core.impl.FortressJUnitTest
Results :
Tests run: 133, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] --- maven-antrun-plugin:1.8:run (fortress-load) @ fortress-core ---
[INFO] Executing tasks
fortress-load:
[INFO] Executed tasks
[INFO] 
[INFO] --- maven-antrun-plugin:1.8:run (fortress-load-debug) @ fortress-core ---
[INFO] Executing tasks
fortress-load-debug:
[INFO] Executed tasks
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  05:45 min
[INFO] Finished at: 2022-01-02T15:04:13Z
[INFO] ------------------------------------------------------------------------
```

4. Rerun the tests to verify teardown APIs work:

```bash
mvn -Dtest=FortressJUnitTest test
```

5. Verify that worked also:

```bash
NUMBER OF ADDS: 993
NUMBER OF BINDS: 1131
NUMBER OF DELETES: 1355
NUMBER OF MODS: 8389
NUMBER OF READS: 30040
NUMBER OF SEARCHES: 12221
Tests run: 167, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 369.396 sec - in org.apache.directory.fortress.core.impl.FortressJUnitTest
Results :
Tests run: 167, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] --- maven-antrun-plugin:1.8:run (fortress-load) @ fortress-core ---
[INFO] Executing tasks
fortress-load:
[INFO] Executed tasks
[INFO] 
[INFO] --- maven-antrun-plugin:1.8:run (fortress-load-debug) @ fortress-core ---
[INFO] Executing tasks
fortress-load-debug:
[INFO] Executed tasks
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  06:33 min
[INFO] Finished at: 2022-01-02T15:13:10Z
[INFO] ------------------------------------------------------------------------
```
 Notice more tests ran this time vs the first time, due to teardown

 Test Notes:
  * If tests complete without errors, Apache Fortress works with OpenLDAP server running in Docker image.
  * These tests load thousands of objects into the target ldap server.
  * Warning messages are negative tests in action.

6. Optional sections in the [README](README.md) file:

 * SECTION 11. Instructions to run the Apache Fortress Command Line Interpreter (CLI).
 * SECTION 12. Instructions to run the Apache Fortress Command Console.
 * SECTION 13. Instructions to build and test the Apache Fortress samples.
 * SECTION 14. Instructions to performance test.

___________________________________________________________________________________
## SECTION 5. Docker Command Reference

#### Build image

```bash
docker build -t apachedirectory/openldap-for-apache-fortress-tests -f src/docker/openldap-for-apache-fortress-tests/Dockerfile .
```

 * trailing dot matters

 Or just to be sure don't use cached layers:

```bash
docker build  --no-cache=true -t apachedirectory/openldap-for-apache-fortress-tests -f src/docker/openldap-for-apache-fortress-tests/Dockerfile .
```

#### Run container with specified id and port mapping

```bash
docker run --name=openldap -d -p 32768:389 -P apachedirectory/openldap-for-apache-fortress-tests
```

#### Go into the container

```bash
docker exec -it openldap
```

#### Restart container

```bash
docker restart openldap
```

#### Stop and delete container

```bash
docker stop openldap
docker rm openldap
```

____________________________________________________________________________________
#### END OF README-QUICKSTART-DOCKER-SLAPD
