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

import org.apache.directory.fortress.core.model.Suffix;

/**
 * The class is used by {@link FortressAntTask} to create new {@link org.apache.directory.fortress.core.model.Suffix} used 
 * to drive {@link org.apache.directory.fortress.core.impl.SuffixP#add(org.apache.directory.fortress.core.model.Suffix)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag 
 * used by load utility.
 * <p>
 * This class name, 'Addsuffix', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addsuffix>
 *           ...
 *         </addsuffix>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Addsuffix

{
    final private List<Suffix> suffixes = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Addsuffix()
    {
    }

    /**
     * This method name, 'addSuffix', is used for derived xml tag 'suffix' in the load script.
     * <pre>
     * {@code
     * <addsuffix>
     *     <suffix name="jts" dc="com" description="Joshua Tree Software"/>
     * </addsuffix>
     * }
     * </pre>
     *
     * @param suffix contains reference to data element targeted for insertion..
     */
    public void addSuffix(Suffix suffix)
    {
        this.suffixes.add(suffix);
    }

    /**
     * Used by {@link FortressAntTask#addSuffixes()} to retrieve list of Suffixes as defined in input xml file.
     *
     * @return collection containing {@link org.apache.directory.fortress.core.model.Suffix}s targeted for insertion.
     */
    public List<Suffix> getSuffixes()
    {
        return this.suffixes;
    }
}

