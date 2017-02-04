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
package org.apache.directory.fortress.core.rest;

import java.util.Properties;

import org.apache.directory.fortress.core.PropertyMgr;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.FortEntity;
import org.apache.directory.fortress.core.model.Session;

/**
 * TODO: implement me...
 */
public class PropertyMgrRestImpl implements PropertyMgr
{

    @Override
    public FortEntity add( FortEntity entity, Properties props ) throws SecurityException
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException( "not implemented" );
    }

    @Override
    public FortEntity update( FortEntity entity, Properties props ) throws SecurityException
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException( "not implemented" );
    }

    @Override
    public void delete( FortEntity entity, Properties props ) throws SecurityException
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException( "not implemented" );
    }

    @Override
    public Properties get( FortEntity entity ) throws SecurityException
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException( "not implemented" );
    }

    @Override
    public void setAdmin( Session session )
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException( "not implemented" );
    }

    @Override
    public void setContextId( String contextId )
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException( "not implemented" );
    }
}
