/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ldap;

import com.jts.fortress.FortEntity;
import com.jts.fortress.constants.GlobalIds;
import com.jts.fortress.hier.Relationship;
import com.jts.fortress.util.time.Constraint;
import com.jts.fortress.util.AlphabeticalOrder;
import com.jts.fortress.hier.Hier;
import com.jts.fortress.util.time.CUtil;

import com.jts.fortress.util.attr.VUtil;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttribute;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPControl;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPDN;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPReferralException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchConstraints;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchResults;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModification;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class contains static utilities to perform low-level entity to ldap persistence.  These methods are called by the
 * Fortress DAO's, i.e. {@link com.jts.fortress.rbac.UserDAO}. {@link com.jts.fortress.rbac.RoleDAO}, {@link com.jts.fortress.rbac.PermDAO}, ....
 * These are low-level data utilities and no validations are performed.  These APIs are not intended for outside programs.
 * <p/>
 * This class is thread safe.
 * <p/>

 *
 * @author smckinn
 * @created August 30, 2009
 */
public class DaoUtil
{
    private static final String OPENLDAP_PROXY_CONTROL = "2.16.840.1.113730.3.4.18";
    private static final int MAX_DEPTH = 100;
    private static final String OCLS_NM = DaoUtil.class.getName();
    private static final Logger log = Logger.getLogger(OCLS_NM);

    /**
     * Read the ldap record from specified location.
     *
     * @param ld   handle to ldap connection.
     * @param dn   contains ldap distinguished name.
     * @param atrs array contains array names to pull back.
     * @return ldap entry.
     * @throws LDAPException in the event system error occurs.
     */
    public static LDAPEntry read(LDAPConnection ld, String dn, String[] atrs)
        throws LDAPException
    {
        return ld.read(dn, atrs);
    }

    /**
     * Add a new ldap entry to the directory.  Do not add audit context.
     *
     * @param ld    handle to ldap connection.
     * @param entry contains data to add..
     * @throws LDAPException in the event system error occurs.
     */
    public static void add(LDAPConnection ld, LDAPEntry entry)
        throws LDAPException
    {
        ld.add(entry);
    }

    /**
     * Add a new ldap entry to the directory.  Add audit context.
     *
     * @param ld     handle to ldap connection.
     * @param entry  contains data to add..
     * @param entity contains audit context.
     * @throws LDAPException in the event system error occurs.
     */
    public static void add(LDAPConnection ld, LDAPEntry entry, FortEntity entity)
        throws LDAPException
    {
        if (GlobalIds.IS_AUDIT && entity != null && entity.getAdminSession() != null)
        {
            LDAPAttributeSet attrs = entry.getAttributeSet();
            if (VUtil.isNotNullOrEmpty(entity.getAdminSession().getInternalUserId()))
            {
                attrs.add(new LDAPAttribute(GlobalIds.FT_MODIFIER, entity.getAdminSession().getInternalUserId()));
            }
            if (VUtil.isNotNullOrEmpty(entity.getModCode()))
            {
                attrs.add(new LDAPAttribute(GlobalIds.FT_MODIFIER_CODE, entity.getModCode()));
            }
            if (VUtil.isNotNullOrEmpty(entity.getModId()))
            {
                attrs.add(new LDAPAttribute(GlobalIds.FT_MODIFIER_ID, entity.getModId()));
            }
        }
        ld.add(entry);
    }

    /**
     * Update exiting ldap entry to the directory.  Do not add audit context.
     *
     * @param ld   handle to ldap connection.
     * @param dn   contains distinguished node of entry.
     * @param mods contains data to modify.
     * @throws LDAPException in the event system error occurs.
     */
    public static void modify(LDAPConnection ld, String dn, LDAPModificationSet mods)
        throws LDAPException
    {
        ld.modify(dn, mods);
    }

