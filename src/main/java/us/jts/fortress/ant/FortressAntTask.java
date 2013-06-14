/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ant;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.input.InputRequest;

import us.jts.fortress.AdminMgr;
import us.jts.fortress.AdminMgrFactory;
import us.jts.fortress.DelAdminMgr;
import us.jts.fortress.DelAdminMgrFactory;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.GlobalIds;
import us.jts.fortress.PwPolicyMgr;
import us.jts.fortress.PwPolicyMgrFactory;
import us.jts.fortress.SecurityException;
import us.jts.fortress.cfg.ConfigMgr;
import us.jts.fortress.cfg.ConfigMgrFactory;
import us.jts.fortress.ldap.container.OrganizationalUnit;
import us.jts.fortress.ldap.container.OrganizationalUnitP;
import us.jts.fortress.ldap.suffix.Suffix;
import us.jts.fortress.ldap.suffix.SuffixP;
import us.jts.fortress.rbac.AdminRole;
import us.jts.fortress.rbac.Context;
import us.jts.fortress.rbac.OrgUnit;
import us.jts.fortress.rbac.OrgUnitAnt;
import us.jts.fortress.rbac.PermGrant;
import us.jts.fortress.rbac.PermObj;
import us.jts.fortress.rbac.Permission;
import us.jts.fortress.rbac.PwPolicy;
import us.jts.fortress.rbac.Relationship;
import us.jts.fortress.rbac.Role;
import us.jts.fortress.rbac.SDSet;
import us.jts.fortress.rbac.User;
import us.jts.fortress.rbac.UserAdminRole;
import us.jts.fortress.rbac.UserRole;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.input.InputRequest;
import org.apache.log4j.Logger;
import us.jts.fortress.util.attr.VUtil;


