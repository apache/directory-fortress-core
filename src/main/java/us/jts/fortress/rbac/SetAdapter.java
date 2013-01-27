package us.jts.fortress.rbac;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: Shawn McKinney
 * Date: 1/21/12
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SetAdapter extends XmlAdapter<ArrayList<String>, Set<String>>
{
    public Set<String> unmarshal(ArrayList<String> val) throws Exception
    {
        Set<String> members = null;
        if(val != null)
        {
            members = new TreeSet<String>();
            for(String member : val)
            {
                members.add(member);
            }
        }
        return members;
    }

    public ArrayList<String> marshal(Set<String> val) throws Exception
    {
        ArrayList<String> members = null;
        if(val != null)
        {
            members = new ArrayList<String>();
            for(String member : val)
            {
                members.add(member);
            }
        }
        return members;
    }
}


/*
    public char[] unmarshal(String val) throws Exception
    {
        return val.toCharArray();
    }

    public String marshal(char[] val) throws Exception
    {
        return val.toString();
    }

 */
