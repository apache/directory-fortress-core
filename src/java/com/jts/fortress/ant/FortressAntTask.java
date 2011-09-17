/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import com.jts.fortress.AdminMgr;
import com.jts.fortress.PswdPolicyMgr;
import com.jts.fortress.SecurityException;
import com.jts.fortress.configuration.ConfigMgr;
import com.jts.fortress.configuration.ConfigMgrFactory;
import com.jts.fortress.ldap.container.OrganizationalUnit;
import com.jts.fortress.ldap.container.OrganizationalUnitP;
import com.jts.fortress.ldap.suffix.Suffix;
import com.jts.fortress.ldap.suffix.SuffixP;
import com.jts.fortress.DelegatedAdminMgr;
import com.jts.fortress.DelegatedAdminMgrFactory;
import com.jts.fortress.arbac.UserAdminRole;
import com.jts.fortress.arbac.OrgUnitAnt;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.pwpolicy.PswdPolicy;
import com.jts.fortress.rbac.Permission;
import com.jts.fortress.rbac.PermObj;

import com.jts.fortress.rbac.Role;
import com.jts.fortress.rbac.SDSet;
import com.jts.fortress.rbac.User;
import com.jts.fortress.rbac.UserRole;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.input.InputRequest;
import org.apache.log4j.Logger;

/**
 * This class implements Apache Ant custom task and is used to drive the Fortress Administrative APIs using XML files.  The
 * methods in this class are not intended to be callable by outside programs.  The following APIs are supported:
 * <p/>
 * <ol>
 * <li>{@link com.jts.fortress.AdminMgr}</li>
 * <li>{@link DelegatedAdminMgr}</li>
 * <li>{@link com.jts.fortress.PswdPolicyMgr}</li>
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
 *          <addrole> ...</addrole>
 *          <delrole> ...</delrole>
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
 *          <adduser> ... </adduser>
 *          <deluser> ...</deluser>
 *          <addadminrole>  ... </addadminrole>
 *          <deladminrole>  ... </deladminrole>
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
 * <li>Delete User Role Assignments {@link com.jts.fortress.AdminMgr#deassignUser(com.jts.fortress.rbac.UserRole)}</li>
 * <li>Delete User AdminRole Assignments {@link DelegatedAdminMgr#deassignUser(com.jts.fortress.arbac.UserAdminRole)}</li>
 * <li>Revoke Permission Assignments Delete{@link com.jts.fortress.AdminMgr#revokePermission(com.jts.fortress.rbac.Permission, com.jts.fortress.rbac.Role)}</li>
 * <li>Delete Users {@link com.jts.fortress.AdminMgr#disableUser(com.jts.fortress.rbac.User)}</li>
 * <li>Delete Password Policies {@link com.jts.fortress.PswdPolicyMgr#delete(com.jts.fortress.pwpolicy.PswdPolicy)}</li>
 * <li>Delete Permission Operations {@link com.jts.fortress.AdminMgr#deletePermission(com.jts.fortress.rbac.Permission)}</li>
 * <li>Delete Permission Objects {@link com.jts.fortress.AdminMgr#deletePermObj(com.jts.fortress.rbac.PermObj)}</li>
 * <li>Delete SSD and DSD Sets {@link com.jts.fortress.AdminMgr#deleteDsdSet(com.jts.fortress.rbac.SDSet)} and {@link com.jts.fortress.AdminMgr#deleteSsdSet(com.jts.fortress.rbac.SDSet)}</li>
 * <li>Delete RBAC Roles {@link com.jts.fortress.AdminMgr#deleteRole(com.jts.fortress.rbac.Role)}</li>
 * <li>Delete ARBAC Roles {@link com.jts.fortress.DelegatedAdminMgr#deleteRole(com.jts.fortress.arbac.AdminRole)}</li>
 * <li>Delete User and Perm OUs {@link com.jts.fortress.DelegatedAdminMgr#delete(com.jts.fortress.arbac.OrgUnit)} USER and PERM</li>
 * <li>Delete Configuration Entries {@link com.jts.fortress.configuration.ConfigMgr#delete(String, java.util.Properties)}</li>
 * <li>Delete Containers {@link com.jts.fortress.ldap.container.OrganizationalUnitP#delete(com.jts.fortress.ldap.container.OrganizationalUnit)}</li>
 * <li>Delete Suffix {@link com.jts.fortress.ldap.suffix.SuffixP#delete(com.jts.fortress.ldap.suffix.Suffix)}}</li>
 * <li>Add Suffix {@link com.jts.fortress.ldap.suffix.SuffixP#add(com.jts.fortress.ldap.suffix.Suffix)}}</li>
 * <li>Add Containers {@link com.jts.fortress.ldap.container.OrganizationalUnitP#add(com.jts.fortress.ldap.container.OrganizationalUnit)}</li>
 * <li>Add Configuration Parameters {@link ConfigMgr#add(String, java.util.Properties)}</li>
 * <li>Add User and Perm OUs {@link DelegatedAdminMgr#add(com.jts.fortress.arbac.OrgUnit)} USER and PERM</li>
 * <li>Add ARBAC Roles {@link com.jts.fortress.DelegatedAdminMgr#addRole(com.jts.fortress.arbac.AdminRole)}</li>
 * <li>Add RBAC Roles {@link com.jts.fortress.AdminMgr#addRole(com.jts.fortress.rbac.Role)}</li>
 * <li>Add DSD and SSD Sets {@link com.jts.fortress.AdminMgr#createDsdSet(com.jts.fortress.rbac.SDSet)} and {@link com.jts.fortress.AdminMgr#createSsdSet(com.jts.fortress.rbac.SDSet)}</li>
 * <li>Add Permission Objects {@link com.jts.fortress.AdminMgr#addPermObj(com.jts.fortress.rbac.PermObj)}</li>
 * <li>Add Permission Operations {@link com.jts.fortress.AdminMgr#addPermission(com.jts.fortress.rbac.Permission)}</li>
 * <li>Add Password Policies {@link com.jts.fortress.PswdPolicyMgr#add(com.jts.fortress.pwpolicy.PswdPolicy)}</li>
 * <li>Add Users {@link com.jts.fortress.AdminMgr#addUser(com.jts.fortress.rbac.User)}</li>
 * <li>Grant RBAC Permissions {@link com.jts.fortress.AdminMgr#grantPermission(com.jts.fortress.rbac.Permission, com.jts.fortress.rbac.Role)}</li>
 * <li>Assign ARBAC Roles {@link DelegatedAdminMgr#assignUser(com.jts.fortress.arbac.UserAdminRole)}</li>
 * <li>Assign RBAC Roles {@link com.jts.fortress.AdminMgr#assignUser(com.jts.fortress.rbac.UserRole)}</li>
 * </li>
 * </ol>
 * <p/>
 *
 * @author smckinn
 * @created February 5, 2011
 */
