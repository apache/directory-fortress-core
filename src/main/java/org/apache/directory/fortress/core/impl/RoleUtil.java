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


import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.ValidationException;
import org.apache.directory.fortress.core.model.Graphable;
import org.apache.directory.fortress.core.model.Hier;
import org.apache.directory.fortress.core.model.ParentUtil;
import org.apache.directory.fortress.core.model.Relationship;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.util.cache.Cache;
import org.apache.directory.fortress.core.util.cache.CacheMgr;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This utility wraps {@link org.apache.directory.fortress.core.impl.HierUtil} methods to provide hierarchical functionality for the {@link org.apache.directory.fortress.core.model.Role} data set.
 * The {@code cn=Hierarchies, ou=Roles} data is stored within a cache, {@link #roleCache}, contained within this class.  The parent-child edges are contained in LDAP,
 * in {@code ftParents} attribute.  The ldap data is retrieved {@link org.apache.directory.fortress.core.impl.RoleP#getAllDescendants(String)} and loaded into {@code org.jgrapht.graph.SimpleDirectedGraph}.
 * The graph...
 * <ol>
 * <li>is stored as singleton in this class with vertices of {@code String}, and edges, as {@link org.apache.directory.fortress.core.model.Relationship}s</li>
 * <li>utilizes open source library, see <a href="http://www.jgrapht.org/">JGraphT</a>.</li>
 * <li>contains a general hierarchical data structure i.e. allows multiple inheritance with parents.</li>
 * <li>is a simple directed graph thus does not allow cycles.</li>
 * </ol>
 * After update is performed to ldap, the singleton is refreshed with latest info.
 * <p>
 * Static methods on this class are intended for use by other Fortress classes, i.e. {@link org.apache.directory.fortress.core.impl.UserDAO} and {@link org.apache.directory.fortress.core.impl.PermDAO}
 * and cannot be directly invoked by outside programs.
 * <p>
 * This class contains singleton that can be updated but is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class RoleUtil implements ParentUtil
{
    private Cache roleCache;
    private RoleP roleP = new RoleP();
    private static final String CLS_NM = RoleUtil.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    private static volatile RoleUtil sINSTANCE = null;

    static RoleUtil getInstance()
    {
        if(sINSTANCE == null)
        {
            synchronized (RoleUtil.class)
            {
                if(sINSTANCE == null)
                {
                    sINSTANCE = new RoleUtil();
                }
            }
        }
        return sINSTANCE;
    }
    
    private RoleUtil()
    {
        init();
    }
    
    /**
     * Initialize the Role hierarchies.  This will read the {@link org.apache.directory.fortress.core.model.Hier} data set from ldap and load into
     * the JGraphT simple digraph that referenced statically within this class.
     */
    private void init()
    {
        roleP = new RoleP();
    
        CacheMgr cacheMgr = CacheMgr.getInstance();
        roleCache = cacheMgr.getCache( "fortress.roles" );
    }


    /**
     * Used to determine if one {@link org.apache.directory.fortress.core.model.Role} is the parent of another.  This method
     * will call recursive routine {@link #getAscendants(String, String)} to walk the {@code org.jgrapht.graph.SimpleDirectedGraph} data structure
     * returning flag indicating if parent-child relationship is valid.
     *
     * @param child  maps to logical {@link org.apache.directory.fortress.core.model.Role#name} on 'ftRls' object class.
     * @param parent maps to logical {@link org.apache.directory.fortress.core.model.Role#name} on 'ftRels' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return boolean result, 'true' indicates parent/child relationship exists.
     */
    boolean isParent( String child, String parent, String contextId )
    {
        boolean result = false;
        Set<String> parents = getAscendants( child, contextId );
        if ( parents != null && parents.size() > 0 )
        {
            result = parents.contains( parent.toUpperCase() );
        }
        return result;
    }


    /**
     * Recursively traverse the {@link org.apache.directory.fortress.core.model.Role} graph and return all of the descendants of a given node {@link org.apache.directory.fortress.core.model.Role#name}.
     *
     * @param roleName {@link org.apache.directory.fortress.core.model.Role#name} on 'ftRls' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of Role names are descendants {@link org.apache.directory.fortress.core.model.Role}s of given parent.
     */
    Set<String> getDescendants( String roleName, String contextId )
    {
        return HierUtil.getDescendants( roleName.toUpperCase(), getGraph( contextId ) );
    }


    /**
     * Traverse the {@link org.apache.directory.fortress.core.model.Role} graph and return all children (direct descendants) of a given parent node {@link org.apache.directory.fortress.core.model.Role#name}.
     *
     * @param roleName {@link org.apache.directory.fortress.core.model.Role#name} on 'ftRls' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of Role names are children {@link org.apache.directory.fortress.core.model.Role}s of given parent.
     */
    Set<String> getChildren( String roleName, String contextId )
    {
        return HierUtil.getChildren( roleName.toUpperCase(), getGraph( contextId ) );
    }


    /**
     * Recursively traverse the hierarchical role graph and return all of the ascendants of a given role.
     *
     * @param roleName maps to logical {@link org.apache.directory.fortress.core.model.Role#name} on 'ftRls' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of Role names that are ascendants of given child.
     */
    Set<String> getAscendants( String roleName, String contextId )
    {
        return HierUtil.getAscendants( roleName.toUpperCase(), getGraph( contextId ) );
    }


    /**
     * Traverse the hierarchical role graph and return all of the parents (direct ascendants) of a given role.
     *
     * @param roleName maps to logical {@link org.apache.directory.fortress.core.model.Role#name} on 'ftRls' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of Role names that are parents of given child.
     */
    Set<String> getParents( String roleName, String contextId )
    {
        return HierUtil.getParents( roleName.toUpperCase(), getGraph( contextId ) );
    }


    /**
     * Traverse the hierarchical role graph and return all of the parents (direct ascendants) of a given role.
     *
     * @param roleName maps to logical {@link org.apache.directory.fortress.core.model.Role#name} on 'ftRls' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of Role names that are parents of given child.
     */
    public Set<String> getParentsCB( String roleName, String contextId )
    {
        return HierUtil.getParents( roleName.toUpperCase(), getGraph( contextId ) );
    }


    /**
     * Determine the number of children (direct descendants) a given parent role has.
     *
     * @param roleName maps to logical {@link org.apache.directory.fortress.core.model.Role#name} on 'ftRls' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return int value contains the number of children of a given parent nRole.
     */
    int numChildren( String roleName, String contextId )
    {
        return HierUtil.numChildren( roleName.toUpperCase(), getGraph( contextId ) );
    }


    /**
     * Return Set of RBAC {@link org.apache.directory.fortress.core.model.Role#name}s ascendants.  Used by {@link org.apache.directory.fortress.core.impl.PermDAO#checkPermission}
     * for computing authorized {@link org.apache.directory.fortress.core.model.UserRole#name}s.
     *
     * @param uRoles contains list of Roles activated within a {@link org.apache.directory.fortress.core.model.User}'s {@link org.apache.directory.fortress.core.model.Session}.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return contains Set of all authorized RBAC Roles for a given User.
     */
    Set<String> getInheritedRoles( List<UserRole> uRoles, String contextId )
    {
        // create Set with case insensitive comparator:
        Set<String> iRoles = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        if ( CollectionUtils.isNotEmpty( uRoles ) )
        {
            for ( UserRole uRole : uRoles )
            {
                String rleName = uRole.getName();
                iRoles.add( rleName );
                Set<String> parents = HierUtil.getAscendants( rleName, getGraph( contextId ) );
                if ( CollectionUtils.isNotEmpty( parents ) )
                {
                    iRoles.addAll( parents );
                }
            }
        }
        return iRoles;
    }


    /**
     *
     * @param roles
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return set of ascendant roles associated with this entry.
     */
    Set<String> getAscendantRoles( List<String> roles, String contextId )
    {
        // create Set with case insensitive comparator:
        Set<String> iRoles = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        if ( CollectionUtils.isNotEmpty( roles ) )
        {
            for ( String role : roles )
            {
                iRoles.add( role );
                Set<String> parents = HierUtil.getAscendants( role, getGraph( contextId ) );
                if ( CollectionUtils.isNotEmpty( parents ) )
                {
                    iRoles.addAll( parents );
                }
            }
        }
        return iRoles;
    }


    /**
     *
     * @param roles
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return set of descendant roles associated with this entry.
     */
    Set<String> getDescendantRoles( Set<String> roles, String contextId )
    {
        // create Set with case insensitive comparator:
        Set<String> iRoles = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        if ( CollectionUtils.isNotEmpty( roles ) )
        {
            for ( String role : roles )
            {
                iRoles.add( role );
                Set<String> children = HierUtil.getDescendants( role, getGraph( contextId ) );
                if ( CollectionUtils.isNotEmpty( children ) )
                {
                    iRoles.addAll( children );
                }
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
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of names that are parents of given child.
     */
    Set<String> getAscendants( String childName, String parentName, boolean isInclusive, String contextId )
    {
        return HierUtil.getAscendants( childName, parentName, isInclusive, getGraph( contextId ) );
    }


    /**
     * This api is used by {@link AdminMgrImpl} to determine parentage for Hierarchical RBAC processing.
     * It calls {@link HierUtil#validateRelationship(org.jgrapht.graph.SimpleDirectedGraph, String, String, boolean)} to evaluate three adminRole relationship expressions:
     * <ol>
     * <li>If child equals parent</li>
     * <li>If mustExist true and parent-child relationship exists</li>
     * <li>If mustExist false and parent-child relationship does not exist</li>
     * </ol>
     * Method will throw {@link org.apache.directory.fortress.core.ValidationException} if rule check fails meaning caller failed validation
     * attempt to add/remove hierarchical relationship failed.
     *
     * @param childRole  contains {@link org.apache.directory.fortress.core.model.Role#name} of child.
     * @param parentRole contains {@link org.apache.directory.fortress.core.model.Role#name} of parent.
     * @param mustExist  boolean is used to specify if relationship must be true.
     * @throws org.apache.directory.fortress.core.ValidationException
     *          in the event it fails one of the 3 checks.
     */
    void validateRelationship( Role childRole, Role parentRole, boolean mustExist )
        throws ValidationException
    {
        HierUtil.validateRelationship( getGraph( childRole.getContextId() ), childRole.getName(), parentRole.getName(),
            mustExist );
    }


    /**
     * This api allows synchronized access to allow updates to hierarchical relationships.
     * Method will update the hierarchical data set and reload the JGraphT simple digraph with latest.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @param relationship contains parent-child relationship targeted for addition.
     * @param op   used to pass the ldap op {@link org.apache.directory.fortress.core.model.Hier.Op#ADD}, {@link org.apache.directory.fortress.core.model.Hier.Op#MOD}, {@link org.apache.directory.fortress.core.model.Hier.Op#REM}
     * @throws org.apache.directory.fortress.core.SecurityException in the event of a system error.
     */
    void updateHier( String contextId, Relationship relationship, Hier.Op op ) throws SecurityException
    {
        HierUtil.updateHier( getGraph( contextId ), relationship, op );
    }


    /**
     * Read this ldap record,{@code cn=Hierarchies, ou=OS-P} into this entity, {@link Hier}, before loading into this collection class,{@code org.jgrapht.graph.SimpleDirectedGraph}
     * using 3rd party lib, <a href="http://www.jgrapht.org/">JGraphT</a>.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return handle to simple digraph containing role hierarchies.
     */
    private synchronized SimpleDirectedGraph<String, Relationship> loadGraph( String contextId )
    {
        Hier inHier = new Hier( Hier.Type.ROLE );
        inHier.setContextId( contextId );
        LOG.info( "loadGraph initializing ROLE context [{}]", inHier.getContextId() );
        List<Graphable> descendants = null;

        try
        {
            descendants = roleP.getAllDescendants( inHier.getContextId() );
        }
        catch ( SecurityException se )
        {
            LOG.info( "loadGraph caught SecurityException={}", se );
        }

        Hier hier = HierUtil.loadHier( contextId, descendants );
        SimpleDirectedGraph<String, Relationship> graph;

        graph = HierUtil.buildGraph( hier );
        roleCache.put( getKey( contextId ), graph );

        return graph;
    }


    /**
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return key to this tenant's cache entry.
     */
    private String getKey( String contextId )
    {
        String key = HierUtil.Type.ROLE.toString();

        if ( StringUtils.isNotEmpty( contextId ) && !contextId.equalsIgnoreCase( GlobalIds.NULL ) )
        {
            key += ":" + contextId;
        }

        return key;
    }


    /**
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return handle to simple digraph containing role hierarchies.
     */
    private SimpleDirectedGraph<String, Relationship> getGraph( String contextId )
    {
        String key = getKey( contextId );        
        LOG.debug("Getting graph for key " + contextId);
         
        SimpleDirectedGraph<String, Relationship> graph = ( SimpleDirectedGraph<String, Relationship> ) roleCache
                 .get( key );
             
        if(graph == null){
            LOG.debug("Graph was null, creating... " + contextId);
            return loadGraph( contextId );
        }
        else{
            LOG.debug("Graph found in cache, returning...");
            return graph;
        }
    }
}
