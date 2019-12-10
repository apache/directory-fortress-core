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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.AdminMgr;
import org.apache.directory.fortress.core.AdminMgrFactory;
import org.apache.directory.fortress.core.CfgException;
import org.apache.directory.fortress.core.ConfigMgr;
import org.apache.directory.fortress.core.ConfigMgrFactory;
import org.apache.directory.fortress.core.DelAdminMgr;
import org.apache.directory.fortress.core.DelAdminMgrFactory;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.GroupMgr;
import org.apache.directory.fortress.core.GroupMgrFactory;
import org.apache.directory.fortress.core.PwPolicyMgr;
import org.apache.directory.fortress.core.PwPolicyMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.impl.OrganizationalUnitP;
import org.apache.directory.fortress.core.impl.SuffixP;
import org.apache.directory.fortress.core.model.*;
import org.apache.directory.fortress.core.util.PropUtil;
import org.apache.directory.fortress.core.util.ClassUtil;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.Testable;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.input.InputRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class implements Apache Ant custom task and is used to drive the Fortress Administrative APIs using XML files.
 * The methods in this class are not intended to be callable by outside programs.  The following APIs are supported:
 * <ol>
 *   <li>{@link org.apache.directory.fortress.core.AdminMgr}</li>
 *   <li>{@link org.apache.directory.fortress.core.DelAdminMgr}</li>
 *   <li>{@link org.apache.directory.fortress.core.PwPolicyMgr}</li>
 *   <li>{@link ConfigMgr}</li>
 * </ol>
 * <p>
 * using the custom Ant task that is implemented in this class.  The format of the XML is flat and consists of entity
 * names along with their attributes.
 * <h3>
 *   This class will process xml formatted requests with the following tags:
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
 *   <li>
 *     Delete User Role Assignments {@link org.apache.directory.fortress.core.AdminMgr#deassignUser(
 *     org.apache.directory.fortress.core.model.UserRole)}
 *   </li>
 *   <li>Delete User AdminRole Assignments {@link DelAdminMgr#deassignUser(UserAdminRole)}</li>
 *   <li>
 *     Revoke Permission Assignments Delete{@link AdminMgr#revokePermission(
 *     org.apache.directory.fortress.core.model.Permission,
 *     org.apache.directory.fortress.core.model.Role)}
 *   </li>
 *   <li>
 *     Delete Users {@link org.apache.directory.fortress.core.AdminMgr#disableUser(
 *     org.apache.directory.fortress.core.model.User)}
 *   </li>
 *   <li>
 *     Delete Password Policies {@link org.apache.directory.fortress.core.PwPolicyMgr#delete(
 *     org.apache.directory.fortress.core.model.PwPolicy)}
 *   </li>
 *   <li>
 *     Delete Permission Operations {@link org.apache.directory.fortress.core.AdminMgr#deletePermission(
 *     org.apache.directory.fortress.core.model.Permission)}
 *   </li>
 *   <li>
 *     Delete Permission Objects {@link org.apache.directory.fortress.core.AdminMgr#deletePermObj(
 *     org.apache.directory.fortress.core.model.PermObj)}
 *   </li>
 *   <li>
 *     Delete SSD and DSD Sets {@link org.apache.directory.fortress.core.AdminMgr#deleteDsdSet(
 *     org.apache.directory.fortress.core.model.SDSet)} and 
 *     {@link org.apache.directory.fortress.core.AdminMgr#deleteSsdSet(org.apache.directory.fortress.core.model.SDSet)}
 *   </li>
 *   <li>
 *     Delete RBAC Roles Inheritances {@link org.apache.directory.fortress.core.AdminMgr#deleteInheritance(
 *     org.apache.directory.fortress.core.model.Role, org.apache.directory.fortress.core.model.Role)}
 *   </li>
 *   <li>
 *     Delete RBAC Roles {@link org.apache.directory.fortress.core.AdminMgr#deleteRole(
 *     org.apache.directory.fortress.core.model.Role)}
 *   </li>
 *   <li>
 *     Delete ARBAC Role Inheritances {@link DelAdminMgr#deleteInheritance(
 *     org.apache.directory.fortress.core.model.AdminRole,org.apache.directory.fortress.core.model.AdminRole)}
 *   </li>
 *   <li>
 *     Delete ARBAC Roles {@link org.apache.directory.fortress.core.DelAdminMgr#deleteRole(
 *     org.apache.directory.fortress.core.model.AdminRole)}
 *   </li>
 *   <li>
 *     Delete User and Perm OU Inheritances {@link DelAdminMgr#deleteInheritance(
 *     org.apache.directory.fortress.core.model.OrgUnit, org.apache.directory.fortress.core.model.OrgUnit)} USER and PERM
 *   </li>
 *   <li>
 *     Delete User and Perm OUs {@link org.apache.directory.fortress.core.DelAdminMgr#delete(
 *     org.apache.directory.fortress.core.model.OrgUnit)} USER and PERM
 *   </li>
 *   <li>
 *     Delete Configuration Entries {@link org.apache.directory.fortress.core.ConfigMgr#delete(String, java.util.Properties)}
 *   </li>
 *   <li>Delete Containers {@link OrganizationalUnitP#delete(OrganizationalUnit)}</li>
 *   <li>
 *     Delete Suffix {@link org.apache.directory.fortress.core.impl.SuffixP#delete(
 *     org.apache.directory.fortress.core.model.Suffix)}}
 *   </li>
 *   <li>Add Suffix {@link SuffixP#add(Suffix)}}</li>
 *   <li>Add Containers {@link OrganizationalUnitP#add(OrganizationalUnit)}</li>
 *   <li>Add Configuration Parameters {@link ConfigMgr#add(Configuration)}</li>
 *   <li>
 *     Add User and Perm OUs {@link org.apache.directory.fortress.core.DelAdminMgr#add(
 *     org.apache.directory.fortress.core.model.OrgUnit)} USER and PERM
 *   </li>
 *   <li>
 *     Add User and Perm OU Inheritances {@link DelAdminMgr#addInheritance(org.apache.directory.fortress.core.model.OrgUnit,
 *     org.apache.directory.fortress.core.model.OrgUnit)} USER and PERM
 *   </li>
 *   <li>
 *     Add ARBAC Roles {@link org.apache.directory.fortress.core.DelAdminMgr#addRole(
 *     org.apache.directory.fortress.core.model.AdminRole)}
 *   </li>
 *   <li>
 *     Add ARBAC Role Inheritances {@link org.apache.directory.fortress.core.DelAdminMgr#addInheritance(
 *     org.apache.directory.fortress.core.model.AdminRole, org.apache.directory.fortress.core.model.AdminRole)}
 *   </li>
 *   <li>
 *     Add RBAC Roles {@link org.apache.directory.fortress.core.AdminMgr#addRole(
 *     org.apache.directory.fortress.core.model.Role)}
 *   </li>
 *   <li>
 *     Add RBAC Role Inheritances {@link org.apache.directory.fortress.core.AdminMgr#addInheritance(
 *     org.apache.directory.fortress.core.model.Role, org.apache.directory.fortress.core.model.Role)}
 *   </li>
 *   <li>
 *     Add DSD and SSD Sets {@link org.apache.directory.fortress.core.AdminMgr#createDsdSet(
 *     org.apache.directory.fortress.core.model.SDSet)} and 
 *     {@link org.apache.directory.fortress.core.AdminMgr#createSsdSet(org.apache.directory.fortress.core.model.SDSet)}
 *   </li>
 *   <li>
 *     Add Permission Objects {@link org.apache.directory.fortress.core.AdminMgr#addPermObj(
 *     org.apache.directory.fortress.core.model.PermObj)}
 *   </li>
 *   <li>
 *     Add Permission Operations {@link org.apache.directory.fortress.core.AdminMgr#addPermission(
 *     org.apache.directory.fortress.core.model.Permission)}
 *   </li>
 *   <li>
 *     Add Password Policies {@link org.apache.directory.fortress.core.PwPolicyMgr#add(
 *     org.apache.directory.fortress.core.model.PwPolicy)}
 *   </li>
 *   <li>
 *     Add Users {@link org.apache.directory.fortress.core.AdminMgr#addUser(org.apache.directory.fortress.core.model.User)}
 *   </li>
 *   <li>
 *     Grant RBAC Permissions {@link org.apache.directory.fortress.core.AdminMgr#grantPermission(
 *     org.apache.directory.fortress.core.model.Permission, org.apache.directory.fortress.core.model.Role)}
 *   </li>
 *   <li>
 *     Assign ARBAC Roles {@link org.apache.directory.fortress.core.DelAdminMgr#assignUser(
 *     org.apache.directory.fortress.core.model.UserAdminRole)}
 *   </li>
 *   <li>
 *     Assign RBAC Roles {@link org.apache.directory.fortress.core.AdminMgr#assignUser(
 *     org.apache.directory.fortress.core.model.UserRole)}
 *   </li>
 * </ol>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class FortressAntTask extends Task implements InputHandler
{
    public static final boolean DEBUG = ( ( System.getProperty( "debug.admin" ) != null ) && ( System.getProperty(
        "debug.admin" ).equalsIgnoreCase( "true" ) ) );
    private static final String SEMICOLON = ";";
    private final List<Addconfig> addconfig = new ArrayList<>();
    private final List<Updconfig> updconfig = new ArrayList<>();
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
    private final List<Addroleconstraint> addroleconstraints = new ArrayList<>();
    private final List<Delroleconstraint> delroleconstraints = new ArrayList<>();

    private ConfigMgr cfgMgr = null;
    private AdminMgr adminMgr = null;
    private DelAdminMgr dAdminMgr = null;
    private PwPolicyMgr policyMgr = null;
    private GroupMgr groupMgr = null;
    private static final String CLS_NM = FortressAntTask.class.getName();
    protected static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private Context context;
    // This system property can be used to set the default tenant id:
    private static final String TENANT = System.getProperty( "tenant" );
    private String tenant;

    public String getTenant()
    {
        return tenant;
    }

    /**
     * Load the entity with data.
     *
     * @param addcontext contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddcontext( Addcontext addcontext )
    {
        this.addcontexts.add( addcontext );
    }

    /**
     * This method is used as an alternative way to set tenant id.  By setting this element in xml file:
     * <addcontext>
     *     <context name="${tenant}"/>
     </addcontext>
     *
     *
     * @param context contains the tenant info.
     */
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
            // This value is set by system property "tenant":
            if( StringUtils.isEmpty( TENANT ) || TENANT.equals( "${tenant}" ) )
            {
                // Use the default context:
                this.tenant = GlobalIds.HOME;
            }
            else
            {
                this.tenant = TENANT;
                LOG.info( "FortressAntTask constructor using tenant={}", tenant );
            }
            cfgMgr = ConfigMgrFactory.createInstance();
            adminMgr = AdminMgrFactory.createInstance( this.tenant );
            dAdminMgr = DelAdminMgrFactory.createInstance( this.tenant );
            policyMgr = PwPolicyMgrFactory.createInstance( this.tenant );
            groupMgr = GroupMgrFactory.createInstance( this.tenant );
        }
        catch ( SecurityException se )
        {
            LOG.warn( " FortressAntTask constructor tenant={}, caught SecurityException={}", this.tenant, se );
        }
    }

    /**
     * Used by Apache Ant to load data from xml into entities.
     *
     * @param request The input request
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
     * @param updcfg contains the ant initialized data entities to be handed off for further processing.
     */
    public void addUpdconfig( Updconfig updcfg )
    {
        this.updconfig.add( updcfg );
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
     * Load the entity with data.
     *
     * @param addroleconstraint contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddroleconstraint( Addroleconstraint addroleconstraint )
    {
        this.addroleconstraints.add( addroleconstraint );
    }


    /**
     * Load the entity with data.
     *
     * @param delroleconstraint contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelroleconstraint( Delroleconstraint delroleconstraint )
    {
        this.delroleconstraints.add( delroleconstraint );
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
     * @throws BuildException An error occurred while building
     */
    public void execute() throws BuildException
    {
        LOG.info( "FORTRESS ANT TASK NAME : {}", getTaskName() );

        if ( isListNotNull( addcontexts ) )
        {
            setContext( addcontexts.get( 0 ).getContexts().get( 0 ) );
        }

        delRoleConstraints();
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
        updConfig();
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
        addRoleConstraints();

        testResults();

        System.exit( 0 );
    }

    /**
     * @throws BuildException An error occurred while building
     */
    private void testResults() throws BuildException
    {
        // Test the results?
        if ( DEBUG )
        {
            // Verify the input XML file against what made it into the target LDAP directory:
            LOG.info( "DEBUG MODE" );
            try
            {
                String testClassName = Config.getInstance().getProperty( getTaskName() );
                if ( StringUtils.isEmpty( testClassName ) )
                {
                    testClassName = "org.apache.directory.fortress.core.impl.FortressAntLoadTest";
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
     * @throws BuildException An error occurred while building
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
                LOG.info( "addUsers tenant={} userid={} description={} orgUnit={}",
                    getTenant(), user.getUserId(), user.getDescription(), user.getOu() );
                try
                {
                    addUser( user );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addUsers userId [{}] caught SecurityException={}", user.getUserId(), se );
                }
            }
        }
    }

    /**
     * Utility method called by addUsers()
     *
     * @param user
     * @throws SecurityException
     */
    private void addUser(User user) throws SecurityException
    {
        try
        {
            adminMgr.addUser( user );
            assignUser( user );
        }
        catch ( SecurityException se )
        {
            // If User entity already there then call the udpate method.
            if ( se.getErrorId() == GlobalErrIds.USER_ID_DUPLICATE )
            {
                adminMgr.updateUser( user );
                assignUser( user );
                LOG.info( "addUsers tenant={} Update entity - userId={}", getTenant(), user.getUserId() );
            }
            else
            {
                throw se;
            }
        }
    }

    /**
     * Utility method to assign roles to user.
     *
     * @param user
     * @throws SecurityException
     */
    private void assignUser( User user ) throws SecurityException
    {
        if ( CollectionUtils.isNotEmpty( user.getRoles() ) )
        {
            for ( UserRole uRole : user.getRoles() )
            {
                adminMgr.assignUser( uRole );
            }
        }
        if ( CollectionUtils.isNotEmpty( user.getAdminRoles() ) )
        {
            for ( UserAdminRole uAdminRoleRole : user.getAdminRoles() )
            {
                dAdminMgr.assignUser( uAdminRoleRole );
            }
        }
    }

    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "deleteUsers tenant={} userid={}", getTenant(), user.getUserId() );
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
     * @throws BuildException An error occurred while building
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
                LOG.info( "addGroups tenant={} name={} description={}", getTenant(), group.getName(), group.getDescription() );
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
     * @throws BuildException An error occurred while building
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
                LOG.info( "deleteGroups tenant={} name={}", getTenant(), group.getName() );
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
     * @throws BuildException An error occurred while building
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
                if ( CollectionUtils.isNotEmpty( members ) )
                {
                    for ( String member : members )
                    {
                        LOG.info( "addGroupMembers tenant={} name={}, member={}", getTenant(), group.getName(), member );
                        try
                        {
                            groupMgr.assign( group, member );
                        }
                        catch ( SecurityException se )
                        {
                            LOG.warn( "addGroupMembers tenant={} name [{}], member [{}] caught SecurityException={}",
                                getTenant(), group.getName(), member, se );
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
     * @throws BuildException An error occurred while building
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
                if ( CollectionUtils.isNotEmpty( group.getMembers() ) )
                {
                    for ( String member : group.getMembers() )
                    {
                        LOG.info( "deleteGroupMembers tenant={} name={}, member={}", getTenant(), group.getName(), member );
                        try
                        {
                            groupMgr.deassign( group, member );
                        }
                        catch ( SecurityException se )
                        {
                            LOG.warn( "deleteGroupMembers tenant={} name [{}], member [{}] caught SecurityException={}",
                                getTenant(), group.getName(), member, se );
                        }
                    }
                }
                else
                {
                    LOG.info( "deleteGroupMembers tenant={} name={}, no member found", getTenant(), group.getName() );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
     */
    private void addGroupProperties()
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
                if ( PropUtil.isNotEmpty( group.getProperties() ) )
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
                            LOG.warn( "addGroupProperties tenant={} name [{}], key [{}], value [{}] caught SecurityException={}",
                                getTenant(), group.getName(), key, val, se );
                        }
                    }
                }
                else
                {
                    LOG.info( "addGroupProperties tenant={} name={}, no properties found", getTenant(), group.getName() );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                if ( PropUtil.isNotEmpty( group.getProperties() ) )
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
                                "deleteGroupProperties tenant={} name [{}], key [{}], value [{}] caught SecurityException={}",
                                getTenant(), group.getName(), key, val, se );
                        }
                    }
                }
                else
                {
                    LOG.info( "deleteGroupProperties tenant={} name={}, no properties found", getTenant(), group.getName() );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addUserRoles tenant={} userid={} role name={}", getTenant(), userRole.getUserId(), userRole.getName() );
                try
                {
                    //Role role = new Role(userRole);
                    adminMgr.assignUser( userRole );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addUserRoles tenant={} userId={} roleName={} caught SecurityException={}", getTenant(), userRole.getUserId(), userRole.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "delUserRoles tenant={} userid={} role name={}", getTenant(), userRole.getUserId(), userRole.getName() );
                try
                {
                    adminMgr.deassignUser( userRole );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "delUserRoles tenant={} userId={} roleName={} caught SecurityException={}", getTenant(), userRole.getUserId(), userRole.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
     */
    private void addRoleConstraints() throws BuildException
    {
        if( addroleconstraints == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addroleconstraint addroleconstraint : addroleconstraints )
        {
            List<RoleConstraintAnt> roleconstraints = addroleconstraint.getRoleConstraints();
            for ( RoleConstraintAnt roleConstraint : roleconstraints )
            {
                try
                {
                    // If userid null, enable constraint on role:
                    if( StringUtils.isEmpty( roleConstraint.getUserId() ) && roleConstraint.getType() == org.apache.directory.fortress.core.model.RoleConstraint.RCType.USER )
                    {
                        adminMgr.enableRoleConstraint( new Role( roleConstraint.getRole() ), roleConstraint );
                        LOG.info( "enableRoleConstraint successfully enabled: tenant={} type={} role={} key={}", getTenant(), roleConstraint.getType(), roleConstraint.getRole(), roleConstraint.getKey() );
                    }
                    else
                    {
                        adminMgr.addRoleConstraint( new UserRole( roleConstraint.getUserId(), roleConstraint.getRole() ), roleConstraint );
                        LOG.info( "addRoleConstraint successfully added: tenant={} type={} userid={} role={} key={} value={}", getTenant(), roleConstraint.getType(), roleConstraint.getUserId(), roleConstraint.getRole(), roleConstraint.getKey(), roleConstraint.getValue() );
                    }
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addRoleConstraints tenant={} userId={} roleName={} caught SecurityException={}", getTenant(), roleConstraint.getUserId(), roleConstraint.getRole(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
     */
    private void delRoleConstraints() throws BuildException
    {
        if( addroleconstraints == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Delroleconstraint delroleconstraint : delroleconstraints )
        {
            List<RoleConstraintAnt> roleconstraints = delroleconstraint.getRoleConstraints();
            for ( RoleConstraintAnt roleConstraint : roleconstraints )
            {
                try
                {
                    // If userid null, disable constraint on role:
                    if( StringUtils.isEmpty( roleConstraint.getUserId() ) && roleConstraint.getType() == org.apache.directory.fortress.core.model.RoleConstraint.RCType.USER )
                    {
                        adminMgr.disableRoleConstraint( new Role( roleConstraint.getRole() ), roleConstraint );
                        LOG.info( "disableRoleConstraint successfully disabled: tenant={} type={} role={} key={}", getTenant(), roleConstraint.getType(), roleConstraint.getRole(), roleConstraint.getKey() );
                    }
                    else
                    {
                        adminMgr.removeRoleConstraint( new UserRole( roleConstraint.getUserId(), roleConstraint.getRole()
                        ), roleConstraint );
                        LOG.info( "removeRoleConstraint success: tenant={} type={} userid={} role={} key={} value={}",
                            getTenant(), roleConstraint.getType(), roleConstraint.getUserId(), roleConstraint.getRole(),
                            roleConstraint.getKey(), roleConstraint.getValue() );
                    }
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "delRoleConstraints tenant={} userId={} roleName={} caught SecurityException={}", getTenant(), roleConstraint.getUserId(), roleConstraint.getRole(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addRoles tenant={} name={} description={}", getTenant(), role.getName(), role.getDescription() );
                try
                {
                    adminMgr.addRole( role );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addRoles tenant={} name [{}] caught SecurityException={}", getTenant(), role.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "deleteRoles tenant={} name={}", getTenant(), role.getName() );
                try
                {
                    adminMgr.deleteRole( role );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteRoles tenant={} name [{}] caught SecurityException={}", getTenant(), role.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addRoleInheritances tenant={} parent={} child={}", getTenant(), relationship.getParent(), relationship.getChild() );
                try
                {
                    adminMgr.addInheritance( new Role( relationship.getParent() ), new Role( relationship.getChild()
                        ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addRoleInheritances tenant={} parent [{}] child [{}] caught SecurityException={}",
                        getTenant(), relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "deleteRoleInheritances tenant={} parent={} child={}", getTenant(), relationship.getParent(),
                    relationship.getChild() );
                try
                {
                    adminMgr.deleteInheritance( new Role( relationship.getParent() ),
                        new Role( relationship.getChild() ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteRoleInheritances tenant={} parent [{}] child [{}] caught SecurityException={}",
                        getTenant(), relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addSdsets tenant={} name={} description={}", getTenant(), sd.getName(), sd.getDescription() );
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
                    LOG.warn( "addSdsets tenant={} name [{}] caught SecurityException={}", getTenant(), sd.getName() + se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "deleteSdsets tenant={} name={}", getTenant(), sd.getName() );
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
                    LOG.warn( "deleteSdsets tenant={} name [{}] caught SecurityException={}", getTenant(), sd.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addPermObjs tenant={} objName={} description={} orgUnit={} type={}",
                    getTenant(), permObj.getObjName(), permObj.getDescription(), permObj.getOu(), permObj.getType() );
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
                            LOG.info( "addPermObjs tenant={} update entity objName={} description={} orgUnit={} type={}", getTenant(), permObj.getObjName(), permObj
                                .getDescription(), permObj.getOu(), permObj.getType() );
                        }
                        else
                        {
                            throw se;
                        }
                    }
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addPermObjs tenant={} objName [{}] caught SecurityException={}",
                        getTenant(), permObj.getObjName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "deletePermObjs tenant={} objName={} description={}",
                    getTenant(), permObj.getObjName(), permObj.getDescription() );
                try
                {
                    adminMgr.deletePermObj( permObj );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deletePermObjs tenant={} name [{}] caught SecurityException={}", getTenant(), permObj.getObjName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addPermOps tenant={} name={} objName={}", getTenant(), permission.getOpName(), permission.getObjName() );
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
                            LOG.info( "addPermOps tenant={} - update entity - name={} objName={}",
                                getTenant(), permission.getOpName(), permission.getObjName() );
                        }
                        else
                        {
                            throw se;
                        }
                    }
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addPermOps tenant={} name [{}] objName [{}] caught SecurityException={}",
                        getTenant(), permission.getOpName(), permission.getObjName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "deletePermOps tenant={} name={} objName={}", getTenant(), permission.getOpName(), permission.getObjName() );
                try
                {
                    adminMgr.deletePermission( permission );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deletePermOps tenant={} name [{}] objName[{}] caught SecurityException={}",
                        getTenant(), permission.getOpName(), permission.getObjName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                try
                {
                    Permission perm = new Permission( permGrant.getObjName(), permGrant.getOpName(),
                        permGrant.isAdmin() );
                    perm.setOpName( permGrant.getOpName() );
                    perm.setObjId( permGrant.getObjId() );
                    if ( permGrant.getRoleNm() != null && permGrant.getRoleNm().length() > 0 )
                    {
                        LOG.info( "addPermGrants tenant={} roleName={} objName={} opName={} objId={}", getTenant(), permGrant.getRoleNm(), permGrant.getObjName(), permGrant.getOpName(), permGrant.getObjId() );
                        adminMgr.grantPermission( perm, new Role( permGrant.getRoleNm() ) );
                    }
                    else if ( permGrant.getUserId() != null && permGrant.getUserId().length() > 0 )
                    {
                        LOG.info( "addPermGrants tenant={} userId={} objName={} opName={} objId={}", getTenant(), permGrant.getUserId(), permGrant.getObjName(), permGrant.getOpName(), permGrant.getObjId() );
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
                    LOG.warn( "addPermGrants tenant={} roleName={} objName={} opName={} objId={} caught SecurityException={}" , getTenant(), permGrant.getRoleNm(), permGrant.getObjName(), permGrant.getOpName(), permGrant.getObjId(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                try
                {
                    Permission perm = new Permission( permGrant.getObjName(), permGrant.getOpName(),
                        permGrant.isAdmin() );
                    perm.setOpName( permGrant.getOpName() );
                    perm.setObjId( permGrant.getObjId() );
                    if ( permGrant.getRoleNm() != null && permGrant.getRoleNm().length() > 0 )
                    {
                        LOG.info( "deletePermGrants tenant={} roleName={} objName={} opName={} objId={}", getTenant(), permGrant.getRoleNm(), permGrant.getObjName(), permGrant.getOpName(), permGrant.getObjId() );
                        adminMgr.revokePermission( perm, new Role( permGrant.getRoleNm() ) );
                    }
                    else if ( permGrant.getUserId() != null && permGrant.getUserId().length() > 0 )
                    {
                        LOG.info( "deletePermGrants tenant={} userId={} objName={} opName={} objId={}", getTenant(), permGrant.getUserId(), permGrant.getObjName(), permGrant.getOpName(), permGrant.getObjId() );
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
                    LOG.warn( "deletePermGrants tenant={} roleName={} objName={} opName={} objId={} caught SecurityException={}" , getTenant(), permGrant.getRoleNm(), permGrant.getObjName(), permGrant.getOpName(), permGrant.getObjId(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addPolicies tenant={} name={}", getTenant(), policy.getName() );
                try
                {
                    policyMgr.add( policy );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addPolicies tenant={} name [{}] caught SecurityException={}", getTenant(), policy.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "deletePolicies tenant={} name={}", getTenant(), policy.getName() );
                try
                {
                    policyMgr.delete( policy );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deletePolicies tenant={} name [{}] caught SecurityException={}", getTenant(), policy.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addContainers tenant={} name={} description={}", getTenant(), ou.getName(), ou.getDescription() );
                try
                {
                    OrganizationalUnitP op = new OrganizationalUnitP();
                    // Set the tenant id onto the entity.
                    // Normally this info would be passed in via a manager constructor.  Since these methods aren't implemented by a manager, we must do this here:
                    if( ! StringUtils.isEmpty( TENANT ) && ! TENANT.equals( "${tenant}" ) )
                    {
                        ou.setContextId( TENANT );
                    }
                    op.add( ou );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addContainers tenant={} name [{}] caught SecurityException={}", getTenant(), ou.getName(), se.getMessage() );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "deleteContainers tenant={} name={}", getTenant(), ou.getName() );
                try
                {
                    OrganizationalUnitP op = new OrganizationalUnitP();
                    // Set the tenant id onto the entity.
                    // Normally this info would be passed in via a manager constructor.  Since these methods aren't implemented by a manager, we must do this here:
                    if( ! StringUtils.isEmpty( TENANT ) && ! TENANT.equals( "${tenant}" ) )
                    {
                        ou.setContextId( TENANT );
                    }
                    op.delete( ou );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteContainers tenant={} name [{}] caught SecurityException={}", getTenant(), ou.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addSuffixes tenant={} name={} description={}", getTenant(), suffix.getName(), suffix.getDescription() );
                try
                {
                    SuffixP sp = new SuffixP();
                    sp.add( suffix );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addSuffixes tenant={} name [{}] caught SecurityException={}", getTenant(), suffix.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "deleteSuffixes tenant={} name={}", getTenant(), suffix.getName() );
                try
                {
                    SuffixP sp = new SuffixP();
                    sp.delete( suffix );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteSuffixes tenant={} name={} caught SecurityException={}", getTenant(), suffix.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addOrgunits tenant={} name={} typeName={} description={}",
                    getTenant(), ou.getName(), ou.getTypeName(), ou.getDescription() );
                try
                {
                    dAdminMgr.add( ou );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addOrgunits tenant={} name={} caught SecurityException={}", getTenant(), ou.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "deleteOrgunits tenant={} name={} typeName={}", getTenant(), ou.getName(), ou.getTypeName() );
                try
                {
                    dAdminMgr.delete( ou );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteOrgunits tenant={} name={} caught SecurityException={}", getTenant(), ou.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addUserOrgunitInheritances tenant={} parent={} child={}",
                    getTenant(), relationship.getParent(), relationship.getChild() );
                try
                {
                    dAdminMgr.addInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.USER ),
                        new OrgUnit( relationship.getChild(), OrgUnit.Type.USER ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addUserOrgunitInheritances tenant={} parent={} child={} caught SecurityException={}",
                        getTenant(), relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "deleteUserOrgunitInheritances tenant={} parent={} child={}" +
                    getTenant(), relationship.getParent(), relationship.getChild() );
                try
                {
                    dAdminMgr.deleteInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.USER ),
                        new OrgUnit( relationship.getChild(), OrgUnit.Type.USER ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteUserOrgunitInheritances tenant={} parent={} child={} caught SecurityException={}",
                        getTenant(), relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addPermOrgunitInheritances tenant={} parent={} child={}",
                    getTenant(), relationship.getParent(), relationship.getChild() );
                try
                {
                    dAdminMgr.addInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.PERM ),
                        new OrgUnit( relationship.getChild(), OrgUnit.Type.PERM ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addPermOrgunitInheritances tenant={} parent={} child={} caught SecurityException={}",
                        getTenant(), relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "deletePermOrgunitInheritances tenant={} parent={} child={}",
                    getTenant(), relationship.getParent(), relationship.getChild() );
                try
                {
                    dAdminMgr.deleteInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.PERM ),
                        new OrgUnit( relationship.getChild(), OrgUnit.Type.PERM ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deletePermOrgunitInheritances tenant={} parent={} child={} caught SecurityException={}",
                        getTenant(), relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addAdminRoles tenant={} name={} description={}", getTenant(), role.getName(), role.getDescription() );
                try
                {
                    dAdminMgr.addRole( role );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addAdminRoles tenant={} name={} caught SecurityException={}", getTenant(), role.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "deleteAdminRoles tenant={} name={}", getTenant(), role.getName() );
                try
                {
                    dAdminMgr.deleteRole( role );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteAdminRoles tenant={} name={} caught SecurityException={}", getTenant(), role.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addAdminRoleInheritances tenant={} parent={} child={}",
                    getTenant(), relationship.getParent(), relationship.getChild() );
                try
                {
                    dAdminMgr.addInheritance( new AdminRole( relationship.getParent() ),
                        new AdminRole( relationship.getChild() ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addAdminRoleInheritances tenant={} parent={} child={} caught SecurityException={}",
                        getTenant(), relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "deleteAdminRoleInheritances tenant={} parent={} child={}",
                    getTenant(), relationship.getParent(), relationship.getChild() );
                try
                {
                    dAdminMgr.deleteInheritance( new AdminRole( relationship.getParent() ),
                        new AdminRole( relationship.getChild() ) );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "deleteAdminRoleInheritances tenant={} parent={} child={} caught SecurityException={}",
                        getTenant(), relationship.getParent(), relationship.getChild(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "addUserAdminRoles tenant={} userid={} role name={}", getTenant(), userRole.getUserId(), userRole.getName() );
                try
                {
                    //AdminRole role = new AdminRole(userRole);
                    dAdminMgr.assignUser( userRole );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "addUserAdminRoles tenant={} userId={} role name={} caught SecurityException={}",
                        getTenant(), userRole.getUserId(), userRole.getName(), se );
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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
                LOG.info( "delUserAdminRoles tenant={} userid={} role name={}", getTenant(), userRole.getUserId(), userRole.getName() );
                try
                {
                    dAdminMgr.deassignUser( userRole );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "delUserAdminRoles tenant={} userId={} role name={} caught SecurityException={}",
                        getTenant(), userRole.getUserId(), userRole.getName(), se );
                }
            }
        }
    }


    /**
     * Add a new configuration node and its associated property values into the directory.
     */
    private void addConfig() throws BuildException
    {
        LOG.info( "addConfig" );
        if( addconfig == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Addconfig addcfg : addconfig )
        {
            Properties props = new Properties();
            Configuration configuration = new Configuration();
            try
            {
                List<ConfigAnt> cfgs = addcfg.getConfig();
                for ( ConfigAnt cfg : cfgs )
                {
                    String val = cfg.getProps();
                    int indx = val.indexOf( GlobalIds.PROP_SEP );
                    if ( indx >= 1 )
                    {
                        String name = val.substring( 0, indx );
                        String value = val.substring( indx + 1 );

                        // The config realm property is required on updconfig op and points to the existing node in ldap to update with these new props.
                        if( name.equalsIgnoreCase( GlobalIds.CONFIG_REALM ))
                        {
                            configuration.setName( value );
                        }
                        else if( name.equalsIgnoreCase( GlobalIds.CONFIG_UID_NUMBER ))
                        {
                            configuration.setUidNumber( value );
                        }
                        else if( name.equalsIgnoreCase( GlobalIds.CONFIG_GID_NUMBER ))
                        {
                            configuration.setGidNumber( value );
                        }
                        else
                        {
                            props.setProperty( name, value );
                            LOG.info( "addConfig name [{}] value [{}]", name, value );
                        }
                    }
                }
                configuration.addProperties( props );
                LOG.info( "addConfig realm name [{}]", configuration.getName() );
                LOG.info( "addConfig gid.number [{}]", configuration.getGidNumber() );
                LOG.info( "addConfig uid.number [{}]", configuration.getUidNumber() );
                cfgMgr.add( configuration );
            }
            catch ( SecurityException se )
            {
                if ( se.getErrorId() == GlobalErrIds.FT_CONFIG_ALREADY_EXISTS )
                {
                    try
                    {
                        LOG.info( "addConfig realm name={} entry already exists, attempt to update", configuration.getName() );
                        cfgMgr.update( configuration );
                        LOG.info( "addConfig realm name={} update [{}] successful", configuration.getName(), configuration.getName() );
                    }
                    catch ( SecurityException se2 )
                    {
                        LOG.warn( "addConfig realm name={] update failed SecurityException={}", configuration.getName(), se2 );
                    }
                }
                else
                {
                    LOG.warn( "addConfig realm name={} failed SecurityException={}", configuration.getName(), se );
                }
            }
        }
    }


    /**
     * Update existing configuration node with new values.
     */
    private void updConfig() throws BuildException
    {
        LOG.info( "updateConfig" );

        if( updconfig == null )
        {
            return;
        }

        // Loop through the entityclass elements
        for ( Updconfig updcfg : updconfig )
        {
            Properties props = new Properties();
            Configuration configuration = new Configuration();
            String configNodeName = "";
            List<ConfigAnt> cfgs = updcfg.getConfig();
            for ( ConfigAnt cfg : cfgs )
            {
                LOG.info( "updateConfig" );
                String val = cfg.getProps();
                int indx = val.indexOf( GlobalIds.PROP_SEP );
                if ( indx >= 1 )
                {
                    String name = val.substring( 0, indx );
                    String value = val.substring( indx + 1 );

                    // The config realm property is required on updconfig op and points to the existing node in ldap to update with these new props.
                    if( name.equalsIgnoreCase( GlobalIds.CONFIG_REALM ))
                    {
                        configuration.setName( value );
                    }
                    else if( name.equalsIgnoreCase( GlobalIds.CONFIG_UID_NUMBER ))
                    {
                        configuration.setUidNumber( value );
                    }
                    else if( name.equalsIgnoreCase( GlobalIds.CONFIG_GID_NUMBER ))
                    {
                        configuration.setGidNumber( value );
                    }
                    else
                    {
                        props.setProperty( name, value );
                        LOG.info( "updateConfig name [{}] value [{}]", name, value );
                    }
                }
            }
            // Can't go on w/out a name for the config node to update.
            if ( StringUtils.isEmpty( configuration.getName() ))
            {
                LOG.warn( "updConfig realm name not specified, operation aborted." );
                LOG.warn( "Add entry like this  to input xml: <config props=\"config.realm:DEFAULT\"/>" );
            }
            else
            {
                configuration.addProperties( props );
                LOG.info( "updConfig realm name [{}]", configuration.getName() );
                LOG.info( "updConfig gid.number [{}]", configuration.getGidNumber() );
                LOG.info( "updConfig uid.number [{}]", configuration.getUidNumber() );
                try
                {
                    cfgMgr.update( configuration );
                }
                catch ( SecurityException se )
                {
                    LOG.warn( "updConfig realm name={} failed SecurityException={}", configNodeName, se );
                    LOG.warn( "Verify that config realm name={} exists", configNodeName);
                }
            }
        }
    }


    /**
     * @throws BuildException An error occurred while building
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