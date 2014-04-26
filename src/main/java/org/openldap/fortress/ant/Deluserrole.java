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

import org.openldap.fortress.rbac.UserRole;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to load {@link org.openldap.fortress.rbac.UserRole}s used to drive {@link org.openldap.fortress.AdminMgr#deassignUser(org.openldap.fortress.rbac.UserRole)}.
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
    final private List<UserRole> userroles = new ArrayList<>();

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
     * @return collection containing {@link org.openldap.fortress.rbac.UserRole}s targeted for removal.
     */
    public List<UserRole> getUserRoles()
    {
        return this.userroles;
    }
}