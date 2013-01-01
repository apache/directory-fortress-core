/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package com.jts.fortress.ant;

import com.jts.fortress.rbac.UserRole;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to load {@link com.jts.fortress.rbac.UserRole}s used to drive {@link com.jts.fortress.AdminMgr#deassignUser(com.jts.fortress.rbac.UserRole)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Deluserrole', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <deluserrole>
 *           ...
 *         </deluserrole>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 */
public class Deluserrole
{
    final private List<UserRole> userroles = new ArrayList<UserRole>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Deluserrole()
    {
    }

    /**
     * <p>This method name, 'addUserRole', is used for derived xml tag 'userrole' in the load script.</p>
     * <pre>
     * {@code
     * <deluserrole>
     *     <userrole userId="demoUser1" name="role1"/>
     *     <userrole userId="demoUser5" name="role1"/>
     *     <userrole userId="demoUser7" name="role1"/>
     *     <userrole userId="demoUser9" name="role1"/>
     * </deluserrole>
     * }
     * </pre>
     *
     * @param userRole contains reference to data element targeted for removal..
     */
    public void addUserRole(UserRole userRole)
    {
        this.userroles.add(userRole);
    }

    /**
     * Used by {@link FortressAntTask#delUserRoles()} to retrieve list of UserRoles as defined in input xml file.
     *
     * @return collection containing {@link com.jts.fortress.rbac.UserRole}s targeted for removal.
     */
    public List<UserRole> getUserRoles()
    {
        return this.userroles;
    }
}