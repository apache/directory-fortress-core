/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.util.attr;

import com.jts.fortress.configuration.Config;
import com.jts.fortress.ValidationException;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.constants.GlobalIds;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;


/**
 * This class contains simple data validation utilities.  The data validations include null, length
 * and simple reasonability checking.  All utilities will throw {@link com.jts.fortress.ValidationException} for failures.
 */
public class VUtil
{
    private static final String OCLS_NM = VUtil.class.getName();
    private final static Logger log = Logger.getLogger(OCLS_NM);
    private static int maximumFieldLen = 130;
    private final static String VALIDATE_LENGTH = "fieldLength";

    static
    {
        String lengthProp = Config.getProperty(VALIDATE_LENGTH);
        try
        {
            if (lengthProp != null)
            {
                Integer len = new Integer(lengthProp);
                maximumFieldLen = len.intValue();
            }
        }
        catch (java.lang.NumberFormatException nfe)
        {
            //ignore
        }
    }

    private final static int MAXIMUM_FIELD_LEN = maximumFieldLen;
    private final static int maxFieldLength = MAXIMUM_FIELD_LEN;
    private final static char[] LDAP_META_CHARS = loadLdapEscapeChars();
    private final static String[] LDAP_REPL_VALS = loadValidLdapVals();
    private static final int TIME_LEN = 4;
    private static final int DATE_LEN = 8;
    private static final int DAYMASK_LEN = 7;
    private static final String TIME_FORMAT = "HHmm";
    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final char SUNDAY = '1';
    private static final char SATURDAY = '7';

    /**
     * Simple length check on orgunit that uses {@link com.jts.fortress.constants.GlobalIds#OU_LEN}.
     * @param orgUnitId contains the ou name.
     * @throws ValidationException in the event of failure, {@link com.jts.fortress.constants.GlobalErrIds#ORG_LEN_INVLD}.
     *
     */
    public static void orgUnit(String orgUnitId)
        throws ValidationException
    {
        int length = orgUnitId.length();
        if (length > maxFieldLength)
        {
            String error = OCLS_NM + ".orgUnit value <" + orgUnitId + "> invalid length <" + length + ">";
            throw new com.jts.fortress.ValidationException(GlobalErrIds.ORG_LEN_INVLD, error);
        }
    }

    /**
     * Simple length check on User password that uses {@link com.jts.fortress.constants.GlobalIds#PASSWORD_LEN}.
     * @param password contains the User's password.
     * @throws com.jts.fortress.ValidationException in the event of failure, {@link com.jts.fortress.constants.GlobalErrIds#USER_PW_INVLD_LEN}.
     *
     */
    public static void password(char[] password)
        throws com.jts.fortress.ValidationException
    {
        int length = password.length;
        if (length > GlobalIds.PASSWORD_LEN)
        {
            String error = OCLS_NM + ".password invalid length <" + length + ">";
            throw new com.jts.fortress.ValidationException(GlobalErrIds.USER_PW_INVLD_LEN, error);
        }
    }

    /**
     * Simple length check and safe text validation on description field that uses {@link com.jts.fortress.constants.GlobalIds#DESC_LEN}.
     * @param description contains the User's password.
     * @throws com.jts.fortress.ValidationException in the event of failure, {@link com.jts.fortress.constants.GlobalErrIds#CONST_DESC_LEN_INVLD}.
     *
     */
    public static void description(String description)
        throws ValidationException
    {
        int length = description.length();
        if (length > GlobalIds.DESC_LEN)
        {
            String error = OCLS_NM + ".description value <" + description + "> invalid length <" + length + ">";
            throw new com.jts.fortress.ValidationException(GlobalErrIds.CONST_DESC_LEN_INVLD, error);
        }
        RegExUtil.safeText(description);
    }

