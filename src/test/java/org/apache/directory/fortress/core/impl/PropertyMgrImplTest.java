
package org.apache.directory.fortress.core.impl;

import java.util.Properties;

import org.apache.directory.fortress.core.PropertyMgr;
import org.apache.directory.fortress.core.PropertyMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.FortEntity;
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
        }        
    };
    
    public PropertyMgrImplTest( String name )
    {
        super( name );
    }

    public void testAddProperties(){
        
        Role role = RoleTestData.getRole( RoleTestData.ROLES_TR1[0] );
        
        addProperties( "ADD-PROP-RLE", role, PROPS[0] );        
    }
    
    public static void addProperties(String msg, FortEntity entity, String[] propArray )
    {
        LogUtil.logIt( msg );
        try
        {            
            PropertyMgr propertyMgr = getManagedPropertyMgr();
            propertyMgr.add( entity, getProperty( propArray ) );
            
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
