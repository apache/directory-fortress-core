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


import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.fortress.core.GlobalIds;

public class EIds
{
    public static final String EXAMPLE_ADMIN_IMPLEMENTATION = "exampleAdminImplementation";
    public static final String EXAMPLE_ADMIN_DEFAULT_CLASS = "ExampleAdminMgrImpl";

    // place any global variables related to example entity here
    public static final String EXAMPLE_ROOT = "example.root";

    public static final int EXAMPLE_LEN = 40;

    public static final String EXAMPLE_NM = "oamExampleName";
    public static final String EXAMPLE_OBJECT_CLASS_NM = "oamExamples";

    public static final String EXAMPLE_OBJ_CLASS[] = {
            SchemaConstants.TOP_OC, EXAMPLE_OBJECT_CLASS_NM, GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME
//            GlobalIds.TOP, EXAMPLE_OBJECT_CLASS_NM, GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME, GlobalIds.TEMPORAL_AUX_OBJECT_CLASS_NAME
    };

    public static final int BATCH_SIZE = 100;
}

