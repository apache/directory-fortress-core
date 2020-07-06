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

import org.apache.directory.fortress.annotation.AdminPermissionOperation;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.PwPolicyMgr;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.PwPolicy;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.util.VUtil;

/**
 * This class is used to perform administrative and review functions on the PWPOLICIES and USERS data sets.
 * <h3></h3>
 * <h4>Password Policies</h4>
 * OpenLDAP and ApacheDS support the IETF draft
 * <a href="http://tools.ietf.org/html/draft-behera-ldap-password-policy-10/">Password Policies for LDAP directories</a>.
 * Policies may be applied at the user, group or global level.
 * <p>
 * <img src="../doc-files/PasswordPolicy.png" alt="">
 * <p>
 * Password enforcement options include:
 * <ol>
 *   <li>A configurable limit on failed authentication attempts.</li>
 *   <li>A counter to track the number of failed authentication attempts.</li>
 *   <li>
 *     A time frame in which the limit of consecutive failed authentication attempts must happen before action is taken.
 *   </li>
 *   <li>
 *     The action to be taken when the limit is reached. The action will either be nothing, or the account will be locked.
 *   </li>
 *   <li>An amount of time the account is locked (if it is to be locked) This can be indefinite.</li>
 *   <li>Password expiration.</li>
 *   <li>Expiration warning</li>
 *   <li>Grace authentications</li>
 *   <li>Password history</li>
 *   <li>Password minimum age</li>
 *   <li>Password minimum length</li>
 *   <li>Password Change after Reset</li>
 *   <li>Safe Modification of Password</li>
 * </ol>
 * <p>
 * This class is NOT thread safe if parent instance variables ({@link #contextId} or {@link #adminSess}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class PwPolicyMgrImpl  extends Manageable implements PwPolicyMgr
{
    private static final String CLS_NM = PwPolicyMgrImpl.class.getName();
    private PolicyP policyP;
    private UserP userP;

    public PwPolicyMgrImpl() {
        policyP = new PolicyP();
        userP = new UserP();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void add(PwPolicy policy)
        throws SecurityException
    {
        String methodName = "add";
        assertContext(CLS_NM, methodName, policy, GlobalErrIds.PSWD_PLCY_NULL);
        setEntitySession(CLS_NM, methodName, policy);
        policyP.add(policy);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void update(PwPolicy policy)
        throws SecurityException
    {
        String methodName = "update";
        assertContext(CLS_NM, methodName, policy, GlobalErrIds.PSWD_PLCY_NULL);
        setEntitySession(CLS_NM, methodName, policy);
        policyP.update(policy);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void delete(PwPolicy policy)
        throws SecurityException
    {
        String methodName = "delete";
        assertContext(CLS_NM, methodName, policy, GlobalErrIds.PSWD_PLCY_NULL);
        policy.setAdminSession(adminSess);
        setEntitySession(CLS_NM, methodName, policy);
        policyP.delete(policy);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public PwPolicy read(String name)
        throws SecurityException
    {
        String methodName = "read";
        VUtil.assertNotNullOrEmpty(name, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + "." + methodName);
        checkAccess(CLS_NM, methodName);
        PwPolicy policy = new PwPolicy(name);
        policy.setContextId(this.contextId);
        return policyP.read(policy);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public List<PwPolicy> search(String searchVal)
        throws SecurityException
    {
        String methodName = "search";
        VUtil.assertNotNull(searchVal, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + "." + methodName);
        checkAccess(CLS_NM, methodName);
        PwPolicy policy = new PwPolicy(searchVal);
        policy.setContextId(this.contextId);
        return policyP.search(policy);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void updateUserPolicy(String userId, String policyName)
        throws SecurityException
    {
        String methodName = "updateUserPolicy";
        VUtil.assertNotNullOrEmpty(userId, GlobalErrIds.USER_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNullOrEmpty(policyName, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + "." + methodName);
        User user = new User(userId);
        user.setPwPolicy(policyName);
        user.setAdminSession(adminSess);
        setEntitySession(CLS_NM, methodName, user);
        userP.update(user);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @AdminPermissionOperation
    public void deletePasswordPolicy(String userId)
        throws SecurityException
    {
        String methodName = "deletePasswordPolicy";
        VUtil.assertNotNullOrEmpty(userId, GlobalErrIds.USER_NULL, CLS_NM + "." + methodName);
        User user = new User(userId);
        user.setAdminSession(adminSess);
        setEntitySession(CLS_NM, methodName, user);
        userP.deletePwPolicy(user);
    }
}

