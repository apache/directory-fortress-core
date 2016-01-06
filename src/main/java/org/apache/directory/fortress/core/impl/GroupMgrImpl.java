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

import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GroupMgr;
import org.apache.directory.fortress.core.ReviewMgr;
import org.apache.directory.fortress.core.ReviewMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.Group;
import org.apache.directory.fortress.core.model.User;

import java.util.ArrayList;
import java.util.List;


/**
 * This Manager impl supplies CRUD methods used to manage groups stored within the ldap directory.
 * LDAP group nodes are used for utility and security functions within various systems and apps.
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class GroupMgrImpl extends Manageable implements GroupMgr
{
    private static final String CLS_NM = GroupMgrImpl.class.getName();
    private static final GroupP GROUP_P = new GroupP();

    /**
     * {@inheritDoc}
     */
    @Override
    public Group add( Group group ) throws org.apache.directory.fortress.core.SecurityException
    {
        String methodName = "add";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        
        if(!group.isMemberDn())
        {
            loadUserDns( group );
        }

        return GROUP_P.add( group );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group update( Group group ) throws SecurityException
    {
        String methodName = "update";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        
        return GROUP_P.update( group );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group delete( Group group ) throws SecurityException
    {
        String methodName = "delete";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        
        return GROUP_P.delete( group );
    }

    /**
     * {@inheritDoc}
     */
    public Group add( Group group, String key, String value ) throws SecurityException
    {
        String methodName = "addProperty";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        
        return GROUP_P.add( group, key, value );
    }

    /**
     * {@inheritDoc}
     */
    public Group delete( Group group, String key, String value ) throws SecurityException
    {
        String methodName = "deleteProperty";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        
        return GROUP_P.delete( group, key, value );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group read( Group group ) throws SecurityException
    {
        String methodName = "read";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        
        return GROUP_P.read( group );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> find( Group group ) throws SecurityException
    {
        String methodName = "find";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        
        return GROUP_P.search( group );
    }

    /**
     * {@inheritDoc}
     */
    public List<Group> find( User user ) throws SecurityException
    {
        String methodName = "findWithUsers";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        checkAccess(CLS_NM, methodName);
        loadUserDn( user );
        
        return GROUP_P.search( user );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group assign( Group group, String member ) throws SecurityException
    {
        String methodName = "assign";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();
        User user = reviewMgr.readUser( new User( member ) );
        
        return GROUP_P.assign( group, user.getDn() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group deassign( Group group, String member ) throws SecurityException
    {
        String methodName = "deassign";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();
        User user = reviewMgr.readUser( new User( member ) );
        
        return GROUP_P.deassign( group, user.getDn() );
    }

    private void loadUserDns( Group group ) throws SecurityException
    {
        if( CollectionUtils.isNotEmpty( group.getMembers() ))
        {
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();
            List<String> userDns = new ArrayList<String>();
            
            for( String member : group.getMembers() )
            {
                User user = reviewMgr.readUser( new User( member ) );
                userDns.add( user.getDn() );
            }
            
            group.setMembers( userDns );
        }
    }

    private void loadUserDn( User inUser ) throws SecurityException
    {
        ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();
        User outUser = reviewMgr.readUser( inUser );
        inUser.setDn( outUser.getDn() );
    }
}