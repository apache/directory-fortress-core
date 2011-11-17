/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import com.jts.fortress.hier.Relationship;
import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to load {@link Relationship}s used to drive {@link com.jts.fortress.AdminMgr#deleteInheritance(com.jts.fortress.rbac.Role, com.jts.fortress.rbac.Role)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Delroleinheritance', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <delroleinheritance>
 *           ...
 *         </delroleinheritance>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 *
 * @author smckinn
 * @created November 16, 2011
 */
public class Delroleinheritance
{
    final private List<Relationship> relationships = new ArrayList<Relationship>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Delroleinheritance()
    {
    }

    /**
     * <p>This method name, 'addRoleInheritance', is used for derived xml tag 'relationship' in the load script.</p>
     * <pre>
     * {@code
     * <delroleinheritance>
     *     <roleinheritance child="r2" parent="r1"/>
     *     <roleinheritance child="r3" parent="r1"/>
     * </delroleinheritance>
     * }
     * </pre>
     *
     * @param relationship contains reference to data element targeted for removal.
     */
    public void addRoleInheritance(Relationship relationship)
    {
        this.relationships.add(relationship);
    }

    /**
     * Used by {@link FortressAntTask#deleteRoles()} to retrieve list of Relationships as defined in input xml file.
     *
     * @return collection containing {@link Relationship}s targeted for removal.
     */
    public List<Relationship> getRoleInheritances()
    {
        return this.relationships;
    }
}

