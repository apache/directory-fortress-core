/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.hier;

import com.jts.fortress.ValidationException;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.util.AlphabeticalOrder;
import org.apache.log4j.Logger;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * This utility performs base hierarchical processing using this software <a href="http://www.jgrapht.org/">JGraphT</a></li>.
 * </p>
 * It is used to provide hierarchical processing APIs for the following data sets:
 * <ol>
 * <li>RBAC Role relations are stored in {@code cn=Hierarchies,ou=Roles,ou=RBAC} ldap node and cached as singleton in {@link com.jts.fortress.rbac.RoleUtil}</li>
 * <li>ARBAC Admin Role relations are stored in {@code cn=Hierarchies,ou=AdminRoles,ou=ARBAC} ldap node and cached as singleton in {@link com.jts.fortress.arbac.AdminRoleUtil}</li>
 * <li>User Organizational Unit relations are stored in {@code cn=Hierarchies,ou=OS-U,ou=ARBAC} node and cached as {@link com.jts.fortress.arbac.UsoUtil}</li>
 * <li>Permission Organizational Unit relations are stored in {@code cn=Hierarchies,ou=OS-P,ou=ARBAC} node and cached as {@link com.jts.fortress.arbac.PsoUtil}</li>
 * </ol>
 * This class...
 * <ol>
 * <li>manipulates data that is stored as singleton inside other classes with vertices of {@code String}, and edges, as {@link com.jts.fortress.hier.Relationship}s</li>
 * <li>utilizes open source library, see <a href="http://www.jgrapht.org/">JGraphT</a>.</li>
 * <li>processes general hierarchical data structure i.e. allows multiple inheritance with parents.</li>
 * <li>constructs and parses simple directed graphs.</li>
 * </ol>
 * Public static methods on this class are intended for use by other Fortress classes, see above and should not be directly invoked by outside programs.
 * <p/>
 * This class is thread safe.
 * <p/>

 *
 * @author smckinn
 * @created July 10, 2010
 */
public class HierUtil
{
    /**
     * Constants used within this class:
     */
    private static final String OCLS_NM = HierUtil.class.getName();
    private static final Logger log = Logger.getLogger(OCLS_NM);
    private static final String VERTEX = "Vertex";
    private static final String DEFAULT_VERTEX_1 = "df1";
    private static final String DEFAULT_VERTEX_2 = "df2";

    /**
     * This api is used to determine parentage for Hierarchical processing.
     * It evaluates three relationship expressions:
     * <ol>
     * <li>If child equals parent</li>
     * <li>If mustExist true and parent-child relationship exists</li>
     * <li>If mustExist false and parent-child relationship does not exist</li>
     * </ol>
     * Method will throw {@link com.jts.fortress.ValidationException} if rule check fails meaning caller failed validation
     * attempt to add/remove hierarchical relationship failed.
     *
     * @param graph     contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @param child     contains name of child.
     * @param parent    contains name of parent.
     * @param mustExist boolean is used to specify if relationship must be true.
     * @throws com.jts.fortress.ValidationException in the event it fails one of the 3 checks.
     */
    public static void validateRelationship(SimpleDirectedGraph<String, Relationship> graph, String child, String parent, boolean mustExist)
        throws com.jts.fortress.ValidationException
    {
        if (child.equalsIgnoreCase(parent))
        {
            String error = OCLS_NM + ".validateRelationship child [" + child + "] same as parent [" + parent + "]";
            throw new ValidationException(GlobalErrIds.HIER_REL_INVLD, error);
        }
        Relationship rel = new Relationship(child.toUpperCase(), parent.toUpperCase());
        if (mustExist && !isRelationship(graph, rel))
        {
            String error = OCLS_NM + ".validateRelationship child [" + child + "] does not have parent [" + parent + "]";
            throw new com.jts.fortress.ValidationException(GlobalErrIds.HIER_REL_NOT_EXIST, error);
        }
        else if (!mustExist && isRelationship(graph, rel))
        {
            String error = OCLS_NM + ".validateRelationship child [" + child + "] already has parent [" + parent + "]";
            throw new com.jts.fortress.ValidationException(GlobalErrIds.HIER_REL_EXIST, error);
        }
    }