    /**
     * Perform a simple length and safe text validation.
     * @param value contains the attribute to check.
     * @param validLen contains the length to use.
     * @throws com.jts.fortress.ValidationException in the event of length {@link com.jts.fortress.constants.GlobalErrIds#CONST_INVLD_FIELD_LEN} or regex failure.
     *
     */
    public static void safeText(String value, int validLen)
        throws com.jts.fortress.ValidationException
    {
        int length = value.length();
        if (length > validLen)
        {
            String error = OCLS_NM + ".safeText value <" + value + "> invalid length <" + length + ">";
            throw new com.jts.fortress.ValidationException(GlobalErrIds.CONST_INVLD_FIELD_LEN, error);
        }
        RegExUtil.safeText(value);
    }

    /**
     * Simple null, {@link com.jts.fortress.constants.GlobalErrIds#USER_ID_NULL}, and length checks, {@link com.jts.fortress.constants.GlobalErrIds#CONST_INVLD_FIELD_LEN}, on userId.
     * @param userId contains the userId, maps to {@link com.jts.fortress.rbac.User#userId}.
     * @throws com.jts.fortress.ValidationException in the event of failure, {@link GlobalErrIds#CONST_INVLD_FIELD_LEN}.
     *
     */
    public static void userId(String userId)
        throws com.jts.fortress.ValidationException
    {
        if (!isNotNullOrEmpty(userId))
        {
            String error = OCLS_NM + ".userId validation failed, null or empty value";
            throw new ValidationException(GlobalErrIds.USER_ID_NULL, error);
        }
        int length = userId.length();
        if (length > GlobalIds.USERID_LEN)
        {
            String error = OCLS_NM + ".safeText value <" + userId + "> invalid length <" + length + ">";
            throw new ValidationException(GlobalErrIds.CONST_INVLD_FIELD_LEN, error);
        }
    }

    /**
     * Perform simple length and safe text validations on collection of name-value pairs.
     *
     * @param props contains name-value pairs in {@code name:value} format.
     * @throws com.jts.fortress.ValidationException in the event of failure.
     */
    public static void properties(Properties props)
        throws ValidationException
    {
        if (props != null && props.size() > 0)
        {
            for (Enumeration e = props.propertyNames(); e.hasMoreElements();)
            {
                String key = (String) e.nextElement();
                String val = props.getProperty(key);
                safeText(key, com.jts.fortress.constants.GlobalIds.PROP_LEN);
                safeText(val, com.jts.fortress.constants.GlobalIds.PROP_LEN);
            }
        }
    }


    /**
     * Perform simple reasonability check on contraint timeout value.
     * @param timeout must be greater than 0 and less than max value for {@link Integer#MAX_VALUE}
     * @throws com.jts.fortress.ValidationException in the event value falls out of range.
     *
     */
    public static void timeout(Integer timeout)
        throws com.jts.fortress.ValidationException
    {
        if (timeout < 0 || timeout >= Integer.MAX_VALUE)
        {
            String error = OCLS_NM + ".timeout - invalid timeout value <" + timeout + ">";
            throw new ValidationException(GlobalErrIds.CONST_TIMEOUT_INVLD, error);
        }
    }

    /**
     * Perform simple reasonability check on contraint beginTime value.
     * @param beginTime if set, must be equal to {@link #TIME_LEN}.
     * @throws com.jts.fortress.ValidationException in the event value falls out of range.
     *
     */
    public static void beginTime(String beginTime)
        throws com.jts.fortress.ValidationException
    {
        if (beginTime != null && beginTime.length() == TIME_LEN)
        {
            if (checkTime(beginTime))
            {
                String error = OCLS_NM + ".beginTime - invalid beginTime value <" + beginTime + ">";
                throw new ValidationException(GlobalErrIds.CONST_BEGINTIME_INVLD, error);
            }
        }
        else
        {
            String error = OCLS_NM + ".beginTime - null or invalid length (must be 4) for beginTime value";
            throw new com.jts.fortress.ValidationException(GlobalErrIds.CONST_BEGINTIME_LEN_ERR, error);
        }
    }

