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
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.model.Relationship;
import org.apache.directory.fortress.core.util.cache.Cache;
import org.apache.directory.fortress.core.util.cache.CacheMgr;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This utility wraps {@link HierUtil} methods to provide hierarchical functionality using the {@link org.apache.directory.fortress.core.model.OrgUnit} data set
 * for Permissions, {@link org.apache.directory.fortress.core.model.OrgUnit.Type#PERM}.
 * The {@code cn=Hierarchies, ou=OS-P} data contains Permission OU pools and within a data cache, {@link #psoCache}, contained within this class.  The parent-child edges are contained in LDAP,
 * in {@code ftParents} attribute.  The ldap data is retrieved {@link OrgUnitP#getAllDescendants(org.apache.directory.fortress.core.model.OrgUnit)} and loaded into {@code org.jgrapht.graph.SimpleDirectedGraph}.
 * The graph...
 * <ol>
 * <li>is stored as singleton in this class with vertices of {@code String}, and edges, as {@link org.apache.directory.fortress.core.model.Relationship}s</li>
 * <li>utilizes open source library, see <a href="http://www.jgrapht.org/">JGraphT</a>.</li>
 * <li>contains a general hierarchical data structure i.e. allows multiple inheritance with parents.</li>
 * <li>is a simple directed graph thus does not allow cycles.</li>
 * </ol>
 * After update is performed to ldap, the singleton is refreshed with latest info.
 * <p>
 * Static methods on this class are intended for use by other Fortress classes, i.e. {@link DelAdminMgrImpl}.
 * and cannot be directly invoked by outside programs.
 * <p>
 * This class contains singleton that can be updated but is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class PsoUtil
{
    private Cache psoCache;
    private OrgUnitP orgUnitP;
    private static final String CLS_NM = PsoUtil.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    private static volatile PsoUtil sINSTANCE = null;

    static PsoUtil getInstance()
    {
        if(sINSTANCE == null)
        {
            synchronized (PsoUtil.class)
            {
                if(sINSTANCE == null)
                {
                    sINSTANCE = new PsoUtil();
                }
            }
        }
        return sINSTANCE;
    }
    
    private PsoUtil(){
        init();
    }
    
    /**
     * Initialize the Perm OU hierarchies.  This will read the {@link org.apache.directory.fortress.core.model.Hier} data set from ldap and load into
     * the JGraphT simple digraph that referenced statically within this class.
     */
    private void init()
    {
        orgUnitP = new OrgUnitP();
    
        CacheMgr cacheMgr = CacheMgr.getInstance();
        psoCache = cacheMgr.getCache( "fortress.pso" );                
    }


    /**
     * Recursively traverse the {@link org.apache.directory.fortress.core.model.OrgUnit} graph and return all of the descendants of a given parent {@link org.apache.directory.fortress.core.model.OrgUnit#name}.
     *
     * @param name      {@link org.apache.directory.fortress.core.model.OrgUnit#name} maps on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of names of descendants {@link org.apache.directory.fortress.core.model.OrgUnit}s of given parent.
     */
    Set<String> getDescendants( String name, String contextId )
    {
        return HierUtil.getDescendants( name, getGraph( contextId ) );
    }


    /**
     * Recursively traverse the {@link org.apache.directory.fortress.core.model.OrgUnit.Type#USER} graph and return all of the ascendants of a given child ou.
     *
     * @param name      maps to logical {@link org.apache.directory.fortress.core.model.OrgUnit#name} on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of ou names that are ascendants of given child.
     */
    Set<String> getAscendants( String name, String contextId )
    {
        return HierUtil.getAscendants( name, getGraph( contextId ) );
    }


    /**
     * Traverse one level of the {@link org.apache.directory.fortress.core.model.OrgUnit} graph and return all of the children (direct descendants) of a given parent {@link org.apache.directory.fortress.core.model.OrgUnit#name}.
     *
     * @param name      {@link org.apache.directory.fortress.core.model.OrgUnit#name} maps on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of names of children {@link org.apache.directory.fortress.core.model.OrgUnit}s of given parent.
     */
    public Set<String> getChildren( String name, String contextId )
    {
        return HierUtil.getChildren( name, getGraph( contextId ) );
    }


    /**
     * Traverse one level of the {@link org.apache.directory.fortress.core.model.OrgUnit.Type#USER} graph and return all of the parents (direct ascendants) of a given child ou.
     *
     * @param name      maps to logical {@link org.apache.directory.fortress.core.model.OrgUnit#name} on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return Set of ou names that are parents of given child.
     */
    Set<String> getParents( String name, String contextId )
    {
        return HierUtil.getParents( name, getGraph( contextId ) );
    }


    /**
     * Recursively traverse the {@link org.apache.directory.fortress.core.model.OrgUnit.Type#PERM} graph and return number of children a given parent ou has.
     *
     * @param name      maps to logical {@link org.apache.directory.fortress.core.model.OrgUnit#name} on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return int value contains the number of children of a given parent ou.
     */
    int numChildren( String name, String contextId )
    {
        return HierUtil.numChildren( name, getGraph( contextId ) );
    }


    /**
     * Return Set of {@link org.apache.directory.fortress.core.model.OrgUnit#name}s ascendants contained within {@link org.apache.directory.fortress.core.model.OrgUnit.Type#PERM}.
     *
     * @param ous       contains list of {@link org.apache.directory.fortress.core.model.OrgUnit}s.
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return contains Set of all descendants.
     */
    Set<String> getInherited( List<OrgUnit> ous, String contextId )
    {
        // create Set with case insensitive comparator:
        Set<String> iOUs = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        if ( CollectionUtils.isNotEmpty( ous ) )
        {
            for ( OrgUnit ou : ous )
            {
                String name = ou.getName();
                iOUs.add( name );
                Set<String> parents = HierUtil.getAscendants( name, getGraph( contextId ) );

                if ( CollectionUtils.isNotEmpty( parents ) )
                {
                    iOUs.addAll( parents );
                }
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
     * Method will throw {@link org.apache.directory.fortress.core.ValidationException} if rule check fails meaning caller failed validation
     * attempt to add/remove hierarchical relationship failed.
     *
     * @param child     contains {@link org.apache.directory.fortress.core.model.OrgUnit#name} of child.
     * @param parent    contains {@link org.apache.directory.fortress.core.model.OrgUnit#name} of parent.
     * @param mustExist boolean is used to specify if relationship must be true.
     * @throws org.apache.directory.fortress.core.ValidationException
     *          in the event it fails one of the 3 checks.
     */
    void validateRelationship( OrgUnit child, OrgUnit parent, boolean mustExist )
        throws ValidationException
    {
        HierUtil.validateRelationship( getGraph( child.getContextId() ), child.getName(), parent.getName(), mustExist );
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
     * @return handle to simple digraph containing perm ou hierarchies.
     */
    private synchronized SimpleDirectedGraph<String, Relationship> loadGraph( String contextId )
    {
        Hier inHier = new Hier( Hier.Type.ROLE );
        inHier.setContextId( contextId );
        LOG.info( "loadGraph initializing PSO context [{}]", inHier.getContextId() );
        List<Graphable> descendants = null;

        try
        {
            OrgUnit orgUnit = new OrgUnit();
            orgUnit.setType( OrgUnit.Type.PERM );
            orgUnit.setContextId( contextId );
            descendants = orgUnitP.getAllDescendants( orgUnit );
        }
        catch ( SecurityException se )
        {
            LOG.info( "loadGraph caught SecurityException={}", se );
        }

        Hier hier = HierUtil.loadHier( contextId, descendants );
        SimpleDirectedGraph<String, Relationship> graph;

        graph = HierUtil.buildGraph( hier );
        psoCache.put( getKey( contextId ), graph );

        return graph;
    }


    /**
     * @param contextId maps to sub-tree in DIT, e.g. ou=contextId, dc=example, dc=com.
     * @return handle to simple digraph containing perm ou hierarchies.
     */
    private SimpleDirectedGraph<String, Relationship> getGraph( String contextId )
    {
        String key = getKey( contextId );        
        LOG.debug("Getting graph for key " + contextId);
         
        SimpleDirectedGraph<String, Relationship> graph = ( SimpleDirectedGraph<String, Relationship> ) psoCache
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
     * @return key to this tenant's cache entry.
     */
    private static String getKey( String contextId )
    {
        String key = HierUtil.Type.PSO.toString();
        if ( StringUtils.isNotEmpty( contextId ) && !contextId.equalsIgnoreCase( GlobalIds.NULL ) )
        {
            key += ":" + contextId;
        }
        return key;
    }
}
