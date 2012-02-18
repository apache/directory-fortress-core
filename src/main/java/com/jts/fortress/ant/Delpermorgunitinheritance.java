/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import com.jts.fortress.hier.Relationship;
import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to load {@link Relationship}s used to drive {@link com.jts.fortress.AdminMgr#deleteInheritance(com.jts.fortress.rbac.Role, com.jts.fortress.rbac.Role)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Delpermorgunitinheritance', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <delpermorgunitinheritance>
 *           ...
 *         </delpermorgunitinheritance>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 *
 * @author smckinn
 * @created November 18, 2011
 */
public class Delpermorgunitinheritance
{
    final private List<Relationship> relationships = new ArrayList<Relationship>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Delpermorgunitinheritance()
    {
    }

    /**
     * <p>This method name, 'addRelationship', is used for derived xml tag 'relationship' in the load script.</p>
     * <pre>
     * {@code
     * <delpermorgunitinheritance>
     *     <relationship child="ou2" parent="ou1"/>
     *     <relationship child="ou3" parent="ou1"/>
     *     <relationship child="ou4" parent="ou1"/>
     * </delpermorgunitinheritance>
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
     * Used by {@link FortressAntTask#deleteRoles()} to retrieve list of Relationships as defined in input xml file.
     *
     * @return collection containing {@link Relationship}s targeted for removal.
     */
    public List<Relationship> getRelationships()
    {
        return this.relationships;
    }
}

