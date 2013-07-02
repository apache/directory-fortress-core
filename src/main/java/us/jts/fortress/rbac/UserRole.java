/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;


import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import us.jts.fortress.GlobalIds;
import us.jts.fortress.util.time.CUtil;
import us.jts.fortress.util.time.Constraint;


/**
 * The Fortress UserRole entity is used to store an RBAC User to Role assignment along with its temporal constraint values.
 * The contents of the UserRole entity will be stored on the User entity in the 'ftRA' (Role name) and 'ftRC' (Temporal Constraints) attributes on the 'ftUserAttrs' object class.
 * The UserRole entity carries elements of {@link us.jts.fortress.util.time.Constraint}.  Any attributes of Constraint not set within this entity
 * will use same attribute from the {@link us.jts.fortress.rbac.Role} entity.  Thus the UserRole can override Constraint attributes from it's corresponding Role if required by caller.
 * <p/>
 * <h4>UserRole Schema</h4>
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
@XmlRootElement(name = "fortUserRole")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userRole", propOrder =
    {
        "name",
        "userId",
        "parents",
        "beginDate",
        "beginLockDate",
        "beginTime",
        "dayMask",
        "endDate",
        "endLockDate",
        "endTime",
        "timeout"
})
@XmlSeeAlso(
    {
        UserAdminRole.class
})
public class UserRole extends FortEntity implements java.io.Serializable, Constraint
{
protected String userId;
protected String name;
private Integer timeout;
private String beginTime;
private String endTime;
private String beginDate;
private String endDate;
private String beginLockDate;
private String endLockDate;
private String dayMask;
@XmlElement(nillable = true)
private Set<String> parents;


/**
 * Default constructor is used by internal Fortress classes.
 */
public UserRole()
{
}


/**
 * Construct a UserRole entity given the required attributes 'userId' and 'role' name.
 *
 * @param userId maps to the 'uid' attribute on the 'inetOrgPerson' object class.
 * @param role   maps to the 'ftRA' attribute on the 'ftUserAttrs' object class.
 */
public UserRole( String userId, String role )
{
    this.userId = userId;
    name = role;

}


/**
 * Construct an RBAC Role with required attribute 'userId' and optional temporal constraint.
 *
 * @param userId maps to the 'uid' attribute on the 'inetOrgPerson' object class.
 * @param con    maps to 'ftRC' attribute in 'ftUserAttrs' object class.
 */
public UserRole( String userId, Constraint con )
{
    this.userId = userId;
    CUtil.copy( con, this );
}


/**
 * Construct a UserRole entity given the required attribute role' name.
 *
 * @param role maps to the 'ftRA' attribute on the 'ftUserAttrs' object class.
 */
public UserRole( String role )
{
    name = role;
}


/**
 * This method loads UserRole entity temporal constraint instance variables with data that was retrieved from the
 * 'ftRC' attribute on the 'ftUserAttrs' object class.  This is the raw format that Fortress uses to condense the temporal data into
 * a compact String for efficient storage and retrieval and is not intended to be called by external programs.
 *
 * @param szRawData contains a raw formatted String that maps to 'ftRC' attribute on 'ftUserAttrs' object class
 */
public void load( String szRawData, String contextId )
{
    if ( ( szRawData != null ) && ( szRawData.length() > 0 ) )
    {
        StringTokenizer tkn = new StringTokenizer( szRawData, GlobalIds.DELIMITER );

        if ( tkn.countTokens() > 0 )
        {
            int count = tkn.countTokens();

            for ( int i = 0; i < count; i++ )
            {
                switch ( i )
                {
                    case 0:
                        name = tkn.nextToken();
                        parents = RoleUtil.getParents( name.toUpperCase(), contextId );
                        break;

                    case 1:
                        timeout = Integer.parseInt( tkn.nextToken() );
                        break;

                    case 2:
                        beginTime = tkn.nextToken();
                        break;

                    case 3:
                        endTime = tkn.nextToken();
                        break;

                    case 4:
                        beginDate = tkn.nextToken();
                        break;

                    case 5:
                        endDate = tkn.nextToken();
                        break;

                    case 6:
                        beginLockDate = tkn.nextToken();
                        break;

                    case 7:
                        endLockDate = tkn.nextToken();
                        break;

                    case 8:
                        dayMask = tkn.nextToken();
                        break;
                }
            }
        }
    }
}


