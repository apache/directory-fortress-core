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
package org.openldap.fortress;

import org.openldap.fortress.rbac.AdminRole;
import org.openldap.fortress.rbac.OrgUnit;
import org.openldap.fortress.rbac.UserAdminRole;
import org.openldap.fortress.rbac.PermObj;
import org.openldap.fortress.rbac.Permission;
import org.openldap.fortress.rbac.User;

/**
 * This class prescribes the ARBAC02 DelAdminMgr interface for performing policy administration of Fortress ARBAC entities
 * that reside in LDAP directory.
 * These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification describes delegated administrative
 * operations for the creation and maintenance of ARBAC element sets and relations.  Delegated administrative review functions for performing administrative queries
 * and system functions for creating and managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 * <p/>
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="./doc-files/ARbac.png">
 * <p/>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without sacrificing regulations for accountability or traceability.
 * <p/>
 * This interface's implementer will NOT be thread safe if parent instance variables ({@link Manageable#setContextId(String)} or {@link Manageable#setAdmin(org.openldap.fortress.rbac.Session)}) are set.
 *
 * @author Shawn McKinney
 */
public interface DelAdminMgr extends Manageable
{
    /**
     * This command creates a new admin role. The command is valid if and only if the new admin role is not
     * already a member of the ADMIN ROLES data set. The ADMIN ROLES data set is updated.
     * Initially, no user or permission is assigned to the new role.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link AdminRole#name} - contains the name of the new AdminRole being targeted for addition to LDAP</li>
     * </ul>
     * <p/>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link AdminRole#description} - contains any safe text</li>
     * <li>{@link AdminRole#osPs} * - multi-occurring attribute used to set associations to existing PERMS OrgUnits</li>
     * <li>{@link AdminRole#osUs} * - multi-occurring attribute used to set associations to existing USERS OrgUnits</li>
     * <li>{@link AdminRole#beginRange} - contains the name of an existing RBAC Role that represents the lowest role in hierarchy that administrator (whoever has this AdminRole activated) controls</li>
     * <li>{@link AdminRole#endRange} - contains the name of an existing RBAC Role that represents that highest role in hierarchy that administrator may control</li>
     * <li>{@link AdminRole#beginInclusive} - if 'true' the RBAC Role specified in beginRange is also controlled by the posessor of this AdminRole</li>
     * <li>{@link AdminRole#endInclusive} - if 'true' the RBAC Role specified in endRange is also controlled by the administratrator</li>
     * <li>{@link AdminRole#beginTime} - HHMM - determines begin hour adminRole may be activated into user's ARBAC session</li>
     * <li>{@link AdminRole#endTime} - HHMM - determines end hour adminRole may be activated into user's ARBAC session.</li>
     * <li>{@link AdminRole#beginDate} - YYYYMMDD - determines date when adminRole may be activated into user's ARBAC session</li>
     * <li>{@link AdminRole#endDate} - YYYYMMDD - indicates latest date adminRole may be activated into user's ARBAC session</li>
     * <li>{@link AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     * <li>{@link AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     * <li>{@link AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's ARBAC session</li>
     * </ul>
     *
     * @param role Contains role name and description.
     * @return AdminRole contains reference to entity added.
     * @throws org.openldap.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public AdminRole addRole(AdminRole role)
        throws org.openldap.fortress.SecurityException;


    /**
     * This command deletes an existing admin role from the ARBAC database. The command is valid
     * if and only if the role to be deleted is a member of the ADMIN ROLES data set.  This command will
     * also deassign role from all users.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link AdminRole#name} - contains the name of the new AdminRole being targeted for removal</li>
     * </ul>
     * <p/>
     *
     * @param role Contains role name.
     * @throws org.openldap.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    public void deleteRole(AdminRole role)
        throws org.openldap.fortress.SecurityException;


    /**
     * Method will update an AdminRole entity in the directory.  The role must exist in directory prior to this call.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link AdminRole#name} - contains the name of the new AdminRole being targeted for updating</li>
     * </ul>
     * <p/>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link AdminRole#description} - contains any safe text</li>
     * <li>{@link AdminRole#osPs} * - multi-occurring attribute used to set associations to existing PERMS OrgUnits</li>
     * <li>{@link AdminRole#osUs} * - multi-occurring attribute used to set associations to existing USERS OrgUnits</li>
     * <li>{@link AdminRole#beginRange} - contains the name of an existing RBAC Role that represents the lowest role in hierarchy that administrator (whoever has this AdminRole activated) controls</li>
     * <li>{@link AdminRole#endRange} - contains the name of an existing RBAC Role that represents that highest role in hierarchy that administrator may control</li>
     * <li>{@link AdminRole#beginInclusive} - if 'true' the RBAC Role specified in beginRange is also controlled by the posessor of this AdminRole</li>
     * <li>{@link AdminRole#endInclusive} - if 'true' the RBAC Role specified in endRange is also controlled by the administratrator</li>
     * <li>{@link AdminRole#beginTime} - HHMM - determines begin hour adminRole may be activated into user's ARBAC session</li>
     * <li>{@link AdminRole#endTime} - HHMM - determines end hour adminRole may be activated into user's ARBAC session.</li>
     * <li>{@link AdminRole#beginDate} - YYYYMMDD - determines date when adminRole may be activated into user's ARBAC session</li>
     * <li>{@link AdminRole#endDate} - YYYYMMDD - indicates latest date adminRole may be activated into user's ARBAC session</li>
     * <li>{@link AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     * <li>{@link AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     * <li>{@link AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's ARBAC session</li>
     * </ul>
     *
     * @param role Contains role name and new description.
     * @return AdminRole contains reference to entity operated on.
     * @throws org.openldap.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    public AdminRole updateRole(AdminRole role)
        throws org.openldap.fortress.SecurityException;


    /**
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
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link UserAdminRole#name} - contains the name for already existing AdminRole to be assigned</li>
     * <li>{@link UserAdminRole#userId} - contains the userId for existing User</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link UserAdminRole#beginTime} - HHMM - determines begin hour AdminRole may be activated into user's RBAC session</li>
     * <li>{@link UserAdminRole#endTime} - HHMM - determines end hour AdminRole may be activated into user's RBAC session.</li>
     * <li>{@link UserAdminRole#beginDate} - YYYYMMDD - determines date when AdminRole may be activated into user's RBAC session</li>
     * <li>{@link UserAdminRole#endDate} - YYYYMMDD - indicates latest date AdminRole may be activated into user's RBAC session</li>
     * <li>{@link UserAdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     * <li>{@link UserAdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     * <li>{@link UserAdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's ARBAC session</li>
     * </ul>
     *
     * @param uAdminRole entity contains {@link org.openldap.fortress.rbac.User#userId} and {@link org.openldap.fortress.rbac.AdminRole#name} and optional {@code Constraints}..
     * @throws org.openldap.fortress.SecurityException
     *          in the event data error in user or role objects or system error.
     */
    public void assignUser(UserAdminRole uAdminRole)
        throws org.openldap.fortress.SecurityException;


