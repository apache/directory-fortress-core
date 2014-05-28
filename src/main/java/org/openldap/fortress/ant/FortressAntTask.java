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

package org.openldap.fortress.ant;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.input.InputRequest;
import org.openldap.fortress.cfg.Config;
import org.openldap.fortress.ldap.group.Group;
import org.openldap.fortress.ldap.group.GroupMgr;
import org.openldap.fortress.ldap.group.GroupMgrFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openldap.fortress.AdminMgr;
import org.openldap.fortress.AdminMgrFactory;
import org.openldap.fortress.CfgException;
import org.openldap.fortress.DelAdminMgr;
import org.openldap.fortress.DelAdminMgrFactory;
import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.PwPolicyMgr;
import org.openldap.fortress.PwPolicyMgrFactory;
import org.openldap.fortress.SecurityException;
import org.openldap.fortress.cfg.ConfigMgr;
import org.openldap.fortress.cfg.ConfigMgrFactory;
import org.openldap.fortress.ldap.container.OrganizationalUnit;
import org.openldap.fortress.ldap.container.OrganizationalUnitP;
import org.openldap.fortress.ldap.suffix.Suffix;
import org.openldap.fortress.ldap.suffix.SuffixP;

import org.openldap.fortress.rbac.AdminRole;
import org.openldap.fortress.rbac.ClassUtil;
import org.openldap.fortress.rbac.Context;
import org.openldap.fortress.rbac.OrgUnit;
import org.openldap.fortress.rbac.OrgUnitAnt;
import org.openldap.fortress.rbac.PermGrant;
import org.openldap.fortress.rbac.PermObj;
import org.openldap.fortress.rbac.Permission;
import org.openldap.fortress.rbac.PwPolicy;
import org.openldap.fortress.rbac.Relationship;
import org.openldap.fortress.rbac.Role;
import org.openldap.fortress.rbac.SDSet;
import org.openldap.fortress.rbac.User;
import org.openldap.fortress.rbac.UserAdminRole;
import org.openldap.fortress.rbac.UserRole;
import org.openldap.fortress.util.Testable;
import org.openldap.fortress.util.attr.VUtil;


/**
 * This class implements Apache Ant custom task and is used to drive the Fortress Administrative APIs using XML files
 * .  The
 * methods in this class are not intended to be callable by outside programs.  The following APIs are supported:
 * <p/>
 * <ol>
 * <li>{@link org.openldap.fortress.AdminMgr}</li>
 * <li>{@link org.openldap.fortress.DelAdminMgr}</li>
 * <li>{@link org.openldap.fortress.PwPolicyMgr}</li>
 * <li>{@link ConfigMgr}</li>
 * </ol>
 * <p/>
 * using the custom Ant task that is implemented in this class.  The format of the XML is flat and consists of entity
 * names
 * along with their attributes.
 * <h3>
 * This class will process xml formatted requests with the following tags:
 * </h3>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *          <adduser> ...</adduser>
 *          <deluser> ...</deluser>
 *          <adduserrole> ...</adduserrole>
 *          <deluserrole> ...</deluserrole>
 *          <addrole> ...</addrole>*
 *          <delrole> ...</delrole>
 *          <addroleinheritance>...</addroleinheritance>
 *          <delroleinheritance>...</delroleinheritance>
 *          <addsdset>STATIC or DYNAMIC</addsdset>
 *          <delsdset>STATIC or DYNAMIC</delsdset>
 *          <delpwpolicy> ...</delpwpolicy>
 *          <addpwpolicy> ...</addpwpolicy>
 *          <addpermobj> RBAC or ARBAC permission objects</addpermobj>
 *          <delpermobj> RBAC or ARBAC permission objects</delpermobj>
 *          <addpermop> RBAC or ARBAC permission operations</addpermop>
 *          <delpermop> RBAC or ARBAC permission operations</delpermop>
 *          <addpermgrant> RBAC or ARBAC permission grants </addpermgrant>
 *          <delpermgrant> RBAC or ARBAC permission revokes </delpermgrant>
 *          <addorgunit> User OUs or Perm OUs </addorgunit>
 *          <delorgunit> User OUs or Perm OUs </delorgunit>
 *          <adduserorgunitinheritance> ...</adduserorgunitinheritance>
 *          <deluserorgunitinheritance> ...</deluserorgunitinheritance>
 *          <addpermorgunitinheritance> ...</addpermorgunitinheritance>
 *          <delpermorgunitinheritance> ...</delpermorgunitinheritance>
 *          <adduser> ... </adduser>
 *          <deluser> ...</deluser>
 *          <addadminrole>  ... </addadminrole>
 *          <deladminrole>  ... </deladminrole>
 *          <addadminroleinheritance>...</addadminroleinheritance>
 *          <deladminroleinheritance>...</deladminroleinheritance>
 *          <adduseradminrole> ... </adduseradminrole>
 *          <deluseradminrole>  ... </deluseradminrole>
 *      </FortressAdmin>
 * </target>
 * }
 * </pre>
 * <h3>Order of Precedence</h3>
 * The order of operations in the XML does not effect the order or precedence which has been "hard-wired" by the
 * processing order within this class.
 * <ol>
 * <li>Delete User Role Assignments {@link org.openldap.fortress.AdminMgr#deassignUser(org.openldap.fortress.rbac.UserRole)}</li>
 * <li>Delete User AdminRole Assignments {@link DelAdminMgr#deassignUser(UserAdminRole)}</li>
 * <li>Revoke Permission Assignments Delete{@link AdminMgr#revokePermission(org.openldap.fortress.rbac.Permission,
 * org.openldap.fortress.rbac.Role)}</li>
 * <li>Delete Users {@link org.openldap.fortress.AdminMgr#disableUser(org.openldap.fortress.rbac.User)}</li>
 * <li>Delete Password Policies {@link org.openldap.fortress.PwPolicyMgr#delete(org.openldap.fortress.rbac.PwPolicy)}</li>
 * <li>Delete Permission Operations {@link org.openldap.fortress.AdminMgr#deletePermission(org.openldap.fortress.rbac.Permission)
 * }</li>
 * <li>Delete Permission Objects {@link org.openldap.fortress.AdminMgr#deletePermObj(org.openldap.fortress.rbac.PermObj)}</li>
 * <li>Delete SSD and DSD Sets {@link org.openldap.fortress.AdminMgr#deleteDsdSet(org.openldap.fortress.rbac.SDSet)} and {@link
 * org.openldap.fortress.AdminMgr#deleteSsdSet(org.openldap.fortress.rbac.SDSet)}</li>
 * <li>Delete RBAC Roles Inheritances {@link org.openldap.fortress.AdminMgr#deleteInheritance(org.openldap.fortress.rbac.Role,
 * org.openldap.fortress.rbac.Role)}</li>
 * <li>Delete RBAC Roles {@link org.openldap.fortress.AdminMgr#deleteRole(org.openldap.fortress.rbac.Role)}</li>
 * <li>Delete ARBAC Role Inheritances {@link DelAdminMgr#deleteInheritance(org.openldap.fortress.rbac.AdminRole,
 * org.openldap.fortress.rbac.AdminRole)}</li>
 * <li>Delete ARBAC Roles {@link org.openldap.fortress.DelAdminMgr#deleteRole(org.openldap.fortress.rbac.AdminRole)}</li>
 * <li>Delete User and Perm OU Inheritances {@link DelAdminMgr#deleteInheritance(org.openldap.fortress.rbac.OrgUnit,
 * org.openldap.fortress.rbac.OrgUnit)} USER and PERM</li>
 * <li>Delete User and Perm OUs {@link org.openldap.fortress.DelAdminMgr#delete(org.openldap.fortress.rbac.OrgUnit)} USER and
 * PERM</li>
 * <li>Delete Configuration Entries {@link org.openldap.fortress.cfg.ConfigMgr#delete(String, java.util.Properties)}</li>
 * <li>Delete Containers {@link OrganizationalUnitP#delete(OrganizationalUnit)}</li>
 * <li>Delete Suffix {@link org.openldap.fortress.ldap.suffix.SuffixP#delete(org.openldap.fortress.ldap.suffix.Suffix)}}</li>
 * <li>Add Suffix {@link SuffixP#add(Suffix)}}</li>
 * <li>Add Containers {@link OrganizationalUnitP#add(OrganizationalUnit)}</li>
 * <li>Add Configuration Parameters {@link ConfigMgr#add(String, java.util.Properties)}</li>
 * <li>Add User and Perm OUs {@link org.openldap.fortress.DelAdminMgr#add(org.openldap.fortress.rbac.OrgUnit)} USER and PERM</li>
 * <li>Add User and Perm OU Inheritances {@link DelAdminMgr#addInheritance(org.openldap.fortress.rbac.OrgUnit,
 * org.openldap.fortress.rbac.OrgUnit)} USER and PERM</li>
 * <li>Add ARBAC Roles {@link org.openldap.fortress.DelAdminMgr#addRole(org.openldap.fortress.rbac.AdminRole)}</li>
 * <li>Add ARBAC Role Inheritances {@link org.openldap.fortress.DelAdminMgr#addInheritance(org.openldap.fortress.rbac.AdminRole,
 * org.openldap.fortress.rbac.AdminRole)}</li>
 * <li>Add RBAC Roles {@link org.openldap.fortress.AdminMgr#addRole(org.openldap.fortress.rbac.Role)}</li>
 * <li>Add RBAC Role Inheritances {@link org.openldap.fortress.AdminMgr#addInheritance(org.openldap.fortress.rbac.Role,
 * org.openldap.fortress.rbac.Role)}</li>
 * <li>Add DSD and SSD Sets {@link org.openldap.fortress.AdminMgr#createDsdSet(org.openldap.fortress.rbac.SDSet)} and {@link org.openldap.fortress.AdminMgr#createSsdSet(org.openldap.fortress.rbac.SDSet)}</li>
 * <li>Add Permission Objects {@link org.openldap.fortress.AdminMgr#addPermObj(org.openldap.fortress.rbac.PermObj)}</li>
 * <li>Add Permission Operations {@link org.openldap.fortress.AdminMgr#addPermission(org.openldap.fortress.rbac.Permission)}</li>
 * <li>Add Password Policies {@link org.openldap.fortress.PwPolicyMgr#add(org.openldap.fortress.rbac.PwPolicy)}</li>
 * <li>Add Users {@link org.openldap.fortress.AdminMgr#addUser(org.openldap.fortress.rbac.User)}</li>
 * <li>Grant RBAC Permissions {@link org.openldap.fortress.AdminMgr#grantPermission(org.openldap.fortress.rbac.Permission,
 * org.openldap.fortress.rbac.Role)}</li>
 * <li>Assign ARBAC Roles {@link org.openldap.fortress.DelAdminMgr#assignUser(org.openldap.fortress.rbac.UserAdminRole)}</li>
 * <li>Assign RBAC Roles {@link org.openldap.fortress.AdminMgr#assignUser(org.openldap.fortress.rbac.UserRole)}</li>
 * </li>
 * </ol>
 * <p/>
 *
 * @author Shawn McKinney
 */
