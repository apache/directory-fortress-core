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
package org.apache.directory.fortress.core;


import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.model.UserAdminRole;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.User;


/**
 * This class prescribes the ARBAC02 DelAdminMgr interface for performing policy administration of Fortress ARBAC entities
 * that reside in LDAP directory.
 * These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification 
 * describes delegated administrative operations for the creation and maintenance of ARBAC element sets and relations.  
 * Delegated administrative review functions for performing administrative queries and system functions for creating and 
 * managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="./doc-files/ARbac.png" alt="">
 * <p>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises 
 * the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without 
 * sacrificing regulations for accountability or traceability.
 * <p>
 * This interface's implementer will NOT be thread safe if parent instance variables ({@link Manageable#setContextId(String)} 
 * or {@link Manageable#setAdmin(org.apache.directory.fortress.core.model.Session)}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface DelAdminMgr extends Manageable
{
    /**
     * This command creates a new admin role. The command is valid if and only if the new admin role is not
     * already a member of the ADMIN ROLES data set. The ADMIN ROLES data set is updated.
     * Initially, no user or permission is assigned to the new role.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link AdminRole#name} - contains the name of the new AdminRole being targeted for addition to LDAP</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>{@link AdminRole#description} - contains any safe text</li>
     *   <li>{@link AdminRole#osPs} * - multivalued attribute used to set associations to existing PERMS OrgUnits</li>
     *   <li>{@link AdminRole#osUs} * - multivalued attribute used to set associations to existing USERS OrgUnits</li>
     *   <li>
     *     {@link AdminRole#beginRange} - contains the name of an existing RBAC Role that represents the lowest role in 
     *     hierarchy that administrator (whoever has this AdminRole activated) controls
     *   </li>
     *   <li>
     *     {@link AdminRole#endRange} - contains the name of an existing RBAC Role that represents that highest role in 
     *     hierarchy that administrator may control
     *   </li>
     *   <li>
     *     {@link AdminRole#beginInclusive} - if 'true' the RBAC Role specified in beginRange is also controlled by the 
     *     possessor of this AdminRole
     *   </li>
     *   <li>
     *     {@link AdminRole#endInclusive} - if 'true' the RBAC Role specified in endRange is also controlled by the 
     *     administrator
     *   </li>
     *   <li>
     *     {@link AdminRole#beginTime} - HHMM - determines begin hour adminRole may be activated into user's ARBAC session
     *   </li>
     *   <li>
     *     {@link AdminRole#endTime} - HHMM - determines end hour adminRole may be activated into user's ARBAC session.
     *   </li>
     *   <li>
     *     {@link AdminRole#beginDate} - YYYYMMDD - determines date when adminRole may be activated into user's ARBAC 
     *     session
     *   </li>
     *   <li>
     *     {@link AdminRole#endDate} - YYYYMMDD - indicates latest date adminRole may be activated into user's ARBAC 
     *     session
     *   </li>
     *   <li>{@link AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     *   <li>{@link AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     *   <li>{@link AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated 
     *   into user's ARBAC session</li>
     * </ul>
     *
     * @param role Contains role name and description.
     * @return AdminRole contains reference to entity added.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    AdminRole addRole( AdminRole role )
        throws SecurityException;


    /**
     * This command deletes an existing admin role from the ARBAC database. The command is valid
     * if and only if the role to be deleted is a member of the ADMIN ROLES data set.  This command will
     * also deassign role from all users.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link AdminRole#name} - contains the name of the new AdminRole being targeted for removal</li>
     * </ul>
     *
     * @param role Contains role name.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    void deleteRole( AdminRole role )
        throws SecurityException;


    /**
     * Method will update an AdminRole entity in the directory.  The role must exist in directory prior to this call.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link AdminRole#name} - contains the name of the new AdminRole being targeted for updating</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>{@link AdminRole#description} - contains any safe text</li>
     *   <li>{@link AdminRole#osPs} * - multivalued attribute used to set associations to existing PERMS OrgUnits</li>
     *   <li>{@link AdminRole#osUs} * - multivalued attribute used to set associations to existing USERS OrgUnits</li>
     *   <li>
     *     {@link AdminRole#beginRange} - contains the name of an existing RBAC Role that represents the lowest role in 
     *     hierarchy that administrator (whoever has this AdminRole activated) controls
     *   </li>
     *   <li>
     *     {@link AdminRole#endRange} - contains the name of an existing RBAC Role that represents that highest role in 
     *     hierarchy that administrator may control
     *   </li>
     *   <li>
     *     {@link AdminRole#beginInclusive} - if 'true' the RBAC Role specified in beginRange is also controlled by the 
     *     possessor of this AdminRole
     *   </li>
     *   <li>
     *     {@link AdminRole#endInclusive} - if 'true' the RBAC Role specified in endRange is also controlled by the 
     *     administrator
     *   </li>
     *   <li>
     *     {@link AdminRole#beginTime} - HHMM - determines begin hour adminRole may be activated into user's ARBAC session
     *   </li>
     *   <li>
     *     {@link AdminRole#endTime} - HHMM - determines end hour adminRole may be activated into user's ARBAC session.
     *   </li>
     *   <li>
     *     {@link AdminRole#beginDate} - YYYYMMDD - determines date when adminRole may be activated into user's ARBAC session
     *   </li>
     *   <li>
     *     {@link AdminRole#endDate} - YYYYMMDD - indicates latest date adminRole may be activated into user's ARBAC session
     *   </li>
     *   <li>{@link AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     *   <li>{@link AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     *   <li>
     *     {@link AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into 
     *     user's ARBAC session
     *   </li>
     * </ul>
     *
     * @param role Contains role name and new description.
     * @return AdminRole contains reference to entity operated on.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    AdminRole updateRole( AdminRole role )
        throws SecurityException;


    /**
     * This command assigns a user to an admin role.
     * Successful completion of this op, the following occurs:
     * <ul>
     *   <li>
     *     User entity (resides in people container) has role assignment added to aux object class attached to actual user 
     *     record.
     *   </li>
     *   <li>AdminRole entity (resides in admin role container) has userId added as role occupant.</li>
     *   <li>(optional) Temporal constraints may be associated with <code>ftUserAttrs</code> aux object class based on:
     *     <ul>
     *       <li>timeout - number (in minutes) of session inactivity time allowed.</li>
     *       <li>beginDate - YYYYMMDD - determines date when role may be activated.</li>
     *       <li>endDate - YYMMDD - indicates latest date role may be activated.</li>
     *       <li>beginLockDate - YYYYMMDD - determines beginning of enforced inactive status</li>
     *       <li>endLockDate - YYMMDD - determines end of enforced inactive status.</li>
     *       <li>beginTime - HHMM - determines begin hour role may be activated in user's session.</li>
     *       <li>endTime - HHMM - determines end hour role may be activated in user's session.*</li>
     *       <li>dayMask - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day of week role may be activated.</li>
     *     </ul>
     *   </li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link UserAdminRole#name} - contains the name for already existing AdminRole to be assigned</li>
     *   <li>{@link UserAdminRole#userId} - contains the userId for existing User</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>
     *     {@link UserAdminRole#beginTime} - HHMM - determines begin hour AdminRole may be activated into user's RBAC session
     *   </li>
     *   <li>
     *     {@link UserAdminRole#endTime} - HHMM - determines end hour AdminRole may be activated into user's RBAC session.
     *   </li>
     *   <li>
     *     {@link UserAdminRole#beginDate} - YYYYMMDD - determines date when AdminRole may be activated into user's RBAC 
     *     session
     *   </li>
     *   <li>
     *     {@link UserAdminRole#endDate} - YYYYMMDD - indicates latest date AdminRole may be activated into user's RBAC 
     *     session
     *   </li>
     *   <li>{@link UserAdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     *   <li>{@link UserAdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     *   <li>
     *     {@link UserAdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated 
     *     into user's ARBAC session
     *   </li>
     * </ul>
     *
     * @param uAdminRole entity contains {@link org.apache.directory.fortress.core.model.User#userId} and 
     * {@link org.apache.directory.fortress.core.model.AdminRole#name} and optional {@code Constraints}..
     * @throws SecurityException in the event data error in user or role objects or system error.
     */
    void assignUser( UserAdminRole uAdminRole )
        throws SecurityException;


    /**
     * This method removes assigned admin role from user entity.  Both user and admin role entities must exist and have role 
     * relationship before calling this method.
     * Successful completion:
     * <pre>
     * del Role to User assignment in User data set
     * AND
     * User to Role assignment in Admin Role data set.
     * </pre>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.UserAdminRole#name} - contains the name for already existing 
     *     AdminRole to be deassigned
     *   </li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.UserAdminRole#userId} - contains the userId for existing User
     *   </li>
     * </ul>
     *
     * @param uAdminRole entity contains {@link org.apache.directory.fortress.core.model.User#userId} and 
     * {@link org.apache.directory.fortress.core.model.AdminRole#name}.
     * @throws SecurityException - in the event data error in user or role objects or system error.
     */
    void deassignUser( UserAdminRole uAdminRole )
        throws SecurityException;


    /**
     * Commands adds a new OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit 
     *     to be added
     *   </li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.OrgUnit#type} - contains the type of OU:  
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#USER} or 
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#PERM}
     *   </li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>{@link org.apache.directory.fortress.core.model.OrgUnit#description} - contains any safe text</li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return OrgUnit contains reference to entity added.
     * @throws SecurityException in the event of data validation or system error.
     */
    OrgUnit add( OrgUnit entity )
        throws SecurityException;


    /**
     * Commands updates existing OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit 
     *     to be updated
     *   </li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.OrgUnit#type} - contains the type of OU:  
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#USER} or 
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#PERM}
     *   </li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>{@link org.apache.directory.fortress.core.model.OrgUnit#description} - contains any safe text</li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return OrgUnit contains reference to entity operated on.
     * @throws SecurityException in the event of data validation or system error.
     */
    OrgUnit update( OrgUnit entity )
        throws SecurityException;


    /**
     * Commands deletes existing OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit 
     *     to be removed
     *   </li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.OrgUnit#type} - contains the type of OU:  
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#USER} or 
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#PERM}
     *   </li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return OrgUnit contains reference to entity operated on.
     * @throws SecurityException in the event of data validation or system error.
     */
    OrgUnit delete( OrgUnit entity )
        throws SecurityException;


    /**
     * This command creates a new orgunit child, and inserts it in the orgunit hierarchy as an immediate descendant of
     * the existing orgunit parent.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The child orgunit is not a member of the ORGUNITS data set.</li>
     *   <li>The parent orgunit is a member of the ORGUNITS data set.</li>
     * </ul>
     * <p> This method:
     * <ul>
     *   <li>Adds new orgunit.</li>
     *   <li>Assigns orgunit relationship between new child and pre-existing parent.</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     parentRole - {@link org.apache.directory.fortress.core.model.OrgUnit#name} - contains the name of existing OrgUnit 
     *     to be parent
     *   </li>
     *   <li>
     *     parentRole - {@link org.apache.directory.fortress.core.model.OrgUnit#type} - contains the type of OrgUnit targeted:
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#USER} or 
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#PERM}
     *   </li>
     *   <li>
     *     childRole - {@link org.apache.directory.fortress.core.model.OrgUnit#name} - contains the name of new OrgUnit to be 
     *     child
     *   </li>
     * </ul>
     * <h4>optional parameters child</h4>
     * <ul>
     *   <li>
     *     childRole - {@link org.apache.directory.fortress.core.model.OrgUnit#description} - maps to description attribute 
     *     on organizationalUnit object class for new child
     *   </li>
     * </ul>
     *
     * @param parent This entity must be present in ORGUNIT data set.  Success will add rel with child.
     * @param child  This entity must not be present in ORGUNIT data set.  Success will add the new entity to ORGUNIT data set.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    void addDescendant( OrgUnit parent, OrgUnit child )
        throws SecurityException;


    /**
     * This command creates a new orgunit parent, and inserts it in the orgunit hierarchy as an immediate ascendant of
     * the existing child orgunit.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The parent is not a member of the ORGUNITS data set.</li>
     *   <li>The child is a member of the ORGUNITS data set.</li>
     * </ul>
     * <p>
     * This method:
     * <ul>
     *   <li>Adds new orgunit.</li>
     *   <li>Assigns orgunit relationship between new parent and pre-existing child.</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     child - {@link org.apache.directory.fortress.core.model.OrgUnit#name} - contains the name of existing OrgUnit to 
     *     be parent
     *   </li>
     *   <li>
     *     parent - {@link org.apache.directory.fortress.core.model.OrgUnit#type} - contains the type of OrgUnit targeted: 
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#USER} or 
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#PERM}
     *   </li>
     *   <li>
     *     parent - {@link org.apache.directory.fortress.core.model.OrgUnit#name} - contains the name of new OrgUnit to be 
     *     child
     *   </li>
     * </ul>
     * <h4>optional parameters child</h4>
     * <ul>
     *   <li>
     *     parent - {@link OrgUnit#description} - maps to description attribute on organizationalUnit object class for new 
     *     child
     *   </li>
     * </ul>
     *
     * @param parent completion of op assigns new child relationship with child orgunit.
     * @param child  completion of op assigns new parent relationship with parent orgunit.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    void addAscendant( OrgUnit child, OrgUnit parent )
        throws SecurityException;


    /**
     * This command establishes a new immediate inheritance relationship with parent orgunit &lt;&lt;-- child orgunit
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li> The parent and child are members of the ORGUNITS data set.</li>
     *   <li> The parent is not an immediate ascendant of child.</li>
     *   <li> The child does not properly inherit parent (in order to avoid cycle creation).</li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     parent - {@link org.apache.directory.fortress.core.model.OrgUnit#name} - contains the name of existing OrgUnit to 
     *     be parent
     *   </li>
     *   <li>
     *     parent - {@link org.apache.directory.fortress.core.model.OrgUnit#type} - contains the type of OrgUnit targeted: 
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#USER} or 
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#PERM}
     *   </li>
     *   <li>
     *     child - {@link org.apache.directory.fortress.core.model.OrgUnit#name} - contains the name of existing OrgUnit to 
     *     be child
     *   </li>
     * </ul>
     *
     * @param parent completion of op deassigns child relationship with child orgunit.
     * @param child  completion of op deassigns parent relationship with parent orgunit.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    void addInheritance( OrgUnit parent, OrgUnit child )
        throws SecurityException;


    /**
     * This command deletes an existing immediate inheritance relationship parent &lt;&lt;-- child.
     * <p>
     * The command is valid if and only if:
     * <ul>
     *   <li>The orgunits parent and child are members of the ORGUNITS data set.</li>
     *   <li>The parent is an immediate ascendant of child.</li>
     *   <li>
     *     The new inheritance relation is computed as the reflexive-transitive closure of the immediate inheritance
     *     relation resulted after deleting the relationship parent &lt;&lt;-- child.
     *   </li>
     * </ul>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     parent - {@link org.apache.directory.fortress.core.model.OrgUnit#name} - contains the name of existing OrgUnit to 
     *     remove as parent
     *   </li>
     *   <li>
     *     parent - {@link org.apache.directory.fortress.core.model.OrgUnit#type} - contains the type of OrgUnit targeted: 
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#USER} or 
     *     {@link org.apache.directory.fortress.core.model.OrgUnit.Type#PERM}
     *   </li>
     *   <li>
     *     child - {@link org.apache.directory.fortress.core.model.OrgUnit#name} - contains the name of existing OrgUnit to 
     *     remove as child
     *   </li>
     * </ul>
     *
     * @param parent completion of op removes child relationship with childRole.
     * @param child  completion of op removes parent relationship with parentRole.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    void deleteInheritance( OrgUnit parent, OrgUnit child )
        throws SecurityException;


    /**
     * This command creates a new role childRole, and inserts it in the role hierarchy as an immediate descendant of
     * the existing role parentRole. The command is valid if and only if childRole is not a member of the ADMINROLES data 
     * set, and parentRole is a member of the ADMINROLES data set.
     * <p>
     * This method:
     * <ol>
     *   <li>Adds new role.</li>
     *   <li>Assigns role relationship between new childRole and pre-existing parentRole.</li>
     * </ol>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     parentRole - {@link org.apache.directory.fortress.core.model.AdminRole#name} - contains the name of existing Role 
     *     to be parent
     *   </li>
     *   <li>
     *     childRole - {@link org.apache.directory.fortress.core.model.AdminRole#name} - contains the name of new Role to be 
     *     child
     *   </li>
     * </ul>
     * <h4>optional parameters childRole</h4>
     * <ul>
     *   <li>
     *     childRole - {@link org.apache.directory.fortress.core.model.AdminRole#description} - maps to description attribute 
     *     on organizationalRole object class for new child
     *   </li>
     *   <li>
     *     childRole - {@link org.apache.directory.fortress.core.model.AdminRole#beginTime} - HHMM - determines begin hour 
     *     role may be activated into user's session for new child
     *   </li>
     *   <li>
     *     childRole - {@link org.apache.directory.fortress.core.model.AdminRole#endTime} - HHMM - determines end hour role 
     *     may be activated into user's session for new child
     *   </li>
     *   <li>
     *     childRole - {@link org.apache.directory.fortress.core.model.AdminRole#beginDate} - YYYYMMDD - determines date when 
     *     role may be activated into user's session for new child
     *   </li>
     *   <li>
     *     childRole - {@link org.apache.directory.fortress.core.model.AdminRole#endDate} - YYYYMMDD - indicates latest date 
     *     role may be activated into user's session for new chil
     *   </li>
     *   <li>
     *     childRole - {@link org.apache.directory.fortress.core.model.AdminRole#beginLockDate} - YYYYMMDD - determines 
     *     beginning of enforced inactive status for new child
     *   </li>
     *   <li>
     *     childRole - {@link org.apache.directory.fortress.core.model.AdminRole#endLockDate} - YYYYMMDD - determines end of 
     *     enforced inactive status for new child
     *   </li>
     *   <li>
     *     childRole - {@link org.apache.directory.fortress.core.model.AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, 
     *     etc - specifies which day role may be activated into user's session for new child
     *   </li>
     * </ul>
     *
     * @param parentRole This entity must be present in ADMINROLES data set.  Success will add role rel with childRole.
     * @param childRole  This entity must not be present in ADMINROLES data set.  Success will add the new role entity to 
     * ADMINROLES data set.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    void addDescendant( AdminRole parentRole, AdminRole childRole )
        throws SecurityException;


    /**
     * This command creates a new role parentRole, and inserts it in the role hierarchy as an immediate ascendant of
     * the existing role childRole. The command is valid if and only if parentRole is not a member of the ADMINROLES data set,
     * and childRole is a member of the ADMINROLES data set.
     * This method:
     * <ol>
     *   <li>Adds new role.</li>
     *   <li>Assigns role relationship between new parentRole and pre-existing childRole.</li>
     * </ol>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     childRole - {@link org.apache.directory.fortress.core.model.AdminRole#name} - contains the name of existing Role 
     *     to be child
     *   </li>
     *   <li>
     *     parentRole - {@link org.apache.directory.fortress.core.model.AdminRole#name} - contains the name of new Role to be 
     *     added as parent
     *   </li>
     * </ul>
     * <h4>optional parameters parentRole</h4>
     * <ul>
     *   <li>
     *     parentRole - {@link org.apache.directory.fortress.core.model.AdminRole#description} - maps to description 
     *     attribute on organizationalRole object class for new parent
     *   </li>
     *   <li>
     *     parentRole - {@link org.apache.directory.fortress.core.model.AdminRole#beginTime} - HHMM - determines begin hour 
     *     role may be activated into user's session for new parent
     *   </li>
     *   <li>
     *     parentRole - {@link org.apache.directory.fortress.core.model.AdminRole#endTime} - HHMM - determines end hour role 
     *     may be activated into user's session for new parent
     *   </li>
     *   <li>
     *     parentRole - {@link org.apache.directory.fortress.core.model.AdminRole#beginDate} - YYYYMMDD - determines date 
     *     when role may be activated into user's session for new parent
     *   </li>
     *   <li>
     *     parentRole - {@link org.apache.directory.fortress.core.model.AdminRole#endDate} - YYYYMMDD - indicates latest date 
     *     role may be activated into user's session for new parent
     *   </li>
     *   <li>
     *     parentRole - {@link org.apache.directory.fortress.core.model.AdminRole#beginLockDate} - YYYYMMDD - determines 
     *     beginning of enforced inactive status for new parent
     *   </li>
     *   <li>
     *     parentRole - {@link org.apache.directory.fortress.core.model.AdminRole#endLockDate} - YYYYMMDD - determines end 
     *     of enforced inactive status for new parent
     *   </li>
     *   <li>
     *     parentRole - {@link org.apache.directory.fortress.core.model.AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, 
     *     etc - specifies which day role may be activated into user's session for new parent
     *   </li>
     * </ul>
     *
     * @param parentRole completion of op assigns new child relationship with childRole.
     * @param childRole  completion of op assigns new parent relationship with parentRole.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    void addAscendant( AdminRole childRole, AdminRole parentRole )
        throws SecurityException;


    /**
     * This command establishes a new immediate inheritance relationship parentRole &lt;&lt;-- childRole between existing
     * roles parentRole, childRole. The command is valid if and only if parentRole and childRole are members of the 
     * ADMINROLES data set, parentRole is not an immediate ascendant of childRole, and childRole does not properly inherit 
     * parentRole (in order to avoid cycle creation).
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     parentRole - {@link org.apache.directory.fortress.core.model.AdminRole#name} - contains the name of existing AdminRole 
     *     to be parent
     *   </li>
     *   <li>
     *     childRole - {@link org.apache.directory.fortress.core.model.AdminRole#name} - contains the name of existing AdminRole 
     *     to be child
     *   </li>
     * </ul>
     *
     * @param parentRole completion of op deassigns child relationship with childRole.
     * @param childRole  completion of op deassigns parent relationship with parentRole.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    void addInheritance( AdminRole parentRole, AdminRole childRole )
        throws SecurityException;


    /**
     * This command deletes an existing immediate inheritance relationship parentRole &lt;&lt;-- childRole. The command is
     * valid if and only if the roles parentRole and childRole are members of the ADMINROLES data set, and parentRole is an
     * immediate ascendant of childRole. The new inheritance relation is computed as the reflexive-transitive
     * closure of the immediate inheritance relation resulted after deleting the relationship parentRole &lt;&lt;-- childRole.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     parentRole - {@link org.apache.directory.fortress.core.model.AdminRole#name} - contains the name of existing 
     *     AdminRole to remove as parent
     *   </li>
     *   <li>
     *     childRole - {@link org.apache.directory.fortress.core.model.AdminRole#name} - contains the name of existing 
     *     AdminRole to remove as child
     *   </li>
     * </ul>
     *
     * @param parentRole completion of op removes child relationship with childRole.
     * @param childRole  completion of op removes parent relationship with parentRole.
     * @throws SecurityException thrown in the event of data validation or system error.
     */
    void deleteInheritance( AdminRole parentRole, AdminRole childRole )
        throws SecurityException;


    /**
     * This method will add an administrative permission operation to an existing permission object which resides under 
     * {@code ou=AdminPerms,ou=ARBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may have {@link org.apache.directory.fortress.core.model.AdminRole} or 
     * {@link org.apache.directory.fortress.core.model.User} associations.  The target 
     * {@link org.apache.directory.fortress.core.model.Permission} must not exist prior to calling.
     * A Fortress Permission instance exists in a hierarchical, one-many relationship between its parent and itself as stored 
     * in ldap tree: ({@link org.apache.directory.fortress.core.model.PermObj}*-&gt;
     * {@link org.apache.directory.fortress.core.model.Permission}).
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.Permission#objName} - contains the name of existing object being 
     *     targeted for the permission add
     *   </li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.Permission#opName} - contains the name of new permission operation 
     *     being added
     *   </li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>
     *     {@link Permission#roles} * - multi occurring attribute contains RBAC Roles that permission operation is being 
     *     granted to
     *   </li>
     *   <li>
     *     {@link Permission#users} * - multi occurring attribute contains Users that permission operation is being granted 
     *     to
     *   </li>
     *   <li>
     *     {@link Permission#props} * - multi-occurring property key and values are separated with a ':'.  e.g. 
     *     mykey1:myvalue1
     *   </li>
     *   <li>
     *     {@link Permission#type} - any safe text
     *   </li>
     * </ul>
     *
     * @param perm must contain the object, {@link org.apache.directory.fortress.core.model.Permission#objName}, and 
     * operation, {@link Permission#opName}, that identifies target along with optional other attributes..
     * @return copy of Permission entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    Permission addPermission( Permission perm )
        throws SecurityException;


    /**
     * This method will update administrative permission operation pre-existing in target directory under 
     * {@code ou=AdminPerms,ou=ARBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may also contain {@link org.apache.directory.fortress.core.model.AdminRole} or 
     * {@link org.apache.directory.fortress.core.model.User} associations to add or remove using this function.
     * The perm operation must exist before making this call.  Only non-null attributes will be updated.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.Permission#objName} - contains the name of existing object being 
     *     targeted for the permission update
     *   </li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.Permission#opName} - contains the name of existing permission 
     *     operation being updated
     *   </li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.Permission#roles} * - multi occurring attribute contains RBAC 
     *     Roles that permission operation is being granted to
     *   </li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.Permission#users} * - multi occurring attribute contains Users 
     *     that permission operation is being granted to
     *   </li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.Permission#props} * - multi-occurring property key and values are 
     *     separated with a ':'.  e.g. mykey1:myvalue1
     *   </li>
     *   <li>{@link org.apache.directory.fortress.core.model.Permission#type} - any safe text</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objName}, and operation, {@link Permission#opName}, that 
     * identifies target and any optional data to update.  Null or empty attributes will be ignored.
     * @return copy of Permission entity.
     * @throws SecurityException thrown in the event of perm object data or system error.
     */
    Permission updatePermission( Permission perm )
        throws SecurityException;


    /**
     * This method will remove administrative permission operation entity from permission object. A Fortress permission is 
     * (object-&gt;operation).
     * The perm operation must exist before making this call.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.Permission#objName} - contains the name of existing object being 
     *     targeted for the permission delete
     *   </li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.Permission#opName} - contains the name of existing permission 
     *     operation being removed
     *   </li>
     * </ul>
     *
     * @param perm must contain the object, {@link org.apache.directory.fortress.core.model.Permission#objName}, and 
     * operation, {@link org.apache.directory.fortress.core.model.Permission#opName}, that identifies target.
     * @throws SecurityException thrown in the event of perm object data or system error.
     */
    void deletePermission( Permission perm )
        throws SecurityException;


    /**
     * This method will add administrative permission object to admin perms container in directory. The perm object must not 
     * exist before making this call.
     * A {@link org.apache.directory.fortress.core.model.PermObj} instance exists in a hierarchical, one-many relationship 
     * between itself and children as stored in ldap tree: ({@link org.apache.directory.fortress.core.model.PermObj}*
     * -&gt; {@link org.apache.directory.fortress.core.model.Permission}).
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.PermObj#objName} - contains the name of new object being added
     *   </li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.PermObj#ou} - contains the name of an existing PERMS OrgUnit this 
     *     object is associated with
     *   </li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>{@link org.apache.directory.fortress.core.model.PermObj#description} - any safe text</li>
     *   <li>{@link org.apache.directory.fortress.core.model.PermObj#type} - contains any safe text</li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.PermObj#props} * - multi-occurring property key and values are 
     *     separated with a ':'.  e.g. mykey1:myvalue1
     *   </li>
     * </ul>
     *
     * @param pObj must contain the {@link org.apache.directory.fortress.core.model.PermObj#objName} and 
     * {@link org.apache.directory.fortress.core.model.PermObj#ou}.  The other attributes are optional.
     * @return copy of PermObj entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    PermObj addPermObj( PermObj pObj )
        throws SecurityException;


    /**
     * This method will update administrative permission object in perms container in directory.  The perm object must exist 
     * before making this call.
     * A {@link org.apache.directory.fortress.core.model.PermObj} instance exists in a hierarchical, one-many relationship 
     * between itself and children as stored in ldap tree: ({@link org.apache.directory.fortress.core.model.PermObj}*
     * -&gt; {@link org.apache.directory.fortress.core.model.Permission}).
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.PermObj#objName} - contains the name of existing object being 
     *     updated
     *   </li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.PermObj#ou} - contains the name of an existing PERMS OrgUnit this 
     *     object is associated with
     *   </li>
     *   <li>{@link org.apache.directory.fortress.core.model.PermObj#description} - any safe text</li>
     *   <li>{@link org.apache.directory.fortress.core.model.PermObj#type} - contains any safe text</li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.PermObj#props} * - multi-occurring property key and values are 
     *     separated with a ':'.  e.g. mykey1:myvalue1
     *   </li>
     * </ul>
     *
     * @param pObj must contain the {@link org.apache.directory.fortress.core.model.PermObj#objName}. Only non-null 
     * attributes will be updated.
     * @return copy of newly updated PermObj entity.
     * @throws SecurityException thrown in the event of perm object data or system error.
     */
    PermObj updatePermObj( PermObj pObj )
        throws SecurityException;


    /**
     * This method will remove administrative permission object from perms container in directory.  This method will also 
     * remove in associated permission objects that are attached to this object.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.PermObj#objName} - contains the name of existing object targeted 
     *     for removal
     *   </li>
     * </ul>
     *
     * @param pObj must contain the {@link org.apache.directory.fortress.core.model.PermObj#objName} of object targeted for 
     * removal.
     * @throws SecurityException thrown in the event of perm object data or system error.
     */
    void deletePermObj( PermObj pObj )
        throws SecurityException;


    /**
     * This command grants an AdminRole the administrative permission to perform an operation on an object to a role.
     * The command is implemented by granting administrative permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * and the adminRole is a member of the ADMIN_ROLES data set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link org.apache.directory.fortress.core.model.Permission#objName} - contains the object name</li>
     *   <li>{@link org.apache.directory.fortress.core.model.Permission#opName} - contains the operation name</li>
     *   <li>{@link org.apache.directory.fortress.core.model.AdminRole#name} - contains the adminRole name</li>
     * </ul>
     *
     * @param perm must contain the object, {@link org.apache.directory.fortress.core.model.Permission#objName}, and 
     * operation, {@link org.apache.directory.fortress.core.model.Permission#opName}, that identifies target.
     * @param role must contains {@link org.apache.directory.fortress.core.model.AdminRole#name}.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    void grantPermission( Permission perm, AdminRole role )
        throws SecurityException;


    /**
     * This command revokes the administrative permission to perform an operation on an object from the set
     * of permissions assigned to an AdminRole. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * the role is a member of the ADMIN_ROLES data set, and the permission is assigned to that AdminRole.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link org.apache.directory.fortress.core.model.Permission#objName} - contains the object name</li>
     *   <li>{@link org.apache.directory.fortress.core.model.Permission#opName} - contains the operation name</li>
     *   <li>{@link org.apache.directory.fortress.core.model.AdminRole#name} - contains the adminRole name</li>
     * </ul>
     *
     * @param perm must contain the object, {@link org.apache.directory.fortress.core.model.Permission#objName}, and operation, {@link org.apache.directory.fortress.core.model.Permission#opName}, that identifies target.
     * @param role must contains {@link org.apache.directory.fortress.core.model.AdminRole#name}.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    void revokePermission( Permission perm, AdminRole role )
        throws SecurityException;


    /**
     * This command grants a user the administrative permission to perform an operation on an object to a user.
     * The command is implemented by granting administrative permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents an administrative permission,
     * and the user is a member of the USERS data set.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link org.apache.directory.fortress.core.model.Permission#objName} - contains the object name</li>
     *   <li>{@link org.apache.directory.fortress.core.model.Permission#opName} - contains the operation name</li>
     *   <li>{@link org.apache.directory.fortress.core.model.User#userId} - contains the userId</li>
     * </ul>
     *
     * @param perm must contain the object, {@link org.apache.directory.fortress.core.model.Permission#objName}, and 
     * operation, {@link org.apache.directory.fortress.core.model.Permission#opName}, that identifies target.
     * @param user must contain {@link org.apache.directory.fortress.core.model.User#userId} of target User entity.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    void grantPermission( Permission perm, User user )
        throws SecurityException;


    /**
     * This command revokes the administrative permission to perform an operation on an object from the set
     * of permissions assigned to a user. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents an administrative permission,
     * the user is a member of the USERS data set, and the permission is assigned to that user.
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link org.apache.directory.fortress.core.model.Permission#objName} - contains the object name</li>
     *   <li>{@link org.apache.directory.fortress.core.model.Permission#opName} - contains the operation name</li>
     *   <li>{@link org.apache.directory.fortress.core.model.User#userId} - contains the userId</li>
     * </ul>
     *
     * @param perm must contain the object, {@link org.apache.directory.fortress.core.model.Permission#objName}, and 
     * operation, {@link org.apache.directory.fortress.core.model.Permission#opName}, that identifies target.
     * @param user must contain {@link org.apache.directory.fortress.core.model.User#userId} of target User entity.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    void revokePermission( Permission perm, User user )
        throws SecurityException;
}