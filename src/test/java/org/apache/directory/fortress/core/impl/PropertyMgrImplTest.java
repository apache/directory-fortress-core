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

import java.util.Properties;

import org.apache.directory.fortress.core.PropertyMgr;
import org.apache.directory.fortress.core.PropertyMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.FortEntity;
import org.apache.directory.fortress.core.model.Group;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

public class PropertyMgrImplTest extends TestCase
{
    private static final String CLS_NM = PropertyMgrImplTest.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static Session adminSess = null;

    public static final String[][] PROPS = {
        {
            "test-key",
            "test-value"
        },
        {
            "test-key-2",
            "test-value-2"
        }        
    };
    
    public static final String[][] PROPS_UPDATED = {
        {
            "test-key",
            "test-value-updated"
        }        
    };
    
    public PropertyMgrImplTest( String name )
    {
        super( name );
    }

    public void testCrudProperties(){
        
        Role role = RoleTestData.getRole( RoleTestData.ROLES_TR1[0] );
        AdminRole adminRole = AdminRoleTestData.getRole( AdminRoleTestData.AROLES_TR1[0] );
        PermObj permObj = PermTestData.getObj( PermTestData.OBJS_TOB1[0] );
        Permission permOp = PermTestData.getOp( permObj.getObjName(), PermTestData.OPS_TOP1[0] );
        Group group = GroupTestData.TEST_GROUP1;
        
        addProperties( "ADD-PROP-RLE", role, PROPS );        
        addProperties( "ADD-PROP-ADMRLE", adminRole, PROPS );
        addProperties( "ADD-PROP-POBJ", permObj, PROPS );
        addProperties( "ADD-PROP-POP", permOp, PROPS );
        addProperties( "ADD-PROP-GRP", group, PROPS );
        
        updateProperties( "UPD-PROP-RLE", role, PROPS_UPDATED[0] );        
        updateProperties( "UPD-PROP-ADMRLE", adminRole, PROPS_UPDATED[0] );
        updateProperties( "UPD-PROP-POBJ", permObj, PROPS_UPDATED[0] );
        updateProperties( "UPD-PROP-POP", permOp, PROPS_UPDATED[0] );
        updateProperties( "UPD-PROP-POP", group, PROPS_UPDATED[0] );
        
        deleteProperties( "DEL-PROP-RLE", role, PROPS_UPDATED[0] );        
        deleteProperties( "DEL-PROP-ADMRLE", adminRole, PROPS_UPDATED[0] );
        deleteProperties( "DEL-PROP-POBJ", permObj, PROPS_UPDATED[0] );
        deleteProperties( "DEL-PROP-POP", permOp, PROPS_UPDATED[0] );
        deleteProperties( "DEL-PROP-POP", group, PROPS_UPDATED[0] );
    }
    
    public static void addProperties(String msg, FortEntity entity, String[][] propArrays )
    {
        LogUtil.logIt( msg );
        try
        {                        
            PropertyMgr propertyMgr = getManagedPropertyMgr();
            
            for(String[] propArray : propArrays){
                propertyMgr.add( entity, getProperty( propArray ) );
                Properties newAddedProps = propertyMgr.get( entity );
                assertNotNull( newAddedProps );
                assertEquals( propArray[1],  newAddedProps.getProperty( propArray[0] ) );
            }
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "addProperties caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }
    
    public static void updateProperties(String msg, FortEntity entity, String[] propArray )
    {
        LogUtil.logIt( msg );
        try
        {            
            PropertyMgr propertyMgr = getManagedPropertyMgr();
            propertyMgr.update( entity, getProperty( propArray ) );
            
            Properties newAddedProps = propertyMgr.get( entity );
            assertEquals( propArray[1],  newAddedProps.getProperty( propArray[0] ) );
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "addProperties caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }
    
    public static void deleteProperties(String msg, FortEntity entity, String[] propArray )
    {
        LogUtil.logIt( msg );
        try
        {            
            PropertyMgr propertyMgr = getManagedPropertyMgr();
            propertyMgr.delete( entity, getProperty( propArray ) );
            
            Properties newAddedProps = propertyMgr.get( entity );
            assertNull( newAddedProps.getProperty( propArray[0] ) );
        }
        catch ( SecurityException ex )
        {
            LOG.error(
                "addProperties caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }
    
    public static PropertyMgr getManagedPropertyMgr() throws SecurityException
    {
        return PropertyMgrFactory.createInstance( TestUtils.getContext(), adminSess );
    }
    
    public static Properties getProperty( String[] prop )
    {
        Properties props = new Properties();
        props.setProperty( prop[0], prop[1] );
        return props;        
    }
}
