/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import com.jts.fortress.rbac.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to load {@link com.jts.fortress.rbac.Role}s used to drive {@link com.jts.fortress.AdminMgr#addRole(com.jts.fortress.rbac.Role)}}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Addrole', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <OamAdmin>
 *         <addrole>
 *           ...
 *         </addrole>
 *     </OamAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author smckinn
 * @created November 23, 2010
 */
public class Addrole
{
    final private List<Role> roles = new ArrayList<Role>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Addrole()
    {
    }

    /**
     * <p>This method name, 'addRole', is used for derived xml tag 'role' in the load script.</p>
     * <pre>
     * {@code
     * <addrole>
     *     <role name="role1" description="Tomcat Role for Calendar App" beginTime="0800" endTime="1700" beginDate="20110101" endDate="20111231" beginLockDate="20110601" endLockDate="20110615" dayMask="23456" timeout="60"/>
     *     <role name="role2" description="Tomcat Role 2 for Calendar App"/>
     * </addrole>
     * }
     * </pre>
     *
     * @param role contains reference to data element targeted for insertion..
     */
    public void addRole(Role role)
    {
        this.roles.add(role);
    }

    /**
     * Used by {@link FortressAntTask#addRoles()} to retrieve list of Roles as defined in input xml file.
     *
     * @return collection containing {@link com.jts.fortress.rbac.Role}s targeted for insertion.
     */
    public List<Role> getRoles()
    {
        return this.roles;
    }
}

