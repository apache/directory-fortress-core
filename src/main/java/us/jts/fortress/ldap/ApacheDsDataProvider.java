/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ldap;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.SearchCursor;
import org.apache.directory.api.ldap.codec.api.LdapApiService;
import org.apache.directory.api.ldap.codec.api.LdapApiServiceFactory;
import org.apache.directory.api.ldap.codec.standalone.StandaloneLdapApiService;
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
import org.apache.directory.api.ldap.model.exception.LdapReferralException;
import org.apache.directory.api.ldap.model.message.CompareRequest;
import org.apache.directory.api.ldap.model.message.CompareRequestImpl;
import org.apache.directory.api.ldap.model.message.CompareResponse;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.apache.directory.api.ldap.model.message.SearchRequestImpl;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapConnectionPool;
import org.apache.directory.ldap.client.api.PoolableLdapConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.jts.fortress.CfgRuntimeException;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.GlobalIds;
import us.jts.fortress.cfg.Config;
import us.jts.fortress.rbac.FortEntity;
import us.jts.fortress.rbac.Hier;
import us.jts.fortress.rbac.Relationship;
import us.jts.fortress.util.attr.VUtil;
import us.jts.fortress.util.crypto.EncryptUtil;
import us.jts.fortress.util.time.CUtil;
import us.jts.fortress.util.time.Constraint;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModification;


/**
 * Abstract class contains methods to perform low-level entity to ldap persistence.  These methods are called by the
 * Fortress DAO's, i.e. {@link us.jts.fortress.rbac.dao.apache.UserDAO}. {@link us.jts.fortress.rbac.dao.apache
 * .RoleDAO}, {@link us.jts.fortress.rbac.dao.apache.PermDAO}, ....
 * These are low-level data utilities, very little if any data validations are performed here.
 * <p/>
 * This class is thread safe.
 * <p/>
 *
 * @author Shawn McKinney
 */
public abstract class ApacheDsDataProvider
{
    private static final int MAX_DEPTH = 100;
    private static final String CLS_NM = ApacheDsDataProvider.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final LdapCounters counters = new LdapCounters();
    private static final String LDAP_HOST = "host";
    private static final String LDAP_PORT = "port";
    private static final String LDAP_ADMIN_POOL_MIN = "min.admin.conn";
    private static final String LDAP_ADMIN_POOL_MAX = "max.admin.conn";
    private static final String LDAP_ADMIN_POOL_UID = "admin.user";
    private static final String LDAP_ADMIN_POOL_PW = "admin.pw";

    /**
     * The Admin connection pool
     */
    private static LdapConnectionPool adminPool;

    /**
     * The Log connection pool
     */
    private static LdapConnectionPool logPool;

    /**
     * The User connection pool
     */
    private static LdapConnectionPool userPool;

    static
    {
        String host = Config.getProperty( LDAP_HOST, "localhost" );
        int port = Config.getInt( LDAP_PORT, 10389 );
        int min = Config.getInt( LDAP_ADMIN_POOL_MIN, 1 );
        int max = Config.getInt( LDAP_ADMIN_POOL_MAX, 10 );

        LdapConnectionConfig config = new LdapConnectionConfig();
        config.setLdapHost( host );
        config.setLdapPort( port );
        config.setName( Config.getProperty( LDAP_ADMIN_POOL_UID, "" ) );

        String adminPw = null;

        if ( EncryptUtil.isEnabled() )
        {
            adminPw = EncryptUtil.decrypt( Config.getProperty( LDAP_ADMIN_POOL_PW ) );
        }
        else
        {
            adminPw = Config.getProperty( LDAP_ADMIN_POOL_PW );
        }

        config.setCredentials( adminPw );
        try
        {
            System.setProperty( StandaloneLdapApiService.EXTENDED_OPERATIONS_LIST,
                "org.openldap.accelerator.impl.createSession.RbacCreateSessionFactory,"
              + "org.openldap.accelerator.impl.checkAccess.RbacCheckAccessFactory,"
              + "org.openldap.accelerator.impl.addRole.RbacAddRoleFactory,"
              + "org.openldap.accelerator.impl.dropRole.RbacDropRoleFactory,"
              + "org.openldap.accelerator.impl.deleteSession.RbacDeleteSessionFactory");

            LdapApiService ldapApiService = new StandaloneLdapApiService();
            if ( LdapApiServiceFactory.isInitialized() == false )
            {
                LdapApiServiceFactory.initialize( ldapApiService );
            }
            config.setLdapApiService( ldapApiService );
        }
        catch ( Exception ex )
        {
            String error =  "Exception caught initializing Admin Pool: " + ex;
            throw new CfgRuntimeException( GlobalErrIds.FT_APACHE_LDAP_POOL_INIT_FAILED, error, ex );
        }

        PoolableLdapConnectionFactory factory = new PoolableLdapConnectionFactory( config );

        // Create the Admin pool
        adminPool = new LdapConnectionPool( factory );
        adminPool.setTestOnBorrow( true );
        adminPool.setWhenExhaustedAction( GenericObjectPool.WHEN_EXHAUSTED_GROW );
        adminPool.setMaxActive( max );
        adminPool.setMinIdle( min );

        // Create the Log pool
        logPool = new LdapConnectionPool( factory );
        logPool.setTestOnBorrow( true );
        logPool.setWhenExhaustedAction( GenericObjectPool.WHEN_EXHAUSTED_GROW );
        logPool.setMaxActive( max );
        logPool.setMinIdle( min );

        // Create the User pool
        userPool = new LdapConnectionPool( factory );
        userPool.setTestOnBorrow( true );
        userPool.setWhenExhaustedAction( GenericObjectPool.WHEN_EXHAUSTED_GROW );
        userPool.setMaxActive( max );
        userPool.setMinIdle( min );
    }


