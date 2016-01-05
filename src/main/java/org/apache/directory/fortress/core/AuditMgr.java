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
package org.apache.directory.fortress.core;


import org.apache.directory.fortress.core.model.AuthZ;
import org.apache.directory.fortress.core.model.Mod;
import org.apache.directory.fortress.core.model.UserAudit;
import org.apache.directory.fortress.core.model.Bind;

import java.util.List;


/**
 * This interface prescribes methods used to search <a href="http://www.openldap.org/">OpenLDAP</a>'s slapd access log.  
 * The access log events are persisted in 
 * <a href="http://www.oracle.com/technetwork/database/berkeleydb/overview/index.html">BDB</a> and available for inquiry 
 * via common LDAP protocols.
 * Audit entries stored on behalf of Fortress operations correspond to runtime authentication 
 * {@link org.apache.directory.fortress.core.model.Bind}, authorization 
 * {@link org.apache.directory.fortress.core.model.AuthZ} and modification 
 * {@link org.apache.directory.fortress.core.model.Mod}
 * events as they occur automatically on the server when audit is enabled.
 * <h3></h3>
 * <h4>Audit Interrogator</h4>
 * Provides an OpenLDAP access log retrieval mechanism that enables security event monitoring.
 * <ol>
 *   <li>Authentication events:</li>
 *   <li>
 *     <ul>
 *       <li>Session enablement events</li>
 *       <li>Authorization events</li>
 *       <li>Entity mods and deletes</li>
 *     </ul>
 *   </li>
 * </ol>
 * <img src="./doc-files/Audit.png" alt="">
 * <p>
 * All events include Fortress context, see {@link org.apache.directory.fortress.core.model.FortEntity}.
 * <h4>
 *   The following APIs generate events subsequently stored in this access log:
 * </h4>
 * <ul>
 *   <li>{@link AccessMgr}</li>
 *   <li>{@link AdminMgr}</li>
 *   <li>{@link AdminMgr}</li>
 *   <li>{@link DelAdminMgr}</li>
 *   <li>{@link ConfigMgr}</li>
 *   <li>{@link PwPolicyMgr}</li>
 * </ul>
 * <h4>
 *   The following reports are supported using search input: {@link org.apache.directory.fortress.core.model.UserAudit}
 * </h4>
 * <ul>
 *   <li>
 *     User Authentications:     <code>List&lt;{@link org.apache.directory.fortress.core.model.Bind}&gt;  
 *     {@link AuditMgr#searchBinds(org.apache.directory.fortress.core.model.UserAudit)}</code>
 *   </li>
 *   <li>
 *     Invalid User AuthN:       <code>List&lt;{@link org.apache.directory.fortress.core.model.Bind}&gt;  
 *     {@link AuditMgr#searchInvalidUsers(org.apache.directory.fortress.core.model.UserAudit)} </code>
 *   </li>
 *   <li>
 *     User Authorizations 1:    <code>List&lt;{@link org.apache.directory.fortress.core.model.AuthZ}&gt; 
 *     {@link AuditMgr#getUserAuthZs(org.apache.directory.fortress.core.model.UserAudit)} </code>
 *   </li>
 *   <li>
 *     User Authorizations 2:    <code>List&lt;{@link org.apache.directory.fortress.core.model.AuthZ}&gt; 
 *     {@link AuditMgr#searchAuthZs(org.apache.directory.fortress.core.model.UserAudit)} </code>
 *   </li>
 *   <li>
 *     User Session Activations: <code>List&lt;{@link org.apache.directory.fortress.core.model.Mod}&gt;   
 *     {@link AuditMgr#searchUserSessions(org.apache.directory.fortress.core.model.UserAudit)} </code>
 *   </li>
 *   <li>
 *     Entity Modifications:     <code>List&lt;{@link org.apache.directory.fortress.core.model.Mod}&gt;   
 *     {@link AuditMgr#searchAdminMods(org.apache.directory.fortress.core.model.UserAudit)} </code>
 *   </li>
 * </ul>
 * <p>
 * This interface's implementer will NOT be thread safe if parent instance variables ({@link Manageable#setContextId(String)} 
 * or {@link Manageable#setAdmin(org.apache.directory.fortress.core.model.Session)}) are set.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface AuditMgr extends Manageable
{
    /**
     * This method returns a list of authorization events for a particular user 
     * {@link org.apache.directory.fortress.core.model.UserAudit#userId}
     * and given timestamp field {@link org.apache.directory.fortress.core.model.UserAudit#beginDate}.<br>
     * Method also can discriminate between all events or failed only by setting 
     * {@link org.apache.directory.fortress.core.model.UserAudit#failedOnly}.
     * <h3></h3>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>{@link org.apache.directory.fortress.core.model.UserAudit#userId} - contains the target userId</li>
     *   <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     *   <li>{@link UserAudit#failedOnly} - if set to 'true', return only failed authorization events</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws SecurityException if a runtime system error occurs.
     */
    List<AuthZ> getUserAuthZs( UserAudit uAudit )
        throws SecurityException;


    /**
     * This method returns a list of authorization events for a particular user 
     * {@link org.apache.directory.fortress.core.model.UserAudit#userId},
     * object {@link org.apache.directory.fortress.core.model.UserAudit#objName}, and given timestamp field 
     * {@link org.apache.directory.fortress.core.model.UserAudit#beginDate}.<BR>
     * Method also can discriminate between all events or failed only by setting flag {@link UserAudit#failedOnly}..
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link UserAudit#userId} - contains the target userId</li>
     *   <li>{@link UserAudit#objName} - contains the object (authorization resource) name</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     *   <li>{@link UserAudit#failedOnly} - if set to 'true', return only failed authorization events</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws SecurityException
     *          if a runtime system error occurs.
     */
    List<AuthZ> searchAuthZs( UserAudit uAudit )
        throws SecurityException;


    /**
     * This method returns a list of authentication audit events for a particular user 
     * {@link org.apache.directory.fortress.core.model.UserAudit#userId},
     * and given timestamp field {@link org.apache.directory.fortress.core.model.UserAudit#beginDate}.<BR>
     * <h3></h3>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>{@link org.apache.directory.fortress.core.model.UserAudit#userId} - contains the target userId</li>
     *   <li>
     *     {@link org.apache.directory.fortress.core.model.UserAudit#beginDate} - contains the date in which to begin search
     *   </li>
     *   <li>{@link UserAudit#failedOnly} - if set to 'true', return only failed authorization events</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Bind.  Each Bind object contains one bind event.
     * @throws SecurityException
     *          if a runtime system error occurs.
     */
    List<Bind> searchBinds( UserAudit uAudit )
        throws SecurityException;


    /**
     * This method returns a list of sessions created for a given user {@link UserAudit#userId},
     * and timestamp {@link org.apache.directory.fortress.core.model.UserAudit#beginDate}.<BR>
     * <h3></h3>
     * <h4>required parameters</h4>
     * <ul>
     *   <li>{@link UserAudit#userId} - contains the target userId</li>
     * </ul>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Mod.  Each Mod object in list corresponds to one update or delete event on directory.
     * @throws SecurityException if a runtime system error occurs.
     */
    List<Mod> searchUserSessions( UserAudit uAudit )
        throws SecurityException;


    /**
     * This method returns a list of admin operations events for a particular entity 
     * {@link org.apache.directory.fortress.core.model.UserAudit#dn},
     * object {@link UserAudit#objName} and timestamp {@link org.apache.directory.fortress.core.model.UserAudit#beginDate}.  
     * If the internal userId {@link org.apache.directory.fortress.core.model.UserAudit#internalUserId} is set it will limit 
     * search by that field.
     * <h3></h3>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>
     *     {@link UserAudit#dn} - contains the LDAP distinguished name for the updated object.  For example if caller
     *     wants to find out what changes were made to John Doe's user object this would be 
     *     'uid=jdoe,ou=People,dc=example,dc=com'
     *   </li>
     *   <li>
     *     {@link UserAudit#objName} - contains the object (authorization resource) name corresponding to the event.  For 
     *     example if caller wants to return events where User object was modified, this would be 'updateUser'
     *   </li>
     *   <li
     *     >{@link org.apache.directory.fortress.core.model.UserAudit#internalUserId} - maps to the internalUserId of user 
     *     who changed the record in LDAP.  This maps to {@link org.apache.directory.fortress.core.model.User#internalId}.
     *   </li>
     *   <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     *   <li>{@link UserAudit#endDate} - contains the date in which to end search</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Mod.  Each Mod object in list corresponds to one update or delete event on directory.
     * @throws SecurityException
     *          if a runtime system error occurs.
     */
    List<Mod> searchAdminMods( UserAudit uAudit )
        throws SecurityException;


    /**
     * This method returns a list of failed authentication attempts on behalf of an invalid identity 
     * {@link org.apache.directory.fortress.core.model.UserAudit#userId}, and given timestamp {@link UserAudit#beginDate}.  
     * If the {@link org.apache.directory.fortress.core.model.UserAudit#failedOnly} is true it will
     * return only authentication attempts made with invalid userId.  This event represents either User incorrectly entering 
     * userId during signon or possible fraudulent logon attempt by hostile agent.
     * <p>
     * This event is generated when Fortress looks up User record prior to LDAP bind operation.
     * <h3></h3>
     * <h4>optional parameters</h4>
     * <ul>
     *   <li>{@link UserAudit#userId} - contains the target userId</li>
     *   <li>{@link UserAudit#beginDate} - contains the date in which to begin search</li>
     *   <li>{@link UserAudit#failedOnly} - if set to 'true', return only failed authorization events</li>
     * </ul>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one failed authentication event.
     * @throws SecurityException
     *          if a runtime system error occurs.
     */
    List<AuthZ> searchInvalidUsers( UserAudit uAudit )
        throws SecurityException;
}
