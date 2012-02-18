/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.arbac;

/**
 *  Description of the Class
 *
 *@author     smckinn
 *@created    December 5, 2010
 */
public class PRA
		 implements java.io.Serializable, Comparable
{
    private String pou;
    private String urole;
    private String arole;
    private boolean canAssign;

    @Override
    public int hashCode()
    {
        int result = pou != null ? pou.hashCode() : 0;
        result = 31 * result + (urole != null ? urole.hashCode() : 0);
        result = 31 * result + (arole != null ? arole.hashCode() : 0);
        result = 31 * result + (canAssign ? 1 : 0);
        return result;
    }

    public PRA(String arole, String pou, String urole, boolean canAssign)
    {
        this.arole = arole;
        this.pou = pou;
        this.urole = urole;
        this.canAssign = canAssign;
    }
    
    public int compareTo(Object o)
    {
        PRA k1 = this;
        PRA k2 = (PRA) o;
        String s1 = k1.getPou() + k1.getUrole() + k1.getArole();
        String s2 = k2.getPou() + k2.getUrole() + k2.getArole();
        return s1.compareToIgnoreCase(s2);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PRA that = (PRA) o;

        if (!this.getPou().equalsIgnoreCase(that.getPou())) return false;
        if (!this.getUrole().equalsIgnoreCase(that.getUrole())) return false;
        if (!this.getArole().equalsIgnoreCase(that.getArole())) return false;
        return true;
    }

    public String getPou()
    {
        return pou;
    }

    public void setPou(String pou)
    {
        this.pou = pou;
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

