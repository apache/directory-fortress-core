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

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to load {@link org.openldap.fortress.rbac.AdminRole}s used to drive {@link org.openldap.fortress.DelAdminMgr#addRole(org.openldap.fortress.rbac.AdminRole)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Addadminrole', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addadminrole>
 *           ...
 *         </addadminrole>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 */
public class Addadminrole
{
    final private List<AdminRoleAnt> roles = new ArrayList<>();

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
     * @param role contains extension of {@link org.openldap.fortress.rbac.AdminRole}.
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

