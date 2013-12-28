/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import java.lang.String;

/**
 * Contains a parent child data entity that is used for hierarchical processing.  This entity is used to construct edges in graphs.
 * <p/>

 *
 * @author Shawn McKinney
 */
public class Relationship
    implements java.io.Serializable
{
    private String child;
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
    public Relationship(String child, String parent)
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
    public void setChild(String child)
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
    public void setParent(String parent)
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
    public boolean equals(Object thatObj)
    {
        if (this == thatObj)
        {
            return true;
        }
        if (this.getChild() == null || this.getParent() == null)
        {
            return false;
        }
        if (!(thatObj instanceof Relationship))
        {
            return false;
        }
        Relationship thatKey = (Relationship) thatObj;
        if (thatKey.getChild() == null || thatKey.getParent() == null)
        {
            return false;
        }
        return ((thatKey.getChild().equalsIgnoreCase(this.getChild()) && thatKey.getParent().equalsIgnoreCase(this.getParent())));
    }
}
