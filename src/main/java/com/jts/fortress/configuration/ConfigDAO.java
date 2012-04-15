/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.configuration;

import com.jts.fortress.FinderException;
import com.jts.fortress.RemoveException;
import com.jts.fortress.UpdateException;
import com.jts.fortress.ldap.DaoUtil;
import com.jts.fortress.ldap.PoolMgr;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.util.attr.AttrHelper;
import com.jts.fortress.util.attr.VUtil;
import org.apache.log4j.Logger;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;

import java.util.Properties;

/**
 * This class provides data access for the standard ldap object device that has been extended to support name/value pairs.
 * Fortress uses this data structure to store its remote configuration parameters.
 * <p/>
 * The Fortress Config node is a combination of:
 * <p/>
 * 'device' Structural Object Class is used to store basic attributes like cn which will be used for config node name.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.5.6.14 NAME 'device'</code>
 * <li> <code>DESC 'RFC2256: a device''</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST cn</code>
 * <li> <code>MAY ( serialNumber $ seeAlso $ owner $ ou $ o $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * 'ftProperties' AUXILIARY Object Class is used to store name/value pairs on target node.<br />
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
 * <p/>
 * <p/>

 *
 * @author Shawn McKinney
 * @created February 4, 2011
 */
public final class ConfigDAO

{
    private static final String CLS_NM = ConfigDAO.class.getName();
    final private static Logger log = Logger.getLogger(CLS_NM);

    private static final String CONFIG_ROOT_PARAM = "config.root";
    private final static String CONFIG_ROOT_DN = Config.getProperty(CONFIG_ROOT_PARAM);

    private final static String DEVICE_OBJECT_CLASS_NM = "device";

    private final static String CONFIG_OBJ_CLASS[] = {
        DEVICE_OBJECT_CLASS_NM, GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME
    };

    private final static String[] CONFIG_ATRS = {
        GlobalIds.CN, GlobalIds.PROPS
    };

    /**
     * Package private default constructor.
     */
    ConfigDAO(){}


    /**
     * @param name
     * @param props
     * @return
     * @throws com.jts.fortress.CreateException
     */
    Properties create(String name, Properties props)
        throws com.jts.fortress.CreateException
    {
        LDAPConnection ld = null;
        String dn = getDn(name);
        log.info(CLS_NM + ".create dn [" + dn + "]");
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add(DaoUtil.createAttributes(GlobalIds.OBJECT_CLASS, CONFIG_OBJ_CLASS));
            //attrs.add(DaoUtil.createAttribute(GlobalIds.CN, "Config"));
            attrs.add(DaoUtil.createAttribute(GlobalIds.CN, name));
            DaoUtil.loadProperties(props, attrs, GlobalIds.PROPS);
            LDAPEntry myEntry = new LDAPEntry(dn, attrs);
            DaoUtil.add(ld, myEntry);
        }
        catch (LDAPException e)
        {
            String error;
            if (e.getLDAPResultCode() == LDAPException.ENTRY_ALREADY_EXISTS)
            {
                String warning = CLS_NM + ".create config dn [" + dn + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
                throw new com.jts.fortress.CreateException(GlobalErrIds.FT_CONFIG_ALREADY_EXISTS, warning);
            }
            else
            {
                error = CLS_NM + ".create config dn [" + dn + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            }
            log.error(error, e);
            throw new com.jts.fortress.CreateException(GlobalErrIds.FT_CONFIG_CREATE_FAILED, error);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return props;
    }


    /**
     * @param name
     * @param props
     * @return
     * @throws com.jts.fortress.UpdateException
     */
    Properties update(String name, Properties props)
        throws com.jts.fortress.UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn(name);
        log.info(CLS_NM + "update dn [" + dn + "]");
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            if (com.jts.fortress.util.attr.VUtil.isNotNullOrEmpty(props))
            {
                DaoUtil.loadProperties(props, mods, GlobalIds.PROPS, true);
            }
            if (mods.size() > 0)
            {
                DaoUtil.modify(ld, dn, mods);
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".update dn [" + dn + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new com.jts.fortress.UpdateException(GlobalErrIds.FT_CONFIG_UPDATE_FAILED, error);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return props;
    }


    /**
     * @param name
     * @throws com.jts.fortress.RemoveException
     */
    void remove(String name)
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = getDn(name);
        log.info(CLS_NM + ".remove dn [" + dn + "]");
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            DaoUtil.delete(ld, dn);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".remove dn [" + dn + "] LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new com.jts.fortress.RemoveException(GlobalErrIds.FT_CONFIG_DELETE_FAILED, error);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
    }


    /**
     * @param name
     * @param props
     * @return
     * @throws com.jts.fortress.UpdateException
     */
    Properties remove(String name, Properties props)
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn(name);
        log.info(CLS_NM + "remove props dn [" + dn + "]");
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            if (VUtil.isNotNullOrEmpty(props))
            {
                DaoUtil.removeProperties(props, mods, GlobalIds.PROPS);
            }
            if (mods.size() > 0)
            {
                DaoUtil.modify(ld, dn, mods);
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".remove props dn [" + dn + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new com.jts.fortress.UpdateException(GlobalErrIds.FT_CONFIG_DELETE_PROPS_FAILED, error);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return props;
    }


    /**
     * @param name
     * @return
     * @throws com.jts.fortress.FinderException
     */
    Properties getConfig(String name)
        throws FinderException
    {
        Properties props = null;
        LDAPConnection ld = null;
        String dn = getDn(name);
        log.info(CLS_NM + "getConfig dn [" + dn + "]");
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPEntry findEntry = DaoUtil.read(ld, dn, CONFIG_ATRS);
            props = AttrHelper.getProperties(DaoUtil.getAttributes(findEntry, GlobalIds.PROPS));
        }
        catch (LDAPException e)
        {
            if (e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT)
            {
                String warning = CLS_NM + ".getConfig COULD NOT FIND ENTRY for dn [" + dn + "]";
                throw new com.jts.fortress.FinderException(GlobalErrIds.FT_CONFIG_NOT_FOUND, warning);
            }
            String error = CLS_NM + ".getConfig dn [" + dn + "] LEXCD=" + e.getLDAPResultCode() + " LEXMSG=" + e;
            throw new FinderException(GlobalErrIds.FT_CONFIG_READ_FAILED, error);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return props;
    }

    /**
     *
     * @param name
     * @return
     */
    private String getDn(String name)
    {
        return GlobalIds.CN + "=" + name + "," + CONFIG_ROOT_DN;
    }
}

