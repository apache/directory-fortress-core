/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import com.jts.fortress.rbac.UserRole;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to load {@link com.jts.fortress.rbac.UserRole}s used to drive {@link com.jts.fortress.AdminMgr#assignUser(com.jts.fortress.rbac.UserRole)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Adduserrole', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <adduserrole>
 *           ...
 *         </adduserrole>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author smckinn
 * @created January 28, 2010
 */
public class Adduserrole
{
    final private List<UserRole> userroles = new ArrayList<UserRole>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Adduserrole()
    {
    }

    /**
     * <p>This method name, 'addUserRole', is used for derived xml tag 'userrole' in the load script.</p>
     * <pre>
     * {@code
     * <adduserrole>
     *     <userrole userId="demoUser1" name="role1" beginTime="0800" endTime="0700" beginDate="20110101" endDate="none" beginLockDate="none" endLockDate="none" dayMask="23456" timeout="30"/>
     *     <!-- Bad - role end time -->
     *     <userrole userId="demoUser5" name="role1"  beginTime="0700" endTime="0800" beginDate="20100101" endDate="21000101" beginLockDate="none" endLockDate="none" dayMask="all" timeout="0"/>
     *     <!-- Bad - role  begin date -->
     *     <userrole userId="demoUser7" name="role1"  beginTime="0000" endTime="0000" beginDate="20110110" endDate="21000101" beginLockDate="none" endLockDate="none" dayMask="all" timeout="0"/>
     *     <!-- Bad - role  end date -->
     *     <userrole userId="demoUser9" name="role1"  beginTime="0000" endTime="0000" beginDate="20100101" endDate="20100608" beginLockDate="none" endLockDate="none" dayMask="all" timeout="0"/>
     * </adduserrole>
     * }
     * </pre>
     *
     * @param userRole contains reference to data element targeted for insertion..
     */
    public void addUserRole(UserRole userRole)
    {
        this.userroles.add(userRole);
    }

    /**
     * Used by {@link FortressAntTask#addUserRoles()} to retrieve list of UserRoles as defined in input xml file.
     *
     * @return collection containing {@link com.jts.fortress.rbac.UserRole}s targeted for insertion.
     */
    public List<UserRole> getUserRoles()
    {
        return this.userroles;
    }
}

