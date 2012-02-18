/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import com.jts.fortress.hier.Relationship;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to load {@link Relationship}s used to drive {@link com.jts.fortress.DelegatedAdminMgr#addAscendant(com.jts.fortress.arbac.OrgUnit, com.jts.fortress.arbac.OrgUnit)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Adduserorgunitinheritance', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <adduserorgunitinheritance>
 *           ...
 *         </adduserorgunitinheritance>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author smckinn
 * @created November 18, 2011
 */
public class Adduserorgunitinheritance
{
    final private List<Relationship> relationships = new ArrayList<Relationship>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Adduserorgunitinheritance()
    {
    }

    /**
     * <p>This method name, 'addRelationship', is used for derived xml tag 'relationship' in the load script.</p>
     * <pre>
     * {@code
     * <adduserorgunitinheritance>
     *     <relationship child="ou2" parent="ou1"/>
     *     <relationship child="ou3" parent="ou1"/>
     *     <relationship child="ou4" parent="ou1"/>
     * </adduserorgunitinheritance>
     * }
     * </pre>
     *
     * @param relationship contains reference to data element targeted for insertion..
     */
    public void addRelationship(Relationship relationship)
    {
        this.relationships.add(relationship);
    }

    /**
     * Used by {@link FortressAntTask#addAdduserorgunitinheritance(Adduserorgunitinheritance)} to retrieve list of User Org Unit relationships as defined in input xml file.
     *
     * @return collection containing {@link Relationship}s targeted for insertion.
     */
    public List<Relationship> getRelationships()
    {
        return this.relationships;
    }
}

