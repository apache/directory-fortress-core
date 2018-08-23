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
package org.apache.directory.fortress.core.model;


import java.util.List;

/**
 * The Fortress Constraint interface prescribes attributes that are used to store, process and retrieve temporal validation attributes on
 * {@link org.apache.directory.fortress.core.model.User}, {@link org.apache.directory.fortress.core.model.UserRole}, {@link org.apache.directory.fortress.core.model.Role},
 * {@link org.apache.directory.fortress.core.model.AdminRole}, {@link org.apache.directory.fortress.core.model.UserAdminRole} entities.
 * <p>
 * <img src="../../doc-files/TemporalRbac.png" alt="">
 * <p>
 * <h3>Temporal Constraints on User and Role Assignments</h3>
 * In addition to the standard RBAC support, Fortress provides coverage for temporal constraints on role and user activation into session.
 * Temporal constraints affect when Users may activate Roles within runtime system at a particular point in time.  For example a nurse may be assigned to the "ChargeNurse" role but be limited as to when she is permitted to perform those duties, i.e. weekend graveyard shift.  Another example is a bank teller who is assigned to a "Teller" role but may only act within role between the hours of 9:00 to 5:00 on Monday thru Friday during normal business hours.
 * Additionally Fortress temporal constraints are checked during user authentication to control when a user is actually permitted to sign-on to a system.  The constraints may also be applied to enforce temporary blackout periods to cover vacations, leave of absences, sabbaticals, etc.
 * <p>
 * <h4>Constraint Schema</h4>
 * The entity maps to Fortress LDAP Schema object classes:
 * <p>
 * 1. ftRls Structural objectclass is used to store the Role information like name and temporal constraint attributes.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass    ( 1.3.6.1.4.1.38088.2.1</code>
 * <li> <code>NAME 'ftRls'</code>
 * <li> <code>DESC 'Fortress Role Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftRoleName )</code>
 * <li> <code>MAY ( description $ ftCstr ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p>
 * 2. ftUserAttrs is used to store user RBAC and Admin role assignment and other security attributes on User entity.
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
 * <p>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public interface Constraint
{
    /**
     * temporal boolean flag is used by internal Fortress components.
     *
     * @return boolean indicating if temporal constraints are placed on user.
     */
    boolean isTemporalSet();


    /**
     * Set the integer timeout that contains max time ((in minutes)) that entity may remain inactive.
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param timeout maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    void setTimeout( Integer timeout );


    /**
     * Set the begin time of day entity is allowed to be activated in system.  The format is military time - HHMM, i.e. 0800 (8:00 am) or 1700 (5:00 p.m.).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param beginTime maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    void setBeginTime( String beginTime );


    /**
     * Set the end time of day entity is allowed to be activated in system.  The format is military time - HHMM, i.e. 0000 (12:00 am) or 2359 (11:59 p.m.).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param endTime maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    void setEndTime( String endTime );


    /**
     * Set the beginDate when entity is allowed to be activated in system.  The format is - YYYYMMDD, i.e. 20100101 (January 1, 2001).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param beginDate maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    void setBeginDate( String beginDate );


    /**
     * Set the end date when entity is not allowed to be activated in system.  The format is - YYYYMMDD, i.e. 20100101 (January 1, 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param endDate maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    void setEndDate( String endDate );


    /**
     * Set the daymask that specifies what days of week entity is allowed to be activated in system.  The format is 1234567, i.e. 23456 (Monday, Tuesday, Wednesday, Thursday, Friday).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param dayMask maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    void setDayMask( String dayMask );


    /**
     * Set the begin lock date when entity is temporarily not allowed to be activated in system.  The format is - YYYYMMDD, 20100101 (January 1, 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param beginLockDate maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    void setBeginLockDate( String beginLockDate );


    /**
     * Set the end lock date when entity is allowed to be activated in system once again.  The format is - YYYYMMDD, i.e. 20100101 (January 1, 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @param endLockDate maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    void setEndLockDate( String endLockDate );


    /**
     * This is used internally by Fortress for Constraint operations.  Values set here by external caller will be ignored.
     *
     * @param name contains attribute used internally for constraint checking.
     */
    void setName( String name );


    /**
     * Required on DAO classes convert from raw data to object format.  Not intended for external use.
     *
     * @return String that maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    String getRawData();


    /**
     * Return the integer timeout that contains total time ((in minutes)) that entity may remain inactive.
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return int that maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    Integer getTimeout();


    /**
     * Contains the begin time of day entity is allowed to be activated in system.  The format is military time - HHMM, i.e. 0800 (8:00 am) or 1700 (5:00 p.m.).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return String that maps to 'ftCstr', 'ftRC', 'ftARC' attributes in 'ftUserAttrs' object class and 'ftCstr' attribute in 'ftRls' object class.
     */
    String getBeginTime();


    /**
     * Contains the end time of day entity is allowed to be activated in system.  The format is military time - HHMM, i.e. 0000 (12:00 am) or 2359 (11:59 p.m.).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return String that maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    String getEndTime();


    /**
     * Contains the begin date when entity is allowed to be activated in system.  The format is - YYYYMMDD, i.e. 20100101 (January 1, 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return String that maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    String getBeginDate();


    /**
     * Contains the end date when entity is allowed to be activated in system.  The format is - YYYYMMDD, i.e. 20101231 (December 31, 2011).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return String that maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    String getEndDate();


    /**
     * Contains the begin lock date when entity is temporarily not allowed to activated in system.  The format is - YYYYMMDD, i.e. 20100101 (January 1, 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return String that maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    String getBeginLockDate();


    /**
     * Contains the end lock date when entity is allowed to be activated in system once again.  The format is - YYYYMMDD, i.e. 20100101 (January 1, 2010).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return String that maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    String getEndLockDate();


    /**
     * Get the daymask that indicates what days of week entity is allowed to be activated in system.  The format is 1234567, i.e. 23456 (Monday, Tuesday, Wednesday, Thursday, Friday).
     * This attribute is optional but if set will be validated for reasonableness.
     *
     * @return String that maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    String getDayMask();


    /**
     * This is used internally by Fortress for Constraint operations.
     *
     * @return String that maps to {@code ftCstr}, {@code ftRC}, {@code ftARC} attributes in {@code ftUserAttrs} object class and {@code ftCstr} attribute in {@code ftRls} object class.
     */
    String getName();

    /**
     * Return a list of role constraints on entity.
     *
     * @return
     */
    List<RoleConstraint> getConstraints();

}