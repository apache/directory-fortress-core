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


import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.util.Config;


/**
 * The UserAdminRole entity extends the UserRole and is used to store ARBAC User to AdminRole assignment along with temporal and
 * ARBAC contraint values.
 * The contents of the UserAdminRole entity will be stored on the User entity in the 'ftARA' (AdminRole name) and 'ftARC' (Temporal and ARBAC Constraints) attributes on the 'ftUserAttrs' object class.
 * The UserAdminRole entity carries elements of {@link Constraint}.  Any attributes of Constraint not set within this entity
 * will use same attribute from the {@link AdminRole} entity.  Thus the UserAdminRole can override Constraint attributes from it's corresponding AdminRole if required by caller.
 * <p>
 * <h4>UserAdminRole Schema</h4>
 * ftUserAttrs is used to store RBAC and ARBAC Role role assignments and other security attributes on User entity.
 * <pre>
 * ------------------------------------------
 * Fortress User Attributes Auxiliary Object Class
 * objectclass ( 1.3.6.1.4.1.38088.3.1
 *  NAME 'ftUserAttrs'
 *  DESC 'Fortress User Attribute AUX Object Class'
 *  AUXILIARY
 *  MUST (
 *      ftId
 *  )
 *  MAY (
 *      ftRC $
 *      ftRA $
 *      ftARC $
 *      ftARA $
 *      ftCstr $
 *      ftSystem
 *  )
 * )
 * ------------------------------------------
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "fortUserAdminRole")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userAdminRole", propOrder =
    {
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
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;
    private Set<String> osPs;
    private Set<String> osUs;
    private String beginRange;
    private String endRange;
    private boolean beginInclusive;
    private boolean endInclusive;
    private Set<String> parents;

    // Used for formatting raw data:
    private static final String P = "P";
    private static final String U = "U";
    private static final String R = "R";
    private static final String LEFT_PAREN = "(";
    private static final String RIGHT_PAREN = ")";
    private static final String LEFT_BRACKET = "[";
    private static final String RIGHT_BRACKET = "]";


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
    public UserAdminRole( String userId, String name )
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
    public UserAdminRole( String userId, Constraint con )
    {
        this.userId = userId;
        ConstraintUtil.copy( con, this );
    }


    /**
     * This method loads UserAdminRole entity temporal and ARBAC constraint instance variables with data that was retrieved from the
     * 'ftARC' attribute on the 'ftUserAttrs' object class.  This is the raw format that Fortress uses to condense the temporal and ARBAC data into
     * a compact String for efficient storage and retrieval and is not intended to be called by external programs.
     *
     * @param szRawData contains a raw formatted String that maps to 'ftARC' attribute on 'ftUserAttrs' object class
     * @param contextId contains the tenant id.
     * @param parentUtil provides method to getParents.
     */
    public void load( String szRawData, String contextId, ParentUtil parentUtil )
    {
        if ( ( szRawData != null ) && ( szRawData.length() > 0 ) )
        {
            String[] tokens = StringUtils.splitPreserveAllTokens( szRawData, Config.getInstance().getDelimiter() );
            for ( int i = 0; i < tokens.length; i++ )
            {
                if ( StringUtils.isNotEmpty( tokens[i] ) )
                {
                    switch ( i )
                    {
                        case 0:
                            name = tokens[i];
                            parents = parentUtil.getParentsCB( name.toUpperCase(), contextId );
                            break;

                        case 1:
                            this.setTimeout( Integer.parseInt( tokens[i] ) );
                            break;

                        case 2:
                            this.setBeginTime( tokens[i] );
                            break;

                        case 3:
                            this.setEndTime( tokens[i] );
                            break;

                        case 4:
                            this.setBeginDate( tokens[i] );
                            break;

                        case 5:
                            this.setEndDate( tokens[i] );
                            break;

                        case 6:
                            this.setBeginLockDate( tokens[i] );
                            break;

                        case 7:
                            this.setEndLockDate( tokens[i] );
                            break;

                        case 8:
                            this.setDayMask( tokens[i] );
                            break;

                        default:
                            String szValue = tokens[i];
                            int indx = szValue.indexOf( P + GlobalIds.PROP_SEP );
                            if ( indx >= 0 )
                            {
                                String szOsP = szValue.substring( indx + 2 );
                                this.setOsP( szOsP );
                            }
                            indx = szValue.indexOf( U + GlobalIds.PROP_SEP );
                            if ( indx >= 0 )
                            {
                                String szOsU = szValue.substring( indx + 2 );
                                this.setOsU( szOsU );
                            }
                            indx = szValue.indexOf( R + GlobalIds.PROP_SEP );
                            if ( indx >= 0 )
                            {
                                String szRangeRaw = szValue.substring( indx + 2 );
                                this.setRoleRangeRaw( szRangeRaw );
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
        String delimeter = Config.getInstance().getDelimiter();
        StringBuilder sb = new StringBuilder();
        sb.append( name );
        sb.append( delimeter );
        sb.append( this.getTimeout() );
        sb.append( delimeter );

        if ( this.getBeginTime() != null )
        {
            sb.append( this.getBeginTime() );
        }

        sb.append( delimeter );

        if ( this.getEndTime() != null )
        {
            sb.append( this.getEndTime() );
        }

        sb.append( delimeter );

        if ( this.getBeginDate() != null )
        {
            sb.append( this.getBeginDate() );
        }

        sb.append( delimeter );

        if ( this.getEndDate() != null )
        {
            sb.append( this.getEndDate() );
        }

        sb.append( delimeter );

        if ( this.getBeginLockDate() != null )
        {
            sb.append( this.getBeginLockDate() );

        }

        sb.append( delimeter );

        if ( this.getEndLockDate() != null )
        {
            sb.append( this.getEndLockDate() );
        }

        sb.append( delimeter );

        if ( this.getDayMask() != null )
        {
            sb.append( this.getDayMask() );
        }

        if ( this.getOsUSet() != null )
        {
            for ( String org : this.getOsUSet() )
            {
                sb.append( delimeter );
                sb.append( U );
                sb.append( GlobalIds.PROP_SEP );
                sb.append( org );
            }
        }

        if ( this.getOsPSet() != null )
        {
            for ( String org : this.getOsPSet() )
            {
                sb.append( delimeter );
                sb.append( P );
                sb.append( GlobalIds.PROP_SEP );
                sb.append( org );
            }
        }
        if ( StringUtils.isNotEmpty( this.getRoleRangeRaw() ) )
        {
            sb.append( delimeter );
            sb.append( R );
            sb.append( GlobalIds.PROP_SEP );
            sb.append( this.getRoleRangeRaw() );
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
    public void setRoleRangeRaw( String szRaw )
    {
        if ( StringUtils.isNotEmpty( szRaw ) )
        {
            int bindx = szRaw.indexOf( LEFT_PAREN );
            if ( bindx > -1 )
            {
                this.setBeginInclusive( false );
            }
            else
            {
                bindx = szRaw.indexOf( LEFT_BRACKET );
                this.setBeginInclusive( true );
            }
            int eindx = szRaw.indexOf( RIGHT_PAREN );
            if ( eindx > -1 )
            {
                this.setEndInclusive( false );
            }
            else
            {
                eindx = szRaw.indexOf( RIGHT_BRACKET );
                this.setEndInclusive( true );
            }
            int cindx = szRaw.indexOf( GlobalIds.PROP_SEP );
            if ( cindx > -1 )
            {
                String szBeginRange = szRaw.substring( bindx + 1, cindx );
                String szEndRange = szRaw.substring( cindx + 1, eindx );
                this.setBeginRange( szBeginRange );
                this.setEndRange( szEndRange );
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
        if ( this.beginRange != null )
        {
            if ( this.isBeginInclusive() )
                szRaw += LEFT_BRACKET;
            else
                szRaw += LEFT_PAREN;
            szRaw += this.getBeginRange();
            szRaw += GlobalIds.PROP_SEP;
            szRaw += this.getEndRange();
            if ( this.isEndInclusive() )
                szRaw += RIGHT_BRACKET;
            else
                szRaw += RIGHT_PAREN;
        }
        return szRaw;
    }


    /**
     * Get a collection of optional Perm OU attributes that were stored on the AdminRole entity.
     *
     * @return List of type String containing Perm OU.  This maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public Set<String> getOsPSet()
    {
        return osPs;
    }


    /**
     * Set a collection of optional Perm OU attributes to be stored on the AdminRole entity.
     *
     * @param osPs is a List of type String containing Perm OU.  This maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public void setOsPSet(Set<String> osPs)
    {
        this.osPs = osPs;
    }


    /**
     * Set a Perm OU attribute to be stored on the AdminRole entity.
     *
     * @param osP is a Perm OU that maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public void setOsP( String osP )
    {
        if ( this.osPs == null )
        {
            // create Set with case insensitive comparator:
            osPs = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        }
        osPs.add( osP );
    }


    /**
     * Get a collection of optional User OU attributes that were stored on the AdminRole entity.
     *
     * @return List of type String containing User OU.  This maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public Set<String> getOsUSet()
    {
        return osUs;
    }


    /**
     * Set a collection of optional User OU attributes to be stored on the AdminRole entity.
     *
     * @param osUs is a List of type String containing User OU.  This maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public void setOsUSet(Set<String> osUs)
    {
        this.osUs = osUs;
    }


    /**
     * Set a User OU attribute to be stored on the AdminRole entity.
     *
     * @param osU is a User OU that maps to 'ftARC' attribute on 'ftUserAttrs' aux object class.
     */
    @Override
    public void setOsU( String osU )
    {
        if ( this.osUs == null )
        {
            // create Set with case insensitive comparator:
            osUs = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        }
        osUs.add( osU );
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
    public void setBeginRange( String beginRange )
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
    public void setEndRange( String endRange )
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
    public void setBeginInclusive( boolean beginInclusive )
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
    public void setEndInclusive( boolean endInclusive )
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
    public void setParents( Set<String> parents )
    {
        this.parents = parents;
    }


    /**
     * Matches the userId and admin role name from two UserAdminRole entities.
     *
     * @param thatObj contains a UserAdminRole entity.
     * @return boolean indicating both objects contain matching userId and Admin Role names.
     */
    public boolean equals( Object thatObj )
    {
        if ( this == thatObj )
        {
            return true;
        }

        if ( this.getName() == null )
        {
            return false;
        }

        if ( !( thatObj instanceof UserAdminRole ) )
        {
            return false;
        }

        UserAdminRole thatRole = ( UserAdminRole ) thatObj;

        if ( thatRole.getName() == null )
        {
            return false;
        }

        return ( ( thatRole.getName().equalsIgnoreCase( this.getName() ) ) && ( thatRole.getUserId()
            .equalsIgnoreCase( this.getUserId() ) ) );
    }


    @Override
    public int hashCode()
    {
        int result = osPs != null ? osPs.hashCode() : 0;
        result = 31 * result + ( osUs != null ? osUs.hashCode() : 0 );
        result = 31 * result + ( beginRange != null ? beginRange.hashCode() : 0 );
        result = 31 * result + ( endRange != null ? endRange.hashCode() : 0 );
        result = 31 * result + ( beginInclusive ? 1 : 0 );
        result = 31 * result + ( endInclusive ? 1 : 0 );
        result = 31 * result + ( parents != null ? parents.hashCode() : 0 );
        return result;
    }
}