    /**
     * Given a contextId and a fortress param name return the LDAP dn.
     *
     * @param contextId is to determine what sub-tree to use.
     * @param root      contains the fortress parameter name that corresponds with a particular LDAP container.
     * @return String contains the dn to use for operation.
     */
    protected String getRootDn( String contextId, String root )
    {
        String szDn = Config.getProperty( root );
        StringBuilder dn = new StringBuilder();

        // The contextId must not be null, or "HOME" or "null"
        if ( VUtil.isNotNullOrEmpty( contextId ) && !contextId.equalsIgnoreCase( GlobalIds.NULL ) && !contextId
            .equals( GlobalIds.HOME ) )
        {
            int idx = szDn.indexOf( Config.getProperty( GlobalIds.SUFFIX ) );

            if ( idx != -1 )
            {
                // Found. The DN is ,ou=<contextId>,  
                dn.append( szDn.substring( 0, idx - 1 ) ).append( "," ).append( GlobalIds.OU ).append( "=" ).append(
                    contextId ).append( "," ).append( szDn.substring( idx ) );
            }
        }
        else
        {
            dn.append( szDn );
        }

        return dn.toString();
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
        if ( VUtil.isNotNullOrEmpty( contextId ) && !contextId.equalsIgnoreCase( GlobalIds.NULL ) && !contextId
            .equals( GlobalIds.HOME ) )
        {
            dn.append( GlobalIds.OU ).append( "=" ).append( contextId ).append( "," +
                "" ).append( Config.getProperty( GlobalIds.SUFFIX ) );
        }
        else
        {
            dn.append( Config.getProperty( GlobalIds.SUFFIX ) );
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
        counters.incrementRead();

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
        counters.incrementRead();

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
        counters.incrementAdd();
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
        counters.incrementAdd();

        if ( GlobalIds.IS_AUDIT && entity != null && entity.getAdminSession() != null )
        {
            if ( VUtil.isNotNullOrEmpty( entity.getAdminSession().getInternalUserId() ) )
            {
                entry.add( GlobalIds.FT_MODIFIER, entity.getAdminSession().getInternalUserId() );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getModCode() ) )
            {
                entry.add( GlobalIds.FT_MODIFIER_CODE, entity.getModCode() );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getModId() ) )
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
        counters.incrementMod();
        connection.modify( dn, mods.toArray( new Modification[]{} ) );
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
        counters.incrementMod();
        audit( mods, entity );
        connection.modify( dn, mods.toArray( new Modification[]{} ) );
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
        counters.incrementDelete();
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
        counters.incrementDelete();
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
     * @throws IOException
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
     * @throws IOException
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
        SearchCursor cursor = search( connection, dn, SearchScope.ONELEVEL, "(objectclass=*)", GlobalIds.NO_ATRS,
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
            catch ( LdapReferralException lre )
            {
                // cannot continue;
                String error = "." + method + " dn [" + dn + "] caught LDAPReferralException=" + lre.getMessage() +
                    "=" + lre.getReferralInfo();
                throw lre;
            }
            catch ( LdapException le )
            {
                // cannot continue;
                String error = "." + method + " dn [" + dn + "] caught LdapException=" + le.getMessage();
                throw new LdapException( error );
            }
        }

        // delete the node:
        counters.incrementDelete();
        delete( connection, dn );
    }