    /**
     * This method removes assigned admin role from user entity.  Both user and admin role entities must exist and have role relationship
     * before calling this method.
     * Successful completion:
     * del Role to User assignment in User data set
     * AND
     * User to Role assignment in Admin Role data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.UserAdminRole#name} - contains the name for already existing AdminRole to be deassigned</li>
     * <li>{@link org.openldap.fortress.rbac.UserAdminRole#userId} - contains the userId for existing User</li>
     * </ul>
     *
     * @param uAdminRole entity contains {@link org.openldap.fortress.rbac.User#userId} and {@link org.openldap.fortress.rbac.AdminRole#name}.
     * @throws SecurityException - in the event data error in user or role objects or system error.
     */
    public void deassignUser(UserAdminRole uAdminRole)
        throws SecurityException;

    /**
     * Commands adds a new OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit to be added</li>
     * <li>{@link org.openldap.fortress.rbac.OrgUnit#type} - contains the type of OU:  {@link org.openldap.fortress.rbac.OrgUnit.Type#USER} or {@link org.openldap.fortress.rbac.OrgUnit.Type#PERM}</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.OrgUnit#description} - contains any safe text</li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return OrgUnit contains reference to entity added.
     * @throws org.openldap.fortress.SecurityException
     *          in the event of data validation or system error.
     */
    public OrgUnit add(OrgUnit entity)
        throws org.openldap.fortress.SecurityException;

