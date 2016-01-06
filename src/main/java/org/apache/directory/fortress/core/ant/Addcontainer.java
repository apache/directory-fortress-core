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

import org.apache.directory.fortress.core.model.OrganizationalUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to create new 
 * {@link org.apache.directory.fortress.core.model.OrganizationalUnit}s used to drive 
 * {@link org.apache.directory.fortress.core.impl.OrganizationalUnitP#add(
 *      org.apache.directory.fortress.core.model.OrganizationalUnit)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag 
 * used by load utility.
 * <p>
 * This class name, 'Addcontainer', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addcontainer>
 *           ...
 *         </addcontainer>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Addcontainer

{
    final private List<OrganizationalUnit> containers = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Addcontainer()
    {
    }

    /**
     * This method name, 'addContainer', is used for derived xml tag 'container' in the load script.
     * <pre>
     * {@code
     * <addcontainer>
     *     <container name="Config" description="Fortress Configuration Realms"/>
     *     <container name="People" description="Fortress People"/>
     *     <container name="Policies" description="Fortress Policies"/>
     *     <container name="RBAC" description="Fortress RBAC Policies"/>
     *     <container name="Roles" parent="RBAC" description="Fortress Roles"/>
     *     <container name="Permissions" parent="RBAC" description="Fortress Permissions"/>
     *     <container name="Constraints" parent="RBAC" description="Fortress Separation of Duty Constraints"/>
     *     <container name="ARBAC" description="Fortress Administrative RBAC Policies"/>
     *     <container name="OS-U" parent="ARBAC" description="Fortress User Organizational Units"/>
     *     <container name="OS-P" parent="ARBAC" description="Fortress Perm Organizational Units"/>
     *     <container name="AdminRoles" parent="ARBAC" description="Fortress AdminRoles"/>
     *     <container name="AdminPerms" parent="ARBAC" description="Fortress Admin Permissions"/>
     * </addcontainer>
     * }
     * </pre>
     *
     * @param ou contains reference to data element targeted for insertion..
     */
    public void addContainer( OrganizationalUnit ou)
    {
        this.containers.add(ou);
    }

    /**
     * Used by {@link FortressAntTask#addContainers()} to retrieve list of OrganizationalUnits as defined in input xml file.
     * @return collection containing {@link org.apache.directory.fortress.core.model.OrganizationalUnit}s targeted for 
     * insertion.
     */
    public List<OrganizationalUnit> getContainers()
    {
        return this.containers;
    }
}

