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
import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.Graphable;
import org.apache.directory.fortress.core.model.Hier;
import org.apache.directory.fortress.core.model.Relationship;
import org.apache.directory.fortress.core.model.UserAdminRole;
import org.apache.directory.fortress.core.util.cache.Cache;
import org.apache.directory.fortress.core.util.cache.CacheMgr;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This utility wraps {@link org.apache.directory.fortress.core.impl.HierUtil} methods to provide hierarchical functionality for the {@link org.apache.directory.fortress.core.model.AdminRole} data set.
 * The child to parent relationships are stored within a data cache, {@link #adminRoleCache}, contained within this class.  The parent-child edges are contained in LDAP,
 * in {@code ftParents} attribute.  The ldap data is retrieved {@link org.apache.directory.fortress.core.impl.AdminRoleP#getAllDescendants(String)} and loaded into {@code org.jgrapht.graph.SimpleDirectedGraph}.
 * The graph...
 * <ol>
 * <li>is stored as singleton in this class with vertices of {@code String}, and edges, as {@link org.apache.directory.fortress.core.model.Relationship}s</li>
 * <li>utilizes open source library, see <a href="http://www.jgrapht.org/">JGraphT</a>.</li>
 * <li>contains a general hierarchical data structure i.e. allows multiple inheritance with parents.</li>
 * <li>is a simple directed graph thus does not allow cycles.</li>
 * </ol>
 * After update is performed to ldap, the singleton is refreshed with latest info.
 * <p>
 * Static methods on this class are intended for use by other Fortress classes, i.e. {@link DelAdminMgrImpl} and {@link org.apache.directory.fortress.core.impl.PermDAO}
 * and cannot be directly invoked by outside programs.
 * <p>
 * This class contains singleton that can be updated but is thread safe.

 *  @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class AdminRoleUtil
{
    private static final Cache adminRoleCache;
    private static final AdminRoleP adminRoleP = new AdminRoleP();
    private static final String CLS_NM = AdminRoleUtil.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    /**
     * Initialize the AdminRole hierarchies.  This will read the {@link org.apache.directory.fortress.core.model.Hier} data set from ldap and load into
     * the JGraphT simple digraph that referenced statically within this class.
     */
    static
    {
        CacheMgr cacheMgr = CacheMgr.getInstance();
        adminRoleCache = cacheMgr.getCache( "fortress.admin.roles" );
    }

    /**
     * Private constructor
     *
     */
    private AdminRoleUtil()
    {
    }

    /**
     * Used to determine if one {@link org.apache.directory.fortress.core.model.AdminRole} is the parent of another.  This method
     * will call recursive routine {@link #getAscendants(String, String)} to walk the {@code org.jgrapht.graph.SimpleDirectedGraph} data structure
     * returning flag indicating if parent-child relationship is valid.
     *
     * @param child maps to logical {@link org.apache.directory.fortress.core.model.AdminRole#name} on 'ftRls' object class.
     * @param parent maps to logical {@link org.apache.directory.fortress.core.model.AdminRole#name} on 'ftRls' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return boolean result, 'true' indicates parent/child relationship exists.
     */
    static boolean isParent( String child, String parent, String contextId )
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
     * Recursively traverse the {@link org.apache.directory.fortress.core.model.AdminRole} graph and return all of the descendants of a given parent {@link org.apache.directory.fortress.core.model.AdminRole#name}.
     * @param roleName {@link org.apache.directory.fortress.core.model.AdminRole#name} maps on 'ftRls' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of AdminRole names are children {@link org.apache.directory.fortress.core.model.AdminRole}s of given parent.
     */
    static Set<String> getDescendants( String roleName, String contextId )
    {
        return HierUtil.getDescendants( roleName, getGraph( contextId ) );
    }


    /**
     * Recursively traverse the hierarchical role graph and return all of the parents of a given child role.
     * @param roleName maps to logical {@link org.apache.directory.fortress.core.model.AdminRole#name} on 'ftRls' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of AdminRole names that are descendants of given node.
     */
    public static Set<String> getAscendants( String roleName, String contextId )
    {
        return HierUtil.getAscendants( roleName, getGraph( contextId ) );
    }


    /**
     * Traverse one level of the {@link org.apache.directory.fortress.core.model.AdminRole} graph and return all of the parents (direct ascendants) of a given parent {@link org.apache.directory.fortress.core.model.AdminRole#name}.
     * @param roleName {@link org.apache.directory.fortress.core.model.AdminRole#name} maps on 'ftRls' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of AdminRole names are parents {@link org.apache.directory.fortress.core.model.AdminRole}s of given child.
     */
    static Set<String> getParents( String roleName, String contextId )
    {
        return HierUtil.getParents( roleName, getGraph( contextId ) );
    }


    /**
     * Traverse one level of the hierarchical role graph and return all of the children (direct descendants) of a given parent role.
     * @param roleName maps to logical {@link org.apache.directory.fortress.core.model.AdminRole#name} on 'ftRls' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of AdminRole names that are children of given parent.
     */
    public static Set<String> getChildren( String roleName, String contextId )
    {
        return HierUtil.getChildren( roleName, getGraph( contextId ) );
    }


    /**
     * Return number of children (direct descendants) a given parent role has.
     * @param roleName maps to logical {@link org.apache.directory.fortress.core.model.AdminRole#name} on 'ftRls' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return int value contains the number of children of a given parent AdminRole.
     */
    static int numChildren( String roleName, String contextId )
    {
        return HierUtil.numChildren( roleName, getGraph( contextId ) );
    }


    /**
     * Return Set of {@link org.apache.directory.fortress.core.model.AdminRole#name}s ascendants.  Used by {@link org.apache.directory.fortress.core.impl.PermDAO#checkPermission}
     * for computing authorized {@link org.apache.directory.fortress.core.model.UserAdminRole#name}s.
     * @param uRoles contains list of adminRoles activated within a {@link org.apache.directory.fortress.core.model.User}'s {@link org.apache.directory.fortress.core.model.Session}.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return contains Set of all authorized adminRoles for a given User.
     */
    public static Set<String> getInheritedRoles( List<UserAdminRole> uRoles, String contextId )
    {
        // create Set with case insensitive comparator:
        Set<String> iRoles = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );

        if ( CollectionUtils.isNotEmpty( uRoles ) )
        {
            for ( UserAdminRole uRole : uRoles )
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
     * This api is used by {@link DelAdminMgrImpl} to determine parentage for Hierarchical ARBAC processing.
     * It calls {@link HierUtil#validateRelationship(org.jgrapht.graph.SimpleDirectedGraph, String, String, boolean)} to evaluate three adminRole relationship expressions:
     * <ol>
     * <li>If child equals parent</li>
     * <li>If mustExist true and parent-child relationship exists</li>
     * <li>If mustExist false and parent-child relationship does not exist</li>
     * </ol>
     * Method will throw {@link org.apache.directory.fortress.core.ValidationException} if rule check fails meaning caller failed validation
     * attempt to add/remove hierarchical relationship failed.
     *
     * @param childRole contains {@link org.apache.directory.fortress.core.model.AdminRole#name} of child.
     * @param parentRole contains {@link org.apache.directory.fortress.core.model.AdminRole#name} of parent.
     * @param mustExist boolean is used to specify if relationship must be true.
     * @throws org.apache.directory.fortress.core.ValidationException in the event it fails one of the 3 checks.
     */
    static void validateRelationship( AdminRole childRole, AdminRole parentRole, boolean mustExist )
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
    static void updateHier( String contextId, Relationship relationship, Hier.Op op ) throws SecurityException
    {
        HierUtil.updateHier( getGraph( contextId ), relationship, op );
    }


    /**
     * Read this ldap record,{@code cn=Hierarchies, ou=OS-P} into this entity, {@link Hier}, before loading into this collection class,{@code org.jgrapht.graph.SimpleDirectedGraph}
     * using 3rd party lib, <a href="http://www.jgrapht.org/">JGraphT</a>.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return handle to simple digraph containing adminRole hierarchies.
     */
    private static synchronized SimpleDirectedGraph<String, Relationship> loadGraph( String contextId )
    {
        Hier inHier = new Hier( Hier.Type.ROLE );
        inHier.setContextId( contextId );
        LOG.info( "loadGraph initializing ADMIN ROLE context [{}]", inHier.getContextId() );
        List<Graphable> descendants = null;

        try
        {
            descendants = adminRoleP.getAllDescendants( inHier.getContextId() );
        }
        catch ( SecurityException se )
        {
            LOG.info( "loadGraph caught SecurityException={}", se );
        }

        Hier hier = HierUtil.loadHier( contextId, descendants );
        SimpleDirectedGraph<String, Relationship> graph;

        graph = HierUtil.buildGraph( hier );
        adminRoleCache.put( getKey( contextId ), graph );

        return graph;
    }


    /**
     * Read this ldap record,{@code cn=Hierarchies, ou=OS-P} into this entity, {@link Hier}, before loading into this collection class,{@code org.jgrapht.graph.SimpleDirectedGraph}
     * using 3rd party lib, <a href="http://www.jgrapht.org/">JGraphT</a>.
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return handle to simple digraph containing adminRole hierarchies.
     */
    private static SimpleDirectedGraph<String, Relationship> getGraph( String contextId )
    {
        String key = getKey( contextId );        
        LOG.debug("Getting graph for key " + contextId);
         
        SimpleDirectedGraph<String, Relationship> graph = ( SimpleDirectedGraph<String, Relationship> ) adminRoleCache
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


    /**
     *
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return containing key to cache entry for this tenant.
     */
    private static String getKey( String contextId )
    {
        String key = HierUtil.Type.ARLE.toString();
        if ( StringUtils.isNotEmpty( contextId ) && !contextId.equalsIgnoreCase( GlobalIds.NULL ) )
        {
            key += ":" + contextId;
        }
        return key;
    }
}
