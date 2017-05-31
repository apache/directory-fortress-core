
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