    /**
     * Update exiting ldap entry to the directory.  Add audit context.
     *
     * @param ld     handle to ldap connection.
     * @param dn     contains distinguished node of entry.
     * @param mods   contains data to modify.
     * @param entity contains audit context.
     * @throws LDAPException in the event system error occurs.
     */
    public static void modify(LDAPConnection ld, String dn, LDAPModificationSet mods, FortEntity entity)
        throws LDAPException
    {
        audit(mods, entity);
        ld.modify(dn, mods);
    }

    /**
     * Delete exiting ldap entry from the directory.  Do not add audit context.
     *
     * @param ld handle to ldap connection.
     * @param dn contains distinguished node of entry targeted for removal..
     * @throws LDAPException in the event system error occurs.
     */
    public static void delete(LDAPConnection ld, String dn)
        throws LDAPException
    {
        ld.delete(dn);
    }

    /**
     * Delete exiting ldap entry from the directory.  Add audit context.  This method will call modify prior to delete which will
     * force corresponding audit record to be written to slapd access log.
     *
     * @param ld     handle to ldap connection.
     * @param dn     contains distinguished node of entry targeted for removal..
     * @param entity contains audit context.
     * @throws LDAPException in the event system error occurs.
     */
    public static void delete(LDAPConnection ld, String dn, FortEntity entity)
        throws LDAPException
    {
        LDAPModificationSet mods = new LDAPModificationSet();
        audit(mods, entity);
        if (mods.size() > 0)
            modify(ld, dn, mods);
        ld.delete(dn);
    }

    /**
     * Delete exiting ldap entry and all descendants from the directory.  Do not add audit context.
     *
     * @param ld handle to ldap connection.
     * @param dn contains distinguished node of entry targeted for removal..
     * @throws LDAPException in the event system error occurs.
     */
    public static void deleteRecursive(LDAPConnection ld, String dn)
        throws LDAPException
    {
        int recursiveCount = 0;
        deleteRecursive(dn, ld, recursiveCount);
    }

    /**
     * Delete exiting ldap entry and all descendants from the directory.  Add audit context.  This method will call modify prior to delete which will
     * force corresponding audit record to be written to slapd access log.
     *
     * @param ld     handle to ldap connection.
     * @param dn     contains distinguished node of entry targeted for removal..
     * @param entity contains audit context.
     * @throws LDAPException in the event system error occurs.
     */
    public static void deleteRecursive(LDAPConnection ld, String dn, FortEntity entity)
        throws LDAPException
    {
        LDAPModificationSet mods = new LDAPModificationSet();
        audit(mods, entity);
        if (mods.size() > 0)
            modify(ld, dn, mods);
        deleteRecursive(ld, dn);
    }

    /**
     * Used to recursively remove all nodes up to record pointed to by dn attribute.
     *
     * @param dn             contains distinguished node of entry targeted for removal..
     * @param ld             handle to ldap connection.
     * @param recursiveCount keeps track of how many iterations have been performed.
     * @throws LDAPException in the event system error occurs.
     */
    private static void deleteRecursive(String dn, LDAPConnection ld, int recursiveCount)
        throws LDAPException
    {
        String method = "deleteRecursive";
        // Sanity check - only allow max tree depth of 100
        if (recursiveCount++ > MAX_DEPTH)
        {
            // too deep inside of a recursive sequence;
            String error = OCLS_NM + "." + method + " dn [" + dn + "] depth error in recursive";
            throw new LDAPException(error, LDAPException.OPERATION_ERROR);
        }

        String theDN;
        // Find child nodes
        LDAPSearchResults res = search(ld, dn, LDAPConnection.SCOPE_ONE, "objectclass=*", GlobalIds.NO_ATRS, false, 0);

        // Iterate over all entries under this entry
        while (res.hasMoreElements())
        {
            try
            {
                // Next directory entry
                LDAPEntry entry = res.next();
                theDN = entry.getDN();
                // continue down:
                deleteRecursive(theDN, ld, recursiveCount);
                recursiveCount--;
            }
            catch (LDAPReferralException lre)
            {
                // cannot continue;
                String error = OCLS_NM + "." + method + " dn [" + dn + "] caught LDAPReferralException=" + lre.errorCodeToString() + "=" + lre.getLDAPErrorMessage();
                throw new LDAPException(error, lre.getLDAPResultCode());
            }
            catch (LDAPException ldape)
            {
                // cannot continue;
                String error = OCLS_NM + "." + method + " dn [" + dn + "] caught LDAPException=" + ldape.errorCodeToString() + "=" + ldape.getLDAPErrorMessage();
                throw new LDAPException(error, ldape.getLDAPResultCode());
            }
        }
        // delete the node:
        delete(ld, dn);
    }

