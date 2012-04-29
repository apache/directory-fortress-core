/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rest;

import com.jts.fortress.*;
import com.jts.fortress.SecurityException;
import com.jts.fortress.arbac.AdminRole;
import com.jts.fortress.arbac.AdminRoleRelationship;
import com.jts.fortress.arbac.OrgUnit;
import com.jts.fortress.arbac.OrgUnitRelationship;
import com.jts.fortress.arbac.UserAdminRole;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.rbac.PermGrant;
import com.jts.fortress.rbac.Permission;
import com.jts.fortress.rbac.PermObj;
import com.jts.fortress.rbac.User;
import com.jts.fortress.rbac.Session;
import com.jts.fortress.util.attr.VUtil;


/**
 * This object implements the ARBAC02 DelegatedAdminMgr interface for performing policy administration of Fortress ARBAC entities
 * using HTTP access to En Masse REST server.
 * These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification describes delegated administrative
 * operations for the creation and maintenance of ARBAC element sets and relations.  Delegated administrative review functions for performing administrative queries
 * and system functions for creating and managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 * This object is NOT thread safe.
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="../../../../images/ARbac.png">
 * <p/>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without sacrificing regulations for accountability or traceability.
 * <p/>
 *
 * @author Shawn McKinney
 * @created September 18, 2010
 */