    /**
     * Commands updates existing OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit to be updated</li>
     * <li>{@link org.openldap.fortress.rbac.OrgUnit#type} - contains the type of OU:  {@link org.openldap.fortress.rbac.OrgUnit.Type#USER} or {@link org.openldap.fortress.rbac.OrgUnit.Type#PERM}</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.OrgUnit#description} - contains any safe text</li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return OrgUnit contains reference to entity operated on.
     * @throws org.openldap.fortress.SecurityException
     *          in the event of data validation or system error.
     */
    public OrgUnit update(OrgUnit entity)
        throws org.openldap.fortress.SecurityException;

    /**
     * Commands deletes existing OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit to be removed</li>
     * <li>{@link org.openldap.fortress.rbac.OrgUnit#type} - contains the type of OU:  {@link org.openldap.fortress.rbac.OrgUnit.Type#USER} or {@link org.openldap.fortress.rbac.OrgUnit.Type#PERM}</li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return OrgUnit contains reference to entity operated on.
     * @throws org.openldap.fortress.SecurityException
     *          in the event of data validation or system error.
     */
    public OrgUnit delete(OrgUnit entity)
        throws org.openldap.fortress.SecurityException;


    /**
     * This command creates a new orgunit child, and inserts it in the orgunit hierarchy as an immediate descendant of
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
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link org.openldap.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to be parent</li>
     * <li>parentRole - {@link org.openldap.fortress.rbac.OrgUnit#type} - contains the type of OrgUnit targeted: {@link org.openldap.fortress.rbac.OrgUnit.Type#USER} or {@link org.openldap.fortress.rbac.OrgUnit.Type#PERM}</li>
     * <li>childRole - {@link org.openldap.fortress.rbac.OrgUnit#name} - contains the name of new OrgUnit to be child</li>
     * </ul>
     * <h4>optional parameters child</h4>
     * <ul>
     * <li>childRole - {@link org.openldap.fortress.rbac.OrgUnit#description} - maps to description attribute on organizationalUnit object class for new child</li>
     * </ul>
     *
     * @param parent This entity must be present in ORGUNIT data set.  Success will add rel with child.
     * @param child  This entity must not be present in ORGUNIT data set.  Success will add the new entity to ORGUNIT data set.
     * @throws org.openldap.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addDescendant(OrgUnit parent, OrgUnit child)
        throws org.openldap.fortress.SecurityException;


    /**
     * This command creates a new orgunit parent, and inserts it in the orgunit hierarchy as an immediate ascendant of
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
     * <h4>required parameters</h4>
     * <ul>
     * <li>child - {@link org.openldap.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to be parent</li>
     * <li>parent - {@link org.openldap.fortress.rbac.OrgUnit#type} - contains the type of OrgUnit targeted: {@link org.openldap.fortress.rbac.OrgUnit.Type#USER} or {@link org.openldap.fortress.rbac.OrgUnit.Type#PERM}</li>
     * <li>parent - {@link org.openldap.fortress.rbac.OrgUnit#name} - contains the name of new OrgUnit to be child</li>
     * </ul>
     * <h4>optional parameters child</h4>
     * <ul>
     * <li>parent - {@link OrgUnit#description} - maps to description attribute on organizationalUnit object class for new child</li>
     * </ul>
     *
     * @param parent completion of op assigns new child relationship with child orgunit.
     * @param child  completion of op assigns new parent relationship with parent orgunit.
     * @throws org.openldap.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addAscendant(OrgUnit child, OrgUnit parent)
        throws org.openldap.fortress.SecurityException;


    /**
     * This command establishes a new immediate inheritance relationship with parent orgunit <<-- child orgunit
     * <p/>
     * The command is valid if and only if:
     * <ul>
     * <li> The parent and child are members of the ORGUNITS data set.
     * <li> The parent is not an immediate ascendant of child.
     * <li> The child does not properly inherit parent (in order to avoid cycle creation).
     * </ul>
     * <h4>required parameters</h4>
     * <ul>
     * <li>parent - {@link org.openldap.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to be parent</li>
     * <li>parent - {@link org.openldap.fortress.rbac.OrgUnit#type} - contains the type of OrgUnit targeted: {@link org.openldap.fortress.rbac.OrgUnit.Type#USER} or {@link org.openldap.fortress.rbac.OrgUnit.Type#PERM}</li>
     * <li>child - {@link org.openldap.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to be child</li>
     * </ul>
     *
     * @param parent completion of op deassigns child relationship with child orgunit.
     * @param child  completion of op deassigns parent relationship with parent orgunit.
     * @throws org.openldap.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addInheritance(OrgUnit parent, OrgUnit child)
        throws org.openldap.fortress.SecurityException;


    /**
     * This command deletes an existing immediate inheritance relationship parent <<-- child.
     * <p/>
     * The command is valid if and only if:
     * <ul>
     * <li> The orgunits parent and child are members of the ORGUNITS data set.
     * <li> The parent is an immediate ascendant of child.
     * <li> The new inheritance relation is computed as the reflexive-transitive closure of the immediate inheritance
     * relation resulted after deleting the relationship parent <<-- child.
     * </ul>
     * <h4>required parameters</h4>
     * <ul>
     * <li>parent - {@link org.openldap.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to remove as parent</li>
     * <li>parent - {@link org.openldap.fortress.rbac.OrgUnit#type} - contains the type of OrgUnit targeted: {@link org.openldap.fortress.rbac.OrgUnit.Type#USER} or {@link org.openldap.fortress.rbac.OrgUnit.Type#PERM}</li>
     * <li>child - {@link org.openldap.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to remove as child</li>
     * </ul>
     *
     * @param parent completion of op removes child relationship with childRole.
     * @param child  completion of op removes parent relationship with parentRole.
     * @throws org.openldap.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void deleteInheritance(OrgUnit parent, OrgUnit child)
        throws org.openldap.fortress.SecurityException;


    /**
     * This command creates a new role childRole, and inserts it in the role hierarchy as an immediate descendant of
     * the existing role parentRole. The command is valid if and only if childRole is not a member of the ADMINROLES data set,
     * and parentRole is a member of the ADMINROLES data set.
     * <p/>
     * This method:
     * 1 - Adds new role.
     * 2 - Assigns role relationship between new childRole and pre-existing parentRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link org.openldap.fortress.rbac.AdminRole#name} - contains the name of existing Role to be parent</li>
     * <li>childRole - {@link org.openldap.fortress.rbac.AdminRole#name} - contains the name of new Role to be child</li>
     * </ul>
     * <h4>optional parameters childRole</h4>
     * <ul>
     * <li>childRole - {@link org.openldap.fortress.rbac.AdminRole#description} - maps to description attribute on organizationalRole object class for new child</li>
     * <li>childRole - {@link org.openldap.fortress.rbac.AdminRole#beginTime} - HHMM - determines begin hour role may be activated into user's session for new child</li>
     * <li>childRole - {@link org.openldap.fortress.rbac.AdminRole#endTime} - HHMM - determines end hour role may be activated into user's session for new child</li>
     * <li>childRole - {@link org.openldap.fortress.rbac.AdminRole#beginDate} - YYYYMMDD - determines date when role may be activated into user's session for new child</li>
     * <li>childRole - {@link org.openldap.fortress.rbac.AdminRole#endDate} - YYYYMMDD - indicates latest date role may be activated into user's session for new child</li>
     * <li>childRole - {@link org.openldap.fortress.rbac.AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status for new child</li>
     * <li>childRole - {@link org.openldap.fortress.rbac.AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status for new child</li>
     * <li>childRole - {@link org.openldap.fortress.rbac.AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's session for new child</li>
     * </ul>
     *
     * @param parentRole This entity must be present in ADMINROLES data set.  Success will add role rel with childRole.
     * @param childRole  This entity must not be present in ADMINROLES data set.  Success will add the new role entity to ADMINROLES data set.
     * @throws org.openldap.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addDescendant(AdminRole parentRole, AdminRole childRole)
        throws org.openldap.fortress.SecurityException;

    /**
     * This command creates a new role parentRole, and inserts it in the role hierarchy as an immediate ascendant of
     * the existing role childRole. The command is valid if and only if parentRole is not a member of the ADMINROLES data set,
     * and childRole is a member of the ADMINROLES data set.
     * This method:
     * 1 - Adds new role.
     * 2 - Assigns role relationship between new parentRole and pre-existing childRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>childRole - {@link org.openldap.fortress.rbac.AdminRole#name} - contains the name of existing Role to be child</li>
     * <li>parentRole - {@link org.openldap.fortress.rbac.AdminRole#name} - contains the name of new Role to be added as parent</li>
     * </ul>
     * <h4>optional parameters parentRole</h4>
     * <ul>
     * <li>parentRole - {@link org.openldap.fortress.rbac.AdminRole#description} - maps to description attribute on organizationalRole object class for new parent</li>
     * <li>parentRole - {@link org.openldap.fortress.rbac.AdminRole#beginTime} - HHMM - determines begin hour role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link org.openldap.fortress.rbac.AdminRole#endTime} - HHMM - determines end hour role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link org.openldap.fortress.rbac.AdminRole#beginDate} - YYYYMMDD - determines date when role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link org.openldap.fortress.rbac.AdminRole#endDate} - YYYYMMDD - indicates latest date role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link org.openldap.fortress.rbac.AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status for new parent</li>
     * <li>parentRole - {@link org.openldap.fortress.rbac.AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status for new parent</li>
     * <li>parentRole - {@link org.openldap.fortress.rbac.AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's session for new parent</li>
     * </ul>
     *
     * @param parentRole completion of op assigns new child relationship with childRole.
     * @param childRole  completion of op assigns new parent relationship with parentRole.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    public void addAscendant(AdminRole childRole, AdminRole parentRole)
        throws SecurityException;

    /**
     * This command establishes a new immediate inheritance relationship parentRole <<-- childRole between existing
     * roles parentRole, childRole. The command is valid if and only if parentRole and childRole are members of the ADMINROLES data
     * set, parentRole is not an immediate ascendant of childRole, and childRole does not properly inherit parentRole (in order to
     * avoid cycle creation).
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link org.openldap.fortress.rbac.AdminRole#name} - contains the name of existing AdminRole to be parent</li>
     * <li>childRole - {@link org.openldap.fortress.rbac.AdminRole#name} - contains the name of existing AdminRole to be child</li>
     * </ul>
     *
     * @param parentRole completion of op deassigns child relationship with childRole.
     * @param childRole  completion of op deassigns parent relationship with parentRole.
     * @throws org.openldap.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addInheritance(AdminRole parentRole, AdminRole childRole)
        throws org.openldap.fortress.SecurityException;

    /**
     * This command deletes an existing immediate inheritance relationship parentRole <<-- childRole. The command is
     * valid if and only if the roles parentRole and childRole are members of the ADMINROLES data set, and parentRole is an
     * immediate ascendant of childRole. The new inheritance relation is computed as the reflexive-transitive
     * closure of the immediate inheritance relation resulted after deleting the relationship parentRole <<-- childRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link org.openldap.fortress.rbac.AdminRole#name} - contains the name of existing AdminRole to remove as parent</li>
     * <li>childRole - {@link org.openldap.fortress.rbac.AdminRole#name} - contains the name of existing AdminRole to remove as child</li>
     * </ul>
     *
     * @param parentRole completion of op removes child relationship with childRole.
     * @param childRole  completion of op removes parent relationship with parentRole.
     * @throws org.openldap.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void deleteInheritance(AdminRole parentRole, AdminRole childRole)
        throws org.openldap.fortress.SecurityException;


    /**
     * This method will add an administrative permission operation to an existing permission object which resides under {@code ou=AdminPerms,ou=ARBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may have {@link org.openldap.fortress.rbac.AdminRole} or {@link org.openldap.fortress.rbac.User} associations.  The target {@link org.openldap.fortress.rbac.Permission} must not exist prior to calling.
     * A Fortress Permission instance exists in a hierarchical, one-many relationship between its parent and itself as stored in ldap tree: ({@link org.openldap.fortress.rbac.PermObj}*->{@link org.openldap.fortress.rbac.Permission}).
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.Permission#objName} - contains the name of existing object being targeted for the permission add</li>
     * <li>{@link org.openldap.fortress.rbac.Permission#opName} - contains the name of new permission operation being added</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link Permission#roles} * - multi occurring attribute contains RBAC Roles that permission operation is being granted to</li>
     * <li>{@link Permission#users} * - multi occurring attribute contains Users that permission operation is being granted to</li>
     * <li>{@link Permission#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * <li>{@link Permission#type} - any safe text</li>
     * </ul>
     *
     * @param perm must contain the object, {@link org.openldap.fortress.rbac.Permission#objName}, and operation, {@link Permission#opName}, that identifies target along with optional other attributes..
     * @return copy of Permission entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    public Permission addPermission(Permission perm)
        throws org.openldap.fortress.SecurityException;


    /**
     * This method will update administrative permission operation pre-existing in target directory under {@code ou=AdminPerms,ou=ARBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may also contain {@link org.openldap.fortress.rbac.AdminRole} or {@link org.openldap.fortress.rbac.User} associations to add or remove using this function.
     * The perm operation must exist before making this call.  Only non-null attributes will be updated.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.Permission#objName} - contains the name of existing object being targeted for the permission update</li>
     * <li>{@link org.openldap.fortress.rbac.Permission#opName} - contains the name of existing permission operation being updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.Permission#roles} * - multi occurring attribute contains RBAC Roles that permission operation is being granted to</li>
     * <li>{@link org.openldap.fortress.rbac.Permission#users} * - multi occurring attribute contains Users that permission operation is being granted to</li>
     * <li>{@link org.openldap.fortress.rbac.Permission#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * <li>{@link org.openldap.fortress.rbac.Permission#type} - any safe text</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName}, that identifies target and any optional data to update.  Null or empty attributes will be ignored.
     * @return copy of Permission entity.
     * @throws org.openldap.fortress.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    public Permission updatePermission(Permission perm)
        throws org.openldap.fortress.SecurityException;


    /**
     * This method will remove administrative permission operation entity from permission object. A Fortress permission is (object->operation).
     * The perm operation must exist before making this call.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.Permission#objName} - contains the name of existing object being targeted for the permission delete</li>
     * <li>{@link org.openldap.fortress.rbac.Permission#opName} - contains the name of existing permission operation being removed</li>
     * </ul>
     *
     * @param perm must contain the object, {@link org.openldap.fortress.rbac.Permission#objName}, and operation, {@link org.openldap.fortress.rbac.Permission#opName}, that identifies target.
     * @throws org.openldap.fortress.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    public void deletePermission(Permission perm)
        throws SecurityException;


    /**
     * This method will add administrative permission object to admin perms container in directory. The perm object must not exist before making this call.
     * A {@link org.openldap.fortress.rbac.PermObj} instance exists in a hierarchical, one-many relationship between itself and children as stored in ldap tree: ({@link org.openldap.fortress.rbac.PermObj}*->{@link org.openldap.fortress.rbac.Permission}).
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.PermObj#objName} - contains the name of new object being added</li>
     * <li>{@link org.openldap.fortress.rbac.PermObj#ou} - contains the name of an existing PERMS OrgUnit this object is associated with</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.PermObj#description} - any safe text</li>
     * <li>{@link org.openldap.fortress.rbac.PermObj#type} - contains any safe text</li>
     * <li>{@link org.openldap.fortress.rbac.PermObj#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * </ul>
     *
     * @param pObj must contain the {@link org.openldap.fortress.rbac.PermObj#objName} and {@link org.openldap.fortress.rbac.PermObj#ou}.  The other attributes are optional.
     * @return copy of PermObj entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    public PermObj addPermObj(PermObj pObj)
        throws SecurityException;


    /**
     * This method will update administrative permission object in perms container in directory.  The perm object must exist before making this call.
     * A {@link org.openldap.fortress.rbac.PermObj} instance exists in a hierarchical, one-many relationship between itself and children as stored in ldap tree: ({@link org.openldap.fortress.rbac.PermObj}*->{@link org.openldap.fortress.rbac.Permission}).
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.PermObj#objName} - contains the name of existing object being updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.PermObj#ou} - contains the name of an existing PERMS OrgUnit this object is associated with</li>
     * <li>{@link org.openldap.fortress.rbac.PermObj#description} - any safe text</li>
     * <li>{@link org.openldap.fortress.rbac.PermObj#type} - contains any safe text</li>
     * <li>{@link org.openldap.fortress.rbac.PermObj#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * </ul>
     *
     * @param pObj must contain the {@link org.openldap.fortress.rbac.PermObj#objName}. Only non-null attributes will be updated.
     * @return copy of newly updated PermObj entity.
     * @throws org.openldap.fortress.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    public PermObj updatePermObj(PermObj pObj)
        throws org.openldap.fortress.SecurityException;


    /**
     * This method will remove administrative permission object from perms container in directory.  This method will also remove
     * in associated permission objects that are attached to this object.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.PermObj#objName} - contains the name of existing object targeted for removal</li>
     * </ul>
     *
     * @param pObj must contain the {@link org.openldap.fortress.rbac.PermObj#objName} of object targeted for removal.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    public void deletePermObj(PermObj pObj)
        throws SecurityException;


    /**
     * This command grants an AdminRole the administrative permission to perform an operation on an object to a role.
     * The command is implemented by granting administrative permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * and the adminRole is a member of the ADMIN_ROLES data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.Permission#objName} - contains the object name</li>
     * <li>{@link org.openldap.fortress.rbac.Permission#opName} - contains the operation name</li>
     * <li>{@link org.openldap.fortress.rbac.AdminRole#name} - contains the adminRole name</li>
     * </ul>
     *
     * @param perm must contain the object, {@link org.openldap.fortress.rbac.Permission#objName}, and operation, {@link org.openldap.fortress.rbac.Permission#opName}, that identifies target.
     * @param role must contains {@link org.openldap.fortress.rbac.AdminRole#name}.
     * @throws org.openldap.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    public void grantPermission(Permission perm, AdminRole role)
        throws org.openldap.fortress.SecurityException;


    /**
     * This command revokes the administrative permission to perform an operation on an object from the set
     * of permissions assigned to an AdminRole. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * the role is a member of the ADMIN_ROLES data set, and the permission is assigned to that AdminRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.Permission#objName} - contains the object name</li>
     * <li>{@link org.openldap.fortress.rbac.Permission#opName} - contains the operation name</li>
     * <li>{@link org.openldap.fortress.rbac.AdminRole#name} - contains the adminRole name</li>
     * </ul>
     *
     * @param perm must contain the object, {@link org.openldap.fortress.rbac.Permission#objName}, and operation, {@link org.openldap.fortress.rbac.Permission#opName}, that identifies target.
     * @param role must contains {@link org.openldap.fortress.rbac.AdminRole#name}.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    public void revokePermission(Permission perm, AdminRole role)
        throws org.openldap.fortress.SecurityException;


    /**
     * This command grants a user the administrative permission to perform an operation on an object to a user.
     * The command is implemented by granting administrative permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents an administrative permission,
     * and the user is a member of the USERS data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.Permission#objName} - contains the object name</li>
     * <li>{@link org.openldap.fortress.rbac.Permission#opName} - contains the operation name</li>
     * <li>{@link org.openldap.fortress.rbac.User#userId} - contains the userId</li>
     * </ul>
     *
     * @param perm must contain the object, {@link org.openldap.fortress.rbac.Permission#objName}, and operation, {@link org.openldap.fortress.rbac.Permission#opName}, that identifies target.
     * @param user must contain {@link org.openldap.fortress.rbac.User#userId} of target User entity.
     * @throws org.openldap.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    public void grantPermission(Permission perm, User user)
        throws SecurityException;


    /**
     * This command revokes the administrative permission to perform an operation on an object from the set
     * of permissions assigned to a user. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents an administrative permission,
     * the user is a member of the USERS data set, and the permission is assigned to that user.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.openldap.fortress.rbac.Permission#objName} - contains the object name</li>
     * <li>{@link org.openldap.fortress.rbac.Permission#opName} - contains the operation name</li>
     * <li>{@link org.openldap.fortress.rbac.User#userId} - contains the userId</li>
     * </ul>
     *
     * @param perm must contain the object, {@link org.openldap.fortress.rbac.Permission#objName}, and operation, {@link org.openldap.fortress.rbac.Permission#opName}, that identifies target.
     * @param user must contain {@link org.openldap.fortress.rbac.User#userId} of target User entity.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    public void revokePermission(Permission perm, User user)
        throws org.openldap.fortress.SecurityException;

}