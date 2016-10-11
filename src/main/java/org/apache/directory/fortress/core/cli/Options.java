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
package org.apache.directory.fortress.core.cli;

import java.util.Vector;

import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.model.Address;
import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.Constraint;
import org.apache.directory.fortress.core.model.Group;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Relationship;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.SDSet;
import org.apache.directory.fortress.core.model.User;

/**
 * This converts between Fortress entities and the JArg Options.  It contains attributes passed from JArgs command interpreter.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Options implements java.io.Serializable
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;

    private final CmdLineParser parser;
    private final CmdLineParser.Option userId;
    private final CmdLineParser.Option password;
    private final CmdLineParser.Option newPassword;
    private final CmdLineParser.Option ou;
    private final CmdLineParser.Option pwPolicy;
    private final CmdLineParser.Option cn;
    private final CmdLineParser.Option sn;
    private final CmdLineParser.Option description;
    private final CmdLineParser.Option title;
    private final CmdLineParser.Option employeeType;
    private final CmdLineParser.Option beginTime;
    private final CmdLineParser.Option endTime;
    private final CmdLineParser.Option beginDate;
    private final CmdLineParser.Option endDate;
    private final CmdLineParser.Option beginLockDate;
    private final CmdLineParser.Option endLockDate;
    private final CmdLineParser.Option dayMask;
    private final CmdLineParser.Option name;
    private final CmdLineParser.Option timeout;
    private final CmdLineParser.Option properties;
    private final CmdLineParser.Option roleAssigns;
    private final CmdLineParser.Option role;
    private final CmdLineParser.Option adminRoleAssigns;
    private final CmdLineParser.Option type;
    private final CmdLineParser.Option opName;
    private final CmdLineParser.Option ascendant;
    private final CmdLineParser.Option descendant;
    private final CmdLineParser.Option cardinality;
    private final CmdLineParser.Option osPs;
    private final CmdLineParser.Option osUs;
    private final CmdLineParser.Option beginRange;
    private final CmdLineParser.Option endRange;
    private final CmdLineParser.Option beginInclusive;
    private final CmdLineParser.Option endInclusive;
    private final CmdLineParser.Option phones;
    private final CmdLineParser.Option mobiles;
    private final CmdLineParser.Option emails;
    private final CmdLineParser.Option address;
    private final CmdLineParser.Option state;
    private final CmdLineParser.Option city;
    private final CmdLineParser.Option postalCode;
    private final CmdLineParser.Option postalOfficeBox;
    private final CmdLineParser.Option protocol;
    private final CmdLineParser.Option member;

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
        this.protocol = parser.addStringOption('X', "protocol");
        this.member = parser.addStringOption('M', "member");
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
            Integer cardinality = Integer.valueOf( getCardinality() );
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
    public Group getGroup()
    {
        Group group = new Group();
        group.setName( getName() );
        group.setDescription( getDescription() );
        group.setProtocol( getProtocol() );
        updateAssigns(group);
        updateProperties(group);
        return group;
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
        permObj.setObjName( getName() );
        permObj.setDescription(getDescription());
        permObj.setOu(getOu());
        permObj.setType(getType());
        updateProperties(permObj);
        return permObj;
    }

    public Permission getPermission()
    {
        Permission perm = new Permission();
        perm.setObjName( getName() );
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
            Integer to = Integer.valueOf( getTimeout() );
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

    public String getPassword()
    {
        return (String) parser.getOptionValue(password);
    }

    public String getNewPassword()
    {
        return (String) parser.getOptionValue(newPassword);
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

    private void updateProperties(Group group)
    {
        Vector fractionValues = parser.getOptionValues(properties);
        if (fractionValues != null)
        {
            for (Object raw : fractionValues)
            {
                String szRaw = (String) raw;
                int indx = szRaw.indexOf('=');
                if (indx >= 1)
                {
                    group.addProperty(szRaw.substring(0, indx), szRaw.substring(indx + 1));
                }
            }
        }
    }

    private void updateAssigns(Group group)
    {
        Vector fractionValues = parser.getOptionValues(member);
        if (fractionValues != null)
        {
            for (Object raw : fractionValues)
            {
                String szRaw = (String) raw;
                group.setMember( szRaw );
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
                user.setRoleName(szRaw);
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
                user.setAdminRoleName(szRaw);
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

    String getProtocol()
    {
        return (String) parser.getOptionValue(protocol);
    }

    String getMember()
    {
        return (String) parser.getOptionValue(member);
    }
}
