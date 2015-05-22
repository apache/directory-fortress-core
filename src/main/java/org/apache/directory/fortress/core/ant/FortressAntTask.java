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
package org.apache.directory.fortress.core.ant;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.input.InputRequest;
import org.apache.directory.fortress.core.cfg.Config;
import org.apache.directory.fortress.core.ldap.group.Group;
import org.apache.directory.fortress.core.ldap.group.GroupMgr;
import org.apache.directory.fortress.core.ldap.group.GroupMgrFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.AdminMgr;
import org.apache.directory.fortress.core.AdminMgrFactory;
import org.apache.directory.fortress.core.CfgException;
import org.apache.directory.fortress.core.DelAdminMgr;
import org.apache.directory.fortress.core.DelAdminMgrFactory;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.PwPolicyMgr;
import org.apache.directory.fortress.core.PwPolicyMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.cfg.ConfigMgr;
import org.apache.directory.fortress.core.cfg.ConfigMgrFactory;
import org.apache.directory.fortress.core.ldap.container.OrganizationalUnit;
import org.apache.directory.fortress.core.ldap.container.OrganizationalUnitP;
import org.apache.directory.fortress.core.ldap.suffix.Suffix;
import org.apache.directory.fortress.core.ldap.suffix.SuffixP;

import org.apache.directory.fortress.core.rbac.AdminRole;
import org.apache.directory.fortress.core.rbac.ClassUtil;
import org.apache.directory.fortress.core.rbac.Context;
import org.apache.directory.fortress.core.rbac.OrgUnit;
import org.apache.directory.fortress.core.rbac.OrgUnitAnt;
import org.apache.directory.fortress.core.rbac.PermGrant;
import org.apache.directory.fortress.core.rbac.PermObj;
import org.apache.directory.fortress.core.rbac.Permission;
import org.apache.directory.fortress.core.rbac.PwPolicy;
import org.apache.directory.fortress.core.rbac.Relationship;
import org.apache.directory.fortress.core.rbac.Role;
import org.apache.directory.fortress.core.rbac.SDSet;
import org.apache.directory.fortress.core.rbac.User;
import org.apache.directory.fortress.core.rbac.UserAdminRole;
import org.apache.directory.fortress.core.rbac.UserRole;
import org.apache.directory.fortress.core.util.Testable;
import org.apache.directory.fortress.core.util.attr.VUtil;