    /**
     * Perform simple reasonability check on contraint endTime value.
     * @param endTime if set, must be equal to {@link #TIME_LEN}.
     * @throws com.jts.fortress.ValidationException in the event value falls out of range.
     *
     */
    public static void endTime(String endTime)
        throws com.jts.fortress.ValidationException
    {
        if (endTime != null && endTime.length() == TIME_LEN)
        {
            if (checkTime(endTime))
            {
                String error = OCLS_NM + ".endTime - invalid endTime value <" + endTime + ">";
                throw new com.jts.fortress.ValidationException(GlobalErrIds.CONST_ENDTIME_INVLD, error);
            }
        }
        else
        {
            String error = OCLS_NM + ".endTime - null or invalid length (must be 4) for endTime value";
            throw new com.jts.fortress.ValidationException(com.jts.fortress.constants.GlobalErrIds.CONST_ENDTIME_LEN_ERR, error);
        }
    }

    /**
     * Perform simple reasonability check on contraint beginDate value.
     * @param beginDate if set, must be equal to {@link #DATE_LEN}.
     * @throws com.jts.fortress.ValidationException in the event value falls out of range.
     *
     */
    public static void beginDate(String beginDate)
        throws ValidationException
    {
        if (isNotNullOrEmpty(beginDate))
        {
            if (beginDate.compareToIgnoreCase(com.jts.fortress.constants.GlobalIds.NONE) != 0)
            {
                if (beginDate.length() != DATE_LEN || checkDate(beginDate))
                {
                    String error = OCLS_NM + ".beginDate - invalid beginDate value <" + beginDate + ">";
                    throw new com.jts.fortress.ValidationException(GlobalErrIds.CONST_BEGINDATE_INVLD, error);
                }
            }
        }
        else
        {
            String error = OCLS_NM + ".beginDate - null or empty beginDate value";
            throw new ValidationException(GlobalErrIds.CONST_BEGINDATE_NULL, error);
        }
    }

    /**
     * Perform simple reasonability check on contraint endDate value.
     * @param endDate if set, must be equal to {@link #DATE_LEN}.
     * @throws com.jts.fortress.ValidationException in the event value falls out of range.
     *
     */
    public static void endDate(String endDate)
        throws ValidationException
    {
        if (isNotNullOrEmpty(endDate))
        {
            if (endDate.compareToIgnoreCase(GlobalIds.NONE) != 0)
            {
                if (endDate.length() != DATE_LEN || checkDate(endDate))
                {
                    String error = OCLS_NM + ".endDate - invalid endDate value <" + endDate + ">";
                    throw new ValidationException(GlobalErrIds.CONST_ENDDATE_INVLD, error);
                }
            }
        }
        else
        {
            String error = OCLS_NM + ".endDate - null or empty endDate value";
            throw new com.jts.fortress.ValidationException(GlobalErrIds.CONST_ENDDATE_NULL, error);
        }
    }

    /**
     * Perform simple reasonability check on contraint dayMask value.
     * @param dayMask if set, will be validated.
     * @throws com.jts.fortress.ValidationException in the event value falls out of range.
     *
     */
    public static void dayMask(String dayMask)
        throws ValidationException
    {
        if (isNotNullOrEmpty(dayMask))
        {
            if (dayMask.compareToIgnoreCase(GlobalIds.ALL) != 0)
            {
                if (dayMask.length() > DAYMASK_LEN || checkMask(dayMask))
                {
                    String error = OCLS_NM + ".dayMask - invalid dayMask value <" + dayMask + ">";
                    throw new ValidationException(GlobalErrIds.CONST_DAYMASK_INVLD, error);
                }
            }
        }
        else
        {
            String error = OCLS_NM + ".dayMask - null or empty dayMask value";
            throw new com.jts.fortress.ValidationException(GlobalErrIds.CONST_DAYMASK_NULL, error);
        }
    }

