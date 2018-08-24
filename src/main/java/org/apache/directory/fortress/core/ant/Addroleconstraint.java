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

import org.apache.directory.fortress.core.model.RoleConstraint;
import org.apache.directory.fortress.core.model.UserRole;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to load {@link org.apache.directory.fortress.core.model.RoleConstraint}s used to
 * drive {@link org.apache.directory.fortress.core.AdminMgr#addRoleConstraint( UserRole uRole, RoleConstraint roleConstraint )}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml
 * tag used by load utility.
 * <p>
 * This class name, 'Addroleconstraint', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addroleconstraint>
 *           ...
 *         </addroleconstraint>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Addroleconstraint
{
    final private List<RoleConstraintAnt> roleconstraints = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Addroleconstraint()
    {
    }

    /**
     * This method name, 'Addroleconstraint', is used for derived xml tag 'roleconstraint' in the load script.
     * <pre>
     * {@code
     * <addroleconstraint>
     *      <roleconstraint userId="curly" role="Tellers" key="locale" value="east" typeName="USER"/>
     * </addroleconstraint>
     * }
     * </pre>
     *
     * @param roleConstraint contains reference to data element targeted for insertion..
     */
    public void addRoleConstraint(RoleConstraintAnt roleConstraint)
    {
        this.roleconstraints.add(roleConstraint);
    }

    /**
     * Used by {@link FortressAntTask#addUserRoles()} to retrieve list of roleconstraints as defined in input xml file.
     *
     * @return collection containing {@link org.apache.directory.fortress.core.model.RoleConstraint}s targeted for insertion.
     */
    public List<RoleConstraintAnt> getRoleConstraints()
    {
        return this.roleconstraints;
    }
}

