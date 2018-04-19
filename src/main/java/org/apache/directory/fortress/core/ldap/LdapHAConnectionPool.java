package org.apache.directory.fortress.core.ldap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapConnectionPool;
import org.apache.directory.ldap.client.api.ValidatingPoolableLdapConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LdapHAConnectionPool
{
    private static final Logger LOG = LoggerFactory.getLogger( LdapHAConnectionPool.class );
    private static final int DEFAULT_RETRIES_ALLOWED = 2;
    private static final Map<String, LdapConnectionPool> CONNECTION_POOL_LOOKUP_TABLE = new HashMap<>();
    private final LdapConnectionPool[] connectionPools;
    private final HAConnectionStrategy connectionStrategy;
    private final AtomicInteger requestCounter;
    private final int availableNodes;
    private final AtomicInteger masterNodeIndex;
    private final AtomicInteger allowedRetriesCounter;
    private final int allowedRetries;

    public LdapHAConnectionPool( LdapConnectionConfig[] configs, HAConnectionStrategy connectionStrategy,
            boolean testOnBorrow, byte exhaustedAction, int maxActive, int minIdle, int maxIdle, int allowedRetries )
    {
        this.availableNodes = configs.length;
        this.requestCounter = new AtomicInteger( 0 );
        this.masterNodeIndex = new AtomicInteger( 0 );
        if ( allowedRetries == 0 )
        {
            this.allowedRetries = DEFAULT_RETRIES_ALLOWED;
        } else
        {
            this.allowedRetries = allowedRetries;
        }

        this.allowedRetriesCounter = new AtomicInteger( allowedRetries );
        connectionPools = new LdapConnectionPool[configs.length];
        for ( int i = 0; i < configs.length; i++ )
        {
            connectionPools[i] = new LdapConnectionPool( new ValidatingPoolableLdapConnectionFactory( configs[i] ) );
            connectionPools[i].setTestOnBorrow( true );
            connectionPools[i].setWhenExhaustedAction( GenericObjectPool.WHEN_EXHAUSTED_GROW );
            connectionPools[i].setMaxActive( maxActive );
            connectionPools[i].setMinIdle( minIdle );
            connectionPools[i].setMaxIdle( maxIdle );
        }
        this.connectionStrategy = connectionStrategy;
    }

    public LdapConnection getConnection() throws LdapException
    {
        LdapConnection connection;
        try
        {
            connection = getConnectionByStrategy();
            LOG.trace( "borrowed connection {}", connection );
        } catch ( LdapException | RuntimeException e )
        {
            throw e;
        } catch ( Exception e )
        {
            LOG.error( "An unexpected exception was thrown: ", e );
            throw new RuntimeException( e );
        }

        return connection;
    }

    private LdapConnection getConnectionByStrategy() throws LdapException
    {
        LdapConnection borrowedConnection = null;
        switch ( this.connectionStrategy )
        {
            case ACTIVE_PASSIVE:
                try
                {
                    int poolIndex = masterNodeIndex.get();
                    borrowedConnection = connectionPools[poolIndex].borrowObject();
                    allowedRetriesCounter.set( allowedRetries );
                    CONNECTION_POOL_LOOKUP_TABLE.put( borrowedConnection.toString(), connectionPools[poolIndex] );
                } catch ( LdapException | RuntimeException e )
                {
                    if ( masterNodeIndex.get() < ( availableNodes - 1 ) )
                    {
                        masterNodeIndex.incrementAndGet();
                        return getConnectionByStrategy();
                    } else
                    {
                        if ( allowedRetriesCounter.decrementAndGet() > 0 )
                        {
                            masterNodeIndex.set( 0 );
                            return getConnectionByStrategy();
                        } else
                        {
                            throw new RuntimeException( "could not allowed more retries for active passive strategy" );
                        }
                    }

                } catch ( Exception e )
                {
                    LOG.error( "An unexpected exception with active passive strategy was thrown: ", e );
                    throw new RuntimeException( e );
                }
                break;

            default:
                try
                {
                    int poolIndex = getCurrentRequestNode();
                    borrowedConnection = connectionPools[poolIndex].borrowObject();
                    allowedRetriesCounter.set( allowedRetries );
                    CONNECTION_POOL_LOOKUP_TABLE.put( borrowedConnection.toString(), connectionPools[poolIndex] );
                } catch ( LdapException | RuntimeException e )
                {
                    if ( allowedRetriesCounter.decrementAndGet() > 0 )
                    {
                        return getConnectionByStrategy();
                    } else
                    {
                        throw new RuntimeException( "could not allowed more retries for round robin strategy" );
                    }

                } catch ( Exception e )
                {
                    LOG.error( "An unexpected exception was thrown: ", e );
                    throw new RuntimeException( e );
                }
                break;
        }

        return borrowedConnection;
    }

    private int getCurrentRequestNode()
    {
        return requestCounter.getAndIncrement() % availableNodes;
    }

    public void releaseConnection( LdapConnection connection )
    {
        try
        {
            CONNECTION_POOL_LOOKUP_TABLE.get( connection.toString() ).releaseConnection( connection );
            CONNECTION_POOL_LOOKUP_TABLE.remove( connection.toString() );
        } catch ( Exception e )
        {
            throw new RuntimeException( e.getMessage(), e );
        }
    }

}
