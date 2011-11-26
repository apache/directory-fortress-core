/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.arbac;

import com.jts.fortress.RemoveException;
import com.jts.fortress.UpdateException;
import com.jts.fortress.util.AlphabeticalOrder;
import com.jts.fortress.CreateException;
import com.jts.fortress.FinderException;

import com.jts.fortress.ldap.DaoUtil;
import com.jts.fortress.ldap.PoolMgr;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.util.attr.VUtil;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.configuration.Config;

import org.apache.log4j.Logger;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttribute;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModification;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchResults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class provides dataaccess to the OrgUnit datasets in LDAP.
 * <p/>
 * The OrgUnitDAO maintains the following structural and aux object classes:
 * <h4>1. organizationalUnit Structural Object Class is used to store basic attributes like ou and description</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.5.6.5 NAME 'organizationalUnit'</code>
 * <li> <code>DESC 'RFC2256: an organizational unit'</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST ou</code>
 * <li> <code>MAY ( userPassword $ searchGuide $ seeAlso $ businessCategory $</code>
 * <li> <code>x121Address $ registeredAddress $ destinationIndicator $</code>
 * <li> <code>preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $</code>
 * <li> <code>telephoneNumber $ internationaliSDNNumber $</code>
 * <li> <code>facsimileTelephoneNumber $ street $ postOfficeBox $ postalCode $</code>
 * <li> <code>postalAddress $ physicalDeliveryOfficeName $ st $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>2. ftOrgUnit Structural objectclass is used to store the OrgUnit internal id</h4>
 * <ul>                                                              com.jts.fortress.arbac.
 * <li>  ------------------------------------------
 * <li> <code> objectclass	( 1.3.6.1.4.1.38088.2.6</code>
 * <li> <code>NAME 'ftOrgUnit'</code>
 * <li> <code>DESC 'Fortress OrgUnit Class'</code>
 * <li> <code>SUP organizationalunit</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId ) )</code>
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

 * @author smckinn
 * @created September 18, 2010
 */
public class OrgUnitDAO

{
    private static final String OCLS_NM = OrgUnitDAO.class.getName();
    final private static Logger log = Logger.getLogger(OCLS_NM);

    private final static String ORGUNIT_OBJECT_CLASS_NM = "ftOrgUnit";

    private final static String ORGUNIT_OBJ_CLASS[] = {
        GlobalIds.TOP, ORGUNIT_OBJECT_CLASS_NM, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };
    private final static String[] ORGUNIT_ATRS = {
        GlobalIds.FT_IID, GlobalIds.OU, GlobalIds.DESC
    };

    private final static String[] ORGUNIT_ATR = {
        GlobalIds.OU
    };

    /**
     * Package private default constructor.
     */
    OrgUnitDAO(){}


