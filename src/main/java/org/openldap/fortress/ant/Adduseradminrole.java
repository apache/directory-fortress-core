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
 * The class is used by {@link FortressAntTask} to load {@link org.openldap.fortress.rbac.UserAdminRole}s used to drive {@link org.openldap.fortress.DelAdminMgr#assignUser(org.openldap.fortress.rbac.UserAdminRole)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Adduseradminrole', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <adduseradminrole>
 *           ...
 *         </adduseradminrole>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 */
public class Adduseradminrole
{
    final private List<UserAdminRole> userroles = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Adduseradminrole()
    {
    }

    /**
     * <p>This method name, 'addUserRole', is used for derived xml tag 'userrole' in the load script.</p>
     * <pre>
     * {@code
     * <adduseradminrole>
     *     <userrole userId="demouser4"
     *         name="DemoAdminUsers"
     *         beginTime="0800"
     *         endTime="1700"
     *         beginDate="20110101"
     *         endDate="none"
     *         beginLockDate="none"
     *         endLockDate="none"
     *         dayMask="23456"
     *         timeout="15"/>
     * </adduseradminrole>
     * }
     * </pre>
     *
     * @param userRole contains reference to data element targeted for insertion..
     */
    public void addUserRole(UserAdminRole userRole)
    {
        this.userroles.add(userRole);
    }

    /**
     * Used by {@link FortressAntTask#addUserAdminRoles()} to retrieve list of UserAdminRoles as defined in input xml file.
     *
     * @return collection containing {@link org.openldap.fortress.rbac.UserAdminRole}s targeted for insertion.
     */
    public List<UserAdminRole> getUserRoles()
    {
        return this.userroles;
    }
}

