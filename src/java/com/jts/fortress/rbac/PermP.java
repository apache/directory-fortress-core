/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.FinderException;
import com.jts.fortress.SecurityException;
import com.jts.fortress.ValidationException;
import com.jts.fortress.arbac.AdminRole;
import com.jts.fortress.arbac.OrgUnit;
import com.jts.fortress.arbac.OrgUnitP;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.util.attr.VUtil;
import com.jts.fortress.util.time.CUtil;

import java.util.List;

/**
 * Process module for the Permission entity.  This class performs data validations and error mapping.  It is typically called
 * by internal Fortress manager classes ({@link com.jts.fortress.rbac.AdminMgrImpl}, {@link com.jts.fortress.rbac.AccessMgrImpl},
 * {@link com.jts.fortress.rbac.ReviewMgrImpl}, ...) and not intended for external non-Fortress clients.  This class will accept,
 * {@link com.jts.fortress.rbac.PermObj} or {@link com.jts.fortress.rbac.Permission}, validate its contents and forward on to it's corresponding DAO class {@link com.jts.fortress.rbac.PermDAO}.
 * <p>
 * Class will throw {@link com.jts.fortress.SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link com.jts.fortress.FinderException},
 * {@link com.jts.fortress.CreateException},{@link com.jts.fortress.UpdateException},{@link com.jts.fortress.RemoveException}),
 *  or {@link com.jts.fortress.ValidationException} as {@link com.jts.fortress.SecurityException}s with appropriate
 * error id from {@link com.jts.fortress.constants.GlobalErrIds}.
 * <p>
 * This object is thread safe.
 * </p>

 *
 * @author smckinn
 * @created September 29, 2009
 */
public class PermP
{
    /**
     * Description of the Field
     */
    private static final String OCLS_NM = PermP.class.getName();
    private static final PermDAO pDao = new PermDAO();
    private static final OrgUnitP op = new OrgUnitP();

    /**
     * This function returns a Boolean value meaning whether the subject of a given session is
     * allowed or not to perform a given operation on a given object. The function is valid if and
     * only if the session is a valid Fortress session, the object is a member of the OBJS data set,
     * and the operation is a member of the OPS data set. The session's subject has the permission
     * to perform the operation on that object if and only if that permission is assigned to (at least)
     * one of the session's active roles. This implementation will verify the roles or userId correspond
     * to the subject's active roles are registered in the object's access control list.
     *
     * @param session    This object must be instantiated by calling "createSession" method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param permission object contains obj attribute which is a String and contains the name of the object user is trying to access;
     *                   perm object contains operation attribute which is also a String and contains the operation name for the object.
     * @return True of user has access, false otherwise.
     * @throws SecurityException in the event of data validation failure, security policy violation or DAO error.
     */
    public boolean checkPermission(Session session, Permission permission)
        throws SecurityException
    {
        return pDao.checkPermission(session, permission);
    }


    /**
     * Takes a Permission entity that contains full or partial object name and/or full or partial operation name for search.
     *
     * @param permission contains all or partial object name and/or all or partial operation name.
     * @return List of type Permission containing fully populated matching Permission entities.
     * @throws com.jts.fortress.SecurityException in the event of DAO search error.
     */
    public final List<Permission> search(Permission permission)
        throws SecurityException
    {
        return pDao.findPermissions(permission);
    }

    /**
     * Takes a Permission object entity that contains full or partial object name for search Permission Objects in directory..
     *
     * @param permObj contains all or partial object name.
     * @return List of type Permission Objects containing fully populated matching entities.
     * @throws SecurityException in the event of DAO search error.
     */
    public final List<PermObj> search(PermObj permObj)
        throws SecurityException
    {
        return pDao.findPermissions(permObj);
    }

    /**
     * Takes an OrgUnit entity that contains full or partial orgUnitId for search Permission Objects in directory..
     *
     * @param ou contains all or OrgUnitId.
     * @return List of type Permission Objects containing fully populated matching entities.
     * @throws SecurityException in the event of DAO search error.
     */
    public final List<PermObj> search(OrgUnit ou, boolean limitSize)
        throws SecurityException
    {
        return pDao.findPermissions(ou, limitSize);
    }


    /**
     * Search will return a list of matching permissions that are assigned to a given RBAC or Admin role name.  The
     * DAO class will search the Admin perms if the "isAdmin" boolean flag is "true", otherwise it will search RBAC perm tree.
     *
     * @param role contains the RBAC or Admin Role name targeted for search.
     * @return List of type Permission containing fully populated matching Permission entities.
     * @throws SecurityException in the event of DAO search error.
     */
    public final List<Permission> search(Role role)
        throws SecurityException
    {
        return pDao.findPermissions(role);
    }

