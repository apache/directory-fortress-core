package com.jts.fortress.rbac;

import com.jts.fortress.util.cache.Cache;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;
import com.jts.fortress.util.cache.CacheMgr;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: smckinn
 * Date: 6/2/12
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class CacheSample
{
    private static final String CLS_NM = CacheSample.class.getName();
    final protected static Logger log = Logger.getLogger(CLS_NM);
    private static final SdP sdP = new SdP();
    private CacheMgr cacheManager;
    private Cache cache;

    private void initializeCache()
    {
        cacheManager = CacheMgr.getInstance();
        cache = cacheManager.getCache("fortress.dsd");
    }

    private void loadCache()
    {
        try
        {
            SDSet sdSet = new SDSet();
            sdSet.setType(SDSet.SDType.DYNAMIC);
            sdSet.setName("");
            List<SDSet> dsds = sdP.search(sdSet);
            if (dsds != null)
            {
                for (SDSet dsd : dsds)
                {
                    Set<String> members = dsd.getMembers();
                    if (members != null)
                    {
                        for (String member : members)
                        {
                            DsdCacheEntry entry = new DsdCacheEntry(member, dsd);
                            String key = dsd.getName() + ":" + member;
                            cache.put(key, entry);
                            log.info("Add DSD key: " + key + " members: " + dsd.getMembers() + " to the cache");
                        }
                    }
                }
            }
            else
            {
                log.info("No records found for loading into test cache");
            }
        }
        catch (com.jts.fortress.SecurityException se)
        {
            String error = CLS_NM + " static initializer caught SecurityException=" + se.getMessage() + " rc=" + se.getErrorId();
            log.error(error);
        }
    }


    public void runTests() throws IOException
    {
        loadCache();
        Attribute<String> member = cache.getSearchAttribute("member");
        Query query = cache.createQuery();
        query.includeKeys();
        query.includeValues();
        Set<String> roles = new HashSet<String>();
        roles.add("oamt17dsd1");
        roles.add("oamt17dsd4");
        roles.add("oamT13DSD6");
        roles.add("oamT16SDR7");
        query.addCriteria(member.in(roles));
        Results results = query.execute();
        System.out.println(" Size: " + results.size());
        System.out.println("----Results-----\n");
        Set<SDSet> resultSet = new HashSet<SDSet>();

        for (Result result : results.all())
        {
            DsdCacheEntry entry = (DsdCacheEntry) result.getValue();
            resultSet.add((SDSet) entry.getSdSet());
        }

        for (SDSet sdSet : resultSet)
        {
            log.info("Found SDSet: " + sdSet.getName());
        }
    }


    /**
     * put your documentation comment here
     *
     * @param args
     */
    public static void main(String[] args)
    {
        log.info(CLS_NM + ".main Test #args=" + args.length);
        CacheSample sample = new CacheSample();
        sample.initializeCache();
        try
        {
            sample.runTests();
        }
        catch (IOException ioe)
        {
            String error = CLS_NM + ".main caught IOException=" + ioe;
            log.error(error);
            ioe.printStackTrace();
        }
    }

    public class DsdCacheEntry
    {
        private String member;
        private SDSet sdSet;

        public DsdCacheEntry(String member, SDSet sdSet)
        {
            this.sdSet = sdSet;
            this.member = member;
        }

        public String getMember()
        {
            return member;
        }

        public void setMember(String member)
        {
            this.member = member;
        }

        public SDSet getSdSet()
        {
            return sdSet;
        }

        public void setSdSet(SDSet sdSet)
        {
            this.sdSet = sdSet;
        }
    }
}