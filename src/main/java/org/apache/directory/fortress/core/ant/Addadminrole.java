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
 * The class is used by {@link FortressAntTask} to load {@link org.apache.directory.fortress.core.model.AdminRole}s used to 
 * drive {@link org.apache.directory.fortress.core.DelAdminMgr#addRole(org.apache.directory.fortress.core.model.AdminRole)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag 
 * used by load utility.
 * <p>
 * This class name, 'Addadminrole', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addadminrole>
 *           ...
 *         </addadminrole>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Addadminrole
{
    final private List<AdminRoleAnt> roles = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Addadminrole()
    {
    }

    /**
     * This method name, 'addRole', is used for derived xml tag 'role' in the load script.
     * <pre>
     * {@code
     * <addadminrole>
     *      <role name="DemoAdminUsers"
     *          description="Test Admin Role for Demo"
     *          osps="demoapps1,demoapps2"
     *          osus="demousrs1,demousrs2"
     *          beginrange="role1"
     *          endrange="role1"
     *          begininclusive="true"
     *          endinclusive="true"/>
     * </addadminrole>
     * }
     * </pre>
     *
     * @param role contains extension of {@link org.apache.directory.fortress.core.model.AdminRole}.
     */
    public void addRole(AdminRoleAnt role)
    {
        this.roles.add(role);
    }

    /**
     * Used by {@link FortressAntTask#addAdminRoles()} to retrieve list of AdminRoles as defined in input xml file.
     *
     * @return collection containing {@link AdminRoleAnt}s targeted for insertion.
     */
    public List<AdminRoleAnt> getRoles()
    {
        return this.roles;
    }
}

