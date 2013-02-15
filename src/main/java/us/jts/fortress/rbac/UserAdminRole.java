/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import us.jts.fortress.GlobalIds;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * The UserAdminRole entity extends the UserRole and is used to store ARBAC User to AdminRole assignment along with temporal and
 * ARBAC contraint values.
 * The contents of the UserAdminRole entity will be stored on the User entity in the 'ftARA' (AdminRole name) and 'ftARC' (Temporal and ARBAC Constraints) attributes on the 'ftUserAttrs' object class.
 * The UserAdminRole entity carries elements of {@link us.jts.fortress.util.time.Constraint}.  Any attributes of Constraint not set within this entity
 * will use same attribute from the {@link AdminRole} entity.  Thus the UserAdminRole can override Constraint attributes from it's corresponding AdminRole if required by caller.
 * <p/>
 * <h4>UserAdminRole Schema</h4>
 * ftUserAttrs is used to store RBAC and ARBAC Role role assignments and other security attributes on User entity.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.1</code>
 * <li> <code>NAME 'ftUserAttrs'</code>
 * <li> <code>DESC 'Fortress User Attribute AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MUST ( ftId )</code>
 * <li> <code>MAY ( ftRC $ ftRA $ ftARC $ ftARA $ ftCstr</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 *
 * @author Shawn McKinney
 */
/*
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userAdminRole", propOrder = {
    "beginInclusive",
    "beginRange",
    "endInclusive",
    "endRange",
    "osP",
    "osU",
    "roleRangeRaw"
})
*/
@XmlRootElement(name = "fortUserAdminRole")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userAdminRole", propOrder = {
    "osPs",
    "osUs",
    "beginInclusive",
    "beginRange",
    "endInclusive",
    "endRange",
    "parents"
})
public class UserAdminRole extends UserRole implements Administrator
{
    @XmlElement(nillable = true)
    private Set<String> osPs;
    @XmlElement(nillable = true)
    private Set<String> osUs;
    private String beginRange;
    private String endRange;
    private boolean beginInclusive;
    private boolean endInclusive;
    @XmlElement(nillable = true)
    private Set<String> parents;

    /**
     * Default constructor is used by internal Fortress classes.
     */
    public UserAdminRole()
    {
    }

    /**
     * Construct a UserRole entity given the required attributes 'userId' and 'role' name.
     *
     * @param userId maps to the 'uid' attribute on the 'inetOrgPerson' object class.
     * @param name   maps to the 'ftARA' attribute on the 'ftUserAttrs' object class.
     */
    public UserAdminRole(String userId, String name)
    {
        this.userId = userId;
        this.name = name;
    }

    /**
     * Construct an ARBAC Role with required attribute 'userId' and optional temporal constraint.
     *
     * @param userId maps to the 'uid' attribute on the 'inetOrgPerson' object class.
     * @param con    maps to 'ftARC' attribute in 'ftUserAttrs' object class.
     */
    public UserAdminRole(String userId, us.jts.fortress.util.time.Constraint con)
    {
        this.userId = userId;
        us.jts.fortress.util.time.CUtil.copy(con, this);
    }

    /**
     * This method loads UserAdminRole entity temporal and ARBAC constraint instance variables with data that was retrieved from the
     * 'ftARC' attribute on the 'ftUserAttrs' object class.  This is the raw format that Fortress uses to condense the temporal and ARBAC data into
     * a compact String for efficient storage and retrieval and is not intended to be called by external programs.
     *
     * @param szRawData contains a raw formatted String that maps to 'ftARC' attribute on 'ftUserAttrs' object class
     */
    public void load(String szRawData, String contextId)
    {
        if (szRawData != null && szRawData.length() > 0)
        {
            StringTokenizer tkn = new StringTokenizer(szRawData, GlobalIds.COMMA);
            if (tkn.countTokens() > 0)
            {
                int count = tkn.countTokens();
                for (int i = 0; i < count; i++)
                {
                    switch (i)
                    {
                        case 0:
                            this.setName(tkn.nextToken());
                            this.setParents(AdminRoleUtil.getParents(this.name.toUpperCase(), contextId));
                            break;
                        case 1:
                            this.setTimeout(Integer.parseInt(tkn.nextToken()));
                            break;
                        case 2:
                            this.setBeginTime(tkn.nextToken());
                            break;
                        case 3:
                            this.setEndTime(tkn.nextToken());
                            break;
                        case 4:
                            this.setBeginDate(tkn.nextToken());
                            break;
                        case 5:
                            this.setEndDate(tkn.nextToken());
                            break;
                        case 6:
                            this.setBeginLockDate(tkn.nextToken());
                            break;
                        case 7:
                            this.setEndLockDate(tkn.nextToken());
                            break;
                        case 8:
                            this.setDayMask(tkn.nextToken());
                            break;
                        default:
                            String szValue = tkn.nextToken();
                            int indx = szValue.indexOf("P:");
                            if (indx >= 0)
                            {
                                String szOsP = szValue.substring(indx + 2);
                                this.setOsP(szOsP);
                            }
                            indx = szValue.indexOf("U:");
                            if (indx >= 0)
                            {
                                String szOsU = szValue.substring(indx + 2);
                                this.setOsU(szOsU);
                            }
                            indx = szValue.indexOf("R:");
                            if (indx >= 0)
                            {
                                String szRangeRaw = szValue.substring(indx + 2);
                                this.setRoleRangeRaw(szRangeRaw);
                            }
                            break;
                    }
                }
            }
        }
    }


