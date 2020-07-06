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
package org.apache.directory.fortress.core.rest;


import org.apache.directory.fortress.core.AdminMgr;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.Manageable;
import org.apache.directory.fortress.core.model.FortRequest;
import org.apache.directory.fortress.core.model.FortResponse;
import org.apache.directory.fortress.core.model.PermGrant;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.PermissionAttribute;
import org.apache.directory.fortress.core.model.PermissionAttributeSet;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.RoleConstraint;
import org.apache.directory.fortress.core.model.RoleRelationship;
import org.apache.directory.fortress.core.model.SDSet;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.util.VUtil;


/**
 * This class performs administrative functions to provision Fortress RBAC entities using HTTP access to Fortress Rest server.  These APIs
 * map directly to similar named APIs specified by ANSI and NIST RBAC models.
 * Many of the java doc function descriptions found below were taken directly from ANSI INCITS 359-2004.
 * The RBAC Functional specification describes administrative operations for the creation
 * and maintenance of RBAC element sets and relations; administrative review functions for
 * performing administrative queries; and system functions for creating and managing
 * RBAC attributes on user sessions and making access control decisions.
 * <p>
 * <hr>
 * <h4>RBAC0 - Core</h4>
 * Many-to-many relationship between Users, Roles and Permissions. Selective role activation into sessions.  API to add, update, delete identity data and perform identity and access control decisions during runtime operations.
 * <p>
 * <img src="../doc-files/RbacCore.png" alt="">
 * <hr>
 * <h4>RBAC1 - General Hierarchical Roles</h4>
 * Simplifies role engineering tasks using inheritance of one or more parent roles.
 * <p>
 * <img src="../doc-files/RbacHier.png" alt="">
 * <hr>
 * <h4>RBAC2 - Static Separation of Duty (SSD) Relations</h4>
 * Enforce mutual membership exclusions across role assignments.  Facilitate dual control policies by restricting which roles may be assigned to users in combination.  SSD provide added granularity for authorization limits which help enterprises meet strict compliance regulations.
 * <p>
 * <img src="../doc-files/RbacSSD.png" alt="">
 * <hr>
 * <h4>RBAC3 - Dynamic Separation of Duty (DSD) Relations</h4>
 * Control allowed role combinations to be activated within an RBAC session.  DSD policies fine tune role policies that facilitate authorization dual control and two man policy restrictions during runtime security checks.
 * <p>
 * <img src="../doc-files/RbacDSD.png" alt="">
 * <hr>
 * <p>
 * This class is NOT thread safe as it contains instance variables.
 * <p>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class AdminMgrRestImpl extends Manageable implements AdminMgr
{
    private static final String CLS_NM = AdminMgrRestImpl.class.getName();


    /**
     * {@inheritDoc}
     */
    @Override
    public User addUser( User user )
        throws SecurityException
    {
        VUtil.assertNotNull( user, GlobalErrIds.USER_NULL, CLS_NM + ".addUser" );
        User retUser;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( user );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.USER_ADD );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retUser = ( User ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retUser;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void disableUser( User user )
        throws SecurityException
    {
        VUtil.assertNotNull( user, GlobalErrIds.USER_NULL, CLS_NM + ".disableUser" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( user );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.USER_DISABLE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUser( User user )
        throws SecurityException
    {
        VUtil.assertNotNull( user, GlobalErrIds.USER_NULL, CLS_NM + ".deleteUser" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( user );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.USER_DELETE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public User updateUser( User user )
        throws SecurityException
    {
        VUtil.assertNotNull( user, GlobalErrIds.USER_NULL, CLS_NM + ".updateUser" );
        User retUser;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( user );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.USER_UPDATE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retUser = ( User ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retUser;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void changePassword( User user, String newPassword )
        throws SecurityException
    {
        VUtil.assertNotNull( user, GlobalErrIds.USER_NULL, CLS_NM + ".changePassword" );
        VUtil.assertNotNullOrEmpty( newPassword, GlobalErrIds.USER_PW_NULL, CLS_NM + ".changePassword" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        user.setNewPassword( newPassword );
        request.setEntity( user );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.USER_CHGPW );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void lockUserAccount( User user )
        throws SecurityException
    {
        VUtil.assertNotNull( user, GlobalErrIds.USER_NULL, CLS_NM + ".lockUserAccount" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( user );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.USER_LOCK );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void unlockUserAccount( User user )
        throws SecurityException
    {
        VUtil.assertNotNull( user, GlobalErrIds.USER_NULL, CLS_NM + ".unlockUserAccount" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( user );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.USER_UNLOCK );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void resetPassword( User user, String newPassword )
        throws SecurityException
    {
        VUtil.assertNotNull( user, GlobalErrIds.USER_NULL, CLS_NM + ".resetPassword" );
        VUtil.assertNotNullOrEmpty( newPassword, GlobalErrIds.USER_PW_NULL, CLS_NM + ".resetPassword" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        user.setNewPassword( newPassword );
        request.setEntity( user );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.USER_RESET );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePasswordPolicy( User user )
        throws SecurityException
    {
        throw new java.lang.UnsupportedOperationException();

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Role addRole( Role role )
        throws SecurityException
    {
        VUtil.assertNotNull( role, GlobalErrIds.ROLE_NULL, CLS_NM + ".addRole" );
        Role retRole;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( role );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_ADD );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retRole = ( Role ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retRole;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRole( Role role )
        throws SecurityException
    {
        VUtil.assertNotNull( role, GlobalErrIds.ROLE_NULL, CLS_NM + ".deleteRole" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( role );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_DELETE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Role updateRole( Role role )
        throws SecurityException
    {
        VUtil.assertNotNull( role, GlobalErrIds.ROLE_NULL, CLS_NM + ".updateRole" );
        Role retRole;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( role );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_UPDATE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retRole = ( Role ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retRole;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void assignUser( UserRole uRole )
        throws SecurityException
    {
        VUtil.assertNotNull( uRole, GlobalErrIds.URLE_NULL, CLS_NM + ".assignUser" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( uRole );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_ASGN );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deassignUser( UserRole uRole )
        throws SecurityException
    {
        VUtil.assertNotNull( uRole, GlobalErrIds.URLE_NULL, CLS_NM + ".deassignUser" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( uRole );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_DEASGN );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Permission addPermission( Permission perm )
        throws SecurityException
    {
        VUtil.assertNotNull( perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".addPermission" );
        Permission retPerm;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( perm );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PERM_ADD );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retPerm = ( Permission ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retPerm;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Permission updatePermission( Permission perm )
        throws SecurityException
    {
        VUtil.assertNotNull( perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".updatePermission" );
        Permission retPerm;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( perm );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PERM_UPDATE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retPerm = ( Permission ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retPerm;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePermission( Permission perm )
        throws SecurityException
    {
        VUtil.assertNotNull( perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".deletePermission" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( perm );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PERM_DELETE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PermObj addPermObj( PermObj pObj )
        throws SecurityException
    {
        VUtil.assertNotNull( pObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".addPermObj" );
        PermObj retObj;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( pObj );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.OBJ_ADD );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retObj = ( PermObj ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retObj;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PermObj updatePermObj( PermObj pObj )
        throws SecurityException
    {
        VUtil.assertNotNull( pObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".updatePermObj" );
        PermObj retObj;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( pObj );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.OBJ_UPDATE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retObj = ( PermObj ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retObj;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePermObj( PermObj pObj )
        throws SecurityException
    {
        VUtil.assertNotNull( pObj, GlobalErrIds.PERM_OBJECT_NULL, CLS_NM + ".deletePermObj" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( pObj );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.OBJ_DELETE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void grantPermission( Permission perm, Role role )
        throws SecurityException
    {
        VUtil.assertNotNull( perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".grantPermission" );
        VUtil.assertNotNull( role, GlobalErrIds.ROLE_NULL, CLS_NM + ".grantPermission" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin( perm.isAdmin() );
        permGrant.setObjName( perm.getObjName() );
        permGrant.setObjId( perm.getObjId() );
        permGrant.setOpName( perm.getOpName() );
        permGrant.setRoleNm( role.getName() );
        request.setEntity( permGrant );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_GRANT );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void revokePermission( Permission perm, Role role )
        throws SecurityException
    {
        VUtil.assertNotNull( perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".revokePermission" );
        VUtil.assertNotNull( role, GlobalErrIds.ROLE_NULL, CLS_NM + ".revokePermission" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin( perm.isAdmin() );
        permGrant.setObjName( perm.getObjName() );
        permGrant.setObjId( perm.getObjId() );
        permGrant.setOpName( perm.getOpName() );
        permGrant.setRoleNm( role.getName() );
        request.setEntity( permGrant );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_REVOKE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void grantPermission( Permission perm, User user )
        throws SecurityException
    {
        VUtil.assertNotNull( perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".grantPermissionUser" );
        VUtil.assertNotNull( user, GlobalErrIds.USER_NULL, CLS_NM + ".grantPermissionUser" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin( perm.isAdmin() );
        permGrant.setObjName( perm.getObjName() );
        permGrant.setObjId( perm.getObjId() );
        permGrant.setOpName( perm.getOpName() );
        permGrant.setUserId( user.getUserId() );
        request.setEntity( permGrant );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.USER_GRANT );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void revokePermission( Permission perm, User user )
        throws SecurityException
    {
        VUtil.assertNotNull( perm, GlobalErrIds.PERM_OPERATION_NULL, CLS_NM + ".revokePermission" );
        VUtil.assertNotNull( user, GlobalErrIds.USER_NULL, CLS_NM + ".revokePermission" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        PermGrant permGrant = new PermGrant();
        permGrant.setAdmin( perm.isAdmin() );
        permGrant.setObjName( perm.getObjName() );
        permGrant.setObjId( perm.getObjId() );
        permGrant.setOpName( perm.getOpName() );
        permGrant.setUserId( user.getUserId() );
        request.setEntity( permGrant );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.USER_REVOKE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addDescendant( Role parentRole, Role childRole )
        throws SecurityException
    {
        VUtil.assertNotNull( parentRole, GlobalErrIds.PARENT_ROLE_NULL, CLS_NM + ".addDescendant" );
        VUtil.assertNotNull( childRole, GlobalErrIds.CHILD_ROLE_NULL, CLS_NM + ".addDescendant" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        RoleRelationship relationship = new RoleRelationship();
        relationship.setParent( parentRole );
        relationship.setChild( childRole );
        request.setEntity( relationship );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_DESC );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addAscendant( Role childRole, Role parentRole )
        throws SecurityException
    {
        VUtil.assertNotNull( parentRole, GlobalErrIds.PARENT_ROLE_NULL, CLS_NM + ".addAscendant" );
        VUtil.assertNotNull( childRole, GlobalErrIds.CHILD_ROLE_NULL, CLS_NM + ".addAscendant" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        RoleRelationship relationship = new RoleRelationship();
        relationship.setParent( parentRole );
        relationship.setChild( childRole );
        request.setEntity( relationship );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_ASC );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addInheritance( Role parentRole, Role childRole )
        throws SecurityException
    {
        VUtil.assertNotNull( parentRole, GlobalErrIds.PARENT_ROLE_NULL, CLS_NM + ".addInheritance" );
        VUtil.assertNotNull( childRole, GlobalErrIds.CHILD_ROLE_NULL, CLS_NM + ".addInheritance" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        RoleRelationship relationship = new RoleRelationship();
        relationship.setParent( parentRole );
        relationship.setChild( childRole );
        request.setEntity( relationship );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_ADDINHERIT );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteInheritance( Role parentRole, Role childRole )
        throws SecurityException
    {
        VUtil.assertNotNull( parentRole, GlobalErrIds.PARENT_ROLE_NULL, CLS_NM + ".deleteInheritance" );
        VUtil.assertNotNull( childRole, GlobalErrIds.CHILD_ROLE_NULL, CLS_NM + ".deleteInheritance" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        RoleRelationship relationship = new RoleRelationship();
        relationship.setParent( parentRole );
        relationship.setChild( childRole );
        request.setEntity( relationship );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_DELINHERIT );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SDSet createSsdSet( SDSet ssdSet )
        throws SecurityException
    {
        VUtil.assertNotNull( ssdSet, GlobalErrIds.SSD_NULL, CLS_NM + ".createSsdSet" );
        SDSet retSet;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( ssdSet );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.SSD_ADD );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retSet = ( SDSet ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retSet;
    }


    /**
     * {@inheritDoc}
     */
    public SDSet updateSsdSet( SDSet ssdSet )
        throws SecurityException
    {
        VUtil.assertNotNull( ssdSet, GlobalErrIds.SSD_NULL, CLS_NM + ".updateSsdSet" );
        SDSet retSet;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( ssdSet );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.SSD_UPDATE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retSet = ( SDSet ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retSet;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SDSet addSsdRoleMember( SDSet ssdSet, Role role )
        throws SecurityException
    {
        VUtil.assertNotNull( ssdSet, GlobalErrIds.SSD_NULL, CLS_NM + ".addSsdRoleMember" );
        VUtil.assertNotNull( role, GlobalErrIds.ROLE_NULL, CLS_NM + ".addSsdRoleMember" );
        SDSet retSet;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( ssdSet );
        request.setValue( role.getName() );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.SSD_ADD_MEMBER );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retSet = ( SDSet ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retSet;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SDSet deleteSsdRoleMember( SDSet ssdSet, Role role )
        throws SecurityException
    {
        VUtil.assertNotNull( ssdSet, GlobalErrIds.SSD_NULL, CLS_NM + ".deleteSsdRoleMember" );
        VUtil.assertNotNull( role, GlobalErrIds.ROLE_NULL, CLS_NM + ".deleteSsdRoleMember" );
        SDSet retSet;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( ssdSet );
        request.setValue( role.getName() );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.SSD_DEL_MEMBER );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retSet = ( SDSet ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retSet;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SDSet deleteSsdSet( SDSet ssdSet )
        throws SecurityException
    {
        VUtil.assertNotNull( ssdSet, GlobalErrIds.SSD_NULL, CLS_NM + ".deleteSsdSet" );
        SDSet retSet;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( ssdSet );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.SSD_DELETE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retSet = ( SDSet ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retSet;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SDSet setSsdSetCardinality( SDSet ssdSet, int cardinality )
        throws SecurityException
    {
        VUtil.assertNotNull( ssdSet, GlobalErrIds.SSD_NULL, CLS_NM + ".setSsdSetCardinality" );
        SDSet retSet;
        FortRequest request = RestUtils.getRequest( this.contextId );
        ssdSet.setCardinality( cardinality );
        request.setEntity( ssdSet );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.SSD_CARD_UPDATE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retSet = ( SDSet ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retSet;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SDSet createDsdSet( SDSet dsdSet )
        throws SecurityException
    {
        VUtil.assertNotNull( dsdSet, GlobalErrIds.SSD_NULL, CLS_NM + ".createDsdSet" );
        SDSet retSet;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( dsdSet );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.DSD_ADD );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retSet = ( SDSet ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retSet;
    }


    /**
     * {@inheritDoc}
     */
    public SDSet updateDsdSet( SDSet dsdSet )
        throws SecurityException
    {
        VUtil.assertNotNull( dsdSet, GlobalErrIds.SSD_NULL, CLS_NM + ".updateDsdSet" );
        SDSet retSet;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( dsdSet );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.DSD_UPDATE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retSet = ( SDSet ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retSet;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SDSet addDsdRoleMember( SDSet dsdSet, Role role )
        throws SecurityException
    {
        VUtil.assertNotNull( dsdSet, GlobalErrIds.SSD_NULL, CLS_NM + ".addDsdRoleMember" );
        VUtil.assertNotNull( role, GlobalErrIds.ROLE_NULL, CLS_NM + ".addDsdRoleMember" );
        SDSet retSet;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( dsdSet );
        request.setValue( role.getName() );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.DSD_ADD_MEMBER );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retSet = ( SDSet ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retSet;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SDSet deleteDsdRoleMember( SDSet dsdSet, Role role )
        throws SecurityException
    {
        VUtil.assertNotNull( dsdSet, GlobalErrIds.SSD_NULL, CLS_NM + ".deleteDsdRoleMember" );
        VUtil.assertNotNull( role, GlobalErrIds.ROLE_NULL, CLS_NM + ".deleteSsdRoleMember" );
        SDSet retSet;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( dsdSet );
        request.setValue( role.getName() );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.DSD_DEL_MEMBER );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retSet = ( SDSet ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retSet;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SDSet deleteDsdSet( SDSet dsdSet )
        throws SecurityException
    {
        VUtil.assertNotNull( dsdSet, GlobalErrIds.SSD_NULL, CLS_NM + ".deleteDsdSet" );
        SDSet retSet;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( dsdSet );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.DSD_DELETE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retSet = ( SDSet ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retSet;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SDSet setDsdSetCardinality( SDSet dsdSet, int cardinality )
        throws SecurityException
    {
        VUtil.assertNotNull( dsdSet, GlobalErrIds.SSD_NULL, CLS_NM + ".setSsdSetCardinality" );
        SDSet retSet;
        FortRequest request = RestUtils.getRequest( this.contextId );
        dsdSet.setCardinality( cardinality );
        request.setEntity( dsdSet );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.DSD_CARD_UPDATE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retSet = ( SDSet ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retSet;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public RoleConstraint addRoleConstraint(UserRole uRole, RoleConstraint roleConstraint)
        throws SecurityException
    {
        VUtil.assertNotNull( uRole, GlobalErrIds.URLE_NULL, CLS_NM + ".addRoleConstraint" );
        VUtil.assertNotNull( roleConstraint, GlobalErrIds.RCON_NULL, CLS_NM + ".addRoleConstraint" );
        RoleConstraint retCnst;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( uRole );
        request.setEntity2( roleConstraint );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_ADD_CONSTRAINT );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retCnst = ( RoleConstraint ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retCnst;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removeRoleConstraint( UserRole uRole, RoleConstraint roleConstraint ) throws SecurityException
    {
        VUtil.assertNotNull( uRole, GlobalErrIds.URLE_NULL, CLS_NM + ".removeRoleConstraint" );
        VUtil.assertNotNull( roleConstraint, GlobalErrIds.RCON_NULL, CLS_NM + ".removeRoleConstraint" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( uRole );
        request.setEntity2( roleConstraint );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_DELETE_CONSTRAINT );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PermissionAttributeSet addPermissionAttributeSet( PermissionAttributeSet permAttributeSet )
        throws SecurityException
    {
        VUtil.assertNotNull( permAttributeSet, GlobalErrIds.PERM_ATTRIBUTE_SET_NULL, CLS_NM + ".addPermissionAttributeSet" );
        PermissionAttributeSet retSet;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( permAttributeSet );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PERM_ADD_ATTRIBUTE_SET );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retSet = ( PermissionAttributeSet ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retSet;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePermissionAttributeSet(
            PermissionAttributeSet permAttributeSet) throws SecurityException
    {
        VUtil.assertNotNull( permAttributeSet, GlobalErrIds.PERM_ATTRIBUTE_SET_NULL, CLS_NM + ".deletePermissionAttributeSet" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( permAttributeSet );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PERM_DELETE_ATTRIBUTE_SET );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PermissionAttribute addPermissionAttributeToSet( PermissionAttribute permAttribute, String attributeSetName )
            throws SecurityException
    {
        VUtil.assertNotNull( permAttribute, GlobalErrIds.PERM_ATTRIBUTE_SET_NULL, CLS_NM + ".addPermissionAttributeToSet" );
        VUtil.assertNotNull( attributeSetName, GlobalErrIds.PERM_ATTRIBUTE_SET_NM_NULL, CLS_NM + ".addPermissionAttributeToSet" );
        PermissionAttribute retAttr;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( permAttribute );
        request.setValue( attributeSetName );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PERM_ADD_PERM_ATTRIBUTE_TO_SET );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retAttr = ( PermissionAttribute ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retAttr;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removePermissionAttributeFromSet( PermissionAttribute permAttribute, String attributeSetName )
            throws SecurityException
    {
        VUtil.assertNotNull( permAttribute, GlobalErrIds.PERM_ATTRIBUTE_SET_NULL, CLS_NM + ".removePermissionAttributeFromSet" );
        VUtil.assertNotNull( attributeSetName, GlobalErrIds.PERM_ATTRIBUTE_SET_NM_NULL, CLS_NM + ".removePermissionAttributeFromSet" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( permAttribute );
        request.setValue( attributeSetName );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PERM_DELETE_PERM_ATTRIBUTE_TO_SET );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePermissionAttributeInSet(PermissionAttribute permAttribute, String attributeSetName, boolean replaceValidValues)
        throws SecurityException
    {
        VUtil.assertNotNull( permAttribute, GlobalErrIds.PERM_ATTRIBUTE_SET_NULL, CLS_NM + ".updatePermissionAttributeInSet" );
        VUtil.assertNotNull( attributeSetName, GlobalErrIds.PERM_ATTRIBUTE_SET_NM_NULL, CLS_NM + ".updatePermissionAttributeInSet" );
        PermissionAttribute retAttr;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( permAttribute );
        request.setValue( attributeSetName );
        request.setIsFlag( replaceValidValues );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PERM_UPDATE_PERM_ATTRIBUTE_IN_SET );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removeRoleConstraint( UserRole uRole, String roleConstraintId ) throws SecurityException
    {
        //throw new UnsupportedOperationException( "not implemented" );
        VUtil.assertNotNull( uRole, GlobalErrIds.URLE_NULL, CLS_NM + ".removeRoleConstraint" );
        VUtil.assertNotNull( roleConstraintId, GlobalErrIds.ROLE_CONSTRAINT_VALUE_NULL, CLS_NM + ".removeRoleConstraint" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( uRole );
        request.setValue( roleConstraintId );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_DELETE_CONSTRAINT_ID );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableRoleConstraint( Role role, RoleConstraint roleConstraint )
        throws SecurityException
    {
        String methodName = ".enableRoleConstraint";
        VUtil.assertNotNull( role, GlobalErrIds.ROLE_NULL, CLS_NM + methodName );
        VUtil.assertNotNull( roleConstraint, GlobalErrIds.RCON_NULL, CLS_NM + methodName );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( role );
        request.setEntity2( roleConstraint );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_ENABLE_CONSTRAINT );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableRoleConstraint( Role role, RoleConstraint roleConstraint )
        throws SecurityException
    {
        String methodName = ".disableRoleConstraint";
        VUtil.assertNotNull( role, GlobalErrIds.ROLE_NULL, CLS_NM + methodName );
        VUtil.assertNotNull( roleConstraint, GlobalErrIds.RCON_NULL, CLS_NM + methodName );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( role );
        request.setEntity2( roleConstraint );
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.ROLE_DISABLE_CONSTRAINT );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }
}