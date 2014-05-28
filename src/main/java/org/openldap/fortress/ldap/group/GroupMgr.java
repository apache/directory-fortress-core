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

import org.openldap.fortress.Manageable;
import org.openldap.fortress.rbac.User;

import java.util.List;

/**
 * This interface prescribes CRUD methods used to manage groups stored within the ldap directory.
 * <p/>
 * This class is thread safe.
 * <p/>
 *
 * @author Shawn McKinney
 */
public interface GroupMgr extends Manageable
{
    /**
     * Create a new group node.,
     *
     * @param group contains {@link Group}.
     * @return {@link Group} containing entity just added.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    public Group add( Group group ) throws org.openldap.fortress.SecurityException;

    /**
     * Modify existing group node.  The name is required.  Does not update members or properties.
     * Use {@link GroupMgr#add( Group group, String key, String value )}, {@link GroupMgr#delete( Group group, String key, String value )},
     * {@link GroupMgr#assign( Group group, String member) }, or {@link GroupMgr#deassign( Group group, String member) } for multi-occurring attributes.
     *
     * @param group contains {@link Group}.
     * @return {@link Group} containing entity just modified.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    public Group update( Group group ) throws org.openldap.fortress.SecurityException;

    /**
     * Delete existing group node.  The name is required.
     *
     * @param group contains {@link Group}.
     * @return {@link Group} containing entity just removed.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    public Group delete( Group group ) throws org.openldap.fortress.SecurityException;

    /**
     * Add a property to an existing group node. Must have a name and at least one member.
     *
     * @param group contains {@link Group}.
     * @param key contains the property key.
     * @param value contains contains the property value.
     * @return {@link Group} containing entity just modified.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    public Group add( Group group, String key, String value ) throws org.openldap.fortress.SecurityException;

    /**
     * Delete existing group node.  The name is required.
     *
     * @param group contains {@link Group}.
     * @param key contains the property key.
     * @param value contains contains the property value.
     * @return {@link Group} containing entity just modified.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    public Group delete( Group group, String key, String value ) throws org.openldap.fortress.SecurityException;

    /**
     * Read an existing group node.  The name is required.
     *
     * @param group contains {@link Group} with name field set with an existing group name.
     * @return {@link Group} containing entity found.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    public Group read( Group group ) throws org.openldap.fortress.SecurityException;

    /**
     * Search using a full or partial group node.  The name is required.
     *
     * @param group contains {@link Group}.
     * @return List of type {@link Group} containing entities found.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    public List<Group> find( Group group ) throws org.openldap.fortress.SecurityException;

    /**
     * Search for groups by userId.  Member (maps to userId) and is required.
     *
     * @param user contains userId that maps to Group member attribute.
     * @return {@link Group} containing entity just added.
     * @throws org.openldap.fortress.SecurityException in the event system error.
     */
    public List<Group> find( User user ) throws org.openldap.fortress.SecurityException;

    /**
     * Assign a user to an existing group node.  The group name and member are required.
     *
     * @param group contains {@link Group}.
     * @param member is the relative distinguished name (rdn) of an existing user in ldap.
     * @return {@link Group} containing entity to assign.
     * @throws org.openldap.fortress.SecurityException in the event entry already present or other system error.
     */
    public Group assign( Group group, String member ) throws org.openldap.fortress.SecurityException;

    /**
     * Deassign a member from an existing group node. The group name and member are required.
     *
     * @param group contains {@link Group}.
     * @param member is the relative distinguished name (rdn) of an existing user in ldap.
     * @return {@link Group} containing entity to deassign
     * @throws org.openldap.fortress.SecurityException in the event entry already present or other system error.
     */
    public Group deassign( Group group, String member ) throws org.openldap.fortress.SecurityException;
}