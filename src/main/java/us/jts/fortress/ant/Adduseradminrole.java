/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ant;

import us.jts.fortress.rbac.UserAdminRole;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to load {@link us.jts.fortress.rbac.UserAdminRole}s used to drive {@link us.jts.fortress.DelAdminMgr#assignUser(us.jts.fortress.rbac.UserAdminRole)}.
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
     * @return collection containing {@link us.jts.fortress.rbac.UserAdminRole}s targeted for insertion.
     */
    public List<UserAdminRole> getUserRoles()
    {
        return this.userroles;
    }
}

