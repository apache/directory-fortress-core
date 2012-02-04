/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.arbac;

/**
 *  Description of the Class
 *
 *@author     smckinn
 *@created    December 5, 2010
 */
public class URA
		 implements java.io.Serializable, Comparable
{
    private String uou;
    private String urole;
    private String arole;
    private boolean canAssign;

    @Override
    public int hashCode()
    {
        int result = uou != null ? uou.hashCode() : 0;
        result = 31 * result + (urole != null ? urole.hashCode() : 0);
        result = 31 * result + (arole != null ? arole.hashCode() : 0);
        result = 31 * result + (canAssign ? 1 : 0);
        return result;
    }

    public URA(String arole, String uou, String urole, boolean canAssign)
    {
        this.arole = arole;
        this.uou = uou;
        this.urole = urole;
        this.canAssign = canAssign;
    }
    
    public int compareTo(Object o)
    {
        URA k1 = this;
        URA k2 = (URA) o;
        String s1 = k1.getUou() + k1.getUrole() + k1.getArole();
        String s2 = k2.getUou() + k2.getUrole() + k2.getArole();
        return s1.compareToIgnoreCase(s2);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        URA that = (URA) o;

        if (!this.getUou().equalsIgnoreCase(that.getUou())) return false;
        if (!this.getUrole().equalsIgnoreCase(that.getUrole())) return false;
        if (!this.getArole().equalsIgnoreCase(that.getArole())) return false;
        return true;
    }

    public String getUou()
    {
        return uou;
    }

    public void setUou(String uou)
    {
        this.uou = uou;
    }

    public String getUrole()
    {
        return urole;
    }

    public void setUrole(String urole)
    {
        this.urole = urole;
    }

    public String getArole()
    {
        return arole;
    }

    public void setArole(String arole)
    {
        this.arole = arole;
    }

    public boolean isCanAssign()
    {
        return canAssign;
    }

    public void setCanAssign(boolean canAssign)
    {
        this.canAssign = canAssign;
    }
}

