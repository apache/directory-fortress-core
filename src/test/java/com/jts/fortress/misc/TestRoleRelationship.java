/*

 */

/*
 *  This class is used for testing purposes.
 */

package com.jts.fortress.misc;

import com.jts.fortress.hier.Relationship;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Description of the Class
 *
 * @author Shawn McKinney
 * @created September 18, 2010
 */
public class TestRoleRelationship
{

    private static final String CLS_NM = TestRoleRelationship.class.getName();
    final protected static Logger log = Logger.getLogger(CLS_NM);
    private List<Relationship> rels;

    /**
     * put your documentation comment here
     *
     * @param args
     */
    public static void main(String[] args)
    {
        log.info(CLS_NM + ".main Test #args=" + args.length);
        int i = 0;
        TestRoleRelationship tc = new TestRoleRelationship();

        for (String arg : args)
        {
            log.info(CLS_NM + ".main add[" + i++ + "]=" + arg);
            //tc.addMember(arg);
            int indx = arg.indexOf(':');
            if (indx >= 1)
            {
                // separate the parent from the child:
                String child = arg.substring(0, indx - 1);
                String parent = arg.substring(indx + 1);
                Relationship rel = new Relationship(child, parent);
                tc.addMember(rel);
            }
        }

        log.info("size of members size=" + tc.rels.size());
        log.info("size of members values=" + tc.rels.toString());
        log.info("size of members hashCode=" + tc.rels.hashCode());

        i = 0;
        for (String arg : args)
        {
            log.info(CLS_NM + ".main remove[" + i++ + "]=" + arg);
            int indx = arg.indexOf(':');
            if (indx >= 1)
            {
                // separate the parent from the child:
                String child = arg.substring(0, indx - 1);
                String parent = arg.substring(indx + 1);
                Relationship rel = new Relationship(child, parent);
                Relationship mem = tc.getRelationship(rel);
                boolean result = tc.isContained(rel);

                log.info(CLS_NM + ".main mem[" + i + "]=" + mem);
                log.info(CLS_NM + ".main result[" + i + "]=" + result);


                child = child.toUpperCase();
                parent = parent.toUpperCase();
                rel = new Relationship(child, parent);
                mem = tc.getRelationship(rel);
                result = tc.isContained(rel);

                log.info(CLS_NM + ".main mem2[" + i + "]=" + mem);
                log.info(CLS_NM + ".main result2[" + i + "]=" + result);
                tc.addMember(rel);
            }
        }
	}

    /**
     *
     * @param rel
     * @return
     */
    public Relationship getRelationship(Relationship rel)
    {
        Relationship member = null;
        if (this.rels != null)
        {

            int indx = this.rels.indexOf(rel);
            if(indx != -1)
            {
                member = rels.get(indx);
            }
        }
        return member;
    }

    /**
     *
     * @param rel
     * @return
     */
    public boolean isContained(Relationship rel)
    {
        boolean member = false;
        if (this.rels != null)
        {
            member = rels.contains(rel);
        }
        return member;
    }

    public void addMember(Relationship rel)
    {
        if (this.rels == null)
        {
            this.rels = new ArrayList<Relationship>();
        }
        this.rels.add(rel);
    }

}

