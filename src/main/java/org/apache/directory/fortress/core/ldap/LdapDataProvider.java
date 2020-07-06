/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core.ldap;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.directory.api.ldap.extras.controls.ppolicy.PasswordPolicy;
import org.apache.directory.api.ldap.extras.controls.ppolicy.PasswordPolicyImpl;
import org.apache.directory.api.ldap.extras.controls.ppolicy_impl.PasswordPolicyDecorator;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.SearchCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.DefaultAttribute;
import org.apache.directory.api.ldap.model.entry.DefaultModification;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.entry.Value;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.exception.LdapOperationErrorException;
import org.apache.directory.api.ldap.model.message.BindRequest;
import org.apache.directory.api.ldap.model.message.BindRequestImpl;
import org.apache.directory.api.ldap.model.message.BindResponse;
import org.apache.directory.api.ldap.model.message.CompareRequest;
import org.apache.directory.api.ldap.model.message.CompareRequestImpl;
import org.apache.directory.api.ldap.model.message.CompareResponse;
import org.apache.directory.api.ldap.model.message.Control;
import org.apache.directory.api.ldap.model.message.Response;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.apache.directory.api.ldap.model.message.SearchRequestImpl;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.message.controls.ProxiedAuthz;
import org.apache.directory.api.ldap.model.message.controls.ProxiedAuthzImpl;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.model.Constraint;
import org.apache.directory.fortress.core.model.ConstraintUtil;
import org.apache.directory.fortress.core.model.FortEntity;
import org.apache.directory.fortress.core.model.Hier;
import org.apache.directory.fortress.core.model.Relationship;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.LdapUtil;
import org.apache.directory.ldap.client.api.LdapConnection;


