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

package org.openldap.fortress.rbac.dao;

import org.openldap.fortress.SecurityException;
import org.openldap.fortress.rbac.Permission;
import org.openldap.fortress.rbac.Session;
import org.openldap.fortress.rbac.User;
import org.openldap.fortress.rbac.UserRole;


public interface AcceleratorDAO
{
    public Session createSession( User user ) throws SecurityException;
    public void deleteSession( Session session ) throws SecurityException;
    public boolean checkAccess( Session session, Permission perm ) throws SecurityException;
    public void dropActiveRole( Session session, UserRole userRole ) throws SecurityException;
    public void addActiveRole( Session session, UserRole userRole ) throws SecurityException;
}
