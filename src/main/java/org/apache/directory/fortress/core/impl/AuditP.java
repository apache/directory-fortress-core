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
package org.apache.directory.fortress.core.impl;


import java.util.List;

import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.AuthZ;
import org.apache.directory.fortress.core.model.Bind;
import org.apache.directory.fortress.core.model.Mod;
import org.apache.directory.fortress.core.model.UserAudit;


/**
 * This class is process layer for Fortress audit data.  It performs data validation
 * and data mapping functions.
 * Process module for the for Fortress audit data.  It performs data validation and data mapping functions.
 * The audit data is passed using {@link org.apache.directory.fortress.core.model.AuthZ} class.  This class does perform simple data validations to ensure data reasonability and
 * the required fields are present..<BR>
 * The methods in this class are called by {@link AuditMgrImpl} methods during audit log interrogations.
 * <p>
 * Class will throw {@link SecurityException} to caller in the event of security policy, data constraint violation or system
 * error internal to DAO object. This class will forward DAO exception {@link org.apache.directory.fortress.core.FinderException},
 * or {@link org.apache.directory.fortress.core.ValidationException} as {@link SecurityException}s with appropriate
 * error id from {@link org.apache.directory.fortress.core.GlobalErrIds}.
 * <p>
 * This class performs simple data validations.
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class AuditP
{
    private AuditDAO aDao = new AuditDAO();


    /**
     * This method returns a list of authorization events for a particular user {@link org.apache.directory.fortress.core.model.UserAudit#userId}
     * and given timestamp field {@link org.apache.directory.fortress.core.model.UserAudit#beginDate}.<BR>
     * Method also can discriminate between all events or failed only by setting {@link org.apache.directory.fortress.core.model.UserAudit#failedOnly}.
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws SecurityException if a runtime system error occurs.
     */
    List<AuthZ> getAuthZs( UserAudit uAudit ) throws SecurityException
    {
        return aDao.getAllAuthZs( uAudit );
    }


    /**
     * This method returns a list of authorization events for a particular user {@link UserAudit#userId},
     * object {@link UserAudit#objName}, and given timestamp field {@link UserAudit#beginDate}.<BR>
     * Method also can discriminate between all events or failed only by setting flag {@link UserAudit#failedOnly}..
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws SecurityException if a runtime system error occurs.
     */
    List<AuthZ> searchAuthZs( UserAudit uAudit ) throws SecurityException
    {
        return aDao.searchAuthZs( uAudit );
    }


    /**
     * This method returns a list of authentication audit events for a particular user {@link UserAudit#userId},
     * and given timestamp field {@link UserAudit#beginDate}.<BR>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type Bind.  Each Bind object contains one bind event.
     * @throws SecurityException if a runtime system error occurs.
     */
    List<Bind> searchBinds( UserAudit uAudit ) throws SecurityException
    {
        return aDao.searchBinds( uAudit );
    }


    /**
     * This method returns a list of sessions created for a given user {@link UserAudit#userId},
     * and timestamp {@link UserAudit#beginDate}.<BR>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws SecurityException if a runtime system error occurs.
     */
    List<Mod> searchUserMods( UserAudit uAudit ) throws SecurityException
    {
        return aDao.searchUserMods( uAudit );
    }


    /**
     * This method returns a list of admin operations events for a particular entity {@link UserAudit#dn},
     * object {@link UserAudit#objName} and timestamp {@link UserAudit#beginDate}.  If the internal
     * userId {@link UserAudit#internalUserId} is set it will limit search by that field.
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one authorization event.
     * @throws SecurityException if a runtime system error occurs.
     */
    List<Mod> searchAdminMods( UserAudit uAudit ) throws SecurityException
    {
        return aDao.searchAdminMods( uAudit );
    }


    /**
     * This method returns a list of failed authentication events for a particular invalid user {@link UserAudit#userId},
     * and given timestamp {@link UserAudit#beginDate}.  If the {@link UserAudit#failedOnly} is true it will
     * return only authentication attempts made with invalid userId.
     * <p>
     * This is possible because Fortress performs read on user before the bind.
     * <p>
     *
     * @param uAudit This entity is instantiated and populated before invocation.
     * @return a List of objects of type AuthZ.  Each AuthZ object contains one failed authentication event.
     * @throws SecurityException if a runtime system error occurs.
     */
    List<AuthZ> searchInvalidAuthNs( UserAudit uAudit ) throws SecurityException
    {
        return aDao.searchInvalidAuthNs( uAudit );
    }
}
