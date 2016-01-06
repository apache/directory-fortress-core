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
 * The class is used by {@link FortressAntTask} to load {@link org.apache.directory.fortress.core.ant.PermAnt}s used to drive 
 * {@link org.apache.directory.fortress.core.AdminMgr#deletePermission(org.apache.directory.fortress.core.model.Permission)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml 
 * tag used by load utility.
 * <p>
 * This class name, 'DelpermOp', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <delpermop>
 *           ...
 *         </delpermop>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DelpermOp
{
    final private List<PermAnt> permissions = new ArrayList<>();


    /**
     * All Ant data entities must have a default constructor.
     */
    public DelpermOp()
    {
    }


    /**
     * This method name, 'addPermOp', is used for derived xml tag 'permop' in the load script.
     * <pre>
     * {@code
     * <delpermop>
     *     <permop opName="main" objName="/jsp/cal/cal1.jsp"/>
     *     <permop opName="8am" objName="/jsp/cal/cal2.jsp"/>
     *     <permop opName="9am" objName="/jsp/cal/cal2.jsp"/>
     *     <permop opName="10am" objName="/jsp/cal/cal2.jsp"/>
     *     <permop opName="11am" objName="/jsp/cal/cal2.jsp"/>
     *     <permop opName="12pm" objName="/jsp/cal/cal2.jsp"/>
     *     <permop opName="1pm" objName="/jsp/cal/cal2.jsp"/>
     *     <permop opName="2pm" objName="/jsp/cal/cal2.jsp"/>
     *     <permop opName="3pm" objName="/jsp/cal/cal2.jsp"/>
     *     <permop opName="4pm" objName="/jsp/cal/cal2.jsp"/>
     *     <permop opName="5pm" objName="/jsp/cal/cal2.jsp"/>
     *     <permop opName="6pm" objName="/jsp/cal/cal2.jsp"/>
     * </delpermop>
     * }
     * </pre>
     *
     * @param permission contains reference to data element targeted for removal..
     */
    public void addPermOp(PermAnt permission)
    {
        this.permissions.add(permission);
    }


    /**
     * Used by {@link FortressAntTask#addPermOps()} to retrieve list of Permissions as defined in input xml file.
     *
     * @return collection containing {@link PermAnt}s targeted for removal.
     */
    public List<PermAnt> getPermOps()
    {
        return this.permissions;
    }
}