    /**
     * This method Convert from logical, {@code org.jgrapht.graph.SimpleDirectedGraph} to ldap entity, {@link Hier}.
     * The conversion iterates over all edges in the graph and loads the corresponding {@link com.jts.fortress.hier.Relationship} data
     * into the ldap entity.  The ldap entity stores this data physically in the {@code ftRels} attribute of {@code ftHier} object class.
     *
     * @param graph contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @return reference to hierarchical ldap entity {@link Hier}.
     */
    public static Hier toHier(SimpleDirectedGraph<String, Relationship> graph)
    {
        Hier he = new Hier();
        Set<Relationship> eSet = graph.edgeSet();
        for (Relationship edge : eSet)
        {
            he.setRelationship(edge);
        }
        return he;
    }

    /**
     * This method converts from physical ldap entity format, {@link Hier} to logical {@code org.jgrapht.graph.SimpleDirectedGraph}.
     *
     * @param hier contains parent-child relationship in preparation to storing in ldap {@code ftRels} attribute of {@code ftHier} object class.
     * @return {@code org.jgrapht.graph.SimpleDirectedGraph} containing the vertices of {@code String}, and edges, as {@link com.jts.fortress.hier.Relationship}s that correspond to relational data.
     */
    public static SimpleDirectedGraph<String, Relationship> toGraph(Hier hier)
        throws com.jts.fortress.SecurityException
    {
        log.debug(OCLS_NM + ".toGraph");
        SimpleDirectedGraph<String, Relationship> graph =
            new SimpleDirectedGraph<String, com.jts.fortress.hier.Relationship>(Relationship.class);
        List<Relationship> edges = hier.getRelationships();
        if (edges != null && edges.size() > 0)
        {
            for (Relationship edge : edges)
            {
                String child = edge.getChild();
                String parent = edge.getParent();
                graph.addVertex(child);
                graph.addVertex(parent);
                graph.addEdge(child, parent, edge);
                if (log.isDebugEnabled())
                    log.debug(OCLS_NM + ".toGraph child=" + child + " parent=" + parent);
            }
        }
        return graph;
    }

    /**
     * Return number of children (direct descendants) a given parent node has.
     *
     * @param name  contains the vertex of graph to gather descendants from.
     * @param graph contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @return int value contains the number of children of a given parent vertex.
     */
    public static int numChildren(String name, SimpleDirectedGraph<String, Relationship> graph)
    {
        Map vx = new HashMap();
        vx.put(VERTEX, name.toUpperCase());
        return numChildren(vx, graph);
    }

    /**
     * Determine if parent-child relationship exists in supplied digraph.
     *
     * @param graph contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @param rel   contains parent and child names.
     * @return boolean value.  true indicates parent-child relationship exists in digraph.
     */
    private static boolean isRelationship(SimpleDirectedGraph<String, Relationship> graph, com.jts.fortress.hier.Relationship rel)
    {
        return graph.containsEdge(rel);
    }

    /**
     * Determine how many children a given parent node has.
     *
     * @param vertex of parent.
     * @param graph  contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @return
     */
    private static int numChildren(Map vertex, SimpleDirectedGraph<String, Relationship> graph)
    {
        String v;
        int numChildren = 0;
        try
        {
            v = (String) vertex.get(VERTEX);
            if (v == null)
            {
                log.debug(OCLS_NM + ".getDescendants vertex is null");
                return 0;
            }
            if (log.isDebugEnabled())
                log.debug(OCLS_NM + ".hasChildren [" + v + "]");

            numChildren = graph.inDegreeOf(v);
        }
        catch (java.lang.IllegalArgumentException e)
        {
            log.debug(OCLS_NM + ".getDescendants vertex not found");
        }
        return numChildren;
    }

    /**
     * Recursively traverse the hierarchical graph and return all of the ascendants of a given node.
     *
     * @param childName maps to vertex to determine parentage.
     * @param graph     contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @return Set of names that are parents of given child.
     */
    public static Set<String> getAscendants(String childName, SimpleDirectedGraph<String, Relationship> graph)
    {
        Map vx = new HashMap();
        // TreeSet will return in sorted order:
        // create Set with case insensitive comparator:
        Set<String> parents = new TreeSet<String>(new AlphabeticalOrder());
        vx.put(VERTEX, childName.toUpperCase());
        getAscendants(vx, graph, parents);
        return parents;
    }