public class FortressAntTask extends Task implements InputHandler
{
    public static final boolean DEBUG = ( ( System.getProperty( "debug.admin" ) != null ) && ( System.getProperty(
        "debug.admin" ).equalsIgnoreCase( "true" ) ) );
    private static final String SEMICOLON = ";";
    final private List<org.openldap.fortress.ant.Addconfig> addconfig = new ArrayList<>();
    final private List<org.openldap.fortress.ant.Delconfig> delconfig = new ArrayList<>();
    final private List<Adduser> addusers = new ArrayList<>();
    final private List<Deluser> delusers = new ArrayList<>();
    final private List<Adduserrole> adduserroles = new ArrayList<>();
    final private List<Deluserrole> deluserroles = new ArrayList<>();
    final private List<Addrole> addroles = new ArrayList<>();
    final private List<Delrole> delroles = new ArrayList<>();
    final private List<Addsdset> addsdsets = new ArrayList<>();
    final private List<Addroleinheritance> addroleinheritances = new ArrayList<>();
    final private List<Delroleinheritance> delroleinheritances = new ArrayList<>();
    final private List<Delsdset> delsdsets = new ArrayList<>();
    final private List<AddpermOp> addpermOps = new ArrayList<>();
    final private List<DelpermOp> delpermOps = new ArrayList<>();
    final private List<AddpermObj> addpermObjs = new ArrayList<>();
    final private List<DelpermObj> delpermObjs = new ArrayList<>();
    final private List<AddpermGrant> addpermGrants = new ArrayList<>();
    final private List<DelpermGrant> delpermGrants = new ArrayList<>();
    final private List<Addpwpolicy> addpolicies = new ArrayList<>();
    final private List<Delpwpolicy> delpolicies = new ArrayList<>();
    final private List<Addcontainer> addcontainers = new ArrayList<>();
    final private List<Delcontainer> delcontainers = new ArrayList<>();
    final private List<Addsuffix> addsuffixes = new ArrayList<>();
    final private List<Delsuffix> delsuffixes = new ArrayList<>();
    final private List<Addorgunit> addorgunits = new ArrayList<>();
    final private List<Delorgunit> delorgunits = new ArrayList<>();
    final private List<Adduserorgunitinheritance> adduserorgunitinheritances = new ArrayList<>();
    final private List<Deluserorgunitinheritance> deluserorgunitinheritances = new ArrayList<>();
    final private List<Addpermorgunitinheritance> addpermorgunitinheritances = new ArrayList<>();
    final private List<Delpermorgunitinheritance> delpermorgunitinheritances = new ArrayList<>();
    final private List<Addadminrole> addadminroles = new ArrayList<>();
    final private List<Deladminrole> deladminroles = new ArrayList<>();
    final private List<Adduseradminrole> adduseradminroles = new ArrayList<>();
    final private List<Addadminroleinheritance> addadminroleinheritances = new ArrayList<>();
    final private List<Deladminroleinheritance> deladminroleinheritances = new ArrayList<>();
    final private List<Deluseradminrole> deluseradminroles = new ArrayList<>();
    final private List<Addcontext> addcontexts = new ArrayList<>();
    final protected List<Addgroup> addgroups = new ArrayList<>();
    final protected List<Delgroup> delgroups = new ArrayList<>();
    final protected List<Addgroupmember> addgroupmembers = new ArrayList<>();
    final protected List<Delgroupmember> delgroupmembers = new ArrayList<>();
    final protected List<Addgroupproperty> addgroupproperties = new ArrayList<>();
    final protected List<Delgroupproperty> delgroupproperties = new ArrayList<>();

    protected ConfigMgr cfgMgr = null;
    protected AdminMgr adminMgr = null;
    protected DelAdminMgr dAdminMgr = null;
    protected PwPolicyMgr policyMgr = null;
    protected GroupMgr groupMgr = null;
    private static final String CLS_NM = FortressAntTask.class.getName();
    protected static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    protected Context context;


