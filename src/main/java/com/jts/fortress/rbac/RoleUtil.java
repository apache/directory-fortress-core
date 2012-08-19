/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.GlobalIds;
import com.jts.fortress.SecurityException;
import com.jts.fortress.ValidationException;

import com.jts.fortress.util.attr.VUtil;
import com.jts.fortress.util.cache.CacheMgr;
import com.jts.fortress.util.cache.Cache;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This utility wraps {@link HierUtil} and {@link HierP} methods to provide hierarchical functionality for the {@link com.jts.fortress.rbac.Role} data set.
 * The {@code cn=Hierarchies, ou=Roles} data is stored within a cache, {@link #m_roleCache}, contained within this class.  The parent-child edges are contained in LDAP,
 * i.e. {@code cn=Hierarchies, ou=Roles} which uses entity {@link Hier}.  The ldap data is retrieved {@link HierP#read(Hier)} and loaded into {@code org.jgrapht.graph.SimpleDirectedGraph}.
 * The graph...
 * <ol>
 * <li>is stored as singleton in this class with vertices of {@code String}, and edges, as {@link Relationship}s</li>
 * <li>utilizes open source library, see <a href="http://www.jgrapht.org/">JGraphT</a>.</li>
 * <li>contains a general hierarchical data structure i.e. allows multiple inheritance with parents.</li>
 * <li>is a simple directed graph thus does not allow cycles.</li>
 * <li>is refreshed by reading this ldap record,{@code cn=Hierarchies, ou=Roles} into this entity, {@link Hier}, before loading into this collection class,{@code org.jgrapht.graph.SimpleDirectedGraph} using 3rd party lib, <a href="http://www.jgrapht.org/">JGraphT</a>.
 * <li>can only be updated via the synchronized method {@link #updateHier} which may add, {@link com.jts.fortress.rbac.Hier.Op#ADD}, change, {@link com.jts.fortress.rbac.Hier.Op#MOD}, or delete, {@link com.jts.fortress.rbac.Hier.Op#REM} parent-child relationships.</li>
 * </ol>
 * After update is performed to ldap, the singleton is refreshed with latest info.
 * <p/>
 * Public static methods on this class are intended for use by other Fortress classes, i.e. {@link com.jts.fortress.rbac.UserDAO} and {@link com.jts.fortress.rbac.PermDAO}
 * and cannot be directly invoked by outside programs.
 * <p/>
 * This class contains singleton that can be updated but is thread safe.
 * <p/>

 *
 * @author Shawn McKinney
 * @created September 25, 2010
 */
final class RoleUtil
{
    private static Cache m_roleCache;
    private static final String ROLES = "roles";
    private static final String FORTRESS_ROLES = "fortress.roles";

    /**
     * Initialize the Role hierarchies.  This will read the {@link Hier} data set from ldap and load into
     * the JGraphT simple digraph that referenced statically within this class.
     */
    static
    {
        CacheMgr cacheMgr = CacheMgr.getInstance();
        RoleUtil.m_roleCache = cacheMgr.getCache(FORTRESS_ROLES);
    }

    /**
     * Used to determine if one {@link com.jts.fortress.rbac.Role} is the parent of another.  This method
     * will call recursive routine {@link #getAscendants(String, String)} to walk the {@code org.jgrapht.graph.SimpleDirectedGraph} data structure
     * returning flag indicating if parent-child relationship is valid.
     *
     * @param child  maps to logical {@link com.jts.fortress.rbac.Role#name} and physical 'ftRels' attribute on 'ftHier' object class.
     * @param parent maps to logical {@link com.jts.fortress.rbac.Role#name} and physical 'ftRels' attribute on 'ftHier' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return boolean result, 'true' indicates parent/child relationship exists.
     */
    static boolean isParent(String child, String parent, String contextId)
    {
        boolean result = false;
        Set<String> parents = getAscendants(child,contextId);
        if (parents != null && parents.size() > 0)
        {
            result = parents.contains(parent.toUpperCase());
        }
        return result;
    }

    /**
     * Recursively traverse the {@link com.jts.fortress.rbac.Role} graph and return all of the descendants of a given node {@link com.jts.fortress.rbac.Role#name}.
     *
     * @param roleName {@link com.jts.fortress.rbac.Role#name} maps to 'ftRels' attribute on 'ftHier' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return Set of Role names are descendants {@link com.jts.fortress.rbac.Role}s of given parent.
     */
    static Set<String> getDescendants(String roleName, String contextId)
    {
        return HierUtil.getDescendants(roleName, getGraph(contextId));
    }

    /**
     * Traverse the {@link com.jts.fortress.rbac.Role} graph and return all children (direct descendants) of a given parent node {@link com.jts.fortress.rbac.Role#name}.
     *
     * @param roleName {@link com.jts.fortress.rbac.Role#name} maps to 'ftRels' attribute on 'ftHier' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return Set of Role names are children {@link com.jts.fortress.rbac.Role}s of given parent.
     */
    static Set<String> getChildren(String roleName, String contextId)
    {
        return HierUtil.getChildren(roleName, getGraph(contextId));
    }

    /**
     * Recursively traverse the hierarchical role graph and return all of the ascendants of a given role.
     *
     * @param roleName maps to logical {@link com.jts.fortress.rbac.Role#name} and physical 'ftRels' attribute on 'ftHier' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return Set of Role names that are ascendants of given child.
     */
    static Set<String> getAscendants(String roleName, String contextId)
    {
        return HierUtil.getAscendants(roleName, getGraph(contextId));
    }

    /**
     * Traverse the hierarchical role graph and return all of the parents (direct ascendants) of a given role.
     *
     * @param roleName maps to logical {@link com.jts.fortress.rbac.Role#name} and physical 'ftRels' attribute on 'ftHier' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return Set of Role names that are parents of given child.
     */
    static Set<String> getParents(String roleName, String contextId)
    {
        return HierUtil.getParents(roleName, getGraph(contextId));
    }

    /**
     * Determine the number of children (direct descendants) a given parent role has.
     *
     * @param roleName maps to logical {@link com.jts.fortress.rbac.Role#name} and physical 'ftRels' attribute on 'ftHier' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return int value contains the number of children of a given parent nRole.
     */
    static int numChildren(String roleName, String contextId)
    {
        return HierUtil.numChildren(roleName, getGraph(contextId));
    }

    /**
     * Return Set of RBAC {@link com.jts.fortress.rbac.Role#name}s ascendants.  Used by {@link com.jts.fortress.rbac.PermDAO#checkPermission}
     * for computing authorized {@link com.jts.fortress.rbac.UserRole#name}s.
     *
     * @param uRoles contains list of Roles activated within a {@link com.jts.fortress.rbac.User}'s {@link com.jts.fortress.rbac.Session}.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return contains Set of all authorized RBAC Roles for a given User.
     */
    static Set<String> getInheritedRoles(List<UserRole> uRoles, String contextId)
    {
        // create Set with case insensitive comparator:
        Set<String> iRoles = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        if (VUtil.isNotNullOrEmpty(uRoles))
        {
            for (UserRole uRole : uRoles)
            {
                String rleName = uRole.getName();
                iRoles.add(rleName);
                Set<String> parents = HierUtil.getAscendants(rleName, getGraph(contextId));
                if (VUtil.isNotNullOrEmpty(parents))
                    iRoles.addAll(parents);
            }
        }
        return iRoles;
    }

    /**
     *
     * @param roles
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return
     */
    static Set<String> getAscendantRoles(List<String> roles, String contextId)
    {
        // create Set with case insensitive comparator:
        Set<String> iRoles = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        if (VUtil.isNotNullOrEmpty(roles))
        {
            for (String role : roles)
            {
                iRoles.add(role);
                Set<String> parents = HierUtil.getAscendants(role, getGraph(contextId));
                if (VUtil.isNotNullOrEmpty(parents))
                    iRoles.addAll(parents);
            }
        }
        return iRoles;
    }

    /**
     *
     * @param roles
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return
     */
    static Set<String> getDescendantRoles(Set<String> roles, String contextId)
    {
        // create Set with case insensitive comparator:
        Set<String> iRoles = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        if (VUtil.isNotNullOrEmpty(roles))
        {
            for (String role : roles)
            {
                iRoles.add(role);
                Set<String> children = HierUtil.getDescendants(role, getGraph(contextId));
                if (VUtil.isNotNullOrEmpty(children))
                    iRoles.addAll(children);
            }
        }
        return iRoles;
    }

    /**
     * Recursively traverse the hierarchical graph and return all of the ascendants of a given child node.
     *
     * @param childName   maps to vertex to determine parentage.
     * @param parentName  points to top most ascendant where traversal must stop.
     * @param isInclusive if set to true will include the parentName in the result set.  False will not return specified parentName.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return Set of names that are parents of given child.
     */
    static Set<String> getAscendants(String childName, String parentName, boolean isInclusive, String contextId)
    {
        return HierUtil.getAscendants(childName, parentName, isInclusive, getGraph(contextId));
    }


    /**
     * This api is used by {@link com.jts.fortress.rbac.AdminMgrImpl} to determine parentage for Hierarchical RBAC processing.
     * It calls {@link HierUtil#validateRelationship(org.jgrapht.graph.SimpleDirectedGraph, String, String, boolean)} to evaluate three adminRole relationship expressions:
     * <ol>
     * <li>If child equals parent</li>
     * <li>If mustExist true and parent-child relationship exists</li>
     * <li>If mustExist false and parent-child relationship does not exist</li>
     * </ol>
     * Method will throw {@link com.jts.fortress.ValidationException} if rule check fails meaning caller failed validation
     * attempt to add/remove hierarchical relationship failed.
     *
     * @param childRole  contains {@link com.jts.fortress.rbac.Role#name} of child.
     * @param parentRole contains {@link com.jts.fortress.rbac.Role#name} of parent.
     * @param mustExist  boolean is used to specify if relationship must be true.
     * @throws com.jts.fortress.ValidationException
     *          in the event it fails one of the 3 checks.
     */
    static void validateRelationship(Role childRole, Role parentRole, boolean mustExist)
        throws ValidationException
    {
        HierUtil.validateRelationship(getGraph(childRole.getContextId()), childRole.getName(), parentRole.getName(), mustExist);
    }

    /**
     * This api allows synchronized access to {@link HierUtil#validateRelationship(org.jgrapht.graph.SimpleDirectedGraph, String, String, boolean)}
     * to {@link com.jts.fortress.rbac.AdminMgrImpl} to allow updates to RBAC Role relationships.
     * Method will update the RBAC Role hierarchical data set and reload the JGraphT simple digraph with latest.
     *
     * @param hier maps to 'ftRels' attribute on 'ftHier' object class.
     * @param op   used to pass the ldap op {@link Hier.Op#ADD}, {@link Hier.Op#MOD}, {@link com.jts.fortress.rbac.Hier.Op#REM}
     * @throws com.jts.fortress.SecurityException in the event of a system error.
     */
    static void updateHier(Hier hier, Hier.Op op) throws SecurityException
    {
        HierP hp = new HierP();
        hp.update(hier, op);
        loadGraph(hier.getContextId());
    }

    /**
     * Read this ldap record,{@code cn=Hierarchies, ou=OS-P} into this entity, {@link Hier}, before loading into this collection class,{@code org.jgrapht.graph.SimpleDirectedGraph}
     * using 3rd party lib, <a href="http://www.jgrapht.org/">JGraphT</a>.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return
     */
    private static SimpleDirectedGraph<String, Relationship> loadGraph(String contextId)
    {
        Hier inHier = new Hier(Hier.Type.ROLE);
        inHier.setContextId(contextId);
        Hier hier = HierUtil.readHier(inHier);
        SimpleDirectedGraph<String, Relationship> graph = HierUtil.buildGraph(hier);
        m_roleCache.put(getKey(contextId), graph);
        return graph;
    }

    /**
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return
     */
    private static String getKey(String contextId)
    {
        String key = ROLES;
        if(VUtil.isNotNullOrEmpty(contextId) && !contextId.equalsIgnoreCase(GlobalIds.NULL))
        {
            key += ":" + contextId;
        }
        return key;
    }

    /**
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return
     */
    private static SimpleDirectedGraph<String, Relationship> getGraph(String contextId)
    {
        SimpleDirectedGraph<String, Relationship> graph = (SimpleDirectedGraph<String, Relationship>) m_roleCache.get(getKey(contextId));
        if (graph == null)
        {
            graph = loadGraph(contextId);
        }
        return graph;
    }
}