/**
 * This class implements Apache Ant custom task and is used to drive the Fortress Administrative APIs using XML files.  The
 * methods in this class are not intended to be callable by outside programs.  The following APIs are supported:
 * <p/>
 * <ol>
 * <li>{@link us.jts.fortress.AdminMgr}</li>
 * <li>{@link us.jts.fortress.DelAdminMgr}</li>
 * <li>{@link us.jts.fortress.PwPolicyMgr}</li>
 * <li>{@link ConfigMgr}</li>
 * </ol>
 * <p/>
 * using the custom Ant task that is implemented in this class.  The format of the XML is flat and consists of entity names
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
 * <li>Delete User Role Assignments {@link us.jts.fortress.AdminMgr#deassignUser(us.jts.fortress.rbac.UserRole)}</li>
 * <li>Delete User AdminRole Assignments {@link us.jts.fortress.DelAdminMgr#deassignUser(us.jts.fortress.rbac.UserAdminRole)}</li>
 * <li>Revoke Permission Assignments Delete{@link us.jts.fortress.AdminMgr#revokePermission(us.jts.fortress.rbac.Permission, us.jts.fortress.rbac.Role)}</li>
 * <li>Delete Users {@link us.jts.fortress.AdminMgr#disableUser(us.jts.fortress.rbac.User)}</li>
 * <li>Delete Password Policies {@link us.jts.fortress.PwPolicyMgr#delete(us.jts.fortress.rbac.PwPolicy)}</li>
 * <li>Delete Permission Operations {@link us.jts.fortress.AdminMgr#deletePermission(us.jts.fortress.rbac.Permission)}</li>
 * <li>Delete Permission Objects {@link us.jts.fortress.AdminMgr#deletePermObj(us.jts.fortress.rbac.PermObj)}</li>
 * <li>Delete SSD and DSD Sets {@link us.jts.fortress.AdminMgr#deleteDsdSet(us.jts.fortress.rbac.SDSet)} and {@link us.jts.fortress.AdminMgr#deleteSsdSet(us.jts.fortress.rbac.SDSet)}</li>
 * <li>Delete RBAC Roles Inheritances {@link us.jts.fortress.AdminMgr#deleteInheritance(us.jts.fortress.rbac.Role, us.jts.fortress.rbac.Role)}</li>
 * <li>Delete RBAC Roles {@link us.jts.fortress.AdminMgr#deleteRole(us.jts.fortress.rbac.Role)}</li>
 * <li>Delete ARBAC Role Inheritances {@link us.jts.fortress.DelAdminMgr#deleteInheritance(us.jts.fortress.rbac.AdminRole, us.jts.fortress.rbac.AdminRole)}</li>
 * <li>Delete ARBAC Roles {@link us.jts.fortress.DelAdminMgr#deleteRole(us.jts.fortress.rbac.AdminRole)}</li>
 * <li>Delete User and Perm OU Inheritances {@link us.jts.fortress.DelAdminMgr#deleteInheritance(us.jts.fortress.rbac.OrgUnit, us.jts.fortress.rbac.OrgUnit)} USER and PERM</li>
 * <li>Delete User and Perm OUs {@link us.jts.fortress.DelAdminMgr#delete(us.jts.fortress.rbac.OrgUnit)} USER and PERM</li>
 * <li>Delete Configuration Entries {@link us.jts.fortress.cfg.ConfigMgr#delete(String, java.util.Properties)}</li>
 * <li>Delete Containers {@link us.jts.fortress.ldap.container.OrganizationalUnitP#delete(us.jts.fortress.ldap.container.OrganizationalUnit)}</li>
 * <li>Delete Suffix {@link us.jts.fortress.ldap.suffix.SuffixP#delete(us.jts.fortress.ldap.suffix.Suffix)}}</li>
 * <li>Add Suffix {@link us.jts.fortress.ldap.suffix.SuffixP#add(us.jts.fortress.ldap.suffix.Suffix)}}</li>
 * <li>Add Containers {@link us.jts.fortress.ldap.container.OrganizationalUnitP#add(us.jts.fortress.ldap.container.OrganizationalUnit)}</li>
 * <li>Add Configuration Parameters {@link ConfigMgr#add(String, java.util.Properties)}</li>
 * <li>Add User and Perm OUs {@link us.jts.fortress.DelAdminMgr#add(us.jts.fortress.rbac.OrgUnit)} USER and PERM</li>
 * <li>Add User and Perm OU Inheritances {@link us.jts.fortress.DelAdminMgr#addInheritance(us.jts.fortress.rbac.OrgUnit, us.jts.fortress.rbac.OrgUnit)} USER and PERM</li>
 * <li>Add ARBAC Roles {@link us.jts.fortress.DelAdminMgr#addRole(us.jts.fortress.rbac.AdminRole)}</li>
 * <li>Add ARBAC Role Inheritances {@link us.jts.fortress.DelAdminMgr#addInheritance(us.jts.fortress.rbac.AdminRole, us.jts.fortress.rbac.AdminRole)}</li>
 * <li>Add RBAC Roles {@link us.jts.fortress.AdminMgr#addRole(us.jts.fortress.rbac.Role)}</li>
 * <li>Add RBAC Role Inheritances {@link us.jts.fortress.AdminMgr#addInheritance(us.jts.fortress.rbac.Role, us.jts.fortress.rbac.Role)}</li>
 * <li>Add DSD and SSD Sets {@link us.jts.fortress.AdminMgr#createDsdSet(us.jts.fortress.rbac.SDSet)} and {@link us.jts.fortress.AdminMgr#createSsdSet(us.jts.fortress.rbac.SDSet)}</li>
 * <li>Add Permission Objects {@link us.jts.fortress.AdminMgr#addPermObj(us.jts.fortress.rbac.PermObj)}</li>
 * <li>Add Permission Operations {@link us.jts.fortress.AdminMgr#addPermission(us.jts.fortress.rbac.Permission)}</li>
 * <li>Add Password Policies {@link us.jts.fortress.PwPolicyMgr#add(us.jts.fortress.rbac.PwPolicy)}</li>
 * <li>Add Users {@link us.jts.fortress.AdminMgr#addUser(us.jts.fortress.rbac.User)}</li>
 * <li>Grant RBAC Permissions {@link us.jts.fortress.AdminMgr#grantPermission(us.jts.fortress.rbac.Permission, us.jts.fortress.rbac.Role)}</li>
 * <li>Assign ARBAC Roles {@link us.jts.fortress.DelAdminMgr#assignUser(us.jts.fortress.rbac.UserAdminRole)}</li>
 * <li>Assign RBAC Roles {@link us.jts.fortress.AdminMgr#assignUser(us.jts.fortress.rbac.UserRole)}</li>
 * </li>
 * </ol>
 * <p/>
 *
 * @author Shawn McKinney
 */