    /**
     * Add the audit context variables to the modfication set.
     *
     * @param mods   used to update ldap attributes.
     * @param entity contains audit context.
     * @throws LdapException in the event of error with ldap client.
     */
    private void audit( List<Modification> mods, FortEntity entity )
    {
        if ( GlobalIds.IS_AUDIT && ( entity != null ) && ( entity.getAdminSession() != null ) )
        {
            if ( VUtil.isNotNullOrEmpty( entity.getAdminSession().getInternalUserId() ) )
            {
                Modification modification = new DefaultModification( ModificationOperation.REPLACE_ATTRIBUTE,
                    GlobalIds.FT_MODIFIER, entity.getAdminSession().getInternalUserId() );
                mods.add( modification );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getModCode() ) )
            {
                Modification modification = new DefaultModification( ModificationOperation.REPLACE_ATTRIBUTE,
                    GlobalIds.FT_MODIFIER_CODE, entity.getModCode() );
                mods.add( modification );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getModId() ) )
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
        counters.incrementSearch();

        SearchRequest searchRequest = new SearchRequestImpl();
        searchRequest.setBase( new Dn( baseDn ) );
        searchRequest.setScope( scope );
        searchRequest.setFilter( filter );
        searchRequest.setTypesOnly( attrsOnly );
        searchRequest.addAttributes( attrs );
        SearchCursor result = connection.search( searchRequest );

