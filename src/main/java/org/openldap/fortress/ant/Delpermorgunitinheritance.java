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

package org.openldap.fortress.ant;

import org.openldap.fortress.rbac.Relationship;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to load {@link Relationship}s used to drive {@link org.openldap.fortress.AdminMgr#deleteInheritance(org.openldap.fortress.rbac.Role, org.openldap.fortress.rbac.Role)}.
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
 * @author Shawn McKinney
 */
public class Delpermorgunitinheritance
{
    final private List<Relationship> relationships = new ArrayList<>();

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

