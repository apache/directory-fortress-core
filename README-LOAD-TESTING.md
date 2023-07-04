
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
 5. Configure Log4j2
 6. Run the tests
 7. Understanding the tests
 8. Troubleshooting

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

D. Create Session:
- [src/test/jmeter/ftCreateSession.jmx](src/test/jmeter/ftCreateSession.jmx)

E. Bind User:
 - [src/test/jmeter/ftBindUser.jmx](src/test/jmeter/ftBindUser.jmx)

F. Check Access:
 - [src/test/jmeter/ftCheckAccess.jmx](src/test/jmeter/ftCheckAccess.jmx)

G. Check Role:
 - [src/test/jmeter/ftCheckRole.jmx](src/test/jmeter/ftCheckRole.jmx)

___________________________________________________________________________________
### 4. Setting the jmeter parameters

These settings affect the length, duration, and the number of threads:

 - **LoopController.continue_forever**: boolean value, if *false*, test duration is controlled by numbers of *loops* and *threads*.
 - **LoopController.loops**: integer value, contains the number of iterations each thread performs the test function.
 - **ThreadGroup.num_threads**: integer value, contains the number of threads to use in the test.
 - **ThreadGroup.ramp_time**: integer value, number of seconds for starting threads.  A rule of thumb, set to same as num_threads.

For example:
```xml
<ThreadGroup guiclass="ThreadGroupGui" testclass="ThreadGroup" testname="Fortress AddUser" enabled="true">
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

- This test starts ten threads in ten seconds. Each thread executes the test case 1000 times before terminating.

### 5. Configure Log4j2

The jmeter-maven-plugin log4j config file, [log4j2.xml](src/test/conf/log4j2.xml) must be present in the src/test/conf folder.

```xml
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
        <Root level="warn">
            <AppenderRef ref="file"/>
        </Root>
    </Loggers>