/**
 * Required on DAO classes convert Temporal attributes stored on entity to raw data object format needed for ldap.  For internal use only.
 *
 * @return String that maps to 'ftRA' attribute on the 'ftUserAttrs' object class.
 */
@Override
public String getRawData()
{
    StringBuilder sb = new StringBuilder();

    sb.append( name );
    sb.append( GlobalIds.DELIMITER );
    sb.append( timeout );
    sb.append( GlobalIds.DELIMITER );

    if ( beginTime != null )
    {
        sb.append( beginTime );
    }

    sb.append( GlobalIds.DELIMITER );

    if ( endTime != null )
    {
        sb.append( endTime );
    }

    sb.append( GlobalIds.DELIMITER );

    if ( beginDate != null )
    {
        sb.append( beginDate );
    }

    sb.append( GlobalIds.DELIMITER );

    if ( endDate != null )
    {
        sb.append( endDate );
    }

    sb.append( GlobalIds.DELIMITER );

    if ( beginLockDate != null )
    {
        sb.append( beginLockDate );
    }

    sb.append( GlobalIds.DELIMITER );

    if ( endLockDate != null )
    {
        sb.append( endLockDate );
    }

    sb.append( GlobalIds.DELIMITER );

    if ( dayMask != null )
    {
        sb.append( dayMask );
    }

    return sb.toString();
}


/**
 * Used to retrieve UserRole Role name attribute.  The Fortress UserRole name maps to 'ftRA' attribute on 'ftUserAttrs' object class.
 */
@Override
public String toString()
{
    return name;
}


/**
 * Return the userId that is associated with UserRole.  UserId is required attribute and must be set on all UserRole assignment operations.
 *
 * @return attribute maps to 'uid' in 'inetOrgPerson' object class.
 */
public String getUserId()
{
    return userId;
}


/**
 * Set the userId that is associated with UserRole.  UserId is required attribute and must be set on all UserRole assignment operations.
 *
 * @param userId maps to 'uid' in 'inetOrgPerson' object class.
 */
public void setUserId( String userId )
{
    this.userId = userId;
}


/**
 * Get the Role name required attribute of the UserRole object
 *
 * @param name maps to 'ftRC' and 'ftRA' attributes on 'ftUserAttrs' object class.
 */
@Override
public void setName( String name )
{
    this.name = name;
}


/**
 * Set the Role name required attribute of the UserRole object
 *
 * @return attribute maps to 'ftRC' and 'ftRA' attributes on 'ftUserAttrs' object class.
 */
@Override
public String getName()
{
    return name;
}


/**
 * temporal boolean flag is used by internal Fortress components.
 *
 * @return boolean indicating if temporal constraints are placed on UserRole.
 */
@Override
public boolean isTemporalSet()
{
    return ( beginTime != null || endTime != null || beginDate != null || endDate != null || beginLockDate != null
        || endLockDate != null || dayMask != null );
}


