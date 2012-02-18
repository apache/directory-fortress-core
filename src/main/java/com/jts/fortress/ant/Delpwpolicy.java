/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to load {@link com.jts.fortress.pwpolicy.PswdPolicy}s used to drive {@link com.jts.fortress.PswdPolicyMgr#delete(com.jts.fortress.pwpolicy.PswdPolicy)}.
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
 * @author smckinn
 * @created November 23, 2010
 */
public class Delpwpolicy
{
    final private List<com.jts.fortress.pwpolicy.PswdPolicy> policies = new ArrayList<com.jts.fortress.pwpolicy.PswdPolicy>();

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
    public void addPolicy(com.jts.fortress.pwpolicy.PswdPolicy policy)
    {
        this.policies.add(policy);
    }

    /**
     * Used by {@link FortressAntTask#deletePolicies()} to retrieve list of PswdPolicy as defined in input xml file.
     *
     * @return collection containing {@link com.jts.fortress.pwpolicy.PswdPolicy}s targeted for removal.
     */
    public List<com.jts.fortress.pwpolicy.PswdPolicy> getPolicies()
    {
        return this.policies;
    }
}