    /**
     * Utility function recursively traverses a given digraph to build a set of all ascendant names.
     *
     * @param vertex  contains the position of the cursor for traversal of graph.
     * @param graph   contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @param ascendants contains the result set of ascendant names.
     * @return value contains the vertex of current position.
     */
    private static String getAscendants(Map vertex, SimpleDirectedGraph<String, Relationship> graph, Set<String> ascendants)
    {
        String v;
        v = (String) vertex.get(VERTEX);
        if (v == null)
        {
            log.debug(OCLS_NM + ".getAscendants vertex is null");
            return null;
        }
        else if (graph == null)
        {
            log.debug(OCLS_NM + ".getAscendants graph is null");
            return null;
        }
        if (log.isDebugEnabled())
        {
            log.debug(OCLS_NM + ".getAscendants [" + v + "]");
        }
        Set<Relationship> edges;
        try
        {
            edges = graph.outgoingEdgesOf(v);
        }
        catch (java.lang.IllegalArgumentException iae)
        {
            if (log.isDebugEnabled())
            {
                log.debug(OCLS_NM + ".getAscendants no parent found");
            }
            return null;
        }
        for (Relationship edge : edges)
        {
            vertex.put(VERTEX, edge.getParent());
            ascendants.add(edge.getParent());
            v = getAscendants(vertex, graph, ascendants);
        }
        return v;
    }

    /**
     * Recursively traverse the hierarchical graph and return all of the descendants for a given node.
     *
     * @param parentName maps to vertex to determine parentage.
     * @param graph      contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @return Set of names that are children of given parent.
     */
    public static Set<String> getDescendants(String parentName, SimpleDirectedGraph<String, Relationship> graph)
    {
        Map vx = new HashMap();
        // TreeSet will return in sorted order:
        // create Set with case insensitive comparator:
        Set<String> children = new TreeSet<String>(new AlphabeticalOrder());

        vx.put(VERTEX, parentName.toUpperCase());
        getDescendants(vx, graph, children);
        return children;
    }


    /**
     * Utility function recursively traverses a given digraph to build a set of all descendants names.
     *
     * @param vertex   contains the position of the cursor for traversal of graph.
     * @param graph    contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @param descendants contains the result set of names of all descendants of node.
     * @return value contains the vertex of current position.
     */
    private static String getDescendants(Map vertex, SimpleDirectedGraph<String, Relationship> graph, Set<String> descendants)
    {
        String v;
        v = (String) vertex.get(VERTEX);
        if (v == null)
        {
            log.debug(OCLS_NM + ".getDescendants vertex is null");
            return null;
        }
        else if (graph == null)
        {
            log.debug(OCLS_NM + ".getDescendants graph is null");
            return null;
        }
        if (log.isDebugEnabled())
            log.debug(OCLS_NM + ".getDescendants [" + v + "]");

        Set<Relationship> edges;
        try
        {
            edges = graph.incomingEdgesOf(v);
        }
        catch (java.lang.IllegalArgumentException iae)
        {
            log.debug(OCLS_NM + ".getDescendants no parent found");
            return null;
        }
        for (Relationship edge : edges)
        {
            vertex.put(VERTEX, edge.getChild());
            descendants.add(edge.getChild());
            v = getDescendants(vertex, graph, descendants);
        }
        return v;
    }

    /**
     * Utility function returns a set of all children (direct descendant) names.
     *
     * @param vertex   contains the position of the cursor for traversal of graph.
     * @param graph    contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @return value contains the vertex of current position.
     */
    public static Set<String> getChildren(String vertex, SimpleDirectedGraph<String, Relationship> graph)
    {
        Set<String> descendants = new TreeSet<String>();
        if (graph == null)
        {
            log.debug(OCLS_NM + ".getChildren graph is null");
            return null;
        }
        if (log.isDebugEnabled())
            log.debug(OCLS_NM + ".getChildren [" + vertex + "]");

        Set<Relationship> edges;
        try
        {
            edges = graph.incomingEdgesOf(vertex);
        }
        catch (java.lang.IllegalArgumentException iae)
        {
            log.debug(OCLS_NM + ".getChildren no parent found");
            return null;
        }
        for (Relationship edge : edges)
        {
            descendants.add(edge.getChild());
        }
        return descendants;
    }

