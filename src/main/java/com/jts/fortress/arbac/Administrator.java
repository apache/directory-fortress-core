/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.arbac;

import java.util.Set;

/**
 * This interface is used by Fortress admin role processing.  It prescribes the APIs that are necessary for an Administrative
 * Role entity to fullfill the ARBAC functionality.
 * <p/>

 *
 * @author smckinn
 * @created November 13, 2010
 */
public interface Administrator
{

    /**
     * Get a collection of optional Perm OU attributes that were stored on the AdminRole entity.
     *
     * @return List of type String containing Perm OU.  This maps to 'ftOSP' attribute on 'ftPools' aux object class.
     */
    public Set<String> getOsP();

    /**
     * Set a collection of optional Perm OU attributes to be stored on the AdminRole entity.
     *
     * @param osPs is a List of type String containing Perm OU.  This maps to 'ftOSP' attribute on 'ftPools' aux object class.
     */
    public void setOsP(Set<String> osPs);

    /**
     * Set a Perm OU attribute to be stored on the AdminRole entity.
     *
     * @param osP is a Perm OU that maps to 'ftOSP' attribute on 'ftPools' aux object class.
     */
    public void setOsP(String osP);

    /**
     * Get a collection of optional User OU attributes that were stored on the AdminRole entity.
     *
     * @return List of type String containing User OU.  This maps to 'ftOSU' attribute on 'ftPools' aux object class.
     */
    public Set<String> getOsU();

    /**
     * Set a collection of optional User OU attributes to be stored on the AdminRole entity.
     *
     * @param osUs is a List of type String containing User OU.  This maps to 'ftOSU' attribute on 'ftPools' aux object class.
     */
    public void setOsU(Set<String> osUs);

    /**
     * Set a User OU attribute to be stored on the AdminRole entity.
     *
     * @param osU is a User OU that maps to 'ftOSU' attribute on 'ftPools' aux object class.
     */
    public void setOsU(String osU);

    /**
     * Load the role range attributes given a raw format.  This method is used internal to Fortress and is not intended
     * to be used by external callers.
     *
     * @param szRaw maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    void setRoleRangeRaw(String szRaw);

    /**
     * Get the raw format for role range using current AdminRole entity attributes.  This method is used internal to Fortress and is not intended
     * to be used by external callers.
     *
     * @return String maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    public String getRoleRangeRaw();

    /**
     * Return the begin Role range attribute for AdminRole entity.
     *
     * @return String that maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    public String getBeginRange();

    /**
     * Set the begin Role range attribute for AdminRole entity.
     *
     * @param beginRange maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    public void setBeginRange(String beginRange);

    /**
     * Return the end Role range attribute for AdminRole entity.
     *
     * @return String that maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    public String getEndRange();

    /**
     * Set the end Role range attribute for AdminRole entity.
     *
     * @param endRange maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    public void setEndRange(String endRange);

    /**
     * Set the begin inclusive which specifies if role range includes or excludes the 'beginRange' attribute.
     *
     * @return String that maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    public boolean isBeginInclusive();

    /**
     * Get the begin inclusive which specifies if role range includes or excludes the 'beginRange' attribute.
     *
     * @param beginInclusive maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    public void setBeginInclusive(boolean beginInclusive);

    /**
     * Set the end inclusive which specifies if role range includes or excludes the 'endRange' attribute.
     *
     * @return String that maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    public boolean isEndInclusive();

    /**
     * Get the end inclusive which specifies if role range includes or excludes the 'endRange' attribute.
     *
     * @param endInclusive maps to 'ftRange' attribute on 'ftPools' aux object class.
     */
    public void setEndInclusive(boolean endInclusive);
}

