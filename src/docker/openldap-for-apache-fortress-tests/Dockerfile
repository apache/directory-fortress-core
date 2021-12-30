#
#   Licensed to the Apache Software Foundation (ASF) under one
#   or more contributor license agreements.  See the NOTICE file
#   distributed with this work for additional information
#   regarding copyright ownership.  The ASF licenses this file
#   to you under the Apache License, Version 2.0 (the
#   "License"); you may not use this file except in compliance
#   with the License.  You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing,
#   software distributed under the License is distributed on an
#   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#   KIND, either express or implied.  See the License for the
#   specific language governing permissions and limitations
#   under the License.
#
FROM centos:8
# Install openldap
RUN dnf install 'dnf-command(copr)' -y
RUN dnf install epel-release -y
ADD https://repo.symas.com/configs/SOLDAP/rhel8/release25.repo /etc/yum.repos.d/soldap-release25.repo
RUN yum install symas-openldap-clients symas-openldap-servers -y
# Add fortress schema and slapd config
ADD ldap/schema/fortress.schema /opt/symas/etc/openldap/schema/
ADD ldap/schema/rbac.schema /opt/symas/etc/openldap/schema/
ADD src/docker/openldap-for-apache-fortress-tests/slapd.conf /opt/symas/etc/openldap/
# Create run directory
RUN mkdir -p /var/run/openldap
# Create database directories
RUN mkdir -p "/var/symas/openldap-data/dc=example,dc=com"
RUN mkdir -p "/var/symas/openldap-data/cn=log"
# create slapd user and group
RUN groupadd openldap
RUN useradd openldap -g openldap
# Prepare the runtime env
RUN chown -R openldap:openldap /opt/symas/lib/
RUN chown -R openldap:openldap /var/run/openldap/
RUN chown -R openldap:openldap /opt/symas/etc/openldap/
RUN chown -R openldap:openldap /var/symas/openldap-data/
RUN /opt/symas/sbin/slaptest -u -f /opt/symas/etc/openldap/slapd.conf -u
EXPOSE 389
# Start daemon
CMD ["/opt/symas/lib/slapd", "-d", "stats", "-u", "openldap", "-g", "openldap"]
