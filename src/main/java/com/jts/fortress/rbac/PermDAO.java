/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.CreateException;
import com.jts.fortress.ObjectFactory;
import com.jts.fortress.RemoveException;
import com.jts.fortress.arbac.AdminRoleUtil;
import com.jts.fortress.arbac.OrgUnit;
import com.jts.fortress.configuration.Config;
import com.jts.fortress.FinderException;
import com.jts.fortress.UpdateException;
import com.jts.fortress.ldap.DaoUtil;
import com.jts.fortress.ldap.PoolMgr;
import com.jts.fortress.arbac.AdminRole;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.constants.GlobalErrIds;

import com.jts.fortress.util.attr.AttrHelper;
import com.jts.fortress.util.attr.VUtil;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttribute;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModification;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchResults;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
 * <p/>
 *
 * @author Shawn McKinney
 * @created September 29, 2009
 */
public final class PermDAO
{

    private static final String CLS_NM = PermDAO.class.getName();
    /*
      *  *************************************************************************
      *  **  OpenAccessMgr PERMISSION STATICS
      *  ************************************************************************
      */
    private final static String TYPE = "ftType";
    private final static String PERM_OBJ_OBJECT_CLASS_NAME = "ftObject";
    private final static String PERM_OP_OBJECT_CLASS_NAME = "ftOperation";

    private final static String PERM_OBJ_OBJ_CLASS[] = {
        GlobalIds.TOP, "organizationalunit", PERM_OBJ_OBJECT_CLASS_NAME, GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };

    private final static String PERM_OP_OBJ_CLASS[] = {
        GlobalIds.TOP, "organizationalrole", PERM_OP_OBJECT_CLASS_NAME, GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };

    private static final String PERM_NAME = "ftPermName";
    private static final String POBJ_ID = "ftObjId";
    private static final String POP_NAME = "ftOpNm";
    private static final String ROLES = "ftRoles";
    private static final String USERS = "ftUsers";
    private static final String[] PERMISSION_OP_ATRS = {
        GlobalIds.FT_IID, PERM_NAME, GlobalIds.POBJ_NAME, POP_NAME, GlobalIds.DESC, GlobalIds.OU,
        POBJ_ID, TYPE, ROLES, USERS, GlobalIds.PROPS
    };

    private static final String[] PERMISION_OBJ_ATRS = {
        GlobalIds.FT_IID, GlobalIds.POBJ_NAME, GlobalIds.DESC, GlobalIds.OU, TYPE,
        GlobalIds.PROPS
    };

    /**
     * Default constructor is used by internal Fortress classes.
     */
    PermDAO()
    {
    }

    /**
     * @param entity
     * @return
     * @throws com.jts.fortress.CreateException
     *
     */
    PermObj createObject(PermObj entity)
        throws CreateException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add(DaoUtil.createAttributes(GlobalIds.OBJECT_CLASS, PERM_OBJ_OBJ_CLASS));
            attrs.add(DaoUtil.createAttribute(GlobalIds.POBJ_NAME, entity.getObjectName()));

            // this will generatre a new random, unique id on this entity:
            entity.setInternalId();
            // create the rDN:
            attrs.add(DaoUtil.createAttribute(GlobalIds.FT_IID, entity.getInternalId()));
            // ou is required:
            attrs.add(DaoUtil.createAttribute(GlobalIds.OU, entity.getOu()));

            // description is optional:
            if (VUtil.isNotNullOrEmpty(entity.getDescription()))
            {
                attrs.add(DaoUtil.createAttribute(GlobalIds.DESC, entity.getDescription()));
            }
            // type is optional:
            if (VUtil.isNotNullOrEmpty(entity.getType()))
            {
                attrs.add(DaoUtil.createAttribute(TYPE, entity.getType()));
            }
            // props are optional as well:
            //if the props is null don't try to load these attributes
            if (VUtil.isNotNullOrEmpty(entity.getProperties()))
            {
                DaoUtil.loadProperties(entity.getProperties(), attrs, GlobalIds.PROPS);
            }

            // create the new entry:
            LDAPEntry myEntry = new LDAPEntry(dn, attrs);

