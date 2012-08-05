/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.GlobalErrIds;
import com.jts.fortress.GlobalIds;
import com.jts.fortress.cfg.Config;
import com.jts.fortress.CreateException;
import com.jts.fortress.FinderException;
import com.jts.fortress.RemoveException;
import com.jts.fortress.UpdateException;
import com.jts.fortress.ldap.DaoUtil;
import com.jts.fortress.ldap.PoolMgr;
import com.jts.fortress.util.attr.VUtil;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;

import org.apache.log4j.Logger;


/**
 * This class contains data access for Fortress hierarchical entities.
 * <p/>
 * All entities (User, Role, Permission, Policy, SDSet, etc...) are used to carry data between Fortress's
 * layers starting with the (1) Manager layer down thru middle (2) Process layer and it's processing rules into
 * (3) DAO layer where persistence with the OpenLDAP server occurs.  The clients must instantiate an Fortress entity before use
 * and must provide enough information to uniquely identity target record for reads.
 * <p/>
 * The Fortress ftHier Entity Class is used internal to Fortress and usually does not require manipulation by external program.  The
 * entity is a composite of 3 different LDAP Schema object classes:
 * <h3>1. organizationalRole Structural Object Class is used to store basic attributes like cn and description</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.5.6.8 NAME 'organizationalRole'</code>
 * <li> <code>DESC 'RFC2256: an organizational role'</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST cn</code>
 * <li> <code>MAY ( x121Address $ registeredAddress $ destinationIndicator $</code>
 * <li> <code>preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $</code>
 * <li> <code>telephoneNumber $ internationaliSDNNumber $ facsimileTelephoneNumber $</code>
 * <li> <code>seeAlso $ roleOccupant $ preferredDeliveryMethod $ street $</code>
 * <li> <code>postOfficeBox $ postalCode $ postalAddress $</code>
 * <li> <code>physicalDeliveryOfficeName $ ou $ st $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h3>2. ftHier AUXILIARY Object Class is used to store parent to child relationships on target entity</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass	( 1.3.6.1.4.1.38088.2.7</code>
 * <li> <code>NAME 'ftHier'</code>
 * <li> <code>DESC 'Fortress Hierarchy Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( cn ) </code>
 * <li> <code> MAY ( ftRels $ description ) ) </code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>3. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity</h4>
 * <ul>
 * <li>  ------------------------------------------
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
 * @created June 13, 2010
 */
final class HierDAO

{
    private static final String CLS_NM = HierDAO.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);
    private static final String NODE_NAME = "Hierarchies";
    private static final String NODE_RELS = "ftRels";
    private static final String[] HIER_ATRS = {
        NODE_RELS, GlobalIds.DESC
    };
    private static final String HIER_OBJECT_CLASS_NM = "ftHier";
    private static final String HIER_OBJ_CLASS[] = {
        GlobalIds.TOP, HIER_OBJECT_CLASS_NM, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };

    /**
     * package private constructor to disallow others to construct.
     *
     */
    HierDAO(){}


    /**
     * @param entity
     * @return
     * @throws com.jts.fortress.CreateException
     */
    final Hier create(Hier entity)
        throws CreateException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add(DaoUtil.createAttributes(GlobalIds.OBJECT_CLASS, HIER_OBJ_CLASS));
            attrs.add(DaoUtil.createAttribute(GlobalIds.CN, "Hierarchies"));
            DaoUtil.loadRelationshipAttrs(entity.getRelationships(), attrs, NODE_RELS);
            LDAPEntry myEntry = new LDAPEntry(dn, attrs);
            DaoUtil.add(ld, myEntry, entity);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".create dn [" + dn + "] type [" + entity.getType() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new CreateException(GlobalErrIds.HIER_ADD_FAILED, error, e);
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
     */
    final Hier update(Hier entity, Hier.Op op)
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            DaoUtil.loadRelationshipAttrs(entity.getRelationships(), mods, NODE_RELS, op);
            if (mods.size() > 0)
            {
                DaoUtil.modify(ld, dn, mods, entity);
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".update dn [" + dn + "] type " + entity.getType() + " op [" + op + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException(GlobalErrIds.HIER_UPDATE_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @throws com.jts.fortress.RemoveException
     */
    final void remove(Hier hier)
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = getDn(hier);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            DaoUtil.delete(ld, dn, hier);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".remove dn [" + dn + "] type [" + hier.getType() + "] name=" + NODE_NAME + " LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new RemoveException(GlobalErrIds.HIER_DELETE_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
    }


    /**
     * @param hier
     * @return
     */
    private String getDn(Hier hier)
    {
        String dn = GlobalIds.CN + "=" + NODE_NAME + ",";
        switch (hier.getType())
        {
            case ROLE:
                dn += DaoUtil.getRootDn(hier.getContextId(), GlobalIds.ROLE_ROOT);
                break;
            case AROLE:
                dn += DaoUtil.getRootDn(hier.getContextId(), GlobalIds.ADMIN_ROLE_ROOT);
                break;
            case USER:
                dn += DaoUtil.getRootDn(hier.getContextId(), GlobalIds.OSU_ROOT);
                break;
            case PERM:
                dn += DaoUtil.getRootDn(hier.getContextId(), GlobalIds.PSU_ROOT);
                break;
            default:
                String warning = CLS_NM + ".getDn invalid type";
                log.warn(warning);
                break;
        }
        return dn;
    }

    /**
     * @param hier
     * @return
     * @throws com.jts.fortress.FinderException
     */
    final Hier getHier(Hier hier)
        throws FinderException
    {
        Hier entity = null;
        LDAPConnection ld = null;
        String dn = getDn(hier);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPEntry findEntry = DaoUtil.read(ld, dn, HIER_ATRS);
            entity = unloadLdapEntry(findEntry);
            if (entity == null)
            {
                String warning = CLS_NM + ".getHier no entry found dn [" + dn + "] Fortress errorCode=" + GlobalErrIds.ROLE_NOT_FOUND;
                throw new FinderException(GlobalErrIds.ROLE_NOT_FOUND, warning);
            }
        }
        catch (LDAPException e)
        {
            if (e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT)
            {
                String warning = CLS_NM + ".getHier Obj COULD NOT FIND ENTRY for dn [" + dn + "] Fortress errorCode=" + GlobalErrIds.ROLE_NOT_FOUND;
                throw new FinderException(GlobalErrIds.HIER_NOT_FOUND, warning);
            }
            String error = CLS_NM + ".getHier dn [" + dn + "] LEXCD=" + e.getLDAPResultCode() + " LEXMSG=" + e;
            throw new FinderException(GlobalErrIds.HIER_READ_FAILED, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param le
     * @return
     * @throws LDAPException
     */
    private Hier unloadLdapEntry(LDAPEntry le)
        throws LDAPException
    {
        Hier entity = new Hier(DaoUtil.getRelationshipAttributes(le, NODE_RELS));
        return entity;
    }
}
