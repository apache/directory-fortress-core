/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to load {@link com.jts.fortress.arbac.AdminRole}s used to drive {@link com.jts.fortress.DelegatedAdminMgr#addRole(com.jts.fortress.arbac.AdminRole)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Addadminrole', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <OamAdmin>
 *         <addadminrole>
 *           ...
 *         </addadminrole>
 *     </OamAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author smckinn
 * @created December 18, 2010
 */
public class Addadminrole
{
    final private List<AdminRoleAnt> roles = new ArrayList<AdminRoleAnt>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Addadminrole()
    {
    }

    /**
     * <p>This method name, 'addRole', is used for derived xml tag 'role' in the load script.</p>
     * <pre>
     * {@code
     * <addadminrole>
     *      <role name="DemoAdminUsers"
     *          description="Test Admin Role for Demo"
     *          osps="demoapps1,demoapps2"
     *          osus="demousrs1,demousrs2"
     *          beginrange="role1"
     *          endrange="role1"
     *          begininclusive="true"
     *          endinclusive="true"/>
     * </addadminrole>
     * }
     * </pre>
     *
     * @param role contains extension of {@link com.jts.fortress.arbac.AdminRole}.
     */
    public void addRole(AdminRoleAnt role)
    {
        this.roles.add(role);
    }

    /**
     * Used by {@link FortressAntTask#addAdminRoles()} to retrieve list of AdminRoles as defined in input xml file.
     *
     * @return collection containing {@link AdminRoleAnt}s targeted for insertion.
     */
    public List<AdminRoleAnt> getRoles()
    {
        return this.roles;
    }
}

