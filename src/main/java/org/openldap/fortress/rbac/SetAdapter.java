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

package org.openldap.fortress.rbac;

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
            members = new TreeSet<>();
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
            members = new ArrayList<>();
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