    /**
     * Load the entity with data.
     *
     * @param addcontext contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddcontext( Addcontext addcontext )
    {
        this.addcontexts.add( addcontext );
    }


    public void setContext( Context context )
    {
        System.out.println( CLS_NM + ".setContext name: " + context.getName() );
        this.context = context;
        try
        {
            adminMgr = org.openldap.fortress.AdminMgrFactory.createInstance( context.getName() );
            dAdminMgr = DelAdminMgrFactory.createInstance( context.getName() );
            policyMgr = PwPolicyMgrFactory.createInstance( context.getName() );
            groupMgr = GroupMgrFactory.createInstance( context.getName() );
        }
        catch ( SecurityException se )
        {
            LOG.warn( " FortressAntTask setContext caught SecurityException=" + se );
        }
    }


    /**
     * Default constructor initializes he Manager APIs.
     */
    public FortressAntTask()
    {
        try
        {
            cfgMgr = ConfigMgrFactory.createInstance();
            adminMgr = AdminMgrFactory.createInstance( GlobalIds.HOME );
            dAdminMgr = DelAdminMgrFactory.createInstance( GlobalIds.HOME );
            policyMgr = PwPolicyMgrFactory.createInstance( GlobalIds.HOME );
            groupMgr = GroupMgrFactory.createInstance( GlobalIds.HOME );
        }
        catch ( SecurityException se )
        {
            LOG.warn( " FortressAntTask constructor caught SecurityException=" + se );
        }
    }


    /**
     * Used by Apache Ant to load data from xml into entities.
     *
     * @param request
     */
    public void handleInput( InputRequest request )
    {
        LOG.info( "handleInput request=" + request );
    }


    /**
     * Load the entity with data.
     *
     * @param addcfg contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddconfig( Addconfig addcfg )
    {
        this.addconfig.add( addcfg );
    }


    /**
     * Load the entity with data.
     *
     * @param delcfg contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelconfig( Delconfig delcfg )
    {
        this.delconfig.add( delcfg );
    }


    /**
     * Load the entity with data.
     *
     * @param adduser contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAdduser( Adduser adduser )
    {
        this.addusers.add( adduser );
    }


    /**
     * Load the entity with data.
     *
     * @param deluser contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDeluser( Deluser deluser )
    {
        this.delusers.add( deluser );
    }


    /**
     * Load the entity with data.
     *
     * @param adduserrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAdduserrole( Adduserrole adduserrole )
    {
        this.adduserroles.add( adduserrole );
    }


    /**
     * Load the entity with data.
     *
     * @param deluserrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDeluserrole( Deluserrole deluserrole )
    {
        this.deluserroles.add( deluserrole );
    }


    /**
     * Load the entity with data.
     *
     * @param addrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddrole( Addrole addrole )
    {
        this.addroles.add( addrole );
    }


    /**
     * Load the entity with data.
     *
     * @param delrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelrole( Delrole delrole )
    {
        this.delroles.add( delrole );
    }


    /**
     * Load the entity with data.
     *
     * @param addroleinheritance contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddroleinheritance( Addroleinheritance addroleinheritance )
    {
        this.addroleinheritances.add( addroleinheritance );
    }


    /**
     * Load the entity with data.
     *
     * @param delroleinheritance contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelroleinheritance( Delroleinheritance delroleinheritance )
    {
        this.delroleinheritances.add( delroleinheritance );
    }


    /**
     * Load the entity with data.
     *
     * @param addsd contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddsdset( Addsdset addsd )
    {
        this.addsdsets.add( addsd );
    }


    /**
     * Load the entity with data.
     *
     * @param delsd contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelsdset( Delsdset delsd )
    {
        this.delsdsets.add( delsd );
    }


    /**
     * Load the entity with data.
     *
     * @param addpermOp contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddpermOp( AddpermOp addpermOp )
    {
        this.addpermOps.add( addpermOp );
    }


    /**
     * Load the entity with data.
     *
     * @param delpermOp contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelpermOp( DelpermOp delpermOp )
    {
        this.delpermOps.add( delpermOp );
    }


    /**
     * Load the entity with data.
     *
     * @param addpermObj contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddpermObj( AddpermObj addpermObj )
    {
        this.addpermObjs.add( addpermObj );
    }


    /**
     * Load the entity with data.
     *
     * @param delpermObj contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelpermObj( DelpermObj delpermObj )
    {
        this.delpermObjs.add( delpermObj );
    }


    /**
     * Load the entity with data.
     *
     * @param addpermGrant contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddpermGrant( AddpermGrant addpermGrant )
    {
        this.addpermGrants.add( addpermGrant );
    }


    /**
     * Load the entity with data.
     *
     * @param delpermGrant contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelpermGrant( DelpermGrant delpermGrant )
    {
        this.delpermGrants.add( delpermGrant );
    }


    /**
     * Load the entity with data.
     *
     * @param addpwpolicy contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddpwpolicy( Addpwpolicy addpwpolicy )
    {
        this.addpolicies.add( addpwpolicy );
    }


    /**
     * Load the entity with data.
     *
     * @param delpwpolicy contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelpwpolicy( Delpwpolicy delpwpolicy )
    {
        this.delpolicies.add( delpwpolicy );
    }


    /**
     * Load the entity with data.
     *
     * @param addcontainer contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddcontainer( Addcontainer addcontainer )
    {
        this.addcontainers.add( addcontainer );
    }


    /**
     * Load the entity with data.
     *
     * @param delcontainer contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelcontainer( Delcontainer delcontainer )
    {
        this.delcontainers.add( delcontainer );
    }


    /**
     * Load the entity with data.
     *
     * @param addsuffix contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddsuffix( Addsuffix addsuffix )
    {
        this.addsuffixes.add( addsuffix );
    }


    /**
     * Load the entity with data.
     *
     * @param delsuffix contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelsuffix( Delsuffix delsuffix )
    {
        this.delsuffixes.add( delsuffix );
    }


    /**
     * Load the entity with data.
     *
     * @param addorgunit contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddorgunit( Addorgunit addorgunit )
    {
        this.addorgunits.add( addorgunit );
    }


    /**
     * Load the entity with data.
     *
     * @param delorgunit contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelorgunit( Delorgunit delorgunit )
    {
        this.delorgunits.add( delorgunit );
    }


    /**
     * Load the entity with data.
     *
     * @param addinheritance contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAdduserorgunitinheritance( Adduserorgunitinheritance addinheritance )
    {
        this.adduserorgunitinheritances.add( addinheritance );
    }


    /**
     * Load the entity with data.
     *
     * @param delinheritance contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDeluserorgunitinheritance( Deluserorgunitinheritance delinheritance )
    {
        this.deluserorgunitinheritances.add( delinheritance );
    }


    /**
     * Load the entity with data.
     *
     * @param addinheritance contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddpermorgunitinheritance( Addpermorgunitinheritance addinheritance )
    {
        this.addpermorgunitinheritances.add( addinheritance );
    }


    /**
     * Load the entity with data.
     *
     * @param delinheritance contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelpermorgunitinheritance( Delpermorgunitinheritance delinheritance )
    {
        this.delpermorgunitinheritances.add( delinheritance );
    }


    /**
     * Load the entity with data.
     *
     * @param addrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddadminrole( Addadminrole addrole )
    {
        this.addadminroles.add( addrole );
    }


    /**
     * Load the entity with data.
     *
     * @param delrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDeladminrole( Deladminrole delrole )
    {
        this.deladminroles.add( delrole );
    }


    /**
     * Load the entity with data.
     *
     * @param addadminroleinheritance contains the ant initialized data entities to be handed off for further
     *                                processing.
     */
    public void addAddadminroleinheritance( Addadminroleinheritance addadminroleinheritance )
    {
        this.addadminroleinheritances.add( addadminroleinheritance );
    }


    /**
     * Load the entity with data.
     *
     * @param deladminroleinheritance contains the ant initialized data entities to be handed off for further
     *                                processing.
     */
    public void addDeladminroleinheritance( Deladminroleinheritance deladminroleinheritance )
    {
        this.deladminroleinheritances.add( deladminroleinheritance );
    }


