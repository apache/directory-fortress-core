/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to load {@link UserAnt}s used to drive {@link us.jts.fortress.AdminMgr#disableUser(us.jts.fortress.rbac.User)}.
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
    final private List<UserAnt> users = new ArrayList<UserAnt>();

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

