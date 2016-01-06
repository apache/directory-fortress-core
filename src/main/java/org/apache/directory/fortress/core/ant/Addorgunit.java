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
 * The class is used by {@link FortressAntTask} to create new {@link org.apache.directory.fortress.core.model.OrgUnit}s used 
 * to drive {@link org.apache.directory.fortress.core.DelAdminMgr#add(org.apache.directory.fortress.core.model.OrgUnit)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml 
 * tag used by load utility.
 * <p>
 * This class name, 'Addorgunit', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addorgunit>
 *           ...
 *         </addorgunit>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Addorgunit
{
    final private List<OrgUnitAnt> ous = new ArrayList<>();


    /**
     * All Ant data entities must have a default constructor.
     */
    public Addorgunit()
    {
    }


    /**
     * This method name, 'addOrgUnit', is used for derived xml tag 'orgunit' in the load script.
     * <pre>
     * {@code
     * <addorgunit>
     *     <orgunit name="demousrs1" typeName="USER" description="Test User Org 1 for User on Tomcat Calendar App"/>
     *     <orgunit name="demousrs2" typename="USER" description="Test User Org 2 for User on Tomcat  Calendar App"/>
     *     <orgunit name="demoapps1" typeName="PERM" description="Test Perm Org 1 for Permission on Tomcat Calendar App"/>
     *     <orgunit name="demoapps2" typename="PERM" description="Test Perm Org 2 for Permission on Tomcat Calendar App"/>
     * </addorgunit>
     * }
     * </pre>
     *
     * @param ou contains reference to data element targeted for insertion..
     */
    public void addOrgUnit(OrgUnitAnt ou)
    {
        this.ous.add(ou);
    }


    /**
     * Used by {@link FortressAntTask#addOrgunits()} to retrieve list of OrgUnits as defined in input xml file.
     *
     * @return collection containing {@link org.apache.directory.fortress.core.model.OrgUnit}s targeted for insertion.
     */
    public List<OrgUnitAnt> getOrgUnits()
    {
        return this.ous;
    }
}