            // now add the new entry to directory:
            DaoUtil.add(ld, myEntry, entity);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".createObject perm obj [" + entity.getObjectName() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new CreateException(GlobalErrIds.PERM_ADD_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param entity
     * @return
     * @throws com.jts.fortress.UpdateException
     *
     */
    PermObj updateObj(PermObj entity)
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            if (VUtil.isNotNullOrEmpty(entity.getOu()))
            {
                LDAPAttribute ou = new LDAPAttribute(GlobalIds.OU, entity.getOu());
                mods.add(LDAPModification.REPLACE, ou);
            }
            if (VUtil.isNotNullOrEmpty(entity.getDescription()))
            {
                LDAPAttribute desc = new LDAPAttribute(GlobalIds.DESC,
                    entity.getDescription());
                mods.add(LDAPModification.REPLACE, desc);
            }
            if (VUtil.isNotNullOrEmpty(entity.getType()))
            {
                LDAPAttribute type = new LDAPAttribute(TYPE,
                    entity.getType());
                mods.add(LDAPModification.REPLACE, type);
            }
            if (VUtil.isNotNullOrEmpty(entity.getProperties()))
            {
                DaoUtil.loadProperties(entity.getProperties(), mods, GlobalIds.PROPS, true);
            }
            if (mods.size() > 0)
            {
                DaoUtil.modify(ld, dn, mods, entity);
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".updateObj objectName [" + entity.getObjectName() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException(GlobalErrIds.PERM_UPDATE_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param entity
     * @throws com.jts.fortress.RemoveException
     *
     */
    void deleteObj(PermObj entity)
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            DaoUtil.deleteRecursive(ld, dn, entity);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".deleteObj objectName [" + entity.getObjectName() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new RemoveException(GlobalErrIds.PERM_DELETE_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
    }


    /**
     * @param entity
     * @return
     * @throws com.jts.fortress.CreateException
     *
     */
    Permission createOperation(Permission entity)
        throws CreateException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add(DaoUtil.createAttributes(GlobalIds.OBJECT_CLASS, PERM_OP_OBJ_CLASS));
            attrs.add(DaoUtil.createAttribute(POP_NAME, entity.getOpName()));
            attrs.add(DaoUtil.createAttribute(GlobalIds.POBJ_NAME, entity.getObjectName()));
            entity.setAbstractName(entity.getObjectName() + "." + entity.getOpName());

            // this will generatre a new random, unique id on this entity:
            entity.setInternalId();
            // create the internal id:
            attrs.add(DaoUtil.createAttribute(GlobalIds.FT_IID, entity.getInternalId()));
            // the abstract name is the human readable identifier:
            attrs.add(DaoUtil.createAttribute(PERM_NAME, entity.getAbstractName()));
            // organizational name requires CN attribute:
            attrs.add(DaoUtil.createAttribute(GlobalIds.CN, entity.getAbstractName()));

            // objectid is optional:
            if (VUtil.isNotNullOrEmpty(entity.getObjectId()))
            {
                attrs.add(DaoUtil.createAttribute(POBJ_ID, entity.getObjectId()));
            }
            // type is optional:
            if (VUtil.isNotNullOrEmpty(entity.getType()))
            {
                attrs.add(DaoUtil.createAttribute(TYPE, entity.getType()));
            }
            // These are multi-valued attributes, use the util function to load:
            // These items are optional as well.  The utility function will return quietly if no items are loaded into collection:
            DaoUtil.loadAttrs(entity.getRoles(), attrs, ROLES);
            DaoUtil.loadAttrs(entity.getUsers(), attrs, USERS);

            // props are optional as well:
            //if the props is null don't try to load these attributes
            if (VUtil.isNotNullOrEmpty(entity.getProperties()))
            {
                DaoUtil.loadProperties(entity.getProperties(), attrs, GlobalIds.PROPS);
            }
            // create the new entry:
            LDAPEntry myEntry = new LDAPEntry(dn, attrs);
            // now add the new entry to directory:
            DaoUtil.add(ld, myEntry, entity);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".createOperation objectName [" + entity.getObjectName() + "] opName [" + entity.getOpName() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new CreateException(GlobalErrIds.PERM_ADD_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param entity
     * @return
     * @throws com.jts.fortress.UpdateException
     *
     */
    Permission updateOperation(Permission entity)
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            if (VUtil.isNotNullOrEmpty(entity.getAbstractName()))
            {
                // the abstract name is the human readable identifier:
                LDAPAttribute abstractName = new LDAPAttribute(PERM_NAME,
                    entity.getAbstractName());
                mods.add(LDAPModification.REPLACE, abstractName);
            }
            if (VUtil.isNotNullOrEmpty(entity.getType()))
            {
                LDAPAttribute type = new LDAPAttribute(TYPE,
                    entity.getType());
                mods.add(LDAPModification.REPLACE, type);
            }

            // These are multi-valued attributes, use the util function to load:
            DaoUtil.loadAttrs(entity.getRoles(), mods, ROLES);
            DaoUtil.loadAttrs(entity.getUsers(), mods, USERS);
            DaoUtil.loadProperties(entity.getProperties(), mods, GlobalIds.PROPS, true);
            if (mods.size() > 0)
            {
                DaoUtil.modify(ld, dn, mods, entity);
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".updateOperation objectName [" + entity.getObjectName() + "] opName [" + entity.getOpName() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException(GlobalErrIds.PERM_UPDATE_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param entity
     * @throws com.jts.fortress.RemoveException
     *
     */
    void deleteOperation(Permission entity)
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = getOpRdn(entity.getOpName(), entity.getObjectId()) + "," + GlobalIds.POBJ_NAME + "=" + entity.getObjectName() + "," + getRootDn(entity.isAdmin());
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            DaoUtil.deleteRecursive(ld, dn, entity);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".deleteOperation objectName [" + entity.getObjectName() + "] opName [" + entity.getOpName() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new RemoveException(GlobalErrIds.PERM_DELETE_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
    }


    /**
     * @param pOp
     * @param role
     * @throws com.jts.fortress.UpdateException
     *
     * @throws com.jts.fortress.FinderException
     *
     */
    void grant(Permission pOp, Role role)
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn(pOp);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute attr = new LDAPAttribute(ROLES, role.getName());
            mods.add(LDAPModification.ADD, attr);
            DaoUtil.modify(ld, dn, mods, pOp);
        }
        catch (LDAPException e)
        {
            if (e.getLDAPResultCode() == LDAPException.ATTRIBUTE_OR_VALUE_EXISTS)
            {
                String warning = CLS_NM + ".grant perm object [" + pOp.getObjectName() + "] operation [" + pOp.getOpName() + "] role [" + role.getName() + "] assignment already exists, Fortress errCode=" + GlobalErrIds.PERM_ROLE_EXIST;
                throw new UpdateException(GlobalErrIds.PERM_ROLE_EXIST, warning);
            }
            else if (e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT)
            {
                String warning = CLS_NM + ".grant perm object [" + pOp.getObjectName() + "] operation [" + pOp.getOpName() + "] role [" + role.getName() + "] perm not found, Fortress errCode=" + GlobalErrIds.PERM_OP_NOT_FOUND;
                throw new UpdateException(GlobalErrIds.PERM_OP_NOT_FOUND, warning);
            }
            else
            {
                String error = CLS_NM + ".grant perm object [" + pOp.getObjectName() + "] operation [" + pOp.getOpName() + "] name [" + role.getName() + "]  caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
                throw new UpdateException(GlobalErrIds.PERM_GRANT_FAILED, error, e);
            }
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
    }


    /**
     * @param pOp
     * @param role
     * @throws com.jts.fortress.UpdateException
     *
     * @throws com.jts.fortress.FinderException
     *
     */
    void revoke(Permission pOp, Role role)
        throws UpdateException, FinderException
    {
        LDAPConnection ld = null;
        String dn = getDn(pOp);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute attr = new LDAPAttribute(ROLES, role.getName());
            mods.add(LDAPModification.DELETE, attr);
            DaoUtil.modify(ld, dn, mods, pOp);
        }
        catch (LDAPException e)
        {
            if (e.getLDAPResultCode() == LDAPException.NO_SUCH_ATTRIBUTE)
            {
                String warning = CLS_NM + ".revoke perm object [" + pOp.getObjectName() + "] operation [" + pOp.getOpName() + "] name [" + role.getName() + "] assignment does not exist.";
                throw new FinderException(GlobalErrIds.PERM_ROLE_NOT_EXIST, warning);
            }
            else
            {
                String error = CLS_NM + ".revoke perm object [" + pOp.getObjectName() + "] operation [" + pOp.getOpName() + "] name [" + role.getName() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
                throw new UpdateException(GlobalErrIds.PERM_REVOKE_FAILED, error, e);
            }
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
    }


    /**
     * @param pOp
     * @param user
     * @throws com.jts.fortress.UpdateException
     *
     * @throws com.jts.fortress.FinderException
     *
     */
    void grant(Permission pOp, User user)
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn(pOp);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute attr = new LDAPAttribute(USERS, user.getUserId());
            mods.add(LDAPModification.ADD, attr);
            DaoUtil.modify(ld, dn, mods, pOp);
        }
        catch (LDAPException e)
        {
            if (e.getLDAPResultCode() == LDAPException.ATTRIBUTE_OR_VALUE_EXISTS)
            {
                String warning = CLS_NM + ".grant perm object [" + pOp.getObjectName() + "] operation [" + pOp.getOpName() + "] userId [" + user.getUserId() + "] assignment already exists, Fortress errCode=" + GlobalErrIds.PERM_USER_EXIST;
                throw new UpdateException(GlobalErrIds.PERM_USER_EXIST, warning);
            }
            else if (e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT)
            {
                String warning = CLS_NM + ".grant perm object [" + pOp.getObjectName() + "] operation [" + pOp.getOpName() + "] userId [" + user.getUserId() + "] perm not found, Fortress errCode=" + GlobalErrIds.PERM_OP_NOT_FOUND;
                throw new UpdateException(GlobalErrIds.PERM_OP_NOT_FOUND, warning);
            }
            else
            {
                String error = CLS_NM + ".grant perm object [" + pOp.getObjectName() + "] operation [" + pOp.getOpName() + "] userId [" + user.getUserId() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
                throw new UpdateException(GlobalErrIds.PERM_GRANT_USER_FAILED, error, e);
            }
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
    }


    /**
     * @param pOp
     * @param user
     * @throws com.jts.fortress.UpdateException
     *
     * @throws com.jts.fortress.FinderException
     *
     */
    void revoke(Permission pOp, User user)
        throws UpdateException, FinderException
    {
        LDAPConnection ld = null;
        String dn = getDn(pOp);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute attr = new LDAPAttribute(USERS, user.getUserId());
            mods.add(LDAPModification.DELETE, attr);
            DaoUtil.modify(ld, dn, mods, pOp);
        }
        catch (LDAPException e)
        {
            if (e.getLDAPResultCode() == LDAPException.NO_SUCH_ATTRIBUTE)
            {
                String warning = CLS_NM + ".revoke perm object [" + pOp.getObjectName() + "] operation [" + pOp.getOpName() + "] userId [" + user.getUserId() + "] assignment does not exist.";
                throw new FinderException(GlobalErrIds.PERM_USER_NOT_EXIST, warning);
            }
            else
            {
                String error = CLS_NM + ".revoke perm object [" + pOp.getObjectName() + "] operation [" + pOp.getOpName() + "] userId [" + user.getUserId() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
                throw new UpdateException(GlobalErrIds.PERM_REVOKE_FAILED, error, e);
            }
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
    }


    /**
     * @param permission
     * @return
     * @throws com.jts.fortress.FinderException
     *
     */
    Permission getPerm(Permission permission)
        throws FinderException
    {
        Permission entity = null;
        LDAPConnection ld = null;
        String dn = getOpRdn(permission.getOpName(), permission.getObjectId()) + "," + GlobalIds.POBJ_NAME + "=" + permission.getObjectName() + "," + getRootDn(permission.isAdmin());
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPEntry findEntry = DaoUtil.read(ld, dn, PERMISSION_OP_ATRS);
            entity = unloadPopLdapEntry(findEntry, 0);
            if (entity == null)
            {
                String warning = CLS_NM + ".getPerm no entry found dn [" + dn + "]";
                throw new FinderException(GlobalErrIds.PERM_OP_NOT_FOUND, warning);
            }
        }
        catch (LDAPException e)
        {
            if (e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT)
            {
                String warning = CLS_NM + ".getPerm Op COULD NOT FIND ENTRY for dn [" + dn + "]";
                throw new FinderException(GlobalErrIds.PERM_OP_NOT_FOUND, warning);
            }

            String error = CLS_NM + ".getUser [" + dn + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException(GlobalErrIds.PERM_READ_OP_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param permObj
     * @return
     * @throws com.jts.fortress.FinderException
     *
     */
    PermObj getPerm(PermObj permObj)
        throws FinderException
    {
        PermObj entity = null;
        LDAPConnection ld = null;
        String dn = GlobalIds.POBJ_NAME + "=" + permObj.getObjectName() + "," + getRootDn(permObj.isAdmin());
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPEntry findEntry = DaoUtil.read(ld, dn, PERMISION_OBJ_ATRS);
            entity = unloadPobjLdapEntry(findEntry, 0);
            if (entity == null)
            {
                String warning = CLS_NM + ".getPerm Obj no entry found dn [" + dn + "]";
                throw new FinderException(GlobalErrIds.PERM_OBJ_NOT_FOUND, warning);
            }
        }
        catch (LDAPException e)
        {
            if (e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT)
            {
                String warning = CLS_NM + ".getPerm Obj COULD NOT FIND ENTRY for dn [" + dn + "]";
                throw new FinderException(GlobalErrIds.PERM_OBJ_NOT_FOUND, warning);
            }
            String error = CLS_NM + ".getPerm Obj dn [" + dn + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException(GlobalErrIds.PERM_READ_OBJ_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param session
     * @param permission
     * @return
     * @throws com.jts.fortress.FinderException
     *
     */
    boolean checkPermission(Session session, Permission permission)
        throws FinderException
    {
        boolean result = false;
        LDAPConnection ld = null;
        String dn = GlobalIds.POBJ_NAME + "=" + permission.getObjectName() + "," + getRootDn(permission.isAdmin());
        try
        {
            String permObjVal = VUtil.encodeSafeText(permission.getObjectName(), GlobalIds.PERM_LEN);
            String permOpVal = VUtil.encodeSafeText(permission.getOpName(), GlobalIds.PERM_LEN);

            // use an unauthenticated connection as we're asserting the end user's identity onto the it:
            ld = PoolMgr.getConnection(PoolMgr.ConnType.USER);
            String filter = "(&(objectclass=" + PERM_OP_OBJECT_CLASS_NAME + ")("
                + POP_NAME + "=" + permOpVal + ")("
                + GlobalIds.POBJ_NAME + "=" + permObjVal + ")(|";
            filter += "(" + USERS + "=" + session.getUserId() + ")";
            Set<String> roles;
            if (permission.isAdmin())
            {
                roles = AdminRoleUtil.getInheritedRoles(session.getAdminRoles());
            }
            else
            {
                roles = RoleUtil.getInheritedRoles(session.getRoles());
            }
            if (VUtil.isNotNullOrEmpty(roles))
            {
                for (String uRole : roles)
                {
                    filter += "(" + ROLES + "=" + uRole + ")";
                }
            }
            filter += "))";
            // The search method uses OpenLDAP's Proxy Authorization Control to assert identity of end user onto connection: 
            LDAPEntry entry = DaoUtil.searchNode(ld, dn, LDAPConnection.SCOPE_ONE, filter, GlobalIds.NO_ATRS, true, session.getUser().getDn());
            Permission entity = unloadPopLdapEntry(entry, 0);
            if (entity != null)
            {
                result = true;
            }
        }
        catch (UnsupportedEncodingException ee)
        {
            String error = CLS_NM + ".checkPermission caught UnsupportedEncodingException=" + ee.getMessage();
            throw new FinderException(GlobalErrIds.PERM_READ_OP_FAILED, error, ee);
        }
        catch (LDAPException e)
        {
            if (e.getLDAPResultCode() != LDAPException.NO_RESULTS_RETURNED && e.getLDAPResultCode() != LDAPException.NO_SUCH_OBJECT)
            {
                String error = CLS_NM + ".checkPermission caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
                throw new FinderException(GlobalErrIds.PERM_READ_OP_FAILED, error, e);
            }
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.USER);
        }
        return result;
    }


    /**
     * @param le
     * @return
     * @throws LDAPException
     */
    private Permission unloadPopLdapEntry(LDAPEntry le, long sequence)
        throws LDAPException
    {
        Permission entity = new ObjectFactory().createPermission();
        entity.setSequenceId(sequence);
        entity.setAbstractName(DaoUtil.getAttribute(le, PERM_NAME));
        entity.setObjectName(DaoUtil.getAttribute(le, GlobalIds.POBJ_NAME));
        entity.setObjectId(DaoUtil.getAttribute(le, POBJ_ID));
        entity.setOpName(DaoUtil.getAttribute(le, POP_NAME));
        entity.setInternalId(DaoUtil.getAttribute(le, GlobalIds.FT_IID));
        entity.setRoles(DaoUtil.getAttributes(le, ROLES));
        entity.setUsers(DaoUtil.getAttributes(le, USERS));
        entity.setType(DaoUtil.getAttribute(le, TYPE));
        entity.addProperties(AttrHelper.getProperties(DaoUtil.getAttributes(le, GlobalIds.PROPS)));
        return entity;
    }

    /**
     * @param le
     * @return
     * @throws LDAPException
     */
    private PermObj unloadPobjLdapEntry(LDAPEntry le, long sequence)
        throws LDAPException
    {
        PermObj entity = new ObjectFactory().createPermObj();
        entity.setSequenceId(sequence);
        entity.setObjectName(DaoUtil.getAttribute(le, GlobalIds.POBJ_NAME));
        entity.setOu(DaoUtil.getAttribute(le, GlobalIds.OU));
        entity.setDn(le.getDN());
        entity.setInternalId(DaoUtil.getAttribute(le, GlobalIds.FT_IID));
        entity.setType(DaoUtil.getAttribute(le, TYPE));
        entity.setDescription(DaoUtil.getAttribute(le, GlobalIds.DESC));
        entity.addProperties(AttrHelper.getProperties(DaoUtil.getAttributes(le, GlobalIds.PROPS)));
        return entity;
    }

    /**
     * @param permission
     * @return
     * @throws com.jts.fortress.FinderException
     *
     */
    List<Permission> findPermissions(Permission permission)
        throws FinderException
    {
        List<Permission> permList = new ArrayList<Permission>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String permRoot = getRootDn(permission.isAdmin());
        try
        {
            String permObjVal = VUtil.encodeSafeText(permission.getObjectName(), GlobalIds.PERM_LEN);
            String permOpVal = VUtil.encodeSafeText(permission.getOpName(), GlobalIds.PERM_LEN);
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            String filter = "(&(objectclass=" + PERM_OP_OBJECT_CLASS_NAME + ")("
                + GlobalIds.POBJ_NAME + "=" + permObjVal + "*)("
                + POP_NAME + "=" + permOpVal + "*))";

            searchResults = DaoUtil.search(ld, permRoot,
                LDAPConnection.SCOPE_SUB, filter, PERMISSION_OP_ATRS, false, GlobalIds.BATCH_SIZE);
            long sequence = 0;
            while (searchResults.hasMoreElements())
            {
                permList.add(unloadPopLdapEntry(searchResults.next(), sequence++));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".findPermissions caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException(GlobalErrIds.PERM_SEARCH_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return permList;
    }


    /**
     * @param permObj
     * @return
     * @throws com.jts.fortress.FinderException
     *
     */
    List<PermObj> findPermissions(PermObj permObj)
        throws FinderException
    {
        List<PermObj> permList = new ArrayList<PermObj>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String permRoot = getRootDn(permObj.isAdmin());
        try
        {
            String permObjVal = VUtil.encodeSafeText(permObj.getObjectName(), GlobalIds.PERM_LEN);
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            String filter = "(&(objectclass=" + PERM_OBJ_OBJECT_CLASS_NAME + ")("
                + GlobalIds.POBJ_NAME + "=" + permObjVal + "*))";
            searchResults = DaoUtil.search(ld, permRoot,
                LDAPConnection.SCOPE_SUB, filter, PERMISION_OBJ_ATRS, false, GlobalIds.BATCH_SIZE);
            long sequence = 0;
            while (searchResults.hasMoreElements())
            {
                permList.add(unloadPobjLdapEntry(searchResults.next(), sequence++));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".findPermissions caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException(GlobalErrIds.PERM_SEARCH_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return permList;
    }


    /**
     * @param ou
     * @return
     * @throws FinderException
     */
    List<PermObj> findPermissions(OrgUnit ou, boolean limitSize)
        throws FinderException
    {
        List<PermObj> permList = new ArrayList<PermObj>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String permRoot = Config.getProperty(GlobalIds.PERM_ROOT);
        try
        {
            String ouVal = VUtil.encodeSafeText(ou.getName(), GlobalIds.OU_LEN);
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            String filter = "(&(objectclass=" + PERM_OBJ_OBJECT_CLASS_NAME + ")("
                + GlobalIds.OU + "=" + ouVal + "*))";
            int maxLimit;
            if (limitSize)
            {
                maxLimit = 10;
            }
            else
            {
                maxLimit = 0;
            }
            searchResults = DaoUtil.search(ld, permRoot,
                LDAPConnection.SCOPE_SUB, filter, PERMISION_OBJ_ATRS, false, GlobalIds.BATCH_SIZE, maxLimit);
            long sequence = 0;
            while (searchResults.hasMoreElements())
            {
                permList.add(unloadPobjLdapEntry(searchResults.next(), sequence++));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".findPermissions caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException(GlobalErrIds.PERM_SEARCH_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return permList;
    }


    /**
     * @param role
     * @return
     * @throws com.jts.fortress.FinderException
     *
     */
    List<Permission> findPermissions(Role role)
        throws FinderException
    {
        List<Permission> permList = new ArrayList<Permission>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String permRoot;
        if (role.getClass().equals(AdminRole.class))
        {
            permRoot = Config.getProperty(GlobalIds.ADMIN_PERM_ROOT);
        }
        else
        {
            permRoot = Config.getProperty(GlobalIds.PERM_ROOT);
        }
        try
        {
            String roleVal = VUtil.encodeSafeText(role.getName(), GlobalIds.ROLE_LEN);
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            String filter = "(&(objectclass=" + PERM_OP_OBJECT_CLASS_NAME + ")(";
            Set<String> roles;
            if (role.getClass().equals(AdminRole.class))
            {
                roles = AdminRoleUtil.getAscendants(role.getName());
            }
            else
            {
                roles = RoleUtil.getAscendants(role.getName());
            }

            if (VUtil.isNotNullOrEmpty(roles))
            {
                filter += "|(" + ROLES + "=" + roleVal + ")";
                for (String uRole : roles)
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
            searchResults = DaoUtil.search(ld, permRoot,
                LDAPConnection.SCOPE_SUB, filter, PERMISSION_OP_ATRS, false, GlobalIds.BATCH_SIZE);
            long sequence = 0;
            while (searchResults.hasMoreElements())
            {
                permList.add(unloadPopLdapEntry(searchResults.next(), sequence++));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".findPermissions caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException(GlobalErrIds.PERM_ROLE_SEARCH_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return permList;
    }

    /**
     * @param user
     * @return
     * @throws com.jts.fortress.FinderException
     *
     */
    List<Permission> findPermissions(User user)
        throws FinderException
    {
        List<Permission> permList = new ArrayList<Permission>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String permRoot = Config.getProperty(GlobalIds.PERM_ROOT);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            String filter = "(&(objectclass=" + PERM_OP_OBJECT_CLASS_NAME + ")(|";
            Set<String> roles = RoleUtil.getInheritedRoles(user.getRoles());
            if (VUtil.isNotNullOrEmpty(roles))
            {
                for (String uRole : roles)
                {
                    filter += "(" + ROLES + "=" + uRole + ")";
                }
            }
            filter += "(" + USERS + "=" + user.getUserId() + ")))";
            searchResults = DaoUtil.search(ld, permRoot,
                LDAPConnection.SCOPE_SUB, filter, PERMISSION_OP_ATRS, false, GlobalIds.BATCH_SIZE);
            long sequence = 0;
            while (searchResults.hasMoreElements())
            {
                permList.add(unloadPopLdapEntry(searchResults.next(), sequence++));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".findPermissions user [" + user.getUserId() + "] caught LDAPException in PermDAO.findPermissions=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException(GlobalErrIds.PERM_USER_SEARCH_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return permList;
    }


    /**
     * @param user
     * @return
     * @throws com.jts.fortress.FinderException
     *
     */
    List<Permission> findUserPermissions(User user)
        throws FinderException
    {
        List<Permission> permList = new ArrayList<Permission>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String permRoot = Config.getProperty(GlobalIds.PERM_ROOT);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            String filter = "(&(objectclass=" + PERM_OP_OBJECT_CLASS_NAME + ")";
            filter += "(" + USERS + "=" + user.getUserId() + "))";
            searchResults = DaoUtil.search(ld, permRoot,
                LDAPConnection.SCOPE_SUB, filter, PERMISSION_OP_ATRS, false, GlobalIds.BATCH_SIZE);
            long sequence = 0;
            while (searchResults.hasMoreElements())
            {
                permList.add(unloadPopLdapEntry(searchResults.next(), sequence++));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".findUserPermissions user [" + user.getUserId() + "] caught LDAPException in PermDAO.findPermissions=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException(GlobalErrIds.PERM_USER_SEARCH_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return permList;
    }


    /**
     * @param session
     * @return
     * @throws com.jts.fortress.FinderException
     *
     */
    List<Permission> findPermissions(Session session)
        throws FinderException
    {
        List<Permission> permList = new ArrayList<Permission>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String permRoot = Config.getProperty(GlobalIds.PERM_ROOT);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            String filter = "(&(objectclass=" + PERM_OP_OBJECT_CLASS_NAME + ")(|";
            filter += "(" + USERS + "=" + session.getUserId() + ")";
            //Set<String> roles = session.getAuthorizedRoles();
            Set<String> roles = RoleUtil.getInheritedRoles(session.getRoles());
            if (VUtil.isNotNullOrEmpty(roles))
            {
                for (String uRole : roles)
                {
                    filter += "(" + ROLES + "=" + uRole + ")";
                }
            }
            filter += "))";
            searchResults = DaoUtil.search(ld, permRoot,
                LDAPConnection.SCOPE_SUB, filter, PERMISSION_OP_ATRS, false, GlobalIds.BATCH_SIZE);
            long sequence = 0;
            while (searchResults.hasMoreElements())
            {
                permList.add(unloadPopLdapEntry(searchResults.next(), sequence++));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".findPermissions user [" + session.getUserId() + "] caught LDAPException in PermDAO.findPermissions=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException(GlobalErrIds.PERM_SESS_SEARCH_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return permList;
    }


    /**
     * @param opName
     * @param objId
     * @return
     */
    final static String getOpRdn(String opName, String objId)
    {
        String rDn;
        if (objId != null && objId.length() > 0)
            rDn = POP_NAME + "=" + opName + "+" + POBJ_ID + "=" + objId;
        else
            rDn = POP_NAME + "=" + opName;
        return rDn;
    }

    private String getDn(Permission pOp)
    {
        return getOpRdn(pOp.getOpName(), pOp.getObjectId()) + "," + GlobalIds.POBJ_NAME + "=" + pOp.getObjectName() + "," + getRootDn(pOp.isAdmin());
    }

    private String getDn(PermObj pObj)
    {
        return GlobalIds.POBJ_NAME + "=" + pObj.getObjectName() + "," + getRootDn(pObj.isAdmin());
    }

    /**
     * @param isAdmin
     * @return
     */
    private String getRootDn(boolean isAdmin)
    {
        String dn;
        if (isAdmin)
        {
            dn = Config.getProperty(GlobalIds.ADMIN_PERM_ROOT);
        }
        else
        {
            dn = Config.getProperty(GlobalIds.PERM_ROOT);
        }
        return dn;
    }
}