    /**
     * @param entity
     * @return
     * @throws com.jts.fortress.CreateException
     *
     */
    OrgUnit create(OrgUnit entity)
        throws CreateException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity.getType(), entity.getName());
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add(DaoUtil.createAttributes(GlobalIds.OBJECT_CLASS, ORGUNIT_OBJ_CLASS));
            entity.setId();
            attrs.add(DaoUtil.createAttribute(GlobalIds.FT_IID, entity.getId()));
            if (entity.getDescription() != null && entity.getDescription().length() > 0)
                attrs.add(DaoUtil.createAttribute(GlobalIds.DESC, entity.getDescription()));
            // organizational name requires OU attribute:
            attrs.add(DaoUtil.createAttribute(GlobalIds.OU, entity.getName()));
            LDAPEntry myEntry = new LDAPEntry(dn, attrs);
            DaoUtil.add(ld, myEntry, entity);
        }
        catch (LDAPException e)
        {
            String error = OCLS_NM + ".create orgUnit name <" + entity.getName() + "> type <" + entity.getType() + "> root <" + dn + "> caught LDAPException=" + e;
            int errCode;
            if(entity.getType() == OrgUnit.Type.PERM)
            {
                errCode = GlobalErrIds.ORG_ADD_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_ADD_FAILED_USER;

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
     *
     */
    OrgUnit update(OrgUnit entity)
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity.getType(), entity.getName());
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            if (entity.getDescription() != null && entity.getDescription().length() > 0)
            {
                LDAPAttribute desc = new LDAPAttribute(GlobalIds.DESC, entity.getDescription());
                mods.add(LDAPModification.REPLACE, desc);
            }
            if (mods.size() > 0)
            {
                DaoUtil.modify(ld, dn, mods, entity);
            }
        }
        catch (LDAPException e)
        {
            String error = OCLS_NM + ".update orgUnit name <" + entity.getName() + "> type <" + entity.getType() + "> root <" + dn + "> caught LDAPException=" + e;
            int errCode;
            if(entity.getType() == OrgUnit.Type.PERM)
            {
                errCode = GlobalErrIds.ORG_UPDATE_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_UPDATE_FAILED_USER;

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
     * @return
     * @throws com.jts.fortress.RemoveException
     *
     */
    OrgUnit remove(OrgUnit entity)
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity.getType(), entity.getName());
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            DaoUtil.delete(ld, dn, entity);
        }
        catch (LDAPException e)
        {
            String error = OCLS_NM + ".remove orgUnit name <" + entity.getName() + "> type <" + entity.getType() + "> root <" + dn + "> caught LDAPException=" + e;
            int errCode;
            if(entity.getType() == OrgUnit.Type.PERM)
            {
                errCode = GlobalErrIds.ORG_DELETE_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_DELETE_FAILED_USER;

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
     * @param entity
     * @return
     * @throws FinderException
     *
     */
    OrgUnit findByKey(OrgUnit entity)
        throws FinderException
    {
        OrgUnit oe = null;
        LDAPConnection ld = null;
        String dn = getDn(entity.getType(), entity.getName());
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPEntry findEntry = DaoUtil.read(ld, dn, ORGUNIT_ATRS);
            oe = getEntityFromLdapEntry(findEntry, 0);
            if (entity == null)
            {
                String warning = OCLS_NM + ".findByKey orgUnit name <" + entity.getName() + "> type <" + entity.getType() + "> COULD NOT FIND ENTRY for dn <" + dn + ">";
                int errCode;
                if(entity.getType() == OrgUnit.Type.PERM)
                {
                    errCode = GlobalErrIds.ORG_NOT_FOUND_PERM;
                }
                else
                {
                    errCode = GlobalErrIds.ORG_NOT_FOUND_USER;

                }
                throw new FinderException(errCode, warning);
            }
        }
        catch (LDAPException e)
        {
            if (e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT)
            {
                String warning = OCLS_NM + ".findByKey orgUnit name <" + entity.getName() + "> type <" + entity.getType() + "> COULD NOT FIND ENTRY for dn <" + dn + ">";
                int errCode;
                if(entity.getType() == OrgUnit.Type.PERM)
                {
                    errCode = GlobalErrIds.ORG_NOT_FOUND_PERM;
                }
                else
                {
                    errCode = GlobalErrIds.ORG_NOT_FOUND_USER;

                }
                throw new FinderException(errCode, warning);
            }
            else
            {
                String error = OCLS_NM + ".findByKey orgUnitName <" + entity.getName() + "> type <" + entity.getType() + "> dn <" + dn + "> caught LDAPException=" + e;
                int errCode;
                if(entity.getType() == OrgUnit.Type.PERM)
                {
                    errCode = GlobalErrIds.ORG_READ_FAILED_PERM;
                }
                else
                {
                    errCode = GlobalErrIds.ORG_READ_FAILED_USER;

                }
                throw new FinderException(errCode, error, e);
            }
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return oe;
    }


    /**
     * @param searchVal
     * @return
     * @throws com.jts.fortress.FinderException
     *
     */
    List<OrgUnit> findOrgs(OrgUnit.Type type, String searchVal)
        throws FinderException
    {
        List<OrgUnit> orgUnitList = new ArrayList<OrgUnit>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String orgUnitRoot = getRootDn(type);
        try
        {
            searchVal = VUtil.encodeSafeText(searchVal, GlobalIds.ROLE_LEN);
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            String filter = "(&(objectclass=" + ORGUNIT_OBJECT_CLASS_NM + ")("
                + GlobalIds.OU + "=" + searchVal + "*))";
            searchResults = DaoUtil.search(ld, orgUnitRoot,
                LDAPConnection.SCOPE_ONE, filter, ORGUNIT_ATRS, false, GlobalIds.BATCH_SIZE);
            long sequence = 0;
            while (searchResults.hasMoreElements())
            {
                orgUnitList.add(getEntityFromLdapEntry(searchResults.next(), sequence++));
            }
        }
        catch (LDAPException e)
        {
            String error = OCLS_NM + ".findOrgs search val <" + searchVal + "> type <" + type + "> root <" + orgUnitRoot + "> caught LDAPException=" + e;
            int errCode;
            if(type == OrgUnit.Type.PERM)
            {
                errCode = GlobalErrIds.ORG_SEARCH_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_SEARCH_FAILED_USER;
            }

            throw new FinderException(errCode, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return orgUnitList;
    }


    /**
     *
     * @param type
     * @return
     * @throws FinderException
     */
    Set<String> getOrgs(OrgUnit.Type type)
        throws FinderException
    {
        Set<String> ouSet = new TreeSet<String>(new AlphabeticalOrder());
        LDAPConnection ld = null;
        String orgUnitRoot = getRootDn(type);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            String filter = "(objectclass=" + ORGUNIT_OBJECT_CLASS_NM + ")";
            LDAPSearchResults searchResults = DaoUtil.search(ld, orgUnitRoot,
                LDAPConnection.SCOPE_ONE, filter, ORGUNIT_ATR, false, GlobalIds.BATCH_SIZE);
            while (searchResults.hasMoreElements())
            {
                ouSet.add(DaoUtil.getAttribute(searchResults.next(), GlobalIds.OU));
            }
        }
        catch (LDAPException e)
        {
            String error = OCLS_NM + ".getOrgs type <" + type + "> root <" + orgUnitRoot + "> caught LDAPException=" + e;
            int errCode;
            if(type == OrgUnit.Type.PERM)
            {
                errCode = GlobalErrIds.ORG_GET_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_GET_FAILED_USER;
            }
            throw new FinderException(errCode, error, e);
        }
        finally
        {
            PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
        }
        return ouSet;
    }


    private String getDn(OrgUnit.Type type, String name)
    {
        return GlobalIds.OU + "=" + name + "," + getRootDn(type);
    }

    /**
     * @param type
     * @return
     */
    private String getRootDn(OrgUnit.Type type)
    {
        String dn = null;
        switch (type)
        {
            case USER:
                dn = Config.getProperty(GlobalIds.OSU_ROOT);
                break;
            case PERM:
                dn = Config.getProperty(GlobalIds.PSU_ROOT);
                break;
            default:
                String warning = OCLS_NM + ".getRootDn invalid type";
                log.warn(warning);
                break;
        }
        return dn;
    }

    /**
     * @param le
     * @return
     * @throws LDAPException
     */
    private OrgUnit getEntityFromLdapEntry(LDAPEntry le, long sequence)
        throws LDAPException
    {
        OrgUnit entity = new OrgUnit();
        entity.setSequenceId(sequence);
        entity.setId(DaoUtil.getAttribute(le, GlobalIds.FT_IID));
        entity.setName(DaoUtil.getAttribute(le, GlobalIds.OU));
        entity.setDescription(DaoUtil.getAttribute(le, GlobalIds.DESC));
        String dn = le.getDN();
        if (dn.indexOf(getRootDn(OrgUnit.Type.PERM)) != -1)
            entity.setType(OrgUnit.Type.PERM);
        else
            entity.setType(OrgUnit.Type.USER);
        return entity;
    }
}