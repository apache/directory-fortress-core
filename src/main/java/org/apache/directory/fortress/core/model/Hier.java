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
import java.util.ArrayList;
import java.util.List;

import org.apache.directory.api.util.Strings;


/**
 * All entities (User, Role, Permission, Policy, SDSet, etc...) are used to carry data between Fortress's
 * layers starting with the (1) Manager layer down thru middle (2) Process layer and it's processing rules into
 * (3) DAO layer where persistence with the LDAP server occurs.  The clients must instantiate an Fortress entity before use
 * and must provide enough information to uniquely identity target record for reads.
 * <p>
 * <h4>Hierarchical Relationship Schema</h4>
 * <p>
 * The Fortress ftHier Entity Class is used internal to Fortress and usually does not require manipulation by external program.  The
 * entity is a composite of 3 different LDAP Schema object classes:
 * <p>
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
 * <p>
 * 2. ftHier AUXILIARY Object Class is used to store parent to child relationships on target entity.
 * <pre>
 * ------------------------------------------
 * Fortress Hierarchies Structural Object Class
 * objectclass    ( 1.3.6.1.4.1.38088.2.7
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
 * <p>
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
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Hier extends FortEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** The operation */
    public Op op;

    /** The hierarchy type - ROLE, AROLE, USER, PERM */
    public Type type;

    /** the list of relationships that are set in collection on this entity */
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
    public Hier( List<Relationship> relationships )
    {
        this.relationships = relationships;
    }


    /**
     * Construct entity given a hierarchy type - ROLE, AROLE, USER, PERM.
     *
     * @param type determines where the target node resides.  For example the 'ROLE' type will specify the RBAC Role container as target.
     */
    public Hier( Type type )
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
    public Hier( Type type, String child, String parent )
    {
        this.type = type;
        setRelationship( child, parent );
    }


    /**
     * Construct entity given a parent and child.
     *
     * @param child  maps to the 'ftRels' attribute in 'ftHier' object class.
     * @param parent maps to the 'ftRels' attribute in 'ftHier' object class.
     */
    public Hier( String child, String parent )
    {
        setRelationship( child, parent );
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
    public void setOp( Op op )
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
    public void setType( Type type )
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
    public boolean isRelationship( String role, String parent )
    {
        boolean result = false;

        if ( relationships != null )
        {
            result = relationships.contains(
                new Relationship( Strings.toUpperCase( role ), Strings.toUpperCase( parent ) ) );
        }

        return result;
    }


    /**
     * Set the child and parent into the collection of valid relationships stored in this entity.
     *
     * @param role   attribute maps to the 'ftRels' attribute on 'ftHier' object class.
     * @param parent attribute maps to the 'ftRels' attribute on 'ftHier' object class.
     */
    public void setRelationship( String role, String parent )
    {
        if ( relationships == null )
        {
            relationships = new ArrayList<Relationship>();
        }

        relationships.add(
            new Relationship( Strings.toUpperCase( role ), Strings.toUpperCase( parent ) ) );
    }


    /**
     * Set the relationship object into the collection of valid relationships stored in this entity.
     *
     * @param rel attribute maps to the 'ftRels' attribute on 'ftHier' object class.
     */
    public void setRelationship( Relationship rel )
    {
        if ( relationships == null )
        {
            relationships = new ArrayList<Relationship>();
        }

        relationships.add( rel );
    }


    /**
     * Remove the specified relationship from the collection of valid relationships stored in this entity.
     *
     * @param role   attribute maps to the 'ftRels' attribute on 'ftHier' object class.
     * @param parent attribute maps to the 'ftRels' attribute on 'ftHier' object class.
     */
    public void removeRelationship( String role, String parent )
    {
        if ( relationships != null )
        {
            relationships.remove(
                new Relationship( Strings.toUpperCase( role ), Strings.toUpperCase( parent ) ) );
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
    public void setRelationships( List<Relationship> relationships )
    {
        this.relationships = relationships;
    }


    /**
     * @see Object#toString()
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append( "Hier object: \n" );

        sb.append( "    operation :" ).append( op ).append( '\n' );
        sb.append( "    type :" ).append( type ).append( '\n' );

        if ( relationships != null )
        {
            sb.append( "    relationships : " );

            boolean isFirst = true;

            for ( Relationship relationship : relationships )
            {
                if ( isFirst )
                {
                    isFirst = false;
                }
                else
                {
                    sb.append( ", " );
                }

                sb.append( relationship );
            }

            sb.append( '\n' );
        }

        return sb.toString();
    }
}