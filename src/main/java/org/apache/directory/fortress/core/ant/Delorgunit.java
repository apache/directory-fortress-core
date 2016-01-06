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
 * The class is used by {@link FortressAntTask} to delete {@link org.apache.directory.fortress.core.model.OrgUnit}s used to 
 * drive {@link org.apache.directory.fortress.core.DelAdminMgr#delete(org.apache.directory.fortress.core.model.OrgUnit)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml 
 * tag used by load utility.
 * <p>
 * This class name, 'Delorgunit', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <delorgunit>
 *           ...
 *         </delorgunit>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Delorgunit
{
    final private List<OrgUnitAnt> ous = new ArrayList<>();


    /**
     * All Ant data entities must have a default constructor.
     */
    public Delorgunit()
    {
    }


    /**
     * This method name, 'addOrgUnit', is used for derived xml tag 'orgunit' in the load script.
     * <pre>
     * {@code
     * <delorgunit>
     *     <orgunit name="demousrs1" typeName="USER"/>
     *     <orgunit name="demousrs2" typename="USER"/>
     *     <orgunit name="demoapps1" typeName="PERM"/>
     *     <orgunit name="demoapps2" typename="PERM"/>
     * </delorgunit>
     * }
     * </pre>
     *
     * @param ou contains reference to data element targeted for deletion..
     */
    public void addOrgUnit(OrgUnitAnt ou)
    {
        this.ous.add(ou);
    }


    /**
     * Used by {@link FortressAntTask#addOrgunits()} to retrieve list of OrgUnits as defined in input xml file.
     *
     * @return collection containing {@link org.apache.directory.fortress.core.model.OrgUnit}s targeted for removal.
     */
    public List<OrgUnitAnt> getOrgUnits()
    {
        return this.ous;
    }
}