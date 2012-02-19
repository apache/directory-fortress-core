/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.pwpolicy;

import com.jts.fortress.FortEntity;
import com.jts.fortress.SecurityException;
import com.jts.fortress.arbac.AdminUtil;
import com.jts.fortress.pwpolicy.openldap.PolicyP;
import com.jts.fortress.rbac.Permission;
import com.jts.fortress.rbac.Session;
import com.jts.fortress.rbac.UserP;
import com.jts.fortress.util.attr.VUtil;
import com.jts.fortress.PswdPolicyMgr;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.rbac.User;

import java.util.List;

/**
 * This object is used to perform administrative and review functions on the PWPOLICIES and USERS data sets.
 * <p/>
 * <h4>Password Policies</h4>
 * <a href="http://www.openldap.org/">OpenLDAP</a> supports the IETF draft <a href="http://tools.ietf.org/html/draft-behera-ldap-password-policy-10/">Password Policies for LDAP directories</a></li>.  Policies may be applied at the user, group or global level.
 * <p/>
 * <img src="../../../../../images/PasswordPolicy.png">
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
 * This object is NOT thread safe.
 * <p/>

 *
 * @author smckinn
 * @created October 17, 2009
 */
public class PswdPolicyMgrImpl implements PswdPolicyMgr
{
    private static final String CLS_NM = PswdPolicyMgrImpl.class.getName();
    private static final PolicyP policyP = new PolicyP();

    // thread unsafe variable:
    private Session adminSess;

    /**
     * Setting Session into this object will enforce ARBAC controls and render this class
     * thread unsafe..
     *
     * @param session
     */
    public void setAdmin(Session session)
    {
        this.adminSess = session;
    }

    private void setEntitySession(String opName, FortEntity entity) throws SecurityException
    {
        if (this.adminSess != null)
        {
            // TODO: TURN IT BACK ON:
            AdminUtil.setEntitySession(adminSess, new Permission(CLS_NM, opName), entity);
        }
    }

    /**
     * This method will add a new policy entry to the POLICIES data set.  This command is valid
     * if and only if the policy entry is not already present in the POLICIES data set.
     *
     * @param policy Object must contain {@link PswdPolicy#name} and optionally other attributes.
     * @throws SecurityException In the event of data validation or system error.
     */
    public void add(PswdPolicy policy)
        throws SecurityException
    {
        String methodName = "add";
        VUtil.assertNotNull(policy, GlobalErrIds.PSWD_PLCY_NULL, CLS_NM + "." + methodName);
        setEntitySession(methodName, policy);
        policyP.add(policy);
    }


    /**
     * This method will update an exiting policy entry to the POLICIES data set.  This command is valid
     * if and only if the policy entry is already present in the POLICIES data set.
     *
     * @param policy Object must contain {@link PswdPolicy#name} and optionally all non-null attributes will be updated.  null attributes will be ignored.
     * @throws SecurityException In the event policy not found , data validation or system error.
     */
    public void update(PswdPolicy policy)
        throws SecurityException
    {
        String methodName = "update";
        VUtil.assertNotNull(policy, GlobalErrIds.PSWD_PLCY_NULL, CLS_NM + "." + methodName);
        setEntitySession(methodName, policy);
        policyP.update(policy);
    }


    /**
     * This method will delete exiting policy entry from the POLICIES data set.  This command is valid
     * if and only if the policy entry is already present in the POLICIES data set.  Existing users that
     * are assigned this policy will be removed from association.
     *
     * @param policy Object must contain {@link PswdPolicy#name} of the policy entity to remove.
     * @throws com.jts.fortress.SecurityException In the event policy entity not found or system error.
     */
    public void delete(PswdPolicy policy)
        throws SecurityException
    {
        String methodName = "delete";
        VUtil.assertNotNull(policy, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + "." + methodName);
        policy.setAdminSession(adminSess);
        setEntitySession(methodName, policy);
        policyP.delete(policy);
    }


    /**
     * This method will return the password policy entity to the caller.  This command is valid
     * if and only if the policy entry is present in the POLICIES data set.
     *
     * @return PswdPolicy entity returns fully populated with attributes.
     * @return PswdPolicy entity returns fully populated with attributes.
     * @throws SecurityException In the event policy entry not found, data validation or system error.
     */
    public PswdPolicy read(String name)
        throws SecurityException
    {
        String methodName = "read";
        VUtil.assertNotNullOrEmpty(name, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + "." + methodName);
        return policyP.read(name);
    }


    /**
     * This method will return a list of all password policy entities that match a particular search string.
     * This command will return an empty list of no matching entries are found.
     *
     * @param searchVal String contains the leading chars of a policy entity.  This search is not case sensitive.
     * @return List<PswdPolicy> contains all matching password policy entities. If no records found this will be empty.
     * @throws SecurityException In the event of data validation or system error.
     */
    public List<PswdPolicy> search(String searchVal)
        throws SecurityException
    {
        String methodName = "search";
        VUtil.assertNotNull(searchVal, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + "." + methodName);
        return policyP.search(searchVal);
    }


    /**
     * This method will associate a user entity with a password policy entity.  This function is valid
     * if and only if the user is a member of the USERS data set and the policyName refers to a
     * policy that is a member of the PWPOLICIES data set.
     *
     * @param userId     Contains {@link com.jts.fortress.rbac.User#userId} of a User entity in USERS data set.
     * @param policyName String contains the {@link PswdPolicy#name} of a pw policy entity contained within the PWPOLICIES data set.
     * @throws SecurityException thrown in the event either user or policy not valid or system error.
     */
    public void updateUserPolicy(String userId, String policyName)
        throws SecurityException
    {
        String methodName = "updateUserPolicy";
        VUtil.assertNotNullOrEmpty(userId, GlobalErrIds.USER_NULL, CLS_NM + "." + methodName);
        VUtil.assertNotNullOrEmpty(policyName, GlobalErrIds.PSWD_NAME_NULL, CLS_NM + "." + methodName);
        UserP up = new UserP();
        User user = new User(userId);
        user.setPwPolicy(policyName);
        user.setAdminSession(adminSess);
        setEntitySession(methodName, user);
        up.update(user);
    }


    /**
     * This method will return the pw policy assignment from a user entity.  This function is valid
     * if and only if the user is a member of the USERS data set and the policy attribute is assigned.
     * Removal of pw policy assignment will revert the user's policy to use the global default for OpenLDAP
     * instance that contains user.
     *
     * @param userId Contains {@link User#userId} of a User entity in USERS data set.
     * @throws SecurityException Thrown in the event either user not valid or system error.
     */
    public void deletePasswordPolicy(String userId)
        throws SecurityException
    {
        String methodName = "deletePasswordPolicy";
        VUtil.assertNotNullOrEmpty(userId, GlobalErrIds.USER_NULL, CLS_NM + "." + methodName);
        UserP up = new UserP();
        User user = new User(userId);
        user.setAdminSession(adminSess);
        setEntitySession(methodName, user);
        up.deletePwPolicy(user);
    }
}