    /**
     * Search will return a list of matching permissions that are assigned to a given User.  This method searches
     * the RBAC perms only.
     *
     * @param user contains the userId targeted for search.
     * @return List of type Permission containing fully populated matching Permission entities.
     * @throws SecurityException in the event of DAO search error.
     */
    public final List<Permission> search(User user)
        throws SecurityException
    {
        return pDao.findPermissions(user);
    }


    /**
     * Remove the User assignment attribute from all RBAC permssions.  This method is called by AdminMgrImpl
     * when the User is being deleted.
     *
     * @param user contains the userId targeted for attribute removal.
     * @throws SecurityException in the event of DAO search error.
     */
    public final void remove(User user)
        throws SecurityException
    {
        List<Permission> list;
        try
        {
            list = pDao.findUserPermissions(user);
            for (Permission perm : list)
            {
                revoke(perm, user);
            }
        }
        catch (FinderException fe)
        {
            String error = OCLS_NM + ".remove userId <" + user.getUserId() + "> caught FinderException=" + fe;
            throw new SecurityException(GlobalErrIds.PERM_BULK_USER_REVOKE_FAILED, error, fe);
        }
    }


    /**
     * Remove the RBAC Role assignment attribute from all RBAC permssions.  This method is called by AdminMgrImpl
     * when the RBAC Role is being deleted.
     *
     * @param role contains the name of Role targeted for attribute removal.
     * @throws SecurityException in the event of DAO search error.
     */
    public final void remove(Role role)
        throws SecurityException
    {
        List<Permission> list;
        try
        {
            list = pDao.findPermissions(role);
            for (Permission perm : list)
            {
                revoke(perm, role);
            }
        }
        catch (FinderException fe)
        {
            String error = OCLS_NM + ".remove role <" + role.getName() + "> caught FinderException=" + fe;
            throw new SecurityException(GlobalErrIds.PERM_BULK_ROLE_REVOKE_FAILED, error, fe);
        }
    }


    /**
     * Remove the Admin Role assignment attribute from all Admin permssions.  This method is called by DelegatedAdminMgrImpl
     * when the AdminRole is being deleted.
     *
     * @param role contains the name of AdminRole targeted for attribute removal.
     * @throws SecurityException in the event of DAO search error.
     */
    public final void remove(AdminRole role)
        throws SecurityException
    {
        List<Permission> list;
        try
        {
            list = pDao.findPermissions(role);
            for (Permission perm : list)
            {
                perm.setAdmin(true);
                revoke(perm, role);
            }
        }
        catch (FinderException fe)
        {
            String error = OCLS_NM + ".remove admin role <" + role.getName() + "> caught FinderException=" + fe;
            throw new SecurityException(GlobalErrIds.PERM_BULK_ADMINROLE_REVOKE_FAILED, error, fe);
        }
    }


    /**
     * This function returns the permissions of the session, i.e., the permissions assigned
     * to its authorized roles. The function is valid if and only if the session is a valid Fortress session.
     *
     * @param session This object must be instantiated by calling "createSession" method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @return List<Permission> containing permissions (op, obj) active for user's session.
     * @throws com.jts.fortress.SecurityException is thrown if runtime error occurs with system.
     */
    public final List<Permission> search(Session session)
        throws SecurityException
    {
        return pDao.findPermissions(session);
    }


    /**
     * Return the matching Permission entity.  This method will throw SecurityException if not found.
     *
     * @param permission contains the full permission object and operation name.
     * @return Permission containing fully populated matching object.
     * @throws com.jts.fortress.SecurityException is thrown if permission not found or runtime error occurs with system.
     */
    public Permission read(Permission permission)
        throws SecurityException
    {
        return pDao.getPerm(permission);
    }


    /**
     * Return the matching Permission object entity.  This method will throw SecurityException if not found.
     *
     * @param permObj contains the full permission object name.
     * @return PermObj containing fully populated matching object.
     * @throws com.jts.fortress.SecurityException is thrown if perm object not found or runtime error occurs with system.
     */
    public PermObj read(PermObj permObj)
        throws SecurityException
    {
        return pDao.getPerm(permObj);
    }


    /**
     * Adds a new Permission Object entity to directory.  The Permission Object entity input will be validated to ensure that:
     * object name is present, orgUnitId is valid, reasonability checks on all of the
     * other populated values.
     *
     * @param entity Permission object entity contains data targeted for insertion.
     * @return Permission entity copy of input + additional attributes (internalId) that were added by op.
     * @throws com.jts.fortress.SecurityException in the event of data validation or DAO system error.
     */
    public PermObj add(PermObj entity)
        throws SecurityException
    {
        validate(entity, false);
        return pDao.createObject(entity);
    }