    /**
     * Load the entity with data.
     *
     * @param adduserrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAdduseradminrole( Adduseradminrole adduserrole )
    {
        this.adduseradminroles.add( adduserrole );
    }


    /**
     * Load the entity with data.
     *
     * @param deluserrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDeluseradminrole( Deluseradminrole deluserrole )
    {
        this.deluseradminroles.add( deluserrole );
    }

    /**
     * Load the entity with data.
     *
     * @param addgroup contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddgroup( Addgroup addgroup )
    {
        this.addgroups.add( addgroup );
    }


    /**
     * Load the entity with data.
     *
     * @param delgroup contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelgroup( Delgroup delgroup )
    {
        this.delgroups.add( delgroup );
    }


    /**
     * Load the entity with data.
     *
     * @param addgroupmember contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddgroupmember( Addgroupmember addgroupmember )
    {
        this.addgroupmembers.add( addgroupmember );
    }


    /**
     * Load the entity with data.
     *
     * @param delgroupmember contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelgroupmember( Delgroupmember delgroupmember )
    {
        this.delgroupmembers.add( delgroupmember );
    }


    /**
     * Load the entity with data.
     *
     * @param addgroupproperty contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddgroupproperty( Addgroupproperty addgroupproperty )
    {
        this.addgroupproperties.add( addgroupproperty );
    }


    /**
     * Load the entity with data.
     *
     * @param delgroupproperty contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelgroupproperty( Delgroupproperty delgroupproperty )
    {
        this.delgroupproperties.add( delgroupproperty );
    }


    /**
     * @param list
     * @return boolean
     */
    private boolean isListNotNull( List list )
    {
        return ( list != null && list.size() > 0 );
    }


