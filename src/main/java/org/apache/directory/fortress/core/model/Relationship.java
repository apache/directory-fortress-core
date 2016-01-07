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

import java.io.Serializable;

/**
 * Contains a parent child data entity that is used for hierarchical processing.  This entity is used to construct edges in graphs.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Relationship implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** The child */
    private String child;
    
    /** The parent */
    private String parent;

    /**
     * No argument constructor is necessary for Ant admin utility
     *
     */
    public Relationship()
    {
    }
    
    
    /**
     * Construct a new relationship given a child and parent name.
     *
     * @param child  contains the name of child.
     * @param parent contains the name of parent.
     */
    public Relationship( String child, String parent )
    {
        this.child = child;
        this.parent = parent;
    }
    

    /**
     * Return the child name.
     *
     * @return name of child.
     */
    public String getChild()
    {
        return child;
    }
    

    /**
     * Set the child name.
     *
     * @param child contains the name of child.
     */
    public void setChild( String child )
    {
        this.child = child;
    }
    

    /**
     * Return the parent name.
     *
     * @return name of parent.
     */
    public String getParent()
    {
        return parent;
    }
    

    /**
     * Set the parent name.
     *
     * @param parent contains the name of parent.
     */
    public void setParent( String parent )
    {
        this.parent = parent;
    }
    

    /**
     * Compute the hashcode on the parent and child values.  This is used for list processing.
     *
     * @return hashcode that includes parent concatenated with child.
     */
    public final int hashCode()
    {
        return child.hashCode() + parent.hashCode();
    }
    

    /**
     * Matches the parent and child values from two Relationship entities.
     *
     * @param thatObj contains a Relationship entity.
     * @return boolean indicating both objects contain matching parent and child names.
     */
    public boolean equals (Object thatObj )
    {
        if ( this == thatObj )
        {
            return true;
        }
        
        if ( ( this.getChild() == null ) || ( this.getParent() == null ) )
        {
            return false;
        }
        
        if ( !( thatObj instanceof Relationship ) )
        {
            return false;
        }
        
        Relationship thatKey = (Relationship) thatObj;
        
        if ( ( thatKey.getChild() == null ) || ( thatKey.getParent() == null ) )
        {
            return false;
        }
        
        return ( thatKey.getChild().equalsIgnoreCase( this.getChild() ) 
                 && thatKey.getParent().equalsIgnoreCase( this.getParent() ) );
    }


    /**
     * @see Object#toString()
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append( "Relationship object: \n" );

        sb.append( "    parent :" ).append( parent ).append( '\n' );
        sb.append( "    child :" ).append( child ).append( '\n' );

        return sb.toString();
    }
}