    /**
     * Adds a new Permission operation entity to directory.  The Permission operation entity input will be validated to ensure that:
     * operation name is present, roles (optional) are valid, reasonability checks on all of the
     * other populated values.
     *
     * @param entity Permission operation entity contains data targeted for insertion.
     * @return Permission operation entity copy of input + additional attributes (internalId) that were added by op.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    public Permission add(Permission entity)
        throws SecurityException
    {
        validate(entity, false);
        return pDao.createOperation(entity);
    }


    /**
     * Update existing Permission Object attributes with the input entity.  Null or empty attributes will be ignored.
     * The Permission Object entity input will be validated to ensure that:
     * object name is present, orgUnitId is valid, reasonability checks on all of the other populated values.
     *
     * @param entity Permission object entity contains data targeted for updating.
     * @return Permission entity copy of input + additional attributes (internalId) that were updated by op.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    public PermObj update(PermObj entity)
        throws SecurityException
    {
        update(entity, true);
        return entity;
    }


    /**
     * Update existing Permission Object attributes with the input entity.  Null or empty attributes will be ignored.
     * The Permission Object entity input will be validated to ensure that:
     * object name is present, orgUnitId is valid, reasonability checks on all of the other populated values.
     *
     * @param entity   Permission object entity contains data targeted for updating.
     * @param validate if false will skip the validations described above.
     * @return Permission entity copy of input + additional attributes (internalId) that were updated by op.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    private PermObj update(PermObj entity, boolean validate)
        throws SecurityException
    {
        if (validate)
        {
            validate(entity, true);
        }
        return pDao.updateObj(entity);
    }


    /**
     * Update existing Permission Operation Object attributes with the input entity.  Null or empty attributes will be ignored.
     * The Permission Operation Object entity input will be validated to ensure that:
     * object name is present, orgUnitId is valid, reasonability checks on all of the other populated values.
     *
     * @param entity Permission operation object entity contains data targeted for updating.
     * @return Permission entity copy of input + additional attributes (internalId) that were updated by op.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    public Permission update(Permission entity)
        throws SecurityException
    {
        update(entity, true);
        return entity;
    }


    /**
     * Update existing Permission Operation Object attributes with the input entity.  Null or empty attributes will be ignored.
     * The Permission Operation Object entity input will be validated to ensure that:
     * object name is present, orgUnitId is valid, reasonability checks on all of the other populated values.
     *
     * @param entity   Permission operation object entity contains data targeted for updating.
     * @param validate if false will skip the validations described above.
     * @return Permission entity copy of input + additional attributes (internalId) that were updated by op.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    private Permission update(Permission entity, boolean validate)
        throws SecurityException
    {
        if (validate)
        {
            validate(entity, true);
        }
        return pDao.updateOperation(entity);
    }


    /**
     * This method performs a "hard" delete.  It completely removes all data associated with this Permission Object from the directory
     * including the Permission operations..
     * Permission Object entity must exist in directory prior to making this call else exception will be thrown.
     *
     * @param entity Contains the Permission Object name targeted for deletion.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    public void delete(PermObj entity)
        throws SecurityException
    {
        pDao.deleteObj(entity);
    }


    /**
     * This method performs a "hard" delete.  It completely removes all data associated with this Permission Operation from the directory
     * Permission Operation entity must exist in directory prior to making this call else exception will be thrown.
     *
     * @param entity Contains the Permission Operation name targeted for deletion.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    public void delete(Permission entity)
        throws SecurityException
    {
        pDao.deleteOperation(entity);
    }


    /**
     * This command grants a role the permission to perform an operation on an object to a role.
     * The command is implemented by granting permission by setting the access control list of
     * the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * and the role is a member of the ROLES data set.
     *
     * @param pOp  contains object and operation name for resource.
     * @param role contains the role name
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    public final void grant(Permission pOp, Role role)
        throws SecurityException
    {
        // Now assign it to the perm op:
        pDao.grant(pOp, role);
    }


    /**
     * This command revokes the permission to perform an operation on an object from the set
     * of permissions assigned to a role. The command is implemented by setting the access control
     * list of the object involved.
     * The command is valid if and only if the pair (operation, object) represents a permission,
     * the role is a member of the ROLES data set, and the permission is assigned to that role.
     *
     * @param pOp  contains object and operation name for resource.
     * @param role contains role name
     * @throws com.jts.fortress.SecurityException Thrown in the event of data validation or system error.
     */
    public final void revoke(Permission pOp, Role role)
        throws SecurityException
    {
        pDao.revoke(pOp, role);
    }


