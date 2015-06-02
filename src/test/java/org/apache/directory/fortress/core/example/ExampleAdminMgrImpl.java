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
package org.apache.directory.fortress.core.example;

import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * Object provides example of Fortress API.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @created December 26, 2010
 */
public class ExampleAdminMgrImpl implements ExampleAdminMgr
{
    private static final String CLS_NM = ExampleAdminMgrImpl.class.getName();
    private static final ExampleP examP = new ExampleP();

    /**
     * @param example
     * @return
     * @throws org.apache.directory.fortress.core.SecurityException
     */
    public Example addExample(Example example)
        throws SecurityException
    {
        VUtil.assertNotNull( example, EErrIds.EXAMPLE_ID_NULL, ".addExample" );
        return examP.add(example);
    }

    /**
     * @param example
     * @throws org.apache.directory.fortress.core.SecurityException
     */
    public void delExample(Example example)
        throws SecurityException
    {
        VUtil.assertNotNull( example, EErrIds.EXAMPLE_ID_NULL, ".addExample" );
        examP.delete(example.getName());
    }
}