    /**
     * Recursively traverse the hierarchical graph and return all of the ascendants of a given node.
     *
     * @param childName   maps to vertex to determine parentage.
     * @param parentName  points to top most ascendant where traversal must stop.
     * @param isInclusive if set to true will include the parentName in the result set.  False will not return specified parentName.
     * @param graph       contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @return Set of names that are parents of given child.
     */
    public static Set<String> getAscendants(String childName, String parentName, boolean isInclusive, SimpleDirectedGraph<String, Relationship> graph)
    {
        Map vx = new HashMap();
        // TreeSet will return in sorted order:
        // create Set with case insensitive comparator:
        Set<String> parents = new TreeSet<String>(new AlphabeticalOrder());

        vx.put(VERTEX, childName.toUpperCase());
        getAscendants(vx, graph, parents, parentName, isInclusive);
        return parents;
    }

    /**
     * Private utility to recursively traverse the hierarchical graph and return all of the ascendants of a given child node.
     *
     * @param vertex      contains node name and acts as cursor for current location.
     * @param graph       contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @param parents     contains the result set of parent nodes.
     * @param stopName    contains the name of node where traversal ends.
     * @param isInclusive if set to true will include the parentName in the result set.  /home/smckinn/GIT/fortressDev/openldap-fortress-core/ldap/setup/HierarchicalRoleExample.xmlFalse will not return specified parentName.
     * @return Set of names that are parents of given child.
     */
    private static String getAscendants(Map vertex, SimpleDirectedGraph<String, Relationship> graph, Set<String> parents, String stopName, boolean isInclusive)
    {
        String v;
        v = (String) vertex.get(VERTEX);
        if (v == null)
        {
            log.debug(OCLS_NM + ".getAscendants vertex is null");
            return null;
        }
        else if (graph == null)
        {
            log.debug(OCLS_NM + ".getAscendants graph is null");
            return null;
        }
        if (log.isDebugEnabled())
        {
            log.debug(OCLS_NM + ".getAscendants [" + v + "]");
        }
        Set<Relationship> edges;
        try
        {
            edges = graph.outgoingEdgesOf(v);
        }
        catch (java.lang.IllegalArgumentException iae)
        {
            log.debug(OCLS_NM + ".getAscendants no parent found");
            return null;
        }
        for (Relationship edge : edges)
        {
            if (edge.getParent().equalsIgnoreCase(stopName))
            {
                if (isInclusive)
                {
                    parents.add(edge.getParent());
                }
                break;
            }
            else
            {
                vertex.put(VERTEX, edge.getParent());
                parents.add(edge.getParent());
                v = getAscendants(vertex, graph, parents, stopName, isInclusive);
            }
        }
        return v;
    }

    /**
     * Private utility to return the parents (direct ascendants) of a given child node.
     *
     * @param vertex      contains node name and acts as cursor for current location.
     * @param graph       contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @return Set of names that are parents of given child.
     */
    public static Set<String> getParents(String vertex, SimpleDirectedGraph<String, Relationship> graph)
    {
        Set<String> parents = new TreeSet<String>();
        if (graph == null)
        {
            log.debug(OCLS_NM + ".getParents graph is null");
            return null;
        }
        if (log.isDebugEnabled())
        {
            log.debug(OCLS_NM + ".getParents [" + vertex + "]");
        }
        Set<Relationship> edges;
        try
        {
            edges = graph.outgoingEdgesOf(vertex);
        }
        catch (java.lang.IllegalArgumentException iae)
        {
            log.debug(OCLS_NM + ".getParents no parent found");
            return null;
        }
        for (Relationship edge : edges)
        {
            parents.add(edge.getParent());
        }
        return parents;
    }

