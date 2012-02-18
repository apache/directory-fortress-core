/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.cli;


import com.jts.fortress.arbac.AdminRole;
import com.jts.fortress.arbac.OrgUnit;
import com.jts.fortress.hier.Relationship;
import com.jts.fortress.rbac.*;
import com.jts.fortress.util.attr.VUtil;
import com.jts.fortress.util.time.Constraint;

import java.util.Vector;

/**
 * This converts between Fortress entities and the JArg Options.  It contains attributes passed from JArgs command interpreter.

 * @author smckinn
 * @created December 1, 2011
 */
public class Options implements java.io.Serializable
{
    CmdLineParser parser;
    CmdLineParser.Option userId;
    CmdLineParser.Option password;
    CmdLineParser.Option newPassword;
    CmdLineParser.Option ou;
    CmdLineParser.Option pwPolicy;
    CmdLineParser.Option cn;
    CmdLineParser.Option sn;
    CmdLineParser.Option description;
    CmdLineParser.Option beginTime;
    CmdLineParser.Option endTime;
    CmdLineParser.Option beginDate;
    CmdLineParser.Option endDate;
    CmdLineParser.Option beginLockDate;
    CmdLineParser.Option endLockDate;
    CmdLineParser.Option dayMask;
    CmdLineParser.Option name;
    CmdLineParser.Option timeout;
    CmdLineParser.Option properties;
    CmdLineParser.Option roleAssigns;
    CmdLineParser.Option role;
    CmdLineParser.Option adminRoleAssigns;
    CmdLineParser.Option type;
    CmdLineParser.Option opName;
    CmdLineParser.Option ascendant;
    CmdLineParser.Option descendant;
    CmdLineParser.Option cardinality;
    CmdLineParser.Option osPs;
    CmdLineParser.Option osUs;
    CmdLineParser.Option beginRange;
    CmdLineParser.Option endRange;
    CmdLineParser.Option beginInclusive;
    CmdLineParser.Option endInclusive;

    /**
     *
     * @param parser
     */
    public Options(CmdLineParser parser)
    {
        this.parser = parser;
        this.userId = parser.addStringOption('u', "userId");
        this.password = parser.addStringOption('p', "password");
        this.newPassword = parser.addStringOption('V', "newpassword");
        this.ou = parser.addStringOption('o', "orgUnit");
        this.pwPolicy = parser.addStringOption('w', "pwPolicy");
        this.cn = parser.addStringOption('c', "cn");
        this.sn = parser.addStringOption('s', "sn");
        this.description = parser.addStringOption('d', "description");
        this.beginTime = parser.addStringOption('b', "beginTime");
        this.endTime = parser.addStringOption('e', "endTime");
        this.beginDate = parser.addStringOption('B', "beginDate");
        this.endDate = parser.addStringOption('E', "endDate");
        this.beginLockDate = parser.addStringOption('l', "beginLockDate");
        this.endLockDate = parser.addStringOption('N', "endLockDate");
        this.dayMask = parser.addStringOption('m', "dayMask");
        this.name = parser.addStringOption('n', "name");
        this.timeout = parser.addStringOption('t', "timeout");
        this.properties = parser.addStringOption('v', "properties");
        this.roleAssigns = parser.addStringOption('r', "roles");
        this.role = parser.addStringOption('R', "role");
        this.adminRoleAssigns = parser.addStringOption('a', "adminRoles");
        this.type = parser.addStringOption('T', "type");
        this.opName = parser.addStringOption('O', "opName");
        this.ascendant = parser.addStringOption('A', "ascendant");
        this.descendant = parser.addStringOption('D', "descendant");
        this.cardinality = parser.addStringOption('C', "cardinality");
        this.osPs = parser.addStringOption('P', "osPs");
        this.osUs = parser.addStringOption('U', "osUs");
        this.beginRange = parser.addStringOption('x', "beginRange");
        this.endRange = parser.addStringOption('w', "endRange");
        this.beginInclusive = parser.addStringOption('y', "beginInclusive");
        this.endInclusive = parser.addStringOption('z', "endInclusive");
    }

    /**
     *
     * @return
     */
    public SDSet getSdSet()
    {
        SDSet sdSet = new SDSet();
        sdSet.setName(getName());
        sdSet.setDescription(getDescription());
        updateRoleAssigns(sdSet);
        try
        {
            Integer cardinality = new Integer(getCardinality());
            sdSet.setCardinality(cardinality);
        }
        catch(NumberFormatException ne)
        {
            // default is '2'.
            sdSet.setCardinality(new Integer(2));
        }
        return sdSet;
    }

