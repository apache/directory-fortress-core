/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */

package org.openldap.fortress.rbac;

import java.util.ArrayList;
import java.util.List;

/**
 * All entities (User, Role, Permission, Policy, SDSet, etc...) are used to carry data between Fortress's
 * layers starting with the (1) Manager layer down thru middle (2) Process layer and it's processing rules into
 * (3) DAO layer where persistence with the OpenLDAP server occurs.  The clients must instantiate an Fortress entity before use
 * and must provide enough information to uniquely identity target record for reads.
 * <p/>
 * <h4>Hierarchical Relationship Schema</h4>
 * <p/>
 * The Fortress ftHier Entity Class is used internal to Fortress and usually does not require manipulation by external program.  The
 * entity is a composite of 3 different LDAP Schema object classes:
 * <p/>
 * 1. organizationalRole Structural Object Class is used to store basic attributes like cn and description.
 * <pre>
 * ------------------------------------------
 * objectclass ( 2.5.6.8 NAME 'organizationalRole'
 *  DESC 'RFC2256: an organizational role'
 *  SUP top STRUCTURAL
 *  MUST cn
 *  MAY (
 *      x121Address $ registeredAddress $ destinationIndicator $
 *      preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $
 *      telephoneNumber $ internationaliSDNNumber $ facsimileTelephoneNumber $
 *      seeAlso $ roleOccupant $ preferredDeliveryMethod $ street $
 *      postOfficeBox $ postalCode $ postalAddress $
 *      physicalDeliveryOfficeName $ ou $ st $ l $ description
 *  )
 * )
 * ------------------------------------------
 * </pre>
 * <p/>
 * 2. ftHier AUXILIARY Object Class is used to store parent to child relationships on target entity.
 * <pre>
 * ------------------------------------------
 * Fortress Hierarchies Structural Object Class
 * objectclass	( 1.3.6.1.4.1.38088.2.7
 * NAME 'ftHier'
 * DESC 'Fortress Hierarchy Structural Object Class'
 * SUP organizationalrole
 * STRUCTURAL
 * MUST (
 *      cn
 *  )
 * MAY (
 *      ftRels $
 *      description
 *  )
 * )
 * <p/>
 * 3. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.
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
 * <p/>
 *
 * @author Shawn McKinney
 */
