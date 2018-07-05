/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core.impl;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;

import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.fortress.core.model.Graphable;
import org.apache.directory.fortress.core.model.Hier;
import org.apache.directory.fortress.core.model.Relationship;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.ValidationException;


/**
 * This utility performs base hierarchical processing using this software <a href="http://www.jgrapht.org/">JGraphT</a></li>.
 * <p>
 * It is used to provide hierarchical processing APIs for the following data sets:
 * <ol>
 * <li>RBAC Role relations are stored in {@code cn=Hierarchies,ou=Roles,ou=RBAC} ldap node and cached as singleton in {@link RoleUtil}</li>
 * <li>ARBAC Admin Role relations are stored in {@code cn=Hierarchies,ou=AdminRoles,ou=ARBAC} ldap node and cached as singleton in {@link AdminRoleUtil}</li>
 * <li>User Organizational Unit relations are stored in {@code cn=Hierarchies,ou=OS-U,ou=ARBAC} node and cached as {@link org.apache.directory.fortress.core.impl.UsoUtil}</li>
 * <li>Permission Organizational Unit relations are stored in {@code cn=Hierarchies,ou=OS-P,ou=ARBAC} node and cached as {@link org.apache.directory.fortress.core.impl.PsoUtil}</li>
 * </ol>
 * This class...
 * <ol>
 * <li>manipulates data that is stored as singleton inside other classes with vertices of {@code String}, and edges, as {@link org.apache.directory.fortress.core.model.Relationship}s</li>
 * <li>utilizes open source library, see <a href="http://www.jgrapht.org/">JGraphT</a>.</li>
 * <li>processes general hierarchical data structure i.e. allows multiple inheritance with parents.</li>
 * <li>constructs and parses simple directed graphs.</li>
 * </ol>
 * Static methods on this class are intended for use by other Fortress classes, and cannot be directly invoked by outside programs.
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class HierUtil
{
    /**
     * Constants used within this class:
     */
    private static final String CLS_NM = HierUtil.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final String VERTEX = "Vertex";

    /**
     * The 'Type' attribute corresponds to what type of hierarchy is being referred to.
     */
    enum Type
    {
        ROLE,
        ARLE,
        USO,
        PSO
    }

    private static final Map<String, ReadWriteLock> synchMap = new HashMap<String, ReadWriteLock>();


    /**
     * Private constructor
     *
     */
    private HierUtil()
    {
    }

    /**
     * This api is used to determine parentage for Hierarchical processing.
     * It evaluates three relationship expressions:
     * <ol>
     * <li>If child equals parent</li>
     * <li>If mustExist true and parent-child relationship exists</li>
     * <li>If mustExist false and parent-child relationship does not exist</li>
     * </ol>
     * Method will throw {@link org.apache.directory.fortress.core.ValidationException} if rule check fails meaning caller failed validation
     * attempt to add/remove hierarchical relationship failed.
     *
     * @param graph     contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @param child     contains name of child.
     * @param parent    contains name of parent.
     * @param mustExist boolean is used to specify if relationship must be true.
     * @throws org.apache.directory.fortress.core.ValidationException
     *          in the event it fails one of the 3 checks.
     */
    static void validateRelationship( SimpleDirectedGraph<String, Relationship> graph, String child, String parent,
        boolean mustExist )
        throws ValidationException
    {
        // Ensure the two nodes aren't the same:
        if ( child.equalsIgnoreCase( parent ) )
        {
            String error = "validateRelationship child [" + child + "] same as parent [" + parent + "]";
            throw new ValidationException( GlobalErrIds.HIER_REL_INVLD, error );
        }
        Relationship rel = new Relationship( child.toUpperCase(), parent.toUpperCase() );
        // Ensure there is a valid child to parent relationship.
        if ( mustExist && !isRelationship( graph, rel ) )
        {
            String error = "validateRelationship child [" + child + "] does not have parent [" + parent + "]";
            throw new ValidationException( GlobalErrIds.HIER_REL_NOT_EXIST, error );
        }
        // Ensure the child doesn't already have the parent as an ascendant.
        else if ( !mustExist && isAscendant( child, parent, graph ) )
        {
            String error = "validateRelationship child [" + child + "] already has parent [" + parent + "]";
            throw new ValidationException( GlobalErrIds.HIER_REL_EXIST, error );
        }
        // Prevent cycles by making sure the child isn't an ascendant of parent.
        else if ( !mustExist && isDescedant( parent, child, graph ) )
        {
            String error = "validateRelationship child [" + child + "] is parent of [" + parent + "]";
            throw new ValidationException( GlobalErrIds.HIER_REL_CYCLIC, error );
        }
    }


    /**
     * This method converts from physical ldap entity format, {@link Hier} to logical {@code org.jgrapht.graph.SimpleDirectedGraph}.
     *
     * @param hier contains parent-child relationship in preparation to storing in ldap {@code ftRels} attribute of {@code ftHier} object class.
     * @return {@code org.jgrapht.graph.SimpleDirectedGraph} containing the vertices of {@code String}, and edges, as {@link Relationship}s that correspond to relational data.
     */
    private static SimpleDirectedGraph<String, Relationship> toGraph( Hier hier )
    {
        LOG.debug( "toGraph" );
        SimpleDirectedGraph<String, Relationship> graph =
            new SimpleDirectedGraph<>( Relationship.class );
        List<Relationship> edges = hier.getRelationships();
        if ( edges != null && edges.size() > 0 )
        {
            for ( Relationship edge : edges )
            {
                String child = edge.getChild();
                String parent = edge.getParent();

                try
                {
                    graph.addVertex( child );
                    graph.addVertex( parent );
                    graph.addEdge( child, parent, edge );
                }
                catch (java.lang.IllegalArgumentException e)
                {
                    String error = "toGraph child: " + child + " parent: " + parent + " caught IllegalArgumentException=" + e;
                    LOG.error( error );
                }

                LOG.debug( "toGraph child={}, parent={}", child, parent );
            }
        }
        return graph;
    }


    /**
     * This method is synchronized and adds an edge and its associated vertices to simple directed graph stored in static memory of this process.
     *
     * @param graph synchronized parameter contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @param relation contains parent-child relationship targeted for addition.
     * @return {@code org.jgrapht.graph.SimpleDirectedGraph} containing the vertices of {@code String}, and edges, as {@link Relationship}s that correspond to relational data.
     */
    private static void addEdge( SimpleDirectedGraph<String, Relationship> graph, Relationship relation )
    {
        LOG.debug( "addEdge" );
        synchronized ( graph )
        {
            graph.addVertex( relation.getChild().toUpperCase() );
            graph.addVertex( relation.getParent().toUpperCase() );
            graph.addEdge( relation.getChild().toUpperCase(), relation.getParent().toUpperCase(), relation );
        }
    }


    /**
     * This method is synchronized and removes an edge from a simple directed graph stored in static memory of this process.
     *
     * @param graph synchronized parameter contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @param relation contains parent-child relationship targeted for removal.
     * @return {@code org.jgrapht.graph.SimpleDirectedGraph} containing the vertices of {@code String}, and edges, as {@link Relationship}s that correspond to relational data.
     */
    private static void removeEdge( SimpleDirectedGraph<String, Relationship> graph, Relationship relation )
    {
        LOG.debug( "removeEdge" );
        synchronized ( graph )
        {
            graph.removeEdge( relation );
        }
    }


    /**
     * Return number of children (direct descendants) a given parent node has.
     *
     * @param name  contains the vertex of graph to gather descendants from.
     * @param graph contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @return int value contains the number of children of a given parent vertex.
     */
    static int numChildren( String name, SimpleDirectedGraph<String, Relationship> graph )
    {
        Map<String, String> vx = new HashMap<>();
        vx.put( VERTEX, name.toUpperCase() );
        return numChildren( vx, graph );
    }


    /**
     * Determine if parent-child relationship exists in supplied digraph.
     *
     * @param graph contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @param rel   contains parent and child names.
     * @return boolean value.  true indicates parent-child relationship exists in digraph.
     */
    private static boolean isRelationship( SimpleDirectedGraph<String, Relationship> graph, Relationship rel )
    {
        return graph.containsEdge( rel );
    }


    /**
     * Determine how many children a given parent node has.
     *
     * @param vertex of parent.
     * @param graph  contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @return
     */
    private static int numChildren( Map<String, String> vertex, SimpleDirectedGraph<String, Relationship> graph )
    {
        int numChildren = 0;
        try
        {
            String v = vertex.get( VERTEX );
            if ( v == null )
            {
                return 0;
            }
            LOG.debug( "hasChildren [{}]", v );
            numChildren = graph.inDegreeOf( v );
        }
        catch ( java.lang.IllegalArgumentException e )
        {
            // vertex is leaf.
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
    static Set<String> getAscendants( String childName, SimpleDirectedGraph<String, Relationship> graph )
    {
        Map<String, String> vx = new HashMap<>();
        // TreeSet will return in sorted order with case insensitive comparator:
        Set<String> parents = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        vx.put( VERTEX, childName.toUpperCase() );
        getAscendants( vx, graph, parents );
        return parents;
    }


    /**
     * Utility function recursively traverses a given digraph to build a set of all ascendant names.
     *
     * @param vertex     contains the position of the cursor for traversal of graph.
     * @param graph      contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @param ascendants contains the result set of ascendant names.
     * @return value contains the vertex of current position.
     */
    private static String getAscendants( Map<String, String> vertex, SimpleDirectedGraph<String, Relationship> graph,
        Set<String> ascendants )
    {
        String v = vertex.get( VERTEX );
        if ( v == null )
        {
            return null;
        }
        else if ( graph == null )
        {
            return null;
        }
        LOG.debug( "getAscendants [{}]", v);
        Set<Relationship> edges;
        try
        {
            edges = graph.outgoingEdgesOf( v );

        }
        catch ( java.lang.IllegalArgumentException iae )
        {
            // vertex is leaf.
            return null;
        }
        for ( Relationship edge : edges )
        {
            vertex.put( VERTEX, edge.getParent() );
            ascendants.add( edge.getParent() );
            v = getAscendants( vertex, graph, ascendants );
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
    static Set<String> getDescendants( String parentName, SimpleDirectedGraph<String, Relationship> graph )
    {
        Map<String, String> vx = new HashMap<>();
        // TreeSet will return in sorted order with case insensitive comparator:
        Set<String> children = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        vx.put( VERTEX, parentName.toUpperCase() );
        getDescendants( vx, graph, children );
        return children;
    }


    /**
     * Recursively traverse the hierarchical graph and determine child node contains a given parent as one of its ascendants.
     *
     * @param childName maps to vertex to determine parentage.
     * @param parentName maps to vertex to determine parentage.
     * @param graph      contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @return Set of names that are children of given parent.
     */
    private static boolean isAscendant( String childName, String parentName,
        SimpleDirectedGraph<String, Relationship> graph )
    {
        boolean isAscendant = false;
        Set<String> ascendants = getAscendants( childName, graph );
        if ( ascendants.contains( parentName ) )
        {
            isAscendant = true;
        }
        return isAscendant;
    }


    /**
     * Recursively traverse the hierarchical graph and determine if parent node contains a given child as one of its descendants.
     *
     * @param childName maps to vertex to determine parentage.
     * @param parentName maps to vertex to determine parentage.
     * @param graph      contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @return Set of names that are children of given parent.
     */
    private static boolean isDescedant( String childName, String parentName,
        SimpleDirectedGraph<String, Relationship> graph )
    {
        boolean isDescendant = false;
        Set<String> descendants = getDescendants( parentName, graph );
        if ( descendants.contains( childName ) )
        {
            isDescendant = true;
        }
        return isDescendant;
    }


    /**
     * Utility function recursively traverses a given digraph to build a set of all descendants names.
     *
     * @param vertex      contains the position of the cursor for traversal of graph.
     * @param graph       contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @param descendants contains the result set of names of all descendants of node.
     * @return value contains the vertex of current position.
     */
    private static String getDescendants( Map<String, String> vertex, SimpleDirectedGraph<String, Relationship> graph,
        Set<String> descendants )
    {
        String v = vertex.get( VERTEX );
        if ( v == null )
        {
            return null;
        }
        else if ( graph == null )
        {
            return null;
        }
        LOG.debug( "getDescendants [{}]", v);
        Set<Relationship> edges;
        try
        {
            edges = graph.incomingEdgesOf( v );
        }
        catch ( java.lang.IllegalArgumentException iae )
        {
            // vertex is leaf.
            return null;
        }
        for ( Relationship edge : edges )
        {
            vertex.put( VERTEX, edge.getChild() );
            descendants.add( edge.getChild() );
            v = getDescendants( vertex, graph, descendants );
        }
        return v;
    }


    /**
     * Utility function returns a set of all children (direct descendant) names.
     *
     * @param vertex contains the position of the cursor for traversal of graph.
     * @param graph  contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @return value contains the vertex of current position.
     */
    static Set<String> getChildren( String vertex, SimpleDirectedGraph<String, Relationship> graph )
    {
        Set<String> descendants = new HashSet<>();
        if ( graph == null )
        {
            return null;
        }

        LOG.debug( "getChildren [{}]", vertex );
        Set<Relationship> edges;
        try
        {
            edges = graph.incomingEdgesOf( vertex );
        }
        catch ( java.lang.IllegalArgumentException iae )
        {
            // vertex is leaf.
            return null;
        }
        for ( Relationship edge : edges )
        {
            descendants.add( edge.getChild() );
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
    static Set<String> getAscendants( String childName, String parentName, boolean isInclusive,
        SimpleDirectedGraph<String, Relationship> graph )
    {
        Map<String, String> vx = new HashMap<>();
        // TreeSet will return in sorted order with case insensitive comparator:
        Set<String> parents = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );

        vx.put( VERTEX, childName.toUpperCase() );
        getAscendants( vx, graph, parents, parentName, isInclusive );
        return parents;
    }


    /**
     * Private utility to recursively traverse the hierarchical graph and return all of the ascendants of a given child node.
     *
     * @param vertex      contains node name and acts as cursor for current location.
     * @param graph       contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @param parents     contains the result set of parent nodes.
     * @param stopName    contains the name of node where traversal ends.
     * @param isInclusive if set to true will include the parentName in the result set. False will not return specified parentName.
     * @return Set of names that are parents of given child.
     */
    private static String getAscendants( Map<String, String> vertex, SimpleDirectedGraph<String, Relationship> graph,
        Set<String> parents, String stopName, boolean isInclusive )
    {
        String v = vertex.get( VERTEX );
        if ( v == null )
        {
            return null;
        }
        else if ( graph == null )
        {
            return null;
        }
        LOG.debug( "getAscendants [{}]", v);
        Set<Relationship> edges;
        try
        {
            edges = graph.outgoingEdgesOf( v );
        }
        catch ( java.lang.IllegalArgumentException iae )
        {
            // vertex is leaf.
            return null;
        }
        for ( Relationship edge : edges )
        {
            if ( edge.getParent().equalsIgnoreCase( stopName ) )
            {
                if ( isInclusive )
                {
                    parents.add( edge.getParent() );
                }
                break;
            }
            else
            {
                vertex.put( VERTEX, edge.getParent() );
                parents.add( edge.getParent() );
                v = getAscendants( vertex, graph, parents, stopName, isInclusive );
            }
        }
        return v;
    }


    /**
     * Private utility to return the parents (direct ascendants) of a given child node.
     *
     * @param vertex contains node name and acts as cursor for current location.
     * @param graph  contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @return Set of names that are parents of given child.
     */
    static Set<String> getParents( String vertex, SimpleDirectedGraph<String, Relationship> graph )
    {
        Set<String> parents = new HashSet<>();
        if ( graph == null )
        {
            return null;
        }
        LOG.debug( "getParents [{}]", vertex);
        Set<Relationship> edges;
        try
        {
            edges = graph.outgoingEdgesOf( vertex );
        }
        catch ( java.lang.IllegalArgumentException iae )
        {
            // vertex is leaf.
            return null;
        }
        for ( Relationship edge : edges )
        {
            parents.add( edge.getParent() );
        }
        return parents;
    }


    /**
     * This method will retrieve the list of all parent-child relationships for a given node.  If the node was not found in
     * ldap this method will create a new node and store default data.
     * The following ldap nodes are currently storing hierarchical data:
     * <ol>
     * <li>RBAC Role relations are stored in {@code cn=Hierarchies,ou=Roles,ou=RBAC} ldap node and cached as singleton in {@link RoleUtil}</li>
     * <li>ARBAC Admin Role relations are stored in {@code cn=Hierarchies,ou=AdminRoles,ou=ARBAC} ldap node and cached as singleton in {@link AdminRoleUtil}</li>
     * <li>User Organizational Unit relations are stored in {@code cn=Hierarchies,ou=OS-U,ou=ARBAC} node and cached as {@link org.apache.directory.fortress.core.impl.UsoUtil}</li>
     * <li>Permission Organizational Unit relations are stored in {@code cn=Hierarchies,ou=OS-P,ou=ARBAC} node and cached as {@link org.apache.directory.fortress.core.impl.PsoUtil}</li>
     * </ol>
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return reference the the Hier result set retrieved from ldap.
     */
    static Hier loadHier( String contextId, List<Graphable> descendants )
    {
        Hier hier = new Hier();
        if ( CollectionUtils.isNotEmpty( descendants ) )
        {
            hier.setContextId( contextId );
            for ( Graphable descendant : descendants )
            {
                Set<String> parents = descendant.getParents();
                if ( CollectionUtils.isNotEmpty( parents ) )
                {
                    for ( String parent : parents )
                    {
                        Relationship relationship = new Relationship();
                        relationship.setChild( descendant.getName().toUpperCase() );
                        relationship.setParent( parent.toUpperCase() );
                        hier.setRelationship( relationship );
                    }
                }
            }
        }
        return hier;
    }


    /**
     * This api allows synchronized access to allow updates to hierarchical relationships.
     * Method will update the hierarchical data set and reload the JGraphT simple digraph with latest.
     *
     * @param graph contains a reference to simple digraph {@code org.jgrapht.graph.SimpleDirectedGraph}.
     * @param relationship contains parent-child relationship targeted for addition.
     * @param op   used to pass the ldap op {@link Hier.Op#ADD}, {@link Hier.Op#MOD}, {@link org.apache.directory.fortress.core.model.Hier.Op#REM}
     * @throws org.apache.directory.fortress.core.SecurityException in the event of a system error.
     */
    static void updateHier( SimpleDirectedGraph<String, Relationship> graph, Relationship relationship, Hier.Op op )
        throws SecurityException
    {
        if ( op == Hier.Op.ADD )
            HierUtil.addEdge( graph, relationship );
        else if ( op == Hier.Op.REM )
            HierUtil.removeEdge( graph, relationship );
        else
            throw new SecurityException( GlobalErrIds.HIER_CANNOT_PERFORM, CLS_NM
                + "updateHier Cannot perform hierarchical operation" );
    }


    /**
     * Method instantiates a new digraph, {@code org.jgrapht.graph.SimpleDirectedGraph}, using data passed in via
     * {@link Hier} entity.
     *
     * @param hier contains the source data for digraph.
     * @return reference to {@code org.jgrapht.graph.SimpleDirectedGraph}.
     */
    static SimpleDirectedGraph<String, Relationship> buildGraph( Hier hier )
    {
        SimpleDirectedGraph<String, Relationship> graph;
        LOG.debug( "buildGraph is initializing" );
        if ( hier == null )
        {
            String error = "buildGraph detected null hier=";
            LOG.error( error );
            return null;
        }
        graph = toGraph( hier );
        LOG.debug( "buildGraph is success" );
        return graph;
    }
}
