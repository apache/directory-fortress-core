/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import com.jts.fortress.ldap.container.OrganizationalUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to create new {@link com.jts.fortress.ldap.container.OrganizationalUnit}s used to drive {@link com.jts.fortress.ldap.container.OrganizationalUnitP#add(com.jts.fortress.ldap.container.OrganizationalUnit)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Addcontainer', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addcontainer>
 *           ...
 *         </addcontainer>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author smckinn
 * @created January 21, 2010
 */
public class Addcontainer

{
    final private List<com.jts.fortress.ldap.container.OrganizationalUnit> containers = new ArrayList<com.jts.fortress.ldap.container.OrganizationalUnit>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Addcontainer()
    {
    }

    /**
     * <p>This method name, 'addContainer', is used for derived xml tag 'container' in the load script.</p>
     * <pre>
     * {@code
     * <addcontainer>
     *     <container name="Config" description="Fortress Configuration Realms"/>
     *     <container name="People" description="Fortress People"/>
     *     <container name="Policies" description="Fortress Policies"/>
     *     <container name="RBAC" description="Fortress RBAC Policies"/>
     *     <container name="Roles" parent="RBAC" description="Fortress Roles"/>
     *     <container name="Permissions" parent="RBAC" description="Fortress Permissions"/>
     *     <container name="Constraints" parent="RBAC" description="Fortress Separation of Duty Constraints"/>
     *     <container name="ARBAC" description="Fortress Administrative RBAC Policies"/>
     *     <container name="OS-U" parent="ARBAC" description="Fortress User Organizational Units"/>
     *     <container name="OS-P" parent="ARBAC" description="Fortress Perm Organizational Units"/>
     *     <container name="AdminRoles" parent="ARBAC" description="Fortress AdminRoles"/>
     *     <container name="AdminPerms" parent="ARBAC" description="Fortress Admin Permissions"/>
     * </addcontainer>
     * }
     * </pre>
     *
     * @param ou contains reference to data element targeted for insertion..
     */
    public void addContainer(com.jts.fortress.ldap.container.OrganizationalUnit ou)
    {
        this.containers.add(ou);
    }

    /**
     * Used by {@link FortressAntTask#addContainers()} to retrieve list of OrganizationalUnits as defined in input xml file.
     * @return collection containing {@link com.jts.fortress.ldap.container.OrganizationalUnit}s targeted for insertion.
     */
    public List<OrganizationalUnit> getContainers()
    {
        return this.containers;
    }
}