/**
 * This class implements Apache Ant custom task and is used to drive the Fortress Administrative APIs using XML files
 * .  The
 * methods in this class are not intended to be callable by outside programs.  The following APIs are supported:
 * <p/>
 * <ol>
 * <li>{@link org.apache.directory.fortress.core.AdminMgr}</li>
 * <li>{@link org.apache.directory.fortress.core.DelAdminMgr}</li>
 * <li>{@link org.apache.directory.fortress.core.PwPolicyMgr}</li>
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
 * <li>Delete User Role Assignments {@link org.apache.directory.fortress.core.AdminMgr#deassignUser(org.apache.directory.fortress.core.rbac.UserRole)}</li>
 * <li>Delete User AdminRole Assignments {@link DelAdminMgr#deassignUser(UserAdminRole)}</li>
 * <li>Revoke Permission Assignments Delete{@link AdminMgr#revokePermission(org.apache.directory.fortress.core.rbac.Permission,
 * org.apache.directory.fortress.core.rbac.Role)}</li>
 * <li>Delete Users {@link org.apache.directory.fortress.core.AdminMgr#disableUser(org.apache.directory.fortress.core.rbac.User)}</li>
 * <li>Delete Password Policies {@link org.apache.directory.fortress.core.PwPolicyMgr#delete(org.apache.directory.fortress.core.rbac.PwPolicy)}</li>
 *
 * <li>Delete Permission Operations {@link org.apache.directory.fortress.core.AdminMgr#deletePermission(org.apache.directory.fortress.core.rbac.Permission)
 * }</li>
 * <li>Delete Permission Objects {@link org.apache.directory.fortress.core.AdminMgr#deletePermObj(org.apache.directory.fortress.core.rbac.PermObj)}</li>
 *
 * <li>Delete SSD and DSD Sets {@link org.apache.directory.fortress.core.AdminMgr#deleteDsdSet(org.apache.directory.fortress.core.rbac.SDSet)} and {@link
 * org.apache.directory.fortress.core.AdminMgr#deleteSsdSet(org.apache.directory.fortress.core.rbac.SDSet)}</li>
 * <li>Delete RBAC Roles Inheritances {@link org.apache.directory.fortress.core.AdminMgr#deleteInheritance(org.apache.directory.fortress.core.rbac.Role,
 * org.apache.directory.fortress.core.rbac.Role)}</li>
 * <li>Delete RBAC Roles {@link org.apache.directory.fortress.core.AdminMgr#deleteRole(org.apache.directory.fortress.core.rbac.Role)}</li>
 * <li>Delete ARBAC Role Inheritances {@link DelAdminMgr#deleteInheritance(org.apache.directory.fortress.core.rbac.AdminRole,
 * org.apache.directory.fortress.core.rbac.AdminRole)}</li>
 * <li>Delete ARBAC Roles {@link org.apache.directory.fortress.core.DelAdminMgr#deleteRole(org.apache.directory.fortress.core.rbac.AdminRole)}</li>
 * <li>Delete User and Perm OU Inheritances {@link DelAdminMgr#deleteInheritance(org.apache.directory.fortress.core.rbac.OrgUnit,
 * org.apache.directory.fortress.core.rbac.OrgUnit)} USER and PERM</li>
 * <li>Delete User and Perm OUs {@link org.apache.directory.fortress.core.DelAdminMgr#delete(org.apache.directory.fortress.core.rbac.OrgUnit)} USER and
 * PERM</li>
 * <li>Delete Configuration Entries {@link org.apache.directory.fortress.core.cfg.ConfigMgr#delete(String, java.util.Properties)}</li>
 * <li>Delete Containers {@link OrganizationalUnitP#delete(OrganizationalUnit)}</li>
 * <li>Delete Suffix {@link org.apache.directory.fortress.core.ldap.suffix.SuffixP#delete(org.apache.directory.fortress.core.ldap.suffix.Suffix)}}</li>
 * <li>Add Suffix {@link SuffixP#add(Suffix)}}</li>
 * <li>Add Containers {@link OrganizationalUnitP#add(OrganizationalUnit)}</li>
 * <li>Add Configuration Parameters {@link ConfigMgr#add(String, java.util.Properties)}</li>
 * <li>Add User and Perm OUs {@link org.apache.directory.fortress.core.DelAdminMgr#add(org.apache.directory.fortress.core.rbac.OrgUnit)} USER and PERM</li>
 * <li>Add User and Perm OU Inheritances {@link DelAdminMgr#addInheritance(org.apache.directory.fortress.core.rbac.OrgUnit,
 * org.apache.directory.fortress.core.rbac.OrgUnit)} USER and PERM</li>
 * <li>Add ARBAC Roles {@link org.apache.directory.fortress.core.DelAdminMgr#addRole(org.apache.directory.fortress.core.rbac.AdminRole)}</li>
 * <li>Add ARBAC Role Inheritances {@link org.apache.directory.fortress.core.DelAdminMgr#addInheritance(org.apache.directory.fortress.core.rbac.AdminRole,
 * org.apache.directory.fortress.core.rbac.AdminRole)}</li>
 * <li>Add RBAC Roles {@link org.apache.directory.fortress.core.AdminMgr#addRole(org.apache.directory.fortress.core.rbac.Role)}</li>
 * <li>Add RBAC Role Inheritances {@link org.apache.directory.fortress.core.AdminMgr#addInheritance(org.apache.directory.fortress.core.rbac.Role,
 * org.apache.directory.fortress.core.rbac.Role)}</li>
 * <li>Add DSD and SSD Sets {@link org.apache.directory.fortress.core.AdminMgr#createDsdSet(org.apache.directory.fortress.core.rbac.SDSet)} and {@link org.apache.directory.fortress.core.AdminMgr#createSsdSet(org.apache.directory.fortress.core.rbac.SDSet)}</li>
 * <li>Add Permission Objects {@link org.apache.directory.fortress.core.AdminMgr#addPermObj(org.apache.directory.fortress.core.rbac.PermObj)}</li>
 * <li>Add Permission Operations {@link org.apache.directory.fortress.core.AdminMgr#addPermission(org.apache.directory.fortress.core.rbac.Permission)}</li>
 * <li>Add Password Policies {@link org.apache.directory.fortress.core.PwPolicyMgr#add(org.apache.directory.fortress.core.rbac.PwPolicy)}</li>
 * <li>Add Users {@link org.apache.directory.fortress.core.AdminMgr#addUser(org.apache.directory.fortress.core.rbac.User)}</li>
 * <li>Grant RBAC Permissions {@link org.apache.directory.fortress.core.AdminMgr#grantPermission(org.apache.directory.fortress.core.rbac.Permission,
 * org.apache.directory.fortress.core.rbac.Role)}</li>
 * <li>Assign ARBAC Roles {@link org.apache.directory.fortress.core.DelAdminMgr#assignUser(org.apache.directory.fortress.core.rbac.UserAdminRole)}</li>
 * <li>Assign RBAC Roles {@link org.apache.directory.fortress.core.AdminMgr#assignUser(org.apache.directory.fortress.core.rbac.UserRole)}</li>
 * </li>
 * </ol>
 * <p/>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class FortressAntTask extends Task implements InputHandler
{
    public static final boolean DEBUG = ( ( System.getProperty( "debug.admin" ) != null ) && ( System.getProperty(
        "debug.admin" ).equalsIgnoreCase( "true" ) ) );
    private static final String SEMICOLON = ";";
    private final List<Addconfig> addconfig = new ArrayList<>();
    private final List<Delconfig> delconfig = new ArrayList<>();
    private final List<Adduser> addusers = new ArrayList<>();
    private final List<Deluser> delusers = new ArrayList<>();
    private final List<Adduserrole> adduserroles = new ArrayList<>();
    private final List<Deluserrole> deluserroles = new ArrayList<>();
    private final List<Addrole> addroles = new ArrayList<>();
    private final List<Delrole> delroles = new ArrayList<>();
    private final List<Addsdset> addsdsets = new ArrayList<>();
    private final List<Addroleinheritance> addroleinheritances = new ArrayList<>();
    private final List<Delroleinheritance> delroleinheritances = new ArrayList<>();
    private final List<Delsdset> delsdsets = new ArrayList<>();
    private final List<AddpermOp> addpermOps = new ArrayList<>();
    private final List<DelpermOp> delpermOps = new ArrayList<>();
    private final List<AddpermObj> addpermObjs = new ArrayList<>();
    private final List<DelpermObj> delpermObjs = new ArrayList<>();
    private final List<AddpermGrant> addpermGrants = new ArrayList<>();
    private final List<DelpermGrant> delpermGrants = new ArrayList<>();
    private final List<Addpwpolicy> addpolicies = new ArrayList<>();
    private final List<Delpwpolicy> delpolicies = new ArrayList<>();
    private final List<Addcontainer> addcontainers = new ArrayList<>();
    private final List<Delcontainer> delcontainers = new ArrayList<>();
    private final List<Addsuffix> addsuffixes = new ArrayList<>();
    private final List<Delsuffix> delsuffixes = new ArrayList<>();
    private final List<Addorgunit> addorgunits = new ArrayList<>();
    private final List<Delorgunit> delorgunits = new ArrayList<>();
    private final List<Adduserorgunitinheritance> adduserorgunitinheritances = new ArrayList<>();
    private final List<Deluserorgunitinheritance> deluserorgunitinheritances = new ArrayList<>();
    private final List<Addpermorgunitinheritance> addpermorgunitinheritances = new ArrayList<>();
    private final List<Delpermorgunitinheritance> delpermorgunitinheritances = new ArrayList<>();
    private final List<Addadminrole> addadminroles = new ArrayList<>();
    private final List<Deladminrole> deladminroles = new ArrayList<>();
    private final List<Adduseradminrole> adduseradminroles = new ArrayList<>();
    private final List<Addadminroleinheritance> addadminroleinheritances = new ArrayList<>();
    private final List<Deladminroleinheritance> deladminroleinheritances = new ArrayList<>();
    private final List<Deluseradminrole> deluseradminroles = new ArrayList<>();
    private final List<Addcontext> addcontexts = new ArrayList<>();
    private final List<Addgroup> addgroups = new ArrayList<>();
    private final List<Delgroup> delgroups = new ArrayList<>();
    private final List<Addgroupmember> addgroupmembers = new ArrayList<>();
    private final List<Delgroupmember> delgroupmembers = new ArrayList<>();
    private final List<Addgroupproperty> addgroupproperties = new ArrayList<>();
    private final List<Delgroupproperty> delgroupproperties = new ArrayList<>();

    private ConfigMgr cfgMgr = null;
    private AdminMgr adminMgr = null;
    private DelAdminMgr dAdminMgr = null;
    private PwPolicyMgr policyMgr = null;
    private GroupMgr groupMgr = null;
    private static final String CLS_NM = FortressAntTask.class.getName();
    protected static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private Context context;


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
            adminMgr = AdminMgrFactory.createInstance( context.getName() );
            dAdminMgr = DelAdminMgrFactory.createInstance( context.getName() );
            policyMgr = PwPolicyMgrFactory.createInstance( context.getName() );
            groupMgr = GroupMgrFactory.createInstance( context.getName() );
        }
        catch ( SecurityException se )
        {
            LOG.warn( " FortressAntTask setContext caught SecurityException={}", se );
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
            LOG.warn( " FortressAntTask constructor caught SecurityException={}", se );
        }
    }


    /**
     * Used by Apache Ant to load data from xml into entities.
     *
     * @param request
     */
    public void handleInput( InputRequest request )
    {
        LOG.info( "handleInput request={}", request );
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
    private boolean isListNotNull( List<?> list )
    {
        return ( ( list != null ) && ( list.size() > 0 ) );
    }


    /**
     * @throws BuildException
     */
    public void execute() throws BuildException
    {
        LOG.info( "FORTRESS ANT TASK NAME : {}", getTaskName() );

        if ( isListNotNull( addcontexts ) )
        {
            setContext( addcontexts.get( 0 ).getContexts().get( 0 ) );
        }

        delUserRoles();
        delUserAdminRoles();
        deletePermGrants();
        deleteGroupProperties();
        deleteGroupMembers();
        deleteGroups();
        deleteUsers();
        deletePolicies();
        deletePermOps();
        deletePermObjs();
        deleteSdsets();
        deleteRoleInheritances();
        deleteRoles();
        deleteAdminRoleInheritances();
        deleteAdminRoles();
        deleteUserOrgunitInheritances();
        deletePermOrgunitInheritances();
        delOrgunits();
        deleteConfig();
        deleteContainers();
        deleteSuffixes();
        addSuffixes();
        addContainers();
        addConfig();
        addOrgunits();
        addUserOrgunitInheritances();
        addPermOrgunitInheritances();
        addAdminRoles();
        addAdminRoleInheritances();
        addRoles();
        addRoleInheritances();
        addSdsets();
        addPermObjs();
        addPermOps();
        addPolicies();
        addUsers();
        addGroups();
        addGroupMembers();
        addGroupProperties();
        addPermGrants();
        addUserAdminRoles();
        addUserRoles();

        testResults();
    }

    /**
     *
     */
    private void testResults()
    {
        // Test the results?
        if ( DEBUG )
        {
            // Verify the input XML file against what made it into the target LDAP directory:
            LOG.info( "DEBUG MODE" );
            try
            {
                String testClassName = Config.getProperty( getTaskName() );
                if ( !VUtil.isNotNullOrEmpty( testClassName ) )
                {
                    testClassName = "org.apache.directory.fortress.core.rbac.FortressAntLoadTest";
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
        if( addusers == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Adduser adduser : addusers )
        {
            List<UserAnt> users = adduser.getUsers();

            for ( UserAnt user : users )
            {
                LOG.info( "addUsers userid={} description={} orgUnit={}",
                    user.getUserId(), user.getDescription(), user.getOu() );
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
                            LOG.info( "addUsers - Update entity - userId={}", user.getUserId() );
                        }
                        else
                        {
                            throw se;
                        }
                    }
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addUsers userId [{}] caught SecurityException={}", user.getUserId(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteUsers() throws BuildException
    {
        if( delusers == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Deluser deluser : delusers )
        {
            List<UserAnt> users = deluser.getUsers();
            for ( UserAnt user : users )
            {
                LOG.info( "deleteUsers userid={}", user.getUserId() );
                try
                {
                    adminMgr.deleteUser( user );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteUsers userId [{}] caught SecurityException={}", user.getUserId(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addGroups() throws BuildException
    {
        if( addgroups == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addgroup addgroup : addgroups )
        {
            List<Group> groups = addgroup.getGroups();

            for ( Group group : groups )
            {
                LOG.info( "addGroups name={} description={}", group.getName(), group.getDescription() );
                try
                {
                    groupMgr.add( group );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addGroups name [{}] caught SecurityException={}", group.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteGroups() throws BuildException
    {
        if( delgroups == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Delgroup delgroup : delgroups )
        {
            List<Group> groups = delgroup.getGroups();
            for ( Group group : groups )
            {
                LOG.info( "deleteGroups name={}", group.getName() );
                try
                {
                    groupMgr.delete( group );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteGroups name [{}] caught SecurityException={}", group.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addGroupMembers() throws BuildException
    {
        if( addgroupmembers == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addgroupmember addgroupmember : addgroupmembers )
        {
            List<Group> groups = addgroupmember.getGroups();

            for ( Group group : groups )
            {
                List<String> members = group.getMembers();
                if ( VUtil.isNotNullOrEmpty( members ) )
                {
                    for ( String member : members )
                    {
                        LOG.info( "addGroupMembers name={}, member={}", group.getName(), member );
                        try
                        {
                            groupMgr.assign( group, member );
                        }
                        catch ( SecurityException se )
                        {
                            LOG.warn( "addGroupMembers name [{}], member [{}] caught SecurityException={}",
                                group.getName(), member, se );
                        }
                    }
                }
                else
                {
                    LOG.info( "addGroupMembers name={}, no member found", group.getName() );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteGroupMembers() throws BuildException
    {
        if( delgroupmembers == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Delgroupmember delgroupmember : delgroupmembers )
        {
            List<Group> groups = delgroupmember.getGroups();
            for ( Group group : groups )
            {
                if ( VUtil.isNotNullOrEmpty( group.getMembers() ) )
                {
                    for ( String member : group.getMembers() )
                    {
                        LOG.info( "deleteGroupmembers name={}, member={}", group.getName(), member );
                        try
                        {
                            groupMgr.deassign( group, member );
                        }
                        catch ( SecurityException se )
                        {
                            LOG.warn( "deleteGroupmembers name [{}], member [{}] caught SecurityException={}",
                                group.getName(), member, se );
                        }
                    }
                }
                else
                {
                    LOG.info( "deleteGroupmembers name={}, no member found", group.getName() );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addGroupProperties() throws BuildException
    {
        if( addgroupproperties == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addgroupproperty addgroupproperty : addgroupproperties )
        {
            List<Group> groups = addgroupproperty.getGroups();
            for ( Group group : groups )
            {
                if ( VUtil.isNotNullOrEmpty( group.getProperties() ) )
                {
                    for ( Enumeration<?> e = group.getProperties().propertyNames(); e.hasMoreElements(); )
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
                            LOG.warn( "addGroupProperties name [{}], key [{}], value [{}] caught SecurityException={}",
                                group.getName(), key, val, se );
                        }
                    }
                }
                else
                {
                    LOG.info( "addGroupProperties name={}, no properties found", group.getName() );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteGroupProperties() throws BuildException
    {
        if( delgroupproperties == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Delgroupproperty delgroupproperty : delgroupproperties )
        {
            List<Group> groups = delgroupproperty.getGroups();
            for ( Group group : groups )
            {
                if ( VUtil.isNotNullOrEmpty( group.getProperties() ) )
                {
                    for ( Enumeration<?> e = group.getProperties().propertyNames(); e.hasMoreElements(); )
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
                            LOG.warn(
                                "deleteGroupProperties name [{}], key [{}], value [{}] caught SecurityException={}",
                                group.getName(), key, val, se );
                        }
                    }
                }
                else
                {
                    LOG.info( "deleteGroupProperties name={}, no properties found", group.getName() );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addUserRoles() throws BuildException
    {
        if( adduserroles == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Adduserrole adduserrole : adduserroles )
        {
            List<UserRole> userroles = adduserrole.getUserRoles();
            for ( UserRole userRole : userroles )
            {
                LOG.info( "addUserRoles userid={} role name={}", userRole.getUserId(), userRole.getName() );

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
        if( deluserroles == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Deluserrole deluserrole : deluserroles )
        {
            List<UserRole> userroles = deluserrole.getUserRoles();
            for ( UserRole userRole : userroles )
            {
                LOG.info( "delUserRoles userid={} role name={}", userRole.getUserId(), userRole.getName() );
                try
                {
                    adminMgr.deassignUser( userRole );
                }
                catch ( SecurityException se )
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
        if( addroles == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addrole addrole : addroles )
        {
            List<Role> roles = addrole.getRoles();
            for ( Role role : roles )
            {
                LOG.info( "addRoles name={} description={}", role.getName(), role.getDescription() );
                try
                {
                    adminMgr.addRole( role );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addRoles name [{}] caught SecurityException={}", role.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteRoles() throws BuildException
    {
        if( delroles == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Delrole delrole : delroles )
        {
            List<Role> roles = delrole.getRoles();
            for ( Role role : roles )
            {
                LOG.info( "deleteRoles name={}", role.getName() );
                try
                {
                    adminMgr.deleteRole( role );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteRoles name [{}] caught SecurityException={}", role.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addRoleInheritances() throws BuildException
    {
        if( addroleinheritances == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addroleinheritance addroleinheritance : addroleinheritances )
        {
            List<Relationship> roles = addroleinheritance.getRelationships();
            for ( Relationship relationship : roles )
            {
                LOG.info( "addRoleInheritances parent={} child={}", relationship.getParent(), relationship.getChild() );
                try
                {
                    adminMgr.addInheritance( new Role( relationship.getParent() ), new Role( relationship.getChild()
                        ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addRoleInheritances parent [{}] child [{}] caught SecurityException={}",
                        relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteRoleInheritances() throws BuildException
    {
        if( delroleinheritances == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Delroleinheritance delroleinheritance : delroleinheritances )
        {
            List<Relationship> roles = delroleinheritance.getRelationships();
            for ( Relationship relationship : roles )
            {
                LOG.info( "deleteRoleInheritances parent={} child={}", relationship.getParent(),
                    relationship.getChild() );
                try
                {
                    adminMgr.deleteInheritance( new Role( relationship.getParent() ),
                        new Role( relationship.getChild() ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteRoleInheritances parent [{}] child [{}] caught SecurityException={}",
                        relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addSdsets() throws BuildException
    {
        if( addsdsets == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addsdset addsdset : addsdsets )
        {
            List<SDSetAnt> sds = addsdset.getSdset();
            for ( SDSetAnt sd : sds )
            {
                LOG.info( "addSdsets name={} description={}", sd.getName(), sd.getDescription() );
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
                    LOG.warn( "addSdsets name [{}] caught SecurityException={}", sd.getName() + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteSdsets() throws BuildException
    {
        if( delsdsets == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Delsdset delsdset : delsdsets )
        {
            List<SDSetAnt> sds = delsdset.getSdset();
            for ( SDSetAnt sd : sds )
            {
                LOG.info( "deleteSdsets name={}", sd.getName() );
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
                    LOG.warn( "deleteSdsets name [{}] caught SecurityException={}", sd.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addPermObjs() throws BuildException
    {
        if( addpermObjs == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( AddpermObj addpermObj : addpermObjs )
        {
            List<PermObj> permObjs = addpermObj.getPermObjs();
            for ( PermObj permObj : permObjs )
            {
                LOG.info( "addPermObjs objName={} description={} orgUnit={} type={}",
                    permObj.getObjName(), permObj.getDescription(), permObj.getOu(), permObj.getType() );
                try
                {
                    try
                    {
                        adminMgr.addPermObj( permObj );
                    }
                    catch ( SecurityException se )
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
                catch ( SecurityException se )
                {
                    LOG.warn( "addPermObjs objName [{}] caught SecurityException={}",
                        permObj.getObjName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deletePermObjs() throws BuildException
    {
        if( delpermObjs == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( DelpermObj delpermObj : delpermObjs )
        {
            List<PermObj> permObjs = delpermObj.getObjs();
            for ( PermObj permObj : permObjs )
            {
                LOG.info( "deletePermObjs objName={} description={}",
                    permObj.getObjName(), permObj.getDescription() );
                try
                {
                    adminMgr.deletePermObj( permObj );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deletePermObjs name [{}] caught SecurityException={}", permObj.getObjName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addPermOps() throws BuildException
    {
        if( addpermOps == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( AddpermOp addpermOp : addpermOps )
        {
            List<PermAnt> permissions = addpermOp.getPermOps();
            for ( PermAnt permission : permissions )
            {
                LOG.info( "addPermOps name={} objName={}", permission.getOpName(), permission.getObjName() );
                try
                {
                    try
                    {
                        adminMgr.addPermission( permission );
                    }
                    catch ( SecurityException se )
                    {
                        // If Perm Object entity already there then call the udpate method.
                        if ( se.getErrorId() == GlobalErrIds.PERM_DUPLICATE )
                        {
                            adminMgr.updatePermission( permission );
                            LOG.info( "addPermOps - update entity - name={} objName={}",
                                permission.getOpName(), permission.getObjName() );
                        }
                        else
                        {
                            throw se;
                        }
                    }
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addPermOps name [{}] objName [{}] caught SecurityException={}",
                        permission.getOpName(), permission.getObjName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deletePermOps() throws BuildException
    {
        if( delpermOps == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( DelpermOp delpermOp : delpermOps )
        {
            List<PermAnt> permissions = delpermOp.getPermOps();
            for ( Permission permission : permissions )
            {
                LOG.info( "deletePermOps name={} objName={}", permission.getOpName(), permission.getObjName() );
                try
                {
                    adminMgr.deletePermission( permission );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deletePermOps name [{}] objName[{}] caught SecurityException={}",
                        permission.getOpName(), permission.getObjName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addPermGrants() throws BuildException
    {
        if( addpermGrants == null )
        {
            return;
        }

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
        if( delpermGrants == null )
        {
            return;
        }

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
        if( addpolicies == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addpwpolicy addpwpolicy : addpolicies )
        {
            List<PwPolicy> policies = addpwpolicy.getPolicies();
            for ( PwPolicy policy : policies )
            {
                LOG.info( "addPolicies name={}", policy.getName() );
                try
                {
                    policyMgr.add( policy );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addPolicies name [{}] caught SecurityException={}", policy.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deletePolicies() throws BuildException
    {
        if( delpolicies == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Delpwpolicy delpwpolicy : delpolicies )
        {
            List<PwPolicy> policies = delpwpolicy.getPolicies();
            for ( PwPolicy policy : policies )
            {
                LOG.info( "deletePolicies name={}", policy.getName() );
                try
                {
                    policyMgr.delete( policy );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deletePolicies name [{}] caught SecurityException={}", policy.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addContainers() throws BuildException
    {
        if( addcontainers == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addcontainer addcontainer : addcontainers )
        {
            List<OrganizationalUnit> containers = addcontainer.getContainers();

            for ( OrganizationalUnit ou : containers )
            {
                LOG.info( "addContainers name={} description={}", ou.getName(), ou.getDescription() );
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
                    LOG.warn( "addContainers name [{}] caught SecurityException={}", ou.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteContainers() throws BuildException
    {
        if( delcontainers == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Delcontainer delcontainer : delcontainers )
        {
            List<OrganizationalUnit> containers = delcontainer.getContainers();
            for ( OrganizationalUnit ou : containers )
            {
                LOG.info( "deleteContainers name={}", ou.getName() );
                try
                {
                    OrganizationalUnitP op = new OrganizationalUnitP();
                    op.delete( ou );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteContainers name [{}] caught SecurityException={}", ou.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addSuffixes() throws BuildException
    {
        if( addsuffixes == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addsuffix addsuffix : addsuffixes )
        {
            List<Suffix> suffixes = addsuffix.getSuffixes();

            for ( Suffix suffix : suffixes )
            {
                LOG.info( "addSuffixes name={} description={}", suffix.getName(), suffix.getDescription() );
                try
                {
                    SuffixP sp = new SuffixP();
                    sp.add( suffix );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addSuffixes name [{}] caught SecurityException={}", suffix.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteSuffixes() throws BuildException
    {
        if( delsuffixes == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Delsuffix delsuffix : delsuffixes )
        {
            List<Suffix> suffixes = delsuffix.getSuffixes();
            for ( Suffix suffix : suffixes )
            {
                LOG.info( "deleteSuffixes name={}", suffix.getName() );
                try
                {
                    SuffixP sp = new SuffixP();
                    sp.delete( suffix );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteSuffixes name [{}] caught SecurityException={}", suffix.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addOrgunits() throws BuildException
    {
        if( addorgunits == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addorgunit addorgunit : addorgunits )
        {
            List<OrgUnitAnt> ous = addorgunit.getOrgUnits();
            for ( OrgUnitAnt ou : ous )
            {
                LOG.info( "addOrgunits name={} typeName={} description={}",
                    ou.getName(), ou.getTypeName(), ou.getDescription() );
                try
                {
                    dAdminMgr.add( ou );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addOrgunits name [{}] caught SecurityException={}", ou.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void delOrgunits() throws BuildException
    {
        if( delorgunits == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Delorgunit delorgunit : delorgunits )
        {
            List<OrgUnitAnt> ous = delorgunit.getOrgUnits();
            for ( OrgUnitAnt ou : ous )
            {
                LOG.info( "deleteOrgunits name={} typeName={}", ou.getName(), ou.getTypeName() );
                try
                {
                    dAdminMgr.delete( ou );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteOrgunits name [{}] caught SecurityException={}", ou.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addUserOrgunitInheritances() throws BuildException
    {
        if( adduserorgunitinheritances == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Adduserorgunitinheritance adduserorgunitinheritance : adduserorgunitinheritances )
        {
            List<Relationship> orgs = adduserorgunitinheritance.getRelationships();
            for ( Relationship relationship : orgs )
            {
                LOG.info( "addUserOrgunitInheritances parent={} child={}",
                    relationship.getParent(), relationship.getChild() );
                try
                {
                    dAdminMgr.addInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.USER ),
                        new OrgUnit( relationship.getChild(), OrgUnit.Type.USER ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addUserOrgunitInheritances parent [{}] child [{}] caught SecurityException={}",
                        relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteUserOrgunitInheritances() throws BuildException
    {
        if( deluserorgunitinheritances == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Deluserorgunitinheritance deluserorgunitinheritance : deluserorgunitinheritances )
        {
            List<Relationship> orgs = deluserorgunitinheritance.getRelationships();
            for ( Relationship relationship : orgs )
            {
                LOG.info( "deleteUserOrgunitInheritances parent={} child={}" +
                    relationship.getParent(), relationship.getChild() );
                try
                {
                    dAdminMgr.deleteInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.USER ),
                        new OrgUnit( relationship.getChild(), OrgUnit.Type.USER ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteUserOrgunitInheritances parent [{}] child [{}] caught SecurityException={}",
                        relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addPermOrgunitInheritances() throws BuildException
    {
        if( addpermorgunitinheritances == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addpermorgunitinheritance addpermorgunitinheritance : addpermorgunitinheritances )
        {
            List<Relationship> orgs = addpermorgunitinheritance.getRelationships();
            for ( Relationship relationship : orgs )
            {
                LOG.info( "addPermOrgunitInheritances parent={} child={}",
                    relationship.getParent(), relationship.getChild() );
                try
                {
                    dAdminMgr.addInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.PERM ),
                        new OrgUnit( relationship.getChild(), OrgUnit.Type.PERM ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addPermOrgunitInheritances parent [{}] child [{}] caught SecurityException={}",
                        relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deletePermOrgunitInheritances() throws BuildException
    {
        if( delpermorgunitinheritances == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Delpermorgunitinheritance delpermorgunitinheritance : delpermorgunitinheritances )
        {
            List<Relationship> orgs = delpermorgunitinheritance.getRelationships();
            for ( Relationship relationship : orgs )
            {
                LOG.info( "deletePermOrgunitInheritances parent={} child={}",
                    relationship.getParent(), relationship.getChild() );
                try
                {
                    dAdminMgr.deleteInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.PERM ),
                        new OrgUnit( relationship.getChild(), OrgUnit.Type.PERM ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deletePermOrgunitInheritances parent [{}] child [{}] caught SecurityException={}",
                        relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addAdminRoles() throws BuildException
    {
        if( addadminroles == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addadminrole addrole : addadminroles )
        {
            List<AdminRoleAnt> roles = addrole.getRoles();
            for ( AdminRoleAnt role : roles )
            {
                LOG.info( "addAdminRoles name={} description={}", role.getName(), role.getDescription() );
                try
                {
                    dAdminMgr.addRole( role );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addAdminRoles name [{}] caught SecurityException={}", role.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteAdminRoles() throws BuildException
    {
        if( deladminroles == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Deladminrole delrole : deladminroles )
        {
            List<AdminRoleAnt> roles = delrole.getRoles();
            for ( AdminRoleAnt role : roles )
            {
                LOG.info( "deleteAdminRoles name={}", role.getName() );
                try
                {
                    dAdminMgr.deleteRole( role );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteAdminRoles name [{}] caught SecurityException={}", role.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addAdminRoleInheritances() throws BuildException
    {
        if( addadminroleinheritances == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addadminroleinheritance addadminroleinheritance : addadminroleinheritances )
        {
            List<Relationship> roles = addadminroleinheritance.getRelationships();
            for ( Relationship relationship : roles )
            {
                LOG.info( "addAdminRoleInheritances parent={} child={}",
                    relationship.getParent(), relationship.getChild() );
                try
                {
                    dAdminMgr.addInheritance( new AdminRole( relationship.getParent() ),
                        new AdminRole( relationship.getChild() ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addAdminRoleInheritances parent [{}] child [{}] caught SecurityException={}",
                        relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteAdminRoleInheritances() throws BuildException
    {
        if( deladminroleinheritances == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Deladminroleinheritance deladminroleinheritance : deladminroleinheritances )
        {
            List<Relationship> roles = deladminroleinheritance.getRelationships();
            for ( Relationship relationship : roles )
            {
                LOG.info( "deleteAdminRoleInheritances parent={} child={}",
                    relationship.getParent(), relationship.getChild() );
                try
                {
                    dAdminMgr.deleteInheritance( new AdminRole( relationship.getParent() ),
                        new AdminRole( relationship.getChild() ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteAdminRoleInheritances parent [{}] child [{}] caught SecurityException={}",
                        relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addUserAdminRoles() throws BuildException
    {
        if( adduseradminroles == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Adduseradminrole adduserrole : adduseradminroles )
        {
            List<UserAdminRole> userroles = adduserrole.getUserRoles();
            for ( UserAdminRole userRole : userroles )
            {
                LOG.info( "addUserAdminRoles userid={} role name={}", userRole.getUserId(), userRole.getName() );
                try
                {
                    //AdminRole role = new AdminRole(userRole);
                    dAdminMgr.assignUser( userRole );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addUserAdminRoles userId [{}] role name [{}] caught SecurityException={}",
                        userRole.getUserId(), userRole.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void delUserAdminRoles() throws BuildException
    {
        if( deluseradminroles == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Deluseradminrole deluserrole : deluseradminroles )
        {
            List<UserAdminRole> userroles = deluserrole.getUserRoles();
            for ( UserAdminRole userRole : userroles )
            {
                LOG.info( "delUserAdminRoles userid={} role name={}", userRole.getUserId(), userRole.getName() );
                try
                {
                    dAdminMgr.deassignUser( userRole );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "delUserAdminRoles userId [{}] role name [{}] caught SecurityException={}",
                        userRole.getUserId(), userRole.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addConfig() throws BuildException
    {
        if( addconfig == null )
        {
            return;
        }

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
                LOG.info( "addConfig realm name [{}]", configNodeName );
                cfgMgr.add( configNodeName, props );
            }
            catch ( SecurityException se )
            {
                if ( se.getErrorId() == GlobalErrIds.FT_CONFIG_ALREADY_EXISTS )
                {
                    try
                    {
                        LOG.info( "addConfig realm name [{}] entry already exists, attempt to update", configNodeName );
                        cfgMgr.update( configNodeName, props );
                        LOG.info( "addConfig realm name [{}] update [{}] successful", configNodeName, configNodeName );
                    }
                    catch ( SecurityException se2 )
                    {
                        LOG.warn( "addConfig realm name [{}] update failed SecurityException={}", configNodeName, se2 );
                    }
                }
                else
                {
                    LOG.warn( "addConfig realm name [{}] failed SecurityException={}", configNodeName, se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteConfig() throws BuildException
    {
        if( delconfig == null )
        {
            return;
        }

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
                LOG.info( "delConfig realm name [{}]", configNodeName );
                props.remove( GlobalIds.CONFIG_REALM );
                cfgMgr.delete( configNodeName, props );
            }
            catch ( SecurityException se )
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