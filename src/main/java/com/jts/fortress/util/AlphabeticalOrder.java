/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This object overrides {@code java.util.Comparator.compare} method.  An instance of this class may be passed to Set constructors which enables case insensitive name sets.
 *The overridden method wraps java api {@link String#compareToIgnoreCase(String)}.
 * </p>
 * The following example shows how to instantiate a case insensitive name set:<br/>
 * {@code Set<String> attrValues = new TreeSet<String>(new AlphabeticalOrder());}
 *
 * @author smckinn
 * @created September 10, 2010
 */
public class AlphabeticalOrder implements Comparator<String>, Serializable
{
    public int compare(String str1, String str2)
    {
        return str1.compareToIgnoreCase(str2);
    }
}

