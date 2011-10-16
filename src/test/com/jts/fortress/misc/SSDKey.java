/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.misc;

import java.lang.String;

public class SSDKey
    implements java.io.Serializable
    //, java.util.Comparator
{
    public SSDKey(String r1, String r2)
    {
        this.r1 = r1;
        this.r2 = r2;
    }
    public String getR1()
    {
        return r1;
    }

    public void setR1(String r1)
    {
        this.r1 = r1;
    }

    public String getR2()
    {
        return r2;
    }

    public void setR2(String r2)
    {
        this.r2 = r2;
    }

    private String r1;
    private String r2;
	

	public final int hashCode()
	{
		return r1.hashCode() + r2.hashCode();
	}

    //public int compare(Object o1, Object o2)
    //{
    //    SSDKey k1 = (SSDKey) o1;
    //    SSDKey k2 = (SSDKey) o2;
    //    String s1 = k1.getR1() + k1.getR2();
    //    String s2 = k2.getR1() + k2.getR2();
    //    return s1.compareToIgnoreCase(s2);
    //}

    public boolean equals(Object thatObj)
    {
        if (this == thatObj) return true;
        if (this.getR1() == null || this.getR2() == null) return false;
        if (!(thatObj instanceof SSDKey)) return false;
        SSDKey thatKey = (SSDKey) thatObj;
        if (thatKey.getR1() == null || thatKey.getR2() == null) return false;
        return ((thatKey.getR1().equalsIgnoreCase(this.getR1()) && thatKey.getR2().equalsIgnoreCase(this.getR2())) || (thatKey.getR1().equalsIgnoreCase(this.getR2()) && thatKey.getR2().equalsIgnoreCase(this.getR1())));
    }
}

