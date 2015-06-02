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
package org.apache.directory.fortress.core.impl;


/**
 *  Description of the Class
 *
 *@author     Shawn McKinney
 */
public class URA implements java.io.Serializable, Comparable
{
    private String uou;
    private String urole;
    private String arole;
    private boolean canAssign;


    @Override
    public int hashCode()
    {
        int result = uou != null ? uou.hashCode() : 0;
        result = 31 * result + ( urole != null ? urole.hashCode() : 0 );
        result = 31 * result + ( arole != null ? arole.hashCode() : 0 );
        result = 31 * result + ( canAssign ? 1 : 0 );
        return result;
    }


    public URA( String arole, String uou, String urole, boolean canAssign )
    {
        this.arole = arole;
        this.uou = uou;
        this.urole = urole;
        this.canAssign = canAssign;
    }


    public int compareTo( Object o )
    {
        URA k1 = this;
        URA k2 = ( URA ) o;
        String s1 = k1.getUou() + k1.getUrole() + k1.getArole();
        String s2 = k2.getUou() + k2.getUrole() + k2.getArole();
        return s1.compareToIgnoreCase( s2 );
    }


    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;

        URA that = ( URA ) o;

        if ( !this.getUou().equalsIgnoreCase( that.getUou() ) )
            return false;
        if ( !this.getUrole().equalsIgnoreCase( that.getUrole() ) )
            return false;
        return this.getArole().equalsIgnoreCase( that.getArole() );
    }


    public String getUou()
    {
        return uou;
    }


    public void setUou( String uou )
    {
        this.uou = uou;
    }


    public String getUrole()
    {
        return urole;
    }


    public void setUrole( String urole )
    {
        this.urole = urole;
    }


    public String getArole()
    {
        return arole;
    }


    public void setArole( String arole )
    {
        this.arole = arole;
    }


    public boolean isCanAssign()
    {
        return canAssign;
    }


    public void setCanAssign( boolean canAssign )
    {
        this.canAssign = canAssign;
    }
}
