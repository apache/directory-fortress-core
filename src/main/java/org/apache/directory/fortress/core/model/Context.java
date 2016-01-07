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


/**
 * This class contains the Context id which is used as container for segregating data by customer 
 * within the LDAP Directory Information Tree.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Context
{
    /** The context ID */
    private String name;
    
    /** The content description */ 
    private String description;


    /**
     * Generate instance of context.
     *
     * @param name        contains the id to use for sub-directory within the DIT.
     * @param description maps to 'description' attribute in 'organizationalUnit' object class.
     */
    public Context(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    /**
     * Default constructor used by {@link org.apache.directory.fortress.core.ant.FortressAntTask}
     */
    public Context()
    {
    }
    

    /**
     * Get the id to use for sub-directory within the DIT.  This attribute is required.
     *
     * @return name maps to 'dcObject' object class.
     */
    public String getName()
    {
        return name;
    }
    

    /**
     * Set the id to use for sub-directory within the DIT.  This attribute is required.
     *
     * @param name maps to 'dcObject' object class.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    

    /**
     * Get the description for the context.  This value is not required or constrained
     * but is validated on reasonability.
     *
     * @return field maps to 'description' attribute on 'organizationalUnit'.
     */
    public String getDescription()
    {
        return description;
    }
    

    /**
     * Set the description for the context.  This value is not required or constrained
     * but is validated on reasonability.
     *
     * @param description maps to to 'description' attribute on 'organizationalUnit'.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * @see Object#toString()
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append( "Context object: \n" );
        sb.append( "    name :" ).append( name ).append( '\n' );
        sb.append( "    description :" ).append( description ).append( '\n' );

        return sb.toString();
    }
}
