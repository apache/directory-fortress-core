/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.arbac.AdminRole;
import com.jts.fortress.arbac.OrgUnit;
import com.jts.fortress.arbac.UserAdminRole;
import com.jts.fortress.rbac.User;

import java.util.List;

/**
 * This class prescribes the ARBAC02 DelegatedReviewMgr interface for performing policy interrogation of provisioned Fortress ARBAC entities
 * that reside in LDAP directory.
 * These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification describes delegated administrative
 * operations for the creation and maintenance of ARBAC element sets and relations.  Delegated administrative review functions for performing administrative queries
 * and system functions for creating and managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="../../../images/ARbac.png">
 * <p/>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without sacrificing regulations for accountability or traceability.
 * <p/>

 * @author smckinn
 * @created September 18, 2010
 */
public interface DelegatedReviewMgr extends com.jts.fortress.Authorizable
{
    /**
     * Method reads Admin Role entity from the admin role container in directory.
     * @param role contains role name to be read.
     * @return AdminRole entity that corresponds with role name.
     * @throws com.jts.fortress.SecurityException will be thrown if role not found or system error occurs.
     */
    public AdminRole readRole(AdminRole role)
        throws com.jts.fortress.SecurityException;


    /**
     * Method will return a list of type Admin Role.
     * @param searchVal contains the all or some of the chars corresponding to admin role entities stored in directory.
     * @return List of type AdminRole containing role entities that match the search criteria.
     * @throws com.jts.fortress.SecurityException in the event of system error.
     */
    public List<AdminRole> findRoles(String searchVal)
        throws SecurityException;


    /**
     * This function returns the set of admin roles assigned to a given user. The function is valid if and
     * only if the user is a member of the USERS data set.
     * @param user contains userId matching user entity stored in the directory.
     * @return List of type UserAdminRole containing the user admin role data.
     * @throws com.jts.fortress.SecurityException If user not found or system error occurs.
     */
    public List<UserAdminRole> assignedRoles(User user)
        throws com.jts.fortress.SecurityException;

    /**
     * This method returns the data set of all users who are assigned the given admin role.  This searches the User data set for
     * Role relationship.  This method does NOT search for hierarchical Admin Roles relationships.
     * @param role contains the role name used to search the User data set.
     * @return  List of type User containing the users assigned data.
     * @throws com.jts.fortress.SecurityException If system error occurs.
     */
    public List<User> assignedUsers(AdminRole role)
        throws com.jts.fortress.SecurityException;


    /**
     * Commands reads existing OrgUnit entity from OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * @param entity contains OrgUnit name and type.
     * @return
     * @throws SecurityException in the event of data validation or system error.
     */
    public OrgUnit read(OrgUnit entity)
        throws SecurityException;

    /**
     * Commands searches existing OrgUnit entities from OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type parameter on API.
     * @param type either PERM or USER
     * @param searchVal contains the leading chars for existing OrgUnit in OrgUnit dataset.
     * @return
     * @throws com.jts.fortress.SecurityException
     */
    public List<OrgUnit> search(OrgUnit.Type type, String searchVal)
        throws SecurityException;
}

