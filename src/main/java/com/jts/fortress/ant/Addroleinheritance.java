/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import com.jts.fortress.rbac.Relationship;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to load {@link Relationship}s used to drive {@link com.jts.fortress.AdminMgr#addInheritance(com.jts.fortress.rbac.Role, com.jts.fortress.rbac.Role)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Addroleinheritance', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addroleinheritance>
 *           ...
 *         </addroleinheritance>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 * @created November 16, 2011
 */
public class Addroleinheritance
{
    final private List<Relationship> relationships = new ArrayList<Relationship>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Addroleinheritance()
    {
    }

    /**
     * <p>This method name, 'addRelationship', is used for derived xml tag 'relationship' in the load script.</p>
     * <pre>
     * {@code
     * <addroleinheritance>
     *     <relationship child="r2" parent="r1"/>
     *     <relationship child="r3" parent="r1"/>
     *     <relationship child="r4" parent="r1"/>
     * </addroleinheritance>
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
     * Used by {@link com.jts.fortress.ant.FortressAntTask#addRoleInheritances()} to retrieve list of Role relationships as defined in input xml file.
     *
     * @return collection containing {@link Relationship}s targeted for insertion.
     */
    public List<Relationship> getRelationships()
    {
        return this.relationships;
    }
}

