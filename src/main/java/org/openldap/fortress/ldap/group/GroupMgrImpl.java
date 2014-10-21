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
package org.openldap.fortress.ldap.group;

import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.ReviewMgr;
import org.openldap.fortress.ReviewMgrFactory;
import org.openldap.fortress.rbac.Manageable;
import org.openldap.fortress.rbac.User;
import org.openldap.fortress.util.attr.VUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This Manager impl supplies CRUD methods used to manage groups stored within the ldap directory.
 * LDAP group nodes are used for utility and security functions within various systems and apps.
 * <p/>
 * This class is thread safe.
 * <p/>

 *
 * @author Shawn McKinney
 */
public class GroupMgrImpl extends Manageable implements GroupMgr
{
    private static final String CLS_NM = GroupMgrImpl.class.getName();
    private static final GroupP groupP = new GroupP();

    /**
     * Create a new group node.  Must have a name and at least one member.
     *
     * @param group contains {@link Group}.
     * @return {@link Group} containing entity just added.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    @Override
    public Group add( Group group ) throws org.openldap.fortress.SecurityException
    {
        String methodName = "add";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        if(!group.isMemberDn())
        {
            loadUserDns( group );
        }

        return groupP.add( group );
    }

    /**
     * Modify existing group node.  The name is required.  Does not update members or properties.
     * Use {@link GroupMgr#add( Group group, String key, String value )}, {@link GroupMgr#delete( Group group, String key, String value )},
     * {@link GroupMgr#assign( Group group, String member) }, or {@link GroupMgr#deassign( Group group, String member) } for multi-occurring attributes.
     *
     * @param group contains {@link Group}.
     * @return {@link Group} containing entity just modified.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    @Override
    public Group update( Group group ) throws org.openldap.fortress.SecurityException
    {
        String methodName = "update";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        return groupP.update( group );
    }

    /**
     * Delete existing group node.  The name is required.
     *
     * @param group contains {@link Group}.
     * @return {@link Group} containing entity just removed.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    @Override
    public Group delete( Group group ) throws org.openldap.fortress.SecurityException
    {
        String methodName = "delete";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        return groupP.delete( group );
    }

    /**
     * Add a property to an existing group node.  The name is required.
     *
     * @param group contains {@link Group}.
     * @param key contains the property key.
     * @param value contains contains the property value.
     * @return {@link Group} containing entity just modified.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    public Group add( Group group, String key, String value ) throws org.openldap.fortress.SecurityException
    {
        String methodName = "addProperty";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        return groupP.add( group, key, value );
    }

    /**
     * Delete existing group node.  The name is required.
     *
     * @param group contains {@link Group}.
     * @param key contains the property key.
     * @param value contains contains the property value.
     * @return {@link Group} containing entity just modified.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    public Group delete( Group group, String key, String value ) throws org.openldap.fortress.SecurityException
    {
        String methodName = "deleteProperty";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        return groupP.delete( group, key, value );
    }

    /**
     * Read an existing group node.  The name is required.
     *
     * @param group contains {@link Group} with name field set with an existing group name.
     * @return {@link Group} containing entity found.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    @Override
    public Group read( Group group ) throws org.openldap.fortress.SecurityException
    {
        String methodName = "read";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        return groupP.read( group );
    }

    /**
     * Search using a full or partial group node.  The name is required.
     *
     * @param group contains {@link Group}.
     * @return List of type {@link Group} containing entities found.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    @Override
    public List<Group> find( Group group ) throws org.openldap.fortress.SecurityException
    {
        String methodName = "find";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        return groupP.search( group );
    }

    /**
     * Search for groups by userId.  Member (maps to userId) and is required.
     *
     * @param user contains userId that maps to Group member attribute.
     * @return {@link Group} containing entity just added.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    public List<Group> find( User user ) throws org.openldap.fortress.SecurityException
    {
        String methodName = "findWithUsers";
        assertContext(CLS_NM, methodName, user, GlobalErrIds.USER_NULL);
        checkAccess(CLS_NM, methodName);
        loadUserDn( user );
        return groupP.search( user );
    }

    /**
     * Assign a user to an existing group node.  The name is required and userDn are required.
     *
     * @param group contains {@link Group}.
     * @param member is the relative distinguished name (rdn) of an existing user in ldap.
     * @return {@link Group} containing entity to assign.
     * @throws org.openldap.fortress.SecurityException in the event entry already present or other system error.
     */
    @Override
    public Group assign( Group group, String member ) throws org.openldap.fortress.SecurityException
    {
        String methodName = "assign";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();
        User user = reviewMgr.readUser( new User( member ) );
        return groupP.assign( group, user.getDn() );
    }

    /**
     * Deassign a user from an existing group node.  The name is required and userDn are required.
     *
     * @param group contains {@link Group}.
     * @param member is the relative distinguished name (rdn) of an existing user in ldap.
     * @return {@link Group} containing entity to deassign
     * @throws org.openldap.fortress.SecurityException in the event entry already present or other system error.
     */
    @Override
    public Group deassign( Group group, String member ) throws org.openldap.fortress.SecurityException
    {
        String methodName = "deassign";
        assertContext(CLS_NM, methodName, group, GlobalErrIds.GROUP_NULL);
        checkAccess(CLS_NM, methodName);
        ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();
        User user = reviewMgr.readUser( new User( member ) );
        return groupP.deassign( group, user.getDn() );
    }

    private void loadUserDns( Group group ) throws org.openldap.fortress.SecurityException
    {
        if( VUtil.isNotNullOrEmpty( group.getMembers() ))
        {
            ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();
            List<String> userDns = new ArrayList<>();
            for( String member : group.getMembers() )
            {
                User user = reviewMgr.readUser( new User( member ) );
                userDns.add( user.getDn() );
            }
            group.setMembers( userDns );
        }
    }

    private void loadUserDn( User inUser ) throws org.openldap.fortress.SecurityException
    {
        ReviewMgr reviewMgr = ReviewMgrFactory.createInstance();
        String userDns;
        User outUser = reviewMgr.readUser( inUser );
        inUser.setDn( outUser.getDn() );
    }
}