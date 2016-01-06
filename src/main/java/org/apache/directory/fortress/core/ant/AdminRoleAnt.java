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
package org.apache.directory.fortress.core.ant;


import org.apache.directory.fortress.core.model.AdminRole;

import java.util.StringTokenizer;


/**
 * Entity is used by custom Apache Ant task for special handling of collections.  This is necessary because the
 * Ant parser cannot deal with complex data attribute types.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AdminRoleAnt extends AdminRole
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;
    private String osPs;
    private String osUs;


    /**
     * Default constructor required for all Ant entities.
     *
     * @return String containing OSPs.
     */
    public String getOSPs()
    {
        return osPs;
    }


    /**
     * Set the list of Perm OUs as a comma delimited string.  This method will convert from that format to
     * the AdminRole native format which is collection of Strings.
     *
     * @param osPs The list of Perm OUs
     */
    public void setOSPs( String osPs )
    {
        this.osPs = osPs;
        
        if ( osPs != null )
        {
            // allow the setter to process comma delimited strings:
            StringTokenizer tkn = new StringTokenizer( osPs, "," );
            if ( tkn.countTokens() > 0 )
            {
                while ( tkn.hasMoreTokens() )
                {
                    String osP = tkn.nextToken();
                    setOsP( osP );
                    /**
                    * Set the list of Perm OUs as a comma delimited string.  This method will convert from that format to
                    * the AdminRole native format which is collection of Strings.
                    * @param osPs
                    */

                }
            }
        }
    }


    /**
     * Return the comma delimited OU string.
     *
     * @return String containing OSUs.
     */
    public String getOSUs()
    {
        return osUs;
    }


    /**
     * Load an OU into the collection of OUs stored by collection.
     *
     * @param osUs contains OU name.
     */
    public void setOSUs( String osUs )
    {
        this.osUs = osUs;
        if ( osUs != null )
        {
            // allow the setter to process comma delimited strings:
            StringTokenizer tkn = new StringTokenizer( osUs, "," );
            if ( tkn.countTokens() > 0 )
            {
                while ( tkn.hasMoreTokens() )
                {
                    String osU = tkn.nextToken();
                    setOsU( osU );
                }
            }
        }
    }
}
