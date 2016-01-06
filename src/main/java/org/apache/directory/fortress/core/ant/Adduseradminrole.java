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

import org.apache.directory.fortress.core.model.UserAdminRole;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link org.apache.directory.fortress.core.ant.FortressAntTask} to load 
 * {@link org.apache.directory.fortress.core.model.UserAdminRole}s used to drive 
 * {@link org.apache.directory.fortress.core.DelAdminMgr#assignUser(org.apache.directory.fortress.core.model.UserAdminRole)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml 
 * tag used by load utility.
 * <p>
 * This class name, 'Adduseradminrole', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <adduseradminrole>
 *           ...
 *         </adduseradminrole>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Adduseradminrole
{
    final private List<UserAdminRole> userroles = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Adduseradminrole()
    {
    }

    /**
     * This method name, 'addUserRole', is used for derived xml tag 'userrole' in the load script.
     * <pre>
     * {@code
     * <adduseradminrole>
     *     <userrole userId="demouser4"
     *         name="DemoAdminUsers"
     *         beginTime="0800"
     *         endTime="1700"
     *         beginDate="20110101"
     *         endDate="none"
     *         beginLockDate="none"
     *         endLockDate="none"
     *         dayMask="23456"
     *         timeout="15"/>
     * </adduseradminrole>
     * }
     * </pre>
     *
     * @param userRole contains reference to data element targeted for insertion..
     */
    public void addUserRole(UserAdminRole userRole)
    {
        this.userroles.add(userRole);
    }

    /**
     * Used by {@link org.apache.directory.fortress.core.ant.FortressAntTask#addUserAdminRoles()} to retrieve list of 
     * UserAdminRoles as defined in input xml file.
     *
     * @return collection containing {@link org.apache.directory.fortress.core.model.UserAdminRole}s targeted for insertion.
     */
    public List<UserAdminRole> getUserRoles()
    {
        return this.userroles;
    }
}

