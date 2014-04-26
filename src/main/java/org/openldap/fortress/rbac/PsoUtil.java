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


import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jgrapht.graph.SimpleDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.SecurityException;
import org.openldap.fortress.ValidationException;
import org.openldap.fortress.util.attr.VUtil;
import org.openldap.fortress.util.cache.Cache;
import org.openldap.fortress.util.cache.CacheMgr;


/**
 * This utility wraps {@link HierUtil} methods to provide hierarchical functionality using the {@link org.openldap.fortress.rbac.OrgUnit} data set
 * for Permissions, {@link org.openldap.fortress.rbac.OrgUnit.Type#PERM}.
 * The {@code cn=Hierarchies, ou=OS-P} data contains Permission OU pools and within a data cache, {@link #psoCache}, contained within this class.  The parent-child edges are contained in LDAP,
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
public final class PsoUtil
{
    private static final Cache psoCache;
    private static OrgUnitP orgUnitP = new OrgUnitP();
    private static final String CLS_NM = PsoUtil.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    /**
     * Initialize the Perm OU hierarchies.  This will read the {@link Hier} data set from ldap and load into
     * the JGraphT simple digraph that referenced statically within this class.
     */
    static
    {
        CacheMgr cacheMgr = CacheMgr.getInstance();
        psoCache = cacheMgr.getCache( "fortress.pso" );
    }


    /**
     * Recursively traverse the {@link org.openldap.fortress.rbac.OrgUnit} graph and return all of the descendants of a given parent {@link org.openldap.fortress.rbac.OrgUnit#name}.
     *
     * @param name      {@link org.openldap.fortress.rbac.OrgUnit#name} maps on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return Set of names of descendants {@link org.openldap.fortress.rbac.OrgUnit}s of given parent.
     */
    static Set<String> getDescendants( String name, String contextId )
    {
        return HierUtil.getDescendants( name, getGraph( contextId ) );
    }


    /**
     * Recursively traverse the {@link org.openldap.fortress.rbac.OrgUnit.Type#USER} graph and return all of the ascendants of a given child ou.
     *
     * @param name      maps to logical {@link org.openldap.fortress.rbac.OrgUnit#name} on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return Set of ou names that are ascendants of given child.
     */
    static Set<String> getAscendants( String name, String contextId )
    {
        return HierUtil.getAscendants( name, getGraph( contextId ) );
    }


    /**
     * Traverse one level of the {@link org.openldap.fortress.rbac.OrgUnit} graph and return all of the children (direct descendants) of a given parent {@link org.openldap.fortress.rbac.OrgUnit#name}.
     *
     * @param name      {@link org.openldap.fortress.rbac.OrgUnit#name} maps on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return Set of names of children {@link org.openldap.fortress.rbac.OrgUnit}s of given parent.
     */
    public static Set<String> getChildren( String name, String contextId )
    {
        return HierUtil.getChildren( name, getGraph( contextId ) );
    }


    /**
     * Traverse one level of the {@link org.openldap.fortress.rbac.OrgUnit.Type#USER} graph and return all of the parents (direct ascendants) of a given child ou.
     *
     * @param name      maps to logical {@link org.openldap.fortress.rbac.OrgUnit#name} on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return Set of ou names that are parents of given child.
     */
    static Set<String> getParents( String name, String contextId )
    {
        return HierUtil.getParents( name, getGraph( contextId ) );
    }


    /**
     * Recursively traverse the {@link org.openldap.fortress.rbac.OrgUnit.Type#PERM} graph and return number of children a given parent ou has.
     *
     * @param name      maps to logical {@link org.openldap.fortress.rbac.OrgUnit#name} on 'ftOrgUnit' object class.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return int value contains the number of children of a given parent ou.
     */
    static int numChildren( String name, String contextId )
    {
        return HierUtil.numChildren( name, getGraph( contextId ) );
    }


    /**
     * Return Set of {@link org.openldap.fortress.rbac.OrgUnit#name}s ascendants contained within {@link org.openldap.fortress.rbac.OrgUnit.Type#PERM}.
     *
     * @param ous       contains list of {@link org.openldap.fortress.rbac.OrgUnit}s.
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return contains Set of all descendants.
     */
    static Set<String> getInherited( List<OrgUnit> ous, String contextId )
    {
        // create Set with case insensitive comparator:
        Set<String> iOUs = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        if ( VUtil.isNotNullOrEmpty( ous ) )
        {
            for ( OrgUnit ou : ous )
            {
                String name = ou.getName();
                iOUs.add( name );
                Set<String> parents = HierUtil.getAscendants( name, getGraph( contextId ) );
                if ( VUtil.isNotNullOrEmpty( parents ) )
                    iOUs.addAll( parents );
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
     * Method will throw {@link org.openldap.fortress.ValidationException} if rule check fails meaning caller failed validation
     * attempt to add/remove hierarchical relationship failed.
     *
     * @param child     contains {@link org.openldap.fortress.rbac.OrgUnit#name} of child.
     * @param parent    contains {@link org.openldap.fortress.rbac.OrgUnit#name} of parent.
     * @param mustExist boolean is used to specify if relationship must be true.
     * @throws org.openldap.fortress.ValidationException
     *          in the event it fails one of the 3 checks.
     */
    static void validateRelationship( OrgUnit child, OrgUnit parent, boolean mustExist )
        throws ValidationException
    {
        HierUtil.validateRelationship( getGraph( child.getContextId() ), child.getName(), parent.getName(), mustExist );
    }


    /**
     * This api allows synchronized access to allow updates to hierarchical relationships.
     * Method will update the hierarchical data set and reload the JGraphT simple digraph with latest.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @param relationship contains parent-child relationship targeted for addition.
     * @param op   used to pass the ldap op {@link Hier.Op#ADD}, {@link Hier.Op#MOD}, {@link org.openldap.fortress.rbac.Hier.Op#REM}
     * @throws org.openldap.fortress.SecurityException in the event of a system error.
     */
    static void updateHier( String contextId, Relationship relationship, Hier.Op op ) throws SecurityException
    {
        HierUtil.updateHier( getGraph( contextId ), relationship, op );
    }


    /**
     * Read this ldap record,{@code cn=Hierarchies, ou=OS-P} into this entity, {@link Hier}, before loading into this collection class,{@code org.jgrapht.graph.SimpleDirectedGraph}
     * using 3rd party lib, <a href="http://www.jgrapht.org/">JGraphT</a>.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return
     */
    private static SimpleDirectedGraph<String, Relationship> loadGraph( String contextId )
    {
        Hier inHier = new Hier( Hier.Type.ROLE );
        inHier.setContextId( contextId );
        LOG.info( "loadGraph initializing PSO context [" + inHier.getContextId() + "]" );
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
            LOG.info( "loadGraph caught SecurityException=" + se );
        }
        Hier hier = HierUtil.loadHier( contextId, descendants );
        SimpleDirectedGraph<String, Relationship> graph;
        synchronized ( HierUtil.getLock( contextId, HierUtil.Type.PSO ) )
        {
            graph = HierUtil.buildGraph( hier );
        }
        psoCache.put( getKey( contextId ), graph );
        return graph;
    }


    /**
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return
     */
    private static SimpleDirectedGraph<String, Relationship> getGraph( String contextId )
    {
        SimpleDirectedGraph<String, Relationship> graph = ( SimpleDirectedGraph<String, Relationship> ) psoCache
            .get( getKey( contextId ) );
        if ( graph == null )
        {
            graph = loadGraph( contextId );
        }
        return graph;
    }


    /**
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     * @return
     */
    private static String getKey( String contextId )
    {
        String key = HierUtil.Type.PSO.toString();
        if ( VUtil.isNotNullOrEmpty( contextId ) && !contextId.equalsIgnoreCase( GlobalIds.NULL ) )
        {
            key += ":" + contextId;
        }
        return key;
    }
}
