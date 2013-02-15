/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;


/**
 * This class is used by {@link FortressAntTask} to load {@link us.jts.fortress.rbac.AdminRole}s used to drive {@link us.jts.fortress.DelAdminMgr#deleteRole(us.jts.fortress.rbac.AdminRole)}.
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
     * @param role contains extension of {@link us.jts.fortress.rbac.AdminRole}.
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

