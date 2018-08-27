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
package org.apache.directory.fortress.core.impl;


import junit.framework.TestCase;

/**
 * Description of the Class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class ConfigTestData extends TestCase
{
    public final static int REALM_COL = 0;
    public final static int NAME_COL = 1;
    public final static int VALUE_COL = 2;

    final public static String[][] ABAC_SAMPLE1 =
    {
        {
            "Default", /* REALM_COL */
            "Tellers", /* NAME_COL */
            "locale", /* VALUE_COL */
        },
        {
            "Default", /* CONFIG_REALM_COL */
            "Washers", /* NAME_COL */
            "locale", /* VALUE_COL */
        }
    };

}