</Configuration>
```

Tips:
- Use course-grained logging and a file appender for application logging.
- Use console output for viewing the jmeter test progress. 
- Use the jmeter and log4j log files (more later) when troubleshooting and reporting.

More info on testing with the jmeter-maven-plugin: 
- [Basic Configuration](https://github.com/jmeter-maven-plugin/jmeter-maven-plugin/wiki/Basic-Configuration)
___________________________________________________________________________________
### 6. Run the tests

#### Description of the tests

The tests run from the command line as a maven profile.

| Name           | Usage                                               |
|----------------|-----------------------------------------------------|
| add users      | mvn verify -Ploadtest -Dtype=ftAddUser [args]       |
| del users      | mvn verify -Ploadtest -Dtype=ftDelUser [args]       |
| user check     | mvn verify -Ploadtest -Dtype=ftCheckUser [args]     |
| create session | mvn verify -Ploadtest -Dtype=ftCreateSession [args] |
| bind user      | mvn verify -Ploadtest -Dtype=ftBindUser [args]      |
| check access   | mvn verify -Ploadtest -Dtype=ftCheckAccess  [args]  |
| check role     | mvn verify -Ploadtest -Dtype=ftCheckRole  [args]    |

#### Description of runtime arguments

The following may be injected into the runtime either as Java system (-D) command-line arguments and/or jmeter params:

| Name      | Type    | Test                      | Jmeter param | Java system prop | Description                                                                                | Example                | Default                |
|-----------| ------- |---------------------------|--------------| ---------------- |--------------------------------------------------------------------------------------------|------------------------|------------------------|
| qualifier | String  | all                       | True         | True             | Part of the userid: hostname + qualifier + counter.                                        | qualifier=A1           | none                   |
| verify    | Boolean | all                       | True         | True             | e.g. Read after op to verify success.                                                      | verify=true            | false                  |
| update    | Boolean | ftAddUser                 | True         | True             | Edit user's description if set to true.                                                    | update=true            | false                  |
| sleep     | Integer | all                       | True         | True             | Sleep this many milliseconds after op.                                                     | sleep=30               | none (no sleep)        |
| hostname  | String  | all                       | False        | True             | Useful for distributing the load in a multi-master env. Will override fortress.properties. | hostname=foo           | none                   |
| duplicate | Integer | ftAddUser,ftDelUser       | False        | True             | Duplicate operation after specified interval.                                              | duplicate=1000         | none (don't duplicate) |
| batchsize | Integer | ftCheckuser,ftCheckAccess | True         | True             | Set the number of users in sample size to iterate over.                                    | batchsize=1000         | 10                     |
| ou        | String  | ftAddUser                 | True         | True             | The group name used                                                                        | name=localhost-A1-1    | none                   |
| role      | String  | ftAddUser,ftCheckRole     | True         | True             | The role name used                                                                         | role=jmeterrole        | none                   |
| perm      | String  | ftCheckUser,ftCheckAccess | True         | True             | Required format is objectName.operationName                                                | perm=jmeterobject.oper | none                   |
| password  | String  | all                       | True         | True             | Set the password for test users                                                            | password=newsecret     | secret                 |

* The Java system properties take precedence over jmeter params.

#### Examples of jmeter tests

From **FORTRESS_HOME** folder, enter the following command from a system prompt:

A. Add Users:
 
- Optionally performs updates and/or role assignments on each entry.
 
```bash
mvn verify -Ploadtest -Dtype=ftAddUser -Dqualifier=A1 -Dverify=true -Dsleep=30 -Dupdate=true -Dou=loadtestu -Drole=jmeterrole -Dpassword=secret
```

This test adds users.  It uses runtime arguments to define behavior:
 - qualifier=A1     <-- construct userid: hostname + qualifier + counter 
 - verify=true      <-- read after operation to verify success 
 - update=true      <-- edit user's description if set to true 
 - sleep=30         <-- sleep this many milliseconds between ops 
 - ou=loadtestu     <-- required attribute on user and must exist in user ou tree prior to test 
 - role=jmeterrole  <-- optional attribute on user 
 - hostname=foo     <-- optional override to fortress.properties. For distributing load across servers in a multi-provider env.
 - password=secret  <-- optional override to default. Can be set as jmeter property.

- All but hostname may also be set as properties in [add config](src/test/jmeter/ftAddUser.jmx) or [del config](src/test/jmeter/ftDelUser.jmx) files.

B. Delete Users:

```bash
mvn verify -Ploadtest -Dtype=ftDelUser -Dqualifier=A1 -Dverify=true -Dsleep=30
```

  * Same properties as add except for 'ou', which is not used for delete ops

C. Check Users:
 
Will perform a createSession.  Optionally verification and permission checks.

```bash
mvn verify -Ploadtest -Dtype=ftCheckUser -Dqualifier=A1 -Dverify=true -Dperm=jmeterobject.oper -Dbatchsize=10000
```

This test performs createSession and optionally checkAccess on users.  It uses runtime arguments to define behavior:
 - perm=jmeterobject.oper   <-- optional, performs permission checks if set. Format is objectName.operationName.
 - batchsize=10000          <-- we have 10000 users in our batch

D. Create Session:
 
Only createSession.

```bash
mvn verify -Ploadtest -Dtype=ftCreateSession -Dqualifier=A1 -Dbatchsize=10000
```

E. Check Access:
 
Will perform a createSession and one permission check.

```bash
mvn verify -Ploadtest -Dtype=ftCheckAccess -Dqualifier=A1 -Dperm=jmeterobject.oper -Dbatchsize=10000
```
This test performs createSession and checkAccess on users.  It uses runtime arguments to define behavior:
- perm=jmeterobject.oper   <-- This is required. Format: objectName.operationName.

F. Bind User:

Only bindUser.

```bash
mvn verify -Ploadtest -Dtype=ftBindUser -Dqualifier=A1 -Dbatchsize=10000
```

G. Check Role:

Will perform a createSession and one role check.

```bash
mvn verify -Ploadtest -Dtype=ftCheckRole -Dqualifier=A1 -Drole=jmeterrole -Dbatchsize=10000
```

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

- Tests that call checkAccess method (CheckUser, CheckAccess) will fail if role has not been assigned.
- Tests that call isUserInRole method (CheckRole) will fail if not assigned.

E. Perm

- This param is optional in CheckUser and required in CheckAccess.  

```bash
# will call checkAccess 10 times (if set) per test iteration:
mvn verify -Ploadtest -Dtype=ftCheckUser -Dperm=jmeterobject.oper -Dbatchsize=10000
# calls checkAccess once per test:
mvn verify -Ploadtest -Dtype=ftCheckAccess -Dperm=jmeterobject.oper -Dbatchsize=10000
```

- These permissions must already exist (loaded via security policy) before running this test.
- CheckUser will iterate over 10 perms, e.g. jmeter.oper1, jmeter.oper2, jmeter.oper3, ... jmeter.oper10
- CheckUser picks a random number between 1 and 10 and uses that.

___________________________________________________________________________________
### 8. Troubleshooting

- The first place to look is the standard out.
- The files listed below contain additional info.

A. View the results
 - target/jmeter/results/[DATE]-ftAddUser.csv
 - target/jmeter/results/[DATE]ftDelUser.jmx.csv
 - target/jmeter/results/[DATE]ftCheckUser.csv
 - target/jmeter/results/[DATE]ftCreateSession.csv
 - target/jmeter/results/[DATE]ftCheckAccess.csv
 - target/jmeter/results/[DATE]ftBindUser.csv
 - target/jmeter/results/[DATE]ftCheckRole.csv

B. View the Log4j logs

if file logging enabled in log4j2.xml:
* ./target/.../jmeter/bin/fortress-jmeter.log

Otherwise log4j outputs to console

#### Common Errors

##### A. jmeter tests fail java.lang.NoSuchMethodError

Happens when jmeter test instance can't load all of its classes. Ensure the required libs have been loaded.

```bash
[INFO] Executing test: ftAddUser.jmx
[INFO] Arguments for forked JMeter JVM: [java, -Xms1024M, -Xmx1024M, -Djava.awt.headless=true, -jar, ApacheJMeter-5.5.jar, -d, /home/smckinn/GIT/fortressDev/directory-fortress-core/target/b8ccc8a0-8845-4d34-aa10-b4ec7f50f4f5/jmeter, -j, /home/smckinn/GIT/fortressDev/directory-fortress-core/target/jmeter/logs/ftAddUser.jmx.log, -l, /home/smckinn/GIT/fortressDev/directory-fortress-core/target/jmeter/results/20230702-ftAddUser.csv, -n, -t, /home/smckinn/GIT/fortressDev/directory-fortress-core/target/jmeter/testFiles/ftAddUser.jmx, -Dsun.net.http.allowRestrictedHeaders, true, -Dqualifier, A10, -Dversion, 3.0.0-SNAPSHOT]
[INFO]  
[INFO] java.lang.NoSuchMethodError: 'void org.apache.logging.slf4j.Log4jLoggerFactory.<init>(org.apache.logging.slf4j.Log4jMarkerFactory)'
[INFO]  at org.apache.logging.slf4j.SLF4JServiceProvider.initialize(SLF4JServiceProvider.java:54)
[INFO]  at org.slf4j.LoggerFactory.bind(LoggerFactory.java:183)
[INFO]  at org.slf4j.LoggerFactory.performInitialization(LoggerFactory.java:170)
[INFO]  at org.slf4j.LoggerFactory.getProvider(LoggerFactory.java:455)
[INFO]  at org.slf4j.LoggerFactory.getILoggerFactory(LoggerFactory.java:441)
[INFO]  at org.slf4j.LoggerFactory.getLogger(LoggerFactory.java:390)
[INFO]  at org.slf4j.LoggerFactory.getLogger(LoggerFactory.java:416)
[INFO]  at org.apache.jmeter.JMeter.<clinit>(JMeter.java:113)
[INFO]  at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
[INFO]  at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
[INFO]  at java.base/jdk.internal.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
[INFO]  at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:490)
[INFO]  at org.apache.jmeter.NewDriver.main(NewDriver.java:257)
[INFO] JMeter home directory was detected as: /home/smckinn/GIT/fortressDev/directory-fortress-core/target/b8ccc8a0-8845-4d34-aa10-b4ec7f50f4f5/jmeter
[INFO] Completed Test: /home/smckinn/GIT/fortressDev/directory-fortress-core/target/jmeter/testFiles/ftAddUser.jmx
[INFO]  
[INFO] 
[INFO] --- tools:1.4:verify-legal-files (verify-legal-files) @ fortress-core ---
```

##### B. jmeter error: Error in NonGUIDriver null

The current version of jmeter v5.6 requires 

```xml
    <!-- Required for jmeter runtime. Fixes: Error in NonGUIDriver java.lang.NullPointerException -->
    <dependency>
      <groupId>org.apache.jmeter</groupId>
      <artifactId>jorphan</artifactId>
      <version>${version.jmeter.java}</version>
      <scope>test</scope>
    </dependency>
