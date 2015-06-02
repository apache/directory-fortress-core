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


import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.PwPolicyMgr;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.FortRequest;
import org.apache.directory.fortress.core.model.FortResponse;
import org.apache.directory.fortress.core.impl.Manageable;
import org.apache.directory.fortress.core.model.PwPolicy;
import org.apache.directory.fortress.core.util.VUtil;

import java.util.List;


/**
 * This class is used to perform administrative and review functions on the PWPOLICIES and USERS data sets using HTTP access to En Masse REST server.
 * <p/>
 * <h4>Password Policies</h4>
 * <a href="http://www.openldap.org/">OpenLDAP</a> supports the IETF draft <a href="http://tools.ietf.org/html/draft-behera-ldap-password-policy-10/">Password Policies for LDAP directories</a></li>.  Policies may be applied at the user, group or global level.
 * <p/>
 * <img src="../doc-files/PasswordPolicy.png">
 * <p/>
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
 * <p/>
 * This class is NOT thread safe.
 * <p/>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class PwPolicyMgrRestImpl extends Manageable implements PwPolicyMgr
{
    private static final String CLS_NM = PwPolicyMgrRestImpl.class.getName();


    /**
     * This method will add a new policy entry to the POLICIES data set.  This command is valid
     * if and only if the policy entry is not already present in the POLICIES data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#name} - Maps to name attribute of pwdPolicy object class being added.</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#minAge} - This attribute holds the number of seconds that must elapse between
     * modifications to the password.  If this attribute is not present, 0
     * seconds is assumed.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#maxAge} - This attribute holds the number of seconds after which a modified
     * password will expire. If this attribute is not present, or if the value is 0 the password
     * does not expire.  If not 0, the value must be greater than or equal
     * to the value of the pwdMinAge.
     * </li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#inHistory} - This attribute specifies the maximum number of used passwords stored
     * in the pwdHistory attribute. If this attribute is not present, or if the value is 0, used
     * passwords are not stored in the pwdHistory attribute and thus may be reused.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#minLength} - When quality checking is enabled, this attribute holds the minimum
     * number of characters that must be used in a password.  If this
     * attribute is not present, no minimum password length will be
     * enforced.  If the server is unable to check the length (due to a
     * hashed password or otherwise), the server will, depending on the
     * value of the pwdCheckQuality attribute, either accept the password
     * without checking it ('0' or '1') or refuse it ('2').</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#expireWarning} - This attribute specifies the maximum number of seconds before a
     * password is due to expire that expiration warning messages will be
     * returned to an authenticating user.  If this attribute is not present, or if the value is 0 no warnings
     * will be returned.  If not 0, the value must be smaller than the value
     * of the pwdMaxAge attribute.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#graceLoginLimit} - This attribute specifies the number of times an expired password can
     * be used to authenticate.  If this attribute is not present or if the
     * value is 0, authentication will fail. </li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#lockout} - This attribute indicates, when its value is "TRUE", that the password
     * may not be used to authenticate after a specified number of
     * consecutive failed bind attempts.  The maximum number of consecutive
     * failed bind attempts is specified in pwdMaxFailure.  If this attribute is not present, or if the
     * value is "FALSE", the password may be used to authenticate when the number of failed bind
     * attempts has been reached.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#lockoutDuration} - This attribute holds the number of seconds that the password cannot
     * be used to authenticate due to too many failed bind attempts.  If
     * this attribute is not present, or if the value is 0 the password
     * cannot be used to authenticate until reset by a password
     * administrator.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#maxFailure} - This attribute specifies the number of consecutive failed bind
     * attempts after which the password may not be used to authenticate.
     * If this attribute is not present, or if the value is 0, this policy
     * is not checked, and the value of pwdLockout will be ignored.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#failureCountInterval} - This attribute holds the number of seconds after which the password
     * failures are purged from the failure counter, even though no
     * successful authentication occurred.  If this attribute is not present, or if its value is 0, the failure
     * counter is only reset by a successful authentication.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#mustChange} - This attribute specifies with a value of "TRUE" that users must
     * change their passwords when they first bind to the directory after a
     * password is set or reset by a password administrator.  If this
     * attribute is not presen        request.setContextId(this.contextId);t, or if the value is "FALSE", users are not
     * required to change their password upon binding after the password
     * administrator sets or resets the password.  This attribute is not set
     * due to any actions specified by this document, it is typically set by
     * a password administrator after resetting a user's password.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#allowUserChange} - This attribute indicates whether users can change their own
     * passwords, although the change operation is still subject to access
     * control.  If this attribute is not present, a value of "TRUE" is
     * assumed.  This attribute is intended to be used in the absence of an access control mechanism.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#safeModify} - This attribute specifies whether or not the existing password must be
     * sent along with the new password when being changed.  If this
     * attribute is not present, a "FALSE" value is assumed.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#checkQuality} - This attribute indicates how the password quality will be verified
     * while being modified or added.  If this attribute is not present, or
     * if the value is '0', quality checking will not be enforced.  A value
     * of '1' indicates that the server will check the quality, and if the
     * server is unable to check it (due to a hashed password or other
     * reasons) it will be accepted.  A value of '2' indicates that the
     * server will check the quality, and if the server is unable to verify
     * it, it will return an error refusing the password. </li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#attribute} - This holds the name of the attribute to which the password policy is
     * applied.  For example, the password policy may be applied to the
     * userPassword attribute </li>
     * </ul>
     *
     * @param policy Object must contain {@link org.apache.directory.fortress.core.model.PwPolicy#name} and optionally other attributes.
     * @throws SecurityException In the event of data validation or system error.
     */
    @Override
    public void add( PwPolicy policy )
        throws SecurityException
    {
        VUtil.assertNotNull( policy, GlobalErrIds.PSWD_PLCY_NULL, CLS_NM + ".add" );
        FortRequest request = new FortRequest();
        request.setContextId( this.contextId );
        request.setEntity( policy );
        if ( this.adminSess != null )
        {
            request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.post( szRequest, HttpIds.PSWD_ADD );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * This method will update an exiting policy entry to the POLICIES data set.  This command is valid
     * if and only if the policy entry is already present in the POLICIES data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#name} - Maps to name attribute of pwdPolicy object class being updated.</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#minAge} - This attribute holds the number of seconds that must elapse between
     * modifications to the password.  If this attribute is not present, 0
     * seconds is assumed.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#maxAge} - This attribute holds the number of seconds after which a modified
     * password will expire. If this attribute is not present, or if the value is 0 the password
     * does not expire.  If not 0, the value must be greater than or equal
     * to the value of the pwdMinAge.
     * </li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#inHistory} - This attribute specifies the maximum number of used passwords stored
     * in the pwdHistory attribute. If this attribute is not present, or if the value is 0, used
     * passwords are not stored in the pwdHistory attribute and thus may be reused.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#minLength} - When quality checking is enabled, this attribute holds the minimum
     * number of characters that must be used in a password.  If this
     * attribute is not present, no minimum password length will be
     * enforced.  If the server is unable to check the length (due to a
     * hashed password or otherwise), the server will, depending on the
     * value of the pwdCheckQuality attribute, either accept the password
     * without checking it ('0' or '1') or refuse it ('2').</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#expireWarning} - This attribute specifies the maximum number of seconds before a
     * password is due to expire that expiration warning messages will be
     * returned to an authenticating user.  If this attribute is not present, or if the value is 0 no warnings
     * will be returned.  If not 0, the value must be smaller than the value
     * of the pwdMaxAge attribute.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#graceLoginLimit} - This attribute specifies the number of times an expired password can
     * be used to authenticate.  If this attribute is not present or if the
     * value is 0, authentication will fail. </li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#lockout} - This attribute indicates, when its value is "TRUE", that the password
     * may not be used to authenticate after a specified number of
     * consecutive failed bind attempts.  The maximum number of consecutive
     * failed bind attempts is specified in pwdMaxFailure.  If this attribute is not present, or if the
     * value is "FALSE", the password may be used to authenticate when the number of failed bind
     * attempts has been reached.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#lockoutDuration} - This attribute holds the number of seconds that the password cannot
     * be used to authenticate due to too many failed bind attempts.  If
     * this attribute is not present, or if the value is 0 the password
     * cannot be used to authenticate until reset by a password
     * administrator.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#maxFailure} - This attribute specifies the number of consecutive failed bind
     * attempts after which the password may not be used to authenticate.
     * If this attribute is not present, or if the value is 0, this policy
     * is not checked, and the value of pwdLockout will be ignored.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#failureCountInterval} - This attribute holds the number of seconds after which the password
     * failures are purged from the failure counter, even though no
     * successful authentication occurred.  If this attribute is not present, or if its value is 0, the failure
     * counter is only reset by a successful authentication.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#mustChange} - This attribute specifies with a value of "TRUE" that users must
     * change their passwords when they first bind to the directory after a
     * password is set or reset by a password administrator.  If this
     * attribute is not present, or if the value is "FALSE", users are not
     * required to change their password upon binding after the password
     * administrator sets or resets the password.  This attribute is not set
     * due to any actions specified by this document, it is typically set by
     * a password administrator after resetting a user's password.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#allowUserChange} - This attribute indicates whether users can change their own
     * passwords, although the change operation is still subject to access
     * control.  If this attribute is not present, a value of "TRUE" is
     * assumed.  This attribute is intended to be used in the absence of an access control mechanism.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#safeModify} - This attribute specifies whether or not the existing password must be
     * sent along with the new password when being changed.  If this
     * attribute is not present, a "FALSE" value is assumed.</li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#checkQuality} - This attribute indicates how the password quality will be verified
     * while being modified or added.  If this attribute is not present, or
     * if the value is '0', quality checking will not be enforced.  A value
     * of '1' indicates that the server will check the quality, and if the
     * server is unable to check it (due to a hashed password or other
     * reasons) it will be accepted.  A value of '2' indicates that the
     * server will check the quality, and if the server is unable to verify
     * it, it will return an error refusing the password. </li>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#attribute} - This holds the name of the attribute to which the password policy is
     * applied.  For example, the password policy may be applied to the
     * userPassword attribute </li>
     * </ul>
     *
     * @param policy Object must contain {@link org.apache.directory.fortress.core.model.PwPolicy#name} and optionally all non-null attributes will be updated.  null attributes will be ignored.
     * @throws SecurityException In the event policy not found , data validation or system error.
     */
    @Override
    public void update( PwPolicy policy )
        throws SecurityException
    {
        VUtil.assertNotNull( policy, GlobalErrIds.PSWD_PLCY_NULL, CLS_NM + ".update" );
        FortRequest request = new FortRequest();
        request.setContextId( this.contextId );
        request.setEntity( policy );
        if ( this.adminSess != null )
        {
            request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.post( szRequest, HttpIds.PSWD_UPDATE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * This method will delete exiting policy entry from the POLICIES data set.  This command is valid
     * if and only if the policy entry is already present in the POLICIES data set.  Existing users that
     * are assigned this policy will be removed from association.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#name} - Maps to name attribute of pwdPolicy object class being removed.</li>
     * </ul>
     *
     * @param policy Object must contain {@link org.apache.directory.fortress.core.model.PwPolicy#name} of the policy entity to remove.
     * @throws org.apache.directory.fortress.core.SecurityException
     *          In the event policy entity not found or system error.
     */
    @Override
    public void delete( PwPolicy policy )
        throws SecurityException
    {
        VUtil.assertNotNull( policy, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + ".delete" );
        FortRequest request = new FortRequest();
        request.setContextId( this.contextId );
        request.setEntity( policy );
        if ( this.adminSess != null )
        {
            request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.post( szRequest, HttpIds.PSWD_DELETE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * This method will return the password policy entity to the caller.  This command is valid
     * if and only if the policy entry is present in the POLICIES data set.
     * <h4>required parameters</h4>
     * <ul>
     * <li>{@link org.apache.directory.fortress.core.model.PwPolicy#name} - Maps to name attribute of pwdPolicy object class being read.</li>
     * </ul>
     *
     * @return PswdPolicy entity returns fully populated with attributes.
     * @throws SecurityException In the event policy entry not found, data validation or system error.
     */
    @Override
    public PwPolicy read( String name )
        throws SecurityException
    {
        VUtil.assertNotNullOrEmpty( name, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + ".read" );
        PwPolicy retPolicy;
        FortRequest request = new FortRequest();
        request.setContextId( this.contextId );
        request.setEntity( new PwPolicy( name ) );
        if ( this.adminSess != null )
        {
            request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.post( szRequest, HttpIds.PSWD_READ );
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
     * This method will return a list of all password policy entities that match a particular search string.
     * This command will return an empty list of no matching entries are found.
     *
     * @param searchVal String contains the leading chars of a policy entity.  This search is not case sensitive.
     * @return List<PswdPolicy> contains all matching password policy entities. If no records found this will be empty.
     * @throws SecurityException In the event of data validation or system error.
     */
    @Override
    public List<PwPolicy> search( String searchVal )
        throws SecurityException
    {
        VUtil.assertNotNull( searchVal, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + ".search" );
        List<PwPolicy> retPolicies;
        FortRequest request = new FortRequest();
        request.setContextId( this.contextId );
        request.setEntity( new PwPolicy( searchVal ) );
        if ( this.adminSess != null )
        {
            request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.post( szRequest, HttpIds.PSWD_SEARCH );
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
     * This method will associate a user entity with a password policy entity.  This function is valid
     * if and only if the user is a member of the USERS data set and the policyName refers to a
     * policy that is a member of the PWPOLICIES data set.
     *
     * @param userId Contains {@link org.apache.directory.fortress.core.model.User#userId} of a User entity in USERS data set.
     * @param name   String contains the {@link org.apache.directory.fortress.core.model.PwPolicy#name} of a pw policy entity contained within the PWPOLICIES data set.
     * @throws SecurityException thrown in the event either user or policy not valid or system error.
     */
    @Override
    public void updateUserPolicy( String userId, String name )
        throws SecurityException
    {
        String methodName = "updateUserPolicy";
        VUtil.assertNotNullOrEmpty( userId, GlobalErrIds.USER_NULL, CLS_NM + "." + methodName );
        VUtil.assertNotNullOrEmpty( name, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + "." + methodName );
        FortRequest request = new FortRequest();
        request.setContextId( this.contextId );
        request.setEntity( new PwPolicy( name ) );
        request.setValue( userId );
        if ( this.adminSess != null )
        {
            request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.post( szRequest, HttpIds.PSWD_USER_ADD );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }


    /**
     * This method will remove the pw policy assignment from a user entity.  This function is valid
     * if and only if the user is a member of the USERS data set and the policy attribute is assigned.
     * Removal of pw policy assignment will revert the user's policy to use the global default for OpenLDAP
     * instance that contains user.
     *
     * @param userId Contains {@link User#userId} of a User entity in USERS data set.
     * @throws SecurityException Thrown in the event either user not valid or system error.
     */
    @Override
    public void deletePasswordPolicy( String userId )
        throws SecurityException
    {
        VUtil.assertNotNullOrEmpty( userId, GlobalErrIds.USER_NULL, CLS_NM + ".deletePasswordPolicy" );
        FortRequest request = new FortRequest();
        request.setContextId( this.contextId );
        request.setValue( userId );
        if ( this.adminSess != null )
        {
            request.setSession( adminSess );
        }
        String szRequest = RestUtils.marshal( request );
        String szResponse = RestUtils.post( szRequest, HttpIds.PSWD_USER_DELETE );
        FortResponse response = RestUtils.unmarshall( szResponse );
        if ( response.getErrorCode() != 0 )
        {
            throw new SecurityException( response.getErrorCode(), response.getErrorMessage() );
        }
    }
}
