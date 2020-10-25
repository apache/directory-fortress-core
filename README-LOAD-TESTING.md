
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

# README-LOAD-TESTING

Contains instructions to load test Apache Fortress using maven jmeter plugin.

## Prerequisites

A working Apache Fortress test environment.  Follow the instructions in the README's located under package root folder.
___________________________________________________________________________________
## SECTION 1. Instructions to performance test

 This section describes the Apache Fortress jmeter test programs.

1. Load the security policy and users required by the jmeter test routines:

 ```
 mvn install -Dload.file=./ldap/setup/JmeterTestPolicy.xml
 ```

2. The .jmx files located in [.src/test/jmeter](.src/test/jmeter) folder correspond to each test type:

 A. Add User:
  [src/test/jmeter/ftAddUser.jmx](src/test/jmeter/ftAddUser.jmx)

 B. Del User:
  [src/test/jmeter/ftDelUser.jmx](src/test/jmeter/ftDelUser.jmx)

 C. Check User:
  [src/test/jmeter/ftCheckUser.jmx](src/test/jmeter/ftCheckUser.jmx)

3. Setting the jmeter parameters.

 These settings affect the length, duration, and the number of threads:

 * **LoopController.continue_forever**: boolean value, if *false*, test duration is controlled by numbers of *loops* and *threads*.
 * **LoopController.loops**: integer value, contains the number of iterations each thread performs the test function.
 * **ThreadGroup.num_threads**: integer value, contains the number of threads to use in the test.
 * **ThreadGroup.ramp_time**: integer value, number of seconds for starting threads.  A rule of thumb, set to same as num_threads.

 For example:
 ```
 <ThreadGroup guiclass="ThreadGroupGui" testclass="ThreadGroup" testname="Fortress CreateSession" enabled="true">
     ...
     <elementProp name="ThreadGroup.main_controller" ...>
         <boolProp name="LoopController.continue_forever">false</boolProp>
         <stringProp name="LoopController.loops">1000</stringProp>
     </elementProp>
     <stringProp name="ThreadGroup.num_threads">10</stringProp>
     <stringProp name="ThreadGroup.ramp_time">10</stringProp>
     ...
 </ThreadGroup>
 ```

 This test will start ten threads in ten seconds.  Each thread executes the *createSession* function 1000 times before terminating.

4.  Run the tests.

 From **FORTRESS_HOME** folder, enter the following command from a system prompt:

 A. Add Users:
 
 Will add user entry.  Optionally will perform an update and/or role assignment.
 
  ```
  mvn -Ploadtest-fortress-adduser jmeter:jmeter -Dqualifier=A1 -Dverify=true -Dsleep=30 -Dupdate=true -Dou=loadtestu -Drole=jmeterrole
  ```

  This test adds users.  It uses runtime arguments to define behavior:
   * hostname=foo     <-- optional field useful for distributing the load across servers in a multi-master env, it will override what's in fortress.properties 
   * qualifier=A1     <-- this is used to construct userid: hostname + qualifier + counter 
   * verify=true      <-- will read after operation to verify success 
   * update=true      <-- will edit user's description if set to true 
   * sleep=30         <-- sleep this many millisecones after each op 
   * ou=loadtestu     <-- this is a required attribute on user entry and must exist in user ou tree prior to test 
   * role=jmeterrole  <-- this is an optional attribute on user entry 
     
  * All but hostname may also be set as properties in [add config](src/test/jmeter/ftAddUser.jmx) or [del config](src/test/jmeter/ftDelUser.jmx) files.
     
 B. Delete Users:
 
  ```
  mvn -Ploadtest-fortress-deluser jmeter:jmeter -Dqualifier=A1 -Dverify=true -Dsleep=30
  ```

  * Same properties as add except for 'ou', which is not used for delete ops

 C. Check Users:
 
 Will perform a createSession.  Optionally reads the entry and/or permission checks.
 
  ```
  mvn -Ploadtest-fortress-checkuser jmeter:jmeter -Dqualifier=A1 -Dverify=true -Dsize=20 -Dperm=jmeterobject.oper
  ```

  This test performs createSession on users.  It uses runtime arguments to define behavior:
   * size=20                  <-- defines the number of users in the test set
   * Dperm=jmeterobject.oper  <-- this is an optional property, will perform permission checks if set

5. Understanding the tests.

A. Qualifier property.

The add test generates userids based on: hostname + qualifier + counter.  The counter is global across all threads, so if you enable 20 threads * 100 loops, with a qualifier = 'A1', 2,000 users will be added:

  ```
  mvn -Ploadtest-fortress-adduser jmeter:jmeter -Dqualifier=A1
  ```

hostname-A1-1
hostname-A1-2
hostname-A1-3
...
hostname-A1-1000

If you run the test a second time (before a delete run) there will be duplicates because it tries to add users with same userids again.  This is the idea of the 'qualifier'.  Change its value to ensure the uids remain unique across test runs.

  ```
  mvn -Ploadtest-fortress-adduser jmeter:jmeter -Dqualifier=A2
  ```

Or, you can run a delete before the next add:

  ```
  mvn -Ploadtest-fortress-deluser jmeter:jmeter -Dqualifier=A1
  ```

Just make sure the thread and loop counts in ftDelUser.jmx are the same as ftAddUser.jmx

B. Verify

If set to true, after every operation, a read of the entry will be performed.

  ```
  mvn -Ploadtest-fortress-adduser jmeter:jmeter -Dqualifier=A1 -Dverify=true
  ```

C. Update

If set to true, after every add, an update will be performed on user's description field.

  ```
  mvn -Ploadtest-fortress-adduser jmeter:jmeter -Dqualifier=A1 -Dupdate=true
  ```

D. Role

If 'role' set as property, it will be assigned after the user has been added.  The role itself must already exist before being used in assignment to user.

  ```
  mvn -Ploadtest-fortress-adduser jmeter:jmeter -Dqualifier=A1 -Drole=jmeterrole
  ```

E. Perm

This applies only to the CheckUser test.  If set, it will be used as permission in checkAccess call 10 times.  For example:

  ```
  mvn -Ploadtest-fortress-checkuser jmeter:jmeter -Dperm=jmeterobject.oper
  ```

  Will call checkAccess:
  
  obj: jmeterobject op oper1
  obj: jmeterobject op oper2
  obj: jmeterobject op oper3
  ...
  obj: jmeterobject op oper10
  
These assignments must already exist before running this test.  

____________________________________________________________________________________
 
 #### END OF README
