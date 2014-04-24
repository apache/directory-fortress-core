/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac.dao;

import us.jts.fortress.SecurityException;
import us.jts.fortress.rbac.Permission;
import us.jts.fortress.rbac.Session;
import us.jts.fortress.rbac.User;
import us.jts.fortress.rbac.UserRole;


public interface AcceleratorDAO
{
    public Session createSession( User user ) throws SecurityException;
    public void deleteSession( Session session ) throws SecurityException;
    public boolean checkAccess( Session session, Permission perm ) throws SecurityException;
    public void dropActiveRole( Session session, UserRole userRole ) throws SecurityException;
    public void addActiveRole( Session session, UserRole userRole ) throws SecurityException;
}