public class Hier extends FortEntity
    implements java.io.Serializable
{
    public Op op;
    public Type type;
    private List<Relationship> relationships;

    /**
     * default constructor is used by internal components.
     */
    public Hier()
    {
    }

    /**
     * construct hierarchy given a list of parent-child relationships.
     *
     * @param relationships maps to 'ftRels' attribute on 'ftHier' object class.
     */
    public Hier(List<Relationship> relationships)
    {
        this.relationships = relationships;
    }

    /**
     * Construct entity given a hierarchy type - ROLE, AROLE, USER, PERM.
     *
     * @param type determines where the target node resides.  For example the 'ROLE' type will specify the RBAC Role container as target.
     */
    public Hier(Type type)
    {
        this.type = type;
    }

    /**
     * Construct entity given a parent, child and a hierarchy type.
     *
     * @param type   determines where the target node resides.  For example the 'ROLE' type will specify the RBAC Role container as target.
     * @param child  maps to the 'ftRels' attribute in 'ftHier' object class.
     * @param parent maps to the 'ftRels' attribute in 'ftHier' object class.
     */
    public Hier(Type type, String child, String parent)
    {
        this.type = type;
        setRelationship(child, parent);
    }

    /**
     * Construct entity given a parent and child.
     *
     * @param child  maps to the 'ftRels' attribute in 'ftHier' object class.
     * @param parent maps to the 'ftRels' attribute in 'ftHier' object class.
     */
    public Hier(String child, String parent)
    {
        setRelationship(child, parent);
    }

    /**
     * Operation type specifies if Add, Update or Deletion of relationship is being targeted.
     */
    public enum Op
    {
        /**
         * Add a new hierarchical relationship to the data set.
         */
        ADD,

        /**
         * Modify an existing hierarchical relationship in the data set.
         */
        MOD,

        /**
         * Remove an existing hierarchical relationship from the data set.
         */
        REM
    }

    /**
     * Return the operation to execute on behalf of this entity.
     *
     * @return Op value which maps to Add, Update or Delete attribute targets.
     */
    public Op getOp()
    {
        return op;
    }

    /**
     * The the operation for which this entity is bound for.  Add, Update or Delete.
     *
     * @param op type contains 'ADD', 'MOD', or 'REM'.
     */
    public void setOp(Op op)
    {
        this.op = op;
    }

    /**
     * Enumeration is used to specify which hierarchy node this entity is bound to.  RBAC Role, Admin Roles, User OU or Perm OU.
     */
    public enum Type
    {
        /**
         * RBAC Role data set
         */
        ROLE,

        /**
         * Administrative Role data set
         */
        AROLE,

        /**
         * User OU data set
         */
        USER,

        /**
         * Permission OU data set
         */
        PERM
    }

    /**
     * Return required the type of node this entity is bound to.
     *
     * @return variable specifies which directory node the hierarchy entity is bound to.
     */
    public Type getType()
    {
        return type;
    }

    /**
     * Set the required type which determines which directory node this entity is bound to.
     *
     * @param type variable specifies which directory node the hierarchy entity is bound to.
     */
    public void setType(Type type)
    {
        this.type = type;
    }


    /**
     * Return true if child and parent represent a valid relationship that is contained within the collection of
     * relationships.
     *
     * @param role   attribute maps to the 'ftRels' attribute on 'ftHier' object class.
     * @param parent attribute maps to the 'ftRels' attribute on 'ftHier' object class.
     */
    public boolean isRelationship(String role, String parent)
    {
        boolean result = false;
        if (relationships != null)
        {
            result = relationships.contains(new Relationship(role.toUpperCase(), parent.toUpperCase()));
        }

        return result;
    }

    /**
     * Set the child and parent into the collection of valid relationships stored in this entity.
     *
     * @param role   attribute maps to the 'ftRels' attribute on 'ftHier' object class.
     * @param parent attribute maps to the 'ftRels' attribute on 'ftHier' object class.
     */
    public void setRelationship(String role, String parent)
    {
        if (relationships == null)
        {
            relationships = new ArrayList<>();
        }

        relationships.add(new Relationship(role.toUpperCase(), parent.toUpperCase()));
    }

    /**
     * Set the relationship object into the collection of valid relationships stored in this entity.
     *
     * @param rel attribute maps to the 'ftRels' attribute on 'ftHier' object class.
     */
    public void setRelationship(Relationship rel)
    {
        if (relationships == null)
        {
            relationships = new ArrayList<>();
        }

        relationships.add(rel);
    }

    /**
     * Remove the specified relationship from the collection of valid relationships stored in this entity.
     *
     * @param role   attribute maps to the 'ftRels' attribute on 'ftHier' object class.
     * @param parent attribute maps to the 'ftRels' attribute on 'ftHier' object class.
     */
    public void removeRelationship(String role, String parent)
    {
        if (relationships != null)
        {
            relationships.remove(new Relationship(role.toUpperCase(), parent.toUpperCase()));
        }
    }

    /**
     * Return the list of relationships that are set in collection on this entity.
     *
     * @return List of relationships that map to the 'ftRels' attribute on the 'ftHier' object class.
     */
    public List<Relationship> getRelationships()
    {
        return relationships;
    }

    /**
     * Set the list of relationships that are set in collection on this entity.
     *
     * @param relationships that map to the 'ftRels' attribute on the 'ftHier' object class.
     */
    public void setRelationships(List<Relationship> relationships)
    {
        this.relationships = relationships;
    }
}