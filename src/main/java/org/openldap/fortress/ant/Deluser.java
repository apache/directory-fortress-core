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
 * The class is used by {@link FortressAntTask} to load {@link UserAnt}s used to drive {@link org.openldap.fortress.AdminMgr#disableUser(org.openldap.fortress.rbac.User)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Deluser', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <deluser>
 *           ...
 *         </deluser>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 */
public class Deluser
{
    final private List<UserAnt> users = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Deluser()
    {
    }

    /**
     * <p>This method name, 'addUser', is used for derived xml tag 'user' in the load script.</p>
     * <pre>
     * {@code
     * <deluser>
     *     <user userId="demoUser1"/>
     *     <user userId="demoUser3"/>
     *     <user userId="demoUser4"/>
     *     <user userId="demoUser5"/>
     *     <user userId="demoUser7"/>
     *     <user userId="demoUser9"/>
     * </deluser>
     * }
     * </pre>
     *
     * @param user contains reference to data element targeted for removal.
     */
    public void addUser(UserAnt user)
    {
        this.users.add(user);
    }

    /**
     * Used by {@link FortressAntTask#deleteUsers()} to retrieve list of Users as defined in input xml file.
     *
     * @return collection containing {@link UserAnt}s targeted for removal.
     */
    public List<UserAnt> getUsers()
    {
        return this.users;
    }
}