    /**
     * This method creates raw data format that represents UserAdminRole temporal and ARBAC constraints using instance variables inside entity.
     * The raw data is eventually stored in the 'ftARC' attribute on the 'ftUserAttrs' object class.
     * This is the raw format that Fortress uses to condense the temporal and ARBAC data into a compact String for efficient storage and retrieval
     * and is not intended to be called by external programs.
     *
     * @return String contains a raw formatted String that maps to 'ftARC' attribute on 'ftUserAttrs' object class
     */
    @Override
    public String getRawData()
    {
        String szRole;
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName());
        sb.append(GlobalIds.COMMA);
        sb.append(this.getTimeout());
        sb.append(GlobalIds.COMMA);
        if (this.getBeginTime() != null)
            sb.append(this.getBeginTime());
        sb.append(GlobalIds.COMMA);
        if (this.getEndTime() != null)
            sb.append(this.getEndTime());
        sb.append(GlobalIds.COMMA);
        if (this.getBeginDate() != null)
            sb.append(this.getBeginDate());
        sb.append(GlobalIds.COMMA);
        if (this.getEndDate() != null)
            sb.append(this.getEndDate());
        sb.append(GlobalIds.COMMA);
        if (this.getBeginLockDate() != null)
            sb.append(this.getBeginLockDate());
        sb.append(GlobalIds.COMMA);
        if (this.getEndLockDate() != null)
            sb.append(this.getEndLockDate());
        sb.append(GlobalIds.COMMA);
        if (this.getDayMask() != null)
            sb.append(this.getDayMask());
        if (this.getOsU() != null)
        {
            for (String org : this.getOsU())
            {
                sb.append(",U:");
                sb.append(org);
            }
        }
        if (this.getOsP() != null)
        {
            for (String org : this.getOsP())
            {
                sb.append(",P:");
                sb.append(org);
            }
        }
        if (this.getRoleRangeRaw() != null)
        {
            sb.append(",R:");
            sb.append(this.getRoleRangeRaw());
        }

