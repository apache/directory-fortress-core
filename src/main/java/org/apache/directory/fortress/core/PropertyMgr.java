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
package org.apache.directory.fortress.core;

import java.util.Properties;

import org.apache.directory.fortress.core.model.FortEntity;

public interface PropertyMgr extends Manageable
{
    /**
     * Adds properties (ftProps) to a supplied fortress entity (Group, Role, AdminRole, Permission, PermObj) 
     *
     * @param entity Entity to add properties
     * @param props Properties to add to entity
     * @return Updated entity
     * @throws SecurityException
     */
    FortEntity add( FortEntity entity, Properties props ) throws SecurityException;
    
    /**
     * Update properties (ftProps) to a supplied fortress entity (Group, Role, AdminRole, Permission, PermObj) 
     *
     * @param entity Entity to update properties
     * @param props Properties to update to entity
     * @return Updated entity
     * @throws SecurityException
     */
    FortEntity update( FortEntity entity, Properties props ) throws SecurityException;
    
    /**
     * Delete properties (ftProps) from a supplied fortress entity (Group, Role, AdminRole, Permission, PermObj) 
     *
     * @param entity Entity to delete properties from
     * @param props Properties to delete from entity
     * @throws SecurityException
     */
    void delete( FortEntity entity, Properties props ) throws SecurityException;
    
    /**
     * Retrieve properties (ftProps) from a supplied fortress entity (Group, Role, AdminRole, Permission, PermObj) 
     *
     * @param entity Entity to return properties from
     * @return Properties for the supplied entity
     * @throws SecurityException
     */
    Properties get( FortEntity entity ) throws SecurityException;
}
