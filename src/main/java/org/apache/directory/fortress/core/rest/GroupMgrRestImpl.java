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

import org.apache.directory.fortress.core.*;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.Manageable;
import org.apache.directory.fortress.core.model.FortRequest;
import org.apache.directory.fortress.core.model.FortResponse;
import org.apache.directory.fortress.core.model.Group;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.util.VUtil;

import java.util.List;

/**
 * This Manager impl supplies CRUD methods used to manage groups using REST.
 * LDAP group nodes are used for utility and security functions within various systems and apps.
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class GroupMgrRestImpl  extends Manageable implements GroupMgr
{
    private static final String CLS_NM = GroupMgrRestImpl.class.getName();

    /**
     * {@inheritDoc}
     */
    @Override
    public Group add( Group group ) throws SecurityException
    {
        VUtil.assertNotNull( group, GlobalErrIds.GROUP_NULL, CLS_NM + ".add" );
        Group retGroup;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( group );
        if ( this.adminSess != null )
        {
            ////request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.GROUP_ADD );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retGroup = ( Group ) response.getEntity();
        }
        else
        {
            throw new org.apache.directory.fortress.core.SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retGroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group update( Group group ) throws SecurityException
    {
        VUtil.assertNotNull( group, GlobalErrIds.GROUP_NULL, CLS_NM + ".update" );
        Group retGroup;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( group );
        if ( this.adminSess != null )
        {
            ////request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.GROUP_UPDATE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retGroup = ( Group ) response.getEntity();
        }
        else
        {
            throw new org.apache.directory.fortress.core.SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retGroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group delete( Group group ) throws SecurityException
    {
        VUtil.assertNotNull( group, GlobalErrIds.GROUP_NULL, CLS_NM + ".delete" );
        Group retGroup;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( group );
        if ( this.adminSess != null )
        {
            ////request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.GROUP_DELETE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retGroup = ( Group ) response.getEntity();
        }
        else
        {
            throw new org.apache.directory.fortress.core.SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retGroup;
    }

    /**
     * {@inheritDoc}
     */
    public Group add( Group group, String key, String value ) throws SecurityException
    {
        throw new UnsupportedOperationException();
    }


    /**
     * {@inheritDoc}
     */
    public Group delete( Group group, String key, String value ) throws SecurityException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group read( Group group ) throws SecurityException
    {
        VUtil.assertNotNull(group, GlobalErrIds.GROUP_NULL, CLS_NM + ".read");
        Group retGroup;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(group);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.GROUP_READ);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retGroup = (Group) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retGroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> find( Group group ) throws SecurityException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public List<Group> find( User user ) throws SecurityException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> roleGroups( Role role ) throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".roleGroups");
        List<Group> retGroups;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.GROUP_ASGNED);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retGroups = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retGroups;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserRole> groupRoles( Group group ) throws SecurityException
    {
        VUtil.assertNotNull(group, GlobalErrIds.GROUP_NULL, CLS_NM + ".groupRoles");
        List<UserRole> retRoles;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
        request.setEntity(group);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.getInstance().post(szRequest, HttpIds.GROUP_ROLE_ASGNED);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retRoles = response.getEntities();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retRoles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group assign( Group group, String member ) throws SecurityException
    {
        VUtil.assertNotNull( group, GlobalErrIds.GROUP_NULL, CLS_NM + ".assign" );
        VUtil.assertNotNull( member, GlobalErrIds.GROUP_MEMBER_NULL, CLS_NM + ".assign" );
        Group retGroup;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( group );
        request.setValue( member );
        if ( this.adminSess != null )
        {
            ////request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.GROUP_ASGN );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retGroup = ( Group ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retGroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group deassign( Group group, String member ) throws SecurityException
    {
        VUtil.assertNotNull( group, GlobalErrIds.GROUP_NULL, CLS_NM + ".deassign" );
        VUtil.assertNotNull( member, GlobalErrIds.GROUP_MEMBER_NULL, CLS_NM + ".deassign" );
        Group retGroup;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( group );
        request.setValue( member );
        if ( this.adminSess != null )
        {
            ////request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.GROUP_DEASGN );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retGroup = ( Group ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retGroup;
    }
}
