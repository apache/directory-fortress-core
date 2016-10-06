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


import org.apache.directory.fortress.core.model.Group;
import org.apache.directory.fortress.core.model.Group.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class GroupTestData
{
    public static final Group TEST_GROUP1 =
            createGroup
            (
                    "TestGroup1",
                    Type.ROLE,
                    "protocol1",
                    false,
                    roleNames( RoleTestData.ROLES_TR1 )
            );

    public static final Group TEST_GROUP2 =
            createGroup
            (
                    "TestGroup2",
                    Type.ROLE,
                    "protocol2",
                    false,
                    roleNames( RoleTestData.ROLES_TR2 )
            );

    public static final Group TEST_GROUP3 =
            createGroup
            (
                    "TestGroup3",
                    Type.USER,
                    "protocol3",
                    false,
                    userNames( UserTestData.USERS_TU1 )
            );

    private static List<String> roleNames(String[][] rolesArray )
    {
        List<String> names = new ArrayList<>();
        for (String[] role : rolesArray )
        {
            names.add( RoleTestData.getName( role ) );
        }
        return names;
    }

    private static List<String> userNames(String[][] userArray )
    {
        List<String> names = new ArrayList<>();
        for (String[] user : userArray )
        {
            names.add( UserTestData.getUserId( user ) );
        }
        return names;
    }

    private static Group createGroup(String name, Type type, String protocol, boolean memberDn, List<String> memberNames)
    {
        Group group = new Group();
        group.setName( name );
        group.setProtocol( protocol );
        group.setMemberDn( memberDn );
        group.setType( type );
        group.setMembers( memberNames );
        return group;
    }
}