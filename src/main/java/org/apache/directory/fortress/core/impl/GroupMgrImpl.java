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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.fortress.annotation.AdminPermissionOperation;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GroupMgr;
import org.apache.directory.fortress.core.ReviewMgr;
import org.apache.directory.fortress.core.ReviewMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.Group;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;


/**
 * This Manager impl supplies CRUD methods used to manage groups stored within the ldap directory.
 * LDAP group nodes are used for utility and security functions within various systems and apps.
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class GroupMgrImpl extends Manageable implements GroupMgr, Serializable
{
    private static final String CLS_NM = GroupMgrImpl.class.getName();
    private GroupP groupP = new GroupP();

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Group add( Group group ) throws org.apache.directory.fortress.core.SecurityException
    {
        String methodName = "add";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        
        if(!group.isMemberDn())
        {
            if( group.getType() == Group.Type.ROLE )
            {
                loadRoleDns( group );
            }
            else
            {
                loadUserDns( group );
            }
            group.setMemberDn(true);
        }

        return groupP.add( group );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Group update( Group group ) throws SecurityException
    {
        String methodName = "update";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        
        return groupP.update( group );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Group delete( Group group ) throws SecurityException
    {
        String methodName = "delete";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        
        return groupP.delete( group );
    }

    /**
     * {@inheritDoc}
     */
    @AdminPermissionOperation(operationName="addProperty")
    public Group add( Group group, String key, String value ) throws SecurityException
    {
        String methodName = "addProperty";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        
        return groupP.add( group, key, value );
    }

    /**
     * {@inheritDoc}
     */
    @AdminPermissionOperation(operationName="deleteProperty")
    public Group delete( Group group, String key, String value ) throws SecurityException
    {
        String methodName = "deleteProperty";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        
        return groupP.delete( group, key, value );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Group read( Group group ) throws SecurityException
    {
        String methodName = "read";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        
        return groupP.read( group );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<Group> find( Group group ) throws SecurityException
    {
        String methodName = "find";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        
        return groupP.search( group );
    }

    /**
     * {@inheritDoc}
     */
    @AdminPermissionOperation(operationName="findWithUsers")
    public List<Group> find( User user ) throws SecurityException
    {
        String methodName = "findWithUsers";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        checkAccess( CLS_NM, methodName );
        loadUserDn( user );
        
        return groupP.search( user );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<Group> roleGroups( Role role ) throws SecurityException
    {
        String methodName = "roleGroups";
        assertContext(CLS_NM, methodName, role, GlobalErrIds.ROLE_NULL);
        checkAccess( CLS_NM, methodName );
        loadRoleDn( role );

        return groupP.roleGroups( role );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<UserRole> groupRoles( Group group ) throws SecurityException
    {
        String methodName = "groupRoles";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess( CLS_NM, methodName );

        return groupP.groupRoles( group );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Group assign( Group group, String member ) throws SecurityException
    {
        String methodName = "assign";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        ReviewMgr reviewMgr = ReviewMgrFactory.createInstance( this.contextId );
        String dn;
        if( group.getType() == Group.Type.ROLE )
        {
            Role inRole = new Role( member );
            inRole.setContextId( group.getContextId() );
            Role role = reviewMgr.readRole( inRole );
            dn = role.getDn();
            // Validate SSD constraints
            SDUtil.getInstance().validateSSD( group, role );
        }
        else
        {
            User inUser = new User( member );
            inUser.setContextId( group.getContextId() );
            User user = reviewMgr.readUser( inUser );
            dn = user.getDn();
        }

        return groupP.assign( group, dn );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public Group deassign( Group group, String member ) throws SecurityException
    {
        String methodName = "deassign";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        ReviewMgr reviewMgr = ReviewMgrFactory.createInstance( this.contextId );
        String dn;
        if( group.getType() == Group.Type.ROLE )
        {
            Role role = reviewMgr.readRole( new Role( member ) );
            dn = role.getDn();
        }
        else
        {
            User user = reviewMgr.readUser( new User( member ) );
            dn = user.getDn();
        }

        return groupP.deassign( group, dn );
    }

    private void loadUserDns( Group group ) throws SecurityException
    {
        if( CollectionUtils.isNotEmpty( group.getMembers() ))
        {
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance( this.contextId );
            List<String> userDns = new ArrayList<String>();
            
            for( String member : group.getMembers() )
            {
                User user = reviewMgr.readUser( new User( member ) );
                userDns.add( user.getDn() );
            }
            
            group.setMembers( userDns );
        }
    }

    private void loadRoleDns( Group group ) throws SecurityException
    {
        if( CollectionUtils.isNotEmpty( group.getMembers() ))
        {
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance( this.contextId );
            List<String> roleDns = new ArrayList<String>();

            for( String member : group.getMembers() )
            {
                Role role = reviewMgr.readRole( new Role( member ) );
                roleDns.add( role.getDn() );
            }

            group.setMembers( roleDns );
        }
    }

    private void loadUserDn( User inUser ) throws SecurityException
    {
        ReviewMgr reviewMgr = ReviewMgrFactory.createInstance( this.contextId );
        User outUser = reviewMgr.readUser( inUser );
        inUser.setDn( outUser.getDn() );
    }

    private void loadRoleDn( Role inRole ) throws SecurityException
    {
        ReviewMgr reviewMgr = ReviewMgrFactory.createInstance( this.contextId );
        Role outRole = reviewMgr.readRole( inRole );
        inRole.setDn( outRole.getDn() );
    }
}