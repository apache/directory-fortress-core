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


import java.util.List;

import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.PwPolicyMgr;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.Manageable;
import org.apache.directory.fortress.core.model.FortRequest;
import org.apache.directory.fortress.core.model.FortResponse;
import org.apache.directory.fortress.core.model.PwPolicy;
import org.apache.directory.fortress.core.util.VUtil;


/**
 * This class is used to perform administrative and review functions on the PWPOLICIES and USERS data sets using HTTP access to Fortress Rest server.
 * <p>
 * <h4>Password Policies</h4>
 * <a href="http://www.openldap.org/">OpenLDAP</a> supports the IETF draft <a href="http://tools.ietf.org/html/draft-behera-ldap-password-policy-10/">Password Policies for LDAP directories</a></li>.  Policies may be applied at the user, group or global level.
 * <p>
 * <img src="../doc-files/PasswordPolicy.png" alt="">
 * <p>
 * Password enforcement options include:
 * <ol>
 * <li>A configurable limit on failed authentication attempts.</li>
 * <li>A counter to track the number of failed authentication attempts.</li>
 * <li>A time frame in which the limit of consecutive failed authentication attempts must happen before action is taken.</li>
 * <li>The action to be taken when the limit is reached. The action will either be nothing, or the account will be locked.</li>
 * <li>An amount of time the account is locked (if it is to be locked) This can be indefinite.</li>
 * <li>Password expiration.</li>
 * <li>Expiration warning</li>
 * <li>Grace authentications</li>
 * <li>Password history</li>
 * <li>Password minimum age</li>
 * <li>Password minimum length</li>
 * <li>Password Change after Reset</li>
 * <li>Safe Modification of Password</li>
 * </ol>
 * <p>
 * This class is NOT thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class PwPolicyMgrRestImpl extends Manageable implements PwPolicyMgr
{
    private static final String CLS_NM = PwPolicyMgrRestImpl.class.getName();


    /**
     * {@inheritDoc}
     */
    @Override
    public void add( PwPolicy policy )
        throws SecurityException
    {
        VUtil.assertNotNull( policy, GlobalErrIds.PSWD_PLCY_NULL, CLS_NM + ".add" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( policy );
        if ( this.adminSess != null )
        {
            ////request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PSWD_ADD );
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
    public void update( PwPolicy policy )
        throws SecurityException
    {
        VUtil.assertNotNull( policy, GlobalErrIds.PSWD_PLCY_NULL, CLS_NM + ".update" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( policy );
        if ( this.adminSess != null )
        {
            ////request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PSWD_UPDATE );
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
    public void delete( PwPolicy policy )
        throws SecurityException
    {
        VUtil.assertNotNull( policy, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + ".delete" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( policy );
        if ( this.adminSess != null )
        {
            ////request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PSWD_DELETE );
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
    public PwPolicy read( String name )
        throws SecurityException
    {
        VUtil.assertNotNullOrEmpty( name, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + ".read" );
        PwPolicy retPolicy;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( new PwPolicy( name ) );
        if ( this.adminSess != null )
        {
            ////request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PSWD_READ );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retPolicy = ( PwPolicy ) response.getEntity();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retPolicy;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<PwPolicy> search( String searchVal )
        throws SecurityException
    {
        VUtil.assertNotNull( searchVal, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + ".search" );
        List<PwPolicy> retPolicies;
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( new PwPolicy( searchVal ) );
        if ( this.adminSess != null )
        {
            ////request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PSWD_SEARCH );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() == 0 )
        {
            retPolicies = response.getEntities();
        }
        else
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
        return retPolicies;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUserPolicy( String userId, String name )
        throws SecurityException
    {
        String methodName = "updateUserPolicy";
        VUtil.assertNotNullOrEmpty( userId, GlobalErrIds.USER_NULL, CLS_NM + "." + methodName );
        VUtil.assertNotNullOrEmpty( name, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + "." + methodName );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setEntity( new PwPolicy( name ) );
        request.setValue( userId );
        if ( this.adminSess != null )
        {
            ////request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PSWD_USER_ADD );
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
    public void deletePasswordPolicy( String userId )
        throws SecurityException
    {
        VUtil.assertNotNullOrEmpty( userId, GlobalErrIds.USER_NULL, CLS_NM + ".deletePasswordPolicy" );
        FortRequest request = RestUtils.getRequest( this.contextId );
        request.setValue( userId );
        if ( this.adminSess != null )
        {
            ////request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.getInstance().post( szRequest, HttpIds.PSWD_USER_DELETE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }
}
