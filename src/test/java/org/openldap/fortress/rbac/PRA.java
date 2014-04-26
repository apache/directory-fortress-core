/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */

package org.openldap.fortress.rbac;


/**
 *  Description of the Class
 *
 *@author     Shawn McKinney
 */
public class PRA implements java.io.Serializable, Comparable
{
    private String pou;
    private String urole;
    private String arole;
    private boolean canAssign;


    @Override
    public int hashCode()
    {
        int result = pou != null ? pou.hashCode() : 0;
        result = 31 * result + ( urole != null ? urole.hashCode() : 0 );
        result = 31 * result + ( arole != null ? arole.hashCode() : 0 );
        result = 31 * result + ( canAssign ? 1 : 0 );
        return result;
    }


    public PRA( String arole, String pou, String urole, boolean canAssign )
    {
        this.arole = arole;
        this.pou = pou;
        this.urole = urole;
        this.canAssign = canAssign;
    }


    public int compareTo( Object o )
    {
        PRA k1 = this;
        PRA k2 = ( PRA ) o;
        String s1 = k1.getPou() + k1.getUrole() + k1.getArole();
        String s2 = k2.getPou() + k2.getUrole() + k2.getArole();
        return s1.compareToIgnoreCase( s2 );
    }


    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;

        PRA that = ( PRA ) o;

        if ( !this.getPou().equalsIgnoreCase( that.getPou() ) )
            return false;
        if ( !this.getUrole().equalsIgnoreCase( that.getUrole() ) )
            return false;
        return this.getArole().equalsIgnoreCase( that.getArole() );
    }


    public String getPou()
    {
        return pou;
    }


    public void setPou( String pou )
    {
        this.pou = pou;
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
