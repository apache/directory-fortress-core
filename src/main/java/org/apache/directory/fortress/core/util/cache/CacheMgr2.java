package org.apache.directory.fortress.core.util.cache;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.directory.fortress.core.CfgRuntimeException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheMgr2
{
    private static final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    //private static volatile CacheMgr2 sINSTANCE = null;
    private static final Logger LOG = LoggerFactory.getLogger( CacheMgr2.class.getName() );

/*
    public static CacheMgr2 getInstance()
    {
        try
        {
            if( sINSTANCE == null)
            {
                synchronized (CacheMgr2.class)
                {
                    if( sINSTANCE == null){
                        sINSTANCE = new CacheMgr2();
                    }
                }
            }
        }
        catch (Exception e)
        {
            String error = "CacheMgr.getInstance caught Exception=" + e.getMessage();
            LOG.error( error );
            throw new CfgRuntimeException( GlobalErrIds.FT_CACHE_NOT_CONFIGURED, error, e );
        }
        return sINSTANCE;
    }
*/

    public static Cache getCache(String cacheName)
    {
        return new HzlCacheImpl(cacheName, hazelcastInstance);
    }
}