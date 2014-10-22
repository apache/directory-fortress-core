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
import java.util.Set;

import org.apache.directory.fortress.core.CreateException;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.RemoveException;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.UpdateException;
import org.apache.directory.fortress.core.rbac.AdminRole;
import org.apache.directory.fortress.core.rbac.OrgUnit;
import org.apache.directory.fortress.core.rbac.Role;
import org.apache.directory.fortress.core.rbac.Session;
import org.apache.directory.fortress.core.rbac.User;
import org.apache.directory.fortress.core.rbac.UserAdminRole;
import org.apache.directory.fortress.core.rbac.UserRole;


/**
 * Data access class for LDAP User entity.
 * <p/>
 * <p/>
 * The Fortress User LDAP schema follows:
 * <p/>
 * <h4>1. InetOrgPerson Structural Object Class </h4>
 * <code># The inetOrgPerson represents people who are associated with an</code><br />
 * <code># organization in some way.  It is a structural class and is derived</code><br />
 * <code># from the organizationalPerson which is defined in X.521 [X521].</code><br />
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.16.840.1.113730.3.2.2</code>
 * <li> <code>NAME 'inetOrgPerson'</code>
 * <li> <code>DESC 'RFC2798: Internet Organizational Person'</code>
 * <li> <code>SUP organizationalPerson</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MAY ( audio $ businessCategory $ carLicense $ departmentNumber $</code>
 * <li> <code>displayName $ employeeNumber $ employeeType $ givenName $</code>
 * <li> <code>homePhone $ homePostalAddress $ initials $ jpegPhoto $</code>
 * <li> <code>labeledURI $ mail $ manager $ mobile $ o $ pager $ photo $</code>
 * <li> <code>roomNumber $ secretary $ uid $ userCertificate $</code>
 * <li> <code>x500uniqueIdentifier $ preferredLanguage $</code>
 * <li> <code>userSMIMECertificate $ userPKCS12 ) )</code>
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
 * <p/>
 * <h4>3. ftUserAttrs is used to store user RBAC and Admin role assignment and other security attributes on User entity</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.1</code>
 * <li> <code>NAME 'ftUserAttrs'</code>
 * <li> <code>DESC 'Fortress User Attribute AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MUST ( ftId )</code>
 * <li> <code>MAY ( ftRC $ ftRA $ ftARC $ ftARA $ ftCstr</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>4. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.</h4>
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
public interface UserDAO
{
    List<User> findUsers( OrgUnit ou, boolean limitSize ) throws FinderException;


    List<User> findUsers( User user ) throws FinderException;


    List<String> findUsers( User user, int limit ) throws FinderException;


    String assign( UserRole uRole ) throws UpdateException, FinderException;


    /**
     * @param uRole
     * @return
     * @throws UpdateException
     *
     * @throws FinderException
     *
     */
    String assign( UserAdminRole uRole ) throws UpdateException, FinderException;


    boolean changePassword( User entity, char[] newPassword ) throws SecurityException;


    Session checkPassword( User user ) throws FinderException;


    List<User> getAuthorizedUsers( Role role ) throws FinderException;


    User update( User entity ) throws UpdateException;


    void lock( User user ) throws UpdateException;


    void unlock( User user ) throws UpdateException;


    User create( User entity ) throws CreateException;


    /**
     * @param uRole
     * @return
     * @throws UpdateException
     *
     * @throws FinderException
     *
     */
    String deassign( UserAdminRole uRole ) throws UpdateException, FinderException;


    String deassign( UserRole uRole ) throws UpdateException, FinderException;


    void resetUserPassword( User user ) throws UpdateException;


    User updateProps( User entity, boolean replace ) throws UpdateException;


    String remove( User user ) throws RemoveException;


    String deletePwPolicy( User user ) throws UpdateException;


    /**
     * @param role
     * @return
     * @throws FinderException
     */
    List<User> getAssignedUsers( AdminRole role ) throws FinderException;


    List<User> getAssignedUsers( Role role ) throws FinderException;


    List<String> getRoles( User user ) throws FinderException;


    User getUser( User user, boolean isRoles ) throws FinderException;


    Set<String> getAssignedUsers( Set<String> roles, String contextId ) throws FinderException;


    List<String> getAuthorizedUsers( Role role, int limit ) throws FinderException;
}