    /**
     * Add the audit context variables to the modfication set.
     *
     * @param mods   used to update ldap attributes.
     * @param entity contains audit context.
     * @throws LDAPException in the event of error with ldap client.
     */
    private static void audit(LDAPModificationSet mods, FortEntity entity)
        throws LDAPException
    {
        if (GlobalIds.IS_AUDIT && entity != null && entity.getAdminSession() != null)
        {
            if (VUtil.isNotNullOrEmpty(entity.getAdminSession().getInternalUserId()))
            {
                LDAPAttribute modifier = new LDAPAttribute(GlobalIds.FT_MODIFIER, entity.getAdminSession().getInternalUserId());
                mods.add(LDAPModification.REPLACE, modifier);
            }
            if (VUtil.isNotNullOrEmpty(entity.getModCode()))
            {
                LDAPAttribute modCode = new LDAPAttribute(GlobalIds.FT_MODIFIER_CODE, entity.getModCode());
                mods.add(LDAPModification.REPLACE, modCode);
            }
            if (VUtil.isNotNullOrEmpty(entity.getModId()))
            {
                LDAPAttribute modId = new LDAPAttribute(GlobalIds.FT_MODIFIER_ID, entity.getModId());
                mods.add(LDAPModification.REPLACE, modId);
            }
        }
    }

    /**
     * Perform normal ldap search accepting default batch size.
     *
     * @param ld        is LDAPConnection object used for all communication with host.
     * @param baseDn    contains address of distinguished name to begin ldap search
     * @param scope     indicates depth of search starting at basedn.  0 (base dn), 1 (one level down) or 2 (infinite) are valid values.
     * @param filter    contains the search criteria
     * @param atrs      is the requested list of attritubutes to return from directory search.
     * @param attrsOnly if true pull back attribute names only.
     * @return result set containing ldap entries returned from directory.
     * @throws LDAPException thrown in the event of error in ldap client or server code.
     */
    public static LDAPSearchResults search(LDAPConnection ld,
                                           String baseDn,
                                           int scope,
                                           String filter,
                                           String[] atrs,
                                           boolean attrsOnly)
        throws LDAPException
    {
        LDAPSearchResults result;
        result = ld.search(baseDn, scope, filter, atrs, attrsOnly);
        return result;
    }

    /**
     * Perform normal ldap search specifying default batch size.
     *
     * @param ld        is LDAPConnection object used for all communication with host.
     * @param baseDn    contains address of distinguished name to begin ldap search
     * @param scope     indicates depth of search starting at basedn.  0 (base dn), 1 (one level down) or 2 (infinite) are valid values.
     * @param filter    contains the search criteria
     * @param atrs      is the requested list of attritubutes to return from directory search.
     * @param attrsOnly if true pull back attribute names only.
     * @param batchSize Will block until this many entries are ready to return from server.  0 indicates to block until all results are ready.
     * @return result set containing ldap entries returned from directory.
     * @throws LDAPException thrown in the event of error in ldap client or server code.
     */
    public static LDAPSearchResults search(LDAPConnection ld,
                                           String baseDn,
                                           int scope,
                                           String filter,
                                           String[] atrs,
                                           boolean attrsOnly,
                                           int batchSize)
        throws LDAPException
    {
        LDAPSearchResults result;
        LDAPSearchConstraints ldCons = new LDAPSearchConstraints();
        // Returns the maximum number of search results that are to be returned; 0 means there is no limit.
        ldCons.setMaxResults(0);
        ldCons.setBatchSize(batchSize);
        result = ld.search(baseDn, scope, filter, atrs, attrsOnly, ldCons);
        return result;
    }


