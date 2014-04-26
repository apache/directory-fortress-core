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

import org.openldap.fortress.rbac.UserAdminRole;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to load {@link org.openldap.fortress.rbac.UserAdminRole}s used to drive {@link org.openldap.fortress.DelAdminMgr#deassignUser(org.openldap.fortress.rbac.UserAdminRole)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Deluseradminrole', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <deluseradminrole>
 *           ...
 *         </deluseradminrole>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 */
public class Deluseradminrole
{
    final private List<UserAdminRole> userroles = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Deluseradminrole()
    {
    }

    /**
     * <p>This method name, 'addUserRole', is used for derived xml tag 'userrole' in the load script.</p>
     * <pre>
     * {@code
     * <deluseradminrole>
     *     <userrole userId="demouser4" name="DemoAdminUsers"/>
     * </deluseradminrole>
     * }
     * </pre>
     *
     * @param userRole contains reference to data element targeted for removal.
     */
    public void addUserRole(UserAdminRole userRole)
    {
        this.userroles.add(userRole);
    }

    /**
     * Used by {@link FortressAntTask#delUserAdminRoles()} to retrieve list of UserAdminRoles as defined in input xml file.
     *
     * @return collection containing {@link org.openldap.fortress.rbac.UserAdminRole}s targeted for removal.
     */
    public List<UserAdminRole> getUserRoles()
    {
        return this.userroles;
    }
}

