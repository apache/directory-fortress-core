package com.jts.fortress.util.cache;

import com.jts.fortress.ConfigurationException;
import com.jts.fortress.ConfigurationRuntimeException;
import com.jts.fortress.configuration.Config;
import com.jts.fortress.constants.GlobalErrIds;
import com.jts.fortress.util.ClassUtil;
import net.sf.ehcache.CacheManager;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is a facade and shields internal Fortress objects from specifics of the actual
 * cache implementation that is in use.
 * @author Shawn McKinney
 * @created March 9, 2012
 */
public class CacheMgr
{
    private static final String EHCACHE_CONFIG_FILE = "ehcache.config.file";
    private CacheManager m_ehCacheImpl;
    private static CacheMgr m_ftCacheImpl;
    private static AtomicBoolean isFtCacheInitialized = new AtomicBoolean(false);
    private static Object m_lock = new Object();

    /**
     * Private constructor.
     *
     * @param cacheMangerImpl contains a reference to cache implementation manager.
     */
    private CacheMgr(CacheManager cacheMangerImpl)
    {
        m_ehCacheImpl = cacheMangerImpl;
    }

    /**
     * Create or return the fortress cache manager reference.
     * @return handle to the cache manager in effect for process.
     */
    public static CacheMgr getInstance()
    {
        if (!isFtCacheInitialized.get())
        {
            synchronized (m_lock)
            {
                String cacheConfig = null;
                try
                {
                    cacheConfig = Config.getProperty(EHCACHE_CONFIG_FILE);
                    m_ftCacheImpl = new CacheMgr(CacheManager.create(ClassUtil.resourceAsStream(cacheConfig)));
                    isFtCacheInitialized.set(true);
                }
                catch (ConfigurationException ce)
                {
                    throw new ConfigurationRuntimeException(GlobalErrIds.FT_CACHE_NOT_CONFIGURED, cacheConfig);
                }
            }
        }
        return m_ftCacheImpl;
    }

    /**
     * Create a new reference to the ehcache cache implementation.
     *
     * @param cacheName contains the name of the cache to retrieve
     * @return reference to cache for specified object.
     */
    public Cache getCache(String cacheName)
    {
        return CacheFactory.createInstance(cacheName, m_ehCacheImpl);
    }
}
