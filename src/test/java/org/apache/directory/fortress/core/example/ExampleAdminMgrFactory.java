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
import org.apache.directory.fortress.core.util.ClassUtil;
import org.apache.directory.fortress.core.util.Config;

/**
 * Factory class used to instantiate the ExampleAdminMgrImpl.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @created December 26, 2010
 */
public class ExampleAdminMgrFactory
{
    private static String exampleAdminClassName = Config.getInstance().getProperty( EIds.EXAMPLE_ADMIN_IMPLEMENTATION );


    public static ExampleAdminMgr createInstance()
        throws SecurityException
    {
        ExampleAdminMgr adminMgr;
        if (exampleAdminClassName == null || exampleAdminClassName.compareTo("") == 0)
        {
            exampleAdminClassName = EIds.EXAMPLE_ADMIN_DEFAULT_CLASS;
        }
        adminMgr = (ExampleAdminMgr) ClassUtil.createInstance(exampleAdminClassName);
        return adminMgr;
    }
}

