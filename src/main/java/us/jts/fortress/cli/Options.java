/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */
package us.jts.fortress.cli;

import us.jts.fortress.GlobalIds;
import us.jts.fortress.rbac.AdminRole;
import us.jts.fortress.rbac.OrgUnit;
import us.jts.fortress.rbac.Relationship;
import us.jts.fortress.rbac.*;
import us.jts.fortress.util.attr.VUtil;
import us.jts.fortress.util.time.Constraint;

import java.util.Vector;

/**
 * This converts between Fortress entities and the JArg Options.  It contains attributes passed from JArgs command interpreter.
 *
 * @author Shawn McKinney
 */
public class Options implements java.io.Serializable
{
    final CmdLineParser parser;
    final CmdLineParser.Option userId;
    final CmdLineParser.Option password;
    final CmdLineParser.Option newPassword;
    final CmdLineParser.Option ou;
    final CmdLineParser.Option pwPolicy;
    final CmdLineParser.Option cn;
    final CmdLineParser.Option sn;
    final CmdLineParser.Option description;
    final CmdLineParser.Option title;
    final CmdLineParser.Option employeeType;
    final CmdLineParser.Option beginTime;
    final CmdLineParser.Option endTime;
    final CmdLineParser.Option beginDate;
    final CmdLineParser.Option endDate;
    final CmdLineParser.Option beginLockDate;
    final CmdLineParser.Option endLockDate;
    final CmdLineParser.Option dayMask;
    final CmdLineParser.Option name;
    final CmdLineParser.Option timeout;
    final CmdLineParser.Option properties;
    final CmdLineParser.Option roleAssigns;
    final CmdLineParser.Option role;
    final CmdLineParser.Option adminRoleAssigns;
    final CmdLineParser.Option type;
    final CmdLineParser.Option opName;
    final CmdLineParser.Option ascendant;
    final CmdLineParser.Option descendant;
    final CmdLineParser.Option cardinality;
    final CmdLineParser.Option osPs;
    final CmdLineParser.Option osUs;
    final CmdLineParser.Option beginRange;
    final CmdLineParser.Option endRange;
    final CmdLineParser.Option beginInclusive;
    final CmdLineParser.Option endInclusive;
    final CmdLineParser.Option phones;
    final CmdLineParser.Option mobiles;
    final CmdLineParser.Option emails;
    final CmdLineParser.Option address;
    final CmdLineParser.Option state;
    final CmdLineParser.Option city;
    final CmdLineParser.Option postalCode;
    final CmdLineParser.Option postalOfficeBox;

    /**
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
        this.phones = parser.addStringOption('y', "phones");
        this.mobiles = parser.addStringOption('Y', "mobiles");
        this.emails = parser.addStringOption('@', "emails");
        this.address = parser.addStringOption('>', "address");
        this.state = parser.addStringOption('<', "state");
        this.city = parser.addStringOption('3', "city");
        this.postalCode = parser.addStringOption('z', "postalCode");
        this.postalOfficeBox = parser.addStringOption('2', "postalOfficeBox");
        this.title = parser.addStringOption('3', "title");
        this.employeeType = parser.addStringOption('4', "employeeType");
    }

    /**
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
        catch (NumberFormatException ne)
        {
            // default is '2'.
            sdSet.setCardinality(2);
        }
        return sdSet;
    }

    /**
     */
    public OrgUnit getOrgUnit()
    {
        OrgUnit orgUnit = new OrgUnit();
        orgUnit.setName(getName());
        orgUnit.setDescription(getDescription());
        return orgUnit;
    }

    /**
     */
    public Role getRole()
    {
        Role role = new Role();
        role.setDescription(getDescription());
        updateTemporal(role);
        return role;
    }

