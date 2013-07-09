/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac.dao.apache;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.SearchCursor;
import org.apache.directory.api.ldap.model.entry.DefaultAttribute;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.DefaultModification;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.exception.LdapAttributeInUseException;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.exception.LdapNoSuchAttributeException;
import org.apache.directory.api.ldap.model.exception.LdapNoSuchObjectException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.jts.fortress.CreateException;
import us.jts.fortress.FinderException;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.GlobalIds;
import us.jts.fortress.ObjectFactory;
import us.jts.fortress.RemoveException;
import us.jts.fortress.UpdateException;
import us.jts.fortress.ldap.ApacheDsDataProvider;
import us.jts.fortress.rbac.AdminRole;
import us.jts.fortress.rbac.AdminRoleUtil;
import us.jts.fortress.rbac.OrgUnit;
import us.jts.fortress.rbac.PermObj;
import us.jts.fortress.rbac.Permission;
import us.jts.fortress.rbac.Role;
import us.jts.fortress.rbac.RoleUtil;
import us.jts.fortress.rbac.Session;
import us.jts.fortress.rbac.User;
import us.jts.fortress.util.attr.AttrHelper;
import us.jts.fortress.util.attr.VUtil;


/**
 * Permission data access class for LDAP. 
 * <p/>
 * This DAO class maintains the PermObj and Permission entities.
 * <h3>The Fortress PermObj Entity Class is a composite of 3 LDAP Schema object classes</h2>
 * <h4>PermObj Base - ftObject STRUCTURAL Object Class is used to store object name, id and type variables on target entity.</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass	( 1.3.6.1.4.1.38088.2.2</code>
 * <li> <code>NAME 'ftObject'</code>
 * <li> <code>DESC 'Fortress Permission Object Class'</code>
 * <li> <code>SUP organizationalunit</code>                                              GlobalIds
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST (</code>
 * <li> <code>ftId $ ftObjNm ) </code>
 * <li> <code>MAY ( ftType ) )  </code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>PermObj - ftProperties AUXILIARY Object Class is used to store client specific name/value pairs on target entity.</h4>
 * <code>This aux object class can be used to store custom attributes.</code><br />
 * <code>The properties collections consist of name/value pairs and are not constrainted by Fortress.</code><br />
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.2</code>
 * <li> <code>NAME 'ftProperties'</code>
 * <li> <code>DESC 'Fortress Properties AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY ( ftProps ) ) </code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>PermObj - ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.</h4>
 * <ul>
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.4</code>
 * <li> <code>NAME 'ftMods'</code>
 * <li> <code>DESC 'Fortress Modifiers AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY (</code>
 * <li> <code>ftModifier $</code>
 * <li> <code>ftModCode $</code>
 * <li> <code>ftModId ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h3>The Fortress Permission Entity Class is composite of 3 LDAP Schema object classes</h3>
 * The Permission entity extends a single OpenLDAP standard structural object class, 'organizationalRole' with
 * one extension structural class, ftOperation,  and two auxiliary object classes, ftProperties, ftMods.
 * The following 4 LDAP object classes will be mapped into this entity:
 * <h4>Permission Base - 'ftOperation' STRUCTURAL Object Class is assigned roles and/or users which grants permissions which can be later checked</h4>
 * using either 'checkAccess' or 'sessionPermissions APIs both methods that reside in the 'AccessMgrImpl' class.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass	( 1.3.6.1.4.1.38088.2.3</code>
 * <li> <code>NAME 'ftOperation'</code>
 * <li> <code>DESC 'Fortress Permission Operation Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftPermName $</code>
 * <li> <code>ftObjNm $ ftOpNm )</code>
 * <li> <code>MAY ( ftRoles $ ftUsers $</code>
 * <li> <code> ftObjId $ ftType) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>Permission Aux - ftProperties AUXILIARY Object Class is used to store optional client or otherwise custom name/value pairs on target entity.</h4>
 * <code>This aux object class can be used to store custom attributes.</code><br />
 * <code>The properties collections consist of name/value pairs and are not constrainted by Fortress.</code><br />
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.2</code>
 * <li> <code>NAME 'ftProperties'</code>
 * <li> <code>DESC 'Fortress Properties AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY ( ftProps ) ) </code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>Permission Aux - ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.</h4>
 * <ul>
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.4</code>
 * <li> <code>NAME 'ftMods'</code>
 * <li> <code>DESC 'Fortress Modifiers AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY (</code>
 * <li> <code>ftModifier $</code>
 * <li> <code>ftModCode $</code>
 * <li> <code>ftModId ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * This class is thread safe.
 * <p/>
 *
 * @author Shawn McKinney
 */
public final class PermDAO extends ApacheDsDataProvider implements us.jts.fortress.rbac.dao.PermDAO
{
    private static final String CLS_NM = PermDAO.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    /*
      *  *************************************************************************
      *  **  OpenAccessMgr PERMISSION STATICS
      *  ************************************************************************
      */
    private static final String TYPE = "ftType";
    private static final String PERM_OBJ_OBJECT_CLASS_NAME = "ftObject";
    private static final String PERM_OP_OBJECT_CLASS_NAME = "ftOperation";

