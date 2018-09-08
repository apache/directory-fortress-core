#!/bin/sh
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

# stop execution if any command fails (i.e. exits with status code > 0)
set -e

# trace commands
set -x

# startup docker container
docker pull apachedirectory/apacheds-for-apache-fortress-tests
CONTAINER_ID=$(docker run -d -P apachedirectory/apacheds-for-apache-fortress-tests)
CONTAINER_PORT=$(docker inspect --format='{{(index (index .NetworkSettings.Ports "10389/tcp") 0).HostPort}}' $CONTAINER_ID)
echo $CONTAINER_PORT

# configure build.properties
cp build.properties.example build.properties
sed -i 's/^ldap\.server\.type=.*/ldap.server.type=apacheds/' build.properties
sed -i 's/^ldap\.host=.*/ldap.host=localhost/' build.properties
sed -i 's/^ldap\.port=.*/ldap.port='${CONTAINER_PORT}'/' build.properties
sed -i 's/^suffix\.name=.*/suffix.name=example/' build.properties
sed -i 's/^suffix\.dc=.*/suffix.dc=com/' build.properties
sed -i 's/^root\.dn=.*/root.dn=uid=admin,ou=system/' build.properties
sed -i 's/^root\.pw=.*/root.pw={SSHA}pSOV2TpCxj2NMACijkcMko4fGrFopctU/' build.properties
sed -i 's/^cfg\.root\.pw=.*/cfg.root.pw=secret/' build.properties

# prepare
mvn clean install
mvn install -Dload.file=./ldap/setup/refreshLDAPData.xml
mvn install -Dload.file=./ldap/setup/DelegatedAdminManagerLoad.xml

# run tests
mvn test -Dtest=FortressJUnitTest

# rerun tests to verify teardown APIs work
mvn test -Dtest=FortressJUnitTest

# stop and delete docker container
docker stop $CONTAINER_ID
docker rm $CONTAINER_ID

