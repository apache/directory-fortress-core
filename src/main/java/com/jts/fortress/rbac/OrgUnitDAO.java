/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.*;
import com.jts.fortress.ldap.DataProvider;

import com.jts.fortress.ldap.PoolMgr;
import com.jts.fortress.GlobalErrIds;
import com.jts.fortress.GlobalIds;

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
 *
 * @author Shawn McKinney
 * @created September 18, 2010
 */
final class OrgUnitDAO extends DataProvider

{
    private static final String CLS_NM = OrgUnitDAO.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);
    private static final String ORGUNIT_OBJECT_CLASS_NM = "ftOrgUnit";

    private static final String ORGUNIT_OBJ_CLASS[] = {
        GlobalIds.TOP, ORGUNIT_OBJECT_CLASS_NM, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };
    private static final String[] ORGUNIT_ATRS = {
        GlobalIds.FT_IID, GlobalIds.OU, GlobalIds.DESC, GlobalIds.PARENT_NODES
    };

    private static final String[] ORGUNIT_ATR = {
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
    final OrgUnit create(OrgUnit entity)
        throws CreateException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add(createAttributes(GlobalIds.OBJECT_CLASS, ORGUNIT_OBJ_CLASS));
            entity.setId();
            attrs.add(createAttribute(GlobalIds.FT_IID, entity.getId()));
            if (entity.getDescription() != null && entity.getDescription().length() > 0)
                attrs.add(createAttribute(GlobalIds.DESC, entity.getDescription()));
            // organizational name requires OU attribute:
            attrs.add(createAttribute(GlobalIds.OU, entity.getName()));

            // These multi-valued attributes are optional.  The utility function will return quietly if no items are loaded into collection:
            loadAttrs(entity.getParents(), attrs, GlobalIds.PARENT_NODES);

            LDAPEntry myEntry = new LDAPEntry(dn, attrs);
            add(ld, myEntry, entity);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".create orgUnit name [" + entity.getName() + "] type [" + entity.getType() + "] root [" + dn + "] caught LDAPException=" + e;
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
    final OrgUnit update(OrgUnit entity)
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPModificationSet mods = new LDAPModificationSet();
            if (entity.getDescription() != null && entity.getDescription().length() > 0)
            {
                LDAPAttribute desc = new LDAPAttribute(GlobalIds.DESC, entity.getDescription());
                mods.add(LDAPModification.REPLACE, desc);
            }
            loadAttrs(entity.getParents(), mods, GlobalIds.PARENT_NODES);
            if (mods.size() > 0)
            {
                modify(ld, dn, mods, entity);
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".update orgUnit name [" + entity.getName() + "] type [" + entity.getType() + "] root [" + dn + "] caught LDAPException=" + e;
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
    final OrgUnit remove(OrgUnit entity)
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = getDn(entity);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            delete(ld, dn, entity);
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".remove orgUnit name [" + entity.getName() + "] type [" + entity.getType() + "] root [" + dn + "] caught LDAPException=" + e;
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
    final OrgUnit findByKey(OrgUnit entity)
        throws FinderException
    {
        OrgUnit oe = null;
        LDAPConnection ld = null;
        String dn = getDn(entity);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            LDAPEntry findEntry = read(ld, dn, ORGUNIT_ATRS);
            oe = getEntityFromLdapEntry(findEntry, 0, entity.getContextId());
            if (entity == null)
            {
                String warning = CLS_NM + ".findByKey orgUnit name [" + entity.getName() + "] type [" + entity.getType() + "] COULD NOT FIND ENTRY for dn [" + dn + "]";
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
                String warning = CLS_NM + ".findByKey orgUnit name [" + entity.getName() + "] type [" + entity.getType() + "] COULD NOT FIND ENTRY for dn [" + dn + "]";
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
                String error = CLS_NM + ".findByKey orgUnitName [" + entity.getName() + "] type [" + entity.getType() + "] dn [" + dn + "] caught LDAPException=" + e;
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
     * @param orgUnit
     * @return
     * @throws com.jts.fortress.FinderException
     *
     */
    final List<OrgUnit> findOrgs(OrgUnit orgUnit)
        throws FinderException
    {
        List<OrgUnit> orgUnitList = new ArrayList<OrgUnit>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String orgUnitRoot = getOrgRoot(orgUnit);
        try
        {
            String searchVal = encodeSafeText(orgUnit.getName(), GlobalIds.ROLE_LEN);
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            String filter = GlobalIds.FILTER_PREFIX + ORGUNIT_OBJECT_CLASS_NM + ")("
                + GlobalIds.OU + "=" + searchVal + "*))";
            searchResults = search(ld, orgUnitRoot,
                LDAPConnection.SCOPE_ONE, filter, ORGUNIT_ATRS, false, GlobalIds.BATCH_SIZE);
            long sequence = 0;
            while (searchResults.hasMoreElements())
            {
                orgUnitList.add(getEntityFromLdapEntry(searchResults.next(), sequence++, orgUnit.getContextId()));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".findOrgs search val [" + orgUnit.getName() + "] type [" + orgUnit.getType() + "] root [" + orgUnitRoot + "] caught LDAPException=" + e;
            int errCode;
            if(orgUnit.getType() == OrgUnit.Type.PERM)
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
     * @param orgUnit
     * @return
     * @throws FinderException
     */
    final Set<String> getOrgs(OrgUnit orgUnit)
        throws FinderException
    {
        Set<String> ouSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        LDAPConnection ld = null;
        String orgUnitRoot = getOrgRoot(orgUnit);
        try
        {
            ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
            String filter = "(objectclass=" + ORGUNIT_OBJECT_CLASS_NM + ")";
            LDAPSearchResults searchResults = search(ld, orgUnitRoot,
                LDAPConnection.SCOPE_ONE, filter, ORGUNIT_ATR, false, GlobalIds.BATCH_SIZE);
            while (searchResults.hasMoreElements())
            {
                ouSet.add(getAttribute(searchResults.next(), GlobalIds.OU));
            }
        }
        catch (LDAPException e)
        {
            String error = CLS_NM + ".getOrgs type [" + orgUnit.getType() + "] root [" + orgUnitRoot + "] caught LDAPException=" + e;
            int errCode;
            if(orgUnit.getType() == OrgUnit.Type.PERM)
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

    /**
      *
      * @param orgUnit
      * @return
      * @throws FinderException
      */
     final List<Graphable> getAllDescendants(OrgUnit orgUnit)
         throws FinderException
     {
         String orgUnitRoot = getOrgRoot(orgUnit);
         String[] DESC_ATRS = { GlobalIds.OU, GlobalIds.PARENT_NODES };
         List<Graphable> descendants = new ArrayList<Graphable>();
         LDAPConnection ld = null;
         LDAPSearchResults searchResults;
         String filter = null;
         try
         {
             ld = PoolMgr.getConnection(PoolMgr.ConnType.ADMIN);
             filter = GlobalIds.FILTER_PREFIX + ORGUNIT_OBJECT_CLASS_NM + ")("
                 + GlobalIds.PARENT_NODES + "=*))";
             searchResults = search(ld, orgUnitRoot,
                 LDAPConnection.SCOPE_ONE, filter, DESC_ATRS, false, GlobalIds.BATCH_SIZE);
             long sequence = 0;
             while (searchResults.hasMoreElements())
             {
                 descendants.add(unloadDescendants(searchResults.next(), sequence++, orgUnit.getContextId()));
             }
         }
         catch (LDAPException e)
         {
             String error = CLS_NM + ".getAllDescendants filter [" + filter + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
             throw new FinderException(GlobalErrIds.ARLE_SEARCH_FAILED, error, e);
         }
         finally
         {
             PoolMgr.closeConnection(ld, PoolMgr.ConnType.ADMIN);
         }
         return descendants;
     }

    /**
     * @param orgUnit
     * @return
     */
    private String getDn(OrgUnit orgUnit)
    {
        String dn = null;
        switch (orgUnit.type)
        {
            case USER:
                 dn = GlobalIds.OU + "=" + orgUnit.getName() + "," + getRootDn(orgUnit.getContextId(), GlobalIds.OSU_ROOT);
                break;
            case PERM:
                dn = GlobalIds.OU + "=" + orgUnit.getName() + "," + getRootDn(orgUnit.getContextId(), GlobalIds.PSU_ROOT);
                break;
            default:
                String warning = CLS_NM + ".getDn invalid type";
                log.warn(warning);
                break;
        }
        return dn;
    }

    /**
     *
     * @param orgUnit
     * @return
     */
    private String getOrgRoot(OrgUnit orgUnit)
    {
        String dn = null;
        switch (orgUnit.type)
        {
            case USER:
                dn = getRootDn(orgUnit.getContextId(), GlobalIds.OSU_ROOT);
                break;
            case PERM:
                dn = getRootDn(orgUnit.getContextId(), GlobalIds.PSU_ROOT);
                break;
            default:
                String warning = CLS_NM + ".getOrgRootDn invalid type";
                log.warn(warning);
                break;
        }
        return dn;
    }

    /**
    *
    * @param le
    * @param sequence
    * @param contextId
    * @return
    * @throws LDAPException
    */
   private Graphable unloadDescendants(LDAPEntry le, long sequence, String contextId)
       throws LDAPException
   {
       OrgUnit entity = new ObjectFactory().createOrgUnit();
       entity.setSequenceId(sequence);
       entity.setName(getAttribute(le, GlobalIds.OU));
       entity.setParents(getAttributeSet(le, GlobalIds.PARENT_NODES));
       return entity;
   }

    /**
     *
     * @param le
     * @param sequence
     * @param contextId
     * @return
     * @throws LDAPException
     */
    private OrgUnit getEntityFromLdapEntry(LDAPEntry le, long sequence, String contextId)
        throws LDAPException
    {
        OrgUnit entity = new ObjectFactory().createOrgUnit();
        entity.setSequenceId(sequence);
        entity.setId(getAttribute(le, GlobalIds.FT_IID));
        entity.setName(getAttribute(le, GlobalIds.OU));
        entity.setDescription(getAttribute(le, GlobalIds.DESC));
        String dn = le.getDN();
         if (dn.contains(getRootDn(contextId, GlobalIds.PSU_ROOT)))
        {
            entity.setType(OrgUnit.Type.PERM);
            //entity.setParents(PsoUtil.getParents(entity.getName().toUpperCase(), contextId));
            entity.setChildren(PsoUtil.getChildren(entity.getName().toUpperCase(), contextId));
        }
         else if (dn.contains(getRootDn(contextId, GlobalIds.OSU_ROOT)))
        {
            entity.setType(OrgUnit.Type.USER);
            //entity.setParents(UsoUtil.getParents(entity.getName().toUpperCase(), contextId));
            entity.setChildren(UsoUtil.getChildren(entity.getName().toUpperCase(), contextId));
        }
        entity.setParents(getAttributeSet(le, GlobalIds.PARENT_NODES));
        return entity;
    }
}