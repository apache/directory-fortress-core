/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import com.jts.fortress.rbac.Role;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to load {@link com.jts.fortress.rbac.Role}s used to drive {@link com.jts.fortress.AdminMgr#deleteRole(com.jts.fortress.rbac.Role)}}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Delrole', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <OamAdmin>
 *         <delrole>
 *           ...
 *         </delrole>
 *     </OamAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author smckinn
 * @created November 23, 2010
 */
public class Delrole
{
    final private List<Role> roles = new ArrayList<Role>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Delrole()
    {
    }

    /**
     * <p>This method name, 'addRole', is used for derived xml tag 'role' in the load script.</p>
     * <pre>
     * {@code
     * <delrole>
     *     <role name="role1"/>
     *     <role name="role2"/>
     * </delrole>
     * }
     * </pre>
     *
     * @param role contains reference to data element targeted for removal.
     */
    public void addRole(Role role)
    {
        this.roles.add(role);
    }

    /**
     * Used by {@link FortressAntTask#deleteRoles()} to retrieve list of Roles as defined in input xml file.
     *
     * @return collection containing {@link com.jts.fortress.rbac.Role}s targeted for removal.
     */
    public List<Role> getRoles()
    {
        return this.roles;
    }
}

