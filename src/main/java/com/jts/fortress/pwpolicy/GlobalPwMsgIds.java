/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.pwpolicy;

/**
 * This class contains constants that contain status for Fortress password policy checking within {@link OLPWControlImpl#checkPasswordPolicy(com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection, boolean, com.jts.Fortress.security.rbac.user.PwMessage)}
 *
 * @author Shawn McKinney
 * @created October 13, 2009
 */
public class GlobalPwMsgIds
    implements java.io.Serializable
{
    /**
     * The processor could not process the password message.
     */
    public final static int INVALID_PASSWORD_MESSAGE = -10;

    /**
     * Password policies check out good.
     */
    public final static int GOOD = 0;

    /**
     * No warnings are associated with password.
     */
    public final static int PP_NOWARNING = 0;

    /**
     * The password policy control was not found in the message.
     */
    public final static int NO_CONTROLS_FOUND = 10;

    /**
     * The password is expiring.
     */
    public final static int PASSWORD_EXPIRATION_WARNING = 11;

    /**
     * Password in grace.
     */
    public final static int PASSWORD_GRACE_WARNING = 12;

    /**
     * The password has expired.
     */
    public final static int PASSWORD_HAS_EXPIRED = 100;

    /**
     * The User account is locked on the server.
     */
    public final static int ACCOUNT_LOCKED = 101;

    /**
     * The change after reset flag is set indicating the user must change their password.
     */
    public final static int CHANGE_AFTER_RESET = 102;

    /**
     * The User is not allowed to modify their password.
     */
    public final static int NO_MODIFICATIONS = 103;

    /**
     * The User did not supply their old password when trying to change.
     */
    public final static int MUST_SUPPLY_OLD = 104;

    /**
     * The password is not of sufficient quality.
     */
    public final static int INSUFFICIENT_QUALITY = 105;

    /**
     * The new password is too short.
     */
    public final static int PASSWORD_TOO_SHORT = 106;

    /**
     * The new password is too young to be changed.  This is typically used to prevent users from cycling thru
     * password on forced password change to reuse their previous password (to circumvent history constraints).
     */
    public final static int PASSWORD_TOO_YOUNG = 107;

    /**
     * The new password was found in history.
     */
    public final static int HISTORY_VIOLATION = 108;

    /**
     * The User account is locked.
     */
    public final static int ACCOUNT_LOCKED_CONSTRAINTS = 109;

    /**
     * These message will correspond with the errors above and will be loaded into the pw message entity and returned to
     * caller.
     */
    private final static String[] pwMsgs = {
        "password will expire",
        "password in grace limit",
        "password is expired",
        "user account is locked",
        "password requires change after reset",
        "password modification is not allowed",
        "old password is required",
        "insufficient password quality is found",
        "password is too short",
        "password is too young",
        "password is in history",
        "account is locked due to user constraints"
    };

    /**
     * array contains the password policy violations.
     */
    private final static int[] pwIds = {
        PASSWORD_EXPIRATION_WARNING,
        PASSWORD_GRACE_WARNING,
        PASSWORD_HAS_EXPIRED,
        ACCOUNT_LOCKED,
        CHANGE_AFTER_RESET,
        NO_MODIFICATIONS,
        MUST_SUPPLY_OLD,
        INSUFFICIENT_QUALITY,
        PASSWORD_TOO_SHORT,
        PASSWORD_TOO_YOUNG,
        HISTORY_VIOLATION,
        ACCOUNT_LOCKED_CONSTRAINTS
    };


    /**
     * Return a message that corresponds with error code for User's password.
     *
     * @param iId contains index offset of message.
     * @return string containing the error message.
     */
    public static String getMessage(int iId)
    {
        for (int i = 0; i < pwIds.length; i++)
        {
            if (iId == pwIds[i])
            {
                return pwMsgs[i];
            }
        }
        return "";
    }


    /**
     * Given a particular message value return the integer offset that corresponds to the index position
     * within the message array.
     *
     * @param strMsg
     * @return
     */
    private static int getMessageCode(String strMsg)
    {
        for (int i = 0; i < pwMsgs.length; i++)
        {
            if (pwMsgs[i].equals(strMsg))
            {
                return pwIds[i];
            }
        }
        return -1;
    }
}

