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

import java.util.Collection;
import java.util.Properties;

/**
 * Contains utilities for null checks of various objects.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class ObjUtil
{
    // cannot construct
    private ObjUtil()
    {

    }

    /**
     * Method will return true if string array reference is not null or empty.
     *
     * @param value contains the reference to string array.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotNullOrEmpty( String[] value )
    {
        return ( value != null ) && ( value.length > 0 );
    }


    /**
     * Method will return true if string reference is not null or empty.
     *
     * @param value contains the reference to string.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotNullOrEmpty( char[] value )
    {
        return ( value != null ) && ( value.length > 0 );
    }


    /**
     * Method will return true if list is not null or empty.
     *
     * @param list contains the reference to list.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotNullOrEmpty( Collection<?> list )
    {
        return ( list != null ) && ( list.size() > 0 );
    }


    /**
     * Method will return true if props is not null or empty.
     *
     * @param props contains the reference to props.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotNullOrEmpty( Properties props )
    {
        return ( props != null ) && ( props.size() > 0 );
    }


    /**
     * Method will return true if input is not null or empty.
     *
     * @param iVal contains the reference to Integer variable.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotNullOrEmpty( Integer iVal )
    {
        return ( iVal != null );
    }


    /**
     * Method will return true if input is not null or empty.
     *
     * @param bVal contains the reference to Boolean variable.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotNullOrEmpty( Boolean bVal )
    {
        return ( bVal != null );
    }


    /**
     * Method will return true if byte array reference is not null or empty.
     *
     * @param value contains the reference to byte array.
     * @return boolean if validation succeeds.
     */
    public static boolean isNotNullOrEmpty( byte[] value )
    {
        boolean result = false;
        if ( value != null && value.length > 0 )
        {
            result = true;
        }
        return result;
    }
}
