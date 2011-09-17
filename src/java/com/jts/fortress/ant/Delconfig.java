/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to load {@link ConfigAnt}s used to drive {@link com.jts.fortress.configuration.ConfigMgr#delete(String, java.util.Properties)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Delconfig', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <OamAdmin>
 *         <delconfig>
 *           ...
 *         </delconfig>
 *     </OamAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author smckinn
 * @created February 5, 2011
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
     *     <config props="configRealm:DEFAULT"/>
     *     <config props="enableAudit:true"/>
     *     <config props="authNType:default"/>
     *     <config props="pwPolicy:openldap"/>
     *     <config props="clientSideSorting:true"/>
     *     <config props="suffix:dc=jts\,dc=com"/>
     *     <config props="userRoot:ou=People\,dc=jts\,dc=com"/>
     *     <config props="pwPolicyRoot:ou=Policies\,dc=jts\,dc=com"/>
     *     <config props="roleRoot:ou=Roles\,ou=RBAC\,dc=jts\,dc=com"/>
     *     <config props="permRoot:ou=Permissions\,ou=RBAC\,dc=jts\,dc=com"/>
     *     <config props="sdRoot:ou=Constraints\,ou=RBAC\,dc=jts\,dc=com"/>
     *     <config props="userOrgUnitRoot:ou=OS-U\,ou=ARBAC\,dc=jts\,dc=com"/>
     *     <config props="permOrgUnitRoot:ou=OS-P\,ou=ARBAC\,dc=jts\,dc=com"/>
     *     <config props="adminRoleRoot:ou=AdminRoles\,ou=ARBAC\,dc=jts\,dc=com"/>
     *     <config props="adminPermRoot:ou=AdminPerms\,ou=ARBAC\,dc=jts\,dc=com"/>
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

