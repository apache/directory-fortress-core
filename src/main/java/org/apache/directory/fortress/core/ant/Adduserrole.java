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

import org.apache.directory.fortress.core.model.UserRole;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to load {@link org.apache.directory.fortress.core.model.UserRole}s used to 
 * drive {@link org.apache.directory.fortress.core.AdminMgr#assignUser(org.apache.directory.fortress.core.model.UserRole)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml 
 * tag used by load utility.
 * <p>
 * This class name, 'Adduserrole', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <adduserrole>
 *           ...
 *         </adduserrole>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Adduserrole
{
    final private List<UserRole> userroles = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Adduserrole()
    {
    }

    /**
     * This method name, 'addUserRole', is used for derived xml tag 'userrole' in the load script.
     * <pre>
     * {@code
     * <adduserrole>
     *     <userrole userId="demoUser1" name="role1" beginTime="0800" endTime="0700" beginDate="20110101" endDate="none" 
     *         beginLockDate="none" endLockDate="none" dayMask="23456" timeout="30"/>
     *     <!-- Bad - role end time -->
     *     <userrole userId="demoUser5" name="role1"  beginTime="0700" endTime="0800" beginDate="20100101" endDate="21000101" 
     *         beginLockDate="none" endLockDate="none" dayMask="all" timeout="0"/>
     *     <!-- Bad - role  begin date -->
     *     <userrole userId="demoUser7" name="role1"  beginTime="0000" endTime="0000" beginDate="20110110" endDate="21000101" 
     *         beginLockDate="none" endLockDate="none" dayMask="all" timeout="0"/>
     *     <!-- Bad - role  end date -->
     *     <userrole userId="demoUser9" name="role1"  beginTime="0000" endTime="0000" beginDate="20100101" endDate="20100608" 
     *         beginLockDate="none" endLockDate="none" dayMask="all" timeout="0"/>
     * </adduserrole>
     * }
     * </pre>
     *
     * @param userRole contains reference to data element targeted for insertion..
     */
    public void addUserRole(UserRole userRole)
    {
        this.userroles.add(userRole);
    }

    /**
     * Used by {@link FortressAntTask#addUserRoles()} to retrieve list of UserRoles as defined in input xml file.
     *
     * @return collection containing {@link org.apache.directory.fortress.core.model.UserRole}s targeted for insertion.
     */
    public List<UserRole> getUserRoles()
    {
        return this.userroles;
    }
}

