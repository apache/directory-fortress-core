/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import us.jts.fortress.GlobalIds;
import us.jts.fortress.ValidationException;
import us.jts.fortress.SecurityException;

import us.jts.fortress.util.attr.VUtil;
import us.jts.fortress.util.cache.CacheMgr;
import us.jts.fortress.util.cache.Cache;
import org.apache.log4j.Logger;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This utility wraps {@link HierUtil} methods to provide hierarchical functionality using the {@link us.jts.fortress.rbac.OrgUnit} data set
 * for Permissions, {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}.
 * The {@code cn=Hierarchies, ou=OS-P} data contains Permission OU pools and within a data cache, {@link #m_psoCache}, contained within this class.  The parent-child edges are contained in LDAP,
 * in {@code ftParents} attribute.  The ldap data is retrieved {@link OrgUnitP#getAllDescendants(OrgUnit)} and loaded into {@code org.jgrapht.graph.SimpleDirectedGraph}.
 * The graph...
 * <ol>
 * <li>is stored as singleton in this class with vertices of {@code String}, and edges, as {@link Relationship}s</li>
 * <li>utilizes open source library, see <a href="http://www.jgrapht.org/">JGraphT</a>.</li>
 * <li>contains a general hierarchical data structure i.e. allows multiple inheritance with parents.</li>
 * <li>is a simple directed graph thus does not allow cycles.</li>
 * </ol>
 * After update is performed to ldap, the singleton is refreshed with latest info.
 * <p/>
 * Static methods on this class are intended for use by other Fortress classes, i.e. {@link DelAdminMgrImpl}.
 * and cannot be directly invoked by outside programs.
 * <p/>
 * This class contains singleton that can be updated but is thread safe.
 * <p/>
 *
 * @author Shawn McKinney
 */
final class PsoUtil
{
    private static Cache m_psoCache;
    private static OrgUnitP orgUnitP = new OrgUnitP();
    private static final String FORTRESS_PSO = "fortress.pso";
    private static final String CLS_NM = PsoUtil.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);


    /**
     * Initialize the Perm OU hierarchies.  This will read the {@link Hier} data set from ldap and load into
     * the JGraphT simple digraph that referenced statically within this class.
     */
    static
    {
        CacheMgr cacheMgr = CacheMgr.getInstance();
        PsoUtil.m_psoCache = cacheMgr.getCache(FORTRESS_PSO);
        //loadGraph();
    }

    /**
     * Recursively traverse the {@link us.jts.fortress.rbac.OrgUnit} graph and return all of the descendants of a given parent {@link us.jts.fortress.rbac.OrgUnit#name}.
     *
     * @param name      {@link us.jts.fortress.rbac.OrgUnit#name} maps on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return Set of names of descendants {@link us.jts.fortress.rbac.OrgUnit}s of given parent.
     */
    static Set<String> getDescendants(String name, String contextId)
    {
        return HierUtil.getDescendants(name, getGraph(contextId));
    }

    /**
     * Recursively traverse the {@link us.jts.fortress.rbac.OrgUnit.Type#USER} graph and return all of the ascendants of a given child ou.
     *
     * @param name      maps to logical {@link us.jts.fortress.rbac.OrgUnit#name} on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return Set of ou names that are ascendants of given child.
     */
    static Set<String> getAscendants(String name, String contextId)
    {
        return HierUtil.getAscendants(name, getGraph(contextId));
    }

    /**
     * Traverse one level of the {@link us.jts.fortress.rbac.OrgUnit} graph and return all of the children (direct descendants) of a given parent {@link us.jts.fortress.rbac.OrgUnit#name}.
     *
     * @param name      {@link us.jts.fortress.rbac.OrgUnit#name} maps on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return Set of names of children {@link us.jts.fortress.rbac.OrgUnit}s of given parent.
     */
    static Set<String> getChildren(String name, String contextId)
    {
        return HierUtil.getChildren(name, getGraph(contextId));
    }

    /**
     * Traverse one level of the {@link us.jts.fortress.rbac.OrgUnit.Type#USER} graph and return all of the parents (direct ascendants) of a given child ou.
     *
     * @param name      maps to logical {@link us.jts.fortress.rbac.OrgUnit#name} on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return Set of ou names that are parents of given child.
     */
    static Set<String> getParents(String name, String contextId)
    {
        return HierUtil.getParents(name, getGraph(contextId));
    }

    /**
     * Recursively traverse the {@link us.jts.fortress.rbac.OrgUnit.Type#PERM} graph and return number of children a given parent ou has.
     *
     * @param name      maps to logical {@link us.jts.fortress.rbac.OrgUnit#name} on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return int value contains the number of children of a given parent ou.
     */
    static int numChildren(String name, String contextId)
    {
        return HierUtil.numChildren(name, getGraph(contextId));
    }

    /**
     * Return Set of {@link us.jts.fortress.rbac.OrgUnit#name}s ascendants contained within {@link us.jts.fortress.rbac.OrgUnit.Type#PERM}.
     *
     * @param ous       contains list of {@link us.jts.fortress.rbac.OrgUnit}s.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return contains Set of all descendants.
     */
    static Set<String> getInherited(List<OrgUnit> ous, String contextId)
    {
        // create Set with case insensitive comparator:
        Set<String> iOUs = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        if (VUtil.isNotNullOrEmpty(ous))
        {
            for (OrgUnit ou : ous)
            {
                String name = ou.getName();
                iOUs.add(name);
                Set<String> parents = HierUtil.getAscendants(name, getGraph(contextId));
                if (VUtil.isNotNullOrEmpty(parents))
                    iOUs.addAll(parents);
            }
        }
        return iOUs;
    }


    /**
     * This api is used by {@link DelAdminMgrImpl} to determine parentage for Permission OU processing.
     * It calls {@link HierUtil#validateRelationship(org.jgrapht.graph.SimpleDirectedGraph, String, String, boolean)} to evaluate three OU relationship expressions:
     * <ol>
     * <li>If child equals parent</li>
     * <li>If mustExist true and parent-child relationship exists</li>
     * <li>If mustExist false and parent-child relationship does not exist</li>
     * </ol>
     * Method will throw {@link us.jts.fortress.ValidationException} if rule check fails meaning caller failed validation
     * attempt to add/remove hierarchical relationship failed.
     *
     * @param child     contains {@link us.jts.fortress.rbac.OrgUnit#name} of child.
     * @param parent    contains {@link us.jts.fortress.rbac.OrgUnit#name} of parent.
     * @param mustExist boolean is used to specify if relationship must be true.
     * @throws us.jts.fortress.ValidationException
     *          in the event it fails one of the 3 checks.
     */
    static void validateRelationship(OrgUnit child, OrgUnit parent, boolean mustExist)
        throws ValidationException
    {
        HierUtil.validateRelationship(getGraph(child.getContextId()), child.getName(), parent.getName(), mustExist);
    }


    /**
     * This api allows synchronized access to allow updates to hierarchical relationships.
     * Method will update the hierarchical data set and reload the JGraphT simple digraph with latest.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param relationship contains parent-child relationship targeted for addition.
     * @param op   used to pass the ldap op {@link Hier.Op#ADD}, {@link Hier.Op#MOD}, {@link us.jts.fortress.rbac.Hier.Op#REM}
     * @throws us.jts.fortress.SecurityException in the event of a system error.
     */
    static void updateHier(String contextId, Relationship relationship, Hier.Op op) throws SecurityException
    {
        HierUtil.updateHier(getGraph(contextId), relationship, op);
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
        log.info(CLS_NM + ".loadGraph initializing PSO context [" + inHier.getContextId() + "]");
        List<Graphable> descendants = null;
        try
        {
            OrgUnit orgUnit = new OrgUnit();
            orgUnit.setType(OrgUnit.Type.PERM);
            orgUnit.setContextId(contextId);
            descendants = orgUnitP.getAllDescendants(orgUnit);
        }
        catch(SecurityException se)
        {
            log.info(CLS_NM + ".loadGraph caught SecurityException=" + se);
        }
        Hier hier = HierUtil.loadHier(contextId, descendants);
        SimpleDirectedGraph<String, Relationship> graph;
        synchronized (HierUtil.getLock(contextId, HierUtil.Type.PSO))
        {
            graph = HierUtil.buildGraph(hier);
        }
        m_psoCache.put(getKey(contextId), graph);
        return graph;
    }

    /**
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return
     */
    private static SimpleDirectedGraph<String, Relationship> getGraph(String contextId)
    {
        SimpleDirectedGraph<String, Relationship> graph = (SimpleDirectedGraph<String, Relationship>) m_psoCache.get(getKey(contextId));
        if (graph == null)
        {
            graph = loadGraph(contextId);
        }
        return graph;
    }

    /**
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return
     */
    private static String getKey(String contextId)
    {
        String key = HierUtil.Type.PSO.toString();
        if (VUtil.isNotNullOrEmpty(contextId) && !contextId.equalsIgnoreCase(GlobalIds.NULL))
        {
            key += ":" + contextId;
        }
        return key;
    }
}
