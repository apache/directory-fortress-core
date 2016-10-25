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
import org.apache.directory.fortress.core.model.FortEntity;
import org.apache.directory.fortress.core.model.Role;

public class PropertyP
{
    private PropertyDAO propDAO = new PropertyDAO();
    private RoleDAO rDAO = new RoleDAO();
    
    public FortEntity addProperties( FortEntity entity, Properties props ) throws UpdateException, FinderException {
        
        return propDAO.addProperties( entity, props, this.getPropertyProvider( entity ) );
    }
    
    private PropertyProvider getPropertyProvider( FortEntity entity ){
        if( entity instanceof Role ){
            return rDAO;
        }
        //TODO: add rest of DAOs
        else{
            //TODO: throw exception
            return null;
        }
    }
}
