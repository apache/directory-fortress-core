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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.directory.fortress.core.model.AuthZ;
import org.apache.directory.fortress.core.model.Bind;
import org.apache.directory.fortress.core.model.Mod;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserAudit;
import org.apache.directory.fortress.core.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.AuditMgr;
import org.apache.directory.fortress.core.AuditMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.util.LogUtil;


/**
 * AccessMgrImpl Tester.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AuditMgrImplTest extends TestCase
{
    private static final String CLS_NM = AuditMgrImplTest.class.getName();
    private static Session adminSess = null;
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    public AuditMgrImplTest( String name )
    {
        super( name );
    }


    public void setUp() throws Exception
    {
        super.setUp();
    }


    public void tearDown() throws Exception
    {
        super.tearDown();
    }


    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest(new AuditMgrImplTest("testSearchBinds"));
        //suite.addTest(new AuditMgrImplTest("testSearchAuthNInvalid"));
        //suite.addTest(new AuditMgrImplTest("testGetAuthZs"));
        //suite.addTest( new AuditMgrImplTest( "testSearchAuthZs" ) );
        //suite.addTest(new AuditMgrImplTest("testSearchMods"));
        //suite.addTest(new AuditMgrImplTest("testSearchAdminMods"));
        return suite;
    }


    /**
     *
     */
    public void testSearchAdminMods()
    {
        searchAdminMods( "SESS-USRS RBAC TU0", PermTestData.ADMINMGR_OBJ,
            PermTestData.ADMINMGR_OPS );
        searchAdminMods( "SESS-USRS ARBAC TU0", PermTestData.DELEGATEDMGR_OBJ,
            PermTestData.DELEGATEDMGR_OPS );
        searchAdminMods( "SESS-USRS PWPOLICY TU0", PermTestData.PSWDMGR_OBJ,
            PermTestData.PSWDMGR_OPS );
/*
        searchAdminMods( "SESS-USRS RBAC TU0", UserTestData.USERS_TU0, PermTestData.ADMINMGR_OBJ,
            PermTestData.ADMINMGR_OPS );
        searchAdminMods( "SESS-USRS ARBAC TU0", UserTestData.USERS_TU0, PermTestData.DELEGATEDMGR_OBJ,
            PermTestData.DELEGATEDMGR_OPS );
        searchAdminMods( "SESS-USRS PWPOLICY TU0", UserTestData.USERS_TU0, PermTestData.PSWDMGR_OBJ,
            PermTestData.PSWDMGR_OPS );
*/
    }

    private static Map disabled = loadAuditMap();


    private static Map<String, String> loadAuditMap()
    {
        disabled = new HashMap();
        disabled.put("AdminMgrImpl.updateSsdSet", null);
        disabled.put("AdminMgrImpl.updateDsdSet", null);
        // TODO: this should not be disabled, must place audit context into entry before it is auditable:
        disabled.put("AdminMgrImpl.enableRoleConstraint", null);
        disabled.put("AdminMgrImpl.disableRoleConstraint", null);
        //disabled.put("AdminMgrImpl.addPermissionAttributeSet", null);
        //disabled.put("AdminMgrImpl.addPermissionAttributeToSet", null);
        //disabled.put("AdminMgrImpl.deletePermissionAttributeSet", null);
        disabled.put("PwPolicyMgrImpl.search", null);
        disabled.put("PwPolicyMgrImpl.read", null);
        LOG.info( "loadAuditMap isFirstRun [" + FortressJUnitTest.isFirstRun() + "]" );
        if ( FortressJUnitTest.isFirstRun() )
        {
            disabled.put( "AdminMgrImpl.deleteRole", null );
            disabled.put( "AdminMgrImpl.deleteUser", null );
            disabled.put( "AdminMgrImpl.deassignUser", null );
            disabled.put( "AdminMgrImpl.deletePermission", null );
            disabled.put( "AdminMgrImpl.deletePermObj", null );
            disabled.put( "AdminMgrImpl.revokePermission", null );
            disabled.put( "AdminMgrImpl.revokePermissionUser", null );
            disabled.put( "AdminMgrImpl.deleteInheritance", null );
            disabled.put( "AdminMgrImpl.deleteSsdRoleMember", null );
            disabled.put( "AdminMgrImpl.deleteSsdSet", null );
            disabled.put( "AdminMgrImpl.deleteDsdRoleMember", null );
            disabled.put( "AdminMgrImpl.deleteDsdSet", null );
            disabled.put( "AdminMgrImpl.disableUser", null );
            disabled.put( "AdminMgrImpl.deletePermissionAttributeSet", null );
            disabled.put( "AdminMgrImpl.removePermissionAttributeFromSet", null );
            disabled.put( "DelAdminMgrImpl.deleteRole", null );
            disabled.put( "DelAdminMgrImpl.deassignUser", null );
            disabled.put( "DelAdminMgrImpl.deleteOU", null );
            disabled.put( "DelAdminMgrImpl.deleteInheritanceOU", null );
            disabled.put( "DelAdminMgrImpl.deleteInheritanceRole", null );
            disabled.put( "PwPolicyMgrImpl.deletePasswordPolicy", null );
            disabled.put( "PwPolicyMgrImpl.delete", null );
        }
        // Only for OpenLDAP and ApacheDS
        if ( !Config.getInstance().isOpenldap() && !Config.getInstance().isApacheds() )
        {
            LOG.info( "loadAuditMap Disable Password Policy" );
            disabled.put( "PswdPolicyMgrImpl.testMinAge", null );
            disabled.put( "PswdPolicyMgrImpl.testMaxAge", null );
            disabled.put( "PswdPolicyMgrImpl.testInHistory", null );
            disabled.put( "PswdPolicyMgrImpl.testMinLength", null );
            disabled.put( "PswdPolicyMgrImpl.testExpireWarning", null );
            disabled.put( "PswdPolicyMgrImpl.testGraceLoginLimit", null );
            disabled.put( "PswdPolicyMgrImpl.testMaxFailure", null );
            disabled.put( "PswdPolicyMgrImpl.testLockoutDuration", null );
            disabled.put( "PswdPolicyMgrImpl.testLockout", null );
            disabled.put( "PswdPolicyMgrImpl.testFailureCountInterval", null );
            disabled.put( "PswdPolicyMgrImpl.testMustChange", null );
            disabled.put( "PswdPolicyMgrImpl.testAllowUserChange", null );
            disabled.put( "PswdPolicyMgrImpl.testSafeModify", null );
        }
        return disabled;
    }


    private static boolean isAudit( String objName, String opName )
    {
        return !disabled.containsKey( objName + "." + opName );
    }


    /**
     *
     * @param msg
     */
    private static void searchAdminMods( String msg, String[][] oArray, String[][] opArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AuditMgr auditMgr = getManagedAuditMgr();
            User user = adminSess.getUser();
            // now search for successful session creation events:
            UserAudit uAudit = new UserAudit();
            uAudit.setUserId( user.getUserId() );
            for ( String[] obj : oArray )
            {
                String objName = AdminUtil.getObjName( PermTestData.getName( obj ) );
                uAudit.setObjName( objName );
                for ( String[] op : opArray )
                {
                    uAudit.setOpName( PermTestData.getName( op ) );
                    List<Mod> mods = auditMgr.searchAdminMods( uAudit );
                    assertNotNull( mods );

                    assertTrue(
                        CLS_NM + "searchAdminMods failed search for successful authentication user ["
                            + user.getUserId() + "] object [" + objName + "] operation ["
                            + PermTestData.getName( op ) + "]",
                        mods.size() > 0 || !isAudit( objName, PermTestData.getName( op ) ) );
                    boolean result = mods.size() > 0 || !isAudit( objName, PermTestData.getName( op ) );
                    LOG.debug( "searchAdminMods search user [" + user.getUserId() + "] object ["
                        + objName + "] operation [" + PermTestData.getName( op ) + "] result: " + result );
                    //System.out.println("searchAdminMods search user [" + user.getUserId() + "] object [" + objName + "] operation [" + PermTestData.getName(op) + "] result: " + result);
                }
            }
            LOG.debug( "searchAdminMods successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "searchAdminMods: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testSearchMods()
    {
        searchMods( "SESS-USRS TU1_UPD", UserTestData.USERS_TU1_UPD );
        searchMods( "SESS-USRS TU3", UserTestData.USERS_TU3 );
        searchMods( "SESS-USRS TU4", UserTestData.USERS_TU4 );
    }


    /**
     *
     * @param msg
     * @param uArray
     */
    private static void searchMods( String msg, String[][] uArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AuditMgr auditMgr = getManagedAuditMgr();
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                // now search for successful session creation events:
                UserAudit uAudit = new UserAudit();
                uAudit.setUserId( user.getUserId() );
                uAudit.setFailedOnly( false );
                List<Mod> mods = auditMgr.searchUserSessions( uAudit );
                assertNotNull( mods );
                assertTrue(
                    CLS_NM + "searchUserSessions failed search for successful authentication user [" + user.getUserId()
                        + "]", mods.size() > 0 );
            }
            LOG.debug( "searchUserSessions successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "searchUserSessions: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    public void testSearchAuthZs()
    {
        searchAuthZs( "GET-AUTHZ TU1_UPD", UserTestData.USERS_TU1_UPD, PermTestData.OBJS_TOB1, PermTestData.OPS_TOP1, false );
        searchAuthZs( "GET-AUTHZ TU3", UserTestData.USERS_TU3, PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, false );
        searchAuthZs( "GET-AUTHZ TU4", UserTestData.USERS_TU4, PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, false );

        // search for failed only:
        searchAuthZs( "GET-AUTHZ TU1_UPD", UserTestData.USERS_TU1_UPD, PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true );
        searchAuthZs( "GET-AUTHZ TU3", UserTestData.USERS_TU3, PermTestData.OBJS_TOB2, PermTestData.OPS_TOP2, true );
        searchAuthZs( "GET-AUTHZ TU4", UserTestData.USERS_TU4, PermTestData.OBJS_TOB3, PermTestData.OPS_TOP3, true );
    }


    /**
     *
     * @param msg
     * @param uArray
     */
    private static void searchAuthZs( String msg, String[][] uArray, String[][] oArray, String[][] opArray,
        boolean failedOnly )
    {
        LogUtil.logIt( msg );
        try
        {
            AuditMgr auditMgr = getManagedAuditMgr();
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                // now search for successful authentications:
                UserAudit uAudit = new UserAudit();
                uAudit.setUserId( user.getUserId() );
                uAudit.setFailedOnly( false );
                for ( String[] obj : oArray )
                {
                    uAudit.setObjName( PermTestData.getName( obj ) );
                    for ( String[] op : opArray )
                    {
                        uAudit.setOpName( PermTestData.getName( op ) );
                        uAudit.setObjId( PermTestData.getObjId( op ) );
                        uAudit.setFailedOnly( failedOnly );
                        List<AuthZ> authZs = auditMgr.searchAuthZs( uAudit );
                        assertNotNull( authZs );
                        assertTrue(
                            CLS_NM + "searchAuthZs failedOnly=" + failedOnly + ", search authorizations user ["
                                + user.getUserId() + "], objName [" + uAudit.getObjName() + "], opName [" + uAudit.getOpName() + "], objId [" + uAudit.getObjId() + "]", authZs.size() > 0 );
                    }
                }
            }
            LOG.debug( "searchAuthZs successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "searchAuthZs: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testGetAuthZs()
    {
        getAuthZs( "GET-AUTHZ TU1_UPD", UserTestData.USERS_TU1_UPD );
        getAuthZs( "GET-AUTHZ TU3", UserTestData.USERS_TU3 );
        getAuthZs( "GET-AUTHZ TU4", UserTestData.USERS_TU4 );
    }


    /**
     *
     * @param msg
     * @param uArray
     */
    private static void getAuthZs( String msg, String[][] uArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AuditMgr auditMgr = getManagedAuditMgr();
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                // now search for successful authentications:
                UserAudit uAudit = new UserAudit();
                uAudit.setUserId( user.getUserId() );
                uAudit.setFailedOnly( false );
                List<AuthZ> authZs = auditMgr.getUserAuthZs( uAudit );
                assertNotNull( authZs );
                assertTrue(
                    CLS_NM + "getUserAuthZs failed search for successful authorization user [" + user.getUserId() + "]",
                    authZs.size() > 0 );

                // now search for failed authentications:
                uAudit.setFailedOnly( true );
                authZs = auditMgr.getUserAuthZs( uAudit );
                assertNotNull( authZs );
                assertTrue( CLS_NM + "getUserAuthZs failed search for failed authorization user [" + user.getUserId()
                    + "]", authZs.size() > 0 );
            }
            LOG.debug( "getUserAuthZs successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "getUserAuthZs: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testSearchAuthNInvalid()
    {
        searchAuthNInvalid( "INV-AUTHN" );
    }


    /**
     *
     * @param msg
     */
    private static void searchAuthNInvalid( String msg )
    {
        LogUtil.logIt( msg );
        try
        {
            AuditMgr auditMgr = getManagedAuditMgr();
            UserAudit uAudit = new UserAudit();
            List<AuthZ> resultSet = auditMgr.searchInvalidUsers( uAudit );
            assertNotNull( resultSet );
            assertTrue( CLS_NM + "searchInvalidUsers failed search for invalid authentications", resultSet.size() > 0 );
            LOG.debug( "searchInvalidUsers successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "searchInvalidUsers: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     */
    public void testSearchBinds()
    {
        searchBinds( "BIND-USRS TU1_UPD", UserTestData.USERS_TU1_UPD );
        searchBinds( "BIND-USRS TU2_CHG", UserTestData.USERS_TU2_CHG );
        searchBinds( "BIND-USRS TU3", UserTestData.USERS_TU3 );
        searchBinds( "BIND-USRS TU4", UserTestData.USERS_TU4 );
    }


    /**
     *
     * @param msg
     * @param uArray
     */
    private static void searchBinds( String msg, String[][] uArray )
    {
        LogUtil.logIt( msg );
        try
        {
            AuditMgr auditMgr = getManagedAuditMgr();
            for ( String[] usr : uArray )
            {
                User user = UserTestData.getUser( usr );
                // now search for successful authentications:
                UserAudit uAudit = new UserAudit();
                uAudit.setUserId( user.getUserId() );
                uAudit.setFailedOnly( false );
                List<Bind> binds = auditMgr.searchBinds( uAudit );
                assertNotNull( binds );
                assertTrue(
                    CLS_NM + "searchBinds failed search for successful authentication user [" + user.getUserId() + "]",
                    binds.size() > 0 );

                // now search for failed authentications:
                uAudit.setFailedOnly( true );
                binds = auditMgr.searchBinds( uAudit );
                assertNotNull( binds );
                assertTrue( CLS_NM + "searchBinds failed search for failed authentication user [" + user.getUserId()
                    + "]", binds.size() > 0 );
            }
            LOG.debug( "searchBinds successful" );
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "searchBinds: failed with SecurityException rc=" + ex.getErrorId() + ", msg="
                    + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }


    /**
     *
     * @return
     * @throws org.apache.directory.fortress.core.SecurityException
     */
    private static AuditMgr getManagedAuditMgr() throws SecurityException
    {
        if ( FortressJUnitTest.isAdminEnabled() && adminSess == null )
        {
            adminSess = DelegatedMgrImplTest.createAdminSession();
        }
        return AuditMgrFactory.createInstance( TestUtils.getContext(), adminSess );
    }
}
