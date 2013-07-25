/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;


import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import us.jts.fortress.rbac.dao.AdminRoleDAO;
import us.jts.fortress.rbac.dao.OrgUnitDAO;
import us.jts.fortress.util.attr.VUtil;
import us.jts.fortress.util.time.CUtil;
import us.jts.fortress.util.time.Constraint;


/**
 * All entities ({@link AdminRole}, {@link OrgUnit},
 * {@link us.jts.fortress.rbac.SDSet} etc...) are used to carry data between three Fortress
 * layers.starting with the (1) Manager layer down thru middle (2) Process layer and it's processing rules into
 * (3) DAO layer where persistence with the OpenLDAP server occurs.
 * <h4>Fortress Processing Layers</h4>
 * <ol>
 * <li>Manager layer: {@link DelAdminMgrImpl}, {@link DelAccessMgrImpl}, {@link DelReviewMgrImpl},...</li>
 * <li>Process layer: {@link AdminRoleP}, {@link OrgUnitP},...</li>
 * <li>DAO layer: {@link AdminRoleDAO}, {@link OrgUnitDAO},...</li>
 * </ol>
 * Fortress clients first instantiate and populate a data entity before invoking any of the Manager APIs.  The caller must
 * provide enough information to uniquely identity the entity target within ldap.<br />
 * For example, this entity requires {@link #name} set before passing into {@link DelAdminMgrImpl} or  {@link DelReviewMgrImpl} APIs.
 * Create methods usually require more attributes (than Read) due to constraints enforced between entities.
 * <p/>
 * This entity extends the {@link us.jts.fortress.rbac.Role} entity and is used to store the ARBAC AdminRole assignments that comprise the many-to-many relationships between Users and Administrative Permissions.
 * In addition it is used to store the ARBAC {@link OrgUnit.Type#PERM} and {@link OrgUnit.Type#USER} OU information that adheres to the AdminRole entity in the ARBAC02 model.
 * <br />The unique key to locate AdminRole entity (which is subsequently assigned both to Users and administrative Permissions) is {@link AdminRole#name}.<br />
 * <p/>
 * There is a many-to-many relationship between User's, Administrative Roles and Administrative Permissions.
 * <h3>{@link us.jts.fortress.rbac.User}*<->*{@link AdminRole}*<->*{@link us.jts.fortress.rbac.Permission}</h3>
 * Example to create new ARBAC AdminRole:
 * <p/>
 * <code>AdminRole myRole = new AdminRole("MyRoleName");</code><br />
 * <code>myRole.setDescription("This is a test admin role");</code><br />
 * <code>DelAdminMgr delAdminMgr = DelAdminMgrFactory.createInstance();</code><br />
 * <code>delAdminMgr.addRole(myRole);</code><br />
 * <p/>
 * This will create a AdminRole name that can be used as a target for User-AdminRole assignments and AdminRole-AdminPermission grants.
 * <p/>
 * <p/>
 * <h4>Administrative Role Schema</h4>
 * The Fortress AdminRole entity is a composite of the following other Fortress structural and aux object classes:
 * <p/>
 * 1. organizationalRole Structural Object Class is used to store basic attributes like cn and description.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.5.6.8 NAME 'organizationalRole'</code>
 * <li> <code>DESC 'RFC2256: an organizational role'</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST cn</code>
 * <li> <code>MAY ( x121Address $ registeredAddress $ destinationIndicator $</code>
 * <li> <code>preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $</code>
 * <li> <code>telephoneNumber $ internationaliSDNNumber $ facsimileTelephoneNumber $</code>
 * <li> <code>seeAlso $ roleOccupant $ preferredDeliveryMethod $ street $</code>
 * <li> <code>postOfficeBox $ postalCode $ postalAddress $</code>
 * <li> <code>physicalDeliveryOfficeName $ ou $ st $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * 2. ftRls Structural objectclass is used to store the AdminRole information like name, and temporal constraints.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass	( 1.3.6.1.4.1.38088.2.1</code>
 * <li> <code>NAME 'ftRls'</code>
 * <li> <code>DESC 'Fortress Role Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftRoleName )</code>
 * <li> <code>MAY ( description $ ftCstr ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * 3. ftProperties AUXILIARY Object Class is used to store client specific name/value pairs on target entity.<br />
 * <code># This aux object class can be used to store custom attributes.</code><br />
 * <code># The properties collections consist of name/value pairs and are not constrainted by Fortress.</code><br />
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.2</code>
 * <li> <code>NAME 'ftProperties'</code>
 * <li> <code>DESC 'Fortress Properties AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY ( ftProps ) ) </code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * <p/>
 * 4. ftPools Auxiliary object class store the ARBAC Perm and User OU assignments on AdminRole entity.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.3</code>
 * <li> <code>NAME 'ftPools'</code>
 * <li> <code>DESC 'Fortress Pools AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY ( ftOSU $ ftOSP ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * 5. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.4</code>
 * <li> <code>NAME 'ftMods'</code>
 * <li> <code>DESC 'Fortress Modifiers AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY (</code>
 * <li> <code>ftModifier $</code>
 * <li> <code>ftModCode $</code>
 * <li> <code>ftModId ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>

 *
 * @author Shawn McKinney
 */