/**
 * Set the integer timeout that contains max time (in seconds) that entity may remain inactive.
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @param timeout maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public void setTimeout( Integer timeout )
{
    this.timeout = timeout;
}


/**
 * Set the begin time of day entity is allowed to be activated in system.  The format is military time - HHMM, i.e. 0800 (8:00 am) or 1700 (5:00 p.m.).
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @param beginTime maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public void setBeginTime( String beginTime )
{
    this.beginTime = beginTime;
}


/**
 * Set the end time of day entity is allowed to be activated in system.  The format is military time - HHMM, i.e. 0000 (12:00 am) or 2359 (11:59 p.m.).
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @param endTime maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public void setEndTime( String endTime )
{
    this.endTime = endTime;
}


/**
 * Set the beginDate when entity is allowed to be activated in system.  The format is - YYYYMMDD, i.e. 20100101 (January 1, 2001).
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @param beginDate maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public void setBeginDate( String beginDate )
{
    this.beginDate = beginDate;
}


/**
 * Set the end date when entity is not allowed to be activated in system.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @param endDate maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public void setEndDate( String endDate )
{
    this.endDate = endDate;
}


/**
 * Set the daymask that specifies what days of week entity is allowed to be activated in system.  The format is 1234567, i.e. 23456 (Monday, Tuesday, Wednesday, Thursday, Friday).
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @param dayMask maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public void setDayMask( String dayMask )
{
    this.dayMask = dayMask;
}


/**
 * Set the begin lock date when entity is temporarily not allowed to be activated in system.  The format is - YYYYMMDD, 20100101 (January 1. 2010).
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @param beginLockDate maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public void setBeginLockDate( String beginLockDate )
{
    this.beginLockDate = beginLockDate;
}


/**
 * Set the end lock date when entity is allowed to be activated in system once again.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @param endLockDate maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public void setEndLockDate( String endLockDate )
{
    this.endLockDate = endLockDate;
}


/**
 * Return the integer timeout that contains total time (in seconds) that entity may remain inactive.
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @return int that maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public Integer getTimeout()
{
    return timeout;
}


/**
 * Contains the begin time of day entity is allowed to be activated in system.  The format is military time - HHMM, i.e. 0800 (8:00 am) or 1700 (5:00 p.m.).
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @return String that maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public String getBeginTime()
{
    return beginTime;
}


/**
 * Contains the end time of day entity is allowed to be activated in system.  The format is military time - HHMM, i.e. 0000 (12:00 am) or 2359 (11:59 p.m.).
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @return String that maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public String getEndTime()
{
    return endTime;
}


/**
 * Contains the begin date when entity is allowed to be activated in system.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @return String that maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public String getBeginDate()
{
    return beginDate;
}


/**
 * Contains the end date when entity is allowed to be activated in system.  The format is - YYYYMMDD, i.e. 20101231 (December 31, 2011).
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @return String that maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public String getEndDate()
{
    return endDate;
}


/**
 * Contains the begin lock date when entity is temporarily not allowed to activated in system.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @return String that maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public String getBeginLockDate()
{
    return beginLockDate;
}


/**
 * Contains the end lock date when entity is allowed to be activated in system once again.  The format is - YYYYMMDD, i.e. 20100101 (January 1. 2010).
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @return String that maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public String getEndLockDate()
{
    return endLockDate;
}


/**
 * Get the daymask that indicates what days of week entity is allowed to be activated in system.  The format is 1234567, i.e. 23456 (Monday, Tuesday, Wednesday, Thursday, Friday).
 * This attribute is optional but if set will be validated for reasonableness.
 *
 * @return String that maps to 'ftRC', attribute on 'ftUserAttrs' object class.
 */
@Override
public String getDayMask()
{
    return dayMask;
}


/**
 * Get the names of roles that are parents (direct ascendants) of this role.
 * @return Set of parent role names assigned to this role.
 */
public Set<String> getParents()
{
    return parents;
}


/**
 * Set the names of roles names that are parents (direct ascendants) of this role.
 * @param parents contains the Set of parent role names assigned to this role.
 */
public void setParents( Set<String> parents )
{
    this.parents = parents;
}


/**
 * Matches the userId and role name from two UserRole entities.
 *
 * @param thatObj contains a UserRole entity.
 * @return boolean indicating both objects contain matching userId and Role names.
 */
public boolean equals( Object thatObj )
{
    if ( this == thatObj )
    {
        return true;
    }

    if ( name == null )
    {
        return false;
    }

    if ( !( thatObj instanceof UserRole ) )
    {
        return false;
    }

    UserRole thatRole = ( UserRole ) thatObj;

    if ( thatRole.getName() == null )
    {
        return false;
    }

    return ( thatRole.getName().equalsIgnoreCase( name ) );
}
}
