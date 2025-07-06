package org.apache.directory.fortress.core.util.cache;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheMgr2
{
    private static final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    private static final Logger LOG = LoggerFactory.getLogger(CacheMgr2.class.getName());

    public static Cache getCache(String cacheName)
    {
        return new HzlCacheImpl(cacheName, hazelcastInstance);
    }
}