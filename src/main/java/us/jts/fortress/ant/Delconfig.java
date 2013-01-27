/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to load {@link ConfigAnt}s used to drive {@link us.jts.fortress.cfg.ConfigMgr#delete(String, java.util.Properties)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Delconfig', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <delconfig>
 *           ...
 *         </delconfig>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 */
public class Delconfig
{
    final private List<ConfigAnt> config = new ArrayList<ConfigAnt>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Delconfig()
    {
    }

    /**
     * <p>This method name, 'addConfig', is used for derived xml tag 'config' in the load script.</p>
     * <pre>
     * {@code
     * <delconfig>
     *     <config props="config.realm:DEFAULT"/>
     *     <config props="enable.audit:true"/>
     *     <config props="authn.type:default"/>
     *     <config props="password.policy:openldap"/>
     *     <config props="clientside.sorting:true"/>
     *     <config props="suffix:dc=jts\,dc=com"/>
     *     <config props="user.root:ou=People\,dc=jts\,dc=com"/>
     *     <config props="pwpolicy.root:ou=Policies\,dc=jts\,dc=com"/>
     *     <config props="role.root:ou=Roles\,ou=RBAC\,dc=jts\,dc=com"/>
     *     <config props="perm.root:ou=Permissions\,ou=RBAC\,dc=jts\,dc=com"/>
     *     <config props="sdconstraint.root:ou=Constraints\,ou=RBAC\,dc=jts\,dc=com"/>
     *     <config props="userou.root:ou=OS-U\,ou=ARBAC\,dc=jts\,dc=com"/>
     *     <config props="permou.root:ou=OS-P\,ou=ARBAC\,dc=jts\,dc=com"/>
     *     <config props="adminrole.root:ou=AdminRoles\,ou=ARBAC\,dc=jts\,dc=com"/>
     *     <config props="adminperm.root:ou=AdminPerms\,ou=ARBAC\,dc=jts\,dc=com"/>
     *     ...
     * </delconfig>
     * }
     * </pre>
     *
     * @param config contains reference to data element targeted for removal..
     */
    public void addConfig(ConfigAnt config)
    {
        this.config.add(config);
    }

    /**
     * Used by {@link FortressAntTask#deleteConfig()} to retrieve list of properties as defined in input xml file.
     *
     * @return collection containing {@link ConfigAnt}s targeted for removal.
     */
    public List<ConfigAnt> getConfig()
    {
        return this.config;
    }
}