    /**
     * Method grants a permission directly to a User entity.
     *
     * @param pOp  contains object and operation name for resource.
     * @param user contains userid of User entity.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    public final void grant(Permission pOp, User user)
        throws SecurityException
    {
        // call dao to grant userId access to the perm op:
        pDao.grant(pOp, user);
    }


    /**
     * Method revokes a permission directly from a User entity.
     *
     * @param pOp  contains object and operation name for resource.
     * @param user contains userid of User entity.
     * @throws com.jts.fortress.SecurityException Thrown in the event of data validation or system error.
     */
    public final void revoke(Permission pOp, User user)
        throws SecurityException
    {
        pDao.revoke(pOp, user);
    }


    /**
     * Method will perform various validations to ensure the integrity of the Permission Object entity targeted for insertion
     * or updating in directory.  Data reasonability checks will be performed on all non-null attributes.
     *
     * @param pObj     Permission Object entity contains data targeted for insertion or update.
     * @param isUpdate if true update operation is being performed which specifies a different set of targeted attributes.
     * @throws com.jts.fortress.ValidationException in the event of data validation error.
     */
    void validate(PermObj pObj, boolean isUpdate)
        throws ValidationException
    {
        if (!isUpdate)
        {
            // Validate length
            VUtil.orgUnit(pObj.getOu());
            // ensure ou exists in the OS-P pool:
            OrgUnit ou = new OrgUnit(pObj.getOu(), OrgUnit.Type.PERM);
            if (!op.isValid(ou))
            {
                String error = OCLS_NM + ".validate detected invalid orgUnit name <" + pObj.getOu() + "> for object name <" + pObj.getObjectName() + ">";
                //log.warn(error);
                throw new ValidationException(GlobalErrIds.PERM_OU_INVALID, error);
            }
            if (VUtil.isNotNullOrEmpty(pObj.getObjectName()))
            {
                VUtil.description(pObj.getObjectName());
            }
            if (VUtil.isNotNullOrEmpty(pObj.getOu()))
            {
                VUtil.orgUnit(pObj.getOu());
            }
            if (VUtil.isNotNullOrEmpty(pObj.getDescription()))
            {
                VUtil.description(pObj.getDescription());
            }
        }
        else
        {
            if (VUtil.isNotNullOrEmpty(pObj.getOu()))
            {
                VUtil.orgUnit(pObj.getOu());
                // ensure ou exists in the OS-P pool:
                OrgUnit ou = new OrgUnit(pObj.getOu(), OrgUnit.Type.PERM);
                if (!op.isValid(ou))
                {
                    String error = OCLS_NM + ".validate detected invalid orgUnit name <" + pObj.getOu() + "> for object name <" + pObj.getObjectName() + ">";
                    throw new ValidationException(GlobalErrIds.PERM_OU_INVALID, error);
                }
            }
            if (VUtil.isNotNullOrEmpty(pObj.getDescription()))
            {
                VUtil.description(pObj.getDescription());
            }
        }
    }


    /**
     * Method will perform various validations to ensure the integrity of the Permission Operation entity targeted for insertion
     * or updating in directory.  Data reasonability checks will be performed on all non-null attributes.
     *
     * @param pOp      Permission Operation entity contains data targeted for insertion or update.
     * @param isUpdate if true update operation is being performed which specifies a different set of targeted attributes.
     * @throws com.jts.fortress.SecurityException in the event of data validation error or DAO error.
     */
    void validate(Permission pOp, boolean isUpdate)
        throws SecurityException
    {
        if (!isUpdate)
        {
            //operation
            if (pOp.getOpName() != null && pOp.getOpName().length() > 0)
            {
                VUtil.description(pOp.getOpName());
            }
        }
        if (VUtil.isNotNullOrEmpty(pOp.getType()))
        {
            VUtil.description(pOp.getType());
        }
        // Validate Role Grants:
        if (VUtil.isNotNullOrEmpty(pOp.getRoles()))
        {
            List<String> roles = pOp.getRoles();
            RoleP rp = new RoleP();
            for (String role : roles)
            {
                rp.read(role);
            }
        }
        // Validate User Grants:
        if (VUtil.isNotNullOrEmpty(pOp.getUsers()))
        {
            List<String> users = pOp.getUsers();
            UserP up = new UserP();
            for (String userId : users)
            {
                up.read(userId, false);
            }
        }
    }
}