/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;


import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import us.jts.fortress.rbac.dao.RoleDAO;
import us.jts.fortress.rbac.dao.UserDAO;


/**
 * All entities ({@link User}, {@link Role}, {@link us.jts.fortress.rbac.Permission},
 * {@link PwPolicy} {@link SDSet} etc...) are used to carry data between three Fortress
 * layers.starting with the (1) Manager layer down thru middle (2) Process layer and it's processing rules into
 * (3) DAO layer where persistence with the OpenLDAP server occurs.
 * <h4>Fortress Processing Layers</h4>
 * <ol>
 * <li>Manager layer:  {@link us.jts.fortress.rbac.AdminMgrImpl}, {@link us.jts.fortress.rbac.AccessMgrImpl}, {@link us.jts.fortress.rbac.ReviewMgrImpl},...</li>
 * <li>Process layer:  {@link UserP}, {@link RoleP}, {@link PermP},...</li>
 * <li>DAO layer: {@link UserDAO}, {@link RoleDAO}, {@link us.jts.fortress.rbac.dao.PermDAO},...</li>
 * </ol>
 * Fortress clients first instantiate and populate a data entity before invoking any of the Manager APIs.  The caller must
 * provide enough information to uniquely identity the entity target within ldap.<br />
 * For example, this entity requires {@link #objectName} and {@link #ou} attributes set before passing into {@link us.jts.fortress.rbac.AdminMgrImpl} or  {@link us.jts.fortress.rbac.ReviewMgrImpl} APIs.
 * Create methods usually require more attributes (than Read) due to constraints enforced between entities.
 * <p/>
 * <h4>PermObj entity attribute usages include</h4>
 * <ul>
 * <li>{@link #setObjectName} and {@link #setOu} attributes set before calling {@link us.jts.fortress.rbac.AdminMgrImpl#addPermObj(PermObj)}.
 * <li>{@link #addProperty} may be set before calling {@link us.jts.fortress.rbac.AdminMgrImpl#addPermObj(PermObj)}.
 * <li>{@link #getProperty} may be set after calling {@link us.jts.fortress.rbac.ReviewMgrImpl#findPermObjs(PermObj)}.
 * </ul>
 * <p/>
 * <h4>More Permission entity notes</h4>
 * <ul>
 * <li>The {@link PermObj} entity is not used for authorization checks, rather contains {@link us.jts.fortress.rbac.Permission} which are themselves authorization targets.<br />
 * <li>This entity must be associated with a valid Perm OU {@link OrgUnit.Type#PERM} that is contained within the {@code ou=OS-P,ou=ARBAC,dc=example,dc=com} location in ldap.
 * <li>The object to operation pairings enable application resources to be mapped to Fortress permissions in a way that is natural for object oriented programming.
 * <li>Permissions = Object {@link PermObj} 1<->* Operations {@link us.jts.fortress.rbac.Permission}
 * <p/>
 * <img src="../doc-files/RbacCore.png">
 * <li>The unique key to locate an Fortress PermObj entity is {@code PermObj#objectName}.
 * <li>For sample code usages check out {@link us.jts.fortress.rbac.Permission} javadoc.
 * </ul>
 * <p/>
 * <h4>PermObj Schema</h4>
 * The Fortress PermObj Entity Class is a composite of 3 different LDAP Schema object classes:
 * <p/>
 * 1. ftObject AUXILIARY Object Class is used to store object name, id and type variables on target entity.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass	( 1.3.6.1.4.1.38088.2.2</code>
 * <li> <code>NAME 'ftObject'</code>
 * <li> <code>DESC 'Fortress Permission Object Class'</code>
 * <li> <code>SUP organizationalunit</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST (</code>
 * <li> <code>ftId $ ftObjNm ) </code>
 * <li> <code>MAY ( ftType ) )  </code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * 2. ftProperties AUXILIARY Object Class is used to store client specific name/value pairs on target entity.
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
 * 3. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.
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
@XmlRootElement(name = "fortObject")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "permObj", propOrder =
    {
        "objectName",
        "description",
        "internalId",
        "ou",
        "type",
        "props",
        "admin"
})
public class PermObj extends FortEntity
    implements java.io.Serializable
{
    private boolean admin;
    private String internalId;
    private String objectName;
    private String description;
    @XmlElement(nillable = true)
    private Props props = new Props();
    //private Properties props;
    private String ou;
    private String type;
    @XmlTransient
    private String dn;


    /**
     * Default Constructor used internal to Fortress.
     */
    public PermObj()
    {

    }


    /**
     * Construct an Fortress PermObj entity given an object name.
     *
     * @param objectName maps to 'ftObjNm' attribute in 'ftObject' object class.
     */
    public PermObj( String objectName )
    {
        this.objectName = objectName;
    }


    /**
     * Construct an Fortress PermObj entity given an object and perm ou name.
     *
     * @param objectName maps to 'ftObjNm' attribute in 'ftObject' object class.
     * @param ou maps to 'ou' attribute in 'ftObject' object class.
     */
    public PermObj( String objectName, String ou )
    {
        this.objectName = objectName;
        this.ou = ou;
    }


    /**
     * Get the authorization target's object name.  This is typically mapped to the class name for component
     * that is the target for Fortress authorization check. For example 'PatientRelationshipInquire'.
     *
     * @return the name of the object which maps to 'ftObjNm' attribute in 'ftObject' object class.
     */
    public String getObjectName()
    {
        return objectName;
    }


    /**
     * This attribute is required and sets the authorization target object name.  This name is typically derived from the class name
     * for component that is the target for Fortress authorization check. For example 'CustomerCheckOutPage'.
     *
     */
    public void setObjectName( String objectName )
    {
        this.objectName = objectName;
    }


    /**
     * This attribute is required but is set automatically by Fortress DAO class before object is persisted to ldap.
     * This generated internal id is associated with PermObj.  This method is used by DAO class and
     * is not available to outside classes.   The generated attribute maps to 'ftId' in 'ftObject' object class.
     */
    public void setInternalId()
    {
        // generate a unique id that will be used as the rDn for this entry:
        UUID uuid = UUID.randomUUID();
        this.internalId = uuid.toString();

        //UID iid = new UID();
        // assign the unique id to the internal id of the entity:
        //this.internalId = iid.toString();
    }


    /**
     * Set the internal id that is associated with PermObj.  This method is used by DAO class and
     * is generated automatically by Fortress.  Attribute stored in LDAP cannot be changed by external caller.
     * This method can be used by client for search purposes only.
     *
     * @param internalId maps to 'ftId' in 'ftObject' object class.
     */
    public void setInternalId( String internalId )
    {
        this.internalId = internalId;
    }


    /**
     * Return the internal id that is associated with PermObj.  This attribute is generated automatically
     * by Fortress when new PermObj is added to directory and is not known or changeable by external client.
     *
     * @return attribute maps to 'ftId' in 'ftObject' object class.
     */
    public String getInternalId()
    {
        return internalId;
    }


    /**
     * If set to true, this entity will be loaded into the Admin Permission data set.
     *
     * @return boolean indicating if entity is an RBAC (false) or Admin (true) Permission Object.
     */
    public boolean isAdmin()
    {
        return admin;
    }


    /**
     * If set to true, this entity will be loaded into the Admin Permission data set.
     *
     * @param admin boolean variable indicates if entity is an RBAC or ARBAC Permission Object.
     */

    public void setAdmin( boolean admin )
    {
        this.admin = admin;
    }


    /**
     * Sets the optional description that is associated with PermObj.  This attribute is validated but not constrained by Fortress.
     *
     * @param description that is mapped to same name in 'organizationalUnit' object class.
     */
    public void setDescription( String description )
    {
        this.description = description;
    }


    /**
     * Returns optional description that is associated with PermObj.  This attribute is validated but not constrained by Fortress.
     *
     * @return value that is mapped to 'description' in 'organizationalUnit' object class.
     */
    public String getDescription()
    {
        return description;
    }


    /**
      * Gets the value of the Props property.  This method is used by Fortress and En Masse and should not be called by external programs.
      *
      * @return
      *     possible object is
      *     {@link Props }
      *
      */
    public Props getProps()
    {
        return props;
    }


    /**
     * Sets the value of the Props property.  This method is used by Fortress and En Masse and should not be called by external programs.
     *
     * @param value
     *     allowed object is
     *     {@link Props }
     *
     */
    public void setProps( Props value )
    {
        this.props = value;
    }


    /**
     * Add name/value pair to list of properties associated with PermObj.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @param key   contains property name and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     * @param value
     */
    public void addProperty( String key, String value )
    {
        Props.Entry entry = new Props.Entry();
        entry.setKey( key );
        entry.setValue( value );
        this.props.getEntry().add( entry );
    }


    /**
     * Get a name/value pair attribute from list of properties associated with PermObj.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @param key contains property name and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     * @return value containing name/value pair that maps to 'ftProps' attribute in 'ftProperties' aux object class.
     */
    public String getProperty( String key )
    {
        List<Props.Entry> props = this.props.getEntry();
        Props.Entry keyObj = new Props.Entry();
        keyObj.setKey( key );

        String value = null;
        int indx = props.indexOf( keyObj );
        if ( indx != -1 )
        {
            Props.Entry entry = props.get( props.indexOf( keyObj ) );
            value = entry.getValue();
        }

        return value;
    }


    /**
     * Add new collection of name/value pairs to attributes associated with PermObj.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @param props contains collection of name/value pairs and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     */
    public void addProperties( Properties props )
    {
        if ( props != null )
        {
            for ( Enumeration e = props.propertyNames(); e.hasMoreElements(); )
            {
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                String key = ( String ) e.nextElement();
                String val = props.getProperty( key );
                addProperty( key, val );
            }
        }
    }


    /**
     * Return the collection of name/value pairs to attributes associated with PermObj.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @return Properties contains collection of name/value pairs and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     */
    public Properties getProperties()
    {
        Properties properties = null;
        List<Props.Entry> props = this.props.getEntry();
        if ( props.size() > 0 )
        {
            properties = new Properties();
            //int size = props.size();
            for ( Props.Entry entry : props )
            {
                String key = entry.getKey();
                String val = entry.getValue();
                properties.setProperty( key, val );
            }
        }
        return properties;
    }


    /**
    * Add name/value pair to list of properties associated with PermObj.  These values are not constrained by Fortress.
    * Properties are optional.
    *
    * @param key   contains property name and maps to 'ftProps' attribute in 'ftProperties' aux object class.
    * @param value
    */
    //public void addProperty(String key, String value)
    //{
    //    if (props == null)
    //    {
    //        props = new Properties();
    //    }

    //    this.props.setProperty(key, value);
    //}

    /**
     * Add new collection of name/value pairs to attributes associated with PermObj.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @param props contains collection of name/value pairs and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     */
    //public void addProperties(Properties props)
    //{
    //    this.props = props;
    //}

    /**
     * Return the collection of name/value pairs to attributes associated with PermObj.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @return Properties contains collection of name/value pairs and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     */
    //public Properties getProperties()
    //{
    //    return this.props;
    //}

    /**
     * Set the orgUnit name associated with PermObj.  This attribute is validated and constrained by Fortress and must contain name of existing Perm OU.
     * This attribute is required on add but not on read.
     *
     * @param ou mapped to same name in 'ftObject' object class.
     */
    public void setOu( String ou )
    {
        this.ou = ou;
    }


    /**
     * Return orgUnit name for PermObj.  This attribute is validated and constrained by Fortress and must contain name of existing Perm OU.
     * This attribute is required on add but not on read.
     *
     * @return value that is mapped to 'ou' in 'ftObject' object class.
     */
    public String getOu()
    {
        return ou;
    }


    /**
     * Sets the type attribute of the Perm object.  Currently the type is not constrained to any
     * preexisting Fortress data set meaning the type is user defined and can be used for grouping like permissions.
     *
     * @param type maps to attribute name 'ftType' in 'ftObject' object class.
     */
    public void setType( String type )
    {
        this.type = type;
    }


    /**
     * Get the type attribute of the Perm object.  Currently the type is not constrained to any
     * preexisting Fortress data set meaning the type is user defined and can be used for grouping like permissions.
     *
     * @return maps to attribute name 'ftType' in 'ftObject' object class.
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set distinguished name associated with PermObj.  This attribute is used by DAO and is not allowed for outside classes.
     * This attribute should not be set by external callers.
     *
     * @param dn that is mapped to same name in 'organizationalUnit' object class.
     */
    public void setDn( String dn )
    {
        this.dn = dn;
    }


    /**
     * Returns distinguished name associated with PermObj.  This attribute is generated by DAO and is not allowed for outside classes to modify.
     * This attribute is for internal user only and need not be processed by external clients.
     *
     * @return value that is mapped to 'dn' in 'organizationalUnit' object class.
     */
    public String getDn()
    {
        return dn;
    }


    /**
     * Matches the objectName from two PermObj entities.
     *
     * @param thatObj contains a PermObj entity.
     * @return boolean indicating both objects contain matching objectNames.
     */
    public boolean equals( Object thatObj )
    {
        if ( this == thatObj )
            return true;
        if ( this.getObjectName() == null )
            return false;
        if ( !( thatObj instanceof PermObj ) )
            return false;
        PermObj thatPermObj = ( PermObj ) thatObj;
        if ( thatPermObj.getObjectName() == null )
            return false;
        return thatPermObj.getObjectName().equalsIgnoreCase( this.getObjectName() );
    }
}
