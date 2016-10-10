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
package org.apache.directory.fortress.core.model;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class contains the Password Policy entity which is used to pass directives into and out of ldap.<br>
 * The unique key to locate a Policy entity (which is subsequently assigned to Users) is {@link #name}.
 * <h3></h3>
 * <h4>Password Policies</h4>
 * <a href="http://www.openldap.org/">OpenLDAP</a> supports the IETF draft 
 * <a href="http://tools.ietf.org/html/draft-behera-ldap-password-policy-10/">Password Policies for LDAP directories</a>. 
 * Policies may be applied at the user, group or global level.
 * <p>
 * <img src="../doc-files/PasswordPolicy.png" alt="">
 * <p>
 * Password enforcement options include:
 * <ol>
 *   <li>A configurable limit on failed authentication attempts.</li>
 *   <li>A counter to track the number of failed authentication attempts.</li>
 *   <li>A time frame in which the limit of consecutive failed authentication attempts must happen before action is taken.</li>
 *   <li>The action to be taken when the limit is reached. The action will either be nothing, or the account will be locked.</li>
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
 * <h4>Schema</h4>
 * The OpenLDAP Password Policy entity is a composite of the following structural and aux object classes:
 * <p>
 * 1. organizationalRole Structural Object Class is used to store basic attributes like cn and description.
 * <pre>
 * ------------------------------------------
 * objectclass ( 2.5.6.14 NAME 'device'
 *  DESC 'RFC2256: a device'
 *  SUP top STRUCTURAL
 *  MUST cn
 *  MAY (
 *      serialNumber $ seeAlso $ owner $ ou $ o $ l $ description
 *  )
 * )
 * ------------------------------------------
 * </pre>
 * <p>
 * 2. pwdPolicy AUXILIARY Object Class is used to store OpenLDAP Password Policies.
 * <pre>
 * ------------------------------------------
 * objectclass ( 1.3.6.1.4.1.42.2.27.8.2.1
 *  NAME 'pwdPolicy'
 *  SUP top
 *  AUXILIARY
 *  MUST (
 *      pwdAttribute
 *  )
 *  MAY (
 *      pwdMinAge $ pwdMaxAge $ pwdInHistory $ pwdCheckQuality $
 *      pwdMinLength $ pwdExpireWarning $ pwdGraceAuthNLimit $ pwdLockout $
 *      pwdLockoutDuration $ pwdMaxFailure $ pwdFailureCountInterval $
 *      pwdMustChange $ pwdAllowUserChange $ pwdSafeModify
 *  )
 * )
 * ------------------------------------------
 * </pre>
 * <p>
 * 3. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.
 * <pre>
 * ------------------------------------------
 * Fortress Audit Modification Auxiliary Object Class
 * objectclass ( 1.3.6.1.4.1.38088.3.4
 *  NAME 'ftMods'
 *  DESC 'Fortress Modifiers AUX Object Class'
 *  AUXILIARY
 *  MAY (
 *      ftModifier $
 *      ftModCode $
 *      ftModId
 *  )
 * )
 * ------------------------------------------
 * </pre>
 * <p>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "fortPolicy")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pswdpolicy", propOrder = {
    "name",
    "attribute",
    "minAge",
    "maxAge",
    "inHistory",
    "checkQuality",
    "minLength",
    "expireWarning",
    "graceLoginLimit",
    "lockout",
    "lockoutDuration",
    "maxFailure",
    "failureCountInterval",
    "mustChange",
    "allowUserChange",
    "safeModify"
})
public class PwPolicy extends FortEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Maps to name attribute of pwdPolicy object class.
     */
    private String name;

    /**
     * 5.2.1  pwdAttribute
     * <p>
     * This holds the name of the attribute to which the password policy is
     * applied.  For example, the password policy may be applied to the
     * userPassword attribute.
     */
    private String attribute;

    /**
     * 5.2.2  pwdMinAge
     * <p>
     * This attribute holds the number of seconds that must elapse between
     * modifications to the password.  If this attribute is not present, 0
     * seconds is assumed.
     */
    private Integer minAge;

    /**
     * 5.2.3  pwdMaxAge
     * <p>
     * This attribute holds the number of seconds after which a modified
     * password will expire.
     * <p>
     * If this attribute is not present, or if the value is 0 the password
     * does not expire.  If not 0, the value must be greater than or equal
     * to the value of the pwdMinAge.
     */
    private Long maxAge;

    /**
     * 5.2.4  pwdInHistory
     * <p>
     * This attribute specifies the maximum number of used passwords stored
     * in the pwdHistory attribute.
     * <p>
     * If this attribute is not present, or if the value is 0, used
     * passwords are not stored in the pwdHistory attribute and thus may be
     * reused.
     */
    private Short inHistory;

    /**
     * 5.2.5  pwdCheckQuality
     * <p>
     * This attribute indicates how the password quality will be verified
     * while being modified or added.  If this attribute is not present, or
     * if the value is '0', quality checking will not be enforced.  A value
     * of '1' indicates that the server will check the quality, and if the
     * server is unable to check it (due to a hashed password or other
     * reasons) it will be accepted.  A value of '2' indicates that the
     * server will check the quality, and if the server is unable to verify
     * it, it will return an error refusing the password.
     */
    private Short checkQuality;

    /**
     * 5.2.6  pwdMinLength
     * <p>
     * When quality checking is enabled, this attribute holds the minimum
     * number of characters that must be used in a password.  If this
     * attribute is not present, no minimum password length will be
     * enforced.  If the server is unable to check the length (due to a
     * hashed password or otherwise), the server will, depending on the
     * value of the pwdCheckQuality attribute, either accept the password
     * without checking it ('0' or '1') or refuse it ('2').
     */
    private Short minLength;

    /**
     * 5.2.7  pwdExpireWarning
     * <p>
     * This attribute specifies the maximum number of seconds before a
     * password is due to expire that expiration warning messages will be
     * returned to an authenticating user.
     * <p>
     * If this attribute is not present, or if the value is 0 no warnings
     * will be returned.  If not 0, the value must be smaller than the value
     * of the pwdMaxAge attribute.
     */
    private Long expireWarning;

    /**
     * 5.2.8  pwdGraceAuthNLimit
     * <p>
     * This attribute specifies the number of times an expired password can
     * be used to authenticate.  If this attribute is not present or if the
     * value is 0, authentication will fail.
     */
    private Short graceLoginLimit;

    /**
     * 5.2.9  pwdLockout
     * <p>
     * This attribute indicates, when its value is "TRUE", that the password
     * may not be used to authenticate after a specified number of
     * consecutive failed bind attempts.  The maximum number of consecutive
     * failed bind attempts is specified in pwdMaxFailure.
     * <p>
     * If this attribute is not present, or if the value is "FALSE", the
     * password may be used to authenticate when the number of failed bind
     * attempts has been reached.
     */
    private Boolean lockout;

    /**
     * 5.2.10  pwdLockoutDuration
     * <p>
     * This attribute holds the number of seconds that the password cannot
     * be used to authenticate due to too many failed bind attempts.  If
     * this attribute is not present, or if the value is 0 the password
     * cannot be used to authenticate until reset by a password
     * administrator.
     */
    private Integer lockoutDuration;

    /**
     * 5.2.11  pwdMaxFailure
     * <p>
     * This attribute specifies the number of consecutive failed bind
     * attempts after which the password may not be used to authenticate.
     * If this attribute is not present, or if the value is 0, this policy
     * is not checked, and the value of pwdLockout will be ignored.
     */
    private Short maxFailure;

    /**
     * 5.2.12  pwdFailureCountInterval
     * <p>
     * This attribute holds the number of seconds after which the password
     * failures are purged from the failure counter, even though no
     * successful authentication occurred.
     * <p>
     * If this attribute is not present, or if its value is 0, the failure
     * counter is only reset by a successful authentication.
     */
    private Short failureCountInterval;

    /**
     * 5.2.13  pwdMustChange
     * <p>
     * This attribute specifies with a value of "TRUE" that users must
     * change their passwords when they first bind to the directory after a
     * password is set or reset by a password administrator.  If this
     * attribute is not present, or if the value is "FALSE", users are not
     * required to change their password upon binding after the password
     * administrator sets or resets the password.  This attribute is not set
     * due to any actions specified by this document, it is typically set by
     * a password administrator after resetting a user's password.
     */
    private Boolean mustChange;

    /**
     * 5.2.14  pwdAllowUserChange
     * <p>
     * This attribute indicates whether users can change their own
     * passwords, although the change operation is still subject to access
     * control.  If this attribute is not present, a value of "TRUE" is
     * assumed.  This attribute is intended to be used in the absence of an
     * access control mechanism.
     */
    private Boolean allowUserChange;

    /**
     * 5.2.15  pwdSafeModify
     * <p>
     * This attribute specifies whether or not the existing password must be
     * sent along with the new password when being changed.  If this
     * attribute is not present, a "FALSE" value is assumed.
     */
    private Boolean safeModify;

    /**
     * Default constructor is used by internal Fortress classes and not intended for external use.
     */
    public PwPolicy()
    {
    }
    

    /**
     * Create instance given a policy name.
     * @param name
     */
    public PwPolicy(String name)
    {
        this.name = name;
    }


    /**
     * Get the policy name associated with this instance.
     * @return attribute stored as 'cn' in 'pwdPolicy' object class.
     */
    public String getName()
    {
        return name;
    }
    

    /**
     * Set the required attribute policy name on this entity.
     * @param name stored as 'cn' in 'pwdPolicy' object class.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    

    /**
     * This optional attribute holds the number of seconds that must elapse between
     * modifications to the password. If this attribute is not present, 0
     * seconds is assumed.
     *
     * @return attribute stored as 'pwdMinAge' in 'pwdPolicy' object class.
     */
    public Integer getMinAge()
    {
        return minAge;
    }
    

    /**
     * This optional attribute holds the number of seconds that must elapse between
     * modifications to the password. If this attribute is not present, 0
     * seconds is assumed.
     *
     * @param minAge stored as 'pwdMinAge' in 'pwdPolicy' object class.
     */
    public void setMinAge(Integer minAge)
    {
        this.minAge = minAge;
    }
    

    /**
     * This optional attribute holds the number of seconds after which a modified
     * password will expire.
     * If this attribute is not present, or if the value is 0 the password
     * does not expire. If not 0, the value must be greater than or equal
     * to the value of the pwdMinAge.
     *
     * @return attribute stored as 'pwdMaxAge' in 'pwdPolicy' object class.
     */
    public Long getMaxAge()
    {
        return maxAge;
    }
    

    /**
     * This optional attribute holds the number of seconds after which a modified
     * password will expire.
     * If this attribute is not present, or if the value is 0 the password
     * does not expire. If not 0, the value must be greater than or equal
     * to the value of the pwdMinAge.
     *
     * @param maxAge attribute stored as 'pwdMaxAge' in 'pwdPolicy' object class.
     */
    public void setMaxAge(Long maxAge)
    {
        this.maxAge = maxAge;
    }
    

    /**
     * This optional attribute specifies the maximum number of used passwords stored
     * in the pwdHistory attribute.
     * If this attribute is not present, or if the value is 0, used
     * passwords are not stored in the pwdInHistory attribute and thus may be
     * reused.
     *
     * @return attribute stored as 'pwdInHistory' in 'pwdPolicy' object class.
     */
    public Short getInHistory()
    {
        return inHistory;
    }
    

    /**
     * This optional attribute specifies the maximum number of used passwords stored
     * in the pwdHistory attribute.
     * If this attribute is not present, or if the value is 0, used
     * passwords are not stored in the pwdInHistory attribute and thus may be
     * reused.
     *
     * @param inHistory attribute stored as 'pwdInHistory' in 'pwdPolicy' object class.
     */
    public void setInHistory(Short inHistory)
    {
        this.inHistory = inHistory;
    }
    

    /**
     * This optional attribute is not currently supported by Fortress.
     * This attribute indicates how the password quality will be verified
     * while being modified or added. If this attribute is not present, or
     * if the value is '0', quality checking will not be enforced. A value
     * of '1' indicates that the server will check the quality, and if the
     * server is unable to check it (due to a hashed password or other
     * reasons) it will be accepted. A value of '2' indicates that the
     * server will check the quality, and if the server is unable to verify
     * it, it will return an error refusing the password.
     *
     * @return attribute stored as 'pwdCheckQuality' in 'pwdPolicy' object class.
     */
    public Short getCheckQuality()
    {
        return checkQuality;
    }
    

    /**
     * This optional attribute is not currently supported by Fortress.
     * This attribute indicates how the password quality will be verified
     * while being modified or added. If this attribute is not present, or
     * if the value is '0', quality checking will not be enforced. A value
     * of '1' indicates that the server will check the quality, and if the
     * server is unable to check it (due to a hashed password or other
     * reasons) it will be accepted. A value of '2' indicates that the
     * server will check the quality, and if the server is unable to verify
     * it, it will return an error refusing the password.
     *
     * @param checkQuality attribute stored as 'pwdCheckQuality' in 'pwdPolicy' object class.
     */
    public void setCheckQuality(Short checkQuality)
    {
        this.checkQuality = checkQuality;
    }
    

    /**
     * When quality checking is enabled, this optional attribute holds the minimum
     * number of characters that must be used in a password. If this
     * attribute is not present, no minimum password length will be
     * enforced. If the server is unable to check the length (due to a
     * hashed password or otherwise), the server will, depending on the
     * value of the pwdCheckQuality attribute, either accept the password
     * without checking it ('0' or '1') or refuse it ('2').
     *
     * @return attribute stored as 'pwdMinLength' in 'pwdPolicy' object class.
     */
    public Short getMinLength()
    {
        return minLength;
    }
    

    /**
     * When quality checking is enabled, this optional attribute holds the minimum
     * number of characters that must be used in a password. If this
     * attribute is not present, no minimum password length will be
     * enforced. If the server is unable to check the length (due to a
     * hashed password or otherwise), the server will, depending on the
     * value of the pwdCheckQuality attribute, either accept the password
     * without checking it ('0' or '1') or refuse it ('2').
     *
     * @param minLength attribute stored as 'pwdMinLength' in 'pwdPolicy' object class.
     */
    public void setMinLength(Short minLength)
    {
        this.minLength = minLength;
    }
    

    /**
     * This optional attribute specifies the maximum number of seconds before a
     * password is due to expire that expiration warning messages will be
     * returned to an authenticating user.
     * If this attribute is not present, or if the value is 0 no warnings
     * will be returned. If not 0, the value must be smaller than the value
     * of the pwdMaxAge attribute.
     *
     * @return attribute stored as 'pwdExpireWarning' in 'pwdPolicy' object class.
     */
    public Long getExpireWarning()
    {
        return expireWarning;
    }
    

    /**
     * This optional attribute specifies the maximum number of seconds before a
     * password is due to expire that expiration warning messages will be
     * returned to an authenticating user.
     * If this attribute is not present, or if the value is 0 no warnings
     * will be returned. If not 0, the value must be smaller than the value
     * of the pwdMaxAge attribute.
     *
     * @param expireWarning attribute stored as 'pwdExpireWarning' in 'pwdPolicy' object class.
     */
    public void setExpireWarning(Long expireWarning)
    {
        this.expireWarning = expireWarning;
    }
    

    /**
     * This optional attribute specifies the number of times an expired password can
     * be used to authenticate. If this attribute is not present or if the
     * value is 0, authentication will fail.
     *
     * @return attribute stored as 'pwdGraceAuthNLimit' in 'pwdPolicy' object class.
     */
    public Short getGraceLoginLimit()
    {
        return graceLoginLimit;
    }
    

    /**
     * This optional attribute specifies the number of times an expired password can
     * be used to authenticate. If this attribute is not present or if the
     * value is 0, authentication will fail.
     *
     * @param graceLoginLimit attribute stored as 'pwdGraceAuthNLimit' in 'pwdPolicy' object class.
     */
    public void setGraceLoginLimit(Short graceLoginLimit)
    {
        this.graceLoginLimit = graceLoginLimit;
    }
    

    /**
     * This optional attribute indicates, when its value is "TRUE", that the password
     * may not be used to authenticate after a specified number of
     * consecutive failed bind attempts. The maximum number of consecutive
     * failed bind attempts is specified in pwdMaxFailure.
     * If this attribute is not present, or if the value is "FALSE", the
     * password may be used to authenticate when the number of failed bind
     * attempts has been reached.
     *
     * @return attribute stored as 'pwdLockout' in 'pwdPolicy' object class.
     */
    public Boolean getLockout()
    {
        return lockout;
    }
    

    /**
     * This optional attribute indicates, when its value is "TRUE", that the password
     * may not be used to authenticate after a specified number of
     * consecutive failed bind attempts. The maximum number of consecutive
     * failed bind attempts is specified in pwdMaxFailure.
     * If this attribute is not present, or if the value is "FALSE", the
     * password may be used to authenticate when the number of failed bind
     * attempts has been reached.
     *
     * @param lockout attribute stored as 'pwdLockout' in 'pwdPolicy' object class.
     */
    public void setLockout(Boolean lockout)
    {
        this.lockout = lockout;
    }
    

    /**
     * This optional attribute holds the number of seconds that the password cannot
     * be used to authenticate due to too many failed bind attempts. If
     * this attribute is not present, or if the value is 0 the password
     * cannot be used to authenticate until reset by a password
     * administrator.
     *
     * @return attribute stored as 'pwdLockoutDuration' in 'pwdPolicy' object class.
     */
    public Integer getLockoutDuration()
    {
        return lockoutDuration;
    }
    

    /**
     * This optional attribute holds the number of seconds that the password cannot
     * be used to authenticate due to too many failed bind attempts. If
     * this attribute is not present, or if the value is 0 the password
     * cannot be used to authenticate until reset by a password
     * administrator.
     *
     * @param lockoutDuration attribute stored as 'pwdLockoutDuration' in 'pwdPolicy' object class.
     */
    public void setLockoutDuration(Integer lockoutDuration)
    {
        this.lockoutDuration = lockoutDuration;
    }
    

    /**
     * This optional attribute specifies the number of consecutive failed bind
     * attempts after which the password may not be used to authenticate.
     * If this attribute is not present, or if the value is 0, this policy
     * is not checked, and the value of pwdLockout will be ignored.
     *
     * @return attribute stored as 'pwdMaxFailure' in 'pwdPolicy' object class.
     */
    public Short getMaxFailure()
    {
        return maxFailure;
    }
    

    /**
     * This optional attribute specifies the number of consecutive failed bind
     * attempts after which the password may not be used to authenticate.
     * If this attribute is not present, or if the value is 0, this policy
     * is not checked, and the value of pwdLockout will be ignored.
     *
     * @param maxFailure attribute stored as 'pwdMaxFailure' in 'pwdPolicy' object class.
     */
    public void setMaxFailure(Short maxFailure)
    {
        this.maxFailure = maxFailure;
    }
    

    /**
     * This optional attribute holds the number of seconds after which the password
     * failures are purged from the failure counter, even though no
     * successful authentication occurred.
     * If this attribute is not present, or if its value is 0, the failure
     * counter is only reset by a successful authentication.
     *
     * @return attribute stored as 'pwdFailureCountInterval' in 'pwdPolicy' object class.
     */
    public Short getFailureCountInterval()
    {
        return failureCountInterval;
    }
    

    /**
     * This optional attribute holds the number of seconds after which the password
     * failures are purged from the failure counter, even though no
     * successful authentication occurred.
     * If this attribute is not present, or if its value is 0, the failure
     * counter is only reset by a successful authentication.
     *
     * @param failureCountInterval attribute stored as 'pwdFailureCountInterval' in 'pwdPolicy' object class.
     */
    public void setFailureCountInterval(Short failureCountInterval)
    {
        this.failureCountInterval = failureCountInterval;
    }
    

    /**
     * This optional attribute specifies with a value of "TRUE" that users must
     * change their passwords when they first bind to the directory after a
     * password is set or reset by a password administrator. If this
     * attribute is not present, or if the value is "FALSE", users are not
     * required to change their password upon binding after the password
     * administrator sets or resets the password. This attribute is not set
     * due to any actions specified by this document, it is typically set by
     * a password administrator after resetting a user's password.
     *
     * @return attribute stored as 'pwdMustChange' in 'pwdPolicy' object class.
     */
    public Boolean getMustChange()
    {
        return mustChange;
    }
    

    /**
     * This optional attribute specifies with a value of "TRUE" that users must
     * change their passwords when they first bind to the directory after a
     * password is set or reset by a password administrator. If this
     * attribute is not present, or if the value is "FALSE", users are not
     * required to change their password upon binding after the password
     * administrator sets or resets the password. This attribute is not set
     * due to any actions specified by this document, it is typically set by
     * a password administrator after resetting a user's password.
     *
     * @param mustChange attribute stored as 'pwdMustChange' in 'pwdPolicy' object class.
     */
    public void setMustChange(Boolean mustChange)
    {
        this.mustChange = mustChange;
    }
    

    /**
     * This optional attribute indicates whether users can change their own
     * passwords, although the change operation is still subject to access
     * control. If this attribute is not present, a value of "TRUE" is
     * assumed. This attribute is intended to be used in the absence of an
     * access control mechanism.
     *
     * @return attribute stored as 'pwdAllowUserChange' in 'pwdPolicy' object class.
     */
    public Boolean getAllowUserChange()
    {
        return allowUserChange;
    }
    

    /**
     * This optional attribute indicates whether users can change their own
     * passwords, although the change operation is still subject to access
     * control. If this attribute is not present, a value of "TRUE" is
     * assumed. This attribute is intended to be used in the absence of an
     * access control mechanism.
     *
     * @param allowUserChange attribute stored as 'pwdAllowUserChange' in 'pwdPolicy' object class.
     */
    public void setAllowUserChange(Boolean allowUserChange)
    {
        this.allowUserChange = allowUserChange;
    }
    

    /**
     * This optional attribute specifies whether or not the existing password must be
     * sent along with the new password when being changed. If this
     * attribute is not present, a "FALSE" value is assumed.
     *
     * @return attribute stored as 'pwdSafeModify' in 'pwdPolicy' object class.
     */
    public Boolean getSafeModify()
    {
        return safeModify;
    }
    

    /**
     * This optional attribute specifies whether or not the existing password must be
     * sent along with the new password when being changed. If this
     * attribute is not present, a "FALSE" value is assumed.
     *
     * @param safeModify attribute stored as 'pwdSafeModify' in 'pwdPolicy' object class.
     */
    public void setSafeModify(Boolean safeModify)
    {
        this.safeModify = safeModify;
    }
    

    /**
     * Matches the name from two PwPolicy entities.
     *
     * @param thatObj contains a Role entity.
     * @return boolean indicating both objects contain matching PwPolicy names.
     */
    public boolean equals(Object thatObj)
    {
        if ( this == thatObj )
        {
            return true;
        }
        
        if ( this.getName() == null )
        {
            return false;
        }
        
        if ( !( thatObj instanceof PwPolicy ) )
        {
            return false;
        }
        
        PwPolicy thatPolicy = (PwPolicy) thatObj;
        
        if ( thatPolicy.getName() == null )
        {
            return false;
        }
        
        return thatPolicy.getName().equalsIgnoreCase( this.getName() );
    }

    @Override
    public int hashCode()
    {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + ( attribute != null ? attribute.hashCode() : 0 );
        result = 31 * result + ( minAge != null ? minAge.hashCode() : 0 );
        result = 31 * result + ( maxAge != null ? maxAge.hashCode() : 0 );
        result = 31 * result + ( inHistory != null ? inHistory.hashCode() : 0 );
        result = 31 * result + ( checkQuality != null ? checkQuality.hashCode() : 0 );
        result = 31 * result + ( minLength != null ? minLength.hashCode() : 0 );
        result = 31 * result + ( expireWarning != null ? expireWarning.hashCode() : 0 );
        result = 31 * result + ( graceLoginLimit != null ? graceLoginLimit.hashCode() : 0 );
        result = 31 * result + ( lockout != null ? lockout.hashCode() : 0 );
        result = 31 * result + ( lockoutDuration != null ? lockoutDuration.hashCode() : 0 );
        result = 31 * result + ( maxFailure != null ? maxFailure.hashCode() : 0 );
        result = 31 * result + ( failureCountInterval != null ? failureCountInterval.hashCode() : 0 );
        result = 31 * result + ( mustChange != null ? mustChange.hashCode() : 0 );
        result = 31 * result + ( allowUserChange != null ? allowUserChange.hashCode() : 0 );
        result = 31 * result + ( safeModify != null ? safeModify.hashCode() : 0 );
        return result;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append( "PwPolicy object: \n" );

        sb.append( "    attribute :" ).append( attribute ).append( '\n' );
        sb.append( "    maxAge :" ).append( maxAge ).append( '\n' );
        sb.append( "    minAge :" ).append( minAge ).append( '\n' );
        sb.append( "    allowUserChange :" ).append( allowUserChange ).append( '\n' );
        sb.append( "    checkQuality :" ).append( checkQuality ).append( '\n' );
        sb.append( "    expireWarning :" ).append( expireWarning ).append( '\n' );
        sb.append( "    failureCountInterval :" ).append( failureCountInterval ).append( '\n' );
        sb.append( "    graceLoginLimit :" ).append( graceLoginLimit ).append( '\n' );
        sb.append( "    inHistory :" ).append( inHistory ).append( '\n' );
        sb.append( "    lockout :" ).append( lockout ).append( '\n' );
        sb.append( "    lockoutDuration :" ).append( lockoutDuration ).append( '\n' );
        sb.append( "    maxFailure :" ).append( maxFailure ).append( '\n' );
        sb.append( "    minLength :" ).append( minLength ).append( '\n' );
        sb.append( "    mustChange :" ).append( mustChange ).append( '\n' );
        sb.append( "    name :" ).append( name ).append( '\n' );
        sb.append( "    safeModify :" ).append( safeModify ).append( '\n' );

        return sb.toString();
    }
}