    /**
     * Perform normal ldap search specifying default batch size and max entries to return.
     *
     * @param ld         is LDAPConnection object used for all communication with host.
     * @param baseDn     contains address of distinguished name to begin ldap search
     * @param scope      indicates depth of search starting at basedn.  0 (base dn), 1 (one level down) or 2 (infinite) are valid values.
     * @param filter     contains the search criteria
     * @param atrs       is the requested list of attritubutes to return from directory search.
     * @param attrsOnly  if true pull back attribute names only.
     * @param batchSize  Will block until this many entries are ready to return from server.  0 indicates to block until all results are ready.
     * @param maxEntries specifies the maximum number of entries to return in this search query.
     * @return result set containing ldap entries returned from directory.
     * @throws LDAPException thrown in the event of error in ldap client or server code.
     */
    public static LDAPSearchResults search(LDAPConnection ld,
                                           String baseDn,
                                           int scope,
                                           String filter,
                                           String[] atrs,
                                           boolean attrsOnly,
                                           int batchSize,
                                           int maxEntries)
        throws LDAPException
    {
        LDAPSearchResults result;
        LDAPSearchConstraints ldCons = new LDAPSearchConstraints();
        // Returns the maximum number of search results that are to be returned;
        ldCons.setMaxResults(maxEntries);
        ldCons.setBatchSize(batchSize);
        result = ld.search(baseDn, scope, filter, atrs, attrsOnly, ldCons);
        return result;
    }

    /**
     * This method will search the directory and return at most one record.  If more than one record is found
     * an ldap exception will be thrown.
     *
     * @param ld        is LDAPConnection object used for all communication with host.
     * @param baseDn    contains address of distinguished name to begin ldap search
     * @param scope     indicates depth of search starting at basedn.  0 (base dn), 1 (one level down) or 2 (infinite) are valid values.
     * @param filter    contains the search criteria
     * @param atrs      is the requested list of attritubutes to return from directory search.
     * @param attrsOnly if true pull back attribute names only.
     * @return entry   containing target ldap node.
     * @throws LDAPException thrown in the event of error in ldap client or server code.
     */
    public static LDAPEntry searchNode(LDAPConnection ld,
                                       String baseDn,
                                       int scope, String filter,
                                       String[] atrs,
                                       boolean attrsOnly)
        throws LDAPException
    {
        LDAPSearchResults result = ld.search(baseDn, scope,
            filter, atrs, attrsOnly);
        if (result.getCount() > 1)
        {
            throw new LDAPException(LDAPException.OPERATION_ERROR + "Fortress Search criteria failed to return unique record for LDAP search of base DN [" + baseDn + "] filter [" + filter + "]");
        }
        return result.next();
    }

