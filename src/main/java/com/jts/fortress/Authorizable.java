/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress;


import com.jts.fortress.rbac.Session;

/**
 * Interface is used to allow Fortress clients to load administrative session into a Manager impl.  Once the ARBAC session has
 * been loaded into the callable objects, authorization will be performed on behalf of the user who is contained within
 * the Session object.  The permissions checked will be administrative permissions.
 * <p/>

 * @author Shawn McKinney
 * @created January 9, 2011
 */
public interface Authorizable
{
    /**
     * Use this method to load an administrative user's ARBAC Session object into Manager object will enable authorization to
     * be performed on behalf of admin user.
     * @param session contains a valid Fortress ARBAC Session object.
     */
    public void setAdmin(Session session);
}