    /**
     */
    public AdminRole getAdminRole()
    {
        AdminRole role = new AdminRole();
        role.setDescription(getDescription());
        role.setBeginRange(getBeginRange());
        role.setEndRange(getEndRange());
        Boolean bVal = Boolean.valueOf(getBeginInclusive());
        role.setBeginInclusive(bVal);
        bVal = Boolean.valueOf(getEndInclusive());
        role.setEndInclusive(bVal);
        updateOsPs(role);
        updateOsUs(role);
        updateTemporal(role);
        return role;
    }

    /**
     */
    public Relationship getRelationship()
    {
        Relationship relationship = new Relationship();
        relationship.setChild(getDescendant());
        relationship.setParent(getAscendant());
        return relationship;
    }

    /**
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
        user.setTitle(getTitle());
        user.setEmployeeType(getEmployeeType());
        updateTemporal(user);
        updateProperties(user);
        updateRoleAssigns(user);
        updateAdminRoleAssigns(user);
        updatePhones(user);
        updateMobiles(user);
        updateEmails(user);
        updateAddress(user);
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
            constraint.setTimeout(to);
        }
        catch (NumberFormatException ne)
        {
            constraint.setTimeout(0);
        }
    }

    public String getUserId()
    {
        return (String) parser.getOptionValue(userId);
    }

    public char[] getPassword()
    {
        char[] pw = null;
        String szPw = (String) parser.getOptionValue(password);
        if (VUtil.isNotNullOrEmpty(szPw))
        {
            pw = szPw.toCharArray();
        }
        return pw;
    }

    public char[] getNewPassword()
    {
        char[] pw = null;
        String szPw = (String) parser.getOptionValue(newPassword);
        if (VUtil.isNotNullOrEmpty(szPw))
        {
            pw = szPw.toCharArray();
        }
        return pw;
    }

    private void updateProperties(User user)
    {
        Vector fractionValues = parser.getOptionValues(properties);
        if (fractionValues != null)
        {
            for (Object raw : fractionValues)
            {
                String szRaw = (String) raw;
                int indx = szRaw.indexOf(GlobalIds.PROP_SEP);
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
        if (fractionValues != null)
        {
            for (Object raw : fractionValues)
            {
                String szRaw = (String) raw;
                int indx = szRaw.indexOf(GlobalIds.PROP_SEP);
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
        if (fractionValues != null)
        {
            for (Object raw : fractionValues)
            {
                String szRaw = (String) raw;
                int indx = szRaw.indexOf(GlobalIds.PROP_SEP);
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
        if (fractionValues != null)
        {
            for (Object raw : fractionValues)
            {
                String szRaw = (String) raw;
                user.setRole(szRaw);
            }
        }
    }

    private void updateRoleAssigns(Permission perm)
    {
        Vector fractionValues = parser.getOptionValues(roleAssigns);
        if (fractionValues != null)
        {
            for (Object raw : fractionValues)
            {
                String szRaw = (String) raw;
                perm.setRole(szRaw);
            }
        }
    }

    private void updateAdminRoleAssigns(User user)
    {
        Vector fractionValues = parser.getOptionValues(adminRoleAssigns);
        if (fractionValues != null)
        {
            for (Object raw : fractionValues)
            {
                String szRaw = (String) raw;
                user.setAdminRole(szRaw);
            }
        }
    }

    private void updateRoleAssigns(SDSet sdSet)
    {
        Vector fractionValues = parser.getOptionValues(roleAssigns);
        if (fractionValues != null)
        {
            for (Object raw : fractionValues)
            {
                String szRaw = (String) raw;
                sdSet.addMember(szRaw);
            }
        }
    }

    private void updateOsPs(AdminRole role)
    {
        Vector fractionValues = parser.getOptionValues(osPs);
        if (fractionValues != null)
        {
            for (Object raw : fractionValues)
            {
                String szRaw = (String) raw;
                role.setOsP(szRaw);
            }
        }
    }

    private void updateOsUs(AdminRole role)
    {
        Vector fractionValues = parser.getOptionValues(osPs);
        if (fractionValues != null)
        {
            for (Object raw : fractionValues)
            {
                String szRaw = (String) raw;
                role.setOsU(szRaw);
            }
        }
    }

    private void updatePhones(User user)
    {
        Vector fractionValues = parser.getOptionValues(phones);
        if (fractionValues != null)
        {
            for (Object val : fractionValues)
            {
                String number = (String) val;
                user.setPhone(number);
            }
        }
    }

    private void updateMobiles(User user)
    {
        Vector fractionValues = parser.getOptionValues(mobiles);
        if (fractionValues != null)
        {
            for (Object val : fractionValues)
            {
                String number = (String) val;
                user.setMobile(number);
            }
        }
    }

    private void updateEmails(User user)
    {
        Vector fractionValues = parser.getOptionValues(emails);
        if (fractionValues != null)
        {
            for (Object val : fractionValues)
            {
                String email = (String) val;
                user.setPhone(email);
            }
        }
    }

    private void updateAddress(User user)
    {
        Address uAddr = user.getAddress();
        Vector fractionValues = parser.getOptionValues(address);
        if (fractionValues != null)
        {
            for (Object val : fractionValues)
            {
                String szAddress = (String) val;
                uAddr.setAddress(szAddress);
            }
        }
        uAddr.setCity(getCity());
        uAddr.setState(getState());
        uAddr.setPostalCode(getPostalCode());
        uAddr.setPostOfficeBox(getPostOfficeBox());
    }

    String getState()
    {
        return (String) parser.getOptionValue(state);
    }

    String getCity()
    {
        return (String) parser.getOptionValue(city);
    }

    String getPostalCode()
    {
        return (String) parser.getOptionValue(postalCode);
    }

    String getPostOfficeBox()
    {
        return (String) parser.getOptionValue(postalOfficeBox);
    }

    String getOu()
    {
        return (String) parser.getOptionValue(ou);
    }

    String getPwPolicy()
    {
        return (String) parser.getOptionValue(pwPolicy);
    }

    String getCn()
    {
        return (String) parser.getOptionValue(cn);
    }

    String getSn()
    {
        return (String) parser.getOptionValue(sn);
    }

    String getDescription()
    {
        return (String) parser.getOptionValue(description);
    }

    String getTitle()
    {
        return (String) parser.getOptionValue(title);
    }

    String getEmployeeType()
    {
        return (String) parser.getOptionValue(employeeType);
    }

    String getBeginTime()
    {
        return (String) parser.getOptionValue(beginTime);
    }

    String getEndTime()
    {
        return (String) parser.getOptionValue(endTime);
    }

    String getBeginDate()
    {
        return (String) parser.getOptionValue(beginDate);
    }

    String getEndDate()
    {
        return (String) parser.getOptionValue(endDate);
    }

    String getBeginLockDate()
    {
        return (String) parser.getOptionValue(beginLockDate);
    }

    String getEndLockDate()
    {
        return (String) parser.getOptionValue(endLockDate);
    }

    String getDayMask()
    {
        return (String) parser.getOptionValue(dayMask);
    }

    String getRoleNm()
    {
        return (String) parser.getOptionValue(role);
    }

    String getName()
    {
        return (String) parser.getOptionValue(name);
    }

    String getTimeout()
    {
        return (String) parser.getOptionValue(timeout);
    }

    String getType()
    {
        return (String) parser.getOptionValue(type);
    }

    String getOpName()
    {
        return (String) parser.getOptionValue(opName);
    }

    String getAscendant()
    {
        return (String) parser.getOptionValue(ascendant);
    }

    String getDescendant()
    {
        return (String) parser.getOptionValue(descendant);
    }

    String getCardinality()
    {
        return (String) parser.getOptionValue(cardinality);
    }

    String getBeginRange()
    {
        return (String) parser.getOptionValue(beginRange);
    }

    String getEndRange()
    {
        return (String) parser.getOptionValue(endRange);
    }

    String getBeginInclusive()
    {
        return (String) parser.getOptionValue(beginInclusive);
    }

    String getEndInclusive()
    {
        return (String) parser.getOptionValue(endInclusive);
    }
}