    /**
     * This search method uses OpenLDAP Proxy Authorization Control to assert arbitrary user identity onto connection.
     *
     * @param ld        is LDAPConnection object used for all communication with host.
     * @param baseDn    contains address of distinguished name to begin ldap search
     * @param scope     indicates depth of search starting at basedn.  0 (base dn), 1 (one level down) or 2 (infinite) are valid values.
     * @param filter    contains the search criteria
     * @param atrs      is the requested list of attritubutes to return from directory search.
     * @param attrsOnly if true pull back attribute names only.
     * @param userDn    string value represents the identity of user on who's behalf the request was initiated.  The value will be stored in openldap auditsearch record AuthZID's attribute.
     * @return
     * @throws LDAPException thrown in the event of error in ldap client or server code.
     */
    public static LDAPEntry searchNode(LDAPConnection ld,
                                       String baseDn,
                                       int scope,
                                       String filter,
                                       String[] atrs,
                                       boolean attrsOnly,
                                       String userDn)
        throws LDAPException, UnsupportedEncodingException
    {
        LDAPControl proxyCtl = new LDAPControl(OPENLDAP_PROXY_CONTROL, true, (GlobalIds.DN + ": " + userDn).getBytes(GlobalIds.UTF8));
        com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchConstraints opt = new com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchConstraints();
        opt.setServerControls(proxyCtl);
        LDAPSearchResults result = ld.search(baseDn, scope, filter, atrs, attrsOnly, opt);
        if (result.getCount() > 1)
        {
            throw new LDAPException(LDAPException.OPERATION_ERROR + "Fortress Search criteria failed to return unique record for LDAP search of base DN [" + baseDn + "] filter [" + filter + "]");
        }
        return result.next();
    }


    /**
     * Method wraps ldap client to return multi-occurring attribute values by name within a given entry and returns as a list of strings.
     *
     * @param entry         contains the target ldap entry.
     * @param attributeName name of ldap attribute to retrieve.
     * @return List of type string containing attribute values.
     * @throws LDAPException in the event of ldap client error.
     */
    public static List<String> getAttributes(LDAPEntry entry, String attributeName)
        throws LDAPException
    {
        List<String> attrValues = new ArrayList<String>();
        LDAPAttribute attr;
        Enumeration values;
        attr = entry.getAttribute(attributeName);
        if (attr != null)
        {
            values = attr.getStringValues();
        }
        else
        {
            return null;
        }
        if (values != null)
        {
            while (values.hasMoreElements())
            {
                attrValues.add((String) values.nextElement());
            }
        }
        return attrValues;
    }


    /**
     * Method wraps ldap client to return multi-occurring attribute values by name within a given entry and returns as a set of strings.
     *
     * @param entry         contains the target ldap entry.
     * @param attributeName name of ldap attribute to retrieve.
     * @return List of type string containing attribute values.
     * @throws LDAPException in the event of ldap client error.
     */
    public static Set<String> getAttributeSet(LDAPEntry entry, String attributeName)
        throws LDAPException
    {
        // create Set with case insensitive comparator:
        Set<String> attrValues = new TreeSet<String>(new AlphabeticalOrder());
        LDAPAttribute attr;
        Enumeration values;
        attr = entry.getAttribute(attributeName);
        if (attr != null)
        {
            values = attr.getStringValues();
        }
        else
        {
            return null;
        }
        if (values != null)
        {
            while (values.hasMoreElements())
            {
                attrValues.add((String) values.nextElement());
            }
        }
        return attrValues;
    }


    /**
     * Method wraps ldap client to return multi-occurring attribute values by name within a given entry and return as a list of type {@link com.jts.fortress.hier.Relationship}.
     *
     * @param entry         contains the target ldap entry.
     * @param attributeName name of ldap attribute to retrieve.
     * @return List of type {@link com.jts.fortress.hier.Relationship} containing parent-child relationships.
     * @throws LDAPException in the event of ldap client error.
     */
    public static List<Relationship> getRelationshipAttributes(LDAPEntry entry, String attributeName)
        throws LDAPException
    {
        List<Relationship> attrValues = new ArrayList<Relationship>();
        LDAPAttribute attr;
        Enumeration values;

        attr = entry.getAttribute(attributeName);
        if (attr != null)
        {
            values = attr.getStringValues();
        }
        else
        {
            return null;
        }
        if (values != null)
        {
            while (values.hasMoreElements())
            {
                String edge = (String) values.nextElement();
                int indx = edge.indexOf(GlobalIds.PROP_SEP);
                if (indx >= 1)
                {
                    // This LDAP attr is stored as a name-value pair separated by a ':'.
                    // Separate the parent from the child:
                    String child = edge.substring(0, indx);
                    String parent = edge.substring(indx + 1);

                    // Load the parent/child relationship values into a helper class:
                    Relationship rel = new Relationship(child, parent);
                    attrValues.add(rel);
                }
                else
                {
                    String warning = OCLS_NM + ".getRelAttributes detected incorrect data in role relationship field: " + edge;
                    log.warn(warning);
                }
            }
        }
        return attrValues;
    }


