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

import java.io.Serializable;
import java.util.Properties;

import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.PropertyMgr;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.FortEntity;
import org.apache.directory.fortress.core.model.Group;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Role;

public class PropertyMgrImpl extends Manageable implements PropertyMgr, Serializable
{

    private PropertyP propP = new PropertyP();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public FortEntity add( FortEntity entity, Properties props ) throws SecurityException
    {
        checkPropertyUpdateAccess( entity );
        entity.setContextId( this.contextId );
        return propP.addProperties( entity, props );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FortEntity update( FortEntity entity, Properties props ) throws SecurityException
    {
        checkPropertyUpdateAccess( entity );
        entity.setContextId( this.contextId );
        return propP.updateProperties( entity, props );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( FortEntity entity, Properties props ) throws SecurityException
    {
        checkPropertyUpdateAccess( entity );
        entity.setContextId( this.contextId );
        propP.deleteProperties( entity, props );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Properties get( FortEntity entity ) throws SecurityException
    {
        checkPropertyGetAccess( entity );
        entity.setContextId( this.contextId );
        return propP.getProperties( entity );
    }
    
    private void checkPropertyUpdateAccess( FortEntity entity ) throws SecurityException    
    {
        if( entity instanceof Role ){
            checkAccess( AdminMgrImpl.class.getName(), "updateRole" );
        }
        else if( entity instanceof AdminRole ){
            checkAccess( DelAdminMgrImpl.class.getName(), "updateRole" );
        }
        else if( entity instanceof Group ){
            checkAccess( GroupMgrImpl.class.getName(), "update" );
        }
        else if( entity instanceof PermObj ){
            PermObj pObj = (PermObj)entity;
            if(pObj.isAdmin()){
                checkAccess( DelAdminMgrImpl.class.getName(), "updatePermObj" );
            }
            else{
                checkAccess( AdminMgrImpl.class.getName(), "updatePermObj" );
            }       
        }
        else if( entity instanceof Permission ){
            Permission perm = (Permission)entity;
            if(perm.isAdmin()){
                checkAccess( DelAdminMgrImpl.class.getName(), "updatePermission" );
            }
            else{
                checkAccess( AdminMgrImpl.class.getName(), "updatePermission" );
            }  
        }
        else{
            throw new SecurityException( GlobalErrIds.ENTITY_PROP_NOT_SUPPORTED, "Properties not allowed on supplied entity" );
        }
    }
    
    private void checkPropertyGetAccess( FortEntity entity ) throws SecurityException
    {
        if( entity instanceof Role ){
            checkAccess( ReviewMgrImpl.class.getName(), "readRole" );
        }
        else if( entity instanceof AdminRole ){
            checkAccess( DelReviewMgrImpl.class.getName(), "readRole" );
        }
        else if( entity instanceof Group ){
            checkAccess( GroupMgrImpl.class.getName(), "read" );
        }
        else if( entity instanceof PermObj ){
            PermObj pObj = (PermObj)entity;
            if(pObj.isAdmin()){
                checkAccess( DelReviewMgrImpl.class.getName(), "readPermObj" );
            }
            else{
                checkAccess( ReviewMgrImpl.class.getName(), "readPermObj" );
            }       
        }
        else if( entity instanceof Permission ){
            Permission perm = (Permission)entity;
            if(perm.isAdmin()){
                checkAccess( DelReviewMgrImpl.class.getName(), "readPermission" );
            }
            else{
                checkAccess( ReviewMgrImpl.class.getName(), "readPermission" );
            }  
        }
        else{
            throw new SecurityException( GlobalErrIds.ENTITY_PROP_NOT_SUPPORTED, "Properties not allowed on supplied entity" );
        }
    }

}
