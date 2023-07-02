
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

# README-LOAD-TESTING

Contains instructions to load test Apache Fortress using the Apache Maven Jmeter plugin.
___________________________________________________________________________________
## Table of Contents

 1. Typical deployment
 2. Load the security policy
 3. Find the .jmx files
 4. Setting the jmeter parameters
 5. Run the tests
 6. Understanding the tests
 7. Troubleshooting

___________________________________________________________________________________
### 1. Typical deployment

```
            .--------------.      
            |     Maven    |      
            '-------.------'      
                    | in-process
            .-------'-------.
            | Jmeter plugin |
            '-------.-------'
                    | in-process
            .-------'------.
            | FortressCore |
            '-------.------'
                    | LDAPS
          .---------'-------.
          | DirectoryServer |
          '-----------------'
```

- Uses the ```mvn verify``` command to invoke maven jmeter plugin which then executes jmeter tests running various fortress core test cases. 

___________________________________________________________________________________
### 2. Load the security policy

```bash
mvn install -Dload.file=./ldap/setup/JmeterTestPolicy.xml
```

- Loads a test security policy used in the subsequent jmeter test cases. For example, the test users may be assigned a particular role that's provisioned here.

___________________________________________________________________________________
### 3. Find the .jmx files

- These metadata files contain parameters that correspond with each test case.

A. Add User:
 - [src/test/jmeter/ftAddUser.jmx](src/test/jmeter/ftAddUser.jmx)

B. Del User:
 - [src/test/jmeter/ftDelUser.jmx](src/test/jmeter/ftDelUser.jmx)

C. Check User:
 - [src/test/jmeter/ftCheckUser.jmx](src/test/jmeter/ftCheckUser.jmx)

___________________________________________________________________________________
### 4. Setting the jmeter parameters

These settings affect the length, duration, and the number of threads:

 - **LoopController.continue_forever**: boolean value, if *false*, test duration is controlled by numbers of *loops* and *threads*.
 - **LoopController.loops**: integer value, contains the number of iterations each thread performs the test function.
 - **ThreadGroup.num_threads**: integer value, contains the number of threads to use in the test.
 - **ThreadGroup.ramp_time**: integer value, number of seconds for starting threads.  A rule of thumb, set to same as num_threads.

For example:
```jmx
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

- This test will start ten threads in ten seconds.  Each thread executes the *createSession* function 1000 times before terminating.

### 5. Set the logging config

For log4j to work in maven jmeter plugin, its config file must be present under src/test/conf.

```bash
vi src/test/conf/log4j2.xml

# change log levels as needed:
<?xml version="1.0" encoding="UTF-8"?>
<Configuration>
    <Appenders>
        <File name="file" fileName="fortress-jmeter.log" append="true">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:sss} %-5p %c{1}:%L - %m%n"/>
        </File>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:sss} %-5p %c{1}:%L - %m%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Logger name="org.apache.directory.api" level="info"/>
        <Logger name="org.apache.directory.fortress.core" level="info"/>
        <!--<Logger name="rg.apache.directory.fortress.core.jmeter.CheckUser" level="debug"/>-->
        <Root level="warn">
            <AppenderRef ref="file"/>
        </Root>
    </Loggers>
