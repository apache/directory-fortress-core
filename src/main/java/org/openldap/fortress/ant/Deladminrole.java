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
 * This class is used by {@link FortressAntTask} to load {@link org.openldap.fortress.rbac.AdminRole}s used to drive {@link org.openldap.fortress.DelAdminMgr#deleteRole(org.openldap.fortress.rbac.AdminRole)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>The class name, 'Deladminrole', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <deladminrole>
 *           ...
 *         </deladminrole>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 */
public class Deladminrole
{
	final private List<AdminRoleAnt> roles = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
	public Deladminrole() { }


    /**
     * <p>This method name, 'addRole', is used for the derived xml tag 'role' in the load script.</p>
     * <pre>
     * {@code
     * <deladminrole>
     *      <role name="DemoAdminUsers"
     * </deladminrole>
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
     * Used by {@link FortressAntTask#deleteAdminRoles()} to retrieve list of AdminRoles as defined in input xml file.
     * @return collection containing {@link AdminRoleAnt}s targeted for removal.
     */
	public List<AdminRoleAnt> getRoles()
	{
		return this.roles;
	}
}