    /**
     *
     * @return
     */
    public OrgUnit getOrgUnit()
    {
        OrgUnit orgUnit = new OrgUnit();
        orgUnit.setName(getName());
        orgUnit.setDescription(getDescription());
        return orgUnit;
    }

    /**
     *
     * @return
     */
    public Role getRole()
    {
        Role role = new Role();
        role.setDescription(getDescription());
        updateTemporal(role);
        return role;
    }

    /**
     *
     * @return
     */
    public AdminRole getAdminRole()
    {
        AdminRole role = new AdminRole();
        role.setDescription(getDescription());
        role.setBeginRange(getBeginRange());
        role.setEndRange(getEndRange());
        Boolean bVal = new Boolean(getBeginInclusive());
        role.setBeginInclusive(bVal);
        bVal = new Boolean(getEndInclusive());
        role.setEndInclusive(bVal);
        updateOsPs(role);
        updateOsUs(role);
        updateTemporal(role);
        return role;
    }

    /**
     *
     * @return
     */
    public Relationship getRelationship()
    {
        Relationship relationship = new Relationship();
        relationship.setChild(getDescendant());
        relationship.setParent(getAscendant());
        return relationship;
    }

    /**
     *
     * @return
     */
    public PermObj getPermObj()
    {
        PermObj permObj = new PermObj();
        permObj.setObjectName(getName());
        permObj.setDescription(getDescription());
        permObj.setOu(getOu());
        permObj.setType(getType());
        updateProperties(permObj);
        return permObj;
    }

    public Permission getPermission()
    {
        Permission perm = new Permission();
        perm.setObjectName(getName());
        perm.setOpName(getOpName());
        updateRoleAssigns(perm);
        updateProperties(perm);
        perm.setType(getType());
        return perm;
    }

    /**
     *
     * @return
     */
    public User getUser()
    {
        User user = new User();
        user.setUserId(getUserId());
        user.setPassword(getPassword());
        user.setOu(getOu());
        user.setPwPolicy(getPwPolicy());
        user.setCn(getCn());
        user.setSn(getSn());
        user.setDescription(getDescription());
        updateTemporal(user);
        updateProperties(user);
        updateRoleAssigns(user);
        updateAdminRoleAssigns(user);
        return user;
    }

    private void updateTemporal(Constraint constraint)
    {
        constraint.setBeginTime(getBeginTime());
        constraint.setEndTime(getEndTime());
        constraint.setBeginDate(getBeginDate());
        constraint.setEndDate(getEndDate());
        constraint.setBeginLockDate(getBeginLockDate());
        constraint.setEndLockDate(getEndLockDate());
        constraint.setDayMask(getDayMask());
        constraint.setName(getName());
        try
        {
            Integer to = new Integer(getTimeout());
            constraint.setTimeout(to.intValue());
        }
        catch(NumberFormatException ne)
        {
            constraint.setTimeout(0);
        }
    }

    public String getUserId()
    {
        return (String)parser.getOptionValue(userId);
    }

    public char[] getPassword()
    {
        char[] pw = null;
        String szPw = (String)parser.getOptionValue(password);
        if(VUtil.isNotNullOrEmpty(szPw))
        {
            pw = szPw.toCharArray();
        }
        return pw;
    }

    public char[] getNewPassword()
    {
        char[] pw = null;
        String szPw = (String)parser.getOptionValue(newPassword);
        if(VUtil.isNotNullOrEmpty(szPw))
        {
            pw = szPw.toCharArray();
        }
        return pw;
    }

    private void updateProperties(User user)
    {
        Vector fractionValues = parser.getOptionValues(properties);
        if(fractionValues != null)
        {
            for(Object raw : fractionValues)
            {
                String szRaw = (String)raw;
                int indx = szRaw.indexOf(':');
                if (indx >= 1)
                {
                    user.addProperty(szRaw.substring(0, indx), szRaw.substring(indx + 1));
                }
            }
        }
    }

    private void updateProperties(PermObj permObj)
    {
        Vector fractionValues = parser.getOptionValues(properties);
        if(fractionValues != null)
        {
            for(Object raw : fractionValues)
            {
                String szRaw = (String)raw;
                int indx = szRaw.indexOf(':');
                if (indx >= 1)
                {
                    permObj.addProperty(szRaw.substring(0, indx), szRaw.substring(indx + 1));
                }
            }
        }
    }

