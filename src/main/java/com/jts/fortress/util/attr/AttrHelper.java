/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package com.jts.fortress.util.attr;

import com.jts.fortress.GlobalIds;
import com.jts.fortress.rbac.Administrator;
import com.jts.fortress.rbac.AuthZ;
import com.jts.fortress.rbac.Permission;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Properties;

/**
 * Class contains static utilities for various conversions of ldap data to application entity and back.  These methods are called by the
 * Fortress DAO's, i.e. {@link com.jts.fortress.rbac.UserDAO}.
 * These are low-level data utilities and no validations are performed.  These apis should not be called by outside programs.
 * <p/>
 * This class is thread safe.
 * <p/>

 * @author Shawn McKinney
 */
public class AttrHelper
{


    /**
     * Perform copy on ARBAC attributes.  This is used during assignment of {@link com.jts.fortress.rbac.AdminRole} to {@link com.jts.fortress.rbac.User}.
     * This method does not perform input validations.
     *
     * @param srcR contains source attributes to copy.
     * @param trgR contains the target reference.
     */
    public static void copyAdminAttrs(Administrator srcR, Administrator trgR)
    {
        trgR.setBeginInclusive(srcR.isBeginInclusive());
        trgR.setEndInclusive(srcR.isEndInclusive());
        trgR.setBeginRange(srcR.getBeginRange());
        trgR.setEndRange(srcR.getEndRange());
        // copy the user and perm pools:
        trgR.setOsP(srcR.getOsP());
        trgR.setOsU(srcR.getOsU());

    }

    /**
     * Convert from a {@link java.util.List} of properties stored as name:value pairs to
     * a {@link java.util.Properties}.
     *
     * @param propList contains a list of name-value pairs separated by a ':'.
     * @return reference to a Properties collection.
     */
    public static Properties getProperties(List<String> propList)
    {
        Properties props = null;
        if (propList != null && propList.size() > 0)
        {
            props = new Properties();
            int size = propList.size();
            for (int i = 0; i < size; i++)
            {

                String raw = propList.get(i);
                int indx = raw.indexOf(GlobalIds.COLON);
                if (indx >= 1)
                {
                    props.setProperty(raw.substring(0, indx), raw.substring(indx + 1));
                }
            }
        }
        return props;
    }

    /**
     * Convert from a comma delimited list of name-value pairs separated by a ':'.  Return the pros as {@link java.util.Properties}.
     *
     * @param inputString contains comma delimited list of properties.
     * @return java collection class containing props.
     */
    public static Properties getProperties(String inputString)
    {
        Properties props = new Properties();
        if (inputString != null && inputString.length() > 0)
        {
            StringTokenizer maxTkn = new StringTokenizer(inputString, GlobalIds.COMMA);
            if (maxTkn.countTokens() > 0)
            {
                while (maxTkn.hasMoreTokens())
                {
                    String val = maxTkn.nextToken();
                    int indx = val.indexOf(GlobalIds.COLON);
                    if (indx >= 1)
                    {
                        String name = val.substring(0, indx);
                        String value = val.substring(indx + 1);
                        props.setProperty(name, value);
                    }
                }
            }
        }
        return props;
    }

    /**
     * Parse a raw slapd access log format data string for userId.
     *
     * @param inputString raw slapd access log data.
     * @return string containing userId.
     */
    public static String getAuthZId(String inputString)
    {
        //reqAuthzID               <uid=fttu3user4,ou=people,dc=jts,dc=com>
        String userId = null;
        if (inputString != null && inputString.length() > 0)
        {
            StringTokenizer maxTkn = new StringTokenizer(inputString, ",");
            if (maxTkn.countTokens() > 0)
            {
                String val = maxTkn.nextToken();
                int indx = val.indexOf('=');
                if (indx >= 1)
                {
                    userId = val.substring(indx + 1);
                }
            }
        }
        return userId;
    }


