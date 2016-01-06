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
 * The class is used by {@link FortressAntTask} to create new {@link SDSetAnt}s used to drive 
 * {@link org.apache.directory.fortress.core.AdminMgr#createSsdSet(org.apache.directory.fortress.core.model.SDSet)} or 
 * {@link org.apache.directory.fortress.core.AdminMgr#createDsdSet(org.apache.directory.fortress.core.model.SDSet)}.
 *
 *
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml 
 * tag used by load utility.
 * <p>
 * This class name, 'Addsdset', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addsdset>
 *           ...
 *         </addsdset>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Addsdset
{
    final private List<SDSetAnt> sds = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Addsdset()
    {
    }

    /**
     * This method name, 'addSdset', is used for derived xml tag 'sdset' in the load script.
     * <pre>
     * {@code
     * <addsdset>
     *     <sdset name="DemoSSD1"
     *         description="Demo static separation of duties"
     *         cardinality="2"
     *         settype="STATIC"
     *         setmembers="role1,role2"/>
     *     <sdset name="DemoDSD1"
     *         description="Demo dynamic separation of duties"
     *         cardinality="2"
     *         settype="DYNAMIC"
     *         setmembers="role1,role3"/>
     * </addsdset>
     * }
     * </pre>
     *
     * @param sd contains reference to data element targeted for insertion..
     */
    public void addSdset(SDSetAnt sd)
    {
        this.sds.add(sd);
    }

    /**
     * Used by {@link FortressAntTask#addSdsets()} to retrieve list of SDSetAnt as defined in input xml file.
     *
     * @return collection containing {@link SDSetAnt}s targeted for insertion.
     */
    public List<SDSetAnt> getSdset()
    {
        return this.sds;
    }
}