    /**
     * @param time
     * @return
     */
    private static boolean checkTime(String time)
    {
        boolean fail = false;

        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
        format.setLenient(false);
        try
        {
            Date parsed = format.parse(time);
        }
        catch (ParseException pe)
        {
            fail = true;
            String error = OCLS_NM + ".checkTime - time <" + time + "> failed validation with ParseException=" + pe;
            log.warn(error);
        }

        return fail;
    }

    /**
     *
     * @param date
     * @return
     */
    private static boolean checkDate(String date)
    {
        boolean fail = false;

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        format.setLenient(false);
        try
        {
            Date parsed = format.parse(date);
        }
        catch (ParseException pe)
        {
            fail = true;
            String error = OCLS_NM + ".checkDate - date <" + date + "> failed validation with ParseException=" + pe;
            log.warn(error);
        }

        return fail;
    }

    /**
     * @param mask
     * @return
     */
    private static boolean checkMask(String mask)
    {
        boolean fail = false;
        for (int i = 0; (i < mask.length() && !fail); i++)
        {
            char ch = mask.charAt(i);
            if (ch < SUNDAY || ch > SATURDAY)
            {
                fail = true;
                String error = OCLS_NM + ".checkMask - mask <" + mask + "> failed validation";
                log.warn(error);
            }
        }
        return fail;
    }


    /**
     * Method will throw exception with supplied error id and object.method name if object reference is null.
     * @param obj contains the reference to check.
     * @param errorCode contains the error id to use if null.
     * @param method contains the method name of caller.
     * @throws com.jts.fortress.ValidationException in the event object is null.
     */
    public static void assertNotNull(Object obj, int errorCode, String method)
        throws ValidationException
    {
        if (obj == null)
        {
            String error = OCLS_NM + ".assertNotNull detected null entity for method <" + method + ">, error code <" + errorCode + ">";
            throw new ValidationException(errorCode, error);
        }
    }

    /**
     * Method will throw exception with supplied error id and object.method name if string reference is null or empty.
     * @param value contains the reference to check.
     * @param errorCode contains the error id to use if null.
     * @param method contains the method name of caller.
     * @throws com.jts.fortress.ValidationException in the event supplied string is null or empty.
     */
    public static void assertNotNullOrEmpty(String value, int errorCode, String method)
        throws ValidationException
    {
        if (value == null || value.length() == 0)
        {
            String error = OCLS_NM + ".assertNotNull detected null entity for method <" + method + ">, error code <" + errorCode + ">";
            throw new com.jts.fortress.ValidationException(errorCode, error);
        }
    }

    /**
     * Method will throw exception with supplied error id and object.method name if string reference is null or empty.
     * @param value contains the reference to check.
     * @param errorCode contains the error id to use if null.
     * @param method contains the method name of caller.
     * @throws com.jts.fortress.ValidationException in the event supplied string is null or empty.
     */
    public static void assertNotNullOrEmpty(char[] value, int errorCode, String method)
        throws ValidationException
    {
        if (value == null || value.length == 0)
        {
            String error = OCLS_NM + ".assertNotNull detected null entity for method <" + method + ">, error code <" + errorCode + ">";
            throw new com.jts.fortress.ValidationException(errorCode, error);
        }
    }

