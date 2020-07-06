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


import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;


/*
## OC2: Fortress Permission Structural Object Class
    objectclass    ( 1.3.6.1.4.1.38088.2.2
    NAME 'ftObject'
    DESC 'Fortress Permission Object Class'
    SUP organizationalunit
    STRUCTURAL
    MUST (
    ftId $
    ftObjNm
    )
    MAY (
    ftType
    )
    )
*/
/**
 * All entities ({@link User}, {@link Role}, {@link Permission}, {@link PwPolicy} {@link SDSet} etc...) are used to carry 
 * data between three Fortress layers, starting with the (1) Manager layer down thru middle (2) Process layer and it's 
 * processing rules into (3) DAO layer where persistence with the LDAP server occurs.
 * <h3></h3>
 * <h4>Fortress Processing Layers</h4>
 * <ol>
 *   <li>
 *     Manager layer:  {@link org.apache.directory.fortress.core.impl.AdminMgrImpl}, 
 *     {@link org.apache.directory.fortress.core.impl.AccessMgrImpl}, 
 *     {@link org.apache.directory.fortress.core.impl.ReviewMgrImpl},...
 *   </li>
 *   <li>
 *     Process layer:  org.apache.directory.fortress.core.impl.UserP,
 *     org.apache.directory.fortress.core.impl.RoleP, org.apache.directory.fortress.core.impl.PermP,...
 *   </li>
 *   <li>
 *     DAO layer: org.apache.directory.fortress.core.impl.UserDAO,
 *     org.apache.directory.fortress.core.impl.RoleDAO, org.apache.directory.fortress.core.impl.PermDAO,...
 *   </li>
 * </ol>
 * Fortress clients first instantiate and populate a data entity before invoking any of the Manager APIs.  The caller must
 * provide enough information to uniquely identity the entity target within ldap.<br>
 * For example, this entity requires {@link #setObjName} and {@link #setOpName} attributes set before passing into 
 * {@link org.apache.directory.fortress.core.impl.AccessMgrImpl} APIs.
 * Create methods usually require more attributes (than Read) due to constraints enforced between entities.
 * <h4>Permission entity attribute usages include</h4>
 * <ul>
 *   <li>
 *     {@link #setObjName} and {@link #setOpName} attributes set before calling 
 *     {@link org.apache.directory.fortress.core.impl.AccessMgrImpl#checkAccess(Session, Permission)}.
 *   </li>
 *   <li>
 *     {@link #getRoles} may be set after calling 
 *     {@link org.apache.directory.fortress.core.impl.ReviewMgrImpl#readPermission(Permission)} or 
 *     {@link org.apache.directory.fortress.core.impl.AccessMgrImpl#sessionPermissions(Session)}.
 *   </li>
 *   <li>
 *     {@link #getUsers} may be set after calling 
 *     {@link org.apache.directory.fortress.core.impl.ReviewMgrImpl#readPermission(Permission)} or 
 *     {@link org.apache.directory.fortress.core.impl.AccessMgrImpl#sessionPermissions(Session)}.
 *   </li>
 * </ul>
 * <h4>More Permission entity notes</h4>
 * <ul>
 *   <li>
 *     The unique key to locate a Permission entity (which is required for all authZ requests) is {@link Permission#objName} 
 *     and {@link Permission#opName}.<br>
 *   </li>
 *   <li>
 *     The Permission entity is used to target function points within computer programs needing authorization. This 
 *     permission model allows a one-to-many relationship between the objects {@link PermObj} and operations 
 *     {@link Permission}.
 *     <p>
 *     <img src="../doc-files/RbacCore.png" alt="">
 *   </li>
 *   <li>
 *     The object to operation pairings enable application resources to be mapped to Fortress permissions in a way that is 
 *     natural for object oriented programming.
 *   </li>
 *   <li>Permissions = Object {@link PermObj} 1-* Operations {@link Permission}</li>
 *   <li>Permissions in Fortress may also be assigned directly to {@link #users}.</li>
 *   <li>
 *     Objects {@link #objName}, Operations {@link #opName}, Roles {@link #roles}, Users {@link #users} are not case 
 *     sensitive for reads or searches.
 *   </li>
 * </ul>
 * <p>
 * The application entity that requires authorization will be mapped to the {@link PermObj} entity and the application's 
 * methods or operation names will be mapped to {@link Permission} entities.
 * For example, the application entity 'ShoppingCart' has 5 operations - 'create', 'read', 'update', 'delete' and 'checkout'.
 * The following code will create the permissions and perform the necessary grants.
 * <pre>
 * try
 * {
 *  // Instantiate the AdminMgr first
 *  AdminMgr adminMgr = AdminMgrFactory.createInstance();
 *
 *  // Now Instantiate the Object
 *  PermObj shoppingCart = new PermObj("ShoppingCart", "KillerBikes.com");
 *
 *  // Add it to the directory
 *  adminMgr.addPermObj(shoppingCart);
 *
 *  // Now create the permission operations and grant to applicable roles:
 *  Permission create = new Permission(shoppingCart.getObjName(), "create");
 *  adminMgr.addPermission(create);
 *  adminMgr.grantPermission(create, new Role("Customer"));
 *
 *  Permission read = new Permission(shoppingCart.getObjName(), "read");
 *  adminMgr.addPermission(read);
 *  adminMgr.grantPermission(read, new Role("Customer"));
 *
 *  Permission update = new Permission(shoppingCart.getObjName(), "update");
 *  adminMgr.addPermission(update);
 *  adminMgr.grantPermission(update, new Role("Admin"));
 *
 *  Permission delete = new Permission(shoppingCart.getObjName(), "delete");
 *  adminMgr.addPermission(delete);
 *  adminMgr.grantPermission(delete, new Role("Manager"));
 *
 *  Permission checkout = new Permission(shoppingCart.getObjName(), "checkout");
 *  adminMgr.addPermission(checkout);
 *  adminMgr.grantPermission(delete, new Role("Customer"));
 * }
 * catch (SecurityException ex)
 * {
 *  // log or throw
 * }
 * </pre>
 * <h4>Notes on the shopping cart example</h4>
 * <ul>
 *   <li>
 *     {@link User} that activate 'Manager' role into their Sessions will be allowed access to 'ShoppingCart.delete' 
 *     permission.
 *   </li>
 *   <li>{@link User} that activate 'Admin' role may perform 'ShoppingCart.update'.</li>
 *   <li>
 *     {@link User} with 'Customer' role may perform the 'ShoppingCart.create'  'ShoppingCart.read and 
 *     'ShoppingCart.checkout'.
 *   </li>
 *   <li>{@link Role}s must exist in ldap before assignment here, see javadoc {@link Role} for details.</li>
 * </ul>
 * <h4>Permission Schema</h4>
 * This Permission entity extends a single standard ldap structural object class, {@code organizationalRole} with
 * one extension structural class, {@code ftOperation}, and two auxiliary object classes, {@code ftProperties}, {@code ftMods}.
 * The following 3 LDAP object classes will be mapped into this entity:
 * <p>
 * 1. {@code ftOperation} STRUCTURAL Object Class is assigned roles and/or users which grants permissions which can be later 
 * checked using either 'checkAccess' or 'sessionPermissions APIs both methods that reside in the 'AccessMgrImpl' class.
 * <pre>
 * ------------------------------------------
 * Fortress Operation Structural Object Class
 * objectclass    ( 1.3.6.1.4.1.38088.2.3
 *  NAME 'ftOperation'
 *  DESC 'Fortress Permission Operation Structural Object Class'
 *  SUP organizationalrole
 *  STRUCTURAL
 *  MUST (
 *      ftId $
 *      ftPermName $
 *      ftObjNm $
 *      ftOpNm
 *  )
 *  MAY (
 *      ftObjId $
 *      ftRoles $
 *      ftUsers $
 *      ftType
 *  )
 * )
 * ------------------------------------------
 * </pre>
 * 2. {@code ftProperties} AUXILIARY Object Class is used to store optional client or otherwise custom name/value pairs on 
 * target entity.<br>
 * <code># This aux object class can be used to store custom attributes.</code><br>
 * <code># The properties collections consist of name/value pairs and are not constrainted by Fortress.</code><br>
 * <pre>
 * ------------------------------------------
 * AC2: Fortress Properties Auxiliary Object Class
 * objectclass ( 1.3.6.1.4.1.38088.3.2
 *  NAME 'ftProperties'
 *  DESC 'Fortress Properties AUX Object Class'
 *  AUXILIARY
 *  MAY (
 *      ftProps
 *  )
 * )
 * ------------------------------------------
 * </pre>
 * 3. {@code ftMods} AUXILIARY Object Class is used to store Fortress audit variables on target entity.
 * <pre>
 * ------------------------------------------
 * Fortress Audit Modification Auxiliary Object Class
 * objectclass ( 1.3.6.1.4.1.38088.3.4
 *  NAME 'ftMods'
 *  DESC 'Fortress Modifiers AUX Object Class'
 *  AUXILIARY
 *  MAY (
 *      ftModifier $
 *      ftModCode $
 *      ftModId
 *  )
 * )
 * ------------------------------------------
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "fortPermission")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "permission", propOrder =
    {
        "objName",
        "opName",
        "objId",
        "description",
        "abstractName",
        "internalId",
        "type",
        "users",
        "roles",
        "props",
        "dn",
        "admin",
        "paSets"
})
public class Permission extends FortEntity implements Serializable
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;

    private boolean admin;
    private String internalId;
    private String opName;
    private String objName;
    private String objId;
    private String abstractName;
    private String type;
    private String dn;
    private String description;
    @XmlElement(nillable = true)
    private Props props = new Props();
    //private Properties props;
    @XmlElement(nillable = true)
    private Set<String> roles;
    @XmlElement(nillable = true)
    private Set<String> users;
    private Set<String> paSets;

    /**
     * This constructor is commonly used to create Permission that is a target for authorization API.
     *
     * @param objName maps to 'ftObjNm' attribute in 'ftOperation' object class.
     * @param opName     maps to 'ftOpNm' attribute in 'ftOperation' object class.
     */
    public Permission( String objName, String opName )
    {
        this.objName = objName;
        this.opName = opName;
    }


    /**
     * Default constructor is used by internal Fortress classes and not intended for external use.
     */
    public Permission()
    {
    }


    /**
     * Constructor is used for APIs that do not require opName for example ARBAC canGrant/canRevoke.
     *
     * @param objName maps to 'ftObjNm' attribute in 'ftOperation' object class.
     */
    public Permission( String objName )
    {
        this.objName = objName;
    }


    /**
     * This constructor adds the objId which is used for creating Permissions that have an identity.
     *
     * @param objName maps to 'ftObjNm' attribute in 'ftOperation' object class.
     * @param opName     maps to 'ftOpNm' attribute in 'ftOperation' object class.
     * @param objId   maps to 'ftObjId' attribute in 'ftOperation' object class.
     */
    public Permission( String objName, String opName, String objId )
    {
        this.objName = objName;
        this.opName = opName;
        this.objId = objId;
    }


    /**
     * This constructor adds the admin flag which is used to process as Administrative permission.
     *
     * @param objName maps to 'ftObjNm' attribute in 'ftOperation' object class.
     * @param opName     maps to 'ftOpNm' attribute in 'ftOperation' object class.
     * @param admin      attribute is used to specify the Permission is to be stored and processed in the Administrative RBAC data sets.
     */
    public Permission( String objName, String opName, boolean admin )
    {
        this.objName = objName;
        this.opName = opName;
        this.admin = admin;
    }


    /**
     * Determine if this Permission is for RBAC or ARBAC processing.
     *
     * @return 'true' indicates administrative permission.
     */
    public boolean isAdmin()
    {
        return admin;
    }


    /**
     * Set will determine if this Permission is for RBAC or ARBAC processing.
     *
     * @param admin contains is 'true' if ARBAC permission..
     */
    public void setAdmin( boolean admin )
    {
        this.admin = admin;
    }


    /**
     * This attribute is required but is set automatically by Fortress DAO class before object is persisted to ldap.
     * This generated internal id is associated with Permission.  This method is used by DAO class and
     * is not available to outside classes.   The generated attribute maps to 'ftId' in 'ftOperation' object class.
     */
    public void setInternalId()
    {
        // generate a unique id that will be used for the internal id:
        UUID uuid = UUID.randomUUID();
        this.internalId = uuid.toString();
    }


    /**
     * Set the internal id that is associated with Permission.  This method is used by DAO class and
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
     * Return the internal id that is associated with Permission.  This attribute is generated automatically
     * by Fortress when new PermObj is added to directory and is not known or changeable by external client.
     *
     * @return attribute maps to 'ftId' in 'ftOperation' object class.
     */
    public String getInternalId()
    {
        return internalId;
    }


    /**
     * Get the Permission operation name.  This is used to specify method name - i.e. Create, Read, Update, Delete, ...
     *
     * @return opName maps to 'ftOpNm' attribute in 'ftOperation' object class.
     */
    public String getOpName()
    {
        return opName;
    }


    /**
     * Set the Permission operation name.  This is used to specify method name - i.e. Create, Read, Update, Delete, ...
     *
     * @param opName maps to 'ftOpNm' attribute in 'ftOperation' object class.
     */
    public void setOpName( String opName )
    {
        this.opName = opName;
    }


    /**
     * Get the authorization target's object name.  This is typically mapped to the class name for component
     * that is the target for Fortress authorization check. For example 'PatientRelationshipInquire'.
     *
     * @return the name of the object which maps to 'ftObjNm' attribute in 'ftOperation' object class.
     */
    public String getObjName()
    {
        return this.objName;
    }


    /**
     * This attribute is required and sets the authorization target object name.  This name is typically derived from the 
     * class name for component that is the target for Fortress authorization check. For example 'CustomerCheckOutPage'.
     * 
     * @param objName The target object name 
     */
    public void setObjName( String objName )
    {
        this.objName = objName;
    }


    /**
     * Return the Permission's abstract name which is the value of objName concatenated with OpName, i.e. 'Patient.checkin'
     * This value is automatically generated by the Fortress DAO class.
     *
     * @return abstractName maps to 'ftPermName' attribute in 'ftOperation' object class.
     */
    public String getAbstractName()
    {
        return abstractName;
    }


    /**
     * Set the Permission's abstract name which is the value of objName concatenated with OpName, i.e. 'Patient.checkin'
     * This value is automatically generated by the Fortress DAO class and value will be ignored if set by external client.
     *
     * @param abstractName maps to 'ftPermName' attribute in 'ftOperation' object class.
     */
    public void setAbstractName( String abstractName )
    {
        this.abstractName = abstractName;
    }


    /**
     * Get the optional type name which is an unconstrained attribute on Permission entity.
     *
     * @return type maps to 'ftType' attribute in 'ftOperation' object class.
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set the optional type name which is an unconstrained attribute on Permission entity.
     *
     * @param type maps to 'ftType' attribute in 'ftOperation' object class.
     */
    public void setType( String type )
    {
        this.type = type;
    }


    /**
     * Get optional objId attribute which can be used to tag a Permission object with an identity, i.e. objName='Customer', objId='12345'.
     * This value is not constrained by any other object.
     *
     * @return maps to 'ftObjectId' attribute in 'ftOperation' object class.
     */
    public String getObjId()
    {
        return objId;
    }


    /**
     * Set optional objId which can be used to tag a Permission object with an identity, i.e. objName='Account', objId='09876543'.
     * This value is not constrained by any other object.
     *
     * @param objId maps to 'ftObjectId' attribute in 'ftOperation' object class.
     */
    public void setObjId( String objId )
    {
        this.objId = objId;
    }


    /**
     * Add a Role name to list of Roles that are valid for this Permission.  This is optional attribute.
     *
     * @param role maps to 'ftRoles' attribute in 'ftOperation' object class.
     */
    public void setRole( String role )
    {
        if ( roles == null )
        {
            roles = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        }

        this.roles.add( role );
    }


    /**
     * Delete a Role name from list of Roles that are valid for this Permission.
     *
     * @param role maps to 'ftRoles' attribute in 'ftOperation' object class.
     */
    public void delRole( String role )
    {
        if ( this.roles != null )
        {
            this.roles.remove( role );
        }
    }


    /**
     * Return the collection of optional Roles that have been loaded into this entity.  This is stored as a multi-occurring
     * attribute of Role names on the 'ftOperation' object class.
     *
     * @return Set containing the roles which maps to 'ftRoles' attribute in 'ftOperation' object class.
     */
    public Set<String> getRoles()
    {
        return this.roles;
    }


    /**
     * Set the collection of optional Roles that have been loaded into this entity.  This is stored as a multi-occurring
     * attribute of Role names on the 'ftOperation' object class.
     *
     * @param roles maps to 'ftRoles' attribute in 'ftOperation' object class.
     */
    public void setRoles( Set<String> roles )
    {
        this.roles = roles;
    }


    /**
     * Add a UserId to list of Users that are valid for this Permission.  This is optional attribute.
     *
     * @param user maps to 'ftUsers' attribute in 'ftOperation' object class.
     */
    public void setUser( String user )
    {
        if ( users == null )
        {
            users = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        }

        this.users.add( user );
    }


    /**
     * Return the collection of optional Users that have been loaded into this entity.  This is stored as a multi-occurring
     * attribute of ftUsers on the 'ftOperation' object class.
     *
     * @return Set containing the Users which maps to 'ftUsers' attribute in 'ftOperation' object class.
     */
    public Set<String> getUsers()
    {
        return this.users;
    }


    /**
     * Set the collection of optional Users that have been loaded into this entity.  This is stored as a multi-occurring
     * attribute of userIds on the 'ftOperation' object class.
     *
     * @param users maps to 'ftUsers' attribute in 'ftOperation' object class.
     */
    public void setUsers( Set<String> users )
    {
        this.users = users;
    }


    public String getDn()
    {
        return dn;
    }


    public void setDn( String dn )
    {
        this.dn = dn;
    }


    /**
     * Return the description field on this entity.  The description is often used as a human readable label for the permission.
     * @return String containing the description.
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set the optional description field on this entity.  The description is used as a human readable label for the permission.
     *
     * @param description String contains the description.
     */
    public void setDescription( String description )
    {
        this.description = description;
    }


    /**
      * Gets the value of the Props property.  This method is used by Fortress Core and Rest and should not be called by external programs.
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
     * Sets the value of the Props property.  This method is used by Fortress Core and Rest and should not be called by external programs.
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
     * Add name/value pair to list of properties associated with Permission.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @param key   contains property name and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     * @param value The property value
     */
    public void addProperty( String key, String value )
    {
        Props.Entry entry = new Props.Entry();
        entry.setKey( key );
        entry.setValue( value );
        this.props.getEntry().add( entry );
    }


    /**
     * Get a name/value pair attribute from list of properties associated with Permission.  These values are not constrained by Fortress.
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
     * Add new collection of name/value pairs to attributes associated with Permission.  These values are not constrained by Fortress.
     * Properties are optional.
     *
     * @param props contains collection of name/value pairs and maps to 'ftProps' attribute in 'ftProperties' aux object class.
     */
    public void addProperties( Properties props )
    {
        if ( props != null )
        {
            for ( Enumeration<?> e = props.propertyNames(); e.hasMoreElements(); )
            {
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                String key = ( String ) e.nextElement();
                String val = props.getProperty( key );
                addProperty( key, val );
            }
        }
    }


    /**
     * Return the collection of name/value pairs to attributes associated with Permission.  These values are not constrained by Fortress.
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
     * Matches the objName, opName and objId from two Permission entities.
     *
     * @param o contains a Permission entity.
     * @return boolean indicating both Permissions contain matching objName and opName attributes.
     */
    @Override
    public boolean equals(Object o)
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        Permission that = ( Permission ) o;

        if ( StringUtils.isNotEmpty( objId ) ? !objId.equalsIgnoreCase( that.objId ) : StringUtils.isNotEmpty( that.objId ) )
        {
            return false;
        }
        if ( objName != null ? !objName.equalsIgnoreCase( that.objName ) : that.objName != null )
        {
            return false;
        }
        if ( opName != null ? !opName.equalsIgnoreCase( that.opName ) : that.opName != null )
        {
            return false;
        }

        return true;
    }


    @Override
    public int hashCode()
    {
        int result = ( admin ? 1 : 0 );
        result = 31 * result + ( internalId != null ? internalId.hashCode() : 0 );
        result = 31 * result + ( opName != null ? opName.hashCode() : 0 );
        result = 31 * result + ( objName != null ? objName.hashCode() : 0 );
        result = 31 * result + ( objId != null ? objId.hashCode() : 0 );
        result = 31 * result + ( abstractName != null ? abstractName.hashCode() : 0 );
        result = 31 * result + ( type != null ? type.hashCode() : 0 );
        result = 31 * result + ( dn != null ? dn.hashCode() : 0 );
        result = 31 * result + ( description != null ? description.hashCode() : 0 );
        result = 31 * result + ( props != null ? props.hashCode() : 0 );
        result = 31 * result + ( roles != null ? roles.hashCode() : 0 );
        result = 31 * result + ( users != null ? users.hashCode() : 0 );
        return result;
    }


    @Override
    public String toString()
    {
        return "Permission{" +
            "objName='" + objName + '\'' +
            ", opName='" + opName + '\'' +
            ", objId='" + objId + '\'' +
            '}';
    }

    public Set<String> getPaSets() {
        if ( paSets == null )
        {
            paSets = new HashSet<String>();
        }
        return paSets;
    }

    public void setPaSets( Set<String> paSets ){
        this.paSets = paSets;
    }
    
    public void setPaSetName(String paSet) {
        if ( paSets == null )
        {
            paSets = new HashSet<String>();
        }

        this.paSets.add( paSet );
    }
}