/**
 * Abstract class contains methods to perform low-level entity to ldap persistence.  These methods are called by the
 * Fortress DAO's, i.e. {@link org.apache.directory.fortress.core.impl.UserDAO}. {@link org.apache.directory.fortress.core.impl.RoleDAO},
 * {@link org.apache.directory.fortress.core.impl.PermDAO}, ....
 * These are low-level data utilities, very little if any data validations are performed here.
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public abstract class LdapDataProvider
{
    // Logging
    private static final String CLS_NM = LdapDataProvider.class.getName();
    private static final int MAX_DEPTH = 100;
    private static final LdapCounters COUNTERS = new LdapCounters();
    private static final PasswordPolicy PP_REQ_CTRL = new PasswordPolicyImpl();

    /**
     * Given a contextId and a fortress param name return the LDAP dn.
     *
     * @param contextId is to determine what sub-tree to use.
     * @param root      contains the fortress parameter name that corresponds with a particular LDAP container.
     * @return String contains the dn to use for operation.
     */
    protected static String getRootDn( String contextId, String root )
    {
        String szDn = Config.getInstance().getProperty( root );

        // The contextId must not be null, or "HOME" or "null"
        if ( StringUtils.isNotEmpty( contextId ) && !contextId.equalsIgnoreCase( GlobalIds.NULL ) && !contextId
            .equals( GlobalIds.HOME ) )
        {
            int idx = szDn.indexOf( Config.getInstance().getProperty( GlobalIds.SUFFIX ) );
            if ( idx > 0 )
            {
                // Found. The DN is ,ou=<contextId>,
                StringBuilder dn = new StringBuilder();
                dn.append( szDn.substring( 0, idx - 1 ) ).append( "," ).append( SchemaConstants.OU_AT ).append( "=" )
                    .append(
                        contextId ).append( "," ).append( szDn.substring( idx ) );

                return dn.toString();
            }
            else
            {
                return "";
            }
        }
        else
        {
            return szDn;
        }
    }

    /**
     * Given a contextId return the LDAP dn that includes the suffix.
     *
     * @param contextId is to determine what sub-tree to use.
     * @return String contains the dn to use for operation.
     */
    protected String getRootDn( String contextId )
    {
        StringBuilder dn = new StringBuilder();
        if ( StringUtils.isNotEmpty( contextId ) && !contextId.equalsIgnoreCase( GlobalIds.NULL ) && !contextId
            .equals( GlobalIds.HOME ) )
        {
            dn.append( SchemaConstants.OU_AT ).append( "=" ).append( contextId ).append( "," +
                "" ).append( Config.getInstance().getProperty( GlobalIds.SUFFIX ) );
        }
        else
        {
            dn.append( Config.getInstance().getProperty( GlobalIds.SUFFIX ) );
        }
        return dn.toString();
    }


    /**
     * Read the ldap record from specified location.
     *
     * @param connection handle to ldap connection.
     * @param dn         contains ldap distinguished name.
     * @param attrs      array contains array names to pull back.
     * @return ldap entry.
     * @throws LdapException in the event system error occurs.
     */
    protected Entry read( LdapConnection connection, String dn, String[] attrs ) throws LdapException
    {
        COUNTERS.incrementRead();

        return connection.lookup( dn, attrs );
    }


    /**
     * Read the ldap record from specified location.
     *
     * @param connection handle to ldap connection.
     * @param dn         contains ldap distinguished name.
     * @param attrs      array contains array names to pull back.
     * @return ldap entry.
     * @throws LdapException in the event system error occurs.
     */
    protected Entry read( LdapConnection connection, Dn dn, String[] attrs ) throws LdapException
    {
        COUNTERS.incrementRead();

        return connection.lookup( dn, attrs );
    }


    /**
     * Read the ldap record from specified location with user assertion.
     *
     * @param connection handle to ldap connection.
     * @param dn         contains ldap distinguished name.
     * @param attrs      array contains array names to pull back.                                        ,
     *                   PoolMgr.ConnType.USER
     * @param userDn     string value represents the identity of user on who's behalf the request was initiated.  The
     *                   value will be stored in openldap auditsearch record AuthZID's attribute.
     * @return ldap entry.
     * @throws LdapException                in the event system error occurs.
     * @throws UnsupportedEncodingException for search control errors.
     */
    protected Entry read( LdapConnection connection, String dn, String[] attrs, String userDn ) throws LdapException
    {
        COUNTERS.incrementRead();

        return connection.lookup( dn, attrs );
    }


    /**
     * Add a new ldap entry to the directory.  Do not add audit context.
     *
     * @param connection handle to ldap connection.
     * @param entry      contains data to add..
     * @throws LdapException in the event system error occurs.
     */
    protected void add( LdapConnection connection, Entry entry ) throws LdapException
    {
        COUNTERS.incrementAdd();
        connection.add( entry );
    }


    /**
     * Add a new ldap entry to the directory.  Add audit context.
     *
     * @param connection handle to ldap connection.
     * @param entry      contains data to add..
     * @param entity     contains audit context.
     * @throws LdapException in the event system error occurs.
     */
    protected void add( LdapConnection connection, Entry entry, FortEntity entity ) throws LdapException
    {
        COUNTERS.incrementAdd();

        if ( !Config.getInstance().isAuditDisabled() && ( entity != null ) && ( entity.getAdminSession() != null ) )
        {
            if ( StringUtils.isNotEmpty( entity.getAdminSession().getInternalUserId() ) )
            {
                entry.add( GlobalIds.FT_MODIFIER, entity.getAdminSession().getInternalUserId() );
            }

            if ( StringUtils.isNotEmpty( entity.getModCode() ) )
            {
                entry.add( GlobalIds.FT_MODIFIER_CODE, entity.getModCode() );
            }

            if ( StringUtils.isNotEmpty( entity.getModId() ) )
            {
                entry.add( GlobalIds.FT_MODIFIER_ID, entity.getModId() );
            }
        }

        connection.add( entry );
    }


    /**
     * Update exiting ldap entry to the directory.  Do not add audit context.
     *
     * @param connection handle to ldap connection.
     * @param dn         contains distinguished node of entry.
     * @param mods       contains data to modify.
     * @throws LdapException in the event system error occurs.
     */
    protected void modify( LdapConnection connection, String dn, List<Modification> mods ) throws LdapException
    {
        COUNTERS.incrementMod();
        connection.modify( dn, mods.toArray( new Modification[]{} ) );
    }


    /**
     * Update exiting ldap entry to the directory.  Do not add audit context.
     *
     * @param connection handle to ldap connection.
     * @param dn         contains distinguished node of entry.
     * @param mods       contains data to modify.
     * @throws LdapException in the event system error occurs.
     */
    protected void modify( LdapConnection connection, Dn dn, List<Modification> mods ) throws LdapException
    {
        COUNTERS.incrementMod();
        connection.modify( dn, mods.toArray( new Modification[]
            {} ) );
    }


    /**
     * Update exiting ldap entry to the directory.  Add audit context.
     *
     * @param connection handle to ldap connection.
     * @param dn         contains distinguished node of entry.
     * @param mods       contains data to modify.
     * @param entity     contains audit context.
     * @throws LdapException in the event system error occurs.
     */
    protected void modify( LdapConnection connection, String dn, List<Modification> mods,
        FortEntity entity ) throws LdapException
    {
        COUNTERS.incrementMod();
        audit( mods, entity );
        connection.modify( dn, mods.toArray( new Modification[]
            {} ) );
    }


    /**
     * Update exiting ldap entry to the directory.  Add audit context.
     *
     * @param connection handle to ldap connection.
     * @param dn         contains distinguished node of entry.
     * @param mods       contains data to modify.
     * @param entity     contains audit context.
     * @throws LdapException in the event system error occurs.
     */
    protected void modify( LdapConnection connection, Dn dn, List<Modification> mods,
        FortEntity entity ) throws LdapException
    {
        COUNTERS.incrementMod();
        audit( mods, entity );
        connection.modify( dn, mods.toArray( new Modification[]
            {} ) );
    }


    /**
     * Delete exiting ldap entry from the directory.  Do not add audit context.
     *
     * @param connection handle to ldap connection.
     * @param dn         contains distinguished node of entry targeted for removal..
     * @throws LdapException in the event system error occurs.
     */
    protected void delete( LdapConnection connection, String dn ) throws LdapException
    {
        COUNTERS.incrementDelete();
        connection.delete( dn );
    }


    /**
     * Delete exiting ldap entry from the directory.  Add audit context.  This method will call modify prior to
     * delete which will
     * force corresponding audit record to be written to slapd access log.
     *
     * @param connection handle to ldap connection.
     * @param dn         contains distinguished node of entry targeted for removal..
     * @param entity     contains audit context.
     * @throws LdapException in the event system error occurs.
     */
    protected void delete( LdapConnection connection, String dn, FortEntity entity ) throws LdapException
    {
        COUNTERS.incrementDelete();
        List<Modification> mods = new ArrayList<Modification>();
        audit( mods, entity );

        if ( mods.size() > 0 )
        {
            modify( connection, dn, mods );
        }

        connection.delete( dn );
    }


    /**
     * Delete exiting ldap entry from the directory.  Add audit context.  This method will call modify prior to
     * delete which will
     * force corresponding audit record to be written to slapd access log.
     *
     * @param connection handle to ldap connection.
     * @param dn         contains distinguished node of entry targeted for removal..
     * @param entity     contains audit context.
     * @throws LdapException in the event system error occurs.
     */
    protected void delete( LdapConnection connection, Dn dn, FortEntity entity ) throws LdapException
    {
        COUNTERS.incrementDelete();
        List<Modification> mods = new ArrayList<Modification>();
        audit( mods, entity );

        if ( mods.size() > 0 )
        {
            modify( connection, dn, mods );
        }

        connection.delete( dn );
    }


    /**
     * Delete exiting ldap entry and all descendants from the directory.  Do not add audit context.
     *
     * @param connection handle to ldap connection.
     * @param dn         contains distinguished node of entry targeted for removal..
     * @throws LdapException   in the event system error occurs.
     * @throws IOException
     * @throws CursorException
     */
    protected void deleteRecursive( LdapConnection connection, String dn ) throws LdapException, CursorException
    {
        int recursiveCount = 0;
        deleteRecursive( dn, connection, recursiveCount );
    }


    /**
     * Delete exiting ldap entry and all descendants from the directory.  Add audit context.  This method will call
     * modify prior to delete which will
     * force corresponding audit record to be written to slapd access log.
     *
     * @param connection handle to ldap connection.
     * @param dn         contains distinguished node of entry targeted for removal..
     * @param entity     contains audit context.
     * @throws LdapException   in the event system error occurs.
     * @throws CursorException
     */
    protected void deleteRecursive( LdapConnection connection, String dn, FortEntity entity ) throws LdapException,
        CursorException
    {
        List<Modification> mods = new ArrayList<Modification>();
        audit( mods, entity );

        if ( mods.size() > 0 )
        {
            modify( connection, dn, mods );
        }

        deleteRecursive( connection, dn );
    }


    /**
     * Used to recursively remove all nodes up to record pointed to by dn attribute.
     *
     * @param dn             contains distinguished node of entry targeted for removal..
     * @param connection     handle to ldap connection.
     * @param recursiveCount keeps track of how many iterations have been performed.
     * @throws LdapException   in the event system error occurs.
     * @throws CursorException
     */
    private void deleteRecursive( String dn, LdapConnection connection, int recursiveCount ) throws LdapException,
        CursorException
    {
        String method = "deleteRecursive";

        // Sanity check - only allow max tree depth of 100
        if ( recursiveCount++ > MAX_DEPTH )
        {
            // too deep inside of a recursive sequence;
            String error = "." + method + " dn [" + dn + "] depth error in recursive";
            throw new LdapOperationErrorException( error );
        }

        String theDN;

        // Find child nodes
        SearchCursor cursor = search( connection, dn, SearchScope.ONELEVEL, "(objectclass=*)",
            SchemaConstants.NO_ATTRIBUTE_ARRAY,
            false, 0 );

        // Iterate over all entries under this entry
        while ( cursor.next() )
        {
            try
            {
                // Next directory entry
                Entry entry = cursor.getEntry();
                theDN = entry.getDn().getName();
                // continue down:
                deleteRecursive( theDN, connection, recursiveCount );
                recursiveCount--;
            }
            catch ( LdapException le )
            {
                // cannot continue;
                String error = "." + method + " dn [" + dn + "] caught LdapException=" + le.getMessage();
                throw new LdapException( error );
            }
        }

        // delete the node:
        COUNTERS.incrementDelete();
        delete( connection, dn );
    }


    /**
     * Add the audit context variables to the modfication set.
     *
     * @param mods   used to update ldap attributes.
     * @param entity contains audit context.
     */
    private void audit( List<Modification> mods, FortEntity entity )
    {
        if ( !Config.getInstance().isAuditDisabled() && ( entity != null ) && ( entity.getAdminSession() != null ) )
        {
            if ( StringUtils.isNotEmpty( entity.getAdminSession().getInternalUserId() ) )
            {
                Modification modification = new DefaultModification( ModificationOperation.REPLACE_ATTRIBUTE,
                    GlobalIds.FT_MODIFIER, entity.getAdminSession().getInternalUserId() );
                mods.add( modification );
            }

            if ( StringUtils.isNotEmpty( entity.getModCode() ) )
            {
                Modification modification = new DefaultModification( ModificationOperation.REPLACE_ATTRIBUTE,
                    GlobalIds.FT_MODIFIER_CODE, entity.getModCode() );
                mods.add( modification );
            }

            if ( StringUtils.isNotEmpty( entity.getModId() ) )
            {
                Modification modification = new DefaultModification( ModificationOperation.REPLACE_ATTRIBUTE,
                    GlobalIds.FT_MODIFIER_ID, entity.getModId() );
                mods.add( modification );
            }
        }
    }


    /**
     * Perform normal ldap search accepting default batch size.
     *
     * @param connection is LdapConnection object used for all communication with host.
     * @param baseDn     contains address of distinguished name to begin ldap search
     * @param scope      indicates depth of search starting at basedn.  0 (base dn),
     *                   1 (one level down) or 2 (infinite) are valid values.
     * @param filter     contains the search criteria
     * @param attrs      is the requested list of attritubutes to return from directory search.
     * @param attrsOnly  if true pull back attribute names only.
     * @return result set containing ldap entries returned from directory.
     * @throws LdapException thrown in the event of error in ldap client or server code.
     */
    protected SearchCursor search( LdapConnection connection, String baseDn, SearchScope scope, String filter,
        String[] attrs, boolean attrsOnly ) throws LdapException
    {
        COUNTERS.incrementSearch();

        SearchRequest searchRequest = new SearchRequestImpl();
        searchRequest.setBase( new Dn( baseDn ) );
        searchRequest.setScope( scope );
        searchRequest.setFilter( filter );
        searchRequest.setTypesOnly( attrsOnly );
        searchRequest.addAttributes( attrs );

        return connection.search( searchRequest );
    }


    /**
     * Perform normal ldap search specifying default batch size and max entries to return.
     *
     * @param connection is LdapConnection object used for all communication with host.
     * @param baseDn     contains address of distinguished name to begin ldap search
     * @param scope      indicates depth of search starting at basedn.  0 (base dn),
     *                   1 (one level down) or 2 (infinite) are valid values.
     * @param filter     contains the search criteria
     * @param attrs      is the requested list of attritubutes to return from directory search.
     * @param attrsOnly  if true pull back attribute names only.
     * @param maxEntries specifies the maximum number of entries to return in this search query.
     * @return result set containing ldap entries returned from directory.
     * @throws LdapException thrown in the event of error in ldap client or server code.
     */
    protected SearchCursor search( LdapConnection connection, String baseDn, SearchScope scope, String filter,
        String[] attrs, boolean attrsOnly, int maxEntries ) throws LdapException
    {
        COUNTERS.incrementSearch();

        SearchRequest searchRequest = new SearchRequestImpl();

        searchRequest.setBase( new Dn( baseDn ) );
        searchRequest.setFilter( filter );
        searchRequest.setScope( scope );
        searchRequest.setSizeLimit( maxEntries );
        searchRequest.setTypesOnly( attrsOnly );
        searchRequest.addAttributes( attrs );

        return connection.search( searchRequest );
    }


    /**
     * This method will search the directory and return at most one record.  If more than one record is found
     * an ldap exception will be thrown.
     *
     * @param connection is LdapConnection object used for all communication with host.
     * @param baseDn     contains address of distinguished name to begin ldap search
     * @param scope      indicates depth of search starting at basedn.  0 (base dn),
     *                   1 (one level down) or 2 (infinite) are valid values.
     * @param filter     contains the search criteria
     * @param attrs      is the requested list of attritubutes to return from directory search.
     * @param attrsOnly  if true pull back attribute names only.
     * @return entry   containing target ldap node.
     * @throws LdapException   thrown in the event of error in ldap client or server code.
     * @throws CursorException If we weren't able to fetch an element from the search result
     */
    protected Entry searchNode( LdapConnection connection, String baseDn, SearchScope scope, String filter,
        String[] attrs, boolean attrsOnly ) throws LdapException, CursorException
    {
        SearchRequest searchRequest = new SearchRequestImpl();

        searchRequest.setBase( new Dn( baseDn ) );
        searchRequest.setFilter( filter );
        searchRequest.setScope( scope );
        searchRequest.setTypesOnly( attrsOnly );
        searchRequest.addAttributes( attrs );

        SearchCursor result = connection.search( searchRequest );

        Entry entry = result.getEntry();

        if ( result.next() )
        {
            throw new LdapException( "searchNode failed to return unique record for LDAP search of base DN [" +
                baseDn + "] filter [" + filter + "]" );
        }

        return entry;
    }


    /**
     * This search method uses OpenLDAP Proxy Authorization Control to assert arbitrary user identity onto connection.
     *
     * @param connection is LdapConnection object used for all communication with host.
     * @param baseDn     contains address of distinguished name to begin ldap search
     * @param scope      indicates depth of search starting at basedn.  0 (base dn),
     *                   1 (one level down) or 2 (infinite) are valid values.
     * @param filter     contains the search criteria
     * @param attrs      is the requested list of attritubutes to return from directory search.
     * @param attrsOnly  if true pull back attribute names only.
     * @param userDn     string value represents the identity of user on who's behalf the request was initiated.  The
     *                   value will be stored in openldap auditsearch record AuthZID's attribute.
     * @return entry   containing target ldap node.
     * @throws LdapException   thrown in the event of error in ldap client or server code.
     * @throws CursorException If we weren't able to fetch an element from the search result
     */
    protected Entry searchNode( LdapConnection connection, String baseDn, SearchScope scope, String filter,
        String[] attrs, boolean attrsOnly, String userDn ) throws LdapException, CursorException
    {
        COUNTERS.incrementSearch();

        SearchRequest searchRequest = new SearchRequestImpl();

        searchRequest.setBase( new Dn( baseDn ) );
        searchRequest.setFilter( filter );
        searchRequest.setScope( scope );
        searchRequest.setTypesOnly( attrsOnly );
        searchRequest.addAttributes( attrs );

        SearchCursor result = connection.search( searchRequest );

        Entry entry = result.getEntry();

        if ( result.next() )
        {
            throw new LdapException( "searchNode failed to return unique record for LDAP search of base DN [" +
                baseDn + "] filter [" + filter + "]" );
        }

        return entry;
    }


    /**
     * This method uses the compare ldap func to assert audit record into the directory server's configured audit
     * logger.
     *
     * This is for one reason - to force the ldap server to maintain an audit trail on checkAccess api.
     *
     * Use proxy authz control (RFC4370) to assert the caller's id onto the record.
     *
     * @param connection is LdapConnection object used for all communication with host.
     * @param dn         contains address of distinguished name to begin ldap search
     * @param userDn     dn for user node
     * @param attribute  attribute used for compare
     * @return true if compare operation succeeds
     * @throws LdapException                thrown in the event of error in ldap client or server code.
     * @throws UnsupportedEncodingException in the event the server cannot perform the operation.
     */
    protected boolean compareNode( LdapConnection connection, String dn, String userDn,
        Attribute attribute ) throws LdapException, UnsupportedEncodingException
    {
        COUNTERS.incrementCompare();

        CompareRequest compareRequest = new CompareRequestImpl();
        compareRequest.setName( new Dn( dn ) );
        compareRequest.setAttributeId( attribute.getId() );
        compareRequest.setAssertionValue( attribute.getString() );

        // Assert the end user's dn onto the reqest using proxy authZ control so openldap can log who the user was (for authZ audit trail)
        ProxiedAuthz proxiedAuthzControl = new ProxiedAuthzImpl();
        proxiedAuthzControl.setAuthzId( "dn: " + userDn );
        compareRequest.addControl( proxiedAuthzControl );
        CompareResponse response = connection.compare( compareRequest );
        return response.getLdapResult().getResultCode() == ResultCodeEnum.SUCCESS;
    }


    /**
     * Method wraps ldap client to return multivalued attribute by name within a given entry and returns
     * as a list of strings.
     *
     * @param entry         contains the target ldap entry.
     * @param attributeName name of ldap attribute to retrieve.
     * @return List of type string containing attribute values.
     */
    protected List<String> getAttributes( Entry entry, String attributeName )
    {
        List<String> attrValues = new ArrayList<>();
        if ( entry != null )
        {
            Attribute attr = entry.get( attributeName );
            if ( attr != null )
            {
                for ( Value<?> value : attr )
                {
                    attrValues.add( value.getString() );
                }
            }
            else
            {
                return null;
            }
        }

        return attrValues;
    }


    /**
     * Return the image stored on the entry.
     *
     * @param entry contains the image target.
     * @param attributeName to be retrieved.
     * @return byte array containing image.
     * @throws LdapInvalidAttributeValueException contains the system error.
     */
    protected byte[] getPhoto( Entry entry, String attributeName ) throws LdapInvalidAttributeValueException
    {
        byte[] photo = null;
        Attribute attr = entry.get( attributeName );

        if ( attr != null )
        {
            photo = attr.getBytes();
        }

        return photo;
    }


    /**
     * Method wraps ldap client to return multivalued attribute by name within a given entry and returns
     * as a set of strings.
     *
     * @param entry         contains the target ldap entry.
     * @param attributeName name of ldap attribute to retrieve.
     * @return List of type string containing attribute values.
     */
    protected Set<String> getAttributeSet( Entry entry, String attributeName )
    {
        // create Set with case insensitive comparator:
        Set<String> attrValues = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );

        if ( entry != null && entry.containsAttribute( attributeName ) )
        {
            for ( Value<?> value : entry.get( attributeName ) )
            {
                attrValues.add( value.getString() );
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
     * @throws LdapInvalidAttributeValueException When we weren't able to get the attribute from the entry
     */
    protected String getAttribute( Entry entry, String attributeName ) throws LdapInvalidAttributeValueException
    {
        if ( entry != null )
        {
            Attribute attr = entry.get( attributeName );

            if ( attr != null )
            {
                return attr.getString();
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }


    /**
     * Method will retrieve the relative distinguished name from a distinguished name variable.
     *
     * @param dn contains ldap distinguished name.
     * @return rDn as string.
     */
    protected String getRdn( String dn )
    {
        try
        {
            return new Dn( dn ).getRdn().getName();
        }
        catch ( LdapInvalidDnException lide )
        {
            return null;
        }
    }


    protected String getRdnValue( String dn )
    {
        try
        {
            return new Dn( dn ).getRdn().getNormValue();
        }
        catch ( LdapInvalidDnException lide )
        {
            return null;
        }
    }


    /**
     * Create multi-occurring ldap attribute given array of strings and attribute name.
     *
     * @param name   contains attribute name to create.
     * @param values array of string that contains attribute values.
     * @return Attribute containing multivalued attribute set.
     * @throws LdapException in the event of ldap client error.
     */
    protected Attribute createAttributes( String name, String values[] ) throws LdapException
    {
        return new DefaultAttribute( name, values );
    }


    /**
     * Convert constraint from raw ldap format to application entity.
     *
     * @param le         ldap entry containing constraint.
     * @param ftDateTime reference to {@link org.apache.directory.fortress.core.model.Constraint} containing formatted data.
     * @throws LdapInvalidAttributeValueException
     *
     * @throws LdapInvalidAttributeValueException when we weren't able to retrieve the attribute from the entry
     */
    protected void unloadTemporal( Entry le, Constraint ftDateTime ) throws LdapInvalidAttributeValueException
    {
        String szRawData = getAttribute( le, GlobalIds.CONSTRAINT );

        if ( szRawData != null && szRawData.length() > 0 )
        {
            ConstraintUtil.setConstraint( szRawData, ftDateTime );
        }
    }


    /**
     * Given an ldap attribute name and a list of attribute values, construct an ldap attribute set to be added to directory.
     *
     * @param list     list of type string containing attribute values to load into attribute set.
     * @param entry    contains ldap attribute set targeted for adding.
     * @param attrName name of ldap attribute being added.
     * @throws LdapException If we weren't able to add the attributes into the entry
     */
    protected void loadAttrs( List<String> list, Entry entry, String attrName ) throws LdapException
    {
        if ( list != null && list.size() > 0 )
        {
            entry.add( attrName, list.toArray( new String[]
                {} ) );
        }
    }


    /**
     * Given an ldap attribute name and a list of attribute values, construct an ldap modification set to be updated
     * in directory.
     *
     * @param list     list of type string containing attribute values to load into modification set.
     * @param mods     contains ldap modification set targeted for updating.
     * @param attrName name of ldap attribute being modified.
     */
    protected void loadAttrs( List<String> list, List<Modification> mods, String attrName )
    {
        if ( ( list != null ) && ( list.size() > 0 ) )
        {
            mods.add( new DefaultModification( ModificationOperation.REPLACE_ATTRIBUTE, attrName,
                list.toArray( new String[]
                    {} ) ) );
        }
    }


    /**
     * Given a collection of {@link org.apache.directory.fortress.core.model.Relationship}s, convert to raw data name-value format and
     * load into ldap modification set in preparation for ldap modify.
     *
     * @param list     contains List of type {@link org.apache.directory.fortress.core.model.Relationship} targeted for updating in ldap.
     * @param mods     ldap modification set containing parent-child relationships in raw ldap format.
     * @param attrName contains the name of the ldap attribute to be updated.
     * @param op       specifies type of mod: {@link org.apache.directory.fortress.core.model.Hier.Op#ADD},
     * {@link org.apache.directory.fortress.core.model.Hier.Op#MOD}, {@link org.apache.directory.fortress.core.model.Hier.Op#REM}.
     */
    protected void loadRelationshipAttrs( List<Relationship> list, List<Modification> mods, String attrName,
        Hier.Op op )
    {
        if ( list != null )
        {
            Attribute attr;

            for ( Relationship rel : list )
            {
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                attr = new DefaultAttribute( attrName, rel.getChild() + GlobalIds.PROP_SEP + rel.getParent() );

                switch ( op )
                {
                    case ADD:
                        mods.add( new DefaultModification( ModificationOperation.ADD_ATTRIBUTE, attr ) );
                        break;

                    case MOD:
                        mods.add( new DefaultModification( ModificationOperation.REPLACE_ATTRIBUTE, attr ) );
                        break;

                    case REM:
                        mods.add( new DefaultModification( ModificationOperation.REMOVE_ATTRIBUTE, attr ) );
                        break;
                }
            }
        }
    }


    /**
     * Given an ldap attribute name and a set of attribute values, construct an ldap modification set to be updated
     * in directory.
     *
     * @param values   set of type string containing attribute values to load into modification set.
     * @param mods     contains ldap modification set targeted for updating.
     * @param attrName name of ldap attribute being updated.
     */
    protected void loadAttrs( Set<String> values, List<Modification> mods, String attrName )
    {
        if ( ( values != null ) && ( values.size() > 0 ) )
        {
            mods.add( new DefaultModification( ModificationOperation.REPLACE_ATTRIBUTE, attrName,
                values.toArray( new String[]
                    {} ) ) );
        }
    }


    /**
     * Given an ldap attribute name and a set of attribute values, construct an ldap attribute set to be added to
     * directory.
     *
     * @param values   set of type string containing attribute values to load into attribute set.
     * @param entry    contains ldap entry to pull attrs from.
     * @param attrName name of ldap attribute being added.
     * @throws LdapException If we weren't able to add the values into the entry
     */
    protected void loadAttrs( Set<String> values, Entry entry, String attrName ) throws LdapException
    {
        if ( ( values != null ) && ( values.size() > 0 ) )
        {
            entry.add( attrName, values.toArray( new String[]
                {} ) );
        }
    }


    /**
     * Given a collection of {@link java.util.Properties}, convert to raw data name-value format and load into ldap
     * modification set in preparation for ldap modify.
     *
     * @param props    contains {@link java.util.Properties} targeted for updating in ldap.
     * @param mods     ldap modification set containing name-value pairs in raw ldap format.
     * @param attrName contains the name of the ldap attribute to be updated.
     * @param replace  boolean variable, if set to true use {@link ModificationOperation#REPLACE_ATTRIBUTE} else {@link
     * ModificationOperation#ADD_ATTRIBUTE}.
     */
    protected void loadProperties( Properties props, List<Modification> mods, String attrName, boolean replace )
    {
        loadProperties( props, mods, attrName, replace, GlobalIds.PROP_SEP );
    }


    /**
     * Given a collection of {@link java.util.Properties}, convert to raw data name-value format and load into ldap
     * modification set in preparation for ldap modify.
     *
     * @param props    contains {@link java.util.Properties} targeted for updating in ldap.
     * @param mods     ldap modification set containing name-value pairs in raw ldap format.
     * @param attrName contains the name of the ldap attribute to be updated.
     * @param replace  boolean variable, if set to true use {@link ModificationOperation#REPLACE_ATTRIBUTE} else {@link
     * ModificationOperation#ADD_ATTRIBUTE}.
     * @param separator contains the char value used to separate name and value in ldap raw format.
     */
    protected void loadProperties( Properties props, List<Modification> mods, String attrName, boolean replace,
        char separator )
    {
        if ( props != null && props.size() > 0 )
        {
            if ( replace )
            {
                mods.add( new DefaultModification( ModificationOperation.REPLACE_ATTRIBUTE, attrName ) );
            }

            for ( Enumeration<?> e = props.propertyNames(); e.hasMoreElements(); )
            {
                String key = ( String ) e.nextElement();
                String val = props.getProperty( key );
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                mods.add( new DefaultModification( ModificationOperation.ADD_ATTRIBUTE, attrName,
                    key + separator + val ) );
            }
        }
    }


    /**
     * Given a collection of {@link java.util.Properties}, convert to raw data name-value format and load into ldap
     * modification set in preparation for ldap modify.
     *
     * @param props    contains {@link java.util.Properties} targeted for removal from ldap.
     * @param mods     ldap modification set containing name-value pairs in raw ldap format to be removed.
     * @param attrName contains the name of the ldap attribute to be removed.
     */
    protected void removeProperties( Properties props, List<Modification> mods, String attrName )
    {
        if ( props != null && props.size() > 0 )
        {
            for ( Enumeration<?> e = props.propertyNames(); e.hasMoreElements(); )
            {
                String key = ( String ) e.nextElement();
                String val = props.getProperty( key );

                // This LDAP attr is stored as a name-value pair separated by a ':'.
                mods.add( new DefaultModification( ModificationOperation.REMOVE_ATTRIBUTE, attrName,
                    key + GlobalIds.PROP_SEP + val ) );
            }
        }
    }


    /**
     * Given a collection of {@link java.util.Properties}, convert to raw data name-value format and load into ldap
     * modification set in preparation for ldap add.
     *
     * @param props    contains {@link java.util.Properties} targeted for adding to ldap.
     * @param entry    contains ldap entry to pull attrs from.
     * @param attrName contains the name of the ldap attribute to be added.
     * @throws LdapException If we weren't able to add the properies into the entry
     */
    protected void loadProperties( Properties props, Entry entry, String attrName ) throws LdapException
    {
        if ( ( props != null ) && ( props.size() > 0 ) )
        {
            Attribute attr = new DefaultAttribute( attrName );

            for ( Enumeration<?> e = props.propertyNames(); e.hasMoreElements(); )
            {
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                String key = ( String ) e.nextElement();
                String val = props.getProperty( key );
                String prop = key + GlobalIds.PROP_SEP + val;

                attr.add( prop );
            }

            if ( attr.size() != 0 )
            {
                entry.add( attr );
            }
        }
    }


    /**
     * Given a collection of {@link java.util.Properties}, convert to raw data name-value format and load into ldap modification set in preparation for ldap add.
     *
     * @param props    contains {@link java.util.Properties} targeted for adding to ldap.
     * @param entry    contains ldap entry to push attrs into.
     * @param attrName contains the name of the ldap attribute to be added.
     * @param separator contains the char value used to separate name and value in ldap raw format.
     * @throws LdapException If we weren't able to add the properies into the entry
     */
    protected void loadProperties( Properties props, Entry entry, String attrName, char separator )
        throws LdapException
    {
        if ( ( props != null ) && ( props.size() > 0 ) )
        {
            Attribute attr = null;

            for ( Enumeration<?> e = props.propertyNames(); e.hasMoreElements(); )
            {
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                String key = ( String ) e.nextElement();
                String val = props.getProperty( key );
                String prop = key + separator + val;

                if ( attr == null )
                {
                    attr = new DefaultAttribute( attrName );
                }
                else
                {
                    attr.add( prop );
                }
            }

            if ( attr != null )
            {
                entry.add( attr );
            }
        }
    }


    /**
     * Encode some text so that it can be used in a LDAP filter.
     *
     * @param value The value to encode
     * @param validLen The maximum accepted length of the value. 
     * @return String containing encoded data.
     * @throws LdapException If the value is longer than the maximum value
     */
    protected String encodeSafeText( String value, int validLen ) throws LdapException
    {
        if ( StringUtils.isNotEmpty( value ) )
        {
            int length = value.length();

            if ( length > validLen )
            {
                String error = "encodeSafeText value [" + value + "] invalid length [" + length + "]";
                throw new LdapException( error );
            }

            if ( LdapUtil.getInstance().isLdapfilterSizeFound() )
            {
                value = escapeLDAPSearchFilter( value );
            }
        }

        return value;
    }


    /**
     * Get Password Policy Response Control from LDAP client.
     *
     * @param resp contains reference to LDAP pw policy response.
     * @return PasswordPolicy response control.
     */
    protected PasswordPolicy getPwdRespCtrl( Response resp )
    {
        Control control = resp.getControls().get( PP_REQ_CTRL.getOid() );
        if ( control == null )
        {
            return null;
        }

        return ( ( PasswordPolicyDecorator ) control ).getDecorated();
    }


    /**
     * Calls the PoolMgr to perform an LDAP bind for a user/password combination.  This function is valid
     * if and only if the user entity is a member of the USERS data set.
     *
     * @param connection connection to ldap server.
     * @param szUserDn   contains the LDAP dn to the user entry in String format.
     * @param password   contains the password in clear text.
     * @return bindResponse contains the result of the operation.
     * @throws LdapException in the event of LDAP error.
     */
    protected BindResponse bind( LdapConnection connection, String szUserDn, String password ) throws LdapException
    {
        COUNTERS.incrementBind();
        Dn userDn = new Dn( szUserDn );
        BindRequest bindReq = new BindRequestImpl();
        bindReq.setDn( userDn );
        bindReq.setCredentials( password );
        bindReq.addControl( PP_REQ_CTRL );
        return connection.bind( bindReq );
    }


    /**
     * Calls the PoolMgr to close the Admin LDAP connection.
     *
     * @param connection handle to ldap connection object.
     */
    public void closeAdminConnection( LdapConnection connection )
    {
        LdapConnectionProvider.getInstance().closeAdminConnection(connection);
    }


    /**
     * Calls the PoolMgr to close the Log LDAP connection.
     *
     * @param connection handle to ldap connection object.
     */
    protected void closeLogConnection( LdapConnection connection )
    {
        LdapConnectionProvider.getInstance().closeLogConnection(connection);
    }


    /**
     * Calls the PoolMgr to close the User LDAP connection.
     *
     * @param connection handle to ldap connection object.
     */
    protected void closeUserConnection( LdapConnection connection )
    {
        LdapConnectionProvider.getInstance().closeUserConnection(connection);
    }


    /**
     * Calls the PoolMgr to get an Admin connection to the LDAP server.
     *
     * @return ldap connection.
     * @throws LdapException If we had an issue getting an LDAP connection
     */
    public LdapConnection getAdminConnection() throws LdapException
    {
        return LdapConnectionProvider.getInstance().getAdminConnection();
    }


    /**
     * Calls the PoolMgr to get an Log connection to the LDAP server.
     *
     * @return ldap connection.
     * @throws LdapException If we had an issue getting an LDAP connection
     */
    protected LdapConnection getLogConnection() throws LdapException
    {
        return LdapConnectionProvider.getInstance().getLogConnection();
    }


    /**
     * Calls the PoolMgr to get an User connection to the LDAP server.
     *
     * @return ldap connection.
     * @throws LdapException If we had an issue getting an LDAP connection
     */
    protected LdapConnection getUserConnection() throws LdapException
    {
        return LdapConnectionProvider.getInstance().getUserConnection();
    }


    /**
     * Return to call reference to dao counter object with running totals for ldap operations add, mod, delete, search, etc.
     *
     * @return {@link LdapCounters} contains long values of atomic ldap operations for current running process.
     */
    public static LdapCounters getLdapCounters()
    {
        return COUNTERS;
    }




    /**
     * Perform encoding on supplied input string for certain unsafe ascii characters.  These chars may be unsafe
     * because ldap reserves some characters as operands.  Safe encoding safeguards from malicious scripting input errors 
     * that are possible if data filtering did not get performed before being passed into dao layer.
     *
     * @param filter contains the data to filter.
     * @return possibly modified input string for matched characters.
     */
    protected String escapeLDAPSearchFilter( String filter )
    {
        StringBuilder sb = new StringBuilder();
        int filterLen = filter.length();

        for ( int i = 0; i < filterLen; i++ )
        {
            boolean found = false;
            char curChar = filter.charAt( i );
            int j = 0;

            for ( ; j < GlobalIds.LDAP_FILTER_SIZE; j++ )
            {
                if ( LdapUtil.getInstance().getLdapMetaChars()[j] > curChar )
                {
                    break;
                }
                else if ( curChar == LdapUtil.getInstance().getLdapMetaChars()[j] )
                {
                    sb.append( "\\" );
                    sb.append( LdapUtil.getInstance().getLdapReplVals()[j] );
                    found = true;
                    break;
                }
            }

            if ( !found )
            {
                sb.append( curChar );
            }
        }

        return sb.toString();
    }

    /**
     * Closes all the ldap connection pools.
     */
    public static void closeAllConnectionPools(){
        LdapConnectionProvider.closeAllConnectionPools();
    }

}