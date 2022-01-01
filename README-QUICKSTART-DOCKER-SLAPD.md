
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

# OpenLDAP & Fortress QUICKSTART on DOCKER

-------------------------------------------------------------------------------
## Table of Contents

 * Document Overview
 * SECTION 1. Prerequisites
 * SECTION 2. Apache Fortress Core Setup using OpenLDAP Docker Image
 * SECTION 3. Apache Fortress Core Integration Test
 * SECTION 4. Docker Commands
___________________________________________________________________________________
## Document Overview

 * This document contains instructions to install Apache Fortress Core using OpenLDAP Docker image.
 * It uses [openldap-for-apache-fortress-tests](src/docker/openldap-for-apache-fortress-tests/Dockerfile)

-------------------------------------------------------------------------------
## SECTION 1. Prerequisites

Minimum software requirements:
 * Centos or Debian Machine
 * Java SDK 8++
 * Apache Maven3++
 * docker-engine

___________________________________________________________________________________
## SECTION 2. Apache Fortress Core Setup using OpenLDAP Docker Image

1. Download the package:

 a. from git:
```
git clone --branch 2.0.7  https://gitbox.apache.org/repos/asf/directory-fortress-core.git
cd directory-fortress-core
```

b. or from Apache:
```
wget https://www.apache.org/dist/directory/fortress/dist/2.0.7/fortress-core-2.0.7-source-release.zip
unzip fortress-core-2.0.7-source-release.zip
cd fortress-core-2.0.7
```

2. Prepare the package:

```
cp build.properties.example build.properties
cp slapd.properties.example slapd.properties
```

 * Seeds the openldap properties with defaults.
 * [slapd.properties.example](slapd.properties.example) contains the default config for openldap docker image.
 * Learn how the fortress config subsystem works: [README-CONFIG](README-CONFIG.md).

3. Now build the apachedirectory openldap docker image (trailing dot matters):

```
docker build -t apachedirectory/openldap-for-apache-fortress-tests -f src/docker/openldap-for-apache-fortress-tests/Dockerfile .
```

 Or just pull the prebuilt image:

```
docker pull apachedirectory/openldap-for-apache-fortress-tests
```

4. Run the docker container:

```
docker run --name=openldap -d -p 32768:389 -P apachedirectory/openldap-for-apache-fortress-tests
```

 * Depending on your Docker setup, may need to run this step as root or sudo priv's.

5. Prepare your terminal for execution of maven commands.

```
#!/bin/sh
export M2_HOME=...
export JAVA_HOME=...
export PATH=$PATH:$M2_HOME/bin
export MAVEN_OPTS="
    -Dfortress.host=localhost  
    -Dfortress.port=32768"
```

 More about 'MAVEN_OPTS':  
  * Provides the coordinates to the ldap server running inside Docker container.  
  * replace the 'fortress.port' value with result from ```echo $CONTAINER_PORT```.
  * if Docker image running on a different machine, replace fortress.host to point to it.  

6. Run the maven install to build fortress and initialize config settings:

```
mvn clean install
```
___________________________________________________________________________________
## SECTION 3. Apache Fortress Core Integration Test

1. From fortress core base folder, enter the following commands:

```
mvn install -Dload.file=./ldap/setup/refreshLDAPData.xml -Dfortress.host=localhost -Dfortress.port=32768
```

 *These will build the Directory Information Tree (DIT), create the config and data policies needed for the integration test to follow.*

2. Next, enter the following command:

```
mvn -Dtest=FortressJUnitTest test -Dfortress.host=localhost -Dfortress.port=32768
```

 More about this step: 
  * Provides the coordinates to the ldap server running inside Docker container.  
  * Replace the 'fortress.port' value with result from ```echo $CONTAINER_PORT```.
  * if Docker image running on a different machine, replace fortress.host to point to it.
  * Tests the APIs against your LDAP server.*

3. Verify the tests worked:

```
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

```
mvn -Dtest=FortressJUnitTest test -Dfortress.host=localhost -Dfortress.port=32768
```

 More about this step: 
  * Again verify fortress.host and fortress.port match your environment.  

5. Verify that worked also:

```
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
 Notice more tests ran this time vs the first time, due to teardown

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
## SECTION 4. Docker Commands

#### Build image

```
docker build -t apachedirectory/openldap-for-apache-fortress-tests -f src/docker/openldap-for-apache-fortress-tests/Dockerfile .
```

 * trailing dot matters

 Or just to be sure don't use cached layers:

```
docker build  --no-cache=true -t apachedirectory/openldap-for-apache-fortress-tests -f src/docker/openldap-for-apache-fortress-tests/Dockerfile .
```

#### Run container

```
CONTAINER_ID=$(docker run -d -P apachedirectory/openldap-for-apache-fortress-tests)
CONTAINER_PORT=$(docker inspect --format='{{(index (index .NetworkSettings.Ports "389/tcp") 0).HostPort}}' $CONTAINER_ID)
echo $CONTAINER_PORT
```
#### Or Run container with specified id and port mapping

```
docker run --name=openldap-fortress -d -p 32768:389 -P apachedirectory/openldap-for-apache-fortress-tests
```

#### Go into the container

```
docker exec -it $CONTAINER_ID bash
```

#### Restart container

```
docker restart $CONTAINER_ID
```

#### Stop and delete container

```
docker stop $CONTAINER_ID
docker rm $CONTAINER_ID
```

____________________________________________________________________________________
#### END OF README-QUICKSTART-DOCKER-SLAPD
