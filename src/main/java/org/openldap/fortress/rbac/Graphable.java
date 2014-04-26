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

package org.openldap.fortress.rbac;


import java.util.Set;

/**
 * The Fortress Graphable interface prescribes attributes that are used to maintain implementor within a simple directed graph.
 * {@link org.openldap.fortress.rbac.Role}, {@link org.openldap.fortress.rbac.AdminRole}, {@link org.openldap.fortress.rbac.OrgUnit} entities.
 * <p/>
 * <img src="../doc-files/HierRoleAscendants.png">
 * <p/>
 * <p/>
 * <img src="../doc-files/HierRoleDescendants.png">
 * <p/>
 * <p/>
 * <img src="../doc-files/HierRoleSimple.png">
 * <p/>
 * <h4>Manageable Schema</h4>
 * The entity maps to Fortress LDAP attributetype ( 1.3.6.1.4.1.1.38088.1.28
 * NAME 'ftParents'
 * DESC 'Fortress Parent Nodes'
 * EQUALITY caseIgnoreMatch
 * SUBSTR caseIgnoreSubstringsMatch
 * SYNTAX 1.3.6.1.4.1.1466.115.121.1.15 )
 * <p/>
 *
 * @author Shawn McKinney
 */
public interface Graphable
{
    /**
     * Get the names of roles that are parents (direct ascendants) of this role.
     *
     * @return Set of parent role names assigned to this role.
     */
    public Set<String> getParents();

    /**
     * Set the names of roles names that are parents (direct ascendants) of this role.
     *
     * @param parents contains the Set of parent role names assigned to this role.
     */
    public void setParents(Set<String> parents);

    /**
     * Set the occupant attribute with the contents of the User dn.
     *
     * @param parent maps to 'ftParents' attribute on 'ftRls' object class.
     */
    public void setParent(String parent);

    /**
     * Set the occupant attribute with the contents of the User dn.
     *
     * @param parent maps to 'ftParents' attribute on 'ftRls' object class.
     */
    public void delParent(String parent);

    /**
     * Get the name required attribute of the node.
     *
     * @return attribute maps to attribute  on 'organizationalUnit' object class.
     */
    public String getName();

    /**
     * Sets the required name attribute on the node.
     *
     */
    public void setName(String name);
}