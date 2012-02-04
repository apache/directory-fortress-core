/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;

import com.jts.fortress.arbac.OrgUnit;
import com.jts.fortress.rbac.Role;
import com.jts.fortress.rbac.Session;
import com.jts.fortress.rbac.User;
import com.jts.fortress.rbac.UserRole;

import javax.xml.bind.annotation.*;
import java.util.UUID;

/**
 * This abstract class is extended by other Fortress entities.  It is used to store contextual data that can be used for
 * administrative RBAC checking in addition to associating an audit context with every LDAP operation.
 * <p/>
 * <p/>
 * objectclass ( 1.3.6.1.4.1.38088.3.4
 * NAME 'ftMods'
 * DESC 'Fortress Modifiers AUX Object Class'
 * AUXILIARY
 * MAY (
 * ftModifier $
 * ftModCode $
 * ftModId
 * )
 * )
 * <p/>
 * <h4>Audit Context Schema</h4>
 * The FortEntity Class is stored in LDAP records as an auxiliary object class:
 * <p/>
 * ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.4</code>
 * <li> <code>NAME 'ftMods'</code>
 * <li> <code>DESC 'Fortress Modifiers AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY ( ftModifier $ ftModCode $ ftModId ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 *
 * @author smckinn
 * @created January 14, 2011
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
    OrgUnit.class,
    UserRole.class,
    User.class
})
public abstract class FortEntity
{
    protected String modCode;
    protected String modId;
    @XmlTransient
    protected Session adminSession;
    protected long sequenceId;

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
    public FortEntity(Session adminSession)
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
     * @param adminSession
     */
    public void setAdminSession(Session adminSession)
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
    public void setModCode(String modCode)
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
    public void setSequenceId(long sequenceId)
    {
        this.sequenceId = sequenceId;
    }
}