    private static final String PERM_OBJ_OBJ_CLASS[] =
        {
            GlobalIds.TOP,
            "organizationalunit",
            PERM_OBJ_OBJECT_CLASS_NAME,
            GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME,
            GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };

    private static final String PERM_OP_OBJ_CLASS[] =
        {
            GlobalIds.TOP,
            "organizationalrole",
            PERM_OP_OBJECT_CLASS_NAME,
            GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME,
            GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };

    private static final String PERM_NAME = "ftPermName";
    private static final String POBJ_ID = "ftObjId";
    private static final String ROLES = "ftRoles";
    private static final String USERS = "ftUsers";
    private static final String[] PERMISSION_OP_ATRS =
        {
            GlobalIds.FT_IID, PERM_NAME, GlobalIds.POBJ_NAME, GlobalIds.POP_NAME, GlobalIds.DESC, GlobalIds.OU,
            POBJ_ID, TYPE, ROLES, USERS, GlobalIds.PROPS
    };

    private static final String[] PERMISION_OBJ_ATRS =
        {
            GlobalIds.FT_IID, GlobalIds.POBJ_NAME, GlobalIds.DESC, GlobalIds.OU, TYPE,
            GlobalIds.PROPS
    };


    /**
     * Default constructor is used by internal Fortress classes.
     */
    public PermDAO()
    {
    }


    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.CreateException
     *
     */
    public final PermObj createObject( PermObj entity ) throws CreateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity, entity.getContextId() );

        try
        {
            Entry entry = new DefaultEntry( dn );
            entry.add( GlobalIds.OBJECT_CLASS, PERM_OBJ_OBJ_CLASS );
            entry.add( GlobalIds.POBJ_NAME, entity.getObjectName() );

            // this will generatre a new random, unique id on this entity:
            entity.setInternalId();

            // create the rDN:
            entry.add( GlobalIds.FT_IID, entity.getInternalId() );

            // ou is required:
            entry.add( GlobalIds.OU, entity.getOu() );

            // description is optional:
            if ( VUtil.isNotNullOrEmpty( entity.getDescription() ) )
            {
                entry.add( GlobalIds.DESC, entity.getDescription() );
            }

            // type is optional:
            if ( VUtil.isNotNullOrEmpty( entity.getType() ) )
            {
                entry.add( TYPE, entity.getType() );
            }

            // props are optional as well:
            //if the props is null don't try to load these attributes
            if ( VUtil.isNotNullOrEmpty( entity.getProperties() ) )
            {
                loadProperties( entity.getProperties(), entry, GlobalIds.PROPS );
            }

            // create the new entry:
            Entry myEntry = new DefaultEntry( dn, entry );

            // now add the new entry to directory:
            ld = getAdminConnection();
            add( ld, myEntry, entity );
        }
        catch ( LdapException e )
        {
            String error = "createObject perm obj [" + entity.getObjectName() + "] caught LdapException="
                + e.getMessage();
            throw new CreateException( GlobalErrIds.PERM_ADD_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.UpdateException
     *
     */
    public final PermObj updateObj( PermObj entity )
        throws UpdateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity, entity.getContextId() );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();

            if ( VUtil.isNotNullOrEmpty( entity.getOu() ) )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE, GlobalIds.OU, entity.getOu() ) );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getDescription() ) )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE, GlobalIds.DESC, entity.getDescription() ) );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getType() ) )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE, TYPE, entity.getType() ) );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getProperties() ) )
            {
                loadProperties( entity.getProperties(), mods, GlobalIds.PROPS, true );
            }

            if ( mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, dn, mods, entity );
            }
        }
        catch ( LdapException e )
        {
            String error = "updateObj objectName [" + entity.getObjectName() + "] caught LdapException="
                + e.getMessage();
            throw new UpdateException( GlobalErrIds.PERM_UPDATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     * @param entity
     * @throws us.jts.fortress.RemoveException
     *
     */
    public final void deleteObj( PermObj entity ) throws RemoveException
    {
        LdapConnection ld = null;
        String dn = getDn( entity, entity.getContextId() );

        try
        {
            ld = getAdminConnection();
            deleteRecursive( ld, dn, entity );
        }
        catch ( LdapException e )
        {
            String error = "deleteObj objectName [" + entity.getObjectName() + "] caught LdapException="
                + e.getMessage();
            throw new RemoveException( GlobalErrIds.PERM_DELETE_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "deleteObj objectName [" + entity.getObjectName() + "] caught LdapException="
                + e.getMessage();
            throw new RemoveException( GlobalErrIds.PERM_DELETE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.CreateException
     *
     */
    public final Permission createOperation( Permission entity )
        throws CreateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity, entity.getContextId() );
        try
        {
            Entry attrs = new DefaultEntry();

            attrs.add( GlobalIds.OBJECT_CLASS, PERM_OP_OBJ_CLASS );
            attrs.add( GlobalIds.POP_NAME, entity.getOpName() );
            attrs.add( GlobalIds.POBJ_NAME, entity.getObjectName() );
            entity.setAbstractName( entity.getObjectName() + "." + entity.getOpName() );

            // this will generate a new random, unique id on this entity:
            entity.setInternalId();

            // create the internal id:
            attrs.add( GlobalIds.FT_IID, entity.getInternalId() );

            // the abstract name is the human readable identifier:
            attrs.add( PERM_NAME, entity.getAbstractName() );

            // organizational name requires CN attribute:
            attrs.add( GlobalIds.CN, entity.getAbstractName() );

            // objectid is optional:
            if ( VUtil.isNotNullOrEmpty( entity.getObjectId() ) )
            {
                attrs.add( POBJ_ID, entity.getObjectId() );
            }

            // type is optional:
            if ( VUtil.isNotNullOrEmpty( entity.getType() ) )
            {
                attrs.add( TYPE, entity.getType() );
            }

            // These are multi-valued attributes, use the util function to load:
            // These items are optional as well.  The utility function will return quietly if no items are loaded into collection:
            loadAttrs( entity.getRoles(), attrs, ROLES );
            loadAttrs( entity.getUsers(), attrs, USERS );

            // props are optional as well:
            //if the props is null don't try to load these attributes
            if ( VUtil.isNotNullOrEmpty( entity.getProperties() ) )
            {
                loadProperties( entity.getProperties(), attrs, GlobalIds.PROPS );
            }

            // create the new entry:
            Entry myEntry = new DefaultEntry( dn, attrs );

            // now add the new entry to directory:
            ld = getAdminConnection();
            add( ld, myEntry, entity );
        }
        catch ( LdapException e )
        {
            String error = "createOperation objectName [" + entity.getObjectName() + "] opName ["
                + entity.getOpName() + "] caught LdapException=" + e.getMessage();
            throw new CreateException( GlobalErrIds.PERM_ADD_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return entity;
    }


    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.UpdateException
     *
     */
    public final Permission updateOperation( Permission entity )
        throws UpdateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity, entity.getContextId() );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();

            if ( VUtil.isNotNullOrEmpty( entity.getAbstractName() ) )
            {
                // the abstract name is the human readable identifier:
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE, PERM_NAME, entity.getAbstractName() ) );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getType() ) )
            {

                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE, TYPE, entity.getType() ) );
            }

            // These are multi-valued attributes, use the util function to load:
            loadAttrs( entity.getRoles(), mods, ROLES );
            loadAttrs( entity.getUsers(), mods, USERS );
            loadProperties( entity.getProperties(), mods, GlobalIds.PROPS, true );

            if ( mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, dn, mods, entity );
            }
        }
        catch ( LdapException e )
        {
            String error = "updateOperation objectName [" + entity.getObjectName() + "] opName ["
                + entity.getOpName() + "] caught LdapException=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.PERM_UPDATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     * @param entity
     * @throws us.jts.fortress.RemoveException
     *
     */
    public final void deleteOperation( Permission entity ) throws RemoveException
    {
        LdapConnection ld = null;
        String dn = getOpRdn( entity.getOpName(), entity.getObjectId() ) + "," + GlobalIds.POBJ_NAME + "="
            + entity.getObjectName() + "," + getRootDn( entity.isAdmin(), entity.getContextId() );

        try
        {
            ld = getAdminConnection();
            deleteRecursive( ld, dn, entity );
        }
        catch ( LdapException e )
        {
            String error = "deleteOperation objectName [" + entity.getObjectName() + "] opName ["
                + entity.getOpName() + "] caught LdapException=" + e.getMessage();
            throw new RemoveException( GlobalErrIds.PERM_DELETE_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "deleteOperation objectName [" + entity.getObjectName() + "] opName ["
                + entity.getOpName() + "] caught LdapException=" + e.getMessage();
            throw new RemoveException( GlobalErrIds.PERM_DELETE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param pOp
     * @param role
     * @throws us.jts.fortress.UpdateException
     *
     * @throws us.jts.fortress.FinderException
     *
     */
    public final void grant( Permission pOp, Role role )
        throws UpdateException
    {
        LdapConnection ld = null;
        String dn = getDn( pOp, pOp.getContextId() );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();

            mods.add( new DefaultModification(
                ModificationOperation.ADD_ATTRIBUTE, ROLES, role.getName() ) );
            ld = getAdminConnection();
            modify( ld, dn, mods, pOp );
        }
        catch ( LdapAttributeInUseException e )
        {
            String warning = "grant perm object [" + pOp.getObjectName() + "] operation ["
                + pOp.getOpName() + "] role [" + role.getName() + "] assignment already exists, Fortress rc="
                + GlobalErrIds.PERM_ROLE_EXIST;
            throw new UpdateException( GlobalErrIds.PERM_ROLE_EXIST, warning );
        }
        catch ( LdapNoSuchObjectException e )
        {
            String warning = "grant perm object [" + pOp.getObjectName() + "] operation ["
                + pOp.getOpName() + "] role [" + role.getName() + "] perm not found, Fortress rc="
                + GlobalErrIds.PERM_OP_NOT_FOUND;
            throw new UpdateException( GlobalErrIds.PERM_OP_NOT_FOUND, warning );
        }
        catch ( LdapException e )
        {
            String error = "grant perm object [" + pOp.getObjectName() + "] operation ["
                + pOp.getOpName() + "] name [" + role.getName() + "]  caught LdapException="
                + e.getMessage();
            throw new UpdateException( GlobalErrIds.PERM_GRANT_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param pOp
     * @param role
     * @throws us.jts.fortress.UpdateException
     *
     * @throws us.jts.fortress.FinderException
     *
     */
    public final void revoke( Permission pOp, Role role )
        throws UpdateException, FinderException
    {
        LdapConnection ld = null;
        String dn = getDn( pOp, pOp.getContextId() );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();
            mods.add( new DefaultModification(
                ModificationOperation.REMOVE_ATTRIBUTE, ROLES, role.getName() ) );
            ld = getAdminConnection();
            modify( ld, dn, mods, pOp );
        }
        catch ( LdapNoSuchAttributeException e )
        {
            String warning = "revoke perm object [" + pOp.getObjectName() + "] operation ["
                + pOp.getOpName() + "] name [" + role.getName() + "] assignment does not exist.";
            throw new FinderException( GlobalErrIds.PERM_ROLE_NOT_EXIST, warning );
        }
        catch ( LdapException e )
        {
            String error = "revoke perm object [" + pOp.getObjectName() + "] operation ["
                + pOp.getOpName() + "] name [" + role.getName() + "] caught LdapException=" +
                e.getMessage();
            throw new UpdateException( GlobalErrIds.PERM_REVOKE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param pOp
     * @param user
     * @throws us.jts.fortress.UpdateException
     *
     * @throws us.jts.fortress.FinderException
     *
     */
    public final void grant( Permission pOp, User user )
        throws UpdateException
    {
        LdapConnection ld = null;
        String dn = getDn( pOp, pOp.getContextId() );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();
            mods.add( new DefaultModification(
                ModificationOperation.ADD_ATTRIBUTE, USERS, user.getUserId() ) );
            ld = getAdminConnection();
            modify( ld, dn, mods, pOp );
        }
        catch ( LdapAttributeInUseException e )
        {
            String warning = "grant perm object [" + pOp.getObjectName() + "] operation ["
                + pOp.getOpName() + "] userId [" + user.getUserId() + "] assignment already exists, Fortress rc="
                + GlobalErrIds.PERM_USER_EXIST;

            throw new UpdateException( GlobalErrIds.PERM_USER_EXIST, warning );
        }
        catch ( LdapNoSuchObjectException e )
        {
            String warning = "grant perm object [" + pOp.getObjectName() + "] operation ["
                + pOp.getOpName() + "] userId [" + user.getUserId() + "] perm not found, Fortress rc="
                + GlobalErrIds.PERM_OP_NOT_FOUND;
            throw new UpdateException( GlobalErrIds.PERM_OP_NOT_FOUND, warning );
        }
        catch ( LdapException e )
        {
            String error = "grant perm object [" + pOp.getObjectName() + "] operation ["
                + pOp.getOpName() + "] userId [" + user.getUserId() + "] caught LdapException="
                + e.getMessage();
            throw new UpdateException( GlobalErrIds.PERM_GRANT_USER_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param pOp
     * @param user
     * @throws us.jts.fortress.UpdateException
     *
     * @throws us.jts.fortress.FinderException
     *
     */
    public final void revoke( Permission pOp, User user )
        throws UpdateException, FinderException
    {
        LdapConnection ld = null;
        String dn = getDn( pOp, pOp.getContextId() );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();

            mods.add( new DefaultModification( ModificationOperation.REMOVE_ATTRIBUTE,
                USERS, user.getUserId() ) );
            ld = getAdminConnection();
            modify( ld, dn, mods, pOp );
        }
        catch ( LdapNoSuchAttributeException e )
        {
            String warning = "revoke perm object [" + pOp.getObjectName() + "] operation ["
                + pOp.getOpName() + "] userId [" + user.getUserId() + "] assignment does not exist.";
            throw new FinderException( GlobalErrIds.PERM_USER_NOT_EXIST, warning );
        }
        catch ( LdapException e )
        {
            String error = "revoke perm object [" + pOp.getObjectName() + "] operation ["
                + pOp.getOpName() + "] userId [" + user.getUserId() + "] caught LdapException="
                + e.getMessage();
            throw new UpdateException( GlobalErrIds.PERM_REVOKE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param permission
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    public final Permission getPerm( Permission permission )
        throws FinderException
    {
        Permission entity = null;
        LdapConnection ld = null;
        String dn = getOpRdn( permission.getOpName(), permission.getObjectId() ) + "," + GlobalIds.POBJ_NAME + "="
            + permission.getObjectName() + "," + getRootDn( permission.isAdmin(), permission.getContextId() );

        try
        {
            ld = getAdminConnection();
            Entry findEntry = read( ld, dn, PERMISSION_OP_ATRS );
            entity = unloadPopLdapEntry( findEntry, 0 );

            if ( entity == null )
            {
                String warning = "getPerm no entry found dn [" + dn + "]";
                throw new FinderException( GlobalErrIds.PERM_OP_NOT_FOUND, warning );
            }
        }
        catch ( LdapNoSuchObjectException e )
        {
            String warning = "getPerm Op COULD NOT FIND ENTRY for dn [" + dn + "]";
            throw new FinderException( GlobalErrIds.PERM_OP_NOT_FOUND, warning );
        }
        catch ( LdapException e )
        {
            String error = "getUser [" + dn + "] caught LdapException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_READ_OP_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return entity;
    }


    /**
     * @param permObj
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    public final PermObj getPerm( PermObj permObj )
        throws FinderException
    {
        PermObj entity = null;
        LdapConnection ld = null;
        String dn = GlobalIds.POBJ_NAME + "=" + permObj.getObjectName() + ","
            + getRootDn( permObj.isAdmin(), permObj.getContextId() );

        try
        {
            ld = getAdminConnection();
            Entry findEntry = read( ld, dn, PERMISION_OBJ_ATRS );
            entity = unloadPobjLdapEntry( findEntry, 0 );

            if ( entity == null )
            {
                String warning = "getPerm Obj no entry found dn [" + dn + "]";
                throw new FinderException( GlobalErrIds.PERM_OBJ_NOT_FOUND, warning );
            }
        }
        catch ( LdapNoSuchObjectException e )
        {
            String warning = "getPerm Obj COULD NOT FIND ENTRY for dn [" + dn + "]";
            throw new FinderException( GlobalErrIds.PERM_OBJ_NOT_FOUND, warning );
        }
        catch ( LdapException e )
        {
            String error = "getPerm Obj dn [" + dn + "] caught LdapException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_READ_OBJ_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     * This method performs fortress authorization using data passed in (session) and stored on ldap server (permission).  It has been recently changed to use ldap compare operations in order to trigger slapd access log updates in directory.
     * It performs ldap operations:  read and (optionally) compare.  The first is to pull back the permission to see if user has access or not.  The second is to trigger audit
     * record storage on ldap server but can be disabled.
     *
     * @param session contains {@link Session#getUserId()}, for rbac check {@link us.jts.fortress.rbac.Session#getRoles()}, for arbac check: {@link us.jts.fortress.rbac.Session#getAdminRoles()}.
     * @param inPerm  must contain required attributes {@link Permission#objectName} and {@link Permission#opName}.  {@link Permission#objectId} is optional.
     * @return boolean containing result of check.
     * @throws us.jts.fortress.FinderException
     *          In the event system error occurs looking up data on ldap server.
     */
    public final boolean checkPermission( Session session, Permission inPerm ) throws FinderException
    {
        boolean isAuthZd = false;
        LdapConnection ld = null;
        String dn = getOpRdn( inPerm.getOpName(), inPerm.getObjectId() ) + "," + GlobalIds.POBJ_NAME + "="
            + inPerm.getObjectName() + "," + getRootDn( inPerm.isAdmin(), inPerm.getContextId() );

        try
        {
            // Use unauthenticated connection because we want to assert the end user identity onto ldap hop:
            ld = getAdminConnection();

            // LDAP Operation #1: Read the targeted permission from ldap server
            //LDAPEntry entry = read(ld, dn, PERMISSION_OP_ATRS, session.getUser().getDn());
            Entry entry = read( ld, dn, PERMISSION_OP_ATRS );

            // load the permission entity with data retrieved from the permission node:
            Permission outPerm = unloadPopLdapEntry( entry, 0 );

            // The admin flag will be set to 'true' if this is an administrative permission:
            outPerm.setAdmin( inPerm.isAdmin() );

            // Pass the tenant id along:
            outPerm.setContextId( inPerm.getContextId() );

            // The objective of these next steps is to evaluate the outcome of authorization attempt and trigger a write to slapd access logger containing the result.
            // The objectClass triggered by slapd access log write for upcoming ldap op is 'auditCompare'.
            // Set this attribute either with actual operation name that will succeed compare (for authZ success) or bogus value which will fail compare (for authZ failure):
            String attributeValue;

            // This method determines if the user is authorized for this permission:
            isAuthZd = isAuthorized( session, outPerm );

            // This is done to leave an audit trail in ldap server log:
            if ( isAuthZd )
            {
                // Yes, set the operation name onto this attribute for storage into audit trail:
                attributeValue = outPerm.getOpName();
            }
            else
            {
                // No, set a simple error message onto this attribute for storage into audit trail:
                attributeValue = "AuthZ Failed";
            }

            // There is a switch in fortress config to disable audit ops like this one.
            // But if used the compare method will use OpenLDAP's Proxy Authorization Control to assert identity of end user onto connection.
            // LDAP Operation #2: Compare.
            addAuthZAudit( ld, dn, session.getUser().getDn(), attributeValue );
        }
        catch ( LdapException e )
        {
            if ( !( e instanceof LdapNoSuchObjectException ) )
            {
                String error = "checkPermission caught LdapException=" + e.getMessage();
                throw new FinderException( GlobalErrIds.PERM_READ_OP_FAILED, error, e );
            }

            // There is a switch in fortress config to disable the audit ops.
            addAuthZAudit( ld, dn, session.getUser().getDn(), "AuthZ Invalid" );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return isAuthZd;
    }


    /**
     * Perform LDAP compare operation here to associate audit record with user authorization event.
     *
     * @param ld this method expects the ldap connection to be good
     * @param permDn contains distinguished name of the permission object.
     * @param userDn contains the distinguished name of the user object.
     * @param attributeValue string value will be associated with the 'audit' record stored in ldap.
     * @throws FinderException in the event ldap system exception occurs.
     */
    private void addAuthZAudit( LdapConnection ld, String permDn, String userDn, String attributeValue )
        throws FinderException
    {
        // Audit can be turned off here with fortress config param: 'enable.audit=false'
        if ( GlobalIds.IS_AUDIT && GlobalIds.IS_OPENLDAP )
        {
            try
            {
                // The compare method uses OpenLDAP's Proxy Authorization Control to assert identity of end user onto connection:
                // LDAP Operation #2: Compare:
                compareNode( ld, permDn, userDn, new DefaultAttribute( GlobalIds.POP_NAME, attributeValue ) );
            }
            catch ( UnsupportedEncodingException ee )
            {
                String error = "addAuthZAudit caught UnsupportedEncodingException=" + ee.getMessage();
                throw new FinderException( GlobalErrIds.PERM_COMPARE_OP_FAILED, error, ee );
            }
            catch ( LdapException e )
            {
                if ( !( e instanceof LdapNoSuchObjectException ) )
                {
                    String error = "addAuthZAudit caught LdapException=" + e.getMessage();
                    throw new FinderException( GlobalErrIds.PERM_COMPARE_OP_FAILED, error, e );
                }
            }
        }
    }


    /**
     * This function will first compare the userId from the session object with the list of users attached to permission object.
     * If match does not occur there, determine if there is a match between the authorized roles of user with roles attached to permission object.
     * For this use {@link us.jts.fortress.rbac.Permission#isAdmin()} to determine if admin permissions or normal permissions have been passed in by caller.
     *
     * @param session contains the {@link us.jts.fortress.rbac.Session#getUserId()},{@link Session#getRoles()} or {@link us.jts.fortress.rbac.Session#getAdminRoles()}.
     * @param permission contains {@link us.jts.fortress.rbac.Permission#getUsers()} and {@link Permission#getRoles()}.
     * @return binary result.
     */
    private boolean isAuthorized( Session session, Permission permission )
    {
        boolean result = false;
        Set<String> userIds = permission.getUsers();

        if ( VUtil.isNotNullOrEmpty( userIds ) && userIds.contains( session.getUserId() ) )
        {
            // user is assigned directly to this permission, no need to look further.
            return true;
        }

        Set<String> roles = permission.getRoles();

        if ( VUtil.isNotNullOrEmpty( roles ) )
        {
            if ( permission.isAdmin() )
            {
                // ARBAC Permission check include's User's inherited admin roles:
                Set<String> activatedRoles = AdminRoleUtil.getInheritedRoles( session.getAdminRoles(),
                    permission.getContextId() );

                for ( String role : roles )
                {
                    // This is case insensitive op determines if user has matching admin role to the admin permission::
                    if ( activatedRoles.contains( role ) )
                    {
                        result = true;
                        break;
                    }
                }
            }
            else
            {
                // RBAC Permission check include's User's inherited roles:
                Set<String> activatedRoles = RoleUtil.getInheritedRoles( session.getRoles(), permission.getContextId() );

                for ( String role : roles )
                {
                    // This is case insensitive op determines if user has matching role:
                    if ( activatedRoles.contains( role ) )
                    {
                        result = true;
                        break;
                    }
                }
            }
        }

        return result;
    }


    /**
     * @param le
     * @param sequence
     * @return
     * @throws LdapInvalidAttributeValueException 
     * @throws LdapException
     */
    private Permission unloadPopLdapEntry( Entry le, long sequence ) throws LdapInvalidAttributeValueException
    {
        Permission entity = new ObjectFactory().createPermission();
        entity.setSequenceId( sequence );
        entity.setAbstractName( getAttribute( le, PERM_NAME ) );
        entity.setObjectName( getAttribute( le, GlobalIds.POBJ_NAME ) );
        entity.setObjectId( getAttribute( le, POBJ_ID ) );
        entity.setOpName( getAttribute( le, GlobalIds.POP_NAME ) );
        entity.setInternalId( getAttribute( le, GlobalIds.FT_IID ) );
        entity.setRoles( getAttributeSet( le, ROLES ) );
        entity.setUsers( getAttributeSet( le, USERS ) );
        entity.setType( getAttribute( le, TYPE ) );
        entity.addProperties( AttrHelper.getProperties( getAttributes( le, GlobalIds.PROPS ) ) );
        return entity;
    }


    /**
     * @param le
     * @param sequence
     * @return
     * @throws LdapInvalidAttributeValueException 
     * @throws LdapException
     */
    private PermObj unloadPobjLdapEntry( Entry le, long sequence ) throws LdapInvalidAttributeValueException
    {
        PermObj entity = new ObjectFactory().createPermObj();
        entity.setSequenceId( sequence );
        entity.setObjectName( getAttribute( le, GlobalIds.POBJ_NAME ) );
        entity.setOu( getAttribute( le, GlobalIds.OU ) );
        entity.setDn( le.getDn().getName() );
        entity.setInternalId( getAttribute( le, GlobalIds.FT_IID ) );
        entity.setType( getAttribute( le, TYPE ) );
        entity.setDescription( getAttribute( le, GlobalIds.DESC ) );
        entity.addProperties( AttrHelper.getProperties( getAttributes( le, GlobalIds.PROPS ) ) );
        return entity;
    }


    /**
     * @param permission
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    public final List<Permission> findPermissions( Permission permission )
        throws FinderException
    {
        List<Permission> permList = new ArrayList<>();
        LdapConnection ld = null;
        String permRoot = getRootDn( permission.isAdmin(), permission.getContextId() );

        try
        {
            String permObjVal = encodeSafeText( permission.getObjectName(), GlobalIds.PERM_LEN );
            String permOpVal = encodeSafeText( permission.getOpName(), GlobalIds.PERM_LEN );
            String filter = GlobalIds.FILTER_PREFIX + PERM_OP_OBJECT_CLASS_NAME + ")("
                + GlobalIds.POBJ_NAME + "=" + permObjVal + "*)("
                + GlobalIds.POP_NAME + "=" + permOpVal + "*))";

            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, permRoot,
                SearchScope.SUBTREE, filter, PERMISSION_OP_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;

            while ( searchResults.next() )
            {
                permList.add( unloadPopLdapEntry( searchResults.getEntry(), sequence++ ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "findPermissions caught LdapException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "findPermissions caught LdapException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return permList;
    }


    /**
     * @param permObj
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    public final List<PermObj> findPermissions( PermObj permObj )
        throws FinderException
    {
        List<PermObj> permList = new ArrayList<>();
        LdapConnection ld = null;
        String permRoot = getRootDn( permObj.isAdmin(), permObj.getContextId() );

        try
        {
            String permObjVal = encodeSafeText( permObj.getObjectName(), GlobalIds.PERM_LEN );
            String filter = GlobalIds.FILTER_PREFIX + PERM_OBJ_OBJECT_CLASS_NAME + ")("
                + GlobalIds.POBJ_NAME + "=" + permObjVal + "*))";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, permRoot,
                SearchScope.SUBTREE, filter, PERMISION_OBJ_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;

            while ( searchResults.next() )
            {
                permList.add( unloadPobjLdapEntry( searchResults.getEntry(), sequence++ ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "findPermissions caught LdapException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "findPermissions caught LdapException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return permList;
    }


    /**
     * @param ou
     * @return
     * @throws FinderException
     */
    public final List<PermObj> findPermissions( OrgUnit ou, boolean limitSize ) throws FinderException
    {
        List<PermObj> permList = new ArrayList<>();
        LdapConnection ld = null;
        String permRoot = getRootDn( ou.getContextId(), GlobalIds.PERM_ROOT );

        try
        {
            String ouVal = encodeSafeText( ou.getName(), GlobalIds.OU_LEN );
            String filter = GlobalIds.FILTER_PREFIX + PERM_OBJ_OBJECT_CLASS_NAME + ")("
                + GlobalIds.OU + "=" + ouVal + "*))";
            int maxLimit;

            if ( limitSize )
            {
                maxLimit = 10;
            }
            else
            {
                maxLimit = 0;
            }

            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, permRoot,
                SearchScope.SUBTREE, filter, PERMISION_OBJ_ATRS, false, GlobalIds.BATCH_SIZE, maxLimit );
            long sequence = 0;

            while ( searchResults.next() )
            {
                permList.add( unloadPobjLdapEntry( searchResults.getEntry(), sequence++ ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "findPermissions caught LdapException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "findPermissions caught LdapException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return permList;
    }


    /**
     * @param role
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    public final List<Permission> findPermissions( Role role ) throws FinderException
    {
        List<Permission> permList = new ArrayList<>();
        LdapConnection ld = null;
        String permRoot;

        if ( role.getClass().equals( AdminRole.class ) )
        {
            permRoot = getRootDn( role.getContextId(), GlobalIds.ADMIN_PERM_ROOT );
        }
        else
        {
            permRoot = getRootDn( role.getContextId(), GlobalIds.PERM_ROOT );
        }

        try
        {
            String roleVal = encodeSafeText( role.getName(), GlobalIds.ROLE_LEN );
            String filter = GlobalIds.FILTER_PREFIX + PERM_OP_OBJECT_CLASS_NAME + ")(";
            Set<String> roles;

            if ( role.getClass().equals( AdminRole.class ) )
            {
                roles = AdminRoleUtil.getAscendants( role.getName(), role.getContextId() );
            }
            else
            {
                roles = RoleUtil.getAscendants( role.getName(), role.getContextId() );
            }

            if ( VUtil.isNotNullOrEmpty( roles ) )
            {
                filter += "|(" + ROLES + "=" + roleVal + ")";

                for ( String uRole : roles )
                {
                    filter += "(" + ROLES + "=" + uRole + ")";
                }

                filter += ")";
            }
            else
            {
                filter += ROLES + "=" + roleVal + ")";
            }

            filter += ")";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, permRoot,
                SearchScope.SUBTREE, filter, PERMISSION_OP_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;

            while ( searchResults.next() )
            {
                permList.add( unloadPopLdapEntry( searchResults.getEntry(), sequence++ ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "findPermissions caught LdapException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_ROLE_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "findPermissions caught LdapException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_ROLE_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return permList;
    }


    /**
     * @param user
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    public final List<Permission> findPermissions( User user ) throws FinderException
    {
        List<Permission> permList = new ArrayList<>();
        LdapConnection ld = null;
        String permRoot = getRootDn( user.getContextId(), GlobalIds.PERM_ROOT );

        try
        {
            String filter = GlobalIds.FILTER_PREFIX + PERM_OP_OBJECT_CLASS_NAME + ")(|";
            Set<String> roles = RoleUtil.getInheritedRoles( user.getRoles(), user.getContextId() );

            if ( VUtil.isNotNullOrEmpty( roles ) )
            {
                for ( String uRole : roles )
                {
                    filter += "(" + ROLES + "=" + uRole + ")";
                }
            }

            filter += "(" + USERS + "=" + user.getUserId() + ")))";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, permRoot,
                SearchScope.SUBTREE, filter, PERMISSION_OP_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;

            while ( searchResults.next() )
            {
                permList.add( unloadPopLdapEntry( searchResults.getEntry(), sequence++ ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "findPermissions user [" + user.getUserId()
                + "] caught LdapException in PermDAO.findPermissions=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_USER_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "findPermissions user [" + user.getUserId()
                + "] caught LdapException in PermDAO.findPermissions=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_USER_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return permList;
    }


    /**
     * @param user
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    public final List<Permission> findUserPermissions( User user ) throws FinderException
    {
        List<Permission> permList = new ArrayList<>();
        LdapConnection ld = null;
        String permRoot = getRootDn( user.getContextId(), GlobalIds.PERM_ROOT );

        try
        {
            String filter = GlobalIds.FILTER_PREFIX + PERM_OP_OBJECT_CLASS_NAME + ")";
            filter += "(" + USERS + "=" + user.getUserId() + "))";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, permRoot,
                SearchScope.SUBTREE, filter, PERMISSION_OP_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;

            while ( searchResults.next() )
            {
                permList.add( unloadPopLdapEntry( searchResults.getEntry(), sequence++ ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "findUserPermissions user [" + user.getUserId()
                + "] caught LdapException in PermDAO.findPermissions=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_USER_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "findUserPermissions user [" + user.getUserId()
                + "] caught LdapException in PermDAO.findPermissions=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_USER_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return permList;
    }


    /**
     * @param session
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    public final List<Permission> findPermissions( Session session ) throws FinderException
    {
        List<Permission> permList = new ArrayList<>();
        LdapConnection ld = null;
        String permRoot = getRootDn( session.getContextId(), GlobalIds.PERM_ROOT );

        try
        {
            String filter = GlobalIds.FILTER_PREFIX + PERM_OP_OBJECT_CLASS_NAME + ")(|";
            filter += "(" + USERS + "=" + session.getUserId() + ")";
            Set<String> roles = RoleUtil.getInheritedRoles( session.getRoles(), session.getContextId() );

            if ( VUtil.isNotNullOrEmpty( roles ) )
            {
                for ( String uRole : roles )
                {
                    filter += "(" + ROLES + "=" + uRole + ")";
                }
            }

            filter += "))";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, permRoot,
                SearchScope.SUBTREE, filter, PERMISSION_OP_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;

            while ( searchResults.next() )
            {
                permList.add( unloadPopLdapEntry( searchResults.getEntry(), sequence++ ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "findPermissions user [" + session.getUserId()
                + "] caught LdapException in PermDAO.findPermissions=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_SESS_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "findPermissions user [" + session.getUserId()
                + "] caught LdapException in PermDAO.findPermissions=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PERM_SESS_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return permList;
    }


    /**
     * @param opName
     * @param objId
     * @return
     */
    static String getOpRdn( String opName, String objId )
    {
        String rDn;

        if ( objId != null && objId.length() > 0 )
        {
            rDn = GlobalIds.POP_NAME + "=" + opName + "+" + POBJ_ID + "=" + objId;
        }
        else
        {
            rDn = GlobalIds.POP_NAME + "=" + opName;
        }

        return rDn;
    }


    private String getDn( Permission pOp, String contextId )
    {
        return getOpRdn( pOp.getOpName(), pOp.getObjectId() ) + "," + GlobalIds.POBJ_NAME + "=" + pOp.getObjectName()
            + "," + getRootDn( pOp.isAdmin(), contextId );
    }


    private String getDn( PermObj pObj, String contextId )
    {
        return GlobalIds.POBJ_NAME + "=" + pObj.getObjectName() + "," + getRootDn( pObj.isAdmin(), contextId );
    }


    private String getRootDn( boolean isAdmin, String contextId )
    {
        String dn;

        if ( isAdmin )
        {
            dn = getRootDn( contextId, GlobalIds.ADMIN_PERM_ROOT );
        }
        else
        {
            dn = getRootDn( contextId, GlobalIds.PERM_ROOT );
        }

        return dn;
    }
}