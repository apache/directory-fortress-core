/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ant;

import us.jts.fortress.rbac.PwPolicy;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to load {@link us.jts.fortress.rbac.PwPolicy}s used to drive {@link us.jts.fortress.PwPolicyMgr#delete(us.jts.fortress.rbac.PwPolicy)}.
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
     * @return collection containing {@link us.jts.fortress.rbac.PwPolicy}s targeted for removal.
     */
    public List<PwPolicy> getPolicies()
    {
        return this.policies;
    }
}