```

Sample error

```bash
[INFO] Executing test: ftAddUser.jmx
[INFO] Arguments for forked JMeter JVM: [java, -Xms1024M, -Xmx1024M, -Djava.awt.headless=true, -jar, ApacheJMeter-5.5.jar, -d, /home/smckinn/GIT/fortressDev/directory-fortress-core/target/dceff4c0-ff24-4e9c-ad4a-7db38b276b52/jmeter, -j, /home/smckinn/GIT/fortressDev/directory-fortress-core/target/jmeter/logs/ftAddUser.jmx.log, -l, /home/smckinn/GIT/fortressDev/directory-fortress-core/target/jmeter/results/20230702-ftAddUser.csv, -n, -t, /home/smckinn/GIT/fortressDev/directory-fortress-core/target/jmeter/testFiles/ftAddUser.jmx, -Dsun.net.http.allowRestrictedHeaders, true, -Dqualifier, A10, -Dversion, 3.0.0-SNAPSHOT]
[INFO]  
[INFO] Error in NonGUIDriver java.lang.NullPointerException
[INFO] An error occurred: Error in NonGUIDriver null
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  12.799 s
[INFO] Finished at: 2023-07-02T14:04:09-05:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal com.lazerycode.jmeter:jmeter-maven-plugin:3.7.0:jmeter (jmeter-tests) on project fortress-core: Test failed with exit code:1 -> [Help 1]
[ERROR] 
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR] 
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoExecutionException
[INFO] Shutdown detected, destroying JMeter process...
```
____________________________________________________________________________________

##### C. jmeter tests hang

Observation: 
When running tests that consume lots of memory, e.g. using many threads and/or enabling REST, the process hangs.
Jmeter and log4j logs stop outputing. The machine's otherwise unaffected. LDAP and/or HTTP servers are responsive.
Root Cause:
Most likely jmeter has run out of memory. Unfortunately, when this happens, 
an error is not reported. The process hangs and stops responding.

Remedy:
Edit the project's pom.mxl. Goto the loadtest profile. Increase the memory allocation, e.g. 2048 (2GB):
```xml
<profile>
   <id>loadtest</id>
   <build>
    ...
      <jMeterProcessJVMSettings>
         <xms>2048</xms>
         <xmx>2048</xmx>
      </jMeterProcessJVMSettings>
    ...
   </build>
</profile>
```
 
#### END OF README
