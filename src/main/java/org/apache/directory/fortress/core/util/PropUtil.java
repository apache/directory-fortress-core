/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core.util;

import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.directory.fortress.core.GlobalIds;

/**
 *  Utilities to convert to/from property formats.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class PropUtil
{
    /**
     * Convert from a {@link java.util.List} of properties stored as name:value pairs to
     * a {@link java.util.Properties}.
     *
     * @param propList contains a list of name-value pairs separated by a ':'.
     * @return reference to a Properties collection.
     */
    public static Properties getProperties(List<String> propList)
    {
        return getProperties(propList, GlobalIds.PROP_SEP );
    }

    /**
     * Convert from a {@link java.util.List} of properties stored as name:value pairs to
     * a {@link java.util.Properties}.
     *
     * @param propList contains a list of name-value pairs separated by a ':'.
     * @param separator contains char to be used to separate key and value.
     * @return reference to a Properties collection.
     */
    public static Properties getProperties( List<String> propList, char separator )
    {
        Properties props = null;
        if (propList != null && propList.size() > 0)
        {
            props = new Properties();
            propList.size();
            for (String raw : propList)
            {
                int indx = raw.indexOf(separator);
                if (indx >= 1)
                {
                    props.setProperty(raw.substring(0, indx), raw.substring(indx + 1));
                }
            }
        }
        return props;
    }

    /**
     * Convert from a comma delimited list of name-value pairs separated by a ':'.  Return the pros as {@link java.util.Properties}.
     *
     * @param inputString contains comma delimited list of properties.
     * @return java collection class containing props.
     */
    public static Properties getProperties( String inputString )
    {
        return getProperties( inputString, GlobalIds.PROP_SEP );
    }

    /**
     * Convert from a comma delimited list of name-value pairs separated by a ':'.  Return the pros as {@link java.util.Properties}.
     *
     * @param inputString contains comma delimited list of properties.
     * @param separator contains char to be used to separate key and value.
     * @return java collection class containing props.
     */
    public static Properties getProperties( String inputString, char separator )
    {
        return getProperties( inputString, separator, Config.getInstance().getDelimiter() );
    }

    /**
     * Convert from a comma delimited list of name-value pairs separated by a ':'.  Return the pros as {@link java.util.Properties}.
     *
     * @param inputString contains comma delimited list of properties.
     * @param separator contains char to be used to separate key and value.
     * @param delimiter contains a single char specifying delimiter between properties.
     * @return java collection class containing props.
     */
    public static Properties getProperties( String inputString, char separator, String delimiter )
    {
        Properties props = new Properties();
        if (inputString != null && inputString.length() > 0)
        {
            StringTokenizer maxTkn = new StringTokenizer(inputString, delimiter);
            if (maxTkn.countTokens() > 0)
            {
                while (maxTkn.hasMoreTokens())
                {
                    String val = maxTkn.nextToken();
                    int indx = val.indexOf(separator);
                    if (indx >= 1)
                    {
                        String name = val.substring(0, indx).trim();
                        String value = val.substring(indx + 1).trim();
                        props.setProperty(name, value);
                    }
                }
            }
        }
        return props;
    }


    /**
     * Method will return true if props is not null or empty.
     *
     * @param props contains the reference to props.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotEmpty( Properties props )
    {
        return ( props != null ) && ( props.size() > 0 );
    }
}
