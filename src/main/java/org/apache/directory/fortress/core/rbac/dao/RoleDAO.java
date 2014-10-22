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
import org.apache.directory.fortress.core.RemoveException;
import org.apache.directory.fortress.core.UpdateException;
import org.apache.directory.fortress.core.rbac.Graphable;
import org.apache.directory.fortress.core.rbac.Role;


/**
 * This class perform data access for Fortress Role entity.
 * <p/>
 * The Fortress Role entity is a composite of the following other Fortress structural and aux object classes:
 * <h4>1. ftRls Structural objectclass is used to store the Role information like name and temporal constraint attributes</h4>
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
 * <h4>3. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity</h4>
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
 * @author Emmanuel Lecharny
 */
public interface RoleDAO
{
    /**
     * @param entity
     * @return
     * @throws CreateException
     */
    Role create( Role entity ) throws CreateException;


    /**
     * @param entity
     * @return
     * @throws org.apache.directory.fortress.core.UpdateException
     *
     */
    Role update( Role entity ) throws UpdateException;


    /**
     *
     * @param entity
     * @throws UpdateException
     */
    void deleteParent( Role entity ) throws UpdateException;


    /**
     * @param entity
     * @param userDn
     * @return
     * @throws org.apache.directory.fortress.core.UpdateException
     *
     */
    Role assign( Role entity, String userDn ) throws UpdateException;


    /**
     * @param entity
     * @param userDn
     * @return
     * @throws org.apache.directory.fortress.core.UpdateException
     *
     */
    Role deassign( Role entity, String userDn ) throws UpdateException;


    /**
     * @param role
     * @throws RemoveException
     */
    void remove( Role role ) throws RemoveException;


    /**
     * @param role
     * @return
     * @throws org.apache.directory.fortress.core.FinderException
     *
     */
    Role getRole( Role role ) throws FinderException;


    /**
     * @param role
     * @return
     * @throws org.apache.directory.fortress.core.FinderException
     *
     */
    List<Role> findRoles( Role role ) throws FinderException;


    /**
     * @param role
     * @param limit
     * @return
     * @throws org.apache.directory.fortress.core.FinderException
     *
     */
    List<String> findRoles( Role role, int limit ) throws FinderException;


    /**
     *
     * @param userDn
     * @param contextId
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