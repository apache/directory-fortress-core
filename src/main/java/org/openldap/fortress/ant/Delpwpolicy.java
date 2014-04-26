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

import org.openldap.fortress.rbac.PwPolicy;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to load {@link org.openldap.fortress.rbac.PwPolicy}s used to drive {@link org.openldap.fortress.PwPolicyMgr#delete(org.openldap.fortress.rbac.PwPolicy)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Delpwpolicy', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <delpwpolicy>
 *           ...
 *         </delpwpolicy>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 */
public class Delpwpolicy
{
    final private List<PwPolicy> policies = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Delpwpolicy()
    {
    }

    /**
     * <p>This method name, 'addPolicy', is used for derived xml tag 'policy' in the load script.</p>
     * <pre>
     * {@code
     * <delpwpolicy>
     *     <policy name="Test1"/>
     * </delpwpolicy>
     * }
     * </pre>
     *
     * @param policy contains reference to data element targeted for removal.
     */
    public void addPolicy(PwPolicy policy)
    {
        this.policies.add(policy);
    }

    /**
     * Used by {@link FortressAntTask#deletePolicies()} to retrieve list of PwPolicy as defined in input xml file.
     *
     * @return collection containing {@link org.openldap.fortress.rbac.PwPolicy}s targeted for removal.
     */
    public List<PwPolicy> getPolicies()
    {
        return this.policies;
    }
}