    /**
     * Method wraps ldap client to return attribute value by name within a given entry and returns as a string.
     *
     * @param entry         contains the target ldap entry.
     * @param attributeName name of ldap attribute to retrieve.
     * @return value contained in a string variable.
     * @throws LDAPException in the event of ldap client error.
     */
    public static String getAttribute(LDAPEntry entry, String attributeName)
        throws LDAPException
    {
        String attrValue = null;
        LDAPAttribute attr;
        Enumeration values;
        attr = entry.getAttribute(attributeName);
        if (attr != null)
        {
            values = attr.getStringValues();
        }
        else
        {
            return null;
        }
        if (values != null)
        {
            attrValue = (String) values.nextElement();
        }
        return attrValue;
    }


    /**
     * Method will retrieve the relative distinguished name from a distinguished name variable.
     *
     * @param dn contains ldap distinguished name.
     * @return rDn as string.
     * @throws LDAPException in the event of ldap client error.
     */
    public static String getRdn(String dn)
        throws LDAPException
    {
        String[] dnList;
        dnList = LDAPDN.explodeDN(dn, true);
        return dnList[0];
    }


    /**
     * Create multi-occurring ldap attribute given array of strings and attribute name.
     *
     * @param name   contains attribute name to create.
     * @param values array of string that contains attribute values.
     * @return LDAPAttribute containing multi-occurring attribute set.
     * @throws LDAPException in the event of ldap client error.
     */
    public static LDAPAttribute createAttributes(String name, String values[])
        throws LDAPException
    {
        LDAPAttribute attr = new LDAPAttribute(name);
        for (int i = 0; i < values.length; i++)
        {
            VUtil.encodeSafeText(values[i], values[i].length());
            attr.addValue(values[i]);
        }
        return attr;
    }


    /**
     * Create ldap attribute given an attribute name and value.
     *
     * @param name  contains attribute name to create.
     * @param value string contains attribute value.
     * @return LDAPAttribute containing new ldap attribute.
     * @throws LDAPException in the event of ldap client error.
     */
    public static LDAPAttribute createAttribute(String name, String value)
        throws LDAPException
    {
        LDAPAttribute attr = new LDAPAttribute(name);
        VUtil.encodeSafeText(value, value.length());
        attr.addValue(value);
        return attr;
    }


    /**
     * Convert constraint from raw ldap format to application entity.
     *
     * @param le          ldap entry containing constraint.
     * @param ftDateTime reference to {@link com.jts.fortress.util.time.Constraint} containing formatted data.
     * @throws LDAPException in the event of ldap client error.
     */
    public static void unloadTemporal(LDAPEntry le, Constraint ftDateTime)
        throws LDAPException
    {

        String szRawData = getAttribute(le, GlobalIds.CONSTRAINT);
        if (szRawData != null && szRawData.length() > 0)
        {
            CUtil.setConstraint(szRawData, ftDateTime);
        }
    }


    /**
     * Given an ldap attribute name and a list of attribute values, construct an ldap attribute set to be added to directory.
     *
     * @param list     list of type string containing attribute values to load into attribute set.
     * @param attrs    contains ldap attribute set targeted for adding.
     * @param attrName name of ldap attribute being added.
     */
    public static void loadAttrs(List<String> list, LDAPAttributeSet attrs, String attrName)
    {
        if (list != null && list.size() > 0)
        {
            LDAPAttribute attr = null;
            for (String val : list)
            {
                if (attr == null)
                {
                    attr = new LDAPAttribute(attrName, val);
                }
                else
                {
                    attr.addValue(val);
                }
            }
            if (attr != null)
            {
                attrs.add(attr);
            }
        }
    }


