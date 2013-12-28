/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ant;

import us.jts.fortress.rbac.Relationship;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to load {@link Relationship}s used to drive {@link us.jts.fortress.AdminMgr#deleteInheritance(us.jts.fortress.rbac.Role, us.jts.fortress.rbac.Role)}.
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
 * @author Shawn McKinney
 */
public class Delroleinheritance
{
    final private List<Relationship> relationships = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Delroleinheritance()
    {
    }

    /**
     * <p>This method name, 'addRelationship', is used for derived xml tag 'relationship' in the load script.</p>
     * <pre>
     * {@code
     * <delroleinheritance>
     *     <relationship child="r2" parent="r1"/>
     *     <relationship child="r3" parent="r1"/>
     * </delroleinheritance>
     * }
     * </pre>
     *
     * @param relationship contains reference to data element targeted for removal.
     */
    public void addRelationship(Relationship relationship)
    {
        this.relationships.add(relationship);
    }

    /**
     * Used by {@link us.jts.fortress.ant.FortressAntTask#deleteRoleInheritances()} to retrieve list of Role relationships as defined in input xml file.
     *
     * @return collection containing {@link Relationship}s targeted for removal.
     */
    public List<Relationship> getRelationships()
    {
        return this.relationships;
    }
}