public final class DelegatedAdminMgrRestImpl
    implements DelegatedAdminMgr
{
    private static final String CLS_NM = DelegatedAdminMgrRestImpl.class.getName();

    // thread unsafe variable:
    private Session adminSess;

    /**
     * Setting Session into this object will enforce Administrative controls.
     *
     * @param session
     */
    public void setAdmin(Session session)
    {
        this.adminSess = session;
    }

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
     * @throws com.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    public AdminRole addRole(AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, CLS_NM + ".addRole");
        AdminRole retRole;
        FortRequest request = new FortRequest();
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ARLE_ADD);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retRole = (AdminRole) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retRole;
    }

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
     * @throws com.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    public void deleteRole(AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, CLS_NM + ".deleteRole");
        FortRequest request = new FortRequest();
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ARLE_DELETE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Method will update a admin Role entity in the directory.  The role must exist in admin role container prior to this call.
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
     * @throws com.jts.fortress.SecurityException
     *          Description of the Exception
     */
    public AdminRole updateRole(AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, CLS_NM + ".updateRole");
        AdminRole retRole;
        FortRequest request = new FortRequest();
        request.setEntity(role);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ARLE_UPDATE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retRole = (AdminRole) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retRole;
    }


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
     * @param uAdminRole entity contains {@link User#userId} and {@link AdminRole#name} and optional {@code Constraints}.
     * @return AdminRole contains copy of input entity and additional data processed by request.
     * @throws SecurityException in the event data error in user or role objects or system error.
     */
    public void assignUser(UserAdminRole uAdminRole)
        throws SecurityException
    {
        VUtil.assertNotNull(uAdminRole, GlobalErrIds.ARLE_NULL, CLS_NM + ".assignUser");
        FortRequest request = new FortRequest();
        request.setEntity(uAdminRole);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ARLE_ASGN);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * This method removes assigned admin role from user entity.  Both user and admin role entities must exist and have role relationship
     * before calling this method.
     * Successful completion:
     * del Role to User assignment in User data set
     * AND
     * User to Role assignment in Admin Role data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link UserAdminRole#name} - contains the name for already existing AdminRole to be deassigned</li>
     * <li>{@link UserAdminRole#userId} - contains the userId for existing User</li>
     * </ul>
     *
     * @param uAdminRole entity contains {@link User#userId} and {@link AdminRole#name}.
     * @return AdminRole contains copy of input entity and additional data processed by request.
     * @throws SecurityException - in the event data error in user or role objects or system error.
     */
    public void deassignUser(UserAdminRole uAdminRole)
        throws SecurityException
    {
        VUtil.assertNotNull(uAdminRole, GlobalErrIds.ARLE_NULL, CLS_NM + ".deassignUser");
        FortRequest request = new FortRequest();
        request.setEntity(uAdminRole);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ARLE_DEASGN);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * Commands adds a new OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit to be added</li>
     * <li>{@link OrgUnit#type} - contains the type of OU:  {@link OrgUnit.Type#USER} or {@link OrgUnit.Type#PERM}</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link OrgUnit#description} - contains any safe text</li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return
     * @throws com.jts.fortress.SecurityException
     *          in the event of data validation or system error.
     */
    public OrgUnit add(OrgUnit entity)
        throws SecurityException
    {
        VUtil.assertNotNull(entity, GlobalErrIds.ORG_NULL, CLS_NM + ".addOU");
        OrgUnit retOrg;
        FortRequest request = new FortRequest();
        request.setEntity(entity);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ORG_ADD);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retOrg = (OrgUnit) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retOrg;
    }

    /**
     * Commands updates existing OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit to be updated</li>
     * <li>{@link OrgUnit#type} - contains the type of OU:  {@link OrgUnit.Type#USER} or {@link OrgUnit.Type#PERM}</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link OrgUnit#description} - contains any safe text</li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return
     * @throws com.jts.fortress.SecurityException
     *          in the event of data validation or system error.
     */
    public OrgUnit update(OrgUnit entity)
        throws SecurityException
    {
        VUtil.assertNotNull(entity, GlobalErrIds.ORG_NULL, CLS_NM + ".updateOU");
        OrgUnit retOrg;
        FortRequest request = new FortRequest();
        request.setEntity(entity);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ORG_UPDATE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retOrg = (OrgUnit) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retOrg;
    }

    /**
     * Commands deletes existing OrgUnit entity to OrgUnit dataset.  The OrgUnit can be either User or Perm and is
     * set by setting type attribute.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit to be removed</li>
     * <li>{@link OrgUnit#type} - contains the type of OU:  {@link OrgUnit.Type#USER} or {@link OrgUnit.Type#PERM}</li>
     * </ul>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return
     * @throws SecurityException in the event of data validation or system error.
     */
    public OrgUnit delete(OrgUnit entity)
        throws SecurityException
    {
        VUtil.assertNotNull(entity, GlobalErrIds.ORG_NULL, CLS_NM + ".deleteOU");
        OrgUnit retOrg;
        FortRequest request = new FortRequest();
        request.setEntity(entity);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ORG_DELETE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retOrg = (OrgUnit) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retOrg;
    }


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
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link OrgUnit#name} - contains the name of existing OrgUnit to be parent</li>
     * <li>parentRole - {@link OrgUnit#type} - contains the type of OrgUnit targeted: {@link OrgUnit.Type#USER} or {@link OrgUnit.Type#PERM}</li>
     * <li>childRole - {@link OrgUnit#name} - contains the name of new OrgUnit to be child</li>
     * </ul>
     * <h4>optional parameters child</h4>
     * <ul>
     * <li>childRole - {@link OrgUnit#description} - maps to description attribute on organizationalUnit object class for new child</li>
     * </ul>
     *
     * @param parent This entity must be present in ORGUNIT data set.  Success will add rel with child.
     * @param child  This entity must not be present in ORGUNIT data set.  Success will add the new entity to ORGUNIT data set.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addDescendant(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "addDescendantOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(child, GlobalErrIds.ORG_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        OrgUnitRelationship relationship = new OrgUnitRelationship();
        relationship.setParent(parent);
        relationship.setChild(child);
        request.setEntity(relationship);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ORG_DESC);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


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
     * <h4>required parameters</h4>
     * <ul>
     * <li>parent - {@link OrgUnit#name} - contains the name of existing OrgUnit to be parent</li>
     * <li>parent - {@link OrgUnit#type} - contains the type of OrgUnit targeted: {@link OrgUnit.Type#USER} or {@link OrgUnit.Type#PERM}</li>
     * <li>child - {@link OrgUnit#name} - contains the name of new OrgUnit to be child</li>
     * </ul>
     * <h4>optional parameters child</h4>
     * <ul>
     * <li>child - {@link OrgUnit#description} - maps to description attribute on organizationalUnit object class for new child</li>
     * </ul>
     *
     * @param parent completion of op assigns new child relationship with child orgunit.
     * @param child  completion of op assigns new parent relationship with parent orgunit.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addAscendant(OrgUnit child, OrgUnit parent)
        throws SecurityException
    {
        String methodName = "addAscendantOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        OrgUnitRelationship relationship = new OrgUnitRelationship();
        relationship.setParent(parent);
        relationship.setChild(child);
        request.setEntity(relationship);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ORG_ASC);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This commands establishes a new immediate inheritance relationship with parent orgunit <<-- child orgunit
     * <p/>
     * The command is valid if and only if:
     * <ul>
     * <li> The parent and child are members of the ORGUNITS data set.
     * <li> The parent is not an immediate ascendant of child.
     * <li> The child does not properly inherit parent (in order to avoid cycle creation).
     * </ul>
     * <h4>required parameters</h4>
     * <ul>
     * <li>parent - {@link OrgUnit#name} - contains the name of existing OrgUnit to be parent</li>
     * <li>parent - {@link OrgUnit#type} - contains the type of OrgUnit targeted: {@link OrgUnit.Type#USER} or {@link OrgUnit.Type#PERM}</li>
     * <li>child - {@link OrgUnit#name} - contains the name of existing OrgUnit to be child</li>
     * </ul>
     *
     * @param parent completion of op deassigns child relationship with child orgunit.
     * @param child  completion of op deassigns parent relationship with parent orgunit.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addInheritance(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "addInheritanceOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(child, GlobalErrIds.ORG_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        OrgUnitRelationship relationship = new OrgUnitRelationship();
        relationship.setParent(parent);
        relationship.setChild(child);
        request.setEntity(relationship);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ORG_ADDINHERIT);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

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
     * <li>parent - {@link OrgUnit#name} - contains the name of existing OrgUnit to remove as parent</li>
     * <li>parent - {@link OrgUnit#type} - contains the type of OrgUnit targeted: {@link OrgUnit.Type#USER} or {@link OrgUnit.Type#PERM}</li>
     * <li>child - {@link OrgUnit#name} - contains the name of existing OrgUnit to remove as child</li>
     * </ul>
     *
     * @param parent completion of op removes child relationship with childRole.
     * @param child  completion of op removes parent relationship with parentRole.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void deleteInheritance(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "deleteInheritanceOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(child, GlobalErrIds.ORG_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        OrgUnitRelationship relationship = new OrgUnitRelationship();
        relationship.setParent(parent);
        relationship.setChild(child);
        request.setEntity(relationship);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ORG_DELINHERIT);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }


    /**
     * This commands creates a new role childRole, and inserts it in the role hierarchy as an immediate descendant of
     * the existing role parentRole. The command is valid if and only if childRole is not a member of the ROLES data set,
     * and parentRole is a member of the ROLES data set.
     *
     * @param parentRole This entity must be present in ADMINROLES data set.  Success will add role rel with childRole.
     * @param childRole  This entity must not be present in ADMINROLES data set.  Success will add the new role entity to ROLE data set.
     *                   This method:
     *                   1 - Adds new role.
     *                   2 - Assigns role relationship between new childRole and pre-existing parentRole.
     *                   <h4>required parameters</h4>
     *                   <ul>
     *                   <li>parentRole - {@link AdminRole#name} - contains the name of existing Role to be parent</li>
     *                   <li>childRole - {@link AdminRole#name} - contains the name of new Role to be child</li>
     *                   </ul>
     *                   <h4>optional parameters childRole</h4>
     *                   <ul>
     *                   <li>childRole - {@link AdminRole#description} - maps to description attribute on organizationalRole object class for new child</li>
     *                   <li>childRole - {@link AdminRole#beginTime} - HHMM - determines begin hour role may be activated into user's session for new child</li>
     *                   <li>childRole - {@link AdminRole#endTime} - HHMM - determines end hour role may be activated into user's session for new child</li>
     *                   <li>childRole - {@link AdminRole#beginDate} - YYYYMMDD - determines date when role may be activated into user's session for new child</li>
     *                   <li>childRole - {@link AdminRole#endDate} - YYYYMMDD - indicates latest date role may be activated into user's session for new child</li>
     *                   <li>childRole - {@link AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status for new child</li>
     *                   <li>childRole - {@link AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status for new child</li>
     *                   <li>childRole - {@link AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's session for new child</li>
     *                   </ul>
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addDescendant(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "addDescendantRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        AdminRoleRelationship relationship = new AdminRoleRelationship();
        relationship.setParent(parentRole);
        relationship.setChild(childRole);
        request.setEntity(relationship);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ARLE_DESC);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This commands creates a new role parentRole, and inserts it in the role hierarchy as an immediate ascendant of
     * the existing role childRole. The command is valid if and only if parentRole is not a member of the ROLES data set,
     * and childRole is a member of the ROLES data set.
     * This method:
     * 1 - Adds new role.
     * 2 - Assigns role relationship between new parentRole and pre-existing childRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>childRole - {@link AdminRole#name} - contains the name of existing Role to be child</li>
     * <li>parentRole - {@link AdminRole#name} - contains the name of new Role to be added as parent</li>
     * </ul>
     * <h4>optional parameters parentRole</h4>
     * <ul>
     * <li>parentRole - {@link AdminRole#description} - maps to description attribute on organizationalRole object class for new parent</li>
     * <li>parentRole - {@link AdminRole#beginTime} - HHMM - determines begin hour role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link AdminRole#endTime} - HHMM - determines end hour role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link AdminRole#beginDate} - YYYYMMDD - determines date when role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link AdminRole#endDate} - YYYYMMDD - indicates latest date role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status for new parent</li>
     * <li>parentRole - {@link AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status for new parent</li>
     * <li>parentRole - {@link AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's session for new parent</li>
     * </ul>
     *
     * @param parentRole completion of op assigns new child relationship with childRole.
     * @param childRole  completion of op assigns new parent relationship with parentRole.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addAscendant(AdminRole childRole, AdminRole parentRole)
        throws SecurityException
    {
        String methodName = "addAscendantRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        AdminRoleRelationship relationship = new AdminRoleRelationship();
        relationship.setParent(parentRole);
        relationship.setChild(childRole);
        request.setEntity(relationship);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ARLE_ASC);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This commands establishes a new immediate inheritance relationship parentRole <<-- childRole between existing
     * roles parentRole, childRole. The command is valid if and only if parentRole and childRole are members of the ROLES data
     * set, parentRole is not an immediate ascendant of childRole, and childRole does not properly inherit parentRole (in order to
     * avoid cycle creation).
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link AdminRole#name} - contains the name of existing AdminRole to be parent</li>
     * <li>childRole - {@link AdminRole#name} - contains the name of existing AdminRole to be child</li>
     * </ul>
     *
     * @param parentRole completion of op deassigns child relationship with childRole.
     * @param childRole  completion of op deassigns parent relationship with parentRole.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void addInheritance(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "addInheritanceRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        AdminRoleRelationship relationship = new AdminRoleRelationship();
        relationship.setParent(parentRole);
        relationship.setChild(childRole);
        request.setEntity(relationship);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ARLE_ADDINHERIT);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This command deletes an existing immediate inheritance relationship parentRole <<-- childRole. The command is
     * valid if and only if the roles parentRole and childRole are members of the ROLES data set, and parentRole is an
     * immediate ascendant of childRole. The new inheritance relation is computed as the reflexive-transitive
     * closure of the immediate inheritance relation resulted after deleting the relationship parentRole <<-- childRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link AdminRole#name} - contains the name of existing AdminRole to remove as parent</li>
     * <li>childRole - {@link AdminRole#name} - contains the name of existing AdminRole to remove as child</li>
     * </ul>
     *
     * @param parentRole completion of op removes child relationship with childRole.
     * @param childRole  completion of op removes parent relationship with parentRole.
     * @throws com.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    public void deleteInheritance(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "deleteInheritanceRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        AdminRoleRelationship relationship = new AdminRoleRelationship();
        relationship.setParent(parentRole);
        relationship.setChild(childRole);
        request.setEntity(relationship);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ARLE_DELINHERIT);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This method will add an administrative permission operation to an existing permission object which resides under {@code ou=AdminPerms,ou=ARBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may have {@link com.jts.fortress.arbac.AdminRole} or {@link com.jts.fortress.rbac.User} associations.  The target {@link Permission} must not exist prior to calling.
     * A Fortress Permission instance exists in a hierarchical, one-many relationship between its parent and itself as stored in ldap tree: ({@link PermObj}*->{@link Permission}).
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the name of existing object being targeted for the permission add</li>
     * <li>{@link Permission#opName} - contains the name of new permission operation being added</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link Permission#roles} * - multi occurring attribute contains RBAC Roles that permission operation is being granted to</li>
     * <li>{@link Permission#users} * - multi occurring attribute contains Users that permission operation is being granted to</li>
     * <li>{@link Permission#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * <li>{@link Permission#type} - any safe text</li>
     * </ul>
     *
     * @param perm must contain the object, {@link com.jts.fortress.rbac.Permission#objectName}, and operation, {@link Permission#opName}, that identifies target along with optional other attributes..
     * @return copy of Permission entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    public Permission addPermission(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".addPermission");
        Permission retPerm;
        FortRequest request = new FortRequest();
        perm.setAdmin(true);
        request.setEntity(perm);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.PERM_ADD);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retPerm = (Permission) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retPerm;
    }

    /**
     * This method will update administrative permission operation pre-existing in target directory under {@code ou=AdminPerms,ou=ARBAC,dc=yourHostName,dc=com} container in directory information tree.
     * The perm operation entity may also contain {@link com.jts.fortress.arbac.AdminRole} or {@link com.jts.fortress.rbac.User} associations to add or remove using this function.
     * The perm operation must exist before making this call.  Only non-null attributes will be updated.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the name of existing object being targeted for the permission update</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation being updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link Permission#roles} * - multi occurring attribute contains RBAC Roles that permission operation is being granted to</li>
     * <li>{@link Permission#users} * - multi occurring attribute contains Users that permission operation is being granted to</li>
     * <li>{@link Permission#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * <li>{@link Permission#type} - any safe text</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target and any optional data to update.  Null or empty attributes will be ignored.
     * @return copy of permOp entity.
     * @throws com.jts.fortress.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    public Permission updatePermission(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".updatePermission");
        Permission retPerm = null;
        FortRequest request = new FortRequest();
        perm.setAdmin(true);
        request.setEntity(perm);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.PERM_UPDATE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retPerm = (Permission) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retPerm;
    }

    /**
     * This method will remove administrative permission operation entity from permission object. A Fortress permission is (object->operation).
     * The perm operation must exist before making this call.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the name of existing object being targeted for the permission delete</li>
     * <li>{@link Permission#opName} - contains the name of existing permission operation being removed</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @throws com.jts.fortress.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    public void deletePermission(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".deletePermission");
        FortRequest request = new FortRequest();
        perm.setAdmin(true);
        request.setEntity(perm);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.PERM_DELETE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This method will add administrative permission object to admin perms container in directory. The perm object must not exist before making this call.
     * A {@link PermObj} instance exists in a hierarchical, one-many relationship between itself and children as stored in ldap tree: ({@link PermObj}*->{@link Permission}).
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link PermObj#objectName} - contains the name of new object being added</li>
     * <li>{@link PermObj#ou} - contains the name of an existing PERMS OrgUnit this object is associated with</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link PermObj#description} - any safe text</li>
     * <li>{@link PermObj#type} - contains any safe text</li>
     * <li>{@link PermObj#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * </ul>
     *
     * @param pObj must contain the {@link PermObj#objectName} and {@link PermObj#ou}.  The other attributes are optional.
     * @return copy of permObj entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    public PermObj addPermObj(PermObj pObj)
        throws SecurityException
    {
        VUtil.assertNotNull(pObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".addPermObj");
        PermObj retObj = null;
        FortRequest request = new FortRequest();
        pObj.setAdmin(true);
        request.setEntity(pObj);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.OBJ_ADD);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retObj = (PermObj) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retObj;
    }

    /**
     * This method will update administrative permission object in perms container in directory.  The perm object must exist before making this call.
     * A {@link PermObj} instance exists in a hierarchical, one-many relationship between itself and children as stored in ldap tree: ({@link PermObj}*->{@link Permission}).
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link PermObj#objectName} - contains the name of existing object being updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link PermObj#ou} - contains the name of an existing PERMS OrgUnit this object is associated with</li>
     * <li>{@link PermObj#description} - any safe text</li>
     * <li>{@link PermObj#type} - contains any safe text</li>
     * <li>{@link PermObj#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * </ul>
     *
     * @param pObj must contain the {@link PermObj#objectName}. Only non-null attributes will be updated.
     * @return copy of newly updated permObj entity.
     * @throws com.jts.fortress.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    public PermObj updatePermObj(PermObj pObj)
        throws SecurityException
    {
        VUtil.assertNotNull(pObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".updatePermObj");
        PermObj retObj;
        FortRequest request = new FortRequest();
        pObj.setAdmin(true);
        request.setEntity(pObj);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.OBJ_UPDATE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() == 0)
        {
            retObj = (PermObj) response.getEntity();
        }
        else
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
        return retObj;
    }

    /**
     * This method will remove administrative permission object from perms container in directory.  This method will also remove
     * in associated permission objects that are attached to this object.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link PermObj#objectName} - contains the name of existing object targeted for removal</li>
     * </ul>
     *
     * @param pObj must contain the {@link PermObj#objectName} of object targeted for removal.
     * @return copy of permObj entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    public void deletePermObj(PermObj pObj)
        throws SecurityException
    {
        VUtil.assertNotNull(pObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".deletePermObj");
        FortRequest request = new FortRequest();
        pObj.setAdmin(true);
        request.setEntity(pObj);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.OBJ_DELETE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This command grants an AdminRole the administrative permission to perform an operation on an object to a role.
     * The command is implemented by granting administrative permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * and the adminRole is a member of the ADMIN_ROLES data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the object name</li>
     * <li>{@link Permission#opName} - contains the operation name</li>
     * <li>{@link AdminRole#name} - contains the adminRole name</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param role must contains {@link AdminRole#name}.
     * @throws com.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    public void grantPermission(Permission perm, AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".grantPermission");
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".grantPermission");
        FortRequest request = new FortRequest();
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin(true);
        permGrant.setObjName(perm.getObjectName());
        permGrant.setObjId(perm.getObjectId());
        permGrant.setOpName(perm.getOpName());
        permGrant.setRoleNm(role.getName());
        request.setEntity(permGrant);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ROLE_GRANT);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This command revokes the administrative permission to perform an operation on an object from the set
     * of permissions assigned to an AdminRole. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * the role is a member of the ADMIN_ROLES data set, and the permission is assigned to that AdminRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the object name</li>
     * <li>{@link Permission#opName} - contains the operation name</li>
     * <li>{@link AdminRole#name} - contains the adminRole name</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param role must contains {@link AdminRole#name}.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    public void revokePermission(Permission perm, AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".revokePermission");
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".revokePermission");
        FortRequest request = new FortRequest();
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin(true);
        permGrant.setObjName(perm.getObjectName());
        permGrant.setObjId(perm.getObjectId());
        permGrant.setOpName(perm.getOpName());
        permGrant.setRoleNm(role.getName());
        request.setEntity(permGrant);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.ROLE_REVOKE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This command grants a user the administrative permission to perform an operation on an object to a user.
     * The command is implemented by granting administrative permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents an administrative permission,
     * and the user is a member of the USERS data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the object name</li>
     * <li>{@link Permission#opName} - contains the operation name</li>
     * <li>{@link User#userId} - contains the userId</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param user must contain {@link User#userId} of target User entity.
     * @throws com.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    public void grantPermission(Permission perm, User user)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".grantPermissionUser");
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".grantPermissionUser");
        FortRequest request = new FortRequest();
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin(true);
        permGrant.setObjName(perm.getObjectName());
        permGrant.setObjId(perm.getObjectId());
        permGrant.setOpName(perm.getOpName());
        permGrant.setUserId(user.getUserId());
        request.setEntity(permGrant);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.USER_GRANT);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * This command revokes the administrative permission to perform an operation on an object from the set
     * of permissions assigned to a user. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents an administrative permission,
     * the user is a member of the USERS data set, and the permission is assigned to that user.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link Permission#objectName} - contains the object name</li>
     * <li>{@link Permission#opName} - contains the operation name</li>
     * <li>{@link User#userId} - contains the userId</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target.
     * @param user must contain {@link User#userId} of target User entity.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    public void revokePermission(Permission perm, User user)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".revokePermission");
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".revokePermission");
        FortRequest request = new FortRequest();
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin(true);
        permGrant.setObjName(perm.getObjectName());
        permGrant.setObjId(perm.getObjectId());
        permGrant.setOpName(perm.getOpName());
        permGrant.setUserId(user.getUserId());
        request.setEntity(permGrant);
        if (this.adminSess != null)
        {
            request.setSession(adminSess);
        }
        String szRequest = RestUtils.marshal(request);
        String szResponse = RestUtils.post(szRequest, HttpIds.USER_REVOKE);
        FortResponse response = RestUtils.unmarshall(szResponse);
        if (response.getErrorCode() != 0)
        {
            throw new SecurityException(response.getErrorCode(), response.getErrorMessage());
        }
    }
}
