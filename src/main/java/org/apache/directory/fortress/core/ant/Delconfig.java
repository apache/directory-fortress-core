/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core.ant;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to load {@link org.apache.directory.fortress.core.ant.ConfigAnt}s used to 
 * drive {@link org.apache.directory.fortress.core.ConfigMgr#delete(String, java.util.Properties)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml 
 * tag used by load utility.
 * <p>
 * This class name, 'Delconfig', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <delconfig>
 *           ...
 *         </delconfig>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Delconfig
{
    final private List<ConfigAnt> config = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Delconfig()
    {
    }

    /**
     * This method name, 'addConfig', is used for derived xml tag 'config' in the load script.
     * <pre>
     * {@code
     * <delconfig>
     *     <config props="config.realm:DEFAULT"/>
     *     <config props="enable.audit:true"/>
     *     <config props="authn.type:default"/>
     *     <config props="password.policy:openldap"/>
     *     <config props="clientside.sorting:true"/>
     *     <config props="suffix:dc=jts\,dc=com"/>
     *     <config props="user.root:ou=People\,dc=jts\,dc=com"/>
     *     <config props="pwpolicy.root:ou=Policies\,dc=jts\,dc=com"/>
     *     <config props="role.root:ou=Roles\,ou=RBAC\,dc=jts\,dc=com"/>
     *     <config props="perm.root:ou=Permissions\,ou=RBAC\,dc=jts\,dc=com"/>
     *     <config props="sdconstraint.root:ou=Constraints\,ou=RBAC\,dc=jts\,dc=com"/>
     *     <config props="userou.root:ou=OS-U\,ou=ARBAC\,dc=jts\,dc=com"/>
     *     <config props="permou.root:ou=OS-P\,ou=ARBAC\,dc=jts\,dc=com"/>
     *     <config props="adminrole.root:ou=AdminRoles\,ou=ARBAC\,dc=jts\,dc=com"/>
     *     <config props="adminperm.root:ou=AdminPerms\,ou=ARBAC\,dc=jts\,dc=com"/>
     *     ...
     * </delconfig>
     * }
     * </pre>
     *
     * @param config contains reference to data element targeted for removal..
     */
    public void addConfig(ConfigAnt config)
    {
        this.config.add(config);
    }

    /**
     * Used by {@link FortressAntTask#deleteConfig()} to retrieve list of properties as defined in input xml file.
     *
     * @return collection containing {@link ConfigAnt}s targeted for removal.
     */
    public List<ConfigAnt> getConfig()
    {
        return this.config;
    }
}

