   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE file
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this file
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this file except in compliance
   with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.

-------------------------------------------------------------------------------
# README for Apache Fortress Multitenancy Configuration
-------------------------------------------------------------------------------

## Table of Contents

 * SECTION 1. Fortress Multitenancy Overview.
 * SECTION 2. About the Datastructures.
 * SECTION 3. How the APIs work.
 * SECTION 4. How to Setup a New Tenant.
 * SECTION 5. Unit Testing

-------------------------------------------------------------------------------
## SECTION 1.  Fortress Multitenancy Overview

* *Software Multitenancy refers to a software architecture in which a single instance of a software runs on a server and serves multiple tenants. A tenant is a group of users who share a common access with specific privileges to the software instance. With a multitenant architecture, a software application is designed to provide every tenant a dedicated share of the instance including its data, configuration, user management, tenant individual functionality and non-functional properties. Multitenancy contrasts with multi-instance architectures, where separate software instances operate on behalf of different tenants.*

 *Commentators regard multitenancy as an important feature of cloud computing.*

 https://en.wikipedia.org/wiki/Multitenancy

* More info here: https://symas.com/products/symas-enforcement-foundry/multi-tenancy/

-------------------------------------------------------------------------------
## SECTION 2.  About the Datastructures

1. Each tenant gets its own copy of the DIT.  For example if a tenant's id is acme123, there will be a container underneath the suffix:

 ```
 ou=acme123, dc=example, dc=com.
 ```

2. Beneath the acme123 suffix will be that tenant's own copy of data.  For example:

 ```
 ou=People, ou=acme, dc=example, dc=com
 ou=Roles, ou=RBAC, ou=acme, dc=example, dc=com
 ou=Permissions, ou=RBAC, ou=acme, dc=example, dc=com
 etc...
 ```

-------------------------------------------------------------------------------
## SECTION 3.  How the APIs work

Then tenant id is passed in factory initialization.  For example:

 ```
 AdminMgr adminMgr = AdminMgrFactory.createInstance( "acme123" );
 ```

 The lifecycle of that particular object will be on behalf of that tenant id.

-------------------------------------------------------------------------------
## SECTION 4.  How to Setup a New Tenant

1. Use the Fortress load utility to set up new tenant contexts.  The 'addcontainer' tag can be used to do this:

 ```
 <addcontainer>
    <container name="acme123" description="ACME 123 tenant context"/>
 ...
 </addcontainer>
 ```

2. Or simply use ldif to create a new object, of type organizational unit, beneath the suffix:
 ```
 dn: ou=acme123, dc=example,dc=com
 ou: acme123
 objectClass: organizationalUnit
 description: ACME 123 tenant context
 ```

3. Now you may use the load utility to initialize the basic DIT.  For example:

 ```
 <addcontainer>
   <container name="People" description="Fortress People"/>
   <container name="Policies" description="Fortress Policies"/>
   <container name="RBAC" description="Fortress RBAC Policies"/>
   <container name="Roles" parent="RBAC" description="Fortress Roles"/>
   <container name="Permissions" parent="RBAC" description="Fortress Permissions"/>
   <container name="Constraints" parent="RBAC" description="Fortress Separation of Duty Constraints"/>
   <container name="ARBAC" description="Fortress Administrative RBAC Policies"/>
   <container name="OS-U" parent="ARBAC" description="Fortress User Organizational Units"/>
   <container name="OS-P" parent="ARBAC" description="Fortress Perm Organizational Units"/>
   <container name="AdminRoles" parent="ARBAC" description="Fortress AdminRoles"/>
   <container name="AdminPerms" parent="ARBAC" description="Fortress Admin Permissions"/>
 </addcontainer>
 ```

4. And when running the load, pass the tenant id:
 ```
 mvn install -Dload.file=./ldap/setup/MyLoadFile.xml -Dtenant=acme123
 ```

 Passing tenant system properties scopes all subsequent fortress xml load operations to that particular tenant.

___________________________________________________________________________________
## SECTION 5.  Unit Testing

Pass the tenant id as system property when running the tests:

 ```
 mvn test -Dtest=FortressJUnitTest -Dtenant=acme123
 ```

 Passing tenant system properties scopes all subsequent test operations to that particular tenant.

___________________________________________________________________________________
#### END OF README-MULTITENANCY