public class FortressAntTask extends Task implements InputHandler
{
    final private List<com.jts.fortress.ant.Addconfig> addconfig = new ArrayList<com.jts.fortress.ant.Addconfig>();
    final private List<com.jts.fortress.ant.Delconfig> delconfig = new ArrayList<com.jts.fortress.ant.Delconfig>();
    final private List<Adduser> addusers = new ArrayList<Adduser>();
    final private List<Deluser> delusers = new ArrayList<Deluser>();
    final private List<Adduserrole> adduserroles = new ArrayList<Adduserrole>();
    final private List<Deluserrole> deluserroles = new ArrayList<Deluserrole>();
    final private List<com.jts.fortress.ant.Addrole> addroles = new ArrayList<com.jts.fortress.ant.Addrole>();
    final private List<Delrole> delroles = new ArrayList<Delrole>();
    final private List<Addsdset> addsdsets = new ArrayList<Addsdset>();
    final private List<Delsdset> delsdsets = new ArrayList<Delsdset>();
    final private List<AddpermOp> addpermOps = new ArrayList<AddpermOp>();
    final private List<DelpermOp> delpermOps = new ArrayList<DelpermOp>();
    final private List<AddpermObj> addpermObjs = new ArrayList<AddpermObj>();
    final private List<com.jts.fortress.ant.DelpermObj> delpermObjs = new ArrayList<com.jts.fortress.ant.DelpermObj>();
    final private List<AddpermGrant> addpermGrants = new ArrayList<AddpermGrant>();
    final private List<DelpermGrant> delpermGrants = new ArrayList<DelpermGrant>();
    final private List<com.jts.fortress.ant.Addpwpolicy> addpolicies = new ArrayList<com.jts.fortress.ant.Addpwpolicy>();
    final private List<com.jts.fortress.ant.Delpwpolicy> delpolicies = new ArrayList<com.jts.fortress.ant.Delpwpolicy>();
    final private List<Addcontainer> addcontainers = new ArrayList<Addcontainer>();
    final private List<Delcontainer> delcontainers = new ArrayList<Delcontainer>();
    final private List<Addsuffix> addsuffixes = new ArrayList<Addsuffix>();
    final private List<Delsuffix> delsuffixes = new ArrayList<Delsuffix>();
    final private List<com.jts.fortress.ant.Addorgunit> addorgunits = new ArrayList<com.jts.fortress.ant.Addorgunit>();
    final private List<Delorgunit> delorgunits = new ArrayList<Delorgunit>();
    final private List<com.jts.fortress.ant.Addadminrole> addadminroles = new ArrayList<com.jts.fortress.ant.Addadminrole>();
    final private List<Deladminrole> deladminroles = new ArrayList<Deladminrole>();
    final private List<com.jts.fortress.ant.Adduseradminrole> adduseradminroles = new ArrayList<com.jts.fortress.ant.Adduseradminrole>();
    final private List<com.jts.fortress.ant.Deluseradminrole> deluseradminroles = new ArrayList<com.jts.fortress.ant.Deluseradminrole>();
    private ConfigMgr cfgMgr = null;
    private AdminMgr adminMgr = null;
    private DelegatedAdminMgr dAdminMgr = null;
    private PswdPolicyMgr policyMgr = null;
    private static final String OCLS_NM = FortressAntTask.class.getName();
    private static final Logger log = Logger.getLogger(OCLS_NM);

