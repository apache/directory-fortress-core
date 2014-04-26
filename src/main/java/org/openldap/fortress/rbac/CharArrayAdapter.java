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