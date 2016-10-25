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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.entry.DefaultModification;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.UpdateException;
import org.apache.directory.fortress.core.ldap.LdapDataProvider;
import org.apache.directory.fortress.core.model.ConstraintUtil;
import org.apache.directory.fortress.core.model.FortEntity;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.PropUtil;
import org.apache.directory.ldap.client.api.LdapConnection;

public class PropertyDAO extends LdapDataProvider
{

    public FortEntity addProperties( FortEntity entity, Properties properties, PropertyProvider propProvider ) throws UpdateException, FinderException{ 
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
            String error = "add entity properties[" + entity.getClass().getSimpleName() + "] caught LDAPException=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.USER_UPDATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return propProvider.getEntity( entity );
    }
}