    /**
     * Default constructor initializes he Manager APIs.
     */
    public FortressAntTask()
    {
        try
        {
            cfgMgr = ConfigMgrFactory.createInstance();
            adminMgr = com.jts.fortress.AdminMgrFactory.createInstance();
            dAdminMgr = DelegatedAdminMgrFactory.createInstance();
            policyMgr = com.jts.fortress.PswdPolicyMgrFactory.createInstance();
        }
        catch (SecurityException se)
        {
            log.fatal(OCLS_NM + " FortressAntTask constructor caught SecurityException=" + se);
            se.printStackTrace();
        }
    }

    /**
     * Used by Apache Ant to load data from xml into entities.
     *
     * @param request
     * @throws org.apache.tools.ant.BuildException
     *
     */
    public void handleInput(InputRequest request)
        throws org.apache.tools.ant.BuildException
    {
        log.info(OCLS_NM + ".handleInput request=" + request);
    }

    /**
     * Load the entity with data.
     *
     * @param addcfg contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddconfig(Addconfig addcfg)
    {
        this.addconfig.add(addcfg);
    }

    /**
     * Load the entity with data.
     *
     * @param delcfg contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelconfig(Delconfig delcfg)
    {
        this.delconfig.add(delcfg);
    }

    /**
     * Load the entity with data.
     *
     * @param adduser contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAdduser(Adduser adduser)
    {
        this.addusers.add(adduser);
    }

    /**
     * Load the entity with data.
     *
     * @param deluser contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDeluser(Deluser deluser)
    {
        this.delusers.add(deluser);
    }

    /**
     * Load the entity with data.
     *
     * @param adduserrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAdduserrole(Adduserrole adduserrole)
    {
        this.adduserroles.add(adduserrole);
    }

    /**
     * Load the entity with data.
     *
     * @param deluserrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDeluserrole(Deluserrole deluserrole)
    {
        this.deluserroles.add(deluserrole);
    }

    /**
     * Load the entity with data.
     *
     * @param addrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddrole(Addrole addrole)
    {
        this.addroles.add(addrole);
    }

    /**
     * Load the entity with data.
     *
     * @param delrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelrole(Delrole delrole)
    {
        this.delroles.add(delrole);
    }

    /**
     * Load the entity with data.
     *
     * @param addsd contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddsdset(Addsdset addsd)
    {
        this.addsdsets.add(addsd);
    }

    /**
     * Load the entity with data.
     *
     * @param delsd contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelsdset(Delsdset delsd)
    {
        this.delsdsets.add(delsd);
    }

    /**
     * Load the entity with data.
     *
     * @param addpermOp contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddpermOp(AddpermOp addpermOp)
    {
        this.addpermOps.add(addpermOp);
    }

    /**
     * Load the entity with data.
     *
     * @param delpermOp contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelpermOp(DelpermOp delpermOp)
    {
        this.delpermOps.add(delpermOp);
    }

    /**
     * Load the entity with data.
     *
     * @param addpermObj contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddpermObj(AddpermObj addpermObj)
    {
        this.addpermObjs.add(addpermObj);
    }

    /**
     * Load the entity with data.
     *
     * @param delpermObj contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelpermObj(DelpermObj delpermObj)
    {
        this.delpermObjs.add(delpermObj);
    }

    /**
     * Load the entity with data.
     *
     * @param addpermGrant contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddpermGrant(AddpermGrant addpermGrant)
    {
        this.addpermGrants.add(addpermGrant);
    }

    /**
     * Load the entity with data.
     *
     * @param delpermGrant contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelpermGrant(DelpermGrant delpermGrant)
    {
        this.delpermGrants.add(delpermGrant);
    }

    /**
     * Load the entity with data.
     *
     * @param addpwpolicy contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddpwpolicy(Addpwpolicy addpwpolicy)
    {
        this.addpolicies.add(addpwpolicy);
    }

    /**
     * Load the entity with data.
     *
     * @param delpwpolicy contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelpwpolicy(Delpwpolicy delpwpolicy)
    {
        this.delpolicies.add(delpwpolicy);
    }

    /**
     * Load the entity with data.
     *
     * @param addcontainer contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddcontainer(Addcontainer addcontainer)
    {
        this.addcontainers.add(addcontainer);
    }

    /**
     * Load the entity with data.
     *
     * @param delcontainer contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelcontainer(Delcontainer delcontainer)
    {
        this.delcontainers.add(delcontainer);
    }

    /**
     * Load the entity with data.
     *
     * @param addsuffix contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddsuffix(Addsuffix addsuffix)
    {
        this.addsuffixes.add(addsuffix);
    }

    /**
     * Load the entity with data.
     *
     * @param delsuffix contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelsuffix(Delsuffix delsuffix)
    {
        this.delsuffixes.add(delsuffix);
    }

    /**
     * Load the entity with data.
     *
     * @param addorgunit contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddorgunit(Addorgunit addorgunit)
    {
        this.addorgunits.add(addorgunit);
    }

    /**
     * Load the entity with data.
     *
     * @param delorgunit contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDelorgunit(Delorgunit delorgunit)
    {
        this.delorgunits.add(delorgunit);
    }

    /**
     * Load the entity with data.
     *
     * @param addrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAddadminrole(Addadminrole addrole)
    {
        this.addadminroles.add(addrole);
    }

    /**
     * Load the entity with data.
     *
     * @param delrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDeladminrole(Deladminrole delrole)
    {
        this.deladminroles.add(delrole);
    }

    /**
     * Load the entity with data.
     *
     * @param adduserrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addAdduseradminrole(Adduseradminrole adduserrole)
    {
        this.adduseradminroles.add(adduserrole);
    }

    /**
     * Load the entity with data.
     *
     * @param deluserrole contains the ant initialized data entities to be handed off for further processing.
     */
    public void addDeluseradminrole(Deluseradminrole deluserrole)
    {
        this.deluseradminroles.add(deluserrole);
    }

