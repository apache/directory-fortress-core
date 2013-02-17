package us.jts.fortress.rbac;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Shawn McKinney
 * Date: 1/8/12
 * Time: 7:29 AM
 */
public class CharArrayAdapter extends XmlAdapter<String, char[]>
{
    public char[] unmarshal(String val) throws Exception
    {
        return val.toCharArray();
    }

    public String marshal(char[] val) throws Exception
    {
        return Arrays.toString(val);
    }
}