@XmlRootElement(name = "fortAdminRole")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adminRole", propOrder =
    {
        "osPs",
        "osUs",
        "beginRange",
        "endRange",
        "beginInclusive",
        "endInclusive"
})
public class AdminRole extends Role implements Administrator
{
    private Set<String> osPs;
    private Set<String> osUs;
    private String beginRange;
    private String endRange;
    private boolean beginInclusive;
    private boolean endInclusive;


    /**
     * Default constructor is used by internal Fortress classes.
     */
    public AdminRole()
    {
    }


    /**
     * Construct an Admin Role with a given temporal constraint.
     *
     * @param con maps to 'OamRC' attribute for 'ftTemporal' aux object classes.
     */
    public AdminRole( Constraint con )
    {
        CUtil.copy( con, this );
    }


    /**
     * Construct an AdminRole entity with a given name.
     *
     */
    public AdminRole( String name )
    {
        this.setName( name );
    }


    /**
     * Load the role range attributes given a raw format.  This method is used internal to Fortress and is not intended
     * to be used by external callers.
     *
     * @param szRaw maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    @Override
    public void setRoleRangeRaw( String szRaw )
    {
        if ( VUtil.isNotNullOrEmpty( szRaw ) )
        {
            int bindx = szRaw.indexOf( "(" );
            if ( bindx > -1 )
            {
                this.setBeginInclusive( false );
            }
            else
            {
                bindx = szRaw.indexOf( "[" );
                this.setBeginInclusive( true );
            }
            int eindx = szRaw.indexOf( ")" );
            if ( eindx > -1 )
            {
                this.setEndInclusive( false );
            }
            else
            {
                eindx = szRaw.indexOf( "]" );
                this.setEndInclusive( true );
            }
            int cindx = szRaw.indexOf( ":" );
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
     *
     * Get the raw format for role range using current AdminRole entity attributes.  This method is used internal to Fortress and is not intended
     * to be used by external callers.
     *
     * @return String maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    @Override
    public String getRoleRangeRaw()
    {
        String szRaw = "";
        if ( this.beginRange != null )
        {
            if ( this.isBeginInclusive() )
                szRaw += "[";
            else
                szRaw += "(";
            szRaw += this.getBeginRange();
            szRaw += ":";
            szRaw += this.getEndRange();
            if ( this.isEndInclusive() )
                szRaw += "]";
            else
                szRaw += ")";
        }
        return szRaw;
    }


    /**
     * Get a collection of optional Perm OU attributes that were stored on the AdminRole entity.
     *
     * @return List of type String containing Perm OU.  This maps to 'ftOSP' attribute on 'ftPools' aux object class.
     */
    @Override
    public Set<String> getOsP()
    {
        return osPs;
    }


