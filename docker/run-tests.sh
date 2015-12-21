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

# startup docker container
CONTAINER_ID=$(docker run -d -P apachedirectory/openldap-for-apache-fortress-tests)
CONTAINER_PORT=$(docker inspect --format='{{(index (index .NetworkSettings.Ports "389/tcp") 0).HostPort}}' $CONTAINER_ID)
echo $CONTAINER_PORT

# configure build.properties
cp slapd.properties.example slapd.properties
sed -i 's/^ldap\.server\.type=.*/ldap.server.type=openldap/' slapd.properties
sed -i 's/^enable\.audit=.*/enable.audit=true/' slapd.properties
sed -i 's/^ldap\.host=.*/ldap.host=localhost/' slapd.properties
sed -i 's/^ldap\.port=.*/ldap.port='${CONTAINER_PORT}'/' slapd.properties
sed -i 's/^suffix\.name=.*/suffix.name=openldap/' slapd.properties
sed -i 's/^suffix\.dc=.*/suffix.dc=org/' slapd.properties
sed -i 's/^root\.dn=.*/root.dn=cn=Manager,${suffix}/' slapd.properties
sed -i 's/^root\.pw=.*/root.pw={SSHA}pSOV2TpCxj2NMACijkcMko4fGrFopctU/' slapd.properties
sed -i 's/^cfg\.root\.pw=.*/cfg.root.pw=secret/' slapd.properties

# prepare
mvn clean install
mvn install -Dload.file=./ldap/setup/refreshLDAPData.xml
mvn install -Dload.file=./ldap/setup/DelegatedAdminManagerLoad.xml

# run tests
mvn test -Dtest=FortressJUnitTest

# stop and delete docker container
docker stop $CONTAINER_ID
docker rm $CONTAINER_ID

