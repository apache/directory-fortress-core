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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapNoSuchObjectException;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.UpdateException;
import org.apache.directory.fortress.core.ldap.LdapDataProvider;
import org.apache.directory.fortress.core.model.FortEntity;
import org.apache.directory.fortress.core.util.PropUtil;
import org.apache.directory.ldap.client.api.LdapConnection;

final class PropertyDAO extends LdapDataProvider
{

    /**
     * Add properties to the provided entity using the provided property provider
     *
     * @param entity A FortressEntity that supports properties (Role, AdminRole, Group, Permission, PermObj)
     * @param properties
     * @param propProvider DAO for entity type that implements property provider interface
     * @return Entity with current property value
     * @throws UpdateException
     * @throws FinderException
     */
    FortEntity addProperties( FortEntity entity, Properties properties, PropertyProvider propProvider ) throws UpdateException, FinderException
    { 
        LdapConnection ld = null;
        String entityDn = propProvider.getDn( entity );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();
            loadProperties( properties, mods, GlobalIds.PROPS, false );

            ld = getAdminConnection();
            modify( ld, entityDn, mods, entity );            
        }
        catch ( LdapException e )
        {
            String error = "add entity properties[" + entity.getClass().getSimpleName() + "] caught LDAPException=" + e;
            throw new UpdateException( GlobalErrIds.USER_UPDATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return propProvider.getEntity( entity );
    }
    
    /**
     * Update properties on the provided entity using the provided property provider     
     *
     * @param entity A FortressEntity that supports properties (Role, AdminRole, Group, Permission, PermObj)
     * @param properties
     * @param propProvider DAO for entity type that implements property provider interface
     * @return Entity with current property value
     * @throws UpdateException
     * @throws FinderException
     */
    FortEntity updateProperties( FortEntity entity, Properties properties, PropertyProvider propProvider ) throws UpdateException, FinderException
    { 
        //ftProps all have same name, so will need to delete proprs first, then readd ones that are approprirate
        
        //get current properties
        Properties currentProps = this.getProperties( entity, propProvider );
        Properties toDeleteProps = new Properties();
        
        //look for proeprties (ftProp=key:value) that are being updated and add to delete list
        for(Object key : properties.keySet()){
            String value = currentProps.getProperty( (String)key );
            toDeleteProps.put( (String)key, value );   
        }
        
        //delete exising properties
        this.deleteProperties( entity, toDeleteProps, propProvider );
        
        //add the udpates back
        this.addProperties( entity, properties, propProvider );        
        
        return propProvider.getEntity( entity );
    }
    
    /**
     * Delete properties to the provided entity using the provided property provider    
     *
     * @param entity A FortressEntity that supports properties (Role, AdminRole, Group, Permission, PermObj)
     * @param properties
     * @param propProvider DAO for entity type that implements property provider interface
     * @throws UpdateException
     * @throws FinderException
     */
    void deleteProperties( FortEntity entity, Properties properties, PropertyProvider propProvider ) throws UpdateException, FinderException
    {
        LdapConnection ld = null;
        String entityDn = propProvider.getDn( entity );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();
            removeProperties( properties, mods, GlobalIds.PROPS );            

            ld = getAdminConnection();
            modify( ld, entityDn, mods, entity );            
        }
        catch ( LdapException e )
        {
            String error = "delete entity properties[" + entity.getClass().getSimpleName() + "] caught LDAPException=" + e;
            throw new UpdateException( GlobalErrIds.USER_UPDATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }
    
    /**
     * Get properties on the provided entity using the provided property provider     
     *
     * @param entity A FortressEntity that supports properties (Role, AdminRole, Group, Permission, PermObj)
     * @param propProvider DAO for entity type that implements property provider interface
     * @return Current properties of entity
     * @throws FinderException
     */
    Properties getProperties( FortEntity entity, PropertyProvider propProvider ) throws FinderException
    { 
        Properties props = null;
        LdapConnection ld = null;
        String entityDn = propProvider.getDn( entity );

        try
        {
            ld = getAdminConnection();
            Entry findEntry = read( ld, entityDn, new String[]{ GlobalIds.PROPS } );
            props = PropUtil.getProperties( getAttributes( findEntry, GlobalIds.PROPS ) );
            
            if( props == null ){
                props = new Properties();
            }
        }
        catch ( LdapNoSuchObjectException e )
        {
            String warning = "get properties COULD NOT FIND ENTRY for entity [" + entityDn + "]";
            throw new FinderException( GlobalErrIds.ENTITY_PROPS_NOT_FOUND, warning );
        }
        catch ( LdapException e )
        {
            String error = "get properties [" + entityDn + "]= caught LDAPException=" + e;
            throw new FinderException( GlobalErrIds.ENTITY_PROPS_LOAD_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return props;
    }
}