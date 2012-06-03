package com.jts.fortress.util.cache;

import com.jts.fortress.constants.GlobalErrIds;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;

/**
 * This object provides cache functionality from <a href="http://ehcache.org//">Ehcache</a> provider.
 *
 * @author Shawn McKinney
 * @created March 9, 2012
 */
public class EhCacheImpl implements Cache
{
    private static final String CLS_NM = EhCacheImpl.class.getName();
    private net.sf.ehcache.Cache cache;
    private String name;

    /**
     * @param name
     * @param cache
     */
    EhCacheImpl(String name, net.sf.ehcache.Cache cache)
    {
        this.name = name;
        this.cache = cache;
    }

    /**
     * @param key
     * @return
     * @throws CacheException
     */
    public Object get(Object key) throws CacheException
    {
        if (cache == null)
        {
            String error = CLS_NM + ".get detected null cache name [" + name + "]";
            throw new CacheException(GlobalErrIds.FT_NULL_CACHE, error);
        }
        try
        {
            Element element = cache.get(key);
            if (element != null)
            {
                return element.getObjectValue();
            }
            else
            {
                return null;
            }
        }
        catch (net.sf.ehcache.CacheException ce)
        {
            String error = CLS_NM + ".get cache name [" + name + "] key [" + key + "] caught CacheException=" + ce.getMessage();
            throw new CacheException(GlobalErrIds.FT_CACHE_GET_ERR, error, ce);
        }
    }

    /**
     * @param key
     * @param value
     * @throws CacheException
     */
    public void put(Object key, Object value) throws CacheException
    {
        if (cache == null)
        {
            String error = CLS_NM + ".put detected null cache name [" + name + "]";
            throw new CacheException(GlobalErrIds.FT_NULL_CACHE, error);
        }
        try
        {
            cache.put(new Element(key, value));
        }
        catch (net.sf.ehcache.CacheException ce)
        {
            String error = CLS_NM + ".put cache name [" + name + "] key [" + key + "] caught CacheException=" + ce.getMessage();
            throw new CacheException(GlobalErrIds.FT_CACHE_PUT_ERR, error, ce);
        }
    }

    /**
     * @param key
     * @return
     * @throws CacheException
     */
    public boolean clear(Object key) throws CacheException
    {
        boolean result = false;
        if (cache == null)
        {
            String error = CLS_NM + ".clear detected null cache name [" + name + "]";
            throw new CacheException(GlobalErrIds.FT_NULL_CACHE, error);
        }
        try
        {
            cache.flush();
        }
        catch (net.sf.ehcache.CacheException ce)
        {
            String error = CLS_NM + ".clear cache name [" + name + "] key [" + key + "] caught CacheException=" + ce.getMessage();
            throw new CacheException(GlobalErrIds.FT_CACHE_CLEAR_ERR, error, ce);
        }
        return result;
    }

    /**
     * @throws CacheException
     */
    public void flush() throws CacheException
    {
        if (cache == null)
        {
            String error = CLS_NM + ".flush detected null cache name [" + name + "]";
            throw new CacheException(GlobalErrIds.FT_NULL_CACHE, error);
        }
        try
        {
            cache.removeAll();
        }
        catch (net.sf.ehcache.CacheException ce)
        {
            String error = CLS_NM + ".flush cache name [" + name + "] caught CacheException=" + ce.getMessage();
            throw new CacheException(GlobalErrIds.FT_CACHE_FLUSH_ERR, error, ce);

        }
    }

    /**
     *
     * @param attributeName
     * @param <T>
     * @return
     * @throws CacheException
     */
    public <T> Attribute<T> getSearchAttribute(String attributeName) throws CacheException
    {
        return this.cache.getSearchAttribute(attributeName);
    }

    /**
     *
     * @return
     */
    public Query createQuery()
    {
        return this.cache.createQuery();
    }
}
