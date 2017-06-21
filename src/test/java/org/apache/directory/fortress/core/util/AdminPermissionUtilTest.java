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
package org.apache.directory.fortress.core.util;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

public class AdminPermissionUtilTest
{

    @Test
    public void getPossibleAdminPermissions(){
        Map<String, List<String>> operations = AdminPermissionUtil.getPossibleAdminOperations();
        
        assertNotNull( operations );
        assertTrue(operations.containsKey( "org.apache.directory.fortress.core.impl.AccessMgrImpl" ));
        assertTrue(operations.get( "org.apache.directory.fortress.core.impl.AccessMgrImpl" ).size() > 0);
        
        assertTrue(operations.containsKey( "org.apache.directory.fortress.core.impl.AdminMgrImpl" ));
        assertTrue(operations.get( "org.apache.directory.fortress.core.impl.AdminMgrImpl" ).size() > 0);
        
        assertTrue(operations.containsKey( "org.apache.directory.fortress.core.impl.AuditMgrImpl" ));
        assertTrue(operations.get( "org.apache.directory.fortress.core.impl.AuditMgrImpl" ).size() > 0);
        
        assertTrue(operations.containsKey( "org.apache.directory.fortress.core.impl.DelAdminMgrImpl" ));
        assertTrue(operations.get( "org.apache.directory.fortress.core.impl.DelAdminMgrImpl" ).size() > 0);
        
        assertTrue(operations.containsKey( "org.apache.directory.fortress.core.impl.DelAccessMgrImpl" ));
        assertTrue(operations.get( "org.apache.directory.fortress.core.impl.DelAccessMgrImpl" ).size() > 0);
        
        assertTrue(operations.containsKey( "org.apache.directory.fortress.core.impl.DelReviewMgrImpl" ));
        assertTrue(operations.get( "org.apache.directory.fortress.core.impl.DelReviewMgrImpl" ).size() > 0);
        
        assertTrue(operations.containsKey( "org.apache.directory.fortress.core.impl.GroupMgrImpl" ));
        assertTrue(operations.get( "org.apache.directory.fortress.core.impl.GroupMgrImpl" ).size() > 0);
        
        assertTrue(operations.containsKey( "org.apache.directory.fortress.core.impl.PwPolicyMgrImpl" ));
        assertTrue(operations.get( "org.apache.directory.fortress.core.impl.PwPolicyMgrImpl" ).size() > 0);
        
        assertTrue(operations.containsKey( "org.apache.directory.fortress.core.impl.ReviewMgrImpl" ));
        assertTrue(operations.get( "org.apache.directory.fortress.core.impl.ReviewMgrImpl" ).size() > 0);
    }
    
}