    private void updateProperties(Permission perm)
    {
        Vector fractionValues = parser.getOptionValues(properties);
        if(fractionValues != null)
        {
            for(Object raw : fractionValues)
            {
                String szRaw = (String)raw;
                int indx = szRaw.indexOf(':');
                if (indx >= 1)
                {
                    perm.addProperty(szRaw.substring(0, indx), szRaw.substring(indx + 1));
                }
            }
        }
    }

    private void updateRoleAssigns(User user)
    {
        Vector fractionValues = parser.getOptionValues(roleAssigns);
        if(fractionValues != null)
        {
            for(Object raw : fractionValues)
            {
                String szRaw = (String)raw;
                user.setRole(szRaw);
            }
        }
    }

    private void updateRoleAssigns(Permission perm)
    {
        Vector fractionValues = parser.getOptionValues(roleAssigns);
        if(fractionValues != null)
        {
            for(Object raw : fractionValues)
            {
                String szRaw = (String)raw;
                perm.setRole(szRaw);
            }
        }
    }

    private void updateAdminRoleAssigns(User user)
    {
        Vector fractionValues = parser.getOptionValues(adminRoleAssigns);
        if(fractionValues != null)
        {
            for(Object raw : fractionValues)
            {
                String szRaw = (String)raw;
                user.setAdminRole(szRaw);
            }
        }
    }

    private void updateRoleAssigns(SDSet sdSet)
    {
        Vector fractionValues = parser.getOptionValues(roleAssigns);
        if(fractionValues != null)
        {
            for(Object raw : fractionValues)
            {
                String szRaw = (String)raw;
                sdSet.addMember(szRaw);
            }
        }
    }

    private void updateOsPs(AdminRole role)
    {
        Vector fractionValues = parser.getOptionValues(osPs);
        if(fractionValues != null)
        {
            for(Object raw : fractionValues)
            {
                String szRaw = (String)raw;
                role.setOsP(szRaw);
            }
        }
    }

    private void updateOsUs(AdminRole role)
    {
        Vector fractionValues = parser.getOptionValues(osPs);
        if(fractionValues != null)
        {
            for(Object raw : fractionValues)
            {
                String szRaw = (String)raw;
                role.setOsU(szRaw);
            }
        }
    }

    public String getOu()
    {
        return (String)parser.getOptionValue(ou);
    }

    public String getPwPolicy()
    {
        return (String)parser.getOptionValue(pwPolicy);
    }

    public String getCn()
    {
        return (String)parser.getOptionValue(cn);
    }

    public String getSn()
    {
        return (String)parser.getOptionValue(sn);
    }

    public String getDescription()
    {
        return (String)parser.getOptionValue(description);
    }

    public String getBeginTime()
    {
        return (String)parser.getOptionValue(beginTime);
    }

    public String getEndTime()
    {
        return (String)parser.getOptionValue(endTime);
    }

    public String getBeginDate()
    {
        return (String)parser.getOptionValue(beginDate);
    }

    public String getEndDate()
    {
        return (String)parser.getOptionValue(endDate);
    }

    public String getBeginLockDate()
    {
        return (String)parser.getOptionValue(beginLockDate);
    }

    public String getEndLockDate()
    {
        return (String)parser.getOptionValue(endLockDate);
    }

    public String getDayMask()
    {
        return (String)parser.getOptionValue(dayMask);
    }

    public String getRoleNm()
     {
         return (String)parser.getOptionValue(role);
     }

     public String getName()
    {
        return (String)parser.getOptionValue(name);
    }

    public String getTimeout()
    {
        return (String)parser.getOptionValue(timeout);
    }

    public String getType()
    {
        return (String)parser.getOptionValue(type);
    }

    public String getOpName()
    {
        return (String)parser.getOptionValue(opName);
    }

    public String getAscendant()
    {
        return (String)parser.getOptionValue(ascendant);
    }

    public String getDescendant()
    {
        return (String)parser.getOptionValue(descendant);
    }

    public String getCardinality()
    {
        return (String)parser.getOptionValue(cardinality);
    }

    public String getBeginRange()
    {
        return (String)parser.getOptionValue(beginRange);
    }

    public String getEndRange()
    {
        return (String)parser.getOptionValue(endRange);
    }

    public String getBeginInclusive()
    {
        return (String)parser.getOptionValue(beginInclusive);
    }

    public String getEndInclusive()
    {
        return (String)parser.getOptionValue(endInclusive);
    }
}
