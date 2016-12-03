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

FROM debian:8

# Install openldap
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update
RUN apt-get install -y -qq slapd ldap-utils

# Add fortress schema and slapd config
ADD ldap/schema/fortress.schema /etc/ldap/schema/
ADD ldap/schema/rbac.schema /etc/ldap/schema/
ADD src/docker/openldap-for-apache-fortress-tests/slapd.conf /etc/ldap/

# Create database directories
RUN mkdir -p /var/lib/ldap/dflt
RUN mkdir -p /var/lib/ldap/hist
RUN chown -R openldap:openldap /var/lib/ldap

# Delete slapd-config which was created during installation
# and create new one by converting from old slapd.conf
RUN rm -rf /etc/ldap/slapd.d/*
RUN slaptest -u -f /etc/ldap/slapd.conf -F /etc/ldap/slapd.d

EXPOSE 389

CMD ["/usr/sbin/slapd", "-d", "32768", "-u", "root", "-g", "root"]

