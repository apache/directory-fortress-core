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



/**
 * This utility is a singleton and has getters / setters to access properties for filtering data bound for ldap.
 * It is used by the Config and LdapDataProvider classes to provide escape ldap data to prevent unauthorized or inadvertent tampering with operations on the server.
 * <p>
 * This class is not thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class LdapUtil
{
    private boolean ldapfilterSizeFound = false;
    private int ldapFilterSize = 25;
    private char[] ldapMetaChars;
    private String[] ldapReplVals;

    private static volatile LdapUtil sINSTANCE = null;

    /**
     * Provided synchronized access to this.
     *
     * @return a reference to singleton instance of this class.
     */
    public static LdapUtil getInstance()
    {
        if ( sINSTANCE == null )
        {
            synchronized ( LdapUtil.class )
            {
                if ( sINSTANCE == null )
                {
                    sINSTANCE = new LdapUtil();
                }
            }
        }
        return sINSTANCE;
    }

    /**
     * The ldap.filter.size specifies the number of entries in the set of ldap filter properties.
     *
     * @return true if the property value was found in the fortress configs.
     */
    public boolean isLdapfilterSizeFound()
    {
        return ldapfilterSizeFound;
    }

    /**
     * Associated with fortress.property named ldap.filter.size.  If the size matches the number of entries found in properties this will be set to 'true'.
     *
     * @param ldapfilterSizeFound true indicates the ldap.filter.size prop was set.
     */
    public void setLdapfilterSizeFound(boolean ldapfilterSizeFound)
    {
        this.ldapfilterSizeFound = ldapfilterSizeFound;
    }

    /**
     * Return the number of entries in the filter list. ldap.filter.size
     *
     * @return the number of entries in set.
     */
    public int getLdapFilterSize()
    {
        return ldapFilterSize;
    }

    /**
     * Set the number of entries in the filter list. ldap.filter.size
     *
     * @param ldapFilterSize corresponds to the number of entries in the ldap filter set.
     */
    public void setLdapFilterSize(int ldapFilterSize)
    {
        this.ldapFilterSize = ldapFilterSize;
    }

    /**
     * Contains the unsafe characters set by fortress.properties named ldap.filter.
     *
     * @return the set of unsafe characters.
     */
    public char[] getLdapMetaChars()
    {
        return ldapMetaChars;
    }

    /**
     * Set the unsafe characters by using ldap.filter specified by fortress.properties.
     *
     * @param ldapMetaChars contains the set of unsafe chars
     */
    public void setLdapMetaChars(char[] ldapMetaChars)
    {
        this.ldapMetaChars = ldapMetaChars.clone();
    }

    /**
     * Contains the safe replacement characters.
     *
     * @return the safe character set.
     */
    public String[] getLdapReplVals()
    {
        return ldapReplVals;
    }

    /**
     * Set the ldap replacement chars.
     *
     * @param ldapReplVals contains the set of encoded chars
     */
    public void setLdapReplVals(String[] ldapReplVals)
    {
        this.ldapReplVals = ldapReplVals.clone();
    }
}