    /**
     * Method will return true if string array reference is not null or empty.
     * @param value contains the reference to string array.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotNullOrEmpty(String[] value)
     {
        boolean result = false;
        if (value != null && value.length > 0)
        {
            result = true;
        }
        return result;
    }

    /**
     * Method will return true if string reference is not null or empty.
     * @param value contains the reference to string.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotNullOrEmpty(String value)
     {
        boolean result = false;
        if (value != null && value.length() > 0)
        {
            result = true;
        }
        return result;
    }

    /**
     * Method will return true if string reference is not null or empty.
     * @param value contains the reference to string.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotNullOrEmpty(char[] value)
     {
        boolean result = false;
        if (value != null && value.length > 0)
        {
            result = true;
        }
        return result;
    }

    /**
     * Method will return true if list is not null or empty.
     * @param list contains the reference to list.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotNullOrEmpty(List list)
     {
        boolean result = false;
        if (list != null && list.size() > 0)
        {
            result = true;
        }
        return result;
    }

    /**
     * Method will return true if set is not null or empty.
     * @param set contains the reference to set.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotNullOrEmpty(Set set)
     {
        boolean result = false;
        if (set != null && set.size() > 0)
        {
            result = true;
        }
        return result;
    }

    /**
     * Method will return true if props is not null or empty.
     * @param props contains the reference to props.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotNullOrEmpty(Properties props)
     {
        boolean result = false;
        if (props != null && props.size() > 0)
        {
            result = true;
        }
        return result;
    }

    /**
     * Method will return true if input is not null or empty.
     * @param iVal contains the reference to Integer variable.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotNullOrEmpty(Integer iVal)
     {
        boolean result = false;
        if (iVal != null)
        {
            result = true;
        }
        return result;
    }

    /**
     *
     * @param value
     * @param validLen
     * @return
     * @throws com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException
     */
    public static String encodeSafeText(String value, int validLen)
        throws LDAPException
    {
        if (isNotNullOrEmpty(value))
        {
            int length = value.length();
            if (length > validLen)
            {
                String error = OCLS_NM + ".encodeSafeText value <" + value + "> invalid length <" + length + ">";
                throw new LDAPException(error, LDAPException.PARAM_ERROR);
            }
            if (GlobalIds.LDAP_FILTER_SIZE_FOUND)
            {
                value = escapeLDAPSearchFilter(value);
            }
        }
        return value;
    }

    /**
     * Perform encoding on supplied input string for certain unsafe ascii characters.  These chars may be unsafe because ldap reserves some
     * characters as operands.  Safe encoding safeguards from malicious scripting input errors that are possible iff data filtering
     * did not get performed before being passed into dao layer.
     *
     * @param filter contains the data to filter.
     * @return possibly modified input string for matched characters.
     */
    public static String escapeLDAPSearchFilter(String filter)
    {
        StringBuilder sb = new StringBuilder();
        int filterLen = filter.length();
        for (int i = 0; i < filterLen; i++)
        {
            boolean found = false;
            char curChar = filter.charAt(i);
            int j = 0;
            for (; j < GlobalIds.LDAP_FILTER_SIZE; j++)
            {
                if (LDAP_META_CHARS[j] > curChar)
                {
                    break;
                }
                else if (curChar == LDAP_META_CHARS[j])
                {
                    sb.append("\\");
                    sb.append(LDAP_REPL_VALS[j]);
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                sb.append(curChar);
            }
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    private static char[] loadLdapEscapeChars()
    {
        if (!GlobalIds.LDAP_FILTER_SIZE_FOUND)
        {
            return null;
        }
        char[] ldapMetaChars = new char[GlobalIds.LDAP_FILTER_SIZE];
        for (int i = 1; ; i++)
        {
            String prop = GlobalIds.LDAP_FILTER + i;
            String value = Config.getProperty(prop);
            if (value == null)
            {
                break;
            }
            ldapMetaChars[i - 1] = value.charAt(0);
        }
        return ldapMetaChars;
    }

    /**
     *
     * @return
     */
    private static String[] loadValidLdapVals()
    {
        if (!GlobalIds.LDAP_FILTER_SIZE_FOUND)
        {
            return null;
        }
        String[] ldapReplacements = new String[GlobalIds.LDAP_FILTER_SIZE];
        for (int i = 1; ; i++)
        {
            String prop = GlobalIds.LDAP_SUB + i;
            String value = Config.getProperty(prop);
            if (value == null)
            {
                break;
            }
            ldapReplacements[i - 1] = value;
        }
        return ldapReplacements;
    }
}