    /**
     * @param list
     * @return
     */
    private boolean isListNotNull(List list)
    {
        return (list != null && list.size() > 0);
    }

    /**
     * @throws BuildException
     */
    public void execute()
        throws BuildException
    {
        if (isListNotNull(deluserroles))
        {
            delUserRoles();
        }
        if (isListNotNull(deluseradminroles))
        {
            delUserAdminRoles();
        }
        if (isListNotNull(delpermGrants))
        {
            deletePermGrants();
        }
        if (isListNotNull(delusers))
        {
            deleteUsers();
        }
        if (isListNotNull(delpolicies))
        {
            deletePolicies();
        }
        if (isListNotNull(delpermOps))
        {
            deletePermOps();
        }
        if (isListNotNull(delpermObjs))
        {
            deletePermObjs();
        }
        if (isListNotNull(delsdsets))
        {
            deleteSdsets();
        }
        if (isListNotNull(delroles))
        {
            deleteRoles();
        }
        if (isListNotNull(deladminroles))
        {
            deleteAdminRoles();
        }
        if (isListNotNull(delorgunits))
        {
            delOrgunits();
        }
        if (isListNotNull(delconfig))
        {
            deleteConfig();
        }
        if (isListNotNull(delcontainers))
        {
            deleteContainers();
        }
        if (isListNotNull(delsuffixes))
        {
            deleteSuffixes();
        }
        if (isListNotNull(addsuffixes))
        {
            addSuffixes();
        }
        if (isListNotNull(addcontainers))
        {
            addContainers();
        }
        if (isListNotNull(addconfig))
        {
            addConfig();
        }
        if (isListNotNull(addorgunits))
        {
            addOrgunits();
        }
        if (isListNotNull(addadminroles))
        {
            addAdminRoles();
        }
        if (isListNotNull(addroles))
        {
            addRoles();
        }
        if (isListNotNull(addsdsets))
        {
            addSdsets();
        }
        if (isListNotNull(addpermObjs))
        {
            addPermObjs();
        }
        if (isListNotNull(addpermOps))
        {
            addPermOps();
        }
        if (isListNotNull(addpolicies))
        {
            addPolicies();
        }
        if (isListNotNull(addusers))
        {
            addUsers();
        }
        if (isListNotNull(addpermGrants))
        {
            addPermGrants();
        }
        if (isListNotNull(adduseradminroles))
        {
            addUserAdminRoles();
        }
        if (isListNotNull(adduserroles))
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
        for (Adduser adduser : addusers)
        {
            List<UserAnt> users = adduser.getUsers();
            for (UserAnt user : users)
            {
                log.info(OCLS_NM + ".addUsers userid=" + user.getUserId()
                    + " description=" + user.getDescription()
                    + " orgUnit=" + user.getOu());
                try
                {
                    try
                    {
                        adminMgr.addUser(user);
                    }
                    catch (SecurityException se)
                    {
                        // If User entity already there then call the udpate method.
                        if (se.getErrorId() == GlobalErrIds.USER_ID_DUPLICATE)
                        {
                            adminMgr.updateUser(user);
                            log.info(OCLS_NM + ".addUsers - Update entity - userId=" + user.getUserId());
                        }
                        else
                        {
                            throw se;
                        }
                    }
                }
                catch (SecurityException se)
                {
                    log.warn(OCLS_NM + ".addUsers userId <" + user.getUserId() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Deluser deluser : delusers)
        {
            List<UserAnt> users = deluser.getUsers();
            for (UserAnt user : users)
            {
                log.info(OCLS_NM + ".deleteUsers userid=" + user.getUserId());
                try
                {
                    adminMgr.deleteUser(user);
                }
                catch (SecurityException se)
                {
                    log.warn(OCLS_NM + ".deleteUsers userId <" + user.getUserId() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Adduserrole adduserrole : adduserroles)
        {
            List<UserRole> userroles = adduserrole.getUserRoles();
            for (UserRole userRole : userroles)
            {
                log.info(OCLS_NM + ".addUserRoles userid=" + userRole.getUserId()
                    + " role name=" + userRole.getName());

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
                    adminMgr.assignUser(userRole);
                }
                catch (SecurityException se)
                {
                    String warning = OCLS_NM + ".addUserRoles userId <" + userRole.getUserId() + "> role name <" + userRole.getName() + "> caught SecurityException=" + se;
                    log.warn(warning);
                    se.printStackTrace();
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
        for (Deluserrole deluserrole : deluserroles)
        {
            List<UserRole> userroles = deluserrole.getUserRoles();
            for (UserRole userRole : userroles)
            {
                log.info(".delUserRoles userid=" + userRole.getUserId()
                    + " role name=" + userRole.getName());
                try
                {
                    adminMgr.deassignUser(userRole);
                }
                catch (com.jts.fortress.SecurityException se)
                {
                    String warning = OCLS_NM + ".delUserRoles userId <" + userRole.getUserId() + "> role name <" + userRole.getName() + "> caught SecurityException=" + se;
                    log.warn(warning);
                    se.printStackTrace();
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
        for (Addrole addrole : addroles)
        {
            List<Role> roles = addrole.getRoles();
            for (Role role : roles)
            {
                log.info(OCLS_NM + ".addRoles name=" + role.getName()
                    + " description=" + role.getDescription());
                try
                {
                    adminMgr.addRole(role);
                }
                catch (SecurityException se)
                {
                    log.warn(OCLS_NM + ".addRoles name <" + role.getName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Delrole delrole : delroles)
        {
            List<Role> roles = delrole.getRoles();
            for (Role role : roles)
            {
                log.info(OCLS_NM + ".deleteRoles name=" + role.getName());
                try
                {
                    adminMgr.deleteRole(role);
                }
                catch (com.jts.fortress.SecurityException se)
                {
                    log.warn(OCLS_NM + ".deleteRoles name <" + role.getName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Addsdset addsdset : addsdsets)
        {
            List<SDSetAnt> sds = addsdset.getSdset();
            for (SDSetAnt sd : sds)
            {
                log.info(OCLS_NM + ".addSdsets name=" + sd.getName()
                    + " description=" + sd.getDescription());
                try
                {
                    adminMgr.createSsdSet(sd);
                }
                catch (SecurityException se)
                {
                    log.warn(OCLS_NM + ".addSdsets name <" + sd.getName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Delsdset delsdset : delsdsets)
        {
            List<SDSetAnt> sds = delsdset.getSdset();
            for (SDSetAnt sd : sds)
            {
                log.info(OCLS_NM + ".deleteSdsets name=" + sd.getName());
                try
                {
                    if (sd.getSetType().equals("STATIC"))
                    {
                        sd.setType(SDSet.SDType.STATIC);
                    }
                    else
                    {
                        sd.setType(SDSet.SDType.DYNAMIC);
                    }
                    adminMgr.deleteSsdSet(sd);
                }
                catch (SecurityException se)
                {
                    log.warn(OCLS_NM + ".deleteSdsets name <" + sd.getName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (AddpermObj addpermObj : addpermObjs)
        {
            List<PermObj> permObjs = addpermObj.getPermObjs();
            for (PermObj permObj : permObjs)
            {
                log.info(OCLS_NM + ".addPermObjs objectName=" + permObj.getObjectName()
                    + " description=" + permObj.getDescription()
                    + " orgUnit=" + permObj.getOu()
                    + " type=" + permObj.getType());
                try
                {
                    try
                    {
                        adminMgr.addPermObj(permObj);
                    }
                    catch (com.jts.fortress.SecurityException se)
                    {
                        // If Perm Object entity already there then call the udpate method.
                        if (se.getErrorId() == com.jts.fortress.constants.GlobalErrIds.PERM_DUPLICATE)
                        {
                            adminMgr.updatePermObj(permObj);
                            System.out.println(OCLS_NM + ".addPermObjs - update entity objectName=" + permObj.getObjectName());
                        }
                        else
                        {
                            throw se;
                        }
                    }
                }
                catch (com.jts.fortress.SecurityException se)
                {
                    log.warn(OCLS_NM + ".addPermObjs objectName <" + permObj.getObjectName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (DelpermObj delpermObj : delpermObjs)
        {
            List<PermObj> permObjs = delpermObj.getObjs();
            for (PermObj permObj : permObjs)
            {
                log.info(OCLS_NM + ".deletePermObjs objectName=" + permObj.getObjectName()
                    + " description=" + permObj.getDescription());
                try
                {
                    adminMgr.deletePermObj(permObj);
                }
                catch (com.jts.fortress.SecurityException se)
                {
                    log.warn(OCLS_NM + ".deletePermObjs name <" + permObj.getObjectName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (AddpermOp addpermOp : addpermOps)
        {
            List<PermAnt> permissions = addpermOp.getPermOps();
            for (PermAnt permission : permissions)
            {
                log.info(OCLS_NM + ".addPermOps name=" + permission.getOpName()
                    + " objectName=" + permission.getObjectName());
                try
                {
                    try
                    {
                        adminMgr.addPermission(permission);
                    }
                    catch (com.jts.fortress.SecurityException se)
                    {
                        // If Perm Object entity already there then call the udpate method.
                        if (se.getErrorId() == com.jts.fortress.constants.GlobalErrIds.PERM_DUPLICATE)
                        {
                            adminMgr.updatePermission(permission);
                            log.info(OCLS_NM + ".addPermOps - update entity - name=" + permission.getOpName()
                                + " objectName=" + permission.getObjectName());
                        }
                        else
                        {
                            throw se;
                        }
                    }
                }
                catch (SecurityException se)
                {
                    log.warn(OCLS_NM + ".addPermOps name <" + permission.getOpName() + "> objectName <" + permission.getObjectName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (DelpermOp delpermOp : delpermOps)
        {
            List<PermAnt> permissions = delpermOp.getPermOps();
            for (Permission permission : permissions)
            {
                log.info(OCLS_NM + ".deletePermOps name=" + permission.getOpName() + " objectName=" + permission.getObjectName());
                try
                {
                    adminMgr.deletePermission(permission);
                }
                catch (com.jts.fortress.SecurityException se)
                {
                    log.warn(OCLS_NM + ".deletePermOps name <" + permission.getOpName() + "> objectName<" + permission.getObjectName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (AddpermGrant addpermGrant : addpermGrants)
        {
            List<PermGrant> permGrants = addpermGrant.getPermGrants();
            for (PermGrant permGrant : permGrants)
            {
                String info = OCLS_NM + ".addPermGrants: Add permission grant object name=" + permGrant.getObjName()
                    + " operation name=" + permGrant.getOpName()
                    + " object id=" + permGrant.getObjId()
                    + " role name=" + permGrant.getRoleNm()
                    + " userId=" + permGrant.getUserId();
                log.info(info);
                try
                {
                    Permission perm = new Permission(permGrant.getObjName(), permGrant.getOpName(), permGrant.isAdmin());
                    perm.setOpName(permGrant.getOpName());
                    perm.setObjectId(permGrant.getObjId());
                    if (permGrant.getRoleNm() != null && permGrant.getRoleNm().length() > 0)
                    {
                        adminMgr.grantPermission(perm, new Role(permGrant.getRoleNm()));
                    }
                    else if (permGrant.getUserId() != null && permGrant.getUserId().length() > 0)
                    {
                        adminMgr.grantPermission(perm, new User(permGrant.getUserId()));
                    }
                    else
                    {
                        String warning = OCLS_NM + ".addPermGrants called without user or role set in xml";
                        log.warn(warning);
                    }
                }
                catch (SecurityException se)
                {
                    String warning = OCLS_NM + ".addPermGrants caught SecurityException=" + se;
                    log.warn(warning);
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
        for (DelpermGrant delpermGrant : delpermGrants)
        {
            List<PermGrant> permGrants = delpermGrant.getPermGrants();
            for (PermGrant permGrant : permGrants)
            {
                String info = OCLS_NM + ".deletePermGrants: Delete permission grant object name=" + permGrant.getObjName()
                    + " operation name=" + permGrant.getOpName()
                    + " role name=" + permGrant.getRoleNm()
                    + " userId=" + permGrant.getUserId();
                log.info(info);
                try
                {
                    Permission perm = new Permission(permGrant.getObjName(), permGrant.getOpName(), permGrant.isAdmin());
                    perm.setOpName(permGrant.getOpName());
                    perm.setObjectId(permGrant.getObjId());
                    if (permGrant.getRoleNm() != null && permGrant.getRoleNm().length() > 0)
                    {
                        adminMgr.revokePermission(perm, new Role(permGrant.getRoleNm()));
                    }
                    else if (permGrant.getUserId() != null && permGrant.getUserId().length() > 0)
                    {
                        adminMgr.revokePermission(perm, new User(permGrant.getUserId()));
                    }
                    else
                    {
                        String warning = OCLS_NM + ".deletePermGrants called without user or role set in xml";
                        log.warn(warning);
                    }
                }
                catch (SecurityException se)
                {
                    String warning = OCLS_NM + ".deletePermGrants caught SecurityException=" + se;
                    log.warn(warning);
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
        for (Addpwpolicy addpwpolicy : addpolicies)
        {
            List<PswdPolicy> policies = addpwpolicy.getPolicies();
            for (PswdPolicy policy : policies)
            {
                log.info(OCLS_NM + ".addPolicies name=" + policy.getName());
                try
                {
                    policyMgr.add(policy);
                }
                catch (SecurityException se)
                {
                    log.warn(OCLS_NM + ".addPolicies name <" + policy.getName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Delpwpolicy delpwpolicy : delpolicies)
        {
            List<PswdPolicy> policies = delpwpolicy.getPolicies();
            for (PswdPolicy policy : policies)
            {
                log.info(OCLS_NM + ".deletePolicies name=" + policy.getName());
                try
                {
                    policyMgr.delete(policy);
                }
                catch (SecurityException se)
                {
                    log.warn(OCLS_NM + ".deletePolicies name <" + policy.getName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Addcontainer addcontainer : addcontainers)
        {
            List<OrganizationalUnit> containers = addcontainer.getContainers();
            for (OrganizationalUnit ou : containers)
            {
                log.info(OCLS_NM + ".addContainers name=" + ou.getName()
                    + " description=" + ou.getDescription());
                try
                {
                    OrganizationalUnitP op = new OrganizationalUnitP();
                    op.add(ou);
                }
                catch (SecurityException se)
                {
                    log.warn(OCLS_NM + ".addContainers name <" + ou.getName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Delcontainer delcontainer : delcontainers)
        {
            List<OrganizationalUnit> containers = delcontainer.getContainers();
            for (OrganizationalUnit ou : containers)
            {
                log.info(OCLS_NM + ".deleteContainers name=" + ou.getName());
                try
                {
                    OrganizationalUnitP op = new OrganizationalUnitP();
                    op.delete(ou);
                }
                catch (SecurityException se)
                {
                    log.warn(OCLS_NM + ".deleteContainers name <" + ou.getName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Addsuffix addsuffix : addsuffixes)
        {
            List<Suffix> suffixes = addsuffix.getSuffixes();
            for (Suffix suffix : suffixes)
            {
                log.info(OCLS_NM + ".addSuffixes name=" + suffix.getName()
                    + " description=" + suffix.getDescription());
                try
                {
                    SuffixP sp = new SuffixP();
                    sp.add(suffix);
                }
                catch (com.jts.fortress.SecurityException se)
                {
                    log.warn(OCLS_NM + ".addSuffixes name <" + suffix.getName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Delsuffix delsuffix : delsuffixes)
        {
            List<Suffix> suffixes = delsuffix.getSuffixes();
            for (Suffix suffix : suffixes)
            {
                log.info(OCLS_NM + ".deleteSuffixes name=" + suffix.getName());
                try
                {
                    SuffixP sp = new SuffixP();
                    sp.delete(suffix);
                }
                catch (SecurityException se)
                {
                    log.warn(OCLS_NM + ".deleteSuffixes name <" + suffix.getName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Addorgunit addorgunit : addorgunits)
        {
            List<OrgUnitAnt> ous = addorgunit.getOrgUnits();
            for (com.jts.fortress.arbac.OrgUnitAnt ou : ous)
            {
                log.info(OCLS_NM + ".addOrgunits name=" + ou.getName()
                    + " typeName=" + ou.getTypeName()
                    + " description=" + ou.getDescription());
                try
                {
                    dAdminMgr.add(ou);
                }
                catch (com.jts.fortress.SecurityException se)
                {
                    log.warn(OCLS_NM + ".addOrgunits name <" + ou.getName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Delorgunit delorgunit : delorgunits)
        {
            List<com.jts.fortress.arbac.OrgUnitAnt> ous = delorgunit.getOrgUnits();
            for (OrgUnitAnt ou : ous)
            {
                log.info(OCLS_NM + ".deleteOrgunits name=" + ou.getName()
                    + " typeName=" + ou.getTypeName());
                try
                {
                    dAdminMgr.delete(ou);
                }
                catch (com.jts.fortress.SecurityException se)
                {
                    log.warn(OCLS_NM + ".deleteOrgunits name <" + ou.getName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Addadminrole addrole : addadminroles)
        {
            List<AdminRoleAnt> roles = addrole.getRoles();
            for (AdminRoleAnt role : roles)
            {
                log.info(OCLS_NM + ".addAdminRoles name=" + role.getName()
                    + " description=" + role.getDescription());
                try
                {
                    dAdminMgr.addRole(role);
                }
                catch (SecurityException se)
                {
                    log.warn(OCLS_NM + ".addAdminRoles name <" + role.getName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Deladminrole delrole : deladminroles)
        {
            List<AdminRoleAnt> roles = delrole.getRoles();
            for (AdminRoleAnt role : roles)
            {
                log.info(OCLS_NM + ".deleteAdminRoles name=" + role.getName());
                try
                {
                    dAdminMgr.deleteRole(role);
                }
                catch (com.jts.fortress.SecurityException se)
                {
                    log.warn(OCLS_NM + ".deleteAdminRoles name <" + role.getName() + "> caught SecurityException=" + se);
                    se.printStackTrace();
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
        for (Adduseradminrole adduserrole : adduseradminroles)
        {
            List<com.jts.fortress.arbac.UserAdminRole> userroles = adduserrole.getUserRoles();
            for (UserAdminRole userRole : userroles)
            {
                log.info(OCLS_NM + ".addUserAdminRoles userid=" + userRole.getUserId()
                    + " role name=" + userRole.getName());
                try
                {
                    //AdminRole role = new AdminRole(userRole);
                    dAdminMgr.assignUser(userRole);
                }
                catch (SecurityException se)
                {
                    String warning = OCLS_NM + ".addUserAdminRoles userId <" + userRole.getUserId() + "> role name <" + userRole.getName() + "> caught SecurityException=" + se;
                    log.warn(warning);
                    se.printStackTrace();
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
        for (Deluseradminrole deluserrole : deluseradminroles)
        {
            List<UserAdminRole> userroles = deluserrole.getUserRoles();
            for (UserAdminRole userRole : userroles)
            {
                log.info(".delUserAdminRoles userid=" + userRole.getUserId()
                    + " role name=" + userRole.getName());
                try
                {
                    dAdminMgr.deassignUser(userRole);
                }
                catch (SecurityException se)
                {
                    String warning = OCLS_NM + ".delUserAdminRoles userId <" + userRole.getUserId() + "> role name <" + userRole.getName() + "> caught SecurityException=" + se;
                    log.warn(warning);
                    se.printStackTrace();
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
        for (Addconfig addcfg : addconfig)
        {
            try
            {
                List<ConfigAnt> cfgs = addcfg.getConfig();
                for (ConfigAnt cfg : cfgs)
                {
                    log.info(OCLS_NM + ".addConfig");
                    String val = cfg.getProps();
                    int indx = val.indexOf(':');
                    if (indx >= 1)
                    {
                        String name = val.substring(0, indx);
                        String value = val.substring(indx + 1);
                        props.setProperty(name, value);
                    }
                }
                configNodeName = props.getProperty(GlobalIds.CONFIG_REALM);
                log.info(OCLS_NM + ".addConfig realm name <" + configNodeName + ">");
                cfgMgr.add(configNodeName, props);
            }
            catch (SecurityException se)
            {
                String msgHdr = OCLS_NM + ".addConfig realm name <" + configNodeName + ">";
                if (se.getErrorId() == GlobalErrIds.FT_CONFIG_ALREADY_EXISTS)
                {
                    try
                    {
                        String warning = msgHdr + " entry already exists, attempt to update";
                        log.info(warning);
                        cfgMgr.update(configNodeName, props);
                        log.info(msgHdr + " update <" + configNodeName + "> successful");
                    }
                    catch (com.jts.fortress.SecurityException se2)
                    {
                        String warning = msgHdr + " update failed SecurityException=" + se2;
                        log.warn(warning);
                    }
                }
                else
                {
                    String warning = msgHdr + " failed SecurityException=" + se;
                    log.warn(warning);
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
        for (Delconfig delcfg : delconfig)
        {
            try
            {
                List<ConfigAnt> cfgs = delcfg.getConfig();
                for (ConfigAnt cfg : cfgs)
                {
                    String val = cfg.getProps();
                    int indx = val.indexOf(':');
                    if (indx >= 1)
                    {
                        String name = val.substring(0, indx);
                        String value = val.substring(indx + 1);
                        props.setProperty(name, value);
                    }
                }
                configNodeName = props.getProperty(GlobalIds.CONFIG_REALM);
                log.info(OCLS_NM + ".delConfig realm name <" + configNodeName + ">");
                props.remove(GlobalIds.CONFIG_REALM);
                cfgMgr.delete(configNodeName, props);
            }
            catch (com.jts.fortress.SecurityException se)
            {
                String warning = OCLS_NM + ".deleteConfig <" + configNodeName + "> caught SecurityException=" + se;
                log.warn(warning);
            }
        }
    }

    public static Properties getProperties(String inputString)
    {
        Properties props = new Properties();
        if (inputString != null && inputString.length() > 0)
        {
            // todo: remove the hardcoded literal here:
            StringTokenizer maxTkn = new StringTokenizer(inputString, ";");
            if (maxTkn.countTokens() > 0)
            {
                while (maxTkn.hasMoreTokens())
                {
                    String val = maxTkn.nextToken();
                    // todo: remove the hardcoded literal here:
                    int indx = val.indexOf(':');
                    if (indx >= 1)
                    {
                        String name = val.substring(0, indx);
                        String value = val.substring(indx + 1);
                        props.setProperty(name, value);
                    }
                }
            }
        }
        return props;
    }

}