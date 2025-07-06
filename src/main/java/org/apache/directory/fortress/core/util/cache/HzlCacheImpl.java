package org.apache.directory.fortress.core.util.cache;

import com.hazelcast.config.IndexConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicates;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

public class HzlCacheImpl implements Cache
{
    private static final String CLS_NM = HzlCacheImpl.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(CLS_NM);

    private final String name;
    private final IMap<Object, Object> cache;

    public HzlCacheImpl(String name, HazelcastInstance hazelcastInstance)
    {
        this.name = name;
        this.cache = hazelcastInstance.getMap(name);
    }

    @Override
    public Object get(Object key) throws CacheException
    {
        if (cache == null)
        {
            String error = "get detected null cache name [" + name + "]";
            throw new CacheException(GlobalErrIds.FT_NULL_CACHE, error);
        }
        try
        {
            return cache.get(key);
        } catch (Exception ce)
        {
            String error = "get cache name [" + name + "] key [" + key + "] caught Exception=" + ce.getMessage();
            throw new CacheException(GlobalErrIds.FT_CACHE_GET_ERR, error, ce);
        }
    }

    @Override
    public void put(Object key, Object value) throws CacheException
    {
        if (cache == null)
        {
            String error = "put detected null cache name [" + name + "]";
            throw new CacheException(GlobalErrIds.FT_NULL_CACHE, error);
        }
        try
        {
            cache.put(key, value);
        } catch (Exception ce)
        {
            String error = "put cache name [" + name + "] key [" + key + "] caught Exception=" + ce.getMessage();
            throw new CacheException(GlobalErrIds.FT_CACHE_PUT_ERR, error, ce);
        }
    }

    @Override
    public boolean clear(Object key) throws CacheException
    {
        if (cache == null)
        {
            String error = "clear detected null cache name [" + name + "]";
            throw new CacheException(GlobalErrIds.FT_NULL_CACHE, error);
        }
        try
        {
            return cache.remove(key) != null;
        } catch (Exception ce)
        {
            String error = "clear cache name [" + name + "] key [" + key + "] caught Exception=" + ce.getMessage();
            throw new CacheException(GlobalErrIds.FT_CACHE_CLEAR_ERR, error, ce);
        }
    }

    @Override
    public void flush() throws CacheException
    {
        if (cache == null)
        {
            String error = "flush detected null cache name [" + name + "]";
            throw new CacheException(GlobalErrIds.FT_NULL_CACHE, error);
        }
        try
        {
            cache.clear();
        } catch (Exception ce)
        {
            String error = "flush cache name [" + name + "] caught Exception=" + ce.getMessage();
            throw new CacheException(GlobalErrIds.FT_CACHE_FLUSH_ERR, error, ce);
        }
    }

    @Override
    public <T> Attribute<T> getSearchAttribute(String attributeName ) throws CacheException
    {
        throw new UnsupportedOperationException("Hazelcast native API does not support search attributes directly");
    }

    @Override
    public Query createQuery()
    {
        throw new UnsupportedOperationException("Hazelcast native API does not provide the same query API as Ehcache 2");
    }

    @Override
    public void addIndex( String name )
    {
        cache.addIndex(new IndexConfig(IndexType.HASH, name));
    }

    @Override
    public <T> Collection<T> createQuery(String name, List<String> values)
    {
        return (Collection<T>) cache.values(Predicates.in(name, values.toArray(new String[0])));
    }

    public void clear()
    {
        cache.clear();
    }
}