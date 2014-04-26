/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */
package org.openldap.fortress.util.cache;


import java.util.concurrent.atomic.AtomicBoolean;

import net.sf.ehcache.CacheManager;
import org.openldap.fortress.CfgException;
import org.openldap.fortress.CfgRuntimeException;
import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.cfg.Config;
import org.openldap.fortress.rbac.ClassUtil;


/**
 * This class is a facade and shields internal Fortress objects from specifics of the actual
 * cache implementation that is in use.
 *
 * @author Shawn McKinney
 */
public class CacheMgr
{
    private static final String EHCACHE_CONFIG_FILE = "ehcache.config.file";
    private final CacheManager m_ehCacheImpl;
    private static CacheMgr m_ftCacheImpl;
    private static final AtomicBoolean isFtCacheInitialized = new AtomicBoolean( false );
    private static final Object m_lock = new Object();


    /**
     * Private constructor.
     *
     * @param cacheMangerImpl contains a reference to cache implementation manager.
     */
    private CacheMgr( CacheManager cacheMangerImpl )
    {
        m_ehCacheImpl = cacheMangerImpl;
    }


    /**
     * Create or return the fortress cache manager reference.
     * @return handle to the cache manager in effect for process.
     */
    public static CacheMgr getInstance()
    {
        if ( !isFtCacheInitialized.get() )
        {
            synchronized ( m_lock )
            {
                String cacheConfig = null;
                try
                {
                    cacheConfig = Config.getProperty( EHCACHE_CONFIG_FILE );
                    m_ftCacheImpl = new CacheMgr( CacheManager.create( ClassUtil.resourceAsStream( cacheConfig ) ) );
                    isFtCacheInitialized.set( true );
                }
                catch ( CfgException ce )
                {
                    throw new CfgRuntimeException( GlobalErrIds.FT_CACHE_NOT_CONFIGURED, cacheConfig );
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
    public Cache getCache( String cacheName )
    {
        return CacheFactory.createInstance( cacheName, m_ehCacheImpl );
    }


    public void clearAll()
    {
        m_ehCacheImpl.clearAll();
    }
}