    /**
     * Given a collection of {@link com.jts.fortress.hier.Relationship}, convert to raw data name-value format and load into ldap attribute set in preparation for ldap add.
     *
     * @param list     contains List of type {@link com.jts.fortress.hier.Relationship} targeted for adding to ldap.
     * @param attrs    collection of ldap attributes containing parent-child relationships in raw ldap format.
     * @param attrName contains the name of the ldap attribute to be added.
     */
    public static void loadRelationshipAttrs(List<Relationship> list, LDAPAttributeSet attrs, String attrName)
    {
        if (list != null)
        {
            LDAPAttribute attr = null;
            for (Relationship rel : list)
            {
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                if (attr == null)
                {
                    attr = new LDAPAttribute(attrName, rel.getChild() + GlobalIds.PROP_SEP + rel.getParent());
                }
                else
                {
                    attr.addValue(rel.getChild() + GlobalIds.PROP_SEP + rel.getParent());
                }
            }
            if (attr != null)
            {
                attrs.add(attr);
            }
        }
    }


    /**
     * Given an ldap attribute name and a set of attribute values, construct an ldap attribute set to be added to directory.
     *
     * @param values   set of type string containing attribute values to load into attribute set.
     * @param attrs    contains ldap attribute set targeted for adding.
     * @param attrName name of ldap attribute being added.
     */
    public static void loadAttrs(Set<String> values, LDAPAttributeSet attrs, String attrName)
    {
        if (values != null && values.size() > 0)
        {
            LDAPAttribute attr = null;
            for (String value : values)
            {
                if (attr == null)
                {
                    attr = new LDAPAttribute(attrName, value);
                }
                else
                {
                    attr.addValue(value);
                }
            }
            if (attr != null)
            {
                attrs.add(attr);
            }
        }
    }


    /**
     * Given an ldap attribute name and a list of attribute values, construct an ldap modification set to be updated in directory.
     *
     * @param list     list of type string containing attribute values to load into modification set.
     * @param mods     contains ldap modification set targeted for updating.
     * @param attrName name of ldap attribute being modified.
     */
    public static void loadAttrs(List<String> list, LDAPModificationSet mods, String attrName)
    {
        if (list != null && list.size() > 0)
        {
            LDAPAttribute attr = new LDAPAttribute(attrName);
            mods.add(LDAPModification.REPLACE, attr);
            for (String val : list)
            {
                attr = new LDAPAttribute(attrName, val);
                mods.add(LDAPModification.ADD, attr);
            }
        }
    }


    /**
     * Given a collection of {@link com.jts.fortress.hier.Relationship}s, convert to raw data name-value format and load into ldap modification set in preparation for ldap modify.
     *
     * @param list     contains List of type {@link com.jts.fortress.hier.Relationship} targeted for updating in ldap.
     * @param mods     ldap modification set containing parent-child relationships in raw ldap format.
     * @param attrName contains the name of the ldap attribute to be updated.
     * @param op       specifies type of mod: {@link Hier.Op#ADD}, {@link com.jts.fortress.hier.Hier.Op#MOD}, {@link Hier.Op#REM}
     */
    public static void loadRelationshipAttrs(List<Relationship> list, LDAPModificationSet mods, String attrName, Hier.Op op)
    {
        if (list != null)
        {
            LDAPAttribute attr;
            for (Relationship rel : list)
            {
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                attr = new LDAPAttribute(attrName, rel.getChild() + GlobalIds.PROP_SEP + rel.getParent());
                switch (op)
                {
                    case ADD:
                        mods.add(LDAPModification.ADD, attr);
                        break;
                    case MOD:
                        mods.add(LDAPModification.REPLACE, attr);
                        break;
                    case REM:
                        mods.add(LDAPModification.DELETE, attr);
                        break;
                }
            }
        }
    }


