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

import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.UpdateException;
import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.FortEntity;
import org.apache.directory.fortress.core.model.Group;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Role;

final class PropertyP
{
    private PropertyDAO propDAO = new PropertyDAO();
    private RoleDAO rDAO = new RoleDAO();
    private AdminRoleDAO arDAO = new AdminRoleDAO();
    private GroupDAO gDAO = new GroupDAO();
    private PermOpDAO popDAO = new PermOpDAO();
    private PermObjDAO pobjDAO = new PermObjDAO();
    
    /**
     * Adds the provided properties to the provided entity 
     *
     * @param entity A FortressEntity that supports properties (Role, AdminRole, Group, Permission, PermObj)
     * @param props Properties to add
     * @return Updated entity with current properties
     * @throws UpdateException
     * @throws FinderException
     */
    FortEntity addProperties( FortEntity entity, Properties props ) throws UpdateException, FinderException
    {        
        return propDAO.addProperties( entity, props, this.getPropertyProvider( entity ) );
    }
    
    /**
     * Updates the provided properties on the provided entity
     *
     * @param entity A FortressEntity that supports properties (Role, AdminRole, Group, Permission, PermObj)
     * @param props Properties to update
     * @return Updated entity with current properties
     * @throws UpdateException
     * @throws FinderException
     */
    FortEntity updateProperties( FortEntity entity, Properties props ) throws UpdateException, FinderException
    {        
        return propDAO.updateProperties( entity, props, this.getPropertyProvider( entity ) );
    }
    
    /**
     * Deletes the provided properties on the provided entity
     *
     * @param entity A FortressEntity that supports properties (Role, AdminRole, Group, Permission, PermObj)
     * @param props Properties to delete
     * @throws UpdateException
     * @throws FinderException
     */
    void deleteProperties( FortEntity entity, Properties props ) throws UpdateException, FinderException
    {        
        propDAO.deleteProperties( entity, props, this.getPropertyProvider( entity ) );
    }
    
    /**
     * Gets the properties that are present on the provided entity     
     *
     * @param entity A FortressEntity that supports properties (Role, AdminRole, Group, Permission, PermObj)
     * @return Properties for the given entity
     * @throws FinderException
     */
    Properties getProperties( FortEntity entity ) throws FinderException
    {        
        return propDAO.getProperties( entity, this.getPropertyProvider( entity ) );
    }
    
    private PropertyProvider getPropertyProvider( FortEntity entity )
    {
        if( entity instanceof AdminRole ){
            return arDAO;
        }
        else  if( entity instanceof Role ){
            return rDAO;
        }
        else if( entity instanceof Group ){
            return gDAO;
        }
        else if( entity instanceof PermObj ){
            return pobjDAO;
        }
        else if( entity instanceof Permission ){
            return popDAO;
        }
        else{
            throw new IllegalArgumentException( "Provided entity does not have an associated property provider DAO" );
        }
    }
}
