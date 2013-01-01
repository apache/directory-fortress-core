/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.rbac.AdminRole;
import com.jts.fortress.rbac.OrgUnit;
import com.jts.fortress.rbac.UserAdminRole;
import com.jts.fortress.rbac.User;

import java.util.List;

/**
 * This class prescribes the ARBAC02 DelReviewMgr interface for performing policy interrogation of provisioned Fortress ARBAC entities
 * that reside in LDAP directory.
 * These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification describes delegated administrative
 * operations for the creation and maintenance of ARBAC element sets and relations.  Delegated administrative review functions for performing administrative queries
 * and system functions for creating and managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="./doc-files/ARbac.png">
 * <p/>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without sacrificing regulations for accountability or traceability.
 * <p/>
 * This interface's implementer will NOT be thread safe if parent instance variables ({@link Manageable#setContextId(String)} or {@link Manageable#setAdmin(com.jts.fortress.rbac.Session)}) are set.
 *
 * @author Shawn McKinney
 */
public interface DelReviewMgr extends Manageable
{
    /**
     * Method reads Admin Role entity from the admin role container in directory.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link AdminRole#name} - contains the name of the AdminRole being targeted for read</li>
     * </ul>
     *
     * @param role contains role name to be read.
     * @return AdminRole entity that corresponds with role name.
     * @throws com.jts.fortress.SecurityException
     *          will be thrown if role not found or system error occurs.
     */
    public AdminRole readRole(AdminRole role)
        throws com.jts.fortress.SecurityException;


    /**
     * Method will return a list of type Admin Role.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link AdminRole#name} - contains all or some chars in the name of AdminRole(s) targeted for search</li>
     * </ul>
     *
     * @param searchVal contains the all or some of the chars corresponding to admin role entities stored in directory.
     * @return List of type AdminRole containing role entities that match the search criteria.
     * @throws com.jts.fortress.SecurityException
     *          in the event of system error.
     */
    public List<AdminRole> findRoles(String searchVal)
        throws SecurityException;


    /**
     * This function returns the set of admin roles assigned to a given user. The function is valid if and
     * only if the user is a member of the USERS data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link User#userId} - contains the userId associated with the User object targeted for search.</li>
     * </ul>
     *
     * @param user contains userId matching user entity stored in the directory.
     * @return List of type UserAdminRole containing the user admin role data.
     * @throws com.jts.fortress.SecurityException
     *          If user not found or system error occurs.
     */
    public List<UserAdminRole> assignedRoles(User user)
        throws com.jts.fortress.SecurityException;

    /**
     * This method returns the data set of all users who are assigned the given admin role.  This searches the User data set for
     * AdminRole relationship.  This method does NOT search for hierarchical Admin Roles relationships.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link AdminRole#name} - contains the name of AdminRole targeted for search</li>
     * </ul>
     *
     * @param role contains the role name used to search the User data set.
     * @return List of type User containing the users assigned data.
     * @throws com.jts.fortress.SecurityException
     *          If system error occurs.
     */
    public List<User> assignedUsers(AdminRole role)
        throws com.jts.fortress.SecurityException;


    /**
     * Commands reads existing OrgUnit entity from OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link com.jts.fortress.rbac.OrgUnit#name} - contains the name associated with the OrgUnit object targeted for search.</li>
     * <li>{@link com.jts.fortress.rbac.OrgUnit#type} - contains the type of OU:  {@link com.jts.fortress.rbac.OrgUnit.Type#USER} or {@link com.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return OrgUnit entity that corresponds with ou name and type.
     * @throws SecurityException in the event of data validation or system error.
     */
    public OrgUnit read(OrgUnit entity)
        throws SecurityException;

    /**
     * Commands searches existing OrgUnit entities from OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type parameter on API.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link com.jts.fortress.rbac.OrgUnit#type} - contains the type of OU:  {@link com.jts.fortress.rbac.OrgUnit.Type#USER} or {@link com.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * <li>searchVal - contains some or all of the chars associated with the OrgUnit objects targeted for search.</li>
     * </ul>
     *
     * @param type      either PERM or USER
     * @param searchVal contains the leading chars that map to {@link OrgUnit#name} on existing OrgUnit(s) targeted for search.
     * @return List of type OrgUnit containing the OrgUnit data.
     * @throws com.jts.fortress.SecurityException
     *
     */
    public List<OrgUnit> search(OrgUnit.Type type, String searchVal)
        throws SecurityException;
}

