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
 * The class is used by {@link FortressAntTask} to remove 
 * {@link org.apache.directory.fortress.core.model.OrganizationalUnit}s  used to drive 
 * {@link org.apache.directory.fortress.core.impl.OrganizationalUnitP#delete(
 * org.apache.directory.fortress.core.model.OrganizationalUnit)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml 
 * tag used by load utility.
 * <p>
 * This class name, 'Delcontainer', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <delcontainer>
 *           ...
 *         </delcontainer>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 * <p style="font-size:2em; color:red;">
 * This class is destructive as it will remove all nodes below the container using recursive delete function.<BR>
 * Extreme care should be taken during execution to ensure target dn is correct and permanent removal of data is intended.  There is no
 * 'undo' for this operation.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Delcontainer
{
    final private List<OrganizationalUnit> containers = new ArrayList<>();


    /**
     * All Ant data entities must have a default constructor.
     */
    public Delcontainer()
    {
    }


    /**
     * This method name, 'addContainer', is used for derived xml tag 'container' in the load script.
     * <pre>
     * {@code
     * <delcontainer>
     *     <container name="Config"/>
     *     <container name="People"/>
     *     <container name="Policies"/>
     *     <container name="RBAC"/>
     *     <container name="Roles" parent="RBAC"/>
     *     <container name="Permissions" parent="RBAC"/>
     *     <container name="Constraints" parent="RBAC"/>
     *     <container name="ARBAC"/>
     *     <container name="OS-U" parent="ARBAC"/>
     *     <container name="OS-P" parent="ARBAC"/>
     *     <container name="AdminRoles" parent="ARBAC"/>
     *     <container name="AdminPerms" parent="ARBAC"/>
     * </delcontainer>
     * }
     * </pre>
     *
     * @param ou contains reference to data element targeted for deletion.
     */
    public void addContainer( OrganizationalUnit ou)
    {
        this.containers.add(ou);
    }


    /**
     * Used by {@link FortressAntTask#deleteContainers()} to retrieve list of OrganizationalUnits as defined in input xml file.
     *
     * @return collection containing {@link org.apache.directory.fortress.core.model.OrganizationalUnit}s targeted for removal.
     */
    public List<OrganizationalUnit> getContainers()
    {
        return this.containers;
    }
}