    /**
     * This method will retrieve the list of all parent-child relationships for a given node.  If the node was not found in
     * ldap this method will create a new node and store default data.
     * The following ldap nodes are currently storing hierarchical data:
     * <ol>
     * <li>RBAC Role relations are stored in {@code cn=Hierarchies,ou=Roles,ou=RBAC} ldap node and cached as singleton in {@link com.jts.fortress.rbac.RoleUtil}</li>
     * <li>ARBAC Admin Role relations are stored in {@code cn=Hierarchies,ou=AdminRoles,ou=ARBAC} ldap node and cached as singleton in {@link com.jts.fortress.arbac.AdminRoleUtil}</li>
     * <li>User Organizational Unit relations are stored in {@code cn=Hierarchies,ou=OS-U,ou=ARBAC} node and cached as {@link com.jts.fortress.arbac.UsoUtil}</li>
     * <li>Permission Organizational Unit relations are stored in {@code cn=Hierarchies,ou=OS-P,ou=ARBAC} node and cached as {@link com.jts.fortress.arbac.PsoUtil}</li>
     * </ol>
     *
     * @param type contains {@link Hier.Type#ROLE},{@link Hier.Type#AROLE}, {@link Hier.Type#USER}, {@link Hier.Type#PERM}
     * @return reference the the Hier result set retrieved from ldap.
     */
    public static Hier readHier(Hier.Type type)
    {
        HierP hp = new HierP();
        Hier hier = null;
        log.debug(OCLS_NM + ".readHier is initializing...");
        try
        {
            hier = hp.read(type);
        }
        catch (com.jts.fortress.SecurityException se)
        {
            if (se.getErrorId() == GlobalErrIds.HIER_NOT_FOUND)
            {
                log.info(OCLS_NM + ".readHier type [" + type + "] building default config...");
                SimpleDirectedGraph<String, Relationship> g =
                    new SimpleDirectedGraph<String, com.jts.fortress.hier.Relationship>(Relationship.class);

                //todo: must add edge to JGraph to workaround init problem.  fix me:
                String dflt1 = DEFAULT_VERTEX_1;
                String dflt2 = DEFAULT_VERTEX_2;
                g.addVertex(dflt1);
                g.addVertex(dflt2);
                g.addEdge(dflt1, dflt2, new Relationship(dflt1, dflt2));
                try
                {
                    hier = HierUtil.toHier(g);
                    hier.setType(type);
                    hier = hp.add(hier);
                    //hier = hp.read(type);
                }
                catch (com.jts.fortress.SecurityException sec)
                {
                    String error = OCLS_NM + ".readHier type [" + type + "] failed default config load, SecurityException=" + sec;
                    log.error(error);
                }
            }
            else
            {
                String error = OCLS_NM + ".readHier type [" + type + "] SecurityException=" + se;
                log.error(error);
            }
        }
        if (hier != null)
        {
            log.debug(OCLS_NM + ".readHier type [" + type + "] success");
        }
        else
        {
            String warning = OCLS_NM + ".readHier type [" + type + "] failed.";
            log.warn(warning);
        }
        return hier;
    }

    /**
     * Method instantiates a new digraph, {@code org.jgrapht.graph.SimpleDirectedGraph}, using data passed in via
     * {@link Hier} entity.
     *
     * @param hier contains the source data for digraph.
     * @return reference to {@code org.jgrapht.graph.SimpleDirectedGraph}.
     */
    public static SimpleDirectedGraph<String, Relationship> buildGraph(Hier hier)
    {
        SimpleDirectedGraph<String, Relationship> graph = null;
        log.debug(OCLS_NM + ".buildGraph is initializing");
        if (hier == null)
        {
            String error = OCLS_NM + ".buildGraph detected null hier=";
            log.error(error);
            return null;
        }
        try
        {
            graph = toGraph(hier);
            log.debug(OCLS_NM + ".buildGraph success to toGraph");
        }
        catch (com.jts.fortress.SecurityException se)
        {
            String error = OCLS_NM + ".buildGraph caught SecurityException=" + se;
            log.error(error);
        }
        log.debug(OCLS_NM + ".buildGraph is success");
        return graph;
    }
}
