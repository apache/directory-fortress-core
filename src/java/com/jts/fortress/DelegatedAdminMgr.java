/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.arbac.AdminRole;
import com.jts.fortress.arbac.OrgUnit;

/**
 * This class prescribes the ARBAC02 DelegatedAdminMgr interface for performing policy administration of Fortress ARBAC entities
 * that reside in LDAP directory.
 * These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification describes delegated administrative
 * operations for the creation and maintenance of ARBAC element sets and relations.  Delegated administrative review functions for performing administrative queries
 * and system functions for creating and managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 *
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="../../../images/ARbac.png">
 * <p/>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without sacrificing regulations for accountability or traceability.
 * <p/>

 * @author smckinn
 * @created September 18, 2010
 */
public interface DelegatedAdminMgr extends com.jts.fortress.Authorizable
{
    /**
     * This command creates a new admin role. The command is valid if and only if the new admin role is not
     * already a member of the ADMIN ROLES data set. The ADMIN ROLES data set is updated.
     * Initially, no user or permission is assigned to the new role.
     *
     * @param role Contains role name and description.
     * @throws com.jts.fortress.SecurityException thrown in the event of data validation or system error.
     */
    public AdminRole addRole(AdminRole role)
        throws com.jts.fortress.SecurityException;


    /**
     * This command deletes an existing admin role from the ARBAC database. The command is valid
     * if and only if the admin role to be deleted is a member of the ADMIN ROLES data set and has been
     * deassigned from all users.
     *
     * @param role Contains role name.
     * @throws com.jts.fortress.SecurityException Thrown in the event of data validation or system error.
     */
    public void deleteRole(AdminRole role)
        throws com.jts.fortress.SecurityException;


    /**

     * Method will update a admin Role entity in the directory.  The role must exist in admin role container prior to this call.
     *
     * @param role Contains role name and new description.
     * @throws com.jts.fortress.SecurityException Thrown in the event of data validation or system error.
     */
    public com.jts.fortress.arbac.AdminRole updateRole(com.jts.fortress.arbac.AdminRole role)
        throws com.jts.fortress.SecurityException;


    /**
     *
     * This command assigns a user to an admin role.
     * Successful completion of this op, the following occurs:
     * </p>
     * <ul>
     * <li> User entity (resides in people container) has role assignment added to aux object class attached to actual user record.
     * <li> AdminRole entity (resides in admin role container) has userId added as role occupant.
     * <li> (optional) Temporal constraints may be associated with <code>ftUserAttrs</code> aux object class based on:
     * <ul>
     * <li> timeout - number in seconds of session inactivity time allowed.
     * <li> beginDate - YYYYMMDD - determines date when role may be activated.
     * <li> endDate - YYMMDD - indicates latest date role may be activated.
     * <li> beginLockDate - YYYYMMDD - determines beginning of enforced inactive status
     * <li> endLockDate - YYMMDD - determines end of enforced inactive status.
     * <li> beginTime - HHMM - determines begin hour role may be activated in user's session.
     * <li> endTime - HHMM - determines end hour role may be activated in user's session.*
     * <li> dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day of week role may be activated.
     * </ul>
     * </ul>
     * @param uAdminRole entity contains {@link com.jts.fortress.rbac.User#userId} and {@link com.jts.fortress.arbac.AdminRole#name} and optional {@code Constraints}..
     * @return AdminRole contains copy of input entity and additional data processed by request.
     * @throws com.jts.fortress.SecurityException in the event data error in user or role objects or system error.
     */
    public void assignUser(com.jts.fortress.arbac.UserAdminRole uAdminRole)
        throws com.jts.fortress.SecurityException;


    /**
     * This method removes assigned admin role from user entity.  Both user and admin role entities must exist and have role relationship
     * before calling this method.
     * Successful completion:
     *      del Role to User assignment in User data set
     * AND
     *      User to Role assignment in Admin Role data set.
     *
     * @param uAdminRole entity contains {@link com.jts.fortress.rbac.User#userId} and {@link com.jts.fortress.arbac.AdminRole#name}.
     * @throws SecurityException - in the event data error in user or role objects or system error.
     */
    public void deassignUser(com.jts.fortress.arbac.UserAdminRole uAdminRole)
        throws SecurityException;

    /**
     * Commands adds a new OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * @param entity contains OrgUnit name and type.
     * @return
     * @throws com.jts.fortress.SecurityException in the event of data validation or system error.
     */
    public OrgUnit add(OrgUnit entity)
        throws com.jts.fortress.SecurityException;

    /**
     * Commands updates existing OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * @param entity contains OrgUnit name and type.
     * @return
     * @throws com.jts.fortress.SecurityException in the event of data validation or system error.
     */
    public com.jts.fortress.arbac.OrgUnit update(com.jts.fortress.arbac.OrgUnit entity)
        throws com.jts.fortress.SecurityException;

    /**
     * Commands deletes existing OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * @param entity contains OrgUnit name and type.
     * @return
     * @throws com.jts.fortress.SecurityException in the event of data validation or system error.
     */
    public com.jts.fortress.arbac.OrgUnit delete(com.jts.fortress.arbac.OrgUnit entity)
        throws com.jts.fortress.SecurityException;


