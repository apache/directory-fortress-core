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


import java.util.StringTokenizer;

/**
 * Class contains static utilities for conversions of ldap data to/from audit format
 * These are low-level data utilities and no validations are performed.  These apis should not be called by outside programs.
 * <p>
 * This class is thread safe.
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class AuditUtil
{

    /**
     * Private constructor
     *
     */
    private AuditUtil()
    {
    }

    /**
     * Parse a raw slapd access log format data string for userId.
     *
     * @param inputString raw slapd access log data.
     * @return string containing userId.
     */
    public static String getAuthZId(String inputString)
    {
        //reqAuthzID               <uid=fttu3user4,ou=people,dc=jts,dc=com>
        String userId = null;
        if (inputString != null && inputString.length() > 0)
        {
            StringTokenizer maxTkn = new StringTokenizer(inputString, ",");
            if (maxTkn.countTokens() > 0)
            {
                String val = maxTkn.nextToken();
                int indx = val.indexOf('=');
                if (indx >= 1)
                {
                    userId = val.substring(indx + 1);
                }
            }
        }
        return userId;
    }
}
