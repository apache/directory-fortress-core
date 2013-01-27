/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ant;

import us.jts.fortress.rbac.PwPolicy;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to load {@link us.jts.fortress.rbac.PwPolicy}s used to drive {@link us.jts.fortress.PwPolicyMgr#add(us.jts.fortress.rbac.PwPolicy)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Addpwpolicy', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addpwpolicy>
 *           ...
 *         </addpwpolicy>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 */
public class Addpwpolicy
{
    final private List<PwPolicy> policies = new ArrayList<PwPolicy>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Addpwpolicy()
    {
    }

    /**
     * <p>This method name, 'addPolicy', is used for derived xml tag 'policy' in the load script.</p>
     * <pre>
     * {@code
     * <addpwpolicy>
     *     <policy name="Test1"
     *         minAge="0"
     *         maxAge="2000000"
     *         inHistory="5"
     *         checkQuality="2"
     *         minLength="4"
     *         expireWarning="1000000"
     *         graceLoginLimit="3"
     *         lockout="true"
     *         lockoutDuration="0"
     *         maxFailure="3"
     *         failureCountInterval="0"
     *         mustChange="true"
     *         allowUserChange="true"
     *         safeModify="false" />
     * </addpwpolicy>
     * }
     * </pre>
     *
     * @param policy contains reference to data element targeted for insertion..
     */
    public void addPolicy(PwPolicy policy)
    {
        this.policies.add(policy);
    }

    /**
     * Used by {@link FortressAntTask#addPolicies()} to retrieve list of PwPolicy as defined in input xml file.
     *
     * @return collection containing {@link us.jts.fortress.rbac.PwPolicy}s targeted for insertion.
     */
    public List<PwPolicy> getPolicies()
    {
        return this.policies;
    }
}