    /**
     * This commands creates a new orgunit child, and inserts it in the orgunit hierarchy as an immediate descendant of
     * the existing orgunit parent.
     * <p>
     * The command is valid if and only if:
     * <ul>
     * <li> The child orgunit is not a member of the ORGUNITS data set.
     * <li> The parent orgunit is a member of the ORGUNITS data set.
     * </ul>
     * </p>
     * <p> This method:
     * <ul>
     * <li> Adds new orgunit.
     * <li> Assigns orgunit relationship between new child and pre-existing parent.
     * </ul>
     * </p>
     * @param parent This entity must be present in ORGUNIT data set.  Success will add rel with child.
     * @param child  This entity must not be present in ORGUNIT data set.  Success will add the new entity to ORGUNIT data set.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addDescendant(com.jts.fortress.arbac.OrgUnit parent, com.jts.fortress.arbac.OrgUnit child)
        throws com.jts.fortress.SecurityException;


    /**
     * This commands creates a new orgunit parent, and inserts it in the orgunit hierarchy as an immediate ascendant of
     * the existing child orgunit.
     * <p>
     * The command is valid if and only if:
     * <ul>
     * <li> The parent is not a member of the ORGUNITS data set.
     * <li> The child is a member of the ORGUNITS data set.
     * </ul>
     * </p>
     * <p> This method:
     * <ul>
     * <li> Adds new orgunit.
     * <li> Assigns orgunit relationship between new parent and pre-existing child.
     * </ul>
     * </p>
     * @param parent completion of op assigns new child relationship with child orgunit.
     * @param child  completion of op assigns new parent relationship with parent orgunit.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addAscendant(com.jts.fortress.arbac.OrgUnit child, OrgUnit parent)
        throws com.jts.fortress.SecurityException;


    /**
     * This commands establishes a new immediate inheritance relationship with parent orgunit <<-- child orgunit
     * <p>
     * The command is valid if and only if:
     * <ul>
     * <li> The parent and child are members of the ORGUNITS data set.
     * <li> The parent is not an immediate ascendant of child.
     * <li> The child does not properly inherit parent (in order to avoid cycle creation).
     * </ul>
     * </p>
     * @param parent completion of op deassigns child relationship with child orgunit.
     * @param child  completion of op deassigns parent relationship with parent orgunit.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addInheritance(com.jts.fortress.arbac.OrgUnit parent, OrgUnit child)
        throws com.jts.fortress.SecurityException;


    /**
     * This command deletes an existing immediate inheritance relationship parent <<-- child.
     * <p>
     * The command is valid if and only if:
     * <ul>
     * <li> The orgunits parent and child are members of the ORGUNITS data set.
     * <li> The parent is an immediate ascendant of child.
     * <li> The new inheritance relation is computed as the reflexive-transitive closure of the immediate inheritance
     * relation resulted after deleting the relationship parent <<-- child.
     * </ul>
     * </p>
     *
     * @param parent completion of op removes child relationship with childRole.
     * @param child  completion of op removes parent relationship with parentRole.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void deleteInheritance(com.jts.fortress.arbac.OrgUnit parent, com.jts.fortress.arbac.OrgUnit child)
        throws com.jts.fortress.SecurityException;


    /**
     * This commands creates a new role childRole, and inserts it in the role hierarchy as an immediate descendant of
     * the existing role parentRole. The command is valid if and only if childRole is not a member of the ADMINROLES data set,
     * and parentRole is a member of the ADMINROLES data set.
     * @param parentRole This entity must be present in ROLE data set.  Success will add role rel with childRole.
     * @param childRole  This entity must not be present in ROLE data set.  Success will add the new role entity to ADMINROLES data set.
     * This method:
     * 1 - Adds new role.
     * 2 - Assigns role relationship between new childRole and pre-existing parentRole.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addDescendant(AdminRole parentRole, com.jts.fortress.arbac.AdminRole childRole)
        throws com.jts.fortress.SecurityException;

    /**
     * This commands creates a new role parentRole, and inserts it in the role hierarchy as an immediate ascendant of
     * the existing role childRole. The command is valid if and only if parentRole is not a member of the ADMINROLES data set,
     * and childRole is a member of the ADMINROLES data set.
     * This method:
     * 1 - Adds new role.
     * 2 - Assigns role relationship between new parentRole and pre-existing childRole.
     * @param parentRole completion of op assigns new child relationship with childRole.
     * @param childRole  completion of op assigns new parent relationship with parentRole.
     * @throws SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addAscendant(AdminRole childRole, AdminRole parentRole)
        throws SecurityException;

    /**
     * This commands establishes a new immediate inheritance relationship parentRole <<-- childRole between existing
     * roles parentRole, childRole. The command is valid if and only if parentRole and childRole are members of the ADMINROLES data
     * set, parentRole is not an immediate ascendant of childRole, and childRole does not properly inherit parentRole (in order to
     * avoid cycle creation).
     * @param parentRole completion of op deassigns child relationship with childRole.
     * @param childRole  completion of op deassigns parent relationship with parentRole.
     * @throws com.jts.fortress.SecurityException thrown in the event of data validation or system error.
     */
    public void addInheritance(com.jts.fortress.arbac.AdminRole parentRole, AdminRole childRole)
        throws com.jts.fortress.SecurityException;

    /**
     * This command deletes an existing immediate inheritance relationship parentRole <<-- childRole. The command is
     * valid if and only if the roles parentRole and childRole are members of the ADMINROLES data set, and parentRole is an
     * immediate ascendant of childRole. The new inheritance relation is computed as the reflexive-transitive
     * closure of the immediate inheritance relation resulted after deleting the relationship parentRole <<-- childRole.
     * @param parentRole completion of op removes child relationship with childRole.
     * @param childRole  completion of op removes parent relationship with parentRole.
     * @throws com.jts.fortress.SecurityException thrown in the event of data validation or system error.
     */
    public void deleteInheritance(AdminRole parentRole, AdminRole childRole)
        throws com.jts.fortress.SecurityException;
}