/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rest;

import us.jts.fortress.*;
import us.jts.fortress.SecurityException;
import us.jts.fortress.rbac.*;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.util.attr.VUtil;


/**
 * This class implements the ARBAC02 DelAdminMgr interface for performing policy administration of Fortress ARBAC entities
 * using HTTP access to En Masse REST server.
 * These APIs map directly to similar named APIs specified by ARBAC02 functions.  The ARBAC Functional specification describes delegated administrative
 * operations for the creation and maintenance of ARBAC element sets and relations.  Delegated administrative review functions for performing administrative queries
 * and system functions for creating and managing ARBAC attributes on user sessions and making delegated administrative access control decisions.
 * This class is NOT thread safe.
 * <h3>Administrative Role Based Access Control (ARBAC)</h3>
 * <img src="../doc-files/ARbac.png">
 * <p/>
 * Fortress fully supports the Oh/Sandhu/Zhang ARBAC02 model for delegated administration.  ARBAC provides large enterprises the capability to delegate administrative authority to users that reside outside of the security admin group.
 * Decentralizing administration helps because it provides security provisioning capability to work groups without sacrificing regulations for accountability or traceability.
 * <p/>
 *
 * @author Shawn McKinney
 */
public final class DelAdminMgrRestImpl extends us.jts.fortress.rbac.Manageable implements DelAdminMgr
{
    private static final String CLS_NM = DelAdminMgrRestImpl.class.getName();

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
     * <li>{@link us.jts.fortress.rbac.AdminRole#description} - contains any safe text</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#osPs} * - multi-occurring attribute used to set associations to existing PERMS OrgUnits</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#osUs} * - multi-occurring attribute used to set associations to existing USERS OrgUnits</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#beginRange} - contains the name of an existing RBAC Role that represents the lowest role in hierarchy that administrator (whoever has this AdminRole activated) controls</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#endRange} - contains the name of an existing RBAC Role that represents that highest role in hierarchy that administrator may control</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#beginInclusive} - if 'true' the RBAC Role specified in beginRange is also controlled by the posessor of this AdminRole</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#endInclusive} - if 'true' the RBAC Role specified in endRange is also controlled by the administratrator</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#beginTime} - HHMM - determines begin hour adminRole may be activated into user's ARBAC session</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#endTime} - HHMM - determines end hour adminRole may be activated into user's ARBAC session.</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#beginDate} - YYYYMMDD - determines date when adminRole may be activated into user's ARBAC session</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#endDate} - YYYYMMDD - indicates latest date adminRole may be activated into user's ARBAC session</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's ARBAC session</li>
     * </ul>
     *
     * @param role Contains role name and description.
     * @return Override contains reference to entity added.
     * @throws us.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    @Override
    public AdminRole addRole(AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, CLS_NM + ".addRole");
        AdminRole retRole;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>{@link us.jts.fortress.rbac.AdminRole#name} - contains the name of the new AdminRole being targeted for removal</li>
     * </ul>
     * <p/>
     *
     * @param role Contains role name.
     * @throws us.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    @Override
    public void deleteRole(AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, CLS_NM + ".deleteRole");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * Method will update an AdminRole entity in the directory.  The role must exist in directory prior to this call.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link AdminRole#name} - contains the name of the new AdminRole being targeted for updating</li>
     * </ul>
     * <p/>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.AdminRole#description} - contains any safe text</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#osPs} * - multi-occurring attribute used to set associations to existing PERMS OrgUnits</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#osUs} * - multi-occurring attribute used to set associations to existing USERS OrgUnits</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#beginRange} - contains the name of an existing RBAC Role that represents the lowest role in hierarchy that administrator (whoever has this AdminRole activated) controls</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#endRange} - contains the name of an existing RBAC Role that represents that highest role in hierarchy that administrator may control</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#beginInclusive} - if 'true' the RBAC Role specified in beginRange is also controlled by the posessor of this AdminRole</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#endInclusive} - if 'true' the RBAC Role specified in endRange is also controlled by the administratrator</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#beginTime} - HHMM - determines begin hour adminRole may be activated into user's ARBAC session</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#endTime} - HHMM - determines end hour adminRole may be activated into user's ARBAC session.</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#beginDate} - YYYYMMDD - determines date when adminRole may be activated into user's ARBAC session</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#endDate} - YYYYMMDD - indicates latest date adminRole may be activated into user's ARBAC session</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's ARBAC session</li>
     * </ul>
     *
     * @param role Contains role name and new description.
     * @return Override contains reference to entity updated.
     * @throws us.jts.fortress.SecurityException
     *          Description of the Exception
     */
    @Override
    public AdminRole updateRole(AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(role, GlobalErrIds.ARLE_NULL, CLS_NM + ".updateRole");
        AdminRole retRole;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#beginTime} - HHMM - determines begin hour AdminRole may be activated into user's RBAC session</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#endTime} - HHMM - determines end hour AdminRole may be activated into user's RBAC session.</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#beginDate} - YYYYMMDD - determines date when AdminRole may be activated into user's RBAC session</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#endDate} - YYYYMMDD - indicates latest date AdminRole may be activated into user's RBAC session</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's ARBAC session</li>
     * </ul>
     *
     * @param uAdminRole entity contains {@link User#userId} and {@link AdminRole#name} and optional {@code Constraints}.
     * @throws SecurityException in the event data error in user or role objects or system error.
     */
    @Override
    public void assignUser(UserAdminRole uAdminRole)
        throws SecurityException
    {
        VUtil.assertNotNull(uAdminRole, GlobalErrIds.ARLE_NULL, CLS_NM + ".assignUser");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#name} - contains the name for already existing AdminRole to be deassigned</li>
     * <li>{@link us.jts.fortress.rbac.UserAdminRole#userId} - contains the userId for existing User</li>
     * </ul>
     *
     * @param uAdminRole entity contains {@link User#userId} and {@link AdminRole#name}.
     * @throws SecurityException - in the event data error in user or role objects or system error.
     */
    @Override
    public void deassignUser(UserAdminRole uAdminRole)
        throws SecurityException
    {
        VUtil.assertNotNull(uAdminRole, GlobalErrIds.ARLE_NULL, CLS_NM + ".deassignUser");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>{@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit to be added</li>
     * <li>{@link us.jts.fortress.rbac.OrgUnit#type} - contains the type of OU:  {@link us.jts.fortress.rbac.OrgUnit.Type#USER} or {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link OrgUnit#description} - contains any safe text</li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return OrgUnit contains reference to entity added.
     * @throws us.jts.fortress.SecurityException
     *          in the event of data validation or system error.
     */
    @Override
    public OrgUnit add(OrgUnit entity)
        throws SecurityException
    {
        VUtil.assertNotNull(entity, GlobalErrIds.ORG_NULL, CLS_NM + ".addOU");
        OrgUnit retOrg;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>{@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit to be updated</li>
     * <li>{@link us.jts.fortress.rbac.OrgUnit#type} - contains the type of OU:  {@link us.jts.fortress.rbac.OrgUnit.Type#USER} or {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link OrgUnit#description} - contains any safe text</li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return OrgUnit contains reference to entity updated.
     * @throws us.jts.fortress.SecurityException
     *          in the event of data validation or system error.
     */
    @Override
    public OrgUnit update(OrgUnit entity)
        throws SecurityException
    {
        VUtil.assertNotNull(entity, GlobalErrIds.ORG_NULL, CLS_NM + ".updateOU");
        OrgUnit retOrg;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>{@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of new USERS or PERMS OrgUnit to be removed</li>
     * <li>{@link us.jts.fortress.rbac.OrgUnit#type} - contains the type of OU:  {@link us.jts.fortress.rbac.OrgUnit.Type#USER} or {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * </ul>
     *
     * @param entity contains OrgUnit name and type.
     * @return OrgUnit contains reference to entity removed.
     * @throws SecurityException in the event of data validation or system error.
     */
    @Override
    public OrgUnit delete(OrgUnit entity)
        throws SecurityException
    {
        VUtil.assertNotNull(entity, GlobalErrIds.ORG_NULL, CLS_NM + ".deleteOU");
        OrgUnit retOrg;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>parentRole - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to be parent</li>
     * <li>parentRole - {@link us.jts.fortress.rbac.OrgUnit#type} - contains the type of OrgUnit targeted: {@link us.jts.fortress.rbac.OrgUnit.Type#USER} or {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * <li>childRole - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of new OrgUnit to be child</li>
     * </ul>
     * <h4>optional parameters child</h4>
     * <ul>
     * <li>childRole - {@link us.jts.fortress.rbac.OrgUnit#description} - maps to description attribute on organizationalUnit object class for new child</li>
     * </ul>
     *
     * @param parent This entity must be present in ORGUNIT data set.  Success will add rel with child.
     * @param child  This entity must not be present in ORGUNIT data set.  Success will add the new entity to ORGUNIT data set.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void addDescendant(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "addDescendantOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(child, GlobalErrIds.ORG_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>parent - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to be parent</li>
     * <li>parent - {@link us.jts.fortress.rbac.OrgUnit#type} - contains the type of OrgUnit targeted: {@link us.jts.fortress.rbac.OrgUnit.Type#USER} or {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * <li>child - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of new OrgUnit to be child</li>
     * </ul>
     * <h4>optional parameters child</h4>
     * <ul>
     * <li>child - {@link us.jts.fortress.rbac.OrgUnit#description} - maps to description attribute on organizationalUnit object class for new child</li>
     * </ul>
     *
     * @param parent completion of op assigns new child relationship with child orgunit.
     * @param child  completion of op assigns new parent relationship with parent orgunit.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void addAscendant(OrgUnit child, OrgUnit parent)
        throws SecurityException
    {
        String methodName = "addAscendantOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>parent - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to be parent</li>
     * <li>parent - {@link us.jts.fortress.rbac.OrgUnit#type} - contains the type of OrgUnit targeted: {@link us.jts.fortress.rbac.OrgUnit.Type#USER} or {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * <li>child - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to be child</li>
     * </ul>
     *
     * @param parent completion of op deassigns child relationship with child orgunit.
     * @param child  completion of op deassigns parent relationship with parent orgunit.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void addInheritance(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "addInheritanceOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(child, GlobalErrIds.ORG_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>parent - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to remove as parent</li>
     * <li>parent - {@link us.jts.fortress.rbac.OrgUnit#type} - contains the type of OrgUnit targeted: {@link us.jts.fortress.rbac.OrgUnit.Type#USER} or {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}</li>
     * <li>child - {@link us.jts.fortress.rbac.OrgUnit#name} - contains the name of existing OrgUnit to remove as child</li>
     * </ul>
     *
     * @param parent completion of op removes child relationship with childRole.
     * @param child  completion of op removes parent relationship with parentRole.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void deleteInheritance(OrgUnit parent, OrgUnit child)
        throws SecurityException
    {
        String methodName = "deleteInheritanceOU";
        VUtil.assertNotNull(parent, GlobalErrIds.ORG_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(parent.getType(), GlobalErrIds.ORG_TYPE_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(child, GlobalErrIds.ORG_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * This command creates a new role childRole, and inserts it in the role hierarchy as an immediate descendant of
     * the existing role parentRole. The command is valid if and only if childRole is not a member of the ADMINROLES data set,
     * and parentRole is a member of the ADMINROLES data set.
     * <p/>
     * This method:
     * 1 - Adds new role.
     * 2 - Assigns role relationship between new childRole and pre-existing parentRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link us.jts.fortress.rbac.AdminRole#name} - contains the name of existing Role to be parent</li>
     * <li>childRole - {@link us.jts.fortress.rbac.AdminRole#name} - contains the name of new Role to be child</li>
     * </ul>
     * <h4>optional parameters childRole</h4>
     * <ul>
     * <li>childRole - {@link us.jts.fortress.rbac.AdminRole#description} - maps to description attribute on organizationalRole object class for new child</li>
     * <li>childRole - {@link us.jts.fortress.rbac.AdminRole#beginTime} - HHMM - determines begin hour role may be activated into user's session for new child</li>
     * <li>childRole - {@link us.jts.fortress.rbac.AdminRole#endTime} - HHMM - determines end hour role may be activated into user's session for new child</li>
     * <li>childRole - {@link us.jts.fortress.rbac.AdminRole#beginDate} - YYYYMMDD - determines date when role may be activated into user's session for new child</li>
     * <li>childRole - {@link us.jts.fortress.rbac.AdminRole#endDate} - YYYYMMDD - indicates latest date role may be activated into user's session for new child</li>
     * <li>childRole - {@link us.jts.fortress.rbac.AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status for new child</li>
     * <li>childRole - {@link us.jts.fortress.rbac.AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status for new child</li>
     * <li>childRole - {@link us.jts.fortress.rbac.AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's session for new child</li>
     * </ul>
     *
     * @param parentRole This entity must be present in ADMINROLES data set.  Success will add role rel with childRole.
     * @param childRole  This entity must not be present in ADMINROLES data set.  Success will add the new role entity to ADMINROLES data set.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void addDescendant(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "addDescendantRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * This command creates a new role parentRole, and inserts it in the role hierarchy as an immediate ascendant of
     * the existing role childRole. The command is valid if and only if parentRole is not a member of the ROLES data set,
     * and childRole is a member of the ROLES data set.
     * This method:
     * 1 - Adds new role.
     * 2 - Assigns role relationship between new parentRole and pre-existing childRole.
     * <h4>required parameters</h4>
     * <ul>
     * <li>childRole - {@link us.jts.fortress.rbac.AdminRole#name} - contains the name of existing Role to be child</li>
     * <li>parentRole - {@link us.jts.fortress.rbac.AdminRole#name} - contains the name of new Role to be added as parent</li>
     * </ul>
     * <h4>optional parameters parentRole</h4>
     * <ul>
     * <li>parentRole - {@link us.jts.fortress.rbac.AdminRole#description} - maps to description attribute on organizationalRole object class for new parent</li>
     * <li>parentRole - {@link us.jts.fortress.rbac.AdminRole#beginTime} - HHMM - determines begin hour role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link us.jts.fortress.rbac.AdminRole#endTime} - HHMM - determines end hour role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link us.jts.fortress.rbac.AdminRole#beginDate} - YYYYMMDD - determines date when role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link us.jts.fortress.rbac.AdminRole#endDate} - YYYYMMDD - indicates latest date role may be activated into user's session for new parent</li>
     * <li>parentRole - {@link us.jts.fortress.rbac.AdminRole#beginLockDate} - YYYYMMDD - determines beginning of enforced inactive status for new parent</li>
     * <li>parentRole - {@link us.jts.fortress.rbac.AdminRole#endLockDate} - YYYYMMDD - determines end of enforced inactive status for new parent</li>
     * <li>parentRole - {@link us.jts.fortress.rbac.AdminRole#dayMask} - 1234567, 1 = Sunday, 2 = Monday, etc - specifies which day role may be activated into user's session for new parent</li>
     * </ul>
     *
     * @param parentRole completion of op assigns new child relationship with childRole.
     * @param childRole  completion of op assigns new parent relationship with parentRole.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void addAscendant(AdminRole childRole, AdminRole parentRole)
        throws SecurityException
    {
        String methodName = "addAscendantRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * This command establishes a new immediate inheritance relationship parentRole <<-- childRole between existing
     * roles parentRole, childRole. The command is valid if and only if parentRole and childRole are members of the ROLES data
     * set, parentRole is not an immediate ascendant of childRole, and childRole does not properly inherit parentRole (in order to
     * avoid cycle creation).
     * <h4>required parameters</h4>
     * <ul>
     * <li>parentRole - {@link us.jts.fortress.rbac.AdminRole#name} - contains the name of existing AdminRole to be parent</li>
     * <li>childRole - {@link us.jts.fortress.rbac.AdminRole#name} - contains the name of existing AdminRole to be child</li>
     * </ul>
     *
     * @param parentRole completion of op deassigns child relationship with childRole.
     * @param childRole  completion of op deassigns parent relationship with parentRole.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void addInheritance(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "addInheritanceRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>parentRole - {@link us.jts.fortress.rbac.AdminRole#name} - contains the name of existing AdminRole to remove as parent</li>
     * <li>childRole - {@link us.jts.fortress.rbac.AdminRole#name} - contains the name of existing AdminRole to remove as child</li>
     * </ul>
     *
     * @param parentRole completion of op removes child relationship with childRole.
     * @param childRole  completion of op removes parent relationship with parentRole.
     * @throws us.jts.fortress.SecurityException
     *          thrown in the event of data validation or system error.
     */
    @Override
    public void deleteInheritance(AdminRole parentRole, AdminRole childRole)
        throws SecurityException
    {
        String methodName = "deleteInheritanceRole";
        VUtil.assertNotNull(parentRole, GlobalErrIds.ARLE_PARENT_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNull(childRole, GlobalErrIds.ARLE_CHILD_NULL, CLS_NM + "." + methodName);
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * The perm operation entity may have {@link us.jts.fortress.rbac.AdminRole} or {@link us.jts.fortress.rbac.User} associations.  The target {@link us.jts.fortress.rbac.Permission} must not exist prior to calling.
     * A Fortress Permission instance exists in a hierarchical, one-many relationship between its parent and itself as stored in ldap tree: ({@link us.jts.fortress.rbac.PermObj}*->{@link us.jts.fortress.rbac.Permission}).
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.Permission#objectName} - contains the name of existing object being targeted for the permission add</li>
     * <li>{@link us.jts.fortress.rbac.Permission#opName} - contains the name of new permission operation being added</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.Permission#roles} * - multi occurring attribute contains RBAC Roles that permission operation is being granted to</li>
     * <li>{@link us.jts.fortress.rbac.Permission#users} * - multi occurring attribute contains Users that permission operation is being granted to</li>
     * <li>{@link us.jts.fortress.rbac.Permission#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * <li>{@link us.jts.fortress.rbac.Permission#type} - any safe text</li>
     * </ul>
     *
     * @param perm must contain the object, {@link us.jts.fortress.rbac.Permission#objectName}, and operation, {@link us.jts.fortress.rbac.Permission#opName}, that identifies target along with optional other attributes..
     * @return copy of Permission entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    @Override
    public Permission addPermission(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".addPermission");
        Permission retPerm;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * The perm operation entity may also contain {@link us.jts.fortress.rbac.AdminRole} or {@link us.jts.fortress.rbac.User} associations to add or remove using this function.
     * The perm operation must exist before making this call.  Only non-null attributes will be updated.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.Permission#objectName} - contains the name of existing object being targeted for the permission update</li>
     * <li>{@link us.jts.fortress.rbac.Permission#opName} - contains the name of existing permission operation being updated</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.Permission#roles} * - multi occurring attribute contains RBAC Roles that permission operation is being granted to</li>
     * <li>{@link us.jts.fortress.rbac.Permission#users} * - multi occurring attribute contains Users that permission operation is being granted to</li>
     * <li>{@link us.jts.fortress.rbac.Permission#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * <li>{@link us.jts.fortress.rbac.Permission#type} - any safe text</li>
     * </ul>
     *
     * @param perm must contain the object, {@link Permission#objectName}, and operation, {@link Permission#opName}, that identifies target and any optional data to update.  Null or empty attributes will be ignored.
     * @return copy of permOp entity.
     * @throws us.jts.fortress.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    @Override
    public Permission updatePermission(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".updatePermission");
        Permission retPerm;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>{@link us.jts.fortress.rbac.Permission#objectName} - contains the name of existing object being targeted for the permission delete</li>
     * <li>{@link us.jts.fortress.rbac.Permission#opName} - contains the name of existing permission operation being removed</li>
     * </ul>
     *
     * @param perm must contain the object, {@link us.jts.fortress.rbac.Permission#objectName}, and operation, {@link us.jts.fortress.rbac.Permission#opName}, that identifies target.
     * @throws us.jts.fortress.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    @Override
    public void deletePermission(Permission perm)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".deletePermission");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * A {@link PermObj} instance exists in a hierarchical, one-many relationship between itself and children as stored in ldap tree: ({@link us.jts.fortress.rbac.PermObj}*->{@link us.jts.fortress.rbac.Permission}).
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.PermObj#objectName} - contains the name of new object being added</li>
     * <li>{@link us.jts.fortress.rbac.PermObj#ou} - contains the name of an existing PERMS OrgUnit this object is associated with</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link us.jts.fortress.rbac.PermObj#description} - any safe text</li>
     * <li>{@link us.jts.fortress.rbac.PermObj#type} - contains any safe text</li>
     * <li>{@link us.jts.fortress.rbac.PermObj#props} * - multi-occurring property key and values are separated with a ':'.  e.g. mykey1:myvalue1</li>
     * </ul>
     *
     * @param pObj must contain the {@link us.jts.fortress.rbac.PermObj#objectName} and {@link us.jts.fortress.rbac.PermObj#ou}.  The other attributes are optional.
     * @return copy of permObj entity.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    @Override
    public PermObj addPermObj(PermObj pObj)
        throws SecurityException
    {
        VUtil.assertNotNull(pObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".addPermObj");
        PermObj retObj;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * A {@link us.jts.fortress.rbac.PermObj} instance exists in a hierarchical, one-many relationship between itself and children as stored in ldap tree: ({@link us.jts.fortress.rbac.PermObj}*->{@link us.jts.fortress.rbac.Permission}).
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
     * @throws us.jts.fortress.SecurityException
     *          - thrown in the event of perm object data or system error.
     */
    @Override
    public PermObj updatePermObj(PermObj pObj)
        throws SecurityException
    {
        VUtil.assertNotNull(pObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".updatePermObj");
        PermObj retObj;
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>{@link us.jts.fortress.rbac.PermObj#objectName} - contains the name of existing object targeted for removal</li>
     * </ul>
     *
     * @param pObj must contain the {@link us.jts.fortress.rbac.PermObj#objectName} of object targeted for removal.
     * @throws SecurityException - thrown in the event of perm object data or system error.
     */
    @Override
    public void deletePermObj(PermObj pObj)
        throws SecurityException
    {
        VUtil.assertNotNull(pObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".deletePermObj");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>{@link us.jts.fortress.rbac.Permission#objectName} - contains the object name</li>
     * <li>{@link us.jts.fortress.rbac.Permission#opName} - contains the operation name</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#name} - contains the adminRole name</li>
     * </ul>
     *
     * @param perm must contain the object, {@link us.jts.fortress.rbac.Permission#objectName}, and operation, {@link us.jts.fortress.rbac.Permission#opName}, that identifies target.
     * @param role must contains {@link us.jts.fortress.rbac.AdminRole#name}.
     * @throws us.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    @Override
    public void grantPermission(Permission perm, AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".grantPermission");
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".grantPermission");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>{@link us.jts.fortress.rbac.Permission#objectName} - contains the object name</li>
     * <li>{@link us.jts.fortress.rbac.Permission#opName} - contains the operation name</li>
     * <li>{@link us.jts.fortress.rbac.AdminRole#name} - contains the adminRole name</li>
     * </ul>
     *
     * @param perm must contain the object, {@link us.jts.fortress.rbac.Permission#objectName}, and operation, {@link us.jts.fortress.rbac.Permission#opName}, that identifies target.
     * @param role must contains {@link us.jts.fortress.rbac.AdminRole#name}.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    @Override
    public void revokePermission(Permission perm, AdminRole role)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".revokePermission");
        VUtil.assertNotNull(role, GlobalErrIds.ROLE_NULL, CLS_NM + ".revokePermission");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>{@link us.jts.fortress.rbac.Permission#objectName} - contains the object name</li>
     * <li>{@link us.jts.fortress.rbac.Permission#opName} - contains the operation name</li>
     * <li>{@link us.jts.fortress.rbac.User#userId} - contains the userId</li>
     * </ul>
     *
     * @param perm must contain the object, {@link us.jts.fortress.rbac.Permission#objectName}, and operation, {@link us.jts.fortress.rbac.Permission#opName}, that identifies target.
     * @param user must contain {@link us.jts.fortress.rbac.User#userId} of target User entity.
     * @throws us.jts.fortress.SecurityException
     *          Thrown in the event of data validation or system error.
     */
    @Override
    public void grantPermission(Permission perm, User user)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".grantPermissionUser");
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".grantPermissionUser");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
     * <li>{@link us.jts.fortress.rbac.Permission#objectName} - contains the object name</li>
     * <li>{@link us.jts.fortress.rbac.Permission#opName} - contains the operation name</li>
     * <li>{@link us.jts.fortress.rbac.User#userId} - contains the userId</li>
     * </ul>
     *
     * @param perm must contain the object, {@link us.jts.fortress.rbac.Permission#objectName}, and operation, {@link us.jts.fortress.rbac.Permission#opName}, that identifies target.
     * @param user must contain {@link us.jts.fortress.rbac.User#userId} of target User entity.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    @Override
    public void revokePermission(Permission perm, User user)
        throws SecurityException
    {
        VUtil.assertNotNull(perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".revokePermission");
        VUtil.assertNotNull(user, GlobalErrIds.USER_NULL, CLS_NM + ".revokePermission");
        FortRequest request = new FortRequest();
        request.setContextId(this.contextId);
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
