/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;


import java.util.Set;

/**
 * The Fortress Graphable interface prescribes attributes that are used to maintain implementor within a simple directed graph.
 * {@link com.jts.fortress.rbac.Role}, {@link com.jts.fortress.rbac.AdminRole}, {@link com.jts.fortress.rbac.OrgUnit} entities.
 * <p/>
 * <img src="../../../../images/HierRoleAscendants.png">
 * <p/>
 * <p/>
 * <img src="../../../../images/HierRoleDescendants.png">
 * <p/>
 * <p/>
 * <img src="../../../../images/HierRoleSimple.png">
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
 * @created October 21, 2012
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
     * @return name maps to attribute on 'organizationalUnit' object class.
     */
    public void setName(String name);
}