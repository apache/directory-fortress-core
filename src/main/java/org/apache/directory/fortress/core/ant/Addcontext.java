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

import org.apache.directory.fortress.core.model.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to create new {@link Context} used to define multi-tenant property.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml 
 * tag used by load utility.
 * <p>
 * This class name, 'Addcontext', is used for the xml tag in the load script.
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addcontext>
 *           ...
 *         </addcontext>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Addcontext

{
    final private List<Context> contexts = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Addcontext()
    {
    }

    /**
     * This method name, 'addContext', is used for derived xml tag 'context' in the load script.
     * <pre>
     * {@code
     * <addcontext>
     *     <context name="123"/>
     * </addsuffix>
     * }
     * </pre>
     *
     * @param context contains reference to data element targeted for insertion..
     */
    public void addContext(Context context)
    {
        this.contexts.add(context);
    }

    /**
     * Used by {@link Context} to retrieve list of contexts as defined in input xml file.
     *
     * @return List of context names.
     */
    public List<Context> getContexts()
    {
        return this.contexts;
    }
}

