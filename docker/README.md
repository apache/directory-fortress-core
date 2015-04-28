> Licensed to the Apache Software Foundation (ASF) under one
> or more contributor license agreements.  See the NOTICE file
> distributed with this work for additional information
> regarding copyright ownership.  The ASF licenses this file
> to you under the Apache License, Version 2.0 (the
> "License"); you may not use this file except in compliance
> with the License.  You may obtain a copy of the License at
>
>    http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing,
> software distributed under the License is distributed on an
> "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
> KIND, either express or implied.  See the License for the
> specific language governing permissions and limitations
> under the License.


# About

A docker image with preconfigured OpenLDAP to be used for running Apache Fortress integration tests.


## Prerequisites

* Docker installation


## Build image

Download Fortress Quickstart from https://symas.com/downloads/ (fortressBuilder-Debian-Silver-x86-64-1.0-RC39.zip).

    docker build -t apachedirectory/openldap-for-apache-fortress-tests .

Or just to be sure don't use cached layers:

    docker build --no-cache=true -t apachedirectory/openldap-for-apache-fortress-tests .


## Run container

    CONTAINER_ID=$(docker run -d -P apachedirectory/openldap-for-apache-fortress-tests)
    CONTAINER_PORT=$(docker inspect --format='{{(index (index .NetworkSettings.Ports "389/tcp") 0).HostPort}}' $CONTAINER_ID)
    echo $CONTAINER_PORT


## Go into the container

    docker exec -it $CONTAINER_ID bash


## Restart container

    docker restart CONTAINER_ID


## Stop and delete container

    docker stop $CONTAINER_ID
    docker rm $CONTAINER_ID


## Run fortress-core tests

Run script `run-tests.sh` in FORTRESS_HOME. 


