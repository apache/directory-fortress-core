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
 * {@link org.apache.directory.fortress.core.AdminMgr#deleteSsdSet(org.apache.directory.fortress.core.model.SDSet)} or 
 * {@link org.apache.directory.fortress.core.AdminMgr#deleteDsdSet(org.apache.directory.fortress.core.model.SDSet)}.
 *
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml 
 * tag used by load utility.
 * <p>
 * This class name, 'Delsdset', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <delsdset>
 *           ...
 *         </delsdset>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Delsdset
{
    final private List<SDSetAnt> sds = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Delsdset()
    {
    }

    /**
     * This method name, 'addSdset', is used for derived xml tag 'sdset' in the load script.
     * <pre>
     * {@code
     * <delsdset>
     *     <sdset name="DemoSSD1" settype="STATIC" />
     *     <sdset name="DemoDSD1" settype="DYNAMIC" />
     * </delsdset>
     * }
     * </pre>
     *
     * @param sd contains reference to data element targeted for removal.
     */
    public void addSdset(SDSetAnt sd)
    {
        this.sds.add(sd);
    }

    /**
     * Used by {@link FortressAntTask#deleteSdsets()} to retrieve list of SDSetAnt as defined in input xml file.
     *
     * @return collection containing {@link SDSetAnt}s targeted for removal.
     */
    public List<SDSetAnt> getSdset()
    {
        return this.sds;
    }
}

