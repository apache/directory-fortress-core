OpenLDAP for Apache Fortress tests
==================================

This directory contains

* a `Dockerfile` for building a Docker image with preconfigured OpenLDAP for Apache Fortress
* a `run-tests.sh` script that start such a Docker container and executes the Fortress tests against it


Build image (run from fortress-core root folder)

    docker build -t apachedirectory/openldap-for-apache-fortress-tests -f src/docker/openldap-for-apache-fortress-tests/Dockerfile .


Push image to docker hub:

    docker push apachedirectory/openldap-for-apache-fortress-tests 

