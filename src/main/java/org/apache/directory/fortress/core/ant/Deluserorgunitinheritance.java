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

import org.apache.directory.fortress.core.model.Relationship;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link org.apache.directory.fortress.core.ant.FortressAntTask} to load {@link Relationship}s used to 
 * drive {@link org.apache.directory.fortress.core.DelAdminMgr#deleteInheritance(
 * org.apache.directory.fortress.core.model.OrgUnit, org.apache.directory.fortress.core.model.OrgUnit)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml 
 * tag used by load utility.
 * <p>
 * This class name, 'Deluserorgunitinheritance', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <deluserorgunitinheritance>
 *           ...
 *         </deluserorgunitinheritance>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Deluserorgunitinheritance
{
    final private List<Relationship> relationships = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Deluserorgunitinheritance()
    {
    }

    /**
     * This method name, 'addRelationship', is used for derived xml tag 'relationship' in the load script.
     * <pre>
     * {@code
     * <deluserorgunitinheritance>
     *     <relationship child="ou2" parent="ou1"/>
     *     <relationship child="ou3" parent="ou1"/>
     *     <relationship child="ou4" parent="ou1"/>
     * </deluserorgunitinheritance>
     * }
     * </pre>
     *
     * @param relationship contains reference to data element targeted for removal.
     */
    public void addRelationship(Relationship relationship)
    {
        this.relationships.add(relationship);
    }

    /**
     * Used by {@link org.apache.directory.fortress.core.ant.FortressAntTask#deleteUserOrgunitInheritances()} to retrieve 
     * list of User OrgUnit relationships as defined in input xml file.
     *
     * @return collection containing {@link Relationship}s targeted for removal.
     */
    public List<Relationship> getRelationships()
    {
        return this.relationships;
    }
}

