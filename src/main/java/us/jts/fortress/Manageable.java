/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress;


import us.jts.fortress.rbac.Session;

/**
 * Interface allows outside clients to manage security and multi-tenant concerns within the Fortress runtime.
 * The {@link #setAdmin(us.jts.fortress.rbac.Session)} method allows A/RBAC sessions to be loaded and allows authorization
 * to be performed on behalf of the user who is contained within the Session object itself.
 * The ARBAC permissions will be checked each time outside client makes calls into Fortress API.
 * This interface also allows Fortress clients to operate in a multi-tenant fashion using {@link #setContextId(String)}.
 * <p/>
 *
 * @author Shawn McKinney
 */
public interface Manageable
{
    /**
     * Use this method to load an administrative user's ARBAC Session object into Manager object which will enable authorization to
     * be performed on behalf of admin user.
     * Setting Session into this object will enforce ARBAC controls and render this class' implementer thread unsafe.
     *
     * @param session contains a valid Fortress ARBAC Session object.
     */
    public void setAdmin(Session session);

    /**
     * Use this method to set the tenant id onto function call into Fortress which allows segregation of data by customer.
     * The contextId is used for multi-tenancy to isolate data sets within a particular sub-tree within DIT.
     * Setting contextId into this object will render this class' implementer thread unsafe.
     *
     * @param contextId maps to sub-tree in DIT, for example ou=contextId, dc=jts, dc = com.
     */
    public void setContextId(String contextId);
}