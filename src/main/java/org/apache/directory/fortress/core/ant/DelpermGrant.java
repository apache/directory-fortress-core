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

import org.apache.directory.fortress.core.model.PermGrant;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to revoke {@link PermGrant}s used to drive 
 * {@link org.apache.directory.fortress.core.AdminMgr#revokePermission(org.apache.directory.fortress.core.model.Permission, 
 * org.apache.directory.fortress.core.model.Role)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml 
 * tag used by load utility.
 * <p>
 * This class name, 'DelpermGrant', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <delpermgrant>
 *           ...
 *         </delpermgrant>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DelpermGrant
{
    private List<PermGrant> permGrants = new ArrayList<>();


    /**
     * All Ant data entities must have a default constructor.
     */
    public DelpermGrant()
    {
    }


    /**
     * This method name, 'addPermGrant', is used for derived xml tag 'permgrant' in the load script.
     * <pre>
     * {@code
     * <delpermgrant>
     *     <permgrant objName="/jsp/cal/cal1.jsp" opName="main" roleNm="role1"/>
     *     <permgrant objName="/jsp/cal/cal2.jsp" opName="8am" roleNm="role1"/>
     *     <permgrant objName="/jsp/cal/cal2.jsp" opName="10am" roleNm="role1"/>
     *     <permgrant objName="/jsp/cal/cal2.jsp" opName="12pm" roleNm="role1"/>
     *     <permgrant objName="/jsp/cal/cal2.jsp" opName="2pm" roleNm="role1"/>
     *     <permgrant objName="/jsp/cal/cal2.jsp" opName="4pm" roleNm="role1"/>
     *     <permgrant objName="/jsp/cal/cal2.jsp" opName="6pm" roleNm="role1"/>
     * </delpermgrant>
     * }
     * </pre>
     *
     * @param permGrant contains reference to data element targeted for revocation.
     */
    public void addPermGrant(PermGrant permGrant)
    {
        this.permGrants.add(permGrant);
    }


    /**
     * Used by {@link FortressAntTask#addPermGrants()} to retrieve list of PermGrant as defined in input xml file.
     *
     * @return collection containing {@link PermGrant}s targeted for revocation.
     */
    public List<PermGrant> getPermGrants()
    {
        return this.permGrants;
    }
}

