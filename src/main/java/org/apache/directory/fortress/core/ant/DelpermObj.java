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

import org.apache.directory.fortress.core.model.PermObj;


/**
 * The class is used by {@link FortressAntTask} to load {@link PermObj}s used to drive 
 * {@link org.apache.directory.fortress.core.AdminMgr#deletePermObj(org.apache.directory.fortress.core.model.PermObj)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml 
 * tag used by load utility.
 * <p>
 * This class name, 'DelpermObj', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <delpermobj>
 *           ...
 *         </delpermobj>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DelpermObj
{
    final private List<PermObj> permObjs = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public DelpermObj()
    {
    }

    /**
     * This method name, 'delPermObj', is used for derived xml tag 'permobj' in the load script.
     * <pre>
     * {@code
     * <delpermobj>
     *     <permobj objName="/jsp/cal/cal1.jsp"/>
     *     <permobj objName="/jsp/cal/cal2.jsp"/>
     * </delpermobj>
     * }
     * </pre>
     *
     * @param permObj contains reference to data element targeted for insertion..
     */
    public void addPermObj(PermObj permObj)
    {
        this.permObjs.add(permObj);
    }

    /**
     * Used by {@link FortressAntTask#addPermObjs()} to retrieve list of PermObjs as defined in input xml file.
     *
     * @return collection containing {@link org.apache.directory.fortress.core.model.PermObj}s targeted for deletion.
     */
    public List<PermObj> getObjs()
    {
        return this.permObjs;
    }
}

