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

import org.apache.directory.fortress.core.PropertyMgr;
import org.apache.directory.fortress.core.ReviewMgr;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.FortEntity;
import org.apache.directory.fortress.core.model.Role;

public class PropertyMgrImpl extends Manageable implements PropertyMgr, Serializable
{

    private PropertyP propP = new PropertyP();
    
    @Override
    public FortEntity add( FortEntity entity, Properties props ) throws SecurityException
    {
        checkPropertyUpdateAccess( entity );
        entity.setContextId( this.contextId );
        return propP.addProperties( entity, props );
    }

    @Override
    public FortEntity update( FortEntity entity, Properties props ) throws SecurityException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete( FortEntity entity, Properties props ) throws SecurityException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String get( FortEntity entity, String key ) throws SecurityException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    private void checkPropertyUpdateAccess( FortEntity entity ) throws SecurityException{
        if( entity instanceof Role ){
            checkAccess( AdminMgrImpl.class.getName(), "updateRole" );
        }
        //TODO: add checks for other instances
        else{
            //TODO: valid error code
            throw new SecurityException( 1, "Properties not allowed on supplied entity" );
        }
    }
    
    private void checkPropertyGetAccess( FortEntity entity ) throws SecurityException{
        if( entity instanceof Role ){
            checkAccess( ReviewMgrImpl.class.getName(), "readRole" );
        }
        //TODO: add checks for other instances
        else{
            //TODO: valid error code
            throw new SecurityException( 1, "Properties not allowed on supplied entity" );
        }
    }

}
