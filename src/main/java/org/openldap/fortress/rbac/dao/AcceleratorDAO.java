/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.openldap.fortress.rbac.dao;

import org.openldap.fortress.SecurityException;
import org.openldap.fortress.rbac.Permission;
import org.openldap.fortress.rbac.Session;
import org.openldap.fortress.rbac.User;
import org.openldap.fortress.rbac.UserRole;

import java.util.List;


public interface AcceleratorDAO
{
    public Session createSession( User user ) throws SecurityException;
    public void deleteSession( Session session ) throws SecurityException;
    public List<UserRole> sessionRoles( Session session ) throws SecurityException;
    public boolean checkAccess( Session session, Permission perm ) throws SecurityException;
    public void dropActiveRole( Session session, UserRole userRole ) throws SecurityException;
    public void addActiveRole( Session session, UserRole userRole ) throws SecurityException;
}