    /**
     * Set a collection of optional Perm OU attributes to be stored on the AdminRole entity.
     *
     * @param osPs is a List of type String containing Perm OU.  This maps to 'ftOSP' attribute on 'ftPools' aux object class.
     */
    @Override
    public void setOsP( Set<String> osPs )
    {
        this.osPs = osPs;
    }


    /**
     * Set a Perm OU attribute to be stored on the AdminRole entity.
     *
     * @param osP is a Perm OU that maps to 'ftOSP' attribute on 'ftPools' aux object class.
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
     * @return List of type String containing User OU.  This maps to 'ftOSU' attribute on 'ftPools' aux object class.
     */
    @Override
    public Set<String> getOsU()
    {
        return osUs;
    }


    /**
     * Set a collection of optional User OU attributes to be stored on the AdminRole entity.
     *
     * @param osUs is a List of type String containing User OU.  This maps to 'ftOSU' attribute on 'ftPools' aux object class.
     */
    @Override
    public void setOsU( Set<String> osUs )
    {
        this.osUs = osUs;
    }


    /**
     * Set a User OU attribute to be stored on the AdminRole entity.
     *
     * @param osU is a User OU that maps to 'ftOSU' attribute on 'ftPools' aux object class.
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
     * Return the begin Role range attribute for AdminRole entity which corresponds to lowest descendant.
     *
     * @return String that maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    @Override
    public String getBeginRange()
    {
        return beginRange;
    }


    /**
     * Set the begin Role range attribute for AdminRole entity which corresponds to lowest descendant.
     *
     * @param beginRange maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    @Override
    public void setBeginRange( String beginRange )
    {
        this.beginRange = beginRange;
    }


    /**
     * Return the end Role range attribute for AdminRole entity which corresponds to highest ascendant.
     *
     * @return String that maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    @Override
    public String getEndRange()
    {
        return endRange;
    }


    /**
     * Set the end Role range attribute for AdminRole entity which corresponds to highest ascendant.
     *
     * @param endRange maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    @Override
    public void setEndRange( String endRange )
    {
        this.endRange = endRange;
    }


    /**
     * Get the begin inclusive which specifies if role range includes or excludes the 'beginRange' attribute.
     *
     * @return String that maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    @Override
    public boolean isBeginInclusive()
    {
        return beginInclusive;
    }


    /**
     * Set the begin inclusive which specifies if role range includes or excludes the 'beginRange' attribute.
     *
     * @param beginInclusive maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    @Override
    public void setBeginInclusive( boolean beginInclusive )
    {
        this.beginInclusive = beginInclusive;
    }


    /**
     * Get the end inclusive which specifies if role range includes or excludes the 'endRange' attribute.
     *
     * @return String that maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    @Override
    public boolean isEndInclusive()
    {
        return endInclusive;
    }


    /**
     * Set the end inclusive which specifies if role range includes or excludes the 'endRange' attribute.
     *
     * @param endInclusive maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    @Override
    public void setEndInclusive( boolean endInclusive )
    {
        this.endInclusive = endInclusive;
    }


    /**
     * Matches the name from two AdminRole entities.
     *
     * @param thatObj contains an AdminRole entity.
     * @return boolean indicating both objects contain matching AdminRole names.
     */
    public boolean equals( Object thatObj )
    {
        if ( this == thatObj )
            return true;
        if ( this.getName() == null )
            return false;
        if ( !( thatObj instanceof AdminRole ) )
            return false;
        Role thatRole = ( Role ) thatObj;
        if ( thatRole.getName() == null )
            return false;
        return thatRole.getName().equalsIgnoreCase( this.getName() );
    }
}