        return result;
    }


    /**
     * Perform normal ldap search specifying default batch size.
     *
     * @param connection is LdapConnection object used for all communication with host.
     * @param baseDn     contains address of distinguished name to begin ldap search
     * @param scope      indicates depth of search starting at basedn.  0 (base dn),
     *                   1 (one level down) or 2 (infinite) are valid values.
     * @param filter     contains the search criteria
     * @param attrs      is the requested list of attributes to return from directory search.
     * @param attrsOnly  if true pull back attribute names only.
     * @param batchSize  Will block until this many entries are ready to return from server.  0 indicates to block
     *                   until all results are ready.
     * @return result set containing ldap entries returned from directory.
     * @throws LdapException thrown in the event of error in ldap client or server code.
     */
    protected SearchCursor search( LdapConnection connection, String baseDn, SearchScope scope, String filter,
        String[] attrs, boolean attrsOnly, int batchSize ) throws LdapException
    {
        counters.incrementSearch();

        SearchRequest searchRequest = new SearchRequestImpl();

        searchRequest.setBase( new Dn( baseDn ) );
        searchRequest.setFilter( filter );
        searchRequest.setScope( scope );
        searchRequest.setSizeLimit( batchSize );
        searchRequest.setTypesOnly( attrsOnly );
        searchRequest.addAttributes( attrs );

        SearchCursor result = connection.search( searchRequest );

        return result;
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
     * @param batchSize  Will block until this many entries are ready to return from server.  0 indicates to block
     *                   until all results are ready.
     * @param maxEntries specifies the maximum number of entries to return in this search query.
     * @return result set containing ldap entries returned from directory.
     * @throws LdapException thrown in the event of error in ldap client or server code.
     */
    protected SearchCursor search( LdapConnection connection, String baseDn, SearchScope scope, String filter,
        String[] attrs, boolean attrsOnly, int batchSize, int maxEntries ) throws LdapException
    {
        counters.incrementSearch();

        SearchRequest searchRequest = new SearchRequestImpl();

        searchRequest.setBase( new Dn( baseDn ) );
        searchRequest.setFilter( filter );
        searchRequest.setScope( scope );
        searchRequest.setSizeLimit( batchSize );
        searchRequest.setTypesOnly( attrsOnly );
        searchRequest.addAttributes( attrs );

        SearchCursor result = connection.search( searchRequest );

        return result;
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
     * @throws IOException
     * @throws CursorException
     */
    protected Entry searchNode( LdapConnection connection, String baseDn, SearchScope scope, String filter,
        String[] attrs, boolean attrsOnly ) throws LdapException, CursorException, IOException
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
     * @throws IOException
     * @throws CursorException
     */
    protected Entry searchNode( LdapConnection connection, String baseDn, SearchScope scope, String filter,
        String[] attrs, boolean attrsOnly, String userDn ) throws LdapException, CursorException, IOException
    {
        counters.incrementSearch();

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
        counters.incrementCompare();

        CompareRequest compareRequest = new CompareRequestImpl();
        compareRequest.setName( new Dn( dn ) );
        compareRequest.setAttributeId( attribute.getId() );
        compareRequest.setAssertionValue( attribute.getString() );

        CompareResponse response = connection.compare( compareRequest );

        return response.getLdapResult().getResultCode() == ResultCodeEnum.SUCCESS;
    }


    /**
     * Method wraps ldap client to return multi-occurring attribute values by name within a given entry and returns
     * as a list of strings.
     *
     * @param entry         contains the target ldap entry.
     * @param attributeName name of ldap attribute to retrieve.
     * @return List of type string containing attribute values.
     * @throws LdapException in the event of ldap client error.
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
     * Method wraps ldap client to return multi-occurring attribute values by name within a given entry and returns
     * as a set of strings.
     *
     * @param entry         contains the target ldap entry.
     * @param attributeName name of ldap attribute to retrieve.
     * @return List of type string containing attribute values.
     * @throws LdapException in the event of ldap client error.
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
     * @throws LdapInvalidAttributeValueException
     *
     * @throws LdapException in the event of ldap client error.
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
     * @throws LdapException in the event of ldap client error.
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


    /**
     * Create multi-occurring ldap attribute given array of strings and attribute name.
     *
     * @param name   contains attribute name to create.
     * @param values array of string that contains attribute values.
     * @return Attribute containing multi-occurring attribute set.
     * @throws LdapException in the event of ldap client error.
     */
    protected Attribute createAttributes( String name, String values[] ) throws LdapException
    {
        Attribute attr = new DefaultAttribute( name, values );

        return attr;
    }


    /**
     * Convert constraint from raw ldap format to application entity.
     *
     * @param le         ldap entry containing constraint.
     * @param ftDateTime reference to {@link us.jts.fortress.util.time.Constraint} containing formatted data.
     * @throws LdapInvalidAttributeValueException
     *
     * @throws LdapException in the event of ldap client error.
     */
    protected void unloadTemporal( Entry le, Constraint ftDateTime ) throws LdapInvalidAttributeValueException
    {
        String szRawData = getAttribute( le, GlobalIds.CONSTRAINT );

        if ( szRawData != null && szRawData.length() > 0 )
        {
            CUtil.setConstraint( szRawData, ftDateTime );
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
                list.toArray( new String[]{} ) ) );
        }
    }


    /**
     * Given a collection of {@link us.jts.fortress.rbac.Relationship}s, convert to raw data name-value format and
     * load into ldap modification set in preparation for ldap modify.
     *
     * @param list     contains List of type {@link us.jts.fortress.rbac.Relationship} targeted for updating in ldap.
     * @param mods     ldap modification set containing parent-child relationships in raw ldap format.
     * @param attrName contains the name of the ldap attribute to be updated.
     * @param op       specifies type of mod: {@link Hier.Op#ADD}, {@link us.jts.fortress.rbac.Hier.Op#MOD},
     * {@link Hier.Op#REM}
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
                values.toArray( new String[]{} ) ) );
        }
    }


    /**
     * Given an ldap attribute name and a set of attribute values, construct an ldap attribute set to be added to
     * directory.
     *
     * @param values   set of type string containing attribute values to load into attribute set.
     * @param entry    contains ldap entry to pull attrs from.
     * @param attrName name of ldap attribute being added.
     * @throws LdapException
     */
    protected void loadAttrs( Set<String> values, Entry entry, String attrName ) throws LdapException
    {
        if ( ( values != null ) && ( values.size() > 0 ) )
        {
            entry.add( attrName, values.toArray( new String[]{} ) );
        }
    }


    /**
     * Given a collection of {@link java.util.Properties}, convert to raw data name-value format and load into ldap
     * modification set in preparation for ldap modify.
     *
     * @param props    contains {@link java.util.Properties} targeted for updating in ldap.
     * @param mods     ldap modification set containing name-value pairs in raw ldap format.
     * @param attrName contains the name of the ldap attribute to be updated.
     * @param replace  boolean variable, if set to true use {@link LDAPModification#REPLACE} else {@link
     * LDAPModification#ADD}.
     */
    protected void loadProperties( Properties props, List<Modification> mods, String attrName, boolean replace )
    {
        if ( props != null && props.size() > 0 )
        {
            if ( replace )
            {
                mods.add( new DefaultModification( ModificationOperation.REPLACE_ATTRIBUTE, attrName ) );
            }

            for ( Enumeration e = props.propertyNames(); e.hasMoreElements(); )
            {
                String key = ( String ) e.nextElement();
                String val = props.getProperty( key );
                // This LDAP attr is stored as a name-value pair separated by a ':'.
                mods.add( new DefaultModification( ModificationOperation.ADD_ATTRIBUTE, attrName,
                    key + GlobalIds.PROP_SEP + val ) );
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
            Attribute prop;

            for ( Enumeration e = props.propertyNames(); e.hasMoreElements(); )
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
     * @throws LdapException
     */
    protected void loadProperties( Properties props, Entry entry, String attrName ) throws LdapException
    {
        if ( ( props != null ) && ( props.size() > 0 ) )
        {
            Attribute attr = new DefaultAttribute( attrName );

            for ( Enumeration e = props.propertyNames(); e.hasMoreElements(); )
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
     * @param value
     * @param validLen
     * @return String containing encoded data.
     * @throws LdapException
     */
    protected String encodeSafeText( String value, int validLen ) throws LdapException
    {
        if ( VUtil.isNotNullOrEmpty( value ) )
        {
            int length = value.length();
            if ( length > validLen )
            {
                String error = "encodeSafeText value [" + value + "] invalid length [" + length + "]";
                throw new LdapException( error );
            }
            if ( GlobalIds.LDAP_FILTER_SIZE_FOUND )
            {
                value = VUtil.escapeLDAPSearchFilter( value );
            }
        }
        return value;
    }


    /**
     * Calls the PoolMgr to perform an LDAP bind for a user/password combination.  This function is valid
     * if and only if the user entity is a member of the USERS data set.  The LDAP directory
     * will return the OpenLDAP PW Policy control.
     *
     * @param connection connection to ldap server.
     * @param userDn     contains the LDAP dn to the user entry.
     * @param password   contains the password in clear text.
     * @return boolean value - true if bind successful, false otherwise.
     * @throws LdapException in the event of LDAP error.
     */
    protected boolean bind( LdapConnection connection, String userDn, char[] password ) throws LdapException
    {
        counters.incrementBind();

        connection.bind( userDn, new String( password ) );
        return true;
    }


    /**
     * Calls the PoolMgr to close the Admin LDAP connection.
     *
     * @param connection handle to ldap connection object.
     * @throws Exception
     */
    protected void closeAdminConnection( LdapConnection connection )
    {
        try
        {
            adminPool.releaseConnection( connection );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e.getMessage() );
        }
    }


    /**
     * Calls the PoolMgr to close the Log LDAP connection.
     *
     * @param connection handle to ldap connection object.
     * @throws Exception
     */
    protected void closeLogConnection( LdapConnection connection )
    {
        try
        {
            logPool.releaseConnection( connection );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e.getMessage() );
        }
    }


    /**
     * Calls the PoolMgr to close the User LDAP connection.
     *
     * @param connection handle to ldap connection object.
     * @throws Exception
     */
    protected void closeUserConnection( LdapConnection connection )
    {
        try
        {
            userPool.releaseConnection( connection );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e.getMessage() );
        }
    }


    /**
     * Calls the PoolMgr to get an Admin connection to the LDAP server.
     *
     * @return ldap connection.
     * @throws LdapException
     */
    protected LdapConnection getAdminConnection() throws LdapException
    {
        try
        {
            return adminPool.getConnection();
        }
        catch ( Exception e )
        {
            throw new LdapException( e.getMessage() );
        }
    }


    /**
     * Calls the PoolMgr to get an Log connection to the LDAP server.
     *
     * @return ldap connection.
     * @throws LdapException
     */
    protected LdapConnection getLogConnection() throws LdapException
    {
        try
        {
            return logPool.getConnection();
        }
        catch ( Exception e )
        {
            throw new LdapException( e.getMessage() );
        }
    }


    /**
     * Calls the PoolMgr to get an User connection to the LDAP server.
     *
     * @return ldap connection.
     * @throws LdapException
     */
    protected LdapConnection getUserConnection() throws LdapException
    {
        try
        {
            return userPool.getConnection();
        }
        catch ( Exception e )
        {
            throw new LdapException( e.getMessage() );
        }
    }


    /**
     * Return to call reference to dao counter object with running totals for ldap operations add, mod, delete, search, etc.
     *
     * @return {@link LdapCounters} contains long values of atomic ldap operations for current running process.
     */
    public static LdapCounters getLdapCounters()
    {
        return counters;
    }
}