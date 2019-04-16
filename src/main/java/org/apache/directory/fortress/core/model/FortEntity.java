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


import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;


/**
 * This abstract class is extended by other Fortress entities.  It is used to store contextual data that can be used for
 * administrative RBAC checking in addition to associating an audit context with every LDAP operation.
 * <h3></h3>
 * <h4>Audit Context Schema</h4>
 * The FortEntity Class is used to tag all Fortress LDAP records with variables contained within this auxiliary object class:
 * <p>
 * ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.
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
 * <p>
 * This class is not thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "fortEntity")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fortEntity", propOrder =
    {
        "modId",
        "modCode",
        "sequenceId"
})
@XmlSeeAlso(
    {
        Role.class,
        SDSet.class,
        OrgUnit.class,
        UserRole.class,
        User.class,
        Permission.class,
        PermObj.class,
        PwPolicy.class,
        RoleRelationship.class,
        PermGrant.class,
        Session.class,
        AdminRoleRelationship.class,
        OrgUnitRelationship.class,
        RolePerm.class,
        UserAudit.class,
        AuthZ.class,
        Bind.class,
        Mod.class,
        Props.class,
        PermissionAttribute.class,
        PermissionAttributeSet.class,
        RoleConstraint.class,
        Configuration.class
})
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="fqcn", visible=false)
public abstract class FortEntity
{
    protected String modCode;
    protected String modId;
    @XmlTransient
    protected Session adminSession;
    protected long sequenceId;
    @XmlTransient
    protected String contextId;

    /**
     * Default constructor will call the setter to load a new internal ID into entity.
     */
    public FortEntity()
    {
        setInternalId();
    }
    
    
    /**
     * Use this constructor to load administrative RBAC session into this entity.
     *
     * @param adminSession contains ARBAC Session object.
     */
    public FortEntity( Session adminSession )
    {
        setInternalId();
        this.adminSession = adminSession;
    }
    
    
    /**
     * This attribute is required but is set automatically by Fortress DAO class before object is persisted to ldap.
     * This generated internal id is associated with PermObj.  This method is used by DAO class and
     * is not available to outside classes.   The generated attribute maps to 'ftId' in 'ftObject' object class.
     */
    private void setInternalId()
    {
        UUID uuid = UUID.randomUUID();
        this.modId = uuid.toString();
    }
    
    
    /**
     * Return the ARBAC Session object that was loaded into this entity.
     *
     * @return ARBAC Session object.
     */
    public Session getAdminSession()
    {
        return adminSession;
    }
    
    
    /**
     * Load an ARBAC Session object into this entity.  Once loaded, all Fortress Manager's will perform administrative
     * permission checks against the User who is contained within the Session.
     *
     * @param adminSession The ARBAC admin session 
     */
    public void setAdminSession( Session adminSession )
    {
        this.adminSession = adminSession;
    }
    
    
    /**
     * Contains the Fortress modification code to be associated with an audit record.  This is the ObjectName.methodName
     * for the Manager API that was called.
     *
     * @return String contains the modification code maps to 'ftModCode' attribute in 'FortEntity' object class.
     */
    public String getModCode()
    {
        return modCode;
    }
    
    
    /**
     * Set the Fortress modification code to be associated with an audit record.  Contains the Fortress modification code
     * which is ObjectName.methodName for the Manager API that was called.
     *
     * @param modCode contains the modification code maps to 'ftModCode' attribute in 'FortEntity' object class.
     */
    public void setModCode( String modCode )
    {
        this.modCode = modCode;
    }
    
    
    /**
     * Get the unique ID that is to be associated with a particular audit record in directory.
     *
     * @return attribute that maps to 'ftModId' attribute in 'FortEntity' object class.
     */
    public String getModId()
    {
        return modId;
    }
    
    
    /**
     * Return the contextId for this record.  The contextId is used for multi-tenancy to isolate data sets within a particular sub-tree within DIT
     *
     * @return value maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     */
    public String getContextId()
    {
        return contextId;
    }
    
    
    /**
     * Set the contextId associated with this record.  The contextId is used for multi-tenancy to isolate data sets within a particular sub-tree within DIT.
     * Package private to prevent outside classes from setting.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     */
    public void setContextId( String contextId )
    {
        this.contextId = contextId;
    }
    
    
    /**
     * Sequence id is used internal to Fortress.
     *
     * @return long value contains sequence id.
     */
    public long getSequenceId()
    {
        return sequenceId;
    }
    
    
    /**
     * Sequence id is used internal to Fortress
     *
     * @param sequenceId contains sequence to use.
     */
    public void setSequenceId( long sequenceId )
    {
        this.sequenceId = sequenceId;
    }
}