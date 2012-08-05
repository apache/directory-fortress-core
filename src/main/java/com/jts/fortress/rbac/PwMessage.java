/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

/**
 * Interface is implemented by {@link com.jts.fortress.rbac.Session} and prescribes methods used to return Fortress
 * password messages to the caller.
 * <p/>

 *
 * @author Shawn McKinney
 * @created October 13, 2009
 */
public interface PwMessage
{
    /**
     * Return the {@link com.jts.fortress.rbac.User#userId} from entity.
     *
     * @param userId maps to {@code uid} attribute on inetOrgPerson object class.
     */
    public void setUserId(String userId);

    /**
     * Set the {@link com.jts.fortress.rbac.User#userId} in entity.
     *
     * @return userId maps to {@code uid} attribute on inetOrgPerson object class.
     */
    public String getUserId();

    /**
     * Contains the message that corresponds to password.  These messages map to {@link GlobalPwMsgIds#pwMsgs}
     *
     * @param message
     */
    public void setMsg(String message);

    /**
     * Return the message that corresponds to last password check.
     *
     * @return message maps to {@link GlobalPwMsgIds#pwMsgs}
     */
    public String getMsg();

    /**
     * If set to true the user's password check out good.
     *
     * @param isAuthenticated
     */
    public void setAuthenticated(boolean isAuthenticated);

    /**
     * If set to true the user's password check out good.
     *
     * @return param isAuthenticated
     */
    public boolean isAuthenticated();

    /**
     * Return the warning id that pertain to User's password. This attribute maps to values between 0 and 100 contained within here {@link GlobalPwMsgIds}
     *
     * @param warning contains warning id.
     */
    public void setWarningId(int warning);


    /**
     * Set the warning id that pertain to User's password. This attribute maps to values between 0 and 100 contained within here {@link GlobalPwMsgIds}
     *
     * @return warning contains warning id.
     */
    public int getWarningId();

    /**
     * Set the error id that pertain to User's password. This attribute maps to values greater than or equal to 100 contained within here {@link GlobalPwMsgIds}
     *
     * @param error contains error id that maps to {@link GlobalPwMsgIds#pwIds}
     */
    public void setErrorId(int error);

    /**
     * Return the error id that pertain to User's password. This attribute maps to values greater than or equal to 100 contained within here {@link GlobalPwMsgIds}
     *
     * @return error contains error id that maps to {@link GlobalPwMsgIds#pwIds}
     */
    public int getErrorId();

    /**
     * Grace count indicates how many binds User can perform before password slips into expired state.
     *
     * @param grace integer containing number of binds allowed for user.
     */
    public void setGraceLogins(int grace);

    /**
     * Get the grace count which indicates how many binds User can perform before password slips into expired state.
     *
     * @return grace integer containing number of binds allowed for user.
     */
    public int getGraceLogins();

    /**
     * The number of seconds until the User's password expires.
     *
     * @param expire value is computed by ldap server and contains the number of seconds until password will expire.
     */
    public void setExpirationSeconds(int expire);

    /**
     * Get the number of seconds until the User's password expires.
     *
     * @return expire value is computed by ldap server and contains the number of seconds until password will expire.
     */
    public int getExpirationSeconds();
}

