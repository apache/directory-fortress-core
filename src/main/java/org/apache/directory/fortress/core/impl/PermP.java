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
package org.apache.directory.fortress.core.impl;


import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.ValidationException;
import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.PermissionAttribute;
import org.apache.directory.fortress.core.model.PermissionAttributeSet;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.util.VUtil;


/**
 * Process module for the Permission entity.  This class performs data validations and error mapping.  It is typically called
 * by internal Fortress manager classes ({@link AdminMgrImpl}, {@link AccessMgrImpl},
 * {@link ReviewMgrImpl}, ...) and not intended for external non-Fortress clients.  This class will accept,
 * {@link PermObj} or {@link Permission}, validate its contents and forward on to it's corresponding DAO class {@link org.apache.directory.fortress.core.impl.PermDAO}.
 * <p>
 * Class will throw {@link SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exceptions ({@link org.apache.directory.fortress.core.FinderException},
 * {@link org.apache.directory.fortress.core.CreateException},{@link org.apache.directory.fortress.core.UpdateException},{@link org.apache.directory.fortress.core.RemoveException}),
 *  or {@link org.apache.directory.fortress.core.ValidationException} as {@link SecurityException}s with appropriate
 * error id from {@link org.apache.directory.fortress.core.GlobalErrIds}.
 * <p>
 * This class is thread safe.
 * <p>

 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class PermP
{
    /**
     * Description of the Field
     */
    private PermDAO pDao = new PermDAO();
    private OrgUnitP orgUnitP = new OrgUnitP();


    /**
     * This function returns a Boolean value meaning whether the subject of a given session is
     * allowed or not to perform a given operation on a given object. The function is valid if and
     * only if the session is a valid Fortress session, the object is a member of the OBJS data set,
     * and the operation is a member of the OPS data set. The session's subject has the permission
     * to perform the operation on that object if and only if that permission is assigned to (at least)
     * one of the session's active roles. This implementation will verify the roles or userId correspond
     * to the subject's active roles are registered in the object's access control list.
     *
     * @param session    This object must be instantiated by calling {@link AccessMgrImpl#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @param permission object contains obj attribute which is a String and contains the name of the object user is trying to access;
     *                   perm object contains operation attribute which is also a String and contains the operation name for the object.
     * @return True of user has access, false otherwise.
     * @throws SecurityException in the event of data validation failure, security policy violation or DAO error.
     */
    boolean checkPermission( Session session, Permission permission ) throws SecurityException
    {
        return pDao.checkPermission( session, permission );
    }


    /**
     * Takes a Permission entity that contains full or partial object name and/or full or partial operation name for search.
     *
     * @param permission contains all or partial object name and/or all or partial operation name.
     * @return List of type Permission containing fully populated matching Permission entities.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Permission> search( Permission permission ) throws SecurityException
    {
        return pDao.findPermissions( permission );
    }
    
    /**
     * Takes a permission object that contains an object name and returns permisison operations for that object
     * 
     * @param permObj Contains full object name
     * @return List of type Permission containing fully populated matching Permission entities.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Permission> searchOperations( PermObj permObj ) throws SecurityException
    {
        return pDao.findPermissionOperations( permObj );
    }

    /**
     * Takes a Permission entity that contains full or partial object name and/or full or partial operation name for search.
     *
     * @param permission contains all or partial object name and/or all or partial operation name.
     * @return List of type Permission containing fully populated matching Permission entities.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Permission> searchAny( Permission permission ) throws SecurityException
    {
        return pDao.findAnyPermissions( permission );
    }


    /**
     * Takes a Permission object entity that contains full or partial object name for search Permission Objects in directory..
     *
     * @param permObj contains all or partial object name.
     * @return List of type Permission Objects containing fully populated matching entities.
     * @throws SecurityException in the event of DAO search error.
     */
    List<PermObj> search( PermObj permObj ) throws SecurityException
    {
        return pDao.findPermissions( permObj );
    }


    /**
     * Takes an OrgUnit entity that contains full or partial orgUnitId for search Permission Objects in directory..
     *
     * @param ou contains all or OrgUnitId.
     * @param limitSize contains max number of entries to return.
     * @return List of type Permission Objects containing fully populated matching entities.
     * @throws SecurityException in the event of DAO search error.
     */
    List<PermObj> search( OrgUnit ou, boolean limitSize ) throws SecurityException
    {
        return pDao.findPermissions( ou, limitSize );
    }


    /**
     * Search will return a list of matching permissions that are assigned to a given RBAC or Admin role name.  The
     * DAO class will search the Admin perms if the "isAdmin" boolean flag is "true", otherwise it will search RBAC perm tree.
     *
     * @param role contains the RBAC or Admin Role name targeted for search.
     * @param noInheritance if true will NOT include inherited roles in the search.
     * @return List of type Permission containing fully populated matching Permission entities.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Permission> search( Role role, boolean noInheritance ) throws SecurityException
    {
        return pDao.findPermissions( role, noInheritance );
    }


    /**
     * Search will return a list of matching permissions that are assigned to a given RBAC or Admin role name.  The
     * DAO class will search the Admin perms if the "isAdmin" boolean flag is "true", otherwise it will search RBAC perm tree.
     *
     * @param role contains the RBAC or Admin Role name targeted for search.
     * @return List of type Permission containing fully populated matching Permission entities.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Permission> search( Role role ) throws SecurityException
    {
        return search( role, false );
    }


    /**
     * Search will return a list of matching permissions that are assigned to a given User.  This method searches
     * the RBAC perms only.
     *
     * @param user contains the userId targeted for search.
     * @return List of type Permission containing fully populated matching Permission entities.
     * @throws SecurityException in the event of DAO search error.
     */
    List<Permission> search( User user ) throws SecurityException
    {
        return pDao.findPermissions( user );
    }


    /**
     * Remove the User assignment attribute from all RBAC permssions.  This method is called by AdminMgrImpl
     * when the User is being deleted.
     *
     * @param user contains the userId targeted for attribute removal.
     * @throws SecurityException in the event of DAO search error.
     */
    void remove( User user ) throws SecurityException
    {
        List<Permission> list;
        try
        {
            list = pDao.findUserPermissions( user );
            for ( Permission perm : list )
            {
                revoke( perm, user );
            }
        }
        catch ( FinderException fe )
        {
            String error = "remove userId [" + user.getUserId() + "] caught FinderException=" + fe;
            throw new SecurityException( GlobalErrIds.PERM_BULK_USER_REVOKE_FAILED, error, fe );
        }
    }


    /**
     * Remove the RBAC Role assignment attribute from all RBAC permssions.  This method is called by AdminMgrImpl
     * when the RBAC Role is being deleted.
     *
     * @param role contains the name of Role targeted for attribute removal.
     * @throws SecurityException in the event of DAO search error.
     */
    void remove( Role role ) throws SecurityException
    {
        List<Permission> list;
        try
        {
            list = search( role );
            for ( Permission perm : list )
            {
                revoke( perm, role );
            }
        }
        catch ( FinderException fe )
        {
            String error = "remove role [" + role.getName() + "] caught FinderException=" + fe;
            throw new SecurityException( GlobalErrIds.PERM_BULK_ROLE_REVOKE_FAILED, error, fe );
        }
    }


    /**
     * Remove the Admin Role assignment attribute from all Admin permssions.  This method is called by DelAdminMgrImpl
     * when the AdminRole is being deleted.
     *
     * @param role contains the name of AdminRole targeted for attribute removal.
     * @throws SecurityException in the event of DAO search error.
     */
    void remove( AdminRole role ) throws SecurityException
    {
        List<Permission> list;
        try
        {
            list = search( role );
            for ( Permission perm : list )
            {
                perm.setAdmin( true );
                revoke( perm, role );
            }
        }
        catch ( FinderException fe )
        {
            String error = "remove admin role [" + role.getName() + "] caught FinderException=" + fe;
            throw new SecurityException( GlobalErrIds.PERM_BULK_ADMINROLE_REVOKE_FAILED, error, fe );
        }
    }


    /**
     * This function returns the permissions of the session, i.e., the permissions assigned
     * to its authorized roles. The function is valid if and only if the session is a valid Fortress session.
     *
     * @param session This object must be instantiated by calling {@link AccessMgrImpl#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @return List<Permission> containing permissions (op, obj) active for user's session.
     * @throws SecurityException is thrown if runtime error occurs with system.
     */
    List<Permission> search( Session session ) throws SecurityException
    {
        return search( session, false );
    }


    /**
     * This function returns the permissions of the session, i.e., the permissions assigned
     * to its authorized roles. The function is valid if and only if the session is a valid Fortress session.
     *
     * @param session This object must be instantiated by calling {@link AccessMgrImpl#createSession} method before passing into the method.  No variables need to be set by client after returned from createSession.
     * @return List<Permission> containing permissions (op, obj) active for user's session.
     * @throws org.apache.directory.fortress.core.SecurityException is thrown if runtime error occurs with system.
     */
    List<Permission> search( Session session, boolean isAdmin )
        throws SecurityException
    {
        return pDao.findPermissions( session, isAdmin );
    }


    /**
     * Return the matching Permission entity.  This method will throw SecurityException if not found.
     *
     * @param permission contains the full permission object and operation name.
     * @return Permission containing fully populated matching object.
     * @throws SecurityException is thrown if permission not found or runtime error occurs with system.
     */
    Permission read( Permission permission ) throws SecurityException
    {
        return pDao.getPerm( permission );
    }


    /**
     * Return the matching Permission object entity.  This method will throw SecurityException if not found.
     *
     * @param permObj contains the full permission object name.
     * @return PermObj containing fully populated matching object.
     * @throws SecurityException is thrown if perm object not found or runtime error occurs with system.
     */
    PermObj read( PermObj permObj ) throws SecurityException
    {
        return pDao.getPerm( permObj );
    }
    
    //TODO: add documentation
    PermissionAttributeSet read( PermissionAttributeSet permAttributeSet ) throws SecurityException
    {
        return pDao.getPermAttributeSet( permAttributeSet );
    }

    /**
     * Adds a new Permission Object entity to directory.  The Permission Object entity input will be validated to ensure that:
     * object name is present, orgUnitId is valid, reasonability checks on all of the
     * other populated values.
     *
     * @param entity Permission object entity contains data targeted for insertion.
     * @return Permission entity copy of input + additional attributes (internalId) that were added by op.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    PermObj add( PermObj entity ) throws SecurityException
    {
        validate( entity, false );
        return pDao.createObject( entity );
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
    Permission add( Permission entity ) throws SecurityException
    {
        validate( entity, false );
        return pDao.createOperation( entity );
    }
    
    /**
     * Adds a new Permission Attribute Set entity to the directory. 
     * 
     * @param entity Permission Attribute Set entity contains data targeted for insertion
     * @return Permission Attribute Set entity copy of input + additional attributes that were added
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    PermissionAttributeSet add( PermissionAttributeSet entity ) throws SecurityException
    {
        validate( entity );
        return pDao.createPermissionAttributeSet( entity );
    }
    
    /**
     * Adds a new Permission Attribute entity to an existing Permission Attribute Set
     * 
     * @param entity Permissino Attribute entity contains data targeted for insertion
     * @param paSetName The name of the Permission Attribute Set this entity is being added to
     * @return Permission Attribute entity copy of input + additional attributes that were added
     * @throws SecurityException in the event of data validation or DAO system error. 
     */
    PermissionAttribute add( PermissionAttribute entity, String paSetName ) throws SecurityException
    {
        validate( entity );
        return pDao.createPermissionAttribute( entity, paSetName );
    }

    /**
     * Deletes a Permission Attribute entity from an existing Permission Attribute Set
     * 
     * @param entity
     * @param attributeSetName
     * @throws SecurityException
     */     
    void delete( PermissionAttribute entity, String paSetName ) throws SecurityException
    {
        validate( entity );                
        pDao.deletePermissionAttribute( entity, paSetName );
    }

    /**
     * Updates a Permission Attribute entity 
     * 
     * @param entity
     * @param paSetName
     * @param replaceValidValues
     * @throws SecurityException
     */
    void update( PermissionAttribute entity, String paSetName, boolean replaceValidValues ) throws SecurityException
    {
        validate( entity );
        pDao.updatePermissionAttribute( entity, paSetName, replaceValidValues );
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
    PermObj update( PermObj entity ) throws SecurityException
    {
        update( entity, true );
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
    private PermObj update( PermObj entity, boolean validate )
        throws SecurityException
    {
        if ( validate )
        {
            validate( entity, true );
        }
        return pDao.updateObj( entity );
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
    Permission update( Permission entity ) throws SecurityException
    {
        update( entity, true );
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
    private Permission update( Permission entity, boolean validate )
        throws SecurityException
    {
        if ( validate )
        {
            validate( entity, true );
        }
        return pDao.updateOperation( entity );
    }


    /**
     * This method performs a "hard" delete.  It completely removes all data associated with this Permission Object from the directory
     * including the Permission operations..
     * Permission Object entity must exist in directory prior to making this call else exception will be thrown.
     *
     * @param entity Contains the Permission Object name targeted for deletion.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    void delete( PermObj entity ) throws SecurityException
    {
        pDao.deleteObj( entity );
    }


    /**
     * This method performs a "hard" delete.  It completely removes all data associated with this Permission Operation from the directory
     * Permission Operation entity must exist in directory prior to making this call else exception will be thrown.
     *
     * @param entity Contains the Permission Operation name targeted for deletion.
     * @throws SecurityException in the event of data validation or DAO system error.
     */
    void delete( Permission entity ) throws SecurityException
    {
        pDao.deleteOperation( entity );
    }

    //TODO: add documentation
    void delete( PermissionAttributeSet entity ) throws SecurityException
    {
        pDao.deleteAttributeSet( entity );
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
    void grant( Permission pOp, Role role ) throws SecurityException
    {
        // Now assign it to the perm op:
        pDao.grant( pOp, role );
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
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    void revoke( Permission pOp, Role role ) throws SecurityException
    {
        pDao.revoke( pOp, role );
    }


    /**
     * Method grants a permission directly to a User entity.
     *
     * @param pOp  contains object and operation name for resource.
     * @param user contains userid of User entity.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    void grant( Permission pOp, User user ) throws SecurityException
    {
        // call dao to grant userId access to the perm op:
        pDao.grant( pOp, user );
    }


    /**
     * Method revokes a permission directly from a User entity.
     *
     * @param pOp  contains object and operation name for resource.
     * @param user contains userid of User entity.
     * @throws SecurityException Thrown in the event of data validation or system error.
     */
    void revoke( Permission pOp, User user ) throws SecurityException
    {
        pDao.revoke( pOp, user );
    }


    /**
     * Method will perform various validations to ensure the integrity of the Permission Object entity targeted for insertion
     * or updating in directory.  Data reasonability checks will be performed on all non-null attributes.
     *
     * @param pObj     Permission Object entity contains data targeted for insertion or update.
     * @param isUpdate if true update operation is being performed which specifies a different set of targeted attributes.
     * @throws org.apache.directory.fortress.core.ValidationException in the event of data validation error.
     */
    void validate( PermObj pObj, boolean isUpdate ) throws ValidationException
    {
        if ( !isUpdate )
        {
            // Validate length
            VUtil.orgUnit( pObj.getOu() );
            // ensure ou exists in the OS-P pool:
            OrgUnit ou = new OrgUnit( pObj.getOu(), OrgUnit.Type.PERM );
            ou.setContextId( pObj.getContextId() );
            if ( !orgUnitP.isValid( ou ) )
            {
                String error = "validate detected invalid orgUnit name [" + pObj.getOu() + "] for object name ["
                    + pObj.getObjName() + "]";
                //log.warn(error);
                throw new ValidationException( GlobalErrIds.PERM_OU_INVALID, error );
            }
            if ( StringUtils.isNotEmpty( pObj.getObjName() ) )
            {
                VUtil.description( pObj.getObjName() );
            }
            if ( StringUtils.isNotEmpty( pObj.getOu() ) )
            {
                VUtil.orgUnit( pObj.getOu() );
            }
            if ( StringUtils.isNotEmpty( pObj.getDescription() ) )
            {
                VUtil.description( pObj.getDescription() );
            }
        }
        else
        {
            if ( StringUtils.isNotEmpty( pObj.getOu() ) )
            {
                VUtil.orgUnit( pObj.getOu() );
                // ensure ou exists in the OS-P pool:
                OrgUnit ou = new OrgUnit( pObj.getOu(), OrgUnit.Type.PERM );
                ou.setContextId( pObj.getContextId() );
                if ( !orgUnitP.isValid( ou ) )
                {
                    String error = "validate detected invalid orgUnit name [" + pObj.getOu() + "] for object name ["
                        + pObj.getObjName() + "]";
                    throw new ValidationException( GlobalErrIds.PERM_OU_INVALID, error );
                }
            }
            if ( StringUtils.isNotEmpty( pObj.getDescription() ) )
            {
                VUtil.description( pObj.getDescription() );
            }
        }
    }


    /**
     * Method will perform various validations to ensure the integrity of the Permission Operation entity targeted for insertion
     * or updating in directory.  Data reasonability checks will be performed on all non-null attributes.
     *
     * @param pOp      Permission Operation entity contains data targeted for insertion or update.
     * @param isUpdate if true update operation is being performed which specifies a different set of targeted attributes.
     * @throws SecurityException in the event of data validation error or DAO error.
     */
    private void validate( Permission pOp, boolean isUpdate )
        throws SecurityException
    {
        if ( !isUpdate )
        {
            //operation
            if ( pOp.getOpName() != null && pOp.getOpName().length() > 0 )
            {
                VUtil.description( pOp.getOpName() );
            }
        }
        if ( StringUtils.isNotEmpty( pOp.getType() ) )
        {
            VUtil.description( pOp.getType() );
        }
        if ( StringUtils.isNotEmpty( pOp.getDescription() ) )
        {
            VUtil.description( pOp.getDescription() );
        }
        // Validate Role Grants:
        if ( CollectionUtils.isNotEmpty( pOp.getRoles() ) )
        {
            Set<String> roles = pOp.getRoles();
            if ( pOp.isAdmin() )
            {
                AdminRoleP arp = new AdminRoleP();
                for ( String roleNm : roles )
                {
                    AdminRole adminRole = new AdminRole( roleNm );
                    adminRole.setContextId( pOp.getContextId() );
                    arp.read( adminRole );
                }
            }
            else
            {
                RoleP rp = new RoleP();
                for ( String roleNm : roles )
                {
                    Role role = new Role( roleNm );
                    role.setContextId( pOp.getContextId() );
                    rp.read( role );
                }
            }
        }
        // Validate User Grants:
        if ( CollectionUtils.isNotEmpty( pOp.getUsers() ) )
        {
            Set<String> users = pOp.getUsers();
            UserP up = new UserP();
            for ( String userId : users )
            {
                User user = new User( userId );
                user.setContextId( pOp.getContextId() );
                up.read( user, false );
            }
        }
        // Validate Perm Attr Set Name
        if( CollectionUtils.isNotEmpty( pOp.getPaSets() ))
        {
            for(String paSetName : pOp.getPaSets())
            {
                validatePaSet( paSetName, pOp.getContextId() );
            }
        }
    }

    /*
     * Ensure the paSet is present and name is safe.
     */
    void validatePaSet( String paSetName, String contextId ) throws ValidationException
    {
        try
        {
            PermissionAttributeSet paSet = new PermissionAttributeSet( paSetName );
            paSet.setContextId( contextId );
            read(paSet);
            VUtil.safeText( paSetName, GlobalIds.DESC_LEN );
        }
        catch( SecurityException e )
        {
            String error = "validatePaSet - paSetName not found with name [" + paSetName + "] caught SecurityException=" + e;
            throw new ValidationException( GlobalErrIds.PERM_ATTRIBUTE_SET_NOT_FOUND, error );
        }
    }
    
    private void validate( PermissionAttributeSet paSet )
            throws SecurityException
    {
        if( StringUtils.isNotEmpty(paSet.getType()) ){
            VUtil.description( paSet.getType() );
        }
        if( StringUtils.isNotEmpty(paSet.getDescription()) ){
            VUtil.description( paSet.getDescription() );
        }
    }
    
    private void validate( PermissionAttribute pa )
            throws SecurityException
    {
        if( StringUtils.isNotEmpty(pa.getAttributeName()) ){
            VUtil.description( pa.getAttributeName() );
        }
        if( StringUtils.isNotEmpty(pa.getDataType()) ){
            VUtil.description( pa.getDataType() );
        }
        if( StringUtils.isNotEmpty(pa.getDefaultOperator()) ){
            VUtil.description( pa.getDefaultOperator() );
        }
        if( StringUtils.isNotEmpty(pa.getDefaultStrategy()) ){
            VUtil.description( pa.getDefaultStrategy() );
        }
        if( StringUtils.isNotEmpty(pa.getDefaultValue()) ){
            VUtil.description( pa.getDefaultValue() );
        }
        if( StringUtils.isNotEmpty(pa.getDescription()) ){
            VUtil.description( pa.getDescription() );
        }        
    }
}