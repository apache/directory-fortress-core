/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
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
        assertContext( CLS_NM, "add", group, GlobalErrIds.GROUP_NULL );
        loadUserDns( group );
        return groupP.add( group );
    }

    /**
     * Modify existing group node.  The name is required.  For multi-occurring attributes, members & properties, supplied
     * values will be added only. Use {@link GroupMgr#delete( Group group, String key, String value )} or {@link GroupMgr#deassign( Group group, String member) }
     * first if you need to replace existing value.
     *
     * @param group contains {@link Group}.
     * @return {@link Group} containing entity just modified.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    @Override
    public Group update( Group group ) throws org.openldap.fortress.SecurityException
    {
        assertContext( CLS_NM, "update", group, GlobalErrIds.GROUP_NULL );
        loadUserDns( group );
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
        assertContext( CLS_NM, "delete", group, GlobalErrIds.GROUP_NULL );
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
        assertContext( CLS_NM, "addProperty", group, GlobalErrIds.GROUP_NULL );
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
        assertContext( CLS_NM, "deleteProperty", group, GlobalErrIds.GROUP_NULL );
        return groupP.delete( group, key, value );
    }

    /**
     * Read an existing group node.  The name is required.
     *
     * @param group contains {@link Group}.
     * @return {@link Group} containing entity just added.
     * @throws org.openldap.fortress.SecurityException in the event entry already present or other system error.
     */
    @Override
    public Group read( Group group ) throws org.openldap.fortress.SecurityException
    {
        assertContext( CLS_NM, "read", group, GlobalErrIds.GROUP_NULL );
        return groupP.read( group );
    }

    /**
     * Search using a full or partial group node.  The name is required.
     *
     * @param group contains {@link Group}.
     * @return {@link Group} containing entity just added.
     * @throws org.openldap.fortress.SecurityException in the event entry already present or other system error.
     */
    @Override
    public List<Group> find( Group group ) throws org.openldap.fortress.SecurityException
    {
        assertContext( CLS_NM, "find", group, GlobalErrIds.GROUP_NULL );
        return groupP.search( group );
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
        assertContext( CLS_NM, "assign", group, GlobalErrIds.GROUP_NULL );
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
        assertContext( CLS_NM, "deassign", group, GlobalErrIds.GROUP_NULL );
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
}