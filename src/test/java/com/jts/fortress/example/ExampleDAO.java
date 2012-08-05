/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.example;

import com.jts.fortress.CreateException;
import com.jts.fortress.GlobalIds;
import com.jts.fortress.RemoveException;
import com.jts.fortress.UpdateException;
import com.jts.fortress.ldap.DaoUtil;
import com.jts.fortress.cfg.Config;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttribute;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
//import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPCompareAttrNames;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModification;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchResults;

import java.util.ArrayList;
import java.util.List;

public class ExampleDAO

{
    private static final String CLS_NM = ExampleDAO.class.getName();
    final protected static Logger log = Logger.getLogger(CLS_NM);
    protected final static String[] EXAMPLE_ATRS = {
        GlobalIds.FT_IID, EIds.EXAMPLE_NM, GlobalIds.DESC, GlobalIds.CONSTRAINT
    };

    /**
     * @param entity
     * @return
     * @throws com.jts.fortress.CreateException
     *
     */
    public Example create(Example entity)
        throws CreateException
    {
        LDAPConnection ld = null;
        String dn = GlobalIds.CN + "=" + entity.getName() + "," + Config.getProperty(EIds.EXAMPLE_ROOT);
        if (log.isEnabledFor(Level.DEBUG))
        {
            log.debug(CLS_NM + ".create dn [" + dn + "]");
        }
        try
        {

            /*
        public class Example
                implements Constraint, java.io.Serializable
        {
            private String id;          // this maps to oamId
            private String name;          // this is oamRoleName
            private String description; // this is description
            private String dn;          // this attribute is automatically saved to each ldap record.
            private String beginTime;     // this attribute is oamBeginTime
            private String endTime;        // this attribute is oamEndTime
            private String beginDate;    // this attribute is oamBeginDate
            private String endDate;        // this attribute is oamEndDate
            private String beginLockDate;// this attribute is oamBeginLockDate
            private String endLockDate;    // this attribute is oamEndLockDate
            private String dayMask;        // this attribute is oamDayMask
            private int timeout;        // this attribute is oamTimeOut
            */

            ld = com.jts.fortress.ldap.PoolMgr.getConnection(com.jts.fortress.ldap.PoolMgr.ConnType.ADMIN);
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add(com.jts.fortress.ldap.DaoUtil.createAttributes(GlobalIds.OBJECT_CLASS, EIds.EXAMPLE_OBJ_CLASS));
            entity.setId();
            attrs.add(com.jts.fortress.ldap.DaoUtil.createAttribute(GlobalIds.FT_IID, entity.getId()));
            attrs.add(DaoUtil.createAttribute(EIds.EXAMPLE_NM, entity.getName()));
            if (entity.getDescription() != null && entity.getDescription().length() > 0)
                attrs.add(com.jts.fortress.ldap.DaoUtil.createAttribute(GlobalIds.DESC, entity.getDescription()));
            // organizational name requires CN attribute:
            attrs.add(com.jts.fortress.ldap.DaoUtil.createAttribute(GlobalIds.CN, entity.getName()));

            //AttrHelper.loadTemporalAttrs(entity, attrs);
            entity.setName("EXAMPLE");
            attrs.add(com.jts.fortress.ldap.DaoUtil.createAttribute(GlobalIds.CONSTRAINT, com.jts.fortress.util.time.CUtil.setConstraint(entity)));

            LDAPEntry myEntry = new LDAPEntry(dn, attrs);
            DaoUtil.add(ld, myEntry);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".create [" + entity.getName() + "] caught LDAPException=" + e;
            log.error(error);
            throw new CreateException(EErrIds.EXAMPLE_ADD_FAILED, error);
        }
        finally
        {
            com.jts.fortress.ldap.PoolMgr.closeConnection(ld, com.jts.fortress.ldap.PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param entity
     * @return
     * @throws com.jts.fortress.UpdateException
     *
     */
    public Example update(Example entity)
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = GlobalIds.CN + "=" + entity.getName() + "," + com.jts.fortress.cfg.Config.getProperty(EIds.EXAMPLE_ROOT);
        if (log.isEnabledFor(Level.DEBUG))
        {
            log.debug(CLS_NM + ".update dn [" + dn + "]");
        }
        try
        {
            ld = com.jts.fortress.ldap.PoolMgr.getConnection(com.jts.fortress.ldap.PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            if (entity.getDescription() != null && entity.getDescription().length() > 0)
            {
                LDAPAttribute desc = new LDAPAttribute(GlobalIds.DESC, entity.getDescription());
                mods.add(LDAPModification.REPLACE, desc);
            }
            String szRawData = com.jts.fortress.util.time.CUtil.setConstraint(entity);
            if (szRawData != null && szRawData.length() > 0)
            {
                LDAPAttribute constraint = new LDAPAttribute(GlobalIds.CONSTRAINT, szRawData);
                mods.add(LDAPModification.REPLACE, constraint);
            }
            if (mods.size() > 0)
            {
                com.jts.fortress.ldap.DaoUtil.modify(ld, dn, mods);
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".update [" + entity.getName() + "] caught LDAPException=" + e;
            log.error(error);
            throw new UpdateException(EErrIds.EXAMPLE_UPDATE_FAILED, error);
        }
        finally
        {
            com.jts.fortress.ldap.PoolMgr.closeConnection(ld, com.jts.fortress.ldap.PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param name
     * @throws com.jts.fortress.RemoveException
     *
     */
    public void remove(String name)
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = GlobalIds.CN + "=" + name + "," + Config.getProperty(EIds.EXAMPLE_ROOT);
        if (log.isEnabledFor(Level.DEBUG))
        {
            log.debug(CLS_NM + ".remove dn [" + dn + "]");
        }
        try
        {
            ld = com.jts.fortress.ldap.PoolMgr.getConnection(com.jts.fortress.ldap.PoolMgr.ConnType.ADMIN);
            DaoUtil.delete(ld, dn);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".remove [" + name + "] caught LDAPException=" + e;
            log.error(error);
            throw new RemoveException(EErrIds.EXAMPLE_DELETE_FAILED, error);
        }
        finally
        {
            com.jts.fortress.ldap.PoolMgr.closeConnection(ld, com.jts.fortress.ldap.PoolMgr.ConnType.ADMIN);
        }
    }


    /**
     * @param name
     * @return
     * @throws com.jts.fortress.FinderException
     *
     */
    public Example findByKey(String name)
        throws com.jts.fortress.FinderException
    {
        Example entity = null;
        LDAPConnection ld = null;
        String dn = GlobalIds.CN + "=" + name + "," + Config.getProperty(EIds.EXAMPLE_ROOT);
        if (log.isEnabledFor(Level.DEBUG))
        {
            log.debug(CLS_NM + ".findByKey dn [" + dn + "]");
        }
        try
        {
            ld = com.jts.fortress.ldap.PoolMgr.getConnection(com.jts.fortress.ldap.PoolMgr.ConnType.ADMIN);
            LDAPEntry findEntry = DaoUtil.read(ld, dn, EXAMPLE_ATRS);
            entity = getEntityFromLdapEntry(findEntry);
            if (entity == null)
            {
                String error = CLS_NM + ".findByKey could not find entry for example name [" + name + "]";
                log.fatal(error);
                throw new com.jts.fortress.FinderException(EErrIds.EXAMPLE_NOT_FOUND, error);
            }
        }
        catch (LDAPException e)
        {
            if (e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT)
            {
                String error = CLS_NM + ".findByKey COULD NOT FIND ENTRY for example name [" + name + "]";
                log.warn(error);
                throw new com.jts.fortress.FinderException(EErrIds.EXAMPLE_NOT_FOUND, error);
            }
            else
            {
                String error = CLS_NM + ".findByKey name [" + name + "] caught LDAPException=" + e;
                log.warn(error);
                throw new com.jts.fortress.FinderException(EErrIds.EXAMPLE_READ_FAILED, error);
            }
        }
        finally
        {
            com.jts.fortress.ldap.PoolMgr.closeConnection(ld, com.jts.fortress.ldap.PoolMgr.ConnType.ADMIN);
        }
        return entity;
    }


    /**
     * @param searchVal
     * @return
     * @throws com.jts.fortress.FinderException
     *
     */
    public List<Example> findExamples(String searchVal)
        throws com.jts.fortress.FinderException
    {
        List<Example> exampleList = new ArrayList<Example>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String exampleRoot = com.jts.fortress.cfg.Config.getProperty(EIds.EXAMPLE_ROOT);

        if (log.isEnabledFor(Level.DEBUG))
        {
            log.debug(CLS_NM + ".findExamples: " + EIds.EXAMPLE_ROOT + " [" + exampleRoot + "]");
        }
        try
        {
            searchVal = com.jts.fortress.util.attr.VUtil.encodeSafeText(searchVal, GlobalIds.ROLE_LEN);
            ld = com.jts.fortress.ldap.PoolMgr.getConnection(com.jts.fortress.ldap.PoolMgr.ConnType.ADMIN);
            String filter = "(&(objectclass=" + EIds.EXAMPLE_OBJ_CLASS.toString() + ")("
                + EIds.EXAMPLE_NM + "=" + searchVal + "*))";
            searchResults = DaoUtil.search(ld, exampleRoot,
                LDAPConnection.SCOPE_ONE, filter, EXAMPLE_ATRS, false, EIds.BATCH_SIZE);
            while (searchResults.hasMoreElements())
            {
                exampleList.add(getEntityFromLdapEntry(searchResults.next()));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".findExamples caught LDAPException=" + e;
            log.warn(error);
            throw new com.jts.fortress.FinderException(EErrIds.EXAMPLE_SEARCH_FAILED, error);
        }
        finally
        {
            com.jts.fortress.ldap.PoolMgr.closeConnection(ld, com.jts.fortress.ldap.PoolMgr.ConnType.ADMIN);
        }
        return exampleList;
    }


    /**
     * @param le
     * @return
     * @throws LDAPException
     */
    private Example getEntityFromLdapEntry(LDAPEntry le)
        throws LDAPException
    {
        /*
        public class Role
                implements Constraint, java.io.Serializable
        {
            private String id;          // this maps to oamId
            private String name;          // this is oamRoleName
            private String description; // this is description
            private String dn;          // this attribute is automatically saved to each ldap record.
            private String beginTime;     // this attribute is oamBeginTime
            private String endTime;        // this attribute is oamEndTime
            private String beginDate;    // this attribute is oamBeginDate
            private String endDate;        // this attribute is oamEndDate
            private String beginLockDate;// this attribute is oamBeginLockDate
            private String endLockDate;    // this attribute is oamEndLockDate
            private String dayMask;        // this attribute is oamDayMask
            private int timeout;        // this attribute is oamTimeOut
            */
        Example entity = new Example();
        entity.setId(com.jts.fortress.ldap.DaoUtil.getAttribute(le, GlobalIds.FT_IID));
        entity.setName(com.jts.fortress.ldap.DaoUtil.getAttribute(le, EIds.EXAMPLE_NM));
        entity.setDescription(com.jts.fortress.ldap.DaoUtil.getAttribute(le, GlobalIds.DESC));
        DaoUtil.unloadTemporal(le, entity);
        return entity;
    }
}