</Configuration>
```

___________________________________________________________________________________
### 6. Run the tests

From **FORTRESS_HOME** folder, enter the following command from a system prompt:

A. Add Users:
 
- Will add user entry.  Optionally will perform an update and/or role assignment.
 
```bash
mvn verify -Ploadtest -Dtype=ftAddUser -Dqualifier=A1 -Dverify=true -Dsleep=30 -Dupdate=true -Dou=loadtestu -Drole=jmeterrole
```

This test adds users.  It uses runtime arguments to define behavior:
 - hostname=foo     <-- optional field useful for distributing the load across servers in a multi-master env, it will override what's in fortress.properties 
 - qualifier=A1     <-- this is used to construct userid: hostname + qualifier + counter 
 - verify=true      <-- will read after operation to verify success 
 - update=true      <-- will edit user's description if set to true 
 - sleep=30         <-- sleep this many millisecones after each op 
 - ou=loadtestu     <-- this is a required attribute on user entry and must exist in user ou tree prior to test 
 - role=jmeterrole  <-- this is an optional attribute on user entry 
     
- All but hostname may also be set as properties in [add config](src/test/jmeter/ftAddUser.jmx) or [del config](src/test/jmeter/ftDelUser.jmx) files.

B. Delete Users:

```bash
mvn verify -Ploadtest -Dtype=ftDelUser -Dqualifier=A1 -Dverify=true -Dsleep=30
```

  * Same properties as add except for 'ou', which is not used for delete ops

C. Check Users:
 
Will perform a createSession.  Optionally reads the entry and/or permission checks.

```bash
mvn verify -Ploadtest -Dtype=ftCheckUser -Dqualifier=A1 -Dverify=true -Dsize=20 -Dperm=jmeterobject.oper
```

This test performs createSession on users.  It uses runtime arguments to define behavior:
 - size=20                  <-- defines the number of users in the test set
 - perm=jmeterobject.oper   <-- this is an optional property, will perform permission checks if set

___________________________________________________________________________________
### 7. Understanding the tests

A. Qualifier property.

- The add test generates userids based on: hostname + qualifier + counter.  The counter is global across all threads, so if you enable 20 threads * 100 loops, with a qualifier = 'A1', 2,000 users will be added:

```bash
mvn verify -Ploadtest -Dtype=ftAddUser -Dqualifier=A1
```

```
hostname-A1-1
hostname-A1-2
hostname-A1-3
...
hostname-A1-1000
```

- If test runs a second time (before a delete run) there'll be duplicates because it tries to add users with same userids again.  
- This is the idea of the 'qualifier'.  
- Change its value to ensure the uids remain unique across test runs.

```bash
mvn verify -Ploadtest -Dtype=ftAddUser -Dqualifier=A2
```

- Or, run a delete prior to the next add:

```bash
mvn verify -Ploadtest -Dtype=ftDelUser -Dqualifier=A1
```

- Just make sure the thread and loop counts in ftDelUser.jmx are the same as ftAddUser.jmx

B. Verify

- If set to true, after every operation, a read of the entry will be performed.

```bash
mvn verify -Ploadtest -Dtype=ftAddUser -Dqualifier=A1 -Dverify=true
```

C. Update

- If set to true, after every add, an update will be performed on user's description field.

```bash
mvn verify -Ploadtest -Dtype=ftAddUser -Dqualifier=A1 -Dupdate=true
```

D. Role

- If 'role' set as property, it will be assigned after the user has been added.  The role itself must already exist before being used in assignment to user.

```bash
mvn verify -Ploadtest -Dtype=ftAddUser -Dqualifier=A1 -Drole=jmeterrole
```

E. Perm

- This applies only to the CheckUser test.  If set, it will be used as permission in checkAccess call 10 times.  For example:

```bash
mvn verify -Ploadtest -Dtype=ftCheckUser -Dperm=jmeterobject.oper
```

- Calls checkAccess repeatedly:

```  
obj: jmeterobject op oper1
obj: jmeterobject op oper2
obj: jmeterobject op oper3
...
obj: jmeterobject op oper10
```

- These assignments must already exist (loaded via security policy) before running this test.  

___________________________________________________________________________________
### 8. Troubleshooting

- The first place to look is the standard out.
- The files listed below contain additional info.

A. View the results
 - target/jmeter/results/[DATE]-ftAddUser.jtl
 - target/jmeter/results/[DATE]ftDelUser.jmx.jtl
 - target/jmeter/results/[DATE]ftCheckUser.jtl

B. View the jmeter logs
 - target/jmeter/logs/ftAddUser.jmx.log
 - target/jmeter/logs/ftDelUser.jmx.log
 - target/jmeter/logs/ftCheckUser.jmx.log
   C. View the Log4j logs

if file logging enabled in log4j2.xml:
* ./target/.../jmeter/bin/fortress-jmeter.log

Otherwise the log4j outputs to console

____________________________________________________________________________________
 
#### END OF README
