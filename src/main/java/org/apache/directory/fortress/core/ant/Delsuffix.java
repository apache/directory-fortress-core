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

import org.apache.directory.fortress.core.model.Suffix;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to create new {@link org.apache.directory.fortress.core.model.Suffix} used to 
 * drive {@link org.apache.directory.fortress.core.impl.SuffixP#delete(org.apache.directory.fortress.core.model.Suffix)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml 
 * tag used by load utility.
 * <p>
 * This class name, 'Delsuffix', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <delsuffix>
 *   ...
 * </delsuffix>
 * }
 * </pre>
 * <p style="font-size:2em; color:red;">
 * This class is destructive as it will remove all nodes below the suffix using recursive delete function.<BR>
 * Extreme care should be taken during execution to ensure target dn is correct and permanent removal of data is intended. 
 * There is no 'undo' for this operation.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Delsuffix
{
    final private List<Suffix> suffixes = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Delsuffix()
    {
    }

    /**
     * This method name, 'addSuffix', is used for derived xml tag 'suffix' in the load script.
     * <pre>
     * {@code
     * <target name="all">
     *     <FortressAdmin>
     *         <delsuffix>
     *           ...
     *         </delsuffix>
     *     </FortressAdmin>
     * </target>
     * }
     * </pre>
     * <p style="font-size:2em; color:red;">
     * This method is destructive and will remove all nodes below.<BR>
     * Extreme care should be taken during execution to ensure target dn is correct and permanent removal of data is intended.  There is no
     * 'undo' for this operation.
     *
     * @param suffix contains reference to data element targeted for removal..
     */
    public void addSuffix( Suffix suffix)
    {
        this.suffixes.add(suffix);
    }

    /**
     * Used by {@link FortressAntTask#deleteSuffixes()} to retrieve list of Suffixes as defined in input xml file.
     *
     * @return collection containing {@link org.apache.directory.fortress.core.model.Suffix}s targeted for removal.
     */
    public List<Suffix> getSuffixes()
    {
        return this.suffixes;
    }
}