    /**
     * Given an ldap attribute name and a set of attribute values, construct an ldap modification set to be updated in directory.
     *
     * @param values   set of type string containing attribute values to load into modification set.
     * @param mods     contains ldap modification set targeted for updating.
     * @param attrName name of ldap attribute being updated.
     */
    public static void loadAttrs(Set<String> values, LDAPModificationSet mods, String attrName)
    {
        if (values != null && values.size() > 0)
        {
            LDAPAttribute attr = new LDAPAttribute(attrName);
            mods.add(LDAPModification.REPLACE, attr);
            for (String value : values)
            {
                attr = new LDAPAttribute(attrName, value);
                mods.add(LDAPModification.ADD, attr);
            }
        }
    }


    /**
     * Given a collection of {@link java.util.Properties}, convert to raw data name-value format and load into ldap modification set in preparation for ldap modify.
     *
     * @param props    contains {@link java.util.Properties} targeted for updating in ldap.
     * @param mods     ldap modification set containing name-value pairs in raw ldap format.
     * @param attrName contains the name of the ldap attribute to be updated.
     * @param replace  boolean variable, if set to true use {@link LDAPModification#REPLACE} else {@link LDAPModification#ADD}.
     */
    public static void loadProperties(Properties props, LDAPModificationSet mods, String attrName, boolean replace)
    {
        if (props != null && props.size() > 0)
        {
            LDAPAttribute prop = new LDAPAttribute(attrName);
            if (replace)
                mods.add(LDAPModification.REPLACE, prop);

            for (Enumeration e = props.propertyNames(); e.hasMoreElements();)
            {
                String key = (String) e.nextElement();
                String val = props.getProperty(key);
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                prop = new LDAPAttribute(attrName, key + GlobalIds.PROP_SEP + val);
                mods.add(LDAPModification.ADD, prop);
            }
        }
    }


    /**
     * Given a collection of {@link java.util.Properties}, convert to raw data name-value format and load into ldap modification set in preparation for ldap modify.
     *
     * @param props    contains {@link java.util.Properties} targeted for removal from ldap.
     * @param mods     ldap modification set containing name-value pairs in raw ldap format to be removed.
     * @param attrName contains the name of the ldap attribute to be removed.
     */
    public static void removeProperties(Properties props, LDAPModificationSet mods, String attrName)
    {
        if (props != null && props.size() > 0)
        {
            LDAPAttribute prop;
            for (Enumeration e = props.propertyNames(); e.hasMoreElements();)
            {
                String key = (String) e.nextElement();
                String val = props.getProperty(key);
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                prop = new LDAPAttribute(attrName, key + GlobalIds.PROP_SEP + val);
                mods.add(LDAPModification.DELETE, prop);
            }
        }
    }


    /**
     * Given a collection of {@link java.util.Properties}, convert to raw data name-value format and load into ldap modification set in preparation for ldap add.
     *
     * @param props    contains {@link java.util.Properties} targeted for adding to ldap.
     * @param attrs    ldap attribute set containing name-value pairs in raw ldap format.
     * @param attrName contains the name of the ldap attribute to be added.
     */
    public static void loadProperties(Properties props, LDAPAttributeSet attrs, String attrName)
    {
        if (props != null && props.size() > 0)
        {
            LDAPAttribute attr = null;
            for (Enumeration e = props.propertyNames(); e.hasMoreElements();)
            {
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                String key = (String) e.nextElement();
                String val = props.getProperty(key);
                String prop = key + GlobalIds.PROP_SEP + val;
                if (attr == null)
                {
                    attr = new LDAPAttribute(attrName, prop);
                }
                else
                {
                    attr.addValue(prop);
                }
            }
            if (attr != null)
            {
                attrs.add(attr);
            }
        }
    }
}