public class FortressAntTask extends Task implements InputHandler
{
    private static final String SEMICOLON = ";";
    final private List<us.jts.fortress.ant.Addconfig> addconfig = new ArrayList<>();
    final private List<us.jts.fortress.ant.Delconfig> delconfig = new ArrayList<>();
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

    private ConfigMgr cfgMgr = null;
    private AdminMgr adminMgr = null;
    private DelAdminMgr dAdminMgr = null;
    private PwPolicyMgr policyMgr = null;
    private static final String CLS_NM = FortressAntTask.class.getName();
    private static final Logger log = Logger.getLogger( CLS_NM );
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
            adminMgr = us.jts.fortress.AdminMgrFactory.createInstance( context.getName() );
            dAdminMgr = DelAdminMgrFactory.createInstance( context.getName() );
            policyMgr = PwPolicyMgrFactory.createInstance( context.getName() );
        }
        catch ( SecurityException se )
        {
            log.warn( CLS_NM + " FortressAntTask setContext caught SecurityException=" + se );
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
        }
        catch ( SecurityException se )
        {
            log.warn( CLS_NM + " FortressAntTask constructor caught SecurityException=" + se );
        }
    }


    /**
     * Used by Apache Ant to load data from xml into entities.
     *
     * @param request
     *
     */
    public void handleInput( InputRequest request )
    {
        log.info( CLS_NM + ".handleInput request=" + request );
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
     * @param addadminroleinheritance contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddadminroleinheritance( Addadminroleinheritance addadminroleinheritance )
    {
        this.addadminroleinheritances.add( addadminroleinheritance );
    }


    /**
     * Load the entity with data.
     *
     * @param deladminroleinheritance contains the ant initialized data entities to be handed off for further processing.
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
    public void execute()
        throws BuildException
    {
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
    }


    /**
     * @throws BuildException
     */
    private void addUsers()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Adduser adduser : addusers )
        {
            List<UserAnt> users = adduser.getUsers();

            for ( UserAnt user : users )
            {
                log.info( CLS_NM + ".addUsers userid=" + user.getUserId()
                    + " description=" + user.getDescription()
                    + " orgUnit=" + user.getOu() );
                try
                {
                    try
                    {
                        adminMgr.addUser(user);
                        if(VUtil.isNotNullOrEmpty(user.getRoles()))
                        {
                            for(UserRole uRole : user.getRoles())
                            {
                                adminMgr.assignUser(uRole);
                            }
                        }
                        if(VUtil.isNotNullOrEmpty(user.getAdminRoles()))
                        {
                            for(UserAdminRole uAdminRoleRole : user.getAdminRoles())
                            {
                                dAdminMgr.assignUser(uAdminRoleRole);
                            }
                        }
                    }
                    catch ( SecurityException se )
                    {
                        // If User entity already there then call the udpate method.
                        if ( se.getErrorId() == GlobalErrIds.USER_ID_DUPLICATE )
                        {
                            adminMgr.updateUser(user);
                            if(VUtil.isNotNullOrEmpty(user.getRoles()))
                            {
                                for(UserRole uRole : user.getRoles())
                                {
                                    adminMgr.assignUser(uRole);
                                }
                            }
                            if(VUtil.isNotNullOrEmpty(user.getAdminRoles()))
                            {
                                for(UserAdminRole uAdminRoleRole : user.getAdminRoles())
                                {
                                    dAdminMgr.assignUser(uAdminRoleRole);
                                }
                            }
                            log.info(CLS_NM + ".addUsers - Update entity - userId=" + user.getUserId());
                        }
                        else
                        {
                            throw se;
                        }
                    }
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".addUsers userId [" + user.getUserId() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteUsers()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Deluser deluser : delusers )
        {
            List<UserAnt> users = deluser.getUsers();
            for ( UserAnt user : users )
            {
                log.info( CLS_NM + ".deleteUsers userid=" + user.getUserId() );
                try
                {
                    adminMgr.deleteUser( user );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".deleteUsers userId [" + user.getUserId() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addUserRoles()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Adduserrole adduserrole : adduserroles )
        {
            List<UserRole> userroles = adduserrole.getUserRoles();
            for ( UserRole userRole : userroles )
            {
                log.info( CLS_NM + ".addUserRoles userid=" + userRole.getUserId()
                    + " role name=" + userRole.getName() );

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
                    String warning = CLS_NM + ".addUserRoles userId [" + userRole.getUserId() + "] role name ["
                        + userRole.getName() + "] caught SecurityException=" + se;
                    log.warn( warning );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void delUserRoles()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Deluserrole deluserrole : deluserroles )
        {
            List<UserRole> userroles = deluserrole.getUserRoles();
            for ( UserRole userRole : userroles )
            {
                log.info( ".delUserRoles userid=" + userRole.getUserId()
                    + " role name=" + userRole.getName() );
                try
                {
                    adminMgr.deassignUser( userRole );
                }
                catch ( us.jts.fortress.SecurityException se )
                {
                    String warning = CLS_NM + ".delUserRoles userId [" + userRole.getUserId() + "] role name ["
                        + userRole.getName() + "] caught SecurityException=" + se;
                    log.warn( warning );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addRoles()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addrole addrole : addroles )
        {
            List<Role> roles = addrole.getRoles();
            for ( Role role : roles )
            {
                log.info( CLS_NM + ".addRoles name=" + role.getName()
                    + " description=" + role.getDescription() );
                try
                {
                    adminMgr.addRole( role );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".addRoles name [" + role.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteRoles()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delrole delrole : delroles )
        {
            List<Role> roles = delrole.getRoles();
            for ( Role role : roles )
            {
                log.info( CLS_NM + ".deleteRoles name=" + role.getName() );
                try
                {
                    adminMgr.deleteRole( role );
                }
                catch ( us.jts.fortress.SecurityException se )
                {
                    log.warn( CLS_NM + ".deleteRoles name [" + role.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addRoleInheritances()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addroleinheritance addroleinheritance : addroleinheritances )
        {
            List<Relationship> roles = addroleinheritance.getRelationships();
            for ( Relationship relationship : roles )
            {
                log.info( CLS_NM + ".addRoleInheritances parent=" + relationship.getParent()
                    + " child=" + relationship.getChild() );
                try
                {
                    adminMgr.addInheritance( new Role( relationship.getParent() ), new Role( relationship.getChild() ) );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".addRoleInheritances parent [" + relationship.getParent() + "] child ["
                        + relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteRoleInheritances()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delroleinheritance delroleinheritance : delroleinheritances )
        {
            List<Relationship> roles = delroleinheritance.getRelationships();
            for ( Relationship relationship : roles )
            {
                log.info( CLS_NM + ".deleteRoleInheritances parent=" + relationship.getParent()
                    + " child=" + relationship.getChild() );
                try
                {
                    adminMgr.deleteInheritance( new Role( relationship.getParent() ),
                        new Role( relationship.getChild() ) );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".deleteRoleInheritances parent [" + relationship.getParent() + "] child ["
                        + relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addSdsets()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addsdset addsdset : addsdsets )
        {
            List<SDSetAnt> sds = addsdset.getSdset();
            for ( SDSetAnt sd : sds )
            {
                log.info( CLS_NM + ".addSdsets name=" + sd.getName()
                    + " description=" + sd.getDescription() );
                try
                {
                    adminMgr.createSsdSet( sd );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".addSdsets name [" + sd.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteSdsets()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delsdset delsdset : delsdsets )
        {
            List<SDSetAnt> sds = delsdset.getSdset();
            for ( SDSetAnt sd : sds )
            {
                log.info( CLS_NM + ".deleteSdsets name=" + sd.getName() );
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
                    log.warn( CLS_NM + ".deleteSdsets name [" + sd.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addPermObjs()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( AddpermObj addpermObj : addpermObjs )
        {
            List<PermObj> permObjs = addpermObj.getPermObjs();
            for ( PermObj permObj : permObjs )
            {
                log.info( CLS_NM + ".addPermObjs objectName=" + permObj.getObjectName()
                    + " description=" + permObj.getDescription()
                    + " orgUnit=" + permObj.getOu()
                    + " type=" + permObj.getType() );
                try
                {
                    try
                    {
                        adminMgr.addPermObj( permObj );
                    }
                    catch ( us.jts.fortress.SecurityException se )
                    {
                        // If Perm Object entity already there then call the udpate method.
                        if ( se.getErrorId() == GlobalErrIds.PERM_DUPLICATE )
                        {
                            adminMgr.updatePermObj( permObj );
                            System.out.println( CLS_NM + ".addPermObjs - update entity objectName="
                                + permObj.getObjectName() );
                        }
                        else
                        {
                            throw se;
                        }
                    }
                }
                catch ( us.jts.fortress.SecurityException se )
                {
                    log.warn( CLS_NM + ".addPermObjs objectName [" + permObj.getObjectName()
                        + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deletePermObjs()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( DelpermObj delpermObj : delpermObjs )
        {
            List<PermObj> permObjs = delpermObj.getObjs();
            for ( PermObj permObj : permObjs )
            {
                log.info( CLS_NM + ".deletePermObjs objectName=" + permObj.getObjectName()
                    + " description=" + permObj.getDescription() );
                try
                {
                    adminMgr.deletePermObj( permObj );
                }
                catch ( us.jts.fortress.SecurityException se )
                {
                    log.warn( CLS_NM + ".deletePermObjs name [" + permObj.getObjectName()
                        + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addPermOps()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( AddpermOp addpermOp : addpermOps )
        {
            List<PermAnt> permissions = addpermOp.getPermOps();
            for ( PermAnt permission : permissions )
            {
                log.info( CLS_NM + ".addPermOps name=" + permission.getOpName()
                    + " objectName=" + permission.getObjectName() );
                try
                {
                    try
                    {
                        adminMgr.addPermission( permission );
                    }
                    catch ( us.jts.fortress.SecurityException se )
                    {
                        // If Perm Object entity already there then call the udpate method.
                        if ( se.getErrorId() == GlobalErrIds.PERM_DUPLICATE )
                        {
                            adminMgr.updatePermission( permission );
                            log.info( CLS_NM + ".addPermOps - update entity - name=" + permission.getOpName()
                                + " objectName=" + permission.getObjectName() );
                        }
                        else
                        {
                            throw se;
                        }
                    }
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".addPermOps name [" + permission.getOpName() + "] objectName ["
                        + permission.getObjectName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deletePermOps()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( DelpermOp delpermOp : delpermOps )
        {
            List<PermAnt> permissions = delpermOp.getPermOps();
            for ( Permission permission : permissions )
            {
                log.info( CLS_NM + ".deletePermOps name=" + permission.getOpName() + " objectName="
                    + permission.getObjectName() );
                try
                {
                    adminMgr.deletePermission( permission );
                }
                catch ( us.jts.fortress.SecurityException se )
                {
                    log.warn( CLS_NM + ".deletePermOps name [" + permission.getOpName() + "] objectName["
                        + permission.getObjectName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addPermGrants()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( AddpermGrant addpermGrant : addpermGrants )
        {
            List<PermGrant> permGrants = addpermGrant.getPermGrants();
            for ( PermGrant permGrant : permGrants )
            {
                String info = CLS_NM + ".addPermGrants: Add permission grant object name=" + permGrant.getObjName()
                    + " operation name=" + permGrant.getOpName()
                    + " object id=" + permGrant.getObjId()
                    + " role name=" + permGrant.getRoleNm()
                    + " userId=" + permGrant.getUserId();
                log.info( info );
                try
                {
                    Permission perm = new Permission( permGrant.getObjName(), permGrant.getOpName(),
                        permGrant.isAdmin() );
                    perm.setOpName( permGrant.getOpName() );
                    perm.setObjectId( permGrant.getObjId() );
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
                        String warning = CLS_NM + ".addPermGrants called without user or role set in xml";
                        log.warn( warning );
                    }
                }
                catch ( SecurityException se )
                {
                    String warning = CLS_NM + ".addPermGrants caught SecurityException=" + se;
                    log.warn( warning );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deletePermGrants()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( DelpermGrant delpermGrant : delpermGrants )
        {
            List<PermGrant> permGrants = delpermGrant.getPermGrants();
            for ( PermGrant permGrant : permGrants )
            {
                String info = CLS_NM + ".deletePermGrants: Delete permission grant object name="
                    + permGrant.getObjName()
                    + " operation name=" + permGrant.getOpName()
                    + " role name=" + permGrant.getRoleNm()
                    + " userId=" + permGrant.getUserId();
                log.info( info );
                try
                {
                    Permission perm = new Permission( permGrant.getObjName(), permGrant.getOpName(),
                        permGrant.isAdmin() );
                    perm.setOpName( permGrant.getOpName() );
                    perm.setObjectId( permGrant.getObjId() );
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
                        String warning = CLS_NM + ".deletePermGrants called without user or role set in xml";
                        log.warn( warning );
                    }
                }
                catch ( SecurityException se )
                {
                    String warning = CLS_NM + ".deletePermGrants caught SecurityException=" + se;
                    log.warn( warning );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addPolicies()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addpwpolicy addpwpolicy : addpolicies )
        {
            List<PwPolicy> policies = addpwpolicy.getPolicies();
            for ( PwPolicy policy : policies )
            {
                log.info( CLS_NM + ".addPolicies name=" + policy.getName() );
                try
                {
                    policyMgr.add( policy );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".addPolicies name [" + policy.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deletePolicies()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delpwpolicy delpwpolicy : delpolicies )
        {
            List<PwPolicy> policies = delpwpolicy.getPolicies();
            for ( PwPolicy policy : policies )
            {
                log.info( CLS_NM + ".deletePolicies name=" + policy.getName() );
                try
                {
                    policyMgr.delete( policy );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".deletePolicies name [" + policy.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addContainers()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addcontainer addcontainer : addcontainers )
        {
            List<OrganizationalUnit> containers = addcontainer.getContainers();

            for ( OrganizationalUnit ou : containers )
            {
                log.info( CLS_NM + ".addContainers name=" + ou.getName()
                    + " description=" + ou.getDescription() );
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
                    log.warn( CLS_NM + ".addContainers name [" + ou.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteContainers()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delcontainer delcontainer : delcontainers )
        {
            List<OrganizationalUnit> containers = delcontainer.getContainers();
            for ( OrganizationalUnit ou : containers )
            {
                log.info( CLS_NM + ".deleteContainers name=" + ou.getName() );
                try
                {
                    OrganizationalUnitP op = new OrganizationalUnitP();
                    op.delete( ou );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".deleteContainers name [" + ou.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addSuffixes()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addsuffix addsuffix : addsuffixes )
        {
            List<Suffix> suffixes = addsuffix.getSuffixes();

            for ( Suffix suffix : suffixes )
            {
                log.info( CLS_NM + ".addSuffixes name=" + suffix.getName()
                    + " description=" + suffix.getDescription() );
                try
                {
                    SuffixP sp = new SuffixP();
                    sp.add( suffix );
                }
                catch ( us.jts.fortress.SecurityException se )
                {
                    log.warn( CLS_NM + ".addSuffixes name [" + suffix.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteSuffixes()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delsuffix delsuffix : delsuffixes )
        {
            List<Suffix> suffixes = delsuffix.getSuffixes();
            for ( Suffix suffix : suffixes )
            {
                log.info( CLS_NM + ".deleteSuffixes name=" + suffix.getName() );
                try
                {
                    SuffixP sp = new SuffixP();
                    sp.delete( suffix );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".deleteSuffixes name [" + suffix.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addOrgunits()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addorgunit addorgunit : addorgunits )
        {
            List<OrgUnitAnt> ous = addorgunit.getOrgUnits();
            for ( OrgUnitAnt ou : ous )
            {
                log.info( CLS_NM + ".addOrgunits name=" + ou.getName()
                    + " typeName=" + ou.getTypeName()
                    + " description=" + ou.getDescription() );
                try
                {
                    dAdminMgr.add( ou );
                }
                catch ( us.jts.fortress.SecurityException se )
                {
                    log.warn( CLS_NM + ".addOrgunits name [" + ou.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void delOrgunits()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delorgunit delorgunit : delorgunits )
        {
            List<OrgUnitAnt> ous = delorgunit.getOrgUnits();
            for ( OrgUnitAnt ou : ous )
            {
                log.info( CLS_NM + ".deleteOrgunits name=" + ou.getName()
                    + " typeName=" + ou.getTypeName() );
                try
                {
                    dAdminMgr.delete( ou );
                }
                catch ( us.jts.fortress.SecurityException se )
                {
                    log.warn( CLS_NM + ".deleteOrgunits name [" + ou.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addUserOrgunitInheritances()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Adduserorgunitinheritance adduserorgunitinheritance : adduserorgunitinheritances )
        {
            List<Relationship> orgs = adduserorgunitinheritance.getRelationships();
            for ( Relationship relationship : orgs )
            {
                log.info( CLS_NM + ".addUserOrgunitInheritances parent=" + relationship.getParent()
                    + " child=" + relationship.getChild() );
                try
                {
                    dAdminMgr.addInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.USER ), new OrgUnit(
                        relationship.getChild(), OrgUnit.Type.USER ) );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".addUserOrgunitInheritances parent [" + relationship.getParent() + "] child ["
                        + relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteUserOrgunitInheritances()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Deluserorgunitinheritance deluserorgunitinheritance : deluserorgunitinheritances )
        {
            List<Relationship> orgs = deluserorgunitinheritance.getRelationships();
            for ( Relationship relationship : orgs )
            {
                log.info( CLS_NM + ".deleteUserOrgunitInheritances parent=" + relationship.getParent()
                    + " child=" + relationship.getChild() );
                try
                {
                    dAdminMgr.deleteInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.USER ),
                        new OrgUnit( relationship.getChild(), OrgUnit.Type.USER ) );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".deleteUserOrgunitInheritances parent [" + relationship.getParent()
                        + "] child [" + relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addPermOrgunitInheritances()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addpermorgunitinheritance addpermorgunitinheritance : addpermorgunitinheritances )
        {
            List<Relationship> orgs = addpermorgunitinheritance.getRelationships();
            for ( Relationship relationship : orgs )
            {
                log.info( CLS_NM + ".addPermOrgunitInheritances parent=" + relationship.getParent()
                    + " child=" + relationship.getChild() );
                try
                {
                    dAdminMgr.addInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.PERM ), new OrgUnit(
                        relationship.getChild(), OrgUnit.Type.PERM ) );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".addPermOrgunitInheritances parent [" + relationship.getParent() + "] child ["
                        + relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deletePermOrgunitInheritances()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Delpermorgunitinheritance delpermorgunitinheritance : delpermorgunitinheritances )
        {
            List<Relationship> orgs = delpermorgunitinheritance.getRelationships();
            for ( Relationship relationship : orgs )
            {
                log.info( CLS_NM + ".deletePermOrgunitInheritances parent=" + relationship.getParent()
                    + " child=" + relationship.getChild() );
                try
                {
                    dAdminMgr.deleteInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.PERM ),
                        new OrgUnit( relationship.getChild(), OrgUnit.Type.PERM ) );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".deletePermOrgunitInheritances parent [" + relationship.getParent()
                        + "] child [" + relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addAdminRoles()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addadminrole addrole : addadminroles )
        {
            List<AdminRoleAnt> roles = addrole.getRoles();
            for ( AdminRoleAnt role : roles )
            {
                log.info( CLS_NM + ".addAdminRoles name=" + role.getName()
                    + " description=" + role.getDescription() );
                try
                {
                    dAdminMgr.addRole( role );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".addAdminRoles name [" + role.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteAdminRoles()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Deladminrole delrole : deladminroles )
        {
            List<AdminRoleAnt> roles = delrole.getRoles();
            for ( AdminRoleAnt role : roles )
            {
                log.info( CLS_NM + ".deleteAdminRoles name=" + role.getName() );
                try
                {
                    dAdminMgr.deleteRole( role );
                }
                catch ( us.jts.fortress.SecurityException se )
                {
                    log.warn( CLS_NM + ".deleteAdminRoles name [" + role.getName() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addAdminRoleInheritances()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Addadminroleinheritance addadminroleinheritance : addadminroleinheritances )
        {
            List<Relationship> roles = addadminroleinheritance.getRelationships();
            for ( Relationship relationship : roles )
            {
                log.info( CLS_NM + ".addAdminRoleInheritances parent=" + relationship.getParent()
                    + " child=" + relationship.getChild() );
                try
                {
                    dAdminMgr.addInheritance( new AdminRole( relationship.getParent() ),
                        new AdminRole( relationship.getChild() ) );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".addAdminRoleInheritances parent [" + relationship.getParent() + "] child ["
                        + relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteAdminRoleInheritances()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Deladminroleinheritance deladminroleinheritance : deladminroleinheritances )
        {
            List<Relationship> roles = deladminroleinheritance.getRelationships();
            for ( Relationship relationship : roles )
            {
                log.info( CLS_NM + ".deleteAdminRoleInheritances parent=" + relationship.getParent()
                    + " child=" + relationship.getChild() );
                try
                {
                    dAdminMgr.deleteInheritance( new AdminRole( relationship.getParent() ),
                        new AdminRole( relationship.getChild() ) );
                }
                catch ( SecurityException se )
                {
                    log.warn( CLS_NM + ".deleteAdminRoleInheritances parent [" + relationship.getParent() + "] child ["
                        + relationship.getChild() + "] caught SecurityException=" + se );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addUserAdminRoles()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Adduseradminrole adduserrole : adduseradminroles )
        {
            List<UserAdminRole> userroles = adduserrole.getUserRoles();
            for ( UserAdminRole userRole : userroles )
            {
                log.info( CLS_NM + ".addUserAdminRoles userid=" + userRole.getUserId()
                    + " role name=" + userRole.getName() );
                try
                {
                    //AdminRole role = new AdminRole(userRole);
                    dAdminMgr.assignUser( userRole );
                }
                catch ( SecurityException se )
                {
                    String warning = CLS_NM + ".addUserAdminRoles userId [" + userRole.getUserId() + "] role name ["
                        + userRole.getName() + "] caught SecurityException=" + se;
                    log.warn( warning );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void delUserAdminRoles()
        throws BuildException
    {
        // Loop through the entityclass elements
        for ( Deluseradminrole deluserrole : deluseradminroles )
        {
            List<UserAdminRole> userroles = deluserrole.getUserRoles();
            for ( UserAdminRole userRole : userroles )
            {
                log.info( ".delUserAdminRoles userid=" + userRole.getUserId()
                    + " role name=" + userRole.getName() );
                try
                {
                    dAdminMgr.deassignUser( userRole );
                }
                catch ( SecurityException se )
                {
                    String warning = CLS_NM + ".delUserAdminRoles userId [" + userRole.getUserId() + "] role name ["
                        + userRole.getName() + "] caught SecurityException=" + se;
                    log.warn( warning );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void addConfig()
        throws BuildException
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
                    log.info( CLS_NM + ".addConfig" );
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
                log.info( CLS_NM + ".addConfig realm name [" + configNodeName + "]" );
                cfgMgr.add( configNodeName, props );
            }
            catch ( SecurityException se )
            {
                String msgHdr = CLS_NM + ".addConfig realm name [" + configNodeName + "]";
                if ( se.getErrorId() == GlobalErrIds.FT_CONFIG_ALREADY_EXISTS )
                {
                    try
                    {
                        String warning = msgHdr + " entry already exists, attempt to update";
                        log.info( warning );
                        cfgMgr.update( configNodeName, props );
                        log.info( msgHdr + " update [" + configNodeName + "] successful" );
                    }
                    catch ( us.jts.fortress.SecurityException se2 )
                    {
                        String warning = msgHdr + " update failed SecurityException=" + se2;
                        log.warn( warning );
                    }
                }
                else
                {
                    String warning = msgHdr + " failed SecurityException=" + se;
                    log.warn( warning );
                }
            }
        }
    }


    /**
     * @throws BuildException
     */
    private void deleteConfig()
        throws BuildException
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
                log.info( CLS_NM + ".delConfig realm name [" + configNodeName + "]" );
                props.remove( GlobalIds.CONFIG_REALM );
                cfgMgr.delete( configNodeName, props );
            }
            catch ( us.jts.fortress.SecurityException se )
            {
                String warning = CLS_NM + ".deleteConfig [" + configNodeName + "] caught SecurityException=" + se;
                log.warn( warning );
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

}