package com.jts.fortress.misc;

import org.apache.log4j.Logger;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Description of the Class
 *
 * @author Shawn McKinney
 * @created September 9, 2010
 */
public class TestComparator
{

    private static final String CLS_NM = TestComparator.class.getName();
    final protected static Logger log = Logger.getLogger(CLS_NM);
    private Map<String, String> members;

    /**
     * put your documentation comment here
     *
     * @param args
     */
    public static void main(String[] args)
    {
        log.info(CLS_NM + ".main Test #args=" + args.length);
        int i = 0;
        TestComparator tc = new TestComparator();

        for (String arg : args)
        {
            log.info(CLS_NM + ".main add[" + i++ + "]=" + arg);
            //tc.addMember(arg);
            tc.addMember(arg.toUpperCase());
        }

        log.info("size of members size=" + tc.members.size());
        log.info("size of members values=" + tc.members.values());
        log.info("size of members hashCode=" + tc.members.hashCode());

        i = 0;
        for (String arg : args)
        {
            log.info(CLS_NM + ".main remove[" + i++ + "]=" + arg);
            String mem = tc.getMember(arg);
            boolean result = tc.isContained(arg);

            log.info(CLS_NM + ".main mem[" + i + "]=" + mem);
            log.info(CLS_NM + ".main result[" + i + "]=" + result);

            mem = tc.getMember(arg.toUpperCase());
            result = tc.isContained(arg.toUpperCase());

            log.info(CLS_NM + ".main mem2[" + i + "]=" + mem);
            log.info(CLS_NM + ".main result2[" + i + "]=" + result);
        }


        // this.members = new TreeMap<String, String>( String.CASE_INSENSITIVE_ORDER );

	}

                //if(map.containsKey(authRole))

    public String getMember(String role)
    {
        String member = null;
        if (this.members != null)
        {
            member = members.get(role);
        }
        return member;
    }

    public boolean isContained(String role)
    {
        boolean member = false;
        if (this.members != null)
        {
            member = members.containsKey(role);
        }
        return member;
    }

    public void addMember(String role)
    {
        if (this.members == null)
        {
            this.members = new TreeMap<String, String>( String.CASE_INSENSITIVE_ORDER );
        }
        this.members.put(role, role);
    }




}

class AlphabeticalOrder implements Comparator<String>
{
    public int compare(String str1, String str2)
    {
        String s1 = str1.toLowerCase();  // Convert to lower case.
        String s2 = str2.toLowerCase();
      return s1.compareTo(s2);  // Compare lower-case Strings.
   }
}