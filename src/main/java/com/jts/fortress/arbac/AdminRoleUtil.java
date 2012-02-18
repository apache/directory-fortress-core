/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.arbac;

import com.jts.fortress.ValidationException;
import com.jts.fortress.SecurityException;
import com.jts.fortress.hier.HierP;
import com.jts.fortress.util.AlphabeticalOrder;
import com.jts.fortress.hier.Hier;
import com.jts.fortress.hier.HierUtil;
import com.jts.fortress.hier.Relationship;

import com.jts.fortress.util.attr.VUtil;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This utility wraps {@link HierUtil} and {@link com.jts.fortress.hier.HierP} methods to provide hierarchical functionality for the {@link com.jts.fortress.arbac.AdminRole} data set.
 * The {@code cn=Hierarchies, ou=AdminRoles} data is stored as an instance variable, {@link #m_graph}, contained within this class.  The parent-child edges are contained in LDAP,
 * i.e. {@code cn=Hierarchies, ou=AdminRoles,...} which uses entity {@link Hier}.  The ldap data is retrieved {@link com.jts.fortress.hier.HierP#read(com.jts.fortress.hier.Hier.Type)} and loaded into {@code org.jgrapht.graph.SimpleDirectedGraph}.
 * The graph...
 * <ol>
 * <li>is stored as singleton in this class with vertices of {@code String}, and edges, as {@link com.jts.fortress.hier.Relationship}s</li>
 * <li>utilizes open source library, see <a href="http://www.jgrapht.org/">JGraphT</a>.</li>
 * <li>contains a general hierarchical data structure i.e. allows multiple inheritance with parents.</li>
 * <li>is a simple directed graph thus does not allow cycles.</li>
 * <li>is refreshed by reading this ldap record,{@code cn=Hierarchies, ou=AdminRoles} into this entity, {@link Hier}, before loading into this collection class,{@code org.jgrapht.graph.SimpleDirectedGraph} using 3rd party lib, <a href="http://www.jgrapht.org/">JGraphT</a>.
 * <li>can only be updated via the synchronized method {@link #updateHier} which may add, {@link com.jts.fortress.hier.Hier.Op#ADD}, change, {@link com.jts.fortress.hier.Hier.Op#MOD}, or delete, {@link com.jts.fortress.hier.Hier.Op#REM} parent-child relationships.</li>
 * </ol>
 * After update is performed to ldap, the singleton is refreshed with latest info.
 * <p/>
 * Public static methods on this class are intended for use by other Fortress classes, i.e. {@link com.jts.fortress.arbac.DelegatedAdminMgrImpl} and {@link com.jts.fortress.rbac.PermDAO}
 * but should not be directly invoked by outside programs.
 * <p/>
 * This class contains singleton that can be updated but is thread safe.
 * <p/>

 *  @author smckinn
 * @created December 17, 2010
 */
public class AdminRoleUtil
{
    // Is synchronized on update:
    private static SimpleDirectedGraph<String, Relationship> m_graph = null;

    /**
     * Initialize the AdminRole hierarchies.  This will read the {@link Hier} data set from ldap and load into
     * the JGraphT simple digraph that referenced statically within this class.
     */
    static
    {
        loadGraph();
    }

    /**
     * Used to determine if one {@link com.jts.fortress.arbac.AdminRole} is the parent of another.  This method
     * will call recursive routine {@link #getAscendants(String)} to walk the {@code org.jgrapht.graph.SimpleDirectedGraph} data structure
     * returning flag indicating if parent-child relationship is valid.
     *
     * @param child maps to logical {@link com.jts.fortress.arbac.AdminRole#name} and physical 'ftRels' attribute on 'ftHier' object class.
     * @param parent maps to logical {@link com.jts.fortress.arbac.AdminRole#name} and physical 'ftRels' attribute on 'ftHier' object class.
     * @return boolean result, 'true' indicates parent/child relationship exists.
     */
    public static boolean isParent(String child, String parent)
    {
        boolean result = false;
        Set<String> parents = getAscendants(child);
        if(parents != null && parents.size() > 0)
        {
            result = parents.contains(parent.toUpperCase());
        }
        return result;
    }

    /**
     * Recursively traverse the {@link com.jts.fortress.arbac.AdminRole} graph and return all of the descendants of a given parent {@link com.jts.fortress.arbac.AdminRole#name}.
     * @param roleName {@link com.jts.fortress.arbac.AdminRole#name} maps to 'ftRels' attribute on 'ftHier' object class.
     * @return Set of AdminRole names are children {@link com.jts.fortress.arbac.AdminRole}s of given parent.
     */
    public static Set<String> getDescendants(String roleName)
    {
        return HierUtil.getDescendants(roleName, m_graph);
    }

    /**
     * Recursively traverse the hierarchical role graph and return all of the parents of a given child role.
     * @param roleName maps to logical {@link com.jts.fortress.arbac.AdminRole#name} and physical 'ftRels' attribute on 'ftHier' object class.
     * @return Set of AdminRole names that are descendants of given node.
     */
    public static Set<String> getAscendants(String roleName)
    {
        return HierUtil.getAscendants(roleName, m_graph);
    }

    /**
     * Traverse one level of the {@link com.jts.fortress.arbac.AdminRole} graph and return all of the parents (direct ascendants) of a given parent {@link com.jts.fortress.arbac.AdminRole#name}.
     * @param roleName {@link com.jts.fortress.arbac.AdminRole#name} maps to 'ftRels' attribute on 'ftHier' object class.
     * @return Set of AdminRole names are parents {@link com.jts.fortress.arbac.AdminRole}s of given child.
     */
    public static Set<String> getParents(String roleName)
    {
        return HierUtil.getParents(roleName, m_graph);
    }

    /**
     * Traverse one level of the hierarchical role graph and return all of the children (direct descendants) of a given parent role.
     * @param roleName maps to logical {@link com.jts.fortress.arbac.AdminRole#name} and physical 'ftRels' attribute on 'ftHier' object class.
     * @return Set of AdminRole names that are children of given parent.
     */
    public static Set<String> getChildren(String roleName)
    {
        return HierUtil.getChildren(roleName, m_graph);
    }

    /**
     * Return number of children (direct descendants) a given parent role has.
     * @param roleName maps to logical {@link com.jts.fortress.arbac.AdminRole#name} and physical 'ftRels' attribute on 'ftHier' object class.
     * @return int value contains the number of children of a given parent AdminRole.
     */
    public static int numChildren(String roleName)
    {
        return HierUtil.numChildren(roleName, m_graph);
    }

    /**
     * Return Set of {@link com.jts.fortress.arbac.AdminRole#name}s ascendants.  Used by {@link com.jts.fortress.rbac.PermDAO#checkPermission}
     * for computing authorized {@link com.jts.fortress.arbac.UserAdminRole#name}s.
     * @param uRoles contains list of adminRoles activated within a {@link com.jts.fortress.rbac.User}'s {@link com.jts.fortress.rbac.Session}.
     * @return contains Set of all authorized adminRoles for a given User.
     */
    public static Set<String> getInheritedRoles(List<UserAdminRole> uRoles)
    {
        // create Set with case insensitive comparator:
        Set<String> iRoles = new TreeSet<String>(new AlphabeticalOrder());
        if (VUtil.isNotNullOrEmpty(uRoles))
        {
            for (UserAdminRole uRole : uRoles)
            {
                String rleName = uRole.getName();
                iRoles.add(rleName);
                Set<String> parents = HierUtil.getAscendants(rleName, m_graph);
                if (VUtil.isNotNullOrEmpty(parents))
                    iRoles.addAll(parents);
            }
        }
        return iRoles;
    }

    /**
     * This api is used by {@link com.jts.fortress.arbac.DelegatedAdminMgrImpl} to determine parentage for Hierarchical ARBAC processing.
     * It calls {@link HierUtil#validateRelationship(org.jgrapht.graph.SimpleDirectedGraph, String, String, boolean)} to evaluate three adminRole relationship expressions:
     * <ol>
     * <li>If child equals parent</li>
     * <li>If mustExist true and parent-child relationship exists</li>
     * <li>If mustExist false and parent-child relationship does not exist</li>
     * </ol>
     * Method will throw {@link com.jts.fortress.ValidationException} if rule check fails meaning caller failed validation
     * attempt to add/remove hierarchical relationship failed.
     *
     * @param childRole contains {@link com.jts.fortress.arbac.AdminRole#name} of child.
     * @param parentRole contains {@link com.jts.fortress.arbac.AdminRole#name} of parent.
     * @param mustExist boolean is used to specify if relationship must be true.
     * @throws com.jts.fortress.ValidationException in the event it fails one of the 3 checks.
     */
    public static void validateRelationship(AdminRole childRole, AdminRole parentRole, boolean mustExist)
        throws ValidationException
    {
        HierUtil.validateRelationship(m_graph, childRole.getName(), parentRole.getName(), mustExist);
    }

    /**
     * This api allows synchronized access to {@link HierUtil#validateRelationship(org.jgrapht.graph.SimpleDirectedGraph, String, String, boolean)}
     * to {@link com.jts.fortress.arbac.DelegatedAdminMgrImpl} to allow updates to AdminRole relationships.
     * Method will update the AdminRole container hierarchical data set and reload the JGraphT simple digraph with latest.
     * @param hier maps to 'ftRels' attribute on 'ftHier' object class.
     * @param op used to pass the ldap op {@link Hier.Op#ADD}, {@link Hier.Op#MOD}, {@link Hier.Op#REM}
     * @throws com.jts.fortress.SecurityException in the event of a system error.
     */
    public static void updateHier(Hier hier, Hier.Op op) throws SecurityException
    {
        HierP hp = new HierP();
        // todo: ensure this is the correct way to manage singleton object.
        // thread safe access to singleton object:
        synchronized (m_graph)
        {
            hp.update(hier, op);
            loadGraph();
        }
    }

    /**
     * Read this ldap record,{@code cn=Hierarchies, ou=AdminRoles} into this entity, {@link Hier}, before loading into this collection class,{@code org.jgrapht.graph.SimpleDirectedGraph}
     * using 3rd party lib, <a href="http://www.jgrapht.org/">JGraphT</a>.
     *
     */
    private static void loadGraph()
    {
        Hier hier = HierUtil.readHier(Hier.Type.AROLE);
        m_graph = HierUtil.buildGraph(hier);
    }
}