    /**
     * @throws BuildException
     */
    public void execute() throws BuildException
    {
        LOG.info( "FORTRESS ANT TASK NAME : " + getTaskName() );
        if ( isListNotNull( addcontexts ) )
        {
            setContext( addcontexts.get( 0 ).getContexts().get( 0 ) );
        }

        if ( isListNotNull( deluserroles ) )
        {
            delUserRoles();
        }

        if ( isListNotNull( deluseradminroles ) )
        {
            delUserAdminRoles();
        }

        if ( isListNotNull( delpermGrants ) )
        {
            deletePermGrants();
        }

        if ( isListNotNull( delgroupproperties ) )
        {
            deleteGroupProperties();
        }

        if ( isListNotNull( delgroupmembers ) )
        {
            deleteGroupMembers();
        }

        if ( isListNotNull( delgroups ) )
        {
            deleteGroups();
        }

        if ( isListNotNull( delusers ) )
        {
            deleteUsers();
        }

        if ( isListNotNull( delpolicies ) )
        {
            deletePolicies();
        }

        if ( isListNotNull( delpermOps ) )
        {
            deletePermOps();
        }

        if ( isListNotNull( delpermObjs ) )
        {
            deletePermObjs();
        }

        if ( isListNotNull( delsdsets ) )
        {
            deleteSdsets();
        }

        if ( isListNotNull( delroleinheritances ) )
        {
            deleteRoleInheritances();
        }

        if ( isListNotNull( delroles ) )
        {
            deleteRoles();
        }

        if ( isListNotNull( deladminroleinheritances ) )
        {
            deleteAdminRoleInheritances();
        }

        if ( isListNotNull( deladminroles ) )
        {
            deleteAdminRoles();
        }

        if ( isListNotNull( deluserorgunitinheritances ) )
        {
            deleteUserOrgunitInheritances();
        }

        if ( isListNotNull( delpermorgunitinheritances ) )
        {
            deletePermOrgunitInheritances();
        }

        if ( isListNotNull( delorgunits ) )
        {
            delOrgunits();
        }

        if ( isListNotNull( delconfig ) )
        {
            deleteConfig();
        }

        if ( isListNotNull( delcontainers ) )
        {
            deleteContainers();
        }

        if ( isListNotNull( delsuffixes ) )
        {
            deleteSuffixes();
        }

        if ( isListNotNull( addsuffixes ) )
        {
            addSuffixes();
        }

        if ( isListNotNull( addcontainers ) )
        {
            addContainers();
        }

        if ( isListNotNull( addconfig ) )
        {
            addConfig();
        }

        if ( isListNotNull( addorgunits ) )
        {
            addOrgunits();
        }

        if ( isListNotNull( adduserorgunitinheritances ) )
        {
            addUserOrgunitInheritances();
        }

        if ( isListNotNull( addpermorgunitinheritances ) )
        {
            addPermOrgunitInheritances();
        }

        if ( isListNotNull( addadminroles ) )
        {
            addAdminRoles();
        }

        if ( isListNotNull( addadminroleinheritances ) )
        {
            addAdminRoleInheritances();
        }

        if ( isListNotNull( addroles ) )
        {
            addRoles();
        }

        if ( isListNotNull( addroleinheritances ) )
        {
            addRoleInheritances();
        }

        if ( isListNotNull( addsdsets ) )
        {
            addSdsets();
        }

        if ( isListNotNull( addpermObjs ) )
        {
            addPermObjs();
        }

        if ( isListNotNull( addpermOps ) )
        {
            addPermOps();
        }

        if ( isListNotNull( addpolicies ) )
        {
            addPolicies();
        }

        if ( isListNotNull( addusers ) )
        {
            addUsers();
        }

        if ( isListNotNull( addgroups ) )
        {
            addGroups();
        }

        if ( isListNotNull( addgroupmembers ) )
        {
            addGroupMembers();
        }

        if ( isListNotNull( addgroupproperties ) )
        {
            addGroupProperties();
        }

        if ( isListNotNull( addpermGrants ) )
        {
            addPermGrants();
        }

        if ( isListNotNull( adduseradminroles ) )
        {
            addUserAdminRoles();
        }

        if ( isListNotNull( adduserroles ) )
        {
            addUserRoles();
        }

        // Test the results?
        if ( DEBUG )
        {
            // Verify the input XML file against what made it into the target LDAP directory:
            LOG.info( "DEBUG MODE" );
            try
            {
                String testClassName = Config.getProperty( getTaskName() );
                if(!VUtil.isNotNullOrEmpty( testClassName ))
                {
                    testClassName = "org.openldap.fortress.rbac.FortressAntLoadTest";
                }
                // Use reflexion to avoid core dependency on test classes located under FORTRESS_HOME/src/main/test
                Testable tester = ( Testable ) ClassUtil.createInstance( testClassName );
                tester.execute( this );
            }
            catch ( CfgException ce )
            {
                String error = "Error executing tests, errCode=" + ce.getErrorId() + " msg=" + ce;
                LOG.warn( error );
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addUsers() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Adduser adduser : addusers )
        {
            List<UserAnt> users = adduser.getUsers();

            for ( UserAnt user : users )
            {
                LOG.info( "addUsers userid=" + user.getUserId() + " description=" + user.getDescription() + " " +
                    "orgUnit=" + user.getOu() );
                try
                {
                    try
                    {
                        adminMgr.addUser( user );
                        if ( VUtil.isNotNullOrEmpty( user.getRoles() ) )
                        {
                            for ( UserRole uRole : user.getRoles() )
                            {
                                adminMgr.assignUser( uRole );
                            }
                        }
                        if ( VUtil.isNotNullOrEmpty( user.getAdminRoles() ) )
                        {
                            for ( UserAdminRole uAdminRoleRole : user.getAdminRoles() )
                            {
                                dAdminMgr.assignUser( uAdminRoleRole );
                            }
                        }
                    }
                    catch ( SecurityException se )
                    {
                        // If User entity already there then call the udpate method.
                        if ( se.getErrorId() == GlobalErrIds.USER_ID_DUPLICATE )
                        {
                            adminMgr.updateUser( user );
                            if ( VUtil.isNotNullOrEmpty( user.getRoles() ) )
                            {
                                for ( UserRole uRole : user.getRoles() )
                                {
                                    adminMgr.assignUser( uRole );
                                }
                            }
                            if ( VUtil.isNotNullOrEmpty( user.getAdminRoles() ) )
                            {
                                for ( UserAdminRole uAdminRoleRole : user.getAdminRoles() )
                                {
                                    dAdminMgr.assignUser( uAdminRoleRole );
                                }
                            }
                            LOG.info( "addUsers - Update entity - userId=" + user.getUserId() );
                        }
                        else
                        {
                            throw se;
                        }
                    }
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addUsers userId [" + user.getUserId() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteUsers() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Deluser deluser : delusers )
        {
            List<UserAnt> users = deluser.getUsers();
            for ( UserAnt user : users )
            {
                LOG.info( "deleteUsers userid=" + user.getUserId() );
                try
                {
                    adminMgr.deleteUser( user );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteUsers userId [" + user.getUserId() + "] caught SecurityException=" + se );
                }
            }
        }
    }

    /**
     * @throws BuildException
     */
    private void addGroups() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addgroup addgroup : addgroups )
        {
            List<Group> groups = addgroup.getGroups();

            for ( Group group : groups )
            {
                LOG.info( "addGroups name=" + group.getName() + " description=" + group.getDescription());
                try
                {
                    groupMgr.add( group );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addGroups name [" + group.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }

    /**
     * @throws BuildException
     */
    private void deleteGroups() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delgroup delgroup : delgroups )
        {
            List<Group> groups = delgroup.getGroups();
            for ( Group group : groups )
            {
                LOG.info( "deleteGroups name=" + group.getName() );
                try
                {
                    groupMgr.delete( group );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteGroups name [" + group.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }

    /**
     * @throws BuildException
     */
    private void addGroupMembers() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addgroupmember addgroupmember : addgroupmembers )
        {
            List<Group> groups = addgroupmember.getGroups();

            for ( Group group : groups )
            {
                List<String> members = group.getMembers();
                if(VUtil.isNotNullOrEmpty( members ))
                {
                    for( String member : members )
                    {
                        LOG.info( "addGroupMembers name=" + group.getName() + ", member=" + member );
                        try
                        {
                            groupMgr.assign( group, member );
                        }
                        catch ( SecurityException se )
                        {
                            LOG.warn( "addGroupMembers name [" + group.getName() + "], member [" + member + "] caught SecurityException=" + se );
                        }
                    }
                }
                else
                {
                    LOG.info( "addGroupMembers name=" + group.getName() + ", no member found" );
                }
            }
        }
    }

    /**
     * @throws BuildException
     */
    private void deleteGroupMembers() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delgroupmember delgroupmember : delgroupmembers )
        {
            List<Group> groups = delgroupmember.getGroups();
            for ( Group group : groups )
            {
                if(VUtil.isNotNullOrEmpty( group.getMembers() ))
                {
                    for(String member : group.getMembers())
                    {
                        LOG.info( "deleteGroupmembers name=" + group.getName() + ", member=" + member );
                        try
                        {
                            groupMgr.deassign( group, member );
                        }
                        catch ( SecurityException se )
                        {
                            LOG.warn( "deleteGroupmembers name [" + group.getName() + "], member [" + member + "] caught SecurityException=" + se );
                        }
                    }
                }
                else
                {
                    LOG.info( "deleteGroupmembers name=" + group.getName() + ", no member found" );
                }
            }
        }
    }

    /**
     * @throws BuildException
     */
    private void addGroupProperties() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addgroupproperty addgroupproperty : addgroupproperties )
        {
            List<Group> groups = addgroupproperty.getGroups();
            for ( Group group : groups )
            {
                if(VUtil.isNotNullOrEmpty( group.getProperties() ))
                {
                    for ( Enumeration e = group.getProperties().propertyNames(); e.hasMoreElements(); )
                    {
                        // This LDAP attr is stored as a name-value pair separated by a ':'.
                        String key = ( String ) e.nextElement();
                        String val = group.getProperties().getProperty( key );
                        try
                        {
                            groupMgr.add( group, key, val );
                        }
                        catch ( SecurityException se )
                        {
                            LOG.warn( "addGroupProperties name [" + group.getName() + "], key [" + key + "], value [" + val + "] caught SecurityException=" + se );
                        }
                    }
                }
                else
                {
                    LOG.info( "addGroupProperties name=" + group.getName() + ", no properties found" );
                }
            }
        }
    }

    /**
     * @throws BuildException
     */
    private void deleteGroupProperties() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delgroupproperty delgroupproperty : delgroupproperties )
        {
            List<Group> groups = delgroupproperty.getGroups();
            for ( Group group : groups )
            {
                if(VUtil.isNotNullOrEmpty( group.getProperties() ))
                {
                    for ( Enumeration e = group.getProperties().propertyNames(); e.hasMoreElements(); )
                    {
                        // This LDAP attr is stored as a name-value pair separated by a ':'.
                        String key = ( String ) e.nextElement();
                        String val = group.getProperties().getProperty( key );
                        try
                        {
                            groupMgr.delete( group, key, val );
                        }
                        catch ( SecurityException se )
                        {
                            LOG.warn( "deleteGroupProperties name [" + group.getName() + "], key [" + key + "], value [" + val + "] caught SecurityException=" + se );
                        }
                    }
                }
                else
                {
                    LOG.info( "deleteGroupProperties name=" + group.getName() + ", no properties found" );
                }
            }
        }
    }

    /**
     * @throws BuildException
     */
    private void addUserRoles() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Adduserrole adduserrole : adduserroles )
        {
            List<UserRole> userroles = adduserrole.getUserRoles();
            for ( UserRole userRole : userroles )
            {
                LOG.info( "addUserRoles userid=" + userRole.getUserId() + " role name=" + userRole.getName() );

                /*
                System.out.println("userrole data:");
                System.out.println("    userId:" + userRole.getUserId());
                System.out.println("    name:" + userRole.getName());
                System.out.println("    bdate:" + userRole.getBeginDate());
                System.out.println("    edate:" + userRole.getEndDate());
                System.out.println("    blckdate:" + userRole.getBeginLockDate());
                System.out.println("    elckdate:" + userRole.getEndLockDate());
                System.out.println("    btime:" + userRole.getBeginTime());
                System.out.println("    etime:" + userRole.getEndTime());
                System.out.println("    day:" + userRole.getDayMask());
                System.out.println("    raw:" + ((UserRole)userRole).getRawData());
                System.out.println("    to:" + userRole.getTimeout());
                */

                try
                {
                    //Role role = new Role(userRole);
                    adminMgr.assignUser( userRole );
                }
                catch ( SecurityException se )
                {
                    String warning = "addUserRoles userId [" + userRole.getUserId() + "] role name [" + userRole
                        .getName() + "] caught SecurityException=" + se;
                    LOG.warn( warning );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void delUserRoles() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Deluserrole deluserrole : deluserroles )
        {
            List<UserRole> userroles = deluserrole.getUserRoles();
            for ( UserRole userRole : userroles )
            {
                LOG.info( "delUserRoles userid=" + userRole.getUserId() + " role name=" + userRole.getName() );
                try
                {
                    adminMgr.deassignUser( userRole );
                }
                catch ( org.openldap.fortress.SecurityException se )
                {
                    String warning = "delUserRoles userId [" + userRole.getUserId() + "] role name [" + userRole
                        .getName() + "] caught SecurityException=" + se;
                    LOG.warn( warning );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addRoles() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addrole addrole : addroles )
        {
            List<Role> roles = addrole.getRoles();
            for ( Role role : roles )
            {
                LOG.info( "addRoles name=" + role.getName() + " description=" + role.getDescription() );
                try
                {
                    adminMgr.addRole( role );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addRoles name [" + role.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteRoles() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delrole delrole : delroles )
        {
            List<Role> roles = delrole.getRoles();
            for ( Role role : roles )
            {
                LOG.info( "deleteRoles name=" + role.getName() );
                try
                {
                    adminMgr.deleteRole( role );
                }
                catch ( org.openldap.fortress.SecurityException se )
                {
                    LOG.warn( "deleteRoles name [" + role.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addRoleInheritances() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addroleinheritance addroleinheritance : addroleinheritances )
        {
            List<Relationship> roles = addroleinheritance.getRelationships();
            for ( Relationship relationship : roles )
            {
                LOG.info( "addRoleInheritances parent=" + relationship.getParent() + " child=" + relationship
                    .getChild() );
                try
                {
                    adminMgr.addInheritance( new Role( relationship.getParent() ), new Role( relationship.getChild()
                    ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addRoleInheritances parent [" + relationship.getParent() + "] child [" + relationship
                        .getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteRoleInheritances() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delroleinheritance delroleinheritance : delroleinheritances )
        {
            List<Relationship> roles = delroleinheritance.getRelationships();
            for ( Relationship relationship : roles )
            {
                LOG.info( "deleteRoleInheritances parent=" + relationship.getParent() + " child=" + relationship
                    .getChild() );
                try
                {
                    adminMgr.deleteInheritance( new Role( relationship.getParent() ),
                        new Role( relationship.getChild() ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteRoleInheritances parent [" + relationship.getParent() + "] child [" +
                        relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addSdsets() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addsdset addsdset : addsdsets )
        {
            List<SDSetAnt> sds = addsdset.getSdset();
            for ( SDSetAnt sd : sds )
            {
                LOG.info( "addSdsets name=" + sd.getName() + " description=" + sd.getDescription() );
                try
                {
                    if ( sd.getType() == SDSet.SDType.STATIC )
                    {
                        adminMgr.createSsdSet( sd );
                    }
                    else
                    {
                        adminMgr.createDsdSet( sd );
                    }
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addSdsets name [" + sd.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteSdsets() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delsdset delsdset : delsdsets )
        {
            List<SDSetAnt> sds = delsdset.getSdset();
            for ( SDSetAnt sd : sds )
            {
                LOG.info( "deleteSdsets name=" + sd.getName() );
                try
                {
                    if ( sd.getSetType().equals( "STATIC" ) )
                    {
                        sd.setType( SDSet.SDType.STATIC );
                    }
                    else
                    {
                        sd.setType( SDSet.SDType.DYNAMIC );
                    }
                    adminMgr.deleteSsdSet( sd );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteSdsets name [" + sd.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addPermObjs() throws BuildException
    {
        // Loop through the entityclass elements
        for ( AddpermObj addpermObj : addpermObjs )
        {
            List<PermObj> permObjs = addpermObj.getPermObjs();
            for ( PermObj permObj : permObjs )
            {
                LOG.info( "addPermObjs objName=" + permObj.getObjName() + " description=" + permObj
                    .getDescription() + " orgUnit=" + permObj.getOu() + " type=" + permObj.getType() );
                try
                {
                    try
                    {
                        adminMgr.addPermObj( permObj );
                    }
                    catch ( org.openldap.fortress.SecurityException se )
                    {
                        // If Perm Object entity already there then call the udpate method.
                        if ( se.getErrorId() == GlobalErrIds.PERM_DUPLICATE )
                        {
                            adminMgr.updatePermObj( permObj );
                            System.out.println( CLS_NM + ".addPermObjs - update entity objName=" + permObj
                                .getObjName() );
                        }
                        else
                        {
                            throw se;
                        }
                    }
                }
                catch ( org.openldap.fortress.SecurityException se )
                {
                    LOG.warn( "addPermObjs objName [" + permObj.getObjName() + "] caught SecurityException=" +
                        se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deletePermObjs() throws BuildException
    {
        // Loop through the entityclass elements
        for ( DelpermObj delpermObj : delpermObjs )
        {
            List<PermObj> permObjs = delpermObj.getObjs();
            for ( PermObj permObj : permObjs )
            {
                LOG.info( "deletePermObjs objName=" + permObj.getObjName() + " description=" + permObj
                    .getDescription() );
                try
                {
                    adminMgr.deletePermObj( permObj );
                }
                catch ( org.openldap.fortress.SecurityException se )
                {
                    LOG.warn( "deletePermObjs name [" + permObj.getObjName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addPermOps() throws BuildException
    {
        // Loop through the entityclass elements
        for ( AddpermOp addpermOp : addpermOps )
        {
            List<PermAnt> permissions = addpermOp.getPermOps();
            for ( PermAnt permission : permissions )
            {
                LOG.info( "addPermOps name=" + permission.getOpName() + " objName=" + permission.getObjName() );
                try
                {
                    try
                    {
                        adminMgr.addPermission( permission );
                    }
                    catch ( org.openldap.fortress.SecurityException se )
                    {
                        // If Perm Object entity already there then call the udpate method.
                        if ( se.getErrorId() == GlobalErrIds.PERM_DUPLICATE )
                        {
                            adminMgr.updatePermission( permission );
                            LOG.info( "addPermOps - update entity - name=" + permission.getOpName() + " objName="
                                + permission.getObjName() );
                        }
                        else
                        {
                            throw se;
                        }
                    }
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addPermOps name [" + permission.getOpName() + "] objName [" + permission
                        .getObjName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deletePermOps() throws BuildException
    {
        // Loop through the entityclass elements
        for ( DelpermOp delpermOp : delpermOps )
        {
            List<PermAnt> permissions = delpermOp.getPermOps();
            for ( Permission permission : permissions )
            {
                LOG.info( "deletePermOps name=" + permission.getOpName() + " objName=" + permission.getObjName() );
                try
                {
                    adminMgr.deletePermission( permission );
                }
                catch ( org.openldap.fortress.SecurityException se )
                {
                    LOG.warn( "deletePermOps name [" + permission.getOpName() + "] objName[" + permission
                        .getObjName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addPermGrants() throws BuildException
    {
        // Loop through the entityclass elements
        for ( AddpermGrant addpermGrant : addpermGrants )
        {
            List<PermGrant> permGrants = addpermGrant.getPermGrants();
            for ( PermGrant permGrant : permGrants )
            {
                String info = "addPermGrants: Add permission grant object name=" + permGrant.getObjName() + " " +
                    "operation name=" + permGrant.getOpName() + " object id=" + permGrant.getObjId() + " role name="
                    + permGrant.getRoleNm() + " userId=" + permGrant.getUserId();
                LOG.info( info );
                try
                {
                    Permission perm = new Permission( permGrant.getObjName(), permGrant.getOpName(),
                        permGrant.isAdmin() );
                    perm.setOpName( permGrant.getOpName() );
                    perm.setObjId( permGrant.getObjId() );
                    if ( permGrant.getRoleNm() != null && permGrant.getRoleNm().length() > 0 )
                    {
                        adminMgr.grantPermission( perm, new Role( permGrant.getRoleNm() ) );
                    }
                    else if ( permGrant.getUserId() != null && permGrant.getUserId().length() > 0 )
                    {
                        adminMgr.grantPermission( perm, new User( permGrant.getUserId() ) );
                    }
                    else
                    {
                        String warning = "addPermGrants called without user or role set in xml";
                        LOG.warn( warning );
                    }
                }
                catch ( SecurityException se )
                {
                    String warning = "addPermGrants caught SecurityException=" + se;
                    LOG.warn( warning );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deletePermGrants() throws BuildException
    {
        // Loop through the entityclass elements
        for ( DelpermGrant delpermGrant : delpermGrants )
        {
            List<PermGrant> permGrants = delpermGrant.getPermGrants();
            for ( PermGrant permGrant : permGrants )
            {
                String info = "deletePermGrants: Delete permission grant object name=" + permGrant.getObjName() + " " +
                    "operation name=" + permGrant.getOpName() + " role name=" + permGrant.getRoleNm() + " userId=" +
                    permGrant.getUserId();
                LOG.info( info );
                try
                {
                    Permission perm = new Permission( permGrant.getObjName(), permGrant.getOpName(),
                        permGrant.isAdmin() );
                    perm.setOpName( permGrant.getOpName() );
                    perm.setObjId( permGrant.getObjId() );
                    if ( permGrant.getRoleNm() != null && permGrant.getRoleNm().length() > 0 )
                    {
                        adminMgr.revokePermission( perm, new Role( permGrant.getRoleNm() ) );
                    }
                    else if ( permGrant.getUserId() != null && permGrant.getUserId().length() > 0 )
                    {
                        adminMgr.revokePermission( perm, new User( permGrant.getUserId() ) );
                    }
                    else
                    {
                        String warning = "deletePermGrants called without user or role set in xml";
                        LOG.warn( warning );
                    }
                }
                catch ( SecurityException se )
                {
                    String warning = "deletePermGrants caught SecurityException=" + se;
                    LOG.warn( warning );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addPolicies() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addpwpolicy addpwpolicy : addpolicies )
        {
            List<PwPolicy> policies = addpwpolicy.getPolicies();
            for ( PwPolicy policy : policies )
            {
                LOG.info( "addPolicies name=" + policy.getName() );
                try
                {
                    policyMgr.add( policy );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addPolicies name [" + policy.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deletePolicies() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delpwpolicy delpwpolicy : delpolicies )
        {
            List<PwPolicy> policies = delpwpolicy.getPolicies();
            for ( PwPolicy policy : policies )
            {
                LOG.info( "deletePolicies name=" + policy.getName() );
                try
                {
                    policyMgr.delete( policy );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deletePolicies name [" + policy.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addContainers() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addcontainer addcontainer : addcontainers )
        {
            List<OrganizationalUnit> containers = addcontainer.getContainers();

            for ( OrganizationalUnit ou : containers )
            {
                LOG.info( "addContainers name=" + ou.getName() + " description=" + ou.getDescription() );
                try
                {
                    OrganizationalUnitP op = new OrganizationalUnitP();

                    if ( this.context != null )
                    {
                        ou.setContextId( this.context.getName() );
                    }

                    op.add( ou );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addContainers name [" + ou.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteContainers() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delcontainer delcontainer : delcontainers )
        {
            List<OrganizationalUnit> containers = delcontainer.getContainers();
            for ( OrganizationalUnit ou : containers )
            {
                LOG.info( "deleteContainers name=" + ou.getName() );
                try
                {
                    OrganizationalUnitP op = new OrganizationalUnitP();
                    op.delete( ou );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteContainers name [" + ou.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addSuffixes() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addsuffix addsuffix : addsuffixes )
        {
            List<Suffix> suffixes = addsuffix.getSuffixes();

            for ( Suffix suffix : suffixes )
            {
                LOG.info( "addSuffixes name=" + suffix.getName() + " description=" + suffix.getDescription() );
                try
                {
                    SuffixP sp = new SuffixP();
                    sp.add( suffix );
                }
                catch ( org.openldap.fortress.SecurityException se )
                {
                    LOG.warn( "addSuffixes name [" + suffix.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteSuffixes() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delsuffix delsuffix : delsuffixes )
        {
            List<Suffix> suffixes = delsuffix.getSuffixes();
            for ( Suffix suffix : suffixes )
            {
                LOG.info( "deleteSuffixes name=" + suffix.getName() );
                try
                {
                    SuffixP sp = new SuffixP();
                    sp.delete( suffix );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteSuffixes name [" + suffix.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addOrgunits() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addorgunit addorgunit : addorgunits )
        {
            List<OrgUnitAnt> ous = addorgunit.getOrgUnits();
            for ( OrgUnitAnt ou : ous )
            {
                LOG.info( "addOrgunits name=" + ou.getName() + " typeName=" + ou.getTypeName() + " description=" + ou
                    .getDescription() );
                try
                {
                    dAdminMgr.add( ou );
                }
                catch ( org.openldap.fortress.SecurityException se )
                {
                    LOG.warn( "addOrgunits name [" + ou.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void delOrgunits() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delorgunit delorgunit : delorgunits )
        {
            List<OrgUnitAnt> ous = delorgunit.getOrgUnits();
            for ( OrgUnitAnt ou : ous )
            {
                LOG.info( "deleteOrgunits name=" + ou.getName() + " typeName=" + ou.getTypeName() );
                try
                {
                    dAdminMgr.delete( ou );
                }
                catch ( org.openldap.fortress.SecurityException se )
                {
                    LOG.warn( "deleteOrgunits name [" + ou.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addUserOrgunitInheritances() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Adduserorgunitinheritance adduserorgunitinheritance : adduserorgunitinheritances )
        {
            List<Relationship> orgs = adduserorgunitinheritance.getRelationships();
            for ( Relationship relationship : orgs )
            {
                LOG.info( "addUserOrgunitInheritances parent=" + relationship.getParent() + " child=" + relationship
                    .getChild() );
                try
                {
                    dAdminMgr.addInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.USER ),
                        new OrgUnit( relationship.getChild(), OrgUnit.Type.USER ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addUserOrgunitInheritances parent [" + relationship.getParent() + "] child [" +
                        relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteUserOrgunitInheritances() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Deluserorgunitinheritance deluserorgunitinheritance : deluserorgunitinheritances )
        {
            List<Relationship> orgs = deluserorgunitinheritance.getRelationships();
            for ( Relationship relationship : orgs )
            {
                LOG.info( "deleteUserOrgunitInheritances parent=" + relationship.getParent() + " child=" +
                    relationship.getChild() );
                try
                {
                    dAdminMgr.deleteInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.USER ),
                        new OrgUnit( relationship.getChild(), OrgUnit.Type.USER ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteUserOrgunitInheritances parent [" + relationship.getParent() + "] child [" +
                        relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addPermOrgunitInheritances() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addpermorgunitinheritance addpermorgunitinheritance : addpermorgunitinheritances )
        {
            List<Relationship> orgs = addpermorgunitinheritance.getRelationships();
            for ( Relationship relationship : orgs )
            {
                LOG.info( "addPermOrgunitInheritances parent=" + relationship.getParent() + " child=" + relationship
                    .getChild() );
                try
                {
                    dAdminMgr.addInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.PERM ),
                        new OrgUnit( relationship.getChild(), OrgUnit.Type.PERM ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addPermOrgunitInheritances parent [" + relationship.getParent() + "] child [" +
                        relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deletePermOrgunitInheritances() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delpermorgunitinheritance delpermorgunitinheritance : delpermorgunitinheritances )
        {
            List<Relationship> orgs = delpermorgunitinheritance.getRelationships();
            for ( Relationship relationship : orgs )
            {
                LOG.info( "deletePermOrgunitInheritances parent=" + relationship.getParent() + " child=" +
                    relationship.getChild() );
                try
                {
                    dAdminMgr.deleteInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.PERM ),
                        new OrgUnit( relationship.getChild(), OrgUnit.Type.PERM ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deletePermOrgunitInheritances parent [" + relationship.getParent() + "] child [" +
                        relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addAdminRoles() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addadminrole addrole : addadminroles )
        {
            List<AdminRoleAnt> roles = addrole.getRoles();
            for ( AdminRoleAnt role : roles )
            {
                LOG.info( "addAdminRoles name=" + role.getName() + " description=" + role.getDescription() );
                try
                {
                    dAdminMgr.addRole( role );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addAdminRoles name [" + role.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteAdminRoles() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Deladminrole delrole : deladminroles )
        {
            List<AdminRoleAnt> roles = delrole.getRoles();
            for ( AdminRoleAnt role : roles )
            {
                LOG.info( "deleteAdminRoles name=" + role.getName() );
                try
                {
                    dAdminMgr.deleteRole( role );
                }
                catch ( org.openldap.fortress.SecurityException se )
                {
                    LOG.warn( "deleteAdminRoles name [" + role.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addAdminRoleInheritances() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addadminroleinheritance addadminroleinheritance : addadminroleinheritances )
        {
            List<Relationship> roles = addadminroleinheritance.getRelationships();
            for ( Relationship relationship : roles )
            {
                LOG.info( "addAdminRoleInheritances parent=" + relationship.getParent() + " child=" + relationship
                    .getChild() );
                try
                {
                    dAdminMgr.addInheritance( new AdminRole( relationship.getParent() ),
                        new AdminRole( relationship.getChild() ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addAdminRoleInheritances parent [" + relationship.getParent() + "] child [" +
                        relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteAdminRoleInheritances() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Deladminroleinheritance deladminroleinheritance : deladminroleinheritances )
        {
            List<Relationship> roles = deladminroleinheritance.getRelationships();
            for ( Relationship relationship : roles )
            {
                LOG.info( "deleteAdminRoleInheritances parent=" + relationship.getParent() + " child=" + relationship
                    .getChild() );
                try
                {
                    dAdminMgr.deleteInheritance( new AdminRole( relationship.getParent() ),
                        new AdminRole( relationship.getChild() ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteAdminRoleInheritances parent [" + relationship.getParent() + "] child [" +
                        relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addUserAdminRoles() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Adduseradminrole adduserrole : adduseradminroles )
        {
            List<UserAdminRole> userroles = adduserrole.getUserRoles();
            for ( UserAdminRole userRole : userroles )
            {
                LOG.info( "addUserAdminRoles userid=" + userRole.getUserId() + " role name=" + userRole.getName() );
                try
                {
                    //AdminRole role = new AdminRole(userRole);
                    dAdminMgr.assignUser( userRole );
                }
                catch ( SecurityException se )
                {
                    String warning = "addUserAdminRoles userId [" + userRole.getUserId() + "] role name [" + userRole
                        .getName() + "] caught SecurityException=" + se;
                    LOG.warn( warning );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void delUserAdminRoles() throws BuildException
    {
        // Loop through the entityclass elements
        for ( Deluseradminrole deluserrole : deluseradminroles )
        {
            List<UserAdminRole> userroles = deluserrole.getUserRoles();
            for ( UserAdminRole userRole : userroles )
            {
                LOG.info( "delUserAdminRoles userid=" + userRole.getUserId() + " role name=" + userRole.getName() );
                try
                {
                    dAdminMgr.deassignUser( userRole );
                }
                catch ( SecurityException se )
                {
                    String warning = "delUserAdminRoles userId [" + userRole.getUserId() + "] role name [" + userRole
                        .getName() + "] caught SecurityException=" + se;
                    LOG.warn( warning );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addConfig() throws BuildException
    {
        Properties props = new Properties();
        String configNodeName = "";
        // Loop through the entityclass elements
        for ( Addconfig addcfg : addconfig )
        {
            try
            {
                List<ConfigAnt> cfgs = addcfg.getConfig();
                for ( ConfigAnt cfg : cfgs )
                {
                    LOG.info( "addConfig" );
                    String val = cfg.getProps();
                    int indx = val.indexOf( GlobalIds.PROP_SEP );
                    if ( indx >= 1 )
                    {
                        String name = val.substring( 0, indx );
                        String value = val.substring( indx + 1 );
                        props.setProperty( name, value );
                    }
                }
                configNodeName = props.getProperty( GlobalIds.CONFIG_REALM );
                LOG.info( "addConfig realm name [" + configNodeName + "]" );
                cfgMgr.add( configNodeName, props );
            }
            catch ( SecurityException se )
            {
                String msgHdr = "addConfig realm name [" + configNodeName + "]";
                if ( se.getErrorId() == GlobalErrIds.FT_CONFIG_ALREADY_EXISTS )
                {
                    try
                    {
                        String warning = msgHdr + " entry already exists, attempt to update";
                        LOG.info( warning );
                        cfgMgr.update( configNodeName, props );
                        LOG.info( msgHdr + " update [" + configNodeName + "] successful" );
                    }
                    catch ( org.openldap.fortress.SecurityException se2 )
                    {
                        String warning = msgHdr + " update failed SecurityException=" + se2;
                        LOG.warn( warning );
                    }
                }
                else
                {
                    String warning = msgHdr + " failed SecurityException=" + se;
                    LOG.warn( warning );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteConfig() throws BuildException
    {
        Properties props = new Properties();
        String configNodeName = "";
        // Loop through the entityclass elements
        for ( Delconfig delcfg : delconfig )
        {
            try
            {
                List<ConfigAnt> cfgs = delcfg.getConfig();
                for ( ConfigAnt cfg : cfgs )
                {
                    String val = cfg.getProps();
                    int indx = val.indexOf( GlobalIds.PROP_SEP );
                    if ( indx >= 1 )
                    {
                        String name = val.substring( 0, indx );
                        String value = val.substring( indx + 1 );
                        props.setProperty( name, value );
                    }
                }
                configNodeName = props.getProperty( GlobalIds.CONFIG_REALM );
                LOG.info( "delConfig realm name [" + configNodeName + "]" );
                props.remove( GlobalIds.CONFIG_REALM );
                cfgMgr.delete( configNodeName, props );
            }
            catch ( org.openldap.fortress.SecurityException se )
            {
                String warning = "deleteConfig [" + configNodeName + "] caught SecurityException=" + se;
                LOG.warn( warning );
            }
        }
    }


    public static Properties getProperties( String inputString )
    {
        Properties props = new Properties();
        if ( inputString != null && inputString.length() > 0 )
        {
            StringTokenizer maxTkn = new StringTokenizer( inputString, SEMICOLON );
            if ( maxTkn.countTokens() > 0 )
            {
                while ( maxTkn.hasMoreTokens() )
                {
                    String val = maxTkn.nextToken();
                    int indx = val.indexOf( GlobalIds.PROP_SEP );
                    if ( indx >= 1 )
                    {
                        String name = val.substring( 0, indx );
                        String value = val.substring( indx + 1 );
                        props.setProperty( name, value );
                    }
                }
            }
        }
        return props;
    }

    public List<AddpermOp> getAddpermOps()
    {
        return addpermOps;
    }

    public List<AddpermObj> getAddpermObjs()
    {
        return addpermObjs;
    }

    public List<Adduser> getAddusers()
    {
        return addusers;
    }

    public List<Adduserrole> getAdduserroles()
    {
        return adduserroles;
    }

    public List<Addrole> getAddroles()
    {
        return addroles;
    }

    public List<Addsdset> getAddsdsets()
    {
        return addsdsets;
    }

    public List<Addroleinheritance> getAddroleinheritances()
    {
        return addroleinheritances;
    }

    public List<AddpermGrant> getAddpermGrants()
    {
        return addpermGrants;
    }

    public List<Addgroup> getAddgroups()
    {
        return addgroups;
    }
}