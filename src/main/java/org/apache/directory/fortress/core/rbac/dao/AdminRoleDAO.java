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
package org.apache.directory.fortress.core.rbac.dao;


import java.util.List;

import org.apache.directory.fortress.core.CreateException;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.RemoveException;
import org.apache.directory.fortress.core.UpdateException;
import org.apache.directory.fortress.core.rbac.AdminRole;
import org.apache.directory.fortress.core.rbac.AdminRoleP;
import org.apache.directory.fortress.core.rbac.Graphable;


/**
 * The AdminRoleDAO is called by {@link AdminRoleP} and processes data via its entity {@link AdminRole}.
 * <p/>
 * The Fortress AdminRoleDAO uses the following other Fortress structural and aux object classes:
 * <h4>1. ftRls Structural objectclass is used to store the AdminRole information like name, and temporal constraints</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass   ( 1.3.6.1.4.1.38088.2.1</code>
 * <li> <code>NAME 'ftRls'</code>
 * <li> <code>DESC 'Fortress Role Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftRoleName )</code>
 * <li> <code>MAY ( description $ ftCstr ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>2. ftProperties AUXILIARY Object Class is used to store client specific name/value pairs on target entity</h4>
 * <code># This aux object class can be used to store custom attributes.</code><br />
 * <code># The properties collections consist of name/value pairs and are not constrainted by Fortress.</code><br />
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.2</code>
 * <li> <code>NAME 'ftProperties'</code>
 * <li> <code>DESC 'Fortress Properties AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY ( ftProps ) ) </code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>3. ftPools Auxiliary object class store the ARBAC Perm and User OU assignments on AdminRole entity</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.3</code>
 * <li> <code>NAME 'ftPools'</code>
 * <li> <code>DESC 'Fortress Pools AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY ( ftOSU $ ftOSP ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>4. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity</h4>
 * <ul>
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.4</code>
 * <li> <code>NAME 'ftMods'</code>
 * <li> <code>DESC 'Fortress Modifiers AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY (</code>
 * <li> <code>ftModifier $</code>
 * <li> <code>ftModCode $</code>
 * <li> <code>ftModId ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * This class is thread safe.
 *
 * @author Shawn McKinney
 */
public interface AdminRoleDAO
{
    /**
     * Create a new AdminRole entity using supplied data.  Required attribute is {@link AdminRole#name}.
     * This data will be stored in the {@link GlobalIds#ADMIN_ROLE_ROOT} container.
     *
     * @param entity record contains AdminRole data.  Null attributes will be ignored.
     * @return input record back to client.
     * @throws org.apache.directory.fortress.core.CreateException in the event LDAP errors occur.
     */
    AdminRole create( AdminRole entity ) throws CreateException;


    /**
     * Update existing AdminRole entity using supplied data.  Required attribute is {@link AdminRole#name}.
     * This data will be stored in the {@link GlobalIds#ADMIN_ROLE_ROOT} container.
     *
     * @param entity record contains AdminRole data.  Null attributes will be ignored.
     * @return input record back to client.
     * @throws UpdateException in the event LDAP errors occur.
     */
    AdminRole update( AdminRole entity ) throws UpdateException;


    /**
     *
     * @param entity
     * @throws UpdateException
     */
    void deleteParent( AdminRole entity ) throws UpdateException;


    /**
     * This method will add the supplied DN as a role occupant to the target record.
     * This data will be stored in the {@link GlobalIds#ADMIN_ROLE_ROOT} container.
     *
     * @param entity record contains {@link AdminRole#name}.  Null attributes will be ignored.
     * @param userDn contains the DN for userId who is being assigned.
     * @return input record back to client.
     * @throws UpdateException in the event LDAP errors occur.
     */
    AdminRole assign( AdminRole entity, String userDn ) throws UpdateException;


    /**
     * This method will remove the supplied DN as a role occupant to the target record.
     * This data will be stored in the {@link GlobalIds#ADMIN_ROLE_ROOT} container.
     *
     * @param entity record contains {@link AdminRole#name}.  Null attributes will be ignored.
     * @param userDn contains the DN for userId who is being deassigned.
     * @return input record back to client.
     * @throws UpdateException in the event LDAP errors occur.
     */
    AdminRole deassign( AdminRole entity, String userDn ) throws UpdateException;


    /**
     * This method will completely remove the AdminRole from the directory.  It will use {@link AdminRole#name} as key.
     * This operation is performed on the {@link GlobalIds#ADMIN_ROLE_ROOT} container.
     *
     * @param role record contains {@link AdminRole#name}.
     * @throws RemoveException in the event LDAP errors occur.
     */
    void remove( AdminRole role ) throws RemoveException;


    /**
     * This method will retrieve the AdminRole from {@link GlobalIds#ADMIN_ROLE_ROOT} container by name.
     *
     * @param adminRole maps to {@link AdminRole#name}.
     * @return AdminRole back to client.
     * @throws FinderException in the event LDAP errors occur.
     */
    AdminRole getRole( AdminRole adminRole ) throws FinderException;


    /**
     * @param adminRole
     * @return
     * @throws FinderException
     *
     */
    List<AdminRole> findRoles( AdminRole adminRole ) throws FinderException;


    /**
     * @param adminRole
     * @param limit
     * @return
     * @throws FinderException
     *
     */
    List<String> findRoles( AdminRole adminRole, int limit ) throws FinderException;


    /**
     * @param userDn
     * @return
     * @throws FinderException
     */
    List<String> findAssignedRoles( String userDn, String contextId ) throws FinderException;


    /**
      *
      * @param contextId
      * @return
      * @throws FinderException
      */
    List<Graphable> getAllDescendants( String contextId ) throws FinderException;
}