        szRole = sb.toString();
        return szRole;
    }

    /**
     * This method loads UserAdminRole entity Role range ARBAC constraint instance variables with data that was retrieved from the
     * 'ftARC' attribute on the 'ftUserAttrs' object class.  This is the raw format that Fortress uses to condense the ARBAC data into
     * a compact String for efficient storage and retrieval and is not intended to be called by external programs.
     *
     * @param szRaw contains a raw formatted String that maps to 'ftARC' attribute on 'ftUserAttrs' object class
     */
    @Override
    public void setRoleRangeRaw(String szRaw)
    {
        if (us.jts.fortress.util.attr.VUtil.isNotNullOrEmpty(szRaw))
        {
            int bindx = szRaw.indexOf("(");
            if (bindx > -1)
            {
                this.setBeginInclusive(false);
            }
            else
            {
                bindx = szRaw.indexOf("[");
                this.setBeginInclusive(true);
            }
            int eindx = szRaw.indexOf(")");
            if (eindx > -1)
            {
                this.setEndInclusive(false);
            }
            else
            {
                eindx = szRaw.indexOf("]");
                this.setEndInclusive(true);
            }
            int cindx = szRaw.indexOf(":");
            if (cindx > -1)
            {
                String szBeginRange = szRaw.substring(bindx + 1, cindx);
                String szEndRange = szRaw.substring(cindx + 1, eindx);
                this.setBeginRange(szBeginRange);
                this.setEndRange(szEndRange);
            }
        }
    }

    /**
     * This method retrieves UserAdminRole instance variables and formats into raw data for ARBAC constraint storage for the
     * 'ftARC' attribute on the 'ftUserAttrs' object class.  This is the raw format that Fortress uses to condense the ARBAC data into
     * a compact String for efficient storage and retrieval and is not intended to be called by external programs.
     *
     * @return String contains a raw formatted String that maps to 'ftARC' attribute on 'ftUserAttrs' object class
     */
    @Override
    public String getRoleRangeRaw()
    {
        String szRaw = "";
        if (this.beginRange != null)
        {
            if (this.isBeginInclusive())
                szRaw += "[";
            else
                szRaw += "(";
            szRaw += this.getBeginRange();
            szRaw += ":";
            szRaw += this.getEndRange();
            if (this.isEndInclusive())
                szRaw += "]";
            else
                szRaw += ")";
        }
        return szRaw;
    }

    /**
     * Get a collection of optional Perm OU attributes that were stored on the AdminRole entity.
     *
     * @return List of type String containing Perm OU.  This maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public Set<String> getOsP()
    {
        return osPs;
    }

    /**
     * Set a collection of optional Perm OU attributes to be stored on the AdminRole entity.
     *
     * @param osPs is a List of type String containing Perm OU.  This maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public void setOsP(Set<String> osPs)
    {
        this.osPs = osPs;
    }

    /**
     * Set a Perm OU attribute to be stored on the AdminRole entity.
     *
     * @param osP is a Perm OU that maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public void setOsP(String osP)
    {
        if (this.osPs == null)
        {
            // create Set with case insensitive comparator:
            osPs = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        }
        osPs.add(osP);
    }

    /**
     * Get a collection of optional User OU attributes that were stored on the AdminRole entity.
     *
     * @return List of type String containing User OU.  This maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public Set<String> getOsU()
    {
        return osUs;
    }

    /**
     * Set a collection of optional User OU attributes to be stored on the AdminRole entity.
     *
     * @param osUs is a List of type String containing User OU.  This maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public void setOsU(Set<String> osUs)
    {
        this.osUs = osUs;
    }

    /**
     * Set a User OU attribute to be stored on the AdminRole entity.
     *
     * @param osU is a User OU that maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public void setOsU(String osU)
    {
        if (this.osUs == null)
        {
            // create Set with case insensitive comparator:
            osUs = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        }
        osUs.add(osU);
    }

    /**
     * Return the begin Role range attribute for AdminRole entity.
     *
     * @return String that maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public String getBeginRange()
    {
        return beginRange;
    }

    /**
     * Set the begin Role range attribute for AdminRole entity.
     *
     * @param beginRange maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public void setBeginRange(String beginRange)
    {
        this.beginRange = beginRange;
    }

    /**
     * Return the end Role range attribute for AdminRole entity.
     *
     * @return String that maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public String getEndRange()
    {
        return endRange;
    }

    /**
     * Set the end Role range attribute for AdminRole entity.
     *
     * @param endRange maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public void setEndRange(String endRange)
    {
        this.endRange = endRange;
    }

    /**
     * Set the begin inclusive which specifies if role range includes or excludes the 'beginRange' attribute.
     *
     * @return String that maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public boolean isBeginInclusive()
    {
        return beginInclusive;
    }

    /**
     * Get the begin inclusive which specifies if role range includes or excludes the 'beginRange' attribute.
     *
     * @param beginInclusive maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public void setBeginInclusive(boolean beginInclusive)
    {
        this.beginInclusive = beginInclusive;
    }

    /**
     * Set the end inclusive which specifies if role range includes or excludes the 'endRange' attribute.
     *
     * @return String that maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public boolean isEndInclusive()
    {
        return endInclusive;
    }

    /**
     * Get the end inclusive which specifies if role range includes or excludes the 'endRange' attribute.
     *
     * @param endInclusive maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public void setEndInclusive(boolean endInclusive)
    {
        this.endInclusive = endInclusive;
    }

    /**
     * Get the names of admin roles that are parents (direct ascendants) of this admin role.
     * @return Set of parent admin role names assigned to this admin role.
     */
    @Override
    public Set<String> getParents()
    {
        return parents;
    }

    /**
     * Set the names of parent admin roles.
     * @param parents Set of admin role names.
     */
    @Override
    public void setParents(Set<String> parents)
    {
        this.parents = parents;
    }

    /**
     * Matches the userId and admin role name from two UserAdminRole entities.
     *
     * @param thatObj contains a UserAdminRole entity.
     * @return boolean indicating both objects contain matching userId and Admin Role names.
     */
    public boolean equals(Object thatObj)
    {
        if (this == thatObj) return true;
        if (this.getName() == null) return false;
        if (!(thatObj instanceof UserAdminRole)) return false;
        UserAdminRole thatRole = (UserAdminRole) thatObj;
        if (thatRole.getName() == null) return false;
        return ((thatRole.getName().equalsIgnoreCase(this.getName())) && (thatRole.getUserId().equalsIgnoreCase(this.getUserId())));
    }
}