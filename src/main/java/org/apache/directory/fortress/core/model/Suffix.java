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
 * A class storing the suffix information
 */
public class Suffix
{
    /** Top level domain component */
    private String dc;
    
    /** top level domain component specifier */
    private String dc2;
    
    /** Second level domain component name */
    private String name;
    
    /** The suffix description */
    private String description;


    /**
     * Generate instance of suffix to be loaded as domain component ldap object.
     *
     * @param dc          top level domain component maps to 'dc' (i.e. 'com') attribute in 'dcObject' object class.
     * @param name        second level domain component name maps to attribute in 'dcObject' object class.
     * @param description maps to 'o' attribute in 'dcObject' object class.
     */
    public Suffix( String dc, String name, String description )
    {
        this.dc = dc;
        this.name = name;
        this.description = description;
    }
    

    /**
     * Default constructor used by {@link org.apache.directory.fortress.core.ant.FortressAntTask}
     */
    public Suffix()
    {
    }
    

    /**
     * Get the second level qualifier on the domain component.  This attribute is required.
     *
     * @return name maps to 'dcObject' object class.
     */
    public String getName()
    {
        return name;
    }
    

    /**
     * Set the second level qualifier on the domain component.  This attribute is required.
     *
     * @param name maps to 'dcObject' object class.
     */
    public void setName( String name )
    {
        this.name = name;
    }
    

    /**
     * Get the description for the domain component.  This value is not required or constrained
     * but is validated on reasonability.
     *
     * @return field maps to 'o' attribute on 'dcObject'.
     */
    public String getDescription()
    {
        return description;
    }
    

    /**
     * Set the description for the domain component.  This value is not required or constrained
     * but is validated on reasonability.
     *
     * @param description maps to 'o' attribute on 'dcObject'.
     */
    public void setDescription( String description )
    {
        this.description = description;
    }
    

    /**
     * Get top level domain component specifier, i.e. dc=com.  This attribute is required.
     *
     * @return dc maps to 'dc' in 'dcObject' object class.
     */
    public String getDc()
    {
        return dc;
    }
    

    /**
     * Set top level domain component specifier, i.e. dc=com.  This attribute is required.
     *
     * @param dc maps to 'dc' in 'dcObject' object class.
     */
    public void setDc( String dc )
    {
        this.dc = dc;
    }
    

    /**
     * Get top level domain component specifier, i.e. dc=com for a three part dc structure.  This attribute is optional.
     *
     * @return dc maps to 'dc' in 'dcObject' object class.
     */
    public String getDc2()
    {
        return dc2;
    }
    

    /**
     * Get top level domain component specifier, i.e. dc=com for three part dc structure.  This attribute is optional.
     *
     */
    public void setDc2( String dc2 )
    {
        this.dc2 = dc2;
    }


    /**
     * @see Object#toString()
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append( "Suffix object: \n" );

        sb.append( "    dc :" ).append( dc ).append( '\n' );
        sb.append( "    dc2 :" ).append( dc2 ).append( '\n' );
        sb.append( "    name :" ).append( name ).append( '\n' );
        sb.append( "    description :" ).append( description ).append( '\n' );

        return sb.toString();
    }
}

