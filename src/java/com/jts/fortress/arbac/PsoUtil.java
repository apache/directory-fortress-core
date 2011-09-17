/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.arbac;

import com.jts.fortress.ValidationException;
import com.jts.fortress.SecurityException;
import com.jts.fortress.hier.Hier;
import com.jts.fortress.hier.HierP;
import com.jts.fortress.hier.Relationship;
import com.jts.fortress.util.AlphabeticalOrder;
import com.jts.fortress.hier.HierUtil;

import com.jts.fortress.util.attr.VUtil;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This utility wraps {@link com.jts.fortress.hier.HierUtil} and {@link com.jts.fortress.hier.HierP} methods to provide hierarchical functionality using the {@link com.jts.fortress.arbac.OrgUnit} data set
 * for Permissions, {@link com.jts.fortress.arbac.OrgUnit.Type#PERM}.
 * The {@code cn=Hierarchies, ou=OS-P} data contains Permission OU pools and is stored as an instance variable, {@link #m_graph}, contained within this class.  The parent-child edges are contained in LDAP,
 * i.e. {@code cn=Hierarchies, ou=OS-P} which uses entity {@link com.jts.fortress.hier.Hier}.  The ldap data is retrieved {@link com.jts.fortress.hier.HierP#read(com.jts.fortress.hier.Hier.Type)} and loaded into {@code org.jgrapht.graph.SimpleDirectedGraph}.
 * The graph...
 * <ol>
 * <li>is stored as singleton in this class with vertices of {@code String}, and edges, as {@link com.jts.fortress.hier.Relationship}s</li>
 * <li>utilizes open source library, see <a href="http://www.jgrapht.org/">JGraphT</a>.</li>
 * <li>contains a general hierarchical data structure i.e. allows multiple inheritance with parents.</li>
 * <li>is a simple directed graph thus does not allow cycles.</li>
 * <li>is refreshed by reading this ldap record,{@code cn=Hierarchies, ou=OS-U} into this entity, {@link com.jts.fortress.hier.Hier}, before loading into this collection class,{@code org.jgrapht.graph.SimpleDirectedGraph} using 3rd party lib, <a href="http://www.jgrapht.org/">JGraphT</a>.
 * <li>can only be updated via the synchronized method {@link #updateHier} which may add, {@link com.jts.fortress.hier.Hier.Op#ADD}, change, {@link com.jts.fortress.hier.Hier.Op#MOD}, or delete, {@link com.jts.fortress.hier.Hier.Op#REM} parent-child relationships.</li>
 * </ol>
 * After update is performed to ldap, the singleton is refreshed with latest info.
 * <p/>
 * Public static methods on this class are intended for use by other Fortress classes, i.e. {@link com.jts.fortress.arbac.DelegatedAdminMgrImpl}.
 * but should not be directly invoked by outside programs.
 * <p/>
 * This class contains singleton that can be updated but is thread safe.
 * <p/>

 *
 * @author smckinn
 * @created September 26, 2010
 */
public class PsoUtil
{
    private static final String OCLS_NM = PsoUtil.class.getName();
    // Is synchronized on update:
    private static SimpleDirectedGraph<String, Relationship> m_graph = null;

    /**
     * Initialize the Perm OU hierarchies.  This will read the {@link com.jts.fortress.hier.Hier} data set from ldap and load into
     * the JGraphT simple digraph that referenced statically within this class.
     */
    static
    {
        loadGraph(Hier.Type.PERM);
    }

    /**
     * Recursively traverse the {@link com.jts.fortress.arbac.OrgUnit} graph and return all of the children of a given parent {@link com.jts.fortress.arbac.OrgUnit#name}.
     *
     * @param name {@link com.jts.fortress.arbac.OrgUnit#name} maps to 'ftRels' attribute on 'ftHier' object class.
     * @return Set of names of children {@link com.jts.fortress.arbac.OrgUnit}s of given parent.
     */
    public static Set<String> getChildren(String name)
    {
        return HierUtil.getChildren(name, m_graph);
    }

    /**
     * Recursively traverse the {@link com.jts.fortress.arbac.OrgUnit.Type#USER} graph and return all of the parents of a given child ou.
     *
     * @param name maps to logical {@link com.jts.fortress.arbac.OrgUnit#name} and physical 'ftRels' attribute on 'ftHier' object class.
     * @return Set of ou names that are parents of given child.
     */
    public static Set<String> getParents(String name)
    {
        return HierUtil.getParents(name, m_graph);
    }


    /**
     * Recursively traverse the {@link com.jts.fortress.arbac.OrgUnit.Type#PERM} graph and return number of children a given parent ou has.
     *
     * @param name maps to logical {@link com.jts.fortress.arbac.OrgUnit#name} and physical 'ftRels' attribute on 'ftHier' object class.
     * @return int value contains the number of children of a given parent ou.
     */
    public static int numChildren(String name)
    {
        return HierUtil.numChildren(name, m_graph);
    }

    /**
     * Return Set of {@link com.jts.fortress.arbac.OrgUnit#name}s ascendants contained within {@link com.jts.fortress.arbac.OrgUnit.Type#PERM}.
     *
     * @param ous contains list of {@link com.jts.fortress.arbac.OrgUnit}s.
     * @return contains Set of all descendants.
     */
    public static Set<String> getInherited(List<OrgUnit> ous)
    {
        // create Set with case insensitive comparator:
        Set<String> iOUs = new TreeSet<String>(new AlphabeticalOrder());
        if (VUtil.isNotNullOrEmpty(ous))
        {
            for (OrgUnit ou : ous)
            {
                String name = ou.getName();
                iOUs.add(name);
                Set<String> parents = HierUtil.getParents(name, m_graph);
                if (VUtil.isNotNullOrEmpty(parents))
                    iOUs.addAll(parents);
            }
        }
        return iOUs;
    }


    /**
     * This api is used by {@link com.jts.fortress.arbac.DelegatedAdminMgrImpl} to determine parentage for Permission OU processing.
     * It calls {@link HierUtil#validateRelationship(org.jgrapht.graph.SimpleDirectedGraph, String, String, boolean)} to evaluate three OU relationship expressions:
     * <ol>
     * <li>If child equals parent</li>
     * <li>If mustExist true and parent-child relationship exists</li>
     * <li>If mustExist false and parent-child relationship does not exist</li>
     * </ol>
     * Method will throw {@link com.jts.fortress.ValidationException} if rule check fails meaning caller failed validation
     * attempt to add/remove hierarchical relationship failed.
     *
     * @param child     contains {@link com.jts.fortress.arbac.OrgUnit#name} of child.
     * @param parent    contains {@link com.jts.fortress.arbac.OrgUnit#name} of parent.
     * @param mustExist boolean is used to specify if relationship must be true.
     * @throws com.jts.fortress.ValidationException
     *          in the event it fails one of the 3 checks.
     */
    public static void validateRelationship(OrgUnit child, OrgUnit parent, boolean mustExist)
        throws ValidationException
    {
        HierUtil.validateRelationship(m_graph, child.getName(), parent.getName(), mustExist);
    }

    /**
     * This api allows synchronized access to {@link com.jts.fortress.hier.HierUtil#validateRelationship(org.jgrapht.graph.SimpleDirectedGraph, String, String, boolean)}
     * to {@link com.jts.fortress.arbac.DelegatedAdminMgrImpl} to allow updates to Permission OU relationships.
     * Method will update the Permission OU hierarchical data set and reload the JGraphT simple digraph with latest.
     *
     * @param hier maps to 'ftRels' attribute on 'ftHier' object class.
     * @param op   used to pass the ldap op {@link com.jts.fortress.hier.Hier.Op#ADD}, {@link com.jts.fortress.hier.Hier.Op#MOD}, {@link com.jts.fortress.hier.Hier.Op#REM}
     * @throws SecurityException in the event of a system error.
     */
    public static void updateHier(Hier hier, Hier.Op op) throws SecurityException
    {
        HierP hp = new HierP();
        // todo: ensure this is the correct way to manage singleton object.
        // thread safe access to singleton object:
        synchronized (m_graph)
        {
            hp.update(hier, op);
            loadGraph(Hier.Type.PERM);
        }
    }

    /**
     * Read this ldap record,{@code cn=Hierarchies, ou=OS-P} into this entity, {@link com.jts.fortress.hier.Hier}, before loading into this collection class,{@code org.jgrapht.graph.SimpleDirectedGraph}
     * using 3rd party lib, <a href="http://www.jgrapht.org/">JGraphT</a>.
     */
    private static void loadGraph(Hier.Type type)
    {
        Hier hier = HierUtil.readHier(type);
        m_graph = HierUtil.buildGraph(hier);
    }
}
