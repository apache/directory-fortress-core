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

FROM java:8

# Prerequisites: Fortress Quickstart downloaded from https://symas.com/downloads/
ADD fortressBuilder-Debian-Silver-x86-64-1.0-RC39.zip /root/fortress/

WORKDIR /root/fortress

RUN \
    # Fortress Quickstart README Section I
    unzip fortressBuilder-Debian-Silver-x86-64-1.0-RC39.zip && \
    chmod a+x -Rf * && \
    # Workaround for FC-33: enable rbac.accelerator=true to enable ACLs in slapd.conf
    sed -i 's/#rbac\.accelerator=true/rbac\.accelerator=true/' build.properties && \
    # Fortress Quickstart README Section II
    sed -i 's/export JAVA_HOME=.*/export JAVA_HOME=\/usr\/lib\/jvm\/java-1.8.0-openjdk-amd64\//' b.sh && \
    ./b.sh dist && \
    # Fortress Quickstart README Section III
    ./b.sh init-slapd && \
    # Cleanup: only the configured OpenLDAP server is required
    rm -rf /root/fortress && \
    rm -rf /root/.ivy2
    
WORKDIR /root

EXPOSE 389

CMD ["/opt/symas/lib64/slapd", "-d", "32768", "-u", "root", "-g", "root"]