    /**
     * Convert from raw ldap generalized time format to {@link java.util.Date}.  Use the UnboundID SDK, <a href="http://www.unboundid.com/products/ldap-sdk/">JGraphT</a>
     * to decode the string.
     *
     * @param inputString containing raw ldap generalized time formatted string.
     * @return converted to {@link java.util.Date}.
     */
    public static Date decodeGeneralizedTime(String inputString) throws ParseException
    {
        Date aDate;
        aDate = com.unboundid.util.StaticUtils.decodeGeneralizedTime(inputString);
        return aDate;
    }


    /**
     * Convert from java date {@link java.util.Date} format to raw ldap generalized time format.  Use the UnboundID SDK, <a href="http://www.unboundid.com/products/ldap-sdk/">JGraphT</a>
     * to encode the string.
     *
     * @param date reference to standard java date.
     * @return converted to standardized ldap generalized time format.
     */
    public static String encodeGeneralizedTime(Date date)
    {
        String szTime;
        szTime = com.unboundid.util.StaticUtils.encodeGeneralizedTime(date);
        return szTime;
    }

    /**
     * Parse slapd access raw data to pull the permission name out.
     *
     * @param authZ raw data contained in Fortress audit entity.
     * @return Permission contains {@link com.jts.fortress.rbac.Permission#objectName} and {@link com.jts.fortress.rbac.Permission#opName}
     */
    public static Permission getAuthZPerm(AuthZ authZ)
    {
        int indx = 0;
        final int objectClass = 1;
        final int oPNm = 2;
        final int oBjNm = 3;
        final int user = 4;
        final int roles = 6;

        // reqFilter
        // <(&(objectClass=ftOperation)
        // (ftOpNm=top1_10)(ftObjNm=tob2_4)
        // (|(ftUsers=fttu3user4)
        // (ftRoles=ftt3role1)
        // (ftRoles=ftt3role2)
        // (ftRoles=ftt3role3)
        // (ftRoles=ftt3role4)
        // (ftRoles=ftt3role5)
        // (ftRoles=ftt3role6)
        // (ftRoles=ftt3role7)
        // (ftRoles=ftt3role8)
        // (ftRoles=ftt3role9)
        // (ftRoles=ftt3role10)))>

        Permission pOp = new Permission();
        String userId = null;
        if (authZ.getReqFilter() != null && authZ.getReqFilter().length() > 0)
        {
            StringTokenizer maxTkn = new StringTokenizer(authZ.getReqFilter(), "(");
            //System.out.println("maxTken size=" + maxTkn.countTokens());
            int numTokens = maxTkn.countTokens();
            for (int i = 0; i < numTokens; i++)
            {
                String val = maxTkn.nextToken();
                //System.out.println("token[" + i + "]=" + val);
                switch (i)
                {
                    case objectClass:
                        indx = val.indexOf('=');
                        if (indx >= 1)
                        {
                            String value = val.substring(indx + 1, val.length() - 1);
                        }
                        break;

                    case oPNm:
                        indx = val.indexOf('=');
                        if (indx >= 1)
                        {
                            pOp.setOpName(val.substring(indx + 1, val.length() - 1));
                        }
                        break;

                    case oBjNm:
                        indx = val.indexOf('=');
                        if (indx >= 1)
                        {
                            pOp.setObjectName(val.substring(indx + 1, val.length() - 1));
                        }
                        break;

                    case user:
                        indx = val.indexOf('=');
                        if (indx >= 1)
                        {
                            pOp.setUser(val.substring(indx + 1, val.length() - 1));
                        }
                        break;

                    default:
                        int indx2 = 0;
                        if (i >= roles)
                        {
                            indx = val.indexOf('=');
                            indx2 = val.indexOf(')');
                        }
                        if (indx >= 1)
                        {
                            pOp.setRole(val.substring(indx + 1, indx2));
                        }
                        break;
                }
            }
        }
        return pOp;
    }
}
