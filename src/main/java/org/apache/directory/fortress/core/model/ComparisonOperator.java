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
package org.apache.directory.fortress.core.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ComparisonOperator
{
    EQUALS( "eq" ),
    NOT_EQUALS( "neq" ),
    GREATER_THAN( "gt" ),
    GREATER_THAN_OR_EQUAL_TO( "gte" ),
    LESS_THAN( "lt" ),
    LESS_THAN_OR_EQUAL_TO( "lte" );

    private final String name;
    private static Map<String, ComparisonOperator> reverseLookup = new HashMap<String, ComparisonOperator>();

    static
    {
        EnumSet<ComparisonOperator> es = EnumSet.allOf( ComparisonOperator.class );

        for ( ComparisonOperator co : es )
        {
            reverseLookup.put( co.toString(), co );
        }
    }

    private ComparisonOperator(String s)
    {
        name = s;
    }

    public static ComparisonOperator fromName(String name)
    {
        if ( name != null )
        {
            return reverseLookup.get( name.trim() );
        }

        return null;
    }

    public String toString()
    {
        return this.name;
    }

}