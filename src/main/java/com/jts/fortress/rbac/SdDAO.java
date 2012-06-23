/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.CreateException;
import com.jts.fortress.ObjectFactory;
import com.jts.fortress.RemoveException;
import com.jts.fortress.configuration.Config;
import com.jts.fortress.FinderException;
import com.jts.fortress.UpdateException;
import com.jts.fortress.hier.RoleUtil;
import com.jts.fortress.ldap.DaoUtil;
import com.jts.fortress.ldap.PoolMgr;
import com.jts.fortress.constants.GlobalErrIds;

import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.attr.VUtil;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttribute;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModification;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchResults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class performs persistence on the RBAC Static Separation of Duties and Dynamic Separation of Duties data sets.
 * <p/>
 * The Fortress SDSet entity is a composite of the following other Fortress structural and aux object classes:
 * <h4>1. organizationalRole Structural Object Class is used to store basic attributes like cn and description</h4>
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
 * <h4>2. The RBAC Separation of Duties</h4>
 * <ul>
 * <li>  ---Static Separation of Duties Set-------
 * <li> <code>objectclass	( 1.3.6.1.4.1.38088.2.4</code>
 * <li> <code>NAME 'ftSSDSet'</code>
 * <li> <code>DESC 'Fortress Role Static Separation of Duty Set Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftSetName $ ftSetCardinality )</code>
 * <li> <code>MAY ( ftRoles $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * OR
 * <h4>Dynamic Separation of Duties Set</h4>
 * <ul>
 * <li>
 * <li> <code>objectclass	( 1.3.6.1.4.1.38088.2.5</code>
 * <li> <code>NAME 'ftDSDSet'</code>
 * <li> <code>DESC 'Fortress Role Dynamic Separation of Duty Set Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftSetName $ ftSetCardinality )</code>
 * <li> <code>MAY ( ftRoles $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>3. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity</h4>
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
 * This class is thread safe.
 * <p/>

 *
 * @author Shawn McKinney
 * @created September 11, 2010
 */
public final class SdDAO

{
    private static final String CLS_NM = SdDAO.class.getName();

    private final static String SD_SET_NM = "ftSetName";
    private final static String ROLES = "ftRoles";
    private final static String SD_SET_CARDINALITY = "ftSetCardinality";

    private final static String SSD_OBJECT_CLASS_NM = "ftSSDSet";
    private final static String SSD_OBJ_CLASS[] = {
        GlobalIds.TOP, SSD_OBJECT_CLASS_NM, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };

    private final static String DSD_OBJECT_CLASS_NM = "ftDSDSet";
    private final static String DSD_OBJ_CLASS[] = {
        GlobalIds.TOP, DSD_OBJECT_CLASS_NM, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };

    private final static String[] SD_SET_ATRS = {
        GlobalIds.FT_IID, SD_SET_NM, GlobalIds.DESC, ROLES, SD_SET_CARDINALITY
    };

    /**
     * Package private constructor
     */
    SdDAO(){}

    /**
     * @param entity
     * @return
     * @throws com.jts.fortress.CreateException
     */
    SDSet create(SDSet entity)
        throws CreateException
    {
        LDAPConnection ld = null;
        String dn = GlobalIds.CN + "=" + entity.getName() + "," + Config.getProperty(GlobalIds.SD_ROOT);
        String[] objectClass = SSD_OBJ_CLASS;
        if (entity.getType() == SDSet.SDType.DYNAMIC)
        {
            objectClass = DSD_OBJ_CLASS;
        }
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add(DaoUtil.createAttributes(GlobalIds.OBJECT_CLASS, objectClass));
            entity.setId();
            attrs.add(DaoUtil.createAttribute(GlobalIds.FT_IID, entity.getId()));
            attrs.add(DaoUtil.createAttribute(SD_SET_NM, entity.getName()));
            // description field is optional on this object class:
            if (VUtil.isNotNullOrEmpty(entity.getDescription()))
            {
                attrs.add(DaoUtil.createAttribute(GlobalIds.DESC, entity.getDescription()));
            }
            // CN attribute is required for this object class:
            attrs.add(DaoUtil.createAttribute(GlobalIds.CN, entity.getName()));
            //DaoUtil.loadAttrs(entity.getMembers().keySet(), attrs, ROLES);
            DaoUtil.loadAttrs(entity.getMembers(), attrs, ROLES);
            attrs.add(DaoUtil.createAttribute(SD_SET_CARDINALITY, "" + entity.getCardinality()));
            LDAPEntry myEntry = new LDAPEntry(dn, attrs);
            DaoUtil.add(ld, myEntry, entity);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".create SD set name [" + entity.getName() + "] type [" + entity.getType() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            int errCode;
            if (entity.getType() == SDSet.SDType.DYNAMIC)
            {
                errCode = GlobalErrIds.DSD_ADD_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_ADD_FAILED;
            }

            throw new CreateException(errCode, error, e);
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
    SDSet update(SDSet entity)
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = GlobalIds.CN + "=" + entity.getName() + "," + Config.getProperty(GlobalIds.SD_ROOT);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            if (VUtil.isNotNullOrEmpty(entity.getDescription()))
            {
                LDAPAttribute desc = new LDAPAttribute(GlobalIds.DESC, entity.getDescription());
                mods.add(LDAPModification.REPLACE, desc);
            }
            if (entity.getCardinality() != null)
            {
                LDAPAttribute cardinality = new LDAPAttribute(SD_SET_CARDINALITY, "" + entity.getCardinality());
                mods.add(LDAPModification.REPLACE, cardinality);
            }
            //DaoUtil.loadAttrs(entity.getMembers().keySet(), mods, ROLES);
            DaoUtil.loadAttrs(entity.getMembers(), mods, ROLES);
            if (mods.size() > 0)
            {
                DaoUtil.modify(ld, dn, mods, entity);
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".update name [" + entity.getName() + "] type [" + entity.getType() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            int errCode;
            if (entity.getType() == SDSet.SDType.DYNAMIC)
            {
                errCode = GlobalErrIds.DSD_UPDATE_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_UPDATE_FAILED;
            }

            throw new UpdateException(errCode, error, e);
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
     */
    SDSet remove(SDSet entity)
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = GlobalIds.CN + "=" + entity.getName() + "," + Config.getProperty(GlobalIds.SD_ROOT);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            DaoUtil.delete(ld, dn, entity);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".remove SD name=" + entity.getName() + " type [" + entity.getType() + "] LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            int errCode;
            if (entity.getType() == SDSet.SDType.DYNAMIC)
            {
                errCode = GlobalErrIds.DSD_DELETE_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_DELETE_FAILED;
            }

            throw new RemoveException(errCode, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param name
     * @return
     * @throws FinderException
     */
    SDSet getSD(String name)
        throws FinderException
    {
        SDSet entity = null;
        LDAPConnection ld = null;
        String dn = GlobalIds.CN + "=" + name + "," + Config.getProperty(GlobalIds.SD_ROOT);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPEntry findEntry = DaoUtil.read(ld, dn, SD_SET_ATRS);
            entity = unloadLdapEntry(findEntry, 0);
            if (entity == null)
            {
                String warning = CLS_NM + ".getSD no entry found dn [" + dn + "]";
                throw new FinderException(GlobalErrIds.SSD_NOT_FOUND, warning);
            }
        }
        catch (LDAPException e)
        {
            if (e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT)
            {
                String warning = CLS_NM + ".getSD Obj COULD NOT FIND ENTRY for dn [" + dn + "]";
                throw new FinderException(GlobalErrIds.SSD_NOT_FOUND, warning);
            }
            String error = CLS_NM + ".getSSD dn [" + dn + "] LEXCD=" + e.getLDAPResultCode() + " LEXMSG=" + e;
            int errCode;
            if (entity.getType() == SDSet.SDType.DYNAMIC)
            {
                errCode = GlobalErrIds.DSD_READ_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_READ_FAILED;
            }

            throw new FinderException(errCode, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * Given an SSD name and type, find matching object in the directory.
     * @param sdset requires name and type.
     * @return List of matching SDSets.
     * @throws com.jts.fortress.FinderException
     */
    List<SDSet> search(SDSet sdset)
        throws FinderException
    {
        List<SDSet> sdList = new ArrayList<SDSet>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String ssdRoot = Config.getProperty(GlobalIds.SD_ROOT);
        String objectClass = SSD_OBJECT_CLASS_NM;
        if (sdset.getType() == SDSet.SDType.DYNAMIC)
        {
            objectClass = DSD_OBJECT_CLASS_NM;
        }
        try
        {
            String searchVal = VUtil.encodeSafeText(sdset.getName(), GlobalIds.ROLE_LEN);
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            String filter = "(&(objectclass=" + objectClass + ")(" + SD_SET_NM + "=" + searchVal + "*))";
            searchResults = DaoUtil.search(ld, ssdRoot,
                LDAPConnection.SCOPE_SUB, filter, SD_SET_ATRS, false, GlobalIds.BATCH_SIZE);
            long sequence = 0;
            while (searchResults.hasMoreElements())
            {
                sdList.add(unloadLdapEntry(searchResults.next(), sequence++));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".search sdset name [" + sdset.getName() + "] type [" + sdset.getType() + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            int errCode;
            if (sdset.getType() == SDSet.SDType.DYNAMIC)
            {
                errCode = GlobalErrIds.DSD_SEARCH_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_SEARCH_FAILED;
            }
            throw new FinderException(errCode, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return sdList;
    }

    /**
     * @param role
     * @return
     * @throws com.jts.fortress.FinderException
     */
    List<SDSet> search(Role role, SDSet.SDType type)
        throws FinderException
    {
        List<SDSet> sdList = new ArrayList<SDSet>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String ssdRoot = Config.getProperty(GlobalIds.SD_ROOT);
        String objectClass = SSD_OBJECT_CLASS_NM;
        if (type == SDSet.SDType.DYNAMIC)
        {
            objectClass = DSD_OBJECT_CLASS_NM;
        }

        try
        {
            String roleVal = VUtil.encodeSafeText(role.getName(), GlobalIds.ROLE_LEN);
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            //String filter = "(&(objectclass=" + SSD_OBJECT_CLASS_NM + ")(" + ROLES + "=" + roleVal + "))";
            String filter = "(&(objectclass=" + objectClass + ")(";
            // Include any parents target role may have:
            Set<String> roles = RoleUtil.getAscendants(role.getName());
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
            searchResults = DaoUtil.search(ld, ssdRoot,
                LDAPConnection.SCOPE_SUB, filter, SD_SET_ATRS, false, GlobalIds.BATCH_SIZE);

            long sequence = 0;
            while (searchResults.hasMoreElements())
            {
                sdList.add(unloadLdapEntry(searchResults.next(), sequence++));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".search role [" + role.getName() + "] type [" + type + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            int errCode;
            if (type == SDSet.SDType.DYNAMIC)
            {
                errCode = GlobalErrIds.DSD_SEARCH_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_SEARCH_FAILED;
            }

            throw new FinderException(errCode, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return sdList;
    }

    /**
     * @param roles
     * @param type
     * @return
     * @throws com.jts.fortress.FinderException
     */
    Set<SDSet> search(Set<String> roles, SDSet.SDType type)
        throws FinderException
    {
        Set<SDSet> sdList = new HashSet<SDSet>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String ssdRoot = Config.getProperty(GlobalIds.SD_ROOT);
        String objectClass = SSD_OBJECT_CLASS_NM;
        if (type == SDSet.SDType.DYNAMIC)
        {
            objectClass = DSD_OBJECT_CLASS_NM;
        }
        try
        {
            if (VUtil.isNotNullOrEmpty(roles))
            {
                ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
                String filter = "(&(objectclass=" + objectClass + ")(|";
                for (String rle : roles)
                {
                    filter += "(" + ROLES + "=" + rle + ")";
                }
                filter += "))";
                searchResults = DaoUtil.search(ld, ssdRoot,
                    LDAPConnection.SCOPE_SUB, filter, SD_SET_ATRS, false, GlobalIds.BATCH_SIZE);
                long sequence = 0;
                while (searchResults.hasMoreElements())
                {
                    sdList.add(unloadLdapEntry(searchResults.next(), sequence++));
                }
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".search type [" + type + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            int errCode;
            if (type == SDSet.SDType.DYNAMIC)
            {
                errCode = GlobalErrIds.DSD_SEARCH_FAILED;
            }
            else
            {
                errCode = GlobalErrIds.SSD_SEARCH_FAILED;
            }
            throw new FinderException(errCode, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return sdList;
    }

    /**
     * @param le
     * @return
     * @throws LDAPException
     */
    private SDSet unloadLdapEntry(LDAPEntry le, long sequence)
        throws LDAPException
    {
        SDSet entity = new ObjectFactory().createSDset();
        entity.setSequenceId(sequence);
        entity.setId(DaoUtil.getAttribute(le, GlobalIds.FT_IID));
        entity.setName(DaoUtil.getAttribute(le, SD_SET_NM));
        entity.setDescription(DaoUtil.getAttribute(le, GlobalIds.DESC));
        entity.setMembers(DaoUtil.getAttributeSet(le, ROLES));
        String szCard = DaoUtil.getAttribute(le, SD_SET_CARDINALITY);
        entity.setCardinality(new Integer(szCard));
        return entity;
    }
}

