package com.jts.fortress.util.cache;

import com.jts.fortress.rbac.SDSet;

public class DsdCacheEntry
{
    private String name;
    private String member;
    private SDSet sdSet;
    private boolean empty;

    public boolean isEmpty()
    {
        return empty;
    }

    public void setEmpty(boolean empty)
    {
        this.empty = empty;
    }

    public DsdCacheEntry(String member, SDSet sdSet, boolean empty)
    {
        this.sdSet = sdSet;
        this.member = member;
        this.empty = empty;
    }

    public String getMember()
    {
        return member;
    }

    public void setMember(String member)
    {
        this.member = member;
    }

    public SDSet getSdSet()
    {
        return sdSet;
    }

    public void setSdSet(SDSet sdSet)
    {
        this.sdSet = sdSet;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}